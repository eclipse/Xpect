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

package org.eclipse.xpect.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.common.types.xtext.AbstractTypeScopeProvider;
import org.eclipse.xtext.parser.antlr.ITokenDefProvider;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.ui.LexerUIBindings;
import org.eclipse.xtext.ui.editor.autoedit.MultiLineTerminalsEditStrategy;
import org.eclipse.xtext.ui.editor.preferences.LanguageRootPreferencePage;
import org.eclipse.xtext.ui.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper;
import org.eclipse.xpect.ui.highlighting.XpectTokenDefProvider;
import org.eclipse.xpect.ui.highlighting.XpectTokenToAttributeMapper;
import org.eclipse.xpect.ui.preferences.XpectRootPreferencePage;
import org.eclipse.xpect.ui.scoping.ClasspathOrJdtBasedTypeScopeProvider;
import org.eclipse.xpect.ui.scoping.ClasspathOrJdtBasedTypeScopeProviderFactory;
import org.eclipse.xpect.ui.services.XpectEObjectAtOffsetHelper;
import org.eclipse.xpect.ui.services.XpectMultiLineTerminalsEditStrategyFactory;

import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@SuppressWarnings("restriction")
public class XpectUiModule extends org.eclipse.xpect.ui.AbstractXpectUiModule {
	public XpectUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}

	@Override
	public void configureHighlightingTokenDefProvider(Binder binder) {
		binder.bind(ITokenDefProvider.class).annotatedWith(Names.named(LexerUIBindings.HIGHLIGHTING)).to(XpectTokenDefProvider.class);
	}

	public Class<? extends AbstractAntlrTokenToAttributeIdMapper> bindAbstractAntlrTokenToAttributeIdMapper() {
		return XpectTokenToAttributeMapper.class;
	}

	@Override
	public Class<? extends AbstractTypeScopeProvider> bindAbstractTypeScopeProvider() {
		return ClasspathOrJdtBasedTypeScopeProvider.class;
	}

	public Class<? extends IJvmTypeProvider.Factory> bindIJvmTypeProvider$Factory() {
		return ClasspathOrJdtBasedTypeScopeProviderFactory.class;
	}

	public Class<? extends MultiLineTerminalsEditStrategy.Factory> bindMultiLineTerminalsEditStrategyFactory() {
		return XpectMultiLineTerminalsEditStrategyFactory.class;
	}

	public Class<? extends EObjectAtOffsetHelper> bindEObjectAtOffsetHelper() {
		return XpectEObjectAtOffsetHelper.class;
	}

	public Class<? extends LanguageRootPreferencePage> bindLanguageRootPreferencePage() {
		return XpectRootPreferencePage.class;
	}

}
