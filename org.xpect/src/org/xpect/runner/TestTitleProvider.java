package org.xpect.runner;

import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.xpect.XpectInvocation;
import org.xpect.parameter.IStatementRelatedRegionProvider;
import org.xpect.parameter.StatementRelatedRegionProvider;
import org.xpect.text.Text;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@StatementRelatedRegionProvider
public class TestTitleProvider implements IStatementRelatedRegionProvider {

	protected TestTitleRegion findTitle(XpectInvocation invocation, String documentString, int offset) {
		Text document = new Text(documentString);
		int lineStart = document.currentLineStart(offset);
		if (lineStart > 0) {
			String prefix = document.substring(lineStart, offset);
			String trimmedPrefix = prefix.trim();
			if (trimmedPrefix.length() > 0)
				for (int i = 0; i < trimmedPrefix.length(); i++) {
					char c = trimmedPrefix.charAt(i);
					if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))
						return new TestTitleRegion(invocation, lineStart, offset - lineStart);
				}
			int prevLine = document.previousLineStart(offset);
			if (prevLine >= 0) {
				String prevPrefix = document.substring(prevLine, prevLine + prefix.length());
				if (prefix.equals(prevPrefix)) {
					String title = document.substring(prevLine + prefix.length(), lineStart).trim();
					if (title.length() > 0)
						return new TestTitleRegion(invocation, prevLine + prefix.length(), lineStart - (prevLine + prefix.length()));
				}
			}
		}
		return null;
	}

	@Override
	public TestTitleRegion getRegion(XpectInvocation invocation) {
		INode node = NodeModelUtils.findActualNodeFor(invocation);
		String document = node.getRootNode().getText();
		TestTitleRegion title = findTitle(invocation, document, node.getOffset());
		if (title != null && title.getRegionText().startsWith("XPECT"))
			title = null;
		return title;
	}

}
