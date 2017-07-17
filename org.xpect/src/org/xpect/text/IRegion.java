package org.xpect.text;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public interface IRegion {

	CharSequence getDocument();

	int getLength();

	int getOffset();

	String getRegionText();
}
