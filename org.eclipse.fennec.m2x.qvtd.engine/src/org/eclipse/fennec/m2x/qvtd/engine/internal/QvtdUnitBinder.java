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
package org.eclipse.fennec.m2x.qvtd.engine.internal;

import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.parser.QvtrParserSupport;
import org.eclipse.fennec.m2x.unit.api.UnitBinder;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;

/**
 * Binds a loaded QVT-R unit: every import the manifest lists as a dependency is merged the way
 * the linker merges it (§7.11.1.1) and struck from the import declaration, so that nothing asks
 * for it again (#140).
 *
 * <p>QVT-R addresses blackbox operations by name across all registered libraries, and a
 * {@code QvtdBlackboxLibrary} declares no operations — so a requirement can be checked only for
 * there being a registry to serve it at all, not for the operation or its shape. That is stated
 * here rather than hidden: a stronger check needs operation descriptors on the QVT-R blackbox API.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class QvtdUnitBinder implements UnitBinder {

	private final QvtdBlackboxRegistry blackboxRegistry;
	private final boolean blackboxEnabled;

	QvtdUnitBinder(QvtdBlackboxRegistry blackboxRegistry, boolean blackboxEnabled) {
		this.blackboxRegistry = blackboxRegistry; // nullable
		this.blackboxEnabled = blackboxEnabled;
	}

	@Override
	public String language() {
		return QvtdUnitCompiler.LANGUAGE;
	}

	@Override
	public void bind(CompiledUnit unit, Map<String, CompiledUnit> dependencies) throws UnitPrepareException {
		Objects.requireNonNull(unit, "unit must not be null");
		if (!(unit.getUnit() instanceof RelationalTransformation transformation)) {
			throw new UnitPrepareException("'" + unit.getManifest().getQualifiedName() + "' is not a QVT-R unit");
		}
		for (String imported : QvtdUnitCompiler.importedNames(transformation)) {
			CompiledUnit dependency = dependencies.get(imported);
			if (dependency == null) {
				throw new UnitPrepareException("'" + unit.getManifest().getQualifiedName() + "' imports '" + imported
						+ "', which is not among the loaded dependencies");
			}
			QvtdLinker.merge(transformation, (RelationalTransformation) dependency.getUnit());
		}
		EAnnotation imports = transformation.getEAnnotation(QvtrParserSupport.IMPORTS_ANNOTATION);
		if (imports != null) {
			transformation.getEAnnotations().remove(imports);
		}
	}

	@Override
	public void verifyBlackboxes(CompiledUnit unit) throws UnitPrepareException {
		if (unit.getManifest().getBlackboxRequirement().isEmpty()) {
			return;
		}
		if (!blackboxEnabled || blackboxRegistry == null || blackboxRegistry.getLibraries().isEmpty()) {
			BlackboxRequirement first = unit.getManifest().getBlackboxRequirement().get(0);
			throw new UnitPrepareException("'" + unit.getManifest().getQualifiedName()
					+ "' requires the blackbox query '" + first.getName()
					+ "', and the runtime has no blackbox library to serve it");
		}
	}
}
