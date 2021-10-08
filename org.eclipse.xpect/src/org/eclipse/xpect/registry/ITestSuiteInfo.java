/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.eclipse.xpect.registry;

import java.util.Set;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;

public interface ITestSuiteInfo {

	public interface Registry {
		final Registry INSTANCE = EcorePlugin.IS_ECLIPSE_RUNNING ? new TestSuiteInfoRegistry.Delegate() : new TestSuiteInfoRegistry();

		ITestSuiteInfo getTestSuite(Resource resource);
	}

	// String getName();

	LazyClass<Object> getClazz();

	Set<String> getFileExtensions();

	// Set<String> getLanguageNames();
}
