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
package org.eclipse.fennec.m2x.ocl.metadata;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Configuration of the registry-backed expression cache.
 *
 * @since 1.0
 */
@ObjectClassDefinition(name = "OCL Metadata Expression Cache",
		description = "Keeps compiled OCL in an EObject registry, anchored to the model version, with an LRU for what has no version to anchor to")
public @interface OclMetadataCacheConfiguration {

	String PREFIX_ = "ocl.metadata.";

	@AttributeDefinition(name = "LRU Size", description = "Entries the in-memory cache keeps: context types with no package to fingerprint, and parses with declared variables")
	int lruSize() default 256;

	@AttributeDefinition(name = "LRU Failure Size", description = "Parse failures kept in memory (never in the registry), in an LRU of their own so broken expressions cannot displace working ones; 0 keeps none")
	int lruFailureSize() default 256;
}
