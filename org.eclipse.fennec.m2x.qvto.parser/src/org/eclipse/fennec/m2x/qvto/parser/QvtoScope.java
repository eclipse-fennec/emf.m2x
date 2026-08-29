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
package org.eclipse.fennec.m2x.qvto.parser;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.model.qvtoperational.Module;

/**
 * What a unit knows while it is being built, shared with its expression builder.
 *
 * <p>All four are mutable and deliberately so: the unit builder fills them as it descends —
 * a {@code typedef} adds a local type, a {@code modeltype} adds a package — and the
 * expression builder reads them while it builds the expressions of that same unit. It is also
 * why they must be shared rather than copied: the expression builder is rebuilt whenever the
 * environment changes, and a copy would lose everything the unit had learned by then.
 *
 * <p>Passed one by one, they were four of the six parameters of a constructor with three
 * overloads (#185), two of them collections of {@code String} keys that no compiler would tell
 * apart if they were swapped.
 *
 * @param importedModuleStubs the modules this unit imports, by name, stubs until the link
 * @param localTypes module-local type names ({@code typedef})
 * @param diagnostics where both builders report what they could not resolve
 * @param declaredPackages the metamodels this unit declared, in declaration order
 * @author Data In Motion Consulting
 * @since 1.0
 */
record QvtoScope(
		Map<String, Module> importedModuleStubs,
		Map<String, EClassifier> localTypes,
		List<Resource.Diagnostic> diagnostics,
		Set<EPackage> declaredPackages) {

	QvtoScope {
		Objects.requireNonNull(importedModuleStubs, "importedModuleStubs must not be null");
		Objects.requireNonNull(localTypes, "localTypes must not be null");
		Objects.requireNonNull(diagnostics, "diagnostics must not be null");
		Objects.requireNonNull(declaredPackages, "declaredPackages must not be null");
	}
}
