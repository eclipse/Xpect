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

package org.eclipse.xpect.expectation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.xpect.expectation.impl.ActualCollection.ToString;

import com.google.common.base.Function;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface CommaSeparatedValuesExpectation {

	boolean caseSensitive() default true;

	Class<? extends Function<Object, String>> itemFormatter() default ToString.class;

	int maxItemsPerLine() default -1;

	int maxLineWidth() default 80;

	boolean ordered() default false;

	boolean quoted() default false;

	boolean whitespaceSensitive() default false;

}
