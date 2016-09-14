/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.xpect.runner;

import org.junit.runner.Description;
import org.xpect.XjmXpectMethod;
import org.xpect.XpectInvocation;
import org.xpect.state.Creates;
import org.xpect.state.StateContainer;

import com.google.common.base.Preconditions;

/**
 * Runs a single test by retrieving arguments from parsers and state container. In order to call a test method, the arguments are to be computed. This is done via
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

	@Creates
	public XpectTestRunner create() {
		return this;
	}

	public Description createDescription() {
		XpectFileRunner runner = getFileRunner();
		Class<?> javaClass = runner.getJavaTestClass();
		Description description = DescriptionFactory.createTestDescription(javaClass, runner.getURIProvider(), invocation);
		return description;
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
		TestExecutor.runTest(state, invocation);
	}

}
