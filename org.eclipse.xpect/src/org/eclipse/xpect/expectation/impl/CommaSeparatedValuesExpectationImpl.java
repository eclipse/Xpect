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

package org.eclipse.xpect.expectation.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.xtext.util.Pair;
import org.junit.ComparisonFailure;
import org.eclipse.xpect.XpectArgument;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.expectation.CommaSeparatedValuesExpectation;
import org.eclipse.xpect.expectation.ICommaSeparatedValuesExpectation;
import org.eclipse.xpect.expectation.impl.ActualCollection.ActualItem;
import org.eclipse.xpect.expectation.impl.ExpectationCollection.ExpectationItem;
import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.text.Text;
import org.eclipse.xpect.util.ReflectionUtil;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@XpectSetupFactory
@XpectImport(ExpectationRegionProvider.class)
public class CommaSeparatedValuesExpectationImpl extends AbstractExpectation implements ICommaSeparatedValuesExpectation {
	protected static class ValueImpl implements ICommaSeparatedValuesExpectation.Value {

		private final ExpectationCollection.ExpectationItem delegate;

		public ValueImpl(ExpectationItem delegate) {
			super();
			this.delegate = delegate;
		}

		public String getText() {
			return delegate.getPure();
		}

		public boolean isNegated() {
			return delegate.isNegated();
		}

		public boolean isWildcard() {
			return delegate.isWildcard();
		}

		@Override
		public String toString() {
			ToStringHelper helper = Objects.toStringHelper(ICommaSeparatedValuesExpectation.Value.class);
			helper.add("text", getText()).add("negated", isNegated()).add("wildcard", isWildcard());
			return helper.toString();
		}
	}

	private final CommaSeparatedValuesExpectation annotation;
	private final ExpectationCollection expectationCollection;

	public CommaSeparatedValuesExpectationImpl(CommaSeparatedValuesExpectationImpl original, CommaSeparatedValuesExpectation config) {
		super(original.getArgument(), original.getTargetSyntax());
		this.annotation = config;
		this.expectationCollection = createExpectationCollection();
	}

	public CommaSeparatedValuesExpectationImpl(XpectArgument argument, TargetSyntaxSupport syntax) {
		super(argument, syntax);
		this.annotation = argument.getAnnotationOrDefault(CommaSeparatedValuesExpectation.class);
		this.expectationCollection = createExpectationCollection();
	}

	public void assertEquals(Iterable<?> actual) {
		assertEquals(actual, null);
	}

	public void assertEquals(Iterable<?> actual, Predicate<String> predicate) {
		ActualCollection act = new ActualCollection();
		act.setTargetLiteralSupport(getTargetSyntaxLiteral());
		act.setCaseSensitive(annotation.caseSensitive());
		act.setOrdered(annotation.ordered());
		act.setQuoted(annotation.quoted());
		act.setSeparator(',');
		act.setWhitespaceSensitive(annotation.whitespaceSensitive());
		act.setItemFormatter(ReflectionUtil.newInstanceUnchecked(annotation.itemFormatter()));
		if (actual != null && predicate != null) {
			if (expectationCollection.isWildcard())
				act.init(expectationCollection.applyPredicate(predicate));
			else
				act.init(actual);
		} else if (predicate != null)
			act.init(expectationCollection.applyPredicate(predicate));
		else if (actual != null)
			act.init(actual);
		else
			throw new NullPointerException();

		String nl = new Text(this.getRegion().getDocument()).getNL();
		if (!expectationCollection.matches(act)) {
			StringBuilder expString = new StringBuilder();
			StringBuilder actString = new StringBuilder();
			boolean expWrap = false;
			boolean expEmpty = false;
			boolean actWrap = false;
			int lineLength = 0, lineCount = 0;
			for (Pair<Collection<ExpectationItem>, ActualItem> pair : expectationCollection.map(act)) {
				String expItem = null;
				String actItem = null;
				if (pair.getFirst() != null && !pair.getFirst().isEmpty()) {
					if (pair.getSecond() != null)
						expItem = pair.getSecond().getEscaped();
					else
						expItem = pair.getFirst().iterator().next().getEscaped();
				} else {
					if (pair.getSecond() != null)
						expItem = str(pair.getSecond().getEscaped().length());
				}
				if (pair.getSecond() != null) {
					actItem = pair.getSecond().getEscaped();
					lineCount++;
					lineLength += actItem.length() + 2;
					boolean count = annotation.maxItemsPerLine() > 0 && lineCount > annotation.maxItemsPerLine();
					boolean width = annotation.maxLineWidth() > 0 && lineLength > annotation.maxLineWidth();
					if (count || width)
						expWrap = actWrap = true;
				}
				if (expItem != null && expString.length() > 0) {
					if (expWrap) {
						expString.append(expEmpty ? nl : "," + nl);
						expWrap = false;
					} else
						expString.append(expEmpty ? "  " : ", ");
				}
				if (actItem != null && actString.length() > 0) {
					if (actWrap) {
						actString.append("," + nl);
						actWrap = false;
						lineCount = 0;
						lineLength = 0;
					} else
						actString.append(", ");
				}
				if (expItem != null) {
					expString.append(expItem);
					expEmpty = expItem.trim().length() == 0;
				}
				if (actItem != null)
					actString.append(actItem);
			}
			String expDoc = replaceInDocument(expString.toString());
			String actDoc = replaceInDocument(actString.toString());
			throw new ComparisonFailure("", expDoc, actDoc);
		}
	}

	public void assertEquals(Predicate<String> predicate) {
		assertEquals(null, predicate);
	}

	@Creates
	public ICommaSeparatedValuesExpectation create() {
		return this;
	}

	protected ExpectationCollection createExpectationCollection() {
		ExpectationCollection exp = new ExpectationCollection();
		exp.setCaseSensitive(annotation.caseSensitive());
		exp.setOrdered(annotation.ordered());
		exp.setQuoted(annotation.quoted());
		exp.setSeparator(',');
		exp.setWhitespaceSensitive(annotation.whitespaceSensitive());
		exp.init(getExpectation());
		return exp;
	}

	public CommaSeparatedValuesExpectation getAnnotation() {
		return annotation;
	}

	public List<Value> getExpectedValues() {
		List<Value> result = Lists.newArrayList();
		for (ExpectationItem item : expectationCollection)
			result.add(new ValueImpl(item));
		return ImmutableList.copyOf(result);
	}

	protected String str(int length) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < length; i++)
			b.append(" ");
		return b.toString();
	}

}