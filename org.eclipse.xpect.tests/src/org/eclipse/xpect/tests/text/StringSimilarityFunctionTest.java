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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.xpect.text.StringEndsSimilarityFunction;
import org.eclipse.xpect.util.IDifferencer;
import org.eclipse.xtext.xbase.lib.Pair;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class StringSimilarityFunctionTest {
	@Test
	public void testEqual() {
		boolean not_equals = (IDifferencer.ISimilarityFunction.EQUAL != this.similarity("abc", "abc"));
		Assert.assertTrue(!not_equals);
	}

	@Test
	public void testUnequal() {
		Assert.assertTrue((IDifferencer.ISimilarityFunction.UPPER_SIMILARITY_BOUND == this.similarity("", "")));
		Assert.assertTrue((IDifferencer.ISimilarityFunction.UPPER_SIMILARITY_BOUND == this.similarity(null, null)));
	}

	@Test
	public void testSimilarBeginning() {
		ArrayList<Pair<String, String>> data = Lists.newArrayList();
		data.add(Pair.of("aaaaaaaaaa", "aaaaaaaaaa"));
		data.add(Pair.of("aaaaaaaaaa", "aaaaaaaaa-"));
		data.add(Pair.of("aaaaaaaaaa", "aaaaaaaa--"));
		data.add(Pair.of("aaaaaaaaaa", "aaaaaaa---"));
		data.add(Pair.of("aaaaaaaaaa", "aaaaaa----"));
		data.add(Pair.of("aaaaaaaaaa", "aaaaa-----"));
		data.add(Pair.of("aaaaaaaaaa", "aaaa------"));
		data.add(Pair.of("aaaaaaaaaa", "aaa-------"));
		data.add(Pair.of("aaaaaaaaaa", "aa--------"));
		data.add(Pair.of("aaaaaaaaaa", "a---------"));
		data.add(Pair.of("aaaaaaaaaa", "----------"));
		String expected = 
				"aaaaaaaaaa <> aaaaaaaaaa --> 0.00\n" +
				"aaaaaaaaaa <> aaaaaaaaa- --> 0.25\n" +
				"aaaaaaaaaa <> aaaaaaaa-- --> 0.50\n" +
				"aaaaaaaaaa <> aaaaaaa--- --> 0.75\n" +
				"aaaaaaaaaa <> aaaaaa---- --> 1.00\n" +
				"aaaaaaaaaa <> aaaaa----- --> 1.00\n" +
				"aaaaaaaaaa <> aaaa------ --> 1.00\n" +
				"aaaaaaaaaa <> aaa------- --> 1.00\n" +
				"aaaaaaaaaa <> aa-------- --> 1.00\n" +
				"aaaaaaaaaa <> a--------- --> 1.00\n" +
				"aaaaaaaaaa <> ---------- --> 1.00\n";
		Assert.assertEquals(expected.trim(), this.similarityStr(data));
	}

	@Test
	public void testSimilarEnd() {
		ArrayList<Pair<String, String>> data = Lists.newArrayList();
		data.add(Pair.of("aaaaaaaaaa", "aaaaaaaaaa"));
		data.add(Pair.of("aaaaaaaaaa", "-aaaaaaaaa"));
		data.add(Pair.of("aaaaaaaaaa", "--aaaaaaaa"));
		data.add(Pair.of("aaaaaaaaaa", "---aaaaaaa"));
		data.add(Pair.of("aaaaaaaaaa", "----aaaaaa"));
		data.add(Pair.of("aaaaaaaaaa", "-----aaaaa"));
		data.add(Pair.of("aaaaaaaaaa", "------aaaa"));
		data.add(Pair.of("aaaaaaaaaa", "-------aaa"));
		data.add(Pair.of("aaaaaaaaaa", "--------aa"));
		data.add(Pair.of("aaaaaaaaaa", "---------a"));
		data.add(Pair.of("aaaaaaaaaa", "----------"));
		String expected = 
				"aaaaaaaaaa <> aaaaaaaaaa --> 0.00\n" +
				"aaaaaaaaaa <> -aaaaaaaaa --> 0.25\n" +
				"aaaaaaaaaa <> --aaaaaaaa --> 0.50\n" +
				"aaaaaaaaaa <> ---aaaaaaa --> 0.75\n" +
				"aaaaaaaaaa <> ----aaaaaa --> 1.00\n" +
				"aaaaaaaaaa <> -----aaaaa --> 1.00\n" +
				"aaaaaaaaaa <> ------aaaa --> 1.00\n" +
				"aaaaaaaaaa <> -------aaa --> 1.00\n" +
				"aaaaaaaaaa <> --------aa --> 1.00\n" +
				"aaaaaaaaaa <> ---------a --> 1.00\n" +
				"aaaaaaaaaa <> ---------- --> 1.00\n";
		Assert.assertEquals(expected.trim(), this.similarityStr(data));
	}

	@Test
	public void testBeginningAndEnd() {
		ArrayList<Pair<String, String>> data = Lists.newArrayList();
		data.add(Pair.of("aaaaaaaaaa", "aaaaaaaaaa"));
		data.add(Pair.of("aaaaaaaaaa", "aaaaa-aaaa"));
		data.add(Pair.of("aaaaaaaaaa", "aaaa--aaaa"));
		data.add(Pair.of("aaaaaaaaaa", "aaaa---aaa"));
		data.add(Pair.of("aaaaaaaaaa", "aaa----aaa"));
		data.add(Pair.of("aaaaaaaaaa", "aaa-----aa"));
		data.add(Pair.of("aaaaaaaaaa", "aa------aa"));
		data.add(Pair.of("aaaaaaaaaa", "aa-------a"));
		data.add(Pair.of("aaaaaaaaaa", "a--------a"));
		data.add(Pair.of("aaaaaaaaaa", "a---------"));
		data.add(Pair.of("aaaaaaaaaa", "----------"));
		String expected = 
				"aaaaaaaaaa <> aaaaaaaaaa --> 0.00\n" +
				"aaaaaaaaaa <> aaaaa-aaaa --> 0.25\n" +
				"aaaaaaaaaa <> aaaa--aaaa --> 0.50\n" +
				"aaaaaaaaaa <> aaaa---aaa --> 0.75\n" +
				"aaaaaaaaaa <> aaa----aaa --> 1.00\n" +
				"aaaaaaaaaa <> aaa-----aa --> 1.00\n" +
				"aaaaaaaaaa <> aa------aa --> 1.00\n" +
				"aaaaaaaaaa <> aa-------a --> 1.00\n" +
				"aaaaaaaaaa <> a--------a --> 1.00\n" +
				"aaaaaaaaaa <> a--------- --> 1.00\n" +
				"aaaaaaaaaa <> ---------- --> 1.00\n";
		Assert.assertEquals(expected.trim(), this.similarityStr(data));
	}

	public String similarityStr(List<Pair<String, String>> pairs) {
		return Joiner.on("\n").join(Iterables.transform(pairs, (it) -> {
			return it.getKey() + " <> " + it.getValue() + " --> " + this.format(this.similarity(it.getKey(), it.getValue()));
		}));
				
	}

	public float similarity(String s1, String s2) {
		return new StringEndsSimilarityFunction().similarity(s1, s2);
	}

	/**
	 * Defines a reproducible float output format for the test, using US locale.
	 */
	public String format(float value) {
		return String.format(Locale.US, "%.2f", Float.valueOf(value));
	}
}
