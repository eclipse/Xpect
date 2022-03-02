/**
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 */
package org.eclipse.xpect.tests.state;

import org.eclipse.xpect.state.Creates;

public class Singleton2 {
	private final String param;

	public Singleton2(@MyAnnotaion1 String param) {
		this.param = param;
	}

	@Creates(Annotation.class)
	public StringBuffer getBuffer() {
		return new StringBuffer(("buffer " + param));
	}
}
