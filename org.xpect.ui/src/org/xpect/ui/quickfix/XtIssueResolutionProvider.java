package org.xpect.ui.quickfix;

import java.util.List;

import org.eclipse.xtext.ui.editor.quickfix.IssueResolution;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionProvider;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionProviderExtension;
import org.eclipse.xtext.validation.Issue;
import org.xpect.registry.DefaultBinding;

import com.google.inject.Inject;

public class XtIssueResolutionProvider implements IssueResolutionProvider, IssueResolutionProviderExtension {

	@Inject
	@DefaultBinding
	private IssueResolutionProvider original;

	@Inject
	private XpectQuickfixProvider xpect;

	@Override
	public boolean hasResolutionFor(String issueCode) {
		return xpect.hasExplicitResolutionFor(issueCode) || original.hasResolutionFor(issueCode);
	}

	@Override
	public List<IssueResolution> getResolutions(Issue issue) {
		if (xpect.hasExplicitResolutionFor(issue.getCode())) {
			return xpect.getResolutions(issue);
		} else {
			return original.getResolutions(issue);
		}
	}

	@Override
	public boolean hasResolutionFor(Issue issue) {
		if (xpect.hasExplicitResolutionFor(issue.getCode())) {
			return true;
		}
		if (original instanceof IssueResolutionProviderExtension) {
			return ((IssueResolutionProviderExtension) original).hasResolutionFor(issue);
		}
		return false;
	}

}
