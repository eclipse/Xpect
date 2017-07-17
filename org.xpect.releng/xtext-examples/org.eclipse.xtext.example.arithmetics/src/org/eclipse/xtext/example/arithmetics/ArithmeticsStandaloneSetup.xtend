package org.eclipse.xtext.example.arithmetics


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class ArithmeticsStandaloneSetup extends ArithmeticsStandaloneSetupGenerated {

	def static void doSetup() {
		new ArithmeticsStandaloneSetup().createInjectorAndDoEMFRegistration()
	}

}
