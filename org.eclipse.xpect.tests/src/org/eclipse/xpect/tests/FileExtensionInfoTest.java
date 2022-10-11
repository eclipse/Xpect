/**
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 */
package org.eclipse.xpect.tests;

import java.io.ByteArrayInputStream;
import java.net.URL;

import org.eclipse.xpect.registry.FileExtensionInfoRegistry;
import org.eclipse.xpect.registry.StandaloneExtensionRegistry;
import org.junit.Test;

import com.google.common.io.Closeables;

public class FileExtensionInfoTest {
	@Test
	public void testEmfExtensionParserDefault() throws Exception {
		String actual = parse(
				"<extension point=\"org.eclipse.emf.ecore.extension_parser\">\n" +
				"	<parser\n" +
				"		class=\"org.FooExecutableExtensionFactory:org.eclipse.xtext.resource.IResourceFactory\"\n" +
				"		type=\"foo\">\n" +
				"	</parser>\n" +
				"</extension>\n");
		String expected = "EmfFileExtensionInfo [fileExtensions = [foo]]\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void testEmfExtensionParser() throws Exception {
		String actual = parse(
				"<extension point=\"org.eclipse.emf.ecore.extension_parser\">\n" +
				"	<parser\n" +
				"		class=\"org.FooExecutableExtensionFactory:org.foo.BarFactory\"\n" +
				"		type=\"foo\">\n" +
				"	</parser>\n" +
				"</extension>\n");
		String expected = "EmfFileExtensionInfo [fileExtensions = [foo], resourceFactory = org.foo.BarFactory]\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void testResourceServiceProviderDefault() throws Exception {
		String actual = parse(
				"<extension point=\"org.eclipse.xtext.extension_resourceServiceProvider\">\n" +
				"	<resourceServiceProvider\n" +
				"		class=\"org.FooExecutableExtensionFactory:org.eclipse.xtext.ui.resource.IResourceUIServiceProvider\"\n" +
				"		uriExtension=\"foo\">\n" +
				"	</resourceServiceProvider>\n" +
				"</extension>\n" +
				"<extension point=\"org.eclipse.xpect.fileExtensions\">\n" +
				"	<fileExtension\n" +
				"		emfResourceFactory=\"org.eclipse.xtext.resource.IResourceFactory\"\n" +
				"		fileExtension=\"foo\"\n" +
				"		xtextLanguageName=\"org.foo.Foo\"\n" +
				"		xtextResourceServiceProvider=\"org.eclipse.xtext.resource.IResourceServiceProvider\"\n" +
				"		xtextRuntimeModule=\"org.foo.RuntimeModule\">\n" +
				"	</fileExtension>\n" +
				"</extension>\n");
		String expected = "XtextFileExtensionInfo [fileExtensions = [foo], languageID = org.foo.Foo, runtimeModule = org.foo.RuntimeModule, uiModule = org.FooUiModule]\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void testResourceServiceProvider() throws Exception {
		String actual = parse(
				"<extension point=\"org.eclipse.xtext.extension_resourceServiceProvider\">\n" +
				"	<resourceServiceProvider\n" +
				"		class=\"org.FooExecutableExtensionFactory:org.foo.ResourceUIServiceProvider\"\n" +
				"		uriExtension=\"foo\">\n" +
				"	</resourceServiceProvider>\n" +
				"</extension>\n" +
				"<extension point=\"org.eclipse.xpect.fileExtensions\">\n" +
				"	<fileExtension\n" +
				"		emfResourceFactory=\"org.foo.ResourceFactory\"\n" +
				"		fileExtension=\"foo\"\n" +
				"		xtextLanguageName=\"org.foo.Foo\"\n" +
				"		xtextResourceServiceProvider=\"org.foo.ResourceServiceProvider\"\n" +
				"		xtextRuntimeModule=\"org.foo.RuntimeModule\">\n" +
				"	</fileExtension>\n" +
				"</extension>\n");
		String expected = "XtextFileExtensionInfo [fileExtensions = [foo], resourceFactory = org.foo.ResourceFactory, languageID = org.foo.Foo, runtimeModule = org.foo.RuntimeModule, uiModule = org.FooUiModule, resourceServiceProvider = org.foo.ResourceServiceProvider, resourceUIServiceProvider = org.foo.ResourceUIServiceProvider]\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void testEditor() throws Exception {
		String actual = parse(
				"<extension point=\"org.eclipse.ui.editors\">\n" +
				"	<editor\n" +
				"		class=\"org.foo.ui.FooExecutableExtensionFactory:org.eclipse.xtext.ui.editor.XtextEditor\"\n" +
				"		contributorClass=\"org.eclipse.ui.editors.text.TextEditorActionContributor\"\n" +
				"		default=\"true\"\n" +
				"		extensions=\"foo, bar\"\n" +
				"		id=\"org.foo.Foo\"\n" +
				"		name=\"Foo Editor\">\n" +
				"	</editor>\n" +
				"</extension>\n");
		String expected = "XtextFileExtensionInfo [fileExtensions = [bar, foo], languageID = org.foo.Foo, runtimeModule = org.foo.FooRuntimeModule, uiModule = org.foo.ui.FooUiModule]\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void testXpectFileExtension() throws Exception {
		String actual = parse(
				"<extension point=\"org.eclipse.xpect.fileExtensions\">\n" +
				"	<fileExtension\n" +
				"		emfResourceFactory=\"org.foo.ResourceFactory\"\n" +
				"		fileExtension=\"foo, bar\"\n" +
				"		xtextLanguageName=\"org.foo.Foo\"\n" +
				"		xtextResourceServiceProvider=\"org.foo.ResourceServiceProvider\"\n" +
				"		xtextResourceUIServiceProvider=\"org.foo.ResourceUIServiceProvider\"\n" +
				"		xtextRuntimeModule=\"org.foo.RuntimeModule\"\n" +
				"		xtextUiModule=\"org.foo.uiModle\">\n" +
				"	</fileExtension>\n" +
				"</extension>\n");
		String expected = "XtextFileExtensionInfo [fileExtensions = [bar, foo], resourceFactory = org.foo.ResourceFactory, languageID = org.foo.Foo, runtimeModule = org.foo.RuntimeModule, uiModule = org.foo.uiModle, resourceServiceProvider = org.foo.ResourceServiceProvider, resourceUIServiceProvider = org.foo.ResourceUIServiceProvider]\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void testLanguageWithoutUI() throws Exception {
		String actual = parse(
				"<extension point=\"org.eclipse.xpect.fileExtensions\">\n" +
				"	<fileExtension\n" +
				"		emfResourceFactory=\"org.foo.ResourceFactory\"\n" +
				"		fileExtension=\"foo, bar\"\n" +
				"		xtextLanguageName=\"org.foo.Foo\"\n" +
				"		xtextResourceServiceProvider=\"org.foo.ResourceServiceProvider\"\n" +
				"		xtextRuntimeModule=\"org.foo.RuntimeModule\">\n" +
				"	</fileExtension>\n" +
				"</extension>\n");
		String expected = "XtextFileExtensionInfo [fileExtensions = [bar, foo], resourceFactory = org.foo.ResourceFactory, languageID = org.foo.Foo, runtimeModule = org.foo.RuntimeModule, resourceServiceProvider = org.foo.ResourceServiceProvider]\n";
		TestUtil.assertEquals(expected, actual);
	}

