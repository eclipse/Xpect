package org.xpect.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.xpect.XpectJavaModelPackage;
import org.xpect.XpectPackage;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectProposalProvider extends AbstractXpectProposalProvider {
	protected void lookupCrossReference(EObject model, EReference reference, ICompletionProposalAcceptor acceptor, Predicate<IEObjectDescription> filter,
			Function<IEObjectDescription, ICompletionProposal> proposalFactory) {
		if (reference == XpectPackage.Literals.XPECT_TEST__DECLARED_SUITE)
			reference = XpectJavaModelPackage.Literals.XJM_CLASS__JVM_CLASS;
		super.lookupCrossReference(model, reference, acceptor, filter, proposalFactory);
	}
}
