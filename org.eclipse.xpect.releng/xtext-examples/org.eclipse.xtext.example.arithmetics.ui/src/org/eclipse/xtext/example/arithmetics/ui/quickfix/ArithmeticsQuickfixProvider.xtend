/*******************************************************************************
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.example.arithmetics.ui.quickfix

import org.eclipse.xtext.example.arithmetics.validation.ArithmeticsValidator
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider
import org.eclipse.xtext.ui.editor.quickfix.Fix
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor
import org.eclipse.xtext.validation.Issue

/**
 * Custom quickfixes.
 *
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#quick-fixes
 */
class ArithmeticsQuickfixProvider extends DefaultQuickfixProvider {

	@Fix(ArithmeticsValidator.NORMALIZABLE)
	def normalize(Issue issue, IssueResolutionAcceptor acceptor) {
		val string = issue.data.get(0)
		acceptor.accept(issue, "Replace with "+string, "Replace expression with '"+string+"'", "upcase.png", [
			xtextDocument.replace(issue.getOffset(), issue.getLength(), string);
		])
	}
}
