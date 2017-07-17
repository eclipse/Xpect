package org.xpect.xtext.lib.setup.emf;

import java.util.List;

import org.xpect.XpectImport;
import org.xpect.setup.XpectSetupComponent;

import com.google.common.collect.Lists;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectSetupComponent
@XpectImport(ResourceSetDefaultsSetup.class)
public class ResourceSet {
	private final List<ResourceFactory> factories = Lists.newArrayList();

	public void add(ResourceFactory file) {
		factories.add(file);
	}

	public List<ResourceFactory> getFactories() {
		return factories;
	}
}
