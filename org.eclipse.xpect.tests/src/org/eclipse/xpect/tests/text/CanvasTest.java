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

import org.eclipse.xpect.text.Canvas;
import org.eclipse.xpect.text.ICanvas;
import org.junit.Assert;
import org.junit.Test;

public class CanvasTest {
  private ICanvas newCanvas() {
    return Canvas.create('-', "$\n");
  }

  @Test
  public void testPaintAt00() {
    ICanvas canvas = this.newCanvas();
    canvas.print("Foo");
    assertToString(canvas, "Foo\n");
  }

  @Test
  public void testPaintAt02() {
    ICanvas canvas = this.newCanvas();
    canvas.at(0, 2).print("Foo");
    assertToString(canvas, "--Foo\n");
  }

  @Test
  public void testPaintAt20() {
    ICanvas canvas = this.newCanvas();
    canvas.at(2, 0).print("Foo");
    String expectation =
    			"$\n" +
				"$\n" +
				"Foo\n";
	assertToString(canvas, expectation);
  }

  @Test
  public void testPaintAt22() {
    ICanvas canvas = this.newCanvas();
    canvas.at(2, 2).print("Foo");
    String expectation =
    			"$\n" +
				"$\n" +
				"--Foo\n";
	assertToString(canvas, expectation);
  }

  @Test
  public void testOverPaintAt22() {
    ICanvas canvas = this.newCanvas();
    canvas.at(2, 2).print("Foo");
    canvas.at(1, 3).print("Bar\nBaz");
    String expectation =
    			"$\n" +
				"---Bar$\n" +
				"--FBaz\n";
	assertToString(canvas, expectation);
  }

  @Test
  public void testSubcanvasAt22() {
    ICanvas canvas = this.newCanvas();
    canvas.at(2, 2).newSubCanvas().print("Foo");
    String expectation = 
    			"$\n" +
				"$\n" +
				"--Foo\n";
	assertToString(canvas, expectation);
  }

  @Test
  public void testSubcanvasAt2235() {
    ICanvas canvas = this.newCanvas();
    canvas.at(2, 2).newSubCanvas().at(3, 5).print("Foo");
    String expectation = 
    			"$\n" +
				"$\n" +
				"$\n" +
				"$\n" +
				"$\n" +
				"-------Foo\n";
	assertToString(canvas, expectation);
  }

  @Test
  public void testBounds() {
    ICanvas canvas = this.newCanvas();
    canvas.withBounds(2, 3).print("abcde\nfghij\nklmno\npqrstuv");
    String expectation = 
    			"abc$\n" +
				"fgh\n";
	assertToString(canvas, expectation);
  }

  @Test
  public void testBoundsAt() {
    ICanvas canvas = this.newCanvas();
    canvas.at(1, 2).withBounds(2, 3).print("abcde\nfghij\nklmno\npqrstuv");
    String expectation = 
    			"$\n" +
				"--abc$\n" +
				"--fgh\n";
	assertToString(canvas, expectation);
  }

  @Test
  public void testBoundsSubcanvasAt() {
    ICanvas canvas = this.newCanvas();
    canvas.at(1, 2).withBounds(2, 3).newSubCanvas().print("abcde\nfghij\nklmno\npqrstuv");
    String expectation = 
    			"$\n" +
				"--abc$\n" +
				"--fgh\n";
	assertToString(canvas, expectation);
  }

  @Test
  public void testFill() {
    ICanvas canvas = this.newCanvas();
    canvas.at(1, 2).withBounds(5, 8).fill("a\nbcd");
    String expectation =
    			"$\n" +
				"--a--a--a-$\n" +
				"--bcdbcdbc$\n" +
				"--a--a--a-$\n" +
				"--bcdbcdbc$\n" +
				"--a--a--a-\n";
	assertToString(canvas, expectation);
  }

  public void assertToString(Object o1, String expectation) {
    Assert.assertEquals(expectation.toString().trim(), o1.toString().trim());
  }
}
