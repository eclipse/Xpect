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

package org.eclipse.xpect.xtext.lib.setup;

import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.XpectReplace;
import org.eclipse.xpect.setup.ThisTestClass;
import org.eclipse.xpect.setup.ThisTestObject;
import org.eclipse.xpect.setup.ThisTestObject.TestObjectSetup;
import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.state.Creates;

import com.google.inject.Injector;

@XpectSetupFactory
@XpectImport(InjectorSetup.class)
@XpectReplace(TestObjectSetup.class)
public class XtextTestObjectSetup extends TestObjectSetup {

	private final Injector injector;

	public XtextTestObjectSetup(@ThisTestClass Class<?> testClass, Injector injector) {
		super(testClass);
		this.injector = injector;
	}

	@Creates(ThisTestObject.class)
	public Object createTestInstance() throws InstantiationException, IllegalAccessException {
		return injector.getInstance(getTestClass());
	}

}
