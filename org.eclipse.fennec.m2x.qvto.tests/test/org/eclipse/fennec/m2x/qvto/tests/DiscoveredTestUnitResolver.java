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
package org.eclipse.fennec.m2x.qvto.tests;

import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;

/**
 * A resolver the engine can only reach by discovering it — declared in
 * {@code META-INF/services}, named nowhere in any configuration.
 *
 * <p>It answers for exactly one name, so a test that resolves {@code discovered.library}
 * can only have got there through {@link java.util.ServiceLoader}.
 */
public class DiscoveredTestUnitResolver implements QvtoUnitResolver {

	public static final String UNIT = "discovered.library";

	@Override
	public Optional<QvtoUnit> resolveUnit(String qualifiedName) {
		if (!UNIT.equals(qualifiedName)) {
			return Optional.empty();
		}
		return Optional.of(new QvtoUnit.SourceUnit(qualifiedName,
				URI.createURI("mem:/" + qualifiedName + ".qvto"),
				"library " + UNIT + ";\n"));
	}
}
