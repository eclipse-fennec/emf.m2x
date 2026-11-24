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
package org.eclipse.fennec.m2x.ocl.engine;

import java.util.Set;

import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Names the {@link OclOperationProvider} services currently registered, as the
 * {@link #OPERATION_PROVIDER_NAMES} property of this service.
 *
 * <p>Target this service to hold a component back until the providers it needs are
 * there. A target filter can require several at once, which no filter on the providers
 * themselves can do — cardinality counts services, not names:
 * <pre>
 * "DefaultOclEngine~myEngine": {
 *     "operationProvidersReady.target": "(&amp;(operationProviderNames=A)(operationProviderNames=B))"
 * }
 * </pre>
 *
 * <p>This selects nothing. Which providers a component binds is still decided by that
 * component's own reference to {@link OclOperationProvider}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface OclOperationProviderProvider {

	/**
	 * Service property holding the {@code provider.name} of every registered
	 * {@link OclOperationProvider}, as a {@code List<String>}.
	 */
	String OPERATION_PROVIDER_NAMES = "operationProviderNames";

	/**
	 * Returns the {@code provider.name} of every registered provider.
	 *
	 * @return unmodifiable snapshot of the names, never {@code null}
	 */
	Set<String> getOperationProviderNames();
}
