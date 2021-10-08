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

public class OffsetToString {
	private CharSequence document;
	private int offset;
	private int vicinityChars = 16;

	public OffsetToString with(int offset, CharSequence document) {
		this.document = document;
		this.offset = offset;
		return this;
	}

	public OffsetToString withVicinityChars(int vicinityChars) {
		this.vicinityChars = vicinityChars;
		return this;
	}

	@Override
	public String toString() {
		CharSequence prefix = CharSequences.getPrefix(document, offset, vicinityChars);
		CharSequence postfix = CharSequences.getPostfix(document, offset, vicinityChars);
		String result = prefix + "|" + postfix;
		return result.replace("\n", "\\n");
	}
}
