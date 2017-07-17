package org.xpect.model;

import org.xpect.Assignment;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class ComponentImplCustom extends ComponentImpl {
	@Override
	public Assignment getAssignment() {
		return (Assignment) eContainer();
	}
}
