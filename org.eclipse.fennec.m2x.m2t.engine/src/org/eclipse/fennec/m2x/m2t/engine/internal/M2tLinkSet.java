/*
 * ******************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 * ******************************************************************
 */
package org.eclipse.fennec.m2x.m2t.engine.internal;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;

/**
 * What the parser and the linker left for one generation to run on.
 *
 * <p>The three belong to the same question — which modules take part, and what is known
 * about the nodes in them — and are filled by the same step. The modules decide which
 * {@code overrides} are visible, so what is in here is also a security boundary: a module
 * that is not in the set cannot take over a template (#184).
 *
 * @param modules the modules this generation runs against, the entry module included
 * @param indentation standalone invocation indentation (MOFM2T §8.4)
 * @param positions where each expression node stood in its template (#116)
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record M2tLinkSet(
		Collection<Module> modules,
		Map<TemplateInvocation, String> indentation,
		Map<EObject, SourcePosition> positions) {

	public M2tLinkSet {
		Objects.requireNonNull(modules, "modules must not be null");
		indentation = indentation == null ? Map.of() : indentation;
		positions = positions == null ? Map.of() : positions;
	}

	/**
	 * A link set of modules alone, with nothing known about their nodes.
	 *
	 * @param modules the modules, must not be {@code null}
	 * @return the link set
	 */
	public static M2tLinkSet of(Collection<Module> modules) {
		return new M2tLinkSet(modules, Map.of(), Map.of());
	}

	/**
	 * A link set of one module.
	 *
	 * @param module the module, must not be {@code null}
	 * @return the link set
	 */
	public static M2tLinkSet of(Module module) {
		Objects.requireNonNull(module, "module must not be null");
		return of(List.of(module));
	}
}
