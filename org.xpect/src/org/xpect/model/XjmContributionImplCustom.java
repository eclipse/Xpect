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

package org.xpect.model;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.util.Strings;
import org.xpect.Environment;
import org.xpect.XjmContribution;
import org.xpect.XpectRequiredEnvironment;
import org.xpect.util.JvmAnnotationUtil;

import com.google.common.collect.Lists;

public class XjmContributionImplCustom extends XjmContributionImpl {

	private static final Logger LOG = Logger.getLogger(XjmContributionImplCustom.class);

	public void initialize(JvmDeclaredType type, Collection<? extends Annotation> roles) {
		super.setDeactivationReason(null);
		super.setJvmClass(type);
		EList<Annotation> newRoles = super.getRoles();
		newRoles.clear();
		newRoles.addAll(roles);
		EList<Environment> newEnvironments = getEnvironments();
		newEnvironments.clear();
		XpectRequiredEnvironment xpectEnvironment = JvmAnnotationUtil.getJavaAnnotation(getJvmClass(), XpectRequiredEnvironment.class);
		if (xpectEnvironment != null)
			newEnvironments.addAll(Lists.newArrayList(xpectEnvironment.value()));
		else
			newEnvironments.addAll(Lists.newArrayList(Environment.values()));
	}

	@Override
	public boolean isActive() {
		JvmDeclaredType cls = getJvmClass();
		return cls != null && !cls.eIsProxy() && Strings.isEmpty(getDeactivationReason());
	}

	@Override
	public <T> T newInstance(Class<T> expectedType) {
		Class<?> clazz = getJavaClass();
		if (clazz == null || !expectedType.isAssignableFrom(clazz))
			return null;
		try {
			T result = expectedType.cast(clazz.newInstance());
			return result;
		} catch (InstantiationException e) {
			LOG.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void setJvmClass(JvmDeclaredType newJvmClass) {
		throw new UnsupportedOperationException(); // use initialize()
	}

	@Override
	public void setReplacedBy(XjmContribution newReplacedBy) {
		super.setReplacedBy(newReplacedBy);
		setDeactivationReason("ReplacedBy: " + newReplacedBy.getJvmClass().getQualifiedName());
	}
}
