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

import java.lang.reflect.Field;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

public class EPackageRegistrar {

	protected static class EPackageDescriptorImpl implements EPackage.Descriptor {
		private final IEPackageInfo info;
		private EPackage pkg = null;

		public EPackageDescriptorImpl(IEPackageInfo info) {
			super();
			this.info = info;
		}

		public EFactory getEFactory() {
			if (pkg != null)
				return pkg.getEFactoryInstance();
			return null;
		}

		public EPackage getEPackage() {
			if (pkg == null)
				try {
					Class<EPackage> clazz = info.getEPackage().load();
					Field field = clazz.getField("eINSTANCE");
					pkg = (EPackage) field.get(null);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (SecurityException e) {
					throw new RuntimeException(e);
				} catch (NoSuchFieldException e) {
					throw new RuntimeException(e);
				}
			return pkg;
		}

		public IEPackageInfo getInfo() {
			return info;
		}

	}

	public static void register(IEPackageInfo info) {
		EPackage.Registry.INSTANCE.put(info.getNamespaceURI(), new EPackageDescriptorImpl(info));
		if (info.getGenmodel() != null)
			EcorePlugin.getEPackageNsURIToGenModelLocationMap().put(info.getNamespaceURI(), URI.createURI(info.getGenmodel()));
	}

}
