/*******************************************************************************
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
