/*******************************************************************************
 * Copyright (c) 2012 itemis AG and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/
package org.eclipse.xpect.mwe2.statefullexer;

public abstract class StateDescription {

	public int getPdaState() {
		return 0;
	}

	public int getFsmState() {
		return 0;
	}

	public Object getGroup() {
		return null;
	}

	public void getCloneSource() {
	}

	public Object getToken() {
		return null;
	}
}
