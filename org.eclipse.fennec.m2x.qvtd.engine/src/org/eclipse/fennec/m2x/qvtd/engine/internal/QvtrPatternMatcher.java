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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem;
import org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionException;

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

	private final OclEngine oclEngine;
	private final QvtrExtentManager extentManager;
	private final int maxBindings;

	public QvtrPatternMatcher(OclEngine oclEngine, QvtrExtentManager extentManager,
			int maxBindings) {
		this.oclEngine = Objects.requireNonNull(oclEngine, "oclEngine must not be null");
		this.extentManager = Objects.requireNonNull(extentManager, "extentManager must not be null");
		this.maxBindings = maxBindings;
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

		// Primitive domain (§7.13.4 primitive domain): no patterns, root variable already
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

		// A binding of the domain binds all its root variables at once (§7.11.3.3): the templates
		// of a domain are matched as a cross product, each pattern extending the bindings of the
		// ones before it — not as a union, which left each binding with one pattern's variables
		// (#355)
		List<Map<String, Object>> current = List.of(preBindings);
		for (DomainPattern pattern : domain.getPattern()) {
			List<Map<String, Object>> next = new ArrayList<>();
			for (Map<String, Object> bindings : current) {
				next.addAll(matchPattern(domain, pattern, bindings));
				checkBindingLimit(next);
			}
			current = next;
			if (current.isEmpty()) {
				break;
			}
		}
		results.addAll(current);
		return results;
	}

	/** Matches one domain pattern, extending the given bindings. */
	private List<Map<String, Object>> matchPattern(RelationDomain domain, DomainPattern pattern,
			Map<String, Object> preBindings) {
		List<Map<String, Object>> results = new ArrayList<>();
		TemplateExp templateExp = pattern.getTemplateExpression();
		if (!(templateExp instanceof ObjectTemplateExp objectTemplate)) {
			return results; // CollectionTemplateExp deferred
		}

		Variable rootVar = objectTemplate.getBindsTo();
		EClass rootType = objectTemplate.getReferredClass();

		if (rootVar == null || rootType == null) {
			return results;
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
			// A variable bound to null is bound: null is a value, and it is not this object (#355)
			if (initialBindings.containsKey(varName) && initialBindings.get(varName) != object) {
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
				checkBindingLimit(nextSets);
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
		Object actualValue;
		if (feature != null) {
			actualValue = object.eGet(feature);
		} else if (item.isIsOpposite() && item.getReferredProperty() != null
				&& item.getReferredProperty().getEContainingClass() != null) {
			// A non-navigable opposite role — the case §7.3 introduces opposite() for: the
			// objects of the class whose property holds this object (#355)
			List<EObject> owners = ownersOf(object, item.getReferredProperty());
			if (owners.isEmpty()) {
				return List.of();
			}
			actualValue = owners.size() == 1 ? owners.get(0) : owners;
		} else {
			return List.of();
		}
		OclExpression valueExpr = item.getValue();

		// Nested ObjectTemplateExp → recursive match
		if (valueExpr instanceof ObjectTemplateExp nestedTemplate) {
			if (actualValue instanceof EObject actualObj) {
				// "o2 is of type <classname2> or one of its subtypes" (§7.10.3.2, #355)
				if (nestedTemplate.getReferredClass() != null
						&& !nestedTemplate.getReferredClass().isInstance(actualObj)) {
					return List.of();
				}
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

		// CollectionTemplateExp → match collection pattern (§7.11.2.3); "If the value expression
		// is a collection template expression and the referred property is single-valued, then
		// the property value is treated as a singleton collection" (§7.11.2.4, #354)
		if (valueExpr instanceof CollectionTemplateExp collTemplate) {
			Collection<?> collection = actualValue instanceof Collection<?> values ? values
					: actualValue == null ? List.of() : List.of(actualValue);
			return matchCollectionTemplate(collTemplate, collection, bindings);
		}

		// VariableExp → bind or compare
		if (valueExpr instanceof VariableExp ve
				&& ve.getReferredVariable() != null) {
			String varName = ve.getReferredVariable().getName();
			if (varName != null) {
				if (bindings.containsKey(varName)) {
					return valuesEqual(bindings.get(varName), actualValue)
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
	 * The objects whose property holds the object: {@code opposite(C::p)} without an eOpposite,
	 * "equivalent to … {c.attribute.includes(a)}" (§7.3). Looked for in the object's own model.
	 */
	@SuppressWarnings("unchecked")
	private List<EObject> ownersOf(EObject object, EStructuralFeature property) {
		List<EObject> owners = new ArrayList<>();
		for (EObject candidate : extentManager.instancesBeside(object, property.getEContainingClass())) {
			EStructuralFeature feature = candidate.eClass().getEStructuralFeature(property.getName());
			if (feature == null) {
				continue;
			}
			Object held = candidate.eGet(feature);
			if (feature.isMany() ? ((List<Object>) held).contains(object) : held == object) {
				owners.add(candidate);
			}
		}
		return owners;
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
	 * Matches a collection template against the values of a property (§7.11.2.3): the members
	 * match distinct elements — each one, a variable, an object template or {@code _}, consumes
	 * an element — and the rest the elements left over; without a rest nothing may be left. For
	 * a Sequence or OrderedSet "the matching member elements must be present at the head of the
	 * sequence in the order specified by the member expressions", and for a Set or OrderedSet "a
	 * valid match requires that each value of the property be unique" (§7.11.2.4). The template's
	 * where condition filters the bindings (§7.11.2.1). Before #354 the members could bind the
	 * same element, variable members stayed in the rest, and kind and where were ignored.
	 */
	private List<Map<String, Object>> matchCollectionTemplate(CollectionTemplateExp template,
			Collection<?> collection, Map<String, Object> bindings) {
		CollectionKind kind = template.getReferredCollectionType() instanceof CollectionType type
				? type.getKind() : CollectionKind.SET;
		List<Object> elements = new ArrayList<>(collection);
		boolean unique = kind == CollectionKind.SET || kind == CollectionKind.ORDERED_SET;
		if (unique && new HashSet<>(elements).size() != elements.size()) {
			return List.of();
		}
		boolean ordered = kind == CollectionKind.SEQUENCE || kind == CollectionKind.ORDERED_SET;

		Map<String, Object> start = new HashMap<>(bindings);
		Variable bindVar = template.getBindsTo();
		if (bindVar != null && !"_".equals(bindVar.getName())) {
			if (start.containsKey(bindVar.getName())
					&& !sameElements(start.get(bindVar.getName()), elements, ordered)) {
				return List.of();
			}
			start.put(bindVar.getName(), new ArrayList<>(elements));
		}

		List<Map<String, Object>> results = new ArrayList<>();
		matchMembers(template, elements, ordered, 0, new boolean[elements.size()], start, results);

		OclExpression where = template.getWhere();
		if (where != null) {
			results.removeIf(bs -> !Boolean.TRUE.equals(evaluateOcl(where, null, bs)));
		}
		return results;
	}

	/** Assigns the members from {@code index} on to unconsumed elements, then binds the rest. */
	private void matchMembers(CollectionTemplateExp template, List<Object> elements, boolean ordered,
			int index, boolean[] consumed, Map<String, Object> bindings, List<Map<String, Object>> results) {
		List<OclExpression> members = template.getMember();
		if (index == members.size()) {
			bindRest(template, elements, consumed, ordered, bindings, results);
			return;
		}
		OclExpression member = members.get(index);
		for (int i = 0; i < elements.size(); i++) {
			// In a Sequence or OrderedSet the i-th member is the i-th element
			if (consumed[i] || (ordered && i != index)) {
				continue;
			}
			for (Map<String, Object> extended : matchMember(member, elements.get(i), bindings)) {
				consumed[i] = true;
				matchMembers(template, elements, ordered, index + 1, consumed, extended, results);
				consumed[i] = false;
			}
		}
	}

	private void bindRest(CollectionTemplateExp template, List<Object> elements, boolean[] consumed,
			boolean ordered, Map<String, Object> bindings, List<Map<String, Object>> results) {
		List<Object> remaining = new ArrayList<>();
		for (int i = 0; i < elements.size(); i++) {
			if (!consumed[i]) {
				remaining.add(elements.get(i));
			}
		}
		Variable rest = template.getRest();
		if (rest == null) {
			// "The variable may be omitted to restrict a match to a collection with no elements
			// other than those matched by the member expressions"
			if (remaining.isEmpty()) {
				results.add(bindings);
			}
		} else if ("_".equals(rest.getName())) {
			results.add(bindings);
		} else if (bindings.containsKey(rest.getName())) {
			if (sameElements(bindings.get(rest.getName()), remaining, ordered)) {
				results.add(bindings);
			}
		} else {
			Map<String, Object> withRest = new HashMap<>(bindings);
			withRest.put(rest.getName(), remaining);
			results.add(withRest);
		}
		checkBindingLimit(results);
	}

	/** The bindings under which a member matches one element. */
	private List<Map<String, Object>> matchMember(OclExpression member, Object element,
			Map<String, Object> bindings) {
		if (member instanceof ObjectTemplateExp memberTemplate) {
			EClass referredClass = memberTemplate.getReferredClass();
			if (element instanceof EObject eo && (referredClass == null || referredClass.isInstance(eo))) {
				return matchObjectTemplateAll(memberTemplate, eo, new HashMap<>(bindings));
			}
			return List.of();
		}
		if (member instanceof VariableExp memberVe && memberVe.getReferredVariable() != null) {
			String varName = memberVe.getReferredVariable().getName();
			if ("_".equals(varName)) {
				return List.of(bindings);
			}
			if (bindings.containsKey(varName)) {
				return valuesEqual(bindings.get(varName), element) ? List.of(bindings) : List.of();
			}
			Map<String, Object> copy = new HashMap<>(bindings);
			copy.put(varName, element);
			return List.of(copy);
		}
		return valuesEqual(evaluateOcl(member, null, bindings), element) ? List.of(bindings) : List.of();
	}

	/** Whether a bound collection holds the same elements — in the same order for an ordered kind. */
	private static boolean sameElements(Object bound, List<Object> elements, boolean ordered) {
		if (!(bound instanceof Collection<?> collection)) {
			return false;
		}
		if (ordered) {
			return new ArrayList<>(collection).equals(elements);
		}
		Map<Object, Integer> counts = new HashMap<>();
		collection.forEach(e -> counts.merge(e, 1, Integer::sum));
		elements.forEach(e -> counts.merge(e, -1, Integer::sum));
		return counts.values().stream().allMatch(c -> c == 0);
	}

	/**
	 * Evaluates an OCL expression in the context of a model object and current bindings.
	 */
	Object evaluateOcl(OclExpression expression, EObject contextObject,
			Map<String, Object> bindings) {
		OclContext ctx = new OclContext(contextObject, null, bindings);
		return oclEngine.evaluate(expression, ctx);
	}

	/**
	 * Checks if the number of binding sets exceeds the configured limit (M-R3).
	 *
	 * @throws QvtdExecutionException if the limit is exceeded
	 */
	private void checkBindingLimit(List<?> bindings) {
		if (bindings.size() > maxBindings) {
			throw new QvtdExecutionException(
					"Pattern matching binding limit exceeded (%d) — possible cross-product explosion"
							.formatted(maxBindings));
		}
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
