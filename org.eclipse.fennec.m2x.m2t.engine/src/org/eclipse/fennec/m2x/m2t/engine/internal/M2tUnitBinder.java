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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.fennec.m2x.m2t.parser.M2tParseResult;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.unit.api.UnitBinder;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;

/**
 * Binds a loaded MOFM2T unit: the link information the compiler kept on the module
 * ({@link M2tLinkInfo}) is turned back into a parse result and linked against the loaded
 * dependencies, then struck — a bound module carries none (#140). MOFM2T has no blackboxes.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class M2tUnitBinder implements UnitBinder {

	@Override
	public String language() {
		return M2tUnitCompiler.LANGUAGE;
	}

	@Override
	public void bind(CompiledUnit unit, Map<String, CompiledUnit> dependencies) throws UnitPrepareException {
		Objects.requireNonNull(unit, "unit must not be null");
		if (!(unit.getUnit() instanceof Module module)) {
			throw new UnitPrepareException("'" + unit.getManifest().getQualifiedName() + "' is not a MOFM2T unit");
		}
		M2tParseResult pending = M2tLinkInfo.recover(module).orElse(null);
		if (pending == null) {
			return; // bound at compile time, or a leaf
		}
		List<M2tParseResult> linkSet = new ArrayList<>();
		linkSet.add(pending);
		for (CompiledUnit dependency : dependencies.values()) {
			linkSet.add(new M2tParseResult((Module) dependency.getUnit(), List.of(), List.of(), Map.of(), Map.of()));
		}
		M2tModuleLinker linker = new M2tModuleLinker();
		linker.link(linkSet);
		List<String> unresolved = linker.unresolvedReferences().getOrDefault(module, List.of());
		if (!unresolved.isEmpty()) {
			throw new UnitPrepareException("'" + unit.getManifest().getQualifiedName() + "' cannot be bound: "
					+ String.join("; ", unresolved));
		}
		M2tLinkInfo.strip(module);
	}

	@Override
	public void verifyBlackboxes(CompiledUnit unit) {
		// MOFM2T has no blackboxes; Java services arrive through the OCL operation providers
	}
}
