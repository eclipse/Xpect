package org.eclipse.xtext.example.domainmodel.xpect.tests;

import org.eclipse.xtext.example.domainmodel.xpect.tests.generator.DMGeneratorTest;
import org.eclipse.xtext.example.domainmodel.xpect.tests.jvmmodelinferrer.DMJvmModelInferrerTest;
import org.eclipse.xtext.example.domainmodel.xpect.tests.linking.DMLinkingTest;
import org.eclipse.xtext.example.domainmodel.xpect.tests.modify.DeleteUtilTest;
import org.eclipse.xtext.example.domainmodel.xpect.tests.parser.ParserTest;
import org.eclipse.xtext.example.domainmodel.xpect.tests.scoping.DMScopingTest;
import org.eclipse.xtext.example.domainmodel.xpect.tests.testsuite.DMXtextTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
/*    */DMJvmModelInferrerTest.class, //
		DMLinkingTest.class,//
		DeleteUtilTest.class,//
		ParserTest.class,//
		DMGeneratorTest.class,//
		DMScopingTest.class,//
		DMXtextTests.class//
})
@RunWith(Suite.class)
public class AllDomainModelXpectTestsThatRunWithXbase28xAsWell {

}
