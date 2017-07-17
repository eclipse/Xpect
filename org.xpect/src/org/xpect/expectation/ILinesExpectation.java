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

package org.xpect.expectation;

import org.xpect.XpectImport;
import org.xpect.expectation.impl.LinesExpectationImpl;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectImport(LinesExpectationImpl.class)
public interface ILinesExpectation {
	void assertEquals(Iterable<?> string);

	void assertEquals(String message, Iterable<?> string);
}
