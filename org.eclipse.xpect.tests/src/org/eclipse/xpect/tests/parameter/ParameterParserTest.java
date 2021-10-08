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

package org.eclipse.xpect.tests.parameter;

import org.junit.runner.RunWith;
import org.eclipse.xpect.expectation.IStringExpectation;
import org.eclipse.xpect.lib.XpectTestResultTest;
import org.eclipse.xpect.parameter.OffsetRegion;
import org.eclipse.xpect.parameter.ParameterParser;
import org.eclipse.xpect.runner.Xpect;
import org.eclipse.xpect.runner.XpectRunner;
import org.eclipse.xpect.runner.XpectSuiteClasses;
import org.eclipse.xpect.text.IRegion;

@RunWith(XpectRunner.class)
@XpectSuiteClasses(XpectTestResultTest.class)
public class ParameterParserTest {

	@Xpect
	@ParameterParser(syntax = "('at' arg1=OFFSET)?")
	public void intOffset(IStringExpectation exp, int offset) {
		exp.assertEquals(offset);
	}

	@Xpect
	@ParameterParser(syntax = "('at' arg1=OFFSET)?")
	public void regionOffset(IStringExpectation exp, OffsetRegion offset) {
		IRegion match = offset.getMatchedRegion();
		if (match != null) {
			int rel = offset.getMatchedOffset() - match.getOffset();
			String text = "Offset " + rel + " in " + match.getRegionText();
			exp.assertEquals(text);
		} else {
			exp.assertEquals("(not matched)");
		}
	}

	@Xpect
	@ParameterParser(syntax = "'kw'")
	public void mandatoryKeyword() {
	}

	@Xpect
	public void mandatoryParameter(int x) {
	}
}
