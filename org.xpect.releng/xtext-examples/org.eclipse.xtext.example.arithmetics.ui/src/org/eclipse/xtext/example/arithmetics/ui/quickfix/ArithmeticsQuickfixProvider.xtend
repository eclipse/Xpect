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
