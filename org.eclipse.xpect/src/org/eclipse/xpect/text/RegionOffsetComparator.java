/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.eclipse.xpect.text;

import java.util.Comparator;

/**
 * 
 * @author Moritz Eysholdt
 */
public class RegionOffsetComparator implements Comparator<IRegion> {

	public int compare(IRegion o1, IRegion o2) {
		return o1.getOffset() - o2.getOffset();
	}

}
