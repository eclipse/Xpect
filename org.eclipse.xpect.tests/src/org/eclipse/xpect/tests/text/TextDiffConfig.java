/**
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 */
package org.eclipse.xpect.tests.text;

import java.util.Collections;

import org.eclipse.xpect.text.ITextDifferencer;
import org.eclipse.xpect.text.StringEndsSimilarityFunction;

import com.google.common.collect.Lists;

public class TextDiffConfig implements ITextDifferencer.ITextDiffConfig<String> {
	@Override
	public boolean isHidden(String token, String segment) {
		for (char c : token.toCharArray()) {
			if (!Character.isWhitespace(c)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Iterable<String> toSegments(String token) {
		return Collections.unmodifiableList(Lists.newArrayList(token));
	}

	@Override
	public float similarity(ITextDifferencer.ISegment object1, ITextDifferencer.ISegment object2) {
		return new StringEndsSimilarityFunction().similarity(object1.toString(), object2.toString());
	}
}
