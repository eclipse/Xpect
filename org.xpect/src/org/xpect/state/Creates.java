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

package org.xpect.state;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.xpect.setup.XpectSetupFactory;

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
