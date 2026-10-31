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
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtbase.Predicate;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern;
import org.eclipse.fennec.m2x.model.qvtrelation.Key;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationCallExp;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomainAssignment;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem;
import org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp;
import org.eclipse.fennec.m2x.ocl.api.OclValues;

/**
 * Handles target domain enforcement for QVT-R transformations (§7.10.2).
 *
 * <p>Extracted from {@link QvtrEvaluator} for testability and separation
 * of concerns. Responsible for:
 * <ul>
 *   <li>Creating target objects from template patterns</li>
 *   <li>Key-based identity lookup (§7.4)</li>
 *   <li>In-place variable-based identity (§7.7)</li>
 *   <li>Property enforcement on target objects</li>
 *   <li>Feature resolution including opposite properties (§7.11.2.4)</li>
 * </ul>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtrEnforcer {

	private static final String SOURCE_ID = "org.eclipse.fennec.m2x.qvtd.engine";

	private final QvtrExtentManager extentManager;
	private final QvtrPatternMatcher patternMatcher;
	private final RelationalTransformation transformation;
	private final QvtrDiagnosticSink diagnostics;
	private final QvtrOclCallback oclCallback;
	private final QvtrBlackboxBridge blackboxBridge;
	/**
	 * What this run assigned to single-valued features, and which relation did: a different value
	 * for the same feature of the same object is a clash (§7.10.2). Values the object had before
	 * the run are no clash — enforcing modifies them (#351).
	 */
	private final Map<EObject, Map<EStructuralFeature, Assignment>> assigned = new IdentityHashMap<>();
	/** The objects created while enforcing the current domain. */
	private final List<EObject> created = new ArrayList<>();
	/** The relation whose domain is being enforced, for the clash report. */
	private Relation enforcing;

	private record Assignment(Object value, String relation) {
	}

	public QvtrEnforcer(QvtrExtentManager extentManager, QvtrPatternMatcher patternMatcher,
			RelationalTransformation transformation, QvtrDiagnosticSink diagnostics,
			QvtrOclCallback oclCallback, QvtrBlackboxBridge blackboxBridge) {
		this.extentManager = extentManager;
		this.patternMatcher = patternMatcher;
		this.transformation = transformation;
		this.diagnostics = diagnostics;
		this.oclCallback = oclCallback;
		this.blackboxBridge = blackboxBridge;
	}

	/**
	 * Enforces a target domain by matching or creating target objects (§7.10.2).
	 */
	public void enforceTargetDomain(Relation relation, RelationDomain targetDomain,
			Map<String, Object> sourceBindings) {
		// First try to match the target domain with existing elements: the first match that also
		// satisfies the where condition (§7.10.2 "satisfies domain k's pattern and where
		// condition", #351)
		for (Map<String, Object> match : patternMatcher.matchDomain(targetDomain, sourceBindings)) {
			if (whereAllows(relation, match)) {
				sourceBindings.putAll(match);
				return;
			}
		}

		// Apply default value assignments (§7.11.3.7) for unbound variables
		for (RelationDomainAssignment assignment : targetDomain.getDefaultAssignment()) {
			Variable var = assignment.getVariable();
			if (var != null && !sourceBindings.containsKey(var.getName())) {
				Object value = oclCallback.evaluate(assignment.getValueExp(), sourceBindings);
				if (value != null) {
					sourceBindings.put(var.getName(), value);
				}
			}
		}

		// implementedby clause (§7.11.3.6): the relation does not hold in this direction — the
		// match above found nothing — so the operation the clause names is invoked, and the
		// relation has to hold when it returns. "It is a runtime exception if the relation
		// evaluates to false after the operation returns"; before #282 nothing checked that.
		TypedModel targetModel = targetDomain.getTypedModel();
		switch (blackboxBridge.invokeImplementedBy(relation, targetModel, sourceBindings)) {
			case INVOKED -> {
				List<Map<String, Object>> afterwards = patternMatcher.matchDomain(targetDomain, sourceBindings);
				if (afterwards.isEmpty()) {
					diagnostics.error("Relation '" + relation.getName() + "' does not hold after its"
							+ " implementedby operation returned (§7.11.3.6)");
				} else {
					sourceBindings.putAll(afterwards.get(0));
				}
				return;
			}
			case FAILED -> {
				return;
			}
			case NONE -> {
				// no implementedby for this direction: QVT-R enforces the domain below
			}
		}

		// No match found → enforce: create target objects from template
		enforcing = relation;
		created.clear();
		for (DomainPattern pattern : targetDomain.getPattern()) {
			TemplateExp templateExp = pattern.getTemplateExpression();
			if (templateExp instanceof ObjectTemplateExp objectTemplate) {
				EObject root = enforceObjectTemplate(objectTemplate, sourceBindings, targetModel);
				if (root != null) {
					// Bind root variable
					Variable rootVar = objectTemplate.getBindsTo();
					if (rootVar != null) {
						sourceBindings.put(rootVar.getName(), root);
					}
				}
			}
		}
		// Every object created here that no container holds is a root of the target model — also
		// a Schema created by a nested template and holding the Table through the opposite of
		// Table.schema, which used to hang in no extent (#351)
		List<EObject> contents = extentManager.getExtent(targetModel).getContents();
		for (EObject object : created) {
			if (object.eContainer() == null && !contents.contains(object)) {
				extentManager.getExtent(targetModel).add(object);
			}
		}
		created.clear();
	}

	/**
	 * Whether the where condition allows a target match: each OCL predicate whose variables the
	 * match binds has to be true. Relation calls and predicates that read what is not bound yet
	 * are left to the where clause after enforcing.
	 */
	private boolean whereAllows(Relation relation, Map<String, Object> match) {
		if (relation.getWhere() == null) {
			return true;
		}
		for (Predicate predicate : relation.getWhere().getPredicate()) {
			OclExpression expr = predicate.getConditionExpression();
			if (expr == null || QvtrFreeVariables.callsRelation(expr)
					|| !match.keySet().containsAll(QvtrFreeVariables.of(expr))) {
				continue;
			}
			if (!Boolean.TRUE.equals(oclCallback.evaluate(expr, match))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Assigns a single-valued feature unless this run already gave it a different value: "It is
	 * an error, if the template expression evaluation results in an assignment of a value to a
	 * property that clashes with another value set for the same property by another rule in the
	 * transformation execution" (§7.10.2, #351).
	 */
	private void assign(EObject target, EStructuralFeature feature, Object value) {
		Map<EStructuralFeature, Assignment> byFeature = assigned.computeIfAbsent(target, t -> new HashMap<>());
		Assignment previous = byFeature.get(feature);
		String relation = enforcing != null ? enforcing.getName() : "?";
		if (previous != null && !Objects.equals(previous.value(), value)) {
			diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE_ID, 0,
					"Relation '%s' assigns %s to '%s' of %s, which relation '%s' set to %s in this run — the values clash (§7.10.2)"
							.formatted(relation, show(value), feature.getName(), show(target), previous.relation(),
									show(previous.value())), null));
			return;
		}
		target.eSet(feature, value);
		byFeature.put(feature, new Assignment(value, relation));
	}

	private static String show(Object value) {
		if (value instanceof EObject eo) {
			EStructuralFeature name = eo.eClass().getEStructuralFeature("name");
			Object n = name != null ? eo.eGet(name) : null;
			return eo.eClass().getName() + (n != null ? " '" + n + "'" : "");
		}
		return value instanceof String str ? "'" + str + "'" : String.valueOf(value);
	}

	/**
	 * Creates or finds an EObject by instantiating an ObjectTemplateExp pattern (§7.10.2).
	 * Uses Key-based identity (§7.4) to find existing objects before creating new ones.
	 */
	public EObject enforceObjectTemplate(ObjectTemplateExp template,
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
				enforceProperties(template, existingObj, bindings, targetModel);
				return existingObj;
			}
		}

		// Key-based identity (§7.4): look for existing object matching key properties. Without
		// a key that locates one, a new object is created — object identity comes only from keys
		// (§7.10.2); the selection by bound variables that took over foreign objects is gone (#350)
		Object keyMatch = findByKey(template, bindings, eClass, targetModel);
		if (keyMatch == KEY_UNDETERMINED) {
			return null;
		}
		if (keyMatch instanceof EObject found) {
			if (bindVar != null) {
				bindings.put(bindVar.getName(), found);
			}
			enforceProperties(template, found, bindings, targetModel);
			return found;
		}

		// Create new instance
		EObject fresh = EcoreUtil.create(eClass);
		created.add(fresh);
		if (bindVar != null) {
			bindings.put(bindVar.getName(), fresh);
		}
		enforceProperties(template, fresh, bindings, targetModel);
		return fresh;
	}

	/**
	 * Sets properties on a target object according to the template's PropertyTemplateItems.
	 */
	@SuppressWarnings("unchecked")
	public void enforceProperties(ObjectTemplateExp template, EObject target,
			Map<String, Object> bindings, TypedModel targetModel) {
		for (PropertyTemplateItem item : template.getPart()) {
			EStructuralFeature feature = resolveFeature(item, target);
			if (feature == null) {
				enforceNonNavigableOpposite(item, target, bindings, targetModel);
				continue;
			}

			OclExpression valueExpr = item.getValue();

			// Nested ObjectTemplateExp → recursive enforce
			if (valueExpr instanceof ObjectTemplateExp nestedTemplate) {
				EObject nested = enforceObjectTemplate(nestedTemplate, bindings, targetModel);
				if (nested != null) {
					if (feature.isMany()) {
						List<EObject> held = (List<EObject>) target.eGet(feature);
						if (!held.contains(nested)) {
							held.add(nested);
						}
					} else {
						assign(target, feature, nested);
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
				value = oclCallback.evaluate(valueExpr, bindings);
			}
			if (value != null) {
				// OCL numbers are Long/BigInteger/BigDecimal; the feature wants its instance class (#322)
				value = OclValues.toEcore(value, feature.getEType());
				if (feature.isMany()) {
					((List<Object>) target.eGet(feature)).add(value);
				} else {
					assign(target, feature, value);
				}
			}
		}
	}

	/**
	 * {@code opposite(C::p) = owner} where {@code p} has no eOpposite (§7.3): the owner's
	 * {@code p} holds the target afterwards (#355).
	 */
	@SuppressWarnings("unchecked")
	private void enforceNonNavigableOpposite(PropertyTemplateItem item, EObject target,
			Map<String, Object> bindings, TypedModel targetModel) {
		EStructuralFeature property = item.getReferredProperty();
		if (!item.isIsOpposite() || property == null) {
			return;
		}
		Object owner = item.getValue() instanceof ObjectTemplateExp nested
				? enforceObjectTemplate(nested, bindings, targetModel)
				: evaluatePropertyValue(item, bindings);
		if (!(owner instanceof EObject ownerObject)) {
			return;
		}
		EStructuralFeature feature = ownerObject.eClass().getEStructuralFeature(property.getName());
		if (feature == null) {
			return;
		}
		if (feature.isMany()) {
			List<Object> held = (List<Object>) ownerObject.eGet(feature);
			if (!held.contains(target)) {
				held.add(target);
			}
		} else {
			assign(ownerObject, feature, target);
		}
	}

	/**
	 * Enforces a CollectionTemplateExp on a target property (§7.11.2.3).
	 */
	@SuppressWarnings("unchecked")
	public void enforceCollectionTemplate(CollectionTemplateExp collTemplate,
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

	/** {@link #findByKey}: a key part's value could not be determined, an ERROR is reported. */
	static final Object KEY_UNDETERMINED = new Object();

	/**
	 * Finds an existing object in the target extent that one of the class's keys locates (§7.4):
	 * "A class may have multiple keys", and an object is found when one of them matches
	 * completely. A key applies when the template gives a value for each of its parts — for a
	 * part whose value is an object template, the object that template enforces; for an
	 * {@code opposite(C::p)} part, the object whose {@code p} holds the candidate (#350).
	 *
	 * @return the object, {@code null} when no key locates one, or {@link #KEY_UNDETERMINED}
	 */
	public Object findByKey(ObjectTemplateExp template, Map<String, Object> bindings,
			EClass eClass, TypedModel targetModel) {
		List<Key> keys = findKeys(eClass);
		if (keys.isEmpty()) {
			return null;
		}
		List<EObject> candidates = null;
		for (Key key : keys) {
			Map<EStructuralFeature, Object> values = new LinkedHashMap<>();
			Map<EStructuralFeature, EObject> owners = new LinkedHashMap<>();
			boolean applies = !key.getPart().isEmpty() || !key.getOppositePart().isEmpty();
			for (EStructuralFeature part : key.getPart()) {
				PropertyTemplateItem item = itemFor(template, part.getName(), false);
				if (item == null) {
					applies = false;
					break;
				}
				Object value = keyValue(item, bindings, targetModel);
				if (value == KEY_UNDETERMINED) {
					return keyUndetermined(eClass, part);
				}
				values.put(part, value);
			}
			for (EStructuralFeature part : key.getOppositePart()) {
				if (!applies) {
					break;
				}
				// opposite(Schema::ownedTable): the template names Schema::ownedTable as an
				// opposite, or the reverse end Table::schema directly
				PropertyTemplateItem item = itemFor(template, part.getName(), true);
				if (item == null && part instanceof EReference ref && ref.getEOpposite() != null) {
					item = itemFor(template, ref.getEOpposite().getName(), false);
				}
				if (item == null) {
					applies = false;
					break;
				}
				Object owner = keyValue(item, bindings, targetModel);
				if (owner == KEY_UNDETERMINED || !(owner instanceof EObject ownerObject)) {
					return keyUndetermined(eClass, part);
				}
				owners.put(part, ownerObject);
			}
			if (!applies) {
				continue;
			}
			if (candidates == null) {
				candidates = extentManager.allInstances(targetModel, eClass);
			}
			for (EObject candidate : candidates) {
				if (keyMatches(candidate, values, owners)) {
					return candidate;
				}
			}
		}
		return null;
	}

	private Object keyUndetermined(EClass eClass, EStructuralFeature part) {
		diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE_ID, 0,
				"Cannot determine the value of key part '%s' of %s, so the object to enforce cannot be located (§7.4)"
						.formatted(part.getName(), eClass.getName()), null));
		return KEY_UNDETERMINED;
	}

	@SuppressWarnings("unchecked")
	private static boolean keyMatches(EObject candidate, Map<EStructuralFeature, Object> values,
			Map<EStructuralFeature, EObject> owners) {
		for (Map.Entry<EStructuralFeature, Object> entry : values.entrySet()) {
			EStructuralFeature feature = candidate.eClass().getEStructuralFeature(entry.getKey().getName());
			if (feature == null) {
				return false;
			}
			Object expected = entry.getValue() == null ? null : OclValues.toEcore(entry.getValue(), feature.getEType());
			if (!Objects.equals(candidate.eGet(feature), expected)) {
				return false;
			}
		}
		for (Map.Entry<EStructuralFeature, EObject> entry : owners.entrySet()) {
			EObject owner = entry.getValue();
			EStructuralFeature feature = owner.eClass().getEStructuralFeature(entry.getKey().getName());
			if (feature == null) {
				return false;
			}
			Object held = owner.eGet(feature);
			boolean holds = feature.isMany() ? ((List<Object>) held).contains(candidate) : held == candidate;
			if (!holds) {
				return false;
			}
		}
		return true;
	}

	/** The template item for a property, by name; an opposite item or a plain one. */
	private static PropertyTemplateItem itemFor(ObjectTemplateExp template, String property, boolean opposite) {
		for (PropertyTemplateItem item : template.getPart()) {
			EStructuralFeature feature = item.getReferredProperty();
			if (feature != null && item.isIsOpposite() == opposite && property.equals(feature.getName())) {
				return item;
			}
		}
		return null;
	}

	/**
	 * A key part's value: for a nested object template the object it binds, or else the object
	 * enforcing it gives — "resolve the nested template first" (#350).
	 */
	private Object keyValue(PropertyTemplateItem item, Map<String, Object> bindings, TypedModel targetModel) {
		if (item.getValue() instanceof ObjectTemplateExp nested) {
			Variable var = nested.getBindsTo();
			if (var != null && bindings.get(var.getName()) instanceof EObject bound) {
				return bound;
			}
			EObject enforced = enforceObjectTemplate(nested, bindings, targetModel);
			return enforced != null ? enforced : KEY_UNDETERMINED;
		}
		return evaluatePropertyValue(item, bindings);
	}

	/**
	 * Evaluates the value of a PropertyTemplateItem for key comparison.
	 */
	public Object evaluatePropertyValue(PropertyTemplateItem item, Map<String, Object> bindings) {
		OclExpression valueExpr = item.getValue();
		if (valueExpr instanceof VariableExp varExp) {
			Variable ref = varExp.getReferredVariable();
			return ref != null ? bindings.get(ref.getName()) : null;
		}
		return oclCallback.evaluate(valueExpr, bindings);
	}

	/**
	 * Finds a Key declaration that applies to the given EClass (§7.4): the first of
	 * {@link #findKeys}.
	 */
	public Key findKey(EClass eClass) {
		List<Key> keys = findKeys(eClass);
		return keys.isEmpty() ? null : keys.get(0);
	}

	/**
	 * The keys that identify objects of the class (§7.4): those declared for the class itself or
	 * a superclass, compared by identity, not by name. Keys of the most specific classes take
	 * precedence — a superclass key applies only where no more specific class has one (decided
	 * in #350).
	 */
	public List<Key> findKeys(EClass eClass) {
		List<Key> applicable = new ArrayList<>();
		for (Key key : transformation.getOwnedKey()) {
			EClass identifies = key.getIdentifies();
			if (identifies != null && identifies.isSuperTypeOf(eClass)) {
				applicable.add(key);
			}
		}
		List<Key> mostSpecific = new ArrayList<>();
		for (Key key : applicable) {
			boolean shadowed = applicable.stream().anyMatch(other -> other.getIdentifies() != key.getIdentifies()
					&& key.getIdentifies().isSuperTypeOf(other.getIdentifies()));
			if (!shadowed) {
				mostSpecific.add(key);
			}
		}
		return mostSpecific;
	}

	/**
	 * Resolves the actual EStructuralFeature from the object's EClass.
	 */
	public EStructuralFeature resolveFeature(PropertyTemplateItem item, EObject object) {
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
	public EStructuralFeature resolveOppositeFeature(EStructuralFeature feature, EClass objectClass) {
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
}
