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

package org.eclipse.xpect;

import org.eclipse.xtext.resource.FileExtensionProvider;
import org.eclipse.xpect.registry.AbstractDelegatingModule;
import org.eclipse.xpect.services.XtFileExtensionProvider;

import com.google.inject.Binder;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XtRuntimeModule extends AbstractDelegatingModule {

	public void configure(Binder binder) {
		overrideAndBackup(binder, FileExtensionProvider.class, XtFileExtensionProvider.class);
	}

}
