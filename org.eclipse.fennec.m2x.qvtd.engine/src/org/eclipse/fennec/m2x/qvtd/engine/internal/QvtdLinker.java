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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnit;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnitResolver;
import org.eclipse.fennec.m2x.qvtd.parser.QvtrParserSupport;
import org.eclipse.fennec.m2x.unit.api.UnitResolutionException;
import org.eclipse.fennec.m2x.unit.resolve.ResolutionPolicy;

/**
 * Resolves the units a transformation imports (§7.11.1).
 *
 * <p>The parser records the names an {@code import} asked for and stops there, because
 * resolving them needs unit resolvers and an allow-list. This is where that happens, and it
 * follows {@code QvtoLinker}: the allow-list is checked <em>before</em> anything is asked,
 * the resolvers are consulted in order, and a name nothing answers for is an error rather
 * than a silence.
 *
 * <p>A resolved unit contributes its rules to the importing transformation, the way an
 * {@code extends} does — rules the importing transformation declares itself win, so an
 * import cannot quietly replace a relation.
 *
 * @since 1.0
 */
public class QvtdLinker {

	private final QvtrParserSupport parserSupport;
	private final EPackage.Registry packageRegistry;
	private final List<QvtdUnitResolver> unitResolvers;
	private final Set<String> allowedUnitModules;

	/** Units resolved during this link, so that {@code maxUnitResolvers} bounds the whole. */
	private final Set<String> resolved = new HashSet<>();
	private QvtdReferencedUnits referencedUnits;

	public QvtdLinker(QvtrParserSupport parserSupport, EPackage.Registry packageRegistry,
			List<QvtdUnitResolver> unitResolvers, Set<String> allowedUnitModules) {
		this.parserSupport = Objects.requireNonNull(parserSupport, "parserSupport must not be null");
		this.packageRegistry = packageRegistry;
		this.unitResolvers = Objects.requireNonNull(unitResolvers, "unitResolvers must not be null");
		this.allowedUnitModules =
				Objects.requireNonNull(allowedUnitModules, "allowedUnitModules must not be null");
	}

	/**
	 * Resolves every import of the given transformation and merges what they bring.
	 *
	 * @param transformation the transformation to link, must not be {@code null}
	 * @throws QvtdParseException if an import cannot be resolved
	 */
	public void link(RelationalTransformation transformation) throws QvtdParseException {
		Objects.requireNonNull(transformation, "transformation must not be null");
		for (String qualifiedName : importedNames(transformation)) {
			if (!resolved.add(qualifiedName)) {
				continue; // the same unit named twice, or reached again through a chain
			}
			RelationalTransformation imported = resolveUnit(qualifiedName);
			if (imported == null) {
				throw new QvtdParseException("Cannot resolve import: " + qualifiedName, List.of());
			}
			// An imported unit may import in turn.
			link(imported);
			merge(transformation, imported);
		}
	}

	/**
	 * The names this transformation's {@code import} declarations asked for, in order.
	 */
	private List<String> importedNames(RelationalTransformation transformation) {
		EAnnotation annotation =
				transformation.getEAnnotation(QvtrParserSupport.IMPORTS_ANNOTATION);
		if (annotation == null) {
			return List.of();
		}
		return new ArrayList<>(new LinkedHashSet<>(annotation.getDetails().values()));
	}

	/**
	 * Asks the resolvers for a unit, after the allow-list has had its say.
	 *
	 * <p>An empty allow-list puts no restriction on names — the enable flag is the gate, and
	 * the engine has already decided whether to hand any resolvers over at all.
	 */
	private RelationalTransformation resolveUnit(String qualifiedName) throws QvtdParseException {
		if (!allowedUnitModules.isEmpty() && !allowedUnitModules.contains(qualifiedName)) {
			return null;
		}
		Optional<QvtdUnit> unit;
		try {
			unit = ResolutionPolicy.resolve(qualifiedName, sources(unitResolvers));
		} catch (UnitResolutionException failure) {
			throw new QvtdParseException("Cannot resolve import '" + qualifiedName + "': " + failure.getMessage(),
					failure);
		}
		return unit.isPresent() ? toTransformation(referencedUnits().dereference(unit.get()), qualifiedName) : null;
	}

	/** The configured resolvers as sources, in configuration order — the order that decides. */
	static List<ResolutionPolicy.Source<QvtdUnit>> sources(List<QvtdUnitResolver> resolvers) {
		List<ResolutionPolicy.Source<QvtdUnit>> sources = new ArrayList<>();
		for (QvtdUnitResolver resolver : resolvers) {
			sources.add(ResolutionPolicy.Source.of(resolver.getClass().getName(), resolver::resolveUnit));
		}
		return sources;
	}

	private RelationalTransformation toTransformation(QvtdUnit unit, String qualifiedName)
			throws QvtdParseException {
		return switch (unit) {
			case QvtdUnit.CompiledUnit compiled -> compiled.transformation();
			case QvtdUnit.SourceUnit source -> parserSupport.parse(source.source(), qualifiedName, packageRegistry);
			// dereferenced before any switch sees it
			case QvtdUnit.ResourceUnit reference -> toTransformation(referencedUnits().dereference(reference),
					qualifiedName);
		};
	}

	private QvtdReferencedUnits referencedUnits() {
		if (referencedUnits == null) {
			referencedUnits = new QvtdReferencedUnits(packageRegistry);
		}
		return referencedUnits;
	}

	/**
	 * Copies the imported rules in, leaving the importing transformation's own alone.
	 *
	 * <p>Deep-copied for the same reason {@code extends} does it: a rule is contained by its
	 * transformation, so moving it would take it out of the unit it came from.
	 */
	/**
	 * Binds an import the QVT-R way: the imported rules are copied into the importing
	 * transformation, a rule whose name the target already carries is skipped (§7.11.1.1).
	 */
	static void merge(RelationalTransformation target, RelationalTransformation imported) {
		Set<String> own = new HashSet<>();
		for (Rule rule : target.getRule()) {
			own.add(rule.getName());
		}
		for (Rule rule : new ArrayList<>(imported.getRule())) {
			if (own.add(rule.getName())) {
				target.getRule().add(EcoreUtil.copy(rule));
			}
		}
	}
}
