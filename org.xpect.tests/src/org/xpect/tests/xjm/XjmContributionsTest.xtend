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

package org.xpect.tests.xjm

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import org.junit.Ignore
import org.junit.Test
import org.xpect.XpectImport
import org.xpect.XpectReplace
import org.xpect.runner.Xpect
import org.xpect.setup.XpectSetupFactory

class XjmContributionsTest extends AbstractXjmTest {

	@Test def void simple() {
		assertXjm(Simple1)
	}

	@Test def void transitiveImport() {
		assertXjm(TransitiveImport)
	}

	@Test def void methodType() {
		assertXjm(MethodType)
	}

	@Test def void methodAnnotation() {
		assertXjm(MethodAnnotation)
	}

	@Ignore @Test def void annotationType() {
		assertXjm(AnnotationType)
	}

	@Test def void replace1() {
		assertXjm(Replacement1)
	}
	
	@Test def void replace2() {
		assertXjm(Replacement2)
	}
}

@XpectImport(Contribution1)
class Simple1 {
	override toString() '''
		suite org.xpect.tests.xjm.Simple1 {
		  test org.xpect.tests.xjm.Simple1 {} // Imports: Contribution1
		  contributionsFor @XpectSetupFactory {
		    org.xpect.tests.xjm.Contribution1 ImportedBy:Simple1
		  }
		}
	'''
}

@XpectSetupFactory
class Contribution1 {
}

@XpectImport(ContributionWithChild)
class TransitiveImport {
	override toString() '''
		suite org.xpect.tests.xjm.TransitiveImport {
		  test org.xpect.tests.xjm.TransitiveImport {} // Imports: ContributionWithChild, Contribution1
		  contributionsFor @XpectSetupFactory {
		    org.xpect.tests.xjm.Contribution1 ImportedBy:ContributionWithChild
		    org.xpect.tests.xjm.ContributionWithChild ImportedBy:TransitiveImport
		  }
		}
	'''
}

@XpectSetupFactory
@XpectImport(Contribution1)
class ContributionWithChild {
}

class MethodType {

	@Xpect def void test(ContributionWithChild arg0) {
	}

	override toString() '''
		suite org.xpect.tests.xjm.MethodType {
		  test org.xpect.tests.xjm.MethodType {
		    @Xpect public void test(); // Imports: ContributionWithChild, Contribution1
		  }
		  contributionsFor @XpectSetupFactory {
		    org.xpect.tests.xjm.Contribution1 ImportedBy:ContributionWithChild
		    org.xpect.tests.xjm.ContributionWithChild ImportedBy:MethodType.test()
		  }
		}
	'''
}

class MethodAnnotation {

	@AnnotationWithImport
	@Xpect def void test() {
	}

	override toString() '''
		suite org.xpect.tests.xjm.MethodAnnotation {
		  test org.xpect.tests.xjm.MethodAnnotation {
		    @Xpect public void test(); // Imports: Contribution1
		  }
		  contributionsFor @XpectSetupFactory {
		    org.xpect.tests.xjm.Contribution1 ImportedBy:MethodAnnotation.test()
		  }
		}
	'''
}

@Retention(RetentionPolicy.RUNTIME)
@XpectImport(Contribution1)
annotation AnnotationWithImport {
}

class AnnotationType {

	@Xpect def void test(@AnnotationWithImport String arg0) {
	}

	override toString() '''
		suite org.xpect.tests.xjm.AnnotationType {
		  test org.xpect.tests.xjm.AnnotationType {
		    @Xpect public void test(); // Imports: Contribution1
		  }
		  contributionsFor @XpectSetupFactory {
		    org.xpect.tests.xjm.Contribution1 ImportedBy:AnnotationType.test()
		  }
		}
	'''
}

@XpectImport(#[Contribution1, Contribution1Replacement])
class Replacement1 {
	override toString() '''
		suite org.xpect.tests.xjm.Replacement1 {
		  test org.xpect.tests.xjm.Replacement1 {} // Imports: Contribution1, Contribution1Replacement
		  contributionsFor @XpectSetupFactory {
		    [INACTIVE] org.xpect.tests.xjm.Contribution1 InactiveBecause: ReplacedBy: org.xpect.tests.xjm.Contribution1Replacement ImportedBy:Replacement1 Contribution1Replacement
		    org.xpect.tests.xjm.Contribution1Replacement ImportedBy:Replacement1
		  }
		}
	'''
}

@XpectImport(#[Contribution1Replacement])
class Replacement2 {
	override toString() '''
		suite org.xpect.tests.xjm.Replacement2 {
		  test org.xpect.tests.xjm.Replacement2 {} // Imports: Contribution1Replacement, Contribution1
		  contributionsFor @XpectSetupFactory {
		    [INACTIVE] org.xpect.tests.xjm.Contribution1 InactiveBecause: ReplacedBy: org.xpect.tests.xjm.Contribution1Replacement ImportedBy:Contribution1Replacement
		    org.xpect.tests.xjm.Contribution1Replacement ImportedBy:Replacement2
		  }
		}
	'''
}

@XpectSetupFactory
@XpectReplace(Contribution1)
class Contribution1Replacement extends Contribution1 {
}
