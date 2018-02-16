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

package org.eclipse.xpect.runner;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xpect.XpectFile;
import org.eclipse.xpect.XpectInvocation;
import org.eclipse.xtext.util.XtextVersion;
import org.junit.runner.Description;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class DescriptionFactory {

	public static Description createFileDescription(Class<?> clazz, IXpectURIProvider uriProvider, URI uri) {
		URI deresolved = uriProvider.deresolveToProject(uri);
		Description result = createFileDescription(deresolved.trimSegments(1).toString(), deresolved.lastSegment());
		return result;
	}

	public static Description createFileDescription(String pathInProject, String fileName) {
		String name = fileName + ": " + pathInProject;
		Description result = Description.createSuiteDescription(name);
		return result;
	}

	public static Description createFileDescription(XpectFile file) {
		URI uri = file.eResource().getURI();
		Preconditions.checkArgument(uri.isPlatform());
		String pathInProject = Joiner.on('/').join(uri.segmentsList().subList(2, uri.segmentCount() - 1));
		return createFileDescription(pathInProject, uri.lastSegment());
	}

	public static Description createFileDescriptionForError(Class<?> clazz, IXpectURIProvider uriProvider, URI uri) {
		URI deresolved = uriProvider.deresolveToProject(uri);
		String name = deresolved.lastSegment() + ": " + deresolved.trimSegments(1).toString();
		Description result = Description.createTestDescription(clazz, name);
		return result;
	}
	
	public static Description createTestDescription(Class<?> javaClass, IXpectURIProvider uriProvider, XpectInvocation invocation) {
		URI uri = uriProvider.deresolveToProject(EcoreUtil.getURI(invocation));
		String title = getTitle(invocation);
		List<String> ret = extractXpectMethodNameAndResourceURI(uri.toString());
		String fragmentToXpectMethod = ret.get(0);
		String pathToResource = ret.get(1);
		
		String text = fragmentToXpectMethod;
		
		if (!Strings.isNullOrEmpty(title))
			text = text + ": " + title;
		// The test name has the following format
		// 		errors~0: This is a comment 〔path/to/file.xt〕
		return Description.createTestDescription(javaClass, text + " \u3014" + pathToResource  + "\u3015");
	}

	public static Description createTestDescription(XpectInvocation invocation) {
		URI uri = EcoreUtil.getURI(invocation);
		Preconditions.checkArgument(uri.isPlatform());
		String className = invocation.getFile().getJavaModel().getTestOrSuite().getJvmClass().getQualifiedName();
		String pathToResource = Joiner.on('/').join(uri.segmentsList().subList(2, uri.segmentCount()));
		String fragmentToXpectMethod = uri.fragment();
		String text = fragmentToXpectMethod;
		String title = getTitle(invocation);
		if (!Strings.isNullOrEmpty(title))
			text = text + ": " + title;
		// The test name has the following format
		// 		errors~0: This is a comment 〔path/to/file.xt〕
		return Description.createTestDescription(className, text + " \u3014" + pathToResource  + "\u3015");
	}
	
	public static List<String> extractXpectMethodNameAndResourceURI(String text) {
		int sharpPos = text.indexOf('#');
		List<String> result = new ArrayList<String>();
		result.add(text.substring(sharpPos + 1));
		result.add(text.substring(0, sharpPos));
		return result;
	}


	public static String getTitle(XpectInvocation inv) {
		String postfix = System.getProperty("xpectTestTitlePostfix");
		String env = System.getProperty("xpectTestTitleShowEnvironment");
		TestTitleRegion titleRegion = inv.getRelatedRegion(TestTitleRegion.class);
		StringBuilder result = new StringBuilder();
		if (titleRegion != null) {
			String title = titleRegion.getTitle();
			if (!Strings.isNullOrEmpty(title)) {
				result.append(title);
			}
		}
		if (!Strings.isNullOrEmpty(postfix)) {
			result.append(postfix);
		}
		if (!Strings.isNullOrEmpty(env)) {
			result.append(EcorePlugin.IS_ECLIPSE_RUNNING ? ".plugin" : ".standalone");
			result.append(".xtext_" + XtextVersion.getCurrent().getVersion());
		}
		return result.toString();
	}
}
