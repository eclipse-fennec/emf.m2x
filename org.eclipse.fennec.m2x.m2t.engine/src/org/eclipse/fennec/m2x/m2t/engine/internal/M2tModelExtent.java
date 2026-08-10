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
package org.eclipse.fennec.m2x.m2t.engine.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.ocl.api.OclModelExtent;

/**
 * The universe {@code allInstances()} sees inside a template.
 *
 * <p>MOFM2T builds on OCL, and OCL leaves the extent of a type to the tooling. The reference
 * answer is the one Acceleo uses — it hands Eclipse OCL its {@code ExtentMap}, which scopes the
 * extent by the <b>resource of the context object's root container</b>, or by that root's own
 * containment tree when it belongs to no resource. That is what this reproduces: a template
 * asking for {@code Book.allInstances()} sees the books of the model it is generating from, not
 * of every model that happens to be loaded.
 *
 * <p><b>Where it goes beyond the reference, and why.</b> Eclipse OCL derives the scope from
 * {@code self}, and yields an empty extent when {@code self} is not an {@code EObject}. In a
 * template that is not an edge case: inside {@code [for (name : String | …)]} the context is a
 * string, and {@code allInstances()} would silently answer with nothing in exactly the place a
 * template author is most likely to write it. The generation's input elements are then used
 * instead — the same model, named by the caller rather than found by navigation.
 *
 * <p>Instances are collected per class on first ask and kept, because a template asks the same
 * question in every iteration of a loop; without that, a single {@code [for]} over a large model
 * would rescan it once per pass. Subtypes count, as {@code EClass.isInstance} decides.
 *
 * <p><b>Not thread-safe</b>, and does not need to be: one extent belongs to one generation run,
 * which is single-threaded by construction.
 */
final class M2tModelExtent implements OclModelExtent {

	private final List<EObject> roots;
	private final Map<EClass, Collection<EObject>> instances = new HashMap<>();

	private M2tModelExtent(List<EObject> roots) {
		this.roots = roots;
	}

	/**
	 * The extent around a context object: the resource of its root container, or that root's
	 * tree when it has no resource.
	 *
	 * @param self the context object, must not be {@code null}
	 * @return the extent
	 */
	static M2tModelExtent aroundSelf(EObject self) {
		return new M2tModelExtent(scopeOf(self));
	}

	/**
	 * The extent around the generation's input elements, for evaluations whose context is not
	 * an {@code EObject} and therefore names no model of its own.
	 *
	 * @param inputElements the input elements, must not be {@code null}
	 * @return the extent, empty if there are no input elements
	 */
	static M2tModelExtent aroundAll(List<? extends EObject> inputElements) {
		// A set by identity of the resolved scopes: two input elements from one resource must
		// not make its content count twice.
		Collection<EObject> merged = new LinkedHashSet<>();
		for (EObject inputElement : inputElements) {
			merged.addAll(scopeOf(inputElement));
		}
		return new M2tModelExtent(List.copyOf(merged));
	}

	@Override
	public Collection<EObject> getAllInstances(EClass eClass) {
		if (eClass == null) {
			return List.of();
		}
		return instances.computeIfAbsent(eClass, this::collect);
	}

	private Collection<EObject> collect(EClass eClass) {
		List<EObject> found = new ArrayList<>();
		// getAllContents yields the roots themselves as well, which is what makes a root
		// element of the searched type part of its own extent.
		for (Iterator<Notifier> contents = EcoreUtil.getAllContents(roots); contents.hasNext();) {
			Notifier next = contents.next();
			if (next instanceof EObject candidate && eClass.isInstance(candidate)) {
				found.add(candidate);
			}
		}
		return List.copyOf(found);
	}

	/** The scope one element implies: its root container's resource, or that root alone. */
	private static List<EObject> scopeOf(EObject element) {
		EObject root = EcoreUtil.getRootContainer(element);
		Resource resource = root.eResource();
		return resource == null ? List.of(root) : List.copyOf(resource.getContents());
	}
}
