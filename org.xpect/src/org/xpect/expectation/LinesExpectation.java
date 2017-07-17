package org.xpect.expectation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xpect.expectation.impl.ActualCollection.ToString;
import org.xpect.util.IdentityStringFunction;

import com.google.common.base.Function;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LinesExpectation {

	boolean caseSensitive() default true;

	Class<? extends Function<Object, String>> itemFormatter() default ToString.class;
	
	Class<? extends Function<String, String>> expectationFormatter() default IdentityStringFunction.class;
	
	boolean ordered() default false;

	boolean quoted() default false;

	boolean whitespaceSensitive() default false;

}
