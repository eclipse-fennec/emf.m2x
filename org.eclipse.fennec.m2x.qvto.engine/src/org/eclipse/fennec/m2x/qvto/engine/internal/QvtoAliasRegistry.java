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
package org.eclipse.fennec.m2x.qvto.engine.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelType;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;

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
	private final EPackage.Registry packageRegistry;
	private final List<Diagnostic> diagnostics;

	QvtoAliasRegistry(OperationalTransformation transformation,
			QvtoExtentManager extentManager, EPackage.Registry packageRegistry,
			List<Diagnostic> diagnostics) {
		this.transformation = Objects.requireNonNull(transformation);
		this.extentManager = Objects.requireNonNull(extentManager);
		this.packageRegistry = Objects.requireNonNull(packageRegistry);
		this.diagnostics = Objects.requireNonNull(diagnostics);
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
			// §8.3.19 notation: tag "alias" RDBMS::Table::key_ = "key";
			// the path qualifies the class through its model type, so the qualifier is
			// what disambiguates two metamodels that both define a class of that name
			String[] parts = target.split("::");
			if (parts.length >= 2) {
				String className = parts[parts.length - 2];
				String featureName = parts[parts.length - 1];
				String qualifier = parts.length >= 3 ? parts[parts.length - 3] : null;
				EClass eClass = findEClass(qualifier, className);
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

	/**
	 * Resolves the class of an alias target.
	 *
	 * <p>The qualifier of the path — {@code RDBMS} in
	 * {@code tag "alias" RDBMS::Table::key_} — names a model type of the transformation
	 * (§8.3.19, §8.2.1.6). When it is present and known, only that model type's
	 * metamodels are searched, which is what makes the answer well defined when two
	 * metamodels declare a class of the same name. Without a usable qualifier the search
	 * falls back to the extents and the configured registry, and says so.
	 */
	private EClass findEClass(String qualifier, String name) {
		if (qualifier != null) {
			for (ModelType modelType : transformation.getUsedModelType()) {
				if (!qualifier.equals(modelType.getName())) {
					continue;
				}
				for (EPackage metamodel : modelType.getMetamodel()) {
					EClass found = findEClassInPackage(metamodel, name);
					if (found != null) {
						return found;
					}
				}
			}
			for (Object value : packageRegistry.values()) {
				if (value instanceof EPackage pkg
						&& (qualifier.equals(pkg.getName()) || qualifier.equals(pkg.getNsURI()))) {
					EClass found = findEClassInPackage(pkg, name);
					if (found != null) {
						return found;
					}
				}
			}
		}
		return findEClassByName(name);
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
		// Fallback: scan the registry the engine was configured with (D42). The name
		// alone may fit several metamodels, so an ambiguous hit is reported rather than
		// silently decided — Eclipse QVT-O warns in the same situation.
		EClass firstMatch = null;
		List<String> matchingPackages = new ArrayList<>();
		for (Object value : packageRegistry.values()) {
			if (value instanceof EPackage pkg) {
				EClass found = findEClassInPackage(pkg, name);
				if (found != null) {
					matchingPackages.add(pkg.getNsURI());
					if (firstMatch == null) {
						firstMatch = found;
					}
				}
			}
		}
		if (matchingPackages.size() > 1) {
			diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING,
					"org.eclipse.fennec.m2x.qvto.engine", 0,
					"Alias target class '" + name + "' is ambiguous — it matches "
							+ matchingPackages + "; the first was used. Qualify the tag"
							+ " target with its model type to make this unambiguous.",
					null));
		}
		return firstMatch;
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
