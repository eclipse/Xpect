package org.eclipse.xpect.tests.expectation.impl;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.eclipse.xpect.expectation.ILinesExpectation;
import org.eclipse.xpect.expectation.LinesExpectation;
import org.eclipse.xpect.lib.XpectTestResultTest;
import org.eclipse.xpect.runner.Xpect;
import org.eclipse.xpect.runner.XpectRunner;
import org.eclipse.xpect.runner.XpectSuiteClasses;

import com.google.common.base.Function;

/**
 * @author Dennis Huebner - Initial contribution and API
 *
 */
@RunWith(XpectRunner.class)
@XpectSuiteClasses(XpectTestResultTest.class)
public class ExpectationTest {

	@Xpect
	public void expectedExpectation(ILinesExpectation expectation) {
		expectation.assertEquals(Arrays.asList(new String[] { "Hallo <tester>!" }));
	}

	@Xpect
	public void modifiedExpectation(@LinesExpectation(expectationFormatter = TestExpectationFormatter.class) ILinesExpectation expectation) {
		expectation.assertEquals(Arrays.asList(new String[] { "Hallo Dennis!" }));
	}

	static public class TestExpectationFormatter implements Function<String, String> {

		@Override
		public String apply(String input) {
			return input.replace("<tester>", "Dennis");
		}
	}
}
