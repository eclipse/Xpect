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

package org.xpect.xtext.lib.setup.emf;

import java.util.List;

import org.xpect.XpectImport;
import org.xpect.setup.XpectSetupComponent;

import com.google.common.collect.Lists;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectSetupComponent
@XpectImport(ResourceSetDefaultsSetup.class)
public class ResourceSet {
	private final List<ResourceFactory> factories = Lists.newArrayList();

	public void add(ResourceFactory file) {
		factories.add(file);
	}

	public List<ResourceFactory> getFactories() {
		return factories;
	}
}
