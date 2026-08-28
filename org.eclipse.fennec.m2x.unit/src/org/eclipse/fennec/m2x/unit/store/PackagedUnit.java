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

import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.unit.api.Unit;

/**
 * A compiled unit in document form, language-neutral: what a store hands back for a compiled
 * unit and what it accepts for one.
 *
 * <p>The qualified name is the manifest's. A language resolver turns this into its own unit
 * record — {@code QvtoUnit.CompiledUnit} around {@code document().getUnit()} — and the compiler
 * recognizes a document behind such an AST by its container.
 *
 * @param document the compiled-unit document, never {@code null}
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record PackagedUnit(CompiledUnit document) implements Unit.Packaged {

	public PackagedUnit {
		Objects.requireNonNull(document, "document must not be null");
		Objects.requireNonNull(document.getManifest(), "a packaged unit carries a manifest");
		Objects.requireNonNull(document.getUnit(), "a packaged unit carries a script");
	}

	@Override
	public String qualifiedName() {
		return document.getManifest().getQualifiedName();
	}

	/**
	 * The language tag of the manifest.
	 *
	 * @return the language, e.g. {@code qvto}
	 */
	public String language() {
		return document.getManifest().getLanguage();
	}
}
