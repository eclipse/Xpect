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

package org.eclipse.xpect.xtext.lib.setup.emf;

import java.util.List;

import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.setup.ISetupInitializer;
import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.setup.XpectSetupRoot;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.xtext.lib.setup.generic.Resource;

import com.google.common.collect.Lists;

@XpectSetupRoot
@XpectSetupFactory
@SuppressWarnings("deprecation")
@XpectImport({ org.eclipse.xpect.xtext.lib.setup.emf.Resource.class, ResourceSet.class, ThisResource.class, File.class, ThisFile.class })
public class ResourceSetDefaultsSetup {

	private List<Resource> genericResources = Lists.newArrayList();

	private ResourceSet resourceSet = null;

	public ResourceSetDefaultsSetup(ISetupInitializer<ResourceSetDefaultsSetup> initializer) {
		super();
		initialize(initializer);
	}

	public void add(Resource genericResource) {
		this.genericResources.add(genericResource);
	}

	public void add(ResourceSet resourceSet) {
		if (this.resourceSet != null)
			throw new IllegalStateException("Only one ResourceSet can be configured");
		this.resourceSet = resourceSet;
	}

	protected void addThisFileIfNeeded() {
		for (ResourceFactory fact : this.resourceSet.getFactories())
			if (fact instanceof ThisResource)
				return;
		this.resourceSet.add(new ThisResource());
	}

	protected void convert(Resource res) {
		if (res instanceof org.eclipse.xpect.xtext.lib.setup.generic.ThisFile)
			this.resourceSet.add(new org.eclipse.xpect.xtext.lib.setup.emf.ThisResource((org.eclipse.xpect.xtext.lib.setup.generic.ThisFile) res));
		else if (res instanceof org.eclipse.xpect.xtext.lib.setup.generic.File)
			this.resourceSet.add(new org.eclipse.xpect.xtext.lib.setup.emf.Resource((org.eclipse.xpect.xtext.lib.setup.generic.File) res));
		else if (res instanceof org.eclipse.xpect.xtext.lib.setup.generic.Folder)
			for (Resource child : ((org.eclipse.xpect.xtext.lib.setup.generic.Folder) res).getChildren())
				convert(child);
		else
			throw new IllegalStateException();
	}

	protected ResourceSet createResourceSet() {
		return new ResourceSet();
	}

	@Creates
	public ResourceSet getResourceSet() {
		return this.resourceSet;
	}

	protected void initialize(ISetupInitializer<ResourceSetDefaultsSetup> initializer) {
		initializer.initialize(this);
		if (resourceSet == null)
			resourceSet = createResourceSet();
		for (Resource res : genericResources)
			convert(res);
		addThisFileIfNeeded();
	}

}