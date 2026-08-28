/*
 * ******************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 * ******************************************************************
 */
package org.eclipse.fennec.m2x.unit.api;

import java.util.Objects;

import org.eclipse.fennec.m2x.model.compiled.DependencyMode;

/**
 * How a compile binds the dependencies of a unit (§3.1 of the compiled-unit concept).
 *
 * <p>The mode is chosen per compile and recorded in the manifest, so that prepare can read how
 * a unit was built instead of guessing. {@link DependencyMode#PIN} is the default: reproducible
 * while the pinned version stays resolvable, and a recompile picks up a fix. {@code embed} is
 * the deliberate choice for transport and archiving — the unit carries its dependencies and
 * needs no resolver to run — and {@code rebind} the deliberate choice for a unit that should
 * follow its libraries, at the price that prepare has to record what it actually bound.
 *
 * @param dependencyMode how imports are bound, never {@code null}
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record UnitCompileOptions(DependencyMode dependencyMode) {

	public UnitCompileOptions {
		Objects.requireNonNull(dependencyMode, "dependencyMode must not be null");
	}

	/**
	 * The default options: dependencies pinned.
	 *
	 * @return the defaults, never {@code null}
	 */
	public static UnitCompileOptions defaults() {
		return new UnitCompileOptions(DependencyMode.PIN);
	}

	/**
	 * Options binding every dependency in the given mode.
	 *
	 * @param mode the mode
	 * @return the options, never {@code null}
	 */
	public static UnitCompileOptions of(DependencyMode mode) {
		return new UnitCompileOptions(mode);
	}
}
