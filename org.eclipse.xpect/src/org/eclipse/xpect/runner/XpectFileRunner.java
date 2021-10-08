/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.eclipse.xpect.runner;

import static org.eclipse.xpect.runner.DescriptionFactory.createFileDescription;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.eclipse.xpect.XjmMethod;
import org.eclipse.xpect.XjmTestMethod;
import org.eclipse.xpect.XpectFile;
import org.eclipse.xpect.XpectInvocation;
import org.eclipse.xpect.XpectJavaModel;
import org.eclipse.xpect.setup.ThisRootTestClass;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.state.StateContainer;

import com.google.common.collect.Lists;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectFileRunner extends Runner implements Filterable, Sortable {
	private List<Runner> children;
	private Description description;
	private final StateContainer state;
	private final XpectFile xpectFile;

	public XpectFileRunner(StateContainer state, XpectFile file) {
		this.xpectFile = file;
		this.state = state;
	}

	@Creates
	public XpectFileRunner create() {
		return this;
	}

	protected List<Runner> createChildren() {
		List<Runner> children = Lists.newArrayList();
		if (xpectFile != null) {
			XpectJavaModel xjm = xpectFile.getJavaModel();
			for (XjmMethod method : xjm.getMethods().values())
				if (method instanceof XjmTestMethod) {
					Runner testRunner = createTestRunner((XjmTestMethod) method);
					if (testRunner != null)
						children.add(testRunner);
				}
			for (XpectInvocation inv : xpectFile.getInvocations()) {
				Runner testRunner = createTestRunner(inv);
				if (testRunner != null)
					children.add(testRunner);
			}
		}
		return children;
	}

	protected Description createDescription() {
		Description result = createFileDescription(getJavaTestClass(), getURIProvider(), getUri());
		for (Runner child : getChildren())
			result.addChild(child.getDescription());
		return result;
	}

	protected Runner createTestRunner(XjmTestMethod method) {
		StateContainer childState = TestExecutor.createState(state, TestExecutor.createTestConfiguration(method));
		return childState.get(TestRunner.class).get();
	}

	protected Runner createTestRunner(XpectInvocation invocation) {
		StateContainer childState = TestExecutor.createState(state, TestExecutor.createXpectConfiguration(invocation));
		return childState.get(XpectTestRunner.class).get();
	}

	public void filter(Filter filter) throws NoTestsRemainException {
		List<Runner> filtered = Lists.newArrayList();
		for (Runner child : getChildren())
			if (filter.shouldRun(child.getDescription()))
				filtered.add(child);
		this.description = null;
		this.children = filtered;
	}

	protected List<Runner> getChildren() {
		if (children == null)
			children = createChildren();
		return children;
	}

	public Description getDescription() {
		if (description == null)
			description = createDescription();
		return description;
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

	public void run(RunNotifier notifier) {
		try {
			state.get(ValidatingSetup.class).get().validate();
			if (getChildren().isEmpty()) {
				notifier.fireTestStarted(getDescription());
				notifier.fireTestFinished(getDescription());
			} else
				for (Runner child : getChildren())
					try {
						child.run(notifier);
					} catch (Throwable t) {
						notifier.fireTestFailure(new Failure(getDescription(), t));
					}
		} catch (Throwable t) {
			notifier.fireTestFailure(new Failure(getDescription(), t));
		} finally {
			try {
				state.invalidate();
			} catch (Throwable t) {
				notifier.fireTestFailure(new Failure(getDescription(), t));
			}
		}
	}

	public void sort(final Sorter sorter) {
		this.description = null;
		Collections.sort(getChildren(), new Comparator<Runner>() {
			public int compare(Runner o1, Runner o2) {
				return sorter.compare(o1.getDescription(), o2.getDescription());
			}
		});
	}

}
