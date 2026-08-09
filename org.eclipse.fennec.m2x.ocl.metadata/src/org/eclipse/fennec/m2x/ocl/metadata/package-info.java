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
 * OCL-specific aspects for the Model Metadata Service.
 *
 * <p>Compiled OCL is filed on the metadata tree, so it is derived once per model version and
 * goes when that version goes — which an LRU inside the engine cannot do, since it knows how
 * many entries it may keep but not what a model version is.
 *
 * <p>The {@code MetadataService} is taken as a parameter, never looked up here.
 * {@link org.eclipse.fennec.emf.osgi.annotation.require.RequireEMF} is what says an
 * implementation has to be present at runtime.
 */
@org.osgi.annotation.bundle.Export
@org.osgi.annotation.versioning.Version("1.0.0")
@org.eclipse.fennec.emf.osgi.annotation.require.RequireEMF
package org.eclipse.fennec.m2x.ocl.metadata;
