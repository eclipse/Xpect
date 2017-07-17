package org.xpect.expectation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface StringExpectation {

	boolean caseSensitive() default true;

	boolean whitespaceSensitive() default false;

	boolean newLineCharacterSensitive() default false;
}
