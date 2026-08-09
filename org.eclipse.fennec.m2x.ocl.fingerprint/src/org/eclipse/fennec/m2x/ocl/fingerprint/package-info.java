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
 * Keys compiled OCL by model fingerprint instead of by nsURI.
 *
 * <p>Compiled against {@code emf.osgi.api} alone — the {@code FingerprintService} is taken
 * as a parameter, never looked up here. {@link org.eclipse.fennec.emf.osgi.annotation.require.RequireEMF}
 * is what says an implementation has to be present at runtime, so the requirement is
 * declared rather than a bundle put on the build path.
 */
@org.osgi.annotation.bundle.Export
@org.osgi.annotation.versioning.Version("1.0.0")
@org.eclipse.fennec.emf.osgi.annotation.require.RequireEMF
package org.eclipse.fennec.m2x.ocl.fingerprint;
