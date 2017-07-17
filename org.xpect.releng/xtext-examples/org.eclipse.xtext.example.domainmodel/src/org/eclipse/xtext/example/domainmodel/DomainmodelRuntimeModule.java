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
}
