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
/**
 * A worked, plain-Java OCL example over real files (#205): a metamodel whose OCL sits in
 * delegate annotations, and a Complete OCL document beside it — run through
 * {@link org.eclipse.fennec.m2x.ocl.example.DelegatesExample EMF's own delegates},
 * {@link org.eclipse.fennec.m2x.ocl.example.AdHocExample ad hoc as a loaded document} and as a
 * {@link org.eclipse.fennec.m2x.ocl.example.CompiledUnitExample compiled unit}. Run any
 * {@code main} from the project directory; the smoke test keeps all three honest.
 */
@org.osgi.annotation.bundle.Export
@org.osgi.annotation.versioning.Version("1.0")
package org.eclipse.fennec.m2x.ocl.example;
