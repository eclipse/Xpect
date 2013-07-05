/*******************************************************************************
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.xpect.services;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.xpect.XpectConstants;
import org.xpect.registry.ILanguageInfo;
import org.xpect.setup.IXpectRunnerSetup.IClassSetupContext;
import org.xpect.util.URIDelegationHandler;

import com.google.inject.Injector;

/**
 * @author Simon Kaufmann - Initial contribution and API
 */
public class XtResourceServiceProviderProvider implements IResourceServiceProvider.Provider {

	public static final XtResourceServiceProviderProvider INSTANCE = new XtResourceServiceProviderProvider();

	private XtResourceServiceProviderProvider() {
	}

	private ThreadLocal<IClassSetupContext> setupContext = new ThreadLocal<IClassSetupContext>();

	public synchronized void setSetupContext(IClassSetupContext setupContext) {
		this.setupContext.set(setupContext);
	}

	private synchronized IClassSetupContext getSetupContext() {
		return setupContext.get();
	}

	public IResourceServiceProvider get(URI uri, String contentType) {
		String ext = new URIDelegationHandler().getOriginalFileExtension(uri.lastSegment());
		if (ext != null) {
			ILanguageInfo info = ILanguageInfo.Registry.INSTANCE.getLanguageByFileExtension(ext);
			if (info == null)
				throw new IllegalStateException("No Xtext language configuration found for file extension '" + ext + "'.");
			Injector injector = info.getInjector();

			// consider modules
			if (getSetupContext() != null) {
				injector = getSetupContext().getInjector(uri);
			}

			if (injector != null) {
				return injector.getInstance(IResourceServiceProvider.class);
			}
		}
		return ILanguageInfo.Registry.INSTANCE.getLanguageByFileExtension(XpectConstants.XT_FILE_EXT).getInjector()
				.getInstance(IResourceServiceProvider.class);
	}

}
