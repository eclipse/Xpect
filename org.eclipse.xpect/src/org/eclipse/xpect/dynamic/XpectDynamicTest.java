package org.eclipse.xpect.dynamic;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.xpect.XjmTestMethod;
import org.eclipse.xpect.runner.ValidatingSetup;
import org.eclipse.xpect.runner.XpectTestGlobalState;
import org.eclipse.xpect.setup.ThisTestObject;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.state.StateContainer;
import org.junit.jupiter.api.DynamicTest;

import com.google.common.base.Preconditions;

public class XpectDynamicTest {

	private final String className;
	private XjmTestMethod method;
	private final StateContainer state;

	public XpectDynamicTest(StateContainer state, XjmTestMethod method) {
		Preconditions.checkNotNull(method);
		this.className = XpectTestGlobalState.INSTANCE.testClass().getName();
		this.method = method;
		this.state = state;
	}

	@Creates
	public XpectDynamicTest create() {
		return this;
	}

	public XjmTestMethod getMethod() {
		return method;
	}

	public StateContainer getState() {
		return state;
	}

	public DynamicTest test() {
		String testName = getName();
		return DynamicTest.dynamicTest(testName, () -> runInternal());
	}

	public String getName() {
		String testName = formatDisplayName(method.getName(), className);
		return testName;
	}

	protected void runInternal() throws Throwable {
		Object test = state.get(Object.class, ThisTestObject.class).get();
		try {
			method.getJavaMethod().invoke(test);
		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
	}

	private static String formatDisplayName(String name, String className) {
		return String.format("%s(%s)", name, className);
	}

}
