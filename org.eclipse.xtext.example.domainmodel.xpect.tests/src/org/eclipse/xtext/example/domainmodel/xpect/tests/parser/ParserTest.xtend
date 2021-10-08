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

package org.eclipse.xtext.example.domainmodel.xpect.tests.parser;

import org.eclipse.emf.ecore.EObject
import org.junit.Test
import org.junit.runner.RunWith
import org.eclipse.xpect.expectation.IStringExpectation
import org.eclipse.xpect.expectation.StringExpectation
import org.eclipse.xpect.runner.Xpect
import org.eclipse.xpect.runner.XpectRunner
import org.eclipse.xpect.runner.XpectTestFiles
import org.eclipse.xpect.setup.XpectSetup
import org.eclipse.xpect.xtext.lib.setup.ThisModel
import org.eclipse.xpect.xtext.lib.setup.XtextStandaloneSetup
import org.eclipse.xpect.xtext.lib.setup.XtextWorkspaceSetup
import org.eclipse.xpect.xtext.lib.util.EObjectFormatter

@RunWith(typeof(XpectRunner))
@XpectSetup(#[typeof(XtextStandaloneSetup), typeof(XtextWorkspaceSetup)])
@XpectTestFiles(fileExtensions="xt")
class ParserTest {

	@Xpect
	def void ast(@StringExpectation IStringExpectation expectation, @ThisModel EObject model) {
		val actual = new EObjectFormatter().resolveCrossReferences().format(model);
		expectation.assertEquals(actual);
	}

	@Test
	def void test() {
		// workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=381963
	}

}
