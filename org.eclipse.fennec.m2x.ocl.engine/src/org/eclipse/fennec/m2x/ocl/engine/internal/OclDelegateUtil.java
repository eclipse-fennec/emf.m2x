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

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;

/**
 * Utility for reading OCL expressions from Ecore annotations.
 *
 * <p>EMF delegates store OCL expressions in EAnnotations with source
 * {@value #DELEGATE_URI}. Each annotation detail holds an expression
 * keyed by its role (e.g. {@code "body"}, {@code "derivation"}).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclDelegateUtil {

	/** The Fennec OCL delegate URI. */
	public static final String DELEGATE_URI = "http://www.eclipse.org/fennec/m2x/ocl/1.0";

	private OclDelegateUtil() {
	}

	/**
	 * Reads a detail value from the OCL delegate annotation on a model element.
	 *
	 * @param element the annotated model element (EOperation, EStructuralFeature, EClass)
	 * @param detailKey the detail key (e.g. "body", "derivation", constraint name)
	 * @return the detail value, or {@code null} if not present
	 */
	static String getAnnotationDetail(EModelElement element, String detailKey) {
		EAnnotation annotation = element.getEAnnotation(DELEGATE_URI);
		if (annotation == null) {
			return null;
		}
		return annotation.getDetails().get(detailKey);
	}
}
