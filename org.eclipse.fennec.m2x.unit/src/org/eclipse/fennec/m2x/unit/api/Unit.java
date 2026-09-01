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
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.osgi.annotation.versioning.ConsumerType;

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
 * <p>A unit is {@link Source}, {@link Compiled} or {@link Referenced} — the nested names keep
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
@ConsumerType
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
	 * A unit named by reference: a resolver answers with where it lives, and loading happens at
	 * consumption, in the consumer's context, through the {@code UnitMaterializer} (#214).
	 *
	 * <p>This is deliberately not a {@link Compiled}: {@code root()} cannot be answered before
	 * loading. The URI is expected to hold a compiled-unit document — the normal case, with the
	 * validation funnel and the carried copies of a store load — or a bare AST, the pre-unit
	 * shape, which loads with its known gaps but binds consistently in the consumer's context.
	 * A resolution conflict cannot compare a reference by fingerprint before loading, so among
	 * agreeing answers a reference never decides (#141).
	 *
	 * @author Data In Motion Consulting
	 * @since 1.0
	 */
	interface Referenced extends Unit {

		/**
		 * Returns where the unit lives.
		 *
		 * @return the location, never {@code null}
		 */
		URI uri();

		@Override
		default UnitKind kind() {
			return UnitKind.COMPILED;
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

	/**
	 * A compiled unit in its document form: the AST together with the manifest, the satellites,
	 * the embedded dependencies and the package copies — what {@code compile()} produces and what
	 * a store holds and hands back.
	 *
	 * <p>A bare {@link Compiled} AST says what a unit is; the document also says what it was built
	 * against and carries the unit fingerprint its manifest was sealed with. A store accepts only
	 * this form for compiled units — a bare AST has no manifest to store it by.
	 *
	 * @author Data In Motion Consulting
	 * @since 1.0
	 */
	interface Packaged extends Compiled {
		/**
		 * Returns the compiled-unit document.
		 *
		 * @return the document, never {@code null}
		 */
		CompiledUnit document();

		@Override
		default EObject root() {
			return document().getUnit();
		}
	}
}
