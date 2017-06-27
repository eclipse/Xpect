package org.xpect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation @XpectRequiredEnvironment can be used on contributions to activate them only for the specified environments.
 * 
 * Example: @XpectRequiredEnvironment(Environment.STANDALONE_TEST)<br>
 * This allows to have implementations of contributions that are specific to certain environments.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XpectRequiredEnvironment {
	Environment[]value();
}
