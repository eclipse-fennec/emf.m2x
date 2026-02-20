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
package org.eclipse.fennec.m2m.qvto.api;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import org.eclipse.emf.ecore.EClassifier;

/**
 * Describes a single blackbox operation contributed by a {@link QvtoBlackboxLibrary}.
 *
 * <p>A blackbox operation is a Java-implemented operation callable from QVT-O
 * transformation code. The {@code handler} receives the context object (or
 * {@code null} for module-level operations) and an array of arguments.
 *
 * @param name the operation name as used in QVT-O source
 * @param contextType the context type for contextual operations, or {@code null} for module-level
 * @param parameterTypes the parameter types in declaration order
 * @param returnType the return type
 * @param handler the implementation: {@code (context, args) -> result}
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record QvtoBlackboxOperation(
		String name,
		EClassifier contextType,
		List<EClassifier> parameterTypes,
		EClassifier returnType,
		BiFunction<Object, Object[], Object> handler) {

	public QvtoBlackboxOperation {
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(returnType, "returnType must not be null");
		Objects.requireNonNull(handler, "handler must not be null");
		parameterTypes = parameterTypes == null ? List.of() : List.copyOf(parameterTypes);
	}

	/**
	 * Creates a module-level blackbox operation (no context type).
	 *
	 * @param name the operation name
	 * @param parameterTypes the parameter types
	 * @param returnType the return type
	 * @param handler the implementation
	 * @return the operation descriptor
	 */
	public static QvtoBlackboxOperation moduleLevel(
			String name,
			List<EClassifier> parameterTypes,
			EClassifier returnType,
			BiFunction<Object, Object[], Object> handler) {
		return new QvtoBlackboxOperation(name, null, parameterTypes, returnType, handler);
	}
}
