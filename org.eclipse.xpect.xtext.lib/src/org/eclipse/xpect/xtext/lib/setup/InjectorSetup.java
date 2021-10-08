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

package org.eclipse.xpect.xtext.lib.setup;

import org.eclipse.xpect.XpectFile;
import org.eclipse.xpect.XpectJavaModel;
import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.util.IXtInjectorProvider;

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
