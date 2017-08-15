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

package org.eclipse.xpect.ui.services;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.texteditor.IAnnotationImageProvider;
import org.eclipse.xpect.ui.internal.XpectActivator;

public class XpectAnnotationImageProvider implements IAnnotationImageProvider {

	public static String ANNOTATION_NAME_SUCCESS = "org.eclipse.xpect.ui.live_test_execution_success_annotation";

	@Override
	public Image getManagedImage(Annotation annotation) {
		if (ANNOTATION_NAME_SUCCESS.equals(annotation.getType())) {
			ImageRegistry imageRegistry = XpectActivator.getInstance().getImageRegistry();
			Image image = imageRegistry.get(ANNOTATION_NAME_SUCCESS);
			if (image == null) {
				image = AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.xpect.ui", "icons/testok.gif").createImage();
				imageRegistry.put(ANNOTATION_NAME_SUCCESS, image);
			}
			return image;
		} else {
			return null;
		}
	}

	@Override
	public String getImageDescriptorId(Annotation annotation) {
		return null;
	}

	@Override
	public ImageDescriptor getImageDescriptor(String imageDescritporId) {
		return null;
	}

}
