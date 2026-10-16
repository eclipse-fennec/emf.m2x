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
package org.eclipse.fennec.m2x.qvtd.engine.internal;

import java.util.Map;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.emf.osgi.fingerprint.FingerprintService;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxLibrary;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnitResolver;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdConfigurationHelper;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngineConfiguration;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
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
 * Publishes the {@link QvtdEngine} as a prototype-scoped service.
 *
 * <p>The configuration policy is optional, so there is a working QVT-R engine without any
 * configuration at all, and configuring it does not mean registering a different one.
 *
 * <p>The OCL engine is a mandatory reference, not something this component builds. There is
 * always one to bind — {@code DefaultOclEngine} is optional-policy too — and taking it as a
 * service is what makes the engine a consumer configured, with its cache and its providers,
 * the engine that actually evaluates the relations. It is bound prototype-required so that
 * every QVT-R engine gets its own, rather than sharing one accessor cache across unrelated
 * consumers.
 *
 * <p>OCL limits are therefore configured on {@code DefaultOclEngine} through {@code ocl.*};
 * this component's own configuration only carries QVT-R's settings:
 * <pre>
 * "DefaultQvtdEngine": { "qvtd.maxRelationDepth": 100 },
 * "DefaultOclEngine":  { "ocl.maxDepth": 500 }
 * </pre>
 *
 * <p>The reference is named {@code oclEngine}, so a consumer that wants a particular one
 * can say so — {@code "oclEngine.target": "(component.name=MyTunedOclEngine)"} — rather than
 * having to know the number bnd would otherwise assign it.
 *
 * <p>Blackbox libraries and unit resolvers are deliberately absent: a transformation names
 * what it imports, so the engine resolves those by name when it parses rather than having
 * them pushed in here (#90).
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
 * <p>Blackbox libraries are the opposite case, and bound the opposite way (#269): a dynamic
 * reference of cardinality {@code 0..n}, through bind and unbind methods. Every
 * {@code QvtdBlackboxLibrary} service is bound as it comes and dropped as it goes — a dynamic
 * multiple reference binds new services without the greedy option (Declarative Services
 * 112.3.7) — and the engine is never restarted for it; each run reads the libraries bound at
 * that moment. Unlike units, a transformation does not name its libraries: a query or an
 * {@code implementedby} is served by whichever library offers the operation, so the engine
 * needs to know the libraries it has, not only to look one up. Standard DS configuration
 * narrows and orders the binding:
 * <pre>
 * "DefaultQvtdEngine": {
 *   "qvtd.blackboxEnabled": true,
 *   "qvtd.allowedBlackboxModules": ["MyBlackbox"],
 *   "blackboxes.target": "(blackbox.group=reporting)",
 *   "blackboxes.cardinality.minimum": 1
 * }
 * </pre>
 * {@code blackboxes.cardinality.minimum} keeps the engine unavailable until that many libraries
 * are bound — the way to make a deployment wait for its libraries rather than run without
 * them. Binding grants nothing by itself: libraries are consulted only with
 * {@code qvtd.blackboxEnabled}, and only for the modules the allow-list permits.
 *
 * @since 1.0
 */
@Designate(ocd = QvtdEngineConfiguration.class)
@Component(name = "DefaultQvtdEngine", service = QvtdEngine.class, scope = ServiceScope.PROTOTYPE, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class QvtdEngineComponent extends QvtdEngineImpl {

	private final QvtdServiceBlackboxRegistry blackboxes;

	@Activate
	public QvtdEngineComponent(
			QvtdEngineConfiguration config,
			@Reference(name = "resourceSet", scope = ReferenceScope.PROTOTYPE_REQUIRED) ResourceSet resourceSet,
			@Reference(name = "oclEngine", scope = ReferenceScope.PROTOTYPE_REQUIRED) OclEngine oclEngine,
			@Reference(name = "unitDiscovery",
					target = "(" + QvtdServiceUnitResolver.RESOLVER_KIND + "=discovery)")
			QvtdUnitResolver unitDiscovery,
			@Reference(name = "fingerprints") FingerprintService fingerprints) {
		this(config, resourceSet, oclEngine, unitDiscovery, fingerprints,
				new QvtdServiceBlackboxRegistry());
	}

	private QvtdEngineComponent(QvtdEngineConfiguration config, ResourceSet resourceSet,
			OclEngine oclEngine, QvtdUnitResolver unitDiscovery, FingerprintService fingerprints,
			QvtdServiceBlackboxRegistry blackboxes) {
		super(QvtdConfigurationHelper.from(config, oclEngine, unitDiscovery, resourceSet, blackboxes),
				new UnitPackager(fingerprints));
		this.blackboxes = blackboxes;
	}

	/**
	 * Binds a blackbox library service.
	 *
	 * @param library    the library
	 * @param properties its service properties, for its ranking
	 */
	@Reference(name = "blackboxes", service = QvtdBlackboxLibrary.class,
			cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC,
			unbind = "removeBlackbox")
	void addBlackbox(QvtdBlackboxLibrary library, Map<String, Object> properties) {
		blackboxes.add(library, properties);
	}

	/**
	 * Unbinds a blackbox library service.
	 *
	 * @param library the library
	 */
	void removeBlackbox(QvtdBlackboxLibrary library) {
		blackboxes.remove(library);
	}
}
