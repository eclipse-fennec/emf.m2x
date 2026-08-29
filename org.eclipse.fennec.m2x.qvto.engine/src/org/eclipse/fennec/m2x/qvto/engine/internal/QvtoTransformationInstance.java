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

import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;

/**
 * Runtime wrapper for an instantiated transformation (§8.1.13).
 *
 * <p>Created by {@code new T(args)} when T is an imported transformation.
 * Extends {@link DynamicEObjectImpl} so OCL property access on config properties
 * works via {@code eGet()}/{@code eSet()}.
 *
 * <p>The actual execution happens when {@code transform()} or
 * {@code parallelTransform()} is called on this instance.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoTransformationInstance extends DynamicEObjectImpl {

	private final OperationalTransformation transformation;
	private final Map<ModelParameter, QvtoModelExtent> modelBindings;
	private final QvtoEngineServices engine;

	/**
	 * Creates a new transformation instance.
	 *
	 * @param transformation the imported transformation to execute
	 * @param moduleClass the EClass representing the transformation module
	 * @param modelBindings mapping from model parameters to extents
	 * @param engine the engine for nested execution
	 */
	QvtoTransformationInstance(OperationalTransformation transformation,
			EClass moduleClass,
			Map<ModelParameter, QvtoModelExtent> modelBindings,
			QvtoEngineServices engine) {
		super(moduleClass);
		this.transformation = Objects.requireNonNull(transformation);
		this.modelBindings = Objects.requireNonNull(modelBindings);
		this.engine = Objects.requireNonNull(engine);
	}

	OperationalTransformation getTransformation() {
		return transformation;
	}

	Map<ModelParameter, QvtoModelExtent> getModelBindings() {
		return modelBindings;
	}

	QvtoEngineServices getEngine() {
		return engine;
	}
}
