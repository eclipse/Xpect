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

import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.expectation.impl.StringDiffExpectationImpl;
import org.eclipse.xpect.text.GenericTokenizer;

import com.google.common.base.Function;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@XpectImport(StringDiffExpectationImpl.class)
public @interface StringDiffExpectation {

	boolean allowSingleLineDiff() default true;

	boolean allowSingleSegmentDiff() default true;

	int linesAfterDiff() default 4;

	int linesBeforeDiff() default 4;

	Class<? extends Function<String, ? extends Iterable<String>>> tokenizer() default GenericTokenizer.class;

	boolean whitespaceSensitive() default false;
}
