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

package org.eclipse.xpect.ui.quickfix;

import java.util.List;

import org.eclipse.xtext.ui.editor.quickfix.IssueResolution;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionProvider;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionProviderExtension;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xpect.registry.DefaultBinding;

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
