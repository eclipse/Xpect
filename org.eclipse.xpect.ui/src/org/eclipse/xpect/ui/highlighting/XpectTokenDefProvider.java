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

package org.eclipse.xpect.ui.highlighting;

import java.io.InputStream;
import java.util.Map;

import org.eclipse.xtext.parser.antlr.AntlrTokenDefProvider;
import org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectTokenDefProvider extends AntlrTokenDefProvider implements IAntlrTokenFileProvider {

	@Override
	public Map<Integer, String> getTokenDefMap() {
		this.setAntlrTokenFileProvider(this);
		return super.getTokenDefMap();
	}

	public InputStream getAntlrTokenFile() {
		ClassLoader classLoader = getClass().getClassLoader();
		String resourcePath = "org/eclipse/xpect/lexer/XpectHI.tokens";
		InputStream result = classLoader.getResourceAsStream(resourcePath);
		return result;
	}

	protected boolean isKeywordToken(String antlrTokenDef) {
		return true;
	}

}
