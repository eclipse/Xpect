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

package org.eclipse.xpect.xtext.lib.tests;

import java.util.Iterator;

import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.IScopeProvider;
import org.junit.runner.RunWith;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.expectation.CommaSeparatedValuesExpectation;
import org.eclipse.xpect.expectation.ICommaSeparatedValuesExpectation;
import org.eclipse.xpect.parameter.ParameterParser;
import org.eclipse.xpect.runner.LiveExecutionType;
import org.eclipse.xpect.runner.Xpect;
import org.eclipse.xpect.runner.XpectRunner;
import org.eclipse.xpect.xtext.lib.setup.ThisOffset;
import org.eclipse.xpect.xtext.lib.setup.XtextStandaloneSetup;
import org.eclipse.xpect.xtext.lib.setup.XtextWorkspaceSetup;
import org.eclipse.xpect.xtext.lib.util.XtextOffsetAdapter.ICrossEReferenceAndEObject;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.inject.Inject;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@RunWith(XpectRunner.class)
@XpectImport({ XtextStandaloneSetup.class, XtextWorkspaceSetup.class })
public class ScopingTest {

	protected static class EObjectDescriptionToStringMapper implements Function<IEObjectDescription, String> {
		public String apply(IEObjectDescription desc) {
			return desc.getName().toString();
		}
	}

	protected static class IsInScope implements Predicate<String> {
		private IQualifiedNameConverter converter;
		private IScope scope;

		public IsInScope(IQualifiedNameConverter converter, IScope scope) {
			super();
			this.converter = converter;
			this.scope = scope;
		}

		public boolean apply(String name) {
			QualifiedName qualifiedName = converter.toQualifiedName(name);
			IEObjectDescription singleElement = scope.getSingleElement(qualifiedName);
			return singleElement != null;
		}
	}

	protected static class ScopeAllElements implements Iterable<String> {
		private IScope scope;

		public ScopeAllElements(IScope scope) {
			super();
			this.scope = scope;
		}

		public Iterator<String> iterator() {
			return Iterators.transform(scope.getAllElements().iterator(), new EObjectDescriptionToStringMapper());
		}

	}

	@Inject
	private IQualifiedNameConverter converter;

	@Inject
	private IScopeProvider scopeProvider;

	public IQualifiedNameConverter getConverter() {
		return converter;
	}

	public IScopeProvider getScopeProvider() {
		return scopeProvider;
	}

	@Xpect(liveExecution = LiveExecutionType.FAST)
	@ParameterParser(syntax = "('at' arg1=OFFSET)?")
	public void scope( //
			@CommaSeparatedValuesExpectation ICommaSeparatedValuesExpectation expectation, //
			@ThisOffset ICrossEReferenceAndEObject arg1 //
	) {
		IScope scope = scopeProvider.getScope(arg1.getEObject(), arg1.getCrossEReference());
		expectation.assertEquals(new ScopeAllElements(scope), new IsInScope(converter, scope));
	}
}
