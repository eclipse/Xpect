/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.example.domainmodel;

import org.eclipse.xtext.resource.persistence.IResourceStorageFacade;
import org.eclipse.xtext.xbase.resource.BatchLinkableResourceStorageFacade;

/**
 * used to register components to be used within the IDE.
 */
public class DomainmodelRuntimeModule extends AbstractDomainmodelRuntimeModule {

	public Class<? extends IResourceStorageFacade> bindResourceStorageFacade() {
		return BatchLinkableResourceStorageFacade.class;
	}

	// contributed by org.eclipse.xtext.generator.formatting2.Formatter2Fragment
	public Class<? extends org.eclipse.xtext.formatting2.IFormatter2> bindIFormatter2() {
		return org.eclipse.xtext.example.domainmodel.formatting2.DomainmodelFormatter.class;
	}

	// contributed by org.eclipse.xtext.generator.formatting2.Formatter2Fragment
	public void configureFormatterPreferences(com.google.inject.Binder binder) {
		binder.bind(org.eclipse.xtext.preferences.IPreferenceValuesProvider.class)
				.annotatedWith(org.eclipse.xtext.formatting2.FormatterPreferences.class)
				.to(org.eclipse.xtext.formatting2.FormatterPreferenceValuesProvider.class);
	}
	
	// contributed by org.eclipse.xtext.generator.xbase.XbaseGeneratorFragment
	public Class<? extends org.eclipse.xtext.xbase.jvmmodel.IJvmModelInferrer> bindIJvmModelInferrer() {
		return org.eclipse.xtext.example.domainmodel.jvmmodel.DomainmodelJvmModelInferrer.class;
	}
}
