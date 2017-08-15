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

package org.eclipse.xpect.ui.junit.registry;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.pde.core.IModelChangeProvider;
import org.eclipse.pde.core.IModelChangedEvent;
import org.eclipse.pde.core.IModelChangedListener;
import org.eclipse.pde.core.plugin.IPluginAttribute;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.xpect.registry.ITestSuiteInfo;
import org.eclipse.xpect.registry.LazyClass;
import org.eclipse.xpect.registry.TestSuiteInfoRegistry.TestSuiteInfo;
import org.eclipse.xpect.util.URIDelegationHandler;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class WorkspaceTestSuiteInfoRegistry implements ITestSuiteInfo.Registry, IModelChangedListener {

	private Map<String, ITestSuiteInfo> extToInfo = null;
	private List<IModelChangeProvider> listentingTo = Lists.newArrayList();

	protected void collectSuites(IPluginModelBase model, Set<String> visited) {
		if (!model.isEnabled())
			return;
		BundleDescription description = model.getBundleDescription();
		if (!visited.add(description.getSymbolicName()))
			return;
		model.addModelChangedListener(this);
		listentingTo.add(model);
		for (IPluginExtension ext : model.getExtensions(true).getExtensions())
			if ("org.eclipse.xpect.testSuite".equals(ext.getPoint())) {
				for (IPluginObject child : ext.getChildren()) {
					if (child instanceof IPluginElement) {
						IPluginElement pluginElement = (IPluginElement) child;
						IPluginAttribute clazz = pluginElement.getAttribute("class");
						IPluginAttribute fileExtension = pluginElement.getAttribute("fileExtension");
						if (clazz != null && fileExtension != null) {
							LazyClass<Object> lazyClass = LazyClass.create(Object.class, clazz.getValue(), (Function<String, Class<?>>) null);
							TestSuiteInfo info = new TestSuiteInfo(lazyClass, Collections.singleton(fileExtension.getValue()));
							this.extToInfo.put(fileExtension.getValue(), info);
						}
					}
				}
			}
		for (BundleDescription desc : description.getDependents()) {
			collectSuites(PluginRegistry.findModel(desc), visited);
		}
	}

	protected void collectSuites() {
		if (this.extToInfo == null) {
			for (IModelChangeProvider provider : listentingTo)
				provider.removeModelChangedListener(this);
			listentingTo.clear();
			this.extToInfo = Maps.newHashMap();
			IPluginModelBase xpect = PluginRegistry.findModel("org.eclipse.xpect");
			collectSuites(xpect, Sets.<String> newHashSet());
		}
	}

	public ITestSuiteInfo getTestSuite(Resource resource) {
		collectSuites();
		String fileExtension = new URIDelegationHandler().getOriginalFileExtension(resource.getURI().lastSegment());
		return this.extToInfo.get(fileExtension);
	}

	public void modelChanged(IModelChangedEvent event) {
		this.extToInfo = null;
	}

}
