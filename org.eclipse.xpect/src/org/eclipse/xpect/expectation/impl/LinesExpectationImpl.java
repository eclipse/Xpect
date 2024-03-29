/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.eclipse.xpect.expectation.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.xtext.util.Pair;
import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.eclipse.xpect.XpectArgument;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.expectation.ILinesExpectation;
import org.eclipse.xpect.expectation.LinesExpectation;
import org.eclipse.xpect.expectation.impl.ActualCollection.ActualItem;
import org.eclipse.xpect.expectation.impl.ExpectationCollection.ExpectationItem;
import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.text.Text;
import org.eclipse.xpect.util.ReflectionUtil;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

@XpectSetupFactory
@XpectImport(ExpectationRegionProvider.class)
public class LinesExpectationImpl extends AbstractExpectation implements ILinesExpectation {

	private final LinesExpectation annotation;

	public LinesExpectationImpl(XpectArgument argument, TargetSyntaxSupport targetSyntax) {
		super(argument, targetSyntax);
		this.annotation = argument.getAnnotationOrDefault(LinesExpectation.class);
	}

	public void assertEquals(Iterable<?> actual) {
		assertEquals("", actual);
	}

	public void assertEquals(String message, Iterable<?> actual) {
		Assert.assertNotNull(actual);

		ExpectationCollection exp = new ExpectationCollection();
		exp.setCaseSensitive(annotation.caseSensitive());
		exp.setOrdered(annotation.ordered());
		exp.setQuoted(annotation.quoted());
		exp.setSeparator('\n');
		exp.setWhitespaceSensitive(annotation.whitespaceSensitive());
		exp.setExpectationFormatter(ReflectionUtil.newInstanceUnchecked(annotation.expectationFormatter()));
		exp.init(getExpectation());

		ActualCollection act = new ActualCollection();
		act.setTargetLiteralSupport(getTargetSyntaxLiteral());
		act.setCaseSensitive(annotation.caseSensitive());
		act.setOrdered(annotation.ordered());
		act.setQuoted(annotation.quoted());
		act.setSeparator('\n');
		act.setWhitespaceSensitive(annotation.whitespaceSensitive());
		act.setItemFormatter(ReflectionUtil.newInstanceUnchecked(annotation.itemFormatter()));
		act.init(actual);

		if (!exp.matches(act)) {
			List<String> expString = Lists.newArrayList();
			List<String> actString = Lists.newArrayList();
			for (Pair<Collection<ExpectationItem>, ActualItem> pair : exp.map(act)) {
				if (pair.getFirst() != null && !pair.getFirst().isEmpty()) {
					if (pair.getSecond() != null)
						expString.add(pair.getSecond().getEscaped());
					else
						expString.add(pair.getFirst().iterator().next().getEscaped());
				}
				if (pair.getSecond() != null)
					actString.add(pair.getSecond().getEscaped());
			}
			String nl = new Text(getRegion().getDocument()).getNL();
			String expDoc = replaceInDocument(Joiner.on(nl).join(expString));
			String actDoc = replaceInDocument(Joiner.on(nl).join(actString));
			throw new ComparisonFailure(message, expDoc, actDoc);
		}
	}

	@Creates
	public ILinesExpectation create() {
		return this;
	}

	public LinesExpectation getAnnotation() {
		return annotation;
	}
}