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
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.xpect.Environment;
import org.xpect.XpectImport;
import org.xpect.XpectRequiredEnvironment;
import org.xpect.setup.XpectSetupComponent;
import org.xpect.util.EnvironmentUtil;
import org.xpect.xtext.lib.setup.FileSetupContext;

@XpectSetupComponent
@XpectRequiredEnvironment(Environment.PLUGIN_TEST)
@XpectImport(WorkspaceDefaultsSetup.class)
public class Workspace extends Container<IWorkspaceRoot> {

	public static class Instance {
		private IFile thisFile;
		private IProject thisProject;
		private IWorkspace workspace;

		public IFile getThisFile() {
			return thisFile;
		}

		public IProject getThisProject() {
			return thisProject;
		}

		public IWorkspace getWorkspace() {
			return workspace;
		}

		public void setThisFile(IFile thisFile) {
			this.thisFile = thisFile;
		}

		public void setThisProject(IProject thisProject) {
			this.thisProject = thisProject;
		}

		public void setWorkspace(IWorkspace workspace) {
			this.workspace = workspace;
		}
	}

	private boolean autobuild = false;

	public Workspace() {
	}

	public void buildIncrementally() {
		try {
			ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, new NullProgressMonitor());
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	public void cleanWorkspace() {
		EnvironmentUtil.requireEnvironment(Environment.PLUGIN_TEST);
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (int i = projects.length - 1; i >= 0; i--)
			try {
				projects[i].delete(true, new NullProgressMonitor());
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
	}

	public Workspace.Instance configureWorkspace(final FileSetupContext ctx) {
		EnvironmentUtil.requireEnvironment(Environment.PLUGIN_TEST);
		final Instance instance = new Instance();
		try {
			IWorkspaceDescription description = ResourcesPlugin.getWorkspace().getDescription();
			if (description.isAutoBuilding() != isAutobuild()) {
				description.setAutoBuilding(isAutobuild());
				ResourcesPlugin.getWorkspace().setDescription(description);
			}
			new WorkspaceModifyOperation() {
				@Override
				protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
					try {
						configure(ctx, root);
						createMembers(ctx, root, instance);
					} catch (IOException e) {
						throw new CoreException(new Status(IStatus.ERROR, "org.xpect.xtext.lib", "Error initializing test workspace", e));
					}
				}
			}.run(new NullProgressMonitor());
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	protected <T> T findRecursive(Container<?> container, Class<T> type) {
		for (IResourceFactory<?, ?> child : container.getMemberFactories()) {
			if (type.isInstance(child))
				return (T) child;
			if (child instanceof Container) {
				T result = findRecursive((Container<?>) child, type);
				if (result != null)
					return result;
			}
		}
		return null;
	}

	public Project getDefaultProject() {
		for (IResourceFactory<?, ?> fact : getMemberFactories())
			if (fact instanceof Project)
				return (Project) fact;
		return null;
	}

	public org.xpect.xtext.lib.setup.workspace.ThisFile getThisFile() {
		return findRecursive(this, org.xpect.xtext.lib.setup.workspace.ThisFile.class);
	}

	public boolean isAutobuild() {
		return autobuild;
	}

	public void setAutobuild(boolean autobuild) {
		this.autobuild = autobuild;
	}

	public void waitForAutoBuild() {
		boolean interrupted = false;
		do {
			try {
				Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);
				interrupted = false;
			} catch (OperationCanceledException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				interrupted = true;
			}
		} while (interrupted);
	}
}
