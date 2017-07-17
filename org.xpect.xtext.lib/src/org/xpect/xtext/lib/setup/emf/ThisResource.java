package org.xpect.xtext.lib.setup.emf;

import org.xpect.setup.XpectSetupComponent;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@XpectSetupComponent
public class ThisResource extends Resource {

	public ThisResource() {
		super(new org.xpect.xtext.lib.setup.generic.ThisFile());
	}

	public ThisResource(org.xpect.xtext.lib.setup.generic.ThisFile file) {
		super(file);
	}

	public ThisResource(String name) {
		super(name);
	}

}
