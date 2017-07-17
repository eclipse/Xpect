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

package org.xpect.tests.text

import org.junit.Assert
import org.xpect.text.GenericTokenizer
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
