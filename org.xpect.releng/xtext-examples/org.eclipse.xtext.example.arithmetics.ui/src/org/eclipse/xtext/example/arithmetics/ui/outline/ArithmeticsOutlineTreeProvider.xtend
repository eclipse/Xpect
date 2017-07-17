package org.eclipse.xtext.example.arithmetics.ui.outline

import org.eclipse.xtext.example.arithmetics.arithmetics.Definition
import org.eclipse.xtext.example.arithmetics.arithmetics.Module
import org.eclipse.xtext.ui.editor.outline.IOutlineNode
import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider

/**
 * Customization of the default outline structure.
 *
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#outline
 */
class ArithmeticsOutlineTreeProvider extends DefaultOutlineTreeProvider {
	def _createChildren(IOutlineNode parentNode, Module module) {
		module.eContents().filter(Definition).forEach [
			createNode(parentNode, it);
		]
	}

	def _isLeaf(Definition definition) {
		true
	}
	
}
