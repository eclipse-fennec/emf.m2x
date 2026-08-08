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
	private String currentPackagePath;

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
	 */
	List<Resource.Diagnostic> getDiagnostics() {
		return diagnostics;
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

	@Override
	public Object visitImportDeclaration(OclParser.ImportDeclarationContext ctx) {
		LOG.fine(() -> "Skipping import declaration: " + ctx.getText());
		return null;
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
			LOG.warning("Unresolved classifier: " + classifierName + " — skipping context");
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
				LOG.warning("Unresolved classifier: " + classifierName + " — skipping context");
				return null;
			}
		} else {
			// G-11: Unqualified: context op(...) — use current package context
			// Without a classifier qualifier, we cannot resolve — skip with warning
			LOG.warning("Unqualified operation context (no classifier) — skipping");
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
			LOG.warning("Unresolved classifier: " + classifierName + " — skipping context");
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

	private OclExpression buildExpression(OclParser.ExpressionContext ctx,
			EClassifier contextType) {
		return buildExpression(ctx, contextType, null);
	}

	private OclExpression buildExpression(OclParser.ExpressionContext ctx,
			EClassifier contextType, String selfAlias) {
		OclAstBuilder builder = new OclAstBuilder(contextType, packageRegistry);
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

		// Search all packages in the registry
		for (Object key : packageRegistry.keySet().toArray()) {
			EPackage pkg = packageRegistry.getEPackage((String) key);
			if (pkg == null) {
				continue;
			}
			if (packagePath != null && !matchesPackage(pkg, packagePath)) {
				continue;
			}
			EClassifier found = pkg.getEClassifier(classifierName);
			if (found != null) {
				return found;
			}
		}

		// If packagePath was set but nothing found, try without package constraint
		if (packagePath != null) {
			for (Object key : packageRegistry.keySet().toArray()) {
				EPackage pkg = packageRegistry.getEPackage((String) key);
				if (pkg == null) {
					continue;
				}
				EClassifier found = pkg.getEClassifier(classifierName);
				if (found != null) {
					return found;
				}
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
