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

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.common.types.access.TypeResource;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.XtextResource;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.expectation.CommaSeparatedValuesExpectation;
import org.eclipse.xpect.expectation.ICommaSeparatedValuesExpectation;
import org.eclipse.xpect.expectation.IStringExpectation;
import org.eclipse.xpect.expectation.StringExpectation;
import org.eclipse.xpect.parameter.ParameterParser;
import org.eclipse.xpect.runner.LiveExecutionType;
import org.eclipse.xpect.runner.Xpect;
import org.eclipse.xpect.runner.XpectRunner;
import org.eclipse.xpect.xtext.lib.setup.ThisOffset;
import org.eclipse.xpect.xtext.lib.setup.XtextStandaloneSetup;
import org.eclipse.xpect.xtext.lib.setup.XtextWorkspaceSetup;
import org.eclipse.xpect.xtext.lib.util.XtextOffsetAdapter.ICrossEReferenceAndEObject;

import com.google.common.collect.Lists;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@SuppressWarnings("restriction")
@RunWith(XpectRunner.class)
@XpectImport({ XtextStandaloneSetup.class, XtextWorkspaceSetup.class })
public class LinkingTest {

	protected String deresolve(URI base, URI uri) {
		if (base.equals(uri.trimFragment()))
			return uri.fragment();
		return uri.deresolve(base).toString();
	}

	protected String getLinkedFragment(EObject targetObject, URI baseUri) {
		if (targetObject.eIsProxy())
			Assert.fail("Reference is a Proxy: " + ((InternalEObject) targetObject).eProxyURI());
		Resource targetResource = targetObject.eResource();
		if (targetResource == null)
			Assert.fail("Referenced EObject is not in a resource.");
		URI target = EcoreUtil.getURI(targetObject);
		return deresolve(baseUri, target);
	}

	@Xpect(liveExecution = LiveExecutionType.FAST)
	@ParameterParser(syntax = "('at' arg1=OFFSET)?")
	public void linkedFragment(IStringExpectation expectation, @ThisOffset ICrossEReferenceAndEObject arg1) {
		Object targetObject = arg1.getEObject().eGet(arg1.getCrossEReference());
		if (targetObject == null) {
			Assert.fail("Reference is null");
		} else if (targetObject instanceof EObject) {
			URI baseUri = arg1.getEObject().eResource().getURI();
			String actual = getLinkedFragment((EObject) targetObject, baseUri);
			expectation.assertEquals(actual);
		} else if (targetObject instanceof EList<?>) {
			Assert.fail("use 'XPECT linkedFragment' (plural)");
		}
	}

	@SuppressWarnings("unchecked")
	@Xpect(liveExecution = LiveExecutionType.FAST)
	@ParameterParser(syntax = "('at' arg1=OFFSET)?")
	public void linkedFragments(@CommaSeparatedValuesExpectation(ordered = true) ICommaSeparatedValuesExpectation expectation, @ThisOffset ICrossEReferenceAndEObject arg1) {
		Object targetObject = arg1.getEObject().eGet(arg1.getCrossEReference());
		if (targetObject == null) {
			Assert.fail("Reference is null");
		} else if (targetObject instanceof EObject) {
			Assert.fail("use 'XPECT linkedFragment' (singular)");
		} else if (targetObject instanceof EList<?>) {
			URI baseUri = arg1.getEObject().eResource().getURI();
			List<String> result = Lists.newArrayList();
			for (EObject target : (List<EObject>) targetObject) {
				String fragment = getLinkedFragment(target, baseUri);
				result.add(fragment);
			}
			expectation.assertEquals(result);
		}
	}

	@Xpect(liveExecution = LiveExecutionType.FAST)
	@ParameterParser(syntax = "('at' arg1=OFFSET)?")
	public void linkedName(@StringExpectation IStringExpectation expectation, @ThisOffset ICrossEReferenceAndEObject arg1) {
		EObject targetObject = (EObject) arg1.getEObject().eGet(arg1.getCrossEReference());
		if (targetObject == null)
			Assert.fail("Reference is null");
		if (targetObject.eIsProxy())
			Assert.fail("Reference is a Proxy: " + ((InternalEObject) targetObject).eProxyURI());
		Resource targetResource = targetObject.eResource();
		if (targetResource instanceof TypeResource)
			targetResource = arg1.getEObject().eResource();
		if (!(targetResource instanceof XtextResource))
			Assert.fail("Referenced EObject is not in an XtextResource.");
		IQualifiedNameProvider provider = ((XtextResource) targetResource).getResourceServiceProvider().get(IQualifiedNameProvider.class);
		QualifiedName name = provider.getFullyQualifiedName(targetObject);
		expectation.assertEquals(name);
	}
}
