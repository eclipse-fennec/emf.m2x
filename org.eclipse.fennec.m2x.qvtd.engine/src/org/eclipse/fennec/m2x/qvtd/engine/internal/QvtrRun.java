/*
 * ******************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 * ******************************************************************
 */
package org.eclipse.fennec.m2x.qvtd.engine.internal;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.RelationImplementationProvider;

/**
 * One execution of a QVT-R transformation: what runs, on what, under which configuration.
 *
 * <p>These four decide everything else a run needs, and used to be passed to the evaluator
 * one by one along with two values derived from them — the extent manager, which is nothing
 * but {@code (transformation, context)}, and the blackbox registry, which is
 * {@link QvtdConfiguration#blackboxRegistry()}. Nine parameters, two of which a caller could
 * get wrong by handing in a registry the configuration does not know about, or an extent
 * manager built for another transformation (#185).
 *
 * @param transformation the transformation to run
 * @param context the direction, the extents and whether this is a check
 * @param config the engine configuration, and with it every limit of the run
 * @param implementationProviders the providers for {@code implementedby} relations (§7.11.4)
 * @param positionLookup where a node stood in its source, for runtime diagnostics (#116)
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record QvtrRun(
		RelationalTransformation transformation,
		QvtdExecutionContext context,
		QvtdConfiguration config,
		List<RelationImplementationProvider> implementationProviders,
		Function<EObject, SourcePosition> positionLookup) {

	public QvtrRun {
		Objects.requireNonNull(transformation, "transformation must not be null");
		Objects.requireNonNull(context, "context must not be null");
		Objects.requireNonNull(config, "config must not be null");
		implementationProviders = List.copyOf(Objects.requireNonNull(implementationProviders,
				"implementationProviders must not be null"));
		positionLookup = positionLookup == null ? node -> null : positionLookup;
	}

	/**
	 * A run with no implementation providers and no source positions.
	 *
	 * @param transformation the transformation to run
	 * @param context the execution context
	 * @param config the engine configuration
	 * @return the run
	 */
	public static QvtrRun of(RelationalTransformation transformation,
			QvtdExecutionContext context, QvtdConfiguration config) {
		return new QvtrRun(transformation, context, config, List.of(), null);
	}

	/**
	 * The same run with the given implementation providers.
	 *
	 * @param providers the providers for {@code implementedby} relations
	 * @return a run with those providers
	 */
	public QvtrRun withImplementationProviders(List<RelationImplementationProvider> providers) {
		return new QvtrRun(transformation, context, config, providers, positionLookup);
	}

	/**
	 * The same run with the given source positions.
	 *
	 * @param positionLookup where a node stood in its source
	 * @return a run that can place its diagnostics
	 */
	public QvtrRun withPositionLookup(Function<EObject, SourcePosition> positionLookup) {
		return new QvtrRun(transformation, context, config, implementationProviders, positionLookup);
	}
}
