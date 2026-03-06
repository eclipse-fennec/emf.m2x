// Generated from grammar/QvtR.g4 by ANTLR 4.13.2
package org.eclipse.fennec.m2x.qvtd.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link QvtRParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface QvtRVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link QvtRParser#compilationUnitEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompilationUnitEntry(QvtRParser.CompilationUnitEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#compilationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompilationUnit(QvtRParser.CompilationUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#importDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDecl(QvtRParser.ImportDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#qualifiedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedName(QvtRParser.QualifiedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#transformationDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransformationDef(QvtRParser.TransformationDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#modelDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelDecl(QvtRParser.ModelDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#metaModelId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetaModelId(QvtRParser.MetaModelIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#keyDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyDecl(QvtRParser.KeyDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#keyProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyProperty(QvtRParser.KeyPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation(QvtRParser.RelationContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclaration(QvtRParser.VarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#domain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDomain(QvtRParser.DomainContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#primitiveTypeDomain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveTypeDomain(QvtRParser.PrimitiveTypeDomainContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#checkEnforceQualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckEnforceQualifier(QvtRParser.CheckEnforceQualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#assignmentExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentExp(QvtRParser.AssignmentExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#template}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplate(QvtRParser.TemplateContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#objectTemplate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectTemplate(QvtRParser.ObjectTemplateContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#propertyTemplateList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyTemplateList(QvtRParser.PropertyTemplateListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#propertyTemplate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyTemplate(QvtRParser.PropertyTemplateContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#collectionTemplate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionTemplate(QvtRParser.CollectionTemplateContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#memberSelection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberSelection(QvtRParser.MemberSelectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#memberItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberItem(QvtRParser.MemberItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#whenClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenClause(QvtRParser.WhenClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(QvtRParser.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#queryDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryDef(QvtRParser.QueryDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#paramDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamDecl(QvtRParser.ParamDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#expressionEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionEntry(QvtRParser.ExpressionEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#completeOclDocumentEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompleteOclDocumentEntry(QvtRParser.CompleteOclDocumentEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#completeOclDocument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompleteOclDocument(QvtRParser.CompleteOclDocumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#importDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDeclaration(QvtRParser.ImportDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#packageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageDeclaration(QvtRParser.PackageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#contextDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContextDeclaration(QvtRParser.ContextDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#classifierContextDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassifierContextDeclaration(QvtRParser.ClassifierContextDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#classifierContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassifierContextBody(QvtRParser.ClassifierContextBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#invariantConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvariantConstraint(QvtRParser.InvariantConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#definitionConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionConstraint(QvtRParser.DefinitionConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#operationContextDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperationContextDeclaration(QvtRParser.OperationContextDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PreCondition}
	 * labeled alternative in {@link QvtRParser#operationContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreCondition(QvtRParser.PreConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PostCondition}
	 * labeled alternative in {@link QvtRParser#operationContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostCondition(QvtRParser.PostConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BodyExpression}
	 * labeled alternative in {@link QvtRParser#operationContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodyExpression(QvtRParser.BodyExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#propertyContextDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyContextDeclaration(QvtRParser.PropertyContextDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InitExpression}
	 * labeled alternative in {@link QvtRParser#propertyContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitExpression(QvtRParser.InitExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeriveExpression}
	 * labeled alternative in {@link QvtRParser#propertyContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeriveExpression(QvtRParser.DeriveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#parameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterList(QvtRParser.ParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(QvtRParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExp}
	 * labeled alternative in {@link QvtRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExp(QvtRParser.NotExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExp}
	 * labeled alternative in {@link QvtRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExp(QvtRParser.AndExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CompareExp}
	 * labeled alternative in {@link QvtRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompareExp(QvtRParser.CompareExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultExp}
	 * labeled alternative in {@link QvtRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultExp(QvtRParser.MultExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddExp}
	 * labeled alternative in {@link QvtRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExp(QvtRParser.AddExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NavigationExp}
	 * labeled alternative in {@link QvtRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNavigationExp(QvtRParser.NavigationExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImpliesExp}
	 * labeled alternative in {@link QvtRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImpliesExp(QvtRParser.ImpliesExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimaryExp}
	 * labeled alternative in {@link QvtRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExp(QvtRParser.PrimaryExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryMinusExp}
	 * labeled alternative in {@link QvtRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinusExp(QvtRParser.UnaryMinusExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqualityExp}
	 * labeled alternative in {@link QvtRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExp(QvtRParser.EqualityExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExp}
	 * labeled alternative in {@link QvtRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExp(QvtRParser.OrExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code XorExp}
	 * labeled alternative in {@link QvtRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXorExp(QvtRParser.XorExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrowExp}
	 * labeled alternative in {@link QvtRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowExp(QvtRParser.ArrowExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExp}
	 * labeled alternative in {@link QvtRParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExp(QvtRParser.ParenExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SelfExp}
	 * labeled alternative in {@link QvtRParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelfExp(QvtRParser.SelfExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfExp}
	 * labeled alternative in {@link QvtRParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExp(QvtRParser.IfExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LetExp}
	 * labeled alternative in {@link QvtRParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetExp(QvtRParser.LetExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralExp}
	 * labeled alternative in {@link QvtRParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExp(QvtRParser.LiteralExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OperationCallExp}
	 * labeled alternative in {@link QvtRParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperationCallExp(QvtRParser.OperationCallExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveTypeExp}
	 * labeled alternative in {@link QvtRParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveTypeExp(QvtRParser.PrimitiveTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CollectionTypeExp}
	 * labeled alternative in {@link QvtRParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionTypeExp(QvtRParser.CollectionTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapTypeExp}
	 * labeled alternative in {@link QvtRParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapTypeExp(QvtRParser.MapTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TupleTypeExp}
	 * labeled alternative in {@link QvtRParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleTypeExp(QvtRParser.TupleTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PathNameExp}
	 * labeled alternative in {@link QvtRParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathNameExp(QvtRParser.PathNameExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DotCallSuffix}
	 * labeled alternative in {@link QvtRParser#propertyOrCallSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotCallSuffix(QvtRParser.DotCallSuffixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PropertySuffix}
	 * labeled alternative in {@link QvtRParser#propertyOrCallSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertySuffix(QvtRParser.PropertySuffixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IteratorCall}
	 * labeled alternative in {@link QvtRParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIteratorCall(QvtRParser.IteratorCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IterateCall}
	 * labeled alternative in {@link QvtRParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterateCall(QvtRParser.IterateCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CollectionOperationCall}
	 * labeled alternative in {@link QvtRParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionOperationCall(QvtRParser.CollectionOperationCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#iteratorVariables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIteratorVariables(QvtRParser.IteratorVariablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(QvtRParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#letBinding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetBinding(QvtRParser.LetBindingContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#isMarkedPre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsMarkedPre(QvtRParser.IsMarkedPreContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link QvtRParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(QvtRParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RealLiteral}
	 * labeled alternative in {@link QvtRParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRealLiteral(QvtRParser.RealLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringLit}
	 * labeled alternative in {@link QvtRParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLit(QvtRParser.StringLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TrueLiteral}
	 * labeled alternative in {@link QvtRParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueLiteral(QvtRParser.TrueLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FalseLiteral}
	 * labeled alternative in {@link QvtRParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseLiteral(QvtRParser.FalseLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link QvtRParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullLiteral(QvtRParser.NullLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InvalidLiteral}
	 * labeled alternative in {@link QvtRParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvalidLiteral(QvtRParser.InvalidLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnlimitedNaturalLiteral}
	 * labeled alternative in {@link QvtRParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnlimitedNaturalLiteral(QvtRParser.UnlimitedNaturalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CollectionLit}
	 * labeled alternative in {@link QvtRParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLit(QvtRParser.CollectionLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TupleLit}
	 * labeled alternative in {@link QvtRParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleLit(QvtRParser.TupleLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapLit}
	 * labeled alternative in {@link QvtRParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLit(QvtRParser.MapLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#stringLiteral_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral_(QvtRParser.StringLiteral_Context ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#collectionLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLiteral(QvtRParser.CollectionLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#collectionKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionKind(QvtRParser.CollectionKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#collectionLiteralPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLiteralPart(QvtRParser.CollectionLiteralPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#tupleLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleLiteral(QvtRParser.TupleLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#tupleLiteralPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleLiteralPart(QvtRParser.TupleLiteralPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#mapLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLiteral(QvtRParser.MapLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#mapLiteralPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLiteralPart(QvtRParser.MapLiteralPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#typeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeExpression(QvtRParser.TypeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(QvtRParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#collectionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionType(QvtRParser.CollectionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#mapType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapType(QvtRParser.MapTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#tupleType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleType(QvtRParser.TupleTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#tupleTypePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleTypePart(QvtRParser.TupleTypePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#pathName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathName(QvtRParser.PathNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtRParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(QvtRParser.IdentifierContext ctx);
}