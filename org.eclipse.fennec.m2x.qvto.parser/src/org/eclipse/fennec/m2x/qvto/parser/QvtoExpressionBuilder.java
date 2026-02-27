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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.model.imperativeocl.AltExp;
import org.eclipse.fennec.m2x.model.imperativeocl.AssertExp;
import org.eclipse.fennec.m2x.model.imperativeocl.AssignExp;
import org.eclipse.fennec.m2x.model.imperativeocl.BlockExp;
import org.eclipse.fennec.m2x.model.imperativeocl.CatchExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ComputeExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ForExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclFactory;
import org.eclipse.fennec.m2x.model.imperativeocl.InstantiationExp;
import org.eclipse.fennec.m2x.model.imperativeocl.LogExp;
import org.eclipse.fennec.m2x.model.imperativeocl.TransformationInstantiationExp;
import org.eclipse.fennec.m2x.model.imperativeocl.RaiseExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ReturnExp;
import org.eclipse.fennec.m2x.model.imperativeocl.SeverityKind;
import org.eclipse.fennec.m2x.model.imperativeocl.SwitchExp;
import org.eclipse.fennec.m2x.model.imperativeocl.TryExp;
import org.eclipse.fennec.m2x.model.imperativeocl.VariableInitExp;
import org.eclipse.fennec.m2x.model.imperativeocl.WhileExp;
import org.eclipse.fennec.m2x.model.ocl.BooleanLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.ClassifierType;
import org.eclipse.fennec.m2x.model.ocl.CollectionItem;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.CollectionLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.CollectionRange;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.EnumLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.IfExp;
import org.eclipse.fennec.m2x.model.ocl.IntegerLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.InvalidLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.IterateExp;
import org.eclipse.fennec.m2x.model.ocl.IteratorExp;
import org.eclipse.fennec.m2x.model.ocl.LetExp;
import org.eclipse.fennec.m2x.model.ocl.MapLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.MapLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.MapType;
import org.eclipse.fennec.m2x.model.ocl.NullLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2x.model.ocl.RealLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.SequenceType;
import org.eclipse.fennec.m2x.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.TupleLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.TupleLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.TypeExp;
import org.eclipse.fennec.m2x.model.ocl.UnlimitedNaturalLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingCallExp;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.Module;
import org.eclipse.fennec.m2x.model.qvtoperational.ObjectExp;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2x.model.qvtoperational.ResolveExp;
import org.eclipse.fennec.m2x.model.qvtoperational.ResolveInExp;

