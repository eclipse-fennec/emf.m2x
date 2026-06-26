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
 * Eclipse IDE integration for the Fennec OCL engine.
 *
 * <p>This package contains the extension-registry entry points named by
 * {@code plugin.xml}. They register the Fennec OCL delegates with EMF's
 * delegate registries so that the generic (reflective) EMF editor evaluates OCL
 * annotations &mdash; invariant constraints, derived-feature derivations, and
 * operation bodies.
 *
 * <p>This is an internal contribution package; it is intentionally
 * <em>not</em> exported. The classes are public only because the Eclipse
 * extension registry instantiates them reflectively.
 */
@org.osgi.annotation.bundle.Export
@org.osgi.annotation.versioning.Version("1.0.0")
package org.eclipse.fennec.m2x.ocl.ide;
