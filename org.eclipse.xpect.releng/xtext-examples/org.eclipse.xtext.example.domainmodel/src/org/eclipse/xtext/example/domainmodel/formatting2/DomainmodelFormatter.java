/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.xtext.example.domainmodel.formatting2;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.example.domainmodel.domainmodel.AbstractElement;
import org.eclipse.xtext.example.domainmodel.domainmodel.DomainModel;
import org.eclipse.xtext.example.domainmodel.domainmodel.DomainmodelPackage;
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity;
import org.eclipse.xtext.example.domainmodel.domainmodel.Feature;
import org.eclipse.xtext.example.domainmodel.domainmodel.Operation;
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration;
import org.eclipse.xtext.example.domainmodel.domainmodel.Property;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.eclipse.xtext.formatting2.IHiddenRegionFormatter;
import org.eclipse.xtext.formatting2.regionaccess.IEObjectRegion;
import org.eclipse.xtext.formatting2.regionaccess.IHiddenRegion;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegionFinder;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegionsFinder;
import org.eclipse.xtext.xbase.formatting2.XbaseFormatter;

import com.google.common.base.Objects;

/**
 * @author Moritz Eysholdt - Initial implementation and API
 */
public class DomainmodelFormatter extends XbaseFormatter {
	protected void _format(DomainModel domainmodel, IFormattableDocument document) {
		document.prepend(domainmodel, (IHiddenRegionFormatter it) -> {
			it.setNewLines(0, 0, 1);
			it.noSpace();
		});
		document.append(domainmodel, (IHiddenRegionFormatter it) -> {
			it.newLine();
		});
		format(domainmodel.getImportSection(), document);
		for (AbstractElement element : domainmodel.getElements()) {
			format(element, document);
		}
	}

	protected void _format(PackageDeclaration pkg, IFormattableDocument document) {
		ISemanticRegion open = this.regionFor(pkg).keyword("{");
		ISemanticRegion close = this.regionFor(pkg).keyword("}");
		document.surround(this.regionFor(pkg).feature(DomainmodelPackage.Literals.ABSTRACT_ELEMENT__NAME),  IHiddenRegionFormatter::oneSpace);
		document.append(open, IHiddenRegionFormatter::newLine);
		document.interior(open, close, IHiddenRegionFormatter::indent);
		for (AbstractElement element : pkg.getElements()) {
				document.format(element);
				document.append(element, (IHiddenRegionFormatter it) -> {
					it.setNewLines(1, 1, 2);
				});
		}
	}

	protected void _format(Entity entity, IFormattableDocument document) {
		ISemanticRegion open = this.regionFor(entity).keyword("{");
		ISemanticRegion close = this.regionFor(entity).keyword("}");
		document.surround(this.regionFor(entity).feature(DomainmodelPackage.Literals.ABSTRACT_ELEMENT__NAME), IHiddenRegionFormatter::oneSpace);
		document.surround(entity.getSuperType(), IHiddenRegionFormatter::oneSpace);
		document.append(open, IHiddenRegionFormatter::newLine);
		document.interior(open, close, IHiddenRegionFormatter::indent);
		format(entity.getSuperType(), document);
		for (Feature feature : entity.getFeatures()) {
			document.format(feature);
			document.append(feature, (IHiddenRegionFormatter it) -> {
				it.setNewLines(1, 1, 2);
			});
		}
	}

	protected void _format(Property property, IFormattableDocument document) {
		document.surround(this.regionFor(property).keyword(":"), IHiddenRegionFormatter::noSpace);
		document.format(property.getType());
	}

	protected void _format(Operation operation, IFormattableDocument document) {
		document.append(this.regionFor(operation).keyword("op"), IHiddenRegionFormatter::oneSpace);
		document.surround(this.regionFor(operation).keyword("("), IHiddenRegionFormatter::noSpace);
		if (!operation.getParams().isEmpty()) {
			for (ISemanticRegion comma : this.regionFor(operation).keywords(",")) {
				document.append(document.prepend(comma,  IHiddenRegionFormatter::noSpace),IHiddenRegionFormatter::oneSpace);
			}
			for (JvmFormalParameter params : operation.getParams()) {
				document.format(params);
			}
			document.prepend(this.regionFor(operation).keyword(")"), IHiddenRegionFormatter::noSpace);
		}
		if (!Objects.equal(operation.getType(), null)) {
			document.append(this.regionFor(operation).keyword(")"),IHiddenRegionFormatter::noSpace);
			document.append(document.prepend(operation.getType(), IHiddenRegionFormatter::noSpace), IHiddenRegionFormatter::oneSpace);
			document.format(operation.getType());
		} else {
			document.append(this.regionFor(operation).keyword(")"), IHiddenRegionFormatter::oneSpace);
		}
		document.format(operation.getBody());
	}

	@Override
	public void format(Object entity, IFormattableDocument document) {
		if (entity instanceof Entity) {
			_format((Entity) entity, document);
			return;
		} else if (entity instanceof Operation) {
			_format((Operation) entity, document);
			return;
		} else if (entity instanceof PackageDeclaration) {
			_format((PackageDeclaration) entity, document);
			return;
		} else if (entity instanceof Property) {
			_format((Property) entity, document);
			return;
		} else if (entity instanceof DomainModel) {
			_format((DomainModel) entity, document);
			return;
		}
		super.format(entity, document);
	}
	
	// downport from AbstractFormatterJava
	
	// implementations that forward the methods of ITextRegionExtensions to simplify formatter code.

		protected ISemanticRegionsFinder allRegionsFor(EObject semanticElement) {
			return textRegionExtensions.allRegionsFor(semanticElement);
		}

		protected Iterable<ISemanticRegion> allSemanticRegions(EObject semanticElement) {
			return textRegionExtensions.allSemanticRegions(semanticElement);
		}

		protected EObject grammarElement(EObject semanticElement) {
			return textRegionExtensions.grammarElement(semanticElement);
		}

		protected ISemanticRegionFinder immediatelyFollowing(EObject semanticElement) {
			return textRegionExtensions.immediatelyFollowing(semanticElement);
		}

		protected ISemanticRegionFinder immediatelyPreceding(EObject semanticElement) {
			return textRegionExtensions.immediatelyPreceding(semanticElement);
		}

		protected boolean isMultiline(EObject semanticElement) {
			return textRegionExtensions.isMultiline(semanticElement);
		}

		protected IHiddenRegion nextHiddenRegion(EObject semanticElement) {
			return textRegionExtensions.nextHiddenRegion(semanticElement);
		}

		protected IHiddenRegion previousHiddenRegion(EObject semanticElement) {
			return textRegionExtensions.previousHiddenRegion(semanticElement);
		}

		protected ISemanticRegionsFinder regionFor(EObject semanticElement) {
			return textRegionExtensions.regionFor(semanticElement);
		}

		protected IEObjectRegion regionForEObject(EObject semanticElement) {
			return textRegionExtensions.regionForEObject(semanticElement);
		}

		protected Iterable<ISemanticRegion> semanticRegions(EObject semanticElement) {
			return textRegionExtensions.semanticRegions(semanticElement);
		}
}
