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

package org.eclipse.xpect.text;

public class CharSequences {
	private static final String DOTS = "...";

	public static <T extends CharSequence> T getMostSimilarCandiate(Iterable<? extends T> candiates, T defautValue, CharSequence similarTo) {
		if (similarTo == null || similarTo.length() == 0)
			return defautValue;
		T result = defautValue;
		int rating = 0;
		for (T candiate : candiates) {
			int r = getSimilarity(candiate, similarTo);
			if (r > rating) {
				rating = r;
				result = candiate;
			}
		}
		return result;
	}

	public static String getPostfix(CharSequence document, int offset, int maxLenght) {
		if (offset + maxLenght > document.length())
			return document.subSequence(offset, document.length()).toString();
		return document.subSequence(offset, offset + (maxLenght - DOTS.length())) + DOTS;
	}

	public static String getPrefix(CharSequence document, int offset, int maxLenght) {
		if (offset < maxLenght)
			return document.subSequence(0, offset).toString();
		return DOTS + document.subSequence(offset - (maxLenght - DOTS.length()), offset);
	}

	public static int getSimilarity(CharSequence s1, CharSequence s2) {
		int result = 0;
		int length = Math.min(s1.length(), s2.length());
		for (int i = 0; i < length; i++)
			if (s1.charAt(i) == s2.charAt(i))
				result++;
			else
				break;
		for (int i = 1; i <= length; i++)
			if (s1.charAt(s1.length() - i) == s2.charAt(s2.length() - i))
				result++;
			else
				break;
		return result;
	}

	public static boolean isWhitespace(CharSequence seq) {
		for (int i = 0; i < seq.length(); i++)
			if (!Character.isWhitespace(seq.charAt(i)))
				return false;
		return true;
	}

	public static String toSingleLineString(Object seq, int maxLenght) {
		if (seq == null)
			return null;
		String result = seq.toString().replace("\n", "\\n");
		if (result.length() <= maxLenght)
			return result;
		return result.substring(0, maxLenght - DOTS.length()) + DOTS;
	}
}
