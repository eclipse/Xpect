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

package org.eclipse.xpect.tests.xjm;

import java.lang.reflect.Constructor;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xpect.tests.XpectInjectorProvider;
import org.eclipse.xpect.XpectJavaModel;
import org.eclipse.xpect.ui.XpectPluginActivator;
import org.eclipse.xpect.util.XpectJavaModelManager;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.XtextRunner;

@InjectWith(XpectInjectorProvider.class)
@RunWith(XtextRunner.class)
public abstract class AbstractXjmTest {
	private static final Logger LOG = Logger.getLogger(AbstractXjmTest.class);

	/**
	 * When running as plugin test we need to ensure that Xpect was activated in the platform.
	 * Some other tests have side effect of doing that, but relying on that will make this
	 * test fail when running alone, or when test runner decides on different test order.
	 * This setup method ensures that when Xpect is activated in the running platform.
	 */
	@BeforeClass
	public static void ensureXpectActivated() {
		if (Platform.isRunning())
			LOG.debug("Activating xpect :: " + XpectPluginActivator.getInstance());
	}

	protected void assertXjm(Class<?> clazz) {
		try {
			XpectJavaModel javaModel = XpectJavaModelManager.createJavaModel(clazz);
			Constructor<?> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			String expected = constructor.newInstance().toString();
			String actual = javaModel.toString();
			Assert.assertEquals(expected.trim(), actual.trim());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
