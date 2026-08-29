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

import java.util.Objects;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;

/**
 * One execution of a QVT-O transformation: what runs, on what, under which options.
 *
 * <p>The extent manager and the configuration properties used to be handed to the evaluator
 * beside these — both are {@code (transformation, context)}, so a caller could pass extents
 * built for another transformation, or properties the environment was not built from. The
 * blackbox registry is the engine's effective one, after its enable flag (#185).
 *
 * @param transformation the transformation to run
 * @param context the extents this run reads and writes, and its configuration properties
 * @param options the evaluation options, and with them the limits of the run
 * @param packageRegistry the metamodels this run resolves against (D42)
 * @param blackboxRegistry the blackbox libraries this run may reach, or {@code null}
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record QvtoRun(
		OperationalTransformation transformation,
		QvtoExecutionContext context,
		QvtoEvaluationOptions options,
		EPackage.Registry packageRegistry,
		QvtoBlackboxRegistry blackboxRegistry) {

	public QvtoRun {
		Objects.requireNonNull(transformation, "transformation must not be null");
		Objects.requireNonNull(context, "context must not be null");
		Objects.requireNonNull(options, "options must not be null");
		Objects.requireNonNull(packageRegistry, "packageRegistry must not be null");
	}
}
