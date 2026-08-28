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
import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.ocl.BooleanLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.CollectionLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.CollectionLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.IfExp;
import org.eclipse.fennec.m2x.model.ocl.IntegerLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.InvalidLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.IterateExp;
import org.eclipse.fennec.m2x.model.ocl.IteratorExp;
import org.eclipse.fennec.m2x.model.ocl.LetExp;
import org.eclipse.fennec.m2x.model.ocl.MapLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.MapLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.NullLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.RealLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.TupleLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.TupleLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.TypeExp;
import org.eclipse.fennec.m2x.model.ocl.UnlimitedNaturalLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.ocl.parser.AbstractExpressionBuilder;
import org.eclipse.fennec.m2x.ocl.parser.OclEnvironment;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;
import org.eclipse.emf.ecore.EObject;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.ParserRuleContext;
import java.util.Map;
import java.util.IdentityHashMap;

/**
 * Visitor for building OCL expression AST nodes from QVT-R parser contexts.
 *
 * <p>Extends {@link QvtRBaseVisitor} for QVT-R grammar-specific contexts
 * and delegates to {@link AbstractExpressionBuilder} for all shared OCL
 * expression building logic.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtrExpressionBuilder extends QvtRBaseVisitor<Object> {

	private static final OclFactory FACTORY = OclFactory.eINSTANCE;

	final AbstractExpressionBuilder support;

	/**
	 * Node to position, by identity — for diagnostics reported at runtime, which know the node but
	 * not where it stood (#116).
	 */
	private final Map<EObject, SourcePosition> nodePositions = new IdentityHashMap<>();

	QvtrExpressionBuilder(EClassifier contextType, EPackage.Registry packageRegistry) {
		this.support = new AbstractExpressionBuilder(contextType, packageRegistry);
	}

	QvtrExpressionBuilder(AbstractExpressionBuilder support) {
		this.support = support;
	}

	// ==================== Environment Convenience ====================

	OclEnvironment env() {
		return support.getEnvironment();
	}

	void setEnv(OclEnvironment env) {
		support.setEnvironment(env);
	}

	// ==================== Expression Entry ====================

	/**
	 * Visits an expression context and returns the OCL expression AST.
	 */
	OclExpression buildExpression(QvtRParser.ExpressionContext ctx) {
		return (OclExpression) visit(ctx);
	}

	// ==================== Literals ====================


	/**
	 * Keeps the diagnostic position with the descent and records where each built node stood — the
	 * one place where the parse tree and the node made from it are both in hand (#110, #116).
	 */
	@Override
	public Object visit(ParseTree tree) {
		if (!(tree instanceof ParserRuleContext context) || context.getStart() == null) {
			return super.visit(tree);
		}
		support.positionAt(context);
		Object built = super.visit(tree);
		if (built instanceof EObject node) {
			nodePositions.putIfAbsent(node, new SourcePosition(context.getStart().getLine(),
					context.getStart().getCharPositionInLine()));
		}
		return built;
	}

	/**
	 * Where each node this builder produced stood.
	 *
	 * @return the positions, by node identity
	 */
	Map<EObject, SourcePosition> getNodePositions() {
		return nodePositions;
	}
	@Override
	public IntegerLiteralExp visitIntegerLiteral(QvtRParser.IntegerLiteralContext ctx) {
		return support.buildIntegerLiteral(ctx.INTEGER_LITERAL().getText());
	}

	@Override
	public RealLiteralExp visitRealLiteral(QvtRParser.RealLiteralContext ctx) {
		return support.buildRealLiteral(ctx.REAL_LITERAL().getText());
	}

	@Override
	public StringLiteralExp visitStringLit(QvtRParser.StringLitContext ctx) {
		return (StringLiteralExp) visit(ctx.stringLiteral_());
	}

	@Override
	public StringLiteralExp visitStringLiteral_(QvtRParser.StringLiteral_Context ctx) {
		List<String> rawTokens = new ArrayList<>();
		for (var token : ctx.STRING_LITERAL()) {
			rawTokens.add(token.getText());
		}
		return support.buildStringLiteral(rawTokens);
	}

	@Override
	public BooleanLiteralExp visitTrueLiteral(QvtRParser.TrueLiteralContext ctx) {
		return support.buildBooleanLiteral(true);
	}

	@Override
	public BooleanLiteralExp visitFalseLiteral(QvtRParser.FalseLiteralContext ctx) {
		return support.buildBooleanLiteral(false);
	}

	@Override
	public NullLiteralExp visitNullLiteral(QvtRParser.NullLiteralContext ctx) {
		return support.buildNullLiteral();
	}

	@Override
	public InvalidLiteralExp visitInvalidLiteral(QvtRParser.InvalidLiteralContext ctx) {
		return support.buildInvalidLiteral();
	}

	@Override
	public UnlimitedNaturalLiteralExp visitUnlimitedNaturalLiteral(
			QvtRParser.UnlimitedNaturalLiteralContext ctx) {
		return support.buildUnlimitedNaturalLiteral();
	}

	@Override
	public OclExpression visitPathNameExp(QvtRParser.PathNameExpContext ctx) {
		support.positionAt(ctx);   // so an unresolved name is reported where it stands (#110)
		return support.buildPathNameExp(pathNameSegments(ctx.pathName()));
	}

	// ==================== Collection Literals ====================

	@Override
	public CollectionLiteralExp visitCollectionLit(QvtRParser.CollectionLitContext ctx) {
		return (CollectionLiteralExp) visit(ctx.collectionLiteral());
	}

	@Override
	public CollectionLiteralExp visitCollectionLiteral(QvtRParser.CollectionLiteralContext ctx) {
		List<CollectionLiteralPart> parts = new ArrayList<>();
		if (ctx.collectionLiteralPart() != null) {
			for (QvtRParser.CollectionLiteralPartContext partCtx : ctx.collectionLiteralPart()) {
				parts.add((CollectionLiteralPart) visit(partCtx));
			}
		}
		return support.buildCollectionLiteral(ctx.collectionKind().getText(), parts);
	}

	@Override
	public CollectionLiteralPart visitCollectionLiteralPart(
			QvtRParser.CollectionLiteralPartContext ctx) {
		if (ctx.expression().size() == 2) {
			return support.buildCollectionRange(
					(OclExpression) visit(ctx.expression(0)),
					(OclExpression) visit(ctx.expression(1)));
		}
		return support.buildCollectionItem((OclExpression) visit(ctx.expression(0)));
	}

	// ==================== Tuple Literals ====================

	@Override
	public TupleLiteralExp visitTupleLit(QvtRParser.TupleLitContext ctx) {
		return (TupleLiteralExp) visit(ctx.tupleLiteral());
	}

	@Override
	public TupleLiteralExp visitTupleLiteral(QvtRParser.TupleLiteralContext ctx) {
		List<TupleLiteralPart> parts = new ArrayList<>();
		for (QvtRParser.TupleLiteralPartContext partCtx : ctx.tupleLiteralPart()) {
			EClassifier type = partCtx.typeExpression() != null
					? resolveTypeExpression(partCtx.typeExpression()) : null;
			parts.add(support.buildTupleLiteralPart(
					identifierText(partCtx.identifier()),
					type,
					(OclExpression) visit(partCtx.expression())));
		}
		return support.buildTupleLiteral(parts);
	}

	// ==================== Map Literals ====================

	@Override
	public MapLiteralExp visitMapLit(QvtRParser.MapLitContext ctx) {
		return (MapLiteralExp) visit(ctx.mapLiteral());
	}

	@Override
	public MapLiteralExp visitMapLiteral(QvtRParser.MapLiteralContext ctx) {
		List<MapLiteralPart> parts = new ArrayList<>();
		if (ctx.mapLiteralPart() != null) {
			for (QvtRParser.MapLiteralPartContext partCtx : ctx.mapLiteralPart()) {
				parts.add(support.buildMapLiteralPart(
						(OclExpression) visit(partCtx.expression(0)),
						(OclExpression) visit(partCtx.expression(1))));
			}
		}
		return support.buildMapLiteral(parts);
	}

	// ==================== Unary Expressions ====================

	@Override
	public OperationCallExp visitNotExp(QvtRParser.NotExpContext ctx) {
		return support.createUnaryOperation("not", (OclExpression) visit(ctx.expression()));
	}

	@Override
	public OperationCallExp visitUnaryMinusExp(QvtRParser.UnaryMinusExpContext ctx) {
		return support.createUnaryOperation("-", (OclExpression) visit(ctx.expression()));
	}

	// ==================== Binary Expressions ====================

	@Override
	public OperationCallExp visitMultExp(QvtRParser.MultExpContext ctx) {
		return support.createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitAddExp(QvtRParser.AddExpContext ctx) {
		return support.createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitCompareExp(QvtRParser.CompareExpContext ctx) {
		return support.createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitEqualityExp(QvtRParser.EqualityExpContext ctx) {
		return support.createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitAndExp(QvtRParser.AndExpContext ctx) {
		return support.createBinaryOperation("and",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitOrExp(QvtRParser.OrExpContext ctx) {
		return support.createBinaryOperation("or",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitXorExp(QvtRParser.XorExpContext ctx) {
		return support.createBinaryOperation("xor",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitImpliesExp(QvtRParser.ImpliesExpContext ctx) {
		return support.createBinaryOperation("implies",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	// ==================== Navigation ====================

	@Override
	public OclExpression visitNavigationExp(QvtRParser.NavigationExpContext ctx) {
		OclExpression source = (OclExpression) visit(ctx.expression());
		boolean isSafe = ctx.getChildCount() > 1 && "?.".equals(ctx.getChild(1).getText());

		QvtRParser.PropertyOrCallSuffixContext suffix = ctx.propertyOrCallSuffix();
		if (suffix instanceof QvtRParser.PropertySuffixContext propCtx) {
			String propName = identifierText(propCtx.identifier());
			return support.buildPropertyCall(source, propName, isSafe, false);
		} else if (suffix instanceof QvtRParser.DotCallSuffixContext callCtx) {
			String opName = identifierText(callCtx.identifier());
			List<OclExpression> args = new ArrayList<>();
			if (callCtx.argumentList() != null) {
				for (QvtRParser.ExpressionContext argCtx : callCtx.argumentList().expression()) {
					args.add((OclExpression) visit(argCtx));
				}
			}
			return support.buildDotOperationCall(source, opName, args, isSafe, false);
		}
		return source;
	}

	@Override
	public OclExpression visitArrowExp(QvtRParser.ArrowExpContext ctx) {
		OclExpression source = (OclExpression) visit(ctx.expression());
		boolean isSafe = ctx.getChildCount() > 1 && "?->".equals(ctx.getChild(1).getText());

		QvtRParser.IteratorOrOperationCallContext call = ctx.iteratorOrOperationCall();
		if (call instanceof QvtRParser.IteratorCallContext iterCtx) {
			return visitIteratorCall(source, iterCtx, isSafe);
		} else if (call instanceof QvtRParser.IterateCallContext iterateCtx) {
			return visitIterateCall(source, iterateCtx, isSafe);
		} else if (call instanceof QvtRParser.CollectionOperationCallContext opCtx) {
			return visitCollectionOperationCall(source, opCtx, isSafe);
		}
		return source;
	}

	private IteratorExp visitIteratorCall(OclExpression source,
			QvtRParser.IteratorCallContext ctx, boolean isSafe) {
		String iterName = identifierText(ctx.identifier());
		OclEnvironment savedEnv = env();

		EClassifier elementType = support.inferElementType(source);
		QvtRParser.IteratorVariablesContext varsCtx = ctx.iteratorVariables();
		List<Variable> iterVars = new ArrayList<>();
		for (int i = 0; i < varsCtx.identifier().size(); i++) {
			EClassifier varType = (i < varsCtx.typeExpression().size()
					&& varsCtx.typeExpression(i) != null)
					? resolveTypeExpression(varsCtx.typeExpression(i)) : elementType;
			Variable iterVar = support.createVariable(
					identifierText(varsCtx.identifier(i)), varType, null);
			iterVars.add(iterVar);
		}
		support.validateIteratorVarCount(iterName, iterVars.size());

		setEnv(env().nested(iterVars));
		OclExpression body = (OclExpression) visit(ctx.expression());
		setEnv(savedEnv);

		return support.buildIteratorExp(source, iterName, iterVars, body, isSafe);
	}

	private IterateExp visitIterateCall(OclExpression source,
			QvtRParser.IterateCallContext ctx, boolean isSafe) {
		OclEnvironment savedEnv = env();

		EClassifier iterType = ctx.typeExpression().size() > 0
				? resolveTypeExpression(ctx.typeExpression(0))
				: support.inferElementType(source);
		Variable iterVar = support.createVariable(
				identifierText(ctx.identifier(1)), iterType, null);

		EClassifier accType = ctx.typeExpression().size() > 1
				? resolveTypeExpression(ctx.typeExpression(1)) : null;
		Variable accVar = support.createVariable(
				identifierText(ctx.identifier(2)), accType,
				(OclExpression) visit(ctx.expression(0)));

		setEnv(env().nested(iterVar));
		setEnv(env().nested(accVar));

		OclExpression body = (OclExpression) visit(ctx.expression(1));
		setEnv(savedEnv);

		return support.buildIterateExp(source, iterVar, accVar, body, isSafe);
	}

	private OclExpression visitCollectionOperationCall(OclExpression source,
			QvtRParser.CollectionOperationCallContext ctx, boolean isSafe) {
		String opName = identifierText(ctx.identifier());

		if (AbstractExpressionBuilder.ITERATOR_NAMES.contains(opName) && ctx.argumentList() != null
				&& ctx.argumentList().expression().size() == 1) {
			OclEnvironment savedEnv = env();
			support.pushImplicitIteratorEnv(source);
			OclExpression body = (OclExpression) visit(ctx.argumentList().expression(0));
			setEnv(savedEnv);
			return support.buildImplicitIterator(source, opName, body, isSafe);
		}

		List<OclExpression> args = new ArrayList<>();
		if (ctx.argumentList() != null) {
			for (QvtRParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				args.add((OclExpression) visit(argCtx));
			}
		}
		return support.buildCollectionOperationCall(source, opName, args, isSafe);
	}

	// ==================== If / Let ====================

	@Override
	public IfExp visitIfExp(QvtRParser.IfExpContext ctx) {
		List<QvtRParser.ExpressionContext> exprs = ctx.expression();
		OclExpression condition = (OclExpression) visit(exprs.get(0));
		OclExpression thenExp = (OclExpression) visit(exprs.get(1));

		int elseifCount = (exprs.size() - 3) / 2;
		List<OclExpression> elseIfConditions = null;
		List<OclExpression> elseIfExps = null;
		if (elseifCount > 0) {
			elseIfConditions = new ArrayList<>();
			elseIfExps = new ArrayList<>();
			for (int i = 0; i < elseifCount; i++) {
				elseIfConditions.add((OclExpression) visit(exprs.get(2 + i * 2)));
				elseIfExps.add((OclExpression) visit(exprs.get(3 + i * 2)));
			}
		}
		OclExpression elseExp = (OclExpression) visit(exprs.get(exprs.size() - 1));
		return support.buildIfExp(condition, thenExp, elseIfConditions, elseIfExps, elseExp);
	}

	@Override
	public LetExp visitLetExp(QvtRParser.LetExpContext ctx) {
		List<QvtRParser.LetBindingContext> bindings = ctx.letBinding();
		OclEnvironment savedEnv = env();

		LetExp outermost = null;
		LetExp current = null;

		for (QvtRParser.LetBindingContext bindCtx : bindings) {
			EClassifier type = bindCtx.typeExpression() != null
					? resolveTypeExpression(bindCtx.typeExpression()) : null;
			Variable var = support.createVariable(
					identifierText(bindCtx.identifier()),
					type,
					(OclExpression) visit(bindCtx.expression()));
			setEnv(env().nested(var));

			LetExp letExp = FACTORY.createLetExp();
			letExp.setOwnedVariable(var);
			if (outermost == null) {
				outermost = letExp;
			} else {
				current.setOwnedIn(letExp);
			}
			current = letExp;
		}

		current.setOwnedIn((OclExpression) visit(ctx.expression()));
		setEnv(savedEnv);
		return outermost;
	}

	// ==================== Primary Expressions ====================

	@Override
	public OclExpression visitSelfExp(QvtRParser.SelfExpContext ctx) {
		return support.buildSelfExp();
	}

	@Override
	public OclExpression visitParenExp(QvtRParser.ParenExpContext ctx) {
		return (OclExpression) visit(ctx.expression());
	}

	@Override
	public OclExpression visitPrimaryExp(QvtRParser.PrimaryExpContext ctx) {
		return (OclExpression) visit(ctx.primaryExpression());
	}

	@Override
	public OclExpression visitLiteralExp(QvtRParser.LiteralExpContext ctx) {
		return (OclExpression) visit(ctx.literalExpression());
	}

	@Override
	public TypeExp visitPrimitiveTypeExp(QvtRParser.PrimitiveTypeExpContext ctx) {
		return support.buildTypeExp(support.createPrimitiveType(ctx.primitiveType().getText()));
	}

	@Override
	public TypeExp visitCollectionTypeExp(QvtRParser.CollectionTypeExpContext ctx) {
		return support.buildTypeExp(resolveCollectionType(ctx.collectionType()));
	}

	@Override
	public TypeExp visitMapTypeExp(QvtRParser.MapTypeExpContext ctx) {
		return support.buildTypeExp(resolveMapType(ctx.mapType()));
	}

	@Override
	public TypeExp visitTupleTypeExp(QvtRParser.TupleTypeExpContext ctx) {
		return support.buildTypeExp(resolveTupleType(ctx.tupleType()));
	}

	@Override
	public OperationCallExp visitOperationCallExp(QvtRParser.OperationCallExpContext ctx) {
		List<String> segments = pathNameSegments(ctx.pathName());
		String opName = segments.get(segments.size() - 1);

		List<OclExpression> args = new ArrayList<>();
		if (ctx.argumentList() != null) {
			for (QvtRParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				args.add((OclExpression) visit(argCtx));
			}
		}
		return support.buildImplicitOperationCall(opName, args, false);
	}

	// ==================== Type Resolution ====================

	EClassifier resolveTypeExpression(QvtRParser.TypeExpressionContext ctx) {
		if (ctx.primitiveType() != null) {
			return support.createPrimitiveType(ctx.primitiveType().getText());
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
		if (ctx.pathName() != null) {
			support.positionAt(ctx);   // so an unresolved name is reported where it stands (#110)
			return support.buildTypeFromPath(pathNameSegments(ctx.pathName()));
		}
		return null;
	}

	private OclType resolveCollectionType(QvtRParser.CollectionTypeContext ctx) {
		return support.buildCollectionType(ctx.collectionKind().getText(),
				resolveTypeExpression(ctx.typeExpression()));
	}

	private OclType resolveMapType(QvtRParser.MapTypeContext ctx) {
		return support.buildMapType(
				resolveTypeExpression(ctx.typeExpression(0)),
				resolveTypeExpression(ctx.typeExpression(1)));
	}

	private OclType resolveTupleType(QvtRParser.TupleTypeContext ctx) {
		List<String> names = new ArrayList<>();
		List<EClassifier> types = new ArrayList<>();
		for (QvtRParser.TupleTypePartContext partCtx : ctx.tupleTypePart()) {
			names.add(identifierText(partCtx.identifier()));
			types.add(resolveTypeExpression(partCtx.typeExpression()));
		}
		return support.buildTupleType(names, types);
	}

	// ==================== Utility Methods ====================

	static String identifierText(QvtRParser.IdentifierContext ctx) {
		if (ctx.ESCAPED_IDENTIFIER() != null) {
			return AbstractExpressionBuilder.extractEscapedIdentifier(
					ctx.ESCAPED_IDENTIFIER().getText());
		}
		return ctx.getText();
	}

	static List<String> pathNameSegments(QvtRParser.PathNameContext ctx) {
		List<String> segments = new ArrayList<>();
		for (var id : ctx.identifier()) {
			segments.add(identifierText(id));
		}
		return segments;
	}
}
