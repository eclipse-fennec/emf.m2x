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

/**
 * Lightweight trace record linking a mapping invocation's source to its result.
 *
 * <p>This is an interim structure for Phase B. Phase C will replace it with
 * the full EMF-based trace model.
 *
 * @param mappingName the name of the mapping that produced this record
 * @param source the source object passed to the mapping
 * @param result the result object created by the mapping
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
record QvtoTraceRecord(String mappingName, Object source, Object result) {
}
