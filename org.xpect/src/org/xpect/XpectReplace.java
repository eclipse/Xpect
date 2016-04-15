package org.xpect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To replace any contribution (even the ones shipped with Xpect), you can annotate your own contribution with @XpectReplace(FooBar). FooBar being the contribution you want to
 * replace. A contribution that has been replaced is not used any further by Xpect.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
public @interface XpectReplace {
	Class<?>[] value();
}
