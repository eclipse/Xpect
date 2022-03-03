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
import org.eclipse.xpect.state.ManagedImpl;
import org.eclipse.xpect.state.StateContainer;
import org.eclipse.xtext.util.IAcceptor;
import org.junit.Assert;
import org.junit.Test;

public class StateContainerTest {
	@Test
	public void testPrimaryValue() {
		StateContainer cnt = new StateContainer((Configuration it) -> {
			it.addDefaultValue("Foo");
		});
		Assert.assertEquals("Foo", cnt.get(String.class).get());
	}

	@Test
	public void testPrimaryValueAnn() {
		StateContainer cnt = new StateContainer((Configuration it) -> {
			it.addValue(TestData.Ann.class, "Foo");
		});
		Assert.assertEquals("Foo", cnt.get(String.class, TestData.Ann.class).get());
	}

	@Test
	public void testPrimaryManaged() {
		StateContainer cnt = new StateContainer((Configuration it) -> {
			it.addDefaultValue(String.class, new ManagedImpl<String>("Foo"));
		});
		Assert.assertEquals("Foo", cnt.get(String.class).get());
	}

	@Test
	public void testPrimaryManagedAnn() {
		StateContainer cnt = new StateContainer((Configuration it) -> {
			it.addValue(TestData.Ann.class, String.class, new ManagedImpl<String>("Foo"));
		});
		Assert.assertEquals("Foo", cnt.get(String.class, TestData.Ann.class).get());
	}

	@Test
	public void testDerivedValue() {
		StateContainer cnt = new StateContainer((Configuration it) -> {
			it.addFactory(TestData.StaticValueProvider.class);
		});
		Assert.assertEquals("DefaultValue", cnt.get(String.class).get());
		Assert.assertEquals("AnnotatedValue", cnt.get(String.class, TestData.Ann.class).get());
	}

	@Test
	public void testDerivedManaged() {
		IAcceptor<Configuration> _function = (Configuration it) -> {
			it.addFactory(TestData.StaticManagedProvider.class);
		};
		StateContainer cnt = new StateContainer(_function);
		Assert.assertEquals("DefaultManaged", cnt.get(String.class).get());
		Assert.assertEquals("AnnotatedManaged", cnt.get(String.class, TestData.Ann.class).get());
	}
}
