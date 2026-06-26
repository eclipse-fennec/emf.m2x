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
package org.eclipse.fennec.m2x.ocl.engine;

import java.util.Objects;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclDelegateUtil;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclInvocationDelegateFactory;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclSettingDelegateFactory;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclValidationDelegateFactory;

/**
 * Public provider for the engine's EMF delegate implementations.
 *
 * <p>The concrete delegate classes live in the engine's non-exported
 * {@code internal} package and are wired for OSGi Declarative Services via
 * constructor injection. This provider exposes them as their plain EMF
 * interface types so that an integration bundle (for example the Eclipse
 * extension-registry contribution in {@code org.eclipse.fennec.m2x.ocl.ide})
 * can register them with the standard EMF delegate registries
 * ({@code org.eclipse.emf.ecore.{validation,setting,invocation}_delegate})
 * <em>without</em> the engine having to export its internals or duplicate the
 * delegate logic.
 *
 * <p>Each factory serves both {@linkplain #DELEGATE_URI the Fennec URI} and
 * {@linkplain #LEGACY_PIVOT_URI the legacy Eclipse OCL Pivot URI}; the
 * underlying delegates resolve annotations across both sources, so the same
 * instance can be registered under either URI.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class OclDelegateFactories {

	/** The Fennec OCL delegate URI. */
	public static final String DELEGATE_URI = OclDelegateUtil.DELEGATE_URI;

	/** The legacy Eclipse OCL Pivot delegate URI, also served by the Fennec engine. */
	public static final String LEGACY_PIVOT_URI = OclDelegateUtil.LEGACY_PIVOT_URI;

	private OclDelegateFactories() {
		// utility class
	}

	/**
	 * Creates a {@link EValidator.ValidationDelegate} backed by the given engine.
	 * Evaluates OCL invariant constraints annotated on EClasses and EOperations.
	 *
	 * @param engine the OCL engine that parses and evaluates the constraints
	 * @return a new validation delegate
	 */
	public static EValidator.ValidationDelegate newValidationDelegate(OclEngineImpl engine) {
		return new OclValidationDelegateFactory(Objects.requireNonNull(engine, "engine must not be null"));
	}

	/**
	 * Creates a {@link EStructuralFeature.Internal.SettingDelegate.Factory} backed
	 * by the given engine. Evaluates OCL derivation expressions for derived features.
	 *
	 * @param engine the OCL engine that parses and evaluates the derivations
	 * @return a new setting delegate factory
	 */
	public static EStructuralFeature.Internal.SettingDelegate.Factory newSettingDelegateFactory(OclEngineImpl engine) {
		return new OclSettingDelegateFactory(Objects.requireNonNull(engine, "engine must not be null"));
	}

	/**
	 * Creates a {@link EOperation.Internal.InvocationDelegate.Factory} backed by
	 * the given engine. Evaluates OCL operation bodies, pre- and postconditions.
	 *
	 * @param engine the OCL engine that parses and evaluates the operation bodies
	 * @return a new invocation delegate factory
	 */
	public static EOperation.Internal.InvocationDelegate.Factory newInvocationDelegateFactory(OclEngineImpl engine) {
		return new OclInvocationDelegateFactory(Objects.requireNonNull(engine, "engine must not be null"));
	}
}
