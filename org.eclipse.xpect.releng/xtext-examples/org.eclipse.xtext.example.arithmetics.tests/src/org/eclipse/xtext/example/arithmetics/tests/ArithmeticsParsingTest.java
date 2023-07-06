/*******************************************************************************
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.example.arithmetics.tests;

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(XtextRunner.class)
@InjectWith(org.eclipse.xtext.example.arithmetics.tests.PatchedArithmeticsInjectorProvider.class)
public class ArithmeticsParsingTest {
  @Inject
  private ParseHelper<org.eclipse.xtext.example.arithmetics.arithmetics.Module> parseHelper;
  
  @Inject
  private ValidationTestHelper validationTestHelper;
  
	@Test
	public void loadModel() throws Exception {
		String model =
				"module test\n" +
				"def fun(a,b) : a * b;\n" +
				"fun(2, fun(3,4));\n";
		validationTestHelper.assertNoErrors(parseHelper.parse(model));
	}
}
