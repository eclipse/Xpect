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
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.xpect.setup.XpectSetupComponent;
import org.xpect.xtext.lib.setup.FileSetupContext;
import org.xpect.xtext.lib.setup.workspace.Workspace.Instance;
import org.xpect.xtext.lib.util.IFileUtil;

@XpectSetupComponent
public class Folder extends Container<IFolder> implements IResourceFactory<IFolder, IContainer> {

	private final org.xpect.xtext.lib.setup.generic.Folder delegate;

	public Folder(String name) {
		this.delegate = new org.xpect.xtext.lib.setup.generic.Folder(name);
	}

	public Folder(org.xpect.xtext.lib.setup.generic.Folder delegate) {
		super();
		this.delegate = delegate;
	}

	public IFolder create(FileSetupContext ctx, IContainer container, Instance instance) throws IOException, CoreException {
		IFolder folder = IFileUtil.createFolder(container, delegate.getLocalURI(ctx).toString());
		configure(ctx, folder);
		createMembers(ctx, folder, instance);
		return folder;
	}

}
