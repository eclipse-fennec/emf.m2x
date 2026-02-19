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
package org.eclipse.fennec.m2m.ocl.engine;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import org.eclipse.fennec.m2m.ocl.api.OclEngine;
import org.eclipse.fennec.m2m.ocl.api.OclExpressionParser;

/**
 * OSGi Declarative Services component that publishes the {@link OclEngine}
 * as a service and registers EMF delegates on activation.
 *
 * <p>The parser is injected via {@code @Reference}. When the component
 * activates, it installs EMF delegates so that Ecore models annotated
 * with the Fennec OCL delegate URI are automatically evaluated.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component(service = OclEngine.class)
public class OclEngineComponent extends OclEngineImpl {

	@Activate
	public OclEngineComponent(@Reference OclExpressionParser parser) {
		super(parser);
		installDelegates();
	}

	@Deactivate
	void deactivate() {
		uninstallDelegates();
	}
}
