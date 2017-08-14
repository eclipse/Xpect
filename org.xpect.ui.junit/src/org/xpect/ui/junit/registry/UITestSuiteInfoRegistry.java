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

package org.xpect.ui.junit.registry;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.xpect.registry.ITestSuiteInfo;
import org.xpect.registry.TestSuiteInfoRegistry;

public class UITestSuiteInfoRegistry implements ITestSuiteInfo.Registry {

	private ITestSuiteInfo.Registry runtimeRegistry = new TestSuiteInfoRegistry();
	private ITestSuiteInfo.Registry workspaceRegistry = new WorkspaceTestSuiteInfoRegistry();

	public ITestSuiteInfo getTestSuite(Resource resource) {
		XtextResourceSet rs = (XtextResourceSet) resource.getResourceSet();
		Object context = rs.getClasspathURIContext();
		if (context instanceof IJavaProject)
			return workspaceRegistry.getTestSuite(resource);
		else
			return runtimeRegistry.getTestSuite(resource);
	}
}
