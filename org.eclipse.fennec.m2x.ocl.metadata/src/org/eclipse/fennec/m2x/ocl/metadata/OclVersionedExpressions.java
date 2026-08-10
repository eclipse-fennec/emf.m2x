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
package org.eclipse.fennec.m2x.ocl.metadata;

import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.osgi.annotation.versioning.ProviderType;

/**
 * What a cache can answer once its entries are anchored to a model version — the two
 * questions an {@link org.eclipse.fennec.m2x.ocl.api.OclExpressionCache} alone cannot.
 *
 * <p>It exists as its own interface so that neither question drags a dependency along.
 * Anchor lookup is what the metadata bridge needs, and only the bridge's adapter mentions
 * bridge types; releasing a version is what a model going away needs, and the code that
 * notices that should not have to know which cache implementation is behind the service.
 *
 * @since 1.0
 */
@ProviderType
public interface OclVersionedExpressions {

	/**
	 * The class an entry belongs to — the context type its expression was compiled against.
	 *
	 * <p>Answered from what this cache filed itself, without asking any registry of models:
	 * an entry may well be older than the model version it belongs to, and a lookup would
	 * find nothing at exactly the moment the answer matters — while a model version's tree is
	 * being built, before it is published.
	 *
	 * @param registryKey the key the entry was filed under, may be {@code null}
	 * @return the context type, or empty if this cache did not file that key, or filed it for
	 *     a classifier that is not an {@link EClass}
	 */
	Optional<EClass> anchorOf(String registryKey);

	/**
	 * Drops everything compiled against one model version.
	 *
	 * <p>Called when that version goes: what was compiled against it holds its classes and
	 * features, so it describes nothing once the version is gone. Which entries those are is
	 * decided by the model fingerprint, so a second, still live version of the same nsURI
	 * keeps everything of its own.
	 *
	 * <p>The fingerprint is computed here rather than passed in, so that the arithmetic of
	 * model identity lives in one place and a caller cannot supply a value from somewhere
	 * else — an advertised fingerprint is context, never truth.
	 *
	 * @param ePackage the departing model version, may be {@code null}, in which case nothing
	 *     is dropped
	 * @return how many entries were dropped
	 */
	int release(EPackage ePackage);
}
