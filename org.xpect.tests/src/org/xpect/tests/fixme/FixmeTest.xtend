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

import org.eclipse.xtext.junit4.InjectWith
import org.junit.runner.RunWith
import org.xpect.XpectInjectorProvider
import org.xpect.expectation.IExpectationRegion
import org.xpect.expectation.IStringExpectation
import org.xpect.expectation.StringExpectation
import org.xpect.expectation.StringExpectation.StringExpectationImpl
import org.xpect.runner.Xpect
import org.xpect.runner.XpectRunner
import org.xpect.runner.XpectTestFiles
import org.xpect.setup.XpectSetup

/**
 * Checking FIXME Keyword in XPECT.
 */
@RunWith( XpectRunner )
@XpectTestFiles( fileExtensions="xt" )
@XpectSetup( #[ ExampleValidatingSetup /*, XtextStandaloneSetup */, SampleTargetSyntaxSupport ]  )
@InjectWith( XpectInjectorProvider )
class FixmeTest {

	
	/** Fails if expectation is not 'fun' */
	@Xpect 
	def void joy(@StringExpectation IStringExpectation expectation){
		println("en-joy")
		val region = (expectation as StringExpectationImpl).region
		println(" +--> expectation = "+ region.expectationToString)
		
		expectation.assertEquals("fun")		
	}
	
	def String expectationToString(IExpectationRegion region) {
		return region.document.subSequence( region.offset, region.offset+region.length ).toString
	}

	
		
}