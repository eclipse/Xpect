package org.xpect.expectation;

import org.xpect.XpectImport;
import org.xpect.expectation.impl.LinesExpectationImpl;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectImport(LinesExpectationImpl.class)
public interface ILinesExpectation {
	void assertEquals(Iterable<?> string);

	void assertEquals(String message, Iterable<?> string);
}
