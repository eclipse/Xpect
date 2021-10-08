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

package org.eclipse.xpect.registry;

import java.util.Collection;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class DelegatingLanguageRegistry implements ILanguageInfo.Registry {

	private ILanguageInfo.Registry delegate;

	public ILanguageInfo.Registry getDelegate() {
		return delegate;
	}

	public ILanguageInfo getLanguageByFileExtension(String fileExtension) {
		return delegate.getLanguageByFileExtension(fileExtension);
	}

	public ILanguageInfo getLanguageByName(String languageName) {
		return delegate.getLanguageByName(languageName);
	}

	public Collection<ILanguageInfo> getLanguages() {
		return delegate.getLanguages();
	}

	public void setDelegate(ILanguageInfo.Registry delegate) {
		this.delegate = delegate;
	}

}
