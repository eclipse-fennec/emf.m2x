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

import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;

/**
 * Asks every {@link QvtoUnitResolver} on the class path, in the order
 * {@link ServiceLoader} presents them.
 *
 * <p>This is what discovery means outside OSGi. It is one resolver from the engine's point
 * of view — it counts once towards the resolver limit, and the allow-list is checked before
 * it is ever asked, so discovery cannot widen which names may be resolved, only who may
 * answer for them.
 *
 * <p>A provider that fails to load is skipped rather than allowed to abort the link: one
 * broken jar on the class path should not decide whether an unrelated import resolves.
 *
 * @since 1.0
 */
public final class ServiceLoaderUnitResolver implements QvtoUnitResolver {

	private static final Logger LOG = Logger.getLogger(ServiceLoaderUnitResolver.class.getName());

	private final ServiceLoader<QvtoUnitResolver> loader;

	public ServiceLoaderUnitResolver() {
		this(ServiceLoader.load(QvtoUnitResolver.class));
	}

	ServiceLoaderUnitResolver(ServiceLoader<QvtoUnitResolver> loader) {
		this.loader = loader;
	}

	@Override
	public Optional<QvtoUnit> resolveUnit(String qualifiedName) {
		for (QvtoUnitResolver resolver : loader) {
			// A provider that declared itself would otherwise recurse forever.
			if (resolver instanceof ServiceLoaderUnitResolver) {
				continue;
			}
			try {
				Optional<QvtoUnit> unit = resolver.resolveUnit(qualifiedName);
				if (unit.isPresent()) {
					return unit;
				}
			} catch (ServiceConfigurationError | RuntimeException failure) {
				LOG.log(Level.WARNING, failure,
						() -> "Discovered unit resolver failed for '" + qualifiedName
								+ "', skipping it: " + resolver.getClass().getName());
			}
		}
		return Optional.empty();
	}
}
