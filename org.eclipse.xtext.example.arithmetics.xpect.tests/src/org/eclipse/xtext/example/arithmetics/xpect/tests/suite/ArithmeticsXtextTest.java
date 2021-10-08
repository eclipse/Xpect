/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.eclipse.xtext.example.arithmetics.xpect.tests.suite;

import org.eclipse.xtext.example.arithmetics.xpect.tests.interpreter.InterpreterTest;
import org.junit.runner.RunWith;
import org.eclipse.xpect.runner.XpectRunner;
import org.eclipse.xpect.runner.XpectSuiteClasses;
import org.eclipse.xpect.runner.XpectTestFiles;
import org.eclipse.xpect.runner.XpectTestFiles.FileRoot;
import org.eclipse.xpect.xtext.lib.tests.JvmModelInferrerTest;
import org.eclipse.xpect.xtext.lib.tests.LinkingTest;
import org.eclipse.xpect.xtext.lib.tests.ResourceDescriptionTest;
import org.eclipse.xpect.xtext.lib.tests.ScopingTest;
import org.eclipse.xpect.xtext.lib.tests.ValidationTest;
import org.eclipse.xpect.xtext.lib.tests.XtextTests;

@RunWith(XpectRunner.class)
@XpectSuiteClasses({ InterpreterTest.class, //
		JvmModelInferrerTest.class, //
		LinkingTest.class,//
		ResourceDescriptionTest.class, //
		ScopingTest.class, //
		ValidationTest.class, //
})
@XpectTestFiles(relativeTo = FileRoot.PROJECT, baseDir = "calc-tests")
public class ArithmeticsXtextTest extends XtextTests {

}
