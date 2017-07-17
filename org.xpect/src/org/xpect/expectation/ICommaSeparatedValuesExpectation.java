package org.xpect.expectation;

import java.util.List;

import org.xpect.XpectImport;
import org.xpect.expectation.impl.CommaSeparatedValuesExpectationImpl;

import com.google.common.base.Predicate;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectImport(CommaSeparatedValuesExpectationImpl.class)
public interface ICommaSeparatedValuesExpectation {
	public interface Value {
		String getText();

		boolean isNegated();

		boolean isWildcard();
	}

	void assertEquals(Iterable<?> string);

	void assertEquals(Iterable<?> string, Predicate<String> predicate);

	void assertEquals(Predicate<String> predicate);

	List<Value> getExpectedValues();
}
