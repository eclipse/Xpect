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

package org.xpect.tests.state

import static org.xpect.tests.TestUtil.*
import org.junit.Test
import org.xpect.state.StateContainer
import org.xpect.tests.state.LoggingTestData.AnnDerived1

public class StateContainerInvalidationTest {

	@Test
	def void testInvalidate() {
		val actual = new LoggingTestData.EventLogger();
		val expected = newArrayList()

		val container = new StateContainer [
			it.addDefaultValue(String, new LoggingTestData.LoggingManaged(actual, "Foo"))
		];
		assertEquals(expected, actual);

		assertEquals("Foo", container.get(String).get());
		expected += "LoggingTestData$LoggingManaged.get() -> Foo"
		assertEquals(expected, actual);

		container.get(String).invalidate();
		expected += "LoggingTestData$LoggingManaged.invalidate() -> Foo"
		assertEquals(expected, actual);
	}

	@Test
	def void testInvalidate2() {
		val actual = new LoggingTestData.EventLogger();
		val expected = newArrayList()

		val container = new StateContainer [
			it.addDefaultValue(actual)
			it.addFactory(LoggingTestData.StaticValueLoggingProvider)
			it.addFactory(LoggingTestData.DerivedProvider)
		];
		assertEquals(expected, actual);

		assertEquals("derivedDefaultValue", container.get(String, AnnDerived1).get());
		expected += "LoggingTestData$StaticValueLoggingProvider.<init>() -> instantiate"
		expected += "LoggingTestData$StaticValueLoggingProvider.getDefaultValue() -> DefaultValue"
		expected += "LoggingTestData$StaticValueLoggingProvider.getAnnotatedValue() -> AnnotatedValue"
		expected += "LoggingTestData$DerivedProvider.getDerived1() -> derivedDefaultValue"
		assertEquals(expected, actual);

		container.get(String).invalidate();
		expected += "LoggingTestData$StaticValueLoggingProvider.invalidateDefaultValue(DefaultValue)"
		expected += "LoggingTestData$DerivedProvider.invalidatesDerived1(derivedDefaultValue)"
		assertEquals(expected, actual);
	}

}
