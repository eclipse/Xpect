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
import org.eclipse.xtext.util.StringInputStream;
import org.eclipse.xpect.xtext.lib.setup.FileSetupContext;

public class ThisFile extends File {

	public ThisFile() {
		super();
	}

	public ThisFile(String name) {
		super(name);
	}

	public InputStream getContents(FileSetupContext ctx) throws IOException {
		if (getFrom() != null)
			return super.getContents(ctx);
		return new StringInputStream(ctx.getXpectFile().getDocument());
	}

	@Override
	public URI getLocalURI(FileSetupContext ctx) {
		URI localURI = super.getLocalURI(ctx);
		if (localURI != null)
			return localURI;
		return URI.createURI(ctx.getXpectFileURI().lastSegment());
	}

}
