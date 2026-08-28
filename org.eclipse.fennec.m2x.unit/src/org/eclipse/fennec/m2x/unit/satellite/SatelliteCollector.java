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
package org.eclipse.fennec.m2x.unit.satellite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Finds the satellites of a parsed unit: the objects its tree references but that no feature
 * of any metamodel contains.
 *
 * <p>A parser creates objects as it goes — the variable {@code self}, a {@code PrimitiveType}
 * for every {@code Integer} it meets, a wrapper type for every classifier it resolves, the
 * default expression of an intermediate class — and the tree references them without owning
 * them. In memory that costs nothing, because a Java reference needs no owner. Saving and
 * copying do: XMI fails with a dangling reference, and a copy points its references back at
 * the originals. Measured on a QVT-O transformation with a mapping, a guard and an intermediate
 * class: thirteen such objects; on a minimal one with no import at all: still one, the type
 * {@code Integer}.
 *
 * <p>This class only <em>finds</em> them. Where they go is the caller's decision — a
 * {@code CompiledUnit.satellite} list in the normal case — and the caller places them with
 * {@link #contain(EObject, List)}, which iterates until nothing is left: a satellite may itself
 * reference a further uncontained object, and that one comes to light only once the first has a
 * root to be reached from.
 *
 * <p>Language-neutral by construction. It walks {@code eAllContents()} and every
 * non-containment, non-derived reference, and asks nothing about the type of what it finds.
 * That is what lets one collector serve QVT-O, QVT-R and MOFM2T: they share the OCL classes
 * their satellites are instances of, and they share {@code EPackage} as their unit root.
 *
 * <p>What is deliberately <b>not</b> a satellite: anything that already lives in a resource,
 * and anything that belongs to a metamodel — see {@link #isMetamodelElement}. A reference into
 * a metamodel — {@code EString}, a user's {@code EClass} — stays a cross-document reference and
 * must, because a unit lends its types rather than owning them; and a metamodel is one whether
 * it was loaded from an {@code .ecore} file, initialized from generated code or built in memory
 * and registered — none of the latter two need live in a resource. Anything already contained
 * in the tree is not one either, wherever in the tree it sits. And the one-to-one partner of a
 * bidirectional pair is not one: an {@code EPackage}'s {@code eFactoryInstance} is created by
 * EMF for every package and belongs to it by construction, not to a parser — see
 * {@link #isBackLinkToOwner}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class SatelliteCollector {

	/**
	 * How many rounds {@link #contain(EObject, List)} runs before it gives up. Each round
	 * places what the previous one exposed; a graph that needs more than this many is not a
	 * parse result but a cycle of references to nowhere.
	 */
	static final int MAX_ROUNDS = 32;

	private SatelliteCollector() {
	}

	/**
	 * Returns the objects the tree under {@code root} references but that live neither in that
	 * tree nor in any resource, in the order they are first met. Each object appears once.
	 *
	 * @param root the root of the tree to inspect, typically the unit
	 * @return the uncontained referenced objects, never {@code null}
	 */
	public static List<EObject> find(EObject root) {
		Objects.requireNonNull(root, "root must not be null");
		EObject documentRoot = EcoreUtil.getRootContainer(root);
		Set<EObject> seen = Collections.newSetFromMap(new IdentityHashMap<>());
		List<EObject> found = new ArrayList<>();
		visit(documentRoot, documentRoot, seen, found);
		for (Iterator<EObject> it = documentRoot.eAllContents(); it.hasNext();) {
			visit(it.next(), documentRoot, seen, found);
		}
		return found;
	}

	/**
	 * Moves every satellite of the tree under {@code root} into {@code container}, and repeats
	 * until the tree references nothing uncontained any more.
	 *
	 * <p>The container has to belong to the same document as {@code root} — the point is that
	 * afterwards every reference resolves within one document. Passing a container from
	 * elsewhere would only move the dangling reference one step.
	 *
	 * @param root the root of the tree to make self-contained
	 * @param container the containment list that becomes the satellites' home
	 * @return how many objects were placed
	 * @throws IllegalArgumentException if the container is not in {@code root}'s document
	 * @throws IllegalStateException if the tree does not become self-contained within
	 *             {@link #MAX_ROUNDS} rounds
	 */
	public static int contain(EObject root, List<EObject> container) {
		Objects.requireNonNull(root, "root must not be null");
		Objects.requireNonNull(container, "container must not be null");
		int placed = 0;
		for (int round = 0; round < MAX_ROUNDS; round++) {
			List<EObject> missing = find(root);
			if (missing.isEmpty()) {
				return placed;
			}
			container.addAll(missing);
			placed += missing.size();
			if (!missing.isEmpty() && EcoreUtil.getRootContainer(missing.get(0))
					!= EcoreUtil.getRootContainer(root)) {
				throw new IllegalArgumentException(
						"the container must belong to the same document as the root");
			}
		}
		throw new IllegalStateException("the tree under " + describe(root)
				+ " did not become self-contained within " + MAX_ROUNDS
				+ " rounds; " + find(root).size() + " objects are still uncontained");
	}

	private static void visit(EObject owner, EObject documentRoot, Set<EObject> seen,
			List<EObject> found) {
		for (EReference reference : owner.eClass().getEAllReferences()) {
			if (reference.isContainment() || reference.isContainer() || reference.isDerived()
					|| isBackLinkToOwner(reference) || !owner.eIsSet(reference)) {
				continue;
			}
			Object value = owner.eGet(reference, false);
			if (value instanceof List<?> values) {
				for (Object target : values) {
					consider(target, documentRoot, seen, found);
				}
			} else {
				consider(value, documentRoot, seen, found);
			}
		}
	}

	/**
	 * An {@code EPackage.eFactoryInstance} is the pattern: a bidirectional pair whose other
	 * end points back at the owner. Such a target belongs with the owner by construction —
	 * EMF creates the factory lazily for every package — and is no parser leftover. Moving it
	 * into a container would take it away from what it belongs to.
	 */
	private static boolean isBackLinkToOwner(EReference reference) {
		EReference opposite = reference.getEOpposite();
		return opposite != null && !reference.isMany() && !opposite.isContainment()
				&& !opposite.isMany();
	}

	private static void consider(Object target, EObject documentRoot, Set<EObject> seen,
			List<EObject> found) {
		if (target instanceof EObject candidate
				&& candidate.eResource() == null
				&& EcoreUtil.getRootContainer(candidate) != documentRoot
				&& !isMetamodelElement(candidate)
				&& seen.add(candidate)) {
			found.add(candidate);
		}
	}

	/**
	 * Whether an object belongs to a metamodel: its outermost container is a plain
	 * {@link EPackage} — an instance of Ecore's own {@code EPackage} class, not of a language
	 * metaclass derived from it — that carries an nsURI.
	 *
	 * <p>That is what tells a user's {@code Book} apart from a parser's leftovers without asking
	 * where either lives. A parser never produces a plain package with an nsURI: its stub for an
	 * import is a language {@code Module}, its intermediate package sits inside the unit, and
	 * the synthetic classifiers and features it leaves behind have no package at all. A
	 * metamodel, on the other hand, may live in a resource, or not — a generated package that
	 * was never given one, a package built in memory and put into a registry — and is external
	 * to the unit in every case. Its elements are addressable by nsURI and fragment, which is
	 * exactly how the fingerprint and XMI refer to them.
	 *
	 * @param object the object
	 * @return {@code true} if it is, or sits inside, a plain EPackage with an nsURI
	 */
	public static boolean isMetamodelElement(EObject object) {
		EObject root = EcoreUtil.getRootContainer(object);
		return root instanceof EPackage ePackage
				&& root.eClass() == EcorePackage.Literals.EPACKAGE
				&& ePackage.getNsURI() != null;
	}

	private static String describe(EObject object) {
		return object.eClass().getName();
	}
}
