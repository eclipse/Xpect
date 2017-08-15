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

package org.eclipse.xpect.ui.contentassist;

import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.ui.editor.contentassist.IContentProposalProvider;
import org.eclipse.xtext.ui.editor.contentassist.RepeatedContentAssistProcessor.ModeAware;
import org.eclipse.xpect.registry.DefaultBinding;
import org.eclipse.xpect.services.XpectGrammarAccess;
import org.eclipse.xpect.ui.internal.XpectActivator;
import org.eclipse.xpect.ui.util.XpectFileAccess;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class XtProposalProvider implements IContentProposalProvider, ModeAware {

	private final Injector xpectInjector = XpectActivator.getInstance().getInjector(XpectActivator.ORG_ECLIPSE_XPECT_XPECT);

	@Inject
	@DefaultBinding
	private IContentProposalProvider dslDelegate;

	private IContentProposalProvider xpectDelegate = xpectInjector.getInstance(IContentProposalProvider.class);

	private ContentAssistContext.Factory xpectContextFactory = xpectInjector.getInstance(ContentAssistContext.Factory.class);

	private XpectGrammarAccess xpectGrammarAccess = xpectInjector.getInstance(XpectGrammarAccess.class);

	public void createProposals(ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		if (showXpectProposals(context)) {
			XtextResource xpectResource = XpectFileAccess.getXpectResource(context.getResource());
			ContentAssistContext[] contexts = xpectContextFactory.create(context.getViewer(), context.getOffset(), xpectResource);
			boolean showDslProposals = false;
			for (ContentAssistContext xpectContext : contexts)
				if (acceptor.canAcceptMoreProposals()) {
					if (isXpectText(xpectContext))
						showDslProposals = true;
					xpectDelegate.createProposals(xpectContext, acceptor);
				}
			if (showDslProposals && acceptor.canAcceptMoreProposals())
				dslDelegate.createProposals(context, acceptor);
		} else {
			dslDelegate.createProposals(context, acceptor);
		}
	}

	protected boolean isXpectText(ContentAssistContext context) {
		INode node = context.getCurrentNode();
		if (node == null || !(node.getGrammarElement() instanceof RuleCall))
			return false;
		RuleCall rc = (RuleCall) node.getGrammarElement();
		return rc.getRule() == xpectGrammarAccess.getANY_OTHERRule();
	}

	protected boolean showXpectProposals(ContentAssistContext context) {
		if (!(context.getCurrentNode() instanceof ILeafNode))
			return false;
		ILeafNode node = (ILeafNode) context.getCurrentNode();
		if (!node.isHidden())
			return false;
		String text = node.getText();
		for (int i = 0; i < text.length(); i++)
			if (!Character.isWhitespace(text.charAt(i)))
				return true;
		return false;
	}

	public String getNextCategory() {
		if (dslDelegate instanceof ModeAware)
			return ((ModeAware) dslDelegate).getNextCategory();
		return "";
	}

	public boolean isLastMode() {
		if (dslDelegate instanceof ModeAware)
			return ((ModeAware) dslDelegate).isLastMode();
		return true;
	}

	public void nextMode() {
		if (dslDelegate instanceof ModeAware)
			((ModeAware) dslDelegate).nextMode();
	}

	public void reset() {
		if (dslDelegate instanceof ModeAware)
			((ModeAware) dslDelegate).reset();
	}

}
