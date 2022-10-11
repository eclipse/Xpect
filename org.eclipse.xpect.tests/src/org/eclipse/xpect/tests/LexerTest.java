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
package org.eclipse.xpect.tests;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.Token;
import org.eclipse.xpect.lexer.XpectRT;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;

public class LexerTest {
	@Test
	public void testSimple() throws Exception {
		String actual = lex("// XPECT_SETUP foo END_SETUP");
		String expected = 
				"RULE_ANY_OTHER \'/\'\n" +
				"RULE_ANY_OTHER \'/\'\n" +
				"RULE_ANY_OTHER \' \'\n" +
				"XPECT_SETUP \'XPECT_SETUP\'\n" +
				"RULE_WS \' \'\n" +
				"RULE_ID \'foo\'\n" +
				"RULE_WS \' \'\n" +
				"END_SETUP \'END_SETUP\'\n";
		this.assertEquals(expected, actual);
	}

	@Test
	public void testX() throws Exception {
		String actual = lex("X");
		String expected = "RULE_ANY_OTHER \'X\'\n";
		this.assertEquals(expected, actual);
	}

	@Test
	public void testXP() throws Exception {
		String actual = lex("XP");
		String expected = 
				"RULE_ANY_OTHER \'X\'\n" +
				"RULE_ANY_OTHER \'P\'\n";
		this.assertEquals(expected, actual);
	}

	@Test
	public void testXPE() throws Exception {
		String actual = lex("XPE");
		String expected = 
				"RULE_ANY_OTHER \'X\'\n" +
				"RULE_ANY_OTHER \'P\'\n" +
				"RULE_ANY_OTHER \'E\'\n";
		this.assertEquals(expected, actual);
	}

	@Test
	public void testXPEC() throws Exception {
		String actual = lex("XPEC");
		String expected = 
				"RULE_ANY_OTHER \'X\'\n" +
				"RULE_ANY_OTHER \'P\'\n" +
				"RULE_ANY_OTHER \'E\'\n" +
				"RULE_ANY_OTHER \'C\'\n";
		this.assertEquals(expected, actual);
	}

	@Test
	public void testXPECT() throws Exception {
		String actual = lex("XPECT");
		String expected = "XPECT \'XPECT\'\n";
		this.assertEquals(expected, actual);
	}

	private String lex(CharSequence text) throws Exception {
		Map<Integer, String> names = this.tokenNames();
		ANTLRStringStream _aNTLRStringStream = new ANTLRStringStream(text.toString());
		XpectRT lexer = new XpectRT(_aNTLRStringStream);
		ArrayList<String> result = Lists.newArrayList();
		while (true) {
			Token token = lexer.nextToken();
			if (Objects.equal(token, Token.EOF_TOKEN)) {
				return IterableExtensions.join(result, "\n");
			}
			Object value = null;
			String getByName = names.get(Integer.valueOf(token.getType()));
			if (getByName != null) {
				value = getByName;
			} else {
				value = Integer.valueOf(token.getType());
			}
			result.add(value + " \'" + token.getText() + "\'");
		}
	}

	private Map<Integer, String> tokenNames() throws Exception {
		String file = "../org.eclipse.xpect/src-gen/org/eclipse/xpect/lexer/XpectRT.tokens";
		LinkedHashMap<Integer, String> result = CollectionLiterals.<Integer, String> newLinkedHashMap();
		for (String line : CharStreams.readLines(new FileReader(file))) {
			String[] s = line.split("=");
			String name = s[0];
			String value = null;
			if (name.startsWith("KEYWORD")) {
				value = "KEYWORD";
			} else {
				value = name;
			}
			result.put(Integer.valueOf(Integer.parseInt(s[1])), value);
		}
		return result;
	}

	private void assertEquals(CharSequence expt, CharSequence act) {
		Assert.assertEquals(expt.toString().trim(), act.toString().trim());
	}
}
