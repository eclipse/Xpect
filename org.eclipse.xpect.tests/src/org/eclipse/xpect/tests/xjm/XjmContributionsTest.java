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
package org.eclipse.xpect.tests.xjm;

import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("all")
public class XjmContributionsTest extends AbstractXjmTest {
	@Test
	public void simple() {
		assertXjm(Simple1.class);
	}

	@Test
	public void transitiveImport() {
		assertXjm(TransitiveImport.class);
	}

	@Test
	public void methodType() {
		assertXjm(MethodType.class);
	}

	@Test
	public void methodAnnotation() {
		assertXjm(MethodAnnotation.class);
	}

	@Ignore
	@Test
	public void annotationType() {
		assertXjm(AnnotationType.class);
	}

	@Test
	public void replace1() {
		assertXjm(Replacement1.class);
	}

	@Test
	public void replace2() {
		assertXjm(Replacement2.class);
	}
}
