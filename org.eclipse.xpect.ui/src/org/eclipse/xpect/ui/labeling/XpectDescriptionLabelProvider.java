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

package org.eclipse.xpect.ui.labeling;

import org.eclipse.xtext.ui.label.DefaultDescriptionLabelProvider;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectDescriptionLabelProvider extends DefaultDescriptionLabelProvider {

	/*
	 * //Labels and icons can be computed like this:
	 * 
	 * String text(IEObjectDescription ele) { return "my "+ele.getName(); }
	 * 
	 * String image(IEObjectDescription ele) { return ele.getEClass().getName()
	 * + ".gif"; }
	 */

}
