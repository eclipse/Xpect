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

package org.eclipse.xpect.tests.xjm

import org.eclipse.xpect.XpectImport
import org.eclipse.xpect.runner.Xpect

@XpectImport(Contribution1)
class Simple1 {
	override toString() '''
		suite org.eclipse.xpect.tests.xjm.Simple1 {
		  test org.eclipse.xpect.tests.xjm.Simple1 {} // Imports: Contribution1
		  contributionsFor @XpectSetupFactory {
		    org.eclipse.xpect.tests.xjm.Contribution1 ImportedBy:Simple1
		  }
		}
	'''
}

@XpectImport(ContributionWithChild)
class TransitiveImport {
	override toString() '''
		suite org.eclipse.xpect.tests.xjm.TransitiveImport {
		  test org.eclipse.xpect.tests.xjm.TransitiveImport {} // Imports: ContributionWithChild, Contribution1
		  contributionsFor @XpectSetupFactory {
		    org.eclipse.xpect.tests.xjm.Contribution1 ImportedBy:ContributionWithChild
		    org.eclipse.xpect.tests.xjm.ContributionWithChild ImportedBy:TransitiveImport
		  }
		}
	'''
}

class MethodType {

	@Xpect def void test(ContributionWithChild arg0) {
	}

	override toString() '''
		suite org.eclipse.xpect.tests.xjm.MethodType {
		  test org.eclipse.xpect.tests.xjm.MethodType {
		    @Xpect public void test(); // Imports: ContributionWithChild, Contribution1
		  }
		  contributionsFor @XpectSetupFactory {
		    org.eclipse.xpect.tests.xjm.Contribution1 ImportedBy:ContributionWithChild
		    org.eclipse.xpect.tests.xjm.ContributionWithChild ImportedBy:MethodType.test()
		  }
		}
	'''
}

class MethodAnnotation {

	@AnnotationWithImport
	@Xpect def void test() {
	}

	override toString() '''
		suite org.eclipse.xpect.tests.xjm.MethodAnnotation {
		  test org.eclipse.xpect.tests.xjm.MethodAnnotation {
		    @Xpect public void test(); // Imports: Contribution1
		  }
		  contributionsFor @XpectSetupFactory {
		    org.eclipse.xpect.tests.xjm.Contribution1 ImportedBy:MethodAnnotation.test()
		  }
		}
	'''
}

class AnnotationType {

	@Xpect def void test(@AnnotationWithImport String arg0) {
	}

	override toString() '''
		suite org.eclipse.xpect.tests.xjm.AnnotationType {
		  test org.eclipse.xpect.tests.xjm.AnnotationType {
		    @Xpect public void test(); // Imports: Contribution1
		  }
		  contributionsFor @XpectSetupFactory {
		    org.eclipse.xpect.tests.xjm.Contribution1 ImportedBy:AnnotationType.test()
		  }
		}
	'''
}

@XpectImport(#[Contribution1, Contribution1Replacement])
class Replacement1 {
	override toString() '''
		suite org.eclipse.xpect.tests.xjm.Replacement1 {
		  test org.eclipse.xpect.tests.xjm.Replacement1 {} // Imports: Contribution1, Contribution1Replacement
		  contributionsFor @XpectSetupFactory {
		    [INACTIVE] org.eclipse.xpect.tests.xjm.Contribution1 InactiveBecause: ReplacedBy: org.eclipse.xpect.tests.xjm.Contribution1Replacement ImportedBy:Replacement1 Contribution1Replacement
		    org.eclipse.xpect.tests.xjm.Contribution1Replacement ImportedBy:Replacement1
		  }
		}
	'''
}

@XpectImport(#[Contribution1Replacement])
class Replacement2 {
	override toString() '''
		suite org.eclipse.xpect.tests.xjm.Replacement2 {
		  test org.eclipse.xpect.tests.xjm.Replacement2 {} // Imports: Contribution1Replacement, Contribution1
		  contributionsFor @XpectSetupFactory {
		    [INACTIVE] org.eclipse.xpect.tests.xjm.Contribution1 InactiveBecause: ReplacedBy: org.eclipse.xpect.tests.xjm.Contribution1Replacement ImportedBy:Contribution1Replacement
		    org.eclipse.xpect.tests.xjm.Contribution1Replacement ImportedBy:Replacement2
		  }
		}
	'''
}
