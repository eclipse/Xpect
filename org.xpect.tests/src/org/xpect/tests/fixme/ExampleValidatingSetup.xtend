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