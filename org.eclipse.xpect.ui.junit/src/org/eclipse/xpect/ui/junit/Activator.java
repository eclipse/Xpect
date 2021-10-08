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

package org.eclipse.xpect.ui.junit;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.eclipse.xpect.registry.ITestSuiteInfo;
import org.eclipse.xpect.registry.TestSuiteInfoRegistry;
import org.eclipse.xpect.ui.XpectPluginActivator;
import org.eclipse.xpect.ui.junit.registry.UITestSuiteInfoRegistry;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.eclipse.xpect.ui.junit"; //$NON-NLS-1$
	private static Activator plugin;

	public Activator() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		XpectPluginActivator.class.getName(); // activate bundle org.eclipse.xpect.ui
		((TestSuiteInfoRegistry.Delegate) ITestSuiteInfo.Registry.INSTANCE).setDelegate(new UITestSuiteInfoRegistry());
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

}
