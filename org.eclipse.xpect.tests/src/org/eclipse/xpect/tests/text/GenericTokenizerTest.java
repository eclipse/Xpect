/**
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 */
package org.eclipse.xpect.tests.text;

import org.eclipse.xpect.text.GenericTokenizer;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Joiner;

public class GenericTokenizerTest {
	@Test
	public void testSimple() {
		assertIsTokenenizedTo("aa| |12| |+|-|\r\n |AbCd", "aa 12 +-\r\n AbCd");
	}

	@Test
	public void testWord() {
		assertIsTokenenizedTo("ab123cd456| |456|ab345", "ab123cd456 456ab345");
	}

	@Test
	public void testSymbols() {
		assertIsTokenenizedTo(";|;|\'|\'|]|]|\\|<|<|>|>|?|?|:|:|\"|\"|}|}|{|{||||", ";;\'\']]\\<<>>??::\"\"}}{{||");
		assertIsTokenenizedTo("_|_|+|+|-|-|=|=|,|,|.|.|/|/|;|;", "__++--==,,..//;;");
		assertIsTokenenizedTo("!|!|@|@|#|#|$|$|%|%|^|^|&|&|*|*|(|(|)|)", "!!@@##$$%%^^&&**(())");
	}

	public void assertIsTokenenizedTo(Object expected, Object source) {
		final String tokenized = Joiner.on("|").join(new GenericTokenizer().apply(source.toString()));
		Assert.assertEquals(expected.toString(), tokenized);
	}
}
