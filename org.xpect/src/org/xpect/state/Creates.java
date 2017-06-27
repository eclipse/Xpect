package org.xpect.state;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.xpect.setup.XpectSetupFactory;

/**
 * Used to mark factory methods in a {@link XpectSetupFactory}.
 * 
 * Instances returned from these factory methods can be injected in Xpect test methods and setup contributions. An Annotation can be given to identify exactly one creation
 * method/instance during injection. Otherwise, the matching is done by type.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Creates {
	Class<? extends Annotation>value() default Default.class;
}
