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

import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.osgi.service.component.annotations.Component;

/**
 * Default no-op {@link OclOperationProvider} that provides no custom operations.
 *
 * <p>This component is registered with low service ranking so that any
 * application-provided {@code OclOperationProvider} automatically takes
 * precedence. If no other provider is registered, this one is used by
 * {@link OclEngineComponent} to satisfy its mandatory {@code @Reference}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component
public class NoOpOclOperationProvider implements OclOperationProvider {

	@Override
	public List<OclOperation> getOperations() {
		return List.of();
	}
}
