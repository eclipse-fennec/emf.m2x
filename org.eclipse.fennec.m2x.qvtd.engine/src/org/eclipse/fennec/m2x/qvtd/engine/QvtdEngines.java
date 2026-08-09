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
package org.eclipse.fennec.m2x.qvtd.engine;

import java.util.Objects;

import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtdEngineImpl;

/**
 * Creates {@link QvtdEngine} instances outside OSGi.
 *
 * <p>In OSGi the engine arrives as a service and Declarative Services wires it;
 * nothing here is needed. On a flat classpath this factory is where the wiring lives —
 * the implementation class sits in a private package, so a caller cannot depend on it
 * by accident.
 *
 * <pre>
 * OclEngine ocl = OclEngines.create(new OclParserSupport());
 * QvtdEngine engine = QvtdEngines.create(QvtdConfiguration.builder(ocl).build());
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class QvtdEngines {

	private QvtdEngines() {
		// static factory
	}

	/**
	 * Creates an engine for QVT-R transformations from the given configuration.
	 *
	 * @param configuration the configuration, must not be {@code null}
	 * @return a new engine
	 */
	public static QvtdEngine create(QvtdConfiguration configuration) {
		Objects.requireNonNull(configuration, "configuration must not be null");
		return new QvtdEngineImpl(configuration);
	}
}
