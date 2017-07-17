package org.eclipse.xtext.example.arithmetics.ui.autoedit

import org.eclipse.jface.text.IDocument
import org.eclipse.xtext.ui.editor.autoedit.DefaultAutoEditStrategyProvider

class AutoEditStrategy extends DefaultAutoEditStrategyProvider {
	override protected void configure(IEditStrategyAcceptor acceptor) {
		super.configure(acceptor)
		acceptor.accept(new InterpreterAutoEdit(), IDocument.DEFAULT_CONTENT_TYPE)
	}
}
