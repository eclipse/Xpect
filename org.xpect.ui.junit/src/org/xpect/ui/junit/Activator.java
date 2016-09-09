package org.xpect.ui.junit;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.xpect.registry.ITestSuiteInfo;
import org.xpect.registry.TestSuiteInfoRegistry;
import org.xpect.ui.junit.registry.UITestSuiteInfoRegistry;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.xpect.ui.junit"; //$NON-NLS-1$
	private static Activator plugin;

	public Activator() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
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
