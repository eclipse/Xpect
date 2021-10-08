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

package org.eclipse.xpect.formatting;

import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectFormatter extends AbstractDeclarativeFormatter {

	@Override
	protected void configureFormatting(FormattingConfig c) {
		// It's usually a good idea to activate the following three statements.
		// They will add and preserve newlines around comments
		// c.setLinewrap(0, 1,
		// 2).before(getGrammarAccess().getSL_COMMENTRule());
		// c.setLinewrap(0, 1,
		// 2).before(getGrammarAccess().getML_COMMENTRule());
		// c.setLinewrap(0, 1, 1).after(getGrammarAccess().getML_COMMENTRule());
	}
}
