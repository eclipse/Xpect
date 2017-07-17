package org.xpect;

import org.eclipse.xtext.resource.FileExtensionProvider;
import org.xpect.registry.AbstractDelegatingModule;
import org.xpect.services.XtFileExtensionProvider;

import com.google.inject.Binder;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XtRuntimeModule extends AbstractDelegatingModule {

	public void configure(Binder binder) {
		overrideAndBackup(binder, FileExtensionProvider.class, XtFileExtensionProvider.class);
	}

}
