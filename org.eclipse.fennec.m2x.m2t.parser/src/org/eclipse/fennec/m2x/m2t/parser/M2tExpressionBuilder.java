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
package org.eclipse.fennec.m2x.m2t.parser;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
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
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;
import org.eclipse.fennec.m2x.ocl.parser.AbstractExpressionBuilder;
import org.eclipse.fennec.m2x.ocl.parser.OclEnvironment;

/**
 * Visitor for building OCL expression AST nodes from M2T parser contexts.
 *
 * <p>Extends {@link M2tParserBaseVisitor} for M2T grammar-specific contexts
 * and delegates to {@link AbstractExpressionBuilder} for all shared OCL
 * expression building logic.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tExpressionBuilder extends M2tParserBaseVisitor<Object> {

	private static final OclFactory FACTORY = OclFactory.eINSTANCE;

	final AbstractExpressionBuilder support;

	/**
	 * Node to position, by identity. A template's nodes belong to one module — M2T builds them
	 * itself and never consults the expression cache — so this map is complete for that module
	 * and outlives nothing else (#116).
	 */
	private final Map<EObject, SourcePosition> positions = new IdentityHashMap<>();

	M2tExpressionBuilder(EClassifier contextType, EPackage.Registry packageRegistry) {
		this.support = new AbstractExpressionBuilder(contextType, packageRegistry);
	}

	M2tExpressionBuilder(AbstractExpressionBuilder support) {
		this.support = support;
	}

	// ==================== Environment Convenience ====================

	private OclEnvironment env() {
		return support.getEnvironment();
	}

	private void setEnv(OclEnvironment env) {
		support.setEnvironment(env);
	}

	// ==================== Expression Entry ====================

	/**
	 * Visits an expression context and returns the OCL expression AST.
	 */
	public OclExpression buildExpression(M2tParser.ExpressionContext ctx) {
		return (OclExpression) visit(ctx);
	}

	// ==================== Literals ====================


	/**
	 * Keeps the diagnostic position with the descent, so a problem the shared expression builder
	 * finds is reported at a place rather than at line 0 (#110). One override covers the visitor,
	 * because every step into the tree goes through here.
	 */
	@Override
	public Object visit(ParseTree tree) {
		if (!(tree instanceof ParserRuleContext context)) {
			return super.visit(tree);
		}
		support.positionAt(context);
		Object built = super.visit(tree);
		// The same step that keeps the parse-time cursor also learns which node came out of this
		// piece of text — which is the only place the two are together (#116).
		if (built instanceof EObject node && context.getStart() != null) {
			positions.putIfAbsent(node, new SourcePosition(context.getStart().getLine(),
					context.getStart().getCharPositionInLine()));
		}
		return built;
	}

	/**
	 * Where each node this builder produced stood in the template.
	 *
	 * @return the positions, by node identity
	 */
	Map<EObject, SourcePosition> getPositions() {
		return positions;
	}
	@Override
	public IntegerLiteralExp visitIntegerLiteral(M2tParser.IntegerLiteralContext ctx) {
		return support.buildIntegerLiteral(ctx.INTEGER_LITERAL().getText());
	}

	@Override
	public RealLiteralExp visitRealLiteral(M2tParser.RealLiteralContext ctx) {
		return support.buildRealLiteral(ctx.REAL_LITERAL().getText());
	}

	@Override
	public StringLiteralExp visitStringLit(M2tParser.StringLitContext ctx) {
		return (StringLiteralExp) visit(ctx.stringLiteral());
	}

	@Override
	public StringLiteralExp visitStringLiteral(M2tParser.StringLiteralContext ctx) {
		List<String> rawTokens = new ArrayList<>();
		for (var token : ctx.STRING_LITERAL()) {
			rawTokens.add(token.getText());
		}
		return support.buildStringLiteral(rawTokens);
	}

	@Override
	public BooleanLiteralExp visitTrueLiteral(M2tParser.TrueLiteralContext ctx) {
		return support.buildBooleanLiteral(true);
	}

	@Override
	public BooleanLiteralExp visitFalseLiteral(M2tParser.FalseLiteralContext ctx) {
		return support.buildBooleanLiteral(false);
	}

	@Override
	public NullLiteralExp visitNullLiteral(M2tParser.NullLiteralContext ctx) {
		return support.buildNullLiteral();
	}

	@Override
	public InvalidLiteralExp visitInvalidLiteral(M2tParser.InvalidLiteralContext ctx) {
		return support.buildInvalidLiteral();
	}

	@Override
	public UnlimitedNaturalLiteralExp visitUnlimitedNaturalLiteral(
			M2tParser.UnlimitedNaturalLiteralContext ctx) {
		return support.buildUnlimitedNaturalLiteral();
	}

	@Override
	public OclExpression visitPathNameExp(M2tParser.PathNameExpContext ctx) {
		return support.buildPathNameExp(pathNameSegments(ctx.pathName()));
	}

	// ==================== Collection Literals ====================

	@Override
	public CollectionLiteralExp visitCollectionLit(M2tParser.CollectionLitContext ctx) {
		return (CollectionLiteralExp) visit(ctx.collectionLiteral());
	}

	@Override
	public CollectionLiteralExp visitCollectionLiteral(M2tParser.CollectionLiteralContext ctx) {
		List<CollectionLiteralPart> parts = new ArrayList<>();
		if (ctx.collectionLiteralPart() != null) {
			for (M2tParser.CollectionLiteralPartContext partCtx : ctx.collectionLiteralPart()) {
				parts.add((CollectionLiteralPart) visit(partCtx));
			}
		}
		return support.buildCollectionLiteral(ctx.collectionKind().getText(), parts);
	}

	@Override
	public CollectionLiteralPart visitCollectionLiteralPart(
			M2tParser.CollectionLiteralPartContext ctx) {
		if (ctx.expression().size() == 2) {
			return support.buildCollectionRange(
					(OclExpression) visit(ctx.expression(0)),
					(OclExpression) visit(ctx.expression(1)));
		}
		return support.buildCollectionItem((OclExpression) visit(ctx.expression(0)));
	}

	// ==================== Tuple Literals ====================

	@Override
	public TupleLiteralExp visitTupleLit(M2tParser.TupleLitContext ctx) {
		return (TupleLiteralExp) visit(ctx.tupleLiteral());
	}

	@Override
	public TupleLiteralExp visitTupleLiteral(M2tParser.TupleLiteralContext ctx) {
		List<TupleLiteralPart> parts = new ArrayList<>();
		for (M2tParser.TupleLiteralPartContext partCtx : ctx.tupleLiteralPart()) {
			OclType type = partCtx.typeExpression() != null
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
	public MapLiteralExp visitMapLit(M2tParser.MapLitContext ctx) {
		return (MapLiteralExp) visit(ctx.mapLiteral());
	}

	@Override
	public MapLiteralExp visitMapLiteral(M2tParser.MapLiteralContext ctx) {
		List<MapLiteralPart> parts = new ArrayList<>();
		if (ctx.mapLiteralPart() != null) {
			for (M2tParser.MapLiteralPartContext partCtx : ctx.mapLiteralPart()) {
				parts.add(support.buildMapLiteralPart(
						(OclExpression) visit(partCtx.expression(0)),
						(OclExpression) visit(partCtx.expression(1))));
			}
		}
		return support.buildMapLiteral(parts);
	}

	// ==================== Unary Expressions ====================

	@Override
	public OperationCallExp visitNotExp(M2tParser.NotExpContext ctx) {
		return support.createUnaryOperation("not", (OclExpression) visit(ctx.expression()));
	}

	@Override
	public OperationCallExp visitUnaryMinusExp(M2tParser.UnaryMinusExpContext ctx) {
		return support.createUnaryOperation("-", (OclExpression) visit(ctx.expression()));
	}

	// ==================== Binary Expressions ====================

	@Override
	public OperationCallExp visitMultExp(M2tParser.MultExpContext ctx) {
		return support.createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitAddExp(M2tParser.AddExpContext ctx) {
		return support.createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitCompareExp(M2tParser.CompareExpContext ctx) {
		return support.createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitEqualityExp(M2tParser.EqualityExpContext ctx) {
		return support.createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitAndExp(M2tParser.AndExpContext ctx) {
		return support.createBinaryOperation("and",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitOrExp(M2tParser.OrExpContext ctx) {
		return support.createBinaryOperation("or",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitXorExp(M2tParser.XorExpContext ctx) {
		return support.createBinaryOperation("xor",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitImpliesExp(M2tParser.ImpliesExpContext ctx) {
		return support.createBinaryOperation("implies",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	// ==================== Navigation ====================

	@Override
	public OclExpression visitNavigationExp(M2tParser.NavigationExpContext ctx) {
		OclExpression source = (OclExpression) visit(ctx.expression());
		boolean isSafe = ctx.getChild(1).getText().equals("?.");

		M2tParser.PropertyOrCallSuffixContext suffix = ctx.propertyOrCallSuffix();
		if (suffix instanceof M2tParser.PropertySuffixContext propCtx) {
			String propName = identifierText(propCtx.identifier());
			return support.buildPropertyCall(source, propName, isSafe, false);
		} else if (suffix instanceof M2tParser.DotCallSuffixContext callCtx) {
			String opName = identifierText(callCtx.identifier());
			List<OclExpression> args = new ArrayList<>();
			if (callCtx.argumentList() != null) {
				for (M2tParser.ExpressionContext argCtx : callCtx.argumentList().expression()) {
					args.add((OclExpression) visit(argCtx));
				}
			}
			return support.buildDotOperationCall(source, opName, args, isSafe, false);
		}
		return source;
	}

	@Override
	public OclExpression visitArrowExp(M2tParser.ArrowExpContext ctx) {
		OclExpression source = (OclExpression) visit(ctx.expression());
		boolean isSafe = ctx.getChild(1).getText().equals("?->");

		M2tParser.IteratorOrOperationCallContext call = ctx.iteratorOrOperationCall();
		if (call instanceof M2tParser.IteratorCallContext iterCtx) {
			return visitIteratorCall(source, iterCtx, isSafe);
		} else if (call instanceof M2tParser.IterateCallContext iterateCtx) {
			return visitIterateCall(source, iterateCtx, isSafe);
		} else if (call instanceof M2tParser.CollectionOperationCallContext opCtx) {
			return visitCollectionOperationCall(source, opCtx, isSafe);
		}
		return source;
	}

	private IteratorExp visitIteratorCall(OclExpression source,
			M2tParser.IteratorCallContext ctx, boolean isSafe) {
		String iterName = identifierText(ctx.identifier());
		OclEnvironment savedEnv = env();

		OclType elementType = support.inferElementType(source);
		M2tParser.IteratorVariablesContext varsCtx = ctx.iteratorVariables();
		List<Variable> iterVars = new ArrayList<>();
		for (int i = 0; i < varsCtx.identifier().size(); i++) {
			OclType varType = (i < varsCtx.typeExpression().size()
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
			M2tParser.IterateCallContext ctx, boolean isSafe) {
		OclEnvironment savedEnv = env();

		OclType iterType = ctx.typeExpression().size() > 0
				? resolveTypeExpression(ctx.typeExpression(0))
				: support.inferElementType(source);
		Variable iterVar = support.createVariable(
				identifierText(ctx.identifier(1)), iterType, null);

		OclType accType = ctx.typeExpression().size() > 1
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
			M2tParser.CollectionOperationCallContext ctx, boolean isSafe) {
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
			for (M2tParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				args.add((OclExpression) visit(argCtx));
			}
		}
		return support.buildCollectionOperationCall(source, opName, args, isSafe);
	}

	// ==================== If / Let (OCL inline expressions) ====================

	@Override
	public IfExp visitOclIfExp(M2tParser.OclIfExpContext ctx) {
		// [if cond then thenExpr (elseif cond then thenExpr)* else elseExpr endif]
		// The grammar uses: expression KW_THEN expression (KW_ELSEIF expression KW_THEN expression)* KW_ELSE expression KW_ENDIF
		// Expressions are interleaved: [0]=condition, [1]=then, [2..]=elseif pairs, last=else
		List<M2tParser.ExpressionContext> exprs = ctx.expression();
		OclExpression condition = (OclExpression) visit(exprs.get(0));
		OclExpression thenExp = (OclExpression) visit(exprs.get(1));

		// Count elseif pairs: everything between index 2 and last-1 (exclusive) in pairs
		int elseifCount = (exprs.size() - 3) / 2; // 3 base (cond, then, else) + 2 per elseif
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
	public LetExp visitOclLetExp(M2tParser.OclLetExpContext ctx) {
		List<M2tParser.LetBindingContext> bindings = ctx.letBinding();
		OclEnvironment savedEnv = env();

		LetExp outermost = null;
		LetExp current = null;

		for (M2tParser.LetBindingContext bindCtx : bindings) {
			OclType type = bindCtx.typeExpression() != null
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
	public OclExpression visitSelfExp(M2tParser.SelfExpContext ctx) {
		return support.buildSelfExp();
	}

	@Override
	public OclExpression visitParenExp(M2tParser.ParenExpContext ctx) {
		return (OclExpression) visit(ctx.expression());
	}

	@Override
	public OclExpression visitPrimaryExp(M2tParser.PrimaryExpContext ctx) {
		return (OclExpression) visit(ctx.primaryExpression());
	}

	@Override
	public OclExpression visitLiteralExp(M2tParser.LiteralExpContext ctx) {
		return (OclExpression) visit(ctx.literalExpression());
	}

	@Override
	public TypeExp visitPrimitiveTypeExp(M2tParser.PrimitiveTypeExpContext ctx) {
		return support.buildTypeExp(support.createPrimitiveType(ctx.primitiveType().getText()));
	}

	@Override
	public TypeExp visitCollectionTypeExp(M2tParser.CollectionTypeExpContext ctx) {
		return support.buildTypeExp(resolveCollectionType(ctx.collectionType()));
	}

	@Override
	public TypeExp visitMapTypeExp(M2tParser.MapTypeExpContext ctx) {
		return support.buildTypeExp(resolveMapType(ctx.mapType()));
	}

	@Override
	public TypeExp visitTupleTypeExp(M2tParser.TupleTypeExpContext ctx) {
		return support.buildTypeExp(resolveTupleType(ctx.tupleType()));
	}

	@Override
	public OperationCallExp visitOperationCallExp(M2tParser.OperationCallExpContext ctx) {
		List<String> segments = pathNameSegments(ctx.pathName());
		String opName = segments.get(segments.size() - 1);

		List<OclExpression> args = new ArrayList<>();
		if (ctx.argumentList() != null) {
			for (M2tParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				args.add((OclExpression) visit(argCtx));
			}
		}
		return support.buildImplicitOperationCall(opName, args, false);
	}

	// ==================== Type Resolution ====================

	OclType resolveTypeExpression(M2tParser.TypeExpressionContext ctx) {
		// The module builder resolves parameter and query types through here, without visiting
		// the node itself — so this is where the position has to be set (#110).
		support.positionAt(ctx);
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
			return support.buildTypeFromPath(pathNameSegments(ctx.pathName()));
		}
		return null;
	}

	private OclType resolveCollectionType(M2tParser.CollectionTypeContext ctx) {
		return support.buildCollectionType(ctx.collectionKind().getText(),
				resolveTypeExpression(ctx.typeExpression()));
	}

	private OclType resolveMapType(M2tParser.MapTypeContext ctx) {
		return support.buildMapType(
				resolveTypeExpression(ctx.typeExpression(0)),
				resolveTypeExpression(ctx.typeExpression(1)));
	}

	private OclType resolveTupleType(M2tParser.TupleTypeContext ctx) {
		List<String> names = new ArrayList<>();
		List<OclType> types = new ArrayList<>();
		for (M2tParser.TupleTypePartContext partCtx : ctx.tupleTypePart()) {
			names.add(identifierText(partCtx.identifier()));
			types.add(resolveTypeExpression(partCtx.typeExpression()));
		}
		return support.buildTupleType(names, types);
	}

	// ==================== Utility Methods (M2T-grammar-specific) ====================

	static String identifierText(M2tParser.IdentifierContext ctx) {
		if (ctx.ESCAPED_IDENTIFIER() != null) {
			return AbstractExpressionBuilder.extractEscapedIdentifier(
					ctx.ESCAPED_IDENTIFIER().getText());
		}
		return ctx.getText();
	}

	static List<String> pathNameSegments(M2tParser.PathNameContext ctx) {
		List<String> segments = new ArrayList<>();
		for (var id : ctx.identifier()) {
			segments.add(identifierText(id));
		}
		return segments;
	}
}
