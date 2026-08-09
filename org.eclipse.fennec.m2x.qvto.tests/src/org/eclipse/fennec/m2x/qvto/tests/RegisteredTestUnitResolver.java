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
import org.osgi.service.component.annotations.Component;

/**
 * A unit offered to the service registry under the name a transformation would import it by.
 *
 * <p>Nothing references this component. The engine finds it only because the name it looks
 * up — taken from the {@code import} in the transformation — matches the {@code qvto.unit.name}
 * property published here.
 */
@Component(service = QvtoUnitResolver.class, property = "qvto.unit.name=registered.library")
public class RegisteredTestUnitResolver implements QvtoUnitResolver {

	public static final String UNIT = "registered.library";

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
