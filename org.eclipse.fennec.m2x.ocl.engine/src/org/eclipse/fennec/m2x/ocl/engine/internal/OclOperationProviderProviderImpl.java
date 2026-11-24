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

import java.util.Collections;
import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.engine.OclOperationProviderProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Tracks the registered {@link OclOperationProvider} services and republishes itself
 * under {@link OclOperationProviderProvider} whenever the set changes, so that a target
 * filter on {@link OclOperationProviderProvider#OPERATION_PROVIDER_NAMES} can hold a
 * component back until the providers it needs are there.
 *
 * <p>The service is registered by hand rather than by {@code @Component(service=...)}
 * because its properties have to change while it stays the same service: re-registering
 * would briefly withdraw it and take every component gated on it down with it.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component(immediate = true, service = {})
public class OclOperationProviderProviderImpl implements OclOperationProviderProvider {

	private static final Logger LOG = Logger.getLogger(OclOperationProviderProviderImpl.class.getName());

	/** The property an {@link OclOperationProvider} is named by. */
	static final String PROVIDER_NAME = "provider.name";

	/** Keyed by provider so an unbind removes the one that went, not a namesake. */
	private final Map<OclOperationProvider, String> names = Collections.synchronizedMap(new LinkedHashMap<>());
	private final ServiceRegistration<OclOperationProviderProvider> registration;

	@Activate
	public OclOperationProviderProviderImpl(BundleContext ctx) {
		registration = ctx.registerService(OclOperationProviderProvider.class, this, serviceProperties());
	}

	@Deactivate
	void deactivate() {
		registration.unregister();
	}

	/**
	 * Takes note of a registered provider.
	 *
	 * <p>A provider without a {@link #PROVIDER_NAME} property is logged and ignored: there
	 * is nothing to wait for it by. It is still bound by the components that take providers
	 * — this component decides nothing about that.
	 *
	 * @param provider   the registered provider
	 * @param properties its service properties
	 */
	@Reference(name = "operationProvider", cardinality = ReferenceCardinality.MULTIPLE,
			policy = ReferencePolicy.DYNAMIC)
	void addOclOperationProvider(OclOperationProvider provider, Map<String, Object> properties) {
		if (!(properties.get(PROVIDER_NAME) instanceof String name)) {
			LOG.log(Level.WARNING, () -> "The OclOperationProvider " + provider.getClass().getName()
					+ " carries no " + PROVIDER_NAME + " property and cannot be waited for.");
			return;
		}
		names.put(provider, name);
		republish();
	}

	/**
	 * Forgets a provider that went away.
	 *
	 * @param provider   the provider that went away
	 * @param properties its service properties
	 */
	void removeOclOperationProvider(OclOperationProvider provider, Map<String, Object> properties) {
		if (names.remove(provider) != null) {
			republish();
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.fennec.m2x.ocl.engine.OclOperationProviderProvider#getOperationProviderNames()
	 */
	@Override
	public Set<String> getOperationProviderNames() {
		synchronized (names) {
			return Set.copyOf(names.values());
		}
	}

	private void republish() {
		registration.setProperties(serviceProperties());
	}

	private Dictionary<String, ?> serviceProperties() {
		List<String> current;
		synchronized (names) {
			current = List.copyOf(names.values());
		}
		return FrameworkUtil.asDictionary(Map.of(OPERATION_PROVIDER_NAMES, current));
	}
}
