package org.xpect;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.common.types.TypesPackage;

import com.google.inject.Injector;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XpectStandaloneSetup extends XpectStandaloneSetupGenerated {

	public static void doSetup() {
		new XpectStandaloneSetup().createInjectorAndDoEMFRegistration();
	}

	@Override
	public Injector createInjectorAndDoEMFRegistration() {
		if (!EPackage.Registry.INSTANCE.containsKey(TypesPackage.eNS_URI))
			EPackage.Registry.INSTANCE.put(TypesPackage.eNS_URI, TypesPackage.eINSTANCE);
		if (!EPackage.Registry.INSTANCE.containsKey(XpectJavaModelPackage.eNS_URI))
			EPackage.Registry.INSTANCE.put(XpectJavaModelPackage.eNS_URI, XpectJavaModelPackage.eINSTANCE);
		if (!EPackage.Registry.INSTANCE.containsKey(XpectPackage.eNS_URI))
			EPackage.Registry.INSTANCE.put(XpectPackage.eNS_URI, XpectPackage.eINSTANCE);
		return super.createInjectorAndDoEMFRegistration();
	}

	@Override
	public void register(Injector injector) {
		super.register(injector);
	}
}
