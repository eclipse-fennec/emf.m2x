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
package org.eclipse.fennec.m2m.qvto.engine.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;

/**
 * Manages tag "alias" declarations (§8.3.19).
 *
 * <p>Maps alias names to (EClass, real feature name) pairs for property resolution.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoAliasRegistry {

	private final OperationalTransformation transformation;
	private final QvtoExtentManager extentManager;
	private final Map<String, Map.Entry<EClass, String>> aliasRegistry = new HashMap<>();

	QvtoAliasRegistry(OperationalTransformation transformation,
			QvtoExtentManager extentManager) {
		this.transformation = Objects.requireNonNull(transformation);
		this.extentManager = Objects.requireNonNull(extentManager);
	}

	/**
	 * §8.3.19: Builds the alias registry from tag "alias" declarations.
	 * Maps alias name → (EClass, real feature name) for property resolution.
	 */
	void build() {
		for (EAnnotation tag : transformation.getOwnedTag()) {
			if (!"alias".equals(tag.getSource())) {
				continue;
			}
			String target = tag.getDetails().get("target");
			String aliasValue = tag.getDetails().get("value");
			if (target == null || aliasValue == null) {
				continue;
			}
			String aliasName = aliasValue;
			// Parse target path: e.g. "ecore::EPackage::name" → class=EPackage, feature=name
			String[] parts = target.split("::");
			if (parts.length >= 2) {
				String className = parts[parts.length - 2];
				String featureName = parts[parts.length - 1];
				// Find the EClass in the transformation's used metamodels
				EClass eClass = findEClassByName(className);
				if (eClass != null && eClass.getEStructuralFeature(featureName) != null) {
					aliasRegistry.put(aliasName, Map.entry(eClass, featureName));
				}
			}
		}
	}

	/**
	 * §8.3.19: Resolves a tag "alias" name to the real feature name for the given EObject.
	 * @return the real feature name, or null if not an alias
	 */
	String resolveAlias(String name, EObject target) {
		var entry = aliasRegistry.get(name);
		if (entry != null && entry.getKey().isInstance(target)) {
			return entry.getValue();
		}
		return null;
	}

	private EClass findEClassByName(String name) {
		// Search through all EPackages known to the extent manager
		for (int i = 0; i < transformation.getModelParameter().size(); i++) {
			QvtoModelExtent extent = extentManager.getExtent(i);
			if (extent == null) {
				continue;
			}
			for (EObject obj : extent.getContents()) {
				EClass ec = obj.eClass();
				if (name.equals(ec.getName())) {
					return ec;
				}
				// Check the package for the class
				EClass found = findEClassInPackage(ec.getEPackage(), name);
				if (found != null) {
					return found;
				}
			}
		}
		// Fallback: search EPackage.Registry
		for (Object value : EPackage.Registry.INSTANCE.values()) {
			if (value instanceof EPackage pkg) {
				EClass found = findEClassInPackage(pkg, name);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}

	private static EClass findEClassInPackage(EPackage pkg, String name) {
		for (var classifier : pkg.getEClassifiers()) {
			if (classifier instanceof EClass ec && name.equals(ec.getName())) {
				return ec;
			}
		}
		for (EPackage sub : pkg.getESubpackages()) {
			EClass found = findEClassInPackage(sub, name);
			if (found != null) {
				return found;
			}
		}
		return null;
	}
}
