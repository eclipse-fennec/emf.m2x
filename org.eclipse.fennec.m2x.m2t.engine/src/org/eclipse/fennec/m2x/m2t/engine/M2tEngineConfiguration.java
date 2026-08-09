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
package org.eclipse.fennec.m2x.m2t.engine;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Configuration of the M2T engine component.
 *
 * <p>Only M2T's own settings live here. The OCL engine that evaluates the expressions
 * inside the templates is a service the component references, so its limits are configured
 * where they belong — on {@code DefaultOclEngine} through {@code ocl.*} — and are not
 * mirrored into a second place that could disagree with the first.
 *
 * @since 1.0
 */
@ObjectClassDefinition(name = "M2T Engine Configuration", description = "Defaults for MOFM2T template generation")
public @interface M2tEngineConfiguration {

	String PREFIX_ = "m2t.";

	@AttributeDefinition(name = "Default Charset", description = "Charset for generated files that do not name one")
	String defaultCharset() default "UTF-8";

	@AttributeDefinition(name = "Whitespace Mode", description = "Whitespace handling: NONE, SPEC or ACCELEO")
	String whitespaceMode() default "ACCELEO";

	@AttributeDefinition(name = "Max Diagnostics", description = "Maximum diagnostics collected before generation gives up")
	int maxDiagnostics() default 10_000;

	@AttributeDefinition(name = "Max Template Depth", description = "Maximum nesting depth of template invocations")
	int maxTemplateDepth() default 1_000;

	@AttributeDefinition(name = "Max For Iterations", description = "Maximum iterations of a single for block")
	int maxForIterations() default 1_000_000;

	@AttributeDefinition(name = "Max Cross Product Size", description = "Maximum size of a for block over several collections")
	int maxCrossProductSize() default 1_000_000;

	@AttributeDefinition(name = "Max Output Size", description = "Maximum number of characters a generation may produce")
	long maxOutputSize() default 10_000_000L;

	@AttributeDefinition(name = "Protected Areas Enabled", description = "Whether protected area markers in existing files are honoured")
	boolean protectedAreaEnabled() default true;
}
