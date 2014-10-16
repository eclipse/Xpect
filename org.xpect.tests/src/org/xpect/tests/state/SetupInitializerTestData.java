package org.xpect.tests.state;

import java.lang.reflect.Constructor;

import org.xpect.AbstractComponent;
import org.xpect.setup.SetupInitializer;

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
