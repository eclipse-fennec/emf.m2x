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
package org.eclipse.fennec.m2x.m2t.parser;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;

import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;

/**
 * Result of parsing a MOFM2T module, carrying the parsed AST along with
 * unresolved name references that need to be linked in a second phase.
 *
 * <p>The linker uses these pending references to resolve cross-module
 * dependencies (extends, imports), template overrides, and invocation targets.
 *
 * @param module the parsed module AST
 * @param extendsNames module names from the {@code extends} clause
 * @param importNames module names from {@code [import ...]} declarations
 * @param overrideNames templates mapped to the names of templates they override
 * @param invocationNames template invocations mapped to the invoked name
 * @param indentationMap standalone template invocations mapped to their indentation string (§8.4)
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record M2tParseResult(
		Module module,
		List<String> extendsNames,
		List<String> importNames,
		Map<Template, List<String>> overrideNames,
		Map<TemplateInvocation, String> invocationNames,
		Map<TemplateInvocation, String> indentationMap,
		Map<EObject, SourcePosition> positions
) {

	/**
	 * Creates a parse result with defensive copies.
	 */
	public M2tParseResult {
		Objects.requireNonNull(module, "module must not be null");
		extendsNames = List.copyOf(extendsNames);
		importNames = List.copyOf(importNames);
		overrideNames = Map.copyOf(overrideNames);
		invocationNames = Map.copyOf(invocationNames);
		indentationMap = Map.copyOf(indentationMap);
		// Not copied into a Map.of: the keys are identities of AST nodes, and the evaluator looks
		// them up by identity when it places a runtime diagnostic (#116).
		positions = positions == null ? Map.of() : Map.copyOf(positions);
	}

	/**
	 * Convenience constructor without indentation map (pre-normalization).
	 */
	public M2tParseResult(Module module, List<String> extendsNames,
			List<String> importNames, Map<Template, List<String>> overrideNames,
			Map<TemplateInvocation, String> invocationNames) {
		this(module, extendsNames, importNames, overrideNames, invocationNames, Map.of(), Map.of());
	}

	/**
	 * Convenience constructor without node positions.
	 */
	public M2tParseResult(Module module, List<String> extendsNames,
			List<String> importNames, Map<Template, List<String>> overrideNames,
			Map<TemplateInvocation, String> invocationNames,
			Map<TemplateInvocation, String> indentationMap) {
		this(module, extendsNames, importNames, overrideNames, invocationNames, indentationMap,
				Map.of());
	}
}
