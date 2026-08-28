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

import org.eclipse.fennec.m2x.ocl.engine.internal.OclEngineComponent;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi Metatype configuration for the {@link OclEngineComponent}.
 *
 * <p>All properties use the {@code ocl.} prefix and map to
 * {@link org.eclipse.fennec.m2x.ocl.api.OclConfiguration} fields.
 * Defaults match {@link org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions#strict()}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ObjectClassDefinition(name = "OCL Engine Configuration", description = "Engine-wide defaults for OCL evaluation security limits and behavior")
public @interface OclEngineConfiguration {

	String PREFIX_ = "ocl.";

	@AttributeDefinition(name = "Max Depth", description = "Maximum recursion depth for OCL evaluation")
	int maxDepth() default 1000;

	@AttributeDefinition(name = "Max Collection Size", description = "Maximum collection size for ranges, products, allInstances")
	int maxCollectionSize() default 1_000_000;

	@AttributeDefinition(name = "Max Closure Iterations", description = "Maximum iterations for closure operations")
	int maxClosureIterations() default 100_000;

	@AttributeDefinition(name = "Max Regex Length", description = "Maximum regex pattern length for matches/replaceAll/replaceFirst")
	int maxRegexLength() default 1000;

	@AttributeDefinition(name = "Timeout (ms)", description = "Evaluation timeout in milliseconds (0 = no timeout)")
	long timeout() default 0;

	@AttributeDefinition(name = "Null Handling", description = "Null handling strategy: STRICT or LENIENT")
	String nullHandling() default "STRICT";

	@AttributeDefinition(name = "Error Recovery", description = "Error recovery strategy: FAIL_FAST or COLLECT_ERRORS")
	String errorRecovery() default "FAIL_FAST";

	@AttributeDefinition(name = "Custom Operations Enabled", description = "Whether config-registered custom operations are enabled")
	boolean customOperationsEnabled() default false;
}
