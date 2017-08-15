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

package org.eclipse.xpect.xtext.lib.tests;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IReferenceDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.XtextResource;
import org.junit.runner.RunWith;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.expectation.ILinesExpectation;
import org.eclipse.xpect.expectation.LinesExpectation;
import org.eclipse.xpect.runner.LiveExecutionType;
import org.eclipse.xpect.runner.Xpect;
import org.eclipse.xpect.runner.XpectRunner;
import org.eclipse.xpect.xtext.lib.setup.ThisResource;
import org.eclipse.xpect.xtext.lib.setup.XtextStandaloneSetup;
import org.eclipse.xpect.xtext.lib.setup.XtextWorkspaceSetup;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@RunWith(XpectRunner.class)
@XpectImport({ XtextStandaloneSetup.class, XtextWorkspaceSetup.class })
public class ResourceDescriptionTest {

	protected static class EObjectDescriptionToStringMapper implements Function<IEObjectDescription, String> {
		public String apply(IEObjectDescription desc) {
			return desc.getEClass().getEPackage().getNsPrefix() + "::" + desc.getEClass().getName() + ": " + desc.getName().toString();
		}
	}

	protected static class ReferenceDescriptionToStringMapper implements Function<IReferenceDescription, String> {

		private final URI base;

		public URI getBase() {
			return base;
		}

		public ReferenceDescriptionToStringMapper(URI base) {
			super();
			this.base = base.trimFragment();
		}

		protected String deresolve(URI uri) {
			if (base.equals(uri.trimFragment()))
				return uri.fragment();
			return uri.deresolve(base).toString();
		}

		public String apply(IReferenceDescription desc) {
			StringBuilder result = new StringBuilder();
			String container = deresolve(desc.getContainerEObjectURI());
			String source = deresolve(desc.getSourceEObjectUri());
			if (source.startsWith(container)) {
				result.append(container);
				result.append("::");
				result.append(source.substring(container.length()));
			} else {
				result.append(container);
				result.append(" <> ");
				result.append(source);
			}
			result.append(" ");
			result.append(desc.getEReference().getName());
			if (desc.getIndexInList() > 0) {
				result.append("[");
				result.append(desc.getIndexInList());
				result.append("]");
			}
			result.append(" --> ");
			result.append(deresolve(desc.getTargetEObjectUri()));
			return result.toString();
		}
	}

	@Inject
	private IResourceDescription.Manager manager;

	@Xpect(liveExecution = LiveExecutionType.FAST)
	public void exportedObjects(@LinesExpectation ILinesExpectation expectation, @ThisResource XtextResource resource) {
		IResourceDescription resourceDescription = manager.getResourceDescription(resource);
		Iterable<IEObjectDescription> exportedObjects = resourceDescription.getExportedObjects();
		expectation.assertEquals(Iterables.transform(exportedObjects, new EObjectDescriptionToStringMapper()));
	}

	@Xpect(liveExecution = LiveExecutionType.FAST)
	public void importedNames(@LinesExpectation ILinesExpectation expectation, @ThisResource XtextResource resource) {
		IResourceDescription resourceDescription = manager.getResourceDescription(resource);
		Iterable<QualifiedName> importedNames = resourceDescription.getImportedNames();
		expectation.assertEquals(importedNames);
	}

	@Xpect(liveExecution = LiveExecutionType.FAST)
	public void referenceDescriptions(@LinesExpectation ILinesExpectation expectation, @ThisResource XtextResource resource) {
		IResourceDescription resourceDescription = manager.getResourceDescription(resource);
		Iterable<IReferenceDescription> referenceDescriptions = resourceDescription.getReferenceDescriptions();
		expectation.assertEquals(Iterables.transform(referenceDescriptions, new ReferenceDescriptionToStringMapper(resource.getURI())));
	}

}
