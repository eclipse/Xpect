package org.xpect.tests.runner;

import org.eclipse.xtext.util.XtextVersion;
import org.junit.runner.RunWith;
import org.xpect.expectation.IStringExpectation;
import org.xpect.expectation.StringExpectation;
import org.xpect.lib.XpectTestResultTest;
import org.xpect.runner.Xpect;
import org.xpect.runner.XpectRunner;
import org.xpect.runner.XpectSuiteClasses;

@RunWith(XpectRunner.class)
@XpectSuiteClasses(XpectTestResultTest.class)
public class RunnerTest {

	static {
		System.out.println("Xtext-Version in Target Platform: " + XtextVersion.getCurrent().getVersion());
	}

	@Xpect
	public void expectedExpectation(@StringExpectation IStringExpectation expectation) {
		expectation.assertEquals("expectedExpectation");
	}
}
