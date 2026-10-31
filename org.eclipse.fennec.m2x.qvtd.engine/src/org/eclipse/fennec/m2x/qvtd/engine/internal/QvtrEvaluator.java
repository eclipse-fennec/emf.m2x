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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtbase.Domain;
import org.eclipse.fennec.m2x.model.qvtbase.Pattern;
import org.eclipse.fennec.m2x.model.qvtbase.Predicate;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationCallExp;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionException;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;

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
	/** Relations already warned about for §7.8, so each is warned about once per run (#287). */
	private final Set<String> section78Warned = new HashSet<>();
	/** Whether the trace limit was already reported, so it is reported once (#284). */
	private boolean traceLimitReported;

	/**
	 * Where an expression node stood, so a runtime diagnostic can name the place and the unit —
	 * including an imported one (#116). Set by the engine, which holds the parser.
	 */
	private Function<EObject, SourcePosition> positionLookup;

	/**
	 * The transformation's queries and blackboxes, as operations the OCL engine can resolve —
	 * built lazily because it captures {@code this} for recursive body evaluation (#118).
	 */
	private QvtrOperationProvider operationProvider;

	private final QvtrEnforcer enforcer;
	private final QvtrBlackboxBridge blackboxBridge;
	private final QvtrQueryEvaluator queryEvaluator;
	private final QvtrQueryEvaluator.QueryDepth queryDepth;

	private final LongSupplier nanoTimeSource;

	private int relationCallDepth;
	/**
	 * Whether relations are checked rather than enforced: a check-only run, or the post-check
	 * of an enforce run (#348).
	 */
	private boolean checking;
	/** Whether a when clause is being evaluated: a relation call in it looks up the trace. */
	private boolean inWhen;
	/** Whether the run changed the target model (§7.7, #353). */
	private boolean modelChanged;
	/** In-place: the objects of the source snapshot, each with the original it copies (#353). */
	private Map<EObject, EObject> originals;
	/** The relations overriding each relation (§7.11.1.4). */
	private final Map<Relation, List<Relation>> overriders = new IdentityHashMap<>();
	private long deadlineNanos;

	/**
	 * Creates an evaluator for one run.
	 *
	 * @param oclEngine the OCL engine expressions are evaluated with
	 * @param env the variable environment
	 * @param run what runs, on what, under which configuration
	 */
	public QvtrEvaluator(OclEngine oclEngine, QvtrEvalEnvironment env, QvtrRun run) {
		this(oclEngine, env, run, System::nanoTime);
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
	public QvtrEvaluator(OclEngine oclEngine, QvtrEvalEnvironment env, QvtrRun run,
			LongSupplier nanoTimeSource) {
		this.nanoTimeSource = Objects.requireNonNull(nanoTimeSource,
				"nanoTimeSource must not be null");
		this.oclEngine = Objects.requireNonNull(oclEngine, "oclEngine must not be null");
		this.env = Objects.requireNonNull(env, "env must not be null");
		Objects.requireNonNull(run, "run must not be null");
		this.transformation = run.transformation();
		this.context = run.context();
		this.config = run.config();
		this.positionLookup = run.positionLookup();
		// Derived, not handed in: an extent manager belongs to exactly this transformation and
		// this context, and the registry is the one the configuration was built with
		this.extentManager = new QvtrExtentManager(transformation, context);
		this.traceManager = new QvtrTraceManager(config.maxTraceRecords());
		this.patternMatcher = new QvtrPatternMatcher(oclEngine, extentManager, config.maxBindings());

		QvtrOclCallback oclCallback = this::evaluateOcl;
		QvtrDiagnosticSink sink = QvtrDiagnosticSink.into(diagnostics);
		this.blackboxBridge = new QvtrBlackboxBridge(config, run.implementationProviders(),
				extentManager, context, sink, oclCallback);
		// One counter for every way into a query body of this run (#181)
		this.queryDepth = new QvtrQueryEvaluator.QueryDepth(config.maxRelationDepth());
		this.queryEvaluator = new QvtrQueryEvaluator(
				transformation, oclCallback, blackboxBridge, queryDepth);
		this.enforcer = new QvtrEnforcer(
				extentManager, patternMatcher, transformation,
				sink, oclCallback, blackboxBridge);
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

		// Which relations override which: the parser set Rule.overrides (#356)
		for (Rule rule : transformation.getRule()) {
			if (rule instanceof Relation rel && rel.getOverrides() instanceof Relation overridden) {
				overriders.computeIfAbsent(overridden, r -> new ArrayList<>()).add(rel);
			}
		}

		List<Relation> topRelations = new ArrayList<>();
		for (Rule rule : transformation.getRule()) {
			if (rule instanceof Relation relation && relation.isIsTopLevel()) {
				if (relation.isIsAbstract()) {
					continue; // §7.11.1.4: "An abstract rule is never matched directly"
				}
				// An overridden relation still runs: it is replaced only for the bindings an
				// overriding relation applies to (§7.11.1.4, #356)
				topRelations.add(relation);
			}
		}

		checking = context.checkOnly();
		Runnable stopWatching = checking ? () -> { } : watchTargetModel();
		try {
			if (!checking) {
				snapshotInPlaceSources();
			}
			runAll(topRelations);
			if (!checking && config.deleteUnmatched()) {
				deleteUnmatched();
			}
		} finally {
			stopWatching.run();
			// The run is complete: a post-check checks the models as they are now
			extentManager.dropSnapshots();
			originals = null;
		}
		// An enforce run checks only the target model's checked domains, unless asked to check
		// every direction once it is done (#348)
		if (!checking && context.postCheck()) {
			checking = true;
			traceManager.clear();
			runAll(topRelations);
		}
		return diagnostics;
	}

	/**
	 * §7.2.2: all top relations have to hold, in no order of declaration — each runs after what
	 * its when clause depends on, a cycle until it adds no trace (#349).
	 */
	private void runAll(List<Relation> topRelations) {
		for (List<Relation> component : QvtrRelationOrder.components(topRelations)) {
			if (!QvtrRelationOrder.isCyclic(component)) {
				runTopRelation(component.get(0));
				continue;
			}
			int traced;
			do {
				checkDeadline();
				traced = traceManager.size();
				component.forEach(this::runTopRelation);
			} while (traceManager.size() > traced);
		}
	}

	/**
	 * Whether the run created, modified or deleted anything in the target model (§7.7).
	 *
	 * @return {@code true} if the target model changed
	 */
	boolean modelChanged() {
		return modelChanged;
	}

	/**
	 * Watches the target model for changes: an adapter on its roots notes every change in an
	 * existing tree, and a comparison of the roots before and after notes roots added or removed
	 * (§7.7 "a Boolean status to indicate whether any changes were made to target models", #353).
	 *
	 * @return what stops watching and settles the status
	 */
	private Runnable watchTargetModel() {
		TypedModel target = targetModel();
		if (target == null) {
			return () -> { };
		}
		QvtdModelExtent extent = extentManager.getExtent(target);
		List<EObject> rootsBefore = List.copyOf(extent.getContents());
		EContentAdapter watcher = new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				if (!notification.isTouch() && notification.getEventType() != Notification.REMOVING_ADAPTER) {
					modelChanged = true;
				}
			}
		};
		rootsBefore.forEach(root -> root.eAdapters().add(watcher));
		return () -> {
			rootsBefore.forEach(root -> root.eAdapters().remove(watcher));
			if (!rootsBefore.equals(extent.getContents())) {
				modelChanged = true;
			}
		};
	}

	/**
	 * In-place (§7.7): "Execution proceeds as if the source and target models are distinct with
	 * an atomic update of non-distinct models occurring on completion of the transformation.
	 * This implies that an implementation that operates in-place must take copies of the old
	 * state". Where a relation has two domains on the target model, its source domains match a
	 * copy of the state before the run, and enforcing writes the model itself — a run no longer
	 * sees its own output as source (#353).
	 */
	private void snapshotInPlaceSources() {
		TypedModel target = targetModel();
		if (target == null || !inPlace(target)) {
			return;
		}
		EcoreUtil.Copier copier = new EcoreUtil.Copier();
		List<EObject> copies = new ArrayList<>(copier.copyAll(extentManager.getExtent(target).getContents()));
		copier.copyReferences();
		originals = new IdentityHashMap<>();
		copier.forEach((original, copy) -> originals.put(copy, original));
		extentManager.snapshotForSources(target, QvtdModelExtent.of(copies));
	}

	private boolean inPlace(TypedModel target) {
		for (Rule rule : transformation.getRule()) {
			if (rule instanceof Relation relation
					&& modelDomains(relation).stream().filter(d -> d.getTypedModel() == target).count() > 1) {
				return true;
			}
		}
		return false;
	}

	private TypedModel targetModel() {
		for (TypedModel model : transformation.getModelParameter()) {
			if (extentManager.isTargetModel(model)) {
				return model;
			}
		}
		return null;
	}

	/** In-place: the binding with every object of the source snapshot replaced by its original. */
	private Map<String, Object> toOriginals(Map<String, Object> binding) {
		Map<String, Object> translated = new HashMap<>(binding);
		translated.replaceAll((name, value) -> value instanceof EObject eo && originals.containsKey(eo)
				? originals.get(eo) : value);
		return translated;
	}

	private void runTopRelation(Relation relation) {
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

	// ── Relation execution ───────────────────────────────────────────

	/**
	 * Executes a single relation (§7.10.1 / §7.10.2).
	 */
	private void executeRelation(Relation relation) {
		checkDeadline();
		warnBeyondSection78(relation);
		env.pushScope();
		try {
			runRelation(relation, Map.of());
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
		warnBeyondSection78(relation);
		int maxDepth = config.maxRelationDepth();
		if (relationCallDepth >= maxDepth) {
			throw new QvtdExecutionException(
					"Relation call depth limit exceeded (%d) in relation '%s'"
							.formatted(maxDepth, relation.getName()));
		}
		relationCallDepth++;
		env.pushScope();
		try {
			runRelation(relation, preBindings);
		} finally {
			relationCallDepth--;
			env.popScope();
		}
	}

	/**
	 * Runs a relation for every valid binding: "For each valid binding of variables of the when
	 * clause and variables of domains other than the target domain k, that satisfy the when
	 * condition and source domain patterns and conditions" (§7.10.2) — a top relation from no
	 * binding, a relation a where clause invokes from the arguments it got.
	 */
	private void runRelation(Relation relation, Map<String, Object> preBindings) {
		// 1. The when clause's relation calls: one binding per trace that matches (§7.2.1,
		// §7.3) — the remaining predicates are evaluated per candidate binding below, because
		// they may read what the domains bind (§7.10.2, #145)
		List<Map<String, Object>> whenBindings = evaluateWhenPreBindings(relation,
				initializedBindings(relation, preBindings));
		if (whenBindings.isEmpty()) {
			return; // A relation call in the when clause found nothing (§7.2.1)
		}

		// 2. Classify domains into source, target, and primitive
		List<RelationDomain> modelDomains = modelDomains(relation);

		// §7.9: checking asks whether the relation holds in every direction (#348)
		if (checking) {
			for (RelationDomain direction : modelDomains) {
				checkDirection(relation, without(modelDomains, direction), direction, whenBindings);
			}
			return;
		}

		RelationDomain targetDomain = null;
		RelationDomain checkedDomain = null;
		for (RelationDomain rd : modelDomains) {
			if (rd.isIsEnforceable() && extentManager.isTargetModel(rd.getTypedModel())) {
				targetDomain = rd;
			}
		}
		if (targetDomain == null) {
			// §7.2.3: "When a relation executes in the direction of a checkonly domain, the domain
			// is simply checked to see whether there exists a valid match" — and every binding it
			// does not hold for is reported (#348). With an enforced domain in the target model,
			// one that is only checked stays a source domain, as for an in-place transformation.
			for (RelationDomain rd : modelDomains) {
				if (extentManager.isTargetModel(rd.getTypedModel())) {
					checkedDomain = rd;
				}
			}
		}
		if (checkedDomain != null) {
			checkDirection(relation, without(modelDomains, checkedDomain), checkedDomain, whenBindings);
			return;
		}
		List<RelationDomain> sourceDomains = without(modelDomains, targetDomain);

		// Called from a where clause: an overriding relation that applies to the roots it was
		// called with runs in its place, whatever this relation's own patterns say; an abstract
		// relation never runs itself (§7.11.1.4, #356)
		if (relationCallDepth > 0 && (relation.isIsAbstract() || overriders.containsKey(relation))) {
			Map<String, Object> roots = initializedBindings(relation, preBindings);
			Relation overriding = applicableOverride(relation, sourceDomains, roots);
			if (overriding != null) {
				executeRelationWithBindings(overriding, overrideBindings(relation, overriding, roots));
				return;
			}
			if (relation.isIsAbstract()) {
				overridden(relation, sourceDomains, roots);
				return;
			}
		}

		// 3. Match source domains, once per when binding
		if (sourceDomains.isEmpty()) {
			return;
		}
		Set<Map<String, Object>> sourceBindings = new LinkedHashSet<>();
		for (Map<String, Object> whenBinding : whenBindings) {
			sourceBindings.addAll(matchSourceDomains(sourceDomains, whenBinding));
		}

		// 4. For each source binding, pre-compute where bindings, then enforce
		for (Map<String, Object> binding : List.copyOf(sourceBindings)) {
			checkDeadline();
			Map<String, Object> sourceBinding = new HashMap<>(binding);
			if (hasOptionalNullBinding(sourceDomains, sourceBinding)) {
				// [?] with nothing to match: the relation holds for the binding, and the when
				// clauses that depend on it find it — for top and non-top relations alike (#349)
				recordTrace(relation, sourceBinding);
				continue;
			}
			if (!whenConditionHolds(relation, sourceBinding)) {
				continue;
			}
			if (overridden(relation, sourceDomains, sourceBinding)) {
				continue;
			}
			// A where predicate that reads only what the sources bind and is false: nothing could
			// make the relation hold, so nothing is created (decided in #351)
			if (!sourceWhereHolds(relation, sourceBinding)) {
				continue;
			}

			queryEvaluator.preComputeWhereBindings(relation, sourceBinding);

			// A binding the target could not be enforced for does not hold, so no when clause
			// may count on it (#349)
			if (targetDomain != null && !enforceOnTarget(relation, targetDomain, sourceBinding)) {
				continue;
			}
			recordTrace(relation, sourceBinding);
			evaluateWhereClause(relation, sourceBinding);
		}
	}

	/**
	 * Whether the where predicates that read only variables the binding already holds — the
	 * when clause's and the source domains' — are true. A false one is an ERROR, and the binding
	 * is not enforced (decided in #351); relation calls and predicates on target variables are
	 * evaluated after enforcing.
	 */
	private boolean sourceWhereHolds(Relation relation, Map<String, Object> binding) {
		Pattern where = relation.getWhere();
		if (where == null) {
			return true;
		}
		for (Predicate predicate : where.getPredicate()) {
			OclExpression expr = predicate.getConditionExpression();
			if (expr == null || QvtrFreeVariables.callsRelation(expr)
					|| !binding.keySet().containsAll(QvtrFreeVariables.of(expr))) {
				continue;
			}
			Object result = evaluateOcl(expr, binding);
			if (!Boolean.TRUE.equals(result)) {
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE_ID, 0,
						"Relation '%s' cannot hold: a where predicate on the source bindings is %s, nothing is created (§7.2.1)"
								.formatted(relation.getName(), result),
						null));
				return false;
			}
		}
		return true;
	}

	/**
	 * The bindings a relation starts from: what it is called with, and each variable with an
	 * initializer bound to its value — a fixed value that constrains the matching (decided in
	 * #357), evaluated in declaration order so that one initializer may read an earlier one.
	 */
	private Map<String, Object> initializedBindings(Relation relation, Map<String, Object> preBindings) {
		Map<String, Object> bindings = new HashMap<>(preBindings);
		for (Variable variable : relation.getVariable()) {
			if (variable.getOwnedInit() != null && !bindings.containsKey(variable.getName())) {
				bindings.put(variable.getName(), evaluateOcl(variable.getOwnedInit(), bindings));
			}
		}
		return bindings;
	}

	/**
	 * Enforces the target domain; in-place on the originals of what the source snapshot bound,
	 * with what enforcing binds added to the binding (#353).
	 */
	private boolean enforceOnTarget(Relation relation, RelationDomain targetDomain, Map<String, Object> binding) {
		if (originals == null) {
			return enforce(relation, targetDomain, binding);
		}
		Map<String, Object> onTarget = toOriginals(binding);
		boolean holds = enforce(relation, targetDomain, onTarget);
		onTarget.forEach(binding::putIfAbsent);
		return holds;
	}

	/**
	 * The value of a relation call inside an expression ({@code not R(p, s)}, decided in #358):
	 * in a when clause whether the trace holds the call, in a check whether the relation holds,
	 * and in an enforced where clause the call is made and holds.
	 */
	private Boolean relationCallValue(RelationCallExp call, Map<String, Object> bindings) {
		if (checking) {
			return relationHolds(call, bindings);
		}
		Relation called = call.getReferredRelation();
		if (called == null) {
			return Boolean.FALSE;
		}
		if (inWhen) {
			List<Object> arguments = new ArrayList<>();
			for (OclExpression argument : call.getArgument()) {
				arguments.add(evaluateOcl(argument, bindings));
			}
			return traceManager.hasTrace(called, arguments);
		}
		invokeNonTopRelation(call, bindings);
		return Boolean.TRUE;
	}

	/** Enforces the target domain for a binding; whether it holds afterwards, without error. */
	private boolean enforce(Relation relation, RelationDomain targetDomain, Map<String, Object> binding) {
		int before = diagnostics.size();
		enforcer.enforceTargetDomain(relation, targetDomain, binding);
		return diagnostics.subList(before, diagnostics.size()).stream()
				.noneMatch(d -> d.getSeverity() >= Diagnostic.ERROR);
	}

	// ── When-clause evaluation ───────────────────────────────────────

	/**
	 * Evaluates the relation-call predicates of a when-clause and returns the bindings they
	 * allow (§7.2.1) — none if one of them is not satisfied.
	 *
	 * <p>These run before the domains are matched, because that is what they are for: a
	 * {@code when \{ PackageToSchema(p, s); \}} looks the earlier relation up in the trace and
	 * binds {@code p} and {@code s}, which then restrict what the domain patterns match
	 * (§7.2.3). Every trace that matches gives a binding of its own — "the pattern will only
	 * match those classes whose namespace property has a reference to the same package that is
	 * bound to p" (§7.3) holds for each package, not for the first one found (#349).
	 *
	 * <p>The remaining predicates are <em>not</em> evaluated here. They may read variables the
	 * domains bind, which do not exist yet at this point; {@link #whenConditionHolds} evaluates
	 * them per candidate binding.
	 *
	 * @param preBindings what the relation is called with; empty for a top relation
	 */
	private List<Map<String, Object>> evaluateWhenPreBindings(Relation relation,
			Map<String, Object> preBindings) {
		List<Map<String, Object>> bindings = List.of(new HashMap<>(preBindings));
		Pattern when = relation.getWhen();
		if (when == null) {
			return bindings;
		}

		for (Predicate predicate : when.getPredicate()) {
			if (predicate.getConditionExpression() instanceof RelationCallExp relCall) {
				Set<Map<String, Object>> next = new LinkedHashSet<>();
				for (Map<String, Object> binding : bindings) {
					next.addAll(evaluateWhenRelationCall(relCall, binding));
				}
				if (next.isEmpty()) {
					return List.of();
				}
				bindings = List.copyOf(next);
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
		boolean outer = inWhen;
		inWhen = true;
		try {
			return whenPredicatesHold(when, binding);
		} finally {
			inWhen = outer;
		}
	}

	private boolean whenPredicatesHold(Pattern when, Map<String, Object> binding) {
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
	 * Evaluates a RelationCallExp in a when-clause by looking up the trace: the binding extended
	 * by each matching trace entry. An argument the binding already holds selects the entries;
	 * one it does not hold yet is bound from each of them (#349).
	 */
	private List<Map<String, Object>> evaluateWhenRelationCall(RelationCallExp relCall,
			Map<String, Object> binding) {
		Relation calledRelation = relCall.getReferredRelation();
		if (calledRelation == null) {
			return List.of();
		}

		List<Object> argValues = new ArrayList<>();
		for (OclExpression argExpr : relCall.getArgument()) {
			if (argExpr instanceof VariableExp varExp) {
				Variable ref = varExp.getReferredVariable();
				argValues.add(ref != null ? binding.get(ref.getName()) : null);
			} else {
				argValues.add(evaluateOcl(argExpr, binding));
			}
		}

		List<String> rootVarNames = QvtrTraceManager.collectRootVarNames(calledRelation);
		List<Map<String, Object>> extended = new ArrayList<>();
		for (QvtrTraceManager.TraceRecord match : traceManager.lookup(calledRelation, argValues)) {
			Map<String, Object> next = new HashMap<>(binding);
			for (int i = 0; i < Math.min(rootVarNames.size(), relCall.getArgument().size()); i++) {
				if (relCall.getArgument().get(i) instanceof VariableExp varExp
						&& varExp.getReferredVariable() != null) {
					String varName = varExp.getReferredVariable().getName();
					Object traceValue = match.bindings().get(rootVarNames.get(i));
					if (traceValue != null && !next.containsKey(varName)) {
						next.put(varName, traceValue);
					}
				}
			}
			extended.add(next);
		}
		return extended;
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
				// "The where clause specifies the condition that must be satisfied by all model
				// elements participating in the relation" (§7.2.1): enforced, it does not hold —
				// a lost assertion is an ERROR (#351, #308)
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE_ID, 0,
						"Relation '%s' does not hold: a where predicate is %s after enforcing (§7.2.1)"
								.formatted(relation.getName(), result),
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
		boolean previous = extentManager.matchingSources(true);
		try {
			return matchSourceDomainsOnSources(sourceDomains, initialBindings);
		} finally {
			extentManager.matchingSources(previous);
		}
	}

	private List<Map<String, Object>> matchSourceDomainsOnSources(List<RelationDomain> sourceDomains,
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

	// ── Deletion (§7.10.2) ───────────────────────────────────────────

	/**
	 * Deletes the target objects no valid binding requires: "for each valid binding of variables
	 * of domain k pattern that satisfies domain k condition, if there does not exist a valid
	 * binding of variables of the when clause and source domains that satisfies the when
	 * condition, source domain patterns and where condition [...] then delete the objects bound
	 * to the variables of domain k [...] only if it is not required to exist by any other valid
	 * binding of the source domains" (§7.10.2).
	 *
	 * <p>After all relations ran: every binding of an enforced target domain that no trace of its
	 * relation holds marks the objects bound to the domain's template variables — its root and
	 * nested objects (decided in #352) — and those no trace of any relation holds are deleted.
	 * Only objects of the target model; never in a check, never towards a checkonly domain.
	 */
	private void deleteUnmatched() {
		if (traceLimitReported) {
			// A cut trace misses bindings that hold: what it does not show is not unrequired
			return;
		}
		Set<EObject> required = Collections.newSetFromMap(new IdentityHashMap<>());
		for (QvtrTraceManager.TraceRecord record : traceManager.records()) {
			for (Object value : record.bindings().values()) {
				if (value instanceof EObject eo) {
					required.add(eo);
				}
			}
		}
		Set<EObject> doomed = Collections.newSetFromMap(new IdentityHashMap<>());
		for (Rule rule : transformation.getRule()) {
			if (!(rule instanceof Relation relation) || relation.isIsAbstract()) {
				continue;
			}
			for (RelationDomain domain : modelDomains(relation)) {
				if (!domain.isIsEnforceable() || !extentManager.isTargetModel(domain.getTypedModel())) {
					continue;
				}
				List<String> variables = templateVariables(domain);
				List<QvtrTraceManager.TraceRecord> traces = traceManager.lookup(relation, List.of());
				for (Map<String, Object> binding : patternMatcher.matchDomain(domain,
						initializedBindings(relation, Map.of()))) {
					if (traces.stream().anyMatch(trace -> holdsFor(trace, binding, variables))) {
						continue;
					}
					for (String variable : variables) {
						if (binding.get(variable) instanceof EObject eo && !required.contains(eo)
								&& inTargetModel(eo, domain)) {
							doomed.add(eo);
						}
					}
				}
			}
		}
		for (EObject object : doomed) {
			delete(object);
		}
	}

	/**
	 * Deletes an object of the target model: out of its container or the extent, and out of
	 * every reference the target model holds to it or to what it contains.
	 */
	@SuppressWarnings("unchecked")
	private void delete(EObject object) {
		QvtdModelExtent extent = null;
		for (Map.Entry<String, QvtdModelExtent> entry : context.modelExtents().entrySet()) {
			if (entry.getValue().getContents().contains(EcoreUtil.getRootContainer(object))) {
				extent = entry.getValue();
			}
		}
		if (extent == null) {
			return;
		}
		Set<EObject> gone = Collections.newSetFromMap(new IdentityHashMap<>());
		gone.add(object);
		object.eAllContents().forEachRemaining(gone::add);
		for (EObject root : List.copyOf(extent.getContents())) {
			List<EObject> tree = new ArrayList<>();
			tree.add(root);
			root.eAllContents().forEachRemaining(tree::add);
			for (EObject holder : tree) {
				if (gone.contains(holder)) {
					continue;
				}
				for (EReference reference : holder.eClass().getEAllReferences()) {
					if (reference.isContainment() || reference.isContainer() || reference.isDerived()
							|| !reference.isChangeable()) {
						continue;
					}
					Object value = holder.eGet(reference);
					if (reference.isMany()) {
						((List<EObject>) value).removeIf(gone::contains);
					} else if (value != null && gone.contains(value)) {
						holder.eUnset(reference);
					}
				}
			}
		}
		if (object.eContainer() != null) {
			EcoreUtil.remove(object);
		} else {
			extent.getContents().remove(object);
		}
	}

	/** Whether a trace record binds the domain's template variables to the same objects. */
	private static boolean holdsFor(QvtrTraceManager.TraceRecord trace, Map<String, Object> binding,
			List<String> variables) {
		for (String variable : variables) {
			if (binding.get(variable) instanceof EObject eo && trace.bindings().get(variable) != eo) {
				return false;
			}
		}
		return true;
	}

	/** The variables the object templates of a domain bind, root first, nested ones included. */
	private static List<String> templateVariables(RelationDomain domain) {
		List<String> variables = new ArrayList<>();
		for (DomainPattern pattern : domain.getPattern()) {
			TemplateExp root = pattern.getTemplateExpression();
			if (root == null) {
				continue;
			}
			List<EObject> templates = new ArrayList<>();
			templates.add(root);
			root.eAllContents().forEachRemaining(templates::add);
			for (EObject o : templates) {
				if (o instanceof ObjectTemplateExp template && template.getBindsTo() != null) {
					variables.add(template.getBindsTo().getName());
				}
			}
		}
		return variables;
	}

	private boolean inTargetModel(EObject object, RelationDomain domain) {
		return extentManager.getExtent(domain.getTypedModel()).getContents()
				.contains(EcoreUtil.getRootContainer(object));
	}

	// ── Checking (§7.9, §7.10.1) ─────────────────────────────────────

	/**
	 * Checks a relation in the direction of one domain: "For each valid binding of variables of
	 * the when clause and variables of domains other than the target domain k, that satisfy the
	 * when condition and source domain patterns and conditions, there must exist a valid binding
	 * of the remaining unbound variables of domain k that satisfies domain k's pattern and where
	 * condition" (§7.10.1). A binding without one is an inconsistency, reported with relation,
	 * direction and binding; a binding the relation holds for is traced, so the relations whose
	 * when clauses depend on it are checked too (#348).
	 */
	private void checkDirection(Relation relation, List<RelationDomain> others, RelationDomain direction,
			List<Map<String, Object>> whenBindings) {
		Set<Map<String, Object>> sourceBindings = new LinkedHashSet<>();
		for (Map<String, Object> whenBinding : whenBindings) {
			sourceBindings.addAll(others.isEmpty() ? List.of(whenBinding) : matchSourceDomains(others, whenBinding));
		}
		for (Map<String, Object> binding : List.copyOf(sourceBindings)) {
			checkDeadline();
			Map<String, Object> sourceBinding = new HashMap<>(binding);
			if (hasOptionalNullBinding(others, sourceBinding)) {
				recordTrace(relation, sourceBinding); // [?] without a match holds (#349)
				continue;
			}
			if (!whenConditionHolds(relation, sourceBinding)) {
				continue;
			}
			// The overriding relation is checked in its place: a top-level one in its own run,
			// any other one here, for this binding and in this direction (#356)
			Relation overriding = applicableOverride(relation, others, sourceBinding);
			if (overriding != null) {
				if (!overriding.isIsTopLevel()) {
					checkOverriding(relation, overriding, direction, sourceBinding);
				}
				continue;
			}
			queryEvaluator.preComputeWhereBindings(relation, sourceBinding);
			Map<String, Object> holding = null;
			for (Map<String, Object> match : patternMatcher.matchDomain(direction, sourceBinding)) {
				if (whereHolds(relation, match)) {
					holding = match;
					break;
				}
			}
			if (holding != null) {
				recordTrace(relation, holding);
			} else {
				reportInconsistency(relation, others, direction, sourceBinding);
			}
		}
	}

	/**
	 * Whether the where condition holds for a binding: its OCL predicates are true and the
	 * relations it calls hold for their arguments (§7.10.1 "satisfies domain k's pattern and where
	 * condition").
	 */
	private boolean whereHolds(Relation relation, Map<String, Object> binding) {
		Pattern where = relation.getWhere();
		if (where == null) {
			return true;
		}
		for (Predicate predicate : where.getPredicate()) {
			OclExpression expr = predicate.getConditionExpression();
			if (expr == null) {
				continue;
			}
			boolean holds = expr instanceof RelationCallExp call
					? relationHolds(call, binding)
					: Boolean.TRUE.equals(evaluateOcl(expr, binding));
			if (!holds) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Whether a relation called from a where clause holds for its arguments: there is a binding
	 * of all its domains, extending the arguments, that satisfies its when and where conditions.
	 * Checking calls it, it does not enforce it.
	 */
	private boolean relationHolds(RelationCallExp call, Map<String, Object> callerBinding) {
		Relation called = call.getReferredRelation();
		if (called == null) {
			return false;
		}
		Map<String, Object> preBindings = new HashMap<>();
		List<String> rootVarNames = QvtrTraceManager.collectRootVarNames(called);
		for (int i = 0; i < Math.min(rootVarNames.size(), call.getArgument().size()); i++) {
			Object value = evaluateOcl(call.getArgument().get(i), callerBinding);
			if (value != null) {
				preBindings.put(rootVarNames.get(i), value);
			}
		}
		return holds(called, preBindings);
	}

	/**
	 * Whether a relation holds for the given roots: some joint binding of its domains extends
	 * them and satisfies its when and where conditions. A binding an overriding relation applies
	 * to holds if that relation holds for it, and an abstract relation never holds directly
	 * (§7.11.1.4, #356).
	 */
	private boolean holds(Relation relation, Map<String, Object> preBindings) {
		if (relationCallDepth >= config.maxRelationDepth()) {
			throw new QvtdExecutionException("Relation call depth limit exceeded (%d) in relation '%s'"
					.formatted(config.maxRelationDepth(), relation.getName()));
		}
		relationCallDepth++;
		try {
			// An overriding relation is tried on the roots themselves: its own when clause and
			// patterns are its condition, whatever the overridden relation's patterns say
			for (Relation overriding : overriders.getOrDefault(relation, List.of())) {
				if (holds(overriding, overrideBindings(relation, overriding, preBindings))) {
					return true;
				}
			}
			if (relation.isIsAbstract()) {
				return false;
			}
			List<RelationDomain> domains = modelDomains(relation);
			for (Map<String, Object> whenBinding : evaluateWhenPreBindings(relation,
					initializedBindings(relation, preBindings))) {
				for (Map<String, Object> match : matchSourceDomains(domains, whenBinding)) {
					Map<String, Object> binding = new HashMap<>(match);
					if (whenConditionHolds(relation, binding) && applicableOverride(relation, domains, binding) == null
							&& whereHolds(relation, binding)) {
						recordTrace(relation, binding);
						return true;
					}
				}
			}
			return false;
		} finally {
			relationCallDepth--;
		}
	}

	/**
	 * Reports a binding a relation does not hold for in one direction: a WARNING with the code
	 * {@link QvtdExecutionResult#INCONSISTENCY} — the run did not fail, the models are not
	 * consistent (#348).
	 */
	private void reportInconsistency(Relation relation, List<RelationDomain> others, RelationDomain direction,
			Map<String, Object> binding) {
		StringBuilder described = new StringBuilder();
		for (RelationDomain domain : others) {
			for (Variable root : domain.getRootVariable()) {
				if (!described.isEmpty()) {
					described.append(", ");
				}
				described.append(root.getName()).append(" = ").append(describe(binding.get(root.getName())));
			}
		}
		String model = direction.getTypedModel() != null ? direction.getTypedModel().getName() : direction.getName();
		diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING, SOURCE_ID, QvtdExecutionResult.INCONSISTENCY,
				"Relation '%s' does not hold in direction '%s': no match for %s (§7.10.1)"
						.formatted(relation.getName(), model, described.isEmpty() ? "the empty binding" : described),
				null));
	}

	private static String describe(Object value) {
		if (value instanceof EObject eo) {
			EStructuralFeature name = eo.eClass().getEStructuralFeature("name");
			Object n = name != null ? eo.eGet(name) : null;
			return eo.eClass().getName() + (n != null ? " '" + n + "'" : "");
		}
		return value instanceof String str ? "'" + str + "'" : String.valueOf(value);
	}

	/** The domains of a relation that have a model, primitive domains left out (§7.13.4). */
	private List<RelationDomain> modelDomains(Relation relation) {
		List<RelationDomain> domains = new ArrayList<>();
		for (Domain domain : relation.getDomain()) {
			if (domain instanceof RelationDomain rd && !isPrimitiveDomain(rd)) {
				domains.add(rd);
			}
		}
		return domains;
	}

	private static List<RelationDomain> without(List<RelationDomain> domains, RelationDomain left) {
		List<RelationDomain> rest = new ArrayList<>(domains);
		rest.remove(left);
		return rest;
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
		// A relation call inside an expression — not R(p, s) — is handed back here (#358)
		OclContext ctx = new OclContext(null, null, bindings, null, null, (foreign, inner) -> {
			Map<String, Object> scope = new HashMap<>(bindings);
			scope.putAll(inner);
			return foreign instanceof RelationCallExp call ? relationCallValue(call, scope) : null;
		});
		try {
			// With diagnostics: what an expression reports is what tells an author why a relation
			// did not match, and the node it came from is what places it (#116). Before this they
			// were dropped, exactly as M2T dropped them until #114.
			// The base is the OCL engine's defaults — what OclConfiguration or the ocl.*
			// configuration set; until #258 it was lenient() built here, and no configured limit
			// reached a relation's expressions. LENIENT for the same reason QVT-O uses it: a
			// relation's queries and blackboxes have no self, and a call on a null source has to
			// proceed to the providers rather than fail (#112). The providers are the
			// transformation's own queries and blackboxes — without them OCL reported "Unknown
			// operation" for a nested query call and no error could be trusted (#118).
			OclResult result = oclEngine.evaluateWithDiagnostics(expression, ctx,
					oclEngine.getDefaultOptions()
							.withNullHandling(OclEvaluationOptions.NullHandling.LENIENT)
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
					blackboxBridge, this::evaluateOcl, queryDepth);
		}
		return operationProvider;
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
	 * Checks if a domain is a primitive domain (§7.13.4 primitive domain).
	 */
	private boolean isPrimitiveDomain(RelationDomain domain) {
		return domain.getTypedModel() == null && domain.getPattern().isEmpty();
	}

	/**
	 * Warns, once per run, about a relation with an {@code implementedby} clause that goes beyond
	 * the restrictions of QVT v1.3 §7.8 (#287).
	 *
	 * <p>"The Relations that may be implemented by Mapping Operations and Black box Operations are
	 * restricted": their domains <em>should</em> be primitive or a simple object template without
	 * sub-elements, and their when and where clauses <em>should</em> not define variables — the
	 * conditions belong in a wrapper relation that calls the simple one. "Should", not "shall", and
	 * the engine checks the relation before and after the operation anyway (#282), so this is a
	 * warning, worded and positioned like the other languages' runtime diagnostics.
	 */
	private void warnBeyondSection78(Relation relation) {
		if (relation.getOperationalImpl().isEmpty() || !section78Warned.add(relation.getName())) {
			return;
		}
		List<String> findings = new ArrayList<>();
		Set<String> domainBound = new HashSet<>();
		for (Domain d : relation.getDomain()) {
			if (!(d instanceof RelationDomain domain)) {
				continue;
			}
			domain.getRootVariable().forEach(v -> domainBound.add(v.getName()));
			for (DomainPattern pattern : domain.getPattern()) {
				TemplateExp template = pattern.getTemplateExpression();
				if (template == null) {
					continue;
				}
				template.eAllContents().forEachRemaining(node -> {
					if (node instanceof VariableExp ve && ve.getReferredVariable() != null) {
						domainBound.add(ve.getReferredVariable().getName());
					} else if (node instanceof TemplateExp nested && nested.getBindsTo() != null) {
						domainBound.add(nested.getBindsTo().getName());
					}
				});
				if (!(template instanceof ObjectTemplateExp simple) || !simple.getPart().isEmpty()) {
					findings.add("domain '" + domain.getName()
							+ "' is neither primitive nor a simple object template without sub-elements");
				}
			}
		}
		addDefinedVariables(relation.getWhen(), "when", domainBound, findings);
		addDefinedVariables(relation.getWhere(), "where", domainBound, findings);
		if (findings.isEmpty()) {
			return;
		}
		String message = "Relation '" + relation.getName() + "': implemented by an operation, but "
				+ String.join("; ", findings)
				+ " (§7.8) — keep it simple and constrain it from a wrapper relation that calls it";
		SourcePosition position = positionLookup.apply(relation.getOperationalImpl().get(0));
		diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING, SOURCE_ID, 0,
				position == null ? message : position + " " + message,
				position == null ? null : new Object[] { position }));
	}

	/** The variables a when- or where-clause binds that no domain binds — what §7.8 calls defining. */
	private static void addDefinedVariables(Pattern clause, String name, Set<String> domainBound,
			List<String> findings) {
		if (clause == null) {
			return;
		}
		Set<String> defined = new TreeSet<>();
		clause.eAllContents().forEachRemaining(node -> {
			if (node instanceof VariableExp ve && ve.getReferredVariable() != null
					// a let or iterator variable is local to its expression, not defined by the clause
					&& !(ve.getReferredVariable().eContainer() instanceof OclExpression)
					&& !domainBound.contains(ve.getReferredVariable().getName())) {
				defined.add(ve.getReferredVariable().getName());
			}
		});
		if (!defined.isEmpty()) {
			findings.add("its " + name + "-clause defines " + (defined.size() == 1 ? "variable " : "variables ")
					+ defined.stream().map(v -> "'" + v + "'").collect(Collectors.joining(", ")));
		}
	}

	/**
	 * Records a relation execution in the trace, and reports the first entry the trace limit
	 * drops (#284).
	 *
	 * <p>Past {@code maxTraceRecords} the trace stops growing, so a when-clause may miss what was
	 * executed afterwards and skip a relation that should run. Before #284 the trace manager only
	 * logged that and the run reported success; now it is an error, reported once, worded as
	 * QVT-O words it.
	 */
	private void recordTrace(Relation relation, Map<String, Object> binding) {
		if (!traceManager.record(relation, binding) && !traceLimitReported) {
			traceLimitReported = true;
			diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE_ID, 0,
					"Maximum trace records exceeded: " + config.maxTraceRecords()
							+ " — when-clauses may miss what was executed after it", null));
		}
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

	// ── Overrides (§7.11.1.4) ─────────────────────────────────────────

	/**
	 * Whether a binding of the relation is handled elsewhere: "The overriding rule is executed in
	 * place of the overridden rule when the overriding conditions are satisfied" (§7.11.1.4) —
	 * its when clause and the patterns of its non-target domains, decided in #356. A top-level
	 * overriding relation covers the binding in its own run; any other one, and every one a where
	 * clause reached the relation through, is invoked with the binding's roots. An abstract
	 * relation "is never matched directly": without an overriding relation for the binding it is
	 * an ERROR.
	 */
	private boolean overridden(Relation relation, List<RelationDomain> sourceDomains, Map<String, Object> binding) {
		Relation overriding = applicableOverride(relation, sourceDomains, binding);
		if (overriding != null) {
			if (relationCallDepth > 0 || !overriding.isIsTopLevel()) {
				executeRelationWithBindings(overriding, overrideBindings(relation, overriding, binding));
			}
			return true;
		}
		if (relation.isIsAbstract()) {
			StringBuilder roots = new StringBuilder();
			for (RelationDomain domain : sourceDomains) {
				for (Variable root : domain.getRootVariable()) {
					roots.append(roots.isEmpty() ? "" : ", ").append(root.getName()).append(" = ")
							.append(describe(binding.get(root.getName())));
				}
			}
			diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE_ID, 0,
					"Abstract relation '%s' is called, but no relation overriding it applies to %s (§7.11.1.4)"
							.formatted(relation.getName(), roots), null));
			return true;
		}
		return false;
	}

	/** Checks an overriding relation for one binding of the relation it overrides, in the same direction. */
	private void checkOverriding(Relation relation, Relation overriding, RelationDomain direction,
			Map<String, Object> binding) {
		List<RelationDomain> overridingDomains = modelDomains(overriding);
		int index = modelDomains(relation).indexOf(direction);
		if (index < 0 || index >= overridingDomains.size()) {
			return;
		}
		RelationDomain overridingDirection = overridingDomains.get(index);
		Map<String, Object> pre = initializedBindings(overriding, overrideBindings(relation, overriding, binding));
		checkDirection(overriding, without(overridingDomains, overridingDirection), overridingDirection,
				evaluateWhenPreBindings(overriding, pre));
	}

	/** The first overriding relation whose when clause and source patterns match the binding. */
	private Relation applicableOverride(Relation relation, List<RelationDomain> sourceDomains,
			Map<String, Object> binding) {
		List<RelationDomain> domains = modelDomains(relation);
		for (Relation overriding : overriders.getOrDefault(relation, List.of())) {
			List<RelationDomain> overridingDomains = modelDomains(overriding);
			List<RelationDomain> overridingSources = new ArrayList<>();
			for (RelationDomain source : sourceDomains) {
				int index = domains.indexOf(source);
				if (index >= 0 && index < overridingDomains.size()) {
					overridingSources.add(overridingDomains.get(index));
				}
			}
			Map<String, Object> pre = initializedBindings(overriding, overrideBindings(relation, overriding, binding));
			for (Map<String, Object> whenBinding : evaluateWhenPreBindings(overriding, pre)) {
				for (Map<String, Object> match : matchSourceDomains(overridingSources, whenBinding)) {
					if (whenConditionHolds(overriding, new HashMap<>(match))) {
						return overriding;
					}
				}
			}
		}
		return null;
	}

	/**
	 * The roots of a binding of the overridden relation, under the overriding relation's names:
	 * the domains correspond by position, the root variables of a domain too.
	 */
	private Map<String, Object> overrideBindings(Relation relation, Relation overriding, Map<String, Object> binding) {
		Map<String, Object> pre = new HashMap<>();
		List<RelationDomain> from = modelDomains(relation);
		List<RelationDomain> to = modelDomains(overriding);
		for (int i = 0; i < Math.min(from.size(), to.size()); i++) {
			List<Variable> fromRoots = from.get(i).getRootVariable();
			List<Variable> toRoots = to.get(i).getRootVariable();
			for (int j = 0; j < Math.min(fromRoots.size(), toRoots.size()); j++) {
				String name = fromRoots.get(j).getName();
				if (binding.containsKey(name)) {
					pre.put(toRoots.get(j).getName(), binding.get(name));
				}
			}
		}
		return pre;
	}
}
