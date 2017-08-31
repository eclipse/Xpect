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

package org.eclipse.xpect.ui.services;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.validation.CheckType;
import org.eclipse.xtext.validation.Issue;
import org.junit.ComparisonFailure;
import org.eclipse.xpect.XpectInvocation;

public class LiveTestIssueFactory {
	private final static String _PREF = "org.eclipse.xpect.live_test_execution.";
	public final static String ISSUE_CODE_TEST_ERROR = _PREF + "test_error";
	public final static String ISSUE_CODE_TEST_EXP_CHANGE = _PREF + "expectation_change";
	public final static String ISSUE_CODE_TEST_EXP_DELETE = _PREF + "expectation_delete";
	public final static String ISSUE_CODE_TEST_EXP_INSERT = _PREF + "expectation_insert";
	public final static String ISSUE_CODE_TEST_FAIL = _PREF + "test_fail";
	public final static String ISSUE_CODE_TEST_IGNORE = _PREF + "test_ignore";
	public final static String ISSUE_CODE_TEST_SUCCESS = _PREF + "test_success";

	protected Issue.IssueImpl createIssue(XpectInvocation inv) {
		Issue.IssueImpl issue = new Issue.IssueImpl();
		issue.setType(CheckType.FAST);
		issue.setUriToProblem(EcoreUtil.getURI(inv));
		return issue;
	}

	protected Issue.IssueImpl createIssueOnInvocation(XpectInvocation inv) {
		Issue.IssueImpl issue = createIssue(inv);
		ITextRegion region = findXPECTKeywordRegion(inv);
		if (region != null) {
			issue.setOffset(region.getOffset());
			issue.setLength(region.getLength());
		} else {
			issue.setOffset(0);
			issue.setLength(1);
		}
		return issue;
	}

	public Issue createSuccessIssue(XpectInvocation inv) {
		Issue.IssueImpl issue = createIssueOnInvocation(inv);
		issue.setMessage("Xpect test passed successfully.");
		issue.setCode(ISSUE_CODE_TEST_SUCCESS);
		issue.setSeverity(Severity.INFO);
		return issue;
	}

	protected void exceptionToIssues(XpectInvocation inv, AssertionError exception, IAcceptor<Issue> result) {
		Issue.IssueImpl issue = createIssueOnInvocation(inv);
		issue.setMessage("Xpect test failed: " + exception.getMessage());
		issue.setCode(ISSUE_CODE_TEST_FAIL);
		issue.setSeverity(Severity.ERROR);
		result.accept(issue);
	}

	protected void exceptionToIssues(XpectInvocation inv, ComparisonFailure exception, IAcceptor<Issue> result) {
		ComparisonFailure cf = (ComparisonFailure) exception;
		String actualDoc = cf.getActual();
		String expectedDoc = inv.getFile().getDocument();
		int fromStart = 0, fromEnd = 1, length = Math.min(actualDoc.length(), expectedDoc.length());
		while (fromStart < length && actualDoc.charAt(fromStart) == expectedDoc.charAt(fromStart)) {
			fromStart++;
		}
		while (fromEnd < length && actualDoc.charAt(actualDoc.length() - fromEnd) == expectedDoc.charAt(expectedDoc.length() - fromEnd)) {
			fromEnd++;
		}
		while (fromStart > 0 && (!Character.isWhitespace(expectedDoc.charAt(fromStart)) || expectedDoc.charAt(fromStart) == '\n' || expectedDoc.charAt(fromStart) == '\r')) {
			fromStart--;
		}
		while (fromEnd > 0 && !Character.isWhitespace(expectedDoc.charAt(expectedDoc.length() - fromEnd))) {
			fromEnd--;
		}
		if (fromStart + fromEnd >= expectedDoc.length()) {
			String text = actualDoc.substring(fromStart, actualDoc.length() - fromEnd + 1);
			Issue.IssueImpl issue = createIssue(inv);
			issue.setMessage("Xpect test failed because the actual test result has '" + text + "' here.");
			issue.setOffset(fromStart);
			issue.setLength(0);
			issue.setCode(ISSUE_CODE_TEST_EXP_INSERT);
			issue.setData(new String[] { text });
			result.accept(issue);
		} else {
			fromStart++;
			int endIndex = actualDoc.length() - fromEnd;
			if (endIndex <= fromStart) {
				Issue.IssueImpl issue = createIssue(inv);
				issue.setMessage("Xpect test failed because this text is not part of the actual test result.");
				issue.setOffset(fromStart);
				issue.setLength(expectedDoc.length() - fromStart - fromEnd);
				issue.setCode(ISSUE_CODE_TEST_EXP_DELETE);
				result.accept(issue);
			} else {
				String text = actualDoc.substring(fromStart, endIndex);
				Issue.IssueImpl issue = createIssue(inv);
				issue.setMessage("Xpect test failed because in the actual test result this text is: " + text);
				issue.setOffset(fromStart);
				issue.setLength(expectedDoc.length() - fromStart - fromEnd);
				issue.setCode(ISSUE_CODE_TEST_EXP_CHANGE);
				issue.setData(new String[] { text });
				result.accept(issue);
			}
		}
	}

	public void exceptionToIssues(XpectInvocation inv, Throwable exception, IAcceptor<Issue> result) {
		if (exception instanceof ComparisonFailure) {
			exceptionToIssues(inv, (ComparisonFailure) exception, result);
		} else if (exception instanceof AssertionError) {
			exceptionToIssues(inv, (AssertionError) exception, result);
		} else {
			Issue.IssueImpl issue = createIssueOnInvocation(inv);
			issue.setMessage("Xpect test failed: " + exception.getMessage());
			issue.setCode(ISSUE_CODE_TEST_ERROR);
			issue.setSeverity(Severity.ERROR);
			result.accept(issue);
		}
	}

	protected ITextRegion findXPECTKeywordRegion(XpectInvocation inv) {
		ICompositeNode node = NodeModelUtils.getNode(inv);
		for (ILeafNode l : node.getLeafNodes()) {
			if (l.getGrammarElement() instanceof Keyword && l.getText().equals("XPECT")) {
				return l.getTextRegion();
			}
		}
		return null;
	}
}
