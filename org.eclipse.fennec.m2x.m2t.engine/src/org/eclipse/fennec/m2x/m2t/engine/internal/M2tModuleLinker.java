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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.m2t.parser.M2tParseResult;
import org.eclipse.fennec.m2x.model.m2t.Block;
import org.eclipse.fennec.m2x.model.m2t.LetBlock;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Macro;
import org.eclipse.fennec.m2x.model.m2t.MacroInvocation;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.ModuleElement;
import org.eclipse.fennec.m2x.model.m2t.Query;
import org.eclipse.fennec.m2x.model.m2t.QueryInvocation;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.TemplateExpression;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;
import org.eclipse.fennec.m2x.model.m2t.VisibilityKind;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;

/**
 * Links parsed MOFM2T modules by resolving cross-module references.
 *
 * <p>Resolves:
 * <ul>
 *   <li>Module extends and imports (name → Module reference)</li>
 *   <li>Template overrides (name → Template reference)</li>
 *   <li>Invocations (name → Template/Query/Macro definition, with AST node replacement)</li>
 * </ul>
 *
 * <p>Visibility rules: private elements are only visible within their own module.
 * Transitive extends are supported (A extends B extends C → A sees C's public elements).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class M2tModuleLinker {

	private static final M2tFactory FACTORY = M2tFactory.eINSTANCE;

	private final Map<String, M2tParseResult> parseResults = new LinkedHashMap<>();
	private final Map<String, Module> moduleIndex = new LinkedHashMap<>();
	private final List<String> warnings = new ArrayList<>();

	/**
	 * Links all provided parse results.
	 *
	 * @param results the parse results to link
	 * @return warnings encountered during linking
	 */
	public List<String> link(List<M2tParseResult> results) {
		Objects.requireNonNull(results, "results must not be null");
		warnings.clear();

		// Phase 1: Build module index
		for (M2tParseResult result : results) {
			Module module = result.module();
			String name = module.getName();
			moduleIndex.put(name, module);
			parseResults.put(name, result);
		}

		// Phase 2: Resolve extends and imports
		for (M2tParseResult result : results) {
			resolveExtendsAndImports(result);
		}

		// Phase 3: Link overrides
		for (M2tParseResult result : results) {
			linkOverrides(result);
		}

		// Phase 4: Link explicit invocations (from templateInvocation grammar rule)
		for (M2tParseResult result : results) {
			linkInvocations(result);
		}

		// Phase 5: Resolve inline operation calls to module element invocations
		// (ANTLR parses [name(args)/] as inlineExpression → OperationCallExp;
		//  we need to replace these with proper TemplateInvocation/QueryInvocation/MacroInvocation)
		for (M2tParseResult result : results) {
			resolveInlineInvocations(result.module());
		}

		return List.copyOf(warnings);
	}

	/**
	 * Links a single module (intra-module linking only, no cross-module resolution).
	 */
	public List<String> linkSingle(M2tParseResult result) {
		return link(List.of(result));
	}

	// --- Phase 2: Extends & Imports ---

	private void resolveExtendsAndImports(M2tParseResult result) {
		Module module = result.module();

		for (String extendsName : result.extendsNames()) {
			Module target = moduleIndex.get(extendsName);
			if (target != null) {
				module.getExtends().add(target);
			} else {
				warnings.add("Unresolved extends '" + extendsName
						+ "' in module '" + module.getName() + "'");
			}
		}

		for (String importName : result.importNames()) {
			Module target = moduleIndex.get(importName);
			if (target != null) {
				module.getImports().add(target);
			} else {
				warnings.add("Unresolved import '" + importName
						+ "' in module '" + module.getName() + "'");
			}
		}
	}

	// --- Phase 3: Overrides ---

	private void linkOverrides(M2tParseResult result) {
		Module module = result.module();

		for (Map.Entry<Template, List<String>> entry : result.overrideNames().entrySet()) {
			Template template = entry.getKey();
			for (String overrideName : entry.getValue()) {
				Template target = resolveTemplate(overrideName, module);
				if (target != null) {
					template.getOverrides().add(target);
				} else {
					warnings.add("Unresolved override '" + overrideName
							+ "' in template '" + template.getName()
							+ "' of module '" + module.getName() + "'");
				}
			}
		}
	}

	// --- Phase 4: Invocations ---

	private void linkInvocations(M2tParseResult result) {
		Module module = result.module();

		for (Map.Entry<TemplateInvocation, String> entry : result.invocationNames().entrySet()) {
			TemplateInvocation invocation = entry.getKey();
			String name = entry.getValue();

			ModuleElement target = resolveModuleElement(name, module);
			if (target == null) {
				warnings.add("Unresolved invocation '" + name
						+ "' in module '" + module.getName() + "'");
				continue;
			}

			if (target instanceof Template template) {
				invocation.setDefinition(template);
			} else if (target instanceof Query query) {
				replaceWithQueryInvocation(invocation, query);
			} else if (target instanceof Macro macro) {
				replaceWithMacroInvocation(invocation, macro);
			}
		}
	}

	/**
	 * Replaces a TemplateInvocation node with a QueryInvocation in the AST.
	 * Transfers arguments from the original invocation.
	 */
	private void replaceWithQueryInvocation(TemplateInvocation invocation, Query query) {
		QueryInvocation queryInv = FACTORY.createQueryInvocation();
		queryInv.setDefinition(query);

		// Transfer arguments (must copy since they're containment refs)
		List<OclExpression> args = new ArrayList<>(invocation.getArgument());
		for (OclExpression arg : args) {
			queryInv.getArgument().add(arg);
		}

		EcoreUtil.replace(invocation, queryInv);
	}

	/**
	 * Replaces a TemplateInvocation node with a MacroInvocation in the AST.
	 * Transfers arguments from the original invocation.
	 */
	private void replaceWithMacroInvocation(TemplateInvocation invocation, Macro macro) {
		MacroInvocation macroInv = FACTORY.createMacroInvocation();
		macroInv.setDefinition(macro);

		// Transfer arguments
		List<OclExpression> args = new ArrayList<>(invocation.getArgument());
		for (OclExpression arg : args) {
			macroInv.getArgument().add(arg);
		}

		EcoreUtil.replace(invocation, macroInv);
	}

	// --- Phase 5: Inline invocation resolution ---

	/**
	 * Walks the module AST to find inline expression wrappers (LetBlock with
	 * __inline__ variable) that contain OperationCallExp matching a module element.
	 * Replaces them with the appropriate invocation node.
	 */
	private void resolveInlineInvocations(Module module) {
		for (ModuleElement element : new ArrayList<>(module.getOwnedModuleElement())) {
			if (element instanceof Block block) {
				resolveInlineInBody(block, module);
			}
		}
	}

	/**
	 * Recursively resolves inline invocations in a block's body.
	 */
	private void resolveInlineInBody(Block block, Module contextModule) {
		EList<TemplateExpression> body = block.getBody();
		for (int i = 0; i < body.size(); i++) {
			TemplateExpression expr = body.get(i);

			// Check for __inline__ LetBlock wrapper around OperationCallExp
			if (expr instanceof LetBlock letBlock
					&& letBlock.getLetVariable() != null
					&& letBlock.getLetVariable().getName().startsWith("__inline__")
					&& letBlock.getBody().isEmpty()) {

				Variable letVar = letBlock.getLetVariable();
				OclExpression init = letVar.getOwnedInit();

				// Handle [super/] — parsed as VariableExp("super") inside __inline__ LetBlock
				if (init instanceof VariableExp ve
						&& ve.getReferredVariable() != null
						&& "super".equals(ve.getReferredVariable().getName())) {
					TemplateInvocation superInv = FACTORY.createTemplateInvocation();
					superInv.setSuper(true);
					body.set(i, superInv);
					continue;
				}

				if (init instanceof OperationCallExp opCall) {
					String opName = opCall.getName();

					if (opName != null) {
						ModuleElement target = resolveModuleElement(opName, contextModule);
						if (target != null) {
							TemplateExpression replacement = createInvocation(target, opCall);
							if (replacement != null) {
								body.set(i, replacement);
								continue;
							}
						} else if (isStandaloneCall(opCall)) {
							// Standalone call (no explicit source) that doesn't match
							// any module element — likely an unresolved invocation
							warnings.add("Unresolved invocation '" + opName
									+ "' in module '" + contextModule.getName() + "'");
						}
					}
				}
			}

			// Recurse into nested blocks
			if (expr instanceof Block nestedBlock) {
				resolveInlineInBody(nestedBlock, contextModule);
			}
			// Recurse into IfBlock elseIf/else
			if (expr instanceof org.eclipse.fennec.m2x.model.m2t.IfBlock ifBlock) {
				for (org.eclipse.fennec.m2x.model.m2t.IfBlock elseIf : ifBlock.getElseIf()) {
					resolveInlineInBody(elseIf, contextModule);
				}
				if (ifBlock.getElse() != null) {
					resolveInlineInBody(ifBlock.getElse(), contextModule);
				}
			}
			// Recurse into LetBlock elseLet/else
			if (expr instanceof LetBlock lb) {
				for (LetBlock elseLet : lb.getElseLet()) {
					resolveInlineInBody(elseLet, contextModule);
				}
				if (lb.getElse() != null) {
					resolveInlineInBody(lb.getElse(), contextModule);
				}
			}
		}
	}

	/**
	 * Creates the appropriate invocation node for a resolved module element.
	 */
	private TemplateExpression createInvocation(ModuleElement target, OperationCallExp opCall) {
		List<OclExpression> args = new ArrayList<>(opCall.getOwnedArguments());

		if (target instanceof Template template) {
			TemplateInvocation inv = FACTORY.createTemplateInvocation();
			inv.setDefinition(template);
			for (OclExpression arg : args) {
				inv.getArgument().add(arg);
			}
			return inv;
		} else if (target instanceof Query query) {
			QueryInvocation inv = FACTORY.createQueryInvocation();
			inv.setDefinition(query);
			for (OclExpression arg : args) {
				inv.getArgument().add(arg);
			}
			return inv;
		} else if (target instanceof Macro macro) {
			MacroInvocation inv = FACTORY.createMacroInvocation();
			inv.setDefinition(macro);
			for (OclExpression arg : args) {
				inv.getArgument().add(arg);
			}
			return inv;
		}
		return null;
	}

	// --- Resolution helpers ---

	/**
	 * Visibility scope for element resolution.
	 * <ul>
	 *   <li>{@code ALL} — local module, all visibilities visible</li>
	 *   <li>{@code EXTENDS} — extended module, public + protected visible</li>
	 *   <li>{@code IMPORTS} — imported module, only public visible</li>
	 * </ul>
	 */
	private enum VisibilityScope { ALL, EXTENDS, IMPORTS }

	/**
	 * Resolves a module element (Template, Query, or Macro) by name.
	 * Search order: local module → extended modules (transitive) → imported modules.
	 * Respects visibility: private = local only, protected = local + extends, public = all.
	 */
	private ModuleElement resolveModuleElement(String name, Module contextModule) {
		// 1. Local module (all visibilities)
		ModuleElement local = findElement(name, contextModule, VisibilityScope.ALL);
		if (local != null) {
			return local;
		}

		// 2. Extended modules (transitive, public + protected)
		for (Module extended : collectTransitiveExtends(contextModule)) {
			ModuleElement found = findElement(name, extended, VisibilityScope.EXTENDS);
			if (found != null) {
				return found;
			}
		}

		// 3. Imported modules (public only)
		for (Module imported : contextModule.getImports()) {
			ModuleElement found = findElement(name, imported, VisibilityScope.IMPORTS);
			if (found != null) {
				return found;
			}
		}

		return null;
	}

	/**
	 * Resolves a template by name (for override linking).
	 */
	private Template resolveTemplate(String name, Module contextModule) {
		// Search extended modules (the typical case for overrides)
		for (Module extended : collectTransitiveExtends(contextModule)) {
			ModuleElement found = findElement(name, extended, VisibilityScope.EXTENDS);
			if (found instanceof Template t) {
				return t;
			}
		}

		// Also search local module
		ModuleElement local = findElement(name, contextModule, VisibilityScope.ALL);
		if (local instanceof Template t) {
			return t;
		}

		// Search imported modules
		for (Module imported : contextModule.getImports()) {
			ModuleElement found = findElement(name, imported, VisibilityScope.IMPORTS);
			if (found instanceof Template t) {
				return t;
			}
		}

		return null;
	}

	/**
	 * Finds a named module element in a module respecting visibility scope.
	 */
	private ModuleElement findElement(String name, Module module, VisibilityScope scope) {
		for (ModuleElement element : module.getOwnedModuleElement()) {
			if (name.equals(element.getName())) {
				VisibilityKind vis = element.getVisibility();
				if (scope == VisibilityScope.IMPORTS && vis != VisibilityKind.PUBLIC) {
					continue;
				}
				if (scope == VisibilityScope.EXTENDS && vis == VisibilityKind.PRIVATE) {
					continue;
				}
				// ALL → everything visible
				return element;
			}
		}
		return null;
	}

	/**
	 * Collects all transitively extended modules (breadth-first, no duplicates).
	 */
	private List<Module> collectTransitiveExtends(Module module) {
		List<Module> result = new ArrayList<>();
		Set<Module> visited = new LinkedHashSet<>();
		List<Module> queue = new ArrayList<>(module.getExtends());

		while (!queue.isEmpty()) {
			Module current = queue.remove(0);
			if (visited.add(current)) {
				result.add(current);
				queue.addAll(current.getExtends());
			}
		}

		return result;
	}

	/**
	 * Returns true if the OperationCallExp has no explicit source (standalone call
	 * like {@code helper(e)}) rather than a method call like {@code e.op()}.
	 */
	private static boolean isStandaloneCall(OperationCallExp opCall) {
		OclExpression source = opCall.getOwnedSource();
		if (source == null) {
			return true;
		}
		// Implicit self source → standalone call
		if (source instanceof VariableExp && opCall.isIsImplicit()) {
			return true;
		}
		return false;
	}
}
