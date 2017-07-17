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

package org.eclipse.xtext.example.arithmetics


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class ArithmeticsStandaloneSetup extends ArithmeticsStandaloneSetupGenerated {

	def static void doSetup() {
		new ArithmeticsStandaloneSetup().createInjectorAndDoEMFRegistration()
	}

}
