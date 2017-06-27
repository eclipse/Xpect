package org.xpect.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xpect.XpectContributionRole;

/**
 * Marks a class to be a XpectSetupRoot contribution to the Xpect Test Setup.
 * 
 * The types of the add(foo)-methods of this class are the types allowed as root-components in the XPECT_SETUP section. They should be @{@link XpectSetupComponent}s. The
 * initialization will read the components from the XPECT_SETUP section and call the add(...) methods accordingly. Furthermore classes annotated with @XpectSetupRoot can be invoked
 * with ISetupInitializer.initialize(T).
 */
@Inherited
@XpectContributionRole
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XpectSetupRoot {

}
