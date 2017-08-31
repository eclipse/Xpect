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

package org.eclipse.xtext.example.arithmetics.xpect.tests;

import org.eclipse.xtext.example.arithmetics.xpect.tests.interpreter.InterpreterTest;
import org.eclipse.xtext.example.arithmetics.xpect.tests.suite.ArithmeticsXtextTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ InterpreterTest.class, ArithmeticsXtextTest.class })
public class AllArithmeticsTests {

}
