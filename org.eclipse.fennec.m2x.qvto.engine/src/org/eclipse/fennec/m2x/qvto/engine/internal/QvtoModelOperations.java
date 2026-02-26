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
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.ocl.ClassifierType;
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

	QvtoModelOperations(Function<OclExpression, Object> evalFn) {
		this.evalFn = Objects.requireNonNull(evalFn);
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
			EClass filterType = resolveEClassArg(evalFn.apply(opCall.getOwnedArguments().get(0)));
			if (filterType != null) {
				List<EObject> result = new ArrayList<>();
				for (EObject eo : extent.getContents()) {
					if (isExactType(eo, filterType)) {
						result.add(eo);
					}
				}
				return wrapNull(result);
			}
			return wrapNull(new ArrayList<>(extent.getContents()));
		}
		// §8.3.5.2: objectsOfKind — includes subtypes
		if ("objectsOfKind".equals(opName) && !opCall.getOwnedArguments().isEmpty()) {
			EClass filterType = resolveEClassArg(evalFn.apply(opCall.getOwnedArguments().get(0)));
			if (filterType != null) {
				List<EObject> result = new ArrayList<>();
				for (EObject eo : extent.getContents()) {
					if (filterType.isInstance(eo)) {
						result.add(eo);
					}
				}
				return wrapNull(result);
			}
			return wrapNull(new ArrayList<>(extent.getContents()));
		}
		// §8.3.5.1: objects — all objects
		if ("objects".equals(opName)) {
			return wrapNull(new ArrayList<>(extent.getContents()));
		}
		// §8.3.5.4: rootObjects — top-level objects not contained by others
		if ("rootObjects".equals(opName)) {
			return wrapNull(new ArrayList<>(extent.getContents()));
		}
		// §8.3.5.5: addElement — add element to extent
		if ("addElement".equals(opName) && !opCall.getOwnedArguments().isEmpty()) {
			checkExtentWritable(extent);
			Object arg = evalFn.apply(opCall.getOwnedArguments().get(0));
			if (arg instanceof EObject eo) {
				extent.add(eo);
			}
			return wrapNull(null);
		}
		// §8.3.5.6: removeElement — remove element from extent
		if ("removeElement".equals(opName) && !opCall.getOwnedArguments().isEmpty()) {
			checkExtentWritable(extent);
			Object arg = evalFn.apply(opCall.getOwnedArguments().get(0));
			if (arg instanceof EObject eo) {
				extent.getContents().remove(eo);
			}
			return wrapNull(null);
		}
		// §8.3.5.9: createEmptyModel — create new empty model extent
		if ("createEmptyModel".equals(opName)) {
			return wrapNull(new BasicQvtoModelExtent());
		}
		// §8.3.5.8: copy — deep copy of model and its extent
		if ("copy".equals(opName)) {
			List<EObject> copies = new ArrayList<>();
			for (EObject eo : extent.getContents()) {
				copies.add(EcoreUtil.copy(eo));
			}
			BasicQvtoModelExtent copyExtent = new BasicQvtoModelExtent();
			copyExtent.setContents(copies);
			return wrapNull(copyExtent);
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
			return wrapNull(new ArrayList<>(eObj.eContents()));
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
			return wrapNull(new ArrayList<>(eObj.eContents()));
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
			EcoreUtil.Copier copier = new EcoreUtil.Copier() {
				private static final long serialVersionUID = 1L;
				@Override
				protected void copyContainment(EReference ref, EObject src, EObject tgt) {
					// shallow: skip containment references
				}
			};
			EObject result = copier.copy(eObj);
			copier.copyReferences();
			return wrapNull(result);
		}
		// §8.3.4.11: deepclone() — deep copy (containments cloned recursively)
		if ("deepclone".equals(opName)) {
			return wrapNull(EcoreUtil.copy(eObj));
		}
		// §8.3.4 (MOF reflective): container() — containing object
		if ("container".equals(opName)) {
			return wrapNull(eObj.eContainer());
		}
		return UNHANDLED;
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
		if (arg instanceof ClassifierType ct && ct.getReferredClassifier() instanceof EClass ec) {
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
