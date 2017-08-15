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

import java.util.Set;

import org.eclipse.xtext.ui.validation.MarkerTypeProvider;
import org.eclipse.xtext.validation.CheckType;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xpect.registry.DefaultBinding;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;

public class XtMarkerTypeProvider extends MarkerTypeProvider {

	public static String MARKER_ID_SUCCESS = "org.eclipse.xpect.ui.live_test_execution_success";
	public static String MARKER_ID_FAIL = "org.eclipse.xpect.ui.live_test_execution_failed";
	public static String MARKER_ID_ERROR = "org.eclipse.xpect.ui.live_test_execution_error";
	public static String MARKER_ID_IGNORED = "org.eclipse.xpect.ui.live_test_execution_ignored";
	public static Set<String> MARKER_ALL = ImmutableSet.<String> builder().add(MARKER_ID_ERROR).add(MARKER_ID_FAIL).add(MARKER_ID_IGNORED).add(MARKER_ID_SUCCESS).build();

	@Inject
	@DefaultBinding
	private MarkerTypeProvider delegate;

	@Override
	public CheckType getCheckType(String markerType) {
		if (MARKER_ALL.contains(markerType))
			return CheckType.FAST;
		return delegate.getCheckType(markerType);
	}

	@Override
	public String getMarkerType(Issue issue) {
		String code = issue.getCode();
		if (LiveTestIssueFactory.ISSUE_CODE_TEST_SUCCESS.equals(code)) {
			return MARKER_ID_SUCCESS;
		}
		if (LiveTestIssueFactory.ISSUE_CODE_TEST_IGNORE.equals(code)) {
			return MARKER_ID_IGNORED;
		}
		if (LiveTestIssueFactory.ISSUE_CODE_TEST_FAIL.equals(code)) {
			return MARKER_ID_FAIL;
		}
		if (LiveTestIssueFactory.ISSUE_CODE_TEST_ERROR.equals(code)) {
			return MARKER_ID_ERROR;
		}
		return delegate.getMarkerType(issue);
	}

}
