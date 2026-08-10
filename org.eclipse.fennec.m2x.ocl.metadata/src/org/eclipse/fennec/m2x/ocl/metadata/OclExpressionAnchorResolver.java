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

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryEntry;
import org.eclipse.fennec.emf.osgi.eobject.registry.metadata.AspectAnchorResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Tells the metadata bridge which class a compiled expression belongs to.
 *
 * <p>The default resolver anchors an entry at the {@code eClass()} of its content, which for a
 * compiled expression is a class of the OCL metamodel — {@code PropertyCallExp} and the like.
 * Nobody looks for compiled OCL there. The class that matters is the <em>context type</em> the
 * expression was compiled against, and that is what this resolver returns.
 *
 * <p>It asks the cache, and nothing else. The context type is not recoverable from the
 * expression itself — {@code OclExpression.getType()} is the type of its result — and looking it
 * up in a registry of live models fails exactly when the answer is needed: during the handler
 * replay of a model version whose tree is not published yet. The cache filed the entry and knows
 * what it filed it for, which makes the answer available at any moment and independent of what
 * is registered.
 *
 * <p><b>The registry stays the read face.</b> One class carries at most one aspect per type id,
 * while an OCL context type has as many expressions as someone writes — so with several
 * expressions per class the last one placed is the one the aspect shows. The aspect is the
 * model-anchored entry point, not a query API; ask the registry when you need all of them.
 *
 * <p>Configured by target, as the bridge expects:
 *
 * <pre>
 * "EObjectRegistryMetadataBridge~ocl": {
 *     "emf.eobject.registry.name": "ocl-compiled",
 *     "aspect.type.id": "ocl.compiled",
 *     "anchorResolver.target": "(anchor.resolver.name=ocl)"
 * }
 * </pre>
 *
 * @since 1.0
 */
@Component(name = "OclExpressionAnchorResolver", service = AspectAnchorResolver.class,
		property = "anchor.resolver.name=ocl")
public class OclExpressionAnchorResolver implements AspectAnchorResolver {

	private final OclVersionedExpressions expressions;

	/**
	 * @param expressions the cache that filed the entries, must not be {@code null}
	 */
	@Activate
	public OclExpressionAnchorResolver(
			@Reference(name = "expressions") OclVersionedExpressions expressions) {
		this.expressions = Objects.requireNonNull(expressions, "expressions must not be null");
	}

	@Override
	public Collection<EClass> anchorsOf(EObjectRegistryEntry entry) {
		if (entry == null) {
			return List.of();
		}
		Optional<EClass> anchor = expressions.anchorOf(entry.key());
		return anchor.isPresent() ? List.of(anchor.get()) : List.of();
	}
}
