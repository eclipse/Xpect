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

package org.xpect.ui;

import org.eclipse.xtext.ui.LanguageSpecific;
import org.eclipse.xtext.ui.editor.IURIEditorOpener;
import org.eclipse.xtext.ui.editor.autoedit.MultiLineTerminalsEditStrategy;
import org.eclipse.xtext.ui.editor.contentassist.IContentProposalProvider;
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkHelper;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionProvider;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.eclipse.xtext.ui.validation.MarkerTypeProvider;
import org.eclipse.xtext.validation.IResourceValidator;
import org.xpect.registry.AbstractDelegatingModule;
import org.xpect.ui.contentassist.XtProposalProvider;
import org.xpect.ui.highlighting.XtHighlightingCalculator;
import org.xpect.ui.highlighting.XtHighlightingConfiguration;
import org.xpect.ui.quickfix.XtIssueResolutionProvider;
import org.xpect.ui.services.XpectMultiLineTerminalsEditStrategyFactory;
import org.xpect.ui.services.XtHyperlinkHelper;
import org.xpect.ui.services.XtLanguageSpecificURIEditorOpener;
import org.xpect.ui.services.XtMarkerTypeProvider;
import org.xpect.ui.services.XtResourceValidator;

import com.google.inject.Binder;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XtWorkbenchUIModule extends AbstractDelegatingModule {

	public void configure(Binder binder) {
		overrideAndBackup(binder, IHighlightingConfiguration.class, XtHighlightingConfiguration.class);
		overrideAndBackup(binder, ISemanticHighlightingCalculator.class, XtHighlightingCalculator.class);
		overrideAndBackup(binder, IResourceValidator.class, XtResourceValidator.class);
		overrideAndBackup(binder, IURIEditorOpener.class, LanguageSpecific.class, XtLanguageSpecificURIEditorOpener.class);
		overrideAndBackup(binder, IContentProposalProvider.class, XtProposalProvider.class);
		overrideAndBackup(binder, MultiLineTerminalsEditStrategy.Factory.class, XpectMultiLineTerminalsEditStrategyFactory.class);
		overrideAndBackup(binder, IHyperlinkHelper.class, XtHyperlinkHelper.class);
		overrideAndBackup(binder, MarkerTypeProvider.class, XtMarkerTypeProvider.class);
		overrideAndBackup(binder, IssueResolutionProvider.class, XtIssueResolutionProvider.class);
	}

}
