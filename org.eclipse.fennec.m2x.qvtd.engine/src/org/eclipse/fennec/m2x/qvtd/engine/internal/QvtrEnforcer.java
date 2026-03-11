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

import java.util.HashMap;
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
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern;
import org.eclipse.fennec.m2x.model.qvtrelation.Key;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomainAssignment;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem;
import org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp;

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
	private final List<Diagnostic> diagnostics;
	private final QvtrOclCallback oclCallback;
	private final QvtrBlackboxBridge blackboxBridge;

	public QvtrEnforcer(QvtrExtentManager extentManager, QvtrPatternMatcher patternMatcher,
			RelationalTransformation transformation, List<Diagnostic> diagnostics,
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
				Object value = oclCallback.evaluate(assignment.getValueExp(), sourceBindings);
				if (value != null) {
					sourceBindings.put(var.getName(), value);
				}
			}
		}

		// implementedby clause (§7.11.3.6): delegate to operational implementation
		TypedModel targetModel = targetDomain.getTypedModel();
		if (blackboxBridge.invokeImplementedBy(relation, targetModel, sourceBindings)) {
			return;
		}

		// No match found → enforce: create target objects from template
		for (DomainPattern pattern : targetDomain.getPattern()) {
			TemplateExp templateExp = pattern.getTemplateExpression();
			if (templateExp instanceof ObjectTemplateExp objectTemplate) {
				EObject created = enforceObjectTemplate(objectTemplate, sourceBindings, targetModel);
				if (created != null) {
					// Add created object to the target extent only if it's a new root
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

		// Key-based identity (§7.4): look for existing object matching key properties
		EObject keyMatch = findByKey(template, bindings, eClass, targetModel);
		if (keyMatch != null) {
			if (bindVar != null) {
				bindings.put(bindVar.getName(), keyMatch);
			}
			enforceProperties(template, keyMatch, bindings, targetModel);
			return keyMatch;
		}

		// In-place / variable-based identity (§7.7)
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
		if (bindVar != null) {
			bindings.put(bindVar.getName(), created);
		}
		enforceProperties(template, created, bindings, targetModel);
		return created;
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
				value = oclCallback.evaluate(valueExpr, bindings);
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

	/**
	 * Finds an existing object in the target extent that matches the Key properties (§7.4).
	 */
	public EObject findByKey(ObjectTemplateExp template, Map<String, Object> bindings,
			EClass eClass, TypedModel targetModel) {
		Key key = findKey(eClass);
		if (key == null || key.getPart().isEmpty()) {
			return null;
		}

		// Compute expected key values from the template's PropertyTemplateItems
		Map<String, Object> keyValues = new HashMap<>();
		for (EStructuralFeature keyPart : key.getPart()) {
			String keyPropName = keyPart.getName();
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
	 */
	public EObject findByBoundVariables(ObjectTemplateExp template,
			Map<String, Object> bindings, EClass eClass, TypedModel targetModel) {
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
	public Object evaluatePropertyValue(PropertyTemplateItem item, Map<String, Object> bindings) {
		OclExpression valueExpr = item.getValue();
		if (valueExpr instanceof VariableExp varExp) {
			Variable ref = varExp.getReferredVariable();
			return ref != null ? bindings.get(ref.getName()) : null;
		}
		return oclCallback.evaluate(valueExpr, bindings);
	}

	/**
	 * Finds the Key declaration for the given EClass (§7.4).
	 */
	public Key findKey(EClass eClass) {
		for (Key key : transformation.getOwnedKey()) {
			EClass identifies = key.getIdentifies();
			if (identifies != null && identifies.getName().equals(eClass.getName())) {
				return key;
			}
		}
		return null;
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
