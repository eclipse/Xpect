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

package org.eclipse.xpect.services;

import java.util.Set;

import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.impl.AbstractNullSafeConverter;
import org.eclipse.xtext.nodemodel.INode;

import com.google.common.collect.ImmutableSet;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectValueConverter extends DefaultTerminalConverters {

	public class IDConverter extends AbstractNullSafeConverter<String> {

		private Set<String> allKeywords = ImmutableSet.copyOf(GrammarUtil.getAllKeywords(getGrammar()));

		@Override
		protected String internalToValue(String string, INode node) {
			return string.replaceAll("[\\^\\s]", "");
		}

		@Override
		protected String internalToString(String value) {
			if (allKeywords.contains(value))
				return "^" + value;
			return value;
		}
	}

	private IDConverter idValueConverter;

	@ValueConverter(rule = "INVOCATION")
	public IValueConverter<String> INVOCATION() {
		if (idValueConverter == null)
			idValueConverter = new IDConverter();
		return idValueConverter;
	}

}
