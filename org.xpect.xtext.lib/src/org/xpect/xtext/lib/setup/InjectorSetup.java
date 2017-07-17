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

package org.xpect.xtext.lib.setup;

import org.xpect.XpectFile;
import org.xpect.XpectJavaModel;
import org.xpect.setup.XpectSetupFactory;
import org.xpect.state.Creates;
import org.xpect.util.IXtInjectorProvider;

import com.google.inject.Injector;

@XpectSetupFactory
public class InjectorSetup {
	private final XpectFile file;
	private final XpectJavaModel xjm;

	public InjectorSetup(XpectJavaModel xjm, XpectFile file) {
		super();
		this.xjm = xjm;
		this.file = file;
	}

	@Creates
	public Injector createInjector() {
		return IXtInjectorProvider.INSTANCE.getInjector(xjm, file.eResource().getURI());
	}

}
