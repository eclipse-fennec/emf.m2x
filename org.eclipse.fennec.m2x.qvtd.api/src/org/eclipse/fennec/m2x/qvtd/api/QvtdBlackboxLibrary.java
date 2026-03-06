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
package org.eclipse.fennec.m2x.qvtd.api;

import java.util.List;

import org.osgi.annotation.versioning.ConsumerType;

/**
 * A blackbox library contributing native Java operations to QVT-R
 * transformations (§7.8).
 *
 * <p>Register libraries via a {@link QvtdBlackboxRegistry}. In OSGi,
 * publish implementations as whiteboard services.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface QvtdBlackboxLibrary {

	/**
	 * Returns the simple module name as referenced in QVT-R source.
	 *
	 * @return the module name, never {@code null}
	 */
	String getModuleName();

	/**
	 * Returns the fully qualified unit name for import resolution.
	 *
	 * @return the qualified name, never {@code null}
	 */
	String getUnitQualifiedName();

	/**
	 * Returns the nsURIs of EPackages used by this library's operations.
	 *
	 * @return the package URIs, never {@code null}
	 */
	List<String> getUsedPackageURIs();

	/**
	 * Invokes the named operation with the given arguments.
	 *
	 * @param operationName the operation name
	 * @param self the context object ({@code this}), or {@code null} for static operations
	 * @param args the operation arguments
	 * @return the operation result, or {@code null}
	 */
	Object invoke(String operationName, Object self, Object[] args);
}
