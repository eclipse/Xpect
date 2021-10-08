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

package org.eclipse.xpect.parameter;

public class IntegerRegion extends DerivedRegion {
	public IntegerRegion(IStatementRelatedRegion origin) {
		super(origin, -1, -1);
	}

	public IntegerRegion(IStatementRelatedRegion origin, int offset, int length) {
		super(origin, offset, length);
	}

	public int getIntegerValue() {
		String text = getRegionText();
		if (text != null)
			return Integer.valueOf(text);
		return 0;
	}
}
