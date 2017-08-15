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

package org.eclipse.xpect.runner;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceFactory;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.junit.ComparisonFailure;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.eclipse.xpect.XpectFile;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.XpectJavaModel;
import org.eclipse.xpect.XpectStandaloneSetup;
import org.eclipse.xpect.registry.ITestSuiteInfo;
import org.eclipse.xpect.runner.XpectTestFiles.Builder;
import org.eclipse.xpect.runner.XpectTestFiles.FileRoot;
import org.eclipse.xpect.state.Configuration;
import org.eclipse.xpect.state.ResolvedConfiguration;
import org.eclipse.xpect.state.StateContainer;
import org.eclipse.xpect.util.AnnotationUtil;
import org.eclipse.xpect.util.IssueVisualizer;
import org.eclipse.xpect.util.XpectJavaModelManager;

import com.google.common.collect.Lists;
import com.google.inject.Injector;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectImport(TestTitleProvider.class)
public class XpectRunner extends ParentRunner<Runner> {

	public static XpectRunner INSTANCE = null;
	public static ClassLoader testClassloader = null;
	private List<Runner> children;
	private Collection<URI> files;
	private final StateContainer state;
	private final IXpectURIProvider uriProvider;
	private final Injector xpectInjector;
	private final XpectJavaModel xpectJavaModel;

	public XpectRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
		INSTANCE = this;
		testClassloader = testClass.getClassLoader();
		this.uriProvider = findUriProvider(testClass);
		this.xpectInjector = findXpectInjector();
		this.xpectJavaModel = XpectJavaModelManager.createJavaModel(testClass);
		this.state = TestExecutor.createState(createRootConfiguration());
	}

	protected Runner createChild(URI uri) {
		try {
			XtextResource resource = loadXpectResource(uri);
			XpectFile file = loadXpectFile(resource);
			Configuration cfg = createChildConfiguration(file);
			StateContainer childState = new StateContainer(state, new ResolvedConfiguration(state.getConfiguration(), cfg));
			return childState.get(XpectFileRunner.class).get();
		} catch (Throwable t) {
			return new ErrorReportingRunner(this, uri, t);
		}
	}

	protected Configuration createChildConfiguration(XpectFile file) {
		return TestExecutor.createFileConfiguration(file);
	}

	protected List<Runner> createChildren(Class<?> clazz) {
		List<Runner> result = Lists.newArrayList();
		for (URI uri : getFiles())
			result.add(createChild(uri));
		return result;
	}

	protected Configuration createRootConfiguration() {
		Configuration config = TestExecutor.createRootConfiguration(this.xpectJavaModel);
		config.addDefaultValue(this);
		config.addDefaultValue(IXpectURIProvider.class, this.uriProvider);
		config.addFactory(XpectFileRunner.class);
		config.addFactory(XpectTestRunner.class);
		config.addFactory(TestRunner.class);
		return config;
	}

	@Override
	protected Description describeChild(Runner child) {
		return child.getDescription();
	}

	protected IXpectURIProvider findUriProvider(Class<?> clazz) throws InitializationError {
		String baseDir = System.getProperty("xpectBaseDir");
		String files = System.getProperty("xpectFiles");
		if (!Strings.isEmpty(baseDir) || !Strings.isEmpty(files)) {
			Builder builder = new XpectTestFiles.Builder().relativeTo(FileRoot.PROJECT);
			if (!Strings.isEmpty(baseDir))
				builder.withBaseDir(baseDir);
			if (files != null)
				for (String file : files.split(";")) {
					String trimmed = file.trim();
					if (!"".equals(trimmed))
						builder.addFile(trimmed);
				}
			return builder.create(clazz);

		}
		IXpectURIProvider provider = AnnotationUtil.newInstanceViaMetaAnnotation(clazz, XpectURIProvider.class, IXpectURIProvider.class);
		if (provider != null)
			return provider;
		return new XpectTestFiles.Builder().relativeTo(FileRoot.CLASS).create(clazz);
	}

	protected Injector findXpectInjector() {
		IResourceServiceProvider rssp = IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI.createURI("foo.xpect"));
		if (rssp != null)
			return rssp.get(Injector.class);
		if (!EcorePlugin.IS_ECLIPSE_RUNNING)
			return new XpectStandaloneSetup().createInjectorAndDoEMFRegistration();
		throw new IllegalStateException("The language *.xpect is not activated");
	}

	@Override
	public List<Runner> getChildren() {
		if (children == null)
			children = createChildren(getTestClass().getJavaClass());
		return children;
	}

	protected Collection<URI> getFiles() {
		if (files == null)
			files = uriProvider.getAllURIs();
		return files;
	}

	public StateContainer getState() {
		return state;
	}

	public IXpectURIProvider getUriProvider() {
		return uriProvider;
	}

	protected Injector getXpectInjector() {
		return xpectInjector;
	}

	public XpectJavaModel getXpectJavaModel() {
		return xpectJavaModel;
	}

	protected XpectFile loadXpectFile(XtextResource res) throws IOException {
		XpectFile file = !res.getContents().isEmpty() ? (XpectFile) res.getContents().get(0) : null;
		if (file == null)
			throw new IllegalStateException("Resource for " + res.getURI() + " is empty.");
		validate(file);
		validate(res);
		return file;
	}

	protected XtextResource loadXpectResource(URI uri) throws IOException {
		XtextResource resource = (XtextResource) getXpectInjector().getInstance(XtextResourceFactory.class).createResource(uri);
		getXpectJavaModel().eResource().getResourceSet().getResources().add(resource);
		resource.load(null);
		return resource;
	}

	@Override
	public void run(RunNotifier notifier) {
		if (getChildren().isEmpty()) {
			notifier.fireTestRunStarted(getDescription());
			notifier.fireTestFailure(new Failure(getDescription(), new RuntimeException("No Tests found via " + getUriProvider())));
		} else {
			try {
				super.run(notifier);
			} finally {
				try {
					state.invalidate();
				} catch (Throwable t) {
					notifier.fireTestFailure(new Failure(getDescription(), t));
				}
			}
		}
	}

	@Override
	protected void runChild(Runner child, RunNotifier notifier) {
		try {
			child.run(notifier);
		} catch (Throwable t) {
			notifier.fireTestFailure(new Failure(child.getDescription(), t));
		}
	}

	protected void validate(XpectFile file) {
		XpectJavaModel model = file.getJavaModel();
		if (model == null || model.eIsProxy()) {
			String fileName = file.eResource().getURI().lastSegment();
			String registry = ITestSuiteInfo.Registry.INSTANCE.toString();
			throw new IllegalStateException("Could not find test suite for " + fileName + ". Registry:\n" + registry);
		}
	}

	protected void validate(XtextResource res) {
		IResourceValidator validator = res.getResourceServiceProvider().get(IResourceValidator.class);
		List<Issue> issues = validator.validate(res, CheckMode.ALL, CancelIndicator.NullImpl);
		if (!issues.isEmpty()) {
			String document = res.getParseResult().getRootNode().getText();
			String errors = new IssueVisualizer().visualize(document, issues);
			throw new ComparisonFailure("Errors in " + res.getURI(), document.trim(), errors.trim());
		}
	}
}
