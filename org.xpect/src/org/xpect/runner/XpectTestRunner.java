/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.xpect.runner;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.eclipse.emf.common.util.EList;
import org.junit.runner.Description;
import org.xpect.XjmXpectMethod;
import org.xpect.XpectInvocation;
import org.xpect.model.XpectInvocationImplCustom;
import org.xpect.parameter.IParameterParser;
import org.xpect.parameter.IParameterProvider;
import org.xpect.parameter.ParameterParser;
import org.xpect.parameter.ParameterProvider;
import org.xpect.setup.ThisTestObject;
import org.xpect.state.Creates;
import org.xpect.state.StateContainer;

import com.google.common.base.Preconditions;

/**
 * Runs a single test by retrieving arguments from parsers and state container.
 * In order to call a test method, the arguments are to be computed. This is done via
 * {@link StateContainer#tryGet(Class, Object...)}.
 * 
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectTestRunner extends AbstractTestRunner {
	private final XpectInvocation invocation;
	private final StateContainer state;

	public XpectTestRunner(StateContainer state, XpectFileRunner uriRunner, XpectInvocation invocation) {
		super(uriRunner);
		Preconditions.checkNotNull(invocation);
		this.invocation = invocation;
		this.state = state;
	}

	protected List<IParameterProvider> collectParameters() {
		XjmXpectMethod method = getMethod();
		int count = method.getParameterCount();
		List<IParameterProvider> result = Arrays.asList(new IParameterProvider[count]);
		EList<IParameterProvider> parameters = getInvocation().getParameters();
		Annotation[][] annotations = method.getJavaMethod().getParameterAnnotations();
		Class<?>[] parameterTypes = method.getJavaMethod().getParameterTypes();
		for (int i = 0; i < count; i++) {
			if (parameters.get(i) != null)
				result.set(i, parameters.get(i));
			else {
				Class<?> expectedType = parameterTypes[i];
				IParameterProvider provider = state.tryGet(IParameterProvider.class, (Object[]) annotations[i]);
				if (provider == null) {
					Object value = state.tryGet(expectedType, (Object[]) annotations[i]);
					if (value != null)
						provider = new ParameterProvider(value);
				}
				if (provider != null) {
					result.set(i, ((XpectInvocationImplCustom) getInvocation()).adaptParameter(provider, expectedType));
					continue;
				}
			}
		}
		return result;
	}

	@Creates
	public XpectTestRunner create() {
		return this;
	}

	public Description createDescription() {
		XpectRunner runner = getFileRunner().getRunner();
		Class<?> javaClass = runner.getTestClass().getJavaClass();
		Description description = DescriptionFactory.createTestDescription(javaClass, runner.getUriProvider(), invocation);
		return description;
	}

	protected Object[] createParameterValues(List<IParameterProvider> proposedParameters) {
		XjmXpectMethod method = getMethod();
		Object[] params = new Object[method.getParameterCount()];
		for (int i = 0; i < method.getParameterCount(); i++) {
			Class<?>[] expectedTypes = method.getJavaMethod().getParameterTypes();
			if (proposedParameters.get(i) != null)
				params[i] = proposedParameters.get(i).get(expectedTypes[i], state);
		}
		return params;
	}

	public XpectInvocation getInvocation() {
		return invocation;
	}

	public XjmXpectMethod getMethod() {
		return invocation.getMethod();
	}

	@Override
	public StateContainer getState() {
		return state;
	}

	@Override
	protected boolean isIgnore() {
		return invocation.getFile().isIgnore() || invocation.isIgnore() || super.isIgnore();
	}

	@Override
	protected void runInternal() throws Throwable {
		Object test = state.get(Object.class, ThisTestObject.class).get();
		// Object test =
		// getInvocation().getMethod().getTest().getJavaClass().newInstance();
		// ctx.setXpectInvocation(getInvocation());
		// ctx.setMethod(getMethod());
		// ctx.setTestInstance(test);
		boolean fixmeMessage = false;
		try {
			// if (setup != null)
			// ctx.setUserTestCtx(setup.beforeTest(ctx, ctx.getUserFileCtx()));
			List<IParameterProvider> parameterProviders = collectParameters();
			Object[] params = createParameterValues(parameterProviders);
			getMethod().getJavaMethod().invoke(test, params);
			// reaching this point implies that no exception was thrown, hence the test passes.
			if( invocation.isFixme() ) {
				fixmeMessage = true;
				throw new InvocationTargetException( new AssertionFailedError("Congrats, this FIXME test is suddenly fixed!"));
			}
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if( invocation.isFixme() && ! fixmeMessage ) {
				// swallow 
			} else {
				// rethrow
				throw cause;
			}
		}
		// finally {
		// if (setup != null)
		// setup.afterTest(ctx, ctx.getUserTestCtx());
		// }
	}

}
