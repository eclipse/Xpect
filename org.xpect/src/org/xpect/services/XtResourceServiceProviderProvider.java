/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.xpect.services;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.xpect.XpectConstants;
import org.xpect.registry.ILanguageInfo;
import org.xpect.runner.XpectRunner;
import org.xpect.util.IXtInjectorProvider;

import com.google.inject.Injector;

/**
 * @author Simon Kaufmann - Initial contribution and API
 */
public class XtResourceServiceProviderProvider implements IResourceServiceProvider.Provider {

	public static final XtResourceServiceProviderProvider INSTANCE = new XtResourceServiceProviderProvider();

	private XtResourceServiceProviderProvider() {
	}

	public IResourceServiceProvider get(URI uri, String contentType) {
		if (XpectRunner.INSTANCE != null) {
			Injector injector = IXtInjectorProvider.INSTANCE.getInjector(XpectRunner.INSTANCE.getXpectJavaModel(), uri);
			if (injector != null)
				return injector.getInstance(IResourceServiceProvider.class);
		}
		Injector injector = ILanguageInfo.Registry.INSTANCE.getLanguageByFileExtension(XpectConstants.XT_FILE_EXT).getInjector();
		return injector.getInstance(IResourceServiceProvider.class);
	}
}
