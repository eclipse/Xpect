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

package org.eclipse.xpect.xtext.lib.tests.util.grammaranalyzer;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Grammar;
import org.junit.runner.RunWith;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.expectation.ILinesExpectation;
import org.eclipse.xpect.expectation.LinesExpectation;
import org.eclipse.xpect.runner.Xpect;
import org.eclipse.xpect.runner.XpectRunner;
import org.eclipse.xpect.xtext.lib.setup.ThisModel;
import org.eclipse.xpect.xtext.lib.setup.XtextStandaloneSetup;
import org.eclipse.xpect.xtext.lib.setup.XtextWorkspaceSetup;
import org.eclipse.xpect.xtext.lib.util.GrammarAnalyzer;
import org.eclipse.xpect.xtext.lib.util.GrammarAnalyzer.CommentRule;

import com.google.common.collect.Lists;

@RunWith(XpectRunner.class)
@XpectImport({ XtextStandaloneSetup.class, XtextWorkspaceSetup.class })
public class GrammarAnalyzerTest {
	@Xpect
	public void comments(@LinesExpectation ILinesExpectation expectation, @ThisModel EObject grammar) {
		List<String> actual = Lists.newArrayList();
		for (CommentRule comment : new GrammarAnalyzer((Grammar) grammar).getCommentRules())
			actual.add(comment.toString());
		expectation.assertEquals(actual);
	}
}
