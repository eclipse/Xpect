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
import org.xpect.text.Region;

public class StatementRelatedRegion extends Region implements IStatementRelatedRegion {

	private final XpectInvocation statement;

	public StatementRelatedRegion(XpectInvocation statement, int offset, int length) {
		super(statement.getFile().getDocument(), offset, length);
		this.statement = statement;
	}

	public XpectInvocation getStatement() {
		return statement;
	}

}
