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

import java.util.Objects;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;

/**
 * A QVT-R compilation unit, either as source text or as a pre-compiled
 * transformation.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public sealed interface QvtdUnit permits QvtdUnit.SourceUnit, QvtdUnit.CompiledUnit {

	/**
	 * Returns the fully qualified unit name.
	 */
	String qualifiedName();

	/**
	 * A source-based unit that needs to be parsed.
	 *
	 * @param qualifiedName the fully qualified unit name
	 * @param uri the source URI
	 * @param source the source text
	 */
	record SourceUnit(String qualifiedName, URI uri, String source) implements QvtdUnit {
		public SourceUnit {
			Objects.requireNonNull(qualifiedName, "qualifiedName must not be null");
			Objects.requireNonNull(uri, "uri must not be null");
			Objects.requireNonNull(source, "source must not be null");
		}
	}

	/**
	 * A pre-compiled unit containing a parsed transformation.
	 *
	 * @param qualifiedName the fully qualified unit name
	 * @param transformation the pre-compiled transformation
	 */
	record CompiledUnit(String qualifiedName, RelationalTransformation transformation) implements QvtdUnit {
		public CompiledUnit {
			Objects.requireNonNull(qualifiedName, "qualifiedName must not be null");
			Objects.requireNonNull(transformation, "transformation must not be null");
		}
	}
}
