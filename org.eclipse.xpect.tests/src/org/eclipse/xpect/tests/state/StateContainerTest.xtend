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

package org.eclipse.xpect.tests.state

import org.junit.Test
import org.eclipse.xpect.state.ManagedImpl
import org.eclipse.xpect.state.StateContainer

import static org.junit.Assert.*

class StateContainerTest {

	@Test
	def void testPrimaryValue() {
		val cnt = new StateContainer [
			it.addDefaultValue("Foo")
		]
		assertEquals("Foo", cnt.get(String).get())
	}

	@Test
	def void testPrimaryValueAnn() {
		val cnt = new StateContainer [
			it.addValue(TestData.Ann, "Foo")
		]
		assertEquals("Foo", cnt.get(String, TestData.Ann).get())
	}

	@Test
	def void testPrimaryManaged() {
		val cnt = new StateContainer [
			it.addDefaultValue(String, new ManagedImpl<String>("Foo"))
		]
		assertEquals("Foo", cnt.get(String).get())
	}

	@Test
	def void testPrimaryManagedAnn() {
		val cnt = new StateContainer [
			it.addValue(TestData.Ann, String, new ManagedImpl<String>("Foo"))
		]
		assertEquals("Foo", cnt.get(String, TestData.Ann).get())
	}

	@Test
	def void testDerivedValue() {
		val cnt = new StateContainer [
			it.addFactory(TestData.StaticValueProvider)
		]
		assertEquals("DefaultValue", cnt.get(String).get())
		assertEquals("AnnotatedValue", cnt.get(String, TestData.Ann).get())
	}

	@Test
	def void testDerivedManaged() {
		val cnt = new StateContainer [
			it.addFactory(TestData.StaticManagedProvider)
		]
		assertEquals("DefaultManaged", cnt.get(String).get())
		assertEquals("AnnotatedManaged", cnt.get(String, TestData.Ann).get())
	}
}
