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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtbase.Domain;
import org.eclipse.fennec.m2x.model.qvtbase.Function;
import org.eclipse.fennec.m2x.model.qvtbase.Pattern;
import org.eclipse.fennec.m2x.model.qvtbase.Predicate;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern;
import org.eclipse.fennec.m2x.model.qvtrelation.Key;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomainAssignment;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationCallExp;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationImplementation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem;
import org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionException;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.RelationImplementationProvider;

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
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtrEvaluator {

	private static final String SOURCE_ID = "org.eclipse.fennec.m2x.qvtd.engine";

	private final OclEngineImpl oclEngine;
	private final QvtrEvalEnvironment env;
	private final RelationalTransformation transformation;
	private final QvtrExtentManager extentManager;
	private final QvtdExecutionContext context;
	private final QvtdBlackboxRegistry blackboxRegistry;
	private final List<RelationImplementationProvider> implementationProviders;
	private final QvtrPatternMatcher patternMatcher;
	private final QvtrTraceManager traceManager = new QvtrTraceManager();
	private final List<Diagnostic> diagnostics = new ArrayList<>();

	public QvtrEvaluator(OclEngineImpl oclEngine, QvtrEvalEnvironment env,
			RelationalTransformation transformation, QvtrExtentManager extentManager,
			QvtdExecutionContext context, QvtdBlackboxRegistry blackboxRegistry,
			List<RelationImplementationProvider> implementationProviders) {
		this.oclEngine = Objects.requireNonNull(oclEngine, "oclEngine must not be null");
		this.env = Objects.requireNonNull(env, "env must not be null");
		this.transformation = Objects.requireNonNull(transformation, "transformation must not be null");
		this.extentManager = Objects.requireNonNull(extentManager, "extentManager must not be null");
		this.context = Objects.requireNonNull(context, "context must not be null");
		this.blackboxRegistry = blackboxRegistry; // may be null
		this.implementationProviders = Objects.requireNonNull(implementationProviders);
		this.patternMatcher = new QvtrPatternMatcher(oclEngine, extentManager);
	}

	/**
	 * Executes the transformation: iterates over all top-level relations and
	 * either checks or enforces them based on the execution context.
	 *
	 * @return collected diagnostics (empty list = success)
	 */
	public List<Diagnostic> execute() {
		// Resolve deferred overrides (EAnnotation → Rule.overrides reference)
		resolveOverrides();

		// Collect overridden relations so they can be skipped
		Set<String> overriddenNames = new java.util.HashSet<>();
		for (Rule rule : transformation.getRule()) {
			if (rule instanceof Relation rel && rel.getOverrides() != null) {
				overriddenNames.add(rel.getOverrides().getName());
			}
		}

		for (Rule rule : transformation.getRule()) {
			if (rule instanceof Relation relation && relation.isIsTopLevel()) {
				// Skip abstract relations — they serve as templates only (§7.11.3.2)
				if (relation.isIsAbstract()) {
					continue;
				}
				// Skip relations that have been overridden (§7.6)
				if (overriddenNames.contains(relation.getName())) {
					continue;
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

	/**
	 * Executes a single relation (§7.10.1 / §7.10.2).
	 */
	private void executeRelation(Relation relation) {
		env.pushScope();
		try {
			// 1. Evaluate when-clause to get pre-bindings
			Map<String, Object> whenBindings = evaluateWhenClause(relation);

			// When-clause not satisfied → relation does not need to hold (§7.2.1)
			if (whenBindings == null) {
				return;
			}

			// 2. Classify domains into source, target, and primitive
			List<RelationDomain> sourceDomains = new ArrayList<>();
			RelationDomain targetDomain = null;

			for (Domain domain : relation.getDomain()) {
				if (domain instanceof RelationDomain rd) {
					if (isPrimitiveDomain(rd)) {
						// Primitive domains (§7.11.3.10): no model association,
						// variables already bound from relation call arguments
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
				// [?] optional: skip enforcement when source root variable is null
				if (hasOptionalNullBinding(sourceDomains, sourceBinding)) {
					continue;
				}

				// Pre-compute where-clause variable bindings (e.g. query calls)
				// before enforcement so target patterns can use computed values
				preComputeWhereBindings(relation, sourceBinding);

				if (targetDomain != null) {
					enforceTargetDomain(relation, targetDomain, sourceBinding);
				}
				// Record trace for this successful binding (§7.2.1)
				traceManager.record(relation, sourceBinding);
				// 5. Evaluate where-clause (may invoke non-top relations)
				evaluateWhereClause(relation, sourceBinding);
			}
		} finally {
			env.popScope();
		}
	}

	/**
	 * Evaluates the when-clause of a relation (§7.2.1).
	 * Returns pre-bound variables from the when predicates.
	 *
	 * <p>RelationCallExp in when-clauses performs trace lookup: the called
	 * relation must have been previously executed with matching argument values.
	 * If a trace is found, the bindings from the trace are merged into the
	 * current bindings, pre-binding variables for the current relation.
	 */
	private Map<String, Object> evaluateWhenClause(Relation relation) {
		Map<String, Object> bindings = new HashMap<>();
		Pattern when = relation.getWhen();
		if (when == null) {
			return bindings;
		}

		for (Predicate predicate : when.getPredicate()) {
			OclExpression expr = predicate.getConditionExpression();
			if (expr == null) {
				continue;
			}

			// RelationCallExp in when → trace lookup (§7.2.1)
			if (expr instanceof RelationCallExp relCall) {
				if (!evaluateWhenRelationCall(relCall, bindings)) {
					return null; // No matching trace → when not satisfied
				}
				continue;
			}

			Object result = evaluateOcl(expr, bindings);
			if (!Boolean.TRUE.equals(result)) {
				return null;
			}
		}
		return bindings;
	}

	/**
	 * Evaluates a RelationCallExp in a when-clause by looking up the trace.
	 * Returns true if a matching trace was found (and merges bindings).
	 */
	private boolean evaluateWhenRelationCall(RelationCallExp relCall,
			Map<String, Object> bindings) {
		Relation calledRelation = relCall.getReferredRelation();
		if (calledRelation == null) {
			return false;
		}

		// Resolve argument values from current bindings.
		// For VariableExp arguments that are unbound (free variables), pass null
		// so the trace lookup treats them as wildcards (§7.2.1).
		List<Object> argValues = new ArrayList<>();
		for (OclExpression argExpr : relCall.getArgument()) {
			if (argExpr instanceof VariableExp varExp) {
				Variable ref = varExp.getReferredVariable();
				if (ref != null) {
					argValues.add(bindings.get(ref.getName())); // null if unbound
				} else {
					argValues.add(null);
				}
			} else {
				argValues.add(evaluateOcl(argExpr, bindings));
			}
		}

		// Look up trace
		List<QvtrTraceManager.TraceRecord> traces = traceManager.lookup(
				calledRelation, argValues);
		if (traces.isEmpty()) {
			return false;
		}

		// Merge bindings from the first matching trace into current bindings.
		// This pre-binds variables for the current relation (e.g. when
		// PackageToSchema(p, s) binds 'p' and 's' for ClassToTable).
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

	/**
	 * Matches source domains sequentially, threading bindings from one domain
	 * to the next (shared variables across domains).
	 */
	private List<Map<String, Object>> matchSourceDomains(List<RelationDomain> sourceDomains,
			Map<String, Object> initialBindings) {
		List<Map<String, Object>> currentBindings = List.of(initialBindings);

		for (RelationDomain domain : sourceDomains) {
			List<Map<String, Object>> nextBindings = new ArrayList<>();
			for (Map<String, Object> binding : currentBindings) {
				nextBindings.addAll(patternMatcher.matchDomain(domain, binding));
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
			return; // When-clause not satisfied
		}

		List<Map<String, Object>> bindings = matchSourceDomains(domains, whenBindings);

		if (bindings.isEmpty() && !domains.isEmpty()) {
			diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING, SOURCE_ID, 0,
					"Relation '%s': no matching bindings found in check-only mode"
							.formatted(relation.getName()),
					null));
		}

		// Evaluate where-clause for all found bindings
		for (Map<String, Object> binding : bindings) {
			evaluateWhereClause(relation, binding);
		}
	}

	/**
	 * Enforces the target domain (§7.10.2): tries to find a matching target element.
	 * If not found, creates one by instantiating the template pattern.
	 */
	private void enforceTargetDomain(Relation relation, RelationDomain targetDomain,
			Map<String, Object> sourceBindings) {
		// First try to match the target domain with existing elements
		List<Map<String, Object>> targetMatches = patternMatcher.matchDomain(
				targetDomain, sourceBindings);

		if (!targetMatches.isEmpty()) {
			// Target already consistent — merge bindings from first match
			sourceBindings.putAll(targetMatches.get(0));
			return;
		}

		// Apply default value assignments (§7.11.3.7) for unbound variables
		for (RelationDomainAssignment assignment : targetDomain.getDefaultAssignment()) {
			Variable var = assignment.getVariable();
			if (var != null && !sourceBindings.containsKey(var.getName())) {
				Object value = evaluateOcl(assignment.getValueExp(), sourceBindings);
				if (value != null) {
					sourceBindings.put(var.getName(), value);
				}
			}
		}

		// implementedby clause (§7.11.3.6): delegate to operational implementation
		// instead of template-based enforcement
		TypedModel targetModel = targetDomain.getTypedModel();
		if (invokeImplementedBy(relation, targetModel, sourceBindings)) {
			return;
		}

		// No match found → enforce: create target objects from template
		for (DomainPattern pattern : targetDomain.getPattern()) {
			TemplateExp templateExp = pattern.getTemplateExpression();
			if (templateExp instanceof ObjectTemplateExp objectTemplate) {
				EObject created = enforceObjectTemplate(objectTemplate, sourceBindings, targetModel);
				if (created != null) {
					// Add created object to the target extent only if it's a new root
					// object (not already contained via EMF containment or extent)
					if (created.eContainer() == null
							&& !extentManager.getExtent(targetModel).getContents().contains(created)) {
						extentManager.getExtent(targetModel).add(created);
					}

					// Bind root variable
					Variable rootVar = objectTemplate.getBindsTo();
					if (rootVar != null) {
						sourceBindings.put(rootVar.getName(), created);
					}
				}
			}
		}
	}

	/**
	 * Creates or finds an EObject by instantiating an ObjectTemplateExp pattern (§7.10.2).
	 * Uses Key-based identity (§7.4) to find existing objects before creating new ones.
	 */
	private EObject enforceObjectTemplate(ObjectTemplateExp template,
			Map<String, Object> bindings, TypedModel targetModel) {
		EClass eClass = template.getReferredClass();
		if (eClass == null || eClass.isAbstract() || eClass.isInterface()) {
			diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE_ID, 0,
					"Cannot instantiate abstract class or interface: " + eClass, null));
			return null;
		}

		// Check if variable is already bound (e.g. from when-clause or shared variable)
		Variable bindVar = template.getBindsTo();
		if (bindVar != null) {
			Object existing = bindings.get(bindVar.getName());
			if (existing instanceof EObject existingObj) {
				// Object exists — update properties
				enforceProperties(template, existingObj, bindings, targetModel);
				return existingObj;
			}
		}

		// Key-based identity (§7.4): look for existing object matching key properties
		EObject keyMatch = findByKey(template, bindings, eClass, targetModel);
		if (keyMatch != null) {
			// Found existing object via key — update non-key properties
			if (bindVar != null) {
				bindings.put(bindVar.getName(), keyMatch);
			}
			enforceProperties(template, keyMatch, bindings, targetModel);
			return keyMatch;
		}

		// In-place / variable-based identity (§7.7): find existing object matching
		// properties whose values come from already-bound variables. This handles
		// in-place transformations where source and target share the same model —
		// bound variable constraints identify the object, literals are enforced.
		EObject varMatch = findByBoundVariables(template, bindings, eClass, targetModel);
		if (varMatch != null) {
			if (bindVar != null) {
				bindings.put(bindVar.getName(), varMatch);
			}
			enforceProperties(template, varMatch, bindings, targetModel);
			return varMatch;
		}

		// Create new instance
		EObject created = EcoreUtil.create(eClass);

		// Bind variable
		if (bindVar != null) {
			bindings.put(bindVar.getName(), created);
		}

		// Set properties
		enforceProperties(template, created, bindings, targetModel);

		return created;
	}

	/**
	 * Finds an existing object in the target extent that matches the Key properties (§7.4).
	 * Returns {@code null} if no key is defined or no match is found.
	 */
	private EObject findByKey(ObjectTemplateExp template, Map<String, Object> bindings,
			EClass eClass, TypedModel targetModel) {
		Key key = findKey(eClass);
		if (key == null || key.getPart().isEmpty()) {
			return null;
		}

		// Compute expected key values from the template's PropertyTemplateItems
		Map<String, Object> keyValues = new HashMap<>();
		for (EStructuralFeature keyPart : key.getPart()) {
			String keyPropName = keyPart.getName();
			// Find the corresponding PropertyTemplateItem in the template
			for (PropertyTemplateItem item : template.getPart()) {
				EStructuralFeature itemFeature = item.getReferredProperty();
				if (itemFeature != null && keyPropName.equals(itemFeature.getName())) {
					Object value = evaluatePropertyValue(item, bindings);
					if (value != null) {
						keyValues.put(keyPropName, value);
					}
					break;
				}
			}
		}

		// All key parts must have values
		if (keyValues.size() != key.getPart().size()) {
			return null;
		}

		// Search the target extent for a matching object
		List<EObject> candidates = extentManager.allInstances(targetModel, eClass);
		for (EObject candidate : candidates) {
			boolean allMatch = true;
			for (Map.Entry<String, Object> entry : keyValues.entrySet()) {
				EStructuralFeature feature = candidate.eClass().getEStructuralFeature(entry.getKey());
				if (feature == null) {
					allMatch = false;
					break;
				}
				Object actualValue = candidate.eGet(feature);
				if (!Objects.equals(actualValue, entry.getValue())) {
					allMatch = false;
					break;
				}
			}
			if (allMatch) {
				return candidate;
			}
		}
		return null;
	}

	/**
	 * Finds an existing object in the target extent whose properties match the
	 * template constraints backed by already-bound variables (§7.7).
	 *
	 * <p>This supports in-place transformations: when source and target share
	 * the same model, variable-valued property constraints (e.g. {@code name = cn}
	 * where {@code cn} is bound from source matching) identify the existing object,
	 * while literal-valued constraints (e.g. {@code kind = 'Persistent'}) are
	 * enforcement values to be set later by {@link #enforceProperties}.
	 *
	 * @return the matching object, or {@code null} if none found
	 */
	private EObject findByBoundVariables(ObjectTemplateExp template,
			Map<String, Object> bindings, EClass eClass, TypedModel targetModel) {
		// Collect constraints from variable-backed properties only
		Map<EStructuralFeature, Object> constraints = new HashMap<>();
		for (PropertyTemplateItem item : template.getPart()) {
			EStructuralFeature feature = item.getReferredProperty();
			if (feature == null) {
				continue;
			}
			OclExpression valueExpr = item.getValue();
			if (valueExpr instanceof VariableExp varExp) {
				Variable ref = varExp.getReferredVariable();
				if (ref != null && bindings.containsKey(ref.getName())) {
					Object value = bindings.get(ref.getName());
					if (value != null) {
						EStructuralFeature resolved = eClass.getEStructuralFeature(
								feature.getName());
						if (resolved != null) {
							constraints.put(resolved, value);
						}
					}
				}
			}
		}
		if (constraints.isEmpty()) {
			return null;
		}

		// Search the target extent for a matching object
		List<EObject> candidates = extentManager.allInstances(targetModel, eClass);
		for (EObject candidate : candidates) {
			boolean allMatch = true;
			for (Map.Entry<EStructuralFeature, Object> entry : constraints.entrySet()) {
				if (!Objects.equals(candidate.eGet(entry.getKey()), entry.getValue())) {
					allMatch = false;
					break;
				}
			}
			if (allMatch) {
				return candidate;
			}
		}
		return null;
	}

	/**
	 * Evaluates the value of a PropertyTemplateItem for key comparison.
	 */
	private Object evaluatePropertyValue(PropertyTemplateItem item, Map<String, Object> bindings) {
		OclExpression valueExpr = item.getValue();
		if (valueExpr instanceof VariableExp varExp) {
			Variable ref = varExp.getReferredVariable();
			return ref != null ? bindings.get(ref.getName()) : null;
		}
		return evaluateOcl(valueExpr, bindings);
	}

	/**
	 * Finds the Key declaration for the given EClass (§7.4).
	 */
	private Key findKey(EClass eClass) {
		for (Key key : transformation.getOwnedKey()) {
			EClass identifies = key.getIdentifies();
			if (identifies != null && identifies.getName().equals(eClass.getName())) {
				return key;
			}
		}
		return null;
	}

	/**
	 * Sets properties on a target object according to the template's PropertyTemplateItems.
	 */
	@SuppressWarnings("unchecked")
	private void enforceProperties(ObjectTemplateExp template, EObject target,
			Map<String, Object> bindings, TypedModel targetModel) {
		for (PropertyTemplateItem item : template.getPart()) {
			EStructuralFeature feature = resolveFeature(item, target);
			if (feature == null) {
				continue;
			}

			OclExpression valueExpr = item.getValue();

			// Nested ObjectTemplateExp → recursive enforce
			if (valueExpr instanceof ObjectTemplateExp nestedTemplate) {
				EObject nested = enforceObjectTemplate(nestedTemplate, bindings, targetModel);
				if (nested != null) {
					if (feature.isMany()) {
						((List<EObject>) target.eGet(feature)).add(nested);
					} else {
						target.eSet(feature, nested);
					}
				}
				continue;
			}

			// CollectionTemplateExp → enforce each member template (§7.11.2.3)
			if (valueExpr instanceof CollectionTemplateExp collTemplate) {
				enforceCollectionTemplate(collTemplate, target, feature, bindings, targetModel);
				continue;
			}

			// If value is a VariableExp, resolve from bindings directly
			Object value;
			if (valueExpr instanceof VariableExp varExp) {
				Variable ref = varExp.getReferredVariable();
				value = ref != null ? bindings.get(ref.getName()) : null;
			} else {
				value = evaluateOcl(valueExpr, bindings);
			}
			if (value != null) {
				if (feature.isMany()) {
					((List<Object>) target.eGet(feature)).add(value);
				} else {
					target.eSet(feature, value);
				}
			}
		}
	}

	/**
	 * Evaluates the where-clause of a relation (§7.2.1).
	 *
	 * <p>RelationCallExp in where-clauses invokes non-top relations (§7.2.2):
	 * the called relation is executed with argument values bound from the
	 * current bindings.
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

			// RelationCallExp in where → invoke non-top relation (§7.2.2)
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
	 *
	 * <p>The arguments to the RelationCallExp are evaluated and bound to the
	 * root variables of the called relation's domains. The relation is then
	 * executed with these pre-bindings.
	 */
	private void invokeNonTopRelation(RelationCallExp relCall,
			Map<String, Object> callerBindings) {
		Relation calledRelation = relCall.getReferredRelation();
		if (calledRelation == null) {
			return;
		}

		// Resolve argument values from caller bindings
		List<Object> argValues = new ArrayList<>();
		for (OclExpression argExpr : relCall.getArgument()) {
			argValues.add(evaluateOcl(argExpr, callerBindings));
		}

		// Build pre-bindings: map root variable names → argument values
		Map<String, Object> preBindings = new HashMap<>();
		List<String> rootVarNames = QvtrTraceManager.collectRootVarNames(calledRelation);
		for (int i = 0; i < Math.min(rootVarNames.size(), argValues.size()); i++) {
			Object value = argValues.get(i);
			if (value != null) {
				preBindings.put(rootVarNames.get(i), value);
			}
		}

		// Execute the called relation with pre-bindings
		executeRelationWithBindings(calledRelation, preBindings);
	}

	/**
	 * Executes a relation with pre-bound root variable values.
	 * Used for non-top relation invocation from where-clauses.
	 */
	private void executeRelationWithBindings(Relation relation,
			Map<String, Object> preBindings) {
		env.pushScope();
		try {
			// 1. Evaluate when-clause (if any)
			Map<String, Object> whenBindings = evaluateWhenClause(relation);
			if (whenBindings == null) {
				return;
			}

			// Merge pre-bindings with when-bindings
			Map<String, Object> mergedBindings = new HashMap<>(preBindings);
			mergedBindings.putAll(whenBindings);

			// 2. Classify domains
			List<RelationDomain> sourceDomains = new ArrayList<>();
			RelationDomain targetDomain = null;

			for (Domain domain : relation.getDomain()) {
				if (domain instanceof RelationDomain rd) {
					if (isPrimitiveDomain(rd)) {
						continue; // §7.11.3.10: already bound from call arguments
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

			// 3. Match source domains with pre-bindings
			List<Map<String, Object>> sourceBindings = matchSourceDomains(
					sourceDomains, mergedBindings);

			// 4. Enforce target for each binding
			for (Map<String, Object> binding : sourceBindings) {
				// [?] optional: skip enforcement when source root variable is null
				if (hasOptionalNullBinding(sourceDomains, binding)) {
					traceManager.record(relation, binding);
					continue;
				}
				preComputeWhereBindings(relation, binding);
				if (targetDomain != null) {
					enforceTargetDomain(relation, targetDomain, binding);
				}
				traceManager.record(relation, binding);
				evaluateWhereClause(relation, binding);
			}
		} finally {
			env.popScope();
		}
	}

	/**
	 * Evaluates an OCL expression with the current variable bindings.
	 */
	private Object evaluateOcl(OclExpression expression, Map<String, Object> bindings) {
		// Short-circuit VariableExp — resolve directly from bindings
		if (expression instanceof VariableExp varExp) {
			Variable ref = varExp.getReferredVariable();
			if (ref != null && bindings.containsKey(ref.getName())) {
				return bindings.get(ref.getName());
			}
		}
		OclContext ctx = new OclContext(null, null, bindings);
		try {
			return oclEngine.evaluate(expression, ctx);
		} catch (Exception e) {
			diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING, SOURCE_ID, 0,
					"OCL evaluation error: " + e.getMessage(), null));
			return null;
		}
	}

	/**
	 * Resolves the actual EStructuralFeature from the object's EClass.
	 * The parser may create placeholder features; this resolves by name.
	 */
	private EStructuralFeature resolveFeature(PropertyTemplateItem item, EObject object) {
		EStructuralFeature feature = item.getReferredProperty();
		if (feature == null) {
			return null;
		}
		EClass objectClass = object.eClass();

		// Opposite property (§7.11.2.4): resolve via eOpposite
		if (item.isIsOpposite()) {
			return resolveOppositeFeature(feature, objectClass);
		}

		if (objectClass.getEAllStructuralFeatures().contains(feature)) {
			return feature;
		}
		return objectClass.getEStructuralFeature(feature.getName());
	}

	/**
	 * Resolves an opposite property by finding the eOpposite on the object's class.
	 */
	private EStructuralFeature resolveOppositeFeature(EStructuralFeature feature,
			EClass objectClass) {
		if (feature instanceof EReference ref && ref.getEOpposite() != null) {
			EReference opposite = ref.getEOpposite();
			if (objectClass.getEAllStructuralFeatures().contains(opposite)) {
				return opposite;
			}
			return objectClass.getEStructuralFeature(opposite.getName());
		}
		// Fallback: search by opposite name
		String featureName = feature.getName();
		for (EReference ref : objectClass.getEAllReferences()) {
			if (ref.getEOpposite() != null
					&& featureName.equals(ref.getEOpposite().getName())) {
				return ref;
			}
		}
		return null;
	}

	/**
	 * Checks if a domain is a primitive domain (§7.11.3.10).
	 * Primitive domains have no model association (no TypedModel) and no patterns.
	 */
	private boolean isPrimitiveDomain(RelationDomain domain) {
		return domain.getTypedModel() == null && domain.getPattern().isEmpty();
	}

	/**
	 * Checks whether any source domain has an optional [?] root variable that
	 * is bound to null. In that case, the relation matched "vacuously" and
	 * enforcement of the target domain should be skipped.
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
	 * Pre-computes where-clause variable bindings that can be resolved before
	 * enforcement. Handles predicates of the form {@code var = expr} where
	 * {@code var} is unbound and {@code expr} can be evaluated (including
	 * query calls, §7.11.4).
	 */
	private void preComputeWhereBindings(Relation relation, Map<String, Object> bindings) {
		Pattern where = relation.getWhere();
		if (where == null) {
			return;
		}

		for (Predicate predicate : where.getPredicate()) {
			OclExpression expr = predicate.getConditionExpression();
			if (expr instanceof RelationCallExp) {
				continue; // Handled in evaluateWhereClause
			}
			if (!(expr instanceof OperationCallExp eqOp) || !"=".equals(eqOp.getName())) {
				continue;
			}

			// Check if left side (source) is an unbound VariableExp
			OclExpression source = eqOp.getOwnedSource();
			if (source instanceof VariableExp varExp) {
				Variable ref = varExp.getReferredVariable();
				if (ref != null && !bindings.containsKey(ref.getName())
						&& !eqOp.getOwnedArguments().isEmpty()) {
					Object value = evaluateExprWithQueries(
							eqOp.getOwnedArguments().get(0), bindings);
					if (value != null) {
						bindings.put(ref.getName(), value);
					}
					continue;
				}
			}

			// Check if right side (argument) is an unbound VariableExp
			if (!eqOp.getOwnedArguments().isEmpty()) {
				OclExpression arg = eqOp.getOwnedArguments().get(0);
				if (arg instanceof VariableExp varExp) {
					Variable ref = varExp.getReferredVariable();
					if (ref != null && !bindings.containsKey(ref.getName())) {
						Object value = evaluateExprWithQueries(source, bindings);
						if (value != null) {
							bindings.put(ref.getName(), value);
						}
					}
				}
			}
		}
	}

	/**
	 * Evaluates an expression, resolving query calls (§7.11.4) from the
	 * transformation's Function definitions.
	 */
	private Object evaluateExprWithQueries(OclExpression expr, Map<String, Object> bindings) {
		if (expr instanceof OperationCallExp opCall) {
			Function query = resolveQuery(opCall.getName());
			if (query != null) {
				return evaluateQueryCall(query, opCall, bindings);
			}
		}
		return evaluateOcl(expr, bindings);
	}

	/**
	 * Resolves a query Function by name from the transformation's synthetic
	 * {@code _queries} EClass.
	 */
	private Function resolveQuery(String name) {
		if (name == null) {
			return null;
		}
		for (EClassifier classifier : transformation.getEClassifiers()) {
			if ("_queries".equals(classifier.getName()) && classifier instanceof EClass queriesClass) {
				for (EOperation op : queriesClass.getEOperations()) {
					if (op instanceof Function f && name.equals(f.getName())) {
						return f;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Evaluates a query Function call (§7.11.4). Binds the query's parameters
	 * to the argument values and evaluates the queryExpression.
	 */
	private Object evaluateQueryCall(Function query, OperationCallExp callExpr,
			Map<String, Object> callerBindings) {
		OclExpression queryBody = query.getQueryExpression();
		if (queryBody == null) {
			// Blackbox query (§7.8): delegate to registered blackbox library
			return evaluateBlackboxQuery(query, callExpr, callerBindings);
		}

		// Build parameter bindings
		Map<String, Object> paramBindings = new HashMap<>();
		EList<EParameter> params = query.getEParameters();
		List<OclExpression> args = callExpr.getOwnedArguments();
		for (int i = 0; i < Math.min(params.size(), args.size()); i++) {
			Object argValue = evaluateOcl(args.get(i), callerBindings);
			paramBindings.put(params.get(i).getName(), argValue);
		}

		// Evaluate query body with parameter bindings
		return evaluateOcl(queryBody, paramBindings);
	}

	/**
	 * Enforces a CollectionTemplateExp on a target property (§7.11.2.3).
	 * For each ObjectTemplateExp member, creates/finds a matching object
	 * and adds it to the collection property.
	 */
	@SuppressWarnings("unchecked")
	private void enforceCollectionTemplate(CollectionTemplateExp collTemplate,
			EObject target, EStructuralFeature feature,
			Map<String, Object> bindings, TypedModel targetModel) {
		for (OclExpression memberExpr : collTemplate.getMember()) {
			if (memberExpr instanceof ObjectTemplateExp memberTemplate) {
				EObject member = enforceObjectTemplate(memberTemplate, bindings, targetModel);
				if (member != null && feature.isMany()) {
					((List<EObject>) target.eGet(feature)).add(member);
				}
			}
		}
	}

	/**
	 * Invokes an implementedby operational implementation (§7.11.3.6).
	 * Searches the relation's {@code operationalImpl} list for an implementation
	 * matching the target direction and delegates first to registered
	 * {@link RelationImplementationProvider}s (D39, Phase 4b), then falls back
	 * to the blackbox registry.
	 *
	 * @return {@code true} if an implementation was found and invoked
	 */
	private boolean invokeImplementedBy(Relation relation, TypedModel targetModel,
			Map<String, Object> bindings) {
		for (RelationImplementation impl : relation.getOperationalImpl()) {
			TypedModel direction = impl.getInDirectionOf();
			if (direction != null && targetModel.getName().equals(direction.getName())) {
				EOperation op = impl.getImpl();
				if (op == null) {
					continue;
				}
				String opName = op.getName();

				// §7.8 / D39: Try RelationImplementationProviders first (QVT-O hybrid)
				String qualifiedName = relation.getName();
				for (RelationImplementationProvider provider : implementationProviders) {
					if (provider.canProvide(qualifiedName)) {
						QvtdExecutionResult result = provider.executeRelation(qualifiedName, context);
						if (result.isSuccess()) {
							return true;
						}
						diagnostics.addAll(result.diagnostics());
						return true; // invoked, even if errors
					}
				}

				// Fallback: blackbox registry
				if (blackboxRegistry == null) {
					continue;
				}

				// Evaluate argument expressions from annotation
				Object[] argValues = new Object[0];
				EAnnotation ann = impl.getEAnnotation("qvtr.implementedby.args");
				if (ann != null) {
					List<EObject> argExprs = ann.getReferences();
					argValues = new Object[argExprs.size()];
					for (int i = 0; i < argExprs.size(); i++) {
						if (argExprs.get(i) instanceof OclExpression argExpr) {
							argValues[i] = evaluateOcl(argExpr, bindings);
						}
					}
				}

				// Invoke through blackbox registry
				for (var library : blackboxRegistry.getLibraries()) {
					try {
						Object result = library.invoke(opName, null, argValues);
						if (result instanceof EObject created) {
							// Add to target extent if needed
							if (created.eContainer() == null) {
								extentManager.getExtent(targetModel).add(created);
							}
						}
						return true;
					} catch (Exception e) {
						// Library doesn't support this operation — try next
					}
				}
			}
		}
		return false;
	}

	/**
	 * Evaluates a blackbox query by delegating to the registered blackbox
	 * library (§7.8). Searches all registered libraries for an operation
	 * matching the query name.
	 */
	private Object evaluateBlackboxQuery(Function query, OperationCallExp callExpr,
			Map<String, Object> callerBindings) {
		if (blackboxRegistry == null) {
			return null;
		}

		// Evaluate arguments
		List<OclExpression> args = callExpr.getOwnedArguments();
		Object[] argValues = new Object[args.size()];
		for (int i = 0; i < args.size(); i++) {
			argValues[i] = evaluateOcl(args.get(i), callerBindings);
		}

		// Search all registered libraries for the operation
		String queryName = query.getName();
		for (var library : blackboxRegistry.getLibraries()) {
			try {
				Object result = library.invoke(queryName, null, argValues);
				if (result != null) {
					return result;
				}
			} catch (Exception e) {
				// Library doesn't support this operation — try next
			}
		}
		return null;
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
					Relation base = relationsByName.get(overridesName);
					if (base != null) {
						rel.setOverrides(base);
					}
				}
			}
		}
	}
}
