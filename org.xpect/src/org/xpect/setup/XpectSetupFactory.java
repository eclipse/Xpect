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
import org.xpect.XpectImport;

/**
 * This annotation is used to mark a class as a XpectSetupFactory. The class must be imported in a test via @{@link XpectImport}.<br>
 * All methods annotated with @Creates contribute objects to the Xpect Setup and can be injected into @Xpect methods and other setup contributions. The class is instantiated and
 * managed by the StateContainer.
 */
@Inherited
@XpectContributionRole
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XpectSetupFactory {
}
