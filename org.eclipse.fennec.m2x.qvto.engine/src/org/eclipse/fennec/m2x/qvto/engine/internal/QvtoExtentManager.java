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
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
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
	/**
	 * The instances of intermediate classes created without an explicit extent: they belong to
	 * no model parameter (§8.1.10, §8.2.1.24), but allInstances() still finds them (#337).
	 */
	private final QvtoModelExtent intermediateData = new BasicQvtoModelExtent();
	/** For collection-of-models parameters: name → list of extents. */
	private final Map<String, List<QvtoModelExtent>> groupedByName = new HashMap<>();
	/**
	 * The extents this execution made read-only for its in parameters. They belong to whoever
	 * passed them, so the flag ends with the execution (#387).
	 */
	private final List<BasicQvtoModelExtent> madeReadOnly = new ArrayList<>();
	/** The models lent to transformations started by parallelTransform() (§8.1.3.3, #385). */
	private final Map<QvtoModelExtent, List<Lending>> lendings = new IdentityHashMap<>();

	/**
	 * A model passed to a transformation running in parallel: whether that transformation may
	 * write it, and the run that ends the lending.
	 */
	private record Lending(String transformation, boolean writes, Future<?> run) {
	}

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
							makeReadOnly(basic);
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
					makeReadOnly(basic);
				}
			}
		}
	}

	private void makeReadOnly(BasicQvtoModelExtent extent) {
		if (!extent.isReadOnly()) {
			extent.setReadOnly(true);
			madeReadOnly.add(extent);
		}
	}

	/**
	 * Ends the read-only status this execution gave the extents of its in parameters (#387).
	 */
	void releaseReadOnly() {
		madeReadOnly.forEach(extent -> extent.setReadOnly(false));
		madeReadOnly.clear();
	}

	/**
	 * Records that a transformation started by parallelTransform() uses the model until its run
	 * ends (§8.1.3.3, #385).
	 *
	 * @param extent the model
	 * @param transformation the name of the transformation it is passed to
	 * @param writes whether that transformation may modify it (a parameter other than in)
	 * @param run the run whose end ends the lending
	 */
	void lend(QvtoModelExtent extent, String transformation, boolean writes, Future<?> run) {
		lendings.computeIfAbsent(extent, e -> new ArrayList<>()).add(new Lending(transformation, writes, run));
	}

	/**
	 * The running transformation that uses the model in a way another use conflicts with:
	 * two uses conflict unless both only read it (§8.1.3.3, #385).
	 *
	 * @param extent the model
	 * @param writes whether the new use may modify it
	 * @return the name of the transformation in the way
	 */
	Optional<String> conflictingUse(QvtoModelExtent extent, boolean writes) {
		return running(extent).filter(lending -> writes || lending.writes())
				.map(Lending::transformation).findFirst();
	}

	/**
	 * The running transformation the object's model is lent to; the caller may not modify the
	 * model until it ends (§8.1.3.3, #385).
	 *
	 * @param object the object to be modified
	 * @return the name of the transformation
	 */
	Optional<String> borrowerOf(EObject object) {
		// Asked on every assignment: only the lent models are searched, and none when nothing is
		// lent — extentOf would search the contents of every model, the growing out model
		// included, which made an assignment cost as much as the model was large
		if (object == null || lendings.isEmpty()) {
			return Optional.empty();
		}
		EObject root = EcoreUtil.getRootContainer(object);
		for (QvtoModelExtent extent : List.copyOf(lendings.keySet())) {
			if (extent.getContents().contains(root) || extent.getContents().contains(object)) {
				return borrowerOf(extent);
			}
		}
		return Optional.empty();
	}

	/**
	 * The running transformation the model is lent to (§8.1.3.3, #385).
	 *
	 * @param extent the model
	 * @return the name of the transformation
	 */
	Optional<String> borrowerOf(QvtoModelExtent extent) {
		return running(extent).map(Lending::transformation).findFirst();
	}

	private Stream<Lending> running(QvtoModelExtent extent) {
		List<Lending> lent = lendings.get(extent);
		if (lent == null) {
			return Stream.empty();
		}
		lent.removeIf(lending -> lending.run().isDone());
		return List.copyOf(lent).stream();
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
	/**
	 * The extent whose contents hold the object's root container, or {@code null} (#342).
	 *
	 * @param object the object
	 * @return its extent
	 */
	QvtoModelExtent extentOf(EObject object) {
		EObject root = EcoreUtil.getRootContainer(object);
		for (QvtoModelExtent extent : extents) {
			if (extent.getContents().contains(root) || extent.getContents().contains(object)) {
				return extent;
			}
		}
		return intermediateData.getContents().contains(root) ? intermediateData : null;
	}

	/**
	 * The extent of the transformation's own intermediate data, which no model parameter holds.
	 *
	 * @return the intermediate data extent
	 */
	QvtoModelExtent intermediateData() {
		return intermediateData;
	}

	/**
	 * Whether more than one out or inout model parameter could take an object of this class:
	 * "two ModelParameters use the same ModelType … a programming hazard that can be diagnosed
	 * by tooling" (§8.1.3.4, #337).
	 *
	 * @param classifier the object's class
	 * @return {@code true} if the inferred extent is a guess among several
	 */
	boolean isAmbiguous(EClassifier classifier) {
		EPackage objPkg = classifier == null ? null : classifier.getEPackage();
		String nsURI = objPkg == null ? null : objPkg.getNsURI();
		if (nsURI == null) {
			return false;
		}
		Set<String> candidates = new LinkedHashSet<>();
		for (ModelParameter mp : modelParams) {
			if ((mp.getKind() == DirectionKind.OUT || mp.getKind() == DirectionKind.INOUT)
					&& mp.getEType() instanceof ModelType modelType
					&& modelType.getMetamodel().stream().anyMatch(mm -> nsURI.equals(mm.getNsURI()))) {
				candidates.add(mp.getName());
			}
		}
		return candidates.size() > 1;
	}

	/**
	 * Every instance of the class in the models of this transformation — the extents OCL's
	 * {@code allInstances()} ranges over in QVT-O (#342).
	 *
	 * @param eClass the class
	 * @return its instances, subclasses included, each once
	 */
	Collection<EObject> allInstances(EClass eClass) {
		Set<EObject> instances = new LinkedHashSet<>();
		List<QvtoModelExtent> searched = new ArrayList<>(extents);
		searched.add(intermediateData);
		for (QvtoModelExtent extent : searched) {
			for (EObject root : extent.getContents()) {
				if (eClass.isInstance(root)) {
					instances.add(root);
				}
				root.eAllContents().forEachRemaining(child -> {
					if (eClass.isInstance(child)) {
						instances.add(child);
					}
				});
			}
		}
		return instances;
	}

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
