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
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;

/**
 * Visitor that transforms an ANTLR4 parse tree into an EMF OCL AST.
 *
 * <p>Implements the two-phase parsing strategy (D8): ANTLR4 produces the parse tree,
 * this visitor constructs {@link OclExpression} instances from {@code ocl.model}.
 *
 * <p>Type resolution is performed during AST construction (Option B) so that
 * the returned expression tree is fully typed for efficient repeated evaluation.
 *
 * <p>Delegates to {@link AbstractExpressionBuilder} for all context-free helper
 * methods (type resolution, name resolution, expression creation). This enables
 * code reuse with the M2T parser.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclAstBuilder extends OclBaseVisitor<Object> {

	private static final OclFactory FACTORY = OclFactory.eINSTANCE;

	final AbstractExpressionBuilder support;

	OclAstBuilder(EClassifier contextType) {
		this(contextType, null);
	}

	OclAstBuilder(EClassifier contextType, EPackage.Registry packageRegistry) {
		this.support = new AbstractExpressionBuilder(contextType, packageRegistry);
	}

	/**
	 * Registers an alias name for the {@code self} variable (G-10: §12.12.5 form [B]).
	 * After this call, both the alias and {@code "self"} resolve to the same variable.
	 */
	void registerSelfAlias(String alias) {
		support.registerSelfAlias(alias);
	}

	// ==================== Environment Convenience ====================

	private OclEnvironment env() {
		return support.getEnvironment();
	}

	private void setEnv(OclEnvironment env) {
		support.setEnvironment(env);
	}

	// ==================== Entry Points ====================

	@Override
	public OclExpression visitExpressionEntry(OclParser.ExpressionEntryContext ctx) {
		return (OclExpression) visit(ctx.expression());
	}

	// ==================== Literals ====================

	@Override
	public IntegerLiteralExp visitIntegerLiteral(OclParser.IntegerLiteralContext ctx) {
		return support.buildIntegerLiteral(ctx.INTEGER_LITERAL().getText());
	}

	@Override
	public RealLiteralExp visitRealLiteral(OclParser.RealLiteralContext ctx) {
		return support.buildRealLiteral(ctx.REAL_LITERAL().getText());
	}

	@Override
	public StringLiteralExp visitStringLit(OclParser.StringLitContext ctx) {
		return (StringLiteralExp) visit(ctx.stringLiteral_());
	}

	@Override
	public StringLiteralExp visitStringLiteral_(OclParser.StringLiteral_Context ctx) {
		List<String> rawTokens = new ArrayList<>();
		for (var token : ctx.STRING_LITERAL()) {
			rawTokens.add(token.getText());
		}
		return support.buildStringLiteral(rawTokens);
	}

	@Override
	public BooleanLiteralExp visitTrueLiteral(OclParser.TrueLiteralContext ctx) {
		return support.buildBooleanLiteral(true);
	}

	@Override
	public BooleanLiteralExp visitFalseLiteral(OclParser.FalseLiteralContext ctx) {
		return support.buildBooleanLiteral(false);
	}

	@Override
	public NullLiteralExp visitNullLiteral(OclParser.NullLiteralContext ctx) {
		return support.buildNullLiteral();
	}

	@Override
	public InvalidLiteralExp visitInvalidLiteral(OclParser.InvalidLiteralContext ctx) {
		return support.buildInvalidLiteral();
	}

	@Override
	public UnlimitedNaturalLiteralExp visitUnlimitedNaturalLiteral(
			OclParser.UnlimitedNaturalLiteralContext ctx) {
		return support.buildUnlimitedNaturalLiteral();
	}

	@Override
	public OclExpression visitPathNameExp(OclParser.PathNameExpContext ctx) {
		return support.buildPathNameExp(pathNameSegments(ctx.pathName()));
	}

	// ==================== Collection Literals ====================

	@Override
	public CollectionLiteralExp visitCollectionLit(OclParser.CollectionLitContext ctx) {
		return (CollectionLiteralExp) visit(ctx.collectionLiteral());
	}

	@Override
	public CollectionLiteralExp visitCollectionLiteral(OclParser.CollectionLiteralContext ctx) {
		List<CollectionLiteralPart> parts = new ArrayList<>();
		if (ctx.collectionLiteralPart() != null) {
			for (OclParser.CollectionLiteralPartContext partCtx : ctx.collectionLiteralPart()) {
				parts.add(visitCollectionLiteralPart(partCtx));
			}
		}
		return support.buildCollectionLiteral(ctx.collectionKind().getText(), parts);
	}

	@Override
	public CollectionLiteralPart visitCollectionLiteralPart(
			OclParser.CollectionLiteralPartContext ctx) {
		if (ctx.expression().size() == 2) {
			return support.buildCollectionRange(
					(OclExpression) visit(ctx.expression(0)),
					(OclExpression) visit(ctx.expression(1)));
		}
		return support.buildCollectionItem((OclExpression) visit(ctx.expression(0)));
	}

	// ==================== Tuple Literals ====================

	@Override
	public TupleLiteralExp visitTupleLit(OclParser.TupleLitContext ctx) {
		return (TupleLiteralExp) visit(ctx.tupleLiteral());
	}

	@Override
	public TupleLiteralExp visitTupleLiteral(OclParser.TupleLiteralContext ctx) {
		List<TupleLiteralPart> parts = new ArrayList<>();
		for (OclParser.TupleLiteralPartContext partCtx : ctx.tupleLiteralPart()) {
			OclType type = partCtx.typeExpression() != null
					? resolveTypeExpression(partCtx.typeExpression()) : null;
			parts.add(support.buildTupleLiteralPart(
					identifierText(partCtx.identifier()),
					type,
					(OclExpression) visit(partCtx.expression())));
		}
		return support.buildTupleLiteral(parts);
	}

	// ==================== Map Literals (v2.5) ====================

	@Override
	public MapLiteralExp visitMapLit(OclParser.MapLitContext ctx) {
		return (MapLiteralExp) visit(ctx.mapLiteral());
	}

	@Override
	public MapLiteralExp visitMapLiteral(OclParser.MapLiteralContext ctx) {
		List<MapLiteralPart> parts = new ArrayList<>();
		if (ctx.mapLiteralPart() != null) {
			for (OclParser.MapLiteralPartContext partCtx : ctx.mapLiteralPart()) {
				parts.add(support.buildMapLiteralPart(
						(OclExpression) visit(partCtx.expression(0)),
						(OclExpression) visit(partCtx.expression(1))));
			}
		}
		return support.buildMapLiteral(parts);
	}

	// ==================== Unary Expressions ====================

	@Override
	public OperationCallExp visitNotExp(OclParser.NotExpContext ctx) {
		return support.createUnaryOperation("not", (OclExpression) visit(ctx.expression()));
	}

	@Override
	public OperationCallExp visitUnaryMinusExp(OclParser.UnaryMinusExpContext ctx) {
		return support.createUnaryOperation("-", (OclExpression) visit(ctx.expression()));
	}

	// ==================== Binary Expressions ====================

	@Override
	public OperationCallExp visitMultExp(OclParser.MultExpContext ctx) {
		return support.createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitAddExp(OclParser.AddExpContext ctx) {
		return support.createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitCompareExp(OclParser.CompareExpContext ctx) {
		return support.createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitEqualityExp(OclParser.EqualityExpContext ctx) {
		return support.createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitAndExp(OclParser.AndExpContext ctx) {
		return support.createBinaryOperation("and",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitOrExp(OclParser.OrExpContext ctx) {
		return support.createBinaryOperation("or",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitXorExp(OclParser.XorExpContext ctx) {
		return support.createBinaryOperation("xor",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitImpliesExp(OclParser.ImpliesExpContext ctx) {
		return support.createBinaryOperation("implies",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	// ==================== Navigation (. and ->) ====================

	@Override
	public OclExpression visitNavigationExp(OclParser.NavigationExpContext ctx) {
		OclExpression source = (OclExpression) visit(ctx.expression());
		boolean isSafe = ctx.getChild(1).getText().equals("?.");

		OclParser.PropertyOrCallSuffixContext suffix = ctx.propertyOrCallSuffix();
		if (suffix instanceof OclParser.PropertySuffixContext propCtx) {
			String propName = identifierText(propCtx.identifier());
			boolean isPre = propCtx.isMarkedPre() != null;
			return support.buildPropertyCall(source, propName, isSafe, isPre);
		} else if (suffix instanceof OclParser.DotCallSuffixContext callCtx) {
			String opName = identifierText(callCtx.identifier());
			boolean isPre = callCtx.isMarkedPre() != null;
			List<OclExpression> args = new ArrayList<>();
			if (callCtx.argumentList() != null) {
				for (OclParser.ExpressionContext argCtx : callCtx.argumentList().expression()) {
					args.add((OclExpression) visit(argCtx));
				}
			}
			return support.buildDotOperationCall(source, opName, args, isSafe, isPre);
		}
		return source;
	}

	@Override
	public OclExpression visitArrowExp(OclParser.ArrowExpContext ctx) {
		OclExpression source = (OclExpression) visit(ctx.expression());
		boolean isSafe = ctx.getChild(1).getText().equals("?->");

		OclParser.IteratorOrOperationCallContext call = ctx.iteratorOrOperationCall();
		if (call instanceof OclParser.IteratorCallContext iterCtx) {
			return visitIteratorCall(source, iterCtx, isSafe);
		} else if (call instanceof OclParser.IterateCallContext iterateCtx) {
			return visitIterateCall(source, iterateCtx, isSafe);
		} else if (call instanceof OclParser.CollectionOperationCallContext opCtx) {
			return visitCollectionOperationCall(source, opCtx, isSafe);
		}
		return source;
	}

	private IteratorExp visitIteratorCall(OclExpression source,
			OclParser.IteratorCallContext ctx, boolean isSafe) {
		String iterName = identifierText(ctx.identifier());
		OclEnvironment savedEnv = env();

		OclType elementType = support.inferElementType(source);

		OclParser.IteratorVariablesContext varsCtx = ctx.iteratorVariables();
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
			OclParser.IterateCallContext ctx, boolean isSafe) {
		OclEnvironment savedEnv = env();

		// Iterator variable
		OclType iterType = ctx.iterType != null
				? resolveTypeExpression(ctx.iterType)
				: support.inferElementType(source);
		Variable iterVar = support.createVariable(
				identifierText(ctx.identifier(1)), iterType, null);

		// Accumulator variable
		OclType accType = ctx.accType != null
				? resolveTypeExpression(ctx.accType) : null;
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
			OclParser.CollectionOperationCallContext ctx, boolean isSafe) {
		String opName = identifierText(ctx.identifier());

		// OCL shorthand: ->any(expr) is ->any(_implicit | expr)
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
			for (OclParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				args.add((OclExpression) visit(argCtx));
			}
		}
		return support.buildCollectionOperationCall(source, opName, args, isSafe);
	}

	// ==================== If / Let ====================

	@Override
	public IfExp visitIfExp(OclParser.IfExpContext ctx) {
		OclExpression condition = (OclExpression) visit(ctx.condition);
		OclExpression thenExp = (OclExpression) visit(ctx.thenExp);

		List<OclExpression> elseIfConditions = null;
		List<OclExpression> elseIfExps = null;
		if (ctx.elseIfCondition != null && !ctx.elseIfCondition.isEmpty()) {
			elseIfConditions = new ArrayList<>();
			elseIfExps = new ArrayList<>();
			for (int i = 0; i < ctx.elseIfCondition.size(); i++) {
				elseIfConditions.add((OclExpression) visit(ctx.elseIfCondition.get(i)));
				elseIfExps.add((OclExpression) visit(ctx.elseIfExp.get(i)));
			}
		}
		OclExpression elseExp = (OclExpression) visit(ctx.elseExp);

		return support.buildIfExp(condition, thenExp, elseIfConditions, elseIfExps, elseExp);
	}

	@Override
	public LetExp visitLetExp(OclParser.LetExpContext ctx) {
		List<OclParser.LetBindingContext> bindings = ctx.letBinding();
		OclEnvironment savedEnv = env();

		LetExp outermost = null;
		LetExp current = null;

		for (OclParser.LetBindingContext bindCtx : bindings) {
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
	public OclExpression visitSelfExp(OclParser.SelfExpContext ctx) {
		return support.buildSelfExp();
	}

	@Override
	public OclExpression visitParenExp(OclParser.ParenExpContext ctx) {
		return (OclExpression) visit(ctx.expression());
	}

	@Override
	public OclExpression visitPrimaryExp(OclParser.PrimaryExpContext ctx) {
		return (OclExpression) visit(ctx.primaryExpression());
	}

	@Override
	public OclExpression visitLiteralExp(OclParser.LiteralExpContext ctx) {
		return (OclExpression) visit(ctx.literalExpression());
	}

	@Override
	public TypeExp visitPrimitiveTypeExp(OclParser.PrimitiveTypeExpContext ctx) {
		return support.buildTypeExp(support.createPrimitiveType(ctx.primitiveType().getText()));
	}

	@Override
	public TypeExp visitCollectionTypeExp(OclParser.CollectionTypeExpContext ctx) {
		return support.buildTypeExp(resolveCollectionType(ctx.collectionType()));
	}

	@Override
	public TypeExp visitMapTypeExp(OclParser.MapTypeExpContext ctx) {
		return support.buildTypeExp(resolveMapType(ctx.mapType()));
	}

	@Override
	public TypeExp visitTupleTypeExp(OclParser.TupleTypeExpContext ctx) {
		return support.buildTypeExp(resolveTupleType(ctx.tupleType()));
	}

	@Override
	public OperationCallExp visitOperationCallExp(OclParser.OperationCallExpContext ctx) {
		List<String> segments = pathNameSegments(ctx.pathName());
		String opName = segments.get(segments.size() - 1);

		List<OclExpression> args = new ArrayList<>();
		if (ctx.argumentList() != null) {
			for (OclParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				args.add((OclExpression) visit(argCtx));
			}
		}

		OperationCallExp exp = support.buildImplicitOperationCall(opName, args,
				ctx.isMarkedPre() != null);
		return exp;
	}

	// ==================== Type Resolution (parser-context-specific) ====================

	OclType resolveTypeExpression(OclParser.TypeExpressionContext ctx) {
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

	private OclType resolveCollectionType(OclParser.CollectionTypeContext ctx) {
		String kindText = ctx.collectionKind().getText();
		OclType elementType = resolveTypeExpression(ctx.typeExpression());
		return support.buildCollectionType(kindText, elementType);
	}

	private OclType resolveMapType(OclParser.MapTypeContext ctx) {
		OclType keyType = resolveTypeExpression(ctx.typeExpression(0));
		OclType valueType = resolveTypeExpression(ctx.typeExpression(1));
		return support.buildMapType(keyType, valueType);
	}

	private OclType resolveTupleType(OclParser.TupleTypeContext ctx) {
		List<String> partNames = new ArrayList<>();
		List<OclType> partTypes = new ArrayList<>();
		for (OclParser.TupleTypePartContext partCtx : ctx.tupleTypePart()) {
			partNames.add(identifierText(partCtx.identifier()));
			partTypes.add(resolveTypeExpression(partCtx.typeExpression()));
		}
		return support.buildTupleType(partNames, partTypes);
	}

	// ==================== Utility Methods (OCL-grammar-specific) ====================

	/**
	 * Extracts the text of an {@code identifier} parser rule, handling both
	 * plain {@code IDENTIFIER} tokens and escaped identifiers {@code _'keyword'}.
	 */
	static String identifierText(OclParser.IdentifierContext ctx) {
		if (ctx.ESCAPED_IDENTIFIER() != null) {
			return AbstractExpressionBuilder.extractEscapedIdentifier(
					ctx.ESCAPED_IDENTIFIER().getText());
		}
		return ctx.IDENTIFIER().getText();
	}

	private List<String> pathNameSegments(OclParser.PathNameContext ctx) {
		List<String> segments = new ArrayList<>();
		for (var id : ctx.identifier()) {
			segments.add(identifierText(id));
		}
		return segments;
	}
}
