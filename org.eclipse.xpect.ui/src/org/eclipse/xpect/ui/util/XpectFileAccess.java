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

package org.eclipse.xpect.ui.util;

import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.ClassloaderClasspathUriResolver;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceFactory;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.StringInputStream;
import org.osgi.framework.Bundle;
import org.eclipse.xpect.XpectConstants;
import org.eclipse.xpect.XpectFile;
import org.eclipse.xpect.registry.ILanguageInfo;
import org.eclipse.xpect.runner.XpectRunner;

import com.google.inject.Injector;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectFileAccess {

	private static class XpectResourceAdapter extends AdapterImpl {
		private final XtextResource resource;

		public XpectResourceAdapter(XtextResource resource) {
			super();
			this.resource = resource;
		}
	}

	public static ClassLoader getXpectLibClassLoader() {
		Bundle lib = Platform.getBundle("org.eclipse.xpect.xtext.lib");
		if (lib != null) {
			try {
				Class<?> cls = lib.loadClass("org.eclipse.xpect.xtext.lib.tests.XtextTests");
				ClassLoader loader = cls.getClassLoader();
				return loader;
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	protected static ResourceSet cloneResourceSet(ResourceSet rs) {
		XtextResourceSet result = new XtextResourceSet();
		result.setPackageRegistry(rs.getPackageRegistry());
		// need delegation or nothing because of "java" protocol
		// result.setResourceFactoryRegistry(rs.getResourceFactoryRegistry());
		result.setURIConverter(rs.getURIConverter());
		if (XpectRunner.testClassloader != null) {
			result.setClasspathURIContext(XpectRunner.testClassloader);
			result.setClasspathUriResolver(new ClassloaderClasspathUriResolver());
		} else if (rs instanceof XtextResourceSet) {
			XtextResourceSet xrs = (XtextResourceSet) rs;
			Object context = xrs.getClasspathURIContext();
			if (context != null) {
				result.setClasspathURIContext(context);
				result.setClasspathUriResolver(xrs.getClasspathUriResolver());
			} else {
				result.setClasspathURIContext(getXpectLibClassLoader());
				result.setClasspathUriResolver(new ClassloaderClasspathUriResolver());
			}
		}
		return result;
	}

	private static XpectFile findXpectFile(Resource xpectResource) {
		for (EObject contents : xpectResource.getContents())
			if (contents instanceof XpectFile)
				return (XpectFile) contents;
		return null;
	}

	public static XpectFile getXpectFile(Resource resource) {
		for (EObject contents : resource.getContents())
			if (contents instanceof XpectFile)
				return (XpectFile) contents;
		return findXpectFile(getXpectResource(resource));
	}

	public static XtextResource getXpectResource(Resource resource) {
		for (EObject contents : resource.getContents())
			if (contents instanceof XpectFile)
				return (XtextResource) resource;
		String document = ((XtextResource) resource).getParseResult().getRootNode().getText();
		for (Adapter a : resource.eAdapters())
			if (a instanceof XpectResourceAdapter) {
				XtextResource xpectResource = ((XpectResourceAdapter) a).resource;
				if (!xpectResource.getParseResult().getRootNode().getText().equals(document))
					load(xpectResource, document);
				return xpectResource;
			}
		ResourceSet rs = cloneResourceSet(resource.getResourceSet());
		Injector injector = ILanguageInfo.Registry.INSTANCE.getLanguageByFileExtension(XpectConstants.XPECT_FILE_EXT).getInjector();
		XtextResource xpectResource = (XtextResource) injector.getInstance(XtextResourceFactory.class).createResource(resource.getURI());
		rs.getResources().add(xpectResource);
		load(xpectResource, document);
		resource.eAdapters().add(new XpectResourceAdapter(xpectResource));
		return xpectResource;

	}

	private static void load(Resource xpectResource, String document) {
		try {
			xpectResource.unload();
			xpectResource.load(new StringInputStream(document), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
