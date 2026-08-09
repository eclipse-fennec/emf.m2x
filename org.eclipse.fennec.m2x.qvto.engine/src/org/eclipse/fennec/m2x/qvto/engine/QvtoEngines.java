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
package org.eclipse.fennec.m2x.qvto.engine;

import java.util.Objects;

import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.qvto.engine.internal.QvtoEngineImpl;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;

/**
 * Creates {@link QvtoEngine} instances outside OSGi.
 *
 * <p>In OSGi the engine arrives as a service and Declarative Services wires it;
 * nothing here is needed. On a flat classpath this factory is where the wiring lives —
 * the implementation class sits in a private package, so a caller cannot depend on it
 * by accident.
 *
 * <pre>
 * OclEngine ocl = OclEngines.create(new OclParserSupport());
 * QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(ocl).build());
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class QvtoEngines {

	private QvtoEngines() {
		// static factory
	}

	/**
	 * Creates an engine for QVT-O transformations from the given configuration.
	 *
	 * @param configuration the configuration, must not be {@code null}
	 * @return a new engine
	 */
	public static QvtoEngine create(QvtoConfiguration configuration) {
		Objects.requireNonNull(configuration, "configuration must not be null");
		return new QvtoEngineImpl(configuration);
	}
}
