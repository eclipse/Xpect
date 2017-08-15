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

package org.eclipse.xpect.tests.state;

import java.lang.reflect.Constructor;

import org.eclipse.xpect.AbstractComponent;
import org.eclipse.xpect.setup.SetupInitializer;

public class SetupInitializerTestData {
	
	
	  public static class A {
	    public A(final Double... d) {
	    }
	    
	    public A(final String... s) {
	    }
	    
	    public A(final String a, final String b) {
	    }
	    
	    public A(final String a, final String... s) {
	    }
	    
	    public A(final Double a, final String... s) {
	    }
	  }
	  
	  public static class SiT<T extends Object> extends SetupInitializer<T> {
	    public SiT(final AbstractComponent rootInstance) {
	      super(rootInstance);
	    }
	    
	    public Constructor<?> findConstructor(final Class<?> clazz, final Object[] params) {
	      return super.findConstructor(clazz, params);
	    }
	  }

}
