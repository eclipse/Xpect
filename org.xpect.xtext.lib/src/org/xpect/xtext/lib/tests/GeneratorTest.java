/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.xpect.xtext.lib.tests;

import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.IGenerator2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.CancelIndicator;
import org.junit.Assert;
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
 * @author Moritz Eysholdt - Initial contribution and API
 */
@RunWith(XpectRunner.class)
@XpectImport({ XtextStandaloneSetup.class, XtextWorkspaceSetup.class })
public class GeneratorTest {

	protected static class NullGeneratorContext implements IGeneratorContext {

		@Override
		public CancelIndicator getCancelIndicator() {
			return null;
		}

	}

	@Inject(optional = true)
	private IGenerator generator;

	@Inject(optional = true)
	private IGenerator2 generator2;

	protected IGeneratorContext createGeneratorContext(XtextResource resource) {
		return new NullGeneratorContext();
	}

	protected InMemoryFileSystemAccessFormatter createInMemoryFileSystemAccessFormatter() {
		return new InMemoryFileSystemAccessFormatter();
	}

	@Xpect(liveExecution = LiveExecutionType.FAST)
	@ParameterParser(syntax = "('file' arg2=TEXT)?")
	public void generated(@StringExpectation IStringExpectation expectation, @ThisResource XtextResource resource, String arg2) {
		InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
		IGenerator gen1 = getGenerator();
		IGenerator2 gen2 = getGenerator2();
		if (gen2 != null) {
			IGeneratorContext context = createGeneratorContext(resource);
			gen2.beforeGenerate(resource, fsa, context);
			gen2.doGenerate(resource, fsa, context);
			gen2.afterGenerate(resource, fsa, context);
		} else if (gen1 != null) {
			gen1.doGenerate(resource, fsa);
		} else {
			Assert.fail("no generator available");
		}
		String files = createInMemoryFileSystemAccessFormatter().includeOnlyFileNamesEndingWith(arg2).apply(fsa);
		expectation.assertEquals(files);
	}

	protected IGenerator getGenerator() {
		return generator;
	}

	protected IGenerator2 getGenerator2() {
		if (generator2 != null) {
			return generator2;
		}
		if (generator instanceof IGenerator2) {
			return (IGenerator2) generator;
		}
		return null;
	}

}
