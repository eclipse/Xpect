package org.xpect.setup;

import java.util.EnumSet;

import org.xpect.Environment;
import org.xpect.state.Precondition;

/**
 * Deprecated, use {@link Precondition} to determine in which environment a setup may be applied.
 * Subclasses may still be used as they provide other additionally functionality.
 */
public interface IXpectSetup {

	EnumSet<Environment> getEnvironments();

}
