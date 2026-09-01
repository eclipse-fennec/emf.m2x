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
package org.eclipse.fennec.m2x.m2t.engine.internal;

import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.unit.api.UnitMaterializeException;
import org.eclipse.fennec.m2x.unit.api.UnitResourceSet;
import org.eclipse.fennec.m2x.unit.materialize.UnitMaterializer;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;

/**
 * Turns a unit named by reference into a loaded one (#214): the consumer's side of
 * {@code M2tUnit.ResourceUnit}. Loading and binding happen here, in a context over the
 * engine's packages — which metamodel instance a reference binds to is this consumer's
 * business, never the resolver's. One instance serves one link or compile run, so every
 * reference it loads shares one context.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class M2tReferencedUnits {

	private final EPackage.Registry packages;
	private final UnitMaterializer materializer = UnitMaterializer.defaults();
	private UnitResourceSet context;

	M2tReferencedUnits(EPackage.Registry packages) {
		this.packages = Objects.requireNonNull(packages, "packages must not be null");
	}

	/**
	 * Returns the unit with a reference replaced by what it names — every other unit as it was.
	 *
	 * @param unit the resolved unit
	 * @return a unit no downstream switch has to know references for
	 * @throws M2tParseException if the reference cannot be loaded, or names no MOFM2T unit
	 */
	M2tUnit dereference(M2tUnit unit) throws M2tParseException {
		if (unit instanceof M2tUnit.CompiledUnit compiled
				&& compiled.module().eContainer() instanceof CompiledUnit document) {
			// A document-backed AST — a store resolver's answer — is bound here, in this run's
			// context, the same place a reference is loaded (#214). The store stays dumb.
			try {
				materializer.materialize(new PackagedUnit(document), context());
			} catch (UnitMaterializeException failure) {
				throw new M2tParseException("Cannot resolve import '" + compiled.qualifiedName() + "': "
						+ failure.getMessage(), failure);
			}
			return unit;
		}
		if (!(unit instanceof M2tUnit.ResourceUnit reference)) {
			return unit;
		}
		EObject root;
		try {
			root = materializer.load(reference.uri(), context());
		} catch (UnitMaterializeException failure) {
			throw new M2tParseException("Cannot resolve import '" + reference.qualifiedName() + "': "
					+ failure.getMessage(), failure);
		}
		EObject ast = root instanceof CompiledUnit document ? document.getUnit() : root;
		if (ast instanceof Module module) {
			return new M2tUnit.CompiledUnit(reference.qualifiedName(), module);
		}
		throw new M2tParseException("Cannot resolve import '" + reference.qualifiedName() + "': "
				+ reference.uri() + " holds no MOFM2T unit but "
				+ (ast == null ? "nothing" : "a " + ast.eClass().getName()));
	}

	private UnitResourceSet context() {
		if (context == null) {
			context = new UnitResourceSet(packages);
		}
		return context;
	}
}
