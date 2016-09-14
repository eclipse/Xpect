/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.xpect.ui.services;

import static org.xpect.runner.TestExecutor.createFileConfiguration;
import static org.xpect.runner.TestExecutor.createRootConfiguration;
import static org.xpect.runner.TestExecutor.createState;
import static org.xpect.runner.TestExecutor.createXpectConfiguration;
import static org.xpect.runner.TestExecutor.runTest;

import java.lang.annotation.Annotation;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.xpect.Environment;
import org.xpect.XpectFile;
import org.xpect.XpectInvocation;
import org.xpect.XpectJavaModel;
import org.xpect.parameter.IStatementRelatedRegion;
import org.xpect.registry.DefaultBinding;
import org.xpect.state.Configuration;
import org.xpect.state.StateContainer;
import org.xpect.ui.util.XpectFileAccess;
import org.xpect.util.EnvironmentUtil;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XtResourceValidator implements IResourceValidator {

	@Inject
	@DefaultBinding
	private IResourceValidator delegate;

	public IResourceValidator getDelegate() {
		return delegate;
	}

	public List<Issue> validate(Resource resource, CheckMode mode, CancelIndicator indicator) {
		XpectFile xpectFile = XpectFileAccess.getXpectFile(resource);
		Configuration fileConfig = createFileConfiguration(xpectFile);
		List<Issue> issues = Lists.newArrayList();
		List<Issue> delegateIssues = validateDelegate(resource, mode, indicator, fileConfig);
		if (delegateIssues != null) {
			issues.addAll(delegateIssues);
		}
		List<Issue> xpectIssues = validateXpect(resource, mode, indicator, fileConfig);
		if (xpectIssues != null) {
			issues.addAll(xpectIssues);
		}
		if (EnvironmentUtil.ENVIRONMENT == Environment.WORKBENCH) {
			List<Issue> testResultIssues = validateTests(resource, mode, indicator, fileConfig);
			issues.addAll(testResultIssues);
		}
		return issues;
	}

	public List<Issue> validateDelegate(Resource resource, CheckMode mode, CancelIndicator indicator, Configuration fileConfig) {
		List<Issue> issues = delegate.validate(resource, mode, indicator);
		List<Issue> result = Lists.newArrayListWithExpectedSize(issues.size());
		for (Issue issue : issues)
			if (issue != null)
				result.add(issue);
		return result;
	}

	public List<Issue> validateXpect(Resource resource, CheckMode mode, CancelIndicator indicator, Configuration fileConfig) {
		XtextResource xpectResource = XpectFileAccess.getXpectResource(resource);
		IResourceValidator validator = xpectResource.getResourceServiceProvider().getResourceValidator();
		return validator.validate(xpectResource, mode, indicator);
	}

	protected void configureTests(Resource resource, CheckMode mode, CancelIndicator indicator, Configuration fileConfig) {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends Annotation> thisResource = (Class<? extends Annotation>) Platform.getBundle("org.xpect.xtext.lib").loadClass("org.xpect.xtext.lib.setup.ThisResource");
			fileConfig.addValue(thisResource, XtextResource.class, (XtextResource) resource);
		} catch (ClassNotFoundException e1) {
			Throwables.propagate(e1);
		}
	}

	protected List<Issue> validateTests(Resource resource, CheckMode mode, CancelIndicator indicator, Configuration fileConfig) {
		List<Issue> result = Lists.newArrayList();
		XpectFile xpectFile = XpectFileAccess.getXpectFile(resource);
		if (xpectFile != null) {
			XpectJavaModel javaModel = xpectFile.getJavaModel();
			if (javaModel != null && javaModel.getTestOrSuite() != null && javaModel.getTestOrSuite().getJavaClass() != null) {
				configureTests(resource, mode, indicator, fileConfig);
				StateContainer rootState = createState(createRootConfiguration(javaModel));
				StateContainer fileState = createState(rootState, fileConfig);
				for (XpectInvocation inv : xpectFile.getInvocations()) {
					StateContainer invState = createState(fileState, createXpectConfiguration(inv));
					try {
						runTest(invState, inv);
					} catch (Throwable e) {
						Issue.IssueImpl issue = new Issue.IssueImpl();
						issue.setMessage(e.getMessage());
						IStatementRelatedRegion region = inv.getExtendedRegion();
						issue.setOffset(region.getOffset());
						issue.setLength(region.getLength());
						result.add(issue);
					} finally {
						invState.invalidate();
					}
				}
			}
		}
		return result;
	}
}