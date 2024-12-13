package org.eclipse.xpect.dynamic;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.xpect.XpectFile;
import org.eclipse.xpect.XpectJavaModel;
import org.eclipse.xpect.XpectStandaloneSetup;
import org.eclipse.xpect.registry.ITestSuiteInfo;
import org.eclipse.xpect.runner.IXpectURIProvider;
import org.eclipse.xpect.runner.TestExecutor;
import org.eclipse.xpect.runner.ValidatingSetup;
import org.eclipse.xpect.runner.XpectTestFiles;
import org.eclipse.xpect.runner.XpectTestGlobalState;
import org.eclipse.xpect.runner.XpectURIProvider;
import org.eclipse.xpect.runner.XpectTestFiles.Builder;
import org.eclipse.xpect.runner.XpectTestFiles.FileRoot;
import org.eclipse.xpect.state.Configuration;
import org.eclipse.xpect.state.ResolvedConfiguration;
import org.eclipse.xpect.state.StateContainer;
import org.eclipse.xpect.util.AnnotationUtil;
import org.eclipse.xpect.util.IssueVisualizer;
import org.eclipse.xpect.util.XpectJavaModelManager;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceFactory;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.junit.ComparisonFailure;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import com.google.inject.Injector;

// a lot of copy-pasting from XpectRunner, however XpectRunner has protected methods that can use different model, injector, etc. via overriding getters, so its hard to extract code without causing damage
class XpectDynamicTestFactory {

	public static Stream<DynamicNode> xpectTests(Class<?> testClass) {
		XpectDynamicTestFactory factory = new XpectDynamicTestFactory(testClass);
		return factory.getChildren();
	}

	private Stream<DynamicNode> children;
	private Collection<URI> files;
	private final Class<?> testClass;
	private final StateContainer state;
	private final IXpectURIProvider uriProvider;
	private final Injector xpectInjector;
	private final XpectJavaModel xpectJavaModel;

	public XpectDynamicTestFactory(Class<?> testClass) {
		this.testClass = testClass;
		this.uriProvider = findUriProvider(testClass);
		this.xpectInjector = findXpectInjector();
		this.xpectJavaModel = XpectJavaModelManager.createJavaModel(testClass);
		/*
		 * NOTE:
		 * Do this before the state creation, otherwise the parts that depend on
		 * the singleton won't initialize properly and test will fail to run!
		 */
		XpectTestGlobalState.INSTANCE.set(xpectJavaModel, testClass);
		this.state = TestExecutor.createState(createRootConfiguration());
	}

	protected DynamicNode createChild(URI uri) {
		try {
			XtextResource resource = loadXpectResource(uri);
			XpectFile file = loadXpectFile(resource);
			Configuration cfg = createChildConfiguration(file);
			StateContainer childState = new StateContainer(state, new ResolvedConfiguration(state.getConfiguration(), cfg));
			XpectDynamicTestCase testCase = childState.get(XpectDynamicTestCase.class).get();
			List<DynamicTest> tests = testCase.getChildren();
			if (tests.isEmpty()) {
				// if there are no tests in the test case, we only validate the setup
				return DynamicTest.dynamicTest(testCase.getName(), () -> {
					try {
						testCase.getState().get(ValidatingSetup.class).get().validate();
						testCase.setUp();
					} finally {
						try {
							testCase.tearDown();
						} finally {
							testCase.getState().invalidate();
						}
					}
				});
			}
			return DynamicContainer.dynamicContainer(testCase.getName(), tests);
		} catch (IOException e) {
			throw new AssertionError("Failed to create Xpect tests for URI: " + uri, e);
		}
	}

	protected Configuration createChildConfiguration(XpectFile file) {
		return TestExecutor.createFileConfiguration(file);
	}

	protected Stream<DynamicNode> createChildren(Class<?> clazz) {
		Collection<URI> fileUris = getFiles();
		// lazy stream
		Stream<DynamicNode> tests = fileUris.stream().map(uri -> createChild(uri));
		return tests;
	}

	protected Configuration createRootConfiguration() {
		Configuration config = TestExecutor.createRootConfiguration(this.xpectJavaModel);
		config.addDefaultValue(this);
		config.addDefaultValue(IXpectURIProvider.class, this.uriProvider);
		config.addFactory(XpectDynamicTestCase.class);
		config.addFactory(XpectInvocationDynamicTest.class);
		config.addFactory(XpectDynamicTest.class);
		return config;
	}

	protected IXpectURIProvider findUriProvider(Class<?> clazz) {
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

	// lazy stream, since otherwise preparation for each test runs as the tests are generated, not when each test eventually runs
	public Stream<DynamicNode> getChildren() {
		if (children == null)
			children = createChildren(testClass);
		return children;
	}

	protected Collection<URI> getFiles() {
		if (files == null)
			files = uriProvider.getAllURIs();
		return files;
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
		XtextResourceFactory xtextResourceFactory = xpectInjector.getInstance(XtextResourceFactory.class);
		XtextResource resource = (XtextResource) xtextResourceFactory.createResource(uri);
		xpectJavaModel.eResource().getResourceSet().getResources().add(resource);
		resource.load(null);
		return resource;
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
