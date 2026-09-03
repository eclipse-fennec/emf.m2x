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
package org.eclipse.fennec.m2x.m2t.engine.internal;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.emf.osgi.fingerprint.FingerprintService;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tUnitResolver;
import org.eclipse.fennec.m2x.m2t.engine.M2tConfigurationHelper;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngineConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Publishes the {@link M2tEngine} as a prototype-scoped service.
 *
 * <p>The configuration policy is optional, so there is a working M2T engine without any
 * configuration at all, and configuring it does not mean registering a different one.
 *
 * <p>The OCL engine is a mandatory reference, not something this component builds. There is
 * always one to bind — {@code DefaultOclEngine} is optional-policy too — and taking it as a
 * service is what makes the engine a consumer configured, with its cache and its providers,
 * the engine that actually runs the templates. It is bound prototype-required so that every
 * M2T engine gets its own, rather than sharing one accessor cache across unrelated consumers.
 *
 * <p>OCL limits are therefore configured on {@code DefaultOclEngine} through {@code ocl.*};
 * this component's own configuration only carries M2T's settings:
 * <pre>
 * "DefaultM2tEngine": { "m2t.whitespaceMode": "SPEC" },
 * "DefaultOclEngine": { "ocl.maxDepth": 500 }
 * </pre>
 *
 * <p>The reference is named {@code oclEngine}, so a consumer that wants a particular one
 * can say so — {@code "oclEngine.target": "(component.name=MyTunedOclEngine)"} — rather than
 * having to know the number bnd would otherwise assign it.
 *
 * <p>Unit resolvers are not a whiteboard here. A template names what it extends or imports,
 * so {@code M2tServiceUnitResolver} looks that name up in the service registry at link time
 * — which is why binding it is one mandatory reference rather than a set of resolvers that
 * would restart this component, and drop its caches, whenever one came or went. It is only
 * consulted when {@code m2t.discoverUnitResolvers} is on.
 *
 * <p>The resource set is a mandatory reference too — deliberately not an optional one with a
 * fallback to the global registry. Under {@code emf.osgi} a metamodel registered for the
 * resource set ({@code emf.model.scope=resourceset}) lands in the resource set's package
 * registry and never in the global one. An engine built with the fallback would resolve a
 * {@code modeltype} to the wrong package instance and match nothing, silently (#245); and an
 * optional reference cannot say when the service arrives, only that it was not there at
 * activation. A mandatory reference means the engine exists once the resource set does. It is
 * bound prototype-required so that every engine has its own, and
 * {@code "resourceSet.target"} picks one where several are published.
 *
 * <p>The fingerprint service is mandatory for the same reason: a constructor reference is
 * either there or the component waits — there is no later moment at which an optional one
 * could be re-bound without rebuilding the engine.
 *
 * @since 1.0
 */
@Designate(ocd = M2tEngineConfiguration.class)
@Component(name = "DefaultM2tEngine", service = M2tEngine.class, scope = ServiceScope.PROTOTYPE, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class M2tEngineComponent extends M2tEngineImpl {

	@Activate
	public M2tEngineComponent(
			M2tEngineConfiguration config,
			@Reference(name = "resourceSet", scope = ReferenceScope.PROTOTYPE_REQUIRED) ResourceSet resourceSet,
			@Reference(name = "oclEngine", scope = ReferenceScope.PROTOTYPE_REQUIRED) OclEngine oclEngine,
			@Reference(name = "unitDiscovery",
					target = "(" + M2tServiceUnitResolver.RESOLVER_KIND + "=discovery)")
			M2tUnitResolver unitDiscovery,
			@Reference(name = "fingerprints") FingerprintService fingerprints) {
		super(M2tConfigurationHelper.from(config, oclEngine, unitDiscovery, resourceSet),
				new UnitPackager(fingerprints));
	}
}
