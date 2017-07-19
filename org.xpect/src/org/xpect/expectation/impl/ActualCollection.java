/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.xpect.expectation.impl;

import org.xpect.expectation.impl.ActualCollection.ActualItem;
import org.xpect.expectation.impl.TargetSyntaxSupport.TargetLiteralSupport;
import org.xpect.util.ReflectionUtil;

import com.google.common.base.Function;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class ActualCollection extends StringCollection<ActualItem> {

	public class ActualItem extends StringCollection<ActualItem>.Item {
		public ActualItem(String pure) {
			super(pure);
		}
	}

	public static class ToString implements Function<Object, String> {
		public String apply(Object from) {
			return from == null ? "null" : from.toString();
		}
	}

	private Function<Object, String> itemFormatter;

	private TargetLiteralSupport targetLiteralSupport;

	public Function<Object, String> getItemFormatter() {
		return itemFormatter;
	}

	public TargetLiteralSupport getTargetLiteralSupport() {
		return targetLiteralSupport;
	}

	public void init(Iterable<?> actual) {
		items = createCollection();
		for (Object obj : actual) {
			String string = itemFormatter.apply(obj);
			String escaped = targetLiteralSupport.escape(string);
			items.add(new ActualItem(escaped));
		}
	}

	@Deprecated
	public void init(Iterable<?> actual, Class<? extends Function<Object, String>> functionClass) {
		setItemFormatter(ReflectionUtil.newInstanceUnchecked(functionClass));
		init(actual);
	}

	public void init(String... actual) {
		items = createCollection();
		for (String obj : actual)
			items.add(new ActualItem(obj));
	}

	public void setItemFormatter(Function<Object, String> itemFormatter) {
		this.itemFormatter = itemFormatter;
	}

	public void setTargetLiteralSupport(TargetLiteralSupport targetLiteralSupport) {
		this.targetLiteralSupport = targetLiteralSupport;
	}
}
