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
@Deprecated
@XpectSetupComponent
public class File extends org.eclipse.xpect.xtext.lib.setup.emf.Resource {

	public File() {
		super();
	}

	public File(org.eclipse.xpect.xtext.lib.setup.generic.File file) {
		super(file);
	}

	public File(String name) {
		super(new org.eclipse.xpect.xtext.lib.setup.generic.File(name));
	}

}
