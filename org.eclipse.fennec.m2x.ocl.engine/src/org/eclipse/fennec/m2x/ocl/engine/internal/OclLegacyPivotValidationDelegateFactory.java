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

import org.eclipse.emf.ecore.EValidator;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Registers the Fennec OCL validation delegate under the legacy Eclipse OCL
 * Pivot delegate URI ({@value OclDelegateUtil#LEGACY_PIVOT_URI}).
 *
 * <p>This is a thin OSGi wrapper around {@link OclValidationDelegateFactory}: the
 * behaviour is identical, only the {@code emf.configuratorName} whiteboard
 * property differs. The emf.osgi delegate registry reads
 * {@code emf.configuratorName} as a single value, so a separate component per
 * served URI is required rather than a multi-valued property. The shared
 * expression-lookup logic in {@link OclDelegateUtil#getAnnotationDetail} already
 * resolves annotations across all {@linkplain OclDelegateUtil#SERVED_URIS served URIs}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component(
		service = EValidator.ValidationDelegate.class,
		property = {
				"emf.configuratorName=" + OclDelegateUtil.LEGACY_PIVOT_URI,
				"emf.name=fennec-ocl-pivot",
				"emf.configuratorType=VALIDATION_DELEGATE"
		})
public class OclLegacyPivotValidationDelegateFactory extends OclValidationDelegateFactory {

	/**
	 * DS constructor — engine is injected as a service.
	 */
	@Activate
	public OclLegacyPivotValidationDelegateFactory(@Reference OclEngineImpl engine) {
		super(engine);
	}
}
