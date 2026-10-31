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
package org.eclipse.fennec.m2x.qvtd.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtbase.Domain;
import org.eclipse.fennec.m2x.model.qvtbase.Function;
import org.eclipse.fennec.m2x.model.qvtbase.FunctionParameter;
import org.eclipse.fennec.m2x.model.qvtbase.Pattern;
import org.eclipse.fennec.m2x.model.qvtbase.Predicate;
import org.eclipse.fennec.m2x.model.qvtbase.QvtbaseFactory;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern;
import org.eclipse.fennec.m2x.model.qvtrelation.Key;
import org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationFactory;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationCallExp;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomainAssignment;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationImplementation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem;
import org.eclipse.fennec.m2x.model.qvttemplate.QvttemplateFactory;
import org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp;
import org.eclipse.fennec.m2x.ocl.api.ParseDiagnostics;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;
import org.eclipse.fennec.m2x.ocl.parser.AbstractExpressionBuilder;
import org.eclipse.fennec.m2x.ocl.parser.OclEnvironment;

/**
 * Visitor that transforms ANTLR4 parse tree nodes into QVT-R EMF AST nodes.
 *
 * <p>Handles transformation-level constructs: transformations, typed models, relations,
 * domains, templates, keys, and queries. Delegates OCL expression subtrees to a
 * {@link QvtrExpressionBuilder} instance.
 *
 * <p>Uses a two-pass strategy:
 * <ol>
 *   <li><b>Pre-pass:</b> Creates transformation shell with typed models (needed for domain resolution)</li>
 *   <li><b>Main pass:</b> Processes relations, keys, queries, filling the AST</li>
 * </ol>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtrUnitBuilder extends QvtRBaseVisitor<Object> {

	private static final OclFactory OCL = OclFactory.eINSTANCE;
	private static final QvtrelationFactory REL = QvtrelationFactory.eINSTANCE;
	private static final QvttemplateFactory TMPL = QvttemplateFactory.eINSTANCE;
	private static final QvtbaseFactory BASE = QvtbaseFactory.eINSTANCE;

	private final EPackage.Registry packageRegistry;
	private QvtrExpressionBuilder expressionBuilder;

	/** The expression builder's environment before any relation scope — self and nothing else. */
	private OclEnvironment baseEnvironment;
	private final List<Resource.Diagnostic> diagnostics = new ArrayList<>();

	/**
	 * Where the visitor currently is, so a problem found while building the AST is reported at a
	 * place rather than at line 0 (#110). The shared helper, because this bundle already depends
	 * on {@code ocl.parser} for the expression builder.
	 */
	private final ParseDiagnostics positions = new ParseDiagnostics();

	/** Current transformation being built. */
	private RelationalTransformation currentTransformation;
	/** Map of typed model name → TypedModel object for domain resolution. */
	private final Map<String, TypedModel> typedModelMap = new HashMap<>();
	/** Map of relation name → Relation object for RelationCallExp resolution. */
	private final Map<String, Relation> relationMap = new HashMap<>();
	/** The query names of the transformation being built, known before its relations (#358). */
	private final Set<String> queryNames = new HashSet<>();
	/** The relations already built — a second one of the same name gets its own object (#358). */
	private final Set<Relation> builtRelations = new HashSet<>();
	/** Where each transformation's extends clause names its base, for an unknown name. */
	private final Map<RelationalTransformation, int[]> extendsPositions = new HashMap<>();
	/** The relations with an overrides clause, and the name they override. */
	private final Map<Relation, PendingOverride> pendingOverrides = new LinkedHashMap<>();

	private record PendingOverride(String name, int line, int column) {
	}
	/** Whether the unit imports other units, whose names the link phase resolves (#360). */
	private boolean unitImports;
	/** The metamodels the imports made available, by alias or name (§7.1.1, #360). */
	private final Map<String, EPackage> importedMetamodels = new HashMap<>();
	/** Where each import was written, keyed by the annotation detail that carries it (#264). */
	private final Map<EObject, SourcePosition> importPositions = new IdentityHashMap<>();
	/**
	 * Where collection member and rest names and {@code implementedby} clauses stood — built here,
	 * not by the expression builder.
	 */
	private final Map<EObject, SourcePosition> templatePositions = new IdentityHashMap<>();
	/** Current relation being processed (for variable scope). */
	private Relation currentRelation;
	/** ObjectTemplateExps marked with [?] optional multiplicity (transient). */
	private final Set<ObjectTemplateExp> optionalTemplates = new HashSet<>();

	QvtrUnitBuilder(EPackage.Registry packageRegistry) {
		this.packageRegistry = packageRegistry;
	}

	// ==================== Entry Point ====================


	/**
	 * Keeps the diagnostic position with the descent — one override covers the visitor, because
	 * every step into the tree goes through here.
	 */
	@Override
	public Object visit(ParseTree tree) {
		if (tree instanceof ParserRuleContext context && context.getStart() != null) {
			positions.positionAt(context.getStart().getLine(),
					context.getStart().getCharPositionInLine());
		}
		return super.visit(tree);
	}
	@Override
	public List<RelationalTransformation> visitCompilationUnitEntry(
			QvtRParser.CompilationUnitEntryContext ctx) {
		return visitCompilationUnit(ctx.compilationUnit());
	}

	@Override
	public List<RelationalTransformation> visitCompilationUnit(QvtRParser.CompilationUnitContext ctx) {
		List<ImportedName> imports = importedUnitNames(ctx);
		unitImports = !imports.isEmpty();
		if (ctx.transformationDef().isEmpty()) {
			RelationalTransformation empty = REL.createRelationalTransformation();
			empty.setName("_unnamed");
			recordImports(empty, imports);
			return List.of(empty);
		}

		// Parse all transformations in the compilation unit (§7.13.7 topLevelCS: a set of them)
		Map<String, RelationalTransformation> transformations = new LinkedHashMap<>();
		for (QvtRParser.TransformationDefContext tCtx : ctx.transformationDef()) {
			// Reset per-transformation state
			typedModelMap.clear();
			relationMap.clear();
			RelationalTransformation t = visitTransformationDef(tCtx);
			if (transformations.containsKey(t.getName())) {
				keyError(tCtx.name, "Duplicate transformation '" + t.getName() + "'");
				continue;
			}
			transformations.put(t.getName(), t);
		}

		for (RelationalTransformation t : transformations.values()) {
			recordImports(t, imports);
		}

		// Resolve 'extends' annotations (§7.11.1.1): merge base rules into child
		Set<RelationalTransformation> deferred = new HashSet<>();
		for (RelationalTransformation t : transformations.values()) {
			EAnnotation ann = t.getEAnnotation(QvtrParserSupport.EXTENDS_ANNOTATION);
			String baseName = ann != null ? ann.getDetails().get("name") : null;
			if (baseName == null) {
				continue;
			}
			RelationalTransformation base = transformations.get(baseName);
			if (base != null) {
				t.setExtends(base);
				mergeInheritedRules(t, base);
			} else if (unitImports) {
				deferred.add(t); // an imported transformation, which the link phase has (#360)
			} else if (extendsPositions.containsKey(t)) {
				int[] at = extendsPositions.get(t);
				positions.positionAt(at[0], at[1]);
				diagnostics.add(positions.at("Transformation '" + t.getName() + "' extends '" + baseName
						+ "', which is no transformation of this unit"));
			}
		}

		resolveOverrides(transformations.values(), deferred);
		return new ArrayList<>(transformations.values());
	}

	/**
	 * Sets {@code Rule.overrides} (§7.13.7 relationCS): the rule of that name in the relation's
	 * transformation, its inherited rules included. A name that names no rule is a positioned
	 * error — before #356 the reference was set only by the evaluator, at run time, into the
	 * caller's AST, and an unknown name was ignored.
	 */
	private void resolveOverrides(Collection<RelationalTransformation> transformations,
			Set<RelationalTransformation> deferred) {
		for (RelationalTransformation t : transformations) {
			for (Rule rule : t.getRule()) {
				EAnnotation ann = rule.getEAnnotation("qvtr.overrides");
				String name = ann != null ? ann.getDetails().get("name") : null;
				if (name == null) {
					continue;
				}
				for (Rule candidate : t.getRule()) {
					if (candidate != rule && name.equals(candidate.getName())) {
						rule.setOverrides(candidate);
					}
				}
				PendingOverride pending = pendingOverrides.get(rule);
				// with an imported base the rule may override an inherited one: the link phase checks it
				if (rule.getOverrides() == null && pending != null && !deferred.contains(t)) {
					positions.positionAt(pending.line(), pending.column());
					diagnostics.add(positions.at("Relation '" + rule.getName() + "' overrides '" + name
							+ "', which is no relation of its transformation"));
				}
			}
		}
		pendingOverrides.clear();
	}

	/**
	 * The units a compilation unit imports, after the metamodel imports among them are taken
	 * care of (§7.1.1, §7.13.1, §7.13.4).
	 *
	 * <p>A quoted URI the package registry knows names a metamodel: it is made available under
	 * its alias, or under its own name, and {@code ::Sub} selects a subpackage. Any other quoted
	 * URI and every unquoted qualified name name a compilation unit, which the link phase
	 * resolves. A trailing {@code ::*} imports all names below the target, so it does not change
	 * what is asked for; an alias together with a wildcard is an error (§7.1.1).
	 */
	private List<ImportedName> importedUnitNames(QvtRParser.CompilationUnitContext ctx) {
		List<ImportedName> names = new ArrayList<>();
		for (QvtRParser.ImportDeclContext importCtx : ctx.importDecl()) {
			if (importCtx.alias != null && importCtx.wildcard != null) {
				keyError(importCtx, "An alias and a wildcard cannot be used together (§7.1.1)");
				continue;
			}
			QvtRParser.ImportTargetContext target = importCtx.importTarget();
			if (target.STRING_LITERAL() != null) {
				String uri = unquote(target.STRING_LITERAL().getText());
				if (packageRegistry.get(uri) != null) {
					importMetamodel(importCtx, uri);
					continue;
				}
				names.add(new ImportedName(uri, positionOf(target)));
			} else {
				names.add(new ImportedName(target.qualifiedName().getText(), positionOf(target)));
			}
		}
		return names;
	}

	/** Makes a registered metamodel, or one of its subpackages, available to model declarations. */
	private void importMetamodel(QvtRParser.ImportDeclContext importCtx, String uri) {
		EPackage pkg = packageRegistry.getEPackage(uri);
		if (pkg == null) {
			keyError(importCtx.importTarget(), "'" + uri + "' is not a metamodel");
			return;
		}
		for (QvtRParser.IdentifierContext segment : importCtx.identifier()) {
			if (segment == importCtx.alias) {
				continue;
			}
			String name = identifierText(segment);
			EPackage sub = null;
			for (EPackage candidate : pkg.getESubpackages()) {
				if (name.equals(candidate.getName())) {
					sub = candidate;
				}
			}
			if (sub == null) {
				keyError(segment, "Metamodel '" + pkg.getName() + "' has no subpackage '" + name + "'");
				return;
			}
			pkg = sub;
		}
		String name = importCtx.alias != null ? identifierText(importCtx.alias) : pkg.getName();
		importedMetamodels.put(name, pkg);
		if (importCtx.wildcard != null) {
			// all names below the target: its subpackages can be named on their own
			for (EPackage sub : pkg.getESubpackages()) {
				importedMetamodels.putIfAbsent(sub.getName(), sub);
			}
		}
	}

	private static String unquote(String literal) {
		return literal.length() >= 2 ? literal.substring(1, literal.length() - 1) : literal;
	}

	private static SourcePosition positionOf(ParserRuleContext ctx) {
		return ctx.getStart() == null ? null
				: new SourcePosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
	}

	/** An imported name and where it was written, for the link phase to report it at (#264). */
	private record ImportedName(String name, SourcePosition position) {
	}

	/**
	 * Keeps the imported names on the transformation so the link phase can resolve them.
	 *
	 * <p>An annotation rather than a model feature, the same way {@code extends} is carried
	 * (§7.11.1.1): the import is unresolved at this point, and a name is all there is to
	 * record.
	 */
	private void recordImports(RelationalTransformation transformation, List<ImportedName> imports) {
		if (imports.isEmpty()) {
			return;
		}
		EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
		annotation.setSource(QvtrParserSupport.IMPORTS_ANNOTATION);
		for (int i = 0; i < imports.size(); i++) {
			annotation.getDetails().put(String.valueOf(i), imports.get(i).name());
			// the detail entry is the node that stands for the declaration
			if (imports.get(i).position() != null
					&& annotation.getDetails().get(i) instanceof EObject entry) {
				importPositions.put(entry, imports.get(i).position());
			}
		}
		transformation.getEAnnotations().add(annotation);
	}

	/**
	 * Merges rules from a base transformation into the extending transformation
	 * (§7.11.1.1). Rules that are overridden by the child are not inherited.
	 * Extension is transitive: if the base itself extends another transformation,
	 * its inherited rules are already merged.
	 */
	static void mergeInheritedRules(RelationalTransformation child,
			RelationalTransformation base) {
		// Collect names of child's own relations (for override detection)
		Set<String> childRelationNames = new HashSet<>();
		for (Rule rule : child.getRule()) {
			childRelationNames.add(rule.getName());
		}

		// Inherit relations not overridden by child (deep copy to avoid
		// EMF containment issues — base rules stay in their own transformation)
		for (Rule baseRule : new ArrayList<>(base.getRule())) {
			if (!childRelationNames.contains(baseRule.getName())) {
				Rule copied = EcoreUtil.copy(baseRule);
				child.getRule().add(copied);
			}
		}

		// Inherit keys
		for (Key baseKey : new ArrayList<>(base.getOwnedKey())) {
			child.getOwnedKey().add(EcoreUtil.copy(baseKey));
		}

		mergeQueries(child, base);
	}

	/** Copies the base's queries the child does not declare (from the _queries synthetic EClass). */
	static void mergeQueries(RelationalTransformation child, RelationalTransformation base) {
		EClass baseQueries = findQueriesClass(base);
		if (baseQueries != null) {
			EClass childQueries = findQueriesClass(child);
			if (childQueries == null) {
				childQueries = EcoreFactory.eINSTANCE.createEClass();
				childQueries.setName("_queries");
				child.getEClassifiers().add(childQueries);
			}
			Set<String> childQueryNames = new HashSet<>();
			for (EOperation op : childQueries.getEOperations()) {
				childQueryNames.add(op.getName());
			}
			for (EOperation baseOp : baseQueries.getEOperations()) {
				if (!childQueryNames.contains(baseOp.getName())) {
					childQueries.getEOperations().add(EcoreUtil.copy(baseOp));
				}
			}
		}
	}

	/**
	 * Finds the synthetic {@code _queries} EClass in a transformation.
	 */
	private static EClass findQueriesClass(RelationalTransformation t) {
		for (EClassifier cls : t.getEClassifiers()) {
			if ("_queries".equals(cls.getName()) && cls instanceof EClass eClass) {
				return eClass;
			}
		}
		return null;
	}

	// ==================== Transformation ====================

	@Override
	public RelationalTransformation visitTransformationDef(
			QvtRParser.TransformationDefContext ctx) {
		currentTransformation = REL.createRelationalTransformation();
		// transformationId ::= pathNameCS (§7.13.4): the name is the last segment
		currentTransformation.setName(lastSegment(ctx.name));

		// Create expression builder with EObject as context type
		expressionBuilder = new QvtrExpressionBuilder(
				EcorePackage.Literals.EOBJECT, packageRegistry);
		baseEnvironment = expressionBuilder.env();

		// Process model declarations (typed models)
		for (QvtRParser.ModelDeclContext modelCtx : ctx.modelDecl()) {
			TypedModel tm = visitModelDecl(modelCtx);
			if (typedModelMap.containsKey(tm.getName())) {
				keyError(modelCtx, "Duplicate model parameter '" + tm.getName() + "'");
			}
			currentTransformation.getModelParameter().add(tm);
			typedModelMap.put(tm.getName(), tm);
		}

		// Handle 'extends' clause
		if (ctx.base != null) {
			// Stored as an annotation and resolved once all transformations of the unit — and
			// for an imported base, the imports — are known
			EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
			ann.setSource(QvtrParserSupport.EXTENDS_ANNOTATION);
			ann.getDetails().put("name", lastSegment(ctx.base));
			currentTransformation.getEAnnotations().add(ann);
			extendsPositions.put(currentTransformation, new int[] {
					ctx.base.getStart().getLine(), ctx.base.getStart().getCharPositionInLine() });
		}

		queryNames.clear();
		for (QvtRParser.QueryDefContext queryCtx : ctx.queryDef()) {
			List<String> segments = QvtrExpressionBuilder.pathNameSegments(queryCtx.pathName());
			queryNames.add(segments.get(segments.size() - 1));
		}

		// Pre-pass: collect all relation names for RelationCallExp resolution
		for (QvtRParser.RelationContext relCtx : ctx.relation()) {
			String relName = getRelationName(relCtx);
			if (relationMap.containsKey(relName)) {
				// Before #358 the second relation's domains were merged into the first
				keyError(relCtx, "Duplicate relation '" + relName + "'");
				continue;
			}
			Relation rel = REL.createRelation();
			rel.setName(relName);
			relationMap.put(relName, rel);
		}

		// Process key declarations
		for (QvtRParser.KeyDeclContext keyCtx : ctx.keyDecl()) {
			Key key = visitKeyDecl(keyCtx);
			currentTransformation.getOwnedKey().add(key);
		}

		// Process relations (main pass)
		for (QvtRParser.RelationContext relCtx : ctx.relation()) {
			Relation relation = visitRelation(relCtx);
			currentTransformation.getRule().add(relation);
		}

		// Process queries — stored as EOperations of a synthetic EClass inside
		// the transformation package (Transformation extends EPackage, not EClass)
		if (!ctx.queryDef().isEmpty()) {
			EClass queryHolder = EcoreFactory.eINSTANCE.createEClass();
			queryHolder.setName("_queries");
			currentTransformation.getEClassifiers().add(queryHolder);
			for (QvtRParser.QueryDefContext queryCtx : ctx.queryDef()) {
				Function function = visitQueryDef(queryCtx);
				queryHolder.getEOperations().add(function);
			}
		}

		checkRelationCalls(currentTransformation);
		return currentTransformation;
	}

	private static String lastSegment(QvtRParser.PathNameContext ctx) {
		List<String> segments = QvtrExpressionBuilder.pathNameSegments(ctx);
		return segments.get(segments.size() - 1);
	}

	/** Extracts the relation name from a RelationContext (for pre-pass). */
	private String getRelationName(QvtRParser.RelationContext ctx) {
		// identifier(0) is always the relation name
		return identifierText(ctx.identifier(0));
	}

	// ==================== Model Declaration ====================

	@Override
	public TypedModel visitModelDecl(QvtRParser.ModelDeclContext ctx) {
		TypedModel tm = BASE.createTypedModel();
		tm.setName(identifierText(ctx.identifier()));

		// Resolve metamodel packages
		for (QvtRParser.MetaModelIdContext mmCtx : ctx.metaModelId()) {
			List<String> segments = QvtrExpressionBuilder.pathNameSegments(mmCtx.pathName());
			String metamodelName = String.join("::", segments);

			// The name being resolved is right here, so the diagnostic can say so. ANTLR calls
			// the generated visit method directly rather than through visit(ParseTree), which is
			// why the descent hook alone does not reach this (#110).
			if (mmCtx.getStart() != null) {
				positions.positionAt(mmCtx.getStart().getLine(),
						mmCtx.getStart().getCharPositionInLine());
			}
			EPackage pkg = resolvePackage(metamodelName);
			if (pkg != null) {
				tm.getUsedPackage().add(pkg);
				// What the transformation declares is what its expressions may name (#158)
				expressionBuilder.support.importPackage(pkg);
			}
		}
		return tm;
	}

	// ==================== Key Declaration ====================

	@Override
	public Key visitKeyDecl(QvtRParser.KeyDeclContext ctx) {
		Key key = REL.createKey();

		// Resolve the class — a key of a class or property that does not exist identifies nothing,
		// and dropping it in silence left the transformation creating duplicates (#350)
		List<String> classPath = QvtrExpressionBuilder.pathNameSegments(ctx.pathName());
		EClassifier cls = resolveClassifier(classPath);
		if (!(cls instanceof EClass eClass)) {
			keyError(ctx, "Unknown key class (" + String.join("::", classPath) + ")");
			return key;
		}
		key.setIdentifies(eClass);

		// Process key properties
		for (QvtRParser.KeyPropertyContext propCtx : ctx.keyProperty()) {
			if (propCtx.OPPOSITE() != null) {
				// opposite(ClassName::propName) → oppositePart
				List<String> oppClassPath = QvtrExpressionBuilder.pathNameSegments(propCtx.pathName());
				String oppPropName = identifierText(propCtx.identifier());
				EClassifier oppCls = resolveClassifier(oppClassPath);
				EStructuralFeature feat = oppCls instanceof EClass oppEClass
						? oppEClass.getEStructuralFeature(oppPropName) : null;
				if (feat == null) {
					keyError(propCtx, "Unknown key property (opposite(" + String.join("::", oppClassPath)
							+ "::" + oppPropName + "))");
				} else {
					key.getOppositePart().add(feat);
				}
			} else {
				// Simple property name → part
				String propName = identifierText(propCtx.identifier());
				EStructuralFeature feat = eClass.getEStructuralFeature(propName);
				if (feat == null) {
					keyError(propCtx, "Unknown key property (" + eClass.getName() + "::" + propName + ")");
				} else {
					key.getPart().add(feat);
				}
			}
		}
		return key;
	}

	private void keyError(ParserRuleContext context, String message) {
		positions.positionAt(context.getStart().getLine(), context.getStart().getCharPositionInLine());
		diagnostics.add(positions.at(message));
	}

	// ==================== Relation ====================

	@Override
	public Relation visitRelation(QvtRParser.RelationContext ctx) {
		String name = identifierText(ctx.identifier(0));

		// Reuse pre-created relation from pre-pass — not for a duplicate, which is reported and
		// gets its own object instead of merging its domains into the first (#358)
		Relation relation = relationMap.get(name);
		if (relation == null || !builtRelations.add(relation)) {
			relation = REL.createRelation();
			relation.setName(name);
		}
		currentRelation = relation;

		// Set top-level
		if (ctx.TOP() != null) {
			relation.setIsTopLevel(true);
		}

		// Set abstract
		if (ctx.ABSTRACT() != null) {
			relation.setIsAbstract(true);
		}

		// Handle overrides: resolved once every transformation and its inherited rules are known
		// (§7.13.7 relationCS: "currTrans.extends.rule->any(r | r.name = …)", #356)
		if (ctx.OVERRIDES() != null && ctx.identifier().size() > 1) {
			String overridesName = identifierText(ctx.identifier(1));
			// The name travels with the relation, into the copies an extending transformation
			// inherits too
			EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
			ann.setSource("qvtr.overrides");
			ann.getDetails().put("name", overridesName);
			relation.getEAnnotations().add(ann);
			pendingOverrides.put(relation, new PendingOverride(overridesName,
					ctx.identifier(1).getStart().getLine(), ctx.identifier(1).getStart().getCharPositionInLine()));
		}

		// Process variable declarations
		for (QvtRParser.VarDeclarationContext varCtx : ctx.varDeclaration()) {
			List<Variable> vars = visitVarDeclaration(varCtx);
			relation.getVariable().addAll(vars);
		}

		// Process domains in source order (§7.13.5 domainListCS): a primitive domain's variable is
		// in scope for the domains after it, and the order is the one a relation call's arguments
		// follow. Before #278 all primitive domains came last, so `name = prefix` after
		// `primitive domain prefix : String` resolved to a stray variable of the same name.
		for (ParseTree child : ctx.children) {
			if (child instanceof QvtRParser.DomainContext domCtx) {
				relation.getDomain().add(visitDomain(domCtx));
			} else if (child instanceof QvtRParser.PrimitiveTypeDomainContext primCtx) {
				relation.getDomain().add(visitPrimitiveTypeDomain(primCtx));
			}
		}

		// Process when clause
		if (ctx.whenClause() != null) {
			relation.setWhen(visitWhenClause(ctx.whenClause()));
		}

		// Process where clause
		if (ctx.whereClause() != null) {
			relation.setWhere(visitWhereClause(ctx.whereClause()));
		}

		currentRelation = null;
		return relation;
	}

	// ==================== Variable Declaration ====================

	@Override
	public List<Variable> visitVarDeclaration(QvtRParser.VarDeclarationContext ctx) {
		EClassifier type = expressionBuilder.resolveTypeExpression(ctx.typeExpression());

		OclExpression initExp = null;
		if (ctx.expression() != null) {
			initExp = buildOcl(ctx.expression());
		}

		List<Variable> vars = new ArrayList<>();
		for (QvtRParser.IdentifierContext idCtx : ctx.identifier()) {
			Variable var = OCL.createVariable();
			var.setName(identifierText(idCtx));
			if (type != null) {
				var.setType(type);
			}
			if (initExp != null) {
				// One initializer per variable: a single expression set as the containment init
				// of each moved to the last one (#357)
				var.setOwnedInit(vars.isEmpty() ? initExp : EcoreUtil.copy(initExp));
			}
			vars.add(var);
		}
		return vars;
	}

	// ==================== Domain ====================

	@Override
	public RelationDomain visitDomain(QvtRParser.DomainContext ctx) {
		RelationDomain domain = REL.createRelationDomain();

		// checkonly / enforce
		if (ctx.checkEnforceQualifier() != null) {
			String qualifier = ctx.checkEnforceQualifier().getText();
			if ("checkonly".equals(qualifier)) {
				domain.setIsCheckable(true);
				domain.setIsEnforceable(false);
			} else if ("enforce".equals(qualifier)) {
				domain.setIsCheckable(true);
				domain.setIsEnforceable(true);
			}
		}

		// Domain name = model identifier → resolve TypedModel
		String modelId = identifierText(ctx.identifier());
		domain.setName(modelId);
		TypedModel tm = typedModelMap.get(modelId);
		if (tm != null) {
			domain.setTypedModel(tm);
		} else {
			keyError(ctx.identifier(), "Unknown model '" + modelId + "': the transformation declares "
					+ String.join(", ", typedModelMap.keySet()));
		}

		// Process templates → DomainPattern(s)
		for (QvtRParser.TemplateContext tmplCtx : ctx.template()) {
			TemplateExp templateExp = visitTemplate(tmplCtx);
			if (templateExp instanceof CollectionTemplateExp) {
				// Decided in #354: the root of a domain is an object template
				keyError(tmplCtx, "A collection template cannot be the root of a domain; use an object template");
			}
			DomainPattern pattern = REL.createDomainPattern();
			pattern.setTemplateExpression(templateExp);

			// An anonymous root gets an implicit variable, as Eclipse does: rootVariable is [1..*]
			// (§7.11.3.3, decided in #358)
			if (templateExp instanceof ObjectTemplateExp root && root.getBindsTo() == null) {
				root.setBindsTo(templateVariable(modelId + "$root" + domain.getPattern().size(),
						root.getReferredClass(), tmplCtx));
			}
			if (tm != null) {
				checkClassesOfModel(templateExp, tm, tmplCtx);
			}

			// Root variable comes from the template's bindsTo
			if (templateExp.getBindsTo() != null) {
				domain.getRootVariable().add(templateExp.getBindsTo());
				// Also register in relation's variable list
				if (currentRelation != null) {
					currentRelation.getVariable().add(templateExp.getBindsTo());
				}
			}

			// [?] optional multiplicity → mark on DomainPattern (has EModelElement)
			if (templateExp instanceof ObjectTemplateExp ote
					&& optionalTemplates.remove(ote)) {
				EAnnotation ann =
						EcoreFactory.eINSTANCE.createEAnnotation();
				ann.setSource("qvtr.optional");
				pattern.getEAnnotations().add(ann);
			}

			bindTemplateVariables(pattern, templateExp);
			domain.getPattern().add(pattern);
		}

		// implementedby clause (§7.11.3.6)
		if (ctx.IMPLEMENTEDBY() != null && ctx.pathName() != null && currentRelation != null) {
			List<String> implPath = QvtrExpressionBuilder.pathNameSegments(ctx.pathName());
			String implName = implPath.get(implPath.size() - 1);

			RelationImplementation relImpl = REL.createRelationImplementation();
			relImpl.setInDirectionOf(tm);
			recordPosition(relImpl, ctx.pathName());   // where a runtime warning points (#287)

			// Create synthetic EOperation for the impl reference
			EOperation implOp =
					EcoreFactory.eINSTANCE.createEOperation();
			implOp.setName(implName);
			relImpl.setImpl(implOp);

			// Store argument expressions as EAnnotation for engine evaluation
			if (ctx.argumentList() != null) {
				EAnnotation ann =
						EcoreFactory.eINSTANCE.createEAnnotation();
				ann.setSource("qvtr.implementedby.args");
				List<OclExpression> argExprs = new ArrayList<>();
				for (QvtRParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
					argExprs.add(buildOcl(argCtx));
				}
				// Store arg count and attach expressions via EAnnotation references
				ann.getDetails().put("count", String.valueOf(argExprs.size()));
				ann.getReferences().addAll(argExprs);
				relImpl.getEAnnotations().add(ann);
			}

			currentRelation.getOperationalImpl().add(relImpl);
		}

		// default_values clause
		if (ctx.assignmentExp() != null) {
			for (QvtRParser.AssignmentExpContext assignCtx : ctx.assignmentExp()) {
				RelationDomainAssignment assignment = visitAssignmentExp(assignCtx);
				domain.getDefaultAssignment().add(assignment);
			}
		}

		return domain;
	}

	@Override
	public RelationDomain visitPrimitiveTypeDomain(QvtRParser.PrimitiveTypeDomainContext ctx) {
		RelationDomain domain = REL.createRelationDomain();
		String varName = identifierText(ctx.identifier());
		domain.setName(varName);

		EClassifier type = expressionBuilder.resolveTypeExpression(ctx.typeExpression());

		Variable rootVar = OCL.createVariable();
		rootVar.setName(varName);
		if (type != null) {
			rootVar.setType(type);
		}
		domain.getRootVariable().add(rootVar);

		if (currentRelation != null) {
			currentRelation.getVariable().add(rootVar);
		}

		return domain;
	}

	// ==================== Template ====================

	@Override
	public TemplateExp visitTemplate(QvtRParser.TemplateContext ctx) {
		TemplateExp result;
		if (ctx.objectTemplate() != null) {
			result = visitObjectTemplate(ctx.objectTemplate());
		} else {
			result = visitCollectionTemplate(ctx.collectionTemplate());
		}

		// Optional where-guard: template '{' expression '}'
		if (ctx.expression() != null) {
			result.setWhere(buildOcl(ctx.expression()));
		}

		return result;
	}

	/**
	 * Binds a property template item to the feature of the object template's class (QVT v1.3
	 * §7.11.2.4: a property template item refers to a property of the referred class). A class
	 * that has no such property is an error at the item's position; a template whose class could
	 * not be resolved keeps the marked placeholder, the evaluator's by-name lookup its last resort.
	 */
	private void resolveTemplateProperty(PropertyTemplateItem item, EClassifier cls,
			QvtRParser.PropertyTemplateContext propCtx) {
		EStructuralFeature current = item.getReferredProperty();
		if (item.isIsOpposite() || !AbstractExpressionBuilder.isUnresolvedProperty(current)) {
			return;
		}
		if (cls instanceof EClass eClass) {
			EStructuralFeature feature = eClass.getEStructuralFeature(current.getName());
			if (feature != null) {
				item.setReferredProperty(feature);
				return;
			}
			positions.positionAt(propCtx.getStart().getLine(), propCtx.getStart().getCharPositionInLine());
			diagnostics.add(positions.at("Unknown property (" + eClass.getName() + "::" + current.getName() + ")"));
		}
	}

	@Override
	public ObjectTemplateExp visitObjectTemplate(QvtRParser.ObjectTemplateContext ctx) {
		ObjectTemplateExp ote = TMPL.createObjectTemplateExp();

		// Resolve the referred class from pathName
		List<String> classPath = QvtrExpressionBuilder.pathNameSegments(ctx.pathName());
		EClassifier cls = resolveClassifier(classPath);
		if (cls instanceof EClass eClass) {
			ote.setReferredClass(eClass);
		} else {
			keyError(ctx.pathName(), "Unknown class (" + String.join("::", classPath) + ")");
		}

		// Optional variable binding
		if (ctx.identifier() != null) {
			ote.setBindsTo(templateVariable(identifierText(ctx.identifier()), cls, ctx));
		}

		// [?] optional multiplicity (Eclipse extension): store temporarily
		// Will be transferred to DomainPattern.EAnnotation in visitDomain
		if (ctx.optionalMultiplicity() != null) {
			optionalTemplates.add(ote);
		}

		// Property template items
		if (ctx.propertyTemplateList() != null) {
			for (QvtRParser.PropertyTemplateContext propCtx :
					ctx.propertyTemplateList().propertyTemplate()) {
				PropertyTemplateItem item = visitPropertyTemplate(propCtx);
				resolveTemplateProperty(item, cls, propCtx);
				checkCollectionKind(item, propCtx);
				ote.getPart().add(item);
			}
		}

		return ote;
	}

	@Override
	public PropertyTemplateItem visitPropertyTemplate(QvtRParser.PropertyTemplateContext ctx) {
		PropertyTemplateItem item = TMPL.createPropertyTemplateItem();

		if (ctx.OPPOSITE() != null) {
			// opposite(ClassName::propName) = expression
			item.setIsOpposite(true);
			List<String> classPath = QvtrExpressionBuilder.pathNameSegments(ctx.pathName());
			String propName = identifierText(ctx.identifier());
			EClassifier cls = resolveClassifier(classPath);
			EStructuralFeature feat = cls instanceof EClass eClass ? eClass.getEStructuralFeature(propName) : null;
			if (feat != null) {
				item.setReferredProperty(feat);
			} else {
				keyError(ctx, "Unknown property (opposite(" + String.join("::", classPath) + "::" + propName + "))");
			}
		} else {
			// identifier = expression — resolved against the enclosing object template's class
			// in visitObjectTemplate, once that class is known; until then a marked placeholder
			// (#153: before, the placeholder was never replaced and became a stored satellite)
			String propName = identifierText(ctx.identifier());
			item.setIsOpposite(false);
			item.setReferredProperty(AbstractExpressionBuilder.unresolvedProperty(propName));
		}

		if (ctx.template() != null) {
			// identifier = template (nested ObjectTemplateExp / CollectionTemplateExp)
			item.setValue(visitTemplate(ctx.template()));
		} else {
			item.setValue(buildOcl(ctx.expression()));
		}

		return item;
	}

	@Override
	public CollectionTemplateExp visitCollectionTemplate(
			QvtRParser.CollectionTemplateContext ctx) {
		CollectionTemplateExp cte = TMPL.createCollectionTemplateExp();

		// Collection kind and element type
		String collKind = ctx.collectionKind().getText();
		EClassifier elementType = expressionBuilder.resolveTypeExpression(ctx.typeExpression());
		OclType collType = expressionBuilder.support.buildCollectionType(collKind, elementType);
		// The collection type itself, kind and element type — EObject lost the kind (#354)
		cte.setReferredCollectionType(collType);

		// Optional variable binding
		if (ctx.identifier() != null) {
			cte.setBindsTo(templateVariable(identifierText(ctx.identifier()), collType, ctx));
		}

		// Member selection
		if (ctx.memberSelection() != null) {
			QvtRParser.MemberSelectionContext selCtx = ctx.memberSelection();

			for (QvtRParser.MemberItemContext itemCtx : selCtx.memberItem()) {
				if (itemCtx.template() != null) {
					// Nested template → add as member expression
					TemplateExp nestedTemplate = visitTemplate(itemCtx.template());
					cte.getMember().add(nestedTemplate);
				} else if (itemCtx.identifier() != null) {
					// Variable reference: the declared variable of that name, not a new one per
					// mention — four Variable(pn) for one pn were satellites with no declaration
					// behind them (#154)
					VariableExp varExp = OCL.createVariableExp();
					varExp.setReferredVariable(relationVariable(identifierText(itemCtx.identifier())));
					recordPosition(varExp, itemCtx);
					cte.getMember().add(varExp);
				} else {
					// '_' wildcard → anonymous variable
					VariableExp varExp = OCL.createVariableExp();
					Variable anonVar = OCL.createVariable();
					anonVar.setName("_");
					varExp.setReferredVariable(anonVar);
					cte.getMember().add(varExp);
				}
			}

			// Rest variable (after '++')
			if (selCtx.identifier() != null) {
				// The declared variable of that name, as for members (§7.13.7 restCS, #357)
				Variable restVar = relationVariable(identifierText(selCtx.identifier()));
				if (restVar.eContainer() == null) {
					recordPosition(restVar, selCtx.identifier());
				}
				cte.setRest(restVar);
			} else if (selCtx.UNDERSCORE() != null) {
				Variable restVar = OCL.createVariable();
				restVar.setName("_");
				cte.setRest(restVar);
			}
		}

		return cte;
	}

	// ==================== When / Where ====================

	@Override
	public Pattern visitWhenClause(QvtRParser.WhenClauseContext ctx) {
		Pattern pattern = BASE.createPattern();
		for (QvtRParser.PredicateContext predicateCtx : ctx.predicate()) {
			Predicate predicate = BASE.createPredicate();
			OclExpression condExpr = buildPredicate(predicateCtx);
			predicate.setConditionExpression(condExpr);
			pattern.getPredicate().add(predicate);
			// §7.11.1.8 bindsTo: a relation call binds the variables it is called with (#357)
			if (condExpr instanceof RelationCallExp call) {
				for (OclExpression argument : call.getArgument()) {
					if (argument instanceof VariableExp ve && ve.getReferredVariable() != null
							&& !pattern.getBindsTo().contains(ve.getReferredVariable())) {
						pattern.getBindsTo().add(ve.getReferredVariable());
					}
				}
			}
		}
		return pattern;
	}

	@Override
	public Pattern visitWhereClause(QvtRParser.WhereClauseContext ctx) {
		Pattern pattern = BASE.createPattern();
		for (QvtRParser.PredicateContext predicateCtx : ctx.predicate()) {
			Predicate predicate = BASE.createPredicate();
			predicate.setConditionExpression(buildPredicate(predicateCtx));
			pattern.getPredicate().add(predicate);
		}
		return pattern;
	}

	/**
	 * A predicate of a when or where clause: an expression, or a template (§7.13.5
	 * {@code <OclExpressionCS> ::= … | <template>}), which holds when a match of it exists (#361).
	 */
	private OclExpression buildPredicate(QvtRParser.PredicateContext ctx) {
		if (ctx.template() == null) {
			return buildExpressionOrRelationCall(ctx.expression());
		}
		TemplateExp template = visitTemplate(ctx.template());
		if (template instanceof CollectionTemplateExp) {
			keyError(ctx, "A collection template cannot be a predicate; use an object template");
		}
		return template;
	}

	/**
	 * Builds an expression from a when/where clause context.
	 * Detects RelationCallExp: an identifier followed by '(' args ')' where the
	 * identifier matches a known relation name.
	 */
	private OclExpression buildExpressionOrRelationCall(QvtRParser.ExpressionContext ctx) {
		OclExpression expr = buildOcl(ctx);

		// A relation call is a Boolean expression usable anywhere — not R(p, s), R(p, s) or …
		// (decided in #358, as Eclipse): every call of a relation in the tree becomes a
		// RelationCallExp, the innermost first
		List<OperationCallExp> calls = new ArrayList<>();
		if (expr instanceof OperationCallExp call) {
			calls.add(call);
		}
		expr.eAllContents().forEachRemaining(o -> {
			if (o instanceof OperationCallExp call) {
				calls.add(call);
			}
		});
		Collections.reverse(calls);
		for (OperationCallExp call : calls) {
			RelationCallExp relCall = relationCall(call, ctx);
			if (relCall == null) {
				continue;
			}
			if (call == expr) {
				expr = relCall;
			} else {
				EcoreUtil.replace(call, relCall);
			}
		}
		return expr;
	}

	/**
	 * The relation call an operation call denotes: an implicit-source call of a relation, or one
	 * qualified with the transformation's own name (§7.13.7 RelationCallExpCS "(identifierCS[1]
	 * '.')? identifierCS[2]"). A qualifier naming no transformation, and an implicit-source call
	 * of a name that is neither a relation nor a query, are errors (#358).
	 */
	private RelationCallExp relationCall(OperationCallExp call, ParserRuleContext ctx) {
		String name = call.getName();
		if (name == null) {
			return null;
		}
		boolean relation = relationMap.containsKey(name);
		boolean qualified = false;
		if (!call.isIsImplicit() && call.getOwnedSource() instanceof VariableExp qualifier
				&& qualifier.getReferredVariable() != null && !isRelationVariable(qualifier.getReferredVariable())) {
			String transformationName = qualifier.getReferredVariable().getName();
			if (!transformationName.equals(currentTransformation.getName())) {
				if (unitImports && call.getReferredOperation() == null) {
					// a relation of an imported transformation: the link phase knows it (#360)
					return pendingRelationCall(call, transformationName, ctx);
				}
				if (relation) {
					keyError(ctx, "Unknown transformation '" + transformationName + "' in " + transformationName
							+ "." + name + "(…)");
				}
				return null;
			}
			qualified = true;
		}
		if (!relation) {
			if (call.isIsImplicit() && call.getReferredOperation() == null && !queryNames.contains(name)) {
				if (unitImports) {
					// an import makes the names of the imported unit visible (§7.1.1, #360)
					return pendingRelationCall(call, null, ctx);
				}
				keyError(ctx, "Unknown relation or query '" + name + "'");
			}
			return null;
		}
		if (!call.isIsImplicit() && !qualified) {
			return null;
		}
		RelationCallExp relCall = REL.createRelationCallExp();
		relCall.setReferredRelation(relationMap.get(name));
		relCall.getArgument().addAll(call.getOwnedArguments());
		recordPosition(relCall, ctx);
		return relCall;
	}

	/**
	 * A call of a relation this unit does not declare, while it imports units: the relation is
	 * looked for in the imported ones once the link phase has them (#360). The call is recorded
	 * on the transformation, which an AST node of OCL cannot carry.
	 */
	private RelationCallExp pendingRelationCall(OperationCallExp call, String transformationName,
			ParserRuleContext ctx) {
		RelationCallExp relCall = REL.createRelationCallExp();
		relCall.getArgument().addAll(call.getOwnedArguments());
		recordPosition(relCall, ctx);
		EAnnotation pending = currentTransformation.getEAnnotation(QvtrParserSupport.PENDING_CALLS_ANNOTATION);
		if (pending == null) {
			pending = EcoreFactory.eINSTANCE.createEAnnotation();
			pending.setSource(QvtrParserSupport.PENDING_CALLS_ANNOTATION);
			currentTransformation.getEAnnotations().add(pending);
		}
		pending.getDetails().put(String.valueOf(pending.getReferences().size()),
				transformationName == null ? call.getName() : transformationName + "." + call.getName());
		pending.getReferences().add(relCall);
		return relCall;
	}

	private boolean isRelationVariable(Variable variable) {
		return currentRelation != null && currentRelation.getVariable().contains(variable);
	}

	/**
	 * §7.11.3.8: "The number and types of the arguments must match the total number and types
	 * of the root variables of the domains" — checked once every relation of the transformation
	 * is built (#358).
	 */
	private void checkRelationCalls(RelationalTransformation transformation) {
		List<RelationCallExp> calls = new ArrayList<>();
		transformation.eAllContents().forEachRemaining(o -> {
			if (o instanceof RelationCallExp call) {
				calls.add(call);
			}
		});
		for (RelationCallExp call : calls) {
			Relation called = call.getReferredRelation();
			if (called == null) {
				continue;
			}
			List<Variable> roots = new ArrayList<>();
			for (Domain domain : called.getDomain()) {
				if (domain instanceof RelationDomain rd) {
					roots.addAll(rd.getRootVariable());
				}
			}
			SourcePosition at = templatePositions.get(call);
			if (roots.size() != call.getArgument().size()) {
				callError(at, "Relation '" + called.getName() + "' has " + roots.size() + " root variables, but is called with "
						+ call.getArgument().size() + " arguments");
				continue;
			}
			for (int i = 0; i < roots.size(); i++) {
				if (roots.get(i).getType() instanceof EClass expected
						&& call.getArgument().get(i).getType() instanceof EClass actual
						&& !expected.isSuperTypeOf(actual) && !actual.isSuperTypeOf(expected)) {
					callError(at, "Argument " + (i + 1) + " of the call of '" + called.getName() + "' is a "
							+ actual.getName() + ", but its root variable '" + roots.get(i).getName() + "' is a "
							+ expected.getName());
				}
			}
		}
	}

	private void callError(SourcePosition at, String message) {
		if (at != null) {
			positions.positionAt(at.line(), at.column());
		}
		diagnostics.add(positions.at(message));
	}

	/**
	 * A domain's templates may only use classes of its typed model's metamodels: §7.1 "the types
	 * of elements they can contain are restricted to those within a set of referenced packages"
	 * (#358).
	 */
	private void checkClassesOfModel(TemplateExp root, TypedModel model, ParserRuleContext ctx) {
		List<EObject> nodes = new ArrayList<>();
		nodes.add(root);
		root.eAllContents().forEachRemaining(nodes::add);
		for (EObject node : nodes) {
			if (node instanceof ObjectTemplateExp template && template.getReferredClass() != null
					&& !inPackages(template.getReferredClass().getEPackage(), model.getUsedPackage())) {
				keyError(ctx, "Class " + template.getReferredClass().getName() + " is not in a metamodel of model '"
						+ model.getName() + "'");
			}
		}
	}

	private static boolean inPackages(EPackage pkg, List<EPackage> packages) {
		for (EPackage p = pkg; p != null; p = p.getESuperPackage()) {
			if (packages.contains(p)) {
				return true;
			}
		}
		return false;
	}

	// ==================== Assignment ====================

	@Override
	public RelationDomainAssignment visitAssignmentExp(QvtRParser.AssignmentExpContext ctx) {
		RelationDomainAssignment assignment = REL.createRelationDomainAssignment();
		String varName = identifierText(ctx.identifier());

		// Look up variable in current relation
		if (currentRelation != null) {
			for (Variable v : currentRelation.getVariable()) {
				if (varName.equals(v.getName())) {
					assignment.setVariable(v);
					break;
				}
			}
		}
		if (assignment.getVariable() == null) {
			// §7.13.7: "assignmentExpCS.ast.variable = … variable->any(v | v.name = …)" — a name no
			// variable has is an error; a placeholder assigned nothing (#358)
			keyError(ctx, "default_values assigns '" + varName + "', which is no variable of relation '"
					+ (currentRelation != null ? currentRelation.getName() : "?") + "'");
			Variable placeholder = OCL.createVariable();
			placeholder.setName(varName);
			assignment.setVariable(placeholder);
		}

		assignment.setValueExp(buildOcl(ctx.expression()));
		return assignment;
	}

	// ==================== Query ====================

	@Override
	public Function visitQueryDef(QvtRParser.QueryDefContext ctx) {
		Function function = BASE.createFunction();

		List<String> nameSegments = QvtrExpressionBuilder.pathNameSegments(ctx.pathName());
		function.setName(nameSegments.get(nameSegments.size() - 1));

		// Parameters
		if (ctx.paramDecl() != null) {
			for (QvtRParser.ParamDeclContext paramCtx : ctx.paramDecl()) {
				FunctionParameter param = BASE.createFunctionParameter();
				param.setName(identifierText(paramCtx.identifier()));
				EClassifier paramType =
						expressionBuilder.resolveTypeExpression(paramCtx.typeExpression());
				if (paramType != null) {
					param.setEType(paramType); // its collection type too, not EObject (#354)
				}
				function.getEParameters().add(param);
			}
		}

		// Return type
		EClassifier returnType = expressionBuilder.resolveTypeExpression(ctx.typeExpression());
		if (returnType != null) {
			function.setEType(returnType);
		}

		// Query body expression (if not abstract/blackbox)
		if (ctx.expression() != null) {
			function.setQueryExpression(buildOcl(ctx.expression()));
		}

		return function;
	}

	// ==================== Resolution Helpers ====================

	/**
	 * Resolves an EPackage by name from the package registry.
	 */
	/**
	 * Where each expression node stood, for runtime diagnostics (#116), where each import
	 * was written, for link diagnostics (#264), and where collection member and rest names
	 * stood, for binding diagnostics (#278), and where each {@code implementedby} clause stood, for
	 * the §7.8 warning (#287).
	 *
	 * @return the positions, by node identity
	 */
	Map<EObject, SourcePosition> getNodePositions() {
		Map<EObject, SourcePosition> positions = new IdentityHashMap<>(importPositions);
		positions.putAll(templatePositions);
		if (expressionBuilder != null) {
			positions.putAll(expressionBuilder.getNodePositions());
		}
		return positions;
	}

	/**
	 * Returns the diagnostics collected while building — unresolved typed model
	 * packages and unresolved type names (#66).
	 */
	List<Resource.Diagnostic> getDiagnostics() {
		List<Resource.Diagnostic> all = new ArrayList<>(diagnostics);
		if (expressionBuilder != null) {
			all.addAll(expressionBuilder.support.getDiagnostics());
		}
		return all;
	}

	private EPackage resolvePackage(String name) {
		EPackage imported = importedMetamodels.get(name);
		if (imported != null) {
			return imported;
		}
		// Try direct URI lookup first
		Object pkg = packageRegistry.get(name);
		if (pkg instanceof EPackage ePkg) {
			return ePkg;
		}
		// Try finding by exact name in all registered packages
		EPackage caseInsensitiveMatch = null;
		for (Object value : packageRegistry.values()) {
			if (value instanceof EPackage ePkg) {
				if (name.equals(ePkg.getName())) {
					return ePkg;
				}
				// Track case-insensitive match as fallback
				if (caseInsensitiveMatch == null
						&& name.equalsIgnoreCase(ePkg.getName())) {
					caseInsensitiveMatch = ePkg;
				}
			}
		}
		if (caseInsensitiveMatch == null) {
			diagnostics.add(positions.at("Unknown metamodel (" + name + ")"));
		}
		return caseInsensitiveMatch;
	}

	/**
	 * Resolves an EClassifier from a path name using all typed models' packages.
	 */
	private EClassifier resolveClassifier(List<String> path) {

		// Search in all typed models' packages
		for (TypedModel tm : typedModelMap.values()) {
			for (EPackage pkg : tm.getUsedPackage()) {
				EClassifier cls = findClassifierInPackage(pkg, path);
				if (cls != null) {
					return cls;
				}
			}
		}

		// No fallback to the whole package registry: a class resolves only in the metamodels of
		// the declared model types (decided in #358)
		return null;
	}

	/**
	 * Finds a classifier in a package hierarchy following the path segments.
	 */
	private EClassifier findClassifierInPackage(EPackage pkg, List<String> path) {
		if (path.size() == 1) {
			EClassifier cls = pkg.getEClassifier(path.get(0));
			for (int i = 0; cls == null && i < pkg.getESubpackages().size(); i++) {
				cls = findClassifierInPackage(pkg.getESubpackages().get(i), path);
			}
			return cls;
		}
		// Multi-segment path: navigate sub-packages
		if (path.get(0).equals(pkg.getName())) {
			if (path.size() == 2) {
				return pkg.getEClassifier(path.get(1));
			}
			for (EPackage sub : pkg.getESubpackages()) {
				EClassifier cls = findClassifierInPackage(sub, path.subList(1, path.size()));
				if (cls != null) {
					return cls;
				}
			}
		}
		// Try direct lookup with last segment
		return pkg.getEClassifier(path.get(path.size() - 1));
	}

	/**
	 * Builds an OCL expression with the current relation's variables in scope.
	 *
	 * <p>Without this, {@code pn} in {@code name = pn} or {@code IsExported(pn)} was unknown to
	 * the OCL parser, which then created a fresh "external" Variable at every mention — four
	 * Variable(pn) for one declared pn, none of them the declaration (#154). The scope is
	 * re-established per expression because the relation's variable list grows while its
	 * domains are visited: a root variable bound by a domain is in scope for the domains and
	 * clauses after it.
	 */
	private OclExpression buildOcl(QvtRParser.ExpressionContext ctx) {
		OclEnvironment scope = currentRelation == null
				? baseEnvironment
				: baseEnvironment.nested(currentRelation.getVariable());
		expressionBuilder.setEnv(scope);
		try {
			return expressionBuilder.buildExpression(ctx);
		} finally {
			expressionBuilder.setEnv(baseEnvironment);
		}
	}

	/**
	 * The relation's variable of that name — declared in the relation or bound by a domain — so
	 * that one name means one Variable within a relation (#154).
	 *
	 * <p>A name declared nowhere gets a fresh variable that is <em>not</em> registered with the
	 * relation: §7.13.5 looks a member name up among the relation's variables and has no rule that
	 * declares one by mention. Registering it made an undeclared name look declared to the binding
	 * validator, which then let it through as bound (#278); unregistered, the validator reports it.
	 */
	private Variable relationVariable(String name) {
		if (currentRelation != null) {
			for (Variable declared : currentRelation.getVariable()) {
				if (name.equals(declared.getName())) {
					return declared;
				}
			}
		}
		Variable variable = OCL.createVariable();
		variable.setName(name);
		return variable;
	}

	/**
	 * The collection kinds a template may have for a multi-valued property (§7.11.2.4): "If the
	 * property value is a set, then the collection type of the collection template expression
	 * must be a set. If the property value is an ordered set [or a sequence], then [it] can be one
	 * of set, sequence, or ordered set." Any other is a parse error (decided in #354). A
	 * single-valued property is a singleton collection that any kind may match.
	 */
	private void checkCollectionKind(PropertyTemplateItem item, ParserRuleContext ctx) {
		EStructuralFeature feature = item.getReferredProperty();
		if (!(item.getValue() instanceof CollectionTemplateExp template) || item.isIsOpposite()
				|| feature == null || !feature.isMany()
				|| !(template.getReferredCollectionType() instanceof CollectionType type)) {
			return;
		}
		String propertyKind;
		boolean allowed;
		CollectionKind kind = type.getKind();
		if (feature.isUnique() && !feature.isOrdered()) {
			propertyKind = "Set";
			allowed = kind == CollectionKind.SET;
		} else if (feature.isUnique() || feature.isOrdered()) {
			propertyKind = feature.isUnique() ? "OrderedSet" : "Sequence";
			allowed = kind == CollectionKind.SET || kind == CollectionKind.ORDERED_SET
					|| kind == CollectionKind.SEQUENCE;
		} else {
			return; // a Bag: §7.11.2.4 has no rule for it
		}
		if (!allowed) {
			keyError(ctx, "A " + kind.getLiteral() + " template cannot match '" + feature.getName()
					+ "', which is a " + propertyKind + " (§7.11.2.4)");
		}
	}

	/**
	 * The variable a template binds: the relation's variable of that name if there is one — one
	 * name, one Variable (§7.13.7 objectTemplateCS) — or else a new one the relation now holds
	 * (§7.11.3.2 "This set includes all the variables occurring in its domains"). A declared type
	 * the template's type cannot be is an error (#357).
	 */
	private Variable templateVariable(String name, EClassifier type, ParserRuleContext ctx) {
		if (currentRelation != null) {
			for (Variable declared : currentRelation.getVariable()) {
				if (name.equals(declared.getName())) {
					EClassifier declaredType = declared.getType();
					if (declaredType == null) {
						declared.setType(type);
					} else if (type instanceof EClass templateClass && declaredType instanceof EClass declaredClass
							&& !declaredClass.isSuperTypeOf(templateClass) && !templateClass.isSuperTypeOf(declaredClass)) {
						positions.positionAt(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
						diagnostics.add(positions.at("Variable '" + name + "' is declared as " + declaredClass.getName()
								+ ", but the template binds it to a " + templateClass.getName()));
					}
					return declared;
				}
			}
		}
		Variable variable = OCL.createVariable();
		variable.setName(name);
		if (type != null) {
			variable.setType(type);
		}
		if (currentRelation != null) {
			currentRelation.getVariable().add(variable);
		}
		return variable;
	}

	/**
	 * Fills a domain pattern's bindsTo with the variables its templates bind: "The set of
	 * variables that are to be bound when the pattern is evaluated" (§7.11.1.8, #357) — template
	 * variables, variables a property is matched to, collection members and rest.
	 */
	private static void bindTemplateVariables(Pattern pattern, TemplateExp root) {
		if (root == null) {
			return;
		}
		List<EObject> nodes = new ArrayList<>();
		nodes.add(root);
		root.eAllContents().forEachRemaining(nodes::add);
		for (EObject node : nodes) {
			Variable bound = null;
			if (node instanceof TemplateExp template) {
				bound = template.getBindsTo();
				if (template instanceof CollectionTemplateExp collection && collection.getRest() != null) {
					addBound(pattern, collection.getRest());
				}
			} else if (node instanceof VariableExp ve && (ve.eContainer() instanceof PropertyTemplateItem
					|| ve.eContainer() instanceof CollectionTemplateExp)) {
				bound = ve.getReferredVariable();
			}
			addBound(pattern, bound);
		}
	}

	private static void addBound(Pattern pattern, Variable variable) {
		if (variable != null && !"_".equals(variable.getName()) && !pattern.getBindsTo().contains(variable)) {
			pattern.getBindsTo().add(variable);
		}
	}

	/**
	 * Records where a template node the expression builder never sees was written, so that a
	 * diagnostic about it can point there (#278).
	 */
	private void recordPosition(EObject node, ParserRuleContext ctx) {
		if (ctx.getStart() != null) {
			templatePositions.put(node, new SourcePosition(ctx.getStart().getLine(),
					ctx.getStart().getCharPositionInLine()));
		}
	}

	// ==================== Identifier Utility ====================

	private static String identifierText(QvtRParser.IdentifierContext ctx) {
		return QvtrExpressionBuilder.identifierText(ctx);
	}
}
