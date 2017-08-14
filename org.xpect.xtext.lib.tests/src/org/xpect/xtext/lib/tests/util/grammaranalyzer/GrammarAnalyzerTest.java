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

package org.xpect.xtext.lib.tests.util.grammaranalyzer;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Grammar;
import org.junit.runner.RunWith;
import org.xpect.XpectImport;
import org.xpect.expectation.ILinesExpectation;
import org.xpect.expectation.LinesExpectation;
import org.xpect.runner.Xpect;
import org.xpect.runner.XpectRunner;
import org.xpect.xtext.lib.setup.ThisModel;
import org.xpect.xtext.lib.setup.XtextStandaloneSetup;
import org.xpect.xtext.lib.setup.XtextWorkspaceSetup;
import org.xpect.xtext.lib.util.GrammarAnalyzer;
import org.xpect.xtext.lib.util.GrammarAnalyzer.CommentRule;

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
