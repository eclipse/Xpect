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

package org.eclipse.xpect.xtext.lib.tests;

import static com.google.common.collect.Iterables.filter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xpect.XjmXpectMethod;
import org.eclipse.xpect.XpectFile;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.XpectInvocation;
import org.eclipse.xpect.XpectReplace;
import org.eclipse.xpect.registry.AbstractDelegatingModule;
import org.eclipse.xpect.registry.DefaultBinding;
import org.eclipse.xpect.setup.XpectGuiceModule;
import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.state.Configuration;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.state.XpectStateAnnotation;
import org.eclipse.xpect.text.IRegion;
import org.eclipse.xpect.text.Region;
import org.eclipse.xpect.ui.services.XtResourceValidator;
import org.eclipse.xpect.ui.util.XpectFileAccess;
import org.eclipse.xpect.util.JvmAnnotationUtil;
import org.eclipse.xpect.xtext.lib.setup.ThisResource;
import org.eclipse.xpect.xtext.lib.setup.XtextValidatingSetup;
import org.eclipse.xpect.xtext.lib.tests.ValidationTestModuleSetup.IssuesByLineProvider;
import org.eclipse.xpect.xtext.lib.util.IssueOverlapsRangePredicate;
import org.eclipse.xpect.xtext.lib.util.NextLine.NextLineProvider;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.inject.Binder;
import com.google.inject.Key;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectGuiceModule
@XpectImport(IssuesByLineProvider.class)
public class ValidationTestModuleSetup extends AbstractDelegatingModule {
	@XpectStateAnnotation
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ConsumedIssues {
		Severity[]value();
	}

	@XpectStateAnnotation
	@Retention(RetentionPolicy.RUNTIME)
	public @interface IssuesByLine {
	}

	@XpectSetupFactory
	@XpectReplace(XtextValidatingSetup.class)
	public static class IssuesByLineProvider extends XtextValidatingSetup {

		private Multimap<IRegion, Issue> issuesByLine = null;

		public IssuesByLineProvider(@ThisResource XtextResource resource) {
			super(resource);
		}

		@Override
		protected List<Issue> collectIssues() {
			return Lists.newArrayList(collectIssuesByLine().get(ValidationTestModuleSetup.UNMATCHED));
		}

		@Creates(IssuesByLine.class)
		public Multimap<IRegion, Issue> collectIssuesByLine() {
			if (issuesByLine == null) {
				TestingResourceValidator validator = (TestingResourceValidator) getResource().getResourceServiceProvider().getResourceValidator();
				issuesByLine = validator.validateDelegateAndMapByOffset(getResource(), CheckMode.ALL, CancelIndicator.NullImpl, null);
			}
			return issuesByLine;
		}
	}

	public static class SeverityPredicate implements Predicate<Issue> {

		private Severity[] severities;

		public SeverityPredicate(Severity... severity) {
			super();
			this.severities = severity;
		}

		@Override
		public boolean apply(Issue input) {
			Severity e = input.getSeverity();
			for (Severity s : severities) {
				if (s == e)
					return true;
			}
			return false;
		}
	}

	public static class TestingResourceValidator extends XtResourceValidator {

		protected Set<Severity> getExpectedSeverity(XpectInvocation inv) {
			if (inv == null || inv.eIsProxy())
				return null;
			XjmXpectMethod method = inv.getMethod();
			if (method == null || method.eIsProxy())
				return null;
			JvmOperation operation = method.getJvmMethod();
			if (operation == null || operation.eIsProxy())
				return null;
			ConsumedIssues annotation = JvmAnnotationUtil.getJavaAnnotation(operation, ConsumedIssues.class);
			if (annotation == null)
				return null;
			EnumSet<Severity> result = EnumSet.copyOf(Lists.newArrayList(annotation.value()));
			return result;
		}

		@Override
		public List<Issue> validateDelegate(Resource resource, CheckMode mode, CancelIndicator indicator, Configuration fileConfig) {
			return Lists.newArrayList(validateDelegateAndMapByOffset(resource, mode, indicator, fileConfig).get(UNMATCHED));
		}

		public Multimap<IRegion, Issue> validateDelegateAndMapByOffset(Resource resource, CheckMode mode, CancelIndicator indicator, Configuration fileConfig) {
			Multimap<IRegion, Issue> result = LinkedHashMultimap.create();
			List<Issue> issuesFromDelegate = super.validateDelegate(resource, mode, indicator, fileConfig);
			if (resource instanceof XtextResource && ((XtextResource) resource).getParseResult() != null) {
				XtextResource xresource = (XtextResource) resource;
				if (issuesFromDelegate != null && !issuesFromDelegate.isEmpty()) {
					XpectFile xpectFile = XpectFileAccess.getXpectFile(resource);
					ValidationTestConfig config = new ValidationTestConfig(xpectFile.<ValidationTestConfig> createSetupInitializer());
					Set<Issue> issues = Sets.newLinkedHashSet(issuesFromDelegate);
					Set<Issue> matched = Sets.newHashSet();
					for (XpectInvocation inv : xpectFile.getInvocations()) {
						Set<Severity> severities = getExpectedSeverity(inv);
						if (severities != null) {
							IRegion region = new NextLineProvider(xresource, inv).getNextLine();
							List<Issue> selected = Lists.newArrayList(filter(issues, new IssueOverlapsRangePredicate(region, severities)));
							result.putAll(region, selected);
							matched.addAll(selected);
						}
					}
					issues.removeAll(matched);
					result.putAll(UNMATCHED, filter(issues, config.getIgnoreFilter()));
				}
			} else {
				result.putAll(UNMATCHED, issuesFromDelegate);
			}
			if (fileConfig != null) {
				fileConfig.addValue(IssuesByLine.class, result);
			}
			return ImmutableMultimap.copyOf(result);
		}
	}

	public static final IRegion UNMATCHED = new Region("(unmatched)", -1, 0);

	public void configure(Binder binder) {
		binder.bind(IResourceValidator.class).to(TestingResourceValidator.class);
		binder.bind(IResourceValidator.class).annotatedWith(DefaultBinding.class).to(getOriginalType(Key.get(IResourceValidator.class)));
	}

}
