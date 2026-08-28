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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

/**
 * A compilation unit of one of the m2x languages, addressed by qualified name.
 *
 * <p>This is the language-neutral base of {@code QvtoUnit}, {@code QvtdUnit} and
 * {@code M2tUnit}. Those three stay the source of truth for their language and
 * remain sealed; they extend this interface so that a store, a fingerprint
 * mechanism or a prepared context can be written once instead of three times
 * (§8 of the compiled-unit concept). The dependency points from the language
 * APIs to this bundle and never back, so no cycle arises (D39).
 *
 * <p>A unit is either {@link Source} or {@link Compiled} — the nested names keep
 * this interface free of a collision with the language-level record names such
 * as {@code QvtoUnit.SourceUnit}.
 *
 * <p><b>Ownership.</b> A unit handed out by a resolver or a store is borrowed,
 * not given away: an imported library lends its types to everyone that imports
 * it, so a caller must not mutate the AST it receives. That is also why type
 * references stay non-containment — exclusive containment would let one owner
 * steal a type from another (§2 of the concept, OCL v2.4 §8.3).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public interface Unit {

	/**
	 * Returns the qualified name this unit is addressed by.
	 *
	 * @return the dot-separated qualified name, never {@code null}
	 */
	String qualifiedName();

	/**
	 * Returns whether this unit carries source text or a compiled AST.
	 *
	 * @return the kind, never {@code null}
	 */
	UnitKind kind();

	/**
	 * A unit that carries source text still to be parsed.
	 *
	 * @author Data In Motion Consulting
	 * @since 1.0
	 */
	interface Source extends Unit {

		/**
		 * Returns where the source was read from.
		 *
		 * @return the source location, never {@code null}
		 */
		URI uri();

		/**
		 * Returns the source text.
		 *
		 * @return the source text, never {@code null}
		 */
		String source();

		@Override
		default UnitKind kind() {
			return UnitKind.SOURCE;
		}
	}

	/**
	 * A unit that carries an already parsed AST.
	 *
	 * @author Data In Motion Consulting
	 * @since 1.0
	 */
	interface Compiled extends Unit {

		/**
		 * Returns the root of the parsed AST — an {@code OperationalTransformation},
		 * a {@code RelationalTransformation} or a MOFM2T {@code Module}, depending on
		 * the language.
		 *
		 * <p>The returned object is borrowed, see the ownership note on {@link Unit}.
		 *
		 * @return the AST root, never {@code null}
		 */
		EObject root();

		@Override
		default UnitKind kind() {
			return UnitKind.COMPILED;
		}
	}
}
