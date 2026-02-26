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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;

/**
 * Manages predefined tags {@code proxy} and {@code topclasses} (§8.3.19).
 *
 * <p>{@code proxy}: marks a module as a placeholder that must be resolved by the linker.
 * {@code topclasses}: restricts which EClass types are valid as root objects in a model extent.
 *
 * <p>Tags {@code rememberChanges} and {@code manuallyChanged} are intentionally ignored
 * (not implemented by Eclipse QVT-O either).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoTagRegistry {

	private final OperationalTransformation transformation;

	/** Module names marked as proxy. */
	private final Set<String> proxyModules = new HashSet<>();

	/** Model parameter name → set of allowed EClass names for root objects. */
	private final Map<String, Set<String>> topclassesMap = new HashMap<>();

	QvtoTagRegistry(OperationalTransformation transformation) {
		this.transformation = Objects.requireNonNull(transformation);
	}

	/**
	 * §8.3.19: Builds the registry from predefined tag declarations.
	 */
	void build() {
		for (EAnnotation tag : transformation.getOwnedTag()) {
			switch (tag.getSource()) {
				case "proxy" -> handleProxy(tag);
				case "topclasses" -> handleTopclasses(tag);
				// "alias" is handled by QvtoAliasRegistry
				// "rememberChanges", "manuallyChanged" intentionally ignored
				default -> { /* unknown tags silently ignored */ }
			}
		}
	}

	/**
	 * §8.3.19: {@code tag "proxy" ModuleName = true;}
	 * Marks a module as a proxy placeholder.
	 */
	private void handleProxy(EAnnotation tag) {
		String target = tag.getDetails().get("target");
		if (target != null) {
			String value = tag.getDetails().get("value");
			if ("true".equalsIgnoreCase(value)) {
				proxyModules.add(target);
			}
		}
	}

	/**
	 * §8.3.19: {@code tag "topclasses" ModelParam = 'Class1, Class2';}
	 * Restricts root object types for a model parameter.
	 */
	private void handleTopclasses(EAnnotation tag) {
		String target = tag.getDetails().get("target");
		String value = tag.getDetails().get("value");
		if (target != null && value != null) {
			Set<String> classNames = new HashSet<>();
			for (String name : value.split(",")) {
				String trimmed = name.trim();
				if (!trimmed.isEmpty()) {
					classNames.add(trimmed);
				}
			}
			if (!classNames.isEmpty()) {
				topclassesMap.put(target, classNames);
			}
		}
	}

	/**
	 * Returns whether the given module name is marked as proxy.
	 */
	boolean isProxy(String moduleName) {
		return proxyModules.contains(moduleName);
	}

	/**
	 * Returns whether the given EClass is allowed as a root object for the named model parameter.
	 * If no topclasses constraint is set for the parameter, all classes are allowed.
	 *
	 * @param modelParamName the model parameter name
	 * @param eClass the EClass to check
	 * @return {@code true} if the class is allowed (or no constraint exists)
	 */
	boolean isTopclassAllowed(String modelParamName, EClass eClass) {
		Set<String> allowed = topclassesMap.get(modelParamName);
		if (allowed == null) {
			return true; // no constraint → all allowed
		}
		return allowed.contains(eClass.getName());
	}

	/**
	 * Returns whether any topclasses constraints are registered.
	 */
	boolean hasTopclassConstraints() {
		return !topclassesMap.isEmpty();
	}
}
