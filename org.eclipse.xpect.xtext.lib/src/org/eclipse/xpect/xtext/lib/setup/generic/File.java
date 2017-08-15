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

package org.eclipse.xpect.xtext.lib.setup.generic;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xpect.xtext.lib.setup.FileSetupContext;
import org.eclipse.xpect.xtext.lib.util.URIUtil;

public class File implements Resource {
	private URI from = null;
	private final URI local;

	public File() {
		this.local = null;
	}

	public File(String name) {
		this.local = URIUtil.createLocalURI(name);
	}

	public InputStream getContents(FileSetupContext ctx) throws IOException {
		URI source = ctx.resolve(getFrom() == null ? getLocalURI(ctx).lastSegment() : getFrom().toString());
		return ctx.getXpectFile().eResource().getResourceSet().getURIConverter().createInputStream(source);
	}

	public URI getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = URI.createURI(from);
	}

	public URI getLocalURI(FileSetupContext ctx) {
		if (local != null)
			return local;
		if (this.from != null)
			return URI.createURI(from.lastSegment());
		return null;
	}

}
