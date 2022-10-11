/**
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 */
package org.eclipse.xpect.tests;

import com.google.common.base.Joiner;
import org.junit.Assert;

public class TestUtil {
	public static void assertEquals(final Object expected, final Object actual) {
		String e = null;
		if (expected instanceof Iterable) {
			e = Joiner.on("\n").join(((Iterable<?>) expected));
		} else {
			if (expected != null) {
				e = expected.toString();
			}
			if (e == null) {
				e = "null";
			}
			e = e.trim();
		}
		String a = null;
		if (actual != null) {
			a = actual.toString();
		}
		if (a == null) {
			a = "null";
		}
		a = a.trim();
		Assert.assertEquals(e, a);
	}
}
