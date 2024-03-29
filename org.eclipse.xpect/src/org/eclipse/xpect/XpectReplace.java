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

package org.eclipse.xpect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To replace any contribution (even the ones shipped with Xpect), you can annotate your own contribution with @XpectReplace(FooBar).
 * 
 * FooBar being the contribution you want to replace. A contribution that has been replaced is not used any further by Xpect. A replacing contribution will often be a subclass of
 * the contribution it replaces. However, this is not required as long as the replacing contribution fulfills all contracts the replaced contribution provides.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
public @interface XpectReplace {
	Class<?>[]value();
}
