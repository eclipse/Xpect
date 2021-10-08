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

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xpect.setup.XpectSetupComponent;
import org.eclipse.xpect.xtext.lib.setup.FileSetupContext;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectSetupComponent
public class Resource implements ResourceFactory {

	private final org.eclipse.xpect.xtext.lib.setup.generic.File delegate;

	public Resource() {
		this.delegate = new org.eclipse.xpect.xtext.lib.setup.generic.File();
	}

	public Resource(org.eclipse.xpect.xtext.lib.setup.generic.File file) {
		this.delegate = file;
	}

	public Resource(String name) {
		delegate = new org.eclipse.xpect.xtext.lib.setup.generic.File(name);
	}

	public org.eclipse.emf.ecore.resource.Resource create(FileSetupContext ctx, ResourceSet resourceSet) throws IOException {
		URI resourceURI = ctx.resolve(delegate.getLocalURI(ctx).toString());
		return ctx.load(resourceSet, resourceURI, delegate.getContents(ctx));
	}

	public void setFrom(String from) {
		delegate.setFrom(from);
	}
}
