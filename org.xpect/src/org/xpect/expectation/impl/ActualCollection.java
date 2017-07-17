package org.xpect.expectation.impl;

import org.eclipse.xtext.util.Exceptions;
import org.xpect.expectation.impl.ActualCollection.ActualItem;
import org.xpect.expectation.impl.TargetSyntaxSupport.TargetLiteralSupport;

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

	private TargetLiteralSupport targetLiteralSupport;

	public TargetLiteralSupport getTargetLiteralSupport() {
		return targetLiteralSupport;
	}

	public void init(Iterable<?> actual, Class<? extends Function<Object, String>> functionClass) {
		items = createCollection();
		try {
			Function<Object, String> func = functionClass.newInstance();
			for (Object obj : actual) {
				String string = func.apply(obj);
				String escaped = targetLiteralSupport.escape(string);
				items.add(new ActualItem(escaped));
			}
		} catch (InstantiationException e) {
			Exceptions.throwUncheckedException(e);
		} catch (IllegalAccessException e) {
			Exceptions.throwUncheckedException(e);
		}
	}

	public void init(String... actual) {
		items = createCollection();
		for (String obj : actual)
			items.add(new ActualItem(obj));
	}

	public void setTargetLiteralSupport(TargetLiteralSupport targetLiteralSupport) {
		this.targetLiteralSupport = targetLiteralSupport;
	}
}
