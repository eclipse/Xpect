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

package org.xpect.registry;

import java.util.Collection;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.google.common.base.Function;

public interface IExtensionInfo {

	public interface Registry {

		Registry INSTANCE = EcorePlugin.IS_ECLIPSE_RUNNING ? new DelegatingExtensionInfoRegistry() : new StandaloneExtensionRegistry();

		Collection<String> getExtensionPoints();

		Collection<IExtensionInfo> getExtensions(String extensionPointName);
	}

	Collection<String> getAttributes();

	String getAttributeValue(String name);

	Function<String, Class<?>> getClassLoader();

	String getLocation();

}
