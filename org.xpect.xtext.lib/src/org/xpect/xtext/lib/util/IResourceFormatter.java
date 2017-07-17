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

package org.xpect.xtext.lib.util;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.google.common.base.Function;

public class IResourceFormatter implements Function<IResource, String> {

	public String format(IResource input) {
		if (input instanceof IContainer)
			return format((IContainer) input);
		if (input != null)
			return input.getClass().getSimpleName() + " " + input.getName();
		return "null";
	}

	protected String format(IContainer container) {
		StringBuilder builder = new StringBuilder();
		builder.append(container.getClass().getSimpleName());
		builder.append(" ");
		builder.append(container.getName());
		builder.append(" {");
		try {
			for (IResource member : container.members()) {
				builder.append("\n  ");
				builder.append(format(member).replace("\n", "\n  "));
			}
		} catch (CoreException e) {
			builder.append("Error while calling container.members():" + e.getMessage());
		}
		builder.append("\n}");
		return builder.toString();
	}

	public String apply(IResource input) {
		return format(input);
	}

	public String formatWorkspace() {
		return format(ResourcesPlugin.getWorkspace().getRoot());
	}

}
