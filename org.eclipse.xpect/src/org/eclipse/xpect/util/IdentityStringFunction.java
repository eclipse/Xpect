package org.eclipse.xpect.util;

import com.google.common.base.Function;

/**
 * @author Dennis Huebner - Initial contribution and API
 *
 */
final public class IdentityStringFunction implements Function<String, String> {

	@Override
	public String apply(String input) {
		return input;
	}

}