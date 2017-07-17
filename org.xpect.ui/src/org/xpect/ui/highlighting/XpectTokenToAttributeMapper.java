package org.xpect.ui.highlighting;

import java.util.Set;

import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultAntlrTokenToAttributeIdMapper;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration;

import com.google.common.collect.ImmutableSet;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectTokenToAttributeMapper extends DefaultAntlrTokenToAttributeIdMapper {

	private final Set<String> keywords_2_7_x = ImmutableSet.of("KEYWORD_8", "KEYWORD_6", "KEYWORD_9", "KEYWORD_11", "KEYWORD_7", "KEYWORD_10");
	private final Set<String> keywords_2_8_x = ImmutableSet.of("True", "False", "FIXME", "END_SETUP", "XPECT_SETUP", "XPECT_IGNORE", "XPECT");

	@Override
	protected String calculateId(String tokenName, int tokenType) {
		if (keywords_2_7_x.contains(tokenName) || keywords_2_8_x.contains(tokenName))
			return DefaultHighlightingConfiguration.KEYWORD_ID;
		if (tokenName.startsWith("KEYWORD_"))
			return DefaultHighlightingConfiguration.PUNCTUATION_ID;
		return super.calculateId(tokenName, tokenType);
	}

}
