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

package org.xpect.xtext.lib.tests;

import org.junit.runner.RunWith;
import org.xpect.runner.XpectRunner;
import org.xpect.runner.XpectSuiteClasses;

/**
 * Collections of all built-in Xtext Core tests.
 * 
 * @author Moritz Eysholdt
 * 
 * @see org.xpect.xtext.xbase.lib.tests.XtextXbaseTests for additional Xbase-tests
 *
 */
@XpectSuiteClasses({ JvmModelInferrerTest.class, //
		LinkingTest.class, //
		ResourceDescriptionTest.class, //
		ScopingTest.class, //
		ValidationTest.class, //
})
@RunWith(XpectRunner.class)
public class XtextTests {

}
