package org.eclipse.xtext.example.domainmodel.validation;

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
public interface IssueCodes {

	String PREFIX = "org.eclipse.xtext.example.domainmodel.domainmodel.";
	
	String INVALID_TYPE_NAME = PREFIX + "InvalidTypeName";
	
	String INVALID_FEATURE_NAME = PREFIX + "InvalidFeatureName";

	String MISSING_TYPE = PREFIX + "MissingType";

}
