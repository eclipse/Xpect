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

package org.eclipse.xpect.xtext.lib.tests.validation;

import org.junit.runner.RunWith;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.runner.XpectRunner;
import org.eclipse.xpect.runner.XpectSuiteClasses;
import org.eclipse.xpect.xtext.lib.setup.XtextStandaloneSetup;
import org.eclipse.xpect.xtext.lib.tests.ValidationTest;
import org.eclipse.xpect.xtext.lib.tests.ValidationTestModuleSetup;

@RunWith(XpectRunner.class)
@XpectSuiteClasses({IssuesTest.class, ValidationTest.class})
@XpectImport({XtextStandaloneSetup.class, ValidationTestModuleSetup.class})
public class IssuesTest {

}
