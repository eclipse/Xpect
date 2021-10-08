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

package org.eclipse.xpect.xtext.lib.setup.generic;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xpect.xtext.lib.setup.FileSetupContext;
import org.eclipse.xpect.xtext.lib.util.URIUtil;

import com.google.common.collect.Lists;

public class Folder implements Resource {

	private List<Resource> children = Lists.newArrayList();

	private final URI local;

	public Folder(String name) {
		this.local = URIUtil.createLocalURI(name);
	}

	public void add(Resource child) {
		children.add(child);
	}

	public List<Resource> getChildren() {
		return children;
	}

	public URI getLocalURI(FileSetupContext ctx) {
		return local;
	}
}
