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

@XpectSetupComponent
public class ThisFile extends File implements IResourceFactory<IFile, IContainer> {

	public ThisFile() {
		super(new org.xpect.xtext.lib.setup.generic.ThisFile());
	}

	public ThisFile(org.xpect.xtext.lib.setup.generic.ThisFile file) {
		super(file);
	}

	public ThisFile(String name) {
		super(name);
	}

	public IFile create(FileSetupContext ctx, IContainer container, Workspace.Instance instance) throws IOException {
		IFile file = super.create(ctx, container, instance);
		instance.setThisFile(file);
		instance.setThisProject(file.getProject());
		return file;
	}

}
