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

import org.eclipse.fennec.emf.osgi.metadata.MetadataHandler;
import org.eclipse.fennec.emf.osgi.model.metadata.PackageMetadata;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * Compiles a model's own OCL when the model registers.
 *
 * <p>Registered as a {@link MetadataHandler}, so the metadata service calls it for every
 * package it takes in. What the package declares — invariants, derivations, operation bodies
 * — is compiled once and filed under that model version, and the delegates find compiled
 * expressions from then on instead of strings in annotations.
 *
 * <p>The cache is selected by name, because filing under a model version is the whole point
 * and an LRU could not do it:
 *
 * <pre>
 * "OclConstraintPrecompiler": { "cache.target": "(cache.name=metadata)" }
 * </pre>
 *
 * <p>Composes the precompiler rather than extending it — a component is wiring.
 *
 * @since 1.0
 */
@Component(name = "OclConstraintPrecompiler", service = MetadataHandler.class)
public class OclConstraintPrecompilerComponent implements MetadataHandler {

	private final OclConstraintPrecompiler precompiler;

	@Activate
	public OclConstraintPrecompilerComponent(
			@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED) OclExpressionParser parser,
			@Reference(name = "cache") OclExpressionCache cache) {
		this.precompiler = new OclConstraintPrecompiler(parser, cache);
	}

	@Override
	public void onPackageRegistered(PackageMetadata packageMetadata) {
		precompiler.onPackageRegistered(packageMetadata);
	}
}
