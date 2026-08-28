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
package org.eclipse.fennec.m2x.m2t.api;

/**
 * How a generation reacts to a reference the linker cannot resolve — an {@code extends}, an
 * {@code import}, an {@code overrides} or an invocation naming something that is not there.
 *
 * <p>QVT-O and QVT-R fail such a case outright ({@code Cannot resolve import}). M2T reported it
 * as a warning and generated anyway, which is the quieter and worse outcome: a missing
 * {@code extends} silently changes which templates are visible, so the document is not absent
 * but wrong (#144).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public enum UnresolvedReferenceMode {

	/**
	 * An unresolved reference is an error: it is reported as an error diagnostic and the
	 * generation produces no files. This is the default, and it matches QVT-O and QVT-R.
	 */
	FAIL,

	/**
	 * An unresolved reference is a warning: it is reported and the generation continues with
	 * whatever could be resolved. The behaviour M2T had before #144, kept reachable for a
	 * caller who knowingly generates from an incomplete module set.
	 */
	WARN
}
