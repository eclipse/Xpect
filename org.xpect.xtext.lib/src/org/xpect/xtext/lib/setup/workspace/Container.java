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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;
import org.xpect.xtext.lib.setup.FileSetupContext;

import com.google.common.collect.Lists;

public class Container<C extends IContainer> extends Resource<C> {

	private final List<IResourceFactory<?, ? super C>> memberFactories = Lists.newArrayList();

	public void add(IResourceFactory<?, ? super C> factory) {
		this.memberFactories.add(factory);
	}

	protected void createMembers(FileSetupContext ctx, C container, Workspace.Instance instance) throws IOException, CoreException {
		for (IResourceFactory<?, ? super C> factory : memberFactories)
			factory.create(ctx, container, instance);
	}

	public List<IResourceFactory<?, ? super C>> getMemberFactories() {
		return memberFactories;
	}

	@SuppressWarnings("unchecked")
	public <T extends IResourceFactory<?, ?>> List<T> getMembers(Class<T> type) {
		List<T> result = Lists.newArrayList();
		for (IResourceFactory<?, ? super C> member : memberFactories)
			if (type.isInstance(member))
				result.add((T) member);
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T extends IResourceFactory<?, ?>> T getMember(Class<T> type) {
		for (IResourceFactory<?, ? super C> member : memberFactories)
			if (type.isInstance(member))
				return (T) member;
		return null;
	}

}
