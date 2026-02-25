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

import java.util.Collection;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * Provides the model extent (universe of instances) for {@code allInstances()} evaluation.
 *
 * <p>In OCL, the expression {@code Employee.allInstances()} requires access to all
 * instances of a given classifier within some scope. This interface abstracts that scope,
 * which is typically backed by a {@code ResourceSet} or a single {@code Resource}.
 *
 * <p>Only needed when evaluating expressions that use {@code allInstances()}.
 * Most OCL expressions navigate from the context object and do not require an extent.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface OclModelExtent {

	/**
	 * Returns all instances of the given {@code EClass} within this extent,
	 * including instances of subtypes.
	 *
	 * @param eClass the classifier whose instances to retrieve
	 * @return a collection of matching instances, never {@code null}
	 */
	Collection<EObject> getAllInstances(EClass eClass);
}
