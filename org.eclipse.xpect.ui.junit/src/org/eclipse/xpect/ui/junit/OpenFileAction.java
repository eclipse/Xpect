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

package org.eclipse.xpect.ui.junit;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.Action;
import org.eclipse.xtext.ui.editor.IURIEditorOpener;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 * 
 * @see org.eclipse.ui.actions.OpenFileAction
 */
public class OpenFileAction extends Action {

	private IURIEditorOpener editorOpener;

	private URI uri;

	public OpenFileAction(IURIEditorOpener editorOpener, URI uri, String text, String toolTipText) {
		super();
		this.uri = uri;
		this.editorOpener = editorOpener;
		setText(text);
		setToolTipText(toolTipText);
	}

	public IURIEditorOpener getEditorOpener() {
		return editorOpener;
	}

	public URI getUri() {
		return uri;
	}

	@Override
	public void run() {
		editorOpener.open(uri, true);
	}

}
