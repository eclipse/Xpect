package org.xpect.expectation;

import com.google.common.base.Function;

/**
 * @author Dennis Huebner - Initial contribution and API
 *
 */
final public class ByPassExpectationImpl implements Function<String,String> {

	@Override
	public String apply(String input) {
		return input;
	}
	
}