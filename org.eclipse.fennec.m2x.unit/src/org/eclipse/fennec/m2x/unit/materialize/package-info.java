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
 * Materialization: from a unit document a store or a resolver handed out to a document bound
 * in a consumer's context.
 *
 * <p>A document travels with unresolved references — proxies are the transport state. The
 * {@link org.eclipse.fennec.m2x.unit.materialize.UnitMaterializer} is where they end: it binds
 * the document in the consumer's {@link org.eclipse.fennec.m2x.unit.api.UnitResourceSet},
 * serves the copies the document carries, runs the validation funnel, and promises that no
 * proxy is handed on.
 */
@org.osgi.annotation.bundle.Export
@org.osgi.annotation.versioning.Version("1.0")
package org.eclipse.fennec.m2x.unit.materialize;
