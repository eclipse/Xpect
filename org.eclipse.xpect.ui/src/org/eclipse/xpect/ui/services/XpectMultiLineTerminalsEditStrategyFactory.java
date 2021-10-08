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

package org.eclipse.xpect.ui.services;

import org.eclipse.xtext.ui.editor.autoedit.MultiLineTerminalsEditStrategy;

public class XpectMultiLineTerminalsEditStrategyFactory extends MultiLineTerminalsEditStrategy.Factory {
	@Override
	public MultiLineTerminalsEditStrategy newInstance(String leftTerminal, String indentationString, String rightTerminal) {
		if (" * ".equals(indentationString))
			indentationString = "   ";
		return super.newInstance(leftTerminal, indentationString, rightTerminal);
	}

}
