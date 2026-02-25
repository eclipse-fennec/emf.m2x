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
package org.eclipse.fennec.m2x.ocl.engine.internal;

import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;

/**
 * Internal data structures for the Complete OCL {@code def:} expression registry.
 */
public final class DefRegistry {

	private DefRegistry() {}

	/**
	 * Key for the def-property/operation registry: (classifier, featureName).
	 */
	public record DefKey(EClassifier classifier, String featureName) {}

	/**
	 * Entry in the def-property registry. Holds the body expression, optional
	 * parameter names (for def-operations), and static flag.
	 */
	public record DefEntry(OclExpression body, List<String> parameterNames, boolean isStatic) {
		public DefEntry {
			parameterNames = List.copyOf(parameterNames);
		}

		public boolean isOperation() {
			return !parameterNames.isEmpty();
		}
	}
}
