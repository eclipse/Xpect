package org.xpect.ui.junit.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class JUnitLaunchShortcut extends org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut {

	private JUnitJavaElementDelegate delegate;

	@Override
	protected ILaunchConfigurationWorkingCopy createLaunchConfiguration(IJavaElement element) throws CoreException {
		ILaunchConfigurationWorkingCopy wc;
		if (delegate != null && element.getElementType() == IJavaElement.TYPE)
			wc = LaunchShortcutUtil.createXpectLaunchConfiguration(delegate, getLaunchConfigurationTypeId());
		else
			wc = super.createLaunchConfiguration(element);
		return wc;
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		try {
			delegate = new JunitJavaElementDelegateAdapterFactory().create(editor);
			if (delegate != null)
				super.launch(new StructuredSelection(delegate), mode);
			else
				super.launch(editor, mode);
		} finally {
			delegate = null;
		}
	}

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			try {
				IStructuredSelection newSelection = LaunchShortcutUtil.replaceWithJavaElementDelegates((IStructuredSelection) selection);
				Object element = newSelection.getFirstElement();
				if (element instanceof JUnitJavaElementDelegate)
					delegate = (JUnitJavaElementDelegate) element;
				super.launch(newSelection, mode);
			} finally {
				delegate = null;
			}
		}
	}
}
