package org.xpect.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xpect.XpectContributionRole;

/**
 * Marks a class as a component in the XPECT_SETUP section.
 * 
 * For this to work, the test must import a {@link XpectSetupRoot} with an add(Foo) method, where Foo is your XpectSetupComponent class. The {@link XpectSetupRoot} class should
 * have an @XpectImport for your component class.
 */
@Inherited
@XpectContributionRole
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XpectSetupComponent {
}
