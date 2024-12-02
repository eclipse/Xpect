package org.eclipse.xpect.dynamic;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xpect.XjmXpectMethod;
import org.eclipse.xpect.XpectInvocation;
import org.eclipse.xpect.runner.DescriptionFactory;
import org.eclipse.xpect.runner.IXpectURIProvider;
import org.eclipse.xpect.runner.TestExecutor;
import org.eclipse.xpect.runner.XpectTestGlobalState;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.state.StateContainer;
import org.junit.AssumptionViolatedException;
import org.junit.jupiter.api.DynamicTest;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class XpectInvocationDynamicTest {

	private final Class<?> testClass;
	private final XpectInvocation invocation;
	private final StateContainer state;

	public XpectInvocationDynamicTest(StateContainer state, XpectInvocation invocation) {
		Preconditions.checkNotNull(invocation);
		this.testClass = XpectTestGlobalState.INSTANCE.testClass();
		this.invocation = invocation;
		this.state = state;
	}

	@Creates
	public XpectInvocationDynamicTest create() {
		return this;
	}

	public DynamicTest test() {
		String testName = getName();
		return DynamicTest.dynamicTest(testName, () -> runInternal());
	}

	public XpectInvocation getInvocation() {
		return invocation;
	}

	public XjmXpectMethod getMethod() {
		return invocation.getMethod();
	}

	public StateContainer getState() {
		return state;
	}

	protected boolean isIgnore() {
		return invocation.getFile().isIgnore() || invocation.isIgnore();
	}

	protected void runInternal() throws Throwable {
		if (isIgnore()) {
			throw new AssumptionViolatedException("Test is ignored");
		}
		TestExecutor.runTest(state, invocation);
	}

	public IXpectURIProvider getURIProvider() {
		return state.get(IXpectURIProvider.class).get();
	}

	public String getName() {
		IXpectURIProvider uriProvider = getURIProvider();
		String testClassName = testClass.getName();
		return getTestNameForInvocation(uriProvider, testClassName, invocation);
	}

	private static String getTestNameForInvocation(IXpectURIProvider uriProvider, String testClassName, XpectInvocation invocation) {
		URI uri = uriProvider.deresolveToProject(EcoreUtil.getURI(invocation));
		String title = DescriptionFactory.getTitle(invocation);
		List<String> ret = DescriptionFactory.extractXpectMethodNameAndResourceURI(uri.toString());
		String fragmentToXpectMethod = ret.get(0);
		String pathToResource = ret.get(1);

		String text = fragmentToXpectMethod;

		if (!Strings.isNullOrEmpty(title))
			text = text + ": " + title;
		// The test name has the following format
		// 		errors~0: This is a comment 〔path/to/file.xt〕
		return formatDisplayName(text + " \u3014" + pathToResource  + "\u3015", testClassName);
	}

	private static String formatDisplayName(String name, String className) {
		return String.format("%s(%s)", name, className);
	}
}
