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

import org.eclipse.xpect.XpectImport;

@XpectImport({ Contribution1Replacement.class })
public class Replacement2 {
  @Override
  public String toString() {
    return  "\n" +
			"suite org.eclipse.xpect.tests.xjm.Replacement2 {\n" +
			"  test org.eclipse.xpect.tests.xjm.Replacement2 {} // Imports: Contribution1Replacement, Contribution1\n" +
			"  contributionsFor @XpectSetupFactory {\n" +
			"    [INACTIVE] org.eclipse.xpect.tests.xjm.Contribution1 InactiveBecause: ReplacedBy: org.eclipse.xpect.tests.xjm.Contribution1Replacement ImportedBy:Contribution1Replacement\n" +
			"    org.eclipse.xpect.tests.xjm.Contribution1Replacement ImportedBy:Replacement2\n" +
			"  }\n" +
			"}\n";
  }
}
