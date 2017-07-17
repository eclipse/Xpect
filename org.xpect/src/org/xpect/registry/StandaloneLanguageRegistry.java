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

package org.xpect.registry;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.util.Modules2;
import org.xpect.XpectConstants;
import org.xpect.registry.IEmfFileExtensionInfo.IXtextFileExtensionInfo;
import org.xpect.services.XtResourceServiceProviderProvider;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class StandaloneLanguageRegistry implements ILanguageInfo.Registry {

	protected static class StandaloneLanguageInfoImpl extends AbstractLanguageInfo {
		public StandaloneLanguageInfoImpl(IXtextFileExtensionInfo info) {
			super(info);
		}

		protected Injector createInjector(Module... modules) {
			try {
				if (modules.length > 0)
					return Guice.createInjector(Modules2.mixin(getRuntimeModule(), Modules2.mixin(modules)));
				else
					return Guice.createInjector(getRuntimeModule());
			} catch (Throwable t) {
				List<String> moduleNames = Lists.newArrayList(getRuntimeModule().getClass().getName());
				for (Module m : modules)
					moduleNames.add(m.getClass().getName());
				throw new RuntimeException("Error creating Injector with modules " + Joiner.on(", ").join(moduleNames), t);
			}
		}

	}

	private static final Logger LOG = Logger.getLogger(StandaloneLanguageRegistry.class);

	private static boolean running = false;

	private Map<String, ILanguageInfo> ext2language;

	private Map<String, ILanguageInfo> name2language;

	public StandaloneLanguageRegistry() {
		try {
			init();
		} catch (Throwable e) {
			LOG.error("Error initalizing language registry", e);
		}
	}

	public ILanguageInfo getLanguageByFileExtension(String fileExtension) {
		return ext2language.get(fileExtension);
	}

	public ILanguageInfo getLanguageByName(String name) {
		return name2language.get(name);
	}

	public Collection<ILanguageInfo> getLanguages() {
		return name2language.values();
	}

	protected void init() {
		ext2language = Maps.newHashMap();
		name2language = Maps.newHashMap();
		if (EcorePlugin.IS_ECLIPSE_RUNNING)
			throw new IllegalStateException("This class can only run in standalone mode (no OSGi, no Eclipse)");
		if (running)
			throw new IllegalStateException("I want to be a singleton!");
		running = true;

		for (String nsURI : IEPackageInfo.Registry.INSTANCE.getNamespaceURIs())
			EPackageRegistrar.register(IEPackageInfo.Registry.INSTANCE.getEPackageInfo(nsURI));

		for (IEmfFileExtensionInfo info : IEmfFileExtensionInfo.Registry.INSTANCE.getFileExtensionInfos()) {
			FileExtensionRegistrar.register(info);
			if (info instanceof IXtextFileExtensionInfo) {
				StandaloneLanguageInfoImpl infoImpl = new StandaloneLanguageInfoImpl((IXtextFileExtensionInfo) info);
				name2language.put(infoImpl.getLanguageName(), infoImpl);
				for (String ext : info.getFileExtensions())
					ext2language.put(ext, infoImpl);
			}
		}
		registerRSPProviderForXt();
	}

	protected void registerRSPProviderForXt() {
		if (IResourceServiceProvider.Registry.INSTANCE.getExtensionToFactoryMap().get(XpectConstants.XT_FILE_EXT) == null) {
			IResourceServiceProvider.Registry.INSTANCE.getExtensionToFactoryMap().put(XpectConstants.XT_FILE_EXT, XtResourceServiceProviderProvider.INSTANCE);
		}
	}

}
