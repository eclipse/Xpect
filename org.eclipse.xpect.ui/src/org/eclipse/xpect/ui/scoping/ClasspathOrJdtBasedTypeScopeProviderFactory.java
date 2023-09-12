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

package org.eclipse.xpect.ui.scoping;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.internal.core.DefaultWorkingCopyOwner;
import org.eclipse.xtext.common.types.access.AbstractTypeProviderFactory;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.common.types.access.impl.ClasspathTypeProvider;
import org.eclipse.xtext.common.types.access.jdt.IJavaProjectProvider;
import org.eclipse.xtext.common.types.access.jdt.IJdtTypeProvider;
import org.eclipse.xtext.common.types.access.jdt.IWorkingCopyOwnerProvider;
import org.eclipse.xtext.common.types.access.jdt.JdtTypeProvider;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class ClasspathOrJdtBasedTypeScopeProviderFactory extends AbstractTypeProviderFactory {

	@Inject
	private ClassLoader classLoader;

	@Inject
	private IJavaProjectProvider javaProjectProvider;

	@Inject
	private IWorkingCopyOwnerProvider wcProvider;

	protected ClasspathTypeProvider createClasspathTypeProvider(ResourceSet resourceSet) {
		return new ClasspathTypeProvider(getClassLoader(resourceSet), resourceSet, null /* getIndexedJvmTypeAccess() */, null);
	}

	protected IJdtTypeProvider createJdtTypeProvider(IJavaProject javaProject, ResourceSet resourceSet) {
		if (javaProject == null)
			throw new IllegalStateException();
		WorkingCopyOwner owner;
		if (wcProvider == null)
			owner = DefaultWorkingCopyOwner.PRIMARY;
		else
			owner = wcProvider.getWorkingCopyOwner(javaProject, resourceSet);
		return new JdtTypeProvider(javaProject, resourceSet, getIndexedJvmTypeAccess(), owner, null);
	}

	public IJvmTypeProvider createTypeProvider(ResourceSet resourceSet) {
		if (resourceSet == null)
			throw new IllegalArgumentException("resourceSet may not be null.");
		IJavaProject javaProject = javaProjectProvider.getJavaProject(resourceSet);
		if (javaProject != null)
			return createJdtTypeProvider(javaProject, resourceSet);
		return createClasspathTypeProvider(resourceSet);
	}

	public ClassLoader getClassLoader(ResourceSet resourceSet) {
		if (resourceSet instanceof XtextResourceSet) {
			XtextResourceSet xtextResourceSet = (XtextResourceSet) resourceSet;
			Object ctx = xtextResourceSet.getClasspathURIContext();
			if (ctx != null) {
				if (ctx instanceof Class<?>) {
					return ((Class<?>) ctx).getClassLoader();
				}
				if (!(ctx instanceof ClassLoader)) {
					return ctx.getClass().getClassLoader();
				}
				return (ClassLoader) ctx;
			}
		}
		return classLoader;
	}

}
