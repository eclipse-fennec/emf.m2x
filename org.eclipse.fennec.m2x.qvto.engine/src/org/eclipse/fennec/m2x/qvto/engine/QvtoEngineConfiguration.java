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
package org.eclipse.fennec.m2x.qvto.engine;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Configuration of the QVT-O engine component.
 *
 * <p>Only QVT-O's own settings live here. The OCL engine that evaluates the expressions
 * inside the transformations is a service the component references, so its limits are
 * configured where they belong — on {@code DefaultOclEngine} through {@code ocl.*} — and
 * are not mirrored into a second place that could disagree with the first.
 *
 * <p>Blackbox libraries and unit resolvers are off by default and gated by an allow-list:
 * both let a transformation reach code and files outside itself, so switching them on is a
 * decision, not a default.
 *
 * @since 1.0
 */
@ObjectClassDefinition(name = "QVT-O Engine Configuration", description = "Defaults for QVT-Operational transformations")
public @interface QvtoEngineConfiguration {

	String PREFIX_ = "qvto.";

	@AttributeDefinition(name = "Blackbox Enabled", description = "Whether transformations may call Java blackbox libraries")
	boolean blackboxEnabled() default false;

	@AttributeDefinition(name = "Allowed Blackbox Modules", description = "Qualified names a transformation may import as a blackbox; empty allows none")
	String[] allowedBlackboxModules() default {};

	@AttributeDefinition(name = "Unit Resolver Enabled", description = "Whether transformations may import units resolved from outside")
	boolean unitResolverEnabled() default false;

	@AttributeDefinition(name = "Allowed Unit Modules", description = "Qualified names a transformation may import as a unit; empty allows none")
	String[] allowedUnitModules() default {};

	@AttributeDefinition(name = "Max Blackbox Libraries", description = "Maximum number of blackbox libraries one engine will use")
	int maxBlackboxLibraries() default 10;

	@AttributeDefinition(name = "Max Unit Resolvers", description = "Maximum number of unit resolvers one engine will use")
	int maxUnitResolvers() default 5;
}
