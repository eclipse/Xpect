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

import java.util.List;

import org.eclipse.xpect.util.IDifferencer.ISimilarityFunction;
import org.eclipse.xpect.util.IDifferencer.MatchKind;

public interface ITextDifferencer {

	public interface ILineDiff {
		MatchKind getKind();

		List<ISegmentDiff> getSegmentDiffs();
	}

	public interface ISegment {
		ISegment getNext();

		ISegment getPrevious();

		int getTokenIndex();

		boolean isHidden();

		boolean isWrap();

		String toString();
	}

	public interface ISegmentDiff {
		MatchKind getKind();

		ISegment getLeft();

		ISegment getRight();

		float getSimilarity();
	}

	public interface ITextDiff {
		List<ILineDiff> getLines();
	}

	public interface ITextDiffConfig<T> extends ISimilarityFunction<ISegment> {

		boolean isHidden(T token, String segment);

		Iterable<String> toSegments(T token);
	}

	<T> ITextDiff diff(List<T> leftTokens, List<T> rightTokens, ITextDiffConfig<T> config);

}
