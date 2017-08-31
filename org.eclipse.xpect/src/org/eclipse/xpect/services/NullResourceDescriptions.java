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

package org.eclipse.xpect.services;

import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;

/**
 * workaround to fix https://github.com/meysholdt/Xpect/issues/21
 * 
 * The Xpect language doesn't declare/export any named elements at all
 * 
 * @author Moritz Eysholdt
 */
public class NullResourceDescriptions implements IResourceDescriptions {

	public boolean isEmpty() {
		return true;
	}

	public Iterable<IEObjectDescription> getExportedObjects() {
		return Collections.emptyList();
	}

	public Iterable<IEObjectDescription> getExportedObjects(EClass type, QualifiedName name, boolean ignoreCase) {
		return Collections.emptyList();
	}

	public Iterable<IEObjectDescription> getExportedObjectsByType(EClass type) {
		return Collections.emptyList();
	}

	public Iterable<IEObjectDescription> getExportedObjectsByObject(EObject object) {
		return Collections.emptyList();
	}

	public Iterable<IResourceDescription> getAllResourceDescriptions() {
		return Collections.emptyList();
	}

	public IResourceDescription getResourceDescription(URI uri) {
		return null;
	}

}
