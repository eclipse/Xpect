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

package org.eclipse.xpect.examples.textile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.eclipse.xpect.expectation.CommaSeparatedValuesExpectation;
import org.eclipse.xpect.expectation.ICommaSeparatedValuesExpectation;
import org.eclipse.xpect.expectation.IStringExpectation;
import org.eclipse.xpect.expectation.StringExpectation;
import org.eclipse.xpect.runner.Xpect;
import org.eclipse.xpect.runner.XpectRunner;
import org.eclipse.xpect.runner.XpectTestFiles;

import com.google.common.collect.Lists;

@RunWith(XpectRunner.class)
@XpectTestFiles(fileExtensions = "xt")
public class SimpleTest {

	@Xpect
	public void simple() {
		System.out.println("Hello World");
	}

	@Xpect
	public void simpleString(@StringExpectation IStringExpectation expectation) {
		System.out.println("simpleString");
		expectation.assertEquals("Foo Bar");
	}

	@Xpect
	public void simpleCSV(
			@CommaSeparatedValuesExpectation ICommaSeparatedValuesExpectation expectation) {
		System.out.println("simpleString");
		expectation.assertEquals(Lists.newArrayList("aa", "bb", "cc"));
	}

	@Test
	public void testPlainJUnitTest() {
		System.out.println("plain junit");
	}

}
