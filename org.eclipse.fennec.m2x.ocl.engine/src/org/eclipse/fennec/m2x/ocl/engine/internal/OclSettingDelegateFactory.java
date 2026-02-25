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

import java.util.Objects;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicSettingDelegate;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * EMF {@link EStructuralFeature.Internal.SettingDelegate.Factory} that evaluates
 * OCL derivation expressions annotated on derived EStructuralFeatures.
 *
 * <p>Reads the OCL expression from the EAnnotation with source
 * {@value OclDelegateUtil#DELEGATE_URI}, detail key {@code "derivation"}.
 *
 * <p>Derived features are read-only by default. Attempts to set, unset, or
 * modify the inverse of a derived feature throw {@link UnsupportedOperationException}.
 *
 * <p>In an OSGi environment, this factory is registered as a service with
 * properties matching the emf.osgi delegate registry whiteboard pattern.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component(
		service = EStructuralFeature.Internal.SettingDelegate.Factory.class,
		property = {
				"emf.configuratorName=" + OclDelegateUtil.DELEGATE_URI,
				"emf.name=fennec-ocl",
				"configuratorType=SETTING_DELEGATE_FACTORY"
		})
public class OclSettingDelegateFactory implements EStructuralFeature.Internal.SettingDelegate.Factory {

	private final OclEngineImpl engine;

	/**
	 * DS constructor — engine is injected as a service.
	 */
	@Activate
	public OclSettingDelegateFactory(@Reference OclEngineImpl engine) {
		this.engine = Objects.requireNonNull(engine, "engine must not be null");
	}

	@Override
	public EStructuralFeature.Internal.SettingDelegate createSettingDelegate(
			EStructuralFeature eStructuralFeature) {
		String derivation = OclDelegateUtil.getAnnotationDetail(eStructuralFeature, "derivation");
		if (derivation == null) {
			derivation = OclDelegateUtil.getAnnotationDetail(eStructuralFeature, "initial");
		}
		if (derivation == null) {
			throw new IllegalStateException(
					"No OCL derivation/initial annotation on feature: "
							+ eStructuralFeature.getEContainingClass().getName()
							+ "." + eStructuralFeature.getName());
		}
		return new OclSettingDelegate(eStructuralFeature, derivation);
	}

	private class OclSettingDelegate extends BasicSettingDelegate.Stateless {

		private final String derivation;
		private volatile OclExpression parsed;

		OclSettingDelegate(EStructuralFeature feature, String derivation) {
			super(feature);
			this.derivation = derivation;
		}

		@Override
		protected Object get(InternalEObject owner, boolean resolve, boolean coreType) {
			try {
				OclExpression expression = getParsedExpression();
				OclContext context = OclContext.of(owner);
				return engine.evaluate(expression, context, engine.getDelegateOptions());
			} catch (Exception e) {
				throw new IllegalStateException(
						"OCL derivation failed for " + eStructuralFeature.getName()
								+ ": " + e.getMessage(), e);
			}
		}

		@Override
		protected boolean isSet(InternalEObject owner) {
			return true;
		}

		private OclExpression getParsedExpression() throws OclParseException {
			OclExpression result = parsed;
			if (result == null) {
				synchronized (this) {
					result = parsed;
					if (result == null) {
						result = engine.parse(derivation,
								eStructuralFeature.getEContainingClass());
						parsed = result;
					}
				}
			}
			return result;
		}
	}
}
