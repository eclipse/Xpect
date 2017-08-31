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
