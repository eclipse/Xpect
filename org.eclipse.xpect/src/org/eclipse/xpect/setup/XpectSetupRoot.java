/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.eclipse.xpect.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.xpect.XpectContributionRole;

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
