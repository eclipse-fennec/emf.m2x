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
package org.eclipse.fennec.m2m.qvto.api;

import java.util.List;

import org.osgi.annotation.versioning.ConsumerType;

/**
 * A blackbox library contributing native Java operations to QVT-O transformations.
 *
 * <p>Blackbox libraries are the primary extension mechanism for calling Java code
 * from QVT-O. In standalone mode, register libraries via
 * {@link QvtoEngine#registerBlackbox(QvtoBlackboxLibrary)}. In OSGi, publish
 * implementations as whiteboard services.
 *
 * <p>The library is identified by its {@linkplain #getModuleName() module name}
 * and {@linkplain #getUnitQualifiedName() qualified unit name}, matching the
 * QVT-O {@code uses} and {@code import} syntax.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 * @see QvtoBlackboxOperation
 */
@ConsumerType
public interface QvtoBlackboxLibrary {

	/**
	 * Returns the simple module name as referenced in QVT-O {@code uses} declarations.
	 *
	 * @return the module name, never {@code null}
	 */
	String getModuleName();

	/**
	 * Returns the fully qualified unit name for {@code import} resolution.
	 *
	 * @return the qualified name, never {@code null}
	 */
	String getUnitQualifiedName();

	/**
	 * Returns the operations provided by this library.
	 *
	 * @return the operations, never {@code null}
	 */
	List<QvtoBlackboxOperation> getOperations();

	/**
	 * Returns the nsURIs of EPackages used by this library's operations.
	 *
	 * @return the package URIs, never {@code null}
	 */
	List<String> getUsedPackageURIs();
}
