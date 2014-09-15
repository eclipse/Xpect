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

import org.xpect.expectation.ITargetSyntaxSupport
import org.xpect.state.Creates
import org.xpect.expectation.ITargetSyntaxSupport.ITargetLiteralSupport
import org.xpect.text.IReplacement

class SampleTargetSyntaxSupport implements ITargetSyntaxSupport {
	
	public new( ){
		
	}
	
	override getLiteralSupport(int offset) {
		println("getLiteralSupport offset="+offset);
		return new SampleTargetLiteralSupport();
	}
	
	override supportsMultiLineLiteral() {
		false
	}
	
	
	@Creates
	public def ITargetSyntaxSupport create() {
		return this;
	}
	
}

class SampleTargetLiteralSupport implements ITargetLiteralSupport {
	
	override adoptToTargetSyntax(IReplacement replacement, boolean enforceMultilineLiteral) {
		// assuming nothing must be changed
		return replacement
	}
	
	override escape(String value) {
		return "'"+value+"'"
	}
	
}