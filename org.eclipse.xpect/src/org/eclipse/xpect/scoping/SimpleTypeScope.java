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

package org.eclipse.xpect.scoping;

import java.util.Collections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;

@SuppressWarnings("restriction")
public class SimpleTypeScope implements IScope {

	private IJvmTypeProvider typeProvider;

	public SimpleTypeScope(IJvmTypeProvider typeProvider) {
		super();
		this.typeProvider = typeProvider;
	}

	public Iterable<IEObjectDescription> getAllElements() {
		return Collections.emptyList();
	}

	public Iterable<IEObjectDescription> getElements(EObject object) {
		return Collections.emptyList();
	}

	public Iterable<IEObjectDescription> getElements(QualifiedName name) {
		return Collections.singletonList(getSingleElement(name));
	}

	public IEObjectDescription getSingleElement(EObject object) {
		return EObjectDescription.create(QualifiedName.create(object.eResource().getURI().segments()), object);
	}

	public IEObjectDescription getSingleElement(QualifiedName name) {
		JvmType eObject = typeProvider.findTypeByName(name.toString());
		if (eObject != null) {
			return EObjectDescription.create(name, eObject);
		}
		return null;
	}

}