	@Test
	public void testRedundant() throws Exception {
		String actual = parse(
				"<extension point=\"org.eclipse.emf.ecore.extension_parser\">\n" +
				"	<parser\n" +
				"		class=\"org.FooExecutableExtensionFactory:org.foo.ResourceFactory\"\n" +
				"		type=\"foo\">\n" +
				"	</parser>\n" +
				"</extension>\n" +
				"<extension point=\"org.eclipse.emf.ecore.extension_parser\">\n" +
				"	<parser\n" +
				"		class=\"org.FooExecutableExtensionFactory:org.foo.ResourceFactory\"\n" +
				"		type=\"bar\">\n" +
				"	</parser>\n" +
				"</extension>\n" +
				"<extension point=\"org.eclipse.xtext.extension_resourceServiceProvider\">\n" +
				"	<resourceServiceProvider\n" +
				"		class=\"org.FooExecutableExtensionFactory:org.foo.ResourceUIServiceProvider\"\n" +
				"		uriExtension=\"foo\">\n" +
				"	</resourceServiceProvider>\n" +
				"</extension>\n" +
				"<extension point=\"org.eclipse.xtext.extension_resourceServiceProvider\">\n" +
				"	<resourceServiceProvider\n" +
				"		class=\"org.FooExecutableExtensionFactory:org.foo.ResourceUIServiceProvider\"\n" +
				"		uriExtension=\"bar\">\n" +
				"	</resourceServiceProvider>\n" +
				"</extension>\n" +
				"<extension point=\"org.eclipse.ui.editors\">\n" +
				"	<editor\n" +
				"		class=\"org.FooExecutableExtensionFactory:org.eclipse.xtext.ui.editor.XtextEditor\"\n" +
				"		contributorClass=\"org.eclipse.ui.editors.text.TextEditorActionContributor\"\n" +
				"		default=\"true\"\n" +
				"		extensions=\"foo, bar\"\n" +
				"		id=\"org.foo.Foo\"\n" +
				"		name=\"Foo Editor\">\n" +
				"	</editor>\n" +
				"</extension>\n" +
				"<extension point=\"org.eclipse.xpect.fileExtensions\">\n" +
				"	<fileExtension\n" +
				"		emfResourceFactory=\"org.foo.ResourceFactory\"\n" +
				"		fileExtension=\"foo, bar\"\n" +
				"		xtextLanguageName=\"org.foo.Foo\"\n" +
				"		xtextResourceServiceProvider=\"org.foo.ResourceServiceProvider\"\n" +
				"		xtextResourceUIServiceProvider=\"org.foo.ResourceUIServiceProvider\"\n" +
				"		xtextRuntimeModule=\"org.foo.FooRuntimeModule\"\n" +
				"		xtextUiModule=\"org.FooUiModule\">\n" +
				"	</fileExtension>\n" +
				"</extension>\n");
		String expected = "XtextFileExtensionInfo [fileExtensions = [bar, foo], resourceFactory = org.foo.ResourceFactory, languageID = org.foo.Foo, runtimeModule = org.foo.FooRuntimeModule, uiModule = org.FooUiModule, resourceServiceProvider = org.foo.ResourceServiceProvider, resourceUIServiceProvider = org.foo.ResourceUIServiceProvider]\n";
		TestUtil.assertEquals(expected, actual);
	}

	public String parse(CharSequence pluginXmlContent) throws Exception {
		String xml = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<?eclipse version=\"3.0\"?>\n" +
				"<plugin>\n" +
				"	" + pluginXmlContent + "\n" +
				"</plugin>\n";
		ByteArrayInputStream in = new ByteArrayInputStream(xml.toString().getBytes("UTF-8"));
		StandaloneExtensionRegistry extensionInfo = new StandaloneExtensionRegistry(new URL("file:/plugin.xml"), in);
		Closeables.close(in, true);
		FileExtensionInfoRegistry fileExtensionInfo = new FileExtensionInfoRegistry(extensionInfo);
		return fileExtensionInfo.toString();
	}
}
