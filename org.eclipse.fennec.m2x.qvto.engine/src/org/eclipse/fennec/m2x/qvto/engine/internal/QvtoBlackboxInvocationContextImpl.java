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
package org.eclipse.fennec.m2x.qvto.engine.internal;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxInvocationContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;

/**
 * Implementation of {@link QvtoBlackboxInvocationContext} that bridges
 * to the current evaluation state.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoBlackboxInvocationContextImpl implements QvtoBlackboxInvocationContext {

	private static final String SOURCE_ID = "org.eclipse.fennec.m2x.qvto.engine.blackbox";

	private final Object self;
	private final List<Diagnostic> diagnostics;
	private final QvtoExtentManager extentManager;
	private final Map<String, Object> configProperties;
	private final EPackage.Registry packageRegistry;

	QvtoBlackboxInvocationContextImpl(Object self, List<Diagnostic> diagnostics,
			QvtoExtentManager extentManager, Map<String, Object> configProperties,
			EPackage.Registry packageRegistry) {
		this.self = self;
		this.diagnostics = Objects.requireNonNull(diagnostics);
		this.extentManager = Objects.requireNonNull(extentManager);
		this.configProperties = Objects.requireNonNull(configProperties);
		this.packageRegistry = Objects.requireNonNull(packageRegistry);
	}

	@Override
	public Object self() {
		return self;
	}

	@Override
	public void addInfo(String message) {
		diagnostics.add(new BasicDiagnostic(Diagnostic.INFO, SOURCE_ID, 0, message, null));
	}

	@Override
	public void addWarning(String message) {
		diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING, SOURCE_ID, 0, message, null));
	}

	@Override
	public void addError(String message) {
		diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE_ID, 0, message, null));
	}

	@Override
	public Map<String, Object> getConfigProperties() {
		return configProperties;
	}

	@Override
	public QvtoModelExtent getExtent(String name) {
		return extentManager.getExtent(name);
	}

	@Override
	public EPackage.Registry getPackageRegistry() {
		return packageRegistry;
	}
}
