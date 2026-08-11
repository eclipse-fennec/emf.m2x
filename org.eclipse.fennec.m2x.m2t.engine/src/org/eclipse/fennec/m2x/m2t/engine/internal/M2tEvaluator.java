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

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.m2t.Block;
import org.eclipse.fennec.m2x.model.m2t.FileBlock;
import org.eclipse.fennec.m2x.model.m2t.ForBlock;
import org.eclipse.fennec.m2x.model.m2t.IfBlock;
import org.eclipse.fennec.m2x.model.m2t.LetBlock;
import org.eclipse.fennec.m2x.model.m2t.Macro;
import org.eclipse.fennec.m2x.model.m2t.MacroInvocation;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.ModuleElement;
import org.eclipse.fennec.m2x.model.ocl.ClassifierType;
import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;
import org.eclipse.fennec.m2x.model.m2t.ProtectedAreaBlock;
import org.eclipse.fennec.m2x.model.m2t.Query;
import org.eclipse.fennec.m2x.model.m2t.QueryInvocation;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.TemplateExpression;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;
import org.eclipse.fennec.m2x.model.m2t.TextExpression;
import org.eclipse.fennec.m2x.model.m2t.TraceBlock;
import org.eclipse.fennec.m2x.model.m2t.util.M2tSwitch;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions.NullHandling;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.api.OclContext;

