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
package org.eclipse.fennec.m2x.unit.compile;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * The EPackages a compiled-unit document refers to from outside itself — the metamodels the
 * unit was compiled against.
 *
 * <p>Found reflectively: every reference that leaves the document is followed to the package it
 * lands in. Packages that are part of every runtime by definition — Ecore, XMLType, the OCL
 * standard library — are not the unit's business and are left out; a package inside the
 * document (an intermediate package, a copy already carried) is not external and does not appear
 * either.
 *
 * <p>A package is <em>dynamic</em> when it has no generated code: its instance is a plain
 * {@link EPackageImpl}, not a generated subclass. Those are the packages a runtime may be unable
 * to supply on its own, and the ones a compiled unit carries a copy of.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class ReferencedPackages {

	/** nsURIs every runtime has; never recorded. */
	static final Set<String> ALWAYS_PRESENT = Set.of(
			EcorePackage.eNS_URI,
			XMLTypePackage.eNS_URI,
			"http://www.eclipse.org/fennec/m2x/ocl/stdlib/1.0");

	private ReferencedPackages() {
	}

	/**
	 * Collects the packages referenced from outside the given document, sorted by nsURI.
	 *
	 * @param document the compiled-unit document, or any tree
	 * @return the packages by nsURI, never {@code null}
	 */
	public static TreeMap<String, EPackage> of(EObject document) {
		Objects.requireNonNull(document, "document must not be null");
		TreeMap<String, EPackage> packages = new TreeMap<>();
		for (EObject target : EcoreUtil.ExternalCrossReferencer.find(document).keySet()) {
			if (target.eIsProxy()) {
				continue;
			}
			EPackage ePackage = packageOf(target);
			if (ePackage == null || ePackage.getNsURI() == null
					|| ALWAYS_PRESENT.contains(ePackage.getNsURI())
					|| EcoreUtil.isAncestor(document, ePackage)) {
				continue;
			}
			packages.putIfAbsent(ePackage.getNsURI(), ePackage);
		}
		return packages;
	}

	/**
	 * Returns whether a package has no generated code behind it.
	 *
	 * @param ePackage the package
	 * @return {@code true} for a dynamic package
	 */
	public static boolean isDynamic(EPackage ePackage) {
		return ePackage.getClass() == EPackageImpl.class;
	}

	/**
	 * The outermost packages of the given ones — what to copy so that a subpackage keeps its
	 * super-package and sibling references.
	 *
	 * @param packages the packages
	 * @return their roots, in first-seen order and without duplicates
	 */
	public static Collection<EPackage> roots(Collection<EPackage> packages) {
		Set<EPackage> roots = new LinkedHashSet<>();
		for (EPackage ePackage : packages) {
			EPackage root = ePackage;
			while (root.getESuperPackage() != null) {
				root = root.getESuperPackage();
			}
			roots.add(root);
		}
		return roots;
	}

	private static EPackage packageOf(EObject target) {
		return switch (target) {
			case EPackage p -> p;
			case EClassifier c -> c.getEPackage();
			case EStructuralFeature f -> f.getEContainingClass() == null ? null
					: f.getEContainingClass().getEPackage();
			case EOperation o -> o.getEContainingClass() == null ? null
					: o.getEContainingClass().getEPackage();
			case EParameter p -> p.getEOperation() == null || p.getEOperation().getEContainingClass() == null
					? null : p.getEOperation().getEContainingClass().getEPackage();
			case EEnumLiteral l -> l.getEEnum() == null ? null : l.getEEnum().getEPackage();
			case EGenericType g -> g.getEClassifier() == null ? null : g.getEClassifier().getEPackage();
			default -> EcoreUtil.getRootContainer(target) instanceof EPackage p ? p : null;
		};
	}
}
