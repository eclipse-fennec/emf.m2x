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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.fennec.m2x.ocl.api.CompleteOclContribution;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.engine.OclConfigurationHelper;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineConfiguration;
import org.eclipse.fennec.m2x.ocl.engine.OclOperationProviderProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
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
 * "DefaultOclEngine": {
 *     "parser.target": "(...)",
 *     "expressionCache.target": "(cache.name=myCustomCache)",
 *     "operationProvider.target": "(provider.name=myProvider)"
 * }
 * </pre>
 *
 * <p>Every registered {@link org.eclipse.fennec.m2x.ocl.api.OclOperationProvider} is bound: 
 * a runtime can hold several at once — the MOFM2T standard library, a set
 * of domain operations, the operations a generator brings — and they have no reason to
 * exclude each other.
 *
 * <p>The {@code customOperationsEnabled} gate still decides whether config-registered
 * operations are resolved at evaluation time; binding a provider does not switch it on.
 *
 * <p>The reference is dynamic, so a provider that comes up after the engine reaches the
 * engine that is already running, and the engine is not rebuilt for it.
 *
 * <p>That is also why binding alone cannot tell a consumer its provider has arrived: a
 * multiple reference is satisfied by nothing at all, so the engine is published whether or
 * not the targeted provider is there yet. A consumer that needs one to be there targets
 * {@link OclOperationProviderProvider} as well, which stays unsatisfied until the names it
 * asks for are registered:
 * <pre>
 * "DefaultOclEngine~myEngine": {
 *     "operationProvider.target": "(|(provider.name=A)(provider.name=B))",
 *     "operationProvidersReady.target": "(&amp;(operationProviderNames=A)(operationProviderNames=B))"
 * }
 * </pre>
 * The two filters are independent: the first decides which providers bind, the second only
 * decides when the engine appears. Nothing checks that they agree.
 *
 * <p>Nothing is lost by letting the set change. No resolved operation is held anywhere:
 * the providers are snapshotted when an evaluation starts and dispatched against at call
 * time, so a bind or unbind is seen by the next evaluation and never by one in flight.
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
@Component(name="DefaultOclEngine", service = { OclEngine.class, OclDelegateSupport.class }, scope = ServiceScope.PROTOTYPE, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class OclEngineComponent extends OclEngineImpl {

	private static final Logger LOG = Logger.getLogger(OclEngineComponent.class.getName());

	/**
	 * @param config                  the engine configuration
	 * @param parser                  the OCL expression parser
	 * @param cache                   the shared expression cache
	 * @param operationProvidersReady the readiness gate; referenced for its satisfiability
	 *                                only and deliberately unused in the body
	 */
	@Activate
	public OclEngineComponent(
			OclEngineConfiguration config,
			@Reference(name = "parser", scope = ReferenceScope.PROTOTYPE_REQUIRED) OclExpressionParser parser,
			@Reference(name = "expressionCache") OclExpressionCache cache,
			@Reference(name = "operationProvidersReady") OclOperationProviderProvider operationProvidersReady) {
		super(OclConfigurationHelper.from(config, parser, cache));
	}

	/**
	 * Puts a Complete OCL document published as a service into effect (#195).
	 *
	 * <p>What the {@link CompleteOclContribution} javadoc has promised since 1.0 and nothing
	 * implemented: a bundle ships constraints and {@code def:} operations for a metamodel it
	 * does not own, and the engine picks them up. The reference is dynamic with multiple
	 * cardinality, which binds a service that arrives later whichever policy option is in
	 * force (§112.3.6), so a contribution that arrives later applies without rebuilding the
	 * engine — as for the operation providers below, a document is loaded and can be
	 * unloaded again.
	 *
	 * <p>A document that cannot be parsed is logged and skipped. The programmatic path throws,
	 * because there the caller asked for this document; a bind cannot — refusing it would
	 * leave the component unsatisfied and take the engine down with it.
	 *
	 * @param contribution the contributed document
	 */
	@Reference(name = "completeOclDocument", cardinality = ReferenceCardinality.MULTIPLE,
			policy = ReferencePolicy.DYNAMIC)
	void addCompleteOclDocument(CompleteOclContribution contribution) {
		try {
			registerCompleteOclDocument(contribution);
		} catch (RuntimeException failure) {
			LOG.log(Level.SEVERE, failure,
					() -> "The Complete OCL document of " + contribution.getDocumentUri()
							+ " was not put into effect: " + failure.getMessage());
		}
	}

	/**
	 * Takes a contributed document back out.
	 *
	 * @param contribution the contributed document
	 */
	void removeCompleteOclDocument(CompleteOclContribution contribution) {
		unregisterCompleteOclDocument(contribution);
	}

	/**
	 * Takes on an operation provider published as a service.
	 *
	 * <p>The {@code customOperationsEnabled} gate still decides whether these operations are
	 * resolved at evaluation time; binding a provider does not switch it on.
	 *
	 * <p>The method is not called {@code addOperationProvider}: that is the protected seam
	 * on {@link OclEngineImpl} this delegates to, and a package-private method cannot
	 * override it.
	 *
	 * @param operationProvider the provider to take on
	 */
	@Reference(name = "operationProvider", cardinality = ReferenceCardinality.MULTIPLE,
			policy = ReferencePolicy.DYNAMIC)
	void addOclOperationProvider(OclOperationProvider operationProvider) {
		addOperationProvider(operationProvider);
	}

	/**
	 * Takes an operation provider back out.
	 *
	 * <p>An evaluation that is already running keeps the providers it started with.
	 *
	 * @param operationProvider the provider to take out
	 */
	void removeOclOperationProvider(OclOperationProvider operationProvider) {
		removeOperationProvider(operationProvider);
	}
}
