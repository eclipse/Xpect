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

package org.eclipse.xpect.xtext.lib.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.state.XpectStateAnnotation;
import org.eclipse.xpect.xtext.lib.setup.ThisModel.ThisModelFactory;

import com.google.common.base.Joiner;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectStateAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@XpectImport(ThisModelFactory.class)
public @interface ThisModel {

	@XpectSetupFactory
	public class ThisModelFactory {
		private final XtextResource resource;

		public ThisModelFactory(@ThisResource XtextResource resource) {
			super();
			this.resource = resource;
		}

		@Creates(ThisModel.class)
		public EObject createThisModel() {
			EList<EObject> contents = resource.getContents();
			if (contents.isEmpty()) {
				StringBuilder error = new StringBuilder();
				error.append("Resource has no contents.\n");
				error.append("URI: " + this.resource.getURI() + "\n");
				if (!this.resource.getErrors().isEmpty()) {
					error.append("Errors:\n");
					Joiner.on("\n").appendTo(error, this.resource.getErrors());
				}
				throw new IllegalStateException(error.toString());
			}
			return contents.get(0);
		}
	}
}
