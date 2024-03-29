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

package org.eclipse.xpect.model;

import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.util.Strings;

public class XjmXpectMethodImplCustom extends XjmXpectMethodImpl {

	@Override
	public int getParameterCount() {
		JvmOperation method = getJvmMethod();
		if (method != null && !method.eIsProxy())
			return method.getParameters().size();
		return 0;
	}

	@Override
	public String toString() {
		String contributions = getImportedContributions();
		String body = "@Xpect public void " + getJvmMethod().getSimpleName() + "();";
		if (Strings.isEmpty(contributions))
			return body;
		return body + " // Imports: " + contributions;
	}

}
