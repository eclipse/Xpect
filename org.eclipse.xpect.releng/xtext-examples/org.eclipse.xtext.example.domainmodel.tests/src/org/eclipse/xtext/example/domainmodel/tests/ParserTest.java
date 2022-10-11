package org.eclipse.xtext.example.domainmodel.tests;

import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.example.domainmodel.domainmodel.DomainModel;
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity;
import org.eclipse.xtext.example.domainmodel.domainmodel.Operation;
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration;
import org.eclipse.xtext.example.domainmodel.domainmodel.Property;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.validation.IssueCodes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

@RunWith(XtextRunner.class)
@InjectWith(InjectorProviderCustom.class)
public class ParserTest {
	@Inject
	private ParseHelper<DomainModel> parseHelper;

	@Inject
	private ValidationTestHelper validationTestHelper;

	@Inject
	private IJvmModelAssociations jvmModelAssociations;

	@Test
	public void testParsing() throws Exception {
		String text =
				"package example {\n" +
				"  entity MyEntity {\n" +
				"    property : String\n" +
				"  }\n" +
				"}\n";
		DomainModel model = this.parseHelper.parse(text);
		PackageDeclaration pack = (PackageDeclaration) model.getElements().get(0);
		Assert.assertEquals("example", pack.getName());
		Entity entity = (Entity) pack.getElements().get(0);
		Assert.assertEquals("MyEntity", entity.getName());
		Property property = (Property) entity.getFeatures().get(0);
		Assert.assertEquals("property", property.getName());
		Assert.assertEquals("java.lang.String", property.getType().getIdentifier());
	}

	@Test
	public void testJvmTypeReferencesValidator() throws Exception {
		String text =
				"import java.util.List\n" +
				"package example {\n" +
				"  entity MyEntity {\n" +
				"    p : List<int>\n" +
				"  }\n" +
				"}\n";
		this.validationTestHelper.assertError(this.parseHelper.parse(text), TypesPackage.Literals.JVM_TYPE_REFERENCE,
				IssueCodes.INVALID_USE_OF_TYPE, "The primitive \'int\' cannot be a type argument");
	}

	@Test
	public void testParsingAndLinking() throws Exception {
		String text =
				"package example {\n" +
				"  entity MyEntity {\n" +
				"    property : String\n" +
				"    op foo(String s) : String {\n" +
				"    \tthis.property = s\n" +
				"    \treturn s.toUpperCase\n" +
				"    }\n" +
				"  }\n" +
				"}\n";
		this.validationTestHelper.assertNoErrors(this.parseHelper.parse(text));
	}

	@Test
	public void testParsingAndLinkingWithImports() throws Exception {
		String text =
				"import java.util.List\n" +
				"package example {\n" +
				"  entity MyEntity {\n" +
				"    p : List<String>\n" +
				"  }\n" +
				"}\n";
		this.validationTestHelper.assertNoErrors(this.parseHelper.parse(text));
	}

	@Test
	public void testReturnTypeInference() throws Exception {
		String text =
				"package example {\n" +
				"  entity MyEntity {\n" +
				"    property : String\n" +
				"    op foo(String s) {\n" +
				"    \treturn property.toUpperCase + s\n" +
				"    }\n" +
				"  }\n" +
				"}\n";
		DomainModel model = this.parseHelper.parse(text);
		PackageDeclaration pack = ((PackageDeclaration) Iterables.getFirst(model.getElements(), null));
		Entity entity = (Entity) Iterables.getFirst(pack.getElements(), null);
		Operation op = (Operation) Iterables.getLast(entity.getFeatures());
		JvmOperation method = (JvmOperation) Iterables.getFirst(this.jvmModelAssociations.getJvmElements(op), null);
		Assert.assertEquals("String", method.getReturnType().getSimpleName());
	}
}
