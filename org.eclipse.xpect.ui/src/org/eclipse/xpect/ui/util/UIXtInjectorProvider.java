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

package org.eclipse.xpect.ui.util;

import java.util.List;

import org.eclipse.xpect.XtRuntimeModule;
import org.eclipse.xpect.ui.XtTestUIModule;
import org.eclipse.xpect.ui.XtWorkbenchUIModule;
import org.eclipse.xpect.util.EnvironmentUtil;
import org.eclipse.xpect.util.IXtInjectorProvider.RuntimeXtInjectorProvider;

import com.google.inject.Module;

public class UIXtInjectorProvider extends RuntimeXtInjectorProvider {

	@Override
	protected void collectBuiltinModules(List<Class<? extends Module>> moduleClasses) {
		switch (EnvironmentUtil.ENVIRONMENT) {
		case STANDALONE_TEST:
			moduleClasses.add(XtRuntimeModule.class);
			break;
		case PLUGIN_TEST:
			moduleClasses.add(XtRuntimeModule.class);
			moduleClasses.add(XtTestUIModule.class);
			break;
		case WORKBENCH:
			moduleClasses.add(XtRuntimeModule.class);
			moduleClasses.add(XtWorkbenchUIModule.class);
			break;
		}
	}
}
