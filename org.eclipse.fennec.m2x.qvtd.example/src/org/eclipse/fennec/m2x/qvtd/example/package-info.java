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
 * A worked, plain-Java QVT-R example over real files (#207): two metamodels, an instance model
 * and a {@code .qvtr} transformation with keys, a query, a where-chained relation and a
 * when-guard — run {@link org.eclipse.fennec.m2x.qvtd.example.AdHocExample ad hoc} and as a
 * {@link org.eclipse.fennec.m2x.qvtd.example.CompiledUnitExample compiled unit}. Run either
 * {@code main} from the project directory; the smoke test keeps both honest.
 */
@org.osgi.annotation.bundle.Export
@org.osgi.annotation.versioning.Version("1.0")
package org.eclipse.fennec.m2x.qvtd.example;
