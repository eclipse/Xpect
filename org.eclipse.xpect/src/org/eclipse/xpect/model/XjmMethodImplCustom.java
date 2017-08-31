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

package org.eclipse.xpect.model;

import java.lang.reflect.Method;

import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xpect.util.IJavaReflectAccess;

public class XjmMethodImplCustom extends XjmMethodImpl {
	@Override
	public String getName() {
		return getJvmMethod().getSimpleName();
	}

	@Override
	public void setJvmMethod(JvmOperation newJvmMethod) {
		super.javaMethod = null;
		super.setJvmMethod(newJvmMethod);
	}

	@Override
	public Method getJavaMethod() {
		Method result = super.getJavaMethod();
		if (result != null)
			return result;
		JvmOperation jvmOperation = super.getJvmMethod();
		if (jvmOperation == null || jvmOperation.eIsProxy())
			return null;
		return super.javaMethod = IJavaReflectAccess.INSTANCE.getMethod(jvmOperation);
	}

}
