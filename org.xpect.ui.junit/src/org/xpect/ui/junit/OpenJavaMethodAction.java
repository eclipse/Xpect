package org.xpect.ui.junit;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.junit.runners.TypeUtil;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PartInitException;
import org.xpect.ui.junit.TestDataUIUtil.TestElementInfo;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 * 
 * @see org.eclipse.ui.actions.OpenFileAction
 */
public class OpenJavaMethodAction extends Action {

	private TestElementInfo element;

	public OpenJavaMethodAction(TestElementInfo element, String text, String toolTipText) {
		super();
		this.element = element;
		setText(text);
		setToolTipText(toolTipText);
	}

	public IMethod findMethod(IType type, String name) throws JavaModelException {
		for (IMethod m : type.getMethods())
			if (m.getElementName().equals(name))
				return m;
		String superType = type.getSuperclassName();
		if (superType != null)
			for (String[] resolved : type.resolveType(superType)) {
				IType type2 = TypeUtil.findType(type.getJavaProject(), resolved[0] + "." + resolved[1]);
				if (type2 != null)
					return findMethod(type2, name);
			}
		return null;
	}

	@Override
	public void run() {
		try {
			IType type = TypeUtil.findType(element.getJavaProject(), element.getClazz());
			if (type != null) {
				IMethod iMethod;
				iMethod = findMethod(type, element.getMethod());
				if (iMethod == null)
					MessageDialog.openError(null, "Java Element not found", "Java Element not found");
				else
					JavaUI.openInEditor(iMethod);
			} else
				MessageDialog.openError(null, "Java Element not found", "Java Element not found");
		} catch (JavaModelException e) {
		} catch (PartInitException e) {
		}
	}

}
