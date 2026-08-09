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

import org.eclipse.fennec.m2x.ocl.api.OclDelegates;

import java.util.List;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;

/**
 * Utility for reading OCL expressions from Ecore annotations.
 *
 * <p>EMF delegates store OCL expressions in EAnnotations with source
 * {@value #DELEGATE_URI}. Each annotation detail holds an expression
 * keyed by its role (e.g. {@code "body"}, {@code "derivation"}).
 *
 * <p>In addition to the native Fennec URI, the engine also serves the legacy
 * Eclipse OCL Pivot delegate URI ({@value #LEGACY_PIVOT_URI}) so that models
 * authored against Eclipse OCL — whose derived features, operation bodies, and
 * constraints are annotated under that URI — evaluate with the Fennec engine.
 * The Fennec engine is a superset of the Eclipse OCL expression handling, so no
 * dialect translation is needed; only the annotation source differs.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclDelegateUtil {

	/** The Fennec OCL delegate URI. */
	public static final String DELEGATE_URI = OclDelegates.DELEGATE_URI;

	/** The legacy Eclipse OCL Pivot delegate URI, also served by the Fennec engine. */
	public static final String LEGACY_PIVOT_URI = OclDelegates.LEGACY_PIVOT_URI;

	/**
	 * The delegate URIs served by the Fennec OCL engine, in priority order.
	 *
	 * <p>The Fennec URI is preferred; the legacy Pivot URI is consulted as a
	 * fallback. Additional URIs can be appended here to broaden interop without
	 * touching the factories or registration logic.
	 */
	public static final List<String> SERVED_URIS = List.of(DELEGATE_URI, LEGACY_PIVOT_URI);

	private OclDelegateUtil() {
	}

	/**
	 * Reads a detail value from an OCL delegate annotation on a model element.
	 *
	 * <p>The {@linkplain #SERVED_URIS served URIs} are consulted in order; the
	 * first annotation that supplies a value for {@code detailKey} wins. This lets
	 * the same factory serve both the Fennec URI and the legacy Pivot URI without
	 * knowing which one a given model used.
	 *
	 * @param element the annotated model element (EOperation, EStructuralFeature, EClass)
	 * @param detailKey the detail key (e.g. "body", "derivation", constraint name)
	 * @return the detail value, or {@code null} if not present under any served URI
	 */
	static String getAnnotationDetail(EModelElement element, String detailKey) {
		for (String uri : SERVED_URIS) {
			EAnnotation annotation = element.getEAnnotation(uri);
			if (annotation == null) {
				continue;
			}
			String value = annotation.getDetails().get(detailKey);
			if (value != null) {
				return value;
			}
		}
		return null;
	}
}
