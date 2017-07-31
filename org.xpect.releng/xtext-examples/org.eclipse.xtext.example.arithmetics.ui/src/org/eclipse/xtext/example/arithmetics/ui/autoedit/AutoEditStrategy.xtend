/*******************************************************************************
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
