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
