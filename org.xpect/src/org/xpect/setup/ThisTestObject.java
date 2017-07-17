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

package org.xpect.setup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.xpect.XpectImport;
import org.xpect.setup.ThisTestObject.TestObjectSetup;
import org.xpect.state.Creates;
import org.xpect.state.XpectStateAnnotation;

@XpectStateAnnotation
@Retention(RetentionPolicy.RUNTIME)
@XpectImport(TestObjectSetup.class)
public @interface ThisTestObject {

	@XpectSetupFactory
	public class TestObjectSetup {
		private final Class<?> testClass;

		public TestObjectSetup(@ThisTestClass Class<?> testClass) {
			super();
			this.testClass = testClass;
		}

		@Creates(ThisTestObject.class)
		public Object createTestInstance() throws InstantiationException, IllegalAccessException {
			return this.testClass.newInstance();
		}

		protected Class<?> getTestClass() {
			return testClass;
		}
	}

}