/**
 * Visitor that transforms ANTLR4 parse tree nodes into EMF OCL + Imperative OCL AST nodes.
 *
 * <p>Handles all expression-level constructs: OCL expressions (literals, operators,
 * navigation, iterators) and QVT-O imperative expressions (block, while, for, switch,
 * compute, object, mapping call, resolve, try/catch, assert, log, return, break, continue,
 * variable declarations, assignments).
 *
 * <p>Re-implements the OCL expression visitor logic from {@code OclAstBuilder} (which is
 * package-private in {@code ocl.parser}) and adds imperative expression building.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoExpressionBuilder extends QvtOBaseVisitor<Object> {

	private static final OclFactory OCL = OclFactory.eINSTANCE;
	private static final ImperativeOclFactory IMP = ImperativeOclFactory.eINSTANCE;
	private static final QvtOperationalFactory QVTO = QvtOperationalFactory.eINSTANCE;

	private final EPackage.Registry packageRegistry;
	private final Map<String, Module> importedModuleStubs;
	private QvtoEnvironment environment;

	QvtoExpressionBuilder(QvtoEnvironment environment, EPackage.Registry packageRegistry) {
		this(environment, packageRegistry, Map.of());
	}

	QvtoExpressionBuilder(QvtoEnvironment environment, EPackage.Registry packageRegistry,
			Map<String, Module> importedModuleStubs) {
		this.environment = Objects.requireNonNull(environment,
				"environment must not be null");
		this.packageRegistry = packageRegistry;
		this.importedModuleStubs = Objects.requireNonNull(importedModuleStubs);
	}

	// ==================== OCL Literals ====================

	@Override
	public IntegerLiteralExp visitIntegerLiteral(QvtOParser.IntegerLiteralContext ctx) {
		IntegerLiteralExp exp = OCL.createIntegerLiteralExp();
		exp.setIntegerSymbol(Long.parseLong(ctx.INTEGER_LITERAL().getText()));
		return exp;
	}

	@Override
	public RealLiteralExp visitRealLiteral(QvtOParser.RealLiteralContext ctx) {
		RealLiteralExp exp = OCL.createRealLiteralExp();
		exp.setRealSymbol(Double.parseDouble(ctx.REAL_LITERAL().getText()));
		return exp;
	}

	@Override
	public StringLiteralExp visitStringLiteral(QvtOParser.StringLiteralContext ctx) {
		StringLiteralExp exp = OCL.createStringLiteralExp();
		// stringLiteral_ rule: (STRING_LITERAL | DOUBLE_QUOTED_STRING)+
		// Adjacent tokens are concatenated in order (Eclipse multilineStrings_262733)
		QvtOParser.StringLiteral_Context strCtx = ctx.stringLiteral_();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strCtx.getChildCount(); i++) {
			String text = strCtx.getChild(i).getText();
			sb.append(unescapeString(text.substring(1, text.length() - 1)));
		}
		exp.setStringSymbol(sb.toString());
		return exp;
	}

	@Override
	public BooleanLiteralExp visitTrueLiteral(QvtOParser.TrueLiteralContext ctx) {
		BooleanLiteralExp exp = OCL.createBooleanLiteralExp();
		exp.setBooleanSymbol(true);
		return exp;
	}

	@Override
	public BooleanLiteralExp visitFalseLiteral(QvtOParser.FalseLiteralContext ctx) {
		BooleanLiteralExp exp = OCL.createBooleanLiteralExp();
		exp.setBooleanSymbol(false);
		return exp;
	}

	@Override
	public NullLiteralExp visitNullLiteral(QvtOParser.NullLiteralContext ctx) {
		return OCL.createNullLiteralExp();
	}

	@Override
	public InvalidLiteralExp visitInvalidLiteral(QvtOParser.InvalidLiteralContext ctx) {
		return OCL.createInvalidLiteralExp();
	}

	@Override
	public UnlimitedNaturalLiteralExp visitUnlimitedNaturalLiteral(
			QvtOParser.UnlimitedNaturalLiteralContext ctx) {
		UnlimitedNaturalLiteralExp exp = OCL.createUnlimitedNaturalLiteralExp();
		exp.setUnlimitedNaturalSymbol(-1L);
		return exp;
	}

	// ==================== Simple Literals (Dict keys, §8.4.7) ====================

	@Override
	public IntegerLiteralExp visitSimpleLitInt(QvtOParser.SimpleLitIntContext ctx) {
		IntegerLiteralExp exp = OCL.createIntegerLiteralExp();
		exp.setIntegerSymbol(Long.parseLong(ctx.INTEGER_LITERAL().getText()));
		return exp;
	}

	@Override
	public RealLiteralExp visitSimpleLitReal(QvtOParser.SimpleLitRealContext ctx) {
		RealLiteralExp exp = OCL.createRealLiteralExp();
		exp.setRealSymbol(Double.parseDouble(ctx.REAL_LITERAL().getText()));
		return exp;
	}

	@Override
	public StringLiteralExp visitSimpleLitString(QvtOParser.SimpleLitStringContext ctx) {
		StringLiteralExp exp = OCL.createStringLiteralExp();
		QvtOParser.StringLiteral_Context strCtx = ctx.stringLiteral_();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strCtx.getChildCount(); i++) {
			String text = strCtx.getChild(i).getText();
			sb.append(unescapeString(text.substring(1, text.length() - 1)));
		}
		exp.setStringSymbol(sb.toString());
		return exp;
	}

	@Override
	public BooleanLiteralExp visitSimpleLitTrue(QvtOParser.SimpleLitTrueContext ctx) {
		BooleanLiteralExp exp = OCL.createBooleanLiteralExp();
		exp.setBooleanSymbol(true);
		return exp;
	}

	@Override
	public BooleanLiteralExp visitSimpleLitFalse(QvtOParser.SimpleLitFalseContext ctx) {
		BooleanLiteralExp exp = OCL.createBooleanLiteralExp();
		exp.setBooleanSymbol(false);
		return exp;
	}

	@Override
	public NullLiteralExp visitSimpleLitNull(QvtOParser.SimpleLitNullContext ctx) {
		return OCL.createNullLiteralExp();
	}

	@Override
	public UnlimitedNaturalLiteralExp visitSimpleLitUnlimited(
			QvtOParser.SimpleLitUnlimitedContext ctx) {
		UnlimitedNaturalLiteralExp exp = OCL.createUnlimitedNaturalLiteralExp();
		exp.setUnlimitedNaturalSymbol(-1L);
		return exp;
	}

	// ==================== Path Name Resolution ====================

	@Override
	public OclExpression visitPathNameExp(QvtOParser.PathNameExpContext ctx) {
		List<String> segments = pathNameSegments(ctx.pathName());

		if (segments.size() == 1) {
			String name = segments.get(0);
			return environment.lookup(name)
					.<OclExpression>map(this::createVariableExp)
					.orElseGet(() -> resolveImplicitProperty(name));
		}

		if (segments.size() == 2) {
			EnumLiteralExp enumExp = tryResolveEnumLiteral(segments);
			if (enumExp != null) {
				return enumExp;
			}
		}

		return resolveQualifiedName(segments);
	}

	// ==================== Collection Literals ====================

	@Override
	public CollectionLiteralExp visitCollectionLit(QvtOParser.CollectionLitContext ctx) {
		return (CollectionLiteralExp) visit(ctx.collectionLiteral());
	}

	@Override
	public CollectionLiteralExp visitCollectionLiteral(QvtOParser.CollectionLiteralContext ctx) {
		CollectionLiteralExp exp = OCL.createCollectionLiteralExp();
		exp.setKind(resolveCollectionKind(ctx.collectionKind().getText()));
		if (ctx.collectionLiteralPart() != null) {
			for (QvtOParser.CollectionLiteralPartContext partCtx : ctx.collectionLiteralPart()) {
				exp.getOwnedParts().add(visitCollectionLiteralPart(partCtx));
			}
		}
		return exp;
	}

	@Override
	public CollectionLiteralPart visitCollectionLiteralPart(
			QvtOParser.CollectionLiteralPartContext ctx) {
		if (ctx.expression().size() == 2) {
			CollectionRange range = OCL.createCollectionRange();
			range.setOwnedFirst((OclExpression) visit(ctx.expression(0)));
			range.setOwnedLast((OclExpression) visit(ctx.expression(1)));
			return range;
		}
		CollectionItem item = OCL.createCollectionItem();
		item.setOwnedItem((OclExpression) visit(ctx.expression(0)));
		return item;
	}

	// ==================== Tuple Literals ====================

	@Override
	public TupleLiteralExp visitTupleLit(QvtOParser.TupleLitContext ctx) {
		return (TupleLiteralExp) visit(ctx.tupleLiteral());
	}

	@Override
	public TupleLiteralExp visitTupleLiteral(QvtOParser.TupleLiteralContext ctx) {
		TupleLiteralExp exp = OCL.createTupleLiteralExp();
		for (QvtOParser.TupleLiteralPartContext partCtx : ctx.tupleLiteralPart()) {
			TupleLiteralPart part = OCL.createTupleLiteralPart();
			part.setName(qvtoIdentifierText(partCtx.qvtoIdentifier()));
			if (partCtx.typeExpression() != null) {
				part.setType(resolveTypeExpression(partCtx.typeExpression()));
			}
			part.setOwnedInit((OclExpression) visit(partCtx.expression()));
			exp.getOwnedParts().add(part);
		}
		return exp;
	}

	// ==================== Map Literals ====================

	@Override
	public MapLiteralExp visitMapLit(QvtOParser.MapLitContext ctx) {
		return (MapLiteralExp) visit(ctx.mapLiteral());
	}

	@Override
	public MapLiteralExp visitMapLiteral(QvtOParser.MapLiteralContext ctx) {
		MapLiteralExp exp = OCL.createMapLiteralExp();
		if (ctx.mapLiteralPart() != null) {
			for (QvtOParser.MapLiteralPartContext partCtx : ctx.mapLiteralPart()) {
				MapLiteralPart part = OCL.createMapLiteralPart();
				part.setOwnedKey((OclExpression) visit(partCtx.expression(0)));
				part.setOwnedValue((OclExpression) visit(partCtx.expression(1)));
				exp.getOwnedParts().add(part);
			}
		}
		return exp;
	}

	// §8.2.2.27: Dict literal — desugars to MapLiteralExp (D26)
	@Override
	public MapLiteralExp visitDictLit(QvtOParser.DictLitContext ctx) {
		return visitDictLiteral(ctx.dictLiteral());
	}

	public MapLiteralExp visitDictLiteral(QvtOParser.DictLiteralContext ctx) {
		MapLiteralExp exp = OCL.createMapLiteralExp();
		if (ctx.dictLiteralPart() != null) {
			for (QvtOParser.DictLiteralPartContext partCtx : ctx.dictLiteralPart()) {
				MapLiteralPart part = OCL.createMapLiteralPart();
				part.setOwnedKey((OclExpression) visit(partCtx.key));
				part.setOwnedValue((OclExpression) visit(partCtx.value));
				exp.getOwnedParts().add(part);
			}
		}
		return exp;
	}

	// ==================== Unary Expressions ====================

	@Override
	public OperationCallExp visitNotExp(QvtOParser.NotExpContext ctx) {
		return createUnaryOperation("not", (OclExpression) visit(ctx.expression()));
	}

	@Override
	public OperationCallExp visitUnaryMinusExp(QvtOParser.UnaryMinusExpContext ctx) {
		return createUnaryOperation("-", (OclExpression) visit(ctx.expression()));
	}

	// §8.4.4 item 1: #Type → oclIsKindOf(Type) shorthand
	// The operand (type) becomes the argument; source is implicit (iterator variable / self).
	@Override
	public OperationCallExp visitHashExp(QvtOParser.HashExpContext ctx) {
		OclExpression typeArg = buildTypeArgument(ctx.expression());
		return createShorthandTypeCheck("oclIsKindOf", typeArg);
	}

	// §8.4.4 item 2: ##Type → oclIsTypeOf(Type) shorthand
	@Override
	public OperationCallExp visitDoubleHashExp(QvtOParser.DoubleHashExpContext ctx) {
		OclExpression typeArg = buildTypeArgument(ctx.expression());
		return createShorthandTypeCheck("oclIsTypeOf", typeArg);
	}

	// §8.4.4 item 3: *"stereotype" → stereotypedBy("stereotype") shorthand (UML-specific)
	@Override
	public OperationCallExp visitUnaryStarExp(QvtOParser.UnaryStarExpContext ctx) {
		OclExpression operand = (OclExpression) visit(ctx.expression());
		return createShorthandTypeCheck("stereotypedBy", operand);
	}

	/**
	 * Creates an OperationCallExp with no explicit source and the operand as argument.
	 * Used for §8.4.4 shorthand operators (#, ##, *) where the source is implicit
	 * (iterator variable in select/reject context, or self).
	 */
	private OperationCallExp createShorthandTypeCheck(String opName, OclExpression argument) {
		OperationCallExp exp = OCL.createOperationCallExp();
		exp.setName(opName);
		exp.getOwnedArguments().add(argument);
		// No explicit source — evaluator will use iterator variable or self
		return exp;
	}

	/**
	 * Resolves the expression after # or ## as a type reference.
	 * If the expression is a path name that resolves to a known EClassifier, returns a TypeExp.
	 * Otherwise falls back to visiting the expression normally.
	 */
	private OclExpression buildTypeArgument(QvtOParser.ExpressionContext exprCtx) {
		TypeExp typeExp = tryBuildTypeCondition(exprCtx);
		if (typeExp != null) {
			return typeExp;
		}
		return (OclExpression) visit(exprCtx);
	}

	// ==================== Binary Expressions ====================

	@Override
	public OperationCallExp visitMultExp(QvtOParser.MultExpContext ctx) {
		return createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitAddExp(QvtOParser.AddExpContext ctx) {
		return createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitCompareExp(QvtOParser.CompareExpContext ctx) {
		return createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitEqualityExp(QvtOParser.EqualityExpContext ctx) {
		// §8.4.4: '!=' is a synonym for '<>' — normalize for OCL engine
		String op = "!=".equals(ctx.op.getText()) ? "<>" : ctx.op.getText();
		return createBinaryOperation(op,
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitAndExp(QvtOParser.AndExpContext ctx) {
		return createBinaryOperation("and",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitOrExp(QvtOParser.OrExpContext ctx) {
		return createBinaryOperation("or",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitXorExp(QvtOParser.XorExpContext ctx) {
		return createBinaryOperation("xor",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitImpliesExp(QvtOParser.ImpliesExpContext ctx) {
		return createBinaryOperation("implies",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	// §8.2.2.4: AssignExp — assignment as expression (e.g., inside if-then-endif)
	@Override
	public OclExpression visitAssignExp(QvtOParser.AssignExpContext ctx) {
		OclExpression left = (OclExpression) visit(ctx.expression(0));
		OclExpression right = (OclExpression) visit(ctx.expression(1));

		// var x := expr → combine into VariableInitExp with init
		if (left instanceof VariableInitExp varInit) {
			varInit.getReferredVariable().setOwnedInit(right);
			return varInit;
		}

		// §8.2.2.11: optional 'default' clause
		OclExpression defaultValue = ctx.defaultValue != null
				? (OclExpression) visit(ctx.defaultValue) : null;
		return buildAssignment(left, right, ctx.assignOp().getText(), defaultValue);
	}

	// ==================== Navigation ====================

	@Override
	public OclExpression visitNavigationExp(QvtOParser.NavigationExpContext ctx) {
		OclExpression source = (OclExpression) visit(ctx.expression());
		boolean isSafe = ctx.getChild(1).getText().equals("?.");

		QvtOParser.PropertyOrCallSuffixContext suffix = ctx.propertyOrCallSuffix();
		if (suffix instanceof QvtOParser.MappingCallSuffixContext mapCtx) {
			return createDotMappingCall(source, mapCtx, isSafe);
		} else if (suffix instanceof QvtOParser.ResolveCallSuffixContext resCtx) {
			return createDotResolveCall(source, resCtx);
		} else if (suffix instanceof QvtOParser.ResolveInCallSuffixContext resInCtx) {
			return createDotResolveInCall(source, resInCtx);
		} else if (suffix instanceof QvtOParser.PropertySuffixContext propCtx) {
			// §8.1.18: this.propName → desugar to variable access for module properties
			if (source instanceof VariableExp ve && "this".equals(ve.getReferredVariable().getName())) {
				String propName = qvtoIdentifierText(propCtx.qvtoIdentifier());
				Variable propVar = OCL.createVariable();
				propVar.setName(propName);
				return createVariableExp(propVar);
			}
			return createPropertyCall(source, propCtx, isSafe);
		} else if (suffix instanceof QvtOParser.DotCallSuffixContext callCtx) {
			return createDotOperationCall(source, callCtx, isSafe);
		}
		return source;
	}

	@Override
	public OclExpression visitArrowExp(QvtOParser.ArrowExpContext ctx) {
		OclExpression source = (OclExpression) visit(ctx.expression());
		String arrowText = ctx.getChild(1).getText();
		boolean isSafe = "?->".equals(arrowText);
		boolean isNot = "!->".equals(arrowText);

		QvtOParser.IteratorOrOperationCallContext call = ctx.iteratorOrOperationCall();
		OclExpression result;
		if (call instanceof QvtOParser.ForEachCallContext forCtx) {
			result = createForEachArrowCall(source, forCtx);
		} else if (call instanceof QvtOParser.IteratorCallContext iterCtx) {
			result = createIteratorExp(source, iterCtx, isSafe);
		} else if (call instanceof QvtOParser.IterateCallContext iterateCtx) {
			result = createIterateExp(source, iterateCtx, isSafe);
		} else if (call instanceof QvtOParser.CollectionOperationCallContext opCtx) {
			result = createCollectionOperation(source, opCtx, isSafe);
		} else if (call instanceof QvtOParser.ArrowResolveCallContext resCtx) {
			result = createArrowResolveCall(source, resCtx);
		} else if (call instanceof QvtOParser.ArrowResolveInCallContext resInCtx) {
			result = createArrowResolveInCall(source, resInCtx);
		} else if (call instanceof QvtOParser.ArrowPropertyCallContext propCtx) {
			// §8.2.2.7: list->prop = list->xcollect(i | i.prop) — collect shorthand
			result = createXcollectPropertyCall(source, propCtx);
		} else if (call instanceof QvtOParser.ArrowMappingCallContext mapCtx) {
			// §8.2.2.7: list->map f() — mapping call on collection
			result = createArrowMappingCallOnCollection(source, mapCtx);
		} else if (call instanceof QvtOParser.ArrowObjectCallContext objCtx) {
			// §8.4.7: coll->object(x:T) Type { ... } = coll->collect(x | object Type { ... })
			result = createArrowObjectCall(source, objCtx);
		} else if (call instanceof QvtOParser.ArrowSwitchCallContext switchCtx) {
			// §8.2.2.8: coll->switch(i) { ... } = coll->xcollect(i | switch { ... })
			result = createArrowSwitchCall(source, switchCtx);
		} else {
			result = source;
		}

		// §8.4.4: !-> negates the result of the arrow operation
		if (isNot) {
			return createUnaryOperation("not", result);
		}
		return result;
	}

	// ==================== Xselect [Type] (§8.2.2.7) ====================

	@Override
	public OclExpression visitXselectExp(QvtOParser.XselectExpContext ctx) {
		OclExpression source = (OclExpression) visit(ctx.expression(0));

		// §8.2.2.7: If condition is a TypeExp, reinterpret as oclIsKindOf(Type)
		QvtOParser.ExpressionContext conditionCtx = ctx.xselectCondition;
		TypeExp typeCondition = tryBuildTypeCondition(conditionCtx);

		// Determine iterator variable name
		String iterName = ctx.xselectIter != null
				? qvtoIdentifierText(ctx.xselectIter)
				: "_xsel_it";

		IteratorExp selectExp = OCL.createIteratorExp();
		selectExp.setOwnedSource(source);
		selectExp.setName("select");

		Variable iterVar = OCL.createVariable();
		iterVar.setName(iterName);
		OclType elementType = inferElementType(source);
		if (elementType != null) {
			iterVar.setType(elementType);
		}
		selectExp.getOwnedIterators().add(iterVar);

		// Register iterator in environment for condition evaluation
		QvtoEnvironment savedEnv = this.environment;
		this.environment = this.environment.nested(iterVar);

		if (typeCondition != null) {
			OperationCallExp oclIsKindOf = OCL.createOperationCallExp();
			oclIsKindOf.setName("oclIsKindOf");
			VariableExp iterRef = OCL.createVariableExp();
			iterRef.setReferredVariable(iterVar);
			oclIsKindOf.setOwnedSource(iterRef);
			oclIsKindOf.getOwnedArguments().add(typeCondition);
			selectExp.setOwnedBody(oclIsKindOf);
		} else {
			OclExpression condition = (OclExpression) visit(conditionCtx);
			selectExp.setOwnedBody(condition);
		}

		this.environment = savedEnv;
		return selectExp;
	}

	@Override
	public OclExpression visitXselectOneExp(QvtOParser.XselectOneExpContext ctx) {
		// §8.2.2.7: list![condition] = xselectOne — returns first matching element
		OclExpression source = (OclExpression) visit(ctx.expression(0));

		String iterName = ctx.xselectOneIter != null
				? qvtoIdentifierText(ctx.xselectOneIter)
				: "_xsel1_it";

		// Build as: source->any(iter | condition)
		IteratorExp anyExp = OCL.createIteratorExp();
		anyExp.setOwnedSource(source);
		anyExp.setName("any");

		Variable iterVar = OCL.createVariable();
		iterVar.setName(iterName);
		OclType elementType = inferElementType(source);
		if (elementType != null) {
			iterVar.setType(elementType);
		}
		anyExp.getOwnedIterators().add(iterVar);

		QvtoEnvironment savedEnv = this.environment;
		this.environment = this.environment.nested(iterVar);

		OclExpression condition = (OclExpression) visit(ctx.xselectOneCondition);

		this.environment = savedEnv;

		anyExp.setOwnedBody(condition);
		return anyExp;
	}

	/**
	 * Try to interpret an expression context as a type reference for xselect.
	 * Returns a TypeExp if the expression is a simple path name that resolves to a type,
	 * null otherwise.
	 */
	private TypeExp tryBuildTypeCondition(QvtOParser.ExpressionContext exprCtx) {
		// Unwrap: expression → primaryExpression → pathNameExp
		if (exprCtx instanceof QvtOParser.PrimaryExpContext primaryCtx) {
			if (primaryCtx.primaryExpression() instanceof QvtOParser.PathNameExpContext pathCtx) {
				List<String> segments = pathNameSegments(pathCtx.pathName());
				EClassifier classifier = resolveClassifier(segments);
				if (classifier != null) {
					TypeExp typeExp = OCL.createTypeExp();
					typeExp.setReferredType(createClassifierType(classifier));
					return typeExp;
				}
			}
		}
		return null;
	}

	// ==================== If / Let ====================

	// §8.2.2.8: imperative if — else is optional (extends OCL where else is mandatory)
	@Override
	public IfExp visitIfExp(QvtOParser.IfExpContext ctx) {
		IfExp ifExp = OCL.createIfExp();
		ifExp.setOwnedCondition((OclExpression) visit(ctx.condition));
		ifExp.setOwnedThen((OclExpression) visit(ctx.thenExp));

		if (ctx.elseIfCondition != null && !ctx.elseIfCondition.isEmpty()) {
			IfExp current = ifExp;
			for (int i = 0; i < ctx.elseIfCondition.size(); i++) {
				IfExp nested = OCL.createIfExp();
				nested.setOwnedCondition((OclExpression) visit(ctx.elseIfCondition.get(i)));
				nested.setOwnedThen((OclExpression) visit(ctx.elseIfExp.get(i)));
				current.setOwnedElse(nested);
				current = nested;
			}
			if (ctx.elseExp != null) {
				current.setOwnedElse((OclExpression) visit(ctx.elseExp));
			}
		} else if (ctx.elseExp != null) {
			ifExp.setOwnedElse((OclExpression) visit(ctx.elseExp));
		}
		// When else is absent, ownedElse stays null — engine returns null for false condition
		return ifExp;
	}

	@Override
	public IfExp visitImperativeIfExp(QvtOParser.ImperativeIfExpContext ctx) {
		IfExp ifExp = OCL.createIfExp();
		ifExp.setOwnedCondition((OclExpression) visit(ctx.impCondition));
		ifExp.setOwnedThen(buildBlock(ctx.thenBlock));

		if (ctx.elifCondition != null && !ctx.elifCondition.isEmpty()) {
			IfExp current = ifExp;
			for (int i = 0; i < ctx.elifCondition.size(); i++) {
				IfExp nested = OCL.createIfExp();
				nested.setOwnedCondition((OclExpression) visit(ctx.elifCondition.get(i)));
				nested.setOwnedThen(buildBlock(ctx.elifBlock.get(i)));
				current.setOwnedElse(nested);
				current = nested;
			}
			if (ctx.elseBlock != null) {
				current.setOwnedElse(buildBlock(ctx.elseBlock));
			}
		} else if (ctx.elseBlock != null) {
			ifExp.setOwnedElse(buildBlock(ctx.elseBlock));
		}
		return ifExp;
	}

	@Override
	public LetExp visitLetExp(QvtOParser.LetExpContext ctx) {
		List<QvtOParser.LetBindingContext> bindings = ctx.letBinding();
		QvtoEnvironment savedEnv = this.environment;

		LetExp outermost = null;
		LetExp current = null;

		for (QvtOParser.LetBindingContext bindCtx : bindings) {
			Variable var = OCL.createVariable();
			var.setName(qvtoIdentifierText(bindCtx.qvtoIdentifier()));
			if (bindCtx.typeExpression() != null) {
				var.setType(resolveTypeExpression(bindCtx.typeExpression()));
			}
			var.setOwnedInit((OclExpression) visit(bindCtx.expression()));

			this.environment = this.environment.nested(var);

			LetExp letExp = OCL.createLetExp();
			letExp.setOwnedVariable(var);

			if (outermost == null) {
				outermost = letExp;
			} else {
				current.setOwnedIn(letExp);
			}
			current = letExp;
		}

		current.setOwnedIn((OclExpression) visit(ctx.expression()));
		this.environment = savedEnv;
		return outermost;
	}

	// ==================== Primary Expression Delegates ====================

	@Override
	public OclExpression visitSelfExp(QvtOParser.SelfExpContext ctx) {
		return environment.lookup("self")
				.<OclExpression>map(this::createVariableExp)
				.orElseGet(() -> {
					Variable selfVar = OCL.createVariable();
					selfVar.setName("self");
					return createVariableExp(selfVar);
				});
	}

	@Override
	public OclExpression visitThisExp(QvtOParser.ThisExpContext ctx) {
		return environment.lookup("this")
				.<OclExpression>map(this::createVariableExp)
				.orElseGet(() -> {
					Variable thisVar = OCL.createVariable();
					thisVar.setName("this");
					return createVariableExp(thisVar);
				});
	}

	@Override
	public OclExpression visitParenExp(QvtOParser.ParenExpContext ctx) {
		return (OclExpression) visit(ctx.expression());
	}

	@Override
	public OclExpression visitPrimaryExp(QvtOParser.PrimaryExpContext ctx) {
		return (OclExpression) visit(ctx.primaryExpression());
	}

	@Override
	public OclExpression visitLiteralExp(QvtOParser.LiteralExpContext ctx) {
		return (OclExpression) visit(ctx.literalExpression());
	}

	@Override
	public TypeExp visitPrimitiveTypeExp(QvtOParser.PrimitiveTypeExpContext ctx) {
		TypeExp exp = OCL.createTypeExp();
		exp.setReferredType(createPrimitiveType(ctx.primitiveType().getText()));
		return exp;
	}

	@Override
	public TypeExp visitCollectionTypeExp(QvtOParser.CollectionTypeExpContext ctx) {
		TypeExp exp = OCL.createTypeExp();
		exp.setReferredType(resolveCollectionType(ctx.collectionType()));
		return exp;
	}

	@Override
	public TypeExp visitMapTypeExp(QvtOParser.MapTypeExpContext ctx) {
		TypeExp exp = OCL.createTypeExp();
		exp.setReferredType(resolveMapType(ctx.mapType()));
		return exp;
	}

	@Override
	public TypeExp visitTupleTypeExp(QvtOParser.TupleTypeExpContext ctx) {
		TypeExp exp = OCL.createTypeExp();
		exp.setReferredType(resolveTupleType(ctx.tupleType()));
		return exp;
	}

	@Override
	public OperationCallExp visitOperationCallExp(QvtOParser.OperationCallExpContext ctx) {
		List<String> segments = pathNameSegments(ctx.pathName());
		String opName = segments.get(segments.size() - 1);

		OperationCallExp exp = OCL.createOperationCallExp();
		exp.setIsImplicit(true);

		// Implicit source: self (if available)
		environment.lookup("self").ifPresent(selfVar -> {
			VariableExp selfRef = createVariableExp(selfVar);
			exp.setOwnedSource(selfRef);
		});

		if (ctx.argumentList() != null) {
			for (QvtOParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				exp.getOwnedArguments().add((OclExpression) visit(argCtx));
			}
		}

		resolveOperation(exp, opName);
		return exp;
	}

	// ==================== QVT-O Primary Delegates ====================

	@Override
	public OclExpression visitBlockPrimary(QvtOParser.BlockPrimaryContext ctx) {
		return (OclExpression) visit(ctx.blockExp());
	}

	@Override
	public OclExpression visitWhilePrimary(QvtOParser.WhilePrimaryContext ctx) {
		return (OclExpression) visit(ctx.whileExp());
	}

	@Override
	public OclExpression visitForPrimary(QvtOParser.ForPrimaryContext ctx) {
		return (OclExpression) visit(ctx.forExp());
	}

	@Override
	public OclExpression visitSwitchPrimary(QvtOParser.SwitchPrimaryContext ctx) {
		return (OclExpression) visit(ctx.switchExp());
	}

	@Override
	public OclExpression visitComputePrimary(QvtOParser.ComputePrimaryContext ctx) {
		return (OclExpression) visit(ctx.computeExp());
	}

	@Override
	public OclExpression visitObjectPrimary(QvtOParser.ObjectPrimaryContext ctx) {
		return (OclExpression) visit(ctx.objectExp());
	}

	@Override
	public OclExpression visitNewPrimary(QvtOParser.NewPrimaryContext ctx) {
		return (OclExpression) visit(ctx.newExp());
	}

	@Override
	public OclExpression visitMappingCallPrimary(QvtOParser.MappingCallPrimaryContext ctx) {
		return (OclExpression) visit(ctx.mappingCallExp());
	}

	@Override
	public OclExpression visitResolvePrimary(QvtOParser.ResolvePrimaryContext ctx) {
		return (OclExpression) visit(ctx.resolveExp());
	}

	@Override
	public OclExpression visitResolveInPrimary(QvtOParser.ResolveInPrimaryContext ctx) {
		return (OclExpression) visit(ctx.resolveInExp());
	}

	@Override
	public OclExpression visitTryPrimary(QvtOParser.TryPrimaryContext ctx) {
		return (OclExpression) visit(ctx.tryExp());
	}

	@Override
	public OclExpression visitRaisePrimary(QvtOParser.RaisePrimaryContext ctx) {
		return (OclExpression) visit(ctx.raiseExp());
	}

	@Override
	public OclExpression visitAssertPrimary(QvtOParser.AssertPrimaryContext ctx) {
		return (OclExpression) visit(ctx.assertExp());
	}

	@Override
	public OclExpression visitLogPrimary(QvtOParser.LogPrimaryContext ctx) {
		return (OclExpression) visit(ctx.logExp());
	}

	@Override
	public OclExpression visitReturnPrimary(QvtOParser.ReturnPrimaryContext ctx) {
		return (OclExpression) visit(ctx.returnExp());
	}

	@Override
	public OclExpression visitBreakPrimary(QvtOParser.BreakPrimaryContext ctx) {
		return IMP.createBreakExp();
	}

	@Override
	public OclExpression visitContinuePrimary(QvtOParser.ContinuePrimaryContext ctx) {
		return IMP.createContinueExp();
	}

	@Override
	public OclExpression visitVarDeclPrimary(QvtOParser.VarDeclPrimaryContext ctx) {
		return (OclExpression) visit(ctx.varDeclExp());
	}

	// ==================== Imperative Expressions ====================

	@Override
	public BlockExp visitBlockExp(QvtOParser.BlockExpContext ctx) {
		BlockExp block = IMP.createBlockExp();
		for (QvtOParser.StatementContext stmtCtx : ctx.statement()) {
			OclExpression stmtExpr = buildStatement(stmtCtx);
			if (stmtExpr != null) {
				block.getBody().add(stmtExpr);
			}
		}
		return block;
	}

	@Override
	public WhileExp visitWhileBasic(QvtOParser.WhileBasicContext ctx) {
		WhileExp whileExp = IMP.createWhileExp();
		whileExp.setCondition((OclExpression) visit(ctx.expression()));
		whileExp.setBody(buildBlock(ctx.block()));
		return whileExp;
	}

	@Override
	public ComputeExp visitWhileWithInit(QvtOParser.WhileWithInitContext ctx) {
		// §8.2.2.4: while (x:Type := init; condition) { body }
		// Desugars to: compute (x:Type = init) { while (condition) { body } }
		QvtoEnvironment savedEnv = this.environment;

		Variable returnedElement = OCL.createVariable();
		returnedElement.setName(qvtoIdentifierText(ctx.varName));
		returnedElement.setType(resolveTypeExpression(ctx.type));
		returnedElement.setOwnedInit((OclExpression) visit(ctx.initValue));
		this.environment = this.environment.nested(returnedElement);

		WhileExp whileExp = IMP.createWhileExp();
		whileExp.setCondition((OclExpression) visit(ctx.condition));
		whileExp.setBody(buildBlock(ctx.block()));

		ComputeExp computeExp = IMP.createComputeExp();
		computeExp.setReturnedElement(returnedElement);
		computeExp.setBody(whileExp);

		this.environment = savedEnv;
		return computeExp;
	}

	/**
	 * §8.2.2.6: ForExp as arrow call — coll->forEach(x) { ... }
	 * Sets the source of the ForExp to the collection expression.
	 * If compute shorthand is used (forEach(i;x:X=init|cond)), wraps in ComputeExp.
	 */
	private OclExpression createForEachArrowCall(OclExpression source,
			QvtOParser.ForEachCallContext ctx) {
		ForExp forExp = IMP.createForExp();
		forExp.setName(ctx.forKind().getText());
		forExp.setOwnedSource(source);

		QvtoEnvironment savedEnv = this.environment;

		QvtOParser.ForVarListContext varList = ctx.forVarList();
		QvtOParser.IteratorVariablesContext iterVars = varList.iteratorVariables();

		for (int i = 0; i < iterVars.qvtoIdentifier().size(); i++) {
			Variable iterVar = OCL.createVariable();
			iterVar.setName(qvtoIdentifierText(iterVars.qvtoIdentifier(i)));
			if (i < iterVars.typeExpression().size() && iterVars.typeExpression(i) != null) {
				iterVar.setType(resolveTypeExpression(iterVars.typeExpression(i)));
			}
			forExp.getOwnedIterators().add(iterVar);
			this.environment = this.environment.nested(iterVar);
		}

		if (ctx.expression() != null) {
			forExp.setCondition((OclExpression) visit(ctx.expression()));
		}

		forExp.setOwnedBody(buildBlock(ctx.block()));

		this.environment = savedEnv;

		// §8.2.2.6: compute shorthand — forEach(i;x:X=init|cond) ≡ compute(x:X=init) forEach(i|cond)
		return wrapInComputeIfShorthand(forExp, varList, savedEnv);
	}

	@Override
	public OclExpression visitForExp(QvtOParser.ForExpContext ctx) {
		ForExp forExp = IMP.createForExp();
		forExp.setName(ctx.forKind().getText());

		QvtoEnvironment savedEnv = this.environment;

		QvtOParser.ForVarListContext varList = ctx.forVarList();
		QvtOParser.IteratorVariablesContext iterVars = varList.iteratorVariables();

		for (int i = 0; i < iterVars.qvtoIdentifier().size(); i++) {
			Variable iterVar = OCL.createVariable();
			iterVar.setName(qvtoIdentifierText(iterVars.qvtoIdentifier(i)));
			if (i < iterVars.typeExpression().size() && iterVars.typeExpression(i) != null) {
				iterVar.setType(resolveTypeExpression(iterVars.typeExpression(i)));
			}
			forExp.getOwnedIterators().add(iterVar);
			this.environment = this.environment.nested(iterVar);
		}

		// Condition after '|' if present (in forExp rule, not forVarList)
		if (ctx.expression() != null) {
			forExp.setCondition((OclExpression) visit(ctx.expression()));
		}

		forExp.setOwnedBody(buildBlock(ctx.block()));

		this.environment = savedEnv;

		// §8.2.2.6: compute shorthand — forEach(i;x:X=init|cond) ≡ compute(x:X=init) forEach(i|cond)
		return wrapInComputeIfShorthand(forExp, varList, savedEnv);
	}

	/**
	 * §8.2.2.6: If forVarList has a compute shorthand (name:Type=init after ';'),
	 * wraps the ForExp in a ComputeExp. Otherwise returns the ForExp unchanged.
	 * Detects compute shorthand by the presence of qvtoIdentifier (the variable name).
	 */
	private OclExpression wrapInComputeIfShorthand(ForExp forExp,
			QvtOParser.ForVarListContext varList, QvtoEnvironment savedEnv) {
		// Compute shorthand: forVarList matched alternative 1 (i;x:Type=init)
		// Detected by qvtoIdentifier being present (the compute variable name)
		if (varList.qvtoIdentifier() == null) {
			return forExp;
		}
		ComputeExp computeExp = IMP.createComputeExp();
		Variable computeVar = OCL.createVariable();
		computeVar.setName(qvtoIdentifierText(varList.qvtoIdentifier()));
		if (varList.typeExpression() != null) {
			computeVar.setType(resolveTypeExpression(varList.typeExpression()));
		}
		if (varList.expression() != null) {
			computeVar.setOwnedInit((OclExpression) visit(varList.expression()));
		}
		computeExp.setReturnedElement(computeVar);
		computeExp.setBody(forExp);
		return computeExp;
	}

	@Override
	public OclExpression visitSwitchExp(QvtOParser.SwitchExpContext ctx) {
		// §8.4.7: switch ('(' <iter_declarator> ')')? <switch_body>
		QvtOParser.SwitchIteratorContext iterCtx = ctx.switchIterator();

		QvtoEnvironment savedEnv = null;
		VariableInitExp varInitExp = null;

		if (iterCtx != null) {
			// Standalone switch with iterator: switch (x := expr) { ... }
			// Bind iterator variable in scope so case conditions can reference it.
			String varName = qvtoIdentifierText(iterCtx.qvtoIdentifier());
			Variable iterVar = OCL.createVariable();
			iterVar.setName(varName);
			if (iterCtx.typeExpression() != null) {
				OclType varType = resolveTypeExpression(iterCtx.typeExpression());
				if (varType != null) {
					iterVar.setType(varType);
				}
			}

			if (iterCtx.expression() != null) {
				// Has init expression: wrap in BlockExp { var x := expr; switch { ... } }
				Variable initVar = OCL.createVariable();
				initVar.setName(varName);
				if (iterVar.getType() != null) {
					initVar.setType(iterVar.getType());
				}
				initVar.setOwnedInit((OclExpression) visit(iterCtx.expression()));
				varInitExp = IMP.createVariableInitExp();
				varInitExp.setReferredVariable(initVar);
				varInitExp.setWithResult(true); // ::= semantics — block returns switch result, not void
			}

			savedEnv = this.environment;
			this.environment = this.environment.nested(iterVar);
		}

		SwitchExp switchExp = IMP.createSwitchExp();

		for (QvtOParser.SwitchAltContext altCtx : ctx.switchAlt()) {
			AltExp alt = IMP.createAltExp();
			alt.setCondition((OclExpression) visit(altCtx.expression(0)));
			alt.setBody((OclExpression) visit(altCtx.expression(1)));
			switchExp.getAlternativePart().add(alt);
		}

		// else part
		if (ctx.expression() != null) {
			switchExp.setElsePart((OclExpression) visit(ctx.expression()));
		}

		if (savedEnv != null) {
			this.environment = savedEnv;
		}

		// If there's an iterator with init, wrap: { var x := expr; switch { ... } }
		if (varInitExp != null) {
			BlockExp block = IMP.createBlockExp();
			block.getBody().add(varInitExp);
			block.getBody().add(switchExp);
			return block;
		}

		return switchExp;
	}

	@Override
	public ComputeExp visitComputeExp(QvtOParser.ComputeExpContext ctx) {
		ComputeExp computeExp = IMP.createComputeExp();

		QvtoEnvironment savedEnv = this.environment;

		Variable returnedElement = buildVarFromDeclarator(ctx.varDeclarator());
		computeExp.setReturnedElement(returnedElement);
		this.environment = this.environment.nested(returnedElement);

		computeExp.setBody(buildBlock(ctx.block()));

		this.environment = savedEnv;
		return computeExp;
	}

	@Override
	public ObjectExp visitObjectExp(QvtOParser.ObjectExpContext ctx) {
		ObjectExp objectExp = QVTO.createObjectExp();

		// optional variable name: object (varName :) Type { ... }
		Variable refVar = OCL.createVariable();
		if (ctx.varName != null) {
			refVar.setName(qvtoIdentifierText(ctx.varName));
		} else {
			refVar.setName("result");
		}
		objectExp.setReferredObject(refVar);

		// Type → instantiatedClass (if resolvable as EClass)
		if (ctx.typeExpression() != null) {
			OclType type = resolveTypeExpression(ctx.typeExpression());
			if (type instanceof ClassifierType ct && ct.getReferredClassifier() instanceof EClass ec) {
				objectExp.setInstantiatedClass(ec);
				objectExp.setType(type);
				refVar.setType(type);
			}
		}

		// §8.1.6: @extentName → explicit extent routing
		if (ctx.extentName != null) {
			Variable extentVar = OCL.createVariable();
			extentVar.setName(qvtoIdentifierText(ctx.extentName));
			objectExp.setExtent(extentVar);
		}

		QvtoEnvironment savedEnv = this.environment;
		this.environment = this.environment.nested(refVar);

		// Body as ConstructorBody
		var body = QVTO.createConstructorBody();
		for (QvtOParser.StatementContext stmtCtx : ctx.block().statement()) {
			OclExpression stmtExpr = buildStatement(stmtCtx);
			if (stmtExpr != null) {
				body.getContent().add(stmtExpr);
			}
		}
		objectExp.setBody(body);

		this.environment = savedEnv;
		return objectExp;
	}

	@Override
	public InstantiationExp visitNewExp(QvtOParser.NewExpContext ctx) {
		List<String> segments = pathNameSegments(ctx.pathName());
		String typeName = String.join("::", segments);

		// §8.1.13: Check if the type name refers to an imported transformation
		Module importedStub = importedModuleStubs.get(typeName);
		if (importedStub != null) {
			TransformationInstantiationExp exp = IMP.createTransformationInstantiationExp();
			// Set the stub as instantiated class (linker will resolve the real module)
			if (importedStub instanceof OperationalTransformation ot) {
				exp.setImportedTransformation(ot);
			}
			// instantiatedClass is set to a placeholder EClass (the stub's module class)
			// The linker will replace the stub, and the evaluator will use importedTransformation
			if (ctx.argumentList() != null) {
				for (QvtOParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
					exp.getArgument().add((OclExpression) visit(argCtx));
				}
			}
			return exp;
		}

		// Regular class instantiation
		InstantiationExp exp = IMP.createInstantiationExp();
		EClassifier classifier = resolveClassifier(segments);
		if (classifier instanceof EClass ec) {
			exp.setInstantiatedClass(ec);
		}

		// §8.1.3: @extentName → explicit extent routing
		if (ctx.extentName != null) {
			Variable extentVar = OCL.createVariable();
			extentVar.setName(qvtoIdentifierText(ctx.extentName));
			exp.setExtent(extentVar);
		}

		if (ctx.argumentList() != null) {
			for (QvtOParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				exp.getArgument().add((OclExpression) visit(argCtx));
			}
		}

		return exp;
	}

	@Override
	public MappingCallExp visitMappingCallExp(QvtOParser.MappingCallExpContext ctx) {
		MappingCallExp exp = QVTO.createMappingCallExp();
		exp.setIsStrict("xmap".equals(ctx.mappingCallKind().getText()));

		String name = scopedNameText(ctx.scopedName());
		exp.setName(name);

		if (ctx.argumentList() != null) {
			for (QvtOParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				exp.getOwnedArguments().add((OclExpression) visit(argCtx));
			}
		}

		return exp;
	}

	@Override
	public ResolveExp visitResolveExp(QvtOParser.ResolveExpContext ctx) {
		ResolveExp exp = QVTO.createResolveExp();

		String kind = ctx.resolveKind().getText();
		exp.setIsDeferred(ctx.getChild(0).getText().equals("late"));
		exp.setIsInverse(kind.startsWith("inv"));
		exp.setOne(kind.contains("one"));

		if (ctx.resolveArgs() != null) {
			buildResolveArgs(exp, ctx.resolveArgs());
		}

		return exp;
	}

	@Override
	public ResolveInExp visitResolveInExp(QvtOParser.ResolveInExpContext ctx) {
		ResolveInExp exp = QVTO.createResolveInExp();

		String kind = ctx.resolveInKind().getText();
		exp.setIsDeferred(ctx.getChild(0).getText().equals("late"));
		exp.setIsInverse(kind.startsWith("inv"));
		exp.setOne(kind.contains("one"));

		// §8.1.11.4: Resolve the scoped mapping name to a MappingOperation reference
		exp.setInMapping(resolveInMapping(ctx.scopedName()));

		if (ctx.resolveArgs() != null) {
			buildResolveArgs(exp, ctx.resolveArgs());
		}

		return exp;
	}

	@Override
	public TryExp visitTryExp(QvtOParser.TryExpContext ctx) {
		TryExp tryExp = IMP.createTryExp();

		// Try body
		for (QvtOParser.StatementContext stmtCtx : ctx.block().statement()) {
			OclExpression stmtExpr = buildStatement(stmtCtx);
			if (stmtExpr != null) {
				tryExp.getTryBody().add(stmtExpr);
			}
		}

		// Catch clauses
		for (QvtOParser.CatchClauseContext catchCtx : ctx.catchClause()) {
			CatchExp catchExp = IMP.createCatchExp();

			if (catchCtx.exceptionTypeList() != null) {
				for (QvtOParser.PathNameContext pathCtx : catchCtx.exceptionTypeList().pathName()) {
					List<String> segments = pathNameSegments(pathCtx);
					EClassifier exType = resolveClassifier(segments);
					if (exType != null) {
						catchExp.getException().add(exType);
					}
				}
			}

			for (QvtOParser.StatementContext stmtCtx : catchCtx.block().statement()) {
				OclExpression stmtExpr = buildStatement(stmtCtx);
				if (stmtExpr != null) {
					catchExp.getBody().add(stmtExpr);
				}
			}

			tryExp.getExceptClause().add(catchExp);
		}

		return tryExp;
	}

	@Override
	public RaiseExp visitRaiseExp(QvtOParser.RaiseExpContext ctx) {
		RaiseExp raiseExp = IMP.createRaiseExp();

		if (ctx.pathName() != null) {
			List<String> segments = pathNameSegments(ctx.pathName());
			EClassifier exType = resolveClassifier(segments);
			if (exType != null) {
				raiseExp.setException(exType);
			}
			if (ctx.expression() != null) {
				raiseExp.setArgument((OclExpression) visit(ctx.expression()));
			}
		} else if (ctx.STRING_LITERAL() != null) {
			// raise 'message' — create a StringLiteralExp as argument
			StringLiteralExp msgExp = OCL.createStringLiteralExp();
			String text = ctx.STRING_LITERAL().getText();
			msgExp.setStringSymbol(unescapeString(text.substring(1, text.length() - 1)));
			raiseExp.setArgument(msgExp);
		}

		return raiseExp;
	}

	@Override
	public AssertExp visitAssertExp(QvtOParser.AssertExpContext ctx) {
		AssertExp assertExp = IMP.createAssertExp();

		// Optional severity identifier
		if (ctx.qvtoIdentifier() != null) {
			String severity = qvtoIdentifierText(ctx.qvtoIdentifier());
			assertExp.setSeverity(switch (severity) {
				case "warning" -> SeverityKind.WARNING;
				case "fatal" -> SeverityKind.FATAL;
				default -> SeverityKind.ERROR;
			});
		}

		assertExp.setAssertion((OclExpression) visit(ctx.expression()));

		if (ctx.logCallExp() != null) {
			assertExp.setLog(buildLogExp(ctx.logCallExp()));
		}

		return assertExp;
	}

	@Override
	public LogExp visitLogExp(QvtOParser.LogExpContext ctx) {
		LogExp logExp = IMP.createLogExp();
		logExp.setName("log");

		if (ctx.argumentList() != null) {
			for (QvtOParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				logExp.getOwnedArguments().add((OclExpression) visit(argCtx));
			}
		}

		if (ctx.expression() != null) {
			logExp.setCondition((OclExpression) visit(ctx.expression()));
		}

		return logExp;
	}

	@Override
	public ReturnExp visitReturnExp(QvtOParser.ReturnExpContext ctx) {
		ReturnExp returnExp = IMP.createReturnExp();
		if (ctx.expression() != null) {
			returnExp.setValue((OclExpression) visit(ctx.expression()));
		}
		return returnExp;
	}

	@Override
	public OclExpression visitVarDeclExp(QvtOParser.VarDeclExpContext ctx) {
		List<QvtOParser.VarDeclaratorContext> declarators = ctx.varDeclarator();
		if (declarators.size() == 1) {
			return buildVariableInitExp(declarators.get(0));
		}
		// §8.2.2.10: Multiple variable declarations grouped with unique var keyword
		BlockExp block = IMP.createBlockExp();
		for (QvtOParser.VarDeclaratorContext decl : declarators) {
			block.getBody().add(buildVariableInitExp(decl));
		}
		return block;
	}

	// ==================== Statement Building ====================

	/**
	 * Builds an expression from a statement context (assignment or expression statement).
	 * <p>
	 * When the grammar parses {@code var x := expr;} as an AssignStatement
	 * (because {@code :=} and {@code ::=} are assignOp tokens), this method
	 * detects the VariableInitExp on the left side and combines it into a
	 * variable declaration with init rather than producing an AssignExp.
	 */
	OclExpression buildStatement(QvtOParser.StatementContext ctx) {
		if (ctx instanceof QvtOParser.VarDeclStatementContext varDeclCtx) {
			return visitVarDeclExp(varDeclCtx.varDeclExp());
		} else if (ctx instanceof QvtOParser.ExpressionStatementContext exprCtx) {
			return (OclExpression) visit(exprCtx.expression());
		}
		return null;
	}

	private AssignExp buildAssignment(OclExpression left, OclExpression right, String op,
			OclExpression defaultValue) {
		AssignExp assign = IMP.createAssignExp();
		assign.setLeft(left);
		assign.getValue().add(right);
		assign.setIsReset(":=".equals(op) || "::=".equals(op));
		assign.setIsSubtract("-=".equals(op));
		if (defaultValue != null) {
			assign.setDefaultValue(defaultValue);
		}
		return assign;
	}

	// ==================== Type Resolution ====================

	OclType resolveTypeExpression(QvtOParser.TypeExpressionContext ctx) {
		if (ctx.primitiveType() != null) {
			return createPrimitiveType(ctx.primitiveType().getText());
		}
		if (ctx.collectionType() != null) {
			return resolveCollectionType(ctx.collectionType());
		}
		if (ctx.mapType() != null) {
			return resolveMapType(ctx.mapType());
		}
		if (ctx.tupleType() != null) {
			return resolveTupleType(ctx.tupleType());
		}
		if (ctx.listType() != null) {
			return resolveListType(ctx.listType());
		}
		if (ctx.dictType() != null) {
			return resolveDictType(ctx.dictType());
		}
		if (ctx.pathName() != null) {
			return createClassifierType(resolveClassifier(pathNameSegments(ctx.pathName())));
		}
		return null;
	}

	private OclType createPrimitiveType(String name) {
		var type = OCL.createPrimitiveType();
		type.setName(name);
		return type;
	}

	ClassifierType createClassifierType(EClassifier classifier) {
		ClassifierType type = OCL.createClassifierType();
		type.setReferredClassifier(classifier);
		if (classifier != null) {
			type.setName(classifier.getName());
		}
		return type;
	}

	private OclType resolveCollectionType(QvtOParser.CollectionTypeContext ctx) {
		CollectionKind kind = resolveCollectionKind(ctx.collectionKind().getText());
		OclType elementType = resolveTypeExpression(ctx.typeExpression());
		CollectionType type = switch (kind) {
			case SET -> OCL.createSetType();
			case ORDERED_SET -> OCL.createOrderedSetType();
			case BAG -> OCL.createBagType();
			case SEQUENCE -> OCL.createSequenceType();
			default -> OCL.createCollectionType();
		};
		type.setKind(kind);
		type.setElementType(elementType);
		return type;
	}

	private OclType resolveMapType(QvtOParser.MapTypeContext ctx) {
		MapType type = OCL.createMapType();
		type.setKeyType(resolveTypeExpression(ctx.typeExpression(0)));
		type.setValueType(resolveTypeExpression(ctx.typeExpression(1)));
		return type;
	}

	private OclType resolveTupleType(QvtOParser.TupleTypeContext ctx) {
		var type = OCL.createTupleType();
		for (QvtOParser.TupleTypePartContext partCtx : ctx.tupleTypePart()) {
			var part = OCL.createTuplePart();
			part.setName(qvtoIdentifierText(partCtx.qvtoIdentifier()));
			part.setType(resolveTypeExpression(partCtx.typeExpression()));
			type.getOwnedParts().add(part);
		}
		return type;
	}

	/** D26: List(T) → SequenceType(T) */
	private OclType resolveListType(QvtOParser.ListTypeContext ctx) {
		SequenceType type = OCL.createSequenceType();
		type.setKind(CollectionKind.SEQUENCE);
		type.setElementType(resolveTypeExpression(ctx.typeExpression()));
		return type;
	}

	/** D26: Dict(K, V) → MapType(K, V) */
	private OclType resolveDictType(QvtOParser.DictTypeContext ctx) {
		MapType type = OCL.createMapType();
		type.setKeyType(resolveTypeExpression(ctx.typeExpression(0)));
		type.setValueType(resolveTypeExpression(ctx.typeExpression(1)));
		return type;
	}

	// ==================== Name Resolution ====================

	EClassifier resolveClassifier(List<String> segments) {
		if (segments.size() == 1) {
			String name = segments.get(0);
			EClassifier fromRegistry = findInRegistry(name);
			if (fromRegistry != null) {
				return fromRegistry;
			}
			// Return a synthetic EClass as placeholder
			EClass synthetic = EcoreFactory.eINSTANCE.createEClass();
			synthetic.setName(name);
			return synthetic;
		}

		String classifierName = segments.get(segments.size() - 1);
		String packageName = String.join("::", segments.subList(0, segments.size() - 1));

		if (packageRegistry != null) {
			for (Object key : packageRegistry.keySet().toArray()) {
				EPackage pkg = packageRegistry.getEPackage((String) key);
				if (pkg != null && (pkg.getName().equals(packageName)
						|| pkg.getNsURI().equals(packageName))) {
					EClassifier found = pkg.getEClassifier(classifierName);
					if (found != null) {
						return found;
					}
				}
			}
		}

		EClass synthetic = EcoreFactory.eINSTANCE.createEClass();
		synthetic.setName(classifierName);
		return synthetic;
	}

	private EClassifier findInRegistry(String name) {
		if (packageRegistry == null) {
			return null;
		}
		for (Object key : packageRegistry.keySet().toArray()) {
			EPackage pkg = packageRegistry.getEPackage((String) key);
			if (pkg != null) {
				EClassifier found = pkg.getEClassifier(name);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}

	private void resolveProperty(PropertyCallExp exp, String propName) {
		EClassifier sourceType = getSourceClassifier(exp.getOwnedSource());
		if (sourceType instanceof EClass eClass) {
			EStructuralFeature feature = eClass.getEStructuralFeature(propName);
			if (feature != null) {
				exp.setReferredProperty(feature);
				EClassifier featureType = feature.getEType();
				if (featureType != null) {
					if (feature.isMany()) {
						exp.setType(createCollectionTypeForFeature(feature));
					} else {
						exp.setType(createClassifierType(featureType));
					}
				}
				return;
			}
		}
		EAttribute synth = EcoreFactory.eINSTANCE.createEAttribute();
		synth.setName(propName);
		exp.setReferredProperty(synth);
	}

	private void resolveOperation(OperationCallExp exp, String opName) {
		exp.setName(opName);
		EClassifier sourceType = getSourceClassifier(exp.getOwnedSource());
		if (sourceType instanceof EClass eClass) {
			int argCount = exp.getOwnedArguments().size();
			EOperation bestMatch = null;
			for (EOperation op : eClass.getEAllOperations()) {
				if (opName.equals(op.getName())) {
					if (op.getEParameters().size() == argCount) {
						bestMatch = op;
						break;
					}
					if (bestMatch == null) {
						bestMatch = op;
					}
				}
			}
			if (bestMatch != null) {
				exp.setReferredOperation(bestMatch);
			}
		}
	}

	private EClassifier getSourceClassifier(OclExpression source) {
		if (source == null) {
			return null;
		}
		OclType type = source.getType();
		if (type instanceof CollectionType colType
				&& colType.getElementType() instanceof ClassifierType ct) {
			return ct.getReferredClassifier();
		}
		if (type instanceof ClassifierType ct) {
			return ct.getReferredClassifier();
		}
		return null;
	}

	private OclType inferElementType(OclExpression source) {
		if (source == null) {
			return null;
		}
		OclType type = source.getType();
		if (type instanceof CollectionType colType && colType.getElementType() != null) {
			return copyType(colType.getElementType());
		}
		if (type instanceof ClassifierType ct && ct.getReferredClassifier() != null) {
			return createClassifierType(ct.getReferredClassifier());
		}
		return null;
	}

	// ==================== Internal Helpers ====================

	private OperationCallExp createUnaryOperation(String opName, OclExpression operand) {
		OperationCallExp exp = OCL.createOperationCallExp();
		exp.setOwnedSource(operand);
		resolveOperation(exp, opName);
		return exp;
	}

	private OperationCallExp createBinaryOperation(String opName, OclExpression left,
			OclExpression right) {
		OperationCallExp exp = OCL.createOperationCallExp();
		exp.setOwnedSource(left);
		exp.getOwnedArguments().add(right);
		resolveOperation(exp, opName);
		return exp;
	}

	private PropertyCallExp createPropertyCall(OclExpression source,
			QvtOParser.PropertySuffixContext ctx, boolean isSafe) {
		PropertyCallExp exp = OCL.createPropertyCallExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		exp.setIsPre(ctx.isMarkedPre() != null);

		String propName = qvtoIdentifierText(ctx.qvtoIdentifier());
		resolveProperty(exp, propName);
		return exp;
	}

	private OperationCallExp createDotOperationCall(OclExpression source,
			QvtOParser.DotCallSuffixContext ctx, boolean isSafe) {
		OperationCallExp exp = OCL.createOperationCallExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		exp.setIsPre(ctx.isMarkedPre() != null);

		if (ctx.argumentList() != null) {
			for (QvtOParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				exp.getOwnedArguments().add((OclExpression) visit(argCtx));
			}
		}

		String opName = qvtoIdentifierText(ctx.qvtoIdentifier());
		resolveOperation(exp, opName);
		return exp;
	}

	private ResolveExp createDotResolveCall(OclExpression source,
			QvtOParser.ResolveCallSuffixContext ctx) {
		ResolveExp exp = QVTO.createResolveExp();
		exp.setOwnedSource(source);

		String kind = ctx.resolveKind().getText();
		exp.setIsDeferred(ctx.getStart().getText().equals("late"));
		exp.setIsInverse(kind.startsWith("inv"));
		exp.setOne(kind.contains("one"));

		if (ctx.resolveArgs() != null) {
			buildResolveArgs(exp, ctx.resolveArgs());
		}

		return exp;
	}

	private ResolveInExp createDotResolveInCall(OclExpression source,
			QvtOParser.ResolveInCallSuffixContext ctx) {
		ResolveInExp exp = QVTO.createResolveInExp();
		exp.setOwnedSource(source);

		String kind = ctx.resolveInKind().getText();
		exp.setIsDeferred(ctx.getStart().getText().equals("late"));
		exp.setIsInverse(kind.startsWith("inv"));
		exp.setOne(kind.contains("one"));

		// §8.1.11.4: Resolve the scoped mapping name to a MappingOperation reference
		exp.setInMapping(resolveInMapping(ctx.scopedName()));

		if (ctx.resolveArgs() != null) {
			buildResolveArgs(exp, ctx.resolveArgs());
		}

		return exp;
	}

	// §8.1.11.7: coll->late resolve(Type) = coll->xcollect(late resolve(Type))
	// Build ResolveExp with collection source — engine handles per-element iteration
	private ResolveExp createArrowResolveCall(OclExpression source,
			QvtOParser.ArrowResolveCallContext ctx) {
		ResolveExp exp = QVTO.createResolveExp();
		exp.setOwnedSource(source);
		String kind = ctx.resolveKind().getText();
		exp.setIsDeferred(ctx.getStart().getText().equals("late"));
		exp.setIsInverse(kind.startsWith("inv"));
		exp.setOne(kind.contains("one"));
		if (ctx.resolveArgs() != null) {
			buildResolveArgs(exp, ctx.resolveArgs());
		}
		return exp;
	}

	// §8.1.11.7: coll->late resolveIn(Mapping, Type) = coll->xcollect(late resolveIn(Mapping, Type))
	private ResolveInExp createArrowResolveInCall(OclExpression source,
			QvtOParser.ArrowResolveInCallContext ctx) {
		ResolveInExp exp = QVTO.createResolveInExp();
		exp.setOwnedSource(source);
		String kind = ctx.resolveInKind().getText();
		exp.setIsDeferred(ctx.getStart().getText().equals("late"));
		exp.setIsInverse(kind.startsWith("inv"));
		exp.setOne(kind.contains("one"));
		exp.setInMapping(resolveInMapping(ctx.scopedName()));
		if (ctx.resolveArgs() != null) {
			buildResolveArgs(exp, ctx.resolveArgs());
		}
		return exp;
	}

	private MappingCallExp createDotMappingCall(OclExpression source,
			QvtOParser.MappingCallSuffixContext ctx, boolean isSafe) {
		MappingCallExp exp = QVTO.createMappingCallExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		exp.setIsStrict("xmap".equals(ctx.mappingCallKind().getText()));

		String name = scopedNameText(ctx.scopedName());
		exp.setName(name);

		if (ctx.argumentList() != null) {
			for (QvtOParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				exp.getOwnedArguments().add((OclExpression) visit(argCtx));
			}
		}

		return exp;
	}

	private IteratorExp createXcollectPropertyCall(OclExpression source,
			QvtOParser.ArrowPropertyCallContext propCtx) {
		// §8.2.2.7: list->prop desugars to list->collect(i | i.prop)
		IteratorExp collectExp = OCL.createIteratorExp();
		collectExp.setOwnedSource(source);
		collectExp.setName("collect");

		Variable iterVar = OCL.createVariable();
		iterVar.setName("_xcol_it");
		OclType elementType = inferElementType(source);
		if (elementType != null) {
			iterVar.setType(elementType);
		}
		collectExp.getOwnedIterators().add(iterVar);

		// Build body: i.prop
		VariableExp iterRef = OCL.createVariableExp();
		iterRef.setReferredVariable(iterVar);
		PropertyCallExp propAccess = OCL.createPropertyCallExp();
		propAccess.setOwnedSource(iterRef);
		String propName = qvtoIdentifierText(propCtx.qvtoIdentifier());
		resolveProperty(propAccess, propName);
		collectExp.setOwnedBody(propAccess);

		return collectExp;
	}

	private IteratorExp createArrowMappingCallOnCollection(OclExpression source,
			QvtOParser.ArrowMappingCallContext mapCtx) {
		// §8.2.2.7: list->map f() desugars to list->xcollect(i | i.map f())
		IteratorExp collectExp = OCL.createIteratorExp();
		collectExp.setOwnedSource(source);
		collectExp.setName("collect");

		Variable iterVar = OCL.createVariable();
		iterVar.setName("_xmap_it");
		OclType elementType = inferElementType(source);
		if (elementType != null) {
			iterVar.setType(elementType);
		}
		collectExp.getOwnedIterators().add(iterVar);

		// Build body: i.map f(args)
		VariableExp iterRef = OCL.createVariableExp();
		iterRef.setReferredVariable(iterVar);
		MappingCallExp mapCall = QVTO.createMappingCallExp();
		mapCall.setOwnedSource(iterRef);
		mapCall.setIsStrict("xmap".equals(mapCtx.mappingCallKind().getText()));
		String name = scopedNameText(mapCtx.scopedName());
		mapCall.setName(name);
		if (mapCtx.argumentList() != null) {
			for (QvtOParser.ExpressionContext argCtx : mapCtx.argumentList().expression()) {
				mapCall.getOwnedArguments().add((OclExpression) visit(argCtx));
			}
		}
		collectExp.setOwnedBody(mapCall);

		return collectExp;
	}

	private IteratorExp createArrowObjectCall(OclExpression source,
			QvtOParser.ArrowObjectCallContext ctx) {
		// §8.4.7: coll->object(x:T) Type { ... } desugars to coll->collect(x | object Type { ... })
		QvtOParser.ObjectExpContext objCtx = ctx.objectExp();

		IteratorExp collectExp = OCL.createIteratorExp();
		collectExp.setOwnedSource(source);
		collectExp.setName("collect");

		// Iterator variable from objectIterator: (x : Type)
		Variable iterVar = OCL.createVariable();
		if (objCtx.objectIterator() != null) {
			QvtOParser.ObjectIteratorContext iterCtx = objCtx.objectIterator();
			iterVar.setName(qvtoIdentifierText(iterCtx.qvtoIdentifier()));
			OclType iterType = resolveTypeExpression(iterCtx.typeExpression());
			if (iterType != null) {
				iterVar.setType(iterType);
			}
		} else {
			// No explicit iterator — generate synthetic name, infer type from source
			iterVar.setName("_xobj_it");
			OclType elementType = inferElementType(source);
			if (elementType != null) {
				iterVar.setType(elementType);
			}
		}
		collectExp.getOwnedIterators().add(iterVar);

		// Build body: the ObjectExp (visited with the iterator variable in scope)
		QvtoEnvironment savedEnv = this.environment;
		this.environment = this.environment.nested(iterVar);
		ObjectExp objectBody = visitObjectExp(objCtx);
		this.environment = savedEnv;

		collectExp.setOwnedBody(objectBody);
		return collectExp;
	}

	private IteratorExp createArrowSwitchCall(OclExpression source,
			QvtOParser.ArrowSwitchCallContext ctx) {
		// §8.2.2.8: coll->switch(i) { ... } desugars to coll->xcollect(i | switch { ... })
		QvtOParser.SwitchExpContext switchCtx = ctx.switchExp();

		IteratorExp xcollectExp = OCL.createIteratorExp();
		xcollectExp.setOwnedSource(source);
		xcollectExp.setName("xcollect");

		// Iterator variable from switchIterator: (i) or (i : Type) or (i := expr)
		Variable iterVar = OCL.createVariable();
		if (switchCtx.switchIterator() != null) {
			QvtOParser.SwitchIteratorContext iterCtx = switchCtx.switchIterator();
			iterVar.setName(qvtoIdentifierText(iterCtx.qvtoIdentifier()));
			if (iterCtx.typeExpression() != null) {
				OclType iterType = resolveTypeExpression(iterCtx.typeExpression());
				if (iterType != null) {
					iterVar.setType(iterType);
				}
			}
		} else {
			// No explicit iterator — generate synthetic name
			iterVar.setName("_xswitch_it");
			OclType elementType = inferElementType(source);
			if (elementType != null) {
				iterVar.setType(elementType);
			}
		}
		xcollectExp.getOwnedIterators().add(iterVar);

		// Build body: the SwitchExp (visited with the iterator variable in scope)
		QvtoEnvironment savedEnv = this.environment;
		this.environment = this.environment.nested(iterVar);
		OclExpression switchBody = visitSwitchExp(switchCtx);
		this.environment = savedEnv;

		xcollectExp.setOwnedBody(switchBody);
		return xcollectExp;
	}

	private IteratorExp createIteratorExp(OclExpression source,
			QvtOParser.IteratorCallContext ctx, boolean isSafe) {
		IteratorExp exp = OCL.createIteratorExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		exp.setName(qvtoIdentifierText(ctx.qvtoIdentifier()));

		QvtoEnvironment savedEnv = this.environment;
		OclType elementType = inferElementType(source);

		QvtOParser.IteratorVariablesContext varsCtx = ctx.iteratorVariables();
		List<Variable> iterVars = new ArrayList<>();
		for (int i = 0; i < varsCtx.qvtoIdentifier().size(); i++) {
			Variable iterVar = OCL.createVariable();
			iterVar.setName(qvtoIdentifierText(varsCtx.qvtoIdentifier(i)));
			if (i < varsCtx.typeExpression().size() && varsCtx.typeExpression(i) != null) {
				iterVar.setType(resolveTypeExpression(varsCtx.typeExpression(i)));
			} else if (elementType != null) {
				iterVar.setType(elementType);
			}
			iterVars.add(iterVar);
			exp.getOwnedIterators().add(iterVar);
		}
		this.environment = this.environment.nested(iterVars);

		exp.setOwnedBody((OclExpression) visit(ctx.expression()));

		this.environment = savedEnv;
		return exp;
	}

	private IterateExp createIterateExp(OclExpression source,
			QvtOParser.IterateCallContext ctx, boolean isSafe) {
		IterateExp exp = OCL.createIterateExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);

		QvtoEnvironment savedEnv = this.environment;

		Variable iterVar = OCL.createVariable();
		iterVar.setName(qvtoIdentifierText(ctx.qvtoIdentifier(1)));
		if (ctx.iterType != null) {
			iterVar.setType(resolveTypeExpression(ctx.iterType));
		} else {
			OclType elementType = inferElementType(source);
			if (elementType != null) {
				iterVar.setType(elementType);
			}
		}
		exp.getOwnedIterators().add(iterVar);

		Variable accVar = OCL.createVariable();
		accVar.setName(qvtoIdentifierText(ctx.qvtoIdentifier(2)));
		if (ctx.accType != null) {
			accVar.setType(resolveTypeExpression(ctx.accType));
		}
		accVar.setOwnedInit((OclExpression) visit(ctx.expression(0)));
		exp.setOwnedResult(accVar);

		this.environment = this.environment.nested(iterVar);
		this.environment = this.environment.nested(accVar);

		exp.setOwnedBody((OclExpression) visit(ctx.expression(1)));

		this.environment = savedEnv;
		return exp;
	}

	private static final Set<String> ITERATOR_NAMES = Set.of(
			"select", "reject", "collect", "collectNested", "forAll", "exists",
			"any", "one", "isUnique", "sortedBy", "closure");

	private OclExpression createCollectionOperation(OclExpression source,
			QvtOParser.CollectionOperationCallContext ctx, boolean isSafe) {
		String opName = qvtoIdentifierText(ctx.qvtoIdentifier());

		// §8.4.4: ->select(#Type) / ->select(##Type) — shorthand type check as iterator predicate
		// When a known iterator receives a single #/## shorthand argument (sourceless oclIsKindOf/oclIsTypeOf),
		// desugar to IteratorExp with the type check as body and iterator variable as source.
		if (ITERATOR_NAMES.contains(opName) && ctx.argumentList() != null
				&& ctx.argumentList().expression().size() == 1) {
			OclExpression arg = (OclExpression) visit(ctx.argumentList().expression(0));
			if (arg instanceof OperationCallExp shorthand
					&& shorthand.getOwnedSource() == null
					&& ("oclIsKindOf".equals(shorthand.getName())
						|| "oclIsTypeOf".equals(shorthand.getName()))) {
				return desugarShorthandIterator(source, opName, shorthand, isSafe);
			}
		}

		OperationCallExp exp = OCL.createOperationCallExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);

		if (ctx.argumentList() != null) {
			for (QvtOParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				exp.getOwnedArguments().add((OclExpression) visit(argCtx));
			}
		}

		resolveOperation(exp, opName);
		return exp;
	}

	/**
	 * Desugars {@code ->select(#Type)} into {@code ->select(_it | _it.oclIsKindOf(Type))}.
	 */
	private IteratorExp desugarShorthandIterator(OclExpression source, String iterName,
			OperationCallExp shorthand, boolean isSafe) {
		IteratorExp iterExp = OCL.createIteratorExp();
		iterExp.setOwnedSource(source);
		iterExp.setName(iterName);
		iterExp.setIsSafe(isSafe);

		Variable iterVar = OCL.createVariable();
		iterVar.setName("_hash_it");
		OclType elementType = inferElementType(source);
		if (elementType != null) {
			iterVar.setType(elementType);
		}
		iterExp.getOwnedIterators().add(iterVar);

		// Set iterator variable as source of the type check
		VariableExp iterRef = OCL.createVariableExp();
		iterRef.setReferredVariable(iterVar);
		shorthand.setOwnedSource(iterRef);

		iterExp.setOwnedBody(shorthand);
		return iterExp;
	}

	private VariableExp createVariableExp(Variable variable) {
		VariableExp exp = OCL.createVariableExp();
		exp.setReferredVariable(variable);
		exp.setType(variable.getType());
		return exp;
	}

	private OclExpression resolveImplicitProperty(String name) {
		// Try to resolve as a classifier (type reference) first
		EClassifier classifier = findInRegistry(name);
		if (classifier != null) {
			TypeExp typeExp = OCL.createTypeExp();
			typeExp.setReferredType(createClassifierType(classifier));
			return typeExp;
		}
		// Fall back to unresolved variable reference
		Variable extVar = OCL.createVariable();
		extVar.setName(name);
		return createVariableExp(extVar);
	}

	private EnumLiteralExp tryResolveEnumLiteral(List<String> segments) {
		String enumName = segments.get(0);
		String literalName = segments.get(1);
		if (packageRegistry != null) {
			for (Object key : packageRegistry.keySet().toArray()) {
				EPackage pkg = packageRegistry.getEPackage((String) key);
				if (pkg != null) {
					EnumLiteralExp result = findEnumLiteralInPackage(pkg, enumName, literalName);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}

	private static EnumLiteralExp findEnumLiteralInPackage(EPackage pkg,
			String enumName, String literalName) {
		for (EClassifier classifier : pkg.getEClassifiers()) {
			if (classifier instanceof EEnum eEnum && eEnum.getName().equals(enumName)) {
				EEnumLiteral literal = eEnum.getEEnumLiteral(literalName);
				if (literal != null) {
					EnumLiteralExp exp = OCL.createEnumLiteralExp();
					exp.setReferredLiteral(literal);
					return exp;
				}
			}
		}
		return null;
	}

	private OclExpression resolveQualifiedName(List<String> segments) {
		EClassifier resolved = resolveClassifier(segments);
		TypeExp exp = OCL.createTypeExp();
		exp.setReferredType(createClassifierType(resolved));
		return exp;
	}

	private CollectionType createCollectionTypeForFeature(EStructuralFeature feature) {
		CollectionType colType = OCL.createCollectionType();
		colType.setElementType(createClassifierType(feature.getEType()));
		if (feature instanceof EReference ref) {
			if (ref.isOrdered()) {
				colType.setKind(ref.isUnique() ? CollectionKind.ORDERED_SET : CollectionKind.SEQUENCE);
			} else {
				colType.setKind(ref.isUnique() ? CollectionKind.SET : CollectionKind.BAG);
			}
		} else {
			colType.setKind(CollectionKind.SEQUENCE);
		}
		return colType;
	}

	private OclType copyType(OclType type) {
		if (type instanceof ClassifierType ct) {
			return createClassifierType(ct.getReferredClassifier());
		}
		if (type instanceof CollectionType colType) {
			CollectionType copy = OCL.createCollectionType();
			if (colType.getElementType() != null) {
				copy.setElementType(copyType(colType.getElementType()));
			}
			copy.setKind(colType.getKind());
			return copy;
		}
		return null;
	}

	private BlockExp buildBlock(QvtOParser.BlockContext ctx) {
		BlockExp block = IMP.createBlockExp();
		for (QvtOParser.StatementContext stmtCtx : ctx.statement()) {
			OclExpression stmtExpr = buildStatement(stmtCtx);
			if (stmtExpr != null) {
				block.getBody().add(stmtExpr);
			}
		}
		return block;
	}

	private Variable buildVarFromDeclarator(QvtOParser.VarDeclaratorContext ctx) {
		Variable var = OCL.createVariable();
		var.setName(qvtoIdentifierText(ctx.qvtoIdentifier()));
		if (ctx.typeExpression() != null) {
			var.setType(resolveTypeExpression(ctx.typeExpression()));
		}
		if (ctx.expression() != null) {
			OclExpression init = (OclExpression) visit(ctx.expression());
			var.setOwnedInit(init);
			// Infer type from init expression if not explicitly declared
			if (var.getType() == null && init != null && init.getType() != null) {
				var.setType(copyType(init.getType()));
			}
		}
		return var;
	}

	private VariableInitExp buildVariableInitExp(QvtOParser.VarDeclaratorContext ctx) {
		VariableInitExp initExp = IMP.createVariableInitExp();

		Variable var = buildVarFromDeclarator(ctx);
		initExp.setReferredVariable(var);

		// §8.2.2.10: '::=' notation — withResult=true, the expression returns the init value
		if (ctx.varInitOp() != null && ctx.varInitOp().getText().equals("::=")) {
			initExp.setWithResult(true);
		}

		// Register in current environment
		this.environment = this.environment.nested(var);

		return initExp;
	}

	private void buildResolveArgs(ResolveExp exp, QvtOParser.ResolveArgsContext ctx) {
		if (ctx.resolveTarget() != null) {
			Variable target = OCL.createVariable();
			if (ctx.resolveTarget() instanceof QvtOParser.NamedResolveTargetContext named) {
				target.setName(qvtoIdentifierText(named.qvtoIdentifier()));
				target.setType(resolveTypeExpression(named.typeExpression()));
			} else if (ctx.resolveTarget() instanceof QvtOParser.TypeOnlyResolveTargetContext typeOnly) {
				target.setName("_it");
				target.setType(resolveTypeExpression(typeOnly.typeExpression()));
			}
			exp.setTarget(target);
		}
		if (ctx.expression() != null) {
			exp.setCondition((OclExpression) visit(ctx.expression()));
		}
	}

	private LogExp buildLogExp(QvtOParser.LogCallExpContext ctx) {
		LogExp logExp = IMP.createLogExp();
		logExp.setName("log");

		if (ctx.argumentList() != null) {
			for (QvtOParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				logExp.getOwnedArguments().add((OclExpression) visit(argCtx));
			}
		}

		if (ctx.expression() != null) {
			logExp.setCondition((OclExpression) visit(ctx.expression()));
		}

		return logExp;
	}

	// ==================== Utility ====================

	private List<String> pathNameSegments(QvtOParser.PathNameContext ctx) {
		List<String> segments = new ArrayList<>();
		for (var id : ctx.qvtoIdentifier()) {
			segments.add(qvtoIdentifierText(id));
		}
		return segments;
	}

	/**
	 * §8.2.1.10: Resolve the context type from a scoped name (e.g., String::wrap, Integer::doubled,
	 * SourceElement::toTarget). Returns null if no context prefix present.
	 */
	OclType resolveContextType(QvtOParser.ScopedNameContext ctx) {
		if (ctx.primitiveType() != null) {
			return createPrimitiveType(ctx.primitiveType().getText());
		}
		if (ctx.collectionType() != null) {
			return resolveCollectionType(ctx.collectionType());
		}
		// §8.4.7: scoped_identifier ::= identifier ('::' identifier)*
		// Context type = all segments except the last (operation name)
		List<QvtOParser.QvtoIdentifierContext> ids = ctx.qvtoIdentifier();
		if (ids.size() >= 2) {
			List<String> segments = new ArrayList<>();
			for (int i = 0; i < ids.size() - 1; i++) {
				segments.add(qvtoIdentifierText(ids.get(i)));
			}
			return createClassifierType(resolveClassifier(segments));
		}
		return null;
	}

	/**
	 * §8.1.11.4: Creates a MappingOperation stub from a scopedName for resolveIn references.
	 * The scoped name may be "ContextType::mappingName" or just "mappingName".
	 * Only the operation name is used for trace lookup at runtime.
	 */
	private MappingOperation resolveInMapping(QvtOParser.ScopedNameContext ctx) {
		String fullName = scopedNameText(ctx);
		// Extract just the operation name (last segment after ::)
		String mappingName = fullName.contains("::")
				? fullName.substring(fullName.lastIndexOf("::") + 2)
				: fullName;
		MappingOperation stub = QVTO.createMappingOperation();
		stub.setName(mappingName);
		return stub;
	}

	String scopedNameText(QvtOParser.ScopedNameContext ctx) {
		// §8.1.9: Contextual operations on primitive types (e.g., query String::wrap())
		if (ctx.primitiveType() != null) {
			return ctx.primitiveType().getText()
					+ "::" + qvtoIdentifierText(ctx.qvtoIdentifier(0));
		}
		// §8.1.9: Contextual operations on collection types (e.g., query Collection(Real)::op())
		if (ctx.collectionType() != null) {
			return ctx.collectionType().getText()
					+ "::" + qvtoIdentifierText(ctx.qvtoIdentifier(0));
		}
		// §8.4.7: scoped_identifier ::= identifier ('::' identifier)*
		List<QvtOParser.QvtoIdentifierContext> ids = ctx.qvtoIdentifier();
		List<String> parts = new ArrayList<>();
		for (QvtOParser.QvtoIdentifierContext idCtx : ids) {
			parts.add(qvtoIdentifierText(idCtx));
		}
		return String.join("::", parts);
	}

	String qualifiedNameText(QvtOParser.QualifiedNameContext ctx) {
		List<String> parts = new ArrayList<>();
		for (QvtOParser.QvtoIdentifierContext idCtx : ctx.qvtoIdentifier()) {
			parts.add(qvtoIdentifierText(idCtx));
		}
		return String.join(".", parts);
	}

	static String qvtoIdentifierText(QvtOParser.QvtoIdentifierContext ctx) {
		return ctx.getText();
	}

	private CollectionKind resolveCollectionKind(String kindText) {
		return switch (kindText) {
			case "Set" -> CollectionKind.SET;
			case "OrderedSet" -> CollectionKind.ORDERED_SET;
			case "Bag" -> CollectionKind.BAG;
			case "Sequence", "List" -> CollectionKind.SEQUENCE; // D26: List alias for Sequence
			default -> CollectionKind.COLLECTION;
		};
	}

	private String unescapeString(String s) {
		StringBuilder sb = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\' && i + 1 < s.length()) {
				char next = s.charAt(i + 1);
				switch (next) {
					case 'n' -> { sb.append('\n'); i++; }
					case 't' -> { sb.append('\t'); i++; }
					case 'r' -> { sb.append('\r'); i++; }
					case 'f' -> { sb.append('\f'); i++; }
					case 'b' -> { sb.append('\b'); i++; }
					case '\\' -> { sb.append('\\'); i++; }
					case '\'' -> { sb.append('\''); i++; }
					case '"' -> { sb.append('"'); i++; }
					case '0', '1', '2', '3', '4', '5', '6', '7' -> {
						// Octal escape: \0 to \377
						int start = i + 1;
						int end = start + 1;
						while (end < s.length() && end - start < 3
								&& s.charAt(end) >= '0' && s.charAt(end) <= '7') {
							end++;
						}
						int octalVal = Integer.parseInt(s.substring(start, end), 8);
						sb.append((char) octalVal);
						i = end - 1;
					}
					default -> { sb.append(c); } // unknown escape, keep backslash
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
