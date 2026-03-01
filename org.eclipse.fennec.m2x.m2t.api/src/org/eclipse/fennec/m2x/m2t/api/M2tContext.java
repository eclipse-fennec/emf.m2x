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
package org.eclipse.fennec.m2x.m2t.api;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;

/**
 * Execution context for a MOFM2T template execution.
 *
 * <p>Contains the input model element(s) and the target directory for
 * file generation.
 *
 * @param inputElements the root model elements to pass to the main template
 * @param targetDirectory the base directory for generated files (may be {@code null} for in-memory)
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record M2tContext(
		List<EObject> inputElements,
		Path targetDirectory) {

	public M2tContext {
		Objects.requireNonNull(inputElements, "inputElements must not be null");
		inputElements = List.copyOf(inputElements);
	}

	/**
	 * Creates a context with a single input element and no target directory.
	 *
	 * @param element the input model element
	 * @return the execution context
	 */
	public static M2tContext of(EObject element) {
		Objects.requireNonNull(element, "element must not be null");
		return new M2tContext(List.of(element), null);
	}

	/**
	 * Creates a context with a single input element and a target directory.
	 *
	 * @param element the input model element
	 * @param targetDirectory the base directory for generated files
	 * @return the execution context
	 */
	public static M2tContext of(EObject element, Path targetDirectory) {
		Objects.requireNonNull(element, "element must not be null");
		return new M2tContext(List.of(element), targetDirectory);
	}

	/**
	 * Creates a context with multiple input elements and a target directory.
	 *
	 * @param elements the input model elements
	 * @param targetDirectory the base directory for generated files
	 * @return the execution context
	 */
	public static M2tContext of(List<EObject> elements, Path targetDirectory) {
		return new M2tContext(elements, targetDirectory);
	}
}
