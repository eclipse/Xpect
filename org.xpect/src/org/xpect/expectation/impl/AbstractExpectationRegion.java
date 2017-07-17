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

package org.xpect.expectation.impl;

import org.xpect.XpectInvocation;
import org.xpect.expectation.IExpectationRegion;
import org.xpect.parameter.StatementRelatedRegion;

public abstract class AbstractExpectationRegion extends StatementRelatedRegion implements IExpectationRegion {
	private final CharSequence document;

	public AbstractExpectationRegion(XpectInvocation statement, CharSequence document, int offset, int length, String separator, int openingSeparatorOffset) {
		super(statement, offset, length);
		this.document = document;
		this.separator = separator;
		this.opeingSeparatorOffset = openingSeparatorOffset;
	}

	public String getDocument() {
		return document.toString();
	}

	private final int opeingSeparatorOffset;
	private final String separator;

	public int getOpeningSeparatorOffset() {
		return opeingSeparatorOffset;
	}

	public String getSeparator() {
		return separator;
	}

}