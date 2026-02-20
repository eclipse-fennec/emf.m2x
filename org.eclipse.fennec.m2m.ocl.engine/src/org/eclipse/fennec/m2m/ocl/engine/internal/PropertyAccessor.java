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
package org.eclipse.fennec.m2m.ocl.engine.internal;

/**
 * Fast property accessor for reading a single structural feature value
 * from a target object.
 *
 * <p>Implementations are typically generated via {@link java.lang.invoke.LambdaMetafactory}
 * to produce JIT-inlineable accessors that bypass the EMF reflective {@code eGet()} chain.
 *
 * <p>This is an engine-internal SPI, not part of the public API.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@FunctionalInterface
interface PropertyAccessor {

	/**
	 * Reads the property value from the target object.
	 *
	 * @param target the object to read from
	 * @return the property value, may be {@code null}
	 */
	Object get(Object target);
}
