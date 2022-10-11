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

import java.util.List;

import org.eclipse.xpect.text.ITextDifferencer;
import org.eclipse.xpect.text.TextDiffToString;
import org.eclipse.xpect.text.TextDifferencer;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TextDifferencerTest {
  @Test
  @Ignore
  public void testEqual() {
    List<String> left = Lists.newArrayList("a", "b", "c");
    List<String> right = Lists.newArrayList("a", "b", "c");
    assertEquals(diff(left, right), "abc\n");
  }

  @Test
  public void testDiffSL() {
    List<String> left = Lists.newArrayList("a", "b", "c");
    List<String> right = Lists.newArrayList("a", "d", "c");
    assertEquals(diff(left, right), "| a[b|d]c\n");
  }

  @Test
  public void testRemoveSL() {
    List<String> left = Lists.newArrayList("a", "b", "c");
    List<String> right = Lists.newArrayList("a", "c");
    assertEquals(diff(left, right), "| a[b|]c\n");
  }

  @Test
  public void testAddSL() {
    List<String> left = Lists.newArrayList("a", "c");
    List<String> right = Lists.newArrayList("a", "b", "c");
    assertEquals(diff(left, right), "| a[|b]c\n");
  }

  @Test
  public void testDiffSLEnd() {
    List<String> left = Lists.newArrayList("a", "b");
    List<String> right = Lists.newArrayList("a", "c");
    assertEquals(diff(left, right), "| a[b|c]\n");
  }

  @Test
  public void testRemoveSLEnd() {
    List<String> left = Lists.newArrayList("a", "b");
    List<String> right = Lists.newArrayList("a");
    assertEquals(diff(left, right), "| a[b|]\n");
  }

  @Test
  public void testAddSLEnd() {
    List<String> left = Lists.newArrayList("a");
    List<String> right = Lists.newArrayList("a", "b");
    assertEquals(diff(left, right), "| a[|b]\n");
  }

  @Test
  public void testDiffML() {
    List<String> left = Lists.newArrayList("a\n", "b\n", "c\n");
    List<String> right = Lists.newArrayList("a\n", "d\n", "c\n");
    String expectation =
    			"  a\n" +
				"- b\n" +
				"+ d\n" +
				"  c\n";
	assertEquals(diff(left, right), expectation);
  }

  @Test
  public void testRemoveML() {
    List<String> left = Lists.newArrayList("a\n", "b\n", "c\n");
    List<String> right = Lists.newArrayList("a\n", "c\n");
    String expectation = 
    			"  a\n" +
				"- b\n" +
				"  c\n";
	assertEquals(diff(left, right), expectation);
  }

  @Test
  public void testAddML() {
    List<String> left = Lists.newArrayList("a\n", "c\n");
    List<String> right = Lists.newArrayList("a\n", "b\n", "c\n");
    String expectation = 
    			"  a\n" +
				"+ b\n" +
				"  c\n";
	assertEquals(diff(left, right), expectation);
  }

  @Test
  @Ignore
  public void testWhitespace() {
    List<String> left = Lists.newArrayList("a", "  ", "b");
    List<String> right = Lists.newArrayList("a", "    ", "b");
    assertEquals(diff(left, right), "a  b\n");
  }

  @Test
  public void testWhitespaceDiff() {
    List<String> left = Lists.newArrayList("a", "  ", "b", "   ", "c");
    List<String> right = Lists.newArrayList("a", "    ", "c");
    assertEquals(diff(left, right), "| a  [b|]   c\n");
  }

  @Test
  public void testWhitespaceDiff2() {
    List<String> left = Lists.newArrayList("a", "  ", "b", "   ", "c", "    ", "d");
    List<String> right = Lists.newArrayList("a", "     ", "d");
    assertEquals(diff(left, right), "| a  [b   c|]    d\n");
  }

  public String diff(List<String> left, List<String> right) {
    TextDiffToString toStr = new TextDiffToString().setAllowSingleLineDiff(false).setAllowSingleSegmentDiff(false);
    ITextDifferencer.ITextDiff diff = new TextDifferencer().<String>diff(left, right, new TextDiffConfig());
    return toStr.apply(diff);
  }

  public void assertEquals(Object o1, Object expectation) {
    Assert.assertEquals(expectation.toString().trim(), o1.toString().trim());
  }
}
