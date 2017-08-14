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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.xpect.setup.XpectSetupComponent;
import org.xpect.xtext.lib.setup.FileSetupContext;
import org.xpect.xtext.lib.util.IFileUtil;

@XpectSetupComponent
public class File implements IResourceFactory<IFile, IContainer> {

	private final org.xpect.xtext.lib.setup.generic.File delegate;

	public File(org.xpect.xtext.lib.setup.generic.File file) {
		delegate = file;
	}

	public File() {
		delegate = new org.xpect.xtext.lib.setup.generic.File();
	}

	public File(String name) {
		delegate = new org.xpect.xtext.lib.setup.generic.File(name);
	}

	public IFile create(FileSetupContext ctx, IContainer container, Workspace.Instance instance) throws IOException {
		return IFileUtil.create(container, delegate.getLocalURI(ctx), delegate.getContents(ctx));
	}

	public void setFrom(String name) {
		delegate.setFrom(name);
	}

}
