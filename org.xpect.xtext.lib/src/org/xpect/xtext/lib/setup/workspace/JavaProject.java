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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.xpect.setup.XpectSetupComponent;
import org.xpect.xtext.lib.setup.FileSetupContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@XpectSetupComponent
public class JavaProject extends Project {

	private static final String JRE_CONTAINER_1_5 = "org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/J2SE-1.5";

	private List<IClasspathEntry> classpathEntries = Lists.newArrayList();

	public JavaProject() {
		this("java_project");
	}

	public JavaProject(String name) {
		super(name);
		addNature(JavaCore.NATURE_ID);
		addBuilder(JavaCore.BUILDER_ID);
		this.classpathEntries.add(JavaCore.newContainerEntry(new Path(JRE_CONTAINER_1_5)));
	}

	public void addClasspathEntry(IClasspathEntry path) {
		classpathEntries.add(path);
	}

	// see /org/eclipse/xtext/xbase/compiler/OnTheFlyJavaCompiler.java
	public void addClassPathOfClass(Class<?> clazz) {
		final String classNameAsPath = "/" + clazz.getName().replace('.', '/');
		String resourceName = classNameAsPath + ".class";
		URL url = clazz.getResource(resourceName);
		if (url == null)
			throw new IllegalArgumentException(resourceName + " not found");
		String pathToFolderOrJar = null;
		if (url.getProtocol().startsWith("bundleresource")) {
			try {
				url = FileLocator.resolve(url);
			} catch (IOException e) {
				throw new WrappedException(e);
			}
		}
		if (url.getProtocol().startsWith("jar")) {
			try {
				final String path = url.getPath().substring(0, url.getPath().indexOf('!'));
				String encodedPath = path.replace(" ", "%20");
				pathToFolderOrJar = new URL(encodedPath).toURI().getRawPath();
			} catch (Exception e) {
				throw new WrappedException(e);
			}
		} else {
			String resolvedRawPath;
			try {
				if (url.toExternalForm().contains(" "))
					resolvedRawPath = URIUtil.toURI(url).getRawPath();
				else
					resolvedRawPath = url.toURI().getRawPath();
			} catch (URISyntaxException e) {
				throw new WrappedException(e);
			}
			pathToFolderOrJar = resolvedRawPath.substring(0, resolvedRawPath.indexOf(classNameAsPath));
		}
		this.classpathEntries.add(JavaCore.newLibraryEntry(new Path(pathToFolderOrJar), null, null));
	}

	public void addContainer(String name) {
		this.classpathEntries.add(JavaCore.newContainerEntry(new Path(name)));
	}

	@Override
	public IProject create(FileSetupContext ctx, IWorkspaceRoot container, Workspace.Instance instance) throws CoreException, IOException {
		IProject project = super.create(ctx, container, instance);
		IJavaProject java = JavaCore.create(project);
		LinkedHashSet<IClasspathEntry> classPath = Sets.newLinkedHashSet(Lists.newArrayList(java.getRawClasspath()));
		classPath.remove(JavaCore.newSourceEntry(project.getFullPath()));
		classPath.addAll(classpathEntries);
		for (SrcFolder srcFolder : getMembers(SrcFolder.class))
			classPath.add(JavaCore.newSourceEntry(project.getFolder(srcFolder.getName()).getFullPath()));
		java.setRawClasspath(Lists.newArrayList(classPath).toArray(new IClasspathEntry[classPath.size()]), new NullProgressMonitor());
		java.save(null, true);
		return project;
	}

}
