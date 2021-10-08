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

package org.eclipse.xpect.xtext.lib.setup.emf;

import org.eclipse.xpect.setup.XpectSetupComponent;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectSetupComponent
public class ThisResource extends Resource {

	public ThisResource() {
		super(new org.eclipse.xpect.xtext.lib.setup.generic.ThisFile());
	}

	public ThisResource(org.eclipse.xpect.xtext.lib.setup.generic.ThisFile file) {
		super(file);
	}

	public ThisResource(String name) {
		super(name);
	}

}
