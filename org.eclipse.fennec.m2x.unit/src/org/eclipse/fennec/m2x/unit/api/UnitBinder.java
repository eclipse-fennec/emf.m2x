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

import java.util.Map;

import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * The language-specific half of Prepare: binding a loaded unit to its loaded dependencies and
 * checking that the runtime has the blackboxes the unit requires.
 *
 * <p>Loading, verifying and following the dependency closure are the same for every language and
 * live in the preparer; what an import <em>is</em> differs — QVT-O binds a stub module, QVT-R
 * merges relations, MOFM2T links {@code extends} and {@code imports} — and that is what an engine
 * supplies here. After {@link #bind} a unit has nothing left to resolve: an engine handed the
 * prepared context asks no resolver, which is the rule Execute lives by.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface UnitBinder {

	/**
	 * Returns the language tag this binder serves, e.g. {@code qvto}.
	 *
	 * @return the language tag, never {@code null}
	 */
	String language();

	/**
	 * Checks a loaded unit for what the language's parser would have guaranteed by construction —
	 * the root is a unit of this language, every variable use has its declaration in the document,
	 * every invocation is bound or recorded for binding — before prepare binds it (#142).
	 *
	 * <p>The neutral shape of the document (manifest, closure, Ecore structure, size, fingerprint)
	 * is checked by the store's {@code UnitValidator}; this is the language's half. The default
	 * checks nothing.
	 *
	 * @param unit the loaded unit
	 * @throws UnitPrepareException if the unit is not well-formed for this language
	 */
	default void validate(CompiledUnit unit) throws UnitPrepareException {
	}

	/**
	 * Binds a unit to its dependencies, all loaded into one context.
	 *
	 * @param unit the unit to bind — a loaded copy, the binder may change it
	 * @param dependencies the loaded dependencies by qualified name, every name the unit's
	 *            manifest lists under pin or rebind
	 * @throws UnitPrepareException if the unit cannot be bound
	 */
	void bind(CompiledUnit unit, Map<String, CompiledUnit> dependencies) throws UnitPrepareException;

	/**
	 * Checks that the runtime has what the unit's blackbox requirements name.
	 *
	 * @param unit the unit
	 * @throws UnitPrepareException if a required blackbox is missing or has a different shape
	 */
	void verifyBlackboxes(CompiledUnit unit) throws UnitPrepareException;
}
