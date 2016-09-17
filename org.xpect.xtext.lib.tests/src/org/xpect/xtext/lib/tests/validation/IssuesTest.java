package org.xpect.xtext.lib.tests.validation;

import org.junit.runner.RunWith;
import org.xpect.XpectImport;
import org.xpect.runner.XpectRunner;
import org.xpect.runner.XpectSuiteClasses;
import org.xpect.xtext.lib.setup.XtextStandaloneSetup;
import org.xpect.xtext.lib.tests.ValidationTest;
import org.xpect.xtext.lib.tests.ValidationTestModuleSetup;

@RunWith(XpectRunner.class)
@XpectSuiteClasses({IssuesTest.class, ValidationTest.class})
@XpectImport({XtextStandaloneSetup.class, ValidationTestModuleSetup.class})
public class IssuesTest {

}
