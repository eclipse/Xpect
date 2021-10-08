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

import org.eclipse.xtext.util.internal.FormattingMigrator;
import org.junit.ComparisonFailure;
import org.eclipse.xpect.XpectArgument;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.expectation.IStringExpectation;
import org.eclipse.xpect.expectation.StringExpectation;
import org.eclipse.xpect.setup.XpectSetupFactory;
import org.eclipse.xpect.state.Creates;
import org.eclipse.xpect.text.Text;

import com.google.common.base.Joiner;

@XpectSetupFactory
@SuppressWarnings("restriction")
@XpectImport(ExpectationRegionProvider.class)
public class StringExpectationImpl extends AbstractExpectation implements IStringExpectation {

	private final StringExpectation annotation;

	public StringExpectationImpl(XpectArgument argument, TargetSyntaxSupport targetSyntax) {
		super(argument, targetSyntax);
		this.annotation = argument.getAnnotationOrDefault(StringExpectation.class);
	}

	public void assertEquals(Object string) {
		if (string == null)
			throw new NullPointerException("Object is null");
		String originalExpectation = getExpectation();
		String nl = new Text(originalExpectation).getNL();
		String actual = string.toString();
		String actualWithNL = Joiner.on(nl).join(new Text(actual).splitIntoLines());
		String escapedActual = getTargetSyntaxLiteral().escape(actualWithNL);
		String migratedExpectation;
		if (annotation.whitespaceSensitive()) {
			if (annotation.newLineCharacterSensitive()) {
				migratedExpectation = originalExpectation;
			} else {
				migratedExpectation = originalExpectation.replaceAll("\\r?\\n", "\n");
			}
		} else {
			FormattingMigrator migrator = new FormattingMigrator();
			migratedExpectation = migrator.migrate(escapedActual, originalExpectation);
		}
		if ((annotation.caseSensitive() && !migratedExpectation.equals(escapedActual)) || (!annotation.caseSensitive() && !migratedExpectation.equalsIgnoreCase(escapedActual))) {
			String expDoc = replaceInDocument(migratedExpectation);
			String actDoc = replaceInDocument(escapedActual);
			throw new ComparisonFailure("", expDoc, actDoc);
		}
	}

	public StringExpectation getAnnotation() {
		return annotation;
	}

	@Creates
	public IStringExpectation create() {
		return this;
	}

}