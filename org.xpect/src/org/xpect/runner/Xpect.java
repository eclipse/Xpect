package org.xpect.runner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Xpect {
	LiveExecutionType liveExecution() default LiveExecutionType.DISABLED;
}
