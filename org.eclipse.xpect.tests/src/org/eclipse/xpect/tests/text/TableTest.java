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
import org.eclipse.xpect.text.Table;
import org.junit.Assert;
import org.junit.Test;

public class TableTest {
	private Table newTable() {
		return new Table();
	}

	@Test
	public void test1by1() {
		final Table table = this.newTable();
		table.getCell(0, 0).setText("X");
		this.assertsPaints(table, "X\n");
	}

	@Test
	public void test2by3() {
		final Table table = this.newTable();
		table.getCell(0, 0).setText("a");
		table.getCell(0, 1).setText("b");
		table.getCell(0, 2).setText("c");
		table.getCell(1, 0).setText("d");
		table.getCell(1, 1).setText("e");
		table.getCell(1, 2).setText("f");
		String expectation = 
				"a-b-c$\n" +
				"$\n" +
				"d-e-f\n";
		this.assertsPaints(table, expectation);
	}

	@Test
	public void test2by3Long() {
		final Table table = this.newTable();
		table.getCell(0, 0).setText("a1");
		table.getCell(0, 1).setText("b12");
		table.getCell(0, 2).setText("c123");
		table.getCell(1, 0).setText("d1234");
		table.getCell(1, 1).setText("e12345");
		table.getCell(1, 2).setText("f123456");
		String expectation = 
				"a1----b12----c123$\n" +
				"$\n" +
				"d1234-e12345-f123456\n";
		this.assertsPaints(table, expectation);
	}

	@Test
	public void test2by3LongTrimmed() {
		final Table table = this.newTable();
		table.setMaxCellWidth(3);
		table.getCell(0, 0).setText("a1");
		table.getCell(0, 1).setText("b12");
		table.getCell(0, 2).setText("c123");
		table.getCell(1, 0).setText("d1234");
		table.getCell(1, 1).setText("e12345");
		table.getCell(1, 2).setText("f123456");
		String expectation = 
				"a1--b12-c12$\n" +
				"$\n" +
				"d12-e12-f12\n";
		this.assertsPaints(table, expectation);
	}

	@Test
	public void test2by3BorderAndSeparators() {
		final Table table = this.newTable();
		table.getCell(0, 0).setText("a1");
		table.getCell(0, 1).setText("b12");
		table.getCell(0, 2).setText("c123");
		table.getCell(1, 0).setText("d1234");
		table.getCell(1, 1).setText("e12345");
		table.getCell(1, 2).setText("f123456");
		table.setSeparatorCrossingBackground("+");
		table.setRowSeparatorBackground("=");
		table.setColumnSeparatorBackground("|");
		table.setBorder(1);
		table.getBottomBorder().setHeight(3);
		table.getRightBorder().setWidth(2);
		table.getRow(0).getBottomSeparator().setBackground("#");
		table.getColumn(0).getRightSeparator().setBackground("@");
		String expectation = 
				"+=====+======+=======++$\n" +
				"|a1---@b12---|c123---||$\n" +
				"+#####+######+#######++$\n" +
				"|d1234@e12345|f123456||$\n" +
				"+=====+======+=======++$\n" +
				"+=====+======+=======++$\n" +
				"+=====+======+=======++\n";
		this.assertsPaints(table, expectation);
	}

	public void assertsPaints(Table table, Object o2) {
		final ICanvas canvas = Canvas.create('-', "$\n");
		table.paint(canvas);
		Assert.assertEquals(o2.toString().trim(), canvas.toString().trim());
	}
}
