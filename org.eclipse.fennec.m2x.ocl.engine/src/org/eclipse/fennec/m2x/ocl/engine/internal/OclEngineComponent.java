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
package org.eclipse.fennec.m2x.ocl.engine.internal;

import org.eclipse.fennec.m2x.ocl.engine.OclConfigurationHelper;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.metatype.annotations.Designate;

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
 * <p>The expression cache and operation provider can be targeted via OSGi Configurator:
 * <pre>
 * "org.eclipse.fennec.m2x.ocl.engine.OclEngineComponent": {
 *     "expressionCache.target": "(cache.name=myCustomCache)",
 *     "operationProvider.target": "(provider.name=myProvider)"
 * }
 * </pre>
 *
 * <p>By default, a {@link NoOpOclOperationProvider} is bound (no custom operations).
 * To use custom operations, register a custom {@link org.eclipse.fennec.m2x.ocl.api.OclOperationProvider}
 * and configure the target filter to select it. The {@code customOperationsEnabled} gate
 * must also be set to {@code true} for operations to be resolved at evaluation time.
 *
 * <p>Engine-wide defaults (security limits, null handling, error recovery) can
 * be configured via ConfigurationAdmin using {@code ocl.*} properties:
 * <pre>
 * "DefaultOclEngine": {
 *     "ocl.maxDepth": 500,
 *     "ocl.nullHandling": "LENIENT"
 * }
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Designate(ocd = OclEngineConfiguration.class)
@Component(name="DefaultOclEngine", service = { OclEngine.class, OclEngineImpl.class }, scope = ServiceScope.PROTOTYPE, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class OclEngineComponent extends OclEngineImpl {

	@Activate
	public OclEngineComponent(
			OclEngineConfiguration config,
			@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED) OclExpressionParser parser,
			@Reference(name = "expressionCache") OclExpressionCache cache,
			@Reference(name = "operationProvider") OclOperationProvider operationProvider) {
		super(OclConfigurationHelper.from(config, parser, cache, operationProvider));
	}
}
