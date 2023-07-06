/**
 * Copyright (c) 2015, 2022 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.xtext.example.arithmetics.tests.interpreter;

import java.math.BigDecimal;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.example.arithmetics.arithmetics.Expression;
import org.eclipse.xtext.example.arithmetics.interpreter.Calculator;
import org.eclipse.xtext.example.arithmetics.tests.PatchedArithmeticsInjectorProvider;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

@RunWith(XtextRunner.class)
@InjectWith(PatchedArithmeticsInjectorProvider.class)
public class CalculatorTest {
  @Inject
  private ParseHelper<org.eclipse.xtext.example.arithmetics.arithmetics.Module> parseHelper;
  
  @Inject
  private Calculator calculator;
  
  @Test
  public void testSimple() throws Exception {
    check(6, "1 + 2 + 3");
    check(0, "1 + 2 - 3");
    check(5, "1 * 2 + 3");
    check((-4), "1 - 2 - 3");
    check(1.5, "1 / 2 * 3");
  }
  
  @Test
  public void testFunction() throws Exception {
    check(12.0, "\n\t\t\tmultiply(2,multiply(2, 3));\n\t\t\tdef multiply(a, b) : a * b;\n\t\t");
  }
  
  protected void check(final double expected, final String expression) throws Exception {
    StringConcatenation builder = new StringConcatenation();
    builder.append("module test ");
    builder.append(expression);
    org.eclipse.xtext.example.arithmetics.arithmetics.Module module = parseHelper.parse(builder);
    BigDecimal result = calculator.evaluate(Iterables.getFirst(Iterables.filter(Iterables.getFirst(module.getStatements(), null).eContents(), Expression.class), null));
    Assert.assertEquals(expected, result.doubleValue(), 0.0001);
  }
}
