package org.xpect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xpect.runner.Xpect;
import org.xpect.setup.XpectGuiceModule;
import org.xpect.setup.XpectSetupComponent;
import org.xpect.setup.XpectSetupFactory;
import org.xpect.setup.XpectSetupRoot;

/**
 * This Annotation is used on a class with @Xpect methods to define what contributions should be present in the Xpect Test Setup. Possible contributions are classes annotated with
 * these Xpect roles:
 * <ul>
 * <li>@{@link XpectSetupFactory}: Provides objects that can be injected into @Xpect methods and other contributions.</li>
 * <li>@{@link XpectSetupComponent}: The class is available in the XPECT_SETUP section, if it is a parameter of some @XpectSetupRoot-annotated class' add(foo) method. These
 * classes will also be proposed by content assist.</li>
 * <li>@{@link XpectSetupRoot}: The types of the add(foo)-methods of this class are the types allowed as root-components in the XPECT_SETUP section.</li>
 * <li>@{@link XpectGuiceModule} The annotated class is a google Guice module. The module will be mixed into the current language’s injector.</li>
 * </ul>
 * The @XpectImport annotation is valid on
 * <ul>
 * <li>all Xpect Java test classes</li>
 * <li>on Java classes that are used as parameter types of @Xpect test methods</li>
 * <li>on Java annotations that are used on parameter types on @Xpect test methods</li>
 * <li>on Java classes that are contributions</li>
 * <li>on super classes of any of the classes mentioned above.</li>
 * </ul>
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
public @interface XpectImport {
	Class<?>[] value();
}
