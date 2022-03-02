/**
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 */
package org.eclipse.xpect.tests.state;

import java.util.ArrayList;

import org.eclipse.xpect.state.Configuration;
import org.eclipse.xpect.state.StateContainer;
import org.eclipse.xpect.tests.TestUtil;
import org.junit.Test;

import com.google.common.collect.Lists;

public class StateContainerInvalidationTest {
	@Test
	public void testInvalidate() {
		LoggingTestData.EventLogger actual = new LoggingTestData.EventLogger();
		ArrayList<String> expected = Lists.newArrayList();
		StateContainer container = new StateContainer((Configuration it) -> {
			it.addDefaultValue(String.class, new LoggingTestData.LoggingManaged(actual, "Foo"));
		});
		TestUtil.assertEquals(expected, actual);
		TestUtil.assertEquals("Foo", container.get(String.class).get());
		expected.add("LoggingTestData$LoggingManaged.get() -> Foo");
		TestUtil.assertEquals(expected, actual);
		container.get(String.class).invalidate();
		expected.add("LoggingTestData$LoggingManaged.invalidate() -> Foo");
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void testInvalidate2() {
		LoggingTestData.EventLogger actual = new LoggingTestData.EventLogger();
		ArrayList<String> expected = Lists.newArrayList();
		StateContainer container = new StateContainer((Configuration it) -> {
			it.addDefaultValue(actual);
			it.addFactory(LoggingTestData.StaticValueLoggingProvider.class);
			it.addFactory(LoggingTestData.DerivedProvider.class);
		});
		TestUtil.assertEquals(expected, actual);
		TestUtil.assertEquals("derivedDefaultValue", container.get(String.class, LoggingTestData.AnnDerived1.class).get());
		expected.add("LoggingTestData$StaticValueLoggingProvider.<init>() -> instantiate");
		expected.add("LoggingTestData$StaticValueLoggingProvider.getDefaultValue() -> DefaultValue");
		expected.add("LoggingTestData$StaticValueLoggingProvider.getAnnotatedValue() -> AnnotatedValue");
		expected.add("LoggingTestData$DerivedProvider.getDerived1() -> derivedDefaultValue");
		TestUtil.assertEquals(expected, actual);
		container.get(String.class).invalidate();
		expected.add("LoggingTestData$StaticValueLoggingProvider.invalidateDefaultValue(DefaultValue)");
		expected.add("LoggingTestData$DerivedProvider.invalidatesDerived1(derivedDefaultValue)");
		TestUtil.assertEquals(expected, actual);
	}
}
