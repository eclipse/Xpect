package org.xpect.xtext.lib.tests;

import org.eclipse.xtext.generator.IGenerator2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.CancelIndicator;
import org.junit.runner.RunWith;
import org.xpect.XpectImport;
import org.xpect.expectation.IStringExpectation;
import org.xpect.expectation.StringExpectation;
import org.xpect.parameter.ParameterParser;
import org.xpect.runner.LiveExecutionType;
import org.xpect.runner.Xpect;
import org.xpect.runner.XpectRunner;
import org.xpect.xtext.lib.setup.ThisResource;
import org.xpect.xtext.lib.setup.XtextStandaloneSetup;
import org.xpect.xtext.lib.setup.XtextWorkspaceSetup;
import org.xpect.xtext.lib.util.InMemoryFileSystemAccessFormatter;

import com.google.inject.Inject;

/**
 * @author Raul Valdoleiros (Skiler) - Initial contribution and API
 */
@RunWith(XpectRunner.class)
@XpectImport({ XtextStandaloneSetup.class, XtextWorkspaceSetup.class })
public class Generator2Test {

	@Inject
	private IGenerator2 generator;

	protected InMemoryFileSystemAccessFormatter createInMemoryFileSystemAccessFormatter() {
		return new InMemoryFileSystemAccessFormatter();
	}

	public IGenerator2 getGenerator() {
		return generator;
	}

	@Xpect(liveExecution = LiveExecutionType.FAST)
	@ParameterParser(syntax = "('file' arg2=TEXT)?")
	public void generated(@StringExpectation IStringExpectation expectation, @ThisResource XtextResource resource, String arg2) {
		InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
		generator.doGenerate(resource, fsa, new NullGeneratorContext());
		String files = createInMemoryFileSystemAccessFormatter().includeOnlyFileNamesEndingWith(arg2).apply(fsa);
		expectation.assertEquals(files);
	}

	private class NullGeneratorContext implements IGeneratorContext {

		@Override
		public CancelIndicator getCancelIndicator() {
			return null;
		}

	}
}
