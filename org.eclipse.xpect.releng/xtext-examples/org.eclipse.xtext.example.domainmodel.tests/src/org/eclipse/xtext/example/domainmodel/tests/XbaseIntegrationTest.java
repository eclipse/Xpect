package org.eclipse.xtext.example.domainmodel.tests;

import java.lang.reflect.Method;

import org.eclipse.xtext.example.domainmodel.domainmodel.DomainModel;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.compiler.JvmModelGenerator;
import org.eclipse.xtext.xbase.compiler.OnTheFlyJavaCompiler2;
import org.eclipse.xtext.xbase.junit.evaluation.AbstractXbaseEvaluationTest;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

/**
 * Xbase integration test.
 * 
 * runs all Xbase tests from {@link AbstractXbaseEvaluationTest} in the context of an
 * entity operation.
 * 
 * Unsupported features can be disabled by overriding the respective test method.
 * 
 * @author Sven Efftinge
 */
@RunWith(XtextRunner.class)
@InjectWith(InjectorProviderCustom.class)
public class XbaseIntegrationTest extends AbstractXbaseEvaluationTest {
  @Inject
  private OnTheFlyJavaCompiler2 javaCompiler;
  
  @Inject
  private ParseHelper<DomainModel> parseHelper;
  
  @Inject
  private ValidationTestHelper validationHelper;
  
  @Inject
  private JvmModelGenerator generator;
  
  @Override
  protected Object invokeXbaseExpression(String expression) throws Exception {
      DomainModel parse = parseHelper.parse((("entity Foo { op doStuff() : Object { " + expression) + " } } "));
      validationHelper.assertNoErrors(parse);
      InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
      generator.doGenerate(parse.eResource(), fsa);
      CharSequence concatenation = fsa.getTextFiles().values().iterator().next();
      Class<?> clazz = javaCompiler.compileToClass("Foo", concatenation.toString());
      Object foo = clazz.newInstance();
      Method method = clazz.getDeclaredMethod("doStuff");
      return method.invoke(foo);
  }
}
