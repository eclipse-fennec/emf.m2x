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
package org.eclipse.fennec.m2x.unit.store;

import java.util.Objects;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.unit.api.Unit;

/**
 * A source text as a store hands it back, language-neutral.
 *
 * @param qualifiedName the name the unit is imported by
 * @param uri where the source was originally read from
 * @param source the text
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record StoredSource(String qualifiedName, URI uri, String source) implements Unit.Source {

	public StoredSource {
		Objects.requireNonNull(qualifiedName, "qualifiedName must not be null");
		Objects.requireNonNull(uri, "uri must not be null");
		Objects.requireNonNull(source, "source must not be null");
	}
}
