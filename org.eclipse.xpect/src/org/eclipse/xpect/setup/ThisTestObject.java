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

package org.eclipse.xpect.setup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.setup.ThisTestObject.TestObjectSetup;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.state.XpectStateAnnotation;

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
