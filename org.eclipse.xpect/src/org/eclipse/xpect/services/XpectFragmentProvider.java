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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IFragmentProvider;
import org.eclipse.xpect.XpectFile;
import org.eclipse.xpect.XpectInvocation;

public class XpectFragmentProvider implements IFragmentProvider {

	protected String getFragment(XpectInvocation obj) {
		return obj.getId();
	}

	public String getFragment(EObject obj, Fallback fallback) {
		if (obj instanceof XpectInvocation)
			return getFragment((XpectInvocation) obj);
		return fallback.getFragment(obj);
	}

	public EObject getEObject(Resource resource, String fragment, Fallback fallback) {
		if (fragment != null && fragment.contains("~"))
			for (EObject obj : resource.getContents())
				if (obj instanceof XpectFile)
					return ((XpectFile) obj).getInvocation(fragment);
		return fallback.getEObject(fragment);
	}

}
