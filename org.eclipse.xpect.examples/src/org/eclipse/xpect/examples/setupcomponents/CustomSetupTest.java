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

package org.eclipse.xpect.examples.setupcomponents;

import java.util.List;

import org.junit.runner.RunWith;
import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.examples.setupcomponents.CustomSetupTest.CustomSetupComponent;
import org.eclipse.xpect.examples.setupcomponents.CustomSetupTest.CustomSetupRoot;
import org.eclipse.xpect.expectation.IStringExpectation;
import org.eclipse.xpect.expectation.StringExpectation;
import org.eclipse.xpect.runner.Xpect;
import org.eclipse.xpect.runner.XpectRunner;
import org.eclipse.xpect.setup.ISetupInitializer;
import org.eclipse.xpect.setup.XpectSetupComponent;
import org.eclipse.xpect.setup.XpectSetupRoot;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * Import classes via @XpectImport; The imported classes can import further
 * classes and/or have an annotation that defines their "role". Here, we see the
 * roles @XpectSetupRoot and @XpectSetupComponent.
 */
@RunWith(XpectRunner.class)
@XpectImport({ CustomSetupRoot.class, CustomSetupComponent.class })
public class CustomSetupTest {

	/**
	 * XpectSetupComponents can be used (referenced) in the XPECT_SETUP section.
	 * They can have constructor parameters, add(Foo)-methods and
	 * set(Foo)-methods.
	 */
	@XpectSetupComponent
	public static class CustomSetupComponent {
		private final String bar;
		private String baz = "defaultBaz";
		private List<CustomSetupComponent> children = Lists.newArrayList();
		private final String foo;

		public CustomSetupComponent() {
			this.foo = "defaultFoo";
			this.bar = "defaultBar";
		}

		public CustomSetupComponent(String foo, String bar) {
			this.foo = foo;
			this.bar = bar;
		}

		public void add(CustomSetupComponent child) {
			children.add(child);
		}

		public void setBaz(String baz) {
			this.baz = baz;
		}

		public void setMyChild(CustomSetupComponent child) {
			children.add(child);
		}

		public void addMyChildren(CustomSetupComponent child) {
			children.add(child);
		}

		@Override
		public String toString() {
			String self = getClass().getSimpleName() + " foo=" + foo + " bar=" + bar + " baz=" + baz;
			if (children.isEmpty())
				return self;
			return self + " {\n  " + Joiner.on("\n").join(children).replace("\n", "\n  ") + "\n}";
		}
	}

	/**
	 * A class annotated with @XpectSetupRoot should have add(Foo) methods. All
	 * types that are a valid parameter for such an add()-method and which are
	 * annotated with @XpectSetupComponent are valid syntactic root components
	 * in the XPECT_SETUP section.
	 */
	@XpectSetupRoot
	public static class CustomSetupRoot {
		private List<CustomSetupComponent> children = Lists.newArrayList();

		public void add(CustomSetupComponent child) {
			children.add(child);
		}
	}

	/**
	 * The ISetupInitializer can initialize an instance of a class annotated
	 * with @XpectSetupRoot with all the values defined in the XPECT_SETUP
	 * section.
	 */
	@Xpect
	public void mysetup(@StringExpectation IStringExpectation expectation, ISetupInitializer<CustomSetupRoot> init) {
		CustomSetupRoot root = new CustomSetupRoot();
		init.initialize(root);
		String actual = Joiner.on(", ").join(root.children);
		expectation.assertEquals(actual);
	}

}
