package org.xpect.runner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface XpectURIProvider {
	Class<? extends IXpectURIProvider> value();
}
