/*******************************************************************************
 * Copyright (c) 2012 itemis AG and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/
package org.eclipse.xpect.mwe2.statefullexer;

import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.Grammar;

public interface ILexerStatesProvider {

	public interface ILexerState {
		Set<EObject> getElements();

		int getID();

		Iterable<ILexerStateTransition> getOutgoingTransitions();

		String getName();

		Set<Object /* TerminalRule | String */> getTokens();
	}

	public interface ILexerStateTransition {
		AbstractElement getElement();

		ILexerState getSource();

		ILexerState getTarget();

		Object getToken();
	}

	public interface ILexerStates extends NfaWithTransitions<ILexerState, ILexerStateTransition>, NfaWithAllStates<ILexerState> {
	}

	public ILexerStates getStates(Grammar grammar);

}
