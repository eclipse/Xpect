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
import org.xpect.expectation.ISingleLineExpectationRegion;

public class SingleLineExpectationRegion extends AbstractExpectationRegion implements ISingleLineExpectationRegion {
	public SingleLineExpectationRegion(XpectInvocation statement, CharSequence document, int offset, int length, String separator, int openingSeparatorOffset) {
		super(statement, document, offset, length, separator, openingSeparatorOffset);
	}
}