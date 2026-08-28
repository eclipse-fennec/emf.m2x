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
	/**
	 * Returns the names of the operations this library serves.
	 *
	 * <p>Without this the engine could only find out by calling: it invoked every registered
	 * library in turn and read an exception as "not mine", which made a library that genuinely
	 * failed — a broken implementation, a {@code SecurityException} from a sandbox — look like a
	 * library that simply does not offer the operation, and let the relation proceed as if no
	 * implementation existed (#180). With the names declared, exactly one library is called and
	 * its failure is a diagnostic.
	 *
	 * <p>It is also what lets the allow-list and the library ceiling of {@code QvtdConfiguration}
	 * be applied before anything is invoked, and what {@code UnitBinder.validate} checks a
	 * compiled unit's blackbox requirements against at prepare time.
	 *
	 * @return the operation names, never {@code null}
	 * @since 1.0
	 */
	List<String> getOperationNames();

	Object invoke(String operationName, Object self, Object[] args);
}
