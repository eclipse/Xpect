/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.xpect.ui.quickfix;

import java.lang.reflect.Method;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.xpect.ui.services.LiveTestIssueFactory;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectQuickfixProvider extends DefaultQuickfixProvider {

	@Fix(LiveTestIssueFactory.ISSUE_CODE_TEST_EXP_DELETE)
	public void delete(final Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, "Delete", "Delete text to match actual test result.", null, new IModification() {
			public void apply(IModificationContext context) throws BadLocationException {
				IXtextDocument xtextDocument = context.getXtextDocument();
				xtextDocument.replace(issue.getOffset(), issue.getLength(), "");
			}
		});
	}

	@Fix(LiveTestIssueFactory.ISSUE_CODE_TEST_EXP_INSERT)
	public void insert(final Issue issue, IssueResolutionAcceptor acceptor) {
		final String newText = issue.getData()[0];
		acceptor.accept(issue, "Insert " + quote(newText), "Insert text to match actual test result.", null, new IModification() {
			public void apply(IModificationContext context) throws BadLocationException {
				IXtextDocument xtextDocument = context.getXtextDocument();
				xtextDocument.replace(issue.getOffset(), 0, newText);
			}
		});
	}

	@Fix(LiveTestIssueFactory.ISSUE_CODE_TEST_EXP_CHANGE)
	public void replace(final Issue issue, IssueResolutionAcceptor acceptor) {
		final String newText = issue.getData()[0];
		acceptor.accept(issue, "Replace with " + quote(newText), "Replace with actual test result.", null, new IModification() {
			public void apply(IModificationContext context) throws BadLocationException {
				IXtextDocument xtextDocument = context.getXtextDocument();
				xtextDocument.replace(issue.getOffset(), issue.getLength(), newText);
			}
		});
	}

	private String quote(final String newText) {
		String label = newText;
		if (newText.length() > 20) {
			label = newText.substring(0, 20) + "...";
		}
		label = "'" + label.replace("\n", "\\n").replace("\r", "\\r") + "'";
		return label;
	}

	public boolean hasExplicitResolutionFor(final String issueCode) {
		if (issueCode == null)
			return false;
		Iterable<Method> methods = collectMethods(getClass(), issueCode);
		return methods.iterator().hasNext();
	}

}
