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

import org.osgi.annotation.versioning.ConsumerType;

/**
 * Extension interface for providing custom OCL operations.
 *
 * <p>Without OSGi, register providers programmatically via
 * {@link OclEngine#registerOperations(OclOperationProvider)}.
 *
 * <p>With OSGi, providers are discovered automatically via the whiteboard pattern:
 * <pre>
 * {@literal @}Component(service = OclOperationProvider.class)
 * public class MyOperations implements OclOperationProvider {
 *     ...
 * }
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface OclOperationProvider {

	/**
	 * Returns the custom operations provided by this provider.
	 *
	 * @return the list of custom operations, never {@code null}
	 */
	List<OclOperation> getOperations();
}
