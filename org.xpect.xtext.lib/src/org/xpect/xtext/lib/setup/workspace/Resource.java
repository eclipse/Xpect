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

package org.xpect.xtext.lib.setup.workspace;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.xpect.xtext.lib.setup.FileSetupContext;

import com.google.common.collect.Lists;

public class Resource<R extends IResource> {
	private final List<IResourceConfigurator<? super R>> configurators = Lists.newArrayList();

	public void add(IResourceConfigurator<? super R> configurator) {
		this.configurators.add(configurator);
	}

	protected void configure(FileSetupContext ctx, R resource) throws IOException {
		for (IResourceConfigurator<? super R> configurator : configurators)
			configurator.configure(ctx, resource);
	}

	public List<IResourceConfigurator<? super R>> getConfigurators() {
		return configurators;
	}

}
