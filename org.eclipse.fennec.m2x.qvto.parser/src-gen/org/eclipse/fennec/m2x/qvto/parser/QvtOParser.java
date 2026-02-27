// Generated from /opt/git/m2m/workspace/org.eclipse.fennec.m2x.qvto.parser/grammar/QvtO.g4 by ANTLR 4.13.2
package org.eclipse.fennec.m2x.qvto.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class QvtOParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45, 
		T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, T__50=51, T__51=52, 
		T__52=53, T__53=54, T__54=55, T__55=56, T__56=57, T__57=58, T__58=59, 
		T__59=60, T__60=61, T__61=62, T__62=63, T__63=64, T__64=65, T__65=66, 
		T__66=67, T__67=68, T__68=69, T__69=70, T__70=71, T__71=72, T__72=73, 
		T__73=74, T__74=75, T__75=76, T__76=77, T__77=78, T__78=79, T__79=80, 
		T__80=81, T__81=82, T__82=83, T__83=84, T__84=85, T__85=86, T__86=87, 
		T__87=88, T__88=89, T__89=90, T__90=91, T__91=92, T__92=93, T__93=94, 
		T__94=95, T__95=96, T__96=97, T__97=98, T__98=99, T__99=100, T__100=101, 
		T__101=102, T__102=103, T__103=104, T__104=105, T__105=106, T__106=107, 
		T__107=108, T__108=109, T__109=110, T__110=111, T__111=112, T__112=113, 
		T__113=114, T__114=115, T__115=116, T__116=117, T__117=118, T__118=119, 
		T__119=120, T__120=121, T__121=122, T__122=123, T__123=124, T__124=125, 
		T__125=126, T__126=127, T__127=128, T__128=129, T__129=130, T__130=131, 
		T__131=132, T__132=133, T__133=134, T__134=135, T__135=136, T__136=137, 
		T__137=138, T__138=139, T__139=140, T__140=141, T__141=142, T__142=143, 
		T__143=144, T__144=145, T__145=146, T__146=147, T__147=148, T__148=149, 
		T__149=150, T__150=151, T__151=152, T__152=153, T__153=154, T__154=155, 
		T__155=156, T__156=157, LINE_COMMENT=158, RESET_ASSIGN=159, ADD_ASSIGN=160, 
		ORDERED_COPY=161, SUBTRACT_ASSIGN=162, DOUBLE_HASH=163, HASH=164, NOT_EQUAL_EXEQ=165, 
		NOT_ARROW=166, LCHEVRON2=167, RCHEVRON2=168, DOUBLE_QUOTED_STRING=169, 
		REAL_LITERAL=170, INTEGER_LITERAL=171, STRING_LITERAL=172, IDENTIFIER=173, 
		WS=174, BLOCK_COMMENT=175;
	public static final int
		RULE_compilationUnitEntry = 0, RULE_compilationUnit = 1, RULE_unitElement = 2, 
		RULE_qvtoImportDecl = 3, RULE_importedNameList = 4, RULE_modeltypeDecl = 5, 
		RULE_packageRefList = 6, RULE_packageRef = 7, RULE_modeltypeWhere = 8, 
		RULE_transformationDef = 9, RULE_transformationRefine = 10, RULE_libraryDef = 11, 
		RULE_modelParamList = 12, RULE_modelParam = 13, RULE_directionKind = 14, 
		RULE_typeSpec = 15, RULE_moduleUsage = 16, RULE_moduleUsageKind = 17, 
		RULE_moduleRefList = 18, RULE_moduleRef = 19, RULE_accessDecl = 20, RULE_qualifier = 21, 
		RULE_moduleElement = 22, RULE_mappingDef = 23, RULE_mappingSignature = 24, 
		RULE_resultList = 25, RULE_resultParam = 26, RULE_paramList = 27, RULE_param = 28, 
		RULE_mappingExtension = 29, RULE_mappingExtensionKind = 30, RULE_whenClause = 31, 
		RULE_whereClause = 32, RULE_guardBlock = 33, RULE_mappingBody = 34, RULE_initSection = 35, 
		RULE_populationSection = 36, RULE_endSection = 37, RULE_helperDef = 38, 
		RULE_queryDef = 39, RULE_constructorDef = 40, RULE_entryDef = 41, RULE_simpleSignature = 42, 
		RULE_propertyDecl = 43, RULE_intermediateClassDef = 44, RULE_exceptionDef = 45, 
		RULE_datatypeDef = 46, RULE_primitiveDef = 47, RULE_enumDef = 48, RULE_enumLiteralList = 49, 
		RULE_enumLiteral = 50, RULE_metamodelDef = 51, RULE_metamodelElement = 52, 
		RULE_typeList = 53, RULE_classifierFeature = 54, RULE_classifierFeatureModifier = 55, 
		RULE_stereotypeQualifier = 56, RULE_multiplicity = 57, RULE_multiplicityRange = 58, 
		RULE_oppositeProperty = 59, RULE_tagDecl = 60, RULE_tagTarget = 61, RULE_typedefDecl = 62, 
		RULE_statementList = 63, RULE_statement = 64, RULE_assignOp = 65, RULE_block = 66, 
		RULE_expression = 67, RULE_primaryExpression = 68, RULE_typeExpression = 69, 
		RULE_listType = 70, RULE_dictType = 71, RULE_collectionKind = 72, RULE_literalExpression = 73, 
		RULE_stringLiteral_ = 74, RULE_dictLiteral = 75, RULE_dictLiteralPart = 76, 
		RULE_literalSimple = 77, RULE_blockExp = 78, RULE_whileExp = 79, RULE_forExp = 80, 
		RULE_forKind = 81, RULE_forVarList = 82, RULE_switchExp = 83, RULE_switchIterator = 84, 
		RULE_switchAlt = 85, RULE_computeExp = 86, RULE_objectExp = 87, RULE_objectIterator = 88, 
		RULE_newExp = 89, RULE_mappingCallExp = 90, RULE_mappingCallKind = 91, 
		RULE_resolveExp = 92, RULE_resolveKind = 93, RULE_resolveInExp = 94, RULE_resolveInKind = 95, 
		RULE_resolveArgs = 96, RULE_resolveTarget = 97, RULE_tryExp = 98, RULE_catchClause = 99, 
		RULE_exceptionTypeList = 100, RULE_raiseExp = 101, RULE_assertExp = 102, 
		RULE_logCallExp = 103, RULE_logExp = 104, RULE_returnExp = 105, RULE_varDeclExp = 106, 
		RULE_varDeclarator = 107, RULE_varInitOp = 108, RULE_pathName = 109, RULE_propertyOrCallSuffix = 110, 
		RULE_iteratorOrOperationCall = 111, RULE_iteratorVariables = 112, RULE_letBinding = 113, 
		RULE_tupleTypePart = 114, RULE_tupleLiteralPart = 115, RULE_scopedName = 116, 
		RULE_scopedNameList = 117, RULE_qualifiedName = 118, RULE_qvtoIdentifier = 119, 
		RULE_expressionEntry = 120, RULE_completeOclDocumentEntry = 121, RULE_completeOclDocument = 122, 
		RULE_importDeclaration = 123, RULE_packageDeclaration = 124, RULE_contextDeclaration = 125, 
		RULE_classifierContextDeclaration = 126, RULE_classifierContextBody = 127, 
		RULE_invariantConstraint = 128, RULE_definitionConstraint = 129, RULE_operationContextDeclaration = 130, 
		RULE_operationContextBody = 131, RULE_propertyContextDeclaration = 132, 
		RULE_propertyContextBody = 133, RULE_parameterList = 134, RULE_parameter = 135, 
		RULE_argumentList = 136, RULE_isMarkedPre = 137, RULE_collectionLiteral = 138, 
		RULE_collectionLiteralPart = 139, RULE_tupleLiteral = 140, RULE_mapLiteral = 141, 
		RULE_mapLiteralPart = 142, RULE_primitiveType = 143, RULE_collectionType = 144, 
		RULE_mapType = 145, RULE_tupleType = 146;
	private static String[] makeRuleNames() {
		return new String[] {
			"compilationUnitEntry", "compilationUnit", "unitElement", "qvtoImportDecl", 
			"importedNameList", "modeltypeDecl", "packageRefList", "packageRef", 
			"modeltypeWhere", "transformationDef", "transformationRefine", "libraryDef", 
			"modelParamList", "modelParam", "directionKind", "typeSpec", "moduleUsage", 
			"moduleUsageKind", "moduleRefList", "moduleRef", "accessDecl", "qualifier", 
			"moduleElement", "mappingDef", "mappingSignature", "resultList", "resultParam", 
			"paramList", "param", "mappingExtension", "mappingExtensionKind", "whenClause", 
			"whereClause", "guardBlock", "mappingBody", "initSection", "populationSection", 
			"endSection", "helperDef", "queryDef", "constructorDef", "entryDef", 
			"simpleSignature", "propertyDecl", "intermediateClassDef", "exceptionDef", 
			"datatypeDef", "primitiveDef", "enumDef", "enumLiteralList", "enumLiteral", 
			"metamodelDef", "metamodelElement", "typeList", "classifierFeature", 
			"classifierFeatureModifier", "stereotypeQualifier", "multiplicity", "multiplicityRange", 
			"oppositeProperty", "tagDecl", "tagTarget", "typedefDecl", "statementList", 
			"statement", "assignOp", "block", "expression", "primaryExpression", 
			"typeExpression", "listType", "dictType", "collectionKind", "literalExpression", 
			"stringLiteral_", "dictLiteral", "dictLiteralPart", "literalSimple", 
			"blockExp", "whileExp", "forExp", "forKind", "forVarList", "switchExp", 
			"switchIterator", "switchAlt", "computeExp", "objectExp", "objectIterator", 
			"newExp", "mappingCallExp", "mappingCallKind", "resolveExp", "resolveKind", 
			"resolveInExp", "resolveInKind", "resolveArgs", "resolveTarget", "tryExp", 
			"catchClause", "exceptionTypeList", "raiseExp", "assertExp", "logCallExp", 
			"logExp", "returnExp", "varDeclExp", "varDeclarator", "varInitOp", "pathName", 
			"propertyOrCallSuffix", "iteratorOrOperationCall", "iteratorVariables", 
			"letBinding", "tupleTypePart", "tupleLiteralPart", "scopedName", "scopedNameList", 
			"qualifiedName", "qvtoIdentifier", "expressionEntry", "completeOclDocumentEntry", 
			"completeOclDocument", "importDeclaration", "packageDeclaration", "contextDeclaration", 
			"classifierContextDeclaration", "classifierContextBody", "invariantConstraint", 
			"definitionConstraint", "operationContextDeclaration", "operationContextBody", 
			"propertyContextDeclaration", "propertyContextBody", "parameterList", 
			"parameter", "argumentList", "isMarkedPre", "collectionLiteral", "collectionLiteralPart", 
			"tupleLiteral", "mapLiteral", "mapLiteralPart", "primitiveType", "collectionType", 
			"mapType", "tupleType"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'import'", "';'", "'from'", "'*'", "','", "'modeltype'", "'uses'", 
			"'('", "')'", "'where'", "'{'", "'}'", "'transformation'", "'refines'", 
			"'library'", "':'", "'in'", "'out'", "'inout'", "'@'", "'access'", "'extends'", 
			"'abstract'", "'blackbox'", "'static'", "'mapping'", "'inherits'", "'merges'", 
			"'disjuncts'", "'when'", "'init'", "'population'", "'end'", "'helper'", 
			"'='", "'query'", "'constructor'", "'main'", "'entry'", "'intermediate'", 
			"'property'", "'configuration'", "'class'", "'exception'", "'datatype'", 
			"'primitive'", "'enum'", "'metamodel'", "'package'", "'ordered'", "'readonly'", 
			"'references'", "'composes'", "'derived'", "'['", "']'", "'..'", "'opposites'", 
			"'~'", "'tag'", "'::'", "'typedef'", "'with'", "'.'", "'?.'", "'->'", 
			"'?->'", "'|'", "'!'", "'not'", "'-'", "'/'", "'%'", "'+'", "'<'", "'>'", 
			"'<='", "'>='", "'<>'", "'and'", "'or'", "'xor'", "'implies'", "'default'", 
			"'self'", "'this'", "'if'", "'then'", "'elseif'", "'elif'", "'else'", 
			"'endif'", "'let'", "'break'", "'continue'", "'List'", "'Dict'", "'Set'", 
			"'OrderedSet'", "'Bag'", "'Sequence'", "'Collection'", "'true'", "'false'", 
			"'null'", "'invalid'", "'unlimited'", "'do'", "'while'", "'forEach'", 
			"'forOne'", "'switch'", "'case'", "'compute'", "'object'", "'new'", "'map'", 
			"'xmap'", "'late'", "'resolve'", "'resolveone'", "'invresolve'", "'invresolveone'", 
			"'resolveIn'", "'resolveoneIn'", "'invresolveIn'", "'invresolveoneIn'", 
			"'try'", "'catch'", "'except'", "'raise'", "'assert'", "'log'", "'return'", 
			"'var'", "'inv'", "'result'", "'include'", "'endpackage'", "'context'", 
			"'def'", "'pre'", "'post'", "'body'", "'derive'", "'Tuple'", "'Map'", 
			"'<-'", "'Boolean'", "'Integer'", "'Real'", "'String'", "'UnlimitedNatural'", 
			"'OclAny'", "'OclVoid'", "'OclInvalid'", "'OclMessage'", null, "':='", 
			"'+='", "'::='", "'-='", "'##'", "'#'", "'!='", "'!->'", "'<<'", "'>>'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, "LINE_COMMENT", "RESET_ASSIGN", "ADD_ASSIGN", "ORDERED_COPY", 
			"SUBTRACT_ASSIGN", "DOUBLE_HASH", "HASH", "NOT_EQUAL_EXEQ", "NOT_ARROW", 
			"LCHEVRON2", "RCHEVRON2", "DOUBLE_QUOTED_STRING", "REAL_LITERAL", "INTEGER_LITERAL", 
			"STRING_LITERAL", "IDENTIFIER", "WS", "BLOCK_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "QvtO.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public QvtOParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompilationUnitEntryContext extends ParserRuleContext {
		public CompilationUnitContext compilationUnit() {
			return getRuleContext(CompilationUnitContext.class,0);
		}
		public TerminalNode EOF() { return getToken(QvtOParser.EOF, 0); }
		public CompilationUnitEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compilationUnitEntry; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitCompilationUnitEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompilationUnitEntryContext compilationUnitEntry() throws RecognitionException {
		CompilationUnitEntryContext _localctx = new CompilationUnitEntryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_compilationUnitEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(294);
			compilationUnit();
			setState(295);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompilationUnitContext extends ParserRuleContext {
		public List<UnitElementContext> unitElement() {
			return getRuleContexts(UnitElementContext.class);
		}
		public UnitElementContext unitElement(int i) {
			return getRuleContext(UnitElementContext.class,i);
		}
		public CompilationUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compilationUnit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitCompilationUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompilationUnitContext compilationUnit() throws RecognitionException {
		CompilationUnitContext _localctx = new CompilationUnitContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_compilationUnit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 5765724575436415050L) != 0)) {
				{
				{
				setState(297);
				unitElement();
				}
				}
				setState(302);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnitElementContext extends ParserRuleContext {
		public QvtoImportDeclContext qvtoImportDecl() {
			return getRuleContext(QvtoImportDeclContext.class,0);
		}
		public ModeltypeDeclContext modeltypeDecl() {
			return getRuleContext(ModeltypeDeclContext.class,0);
		}
		public TransformationDefContext transformationDef() {
			return getRuleContext(TransformationDefContext.class,0);
		}
		public LibraryDefContext libraryDef() {
			return getRuleContext(LibraryDefContext.class,0);
		}
		public AccessDeclContext accessDecl() {
			return getRuleContext(AccessDeclContext.class,0);
		}
		public ModuleElementContext moduleElement() {
			return getRuleContext(ModuleElementContext.class,0);
		}
		public UnitElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unitElement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitUnitElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnitElementContext unitElement() throws RecognitionException {
		UnitElementContext _localctx = new UnitElementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_unitElement);
		try {
			setState(309);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(303);
				qvtoImportDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(304);
				modeltypeDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(305);
				transformationDef();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(306);
				libraryDef();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(307);
				accessDecl();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(308);
				moduleElement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QvtoImportDeclContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public ImportedNameListContext importedNameList() {
			return getRuleContext(ImportedNameListContext.class,0);
		}
		public QvtoImportDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qvtoImportDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitQvtoImportDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QvtoImportDeclContext qvtoImportDecl() throws RecognitionException {
		QvtoImportDeclContext _localctx = new QvtoImportDeclContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_qvtoImportDecl);
		try {
			setState(321);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(311);
				match(T__0);
				setState(312);
				qualifiedName();
				setState(313);
				match(T__1);
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 2);
				{
				setState(315);
				match(T__2);
				setState(316);
				qualifiedName();
				setState(317);
				match(T__0);
				setState(318);
				importedNameList();
				setState(319);
				match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ImportedNameListContext extends ParserRuleContext {
		public List<QvtoIdentifierContext> qvtoIdentifier() {
			return getRuleContexts(QvtoIdentifierContext.class);
		}
		public QvtoIdentifierContext qvtoIdentifier(int i) {
			return getRuleContext(QvtoIdentifierContext.class,i);
		}
		public ImportedNameListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importedNameList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitImportedNameList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportedNameListContext importedNameList() throws RecognitionException {
		ImportedNameListContext _localctx = new ImportedNameListContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_importedNameList);
		int _la;
		try {
			setState(332);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
				enterOuterAlt(_localctx, 1);
				{
				setState(323);
				match(T__3);
				}
				break;
			case T__2:
			case T__5:
			case T__6:
			case T__9:
			case T__12:
			case T__14:
			case T__20:
			case T__21:
			case T__22:
			case T__23:
			case T__24:
			case T__25:
			case T__26:
			case T__27:
			case T__28:
			case T__29:
			case T__30:
			case T__31:
			case T__32:
			case T__33:
			case T__35:
			case T__36:
			case T__37:
			case T__38:
			case T__39:
			case T__40:
			case T__41:
			case T__43:
			case T__44:
			case T__45:
			case T__46:
			case T__47:
			case T__48:
			case T__49:
			case T__50:
			case T__51:
			case T__52:
			case T__53:
			case T__57:
			case T__59:
			case T__61:
			case T__62:
			case T__93:
			case T__94:
			case T__106:
			case T__107:
			case T__108:
			case T__109:
			case T__110:
			case T__111:
			case T__112:
			case T__113:
			case T__114:
			case T__115:
			case T__116:
			case T__117:
			case T__118:
			case T__119:
			case T__120:
			case T__121:
			case T__122:
			case T__123:
			case T__124:
			case T__125:
			case T__126:
			case T__127:
			case T__128:
			case T__129:
			case T__130:
			case T__131:
			case T__132:
			case T__133:
			case T__134:
			case T__135:
			case T__136:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(324);
				qvtoIdentifier();
				setState(329);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(325);
					match(T__4);
					setState(326);
					qvtoIdentifier();
					}
					}
					setState(331);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModeltypeDeclContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public PackageRefListContext packageRefList() {
			return getRuleContext(PackageRefListContext.class,0);
		}
		public TerminalNode STRING_LITERAL() { return getToken(QvtOParser.STRING_LITERAL, 0); }
		public ModeltypeWhereContext modeltypeWhere() {
			return getRuleContext(ModeltypeWhereContext.class,0);
		}
		public ModeltypeDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modeltypeDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitModeltypeDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModeltypeDeclContext modeltypeDecl() throws RecognitionException {
		ModeltypeDeclContext _localctx = new ModeltypeDeclContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_modeltypeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(334);
			match(T__5);
			setState(335);
			qvtoIdentifier();
			setState(337);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING_LITERAL) {
				{
				setState(336);
				match(STRING_LITERAL);
				}
			}

			setState(339);
			match(T__6);
			setState(340);
			packageRefList();
			setState(342);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(341);
				modeltypeWhere();
				}
			}

			setState(344);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PackageRefListContext extends ParserRuleContext {
		public List<PackageRefContext> packageRef() {
			return getRuleContexts(PackageRefContext.class);
		}
		public PackageRefContext packageRef(int i) {
			return getRuleContext(PackageRefContext.class,i);
		}
		public PackageRefListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageRefList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPackageRefList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageRefListContext packageRefList() throws RecognitionException {
		PackageRefListContext _localctx = new PackageRefListContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_packageRefList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(346);
			packageRef();
			setState(351);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(347);
				match(T__4);
				setState(348);
				packageRef();
				}
				}
				setState(353);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PackageRefContext extends ParserRuleContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode STRING_LITERAL() { return getToken(QvtOParser.STRING_LITERAL, 0); }
		public PackageRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageRef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPackageRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageRefContext packageRef() throws RecognitionException {
		PackageRefContext _localctx = new PackageRefContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_packageRef);
		int _la;
		try {
			setState(361);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case T__5:
			case T__6:
			case T__9:
			case T__12:
			case T__14:
			case T__20:
			case T__21:
			case T__22:
			case T__23:
			case T__24:
			case T__25:
			case T__26:
			case T__27:
			case T__28:
			case T__29:
			case T__30:
			case T__31:
			case T__32:
			case T__33:
			case T__35:
			case T__36:
			case T__37:
			case T__38:
			case T__39:
			case T__40:
			case T__41:
			case T__43:
			case T__44:
			case T__45:
			case T__46:
			case T__47:
			case T__48:
			case T__49:
			case T__50:
			case T__51:
			case T__52:
			case T__53:
			case T__57:
			case T__59:
			case T__61:
			case T__62:
			case T__93:
			case T__94:
			case T__106:
			case T__107:
			case T__108:
			case T__109:
			case T__110:
			case T__111:
			case T__112:
			case T__113:
			case T__114:
			case T__115:
			case T__116:
			case T__117:
			case T__118:
			case T__119:
			case T__120:
			case T__121:
			case T__122:
			case T__123:
			case T__124:
			case T__125:
			case T__126:
			case T__127:
			case T__128:
			case T__129:
			case T__130:
			case T__131:
			case T__132:
			case T__133:
			case T__134:
			case T__135:
			case T__136:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(354);
				pathName();
				setState(358);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(355);
					match(T__7);
					setState(356);
					match(STRING_LITERAL);
					setState(357);
					match(T__8);
					}
				}

				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(360);
				match(STRING_LITERAL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModeltypeWhereContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ModeltypeWhereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modeltypeWhere; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitModeltypeWhere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModeltypeWhereContext modeltypeWhere() throws RecognitionException {
		ModeltypeWhereContext _localctx = new ModeltypeWhereContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_modeltypeWhere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363);
			match(T__9);
			setState(364);
			match(T__10);
			setState(365);
			expression(0);
			setState(370);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(366);
				match(T__4);
				setState(367);
				expression(0);
				}
				}
				setState(372);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(373);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TransformationDefContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TransformationRefineContext transformationRefine() {
			return getRuleContext(TransformationRefineContext.class,0);
		}
		public List<QualifierContext> qualifier() {
			return getRuleContexts(QualifierContext.class);
		}
		public QualifierContext qualifier(int i) {
			return getRuleContext(QualifierContext.class,i);
		}
		public ModelParamListContext modelParamList() {
			return getRuleContext(ModelParamListContext.class,0);
		}
		public List<ModuleUsageContext> moduleUsage() {
			return getRuleContexts(ModuleUsageContext.class);
		}
		public ModuleUsageContext moduleUsage(int i) {
			return getRuleContext(ModuleUsageContext.class,i);
		}
		public List<ModuleElementContext> moduleElement() {
			return getRuleContexts(ModuleElementContext.class);
		}
		public ModuleElementContext moduleElement(int i) {
			return getRuleContext(ModuleElementContext.class,i);
		}
		public TransformationDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transformationDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTransformationDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TransformationDefContext transformationDef() throws RecognitionException {
		TransformationDefContext _localctx = new TransformationDefContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_transformationDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(378);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 58720256L) != 0)) {
				{
				{
				setState(375);
				qualifier();
				}
				}
				setState(380);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(381);
			match(T__12);
			setState(382);
			qualifiedName();
			setState(383);
			match(T__7);
			setState(385);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 917504L) != 0)) {
				{
				setState(384);
				modelParamList();
				}
			}

			setState(387);
			match(T__8);
			setState(395);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
			case T__10:
			case T__20:
			case T__21:
				{
				setState(391);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__20 || _la==T__21) {
					{
					{
					setState(388);
					moduleUsage();
					}
					}
					setState(393);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__13:
				{
				setState(394);
				transformationRefine();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(409);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__10:
				{
				setState(397);
				match(T__10);
				setState(401);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 5765724575436374016L) != 0)) {
					{
					{
					setState(398);
					moduleElement();
					}
					}
					setState(403);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(404);
				match(T__11);
				setState(406);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(405);
					match(T__1);
					}
				}

				}
				break;
			case T__1:
				{
				setState(408);
				match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TransformationRefineContext extends ParserRuleContext {
		public ModuleRefContext moduleRef() {
			return getRuleContext(ModuleRefContext.class,0);
		}
		public TransformationRefineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transformationRefine; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTransformationRefine(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TransformationRefineContext transformationRefine() throws RecognitionException {
		TransformationRefineContext _localctx = new TransformationRefineContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_transformationRefine);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(T__13);
			setState(412);
			moduleRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LibraryDefContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public SimpleSignatureContext simpleSignature() {
			return getRuleContext(SimpleSignatureContext.class,0);
		}
		public List<ModuleUsageContext> moduleUsage() {
			return getRuleContexts(ModuleUsageContext.class);
		}
		public ModuleUsageContext moduleUsage(int i) {
			return getRuleContext(ModuleUsageContext.class,i);
		}
		public List<ModuleElementContext> moduleElement() {
			return getRuleContexts(ModuleElementContext.class);
		}
		public ModuleElementContext moduleElement(int i) {
			return getRuleContext(ModuleElementContext.class,i);
		}
		public LibraryDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_libraryDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitLibraryDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LibraryDefContext libraryDef() throws RecognitionException {
		LibraryDefContext _localctx = new LibraryDefContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_libraryDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(414);
			match(T__14);
			setState(415);
			qualifiedName();
			setState(417);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(416);
				simpleSignature();
				}
			}

			setState(422);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__20 || _la==T__21) {
				{
				{
				setState(419);
				moduleUsage();
				}
				}
				setState(424);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(425);
			match(T__10);
			setState(429);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 5765724575436374016L) != 0)) {
				{
				{
				setState(426);
				moduleElement();
				}
				}
				setState(431);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(432);
			match(T__11);
			setState(434);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(433);
				match(T__1);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModelParamListContext extends ParserRuleContext {
		public List<ModelParamContext> modelParam() {
			return getRuleContexts(ModelParamContext.class);
		}
		public ModelParamContext modelParam(int i) {
			return getRuleContext(ModelParamContext.class,i);
		}
		public ModelParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modelParamList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitModelParamList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModelParamListContext modelParamList() throws RecognitionException {
		ModelParamListContext _localctx = new ModelParamListContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_modelParamList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(436);
			modelParam();
			setState(441);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(437);
				match(T__4);
				setState(438);
				modelParam();
				}
				}
				setState(443);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModelParamContext extends ParserRuleContext {
		public DirectionKindContext directionKind() {
			return getRuleContext(DirectionKindContext.class,0);
		}
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeSpecContext typeSpec() {
			return getRuleContext(TypeSpecContext.class,0);
		}
		public ModelParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modelParam; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitModelParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModelParamContext modelParam() throws RecognitionException {
		ModelParamContext _localctx = new ModelParamContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_modelParam);
		try {
			setState(452);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(444);
				directionKind();
				setState(445);
				qvtoIdentifier();
				setState(446);
				match(T__15);
				setState(447);
				typeSpec();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(449);
				directionKind();
				setState(450);
				typeSpec();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirectionKindContext extends ParserRuleContext {
		public DirectionKindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directionKind; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitDirectionKind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectionKindContext directionKind() throws RecognitionException {
		DirectionKindContext _localctx = new DirectionKindContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_directionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(454);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 917504L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeSpecContext extends ParserRuleContext {
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTypeSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSpecContext typeSpec() throws RecognitionException {
		TypeSpecContext _localctx = new TypeSpecContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(456);
			typeExpression();
			setState(459);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__19) {
				{
				setState(457);
				match(T__19);
				setState(458);
				qvtoIdentifier();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleUsageContext extends ParserRuleContext {
		public ModuleUsageKindContext moduleUsageKind() {
			return getRuleContext(ModuleUsageKindContext.class,0);
		}
		public ModuleRefListContext moduleRefList() {
			return getRuleContext(ModuleRefListContext.class,0);
		}
		public ModuleUsageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleUsage; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitModuleUsage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleUsageContext moduleUsage() throws RecognitionException {
		ModuleUsageContext _localctx = new ModuleUsageContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_moduleUsage);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(461);
			moduleUsageKind();
			setState(463);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				{
				setState(462);
				_la = _input.LA(1);
				if ( !(_la==T__12 || _la==T__14) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(465);
			moduleRefList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleUsageKindContext extends ParserRuleContext {
		public ModuleUsageKindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleUsageKind; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitModuleUsageKind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleUsageKindContext moduleUsageKind() throws RecognitionException {
		ModuleUsageKindContext _localctx = new ModuleUsageKindContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_moduleUsageKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(467);
			_la = _input.LA(1);
			if ( !(_la==T__20 || _la==T__21) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleRefListContext extends ParserRuleContext {
		public List<ModuleRefContext> moduleRef() {
			return getRuleContexts(ModuleRefContext.class);
		}
		public ModuleRefContext moduleRef(int i) {
			return getRuleContext(ModuleRefContext.class,i);
		}
		public ModuleRefListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleRefList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitModuleRefList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleRefListContext moduleRefList() throws RecognitionException {
		ModuleRefListContext _localctx = new ModuleRefListContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_moduleRefList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(469);
			moduleRef();
			setState(474);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(470);
				match(T__4);
				setState(471);
				moduleRef();
				}
				}
				setState(476);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleRefContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public SimpleSignatureContext simpleSignature() {
			return getRuleContext(SimpleSignatureContext.class,0);
		}
		public ModuleRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleRef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitModuleRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleRefContext moduleRef() throws RecognitionException {
		ModuleRefContext _localctx = new ModuleRefContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_moduleRef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(477);
			qualifiedName();
			setState(479);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(478);
				simpleSignature();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AccessDeclContext extends ParserRuleContext {
		public ModuleRefListContext moduleRefList() {
			return getRuleContext(ModuleRefListContext.class,0);
		}
		public AccessDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_accessDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitAccessDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AccessDeclContext accessDecl() throws RecognitionException {
		AccessDeclContext _localctx = new AccessDeclContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_accessDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(481);
			match(T__20);
			setState(483);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				{
				setState(482);
				_la = _input.LA(1);
				if ( !(_la==T__12 || _la==T__14) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(485);
			moduleRefList();
			setState(486);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QualifierContext extends ParserRuleContext {
		public QualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitQualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifierContext qualifier() throws RecognitionException {
		QualifierContext _localctx = new QualifierContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_qualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(488);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 58720256L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleElementContext extends ParserRuleContext {
		public MappingDefContext mappingDef() {
			return getRuleContext(MappingDefContext.class,0);
		}
		public HelperDefContext helperDef() {
			return getRuleContext(HelperDefContext.class,0);
		}
		public QueryDefContext queryDef() {
			return getRuleContext(QueryDefContext.class,0);
		}
		public ConstructorDefContext constructorDef() {
			return getRuleContext(ConstructorDefContext.class,0);
		}
		public EntryDefContext entryDef() {
			return getRuleContext(EntryDefContext.class,0);
		}
		public PropertyDeclContext propertyDecl() {
			return getRuleContext(PropertyDeclContext.class,0);
		}
		public IntermediateClassDefContext intermediateClassDef() {
			return getRuleContext(IntermediateClassDefContext.class,0);
		}
		public ExceptionDefContext exceptionDef() {
			return getRuleContext(ExceptionDefContext.class,0);
		}
		public DatatypeDefContext datatypeDef() {
			return getRuleContext(DatatypeDefContext.class,0);
		}
		public PrimitiveDefContext primitiveDef() {
			return getRuleContext(PrimitiveDefContext.class,0);
		}
		public EnumDefContext enumDef() {
			return getRuleContext(EnumDefContext.class,0);
		}
		public MetamodelDefContext metamodelDef() {
			return getRuleContext(MetamodelDefContext.class,0);
		}
		public TagDeclContext tagDecl() {
			return getRuleContext(TagDeclContext.class,0);
		}
		public TypedefDeclContext typedefDecl() {
			return getRuleContext(TypedefDeclContext.class,0);
		}
		public AccessDeclContext accessDecl() {
			return getRuleContext(AccessDeclContext.class,0);
		}
		public ModuleElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleElement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitModuleElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleElementContext moduleElement() throws RecognitionException {
		ModuleElementContext _localctx = new ModuleElementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_moduleElement);
		try {
			setState(509);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(490);
				mappingDef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(491);
				helperDef();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(492);
				queryDef();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(493);
				constructorDef();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(494);
				entryDef();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(495);
				propertyDecl();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(496);
				intermediateClassDef();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(497);
				exceptionDef();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(498);
				datatypeDef();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(499);
				primitiveDef();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(500);
				enumDef();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(501);
				metamodelDef();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(502);
				tagDecl();
				setState(503);
				match(T__1);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(505);
				typedefDecl();
				setState(506);
				match(T__1);
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(508);
				accessDecl();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MappingDefContext extends ParserRuleContext {
		public ScopedNameContext scopedName() {
			return getRuleContext(ScopedNameContext.class,0);
		}
		public MappingSignatureContext mappingSignature() {
			return getRuleContext(MappingSignatureContext.class,0);
		}
		public MappingBodyContext mappingBody() {
			return getRuleContext(MappingBodyContext.class,0);
		}
		public List<QualifierContext> qualifier() {
			return getRuleContexts(QualifierContext.class);
		}
		public QualifierContext qualifier(int i) {
			return getRuleContext(QualifierContext.class,i);
		}
		public DirectionKindContext directionKind() {
			return getRuleContext(DirectionKindContext.class,0);
		}
		public List<MappingExtensionContext> mappingExtension() {
			return getRuleContexts(MappingExtensionContext.class);
		}
		public MappingExtensionContext mappingExtension(int i) {
			return getRuleContext(MappingExtensionContext.class,i);
		}
		public WhenClauseContext whenClause() {
			return getRuleContext(WhenClauseContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public MappingDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mappingDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMappingDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MappingDefContext mappingDef() throws RecognitionException {
		MappingDefContext _localctx = new MappingDefContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_mappingDef);
		int _la;
		try {
			setState(559);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(514);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 58720256L) != 0)) {
					{
					{
					setState(511);
					qualifier();
					}
					}
					setState(516);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(517);
				match(T__25);
				setState(519);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 917504L) != 0)) {
					{
					setState(518);
					directionKind();
					}
				}

				setState(521);
				scopedName();
				setState(522);
				mappingSignature();
				setState(526);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 939540480L) != 0)) {
					{
					{
					setState(523);
					mappingExtension();
					}
					}
					setState(528);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(530);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__29) {
					{
					setState(529);
					whenClause();
					}
				}

				setState(533);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__9) {
					{
					setState(532);
					whereClause();
					}
				}

				setState(535);
				mappingBody();
				setState(537);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(536);
					match(T__1);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(542);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 58720256L) != 0)) {
					{
					{
					setState(539);
					qualifier();
					}
					}
					setState(544);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(545);
				match(T__25);
				setState(547);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 917504L) != 0)) {
					{
					setState(546);
					directionKind();
					}
				}

				setState(549);
				scopedName();
				setState(550);
				mappingSignature();
				setState(554);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 939540480L) != 0)) {
					{
					{
					setState(551);
					mappingExtension();
					}
					}
					setState(556);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(557);
				match(T__1);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MappingSignatureContext extends ParserRuleContext {
		public ParamListContext inputParams;
		public ResultListContext resultList() {
			return getRuleContext(ResultListContext.class,0);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public MappingSignatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mappingSignature; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMappingSignature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MappingSignatureContext mappingSignature() throws RecognitionException {
		MappingSignatureContext _localctx = new MappingSignatureContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_mappingSignature);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(561);
			match(T__7);
			setState(563);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171103763256L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 17592186036227L) != 0) || _la==IDENTIFIER) {
				{
				setState(562);
				((MappingSignatureContext)_localctx).inputParams = paramList();
				}
			}

			setState(565);
			match(T__8);
			setState(568);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__15) {
				{
				setState(566);
				match(T__15);
				setState(567);
				resultList();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ResultListContext extends ParserRuleContext {
		public List<ResultParamContext> resultParam() {
			return getRuleContexts(ResultParamContext.class);
		}
		public ResultParamContext resultParam(int i) {
			return getRuleContext(ResultParamContext.class,i);
		}
		public ResultListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resultList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitResultList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResultListContext resultList() throws RecognitionException {
		ResultListContext _localctx = new ResultListContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_resultList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(570);
			resultParam();
			setState(575);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(571);
				match(T__4);
				setState(572);
				resultParam();
				}
				}
				setState(577);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ResultParamContext extends ParserRuleContext {
		public ResultParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resultParam; }
	 
		public ResultParamContext() { }
		public void copyFrom(ResultParamContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnnamedResultContext extends ResultParamContext {
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public UnnamedResultContext(ResultParamContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitUnnamedResult(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NamedResultContext extends ResultParamContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public NamedResultContext(ResultParamContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitNamedResult(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResultParamContext resultParam() throws RecognitionException {
		ResultParamContext _localctx = new ResultParamContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_resultParam);
		try {
			setState(583);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				_localctx = new NamedResultContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(578);
				qvtoIdentifier();
				setState(579);
				match(T__15);
				setState(580);
				typeExpression();
				}
				break;
			case 2:
				_localctx = new UnnamedResultContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(582);
				typeExpression();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamListContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public ParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitParamList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamListContext paramList() throws RecognitionException {
		ParamListContext _localctx = new ParamListContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(585);
			param();
			setState(590);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(586);
				match(T__4);
				setState(587);
				param();
				}
				}
				setState(592);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public DirectionKindContext directionKind() {
			return getRuleContext(DirectionKindContext.class,0);
		}
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(594);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 917504L) != 0)) {
				{
				setState(593);
				directionKind();
				}
			}

			setState(596);
			qvtoIdentifier();
			setState(597);
			match(T__15);
			setState(598);
			typeExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MappingExtensionContext extends ParserRuleContext {
		public MappingExtensionKindContext mappingExtensionKind() {
			return getRuleContext(MappingExtensionKindContext.class,0);
		}
		public ScopedNameListContext scopedNameList() {
			return getRuleContext(ScopedNameListContext.class,0);
		}
		public ScopedNameContext scopedName() {
			return getRuleContext(ScopedNameContext.class,0);
		}
		public MappingExtensionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mappingExtension; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMappingExtension(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MappingExtensionContext mappingExtension() throws RecognitionException {
		MappingExtensionContext _localctx = new MappingExtensionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_mappingExtension);
		try {
			setState(605);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__26:
			case T__27:
			case T__28:
				enterOuterAlt(_localctx, 1);
				{
				setState(600);
				mappingExtensionKind();
				setState(601);
				scopedNameList();
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 2);
				{
				setState(603);
				match(T__13);
				setState(604);
				scopedName();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MappingExtensionKindContext extends ParserRuleContext {
		public MappingExtensionKindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mappingExtensionKind; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMappingExtensionKind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MappingExtensionKindContext mappingExtensionKind() throws RecognitionException {
		MappingExtensionKindContext _localctx = new MappingExtensionKindContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_mappingExtensionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(607);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 939524096L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhenClauseContext extends ParserRuleContext {
		public GuardBlockContext guardBlock() {
			return getRuleContext(GuardBlockContext.class,0);
		}
		public WhenClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whenClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitWhenClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhenClauseContext whenClause() throws RecognitionException {
		WhenClauseContext _localctx = new WhenClauseContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_whenClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(609);
			match(T__29);
			setState(610);
			guardBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereClauseContext extends ParserRuleContext {
		public GuardBlockContext guardBlock() {
			return getRuleContext(GuardBlockContext.class,0);
		}
		public WhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitWhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(612);
			match(T__9);
			setState(613);
			guardBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GuardBlockContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public GuardBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_guardBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitGuardBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GuardBlockContext guardBlock() throws RecognitionException {
		GuardBlockContext _localctx = new GuardBlockContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_guardBlock);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(615);
			match(T__10);
			setState(616);
			expression(0);
			setState(621);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(617);
					match(T__1);
					setState(618);
					expression(0);
					}
					} 
				}
				setState(623);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			}
			setState(625);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(624);
				match(T__1);
				}
			}

			setState(627);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MappingBodyContext extends ParserRuleContext {
		public PopulationSectionContext populationSection() {
			return getRuleContext(PopulationSectionContext.class,0);
		}
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public InitSectionContext initSection() {
			return getRuleContext(InitSectionContext.class,0);
		}
		public EndSectionContext endSection() {
			return getRuleContext(EndSectionContext.class,0);
		}
		public MappingBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mappingBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMappingBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MappingBodyContext mappingBody() throws RecognitionException {
		MappingBodyContext _localctx = new MappingBodyContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_mappingBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(629);
			match(T__10);
			setState(631);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
			case 1:
				{
				setState(630);
				initSection();
				}
				break;
			}
			setState(635);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(633);
				populationSection();
				}
				break;
			case 2:
				{
				setState(634);
				statementList();
				}
				break;
			}
			setState(638);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__32) {
				{
				setState(637);
				endSection();
				}
			}

			setState(640);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InitSectionContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public InitSectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initSection; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitInitSection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitSectionContext initSection() throws RecognitionException {
		InitSectionContext _localctx = new InitSectionContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_initSection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(642);
			match(T__30);
			setState(643);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PopulationSectionContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public PopulationSectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_populationSection; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPopulationSection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PopulationSectionContext populationSection() throws RecognitionException {
		PopulationSectionContext _localctx = new PopulationSectionContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_populationSection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(645);
			match(T__31);
			setState(646);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EndSectionContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public EndSectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endSection; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitEndSection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EndSectionContext endSection() throws RecognitionException {
		EndSectionContext _localctx = new EndSectionContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_endSection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(648);
			match(T__32);
			setState(649);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HelperDefContext extends ParserRuleContext {
		public ScopedNameContext scopedName() {
			return getRuleContext(ScopedNameContext.class,0);
		}
		public SimpleSignatureContext simpleSignature() {
			return getRuleContext(SimpleSignatureContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<QualifierContext> qualifier() {
			return getRuleContexts(QualifierContext.class);
		}
		public QualifierContext qualifier(int i) {
			return getRuleContext(QualifierContext.class,i);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public HelperDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_helperDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitHelperDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HelperDefContext helperDef() throws RecognitionException {
		HelperDefContext _localctx = new HelperDefContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_helperDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(654);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 58720256L) != 0)) {
				{
				{
				setState(651);
				qualifier();
				}
				}
				setState(656);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(657);
			match(T__33);
			setState(658);
			scopedName();
			setState(659);
			simpleSignature();
			setState(662);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__15) {
				{
				setState(660);
				match(T__15);
				setState(661);
				typeExpression();
				}
			}

			setState(670);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__10:
				{
				setState(664);
				block();
				}
				break;
			case T__34:
				{
				setState(665);
				match(T__34);
				setState(666);
				expression(0);
				setState(667);
				match(T__1);
				}
				break;
			case T__1:
				{
				setState(669);
				match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QueryDefContext extends ParserRuleContext {
		public ScopedNameContext scopedName() {
			return getRuleContext(ScopedNameContext.class,0);
		}
		public SimpleSignatureContext simpleSignature() {
			return getRuleContext(SimpleSignatureContext.class,0);
		}
		public ResultListContext resultList() {
			return getRuleContext(ResultListContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<QualifierContext> qualifier() {
			return getRuleContexts(QualifierContext.class);
		}
		public QualifierContext qualifier(int i) {
			return getRuleContext(QualifierContext.class,i);
		}
		public QueryDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitQueryDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryDefContext queryDef() throws RecognitionException {
		QueryDefContext _localctx = new QueryDefContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_queryDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(675);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 58720256L) != 0)) {
				{
				{
				setState(672);
				qualifier();
				}
				}
				setState(677);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(678);
			match(T__35);
			setState(679);
			scopedName();
			setState(680);
			simpleSignature();
			setState(681);
			match(T__15);
			setState(682);
			resultList();
			setState(689);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__10:
				{
				setState(683);
				block();
				}
				break;
			case T__34:
				{
				setState(684);
				match(T__34);
				setState(685);
				expression(0);
				setState(686);
				match(T__1);
				}
				break;
			case T__1:
				{
				setState(688);
				match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstructorDefContext extends ParserRuleContext {
		public ScopedNameContext scopedName() {
			return getRuleContext(ScopedNameContext.class,0);
		}
		public SimpleSignatureContext simpleSignature() {
			return getRuleContext(SimpleSignatureContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<QualifierContext> qualifier() {
			return getRuleContexts(QualifierContext.class);
		}
		public QualifierContext qualifier(int i) {
			return getRuleContext(QualifierContext.class,i);
		}
		public ConstructorDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructorDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitConstructorDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructorDefContext constructorDef() throws RecognitionException {
		ConstructorDefContext _localctx = new ConstructorDefContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_constructorDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(694);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 58720256L) != 0)) {
				{
				{
				setState(691);
				qualifier();
				}
				}
				setState(696);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(697);
			match(T__36);
			setState(698);
			scopedName();
			setState(699);
			simpleSignature();
			setState(705);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__10:
				{
				setState(700);
				block();
				setState(702);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(701);
					match(T__1);
					}
				}

				}
				break;
			case T__1:
				{
				setState(704);
				match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EntryDefContext extends ParserRuleContext {
		public SimpleSignatureContext simpleSignature() {
			return getRuleContext(SimpleSignatureContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public EntryDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entryDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitEntryDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EntryDefContext entryDef() throws RecognitionException {
		EntryDefContext _localctx = new EntryDefContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_entryDef);
		int _la;
		try {
			setState(721);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__37:
				enterOuterAlt(_localctx, 1);
				{
				setState(707);
				match(T__37);
				setState(708);
				simpleSignature();
				setState(709);
				block();
				setState(711);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(710);
					match(T__1);
					}
				}

				}
				break;
			case T__38:
				enterOuterAlt(_localctx, 2);
				{
				setState(713);
				match(T__38);
				setState(715);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(714);
					simpleSignature();
					}
				}

				setState(717);
				block();
				setState(719);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(718);
					match(T__1);
					}
				}

				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SimpleSignatureContext extends ParserRuleContext {
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public SimpleSignatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleSignature; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitSimpleSignature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleSignatureContext simpleSignature() throws RecognitionException {
		SimpleSignatureContext _localctx = new SimpleSignatureContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_simpleSignature);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(723);
			match(T__7);
			setState(725);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171103763256L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 17592186036227L) != 0) || _la==IDENTIFIER) {
				{
				setState(724);
				paramList();
				}
			}

			setState(727);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyDeclContext extends ParserRuleContext {
		public ScopedNameContext scopedName() {
			return getRuleContext(ScopedNameContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public PropertyDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPropertyDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyDeclContext propertyDecl() throws RecognitionException {
		PropertyDeclContext _localctx = new PropertyDeclContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_propertyDecl);
		int _la;
		try {
			setState(767);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(729);
				match(T__39);
				setState(730);
				match(T__40);
				setState(731);
				scopedName();
				setState(732);
				match(T__15);
				setState(733);
				typeExpression();
				setState(736);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__34) {
					{
					setState(734);
					match(T__34);
					setState(735);
					expression(0);
					}
				}

				setState(738);
				match(T__1);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(740);
				match(T__41);
				setState(741);
				match(T__40);
				setState(742);
				qvtoIdentifier();
				setState(743);
				match(T__15);
				setState(744);
				typeExpression();
				setState(747);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__34) {
					{
					setState(745);
					match(T__34);
					setState(746);
					expression(0);
					}
				}

				setState(749);
				match(T__1);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(751);
				match(T__40);
				setState(752);
				qvtoIdentifier();
				setState(753);
				match(T__15);
				setState(754);
				typeExpression();
				setState(757);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__34) {
					{
					setState(755);
					match(T__34);
					setState(756);
					expression(0);
					}
				}

				setState(759);
				match(T__1);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(761);
				match(T__40);
				setState(762);
				qvtoIdentifier();
				setState(763);
				match(T__34);
				setState(764);
				expression(0);
				setState(765);
				match(T__1);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IntermediateClassDefContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public List<ClassifierFeatureContext> classifierFeature() {
			return getRuleContexts(ClassifierFeatureContext.class);
		}
		public ClassifierFeatureContext classifierFeature(int i) {
			return getRuleContext(ClassifierFeatureContext.class,i);
		}
		public IntermediateClassDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intermediateClassDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitIntermediateClassDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntermediateClassDefContext intermediateClassDef() throws RecognitionException {
		IntermediateClassDefContext _localctx = new IntermediateClassDefContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_intermediateClassDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(769);
			match(T__39);
			setState(770);
			match(T__42);
			setState(771);
			qvtoIdentifier();
			setState(774);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__21) {
				{
				setState(772);
				match(T__21);
				setState(773);
				typeList();
				}
			}

			setState(776);
			match(T__10);
			setState(780);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104680760L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 17592186036227L) != 0) || _la==LCHEVRON2 || _la==IDENTIFIER) {
				{
				{
				setState(777);
				classifierFeature();
				}
				}
				setState(782);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(783);
			match(T__11);
			setState(785);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(784);
				match(T__1);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExceptionDefContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public List<ClassifierFeatureContext> classifierFeature() {
			return getRuleContexts(ClassifierFeatureContext.class);
		}
		public ClassifierFeatureContext classifierFeature(int i) {
			return getRuleContext(ClassifierFeatureContext.class,i);
		}
		public ExceptionDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exceptionDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitExceptionDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExceptionDefContext exceptionDef() throws RecognitionException {
		ExceptionDefContext _localctx = new ExceptionDefContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_exceptionDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(787);
			match(T__43);
			setState(788);
			qvtoIdentifier();
			setState(791);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__21) {
				{
				setState(789);
				match(T__21);
				setState(790);
				typeList();
				}
			}

			setState(793);
			match(T__10);
			setState(797);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104680760L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 17592186036227L) != 0) || _la==LCHEVRON2 || _la==IDENTIFIER) {
				{
				{
				setState(794);
				classifierFeature();
				}
				}
				setState(799);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(800);
			match(T__11);
			setState(802);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(801);
				match(T__1);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DatatypeDefContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public List<ClassifierFeatureContext> classifierFeature() {
			return getRuleContexts(ClassifierFeatureContext.class);
		}
		public ClassifierFeatureContext classifierFeature(int i) {
			return getRuleContext(ClassifierFeatureContext.class,i);
		}
		public DatatypeDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_datatypeDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitDatatypeDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DatatypeDefContext datatypeDef() throws RecognitionException {
		DatatypeDefContext _localctx = new DatatypeDefContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_datatypeDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(804);
			match(T__44);
			setState(805);
			qvtoIdentifier();
			setState(806);
			match(T__10);
			setState(810);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104680760L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 17592186036227L) != 0) || _la==LCHEVRON2 || _la==IDENTIFIER) {
				{
				{
				setState(807);
				classifierFeature();
				}
				}
				setState(812);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(813);
			match(T__11);
			setState(815);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(814);
				match(T__1);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveDefContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public PrimitiveDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPrimitiveDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveDefContext primitiveDef() throws RecognitionException {
		PrimitiveDefContext _localctx = new PrimitiveDefContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_primitiveDef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(817);
			match(T__45);
			setState(818);
			qvtoIdentifier();
			setState(819);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnumDefContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public EnumLiteralListContext enumLiteralList() {
			return getRuleContext(EnumLiteralListContext.class,0);
		}
		public EnumDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitEnumDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumDefContext enumDef() throws RecognitionException {
		EnumDefContext _localctx = new EnumDefContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_enumDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(821);
			match(T__46);
			setState(822);
			qvtoIdentifier();
			setState(823);
			match(T__10);
			setState(825);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104680760L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 17592186036227L) != 0) || ((((_la - 169)) & ~0x3f) == 0 && ((1L << (_la - 169)) & 25L) != 0)) {
				{
				setState(824);
				enumLiteralList();
				}
			}

			setState(827);
			match(T__11);
			setState(829);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(828);
				match(T__1);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnumLiteralListContext extends ParserRuleContext {
		public List<EnumLiteralContext> enumLiteral() {
			return getRuleContexts(EnumLiteralContext.class);
		}
		public EnumLiteralContext enumLiteral(int i) {
			return getRuleContext(EnumLiteralContext.class,i);
		}
		public EnumLiteralListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumLiteralList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitEnumLiteralList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumLiteralListContext enumLiteralList() throws RecognitionException {
		EnumLiteralListContext _localctx = new EnumLiteralListContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_enumLiteralList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(831);
			enumLiteral();
			setState(836);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(832);
				match(T__4);
				setState(833);
				enumLiteral();
				}
				}
				setState(838);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnumLiteralContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL() { return getToken(QvtOParser.STRING_LITERAL, 0); }
		public TerminalNode DOUBLE_QUOTED_STRING() { return getToken(QvtOParser.DOUBLE_QUOTED_STRING, 0); }
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public EnumLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumLiteral; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitEnumLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumLiteralContext enumLiteral() throws RecognitionException {
		EnumLiteralContext _localctx = new EnumLiteralContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_enumLiteral);
		try {
			setState(842);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(839);
				match(STRING_LITERAL);
				}
				break;
			case DOUBLE_QUOTED_STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(840);
				match(DOUBLE_QUOTED_STRING);
				}
				break;
			case T__2:
			case T__5:
			case T__6:
			case T__9:
			case T__12:
			case T__14:
			case T__20:
			case T__21:
			case T__22:
			case T__23:
			case T__24:
			case T__25:
			case T__26:
			case T__27:
			case T__28:
			case T__29:
			case T__30:
			case T__31:
			case T__32:
			case T__33:
			case T__35:
			case T__36:
			case T__37:
			case T__38:
			case T__39:
			case T__40:
			case T__41:
			case T__43:
			case T__44:
			case T__45:
			case T__46:
			case T__47:
			case T__48:
			case T__49:
			case T__50:
			case T__51:
			case T__52:
			case T__53:
			case T__57:
			case T__59:
			case T__61:
			case T__62:
			case T__93:
			case T__94:
			case T__106:
			case T__107:
			case T__108:
			case T__109:
			case T__110:
			case T__111:
			case T__112:
			case T__113:
			case T__114:
			case T__115:
			case T__116:
			case T__117:
			case T__118:
			case T__119:
			case T__120:
			case T__121:
			case T__122:
			case T__123:
			case T__124:
			case T__125:
			case T__126:
			case T__127:
			case T__128:
			case T__129:
			case T__130:
			case T__131:
			case T__132:
			case T__133:
			case T__134:
			case T__135:
			case T__136:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(841);
				qvtoIdentifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetamodelDefContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public List<MetamodelElementContext> metamodelElement() {
			return getRuleContexts(MetamodelElementContext.class);
		}
		public MetamodelElementContext metamodelElement(int i) {
			return getRuleContext(MetamodelElementContext.class,i);
		}
		public MetamodelDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metamodelDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMetamodelDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetamodelDefContext metamodelDef() throws RecognitionException {
		MetamodelDefContext _localctx = new MetamodelDefContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_metamodelDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(844);
			_la = _input.LA(1);
			if ( !(_la==T__47 || _la==T__48) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(845);
			qvtoIdentifier();
			setState(846);
			match(T__10);
			setState(850);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1153186486909140992L) != 0)) {
				{
				{
				setState(847);
				metamodelElement();
				}
				}
				setState(852);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(853);
			match(T__11);
			setState(855);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(854);
				match(T__1);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetamodelElementContext extends ParserRuleContext {
		public IntermediateClassDefContext intermediateClassDef() {
			return getRuleContext(IntermediateClassDefContext.class,0);
		}
		public ExceptionDefContext exceptionDef() {
			return getRuleContext(ExceptionDefContext.class,0);
		}
		public DatatypeDefContext datatypeDef() {
			return getRuleContext(DatatypeDefContext.class,0);
		}
		public PrimitiveDefContext primitiveDef() {
			return getRuleContext(PrimitiveDefContext.class,0);
		}
		public EnumDefContext enumDef() {
			return getRuleContext(EnumDefContext.class,0);
		}
		public TagDeclContext tagDecl() {
			return getRuleContext(TagDeclContext.class,0);
		}
		public MetamodelElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metamodelElement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMetamodelElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetamodelElementContext metamodelElement() throws RecognitionException {
		MetamodelElementContext _localctx = new MetamodelElementContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_metamodelElement);
		try {
			setState(865);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__39:
				enterOuterAlt(_localctx, 1);
				{
				setState(857);
				intermediateClassDef();
				}
				break;
			case T__43:
				enterOuterAlt(_localctx, 2);
				{
				setState(858);
				exceptionDef();
				}
				break;
			case T__44:
				enterOuterAlt(_localctx, 3);
				{
				setState(859);
				datatypeDef();
				}
				break;
			case T__45:
				enterOuterAlt(_localctx, 4);
				{
				setState(860);
				primitiveDef();
				}
				break;
			case T__46:
				enterOuterAlt(_localctx, 5);
				{
				setState(861);
				enumDef();
				}
				break;
			case T__59:
				enterOuterAlt(_localctx, 6);
				{
				setState(862);
				tagDecl();
				setState(863);
				match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeListContext extends ParserRuleContext {
		public List<TypeExpressionContext> typeExpression() {
			return getRuleContexts(TypeExpressionContext.class);
		}
		public TypeExpressionContext typeExpression(int i) {
			return getRuleContext(TypeExpressionContext.class,i);
		}
		public TypeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTypeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeListContext typeList() throws RecognitionException {
		TypeListContext _localctx = new TypeListContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(867);
			typeExpression();
			setState(872);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(868);
				match(T__4);
				setState(869);
				typeExpression();
				}
				}
				setState(874);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassifierFeatureContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public SimpleSignatureContext simpleSignature() {
			return getRuleContext(SimpleSignatureContext.class,0);
		}
		public List<ClassifierFeatureModifierContext> classifierFeatureModifier() {
			return getRuleContexts(ClassifierFeatureModifierContext.class);
		}
		public ClassifierFeatureModifierContext classifierFeatureModifier(int i) {
			return getRuleContext(ClassifierFeatureModifierContext.class,i);
		}
		public MultiplicityContext multiplicity() {
			return getRuleContext(MultiplicityContext.class,0);
		}
		public OppositePropertyContext oppositeProperty() {
			return getRuleContext(OppositePropertyContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ClassifierFeatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classifierFeature; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitClassifierFeature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassifierFeatureContext classifierFeature() throws RecognitionException {
		ClassifierFeatureContext _localctx = new ClassifierFeatureContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_classifierFeature);
		int _la;
		try {
			int _alt;
			setState(911);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(878);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,85,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(875);
						classifierFeatureModifier();
						}
						} 
					}
					setState(880);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,85,_ctx);
				}
				setState(881);
				qvtoIdentifier();
				setState(882);
				match(T__15);
				setState(883);
				typeExpression();
				setState(884);
				simpleSignature();
				setState(885);
				match(T__1);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(890);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,86,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(887);
						classifierFeatureModifier();
						}
						} 
					}
					setState(892);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,86,_ctx);
				}
				setState(893);
				qvtoIdentifier();
				setState(894);
				match(T__15);
				setState(895);
				typeExpression();
				setState(897);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__54) {
					{
					setState(896);
					multiplicity();
					}
				}

				setState(900);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__49) {
					{
					setState(899);
					match(T__49);
					}
				}

				setState(903);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__57) {
					{
					setState(902);
					oppositeProperty();
					}
				}

				setState(907);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__34) {
					{
					setState(905);
					match(T__34);
					setState(906);
					expression(0);
					}
				}

				setState(909);
				match(T__1);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassifierFeatureModifierContext extends ParserRuleContext {
		public StereotypeQualifierContext stereotypeQualifier() {
			return getRuleContext(StereotypeQualifierContext.class,0);
		}
		public ClassifierFeatureModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classifierFeatureModifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitClassifierFeatureModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassifierFeatureModifierContext classifierFeatureModifier() throws RecognitionException {
		ClassifierFeatureModifierContext _localctx = new ClassifierFeatureModifierContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_classifierFeatureModifier);
		try {
			setState(919);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__24:
				enterOuterAlt(_localctx, 1);
				{
				setState(913);
				match(T__24);
				}
				break;
			case T__50:
				enterOuterAlt(_localctx, 2);
				{
				setState(914);
				match(T__50);
				}
				break;
			case T__51:
				enterOuterAlt(_localctx, 3);
				{
				setState(915);
				match(T__51);
				}
				break;
			case T__52:
				enterOuterAlt(_localctx, 4);
				{
				setState(916);
				match(T__52);
				}
				break;
			case T__53:
				enterOuterAlt(_localctx, 5);
				{
				setState(917);
				match(T__53);
				}
				break;
			case LCHEVRON2:
				enterOuterAlt(_localctx, 6);
				{
				setState(918);
				stereotypeQualifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StereotypeQualifierContext extends ParserRuleContext {
		public TerminalNode LCHEVRON2() { return getToken(QvtOParser.LCHEVRON2, 0); }
		public List<QvtoIdentifierContext> qvtoIdentifier() {
			return getRuleContexts(QvtoIdentifierContext.class);
		}
		public QvtoIdentifierContext qvtoIdentifier(int i) {
			return getRuleContext(QvtoIdentifierContext.class,i);
		}
		public TerminalNode RCHEVRON2() { return getToken(QvtOParser.RCHEVRON2, 0); }
		public StereotypeQualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stereotypeQualifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitStereotypeQualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StereotypeQualifierContext stereotypeQualifier() throws RecognitionException {
		StereotypeQualifierContext _localctx = new StereotypeQualifierContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_stereotypeQualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(921);
			match(LCHEVRON2);
			setState(922);
			qvtoIdentifier();
			setState(927);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(923);
				match(T__4);
				setState(924);
				qvtoIdentifier();
				}
				}
				setState(929);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(930);
			match(RCHEVRON2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiplicityContext extends ParserRuleContext {
		public MultiplicityRangeContext multiplicityRange() {
			return getRuleContext(MultiplicityRangeContext.class,0);
		}
		public MultiplicityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicity; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMultiplicity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicityContext multiplicity() throws RecognitionException {
		MultiplicityContext _localctx = new MultiplicityContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_multiplicity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(932);
			match(T__54);
			setState(933);
			multiplicityRange();
			setState(934);
			match(T__55);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiplicityRangeContext extends ParserRuleContext {
		public List<TerminalNode> INTEGER_LITERAL() { return getTokens(QvtOParser.INTEGER_LITERAL); }
		public TerminalNode INTEGER_LITERAL(int i) {
			return getToken(QvtOParser.INTEGER_LITERAL, i);
		}
		public MultiplicityRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicityRange; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMultiplicityRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicityRangeContext multiplicityRange() throws RecognitionException {
		MultiplicityRangeContext _localctx = new MultiplicityRangeContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_multiplicityRange);
		int _la;
		try {
			setState(941);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(936);
				match(INTEGER_LITERAL);
				setState(937);
				match(T__56);
				setState(938);
				_la = _input.LA(1);
				if ( !(_la==T__3 || _la==INTEGER_LITERAL) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(939);
				match(INTEGER_LITERAL);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(940);
				match(T__3);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OppositePropertyContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public MultiplicityContext multiplicity() {
			return getRuleContext(MultiplicityContext.class,0);
		}
		public OppositePropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oppositeProperty; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitOppositeProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OppositePropertyContext oppositeProperty() throws RecognitionException {
		OppositePropertyContext _localctx = new OppositePropertyContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_oppositeProperty);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(943);
			match(T__57);
			setState(945);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__58) {
				{
				setState(944);
				match(T__58);
				}
			}

			setState(947);
			qvtoIdentifier();
			setState(949);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__54) {
				{
				setState(948);
				multiplicity();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TagDeclContext extends ParserRuleContext {
		public TagTargetContext tagTarget() {
			return getRuleContext(TagTargetContext.class,0);
		}
		public TerminalNode STRING_LITERAL() { return getToken(QvtOParser.STRING_LITERAL, 0); }
		public TerminalNode DOUBLE_QUOTED_STRING() { return getToken(QvtOParser.DOUBLE_QUOTED_STRING, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TagDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tagDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTagDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagDeclContext tagDecl() throws RecognitionException {
		TagDeclContext _localctx = new TagDeclContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_tagDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(951);
			match(T__59);
			setState(952);
			_la = _input.LA(1);
			if ( !(_la==DOUBLE_QUOTED_STRING || _la==STRING_LITERAL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(953);
			tagTarget();
			setState(956);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__34) {
				{
				setState(954);
				match(T__34);
				setState(955);
				expression(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TagTargetContext extends ParserRuleContext {
		public List<QvtoIdentifierContext> qvtoIdentifier() {
			return getRuleContexts(QvtoIdentifierContext.class);
		}
		public QvtoIdentifierContext qvtoIdentifier(int i) {
			return getRuleContext(QvtoIdentifierContext.class,i);
		}
		public TagTargetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tagTarget; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTagTarget(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagTargetContext tagTarget() throws RecognitionException {
		TagTargetContext _localctx = new TagTargetContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_tagTarget);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(958);
			qvtoIdentifier();
			setState(963);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__60) {
				{
				{
				setState(959);
				match(T__60);
				setState(960);
				qvtoIdentifier();
				}
				}
				setState(965);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypedefDeclContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TypedefDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedefDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTypedefDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedefDeclContext typedefDecl() throws RecognitionException {
		TypedefDeclContext _localctx = new TypedefDeclContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_typedefDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(966);
			match(T__61);
			setState(967);
			qvtoIdentifier();
			setState(968);
			match(T__34);
			setState(969);
			typeExpression();
			setState(972);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__62) {
				{
				setState(970);
				match(T__62);
				setState(971);
				expression(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementListContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public StatementListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitStatementList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementListContext statementList() throws RecognitionException {
		StatementListContext _localctx = new StatementListContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_statementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(977);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,100,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(974);
					statement();
					}
					} 
				}
				setState(979);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,100,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionStatementContext extends StatementContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitExpressionStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VarDeclStatementContext extends StatementContext {
		public VarDeclExpContext varDeclExp() {
			return getRuleContext(VarDeclExpContext.class,0);
		}
		public VarDeclStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitVarDeclStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_statement);
		try {
			setState(986);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
			case 1:
				_localctx = new VarDeclStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(980);
				varDeclExp();
				setState(981);
				match(T__1);
				}
				break;
			case 2:
				_localctx = new ExpressionStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(983);
				expression(0);
				setState(984);
				match(T__1);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignOpContext extends ParserRuleContext {
		public TerminalNode RESET_ASSIGN() { return getToken(QvtOParser.RESET_ASSIGN, 0); }
		public TerminalNode ADD_ASSIGN() { return getToken(QvtOParser.ADD_ASSIGN, 0); }
		public TerminalNode ORDERED_COPY() { return getToken(QvtOParser.ORDERED_COPY, 0); }
		public TerminalNode SUBTRACT_ASSIGN() { return getToken(QvtOParser.SUBTRACT_ASSIGN, 0); }
		public AssignOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignOp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitAssignOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignOpContext assignOp() throws RecognitionException {
		AssignOpContext _localctx = new AssignOpContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_assignOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(988);
			_la = _input.LA(1);
			if ( !(((((_la - 159)) & ~0x3f) == 0 && ((1L << (_la - 159)) & 15L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(990);
			match(T__10);
			setState(994);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
				{
				{
				setState(991);
				statement();
				}
				}
				setState(996);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(997);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NotExpContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public NotExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitNotExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AndExpContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AndExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitAndExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CompareExpContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public CompareExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitCompareExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryStarExpContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryStarExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitUnaryStarExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MultExpContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public MultExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMultExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class HashExpContext extends ExpressionContext {
		public TerminalNode HASH() { return getToken(QvtOParser.HASH, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public HashExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitHashExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DoubleHashExpContext extends ExpressionContext {
		public TerminalNode DOUBLE_HASH() { return getToken(QvtOParser.DOUBLE_HASH, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DoubleHashExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitDoubleHashExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AddExpContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AddExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitAddExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NavigationExpContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PropertyOrCallSuffixContext propertyOrCallSuffix() {
			return getRuleContext(PropertyOrCallSuffixContext.class,0);
		}
		public NavigationExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitNavigationExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class XselectOneExpContext extends ExpressionContext {
		public QvtoIdentifierContext xselectOneIter;
		public ExpressionContext xselectOneCondition;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public XselectOneExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitXselectOneExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ImpliesExpContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ImpliesExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitImpliesExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryExpContext extends ExpressionContext {
		public PrimaryExpressionContext primaryExpression() {
			return getRuleContext(PrimaryExpressionContext.class,0);
		}
		public PrimaryExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPrimaryExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AssignExpContext extends ExpressionContext {
		public ExpressionContext defaultValue;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AssignOpContext assignOp() {
			return getRuleContext(AssignOpContext.class,0);
		}
		public AssignExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitAssignExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryMinusExpContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryMinusExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitUnaryMinusExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EqualityExpContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode NOT_EQUAL_EXEQ() { return getToken(QvtOParser.NOT_EQUAL_EXEQ, 0); }
		public EqualityExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitEqualityExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OrExpContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public OrExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitOrExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class XorExpContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public XorExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitXorExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrowExpContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public IteratorOrOperationCallContext iteratorOrOperationCall() {
			return getRuleContext(IteratorOrOperationCallContext.class,0);
		}
		public TerminalNode NOT_ARROW() { return getToken(QvtOParser.NOT_ARROW, 0); }
		public ArrowExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitArrowExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class XselectExpContext extends ExpressionContext {
		public QvtoIdentifierContext xselectIter;
		public ExpressionContext xselectCondition;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public XselectExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitXselectExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 134;
		enterRecursionRule(_localctx, 134, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1011);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				{
				_localctx = new HashExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(1000);
				match(HASH);
				setState(1001);
				expression(15);
				}
				break;
			case 2:
				{
				_localctx = new DoubleHashExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1002);
				match(DOUBLE_HASH);
				setState(1003);
				expression(14);
				}
				break;
			case 3:
				{
				_localctx = new UnaryStarExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1004);
				match(T__3);
				setState(1005);
				expression(13);
				}
				break;
			case 4:
				{
				_localctx = new NotExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1006);
				match(T__69);
				setState(1007);
				expression(12);
				}
				break;
			case 5:
				{
				_localctx = new UnaryMinusExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1008);
				match(T__70);
				setState(1009);
				expression(11);
				}
				break;
			case 6:
				{
				_localctx = new PrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(1010);
				primaryExpression();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1073);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,108,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1071);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,107,_ctx) ) {
					case 1:
						{
						_localctx = new MultExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1013);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(1014);
						((MultExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__3 || _la==T__71 || _la==T__72) ) {
							((MultExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1015);
						expression(11);
						}
						break;
					case 2:
						{
						_localctx = new AddExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1016);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(1017);
						((AddExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__70 || _la==T__73) ) {
							((AddExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1018);
						expression(10);
						}
						break;
					case 3:
						{
						_localctx = new CompareExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1019);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(1020);
						((CompareExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 75)) & ~0x3f) == 0 && ((1L << (_la - 75)) & 15L) != 0)) ) {
							((CompareExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1021);
						expression(9);
						}
						break;
					case 4:
						{
						_localctx = new EqualityExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1022);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1023);
						((EqualityExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__34 || _la==T__78 || _la==NOT_EQUAL_EXEQ) ) {
							((EqualityExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1024);
						expression(8);
						}
						break;
					case 5:
						{
						_localctx = new AndExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1025);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1026);
						match(T__79);
						setState(1027);
						expression(7);
						}
						break;
					case 6:
						{
						_localctx = new OrExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1028);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(1029);
						match(T__80);
						setState(1030);
						expression(6);
						}
						break;
					case 7:
						{
						_localctx = new XorExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1031);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(1032);
						match(T__81);
						setState(1033);
						expression(5);
						}
						break;
					case 8:
						{
						_localctx = new ImpliesExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1034);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(1035);
						match(T__82);
						setState(1036);
						expression(4);
						}
						break;
					case 9:
						{
						_localctx = new NavigationExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1037);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(1038);
						_la = _input.LA(1);
						if ( !(_la==T__63 || _la==T__64) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1039);
						propertyOrCallSuffix();
						}
						break;
					case 10:
						{
						_localctx = new ArrowExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1040);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(1041);
						_la = _input.LA(1);
						if ( !(_la==T__65 || _la==T__66 || _la==NOT_ARROW) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1042);
						iteratorOrOperationCall();
						}
						break;
					case 11:
						{
						_localctx = new XselectExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1043);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(1044);
						match(T__54);
						setState(1048);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
						case 1:
							{
							setState(1045);
							((XselectExpContext)_localctx).xselectIter = qvtoIdentifier();
							setState(1046);
							match(T__67);
							}
							break;
						}
						setState(1050);
						((XselectExpContext)_localctx).xselectCondition = expression(0);
						setState(1051);
						match(T__55);
						}
						break;
					case 12:
						{
						_localctx = new XselectOneExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1053);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(1054);
						match(T__68);
						setState(1055);
						match(T__54);
						setState(1059);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
						case 1:
							{
							setState(1056);
							((XselectOneExpContext)_localctx).xselectOneIter = qvtoIdentifier();
							setState(1057);
							match(T__67);
							}
							break;
						}
						setState(1061);
						((XselectOneExpContext)_localctx).xselectOneCondition = expression(0);
						setState(1062);
						match(T__55);
						}
						break;
					case 13:
						{
						_localctx = new AssignExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1064);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(1065);
						assignOp();
						setState(1066);
						expression(0);
						setState(1069);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
						case 1:
							{
							setState(1067);
							match(T__83);
							setState(1068);
							((AssignExpContext)_localctx).defaultValue = expression(0);
							}
							break;
						}
						}
						break;
					}
					} 
				}
				setState(1075);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,108,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryExpressionContext extends ParserRuleContext {
		public PrimaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpression; }
	 
		public PrimaryExpressionContext() { }
		public void copyFrom(PrimaryExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LiteralExpContext extends PrimaryExpressionContext {
		public LiteralExpressionContext literalExpression() {
			return getRuleContext(LiteralExpressionContext.class,0);
		}
		public LiteralExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitLiteralExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MapTypeExpContext extends PrimaryExpressionContext {
		public MapTypeContext mapType() {
			return getRuleContext(MapTypeContext.class,0);
		}
		public MapTypeExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMapTypeExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AssertPrimaryContext extends PrimaryExpressionContext {
		public AssertExpContext assertExp() {
			return getRuleContext(AssertExpContext.class,0);
		}
		public AssertPrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitAssertPrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SwitchPrimaryContext extends PrimaryExpressionContext {
		public SwitchExpContext switchExp() {
			return getRuleContext(SwitchExpContext.class,0);
		}
		public SwitchPrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitSwitchPrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ComputePrimaryContext extends PrimaryExpressionContext {
		public ComputeExpContext computeExp() {
			return getRuleContext(ComputeExpContext.class,0);
		}
		public ComputePrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitComputePrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ImperativeIfExpContext extends PrimaryExpressionContext {
		public ExpressionContext impCondition;
		public BlockContext thenBlock;
		public ExpressionContext expression;
		public List<ExpressionContext> elifCondition = new ArrayList<ExpressionContext>();
		public BlockContext block;
		public List<BlockContext> elifBlock = new ArrayList<BlockContext>();
		public BlockContext elseBlock;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public ImperativeIfExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitImperativeIfExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ResolvePrimaryContext extends PrimaryExpressionContext {
		public ResolveExpContext resolveExp() {
			return getRuleContext(ResolveExpContext.class,0);
		}
		public ResolvePrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitResolvePrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TryPrimaryContext extends PrimaryExpressionContext {
		public TryExpContext tryExp() {
			return getRuleContext(TryExpContext.class,0);
		}
		public TryPrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTryPrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IfExpContext extends PrimaryExpressionContext {
		public ExpressionContext condition;
		public ExpressionContext thenExp;
		public ExpressionContext expression;
		public List<ExpressionContext> elseIfCondition = new ArrayList<ExpressionContext>();
		public List<ExpressionContext> elseIfExp = new ArrayList<ExpressionContext>();
		public ExpressionContext elseExp;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public IfExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitIfExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VarDeclPrimaryContext extends PrimaryExpressionContext {
		public VarDeclExpContext varDeclExp() {
			return getRuleContext(VarDeclExpContext.class,0);
		}
		public VarDeclPrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitVarDeclPrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveTypeExpContext extends PrimaryExpressionContext {
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public PrimitiveTypeExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPrimitiveTypeExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LetExpContext extends PrimaryExpressionContext {
		public List<LetBindingContext> letBinding() {
			return getRuleContexts(LetBindingContext.class);
		}
		public LetBindingContext letBinding(int i) {
			return getRuleContext(LetBindingContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LetExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitLetExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OperationCallExpContext extends PrimaryExpressionContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public OperationCallExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitOperationCallExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ReturnPrimaryContext extends PrimaryExpressionContext {
		public ReturnExpContext returnExp() {
			return getRuleContext(ReturnExpContext.class,0);
		}
		public ReturnPrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitReturnPrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CollectionTypeExpContext extends PrimaryExpressionContext {
		public CollectionTypeContext collectionType() {
			return getRuleContext(CollectionTypeContext.class,0);
		}
		public CollectionTypeExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitCollectionTypeExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WhilePrimaryContext extends PrimaryExpressionContext {
		public WhileExpContext whileExp() {
			return getRuleContext(WhileExpContext.class,0);
		}
		public WhilePrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitWhilePrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NewPrimaryContext extends PrimaryExpressionContext {
		public NewExpContext newExp() {
			return getRuleContext(NewExpContext.class,0);
		}
		public NewPrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitNewPrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ForPrimaryContext extends PrimaryExpressionContext {
		public ForExpContext forExp() {
			return getRuleContext(ForExpContext.class,0);
		}
		public ForPrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitForPrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ContinuePrimaryContext extends PrimaryExpressionContext {
		public ContinuePrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitContinuePrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RaisePrimaryContext extends PrimaryExpressionContext {
		public RaiseExpContext raiseExp() {
			return getRuleContext(RaiseExpContext.class,0);
		}
		public RaisePrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitRaisePrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PathNameExpContext extends PrimaryExpressionContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public PathNameExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPathNameExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SelfExpContext extends PrimaryExpressionContext {
		public SelfExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitSelfExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ObjectPrimaryContext extends PrimaryExpressionContext {
		public ObjectExpContext objectExp() {
			return getRuleContext(ObjectExpContext.class,0);
		}
		public ObjectPrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitObjectPrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenExpContext extends PrimaryExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ParenExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitParenExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LogPrimaryContext extends PrimaryExpressionContext {
		public LogExpContext logExp() {
			return getRuleContext(LogExpContext.class,0);
		}
		public LogPrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitLogPrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ResolveInPrimaryContext extends PrimaryExpressionContext {
		public ResolveInExpContext resolveInExp() {
			return getRuleContext(ResolveInExpContext.class,0);
		}
		public ResolveInPrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitResolveInPrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MappingCallPrimaryContext extends PrimaryExpressionContext {
		public MappingCallExpContext mappingCallExp() {
			return getRuleContext(MappingCallExpContext.class,0);
		}
		public MappingCallPrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMappingCallPrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BreakPrimaryContext extends PrimaryExpressionContext {
		public BreakPrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitBreakPrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TupleTypeExpContext extends PrimaryExpressionContext {
		public TupleTypeContext tupleType() {
			return getRuleContext(TupleTypeContext.class,0);
		}
		public TupleTypeExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTupleTypeExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ThisExpContext extends PrimaryExpressionContext {
		public ThisExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitThisExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BlockPrimaryContext extends PrimaryExpressionContext {
		public BlockExpContext blockExp() {
			return getRuleContext(BlockExpContext.class,0);
		}
		public BlockPrimaryContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitBlockPrimary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
		PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_primaryExpression);
		int _la;
		try {
			int _alt;
			setState(1168);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
			case 1:
				_localctx = new ParenExpContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1076);
				match(T__7);
				setState(1077);
				expression(0);
				setState(1078);
				match(T__8);
				}
				break;
			case 2:
				_localctx = new SelfExpContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1080);
				match(T__84);
				}
				break;
			case 3:
				_localctx = new ThisExpContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1081);
				match(T__85);
				}
				break;
			case 4:
				_localctx = new IfExpContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(1082);
				match(T__86);
				setState(1083);
				((IfExpContext)_localctx).condition = expression(0);
				setState(1084);
				match(T__87);
				setState(1085);
				((IfExpContext)_localctx).thenExp = expression(0);
				setState(1093);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__88 || _la==T__89) {
					{
					{
					setState(1086);
					_la = _input.LA(1);
					if ( !(_la==T__88 || _la==T__89) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1087);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfCondition.add(((IfExpContext)_localctx).expression);
					setState(1088);
					match(T__87);
					setState(1089);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfExp.add(((IfExpContext)_localctx).expression);
					}
					}
					setState(1095);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1098);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__90) {
					{
					setState(1096);
					match(T__90);
					setState(1097);
					((IfExpContext)_localctx).elseExp = expression(0);
					}
				}

				setState(1100);
				match(T__91);
				}
				break;
			case 5:
				_localctx = new ImperativeIfExpContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(1102);
				match(T__86);
				setState(1103);
				match(T__7);
				setState(1104);
				((ImperativeIfExpContext)_localctx).impCondition = expression(0);
				setState(1105);
				match(T__8);
				setState(1106);
				((ImperativeIfExpContext)_localctx).thenBlock = block();
				setState(1115);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,111,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1107);
						match(T__89);
						setState(1108);
						match(T__7);
						setState(1109);
						((ImperativeIfExpContext)_localctx).expression = expression(0);
						((ImperativeIfExpContext)_localctx).elifCondition.add(((ImperativeIfExpContext)_localctx).expression);
						setState(1110);
						match(T__8);
						setState(1111);
						((ImperativeIfExpContext)_localctx).block = block();
						((ImperativeIfExpContext)_localctx).elifBlock.add(((ImperativeIfExpContext)_localctx).block);
						}
						} 
					}
					setState(1117);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,111,_ctx);
				}
				setState(1120);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
				case 1:
					{
					setState(1118);
					match(T__90);
					setState(1119);
					((ImperativeIfExpContext)_localctx).elseBlock = block();
					}
					break;
				}
				setState(1123);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,113,_ctx) ) {
				case 1:
					{
					setState(1122);
					match(T__91);
					}
					break;
				}
				}
				break;
			case 6:
				_localctx = new LetExpContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(1125);
				match(T__92);
				setState(1126);
				letBinding();
				setState(1131);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(1127);
					match(T__4);
					setState(1128);
					letBinding();
					}
					}
					setState(1133);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1134);
				match(T__16);
				setState(1135);
				expression(0);
				}
				break;
			case 7:
				_localctx = new LiteralExpContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(1137);
				literalExpression();
				}
				break;
			case 8:
				_localctx = new BlockPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(1138);
				blockExp();
				}
				break;
			case 9:
				_localctx = new WhilePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(1139);
				whileExp();
				}
				break;
			case 10:
				_localctx = new ForPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(1140);
				forExp();
				}
				break;
			case 11:
				_localctx = new SwitchPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(1141);
				switchExp();
				}
				break;
			case 12:
				_localctx = new ComputePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(1142);
				computeExp();
				}
				break;
			case 13:
				_localctx = new ObjectPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(1143);
				objectExp();
				}
				break;
			case 14:
				_localctx = new NewPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(1144);
				newExp();
				}
				break;
			case 15:
				_localctx = new MappingCallPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(1145);
				mappingCallExp();
				}
				break;
			case 16:
				_localctx = new ResolvePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(1146);
				resolveExp();
				}
				break;
			case 17:
				_localctx = new ResolveInPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(1147);
				resolveInExp();
				}
				break;
			case 18:
				_localctx = new TryPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(1148);
				tryExp();
				}
				break;
			case 19:
				_localctx = new RaisePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(1149);
				raiseExp();
				}
				break;
			case 20:
				_localctx = new AssertPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(1150);
				assertExp();
				}
				break;
			case 21:
				_localctx = new LogPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 21);
				{
				setState(1151);
				logExp();
				}
				break;
			case 22:
				_localctx = new ReturnPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 22);
				{
				setState(1152);
				returnExp();
				}
				break;
			case 23:
				_localctx = new BreakPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 23);
				{
				setState(1153);
				match(T__93);
				}
				break;
			case 24:
				_localctx = new ContinuePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 24);
				{
				setState(1154);
				match(T__94);
				}
				break;
			case 25:
				_localctx = new VarDeclPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 25);
				{
				setState(1155);
				varDeclExp();
				}
				break;
			case 26:
				_localctx = new OperationCallExpContext(_localctx);
				enterOuterAlt(_localctx, 26);
				{
				setState(1156);
				pathName();
				setState(1157);
				match(T__7);
				setState(1159);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
					{
					setState(1158);
					argumentList();
					}
				}

				setState(1161);
				match(T__8);
				}
				break;
			case 27:
				_localctx = new PrimitiveTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 27);
				{
				setState(1163);
				primitiveType();
				}
				break;
			case 28:
				_localctx = new CollectionTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 28);
				{
				setState(1164);
				collectionType();
				}
				break;
			case 29:
				_localctx = new MapTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 29);
				{
				setState(1165);
				mapType();
				}
				break;
			case 30:
				_localctx = new TupleTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 30);
				{
				setState(1166);
				tupleType();
				}
				break;
			case 31:
				_localctx = new PathNameExpContext(_localctx);
				enterOuterAlt(_localctx, 31);
				{
				setState(1167);
				pathName();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeExpressionContext extends ParserRuleContext {
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public CollectionTypeContext collectionType() {
			return getRuleContext(CollectionTypeContext.class,0);
		}
		public MapTypeContext mapType() {
			return getRuleContext(MapTypeContext.class,0);
		}
		public TupleTypeContext tupleType() {
			return getRuleContext(TupleTypeContext.class,0);
		}
		public ListTypeContext listType() {
			return getRuleContext(ListTypeContext.class,0);
		}
		public DictTypeContext dictType() {
			return getRuleContext(DictTypeContext.class,0);
		}
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TypeExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeExpression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTypeExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeExpressionContext typeExpression() throws RecognitionException {
		TypeExpressionContext _localctx = new TypeExpressionContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_typeExpression);
		try {
			setState(1177);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1170);
				primitiveType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1171);
				collectionType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1172);
				mapType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1173);
				tupleType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1174);
				listType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1175);
				dictType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1176);
				pathName();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ListTypeContext extends ParserRuleContext {
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public ListTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitListType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListTypeContext listType() throws RecognitionException {
		ListTypeContext _localctx = new ListTypeContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_listType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1179);
			match(T__95);
			setState(1180);
			match(T__7);
			setState(1181);
			typeExpression();
			setState(1182);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DictTypeContext extends ParserRuleContext {
		public List<TypeExpressionContext> typeExpression() {
			return getRuleContexts(TypeExpressionContext.class);
		}
		public TypeExpressionContext typeExpression(int i) {
			return getRuleContext(TypeExpressionContext.class,i);
		}
		public DictTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dictType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitDictType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DictTypeContext dictType() throws RecognitionException {
		DictTypeContext _localctx = new DictTypeContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_dictType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1184);
			match(T__96);
			setState(1185);
			match(T__7);
			setState(1186);
			typeExpression();
			setState(1187);
			match(T__4);
			setState(1188);
			typeExpression();
			setState(1189);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CollectionKindContext extends ParserRuleContext {
		public CollectionKindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionKind; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitCollectionKind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionKindContext collectionKind() throws RecognitionException {
		CollectionKindContext _localctx = new CollectionKindContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_collectionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1191);
			_la = _input.LA(1);
			if ( !(((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & 125L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralExpressionContext extends ParserRuleContext {
		public LiteralExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalExpression; }
	 
		public LiteralExpressionContext() { }
		public void copyFrom(LiteralExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RealLiteralContext extends LiteralExpressionContext {
		public TerminalNode REAL_LITERAL() { return getToken(QvtOParser.REAL_LITERAL, 0); }
		public RealLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitRealLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CollectionLitContext extends LiteralExpressionContext {
		public CollectionLiteralContext collectionLiteral() {
			return getRuleContext(CollectionLiteralContext.class,0);
		}
		public CollectionLitContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitCollectionLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringLiteralContext extends LiteralExpressionContext {
		public StringLiteral_Context stringLiteral_() {
			return getRuleContext(StringLiteral_Context.class,0);
		}
		public StringLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TrueLiteralContext extends LiteralExpressionContext {
		public TrueLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTrueLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TupleLitContext extends LiteralExpressionContext {
		public TupleLiteralContext tupleLiteral() {
			return getRuleContext(TupleLiteralContext.class,0);
		}
		public TupleLitContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTupleLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InvalidLiteralContext extends LiteralExpressionContext {
		public InvalidLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitInvalidLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MapLitContext extends LiteralExpressionContext {
		public MapLiteralContext mapLiteral() {
			return getRuleContext(MapLiteralContext.class,0);
		}
		public MapLitContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMapLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DictLitContext extends LiteralExpressionContext {
		public DictLiteralContext dictLiteral() {
			return getRuleContext(DictLiteralContext.class,0);
		}
		public DictLitContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitDictLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NullLiteralContext extends LiteralExpressionContext {
		public NullLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitNullLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnlimitedNaturalLiteralContext extends LiteralExpressionContext {
		public UnlimitedNaturalLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitUnlimitedNaturalLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntegerLiteralContext extends LiteralExpressionContext {
		public TerminalNode INTEGER_LITERAL() { return getToken(QvtOParser.INTEGER_LITERAL, 0); }
		public IntegerLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitIntegerLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FalseLiteralContext extends LiteralExpressionContext {
		public FalseLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitFalseLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralExpressionContext literalExpression() throws RecognitionException {
		LiteralExpressionContext _localctx = new LiteralExpressionContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_literalExpression);
		int _la;
		try {
			setState(1205);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER_LITERAL:
				_localctx = new IntegerLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1193);
				match(INTEGER_LITERAL);
				}
				break;
			case REAL_LITERAL:
				_localctx = new RealLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1194);
				match(REAL_LITERAL);
				}
				break;
			case DOUBLE_QUOTED_STRING:
			case STRING_LITERAL:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1195);
				stringLiteral_();
				}
				break;
			case T__102:
				_localctx = new TrueLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(1196);
				match(T__102);
				}
				break;
			case T__103:
				_localctx = new FalseLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(1197);
				match(T__103);
				}
				break;
			case T__104:
				_localctx = new NullLiteralContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(1198);
				match(T__104);
				}
				break;
			case T__105:
				_localctx = new InvalidLiteralContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(1199);
				match(T__105);
				}
				break;
			case T__3:
			case T__106:
				_localctx = new UnlimitedNaturalLiteralContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(1200);
				_la = _input.LA(1);
				if ( !(_la==T__3 || _la==T__106) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case T__95:
			case T__97:
			case T__98:
			case T__99:
			case T__100:
			case T__101:
				_localctx = new CollectionLitContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(1201);
				collectionLiteral();
				}
				break;
			case T__145:
				_localctx = new TupleLitContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(1202);
				tupleLiteral();
				}
				break;
			case T__146:
				_localctx = new MapLitContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(1203);
				mapLiteral();
				}
				break;
			case T__96:
				_localctx = new DictLitContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(1204);
				dictLiteral();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringLiteral_Context extends ParserRuleContext {
		public List<TerminalNode> STRING_LITERAL() { return getTokens(QvtOParser.STRING_LITERAL); }
		public TerminalNode STRING_LITERAL(int i) {
			return getToken(QvtOParser.STRING_LITERAL, i);
		}
		public List<TerminalNode> DOUBLE_QUOTED_STRING() { return getTokens(QvtOParser.DOUBLE_QUOTED_STRING); }
		public TerminalNode DOUBLE_QUOTED_STRING(int i) {
			return getToken(QvtOParser.DOUBLE_QUOTED_STRING, i);
		}
		public StringLiteral_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral_; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitStringLiteral_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteral_Context stringLiteral_() throws RecognitionException {
		StringLiteral_Context _localctx = new StringLiteral_Context(_ctx, getState());
		enterRule(_localctx, 148, RULE_stringLiteral_);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1208); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1207);
					_la = _input.LA(1);
					if ( !(_la==DOUBLE_QUOTED_STRING || _la==STRING_LITERAL) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1210); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,119,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DictLiteralContext extends ParserRuleContext {
		public List<DictLiteralPartContext> dictLiteralPart() {
			return getRuleContexts(DictLiteralPartContext.class);
		}
		public DictLiteralPartContext dictLiteralPart(int i) {
			return getRuleContext(DictLiteralPartContext.class,i);
		}
		public DictLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dictLiteral; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitDictLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DictLiteralContext dictLiteral() throws RecognitionException {
		DictLiteralContext _localctx = new DictLiteralContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_dictLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1212);
			match(T__96);
			setState(1213);
			match(T__10);
			setState(1222);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3 || ((((_la - 103)) & ~0x3f) == 0 && ((1L << (_la - 103)) & 23L) != 0) || ((((_la - 169)) & ~0x3f) == 0 && ((1L << (_la - 169)) & 15L) != 0)) {
				{
				setState(1214);
				dictLiteralPart();
				setState(1219);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(1215);
					match(T__4);
					setState(1216);
					dictLiteralPart();
					}
					}
					setState(1221);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1224);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DictLiteralPartContext extends ParserRuleContext {
		public LiteralSimpleContext key;
		public ExpressionContext value;
		public LiteralSimpleContext literalSimple() {
			return getRuleContext(LiteralSimpleContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DictLiteralPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dictLiteralPart; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitDictLiteralPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DictLiteralPartContext dictLiteralPart() throws RecognitionException {
		DictLiteralPartContext _localctx = new DictLiteralPartContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_dictLiteralPart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1226);
			((DictLiteralPartContext)_localctx).key = literalSimple();
			setState(1227);
			match(T__34);
			setState(1228);
			((DictLiteralPartContext)_localctx).value = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralSimpleContext extends ParserRuleContext {
		public LiteralSimpleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalSimple; }
	 
		public LiteralSimpleContext() { }
		public void copyFrom(LiteralSimpleContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SimpleLitNullContext extends LiteralSimpleContext {
		public SimpleLitNullContext(LiteralSimpleContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitSimpleLitNull(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SimpleLitTrueContext extends LiteralSimpleContext {
		public SimpleLitTrueContext(LiteralSimpleContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitSimpleLitTrue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SimpleLitIntContext extends LiteralSimpleContext {
		public TerminalNode INTEGER_LITERAL() { return getToken(QvtOParser.INTEGER_LITERAL, 0); }
		public SimpleLitIntContext(LiteralSimpleContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitSimpleLitInt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SimpleLitFalseContext extends LiteralSimpleContext {
		public SimpleLitFalseContext(LiteralSimpleContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitSimpleLitFalse(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SimpleLitUnlimitedContext extends LiteralSimpleContext {
		public SimpleLitUnlimitedContext(LiteralSimpleContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitSimpleLitUnlimited(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SimpleLitRealContext extends LiteralSimpleContext {
		public TerminalNode REAL_LITERAL() { return getToken(QvtOParser.REAL_LITERAL, 0); }
		public SimpleLitRealContext(LiteralSimpleContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitSimpleLitReal(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SimpleLitStringContext extends LiteralSimpleContext {
		public StringLiteral_Context stringLiteral_() {
			return getRuleContext(StringLiteral_Context.class,0);
		}
		public SimpleLitStringContext(LiteralSimpleContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitSimpleLitString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralSimpleContext literalSimple() throws RecognitionException {
		LiteralSimpleContext _localctx = new LiteralSimpleContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_literalSimple);
		int _la;
		try {
			setState(1237);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER_LITERAL:
				_localctx = new SimpleLitIntContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1230);
				match(INTEGER_LITERAL);
				}
				break;
			case REAL_LITERAL:
				_localctx = new SimpleLitRealContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1231);
				match(REAL_LITERAL);
				}
				break;
			case DOUBLE_QUOTED_STRING:
			case STRING_LITERAL:
				_localctx = new SimpleLitStringContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1232);
				stringLiteral_();
				}
				break;
			case T__102:
				_localctx = new SimpleLitTrueContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(1233);
				match(T__102);
				}
				break;
			case T__103:
				_localctx = new SimpleLitFalseContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(1234);
				match(T__103);
				}
				break;
			case T__104:
				_localctx = new SimpleLitNullContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(1235);
				match(T__104);
				}
				break;
			case T__3:
			case T__106:
				_localctx = new SimpleLitUnlimitedContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(1236);
				_la = _input.LA(1);
				if ( !(_la==T__3 || _la==T__106) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockExpContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitBlockExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockExpContext blockExp() throws RecognitionException {
		BlockExpContext _localctx = new BlockExpContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_blockExp);
		int _la;
		try {
			setState(1256);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__107:
				enterOuterAlt(_localctx, 1);
				{
				setState(1239);
				match(T__107);
				setState(1240);
				match(T__10);
				setState(1244);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
					{
					{
					setState(1241);
					statement();
					}
					}
					setState(1246);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1247);
				match(T__11);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 2);
				{
				setState(1248);
				match(T__10);
				setState(1252);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
					{
					{
					setState(1249);
					statement();
					}
					}
					setState(1254);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1255);
				match(T__11);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileExpContext extends ParserRuleContext {
		public WhileExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileExp; }
	 
		public WhileExpContext() { }
		public void copyFrom(WhileExpContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WhileBasicContext extends WhileExpContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public WhileBasicContext(WhileExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitWhileBasic(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WhileWithInitContext extends WhileExpContext {
		public QvtoIdentifierContext varName;
		public TypeExpressionContext type;
		public ExpressionContext initValue;
		public ExpressionContext condition;
		public TerminalNode RESET_ASSIGN() { return getToken(QvtOParser.RESET_ASSIGN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public WhileWithInitContext(WhileExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitWhileWithInit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileExpContext whileExp() throws RecognitionException {
		WhileExpContext _localctx = new WhileExpContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_whileExp);
		try {
			setState(1276);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,126,_ctx) ) {
			case 1:
				_localctx = new WhileWithInitContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1258);
				match(T__108);
				setState(1259);
				match(T__7);
				setState(1260);
				((WhileWithInitContext)_localctx).varName = qvtoIdentifier();
				setState(1261);
				match(T__15);
				setState(1262);
				((WhileWithInitContext)_localctx).type = typeExpression();
				setState(1263);
				match(RESET_ASSIGN);
				setState(1264);
				((WhileWithInitContext)_localctx).initValue = expression(0);
				setState(1265);
				match(T__1);
				setState(1266);
				((WhileWithInitContext)_localctx).condition = expression(0);
				setState(1267);
				match(T__8);
				setState(1268);
				block();
				}
				break;
			case 2:
				_localctx = new WhileBasicContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1270);
				match(T__108);
				setState(1271);
				match(T__7);
				setState(1272);
				expression(0);
				setState(1273);
				match(T__8);
				setState(1274);
				block();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForExpContext extends ParserRuleContext {
		public ForKindContext forKind() {
			return getRuleContext(ForKindContext.class,0);
		}
		public ForVarListContext forVarList() {
			return getRuleContext(ForVarListContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ForExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitForExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForExpContext forExp() throws RecognitionException {
		ForExpContext _localctx = new ForExpContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_forExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1278);
			forKind();
			setState(1279);
			match(T__7);
			setState(1280);
			forVarList();
			setState(1283);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__67) {
				{
				setState(1281);
				match(T__67);
				setState(1282);
				expression(0);
				}
			}

			setState(1285);
			match(T__8);
			setState(1286);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForKindContext extends ParserRuleContext {
		public ForKindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forKind; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitForKind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForKindContext forKind() throws RecognitionException {
		ForKindContext _localctx = new ForKindContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_forKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1288);
			_la = _input.LA(1);
			if ( !(_la==T__109 || _la==T__110) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForVarListContext extends ParserRuleContext {
		public IteratorVariablesContext iteratorVariables() {
			return getRuleContext(IteratorVariablesContext.class,0);
		}
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public VarInitOpContext varInitOp() {
			return getRuleContext(VarInitOpContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ForVarListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forVarList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitForVarList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForVarListContext forVarList() throws RecognitionException {
		ForVarListContext _localctx = new ForVarListContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_forVarList);
		int _la;
		try {
			setState(1305);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,129,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1290);
				iteratorVariables();
				setState(1291);
				match(T__1);
				setState(1292);
				qvtoIdentifier();
				setState(1293);
				match(T__15);
				setState(1294);
				typeExpression();
				setState(1298);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__34 || _la==RESET_ASSIGN || _la==ORDERED_COPY) {
					{
					setState(1295);
					varInitOp();
					setState(1296);
					expression(0);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1300);
				iteratorVariables();
				setState(1301);
				match(T__1);
				setState(1302);
				typeExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1304);
				iteratorVariables();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchExpContext extends ParserRuleContext {
		public SwitchIteratorContext switchIterator() {
			return getRuleContext(SwitchIteratorContext.class,0);
		}
		public List<SwitchAltContext> switchAlt() {
			return getRuleContexts(SwitchAltContext.class);
		}
		public SwitchAltContext switchAlt(int i) {
			return getRuleContext(SwitchAltContext.class,i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SwitchExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitSwitchExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchExpContext switchExp() throws RecognitionException {
		SwitchExpContext _localctx = new SwitchExpContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_switchExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1307);
			match(T__111);
			setState(1312);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(1308);
				match(T__7);
				setState(1309);
				switchIterator();
				setState(1310);
				match(T__8);
				}
			}

			setState(1314);
			match(T__10);
			setState(1318);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__112) {
				{
				{
				setState(1315);
				switchAlt();
				}
				}
				setState(1320);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1325);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__90) {
				{
				setState(1321);
				match(T__90);
				setState(1322);
				expression(0);
				setState(1323);
				match(T__1);
				}
			}

			setState(1327);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchIteratorContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public VarInitOpContext varInitOp() {
			return getRuleContext(VarInitOpContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SwitchIteratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchIterator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitSwitchIterator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchIteratorContext switchIterator() throws RecognitionException {
		SwitchIteratorContext _localctx = new SwitchIteratorContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_switchIterator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1329);
			qvtoIdentifier();
			setState(1332);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__15) {
				{
				setState(1330);
				match(T__15);
				setState(1331);
				typeExpression();
				}
			}

			setState(1337);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__34 || _la==RESET_ASSIGN || _la==ORDERED_COPY) {
				{
				setState(1334);
				varInitOp();
				setState(1335);
				expression(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchAltContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public SwitchAltContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchAlt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitSwitchAlt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchAltContext switchAlt() throws RecognitionException {
		SwitchAltContext _localctx = new SwitchAltContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_switchAlt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1339);
			match(T__112);
			setState(1340);
			match(T__7);
			setState(1341);
			expression(0);
			setState(1342);
			match(T__8);
			setState(1343);
			expression(0);
			setState(1344);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComputeExpContext extends ParserRuleContext {
		public VarDeclaratorContext varDeclarator() {
			return getRuleContext(VarDeclaratorContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ComputeExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_computeExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitComputeExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComputeExpContext computeExp() throws RecognitionException {
		ComputeExpContext _localctx = new ComputeExpContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_computeExp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1346);
			match(T__113);
			setState(1347);
			match(T__7);
			setState(1348);
			varDeclarator();
			setState(1349);
			match(T__8);
			setState(1350);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ObjectExpContext extends ParserRuleContext {
		public QvtoIdentifierContext varName;
		public QvtoIdentifierContext extentName;
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ObjectIteratorContext objectIterator() {
			return getRuleContext(ObjectIteratorContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public List<QvtoIdentifierContext> qvtoIdentifier() {
			return getRuleContexts(QvtoIdentifierContext.class);
		}
		public QvtoIdentifierContext qvtoIdentifier(int i) {
			return getRuleContext(QvtoIdentifierContext.class,i);
		}
		public ObjectExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitObjectExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectExpContext objectExp() throws RecognitionException {
		ObjectExpContext _localctx = new ObjectExpContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_objectExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1352);
			match(T__114);
			setState(1354);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(1353);
				objectIterator();
				}
			}

			setState(1359);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,136,_ctx) ) {
			case 1:
				{
				setState(1356);
				((ObjectExpContext)_localctx).varName = qvtoIdentifier();
				setState(1357);
				match(T__15);
				}
				break;
			}
			setState(1362);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104680760L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & -22500405950815745L) != 0) || _la==IDENTIFIER) {
				{
				setState(1361);
				typeExpression();
				}
			}

			setState(1366);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__19) {
				{
				setState(1364);
				match(T__19);
				setState(1365);
				((ObjectExpContext)_localctx).extentName = qvtoIdentifier();
				}
			}

			setState(1368);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ObjectIteratorContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public VarInitOpContext varInitOp() {
			return getRuleContext(VarInitOpContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ObjectIteratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectIterator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitObjectIterator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectIteratorContext objectIterator() throws RecognitionException {
		ObjectIteratorContext _localctx = new ObjectIteratorContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_objectIterator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1370);
			match(T__7);
			setState(1371);
			qvtoIdentifier();
			setState(1372);
			match(T__15);
			setState(1373);
			typeExpression();
			setState(1377);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__34 || _la==RESET_ASSIGN || _la==ORDERED_COPY) {
				{
				setState(1374);
				varInitOp();
				setState(1375);
				expression(0);
				}
			}

			setState(1379);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NewExpContext extends ParserRuleContext {
		public QvtoIdentifierContext extentName;
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public NewExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitNewExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NewExpContext newExp() throws RecognitionException {
		NewExpContext _localctx = new NewExpContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_newExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1381);
			match(T__115);
			setState(1382);
			pathName();
			setState(1385);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__19) {
				{
				setState(1383);
				match(T__19);
				setState(1384);
				((NewExpContext)_localctx).extentName = qvtoIdentifier();
				}
			}

			setState(1387);
			match(T__7);
			setState(1389);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
				{
				setState(1388);
				argumentList();
				}
			}

			setState(1391);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MappingCallExpContext extends ParserRuleContext {
		public MappingCallKindContext mappingCallKind() {
			return getRuleContext(MappingCallKindContext.class,0);
		}
		public ScopedNameContext scopedName() {
			return getRuleContext(ScopedNameContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public MappingCallExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mappingCallExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMappingCallExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MappingCallExpContext mappingCallExp() throws RecognitionException {
		MappingCallExpContext _localctx = new MappingCallExpContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_mappingCallExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1393);
			mappingCallKind();
			setState(1394);
			scopedName();
			setState(1395);
			match(T__7);
			setState(1397);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
				{
				setState(1396);
				argumentList();
				}
			}

			setState(1399);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MappingCallKindContext extends ParserRuleContext {
		public MappingCallKindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mappingCallKind; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMappingCallKind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MappingCallKindContext mappingCallKind() throws RecognitionException {
		MappingCallKindContext _localctx = new MappingCallKindContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_mappingCallKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1401);
			_la = _input.LA(1);
			if ( !(_la==T__116 || _la==T__117) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ResolveExpContext extends ParserRuleContext {
		public ResolveKindContext resolveKind() {
			return getRuleContext(ResolveKindContext.class,0);
		}
		public ResolveArgsContext resolveArgs() {
			return getRuleContext(ResolveArgsContext.class,0);
		}
		public ResolveExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resolveExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitResolveExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResolveExpContext resolveExp() throws RecognitionException {
		ResolveExpContext _localctx = new ResolveExpContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_resolveExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1404);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__118) {
				{
				setState(1403);
				match(T__118);
				}
			}

			setState(1406);
			resolveKind();
			setState(1407);
			match(T__7);
			setState(1409);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104680760L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & -22500405950815745L) != 0) || _la==IDENTIFIER) {
				{
				setState(1408);
				resolveArgs();
				}
			}

			setState(1411);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ResolveKindContext extends ParserRuleContext {
		public ResolveKindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resolveKind; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitResolveKind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResolveKindContext resolveKind() throws RecognitionException {
		ResolveKindContext _localctx = new ResolveKindContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_resolveKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1413);
			_la = _input.LA(1);
			if ( !(((((_la - 120)) & ~0x3f) == 0 && ((1L << (_la - 120)) & 15L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ResolveInExpContext extends ParserRuleContext {
		public ResolveInKindContext resolveInKind() {
			return getRuleContext(ResolveInKindContext.class,0);
		}
		public ScopedNameContext scopedName() {
			return getRuleContext(ScopedNameContext.class,0);
		}
		public ResolveArgsContext resolveArgs() {
			return getRuleContext(ResolveArgsContext.class,0);
		}
		public ResolveInExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resolveInExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitResolveInExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResolveInExpContext resolveInExp() throws RecognitionException {
		ResolveInExpContext _localctx = new ResolveInExpContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_resolveInExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1416);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__118) {
				{
				setState(1415);
				match(T__118);
				}
			}

			setState(1418);
			resolveInKind();
			setState(1419);
			match(T__7);
			setState(1420);
			scopedName();
			setState(1423);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(1421);
				match(T__4);
				setState(1422);
				resolveArgs();
				}
			}

			setState(1425);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ResolveInKindContext extends ParserRuleContext {
		public ResolveInKindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resolveInKind; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitResolveInKind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResolveInKindContext resolveInKind() throws RecognitionException {
		ResolveInKindContext _localctx = new ResolveInKindContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_resolveInKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1427);
			_la = _input.LA(1);
			if ( !(((((_la - 124)) & ~0x3f) == 0 && ((1L << (_la - 124)) & 15L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ResolveArgsContext extends ParserRuleContext {
		public ResolveTargetContext resolveTarget() {
			return getRuleContext(ResolveTargetContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ResolveArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resolveArgs; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitResolveArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResolveArgsContext resolveArgs() throws RecognitionException {
		ResolveArgsContext _localctx = new ResolveArgsContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_resolveArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1429);
			resolveTarget();
			setState(1432);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__67) {
				{
				setState(1430);
				match(T__67);
				setState(1431);
				expression(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ResolveTargetContext extends ParserRuleContext {
		public ResolveTargetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resolveTarget; }
	 
		public ResolveTargetContext() { }
		public void copyFrom(ResolveTargetContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NamedResolveTargetContext extends ResolveTargetContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public NamedResolveTargetContext(ResolveTargetContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitNamedResolveTarget(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TypeOnlyResolveTargetContext extends ResolveTargetContext {
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public TypeOnlyResolveTargetContext(ResolveTargetContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTypeOnlyResolveTarget(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResolveTargetContext resolveTarget() throws RecognitionException {
		ResolveTargetContext _localctx = new ResolveTargetContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_resolveTarget);
		try {
			setState(1439);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,148,_ctx) ) {
			case 1:
				_localctx = new NamedResolveTargetContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1434);
				qvtoIdentifier();
				setState(1435);
				match(T__15);
				setState(1436);
				typeExpression();
				}
				break;
			case 2:
				_localctx = new TypeOnlyResolveTargetContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1438);
				typeExpression();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TryExpContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<CatchClauseContext> catchClause() {
			return getRuleContexts(CatchClauseContext.class);
		}
		public CatchClauseContext catchClause(int i) {
			return getRuleContext(CatchClauseContext.class,i);
		}
		public TryExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tryExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTryExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryExpContext tryExp() throws RecognitionException {
		TryExpContext _localctx = new TryExpContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_tryExp);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1441);
			match(T__127);
			setState(1442);
			block();
			setState(1444); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1443);
					catchClause();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1446); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,149,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CatchClauseContext extends ParserRuleContext {
		public QvtoIdentifierContext exceptionVar;
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ExceptionTypeListContext exceptionTypeList() {
			return getRuleContext(ExceptionTypeListContext.class,0);
		}
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public CatchClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitCatchClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatchClauseContext catchClause() throws RecognitionException {
		CatchClauseContext _localctx = new CatchClauseContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_catchClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1448);
			_la = _input.LA(1);
			if ( !(_la==T__128 || _la==T__129) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1459);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(1449);
				match(T__7);
				setState(1456);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104680760L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 17592186036227L) != 0) || _la==IDENTIFIER) {
					{
					setState(1453);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,150,_ctx) ) {
					case 1:
						{
						setState(1450);
						((CatchClauseContext)_localctx).exceptionVar = qvtoIdentifier();
						setState(1451);
						match(T__15);
						}
						break;
					}
					setState(1455);
					exceptionTypeList();
					}
				}

				setState(1458);
				match(T__8);
				}
			}

			setState(1461);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExceptionTypeListContext extends ParserRuleContext {
		public List<PathNameContext> pathName() {
			return getRuleContexts(PathNameContext.class);
		}
		public PathNameContext pathName(int i) {
			return getRuleContext(PathNameContext.class,i);
		}
		public ExceptionTypeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exceptionTypeList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitExceptionTypeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExceptionTypeListContext exceptionTypeList() throws RecognitionException {
		ExceptionTypeListContext _localctx = new ExceptionTypeListContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_exceptionTypeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1463);
			pathName();
			setState(1468);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1464);
				match(T__4);
				setState(1465);
				pathName();
				}
				}
				setState(1470);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RaiseExpContext extends ParserRuleContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode STRING_LITERAL() { return getToken(QvtOParser.STRING_LITERAL, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public RaiseExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_raiseExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitRaiseExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RaiseExpContext raiseExp() throws RecognitionException {
		RaiseExpContext _localctx = new RaiseExpContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_raiseExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1471);
			match(T__130);
			setState(1481);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case T__5:
			case T__6:
			case T__9:
			case T__12:
			case T__14:
			case T__20:
			case T__21:
			case T__22:
			case T__23:
			case T__24:
			case T__25:
			case T__26:
			case T__27:
			case T__28:
			case T__29:
			case T__30:
			case T__31:
			case T__32:
			case T__33:
			case T__35:
			case T__36:
			case T__37:
			case T__38:
			case T__39:
			case T__40:
			case T__41:
			case T__43:
			case T__44:
			case T__45:
			case T__46:
			case T__47:
			case T__48:
			case T__49:
			case T__50:
			case T__51:
			case T__52:
			case T__53:
			case T__57:
			case T__59:
			case T__61:
			case T__62:
			case T__93:
			case T__94:
			case T__106:
			case T__107:
			case T__108:
			case T__109:
			case T__110:
			case T__111:
			case T__112:
			case T__113:
			case T__114:
			case T__115:
			case T__116:
			case T__117:
			case T__118:
			case T__119:
			case T__120:
			case T__121:
			case T__122:
			case T__123:
			case T__124:
			case T__125:
			case T__126:
			case T__127:
			case T__128:
			case T__129:
			case T__130:
			case T__131:
			case T__132:
			case T__133:
			case T__134:
			case T__135:
			case T__136:
			case IDENTIFIER:
				{
				setState(1472);
				pathName();
				setState(1478);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,155,_ctx) ) {
				case 1:
					{
					setState(1473);
					match(T__7);
					setState(1475);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
						{
						setState(1474);
						expression(0);
						}
					}

					setState(1477);
					match(T__8);
					}
					break;
				}
				}
				break;
			case STRING_LITERAL:
				{
				setState(1480);
				match(STRING_LITERAL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssertExpContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public LogCallExpContext logCallExp() {
			return getRuleContext(LogCallExpContext.class,0);
		}
		public AssertExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assertExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitAssertExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssertExpContext assertExp() throws RecognitionException {
		AssertExpContext _localctx = new AssertExpContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_assertExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1483);
			match(T__131);
			setState(1485);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104680760L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 17592186036227L) != 0) || _la==IDENTIFIER) {
				{
				setState(1484);
				qvtoIdentifier();
				}
			}

			setState(1487);
			match(T__7);
			setState(1488);
			expression(0);
			setState(1489);
			match(T__8);
			setState(1492);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,158,_ctx) ) {
			case 1:
				{
				setState(1490);
				match(T__62);
				setState(1491);
				logCallExp();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LogCallExpContext extends ParserRuleContext {
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LogCallExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logCallExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitLogCallExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogCallExpContext logCallExp() throws RecognitionException {
		LogCallExpContext _localctx = new LogCallExpContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_logCallExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1494);
			match(T__132);
			setState(1495);
			match(T__7);
			setState(1497);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
				{
				setState(1496);
				argumentList();
				}
			}

			setState(1499);
			match(T__8);
			setState(1502);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,160,_ctx) ) {
			case 1:
				{
				setState(1500);
				match(T__29);
				setState(1501);
				expression(0);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LogExpContext extends ParserRuleContext {
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LogExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitLogExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogExpContext logExp() throws RecognitionException {
		LogExpContext _localctx = new LogExpContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_logExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1504);
			match(T__132);
			setState(1505);
			match(T__7);
			setState(1507);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
				{
				setState(1506);
				argumentList();
				}
			}

			setState(1509);
			match(T__8);
			setState(1512);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,162,_ctx) ) {
			case 1:
				{
				setState(1510);
				match(T__29);
				setState(1511);
				expression(0);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnExpContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ReturnExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitReturnExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnExpContext returnExp() throws RecognitionException {
		ReturnExpContext _localctx = new ReturnExpContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_returnExp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1514);
			match(T__133);
			setState(1516);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,163,_ctx) ) {
			case 1:
				{
				setState(1515);
				expression(0);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarDeclExpContext extends ParserRuleContext {
		public List<VarDeclaratorContext> varDeclarator() {
			return getRuleContexts(VarDeclaratorContext.class);
		}
		public VarDeclaratorContext varDeclarator(int i) {
			return getRuleContext(VarDeclaratorContext.class,i);
		}
		public VarDeclExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitVarDeclExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclExpContext varDeclExp() throws RecognitionException {
		VarDeclExpContext _localctx = new VarDeclExpContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_varDeclExp);
		int _la;
		try {
			int _alt;
			setState(1539);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,166,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1518);
				match(T__134);
				setState(1519);
				varDeclarator();
				setState(1524);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,164,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1520);
						match(T__4);
						setState(1521);
						varDeclarator();
						}
						} 
					}
					setState(1526);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,164,_ctx);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1527);
				match(T__134);
				setState(1528);
				match(T__7);
				setState(1529);
				varDeclarator();
				setState(1534);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(1530);
					match(T__4);
					setState(1531);
					varDeclarator();
					}
					}
					setState(1536);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1537);
				match(T__8);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarDeclaratorContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public VarInitOpContext varInitOp() {
			return getRuleContext(VarInitOpContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VarDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclarator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitVarDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclaratorContext varDeclarator() throws RecognitionException {
		VarDeclaratorContext _localctx = new VarDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_varDeclarator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1541);
			qvtoIdentifier();
			setState(1544);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,167,_ctx) ) {
			case 1:
				{
				setState(1542);
				match(T__15);
				setState(1543);
				typeExpression();
				}
				break;
			}
			setState(1549);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,168,_ctx) ) {
			case 1:
				{
				setState(1546);
				varInitOp();
				setState(1547);
				expression(0);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarInitOpContext extends ParserRuleContext {
		public TerminalNode RESET_ASSIGN() { return getToken(QvtOParser.RESET_ASSIGN, 0); }
		public TerminalNode ORDERED_COPY() { return getToken(QvtOParser.ORDERED_COPY, 0); }
		public VarInitOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varInitOp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitVarInitOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarInitOpContext varInitOp() throws RecognitionException {
		VarInitOpContext _localctx = new VarInitOpContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_varInitOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1551);
			_la = _input.LA(1);
			if ( !(_la==T__34 || _la==RESET_ASSIGN || _la==ORDERED_COPY) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PathNameContext extends ParserRuleContext {
		public List<QvtoIdentifierContext> qvtoIdentifier() {
			return getRuleContexts(QvtoIdentifierContext.class);
		}
		public QvtoIdentifierContext qvtoIdentifier(int i) {
			return getRuleContext(QvtoIdentifierContext.class,i);
		}
		public PathNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPathName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathNameContext pathName() throws RecognitionException {
		PathNameContext _localctx = new PathNameContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_pathName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1553);
			qvtoIdentifier();
			setState(1558);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,169,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1554);
					match(T__60);
					setState(1555);
					qvtoIdentifier();
					}
					} 
				}
				setState(1560);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,169,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyOrCallSuffixContext extends ParserRuleContext {
		public PropertyOrCallSuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyOrCallSuffix; }
	 
		public PropertyOrCallSuffixContext() { }
		public void copyFrom(PropertyOrCallSuffixContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ResolveInCallSuffixContext extends PropertyOrCallSuffixContext {
		public ResolveInKindContext resolveInKind() {
			return getRuleContext(ResolveInKindContext.class,0);
		}
		public ScopedNameContext scopedName() {
			return getRuleContext(ScopedNameContext.class,0);
		}
		public ResolveArgsContext resolveArgs() {
			return getRuleContext(ResolveArgsContext.class,0);
		}
		public ResolveInCallSuffixContext(PropertyOrCallSuffixContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitResolveInCallSuffix(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ResolveCallSuffixContext extends PropertyOrCallSuffixContext {
		public ResolveKindContext resolveKind() {
			return getRuleContext(ResolveKindContext.class,0);
		}
		public ResolveArgsContext resolveArgs() {
			return getRuleContext(ResolveArgsContext.class,0);
		}
		public ResolveCallSuffixContext(PropertyOrCallSuffixContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitResolveCallSuffix(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DotCallSuffixContext extends PropertyOrCallSuffixContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public IsMarkedPreContext isMarkedPre() {
			return getRuleContext(IsMarkedPreContext.class,0);
		}
		public DotCallSuffixContext(PropertyOrCallSuffixContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitDotCallSuffix(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PropertySuffixContext extends PropertyOrCallSuffixContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public IsMarkedPreContext isMarkedPre() {
			return getRuleContext(IsMarkedPreContext.class,0);
		}
		public PropertySuffixContext(PropertyOrCallSuffixContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPropertySuffix(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MappingCallSuffixContext extends PropertyOrCallSuffixContext {
		public MappingCallKindContext mappingCallKind() {
			return getRuleContext(MappingCallKindContext.class,0);
		}
		public ScopedNameContext scopedName() {
			return getRuleContext(ScopedNameContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public MappingCallSuffixContext(PropertyOrCallSuffixContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMappingCallSuffix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyOrCallSuffixContext propertyOrCallSuffix() throws RecognitionException {
		PropertyOrCallSuffixContext _localctx = new PropertyOrCallSuffixContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_propertyOrCallSuffix);
		int _la;
		try {
			setState(1604);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,178,_ctx) ) {
			case 1:
				_localctx = new MappingCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1561);
				mappingCallKind();
				setState(1562);
				scopedName();
				setState(1563);
				match(T__7);
				setState(1565);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
					{
					setState(1564);
					argumentList();
					}
				}

				setState(1567);
				match(T__8);
				}
				break;
			case 2:
				_localctx = new ResolveCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1570);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__118) {
					{
					setState(1569);
					match(T__118);
					}
				}

				setState(1572);
				resolveKind();
				setState(1573);
				match(T__7);
				setState(1575);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104680760L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & -22500405950815745L) != 0) || _la==IDENTIFIER) {
					{
					setState(1574);
					resolveArgs();
					}
				}

				setState(1577);
				match(T__8);
				}
				break;
			case 3:
				_localctx = new ResolveInCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1580);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__118) {
					{
					setState(1579);
					match(T__118);
					}
				}

				setState(1582);
				resolveInKind();
				setState(1583);
				match(T__7);
				setState(1584);
				scopedName();
				setState(1587);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(1585);
					match(T__4);
					setState(1586);
					resolveArgs();
					}
				}

				setState(1589);
				match(T__8);
				}
				break;
			case 4:
				_localctx = new DotCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(1591);
				qvtoIdentifier();
				setState(1592);
				match(T__7);
				setState(1594);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
					{
					setState(1593);
					argumentList();
					}
				}

				setState(1596);
				match(T__8);
				setState(1598);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,176,_ctx) ) {
				case 1:
					{
					setState(1597);
					isMarkedPre();
					}
					break;
				}
				}
				break;
			case 5:
				_localctx = new PropertySuffixContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(1600);
				qvtoIdentifier();
				setState(1602);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,177,_ctx) ) {
				case 1:
					{
					setState(1601);
					isMarkedPre();
					}
					break;
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IteratorOrOperationCallContext extends ParserRuleContext {
		public IteratorOrOperationCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iteratorOrOperationCall; }
	 
		public IteratorOrOperationCallContext() { }
		public void copyFrom(IteratorOrOperationCallContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CollectionOperationCallContext extends IteratorOrOperationCallContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public CollectionOperationCallContext(IteratorOrOperationCallContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitCollectionOperationCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrowSwitchCallContext extends IteratorOrOperationCallContext {
		public SwitchExpContext switchExp() {
			return getRuleContext(SwitchExpContext.class,0);
		}
		public ArrowSwitchCallContext(IteratorOrOperationCallContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitArrowSwitchCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrowMappingCallContext extends IteratorOrOperationCallContext {
		public MappingCallKindContext mappingCallKind() {
			return getRuleContext(MappingCallKindContext.class,0);
		}
		public ScopedNameContext scopedName() {
			return getRuleContext(ScopedNameContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ArrowMappingCallContext(IteratorOrOperationCallContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitArrowMappingCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrowPropertyCallContext extends IteratorOrOperationCallContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public ArrowPropertyCallContext(IteratorOrOperationCallContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitArrowPropertyCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrowResolveInCallContext extends IteratorOrOperationCallContext {
		public ResolveInKindContext resolveInKind() {
			return getRuleContext(ResolveInKindContext.class,0);
		}
		public ScopedNameContext scopedName() {
			return getRuleContext(ScopedNameContext.class,0);
		}
		public ResolveArgsContext resolveArgs() {
			return getRuleContext(ResolveArgsContext.class,0);
		}
		public ArrowResolveInCallContext(IteratorOrOperationCallContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitArrowResolveInCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ForEachCallContext extends IteratorOrOperationCallContext {
		public ForKindContext forKind() {
			return getRuleContext(ForKindContext.class,0);
		}
		public ForVarListContext forVarList() {
			return getRuleContext(ForVarListContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ForEachCallContext(IteratorOrOperationCallContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitForEachCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IterateCallContext extends IteratorOrOperationCallContext {
		public TypeExpressionContext iterType;
		public TypeExpressionContext accType;
		public List<QvtoIdentifierContext> qvtoIdentifier() {
			return getRuleContexts(QvtoIdentifierContext.class);
		}
		public QvtoIdentifierContext qvtoIdentifier(int i) {
			return getRuleContext(QvtoIdentifierContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TypeExpressionContext> typeExpression() {
			return getRuleContexts(TypeExpressionContext.class);
		}
		public TypeExpressionContext typeExpression(int i) {
			return getRuleContext(TypeExpressionContext.class,i);
		}
		public IterateCallContext(IteratorOrOperationCallContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitIterateCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrowResolveCallContext extends IteratorOrOperationCallContext {
		public ResolveKindContext resolveKind() {
			return getRuleContext(ResolveKindContext.class,0);
		}
		public ResolveArgsContext resolveArgs() {
			return getRuleContext(ResolveArgsContext.class,0);
		}
		public ArrowResolveCallContext(IteratorOrOperationCallContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitArrowResolveCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IteratorCallContext extends IteratorOrOperationCallContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public IteratorVariablesContext iteratorVariables() {
			return getRuleContext(IteratorVariablesContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public IteratorCallContext(IteratorOrOperationCallContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitIteratorCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrowObjectCallContext extends IteratorOrOperationCallContext {
		public ObjectExpContext objectExp() {
			return getRuleContext(ObjectExpContext.class,0);
		}
		public ArrowObjectCallContext(IteratorOrOperationCallContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitArrowObjectCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IteratorOrOperationCallContext iteratorOrOperationCall() throws RecognitionException {
		IteratorOrOperationCallContext _localctx = new IteratorOrOperationCallContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_iteratorOrOperationCall);
		int _la;
		try {
			setState(1682);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,188,_ctx) ) {
			case 1:
				_localctx = new ForEachCallContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1606);
				forKind();
				setState(1607);
				match(T__7);
				setState(1608);
				forVarList();
				setState(1611);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__67) {
					{
					setState(1609);
					match(T__67);
					setState(1610);
					expression(0);
					}
				}

				setState(1613);
				match(T__8);
				setState(1614);
				block();
				}
				break;
			case 2:
				_localctx = new ArrowResolveCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1617);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__118) {
					{
					setState(1616);
					match(T__118);
					}
				}

				setState(1619);
				resolveKind();
				setState(1620);
				match(T__7);
				setState(1622);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104680760L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & -22500405950815745L) != 0) || _la==IDENTIFIER) {
					{
					setState(1621);
					resolveArgs();
					}
				}

				setState(1624);
				match(T__8);
				}
				break;
			case 3:
				_localctx = new ArrowResolveInCallContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1627);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__118) {
					{
					setState(1626);
					match(T__118);
					}
				}

				setState(1629);
				resolveInKind();
				setState(1630);
				match(T__7);
				setState(1631);
				scopedName();
				setState(1634);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(1632);
					match(T__4);
					setState(1633);
					resolveArgs();
					}
				}

				setState(1636);
				match(T__8);
				}
				break;
			case 4:
				_localctx = new ArrowMappingCallContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(1638);
				mappingCallKind();
				setState(1639);
				scopedName();
				setState(1640);
				match(T__7);
				setState(1642);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
					{
					setState(1641);
					argumentList();
					}
				}

				setState(1644);
				match(T__8);
				}
				break;
			case 5:
				_localctx = new ArrowObjectCallContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(1646);
				objectExp();
				}
				break;
			case 6:
				_localctx = new ArrowSwitchCallContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(1647);
				switchExp();
				}
				break;
			case 7:
				_localctx = new IteratorCallContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(1648);
				qvtoIdentifier();
				setState(1649);
				match(T__7);
				setState(1650);
				iteratorVariables();
				setState(1651);
				match(T__67);
				setState(1652);
				expression(0);
				setState(1653);
				match(T__8);
				}
				break;
			case 8:
				_localctx = new IterateCallContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(1655);
				qvtoIdentifier();
				setState(1656);
				match(T__7);
				setState(1657);
				qvtoIdentifier();
				setState(1660);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__15) {
					{
					setState(1658);
					match(T__15);
					setState(1659);
					((IterateCallContext)_localctx).iterType = typeExpression();
					}
				}

				setState(1662);
				match(T__1);
				setState(1663);
				qvtoIdentifier();
				setState(1666);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__15) {
					{
					setState(1664);
					match(T__15);
					setState(1665);
					((IterateCallContext)_localctx).accType = typeExpression();
					}
				}

				setState(1668);
				match(T__34);
				setState(1669);
				expression(0);
				setState(1670);
				match(T__67);
				setState(1671);
				expression(0);
				setState(1672);
				match(T__8);
				}
				break;
			case 9:
				_localctx = new CollectionOperationCallContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(1674);
				qvtoIdentifier();
				setState(1675);
				match(T__7);
				setState(1677);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
					{
					setState(1676);
					argumentList();
					}
				}

				setState(1679);
				match(T__8);
				}
				break;
			case 10:
				_localctx = new ArrowPropertyCallContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(1681);
				qvtoIdentifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IteratorVariablesContext extends ParserRuleContext {
		public List<QvtoIdentifierContext> qvtoIdentifier() {
			return getRuleContexts(QvtoIdentifierContext.class);
		}
		public QvtoIdentifierContext qvtoIdentifier(int i) {
			return getRuleContext(QvtoIdentifierContext.class,i);
		}
		public List<TypeExpressionContext> typeExpression() {
			return getRuleContexts(TypeExpressionContext.class);
		}
		public TypeExpressionContext typeExpression(int i) {
			return getRuleContext(TypeExpressionContext.class,i);
		}
		public IteratorVariablesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iteratorVariables; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitIteratorVariables(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IteratorVariablesContext iteratorVariables() throws RecognitionException {
		IteratorVariablesContext _localctx = new IteratorVariablesContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_iteratorVariables);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1684);
			qvtoIdentifier();
			setState(1687);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__15) {
				{
				setState(1685);
				match(T__15);
				setState(1686);
				typeExpression();
				}
			}

			setState(1697);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1689);
				match(T__4);
				setState(1690);
				qvtoIdentifier();
				setState(1693);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__15) {
					{
					setState(1691);
					match(T__15);
					setState(1692);
					typeExpression();
					}
				}

				}
				}
				setState(1699);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LetBindingContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public LetBindingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letBinding; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitLetBinding(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetBindingContext letBinding() throws RecognitionException {
		LetBindingContext _localctx = new LetBindingContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_letBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1700);
			qvtoIdentifier();
			setState(1703);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__15) {
				{
				setState(1701);
				match(T__15);
				setState(1702);
				typeExpression();
				}
			}

			setState(1705);
			match(T__34);
			setState(1706);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TupleTypePartContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public TupleTypePartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleTypePart; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTupleTypePart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleTypePartContext tupleTypePart() throws RecognitionException {
		TupleTypePartContext _localctx = new TupleTypePartContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_tupleTypePart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1708);
			qvtoIdentifier();
			setState(1709);
			match(T__15);
			setState(1710);
			typeExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TupleLiteralPartContext extends ParserRuleContext {
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public TupleLiteralPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleLiteralPart; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTupleLiteralPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleLiteralPartContext tupleLiteralPart() throws RecognitionException {
		TupleLiteralPartContext _localctx = new TupleLiteralPartContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_tupleLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1712);
			qvtoIdentifier();
			setState(1715);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__15) {
				{
				setState(1713);
				match(T__15);
				setState(1714);
				typeExpression();
				}
			}

			setState(1717);
			match(T__34);
			setState(1718);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ScopedNameContext extends ParserRuleContext {
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public List<QvtoIdentifierContext> qvtoIdentifier() {
			return getRuleContexts(QvtoIdentifierContext.class);
		}
		public QvtoIdentifierContext qvtoIdentifier(int i) {
			return getRuleContext(QvtoIdentifierContext.class,i);
		}
		public CollectionTypeContext collectionType() {
			return getRuleContext(CollectionTypeContext.class,0);
		}
		public ScopedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scopedName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitScopedName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScopedNameContext scopedName() throws RecognitionException {
		ScopedNameContext _localctx = new ScopedNameContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_scopedName);
		int _la;
		try {
			setState(1736);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__148:
			case T__149:
			case T__150:
			case T__151:
			case T__152:
			case T__153:
			case T__154:
			case T__155:
			case T__156:
				enterOuterAlt(_localctx, 1);
				{
				setState(1720);
				primitiveType();
				setState(1721);
				match(T__60);
				setState(1722);
				qvtoIdentifier();
				}
				break;
			case T__95:
			case T__97:
			case T__98:
			case T__99:
			case T__100:
			case T__101:
				enterOuterAlt(_localctx, 2);
				{
				setState(1724);
				collectionType();
				setState(1725);
				match(T__60);
				setState(1726);
				qvtoIdentifier();
				}
				break;
			case T__2:
			case T__5:
			case T__6:
			case T__9:
			case T__12:
			case T__14:
			case T__20:
			case T__21:
			case T__22:
			case T__23:
			case T__24:
			case T__25:
			case T__26:
			case T__27:
			case T__28:
			case T__29:
			case T__30:
			case T__31:
			case T__32:
			case T__33:
			case T__35:
			case T__36:
			case T__37:
			case T__38:
			case T__39:
			case T__40:
			case T__41:
			case T__43:
			case T__44:
			case T__45:
			case T__46:
			case T__47:
			case T__48:
			case T__49:
			case T__50:
			case T__51:
			case T__52:
			case T__53:
			case T__57:
			case T__59:
			case T__61:
			case T__62:
			case T__93:
			case T__94:
			case T__106:
			case T__107:
			case T__108:
			case T__109:
			case T__110:
			case T__111:
			case T__112:
			case T__113:
			case T__114:
			case T__115:
			case T__116:
			case T__117:
			case T__118:
			case T__119:
			case T__120:
			case T__121:
			case T__122:
			case T__123:
			case T__124:
			case T__125:
			case T__126:
			case T__127:
			case T__128:
			case T__129:
			case T__130:
			case T__131:
			case T__132:
			case T__133:
			case T__134:
			case T__135:
			case T__136:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(1728);
				qvtoIdentifier();
				setState(1733);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__60) {
					{
					{
					setState(1729);
					match(T__60);
					setState(1730);
					qvtoIdentifier();
					}
					}
					setState(1735);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ScopedNameListContext extends ParserRuleContext {
		public List<ScopedNameContext> scopedName() {
			return getRuleContexts(ScopedNameContext.class);
		}
		public ScopedNameContext scopedName(int i) {
			return getRuleContext(ScopedNameContext.class,i);
		}
		public ScopedNameListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scopedNameList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitScopedNameList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScopedNameListContext scopedNameList() throws RecognitionException {
		ScopedNameListContext _localctx = new ScopedNameListContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_scopedNameList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1738);
			scopedName();
			setState(1743);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1739);
				match(T__4);
				setState(1740);
				scopedName();
				}
				}
				setState(1745);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QualifiedNameContext extends ParserRuleContext {
		public List<QvtoIdentifierContext> qvtoIdentifier() {
			return getRuleContexts(QvtoIdentifierContext.class);
		}
		public QvtoIdentifierContext qvtoIdentifier(int i) {
			return getRuleContext(QvtoIdentifierContext.class,i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitQualifiedName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1746);
			qvtoIdentifier();
			setState(1751);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__63) {
				{
				{
				setState(1747);
				match(T__63);
				setState(1748);
				qvtoIdentifier();
				}
				}
				setState(1753);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QvtoIdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(QvtOParser.IDENTIFIER, 0); }
		public QvtoIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qvtoIdentifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitQvtoIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QvtoIdentifierContext qvtoIdentifier() throws RecognitionException {
		QvtoIdentifierContext _localctx = new QvtoIdentifierContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_qvtoIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1754);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104680760L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 17592186036227L) != 0) || _la==IDENTIFIER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionEntryContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(QvtOParser.EOF, 0); }
		public ExpressionEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionEntry; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitExpressionEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionEntryContext expressionEntry() throws RecognitionException {
		ExpressionEntryContext _localctx = new ExpressionEntryContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_expressionEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1756);
			expression(0);
			setState(1757);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompleteOclDocumentEntryContext extends ParserRuleContext {
		public CompleteOclDocumentContext completeOclDocument() {
			return getRuleContext(CompleteOclDocumentContext.class,0);
		}
		public TerminalNode EOF() { return getToken(QvtOParser.EOF, 0); }
		public CompleteOclDocumentEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_completeOclDocumentEntry; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitCompleteOclDocumentEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompleteOclDocumentEntryContext completeOclDocumentEntry() throws RecognitionException {
		CompleteOclDocumentEntryContext _localctx = new CompleteOclDocumentEntryContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_completeOclDocumentEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1759);
			completeOclDocument();
			setState(1760);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompleteOclDocumentContext extends ParserRuleContext {
		public List<ImportDeclarationContext> importDeclaration() {
			return getRuleContexts(ImportDeclarationContext.class);
		}
		public ImportDeclarationContext importDeclaration(int i) {
			return getRuleContext(ImportDeclarationContext.class,i);
		}
		public List<PackageDeclarationContext> packageDeclaration() {
			return getRuleContexts(PackageDeclarationContext.class);
		}
		public PackageDeclarationContext packageDeclaration(int i) {
			return getRuleContext(PackageDeclarationContext.class,i);
		}
		public List<ContextDeclarationContext> contextDeclaration() {
			return getRuleContexts(ContextDeclarationContext.class);
		}
		public ContextDeclarationContext contextDeclaration(int i) {
			return getRuleContext(ContextDeclarationContext.class,i);
		}
		public CompleteOclDocumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_completeOclDocument; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitCompleteOclDocument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompleteOclDocumentContext completeOclDocument() throws RecognitionException {
		CompleteOclDocumentContext _localctx = new CompleteOclDocumentContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_completeOclDocument);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1765);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0 || _la==T__14 || _la==T__137) {
				{
				{
				setState(1762);
				importDeclaration();
				}
				}
				setState(1767);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1772);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__48 || _la==T__139) {
				{
				setState(1770);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__48:
					{
					setState(1768);
					packageDeclaration();
					}
					break;
				case T__139:
					{
					setState(1769);
					contextDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(1774);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ImportDeclarationContext extends ParserRuleContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(QvtOParser.IDENTIFIER, 0); }
		public ImportDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitImportDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportDeclarationContext importDeclaration() throws RecognitionException {
		ImportDeclarationContext _localctx = new ImportDeclarationContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_importDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1775);
			_la = _input.LA(1);
			if ( !(_la==T__0 || _la==T__14 || _la==T__137) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1778);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,201,_ctx) ) {
			case 1:
				{
				setState(1776);
				match(IDENTIFIER);
				setState(1777);
				match(T__15);
				}
				break;
			}
			setState(1780);
			pathName();
			setState(1783);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__60) {
				{
				setState(1781);
				match(T__60);
				setState(1782);
				match(T__3);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PackageDeclarationContext extends ParserRuleContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public List<ContextDeclarationContext> contextDeclaration() {
			return getRuleContexts(ContextDeclarationContext.class);
		}
		public ContextDeclarationContext contextDeclaration(int i) {
			return getRuleContext(ContextDeclarationContext.class,i);
		}
		public PackageDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPackageDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageDeclarationContext packageDeclaration() throws RecognitionException {
		PackageDeclarationContext _localctx = new PackageDeclarationContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_packageDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1785);
			match(T__48);
			setState(1786);
			pathName();
			setState(1790);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__139) {
				{
				{
				setState(1787);
				contextDeclaration();
				}
				}
				setState(1792);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1793);
			match(T__138);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContextDeclarationContext extends ParserRuleContext {
		public ClassifierContextDeclarationContext classifierContextDeclaration() {
			return getRuleContext(ClassifierContextDeclarationContext.class,0);
		}
		public OperationContextDeclarationContext operationContextDeclaration() {
			return getRuleContext(OperationContextDeclarationContext.class,0);
		}
		public PropertyContextDeclarationContext propertyContextDeclaration() {
			return getRuleContext(PropertyContextDeclarationContext.class,0);
		}
		public ContextDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contextDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitContextDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextDeclarationContext contextDeclaration() throws RecognitionException {
		ContextDeclarationContext _localctx = new ContextDeclarationContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_contextDeclaration);
		try {
			setState(1798);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,204,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1795);
				classifierContextDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1796);
				operationContextDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1797);
				propertyContextDeclaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassifierContextDeclarationContext extends ParserRuleContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public List<ClassifierContextBodyContext> classifierContextBody() {
			return getRuleContexts(ClassifierContextBodyContext.class);
		}
		public ClassifierContextBodyContext classifierContextBody(int i) {
			return getRuleContext(ClassifierContextBodyContext.class,i);
		}
		public ClassifierContextDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classifierContextDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitClassifierContextDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassifierContextDeclarationContext classifierContextDeclaration() throws RecognitionException {
		ClassifierContextDeclarationContext _localctx = new ClassifierContextDeclarationContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_classifierContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1800);
			match(T__139);
			setState(1801);
			pathName();
			setState(1803); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1802);
				classifierContextBody();
				}
				}
				setState(1805); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__24 || _la==T__135 || _la==T__140 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassifierContextBodyContext extends ParserRuleContext {
		public InvariantConstraintContext invariantConstraint() {
			return getRuleContext(InvariantConstraintContext.class,0);
		}
		public DefinitionConstraintContext definitionConstraint() {
			return getRuleContext(DefinitionConstraintContext.class,0);
		}
		public ClassifierContextBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classifierContextBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitClassifierContextBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassifierContextBodyContext classifierContextBody() throws RecognitionException {
		ClassifierContextBodyContext _localctx = new ClassifierContextBodyContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_classifierContextBody);
		try {
			setState(1809);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__135:
				enterOuterAlt(_localctx, 1);
				{
				setState(1807);
				invariantConstraint();
				}
				break;
			case T__24:
			case T__140:
				enterOuterAlt(_localctx, 2);
				{
				setState(1808);
				definitionConstraint();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InvariantConstraintContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode IDENTIFIER() { return getToken(QvtOParser.IDENTIFIER, 0); }
		public InvariantConstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_invariantConstraint; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitInvariantConstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InvariantConstraintContext invariantConstraint() throws RecognitionException {
		InvariantConstraintContext _localctx = new InvariantConstraintContext(_ctx, getState());
		enterRule(_localctx, 256, RULE_invariantConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1811);
			match(T__135);
			setState(1819);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(1812);
				match(IDENTIFIER);
				setState(1817);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(1813);
					match(T__7);
					setState(1814);
					expression(0);
					setState(1815);
					match(T__8);
					}
				}

				}
			}

			setState(1821);
			match(T__15);
			setState(1822);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DefinitionConstraintContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(QvtOParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(QvtOParser.IDENTIFIER, i);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public DefinitionConstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definitionConstraint; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitDefinitionConstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionConstraintContext definitionConstraint() throws RecognitionException {
		DefinitionConstraintContext _localctx = new DefinitionConstraintContext(_ctx, getState());
		enterRule(_localctx, 258, RULE_definitionConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1825);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__24) {
				{
				setState(1824);
				match(T__24);
				}
			}

			setState(1827);
			match(T__140);
			setState(1829);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(1828);
				match(IDENTIFIER);
				}
			}

			setState(1831);
			match(T__15);
			setState(1849);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,212,_ctx) ) {
			case 1:
				{
				setState(1832);
				match(IDENTIFIER);
				setState(1833);
				match(T__15);
				setState(1834);
				typeExpression();
				setState(1835);
				match(T__34);
				setState(1836);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1838);
				match(IDENTIFIER);
				setState(1839);
				match(T__7);
				setState(1841);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1840);
					parameterList();
					}
				}

				setState(1843);
				match(T__8);
				setState(1844);
				match(T__15);
				setState(1845);
				typeExpression();
				setState(1846);
				match(T__34);
				setState(1847);
				expression(0);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperationContextDeclarationContext extends ParserRuleContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(QvtOParser.IDENTIFIER, 0); }
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public List<OperationContextBodyContext> operationContextBody() {
			return getRuleContexts(OperationContextBodyContext.class);
		}
		public OperationContextBodyContext operationContextBody(int i) {
			return getRuleContext(OperationContextBodyContext.class,i);
		}
		public OperationContextDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operationContextDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitOperationContextDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperationContextDeclarationContext operationContextDeclaration() throws RecognitionException {
		OperationContextDeclarationContext _localctx = new OperationContextDeclarationContext(_ctx, getState());
		enterRule(_localctx, 260, RULE_operationContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1851);
			match(T__139);
			setState(1852);
			pathName();
			setState(1853);
			match(T__60);
			setState(1854);
			match(IDENTIFIER);
			setState(1855);
			match(T__7);
			setState(1857);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(1856);
				parameterList();
				}
			}

			setState(1859);
			match(T__8);
			setState(1860);
			match(T__15);
			setState(1861);
			typeExpression();
			setState(1863); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1862);
				operationContextBody();
				}
				}
				setState(1865); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 142)) & ~0x3f) == 0 && ((1L << (_la - 142)) & 7L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperationContextBodyContext extends ParserRuleContext {
		public OperationContextBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operationContextBody; }
	 
		public OperationContextBodyContext() { }
		public void copyFrom(OperationContextBodyContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BodyExpressionContext extends OperationContextBodyContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(QvtOParser.IDENTIFIER, 0); }
		public BodyExpressionContext(OperationContextBodyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitBodyExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PreConditionContext extends OperationContextBodyContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(QvtOParser.IDENTIFIER, 0); }
		public PreConditionContext(OperationContextBodyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPreCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PostConditionContext extends OperationContextBodyContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(QvtOParser.IDENTIFIER, 0); }
		public PostConditionContext(OperationContextBodyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPostCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperationContextBodyContext operationContextBody() throws RecognitionException {
		OperationContextBodyContext _localctx = new OperationContextBodyContext(_ctx, getState());
		enterRule(_localctx, 262, RULE_operationContextBody);
		int _la;
		try {
			setState(1885);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__141:
				_localctx = new PreConditionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1867);
				match(T__141);
				setState(1869);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1868);
					match(IDENTIFIER);
					}
				}

				setState(1871);
				match(T__15);
				setState(1872);
				expression(0);
				}
				break;
			case T__142:
				_localctx = new PostConditionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1873);
				match(T__142);
				setState(1875);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1874);
					match(IDENTIFIER);
					}
				}

				setState(1877);
				match(T__15);
				setState(1878);
				expression(0);
				}
				break;
			case T__143:
				_localctx = new BodyExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1879);
				match(T__143);
				setState(1881);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1880);
					match(IDENTIFIER);
					}
				}

				setState(1883);
				match(T__15);
				setState(1884);
				expression(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyContextDeclarationContext extends ParserRuleContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(QvtOParser.IDENTIFIER, 0); }
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public List<PropertyContextBodyContext> propertyContextBody() {
			return getRuleContexts(PropertyContextBodyContext.class);
		}
		public PropertyContextBodyContext propertyContextBody(int i) {
			return getRuleContext(PropertyContextBodyContext.class,i);
		}
		public PropertyContextDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyContextDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPropertyContextDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContextDeclarationContext propertyContextDeclaration() throws RecognitionException {
		PropertyContextDeclarationContext _localctx = new PropertyContextDeclarationContext(_ctx, getState());
		enterRule(_localctx, 264, RULE_propertyContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1887);
			match(T__139);
			setState(1888);
			pathName();
			setState(1889);
			match(T__60);
			setState(1890);
			match(IDENTIFIER);
			setState(1891);
			match(T__15);
			setState(1892);
			typeExpression();
			setState(1894); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1893);
				propertyContextBody();
				}
				}
				setState(1896); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__30 || _la==T__144 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyContextBodyContext extends ParserRuleContext {
		public PropertyContextBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyContextBody; }
	 
		public PropertyContextBodyContext() { }
		public void copyFrom(PropertyContextBodyContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DeriveExpressionContext extends PropertyContextBodyContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(QvtOParser.IDENTIFIER, 0); }
		public DeriveExpressionContext(PropertyContextBodyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitDeriveExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InitExpressionContext extends PropertyContextBodyContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(QvtOParser.IDENTIFIER, 0); }
		public InitExpressionContext(PropertyContextBodyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitInitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContextBodyContext propertyContextBody() throws RecognitionException {
		PropertyContextBodyContext _localctx = new PropertyContextBodyContext(_ctx, getState());
		enterRule(_localctx, 266, RULE_propertyContextBody);
		int _la;
		try {
			setState(1910);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__30:
				_localctx = new InitExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1898);
				match(T__30);
				setState(1900);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1899);
					match(IDENTIFIER);
					}
				}

				setState(1902);
				match(T__15);
				setState(1903);
				expression(0);
				}
				break;
			case T__144:
				_localctx = new DeriveExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1904);
				match(T__144);
				setState(1906);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1905);
					match(IDENTIFIER);
					}
				}

				setState(1908);
				match(T__15);
				setState(1909);
				expression(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterListContext extends ParserRuleContext {
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public ParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterListContext parameterList() throws RecognitionException {
		ParameterListContext _localctx = new ParameterListContext(_ctx, getState());
		enterRule(_localctx, 268, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1912);
			parameter();
			setState(1917);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1913);
				match(T__4);
				setState(1914);
				parameter();
				}
				}
				setState(1919);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(QvtOParser.IDENTIFIER, 0); }
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 270, RULE_parameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1920);
			match(IDENTIFIER);
			setState(1921);
			match(T__15);
			setState(1922);
			typeExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 272, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1924);
			expression(0);
			setState(1929);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1925);
				match(T__4);
				setState(1926);
				expression(0);
				}
				}
				setState(1931);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IsMarkedPreContext extends ParserRuleContext {
		public IsMarkedPreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isMarkedPre; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitIsMarkedPre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IsMarkedPreContext isMarkedPre() throws RecognitionException {
		IsMarkedPreContext _localctx = new IsMarkedPreContext(_ctx, getState());
		enterRule(_localctx, 274, RULE_isMarkedPre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1932);
			match(T__19);
			setState(1933);
			match(T__141);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CollectionLiteralContext extends ParserRuleContext {
		public CollectionKindContext collectionKind() {
			return getRuleContext(CollectionKindContext.class,0);
		}
		public List<CollectionLiteralPartContext> collectionLiteralPart() {
			return getRuleContexts(CollectionLiteralPartContext.class);
		}
		public CollectionLiteralPartContext collectionLiteralPart(int i) {
			return getRuleContext(CollectionLiteralPartContext.class,i);
		}
		public CollectionLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionLiteral; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitCollectionLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionLiteralContext collectionLiteral() throws RecognitionException {
		CollectionLiteralContext _localctx = new CollectionLiteralContext(_ctx, getState());
		enterRule(_localctx, 276, RULE_collectionLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1935);
			collectionKind();
			setState(1936);
			match(T__10);
			setState(1945);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
				{
				setState(1937);
				collectionLiteralPart();
				setState(1942);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(1938);
					match(T__4);
					setState(1939);
					collectionLiteralPart();
					}
					}
					setState(1944);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1947);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CollectionLiteralPartContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public CollectionLiteralPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionLiteralPart; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitCollectionLiteralPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionLiteralPartContext collectionLiteralPart() throws RecognitionException {
		CollectionLiteralPartContext _localctx = new CollectionLiteralPartContext(_ctx, getState());
		enterRule(_localctx, 278, RULE_collectionLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1949);
			expression(0);
			setState(1952);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__56) {
				{
				setState(1950);
				match(T__56);
				setState(1951);
				expression(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TupleLiteralContext extends ParserRuleContext {
		public List<TupleLiteralPartContext> tupleLiteralPart() {
			return getRuleContexts(TupleLiteralPartContext.class);
		}
		public TupleLiteralPartContext tupleLiteralPart(int i) {
			return getRuleContext(TupleLiteralPartContext.class,i);
		}
		public TupleLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleLiteral; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTupleLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleLiteralContext tupleLiteral() throws RecognitionException {
		TupleLiteralContext _localctx = new TupleLiteralContext(_ctx, getState());
		enterRule(_localctx, 280, RULE_tupleLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1954);
			match(T__145);
			setState(1955);
			match(T__10);
			setState(1956);
			tupleLiteralPart();
			setState(1961);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1957);
				match(T__4);
				setState(1958);
				tupleLiteralPart();
				}
				}
				setState(1963);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1964);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MapLiteralContext extends ParserRuleContext {
		public List<MapLiteralPartContext> mapLiteralPart() {
			return getRuleContexts(MapLiteralPartContext.class);
		}
		public MapLiteralPartContext mapLiteralPart(int i) {
			return getRuleContext(MapLiteralPartContext.class,i);
		}
		public MapLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapLiteral; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMapLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapLiteralContext mapLiteral() throws RecognitionException {
		MapLiteralContext _localctx = new MapLiteralContext(_ctx, getState());
		enterRule(_localctx, 282, RULE_mapLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1966);
			match(T__146);
			setState(1967);
			match(T__10);
			setState(1976);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3134514171104678440L) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & -8159229L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 1066779258895L) != 0)) {
				{
				setState(1968);
				mapLiteralPart();
				setState(1973);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(1969);
					match(T__4);
					setState(1970);
					mapLiteralPart();
					}
					}
					setState(1975);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1978);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MapLiteralPartContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public MapLiteralPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapLiteralPart; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMapLiteralPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapLiteralPartContext mapLiteralPart() throws RecognitionException {
		MapLiteralPartContext _localctx = new MapLiteralPartContext(_ctx, getState());
		enterRule(_localctx, 284, RULE_mapLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1980);
			expression(0);
			setState(1981);
			_la = _input.LA(1);
			if ( !(_la==T__62 || _la==T__147) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1982);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveTypeContext extends ParserRuleContext {
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitPrimitiveType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 286, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1984);
			_la = _input.LA(1);
			if ( !(((((_la - 149)) & ~0x3f) == 0 && ((1L << (_la - 149)) & 511L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CollectionTypeContext extends ParserRuleContext {
		public CollectionKindContext collectionKind() {
			return getRuleContext(CollectionKindContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public CollectionTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitCollectionType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionTypeContext collectionType() throws RecognitionException {
		CollectionTypeContext _localctx = new CollectionTypeContext(_ctx, getState());
		enterRule(_localctx, 288, RULE_collectionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1986);
			collectionKind();
			setState(1987);
			match(T__7);
			setState(1988);
			typeExpression();
			setState(1989);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MapTypeContext extends ParserRuleContext {
		public List<TypeExpressionContext> typeExpression() {
			return getRuleContexts(TypeExpressionContext.class);
		}
		public TypeExpressionContext typeExpression(int i) {
			return getRuleContext(TypeExpressionContext.class,i);
		}
		public MapTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitMapType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapTypeContext mapType() throws RecognitionException {
		MapTypeContext _localctx = new MapTypeContext(_ctx, getState());
		enterRule(_localctx, 290, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1991);
			match(T__146);
			setState(1992);
			match(T__7);
			setState(1993);
			typeExpression();
			setState(1994);
			match(T__4);
			setState(1995);
			typeExpression();
			setState(1996);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TupleTypeContext extends ParserRuleContext {
		public List<TupleTypePartContext> tupleTypePart() {
			return getRuleContexts(TupleTypePartContext.class);
		}
		public TupleTypePartContext tupleTypePart(int i) {
			return getRuleContext(TupleTypePartContext.class,i);
		}
		public TupleTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtOVisitor ) return ((QvtOVisitor<? extends T>)visitor).visitTupleType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleTypeContext tupleType() throws RecognitionException {
		TupleTypeContext _localctx = new TupleTypeContext(_ctx, getState());
		enterRule(_localctx, 292, RULE_tupleType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1998);
			match(T__145);
			setState(1999);
			match(T__7);
			setState(2000);
			tupleTypePart();
			setState(2005);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(2001);
				match(T__4);
				setState(2002);
				tupleTypePart();
				}
				}
				setState(2007);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2008);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 67:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 10);
		case 1:
			return precpred(_ctx, 9);
		case 2:
			return precpred(_ctx, 8);
		case 3:
			return precpred(_ctx, 7);
		case 4:
			return precpred(_ctx, 6);
		case 5:
			return precpred(_ctx, 5);
		case 6:
			return precpred(_ctx, 4);
		case 7:
			return precpred(_ctx, 3);
		case 8:
			return precpred(_ctx, 19);
		case 9:
			return precpred(_ctx, 18);
		case 10:
			return precpred(_ctx, 17);
		case 11:
			return precpred(_ctx, 16);
		case 12:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u00af\u07db\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007"+
		"\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007"+
		"\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007"+
		"\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007"+
		"\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007"+
		"\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007"+
		",\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u0007"+
		"1\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u0007"+
		"6\u00027\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007"+
		";\u0002<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007"+
		"@\u0002A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007D\u0002E\u0007"+
		"E\u0002F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007I\u0002J\u0007"+
		"J\u0002K\u0007K\u0002L\u0007L\u0002M\u0007M\u0002N\u0007N\u0002O\u0007"+
		"O\u0002P\u0007P\u0002Q\u0007Q\u0002R\u0007R\u0002S\u0007S\u0002T\u0007"+
		"T\u0002U\u0007U\u0002V\u0007V\u0002W\u0007W\u0002X\u0007X\u0002Y\u0007"+
		"Y\u0002Z\u0007Z\u0002[\u0007[\u0002\\\u0007\\\u0002]\u0007]\u0002^\u0007"+
		"^\u0002_\u0007_\u0002`\u0007`\u0002a\u0007a\u0002b\u0007b\u0002c\u0007"+
		"c\u0002d\u0007d\u0002e\u0007e\u0002f\u0007f\u0002g\u0007g\u0002h\u0007"+
		"h\u0002i\u0007i\u0002j\u0007j\u0002k\u0007k\u0002l\u0007l\u0002m\u0007"+
		"m\u0002n\u0007n\u0002o\u0007o\u0002p\u0007p\u0002q\u0007q\u0002r\u0007"+
		"r\u0002s\u0007s\u0002t\u0007t\u0002u\u0007u\u0002v\u0007v\u0002w\u0007"+
		"w\u0002x\u0007x\u0002y\u0007y\u0002z\u0007z\u0002{\u0007{\u0002|\u0007"+
		"|\u0002}\u0007}\u0002~\u0007~\u0002\u007f\u0007\u007f\u0002\u0080\u0007"+
		"\u0080\u0002\u0081\u0007\u0081\u0002\u0082\u0007\u0082\u0002\u0083\u0007"+
		"\u0083\u0002\u0084\u0007\u0084\u0002\u0085\u0007\u0085\u0002\u0086\u0007"+
		"\u0086\u0002\u0087\u0007\u0087\u0002\u0088\u0007\u0088\u0002\u0089\u0007"+
		"\u0089\u0002\u008a\u0007\u008a\u0002\u008b\u0007\u008b\u0002\u008c\u0007"+
		"\u008c\u0002\u008d\u0007\u008d\u0002\u008e\u0007\u008e\u0002\u008f\u0007"+
		"\u008f\u0002\u0090\u0007\u0090\u0002\u0091\u0007\u0091\u0002\u0092\u0007"+
		"\u0092\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0005\u0001\u012b"+
		"\b\u0001\n\u0001\f\u0001\u012e\t\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002\u0136\b\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003\u0142\b\u0003\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u0148\b\u0004\n\u0004"+
		"\f\u0004\u014b\t\u0004\u0003\u0004\u014d\b\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0003\u0005\u0152\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0003\u0005\u0157\b\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0005\u0006\u015e\b\u0006\n\u0006\f\u0006\u0161\t\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u0167\b\u0007\u0001"+
		"\u0007\u0003\u0007\u016a\b\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0005\b\u0171\b\b\n\b\f\b\u0174\t\b\u0001\b\u0001\b\u0001\t\u0005\t"+
		"\u0179\b\t\n\t\f\t\u017c\t\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u0182"+
		"\b\t\u0001\t\u0001\t\u0005\t\u0186\b\t\n\t\f\t\u0189\t\t\u0001\t\u0003"+
		"\t\u018c\b\t\u0001\t\u0001\t\u0005\t\u0190\b\t\n\t\f\t\u0193\t\t\u0001"+
		"\t\u0001\t\u0003\t\u0197\b\t\u0001\t\u0003\t\u019a\b\t\u0001\n\u0001\n"+
		"\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u01a2\b\u000b"+
		"\u0001\u000b\u0005\u000b\u01a5\b\u000b\n\u000b\f\u000b\u01a8\t\u000b\u0001"+
		"\u000b\u0001\u000b\u0005\u000b\u01ac\b\u000b\n\u000b\f\u000b\u01af\t\u000b"+
		"\u0001\u000b\u0001\u000b\u0003\u000b\u01b3\b\u000b\u0001\f\u0001\f\u0001"+
		"\f\u0005\f\u01b8\b\f\n\f\f\f\u01bb\t\f\u0001\r\u0001\r\u0001\r\u0001\r"+
		"\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u01c5\b\r\u0001\u000e\u0001\u000e"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u01cc\b\u000f\u0001\u0010"+
		"\u0001\u0010\u0003\u0010\u01d0\b\u0010\u0001\u0010\u0001\u0010\u0001\u0011"+
		"\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u01d9\b\u0012"+
		"\n\u0012\f\u0012\u01dc\t\u0012\u0001\u0013\u0001\u0013\u0003\u0013\u01e0"+
		"\b\u0013\u0001\u0014\u0001\u0014\u0003\u0014\u01e4\b\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016"+
		"\u01fe\b\u0016\u0001\u0017\u0005\u0017\u0201\b\u0017\n\u0017\f\u0017\u0204"+
		"\t\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u0208\b\u0017\u0001\u0017"+
		"\u0001\u0017\u0001\u0017\u0005\u0017\u020d\b\u0017\n\u0017\f\u0017\u0210"+
		"\t\u0017\u0001\u0017\u0003\u0017\u0213\b\u0017\u0001\u0017\u0003\u0017"+
		"\u0216\b\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u021a\b\u0017\u0001"+
		"\u0017\u0005\u0017\u021d\b\u0017\n\u0017\f\u0017\u0220\t\u0017\u0001\u0017"+
		"\u0001\u0017\u0003\u0017\u0224\b\u0017\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0005\u0017\u0229\b\u0017\n\u0017\f\u0017\u022c\t\u0017\u0001\u0017\u0001"+
		"\u0017\u0003\u0017\u0230\b\u0017\u0001\u0018\u0001\u0018\u0003\u0018\u0234"+
		"\b\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u0239\b\u0018"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0005\u0019\u023e\b\u0019\n\u0019"+
		"\f\u0019\u0241\t\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0003\u001a\u0248\b\u001a\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0005\u001b\u024d\b\u001b\n\u001b\f\u001b\u0250\t\u001b\u0001\u001c\u0003"+
		"\u001c\u0253\b\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u025e"+
		"\b\u001d\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0001"+
		" \u0001 \u0001 \u0001!\u0001!\u0001!\u0001!\u0005!\u026c\b!\n!\f!\u026f"+
		"\t!\u0001!\u0003!\u0272\b!\u0001!\u0001!\u0001\"\u0001\"\u0003\"\u0278"+
		"\b\"\u0001\"\u0001\"\u0003\"\u027c\b\"\u0001\"\u0003\"\u027f\b\"\u0001"+
		"\"\u0001\"\u0001#\u0001#\u0001#\u0001$\u0001$\u0001$\u0001%\u0001%\u0001"+
		"%\u0001&\u0005&\u028d\b&\n&\f&\u0290\t&\u0001&\u0001&\u0001&\u0001&\u0001"+
		"&\u0003&\u0297\b&\u0001&\u0001&\u0001&\u0001&\u0001&\u0001&\u0003&\u029f"+
		"\b&\u0001\'\u0005\'\u02a2\b\'\n\'\f\'\u02a5\t\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0003"+
		"\'\u02b2\b\'\u0001(\u0005(\u02b5\b(\n(\f(\u02b8\t(\u0001(\u0001(\u0001"+
		"(\u0001(\u0001(\u0003(\u02bf\b(\u0001(\u0003(\u02c2\b(\u0001)\u0001)\u0001"+
		")\u0001)\u0003)\u02c8\b)\u0001)\u0001)\u0003)\u02cc\b)\u0001)\u0001)\u0003"+
		")\u02d0\b)\u0003)\u02d2\b)\u0001*\u0001*\u0003*\u02d6\b*\u0001*\u0001"+
		"*\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0003+\u02e1\b+\u0001"+
		"+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0003+\u02ec"+
		"\b+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0003+\u02f6"+
		"\b+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0003+\u0300"+
		"\b+\u0001,\u0001,\u0001,\u0001,\u0001,\u0003,\u0307\b,\u0001,\u0001,\u0005"+
		",\u030b\b,\n,\f,\u030e\t,\u0001,\u0001,\u0003,\u0312\b,\u0001-\u0001-"+
		"\u0001-\u0001-\u0003-\u0318\b-\u0001-\u0001-\u0005-\u031c\b-\n-\f-\u031f"+
		"\t-\u0001-\u0001-\u0003-\u0323\b-\u0001.\u0001.\u0001.\u0001.\u0005.\u0329"+
		"\b.\n.\f.\u032c\t.\u0001.\u0001.\u0003.\u0330\b.\u0001/\u0001/\u0001/"+
		"\u0001/\u00010\u00010\u00010\u00010\u00030\u033a\b0\u00010\u00010\u0003"+
		"0\u033e\b0\u00011\u00011\u00011\u00051\u0343\b1\n1\f1\u0346\t1\u00012"+
		"\u00012\u00012\u00032\u034b\b2\u00013\u00013\u00013\u00013\u00053\u0351"+
		"\b3\n3\f3\u0354\t3\u00013\u00013\u00033\u0358\b3\u00014\u00014\u00014"+
		"\u00014\u00014\u00014\u00014\u00014\u00034\u0362\b4\u00015\u00015\u0001"+
		"5\u00055\u0367\b5\n5\f5\u036a\t5\u00016\u00056\u036d\b6\n6\f6\u0370\t"+
		"6\u00016\u00016\u00016\u00016\u00016\u00016\u00016\u00056\u0379\b6\n6"+
		"\f6\u037c\t6\u00016\u00016\u00016\u00016\u00036\u0382\b6\u00016\u0003"+
		"6\u0385\b6\u00016\u00036\u0388\b6\u00016\u00016\u00036\u038c\b6\u0001"+
		"6\u00016\u00036\u0390\b6\u00017\u00017\u00017\u00017\u00017\u00017\u0003"+
		"7\u0398\b7\u00018\u00018\u00018\u00018\u00058\u039e\b8\n8\f8\u03a1\t8"+
		"\u00018\u00018\u00019\u00019\u00019\u00019\u0001:\u0001:\u0001:\u0001"+
		":\u0001:\u0003:\u03ae\b:\u0001;\u0001;\u0003;\u03b2\b;\u0001;\u0001;\u0003"+
		";\u03b6\b;\u0001<\u0001<\u0001<\u0001<\u0001<\u0003<\u03bd\b<\u0001=\u0001"+
		"=\u0001=\u0005=\u03c2\b=\n=\f=\u03c5\t=\u0001>\u0001>\u0001>\u0001>\u0001"+
		">\u0001>\u0003>\u03cd\b>\u0001?\u0005?\u03d0\b?\n?\f?\u03d3\t?\u0001@"+
		"\u0001@\u0001@\u0001@\u0001@\u0001@\u0003@\u03db\b@\u0001A\u0001A\u0001"+
		"B\u0001B\u0005B\u03e1\bB\nB\fB\u03e4\tB\u0001B\u0001B\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0003"+
		"C\u03f4\bC\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0003C\u0419\bC\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0003C\u0424\bC\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0003C\u042e\bC\u0005"+
		"C\u0430\bC\nC\fC\u0433\tC\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001"+
		"D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0005D\u0444"+
		"\bD\nD\fD\u0447\tD\u0001D\u0001D\u0003D\u044b\bD\u0001D\u0001D\u0001D"+
		"\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001"+
		"D\u0005D\u045a\bD\nD\fD\u045d\tD\u0001D\u0001D\u0003D\u0461\bD\u0001D"+
		"\u0003D\u0464\bD\u0001D\u0001D\u0001D\u0001D\u0005D\u046a\bD\nD\fD\u046d"+
		"\tD\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001"+
		"D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001"+
		"D\u0001D\u0001D\u0001D\u0001D\u0001D\u0003D\u0488\bD\u0001D\u0001D\u0001"+
		"D\u0001D\u0001D\u0001D\u0001D\u0003D\u0491\bD\u0001E\u0001E\u0001E\u0001"+
		"E\u0001E\u0001E\u0001E\u0003E\u049a\bE\u0001F\u0001F\u0001F\u0001F\u0001"+
		"F\u0001G\u0001G\u0001G\u0001G\u0001G\u0001G\u0001G\u0001H\u0001H\u0001"+
		"I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001"+
		"I\u0001I\u0003I\u04b6\bI\u0001J\u0004J\u04b9\bJ\u000bJ\fJ\u04ba\u0001"+
		"K\u0001K\u0001K\u0001K\u0001K\u0005K\u04c2\bK\nK\fK\u04c5\tK\u0003K\u04c7"+
		"\bK\u0001K\u0001K\u0001L\u0001L\u0001L\u0001L\u0001M\u0001M\u0001M\u0001"+
		"M\u0001M\u0001M\u0001M\u0003M\u04d6\bM\u0001N\u0001N\u0001N\u0005N\u04db"+
		"\bN\nN\fN\u04de\tN\u0001N\u0001N\u0001N\u0005N\u04e3\bN\nN\fN\u04e6\t"+
		"N\u0001N\u0003N\u04e9\bN\u0001O\u0001O\u0001O\u0001O\u0001O\u0001O\u0001"+
		"O\u0001O\u0001O\u0001O\u0001O\u0001O\u0001O\u0001O\u0001O\u0001O\u0001"+
		"O\u0001O\u0003O\u04fd\bO\u0001P\u0001P\u0001P\u0001P\u0001P\u0003P\u0504"+
		"\bP\u0001P\u0001P\u0001P\u0001Q\u0001Q\u0001R\u0001R\u0001R\u0001R\u0001"+
		"R\u0001R\u0001R\u0001R\u0003R\u0513\bR\u0001R\u0001R\u0001R\u0001R\u0001"+
		"R\u0003R\u051a\bR\u0001S\u0001S\u0001S\u0001S\u0001S\u0003S\u0521\bS\u0001"+
		"S\u0001S\u0005S\u0525\bS\nS\fS\u0528\tS\u0001S\u0001S\u0001S\u0001S\u0003"+
		"S\u052e\bS\u0001S\u0001S\u0001T\u0001T\u0001T\u0003T\u0535\bT\u0001T\u0001"+
		"T\u0001T\u0003T\u053a\bT\u0001U\u0001U\u0001U\u0001U\u0001U\u0001U\u0001"+
		"U\u0001V\u0001V\u0001V\u0001V\u0001V\u0001V\u0001W\u0001W\u0003W\u054b"+
		"\bW\u0001W\u0001W\u0001W\u0003W\u0550\bW\u0001W\u0003W\u0553\bW\u0001"+
		"W\u0001W\u0003W\u0557\bW\u0001W\u0001W\u0001X\u0001X\u0001X\u0001X\u0001"+
		"X\u0001X\u0001X\u0003X\u0562\bX\u0001X\u0001X\u0001Y\u0001Y\u0001Y\u0001"+
		"Y\u0003Y\u056a\bY\u0001Y\u0001Y\u0003Y\u056e\bY\u0001Y\u0001Y\u0001Z\u0001"+
		"Z\u0001Z\u0001Z\u0003Z\u0576\bZ\u0001Z\u0001Z\u0001[\u0001[\u0001\\\u0003"+
		"\\\u057d\b\\\u0001\\\u0001\\\u0001\\\u0003\\\u0582\b\\\u0001\\\u0001\\"+
		"\u0001]\u0001]\u0001^\u0003^\u0589\b^\u0001^\u0001^\u0001^\u0001^\u0001"+
		"^\u0003^\u0590\b^\u0001^\u0001^\u0001_\u0001_\u0001`\u0001`\u0001`\u0003"+
		"`\u0599\b`\u0001a\u0001a\u0001a\u0001a\u0001a\u0003a\u05a0\ba\u0001b\u0001"+
		"b\u0001b\u0004b\u05a5\bb\u000bb\fb\u05a6\u0001c\u0001c\u0001c\u0001c\u0001"+
		"c\u0003c\u05ae\bc\u0001c\u0003c\u05b1\bc\u0001c\u0003c\u05b4\bc\u0001"+
		"c\u0001c\u0001d\u0001d\u0001d\u0005d\u05bb\bd\nd\fd\u05be\td\u0001e\u0001"+
		"e\u0001e\u0001e\u0003e\u05c4\be\u0001e\u0003e\u05c7\be\u0001e\u0003e\u05ca"+
		"\be\u0001f\u0001f\u0003f\u05ce\bf\u0001f\u0001f\u0001f\u0001f\u0001f\u0003"+
		"f\u05d5\bf\u0001g\u0001g\u0001g\u0003g\u05da\bg\u0001g\u0001g\u0001g\u0003"+
		"g\u05df\bg\u0001h\u0001h\u0001h\u0003h\u05e4\bh\u0001h\u0001h\u0001h\u0003"+
		"h\u05e9\bh\u0001i\u0001i\u0003i\u05ed\bi\u0001j\u0001j\u0001j\u0001j\u0005"+
		"j\u05f3\bj\nj\fj\u05f6\tj\u0001j\u0001j\u0001j\u0001j\u0001j\u0005j\u05fd"+
		"\bj\nj\fj\u0600\tj\u0001j\u0001j\u0003j\u0604\bj\u0001k\u0001k\u0001k"+
		"\u0003k\u0609\bk\u0001k\u0001k\u0001k\u0003k\u060e\bk\u0001l\u0001l\u0001"+
		"m\u0001m\u0001m\u0005m\u0615\bm\nm\fm\u0618\tm\u0001n\u0001n\u0001n\u0001"+
		"n\u0003n\u061e\bn\u0001n\u0001n\u0001n\u0003n\u0623\bn\u0001n\u0001n\u0001"+
		"n\u0003n\u0628\bn\u0001n\u0001n\u0001n\u0003n\u062d\bn\u0001n\u0001n\u0001"+
		"n\u0001n\u0001n\u0003n\u0634\bn\u0001n\u0001n\u0001n\u0001n\u0001n\u0003"+
		"n\u063b\bn\u0001n\u0001n\u0003n\u063f\bn\u0001n\u0001n\u0003n\u0643\b"+
		"n\u0003n\u0645\bn\u0001o\u0001o\u0001o\u0001o\u0001o\u0003o\u064c\bo\u0001"+
		"o\u0001o\u0001o\u0001o\u0003o\u0652\bo\u0001o\u0001o\u0001o\u0003o\u0657"+
		"\bo\u0001o\u0001o\u0001o\u0003o\u065c\bo\u0001o\u0001o\u0001o\u0001o\u0001"+
		"o\u0003o\u0663\bo\u0001o\u0001o\u0001o\u0001o\u0001o\u0001o\u0003o\u066b"+
		"\bo\u0001o\u0001o\u0001o\u0001o\u0001o\u0001o\u0001o\u0001o\u0001o\u0001"+
		"o\u0001o\u0001o\u0001o\u0001o\u0001o\u0001o\u0003o\u067d\bo\u0001o\u0001"+
		"o\u0001o\u0001o\u0003o\u0683\bo\u0001o\u0001o\u0001o\u0001o\u0001o\u0001"+
		"o\u0001o\u0001o\u0001o\u0003o\u068e\bo\u0001o\u0001o\u0001o\u0003o\u0693"+
		"\bo\u0001p\u0001p\u0001p\u0003p\u0698\bp\u0001p\u0001p\u0001p\u0001p\u0003"+
		"p\u069e\bp\u0005p\u06a0\bp\np\fp\u06a3\tp\u0001q\u0001q\u0001q\u0003q"+
		"\u06a8\bq\u0001q\u0001q\u0001q\u0001r\u0001r\u0001r\u0001r\u0001s\u0001"+
		"s\u0001s\u0003s\u06b4\bs\u0001s\u0001s\u0001s\u0001t\u0001t\u0001t\u0001"+
		"t\u0001t\u0001t\u0001t\u0001t\u0001t\u0001t\u0001t\u0005t\u06c4\bt\nt"+
		"\ft\u06c7\tt\u0003t\u06c9\bt\u0001u\u0001u\u0001u\u0005u\u06ce\bu\nu\f"+
		"u\u06d1\tu\u0001v\u0001v\u0001v\u0005v\u06d6\bv\nv\fv\u06d9\tv\u0001w"+
		"\u0001w\u0001x\u0001x\u0001x\u0001y\u0001y\u0001y\u0001z\u0005z\u06e4"+
		"\bz\nz\fz\u06e7\tz\u0001z\u0001z\u0005z\u06eb\bz\nz\fz\u06ee\tz\u0001"+
		"{\u0001{\u0001{\u0003{\u06f3\b{\u0001{\u0001{\u0001{\u0003{\u06f8\b{\u0001"+
		"|\u0001|\u0001|\u0005|\u06fd\b|\n|\f|\u0700\t|\u0001|\u0001|\u0001}\u0001"+
		"}\u0001}\u0003}\u0707\b}\u0001~\u0001~\u0001~\u0004~\u070c\b~\u000b~\f"+
		"~\u070d\u0001\u007f\u0001\u007f\u0003\u007f\u0712\b\u007f\u0001\u0080"+
		"\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0080\u0003\u0080"+
		"\u071a\b\u0080\u0003\u0080\u071c\b\u0080\u0001\u0080\u0001\u0080\u0001"+
		"\u0080\u0001\u0081\u0003\u0081\u0722\b\u0081\u0001\u0081\u0001\u0081\u0003"+
		"\u0081\u0726\b\u0081\u0001\u0081\u0001\u0081\u0001\u0081\u0001\u0081\u0001"+
		"\u0081\u0001\u0081\u0001\u0081\u0001\u0081\u0001\u0081\u0001\u0081\u0003"+
		"\u0081\u0732\b\u0081\u0001\u0081\u0001\u0081\u0001\u0081\u0001\u0081\u0001"+
		"\u0081\u0001\u0081\u0003\u0081\u073a\b\u0081\u0001\u0082\u0001\u0082\u0001"+
		"\u0082\u0001\u0082\u0001\u0082\u0001\u0082\u0003\u0082\u0742\b\u0082\u0001"+
		"\u0082\u0001\u0082\u0001\u0082\u0001\u0082\u0004\u0082\u0748\b\u0082\u000b"+
		"\u0082\f\u0082\u0749\u0001\u0083\u0001\u0083\u0003\u0083\u074e\b\u0083"+
		"\u0001\u0083\u0001\u0083\u0001\u0083\u0001\u0083\u0003\u0083\u0754\b\u0083"+
		"\u0001\u0083\u0001\u0083\u0001\u0083\u0001\u0083\u0003\u0083\u075a\b\u0083"+
		"\u0001\u0083\u0001\u0083\u0003\u0083\u075e\b\u0083\u0001\u0084\u0001\u0084"+
		"\u0001\u0084\u0001\u0084\u0001\u0084\u0001\u0084\u0001\u0084\u0004\u0084"+
		"\u0767\b\u0084\u000b\u0084\f\u0084\u0768\u0001\u0085\u0001\u0085\u0003"+
		"\u0085\u076d\b\u0085\u0001\u0085\u0001\u0085\u0001\u0085\u0001\u0085\u0003"+
		"\u0085\u0773\b\u0085\u0001\u0085\u0001\u0085\u0003\u0085\u0777\b\u0085"+
		"\u0001\u0086\u0001\u0086\u0001\u0086\u0005\u0086\u077c\b\u0086\n\u0086"+
		"\f\u0086\u077f\t\u0086\u0001\u0087\u0001\u0087\u0001\u0087\u0001\u0087"+
		"\u0001\u0088\u0001\u0088\u0001\u0088\u0005\u0088\u0788\b\u0088\n\u0088"+
		"\f\u0088\u078b\t\u0088\u0001\u0089\u0001\u0089\u0001\u0089\u0001\u008a"+
		"\u0001\u008a\u0001\u008a\u0001\u008a\u0001\u008a\u0005\u008a\u0795\b\u008a"+
		"\n\u008a\f\u008a\u0798\t\u008a\u0003\u008a\u079a\b\u008a\u0001\u008a\u0001"+
		"\u008a\u0001\u008b\u0001\u008b\u0001\u008b\u0003\u008b\u07a1\b\u008b\u0001"+
		"\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0005\u008c\u07a8"+
		"\b\u008c\n\u008c\f\u008c\u07ab\t\u008c\u0001\u008c\u0001\u008c\u0001\u008d"+
		"\u0001\u008d\u0001\u008d\u0001\u008d\u0001\u008d\u0005\u008d\u07b4\b\u008d"+
		"\n\u008d\f\u008d\u07b7\t\u008d\u0003\u008d\u07b9\b\u008d\u0001\u008d\u0001"+
		"\u008d\u0001\u008e\u0001\u008e\u0001\u008e\u0001\u008e\u0001\u008f\u0001"+
		"\u008f\u0001\u0090\u0001\u0090\u0001\u0090\u0001\u0090\u0001\u0090\u0001"+
		"\u0091\u0001\u0091\u0001\u0091\u0001\u0091\u0001\u0091\u0001\u0091\u0001"+
		"\u0091\u0001\u0092\u0001\u0092\u0001\u0092\u0001\u0092\u0001\u0092\u0005"+
		"\u0092\u07d4\b\u0092\n\u0092\f\u0092\u07d7\t\u0092\u0001\u0092\u0001\u0092"+
		"\u0001\u0092\u0000\u0001\u0086\u0093\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDF"+
		"HJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c"+
		"\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4"+
		"\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc"+
		"\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4"+
		"\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec"+
		"\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104"+
		"\u0106\u0108\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a\u011c"+
		"\u011e\u0120\u0122\u0124\u0000\u001c\u0001\u0000\u0011\u0013\u0002\u0000"+
		"\r\r\u000f\u000f\u0001\u0000\u0015\u0016\u0001\u0000\u0017\u0019\u0001"+
		"\u0000\u001b\u001d\u0001\u000001\u0002\u0000\u0004\u0004\u00ab\u00ab\u0002"+
		"\u0000\u00a9\u00a9\u00ac\u00ac\u0001\u0000\u009f\u00a2\u0002\u0000\u0004"+
		"\u0004HI\u0002\u0000GGJJ\u0001\u0000KN\u0003\u0000##OO\u00a5\u00a5\u0001"+
		"\u0000@A\u0002\u0000BC\u00a6\u00a6\u0001\u0000YZ\u0002\u0000``bf\u0002"+
		"\u0000\u0004\u0004kk\u0001\u0000no\u0001\u0000uv\u0001\u0000x{\u0001\u0000"+
		"|\u007f\u0001\u0000\u0081\u0082\u0003\u0000##\u009f\u009f\u00a1\u00a1"+
		"\u000e\u0000\u0003\u0003\u0006\u0007\n\n\r\r\u000f\u000f\u0015\"$*,6:"+
		":<<>?^_k\u0089\u00ad\u00ad\u0003\u0000\u0001\u0001\u000f\u000f\u008a\u008a"+
		"\u0002\u0000??\u0094\u0094\u0001\u0000\u0095\u009d\u089d\u0000\u0126\u0001"+
		"\u0000\u0000\u0000\u0002\u012c\u0001\u0000\u0000\u0000\u0004\u0135\u0001"+
		"\u0000\u0000\u0000\u0006\u0141\u0001\u0000\u0000\u0000\b\u014c\u0001\u0000"+
		"\u0000\u0000\n\u014e\u0001\u0000\u0000\u0000\f\u015a\u0001\u0000\u0000"+
		"\u0000\u000e\u0169\u0001\u0000\u0000\u0000\u0010\u016b\u0001\u0000\u0000"+
		"\u0000\u0012\u017a\u0001\u0000\u0000\u0000\u0014\u019b\u0001\u0000\u0000"+
		"\u0000\u0016\u019e\u0001\u0000\u0000\u0000\u0018\u01b4\u0001\u0000\u0000"+
		"\u0000\u001a\u01c4\u0001\u0000\u0000\u0000\u001c\u01c6\u0001\u0000\u0000"+
		"\u0000\u001e\u01c8\u0001\u0000\u0000\u0000 \u01cd\u0001\u0000\u0000\u0000"+
		"\"\u01d3\u0001\u0000\u0000\u0000$\u01d5\u0001\u0000\u0000\u0000&\u01dd"+
		"\u0001\u0000\u0000\u0000(\u01e1\u0001\u0000\u0000\u0000*\u01e8\u0001\u0000"+
		"\u0000\u0000,\u01fd\u0001\u0000\u0000\u0000.\u022f\u0001\u0000\u0000\u0000"+
		"0\u0231\u0001\u0000\u0000\u00002\u023a\u0001\u0000\u0000\u00004\u0247"+
		"\u0001\u0000\u0000\u00006\u0249\u0001\u0000\u0000\u00008\u0252\u0001\u0000"+
		"\u0000\u0000:\u025d\u0001\u0000\u0000\u0000<\u025f\u0001\u0000\u0000\u0000"+
		">\u0261\u0001\u0000\u0000\u0000@\u0264\u0001\u0000\u0000\u0000B\u0267"+
		"\u0001\u0000\u0000\u0000D\u0275\u0001\u0000\u0000\u0000F\u0282\u0001\u0000"+
		"\u0000\u0000H\u0285\u0001\u0000\u0000\u0000J\u0288\u0001\u0000\u0000\u0000"+
		"L\u028e\u0001\u0000\u0000\u0000N\u02a3\u0001\u0000\u0000\u0000P\u02b6"+
		"\u0001\u0000\u0000\u0000R\u02d1\u0001\u0000\u0000\u0000T\u02d3\u0001\u0000"+
		"\u0000\u0000V\u02ff\u0001\u0000\u0000\u0000X\u0301\u0001\u0000\u0000\u0000"+
		"Z\u0313\u0001\u0000\u0000\u0000\\\u0324\u0001\u0000\u0000\u0000^\u0331"+
		"\u0001\u0000\u0000\u0000`\u0335\u0001\u0000\u0000\u0000b\u033f\u0001\u0000"+
		"\u0000\u0000d\u034a\u0001\u0000\u0000\u0000f\u034c\u0001\u0000\u0000\u0000"+
		"h\u0361\u0001\u0000\u0000\u0000j\u0363\u0001\u0000\u0000\u0000l\u038f"+
		"\u0001\u0000\u0000\u0000n\u0397\u0001\u0000\u0000\u0000p\u0399\u0001\u0000"+
		"\u0000\u0000r\u03a4\u0001\u0000\u0000\u0000t\u03ad\u0001\u0000\u0000\u0000"+
		"v\u03af\u0001\u0000\u0000\u0000x\u03b7\u0001\u0000\u0000\u0000z\u03be"+
		"\u0001\u0000\u0000\u0000|\u03c6\u0001\u0000\u0000\u0000~\u03d1\u0001\u0000"+
		"\u0000\u0000\u0080\u03da\u0001\u0000\u0000\u0000\u0082\u03dc\u0001\u0000"+
		"\u0000\u0000\u0084\u03de\u0001\u0000\u0000\u0000\u0086\u03f3\u0001\u0000"+
		"\u0000\u0000\u0088\u0490\u0001\u0000\u0000\u0000\u008a\u0499\u0001\u0000"+
		"\u0000\u0000\u008c\u049b\u0001\u0000\u0000\u0000\u008e\u04a0\u0001\u0000"+
		"\u0000\u0000\u0090\u04a7\u0001\u0000\u0000\u0000\u0092\u04b5\u0001\u0000"+
		"\u0000\u0000\u0094\u04b8\u0001\u0000\u0000\u0000\u0096\u04bc\u0001\u0000"+
		"\u0000\u0000\u0098\u04ca\u0001\u0000\u0000\u0000\u009a\u04d5\u0001\u0000"+
		"\u0000\u0000\u009c\u04e8\u0001\u0000\u0000\u0000\u009e\u04fc\u0001\u0000"+
		"\u0000\u0000\u00a0\u04fe\u0001\u0000\u0000\u0000\u00a2\u0508\u0001\u0000"+
		"\u0000\u0000\u00a4\u0519\u0001\u0000\u0000\u0000\u00a6\u051b\u0001\u0000"+
		"\u0000\u0000\u00a8\u0531\u0001\u0000\u0000\u0000\u00aa\u053b\u0001\u0000"+
		"\u0000\u0000\u00ac\u0542\u0001\u0000\u0000\u0000\u00ae\u0548\u0001\u0000"+
		"\u0000\u0000\u00b0\u055a\u0001\u0000\u0000\u0000\u00b2\u0565\u0001\u0000"+
		"\u0000\u0000\u00b4\u0571\u0001\u0000\u0000\u0000\u00b6\u0579\u0001\u0000"+
		"\u0000\u0000\u00b8\u057c\u0001\u0000\u0000\u0000\u00ba\u0585\u0001\u0000"+
		"\u0000\u0000\u00bc\u0588\u0001\u0000\u0000\u0000\u00be\u0593\u0001\u0000"+
		"\u0000\u0000\u00c0\u0595\u0001\u0000\u0000\u0000\u00c2\u059f\u0001\u0000"+
		"\u0000\u0000\u00c4\u05a1\u0001\u0000\u0000\u0000\u00c6\u05a8\u0001\u0000"+
		"\u0000\u0000\u00c8\u05b7\u0001\u0000\u0000\u0000\u00ca\u05bf\u0001\u0000"+
		"\u0000\u0000\u00cc\u05cb\u0001\u0000\u0000\u0000\u00ce\u05d6\u0001\u0000"+
		"\u0000\u0000\u00d0\u05e0\u0001\u0000\u0000\u0000\u00d2\u05ea\u0001\u0000"+
		"\u0000\u0000\u00d4\u0603\u0001\u0000\u0000\u0000\u00d6\u0605\u0001\u0000"+
		"\u0000\u0000\u00d8\u060f\u0001\u0000\u0000\u0000\u00da\u0611\u0001\u0000"+
		"\u0000\u0000\u00dc\u0644\u0001\u0000\u0000\u0000\u00de\u0692\u0001\u0000"+
		"\u0000\u0000\u00e0\u0694\u0001\u0000\u0000\u0000\u00e2\u06a4\u0001\u0000"+
		"\u0000\u0000\u00e4\u06ac\u0001\u0000\u0000\u0000\u00e6\u06b0\u0001\u0000"+
		"\u0000\u0000\u00e8\u06c8\u0001\u0000\u0000\u0000\u00ea\u06ca\u0001\u0000"+
		"\u0000\u0000\u00ec\u06d2\u0001\u0000\u0000\u0000\u00ee\u06da\u0001\u0000"+
		"\u0000\u0000\u00f0\u06dc\u0001\u0000\u0000\u0000\u00f2\u06df\u0001\u0000"+
		"\u0000\u0000\u00f4\u06e5\u0001\u0000\u0000\u0000\u00f6\u06ef\u0001\u0000"+
		"\u0000\u0000\u00f8\u06f9\u0001\u0000\u0000\u0000\u00fa\u0706\u0001\u0000"+
		"\u0000\u0000\u00fc\u0708\u0001\u0000\u0000\u0000\u00fe\u0711\u0001\u0000"+
		"\u0000\u0000\u0100\u0713\u0001\u0000\u0000\u0000\u0102\u0721\u0001\u0000"+
		"\u0000\u0000\u0104\u073b\u0001\u0000\u0000\u0000\u0106\u075d\u0001\u0000"+
		"\u0000\u0000\u0108\u075f\u0001\u0000\u0000\u0000\u010a\u0776\u0001\u0000"+
		"\u0000\u0000\u010c\u0778\u0001\u0000\u0000\u0000\u010e\u0780\u0001\u0000"+
		"\u0000\u0000\u0110\u0784\u0001\u0000\u0000\u0000\u0112\u078c\u0001\u0000"+
		"\u0000\u0000\u0114\u078f\u0001\u0000\u0000\u0000\u0116\u079d\u0001\u0000"+
		"\u0000\u0000\u0118\u07a2\u0001\u0000\u0000\u0000\u011a\u07ae\u0001\u0000"+
		"\u0000\u0000\u011c\u07bc\u0001\u0000\u0000\u0000\u011e\u07c0\u0001\u0000"+
		"\u0000\u0000\u0120\u07c2\u0001\u0000\u0000\u0000\u0122\u07c7\u0001\u0000"+
		"\u0000\u0000\u0124\u07ce\u0001\u0000\u0000\u0000\u0126\u0127\u0003\u0002"+
		"\u0001\u0000\u0127\u0128\u0005\u0000\u0000\u0001\u0128\u0001\u0001\u0000"+
		"\u0000\u0000\u0129\u012b\u0003\u0004\u0002\u0000\u012a\u0129\u0001\u0000"+
		"\u0000\u0000\u012b\u012e\u0001\u0000\u0000\u0000\u012c\u012a\u0001\u0000"+
		"\u0000\u0000\u012c\u012d\u0001\u0000\u0000\u0000\u012d\u0003\u0001\u0000"+
		"\u0000\u0000\u012e\u012c\u0001\u0000\u0000\u0000\u012f\u0136\u0003\u0006"+
		"\u0003\u0000\u0130\u0136\u0003\n\u0005\u0000\u0131\u0136\u0003\u0012\t"+
		"\u0000\u0132\u0136\u0003\u0016\u000b\u0000\u0133\u0136\u0003(\u0014\u0000"+
		"\u0134\u0136\u0003,\u0016\u0000\u0135\u012f\u0001\u0000\u0000\u0000\u0135"+
		"\u0130\u0001\u0000\u0000\u0000\u0135\u0131\u0001\u0000\u0000\u0000\u0135"+
		"\u0132\u0001\u0000\u0000\u0000\u0135\u0133\u0001\u0000\u0000\u0000\u0135"+
		"\u0134\u0001\u0000\u0000\u0000\u0136\u0005\u0001\u0000\u0000\u0000\u0137"+
		"\u0138\u0005\u0001\u0000\u0000\u0138\u0139\u0003\u00ecv\u0000\u0139\u013a"+
		"\u0005\u0002\u0000\u0000\u013a\u0142\u0001\u0000\u0000\u0000\u013b\u013c"+
		"\u0005\u0003\u0000\u0000\u013c\u013d\u0003\u00ecv\u0000\u013d\u013e\u0005"+
		"\u0001\u0000\u0000\u013e\u013f\u0003\b\u0004\u0000\u013f\u0140\u0005\u0002"+
		"\u0000\u0000\u0140\u0142\u0001\u0000\u0000\u0000\u0141\u0137\u0001\u0000"+
		"\u0000\u0000\u0141\u013b\u0001\u0000\u0000\u0000\u0142\u0007\u0001\u0000"+
		"\u0000\u0000\u0143\u014d\u0005\u0004\u0000\u0000\u0144\u0149\u0003\u00ee"+
		"w\u0000\u0145\u0146\u0005\u0005\u0000\u0000\u0146\u0148\u0003\u00eew\u0000"+
		"\u0147\u0145\u0001\u0000\u0000\u0000\u0148\u014b\u0001\u0000\u0000\u0000"+
		"\u0149\u0147\u0001\u0000\u0000\u0000\u0149\u014a\u0001\u0000\u0000\u0000"+
		"\u014a\u014d\u0001\u0000\u0000\u0000\u014b\u0149\u0001\u0000\u0000\u0000"+
		"\u014c\u0143\u0001\u0000\u0000\u0000\u014c\u0144\u0001\u0000\u0000\u0000"+
		"\u014d\t\u0001\u0000\u0000\u0000\u014e\u014f\u0005\u0006\u0000\u0000\u014f"+
		"\u0151\u0003\u00eew\u0000\u0150\u0152\u0005\u00ac\u0000\u0000\u0151\u0150"+
		"\u0001\u0000\u0000\u0000\u0151\u0152\u0001\u0000\u0000\u0000\u0152\u0153"+
		"\u0001\u0000\u0000\u0000\u0153\u0154\u0005\u0007\u0000\u0000\u0154\u0156"+
		"\u0003\f\u0006\u0000\u0155\u0157\u0003\u0010\b\u0000\u0156\u0155\u0001"+
		"\u0000\u0000\u0000\u0156\u0157\u0001\u0000\u0000\u0000\u0157\u0158\u0001"+
		"\u0000\u0000\u0000\u0158\u0159\u0005\u0002\u0000\u0000\u0159\u000b\u0001"+
		"\u0000\u0000\u0000\u015a\u015f\u0003\u000e\u0007\u0000\u015b\u015c\u0005"+
		"\u0005\u0000\u0000\u015c\u015e\u0003\u000e\u0007\u0000\u015d\u015b\u0001"+
		"\u0000\u0000\u0000\u015e\u0161\u0001\u0000\u0000\u0000\u015f\u015d\u0001"+
		"\u0000\u0000\u0000\u015f\u0160\u0001\u0000\u0000\u0000\u0160\r\u0001\u0000"+
		"\u0000\u0000\u0161\u015f\u0001\u0000\u0000\u0000\u0162\u0166\u0003\u00da"+
		"m\u0000\u0163\u0164\u0005\b\u0000\u0000\u0164\u0165\u0005\u00ac\u0000"+
		"\u0000\u0165\u0167\u0005\t\u0000\u0000\u0166\u0163\u0001\u0000\u0000\u0000"+
		"\u0166\u0167\u0001\u0000\u0000\u0000\u0167\u016a\u0001\u0000\u0000\u0000"+
		"\u0168\u016a\u0005\u00ac\u0000\u0000\u0169\u0162\u0001\u0000\u0000\u0000"+
		"\u0169\u0168\u0001\u0000\u0000\u0000\u016a\u000f\u0001\u0000\u0000\u0000"+
		"\u016b\u016c\u0005\n\u0000\u0000\u016c\u016d\u0005\u000b\u0000\u0000\u016d"+
		"\u0172\u0003\u0086C\u0000\u016e\u016f\u0005\u0005\u0000\u0000\u016f\u0171"+
		"\u0003\u0086C\u0000\u0170\u016e\u0001\u0000\u0000\u0000\u0171\u0174\u0001"+
		"\u0000\u0000\u0000\u0172\u0170\u0001\u0000\u0000\u0000\u0172\u0173\u0001"+
		"\u0000\u0000\u0000\u0173\u0175\u0001\u0000\u0000\u0000\u0174\u0172\u0001"+
		"\u0000\u0000\u0000\u0175\u0176\u0005\f\u0000\u0000\u0176\u0011\u0001\u0000"+
		"\u0000\u0000\u0177\u0179\u0003*\u0015\u0000\u0178\u0177\u0001\u0000\u0000"+
		"\u0000\u0179\u017c\u0001\u0000\u0000\u0000\u017a\u0178\u0001\u0000\u0000"+
		"\u0000\u017a\u017b\u0001\u0000\u0000\u0000\u017b\u017d\u0001\u0000\u0000"+
		"\u0000\u017c\u017a\u0001\u0000\u0000\u0000\u017d\u017e\u0005\r\u0000\u0000"+
		"\u017e\u017f\u0003\u00ecv\u0000\u017f\u0181\u0005\b\u0000\u0000\u0180"+
		"\u0182\u0003\u0018\f\u0000\u0181\u0180\u0001\u0000\u0000\u0000\u0181\u0182"+
		"\u0001\u0000\u0000\u0000\u0182\u0183\u0001\u0000\u0000\u0000\u0183\u018b"+
		"\u0005\t\u0000\u0000\u0184\u0186\u0003 \u0010\u0000\u0185\u0184\u0001"+
		"\u0000\u0000\u0000\u0186\u0189\u0001\u0000\u0000\u0000\u0187\u0185\u0001"+
		"\u0000\u0000\u0000\u0187\u0188\u0001\u0000\u0000\u0000\u0188\u018c\u0001"+
		"\u0000\u0000\u0000\u0189\u0187\u0001\u0000\u0000\u0000\u018a\u018c\u0003"+
		"\u0014\n\u0000\u018b\u0187\u0001\u0000\u0000\u0000\u018b\u018a\u0001\u0000"+
		"\u0000\u0000\u018c\u0199\u0001\u0000\u0000\u0000\u018d\u0191\u0005\u000b"+
		"\u0000\u0000\u018e\u0190\u0003,\u0016\u0000\u018f\u018e\u0001\u0000\u0000"+
		"\u0000\u0190\u0193\u0001\u0000\u0000\u0000\u0191\u018f\u0001\u0000\u0000"+
		"\u0000\u0191\u0192\u0001\u0000\u0000\u0000\u0192\u0194\u0001\u0000\u0000"+
		"\u0000\u0193\u0191\u0001\u0000\u0000\u0000\u0194\u0196\u0005\f\u0000\u0000"+
		"\u0195\u0197\u0005\u0002\u0000\u0000\u0196\u0195\u0001\u0000\u0000\u0000"+
		"\u0196\u0197\u0001\u0000\u0000\u0000\u0197\u019a\u0001\u0000\u0000\u0000"+
		"\u0198\u019a\u0005\u0002\u0000\u0000\u0199\u018d\u0001\u0000\u0000\u0000"+
		"\u0199\u0198\u0001\u0000\u0000\u0000\u019a\u0013\u0001\u0000\u0000\u0000"+
		"\u019b\u019c\u0005\u000e\u0000\u0000\u019c\u019d\u0003&\u0013\u0000\u019d"+
		"\u0015\u0001\u0000\u0000\u0000\u019e\u019f\u0005\u000f\u0000\u0000\u019f"+
		"\u01a1\u0003\u00ecv\u0000\u01a0\u01a2\u0003T*\u0000\u01a1\u01a0\u0001"+
		"\u0000\u0000\u0000\u01a1\u01a2\u0001\u0000\u0000\u0000\u01a2\u01a6\u0001"+
		"\u0000\u0000\u0000\u01a3\u01a5\u0003 \u0010\u0000\u01a4\u01a3\u0001\u0000"+
		"\u0000\u0000\u01a5\u01a8\u0001\u0000\u0000\u0000\u01a6\u01a4\u0001\u0000"+
		"\u0000\u0000\u01a6\u01a7\u0001\u0000\u0000\u0000\u01a7\u01a9\u0001\u0000"+
		"\u0000\u0000\u01a8\u01a6\u0001\u0000\u0000\u0000\u01a9\u01ad\u0005\u000b"+
		"\u0000\u0000\u01aa\u01ac\u0003,\u0016\u0000\u01ab\u01aa\u0001\u0000\u0000"+
		"\u0000\u01ac\u01af\u0001\u0000\u0000\u0000\u01ad\u01ab\u0001\u0000\u0000"+
		"\u0000\u01ad\u01ae\u0001\u0000\u0000\u0000\u01ae\u01b0\u0001\u0000\u0000"+
		"\u0000\u01af\u01ad\u0001\u0000\u0000\u0000\u01b0\u01b2\u0005\f\u0000\u0000"+
		"\u01b1\u01b3\u0005\u0002\u0000\u0000\u01b2\u01b1\u0001\u0000\u0000\u0000"+
		"\u01b2\u01b3\u0001\u0000\u0000\u0000\u01b3\u0017\u0001\u0000\u0000\u0000"+
		"\u01b4\u01b9\u0003\u001a\r\u0000\u01b5\u01b6\u0005\u0005\u0000\u0000\u01b6"+
		"\u01b8\u0003\u001a\r\u0000\u01b7\u01b5\u0001\u0000\u0000\u0000\u01b8\u01bb"+
		"\u0001\u0000\u0000\u0000\u01b9\u01b7\u0001\u0000\u0000\u0000\u01b9\u01ba"+
		"\u0001\u0000\u0000\u0000\u01ba\u0019\u0001\u0000\u0000\u0000\u01bb\u01b9"+
		"\u0001\u0000\u0000\u0000\u01bc\u01bd\u0003\u001c\u000e\u0000\u01bd\u01be"+
		"\u0003\u00eew\u0000\u01be\u01bf\u0005\u0010\u0000\u0000\u01bf\u01c0\u0003"+
		"\u001e\u000f\u0000\u01c0\u01c5\u0001\u0000\u0000\u0000\u01c1\u01c2\u0003"+
		"\u001c\u000e\u0000\u01c2\u01c3\u0003\u001e\u000f\u0000\u01c3\u01c5\u0001"+
		"\u0000\u0000\u0000\u01c4\u01bc\u0001\u0000\u0000\u0000\u01c4\u01c1\u0001"+
		"\u0000\u0000\u0000\u01c5\u001b\u0001\u0000\u0000\u0000\u01c6\u01c7\u0007"+
		"\u0000\u0000\u0000\u01c7\u001d\u0001\u0000\u0000\u0000\u01c8\u01cb\u0003"+
		"\u008aE\u0000\u01c9\u01ca\u0005\u0014\u0000\u0000\u01ca\u01cc\u0003\u00ee"+
		"w\u0000\u01cb\u01c9\u0001\u0000\u0000\u0000\u01cb\u01cc\u0001\u0000\u0000"+
		"\u0000\u01cc\u001f\u0001\u0000\u0000\u0000\u01cd\u01cf\u0003\"\u0011\u0000"+
		"\u01ce\u01d0\u0007\u0001\u0000\u0000\u01cf\u01ce\u0001\u0000\u0000\u0000"+
		"\u01cf\u01d0\u0001\u0000\u0000\u0000\u01d0\u01d1\u0001\u0000\u0000\u0000"+
		"\u01d1\u01d2\u0003$\u0012\u0000\u01d2!\u0001\u0000\u0000\u0000\u01d3\u01d4"+
		"\u0007\u0002\u0000\u0000\u01d4#\u0001\u0000\u0000\u0000\u01d5\u01da\u0003"+
		"&\u0013\u0000\u01d6\u01d7\u0005\u0005\u0000\u0000\u01d7\u01d9\u0003&\u0013"+
		"\u0000\u01d8\u01d6\u0001\u0000\u0000\u0000\u01d9\u01dc\u0001\u0000\u0000"+
		"\u0000\u01da\u01d8\u0001\u0000\u0000\u0000\u01da\u01db\u0001\u0000\u0000"+
		"\u0000\u01db%\u0001\u0000\u0000\u0000\u01dc\u01da\u0001\u0000\u0000\u0000"+
		"\u01dd\u01df\u0003\u00ecv\u0000\u01de\u01e0\u0003T*\u0000\u01df\u01de"+
		"\u0001\u0000\u0000\u0000\u01df\u01e0\u0001\u0000\u0000\u0000\u01e0\'\u0001"+
		"\u0000\u0000\u0000\u01e1\u01e3\u0005\u0015\u0000\u0000\u01e2\u01e4\u0007"+
		"\u0001\u0000\u0000\u01e3\u01e2\u0001\u0000\u0000\u0000\u01e3\u01e4\u0001"+
		"\u0000\u0000\u0000\u01e4\u01e5\u0001\u0000\u0000\u0000\u01e5\u01e6\u0003"+
		"$\u0012\u0000\u01e6\u01e7\u0005\u0002\u0000\u0000\u01e7)\u0001\u0000\u0000"+
		"\u0000\u01e8\u01e9\u0007\u0003\u0000\u0000\u01e9+\u0001\u0000\u0000\u0000"+
		"\u01ea\u01fe\u0003.\u0017\u0000\u01eb\u01fe\u0003L&\u0000\u01ec\u01fe"+
		"\u0003N\'\u0000\u01ed\u01fe\u0003P(\u0000\u01ee\u01fe\u0003R)\u0000\u01ef"+
		"\u01fe\u0003V+\u0000\u01f0\u01fe\u0003X,\u0000\u01f1\u01fe\u0003Z-\u0000"+
		"\u01f2\u01fe\u0003\\.\u0000\u01f3\u01fe\u0003^/\u0000\u01f4\u01fe\u0003"+
		"`0\u0000\u01f5\u01fe\u0003f3\u0000\u01f6\u01f7\u0003x<\u0000\u01f7\u01f8"+
		"\u0005\u0002\u0000\u0000\u01f8\u01fe\u0001\u0000\u0000\u0000\u01f9\u01fa"+
		"\u0003|>\u0000\u01fa\u01fb\u0005\u0002\u0000\u0000\u01fb\u01fe\u0001\u0000"+
		"\u0000\u0000\u01fc\u01fe\u0003(\u0014\u0000\u01fd\u01ea\u0001\u0000\u0000"+
		"\u0000\u01fd\u01eb\u0001\u0000\u0000\u0000\u01fd\u01ec\u0001\u0000\u0000"+
		"\u0000\u01fd\u01ed\u0001\u0000\u0000\u0000\u01fd\u01ee\u0001\u0000\u0000"+
		"\u0000\u01fd\u01ef\u0001\u0000\u0000\u0000\u01fd\u01f0\u0001\u0000\u0000"+
		"\u0000\u01fd\u01f1\u0001\u0000\u0000\u0000\u01fd\u01f2\u0001\u0000\u0000"+
		"\u0000\u01fd\u01f3\u0001\u0000\u0000\u0000\u01fd\u01f4\u0001\u0000\u0000"+
		"\u0000\u01fd\u01f5\u0001\u0000\u0000\u0000\u01fd\u01f6\u0001\u0000\u0000"+
		"\u0000\u01fd\u01f9\u0001\u0000\u0000\u0000\u01fd\u01fc\u0001\u0000\u0000"+
		"\u0000\u01fe-\u0001\u0000\u0000\u0000\u01ff\u0201\u0003*\u0015\u0000\u0200"+
		"\u01ff\u0001\u0000\u0000\u0000\u0201\u0204\u0001\u0000\u0000\u0000\u0202"+
		"\u0200\u0001\u0000\u0000\u0000\u0202\u0203\u0001\u0000\u0000\u0000\u0203"+
		"\u0205\u0001\u0000\u0000\u0000\u0204\u0202\u0001\u0000\u0000\u0000\u0205"+
		"\u0207\u0005\u001a\u0000\u0000\u0206\u0208\u0003\u001c\u000e\u0000\u0207"+
		"\u0206\u0001\u0000\u0000\u0000\u0207\u0208\u0001\u0000\u0000\u0000\u0208"+
		"\u0209\u0001\u0000\u0000\u0000\u0209\u020a\u0003\u00e8t\u0000\u020a\u020e"+
		"\u00030\u0018\u0000\u020b\u020d\u0003:\u001d\u0000\u020c\u020b\u0001\u0000"+
		"\u0000\u0000\u020d\u0210\u0001\u0000\u0000\u0000\u020e\u020c\u0001\u0000"+
		"\u0000\u0000\u020e\u020f\u0001\u0000\u0000\u0000\u020f\u0212\u0001\u0000"+
		"\u0000\u0000\u0210\u020e\u0001\u0000\u0000\u0000\u0211\u0213\u0003>\u001f"+
		"\u0000\u0212\u0211\u0001\u0000\u0000\u0000\u0212\u0213\u0001\u0000\u0000"+
		"\u0000\u0213\u0215\u0001\u0000\u0000\u0000\u0214\u0216\u0003@ \u0000\u0215"+
		"\u0214\u0001\u0000\u0000\u0000\u0215\u0216\u0001\u0000\u0000\u0000\u0216"+
		"\u0217\u0001\u0000\u0000\u0000\u0217\u0219\u0003D\"\u0000\u0218\u021a"+
		"\u0005\u0002\u0000\u0000\u0219\u0218\u0001\u0000\u0000\u0000\u0219\u021a"+
		"\u0001\u0000\u0000\u0000\u021a\u0230\u0001\u0000\u0000\u0000\u021b\u021d"+
		"\u0003*\u0015\u0000\u021c\u021b\u0001\u0000\u0000\u0000\u021d\u0220\u0001"+
		"\u0000\u0000\u0000\u021e\u021c\u0001\u0000\u0000\u0000\u021e\u021f\u0001"+
		"\u0000\u0000\u0000\u021f\u0221\u0001\u0000\u0000\u0000\u0220\u021e\u0001"+
		"\u0000\u0000\u0000\u0221\u0223\u0005\u001a\u0000\u0000\u0222\u0224\u0003"+
		"\u001c\u000e\u0000\u0223\u0222\u0001\u0000\u0000\u0000\u0223\u0224\u0001"+
		"\u0000\u0000\u0000\u0224\u0225\u0001\u0000\u0000\u0000\u0225\u0226\u0003"+
		"\u00e8t\u0000\u0226\u022a\u00030\u0018\u0000\u0227\u0229\u0003:\u001d"+
		"\u0000\u0228\u0227\u0001\u0000\u0000\u0000\u0229\u022c\u0001\u0000\u0000"+
		"\u0000\u022a\u0228\u0001\u0000\u0000\u0000\u022a\u022b\u0001\u0000\u0000"+
		"\u0000\u022b\u022d\u0001\u0000\u0000\u0000\u022c\u022a\u0001\u0000\u0000"+
		"\u0000\u022d\u022e\u0005\u0002\u0000\u0000\u022e\u0230\u0001\u0000\u0000"+
		"\u0000\u022f\u0202\u0001\u0000\u0000\u0000\u022f\u021e\u0001\u0000\u0000"+
		"\u0000\u0230/\u0001\u0000\u0000\u0000\u0231\u0233\u0005\b\u0000\u0000"+
		"\u0232\u0234\u00036\u001b\u0000\u0233\u0232\u0001\u0000\u0000\u0000\u0233"+
		"\u0234\u0001\u0000\u0000\u0000\u0234\u0235\u0001\u0000\u0000\u0000\u0235"+
		"\u0238\u0005\t\u0000\u0000\u0236\u0237\u0005\u0010\u0000\u0000\u0237\u0239"+
		"\u00032\u0019\u0000\u0238\u0236\u0001\u0000\u0000\u0000\u0238\u0239\u0001"+
		"\u0000\u0000\u0000\u02391\u0001\u0000\u0000\u0000\u023a\u023f\u00034\u001a"+
		"\u0000\u023b\u023c\u0005\u0005\u0000\u0000\u023c\u023e\u00034\u001a\u0000"+
		"\u023d\u023b\u0001\u0000\u0000\u0000\u023e\u0241\u0001\u0000\u0000\u0000"+
		"\u023f\u023d\u0001\u0000\u0000\u0000\u023f\u0240\u0001\u0000\u0000\u0000"+
		"\u02403\u0001\u0000\u0000\u0000\u0241\u023f\u0001\u0000\u0000\u0000\u0242"+
		"\u0243\u0003\u00eew\u0000\u0243\u0244\u0005\u0010\u0000\u0000\u0244\u0245"+
		"\u0003\u008aE\u0000\u0245\u0248\u0001\u0000\u0000\u0000\u0246\u0248\u0003"+
		"\u008aE\u0000\u0247\u0242\u0001\u0000\u0000\u0000\u0247\u0246\u0001\u0000"+
		"\u0000\u0000\u02485\u0001\u0000\u0000\u0000\u0249\u024e\u00038\u001c\u0000"+
		"\u024a\u024b\u0005\u0005\u0000\u0000\u024b\u024d\u00038\u001c\u0000\u024c"+
		"\u024a\u0001\u0000\u0000\u0000\u024d\u0250\u0001\u0000\u0000\u0000\u024e"+
		"\u024c\u0001\u0000\u0000\u0000\u024e\u024f\u0001\u0000\u0000\u0000\u024f"+
		"7\u0001\u0000\u0000\u0000\u0250\u024e\u0001\u0000\u0000\u0000\u0251\u0253"+
		"\u0003\u001c\u000e\u0000\u0252\u0251\u0001\u0000\u0000\u0000\u0252\u0253"+
		"\u0001\u0000\u0000\u0000\u0253\u0254\u0001\u0000\u0000\u0000\u0254\u0255"+
		"\u0003\u00eew\u0000\u0255\u0256\u0005\u0010\u0000\u0000\u0256\u0257\u0003"+
		"\u008aE\u0000\u02579\u0001\u0000\u0000\u0000\u0258\u0259\u0003<\u001e"+
		"\u0000\u0259\u025a\u0003\u00eau\u0000\u025a\u025e\u0001\u0000\u0000\u0000"+
		"\u025b\u025c\u0005\u000e\u0000\u0000\u025c\u025e\u0003\u00e8t\u0000\u025d"+
		"\u0258\u0001\u0000\u0000\u0000\u025d\u025b\u0001\u0000\u0000\u0000\u025e"+
		";\u0001\u0000\u0000\u0000\u025f\u0260\u0007\u0004\u0000\u0000\u0260=\u0001"+
		"\u0000\u0000\u0000\u0261\u0262\u0005\u001e\u0000\u0000\u0262\u0263\u0003"+
		"B!\u0000\u0263?\u0001\u0000\u0000\u0000\u0264\u0265\u0005\n\u0000\u0000"+
		"\u0265\u0266\u0003B!\u0000\u0266A\u0001\u0000\u0000\u0000\u0267\u0268"+
		"\u0005\u000b\u0000\u0000\u0268\u026d\u0003\u0086C\u0000\u0269\u026a\u0005"+
		"\u0002\u0000\u0000\u026a\u026c\u0003\u0086C\u0000\u026b\u0269\u0001\u0000"+
		"\u0000\u0000\u026c\u026f\u0001\u0000\u0000\u0000\u026d\u026b\u0001\u0000"+
		"\u0000\u0000\u026d\u026e\u0001\u0000\u0000\u0000\u026e\u0271\u0001\u0000"+
		"\u0000\u0000\u026f\u026d\u0001\u0000\u0000\u0000\u0270\u0272\u0005\u0002"+
		"\u0000\u0000\u0271\u0270\u0001\u0000\u0000\u0000\u0271\u0272\u0001\u0000"+
		"\u0000\u0000\u0272\u0273\u0001\u0000\u0000\u0000\u0273\u0274\u0005\f\u0000"+
		"\u0000\u0274C\u0001\u0000\u0000\u0000\u0275\u0277\u0005\u000b\u0000\u0000"+
		"\u0276\u0278\u0003F#\u0000\u0277\u0276\u0001\u0000\u0000\u0000\u0277\u0278"+
		"\u0001\u0000\u0000\u0000\u0278\u027b\u0001\u0000\u0000\u0000\u0279\u027c"+
		"\u0003H$\u0000\u027a\u027c\u0003~?\u0000\u027b\u0279\u0001\u0000\u0000"+
		"\u0000\u027b\u027a\u0001\u0000\u0000\u0000\u027c\u027e\u0001\u0000\u0000"+
		"\u0000\u027d\u027f\u0003J%\u0000\u027e\u027d\u0001\u0000\u0000\u0000\u027e"+
		"\u027f\u0001\u0000\u0000\u0000\u027f\u0280\u0001\u0000\u0000\u0000\u0280"+
		"\u0281\u0005\f\u0000\u0000\u0281E\u0001\u0000\u0000\u0000\u0282\u0283"+
		"\u0005\u001f\u0000\u0000\u0283\u0284\u0003\u0084B\u0000\u0284G\u0001\u0000"+
		"\u0000\u0000\u0285\u0286\u0005 \u0000\u0000\u0286\u0287\u0003\u0084B\u0000"+
		"\u0287I\u0001\u0000\u0000\u0000\u0288\u0289\u0005!\u0000\u0000\u0289\u028a"+
		"\u0003\u0084B\u0000\u028aK\u0001\u0000\u0000\u0000\u028b\u028d\u0003*"+
		"\u0015\u0000\u028c\u028b\u0001\u0000\u0000\u0000\u028d\u0290\u0001\u0000"+
		"\u0000\u0000\u028e\u028c\u0001\u0000\u0000\u0000\u028e\u028f\u0001\u0000"+
		"\u0000\u0000\u028f\u0291\u0001\u0000\u0000\u0000\u0290\u028e\u0001\u0000"+
		"\u0000\u0000\u0291\u0292\u0005\"\u0000\u0000\u0292\u0293\u0003\u00e8t"+
		"\u0000\u0293\u0296\u0003T*\u0000\u0294\u0295\u0005\u0010\u0000\u0000\u0295"+
		"\u0297\u0003\u008aE\u0000\u0296\u0294\u0001\u0000\u0000\u0000\u0296\u0297"+
		"\u0001\u0000\u0000\u0000\u0297\u029e\u0001\u0000\u0000\u0000\u0298\u029f"+
		"\u0003\u0084B\u0000\u0299\u029a\u0005#\u0000\u0000\u029a\u029b\u0003\u0086"+
		"C\u0000\u029b\u029c\u0005\u0002\u0000\u0000\u029c\u029f\u0001\u0000\u0000"+
		"\u0000\u029d\u029f\u0005\u0002\u0000\u0000\u029e\u0298\u0001\u0000\u0000"+
		"\u0000\u029e\u0299\u0001\u0000\u0000\u0000\u029e\u029d\u0001\u0000\u0000"+
		"\u0000\u029fM\u0001\u0000\u0000\u0000\u02a0\u02a2\u0003*\u0015\u0000\u02a1"+
		"\u02a0\u0001\u0000\u0000\u0000\u02a2\u02a5\u0001\u0000\u0000\u0000\u02a3"+
		"\u02a1\u0001\u0000\u0000\u0000\u02a3\u02a4\u0001\u0000\u0000\u0000\u02a4"+
		"\u02a6\u0001\u0000\u0000\u0000\u02a5\u02a3\u0001\u0000\u0000\u0000\u02a6"+
		"\u02a7\u0005$\u0000\u0000\u02a7\u02a8\u0003\u00e8t\u0000\u02a8\u02a9\u0003"+
		"T*\u0000\u02a9\u02aa\u0005\u0010\u0000\u0000\u02aa\u02b1\u00032\u0019"+
		"\u0000\u02ab\u02b2\u0003\u0084B\u0000\u02ac\u02ad\u0005#\u0000\u0000\u02ad"+
		"\u02ae\u0003\u0086C\u0000\u02ae\u02af\u0005\u0002\u0000\u0000\u02af\u02b2"+
		"\u0001\u0000\u0000\u0000\u02b0\u02b2\u0005\u0002\u0000\u0000\u02b1\u02ab"+
		"\u0001\u0000\u0000\u0000\u02b1\u02ac\u0001\u0000\u0000\u0000\u02b1\u02b0"+
		"\u0001\u0000\u0000\u0000\u02b2O\u0001\u0000\u0000\u0000\u02b3\u02b5\u0003"+
		"*\u0015\u0000\u02b4\u02b3\u0001\u0000\u0000\u0000\u02b5\u02b8\u0001\u0000"+
		"\u0000\u0000\u02b6\u02b4\u0001\u0000\u0000\u0000\u02b6\u02b7\u0001\u0000"+
		"\u0000\u0000\u02b7\u02b9\u0001\u0000\u0000\u0000\u02b8\u02b6\u0001\u0000"+
		"\u0000\u0000\u02b9\u02ba\u0005%\u0000\u0000\u02ba\u02bb\u0003\u00e8t\u0000"+
		"\u02bb\u02c1\u0003T*\u0000\u02bc\u02be\u0003\u0084B\u0000\u02bd\u02bf"+
		"\u0005\u0002\u0000\u0000\u02be\u02bd\u0001\u0000\u0000\u0000\u02be\u02bf"+
		"\u0001\u0000\u0000\u0000\u02bf\u02c2\u0001\u0000\u0000\u0000\u02c0\u02c2"+
		"\u0005\u0002\u0000\u0000\u02c1\u02bc\u0001\u0000\u0000\u0000\u02c1\u02c0"+
		"\u0001\u0000\u0000\u0000\u02c2Q\u0001\u0000\u0000\u0000\u02c3\u02c4\u0005"+
		"&\u0000\u0000\u02c4\u02c5\u0003T*\u0000\u02c5\u02c7\u0003\u0084B\u0000"+
		"\u02c6\u02c8\u0005\u0002\u0000\u0000\u02c7\u02c6\u0001\u0000\u0000\u0000"+
		"\u02c7\u02c8\u0001\u0000\u0000\u0000\u02c8\u02d2\u0001\u0000\u0000\u0000"+
		"\u02c9\u02cb\u0005\'\u0000\u0000\u02ca\u02cc\u0003T*\u0000\u02cb\u02ca"+
		"\u0001\u0000\u0000\u0000\u02cb\u02cc\u0001\u0000\u0000\u0000\u02cc\u02cd"+
		"\u0001\u0000\u0000\u0000\u02cd\u02cf\u0003\u0084B\u0000\u02ce\u02d0\u0005"+
		"\u0002\u0000\u0000\u02cf\u02ce\u0001\u0000\u0000\u0000\u02cf\u02d0\u0001"+
		"\u0000\u0000\u0000\u02d0\u02d2\u0001\u0000\u0000\u0000\u02d1\u02c3\u0001"+
		"\u0000\u0000\u0000\u02d1\u02c9\u0001\u0000\u0000\u0000\u02d2S\u0001\u0000"+
		"\u0000\u0000\u02d3\u02d5\u0005\b\u0000\u0000\u02d4\u02d6\u00036\u001b"+
		"\u0000\u02d5\u02d4\u0001\u0000\u0000\u0000\u02d5\u02d6\u0001\u0000\u0000"+
		"\u0000\u02d6\u02d7\u0001\u0000\u0000\u0000\u02d7\u02d8\u0005\t\u0000\u0000"+
		"\u02d8U\u0001\u0000\u0000\u0000\u02d9\u02da\u0005(\u0000\u0000\u02da\u02db"+
		"\u0005)\u0000\u0000\u02db\u02dc\u0003\u00e8t\u0000\u02dc\u02dd\u0005\u0010"+
		"\u0000\u0000\u02dd\u02e0\u0003\u008aE\u0000\u02de\u02df\u0005#\u0000\u0000"+
		"\u02df\u02e1\u0003\u0086C\u0000\u02e0\u02de\u0001\u0000\u0000\u0000\u02e0"+
		"\u02e1\u0001\u0000\u0000\u0000\u02e1\u02e2\u0001\u0000\u0000\u0000\u02e2"+
		"\u02e3\u0005\u0002\u0000\u0000\u02e3\u0300\u0001\u0000\u0000\u0000\u02e4"+
		"\u02e5\u0005*\u0000\u0000\u02e5\u02e6\u0005)\u0000\u0000\u02e6\u02e7\u0003"+
		"\u00eew\u0000\u02e7\u02e8\u0005\u0010\u0000\u0000\u02e8\u02eb\u0003\u008a"+
		"E\u0000\u02e9\u02ea\u0005#\u0000\u0000\u02ea\u02ec\u0003\u0086C\u0000"+
		"\u02eb\u02e9\u0001\u0000\u0000\u0000\u02eb\u02ec\u0001\u0000\u0000\u0000"+
		"\u02ec\u02ed\u0001\u0000\u0000\u0000\u02ed\u02ee\u0005\u0002\u0000\u0000"+
		"\u02ee\u0300\u0001\u0000\u0000\u0000\u02ef\u02f0\u0005)\u0000\u0000\u02f0"+
		"\u02f1\u0003\u00eew\u0000\u02f1\u02f2\u0005\u0010\u0000\u0000\u02f2\u02f5"+
		"\u0003\u008aE\u0000\u02f3\u02f4\u0005#\u0000\u0000\u02f4\u02f6\u0003\u0086"+
		"C\u0000\u02f5\u02f3\u0001\u0000\u0000\u0000\u02f5\u02f6\u0001\u0000\u0000"+
		"\u0000\u02f6\u02f7\u0001\u0000\u0000\u0000\u02f7\u02f8\u0005\u0002\u0000"+
		"\u0000\u02f8\u0300\u0001\u0000\u0000\u0000\u02f9\u02fa\u0005)\u0000\u0000"+
		"\u02fa\u02fb\u0003\u00eew\u0000\u02fb\u02fc\u0005#\u0000\u0000\u02fc\u02fd"+
		"\u0003\u0086C\u0000\u02fd\u02fe\u0005\u0002\u0000\u0000\u02fe\u0300\u0001"+
		"\u0000\u0000\u0000\u02ff\u02d9\u0001\u0000\u0000\u0000\u02ff\u02e4\u0001"+
		"\u0000\u0000\u0000\u02ff\u02ef\u0001\u0000\u0000\u0000\u02ff\u02f9\u0001"+
		"\u0000\u0000\u0000\u0300W\u0001\u0000\u0000\u0000\u0301\u0302\u0005(\u0000"+
		"\u0000\u0302\u0303\u0005+\u0000\u0000\u0303\u0306\u0003\u00eew\u0000\u0304"+
		"\u0305\u0005\u0016\u0000\u0000\u0305\u0307\u0003j5\u0000\u0306\u0304\u0001"+
		"\u0000\u0000\u0000\u0306\u0307\u0001\u0000\u0000\u0000\u0307\u0308\u0001"+
		"\u0000\u0000\u0000\u0308\u030c\u0005\u000b\u0000\u0000\u0309\u030b\u0003"+
		"l6\u0000\u030a\u0309\u0001\u0000\u0000\u0000\u030b\u030e\u0001\u0000\u0000"+
		"\u0000\u030c\u030a\u0001\u0000\u0000\u0000\u030c\u030d\u0001\u0000\u0000"+
		"\u0000\u030d\u030f\u0001\u0000\u0000\u0000\u030e\u030c\u0001\u0000\u0000"+
		"\u0000\u030f\u0311\u0005\f\u0000\u0000\u0310\u0312\u0005\u0002\u0000\u0000"+
		"\u0311\u0310\u0001\u0000\u0000\u0000\u0311\u0312\u0001\u0000\u0000\u0000"+
		"\u0312Y\u0001\u0000\u0000\u0000\u0313\u0314\u0005,\u0000\u0000\u0314\u0317"+
		"\u0003\u00eew\u0000\u0315\u0316\u0005\u0016\u0000\u0000\u0316\u0318\u0003"+
		"j5\u0000\u0317\u0315\u0001\u0000\u0000\u0000\u0317\u0318\u0001\u0000\u0000"+
		"\u0000\u0318\u0319\u0001\u0000\u0000\u0000\u0319\u031d\u0005\u000b\u0000"+
		"\u0000\u031a\u031c\u0003l6\u0000\u031b\u031a\u0001\u0000\u0000\u0000\u031c"+
		"\u031f\u0001\u0000\u0000\u0000\u031d\u031b\u0001\u0000\u0000\u0000\u031d"+
		"\u031e\u0001\u0000\u0000\u0000\u031e\u0320\u0001\u0000\u0000\u0000\u031f"+
		"\u031d\u0001\u0000\u0000\u0000\u0320\u0322\u0005\f\u0000\u0000\u0321\u0323"+
		"\u0005\u0002\u0000\u0000\u0322\u0321\u0001\u0000\u0000\u0000\u0322\u0323"+
		"\u0001\u0000\u0000\u0000\u0323[\u0001\u0000\u0000\u0000\u0324\u0325\u0005"+
		"-\u0000\u0000\u0325\u0326\u0003\u00eew\u0000\u0326\u032a\u0005\u000b\u0000"+
		"\u0000\u0327\u0329\u0003l6\u0000\u0328\u0327\u0001\u0000\u0000\u0000\u0329"+
		"\u032c\u0001\u0000\u0000\u0000\u032a\u0328\u0001\u0000\u0000\u0000\u032a"+
		"\u032b\u0001\u0000\u0000\u0000\u032b\u032d\u0001\u0000\u0000\u0000\u032c"+
		"\u032a\u0001\u0000\u0000\u0000\u032d\u032f\u0005\f\u0000\u0000\u032e\u0330"+
		"\u0005\u0002\u0000\u0000\u032f\u032e\u0001\u0000\u0000\u0000\u032f\u0330"+
		"\u0001\u0000\u0000\u0000\u0330]\u0001\u0000\u0000\u0000\u0331\u0332\u0005"+
		".\u0000\u0000\u0332\u0333\u0003\u00eew\u0000\u0333\u0334\u0005\u0002\u0000"+
		"\u0000\u0334_\u0001\u0000\u0000\u0000\u0335\u0336\u0005/\u0000\u0000\u0336"+
		"\u0337\u0003\u00eew\u0000\u0337\u0339\u0005\u000b\u0000\u0000\u0338\u033a"+
		"\u0003b1\u0000\u0339\u0338\u0001\u0000\u0000\u0000\u0339\u033a\u0001\u0000"+
		"\u0000\u0000\u033a\u033b\u0001\u0000\u0000\u0000\u033b\u033d\u0005\f\u0000"+
		"\u0000\u033c\u033e\u0005\u0002\u0000\u0000\u033d\u033c\u0001\u0000\u0000"+
		"\u0000\u033d\u033e\u0001\u0000\u0000\u0000\u033ea\u0001\u0000\u0000\u0000"+
		"\u033f\u0344\u0003d2\u0000\u0340\u0341\u0005\u0005\u0000\u0000\u0341\u0343"+
		"\u0003d2\u0000\u0342\u0340\u0001\u0000\u0000\u0000\u0343\u0346\u0001\u0000"+
		"\u0000\u0000\u0344\u0342\u0001\u0000\u0000\u0000\u0344\u0345\u0001\u0000"+
		"\u0000\u0000\u0345c\u0001\u0000\u0000\u0000\u0346\u0344\u0001\u0000\u0000"+
		"\u0000\u0347\u034b\u0005\u00ac\u0000\u0000\u0348\u034b\u0005\u00a9\u0000"+
		"\u0000\u0349\u034b\u0003\u00eew\u0000\u034a\u0347\u0001\u0000\u0000\u0000"+
		"\u034a\u0348\u0001\u0000\u0000\u0000\u034a\u0349\u0001\u0000\u0000\u0000"+
		"\u034be\u0001\u0000\u0000\u0000\u034c\u034d\u0007\u0005\u0000\u0000\u034d"+
		"\u034e\u0003\u00eew\u0000\u034e\u0352\u0005\u000b\u0000\u0000\u034f\u0351"+
		"\u0003h4\u0000\u0350\u034f\u0001\u0000\u0000\u0000\u0351\u0354\u0001\u0000"+
		"\u0000\u0000\u0352\u0350\u0001\u0000\u0000\u0000\u0352\u0353\u0001\u0000"+
		"\u0000\u0000\u0353\u0355\u0001\u0000\u0000\u0000\u0354\u0352\u0001\u0000"+
		"\u0000\u0000\u0355\u0357\u0005\f\u0000\u0000\u0356\u0358\u0005\u0002\u0000"+
		"\u0000\u0357\u0356\u0001\u0000\u0000\u0000\u0357\u0358\u0001\u0000\u0000"+
		"\u0000\u0358g\u0001\u0000\u0000\u0000\u0359\u0362\u0003X,\u0000\u035a"+
		"\u0362\u0003Z-\u0000\u035b\u0362\u0003\\.\u0000\u035c\u0362\u0003^/\u0000"+
		"\u035d\u0362\u0003`0\u0000\u035e\u035f\u0003x<\u0000\u035f\u0360\u0005"+
		"\u0002\u0000\u0000\u0360\u0362\u0001\u0000\u0000\u0000\u0361\u0359\u0001"+
		"\u0000\u0000\u0000\u0361\u035a\u0001\u0000\u0000\u0000\u0361\u035b\u0001"+
		"\u0000\u0000\u0000\u0361\u035c\u0001\u0000\u0000\u0000\u0361\u035d\u0001"+
		"\u0000\u0000\u0000\u0361\u035e\u0001\u0000\u0000\u0000\u0362i\u0001\u0000"+
		"\u0000\u0000\u0363\u0368\u0003\u008aE\u0000\u0364\u0365\u0005\u0005\u0000"+
		"\u0000\u0365\u0367\u0003\u008aE\u0000\u0366\u0364\u0001\u0000\u0000\u0000"+
		"\u0367\u036a\u0001\u0000\u0000\u0000\u0368\u0366\u0001\u0000\u0000\u0000"+
		"\u0368\u0369\u0001\u0000\u0000\u0000\u0369k\u0001\u0000\u0000\u0000\u036a"+
		"\u0368\u0001\u0000\u0000\u0000\u036b\u036d\u0003n7\u0000\u036c\u036b\u0001"+
		"\u0000\u0000\u0000\u036d\u0370\u0001\u0000\u0000\u0000\u036e\u036c\u0001"+
		"\u0000\u0000\u0000\u036e\u036f\u0001\u0000\u0000\u0000\u036f\u0371\u0001"+
		"\u0000\u0000\u0000\u0370\u036e\u0001\u0000\u0000\u0000\u0371\u0372\u0003"+
		"\u00eew\u0000\u0372\u0373\u0005\u0010\u0000\u0000\u0373\u0374\u0003\u008a"+
		"E\u0000\u0374\u0375\u0003T*\u0000\u0375\u0376\u0005\u0002\u0000\u0000"+
		"\u0376\u0390\u0001\u0000\u0000\u0000\u0377\u0379\u0003n7\u0000\u0378\u0377"+
		"\u0001\u0000\u0000\u0000\u0379\u037c\u0001\u0000\u0000\u0000\u037a\u0378"+
		"\u0001\u0000\u0000\u0000\u037a\u037b\u0001\u0000\u0000\u0000\u037b\u037d"+
		"\u0001\u0000\u0000\u0000\u037c\u037a\u0001\u0000\u0000\u0000\u037d\u037e"+
		"\u0003\u00eew\u0000\u037e\u037f\u0005\u0010\u0000\u0000\u037f\u0381\u0003"+
		"\u008aE\u0000\u0380\u0382\u0003r9\u0000\u0381\u0380\u0001\u0000\u0000"+
		"\u0000\u0381\u0382\u0001\u0000\u0000\u0000\u0382\u0384\u0001\u0000\u0000"+
		"\u0000\u0383\u0385\u00052\u0000\u0000\u0384\u0383\u0001\u0000\u0000\u0000"+
		"\u0384\u0385\u0001\u0000\u0000\u0000\u0385\u0387\u0001\u0000\u0000\u0000"+
		"\u0386\u0388\u0003v;\u0000\u0387\u0386\u0001\u0000\u0000\u0000\u0387\u0388"+
		"\u0001\u0000\u0000\u0000\u0388\u038b\u0001\u0000\u0000\u0000\u0389\u038a"+
		"\u0005#\u0000\u0000\u038a\u038c\u0003\u0086C\u0000\u038b\u0389\u0001\u0000"+
		"\u0000\u0000\u038b\u038c\u0001\u0000\u0000\u0000\u038c\u038d\u0001\u0000"+
		"\u0000\u0000\u038d\u038e\u0005\u0002\u0000\u0000\u038e\u0390\u0001\u0000"+
		"\u0000\u0000\u038f\u036e\u0001\u0000\u0000\u0000\u038f\u037a\u0001\u0000"+
		"\u0000\u0000\u0390m\u0001\u0000\u0000\u0000\u0391\u0398\u0005\u0019\u0000"+
		"\u0000\u0392\u0398\u00053\u0000\u0000\u0393\u0398\u00054\u0000\u0000\u0394"+
		"\u0398\u00055\u0000\u0000\u0395\u0398\u00056\u0000\u0000\u0396\u0398\u0003"+
		"p8\u0000\u0397\u0391\u0001\u0000\u0000\u0000\u0397\u0392\u0001\u0000\u0000"+
		"\u0000\u0397\u0393\u0001\u0000\u0000\u0000\u0397\u0394\u0001\u0000\u0000"+
		"\u0000\u0397\u0395\u0001\u0000\u0000\u0000\u0397\u0396\u0001\u0000\u0000"+
		"\u0000\u0398o\u0001\u0000\u0000\u0000\u0399\u039a\u0005\u00a7\u0000\u0000"+
		"\u039a\u039f\u0003\u00eew\u0000\u039b\u039c\u0005\u0005\u0000\u0000\u039c"+
		"\u039e\u0003\u00eew\u0000\u039d\u039b\u0001\u0000\u0000\u0000\u039e\u03a1"+
		"\u0001\u0000\u0000\u0000\u039f\u039d\u0001\u0000\u0000\u0000\u039f\u03a0"+
		"\u0001\u0000\u0000\u0000\u03a0\u03a2\u0001\u0000\u0000\u0000\u03a1\u039f"+
		"\u0001\u0000\u0000\u0000\u03a2\u03a3\u0005\u00a8\u0000\u0000\u03a3q\u0001"+
		"\u0000\u0000\u0000\u03a4\u03a5\u00057\u0000\u0000\u03a5\u03a6\u0003t:"+
		"\u0000\u03a6\u03a7\u00058\u0000\u0000\u03a7s\u0001\u0000\u0000\u0000\u03a8"+
		"\u03a9\u0005\u00ab\u0000\u0000\u03a9\u03aa\u00059\u0000\u0000\u03aa\u03ae"+
		"\u0007\u0006\u0000\u0000\u03ab\u03ae\u0005\u00ab\u0000\u0000\u03ac\u03ae"+
		"\u0005\u0004\u0000\u0000\u03ad\u03a8\u0001\u0000\u0000\u0000\u03ad\u03ab"+
		"\u0001\u0000\u0000\u0000\u03ad\u03ac\u0001\u0000\u0000\u0000\u03aeu\u0001"+
		"\u0000\u0000\u0000\u03af\u03b1\u0005:\u0000\u0000\u03b0\u03b2\u0005;\u0000"+
		"\u0000\u03b1\u03b0\u0001\u0000\u0000\u0000\u03b1\u03b2\u0001\u0000\u0000"+
		"\u0000\u03b2\u03b3\u0001\u0000\u0000\u0000\u03b3\u03b5\u0003\u00eew\u0000"+
		"\u03b4\u03b6\u0003r9\u0000\u03b5\u03b4\u0001\u0000\u0000\u0000\u03b5\u03b6"+
		"\u0001\u0000\u0000\u0000\u03b6w\u0001\u0000\u0000\u0000\u03b7\u03b8\u0005"+
		"<\u0000\u0000\u03b8\u03b9\u0007\u0007\u0000\u0000\u03b9\u03bc\u0003z="+
		"\u0000\u03ba\u03bb\u0005#\u0000\u0000\u03bb\u03bd\u0003\u0086C\u0000\u03bc"+
		"\u03ba\u0001\u0000\u0000\u0000\u03bc\u03bd\u0001\u0000\u0000\u0000\u03bd"+
		"y\u0001\u0000\u0000\u0000\u03be\u03c3\u0003\u00eew\u0000\u03bf\u03c0\u0005"+
		"=\u0000\u0000\u03c0\u03c2\u0003\u00eew\u0000\u03c1\u03bf\u0001\u0000\u0000"+
		"\u0000\u03c2\u03c5\u0001\u0000\u0000\u0000\u03c3\u03c1\u0001\u0000\u0000"+
		"\u0000\u03c3\u03c4\u0001\u0000\u0000\u0000\u03c4{\u0001\u0000\u0000\u0000"+
		"\u03c5\u03c3\u0001\u0000\u0000\u0000\u03c6\u03c7\u0005>\u0000\u0000\u03c7"+
		"\u03c8\u0003\u00eew\u0000\u03c8\u03c9\u0005#\u0000\u0000\u03c9\u03cc\u0003"+
		"\u008aE\u0000\u03ca\u03cb\u0005?\u0000\u0000\u03cb\u03cd\u0003\u0086C"+
		"\u0000\u03cc\u03ca\u0001\u0000\u0000\u0000\u03cc\u03cd\u0001\u0000\u0000"+
		"\u0000\u03cd}\u0001\u0000\u0000\u0000\u03ce\u03d0\u0003\u0080@\u0000\u03cf"+
		"\u03ce\u0001\u0000\u0000\u0000\u03d0\u03d3\u0001\u0000\u0000\u0000\u03d1"+
		"\u03cf\u0001\u0000\u0000\u0000\u03d1\u03d2\u0001\u0000\u0000\u0000\u03d2"+
		"\u007f\u0001\u0000\u0000\u0000\u03d3\u03d1\u0001\u0000\u0000\u0000\u03d4"+
		"\u03d5\u0003\u00d4j\u0000\u03d5\u03d6\u0005\u0002\u0000\u0000\u03d6\u03db"+
		"\u0001\u0000\u0000\u0000\u03d7\u03d8\u0003\u0086C\u0000\u03d8\u03d9\u0005"+
		"\u0002\u0000\u0000\u03d9\u03db\u0001\u0000\u0000\u0000\u03da\u03d4\u0001"+
		"\u0000\u0000\u0000\u03da\u03d7\u0001\u0000\u0000\u0000\u03db\u0081\u0001"+
		"\u0000\u0000\u0000\u03dc\u03dd\u0007\b\u0000\u0000\u03dd\u0083\u0001\u0000"+
		"\u0000\u0000\u03de\u03e2\u0005\u000b\u0000\u0000\u03df\u03e1\u0003\u0080"+
		"@\u0000\u03e0\u03df\u0001\u0000\u0000\u0000\u03e1\u03e4\u0001\u0000\u0000"+
		"\u0000\u03e2\u03e0\u0001\u0000\u0000\u0000\u03e2\u03e3\u0001\u0000\u0000"+
		"\u0000\u03e3\u03e5\u0001\u0000\u0000\u0000\u03e4\u03e2\u0001\u0000\u0000"+
		"\u0000\u03e5\u03e6\u0005\f\u0000\u0000\u03e6\u0085\u0001\u0000\u0000\u0000"+
		"\u03e7\u03e8\u0006C\uffff\uffff\u0000\u03e8\u03e9\u0005\u00a4\u0000\u0000"+
		"\u03e9\u03f4\u0003\u0086C\u000f\u03ea\u03eb\u0005\u00a3\u0000\u0000\u03eb"+
		"\u03f4\u0003\u0086C\u000e\u03ec\u03ed\u0005\u0004\u0000\u0000\u03ed\u03f4"+
		"\u0003\u0086C\r\u03ee\u03ef\u0005F\u0000\u0000\u03ef\u03f4\u0003\u0086"+
		"C\f\u03f0\u03f1\u0005G\u0000\u0000\u03f1\u03f4\u0003\u0086C\u000b\u03f2"+
		"\u03f4\u0003\u0088D\u0000\u03f3\u03e7\u0001\u0000\u0000\u0000\u03f3\u03ea"+
		"\u0001\u0000\u0000\u0000\u03f3\u03ec\u0001\u0000\u0000\u0000\u03f3\u03ee"+
		"\u0001\u0000\u0000\u0000\u03f3\u03f0\u0001\u0000\u0000\u0000\u03f3\u03f2"+
		"\u0001\u0000\u0000\u0000\u03f4\u0431\u0001\u0000\u0000\u0000\u03f5\u03f6"+
		"\n\n\u0000\u0000\u03f6\u03f7\u0007\t\u0000\u0000\u03f7\u0430\u0003\u0086"+
		"C\u000b\u03f8\u03f9\n\t\u0000\u0000\u03f9\u03fa\u0007\n\u0000\u0000\u03fa"+
		"\u0430\u0003\u0086C\n\u03fb\u03fc\n\b\u0000\u0000\u03fc\u03fd\u0007\u000b"+
		"\u0000\u0000\u03fd\u0430\u0003\u0086C\t\u03fe\u03ff\n\u0007\u0000\u0000"+
		"\u03ff\u0400\u0007\f\u0000\u0000\u0400\u0430\u0003\u0086C\b\u0401\u0402"+
		"\n\u0006\u0000\u0000\u0402\u0403\u0005P\u0000\u0000\u0403\u0430\u0003"+
		"\u0086C\u0007\u0404\u0405\n\u0005\u0000\u0000\u0405\u0406\u0005Q\u0000"+
		"\u0000\u0406\u0430\u0003\u0086C\u0006\u0407\u0408\n\u0004\u0000\u0000"+
		"\u0408\u0409\u0005R\u0000\u0000\u0409\u0430\u0003\u0086C\u0005\u040a\u040b"+
		"\n\u0003\u0000\u0000\u040b\u040c\u0005S\u0000\u0000\u040c\u0430\u0003"+
		"\u0086C\u0004\u040d\u040e\n\u0013\u0000\u0000\u040e\u040f\u0007\r\u0000"+
		"\u0000\u040f\u0430\u0003\u00dcn\u0000\u0410\u0411\n\u0012\u0000\u0000"+
		"\u0411\u0412\u0007\u000e\u0000\u0000\u0412\u0430\u0003\u00deo\u0000\u0413"+
		"\u0414\n\u0011\u0000\u0000\u0414\u0418\u00057\u0000\u0000\u0415\u0416"+
		"\u0003\u00eew\u0000\u0416\u0417\u0005D\u0000\u0000\u0417\u0419\u0001\u0000"+
		"\u0000\u0000\u0418\u0415\u0001\u0000\u0000\u0000\u0418\u0419\u0001\u0000"+
		"\u0000\u0000\u0419\u041a\u0001\u0000\u0000\u0000\u041a\u041b\u0003\u0086"+
		"C\u0000\u041b\u041c\u00058\u0000\u0000\u041c\u0430\u0001\u0000\u0000\u0000"+
		"\u041d\u041e\n\u0010\u0000\u0000\u041e\u041f\u0005E\u0000\u0000\u041f"+
		"\u0423\u00057\u0000\u0000\u0420\u0421\u0003\u00eew\u0000\u0421\u0422\u0005"+
		"D\u0000\u0000\u0422\u0424\u0001\u0000\u0000\u0000\u0423\u0420\u0001\u0000"+
		"\u0000\u0000\u0423\u0424\u0001\u0000\u0000\u0000\u0424\u0425\u0001\u0000"+
		"\u0000\u0000\u0425\u0426\u0003\u0086C\u0000\u0426\u0427\u00058\u0000\u0000"+
		"\u0427\u0430\u0001\u0000\u0000\u0000\u0428\u0429\n\u0002\u0000\u0000\u0429"+
		"\u042a\u0003\u0082A\u0000\u042a\u042d\u0003\u0086C\u0000\u042b\u042c\u0005"+
		"T\u0000\u0000\u042c\u042e\u0003\u0086C\u0000\u042d\u042b\u0001\u0000\u0000"+
		"\u0000\u042d\u042e\u0001\u0000\u0000\u0000\u042e\u0430\u0001\u0000\u0000"+
		"\u0000\u042f\u03f5\u0001\u0000\u0000\u0000\u042f\u03f8\u0001\u0000\u0000"+
		"\u0000\u042f\u03fb\u0001\u0000\u0000\u0000\u042f\u03fe\u0001\u0000\u0000"+
		"\u0000\u042f\u0401\u0001\u0000\u0000\u0000\u042f\u0404\u0001\u0000\u0000"+
		"\u0000\u042f\u0407\u0001\u0000\u0000\u0000\u042f\u040a\u0001\u0000\u0000"+
		"\u0000\u042f\u040d\u0001\u0000\u0000\u0000\u042f\u0410\u0001\u0000\u0000"+
		"\u0000\u042f\u0413\u0001\u0000\u0000\u0000\u042f\u041d\u0001\u0000\u0000"+
		"\u0000\u042f\u0428\u0001\u0000\u0000\u0000\u0430\u0433\u0001\u0000\u0000"+
		"\u0000\u0431\u042f\u0001\u0000\u0000\u0000\u0431\u0432\u0001\u0000\u0000"+
		"\u0000\u0432\u0087\u0001\u0000\u0000\u0000\u0433\u0431\u0001\u0000\u0000"+
		"\u0000\u0434\u0435\u0005\b\u0000\u0000\u0435\u0436\u0003\u0086C\u0000"+
		"\u0436\u0437\u0005\t\u0000\u0000\u0437\u0491\u0001\u0000\u0000\u0000\u0438"+
		"\u0491\u0005U\u0000\u0000\u0439\u0491\u0005V\u0000\u0000\u043a\u043b\u0005"+
		"W\u0000\u0000\u043b\u043c\u0003\u0086C\u0000\u043c\u043d\u0005X\u0000"+
		"\u0000\u043d\u0445\u0003\u0086C\u0000\u043e\u043f\u0007\u000f\u0000\u0000"+
		"\u043f\u0440\u0003\u0086C\u0000\u0440\u0441\u0005X\u0000\u0000\u0441\u0442"+
		"\u0003\u0086C\u0000\u0442\u0444\u0001\u0000\u0000\u0000\u0443\u043e\u0001"+
		"\u0000\u0000\u0000\u0444\u0447\u0001\u0000\u0000\u0000\u0445\u0443\u0001"+
		"\u0000\u0000\u0000\u0445\u0446\u0001\u0000\u0000\u0000\u0446\u044a\u0001"+
		"\u0000\u0000\u0000\u0447\u0445\u0001\u0000\u0000\u0000\u0448\u0449\u0005"+
		"[\u0000\u0000\u0449\u044b\u0003\u0086C\u0000\u044a\u0448\u0001\u0000\u0000"+
		"\u0000\u044a\u044b\u0001\u0000\u0000\u0000\u044b\u044c\u0001\u0000\u0000"+
		"\u0000\u044c\u044d\u0005\\\u0000\u0000\u044d\u0491\u0001\u0000\u0000\u0000"+
		"\u044e\u044f\u0005W\u0000\u0000\u044f\u0450\u0005\b\u0000\u0000\u0450"+
		"\u0451\u0003\u0086C\u0000\u0451\u0452\u0005\t\u0000\u0000\u0452\u045b"+
		"\u0003\u0084B\u0000\u0453\u0454\u0005Z\u0000\u0000\u0454\u0455\u0005\b"+
		"\u0000\u0000\u0455\u0456\u0003\u0086C\u0000\u0456\u0457\u0005\t\u0000"+
		"\u0000\u0457\u0458\u0003\u0084B\u0000\u0458\u045a\u0001\u0000\u0000\u0000"+
		"\u0459\u0453\u0001\u0000\u0000\u0000\u045a\u045d\u0001\u0000\u0000\u0000"+
		"\u045b\u0459\u0001\u0000\u0000\u0000\u045b\u045c\u0001\u0000\u0000\u0000"+
		"\u045c\u0460\u0001\u0000\u0000\u0000\u045d\u045b\u0001\u0000\u0000\u0000"+
		"\u045e\u045f\u0005[\u0000\u0000\u045f\u0461\u0003\u0084B\u0000\u0460\u045e"+
		"\u0001\u0000\u0000\u0000\u0460\u0461\u0001\u0000\u0000\u0000\u0461\u0463"+
		"\u0001\u0000\u0000\u0000\u0462\u0464\u0005\\\u0000\u0000\u0463\u0462\u0001"+
		"\u0000\u0000\u0000\u0463\u0464\u0001\u0000\u0000\u0000\u0464\u0491\u0001"+
		"\u0000\u0000\u0000\u0465\u0466\u0005]\u0000\u0000\u0466\u046b\u0003\u00e2"+
		"q\u0000\u0467\u0468\u0005\u0005\u0000\u0000\u0468\u046a\u0003\u00e2q\u0000"+
		"\u0469\u0467\u0001\u0000\u0000\u0000\u046a\u046d\u0001\u0000\u0000\u0000"+
		"\u046b\u0469\u0001\u0000\u0000\u0000\u046b\u046c\u0001\u0000\u0000\u0000"+
		"\u046c\u046e\u0001\u0000\u0000\u0000\u046d\u046b\u0001\u0000\u0000\u0000"+
		"\u046e\u046f\u0005\u0011\u0000\u0000\u046f\u0470\u0003\u0086C\u0000\u0470"+
		"\u0491\u0001\u0000\u0000\u0000\u0471\u0491\u0003\u0092I\u0000\u0472\u0491"+
		"\u0003\u009cN\u0000\u0473\u0491\u0003\u009eO\u0000\u0474\u0491\u0003\u00a0"+
		"P\u0000\u0475\u0491\u0003\u00a6S\u0000\u0476\u0491\u0003\u00acV\u0000"+
		"\u0477\u0491\u0003\u00aeW\u0000\u0478\u0491\u0003\u00b2Y\u0000\u0479\u0491"+
		"\u0003\u00b4Z\u0000\u047a\u0491\u0003\u00b8\\\u0000\u047b\u0491\u0003"+
		"\u00bc^\u0000\u047c\u0491\u0003\u00c4b\u0000\u047d\u0491\u0003\u00cae"+
		"\u0000\u047e\u0491\u0003\u00ccf\u0000\u047f\u0491\u0003\u00d0h\u0000\u0480"+
		"\u0491\u0003\u00d2i\u0000\u0481\u0491\u0005^\u0000\u0000\u0482\u0491\u0005"+
		"_\u0000\u0000\u0483\u0491\u0003\u00d4j\u0000\u0484\u0485\u0003\u00dam"+
		"\u0000\u0485\u0487\u0005\b\u0000\u0000\u0486\u0488\u0003\u0110\u0088\u0000"+
		"\u0487\u0486\u0001\u0000\u0000\u0000\u0487\u0488\u0001\u0000\u0000\u0000"+
		"\u0488\u0489\u0001\u0000\u0000\u0000\u0489\u048a\u0005\t\u0000\u0000\u048a"+
		"\u0491\u0001\u0000\u0000\u0000\u048b\u0491\u0003\u011e\u008f\u0000\u048c"+
		"\u0491\u0003\u0120\u0090\u0000\u048d\u0491\u0003\u0122\u0091\u0000\u048e"+
		"\u0491\u0003\u0124\u0092\u0000\u048f\u0491\u0003\u00dam\u0000\u0490\u0434"+
		"\u0001\u0000\u0000\u0000\u0490\u0438\u0001\u0000\u0000\u0000\u0490\u0439"+
		"\u0001\u0000\u0000\u0000\u0490\u043a\u0001\u0000\u0000\u0000\u0490\u044e"+
		"\u0001\u0000\u0000\u0000\u0490\u0465\u0001\u0000\u0000\u0000\u0490\u0471"+
		"\u0001\u0000\u0000\u0000\u0490\u0472\u0001\u0000\u0000\u0000\u0490\u0473"+
		"\u0001\u0000\u0000\u0000\u0490\u0474\u0001\u0000\u0000\u0000\u0490\u0475"+
		"\u0001\u0000\u0000\u0000\u0490\u0476\u0001\u0000\u0000\u0000\u0490\u0477"+
		"\u0001\u0000\u0000\u0000\u0490\u0478\u0001\u0000\u0000\u0000\u0490\u0479"+
		"\u0001\u0000\u0000\u0000\u0490\u047a\u0001\u0000\u0000\u0000\u0490\u047b"+
		"\u0001\u0000\u0000\u0000\u0490\u047c\u0001\u0000\u0000\u0000\u0490\u047d"+
		"\u0001\u0000\u0000\u0000\u0490\u047e\u0001\u0000\u0000\u0000\u0490\u047f"+
		"\u0001\u0000\u0000\u0000\u0490\u0480\u0001\u0000\u0000\u0000\u0490\u0481"+
		"\u0001\u0000\u0000\u0000\u0490\u0482\u0001\u0000\u0000\u0000\u0490\u0483"+
		"\u0001\u0000\u0000\u0000\u0490\u0484\u0001\u0000\u0000\u0000\u0490\u048b"+
		"\u0001\u0000\u0000\u0000\u0490\u048c\u0001\u0000\u0000\u0000\u0490\u048d"+
		"\u0001\u0000\u0000\u0000\u0490\u048e\u0001\u0000\u0000\u0000\u0490\u048f"+
		"\u0001\u0000\u0000\u0000\u0491\u0089\u0001\u0000\u0000\u0000\u0492\u049a"+
		"\u0003\u011e\u008f\u0000\u0493\u049a\u0003\u0120\u0090\u0000\u0494\u049a"+
		"\u0003\u0122\u0091\u0000\u0495\u049a\u0003\u0124\u0092\u0000\u0496\u049a"+
		"\u0003\u008cF\u0000\u0497\u049a\u0003\u008eG\u0000\u0498\u049a\u0003\u00da"+
		"m\u0000\u0499\u0492\u0001\u0000\u0000\u0000\u0499\u0493\u0001\u0000\u0000"+
		"\u0000\u0499\u0494\u0001\u0000\u0000\u0000\u0499\u0495\u0001\u0000\u0000"+
		"\u0000\u0499\u0496\u0001\u0000\u0000\u0000\u0499\u0497\u0001\u0000\u0000"+
		"\u0000\u0499\u0498\u0001\u0000\u0000\u0000\u049a\u008b\u0001\u0000\u0000"+
		"\u0000\u049b\u049c\u0005`\u0000\u0000\u049c\u049d\u0005\b\u0000\u0000"+
		"\u049d\u049e\u0003\u008aE\u0000\u049e\u049f\u0005\t\u0000\u0000\u049f"+
		"\u008d\u0001\u0000\u0000\u0000\u04a0\u04a1\u0005a\u0000\u0000\u04a1\u04a2"+
		"\u0005\b\u0000\u0000\u04a2\u04a3\u0003\u008aE\u0000\u04a3\u04a4\u0005"+
		"\u0005\u0000\u0000\u04a4\u04a5\u0003\u008aE\u0000\u04a5\u04a6\u0005\t"+
		"\u0000\u0000\u04a6\u008f\u0001\u0000\u0000\u0000\u04a7\u04a8\u0007\u0010"+
		"\u0000\u0000\u04a8\u0091\u0001\u0000\u0000\u0000\u04a9\u04b6\u0005\u00ab"+
		"\u0000\u0000\u04aa\u04b6\u0005\u00aa\u0000\u0000\u04ab\u04b6\u0003\u0094"+
		"J\u0000\u04ac\u04b6\u0005g\u0000\u0000\u04ad\u04b6\u0005h\u0000\u0000"+
		"\u04ae\u04b6\u0005i\u0000\u0000\u04af\u04b6\u0005j\u0000\u0000\u04b0\u04b6"+
		"\u0007\u0011\u0000\u0000\u04b1\u04b6\u0003\u0114\u008a\u0000\u04b2\u04b6"+
		"\u0003\u0118\u008c\u0000\u04b3\u04b6\u0003\u011a\u008d\u0000\u04b4\u04b6"+
		"\u0003\u0096K\u0000\u04b5\u04a9\u0001\u0000\u0000\u0000\u04b5\u04aa\u0001"+
		"\u0000\u0000\u0000\u04b5\u04ab\u0001\u0000\u0000\u0000\u04b5\u04ac\u0001"+
		"\u0000\u0000\u0000\u04b5\u04ad\u0001\u0000\u0000\u0000\u04b5\u04ae\u0001"+
		"\u0000\u0000\u0000\u04b5\u04af\u0001\u0000\u0000\u0000\u04b5\u04b0\u0001"+
		"\u0000\u0000\u0000\u04b5\u04b1\u0001\u0000\u0000\u0000\u04b5\u04b2\u0001"+
		"\u0000\u0000\u0000\u04b5\u04b3\u0001\u0000\u0000\u0000\u04b5\u04b4\u0001"+
		"\u0000\u0000\u0000\u04b6\u0093\u0001\u0000\u0000\u0000\u04b7\u04b9\u0007"+
		"\u0007\u0000\u0000\u04b8\u04b7\u0001\u0000\u0000\u0000\u04b9\u04ba\u0001"+
		"\u0000\u0000\u0000\u04ba\u04b8\u0001\u0000\u0000\u0000\u04ba\u04bb\u0001"+
		"\u0000\u0000\u0000\u04bb\u0095\u0001\u0000\u0000\u0000\u04bc\u04bd\u0005"+
		"a\u0000\u0000\u04bd\u04c6\u0005\u000b\u0000\u0000\u04be\u04c3\u0003\u0098"+
		"L\u0000\u04bf\u04c0\u0005\u0005\u0000\u0000\u04c0\u04c2\u0003\u0098L\u0000"+
		"\u04c1\u04bf\u0001\u0000\u0000\u0000\u04c2\u04c5\u0001\u0000\u0000\u0000"+
		"\u04c3\u04c1\u0001\u0000\u0000\u0000\u04c3\u04c4\u0001\u0000\u0000\u0000"+
		"\u04c4\u04c7\u0001\u0000\u0000\u0000\u04c5\u04c3\u0001\u0000\u0000\u0000"+
		"\u04c6\u04be\u0001\u0000\u0000\u0000\u04c6\u04c7\u0001\u0000\u0000\u0000"+
		"\u04c7\u04c8\u0001\u0000\u0000\u0000\u04c8\u04c9\u0005\f\u0000\u0000\u04c9"+
		"\u0097\u0001\u0000\u0000\u0000\u04ca\u04cb\u0003\u009aM\u0000\u04cb\u04cc"+
		"\u0005#\u0000\u0000\u04cc\u04cd\u0003\u0086C\u0000\u04cd\u0099\u0001\u0000"+
		"\u0000\u0000\u04ce\u04d6\u0005\u00ab\u0000\u0000\u04cf\u04d6\u0005\u00aa"+
		"\u0000\u0000\u04d0\u04d6\u0003\u0094J\u0000\u04d1\u04d6\u0005g\u0000\u0000"+
		"\u04d2\u04d6\u0005h\u0000\u0000\u04d3\u04d6\u0005i\u0000\u0000\u04d4\u04d6"+
		"\u0007\u0011\u0000\u0000\u04d5\u04ce\u0001\u0000\u0000\u0000\u04d5\u04cf"+
		"\u0001\u0000\u0000\u0000\u04d5\u04d0\u0001\u0000\u0000\u0000\u04d5\u04d1"+
		"\u0001\u0000\u0000\u0000\u04d5\u04d2\u0001\u0000\u0000\u0000\u04d5\u04d3"+
		"\u0001\u0000\u0000\u0000\u04d5\u04d4\u0001\u0000\u0000\u0000\u04d6\u009b"+
		"\u0001\u0000\u0000\u0000\u04d7\u04d8\u0005l\u0000\u0000\u04d8\u04dc\u0005"+
		"\u000b\u0000\u0000\u04d9\u04db\u0003\u0080@\u0000\u04da\u04d9\u0001\u0000"+
		"\u0000\u0000\u04db\u04de\u0001\u0000\u0000\u0000\u04dc\u04da\u0001\u0000"+
		"\u0000\u0000\u04dc\u04dd\u0001\u0000\u0000\u0000\u04dd\u04df\u0001\u0000"+
		"\u0000\u0000\u04de\u04dc\u0001\u0000\u0000\u0000\u04df\u04e9\u0005\f\u0000"+
		"\u0000\u04e0\u04e4\u0005\u000b\u0000\u0000\u04e1\u04e3\u0003\u0080@\u0000"+
		"\u04e2\u04e1\u0001\u0000\u0000\u0000\u04e3\u04e6\u0001\u0000\u0000\u0000"+
		"\u04e4\u04e2\u0001\u0000\u0000\u0000\u04e4\u04e5\u0001\u0000\u0000\u0000"+
		"\u04e5\u04e7\u0001\u0000\u0000\u0000\u04e6\u04e4\u0001\u0000\u0000\u0000"+
		"\u04e7\u04e9\u0005\f\u0000\u0000\u04e8\u04d7\u0001\u0000\u0000\u0000\u04e8"+
		"\u04e0\u0001\u0000\u0000\u0000\u04e9\u009d\u0001\u0000\u0000\u0000\u04ea"+
		"\u04eb\u0005m\u0000\u0000\u04eb\u04ec\u0005\b\u0000\u0000\u04ec\u04ed"+
		"\u0003\u00eew\u0000\u04ed\u04ee\u0005\u0010\u0000\u0000\u04ee\u04ef\u0003"+
		"\u008aE\u0000\u04ef\u04f0\u0005\u009f\u0000\u0000\u04f0\u04f1\u0003\u0086"+
		"C\u0000\u04f1\u04f2\u0005\u0002\u0000\u0000\u04f2\u04f3\u0003\u0086C\u0000"+
		"\u04f3\u04f4\u0005\t\u0000\u0000\u04f4\u04f5\u0003\u0084B\u0000\u04f5"+
		"\u04fd\u0001\u0000\u0000\u0000\u04f6\u04f7\u0005m\u0000\u0000\u04f7\u04f8"+
		"\u0005\b\u0000\u0000\u04f8\u04f9\u0003\u0086C\u0000\u04f9\u04fa\u0005"+
		"\t\u0000\u0000\u04fa\u04fb\u0003\u0084B\u0000\u04fb\u04fd\u0001\u0000"+
		"\u0000\u0000\u04fc\u04ea\u0001\u0000\u0000\u0000\u04fc\u04f6\u0001\u0000"+
		"\u0000\u0000\u04fd\u009f\u0001\u0000\u0000\u0000\u04fe\u04ff\u0003\u00a2"+
		"Q\u0000\u04ff\u0500\u0005\b\u0000\u0000\u0500\u0503\u0003\u00a4R\u0000"+
		"\u0501\u0502\u0005D\u0000\u0000\u0502\u0504\u0003\u0086C\u0000\u0503\u0501"+
		"\u0001\u0000\u0000\u0000\u0503\u0504\u0001\u0000\u0000\u0000\u0504\u0505"+
		"\u0001\u0000\u0000\u0000\u0505\u0506\u0005\t\u0000\u0000\u0506\u0507\u0003"+
		"\u0084B\u0000\u0507\u00a1\u0001\u0000\u0000\u0000\u0508\u0509\u0007\u0012"+
		"\u0000\u0000\u0509\u00a3\u0001\u0000\u0000\u0000\u050a\u050b\u0003\u00e0"+
		"p\u0000\u050b\u050c\u0005\u0002\u0000\u0000\u050c\u050d\u0003\u00eew\u0000"+
		"\u050d\u050e\u0005\u0010\u0000\u0000\u050e\u0512\u0003\u008aE\u0000\u050f"+
		"\u0510\u0003\u00d8l\u0000\u0510\u0511\u0003\u0086C\u0000\u0511\u0513\u0001"+
		"\u0000\u0000\u0000\u0512\u050f\u0001\u0000\u0000\u0000\u0512\u0513\u0001"+
		"\u0000\u0000\u0000\u0513\u051a\u0001\u0000\u0000\u0000\u0514\u0515\u0003"+
		"\u00e0p\u0000\u0515\u0516\u0005\u0002\u0000\u0000\u0516\u0517\u0003\u008a"+
		"E\u0000\u0517\u051a\u0001\u0000\u0000\u0000\u0518\u051a\u0003\u00e0p\u0000"+
		"\u0519\u050a\u0001\u0000\u0000\u0000\u0519\u0514\u0001\u0000\u0000\u0000"+
		"\u0519\u0518\u0001\u0000\u0000\u0000\u051a\u00a5\u0001\u0000\u0000\u0000"+
		"\u051b\u0520\u0005p\u0000\u0000\u051c\u051d\u0005\b\u0000\u0000\u051d"+
		"\u051e\u0003\u00a8T\u0000\u051e\u051f\u0005\t\u0000\u0000\u051f\u0521"+
		"\u0001\u0000\u0000\u0000\u0520\u051c\u0001\u0000\u0000\u0000\u0520\u0521"+
		"\u0001\u0000\u0000\u0000\u0521\u0522\u0001\u0000\u0000\u0000\u0522\u0526"+
		"\u0005\u000b\u0000\u0000\u0523\u0525\u0003\u00aaU\u0000\u0524\u0523\u0001"+
		"\u0000\u0000\u0000\u0525\u0528\u0001\u0000\u0000\u0000\u0526\u0524\u0001"+
		"\u0000\u0000\u0000\u0526\u0527\u0001\u0000\u0000\u0000\u0527\u052d\u0001"+
		"\u0000\u0000\u0000\u0528\u0526\u0001\u0000\u0000\u0000\u0529\u052a\u0005"+
		"[\u0000\u0000\u052a\u052b\u0003\u0086C\u0000\u052b\u052c\u0005\u0002\u0000"+
		"\u0000\u052c\u052e\u0001\u0000\u0000\u0000\u052d\u0529\u0001\u0000\u0000"+
		"\u0000\u052d\u052e\u0001\u0000\u0000\u0000\u052e\u052f\u0001\u0000\u0000"+
		"\u0000\u052f\u0530\u0005\f\u0000\u0000\u0530\u00a7\u0001\u0000\u0000\u0000"+
		"\u0531\u0534\u0003\u00eew\u0000\u0532\u0533\u0005\u0010\u0000\u0000\u0533"+
		"\u0535\u0003\u008aE\u0000\u0534\u0532\u0001\u0000\u0000\u0000\u0534\u0535"+
		"\u0001\u0000\u0000\u0000\u0535\u0539\u0001\u0000\u0000\u0000\u0536\u0537"+
		"\u0003\u00d8l\u0000\u0537\u0538\u0003\u0086C\u0000\u0538\u053a\u0001\u0000"+
		"\u0000\u0000\u0539\u0536\u0001\u0000\u0000\u0000\u0539\u053a\u0001\u0000"+
		"\u0000\u0000\u053a\u00a9\u0001\u0000\u0000\u0000\u053b\u053c\u0005q\u0000"+
		"\u0000\u053c\u053d\u0005\b\u0000\u0000\u053d\u053e\u0003\u0086C\u0000"+
		"\u053e\u053f\u0005\t\u0000\u0000\u053f\u0540\u0003\u0086C\u0000\u0540"+
		"\u0541\u0005\u0002\u0000\u0000\u0541\u00ab\u0001\u0000\u0000\u0000\u0542"+
		"\u0543\u0005r\u0000\u0000\u0543\u0544\u0005\b\u0000\u0000\u0544\u0545"+
		"\u0003\u00d6k\u0000\u0545\u0546\u0005\t\u0000\u0000\u0546\u0547\u0003"+
		"\u0084B\u0000\u0547\u00ad\u0001\u0000\u0000\u0000\u0548\u054a\u0005s\u0000"+
		"\u0000\u0549\u054b\u0003\u00b0X\u0000\u054a\u0549\u0001\u0000\u0000\u0000"+
		"\u054a\u054b\u0001\u0000\u0000\u0000\u054b\u054f\u0001\u0000\u0000\u0000"+
		"\u054c\u054d\u0003\u00eew\u0000\u054d\u054e\u0005\u0010\u0000\u0000\u054e"+
		"\u0550\u0001\u0000\u0000\u0000\u054f\u054c\u0001\u0000\u0000\u0000\u054f"+
		"\u0550\u0001\u0000\u0000\u0000\u0550\u0552\u0001\u0000\u0000\u0000\u0551"+
		"\u0553\u0003\u008aE\u0000\u0552\u0551\u0001\u0000\u0000\u0000\u0552\u0553"+
		"\u0001\u0000\u0000\u0000\u0553\u0556\u0001\u0000\u0000\u0000\u0554\u0555"+
		"\u0005\u0014\u0000\u0000\u0555\u0557\u0003\u00eew\u0000\u0556\u0554\u0001"+
		"\u0000\u0000\u0000\u0556\u0557\u0001\u0000\u0000\u0000\u0557\u0558\u0001"+
		"\u0000\u0000\u0000\u0558\u0559\u0003\u0084B\u0000\u0559\u00af\u0001\u0000"+
		"\u0000\u0000\u055a\u055b\u0005\b\u0000\u0000\u055b\u055c\u0003\u00eew"+
		"\u0000\u055c\u055d\u0005\u0010\u0000\u0000\u055d\u0561\u0003\u008aE\u0000"+
		"\u055e\u055f\u0003\u00d8l\u0000\u055f\u0560\u0003\u0086C\u0000\u0560\u0562"+
		"\u0001\u0000\u0000\u0000\u0561\u055e\u0001\u0000\u0000\u0000\u0561\u0562"+
		"\u0001\u0000\u0000\u0000\u0562\u0563\u0001\u0000\u0000\u0000\u0563\u0564"+
		"\u0005\t\u0000\u0000\u0564\u00b1\u0001\u0000\u0000\u0000\u0565\u0566\u0005"+
		"t\u0000\u0000\u0566\u0569\u0003\u00dam\u0000\u0567\u0568\u0005\u0014\u0000"+
		"\u0000\u0568\u056a\u0003\u00eew\u0000\u0569\u0567\u0001\u0000\u0000\u0000"+
		"\u0569\u056a\u0001\u0000\u0000\u0000\u056a\u056b\u0001\u0000\u0000\u0000"+
		"\u056b\u056d\u0005\b\u0000\u0000\u056c\u056e\u0003\u0110\u0088\u0000\u056d"+
		"\u056c\u0001\u0000\u0000\u0000\u056d\u056e\u0001\u0000\u0000\u0000\u056e"+
		"\u056f\u0001\u0000\u0000\u0000\u056f\u0570\u0005\t\u0000\u0000\u0570\u00b3"+
		"\u0001\u0000\u0000\u0000\u0571\u0572\u0003\u00b6[\u0000\u0572\u0573\u0003"+
		"\u00e8t\u0000\u0573\u0575\u0005\b\u0000\u0000\u0574\u0576\u0003\u0110"+
		"\u0088\u0000\u0575\u0574\u0001\u0000\u0000\u0000\u0575\u0576\u0001\u0000"+
		"\u0000\u0000\u0576\u0577\u0001\u0000\u0000\u0000\u0577\u0578\u0005\t\u0000"+
		"\u0000\u0578\u00b5\u0001\u0000\u0000\u0000\u0579\u057a\u0007\u0013\u0000"+
		"\u0000\u057a\u00b7\u0001\u0000\u0000\u0000\u057b\u057d\u0005w\u0000\u0000"+
		"\u057c\u057b\u0001\u0000\u0000\u0000\u057c\u057d\u0001\u0000\u0000\u0000"+
		"\u057d\u057e\u0001\u0000\u0000\u0000\u057e\u057f\u0003\u00ba]\u0000\u057f"+
		"\u0581\u0005\b\u0000\u0000\u0580\u0582\u0003\u00c0`\u0000\u0581\u0580"+
		"\u0001\u0000\u0000\u0000\u0581\u0582\u0001\u0000\u0000\u0000\u0582\u0583"+
		"\u0001\u0000\u0000\u0000\u0583\u0584\u0005\t\u0000\u0000\u0584\u00b9\u0001"+
		"\u0000\u0000\u0000\u0585\u0586\u0007\u0014\u0000\u0000\u0586\u00bb\u0001"+
		"\u0000\u0000\u0000\u0587\u0589\u0005w\u0000\u0000\u0588\u0587\u0001\u0000"+
		"\u0000\u0000\u0588\u0589\u0001\u0000\u0000\u0000\u0589\u058a\u0001\u0000"+
		"\u0000\u0000\u058a\u058b\u0003\u00be_\u0000\u058b\u058c\u0005\b\u0000"+
		"\u0000\u058c\u058f\u0003\u00e8t\u0000\u058d\u058e\u0005\u0005\u0000\u0000"+
		"\u058e\u0590\u0003\u00c0`\u0000\u058f\u058d\u0001\u0000\u0000\u0000\u058f"+
		"\u0590\u0001\u0000\u0000\u0000\u0590\u0591\u0001\u0000\u0000\u0000\u0591"+
		"\u0592\u0005\t\u0000\u0000\u0592\u00bd\u0001\u0000\u0000\u0000\u0593\u0594"+
		"\u0007\u0015\u0000\u0000\u0594\u00bf\u0001\u0000\u0000\u0000\u0595\u0598"+
		"\u0003\u00c2a\u0000\u0596\u0597\u0005D\u0000\u0000\u0597\u0599\u0003\u0086"+
		"C\u0000\u0598\u0596\u0001\u0000\u0000\u0000\u0598\u0599\u0001\u0000\u0000"+
		"\u0000\u0599\u00c1\u0001\u0000\u0000\u0000\u059a\u059b\u0003\u00eew\u0000"+
		"\u059b\u059c\u0005\u0010\u0000\u0000\u059c\u059d\u0003\u008aE\u0000\u059d"+
		"\u05a0\u0001\u0000\u0000\u0000\u059e\u05a0\u0003\u008aE\u0000\u059f\u059a"+
		"\u0001\u0000\u0000\u0000\u059f\u059e\u0001\u0000\u0000\u0000\u05a0\u00c3"+
		"\u0001\u0000\u0000\u0000\u05a1\u05a2\u0005\u0080\u0000\u0000\u05a2\u05a4"+
		"\u0003\u0084B\u0000\u05a3\u05a5\u0003\u00c6c\u0000\u05a4\u05a3\u0001\u0000"+
		"\u0000\u0000\u05a5\u05a6\u0001\u0000\u0000\u0000\u05a6\u05a4\u0001\u0000"+
		"\u0000\u0000\u05a6\u05a7\u0001\u0000\u0000\u0000\u05a7\u00c5\u0001\u0000"+
		"\u0000\u0000\u05a8\u05b3\u0007\u0016\u0000\u0000\u05a9\u05b0\u0005\b\u0000"+
		"\u0000\u05aa\u05ab\u0003\u00eew\u0000\u05ab\u05ac\u0005\u0010\u0000\u0000"+
		"\u05ac\u05ae\u0001\u0000\u0000\u0000\u05ad\u05aa\u0001\u0000\u0000\u0000"+
		"\u05ad\u05ae\u0001\u0000\u0000\u0000\u05ae\u05af\u0001\u0000\u0000\u0000"+
		"\u05af\u05b1\u0003\u00c8d\u0000\u05b0\u05ad\u0001\u0000\u0000\u0000\u05b0"+
		"\u05b1\u0001\u0000\u0000\u0000\u05b1\u05b2\u0001\u0000\u0000\u0000\u05b2"+
		"\u05b4\u0005\t\u0000\u0000\u05b3\u05a9\u0001\u0000\u0000\u0000\u05b3\u05b4"+
		"\u0001\u0000\u0000\u0000\u05b4\u05b5\u0001\u0000\u0000\u0000\u05b5\u05b6"+
		"\u0003\u0084B\u0000\u05b6\u00c7\u0001\u0000\u0000\u0000\u05b7\u05bc\u0003"+
		"\u00dam\u0000\u05b8\u05b9\u0005\u0005\u0000\u0000\u05b9\u05bb\u0003\u00da"+
		"m\u0000\u05ba\u05b8\u0001\u0000\u0000\u0000\u05bb\u05be\u0001\u0000\u0000"+
		"\u0000\u05bc\u05ba\u0001\u0000\u0000\u0000\u05bc\u05bd\u0001\u0000\u0000"+
		"\u0000\u05bd\u00c9\u0001\u0000\u0000\u0000\u05be\u05bc\u0001\u0000\u0000"+
		"\u0000\u05bf\u05c9\u0005\u0083\u0000\u0000\u05c0\u05c6\u0003\u00dam\u0000"+
		"\u05c1\u05c3\u0005\b\u0000\u0000\u05c2\u05c4\u0003\u0086C\u0000\u05c3"+
		"\u05c2\u0001\u0000\u0000\u0000\u05c3\u05c4\u0001\u0000\u0000\u0000\u05c4"+
		"\u05c5\u0001\u0000\u0000\u0000\u05c5\u05c7\u0005\t\u0000\u0000\u05c6\u05c1"+
		"\u0001\u0000\u0000\u0000\u05c6\u05c7\u0001\u0000\u0000\u0000\u05c7\u05ca"+
		"\u0001\u0000\u0000\u0000\u05c8\u05ca\u0005\u00ac\u0000\u0000\u05c9\u05c0"+
		"\u0001\u0000\u0000\u0000\u05c9\u05c8\u0001\u0000\u0000\u0000\u05ca\u00cb"+
		"\u0001\u0000\u0000\u0000\u05cb\u05cd\u0005\u0084\u0000\u0000\u05cc\u05ce"+
		"\u0003\u00eew\u0000\u05cd\u05cc\u0001\u0000\u0000\u0000\u05cd\u05ce\u0001"+
		"\u0000\u0000\u0000\u05ce\u05cf\u0001\u0000\u0000\u0000\u05cf\u05d0\u0005"+
		"\b\u0000\u0000\u05d0\u05d1\u0003\u0086C\u0000\u05d1\u05d4\u0005\t\u0000"+
		"\u0000\u05d2\u05d3\u0005?\u0000\u0000\u05d3\u05d5\u0003\u00ceg\u0000\u05d4"+
		"\u05d2\u0001\u0000\u0000\u0000\u05d4\u05d5\u0001\u0000\u0000\u0000\u05d5"+
		"\u00cd\u0001\u0000\u0000\u0000\u05d6\u05d7\u0005\u0085\u0000\u0000\u05d7"+
		"\u05d9\u0005\b\u0000\u0000\u05d8\u05da\u0003\u0110\u0088\u0000\u05d9\u05d8"+
		"\u0001\u0000\u0000\u0000\u05d9\u05da\u0001\u0000\u0000\u0000\u05da\u05db"+
		"\u0001\u0000\u0000\u0000\u05db\u05de\u0005\t\u0000\u0000\u05dc\u05dd\u0005"+
		"\u001e\u0000\u0000\u05dd\u05df\u0003\u0086C\u0000\u05de\u05dc\u0001\u0000"+
		"\u0000\u0000\u05de\u05df\u0001\u0000\u0000\u0000\u05df\u00cf\u0001\u0000"+
		"\u0000\u0000\u05e0\u05e1\u0005\u0085\u0000\u0000\u05e1\u05e3\u0005\b\u0000"+
		"\u0000\u05e2\u05e4\u0003\u0110\u0088\u0000\u05e3\u05e2\u0001\u0000\u0000"+
		"\u0000\u05e3\u05e4\u0001\u0000\u0000\u0000\u05e4\u05e5\u0001\u0000\u0000"+
		"\u0000\u05e5\u05e8\u0005\t\u0000\u0000\u05e6\u05e7\u0005\u001e\u0000\u0000"+
		"\u05e7\u05e9\u0003\u0086C\u0000\u05e8\u05e6\u0001\u0000\u0000\u0000\u05e8"+
		"\u05e9\u0001\u0000\u0000\u0000\u05e9\u00d1\u0001\u0000\u0000\u0000\u05ea"+
		"\u05ec\u0005\u0086\u0000\u0000\u05eb\u05ed\u0003\u0086C\u0000\u05ec\u05eb"+
		"\u0001\u0000\u0000\u0000\u05ec\u05ed\u0001\u0000\u0000\u0000\u05ed\u00d3"+
		"\u0001\u0000\u0000\u0000\u05ee\u05ef\u0005\u0087\u0000\u0000\u05ef\u05f4"+
		"\u0003\u00d6k\u0000\u05f0\u05f1\u0005\u0005\u0000\u0000\u05f1\u05f3\u0003"+
		"\u00d6k\u0000\u05f2\u05f0\u0001\u0000\u0000\u0000\u05f3\u05f6\u0001\u0000"+
		"\u0000\u0000\u05f4\u05f2\u0001\u0000\u0000\u0000\u05f4\u05f5\u0001\u0000"+
		"\u0000\u0000\u05f5\u0604\u0001\u0000\u0000\u0000\u05f6\u05f4\u0001\u0000"+
		"\u0000\u0000\u05f7\u05f8\u0005\u0087\u0000\u0000\u05f8\u05f9\u0005\b\u0000"+
		"\u0000\u05f9\u05fe\u0003\u00d6k\u0000\u05fa\u05fb\u0005\u0005\u0000\u0000"+
		"\u05fb\u05fd\u0003\u00d6k\u0000\u05fc\u05fa\u0001\u0000\u0000\u0000\u05fd"+
		"\u0600\u0001\u0000\u0000\u0000\u05fe\u05fc\u0001\u0000\u0000\u0000\u05fe"+
		"\u05ff\u0001\u0000\u0000\u0000\u05ff\u0601\u0001\u0000\u0000\u0000\u0600"+
		"\u05fe\u0001\u0000\u0000\u0000\u0601\u0602\u0005\t\u0000\u0000\u0602\u0604"+
		"\u0001\u0000\u0000\u0000\u0603\u05ee\u0001\u0000\u0000\u0000\u0603\u05f7"+
		"\u0001\u0000\u0000\u0000\u0604\u00d5\u0001\u0000\u0000\u0000\u0605\u0608"+
		"\u0003\u00eew\u0000\u0606\u0607\u0005\u0010\u0000\u0000\u0607\u0609\u0003"+
		"\u008aE\u0000\u0608\u0606\u0001\u0000\u0000\u0000\u0608\u0609\u0001\u0000"+
		"\u0000\u0000\u0609\u060d\u0001\u0000\u0000\u0000\u060a\u060b\u0003\u00d8"+
		"l\u0000\u060b\u060c\u0003\u0086C\u0000\u060c\u060e\u0001\u0000\u0000\u0000"+
		"\u060d\u060a\u0001\u0000\u0000\u0000\u060d\u060e\u0001\u0000\u0000\u0000"+
		"\u060e\u00d7\u0001\u0000\u0000\u0000\u060f\u0610\u0007\u0017\u0000\u0000"+
		"\u0610\u00d9\u0001\u0000\u0000\u0000\u0611\u0616\u0003\u00eew\u0000\u0612"+
		"\u0613\u0005=\u0000\u0000\u0613\u0615\u0003\u00eew\u0000\u0614\u0612\u0001"+
		"\u0000\u0000\u0000\u0615\u0618\u0001\u0000\u0000\u0000\u0616\u0614\u0001"+
		"\u0000\u0000\u0000\u0616\u0617\u0001\u0000\u0000\u0000\u0617\u00db\u0001"+
		"\u0000\u0000\u0000\u0618\u0616\u0001\u0000\u0000\u0000\u0619\u061a\u0003"+
		"\u00b6[\u0000\u061a\u061b\u0003\u00e8t\u0000\u061b\u061d\u0005\b\u0000"+
		"\u0000\u061c\u061e\u0003\u0110\u0088\u0000\u061d\u061c\u0001\u0000\u0000"+
		"\u0000\u061d\u061e\u0001\u0000\u0000\u0000\u061e\u061f\u0001\u0000\u0000"+
		"\u0000\u061f\u0620\u0005\t\u0000\u0000\u0620\u0645\u0001\u0000\u0000\u0000"+
		"\u0621\u0623\u0005w\u0000\u0000\u0622\u0621\u0001\u0000\u0000\u0000\u0622"+
		"\u0623\u0001\u0000\u0000\u0000\u0623\u0624\u0001\u0000\u0000\u0000\u0624"+
		"\u0625\u0003\u00ba]\u0000\u0625\u0627\u0005\b\u0000\u0000\u0626\u0628"+
		"\u0003\u00c0`\u0000\u0627\u0626\u0001\u0000\u0000\u0000\u0627\u0628\u0001"+
		"\u0000\u0000\u0000\u0628\u0629\u0001\u0000\u0000\u0000\u0629\u062a\u0005"+
		"\t\u0000\u0000\u062a\u0645\u0001\u0000\u0000\u0000\u062b\u062d\u0005w"+
		"\u0000\u0000\u062c\u062b\u0001\u0000\u0000\u0000\u062c\u062d\u0001\u0000"+
		"\u0000\u0000\u062d\u062e\u0001\u0000\u0000\u0000\u062e\u062f\u0003\u00be"+
		"_\u0000\u062f\u0630\u0005\b\u0000\u0000\u0630\u0633\u0003\u00e8t\u0000"+
		"\u0631\u0632\u0005\u0005\u0000\u0000\u0632\u0634\u0003\u00c0`\u0000\u0633"+
		"\u0631\u0001\u0000\u0000\u0000\u0633\u0634\u0001\u0000\u0000\u0000\u0634"+
		"\u0635\u0001\u0000\u0000\u0000\u0635\u0636\u0005\t\u0000\u0000\u0636\u0645"+
		"\u0001\u0000\u0000\u0000\u0637\u0638\u0003\u00eew\u0000\u0638\u063a\u0005"+
		"\b\u0000\u0000\u0639\u063b\u0003\u0110\u0088\u0000\u063a\u0639\u0001\u0000"+
		"\u0000\u0000\u063a\u063b\u0001\u0000\u0000\u0000\u063b\u063c\u0001\u0000"+
		"\u0000\u0000\u063c\u063e\u0005\t\u0000\u0000\u063d\u063f\u0003\u0112\u0089"+
		"\u0000\u063e\u063d\u0001\u0000\u0000\u0000\u063e\u063f\u0001\u0000\u0000"+
		"\u0000\u063f\u0645\u0001\u0000\u0000\u0000\u0640\u0642\u0003\u00eew\u0000"+
		"\u0641\u0643\u0003\u0112\u0089\u0000\u0642\u0641\u0001\u0000\u0000\u0000"+
		"\u0642\u0643\u0001\u0000\u0000\u0000\u0643\u0645\u0001\u0000\u0000\u0000"+
		"\u0644\u0619\u0001\u0000\u0000\u0000\u0644\u0622\u0001\u0000\u0000\u0000"+
		"\u0644\u062c\u0001\u0000\u0000\u0000\u0644\u0637\u0001\u0000\u0000\u0000"+
		"\u0644\u0640\u0001\u0000\u0000\u0000\u0645\u00dd\u0001\u0000\u0000\u0000"+
		"\u0646\u0647\u0003\u00a2Q\u0000\u0647\u0648\u0005\b\u0000\u0000\u0648"+
		"\u064b\u0003\u00a4R\u0000\u0649\u064a\u0005D\u0000\u0000\u064a\u064c\u0003"+
		"\u0086C\u0000\u064b\u0649\u0001\u0000\u0000\u0000\u064b\u064c\u0001\u0000"+
		"\u0000\u0000\u064c\u064d\u0001\u0000\u0000\u0000\u064d\u064e\u0005\t\u0000"+
		"\u0000\u064e\u064f\u0003\u0084B\u0000\u064f\u0693\u0001\u0000\u0000\u0000"+
		"\u0650\u0652\u0005w\u0000\u0000\u0651\u0650\u0001\u0000\u0000\u0000\u0651"+
		"\u0652\u0001\u0000\u0000\u0000\u0652\u0653\u0001\u0000\u0000\u0000\u0653"+
		"\u0654\u0003\u00ba]\u0000\u0654\u0656\u0005\b\u0000\u0000\u0655\u0657"+
		"\u0003\u00c0`\u0000\u0656\u0655\u0001\u0000\u0000\u0000\u0656\u0657\u0001"+
		"\u0000\u0000\u0000\u0657\u0658\u0001\u0000\u0000\u0000\u0658\u0659\u0005"+
		"\t\u0000\u0000\u0659\u0693\u0001\u0000\u0000\u0000\u065a\u065c\u0005w"+
		"\u0000\u0000\u065b\u065a\u0001\u0000\u0000\u0000\u065b\u065c\u0001\u0000"+
		"\u0000\u0000\u065c\u065d\u0001\u0000\u0000\u0000\u065d\u065e\u0003\u00be"+
		"_\u0000\u065e\u065f\u0005\b\u0000\u0000\u065f\u0662\u0003\u00e8t\u0000"+
		"\u0660\u0661\u0005\u0005\u0000\u0000\u0661\u0663\u0003\u00c0`\u0000\u0662"+
		"\u0660\u0001\u0000\u0000\u0000\u0662\u0663\u0001\u0000\u0000\u0000\u0663"+
		"\u0664\u0001\u0000\u0000\u0000\u0664\u0665\u0005\t\u0000\u0000\u0665\u0693"+
		"\u0001\u0000\u0000\u0000\u0666\u0667\u0003\u00b6[\u0000\u0667\u0668\u0003"+
		"\u00e8t\u0000\u0668\u066a\u0005\b\u0000\u0000\u0669\u066b\u0003\u0110"+
		"\u0088\u0000\u066a\u0669\u0001\u0000\u0000\u0000\u066a\u066b\u0001\u0000"+
		"\u0000\u0000\u066b\u066c\u0001\u0000\u0000\u0000\u066c\u066d\u0005\t\u0000"+
		"\u0000\u066d\u0693\u0001\u0000\u0000\u0000\u066e\u0693\u0003\u00aeW\u0000"+
		"\u066f\u0693\u0003\u00a6S\u0000\u0670\u0671\u0003\u00eew\u0000\u0671\u0672"+
		"\u0005\b\u0000\u0000\u0672\u0673\u0003\u00e0p\u0000\u0673\u0674\u0005"+
		"D\u0000\u0000\u0674\u0675\u0003\u0086C\u0000\u0675\u0676\u0005\t\u0000"+
		"\u0000\u0676\u0693\u0001\u0000\u0000\u0000\u0677\u0678\u0003\u00eew\u0000"+
		"\u0678\u0679\u0005\b\u0000\u0000\u0679\u067c\u0003\u00eew\u0000\u067a"+
		"\u067b\u0005\u0010\u0000\u0000\u067b\u067d\u0003\u008aE\u0000\u067c\u067a"+
		"\u0001\u0000\u0000\u0000\u067c\u067d\u0001\u0000\u0000\u0000\u067d\u067e"+
		"\u0001\u0000\u0000\u0000\u067e\u067f\u0005\u0002\u0000\u0000\u067f\u0682"+
		"\u0003\u00eew\u0000\u0680\u0681\u0005\u0010\u0000\u0000\u0681\u0683\u0003"+
		"\u008aE\u0000\u0682\u0680\u0001\u0000\u0000\u0000\u0682\u0683\u0001\u0000"+
		"\u0000\u0000\u0683\u0684\u0001\u0000\u0000\u0000\u0684\u0685\u0005#\u0000"+
		"\u0000\u0685\u0686\u0003\u0086C\u0000\u0686\u0687\u0005D\u0000\u0000\u0687"+
		"\u0688\u0003\u0086C\u0000\u0688\u0689\u0005\t\u0000\u0000\u0689\u0693"+
		"\u0001\u0000\u0000\u0000\u068a\u068b\u0003\u00eew\u0000\u068b\u068d\u0005"+
		"\b\u0000\u0000\u068c\u068e\u0003\u0110\u0088\u0000\u068d\u068c\u0001\u0000"+
		"\u0000\u0000\u068d\u068e\u0001\u0000\u0000\u0000\u068e\u068f\u0001\u0000"+
		"\u0000\u0000\u068f\u0690\u0005\t\u0000\u0000\u0690\u0693\u0001\u0000\u0000"+
		"\u0000\u0691\u0693\u0003\u00eew\u0000\u0692\u0646\u0001\u0000\u0000\u0000"+
		"\u0692\u0651\u0001\u0000\u0000\u0000\u0692\u065b\u0001\u0000\u0000\u0000"+
		"\u0692\u0666\u0001\u0000\u0000\u0000\u0692\u066e\u0001\u0000\u0000\u0000"+
		"\u0692\u066f\u0001\u0000\u0000\u0000\u0692\u0670\u0001\u0000\u0000\u0000"+
		"\u0692\u0677\u0001\u0000\u0000\u0000\u0692\u068a\u0001\u0000\u0000\u0000"+
		"\u0692\u0691\u0001\u0000\u0000\u0000\u0693\u00df\u0001\u0000\u0000\u0000"+
		"\u0694\u0697\u0003\u00eew\u0000\u0695\u0696\u0005\u0010\u0000\u0000\u0696"+
		"\u0698\u0003\u008aE\u0000\u0697\u0695\u0001\u0000\u0000\u0000\u0697\u0698"+
		"\u0001\u0000\u0000\u0000\u0698\u06a1\u0001\u0000\u0000\u0000\u0699\u069a"+
		"\u0005\u0005\u0000\u0000\u069a\u069d\u0003\u00eew\u0000\u069b\u069c\u0005"+
		"\u0010\u0000\u0000\u069c\u069e\u0003\u008aE\u0000\u069d\u069b\u0001\u0000"+
		"\u0000\u0000\u069d\u069e\u0001\u0000\u0000\u0000\u069e\u06a0\u0001\u0000"+
		"\u0000\u0000\u069f\u0699\u0001\u0000\u0000\u0000\u06a0\u06a3\u0001\u0000"+
		"\u0000\u0000\u06a1\u069f\u0001\u0000\u0000\u0000\u06a1\u06a2\u0001\u0000"+
		"\u0000\u0000\u06a2\u00e1\u0001\u0000\u0000\u0000\u06a3\u06a1\u0001\u0000"+
		"\u0000\u0000\u06a4\u06a7\u0003\u00eew\u0000\u06a5\u06a6\u0005\u0010\u0000"+
		"\u0000\u06a6\u06a8\u0003\u008aE\u0000\u06a7\u06a5\u0001\u0000\u0000\u0000"+
		"\u06a7\u06a8\u0001\u0000\u0000\u0000\u06a8\u06a9\u0001\u0000\u0000\u0000"+
		"\u06a9\u06aa\u0005#\u0000\u0000\u06aa\u06ab\u0003\u0086C\u0000\u06ab\u00e3"+
		"\u0001\u0000\u0000\u0000\u06ac\u06ad\u0003\u00eew\u0000\u06ad\u06ae\u0005"+
		"\u0010\u0000\u0000\u06ae\u06af\u0003\u008aE\u0000\u06af\u00e5\u0001\u0000"+
		"\u0000\u0000\u06b0\u06b3\u0003\u00eew\u0000\u06b1\u06b2\u0005\u0010\u0000"+
		"\u0000\u06b2\u06b4\u0003\u008aE\u0000\u06b3\u06b1\u0001\u0000\u0000\u0000"+
		"\u06b3\u06b4\u0001\u0000\u0000\u0000\u06b4\u06b5\u0001\u0000\u0000\u0000"+
		"\u06b5\u06b6\u0005#\u0000\u0000\u06b6\u06b7\u0003\u0086C\u0000\u06b7\u00e7"+
		"\u0001\u0000\u0000\u0000\u06b8\u06b9\u0003\u011e\u008f\u0000\u06b9\u06ba"+
		"\u0005=\u0000\u0000\u06ba\u06bb\u0003\u00eew\u0000\u06bb\u06c9\u0001\u0000"+
		"\u0000\u0000\u06bc\u06bd\u0003\u0120\u0090\u0000\u06bd\u06be\u0005=\u0000"+
		"\u0000\u06be\u06bf\u0003\u00eew\u0000\u06bf\u06c9\u0001\u0000\u0000\u0000"+
		"\u06c0\u06c5\u0003\u00eew\u0000\u06c1\u06c2\u0005=\u0000\u0000\u06c2\u06c4"+
		"\u0003\u00eew\u0000\u06c3\u06c1\u0001\u0000\u0000\u0000\u06c4\u06c7\u0001"+
		"\u0000\u0000\u0000\u06c5\u06c3\u0001\u0000\u0000\u0000\u06c5\u06c6\u0001"+
		"\u0000\u0000\u0000\u06c6\u06c9\u0001\u0000\u0000\u0000\u06c7\u06c5\u0001"+
		"\u0000\u0000\u0000\u06c8\u06b8\u0001\u0000\u0000\u0000\u06c8\u06bc\u0001"+
		"\u0000\u0000\u0000\u06c8\u06c0\u0001\u0000\u0000\u0000\u06c9\u00e9\u0001"+
		"\u0000\u0000\u0000\u06ca\u06cf\u0003\u00e8t\u0000\u06cb\u06cc\u0005\u0005"+
		"\u0000\u0000\u06cc\u06ce\u0003\u00e8t\u0000\u06cd\u06cb\u0001\u0000\u0000"+
		"\u0000\u06ce\u06d1\u0001\u0000\u0000\u0000\u06cf\u06cd\u0001\u0000\u0000"+
		"\u0000\u06cf\u06d0\u0001\u0000\u0000\u0000\u06d0\u00eb\u0001\u0000\u0000"+
		"\u0000\u06d1\u06cf\u0001\u0000\u0000\u0000\u06d2\u06d7\u0003\u00eew\u0000"+
		"\u06d3\u06d4\u0005@\u0000\u0000\u06d4\u06d6\u0003\u00eew\u0000\u06d5\u06d3"+
		"\u0001\u0000\u0000\u0000\u06d6\u06d9\u0001\u0000\u0000\u0000\u06d7\u06d5"+
		"\u0001\u0000\u0000\u0000\u06d7\u06d8\u0001\u0000\u0000\u0000\u06d8\u00ed"+
		"\u0001\u0000\u0000\u0000\u06d9\u06d7\u0001\u0000\u0000\u0000\u06da\u06db"+
		"\u0007\u0018\u0000\u0000\u06db\u00ef\u0001\u0000\u0000\u0000\u06dc\u06dd"+
		"\u0003\u0086C\u0000\u06dd\u06de\u0005\u0000\u0000\u0001\u06de\u00f1\u0001"+
		"\u0000\u0000\u0000\u06df\u06e0\u0003\u00f4z\u0000\u06e0\u06e1\u0005\u0000"+
		"\u0000\u0001\u06e1\u00f3\u0001\u0000\u0000\u0000\u06e2\u06e4\u0003\u00f6"+
		"{\u0000\u06e3\u06e2\u0001\u0000\u0000\u0000\u06e4\u06e7\u0001\u0000\u0000"+
		"\u0000\u06e5\u06e3\u0001\u0000\u0000\u0000\u06e5\u06e6\u0001\u0000\u0000"+
		"\u0000\u06e6\u06ec\u0001\u0000\u0000\u0000\u06e7\u06e5\u0001\u0000\u0000"+
		"\u0000\u06e8\u06eb\u0003\u00f8|\u0000\u06e9\u06eb\u0003\u00fa}\u0000\u06ea"+
		"\u06e8\u0001\u0000\u0000\u0000\u06ea\u06e9\u0001\u0000\u0000\u0000\u06eb"+
		"\u06ee\u0001\u0000\u0000\u0000\u06ec\u06ea\u0001\u0000\u0000\u0000\u06ec"+
		"\u06ed\u0001\u0000\u0000\u0000\u06ed\u00f5\u0001\u0000\u0000\u0000\u06ee"+
		"\u06ec\u0001\u0000\u0000\u0000\u06ef\u06f2\u0007\u0019\u0000\u0000\u06f0"+
		"\u06f1\u0005\u00ad\u0000\u0000\u06f1\u06f3\u0005\u0010\u0000\u0000\u06f2"+
		"\u06f0\u0001\u0000\u0000\u0000\u06f2\u06f3\u0001\u0000\u0000\u0000\u06f3"+
		"\u06f4\u0001\u0000\u0000\u0000\u06f4\u06f7\u0003\u00dam\u0000\u06f5\u06f6"+
		"\u0005=\u0000\u0000\u06f6\u06f8\u0005\u0004\u0000\u0000\u06f7\u06f5\u0001"+
		"\u0000\u0000\u0000\u06f7\u06f8\u0001\u0000\u0000\u0000\u06f8\u00f7\u0001"+
		"\u0000\u0000\u0000\u06f9\u06fa\u00051\u0000\u0000\u06fa\u06fe\u0003\u00da"+
		"m\u0000\u06fb\u06fd\u0003\u00fa}\u0000\u06fc\u06fb\u0001\u0000\u0000\u0000"+
		"\u06fd\u0700\u0001\u0000\u0000\u0000\u06fe\u06fc\u0001\u0000\u0000\u0000"+
		"\u06fe\u06ff\u0001\u0000\u0000\u0000\u06ff\u0701\u0001\u0000\u0000\u0000"+
		"\u0700\u06fe\u0001\u0000\u0000\u0000\u0701\u0702\u0005\u008b\u0000\u0000"+
		"\u0702\u00f9\u0001\u0000\u0000\u0000\u0703\u0707\u0003\u00fc~\u0000\u0704"+
		"\u0707\u0003\u0104\u0082\u0000\u0705\u0707\u0003\u0108\u0084\u0000\u0706"+
		"\u0703\u0001\u0000\u0000\u0000\u0706\u0704\u0001\u0000\u0000\u0000\u0706"+
		"\u0705\u0001\u0000\u0000\u0000\u0707\u00fb\u0001\u0000\u0000\u0000\u0708"+
		"\u0709\u0005\u008c\u0000\u0000\u0709\u070b\u0003\u00dam\u0000\u070a\u070c"+
		"\u0003\u00fe\u007f\u0000\u070b\u070a\u0001\u0000\u0000\u0000\u070c\u070d"+
		"\u0001\u0000\u0000\u0000\u070d\u070b\u0001\u0000\u0000\u0000\u070d\u070e"+
		"\u0001\u0000\u0000\u0000\u070e\u00fd\u0001\u0000\u0000\u0000\u070f\u0712"+
		"\u0003\u0100\u0080\u0000\u0710\u0712\u0003\u0102\u0081\u0000\u0711\u070f"+
		"\u0001\u0000\u0000\u0000\u0711\u0710\u0001\u0000\u0000\u0000\u0712\u00ff"+
		"\u0001\u0000\u0000\u0000\u0713\u071b\u0005\u0088\u0000\u0000\u0714\u0719"+
		"\u0005\u00ad\u0000\u0000\u0715\u0716\u0005\b\u0000\u0000\u0716\u0717\u0003"+
		"\u0086C\u0000\u0717\u0718\u0005\t\u0000\u0000\u0718\u071a\u0001\u0000"+
		"\u0000\u0000\u0719\u0715\u0001\u0000\u0000\u0000\u0719\u071a\u0001\u0000"+
		"\u0000\u0000\u071a\u071c\u0001\u0000\u0000\u0000\u071b\u0714\u0001\u0000"+
		"\u0000\u0000\u071b\u071c\u0001\u0000\u0000\u0000\u071c\u071d\u0001\u0000"+
		"\u0000\u0000\u071d\u071e\u0005\u0010\u0000\u0000\u071e\u071f\u0003\u0086"+
		"C\u0000\u071f\u0101\u0001\u0000\u0000\u0000\u0720\u0722\u0005\u0019\u0000"+
		"\u0000\u0721\u0720\u0001\u0000\u0000\u0000\u0721\u0722\u0001\u0000\u0000"+
		"\u0000\u0722\u0723\u0001\u0000\u0000\u0000\u0723\u0725\u0005\u008d\u0000"+
		"\u0000\u0724\u0726\u0005\u00ad\u0000\u0000\u0725\u0724\u0001\u0000\u0000"+
		"\u0000\u0725\u0726\u0001\u0000\u0000\u0000\u0726\u0727\u0001\u0000\u0000"+
		"\u0000\u0727\u0739\u0005\u0010\u0000\u0000\u0728\u0729\u0005\u00ad\u0000"+
		"\u0000\u0729\u072a\u0005\u0010\u0000\u0000\u072a\u072b\u0003\u008aE\u0000"+
		"\u072b\u072c\u0005#\u0000\u0000\u072c\u072d\u0003\u0086C\u0000\u072d\u073a"+
		"\u0001\u0000\u0000\u0000\u072e\u072f\u0005\u00ad\u0000\u0000\u072f\u0731"+
		"\u0005\b\u0000\u0000\u0730\u0732\u0003\u010c\u0086\u0000\u0731\u0730\u0001"+
		"\u0000\u0000\u0000\u0731\u0732\u0001\u0000\u0000\u0000\u0732\u0733\u0001"+
		"\u0000\u0000\u0000\u0733\u0734\u0005\t\u0000\u0000\u0734\u0735\u0005\u0010"+
		"\u0000\u0000\u0735\u0736\u0003\u008aE\u0000\u0736\u0737\u0005#\u0000\u0000"+
		"\u0737\u0738\u0003\u0086C\u0000\u0738\u073a\u0001\u0000\u0000\u0000\u0739"+
		"\u0728\u0001\u0000\u0000\u0000\u0739\u072e\u0001\u0000\u0000\u0000\u073a"+
		"\u0103\u0001\u0000\u0000\u0000\u073b\u073c\u0005\u008c\u0000\u0000\u073c"+
		"\u073d\u0003\u00dam\u0000\u073d\u073e\u0005=\u0000\u0000\u073e\u073f\u0005"+
		"\u00ad\u0000\u0000\u073f\u0741\u0005\b\u0000\u0000\u0740\u0742\u0003\u010c"+
		"\u0086\u0000\u0741\u0740\u0001\u0000\u0000\u0000\u0741\u0742\u0001\u0000"+
		"\u0000\u0000\u0742\u0743\u0001\u0000\u0000\u0000\u0743\u0744\u0005\t\u0000"+
		"\u0000\u0744\u0745\u0005\u0010\u0000\u0000\u0745\u0747\u0003\u008aE\u0000"+
		"\u0746\u0748\u0003\u0106\u0083\u0000\u0747\u0746\u0001\u0000\u0000\u0000"+
		"\u0748\u0749\u0001\u0000\u0000\u0000\u0749\u0747\u0001\u0000\u0000\u0000"+
		"\u0749\u074a\u0001\u0000\u0000\u0000\u074a\u0105\u0001\u0000\u0000\u0000"+
		"\u074b\u074d\u0005\u008e\u0000\u0000\u074c\u074e\u0005\u00ad\u0000\u0000"+
		"\u074d\u074c\u0001\u0000\u0000\u0000\u074d\u074e\u0001\u0000\u0000\u0000"+
		"\u074e\u074f\u0001\u0000\u0000\u0000\u074f\u0750\u0005\u0010\u0000\u0000"+
		"\u0750\u075e\u0003\u0086C\u0000\u0751\u0753\u0005\u008f\u0000\u0000\u0752"+
		"\u0754\u0005\u00ad\u0000\u0000\u0753\u0752\u0001\u0000\u0000\u0000\u0753"+
		"\u0754\u0001\u0000\u0000\u0000\u0754\u0755\u0001\u0000\u0000\u0000\u0755"+
		"\u0756\u0005\u0010\u0000\u0000\u0756\u075e\u0003\u0086C\u0000\u0757\u0759"+
		"\u0005\u0090\u0000\u0000\u0758\u075a\u0005\u00ad\u0000\u0000\u0759\u0758"+
		"\u0001\u0000\u0000\u0000\u0759\u075a\u0001\u0000\u0000\u0000\u075a\u075b"+
		"\u0001\u0000\u0000\u0000\u075b\u075c\u0005\u0010\u0000\u0000\u075c\u075e"+
		"\u0003\u0086C\u0000\u075d\u074b\u0001\u0000\u0000\u0000\u075d\u0751\u0001"+
		"\u0000\u0000\u0000\u075d\u0757\u0001\u0000\u0000\u0000\u075e\u0107\u0001"+
		"\u0000\u0000\u0000\u075f\u0760\u0005\u008c\u0000\u0000\u0760\u0761\u0003"+
		"\u00dam\u0000\u0761\u0762\u0005=\u0000\u0000\u0762\u0763\u0005\u00ad\u0000"+
		"\u0000\u0763\u0764\u0005\u0010\u0000\u0000\u0764\u0766\u0003\u008aE\u0000"+
		"\u0765\u0767\u0003\u010a\u0085\u0000\u0766\u0765\u0001\u0000\u0000\u0000"+
		"\u0767\u0768\u0001\u0000\u0000\u0000\u0768\u0766\u0001\u0000\u0000\u0000"+
		"\u0768\u0769\u0001\u0000\u0000\u0000\u0769\u0109\u0001\u0000\u0000\u0000"+
		"\u076a\u076c\u0005\u001f\u0000\u0000\u076b\u076d\u0005\u00ad\u0000\u0000"+
		"\u076c\u076b\u0001\u0000\u0000\u0000\u076c\u076d\u0001\u0000\u0000\u0000"+
		"\u076d\u076e\u0001\u0000\u0000\u0000\u076e\u076f\u0005\u0010\u0000\u0000"+
		"\u076f\u0777\u0003\u0086C\u0000\u0770\u0772\u0005\u0091\u0000\u0000\u0771"+
		"\u0773\u0005\u00ad\u0000\u0000\u0772\u0771\u0001\u0000\u0000\u0000\u0772"+
		"\u0773\u0001\u0000\u0000\u0000\u0773\u0774\u0001\u0000\u0000\u0000\u0774"+
		"\u0775\u0005\u0010\u0000\u0000\u0775\u0777\u0003\u0086C\u0000\u0776\u076a"+
		"\u0001\u0000\u0000\u0000\u0776\u0770\u0001\u0000\u0000\u0000\u0777\u010b"+
		"\u0001\u0000\u0000\u0000\u0778\u077d\u0003\u010e\u0087\u0000\u0779\u077a"+
		"\u0005\u0005\u0000\u0000\u077a\u077c\u0003\u010e\u0087\u0000\u077b\u0779"+
		"\u0001\u0000\u0000\u0000\u077c\u077f\u0001\u0000\u0000\u0000\u077d\u077b"+
		"\u0001\u0000\u0000\u0000\u077d\u077e\u0001\u0000\u0000\u0000\u077e\u010d"+
		"\u0001\u0000\u0000\u0000\u077f\u077d\u0001\u0000\u0000\u0000\u0780\u0781"+
		"\u0005\u00ad\u0000\u0000\u0781\u0782\u0005\u0010\u0000\u0000\u0782\u0783"+
		"\u0003\u008aE\u0000\u0783\u010f\u0001\u0000\u0000\u0000\u0784\u0789\u0003"+
		"\u0086C\u0000\u0785\u0786\u0005\u0005\u0000\u0000\u0786\u0788\u0003\u0086"+
		"C\u0000\u0787\u0785\u0001\u0000\u0000\u0000\u0788\u078b\u0001\u0000\u0000"+
		"\u0000\u0789\u0787\u0001\u0000\u0000\u0000\u0789\u078a\u0001\u0000\u0000"+
		"\u0000\u078a\u0111\u0001\u0000\u0000\u0000\u078b\u0789\u0001\u0000\u0000"+
		"\u0000\u078c\u078d\u0005\u0014\u0000\u0000\u078d\u078e\u0005\u008e\u0000"+
		"\u0000\u078e\u0113\u0001\u0000\u0000\u0000\u078f\u0790\u0003\u0090H\u0000"+
		"\u0790\u0799\u0005\u000b\u0000\u0000\u0791\u0796\u0003\u0116\u008b\u0000"+
		"\u0792\u0793\u0005\u0005\u0000\u0000\u0793\u0795\u0003\u0116\u008b\u0000"+
		"\u0794\u0792\u0001\u0000\u0000\u0000\u0795\u0798\u0001\u0000\u0000\u0000"+
		"\u0796\u0794\u0001\u0000\u0000\u0000\u0796\u0797\u0001\u0000\u0000\u0000"+
		"\u0797\u079a\u0001\u0000\u0000\u0000\u0798\u0796\u0001\u0000\u0000\u0000"+
		"\u0799\u0791\u0001\u0000\u0000\u0000\u0799\u079a\u0001\u0000\u0000\u0000"+
		"\u079a\u079b\u0001\u0000\u0000\u0000\u079b\u079c\u0005\f\u0000\u0000\u079c"+
		"\u0115\u0001\u0000\u0000\u0000\u079d\u07a0\u0003\u0086C\u0000\u079e\u079f"+
		"\u00059\u0000\u0000\u079f\u07a1\u0003\u0086C\u0000\u07a0\u079e\u0001\u0000"+
		"\u0000\u0000\u07a0\u07a1\u0001\u0000\u0000\u0000\u07a1\u0117\u0001\u0000"+
		"\u0000\u0000\u07a2\u07a3\u0005\u0092\u0000\u0000\u07a3\u07a4\u0005\u000b"+
		"\u0000\u0000\u07a4\u07a9\u0003\u00e6s\u0000\u07a5\u07a6\u0005\u0005\u0000"+
		"\u0000\u07a6\u07a8\u0003\u00e6s\u0000\u07a7\u07a5\u0001\u0000\u0000\u0000"+
		"\u07a8\u07ab\u0001\u0000\u0000\u0000\u07a9\u07a7\u0001\u0000\u0000\u0000"+
		"\u07a9\u07aa\u0001\u0000\u0000\u0000\u07aa\u07ac\u0001\u0000\u0000\u0000"+
		"\u07ab\u07a9\u0001\u0000\u0000\u0000\u07ac\u07ad\u0005\f\u0000\u0000\u07ad"+
		"\u0119\u0001\u0000\u0000\u0000\u07ae\u07af\u0005\u0093\u0000\u0000\u07af"+
		"\u07b8\u0005\u000b\u0000\u0000\u07b0\u07b5\u0003\u011c\u008e\u0000\u07b1"+
		"\u07b2\u0005\u0005\u0000\u0000\u07b2\u07b4\u0003\u011c\u008e\u0000\u07b3"+
		"\u07b1\u0001\u0000\u0000\u0000\u07b4\u07b7\u0001\u0000\u0000\u0000\u07b5"+
		"\u07b3\u0001\u0000\u0000\u0000\u07b5\u07b6\u0001\u0000\u0000\u0000\u07b6"+
		"\u07b9\u0001\u0000\u0000\u0000\u07b7\u07b5\u0001\u0000\u0000\u0000\u07b8"+
		"\u07b0\u0001\u0000\u0000\u0000\u07b8\u07b9\u0001\u0000\u0000\u0000\u07b9"+
		"\u07ba\u0001\u0000\u0000\u0000\u07ba\u07bb\u0005\f\u0000\u0000\u07bb\u011b"+
		"\u0001\u0000\u0000\u0000\u07bc\u07bd\u0003\u0086C\u0000\u07bd\u07be\u0007"+
		"\u001a\u0000\u0000\u07be\u07bf\u0003\u0086C\u0000\u07bf\u011d\u0001\u0000"+
		"\u0000\u0000\u07c0\u07c1\u0007\u001b\u0000\u0000\u07c1\u011f\u0001\u0000"+
		"\u0000\u0000\u07c2\u07c3\u0003\u0090H\u0000\u07c3\u07c4\u0005\b\u0000"+
		"\u0000\u07c4\u07c5\u0003\u008aE\u0000\u07c5\u07c6\u0005\t\u0000\u0000"+
		"\u07c6\u0121\u0001\u0000\u0000\u0000\u07c7\u07c8\u0005\u0093\u0000\u0000"+
		"\u07c8\u07c9\u0005\b\u0000\u0000\u07c9\u07ca\u0003\u008aE\u0000\u07ca"+
		"\u07cb\u0005\u0005\u0000\u0000\u07cb\u07cc\u0003\u008aE\u0000\u07cc\u07cd"+
		"\u0005\t\u0000\u0000\u07cd\u0123\u0001\u0000\u0000\u0000\u07ce\u07cf\u0005"+
		"\u0092\u0000\u0000\u07cf\u07d0\u0005\b\u0000\u0000\u07d0\u07d5\u0003\u00e4"+
		"r\u0000\u07d1\u07d2\u0005\u0005\u0000\u0000\u07d2\u07d4\u0003\u00e4r\u0000"+
		"\u07d3\u07d1\u0001\u0000\u0000\u0000\u07d4\u07d7\u0001\u0000\u0000\u0000"+
		"\u07d5\u07d3\u0001\u0000\u0000\u0000\u07d5\u07d6\u0001\u0000\u0000\u0000"+
		"\u07d6\u07d8\u0001\u0000\u0000\u0000\u07d7\u07d5\u0001\u0000\u0000\u0000"+
		"\u07d8\u07d9\u0005\t\u0000\u0000\u07d9\u0125\u0001\u0000\u0000\u0000\u00e8"+
		"\u012c\u0135\u0141\u0149\u014c\u0151\u0156\u015f\u0166\u0169\u0172\u017a"+
		"\u0181\u0187\u018b\u0191\u0196\u0199\u01a1\u01a6\u01ad\u01b2\u01b9\u01c4"+
		"\u01cb\u01cf\u01da\u01df\u01e3\u01fd\u0202\u0207\u020e\u0212\u0215\u0219"+
		"\u021e\u0223\u022a\u022f\u0233\u0238\u023f\u0247\u024e\u0252\u025d\u026d"+
		"\u0271\u0277\u027b\u027e\u028e\u0296\u029e\u02a3\u02b1\u02b6\u02be\u02c1"+
		"\u02c7\u02cb\u02cf\u02d1\u02d5\u02e0\u02eb\u02f5\u02ff\u0306\u030c\u0311"+
		"\u0317\u031d\u0322\u032a\u032f\u0339\u033d\u0344\u034a\u0352\u0357\u0361"+
		"\u0368\u036e\u037a\u0381\u0384\u0387\u038b\u038f\u0397\u039f\u03ad\u03b1"+
		"\u03b5\u03bc\u03c3\u03cc\u03d1\u03da\u03e2\u03f3\u0418\u0423\u042d\u042f"+
		"\u0431\u0445\u044a\u045b\u0460\u0463\u046b\u0487\u0490\u0499\u04b5\u04ba"+
		"\u04c3\u04c6\u04d5\u04dc\u04e4\u04e8\u04fc\u0503\u0512\u0519\u0520\u0526"+
		"\u052d\u0534\u0539\u054a\u054f\u0552\u0556\u0561\u0569\u056d\u0575\u057c"+
		"\u0581\u0588\u058f\u0598\u059f\u05a6\u05ad\u05b0\u05b3\u05bc\u05c3\u05c6"+
		"\u05c9\u05cd\u05d4\u05d9\u05de\u05e3\u05e8\u05ec\u05f4\u05fe\u0603\u0608"+
		"\u060d\u0616\u061d\u0622\u0627\u062c\u0633\u063a\u063e\u0642\u0644\u064b"+
		"\u0651\u0656\u065b\u0662\u066a\u067c\u0682\u068d\u0692\u0697\u069d\u06a1"+
		"\u06a7\u06b3\u06c5\u06c8\u06cf\u06d7\u06e5\u06ea\u06ec\u06f2\u06f7\u06fe"+
		"\u0706\u070d\u0711\u0719\u071b\u0721\u0725\u0731\u0739\u0741\u0749\u074d"+
		"\u0753\u0759\u075d\u0768\u076c\u0772\u0776\u077d\u0789\u0796\u0799\u07a0"+
		"\u07a9\u07b5\u07b8\u07d5";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}