package org.xpect.xtext.xbase.lib;

import org.eclipse.xtext.xbase.lib.Procedures;
import org.xpect.Environment;
import org.xpect.XpectReplace;
import org.xpect.XpectRequiredEnvironment;
import org.xpect.setup.ISetupInitializer;
import org.xpect.setup.XpectSetupFactory;
import org.xpect.setup.XpectSetupRoot;
import org.xpect.xtext.lib.setup.workspace.JavaProject;
import org.xpect.xtext.lib.setup.workspace.WorkspaceDefaultsSetup;

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
