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
package org.eclipse.fennec.m2x.qvtd.api;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.Diagnostic;

/**
 * What a {@link RelationImplementationProvider} returns for an {@code implementedby} call
 * (QVT v1.3 §7.11.3.6, #282).
 *
 * @param value the operation's result — the object of the enforced domain — or {@code null}
 * @param diagnostics what the operation reported; an {@code ERROR} among them fails the run
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record RelationImplementationResult(Object value, List<Diagnostic> diagnostics) {

	/**
	 * @param value the operation's result, or {@code null}
	 * @param diagnostics what the operation reported, must not be {@code null}
	 */
	public RelationImplementationResult {
		diagnostics = List.copyOf(Objects.requireNonNull(diagnostics, "diagnostics must not be null"));
	}
}
