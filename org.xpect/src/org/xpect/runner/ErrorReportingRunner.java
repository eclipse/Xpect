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

package org.xpect.runner;

import static org.xpect.runner.DescriptionFactory.createFileDescriptionForError;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public class ErrorReportingRunner extends Runner {
	private final List<Throwable> causes;
	private final XpectRunner runner;
	private final URI uri;

	public ErrorReportingRunner(XpectRunner runner, URI uri, Throwable cause) {
		this.runner = runner;
		this.uri = uri;
		causes = getCauses(cause);
	}

	private Description describeCause(Throwable child) {
		return Description.createTestDescription(runner.getTestClass().getJavaClass(), "initializationError");
	}

	private List<Throwable> getCauses(Throwable cause) {
		if (cause instanceof InvocationTargetException)
			return getCauses(cause.getCause());
		if (cause instanceof InitializationError)
			return ((InitializationError) cause).getCauses();
		return Arrays.asList(cause);
	}

	@Override
	public Description getDescription() {
		Description description = createFileDescriptionForError(runner.getTestClass().getJavaClass(), runner.getUriProvider(), uri);
		for (Throwable each : causes)
			description.addChild(describeCause(each));
		return description;
	}

	@Override
	public void run(RunNotifier notifier) {
		for (Throwable each : causes) {
			runCause(each, notifier);
		}
	}

	private void runCause(Throwable child, RunNotifier notifier) {
		Description description = describeCause(child);
		notifier.fireTestStarted(description);
		notifier.fireTestFailure(new Failure(description, child));
		notifier.fireTestFinished(description);
	}
}