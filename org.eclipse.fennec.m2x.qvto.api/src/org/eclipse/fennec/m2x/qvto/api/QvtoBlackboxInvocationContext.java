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

import java.util.Map;

import org.eclipse.emf.ecore.EPackage;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Context provided to blackbox library invocations, giving access to
 * the current evaluation state (§8.1.4).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface QvtoBlackboxInvocationContext {

	/**
	 * Returns the context object ({@code self}) for contextual operations,
	 * or {@code null} for module-level operations.
	 *
	 * @return the context object, or {@code null}
	 */
	Object self();

	/**
	 * Adds an informational diagnostic message.
	 *
	 * @param message the message
	 */
	void addInfo(String message);

	/**
	 * Adds a warning diagnostic message.
	 *
	 * @param message the message
	 */
	void addWarning(String message);

	/**
	 * Adds an error diagnostic message.
	 *
	 * @param message the message
	 */
	void addError(String message);

	/**
	 * Returns the configuration properties of the current execution.
	 *
	 * @return unmodifiable map of config properties, never {@code null}
	 */
	Map<String, Object> getConfigProperties();

	/**
	 * Returns the named model extent from the current execution context.
	 *
	 * @param name the extent name (model parameter name)
	 * @return the extent, or {@code null} if not found
	 */
	QvtoModelExtent getExtent(String name);

	/**
	 * Returns the EPackage registry used by the current execution.
	 *
	 * @return the package registry, never {@code null}
	 */
	EPackage.Registry getPackageRegistry();
}
