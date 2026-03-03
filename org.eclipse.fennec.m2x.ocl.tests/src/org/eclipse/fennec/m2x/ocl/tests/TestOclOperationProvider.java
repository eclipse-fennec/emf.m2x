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
package org.eclipse.fennec.m2x.ocl.tests;

import java.util.List;

import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.osgi.service.component.annotations.Component;

/**
 * Test {@link OclOperationProvider} that provides a single {@code testOp()}
 * operation returning the integer {@code 42}.
 *
 * <p>Registered with {@code provider.name=testProvider} so that engine
 * instances can target it via {@code operationProvider.target=(provider.name=testProvider)}.
 */
@Component(service = OclOperationProvider.class, property = "provider.name=testProvider")
public class TestOclOperationProvider implements OclOperationProvider {

	@Override
	public List<OclOperation> getOperations() {
		AnyType anyType = OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		PrimitiveType intType = OclFactory.eINSTANCE.createPrimitiveType();
		intType.setName("Integer");

		return List.of(OclOperation.of("testOp", anyType, intType,
				(self, args) -> 42));
	}
}
