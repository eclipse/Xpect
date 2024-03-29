/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.eclipse.xpect.xtext.xbase.lib.tests;

import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.xbase.compiler.GeneratorConfig;
import org.eclipse.xtext.xbase.compiler.JvmModelGenerator;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.junit.runner.RunWith;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.expectation.IStringExpectation;
import org.eclipse.xpect.expectation.StringExpectation;
import org.eclipse.xpect.parameter.ParameterParser;
import org.eclipse.xpect.runner.Xpect;
import org.eclipse.xpect.runner.XpectRunner;
import org.eclipse.xpect.xtext.lib.setup.ThisResource;
import org.eclipse.xpect.xtext.lib.setup.XtextStandaloneSetup;
import org.eclipse.xpect.xtext.lib.setup.XtextWorkspaceSetup;
import org.eclipse.xpect.xtext.lib.util.InMemoryFileSystemAccessFormatter;
import org.eclipse.xpect.xtext.xbase.lib.XbaseWorkspaceDefaultsSetup;

import com.google.inject.Inject;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@SuppressWarnings("restriction")
@RunWith(XpectRunner.class)
@XpectImport({ XtextStandaloneSetup.class, XtextWorkspaceSetup.class, XbaseWorkspaceDefaultsSetup.class })
public class JvmModelInferrerTest {

	public static class SignatureGenerator extends JvmModelGenerator {
		@Override
		public void generateExecutableBody(JvmExecutable op, ITreeAppendable appendable, final GeneratorConfig config) {
			appendable.append("{...}");
		}

		@Override
		public ITreeAppendable generateMember(final JvmMember it, final ITreeAppendable appendable, final GeneratorConfig config) {
			switch (it.getVisibility()) {
			case PROTECTED:
			case PUBLIC:
				return super.generateMember(it, appendable, config);
			case DEFAULT:
			case PRIVATE:
			default:
			}
			return appendable;
		}
	}

	@Inject
	private JvmModelGenerator jvmModelGenerator;

	@Inject
	private SignatureGenerator jvmSignatureGenerator;

	protected String formatFiles(InMemoryFileSystemAccess fsa, String fileName) {
		return new InMemoryFileSystemAccessFormatter().includeOnlyFileNamesEndingWith(fileName).apply(fsa);
	}

	public JvmModelGenerator getJvmModelGenerator() {
		return jvmModelGenerator;
	}

	public SignatureGenerator getJvmSignatureGenerator() {
		return jvmSignatureGenerator;
	}

	@Xpect
	@ParameterParser(syntax = "('file' arg2=TEXT)?")
	public void jvmModel(@StringExpectation IStringExpectation expectation, @ThisResource XtextResource resource, String arg2) {
		InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
		jvmModelGenerator.doGenerate(resource, fsa);
		String files = formatFiles(fsa, arg2);
		expectation.assertEquals(files);
	}

	@Xpect
	@ParameterParser(syntax = "('file' arg2=TEXT)?")
	public void jvmModelSignatures(@StringExpectation IStringExpectation expectation, @ThisResource XtextResource resource, String arg2) {
		InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
		jvmSignatureGenerator.doGenerate(resource, fsa);
		String files = formatFiles(fsa, arg2);
		expectation.assertEquals(files);
	}
}
