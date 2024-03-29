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

package org.eclipse.xpect.ui.services;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IResourceFactory;
import org.eclipse.xtext.ui.shared.Access;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xpect.XpectJavaModel;
import org.eclipse.xpect.ui.util.XpectUtil;
import org.eclipse.xpect.util.IXtInjectorProvider;

import com.google.inject.Injector;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XtResourceFactory implements IResourceFactory {

	public Resource createResource(URI uri) {
		for (Pair<IStorage, IProject> p : Access.getIStorage2UriMapper().get().getStorages(uri))
			if (p.getFirst() instanceof IFile) {
				IFile file = (IFile) p.getFirst();
				XpectJavaModel javaModel = XpectUtil.loadJavaModel(file);
				Injector injector = IXtInjectorProvider.INSTANCE.getInjector(javaModel, uri);
				if (injector != null)
					return injector.getInstance(IResourceFactory.class).createResource(uri);
			}
		return Registry.INSTANCE.getFactory(uri.appendFileExtension("xpect")).createResource(uri);
	}

}
