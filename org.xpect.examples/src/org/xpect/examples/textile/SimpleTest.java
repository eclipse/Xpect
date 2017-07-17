package org.xpect.examples.textile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.xpect.expectation.CommaSeparatedValuesExpectation;
import org.xpect.expectation.ICommaSeparatedValuesExpectation;
import org.xpect.expectation.IStringExpectation;
import org.xpect.expectation.StringExpectation;
import org.xpect.runner.Xpect;
import org.xpect.runner.XpectRunner;
import org.xpect.runner.XpectTestFiles;

import com.google.common.collect.Lists;

@RunWith(XpectRunner.class)
@XpectTestFiles(fileExtensions = "xt")
public class SimpleTest {

	@Xpect
	public void simple() {
		System.out.println("Hello World");
	}

	@Xpect
	public void simpleString(@StringExpectation IStringExpectation expectation) {
		System.out.println("simpleString");
		expectation.assertEquals("Foo Bar");
	}

	@Xpect
	public void simpleCSV(
			@CommaSeparatedValuesExpectation ICommaSeparatedValuesExpectation expectation) {
		System.out.println("simpleString");
		expectation.assertEquals(Lists.newArrayList("aa", "bb", "cc"));
	}

	@Test
	public void testPlainJUnitTest() {
		System.out.println("plain junit");
	}

}
