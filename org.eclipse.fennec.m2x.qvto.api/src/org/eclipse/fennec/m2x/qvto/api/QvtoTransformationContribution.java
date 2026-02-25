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
package org.eclipse.fennec.m2x.qvto.api;

import java.net.URI;

import org.osgi.annotation.versioning.ConsumerType;

/**
 * A deployed QVT-O transformation contributed via the OSGi whiteboard.
 *
 * <p>Bundles containing pre-packaged transformations publish implementations
 * of this interface as OSGi services. The engine discovers them automatically.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface QvtoTransformationContribution {

	/**
	 * Returns the URI of the transformation resource.
	 *
	 * @return the transformation URI, never {@code null}
	 */
	URI getTransformationUri();

	/**
	 * Returns the qualified name of the transformation.
	 *
	 * @return the qualified name, never {@code null}
	 */
	String getQualifiedName();
}
