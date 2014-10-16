package org.xpect.tests.state

import org.junit.Test

import static extension org.junit.Assert.*
import org.xpect.tests.state.SetupInitializerTestData.SiT
import org.xpect.tests.state.SetupInitializerTestData.A

class SetupInitilizerTest {

	@Test
	def void specificConstructorStrings() {
		val si = new SiT<Object>(null)
		val String[] strings = #["a", "b"]
		val c = si.findConstructor(A, strings)
		A.assertSame(c.declaringClass)
		2.assertEquals(c.parameterTypes.length)
	}

	@Test
	def void varargConstructorStrings() {
		val si = new SiT<Object>(null)
		val String[] strings = #["a", "b", "c"]
		val c = si.findConstructor(A, strings)
		A.assertSame(c.declaringClass)
		1.assertEquals(c.parameterTypes.length)
	}

	@Test(expected=RuntimeException)
	def void mixedVarargConstructor() {
		val si = new SiT<Object>(null)
		val Object[] objs = #[5.0, "b", "c"]
		val c = si.findConstructor(A, objs)
		A.assertSame(c.declaringClass)
		1.assertEquals(c.parameterTypes.length)
	}

	@Test
	def void varargConstructorStrings2() {
		val si = new SiT<Object>(null)
		val String[] strings = #["a"]
		val c = si.findConstructor(A, strings)
		A.assertSame(c.declaringClass)
		1.assertEquals(c.parameterTypes.length)
		c.parameterTypes.get(0).isArray
	}

	@Test
	def void varargConstructorStrings_empty() {
		val si = new SiT<Object>(null)
		val String[] strings = #[]
		val c = si.findConstructor(A, strings)
		A.assertSame(c.declaringClass)
	}

	@Test
	def void varargConstructor_double() {
		val si = new SiT<Object>(null)
		val Double[] dbls = #[1.3, 4.0]
		val c = si.findConstructor(A, dbls)
		A.assertSame(c.declaringClass)
	}

}
