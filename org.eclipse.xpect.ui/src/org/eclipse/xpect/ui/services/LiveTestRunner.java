/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.eclipse.xpect.ui.services;

import static org.eclipse.xpect.runner.TestExecutor.createRootConfiguration;
import static org.eclipse.xpect.runner.TestExecutor.createState;
import static org.eclipse.xpect.runner.TestExecutor.createXpectConfiguration;
import static org.eclipse.xpect.runner.TestExecutor.runTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.resource.ProjectByResourceProvider;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.CollectionBasedAcceptor;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.CheckType;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xpect.XjmXpectMethod;
import org.eclipse.xpect.XpectFile;
import org.eclipse.xpect.XpectInvocation;
import org.eclipse.xpect.XpectJavaModel;
import org.eclipse.xpect.runner.Xpect;
import org.eclipse.xpect.state.Configuration;
import org.eclipse.xpect.state.StateContainer;
import org.eclipse.xpect.ui.preferences.XpectRootPreferencePage;
import org.eclipse.xpect.ui.util.XpectFileAccess;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class LiveTestRunner {

	@Inject
	private ProjectByResourceProvider projectByResourceProvider;

	@Inject
	private LiveTestIssueFactory issueFactory;

	protected void configureTests(Resource resource, CheckMode mode, CancelIndicator indicator, Configuration fileConfig) {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends Annotation> thisResource = (Class<? extends Annotation>) Platform.getBundle("org.eclipse.xpect.xtext.lib").loadClass("org.eclipse.xpect.xtext.lib.setup.ThisResource");
			fileConfig.addValue(thisResource, XtextResource.class, (XtextResource) resource);
		} catch (ClassNotFoundException e1) {
			Throwables.propagate(e1);
		}
	}

	public List<Issue> validateTests(Resource resource, CheckMode mode, CancelIndicator indicator, Configuration fileConfig) {
		IProject project = projectByResourceProvider.getProjectContext(resource);
		if (project == null || !XpectRootPreferencePage.isLiveTestExecutionEnabled(project)) {
			return Collections.emptyList();
		}
		XpectFile xpectFile = XpectFileAccess.getXpectFile(resource);
		if (xpectFile == null || xpectFile.isIgnore()) {
			return Collections.emptyList();
		}
		List<XpectInvocation> invocations = Lists.newArrayList();
		for (XpectInvocation inv : xpectFile.getInvocations()) {
			if (!inv.isIgnore()) {
				XjmXpectMethod method = inv.getMethod();
				if (method != null && !method.eIsProxy()) {
					Method javaMethod = method.getJavaMethod();
					if (javaMethod != null) {
						Xpect annotation = javaMethod.getAnnotation(Xpect.class);
						CheckType checkType = annotation.liveExecution().toCheckType();
						if (annotation != null && checkType != null && mode.shouldCheck(checkType)) {
							invocations.add(inv);
						}
					}
				}
			}
		}
		if (invocations.isEmpty()) {
			return Collections.emptyList();
		}
		List<Issue> result = Lists.newArrayList();
		XpectJavaModel javaModel = xpectFile.getJavaModel();
		if (javaModel != null && javaModel.getTestOrSuite() != null && javaModel.getTestOrSuite().getJavaClass() != null) {
			configureTests(resource, mode, indicator, fileConfig);
			StateContainer rootState = createState(createRootConfiguration(javaModel));
			StateContainer fileState = createState(rootState, fileConfig);
			for (XpectInvocation inv : invocations) {
				StateContainer invState = createState(fileState, createXpectConfiguration(inv));
				try {
					runTest(invState, inv);
					result.add(issueFactory.createSuccessIssue(inv));
				} catch (Throwable e) {
					issueFactory.exceptionToIssues(inv, e, CollectionBasedAcceptor.of(result));
				} finally {
					invState.invalidate();
				}
			}
		}
		return result;
	}
}
