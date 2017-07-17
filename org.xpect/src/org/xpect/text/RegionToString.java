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

package org.xpect.text;

public class RegionToString {
	private IRegion region;
	private int vicinityChars = 16;

	public RegionToString with(IRegion region) {
		this.region = region;
		return this;
	}

	public RegionToString withVicinityChars(int vicinityChars) {
		this.vicinityChars = vicinityChars;
		return this;
	}

	@Override
	public String toString() {
		CharSequence document = region.getDocument();
		int offset = region.getOffset();
		int end = offset + region.getLength();
		if (offset >= 0 && end <= document.length()) {
			CharSequence prefix = CharSequences.getPrefix(document, offset, vicinityChars);
			CharSequence infix = document.subSequence(offset, end);
			CharSequence postfix = CharSequences.getPostfix(document, end, vicinityChars);
			String result = prefix + ">>>" + infix + "<<<" + postfix;
			return result.replace("\n", "\\n");
		} else {
			return "Invalid Region! offset:" + offset + " end: " + end + " Document:" + document;
		}
	}
}
