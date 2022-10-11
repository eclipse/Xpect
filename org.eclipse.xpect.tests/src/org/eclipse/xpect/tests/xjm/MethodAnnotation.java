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

import org.eclipse.xpect.runner.Xpect;

public class MethodAnnotation {
  @AnnotationWithImport
  @Xpect
  public void test() {
  }

  @Override
  public String toString() {
    return  "\n" +
			"suite org.eclipse.xpect.tests.xjm.MethodAnnotation {\n" +
			"  test org.eclipse.xpect.tests.xjm.MethodAnnotation {\n" +
			"    @Xpect public void test(); // Imports: Contribution1\n" +
			"  }\n" +
			"  contributionsFor @XpectSetupFactory {\n" +
			"    org.eclipse.xpect.tests.xjm.Contribution1 ImportedBy:MethodAnnotation.test()\n" +
			"  }\n" +
			"}\n";
  }
}
