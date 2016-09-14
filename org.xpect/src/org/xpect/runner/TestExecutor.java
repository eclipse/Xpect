package org.xpect.runner;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;
import org.xpect.XjmContribution;
import org.xpect.XjmTestMethod;
import org.xpect.XjmXpectMethod;
import org.xpect.XpectArgument;
import org.xpect.XpectFile;
import org.xpect.XpectInvocation;
import org.xpect.XpectJavaModel;
import org.xpect.expectation.impl.TargetSyntaxSupport;
import org.xpect.setup.ISetupInitializer;
import org.xpect.setup.ThisArgumentType;
import org.xpect.setup.ThisRootTestClass;
import org.xpect.setup.ThisTestClass;
import org.xpect.setup.ThisTestObject;
import org.xpect.setup.XpectSetupFactory;
import org.xpect.setup.ThisTestObject.TestObjectSetup;
import org.xpect.state.Configuration;
import org.xpect.state.Managed;
import org.xpect.state.ResolvedConfiguration;
import org.xpect.state.StateContainer;
import org.xpect.util.EnvironmentUtil;

import com.google.common.primitives.Primitives;

import junit.framework.AssertionFailedError;

public class TestExecutor {
	private static Object cast(Managed<?> value, XpectArgument expectedType) {
		if (value == null)
			throw new RuntimeException("Could not create value for " + expectedType.toString(true, true));
		Class<?> exactType = expectedType.getJavaType();
		Class<?> javaType = exactType.isPrimitive() ? Primitives.wrap(exactType) : exactType;
		if (javaType.isInstance(javaType))
			return javaType;
		Object object = value.get();
		if (object != null && !javaType.isInstance(object))
			throw new RuntimeException("Object of type " + object.getClass().getName() + " is not assignable to argument " + expectedType.toString(true, true));
		return object;
	}

	private static Configuration[] createArgumentConfigurations(XpectInvocation statement) {
		EList<XpectArgument> arguments = statement.getArguments();
		Configuration[] result = new Configuration[arguments.size()];
		for (int i = 0; i < arguments.size(); i++) {
			XpectArgument argument = arguments.get(i);
			Configuration configuration = new Configuration(argument.toString(false, true));
			configuration.addDefaultValue(XpectArgument.class, argument);
			configuration.addValue(ThisArgumentType.class, argument.getJavaType());
			result[i] = configuration;
		}
		return result;
	}

	private static StateContainer[] createArgumentStateContainers(StateContainer state, ResolvedConfiguration[] configurations) {
		StateContainer[] result = new StateContainer[configurations.length];
		for (int i = 0; i < configurations.length; i++)
			result[i] = new StateContainer(state, configurations[i]);
		return result;
	}

	private static Object[] createArgumentValues(StateContainer[] stateContainers, XpectInvocation invocation) {
		EList<XpectArgument> arguments = invocation.getArguments();
		Object[] result = new Object[stateContainers.length];
		for (int i = 0; i < stateContainers.length; i++) {
			StateContainer state = stateContainers[i];
			XpectArgument argument = arguments.get(i);
			try {
				Class<?> keyType = argument.getJavaType();
				Annotation keyAnnotation = argument.getStateAnnotation();
				Managed<?> managed = keyAnnotation != null ? state.get(keyType, keyAnnotation) : state.get(keyType);
				result[i] = cast(managed, argument);
			} catch (Throwable t) {
				throw new RuntimeException("Error creating value for argument " + argument.toString(true, true), t);
			}
		}
		return result;
	}

	public static Configuration createTestConfiguration(XjmTestMethod method) {
		Configuration config = new Configuration(method.getName() + "()");
		config.addValue(ThisTestClass.class, Class.class, method.getTest().getJavaClass());
		config.addDefaultValue(XjmTestMethod.class, method);
		return config;
	}

	public static Configuration createXpectConfiguration(XpectInvocation invocation) {
		Configuration config = new Configuration(invocation.getMethodName() + "(...)");
		config.addValue(ThisTestClass.class, Class.class, invocation.getMethod().getTest().getJavaClass());
		config.addDefaultValue(XpectInvocation.class, invocation);
		config.addDefaultValue(XjmXpectMethod.class, invocation.getMethod());
		return config;
	}

	public static Configuration createFileConfiguration(XpectFile file) {
		Configuration config = new Configuration(file.eResource().getURI().lastSegment());
		config.addDefaultValue(XpectFile.class, file);
		config.addDefaultValue(ISetupInitializer.class, file.createSetupInitializer());
		return config;
	}

	public static Configuration createRootConfiguration(XpectJavaModel model) {
		Configuration config = new Configuration("Root");
		config.addValue(ThisRootTestClass.class, model.getTestOrSuite().getJavaClass());
		config.addFactory(TestObjectSetup.class);
		config.addFactory(ValidatingSetup.class);
		config.addFactory(TargetSyntaxSupport.class);
		config.addFactory(ArgumentContributor.class);
		config.addDefaultValue(XpectJavaModel.class, model);
		Iterable<XjmContribution> contributions = model.getContributions(XpectSetupFactory.class, EnvironmentUtil.ENVIRONMENT);
		for (XjmContribution contribution : contributions)
			config.addFactory(contribution.getJavaClass());
		return config;
	}

	public static StateContainer createState(Configuration config) {
		return new StateContainer(new ResolvedConfiguration(config));
	}

	public static StateContainer createState(StateContainer state, Configuration config) {
		return new StateContainer(state, new ResolvedConfiguration(state.getConfiguration(), config));
	}

	private static ResolvedConfiguration[] resolveArgumentConfiguration(StateContainer state, Configuration[] configurations) {
		ResolvedConfiguration[] result = new ResolvedConfiguration[configurations.length];
		for (int i = 0; i < configurations.length; i++)
			result[i] = new ResolvedConfiguration(state.getConfiguration(), configurations[i]);
		return result;
	}

	public static void runTest(StateContainer state, XpectInvocation invocation) throws Throwable {
		Object test = state.get(Object.class, ThisTestObject.class).get();
		boolean fixmeMessage = false;
		try {
			ArgumentContributor contributor = state.get(ArgumentContributor.class).get();
			Configuration[] configurations = createArgumentConfigurations(invocation);
			contributor.contributeArguments(configurations);
			ResolvedConfiguration[] resolved = resolveArgumentConfiguration(state, configurations);
			StateContainer[] states = createArgumentStateContainers(state, resolved);
			try {
				Object[] args = createArgumentValues(states, invocation);

				invocation.getMethod().getJavaMethod().invoke(test, args);
				// reaching this point implies that no exception was thrown, hence the test passes.
				if (invocation.isFixme()) {
					fixmeMessage = true;
					throw new InvocationTargetException(new AssertionFailedError("Congrats, this FIXME test is suddenly fixed!"));
				}
			} finally {
				for (StateContainer s : states) {
					s.invalidate();
				}
			}
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (invocation.isFixme() && !fixmeMessage) {
				// FIXME-tests pass when they throw an exception
			} else {
				throw cause;
			}
		}

	}

}
