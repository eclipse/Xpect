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

package org.eclipse.xpect;

import org.eclipse.xtext.conversion.IValueConverterService;
import org.eclipse.xtext.resource.IFragmentProvider;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.validation.CompositeEValidator;
import org.eclipse.xpect.services.NullResourceDescriptions;
import org.eclipse.xpect.services.XpectFragmentProvider;
import org.eclipse.xpect.services.XpectValueConverter;

import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectRuntimeModule extends AbstractXpectRuntimeModule {

	@Override
	public Class<? extends IFragmentProvider> bindIFragmentProvider() {
		return XpectFragmentProvider.class;
	}

	@Override
	public Class<? extends IValueConverterService> bindIValueConverterService() {
		return XpectValueConverter.class;
	}

	public void configureEObjectValidator(Binder binder) {
		binder.bind(Boolean.class).annotatedWith(Names.named(CompositeEValidator.USE_EOBJECT_VALIDATOR)).toInstance(Boolean.FALSE);
	}

	public void configureIResourceDescriptions(com.google.inject.Binder binder) {
		binder.bind(IResourceDescriptions.class).to(NullResourceDescriptions.class);
	}

	public void configureIResourceDescriptionsBuilderScope(com.google.inject.Binder binder) {
		binder.bind(IResourceDescriptions.class).annotatedWith(Names.named(ResourceDescriptionsProvider.NAMED_BUILDER_SCOPE)).to(NullResourceDescriptions.class);
	}

}
