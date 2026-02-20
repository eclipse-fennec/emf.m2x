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
import org.eclipse.fennec.m2m.model.imperativeocl.AltExp;
import org.eclipse.fennec.m2m.model.imperativeocl.AssertExp;
import org.eclipse.fennec.m2m.model.imperativeocl.AssignExp;
import org.eclipse.fennec.m2m.model.imperativeocl.BlockExp;
import org.eclipse.fennec.m2m.model.imperativeocl.CatchExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ComputeExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ForExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclFactory;
import org.eclipse.fennec.m2m.model.imperativeocl.InstantiationExp;
import org.eclipse.fennec.m2m.model.imperativeocl.LogExp;
import org.eclipse.fennec.m2m.model.imperativeocl.RaiseExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ReturnExp;
import org.eclipse.fennec.m2m.model.imperativeocl.SeverityKind;
import org.eclipse.fennec.m2m.model.imperativeocl.SwitchExp;
import org.eclipse.fennec.m2m.model.imperativeocl.TryExp;
import org.eclipse.fennec.m2m.model.imperativeocl.VariableInitExp;
import org.eclipse.fennec.m2m.model.imperativeocl.WhileExp;
import org.eclipse.fennec.m2m.model.ocl.BooleanLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.ClassifierType;
import org.eclipse.fennec.m2m.model.ocl.CollectionItem;
import org.eclipse.fennec.m2m.model.ocl.CollectionKind;
import org.eclipse.fennec.m2m.model.ocl.CollectionLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.CollectionLiteralPart;
import org.eclipse.fennec.m2m.model.ocl.CollectionRange;
import org.eclipse.fennec.m2m.model.ocl.CollectionType;
import org.eclipse.fennec.m2m.model.ocl.EnumLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.IfExp;
import org.eclipse.fennec.m2m.model.ocl.IntegerLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.InvalidLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.IterateExp;
import org.eclipse.fennec.m2m.model.ocl.IteratorExp;
import org.eclipse.fennec.m2m.model.ocl.LetExp;
import org.eclipse.fennec.m2m.model.ocl.MapLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.MapLiteralPart;
import org.eclipse.fennec.m2m.model.ocl.MapType;
import org.eclipse.fennec.m2m.model.ocl.NullLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.model.ocl.OclFactory;
import org.eclipse.fennec.m2m.model.ocl.OclType;
import org.eclipse.fennec.m2m.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2m.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2m.model.ocl.RealLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.SequenceType;
import org.eclipse.fennec.m2m.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.TupleLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.TupleLiteralPart;
import org.eclipse.fennec.m2m.model.ocl.TypeExp;
import org.eclipse.fennec.m2m.model.ocl.UnlimitedNaturalLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.Variable;
import org.eclipse.fennec.m2m.model.ocl.VariableExp;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingCallExp;
import org.eclipse.fennec.m2m.model.qvtoperational.ObjectExp;
import org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2m.model.qvtoperational.ResolveExp;
import org.eclipse.fennec.m2m.model.qvtoperational.ResolveInExp;

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
	private QvtoEnvironment environment;

	QvtoExpressionBuilder(QvtoEnvironment environment, EPackage.Registry packageRegistry) {
		this.environment = environment;
		this.packageRegistry = packageRegistry;
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
		String text = ctx.STRING_LITERAL().getText();
		exp.setStringSymbol(unescapeString(text.substring(1, text.length() - 1)));
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
			part.setName(partCtx.IDENTIFIER().getText());
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

	// ==================== Unary Expressions ====================

	@Override
	public OperationCallExp visitNotExp(QvtOParser.NotExpContext ctx) {
		return createUnaryOperation("not", (OclExpression) visit(ctx.expression()));
	}

	@Override
	public OperationCallExp visitUnaryMinusExp(QvtOParser.UnaryMinusExpContext ctx) {
		return createUnaryOperation("-", (OclExpression) visit(ctx.expression()));
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
		return createBinaryOperation(ctx.op.getText(),
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

	// ==================== Navigation ====================

	@Override
	public OclExpression visitNavigationExp(QvtOParser.NavigationExpContext ctx) {
		OclExpression source = (OclExpression) visit(ctx.expression());
		boolean isSafe = ctx.getChild(1).getText().equals("?.");

		QvtOParser.PropertyOrCallSuffixContext suffix = ctx.propertyOrCallSuffix();
		if (suffix instanceof QvtOParser.PropertySuffixContext propCtx) {
			return createPropertyCall(source, propCtx, isSafe);
		} else if (suffix instanceof QvtOParser.DotCallSuffixContext callCtx) {
			return createDotOperationCall(source, callCtx, isSafe);
		}
		return source;
	}

	@Override
	public OclExpression visitArrowExp(QvtOParser.ArrowExpContext ctx) {
		OclExpression source = (OclExpression) visit(ctx.expression());
		boolean isSafe = ctx.getChild(1).getText().equals("?->");

		QvtOParser.IteratorOrOperationCallContext call = ctx.iteratorOrOperationCall();
		if (call instanceof QvtOParser.IteratorCallContext iterCtx) {
			return createIteratorExp(source, iterCtx, isSafe);
		} else if (call instanceof QvtOParser.IterateCallContext iterateCtx) {
			return createIterateExp(source, iterateCtx, isSafe);
		} else if (call instanceof QvtOParser.CollectionOperationCallContext opCtx) {
			return createCollectionOperation(source, opCtx, isSafe);
		}
		return source;
	}

	// ==================== If / Let ====================

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
			current.setOwnedElse((OclExpression) visit(ctx.elseExp));
		} else {
			ifExp.setOwnedElse((OclExpression) visit(ctx.elseExp));
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
			var.setName(bindCtx.IDENTIFIER().getText());
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
	public WhileExp visitWhileExp(QvtOParser.WhileExpContext ctx) {
		WhileExp whileExp = IMP.createWhileExp();
		whileExp.setCondition((OclExpression) visit(ctx.expression()));
		whileExp.setBody(buildBlock(ctx.block()));
		return whileExp;
	}

	@Override
	public ForExp visitForExp(QvtOParser.ForExpContext ctx) {
		ForExp forExp = IMP.createForExp();

		QvtoEnvironment savedEnv = this.environment;

		QvtOParser.ForVarListContext varList = ctx.forVarList();
		QvtOParser.IteratorVariablesContext iterVars = varList.iteratorVariables();

		for (int i = 0; i < iterVars.IDENTIFIER().size(); i++) {
			Variable iterVar = OCL.createVariable();
			iterVar.setName(iterVars.IDENTIFIER(i).getText());
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
		return forExp;
	}

	@Override
	public SwitchExp visitSwitchExp(QvtOParser.SwitchExpContext ctx) {
		SwitchExp switchExp = IMP.createSwitchExp();

		for (QvtOParser.SwitchAltContext altCtx : ctx.switchAlt()) {
			AltExp alt = IMP.createAltExp();
			alt.setCondition((OclExpression) visit(altCtx.expression(0)));
			alt.setBody((OclExpression) visit(altCtx.expression(1)));
			switchExp.getAlternativePart().add(alt);
		}

		// else part
		if (ctx.expression() != null && !ctx.expression().isEmpty()) {
			switchExp.setElsePart((OclExpression) visit(ctx.expression(0)));
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
		if (ctx.qvtoIdentifier() != null) {
			refVar.setName(qvtoIdentifierText(ctx.qvtoIdentifier()));
		} else {
			refVar.setName("result");
		}
		objectExp.setReferredObject(refVar);

		// Type → instantiatedClass (if resolvable as EClass)
		if (ctx.typeExpression() != null) {
			OclType type = resolveTypeExpression(ctx.typeExpression());
			if (type instanceof ClassifierType ct && ct.getReferredClassifier() instanceof EClass ec) {
				objectExp.setInstantiatedClass(ec);
			}
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
		InstantiationExp exp = IMP.createInstantiationExp();

		List<String> segments = pathNameSegments(ctx.pathName());
		EClassifier classifier = resolveClassifier(segments);
		if (classifier instanceof EClass ec) {
			exp.setInstantiatedClass(ec);
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

		// The scoped name references a mapping operation (unresolved at parse time)
		// inMapping will be resolved during linking phase

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
	public VariableInitExp visitVarDeclExp(QvtOParser.VarDeclExpContext ctx) {
		return buildVariableInitExp(ctx.varDeclarator());
	}

	// ==================== Statement Building ====================

	/**
	 * Builds an expression from a statement context (assignment or expression statement).
	 */
	OclExpression buildStatement(QvtOParser.StatementContext ctx) {
		if (ctx instanceof QvtOParser.AssignStatementContext assignCtx) {
			return buildAssignment(assignCtx);
		} else if (ctx instanceof QvtOParser.ExpressionStatementContext exprCtx) {
			return (OclExpression) visit(exprCtx.expression());
		}
		return null;
	}

	private AssignExp buildAssignment(QvtOParser.AssignStatementContext ctx) {
		AssignExp assign = IMP.createAssignExp();
		assign.setLeft((OclExpression) visit(ctx.expression(0)));
		assign.getValue().add((OclExpression) visit(ctx.expression(1)));

		String op = ctx.assignOp().getText();
		assign.setIsReset(":=".equals(op));
		// '+=' → isReset=false (append), '::=' → ordered copy (isReset=true)
		if ("::=".equals(op)) {
			assign.setIsReset(true);
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

	private ClassifierType createClassifierType(EClassifier classifier) {
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
			part.setName(partCtx.IDENTIFIER().getText());
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

		String propName = ctx.IDENTIFIER().getText();
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

		String opName = ctx.IDENTIFIER().getText();
		resolveOperation(exp, opName);
		return exp;
	}

	private IteratorExp createIteratorExp(OclExpression source,
			QvtOParser.IteratorCallContext ctx, boolean isSafe) {
		IteratorExp exp = OCL.createIteratorExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		exp.setName(ctx.IDENTIFIER().getText());

		QvtoEnvironment savedEnv = this.environment;
		OclType elementType = inferElementType(source);

		QvtOParser.IteratorVariablesContext varsCtx = ctx.iteratorVariables();
		List<Variable> iterVars = new ArrayList<>();
		for (int i = 0; i < varsCtx.IDENTIFIER().size(); i++) {
			Variable iterVar = OCL.createVariable();
			iterVar.setName(varsCtx.IDENTIFIER(i).getText());
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
		iterVar.setName(ctx.IDENTIFIER(1).getText());
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
		accVar.setName(ctx.IDENTIFIER(2).getText());
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

	private OperationCallExp createCollectionOperation(OclExpression source,
			QvtOParser.CollectionOperationCallContext ctx, boolean isSafe) {
		OperationCallExp exp = OCL.createOperationCallExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);

		if (ctx.argumentList() != null) {
			for (QvtOParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				exp.getOwnedArguments().add((OclExpression) visit(argCtx));
			}
		}

		String opName = ctx.IDENTIFIER().getText();
		resolveOperation(exp, opName);
		return exp;
	}

	private VariableExp createVariableExp(Variable variable) {
		VariableExp exp = OCL.createVariableExp();
		exp.setReferredVariable(variable);
		exp.setType(variable.getType());
		return exp;
	}

	private OclExpression resolveImplicitProperty(String name) {
		// In QVT-O context, we can't always resolve against contextType,
		// so create an unresolved variable reference
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
			var.setOwnedInit((OclExpression) visit(ctx.expression()));
		}
		return var;
	}

	private VariableInitExp buildVariableInitExp(QvtOParser.VarDeclaratorContext ctx) {
		VariableInitExp initExp = IMP.createVariableInitExp();

		Variable var = buildVarFromDeclarator(ctx);
		initExp.setReferredVariable(var);

		// Register in current environment
		this.environment = this.environment.nested(var);

		return initExp;
	}

	private void buildResolveArgs(ResolveExp exp, QvtOParser.ResolveArgsContext ctx) {
		if (ctx.resolveTarget() != null) {
			Variable target = OCL.createVariable();
			target.setName(qvtoIdentifierText(ctx.resolveTarget().qvtoIdentifier()));
			target.setType(resolveTypeExpression(ctx.resolveTarget().typeExpression()));
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
		for (var id : ctx.IDENTIFIER()) {
			segments.add(id.getText());
		}
		return segments;
	}

	String scopedNameText(QvtOParser.ScopedNameContext ctx) {
		if (ctx.qualifiedName() != null) {
			return qualifiedNameText(ctx.qualifiedName())
					+ "::" + qvtoIdentifierText(ctx.qvtoIdentifier());
		}
		return qvtoIdentifierText(ctx.qvtoIdentifier());
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
			case "Sequence" -> CollectionKind.SEQUENCE;
			default -> CollectionKind.COLLECTION;
		};
	}

	private String unescapeString(String s) {
		return s.replace("\\\\'", "'").replace("\\\\\\\\", "\\\\");
	}
}
