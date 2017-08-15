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

package org.eclipse.xpect.parameter;

import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.state.Creates;

import com.google.common.base.Preconditions;

@XpectSetupFactory
public class StringProvider {
	private final StringRegion region;

	public StringProvider(StringRegion region) {
		Preconditions.checkNotNull(region);
		this.region = region;
	}

	@Creates
	public String getStringValue() {
		String text = region.getRegionText();
		return text;
	}
}
