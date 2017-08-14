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
