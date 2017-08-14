/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

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
