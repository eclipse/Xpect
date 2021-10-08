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

package org.eclipse.xpect.text;

import java.util.List;

import org.eclipse.xtext.util.Strings;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class WhitespaceTokenizer implements Function<String, List<String>> {

	public List<String> apply(String input) {
		List<String> result = Lists.newArrayList();
		if (!Strings.isEmpty(input)) {
			int lastOffset = 0;
			boolean lastWs = Character.isWhitespace(input.charAt(0));
			for (int offset = 1; offset < input.length(); offset++) {
				char c = input.charAt(offset);
				boolean ws = Character.isWhitespace(c);
				if (ws != lastWs) {
					String token = input.substring(lastOffset, offset);
					result.add(token);
					lastOffset = offset;
					lastWs = ws;
				}
			}
			String token = input.substring(lastOffset, input.length());
			result.add(token);
		}
		return result;
	}

}
