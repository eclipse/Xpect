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

package org.eclipse.xpect.ide.services;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

public abstract class Lexer extends org.eclipse.xtext.ide.editor.contentassist.antlr.internal.Lexer {
	public Lexer() {
		super();
	}

	public Lexer(CharStream input) {
		super(input);
	}

	public Lexer(CharStream input, RecognizerSharedState state) {
		super(input, state);
	}

	public boolean matches(String foo) {
		state.backtracking++;
		int start = input.mark();
		try {
			match(foo);
		} catch (RecognitionException re) {
			System.err.println("impossible: " + re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed = false;
		return success;
	}
}
