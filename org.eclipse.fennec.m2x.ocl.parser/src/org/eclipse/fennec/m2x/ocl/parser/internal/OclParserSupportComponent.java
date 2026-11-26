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
package org.eclipse.fennec.m2x.ocl.parser.internal;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * Publishes {@link OclParserSupport} as the {@link OclExpressionParser} service, bound to the
 * {@link ResourceSet} that {@code emf.osgi} registers. Where several resource sets are published,
 * {@code resourceSet.target} picks one.
 *
 * <p>The component lives in its own class so that it has a single public constructor at the
 * {@code init} arity — see #437.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component(
		name = "org.eclipse.fennec.m2x.ocl.parser.OclParserSupport",
		service = OclExpressionParser.class,
		scope = ServiceScope.PROTOTYPE,
		property = "parser.type=DEFAULT")
public class OclParserSupportComponent extends OclParserSupport {

	@Activate
	public OclParserSupportComponent(
			@Reference(name = "resourceSet", scope = ReferenceScope.PROTOTYPE_REQUIRED) ResourceSet resourceSet) {
		super(resourceSet);
	}
}
