package org.xpect.ui;

import org.osgi.framework.BundleContext;
import org.xpect.registry.DelegatingExtensionInfoRegistry;
import org.xpect.registry.DelegatingLanguageRegistry;
import org.xpect.registry.IExtensionInfo;
import org.xpect.registry.ILanguageInfo;
import org.xpect.ui.internal.XpectActivator;
import org.xpect.ui.registry.UIExtensionInfoRegistry;
import org.xpect.ui.registry.UILanugageRegistry;
import org.xpect.ui.util.UIBundleInfoRegistry;
import org.xpect.ui.util.UIJavaReflectAccess;
import org.xpect.ui.util.UIXtInjectorProvider;
import org.xpect.util.IBundleInfo;
import org.xpect.util.IJavaReflectAccess;
import org.xpect.util.IXtInjectorProvider;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectPluginActivator extends XpectActivator {

	public final static String XPECT_EDITOR_ID = XpectPluginActivator.ORG_XPECT_XPECT;
	public final static String XT_EDITOR_ID = "org.xpect.Xt";

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		((DelegatingExtensionInfoRegistry) IExtensionInfo.Registry.INSTANCE).setDelegate(new UIExtensionInfoRegistry());
		((DelegatingLanguageRegistry) ILanguageInfo.Registry.INSTANCE).setDelegate(new UILanugageRegistry());
		((IXtInjectorProvider.Delegate) IXtInjectorProvider.INSTANCE).setDelegate(new UIXtInjectorProvider());
		((IJavaReflectAccess.Delegate) IJavaReflectAccess.INSTANCE).setDelegate(new UIJavaReflectAccess());
		((IBundleInfo.Delegate) IBundleInfo.Registry.INSTANCE).setDelegate(new UIBundleInfoRegistry());
	}

}
