package org.xpect.xtext.lib.setup.emf;

import org.xpect.setup.XpectSetupComponent;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@Deprecated
@XpectSetupComponent
public class File extends org.xpect.xtext.lib.setup.emf.Resource {

	public File() {
		super();
	}

	public File(org.xpect.xtext.lib.setup.generic.File file) {
		super(file);
	}

	public File(String name) {
		super(new org.xpect.xtext.lib.setup.generic.File(name));
	}

}
