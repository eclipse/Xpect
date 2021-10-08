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

package org.eclipse.xpect.runner;

import org.eclipse.xtext.validation.CheckType;

// org.eclipse.xtext.validation.CheckType + DISABLED 
public enum LiveExecutionType {
	DISABLED {
		@Override
		public CheckType toCheckType() {
			return null;
		}
	},
	FAST {
		@Override
		public CheckType toCheckType() {
			return CheckType.FAST;
		}
	};

	public abstract CheckType toCheckType();
}
