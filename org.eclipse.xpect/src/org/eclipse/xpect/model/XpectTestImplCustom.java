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

import org.eclipse.xtext.common.types.JvmDeclaredType;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectTestImplCustom extends XpectTestImpl {
	@Override
	public void setDeclaredSuite(JvmDeclaredType newDeclaredSuite) {
		XpectFileImplCustom file = (XpectFileImplCustom) getFile();
		file.unsetJavaModel();
		super.setDeclaredSuite(newDeclaredSuite);
	}
}
