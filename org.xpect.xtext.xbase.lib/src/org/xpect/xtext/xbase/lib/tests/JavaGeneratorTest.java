package org.xpect.xtext.xbase.lib.tests;

import java.util.Map;

import org.eclipse.xtext.generator.GeneratorContext;
import org.eclipse.xtext.generator.GeneratorDelegate;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.JavaVersion;
import org.eclipse.xtext.xbase.compiler.OnTheFlyJavaCompiler2;
import org.eclipse.xtext.xbase.compiler.RegisteringFileSystemAccess;
import org.eclipse.xtext.xbase.compiler.RegisteringFileSystemAccess.GeneratedFile;
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

import com.google.common.collect.Maps;
import com.google.inject.Inject;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@RunWith(XpectRunner.class)
@XpectImport({ XtextStandaloneSetup.class, XtextWorkspaceSetup.class })
public class JavaGeneratorTest {

	@Inject
	private GeneratorDelegate generator;

	protected IGeneratorContext createGeneratorContext(XtextResource resource) {
		return new GeneratorContext();
	}

	protected InMemoryFileSystemAccessFormatter createInMemoryFileSystemAccessFormatter() {
		return new InMemoryFileSystemAccessFormatter();
	}

	@Xpect(liveExecution = LiveExecutionType.FAST)
	@ParameterParser(syntax = "('file' arg2=TEXT)?")
	public void generatedJava(@StringExpectation IStringExpectation expectation, @ThisResource XtextResource resource, String arg2) {
		RegisteringFileSystemAccess fsa = new RegisteringFileSystemAccess();
		IGeneratorContext context = createGeneratorContext(resource);
		generator.beforeGenerate(resource, fsa, context);
		generator.doGenerate(resource, fsa, context);
		generator.afterGenerate(resource, fsa, context);
		String files = createInMemoryFileSystemAccessFormatter().includeOnlyFileNamesEndingWith(arg2).apply(fsa);
		Map<String, String> sources = Maps.newHashMap();
		for (GeneratedFile file : fsa.getGeneratedFiles()) {
			sources.put(file.getJavaClassName(), file.getContents().toString());
		}
		try {
			OnTheFlyJavaCompiler2 compiler = createCompiler();
			compiler.compileToClasses(sources);
		} catch (Throwable t) {
			System.out.println("--------------------- " + resource.getURI().lastSegment() + "-------------------");
			System.out.println(files);
			System.out.println("-----------------------------------------------------------------------------");
			throw t;
		}
		expectation.assertEquals(files);
	}

	protected OnTheFlyJavaCompiler2 createCompiler() {
		return new OnTheFlyJavaCompiler2(JavaGeneratorTest.class.getClassLoader(), JavaVersion.JAVA8);
	}

	protected GeneratorDelegate getGenerator() {
		return generator;
	}

}
