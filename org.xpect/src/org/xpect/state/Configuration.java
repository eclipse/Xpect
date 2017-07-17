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

package org.xpect.state;

import java.lang.annotation.Annotation;
import java.util.List;

import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.Tuples;
import org.xpect.runner.XpectFileRunner;
import org.xpect.runner.XpectRunner;
import org.xpect.runner.XpectTestRunner;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * Similar to a Guice Module, bindings (values or factories) are added by {@link XpectRunner}, {@link XpectFileRunner}, and {@link XpectTestRunner}. It is not directly used,
 * though. Instead, with this configuration, the {@link StateContainer} is created which is aware of dependencies and is able to resolve them. The state container then injects
 * objects. Bindings are added by the Xpect runners, see {@link XpectRunner#createConfiguration()} for example.
 */
public class Configuration {
	private final List<Class<?>> factories = Lists.newArrayList();
	private final String name;
	private final Multimap<Class<? extends Annotation>, Pair<Class<?>, Managed<?>>> values = LinkedHashMultimap.create();

	public Configuration(String name) {
		this.name = name;
	}

	public <T> void addDefaultValue(Class<? super T> type, Managed<T> value) {
		addValue(Default.class, type, value);
	}

	public <T> void addDefaultValue(Class<? super T> type, T value) {
		addValue(Default.class, type, value);
	}

	@SuppressWarnings("unchecked")
	public <T> void addDefaultValue(T value) {
		if (value instanceof Managed<?>)
			throw new IllegalStateException("please use addDefaultValue(Class<? super T> type, Managed<T> value)");
		addValue(Default.class, (Class<T>) value.getClass(), value);
	}

	public void addFactory(Class<?> factory) {
		Preconditions.checkNotNull(factory);
		factories.add(factory);
	}

	public <T> void addValue(Class<? extends Annotation> annotation, Class<? super T> type, Managed<T> value) {
		if (annotation.getAnnotation(XpectStateAnnotation.class) == null)
			throw new IllegalStateException("@" + annotation.getName() + " must be annotated with @" + XpectStateAnnotation.class.getSimpleName());
		values.put(annotation, Tuples.<Class<?>, Managed<?>> create(type, value));
	}

	public <T> void addValue(Class<? extends Annotation> annotation, Class<? super T> type, T value) {
		addValue(annotation, type, new ManagedImpl<T>(value));
	}

	@SuppressWarnings("unchecked")
	public <T> void addValue(Class<? extends Annotation> annotation, T value) {
		if (value instanceof Managed<?>)
			throw new IllegalStateException("please use addValue(Class<? extends Annotation> annotation, Class<? super T> type, Managed<T> value)");
		addValue(annotation, (Class<T>) value.getClass(), value);
	}

	public List<Class<?>> getFactories() {
		return factories;
	}

	public String getName() {
		return name;
	}

	public Multimap<Class<? extends Annotation>, Pair<Class<?>, Managed<?>>> getValues() {
		return values;
	}

}
