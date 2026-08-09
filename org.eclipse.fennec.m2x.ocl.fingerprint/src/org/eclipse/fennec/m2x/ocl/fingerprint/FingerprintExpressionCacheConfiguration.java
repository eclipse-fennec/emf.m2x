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
package org.eclipse.fennec.m2x.ocl.fingerprint;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Configuration of the fingerprint-keyed expression cache.
 *
 * @since 1.0
 */
@ObjectClassDefinition(name = "OCL Fingerprint Expression Cache",
		description = "Caches compiled OCL keyed by the model fingerprint rather than the nsURI")
public @interface FingerprintExpressionCacheConfiguration {

	String PREFIX_ = "ocl.fingerprint.";

	@AttributeDefinition(name = "Max Size", description = "Entries kept before the least recently used is dropped")
	int maxSize() default 1024;
}
