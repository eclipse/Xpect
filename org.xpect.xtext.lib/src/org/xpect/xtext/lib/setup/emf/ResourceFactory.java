package org.xpect.xtext.lib.setup.emf;

import java.io.IOException;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.xpect.xtext.lib.setup.FileSetupContext;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public interface ResourceFactory {
	Resource create(FileSetupContext ctx, ResourceSet resourceSet) throws IOException;
}
