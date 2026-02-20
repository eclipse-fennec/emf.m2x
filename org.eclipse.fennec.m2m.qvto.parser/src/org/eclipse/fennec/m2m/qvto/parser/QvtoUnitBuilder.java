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
package org.eclipse.fennec.m2m.qvto.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2m.model.imperativeocl.Typedef;
import org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclFactory;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.model.ocl.OclFactory;
import org.eclipse.fennec.m2m.model.ocl.OclType;
import org.eclipse.fennec.m2m.model.ocl.Variable;
import org.eclipse.fennec.m2m.model.qvtoperational.Constructor;
import org.eclipse.fennec.m2m.model.qvtoperational.ContextualProperty;
import org.eclipse.fennec.m2m.model.qvtoperational.DirectionKind;
import org.eclipse.fennec.m2m.model.qvtoperational.EntryOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.Helper;
import org.eclipse.fennec.m2m.model.qvtoperational.ImportKind;
import org.eclipse.fennec.m2m.model.qvtoperational.Library;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingParameter;
import org.eclipse.fennec.m2m.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2m.model.qvtoperational.ModelType;
import org.eclipse.fennec.m2m.model.qvtoperational.Module;
import org.eclipse.fennec.m2m.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationBody;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2m.model.qvtoperational.VarParameter;

