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

package org.eclipse.xpect.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.xpect.Component;
import org.eclipse.xpect.Environment;
import org.eclipse.xpect.XpectConstants;
import org.eclipse.xpect.XpectPackage;
import org.eclipse.xpect.XpectTest;
import org.eclipse.xpect.registry.ILanguageInfo;
import org.eclipse.xpect.setup.XpectSetupComponent;
import org.eclipse.xpect.util.EnvironmentUtil;
import org.eclipse.xpect.util.JvmAnnotationUtil;
import org.eclipse.xpect.util.URIDelegationHandler;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.validation.AbstractDeclarativeValidator;
import org.eclipse.xtext.validation.Check;

import com.google.inject.Inject;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@SuppressWarnings("restriction")
public class XpectJavaValidator extends AbstractDeclarativeValidator {

	@Inject
	private URIDelegationHandler uriDelegationHandler;

	@Inject
	private TypeReferences typeReferences;

	@Override
	protected List<EPackage> getEPackages() {
	    List<EPackage> result = new ArrayList<EPackage>();
	    result.add(EPackage.Registry.INSTANCE.getEPackage("http://www.eclipse.org/xpect/Xpect"));
		return result;
	}

	@Check
	public void validateLanguageModulesAreOnClasspath(XpectTest test) {
		if (EnvironmentUtil.ENVIRONMENT == Environment.WORKBENCH) {
			return;
		}
		String extension = uriDelegationHandler.getOriginalFileExtension(test.eResource().getURI().lastSegment());
		if (extension == null || XpectConstants.XPECT_FILE_EXT.equals(extension)) {
			return;
		}
		ILanguageInfo languageInfo = ILanguageInfo.Registry.INSTANCE.getLanguageByFileExtension(extension);
		validateClassIsOnClasspath(languageInfo.getRuntimeModuleClass(), test);
		if (EcorePlugin.IS_ECLIPSE_RUNNING) {
			validateClassIsOnClasspath(languageInfo.getUIModuleClass(), test);
		}
	}

	protected void validateClassIsOnClasspath(Class<?> cls, Notifier ctx) {
		if (typeReferences.findDeclaredType(cls, ctx) == null) {
			error("The class " + cls.getName() + " is not on the classpath", XpectPackage.Literals.XPECT_TEST__DECLARED_SUITE);
		}
	}

	@Check
	public void checkComponent(Component component) {
		JvmDeclaredType type = component.getComponentClass();
		if (type == null || type.eIsProxy())
			return;
		XpectSetupComponent annotation = JvmAnnotationUtil.getJavaAnnotation(type, XpectSetupComponent.class);
		if (annotation == null) {
			String message = "The class " + type.getQualifiedName() + " must be annotated with @" + XpectSetupComponent.class.getSimpleName();
			warning(message, XpectPackage.Literals.COMPONENT__COMPONENT_CLASS);
		}
	}

}
