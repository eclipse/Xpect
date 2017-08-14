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

package org.eclipse.xtext.example.domainmodel.xpect.tests.modify;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.example.domainmodel.domainmodel.DomainModel;
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity;
import org.eclipse.xtext.example.domainmodel.domainmodel.Property;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class DeleteUtil {

	@Inject
	private TypeReferences typeReferencers;

	@Inject
	private IJvmModelAssociations associations;

	public void deleteEntity(Entity entity) {
		DomainModel dmodel = EcoreUtil2.getContainerOfType(entity, DomainModel.class);
		for (Entity ent : EcoreUtil2.getAllContentsOfType(dmodel, Entity.class)) {
			JvmParameterizedTypeReference superType = ent.getSuperType();
			if (superType != null && associations.getPrimarySourceElement(superType.getType()) == entity)
				EcoreUtil.remove(superType);
		}
		for (Property prop : EcoreUtil2.getAllContentsOfType(dmodel, Property.class)) {
			JvmTypeReference type = prop.getType();
			if (type != null && associations.getPrimarySourceElement(type.getType()) == entity)
				prop.setType(typeReferencers.getTypeForName(Void.TYPE, dmodel));
		}
		EcoreUtil.remove(entity);
	}
}
