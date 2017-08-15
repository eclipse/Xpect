/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.eclipse.xpect.util;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.xpect.Environment;
import org.eclipse.xpect.runner.XpectRunner;

import com.google.common.base.Joiner;

public class EnvironmentUtil {
	public static final Environment ENVIRONMENT = detectEnvironement();

	private static Environment detectEnvironement() {
		if (XpectRunner.testClassloader != null) {
			if (EcorePlugin.IS_ECLIPSE_RUNNING)
				return Environment.PLUGIN_TEST;
			else
				return Environment.STANDALONE_TEST;
		} else {
			if (EcorePlugin.IS_ECLIPSE_RUNNING)
				return Environment.WORKBENCH;
			else
				throw new IllegalStateException("not (yet) supported environment: standalone, but without JUnit.");
		}
	}

	public static void requireEnvironment(Environment... environments) {
		for (Environment env : environments)
			if (ENVIRONMENT == env)
				return;
		StringBuilder msg = new StringBuilder();
		msg.append("This class in only applicable in environment(s) ");
		Joiner.on(" or ").appendTo(msg, environments);
		msg.append(", but environment ");
		msg.append(EnvironmentUtil.ENVIRONMENT);
		msg.append(" has been found.");
		throw new RuntimeException(msg.toString());
	}

}
