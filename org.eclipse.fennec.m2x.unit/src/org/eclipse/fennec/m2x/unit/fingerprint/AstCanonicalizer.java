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
package org.eclipse.fennec.m2x.unit.fingerprint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;

/**
 * Writes an AST as one canonical string: the same tree yields the same text, whatever object
 * identities, registration order, serialization or whitespace produced it.
 *
 * <p>This is the m2x1 canonicalization, and it is <b>frozen</b>: a change to what this class
 * writes changes every fingerprint ever computed, so it must come with a new scheme tag rather
 * than an edit here. The rules, in the order they are applied to a node:
 *
 * <ol>
 *   <li>The node's class as {@code nsURI#Name}, so two metamodels with equal class names differ.</li>
 *   <li>Every set, non-derived, non-transient, non-volatile feature, sorted by name — a
 *       reflective walk with an <em>exclusion</em> list, not a hand-written switch with an
 *       inclusion list: the m2x metamodels grow, and a forgotten class would make two different
 *       units silently share a value. A new feature changes the fingerprint; that is the
 *       conservative direction.</li>
 *   <li>Attributes as {@code EcoreUtil.convertToString}; many-valued in order — operand and
 *       statement order is semantic, and {@code isOrdered()} is true by default and says
 *       nothing, so order is kept unless a feature is explicitly unordered.</li>
 *   <li>Containments recurse.</li>
 *   <li>A non-containment reference to an object <em>in a resource</em> — an {@code EClass} of
 *       a metamodel, a type of the standard library — is its URI, {@code nsURI#fragment}: a
 *       metamodel change does not cascade into every expression fingerprint, and the manifest
 *       keeps the package fingerprints separately.</li>
 *   <li>A non-containment reference into the walked tree is the target's fragment path from the
 *       root. One to an object <em>outside</em> the tree and outside any resource — a parser
 *       satellite: a variable, a wrapper type, a default expression — is walked inline, so the
 *       value depends on what the satellite says and not on which object says it. Two parses of
 *       one source therefore agree even where they created different satellite objects.</li>
 * </ol>
 *
 * <p>Excluded: {@code documentation} and GenModel annotations, container references, the
 * one-to-one back-link of a bidirectional pair ({@code EPackage.eFactoryInstance}). Source
 * positions and comments never enter — they live beside the AST, not in it (#110/#116).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class AstCanonicalizer {

	private static final Set<String> EXCLUDED_ANNOTATION_SOURCES = Set.of(
			"documentation", "http://www.eclipse.org/emf/2002/GenModel");

	private final EObject root;
	private final StringBuilder out = new StringBuilder();
	/** Objects on the current inline-walk path, to cut a cycle through satellites. */
	private final Set<EObject> inProgress = Collections.newSetFromMap(new IdentityHashMap<>());

	private AstCanonicalizer(EObject root) {
		this.root = root;
	}

	/**
	 * Returns the canonical form of the tree under {@code root}.
	 *
	 * @param root the AST root, typically the unit's script
	 * @return the canonical text, never {@code null}
	 */
	static String canonicalize(EObject root) {
		Objects.requireNonNull(root, "root must not be null");
		AstCanonicalizer canonicalizer = new AstCanonicalizer(root);
		canonicalizer.node(root);
		return canonicalizer.out.toString();
	}

	private static boolean isExcludedAnnotation(EObject object) {
		return object instanceof EAnnotation annotation
				&& EXCLUDED_ANNOTATION_SOURCES.contains(String.valueOf(annotation.getSource()));
	}

	private void node(EObject object) {
		if (!inProgress.add(object)) {
			out.append("(@cycle)");
			return;
		}
		try {
			EClass eClass = object.eClass();
			out.append('(').append(classifierUri(eClass));
			List<EStructuralFeature> features = new ArrayList<>(eClass.getEAllStructuralFeatures());
			features.sort(Comparator.comparing(EStructuralFeature::getName));
			for (EStructuralFeature feature : features) {
				if (skip(feature, object)) {
					continue;
				}
				if (feature instanceof EAttribute attribute) {
					out.append(' ').append(feature.getName()).append('=');
					attributeValue(attribute, object.eGet(feature));
					continue;
				}
				EReference reference = (EReference) feature;
				Object value = object.eGet(feature, false);
				if (reference.isContainment() && reference.isMany()) {
					// An excluded annotation is not there at all — neither as an entry nor as an
					// empty list, or its mere presence would change the value
					List<EObject> kept = new ArrayList<>();
					for (Object element : (List<?>) value) {
						if (!isExcludedAnnotation((EObject) element)) {
							kept.add((EObject) element);
						}
					}
					if (kept.isEmpty()) {
						continue;
					}
					value = kept;
				} else if (reference.isContainment() && isExcludedAnnotation((EObject) value)) {
					continue;
				}
				out.append(' ').append(feature.getName()).append('=');
				referenceValue(reference, value);
			}
			out.append(')');
		} finally {
			inProgress.remove(object);
		}
	}

	private static boolean skip(EStructuralFeature feature, EObject owner) {
		// Derived and transient carry no information of their own. Volatile does: Ecore's own
		// ETypedElement.eType is volatile (computed from eGenericType) and is exactly what says
		// which type a feature has — dropping it left an attribute's type out of the value.
		if (feature.isDerived() || feature.isTransient()) {
			return true;
		}
		if (feature instanceof EReference reference) {
			if (reference.isContainer()) {
				return true;
			}
			EReference opposite = reference.getEOpposite();
			if (opposite != null && !reference.isMany() && !opposite.isMany()
					&& !opposite.isContainment() && !reference.isContainment()) {
				return true;
			}
		}
		return !owner.eIsSet(feature);
	}

	private void attributeValue(EAttribute attribute, Object value) {
		EDataType type = attribute.getEAttributeType();
		if (attribute.isMany()) {
			out.append('[');
			boolean first = true;
			for (Object element : (List<?>) value) {
				if (!first) {
					out.append(',');
				}
				first = false;
				literal(type, element);
			}
			out.append(']');
		} else {
			literal(type, value);
		}
	}

	private void literal(EDataType type, Object value) {
		if (value == null) {
			out.append("null");
			return;
		}
		String text = EcoreUtil.convertToString(type, value);
		out.append('"');
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '"' || c == '\\') {
				out.append('\\');
			}
			out.append(c);
		}
		out.append('"');
	}

	private void referenceValue(EReference reference, Object value) {
		if (reference.isMany()) {
			out.append('[');
			boolean first = true;
			for (Object element : (List<?>) value) {
				if (!first) {
					out.append(',');
				}
				first = false;
				target(reference, (EObject) element);
			}
			out.append(']');
		} else {
			target(reference, (EObject) value);
		}
	}

	private void target(EReference reference, EObject target) {
		if (target == null) {
			out.append("null");
			return;
		}
		if (reference.isContainment()) {
			node(target);
			return;
		}
		if (target.eIsProxy()) {
			out.append('<').append(EcoreUtil.getURI(target)).append('>');
			return;
		}
		if (isInTree(target)) {
			out.append("<#").append(EcoreUtil.getRelativeURIFragmentPath(root, target)).append('>');
			return;
		}
		if (isInDocument(target)) {
			// A satellite in the compiled unit's container, beside the script: what it says,
			// inline — the same as an uncontained satellite of a freshly parsed graph, so a parse,
			// its compiled unit and that unit reloaded from XMI agree
			node(target);
			return;
		}
		if (target.eResource() != null || SatelliteCollector.isMetamodelElement(target)) {
			// A metamodel type, a standard-library type, anything that lives elsewhere: by URI.
			// A metamodel counts as elsewhere even without a resource — a package initialized
			// from generated code or built in memory is addressable by nsURI just the same.
			out.append('<').append(EcoreUtil.getURI(target)).append('>');
			return;
		}
		// An uncontained satellite of a parsed graph: what it says, inline
		node(target);
	}

	/**
	 * Whether the root is an ancestor of the object. Not {@code getRootContainer(object) == root}:
	 * in a compiled unit the script sits below the {@code CompiledUnit}, and the absolute root of
	 * an object in the script is that unit, not the script.
	 */
	private boolean isInTree(EObject object) {
		for (EObject current = object; current != null; current = current.eContainer()) {
			if (current == root) {
				return true;
			}
		}
		return false;
	}

	/** In the same document as the root — the compiled unit around the script — but not in the script. */
	private boolean isInDocument(EObject object) {
		EObject documentRoot = EcoreUtil.getRootContainer(root);
		return documentRoot != root && EcoreUtil.getRootContainer(object) == documentRoot;
	}

	private static String classifierUri(EClass eClass) {
		String nsUri = eClass.getEPackage() == null ? "" : eClass.getEPackage().getNsURI();
		return nsUri + "#" + eClass.getName();
	}
}
