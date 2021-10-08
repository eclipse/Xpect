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

package org.eclipse.xpect.text;

import java.util.Collection;

/**
 * 
 * @author Moritz Eysholdt
 */
public class Patch implements IPatch {

	private final Collection<IChange> changes;

	public Patch(Collection<IChange> changes) {
		super();
		this.changes = changes;
	}

	public Collection<IChange> getChanges() {
		return changes;
	}

}
