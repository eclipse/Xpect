package org.eclipse.xtext.example.domainmodel.tests;

import org.eclipse.xtext.example.domainmodel.DomainmodelUiInjectorProvider;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.xbase.junit.ui.AbstractContentAssistTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(DomainmodelUiInjectorProvider.class)
public class ContentAssistTest extends AbstractContentAssistTest {
	@Test
	public void testImportCompletion() throws Exception {
		newBuilder().append("import java.util.Da").assertText("java.util.Date");
	}

	@Test
	public void testImportCompletion_1() throws Exception {
		newBuilder().append("import LinkedHashSet").assertText("java.util.LinkedHashSet");
	}

	@Test
	public void testTypeCompletion() throws Exception {
		newBuilder().append("entity Foo { bar: LinkedHashSet").assertText("java.util.LinkedHashSet");
	}
}
