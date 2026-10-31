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
package org.eclipse.fennec.m2x.qvtd.engine.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;

/**
 * Manages the mapping between {@link TypedModel} declarations and runtime
 * {@link QvtdModelExtent} instances.
 *
 * <p>Provides type-based lookup for pattern matching: given an {@link EClass},
 * returns all instances (including subtypes) from the corresponding extent.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtrExtentManager {

	private final Map<String, QvtdModelExtent> extentsByName;
	private final String targetModelName;
	/**
	 * The copies of models that are source and target at once: matching a source domain reads
	 * the state before the run, enforcing writes the model itself (§7.7, #353).
	 */
	private final Map<String, QvtdModelExtent> sourceSnapshots = new HashMap<>();
	private boolean matchingSources;

	/**
	 * Creates the extent manager by binding transformation TypedModel names
	 * to the runtime model extents from the execution context.
	 *
	 * @param transformation the parsed transformation
	 * @param context the execution context with named model extents
	 */
	public QvtrExtentManager(RelationalTransformation transformation,
			QvtdExecutionContext context) {
		Objects.requireNonNull(transformation, "transformation must not be null");
		Objects.requireNonNull(context, "context must not be null");
		this.extentsByName = new HashMap<>(context.modelExtents());
		this.targetModelName = context.targetModelName();
	}

	/**
	 * Returns the model extent for the given TypedModel.
	 *
	 * @param typedModel the typed model declaration
	 * @return the model extent
	 * @throws IllegalArgumentException if no extent is bound for the model name
	 */
	public QvtdModelExtent getExtent(TypedModel typedModel) {
		Objects.requireNonNull(typedModel, "typedModel must not be null");
		String name = typedModel.getName();
		QvtdModelExtent extent = extentsByName.get(name);
		if (extent == null) {
			throw new IllegalArgumentException(
					"No model extent bound for TypedModel '%s'. Available: %s"
							.formatted(name, extentsByName.keySet()));
		}
		return extent;
	}

	/**
	 * Returns whether the given TypedModel is the target (enforce) model.
	 *
	 * @param typedModel the typed model
	 * @return {@code true} if this is the target model
	 */
	public boolean isTargetModel(TypedModel typedModel) {
		return targetModelName != null && targetModelName.equals(typedModel.getName());
	}

	/**
	 * Collects all instances of the given EClass (including subtypes) from
	 * the model extent bound to the given TypedModel.
	 *
	 * @param typedModel the typed model to search in
	 * @param eClass the type to match
	 * @return list of matching instances, never {@code null}
	 */
	public List<EObject> allInstances(TypedModel typedModel, EClass eClass) {
		Objects.requireNonNull(eClass, "eClass must not be null");
		QvtdModelExtent snapshot = matchingSources ? sourceSnapshots.get(typedModel.getName()) : null;
		QvtdModelExtent extent = snapshot != null ? snapshot : getExtent(typedModel);
		List<EObject> result = new ArrayList<>();
		collectInstances(extent.getContents(), eClass, result);
		return result;
	}

	/**
	 * The instances of a class in every model of the run — where a template that stands as a
	 * predicate, outside any domain, is matched (#361).
	 *
	 * @param eClass the type to match
	 * @return the instances, never {@code null}
	 */
	public List<EObject> allInstances(EClass eClass) {
		Objects.requireNonNull(eClass, "eClass must not be null");
		List<EObject> result = new ArrayList<>();
		for (QvtdModelExtent extent : extentsByName.values()) {
			collectInstances(extent.getContents(), eClass, result);
		}
		return result;
	}

	/**
	 * The instances of a class in the model the object belongs to — where the owner of a
	 * non-navigable opposite role is looked for (§7.3, #355).
	 *
	 * @param object the object whose model is searched
	 * @param eClass the type to match
	 * @return the instances, never {@code null}
	 */
	public List<EObject> instancesBeside(EObject object, EClass eClass) {
		EObject root = EcoreUtil.getRootContainer(object);
		List<EObject> result = new ArrayList<>();
		List<QvtdModelExtent> extents = new ArrayList<>(extentsByName.values());
		extents.addAll(sourceSnapshots.values());
		for (QvtdModelExtent extent : extents) {
			if (extent.getContents().contains(root)) {
				collectInstances(extent.getContents(), eClass, result);
				return result;
			}
		}
		return result;
	}

	/**
	 * Lets source domains of the typed model match the given copy of its state before the run
	 * (§7.7, #353).
	 *
	 * @param typedModel the model that is source and target at once
	 * @param snapshot the copy of its contents
	 */
	public void snapshotForSources(TypedModel typedModel, QvtdModelExtent snapshot) {
		sourceSnapshots.put(typedModel.getName(), snapshot);
	}

	/** Drops the snapshots: what is matched from now on is the models as they are. */
	public void dropSnapshots() {
		sourceSnapshots.clear();
	}

	/**
	 * Switches between matching source domains (which read the snapshots) and anything else.
	 *
	 * @param sources {@code true} while source domains are matched
	 * @return the previous setting
	 */
	public boolean matchingSources(boolean sources) {
		boolean previous = matchingSources;
		matchingSources = sources;
		return previous;
	}

	private void collectInstances(List<EObject> objects, EClass eClass, List<EObject> result) {
		for (EObject obj : objects) {
			if (eClass.isInstance(obj)) {
				result.add(obj);
			}
			collectInstances(obj.eContents(), eClass, result);
		}
	}
}
