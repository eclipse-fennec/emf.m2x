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
package org.eclipse.fennec.m2x.qvto.engine.internal;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxLibrary;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.unit.osgi.RankedServices;

/**
 * The blackbox libraries the engine component has bound as services (#269).
 *
 * <p>Filled by the component's bind and unbind methods, read by every link and run. A library
 * is known by its {@linkplain QvtoBlackboxLibrary#getUnitQualifiedName() qualified name} —
 * the name a transformation imports — and where two services claim the same name, the higher
 * {@code service.ranking} wins.
 *
 * @since 1.0
 */
final class QvtoServiceBlackboxRegistry implements QvtoBlackboxRegistry {

	private final RankedServices<QvtoBlackboxLibrary> libraries =
			new RankedServices<>(QvtoBlackboxLibrary::getUnitQualifiedName);

	void add(QvtoBlackboxLibrary library, Map<String, ?> properties) {
		libraries.add(library, properties);
	}

	void remove(QvtoBlackboxLibrary library) {
		libraries.remove(library);
	}

	@Override
	public Optional<QvtoBlackboxLibrary> getLibrary(String qualifiedName) {
		return libraries.get(qualifiedName);
	}

	@Override
	public List<QvtoBlackboxLibrary> getLibraries() {
		return libraries.all();
	}
}
