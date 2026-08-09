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
 * A second provider, next to {@link TestOclOperationProvider}.
 *
 * <p>It exists to be a second one. While the engine bound exactly one provider, these two
 * could not both be reached, which is the situation a real runtime is in as soon as the
 * MOFM2T standard library and any set of domain operations are both wanted.
 */
@Component(service = OclOperationProvider.class, property = "provider.name=secondProvider")
public class SecondTestOclOperationProvider implements OclOperationProvider {

	@Override
	public List<OclOperation> getOperations() {
		AnyType anyType = OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		PrimitiveType intType = OclFactory.eINSTANCE.createPrimitiveType();
		intType.setName("Integer");

		return List.of(OclOperation.of("secondOp", anyType, intType, (self, args) -> 7));
	}
}
