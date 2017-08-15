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

package org.eclipse.xpect.parameter;

import java.util.Collection;

import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xpect.XpectInvocation;
import org.eclipse.xpect.expectation.IExpectationRegion;
import org.eclipse.xpect.text.IRegion;
import org.eclipse.xpect.text.Text;

@StatementRelatedRegionProvider
public class ParameterRegionProvider implements IStatementRelatedRegionProvider {

	public class RefinableRegion extends StatementRelatedRegion implements IRefinableStatementRelatedRegion {

		public RefinableRegion(XpectInvocation invocation, int offset, int length) {
			super(invocation, offset, length);
		}

		public IStatementRelatedRegion refine(Collection<IStatementRelatedRegion> allRegions) {
			int start = getOffset(), end = getOffset() + getLength();
			for (IRegion claim : allRegions)
				if (claim != this) {
					int claimOffset = claim instanceof IExpectationRegion ? ((IExpectationRegion) claim).getOpeningSeparatorOffset() : claim.getOffset();
					int claimEnd = claim.getOffset() + claim.getLength();
					if (end > claimOffset && end <= claimEnd)
						end = claimOffset;
					if (start >= claimOffset && start < claimEnd)
						start = claimEnd;
				}
			return new ParameterRegion(getStatement(), start, end - start);
		}
	}

	public IStatementRelatedRegion getRegion(XpectInvocation invocation) {
		INode node = NodeModelUtils.getNode(invocation);
		int start = node.getOffset() + node.getLength();
		Text text = new Text(invocation.getFile().getDocument());
		int end = text.currentLineEnd(start);
		if (end < 0)
			end = invocation.getFile().getDocument().length();
		return new RefinableRegion(invocation, start, end - start);
	}

}
