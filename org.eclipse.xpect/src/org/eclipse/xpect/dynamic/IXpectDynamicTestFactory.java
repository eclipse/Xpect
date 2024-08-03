package org.eclipse.xpect.dynamic;

import java.util.stream.Stream;

import org.eclipse.xpect.XpectImport;
import org.eclipse.xpect.runner.TestTitleProvider;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

@XpectImport(TestTitleProvider.class)
public interface IXpectDynamicTestFactory {

	@TestFactory
	default Stream<DynamicNode> tests() {
		return XpectDynamicTestFactory.xpectTests(getClass());
	}
}
