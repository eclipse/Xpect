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

package org.eclipse.xpect.services;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xpect.XpectConstants;
import org.eclipse.xpect.registry.ILanguageInfo;
import org.eclipse.xpect.runner.XpectRunner;
import org.eclipse.xpect.runner.XpectTestGlobalState;
import org.eclipse.xpect.util.IXtInjectorProvider;

import com.google.inject.Injector;

/**
 * @author Simon Kaufmann - Initial contribution and API
 */
public class XtResourceServiceProviderProvider implements IResourceServiceProvider.Provider {

	public static final XtResourceServiceProviderProvider INSTANCE = new XtResourceServiceProviderProvider();

	private XtResourceServiceProviderProvider() {
	}

	public IResourceServiceProvider get(URI uri, String contentType) {
		if (XpectTestGlobalState.INSTANCE.model() != null) {
			Injector injector = IXtInjectorProvider.INSTANCE.getInjector(XpectTestGlobalState.INSTANCE.model(), uri);
			if (injector != null)
				return injector.getInstance(IResourceServiceProvider.class);
		}
		Injector injector = ILanguageInfo.Registry.INSTANCE.getLanguageByFileExtension(XpectConstants.XT_FILE_EXT).getInjector();
		return injector.getInstance(IResourceServiceProvider.class);
	}
}
