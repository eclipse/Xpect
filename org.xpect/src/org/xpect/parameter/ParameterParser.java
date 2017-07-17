package org.xpect.parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xpect.XpectImport;

/**
 * Parses text between method name and expectation separator ('---' or '-->') in order to provide arguments for the test. Syntax similar to Xtext, including alternatives etc.
 * 
 * @author Moritz Eysholdt - Initial contribution and API
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@XpectImport({ ParameterParserImpl.class, ParameterRegionProvider.class })
public @interface ParameterParser {

	String syntax() default "";

}
