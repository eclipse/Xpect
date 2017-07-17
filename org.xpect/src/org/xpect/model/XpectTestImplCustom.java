package org.xpect.model;

import org.eclipse.xtext.common.types.JvmDeclaredType;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectTestImplCustom extends XpectTestImpl {
	@Override
	public void setDeclaredSuite(JvmDeclaredType newDeclaredSuite) {
		XpectFileImplCustom file = (XpectFileImplCustom) getFile();
		file.unsetJavaModel();
		super.setDeclaredSuite(newDeclaredSuite);
	}
}
