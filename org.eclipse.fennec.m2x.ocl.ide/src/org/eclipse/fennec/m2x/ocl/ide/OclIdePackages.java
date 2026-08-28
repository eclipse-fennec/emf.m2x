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
package org.eclipse.fennec.m2x.ocl.ide;

import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.ocl.OclPackage;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;

/**
 * The packages this bundle declares to the Eclipse IDE through {@code plugin.xml}
 * ({@code org.eclipse.emf.ecore.generated_package}), listed here as well so that the declaration
 * and the code cannot drift apart unnoticed — a test compares the two — and so that this bundle
 * imports the packages the extension registry has to load the classes from.
 *
 * <p>Why they need declaring at all: in a plain JVM and under OSGi the OCL metamodel and the
 * standard library reach {@code EPackage.Registry.INSTANCE} when their classes initialize, which
 * happens before anything could reference them. An Eclipse IDE fills its registry from
 * {@code plugin.xml} instead, and reads a resource before running any of our code — a stored
 * unit that references {@code …/ocl/stdlib/1.0#//Integer} would otherwise resolve to nothing
 * (#157). The languages' own metamodels follow when their bundles ship in a feature.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class OclIdePackages {

	/** The packages {@code plugin.xml} declares, by nsURI, in the order declared there. */
	public static final List<EPackage> DECLARED = List.of(
			OclPackage.eINSTANCE,
			OclStandardLibrary.eINSTANCE);

	private OclIdePackages() {
	}
}
