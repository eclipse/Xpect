package org.xpect.xtext.lib.setup.emf;

import org.xpect.setup.XpectSetupComponent;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@Deprecated
@XpectSetupComponent
public class ThisFile extends ThisResource {

	public ThisFile() {
		super(new org.xpect.xtext.lib.setup.generic.ThisFile());
	}

	public ThisFile(org.xpect.xtext.lib.setup.generic.ThisFile file) {
		super(file);
	}

	public ThisFile(String name) {
		super(name);
	}

}
