package org.eclipse.xpect.dynamic;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xpect.XjmTestMethod;
import org.eclipse.xpect.XpectFile;
import org.eclipse.xpect.XpectInvocation;
import org.eclipse.xpect.XpectJavaModel;
import org.eclipse.xpect.runner.IXpectURIProvider;
import org.eclipse.xpect.runner.TestExecutor;
import org.eclipse.xpect.runner.ValidatingSetup;
import org.eclipse.xpect.setup.ThisRootTestClass;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.state.StateContainer;
import org.junit.jupiter.api.DynamicTest;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class XpectDynamicTestCase {
	private List<DynamicTest> children;
	private final StateContainer state;
	private final XpectFile xpectFile;

	public XpectDynamicTestCase(StateContainer state, XpectFile file) {
		this.xpectFile = file;
		this.state = state;
	}

	@Creates
	public XpectDynamicTestCase create() {
		return this;
	}

	/**
	 * <p>
	 * To run code before an Xpect test begins, extend this class and annotate the extending class with:
	 * {@code @org.eclipse.xpect.XpectReplace(org.eclipse.xpect.dynamic.XpectDynamicTestCase)}
	 * </p>
	 * <p>
	 * The extended class must then be included in the values of the annotation of the test case:
	 * {@code @org.eclipse.xpect.XpectImport({...class})}
	 * </p>
	 */
	public void setUp() throws Throwable {
		// nothing to set up
	}

	/**
	 * <p>
	 * To run code after an Xpect test finishes, extend this class and annotate the extending class with:
	 * {@code @org.eclipse.xpect.XpectReplace(org.eclipse.xpect.dynamic.XpectDynamicTestCase)}
	 * </p>
	 * <p>
	 * The extended class must then be included in the values of the annotation of the test case:
	 * {@code @org.eclipse.xpect.XpectImport({...class})}
	 * </p>
	 */
	public void tearDown() throws Throwable {
		// nothing to tear down
	}

	protected List<DynamicTest> createChildren() {
		List<DynamicTest> tests = Lists.newArrayList();
		if (xpectFile != null) {
			/*
			 * With JUnit 4 runners, we can do setup validation before children run and state clean-up after children are done.
			 * With JUnit 5 we cannot.
			 * So the first test does setup validation and the last test does clean-up.
			 * Meaning their execution times will likely increase notably, when compared to the JUnit 4 reported time.
			 * Alternatively we can add a first and last test, for setup validation resp. clean-up. This means extra test nodes in the result, as well as extra test results. We then also have the problem of running tests if the setup validation failed.
			 * XXX: does JUnit 5 offer anything for dynamic tests, to improve this situation?
			 * As of writing this code, @BeforeEach and @AfterEach are only called before and after the factory method runs.
			 * We have a factory method with every URL we must run an Xpect test for, as those URLs are known only via an annotation.
			 */
			AtomicBoolean setupValidated = new AtomicBoolean(false);
			AtomicBoolean setupValidationFailed = new AtomicBoolean(false);
			AtomicInteger finishedChildren = new AtomicInteger(0);
			XpectJavaModel xjm = xpectFile.getJavaModel();
			XjmTestMethod[] methods = xjm.getMethods().values().stream().filter(m -> m instanceof XjmTestMethod).toArray(XjmTestMethod[]::new);
			XpectInvocation[] invocations = Iterables.toArray(xpectFile.getInvocations(), XpectInvocation.class);
			int childrenCount = methods.length + invocations.length;
			for (XjmTestMethod method : methods) {
					DynamicTest test = createDynamicTest(method);
					tests.add(wrapTest(test, childrenCount, setupValidated, setupValidationFailed, finishedChildren));
			}
			for (XpectInvocation inv : invocations) {
				DynamicTest test = createDynamicTest(inv);
				tests.add(wrapTest(test, childrenCount, setupValidated, setupValidationFailed, finishedChildren));
			}
		}
		return tests;
	}

	protected DynamicTest createDynamicTest(XjmTestMethod method) {
		StateContainer childState = TestExecutor.createState(state, TestExecutor.createTestConfiguration(method));
		return childState.get(XpectDynamicTest.class).get().test();
	}

	protected DynamicTest createDynamicTest(XpectInvocation invocation) {
		StateContainer childState = TestExecutor.createState(state, TestExecutor.createXpectConfiguration(invocation));
		DynamicTest test = childState.get(XpectInvocationDynamicTest.class).get().test();
		return test;
	}

	protected DynamicTest wrapTest(DynamicTest test, final int childrenCount, AtomicBoolean validatedSetup, AtomicBoolean setupValidationFailed, AtomicInteger finishedChildren) {
		return DynamicTest.dynamicTest(test.getDisplayName(), () -> {
			try {
				if (!validatedSetup.getAndSet(true)) {
					// first test is running, validate setup
					try {
						state.get(ValidatingSetup.class).get().validate();
					} catch (Throwable t) {
						setupValidationFailed.set(true);
						throw t;
					}
				}
				if (setupValidationFailed.get()) {
					throw new AssertionError("Setup validation failed");
				}
				try {
					setUp();
					test.getExecutable().execute();
				} finally {
					tearDown();
				}
			} finally {
				int finished = finishedChildren.incrementAndGet();
				if (finished >= childrenCount) {
					// last test is done, do clean-up
					state.invalidate();
				}
			}
		});
	}

	protected List<DynamicTest> getChildren() {
		if (children == null)
			children = createChildren();
		return children;
	}

	public Class<?> getJavaTestClass() {
		return state.get(Class.class, ThisRootTestClass.class).get();
	}

	public IXpectURIProvider getURIProvider() {
		return state.get(IXpectURIProvider.class).get();
	}

	public StateContainer getState() {
		return state;
	}

	public URI getUri() {
		return xpectFile.eResource().getURI();
	}

	public XpectFile getXpectFile() {
		return xpectFile;
	}

	public String getName() {
		IXpectURIProvider uriProvider = getURIProvider();
		URI uri = getUri();
		URI deresolved = uriProvider.deresolveToProject(uri);
		String pathInProject = deresolved.trimSegments(1).toString();
		String fileName = deresolved.lastSegment();
		return fileName + ": " + pathInProject;
	}
}
