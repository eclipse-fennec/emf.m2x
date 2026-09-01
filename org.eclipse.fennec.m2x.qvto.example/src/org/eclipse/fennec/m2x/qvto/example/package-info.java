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
 * A worked, plain-Java QVT-O example over real files (#206): two metamodels, an instance model
 * and two {@code .qvto} files in this project, run two ways —
 * {@link org.eclipse.fennec.m2x.qvto.example.AdHocExample ad hoc} (parse and execute, the
 * library resolved as source) and as
 * {@link org.eclipse.fennec.m2x.qvto.example.CompiledUnitExample compiled units} (compile,
 * store, prepare, execute — no parser after compile). Run either {@code main} from the project
 * directory; the smoke test keeps both honest.
 */
@org.osgi.annotation.bundle.Export
@org.osgi.annotation.versioning.Version("1.0")
package org.eclipse.fennec.m2x.qvto.example;
