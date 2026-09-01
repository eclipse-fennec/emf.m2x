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
/**
 * The unit store: sources and compiled units, kept by language, name, kind and fingerprint.
 *
 * <p>{@link org.eclipse.fennec.m2x.unit.store.DefaultUnitStore} is the store; what carries it
 * is a {@link org.eclipse.fennec.m2x.unit.store.UnitStoreBackend} — bytes by key —
 * with {@link org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend} as the one that needs
 * nothing. The store owns the serialization and nothing else: a document goes in as XMI and
 * comes out as an independent copy with its references unresolved — binding it in a consumer's
 * context is the {@link org.eclipse.fennec.m2x.unit.materialize.UnitMaterializer}'s job
 * (§5.5 of the compiled-unit concept, #211).
 */
@org.osgi.annotation.bundle.Export
@org.osgi.annotation.versioning.Version("1.0")
package org.eclipse.fennec.m2x.unit.store;
