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

import java.util.Objects;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.model.m2t.Module;

/**
 * A MOFM2T compilation unit, either as source text or as a pre-compiled
 * module.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public sealed interface M2tUnit permits M2tUnit.SourceUnit, M2tUnit.CompiledUnit {

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
	record SourceUnit(String qualifiedName, URI uri, String source) implements M2tUnit {
		public SourceUnit {
			Objects.requireNonNull(qualifiedName, "qualifiedName must not be null");
			Objects.requireNonNull(uri, "uri must not be null");
			Objects.requireNonNull(source, "source must not be null");
		}
	}

	/**
	 * A pre-compiled unit containing a parsed module.
	 *
	 * @param qualifiedName the fully qualified unit name
	 * @param module the pre-compiled module
	 */
	record CompiledUnit(String qualifiedName, Module module) implements M2tUnit {
		public CompiledUnit {
			Objects.requireNonNull(qualifiedName, "qualifiedName must not be null");
			Objects.requireNonNull(module, "module must not be null");
		}
	}
}
