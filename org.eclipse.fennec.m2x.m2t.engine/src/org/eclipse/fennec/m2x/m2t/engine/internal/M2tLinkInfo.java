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
package org.eclipse.fennec.m2x.m2t.engine.internal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.parser.M2tParseResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;

/**
 * What a MOFM2T module still has to link, kept on the module itself.
 *
 * <p>The parser leaves the names of an {@code extends}, an {@code import}, an {@code overrides}
 * and every not yet resolved invocation in its {@link M2tParseResult}, beside the AST — in the
 * AST a {@code TemplateInvocation} has a {@code definition} or nothing. A unit compiled under
 * {@code pin} or {@code rebind} is stored unbound and bound later, by an engine that never
 * parsed it; without the names it could not be bound at all. So the compiler records them on
 * the module as annotations, and {@link #recover} turns them back into a parse result when a
 * module arrives without one.
 *
 * <p>Names of module-level things go into details; names of things inside the module go into
 * one annotation whose {@code references} list the nodes in-tree and whose details carry the
 * names index by index — the M2T metaclasses below {@code Module} are no
 * {@code EModelElement}s and cannot carry annotations of their own. All of it is internal to
 * the compiled document: a bound unit carries none of it.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class M2tLinkInfo {

	static final String EXTENDS = "m2t.link.extends";
	static final String IMPORTS = "m2t.link.imports";
	static final String OVERRIDES = "m2t.link.overrides";
	static final String INVOCATIONS = "m2t.link.invocations";

	private M2tLinkInfo() {
	}

	/**
	 * Records what the parse result knows and the AST does not.
	 *
	 * @param result the parse result of an unbound module
	 */
	static void record(M2tParseResult result) {
		Objects.requireNonNull(result, "result must not be null");
		Module module = result.module();
		strip(module);
		names(module, EXTENDS, result.extendsNames());
		names(module, IMPORTS, result.importNames());
		if (!result.overrideNames().isEmpty()) {
			EAnnotation overrides = annotation(module, OVERRIDES);
			int i = 0;
			for (Map.Entry<Template, List<String>> entry : result.overrideNames().entrySet()) {
				overrides.getReferences().add(entry.getKey());
				overrides.getDetails().put(String.valueOf(i++), String.join(",", entry.getValue()));
			}
		}
		Map<TemplateInvocation, String> pending = new LinkedHashMap<>();
		result.invocationNames().forEach((invocation, name) -> {
			if (invocation.getDefinition() == null && invocation.eContainer() != null) {
				pending.put(invocation, name);
			}
		});
		if (!pending.isEmpty()) {
			EAnnotation invocations = annotation(module, INVOCATIONS);
			int i = 0;
			for (Map.Entry<TemplateInvocation, String> entry : pending.entrySet()) {
				invocations.getReferences().add(entry.getKey());
				invocations.getDetails().put(String.valueOf(i++), entry.getValue());
			}
		}
	}

	/**
	 * Rebuilds a parse result from what {@link #record} left on the module.
	 *
	 * @param module the module
	 * @return the parse result, or empty if the module carries no link information
	 */
	static Optional<M2tParseResult> recover(Module module) {
		Objects.requireNonNull(module, "module must not be null");
		if (module.getEAnnotation(EXTENDS) == null && module.getEAnnotation(IMPORTS) == null
				&& module.getEAnnotation(OVERRIDES) == null && module.getEAnnotation(INVOCATIONS) == null) {
			return Optional.empty();
		}
		Map<Template, List<String>> overrides = new LinkedHashMap<>();
		EAnnotation overridesAnnotation = module.getEAnnotation(OVERRIDES);
		if (overridesAnnotation != null) {
			int i = 0;
			for (EObject reference : overridesAnnotation.getReferences()) {
				String names = overridesAnnotation.getDetails().get(String.valueOf(i++));
				if (reference instanceof Template template && names != null) {
					overrides.put(template, List.of(names.split(",")));
				}
			}
		}
		Map<TemplateInvocation, String> invocations = new LinkedHashMap<>();
		EAnnotation invocationsAnnotation = module.getEAnnotation(INVOCATIONS);
		if (invocationsAnnotation != null) {
			int i = 0;
			for (EObject reference : invocationsAnnotation.getReferences()) {
				String name = invocationsAnnotation.getDetails().get(String.valueOf(i++));
				if (reference instanceof TemplateInvocation invocation && name != null) {
					invocations.put(invocation, name);
				}
			}
		}
		return Optional.of(new M2tParseResult(module, names(module, EXTENDS), names(module, IMPORTS),
				overrides, invocations));
	}

	/** Removes every link annotation — a bound module carries none. */
	static void strip(Module module) {
		module.getEAnnotations().removeIf(a -> EXTENDS.equals(a.getSource()) || IMPORTS.equals(a.getSource())
				|| OVERRIDES.equals(a.getSource()) || INVOCATIONS.equals(a.getSource()));
	}

	private static void names(Module module, String source, List<String> names) {
		if (names.isEmpty()) {
			return;
		}
		EAnnotation annotation = annotation(module, source);
		for (int i = 0; i < names.size(); i++) {
			annotation.getDetails().put(String.valueOf(i), names.get(i));
		}
	}

	private static List<String> names(Module module, String source) {
		EAnnotation annotation = module.getEAnnotation(source);
		return annotation == null ? List.of() : new ArrayList<>(annotation.getDetails().values());
	}

	private static EAnnotation annotation(Module module, String source) {
		EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
		annotation.setSource(source);
		module.getEAnnotations().add(annotation);
		return annotation;
	}
}
