package org.xpect.ui.services;

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
import org.xpect.XpectInvocation;

public class LiveTestIssueFactory {
	private final static String _PREF = "org.xpect.live_test_execution.";
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
		String actual = cf.getActual();
		String document = inv.getFile().getDocument();
		int fromStart = 0, fromEnd = 1, length = Math.min(actual.length(), document.length());
		while (fromStart < length && actual.charAt(fromStart) == document.charAt(fromStart)) {
			fromStart++;
		}
		while (fromEnd < length && actual.charAt(actual.length() - fromEnd) == document.charAt(document.length() - fromEnd)) {
			fromEnd++;
		}
		while (fromStart > 0 && !Character.isWhitespace(document.charAt(fromStart))) {
			fromStart--;
		}
		while (fromEnd > 0 && !Character.isWhitespace(document.charAt(document.length() - fromEnd))) {
			fromEnd--;
		}
		fromStart++;
		if (fromStart + fromEnd >= document.length()) {
			String text = actual.substring(fromStart, actual.length() - fromEnd);
			Issue.IssueImpl issue = createIssue(inv);
			issue.setMessage("Xpect test failed because the actual test result has '" + text + "' here.");
			issue.setOffset(fromStart);
			issue.setLength(0);
			issue.setCode(ISSUE_CODE_TEST_EXP_INSERT);
			issue.setData(new String[] { text });
			result.accept(issue);
		} else {
			int endIndex = actual.length() - fromEnd;
			if (endIndex <= fromStart) {
				Issue.IssueImpl issue = createIssue(inv);
				issue.setMessage("Xpect test failed because this text is not part of the actual test result.");
				issue.setOffset(fromStart);
				issue.setLength(document.length() - fromStart - fromEnd);
				issue.setCode(ISSUE_CODE_TEST_EXP_DELETE);
				result.accept(issue);
			} else {
				String text = actual.substring(fromStart, endIndex);
				Issue.IssueImpl issue = createIssue(inv);
				issue.setMessage("Xpect test failed because in the actual test result this text is: " + text);
				issue.setOffset(fromStart);
				issue.setLength(document.length() - fromStart - fromEnd);
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
