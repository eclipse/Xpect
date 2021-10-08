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

package org.eclipse.xpect.state;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.eclipse.xpect.setup.XpectSetupFactory;

/**
 * Used to mark factory methods in a {@link XpectSetupFactory}.
 * 
 * Instances returned from these factory methods can be injected in Xpect test methods and setup contributions. An Annotation can be given to identify exactly one creation
 * method/instance during injection. Otherwise, the matching is done by type.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Creates {
	Class<? extends Annotation>value() default Default.class;
}
