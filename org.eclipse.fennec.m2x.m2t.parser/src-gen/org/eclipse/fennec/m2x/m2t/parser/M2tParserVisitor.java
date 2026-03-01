// Generated from /opt/git/m2m/workspace/org.eclipse.fennec.m2x.m2t.parser/grammar/M2tParser.g4 by ANTLR 4.13.2
package org.eclipse.fennec.m2x.m2t.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link M2tParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface M2tParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link M2tParser#module}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule(M2tParser.ModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#moduleDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleDecl(M2tParser.ModuleDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#metamodelList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetamodelList(M2tParser.MetamodelListContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#extendsDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtendsDecl(M2tParser.ExtendsDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#importDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDecl(M2tParser.ImportDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#moduleElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleElement(M2tParser.ModuleElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#commentBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentBlock(M2tParser.CommentBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#templateDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateDef(M2tParser.TemplateDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#signature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignature(M2tParser.SignatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#visibility}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVisibility(M2tParser.VisibilityContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#overridesDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverridesDecl(M2tParser.OverridesDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#guard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGuard(M2tParser.GuardContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#postDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostDecl(M2tParser.PostDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#initSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitSection(M2tParser.InitSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(M2tParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#queryDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryDef(M2tParser.QueryDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#macroDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroDef(M2tParser.MacroDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#argList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgList(M2tParser.ArgListContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#argDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgDecl(M2tParser.ArgDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(M2tParser.BodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#bodyElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodyElement(M2tParser.BodyElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#textBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextBlock(M2tParser.TextBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#inlineExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineExpression(M2tParser.InlineExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#forBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForBlock(M2tParser.ForBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#beforeClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeforeClause(M2tParser.BeforeClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#separatorClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeparatorClause(M2tParser.SeparatorClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#afterClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAfterClause(M2tParser.AfterClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#ifBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfBlock(M2tParser.IfBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#elseIfBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseIfBlock(M2tParser.ElseIfBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#elseBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseBlock(M2tParser.ElseBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#letBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetBlock(M2tParser.LetBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#elseLetBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseLetBlock(M2tParser.ElseLetBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#fileBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFileBlock(M2tParser.FileBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#traceBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTraceBlock(M2tParser.TraceBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#protectedBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProtectedBlock(M2tParser.ProtectedBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#templateInvocation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateInvocation(M2tParser.TemplateInvocationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExp}
	 * labeled alternative in {@link M2tParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExp(M2tParser.NotExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExp}
	 * labeled alternative in {@link M2tParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExp(M2tParser.AndExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CompareExp}
	 * labeled alternative in {@link M2tParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompareExp(M2tParser.CompareExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultExp}
	 * labeled alternative in {@link M2tParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultExp(M2tParser.MultExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddExp}
	 * labeled alternative in {@link M2tParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExp(M2tParser.AddExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NavigationExp}
	 * labeled alternative in {@link M2tParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNavigationExp(M2tParser.NavigationExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImpliesExp}
	 * labeled alternative in {@link M2tParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImpliesExp(M2tParser.ImpliesExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimaryExp}
	 * labeled alternative in {@link M2tParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExp(M2tParser.PrimaryExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryMinusExp}
	 * labeled alternative in {@link M2tParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinusExp(M2tParser.UnaryMinusExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqualityExp}
	 * labeled alternative in {@link M2tParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExp(M2tParser.EqualityExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExp}
	 * labeled alternative in {@link M2tParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExp(M2tParser.OrExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code XorExp}
	 * labeled alternative in {@link M2tParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXorExp(M2tParser.XorExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrowExp}
	 * labeled alternative in {@link M2tParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowExp(M2tParser.ArrowExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExp}
	 * labeled alternative in {@link M2tParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExp(M2tParser.ParenExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SelfExp}
	 * labeled alternative in {@link M2tParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelfExp(M2tParser.SelfExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OclIfExp}
	 * labeled alternative in {@link M2tParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOclIfExp(M2tParser.OclIfExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OclLetExp}
	 * labeled alternative in {@link M2tParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOclLetExp(M2tParser.OclLetExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralExp}
	 * labeled alternative in {@link M2tParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExp(M2tParser.LiteralExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OperationCallExp}
	 * labeled alternative in {@link M2tParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperationCallExp(M2tParser.OperationCallExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveTypeExp}
	 * labeled alternative in {@link M2tParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveTypeExp(M2tParser.PrimitiveTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CollectionTypeExp}
	 * labeled alternative in {@link M2tParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionTypeExp(M2tParser.CollectionTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapTypeExp}
	 * labeled alternative in {@link M2tParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapTypeExp(M2tParser.MapTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TupleTypeExp}
	 * labeled alternative in {@link M2tParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleTypeExp(M2tParser.TupleTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PathNameExp}
	 * labeled alternative in {@link M2tParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathNameExp(M2tParser.PathNameExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DotCallSuffix}
	 * labeled alternative in {@link M2tParser#propertyOrCallSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotCallSuffix(M2tParser.DotCallSuffixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PropertySuffix}
	 * labeled alternative in {@link M2tParser#propertyOrCallSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertySuffix(M2tParser.PropertySuffixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IteratorCall}
	 * labeled alternative in {@link M2tParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIteratorCall(M2tParser.IteratorCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IterateCall}
	 * labeled alternative in {@link M2tParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterateCall(M2tParser.IterateCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CollectionOperationCall}
	 * labeled alternative in {@link M2tParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionOperationCall(M2tParser.CollectionOperationCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#iteratorVariables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIteratorVariables(M2tParser.IteratorVariablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(M2tParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#letBinding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetBinding(M2tParser.LetBindingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link M2tParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(M2tParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RealLiteral}
	 * labeled alternative in {@link M2tParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRealLiteral(M2tParser.RealLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringLit}
	 * labeled alternative in {@link M2tParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLit(M2tParser.StringLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TrueLiteral}
	 * labeled alternative in {@link M2tParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueLiteral(M2tParser.TrueLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FalseLiteral}
	 * labeled alternative in {@link M2tParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseLiteral(M2tParser.FalseLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link M2tParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullLiteral(M2tParser.NullLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InvalidLiteral}
	 * labeled alternative in {@link M2tParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvalidLiteral(M2tParser.InvalidLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnlimitedNaturalLiteral}
	 * labeled alternative in {@link M2tParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnlimitedNaturalLiteral(M2tParser.UnlimitedNaturalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CollectionLit}
	 * labeled alternative in {@link M2tParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLit(M2tParser.CollectionLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TupleLit}
	 * labeled alternative in {@link M2tParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleLit(M2tParser.TupleLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapLit}
	 * labeled alternative in {@link M2tParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLit(M2tParser.MapLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(M2tParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#collectionLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLiteral(M2tParser.CollectionLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#collectionKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionKind(M2tParser.CollectionKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#collectionLiteralPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLiteralPart(M2tParser.CollectionLiteralPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#tupleLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleLiteral(M2tParser.TupleLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#tupleLiteralPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleLiteralPart(M2tParser.TupleLiteralPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#mapLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLiteral(M2tParser.MapLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#mapLiteralPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLiteralPart(M2tParser.MapLiteralPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#typeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeExpression(M2tParser.TypeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(M2tParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#collectionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionType(M2tParser.CollectionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#mapType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapType(M2tParser.MapTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#tupleType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleType(M2tParser.TupleTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#tupleTypePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleTypePart(M2tParser.TupleTypePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#pathName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathName(M2tParser.PathNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link M2tParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(M2tParser.IdentifierContext ctx);
}