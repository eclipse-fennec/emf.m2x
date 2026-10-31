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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
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
import org.eclipse.fennec.m2x.model.m2t.InlineExpression;
import org.eclipse.fennec.m2x.model.m2t.LetBlock;
import org.eclipse.fennec.m2x.model.m2t.M2tPackage;
import org.eclipse.fennec.m2x.model.m2t.Macro;
import org.eclipse.fennec.m2x.model.m2t.MacroInvocation;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.ModuleElement;
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
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions.NullHandling;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;
import org.eclipse.fennec.m2x.ocl.api.OclValues;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;
import org.eclipse.fennec.m2x.ocl.parser.typing.OclTypeRules;

/**
 * Switch-based interpreter for MOFM2T template evaluation.
 *
 * <p>Evaluates the EMF-based M2T AST by dispatching each node type via
 * {@link M2tSwitch}. OCL expressions are evaluated by delegating to the
 * composed {@link OclEngine}.
 *
 * <p>Usage:
 * <pre>
 * M2tEvaluator evaluator = new M2tEvaluator(oclEngine, env, writers, module, List.of(module));
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
	private final M2tLimits limits;
	private int templateDepth;
	private int queryDepth;
	/** The deadline of this generation in {@link System#nanoTime()} terms, 0 for none (#261). */
	private long deadlineNanos;
	/** Set once the deadline was noticed, so it is reported once and stops everything after. */
	private boolean timedOut;

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

	/**
	 * Where each expression node stood in its template. The OCL engine says which node a
	 * diagnostic is about; this says where that node is (#116).
	 */
	private final Map<EObject, SourcePosition> positions;

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
	 * @param linkSet the modules this generation runs against and what is known about them
	 * @param limits what this generation is allowed to do
	 */
	public M2tEvaluator(OclEngine oclEngine, M2tEvalEnvironment env,
			M2tWriterStack writers, Module module, M2tLinkSet linkSet, M2tLimits limits) {
		this.oclEngine = Objects.requireNonNull(oclEngine, "oclEngine must not be null");
		this.env = Objects.requireNonNull(env, "env must not be null");
		Objects.requireNonNull(module, "module must not be null");
		Objects.requireNonNull(linkSet, "linkSet must not be null");
		// MOFM2T §8.3 string operations ride along with every evaluation: the engine
		// belongs to the caller, so M2T adds what it needs instead of building its own.
		// LENIENT null handling is what a generator wants and what D14 asked for: navigating
		// from an unset value leaves an empty spot in the document instead of writing the word
		// OclInvalid into it. The engine reports each occurrence as a warning, forwarded in
		// evaluateOcl, so the author still learns which expression came up empty (#114).
		this.oclOptions = oclEngine.getDefaultOptions()
				.withCustomOperationsEnabled(true)
				.withNullHandling(NullHandling.LENIENT)
				.withAdditionalProviders(withStandardLibraryAndQueries(
						oclEngine.getDefaultOptions(), module, this::invokeQueryValue, this::invokeTemplateValue));
		this.writers = Objects.requireNonNull(writers, "writers must not be null");
		this.indentationMap = linkSet.indentation();
		this.positions = linkSet.positions();
		this.limits = Objects.requireNonNull(limits, "limits must not be null");
		buildOverrideIndex(linkSet.modules());
	}

	/**
	 * Creates a new evaluator on an AST alone, under the default limits.
	 *
	 * @param oclEngine the OCL engine for expression evaluation
	 * @param env the variable environment
	 * @param writers the writer stack for output routing
	 * @param module the module being executed
	 * @param allLinkedModules the modules this generation runs against
	 */
	public M2tEvaluator(OclEngine oclEngine, M2tEvalEnvironment env,
			M2tWriterStack writers, Module module, Collection<Module> allLinkedModules) {
		this(oclEngine, env, writers, module, M2tLinkSet.of(allLinkedModules), M2tLimits.defaults());
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
			deadlineNanos = limits.timeoutMs() > 0
					? System.nanoTime() + limits.timeoutMs() * 1_000_000L
					: 0;
			timedOut = false;
			// The outermost call is the one the caller started the generation with, so its
			// arguments are the input elements — no second channel needed to learn them.
			inputElements = List.copyOf(args);
		}

		if (templateDepth >= limits.maxTemplateDepth()) {
			addError("Maximum template depth exceeded (" + limits.maxTemplateDepth()
					+ ") — possible infinite recursion in template '" + template.getName() + "'");
			return "";
		}
		if (pastDeadline()) {
			return "";
		}
		templateDepth++;
		Template enclosing = executing;
		executing = template;
		env.pushFrame();
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

			if (template.getGuard() != null && !holds(template.getGuard(), "guard of template '" + template.getName() + "'")) {
				return "";
			}

			// Execute body, after the init section
			writers.pushStringWriter();
			executeBody(template);
			String result = writers.popWriter();

			// Apply post-processing if present
			if (template.getPost() != null) {
				env.define("self", result);
				result = render(template.getPost());
			}

			return result;
		} finally {
			env.popFrame();
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
			List<?> items = iterationItems(block);
			// Apply guard filter if present
			if (block.getGuard() != null) {
				items = items.stream().filter(item -> {
					env.pushScope();
					try {
						String varName = block.getLoopVariable() != null
								? block.getLoopVariable().getName() : "self";
						env.define(varName, item);
						return holds(block.getGuard(), "guard of a for block");
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
				writers.append(render(block.getBefore()));
			}

			int iterLimit = Math.min(items.size(), limits.maxForIterations());
			for (int i = 0; i < items.size(); i++) {
				if (i >= iterLimit) {
					addError("Maximum for-block iterations exceeded (" + limits.maxForIterations()
							+ ") — loop terminated early");
					break;
				}
				if (pastDeadline()) {
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
						writers.append(render(block.getEach()));
					}

					executeBody(block);
				} finally {
					env.popScope();
				}
			}

			// Evaluate after expression (outside loop scope, after last iteration)
			if (block.getAfter() != null) {
				writers.append(render(block.getAfter()));
			}

			return WRAPPED_NULL;
		}

		@Override
		public Object caseIfBlock(IfBlock block) {
			// §8.1.21: a condition that is neither true nor false takes no branch, the else
			// branch included — it reports an ERROR instead (#366)
			Boolean condition = condition(block.getIfExpr(), "condition of an if block");
			if (condition == null) {
				return WRAPPED_NULL;
			}
			if (condition) {
				executeBody(block);
				return WRAPPED_NULL;
			}

			// Check elseIf chain
			for (IfBlock elseIf : block.getElseIf()) {
				Boolean elseIfCondition = condition(elseIf.getIfExpr(), "condition of an elseif");
				if (elseIfCondition == null) {
					return WRAPPED_NULL;
				}
				if (elseIfCondition) {
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
		public Object caseInlineExpression(InlineExpression inline) {
			// §7.4: a macro's Body parameter, referred to, inserts the body of the call
			if (inline.getExpression() instanceof VariableExp reference && reference.getReferredVariable() != null
					&& env.lookup(reference.getReferredVariable().getName()) instanceof BodyArgument body) {
				expandBody(body);
				return WRAPPED_NULL;
			}
			// §8.2 '[' OclExpressionCS '/]': the value goes into the document (#370)
			writers.append(render(inline.getExpression()));
			return WRAPPED_NULL;
		}

		@Override
		public Object caseLetBlock(LetBlock block) {

			// MOFM2T §8.1.16: a let block is a subtype test chain — the first let or elselet whose
			// value is an instance of its declared type runs, otherwise the else block. An invalid
			// value ends the whole block (#363).
			for (LetBlock candidate : letChain(block)) {
				LetOutcome outcome = tryLetBlock(candidate);
				if (outcome != LetOutcome.DECLINED) {
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
			String filePath = render(block.getFileUrl());
			if (filePath.isEmpty()) {
				// Acceleo stops the generation here ("undefined file URL"); this one skips the
				// block, so the rest of the document still gets its say, and names the block.
				// Writing on would have produced a file called OclInvalid (#270).
				addDiagnostic(Diagnostic.ERROR, "[file] URL evaluated to nothing — block skipped",
						positionOf(block.getFileUrl()));
				return WRAPPED_NULL;
			}
			OpenModeKind mode = block.getOpenMode();
			Charset charset = StandardCharsets.UTF_8;
			if (block.getCharset() != null) {
				String charsetName = render(block.getCharset());
				try {
					charset = Charset.forName(charsetName);
				} catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
					addWarning("Unknown charset '" + charsetName + "', using UTF-8");
				}
			}

			// §8.1.17: Evaluate optional uniqId for file-rename tracking
			if (block.getUniqId() != null) {
				String uniqId = render(block.getUniqId());
				writers.setFileUniqueId(filePath, uniqId);
			}

			writers.pushFileWriter(filePath, mode, charset);
			executeBody(block);
			writers.popWriter();

			return WRAPPED_NULL;
		}

		@Override
		public Object caseProtectedAreaBlock(ProtectedAreaBlock block) {
			if (!limits.protectedAreaEnabled()) {
				// T-6: Protected areas disabled — emit body content without markers
				executeBody(block);
				return WRAPPED_NULL;
			}

			String markerId = render(block.getMarker());

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
			// D15 (#371): a trace block emits its body; the trace information itself is not
			// recorded. Its model element is evaluated all the same — a mistake in it went
			// unnoticed before
			if (block.getModelElement() != null && evaluateOcl(block.getModelElement()) == OclInvalid.INSTANCE) {
				addDiagnostic(Diagnostic.ERROR, "The model element of a trace block is invalid (MOFM2T §8.1.18)",
						positionOf(block.getModelElement()));
			}
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
				invokeTemplateWithOverride(ctx.overridden, ctx.invoked, ctx.args);
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

			List<List<Object>> invocations = invocationArguments(target, evalArgs);
			if (invocations == null) {
				return WRAPPED_NULL;
			}

			// §8.1.10: before goes in front of the first invocation, the separator between two,
			// after behind the last — of the invocations that took place: a template its guard
			// declines was not invoked (#364)
			String each = null;
			boolean invoked = false;
			for (List<Object> arguments : invocations) {
				writers.pushStringWriter();
				boolean ran;
				String output;
				try {
					ran = invokeChecked(target, arguments, invocation.getArgument());
				} finally {
					output = writers.popWriter();
				}
				if (!ran) {
					continue;
				}
				if (!invoked) {
					if (invocation.getBefore() != null) {
						writers.append(render(invocation.getBefore()));
					}
				} else if (invocation.getEach() != null) {
					if (each == null) {
						each = render(invocation.getEach());
					}
					writers.append(each);
				}
				invoked = true;
				writers.append(output);
			}
			if (invoked && invocation.getAfter() != null) {
				writers.append(render(invocation.getAfter()));
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

			// Evaluate all arguments once, in the caller's scope
			List<Variable> params = query.getParameter();
			List<Object> evalArgs = new ArrayList<>();
			for (int i = 0; i < invocation.getArgument().size(); i++) {
				evalArgs.add(evaluateOcl(invocation.getArgument().get(i)));
			}
			env.pushFrame();
			try {
				for (int i = 0; i < params.size() && i < evalArgs.size(); i++) {
					env.define(params.get(i).getName(), evalArgs.get(i));
				}
				if (!evalArgs.isEmpty()) {
					env.define("self", evalArgs.get(0));
				}

				OclResult result = evaluate(query.getExpression());
				checkQueryResult(query, result == null ? null : result.value());
				writers.append(render(result, query.getExpression()));
			} finally {
				env.popFrame();
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

			// A macro calling itself is bounded like a template doing so (#371)
			if (templateDepth >= limits.maxTemplateDepth()) {
				addError("Maximum template depth exceeded (" + limits.maxTemplateDepth()
						+ ") — possible infinite recursion in macro '" + macro.getName() + "'");
				return WRAPPED_NULL;
			}
			// Evaluate all arguments once, in the caller's scope; the body of the call is kept
			// with that scope, as §7.4 expands it "in the context of the macro call" (#371)
			List<Variable> params = macro.getParameter();
			List<Object> evalArgs = new ArrayList<>();
			for (OclExpression argument : invocation.getArgument()) {
				evalArgs.add(argument instanceof Block body ? new BodyArgument(body, env.allVisibleVariables())
						: evaluateOcl(argument));
			}
			env.pushFrame();
			try {
				for (int i = 0; i < params.size() && i < evalArgs.size(); i++) {
					env.define(params.get(i).getName(), evalArgs.get(i));
				}
				if (!evalArgs.isEmpty()) {
					env.define("self", evalArgs.get(0));
				}

				templateDepth++;
				try {
					executeBody(macro);
				} finally {
					templateDepth--;
				}
			} finally {
				env.popFrame();
			}

			return WRAPPED_NULL;
		}

		@Override
		public Object caseOclExpression(OclExpression expr) {
			// Generic OCL expression in template position — evaluate and output result
			writers.append(render(expr));
			return WRAPPED_NULL;
		}

		@Override
		public Object defaultCase(EObject object) {
			addWarning("Unhandled template element: " + object.eClass().getName());
			return WRAPPED_NULL;
		}
	}

	// --- Helpers ---

	/**
	 * Executes a block: its init section, then its body (§8.1.4). The init variables go into the
	 * current scope — a template's stay visible to its post expression, a for block's are
	 * evaluated per iteration in the scope of the loop variable (#366).
	 */
	private void executeBody(Block block) {
		if (block.getInit() != null) {
			for (Variable v : block.getInit().getVariable()) {
				env.define(v.getName(), v.getOwnedInit() != null ? evaluateOcl(v.getOwnedInit()) : null);
			}
		}
		for (TemplateExpression expr : block.getBody()) {
			templateSwitch.doSwitch(expr);
		}
	}

	/** What one let or elselet of a let block did. */
	private enum LetOutcome {
		/** Its value had the declared type, and its body ran. */
		TAKEN,
		/** Its value was null or of another type: the next one is asked. */
		DECLINED,
		/** Its value was invalid: the let block produces nothing. */
		INVALID
	}

	/** The let block and its elselets, in the order they are tried. */
	private static List<LetBlock> letChain(LetBlock block) {
		List<LetBlock> chain = new ArrayList<>();
		chain.add(block);
		chain.addAll(block.getElseLet());
		return chain;
	}

	/**
	 * Tries one let or elselet (MOFM2T §7, §8.1.16): "a test that checks whether the object bound
	 * to the supertype variable is actually an instance of the specified subtype". {@code null} is
	 * no instance of any type; an untyped declaration takes the type of its value, so for it only
	 * {@code null} fails. An {@code invalid} value is an ERROR and ends the let block, the else
	 * branch included — the line of the conditions (#363, #366).
	 */
	private LetOutcome tryLetBlock(LetBlock block) {
		Variable letVar = block.getLetVariable();
		Object value = letVar.getOwnedInit() != null
				? evaluateOcl(letVar.getOwnedInit()) : null;
		if (value == OclInvalid.INSTANCE) {
			addDiagnostic(Diagnostic.ERROR, "The value of let '" + letVar.getName()
					+ "' is invalid — the let block produces nothing (MOFM2T §8.1.16)", positionOf(letVar.getOwnedInit()));
			return LetOutcome.INVALID;
		}
		if (value == null || !conformsAtRuntime(value, letVar.getType())) {
			return LetOutcome.DECLINED;
		}
		env.pushScope();
		try {
			env.define(letVar.getName(), value);
			executeBody(block);
		} finally {
			env.popScope();
		}
		return LetOutcome.TAKEN;
	}

	/**
	 * Evaluates an OCL expression using the composed OclEngine.
	 * Variables from the M2T environment are made available as the OCL context.
	 */
	Object evaluateOcl(OclExpression expression) {
		OclResult result = evaluate(expression);
		return result == null ? null : result.value();
	}

	/**
	 * Evaluates an expression whose value goes into the document.
	 *
	 * <p>{@code invalid} is not text. It is what an expression answers when it could not be
	 * evaluated — a null argument, an operation nobody provides, a failed cast — and writing
	 * its name into the document turned that into a word a reader had to search for, in a
	 * generation that counted as successful (#270). The spot stays empty instead, and the
	 * expression is named at its line: by the OCL engine's own diagnostic where it reported
	 * why, and by this one where the value turned invalid in silence — a literal
	 * {@code invalid}, or a failure the engine answers without a word.
	 */
	private String render(OclExpression expression) {
		return render(evaluate(expression), expression);
	}

	/** {@link #render(OclExpression)} for a result already evaluated. */
	private String render(OclResult result, OclExpression expression) {
		if (result == null) {
			return "";
		}
		if (result.value() == OclInvalid.INSTANCE) {
			if (result.isSuccess()) {
				addDiagnostic(Diagnostic.ERROR, "Expression evaluated to invalid — nothing written",
						positionOf(expression));
			}
			return "";
		}
		return asString(result.value());
	}

	/**
	 * Evaluates an expression and forwards what it reported; {@code null} for no expression.
	 */
	private OclResult evaluate(OclExpression expression) {
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
			return result;
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
		addDiagnostic(diagnostic.getSeverity(), diagnostic.getMessage(), positionOf(diagnostic));
	}

	/**
	 * The place the diagnostic's node stood in the template, or {@code null} when the engine named
	 * no node or the node came from somewhere this module did not parse.
	 */
	private SourcePosition positionOf(Diagnostic diagnostic) {
		List<?> data = diagnostic.getData();
		if (data == null) {
			return null;
		}
		for (Object entry : data) {
			if (entry instanceof EObject node) {
				SourcePosition position = positionOf(node);
				if (position != null) {
					return position;
				}
			}
		}
		return null;
	}

	/**
	 * The place a node stood in the template, or {@code null} when nothing this module parsed
	 * contains it.
	 *
	 * <p>Up the containment chain: only the node a visit was entered for is recorded, while a
	 * diagnostic is usually about an inner one — the enclosing expression is its place.
	 */
	private SourcePosition positionOf(EObject node) {
		for (EObject current = node; current != null; current = current.eContainer()) {
			SourcePosition position = positions.get(current);
			if (position != null) {
				return position;
			}
		}
		return null;
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
	private static List<OclOperationProvider> withStandardLibraryAndQueries(
			OclEvaluationOptions options, Module module,
			BiFunction<Query, Object[], Object> queryInvoker, BiFunction<Template, Object[], Object> templateInvoker) {
		List<OclOperationProvider> providers = new ArrayList<>(options.additionalProviders());
		providers.add(new M2tStandardLibrary());
		// The module's own queries and templates. Without them a guard — a plain OCL expression the
		// linker's invocation rewrite never reaches — cannot call what the module defines (#146),
		// nor can an invocation inside a larger expression (#364).
		providers.add(new M2tOperationProvider(module, queryInvoker, templateInvoker));
		return List.copyOf(providers);
	}

	/**
	 * Evaluates a query and returns its value, for an invocation that arrived through OCL.
	 *
	 * <p>The counterpart in the template body, {@code caseQueryInvocation}, writes the result to
	 * the document; inside an expression the value is what is wanted. The parameters are bound
	 * in a fresh scope, and {@code self} to the first argument, exactly as there — a query body
	 * written against {@code self} behaves the same whichever way it was called.
	 *
	 * <p>The depth of nested query calls is capped like template invocation depth: a query that
	 * calls itself is a mistake an author can make, and an evaluation with a diagnostics channel
	 * should report it rather than exhaust the stack.
	 */
	private Object invokeQueryValue(Query query, Object[] arguments) {
		if (queryDepth >= limits.maxTemplateDepth()) {
			addError("Maximum query depth exceeded (" + limits.maxTemplateDepth()
					+ ") — possible recursion in query '" + query.getName() + "'");
			return null;
		}
		if (pastDeadline()) {
			return null;
		}
		queryDepth++;
		env.pushFrame();
		try {
			List<Variable> parameters = query.getParameter();
			for (int i = 0; i < parameters.size() && i < arguments.length; i++) {
				env.define(parameters.get(i).getName(), arguments[i]);
			}
			if (arguments.length > 0) {
				env.define("self", arguments[0]);
			}
			Object value = evaluateOcl(query.getExpression());
			checkQueryResult(query, value);
			return value;
		} finally {
			env.popFrame();
			queryDepth--;
		}
	}

	/** The body of a macro invocation, with the variables of the scope it was written in (#371). */
	private record BodyArgument(Block block, Map<String, Object> scope) {
	}

	/** Writes the body of a macro invocation, in the scope of the call. */
	private void expandBody(BodyArgument body) {
		env.pushFrame();
		try {
			body.scope().forEach(env::define);
			executeBody(body.block());
		} finally {
			env.popFrame();
		}
	}

	/**
	 * §8.1.15: a query has a return type. Where the body's type is known the parser checks it;
	 * where it is not, a value that is known not to be of that type is reported here (#370).
	 */
	private void checkQueryResult(Query query, Object value) {
		EClassifier returnType = query.getReturnType();
		EClassifier valueType = OclTypeRules.normalize(runtimeType(value));
		if (returnType != null && valueType != null && !OclTypeRules.conformsTo(valueType, returnType)) {
			addDiagnostic(Diagnostic.WARNING, "Query '" + query.getName() + "' returned a "
					+ OclTypeRules.nameOf(valueType) + ", which is no " + OclTypeRules.nameOf(returnType)
					+ " as it declares (MOFM2T §8.1.15)");
		}
	}

	/** The type of a value as far as it says: a model object's class, a primitive's library type. */
	private static EClassifier runtimeType(Object value) {
		OclStandardLibrary library = OclStandardLibrary.INSTANCE;
		return switch (value) {
			case EObject object -> object.eClass();
			case String text -> library.string();
			case Boolean bool -> library.booleanType();
			case Long l -> library.integer();
			case Integer i -> library.integer();
			case BigInteger big -> library.integer();
			case BigDecimal decimal -> library.real();
			case Double d -> library.real();
			case null, default -> null;
		};
	}

	/**
	 * The argument lists a template invocation runs with (MOFM2T §8.1.10): "If the argument is a
	 * set and the parameter is singleton, then the template is invoked for each member of the
	 * set. If the argument is a set and the parameter is a set, then the template is invoked
	 * once. If the argument is a singleton and the parameter is a set, then the template is
	 * invoked once with the singleton set." Several fanned-out arguments make the cross product.
	 * {@code null} when the cross product exceeds its limit, which is reported.
	 */
	private List<List<Object>> invocationArguments(Template target, List<Object> evalArgs) {
		List<Variable> params = target.getParameter();
		List<List<?>> perParameter = new ArrayList<>();
		for (int i = 0; i < evalArgs.size(); i++) {
			Object arg = evalArgs.get(i);
			EClassifier type = i < params.size() ? params.get(i).getType() : null;
			if (arg instanceof Collection<?> collection && !(type instanceof CollectionType)) {
				perParameter.add(new ArrayList<>(collection));
			} else if (type instanceof CollectionType collectionType && arg != null && arg != OclInvalid.INSTANCE
					&& !(arg instanceof Collection<?>)) {
				perParameter.add(Collections.singletonList(singletonCollection(arg, collectionType)));
			} else {
				perParameter.add(Collections.singletonList(arg));
			}
		}
		long productSize = 1;
		for (List<?> values : perParameter) {
			productSize *= values.size();
			if (productSize > limits.maxCrossProductSize()) {
				// T-3
				addError("Cross-product size exceeds limit (" + limits.maxCrossProductSize()
						+ ") — template invocation skipped");
				return null;
			}
		}
		return crossProduct(perParameter);
	}

	/** One value as a collection of the parameter's kind, for a collection parameter (§8.1.10). */
	private static Collection<Object> singletonCollection(Object value, CollectionType type) {
		Collection<Object> collection = type.getKind() == CollectionKind.SET || type.getKind() == CollectionKind.ORDERED_SET
				? new LinkedHashSet<>() : new ArrayList<>();
		collection.add(value);
		return collection;
	}

	/**
	 * Invokes a template with one argument list, unless an argument rules it out (#364): an
	 * {@code invalid} or {@code null} argument — the template is skipped, as in Acceleo — or one
	 * that is no instance of its parameter's type (§8.1.10: "An argument type matches the
	 * parameter type when the latter is either the same type or a super type"). Each is an
	 * ERROR at the argument. Among same-named templates of the module the most specific one that
	 * fits is taken (#369), then the override that applies.
	 *
	 * @param argumentExpressions where the arguments were written, for the position of a
	 *            diagnostic; empty for a call from an OCL expression
	 * @return whether the template ran, its guard holding
	 */
	private boolean invokeChecked(Template target, List<Object> arguments, List<OclExpression> argumentExpressions) {
		for (int i = 0; i < arguments.size(); i++) {
			Object argument = arguments.get(i);
			if (argument == null || argument == OclInvalid.INSTANCE) {
				addDiagnostic(Diagnostic.ERROR, "Argument " + (i + 1) + " of template '" + target.getName() + "' is "
						+ (argument == null ? "null" : "invalid") + " — the template is not invoked (MOFM2T §8.1.10)",
						argumentPosition(argumentExpressions, i));
				return false;
			}
		}
		Template chosen = dispatch(target, arguments);
		List<Variable> params = chosen.getParameter();
		for (int i = 0; i < arguments.size() && i < params.size(); i++) {
			if (!conformsAtRuntime(arguments.get(i), params.get(i).getType())) {
				addDiagnostic(Diagnostic.ERROR, "Argument " + (i + 1) + " of template '" + chosen.getName() + "' is a "
						+ typeName(arguments.get(i)) + ", which is no " + OclTypeRules.nameOf(params.get(i).getType())
						+ " — the template is not invoked (MOFM2T §8.1.10)", argumentPosition(argumentExpressions, i));
				return false;
			}
		}
		return invokeTemplateWithOverride(resolveOverride(chosen, arguments), chosen, arguments);
	}

	private SourcePosition argumentPosition(List<OclExpression> argumentExpressions, int index) {
		return index < argumentExpressions.size() ? positionOf(argumentExpressions.get(index)) : null;
	}

	/**
	 * The template a call runs among the same-named ones of the module that declares the
	 * target (#369, as Acceleo): of those with as many parameters as there are arguments and
	 * whose parameter types the arguments are instances of, the most specific — the one whose
	 * parameter types all conform to those of the others. The target itself when nothing else fits.
	 */
	private Template dispatch(Template target, List<Object> arguments) {
		if (!(target.eContainer() instanceof Module module)) {
			return target;
		}
		Template best = null;
		for (ModuleElement element : module.getOwnedModuleElement()) {
			if (element instanceof Template candidate && target.getName().equals(candidate.getName())
					&& candidate.getParameter().size() == arguments.size() && fits(candidate, arguments)
					&& (best == null || moreSpecific(candidate, best))) {
				best = candidate;
			}
		}
		return best != null ? best : target;
	}

	private static boolean fits(Template candidate, List<Object> arguments) {
		for (int i = 0; i < arguments.size(); i++) {
			if (!conformsAtRuntime(arguments.get(i), candidate.getParameter().get(i).getType())) {
				return false;
			}
		}
		return true;
	}

	/** Whether every parameter type of {@code a} conforms to the one of {@code b} in its place. */
	static boolean moreSpecific(Template a, Template b) {
		for (int i = 0; i < a.getParameter().size(); i++) {
			if (!OclTypeRules.conformsTo(a.getParameter().get(i).getType(), b.getParameter().get(i).getType())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Evaluates a template invocation written inside an OCL expression (§8.2 {@code <invocation>}
	 * is an {@code OclExpressionCS}): the text the template generates is its value (#364).
	 */
	private Object invokeTemplateValue(Template template, Object[] arguments) {
		List<List<Object>> invocations = invocationArguments(template, Arrays.asList(arguments));
		String text;
		writers.pushStringWriter();
		try {
			if (invocations != null) {
				for (List<Object> invocation : invocations) {
					invokeChecked(template, invocation, List.of());
				}
			}
		} finally {
			text = writers.popWriter();
		}
		return text;
	}

	/**
	 * Invokes a template, pushing super context if the resolved template differs
	 * from the original (i.e., an override was applied).
	 */
	private boolean invokeTemplateWithOverride(Template resolved, Template original,
			List<Object> argValues) {
		Template superTarget = superOf(resolved, original);
		if (superTarget == null) {
			return invokeTemplate(resolved, argValues);
		}
		superStack.push(new SuperContext(superTarget, argValues, original));
		try {
			return invokeTemplate(resolved, argValues);
		} finally {
			superStack.pop();
		}
	}

	/**
	 * What {@code [super/]} in a template points to (MOFM2T §8.1.3): "Let's say that T2 overrides
	 * T1 and T3 … Invocation of T1 resulted in invocation of T2. In this case super points to T1.
	 * … T2 is directly invoked. In this case we have ambiguity … we choose the first listed
	 * template". Of the templates it overrides, the one through which the invoked template is
	 * reached — the invoked one itself, or the one in a chain between them — else the first
	 * listed; {@code null} for a template that overrides nothing (#365).
	 */
	private static Template superOf(Template template, Template invoked) {
		for (Template overridden : template.getOverrides()) {
			if (reaches(overridden, invoked, Collections.newSetFromMap(new IdentityHashMap<>()))) {
				return overridden;
			}
		}
		return template.getOverrides().isEmpty() ? null : template.getOverrides().get(0);
	}

	/** Whether {@code target} is {@code from} or a template {@code from} overrides, transitively. */
	private static boolean reaches(Template from, Template target, Set<Template> visited) {
		if (from == target) {
			return true;
		}
		if (!visited.add(from)) {
			return false;
		}
		for (Template overridden : from.getOverrides()) {
			if (reaches(overridden, target, visited)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The template an invocation actually runs: the applicable one of the family the call names.
	 *
	 * <p>A template and the templates that override it are <b>one invocation target</b>. The
	 * guard decides which of them runs, not whether anything runs, and the module the call is
	 * written in must not change the answer — otherwise the same expression means two different
	 * things in two modules (#149). So the family is collected from the root of the override
	 * chain, the most derived candidate is asked first, and the first whose parameter types fit
	 * and whose guard holds is the one. Where no candidate applies the target is returned and
	 * {@code invokeTemplate} lets its guard decline it, which produces no output — as it did.
	 *
	 * <p>That is the behaviour of the MOFM2T-conformant reference: Acceleo 3.7 collects the
	 * overriding templates and the namesakes of a call
	 * ({@code AcceleoEvaluationEnvironment#getAllCandidates}), evaluates the guards over that
	 * whole list ({@code AcceleoEvaluationVisitor#evaluateGuards}) and takes the most specific of
	 * what remains — the candidate list does not depend on the calling module either.
	 *
	 * <p>MOFM2T §8.1.3: override parameters must be type-compatible (same or narrower).
	 */
	private Template resolveOverride(Template target, List<Object> evalArgs) {
		for (Template candidate : overrideFamily(target)) {
			if (isOverrideApplicable(candidate, evalArgs)) {
				return candidate;
			}
		}
		return target;
	}

	/**
	 * The template and everything that overrides it, most derived first: up to the root of the
	 * chain, then down again over every overriding template.
	 */
	private List<Template> overrideFamily(Template target) {
		Set<Template> visited = Collections.newSetFromMap(new IdentityHashMap<>());
		Template root = target;
		while (visited.add(root) && !root.getOverrides().isEmpty()) {
			root = root.getOverrides().get(0);
		}
		List<Template> family = new ArrayList<>();
		collectFamily(root, family, Collections.newSetFromMap(new IdentityHashMap<>()));
		// Most derived first: an override is asked before what it overrides
		Collections.reverse(family);
		return family;
	}

	private void collectFamily(Template template, List<Template> family, Set<Template> visited) {
		if (!visited.add(template)) {
			return; // a cycle in the override chain stops here
		}
		family.add(template);
		for (Template override : overrideIndex.getOrDefault(template, List.of())) {
			collectFamily(override, family, visited);
		}
	}

	/**
	 * Whether an overriding template applies to the given argument values (MOFM2T §8.1.3): "The
	 * overriding template is invoked in place of the overridden template when the parameter types
	 * match and the guard condition of the overriding template evaluates to true" — every
	 * argument an instance of its parameter type, so that a single parameter deciding for the
	 * overridden template decides for it (#365).
	 */
	private boolean isOverrideApplicable(Template override, List<Object> evalArgs) {
		if (override.getParameter().size() != evalArgs.size() || !fits(override, evalArgs)) {
			return false;
		}

		// Check guard if present
		if (override.getGuard() != null) {
			env.pushFrame();
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
				return holds(override.getGuard(), "guard of template '" + override.getName() + "'");
			} finally {
				env.popFrame();
			}
		}

		return true;
	}

	/**
	 * Invokes a template with resolved argument values. Binds parameters
	 * to argument values and executes the template body.
	 */
	private boolean invokeTemplate(Template target, List<Object> argValues) {
		if (templateDepth >= limits.maxTemplateDepth()) {
			addError("Maximum template depth exceeded (" + limits.maxTemplateDepth()
					+ ") — possible infinite recursion in template '" + target.getName() + "'");
			return false;
		}
		if (pastDeadline()) {
			return false;
		}
		templateDepth++;
		Template enclosing = executing;
		executing = target;
		env.pushFrame();
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

			if (target.getGuard() != null && !holds(target.getGuard(), "guard of template '" + target.getName() + "'")) {
				return false;
			}

			// Apply post-processing if present (Acceleo extension)
			if (target.getPost() != null) {
				writers.pushStringWriter();
				executeBody(target);
				String result = writers.popWriter();
				env.define("self", result);
				writers.append(render(target.getPost()));
			} else {
				executeBody(target);
			}
			return true;
		} finally {
			env.popFrame();
			templateDepth--;
			executing = enclosing;
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
		// A Real is written as OCL writes it: 3.0, never 3 or 3E+1 (#322)
		return OclValues.toText(value);
	}

	/**
	 * Evaluates a guard or condition: {@code true} or {@code false}, or {@code null} for anything
	 * else — {@code null}, {@code invalid}, a value of another type — which is reported as an
	 * ERROR at the expression. Such a condition decides nothing, so the caller takes no branch
	 * (#366, the line of QVT-O #308/#310). A navigation that leniently yields {@code null}
	 * inside a comparison is not affected: {@code null = 'X'} is {@code false}.
	 */
	private Boolean condition(OclExpression expression, String what) {
		Object value = evaluateOcl(expression);
		if (value instanceof Boolean bool) {
			return bool;
		}
		String found = value == null ? "null" : value == OclInvalid.INSTANCE ? "invalid" : "a " + typeName(value);
		addDiagnostic(Diagnostic.ERROR, "The " + what + " is " + found
				+ ", not true or false — nothing is executed (MOFM2T §8.1.21)", positionOf(expression));
		return null;
	}

	/** The name of a value's type, for a message. */
	private static String typeName(Object value) {
		EClassifier type = runtimeType(value);
		return type != null ? OclTypeRules.nameOf(type) : value.getClass().getSimpleName();
	}

	/** Whether a guard holds: {@code false} for a guard that is not true, reported or not. */
	private boolean holds(OclExpression guard, String what) {
		return Boolean.TRUE.equals(condition(guard, what));
	}

	/**
	 * The elements a for block iterates over (§8.1.8, #366): a collection's elements, a single
	 * value as a one-element collection — as Acceleo and OCL's implicit {@code asSet} — and
	 * nothing for {@code null}. An {@code invalid} iteration set is an ERROR. An element that is
	 * no instance of the loop variable's type is skipped, with one ERROR per block.
	 */
	private List<?> iterationItems(ForBlock block) {
		Object iterSet = evaluateOcl(block.getIterSet());
		if (iterSet == null) {
			return List.of();
		}
		if (iterSet == OclInvalid.INSTANCE) {
			addDiagnostic(Diagnostic.ERROR, "The iteration set of a for block is invalid — nothing is iterated (MOFM2T §8.1.8)",
					positionOf(block.getIterSet()));
			return List.of();
		}
		List<?> items = iterSet instanceof Collection<?> collection ? new ArrayList<>(collection) : List.of(iterSet);
		Variable loopVariable = block.getLoopVariable();
		if (loopVariable == null || loopVariable.getType() == null) {
			return items;
		}
		List<Object> fitting = new ArrayList<>();
		String mismatch = null;
		for (Object item : items) {
			if (conformsAtRuntime(item, loopVariable.getType())) {
				fitting.add(item);
			} else if (mismatch == null) {
				mismatch = typeName(item);
			}
		}
		if (mismatch != null) {
			addDiagnostic(Diagnostic.ERROR, "The for block skips the elements that are no "
					+ OclTypeRules.nameOf(loopVariable.getType()) + ", among them a " + mismatch
					+ " (MOFM2T §8.1.8)", positionOf(block.getIterSet()));
		}
		return fitting;
	}

	/**
	 * Whether a value is an instance of a declared type, as far as the value tells: a value whose
	 * type is not known here — a collection, an enumerator, a Java object — is taken as fitting.
	 * {@code null} fits every type; whether a null is wanted is for the caller to say.
	 */
	private static boolean conformsAtRuntime(Object value, EClassifier type) {
		if (value == null || type == null || type == M2tPackage.Literals.BODY) {
			return true;
		}
		EClassifier valueType = OclTypeRules.normalize(runtimeType(value));
		return valueType == null || OclTypeRules.conformsTo(valueType, type);
	}

	/**
	 * Adds an output size limit error diagnostic. Called by the engine after execution
	 * when the writer stack's output limit was exceeded (T-7).
	 *
	 * @param maxOutputSize the configured limit
	 */
	/**
	 * Reports that the JVM stack ran out before the depth limit did.
	 *
	 * <p>{@code maxTemplateDepth} defaults to 1000, and a recursion that deep overflows the
	 * stack first — measured: at 500 the guard reports, at 1000 the generation dies with a
	 * {@code StackOverflowError}, which is an {@code Error} and reaches no caller as a
	 * diagnostic (#178). Lowering the default would guess at a stack size that differs per
	 * platform; catching it here does not.
	 *
	 * @param cause what the JVM threw
	 */
	public void addStackOverflowError(StackOverflowError cause) {
		addError("The stack ran out while evaluating; the template depth limit of "
				+ limits.maxTemplateDepth() + " was not reached first. "
				+ "A recursion without a base case, or a limit above what this stack can take.");
	}

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

	/**
	 * Whether the deadline of this generation has passed — polled where the other limits are
	 * polled: at every template and query invocation and every for-block iteration. The first
	 * time it has, it is reported; from then on every checkpoint stops silently, so what was
	 * generated so far travels with one diagnostic rather than thousands (#261).
	 */
	private boolean pastDeadline() {
		if (deadlineNanos == 0) {
			return false;
		}
		if (!timedOut && System.nanoTime() > deadlineNanos) {
			timedOut = true;
			addError("Execution timeout exceeded (" + limits.timeoutMs() + " ms) — generation stopped");
		}
		return timedOut;
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
		addDiagnostic(severity, message, null);
	}

	private void addDiagnostic(int severity, String message, SourcePosition position) {
		if (diagnostics.size() >= limits.maxDiagnostics()) {
			if (diagnostics.size() == limits.maxDiagnostics()) {
				diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING, SOURCE_ID, 0,
						"Maximum diagnostics limit reached (" + limits.maxDiagnostics()
								+ "), further diagnostics truncated", null));
			}
			return;
		}
		diagnostics.add(new BasicDiagnostic(severity, SOURCE_ID, 0, located(message, position),
				position == null ? null : new Object[] { position }));
	}

	/**
	 * Names the place a diagnostic comes from: the template being executed and, when a
	 * {@code [file]} block is open, the document being written.
	 *
	 * <p>The M2T metamodel carries no positions, so this is what is available — and it is what
	 * turns "some expression came up empty" into something an author can act on. Line and column
	 * need positions in the AST and in the OCL diagnostics (#110, #116).
	 */
	private String located(String message, SourcePosition position) {
		StringBuilder where = new StringBuilder();
		if (position != null) {
			where.append(position);
		}
		if (executing != null) {
			if (where.length() > 0) {
				where.append(' ');
			}
			where.append("[template ").append(executing.getName());
			String target = writers.currentTargetName();
			if (target != null) {
				where.append(" → ").append(target);
			}
			where.append(']');
		}
		return where.length() == 0 ? message : where + " " + message;
	}

	/**
	 * Applies indent-propagation (§8.4 Rule 4): after each newline in the source,
	 * inserts the given indentation string. The first line is NOT indented.
	 */
	private static String fitIndentationTo(String source, String indentation) {
		if (indentation.isEmpty() || source.isEmpty()) {
			return source;
		}
		// §8.4: "Add the whitespace preceding the invocation expression before each line of
		// the text produced by the invoked template" — the first line included. The
		// normalizer stripped that whitespace from the text in front of the invocation, so
		// it is restored here, once per line (#277).
		return indentation + source.replaceAll("(\r\n|\r|\n)", "$1" + Matcher.quoteReplacement(indentation));
	}

	/**
	 * Context for [super/] calls: the overridden template and its argument values.
	 */
	private record SuperContext(Template overridden, List<Object> args, Template invoked) {}
}
