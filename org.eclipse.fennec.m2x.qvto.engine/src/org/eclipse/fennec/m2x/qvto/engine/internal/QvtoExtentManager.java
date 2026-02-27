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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.qvtoperational.DirectionKind;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelType;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext.ParameterBinding;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;

/**
 * Manages the binding between {@link ModelParameter} declarations and
 * {@link QvtoModelExtent} instances from the execution context.
 *
 * <p>Supports both positional binding (legacy) and named binding with
 * collection-of-models parameters (§8.1.1, §8.2.1.5).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtoExtentManager {

	private final List<QvtoModelExtent> extents;
	private final List<ModelParameter> modelParams;
	private final Map<String, QvtoModelExtent> byName = new HashMap<>();
	/** For collection-of-models parameters: name → list of extents. */
	private final Map<String, List<QvtoModelExtent>> groupedByName = new HashMap<>();

	public QvtoExtentManager(OperationalTransformation transformation, QvtoExecutionContext context) {
		Objects.requireNonNull(transformation, "transformation must not be null");
		Objects.requireNonNull(context, "context must not be null");
		this.extents = context.modelExtents();

		List<ModelParameter> params = transformation.getModelParameter();

		if (context.hasParameterBindings()) {
			// Named binding mode: match ParameterBindings to ModelParameters by name
			this.modelParams = List.copyOf(params);
			Map<String, ParameterBinding> bindingMap = new HashMap<>();
			for (ParameterBinding pb : context.parameterBindings()) {
				bindingMap.put(pb.name(), pb);
			}
			for (ModelParameter mp : params) {
				ParameterBinding pb = bindingMap.get(mp.getName());
				if (pb == null) {
					continue;
				}
				if (mp.isSetCollectionKind()) {
					// Collection-of-models parameter: store grouped extents
					groupedByName.put(mp.getName(), pb.extents());
					// byName maps to first extent (for default output operations)
					if (!pb.extents().isEmpty()) {
						byName.put(mp.getName(), pb.extents().get(0));
					}
				} else {
					// Normal single-extent parameter
					if (!pb.extents().isEmpty()) {
						byName.put(mp.getName(), pb.extents().get(0));
					}
				}
				// §8.1.3.2: in-parameter extents are read-only
				if (mp.getKind() == DirectionKind.IN) {
					for (QvtoModelExtent ext : pb.extents()) {
						if (ext instanceof BasicQvtoModelExtent basic) {
							basic.setReadOnly(true);
						}
					}
				}
			}
		} else {
			// Legacy positional binding
			int bound = Math.min(params.size(), extents.size());
			this.modelParams = List.copyOf(params.subList(0, bound));
			for (int i = 0; i < bound; i++) {
				byName.put(params.get(i).getName(), extents.get(i));
				// §8.1.3.2: in-parameter extents are read-only
				if (params.get(i).getKind() == DirectionKind.IN
						&& extents.get(i) instanceof BasicQvtoModelExtent basic) {
					basic.setReadOnly(true);
				}
			}
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
	 * Returns the grouped extents for a collection-of-models parameter.
	 *
	 * @param name the model parameter name
	 * @return the list of extents, or {@code null} if not a collection parameter
	 */
	List<QvtoModelExtent> getExtents(String name) {
		return groupedByName.get(name);
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
				return byName.get(modelParams.get(i).getName());
			}
		}
		return extents.isEmpty() ? null : extents.get(extents.size() - 1);
	}

	/**
	 * §8.1.3.2: Checks whether the given EObject belongs to a read-only ({@code in}) extent.
	 *
	 * @param eObject the object to check
	 * @return {@code true} if the object's root container is in a read-only extent
	 */
	boolean isReadOnly(EObject eObject) {
		if (eObject == null) {
			return false;
		}
		EObject root = EcoreUtil.getRootContainer(eObject);
		for (QvtoModelExtent extent : extents) {
			if (extent.isReadOnly() && extent.getContents().contains(root)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the model parameter name for the given extent.
	 *
	 * @param extent the extent to find the name for
	 * @return the parameter name, or {@code null} if not found
	 */
	String getParameterName(QvtoModelExtent extent) {
		for (var entry : byName.entrySet()) {
			if (entry.getValue() == extent) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * Returns the output extent whose metamodel contains the given classifier.
	 * Matches by EPackage nsURI of the classifier against the ModelType's
	 * metamodel packages of each OUT/INOUT model parameter.
	 *
	 * @param classifier the classifier to find an extent for
	 * @return the matching extent, or {@code null} if no match found
	 */
	QvtoModelExtent getExtentForClassifier(EClassifier classifier) {
		if (classifier == null) {
			return null;
		}
		EPackage objPkg = classifier.getEPackage();
		if (objPkg == null) {
			return null;
		}
		String nsURI = objPkg.getNsURI();
		for (ModelParameter mp : modelParams) {
			DirectionKind kind = mp.getKind();
			if (kind != DirectionKind.OUT && kind != DirectionKind.INOUT) {
				continue;
			}
			if (mp.getEType() instanceof ModelType modelType) {
				for (EPackage metamodel : modelType.getMetamodel()) {
					if (nsURI != null && nsURI.equals(metamodel.getNsURI())) {
						// For collection-of-models: return the first OUT extent in the group
						return byName.get(mp.getName());
					}
				}
			}
		}
		return null;
	}

	/**
	 * Creates an aggregated extent view over all extents of a collection-of-models parameter.
	 * The aggregated extent provides read-only access to all objects across all grouped extents.
	 *
	 * @param name the parameter name
	 * @return an aggregated extent, or {@code null} if not a collection parameter
	 */
	QvtoModelExtent getAggregatedExtent(String name) {
		List<QvtoModelExtent> group = groupedByName.get(name);
		if (group == null) {
			return null;
		}
		List<EObject> aggregated = new ArrayList<>();
		for (QvtoModelExtent ext : group) {
			aggregated.addAll(ext.getContents());
		}
		BasicQvtoModelExtent result = new BasicQvtoModelExtent();
		result.setContents(aggregated);
		if (!group.isEmpty() && group.get(0).isReadOnly()) {
			result.setReadOnly(true);
		}
		return result;
	}
}
