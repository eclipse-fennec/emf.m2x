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
package org.eclipse.fennec.m2x.qvto.engine.internal;

import org.eclipse.fennec.emf.osgi.fingerprint.FingerprintService;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.engine.QvtoConfigurationHelper;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngineConfiguration;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferenceScope;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Publishes the {@link QvtoEngine} as a prototype-scoped service.
 *
 * <p>The configuration policy is optional, so there is a working QVT-O engine without any
 * configuration at all, and configuring it does not mean registering a different one.
 *
 * <p>The OCL engine is a mandatory reference, not something this component builds. There is
 * always one to bind — {@code DefaultOclEngine} is optional-policy too — and taking it as a
 * service is what makes the engine a consumer configured, with its cache and its providers,
 * the engine that actually evaluates the transformation. It is bound prototype-required so
 * that every QVT-O engine gets its own, rather than sharing one accessor cache across
 * unrelated consumers.
 *
 * <p>OCL limits are therefore configured on {@code DefaultOclEngine} through {@code ocl.*};
 * this component's own configuration only carries QVT-O's settings:
 * <pre>
 * "DefaultQvtoEngine": { "qvto.blackboxEnabled": true },
 * "DefaultOclEngine":  { "ocl.maxDepth": 500 }
 * </pre>
 *
 * <p>The reference is named {@code oclEngine}, so a consumer that wants a particular one
 * can say so — {@code "oclEngine.target": "(component.name=MyTunedOclEngine)"} — rather than
 * having to know the number bnd would otherwise assign it.
 *
 * <p>Unit resolvers are not a whiteboard here. A transformation names what it imports, so
 * {@code QvtoServiceUnitResolver} looks that name up in the service registry at link time —
 * which is why binding it is one mandatory reference rather than a set of resolvers that
 * would restart this component, and drop its caches, whenever one came or went. It is only
 * consulted when {@code qvto.discoverUnitResolvers} is on.
 *
 * @since 1.0
 */
@Designate(ocd = QvtoEngineConfiguration.class)
@Component(name = "DefaultQvtoEngine", service = QvtoEngine.class, scope = ServiceScope.PROTOTYPE, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class QvtoEngineComponent extends QvtoEngineImpl {

	@Activate
	public QvtoEngineComponent(
			QvtoEngineConfiguration config,
			@Reference(name = "oclEngine", scope = ReferenceScope.PROTOTYPE_REQUIRED) OclEngine oclEngine,
			@Reference(name = "unitDiscovery",
					target = "(" + QvtoServiceUnitResolver.RESOLVER_KIND + "=discovery)")
			QvtoUnitResolver unitDiscovery,
			@Reference(name = "fingerprints", cardinality = ReferenceCardinality.OPTIONAL)
			FingerprintService fingerprints) {
		super(QvtoConfigurationHelper.from(config, oclEngine, unitDiscovery),
				fingerprints == null ? UnitPackager.withDefaults() : new UnitPackager(fingerprints));
	}
}
