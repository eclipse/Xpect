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

package org.eclipse.xpect.ui.junit.registry;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xpect.registry.ITestSuiteInfo;
import org.eclipse.xpect.registry.TestSuiteInfoRegistry;

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
