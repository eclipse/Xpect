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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.parsetree.reconstr.impl.NodeIterator;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.XpectInvocation;
import org.eclipse.xpect.parameter.IStatementRelatedRegion;
import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.state.XpectStateAnnotation;
import org.eclipse.xpect.text.IRegion;
import org.eclipse.xpect.text.Region;
import org.eclipse.xpect.text.Text;
import org.eclipse.xpect.xtext.lib.setup.ThisResource;
import org.eclipse.xpect.xtext.lib.util.NextLine.NextLineProvider;

@Retention(RetentionPolicy.RUNTIME)
@XpectStateAnnotation
@XpectImport(NextLineProvider.class)
public @interface NextLine {

	@XpectSetupFactory
	public static class NextLineProvider {

		private final IRegion nextLine;

		public NextLineProvider(@ThisResource XtextResource resource, XpectInvocation statement) {
			ICompositeNode rootNode = resource.getParseResult().getRootNode();
			ILeafNode leaf = findNextNonHiddenLeaf(rootNode, statement);
			String document = rootNode.getText();
			Text text = new Text(document);
			int offset = leaf.getOffset();
			int start = text.currentLineStart(offset);
			int end = text.currentLineEnd(offset);
			this.nextLine = new Region(document, start, end - start);
		}

		private ILeafNode findNextNonHiddenLeaf(ICompositeNode rootNode, XpectInvocation statement) {
			IStatementRelatedRegion statementRegion = statement.getExtendedRegion();
			ILeafNode leaf = NodeModelUtils.findLeafNodeAtOffset(rootNode, statementRegion.getOffset() + statementRegion.getLength());
			NodeIterator it = new NodeIterator(leaf);
			while (it.hasNext()) {
				INode next = it.next();
				if (next instanceof ILeafNode && !((ILeafNode) next).isHidden())
					return (ILeafNode) next;
			}
			throw new IllegalStateException("No line with non-hidden tokens found after \n" + statementRegion.toString());
		}

		@Creates(NextLine.class)
		public IRegion getNextLine() {
			return nextLine;
		}

	}

}
