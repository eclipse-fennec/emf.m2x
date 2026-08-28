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

import java.util.Optional;

import org.osgi.annotation.versioning.ConsumerType;

/**
 * Resolves QVT-O compilation units by qualified name.
 *
 * <p>In standalone mode, register resolvers via the engine configuration's
 * {@code addUnitResolver(...)}. In OSGi, publish implementations as whiteboard services (D27),
 * registered under the unit-name property, and rank them with {@code service.ranking} where
 * several may answer for one name.
 *
 * <p><b>Several sources (#141).</b> Every reachable resolver is asked for a name, not only until
 * the first answer: configured resolvers in configuration order, whiteboard services by ranking
 * (highest first), class-path providers in declaration order. The first answer in that order is
 * taken — but two answers with different content for one name are a conflict and end the
 * resolution with a {@code UnitResolutionException}, as does a resolver that throws. A failure is
 * never turned into an empty answer: "the store is broken" must not read as "not found" while a
 * stale copy from the next source steps in.
 *
 * <p><b>Ownership.</b> A unit a resolver hands out is lent, not given away. An imported library
 * lends its types to everyone who imports it, so the receiver must not mutate the AST — the
 * compiler copies what it embeds ({@code UnitPackager.detach}), and the execute-time linker binds
 * inside the <em>importing</em> unit only. A resolver may therefore hand out the same instance
 * again and again; a resolver that hands out a fresh copy each time is free to do so.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface QvtoUnitResolver {

	/**
	 * Resolves a compilation unit by its qualified name.
	 *
	 * @param qualifiedName the dot-separated qualified name
	 * @return the resolved unit, or empty if this resolver does not have it
	 * @throws org.eclipse.fennec.m2x.unit.api.UnitResolutionException if this resolver cannot
	 *             answer — a broken store, an unreadable unit; never for "not found"
	 */
	Optional<QvtoUnit> resolveUnit(String qualifiedName);
}
