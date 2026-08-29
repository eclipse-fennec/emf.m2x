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
package org.eclipse.fennec.m2x.ocl.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Which thing a name means, in the scope one expression is being built in.
 *
 * <p>Split out of {@code AbstractExpressionBuilder} (#194), which mixed four jobs in 1300
 * lines — literals, environment and scoping, name resolution, string unescaping — and where
 * the resolution half is the one that keeps changing: #153 (an unresolved property), #158
 * (which packages are in scope), #186 (the registry searched by simple name) all landed here.
 * All four languages share this builder, so a resolution rule written here is written once
 * rather than four times.
 *
 * <p>The cut is between <em>what a name means</em> and <em>what expression is built for it</em>.
 * This class answers the first question and touches no AST: it takes the scope — the context
 * type, the package registry, the declared imports and aliases — and answers with metamodel
 * elements or with {@code null}. Reporting a miss, choosing a placeholder, setting a type on
 * an expression: all of that stays with the builder, which is where the AST is.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class OclNameResolver {

	private final EPackage.Registry packageRegistry;
	private EClassifier contextType;
	private Map<String, String> packageAliases = Map.of();
	private final Set<EPackage> importedPackages = new LinkedHashSet<>();

	/**
	 * The property names a {@code def:} of the document adds. They belong to their context
	 * classifier for every expression of that document, including one written before the
	 * {@code def:} — which is why a document collects them before it builds a single expression.
	 */
	private final Set<String> definedProperties = new HashSet<>();

	/**
	 * Creates a resolver for one context.
	 *
	 * @param contextType the type of {@code self}, may be {@code null}
	 * @param packageRegistry where a declared name is looked up, may be {@code null}
	 */
	public OclNameResolver(EClassifier contextType, EPackage.Registry packageRegistry) {
		this.contextType = contextType;
		this.packageRegistry = packageRegistry;
	}

	/**
	 * @param contextType the type of {@code self} for the expressions built from here on
	 */
	public void setContextType(EClassifier contextType) {
		this.contextType = contextType;
	}

	/**
	 * @return the type of {@code self}, may be {@code null}
	 */
	public EClassifier getContextType() {
		return contextType;
	}

	/**
	 * @return the registry declared names are looked up in, may be {@code null}
	 */
	public EPackage.Registry getPackageRegistry() {
		return packageRegistry;
	}

	/**
	 * @param aliases alias to package path, must not be {@code null}
	 */
	public void registerPackageAliases(Map<String, String> aliases) {
		this.packageAliases = Map.copyOf(Objects.requireNonNull(aliases, "aliases must not be null"));
	}

	/**
	 * Adds a package to the scope unqualified names resolve in. Idempotent.
	 *
	 * @param ePackage the declared package, must not be {@code null}
	 */
	public void importPackage(EPackage ePackage) {
		importedPackages.add(Objects.requireNonNull(ePackage, "ePackage must not be null"));
	}

	/**
	 * @param names the property names a {@code def:} adds, must not be {@code null}
	 */
	public void declareDefinedProperties(Collection<String> names) {
		definedProperties.addAll(Objects.requireNonNull(names, "names must not be null"));
	}

	/**
	 * @param propName a property name
	 * @return whether a {@code def:} of the document declares it
	 */
	public boolean isDefinedProperty(String propName) {
		return definedProperties.contains(propName);
	}

	/**
	 * The packages in scope for unqualified names: the context type's package first, then the
	 * imported ones in declaration order.
	 *
	 * @return the scope, never {@code null}
	 */
	public List<EPackage> scopePackages() {
		List<EPackage> scope = new ArrayList<>();
		if (contextType != null && contextType.getEPackage() != null) {
			addWithSubpackages(contextType.getEPackage(), scope);
		}
		for (EPackage imported : importedPackages) {
			addWithSubpackages(imported, scope);
		}
		return scope;
	}

	/**
	 * A declared package brings the packages it contains: the nsURI names the root of an Ecore
	 * tree, and a nested package's classifiers are part of that metamodel. Only declared roots
	 * expand — nothing outside the declaration comes into view.
	 */
	private static void addWithSubpackages(EPackage ePackage, List<EPackage> scope) {
		if (scope.contains(ePackage)) {
			return;
		}
		scope.add(ePackage);
		for (EPackage nested : ePackage.getESubpackages()) {
			addWithSubpackages(nested, scope);
		}
	}

	/**
	 * Looks a classifier up by its simple or qualified name, without reporting a miss.
	 *
	 * @param segments the name, simple or {@code pkg::…::Name}
	 * @return the classifier, or {@code null} if nothing in scope has that name
	 */
	public EClassifier findClassifier(List<String> segments) {
		if (segments.size() == 1) {
			String name = segments.get(0);
			if (contextType instanceof EClass contextClass) {
				EClassifier found = contextClass.getEPackage().getEClassifier(name);
				if (found != null) {
					return found;
				}
			}
			return findInScope(name);
		}
		String classifierName = segments.get(segments.size() - 1);
		String packageName = String.join("::", segments.subList(0, segments.size() - 1));
		packageName = packageAliases.getOrDefault(packageName, packageName);

		if (contextType instanceof EClass contextClass) {
			EPackage ctxPkg = contextClass.getEPackage();
			if (ctxPkg.getName().equals(packageName) || ctxPkg.getNsURI().equals(packageName)) {
				EClassifier found = ctxPkg.getEClassifier(classifierName);
				if (found != null) {
					return found;
				}
			}
		}
		// What this expression declared beats what happens to be registered: an import or an
		// alias names one package, the registry names whatever a bundle put there
		for (EPackage pkg : scopePackages()) {
			if (pkg.getName().equals(packageName) || pkg.getNsURI().equals(packageName)) {
				EClassifier found = pkg.getEClassifier(classifierName);
				if (found != null) {
					return found;
				}
			}
		}
		return findInRegistry(packageName, classifierName);
	}

	/**
	 * An unqualified name, in the packages this expression declared.
	 *
	 * @param name the simple classifier name
	 * @return the classifier, or {@code null}
	 */
	public EClassifier findInScope(String name) {
		for (EPackage pkg : scopePackages()) {
			EClassifier found = pkg.getEClassifier(name);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	/**
	 * The registry, asked by nsURI first and only then searched by name.
	 *
	 * <p>The nsURI is the identity of a package; a name is not. Searching the registry means
	 * asking every {@code EPackage.Descriptor} to instantiate its package — in an IDE, every
	 * model bundle that is registered — and then matching on a simple name, so a package
	 * another bundle registered under the same name can answer for one this document meant
	 * (#186). The search stays, because a qualified name without an import has nowhere else to
	 * go, but it runs last and in a fixed order, so it at least answers the same way twice.
	 *
	 * @param packageName the package part of the name, an nsURI or a simple name
	 * @param classifierName the classifier to find in it
	 * @return the classifier, or {@code null}
	 */
	public EClassifier findInRegistry(String packageName, String classifierName) {
		if (packageRegistry == null) {
			return null;
		}
		EPackage byUri = packageRegistry.getEPackage(packageName);
		if (byUri != null) {
			EClassifier found = byUri.getEClassifier(classifierName);
			if (found != null) {
				return found;
			}
		}
		List<String> keys = new ArrayList<>();
		for (Object key : packageRegistry.keySet().toArray()) {
			keys.add((String) key);
		}
		Collections.sort(keys);
		for (String key : keys) {
			EPackage pkg = packageRegistry.getEPackage(key);
			if (pkg != null && (pkg.getName().equals(packageName)
					|| pkg.getNsURI().equals(packageName))) {
				EClassifier found = pkg.getEClassifier(classifierName);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}

	/**
	 * The literal of an enumeration named {@code Enum::LITERAL}, if one is in scope.
	 *
	 * @param enumName the enumeration's name
	 * @param literalName the literal's name
	 * @return the literal, or {@code null}
	 */
	public EEnumLiteral findEnumLiteral(String enumName, String literalName) {
		if (contextType instanceof EClass contextClass) {
			EEnumLiteral literal = findEnumLiteralIn(contextClass.getEPackage(), enumName, literalName);
			if (literal != null) {
				return literal;
			}
		}
		for (EPackage pkg : scopePackages()) {
			EEnumLiteral literal = findEnumLiteralIn(pkg, enumName, literalName);
			if (literal != null) {
				return literal;
			}
		}
		return null;
	}

	private static EEnumLiteral findEnumLiteralIn(EPackage pkg, String enumName, String literalName) {
		for (EClassifier classifier : pkg.getEClassifiers()) {
			if (classifier instanceof EEnum eEnum && eEnum.getName().equals(enumName)) {
				EEnumLiteral literal = eEnum.getEEnumLiteral(literalName);
				if (literal != null) {
					return literal;
				}
			}
		}
		return null;
	}

	/**
	 * The feature a type declares under that name.
	 *
	 * @param type the source type, may be {@code null} or not an {@link EClass}
	 * @param propName the property name
	 * @return the feature, or {@code null} if the type does not declare one
	 */
	public EStructuralFeature findFeature(EClassifier type, String propName) {
		return type instanceof EClass eClass ? eClass.getEStructuralFeature(propName) : null;
	}
}
