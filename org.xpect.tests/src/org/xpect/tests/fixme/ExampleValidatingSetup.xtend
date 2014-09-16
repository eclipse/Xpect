/*******************************************************************************
 * Copyright (c) 2014 NumberFour AG (http://numberfour.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Torsten Krämer - initial API and implementation
 *******************************************************************************/
package org.xpect.tests.fixme

import org.xpect.runner.ValidatingSetup
import org.xpect.setup.ThisRootTestClass
import org.xpect.setup.XpectSetup
import org.xpect.state.Creates

@XpectSetup(ExampleValidatingSetup)
class ExampleValidatingSetup extends ValidatingSetup {

	new(@ThisRootTestClass Object o){
		println("ExampleValidatingSetup o="+o)
	}

	override validate() {
		println("Hallo fixme - validate in ExampleValidatingSetup")	
	}
	
	/** 
	 * This setup class (@XpectSetup) needs a method to return the thing which it sets up. 
	 * Here we simply return the instance of this.
	 */
	@Creates
	public def ValidatingSetup create() {
		return this;
	}
	
}