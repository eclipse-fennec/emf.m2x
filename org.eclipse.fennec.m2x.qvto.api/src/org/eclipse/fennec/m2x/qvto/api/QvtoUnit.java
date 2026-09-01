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
package org.eclipse.fennec.m2x.qvto.api;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import java.util.Objects;

import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.unit.api.Unit;

/**
 * A resolved QVT-O compilation unit, either as source text or a pre-compiled AST.
 *
 * <p>This is a sealed interface with three permitted implementations:
 * <ul>
 *   <li>{@link SourceUnit} — source text to be parsed</li>
 *   <li>{@link CompiledUnit} — an already-parsed transformation AST</li>
 *   <li>{@link ResourceUnit} — a location to load the unit from at consumption</li>
 * </ul>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public sealed interface QvtoUnit extends Unit permits QvtoUnit.SourceUnit, QvtoUnit.CompiledUnit, QvtoUnit.ResourceUnit {

	/**
	 * Returns the qualified name of this unit.
	 *
	 * @return the qualified name, never {@code null}
	 */
	String qualifiedName();

	/**
	 * A source unit containing QVT-O text to be parsed.
	 *
	 * @param qualifiedName the qualified unit name
	 * @param uri the location of the source
	 * @param source the source text
	 */
	record SourceUnit(String qualifiedName, URI uri, String source) implements QvtoUnit, Unit.Source {
		public SourceUnit {
			Objects.requireNonNull(qualifiedName, "qualifiedName must not be null");
			Objects.requireNonNull(uri, "uri must not be null");
			Objects.requireNonNull(source, "source must not be null");
		}
	}

	/**
	 * A pre-compiled unit containing an already-parsed transformation AST.
	 *
	 * @param qualifiedName the qualified unit name
	 * @param transformation the parsed transformation
	 */
	record CompiledUnit(String qualifiedName, OperationalTransformation transformation) implements QvtoUnit, Unit.Compiled {
		public CompiledUnit {
			Objects.requireNonNull(qualifiedName, "qualifiedName must not be null");
			Objects.requireNonNull(transformation, "transformation must not be null");
		}

		@Override
		public EObject root() {
			return transformation;
		}
	}

	/**
	 * A unit named by reference: a resolver answers with where it lives, and loading happens at
	 * consumption, in the consumer's context (#214). The URI is expected to hold a compiled-unit
	 * document — then this is a store load without the store, validation funnel included — or a
	 * bare AST, the pre-unit shape.
	 *
	 * @param qualifiedName the qualified unit name
	 * @param uri where the unit lives
	 */
	record ResourceUnit(String qualifiedName, URI uri) implements QvtoUnit, Unit.Referenced {
		public ResourceUnit {
			Objects.requireNonNull(qualifiedName, "qualifiedName must not be null");
			Objects.requireNonNull(uri, "uri must not be null");
		}
	}
}
