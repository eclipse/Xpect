package org.eclipse.xpect.ui.junit;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.internal.junit.model.TestCaseElement;
import org.eclipse.jdt.internal.junit.model.TestElement;
import org.eclipse.jdt.internal.junit.model.TestSuiteElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.xpect.ui.XpectPluginActivator;
import org.eclipse.xpect.ui.junit.TestDataUIUtil.TestElementInfo;
import org.eclipse.xtext.ui.editor.IURIEditorOpener;

import com.google.inject.Inject;

/**
 * Go to Xpect method action.
 */
@SuppressWarnings("restriction")
public class GotoXpectMethodAction implements IViewActionDelegate, ISelectionChangedListener {

	public static final String ID = "org.eclipse.xpect.ui.junit.gotoXpectMethod"; //$NON-NLS-1$

	private TestCaseElement testCaseElement;

	@Inject
	private IURIEditorOpener globalOpener;

	public GotoXpectMethodAction() {
		XpectPluginActivator.getInstance().getInjector(XpectPluginActivator.ORG_ECLIPSE_XPECT_XPECT).injectMembers(this);
	}

	public void run(IAction action) {
		run();
	}

	public void run() {
		TestSuiteElement suite = testCaseElement.getParent();
		TestElementInfo parsed = TestDataUIUtil.parse(suite);

		String testMethodName = testCaseElement.getTestMethodName();
		int colon = testMethodName.indexOf(':');
		if (colon > 0) {
			testMethodName = testMethodName.substring(0, colon);
		}
		// URI is the result of the parent test suite and the fragment to the Xpect method.
		URI uri = parsed.getURI().appendFragment(testMethodName);		
		new OpenFileAction(globalOpener, uri, "foo", "bar").run();
	}

	public void selectionChanged(SelectionChangedEvent event) {
		// ignore		
	}

	public void init(IViewPart view) {
		// ignore
	}

	public void selectionChanged(IAction action, ISelection selection) {
		testCaseElement = null;
		if (selection instanceof TreeSelection) {
			TreeSelection t = (TreeSelection) selection;
			TestElement testElement = (TestElement) t.getFirstElement();
			if (testElement instanceof TestCaseElement) {
				testCaseElement = (TestCaseElement) testElement;
			}
		}
		action.setEnabled(testCaseElement != null);
	}
}