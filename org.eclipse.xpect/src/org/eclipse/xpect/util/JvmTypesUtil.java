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

package org.eclipse.xpect.util;

import java.util.Set;

import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;

import com.google.common.collect.Sets;

public class JvmTypesUtil {

	private static void collectAllSuperTypes(JvmType type, Set<JvmType> visited) {
		if (type == null || type.eIsProxy() || !visited.add(type))
			return;
		if (type instanceof JvmDeclaredType)
			for (JvmTypeReference ref : ((JvmDeclaredType) type).getSuperTypes())
				collectAllSuperTypes(ref.getType(), visited);
	}

	public static Set<JvmType> getAllSuperTypes(JvmDeclaredType type) {
		Set<JvmType> visited = Sets.newLinkedHashSet();
		for (JvmTypeReference ref : ((JvmDeclaredType) type).getSuperTypes())
			collectAllSuperTypes(ref.getType(), visited);
		return visited;
	}

	public static Set<JvmType> getSelfAndAllSuperTypes(JvmDeclaredType type) {
		Set<JvmType> visited = Sets.newLinkedHashSet();
		visited.add(type);
		for (JvmTypeReference ref : ((JvmDeclaredType) type).getSuperTypes())
			collectAllSuperTypes(ref.getType(), visited);
		return visited;
	}
}
