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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xpect.XjmMethod;
import org.eclipse.xpect.XjmTest;
import org.eclipse.xpect.XpectJavaModel;

public class XpectEObjectAtOffsetHelper extends EObjectAtOffsetHelper {

	@Override
	public EObject getCrossReferencedElement(INode node) {
		EObject element = super.getCrossReferencedElement(node);
		if (!element.eIsProxy()) {
			if (element instanceof XjmMethod)
				return ((XjmMethod) element).getJvmMethod();
			if (element instanceof XpectJavaModel) {
				XjmTest testOrSuite = ((XpectJavaModel) element).getTestOrSuite();
				if (testOrSuite != null && !testOrSuite.eIsProxy())
					return testOrSuite.getJvmClass();
			}
		}
		return element;
	}
}
