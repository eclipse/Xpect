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

package org.eclipse.xpect.registry;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IResourceServiceProvider;

import com.google.inject.Module;

public interface IEmfFileExtensionInfo {

	public interface IXtextFileExtensionInfo extends IEmfFileExtensionInfo {

		String getLanguageID();

		LazyClass<IResourceServiceProvider> getResourceServiceProvider();

		LazyClass<IResourceServiceProvider> getResourceUIServiceProvider();

		LazyClass<Module> getRuntimeModule();

		LazyClass<Module> getUIModule();

		LazyClass<Module> getSharedModule();
	}

	public interface Registry {
		Registry INSTANCE = new FileExtensionInfoRegistry();

		IEmfFileExtensionInfo getEmfFileExtensionInfo(String fileExtension);

		Collection<IEmfFileExtensionInfo> getFileExtensionInfos();
	}

	Set<String> getFileExtensions();

	LazyClass<Resource.Factory> getResourceFactory();
}
