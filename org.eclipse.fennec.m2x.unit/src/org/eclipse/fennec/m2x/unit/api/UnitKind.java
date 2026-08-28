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

/**
 * Whether a unit carries source text or a compiled AST.
 *
 * <p>A {@link UnitStore} holds both kinds side by side, so the kind is part of
 * the key rather than a property of the stored value (§5.5 of the compiled-unit
 * concept). The two kinds also carry different fingerprints: a source has no AST
 * to canonicalize before it is parsed.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public enum UnitKind {

	/** Source text that still has to be parsed. */
	SOURCE,

	/** An already parsed AST, ready to be linked or executed. */
	COMPILED;

	/**
	 * Returns the lower-case tag used to build a store type id, {@code "source"}
	 * or {@code "compiled"}.
	 *
	 * @return the tag, never {@code null}
	 */
	public String tag() {
		return name().toLowerCase();
	}
}
