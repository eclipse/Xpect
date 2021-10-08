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

package org.eclipse.xpect.xtext.lib.setup.emf;

import java.util.List;

import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.setup.XpectSetupComponent;

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
