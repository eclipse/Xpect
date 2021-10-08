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

package org.eclipse.xpect.expectation.impl;

import org.eclipse.xpect.XpectInvocation;
import org.eclipse.xpect.expectation.IExpectationRegion;
import org.eclipse.xpect.parameter.StatementRelatedRegion;

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