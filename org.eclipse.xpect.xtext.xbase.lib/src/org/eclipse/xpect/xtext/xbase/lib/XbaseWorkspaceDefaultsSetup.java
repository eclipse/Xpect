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

package org.eclipse.xpect.xtext.xbase.lib;

import org.eclipse.xtext.xbase.lib.Procedures;
import org.eclipse.xpect.Environment;
import org.eclipse.xpect.XpectReplace;
import org.eclipse.xpect.XpectRequiredEnvironment;
import org.eclipse.xpect.setup.ISetupInitializer;
import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.setup.XpectSetupRoot;
import org.eclipse.xpect.xtext.lib.setup.workspace.JavaProject;
import org.eclipse.xpect.xtext.lib.setup.workspace.WorkspaceDefaultsSetup;

import com.google.common.base.Predicates;
import com.google.inject.Injector;

@XpectSetupRoot
@XpectSetupFactory
@XpectRequiredEnvironment(Environment.PLUGIN_TEST)
@XpectReplace(WorkspaceDefaultsSetup.class)
public class XbaseWorkspaceDefaultsSetup extends WorkspaceDefaultsSetup {

	public XbaseWorkspaceDefaultsSetup(ISetupInitializer<WorkspaceDefaultsSetup> initializer, Injector injector) {
		super(initializer, injector);
	}

	@Override
	protected void initializeXbaseProject() {
		initializeJavaProject();
		JavaProject javaProject = getWorkspace().getMember(JavaProject.class);
		javaProject.addClassPathOfClass(Procedures.class); // xbase.lib
		javaProject.addClassPathOfClass(Predicates.class); // Google Guava
	}

}
