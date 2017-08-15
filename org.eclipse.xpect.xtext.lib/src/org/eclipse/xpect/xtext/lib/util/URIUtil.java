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

package org.eclipse.xpect.xtext.lib.util;

import org.eclipse.emf.common.util.URI;

import com.google.common.base.Preconditions;

public class URIUtil {
	public static URI createLocalURI(String name) {
		URI uri = URI.createURI(name);
		Preconditions.checkArgument(uri.isRelative());
		Preconditions.checkArgument(!uri.hasFragment());
		Preconditions.checkArgument(!uri.hasQuery());
		return uri;
	}
}
