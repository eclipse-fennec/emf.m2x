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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationCallExp;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.LinkDiagnostics;
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
 * <p>A resolved unit makes its names visible (§7.1.1): the importing transformation takes its
 * base from it and calls its relations, and nothing else of it runs (#360).
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
	 * <p>Every import is tried; all that fail are reported together, each as a diagnostic at
	 * its declaration (#264).
	 *
	 * @param transformation the transformation to link, must not be {@code null}
	 * @throws QvtdParseException if an import cannot be resolved
	 */
	public void link(RelationalTransformation transformation) throws QvtdParseException {
		Objects.requireNonNull(transformation, "transformation must not be null");
		LinkDiagnostics failures = new LinkDiagnostics();
		for (String qualifiedName : importedNames(transformation)) {
			if (!resolved.add(qualifiedName)) {
				continue; // the same unit named twice, or reached again through a chain
			}
			try {
				linkImport(transformation, qualifiedName);
			} catch (QvtdParseException failure) {
				failures.add(failure.getMessage(), parserSupport.positionOfImport(transformation, qualifiedName),
						failure);
			}
		}
		if (!failures.hasErrors()) {
			for (Unresolved missing : unresolved(transformation)) {
				failures.add(missing.message(), missing.node() == null ? null : parserSupport.positionOf(missing.node()),
						null);
			}
		}
		if (failures.hasErrors()) {
			throw new QvtdParseException(failures.message(), failures.cause(), failures.getDiagnostics());
		}
	}

	private void linkImport(RelationalTransformation transformation, String qualifiedName)
			throws QvtdParseException {
		RelationalTransformation imported = resolveUnit(qualifiedName);
		if (imported == null) {
			throw new QvtdParseException("Cannot resolve import: " + qualifiedName, List.of());
		}
		// An imported unit may import in turn; what fails there is reported at this import.
		try {
			link(imported);
		} catch (QvtdParseException failure) {
			throw new QvtdParseException("In import '" + qualifiedName + "': " + failure.getMessage(), failure);
		}
		merge(transformation, imported);
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
	 * Binds an import (§7.1.1, §7.13.7): it makes the imported unit's names visible, it does not
	 * bring its relations into the run (#360). What the importing transformation takes from it is
	 * what it asks for — the base its {@code extends} names, inherited the way an {@code extends}
	 * within one unit is (§7.11.1.1), and the relations it calls.
	 *
	 * <p>A called relation is copied in as a non-top relation, so it runs when it is called and
	 * only then, together with the queries of its transformation. Deep copies for the same reason
	 * {@code extends} copies: a rule is contained by its transformation, so moving it would take
	 * it out of the unit it came from.
	 */
	static void merge(RelationalTransformation target, RelationalTransformation imported) {
		EAnnotation base = target.getEAnnotation(QvtrParserSupport.EXTENDS_ANNOTATION);
		if (base != null && target.getExtends() == null && !base.getDetails().containsKey(INHERITED)
				&& imported.getName().equals(base.getDetails().get("name"))) {
			QvtrParserSupport.inherit(target, imported);
			base.getDetails().put(INHERITED, imported.getName());
		}
		EAnnotation pending = target.getEAnnotation(QvtrParserSupport.PENDING_CALLS_ANNOTATION);
		if (pending == null) {
			return;
		}
		for (int i = 0; i < pending.getReferences().size(); i++) {
			if (!(pending.getReferences().get(i) instanceof RelationCallExp call) || call.getReferredRelation() != null) {
				continue;
			}
			String called = pending.getDetails().get(String.valueOf(i));
			int dot = called.lastIndexOf('.');
			String qualifier = dot < 0 ? null : called.substring(0, dot);
			String name = called.substring(dot + 1);
			Relation relation = qualifier == null ? relationNamed(target, name) : null;
			if (relation == null && (qualifier == null || qualifier.equals(imported.getName()))) {
				relation = callable(target, imported, name);
			}
			call.setReferredRelation(relation);
		}
	}

	/** Detail on the extends annotation once an imported base has been inherited. */
	private static final String INHERITED = "inherited";

	private static Relation relationNamed(RelationalTransformation transformation, String name) {
		for (Rule rule : transformation.getRule()) {
			if (rule instanceof Relation relation && name.equals(rule.getName())) {
				return relation;
			}
		}
		return null;
	}

	/** The imported relation of that name, copied into the target once, or {@code null}. */
	private static Relation callable(RelationalTransformation target, RelationalTransformation imported, String name) {
		Relation original = relationNamed(imported, name);
		if (original == null) {
			return null;
		}
		for (Rule rule : target.getRule()) {
			EAnnotation from = rule.getEAnnotation(IMPORTED_ANNOTATION);
			if (rule instanceof Relation relation && from != null && imported.getName().equals(from.getDetails().get("from"))
					&& name.equals(from.getDetails().get("name"))) {
				return relation;
			}
		}
		Relation copy = EcoreUtil.copy(original);
		copy.setIsTopLevel(false); // runs when it is called, not as a top relation of the importer
		if (relationNamed(target, name) != null) {
			copy.setName(imported.getName() + "." + name);
		}
		EAnnotation from = EcoreFactory.eINSTANCE.createEAnnotation();
		from.setSource(IMPORTED_ANNOTATION);
		from.getDetails().put("from", imported.getName());
		from.getDetails().put("name", name);
		copy.getEAnnotations().add(from);
		target.getRule().add(copy);
		QvtrParserSupport.inheritQueries(target, imported);
		return copy;
	}

	/** Annotation on a relation copied in because the importing transformation calls it. */
	static final String IMPORTED_ANNOTATION = "qvtr.imported";

	/** A name the imports did not answer for, and the node that used it, if any. */
	record Unresolved(String message, EObject node) {
	}

	/**
	 * What is still unresolved once every import is bound: a base named by {@code extends}, a
	 * rule named by {@code overrides}, a called relation. Each was left open by the parser
	 * because the unit imports others (#360); none of them may reach a run.
	 *
	 * @param transformation the linked transformation
	 * @return the open names, empty when everything resolved
	 */
	static List<Unresolved> unresolved(RelationalTransformation transformation) {
		List<Unresolved> open = new ArrayList<>();
		EAnnotation base = transformation.getEAnnotation(QvtrParserSupport.EXTENDS_ANNOTATION);
		if (base != null && transformation.getExtends() == null && !base.getDetails().containsKey(INHERITED)) {
			open.add(new Unresolved("Transformation '" + transformation.getName() + "' extends '"
					+ base.getDetails().get("name") + "', which is neither a transformation of its unit nor an imported one",
					null));
		}
		for (Rule rule : transformation.getRule()) {
			EAnnotation overrides = rule.getEAnnotation("qvtr.overrides");
			if (overrides != null && rule.getOverrides() == null) {
				open.add(new Unresolved("Relation '" + rule.getName() + "' overrides '"
						+ overrides.getDetails().get("name") + "', which is no relation of its transformation", null));
			}
		}
		EAnnotation pending = transformation.getEAnnotation(QvtrParserSupport.PENDING_CALLS_ANNOTATION);
		if (pending != null) {
			for (int i = 0; i < pending.getReferences().size(); i++) {
				if (pending.getReferences().get(i) instanceof RelationCallExp call && call.getReferredRelation() == null) {
					open.add(new Unresolved("Unknown relation or query '" + pending.getDetails().get(String.valueOf(i))
							+ "': neither this transformation nor an imported one declares it", call));
				}
			}
		}
		return open;
	}
}
