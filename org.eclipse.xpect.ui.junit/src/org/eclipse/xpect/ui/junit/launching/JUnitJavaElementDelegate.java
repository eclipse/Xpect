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

package org.eclipse.xpect.ui.junit.launching;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;
import org.junit.runner.Description;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class JUnitJavaElementDelegate implements IAdaptable {

	private Description description = null;
	private IJavaElement javaElement = null;
	private String name = null;
	private IResource resource = null;

	public <T> T getAdapter(Class<T> adapter) {
		if (getClass().equals(adapter))
			return adapter.cast(this);
		if (IJavaElement.class.equals(adapter))
			return adapter.cast(javaElement);
		if (Description.class.equals(adapter))
			return adapter.cast(description);
		return null;
	}

	public Description getDescription() {
		return description;
	}

	public IJavaElement getJavaElement() {
		return javaElement;
	}

	public String getName() {
		return name;
	}

	public IResource getResource() {
		return resource;
	}

	public void setDescription(Description description) {
		this.description = description;
	}

	public void setJavaElement(IJavaElement javaElement) {
		this.javaElement = javaElement;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setResource(IResource resource) {
		this.resource = resource;
	}

}
