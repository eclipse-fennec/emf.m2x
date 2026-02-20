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
package org.eclipse.fennec.m2m.qvto.engine.internal;

import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;

/**
 * Lightweight trace record linking a mapping invocation's source to its result.
 *
 * <p>Used for fast resolve lookups at runtime. The EMF-based {@code Trace} model
 * is built in parallel by {@link QvtoTraceManager} for export purposes.
 *
 * @param mappingName the name of the mapping that produced this record
 * @param mappingOp the mapping operation AST node
 * @param source the source object passed to the mapping
 * @param result the result object created by the mapping
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
record QvtoTraceRecord(String mappingName, MappingOperation mappingOp, Object source, Object result) {
}
