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

package org.eclipse.xtext.example.arithmetics.ui.autoedit

import org.eclipse.jface.text.IDocument
import org.eclipse.xtext.ui.editor.autoedit.DefaultAutoEditStrategyProvider

class AutoEditStrategy extends DefaultAutoEditStrategyProvider {
	override protected void configure(IEditStrategyAcceptor acceptor) {
		super.configure(acceptor)
		acceptor.accept(new InterpreterAutoEdit(), IDocument.DEFAULT_CONTENT_TYPE)
	}
}
