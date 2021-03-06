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

package org.eclipse.xpect.runner;

import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.state.Configuration;
import org.eclipse.xpect.state.Creates;

@XpectSetupFactory
public class ArgumentContributor {
	public void contributeArguments(Configuration[] configurations) {
	}

	@Creates
	public ArgumentContributor create() {
		return this;
	}
}
