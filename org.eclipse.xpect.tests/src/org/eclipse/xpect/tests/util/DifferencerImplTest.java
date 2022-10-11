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
package org.eclipse.xpect.tests.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.xpect.util.DifferencerImpl;
import org.eclipse.xpect.util.IDifferencer;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class DifferencerImplTest {
	private String diff(List<String> left, List<String> right) {
		List<IDifferencer.Match> diff = new DifferencerImpl().diff(left, right,  (String s1, String s2) -> {
			Set<Character> set1 = new HashSet<Character>();
			for (char c : s1.toCharArray()) {
				set1.add(Character.valueOf(c));
			}
			Set<Character> set2 = new HashSet<Character>();
			for (char c : s2.toCharArray()) {
				set2.add(Character.valueOf(c));
			}
			float overlap = (float) Sets.<Character> intersection(set1, set2).size();
			float size = (float) Math.max(set1.size(), set2.size());
			return 1 - overlap / size;
		});
		return IterableExtensions.join(diff, "\n");
	}

	@Test
	public void testOneMatch() {
		String o1 = "[0==0]\n";
		operator_tripleEquals(o1, diff(Lists.newArrayList("A"), Lists.newArrayList("A")));
	}

	@Test
	public void testTwoMatches() {
		String o1 = 
				"[0==0]\n" +
				"[1==1]\n";
		operator_tripleEquals(o1, diff(Lists.newArrayList("A", "B"), Lists.newArrayList("A", "B")));
	}

	@Test
	public void testThreeMatches() {
		String o1 = 
				"[0==0]\n" +
				"[1==1]\n" +
				"[2==2]\n";
		operator_tripleEquals(o1, diff(Lists.newArrayList("A", "B", "C"), Lists.newArrayList("A", "B", "C")));
	}

	@Test
	public void testInsertRightBeginning() {
		String o1 = 
				"[---0]\n" +
				"[0==1]\n" +
				"[1==2]\n";
		operator_tripleEquals(o1, diff(Lists.newArrayList("B", "C"), Lists.newArrayList("A", "B", "C")));
	}

	@Test
	public void testInsertRightMiddle() {
		String o1 = 
				"[0==0]\n" +
				"[---1]\n" +
				"[1==2]\n";
		operator_tripleEquals(o1, diff(Lists.newArrayList("A", "C"), Lists.newArrayList("A", "B", "C")));
	}

	@Test
	public void testInsertRightEnd() {
		String o1 = 
				"[0==0]\n" +
				"[1==1]\n" +
				"[---2]\n";
		operator_tripleEquals(o1, diff(Lists.newArrayList("A", "B"), Lists.newArrayList("A", "B", "C")));
	}

	@Test
	@Ignore
	public void testRemoveMiddle() {
		String o1 = 
				"[0==0]\n" +
				"[1---]\n" +
				"[2---]\n" +
				"[3---]\n" +
				"[4==1]\n";
		operator_tripleEquals(o1, diff(Lists.newArrayList("A", "B", "B", "B", "C"), Lists.newArrayList("A", "C")));
	}

	public void operator_tripleEquals(Object o1, Object o2) {
		Assert.assertEquals(o1.toString().trim(), o2.toString().trim());
	}
}
