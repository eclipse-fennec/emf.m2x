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
 * The object medium of the unit store: a {@code UnitStore} over the emf.osgi
 * {@code EObjectRegistry}, holding live compiled-unit documents (#213).
 *
 * <p>This is the pre-unit pattern — a parsed AST handed around as a loaded EMF object (#127) —
 * with the envelope that made it sound: manifest, fingerprints, satellite container, carried
 * packages. The registry delivers the documents; what a document means in a consumer's context
 * is still the {@code UnitMaterializer}'s business, exactly as for the byte medium.
 */
@org.osgi.annotation.bundle.Export
@org.osgi.annotation.versioning.Version("1.0")
package org.eclipse.fennec.m2x.unit.registry;
