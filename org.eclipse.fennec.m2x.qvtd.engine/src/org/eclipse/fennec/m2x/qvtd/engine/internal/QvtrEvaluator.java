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
package org.eclipse.fennec.m2x.qvtd.engine.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongSupplier;
import java.util.Set;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtbase.Domain;
import org.eclipse.fennec.m2x.model.qvtbase.Pattern;
import org.eclipse.fennec.m2x.model.qvtbase.Predicate;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;
import org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationCallExp;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionException;
import org.eclipse.fennec.m2x.qvtd.api.RelationImplementationProvider;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import java.util.function.Function;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;

/**
 * Core evaluator for QVT-R transformations (§7.10).
 *
 * <p>Implements direct interpretation of relational transformations (D33)
 * without lowering to QVT-Core. Uses the OCL engine (D36) for evaluating
 * OCL expressions in when/where clauses and property template values.
 *
 * <p>Execution algorithm for each top-level relation:
 * <ol>
 *   <li>Evaluate when-clause predicates to pre-bind variables</li>
 *   <li>Match source domain patterns to find all valid source bindings</li>
 *   <li>For each source binding, check/enforce the target domain</li>
 *   <li>Evaluate where-clause predicates</li>
 * </ol>
 *
 * <p>Target enforcement, blackbox invocation, and query evaluation are
 * delegated to {@link QvtrEnforcer}, {@link QvtrBlackboxBridge}, and
 * {@link QvtrQueryEvaluator} respectively.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtrEvaluator {

	private static final String SOURCE_ID = "org.eclipse.fennec.m2x.qvtd.engine";

	private final OclEngine oclEngine;
	private final QvtrEvalEnvironment env;
	private final RelationalTransformation transformation;
	private final QvtrExtentManager extentManager;
	private final QvtdExecutionContext context;
	private final QvtdConfiguration config;
	private final QvtrPatternMatcher patternMatcher;
	private final QvtrTraceManager traceManager;
	private final List<Diagnostic> diagnostics = new ArrayList<>();

	/**
	 * Where an expression node stood, so a runtime diagnostic can name the place and the unit —
	 * including an imported one (#116). Set by the engine, which holds the parser.
	 */
	private Function<EObject, SourcePosition> positionLookup = node -> null;

	/**
	 * The transformation's queries and blackboxes, as operations the OCL engine can resolve —
	 * built lazily because it captures {@code this} for recursive body evaluation (#118).
	 */
	private QvtrOperationProvider operationProvider;

	private final QvtrEnforcer enforcer;
	private final QvtrBlackboxBridge blackboxBridge;
	private final QvtrQueryEvaluator queryEvaluator;

	private final LongSupplier nanoTimeSource;

	private int relationCallDepth;
	private long deadlineNanos;

	public QvtrEvaluator(OclEngine oclEngine, QvtrEvalEnvironment env,
			RelationalTransformation transformation, QvtrExtentManager extentManager,
			QvtdExecutionContext context, QvtdConfiguration config,
			QvtdBlackboxRegistry blackboxRegistry,
			List<RelationImplementationProvider> implementationProviders) {
		this(oclEngine, env, transformation, extentManager, context, config,
				blackboxRegistry, implementationProviders, System::nanoTime);
	}

	/**
	 * Creates an evaluator with an explicit time source for the execution deadline
	 * (M-R4).
	 *
	 * <p>The deadline is the one part of the evaluator whose behaviour depends on how
	 * fast the machine is. Taking the clock as a parameter lets a test drive the
	 * deadline instead of racing it — a timeout test that passes because the machine
	 * was slow enough proves nothing.
	 *
	 * @param nanoTimeSource the source of monotonic nanoseconds, must not be {@code null}
	 */
	public QvtrEvaluator(OclEngine oclEngine, QvtrEvalEnvironment env,
			RelationalTransformation transformation, QvtrExtentManager extentManager,
			QvtdExecutionContext context, QvtdConfiguration config,
			QvtdBlackboxRegistry blackboxRegistry,
			List<RelationImplementationProvider> implementationProviders,
			LongSupplier nanoTimeSource) {
		this.nanoTimeSource = Objects.requireNonNull(nanoTimeSource,
				"nanoTimeSource must not be null");
		this.oclEngine = Objects.requireNonNull(oclEngine, "oclEngine must not be null");
		this.env = Objects.requireNonNull(env, "env must not be null");
		this.transformation = Objects.requireNonNull(transformation, "transformation must not be null");
		this.extentManager = Objects.requireNonNull(extentManager, "extentManager must not be null");
		this.context = Objects.requireNonNull(context, "context must not be null");
		this.config = Objects.requireNonNull(config, "config must not be null");
		this.traceManager = new QvtrTraceManager(config.maxTraceRecords());
		this.patternMatcher = new QvtrPatternMatcher(oclEngine, extentManager, config.maxBindings());

		QvtrOclCallback oclCallback = this::evaluateOcl;
		this.blackboxBridge = new QvtrBlackboxBridge(
				blackboxRegistry, config, implementationProviders,
				extentManager, context, diagnostics, oclCallback);
		this.queryEvaluator = new QvtrQueryEvaluator(
				transformation, oclCallback, blackboxBridge);
		this.enforcer = new QvtrEnforcer(
				extentManager, patternMatcher, transformation,
				diagnostics, oclCallback, blackboxBridge);
	}

	// ── Execution entry point ────────────────────────────────────────

	/**
	 * Executes the transformation: iterates over all top-level relations and
	 * either checks or enforces them based on the execution context.
	 *
	 * @return collected diagnostics (empty list = success)
	 */
	public List<Diagnostic> execute() {
		// Initialize timeout deadline (M-R4)
		long timeout = config.timeoutMs();
		this.deadlineNanos = timeout > 0
				? nanoTimeSource.getAsLong() + timeout * 1_000_000L
				: 0;

		// Resolve deferred overrides (EAnnotation → Rule.overrides reference)
		resolveOverrides();

		// Collect overridden relations so they can be skipped
		Set<String> overriddenNames = new HashSet<>();
		for (Rule rule : transformation.getRule()) {
			if (rule instanceof Relation rel && rel.getOverrides() != null) {
				overriddenNames.add(rel.getOverrides().getName());
			}
		}

		for (Rule rule : transformation.getRule()) {
			if (rule instanceof Relation relation && relation.isIsTopLevel()) {
				if (relation.isIsAbstract()) {
					continue; // §7.11.3.2: abstract relations are templates only
				}
				if (overriddenNames.contains(relation.getName())) {
					continue; // §7.6: overridden relations are skipped
				}
				try {
					executeRelation(relation);
				} catch (QvtdExecutionException e) {
					throw e;
				} catch (Exception e) {
					diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE_ID, 0,
							"Error executing relation '%s': %s"
									.formatted(relation.getName(), e.getMessage()),
							null));
				}
			}
		}
		return diagnostics;
	}

	// ── Relation execution ───────────────────────────────────────────

	/**
	 * Executes a single relation (§7.10.1 / §7.10.2).
	 */
	private void executeRelation(Relation relation) {
		checkDeadline();
		env.pushScope();
		try {
			// 1. Evaluate the when-clause's relation calls to get pre-bindings. The remaining
			// predicates are evaluated per candidate binding below, because they may read what
			// the domains bind (§7.10.2, #145).
			Map<String, Object> whenBindings = evaluateWhenPreBindings(relation);
			if (whenBindings == null) {
				return; // A relation call in the when-clause found nothing (§7.2.1)
			}

			// 2. Classify domains into source, target, and primitive
			List<RelationDomain> sourceDomains = new ArrayList<>();
			RelationDomain targetDomain = null;

			for (Domain domain : relation.getDomain()) {
				if (domain instanceof RelationDomain rd) {
					if (isPrimitiveDomain(rd)) {
						continue; // §7.11.3.10
					}
					if (!context.checkOnly() && rd.isIsEnforceable()
								&& extentManager.isTargetModel(rd.getTypedModel())) {
						targetDomain = rd;
					} else {
						sourceDomains.add(rd);
					}
				}
			}

			// In check-only mode, all domains are source domains
			if (context.checkOnly()) {
				matchAndCheckAllDomains(relation, sourceDomains, whenBindings);
				return;
			}

			// 3. Match source domains
			if (sourceDomains.isEmpty()) {
				return;
			}

			List<Map<String, Object>> sourceBindings = matchSourceDomains(sourceDomains, whenBindings);

			// 4. For each source binding, pre-compute where bindings, then enforce
			for (Map<String, Object> sourceBinding : sourceBindings) {
				checkDeadline();
				if (hasOptionalNullBinding(sourceDomains, sourceBinding)) {
					continue;
				}
				if (!whenConditionHolds(relation, sourceBinding)) {
					continue;
				}

				queryEvaluator.preComputeWhereBindings(relation, sourceBinding);

				if (targetDomain != null) {
					enforcer.enforceTargetDomain(relation, targetDomain, sourceBinding);
				}
				traceManager.record(relation, sourceBinding);
				evaluateWhereClause(relation, sourceBinding);
			}
		} finally {
			env.popScope();
		}
	}

	/**
	 * Executes a relation with pre-bound root variable values.
	 * Used for non-top relation invocation from where-clauses.
	 */
	private void executeRelationWithBindings(Relation relation,
			Map<String, Object> preBindings) {
		checkDeadline();
		int maxDepth = config.maxRelationDepth();
		if (relationCallDepth >= maxDepth) {
			throw new QvtdExecutionException(
					"Relation call depth limit exceeded (%d) in relation '%s'"
							.formatted(maxDepth, relation.getName()));
		}
		relationCallDepth++;
		env.pushScope();
		try {
			Map<String, Object> whenBindings = evaluateWhenPreBindings(relation);
			if (whenBindings == null) {
				return;
			}

			Map<String, Object> mergedBindings = new HashMap<>(preBindings);
			mergedBindings.putAll(whenBindings);

			List<RelationDomain> sourceDomains = new ArrayList<>();
			RelationDomain targetDomain = null;

			for (Domain domain : relation.getDomain()) {
				if (domain instanceof RelationDomain rd) {
					if (isPrimitiveDomain(rd)) {
						continue;
					}
					if (!context.checkOnly() && rd.isIsEnforceable()
									&& extentManager.isTargetModel(rd.getTypedModel())) {
						targetDomain = rd;
					} else {
						sourceDomains.add(rd);
					}
				}
			}

			if (context.checkOnly()) {
				matchAndCheckAllDomains(relation, sourceDomains, mergedBindings);
				return;
			}

			List<Map<String, Object>> sourceBindings = matchSourceDomains(
					sourceDomains, mergedBindings);

			for (Map<String, Object> binding : sourceBindings) {
				if (hasOptionalNullBinding(sourceDomains, binding)) {
					traceManager.record(relation, binding);
					continue;
				}
				if (!whenConditionHolds(relation, binding)) {
					continue;
				}
				queryEvaluator.preComputeWhereBindings(relation, binding);
				if (targetDomain != null) {
					enforcer.enforceTargetDomain(relation, targetDomain, binding);
				}
				traceManager.record(relation, binding);
				evaluateWhereClause(relation, binding);
			}
		} finally {
			relationCallDepth--;
			env.popScope();
		}
	}

	// ── When-clause evaluation ───────────────────────────────────────

	/**
	 * Evaluates the relation-call predicates of a when-clause and returns what they bind
	 * (§7.2.1), or {@code null} if one of them is not satisfied.
	 *
	 * <p>These run before the domains are matched, because that is what they are for: a
	 * {@code when \{ PackageToSchema(p, s); \}} looks the earlier relation up in the trace and
	 * binds {@code p} and {@code s}, which then restrict what the domain patterns match
	 * (§7.2.3). A relation call that finds nothing rejects the relation as a whole — there is
	 * no binding under which it could hold.
	 *
	 * <p>The remaining predicates are <em>not</em> evaluated here. They may read variables the
	 * domains bind, which do not exist yet at this point; {@link #whenConditionHolds} evaluates
	 * them per candidate binding.
	 */
	private Map<String, Object> evaluateWhenPreBindings(Relation relation) {
		Map<String, Object> bindings = new HashMap<>();
		Pattern when = relation.getWhen();
		if (when == null) {
			return bindings;
		}

		for (Predicate predicate : when.getPredicate()) {
			OclExpression expr = predicate.getConditionExpression();
			if (expr instanceof RelationCallExp relCall
					&& !evaluateWhenRelationCall(relCall, bindings)) {
				return null;
			}
		}
		return bindings;
	}

	/**
	 * Returns whether the when-condition holds for one candidate binding (§7.10.1, §7.10.2).
	 *
	 * <p>The spec evaluates the when-condition over a <em>joint</em> binding: "For each valid
	 * binding of variables of the when clause and variables of domains other than the target
	 * domain k that satisfy the when condition and source domain patterns and conditions", and
	 * it states outright that "the intersection of a domain variable set and when variable set
	 * need not be null". So a predicate such as {@code when \{ p.name <> null; \}} or
	 * {@code when \{ pn.size() > 1; \}} reads variables the domains bind, and can only be
	 * evaluated once a candidate binding exists.
	 *
	 * <p>Evaluating it before the match — which is what happened until #145 — left those
	 * predicates with an empty binding and reported {@code Unresolved variable}, failing
	 * transformations the spec's own {@code UmlToRdb} example is written in the style of.
	 *
	 * <p>Relation calls are skipped here: {@link #evaluateWhenPreBindings} has already answered
	 * them, and re-running a trace lookup per binding would answer the same thing again.
	 */
	private boolean whenConditionHolds(Relation relation, Map<String, Object> binding) {
		Pattern when = relation.getWhen();
		if (when == null) {
			return true;
		}
		for (Predicate predicate : when.getPredicate()) {
			OclExpression expr = predicate.getConditionExpression();
			if (expr == null || expr instanceof RelationCallExp) {
				continue;
			}
			if (!Boolean.TRUE.equals(evaluateOcl(expr, binding))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Evaluates a RelationCallExp in a when-clause by looking up the trace.
	 */
	private boolean evaluateWhenRelationCall(RelationCallExp relCall,
			Map<String, Object> bindings) {
		Relation calledRelation = relCall.getReferredRelation();
		if (calledRelation == null) {
			return false;
		}

		List<Object> argValues = new ArrayList<>();
		for (OclExpression argExpr : relCall.getArgument()) {
			if (argExpr instanceof VariableExp varExp) {
				Variable ref = varExp.getReferredVariable();
				if (ref != null) {
					argValues.add(bindings.get(ref.getName()));
				} else {
					argValues.add(null);
				}
			} else {
				argValues.add(evaluateOcl(argExpr, bindings));
			}
		}

		List<QvtrTraceManager.TraceRecord> traces = traceManager.lookup(
				calledRelation, argValues);
		if (traces.isEmpty()) {
			return false;
		}

		QvtrTraceManager.TraceRecord match = traces.get(0);
		List<String> rootVarNames = QvtrTraceManager.collectRootVarNames(calledRelation);
		for (int i = 0; i < Math.min(rootVarNames.size(), relCall.getArgument().size()); i++) {
			OclExpression argExpr = relCall.getArgument().get(i);
			if (argExpr instanceof VariableExp varExp) {
				Variable ref = varExp.getReferredVariable();
				if (ref != null) {
					String varName = ref.getName();
					Object traceValue = match.bindings().get(rootVarNames.get(i));
					if (traceValue != null && !bindings.containsKey(varName)) {
						bindings.put(varName, traceValue);
					}
				}
			}
		}
		return true;
	}

	// ── Where-clause evaluation ──────────────────────────────────────

	/**
	 * Evaluates the where-clause of a relation (§7.2.1).
	 * RelationCallExp in where-clauses invokes non-top relations (§7.2.2).
	 */
	private void evaluateWhereClause(Relation relation, Map<String, Object> bindings) {
		Pattern where = relation.getWhere();
		if (where == null) {
			return;
		}

		for (Predicate predicate : where.getPredicate()) {
			OclExpression expr = predicate.getConditionExpression();
			if (expr == null) {
				continue;
			}

			if (expr instanceof RelationCallExp relCall) {
				invokeNonTopRelation(relCall, bindings);
				continue;
			}

			Object result = evaluateOcl(expr, bindings);
			if (!Boolean.TRUE.equals(result)) {
				diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING, SOURCE_ID, 0,
						"Relation '%s': where-clause predicate not satisfied"
								.formatted(relation.getName()),
						null));
			}
		}
	}

	/**
	 * Invokes a non-top relation from a where-clause (§7.2.2).
	 */
	private void invokeNonTopRelation(RelationCallExp relCall,
			Map<String, Object> callerBindings) {
		Relation calledRelation = relCall.getReferredRelation();
		if (calledRelation == null) {
			return;
		}

		List<Object> argValues = new ArrayList<>();
		for (OclExpression argExpr : relCall.getArgument()) {
			argValues.add(evaluateOcl(argExpr, callerBindings));
		}

		Map<String, Object> preBindings = new HashMap<>();
		List<String> rootVarNames = QvtrTraceManager.collectRootVarNames(calledRelation);
		for (int i = 0; i < Math.min(rootVarNames.size(), argValues.size()); i++) {
			Object value = argValues.get(i);
			if (value != null) {
				preBindings.put(rootVarNames.get(i), value);
			}
		}

		executeRelationWithBindings(calledRelation, preBindings);
	}

	// ── Source domain matching ────────────────────────────────────────

	/**
	 * Matches source domains sequentially, threading bindings from one domain
	 * to the next (shared variables across domains).
	 */
	private List<Map<String, Object>> matchSourceDomains(List<RelationDomain> sourceDomains,
			Map<String, Object> initialBindings) {
		List<Map<String, Object>> currentBindings = List.of(initialBindings);
		int maxBindings = config.maxBindings();

		for (RelationDomain domain : sourceDomains) {
			List<Map<String, Object>> nextBindings = new ArrayList<>();
			for (Map<String, Object> binding : currentBindings) {
				nextBindings.addAll(patternMatcher.matchDomain(domain, binding));
				if (nextBindings.size() > maxBindings) {
					throw new QvtdExecutionException(
							"Source domain binding limit exceeded (%d) — possible cross-product explosion"
									.formatted(maxBindings));
				}
			}
			currentBindings = nextBindings;
			if (currentBindings.isEmpty()) {
				break;
			}
		}
		return currentBindings;
	}

	/**
	 * In check-only mode, checks that all domain patterns are consistent.
	 */
	private void matchAndCheckAllDomains(Relation relation, List<RelationDomain> domains,
			Map<String, Object> whenBindings) {
		if (whenBindings == null) {
			return;
		}

		List<Map<String, Object>> bindings = matchSourceDomains(domains, whenBindings);

		if (bindings.isEmpty() && !domains.isEmpty()) {
			diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING, SOURCE_ID, 0,
					"Relation '%s': no matching bindings found in check-only mode"
							.formatted(relation.getName()),
					null));
		}

		for (Map<String, Object> binding : bindings) {
			// The when-condition filters here too: checking asks for a target binding only
			// "for each valid binding ... that satisfies the when condition" (§7.10.1).
			if (!whenConditionHolds(relation, binding)) {
				continue;
			}
			evaluateWhereClause(relation, binding);
		}
	}

	// ── OCL evaluation ───────────────────────────────────────────────

	/**
	 * Evaluates an OCL expression with the current variable bindings.
	 */
	private Object evaluateOcl(OclExpression expression, Map<String, Object> bindings) {
		if (expression instanceof VariableExp varExp) {
			Variable ref = varExp.getReferredVariable();
			if (ref != null && bindings.containsKey(ref.getName())) {
				return bindings.get(ref.getName());
			}
		}
		OclContext ctx = new OclContext(null, null, bindings);
		try {
			// With diagnostics: what an expression reports is what tells an author why a relation
			// did not match, and the node it came from is what places it (#116). Before this they
			// were dropped, exactly as M2T dropped them until #114.
			// LENIENT for the same reason QVT-O uses it: a relation's queries and blackboxes have
			// no self, and a call on a null source has to proceed to the providers rather than
			// fail (#112). The providers are the transformation's own queries and blackboxes —
			// without them OCL reported "Unknown operation" for a nested query call and no error
			// could be trusted (#118).
			OclResult result = oclEngine.evaluateWithDiagnostics(expression, ctx,
					OclEvaluationOptions.lenient()
							.withCustomOperationsEnabled(true)
							.withAdditionalProviders(List.of(operationProvider())));
			result.diagnostics().forEach(this::addOclDiagnostic);
			return result.value();
		} catch (Exception e) {
			diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING, SOURCE_ID, 0,
					"OCL evaluation error: " + e.getMessage(), null));
			return null;
		}
	}

	/** The provider over this transformation's queries and blackboxes — see the field. */
	private QvtrOperationProvider operationProvider() {
		if (operationProvider == null) {
			operationProvider = new QvtrOperationProvider(transformation,
					blackboxBridge, this::evaluateOcl);
		}
		return operationProvider;
	}

	/**
	 * Sets how a node is turned into the place it stood, so a runtime diagnostic can name it.
	 *
	 * @param positionLookup the lookup, {@code null} restores "no position known"
	 */
	public void setPositionLookup(Function<EObject, SourcePosition> positionLookup) {
		this.positionLookup = positionLookup == null ? node -> null : positionLookup;
	}

	/**
	 * Takes over a diagnostic an OCL expression reported, keeping its severity and naming the place
	 * the expression stands when the node is one this engine parsed.
	 */
	private void addOclDiagnostic(Diagnostic diagnostic) {
		SourcePosition position = null;
		if (diagnostic.getData() != null) {
			for (Object entry : diagnostic.getData()) {
				if (entry instanceof EObject node) {
					position = positionLookup.apply(node);
					if (position != null) {
						break;
					}
				}
			}
		}
		diagnostics.add(new BasicDiagnostic(diagnostic.getSeverity(), SOURCE_ID, 0,
				position == null ? diagnostic.getMessage()
						: position + " " + diagnostic.getMessage(),
				position == null ? null : new Object[] { position }));
	}

	// ── Utilities ────────────────────────────────────────────────────

	/**
	 * Checks if the execution deadline has been exceeded (M-R4).
	 */
	private void checkDeadline() {
		if (deadlineNanos > 0 && nanoTimeSource.getAsLong() > deadlineNanos) {
			throw new QvtdExecutionException(
					"Execution timeout exceeded (%d ms)".formatted(config.timeoutMs()));
		}
	}

	/**
	 * Checks if a domain is a primitive domain (§7.11.3.10).
	 */
	private boolean isPrimitiveDomain(RelationDomain domain) {
		return domain.getTypedModel() == null && domain.getPattern().isEmpty();
	}

	/**
	 * Checks whether any source domain has an optional [?] root variable that
	 * is bound to null.
	 */
	private boolean hasOptionalNullBinding(List<RelationDomain> sourceDomains,
			Map<String, Object> bindings) {
		for (RelationDomain domain : sourceDomains) {
			for (DomainPattern pattern : domain.getPattern()) {
				if (pattern.getEAnnotation("qvtr.optional") != null) {
					TemplateExp root = pattern.getTemplateExpression();
					if (root instanceof ObjectTemplateExp ote) {
						Variable rootVar = ote.getBindsTo();
						if (rootVar != null && bindings.containsKey(rootVar.getName())
								&& bindings.get(rootVar.getName()) == null) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Resolves deferred override references from EAnnotations to actual
	 * Rule.overrides references (§7.6).
	 */
	private void resolveOverrides() {
		Map<String, Relation> relationsByName = new HashMap<>();
		for (Rule rule : transformation.getRule()) {
			if (rule instanceof Relation rel) {
				relationsByName.put(rel.getName(), rel);
			}
		}

		for (Rule rule : transformation.getRule()) {
			if (rule instanceof Relation rel) {
				EAnnotation ann = rel.getEAnnotation("qvtr.overrides");
				if (ann != null) {
					String overridesName = ann.getDetails().get("name");
					if (overridesName != null) {
						Relation base = relationsByName.get(overridesName);
						if (base != null) {
							rel.setOverrides(base);
						}
					}
				}
			}
		}
	}
}
