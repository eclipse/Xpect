package org.eclipse.xtext.example.arithmetics.ui.labeling

import com.google.inject.Inject
import org.eclipse.jface.viewers.ILabelProvider
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.ui.label.DefaultDescriptionLabelProvider

/**
 * Provides labels for a IEObjectDescriptions and IResourceDescriptions.
 * 
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#label-provider
 */
class ArithmeticsDescriptionLabelProvider extends DefaultDescriptionLabelProvider {

	@Inject ILabelProvider labelProvider

	override getImage(Object element) {
		if (element instanceof IEObjectDescription) 
			labelProvider.getImage(element.EObjectOrProxy)
		else 
			super.getImage(element)
	}
	 
}
