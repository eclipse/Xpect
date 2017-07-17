package org.xpect.xtext.lib.tests;

import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.resource.XtextResource;
import org.junit.runner.RunWith;
import org.xpect.XpectImport;
import org.xpect.expectation.IStringExpectation;
import org.xpect.expectation.StringExpectation;
import org.xpect.parameter.ParameterParser;
import org.xpect.runner.Xpect;
import org.xpect.runner.XpectRunner;
import org.xpect.xtext.lib.setup.ThisResource;
import org.xpect.xtext.lib.setup.XtextStandaloneSetup;
import org.xpect.xtext.lib.setup.XtextWorkspaceSetup;
import org.xpect.xtext.lib.util.InMemoryFileSystemAccessFormatter;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@RunWith(XpectRunner.class)
@XpectImport({ XtextStandaloneSetup.class, XtextWorkspaceSetup.class })
@Deprecated // use org.xpect.xtext.xbase.lib.tests.JvmModelInferrerTest
public class JvmModelInferrerTest {

	@Deprecated
	protected String formatFiles(InMemoryFileSystemAccess fsa, String fileName) {
		return new InMemoryFileSystemAccessFormatter().includeOnlyFileNamesEndingWith(fileName).apply(fsa);
	}

	@Xpect
	@ParameterParser(syntax = "('file' arg2=TEXT)?")
	public void jvmModel(@StringExpectation IStringExpectation expectation, @ThisResource XtextResource resource, String arg2) {
		throw new UnsupportedOperationException("use org.xpect.xtext.xbase.lib.tests.JvmModelInferrerTest");
	}

	@Xpect
	@ParameterParser(syntax = "('file' arg2=TEXT)?")
	public void jvmModelSignatures(@StringExpectation IStringExpectation expectation, @ThisResource XtextResource resource, String arg2) {
		throw new UnsupportedOperationException("use org.xpect.xtext.xbase.lib.tests.JvmModelInferrerTest");
	}
}
