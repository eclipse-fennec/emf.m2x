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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;

/**
 * Handles element operations (§8.3.4) and extent operations (§8.3.5)
 * on EObject and QvtoModelExtent instances.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoModelOperations {

	/** Sentinel indicating the operation was not handled by this class. */
	static final Object UNHANDLED = new Object();

	/** Sentinel for wrapping null values to distinguish "handled, result is null" from UNHANDLED. */
	private static final Object WRAPPED_NULL = new Object();

	/** Known extent operation names (§8.3.5). */
	private static final Set<String> EXTENT_OPS = Set.of(
			"objectsOfType", "objectsOfKind", "objects", "rootObjects",
			"addElement", "addObject", "removeElement", "createEmptyModel", "copy");

	/** Known element operation names (§8.3.4). */
	private static final Set<String> ELEMENT_OPS = Set.of(
			"metaClassName", "subobjects", "allSubobjects",
			"subobjectsOfType", "subobjectsOfKind",
			"allSubobjectsOfType", "allSubobjectsOfKind",
			"clone", "deepclone", "container");

	/** Checks if the operation name is a known extent operation. */
	static boolean isExtentOperation(String name) {
		return name != null && EXTENT_OPS.contains(name);
	}

	/** Checks if the operation name is a known element operation. */
	static boolean isElementOperation(String name) {
		return name != null && ELEMENT_OPS.contains(name);
	}

	private final Function<OclExpression, Object> evalFn;
	private final Function<EObject, QvtoModelExtent> extentOf;

	QvtoModelOperations(Function<OclExpression, Object> evalFn, Function<EObject, QvtoModelExtent> extentOf) {
		this.evalFn = Objects.requireNonNull(evalFn);
		this.extentOf = Objects.requireNonNull(extentOf);
	}

	/**
	 * Handles QVT-O model extent operations (§8.3.5).
	 * @return the result, {@link #UNHANDLED} if not an extent operation
	 */
	Object handleExtentOperation(OperationCallExp opCall) {
		if (opCall.getOwnedSource() == null) {
			return UNHANDLED;
		}
		Object source = evalFn.apply(opCall.getOwnedSource());
		if (!(source instanceof QvtoModelExtent extent)) {
			return UNHANDLED;
		}

		String opName = opCall.getName();
		// §8.3.5.3: objectsOfType — exact type match only (not subtypes)
		if ("objectsOfType".equals(opName) && !opCall.getOwnedArguments().isEmpty()) {
			return wrapNull(objectsOfType(extent, evalFn.apply(opCall.getOwnedArguments().get(0))));
		}
		// §8.3.5.2: objectsOfKind — includes subtypes
		if ("objectsOfKind".equals(opName) && !opCall.getOwnedArguments().isEmpty()) {
			return wrapNull(objectsOfKind(extent, evalFn.apply(opCall.getOwnedArguments().get(0))));
		}
		// §8.3.5.1: objects — "the set of all objects in the model", not only the roots (#301)
		if ("objects".equals(opName)) {
			return wrapNull(new ArrayList<>(allObjects(extent)));
		}
		// §8.3.5.4: rootObjects — the objects "not contained by other objects in the model"
		if ("rootObjects".equals(opName)) {
			return wrapNull(rootObjects(extent));
		}
		// §8.3.5.5: addElement — add element to extent
		if ("addElement".equals(opName) && !opCall.getOwnedArguments().isEmpty()) {
			checkExtentWritable(extent);
			Object arg = evalFn.apply(opCall.getOwnedArguments().get(0));
			if (arg instanceof EObject eo) {
				addElement(extent, eo);
			}
			return wrapNull(null);
		}
		// §8.3.5.6: removeElement — remove element from extent
		if ("removeElement".equals(opName) && !opCall.getOwnedArguments().isEmpty()) {
			checkExtentWritable(extent);
			Object arg = evalFn.apply(opCall.getOwnedArguments().get(0));
			if (arg instanceof EObject eo) {
				removeElement(extent, eo);
			}
			return wrapNull(null);
		}
		// §8.3.5.9: createEmptyModel — create new empty model extent
		if ("createEmptyModel".equals(opName)) {
			return wrapNull(new BasicQvtoModelExtent());
		}
		// §8.3.5.8: copy — deep copy of model and its extent
		if ("copy".equals(opName)) {
			return wrapNull(copy(extent));
		}
		return UNHANDLED;
	}

	/**
	 * Handles §8.3.4 Element operations on EObject instances.
	 * <p>Operations: metaClassName, subobjects, allSubobjects,
	 * subobjectsOfType/Kind, allSubobjectsOfType/Kind, clone, deepclone, container.
	 * @return the result, {@link #UNHANDLED} if not an element operation
	 */
	Object handleElementOperation(OperationCallExp opCall) {
		if (opCall.getOwnedSource() == null) {
			return UNHANDLED;
		}
		Object source = evalFn.apply(opCall.getOwnedSource());
		if (!(source instanceof EObject eObj)) {
			return UNHANDLED;
		}
		String opName = opCall.getName();

		// §8.3.4.3: metaClassName() : String
		if ("metaClassName".equals(opName)) {
			return wrapNull(eObj.eClass().getName());
		}
		// §8.3.4.4: subobjects() : Set(Element) — immediate children
		if ("subobjects".equals(opName)) {
			return wrapNull(new ArrayList<>(eObj.eContents()));
		}
		// §8.3.4.5: allSubobjects() : Set(Element) — all descendants
		if ("allSubobjects".equals(opName)) {
			List<EObject> result = new ArrayList<>();
			for (var iter = eObj.eAllContents(); iter.hasNext(); ) {
				result.add(iter.next());
			}
			return wrapNull(result);
		}
		// §8.3.4.6: subobjectsOfType(type) — immediate children, exact type
		if ("subobjectsOfType".equals(opName) && !opCall.getOwnedArguments().isEmpty()) {
			EClass filterType = resolveEClassArg(evalFn.apply(opCall.getOwnedArguments().get(0)));
			if (filterType != null) {
				List<EObject> result = new ArrayList<>();
				for (EObject child : eObj.eContents()) {
					if (isExactType(child, filterType)) {
						result.add(child);
					}
				}
				return wrapNull(result);
			}
			return wrapNull(new ArrayList<>()); // a data type or null: no child has it (#342)
		}
		// §8.3.4.8: subobjectsOfKind(type) — immediate children, includes subtypes
		if ("subobjectsOfKind".equals(opName) && !opCall.getOwnedArguments().isEmpty()) {
			EClass filterType = resolveEClassArg(evalFn.apply(opCall.getOwnedArguments().get(0)));
			if (filterType != null) {
				List<EObject> result = new ArrayList<>();
				for (EObject child : eObj.eContents()) {
					if (filterType.isInstance(child)) {
						result.add(child);
					}
				}
				return wrapNull(result);
			}
			return wrapNull(new ArrayList<>()); // a data type or null: no child has it (#342)
		}
		// §8.3.4.7: allSubobjectsOfType(type) — all descendants, exact type
		if ("allSubobjectsOfType".equals(opName) && !opCall.getOwnedArguments().isEmpty()) {
			EClass filterType = resolveEClassArg(evalFn.apply(opCall.getOwnedArguments().get(0)));
			if (filterType != null) {
				List<EObject> result = new ArrayList<>();
				for (var iter = eObj.eAllContents(); iter.hasNext(); ) {
					EObject desc = iter.next();
					if (isExactType(desc, filterType)) {
						result.add(desc);
					}
				}
				return wrapNull(result);
			}
			return wrapNull(new ArrayList<>());
		}
		// §8.3.4.9: allSubobjectsOfKind(type) — all descendants, includes subtypes
		if ("allSubobjectsOfKind".equals(opName) && !opCall.getOwnedArguments().isEmpty()) {
			EClass filterType = resolveEClassArg(evalFn.apply(opCall.getOwnedArguments().get(0)));
			if (filterType != null) {
				List<EObject> result = new ArrayList<>();
				for (var iter = eObj.eAllContents(); iter.hasNext(); ) {
					EObject desc = iter.next();
					if (filterType.isInstance(desc)) {
						result.add(desc);
					}
				}
				return wrapNull(result);
			}
			return wrapNull(new ArrayList<>());
		}
		// §8.3.4.10: clone() — shallow copy (containments NOT cloned)
		if ("clone".equals(opName)) {
			return wrapNull(cloneElement(eObj, false, extentOf.apply(eObj)));
		}
		// §8.3.4.11: deepclone() — deep copy (containments cloned recursively)
		if ("deepclone".equals(opName)) {
			return wrapNull(cloneElement(eObj, true, extentOf.apply(eObj)));
		}
		// §8.3.4 (MOF reflective): container() — containing object
		if ("container".equals(opName)) {
			return wrapNull(eObj.eContainer());
		}
		return UNHANDLED;
	}

	/**
	 * §8.3.5.5 addElement: "The object is first displaced from any usage by setting its container
	 * to null, then it is added to the model's extent so that it provides another root" (#342).
	 */
	static void addElement(QvtoModelExtent extent, EObject element) {
		checkExtentWritable(extent);
		EcoreUtil.remove(element);
		if (!extent.getContents().contains(element)) {
			extent.add(element);
		}
	}

	/**
	 * §8.3.5.6 removeElement: "All references to or from the object are eliminated. References
	 * from collections are removed. References from non-collections are set to null." — for the
	 * element and what it contains, across the extent; it leaves its container or the roots (#342).
	 */
	static void removeElement(QvtoModelExtent extent, EObject element) {
		checkExtentWritable(extent);
		Set<EObject> removed = new LinkedHashSet<>();
		removed.add(element);
		element.eAllContents().forEachRemaining(removed::add);
		for (EObject other : allObjects(extent)) {
			if (!removed.contains(other)) {
				dropReferencesTo(other, removed);
			}
		}
		for (EObject gone : removed) {
			dropReferencesTo(gone, null);
		}
		EcoreUtil.remove(element);
		extent.getContents().remove(element);
	}

	/** Clears the non-containment references of {@code holder} to {@code targets}, or all of them when null. */
	@SuppressWarnings("unchecked")
	private static void dropReferencesTo(EObject holder, Set<EObject> targets) {
		for (EReference reference : holder.eClass().getEAllReferences()) {
			if (reference.isContainment() || reference.isContainer() || !reference.isChangeable()
					|| reference.isDerived() || !holder.eIsSet(reference)) {
				continue;
			}
			if (reference.isMany()) {
				List<EObject> values = (List<EObject>) holder.eGet(reference);
				values.removeIf(value -> targets == null || targets.contains(value));
			} else if (targets == null || targets.contains(holder.eGet(reference))) {
				holder.eUnset(reference);
			}
		}
	}

	/**
	 * §8.3.5.8 copy: "Creates a deep copy of a model and its extent" — one copier for all roots,
	 * so references between them point into the copy, not back to the original (#342).
	 */
	static QvtoModelExtent copy(QvtoModelExtent extent) {
		BasicQvtoModelExtent copyExtent = new BasicQvtoModelExtent();
		copyExtent.setContents(new ArrayList<>(EcoreUtil.copyAll(extent.getContents())));
		return copyExtent;
	}

	/**
	 * §8.3.4.10/§8.3.4.11 clone and deepclone: "The returned object is added to the root of the
	 * model containing the source element" (§8.1.3.4: the extent of the source object, #342). An
	 * element of a read-only (in) model has its copy returned without being added — the model
	 * cannot change.
	 *
	 * @param deep whether contained objects are copied as well
	 * @param sourceExtent the extent the source element belongs to, may be {@code null}
	 */
	static EObject cloneElement(EObject source, boolean deep, QvtoModelExtent sourceExtent) {
		EObject copy;
		if (deep) {
			copy = EcoreUtil.copy(source);
		} else {
			EcoreUtil.Copier copier = new EcoreUtil.Copier() {
				private static final long serialVersionUID = 1L;

				@Override
				protected void copyContainment(EReference ref, EObject src, EObject tgt) {
					// shallow: skip containment references
				}
			};
			copy = copier.copy(source);
			copier.copyReferences();
		}
		if (sourceExtent != null && !sourceExtent.isReadOnly()) {
			sourceExtent.add(copy);
		}
		return copy;
	}

	/**
	 * §8.1.3.2: Guards mutation operations on read-only extents.
	 */
	private static void checkExtentWritable(QvtoModelExtent extent) {
		if (extent.isReadOnly()) {
			throw new QvtoControlFlowException.RaiseException(
					"InvalidModelMutation",
					"Cannot modify read-only model extent (in-parameter)");
		}
	}

	/**
	 * §8.3.5.1: every object of the extent — its roots and what they contain, each once and
	 * in document order. A root may also sit inside another root of the same extent (an
	 * object created into the extent and then put into a containment), hence the set.
	 *
	 * <p>This and the three below are shared with {@link QvtoOperationProvider}, which answers
	 * the same operations when the OCL engine evaluates the call.
	 */
	static Set<EObject> allObjects(QvtoModelExtent extent) {
		Set<EObject> result = new LinkedHashSet<>();
		for (EObject root : extent.getContents()) {
			if (result.add(root)) {
				for (var iter = root.eAllContents(); iter.hasNext(); ) {
					result.add(iter.next());
				}
			}
		}
		return result;
	}

	/** §8.3.5.3: the objects of the extent whose type is exactly the given one. */
	static List<EObject> objectsOfType(QvtoModelExtent extent, Object typeArg) {
		EClass filterType = resolveEClassArg(typeArg);
		List<EObject> result = new ArrayList<>();
		if (filterType == null) {
			return result; // a data type or null names no model object: none has that type (#342)
		}
		for (EObject eo : allObjects(extent)) {
			if (isExactType(eo, filterType)) {
				result.add(eo);
			}
		}
		return result;
	}

	/** §8.3.5.2: the objects of the extent that are of the given type or a subtype. */
	static List<EObject> objectsOfKind(QvtoModelExtent extent, Object typeArg) {
		EClass filterType = resolveEClassArg(typeArg);
		List<EObject> result = new ArrayList<>();
		if (filterType == null) {
			return result; // a data type or null names no model object: none has that type (#342)
		}
		for (EObject eo : allObjects(extent)) {
			if (filterType.isInstance(eo)) {
				result.add(eo);
			}
		}
		return result;
	}

	/** §8.3.5.4: the objects "not contained by other objects in the model". */
	static List<EObject> rootObjects(QvtoModelExtent extent) {
		Set<EObject> all = allObjects(extent);
		List<EObject> roots = new ArrayList<>();
		for (EObject eo : extent.getContents()) {
			// An object added to the extent and later put into a containment of another
			// object of the same model is no longer a root
			if (eo.eContainer() == null || !all.contains(eo.eContainer())) {
				roots.add(eo);
			}
		}
		return roots;
	}

	/**
	 * §8.3.5.3 / §8.3.4.6: Exact type check that is robust across EPackage instances.
	 * <p>Unlike {@code eo.eClass() == filterType} (which fails when EClasses come from
	 * different EPackage instances in dynamic EMF), this compares by nsURI + name.
	 */
	static boolean isExactType(EObject eo, EClass filterType) {
		EClass actual = eo.eClass();
		if (actual == filterType) {
			return true;
		}
		return actual.getName().equals(filterType.getName())
				&& Objects.equals(
						actual.getEPackage().getNsURI(),
						filterType.getEPackage().getNsURI());
	}

	static EClass resolveEClassArg(Object arg) {
		if (arg instanceof EClass ec) {
			return ec;
		}
		return null;
	}

	static Object wrapNull(Object value) {
		return value == null ? WRAPPED_NULL : value;
	}

	static Object unwrapNull(Object value) {
		return value == WRAPPED_NULL ? null : value;
	}
}
