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

import org.osgi.annotation.versioning.ConsumerType;

/**
 * Extension interface for providing custom OCL operations.
 *
 * <p>Without OSGi, register providers via
 * {@link OclConfiguration.Builder#addOperationProvider(OclOperationProvider)}.
 *
 * <p>With OSGi, the {@code OclEngineComponent} binds exactly one
 * {@code OclOperationProvider} via a mandatory {@code @Reference}.
 * A default no-op provider is always available. To supply custom
 * operations, register a component and configure the target filter:
 * <pre>
 * {@literal @}Component(service = OclOperationProvider.class,
 *            property = "provider.name=myProvider")
 * public class MyOperations implements OclOperationProvider {
 *     ...
 * }
 * </pre>
 * Then configure the engine to select it:
 * <pre>
 * "DefaultOclEngine": {
 *     "operationProvider.target": "(provider.name=myProvider)",
 *     "ocl.customOperationsEnabled": true
 * }
 * </pre>
 *
 * <p>If multiple providers are needed, implement a compound provider
 * that delegates to multiple underlying providers.
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
