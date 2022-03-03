/*******************************************************************************
 * Copyright (c) 2012, 2022 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/
package org.eclipse.xpect.tests.state;

import org.eclipse.xpect.state.Configuration;
import org.eclipse.xpect.state.ResolvedConfiguration;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

public class StateTestUtil {
	public static ResolvedConfiguration newAnalyzedConfiguration(Procedure1<? super Configuration> init) {
		Configuration configuration = new Configuration(null);
		if (init != null) {
			init.apply(configuration);
		}
		return new ResolvedConfiguration(configuration);
	}

	public static ResolvedConfiguration newAnalyzedConfiguration(ResolvedConfiguration parent, Procedure1<? super Configuration> init) {
		Configuration configuration = new Configuration(null);
		if (init != null) {
			init.apply(configuration);
		}
		return new ResolvedConfiguration(parent, configuration);
	}
}
