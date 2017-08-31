/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.eclipse.xpect.xtext.lib.tests;

import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.resource.XtextResource;
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

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@RunWith(XpectRunner.class)
@XpectImport({ XtextStandaloneSetup.class, XtextWorkspaceSetup.class })
@Deprecated // use org.eclipse.xpect.xtext.xbase.lib.tests.JvmModelInferrerTest
public class JvmModelInferrerTest {

	@Deprecated
	protected String formatFiles(InMemoryFileSystemAccess fsa, String fileName) {
		return new InMemoryFileSystemAccessFormatter().includeOnlyFileNamesEndingWith(fileName).apply(fsa);
	}

	@Xpect
	@ParameterParser(syntax = "('file' arg2=TEXT)?")
	public void jvmModel(@StringExpectation IStringExpectation expectation, @ThisResource XtextResource resource, String arg2) {
		throw new UnsupportedOperationException("use org.eclipse.xpect.xtext.xbase.lib.tests.JvmModelInferrerTest");
	}

	@Xpect
	@ParameterParser(syntax = "('file' arg2=TEXT)?")
	public void jvmModelSignatures(@StringExpectation IStringExpectation expectation, @ThisResource XtextResource resource, String arg2) {
		throw new UnsupportedOperationException("use org.eclipse.xpect.xtext.xbase.lib.tests.JvmModelInferrerTest");
	}
}
