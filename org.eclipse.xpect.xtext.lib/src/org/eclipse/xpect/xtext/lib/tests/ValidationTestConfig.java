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

package org.eclipse.xpect.xtext.lib.tests;

import java.util.List;

import org.eclipse.xtext.diagnostics.Diagnostic;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.setup.ISetupInitializer;
import org.eclipse.xpect.setup.XpectSetupComponent;
import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.setup.XpectSetupRoot;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.xtext.lib.tests.ValidationTestConfig.IgnoredIssues;
import org.eclipse.xpect.xtext.lib.tests.ValidationTestConfig.LinkingAndValidationIssues;
import org.eclipse.xpect.xtext.lib.tests.ValidationTestConfig.SyntaxAndLinkingAndValidationIssues;
import org.eclipse.xpect.xtext.lib.tests.ValidationTestConfig.ValidationErrorsAndWarnings;
import org.eclipse.xpect.xtext.lib.tests.ValidationTestConfig.ValidationWarnings;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

@XpectSetupFactory
@XpectSetupRoot
@XpectImport({ IgnoredIssues.class, SyntaxAndLinkingAndValidationIssues.class, LinkingAndValidationIssues.class, ValidationErrorsAndWarnings.class, ValidationWarnings.class })
public class ValidationTestConfig {

	@XpectSetupComponent
	public static class IgnoredIssues implements Predicate<Issue> {
		private List<IssueFilter> filters = Lists.newArrayList();

		public void add(IssueFilter filter) {
			this.filters.add(filter);
		}

		public boolean apply(Issue input) {
			for (IssueFilter filter : filters)
				if (filter.shouldIgnore(input))
					return false;
			return true;
		}
	}

	public static interface IssueFilter {
		boolean shouldIgnore(Issue issue);
	}

	@XpectSetupComponent
	public static class LinkingAndValidationIssues implements IssueFilter {
		public boolean shouldIgnore(Issue issue) {
			return !Diagnostic.SYNTAX_DIAGNOSTIC.equals(issue.getCode());
		}
	}

	@XpectSetupComponent
	public static class SyntaxAndLinkingAndValidationIssues implements IssueFilter {
		public boolean shouldIgnore(Issue issue) {
			return true;
		}
	}

	@XpectSetupComponent
	public static class ValidationErrorsAndWarnings implements IssueFilter {
		public boolean shouldIgnore(Issue issue) {
			return !Diagnostic.SYNTAX_DIAGNOSTIC.equals(issue.getCode()) && !Diagnostic.LINKING_DIAGNOSTIC.equals(issue.getCode());
		}
	}

	@XpectSetupComponent
	public static class ValidationWarnings implements IssueFilter {
		public boolean shouldIgnore(Issue issue) {
			return issue.getSeverity() == Severity.WARNING;
		}
	}

	private IgnoredIssues ignoredIssues = null;

	public ValidationTestConfig(ISetupInitializer<ValidationTestConfig> init) {
		init.initialize(this);
	}

	public void add(IgnoredIssues ignored) {
		Preconditions.checkArgument(this.ignoredIssues == null, "ignoredIssues can only be set once.");
		this.ignoredIssues = ignored;
	}

	public Predicate<Issue> getIgnoreFilter() {
		if (ignoredIssues == null)
			return Predicates.alwaysTrue();
		return ignoredIssues;
	}

	@Creates
	public ValidationTestConfig getThis() {
		return this;
	}
}
