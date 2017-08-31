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

package org.eclipse.xpect.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreAccess;
import org.eclipse.xtext.ui.editor.preferences.LanguageRootPreferencePage;
import org.eclipse.xpect.ui.internal.XpectActivator;

public class XpectRootPreferencePage extends LanguageRootPreferencePage {

	public static final String LIVE_TEST_EXECUTION_PREFERENCE_NAME = "org.eclipse.xpect.ui.live_test_execution";

	@Override
	protected void createFieldEditors() {
		super.createFieldEditors();
		Composite parent = getFieldEditorParent();
		addField(new BooleanFieldEditor(LIVE_TEST_EXECUTION_PREFERENCE_NAME, "Run tests live in editor, if possible", SWT.NONE, parent));
	}

	public static boolean isLiveTestExecutionEnabled(IProject project) {
		IPreferenceStoreAccess preferenceStore = XpectActivator.getInstance().getInjector(XpectActivator.ORG_ECLIPSE_XPECT_XPECT).getInstance(IPreferenceStoreAccess.class);
		boolean enabled = preferenceStore.getContextPreferenceStore(project).getBoolean(XpectRootPreferencePage.LIVE_TEST_EXECUTION_PREFERENCE_NAME);
		return enabled;
	}

}