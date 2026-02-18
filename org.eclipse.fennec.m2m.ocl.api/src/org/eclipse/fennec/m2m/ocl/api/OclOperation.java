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
package org.eclipse.fennec.m2m.ocl.api;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import org.eclipse.fennec.m2m.model.ocl.OclType;

/**
 * Describes a custom OCL operation that can be registered via {@link OclOperationProvider}.
 *
 * <p>The {@code ownerType} is an {@link OclType} from the OCL metamodel:
 * <ul>
 *   <li>For operations on Ecore model types, use a {@code ClassifierType} wrapping the
 *       target {@code EClassifier}.</li>
 *   <li>For operations on OCL built-in types, use {@code AnyType}, {@code CollectionType},
 *       {@code PrimitiveType}, etc. directly.</li>
 * </ul>
 *
 * @param name the operation name as it appears in OCL expressions
 * @param ownerType the type this operation is defined on
 * @param parameterTypes the OCL types of the operation parameters (empty for no-arg operations)
 * @param returnType the OCL return type
 * @param implementation the implementation function; receives {@code (self, arguments)} and
 *        returns the result
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record OclOperation(
		String name,
		OclType ownerType,
		List<OclType> parameterTypes,
		OclType returnType,
		BiFunction<Object, Object[], Object> implementation) {

	/**
	 * Canonical constructor with validation.
	 */
	public OclOperation {
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(ownerType, "ownerType must not be null");
		parameterTypes = List.copyOf(Objects.requireNonNull(parameterTypes,
				"parameterTypes must not be null"));
		Objects.requireNonNull(returnType, "returnType must not be null");
		Objects.requireNonNull(implementation, "implementation must not be null");
	}

	/**
	 * Creates a no-arg operation.
	 *
	 * @param name the operation name
	 * @param ownerType the type this operation is defined on
	 * @param returnType the return type
	 * @param implementation the implementation function
	 * @return the operation descriptor
	 */
	public static OclOperation of(String name, OclType ownerType, OclType returnType,
			BiFunction<Object, Object[], Object> implementation) {
		return new OclOperation(name, ownerType, List.of(), returnType, implementation);
	}
}
