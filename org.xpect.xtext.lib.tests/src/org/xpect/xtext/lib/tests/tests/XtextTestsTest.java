package org.xpect.xtext.lib.tests.tests;

import org.junit.runner.RunWith;
import org.xpect.runner.XpectRunner;
import org.xpect.runner.XpectSuiteClasses;
import org.xpect.xtext.lib.setup.XtextStandaloneSetup;
import org.xpect.xtext.lib.setup.XtextWorkspaceSetup;
import org.xpect.xtext.lib.tests.XtextTests;

@RunWith(XpectRunner.class)
@XpectSuiteClasses({ XtextTests.class, XtextWorkspaceSetup.class, XtextStandaloneSetup.class })
public class XtextTestsTest {

}
