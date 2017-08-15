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

package org.eclipse.xpect.expectation.impl;

import org.eclipse.xpect.XpectInvocation;
import org.eclipse.xpect.expectation.IMultiLineExpectationRegion;

public class MultiLineExpectationRegion extends AbstractExpectationRegion implements IMultiLineExpectationRegion {
	private final int closingSeparatorOffset;
	private final String indentation;

	public MultiLineExpectationRegion(XpectInvocation statement, CharSequence document, int offset, int length, String indentation, String separator, int openingSeparatorOffset,
			int closingSeparatorOffset) {
		super(statement, document, offset, length, separator, openingSeparatorOffset);
		this.indentation = indentation;
		this.closingSeparatorOffset = closingSeparatorOffset;
	}

	public int getClosingSeparatorOffset() {
		return closingSeparatorOffset;
	}

	public String getIndentation() {
		return indentation;
	}

	public boolean isSingleLine() {
		return indentation == null;
	}
}