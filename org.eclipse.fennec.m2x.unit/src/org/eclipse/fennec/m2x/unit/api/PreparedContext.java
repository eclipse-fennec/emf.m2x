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

import java.util.Collection;
import java.util.Optional;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.osgi.annotation.versioning.ProviderType;

/**
 * The hand-over value between Prepare and Execute: everything an engine needs to
 * run a unit, with nothing left to resolve.
 *
 * <p>The three phases are Compile, Prepare and Execute. Resolvers are asked in
 * the first two and <b>never</b> in the third — a missing package, a fingerprint
 * mismatch or a missing blackbox fails during Prepare, with a diagnostic, rather
 * than in the middle of a run (§4 of the compiled-unit concept). A prepared
 * context is what makes that testable: an engine handed one has nothing left to
 * look up.
 *
 * <p>The context belongs to the prepare run and is shared by a whole pipeline,
 * not created per unit — otherwise the output of one transformation is foreign to
 * the next, which is the case prepare exists for. It can be reused across many
 * executions, which requires the ASTs it holds to stay unchanged while they are
 * in use: a shared context cannot tolerate a tree that rearranges itself, the
 * second reason type references must not become containments.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface PreparedContext {

	/**
	 * Returns the resource set every unit and every URI-addressed model in this
	 * context is loaded in.
	 *
	 * @return the resource set, never {@code null}
	 */
	ResourceSet resourceSet();

	/**
	 * Returns the package registry the units were verified against.
	 *
	 * <p>Per package, the runtime instance wins where its fingerprint matches the
	 * one recorded for the compiled unit, so generated code stays in play; where no
	 * runtime instance exists, the copy carried by the unit applies. A matching
	 * nsURI with a differing fingerprint is a hard failure at prepare time, not a
	 * silent fallback (§6 of the concept).
	 *
	 * @return the registry, never {@code null}
	 */
	EPackage.Registry packageRegistry();

	/**
	 * Returns a prepared unit by qualified name.
	 *
	 * @param qualifiedName the qualified unit name
	 * @return the unit, or empty if this context does not hold it
	 */
	Optional<Unit> unit(String qualifiedName);

	/**
	 * Returns every unit this context holds — the ones asked for and their
	 * transitive dependencies.
	 *
	 * @return the units, never {@code null}
	 */
	Collection<Unit> units();
}
