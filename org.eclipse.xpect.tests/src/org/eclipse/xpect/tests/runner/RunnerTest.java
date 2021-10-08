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

package org.eclipse.xpect.tests.runner;

import org.eclipse.xtext.util.XtextVersion;
import org.junit.runner.RunWith;
import org.eclipse.xpect.expectation.IStringExpectation;
import org.eclipse.xpect.expectation.StringExpectation;
import org.eclipse.xpect.lib.XpectTestResultTest;
import org.eclipse.xpect.runner.Xpect;
import org.eclipse.xpect.runner.XpectRunner;
import org.eclipse.xpect.runner.XpectSuiteClasses;

@RunWith(XpectRunner.class)
@XpectSuiteClasses(XpectTestResultTest.class)
public class RunnerTest {

	static {
		System.out.println("Xtext-Version in Target Platform: " + XtextVersion.getCurrent().getVersion());
	}

	@Xpect
	public void expectedExpectation(@StringExpectation IStringExpectation expectation) {
		expectation.assertEquals("expectedExpectation");
	}
}
