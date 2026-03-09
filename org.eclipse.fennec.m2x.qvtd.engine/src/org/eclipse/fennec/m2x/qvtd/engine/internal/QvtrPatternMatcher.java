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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem;
import org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;

/**
 * Pattern matcher for QVT-R domain patterns (§7.10.3).
 *
 * <p>Given a {@link RelationDomain} and a set of pre-bound variables (from the
 * when-clause or previous domain matches), this class finds all valid variable
 * bindings by matching the domain's template expressions against model elements
 * from the corresponding extent.
 *
 * <p>The matching algorithm:
 * <ol>
 *   <li>Determine the root variable and its type from the DomainPattern</li>
 *   <li>Collect all candidate instances of that type from the extent</li>
 *   <li>For each candidate, recursively match PropertyTemplateItems</li>
 *   <li>Each successful match produces a binding map (variable name → value)</li>
 * </ol>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtrPatternMatcher {

	private final OclEngineImpl oclEngine;
	private final QvtrExtentManager extentManager;

	public QvtrPatternMatcher(OclEngineImpl oclEngine, QvtrExtentManager extentManager) {
		this.oclEngine = Objects.requireNonNull(oclEngine, "oclEngine must not be null");
		this.extentManager = Objects.requireNonNull(extentManager, "extentManager must not be null");
	}

	/**
	 * Matches a relation domain against its model extent and returns all valid
	 * variable bindings.
	 *
	 * @param domain the relation domain to match
	 * @param preBindings variables already bound (from when-clause or other domains)
	 * @return list of binding maps, one per successful match
	 */
	public List<Map<String, Object>> matchDomain(RelationDomain domain,
			Map<String, Object> preBindings) {
		Objects.requireNonNull(domain, "domain must not be null");
		Objects.requireNonNull(preBindings, "preBindings must not be null");

		List<Map<String, Object>> results = new ArrayList<>();

		// Primitive domain (§7.11.3.10): no patterns, root variable already
		// bound from relation call arguments. Just pass through with bindings.
		if (domain.getPattern().isEmpty()) {
			if (!domain.getRootVariable().isEmpty()) {
				Variable rootVar = domain.getRootVariable().get(0);
				if (preBindings.containsKey(rootVar.getName())) {
					results.add(new HashMap<>(preBindings));
				}
			}
			return results;
		}

		for (DomainPattern pattern : domain.getPattern()) {
			TemplateExp templateExp = pattern.getTemplateExpression();
			if (!(templateExp instanceof ObjectTemplateExp objectTemplate)) {
				continue; // CollectionTemplateExp deferred
			}

			Variable rootVar = objectTemplate.getBindsTo();
			EClass rootType = objectTemplate.getReferredClass();

			if (rootVar == null || rootType == null) {
				continue;
			}

			// [?] optional multiplicity (Eclipse extension): stored on DomainPattern
			boolean isOptional = pattern.getEAnnotation("qvtr.optional") != null;

			// If root variable is already pre-bound, check that single candidate
			Object preBound = preBindings.get(rootVar.getName());
			if (preBound instanceof EObject preBoundObj) {
				if (rootType.isInstance(preBoundObj)) {
					Map<String, Object> bindings = new HashMap<>(preBindings);
					results.addAll(matchObjectTemplateAll(objectTemplate, preBoundObj, bindings));
				}
			} else if (preBound == null && preBindings.containsKey(rootVar.getName())) {
				// Pre-bound to null (from optional [?] relation call) → null-to-null match
				if (isOptional) {
					results.add(new HashMap<>(preBindings));
				}
			} else {
				// Collect all instances from the extent
				TypedModel typedModel = domain.getTypedModel();
				List<EObject> candidates = extentManager.allInstances(typedModel, rootType);

				for (EObject candidate : candidates) {
					Map<String, Object> bindings = new HashMap<>(preBindings);
					bindings.put(rootVar.getName(), candidate);
					results.addAll(matchObjectTemplateAll(objectTemplate, candidate, bindings));
				}

				// [?] optional: if no matches found, bind root variable to null
				if (results.isEmpty() && isOptional) {
					Map<String, Object> nullBindings = new HashMap<>(preBindings);
					nullBindings.put(rootVar.getName(), null);
					results.add(nullBindings);
				}
			}
		}

		return results;
	}

	/**
	 * Matches an ObjectTemplateExp against a concrete EObject, populating the
	 * binding map with variable bindings from PropertyTemplateItems.
	 *
	 * @param template the object template expression
	 * @param object the model element to match against
	 * @param bindings the mutable binding map (updated in-place)
	 * @return {@code true} if the match succeeds
	 */
	boolean matchObjectTemplate(ObjectTemplateExp template, EObject object,
			Map<String, Object> bindings) {
		List<Map<String, Object>> all = matchObjectTemplateAll(template, object, bindings);
		if (!all.isEmpty()) {
			bindings.putAll(all.get(0));
			return true;
		}
		return false;
	}

	/**
	 * Matches an ObjectTemplateExp against a concrete EObject, returning ALL
	 * valid binding sets. Multi-valued references with nested templates produce
	 * one binding set per matching collection element (§7.10.3).
	 */
	List<Map<String, Object>> matchObjectTemplateAll(ObjectTemplateExp template,
			EObject object, Map<String, Object> initialBindings) {
		// Bind the template's variable to the object
		Variable bindVar = template.getBindsTo();
		if (bindVar != null) {
			String varName = bindVar.getName();
			Object existing = initialBindings.get(varName);
			if (existing != null && existing != object) {
				return List.of(); // Binding conflict
			}
			initialBindings.put(varName, object);
		}

		// Process each PropertyTemplateItem, accumulating binding sets.
		// For collection properties with nested templates, each matching element
		// produces a separate binding set (cross-product).
		List<Map<String, Object>> currentSets = new ArrayList<>();
		currentSets.add(initialBindings);

		for (PropertyTemplateItem item : template.getPart()) {
			List<Map<String, Object>> nextSets = new ArrayList<>();
			for (Map<String, Object> bs : currentSets) {
				nextSets.addAll(matchPropertyItemAll(item, object, bs));
			}
			currentSets = nextSets;
			if (currentSets.isEmpty()) {
				return List.of();
			}
		}

		// Filter by template-level where predicate
		OclExpression whereExpr = template.getWhere();
		if (whereExpr != null) {
			currentSets.removeIf(bs ->
					!Boolean.TRUE.equals(evaluateOcl(whereExpr, object, bs)));
		}

		return currentSets;
	}

	/**
	 * Matches a single PropertyTemplateItem, returning all valid binding sets.
	 * For multi-valued references with nested templates, produces one binding
	 * set per matching collection element.
	 */
	private List<Map<String, Object>> matchPropertyItemAll(PropertyTemplateItem item,
			EObject object, Map<String, Object> bindings) {
		EStructuralFeature feature = resolveFeature(item, object);
		if (feature == null) {
			return List.of();
		}

		Object actualValue = object.eGet(feature);
		OclExpression valueExpr = item.getValue();

		// Nested ObjectTemplateExp → recursive match
		if (valueExpr instanceof ObjectTemplateExp nestedTemplate) {
			if (actualValue instanceof EObject actualObj) {
				Map<String, Object> copy = new HashMap<>(bindings);
				List<Map<String, Object>> nested = matchObjectTemplateAll(
						nestedTemplate, actualObj, copy);
				return nested;
			}
			// Multi-valued reference → match each collection element separately
			if (actualValue instanceof Collection<?> collection && !collection.isEmpty()) {
				return matchNestedInCollectionAll(nestedTemplate, collection, bindings);
			}
			return List.of();
		}

		// CollectionTemplateExp → match collection pattern (§7.11.2.3)
		if (valueExpr instanceof CollectionTemplateExp collTemplate) {
			if (actualValue instanceof Collection<?> collection) {
				return matchCollectionTemplate(collTemplate, collection, bindings);
			}
			return List.of();
		}

		// VariableExp → bind or compare
		if (isVariableExpression(valueExpr)) {
			String varName = getVariableName(valueExpr);
			if (varName != null) {
				Object existing = bindings.get(varName);
				if (existing != null) {
					return valuesEqual(existing, actualValue)
							? List.of(bindings) : List.of();
				}
				bindings.put(varName, actualValue);
				return List.of(bindings);
			}
		}

		// OCL expression → evaluate and compare
		Object expectedValue = evaluateOcl(valueExpr, object, bindings);
		return valuesEqual(actualValue, expectedValue)
				? List.of(bindings) : List.of();
	}

	/**
	 * Resolves the actual EStructuralFeature from the object's EClass.
	 * The parser may create placeholder features (name only); this method
	 * resolves them against the runtime EClass.
	 *
	 * <p>For opposite properties (§7.11.2.4), the referredProperty belongs to
	 * the OTHER class. We resolve by finding the eOpposite on the current object's class.
	 */
	private EStructuralFeature resolveFeature(PropertyTemplateItem item, EObject object) {
		EStructuralFeature feature = item.getReferredProperty();
		if (feature == null) {
			return null;
		}

		EClass objectClass = object.eClass();

		// Opposite property (§7.11.2.4): referredProperty belongs to the other class.
		// Navigate via eOpposite to get the feature on the current object's class.
		if (item.isIsOpposite()) {
			return resolveOppositeFeature(feature, objectClass);
		}

		// If the feature belongs to the object's EClass hierarchy, use it directly
		if (objectClass.getEAllStructuralFeatures().contains(feature)) {
			return feature;
		}
		// Otherwise resolve by name (parser placeholder)
		return objectClass.getEStructuralFeature(feature.getName());
	}

	/**
	 * Resolves an opposite property by finding the eOpposite of the referred feature
	 * on the current object's class (§7.11.2.4).
	 */
	private EStructuralFeature resolveOppositeFeature(EStructuralFeature feature, EClass objectClass) {
		// First try: the feature itself might have an eOpposite we can use
		if (feature instanceof EReference ref && ref.getEOpposite() != null) {
			EReference opposite = ref.getEOpposite();
			if (objectClass.getEAllStructuralFeatures().contains(opposite)) {
				return opposite;
			}
			// Resolve by name if placeholder
			return objectClass.getEStructuralFeature(opposite.getName());
		}

		// Fallback: search the object's class for a reference whose eOpposite
		// has the same name as the referred feature
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
	 * Matches a nested ObjectTemplateExp against each element in a collection,
	 * returning one binding set per matching element (§7.10.3).
	 */
	private List<Map<String, Object>> matchNestedInCollectionAll(ObjectTemplateExp template,
			Collection<?> collection, Map<String, Object> bindings) {
		List<Map<String, Object>> results = new ArrayList<>();
		for (Object element : collection) {
			if (element instanceof EObject eo) {
				EClass referredClass = template.getReferredClass();
				if (referredClass != null && referredClass.isInstance(eo)) {
					Map<String, Object> tentative = new HashMap<>(bindings);
					List<Map<String, Object>> nested = matchObjectTemplateAll(
							template, eo, tentative);
					results.addAll(nested);
				}
			}
		}
		return results;
	}

	/**
	 * Matches a CollectionTemplateExp against a collection value (§7.11.2.3).
	 *
	 * <p>Each member expression must match at least one element in the collection.
	 * For ObjectTemplateExp members, each matching element produces a separate
	 * binding set (like nested template matching). The rest variable captures
	 * remaining elements. {@code _} wildcards match any element.
	 */
	private List<Map<String, Object>> matchCollectionTemplate(CollectionTemplateExp template,
			Collection<?> collection, Map<String, Object> bindings) {
		// Bind the template's variable to the collection itself
		Variable bindVar = template.getBindsTo();
		if (bindVar != null && !"_".equals(bindVar.getName())) {
			bindings.put(bindVar.getName(), new ArrayList<>(collection));
		}

		List<OclExpression> members = template.getMember();

		if (members.isEmpty()) {
			// No members → match depends on rest variable
			Variable rest = template.getRest();
			if (rest == null) {
				// No rest, no members → must be empty collection
				return collection.isEmpty() ? List.of(bindings) : List.of();
			}
			// Has rest → bind rest to entire collection
			if (!"_".equals(rest.getName())) {
				bindings.put(rest.getName(), new ArrayList<>(collection));
			}
			return List.of(bindings);
		}

		// Match each member expression against collection elements.
		// Each member must find at least one match. ObjectTemplateExp members
		// produce one binding set per matching element (cross-product).
		List<Map<String, Object>> currentSets = new ArrayList<>();
		currentSets.add(bindings);

		for (OclExpression memberExpr : members) {
			List<Map<String, Object>> nextSets = new ArrayList<>();

			if (memberExpr instanceof ObjectTemplateExp memberTemplate) {
				// Match member template against each collection element
				for (Map<String, Object> bs : currentSets) {
					for (Object element : collection) {
						if (element instanceof EObject eo) {
							EClass referredClass = memberTemplate.getReferredClass();
							if (referredClass != null && referredClass.isInstance(eo)) {
								Map<String, Object> copy = new HashMap<>(bs);
								List<Map<String, Object>> nested = matchObjectTemplateAll(
										memberTemplate, eo, copy);
								nextSets.addAll(nested);
							}
						}
					}
				}
			} else if (isVariableExpression(memberExpr)) {
				String varName = getVariableName(memberExpr);
				if ("_".equals(varName)) {
					// Wildcard: matches any single element — require non-empty collection
					if (!collection.isEmpty()) {
						nextSets.addAll(currentSets);
					}
				} else if (varName != null) {
					// Named variable: bind to each element or compare
					for (Map<String, Object> bs : currentSets) {
						for (Object element : collection) {
							Object existing = bs.get(varName);
							if (existing != null) {
								if (valuesEqual(existing, element)) {
									nextSets.add(bs);
								}
							} else {
								Map<String, Object> copy = new HashMap<>(bs);
								copy.put(varName, element);
								nextSets.add(copy);
							}
						}
					}
				}
			} else {
				// OCL expression: evaluate and check containment
				for (Map<String, Object> bs : currentSets) {
					Object expectedValue = evaluateOcl(memberExpr, null, bs);
					if (collection.contains(expectedValue)) {
						nextSets.add(bs);
					}
				}
			}

			currentSets = nextSets;
			if (currentSets.isEmpty()) {
				return List.of();
			}
		}

		// Bind rest variable to remaining elements (§7.11.2.3)
		Variable rest = template.getRest();
		if (rest != null && !"_".equals(rest.getName())) {
			for (Map<String, Object> bs : currentSets) {
				// Compute remaining: collection minus matched member elements
				List<Object> remaining = new ArrayList<>(collection);
				for (OclExpression memberExpr : members) {
					if (memberExpr instanceof ObjectTemplateExp memberTemplate) {
						Variable memberVar = memberTemplate.getBindsTo();
						if (memberVar != null) {
							Object matched = bs.get(memberVar.getName());
							if (matched != null) {
								remaining.remove(matched);
							}
						}
					}
				}
				bs.put(rest.getName(), remaining);
			}
		} else if (rest == null) {
			// No rest variable → only exact matches (members account for all elements)
			int memberCount = members.size();
			if (collection.size() != memberCount) {
				return List.of();
			}
		}

		return currentSets;
	}

	/**
	 * Evaluates an OCL expression in the context of a model object and current bindings.
	 */
	Object evaluateOcl(OclExpression expression, EObject contextObject,
			Map<String, Object> bindings) {
		OclContext ctx = new OclContext(contextObject, null, bindings);
		return oclEngine.evaluate(expression, ctx);
	}

	private boolean isVariableExpression(OclExpression expr) {
		return expr instanceof org.eclipse.fennec.m2x.model.ocl.VariableExp;
	}

	private String getVariableName(OclExpression expr) {
		if (expr instanceof org.eclipse.fennec.m2x.model.ocl.VariableExp varExp) {
			Variable ref = varExp.getReferredVariable();
			return ref != null ? ref.getName() : null;
		}
		return null;
	}

	private boolean valuesEqual(Object actual, Object expected) {
		if (actual == expected) {
			return true;
		}
		if (actual == null || expected == null) {
			return false;
		}
		return actual.equals(expected);
	}
}
