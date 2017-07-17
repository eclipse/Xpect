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

package org.eclipse.xtext.example.domainmodel.tests;

import org.eclipse.xtext.example.domainmodel.DomainmodelInjectorProvider;
import org.eclipse.xtext.example.domainmodel.DomainmodelRuntimeModule;
import org.eclipse.xtext.example.domainmodel.DomainmodelStandaloneSetup;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectorProviderCustom extends DomainmodelInjectorProvider {

	public Injector internalCreateInjector() {
			return new DomainmodelStandaloneSetup() {
				@Override
				public Injector createInjector() {
					return Guice.createInjector(new DomainmodelRuntimeModule() {
						@Override
						public ClassLoader bindClassLoaderToInstance() {
							return InjectorProviderCustom.class
									.getClassLoader();
						}

					});
				}
			}.createInjectorAndDoEMFRegistration();
	}

}