/**
 * Visitor that transforms module-level ANTLR4 parse tree nodes into QVT-O EMF AST nodes.
 *
 * <p>Handles transformation/library definitions, mapping operations, helpers, queries,
 * constructors, entry points, model types, imports, properties, intermediate classes,
 * tags, and typedefs.
 *
 * <p>Delegates expression-level subtrees to a {@link QvtoExpressionBuilder} instance.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoUnitBuilder extends QvtOBaseVisitor<Object> {

	private static final OclFactory OCL = OclFactory.eINSTANCE;
	private static final ImperativeOclFactory IMP = ImperativeOclFactory.eINSTANCE;
	private static final QvtOperationalFactory QVTO = QvtOperationalFactory.eINSTANCE;

	private final EPackage.Registry packageRegistry;
	private QvtoEnvironment environment;
	private QvtoExpressionBuilder expressionBuilder;

	QvtoUnitBuilder(EPackage.Registry packageRegistry) {
		this.packageRegistry = packageRegistry;
		this.environment = QvtoEnvironment.root();
		this.expressionBuilder = new QvtoExpressionBuilder(environment, packageRegistry);
	}

	// ==================== Entry Point ====================

	@Override
	public OperationalTransformation visitCompilationUnitEntry(
			QvtOParser.CompilationUnitEntryContext ctx) {
		return visitCompilationUnit(ctx.compilationUnit());
	}

	@Override
	public OperationalTransformation visitCompilationUnit(QvtOParser.CompilationUnitContext ctx) {
		// Find the transformation or library definition
		OperationalTransformation transformation = null;

		for (QvtOParser.UnitElementContext elemCtx : ctx.unitElement()) {
			if (elemCtx.transformationDef() != null) {
				transformation = visitTransformationDef(elemCtx.transformationDef());
			} else if (elemCtx.libraryDef() != null) {
				Library lib = visitLibraryDef(elemCtx.libraryDef());
				// Wrap library as transformation for uniform return type
				if (transformation == null) {
					transformation = QVTO.createOperationalTransformation();
					transformation.setName(lib.getName());
				}
			} else if (elemCtx.modeltypeDecl() != null) {
				ModelType modelType = visitModeltypeDecl(elemCtx.modeltypeDecl());
				if (transformation != null) {
					transformation.getUsedModelType().add(modelType);
				}
			} else if (elemCtx.qvtoImportDecl() != null) {
				// Import declarations are processed after transformation is created
			}
		}

		if (transformation == null) {
			transformation = QVTO.createOperationalTransformation();
			transformation.setName("_unnamed");
		}

		// Second pass: imports and top-level module elements
		for (QvtOParser.UnitElementContext elemCtx : ctx.unitElement()) {
			if (elemCtx.qvtoImportDecl() != null) {
				ModuleImport imp = visitQvtoImportDecl(elemCtx.qvtoImportDecl());
				transformation.getModuleImport().add(imp);
			} else if (elemCtx.moduleElement() != null) {
				processModuleElement(elemCtx.moduleElement(), transformation);
			}
		}

		return transformation;
	}

	// ==================== Transformation + Library ====================

	@Override
	public OperationalTransformation visitTransformationDef(
			QvtOParser.TransformationDefContext ctx) {
		OperationalTransformation transformation = QVTO.createOperationalTransformation();

		// Qualifiers
		for (QvtOParser.QualifierContext qCtx : ctx.qualifier()) {
			applyQualifier(transformation, qCtx.getText());
		}

		// Name
		transformation.setName(qualifiedNameText(ctx.qualifiedName()));

		// Model parameters
		if (ctx.modelParamList() != null) {
			for (QvtOParser.ModelParamContext paramCtx : ctx.modelParamList().modelParam()) {
				ModelParameter modelParam = buildModelParameter(paramCtx);
				transformation.getModelParameter().add(modelParam);
			}
		}

		// Module usages (access/extends)
		for (QvtOParser.ModuleUsageContext usageCtx : ctx.moduleUsage()) {
			processModuleUsage(transformation, usageCtx);
		}

		// Set up environment with model parameters as variables
		QvtoEnvironment savedEnv = this.environment;
		for (ModelParameter mp : transformation.getModelParameter()) {
			Variable var = OCL.createVariable();
			var.setName(mp.getName());
			this.environment = this.environment.nested(var);
		}
		updateExpressionBuilder();

		// Module elements
		for (QvtOParser.ModuleElementContext elemCtx : ctx.moduleElement()) {
			processModuleElement(elemCtx, transformation);
		}

		this.environment = savedEnv;
		updateExpressionBuilder();
		return transformation;
	}

	@Override
	public Library visitLibraryDef(QvtOParser.LibraryDefContext ctx) {
		Library library = QVTO.createLibrary();
		library.setName(qualifiedNameText(ctx.qualifiedName()));

		// Module usages
		for (QvtOParser.ModuleUsageContext usageCtx : ctx.moduleUsage()) {
			processModuleUsage(library, usageCtx);
		}

		// Module elements
		for (QvtOParser.ModuleElementContext elemCtx : ctx.moduleElement()) {
			processModuleElement(elemCtx, library);
		}

		return library;
	}

	// ==================== Import + Modeltype ====================

	@Override
	public ModuleImport visitQvtoImportDecl(QvtOParser.QvtoImportDeclContext ctx) {
		ModuleImport imp = QVTO.createModuleImport();
		imp.setKind(ImportKind.ACCESS);
		// The imported module name is stored; actual resolution happens at link time
		return imp;
	}

	@Override
	public ModelType visitModeltypeDecl(QvtOParser.ModeltypeDeclContext ctx) {
		ModelType modelType = QVTO.createModelType();
		modelType.setName(QvtoExpressionBuilder.qvtoIdentifierText(ctx.qvtoIdentifier()));

		// Conformance kind from STRING_LITERAL (e.g., "strict", "effective")
		if (ctx.STRING_LITERAL() != null) {
			String text = ctx.STRING_LITERAL().getText();
			modelType.setConformanceKind(text.substring(1, text.length() - 1));
		}

		// Package references: resolve metamodel packages
		for (QvtOParser.PackageRefContext refCtx : ctx.packageRefList().packageRef()) {
			EPackage resolvedPkg = resolvePackageRef(refCtx);
			if (resolvedPkg != null) {
				modelType.getMetamodel().add(resolvedPkg);
			}
		}

		return modelType;
	}

	// ==================== Module Elements ====================

	private void processModuleElement(QvtOParser.ModuleElementContext ctx, Module module) {
		// Module extends EPackage, so operations are stored on an internal EClass
		// named after the module. This is the standard pattern for QVT-O.
		EClass moduleClass = getOrCreateModuleClass(module);

		if (ctx.mappingDef() != null) {
			MappingOperation mapping = visitMappingDef(ctx.mappingDef());
			moduleClass.getEOperations().add(mapping);
		} else if (ctx.helperDef() != null) {
			Helper helper = visitHelperDef(ctx.helperDef());
			moduleClass.getEOperations().add(helper);
		} else if (ctx.queryDef() != null) {
			Helper query = visitQueryDef(ctx.queryDef());
			moduleClass.getEOperations().add(query);
		} else if (ctx.constructorDef() != null) {
			Constructor constructor = visitConstructorDef(ctx.constructorDef());
			moduleClass.getEOperations().add(constructor);
		} else if (ctx.entryDef() != null) {
			EntryOperation entry = visitEntryDef(ctx.entryDef());
			moduleClass.getEOperations().add(entry);
			module.setEntry(entry);
		} else if (ctx.propertyDecl() != null) {
			processPropertyDecl(ctx.propertyDecl(), module);
		} else if (ctx.intermediateClassDef() != null) {
			processIntermediateClass(ctx.intermediateClassDef(), module);
		} else if (ctx.tagDecl() != null) {
			EAnnotation tag = buildTag(ctx.tagDecl());
			if (tag != null) {
				module.getOwnedTag().add(tag);
			}
		} else if (ctx.typedefDecl() != null) {
			// Typedef: store as owned variable or classifier on module
			visitTypedefDecl(ctx.typedefDecl());
		}
	}

	// ==================== Mapping ====================

	@Override
	public MappingOperation visitMappingDef(QvtOParser.MappingDefContext ctx) {
		MappingOperation mapping = QVTO.createMappingOperation();

		// Qualifiers
		for (QvtOParser.QualifierContext qCtx : ctx.qualifier()) {
			applyOperationQualifier(mapping, qCtx.getText());
		}

		// Direction kind
		if (ctx.directionKind() != null) {
			// Direction on the mapping itself (rare, but valid)
		}

		// Scoped name: may include context type (Type::mappingName)
		String fullName = expressionBuilder.scopedNameText(ctx.scopedName());
		String mappingName = fullName;
		String contextName = null;
		int colonIdx = fullName.lastIndexOf("::");
		if (colonIdx >= 0) {
			contextName = fullName.substring(0, colonIdx);
			mappingName = fullName.substring(colonIdx + 2);
		}
		mapping.setName(mappingName);

		// Context parameter
		if (contextName != null) {
			VarParameter ctxParam = QVTO.createVarParameter();
			ctxParam.setName("self");
			mapping.setContext(ctxParam);
		}

		// Signature: parameters and result
		QvtOParser.MappingSignatureContext sigCtx = ctx.mappingSignature();
		buildSignature(mapping, sigCtx);

		// Set up environment for body
		QvtoEnvironment savedEnv = this.environment;
		setupOperationEnvironment(mapping);

		// TODO: resolve mapping extensions (inherits, merges, disjuncts) during linking phase
		for (QvtOParser.MappingExtensionContext extCtx : ctx.mappingExtension()) {
			// extCtx → populate mapping.getInherited()/getMerged()/getDisjunct()
		}

		// When clause
		if (ctx.whenClause() != null) {
			for (QvtOParser.StatementContext stmtCtx : ctx.whenClause().block().statement()) {
				OclExpression whenExpr = expressionBuilder.buildStatement(stmtCtx);
				if (whenExpr != null) {
					mapping.getWhen().add(whenExpr);
				}
			}
		}

		// Where clause
		if (ctx.whereClause() != null) {
			for (QvtOParser.StatementContext stmtCtx : ctx.whereClause().block().statement()) {
				OclExpression whereExpr = expressionBuilder.buildStatement(stmtCtx);
				if (whereExpr != null) {
					// where has single expression slot
					mapping.setWhere(whereExpr);
					break;
				}
			}
		}

		// Mapping body
		MappingBody body = buildMappingBody(ctx.mappingBody());
		mapping.setBody(body);

		this.environment = savedEnv;
		updateExpressionBuilder();
		return mapping;
	}

	private MappingBody buildMappingBody(QvtOParser.MappingBodyContext ctx) {
		MappingBody body = QVTO.createMappingBody();

		// Init section
		if (ctx.initSection() != null) {
			for (QvtOParser.StatementContext stmtCtx : ctx.initSection().block().statement()) {
				OclExpression expr = expressionBuilder.buildStatement(stmtCtx);
				if (expr != null) {
					body.getInitSection().add(expr);
				}
			}
		}

		// Population section (main body between init and end)
		if (ctx.statementList() != null) {
			for (QvtOParser.StatementContext stmtCtx : ctx.statementList().statement()) {
				OclExpression expr = expressionBuilder.buildStatement(stmtCtx);
				if (expr != null) {
					body.getContent().add(expr);
				}
			}
		}

		// End section
		if (ctx.endSection() != null) {
			for (QvtOParser.StatementContext stmtCtx : ctx.endSection().block().statement()) {
				OclExpression expr = expressionBuilder.buildStatement(stmtCtx);
				if (expr != null) {
					body.getEndSection().add(expr);
				}
			}
		}

		return body;
	}

	// ==================== Helper / Query ====================

	@Override
	public Helper visitHelperDef(QvtOParser.HelperDefContext ctx) {
		Helper helper = QVTO.createHelper();
		helper.setIsQuery(false);

		for (QvtOParser.QualifierContext qCtx : ctx.qualifier()) {
			applyOperationQualifier(helper, qCtx.getText());
		}

		String fullName = expressionBuilder.scopedNameText(ctx.scopedName());
		setOperationNameAndContext(helper, fullName);

		// Signature
		buildSimpleSignature(helper, ctx.simpleSignature());

		// Return type
		if (ctx.typeExpression() != null) {
			OclType returnType = expressionBuilder.resolveTypeExpression(ctx.typeExpression());
			setReturnType(helper, returnType);
		}

		// Body
		QvtoEnvironment savedEnv = this.environment;
		setupOperationEnvironment(helper);

		if (ctx.block() != null) {
			OperationBody body = QVTO.createOperationBody();
			for (QvtOParser.StatementContext stmtCtx : ctx.block().statement()) {
				OclExpression expr = expressionBuilder.buildStatement(stmtCtx);
				if (expr != null) {
					body.getContent().add(expr);
				}
			}
			helper.setBody(body);
		} else if (ctx.expression() != null) {
			OperationBody body = QVTO.createOperationBody();
			OclExpression expr = (OclExpression) expressionBuilder.visit(ctx.expression());
			body.getContent().add(expr);
			helper.setBody(body);
		}

		this.environment = savedEnv;
		updateExpressionBuilder();
		return helper;
	}

	@Override
	public Helper visitQueryDef(QvtOParser.QueryDefContext ctx) {
		Helper query = QVTO.createHelper();
		query.setIsQuery(true);

		for (QvtOParser.QualifierContext qCtx : ctx.qualifier()) {
			applyOperationQualifier(query, qCtx.getText());
		}

		String fullName = expressionBuilder.scopedNameText(ctx.scopedName());
		setOperationNameAndContext(query, fullName);

		buildSimpleSignature(query, ctx.simpleSignature());

		OclType returnType = expressionBuilder.resolveTypeExpression(ctx.typeExpression());
		setReturnType(query, returnType);

		QvtoEnvironment savedEnv = this.environment;
		setupOperationEnvironment(query);

		if (ctx.block() != null) {
			OperationBody body = QVTO.createOperationBody();
			for (QvtOParser.StatementContext stmtCtx : ctx.block().statement()) {
				OclExpression expr = expressionBuilder.buildStatement(stmtCtx);
				if (expr != null) {
					body.getContent().add(expr);
				}
			}
			query.setBody(body);
		} else if (ctx.expression() != null) {
			OperationBody body = QVTO.createOperationBody();
			OclExpression expr = (OclExpression) expressionBuilder.visit(ctx.expression());
			body.getContent().add(expr);
			query.setBody(body);
		}

		this.environment = savedEnv;
		updateExpressionBuilder();
		return query;
	}

	// ==================== Constructor ====================

	@Override
	public Constructor visitConstructorDef(QvtOParser.ConstructorDefContext ctx) {
		Constructor constructor = QVTO.createConstructor();

		String fullName = expressionBuilder.scopedNameText(ctx.scopedName());
		setOperationNameAndContext(constructor, fullName);

		buildSimpleSignature(constructor, ctx.simpleSignature());

		QvtoEnvironment savedEnv = this.environment;
		setupOperationEnvironment(constructor);

		var body = QVTO.createConstructorBody();
		for (QvtOParser.StatementContext stmtCtx : ctx.block().statement()) {
			OclExpression expr = expressionBuilder.buildStatement(stmtCtx);
			if (expr != null) {
				body.getContent().add(expr);
			}
		}
		constructor.setBody(body);

		this.environment = savedEnv;
		updateExpressionBuilder();
		return constructor;
	}

	// ==================== Entry ====================

	@Override
	public EntryOperation visitEntryDef(QvtOParser.EntryDefContext ctx) {
		EntryOperation entry = QVTO.createEntryOperation();
		entry.setName("main");

		if (ctx.simpleSignature() != null) {
			buildSimpleSignature(entry, ctx.simpleSignature());
		}

		QvtoEnvironment savedEnv = this.environment;
		setupOperationEnvironment(entry);

		OperationBody body = QVTO.createOperationBody();
		for (QvtOParser.StatementContext stmtCtx : ctx.block().statement()) {
			OclExpression expr = expressionBuilder.buildStatement(stmtCtx);
			if (expr != null) {
				body.getContent().add(expr);
			}
		}
		entry.setBody(body);

		this.environment = savedEnv;
		updateExpressionBuilder();
		return entry;
	}

	// ==================== Property ====================

	private void processPropertyDecl(QvtOParser.PropertyDeclContext ctx, Module module) {
		if (ctx.getChild(0).getText().equals("intermediate")) {
			// intermediate property Type::propName : Type
			ContextualProperty prop = QVTO.createContextualProperty();

			String fullName = expressionBuilder.scopedNameText(ctx.scopedName());
			int colonIdx = fullName.lastIndexOf("::");
			if (colonIdx >= 0) {
				String contextTypeName = fullName.substring(0, colonIdx);
				String propName = fullName.substring(colonIdx + 2);
				prop.setName(propName);
				// Resolve context type
				EClassifier ctxType = expressionBuilder.resolveClassifier(
						List.of(contextTypeName.split("::")));
				if (ctxType instanceof EClass ec) {
					prop.setContext(ec);
				}
			} else {
				prop.setName(fullName);
			}

			OclType type = expressionBuilder.resolveTypeExpression(ctx.typeExpression());
			if (type != null) {
				setFeatureType(prop, type);
			}

			if (ctx.expression() != null) {
				prop.setInitExpression((OclExpression) expressionBuilder.visit(ctx.expression()));
			}

			if (module instanceof OperationalTransformation ot) {
				ot.getIntermediateProperty().add(prop);
			}
		} else {
			// configuration property
			EAttribute configProp = EcoreFactory.eINSTANCE.createEAttribute();
			configProp.setName(QvtoExpressionBuilder.qvtoIdentifierText(ctx.qvtoIdentifier()));

			OclType type = expressionBuilder.resolveTypeExpression(ctx.typeExpression());
			if (type != null) {
				configProp.setEType(EcorePackage.Literals.ESTRING); // Default to String
			}

			module.getConfigProperty().add(configProp);
		}
	}

	// ==================== Intermediate Class ====================

	private void processIntermediateClass(QvtOParser.IntermediateClassDefContext ctx,
			Module module) {
		EClass intermediateClass = EcoreFactory.eINSTANCE.createEClass();
		intermediateClass.setName(QvtoExpressionBuilder.qvtoIdentifierText(ctx.qvtoIdentifier()));

		// TODO: resolve intermediate class supertypes during linking phase
		if (ctx.typeList() != null) {
			for (QvtOParser.TypeExpressionContext typeCtx : ctx.typeList().typeExpression()) {
				// typeCtx → intermediateClass.getESuperTypes().add(...)
			}
		}

		// Features
		for (QvtOParser.ClassifierFeatureContext featureCtx : ctx.classifierFeature()) {
			EAttribute attr = EcoreFactory.eINSTANCE.createEAttribute();
			attr.setName(QvtoExpressionBuilder.qvtoIdentifierText(featureCtx.qvtoIdentifier()));
			attr.setEType(EcorePackage.Literals.EJAVA_OBJECT);
			intermediateClass.getEStructuralFeatures().add(attr);
		}

		if (module instanceof OperationalTransformation ot) {
			ot.getIntermediateClass().add(intermediateClass);
		}
		module.getEClassifiers().add(intermediateClass);
	}

	// ==================== Tag ====================

	private EAnnotation buildTag(QvtOParser.TagDeclContext ctx) {
		EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
		String tagSource = ctx.STRING_LITERAL().getText();
		annotation.setSource(tagSource.substring(1, tagSource.length() - 1));

		if (ctx.expression() != null) {
			OclExpression value = (OclExpression) expressionBuilder.visit(ctx.expression());
			// Store tag value as detail entry
			annotation.getDetails().put("value", value.toString());
		}

		return annotation;
	}

	// ==================== Typedef ====================

	@Override
	public Typedef visitTypedefDecl(QvtOParser.TypedefDeclContext ctx) {
		Typedef typedef = IMP.createTypedef();
		typedef.setName(QvtoExpressionBuilder.qvtoIdentifierText(ctx.qvtoIdentifier()));

		OclType baseType = expressionBuilder.resolveTypeExpression(ctx.typeExpression());
		if (baseType instanceof org.eclipse.fennec.m2m.model.ocl.ClassifierType ct) {
			typedef.setBase(ct.getReferredClassifier());
		}

		if (ctx.expression() != null) {
			typedef.setCondition((OclExpression) expressionBuilder.visit(ctx.expression()));
		}

		return typedef;
	}

	// ==================== Helpers ====================

	private ModelParameter buildModelParameter(QvtOParser.ModelParamContext ctx) {
		ModelParameter param = QVTO.createModelParameter();
		param.setName(QvtoExpressionBuilder.qvtoIdentifierText(ctx.qvtoIdentifier()));
		param.setKind(resolveDirection(ctx.directionKind().getText()));
		return param;
	}

	private void buildSignature(
			org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation operation,
			QvtOParser.MappingSignatureContext sigCtx) {
		// Input parameters
		if (sigCtx.paramList() != null && sigCtx.paramList().size() > 0) {
			QvtOParser.ParamListContext inputParams = sigCtx.paramList(0);
			for (QvtOParser.ParamContext paramCtx : inputParams.param()) {
				MappingParameter param = QVTO.createMappingParameter();
				param.setName(QvtoExpressionBuilder.qvtoIdentifierText(paramCtx.qvtoIdentifier()));
				OclType type = expressionBuilder.resolveTypeExpression(paramCtx.typeExpression());
				if (type != null) {
					setParameterType(param, type);
				}
				operation.getEParameters().add(param);
			}
		}

		// Result parameters (after ':')
		if (sigCtx.paramList() != null && sigCtx.paramList().size() > 1) {
			QvtOParser.ParamListContext resultParams = sigCtx.paramList(1);
			for (QvtOParser.ParamContext paramCtx : resultParams.param()) {
				VarParameter resultParam = QVTO.createVarParameter();
				resultParam.setName(QvtoExpressionBuilder.qvtoIdentifierText(
						paramCtx.qvtoIdentifier()));
				OclType type = expressionBuilder.resolveTypeExpression(paramCtx.typeExpression());
				if (type != null) {
					setParameterType(resultParam, type);
				}
				operation.getResult().add(resultParam);
			}
		}
	}

	private void buildSimpleSignature(
			org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation operation,
			QvtOParser.SimpleSignatureContext sigCtx) {
		if (sigCtx == null || sigCtx.paramList() == null) {
			return;
		}
		for (QvtOParser.ParamContext paramCtx : sigCtx.paramList().param()) {
			VarParameter param = QVTO.createVarParameter();
			param.setName(QvtoExpressionBuilder.qvtoIdentifierText(paramCtx.qvtoIdentifier()));
			OclType type = expressionBuilder.resolveTypeExpression(paramCtx.typeExpression());
			if (type != null) {
				setParameterType(param, type);
			}
			operation.getEParameters().add(param);
		}
	}

	private void setupOperationEnvironment(
			org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation operation) {
		// self (context parameter)
		if (operation.getContext() != null) {
			Variable selfVar = OCL.createVariable();
			selfVar.setName("self");
			this.environment = this.environment.nested(selfVar);
		}

		// Parameters
		for (var param : operation.getEParameters()) {
			Variable paramVar = OCL.createVariable();
			paramVar.setName(param.getName());
			this.environment = this.environment.nested(paramVar);
		}

		// Result variables
		for (var resultParam : operation.getResult()) {
			Variable resultVar = OCL.createVariable();
			resultVar.setName(resultParam.getName());
			this.environment = this.environment.nested(resultVar);
		}

		// Implicit 'result' variable
		Variable resultVar = OCL.createVariable();
		resultVar.setName("result");
		this.environment = this.environment.nested(resultVar);

		updateExpressionBuilder();
	}

	private void setOperationNameAndContext(
			org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation operation,
			String fullName) {
		int colonIdx = fullName.lastIndexOf("::");
		if (colonIdx >= 0) {
			// TODO: resolve contextName to EClassifier and set context parameter type
			String contextName = fullName.substring(0, colonIdx);
			String opName = fullName.substring(colonIdx + 2);
			operation.setName(opName);

			VarParameter ctxParam = QVTO.createVarParameter();
			ctxParam.setName("self");
			operation.setContext(ctxParam);
		} else {
			operation.setName(fullName);
		}
	}

	private void setReturnType(
			org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation operation,
			OclType type) {
		VarParameter resultParam = QVTO.createVarParameter();
		resultParam.setName("result");
		if (type != null) {
			setParameterType(resultParam, type);
		}
		operation.getResult().add(resultParam);
	}

	private void setParameterType(VarParameter param, OclType type) {
		if (type instanceof org.eclipse.fennec.m2m.model.ocl.ClassifierType ct
				&& ct.getReferredClassifier() != null) {
			param.setEType(ct.getReferredClassifier());
		}
	}

	private void setFeatureType(ContextualProperty prop, OclType type) {
		if (type instanceof org.eclipse.fennec.m2m.model.ocl.ClassifierType ct
				&& ct.getReferredClassifier() != null) {
			prop.setEType(ct.getReferredClassifier());
		}
	}

	private void processModuleUsage(Module module, QvtOParser.ModuleUsageContext ctx) {
		String kind = ctx.moduleUsageKind().getText();
		// TODO: resolve module references and set imp.setImportedModule() during linking phase
		for (QvtOParser.QualifiedNameContext nameCtx : ctx.moduleRefList().qualifiedName()) {
			// nameCtx → resolve to Module and set as importedModule
			ModuleImport imp = QVTO.createModuleImport();
			imp.setKind("extends".equals(kind) ? ImportKind.EXTENSION : ImportKind.ACCESS);
			module.getModuleImport().add(imp);
		}
	}

	private void applyQualifier(OperationalTransformation transformation, String qualifier) {
		if ("blackbox".equals(qualifier)) {
			transformation.setIsBlackbox(true);
		}
	}

	private void applyOperationQualifier(
			org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation operation,
			String qualifier) {
		if ("blackbox".equals(qualifier)) {
			operation.setIsBlackbox(true);
		}
	}

	private EPackage resolvePackageRef(QvtOParser.PackageRefContext ctx) {
		if (ctx.pathName() != null) {
			List<String> segments = new ArrayList<>();
			for (var id : ctx.pathName().IDENTIFIER()) {
				segments.add(id.getText());
			}
			String name = String.join("::", segments);

			// Try nsURI from STRING_LITERAL in parens
			if (ctx.STRING_LITERAL() != null) {
				String nsURI = ctx.STRING_LITERAL().getText();
				nsURI = nsURI.substring(1, nsURI.length() - 1);
				if (packageRegistry != null) {
					EPackage pkg = packageRegistry.getEPackage(nsURI);
					if (pkg != null) {
						return pkg;
					}
				}
			}

			// Try by name
			if (packageRegistry != null) {
				for (Object key : packageRegistry.keySet().toArray()) {
					EPackage pkg = packageRegistry.getEPackage((String) key);
					if (pkg != null && name.equals(pkg.getName())) {
						return pkg;
					}
				}
			}
		} else if (ctx.STRING_LITERAL() != null) {
			String nsURI = ctx.STRING_LITERAL().getText();
			nsURI = nsURI.substring(1, nsURI.length() - 1);
			if (packageRegistry != null) {
				return packageRegistry.getEPackage(nsURI);
			}
		}
		return null;
	}

	/**
	 * Gets or creates the internal EClass that holds module operations.
	 * Since Module extends EPackage (not EClass), operations are stored
	 * on an EClass named after the module within the package.
	 */
	private EClass getOrCreateModuleClass(Module module) {
		String className = module.getName() != null ? module.getName() : "_Module";
		for (EClassifier classifier : module.getEClassifiers()) {
			if (classifier instanceof EClass ec && className.equals(ec.getName())) {
				return ec;
			}
		}
		EClass moduleClass = EcoreFactory.eINSTANCE.createEClass();
		moduleClass.setName(className);
		module.getEClassifiers().add(moduleClass);
		return moduleClass;
	}

	private DirectionKind resolveDirection(String text) {
		return switch (text) {
			case "out" -> DirectionKind.OUT;
			case "inout" -> DirectionKind.INOUT;
			default -> DirectionKind.IN;
		};
	}

	private void updateExpressionBuilder() {
		this.expressionBuilder = new QvtoExpressionBuilder(this.environment, this.packageRegistry);
	}

	private String qualifiedNameText(QvtOParser.QualifiedNameContext ctx) {
		return expressionBuilder.qualifiedNameText(ctx);
	}
}
