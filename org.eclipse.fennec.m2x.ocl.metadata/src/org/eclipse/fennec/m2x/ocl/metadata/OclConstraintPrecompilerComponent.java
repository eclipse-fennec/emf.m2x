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

import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * Compiles a model's own OCL when the model arrives, and drops it when the model goes.
 *
 * <p>The trigger is the {@code EPackage} service: one per live model version, each carrying its
 * own instance of the classes the expressions will hold. That is deliberately not
 * {@code MetadataHandler.onPackageRegistered} — a version's tree is published only after all
 * handlers ran, so pushing from inside one makes placement depend on wiring order. See
 * {@link OclConstraintPrecompiler} for the reasoning.
 *
 * <p>The cache is selected by name, because filing under a model version is the whole point
 * and an LRU could not do it:
 *
 * <pre>
 * "OclConstraintPrecompiler": { "cache.target": "(cache.name=metadata)" }
 * </pre>
 *
 * <p>Releasing on unbind is optional in the sense that nothing breaks without it: entries name
 * their version, so those of a departed one are simply never placed again, and a returning
 * version finds its compiled OCL still there. It is done anyway, because a runtime that sees
 * many versions come and go would otherwise grow a registry nobody reads — and because
 * {@code releasing with the version} is what the user guide promises. It needs the versioned
 * face of the cache; with a cache that has none, binding still works and nothing is released.
 *
 * <p>Composes the precompiler rather than extending it — a component is wiring.
 *
 * @since 1.0
 */
@Component(name = "OclConstraintPrecompiler", immediate = true)
public class OclConstraintPrecompilerComponent {

	private final OclConstraintPrecompiler precompiler;
	private final Optional<OclVersionedExpressions> versioned;

	@Activate
	public OclConstraintPrecompilerComponent(
			@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED) OclExpressionParser parser,
			@Reference(name = "cache") OclExpressionCache cache,
			@Reference(name = "versioned", cardinality = ReferenceCardinality.OPTIONAL)
			Optional<OclVersionedExpressions> versioned) {
		this.precompiler = new OclConstraintPrecompiler(parser, cache);
		this.versioned = versioned;
	}

	/**
	 * A model version arrived: compile what it declares, against this very instance.
	 *
	 * @param ePackage the model version
	 * @param properties its service properties — unused, and on purpose: an advertised
	 *     fingerprint is context, while the identity entries are filed under is computed
	 */
	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC,
			unbind = "removeModel")
	void addModel(EPackage ePackage, Map<String, Object> properties) {
		precompiler.compile(ePackage);
	}

	void removeModel(EPackage ePackage, Map<String, Object> properties) {
		versioned.ifPresent(store -> store.release(ePackage));
	}
}
