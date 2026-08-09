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
package org.eclipse.fennec.m2x.qvtd.engine;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Configuration of the QVT-R engine component.
 *
 * <p>Only QVT-R's own settings live here. The OCL engine that evaluates the expressions
 * inside the relations is a service the component references, so its limits are configured
 * where they belong — on {@code DefaultOclEngine} through {@code ocl.*} — and are not
 * mirrored into a second place that could disagree with the first.
 *
 * <p>Blackbox libraries and unit resolvers are off by default: both let a transformation
 * reach code and files outside itself, so switching them on is a decision. The allow-lists
 * narrow what may be reached once they are on — an empty one does not narrow anything, so
 * enabling a feature without also naming what it may reach permits every name.
 *
 * @since 1.0
 */
@ObjectClassDefinition(name = "QVT-R Engine Configuration", description = "Defaults for QVT-Relations transformations")
public @interface QvtdEngineConfiguration {

	String PREFIX_ = "qvtd.";

	@AttributeDefinition(name = "Blackbox Enabled", description = "Whether transformations may call Java blackbox libraries")
	boolean blackboxEnabled() default false;

	@AttributeDefinition(name = "Allowed Blackbox Modules", description = "Qualified names a transformation may import as a blackbox; empty puts no restriction on the names")
	String[] allowedBlackboxModules() default {};

	@AttributeDefinition(name = "Unit Resolver Enabled", description = "Whether transformations may import units resolved from outside")
	boolean unitResolverEnabled() default false;

	@AttributeDefinition(name = "Allowed Unit Modules", description = "Qualified names a transformation may import as a unit; empty puts no restriction on the names")
	String[] allowedUnitModules() default {};

	@AttributeDefinition(name = "Max Blackbox Libraries", description = "Maximum number of blackbox libraries one engine will use")
	int maxBlackboxLibraries() default 10;

	@AttributeDefinition(name = "Max Unit Resolvers", description = "Maximum number of unit resolvers one engine will use")
	int maxUnitResolvers() default 5;

	@AttributeDefinition(name = "Max Relation Depth", description = "Maximum recursion depth of relation invocation (§7.10)")
	int maxRelationDepth() default 200;

	@AttributeDefinition(name = "Max Bindings", description = "Maximum number of variable bindings one relation may produce")
	int maxBindings() default 10_000;

	@AttributeDefinition(name = "Timeout (ms)", description = "Transformation timeout in milliseconds (0 = no timeout)")
	long timeout() default 0;

	@AttributeDefinition(name = "Max Trace Records", description = "Maximum number of trace records one transformation may create")
	int maxTraceRecords() default 100_000;
}
