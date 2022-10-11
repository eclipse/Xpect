package org.eclipse.xtext.example.domainmodel.jvmmodel;

import java.util.Arrays;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenationClient;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity;
import org.eclipse.xtext.example.domainmodel.domainmodel.Feature;
import org.eclipse.xtext.example.domainmodel.domainmodel.Operation;
import org.eclipse.xtext.example.domainmodel.domainmodel.Property;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

import com.google.common.base.Objects;
import com.google.inject.Inject;

public class DomainmodelJvmModelInferrer extends AbstractModelInferrer {
	@Inject
	private JvmTypesBuilder jvmTypesBuilder;

	@Inject
	@Extension
	private IQualifiedNameProvider iQualifiedNameProvider;

	protected void _infer(Entity entity, @Extension IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		acceptor.accept(this.jvmTypesBuilder.toClass(entity, this.iQualifiedNameProvider.getFullyQualifiedName(entity)), (JvmGenericType it) -> {
			this.jvmTypesBuilder.setDocumentation(it, this.jvmTypesBuilder.getDocumentation(entity));
			if (!Objects.equal(entity.getSuperType(), null)) {
				this.jvmTypesBuilder.operator_add(it.getSuperTypes(), this.jvmTypesBuilder.cloneWithProxies(entity.getSuperType()));
			}
			// let's add a default constructor 
			this.jvmTypesBuilder.operator_add(it.getMembers(), this.jvmTypesBuilder.toConstructor(entity, c -> {
			}));
			// and one which can be called with a lambda for initialization.
			JvmTypeReference procedureType = this._typeReferenceBuilder.typeRef(Procedure1.class, this._typeReferenceBuilder.typeRef(it));
			JvmConstructor constructor = this.jvmTypesBuilder.toConstructor(entity, (JvmConstructor c) -> {
				this.jvmTypesBuilder.operator_add(c.getParameters(),
						this.jvmTypesBuilder.toParameter(entity, "initializer", procedureType));
				// here we implement the body using black box Java code.
				this.jvmTypesBuilder.setBody(c, new StringConcatenationClient() {
					@Override
					protected void appendTo(StringConcatenationClient.TargetStringConcatenation b) {
						b.append("initializer.apply(this);");
						b.newLine();
					}
				});
			});
			this.jvmTypesBuilder.operator_add(it.getMembers(), constructor);
			// now let's go over the features
			for (Feature f : entity.getFeatures()) {
				if (f instanceof Property) {
					// for properties we create a field, a getter and a setter
					JvmField field = this.jvmTypesBuilder.toField(f, ((Property) f).getName(), ((Property) f).getType());
					this.jvmTypesBuilder.operator_add(it.getMembers(), field);
					this.jvmTypesBuilder.operator_add(it.getMembers(), this.jvmTypesBuilder.toGetter(f, ((Property) f).getName(), ((Property) f).getType()));
					this.jvmTypesBuilder.operator_add(it.getMembers(), this.jvmTypesBuilder.toSetter(f, ((Property) f).getName(), ((Property) f).getType()));
				} else if (f instanceof Operation) {
					// operations are mapped to methods
					JvmTypeReference type = ((Operation) f).getType();
					if (type == null) {
						type = this.jvmTypesBuilder.inferredType();
					}
					this.jvmTypesBuilder.operator_add(it.getMembers(), this.jvmTypesBuilder.toMethod(f, ((Operation) f).getName(), type, (JvmOperation o) -> {
						this.jvmTypesBuilder.setDocumentation(o, this.jvmTypesBuilder.getDocumentation(f));
						for (JvmFormalParameter p : ((Operation) f).getParams()) {
							this.jvmTypesBuilder.operator_add(o.getParameters(), this.jvmTypesBuilder.toParameter(p, p.getName(), p.getParameterType()));
						}
						// here the body is implemented using a user expression.
						// Note that by doing this we set the expression into the context of this method, 
						// The parameters, 'this' and all the members of this method will be visible for the expression. 

						this.jvmTypesBuilder.setBody(o, ((Operation) f).getBody());
					}));
				}
			}
			// finally we want to have a nice toString methods.
			this.jvmTypesBuilder.operator_add(it.getMembers(), this.jvmTypesBuilder.toToStringMethod(entity, it));
		});
	}

	@Override
	public void infer(EObject entity, IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
		if (entity instanceof Entity) {
			_infer((Entity) entity, acceptor, prelinkingPhase);
			return;
		} else if (entity != null) {
			_infer(entity, acceptor, prelinkingPhase);
			return;
		} else {
			throw new IllegalArgumentException(
					"Unhandled parameter types: " + Arrays.<Object> asList(entity, acceptor, prelinkingPhase).toString());
		}
	}
}
