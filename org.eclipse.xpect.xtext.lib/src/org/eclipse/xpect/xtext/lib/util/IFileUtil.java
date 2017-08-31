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

package org.eclipse.xpect.xtext.lib.util;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;

import com.google.common.io.Closeables;

public class IFileUtil {

	public static IFile create(IContainer container, URI localURI, InputStream inputStream) {
		return createFile(findFolder(container, localURI), localURI.lastSegment(), inputStream);
	}

	public static IFile createFile(IContainer container, String name, InputStream inputStream) {
		IFile file = container.getFile(new Path(name));
		try {
			file.create(inputStream, true, new NullProgressMonitor());
		} catch (CoreException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				Closeables.close(inputStream, true);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return file;
	}

	public static IFolder createFolder(IContainer container, String name) {
		IFolder file = container.getFolder(new Path(name));
		try {
			file.create(true, true, new NullProgressMonitor());
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
		return file;
	}

	public static IContainer findFolder(IContainer container, URI localURI) {
		if (localURI.segmentCount() > 1) {
			Path path = new Path(localURI.trimSegments(1).toString());
			IFolder folder = container.getFolder(path);
			if (!folder.exists())
				try {
					folder.create(true, true, new NullProgressMonitor());
					return folder;
				} catch (CoreException e) {
					throw new RuntimeException();
				}
		}
		return container;
	}
}
