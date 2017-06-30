package org.xpect.runner;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.util.XtextVersion;
import org.junit.runner.Description;
import org.xpect.XpectFile;
import org.xpect.XpectInvocation;

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
		if (!Strings.isNullOrEmpty(title))
			return Description.createTestDescription(javaClass, uri.toString() + ": " + title);
		else
			return Description.createTestDescription(javaClass, uri.toString());
	}

	public static Description createTestDescription(XpectInvocation invocation) {
		URI uri = EcoreUtil.getURI(invocation);
		Preconditions.checkArgument(uri.isPlatform());
		String className = invocation.getFile().getJavaModel().getTestOrSuite().getJvmClass().getQualifiedName();
		String text = Joiner.on('/').join(uri.segmentsList().subList(2, uri.segmentCount())) + "#" + uri.fragment();
		String title = getTitle(invocation);
		if (!Strings.isNullOrEmpty(title))
			return Description.createTestDescription(className, text + ": " + title);
		else
			return Description.createTestDescription(className, text);
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
