package org.xpect.ui.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;
import org.xpect.XpectJavaModel;
import org.xpect.registry.ILanguageInfo;
import org.xpect.ui.XpectPluginActivator;
import org.xpect.ui.util.XpectUtil;
import org.xpect.util.IXtInjectorProvider;
import org.xpect.util.ReflectionUtil;
import org.xpect.util.URIDelegationHandler;

import com.google.inject.Injector;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XtEditorExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return XpectPluginActivator.getInstance().getBundle();
	}

	@Override
	protected Injector getInjector() {
		IFile file = getFileOfCurrentlyOpeningEditor();
		if (file == null)
			throw new RuntimeException("Could not determine which editor is currently being opened.");
		String ext = new URIDelegationHandler().getOriginalFileExtension(file.getName());
		ILanguageInfo info = ILanguageInfo.Registry.INSTANCE.getLanguageByFileExtension(ext);
		if (info == null)
			throw new RuntimeException("There is no Xtext-language installed for file extension '" + ext + "'.");
		XpectJavaModel javaModel = XpectUtil.loadJavaModel(file);
		URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		if (uri == null)
			throw new RuntimeException("Could not determine URI for IFile " + file.getFullPath().toString());
		return IXtInjectorProvider.INSTANCE.getInjector(javaModel, uri);
	}

	protected IWorkbenchPage getActiveWorkbenchPage() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		if (page != null)
			return page;
		Object perspectiveSwitcher = ReflectionUtil.readField(window, "perspectiveSwitcher", Object.class);
		if (perspectiveSwitcher == null)
			return null;
		Object reorderListener = ReflectionUtil.readField(perspectiveSwitcher, "reorderListener", Object.class);
		if (reorderListener == null)
			return null;
		Object parent = ReflectionUtil.getParentObject(reorderListener);
		if (parent instanceof IWorkbenchPage)
			return (IWorkbenchPage) parent;
		return null;
	}

	protected IFile getFileOfCurrentlyOpeningEditor() {
		try {
			IWorkbenchPage page = getActiveWorkbenchPage();
			if (page != null) {
				IEditorReference[] references = page.getEditorReferences();

				// iterate backwards, because the new editor is likely to be at
				// the end of the list
				for (int i = references.length - 1; i >= 0; i--)

					// only care about Xt-Editors that are *not* yet initialized
					if (references[i].getId().equals(XpectPluginActivator.XT_EDITOR_ID) && references[i].getPart(false) == null) {
						IEditorInput input = references[i].getEditorInput();
						if (input instanceof IFileEditorInput)
							return ((IFileEditorInput) input).getFile();
					}
			}
			return null;
		} catch (PartInitException e) {
			throw new RuntimeException();
		}
	}
}
