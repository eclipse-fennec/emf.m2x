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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.ocl.ClassifierType;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtbase.Function;
import org.eclipse.fennec.m2x.model.qvtbase.FunctionParameter;
import org.eclipse.fennec.m2x.model.qvtbase.Pattern;
import org.eclipse.fennec.m2x.model.qvtbase.Predicate;
import org.eclipse.fennec.m2x.model.qvtbase.QvtbaseFactory;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern;
import org.eclipse.fennec.m2x.model.qvtrelation.Key;
import org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationFactory;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationCallExp;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomainAssignment;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem;
import org.eclipse.fennec.m2x.model.qvttemplate.QvttemplateFactory;
import org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp;

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

	/** Current transformation being built. */
	private RelationalTransformation currentTransformation;
	/** Map of typed model name → TypedModel object for domain resolution. */
	private final Map<String, TypedModel> typedModelMap = new HashMap<>();
	/** Map of relation name → Relation object for RelationCallExp resolution. */
	private final Map<String, Relation> relationMap = new HashMap<>();
	/** Current relation being processed (for variable scope). */
	private Relation currentRelation;

	QvtrUnitBuilder(EPackage.Registry packageRegistry) {
		this.packageRegistry = packageRegistry;
	}

	// ==================== Entry Point ====================

	@Override
	public RelationalTransformation visitCompilationUnitEntry(
			QvtRParser.CompilationUnitEntryContext ctx) {
		return visitCompilationUnit(ctx.compilationUnit());
	}

	@Override
	public RelationalTransformation visitCompilationUnit(QvtRParser.CompilationUnitContext ctx) {
		// QVT-R supports multiple transformations, but we return the first one
		// (mirrors QvtoParserSupport pattern)
		if (ctx.transformationDef().isEmpty()) {
			RelationalTransformation empty = REL.createRelationalTransformation();
			empty.setName("_unnamed");
			return empty;
		}
		return visitTransformationDef(ctx.transformationDef(0));
	}

	// ==================== Transformation ====================

	@Override
	public RelationalTransformation visitTransformationDef(
			QvtRParser.TransformationDefContext ctx) {
		currentTransformation = REL.createRelationalTransformation();
		currentTransformation.setName(identifierText(ctx.identifier(0)));

		// Create expression builder with EObject as context type
		expressionBuilder = new QvtrExpressionBuilder(
				EcorePackage.Literals.EOBJECT, packageRegistry);

		// Process model declarations (typed models)
		for (QvtRParser.ModelDeclContext modelCtx : ctx.modelDecl()) {
			TypedModel tm = visitModelDecl(modelCtx);
			currentTransformation.getModelParameter().add(tm);
			typedModelMap.put(tm.getName(), tm);
		}

		// Handle 'extends' clause
		if (ctx.EXTENDS() != null) {
			// Store as EAnnotation for link-time resolution
			// (actual extends resolution requires multi-file context)
			int extendsIdx = ctx.identifier().size() > 1 ? 1 : 0;
			if (extendsIdx == 1) {
				String extendsName = identifierText(ctx.identifier(1));
				// Deferred: setExtends needs the referenced transformation object
				// For now we store the name as annotation for later resolution
				org.eclipse.emf.ecore.EAnnotation ann =
						org.eclipse.emf.ecore.EcoreFactory.eINSTANCE.createEAnnotation();
				ann.setSource("qvtr.extends");
				ann.getDetails().put("name", extendsName);
				currentTransformation.getEAnnotations().add(ann);
			}
		}

		// Pre-pass: collect all relation names for RelationCallExp resolution
		for (QvtRParser.RelationContext relCtx : ctx.relation()) {
			String relName = getRelationName(relCtx);
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
			EClass queryHolder = org.eclipse.emf.ecore.EcoreFactory.eINSTANCE.createEClass();
			queryHolder.setName("_queries");
			currentTransformation.getEClassifiers().add(queryHolder);
			for (QvtRParser.QueryDefContext queryCtx : ctx.queryDef()) {
				Function function = visitQueryDef(queryCtx);
				queryHolder.getEOperations().add(function);
			}
		}

		return currentTransformation;
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

			EPackage pkg = resolvePackage(metamodelName);
			if (pkg != null) {
				tm.getUsedPackage().add(pkg);
			}
		}
		return tm;
	}

	// ==================== Key Declaration ====================

	@Override
	public Key visitKeyDecl(QvtRParser.KeyDeclContext ctx) {
		Key key = REL.createKey();

		// Resolve the class
		List<String> classPath = QvtrExpressionBuilder.pathNameSegments(ctx.pathName());
		EClassifier cls = resolveClassifier(classPath);
		if (cls instanceof EClass eClass) {
			key.setIdentifies(eClass);

			// Process key properties
			for (QvtRParser.KeyPropertyContext propCtx : ctx.keyProperty()) {
				if (propCtx.OPPOSITE() != null) {
					// opposite(ClassName::propName) → oppositePart
					List<String> oppClassPath =
							QvtrExpressionBuilder.pathNameSegments(propCtx.pathName());
					String oppPropName = identifierText(propCtx.identifier());
					EClassifier oppCls = resolveClassifier(oppClassPath);
					if (oppCls instanceof EClass oppEClass) {
						EStructuralFeature feat = oppEClass.getEStructuralFeature(oppPropName);
						if (feat != null) {
							key.getOppositePart().add(feat);
						}
					}
				} else {
					// Simple property name → part
					String propName = identifierText(propCtx.identifier());
					EStructuralFeature feat = eClass.getEStructuralFeature(propName);
					if (feat != null) {
						key.getPart().add(feat);
					}
				}
			}
		}
		return key;
	}

	// ==================== Relation ====================

	@Override
	public Relation visitRelation(QvtRParser.RelationContext ctx) {
		String name = identifierText(ctx.identifier(0));

		// Reuse pre-created relation from pre-pass
		Relation relation = relationMap.get(name);
		if (relation == null) {
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

		// Handle overrides
		if (ctx.OVERRIDES() != null && ctx.identifier().size() > 1) {
			String overridesName = identifierText(ctx.identifier(1));
			// Deferred: setOverrides needs the referenced relation object
			org.eclipse.emf.ecore.EAnnotation ann =
					org.eclipse.emf.ecore.EcoreFactory.eINSTANCE.createEAnnotation();
			ann.setSource("qvtr.overrides");
			ann.getDetails().put("name", overridesName);
			relation.getEAnnotations().add(ann);
		}

		// Process variable declarations
		for (QvtRParser.VarDeclarationContext varCtx : ctx.varDeclaration()) {
			List<Variable> vars = visitVarDeclaration(varCtx);
			relation.getVariable().addAll(vars);
		}

		// Process domains
		for (QvtRParser.DomainContext domCtx : ctx.domain()) {
			RelationDomain domain = visitDomain(domCtx);
			relation.getDomain().add(domain);
		}
		for (QvtRParser.PrimitiveTypeDomainContext primCtx : ctx.primitiveTypeDomain()) {
			RelationDomain domain = visitPrimitiveTypeDomain(primCtx);
			relation.getDomain().add(domain);
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
		OclType type = expressionBuilder.resolveTypeExpression(ctx.typeExpression());

		OclExpression initExp = null;
		if (ctx.expression() != null) {
			initExp = expressionBuilder.buildExpression(ctx.expression());
		}

		List<Variable> vars = new ArrayList<>();
		for (QvtRParser.IdentifierContext idCtx : ctx.identifier()) {
			Variable var = OCL.createVariable();
			var.setName(identifierText(idCtx));
			if (type != null) {
				var.setType(type);
			}
			if (initExp != null) {
				var.setOwnedInit(initExp);
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
		}

		// Process templates → DomainPattern(s)
		for (QvtRParser.TemplateContext tmplCtx : ctx.template()) {
			TemplateExp templateExp = visitTemplate(tmplCtx);
			DomainPattern pattern = REL.createDomainPattern();
			pattern.setTemplateExpression(templateExp);

			// Root variable comes from the template's bindsTo
			if (templateExp.getBindsTo() != null) {
				domain.getRootVariable().add(templateExp.getBindsTo());
				// Also register in relation's variable list
				if (currentRelation != null) {
					currentRelation.getVariable().add(templateExp.getBindsTo());
				}
			}
			domain.getPattern().add(pattern);
		}

		// implementedby clause
		// (deferred — needs operation resolution context from engine)

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

		OclType type = expressionBuilder.resolveTypeExpression(ctx.typeExpression());

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
			result.setWhere(expressionBuilder.buildExpression(ctx.expression()));
		}

		return result;
	}

	@Override
	public ObjectTemplateExp visitObjectTemplate(QvtRParser.ObjectTemplateContext ctx) {
		ObjectTemplateExp ote = TMPL.createObjectTemplateExp();

		// Resolve the referred class from pathName
		List<String> classPath = QvtrExpressionBuilder.pathNameSegments(ctx.pathName());
		EClassifier cls = resolveClassifier(classPath);
		if (cls instanceof EClass eClass) {
			ote.setReferredClass(eClass);
		}

		// Optional variable binding
		if (ctx.identifier() != null) {
			Variable bindVar = OCL.createVariable();
			bindVar.setName(identifierText(ctx.identifier()));
			if (cls != null) {
				bindVar.setType(createClassifierType(cls));
			}
			ote.setBindsTo(bindVar);
		}

		// Property template items
		if (ctx.propertyTemplateList() != null) {
			for (QvtRParser.PropertyTemplateContext propCtx :
					ctx.propertyTemplateList().propertyTemplate()) {
				PropertyTemplateItem item = visitPropertyTemplate(propCtx);
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
			if (cls instanceof EClass eClass) {
				EStructuralFeature feat = eClass.getEStructuralFeature(propName);
				if (feat != null) {
					item.setReferredProperty(feat);
				}
			}
		} else {
			// identifier = expression
			String propName = identifierText(ctx.identifier());
			// Resolve property from the enclosing object template's referred class
			// (deferred until we have full type context; store name as EAnnotation)
			item.setIsOpposite(false);
			// We'll try to resolve later; for now create a placeholder EAttribute
			org.eclipse.emf.ecore.EAttribute placeholder =
					org.eclipse.emf.ecore.EcoreFactory.eINSTANCE.createEAttribute();
			placeholder.setName(propName);
			item.setReferredProperty(placeholder);
		}

		item.setValue(expressionBuilder.buildExpression(ctx.expression()));

		return item;
	}

	@Override
	public CollectionTemplateExp visitCollectionTemplate(
			QvtRParser.CollectionTemplateContext ctx) {
		CollectionTemplateExp cte = TMPL.createCollectionTemplateExp();

		// Collection kind and element type
		String collKind = ctx.collectionKind().getText();
		OclType elementType = expressionBuilder.resolveTypeExpression(ctx.typeExpression());
		OclType collType = expressionBuilder.support.buildCollectionType(collKind, elementType);
		cte.setReferredCollectionType(getClassifierForType(collType));

		// Optional variable binding
		if (ctx.identifier() != null) {
			Variable bindVar = OCL.createVariable();
			bindVar.setName(identifierText(ctx.identifier()));
			if (collType != null) {
				bindVar.setType(collType);
			}
			cte.setBindsTo(bindVar);
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
					// Variable reference
					VariableExp varExp = OCL.createVariableExp();
					Variable refVar = OCL.createVariable();
					refVar.setName(identifierText(itemCtx.identifier()));
					varExp.setReferredVariable(refVar);
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
				Variable restVar = OCL.createVariable();
				restVar.setName(identifierText(selCtx.identifier()));
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
		for (QvtRParser.ExpressionContext exprCtx : ctx.expression()) {
			Predicate predicate = BASE.createPredicate();
			OclExpression condExpr = buildExpressionOrRelationCall(exprCtx);
			predicate.setConditionExpression(condExpr);
			pattern.getPredicate().add(predicate);
		}
		return pattern;
	}

	@Override
	public Pattern visitWhereClause(QvtRParser.WhereClauseContext ctx) {
		Pattern pattern = BASE.createPattern();
		for (QvtRParser.ExpressionContext exprCtx : ctx.expression()) {
			Predicate predicate = BASE.createPredicate();
			OclExpression condExpr = buildExpressionOrRelationCall(exprCtx);
			predicate.setConditionExpression(condExpr);
			pattern.getPredicate().add(predicate);
		}
		return pattern;
	}

	/**
	 * Builds an expression from a when/where clause context.
	 * Detects RelationCallExp: an identifier followed by '(' args ')' where the
	 * identifier matches a known relation name.
	 */
	private OclExpression buildExpressionOrRelationCall(QvtRParser.ExpressionContext ctx) {
		OclExpression expr = expressionBuilder.buildExpression(ctx);

		// Post-process: if the expression is an OperationCallExp whose name matches
		// a known relation, convert it to a RelationCallExp
		if (expr instanceof org.eclipse.fennec.m2x.model.ocl.OperationCallExp opCall) {
			String opName = opCall.getName();
			if (opName != null && relationMap.containsKey(opName)) {
				RelationCallExp relCall = REL.createRelationCallExp();
				relCall.setReferredRelation(relationMap.get(opName));
				relCall.getArgument().addAll(opCall.getOwnedArguments());
				return relCall;
			}
		}

		return expr;
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
			// Create placeholder variable
			Variable placeholder = OCL.createVariable();
			placeholder.setName(varName);
			assignment.setVariable(placeholder);
		}

		assignment.setValueExp(expressionBuilder.buildExpression(ctx.expression()));
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
				OclType paramType =
						expressionBuilder.resolveTypeExpression(paramCtx.typeExpression());
				if (paramType != null) {
					param.setEType(getClassifierForType(paramType));
				}
				function.getEParameters().add(param);
			}
		}

		// Return type
		OclType returnType = expressionBuilder.resolveTypeExpression(ctx.typeExpression());
		if (returnType != null) {
			function.setEType(getClassifierForType(returnType));
		}

		// Query body expression (if not abstract/blackbox)
		if (ctx.expression() != null) {
			function.setQueryExpression(expressionBuilder.buildExpression(ctx.expression()));
		}

		return function;
	}

	// ==================== Resolution Helpers ====================

	/**
	 * Resolves an EPackage by name from the package registry.
	 */
	private EPackage resolvePackage(String name) {
		// Try direct URI lookup first
		Object pkg = packageRegistry.get(name);
		if (pkg instanceof EPackage ePkg) {
			return ePkg;
		}
		// Try finding by name in all registered packages
		for (Object value : packageRegistry.values()) {
			if (value instanceof EPackage ePkg && name.equals(ePkg.getName())) {
				return ePkg;
			}
		}
		return null;
	}

	/**
	 * Resolves an EClassifier from a path name using all typed models' packages.
	 */
	private EClassifier resolveClassifier(List<String> path) {
		String simpleName = path.get(path.size() - 1);

		// Search in all typed models' packages
		for (TypedModel tm : typedModelMap.values()) {
			for (EPackage pkg : tm.getUsedPackage()) {
				EClassifier cls = findClassifierInPackage(pkg, path);
				if (cls != null) {
					return cls;
				}
			}
		}

		// Fallback: search all registered packages
		for (Object value : packageRegistry.values()) {
			if (value instanceof EPackage pkg) {
				EClassifier cls = pkg.getEClassifier(simpleName);
				if (cls != null) {
					return cls;
				}
			}
		}

		return null;
	}

	/**
	 * Finds a classifier in a package hierarchy following the path segments.
	 */
	private EClassifier findClassifierInPackage(EPackage pkg, List<String> path) {
		if (path.size() == 1) {
			return pkg.getEClassifier(path.get(0));
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
	 * Creates a ClassifierType wrapping an EClassifier.
	 */
	private ClassifierType createClassifierType(EClassifier cls) {
		ClassifierType ct = OCL.createClassifierType();
		ct.setReferredClassifier(cls);
		return ct;
	}

	/**
	 * Extracts an EClassifier from an OclType for use in EMF structural features.
	 */
	private EClassifier getClassifierForType(OclType type) {
		if (type instanceof ClassifierType ct) {
			return ct.getReferredClassifier();
		}
		// For collection/map/tuple types, return EObject as fallback
		return EcorePackage.Literals.EOBJECT;
	}

	// ==================== Identifier Utility ====================

	private static String identifierText(QvtRParser.IdentifierContext ctx) {
		return QvtrExpressionBuilder.identifierText(ctx);
	}
}
