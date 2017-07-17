package org.xpect.xtext.lib.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@Deprecated
// now the OFFSET parameter from @ParameterParser takes care of default offsets
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface ThisOffset {

}
