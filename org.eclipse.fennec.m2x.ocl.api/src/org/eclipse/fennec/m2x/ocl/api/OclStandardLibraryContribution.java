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
package org.eclipse.fennec.m2x.ocl.api;

import java.util.List;

import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * Extension interface for contributing additional types and operations to the OCL standard library.
 *
 * <p>Without OSGi, contributions are provided via constructor injection to the engine.
 *
 * <p>With OSGi, contributions are discovered automatically via the whiteboard pattern:
 * <pre>
 * {@literal @}Component(service = OclStandardLibraryContribution.class)
 * public class MyStdlibExtension implements OclStandardLibraryContribution {
 *     ...
 * }
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface OclStandardLibraryContribution {

	/**
	 * Returns additional operations to add to the standard library.
	 *
	 * @return the list of additional operations, never {@code null}
	 */
	List<OclOperation> getOperations();

	/**
	 * Returns additional type definitions to add to the standard library.
	 *
	 * @return the list of additional types, never {@code null}
	 */
	List<OclType> getTypes();
}
