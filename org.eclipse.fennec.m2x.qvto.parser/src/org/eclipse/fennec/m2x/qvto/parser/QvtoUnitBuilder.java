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
package org.eclipse.fennec.m2x.qvto.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclFactory;
import org.eclipse.fennec.m2x.model.imperativeocl.Typedef;
import org.eclipse.fennec.m2x.model.ocl.ClassifierType;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.qvtoperational.Constructor;
import org.eclipse.fennec.m2x.model.qvtoperational.ContextualProperty;
import org.eclipse.fennec.m2x.model.qvtoperational.DirectionKind;
import org.eclipse.fennec.m2x.model.qvtoperational.EntryOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.Helper;
import org.eclipse.fennec.m2x.model.qvtoperational.ImperativeOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.ImportKind;
import org.eclipse.fennec.m2x.model.qvtoperational.Library;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingParameter;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelType;
import org.eclipse.fennec.m2x.model.qvtoperational.Module;
import org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationBody;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2x.model.qvtoperational.VarParameter;

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
	private static final AtomicLong INTERMEDIATE_SEQ =
			new AtomicLong();

	private record PendingExtension(MappingOperation mapping, String kind, String name) {}

	private final EPackage.Registry packageRegistry;
	private QvtoEnvironment environment;
	private QvtoExpressionBuilder expressionBuilder;
	private final List<PendingExtension> pendingExtensions = new ArrayList<>();
	private final Map<String, Module> importedModuleStubs = new HashMap<>();
	private String intermediatePackageUri;

	QvtoUnitBuilder(EPackage.Registry packageRegistry) {
		this.packageRegistry = packageRegistry;
		this.environment = QvtoEnvironment.root();
		this.expressionBuilder = new QvtoExpressionBuilder(environment, packageRegistry,
				importedModuleStubs);
	}

	// ==================== Entry Point ====================

	@Override
	public OperationalTransformation visitCompilationUnitEntry(
			QvtOParser.CompilationUnitEntryContext ctx) {
		return visitCompilationUnit(ctx.compilationUnit());
	}

	@Override
	public OperationalTransformation visitCompilationUnit(QvtOParser.CompilationUnitContext ctx) {
		// First pass: find transformation/library and collect modeltypes + inline libraries
		OperationalTransformation transformation = null;
		List<ModelType> modelTypes = new ArrayList<>();
		List<Library> inlineLibraries = new ArrayList<>();

		for (QvtOParser.UnitElementContext elemCtx : ctx.unitElement()) {
			if (elemCtx.transformationDef() != null) {
				transformation = visitTransformationDef(elemCtx.transformationDef());
			} else if (elemCtx.libraryDef() != null) {
				Library lib = visitLibraryDef(elemCtx.libraryDef());
				inlineLibraries.add(lib);
			} else if (elemCtx.modeltypeDecl() != null) {
				modelTypes.add(visitModeltypeDecl(elemCtx.modeltypeDecl()));
			}
		}

		if (transformation == null) {
			// Standalone library — wrap as transformation for uniform return type
			if (!inlineLibraries.isEmpty()) {
				Library firstLib = inlineLibraries.remove(0);
				transformation = QVTO.createOperationalTransformation();
				transformation.setName(firstLib.getName());
			} else {
				transformation = QVTO.createOperationalTransformation();
				transformation.setName("_unnamed");
			}
		}

		// Add all collected modeltypes to transformation
		transformation.getUsedModelType().addAll(modelTypes);

		// Link model parameters to their ModelType declarations
		for (ModelParameter mp : transformation.getModelParameter()) {
			EAnnotation ann = mp.getEAnnotation("qvto.modeltype");
			if (ann != null) {
				String typeName = ann.getDetails().get("name");
				for (ModelType mt : modelTypes) {
					if (mt.getName().equals(typeName)) {
						mp.setEType(mt);
						break;
					}
				}
				mp.getEAnnotations().remove(ann);
			}
		}

		// Register inline libraries as implicit access imports (§8.1.4)
		for (Library lib : inlineLibraries) {
			ModuleImport imp = QVTO.createModuleImport();
			imp.setKind(ImportKind.ACCESS);
			imp.setImportedModule(lib);
			transformation.getModuleImport().add(imp);
		}

		// §8.4: For declaration form (transformation T(...);) module elements follow at unit level.
		// Set up model parameter environment so top-level elements can reference model params.
		boolean hasTopLevelModuleElements = false;
		for (QvtOParser.UnitElementContext elemCtx : ctx.unitElement()) {
			if (elemCtx.moduleElement() != null) {
				hasTopLevelModuleElements = true;
				break;
			}
		}

		QvtoEnvironment savedEnv = null;
		if (hasTopLevelModuleElements && transformation != null) {
			savedEnv = this.environment;
			for (ModelParameter mp : transformation.getModelParameter()) {
				Variable var = OCL.createVariable();
				var.setName(mp.getName());
				this.environment = this.environment.nested(var);
			}
			updateExpressionBuilder();
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

		// Resolve deferred mapping extensions for top-level module elements
		if (hasTopLevelModuleElements && transformation != null) {
			resolvePendingExtensions(transformation);
		}

		if (savedEnv != null) {
			this.environment = savedEnv;
			updateExpressionBuilder();
		}

		// Clean up intermediate package from global registry (no longer needed after parse)
		if (intermediatePackageUri != null && packageRegistry != null) {
			packageRegistry.remove(intermediatePackageUri);
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

		// Resolve deferred mapping extensions (disjuncts, inherits, merges)
		resolvePendingExtensions(transformation);

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

		// Resolve deferred mapping extensions (disjuncts, inherits, merges)
		resolvePendingExtensions(library);

		return library;
	}

	// ==================== Import + Modeltype ====================

	@Override
	public ModuleImport visitQvtoImportDecl(QvtOParser.QvtoImportDeclContext ctx) {
		ModuleImport imp = QVTO.createModuleImport();
		imp.setKind(ImportKind.ACCESS);
		// Store qualified name as stub library for link-time resolution
		String qualifiedName = qualifiedNameText(ctx.qualifiedName());
		Library stub = QVTO.createLibrary();
		stub.setName(qualifiedName);
		imp.setImportedModule(stub);
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

		// §8.2.1.6: Additional conditions from where clause
		if (ctx.modeltypeWhere() != null) {
			for (QvtOParser.ExpressionContext exprCtx : ctx.modeltypeWhere().expression()) {
				OclExpression condition = (OclExpression) expressionBuilder.visit(exprCtx);
				if (condition != null) {
					modelType.getAdditionalCondition().add(condition);
				}
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
		int colonIdx = fullName.lastIndexOf("::");
		if (colonIdx >= 0) {
			mappingName = fullName.substring(colonIdx + 2);
		}
		mapping.setName(mappingName);

		// Context parameter — resolve type from scoped name
		OclType contextType = expressionBuilder.resolveContextType(ctx.scopedName());
		if (contextType != null) {
			VarParameter ctxParam = QVTO.createVarParameter();
			ctxParam.setName("self");
			setParameterType(ctxParam, contextType);
			mapping.setContext(ctxParam);
		}

		// Signature: parameters and result
		QvtOParser.MappingSignatureContext sigCtx = ctx.mappingSignature();
		buildSignature(mapping, sigCtx);

		// Set up environment for body
		QvtoEnvironment savedEnv = this.environment;
		setupOperationEnvironment(mapping);

		// Collect mapping extensions for deferred resolution (after all operations are parsed)
		for (QvtOParser.MappingExtensionContext extCtx : ctx.mappingExtension()) {
			String kind = extCtx.mappingExtensionKind().getText();
			for (QvtOParser.ScopedNameContext nameCtx : extCtx.scopedNameList().scopedName()) {
				String refName = expressionBuilder.scopedNameText(nameCtx);
				pendingExtensions.add(new PendingExtension(mapping, kind, refName));
			}
		}

		// When clause — §8.4: guardBlock with expression list (';' as separator)
		if (ctx.whenClause() != null) {
			for (QvtOParser.ExpressionContext exprCtx : ctx.whenClause().guardBlock().expression()) {
				OclExpression whenExpr = (OclExpression) expressionBuilder.visit(exprCtx);
				if (whenExpr != null) {
					mapping.getWhen().add(whenExpr);
				}
			}
		}

		// Where clause — §8.4: guardBlock with expression list
		if (ctx.whereClause() != null) {
			for (QvtOParser.ExpressionContext exprCtx : ctx.whereClause().guardBlock().expression()) {
				OclExpression whereExpr = (OclExpression) expressionBuilder.visit(exprCtx);
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
		if (ctx.populationSection() != null) {
			// §8.2.1.19: Explicit population keyword — content goes into body.content
			for (QvtOParser.StatementContext stmtCtx : ctx.populationSection().block().statement()) {
				OclExpression expr = expressionBuilder.buildStatement(stmtCtx);
				if (expr != null) {
					body.getContent().add(expr);
				}
			}
		} else if (ctx.statementList() != null) {
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

		setOperationNameAndContext(helper, ctx.scopedName());

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

		setOperationNameAndContext(query, ctx.scopedName());

		buildSimpleSignature(query, ctx.simpleSignature());

		// §8.4: query supports resultList (single or multi-result)
		for (QvtOParser.ResultParamContext rCtx : ctx.resultList().resultParam()) {
			VarParameter resultParam = QVTO.createVarParameter();
			if (rCtx instanceof QvtOParser.NamedResultContext named) {
				resultParam.setName(QvtoExpressionBuilder.qvtoIdentifierText(
						named.qvtoIdentifier()));
				OclType type = expressionBuilder.resolveTypeExpression(named.typeExpression());
				if (type != null) {
					setParameterType(resultParam, type);
				}
			} else if (rCtx instanceof QvtOParser.UnnamedResultContext unnamed) {
				resultParam.setName("result");
				OclType type = expressionBuilder.resolveTypeExpression(unnamed.typeExpression());
				if (type != null) {
					setParameterType(resultParam, type);
				}
			}
			query.getResult().add(resultParam);
		}

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

		setOperationNameAndContext(constructor, ctx.scopedName());

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
		} else if (ctx.getChild(0).getText().equals("configuration")) {
			// configuration property
			EAttribute configProp = EcoreFactory.eINSTANCE.createEAttribute();
			configProp.setName(QvtoExpressionBuilder.qvtoIdentifierText(ctx.qvtoIdentifier()));

			OclType type = expressionBuilder.resolveTypeExpression(ctx.typeExpression());
			if (type != null) {
				configProp.setEType(EcorePackage.Literals.ESTRING); // Default to String
			}

			module.getConfigProperty().add(configProp);
		} else {
			// §8.1.18: module-level property (non-configuration, non-intermediate)
			// Supports typed (property x : T = expr;) and untyped (property x = expr;) forms
			// Store as Variable in module.ownedVariable for evaluation at startup
			Variable moduleProp = OCL.createVariable();
			moduleProp.setName(QvtoExpressionBuilder.qvtoIdentifierText(ctx.qvtoIdentifier()));

			// Set type if explicitly declared (typed form)
			if (ctx.typeExpression() != null) {
				OclType type = expressionBuilder.resolveTypeExpression(ctx.typeExpression());
				if (type != null) {
					moduleProp.setType(type);
				}
			}

			if (ctx.expression() != null) {
				moduleProp.setOwnedInit(
						(OclExpression) expressionBuilder.visit(ctx.expression()));
			}

			module.getOwnedVariable().add(moduleProp);
		}
	}

	// ==================== Intermediate Class ====================

	private void processIntermediateClass(QvtOParser.IntermediateClassDefContext ctx,
			Module module) {
		EClass intermediateClass = EcoreFactory.eINSTANCE.createEClass();
		intermediateClass.setName(QvtoExpressionBuilder.qvtoIdentifierText(ctx.qvtoIdentifier()));

		// §8.2.1.3: Register in _INTERMEDIATE package so resolveClassifier finds it
		// and EcoreUtil.create() can instantiate it via the package's EFactory.
		// MUST be added to intermPkg LAST since EMF containment moves the EClass.
		EPackage intermPkg = getOrCreateIntermediatePackage(module);

		// Temporarily add to intermPkg so supertype resolution can find it
		intermPkg.getEClassifiers().add(intermediateClass);

		// Resolve intermediate class supertypes (extends clause)
		if (ctx.typeList() != null) {
			for (QvtOParser.TypeExpressionContext typeCtx : ctx.typeList().typeExpression()) {
				OclType resolved = expressionBuilder.resolveTypeExpression(typeCtx);
				if (resolved instanceof ClassifierType ct
						&& ct.getReferredClassifier() instanceof EClass superClass) {
					intermediateClass.getESuperTypes().add(superClass);
				}
			}
		}

		// Features (with optional modifiers and default value expressions)
		for (QvtOParser.ClassifierFeatureContext featureCtx : ctx.classifierFeature()) {
			EAttribute attr = EcoreFactory.eINSTANCE.createEAttribute();
			attr.setName(QvtoExpressionBuilder.qvtoIdentifierText(featureCtx.qvtoIdentifier()));
			attr.setEType(EcorePackage.Literals.EJAVA_OBJECT);

			// Process modifiers: static, readonly, references, composes, <<id>>
			for (QvtOParser.ClassifierFeatureModifierContext modCtx : featureCtx.classifierFeatureModifier()) {
				if (modCtx.STEREOTYPE_ID() != null) {
					// §8.2.1.3: <<id>> stereotype — marks attribute as EClass ID
					attr.setID(true);
				} else {
					String modText = modCtx.getText();
					EAnnotation modAnn = EcoreFactory.eINSTANCE.createEAnnotation();
					modAnn.setSource("fennec:intermediate:modifier");
					modAnn.getDetails().put(modText, "true");
					attr.getEAnnotations().add(modAnn);
				}
			}

			// Store default value expression if present
			if (featureCtx.expression() != null) {
				OclExpression defaultExpr = (OclExpression) expressionBuilder.visit(featureCtx.expression());
				if (defaultExpr != null) {
					EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
					ann.setSource("fennec:intermediate:default");
					ann.getReferences().add(defaultExpr);
					attr.getEAnnotations().add(ann);
				}
			}

			intermediateClass.getEStructuralFeatures().add(attr);
		}

		if (module instanceof OperationalTransformation ot) {
			ot.getIntermediateClass().add(intermediateClass);
		}

		// Ensure the EClass ends up in intermPkg (not module) for EcoreUtil.create()
		// The add to intermPkg above already placed it there; module.getEClassifiers
		// would move it (EMF containment). So we do NOT add to module.eClassifiers.
	}

	/**
	 * §8.2.1.3: Gets or creates the implicit _INTERMEDIATE package for intermediate classes.
	 * Registers the package in the packageRegistry so type resolution works.
	 */
	private EPackage getOrCreateIntermediatePackage(Module module) {
		if (intermediatePackageUri != null && packageRegistry != null) {
			EPackage existing = packageRegistry.getEPackage(intermediatePackageUri);
			if (existing != null) {
				return existing;
			}
		}
		// Unique URI per builder instance to avoid cross-parse collisions
		intermediatePackageUri = "http://intermediate/" + module.getName()
				+ "/" + INTERMEDIATE_SEQ.incrementAndGet();
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("_INTERMEDIATE");
		pkg.setNsPrefix("_intermediate");
		pkg.setNsURI(intermediatePackageUri);
		if (packageRegistry != null) {
			packageRegistry.put(intermediatePackageUri, pkg);
		}
		return pkg;
	}

	// ==================== Tag ====================

	private EAnnotation buildTag(QvtOParser.TagDeclContext ctx) {
		EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
		String tagSource = ctx.STRING_LITERAL() != null
				? ctx.STRING_LITERAL().getText()
				: ctx.DOUBLE_QUOTED_STRING().getText();
		annotation.setSource(tagSource.substring(1, tagSource.length() - 1));

		// Store the tag target path (e.g. "ecore::EPackage::name")
		if (ctx.tagTarget() != null) {
			String targetPath = ctx.tagTarget().qvtoIdentifier().stream()
					.map(QvtoExpressionBuilder::qvtoIdentifierText)
					.reduce((a, b) -> a + "::" + b)
					.orElse("");
			annotation.getDetails().put("target", targetPath);
		}

		if (ctx.expression() != null) {
			OclExpression value = (OclExpression) expressionBuilder.visit(ctx.expression());
			// Store tag value — extract string from StringLiteralExp
			if (value instanceof StringLiteralExp strLit) {
				annotation.getDetails().put("value", strLit.getStringSymbol());
			} else {
				annotation.getDetails().put("value", value.toString());
			}
		}

		return annotation;
	}

	// ==================== Typedef ====================

	@Override
	public Typedef visitTypedefDecl(QvtOParser.TypedefDeclContext ctx) {
		Typedef typedef = IMP.createTypedef();
		typedef.setName(QvtoExpressionBuilder.qvtoIdentifierText(ctx.qvtoIdentifier()));

		OclType baseType = expressionBuilder.resolveTypeExpression(ctx.typeExpression());
		if (baseType instanceof ClassifierType ct) {
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
		param.setKind(resolveDirection(ctx.directionKind().getText()));

		// §8.4: Named param (directionKind name ':' typeSpec) vs unnamed (directionKind typeSpec)
		if (ctx.qvtoIdentifier() != null) {
			param.setName(QvtoExpressionBuilder.qvtoIdentifierText(ctx.qvtoIdentifier()));
		} else {
			// Unnamed: derive name from type (e.g., 'out ecore' → name 'ecore')
			String typeName = ctx.typeSpec().typeExpression().getText();
			param.setName(typeName);
		}

		// Store the type name from typeSpec for later linking to ModelType
		if (ctx.typeSpec() != null && ctx.typeSpec().typeExpression() != null) {
			String typeName = ctx.typeSpec().typeExpression().getText();
			// Temporarily store as EAnnotation for post-processing
			EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
			ann.setSource("qvto.modeltype");
			ann.getDetails().put("name", typeName);
			param.getEAnnotations().add(ann);
		}
		return param;
	}

	private void buildSignature(
			ImperativeOperation operation,
			QvtOParser.MappingSignatureContext sigCtx) {
		// Input parameters (labeled 'inputParams' in grammar)
		if (sigCtx.inputParams != null) {
			for (QvtOParser.ParamContext paramCtx : sigCtx.inputParams.param()) {
				MappingParameter param = QVTO.createMappingParameter();
				param.setName(QvtoExpressionBuilder.qvtoIdentifierText(paramCtx.qvtoIdentifier()));
				OclType type = expressionBuilder.resolveTypeExpression(paramCtx.typeExpression());
				if (type != null) {
					setParameterType(param, type);
				}
				operation.getEParameters().add(param);
			}
		}

		// Result parameters (§8.4: named 'r : Type' or unnamed 'Type' → implicit 'result')
		if (sigCtx.resultList() != null) {
			for (QvtOParser.ResultParamContext rCtx : sigCtx.resultList().resultParam()) {
				VarParameter resultParam = QVTO.createVarParameter();
				if (rCtx instanceof QvtOParser.NamedResultContext named) {
					resultParam.setName(QvtoExpressionBuilder.qvtoIdentifierText(
							named.qvtoIdentifier()));
					OclType type = expressionBuilder.resolveTypeExpression(named.typeExpression());
					if (type != null) {
						setParameterType(resultParam, type);
					}
				} else if (rCtx instanceof QvtOParser.UnnamedResultContext unnamed) {
					resultParam.setName("result");
					OclType type = expressionBuilder.resolveTypeExpression(unnamed.typeExpression());
					if (type != null) {
						setParameterType(resultParam, type);
					}
				}
				operation.getResult().add(resultParam);
			}
		}
	}

	private void buildSimpleSignature(
			ImperativeOperation operation,
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
			ImperativeOperation operation) {
		// self (context parameter) — set type so self.property resolves during parsing
		if (operation.getContext() != null) {
			Variable selfVar = OCL.createVariable();
			selfVar.setName("self");
			EClassifier ctxType = operation.getContext().getEType();
			if (ctxType != null) {
				selfVar.setType(expressionBuilder.createClassifierType(ctxType));
			}
			this.environment = this.environment.nested(selfVar);
		}

		// Parameters — set type for property resolution
		for (var param : operation.getEParameters()) {
			Variable paramVar = OCL.createVariable();
			paramVar.setName(param.getName());
			if (param.getEType() != null) {
				paramVar.setType(expressionBuilder.createClassifierType(param.getEType()));
			}
			this.environment = this.environment.nested(paramVar);
		}

		// Result variables — set type for property resolution
		for (var resultParam : operation.getResult()) {
			Variable resultVar = OCL.createVariable();
			resultVar.setName(resultParam.getName());
			if (resultParam.getEType() != null) {
				resultVar.setType(expressionBuilder.createClassifierType(resultParam.getEType()));
			}
			this.environment = this.environment.nested(resultVar);
		}

		// Implicit 'result' variable — use first result param type if available
		Variable resultVar = OCL.createVariable();
		resultVar.setName("result");
		if (!operation.getResult().isEmpty() && operation.getResult().get(0).getEType() != null) {
			resultVar.setType(expressionBuilder.createClassifierType(
					operation.getResult().get(0).getEType()));
		}
		this.environment = this.environment.nested(resultVar);

		updateExpressionBuilder();
	}

	private void setOperationNameAndContext(
			ImperativeOperation operation,
			QvtOParser.ScopedNameContext scopedNameCtx) {
		String fullName = expressionBuilder.scopedNameText(scopedNameCtx);
		int colonIdx = fullName.lastIndexOf("::");
		if (colonIdx >= 0) {
			String opName = fullName.substring(colonIdx + 2);
			operation.setName(opName);

			VarParameter ctxParam = QVTO.createVarParameter();
			ctxParam.setName("self");
			// §8.2.1.10: Resolve context type so engine can dispatch by owner type
			OclType contextType = expressionBuilder.resolveContextType(scopedNameCtx);
			if (contextType != null) {
				setParameterType(ctxParam, contextType);
			}
			operation.setContext(ctxParam);
		} else {
			operation.setName(fullName);
		}
	}

	private void setReturnType(
			ImperativeOperation operation,
			OclType type) {
		VarParameter resultParam = QVTO.createVarParameter();
		resultParam.setName("result");
		if (type != null) {
			setParameterType(resultParam, type);
		}
		operation.getResult().add(resultParam);
	}

	private void setParameterType(VarParameter param, OclType type) {
		if (type instanceof ClassifierType ct
				&& ct.getReferredClassifier() != null) {
			param.setEType(ct.getReferredClassifier());
		} else if (type instanceof PrimitiveType pt) {
			// §8.2.1.10: Store primitive context type as EDataType for engine dispatch
			EDataType dt = EcoreFactory.eINSTANCE.createEDataType();
			dt.setName(pt.getName());
			param.setEType(dt);
		}
	}

	private void setFeatureType(ContextualProperty prop, OclType type) {
		if (type instanceof ClassifierType ct
				&& ct.getReferredClassifier() != null) {
			prop.setEType(ct.getReferredClassifier());
		}
	}

	private void processModuleUsage(Module module, QvtOParser.ModuleUsageContext ctx) {
		String kind = ctx.moduleUsageKind().getText();
		for (QvtOParser.QualifiedNameContext nameCtx : ctx.moduleRefList().qualifiedName()) {
			ModuleImport imp = QVTO.createModuleImport();
			imp.setKind("extends".equals(kind) ? ImportKind.EXTENSION : ImportKind.ACCESS);
			// Store qualified name as stub for link-time resolution
			String qualifiedName = qualifiedNameText(nameCtx);
			// Create OperationalTransformation stub so TransformationInstantiationExp
			// can reference it (Library would fail the instanceof check in visitNewExp)
			OperationalTransformation stub = QVTO.createOperationalTransformation();
			stub.setName(qualifiedName);
			imp.setImportedModule(stub);
			module.getModuleImport().add(imp);
			// Track for TransformationInstantiationExp detection in visitNewExp
			importedModuleStubs.put(qualifiedName, stub);
			// Also track simple name (last segment) for unqualified references
			int dotIdx = qualifiedName.lastIndexOf('.');
			if (dotIdx >= 0) {
				importedModuleStubs.put(qualifiedName.substring(dotIdx + 1), stub);
			}
		}
	}

	private void applyQualifier(OperationalTransformation transformation, String qualifier) {
		if ("blackbox".equals(qualifier)) {
			transformation.setIsBlackbox(true);
		}
	}

	private void applyOperationQualifier(
			ImperativeOperation operation,
			String qualifier) {
		if ("blackbox".equals(qualifier)) {
			operation.setIsBlackbox(true);
		}
	}

	private EPackage resolvePackageRef(QvtOParser.PackageRefContext ctx) {
		if (ctx.pathName() != null) {
			List<String> segments = new ArrayList<>();
			for (var id : ctx.pathName().qvtoIdentifier()) {
				segments.add(QvtoExpressionBuilder.qvtoIdentifierText(id));
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
	private void resolvePendingExtensions(Module module) {
		if (pendingExtensions.isEmpty()) {
			return;
		}
		EClass moduleClass = getOrCreateModuleClass(module);
		for (PendingExtension pe : pendingExtensions) {
			// Extract simple name from scoped name (e.g., "SourceElement::toHigh" → "toHigh")
			String simpleName = pe.name.contains("::")
					? pe.name.substring(pe.name.lastIndexOf("::") + 2)
					: pe.name;
			for (EOperation op : moduleClass.getEOperations()) {
				if (op instanceof MappingOperation mo && simpleName.equals(mo.getName())) {
					switch (pe.kind) {
					case "inherits" -> pe.mapping.getInherited().add(mo);
					case "merges" -> pe.mapping.getMerged().add(mo);
					case "disjuncts" -> pe.mapping.getDisjunct().add(mo);
					default -> { /* ignore unknown kinds */ }
					}
					break;
				}
			}
		}
		pendingExtensions.clear();
	}

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
		this.expressionBuilder = new QvtoExpressionBuilder(this.environment, this.packageRegistry,
				this.importedModuleStubs);
	}

	private String qualifiedNameText(QvtOParser.QualifiedNameContext ctx) {
		return expressionBuilder.qualifiedNameText(ctx);
	}
}
