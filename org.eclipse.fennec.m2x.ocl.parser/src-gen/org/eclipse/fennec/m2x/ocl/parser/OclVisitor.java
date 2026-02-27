// Generated from /opt/git/m2m/workspace/org.eclipse.fennec.m2x.ocl.parser/grammar/Ocl.g4 by ANTLR 4.13.2
package org.eclipse.fennec.m2x.ocl.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link OclParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface OclVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link OclParser#expressionEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionEntry(OclParser.ExpressionEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#completeOclDocumentEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompleteOclDocumentEntry(OclParser.CompleteOclDocumentEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#completeOclDocument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompleteOclDocument(OclParser.CompleteOclDocumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#importDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDeclaration(OclParser.ImportDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#packageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageDeclaration(OclParser.PackageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#contextDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContextDeclaration(OclParser.ContextDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#classifierContextDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassifierContextDeclaration(OclParser.ClassifierContextDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#classifierContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassifierContextBody(OclParser.ClassifierContextBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#invariantConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvariantConstraint(OclParser.InvariantConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#definitionConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionConstraint(OclParser.DefinitionConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#operationContextDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperationContextDeclaration(OclParser.OperationContextDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PreCondition}
	 * labeled alternative in {@link OclParser#operationContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreCondition(OclParser.PreConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PostCondition}
	 * labeled alternative in {@link OclParser#operationContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostCondition(OclParser.PostConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BodyExpression}
	 * labeled alternative in {@link OclParser#operationContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodyExpression(OclParser.BodyExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#propertyContextDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyContextDeclaration(OclParser.PropertyContextDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InitExpression}
	 * labeled alternative in {@link OclParser#propertyContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitExpression(OclParser.InitExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeriveExpression}
	 * labeled alternative in {@link OclParser#propertyContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeriveExpression(OclParser.DeriveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#parameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterList(OclParser.ParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(OclParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExp}
	 * labeled alternative in {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExp(OclParser.NotExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExp}
	 * labeled alternative in {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExp(OclParser.AndExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CompareExp}
	 * labeled alternative in {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompareExp(OclParser.CompareExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultExp}
	 * labeled alternative in {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultExp(OclParser.MultExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddExp}
	 * labeled alternative in {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExp(OclParser.AddExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NavigationExp}
	 * labeled alternative in {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNavigationExp(OclParser.NavigationExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImpliesExp}
	 * labeled alternative in {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImpliesExp(OclParser.ImpliesExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimaryExp}
	 * labeled alternative in {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExp(OclParser.PrimaryExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryMinusExp}
	 * labeled alternative in {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinusExp(OclParser.UnaryMinusExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqualityExp}
	 * labeled alternative in {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExp(OclParser.EqualityExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExp}
	 * labeled alternative in {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExp(OclParser.OrExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code XorExp}
	 * labeled alternative in {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXorExp(OclParser.XorExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrowExp}
	 * labeled alternative in {@link OclParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowExp(OclParser.ArrowExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExp}
	 * labeled alternative in {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExp(OclParser.ParenExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SelfExp}
	 * labeled alternative in {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelfExp(OclParser.SelfExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfExp}
	 * labeled alternative in {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExp(OclParser.IfExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LetExp}
	 * labeled alternative in {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetExp(OclParser.LetExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralExp}
	 * labeled alternative in {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExp(OclParser.LiteralExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OperationCallExp}
	 * labeled alternative in {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperationCallExp(OclParser.OperationCallExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveTypeExp}
	 * labeled alternative in {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveTypeExp(OclParser.PrimitiveTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CollectionTypeExp}
	 * labeled alternative in {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionTypeExp(OclParser.CollectionTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapTypeExp}
	 * labeled alternative in {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapTypeExp(OclParser.MapTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TupleTypeExp}
	 * labeled alternative in {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleTypeExp(OclParser.TupleTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PathNameExp}
	 * labeled alternative in {@link OclParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathNameExp(OclParser.PathNameExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DotCallSuffix}
	 * labeled alternative in {@link OclParser#propertyOrCallSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotCallSuffix(OclParser.DotCallSuffixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PropertySuffix}
	 * labeled alternative in {@link OclParser#propertyOrCallSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertySuffix(OclParser.PropertySuffixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IteratorCall}
	 * labeled alternative in {@link OclParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIteratorCall(OclParser.IteratorCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IterateCall}
	 * labeled alternative in {@link OclParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterateCall(OclParser.IterateCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CollectionOperationCall}
	 * labeled alternative in {@link OclParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionOperationCall(OclParser.CollectionOperationCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#iteratorVariables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIteratorVariables(OclParser.IteratorVariablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(OclParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#letBinding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetBinding(OclParser.LetBindingContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#isMarkedPre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsMarkedPre(OclParser.IsMarkedPreContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link OclParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(OclParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RealLiteral}
	 * labeled alternative in {@link OclParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRealLiteral(OclParser.RealLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringLit}
	 * labeled alternative in {@link OclParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLit(OclParser.StringLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TrueLiteral}
	 * labeled alternative in {@link OclParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueLiteral(OclParser.TrueLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FalseLiteral}
	 * labeled alternative in {@link OclParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseLiteral(OclParser.FalseLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link OclParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullLiteral(OclParser.NullLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InvalidLiteral}
	 * labeled alternative in {@link OclParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvalidLiteral(OclParser.InvalidLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnlimitedNaturalLiteral}
	 * labeled alternative in {@link OclParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnlimitedNaturalLiteral(OclParser.UnlimitedNaturalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CollectionLit}
	 * labeled alternative in {@link OclParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLit(OclParser.CollectionLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TupleLit}
	 * labeled alternative in {@link OclParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleLit(OclParser.TupleLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapLit}
	 * labeled alternative in {@link OclParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLit(OclParser.MapLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#stringLiteral_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral_(OclParser.StringLiteral_Context ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#collectionLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLiteral(OclParser.CollectionLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#collectionKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionKind(OclParser.CollectionKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#collectionLiteralPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLiteralPart(OclParser.CollectionLiteralPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#tupleLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleLiteral(OclParser.TupleLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#tupleLiteralPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleLiteralPart(OclParser.TupleLiteralPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#mapLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLiteral(OclParser.MapLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#mapLiteralPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLiteralPart(OclParser.MapLiteralPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#typeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeExpression(OclParser.TypeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(OclParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#collectionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionType(OclParser.CollectionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#mapType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapType(OclParser.MapTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#tupleType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleType(OclParser.TupleTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#tupleTypePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleTypePart(OclParser.TupleTypePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#pathName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathName(OclParser.PathNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link OclParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(OclParser.IdentifierContext ctx);
}