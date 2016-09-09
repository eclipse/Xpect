package org.xpect.xtext.lib.tests;

import org.junit.runner.RunWith;
import org.xpect.runner.XpectRunner;
import org.xpect.runner.XpectSuiteClasses;

/**
 * Collections of all built-in Xtext Core tests.
 * 
 * @author Moritz Eysholdt
 * 
 * @see org.xpect.xtext.xbase.lib.tests.XtextXbaseTests for additional Xbase-tests
 *
 */
@XpectSuiteClasses({ JvmModelInferrerTest.class, //
		LinkingTest.class, //
		ResourceDescriptionTest.class, //
		ScopingTest.class, //
		ValidationTest.class, //
})
@RunWith(XpectRunner.class)
public class XtextTests {

}
