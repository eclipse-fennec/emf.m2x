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
 * A worked, plain-Java MOFM2T example over real files (#208): a metamodel, an instance model
 * and two {@code .mtl} modules — an import, for/if blocks, both file open modes and a protected
 * area whose content survives regeneration. Run
 * {@link org.eclipse.fennec.m2x.m2t.example.AdHocExample ad hoc} or as
 * {@link org.eclipse.fennec.m2x.m2t.example.CompiledUnitExample compiled units}, from the
 * project directory; the smoke test keeps both honest.
 */
@org.osgi.annotation.bundle.Export
@org.osgi.annotation.versioning.Version("1.0")
package org.eclipse.fennec.m2x.m2t.example;
