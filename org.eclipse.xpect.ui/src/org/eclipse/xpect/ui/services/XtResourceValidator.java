/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.eclipse.xpect.ui.services;

import static org.eclipse.xpect.runner.TestExecutor.createFileConfiguration;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xpect.Environment;
import org.eclipse.xpect.XpectFile;
import org.eclipse.xpect.registry.DefaultBinding;
import org.eclipse.xpect.state.Configuration;
import org.eclipse.xpect.ui.util.XpectFileAccess;
import org.eclipse.xpect.util.EnvironmentUtil;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XtResourceValidator implements IResourceValidator {

	@Inject
	@DefaultBinding
	private IResourceValidator delegate;

	@Inject
	private Injector injector;

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
			LiveTestRunner runner = injector.getInstance(LiveTestRunner.class);
			List<Issue> testResultIssues = runner.validateTests(resource, mode, indicator, fileConfig);
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

}
