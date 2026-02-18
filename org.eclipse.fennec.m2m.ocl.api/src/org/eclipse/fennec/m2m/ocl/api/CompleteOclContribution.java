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
package org.eclipse.fennec.m2m.ocl.api;

import org.eclipse.emf.common.util.URI;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * Extension interface for contributing Complete OCL documents.
 *
 * <p>A Complete OCL document defines additional constraints, derived properties,
 * and operation bodies for existing classifiers without modifying the original
 * Ecore model.
 *
 * <p>Without OSGi, register contributions programmatically via
 * {@link OclEngine#registerCompleteOclDocument(CompleteOclContribution)}.
 *
 * <p>With OSGi, contributions are discovered automatically via the whiteboard pattern:
 * <pre>
 * {@literal @}Component(service = CompleteOclContribution.class)
 * public class MyOclDocument implements CompleteOclContribution {
 *     ...
 * }
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface CompleteOclContribution {

	/**
	 * Returns the URI identifying this Complete OCL document.
	 *
	 * @return the document URI, never {@code null}
	 */
	URI getDocumentUri();

	/**
	 * Returns the Complete OCL document text.
	 *
	 * @return the OCL document content, never {@code null}
	 */
	String getDocumentText();
}
