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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.fennec.m2x.ocl.api.CompleteOclContribution;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.engine.OclConfigurationHelper;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineConfiguration;
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
 * <p>Every registered {@link org.eclipse.fennec.m2x.ocl.api.OclOperationProvider} is bound,
 * not one of them: a runtime can hold several at once — the MOFM2T standard library, a set
 * of domain operations, the operations a generator brings — and they have no reason to
 * exclude each other. The reference used to be a single mandatory one, which meant a
 * placeholder had to exist for the common case of none, and that only one provider could
 * ever be in play.
 *
 * <p>The {@code customOperationsEnabled} gate still decides whether config-registered
 * operations are resolved at evaluation time; binding a provider does not switch it on.
 *
 * <p>The reference is static, so a provider appearing or disappearing rebuilds the engine.
 * That costs its property accessor cache and nothing else — the expression cache is a
 * service of its own and survives — and it keeps the set of operations fixed for the life
 * of an engine, which is what makes a resolved operation stay resolved.
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

	@Activate
	public OclEngineComponent(
			OclEngineConfiguration config,
			@Reference(name = "parser", scope = ReferenceScope.PROTOTYPE_REQUIRED) OclExpressionParser parser,
			@Reference(name = "expressionCache") OclExpressionCache cache,
			@Reference(name = "operationProvider", cardinality = ReferenceCardinality.MULTIPLE)
			List<OclOperationProvider> operationProviders) {
		super(OclConfigurationHelper.from(config, parser, cache, operationProviders));
	}

	/**
	 * Puts a Complete OCL document published as a service into effect (#195).
	 *
	 * <p>What the {@link CompleteOclContribution} javadoc has promised since 1.0 and nothing
	 * implemented: a bundle ships constraints and {@code def:} operations for a metamodel it
	 * does not own, and the engine picks them up. The reference is dynamic and greedy, so a
	 * contribution that arrives later applies without rebuilding the engine — unlike the
	 * operation providers above, whose set has to stay fixed for a resolved operation to stay
	 * resolved, a document is loaded and can be unloaded again.
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
}
