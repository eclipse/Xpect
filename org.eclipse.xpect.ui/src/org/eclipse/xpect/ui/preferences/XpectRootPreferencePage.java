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

package org.eclipse.xpect.ui.preferences;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreAccess;
import org.eclipse.xtext.ui.editor.preferences.LanguageRootPreferencePage;
import org.eclipse.xpect.ui.internal.XpectActivator;

public class XpectRootPreferencePage extends LanguageRootPreferencePage {

	public static final String LIVE_TEST_EXECUTION_PREFERENCE_NAME = "org.eclipse.xpect.ui.live_test_execution";
	
	public static final String INCLUDE_CONTENT_CHECK_PREFERENCE_NAME = "org.eclipse.xpect.ui.include_extension_content_check";

	public static final String DISABLE_EDITOR_OVERRIDE_PREFERENCE_NAME = "org.eclipse.xpect.ui.editor_override";
	
	@Override
	protected void createFieldEditors() {
		super.createFieldEditors();
		Composite parent = getFieldEditorParent();
		if (!isPropertyPage()) {
			Group group = new Group(parent, SWT.NONE);
			group.setText("Xpect Editor Override");
			group.setToolTipText("Files which contain the text \"XPECT\" are usually forced to open with the Xpect editor, this can be disabled or adjusted with these settings");
			GridLayoutFactory.fillDefaults().margins(5, 5).applyTo(group);
			GridDataFactory.fillDefaults().span(2, 1).grab(true, false).applyTo(group);
			
			Composite skipFileExtListComposite = new Composite(group, SWT.NONE);
			GridLayoutFactory.fillDefaults().margins(5, 5).applyTo(skipFileExtListComposite);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(skipFileExtListComposite);
			addField(new SkipFileExtensionList(INCLUDE_CONTENT_CHECK_PREFERENCE_NAME, "Include following file extensions from Xpect editor override (includes all if nothing is specified):", skipFileExtListComposite));
			
			Composite disableOverrideComposite = new Composite(group, SWT.NONE);
			GridLayoutFactory.fillDefaults().margins(5, 5).applyTo(disableOverrideComposite);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(disableOverrideComposite);
			addField(new BooleanFieldEditor(DISABLE_EDITOR_OVERRIDE_PREFERENCE_NAME, "Disable editor override", SWT.NONE, disableOverrideComposite));
		}
		addField(new BooleanFieldEditor(LIVE_TEST_EXECUTION_PREFERENCE_NAME, "Run tests live in editor, if possible", SWT.NONE, parent));
	}

	public static boolean isLiveTestExecutionEnabled(IProject project) {
		IPreferenceStoreAccess preferenceStore = XpectActivator.getInstance().getInjector(XpectActivator.ORG_ECLIPSE_XPECT_XPECT).getInstance(IPreferenceStoreAccess.class);
		boolean enabled = preferenceStore.getContextPreferenceStore(project).getBoolean(XpectRootPreferencePage.LIVE_TEST_EXECUTION_PREFERENCE_NAME);
		return enabled;
	}
	
	public static List<String> getIncludedContentCheckExtensions() {
		IPreferenceStoreAccess preferenceStore = XpectActivator.getInstance().getInjector(XpectActivator.ORG_ECLIPSE_XPECT_XPECT).getInstance(IPreferenceStoreAccess.class);
		String extensions = preferenceStore.getWritablePreferenceStore().getString(INCLUDE_CONTENT_CHECK_PREFERENCE_NAME);
		if (extensions.trim().length() == 0) {
			return Collections.emptyList();
		}
		return Arrays.asList(extensions.split(";"));
	}

	
	public static boolean isEditorOverrideDisabled() {
		IPreferenceStoreAccess preferenceStore = XpectActivator.getInstance().getInjector(XpectActivator.ORG_ECLIPSE_XPECT_XPECT).getInstance(IPreferenceStoreAccess.class);
		return preferenceStore.getWritablePreferenceStore().getBoolean(DISABLE_EDITOR_OVERRIDE_PREFERENCE_NAME);
	}
	
}