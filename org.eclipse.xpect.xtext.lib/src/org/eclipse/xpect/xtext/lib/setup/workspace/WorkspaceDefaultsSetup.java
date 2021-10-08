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

package org.eclipse.xpect.xtext.lib.setup.workspace;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xpect.Environment;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.XpectRequiredEnvironment;
import org.eclipse.xpect.setup.ISetupInitializer;
import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.setup.XpectSetupRoot;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.xtext.lib.setup.InjectorSetup;
import org.eclipse.xpect.xtext.lib.util.GrammarAnalyzer;

import com.google.common.collect.Lists;
import com.google.inject.Injector;

@XpectSetupRoot
@XpectSetupFactory
@XpectRequiredEnvironment(Environment.PLUGIN_TEST)
@XpectImport({ InjectorSetup.class, File.class, Folder.class, JavaProject.class, Project.class, SrcFolder.class, ThisFile.class })
public class WorkspaceDefaultsSetup {

	static final private Logger LOG = Logger.getLogger(WorkspaceDefaultsSetup.class);

	public static final Path XTEND_LIBRARY_PATH = new Path("org.eclipse.xtend.XTEND_CONTAINER");

	private List<org.eclipse.xpect.xtext.lib.setup.generic.Resource> genericResources = Lists.newArrayList();

	private Workspace workspace;

	public WorkspaceDefaultsSetup(ISetupInitializer<WorkspaceDefaultsSetup> initializer, Injector injector) {
		super();
		initialize(initializer, injector);
	}

	public void add(org.eclipse.xpect.xtext.lib.setup.generic.Resource file) {
		this.genericResources.add(file);
	}

	public void add(Workspace workspace) {
		if (this.workspace != null)
			throw new IllegalStateException("Only one workspace per setup is supported.");
		this.workspace = workspace;
	}

	protected IResourceFactory<? extends IResource, IContainer> convert(org.eclipse.xpect.xtext.lib.setup.generic.Resource res) {
		if (res instanceof org.eclipse.xpect.xtext.lib.setup.generic.ThisFile)
			return new org.eclipse.xpect.xtext.lib.setup.workspace.ThisFile((org.eclipse.xpect.xtext.lib.setup.generic.ThisFile) res);
		if (res instanceof org.eclipse.xpect.xtext.lib.setup.generic.File)
			return new File((org.eclipse.xpect.xtext.lib.setup.generic.File) res);
		if (res instanceof org.eclipse.xpect.xtext.lib.setup.generic.Folder)
			return new Folder((org.eclipse.xpect.xtext.lib.setup.generic.Folder) res);
		throw new IllegalStateException();
	}

	protected Workspace createWorkspace() {
		return new Workspace();
	}

	public List<org.eclipse.xpect.xtext.lib.setup.generic.Resource> getGenericResources() {
		return genericResources;
	}

	@Creates
	public Workspace getWorkspace() {
		return this.workspace;
	}

	protected void initialize(ISetupInitializer<WorkspaceDefaultsSetup> initializer, Injector injector) {
		initializer.initialize(this);
		if (workspace == null)
			add(createWorkspace());
		initializeProject(injector);
	}

	protected void initializeDefaultProject() {
		if (workspace.getDefaultProject() == null)
			workspace.add(new Project("default_project"));
		Project defaultProject = workspace.getDefaultProject();
		for (org.eclipse.xpect.xtext.lib.setup.generic.Resource res : getGenericResources())
			defaultProject.add(convert(res));
		if (workspace.getThisFile() == null)
			defaultProject.add(new org.eclipse.xpect.xtext.lib.setup.workspace.ThisFile());
	}

	protected void initializeJavaProject() {
		if (workspace.getMember(JavaProject.class) == null)
			workspace.add(new JavaProject("default_java_project"));
		JavaProject javaProject = workspace.getMember(JavaProject.class);
		initializeJavaProject(javaProject);
	}

	protected void initializeJavaProject(JavaProject javaProject) {
		if (javaProject.getMember(SrcFolder.class) == null)
			javaProject.add(new SrcFolder("src"));
		SrcFolder srcFolder = javaProject.getMember(SrcFolder.class);
		for (org.eclipse.xpect.xtext.lib.setup.generic.Resource res : getGenericResources())
			srcFolder.add(convert(res));
		if (workspace.getThisFile() == null)
			srcFolder.add(new org.eclipse.xpect.xtext.lib.setup.workspace.ThisFile());
	}

	protected void initializeProject(Injector injector) {
		GrammarAnalyzer ga = new GrammarAnalyzer(injector.getInstance(IGrammarAccess.class).getGrammar());
		switch (ga.getLanguageKind()) {
		case XBASE:
			initializeXbaseProject();
			break;
		case JAVA:
			initializeJavaProject();
			break;
		default:
			initializeDefaultProject();
			break;
		}
	}

	protected void initializeXbaseProject() {
		LOG.warn("Can't add xbase dependencies to test project; org.eclipse.xpect.xtext.xbase.lib.XbaseWorkspaceDefaultsSetup not loaded.");
		initializeJavaProject();
	}

}
