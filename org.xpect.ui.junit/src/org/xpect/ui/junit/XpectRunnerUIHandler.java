package org.xpect.ui.junit;

import org.eclipse.emf.common.util.URI;
import org.xpect.ui.XpectPluginActivator;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.runners.IRunnerUIHandler;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.xtext.ui.editor.IURIEditorOpener;
import org.xpect.ui.junit.TestDataUIUtil.TestElementInfo;

import com.google.inject.Inject;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectRunnerUIHandler implements IRunnerUIHandler {
	
	public XpectRunnerUIHandler() {
		XpectPluginActivator.getInstance().getInjector(XpectPluginActivator.ORG_XPECT_XPECT).injectMembers(this);
	}

	@Inject
	private IURIEditorOpener globalOpener;

	public boolean contextMenuAboutToShow(ViewPart part, ITestElement ctx, IMenuManager menu) {
		TestElementInfo parsed = TestDataUIUtil.parse(ctx);
		URI uri = parsed.getURI();
		CompareAction compareAction = new CompareAction(ctx);
		if (compareAction.isEnabled())
			menu.add(compareAction);
		if (uri != null) {
			if (uri.hasFragment())
				menu.add(new OpenFileAction(globalOpener, uri, "Go to XPECT", "Show XPECT statement in the Xpect file editor."));
			else
				menu.add(new OpenFileAction(globalOpener, uri, "Go to File", "Open file in the Xpect editor."));
		}
		String method = parsed.getMethod();
		if (method != null)
			menu.add(new OpenJavaMethodAction(parsed, "Go to Method", "Show Java Method declaration"));
		return false;
	}

	public String getSimpleLabel(ViewPart part, ITestElement element) {
		return null;
	}

	public StyledString getStyledLabel(ViewPart part, ITestElement element, int layout) {
		String label = TestDataUIUtil.parse(element).getTitle();
		if (label == null)
			return new StyledString("???");
		int colon = label.indexOf(':');
		if (colon >= 0) {
			StyledString title = new StyledString(label.substring(0, colon));
			return StyledCellLabelProvider.styleDecoratedString(label, StyledString.QUALIFIER_STYLER, title);
		}
		return new StyledString(label);
	}

	public boolean handleDoubleClick(ViewPart part, ITestElement ctx) {
		CompareAction compareAction = new CompareAction(ctx);
		if (compareAction.isEnabled()) {
			compareAction.run();
			return true;
		}
		TestElementInfo parsed = TestDataUIUtil.parse(ctx);
		URI uri = parsed.getURI();
		if (uri != null) {
			new OpenFileAction(globalOpener, uri, "foo", "bar").run();
			return true;
		}
		return false;
	}
}
