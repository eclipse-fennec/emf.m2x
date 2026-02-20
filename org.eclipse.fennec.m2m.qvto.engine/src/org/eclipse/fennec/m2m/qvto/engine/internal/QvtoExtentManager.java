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
package org.eclipse.fennec.m2m.qvto.engine.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.fennec.m2m.model.qvtoperational.DirectionKind;
import org.eclipse.fennec.m2m.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;

/**
 * Manages the binding between {@link ModelParameter} declarations and
 * {@link QvtoModelExtent} instances from the execution context.
 *
 * <p>Extents are bound by position: the first model parameter maps to the
 * first extent, etc.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtoExtentManager {

	private final List<QvtoModelExtent> extents;
	private final List<ModelParameter> modelParams;
	private final Map<String, QvtoModelExtent> byName = new HashMap<>();

	public QvtoExtentManager(OperationalTransformation transformation, QvtoExecutionContext context) {
		Objects.requireNonNull(transformation, "transformation must not be null");
		Objects.requireNonNull(context, "context must not be null");
		this.extents = context.modelExtents();

		List<ModelParameter> params = transformation.getModelParameter();
		int bound = Math.min(params.size(), extents.size());
		this.modelParams = List.copyOf(params.subList(0, bound));
		for (int i = 0; i < bound; i++) {
			byName.put(params.get(i).getName(), extents.get(i));
		}
	}

	/**
	 * Returns the extent bound to the given model parameter.
	 *
	 * @param param the model parameter
	 * @return the extent, or {@code null} if not bound
	 */
	QvtoModelExtent getExtent(ModelParameter param) {
		return byName.get(param.getName());
	}

	/**
	 * Returns the extent at the given positional index.
	 *
	 * @param index the zero-based index
	 * @return the extent, or {@code null} if index is out of bounds
	 */
	QvtoModelExtent getExtent(int index) {
		if (index >= 0 && index < extents.size()) {
			return extents.get(index);
		}
		return null;
	}

	/**
	 * Returns the extent bound to the given parameter name.
	 *
	 * @param name the model parameter name
	 * @return the extent, or {@code null} if not bound
	 */
	QvtoModelExtent getExtent(String name) {
		return byName.get(name);
	}

	/**
	 * Returns the default output extent: the first {@code out} or {@code inout}
	 * model parameter's extent. Falls back to the last extent if none matches.
	 *
	 * @return the default output extent, or {@code null} if no extents exist
	 */
	QvtoModelExtent getDefaultOutputExtent() {
		for (int i = 0; i < modelParams.size(); i++) {
			DirectionKind kind = modelParams.get(i).getKind();
			if (kind == DirectionKind.OUT || kind == DirectionKind.INOUT) {
				return extents.get(i);
			}
		}
		return extents.isEmpty() ? null : extents.get(extents.size() - 1);
	}
}
