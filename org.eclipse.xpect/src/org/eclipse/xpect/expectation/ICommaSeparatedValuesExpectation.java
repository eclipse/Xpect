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

package org.eclipse.xpect.expectation;

import java.util.List;

import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.expectation.impl.CommaSeparatedValuesExpectationImpl;

import com.google.common.base.Predicate;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectImport(CommaSeparatedValuesExpectationImpl.class)
public interface ICommaSeparatedValuesExpectation {
	public interface Value {
		String getText();

		boolean isNegated();

		boolean isWildcard();
	}

	void assertEquals(Iterable<?> string);

	void assertEquals(Iterable<?> string, Predicate<String> predicate);

	void assertEquals(Predicate<String> predicate);

	List<Value> getExpectedValues();
}
