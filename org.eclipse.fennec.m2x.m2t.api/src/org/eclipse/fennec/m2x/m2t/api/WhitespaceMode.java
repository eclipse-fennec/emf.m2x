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
package org.eclipse.fennec.m2x.m2t.api;

/**
 * Controls MOFM2T §8.4 whitespace handling behavior.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public enum WhitespaceMode {

	/**
	 * No whitespace normalization — raw template output.
	 * Body text is output exactly as written in the template source.
	 */
	NONE,

	/**
	 * MOFM2T §8.4 strict mode.
	 *
	 * <p>Applies all spec rules:
	 * <ul>
	 *   <li>Body-trimming for templates and multi-line blocks</li>
	 *   <li>Standalone block leading-whitespace stripping</li>
	 *   <li>Default {@code "\n"} separator injection for standalone for-blocks without explicit separator</li>
	 *   <li>BOL indicator {@code ^} processing</li>
	 *   <li>Indent-propagation for standalone template invocations</li>
	 * </ul>
	 */
	SPEC,

	/**
	 * Acceleo 3.7 compatible mode (default).
	 *
	 * <p>Currently identical to {@link #SPEC}. Reserved for future Acceleo-specific
	 * whitespace behavior differences (e.g. trailing whitespace handling).
	 *
	 * <p>This is the recommended default for compatibility with existing templates.
	 */
	ACCELEO
}
