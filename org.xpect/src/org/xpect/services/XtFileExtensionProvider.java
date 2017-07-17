package org.xpect.services;

import org.eclipse.xtext.resource.FileExtensionProvider;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XtFileExtensionProvider extends FileExtensionProvider {

	@Override
	public boolean isValid(String fileExtension) {
		if ("xt".equals(fileExtension))
			return true;
		return super.isValid(fileExtension);
	}

}
