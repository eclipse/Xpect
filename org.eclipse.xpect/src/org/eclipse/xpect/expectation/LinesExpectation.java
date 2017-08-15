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

package org.eclipse.xpect.expectation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.xpect.expectation.impl.ActualCollection.ToString;
import org.eclipse.xpect.util.IdentityStringFunction;

import com.google.common.base.Function;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LinesExpectation {

	boolean caseSensitive() default true;

	Class<? extends Function<Object, String>> itemFormatter() default ToString.class;
	
	Class<? extends Function<String, String>> expectationFormatter() default IdentityStringFunction.class;
	
	boolean ordered() default false;

	boolean quoted() default false;

	boolean whitespaceSensitive() default false;

}