/**
 * Switch-based interpreter for MOFM2T template evaluation.
 *
 * <p>Evaluates the EMF-based M2T AST by dispatching each node type via
 * {@link M2tSwitch}. OCL expressions are evaluated by delegating to the
 * composed {@link OclEngine}.
 *
 * <p>Usage:
 * <pre>
 * M2tEvaluator evaluator = new M2tEvaluator(oclEngine, env, writers, module);
 * evaluator.execute(mainTemplate, context.inputElements());
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class M2tEvaluator {

	private static final String SOURCE_ID = "m2t.engine";
	private static final Object WRAPPED_NULL = new Object();

	private final OclEngine oclEngine;
	private final OclEvaluationOptions oclOptions;
	private final M2tEvalEnvironment env;
	private final M2tWriterStack writers;
	private final List<Diagnostic> diagnostics = new ArrayList<>();
	private final int maxDiagnostics;
	private final int maxTemplateDepth;
	private final int maxForIterations;
	private final int maxCrossProductSize;
	private final boolean protectedAreaEnabled;
	private int templateDepth;

	/** Maps overridden template → list of overriding templates. */
	private final Map<Template, List<Template>> overrideIndex = new IdentityHashMap<>();

	/**
	 * One extent per model root, so {@code allInstances()} in a loop does not rescan the model
	 * once per pass. Keyed by identity: a root is the same root only if it is the same object.
	 */
	private final Map<EObject, M2tModelExtent> extents = new IdentityHashMap<>();

	/** The elements the generation was started with — the extent when {@code self} is not one. */
	private List<EObject> inputElements = List.of();

	/** The extent around those input elements, built once it is asked for. */
	private M2tModelExtent inputExtent;

	/**
	 * The template being executed, for diagnostics. Without it a warning names the expression
	 * but not the place it stands in, which in a generator of any size is the part an author
	 * needs — one message per template beats one message per run.
	 */
	private Template executing;

	/** Maps standalone template invocations to their indentation string (§8.4). */
	private final Map<TemplateInvocation, String> indentationMap;

	/** Stack for [super/] calls: each entry holds the overridden template + argument values. */
	private final Deque<SuperContext> superStack = new ArrayDeque<>();

	private final TemplateSwitch templateSwitch = new TemplateSwitch();

	/**
	 * Creates a new evaluator.
	 *
	 * @param oclEngine the OCL engine for expression evaluation
	 * @param env the variable environment
	 * @param writers the writer stack for output routing
	 * @param module the module being executed
	 * @param allLinkedModules all modules in the link set (for override resolution)
	 * @param indentationMap standalone invocation indentation map (§8.4)
	 * @param maxDiagnostics maximum diagnostic entries before truncation
	 * @param maxTemplateDepth maximum template invocation depth (T-1)
	 * @param maxForIterations maximum for-block iterations (T-2)
	 * @param maxCrossProductSize maximum cross-product size (T-3)
	 * @param protectedAreaEnabled whether to emit protected area markers (T-6)
	 */
	public M2tEvaluator(OclEngine oclEngine, M2tEvalEnvironment env,
			M2tWriterStack writers, Module module, Collection<Module> allLinkedModules,
			Map<TemplateInvocation, String> indentationMap, int maxDiagnostics,
			int maxTemplateDepth, int maxForIterations, int maxCrossProductSize,
			boolean protectedAreaEnabled) {
		this.oclEngine = Objects.requireNonNull(oclEngine, "oclEngine must not be null");
		// MOFM2T §8.3 string operations ride along with every evaluation: the engine
		// belongs to the caller, so M2T adds what it needs instead of building its own.
		// LENIENT null handling is what a generator wants and what D14 asked for: navigating
		// from an unset value leaves an empty spot in the document instead of writing the word
		// OclInvalid into it. The engine reports each occurrence as a warning, forwarded in
		// evaluateOcl, so the author still learns which expression came up empty (#114).
		this.oclOptions = oclEngine.getDefaultOptions()
				.withCustomOperationsEnabled(true)
				.withNullHandling(NullHandling.LENIENT)
				.withAdditionalProviders(withStandardLibrary(oclEngine.getDefaultOptions()));
		this.env = Objects.requireNonNull(env, "env must not be null");
		this.writers = Objects.requireNonNull(writers, "writers must not be null");
		this.indentationMap = Objects.requireNonNull(indentationMap, "indentationMap must not be null");
		this.maxDiagnostics = maxDiagnostics;
		this.maxTemplateDepth = maxTemplateDepth;
		this.maxForIterations = maxForIterations;
		this.maxCrossProductSize = maxCrossProductSize;
		this.protectedAreaEnabled = protectedAreaEnabled;
		Objects.requireNonNull(module, "module must not be null");
		buildOverrideIndex(allLinkedModules);
	}

	/**
	 * Creates a new evaluator without indentation map (for programmatic AST usage).
	 */
	public M2tEvaluator(OclEngine oclEngine, M2tEvalEnvironment env,
			M2tWriterStack writers, Module module, Collection<Module> allLinkedModules) {
		this(oclEngine, env, writers, module, allLinkedModules, Map.of(), 10_000, 1_000, 1_000_000, 1_000_000, true);
	}

	/**
	 * Builds override index from all linked modules.
	 * Maps each overridden template to its list of overriding templates.
	 */
	private void buildOverrideIndex(Collection<Module> modules) {
		for (Module mod : modules) {
			collectOverrides(mod);
		}
	}

	private void collectOverrides(Module mod) {
		for (ModuleElement element : mod.getOwnedModuleElement()) {
			if (element instanceof Template t) {
				for (Template overridden : t.getOverrides()) {
					overrideIndex.computeIfAbsent(overridden, k -> new ArrayList<>()).add(t);
				}
			}
		}
	}

	/**
	 * Executes a template with the given arguments.
	 *
	 * @param template the template to execute
	 * @param args the arguments to bind to the template parameters
	 * @return the generated text output
	 */
	public String execute(Template template, List<? extends EObject> args) {
		Objects.requireNonNull(template, "template must not be null");

		if (templateDepth == 0) {
			// The outermost call is the one the caller started the generation with, so its
			// arguments are the input elements — no second channel needed to learn them.
			inputElements = List.copyOf(args);
		}

		if (templateDepth >= maxTemplateDepth) {
			addError("Maximum template depth exceeded (" + maxTemplateDepth
					+ ") — possible infinite recursion in template '" + template.getName() + "'");
			return "";
		}
		templateDepth++;
		Template enclosing = executing;
		executing = template;
		env.pushScope();
		try {
			// Bind template parameters
			List<Variable> params = template.getParameter();
			for (int i = 0; i < params.size() && i < args.size(); i++) {
				env.define(params.get(i).getName(), args.get(i));
			}
			// Also bind 'self' to first argument if present
			if (!args.isEmpty()) {
				env.define("self", args.get(0));
			}

			// Evaluate guard if present
			if (template.getGuard() != null) {
				Object guardResult = evaluateOcl(template.getGuard());
				if (!isTrue(guardResult)) {
					return "";
				}
			}

			// Execute init section
			if (template.getInit() != null) {
				for (Variable v : template.getInit().getVariable()) {
					Object initValue = v.getOwnedInit() != null
							? evaluateOcl(v.getOwnedInit())
							: null;
					env.define(v.getName(), initValue);
				}
			}

			// Execute body
			writers.pushStringWriter();
			executeBody(template);
			String result = writers.popWriter();

			// Apply post-processing if present
			if (template.getPost() != null) {
				env.define("self", result);
				Object postResult = evaluateOcl(template.getPost());
				result = asString(postResult);
			}

			return result;
		} finally {
			env.popScope();
			templateDepth--;
			executing = enclosing;
		}
	}

	/**
	 * Returns the collected diagnostics.
	 */
	public List<Diagnostic> getDiagnostics() {
		return List.copyOf(diagnostics);
	}

	// --- Template Switch ---

	private class TemplateSwitch extends M2tSwitch<Object> {

		@Override
		public Object caseTextExpression(TextExpression expr) {
			writers.append(expr.getValue());
			return WRAPPED_NULL;
		}

		@Override
		public Object caseForBlock(ForBlock block) {
			Object iterSetResult = evaluateOcl(block.getIterSet());
			if (!(iterSetResult instanceof Collection<?> collection)) {
				return WRAPPED_NULL;
			}

			List<?> items = new ArrayList<>(collection);
			// Apply guard filter if present
			if (block.getGuard() != null) {
				items = items.stream().filter(item -> {
					env.pushScope();
					try {
						String varName = block.getLoopVariable() != null
								? block.getLoopVariable().getName() : "self";
						env.define(varName, item);
						return isTrue(evaluateOcl(block.getGuard()));
					} finally {
						env.popScope();
					}
				}).toList();
			}

			if (items.isEmpty()) {
				return WRAPPED_NULL;
			}

			// Evaluate before expression (outside loop scope, before first iteration)
			if (block.getBefore() != null) {
				writers.append(asString(evaluateOcl(block.getBefore())));
			}

			int iterLimit = Math.min(items.size(), maxForIterations);
			for (int i = 0; i < items.size(); i++) {
				if (i >= iterLimit) {
					addError("Maximum for-block iterations exceeded (" + maxForIterations
							+ ") — loop terminated early");
					break;
				}
				env.pushScope();
				try {
					String varName = block.getLoopVariable() != null
							? block.getLoopVariable().getName() : "self";
					env.define(varName, items.get(i));
					// Also define 'i' as loop counter (1-based, Acceleo convention)
					env.define("i", i + 1);

					// Separator evaluated between iterations, with 'i' in scope
					if (i > 0 && block.getEach() != null) {
						writers.append(asString(evaluateOcl(block.getEach())));
					}

					executeBody(block);
				} finally {
					env.popScope();
				}
			}

			// Evaluate after expression (outside loop scope, after last iteration)
			if (block.getAfter() != null) {
				writers.append(asString(evaluateOcl(block.getAfter())));
			}

			return WRAPPED_NULL;
		}

		@Override
		public Object caseIfBlock(IfBlock block) {
			Object condResult = evaluateOcl(block.getIfExpr());
			if (isTrue(condResult)) {
				executeBody(block);
				return WRAPPED_NULL;
			}

			// Check elseIf chain
			for (IfBlock elseIf : block.getElseIf()) {
				Object elseIfResult = evaluateOcl(elseIf.getIfExpr());
				if (isTrue(elseIfResult)) {
					executeBody(elseIf);
					return WRAPPED_NULL;
				}
			}

			// Else branch
			if (block.getElse() != null) {
				executeBody(block.getElse());
			}

			return WRAPPED_NULL;
		}

		@Override
		public Object caseLetBlock(LetBlock block) {
			// Inline expression wrapper — special case, no elseLet/else semantics
			Variable letVar = block.getLetVariable();
			if (block.getBody().isEmpty() && letVar.getName().startsWith("__inline__")) {
				Object value = letVar.getOwnedInit() != null
						? evaluateOcl(letVar.getOwnedInit()) : null;
				writers.append(asString(value));
				return WRAPPED_NULL;
			}

			// MOFM2T §8.1.16: LetBlock is a subtype test chain.
			// Evaluate init — if value is non-null (cast succeeded), bind and execute body.
			// Otherwise, try elseLet chain, then else block.
			if (tryLetBlock(block)) {
				return WRAPPED_NULL;
			}

			// Try elseLet chain
			for (LetBlock elseLet : block.getElseLet()) {
				if (tryLetBlock(elseLet)) {
					return WRAPPED_NULL;
				}
			}

			// Else block
			if (block.getElse() != null) {
				executeBody(block.getElse());
			}

			return WRAPPED_NULL;
		}

		@Override
		public Object caseFileBlock(FileBlock block) {
			String filePath = asString(evaluateOcl(block.getFileUrl()));
			OpenModeKind mode = block.getOpenMode();
			Charset charset = StandardCharsets.UTF_8;
			if (block.getCharset() != null) {
				String charsetName = asString(evaluateOcl(block.getCharset()));
				try {
					charset = Charset.forName(charsetName);
				} catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
					addWarning("Unknown charset '" + charsetName + "', using UTF-8");
				}
			}

			// §8.1.17: Evaluate optional uniqId for file-rename tracking
			if (block.getUniqId() != null) {
				String uniqId = asString(evaluateOcl(block.getUniqId()));
				writers.setFileUniqueId(filePath, uniqId);
			}

			writers.pushFileWriter(filePath, mode, charset);
			executeBody(block);
			writers.popWriter();

			return WRAPPED_NULL;
		}

		@Override
		public Object caseProtectedAreaBlock(ProtectedAreaBlock block) {
			if (!protectedAreaEnabled) {
				// T-6: Protected areas disabled — emit body content without markers
				executeBody(block);
				return WRAPPED_NULL;
			}

			String markerId = asString(evaluateOcl(block.getMarker()));

			// Execute body first to capture default content and compute hash (D31)
			writers.pushStringWriter();
			executeBody(block);
			String defaultContent = writers.popWriter();

			writers.append(M2tProtectedAreaMerger.startMarker(markerId, defaultContent));
			writers.append("\n");
			writers.append(defaultContent);
			writers.append(M2tProtectedAreaMerger.endMarker());

			return WRAPPED_NULL;
		}

		@Override
		public Object caseTraceBlock(TraceBlock block) {
			// Trace blocks are evaluated but the traceability info is not yet used
			executeBody(block);
			return WRAPPED_NULL;
		}

		@Override
		public Object caseTemplateInvocation(TemplateInvocation invocation) {
			// Check for indent-propagation (§8.4 Rule 4)
			String indent = indentationMap.get(invocation);
			if (indent != null && !indent.isEmpty()) {
				writers.pushStringWriter();
				Object result = doTemplateInvocation(invocation);
				String output = writers.popWriter();
				writers.append(fitIndentationTo(output, indent));
				return result;
			}
			return doTemplateInvocation(invocation);
		}

		private Object doTemplateInvocation(TemplateInvocation invocation) {
			// Handle [super/]
			if (invocation.isSuper()) {
				SuperContext ctx = superStack.peek();
				if (ctx == null) {
					addError("[super/] called outside of an override context");
					return WRAPPED_NULL;
				}
				Template overridden = ctx.overridden;
				// If the overridden template itself overrides something, set up super context
				if (!overridden.getOverrides().isEmpty()) {
					Template nextSuper = overridden.getOverrides().get(0);
					superStack.push(new SuperContext(nextSuper, ctx.args));
					try {
						invokeTemplate(overridden, ctx.args);
					} finally {
						superStack.pop();
					}
				} else {
					invokeTemplate(overridden, ctx.args);
				}
				return WRAPPED_NULL;
			}

			Template target = invocation.getDefinition();
			if (target == null) {
				addError("Unresolved template invocation — definition is null");
				return WRAPPED_NULL;
			}

			// Evaluate all arguments once
			List<Object> evalArgs = new ArrayList<>();
			for (OclExpression argExpr : invocation.getArgument()) {
				evalArgs.add(evaluateOcl(argExpr));
			}

			// Override resolution: check if an overriding template applies
			Template resolved = resolveOverride(target, evalArgs);

			// §8.1.10: Determine which arguments are sets with singleton parameters
			// If argument is a set and parameter is singleton → iterate
			List<Variable> params = resolved.getParameter();
			List<List<?>> setArgs = new ArrayList<>();
			boolean hasSetIteration = false;

			for (int i = 0; i < evalArgs.size() && i < params.size(); i++) {
				Object arg = evalArgs.get(i);
				if (arg instanceof Collection<?> coll) {
					setArgs.add(new ArrayList<>(coll));
					hasSetIteration = true;
				} else {
					setArgs.add(List.of(arg != null ? arg : WRAPPED_NULL));
				}
			}

			if (hasSetIteration) {
				// T-3: Check cross-product size before computing
				long productSize = 1;
				for (List<?> setArg : setArgs) {
					productSize *= setArg.size();
					if (productSize > maxCrossProductSize) {
						addError("Cross-product size exceeds limit (" + maxCrossProductSize
								+ ") — template invocation skipped");
						return WRAPPED_NULL;
					}
				}
				List<List<Object>> crossProduct = crossProduct(setArgs);

				String before = invocation.getBefore() != null
						? asString(evaluateOcl(invocation.getBefore())) : null;
				String each = invocation.getEach() != null
						? asString(evaluateOcl(invocation.getEach())) : null;
				String after = invocation.getAfter() != null
						? asString(evaluateOcl(invocation.getAfter())) : null;

				if (!crossProduct.isEmpty()) {
					if (before != null) writers.append(before);
					for (int i = 0; i < crossProduct.size(); i++) {
						if (i > 0 && each != null) writers.append(each);
						invokeTemplateWithOverride(resolved, target, crossProduct.get(i));
					}
					if (after != null) writers.append(after);
				}
			} else {
				invokeTemplateWithOverride(resolved, target, evalArgs);
			}

			return WRAPPED_NULL;
		}

		@Override
		public Object caseQueryInvocation(QueryInvocation invocation) {
			Query query = invocation.getDefinition();
			if (query == null) {
				addError("Unresolved query invocation — definition is null");
				return WRAPPED_NULL;
			}

			env.pushScope();
			try {
				// Evaluate all arguments once
				List<Variable> params = query.getParameter();
				List<Object> evalArgs = new ArrayList<>();
				for (int i = 0; i < invocation.getArgument().size(); i++) {
					evalArgs.add(evaluateOcl(invocation.getArgument().get(i)));
				}
				for (int i = 0; i < params.size() && i < evalArgs.size(); i++) {
					env.define(params.get(i).getName(), evalArgs.get(i));
				}
				if (!evalArgs.isEmpty()) {
					env.define("self", evalArgs.get(0));
				}

				Object result = evaluateOcl(query.getExpression());
				writers.append(asString(result));
			} finally {
				env.popScope();
			}

			return WRAPPED_NULL;
		}

		@Override
		public Object caseMacroInvocation(MacroInvocation invocation) {
			Macro macro = invocation.getDefinition();
			if (macro == null) {
				addError("Unresolved macro invocation — definition is null");
				return WRAPPED_NULL;
			}

			env.pushScope();
			try {
				// Evaluate all arguments once
				List<Variable> params = macro.getParameter();
				List<Object> evalArgs = new ArrayList<>();
				for (int i = 0; i < invocation.getArgument().size(); i++) {
					evalArgs.add(evaluateOcl(invocation.getArgument().get(i)));
				}
				for (int i = 0; i < params.size() && i < evalArgs.size(); i++) {
					env.define(params.get(i).getName(), evalArgs.get(i));
				}
				if (!evalArgs.isEmpty()) {
					env.define("self", evalArgs.get(0));
				}

				if (macro.getInit() != null) {
					for (Variable v : macro.getInit().getVariable()) {
						Object initValue = v.getOwnedInit() != null
								? evaluateOcl(v.getOwnedInit()) : null;
						env.define(v.getName(), initValue);
					}
				}

				executeBody(macro);
			} finally {
				env.popScope();
			}

			return WRAPPED_NULL;
		}

		@Override
		public Object caseOclExpression(OclExpression expr) {
			// Generic OCL expression in template position — evaluate and output result
			Object result = evaluateOcl(expr);
			writers.append(asString(result));
			return WRAPPED_NULL;
		}

		@Override
		public Object defaultCase(EObject object) {
			addWarning("Unhandled template element: " + object.eClass().getName());
			return WRAPPED_NULL;
		}
	}

	// --- Helpers ---

	private void executeBody(Block block) {
		for (TemplateExpression expr : block.getBody()) {
			templateSwitch.doSwitch(expr);
		}
	}

	/**
	 * Tries to execute a single LetBlock: evaluates init, if non-null binds
	 * variable and executes body, returns true. If null, returns false.
	 * (MOFM2T §8.1.16 subtype test semantics)
	 */
	private boolean tryLetBlock(LetBlock block) {
		Variable letVar = block.getLetVariable();
		Object value = letVar.getOwnedInit() != null
				? evaluateOcl(letVar.getOwnedInit()) : null;
		if (value == null) {
			return false;
		}
		env.pushScope();
		try {
			env.define(letVar.getName(), value);
			executeBody(block);
		} finally {
			env.popScope();
		}
		return true;
	}

	/**
	 * Evaluates an OCL expression using the composed OclEngine.
	 * Variables from the M2T environment are made available as the OCL context.
	 */
	Object evaluateOcl(OclExpression expression) {
		if (expression == null) {
			return null;
		}

		// Collect all environment variables (excluding 'self')
		Map<String, Object> variables = new LinkedHashMap<>();
		for (var entry : env.allVisibleVariables().entrySet()) {
			if (!"self".equals(entry.getKey())) {
				variables.put(entry.getKey(), entry.getValue());
			}
		}

		// Use the current 'self' value as OCL context
		Object self = env.lookup("self");
		OclContext oclContext;
		if (self instanceof EObject eo) {
			oclContext = new OclContext(eo, extentAround(eo), variables);
		} else {
			// No EObject self — use variables-only context, and the input model as the extent,
			// because a string context names no model to navigate from
			if (self != null) {
				variables.put("self", self);
			}
			oclContext = new OclContext(null, inputExtent(), variables);
		}

		try {
			// With diagnostics, not without: what an expression reports is what tells a template
			// author why a spot in the document came out empty. Dropping them left the generation
			// reporting success while the document said otherwise (#114).
			OclResult result = oclEngine.evaluateWithDiagnostics(expression, oclContext, oclOptions);
			result.diagnostics().forEach(this::addOclDiagnostic);
			return result.value();
		} catch (RuntimeException e) {
			addError("OCL evaluation error [" + e.getClass().getSimpleName() + "]: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Takes over a diagnostic an OCL expression reported, keeping its severity.
	 *
	 * <p>The severity is the engine's to decide, not M2T's: an unset value that leniently
	 * evaluates to nothing is a warning and leaves the generation successful, while an
	 * expression that cannot be evaluated at all is an error and should stop counting as one.
	 */
	private void addOclDiagnostic(Diagnostic diagnostic) {
		addDiagnostic(diagnostic.getSeverity(), diagnostic.getMessage());
	}

	/**
	 * The extent {@code allInstances()} resolves in for the given context object, built once
	 * per model root and reused — see {@link M2tModelExtent} for what the scope is and why.
	 */
	private M2tModelExtent extentAround(EObject self) {
		return extents.computeIfAbsent(EcoreUtil.getRootContainer(self),
				root -> M2tModelExtent.aroundSelf(root));
	}

	/**
	 * The extent around the generation's input elements, for a context that is not an
	 * {@code EObject}. {@code null} when there are no input elements, which leaves
	 * {@code allInstances()} reporting a missing extent rather than answering emptily.
	 */
	private M2tModelExtent inputExtent() {
		if (inputElements.isEmpty()) {
			return null;
		}
		if (inputExtent == null) {
			inputExtent = M2tModelExtent.aroundAll(inputElements);
		}
		return inputExtent;
	}

	/**
	 * Returns the additional providers of the given options with the MOFM2T standard
	 * library appended, so a caller's own providers survive.
	 */
	private static List<OclOperationProvider> withStandardLibrary(OclEvaluationOptions options) {
		List<OclOperationProvider> providers = new ArrayList<>(options.additionalProviders());
		providers.add(new M2tStandardLibrary());
		return List.copyOf(providers);
	}

	/**
	 * Invokes a template, pushing super context if the resolved template differs
	 * from the original (i.e., an override was applied).
	 */
	private void invokeTemplateWithOverride(Template resolved, Template original,
			List<Object> argValues) {
		if (resolved != original) {
			// Find what the resolved template directly overrides (for [super/] target)
			Template superTarget = original; // default fallback
			if (!resolved.getOverrides().isEmpty()) {
				// The template's overrides list contains what it directly overrides
				superTarget = resolved.getOverrides().get(0);
			}
			superStack.push(new SuperContext(superTarget, argValues));
			try {
				invokeTemplate(resolved, argValues);
			} finally {
				superStack.pop();
			}
		} else {
			invokeTemplate(resolved, argValues);
		}
	}

	/**
	 * Resolves override for a target template: checks if any overriding template
	 * is applicable for the given arguments (parameter type compatibility + guard).
	 * Returns the overriding template if found, otherwise the original target.
	 *
	 * <p>MOFM2T §8.1.3: Override parameters must be type-compatible (same or narrower).
	 */
	private Template resolveOverride(Template target, List<Object> evalArgs) {
		Set<Template> visited = Collections.newSetFromMap(new IdentityHashMap<>());
		return resolveOverride(target, evalArgs, visited);
	}

	private Template resolveOverride(Template target, List<Object> evalArgs,
			Set<Template> visited) {
		if (!visited.add(target)) {
			return target; // cycle detected — stop resolution
		}
		List<Template> overrides = overrideIndex.get(target);
		if (overrides == null || overrides.isEmpty()) {
			return target;
		}

		for (Template override : overrides) {
			if (isOverrideApplicable(override, evalArgs)) {
				return resolveOverride(override, evalArgs, visited);
			}
		}

		return target;
	}

	/**
	 * Checks if an overriding template is applicable for the given argument values.
	 * All parameters must be type-compatible and the guard (if present) must be true.
	 */
	private boolean isOverrideApplicable(Template override, List<Object> evalArgs) {
		List<Variable> overrideParams = override.getParameter();

		// Check parameter type compatibility
		for (int i = 0; i < overrideParams.size() && i < evalArgs.size(); i++) {
			Object arg = evalArgs.get(i);
			if (arg == null || arg == WRAPPED_NULL) {
				continue; // null is compatible with any type
			}
			Variable param = overrideParams.get(i);
			if (param.getType() != null && arg instanceof EObject eo) {
				EClassifier paramType = resolveEClassifier(param.getType());
				if (paramType instanceof EClass eClass) {
					if (!eClass.isSuperTypeOf(eo.eClass())) {
						return false;
					}
				}
			}
		}

		// Check guard if present
		if (override.getGuard() != null) {
			env.pushScope();
			try {
				// Bind parameters for guard evaluation
				List<Variable> params = override.getParameter();
				for (int i = 0; i < params.size() && i < evalArgs.size(); i++) {
					Object val = evalArgs.get(i);
					if (val == WRAPPED_NULL) val = null;
					env.define(params.get(i).getName(), val);
				}
				if (!evalArgs.isEmpty()) {
					Object firstVal = evalArgs.get(0);
					if (firstVal == WRAPPED_NULL) firstVal = null;
					env.define("self", firstVal);
				}
				return isTrue(evaluateOcl(override.getGuard()));
			} finally {
				env.popScope();
			}
		}

		return true;
	}

	/**
	 * Resolves an OclType to an EClassifier for type checking.
	 */
	private EClassifier resolveEClassifier(org.eclipse.fennec.m2x.model.ocl.OclType type) {
		if (type instanceof ClassifierType ct) {
			return ct.getReferredClassifier();
		}
		return null;
	}

	/**
	 * Invokes a template with resolved argument values. Binds parameters
	 * to argument values and executes the template body.
	 */
	private void invokeTemplate(Template target, List<Object> argValues) {
		if (templateDepth >= maxTemplateDepth) {
			addError("Maximum template depth exceeded (" + maxTemplateDepth
					+ ") — possible infinite recursion in template '" + target.getName() + "'");
			return;
		}
		templateDepth++;
		env.pushScope();
		try {
			List<Variable> params = target.getParameter();
			for (int i = 0; i < params.size() && i < argValues.size(); i++) {
				Object val = argValues.get(i);
				if (val == WRAPPED_NULL) val = null;
				env.define(params.get(i).getName(), val);
			}
			if (!argValues.isEmpty()) {
				Object firstVal = argValues.get(0);
				if (firstVal == WRAPPED_NULL) firstVal = null;
				env.define("self", firstVal);
			}

			if (target.getGuard() != null) {
				if (!isTrue(evaluateOcl(target.getGuard()))) {
					return;
				}
			}

			if (target.getInit() != null) {
				for (Variable v : target.getInit().getVariable()) {
					Object initValue = v.getOwnedInit() != null
							? evaluateOcl(v.getOwnedInit()) : null;
					env.define(v.getName(), initValue);
				}
			}

			// Apply post-processing if present (Acceleo extension)
			if (target.getPost() != null) {
				writers.pushStringWriter();
				executeBody(target);
				String result = writers.popWriter();
				env.define("self", result);
				Object postResult = evaluateOcl(target.getPost());
				writers.append(asString(postResult));
			} else {
				executeBody(target);
			}
		} finally {
			env.popScope();
			templateDepth--;
		}
	}

	/**
	 * Computes cross-product of lists. Each input list has 1+ elements.
	 * Returns list of combinations, each combination has one element per input list.
	 */
	private static List<List<Object>> crossProduct(List<List<?>> lists) {
		List<List<Object>> result = new ArrayList<>();
		result.add(new ArrayList<>());
		for (List<?> list : lists) {
			List<List<Object>> newResult = new ArrayList<>();
			for (List<Object> existing : result) {
				for (Object item : list) {
					List<Object> combo = new ArrayList<>(existing);
					combo.add(item);
					newResult.add(combo);
				}
			}
			result = newResult;
		}
		return result;
	}

	private static String asString(Object value) {
		if (value == null || value == WRAPPED_NULL) {
			return "";
		}
		return value.toString();
	}

	private static boolean isTrue(Object value) {
		return Boolean.TRUE.equals(value);
	}

	/**
	 * Adds an output size limit error diagnostic. Called by the engine after execution
	 * when the writer stack's output limit was exceeded (T-7).
	 *
	 * @param maxOutputSize the configured limit
	 */
	public void addOutputLimitError(long maxOutputSize) {
		addError("Maximum output size exceeded (" + maxOutputSize
				+ " characters) — output truncated");
	}

	/**
	 * Adds an error diagnostic for a file the generation strategy could not write.
	 * Called by the engine after execution; the content stays in the result.
	 *
	 * @param filePath the file that failed
	 * @param cause the failure
	 */
	public void addGenerationError(String filePath, Exception cause) {
		addError("Failed to write generated file '" + filePath + "' ["
				+ cause.getClass().getSimpleName() + "]: " + cause.getMessage());
	}

	private void addError(String message) {
		addDiagnostic(Diagnostic.ERROR, message);
	}

	private void addWarning(String message) {
		addDiagnostic(Diagnostic.WARNING, message);
	}

	/**
	 * Adds a diagnostic entry, respecting {@code maxDiagnostics} limit.
	 */
	private void addDiagnostic(int severity, String message) {
		if (diagnostics.size() >= maxDiagnostics) {
			if (diagnostics.size() == maxDiagnostics) {
				diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING, SOURCE_ID, 0,
						"Maximum diagnostics limit reached (" + maxDiagnostics
								+ "), further diagnostics truncated", null));
			}
			return;
		}
		diagnostics.add(new BasicDiagnostic(severity, SOURCE_ID, 0, located(message), null));
	}

	/**
	 * Names the place a diagnostic comes from: the template being executed and, when a
	 * {@code [file]} block is open, the document being written.
	 *
	 * <p>The M2T metamodel carries no positions, so this is what is available — and it is what
	 * turns "some expression came up empty" into something an author can act on. Line and column
	 * need positions in the AST and in the OCL diagnostics (#110, #116).
	 */
	private String located(String message) {
		if (executing == null) {
			return message;
		}
		String target = writers.currentTargetName();
		return target == null
				? "[template " + executing.getName() + "] " + message
				: "[template " + executing.getName() + " → " + target + "] " + message;
	}

	/**
	 * Applies indent-propagation (§8.4 Rule 4): after each newline in the source,
	 * inserts the given indentation string. The first line is NOT indented.
	 */
	private static String fitIndentationTo(String source, String indentation) {
		if (indentation.isEmpty() || source.isEmpty()) {
			return source;
		}
		return source.replaceAll("(\r\n|\r|\n)", "$1" + Matcher.quoteReplacement(indentation));
	}

	/**
	 * Context for [super/] calls: the overridden template and its argument values.
	 */
	private record SuperContext(Template overridden, List<Object> args) {}
}
