package org.eclipse.xpect.ui.preferences;

import java.util.Arrays;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.xtext.util.Strings;

public class SkipFileExtensionList extends ListEditor {
	SkipFileExtensionList(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected String[] parseString(String stringList) {
		if (stringList.isEmpty()) {
			return new String[0];
		}
		return stringList.split(";");
	}

	@Override
	protected String getNewInputObject() {
		InputDialog dialog = new InputDialog(getShell(), "Please enter an extension", "Please enter an extension, you want to skip for XPECT content check.", "",
				new IInputValidator() {

					@Override
					public String isValid(String newText) {
						String[] items = getList().getItems();
						for (String existingExtension : items) {
							if (newText.equals(existingExtension)) {
								return "Extension is already skipped";
							}
						}
						return null;
					}
				});
		if (Window.OK != dialog.open()) {
			return null;
		}
		var newExtension = dialog.getValue();
		if (newExtension != null) {
			newExtension = newExtension.trim();
			if (newExtension.isEmpty()) {
				return null;
			}
		}
		return newExtension;
	}

	@Override
	protected String createList(String[] items) {
		if (items == null || items.length == 0)
			return "";
		return Strings.concat(";", Arrays.asList(items));
	}
}