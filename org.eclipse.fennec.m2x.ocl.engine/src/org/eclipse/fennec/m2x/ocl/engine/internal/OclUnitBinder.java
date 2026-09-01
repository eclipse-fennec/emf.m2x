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
package org.eclipse.fennec.m2x.ocl.engine.internal;

import java.util.Map;

import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.ocl.CompleteOclDocument;
import org.eclipse.fennec.m2x.unit.api.UnitBinder;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;

/**
 * The OCL half of preparing a unit (#209): the root has to be a {@link CompleteOclDocument},
 * and that is all — a Complete OCL document imports no other units in this version, so there
 * is nothing to bind and no blackbox to verify. Prepare ends the lifecycle here; the engine's
 * {@code registerCompleteOclDocument(prepared, name)} is what execute is for the other
 * languages.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class OclUnitBinder implements UnitBinder {

	@Override
	public String language() {
		return "ocl";
	}

	@Override
	public void validate(CompiledUnit unit) throws UnitPrepareException {
		if (!(unit.getUnit() instanceof CompleteOclDocument)) {
			throw new UnitPrepareException("'" + unit.getManifest().getQualifiedName()
					+ "' declared an OCL unit but holds a " + unit.getUnit().eClass().getName()
					+ ", not a CompleteOclDocument");
		}
	}

	@Override
	public void bind(CompiledUnit unit, Map<String, CompiledUnit> dependencies) {
		// a Complete OCL document has no unit imports — nothing to bind
	}

	@Override
	public void verifyBlackboxes(CompiledUnit unit) {
		// and no blackboxes either
	}
}
