package org.xpect.registry;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.google.inject.BindingAnnotation;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultBinding {

}
