package org.eclipse.xtext.example.domainmodel.ui.linking;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.diagnostics.DiagnosticMessage;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity;
import org.eclipse.xtext.example.domainmodel.domainmodel.Operation;
import org.eclipse.xtext.example.domainmodel.domainmodel.Property;
import org.eclipse.xtext.example.domainmodel.domainmodel.util.DomainmodelSwitch;
import org.eclipse.xtext.example.domainmodel.validation.IssueCodes;
import org.eclipse.xtext.linking.impl.LinkingDiagnosticMessageProvider;

/**
 * @author Jan Koehnlein initial contribution and API
 */
public class DomainmodelLinkingDiagnosticMessageProvider extends LinkingDiagnosticMessageProvider {

	@Override
	public DiagnosticMessage getUnresolvedProxyMessage(final ILinkingDiagnosticContext context) {
		EObject element = context.getContext();
		if (element instanceof JvmTypeReference) {
			JvmTypeReference jvmTypeReference = (JvmTypeReference) element;
			DiagnosticMessage diagnosticMessage = new DomainmodelSwitch<DiagnosticMessage>() {
				@Override
				public DiagnosticMessage caseEntity(Entity entity) {
					return new DiagnosticMessage("Missing supertype " + context.getLinkText(), Severity.ERROR,
							IssueCodes.MISSING_TYPE, context.getLinkText());
				}

				@Override
				public DiagnosticMessage caseProperty(Property property) {
					return new DiagnosticMessage("Missing property type " + context.getLinkText(), Severity.ERROR,
							IssueCodes.MISSING_TYPE, context.getLinkText());
				}

				@Override
				public DiagnosticMessage caseOperation(Operation operation) {
					return new DiagnosticMessage("Missing return type " + context.getLinkText(), Severity.ERROR,
							IssueCodes.MISSING_TYPE, context.getLinkText());
				}
			}.doSwitch(jvmTypeReference.eContainer());
			if (diagnosticMessage != null)
				return diagnosticMessage;
		}
		return super.getUnresolvedProxyMessage(context);
	}
}
