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
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.m2t.parser.M2tParseResult;
import org.eclipse.fennec.m2x.model.m2t.Block;
import org.eclipse.fennec.m2x.model.m2t.IfBlock;
import org.eclipse.fennec.m2x.model.m2t.InlineExpression;
import org.eclipse.fennec.m2x.model.m2t.LetBlock;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.M2tPackage;
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
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;
import org.eclipse.fennec.m2x.ocl.parser.typing.OclTypeRules;

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
	private final Map<Module, List<UnresolvedReference>> unresolvedReferences = new LinkedHashMap<>();

	/**
	 * A reference naming something that is not there, and where it was written.
	 *
	 * @param message  what is unresolved, in the words {@link #link(List)} warns with
	 * @param position where the reference stands, or {@code null} when that is not known
	 */
	record UnresolvedReference(String message, SourcePosition position) {
	}

	/**
	 * Links all provided parse results.
	 *
	 * @param results the parse results to link
	 * @return warnings encountered during linking
	 */
	public List<String> link(List<M2tParseResult> results) {
		Objects.requireNonNull(results, "results must not be null");
		warnings.clear();
		moduleIndex.clear();
		parseResults.clear();

		// Phase 1: Build module index
		for (M2tParseResult result : results) {
			Module module = result.module();
			String name = module.getName();
			moduleIndex.put(name, module);
			parseResults.put(name, result);
		}

		// Phase 2: Resolve extends and imports
		for (M2tParseResult result : results) {
			checkUniqueNames(result);
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
	 * Records a reference that names something the linker could not find.
	 *
	 * <p>These are the messages {@link #unresolvedReferences()} reports separately, because
	 * whether they are a warning or an error is the caller's decision (#144) — the linker
	 * itself always links what it can and says what it could not.
	 */
	private void unresolved(Module owner, String message, SourcePosition position) {
		warnings.add(message);
		unresolvedReferences.computeIfAbsent(owner, key -> new ArrayList<>())
				.add(new UnresolvedReference(message, position));
	}

	/**
	 * Where a node of the given module stood, as its parse result recorded it — or its nearest
	 * container that has a position, the enclosing expression rather than none.
	 */
	private SourcePosition positionOf(Module module, EObject node) {
		M2tParseResult result = parseResults.get(module.getName());
		if (result == null) {
			return null;
		}
		for (EObject current = node; current != null && current != module; current = current.eContainer()) {
			SourcePosition position = result.positions().get(current);
			if (position != null) {
				return position;
			}
		}
		return null;
	}

	/**
	 * Returns the subset of {@link #link(List)}'s messages that report a reference naming
	 * something that is not there: an {@code extends}, an {@code import}, an
	 * {@code overrides} or an invocation.
	 *
	 * <p>They are the M2T counterpart of {@code Cannot resolve import} in QVT-O and QVT-R, and
	 * under {@link org.eclipse.fennec.m2x.m2t.api.UnresolvedReferenceMode#FAIL} they end the
	 * generation instead of merely being mentioned. They are attributed to the module the
	 * reference is written in: one broken module in a link set must not stop a sound one from
	 * generating.
	 *
	 * @return the unresolved references per module, never {@code null}
	 */
	Map<Module, List<String>> unresolvedReferences() {
		Map<Module, List<String>> copy = new LinkedHashMap<>();
		unresolvedReferences.forEach((module, references) -> copy.put(module,
				references.stream().map(UnresolvedReference::message).toList()));
		return copy;
	}

	/**
	 * The unresolved references written in one module, each with where it stands — for a
	 * compile that reports them as positioned diagnostics (#264).
	 *
	 * @param module the module the references are written in
	 * @return the references in the order they were found, never {@code null}
	 */
	List<UnresolvedReference> unresolvedReferencesOf(Module module) {
		return List.copyOf(unresolvedReferences.getOrDefault(module, List.of()));
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
			if (isBound(module.getExtends(), extendsName)) {
				continue; // bound already — by a compile under embed, or by an earlier link
			}
			Module target = moduleIndex.get(extendsName);
			if (target != null) {
				module.getExtends().add(target);
			} else {
				unresolved(module, "Unresolved extends '" + extendsName
						+ "' in module '" + module.getName() + "'", result.referencePositions().get(extendsName));
			}
		}

		for (String importName : result.importNames()) {
			if (isBound(module.getImports(), importName)) {
				continue;
			}
			Module target = moduleIndex.get(importName);
			if (target != null) {
				module.getImports().add(target);
			} else {
				unresolved(module, "Unresolved import '" + importName
						+ "' in module '" + module.getName() + "'", result.referencePositions().get(importName));
			}
		}
	}

	private static boolean isBound(List<Module> bound, String name) {
		for (Module module : bound) {
			if (name.equals(module.getName())) {
				return true;
			}
		}
		return false;
	}

	// --- Phase 3: Overrides ---

	/**
	 * Links the overrides of a module's templates and checks them (MOFM2T §7, §8.1.3, #365): a
	 * template overrides a public or protected template of a module it extends — not one of its
	 * own module, not one it only imports, not a private one — with as many parameters, each of
	 * the same type as the overridden one's, a subtype or a supertype.
	 */
	private void linkOverrides(M2tParseResult result) {
		Module module = result.module();

		for (Map.Entry<Template, List<String>> entry : result.overrideNames().entrySet()) {
			Template template = entry.getKey();
			SourcePosition position = result.positions().get(template);
			for (String overrideName : entry.getValue()) {
				Template target = resolveOverridden(overrideName, template, module);
				if (target != null) {
					if (checkOverride(template, target, module, position)) {
						template.getOverrides().add(target);
					}
				} else {
					unresolved(module, overrideProblem(overrideName, template, module)
							+ " in template '" + template.getName()
							+ "' of module '" + module.getName() + "'", position);
				}
			}
		}
	}

	/**
	 * The template of an extended module that {@code overrides name} names: of the same-named
	 * ones the first with as many parameters as the overriding template, else the first.
	 */
	private static Template resolveOverridden(String name, Template overriding, Module module) {
		Template first = null;
		for (Module extended : collectTransitiveExtends(module)) {
			for (ModuleElement element : extended.getOwnedModuleElement()) {
				if (element instanceof Template candidate && name.equals(candidate.getName())
						&& isVisible(candidate, VisibilityScope.EXTENDS)) {
					if (candidate.getParameter().size() == overriding.getParameter().size()) {
						return candidate;
					}
					if (first == null) {
						first = candidate;
					}
				}
			}
		}
		return first;
	}

	/** Why an override target was not found, where that is more than its absence. */
	private static String overrideProblem(String name, Template overriding, Module module) {
		for (Module extended : collectTransitiveExtends(module)) {
			if (findElement(name, extended, VisibilityScope.ALL) instanceof Template) {
				return "Template '" + name + "' of module '" + extended.getName()
						+ "' is private and cannot be overridden (MOFM2T §7)";
			}
		}
		for (Module imported : module.getImports()) {
			if (findElement(name, imported, VisibilityScope.ALL) instanceof Template) {
				return "Template '" + name + "' of module '" + imported.getName()
						+ "' is only imported — only a template of an extended module can be overridden (MOFM2T §7)";
			}
		}
		for (ModuleElement element : module.getOwnedModuleElement()) {
			if (element != overriding && element instanceof Template && name.equals(element.getName())) {
				return "Template '" + name + "' is of the same module — only a template of an extended module can be overridden (MOFM2T §7)";
			}
		}
		if (name.equals(overriding.getName())) {
			return "Template '" + name + "' overrides itself — an override needs a template of an extended module (MOFM2T §7)";
		}
		return "Unresolved override '" + name + "'";
	}

	private boolean checkOverride(Template template, Template target, Module module, SourcePosition position) {
		List<Variable> own = template.getParameter();
		List<Variable> overridden = target.getParameter();
		if (own.size() != overridden.size()) {
			unresolved(module, "Template '" + template.getName() + "' has " + own.size() + " parameter(s), the template '"
					+ target.getName() + "' it overrides has " + overridden.size() + " (MOFM2T §8.1.3)", position);
			return false;
		}
		for (int i = 0; i < own.size(); i++) {
			EClassifier a = own.get(i).getType();
			EClassifier b = overridden.get(i).getType();
			if (!OclTypeRules.conformsTo(a, b) && !OclTypeRules.conformsTo(b, a)) {
				unresolved(module, "Parameter '" + own.get(i).getName() + "' of template '" + template.getName()
						+ "' is a " + OclTypeRules.nameOf(a) + ", which is neither the type " + OclTypeRules.nameOf(b)
						+ " of the template it overrides, nor a subtype or supertype of it (MOFM2T §8.1.3)", position);
				return false;
			}
		}
		return true;
	}

	/**
	 * MOFM2T §8.1.2: "A module element has a unique name within the containing module". Same-named
	 * templates with different parameters are kept, the evaluator dispatches among them by the
	 * argument types, as Acceleo does (#369) — a deliberate deviation. Two templates with the same
	 * parameter types, and a template, query or macro sharing a name with one of another kind
	 * or with another query or macro, are errors.
	 */
	private void checkUniqueNames(M2tParseResult result) {
		Module module = result.module();
		Map<String, List<ModuleElement>> byName = new LinkedHashMap<>();
		for (ModuleElement element : module.getOwnedModuleElement()) {
			if (element.getName() != null) {
				byName.computeIfAbsent(element.getName(), name -> new ArrayList<>()).add(element);
			}
		}
		for (List<ModuleElement> namesakes : byName.values()) {
			for (int i = 1; i < namesakes.size(); i++) {
				ModuleElement element = namesakes.get(i);
				for (int j = 0; j < i; j++) {
					ModuleElement earlier = namesakes.get(j);
					if (!(element instanceof Template t && earlier instanceof Template e) || sameParameterTypes(t, e)) {
						unresolved(module, "'" + element.getName() + "' is declared twice in module '" + module.getName()
								+ "' (MOFM2T §8.1.2" + (element instanceof Template ? "; same-named templates need different parameter types" : "")
								+ ")", result.positions().get(element));
						break;
					}
				}
			}
		}
	}

	private static boolean sameParameterTypes(Template a, Template b) {
		if (a.getParameter().size() != b.getParameter().size()) {
			return false;
		}
		for (int i = 0; i < a.getParameter().size(); i++) {
			EClassifier x = a.getParameter().get(i).getType();
			EClassifier y = b.getParameter().get(i).getType();
			if (!OclTypeRules.conformsTo(x, y) || !OclTypeRules.conformsTo(y, x)) {
				return false;
			}
		}
		return true;
	}

	// --- Phase 4: Invocations ---

	private void linkInvocations(M2tParseResult result) {
		Module module = result.module();

		for (Map.Entry<TemplateInvocation, String> entry : result.invocationNames().entrySet()) {
			TemplateInvocation invocation = entry.getKey();
			String name = entry.getValue();

			ModuleElement target = resolveModuleElement(name, module, invocation.getArgument().size());
			if (target == null) {
				unresolved(module, "Unresolved invocation '" + name
						+ "' in module '" + module.getName() + "'", result.positions().get(invocation));
				continue;
			}
			checkArity(target, invocation.getArgument().size(), module, result.positions().get(invocation));

			if (target instanceof Template template) {
				invocation.setDefinition(template);
			} else if (target instanceof Query query) {
				if (invocation.getBefore() != null || invocation.getEach() != null || invocation.getAfter() != null) {
					// A query yields a value, not a series of invocations to put text around
					unresolved(module, "Query '" + name + "' is called with before, separator or after —"
							+ " they belong to a template invocation (MOFM2T §8.1.10)", result.positions().get(invocation));
				}
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
	 * Walks the module AST to find inline expressions ({@code [expr/]}) whose expression is an
	 * OperationCallExp matching a module element.
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

			// An inline expression whose expression calls a template, query or macro
			if (expr instanceof InlineExpression inline) {
				OclExpression init = inline.getExpression();

				// Handle [super/] — parsed as VariableExp("super") inside an inline expression
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
						ModuleElement target = resolveModuleElement(opName, contextModule, argumentCount(opCall));
						if (target != null) {
							checkBodyArgument(target, opCall, contextModule);
							TemplateExpression replacement = createInvocation(target, opCall);
							if (replacement != null) {
								checkArity(target, invocationArguments(replacement).size(), contextModule,
										positionOf(contextModule, opCall));
								body.set(i, replacement);
								continue;
							}
						} else if (isStandaloneCall(opCall)) {
							// Standalone call (no explicit source) that doesn't match
							// any module element — likely an unresolved invocation
							unresolved(contextModule, "Unresolved invocation '" + opName
									+ "' in module '" + contextModule.getName() + "'",
									positionOf(contextModule, opCall));
						}
					}
				}
			}

			// Recurse into nested blocks
			if (expr instanceof Block nestedBlock) {
				resolveInlineInBody(nestedBlock, contextModule);
			}
			// Recurse into IfBlock elseIf/else
			if (expr instanceof IfBlock ifBlock) {
				for (IfBlock elseIf : ifBlock.getElseIf()) {
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
	 *
	 * <p>In the receiver form {@code [c.genClass()/]} the receiver is the first argument, as
	 * {@link M2tOperationProvider} does for a query inside an expression. Dropping it left the
	 * parameter unbound, and the call only worked while the caller happened to have a variable of
	 * the same name in scope (#383).
	 */
	private TemplateExpression createInvocation(ModuleElement target, OperationCallExp opCall) {
		List<OclExpression> args = new ArrayList<>(opCall.getOwnedArguments());
		if (!isStandaloneCall(opCall) && args.size() < parameterCount(target)) {
			args.add(0, opCall.getOwnedSource());
		}

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

	/**
	 * §7.4, §8.1.19, §8.1.20: a body goes to a macro whose last parameter is of type Body, and
	 * such a macro is called with one (#371).
	 */
	private void checkBodyArgument(ModuleElement target, OperationCallExp call, Module contextModule) {
		List<OclExpression> arguments = call.getOwnedArguments();
		boolean withBody = !arguments.isEmpty() && arguments.get(arguments.size() - 1) instanceof Block;
		boolean takesBody = target instanceof Macro macro && !macro.getParameter().isEmpty()
				&& macro.getParameter().get(macro.getParameter().size() - 1).getType() == M2tPackage.Literals.BODY;
		if (withBody && !takesBody) {
			unresolved(contextModule, "'" + target.getName() + "' is called with a body, but "
					+ (target instanceof Macro ? "the macro has no Body parameter" : "only a macro takes one")
					+ " (MOFM2T §8.1.19)", positionOf(contextModule, call));
		} else if (takesBody && !withBody) {
			unresolved(contextModule, "Macro '" + target.getName()
					+ "' takes a body: call it as [" + target.getName() + "(…)]…[/" + target.getName() + "] (MOFM2T §7.4)",
					positionOf(contextModule, call));
		}
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
	/**
	 * Returns every {@link Query} the given module can call, under the same visibility rules
	 * an invocation follows: the module's own queries whatever their visibility, then the
	 * transitively extended modules' public and protected ones, then the imported modules'
	 * public ones. The first query of a name wins, as in
	 * {@link #resolveModuleElement(String, Module)}.
	 *
	 * <p>This exists so that {@link M2tOperationProvider} can hand OCL the same set the linker
	 * resolves an invocation against. A guard is a plain OCL expression, so the linker's
	 * rewrite of an invocation node never reaches it and OCL has to be able to resolve the call
	 * itself (#146) — with one rule, in one place, rather than two that drift apart.
	 *
	 * @param contextModule the module the call is written in
	 * @return the callable queries by name, never {@code null}
	 */
	static Map<String, Query> visibleQueries(Module contextModule) {
		Map<String, Query> queries = new LinkedHashMap<>();
		collectQueries(contextModule, VisibilityScope.ALL, queries);
		for (Module extended : collectTransitiveExtends(contextModule)) {
			collectQueries(extended, VisibilityScope.EXTENDS, queries);
		}
		for (Module imported : contextModule.getImports()) {
			collectQueries(imported, VisibilityScope.IMPORTS, queries);
		}
		return queries;
	}

	/**
	 * Every {@link Template} the given module can call, under the rules of
	 * {@link #visibleQueries(Module)} — for a template invocation inside an OCL expression (#364).
	 * Of same-named templates the first stands for all: the evaluator dispatches among them.
	 *
	 * @param contextModule the module the call is written in
	 * @return the callable templates by name, never {@code null}
	 */
	static Map<String, Template> visibleTemplates(Module contextModule) {
		Map<String, Template> templates = new LinkedHashMap<>();
		collectTemplates(contextModule, VisibilityScope.ALL, templates);
		for (Module extended : collectTransitiveExtends(contextModule)) {
			collectTemplates(extended, VisibilityScope.EXTENDS, templates);
		}
		for (Module imported : contextModule.getImports()) {
			collectTemplates(imported, VisibilityScope.IMPORTS, templates);
		}
		return templates;
	}

	private static void collectTemplates(Module module, VisibilityScope scope, Map<String, Template> collected) {
		for (ModuleElement element : module.getOwnedModuleElement()) {
			if (element instanceof Template template && template.getName() != null && isVisible(element, scope)) {
				collected.putIfAbsent(template.getName(), template);
			}
		}
	}

	private static boolean isVisible(ModuleElement element, VisibilityScope scope) {
		VisibilityKind visibility = element.getVisibility();
		return switch (scope) {
			case ALL -> true;
			case EXTENDS -> visibility != VisibilityKind.PRIVATE;
			case IMPORTS -> visibility == VisibilityKind.PUBLIC;
		};
	}

	private static void collectQueries(Module module, VisibilityScope scope,
			Map<String, Query> collected) {
		for (ModuleElement element : module.getOwnedModuleElement()) {
			if (!(element instanceof Query query) || query.getName() == null) {
				continue;
			}
			VisibilityKind visibility = element.getVisibility();
			if (scope == VisibilityScope.IMPORTS && visibility != VisibilityKind.PUBLIC) {
				continue;
			}
			if (scope == VisibilityScope.EXTENDS && visibility == VisibilityKind.PRIVATE) {
				continue;
			}
			collected.putIfAbsent(query.getName(), query);
		}
	}

	/**
	 * Resolves a module element by name: local module → extended modules (transitive) → imported
	 * modules, under the visibility rules. Of the same-named elements visible, the first with as
	 * many parameters as the call has arguments is taken, else the first — whose arity the caller
	 * then reports (#364, #369).
	 */
	private static ModuleElement resolveModuleElement(String name, Module contextModule, int argumentCount) {
		List<ModuleElement> candidates = new ArrayList<>();
		collectElements(name, contextModule, VisibilityScope.ALL, candidates);
		for (Module extended : collectTransitiveExtends(contextModule)) {
			collectElements(name, extended, VisibilityScope.EXTENDS, candidates);
		}
		for (Module imported : contextModule.getImports()) {
			collectElements(name, imported, VisibilityScope.IMPORTS, candidates);
		}
		for (ModuleElement candidate : candidates) {
			if (parameterCount(candidate) == argumentCount) {
				return candidate;
			}
		}
		return candidates.isEmpty() ? null : candidates.get(0);
	}

	private static void collectElements(String name, Module module, VisibilityScope scope, List<ModuleElement> collected) {
		for (ModuleElement element : module.getOwnedModuleElement()) {
			if (name.equals(element.getName()) && isVisible(element, scope)) {
				collected.add(element);
			}
		}
	}

	/**
	 * §8.1.10, §7: a template, query or macro is invoked "with the correct parameters" — as many
	 * arguments as it has parameters (#364).
	 */
	private void checkArity(ModuleElement target, int argumentCount, Module contextModule, SourcePosition position) {
		int parameterCount = parameterCount(target);
		if (argumentCount != parameterCount) {
			unresolved(contextModule, "'" + target.getName() + "' takes " + parameterCount + " argument(s), the call passes "
					+ argumentCount + " (MOFM2T §8.1.10)", position);
		}
	}

	/** The arguments of a call as written, the receiver of the receiver form counted. */
	private static int argumentCount(OperationCallExp opCall) {
		return opCall.getOwnedArguments().size() + (isStandaloneCall(opCall) ? 0 : 1);
	}

	private static List<OclExpression> invocationArguments(TemplateExpression invocation) {
		return switch (invocation) {
			case TemplateInvocation call -> call.getArgument();
			case QueryInvocation call -> call.getArgument();
			case MacroInvocation call -> call.getArgument();
			default -> List.of();
		};
	}

	/**
	 * Finds a named module element in a module respecting visibility scope.
	 */
	private static ModuleElement findElement(String name, Module module, VisibilityScope scope) {
		for (ModuleElement element : module.getOwnedModuleElement()) {
			if (name.equals(element.getName()) && isVisible(element, scope)) {
				return element;
			}
		}
		return null;
	}

	/**
	 * Collects all transitively extended modules (breadth-first, no duplicates).
	 */
	private static List<Module> collectTransitiveExtends(Module module) {
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
	private static int parameterCount(ModuleElement element) {
		return switch (element) {
			case Template template -> template.getParameter().size();
			case Query query -> query.getParameter().size();
			case Macro macro -> macro.getParameter().size();
			default -> 0;
		};
	}

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
