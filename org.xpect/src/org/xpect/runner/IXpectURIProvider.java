package org.xpect.runner;

import java.util.Collection;

import org.eclipse.emf.common.util.URI;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public interface IXpectURIProvider {

	Collection<URI> getAllURIs();

	URI resolveURI(URI base, String newURI);

	URI deresolveToProject(URI uri);
}
