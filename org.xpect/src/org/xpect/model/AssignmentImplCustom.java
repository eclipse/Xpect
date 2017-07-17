package org.xpect.model;

import org.xpect.AbstractComponent;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class AssignmentImplCustom extends AssignmentImpl {
	@Override
	public AbstractComponent getInstance() {
		return (AbstractComponent) eContainer();
	}
}
