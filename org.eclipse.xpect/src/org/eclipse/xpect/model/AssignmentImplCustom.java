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

package org.eclipse.xpect.model;

import org.eclipse.xpect.AbstractComponent;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class AssignmentImplCustom extends AssignmentImpl {
	@Override
	public AbstractComponent getInstance() {
		return (AbstractComponent) eContainer();
	}
}
