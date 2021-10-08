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

package org.eclipse.xpect.util;

import java.util.List;

/**
 * @author Moritz Eysholdt
 */
public interface IDifferencer {

	public interface ISimilarityFunction<T> {
		final float EQUAL = 0.0f;
		final float UPPER_SIMILARITY_BOUND = 1.0f;

		float similarity(T object1, T object2);
	}

	public enum MatchKind {
		EQUAL, SIMILAR, LEFT_ONLY, RIGHT_ONLY
	}

	public interface Match {
		final int NO_INDEX = -1;
		final float EQUAL = ISimilarityFunction.EQUAL;
		final float UNEQUAL = ISimilarityFunction.UPPER_SIMILARITY_BOUND;

		int getLeft();

		int getRight();

		float getSimilarity();

		MatchKind getKind();
	}

	public <T> List<Match> diff(List<T> left, List<T> right, ISimilarityFunction<? super T> similarityfunction);

}
