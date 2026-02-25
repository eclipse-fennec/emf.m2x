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

import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * OSGi Declarative Services component that publishes the {@link OclEngine}
 * as a prototype-scoped service.
 *
 * <p>Each consumer that injects {@code OclEngine} via {@code @Reference}
 * receives its own engine instance with an isolated property accessor cache.
 * All instances share the same expression cache (injected as a service).
 *
 * <p>EMF delegate registration is <b>not</b> handled by this component.
 * In an OSGi environment, the delegate factories ({@code OclInvocationDelegateFactory},
 * {@code OclSettingDelegateFactory}, {@code OclValidationDelegateFactory}) are
 * registered as separate services and wired into the emf.osgi delegate registries.
 * For standalone (non-OSGi) usage, call {@link OclEngineImpl#installDelegates()}.
 *
 * <p>The expression cache can be targeted via OSGi Configurator:
 * <pre>
 * "org.eclipse.fennec.m2x.ocl.engine.OclEngineComponent": {
 *     "expressionCache.target": "(cache.name=myCustomCache)"
 * }
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component(service = OclEngine.class, scope = ServiceScope.PROTOTYPE)
public class OclEngineComponent extends OclEngineImpl {

	@Activate
	public OclEngineComponent(
			@Reference OclExpressionParser parser,
			@Reference(name = "expressionCache") OclExpressionCache cache) {
		super(OclConfiguration.builder(parser).expressionCache(cache).build());
	}
}
