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
package org.eclipse.fennec.m2m.ocl.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
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
import org.eclipse.fennec.m2m.model.ocl.NullLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.model.ocl.OclFactory;
import org.eclipse.fennec.m2m.model.ocl.OclType;
import org.eclipse.fennec.m2m.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2m.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2m.model.ocl.RealLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.TupleLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.TupleLiteralPart;
import org.eclipse.fennec.m2m.model.ocl.TypeExp;
import org.eclipse.fennec.m2m.model.ocl.UnlimitedNaturalLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.Variable;
import org.eclipse.fennec.m2m.model.ocl.VariableExp;

/**
 * Visitor that transforms an ANTLR4 parse tree into an EMF OCL AST.
 *
 * <p>Implements the two-phase parsing strategy (D8): ANTLR4 produces the parse tree,
 * this visitor constructs {@link OclExpression} instances from {@code ocl.model}.
 *
 * <p>Type resolution is performed during AST construction (Option B) so that
 * the returned expression tree is fully typed for efficient repeated evaluation.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclAstBuilder extends OclBaseVisitor<Object> {

	private static final OclFactory FACTORY = OclFactory.eINSTANCE;

	private final EClassifier contextType;
	private final EPackage.Registry packageRegistry;
	private OclEnvironment environment;

	OclAstBuilder(EClassifier contextType) {
		this(contextType, null);
	}

	OclAstBuilder(EClassifier contextType, EPackage.Registry packageRegistry) {
		this.contextType = contextType;
		this.packageRegistry = packageRegistry;
		Variable selfVar = FACTORY.createVariable();
		selfVar.setName("self");
		selfVar.setType(createClassifierType(contextType));
		this.environment = OclEnvironment.root(selfVar);
	}

	// ==================== Entry Points ====================

	@Override
	public OclExpression visitExpressionEntry(OclParser.ExpressionEntryContext ctx) {
		return (OclExpression) visit(ctx.expression());
	}

	// ==================== Literals ====================

	@Override
	public IntegerLiteralExp visitIntegerLiteral(OclParser.IntegerLiteralContext ctx) {
		IntegerLiteralExp exp = FACTORY.createIntegerLiteralExp();
		exp.setIntegerSymbol(Long.parseLong(ctx.INTEGER_LITERAL().getText()));
		return exp;
	}

	@Override
	public RealLiteralExp visitRealLiteral(OclParser.RealLiteralContext ctx) {
		RealLiteralExp exp = FACTORY.createRealLiteralExp();
		exp.setRealSymbol(Double.parseDouble(ctx.REAL_LITERAL().getText()));
		return exp;
	}

	@Override
	public StringLiteralExp visitStringLiteral(OclParser.StringLiteralContext ctx) {
		StringLiteralExp exp = FACTORY.createStringLiteralExp();
		String text = ctx.STRING_LITERAL().getText();
		// Remove surrounding quotes and unescape
		exp.setStringSymbol(unescapeString(text.substring(1, text.length() - 1)));
		return exp;
	}

	@Override
	public BooleanLiteralExp visitTrueLiteral(OclParser.TrueLiteralContext ctx) {
		BooleanLiteralExp exp = FACTORY.createBooleanLiteralExp();
		exp.setBooleanSymbol(true);
		return exp;
	}

	@Override
	public BooleanLiteralExp visitFalseLiteral(OclParser.FalseLiteralContext ctx) {
		BooleanLiteralExp exp = FACTORY.createBooleanLiteralExp();
		exp.setBooleanSymbol(false);
		return exp;
	}

	@Override
	public NullLiteralExp visitNullLiteral(OclParser.NullLiteralContext ctx) {
		return FACTORY.createNullLiteralExp();
	}

	@Override
	public InvalidLiteralExp visitInvalidLiteral(OclParser.InvalidLiteralContext ctx) {
		return FACTORY.createInvalidLiteralExp();
	}

	@Override
	public UnlimitedNaturalLiteralExp visitUnlimitedNaturalLiteral(
			OclParser.UnlimitedNaturalLiteralContext ctx) {
		UnlimitedNaturalLiteralExp exp = FACTORY.createUnlimitedNaturalLiteralExp();
		exp.setUnlimitedNaturalSymbol(-1L); // * = unlimited
		return exp;
	}

	@Override
	public OclExpression visitPathNameExp(OclParser.PathNameExpContext ctx) {
		List<String> segments = pathNameSegments(ctx.pathName());

		// Check if it's a variable reference (single name)
		if (segments.size() == 1) {
			String name = segments.get(0);
			return environment.lookup(name)
					.<OclExpression>map(this::createVariableExp)
					.orElseGet(() -> resolveImplicitProperty(name));
		}

		// Check if it's an enum literal (Package::Literal)
		if (segments.size() == 2) {
			// Could be enum literal or qualified type — try enum first
			EnumLiteralExp enumExp = tryResolveEnumLiteral(segments);
			if (enumExp != null) {
				return enumExp;
			}
		}

		// Qualified name — resolve as type expression or navigation
		return resolveQualifiedName(segments);
	}

	// ==================== Collection Literals ====================

	@Override
	public CollectionLiteralExp visitCollectionLit(OclParser.CollectionLitContext ctx) {
		return (CollectionLiteralExp) visit(ctx.collectionLiteral());
	}

	@Override
	public CollectionLiteralExp visitCollectionLiteral(OclParser.CollectionLiteralContext ctx) {
		CollectionLiteralExp exp = FACTORY.createCollectionLiteralExp();
		exp.setKind(resolveCollectionKind(ctx.collectionKind().getText()));
		if (ctx.collectionLiteralPart() != null) {
			for (OclParser.CollectionLiteralPartContext partCtx : ctx.collectionLiteralPart()) {
				exp.getOwnedParts().add(visitCollectionLiteralPart(partCtx));
			}
		}
		return exp;
	}

	@Override
	public CollectionLiteralPart visitCollectionLiteralPart(
			OclParser.CollectionLiteralPartContext ctx) {
		if (ctx.expression().size() == 2) {
			// Range: first..last
			CollectionRange range = FACTORY.createCollectionRange();
			range.setOwnedFirst((OclExpression) visit(ctx.expression(0)));
			range.setOwnedLast((OclExpression) visit(ctx.expression(1)));
			return range;
		}
		// Single item
		CollectionItem item = FACTORY.createCollectionItem();
		item.setOwnedItem((OclExpression) visit(ctx.expression(0)));
		return item;
	}

	// ==================== Tuple Literals ====================

	@Override
	public TupleLiteralExp visitTupleLit(OclParser.TupleLitContext ctx) {
		return (TupleLiteralExp) visit(ctx.tupleLiteral());
	}

	@Override
	public TupleLiteralExp visitTupleLiteral(OclParser.TupleLiteralContext ctx) {
		TupleLiteralExp exp = FACTORY.createTupleLiteralExp();
		for (OclParser.TupleLiteralPartContext partCtx : ctx.tupleLiteralPart()) {
			TupleLiteralPart part = FACTORY.createTupleLiteralPart();
			part.setName(partCtx.IDENTIFIER().getText());
			if (partCtx.typeExpression() != null) {
				part.setType(resolveTypeExpression(partCtx.typeExpression()));
			}
			part.setOwnedInit((OclExpression) visit(partCtx.expression()));
			exp.getOwnedParts().add(part);
		}
		return exp;
	}

	// ==================== Map Literals (v2.5) ====================

	@Override
	public MapLiteralExp visitMapLit(OclParser.MapLitContext ctx) {
		return (MapLiteralExp) visit(ctx.mapLiteral());
	}

	@Override
	public MapLiteralExp visitMapLiteral(OclParser.MapLiteralContext ctx) {
		MapLiteralExp exp = FACTORY.createMapLiteralExp();
		if (ctx.mapLiteralPart() != null) {
			for (OclParser.MapLiteralPartContext partCtx : ctx.mapLiteralPart()) {
				MapLiteralPart part = FACTORY.createMapLiteralPart();
				part.setOwnedKey((OclExpression) visit(partCtx.expression(0)));
				part.setOwnedValue((OclExpression) visit(partCtx.expression(1)));
				exp.getOwnedParts().add(part);
			}
		}
		return exp;
	}

	// ==================== Unary Expressions ====================

	@Override
	public OperationCallExp visitNotExp(OclParser.NotExpContext ctx) {
		return createUnaryOperation("not", (OclExpression) visit(ctx.expression()));
	}

	@Override
	public OperationCallExp visitUnaryMinusExp(OclParser.UnaryMinusExpContext ctx) {
		return createUnaryOperation("-", (OclExpression) visit(ctx.expression()));
	}

	// ==================== Binary Expressions ====================

	@Override
	public OperationCallExp visitMultExp(OclParser.MultExpContext ctx) {
		return createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitAddExp(OclParser.AddExpContext ctx) {
		return createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitCompareExp(OclParser.CompareExpContext ctx) {
		return createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitEqualityExp(OclParser.EqualityExpContext ctx) {
		return createBinaryOperation(ctx.op.getText(),
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitAndExp(OclParser.AndExpContext ctx) {
		return createBinaryOperation("and",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitOrExp(OclParser.OrExpContext ctx) {
		return createBinaryOperation("or",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitXorExp(OclParser.XorExpContext ctx) {
		return createBinaryOperation("xor",
				(OclExpression) visit(ctx.expression(0)),
				(OclExpression) visit(ctx.expression(1)));
	}

	@Override
	public OperationCallExp visitImpliesExp(OclParser.ImpliesExpContext ctx) {
		return createBinaryOperation("implies",
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
			return createPropertyCall(source, propCtx, isSafe);
		} else if (suffix instanceof OclParser.DotCallSuffixContext callCtx) {
			return createDotOperationCall(source, callCtx, isSafe);
		}
		return source;
	}

	@Override
	public OclExpression visitArrowExp(OclParser.ArrowExpContext ctx) {
		OclExpression source = (OclExpression) visit(ctx.expression());
		boolean isSafe = ctx.getChild(1).getText().equals("?->");

		OclParser.IteratorOrOperationCallContext call = ctx.iteratorOrOperationCall();
		if (call instanceof OclParser.IteratorCallContext iterCtx) {
			return createIteratorExp(source, iterCtx, isSafe);
		} else if (call instanceof OclParser.IterateCallContext iterateCtx) {
			return createIterateExp(source, iterateCtx, isSafe);
		} else if (call instanceof OclParser.CollectionOperationCallContext opCtx) {
			return createCollectionOperation(source, opCtx, isSafe);
		}
		return source;
	}

	// ==================== If / Let ====================

	@Override
	public IfExp visitIfExp(OclParser.IfExpContext ctx) {
		IfExp ifExp = FACTORY.createIfExp();
		ifExp.setOwnedCondition((OclExpression) visit(ctx.condition));
		ifExp.setOwnedThen((OclExpression) visit(ctx.thenExp));

		// Handle elseif chain by nesting IfExps
		if (ctx.elseIfCondition != null && !ctx.elseIfCondition.isEmpty()) {
			IfExp current = ifExp;
			for (int i = 0; i < ctx.elseIfCondition.size(); i++) {
				IfExp nested = FACTORY.createIfExp();
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
	public LetExp visitLetExp(OclParser.LetExpContext ctx) {
		// Chain multiple let bindings: let a=1, b=2 in body → LetExp(a, LetExp(b, body))
		List<OclParser.LetBindingContext> bindings = ctx.letBinding();
		OclEnvironment savedEnv = this.environment;

		LetExp outermost = null;
		LetExp current = null;

		for (OclParser.LetBindingContext bindCtx : bindings) {
			Variable var = FACTORY.createVariable();
			var.setName(bindCtx.IDENTIFIER().getText());
			if (bindCtx.typeExpression() != null) {
				var.setType(resolveTypeExpression(bindCtx.typeExpression()));
			}
			var.setOwnedInit((OclExpression) visit(bindCtx.expression()));

			this.environment = this.environment.nested(var);

			LetExp letExp = FACTORY.createLetExp();
			letExp.setOwnedVariable(var);

			if (outermost == null) {
				outermost = letExp;
			} else {
				current.setOwnedIn(letExp);
			}
			current = letExp;
		}

		// The innermost let's body is the 'in' expression
		current.setOwnedIn((OclExpression) visit(ctx.expression()));

		this.environment = savedEnv;
		return outermost;
	}

	// ==================== Primary Expressions ====================

	@Override
	public OclExpression visitSelfExp(OclParser.SelfExpContext ctx) {
		return createVariableExp(environment.lookup("self").orElseThrow());
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
		TypeExp exp = FACTORY.createTypeExp();
		exp.setReferredType(createPrimitiveType(ctx.primitiveType().getText()));
		return exp;
	}

	@Override
	public TypeExp visitCollectionTypeExp(OclParser.CollectionTypeExpContext ctx) {
		TypeExp exp = FACTORY.createTypeExp();
		exp.setReferredType(resolveCollectionType(ctx.collectionType()));
		return exp;
	}

	@Override
	public TypeExp visitMapTypeExp(OclParser.MapTypeExpContext ctx) {
		TypeExp exp = FACTORY.createTypeExp();
		exp.setReferredType(resolveMapType(ctx.mapType()));
		return exp;
	}

	@Override
	public TypeExp visitTupleTypeExp(OclParser.TupleTypeExpContext ctx) {
		TypeExp exp = FACTORY.createTypeExp();
		exp.setReferredType(resolveTupleType(ctx.tupleType()));
		return exp;
	}

	@Override
	public OperationCallExp visitOperationCallExp(OclParser.OperationCallExpContext ctx) {
		List<String> segments = pathNameSegments(ctx.pathName());
		String opName = segments.get(segments.size() - 1);

		OperationCallExp exp = FACTORY.createOperationCallExp();
		exp.setIsImplicit(true);

		// Implicit source: self
		VariableExp selfRef = createVariableExp(environment.lookup("self").orElseThrow());
		exp.setOwnedSource(selfRef);

		if (ctx.argumentList() != null) {
			for (OclParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				exp.getOwnedArguments().add((OclExpression) visit(argCtx));
			}
		}

		// Resolve operation on the context type
		resolveOperation(exp, opName);
		return exp;
	}

	// ==================== Helper Methods ====================

	private OperationCallExp createUnaryOperation(String opName, OclExpression operand) {
		OperationCallExp exp = FACTORY.createOperationCallExp();
		exp.setOwnedSource(operand);
		resolveOperation(exp, opName);
		return exp;
	}

	private OperationCallExp createBinaryOperation(String opName, OclExpression left,
			OclExpression right) {
		OperationCallExp exp = FACTORY.createOperationCallExp();
		exp.setOwnedSource(left);
		exp.getOwnedArguments().add(right);
		resolveOperation(exp, opName);
		return exp;
	}

	private OclExpression createPropertyCall(OclExpression source,
			OclParser.PropertySuffixContext ctx, boolean isSafe) {
		// Check if source is a collection → implicit collect (OCL v2.4 §7.6.1)
		if (source.getType() instanceof CollectionType sourceColType) {
			return createImplicitCollect(source, ctx, isSafe, sourceColType);
		}

		PropertyCallExp exp = FACTORY.createPropertyCallExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		exp.setIsPre(ctx.isMarkedPre() != null);

		String propName = ctx.IDENTIFIER().getText();
		resolveProperty(exp, propName);
		return exp;
	}

	private IteratorExp createImplicitCollect(OclExpression source,
			OclParser.PropertySuffixContext ctx, boolean isSafe,
			CollectionType sourceColType) {
		// Create iterator variable typed to the element type
		Variable iterVar = FACTORY.createVariable();
		iterVar.setName("_implicit");
		if (sourceColType.getElementType() != null) {
			iterVar.setType(copyType(sourceColType.getElementType()));
		}

		// Create the body: iterVar.propName
		VariableExp iterRef = FACTORY.createVariableExp();
		iterRef.setReferredVariable(iterVar);
		if (iterVar.getType() != null) {
			iterRef.setType(copyType(iterVar.getType()));
		}

		PropertyCallExp bodyExp = FACTORY.createPropertyCallExp();
		bodyExp.setOwnedSource(iterRef);
		bodyExp.setIsSafe(isSafe);
		bodyExp.setIsPre(ctx.isMarkedPre() != null);
		String propName = ctx.IDENTIFIER().getText();
		resolveProperty(bodyExp, propName);

		// Build the IteratorExp("collect")
		IteratorExp collectExp = FACTORY.createIteratorExp();
		collectExp.setOwnedSource(source);
		collectExp.setName("collect");
		collectExp.getOwnedIterators().add(iterVar);
		collectExp.setOwnedBody(bodyExp);

		// Result type: the body type determines the element type of the result Sequence
		OclType bodyType = bodyExp.getType();
		if (bodyType != null) {
			CollectionType resultType = FACTORY.createCollectionType();
			resultType.setElementType(copyType(bodyType));
			resultType.setKind(CollectionKind.SEQUENCE); // collect always produces Sequence
			collectExp.setType(resultType);
		}

		return collectExp;
	}

	private OclType copyType(OclType type) {
		if (type instanceof ClassifierType ct) {
			return createClassifierType(ct.getReferredClassifier());
		}
		if (type instanceof CollectionType colType) {
			CollectionType copy = FACTORY.createCollectionType();
			if (colType.getElementType() != null) {
				copy.setElementType(copyType(colType.getElementType()));
			}
			copy.setKind(colType.getKind());
			return copy;
		}
		return null;
	}

	private OperationCallExp createDotOperationCall(OclExpression source,
			OclParser.DotCallSuffixContext ctx, boolean isSafe) {
		OperationCallExp exp = FACTORY.createOperationCallExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		exp.setIsPre(ctx.isMarkedPre() != null);

		if (ctx.argumentList() != null) {
			for (OclParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				exp.getOwnedArguments().add((OclExpression) visit(argCtx));
			}
		}

		String opName = ctx.IDENTIFIER().getText();
		resolveOperation(exp, opName);
		return exp;
	}

	private IteratorExp createIteratorExp(OclExpression source,
			OclParser.IteratorCallContext ctx, boolean isSafe) {
		IteratorExp exp = FACTORY.createIteratorExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		exp.setName(ctx.IDENTIFIER().getText());

		OclEnvironment savedEnv = this.environment;

		// Iterator variables — infer element type from source collection
		OclType elementType = inferElementType(source);

		OclParser.IteratorVariablesContext varsCtx = ctx.iteratorVariables();
		List<Variable> iterVars = new ArrayList<>();
		for (int i = 0; i < varsCtx.IDENTIFIER().size(); i++) {
			Variable iterVar = FACTORY.createVariable();
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

		// Body
		exp.setOwnedBody((OclExpression) visit(ctx.expression()));

		// Set result type: select/reject/closure preserve element type,
		// collect yields the body type, forAll/exists/one/isUnique yield Boolean
		String iterName = ctx.IDENTIFIER().getText();
		if (elementType != null) {
			switch (iterName) {
				case "select", "reject", "sortedBy", "closure":
					exp.setType(elementType);
					break;
				default:
					break;
			}
		}

		this.environment = savedEnv;
		return exp;
	}

	private IterateExp createIterateExp(OclExpression source,
			OclParser.IterateCallContext ctx, boolean isSafe) {
		IterateExp exp = FACTORY.createIterateExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);

		OclEnvironment savedEnv = this.environment;

		// Iterator variable (IDENTIFIER(0) = "iterate", IDENTIFIER(1) = iter var)
		Variable iterVar = FACTORY.createVariable();
		iterVar.setName(ctx.IDENTIFIER(1).getText());
		if (ctx.iterType != null) {
			iterVar.setType(resolveTypeExpression(ctx.iterType));
		} else {
			// Infer element type from source collection
			OclType elementType = inferElementType(source);
			if (elementType != null) {
				iterVar.setType(elementType);
			}
		}
		exp.getOwnedIterators().add(iterVar);

		// Accumulator variable (second IDENTIFIER after ';')
		Variable accVar = FACTORY.createVariable();
		accVar.setName(ctx.IDENTIFIER(2).getText());
		if (ctx.accType != null) {
			accVar.setType(resolveTypeExpression(ctx.accType));
		}
		accVar.setOwnedInit((OclExpression) visit(ctx.expression(0)));
		exp.setOwnedResult(accVar);

		this.environment = this.environment.nested(iterVar);
		this.environment = this.environment.nested(accVar);

		// Body
		exp.setOwnedBody((OclExpression) visit(ctx.expression(1)));

		this.environment = savedEnv;
		return exp;
	}

	private OperationCallExp createCollectionOperation(OclExpression source,
			OclParser.CollectionOperationCallContext ctx, boolean isSafe) {
		OperationCallExp exp = FACTORY.createOperationCallExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);

		if (ctx.argumentList() != null) {
			for (OclParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				exp.getOwnedArguments().add((OclExpression) visit(argCtx));
			}
		}

		String opName = ctx.IDENTIFIER().getText();
		resolveOperation(exp, opName);
		return exp;
	}

	private VariableExp createVariableExp(Variable variable) {
		VariableExp exp = FACTORY.createVariableExp();
		exp.setReferredVariable(variable);
		exp.setType(variable.getType());
		return exp;
	}

	// ==================== Type Resolution ====================

	OclType resolveTypeExpression(OclParser.TypeExpressionContext ctx) {
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
		if (ctx.pathName() != null) {
			return createClassifierType(resolveClassifier(pathNameSegments(ctx.pathName())));
		}
		return null;
	}

	private OclType createPrimitiveType(String name) {
		var type = FACTORY.createPrimitiveType();
		type.setName(name);
		return type;
	}

	private ClassifierType createClassifierType(EClassifier classifier) {
		ClassifierType type = FACTORY.createClassifierType();
		type.setReferredClassifier(classifier);
		type.setName(classifier.getName());
		return type;
	}

	private CollectionType createCollectionTypeForFeature(EStructuralFeature feature) {
		CollectionType colType = FACTORY.createCollectionType();
		colType.setElementType(createClassifierType(feature.getEType()));
		// EReferences are ordered → Sequence; unordered → Set
		if (feature instanceof EReference ref) {
			if (ref.isOrdered()) {
				colType.setKind(ref.isUnique() ? CollectionKind.ORDERED_SET : CollectionKind.SEQUENCE);
			} else {
				colType.setKind(ref.isUnique() ? CollectionKind.SET : CollectionKind.BAG);
			}
		} else {
			// EAttribute many-valued defaults to Sequence
			colType.setKind(CollectionKind.SEQUENCE);
		}
		return colType;
	}

	private OclType resolveCollectionType(OclParser.CollectionTypeContext ctx) {
		CollectionKind kind = resolveCollectionKind(ctx.collectionKind().getText());
		OclType elementType = resolveTypeExpression(ctx.typeExpression());
		CollectionType type = switch (kind) {
			case SET -> FACTORY.createSetType();
			case ORDERED_SET -> FACTORY.createOrderedSetType();
			case BAG -> FACTORY.createBagType();
			case SEQUENCE -> FACTORY.createSequenceType();
			default -> FACTORY.createCollectionType();
		};
		type.setKind(kind);
		type.setElementType(elementType);
		return type;
	}

	private OclType resolveMapType(OclParser.MapTypeContext ctx) {
		var type = FACTORY.createMapType();
		type.setKeyType(resolveTypeExpression(ctx.typeExpression(0)));
		type.setValueType(resolveTypeExpression(ctx.typeExpression(1)));
		return type;
	}

	private OclType resolveTupleType(OclParser.TupleTypeContext ctx) {
		var type = FACTORY.createTupleType();
		for (OclParser.TupleTypePartContext partCtx : ctx.tupleTypePart()) {
			var part = FACTORY.createTuplePart();
			part.setName(partCtx.IDENTIFIER().getText());
			part.setType(resolveTypeExpression(partCtx.typeExpression()));
			type.getOwnedParts().add(part);
		}
		return type;
	}

	// ==================== Name Resolution Stubs ====================
	// These will be fully implemented when the type resolver is connected.

	private void resolveProperty(PropertyCallExp exp, String propName) {
		EClassifier sourceType = getSourceClassifier(exp.getOwnedSource());
		if (sourceType instanceof EClass eClass) {
			EStructuralFeature feature = eClass.getEStructuralFeature(propName);
			if (feature != null) {
				exp.setReferredProperty(feature);
				// Set result type so chained navigations can resolve
				EClassifier featureType = feature.getEType();
				if (featureType != null) {
					if (feature.isMany()) {
						// Many-valued feature → CollectionType wrapping the element type
						exp.setType(createCollectionTypeForFeature(feature));
					} else {
						exp.setType(createClassifierType(featureType));
					}
				}
				return;
			}
		}
		// Unresolved (e.g. tuple part access) — create synthetic EAttribute so name is available
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
						break; // exact match by name + param count
					}
					if (bestMatch == null) {
						bestMatch = op; // fallback: name-only match
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
			return contextType;
		}
		OclType type = source.getType();
		if (type instanceof CollectionType colType && colType.getElementType() instanceof ClassifierType ct) {
			// For collection-typed source, resolve against the element type
			return ct.getReferredClassifier();
		}
		if (type instanceof ClassifierType ct) {
			return ct.getReferredClassifier();
		}
		return null;
	}

	/**
	 * Infers the element type for iterator variables from the source expression.
	 * For collection navigation (e.g., self.employees), the element type is the
	 * type of the collection elements (e.g., Person).
	 */
	private OclType inferElementType(OclExpression source) {
		if (source == null) {
			return null;
		}
		OclType type = source.getType();
		// CollectionType → extract element type
		if (type instanceof CollectionType colType && colType.getElementType() != null) {
			return copyType(colType.getElementType());
		}
		// ClassifierType → used as element type directly (e.g. scalar source)
		if (type instanceof ClassifierType ct && ct.getReferredClassifier() != null) {
			return createClassifierType(ct.getReferredClassifier());
		}
		return null;
	}

	private EClassifier resolveClassifier(List<String> segments) {
		// 1. Single segment: try context package first
		if (segments.size() == 1) {
			String name = segments.get(0);
			if (contextType instanceof EClass contextClass) {
				EClassifier found = contextClass.getEPackage().getEClassifier(name);
				if (found != null) {
					return found;
				}
			}
			// Fallback: search all registered packages
			EClassifier fromRegistry = findInRegistry(name);
			if (fromRegistry != null) {
				return fromRegistry;
			}
			return contextType;
		}
		// 2. Multi-segment: last = classifier name, preceding = package name/nsURI
		String classifierName = segments.get(segments.size() - 1);
		String packageName = String.join("::", segments.subList(0, segments.size() - 1));

		// Try context package by name match
		if (contextType instanceof EClass contextClass) {
			EPackage ctxPkg = contextClass.getEPackage();
			if (ctxPkg.getName().equals(packageName) || ctxPkg.getNsURI().equals(packageName)) {
				EClassifier found = ctxPkg.getEClassifier(classifierName);
				if (found != null) {
					return found;
				}
			}
		}
		// Search registry by package name/nsURI
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
		return contextType;
	}

	/** Searches the package registry for a classifier by simple name. */
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

	private OclExpression resolveImplicitProperty(String name) {
		// Try as property on self
		if (contextType instanceof EClass eClass) {
			EStructuralFeature feature = eClass.getEStructuralFeature(name);
			if (feature != null) {
				PropertyCallExp exp = FACTORY.createPropertyCallExp();
				exp.setIsImplicit(true);
				exp.setOwnedSource(createVariableExp(environment.lookup("self").orElseThrow()));
				exp.setReferredProperty(feature);
				EClassifier featureType = feature.getEType();
				if (featureType != null) {
					if (feature.isMany()) {
						exp.setType(createCollectionTypeForFeature(feature));
					} else {
						exp.setType(createClassifierType(featureType));
					}
				}
				return exp;
			}
		}
		// Try as type name (e.g. Person, Company) in context package
		if (contextType instanceof EClass contextClass) {
			for (EClassifier classifier : contextClass.getEPackage().getEClassifiers()) {
				if (classifier.getName().equals(name)) {
					TypeExp typeExp = FACTORY.createTypeExp();
					typeExp.setReferredType(createClassifierType(classifier));
					return typeExp;
				}
			}
		}
		// Try type name in registry
		EClassifier fromRegistry = findInRegistry(name);
		if (fromRegistry != null) {
			TypeExp typeExp = FACTORY.createTypeExp();
			typeExp.setReferredType(createClassifierType(fromRegistry));
			return typeExp;
		}
		// Unknown name — create a variable expression (may be external variable)
		Variable extVar = FACTORY.createVariable();
		extVar.setName(name);
		return createVariableExp(extVar);
	}

	private EnumLiteralExp tryResolveEnumLiteral(List<String> segments) {
		String enumName = segments.get(0);
		String literalName = segments.get(1);
		// Search context package
		if (contextType instanceof EClass contextClass) {
			EnumLiteralExp result = findEnumLiteralInPackage(
					contextClass.getEPackage(), enumName, literalName);
			if (result != null) {
				return result;
			}
		}
		// Search registry
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
					EnumLiteralExp exp = FACTORY.createEnumLiteralExp();
					exp.setReferredLiteral(literal);
					return exp;
				}
			}
		}
		return null;
	}

	private OclExpression resolveQualifiedName(List<String> segments) {
		// Qualified name: try as type expression
		EClassifier resolved = resolveClassifier(segments);
		TypeExp exp = FACTORY.createTypeExp();
		exp.setReferredType(createClassifierType(resolved));
		return exp;
	}

	// ==================== Utility Methods ====================

	private List<String> pathNameSegments(OclParser.PathNameContext ctx) {
		List<String> segments = new ArrayList<>();
		for (var id : ctx.IDENTIFIER()) {
			segments.add(id.getText());
		}
		return segments;
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
