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

package org.eclipse.xpect.ui.services;

import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkHelper;
import org.eclipse.xpect.registry.DefaultBinding;
import org.eclipse.xpect.ui.util.XpectFileAccess;

import com.google.inject.Inject;

public class XtHyperlinkHelper implements IHyperlinkHelper {

	@Inject
	@DefaultBinding
	private IHyperlinkHelper originalHelper;

	public IHyperlink[] createHyperlinksByOffset(XtextResource resource, int offset, boolean createMultipleHyperlinks) {
		XtextResource xpectResource = XpectFileAccess.getXpectResource(resource);
		IHyperlinkHelper hyperlinkHelper = xpectResource.getResourceServiceProvider().get(IHyperlinkHelper.class);
		IHyperlink[] xpectLinks = hyperlinkHelper.createHyperlinksByOffset(xpectResource, offset, createMultipleHyperlinks);
		IHyperlink[] originalLinks = originalHelper.createHyperlinksByOffset(resource, offset, createMultipleHyperlinks);

		if (xpectLinks != null && originalLinks != null) {
			IHyperlink[] result = new IHyperlink[xpectLinks.length + originalLinks.length];
			System.arraycopy(xpectLinks, 0, result, 0, xpectLinks.length);
			System.arraycopy(originalLinks, 0, result, xpectLinks.length, originalLinks.length);
			return result;
		} else if (xpectLinks != null) {
			return xpectLinks;
		}
		return originalLinks;
	}

}
