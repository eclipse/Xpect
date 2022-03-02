/**
 * Copyright (c) 2012, 2022 TypeFox GmbH and itemis AG.
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

import java.lang.reflect.Constructor;

import org.junit.Assert;
import org.junit.Test;

public class SetupInitilizerTest {
  @Test
  public void specificConstructorStrings() {
    SetupInitializerTestData.SiT<Object> si = new SetupInitializerTestData.SiT<Object>(null);
    String[] strings = { "a", "b" };
    Constructor<?> c = si.findConstructor(SetupInitializerTestData.A.class, strings);
    Assert.assertSame(SetupInitializerTestData.A.class, c.getDeclaringClass());
    Assert.assertEquals(2, c.getParameterTypes().length);
  }
  
  @Test
  public void varargConstructorStrings() {
    SetupInitializerTestData.SiT<Object> si = new SetupInitializerTestData.SiT<Object>(null);
    String[] strings = { "a", "b", "c" };
    Constructor<?> c = si.findConstructor(SetupInitializerTestData.A.class, strings);
    Assert.assertSame(SetupInitializerTestData.A.class, c.getDeclaringClass());
    Assert.assertEquals(1, c.getParameterTypes().length);
  }
  
  @Test(expected = RuntimeException.class)
  public void mixedVarargConstructor() {
    SetupInitializerTestData.SiT<Object> si = new SetupInitializerTestData.SiT<Object>(null);
    Object[] objs = { Double.valueOf(5.0), "b", "c" };
    Constructor<?> c = si.findConstructor(SetupInitializerTestData.A.class, objs);
    Assert.assertSame(SetupInitializerTestData.A.class, c.getDeclaringClass());
    Assert.assertEquals(1, c.getParameterTypes().length);
  }
  
  @Test
  public void varargConstructorStrings2() {
    SetupInitializerTestData.SiT<Object> si = new SetupInitializerTestData.SiT<Object>(null);
    String[] strings = { "a" };
    Constructor<?> c = si.findConstructor(SetupInitializerTestData.A.class, strings);
    Assert.assertSame(SetupInitializerTestData.A.class, c.getDeclaringClass());
    Assert.assertEquals(1, c.getParameterTypes().length);
    (c.getParameterTypes()[0]).isArray();
  }
  
  @Test
  public void varargConstructorStrings_empty() {
    SetupInitializerTestData.SiT<Object> si = new SetupInitializerTestData.SiT<Object>(null);
    String[] strings = {};
    Constructor<?> c = si.findConstructor(SetupInitializerTestData.A.class, strings);
    Assert.assertSame(SetupInitializerTestData.A.class, c.getDeclaringClass());
  }
  
  @Test
  public void varargConstructor_double() {
    SetupInitializerTestData.SiT<Object> si = new SetupInitializerTestData.SiT<Object>(null);
    Double[] dbls = { Double.valueOf(1.3), Double.valueOf(4.0) };
    Constructor<?> c = si.findConstructor(SetupInitializerTestData.A.class, dbls);
    Assert.assertSame(SetupInitializerTestData.A.class, c.getDeclaringClass());
  }
}
