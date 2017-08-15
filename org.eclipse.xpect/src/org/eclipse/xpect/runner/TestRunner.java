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

import java.lang.reflect.InvocationTargetException;

import org.junit.runner.Description;
import org.eclipse.xpect.XjmTestMethod;
import org.eclipse.xpect.setup.ThisTestObject;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.state.StateContainer;

import com.google.common.base.Preconditions;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class TestRunner extends AbstractTestRunner {

	private XjmTestMethod method;

	private final StateContainer state;

	public TestRunner(StateContainer state, XpectFileRunner uriRunner, XjmTestMethod method) {
		super(uriRunner);
		Preconditions.checkNotNull(method);
		this.method = method;
		this.state = state;
	}

	@Creates
	public TestRunner create() {
		return this;
	}

	public Description createDescription() {
		Class<?> javaClass = getFileRunner().getJavaTestClass();
		String name = method.getName();
		return Description.createTestDescription(javaClass, name);
	}

	public XjmTestMethod getMethod() {
		return method;
	}

	@Override
	public StateContainer getState() {
		return state;
	}

	@Override
	protected void runInternal() throws Throwable {
		Object test = state.get(Object.class, ThisTestObject.class).get();
		// Object test = method.getTest().getJavaClass().newInstance();
		// ctx.setMethod(method);
		// ctx.setTestInstance(test);
		try {
			// if (setup != null)
			// ctx.setUserTestCtx(setup.beforeTest(ctx, ctx.getUserFileCtx()));
			method.getJavaMethod().invoke(test);
		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
		// finally {
		// if (setup != null)
		// setup.afterTest(ctx, ctx.getUserTestCtx());
		// }
	}

}
