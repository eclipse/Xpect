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

import org.eclipse.xpect.state.Configuration;
import org.eclipse.xpect.state.ResolvedConfiguration;
import org.eclipse.xpect.tests.TestUtil;
import org.junit.Test;

public class AnalyzedConfigurationTest {
	@Test
	public void testSimple() {
		ResolvedConfiguration actual = StateTestUtil.newAnalyzedConfiguration((Configuration it) -> {
			it.addFactory(Singleton1.class);
			it.addFactory(Singleton2.class);
			it.addDefaultValue("MyString1");
			it.addValue(MyAnnotaion1.class, "MyString2");
		});
		String expected = 
				"Primary Values {\n" +
				"    PrimaryValue[@MyAnnotaion1 String Managed[MyString2]]\n" +
				"    PrimaryValue[StateContainer Managed[null]]\n" +
				"    PrimaryValue[String Managed[MyString1]]\n" +
				"}\n" +
				"Derived Values {\n" +
				"    org.eclipse.xpect.tests.state.Singleton1 {\n" +
				"        out DerivedValue[StringBuffer Singleton1.getBuffer()]\n" +
				"    }\n" +
				"    org.eclipse.xpect.tests.state.Singleton2 {\n" +
				"        in PrimaryValue[@MyAnnotaion1 String Managed[MyString2]]\n" +
				"        out DerivedValue[@Annotation StringBuffer Singleton2.getBuffer()]\n" +
				"    }\n" +
				"}\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void testStaticValue() {
		ResolvedConfiguration actual = StateTestUtil.newAnalyzedConfiguration((Configuration it) -> {
			it.addFactory(TestData.StaticValueProvider.class);
		});
		String expected =
				"Primary Values {\n" +
				"    PrimaryValue[StateContainer Managed[null]]\n" +
				"}\n" +
				"Derived Values {\n" +
				"    org.eclipse.xpect.tests.state.TestData$StaticValueProvider {\n" +
				"        out DerivedValue[@Ann String StaticValueProvider.getAnnotatedValue()]\n" +
				"        out DerivedValue[String StaticValueProvider.getDefaultValue()]\n" +
				"    }\n" +
				"}\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void testStaticManaged() {
		ResolvedConfiguration actual = StateTestUtil.newAnalyzedConfiguration((Configuration it) -> {
			it.addFactory(TestData.StaticManagedProvider.class);
		});
		String expected = 
				"Primary Values {\n" +
				"    PrimaryValue[StateContainer Managed[null]]\n" +
				"}\n" +
				"Derived Values {\n" +
				"    org.eclipse.xpect.tests.state.TestData$StaticManagedProvider {\n" +
				"        out DerivedValue[@Ann String StaticManagedProvider.getAnnotatedManaged()]\n" +
				"        out DerivedValue[String StaticManagedProvider.getDefaultManaged()]\n" +
				"    }\n" +
				"}\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void testLogginTestDataValue() {
		ResolvedConfiguration actual = StateTestUtil.newAnalyzedConfiguration((Configuration it) -> {
			it.addDefaultValue(new LoggingTestData.EventLogger());
			it.addFactory(LoggingTestData.StaticValueLoggingProvider.class);
			it.addFactory(LoggingTestData.DerivedProvider.class);
		});
		String expected =
				"Primary Values {\n" +
				"    PrimaryValue[EventLogger Managed[]]\n" +
				"    PrimaryValue[StateContainer Managed[null]]\n" +
				"}\n" +
				"Derived Values {\n" +
				"    org.eclipse.xpect.tests.state.LoggingTestData$StaticValueLoggingProvider {\n" +
				"        in PrimaryValue[EventLogger Managed[]]\n" +
				"        out DerivedValue[@Ann String StaticValueLoggingProvider.getAnnotatedValue()]\n" +
				"        out DerivedValue[String StaticValueLoggingProvider.getDefaultValue()]\n" +
				"    }\n" +
				"    org.eclipse.xpect.tests.state.LoggingTestData$DerivedProvider {\n" +
				"        in DerivedValue[@Ann String StaticValueLoggingProvider.getAnnotatedValue()]\n" +
				"        in DerivedValue[String StaticValueLoggingProvider.getDefaultValue()]\n" +
				"        in PrimaryValue[EventLogger Managed[]]\n" +
				"        out DerivedValue[@AnnDerived1 String DerivedProvider.getDerived1()]\n" +
				"        out DerivedValue[@AnnDerived2 String DerivedProvider.getDerived2()]\n" +
				"    }\n" +
				"}\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void testLogginTestDataManaged() {
		ResolvedConfiguration actual = StateTestUtil.newAnalyzedConfiguration((Configuration it) -> {
			it.addDefaultValue(new LoggingTestData.EventLogger());
			it.addFactory(LoggingTestData.StaticManagedLoggingProvider.class);
			it.addFactory(LoggingTestData.DerivedProvider.class);
		});
		String expected =
				"Primary Values {\n" +
				"    PrimaryValue[EventLogger Managed[]]\n" +
				"    PrimaryValue[StateContainer Managed[null]]\n" +
				"}\n" +
				"Derived Values {\n" +
				"    org.eclipse.xpect.tests.state.LoggingTestData$StaticManagedLoggingProvider {\n" +
				"        in PrimaryValue[EventLogger Managed[]]\n" +
				"        out DerivedValue[@Ann String StaticManagedLoggingProvider.getAnnotatedManaged()]\n" +
				"        out DerivedValue[String StaticManagedLoggingProvider.getDefaultManaged()]\n" +
				"    }\n" +
				"    org.eclipse.xpect.tests.state.LoggingTestData$DerivedProvider {\n" +
				"        in DerivedValue[@Ann String StaticManagedLoggingProvider.getAnnotatedManaged()]\n" +
				"        in DerivedValue[String StaticManagedLoggingProvider.getDefaultManaged()]\n" +
				"        in PrimaryValue[EventLogger Managed[]]\n" +
				"        out DerivedValue[@AnnDerived1 String DerivedProvider.getDerived1()]\n" +
				"        out DerivedValue[@AnnDerived2 String DerivedProvider.getDerived2()]\n" +
				"    }\n" +
				"}\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void multiConstructor1() {
		ResolvedConfiguration actual = StateTestUtil.newAnalyzedConfiguration((Configuration it) -> {
			it.addFactory(MultiConstructor.class);
		});
		String expected = 
				"Primary Values {\n" +
				"    PrimaryValue[StateContainer Managed[null]]\n" +
				"}\n" +
				"Derived Values {\n" +
				"    org.eclipse.xpect.tests.state.MultiConstructor {\n" +
				"        out DerivedValue[Object MultiConstructor.get()]\n" +
				"    }\n" +
				"    UNRESOLVED org.eclipse.xpect.tests.state.MultiConstructor {\n" +
				"        in (unresolved class java.lang.String)\n" +
				"        out DerivedValue[Object MultiConstructor.get()]\n" +
				"    }\n" +
				"    UNRESOLVED org.eclipse.xpect.tests.state.MultiConstructor {\n" +
				"        in (unresolved interface java.lang.CharSequence)\n" +
				"        out DerivedValue[Object MultiConstructor.get()]\n" +
				"    }\n" +
				"}\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void multiConstructor2() {
		ResolvedConfiguration base = StateTestUtil.newAnalyzedConfiguration((Configuration it) -> {
			it.addFactory(MultiConstructor.class);
		});
		ResolvedConfiguration actual = StateTestUtil.newAnalyzedConfiguration(base, (Configuration it) -> {
			it.addDefaultValue(String.class, "myString");
		});
		String expected = 
				"Primary Values {\n" +
				"    PrimaryValue[StateContainer Managed[null]]\n" +
				"    PrimaryValue[String Managed[myString]]\n" +
				"}\n" +
				"Derived Values {\n" +
				"    org.eclipse.xpect.tests.state.MultiConstructor {\n" +
				"        in PrimaryValue[String Managed[myString]]\n" +
				"        out DerivedValue[Object MultiConstructor.get()]\n" +
				"    }\n" +
				"}\n" +
				"Primary Values {\n" +
				"    PrimaryValue[StateContainer Managed[null]]\n" +
				"}\n" +
				"Derived Values {\n" +
				"    org.eclipse.xpect.tests.state.MultiConstructor {\n" +
				"        out DerivedValue[Object MultiConstructor.get()]\n" +
				"    }\n" +
				"    UNRESOLVED org.eclipse.xpect.tests.state.MultiConstructor {\n" +
				"        in (unresolved class java.lang.String)\n" +
				"        out DerivedValue[Object MultiConstructor.get()]\n" +
				"    }\n" +
				"    UNRESOLVED org.eclipse.xpect.tests.state.MultiConstructor {\n" +
				"        in (unresolved interface java.lang.CharSequence)\n" +
				"        out DerivedValue[Object MultiConstructor.get()]\n" +
				"    }\n" +
				"}\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void multiConstructor3() {
		ResolvedConfiguration base = StateTestUtil.newAnalyzedConfiguration((Configuration it) -> {
			it.addFactory(MultiConstructor.class);
		});
		ResolvedConfiguration actual = StateTestUtil.newAnalyzedConfiguration(base, (Configuration it) -> {
			it.addDefaultValue(CharSequence.class, "mySequence");
		});
		String expected = 
				"Primary Values {\n" +
				"    PrimaryValue[CharSequence Managed[mySequence]]\n" +
				"    PrimaryValue[StateContainer Managed[null]]\n" +
				"}\n" +
				"Derived Values {\n" +
				"    org.eclipse.xpect.tests.state.MultiConstructor {\n" +
				"        in PrimaryValue[CharSequence Managed[mySequence]]\n" +
				"        out DerivedValue[Object MultiConstructor.get()]\n" +
				"    }\n" +
				"    UNRESOLVED org.eclipse.xpect.tests.state.MultiConstructor {\n" +
				"        in (unresolved class java.lang.String)\n" +
				"        out DerivedValue[Object MultiConstructor.get()]\n" +
				"    }\n" +
				"}\n" +
				"Primary Values {\n" +
				"    PrimaryValue[StateContainer Managed[null]]\n" +
				"}\n" +
				"Derived Values {\n" +
				"    org.eclipse.xpect.tests.state.MultiConstructor {\n" +
				"        out DerivedValue[Object MultiConstructor.get()]\n" +
				"    }\n" +
				"    UNRESOLVED org.eclipse.xpect.tests.state.MultiConstructor {\n" +
				"        in (unresolved class java.lang.String)\n" +
				"        out DerivedValue[Object MultiConstructor.get()]\n" +
				"    }\n" +
				"    UNRESOLVED org.eclipse.xpect.tests.state.MultiConstructor {\n" +
				"        in (unresolved interface java.lang.CharSequence)\n" +
				"        out DerivedValue[Object MultiConstructor.get()]\n" +
				"    }\n" +
				"}\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void typeParam() {
		ResolvedConfiguration actual = StateTestUtil.newAnalyzedConfiguration((Configuration it) -> {
			it.addDefaultValue(Class.class, String.class);
			it.addFactory(TypeParam.class);
		});
		String expected = 
				"Primary Values {\n" +
				"    PrimaryValue[Class Managed[class java.lang.String]]\n" +
				"    PrimaryValue[StateContainer Managed[null]]\n" +
				"}\n" +
				"Derived Values {\n" +
				"    org.eclipse.xpect.tests.state.TypeParam {\n" +
				"        in PrimaryValue[Class Managed[class java.lang.String]]\n" +
				"        out DerivedValue[String TypeParam.get()]\n" +
				"    }\n" +
				"}\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void typeParamUnresolved() {
		ResolvedConfiguration actual = StateTestUtil.newAnalyzedConfiguration((Configuration it) -> {
			it.addFactory(TypeParam.class);
		});
		String expected = 
				"Primary Values {\n" +
				"    PrimaryValue[StateContainer Managed[null]]\n" +
				"}\n" +
				"Derived Values {\n" +
				"    UNRESOLVED org.eclipse.xpect.tests.state.TypeParam {\n" +
				"        in (unresolved class java.lang.Class)\n" +
				"        out DerivedValue[(unresolved) TypeParam.get()]\n" +
				"    }\n" +
				"}\n";
		TestUtil.assertEquals(expected, actual);
	}
}
