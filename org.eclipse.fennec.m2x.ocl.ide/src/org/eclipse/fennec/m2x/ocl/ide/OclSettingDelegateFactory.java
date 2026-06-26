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
package org.eclipse.fennec.m2x.ocl.ide;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2x.ocl.engine.OclDelegateFactories;

/**
 * Extension-registry entry point for the OCL setting (derivation) delegate.
 *
 * <p>Registered by {@code plugin.xml} on the
 * {@code org.eclipse.emf.ecore.setting_delegate} extension point. EMF
 * instantiates this factory via its public no-argument constructor and asks it
 * for a setting delegate whenever a derived feature carrying an OCL
 * {@code derivation}/{@code initial} annotation is read in the generic EMF
 * editor.
 *
 * <p>The actual evaluation is delegated to the Fennec OCL engine via
 * {@link OclDelegateFactories}; this class only adapts the engine factory to
 * the registry's no-argument instantiation contract.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclSettingDelegateFactory implements EStructuralFeature.Internal.SettingDelegate.Factory {

	private final EStructuralFeature.Internal.SettingDelegate.Factory delegate =
			OclDelegateFactories.newSettingDelegateFactory(SharedOclEngine.get());

	/**
	 * Public no-argument constructor required by the Eclipse extension registry.
	 */
	public OclSettingDelegateFactory() {
		// instantiated reflectively by the extension registry
	}

	@Override
	public EStructuralFeature.Internal.SettingDelegate createSettingDelegate(
			EStructuralFeature eStructuralFeature) {
		return delegate.createSettingDelegate(eStructuralFeature);
	}
}
