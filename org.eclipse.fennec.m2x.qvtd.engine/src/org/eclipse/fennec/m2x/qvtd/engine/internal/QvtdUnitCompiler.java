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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.qvtbase.Function;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnit;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnitResolver;
import org.eclipse.fennec.m2x.qvtd.parser.QvtrParserSupport;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.compile.SignatureFingerprint;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;

/**
 * Compile for QVT-R: parse → resolve → package (§4 of the compiled-unit concept, #139).
 *
 * <p>QVT-R binds an import by merging the imported relations into the importing transformation
 * (§7.11.1.1) — there is no reference left to an imported unit afterwards. That shapes the
 * modes:
 *
 * <ul>
 * <li><b>embed</b> — the dependency is compiled in turn and merged here, at compile time; the
 * import is struck from the transformation. The merged rules <em>are</em> the embedding, so no
 * embedded unit is attached — it would be dead weight nothing refers to. The manifest entry
 * records the name and the fingerprint of the unit that was merged.</li>
 * <li><b>pin</b> — the dependency is compiled to learn its unit fingerprint, which the manifest
 * records; nothing is merged, the import stays declared for prepare or execute to bind.</li>
 * <li><b>rebind</b> — the manifest records the name; the import stays declared.</li>
 * </ul>
 *
 * <p>A blackbox query — a {@code Function} declared without a body — is not a dependency: its
 * implementation is the runtime's. The manifest gets a requirement naming the operation and
 * the fingerprint of its signature; QVT-R addresses blackbox operations by name across all
 * registered libraries (§7.8), so there is no library to name.
 *
 * <p>Nothing a resolver lends is mutated: a source is parsed afresh, a bare AST is copied
 * together with its satellites, a unit that already sits in a compiled document is taken as a
 * copy of that document.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class QvtdUnitCompiler {

	static final String LANGUAGE = "qvtr";

	private final QvtrParserSupport parserSupport;
	private final EPackage.Registry packageRegistry;
	private final List<QvtdUnitResolver> unitResolvers;
	private final Set<String> allowedUnitModules;
	private final UnitPackager packager;
	private final DependencyMode mode;

	QvtdUnitCompiler(QvtrParserSupport parserSupport, EPackage.Registry packageRegistry,
			List<QvtdUnitResolver> unitResolvers, Set<String> allowedUnitModules,
			UnitPackager packager, UnitCompileOptions options) {
		this.parserSupport = Objects.requireNonNull(parserSupport, "parserSupport must not be null");
		this.packageRegistry = packageRegistry;
		this.unitResolvers = Objects.requireNonNull(unitResolvers, "unitResolvers must not be null");
		this.allowedUnitModules = Objects.requireNonNull(allowedUnitModules, "allowedUnitModules must not be null");
		this.packager = Objects.requireNonNull(packager, "packager must not be null");
		this.mode = Objects.requireNonNull(options, "options must not be null").dependencyMode();
	}

	CompiledUnit compile(String source, String qualifiedName) throws QvtdParseException {
		RelationalTransformation transformation = parserSupport.parse(source, qualifiedName, packageRegistry);
		return compile(transformation, qualifiedName, source, new ArrayDeque<>());
	}

	private CompiledUnit compile(RelationalTransformation transformation, String qualifiedName,
			String source, Deque<String> path) throws QvtdParseException {
		if (path.contains(qualifiedName)) {
			throw new QvtdParseException("Circular import detected: " + qualifiedName, List.of());
		}
		path.push(qualifiedName);
		CompiledUnit document;
		try {
			document = packager.begin(LANGUAGE, qualifiedName, transformation, mode, source);
		} catch (IllegalArgumentException e) {
			throw new QvtdParseException("Cannot compile '" + qualifiedName + "': " + e.getMessage(), e);
		}
		for (String imported : importedNames(transformation)) {
			QvtdUnit unit = resolveUnit(imported)
					.orElseThrow(() -> new QvtdParseException("Cannot resolve import: " + imported, List.of()));
			bind(transformation, imported, unit, document, path);
		}
		for (Function query : blackboxQueries(transformation)) {
			document.getManifest().getBlackboxRequirement().add(requirement(query));
		}
		path.pop();
		try {
			return packager.seal(document);
		} catch (IllegalStateException e) {
			throw new QvtdParseException("Cannot compile '" + qualifiedName + "': " + e.getMessage(), e);
		}
	}

	private void bind(RelationalTransformation transformation, String imported, QvtdUnit unit,
			CompiledUnit document, Deque<String> path) throws QvtdParseException {
		DependencyEntry entry = CompiledFactory.eINSTANCE.createDependencyEntry();
		entry.setQualifiedName(imported);
		entry.setMode(mode);
		switch (mode) {
			case EMBED -> {
				CompiledUnit dependency = compileDependency(unit, imported, path);
				QvtdLinker.merge(transformation, (RelationalTransformation) dependency.getUnit());
				strikeImport(transformation, imported);
				entry.setFingerprint(dependency.getManifest().getUnitFingerprint());
			}
			case PIN -> entry.setFingerprint(fingerprintOf(unit, imported, path));
			case REBIND -> {
				// resolved, so the name is known to be servable; merged at prepare time
			}
		}
		document.getManifest().getDependencyEntry().add(entry);
	}

	private CompiledUnit compileDependency(QvtdUnit unit, String qualifiedName, Deque<String> path)
			throws QvtdParseException {
		return switch (unit) {
			case QvtdUnit.SourceUnit source -> compile(
					parserSupport.parse(source.source(), qualifiedName, packageRegistry),
					qualifiedName, source.source(), path);
			case QvtdUnit.CompiledUnit compiled -> {
				if (compiled.transformation().eContainer() instanceof CompiledUnit document) {
					yield EcoreUtil.copy(document);
				}
				yield compile((RelationalTransformation) UnitPackager.detach(compiled.transformation()),
						qualifiedName, null, path);
			}
		};
	}

	private String fingerprintOf(QvtdUnit unit, String qualifiedName, Deque<String> path)
			throws QvtdParseException {
		if (unit instanceof QvtdUnit.CompiledUnit compiled
				&& compiled.transformation().eContainer() instanceof CompiledUnit document
				&& document.getManifest() != null && document.getManifest().getUnitFingerprint() != null) {
			return document.getManifest().getUnitFingerprint();
		}
		return compileDependency(unit, qualifiedName, path).getManifest().getUnitFingerprint();
	}

	private Optional<QvtdUnit> resolveUnit(String qualifiedName) {
		if (!allowedUnitModules.isEmpty() && !allowedUnitModules.contains(qualifiedName)) {
			return Optional.empty();
		}
		for (QvtdUnitResolver resolver : unitResolvers) {
			Optional<QvtdUnit> unit = resolver.resolveUnit(qualifiedName);
			if (unit.isPresent()) {
				return unit;
			}
		}
		return Optional.empty();
	}

	static List<String> importedNames(RelationalTransformation transformation) {
		EAnnotation annotation = transformation.getEAnnotation(QvtrParserSupport.IMPORTS_ANNOTATION);
		if (annotation == null) {
			return List.of();
		}
		return new ArrayList<>(new LinkedHashSet<>(annotation.getDetails().values()));
	}

	/**
	 * Removes a bound import from the declaration, so that nothing asks for it again: the
	 * rules are in the transformation now. An annotation left without details goes as a whole.
	 */
	private static void strikeImport(RelationalTransformation transformation, String imported) {
		EAnnotation annotation = transformation.getEAnnotation(QvtrParserSupport.IMPORTS_ANNOTATION);
		if (annotation == null) {
			return;
		}
		annotation.getDetails().removeIf(detail -> imported.equals(detail.getValue()));
		if (annotation.getDetails().isEmpty()) {
			transformation.getEAnnotations().remove(annotation);
		}
	}

	/** The queries declared without a body — what the runtime has to supply (§7.8). */
	static List<Function> blackboxQueries(RelationalTransformation transformation) {
		List<Function> queries = new ArrayList<>();
		for (EClassifier classifier : transformation.getEClassifiers()) {
			if (classifier instanceof EClass queriesClass) {
				for (EOperation operation : queriesClass.getEOperations()) {
					if (operation instanceof Function function && function.getQueryExpression() == null) {
						queries.add(function);
					}
				}
			}
		}
		return queries;
	}

	private static BlackboxRequirement requirement(Function query) {
		BlackboxRequirement requirement = CompiledFactory.eINSTANCE.createBlackboxRequirement();
		requirement.setName(query.getName());
		requirement.setSignatureFingerprint(SignatureFingerprint.of(List.of(signature(query))));
		return requirement;
	}

	/** {@code name(param,param):return}, types as {@code nsURI#Name}. */
	static String signature(Function query) {
		List<String> parameters = new ArrayList<>();
		for (EParameter parameter : query.getEParameters()) {
			parameters.add(typeKey(parameter.getEType()));
		}
		return query.getName() + "(" + String.join(",", parameters) + "):" + typeKey(query.getEType());
	}

	private static String typeKey(EClassifier type) {
		if (type == null) {
			return "";
		}
		String nsURI = type.getEPackage() == null ? "" : type.getEPackage().getNsURI();
		return nsURI + "#" + type.getName();
	}
}
