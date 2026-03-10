// Generated from /opt/git/m2m/workspace/org.eclipse.fennec.m2x.qvto.parser/grammar/QvtO.g4 by ANTLR 4.13.2
package org.eclipse.fennec.m2x.qvto.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link QvtOParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface QvtOVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link QvtOParser#compilationUnitEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompilationUnitEntry(QvtOParser.CompilationUnitEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#compilationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompilationUnit(QvtOParser.CompilationUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#unitElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitElement(QvtOParser.UnitElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#qvtoImportDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQvtoImportDecl(QvtOParser.QvtoImportDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#importedNameList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportedNameList(QvtOParser.ImportedNameListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#modeltypeDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModeltypeDecl(QvtOParser.ModeltypeDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#packageRefList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageRefList(QvtOParser.PackageRefListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#packageRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageRef(QvtOParser.PackageRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#modeltypeWhere}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModeltypeWhere(QvtOParser.ModeltypeWhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#transformationDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransformationDef(QvtOParser.TransformationDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#transformationRefine}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransformationRefine(QvtOParser.TransformationRefineContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#libraryDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibraryDef(QvtOParser.LibraryDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#modelParamList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelParamList(QvtOParser.ModelParamListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#modelParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelParam(QvtOParser.ModelParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#directionKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirectionKind(QvtOParser.DirectionKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#typeSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSpec(QvtOParser.TypeSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#moduleUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleUsage(QvtOParser.ModuleUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#moduleUsageKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleUsageKind(QvtOParser.ModuleUsageKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#moduleRefList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleRefList(QvtOParser.ModuleRefListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#moduleRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleRef(QvtOParser.ModuleRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#moduleRefName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleRefName(QvtOParser.ModuleRefNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#accessDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccessDecl(QvtOParser.AccessDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#qualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifier(QvtOParser.QualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#moduleElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleElement(QvtOParser.ModuleElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#mappingDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMappingDef(QvtOParser.MappingDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#mappingSignature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMappingSignature(QvtOParser.MappingSignatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#resultList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResultList(QvtOParser.ResultListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NamedResult}
	 * labeled alternative in {@link QvtOParser#resultParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedResult(QvtOParser.NamedResultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnnamedResult}
	 * labeled alternative in {@link QvtOParser#resultParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnnamedResult(QvtOParser.UnnamedResultContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#paramList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamList(QvtOParser.ParamListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(QvtOParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#mappingExtension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMappingExtension(QvtOParser.MappingExtensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#mappingExtensionKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMappingExtensionKind(QvtOParser.MappingExtensionKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#whenClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenClause(QvtOParser.WhenClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(QvtOParser.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#guardBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGuardBlock(QvtOParser.GuardBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#mappingBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMappingBody(QvtOParser.MappingBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#initSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitSection(QvtOParser.InitSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#populationSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPopulationSection(QvtOParser.PopulationSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#endSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndSection(QvtOParser.EndSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#helperDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHelperDef(QvtOParser.HelperDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#queryDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryDef(QvtOParser.QueryDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#constructorDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorDef(QvtOParser.ConstructorDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#entryDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntryDef(QvtOParser.EntryDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#simpleSignature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleSignature(QvtOParser.SimpleSignatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#propertyDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyDecl(QvtOParser.PropertyDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#intermediateClassDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntermediateClassDef(QvtOParser.IntermediateClassDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#exceptionDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptionDef(QvtOParser.ExceptionDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#datatypeDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatypeDef(QvtOParser.DatatypeDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#primitiveDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveDef(QvtOParser.PrimitiveDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#enumDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumDef(QvtOParser.EnumDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#enumLiteralList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumLiteralList(QvtOParser.EnumLiteralListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#enumLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumLiteral(QvtOParser.EnumLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#metamodelDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetamodelDef(QvtOParser.MetamodelDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#metamodelElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetamodelElement(QvtOParser.MetamodelElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#typeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeList(QvtOParser.TypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#classifierFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassifierFeature(QvtOParser.ClassifierFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#classifierFeatureModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassifierFeatureModifier(QvtOParser.ClassifierFeatureModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#stereotypeQualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStereotypeQualifier(QvtOParser.StereotypeQualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#multiplicity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicity(QvtOParser.MultiplicityContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#multiplicityRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicityRange(QvtOParser.MultiplicityRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#oppositeProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOppositeProperty(QvtOParser.OppositePropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#tagDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagDecl(QvtOParser.TagDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#tagTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagTarget(QvtOParser.TagTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#typedefDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedefDecl(QvtOParser.TypedefDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#statementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementList(QvtOParser.StatementListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarDeclStatement}
	 * labeled alternative in {@link QvtOParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclStatement(QvtOParser.VarDeclStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpressionStatement}
	 * labeled alternative in {@link QvtOParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(QvtOParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#assignOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignOp(QvtOParser.AssignOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(QvtOParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExp(QvtOParser.NotExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExp(QvtOParser.AndExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CompareExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompareExp(QvtOParser.CompareExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryStarExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryStarExp(QvtOParser.UnaryStarExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultExp(QvtOParser.MultExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code HashExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHashExp(QvtOParser.HashExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DoubleHashExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleHashExp(QvtOParser.DoubleHashExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExp(QvtOParser.AddExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NavigationExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNavigationExp(QvtOParser.NavigationExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code XselectOneExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXselectOneExp(QvtOParser.XselectOneExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImpliesExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImpliesExp(QvtOParser.ImpliesExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimaryExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExp(QvtOParser.PrimaryExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignExp(QvtOParser.AssignExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryMinusExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinusExp(QvtOParser.UnaryMinusExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqualityExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExp(QvtOParser.EqualityExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExp(QvtOParser.OrExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code XorExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXorExp(QvtOParser.XorExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrowExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowExp(QvtOParser.ArrowExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code XselectExp}
	 * labeled alternative in {@link QvtOParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXselectExp(QvtOParser.XselectExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExp}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExp(QvtOParser.ParenExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SelfExp}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelfExp(QvtOParser.SelfExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ThisExp}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThisExp(QvtOParser.ThisExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfExp}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExp(QvtOParser.IfExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImperativeIfExp}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImperativeIfExp(QvtOParser.ImperativeIfExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LetExp}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetExp(QvtOParser.LetExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralExp}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExp(QvtOParser.LiteralExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BlockPrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockPrimary(QvtOParser.BlockPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhilePrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhilePrimary(QvtOParser.WhilePrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForPrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForPrimary(QvtOParser.ForPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SwitchPrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchPrimary(QvtOParser.SwitchPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ComputePrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComputePrimary(QvtOParser.ComputePrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ObjectPrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectPrimary(QvtOParser.ObjectPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NewPrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewPrimary(QvtOParser.NewPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MappingCallPrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMappingCallPrimary(QvtOParser.MappingCallPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ResolvePrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResolvePrimary(QvtOParser.ResolvePrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ResolveInPrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResolveInPrimary(QvtOParser.ResolveInPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TryPrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryPrimary(QvtOParser.TryPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RaisePrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRaisePrimary(QvtOParser.RaisePrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssertPrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssertPrimary(QvtOParser.AssertPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogPrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogPrimary(QvtOParser.LogPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReturnPrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnPrimary(QvtOParser.ReturnPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BreakPrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakPrimary(QvtOParser.BreakPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ContinuePrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinuePrimary(QvtOParser.ContinuePrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarDeclPrimary}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclPrimary(QvtOParser.VarDeclPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OperationCallExp}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperationCallExp(QvtOParser.OperationCallExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveTypeExp}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveTypeExp(QvtOParser.PrimitiveTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CollectionTypeExp}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionTypeExp(QvtOParser.CollectionTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapTypeExp}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapTypeExp(QvtOParser.MapTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TupleTypeExp}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleTypeExp(QvtOParser.TupleTypeExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PathNameExp}
	 * labeled alternative in {@link QvtOParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathNameExp(QvtOParser.PathNameExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#typeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeExpression(QvtOParser.TypeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#listType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListType(QvtOParser.ListTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#dictType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictType(QvtOParser.DictTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#collectionKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionKind(QvtOParser.CollectionKindContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link QvtOParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(QvtOParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RealLiteral}
	 * labeled alternative in {@link QvtOParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRealLiteral(QvtOParser.RealLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link QvtOParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(QvtOParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TrueLiteral}
	 * labeled alternative in {@link QvtOParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueLiteral(QvtOParser.TrueLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FalseLiteral}
	 * labeled alternative in {@link QvtOParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseLiteral(QvtOParser.FalseLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link QvtOParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullLiteral(QvtOParser.NullLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InvalidLiteral}
	 * labeled alternative in {@link QvtOParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvalidLiteral(QvtOParser.InvalidLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnlimitedNaturalLiteral}
	 * labeled alternative in {@link QvtOParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnlimitedNaturalLiteral(QvtOParser.UnlimitedNaturalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CollectionLit}
	 * labeled alternative in {@link QvtOParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLit(QvtOParser.CollectionLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TupleLit}
	 * labeled alternative in {@link QvtOParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleLit(QvtOParser.TupleLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapLit}
	 * labeled alternative in {@link QvtOParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLit(QvtOParser.MapLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DictLit}
	 * labeled alternative in {@link QvtOParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictLit(QvtOParser.DictLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#stringLiteral_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral_(QvtOParser.StringLiteral_Context ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#dictLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictLiteral(QvtOParser.DictLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#dictLiteralPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictLiteralPart(QvtOParser.DictLiteralPartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleLitInt}
	 * labeled alternative in {@link QvtOParser#literalSimple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleLitInt(QvtOParser.SimpleLitIntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleLitReal}
	 * labeled alternative in {@link QvtOParser#literalSimple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleLitReal(QvtOParser.SimpleLitRealContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleLitString}
	 * labeled alternative in {@link QvtOParser#literalSimple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleLitString(QvtOParser.SimpleLitStringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleLitTrue}
	 * labeled alternative in {@link QvtOParser#literalSimple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleLitTrue(QvtOParser.SimpleLitTrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleLitFalse}
	 * labeled alternative in {@link QvtOParser#literalSimple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleLitFalse(QvtOParser.SimpleLitFalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleLitNull}
	 * labeled alternative in {@link QvtOParser#literalSimple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleLitNull(QvtOParser.SimpleLitNullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleLitUnlimited}
	 * labeled alternative in {@link QvtOParser#literalSimple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleLitUnlimited(QvtOParser.SimpleLitUnlimitedContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#blockExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockExp(QvtOParser.BlockExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileWithInit}
	 * labeled alternative in {@link QvtOParser#whileExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileWithInit(QvtOParser.WhileWithInitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileBasic}
	 * labeled alternative in {@link QvtOParser#whileExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileBasic(QvtOParser.WhileBasicContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#forExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForExp(QvtOParser.ForExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#forKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForKind(QvtOParser.ForKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#forVarList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForVarList(QvtOParser.ForVarListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#switchExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchExp(QvtOParser.SwitchExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#switchIterator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchIterator(QvtOParser.SwitchIteratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#switchAlt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchAlt(QvtOParser.SwitchAltContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#computeExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComputeExp(QvtOParser.ComputeExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#objectExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectExp(QvtOParser.ObjectExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#objectIterator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectIterator(QvtOParser.ObjectIteratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#newExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewExp(QvtOParser.NewExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#mappingCallExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMappingCallExp(QvtOParser.MappingCallExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#mappingCallKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMappingCallKind(QvtOParser.MappingCallKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#resolveExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResolveExp(QvtOParser.ResolveExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#resolveKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResolveKind(QvtOParser.ResolveKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#resolveInExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResolveInExp(QvtOParser.ResolveInExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#resolveInKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResolveInKind(QvtOParser.ResolveInKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#resolveArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResolveArgs(QvtOParser.ResolveArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NamedResolveTarget}
	 * labeled alternative in {@link QvtOParser#resolveTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedResolveTarget(QvtOParser.NamedResolveTargetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeOnlyResolveTarget}
	 * labeled alternative in {@link QvtOParser#resolveTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeOnlyResolveTarget(QvtOParser.TypeOnlyResolveTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#tryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryExp(QvtOParser.TryExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#catchClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchClause(QvtOParser.CatchClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#exceptionTypeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptionTypeList(QvtOParser.ExceptionTypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#raiseExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRaiseExp(QvtOParser.RaiseExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#assertExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssertExp(QvtOParser.AssertExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#logCallExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogCallExp(QvtOParser.LogCallExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#logExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogExp(QvtOParser.LogExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#returnExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnExp(QvtOParser.ReturnExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#varDeclExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclExp(QvtOParser.VarDeclExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#varDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclarator(QvtOParser.VarDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#varInitOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarInitOp(QvtOParser.VarInitOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#pathName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathName(QvtOParser.PathNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MappingCallSuffix}
	 * labeled alternative in {@link QvtOParser#propertyOrCallSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMappingCallSuffix(QvtOParser.MappingCallSuffixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ResolveCallSuffix}
	 * labeled alternative in {@link QvtOParser#propertyOrCallSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResolveCallSuffix(QvtOParser.ResolveCallSuffixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ResolveInCallSuffix}
	 * labeled alternative in {@link QvtOParser#propertyOrCallSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResolveInCallSuffix(QvtOParser.ResolveInCallSuffixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DotCallSuffix}
	 * labeled alternative in {@link QvtOParser#propertyOrCallSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotCallSuffix(QvtOParser.DotCallSuffixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PropertySuffix}
	 * labeled alternative in {@link QvtOParser#propertyOrCallSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertySuffix(QvtOParser.PropertySuffixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForEachCall}
	 * labeled alternative in {@link QvtOParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForEachCall(QvtOParser.ForEachCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrowResolveCall}
	 * labeled alternative in {@link QvtOParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowResolveCall(QvtOParser.ArrowResolveCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrowResolveInCall}
	 * labeled alternative in {@link QvtOParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowResolveInCall(QvtOParser.ArrowResolveInCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrowMappingCall}
	 * labeled alternative in {@link QvtOParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowMappingCall(QvtOParser.ArrowMappingCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrowObjectCall}
	 * labeled alternative in {@link QvtOParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowObjectCall(QvtOParser.ArrowObjectCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrowSwitchCall}
	 * labeled alternative in {@link QvtOParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowSwitchCall(QvtOParser.ArrowSwitchCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IteratorCall}
	 * labeled alternative in {@link QvtOParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIteratorCall(QvtOParser.IteratorCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IterateCall}
	 * labeled alternative in {@link QvtOParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterateCall(QvtOParser.IterateCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CollectionOperationCall}
	 * labeled alternative in {@link QvtOParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionOperationCall(QvtOParser.CollectionOperationCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrowPropertyCall}
	 * labeled alternative in {@link QvtOParser#iteratorOrOperationCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowPropertyCall(QvtOParser.ArrowPropertyCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#iteratorVariables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIteratorVariables(QvtOParser.IteratorVariablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#letBinding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetBinding(QvtOParser.LetBindingContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#tupleTypePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleTypePart(QvtOParser.TupleTypePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#tupleLiteralPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleLiteralPart(QvtOParser.TupleLiteralPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#scopedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScopedName(QvtOParser.ScopedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#scopedNameList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScopedNameList(QvtOParser.ScopedNameListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#qualifiedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedName(QvtOParser.QualifiedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#qvtoIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQvtoIdentifier(QvtOParser.QvtoIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#expressionEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionEntry(QvtOParser.ExpressionEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#completeOclDocumentEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompleteOclDocumentEntry(QvtOParser.CompleteOclDocumentEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#completeOclDocument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompleteOclDocument(QvtOParser.CompleteOclDocumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#importDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDeclaration(QvtOParser.ImportDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#packageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageDeclaration(QvtOParser.PackageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#contextDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContextDeclaration(QvtOParser.ContextDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#classifierContextDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassifierContextDeclaration(QvtOParser.ClassifierContextDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#classifierContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassifierContextBody(QvtOParser.ClassifierContextBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#invariantConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvariantConstraint(QvtOParser.InvariantConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#definitionConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionConstraint(QvtOParser.DefinitionConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#operationContextDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperationContextDeclaration(QvtOParser.OperationContextDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PreCondition}
	 * labeled alternative in {@link QvtOParser#operationContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreCondition(QvtOParser.PreConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PostCondition}
	 * labeled alternative in {@link QvtOParser#operationContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostCondition(QvtOParser.PostConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BodyExpression}
	 * labeled alternative in {@link QvtOParser#operationContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodyExpression(QvtOParser.BodyExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#propertyContextDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyContextDeclaration(QvtOParser.PropertyContextDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InitExpression}
	 * labeled alternative in {@link QvtOParser#propertyContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitExpression(QvtOParser.InitExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeriveExpression}
	 * labeled alternative in {@link QvtOParser#propertyContextBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeriveExpression(QvtOParser.DeriveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#parameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterList(QvtOParser.ParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(QvtOParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(QvtOParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#isMarkedPre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsMarkedPre(QvtOParser.IsMarkedPreContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#collectionLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLiteral(QvtOParser.CollectionLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#collectionLiteralPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLiteralPart(QvtOParser.CollectionLiteralPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#tupleLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleLiteral(QvtOParser.TupleLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#mapLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLiteral(QvtOParser.MapLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#mapLiteralPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLiteralPart(QvtOParser.MapLiteralPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(QvtOParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#collectionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionType(QvtOParser.CollectionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#mapType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapType(QvtOParser.MapTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#tupleType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleType(QvtOParser.TupleTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link QvtOParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(QvtOParser.IdentifierContext ctx);
}