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

package org.eclipse.xpect.registry;

import java.util.Collection;

public class DelegatingExtensionInfoRegistry implements IExtensionInfo.Registry {

	private IExtensionInfo.Registry delegate;

	public IExtensionInfo.Registry getDelegate() {
		return delegate;
	}

	public Collection<String> getExtensionPoints() {
		return delegate.getExtensionPoints();
	}

	public Collection<IExtensionInfo> getExtensions(String extensionPointName) {
		return delegate.getExtensions(extensionPointName);
	}

	public void setDelegate(IExtensionInfo.Registry delegate) {
		this.delegate = delegate;
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
