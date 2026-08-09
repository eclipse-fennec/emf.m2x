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
package org.eclipse.fennec.m2x.ocl.metadata.tests;

import java.util.concurrent.CompletableFuture;

import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectProvider;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryWriter;
import org.osgi.service.component.annotations.Component;

/**
 * A provider that loads nothing.
 *
 * <p>A registry only publishes once its initial provider has finished loading, which is what
 * keeps consumers from ever seeing a half-filled one. Compiled OCL does not come from a
 * content source, though — it is written through the writer as expressions are compiled — so
 * the initial load here is genuinely empty, and saying so is the whole job of this class.
 */
@Component(service = EObjectProvider.class,
		property = "emf.eobject.provider.name=ocl-empty-provider")
public class EmptyEObjectProvider implements EObjectProvider {

	@Override
	public CompletableFuture<Void> load(EObjectRegistryWriter writer) {
		return CompletableFuture.completedFuture(null);
	}
}
