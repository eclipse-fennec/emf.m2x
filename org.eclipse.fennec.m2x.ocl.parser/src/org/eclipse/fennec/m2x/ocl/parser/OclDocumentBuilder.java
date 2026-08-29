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

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.model.ocl.ConstraintKind;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.ocl.api.ParseDiagnostic;

/**
 * Visitor that transforms a Complete OCL document parse tree into a list of
 * {@link Constraint} objects.
 *
 * <p>Each constraint carries its kind (INV, DEF, PRE, POST, BODY, INIT, DERIVE),
 * the resolved context classifier/operation/property, and the parsed expression body.
 *
 * <p>Classifier resolution uses the given {@link EPackage.Registry}. Unresolved
 * classifiers cause a warning and the context declaration is skipped.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclDocumentBuilder extends OclBaseVisitor<Object> {

	private static final Logger LOG = Logger.getLogger(OclDocumentBuilder.class.getName());
	private static final OclFactory FACTORY = OclFactory.eINSTANCE;

	private final EPackage.Registry packageRegistry;
	private final List<Constraint> constraints = new ArrayList<>();
	private final List<Resource.Diagnostic> diagnostics = new ArrayList<>();

	/** Aliases from {@code import alias : path} declarations, usable in qualified names. */
	private final Map<String, String> packageAliases = new HashMap<>();

	/**
	 * The packages the document imports, by nsURI or by name. An unqualified name in the
	 * document resolves here and in the current {@code package} scope — not in every package
	 * the registry knows (OCL v2.4 §12.1, #158).
	 */
	private final List<EPackage> importedPackages = new ArrayList<>();
	private String currentPackagePath;

	/** Whether an unknown property is reported — see {@code OclParserSupport.strictPropertyResolution}. */
	private boolean strictPropertyResolution;

	void setStrictPropertyResolution(boolean strict) {
		this.strictPropertyResolution = strict;
	}

	OclDocumentBuilder(EPackage.Registry packageRegistry) {
		this.packageRegistry = packageRegistry;
	}

	/**
	 * Builds a list of constraints from the given document parse tree.
	 *
	 * @param tree the parse tree for a Complete OCL document entry
	 * @return the list of constraints
	 */
	/**
	 * Returns the diagnostics collected while building the document (#66).
	 *
	 * @return what could not be built, unmodifiable
	 */
	List<Resource.Diagnostic> getDiagnostics() {
		return List.copyOf(diagnostics);
	}

	List<Constraint> buildDocument(OclParser.CompleteOclDocumentEntryContext tree) {
		constraints.clear();
		visit(tree);
		return new ArrayList<>(constraints);
	}

	@Override
	public Object visitCompleteOclDocumentEntry(OclParser.CompleteOclDocumentEntryContext ctx) {
		return visit(ctx.completeOclDocument());
	}

	@Override
	public Object visitCompleteOclDocument(OclParser.CompleteOclDocumentContext ctx) {
		for (OclParser.ImportDeclarationContext importCtx : ctx.importDeclaration()) {
			visitImportDeclaration(importCtx);
		}
		// The names a def: adds, before any expression is built: a def defines a property of its
		// context classifier for the whole document, and an expression may navigate to one that
		// stands further down (chained defs, OCL v2.4 §12.12). Strict resolution would otherwise
		// report it as unknown (#153).
		collectDefNames(ctx, definedProperties);
		for (int i = 0; i < ctx.getChildCount(); i++) {
			var child = ctx.getChild(i);
			if (child instanceof OclParser.PackageDeclarationContext pkgCtx) {
				visitPackageDeclaration(pkgCtx);
			} else if (child instanceof OclParser.ContextDeclarationContext ctxDecl) {
				visitContextDeclaration(ctxDecl);
			}
		}
		return null;
	}

	private final List<String> definedProperties = new ArrayList<>();

	private static void collectDefNames(ParseTree node, List<String> names) {
		if (node instanceof OclParser.DefinitionConstraintContext def) {
			List<OclParser.IdentifierContext> ids = def.identifier();
			if (!ids.isEmpty()) {
				names.add(OclAstBuilder.identifierText(ids.get(ids.size() - 1)));
			}
		}
		for (int i = 0; i < node.getChildCount(); i++) {
			collectDefNames(node.getChild(i), names);
		}
	}

	/**
	 * Handles {@code import}, {@code include} and {@code library} declarations.
	 *
	 * <p>Not from the OCL specification: its Complete OCL concrete syntax has no import at
	 * all — {@code packageDeclarationCS ::= "package" pathNameCS contextDeclarationCS*
	 * "endpackage"} (§12.12.1), and the word "import" does not occur in the document. These
	 * are accepted for compatibility with Eclipse OCL, which is why what they mean here is a
	 * decision rather than a reading of the spec.
	 *
	 * <p>Importing a package is a no-op, and correctly so: type names resolve against the
	 * whole package registry the engine was given, whether or not a document says it wants
	 * them, so there is nothing an import could add.
	 *
	 * <p>An alias is remembered, so {@code import c : company} makes {@code c::Person}
	 * resolve for the rest of the document. Without that the alias resolves to nothing and
	 * the type comes out unknown, at a place that gives no hint about the cause.
	 *
	 * <p>{@code library} is reported. It names a library of additional operations, and in
	 * Fennec those are given to the engine as operation providers rather than named by the
	 * document — so there is nothing to resolve it to, and saying so beats a later
	 * "unknown operation".
	 */
	@Override
	public Object visitImportDeclaration(OclParser.ImportDeclarationContext ctx) {
		String path = ctx.pathName() != null ? ctx.pathName().getText() : "";
		if (ctx.getStart() != null && "library".equals(ctx.getStart().getText())) {
			addError("Library imports are not supported (library " + path
					+ "). Additional operations are given to the engine as operation "
					+ "providers rather than named by the document.", ctx);
		} else {
			if (ctx.identifier() != null) {
				packageAliases.put(OclAstBuilder.identifierText(ctx.identifier()), path);
			}
			EPackage imported = resolveImportedPackage(path);
			if (imported != null) {
				importedPackages.add(imported);
			} else {
				addError("Unknown package in import: " + path, ctx);
			}
		}
		return null;
	}

	/** Records a diagnostic for the document being built, at the place it was found. */
	private void addError(String message, ParserRuleContext ctx) {
		diagnostics.add(ctx == null ? new ParseDiagnostic(message,
				ParseDiagnostic.UNKNOWN_POSITION, ParseDiagnostic.UNKNOWN_POSITION)
				: new ParseDiagnostic(message, ctx.getStart().getLine(),
						ctx.getStart().getCharPositionInLine()));
	}

	@Override
	public Object visitPackageDeclaration(OclParser.PackageDeclarationContext ctx) {
		String savedPackagePath = currentPackagePath;
		currentPackagePath = pathNameText(ctx.pathName());
		for (OclParser.ContextDeclarationContext ctxDecl : ctx.contextDeclaration()) {
			visitContextDeclaration(ctxDecl);
		}
		currentPackagePath = savedPackagePath;
		return null;
	}

	@Override
	public Object visitContextDeclaration(OclParser.ContextDeclarationContext ctx) {
		if (ctx.classifierContextDeclaration() != null) {
			visitClassifierContextDeclaration(ctx.classifierContextDeclaration());
		} else if (ctx.operationContextDeclaration() != null) {
			visitOperationContextDeclaration(ctx.operationContextDeclaration());
		} else if (ctx.propertyContextDeclaration() != null) {
			visitPropertyContextDeclaration(ctx.propertyContextDeclaration());
		}
		return null;
	}

	// ==================== Classifier Context ====================

	@Override
	public Object visitClassifierContextDeclaration(
			OclParser.ClassifierContextDeclarationContext ctx) {
		String classifierName = pathNameText(ctx.pathName());
		EClassifier classifier = resolveClassifierFromPath(classifierName);
		if (classifier == null) {
			// Reported, not logged away: every invariant of this block would vanish while
			// parseDocument answers successfully, so a typo in the type name switches
			// constraints off and nobody is told (#187)
			addError("Unresolved classifier '" + classifierName + "' in context declaration; "
					+ "the constraints of this block cannot be built", ctx);
			return null;
		}

		// G-10: Self-variable alias — 'context' (alias ':')? pathName
		// If an identifier is present before ':', it's the self-alias
		String selfAlias = null;
		if (ctx.identifier() != null) {
			selfAlias = OclAstBuilder.identifierText(ctx.identifier());
		}

		for (OclParser.ClassifierContextBodyContext bodyCtx : ctx.classifierContextBody()) {
			if (bodyCtx.invariantConstraint() != null) {
				buildInvariant(bodyCtx.invariantConstraint(), classifier, selfAlias);
			} else if (bodyCtx.definitionConstraint() != null) {
				buildDefinition(bodyCtx.definitionConstraint(), classifier, selfAlias);
			}
		}
		return null;
	}

	private void buildInvariant(OclParser.InvariantConstraintContext ctx, EClassifier classifier,
			String selfAlias) {
		Constraint constraint = FACTORY.createConstraint();
		constraint.setKind(ConstraintKind.INV);
		constraint.setContextClassifier(classifier);

		// inv name(...) : expr — name is optional identifier
		if (ctx.identifier() != null) {
			constraint.setName(OclAstBuilder.identifierText(ctx.identifier()));
		}

		// The body expression is the last expression in the rule
		// Grammar: 'inv' (identifier ('(' expression ')')?)? ':' expression
		// If there's a guard expression in parens, it's expression(0), body is expression(1)
		// If no guard, body is expression(0)
		OclParser.ExpressionContext bodyExpr = ctx.expression().size() > 1
				? ctx.expression(1)
				: ctx.expression(0);
		constraint.setSpecification(buildExpression(bodyExpr, classifier, selfAlias));
		constraints.add(constraint);
	}

	private void buildDefinition(OclParser.DefinitionConstraintContext ctx,
			EClassifier classifier, String selfAlias) {
		Constraint constraint = FACTORY.createConstraint();
		constraint.setKind(ConstraintKind.DEF);
		constraint.setContextClassifier(classifier);

		// static def: ... — check for 'static' keyword (OCL v2.4 §12.12.6)
		if (ctx.getChild(0) != null && "static".equals(ctx.getChild(0).getText())) {
			constraint.setIsStatic(true);
		}

		// def label? : featureName : type = expr        (attribute def)
		// def label? : featureName ( params ) : type = expr  (operation def)
		// The feature name is always the LAST identifier in the list.
		// If there are 2 identifiers, the first is the optional label (ignored).
		List<OclParser.IdentifierContext> ids = ctx.identifier();
		if (!ids.isEmpty()) {
			constraint.setName(OclAstBuilder.identifierText(ids.get(ids.size() - 1)));
		}

		// Operation def: parameterList presence indicates operation definition.
		// Store parameter names in a synthetic EOperation on contextOperation.
		OclParser.ParameterListContext paramList = ctx.parameterList();
		if (paramList != null || isOperationDef(ctx)) {
			EOperation syntheticOp = EcoreFactory.eINSTANCE.createEOperation();
			syntheticOp.setName(constraint.getName());
			if (paramList != null) {
				for (OclParser.ParameterContext param : paramList.parameter()) {
					EParameter eParam = EcoreFactory.eINSTANCE.createEParameter();
					eParam.setName(OclAstBuilder.identifierText(param.identifier()));
					syntheticOp.getEParameters().add(eParam);
				}
			}
			constraint.setContextOperation(syntheticOp);
		}

		constraint.setSpecification(buildExpression(ctx.expression(), classifier, selfAlias));
		constraints.add(constraint);
	}

	/**
	 * Detects operation def syntax by scanning for '(' token in the parse tree children.
	 * Handles the case of zero-parameter operation defs: {@code def: foo() : Type = expr}
	 */
	private boolean isOperationDef(OclParser.DefinitionConstraintContext ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if ("(".equals(ctx.getChild(i).getText())) {
				return true;
			}
		}
		return false;
	}

	// ==================== Operation Context ====================

	@Override
	public Object visitOperationContextDeclaration(
			OclParser.OperationContextDeclarationContext ctx) {
		// G-11/G-12: 'context' (pathName '::')? identifier '(' parameterList? ')' (':' typeExpression)?
		EClassifier classifier;
		String opName;

		if (ctx.pathName() != null) {
			// Qualified: context Type::op(...)
			String classifierName = pathNameText(ctx.pathName());
			classifier = resolveClassifierFromPath(classifierName);
			if (classifier == null) {
				addError("Unresolved classifier '" + classifierName
						+ "' in operation context declaration; the constraints of this block "
						+ "cannot be built", ctx);
				return null;
			}
		} else {
			// G-11: an unqualified `context op(...)` has no classifier to attach the operation
			// to. Reported rather than skipped: the body or the pre/postcondition the document
			// defines would simply be absent, and the caller would have no way of knowing.
			addError("Operation context without a classifier is not supported; "
					+ "write 'context Type::op(...)'", ctx);
			return null;
		}

		opName = OclAstBuilder.identifierText(ctx.identifier());
		int paramCount = ctx.parameterList() != null ? ctx.parameterList().parameter().size() : 0;
		EOperation operation = resolveOperation(classifier, opName, paramCount);

		for (OclParser.OperationContextBodyContext bodyCtx : ctx.operationContextBody()) {
			if (bodyCtx instanceof OclParser.PreConditionContext preCtx) {
				buildPreCondition(preCtx, classifier, operation);
			} else if (bodyCtx instanceof OclParser.PostConditionContext postCtx) {
				buildPostCondition(postCtx, classifier, operation);
			} else if (bodyCtx instanceof OclParser.BodyExpressionContext bodyExprCtx) {
				buildBodyExpression(bodyExprCtx, classifier, operation);
			}
		}
		return null;
	}

	private void buildPreCondition(OclParser.PreConditionContext ctx, EClassifier classifier,
			EOperation operation) {
		Constraint constraint = FACTORY.createConstraint();
		constraint.setKind(ConstraintKind.PRE);
		constraint.setContextClassifier(classifier);
		constraint.setContextOperation(operation);
		if (ctx.identifier() != null) {
			constraint.setName(OclAstBuilder.identifierText(ctx.identifier()));
		}
		constraint.setSpecification(buildExpression(ctx.expression(), classifier));
		constraints.add(constraint);
	}

	private void buildPostCondition(OclParser.PostConditionContext ctx, EClassifier classifier,
			EOperation operation) {
		Constraint constraint = FACTORY.createConstraint();
		constraint.setKind(ConstraintKind.POST);
		constraint.setContextClassifier(classifier);
		constraint.setContextOperation(operation);
		if (ctx.identifier() != null) {
			constraint.setName(OclAstBuilder.identifierText(ctx.identifier()));
		}
		constraint.setSpecification(buildExpression(ctx.expression(), classifier));
		constraints.add(constraint);
	}

	private void buildBodyExpression(OclParser.BodyExpressionContext ctx, EClassifier classifier,
			EOperation operation) {
		Constraint constraint = FACTORY.createConstraint();
		constraint.setKind(ConstraintKind.BODY);
		constraint.setContextClassifier(classifier);
		constraint.setContextOperation(operation);
		if (ctx.identifier() != null) {
			constraint.setName(OclAstBuilder.identifierText(ctx.identifier()));
		}
		constraint.setSpecification(buildExpression(ctx.expression(), classifier));
		constraints.add(constraint);
	}

	// ==================== Property Context ====================

	@Override
	public Object visitPropertyContextDeclaration(
			OclParser.PropertyContextDeclarationContext ctx) {
		String classifierName = pathNameText(ctx.pathName());
		EClassifier classifier = resolveClassifierFromPath(classifierName);
		if (classifier == null) {
			// Reported, not logged away: every invariant of this block would vanish while
			// parseDocument answers successfully, so a typo in the type name switches
			// constraints off and nobody is told (#187)
			addError("Unresolved classifier '" + classifierName + "' in context declaration; "
					+ "the constraints of this block cannot be built", ctx);
			return null;
		}

		String propName = OclAstBuilder.identifierText(ctx.identifier());
		EStructuralFeature property = resolveProperty(classifier, propName);

		for (OclParser.PropertyContextBodyContext bodyCtx : ctx.propertyContextBody()) {
			if (bodyCtx instanceof OclParser.InitExpressionContext initCtx) {
				buildInitExpression(initCtx, classifier, property);
			} else if (bodyCtx instanceof OclParser.DeriveExpressionContext deriveCtx) {
				buildDeriveExpression(deriveCtx, classifier, property);
			}
		}
		return null;
	}

	private void buildInitExpression(OclParser.InitExpressionContext ctx, EClassifier classifier,
			EStructuralFeature property) {
		Constraint constraint = FACTORY.createConstraint();
		constraint.setKind(ConstraintKind.INIT);
		constraint.setContextClassifier(classifier);
		constraint.setContextProperty(property);
		if (ctx.identifier() != null) {
			constraint.setName(OclAstBuilder.identifierText(ctx.identifier()));
		}
		constraint.setSpecification(buildExpression(ctx.expression(), classifier));
		constraints.add(constraint);
	}

	private void buildDeriveExpression(OclParser.DeriveExpressionContext ctx,
			EClassifier classifier, EStructuralFeature property) {
		Constraint constraint = FACTORY.createConstraint();
		constraint.setKind(ConstraintKind.DERIVE);
		constraint.setContextClassifier(classifier);
		constraint.setContextProperty(property);
		if (ctx.identifier() != null) {
			constraint.setName(OclAstBuilder.identifierText(ctx.identifier()));
		}
		constraint.setSpecification(buildExpression(ctx.expression(), classifier));
		constraints.add(constraint);
	}

	// ==================== Expression Delegation ====================

	/**
	 * The package an {@code import} names, by nsURI or by (qualified) package name. Looking a
	 * declared name up in the registry is what a declaration is for; what this class no longer
	 * does is look a <em>type</em> name up in every package without a declaration.
	 */
	private EPackage resolveImportedPackage(String path) {
		if (packageRegistry == null) {
			return null;
		}
		String unquoted = path.length() >= 2 && (path.startsWith("'") || path.startsWith("\""))
				? path.substring(1, path.length() - 1) : path;
		EPackage byUri = packageRegistry.getEPackage(unquoted);
		if (byUri != null) {
			return byUri;
		}
		for (String key : registryKeys()) {
			EPackage pkg = packageRegistry.getEPackage(key);
			if (pkg != null && matchesPackage(pkg, unquoted)) {
				return pkg;
			}
		}
		// A classifier path (import company::Company) imports the classifier's package
		int separator = unquoted.lastIndexOf("::");
		if (separator > 0) {
			String packagePath = unquoted.substring(0, separator);
			String classifierName = unquoted.substring(separator + 2);
			for (String key : registryKeys()) {
				EPackage pkg = packageRegistry.getEPackage(key);
				if (pkg != null && matchesPackage(pkg, packagePath)
						&& pkg.getEClassifier(classifierName) != null) {
					return pkg;
				}
			}
		}
		return null;
	}

	/**
	 * The registry's keys, in a fixed order.
	 *
	 * <p>Searching the registry by package <em>name</em> is ambiguous — an nsURI identifies a
	 * package, a name does not — so a package another bundle registered under the same name can
	 * answer for the one a document meant. The search is still needed where a name has nowhere
	 * else to go, but the registry is a hash map: without an order, which package answered
	 * could differ between two runs of the same document (#186).
	 *
	 * @return the nsURIs, sorted
	 */
	private List<String> registryKeys() {
		List<String> keys = new ArrayList<>();
		for (Object key : packageRegistry.keySet().toArray()) {
			keys.add((String) key);
		}
		Collections.sort(keys);
		return keys;
	}

	private OclExpression buildExpression(OclParser.ExpressionContext ctx,
			EClassifier contextType) {
		return buildExpression(ctx, contextType, null);
	}

	private OclExpression buildExpression(OclParser.ExpressionContext ctx,
			EClassifier contextType, String selfAlias) {
		OclAstBuilder builder = new OclAstBuilder(contextType, packageRegistry, strictPropertyResolution);
		builder.support.declareDefinedProperties(definedProperties);
		builder.support.registerPackageAliases(packageAliases);
		importedPackages.forEach(builder.support::importPackage);
		if (selfAlias != null) {
			builder.registerSelfAlias(selfAlias);
		}
		OclExpression expression = (OclExpression) builder.visit(ctx);
		diagnostics.addAll(builder.support.getDiagnostics());
		return expression;
	}

	// ==================== Classifier Resolution ====================

	/**
	 * Resolves a classifier from a qualified path name against the package registry.
	 * Uses {@code currentPackagePath} from {@code package...endpackage} as scope.
	 */
	private EClassifier resolveClassifierFromPath(String qualifiedName) {
		// Split qualified name: last segment = classifier name, preceding = package path
		String[] segments = qualifiedName.split("::");
		String classifierName = segments[segments.length - 1];

		// Build the package path prefix (if any segments before the classifier name)
		String packagePath = null;
		if (segments.length > 1) {
			packagePath = String.join("::", java.util.Arrays.copyOf(segments, segments.length - 1));
		} else if (currentPackagePath != null) {
			packagePath = currentPackagePath;
		}

		if (packagePath != null) {
			// Qualified, or inside a package … endpackage: the named package, and only that one.
			// What the document imported comes first — it named that package on purpose
			for (EPackage imported : importedPackages) {
				if (matchesPackage(imported, packagePath)) {
					EClassifier found = imported.getEClassifier(classifierName);
					if (found != null) {
						return found;
					}
				}
			}
			EPackage byUri = packageRegistry.getEPackage(packagePath);
			if (byUri != null) {
				EClassifier found = byUri.getEClassifier(classifierName);
				if (found != null) {
					return found;
				}
			}
			for (String key : registryKeys()) {
				EPackage pkg = packageRegistry.getEPackage(key);
				if (pkg != null && matchesPackage(pkg, packagePath)) {
					EClassifier found = pkg.getEClassifier(classifierName);
					if (found != null) {
						return found;
					}
				}
			}
			return null;
		}
		// Unqualified and outside any package: what the document imports (OCL v2.4 §12.1).
		// Until #158 every package in the registry was searched, so which type a bare name meant
		// depended on what else was registered in the JVM.
		for (EPackage pkg : importedPackages) {
			EClassifier found = pkg.getEClassifier(classifierName);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	private boolean matchesPackage(EPackage pkg, String packagePath) {
		// Match by package name or nsURI
		return packagePath.equals(pkg.getName())
				|| packagePath.equals(pkg.getNsURI())
				|| packagePath.equals(pkg.getNsPrefix());
	}

	// ==================== Operation/Property Resolution ====================

	private EOperation resolveOperation(EClassifier classifier, String opName, int paramCount) {
		if (classifier instanceof EClass eClass) {
			for (EOperation op : eClass.getEAllOperations()) {
				if (opName.equals(op.getName())
						&& op.getEParameters().size() == paramCount) {
					return op;
				}
			}
		}
		return null;
	}

	private EStructuralFeature resolveProperty(EClassifier classifier, String propName) {
		if (classifier instanceof EClass eClass) {
			return eClass.getEStructuralFeature(propName);
		}
		return null;
	}

	// ==================== Utility ====================

	private String pathNameText(OclParser.PathNameContext ctx) {
		List<String> segments = new ArrayList<>();
		for (var id : ctx.identifier()) {
			segments.add(OclAstBuilder.identifierText(id));
		}
		return String.join("::", segments);
	}
}
