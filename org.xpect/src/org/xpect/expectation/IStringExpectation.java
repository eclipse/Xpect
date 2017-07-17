package org.xpect.expectation;

import org.xpect.XpectImport;
import org.xpect.expectation.impl.StringExpectationImpl;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectImport(StringExpectationImpl.class)
public interface IStringExpectation {
	void assertEquals(Object string);
}
