/*******************************************************************************
 * Copyright (c) 2024 Simeon Andreev and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Simeon Andreev - Initial contribution and API
 *******************************************************************************/
package org.eclipse.xpect.runner;

import org.eclipse.xpect.XpectJavaModel;

/**
 * @author Simeon Andreev - Initial contribution and API
 */
public class XpectTestGlobalState {

	public static final XpectTestGlobalState INSTANCE = new XpectTestGlobalState();

	private XpectJavaModel model;
	private Class<?> testClass;

	public void set(XpectJavaModel model, Class<?> testClass) {
		this.model = model;
		this.testClass = testClass;
	}

	public XpectJavaModel model() {
		return model;
	}

	public Class<?> testClass() {
		return testClass;
	}
}
