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

package org.xpect.parameter;

import org.xpect.XpectInvocation;
import org.xpect.text.CharSequences;
import org.xpect.text.IRegion;

public class OffsetRegion extends DerivedRegion {

	private final int matchedOffset;
	private final DerivedRegion matchedRegion;

	public OffsetRegion(IStatementRelatedRegion origin, int matchedOffset) {
		super(origin, -1, -1);
		this.matchedRegion = null;
		this.matchedOffset = matchedOffset;
	}

	public OffsetRegion(IStatementRelatedRegion origin, int offset, int length) {
		super(origin, offset, length);
		String val = getRegionText();
		int add = val.indexOf('|');
		if (add >= 0)
			val = val.substring(0, add) + val.substring(add + 1);
		else
			add = 0;
		XpectInvocation statement = getStatement();
		IStatementRelatedRegion extendedRegion = statement.getExtendedRegion();
		int nodeOffset = extendedRegion.getOffset() + extendedRegion.getLength();
		String text = statement.getFile().getDocument();

		int result = -1;
		do {
			result = text.indexOf(val, nodeOffset);
			nodeOffset = result + 1;
		} while (statement.getFile().getInvocationAt(result) != null);

		if (result >= 0) {
			this.matchedOffset = result + add;
			if (add > 0)
				this.matchedRegion = createMatchedRegion(result, length - 1);
			else
				this.matchedRegion = createMatchedRegion(result, length);
		} else
			throw new RuntimeException("OFFSET '" + val + "' not found.");
	}

	private DerivedRegion createMatchedRegion(int offset, int length) {
		return new DerivedRegion(this, offset, length);
	}

	public int getMatchedOffset() {
		return matchedOffset;
	}

	public IRegion getMatchedRegion() {
		return matchedRegion;
	}

	@Override
	public String toString() {
		if (matchedOffset < 0 || matchedRegion == null) {
			return "(not available)";
		} else {
			int offset = matchedRegion.getOffset();
			int end = offset + matchedRegion.getLength();
			CharSequence document = getDocument();
			String prefix = CharSequences.getPrefix(document, offset, 24);
			String postfix = CharSequences.getPostfix(document, end, 24);
			String before = document.subSequence(offset, matchedOffset).toString();
			String after = document.subSequence(matchedOffset, end).toString();
			return prefix + ">>>" + before + "|" + after + "<<<" + postfix;
		}
	}

}
