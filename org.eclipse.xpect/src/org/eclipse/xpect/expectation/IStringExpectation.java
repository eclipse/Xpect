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

package org.eclipse.xpect.expectation;

import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.expectation.impl.StringExpectationImpl;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectImport(StringExpectationImpl.class)
public interface IStringExpectation {
	void assertEquals(Object string);
}
