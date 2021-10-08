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

package org.eclipse.xpect.tests.text

import org.junit.Assert
import org.eclipse.xpect.text.GenericTokenizer
import org.junit.Test

class GenericTokenizerTest {

	@Test def void testSimple() {
		"aa| |12| |+|-|\r\n |AbCd" === "aa 12 +-\r\n AbCd"
	}

	@Test def void testWord() {
		"ab123cd456| |456|ab345" === "ab123cd456 456ab345"
	}

	@Test def void testSymbols() {
		";|;|'|'|]|]|\\|<|<|>|>|?|?|:|:|\"|\"|}|}|{|{||||" === ";;'']]\\<<>>??::\"\"}}{{||"
		"_|_|+|+|-|-|=|=|,|,|.|.|/|/|;|;" === "__++--==,,..//;;"
		"!|!|@|@|#|#|$|$|%|%|^|^|&|&|*|*|(|(|)|)" === "!!@@##$$%%^^&&**(())"
	}

	def operator_tripleEquals(Object o1, Object o2) {
		val tokenized = new GenericTokenizer().apply(o2.toString).join("|")
		Assert.assertEquals(o1.toString, tokenized)
	}
}
