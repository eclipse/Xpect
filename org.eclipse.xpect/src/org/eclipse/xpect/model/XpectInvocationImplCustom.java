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

package org.eclipse.xpect.model;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xpect.XjmContribution;
import org.eclipse.xpect.XjmXpectMethod;
import org.eclipse.xpect.XpectArgument;
import org.eclipse.xpect.XpectFactory;
import org.eclipse.xpect.XpectFile;
import org.eclipse.xpect.XpectJavaModel;
import org.eclipse.xpect.XpectPackage;
import org.eclipse.xpect.parameter.IStatementRelatedRegion;
import org.eclipse.xpect.parameter.IStatementRelatedRegionProvider;
import org.eclipse.xpect.parameter.StatementRelatedRegion;
import org.eclipse.xpect.parameter.StatementRelatedRegionProvider;
import org.eclipse.xpect.util.IJavaReflectAccess;

import com.google.common.collect.Lists;

public class XpectInvocationImplCustom extends XpectInvocationImpl {

	@Override
	public EList<XpectArgument> getArguments() {
		if (this.arguments == null) {
			XjmXpectMethod xjmMethod = getMethod();
			if (xjmMethod != null && !xjmMethod.eIsProxy()) {
				Method javaMethod = xjmMethod.getJavaMethod();
				if (javaMethod != null) {
					EList<XpectArgument> sup = super.getArguments();
					int count = javaMethod.getParameterTypes().length;
					for (int i = 0; i < count; i++) {
						XpectArgument argument = XpectFactory.eINSTANCE.createXpectArgument();
						argument.setIndex(i);
						sup.add(argument);
					}
				}
			}
		}
		return super.getArguments();
	}

	@Override
	public XpectFile getFile() {
		return (XpectFile) eContainer();
	}

	@Override
	public IStatementRelatedRegion getExtendedRegion() {
		if (this.extendedRegion == null) {
			INode node = NodeModelUtils.getNode(this);
			int offset = node.getOffset();
			int end = offset + node.getLength();
			for (IStatementRelatedRegion region : getRelatedRegions()) {
				int o = region.getOffset();
				if (o < offset)
					offset = o;
				int e = o + region.getLength();
				if (e > end)
					end = e;
			}
			this.extendedRegion = new StatementRelatedRegion(this, offset, end - offset);
		}
		return super.getExtendedRegion();
	}

	@Override
	public String getId() {
		if (this.id == null)
			((XpectFileImplCustom) this.getFile()).initalizeInvocationsIDs();
		return super.getId();
	}

	@Override
	public String getMethodName() {
		XjmXpectMethod method = basicGetMethod();
		return method != null && !method.eIsProxy() ? method.getName() : getMethodNameFromNodeModel();
	}

	protected String getMethodNameFromNodeModel() {
		for (INode node : NodeModelUtils.findNodesForFeature(this, XpectPackage.Literals.XPECT_INVOCATION__METHOD))
			return NodeModelUtils.getTokenText(node);
		return null;
	}

	protected Class<?> getParameterType(int paramIndex) {
		XjmXpectMethod xpectMethod = getMethod();
		if (xpectMethod == null)
			return null;
		JvmOperation jvmMethod = xpectMethod.getJvmMethod();
		if (jvmMethod == null || jvmMethod.eIsProxy())
			return null;
		JvmTypeReference parameterType = jvmMethod.getParameters().get(paramIndex).getParameterType();
		if (parameterType == null || parameterType.eIsProxy() || parameterType.getType() == null)
			return null;
		Class<?> expectedType = IJavaReflectAccess.INSTANCE.getRawType(parameterType.getType());
		return expectedType;
	}

	@Override
	public <T extends IStatementRelatedRegion> T getRelatedRegion(Class<T> type) {
		for (IStatementRelatedRegion region : getRelatedRegions())
			if (type.isInstance(region))
				return type.cast(region);
		return null;
	}

	@Override
	public EList<IStatementRelatedRegion> getRelatedRegions() {
		if (this.relatedRegions == null) {
			List<IStatementRelatedRegion> regions = Lists.newArrayList();
			XpectJavaModel xjm = getFile().getJavaModel();
			if (xjm != null) {
				for (XjmContribution contrib : xjm.getContributions(StatementRelatedRegionProvider.class)) {
					IStatementRelatedRegionProvider provider = contrib.newInstance(IStatementRelatedRegionProvider.class);
					if (provider != null) {
						IStatementRelatedRegion region = provider.getRegion(this);
						if (region != null)
							regions.add(region);
					}
				}
				for (int i = 0; i < regions.size(); i++) {
					IStatementRelatedRegion region = regions.get(i);
					if (region instanceof IStatementRelatedRegionProvider.IRefinableStatementRelatedRegion) {
						IStatementRelatedRegion refined = ((IStatementRelatedRegionProvider.IRefinableStatementRelatedRegion) region).refine(regions);
						regions.set(i, refined);
					}
				}
			}
			super.getRelatedRegions().addAll(regions);
		}
		return super.getRelatedRegions();
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setMethod(XjmXpectMethod newMethod) {
		((XpectFileImplCustom) getFile()).unsetInvocationIDs();
		this.relatedRegions = null;
		this.arguments = null;
		this.extendedRegion = null;
		super.setMethod(newMethod);
	}

}
