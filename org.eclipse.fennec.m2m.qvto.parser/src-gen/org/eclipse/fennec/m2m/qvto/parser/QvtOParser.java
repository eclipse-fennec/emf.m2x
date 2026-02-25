// Generated from /opt/git/m2m/workspace/org.eclipse.fennec.m2m.qvto.parser/grammar/QvtO.g4 by ANTLR 4.13.2
package org.eclipse.fennec.m2m.qvto.parser;
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
		T__143=144, RESET_ASSIGN=145, ADD_ASSIGN=146, ORDERED_COPY=147, STEREOTYPE_ID=148, 
		DOUBLE_QUOTED_STRING=149, REAL_LITERAL=150, INTEGER_LITERAL=151, STRING_LITERAL=152, 
		IDENTIFIER=153, WS=154, LINE_COMMENT=155, BLOCK_COMMENT=156;
	public static final int
		RULE_compilationUnitEntry = 0, RULE_compilationUnit = 1, RULE_unitElement = 2, 
		RULE_qvtoImportDecl = 3, RULE_modeltypeDecl = 4, RULE_packageRefList = 5, 
		RULE_packageRef = 6, RULE_modeltypeWhere = 7, RULE_transformationDef = 8, 
		RULE_libraryDef = 9, RULE_modelParamList = 10, RULE_modelParam = 11, RULE_directionKind = 12, 
		RULE_typeSpec = 13, RULE_moduleUsage = 14, RULE_moduleUsageKind = 15, 
		RULE_moduleRefList = 16, RULE_qualifier = 17, RULE_moduleElement = 18, 
		RULE_mappingDef = 19, RULE_mappingSignature = 20, RULE_resultList = 21, 
		RULE_resultParam = 22, RULE_paramList = 23, RULE_param = 24, RULE_mappingExtension = 25, 
		RULE_mappingExtensionKind = 26, RULE_whenClause = 27, RULE_whereClause = 28, 
		RULE_guardBlock = 29, RULE_mappingBody = 30, RULE_initSection = 31, RULE_populationSection = 32, 
		RULE_endSection = 33, RULE_helperDef = 34, RULE_queryDef = 35, RULE_constructorDef = 36, 
		RULE_entryDef = 37, RULE_simpleSignature = 38, RULE_propertyDecl = 39, 
		RULE_intermediateClassDef = 40, RULE_typeList = 41, RULE_classifierFeature = 42, 
		RULE_classifierFeatureModifier = 43, RULE_tagDecl = 44, RULE_tagTarget = 45, 
		RULE_typedefDecl = 46, RULE_statementList = 47, RULE_statement = 48, RULE_assignOp = 49, 
		RULE_block = 50, RULE_expression = 51, RULE_primaryExpression = 52, RULE_typeExpression = 53, 
		RULE_listType = 54, RULE_dictType = 55, RULE_collectionKind = 56, RULE_literalExpression = 57, 
		RULE_stringLiteral_ = 58, RULE_dictLiteral = 59, RULE_dictLiteralPart = 60, 
		RULE_blockExp = 61, RULE_whileExp = 62, RULE_forExp = 63, RULE_forKind = 64, 
		RULE_forVarList = 65, RULE_switchExp = 66, RULE_switchAlt = 67, RULE_computeExp = 68, 
		RULE_objectExp = 69, RULE_newExp = 70, RULE_mappingCallExp = 71, RULE_mappingCallKind = 72, 
		RULE_resolveExp = 73, RULE_resolveKind = 74, RULE_resolveInExp = 75, RULE_resolveInKind = 76, 
		RULE_resolveArgs = 77, RULE_resolveTarget = 78, RULE_tryExp = 79, RULE_catchClause = 80, 
		RULE_exceptionTypeList = 81, RULE_raiseExp = 82, RULE_assertExp = 83, 
		RULE_logCallExp = 84, RULE_logExp = 85, RULE_returnExp = 86, RULE_varDeclExp = 87, 
		RULE_varDeclarator = 88, RULE_varInitOp = 89, RULE_pathName = 90, RULE_propertyOrCallSuffix = 91, 
		RULE_iteratorOrOperationCall = 92, RULE_iteratorVariables = 93, RULE_letBinding = 94, 
		RULE_tupleTypePart = 95, RULE_tupleLiteralPart = 96, RULE_scopedName = 97, 
		RULE_scopedNameList = 98, RULE_qualifiedName = 99, RULE_qvtoIdentifier = 100, 
		RULE_expressionEntry = 101, RULE_completeOclDocumentEntry = 102, RULE_completeOclDocument = 103, 
		RULE_importDeclaration = 104, RULE_packageDeclaration = 105, RULE_contextDeclaration = 106, 
		RULE_classifierContextDeclaration = 107, RULE_classifierContextBody = 108, 
		RULE_invariantConstraint = 109, RULE_definitionConstraint = 110, RULE_operationContextDeclaration = 111, 
		RULE_operationContextBody = 112, RULE_propertyContextDeclaration = 113, 
		RULE_propertyContextBody = 114, RULE_parameterList = 115, RULE_parameter = 116, 
		RULE_argumentList = 117, RULE_isMarkedPre = 118, RULE_collectionLiteral = 119, 
		RULE_collectionLiteralPart = 120, RULE_tupleLiteral = 121, RULE_mapLiteral = 122, 
		RULE_mapLiteralPart = 123, RULE_primitiveType = 124, RULE_collectionType = 125, 
		RULE_mapType = 126, RULE_tupleType = 127;
	private static String[] makeRuleNames() {
		return new String[] {
			"compilationUnitEntry", "compilationUnit", "unitElement", "qvtoImportDecl", 
			"modeltypeDecl", "packageRefList", "packageRef", "modeltypeWhere", "transformationDef", 
			"libraryDef", "modelParamList", "modelParam", "directionKind", "typeSpec", 
			"moduleUsage", "moduleUsageKind", "moduleRefList", "qualifier", "moduleElement", 
			"mappingDef", "mappingSignature", "resultList", "resultParam", "paramList", 
			"param", "mappingExtension", "mappingExtensionKind", "whenClause", "whereClause", 
			"guardBlock", "mappingBody", "initSection", "populationSection", "endSection", 
			"helperDef", "queryDef", "constructorDef", "entryDef", "simpleSignature", 
			"propertyDecl", "intermediateClassDef", "typeList", "classifierFeature", 
			"classifierFeatureModifier", "tagDecl", "tagTarget", "typedefDecl", "statementList", 
			"statement", "assignOp", "block", "expression", "primaryExpression", 
			"typeExpression", "listType", "dictType", "collectionKind", "literalExpression", 
			"stringLiteral_", "dictLiteral", "dictLiteralPart", "blockExp", "whileExp", 
			"forExp", "forKind", "forVarList", "switchExp", "switchAlt", "computeExp", 
			"objectExp", "newExp", "mappingCallExp", "mappingCallKind", "resolveExp", 
			"resolveKind", "resolveInExp", "resolveInKind", "resolveArgs", "resolveTarget", 
			"tryExp", "catchClause", "exceptionTypeList", "raiseExp", "assertExp", 
			"logCallExp", "logExp", "returnExp", "varDeclExp", "varDeclarator", "varInitOp", 
			"pathName", "propertyOrCallSuffix", "iteratorOrOperationCall", "iteratorVariables", 
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
			null, "'import'", "';'", "'modeltype'", "'uses'", "','", "'('", "')'", 
			"'where'", "'{'", "'}'", "'transformation'", "'library'", "':'", "'in'", 
			"'out'", "'inout'", "'@'", "'access'", "'extends'", "'abstract'", "'blackbox'", 
			"'static'", "'mapping'", "'inherits'", "'merges'", "'disjuncts'", "'when'", 
			"'init'", "'population'", "'end'", "'helper'", "'='", "'query'", "'constructor'", 
			"'main'", "'entry'", "'intermediate'", "'property'", "'configuration'", 
			"'class'", "'readonly'", "'references'", "'composes'", "'tag'", "'::'", 
			"'typedef'", "'with'", "'.'", "'?.'", "'->'", "'?->'", "'['", "'|'", 
			"']'", "'!'", "'not'", "'-'", "'*'", "'/'", "'+'", "'<'", "'>'", "'<='", 
			"'>='", "'<>'", "'and'", "'or'", "'xor'", "'implies'", "'default'", "'self'", 
			"'this'", "'if'", "'then'", "'elseif'", "'elif'", "'else'", "'endif'", 
			"'let'", "'break'", "'continue'", "'List'", "'Dict'", "'Set'", "'OrderedSet'", 
			"'Bag'", "'Sequence'", "'Collection'", "'true'", "'false'", "'null'", 
			"'invalid'", "'do'", "'while'", "'forEach'", "'forOne'", "'switch'", 
			"'case'", "'compute'", "'object'", "'new'", "'map'", "'xmap'", "'late'", 
			"'resolve'", "'resolveone'", "'invresolve'", "'invresolveone'", "'resolveIn'", 
			"'resolveoneIn'", "'invresolveIn'", "'invresolveoneIn'", "'try'", "'catch'", 
			"'except'", "'raise'", "'assert'", "'log'", "'return'", "'var'", "'inv'", 
			"'result'", "'include'", "'package'", "'endpackage'", "'context'", "'def'", 
			"'pre'", "'post'", "'body'", "'derive'", "'..'", "'Tuple'", "'Map'", 
			"'<-'", "'Boolean'", "'Integer'", "'Real'", "'String'", "'UnlimitedNatural'", 
			"'OclAny'", "'OclVoid'", "'OclInvalid'", "'OclMessage'", "':='", "'+='", 
			"'::='"
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
			null, "RESET_ASSIGN", "ADD_ASSIGN", "ORDERED_COPY", "STEREOTYPE_ID", 
			"DOUBLE_QUOTED_STRING", "REAL_LITERAL", "INTEGER_LITERAL", "STRING_LITERAL", 
			"IDENTIFIER", "WS", "LINE_COMMENT", "BLOCK_COMMENT"
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
			setState(256);
			compilationUnit();
			setState(257);
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
			setState(262);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 89054015133706L) != 0)) {
				{
				{
				setState(259);
				unitElement();
				}
				}
				setState(264);
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
			setState(270);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(265);
				qvtoImportDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(266);
				modeltypeDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(267);
				transformationDef();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(268);
				libraryDef();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(269);
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
			enterOuterAlt(_localctx, 1);
			{
			setState(272);
			match(T__0);
			setState(273);
			qualifiedName();
			setState(274);
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
		enterRule(_localctx, 8, RULE_modeltypeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(276);
			match(T__2);
			setState(277);
			qvtoIdentifier();
			setState(279);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING_LITERAL) {
				{
				setState(278);
				match(STRING_LITERAL);
				}
			}

			setState(281);
			match(T__3);
			setState(282);
			packageRefList();
			setState(284);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(283);
				modeltypeWhere();
				}
			}

			setState(286);
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
		enterRule(_localctx, 10, RULE_packageRefList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			packageRef();
			setState(293);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(289);
				match(T__4);
				setState(290);
				packageRef();
				}
				}
				setState(295);
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
		enterRule(_localctx, 12, RULE_packageRef);
		int _la;
		try {
			setState(303);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case T__3:
			case T__7:
			case T__10:
			case T__11:
			case T__17:
			case T__18:
			case T__19:
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
			case T__32:
			case T__33:
			case T__34:
			case T__35:
			case T__36:
			case T__37:
			case T__38:
			case T__43:
			case T__45:
			case T__46:
			case T__79:
			case T__80:
			case T__92:
			case T__93:
			case T__94:
			case T__95:
			case T__96:
			case T__97:
			case T__98:
			case T__99:
			case T__100:
			case T__101:
			case T__102:
			case T__103:
			case T__104:
			case T__105:
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
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(296);
				pathName();
				setState(300);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__5) {
					{
					setState(297);
					match(T__5);
					setState(298);
					match(STRING_LITERAL);
					setState(299);
					match(T__6);
					}
				}

				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(302);
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
		enterRule(_localctx, 14, RULE_modeltypeWhere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
			match(T__7);
			setState(306);
			match(T__8);
			setState(307);
			expression(0);
			setState(312);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(308);
				match(T__4);
				setState(309);
				expression(0);
				}
				}
				setState(314);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(315);
			match(T__9);
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
		enterRule(_localctx, 16, RULE_transformationDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(320);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 7340032L) != 0)) {
				{
				{
				setState(317);
				qualifier();
				}
				}
				setState(322);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(323);
			match(T__10);
			setState(324);
			qualifiedName();
			setState(325);
			match(T__5);
			setState(327);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 114688L) != 0)) {
				{
				setState(326);
				modelParamList();
				}
			}

			setState(329);
			match(T__6);
			setState(333);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__17 || _la==T__18) {
				{
				{
				setState(330);
				moduleUsage();
				}
				}
				setState(335);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(348);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__8:
				{
				setState(336);
				match(T__8);
				setState(340);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 89054015127552L) != 0)) {
					{
					{
					setState(337);
					moduleElement();
					}
					}
					setState(342);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(343);
				match(T__9);
				setState(345);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(344);
					match(T__1);
					}
				}

				}
				break;
			case T__1:
				{
				setState(347);
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
		enterRule(_localctx, 18, RULE_libraryDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(350);
			match(T__11);
			setState(351);
			qualifiedName();
			setState(353);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__5) {
				{
				setState(352);
				simpleSignature();
				}
			}

			setState(358);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__17 || _la==T__18) {
				{
				{
				setState(355);
				moduleUsage();
				}
				}
				setState(360);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(361);
			match(T__8);
			setState(365);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 89054015127552L) != 0)) {
				{
				{
				setState(362);
				moduleElement();
				}
				}
				setState(367);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(368);
			match(T__9);
			setState(370);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(369);
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
		enterRule(_localctx, 20, RULE_modelParamList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372);
			modelParam();
			setState(377);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(373);
				match(T__4);
				setState(374);
				modelParam();
				}
				}
				setState(379);
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
		enterRule(_localctx, 22, RULE_modelParam);
		try {
			setState(388);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(380);
				directionKind();
				setState(381);
				qvtoIdentifier();
				setState(382);
				match(T__12);
				setState(383);
				typeSpec();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(385);
				directionKind();
				setState(386);
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
		enterRule(_localctx, 24, RULE_directionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(390);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 114688L) != 0)) ) {
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
		enterRule(_localctx, 26, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(392);
			typeExpression();
			setState(395);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__16) {
				{
				setState(393);
				match(T__16);
				setState(394);
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
		enterRule(_localctx, 28, RULE_moduleUsage);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(397);
			moduleUsageKind();
			setState(399);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(398);
				_la = _input.LA(1);
				if ( !(_la==T__10 || _la==T__11) ) {
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
			setState(401);
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
		enterRule(_localctx, 30, RULE_moduleUsageKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403);
			_la = _input.LA(1);
			if ( !(_la==T__17 || _la==T__18) ) {
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
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
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
		enterRule(_localctx, 32, RULE_moduleRefList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(405);
			qualifiedName();
			setState(410);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(406);
				match(T__4);
				setState(407);
				qualifiedName();
				}
				}
				setState(412);
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
		enterRule(_localctx, 34, RULE_qualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(413);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 7340032L) != 0)) ) {
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
		public TagDeclContext tagDecl() {
			return getRuleContext(TagDeclContext.class,0);
		}
		public TypedefDeclContext typedefDecl() {
			return getRuleContext(TypedefDeclContext.class,0);
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
		enterRule(_localctx, 36, RULE_moduleElement);
		try {
			setState(428);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(415);
				mappingDef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(416);
				helperDef();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(417);
				queryDef();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(418);
				constructorDef();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(419);
				entryDef();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(420);
				propertyDecl();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(421);
				intermediateClassDef();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(422);
				tagDecl();
				setState(423);
				match(T__1);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(425);
				typedefDecl();
				setState(426);
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
		enterRule(_localctx, 38, RULE_mappingDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(433);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 7340032L) != 0)) {
				{
				{
				setState(430);
				qualifier();
				}
				}
				setState(435);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(436);
			match(T__22);
			setState(438);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 114688L) != 0)) {
				{
				setState(437);
				directionKind();
				}
			}

			setState(440);
			scopedName();
			setState(441);
			mappingSignature();
			setState(445);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 117440512L) != 0)) {
				{
				{
				setState(442);
				mappingExtension();
				}
				}
				setState(447);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(449);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__26) {
				{
				setState(448);
				whenClause();
				}
			}

			setState(452);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(451);
				whereClause();
				}
			}

			setState(454);
			mappingBody();
			setState(456);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(455);
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
		enterRule(_localctx, 40, RULE_mappingSignature);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(458);
			match(T__5);
			setState(460);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 229793635096856L) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & 8796093014019L) != 0) || _la==IDENTIFIER) {
				{
				setState(459);
				((MappingSignatureContext)_localctx).inputParams = paramList();
				}
			}

			setState(462);
			match(T__6);
			setState(465);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(463);
				match(T__12);
				setState(464);
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
		enterRule(_localctx, 42, RULE_resultList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(467);
			resultParam();
			setState(472);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(468);
				match(T__4);
				setState(469);
				resultParam();
				}
				}
				setState(474);
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
		enterRule(_localctx, 44, RULE_resultParam);
		try {
			setState(480);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				_localctx = new NamedResultContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(475);
				qvtoIdentifier();
				setState(476);
				match(T__12);
				setState(477);
				typeExpression();
				}
				break;
			case 2:
				_localctx = new UnnamedResultContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(479);
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
		enterRule(_localctx, 46, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			param();
			setState(487);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(483);
				match(T__4);
				setState(484);
				param();
				}
				}
				setState(489);
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
		enterRule(_localctx, 48, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(491);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 114688L) != 0)) {
				{
				setState(490);
				directionKind();
				}
			}

			setState(493);
			qvtoIdentifier();
			setState(494);
			match(T__12);
			setState(495);
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
		enterRule(_localctx, 50, RULE_mappingExtension);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(497);
			mappingExtensionKind();
			setState(498);
			scopedNameList();
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
		enterRule(_localctx, 52, RULE_mappingExtensionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(500);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 117440512L) != 0)) ) {
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
		enterRule(_localctx, 54, RULE_whenClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(502);
			match(T__26);
			setState(503);
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
		enterRule(_localctx, 56, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(505);
			match(T__7);
			setState(506);
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
		enterRule(_localctx, 58, RULE_guardBlock);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(508);
			match(T__8);
			setState(509);
			expression(0);
			setState(514);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(510);
					match(T__1);
					setState(511);
					expression(0);
					}
					} 
				}
				setState(516);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			}
			setState(518);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(517);
				match(T__1);
				}
			}

			setState(520);
			match(T__9);
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
		enterRule(_localctx, 60, RULE_mappingBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(522);
			match(T__8);
			setState(524);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(523);
				initSection();
				}
				break;
			}
			setState(528);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(526);
				populationSection();
				}
				break;
			case 2:
				{
				setState(527);
				statementList();
				}
				break;
			}
			setState(531);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__29) {
				{
				setState(530);
				endSection();
				}
			}

			setState(533);
			match(T__9);
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
		enterRule(_localctx, 62, RULE_initSection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(535);
			match(T__27);
			setState(536);
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
		enterRule(_localctx, 64, RULE_populationSection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(538);
			match(T__28);
			setState(539);
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
		enterRule(_localctx, 66, RULE_endSection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(541);
			match(T__29);
			setState(542);
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
		enterRule(_localctx, 68, RULE_helperDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(547);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 7340032L) != 0)) {
				{
				{
				setState(544);
				qualifier();
				}
				}
				setState(549);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(550);
			match(T__30);
			setState(551);
			scopedName();
			setState(552);
			simpleSignature();
			setState(555);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(553);
				match(T__12);
				setState(554);
				typeExpression();
				}
			}

			setState(563);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__8:
				{
				setState(557);
				block();
				}
				break;
			case T__31:
				{
				setState(558);
				match(T__31);
				setState(559);
				expression(0);
				setState(560);
				match(T__1);
				}
				break;
			case T__1:
				{
				setState(562);
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
		enterRule(_localctx, 70, RULE_queryDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(568);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 7340032L) != 0)) {
				{
				{
				setState(565);
				qualifier();
				}
				}
				setState(570);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(571);
			match(T__32);
			setState(572);
			scopedName();
			setState(573);
			simpleSignature();
			setState(574);
			match(T__12);
			setState(575);
			resultList();
			setState(582);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__8:
				{
				setState(576);
				block();
				}
				break;
			case T__31:
				{
				setState(577);
				match(T__31);
				setState(578);
				expression(0);
				setState(579);
				match(T__1);
				}
				break;
			case T__1:
				{
				setState(581);
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
		enterRule(_localctx, 72, RULE_constructorDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(584);
			match(T__33);
			setState(585);
			scopedName();
			setState(586);
			simpleSignature();
			setState(587);
			block();
			setState(589);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(588);
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
		enterRule(_localctx, 74, RULE_entryDef);
		int _la;
		try {
			setState(605);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__34:
				enterOuterAlt(_localctx, 1);
				{
				setState(591);
				match(T__34);
				setState(592);
				simpleSignature();
				setState(593);
				block();
				setState(595);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(594);
					match(T__1);
					}
				}

				}
				break;
			case T__35:
				enterOuterAlt(_localctx, 2);
				{
				setState(597);
				match(T__35);
				setState(599);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__5) {
					{
					setState(598);
					simpleSignature();
					}
				}

				setState(601);
				block();
				setState(603);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(602);
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
		enterRule(_localctx, 76, RULE_simpleSignature);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(607);
			match(T__5);
			setState(609);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 229793635096856L) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & 8796093014019L) != 0) || _la==IDENTIFIER) {
				{
				setState(608);
				paramList();
				}
			}

			setState(611);
			match(T__6);
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
		enterRule(_localctx, 78, RULE_propertyDecl);
		int _la;
		try {
			setState(651);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(613);
				match(T__36);
				setState(614);
				match(T__37);
				setState(615);
				scopedName();
				setState(616);
				match(T__12);
				setState(617);
				typeExpression();
				setState(620);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__31) {
					{
					setState(618);
					match(T__31);
					setState(619);
					expression(0);
					}
				}

				setState(622);
				match(T__1);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(624);
				match(T__38);
				setState(625);
				match(T__37);
				setState(626);
				qvtoIdentifier();
				setState(627);
				match(T__12);
				setState(628);
				typeExpression();
				setState(631);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__31) {
					{
					setState(629);
					match(T__31);
					setState(630);
					expression(0);
					}
				}

				setState(633);
				match(T__1);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(635);
				match(T__37);
				setState(636);
				qvtoIdentifier();
				setState(637);
				match(T__12);
				setState(638);
				typeExpression();
				setState(641);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__31) {
					{
					setState(639);
					match(T__31);
					setState(640);
					expression(0);
					}
				}

				setState(643);
				match(T__1);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(645);
				match(T__37);
				setState(646);
				qvtoIdentifier();
				setState(647);
				match(T__31);
				setState(648);
				expression(0);
				setState(649);
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
		enterRule(_localctx, 80, RULE_intermediateClassDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(653);
			match(T__36);
			setState(654);
			match(T__39);
			setState(655);
			qvtoIdentifier();
			setState(658);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__18) {
				{
				setState(656);
				match(T__18);
				setState(657);
				typeList();
				}
			}

			setState(660);
			match(T__8);
			setState(664);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 245186797771032L) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & 8796093014019L) != 0) || _la==STEREOTYPE_ID || _la==IDENTIFIER) {
				{
				{
				setState(661);
				classifierFeature();
				}
				}
				setState(666);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(667);
			match(T__9);
			setState(669);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(668);
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
		enterRule(_localctx, 82, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(671);
			typeExpression();
			setState(676);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(672);
				match(T__4);
				setState(673);
				typeExpression();
				}
				}
				setState(678);
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
		public List<ClassifierFeatureModifierContext> classifierFeatureModifier() {
			return getRuleContexts(ClassifierFeatureModifierContext.class);
		}
		public ClassifierFeatureModifierContext classifierFeatureModifier(int i) {
			return getRuleContext(ClassifierFeatureModifierContext.class,i);
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
		enterRule(_localctx, 84, RULE_classifierFeature);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(682);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,60,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(679);
					classifierFeatureModifier();
					}
					} 
				}
				setState(684);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,60,_ctx);
			}
			setState(685);
			qvtoIdentifier();
			setState(686);
			match(T__12);
			setState(687);
			typeExpression();
			setState(690);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(688);
				match(T__31);
				setState(689);
				expression(0);
				}
			}

			setState(692);
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
	public static class ClassifierFeatureModifierContext extends ParserRuleContext {
		public TerminalNode STEREOTYPE_ID() { return getToken(QvtOParser.STEREOTYPE_ID, 0); }
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
		enterRule(_localctx, 86, RULE_classifierFeatureModifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(694);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 15393166983168L) != 0) || _la==STEREOTYPE_ID) ) {
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
		enterRule(_localctx, 88, RULE_tagDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(696);
			match(T__43);
			setState(697);
			_la = _input.LA(1);
			if ( !(_la==DOUBLE_QUOTED_STRING || _la==STRING_LITERAL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(698);
			tagTarget();
			setState(701);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(699);
				match(T__31);
				setState(700);
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
		enterRule(_localctx, 90, RULE_tagTarget);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(703);
			qvtoIdentifier();
			setState(708);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__44) {
				{
				{
				setState(704);
				match(T__44);
				setState(705);
				qvtoIdentifier();
				}
				}
				setState(710);
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
		enterRule(_localctx, 92, RULE_typedefDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(711);
			match(T__45);
			setState(712);
			qvtoIdentifier();
			setState(713);
			match(T__31);
			setState(714);
			typeExpression();
			setState(717);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__46) {
				{
				setState(715);
				match(T__46);
				setState(716);
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
		enterRule(_localctx, 94, RULE_statementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(722);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(719);
					statement();
					}
					} 
				}
				setState(724);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
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
		enterRule(_localctx, 96, RULE_statement);
		try {
			setState(731);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
			case 1:
				_localctx = new VarDeclStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(725);
				varDeclExp();
				setState(726);
				match(T__1);
				}
				break;
			case 2:
				_localctx = new ExpressionStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(728);
				expression(0);
				setState(729);
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
		enterRule(_localctx, 98, RULE_assignOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(733);
			_la = _input.LA(1);
			if ( !(((((_la - 145)) & ~0x3f) == 0 && ((1L << (_la - 145)) & 7L) != 0)) ) {
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
		enterRule(_localctx, 100, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(735);
			match(T__8);
			setState(739);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
				{
				{
				setState(736);
				statement();
				}
				}
				setState(741);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(742);
			match(T__9);
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
		int _startState = 102;
		enterRecursionRule(_localctx, 102, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(750);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__55:
				{
				_localctx = new NotExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(745);
				match(T__55);
				setState(746);
				expression(12);
				}
				break;
			case T__56:
				{
				_localctx = new UnaryMinusExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(747);
				match(T__56);
				setState(748);
				expression(11);
				}
				break;
			case T__2:
			case T__3:
			case T__5:
			case T__7:
			case T__8:
			case T__10:
			case T__11:
			case T__17:
			case T__18:
			case T__19:
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
			case T__32:
			case T__33:
			case T__34:
			case T__35:
			case T__36:
			case T__37:
			case T__38:
			case T__43:
			case T__45:
			case T__46:
			case T__57:
			case T__70:
			case T__71:
			case T__72:
			case T__78:
			case T__79:
			case T__80:
			case T__81:
			case T__82:
			case T__83:
			case T__84:
			case T__85:
			case T__86:
			case T__87:
			case T__88:
			case T__89:
			case T__90:
			case T__91:
			case T__92:
			case T__93:
			case T__94:
			case T__95:
			case T__96:
			case T__97:
			case T__98:
			case T__99:
			case T__100:
			case T__101:
			case T__102:
			case T__103:
			case T__104:
			case T__105:
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
			case T__132:
			case T__133:
			case T__135:
			case T__136:
			case T__137:
			case T__138:
			case T__139:
			case T__140:
			case T__141:
			case T__142:
			case T__143:
			case DOUBLE_QUOTED_STRING:
			case REAL_LITERAL:
			case INTEGER_LITERAL:
			case STRING_LITERAL:
			case IDENTIFIER:
				{
				_localctx = new PrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(749);
				primaryExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(812);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,73,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(810);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
					case 1:
						{
						_localctx = new MultExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(752);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(753);
						((MultExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__57 || _la==T__58) ) {
							((MultExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(754);
						expression(11);
						}
						break;
					case 2:
						{
						_localctx = new AddExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(755);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(756);
						((AddExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__56 || _la==T__59) ) {
							((AddExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(757);
						expression(10);
						}
						break;
					case 3:
						{
						_localctx = new CompareExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(758);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(759);
						((CompareExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & 15L) != 0)) ) {
							((CompareExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(760);
						expression(9);
						}
						break;
					case 4:
						{
						_localctx = new EqualityExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(761);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(762);
						((EqualityExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__31 || _la==T__64) ) {
							((EqualityExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(763);
						expression(8);
						}
						break;
					case 5:
						{
						_localctx = new AndExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(764);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(765);
						match(T__65);
						setState(766);
						expression(7);
						}
						break;
					case 6:
						{
						_localctx = new OrExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(767);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(768);
						match(T__66);
						setState(769);
						expression(6);
						}
						break;
					case 7:
						{
						_localctx = new XorExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(770);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(771);
						match(T__67);
						setState(772);
						expression(5);
						}
						break;
					case 8:
						{
						_localctx = new ImpliesExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(773);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(774);
						match(T__68);
						setState(775);
						expression(4);
						}
						break;
					case 9:
						{
						_localctx = new NavigationExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(776);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(777);
						_la = _input.LA(1);
						if ( !(_la==T__47 || _la==T__48) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(778);
						propertyOrCallSuffix();
						}
						break;
					case 10:
						{
						_localctx = new ArrowExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(779);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(780);
						_la = _input.LA(1);
						if ( !(_la==T__49 || _la==T__50) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(781);
						iteratorOrOperationCall();
						}
						break;
					case 11:
						{
						_localctx = new XselectExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(782);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(783);
						match(T__51);
						setState(787);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
						case 1:
							{
							setState(784);
							((XselectExpContext)_localctx).xselectIter = qvtoIdentifier();
							setState(785);
							match(T__52);
							}
							break;
						}
						setState(789);
						((XselectExpContext)_localctx).xselectCondition = expression(0);
						setState(790);
						match(T__53);
						}
						break;
					case 12:
						{
						_localctx = new XselectOneExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(792);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(793);
						match(T__54);
						setState(794);
						match(T__51);
						setState(798);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
						case 1:
							{
							setState(795);
							((XselectOneExpContext)_localctx).xselectOneIter = qvtoIdentifier();
							setState(796);
							match(T__52);
							}
							break;
						}
						setState(800);
						((XselectOneExpContext)_localctx).xselectOneCondition = expression(0);
						setState(801);
						match(T__53);
						}
						break;
					case 13:
						{
						_localctx = new AssignExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(803);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(804);
						assignOp();
						setState(805);
						expression(0);
						setState(808);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
						case 1:
							{
							setState(806);
							match(T__69);
							setState(807);
							((AssignExpContext)_localctx).defaultValue = expression(0);
							}
							break;
						}
						}
						break;
					}
					} 
				}
				setState(814);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,73,_ctx);
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
		enterRule(_localctx, 104, RULE_primaryExpression);
		int _la;
		try {
			int _alt;
			setState(907);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
			case 1:
				_localctx = new ParenExpContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(815);
				match(T__5);
				setState(816);
				expression(0);
				setState(817);
				match(T__6);
				}
				break;
			case 2:
				_localctx = new SelfExpContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(819);
				match(T__70);
				}
				break;
			case 3:
				_localctx = new ThisExpContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(820);
				match(T__71);
				}
				break;
			case 4:
				_localctx = new IfExpContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(821);
				match(T__72);
				setState(822);
				((IfExpContext)_localctx).condition = expression(0);
				setState(823);
				match(T__73);
				setState(824);
				((IfExpContext)_localctx).thenExp = expression(0);
				setState(832);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__74 || _la==T__75) {
					{
					{
					setState(825);
					_la = _input.LA(1);
					if ( !(_la==T__74 || _la==T__75) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(826);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfCondition.add(((IfExpContext)_localctx).expression);
					setState(827);
					match(T__73);
					setState(828);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfExp.add(((IfExpContext)_localctx).expression);
					}
					}
					setState(834);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(837);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__76) {
					{
					setState(835);
					match(T__76);
					setState(836);
					((IfExpContext)_localctx).elseExp = expression(0);
					}
				}

				setState(839);
				match(T__77);
				}
				break;
			case 5:
				_localctx = new ImperativeIfExpContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(841);
				match(T__72);
				setState(842);
				match(T__5);
				setState(843);
				((ImperativeIfExpContext)_localctx).impCondition = expression(0);
				setState(844);
				match(T__6);
				setState(845);
				((ImperativeIfExpContext)_localctx).thenBlock = block();
				setState(854);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,76,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(846);
						match(T__75);
						setState(847);
						match(T__5);
						setState(848);
						((ImperativeIfExpContext)_localctx).expression = expression(0);
						((ImperativeIfExpContext)_localctx).elifCondition.add(((ImperativeIfExpContext)_localctx).expression);
						setState(849);
						match(T__6);
						setState(850);
						((ImperativeIfExpContext)_localctx).block = block();
						((ImperativeIfExpContext)_localctx).elifBlock.add(((ImperativeIfExpContext)_localctx).block);
						}
						} 
					}
					setState(856);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,76,_ctx);
				}
				setState(859);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
				case 1:
					{
					setState(857);
					match(T__76);
					setState(858);
					((ImperativeIfExpContext)_localctx).elseBlock = block();
					}
					break;
				}
				setState(862);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
				case 1:
					{
					setState(861);
					match(T__77);
					}
					break;
				}
				}
				break;
			case 6:
				_localctx = new LetExpContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(864);
				match(T__78);
				setState(865);
				letBinding();
				setState(870);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(866);
					match(T__4);
					setState(867);
					letBinding();
					}
					}
					setState(872);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(873);
				match(T__13);
				setState(874);
				expression(0);
				}
				break;
			case 7:
				_localctx = new LiteralExpContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(876);
				literalExpression();
				}
				break;
			case 8:
				_localctx = new BlockPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(877);
				blockExp();
				}
				break;
			case 9:
				_localctx = new WhilePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(878);
				whileExp();
				}
				break;
			case 10:
				_localctx = new ForPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(879);
				forExp();
				}
				break;
			case 11:
				_localctx = new SwitchPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(880);
				switchExp();
				}
				break;
			case 12:
				_localctx = new ComputePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(881);
				computeExp();
				}
				break;
			case 13:
				_localctx = new ObjectPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(882);
				objectExp();
				}
				break;
			case 14:
				_localctx = new NewPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(883);
				newExp();
				}
				break;
			case 15:
				_localctx = new MappingCallPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(884);
				mappingCallExp();
				}
				break;
			case 16:
				_localctx = new ResolvePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(885);
				resolveExp();
				}
				break;
			case 17:
				_localctx = new ResolveInPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(886);
				resolveInExp();
				}
				break;
			case 18:
				_localctx = new TryPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(887);
				tryExp();
				}
				break;
			case 19:
				_localctx = new RaisePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(888);
				raiseExp();
				}
				break;
			case 20:
				_localctx = new AssertPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(889);
				assertExp();
				}
				break;
			case 21:
				_localctx = new LogPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 21);
				{
				setState(890);
				logExp();
				}
				break;
			case 22:
				_localctx = new ReturnPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 22);
				{
				setState(891);
				returnExp();
				}
				break;
			case 23:
				_localctx = new BreakPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 23);
				{
				setState(892);
				match(T__79);
				}
				break;
			case 24:
				_localctx = new ContinuePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 24);
				{
				setState(893);
				match(T__80);
				}
				break;
			case 25:
				_localctx = new VarDeclPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 25);
				{
				setState(894);
				varDeclExp();
				}
				break;
			case 26:
				_localctx = new OperationCallExpContext(_localctx);
				enterOuterAlt(_localctx, 26);
				{
				setState(895);
				pathName();
				setState(896);
				match(T__5);
				setState(898);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
					{
					setState(897);
					argumentList();
					}
				}

				setState(900);
				match(T__6);
				}
				break;
			case 27:
				_localctx = new PrimitiveTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 27);
				{
				setState(902);
				primitiveType();
				}
				break;
			case 28:
				_localctx = new CollectionTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 28);
				{
				setState(903);
				collectionType();
				}
				break;
			case 29:
				_localctx = new MapTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 29);
				{
				setState(904);
				mapType();
				}
				break;
			case 30:
				_localctx = new TupleTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 30);
				{
				setState(905);
				tupleType();
				}
				break;
			case 31:
				_localctx = new PathNameExpContext(_localctx);
				enterOuterAlt(_localctx, 31);
				{
				setState(906);
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
		enterRule(_localctx, 106, RULE_typeExpression);
		try {
			setState(916);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(909);
				primitiveType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(910);
				collectionType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(911);
				mapType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(912);
				tupleType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(913);
				listType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(914);
				dictType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(915);
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
		enterRule(_localctx, 108, RULE_listType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(918);
			match(T__81);
			setState(919);
			match(T__5);
			setState(920);
			typeExpression();
			setState(921);
			match(T__6);
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
		enterRule(_localctx, 110, RULE_dictType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(923);
			match(T__82);
			setState(924);
			match(T__5);
			setState(925);
			typeExpression();
			setState(926);
			match(T__4);
			setState(927);
			typeExpression();
			setState(928);
			match(T__6);
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
		enterRule(_localctx, 112, RULE_collectionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(930);
			_la = _input.LA(1);
			if ( !(((((_la - 82)) & ~0x3f) == 0 && ((1L << (_la - 82)) & 125L) != 0)) ) {
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
		enterRule(_localctx, 114, RULE_literalExpression);
		try {
			setState(944);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER_LITERAL:
				_localctx = new IntegerLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(932);
				match(INTEGER_LITERAL);
				}
				break;
			case REAL_LITERAL:
				_localctx = new RealLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(933);
				match(REAL_LITERAL);
				}
				break;
			case DOUBLE_QUOTED_STRING:
			case STRING_LITERAL:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(934);
				stringLiteral_();
				}
				break;
			case T__88:
				_localctx = new TrueLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(935);
				match(T__88);
				}
				break;
			case T__89:
				_localctx = new FalseLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(936);
				match(T__89);
				}
				break;
			case T__90:
				_localctx = new NullLiteralContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(937);
				match(T__90);
				}
				break;
			case T__91:
				_localctx = new InvalidLiteralContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(938);
				match(T__91);
				}
				break;
			case T__57:
				_localctx = new UnlimitedNaturalLiteralContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(939);
				match(T__57);
				}
				break;
			case T__81:
			case T__83:
			case T__84:
			case T__85:
			case T__86:
			case T__87:
				_localctx = new CollectionLitContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(940);
				collectionLiteral();
				}
				break;
			case T__132:
				_localctx = new TupleLitContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(941);
				tupleLiteral();
				}
				break;
			case T__133:
				_localctx = new MapLitContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(942);
				mapLiteral();
				}
				break;
			case T__82:
				_localctx = new DictLitContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(943);
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
		enterRule(_localctx, 116, RULE_stringLiteral_);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(947); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(946);
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
				setState(949); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,84,_ctx);
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
		enterRule(_localctx, 118, RULE_dictLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(951);
			match(T__82);
			setState(952);
			match(T__8);
			setState(961);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
				{
				setState(953);
				dictLiteralPart();
				setState(958);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(954);
					match(T__4);
					setState(955);
					dictLiteralPart();
					}
					}
					setState(960);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(963);
			match(T__9);
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
		public ExpressionContext key;
		public ExpressionContext value;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
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
		enterRule(_localctx, 120, RULE_dictLiteralPart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(965);
			((DictLiteralPartContext)_localctx).key = expression(0);
			setState(966);
			match(T__31);
			setState(967);
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
		enterRule(_localctx, 122, RULE_blockExp);
		int _la;
		try {
			setState(986);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__92:
				enterOuterAlt(_localctx, 1);
				{
				setState(969);
				match(T__92);
				setState(970);
				match(T__8);
				setState(974);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
					{
					{
					setState(971);
					statement();
					}
					}
					setState(976);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(977);
				match(T__9);
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(978);
				match(T__8);
				setState(982);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
					{
					{
					setState(979);
					statement();
					}
					}
					setState(984);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(985);
				match(T__9);
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
		enterRule(_localctx, 124, RULE_whileExp);
		try {
			setState(1006);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
			case 1:
				_localctx = new WhileWithInitContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(988);
				match(T__93);
				setState(989);
				match(T__5);
				setState(990);
				((WhileWithInitContext)_localctx).varName = qvtoIdentifier();
				setState(991);
				match(T__12);
				setState(992);
				((WhileWithInitContext)_localctx).type = typeExpression();
				setState(993);
				match(RESET_ASSIGN);
				setState(994);
				((WhileWithInitContext)_localctx).initValue = expression(0);
				setState(995);
				match(T__1);
				setState(996);
				((WhileWithInitContext)_localctx).condition = expression(0);
				setState(997);
				match(T__6);
				setState(998);
				block();
				}
				break;
			case 2:
				_localctx = new WhileBasicContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1000);
				match(T__93);
				setState(1001);
				match(T__5);
				setState(1002);
				expression(0);
				setState(1003);
				match(T__6);
				setState(1004);
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
		enterRule(_localctx, 126, RULE_forExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1008);
			forKind();
			setState(1009);
			match(T__5);
			setState(1010);
			forVarList();
			setState(1013);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__52) {
				{
				setState(1011);
				match(T__52);
				setState(1012);
				expression(0);
				}
			}

			setState(1015);
			match(T__6);
			setState(1016);
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
		enterRule(_localctx, 128, RULE_forKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1018);
			_la = _input.LA(1);
			if ( !(_la==T__94 || _la==T__95) ) {
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
		enterRule(_localctx, 130, RULE_forVarList);
		int _la;
		try {
			setState(1035);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1020);
				iteratorVariables();
				setState(1021);
				match(T__1);
				setState(1022);
				qvtoIdentifier();
				setState(1023);
				match(T__12);
				setState(1024);
				typeExpression();
				setState(1028);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__31 || _la==RESET_ASSIGN || _la==ORDERED_COPY) {
					{
					setState(1025);
					varInitOp();
					setState(1026);
					expression(0);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1030);
				iteratorVariables();
				setState(1031);
				match(T__1);
				setState(1032);
				typeExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1034);
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
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<SwitchAltContext> switchAlt() {
			return getRuleContexts(SwitchAltContext.class);
		}
		public SwitchAltContext switchAlt(int i) {
			return getRuleContext(SwitchAltContext.class,i);
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
		enterRule(_localctx, 132, RULE_switchExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1037);
			match(T__96);
			setState(1042);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__5) {
				{
				setState(1038);
				match(T__5);
				setState(1039);
				expression(0);
				setState(1040);
				match(T__6);
				}
			}

			setState(1044);
			match(T__8);
			setState(1048);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__97) {
				{
				{
				setState(1045);
				switchAlt();
				}
				}
				setState(1050);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1055);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__76) {
				{
				setState(1051);
				match(T__76);
				setState(1052);
				expression(0);
				setState(1053);
				match(T__1);
				}
			}

			setState(1057);
			match(T__9);
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
		enterRule(_localctx, 134, RULE_switchAlt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1059);
			match(T__97);
			setState(1060);
			match(T__5);
			setState(1061);
			expression(0);
			setState(1062);
			match(T__6);
			setState(1063);
			expression(0);
			setState(1064);
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
		enterRule(_localctx, 136, RULE_computeExp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1066);
			match(T__98);
			setState(1067);
			match(T__5);
			setState(1068);
			varDeclarator();
			setState(1069);
			match(T__6);
			setState(1070);
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
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
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
		enterRule(_localctx, 138, RULE_objectExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1072);
			match(T__99);
			setState(1076);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				{
				setState(1073);
				qvtoIdentifier();
				setState(1074);
				match(T__12);
				}
				break;
			}
			setState(1079);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 229793634982168L) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & -45027200180690433L) != 0) || _la==T__143 || _la==IDENTIFIER) {
				{
				setState(1078);
				typeExpression();
				}
			}

			setState(1081);
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
	public static class NewExpContext extends ParserRuleContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
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
		enterRule(_localctx, 140, RULE_newExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1083);
			match(T__100);
			setState(1084);
			pathName();
			setState(1085);
			match(T__5);
			setState(1087);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
				{
				setState(1086);
				argumentList();
				}
			}

			setState(1089);
			match(T__6);
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
		enterRule(_localctx, 142, RULE_mappingCallExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1091);
			mappingCallKind();
			setState(1092);
			scopedName();
			setState(1093);
			match(T__5);
			setState(1095);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
				{
				setState(1094);
				argumentList();
				}
			}

			setState(1097);
			match(T__6);
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
		enterRule(_localctx, 144, RULE_mappingCallKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1099);
			_la = _input.LA(1);
			if ( !(_la==T__101 || _la==T__102) ) {
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
		enterRule(_localctx, 146, RULE_resolveExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1102);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__103) {
				{
				setState(1101);
				match(T__103);
				}
			}

			setState(1104);
			resolveKind();
			setState(1105);
			match(T__5);
			setState(1107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 229793634982168L) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & -45027200180690433L) != 0) || _la==T__143 || _la==IDENTIFIER) {
				{
				setState(1106);
				resolveArgs();
				}
			}

			setState(1109);
			match(T__6);
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
		enterRule(_localctx, 148, RULE_resolveKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1111);
			_la = _input.LA(1);
			if ( !(((((_la - 105)) & ~0x3f) == 0 && ((1L << (_la - 105)) & 15L) != 0)) ) {
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
		enterRule(_localctx, 150, RULE_resolveInExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1114);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__103) {
				{
				setState(1113);
				match(T__103);
				}
			}

			setState(1116);
			resolveInKind();
			setState(1117);
			match(T__5);
			setState(1118);
			scopedName();
			setState(1121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(1119);
				match(T__4);
				setState(1120);
				resolveArgs();
				}
			}

			setState(1123);
			match(T__6);
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
		enterRule(_localctx, 152, RULE_resolveInKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1125);
			_la = _input.LA(1);
			if ( !(((((_la - 109)) & ~0x3f) == 0 && ((1L << (_la - 109)) & 15L) != 0)) ) {
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
		enterRule(_localctx, 154, RULE_resolveArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1127);
			resolveTarget();
			setState(1130);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__52) {
				{
				setState(1128);
				match(T__52);
				setState(1129);
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
		enterRule(_localctx, 156, RULE_resolveTarget);
		try {
			setState(1137);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				_localctx = new NamedResolveTargetContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1132);
				qvtoIdentifier();
				setState(1133);
				match(T__12);
				setState(1134);
				typeExpression();
				}
				break;
			case 2:
				_localctx = new TypeOnlyResolveTargetContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1136);
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
		enterRule(_localctx, 158, RULE_tryExp);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1139);
			match(T__112);
			setState(1140);
			block();
			setState(1142); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1141);
					catchClause();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1144); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,107,_ctx);
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
		enterRule(_localctx, 160, RULE_catchClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1146);
			_la = _input.LA(1);
			if ( !(_la==T__113 || _la==T__114) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1157);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__5) {
				{
				setState(1147);
				match(T__5);
				setState(1154);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 229793634982168L) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & 8796093014019L) != 0) || _la==IDENTIFIER) {
					{
					setState(1151);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,108,_ctx) ) {
					case 1:
						{
						setState(1148);
						((CatchClauseContext)_localctx).exceptionVar = qvtoIdentifier();
						setState(1149);
						match(T__12);
						}
						break;
					}
					setState(1153);
					exceptionTypeList();
					}
				}

				setState(1156);
				match(T__6);
				}
			}

			setState(1159);
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
		enterRule(_localctx, 162, RULE_exceptionTypeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1161);
			pathName();
			setState(1166);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1162);
				match(T__4);
				setState(1163);
				pathName();
				}
				}
				setState(1168);
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
		enterRule(_localctx, 164, RULE_raiseExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1169);
			match(T__115);
			setState(1179);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case T__3:
			case T__7:
			case T__10:
			case T__11:
			case T__17:
			case T__18:
			case T__19:
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
			case T__32:
			case T__33:
			case T__34:
			case T__35:
			case T__36:
			case T__37:
			case T__38:
			case T__43:
			case T__45:
			case T__46:
			case T__79:
			case T__80:
			case T__92:
			case T__93:
			case T__94:
			case T__95:
			case T__96:
			case T__97:
			case T__98:
			case T__99:
			case T__100:
			case T__101:
			case T__102:
			case T__103:
			case T__104:
			case T__105:
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
			case IDENTIFIER:
				{
				setState(1170);
				pathName();
				setState(1176);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,113,_ctx) ) {
				case 1:
					{
					setState(1171);
					match(T__5);
					setState(1173);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
						{
						setState(1172);
						expression(0);
						}
					}

					setState(1175);
					match(T__6);
					}
					break;
				}
				}
				break;
			case STRING_LITERAL:
				{
				setState(1178);
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
		enterRule(_localctx, 166, RULE_assertExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1181);
			match(T__116);
			setState(1183);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 229793634982168L) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & 8796093014019L) != 0) || _la==IDENTIFIER) {
				{
				setState(1182);
				qvtoIdentifier();
				}
			}

			setState(1185);
			match(T__5);
			setState(1186);
			expression(0);
			setState(1187);
			match(T__6);
			setState(1190);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
			case 1:
				{
				setState(1188);
				match(T__46);
				setState(1189);
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
		enterRule(_localctx, 168, RULE_logCallExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1192);
			match(T__117);
			setState(1193);
			match(T__5);
			setState(1195);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
				{
				setState(1194);
				argumentList();
				}
			}

			setState(1197);
			match(T__6);
			setState(1200);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,118,_ctx) ) {
			case 1:
				{
				setState(1198);
				match(T__26);
				setState(1199);
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
		enterRule(_localctx, 170, RULE_logExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1202);
			match(T__117);
			setState(1203);
			match(T__5);
			setState(1205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
				{
				setState(1204);
				argumentList();
				}
			}

			setState(1207);
			match(T__6);
			setState(1210);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,120,_ctx) ) {
			case 1:
				{
				setState(1208);
				match(T__26);
				setState(1209);
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
		enterRule(_localctx, 172, RULE_returnExp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1212);
			match(T__118);
			setState(1214);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
			case 1:
				{
				setState(1213);
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
		enterRule(_localctx, 174, RULE_varDeclExp);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1216);
			match(T__119);
			setState(1217);
			varDeclarator();
			setState(1222);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,122,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1218);
					match(T__4);
					setState(1219);
					varDeclarator();
					}
					} 
				}
				setState(1224);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,122,_ctx);
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
		enterRule(_localctx, 176, RULE_varDeclarator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1225);
			qvtoIdentifier();
			setState(1228);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,123,_ctx) ) {
			case 1:
				{
				setState(1226);
				match(T__12);
				setState(1227);
				typeExpression();
				}
				break;
			}
			setState(1233);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,124,_ctx) ) {
			case 1:
				{
				setState(1230);
				varInitOp();
				setState(1231);
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
		enterRule(_localctx, 178, RULE_varInitOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1235);
			_la = _input.LA(1);
			if ( !(_la==T__31 || _la==RESET_ASSIGN || _la==ORDERED_COPY) ) {
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
		enterRule(_localctx, 180, RULE_pathName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1237);
			qvtoIdentifier();
			setState(1242);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,125,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1238);
					match(T__44);
					setState(1239);
					qvtoIdentifier();
					}
					} 
				}
				setState(1244);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,125,_ctx);
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
		enterRule(_localctx, 182, RULE_propertyOrCallSuffix);
		int _la;
		try {
			setState(1288);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
			case 1:
				_localctx = new MappingCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1245);
				mappingCallKind();
				setState(1246);
				scopedName();
				setState(1247);
				match(T__5);
				setState(1249);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
					{
					setState(1248);
					argumentList();
					}
				}

				setState(1251);
				match(T__6);
				}
				break;
			case 2:
				_localctx = new ResolveCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1254);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__103) {
					{
					setState(1253);
					match(T__103);
					}
				}

				setState(1256);
				resolveKind();
				setState(1257);
				match(T__5);
				setState(1259);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 229793634982168L) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & -45027200180690433L) != 0) || _la==T__143 || _la==IDENTIFIER) {
					{
					setState(1258);
					resolveArgs();
					}
				}

				setState(1261);
				match(T__6);
				}
				break;
			case 3:
				_localctx = new ResolveInCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1264);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__103) {
					{
					setState(1263);
					match(T__103);
					}
				}

				setState(1266);
				resolveInKind();
				setState(1267);
				match(T__5);
				setState(1268);
				scopedName();
				setState(1271);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(1269);
					match(T__4);
					setState(1270);
					resolveArgs();
					}
				}

				setState(1273);
				match(T__6);
				}
				break;
			case 4:
				_localctx = new DotCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(1275);
				qvtoIdentifier();
				setState(1276);
				match(T__5);
				setState(1278);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
					{
					setState(1277);
					argumentList();
					}
				}

				setState(1280);
				match(T__6);
				setState(1282);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,132,_ctx) ) {
				case 1:
					{
					setState(1281);
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
				setState(1284);
				qvtoIdentifier();
				setState(1286);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,133,_ctx) ) {
				case 1:
					{
					setState(1285);
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

	public final IteratorOrOperationCallContext iteratorOrOperationCall() throws RecognitionException {
		IteratorOrOperationCallContext _localctx = new IteratorOrOperationCallContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_iteratorOrOperationCall);
		int _la;
		try {
			setState(1364);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,144,_ctx) ) {
			case 1:
				_localctx = new ForEachCallContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1290);
				forKind();
				setState(1291);
				match(T__5);
				setState(1292);
				forVarList();
				setState(1295);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__52) {
					{
					setState(1293);
					match(T__52);
					setState(1294);
					expression(0);
					}
				}

				setState(1297);
				match(T__6);
				setState(1298);
				block();
				}
				break;
			case 2:
				_localctx = new ArrowResolveCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1301);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__103) {
					{
					setState(1300);
					match(T__103);
					}
				}

				setState(1303);
				resolveKind();
				setState(1304);
				match(T__5);
				setState(1306);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 229793634982168L) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & -45027200180690433L) != 0) || _la==T__143 || _la==IDENTIFIER) {
					{
					setState(1305);
					resolveArgs();
					}
				}

				setState(1308);
				match(T__6);
				}
				break;
			case 3:
				_localctx = new ArrowResolveInCallContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1311);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__103) {
					{
					setState(1310);
					match(T__103);
					}
				}

				setState(1313);
				resolveInKind();
				setState(1314);
				match(T__5);
				setState(1315);
				scopedName();
				setState(1318);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(1316);
					match(T__4);
					setState(1317);
					resolveArgs();
					}
				}

				setState(1320);
				match(T__6);
				}
				break;
			case 4:
				_localctx = new ArrowMappingCallContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(1322);
				mappingCallKind();
				setState(1323);
				scopedName();
				setState(1324);
				match(T__5);
				setState(1326);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
					{
					setState(1325);
					argumentList();
					}
				}

				setState(1328);
				match(T__6);
				}
				break;
			case 5:
				_localctx = new IteratorCallContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(1330);
				qvtoIdentifier();
				setState(1331);
				match(T__5);
				setState(1332);
				iteratorVariables();
				setState(1333);
				match(T__52);
				setState(1334);
				expression(0);
				setState(1335);
				match(T__6);
				}
				break;
			case 6:
				_localctx = new IterateCallContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(1337);
				qvtoIdentifier();
				setState(1338);
				match(T__5);
				setState(1339);
				qvtoIdentifier();
				setState(1342);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__12) {
					{
					setState(1340);
					match(T__12);
					setState(1341);
					((IterateCallContext)_localctx).iterType = typeExpression();
					}
				}

				setState(1344);
				match(T__1);
				setState(1345);
				qvtoIdentifier();
				setState(1348);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__12) {
					{
					setState(1346);
					match(T__12);
					setState(1347);
					((IterateCallContext)_localctx).accType = typeExpression();
					}
				}

				setState(1350);
				match(T__31);
				setState(1351);
				expression(0);
				setState(1352);
				match(T__52);
				setState(1353);
				expression(0);
				setState(1354);
				match(T__6);
				}
				break;
			case 7:
				_localctx = new CollectionOperationCallContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(1356);
				qvtoIdentifier();
				setState(1357);
				match(T__5);
				setState(1359);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
					{
					setState(1358);
					argumentList();
					}
				}

				setState(1361);
				match(T__6);
				}
				break;
			case 8:
				_localctx = new ArrowPropertyCallContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(1363);
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
		enterRule(_localctx, 186, RULE_iteratorVariables);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1366);
			qvtoIdentifier();
			setState(1369);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(1367);
				match(T__12);
				setState(1368);
				typeExpression();
				}
			}

			setState(1379);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1371);
				match(T__4);
				setState(1372);
				qvtoIdentifier();
				setState(1375);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__12) {
					{
					setState(1373);
					match(T__12);
					setState(1374);
					typeExpression();
					}
				}

				}
				}
				setState(1381);
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
		enterRule(_localctx, 188, RULE_letBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1382);
			qvtoIdentifier();
			setState(1385);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(1383);
				match(T__12);
				setState(1384);
				typeExpression();
				}
			}

			setState(1387);
			match(T__31);
			setState(1388);
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
		enterRule(_localctx, 190, RULE_tupleTypePart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1390);
			qvtoIdentifier();
			setState(1391);
			match(T__12);
			setState(1392);
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
		enterRule(_localctx, 192, RULE_tupleLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1394);
			qvtoIdentifier();
			setState(1397);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(1395);
				match(T__12);
				setState(1396);
				typeExpression();
				}
			}

			setState(1399);
			match(T__31);
			setState(1400);
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
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public QvtoIdentifierContext qvtoIdentifier() {
			return getRuleContext(QvtoIdentifierContext.class,0);
		}
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
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
		enterRule(_localctx, 194, RULE_scopedName);
		try {
			setState(1415);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,150,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1402);
				qualifiedName();
				setState(1403);
				match(T__44);
				setState(1404);
				qvtoIdentifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1406);
				primitiveType();
				setState(1407);
				match(T__44);
				setState(1408);
				qvtoIdentifier();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1410);
				collectionType();
				setState(1411);
				match(T__44);
				setState(1412);
				qvtoIdentifier();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1414);
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
		enterRule(_localctx, 196, RULE_scopedNameList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1417);
			scopedName();
			setState(1422);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1418);
				match(T__4);
				setState(1419);
				scopedName();
				}
				}
				setState(1424);
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
		enterRule(_localctx, 198, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1425);
			qvtoIdentifier();
			setState(1430);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__47) {
				{
				{
				setState(1426);
				match(T__47);
				setState(1427);
				qvtoIdentifier();
				}
				}
				setState(1432);
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
		enterRule(_localctx, 200, RULE_qvtoIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1433);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 229793634982168L) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & 8796093014019L) != 0) || _la==IDENTIFIER) ) {
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
		enterRule(_localctx, 202, RULE_expressionEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1435);
			expression(0);
			setState(1436);
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
		enterRule(_localctx, 204, RULE_completeOclDocumentEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1438);
			completeOclDocument();
			setState(1439);
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
		enterRule(_localctx, 206, RULE_completeOclDocument);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1444);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0 || _la==T__11 || _la==T__122) {
				{
				{
				setState(1441);
				importDeclaration();
				}
				}
				setState(1446);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1451);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__123 || _la==T__125) {
				{
				setState(1449);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__123:
					{
					setState(1447);
					packageDeclaration();
					}
					break;
				case T__125:
					{
					setState(1448);
					contextDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(1453);
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
		enterRule(_localctx, 208, RULE_importDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1454);
			_la = _input.LA(1);
			if ( !(_la==T__0 || _la==T__11 || _la==T__122) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1457);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,156,_ctx) ) {
			case 1:
				{
				setState(1455);
				match(IDENTIFIER);
				setState(1456);
				match(T__12);
				}
				break;
			}
			setState(1459);
			pathName();
			setState(1462);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__44) {
				{
				setState(1460);
				match(T__44);
				setState(1461);
				match(T__57);
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
		enterRule(_localctx, 210, RULE_packageDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1464);
			match(T__123);
			setState(1465);
			pathName();
			setState(1469);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__125) {
				{
				{
				setState(1466);
				contextDeclaration();
				}
				}
				setState(1471);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1472);
			match(T__124);
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
		enterRule(_localctx, 212, RULE_contextDeclaration);
		try {
			setState(1477);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,159,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1474);
				classifierContextDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1475);
				operationContextDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1476);
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
		enterRule(_localctx, 214, RULE_classifierContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1479);
			match(T__125);
			setState(1480);
			pathName();
			setState(1482); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1481);
				classifierContextBody();
				}
				}
				setState(1484); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__120 || _la==T__126 );
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
		enterRule(_localctx, 216, RULE_classifierContextBody);
		try {
			setState(1488);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__120:
				enterOuterAlt(_localctx, 1);
				{
				setState(1486);
				invariantConstraint();
				}
				break;
			case T__126:
				enterOuterAlt(_localctx, 2);
				{
				setState(1487);
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
		enterRule(_localctx, 218, RULE_invariantConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1490);
			match(T__120);
			setState(1498);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(1491);
				match(IDENTIFIER);
				setState(1496);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__5) {
					{
					setState(1492);
					match(T__5);
					setState(1493);
					expression(0);
					setState(1494);
					match(T__6);
					}
				}

				}
			}

			setState(1500);
			match(T__12);
			setState(1501);
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
		enterRule(_localctx, 220, RULE_definitionConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1503);
			match(T__126);
			setState(1505);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(1504);
				match(IDENTIFIER);
				}
			}

			setState(1507);
			match(T__12);
			setState(1525);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,166,_ctx) ) {
			case 1:
				{
				setState(1508);
				match(IDENTIFIER);
				setState(1509);
				match(T__12);
				setState(1510);
				typeExpression();
				setState(1511);
				match(T__31);
				setState(1512);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1514);
				match(IDENTIFIER);
				setState(1515);
				match(T__5);
				setState(1517);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1516);
					parameterList();
					}
				}

				setState(1519);
				match(T__6);
				setState(1520);
				match(T__12);
				setState(1521);
				typeExpression();
				setState(1522);
				match(T__31);
				setState(1523);
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
		enterRule(_localctx, 222, RULE_operationContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1527);
			match(T__125);
			setState(1528);
			pathName();
			setState(1529);
			match(T__44);
			setState(1530);
			match(IDENTIFIER);
			setState(1531);
			match(T__5);
			setState(1533);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(1532);
				parameterList();
				}
			}

			setState(1535);
			match(T__6);
			setState(1536);
			match(T__12);
			setState(1537);
			typeExpression();
			setState(1539); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1538);
				operationContextBody();
				}
				}
				setState(1541); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 7L) != 0) );
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
		enterRule(_localctx, 224, RULE_operationContextBody);
		int _la;
		try {
			setState(1561);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__127:
				_localctx = new PreConditionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1543);
				match(T__127);
				setState(1545);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1544);
					match(IDENTIFIER);
					}
				}

				setState(1547);
				match(T__12);
				setState(1548);
				expression(0);
				}
				break;
			case T__128:
				_localctx = new PostConditionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1549);
				match(T__128);
				setState(1551);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1550);
					match(IDENTIFIER);
					}
				}

				setState(1553);
				match(T__12);
				setState(1554);
				expression(0);
				}
				break;
			case T__129:
				_localctx = new BodyExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1555);
				match(T__129);
				setState(1557);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1556);
					match(IDENTIFIER);
					}
				}

				setState(1559);
				match(T__12);
				setState(1560);
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
		enterRule(_localctx, 226, RULE_propertyContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1563);
			match(T__125);
			setState(1564);
			pathName();
			setState(1565);
			match(T__44);
			setState(1566);
			match(IDENTIFIER);
			setState(1567);
			match(T__12);
			setState(1568);
			typeExpression();
			setState(1570); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1569);
				propertyContextBody();
				}
				}
				setState(1572); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__27 || _la==T__130 );
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
		enterRule(_localctx, 228, RULE_propertyContextBody);
		int _la;
		try {
			setState(1586);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__27:
				_localctx = new InitExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1574);
				match(T__27);
				setState(1576);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1575);
					match(IDENTIFIER);
					}
				}

				setState(1578);
				match(T__12);
				setState(1579);
				expression(0);
				}
				break;
			case T__130:
				_localctx = new DeriveExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1580);
				match(T__130);
				setState(1582);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1581);
					match(IDENTIFIER);
					}
				}

				setState(1584);
				match(T__12);
				setState(1585);
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
		enterRule(_localctx, 230, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1588);
			parameter();
			setState(1593);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1589);
				match(T__4);
				setState(1590);
				parameter();
				}
				}
				setState(1595);
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
		enterRule(_localctx, 232, RULE_parameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1596);
			match(IDENTIFIER);
			setState(1597);
			match(T__12);
			setState(1598);
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
		enterRule(_localctx, 234, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1600);
			expression(0);
			setState(1605);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1601);
				match(T__4);
				setState(1602);
				expression(0);
				}
				}
				setState(1607);
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
		enterRule(_localctx, 236, RULE_isMarkedPre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1608);
			match(T__16);
			setState(1609);
			match(T__127);
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
		enterRule(_localctx, 238, RULE_collectionLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1611);
			collectionKind();
			setState(1612);
			match(T__8);
			setState(1621);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
				{
				setState(1613);
				collectionLiteralPart();
				setState(1618);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(1614);
					match(T__4);
					setState(1615);
					collectionLiteralPart();
					}
					}
					setState(1620);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1623);
			match(T__9);
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
		enterRule(_localctx, 240, RULE_collectionLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1625);
			expression(0);
			setState(1628);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__131) {
				{
				setState(1626);
				match(T__131);
				setState(1627);
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
		enterRule(_localctx, 242, RULE_tupleLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1630);
			match(T__132);
			setState(1631);
			match(T__8);
			setState(1632);
			tupleLiteralPart();
			setState(1637);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1633);
				match(T__4);
				setState(1634);
				tupleLiteralPart();
				}
				}
				setState(1639);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1640);
			match(T__9);
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
		enterRule(_localctx, 244, RULE_mapLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1642);
			match(T__133);
			setState(1643);
			match(T__8);
			setState(1652);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 504632951900478296L) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & -4607182418800017657L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 254463L) != 0)) {
				{
				setState(1644);
				mapLiteralPart();
				setState(1649);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(1645);
					match(T__4);
					setState(1646);
					mapLiteralPart();
					}
					}
					setState(1651);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1654);
			match(T__9);
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
		enterRule(_localctx, 246, RULE_mapLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1656);
			expression(0);
			setState(1657);
			_la = _input.LA(1);
			if ( !(_la==T__46 || _la==T__134) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1658);
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
		enterRule(_localctx, 248, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1660);
			_la = _input.LA(1);
			if ( !(((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 511L) != 0)) ) {
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
		enterRule(_localctx, 250, RULE_collectionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1662);
			collectionKind();
			setState(1663);
			match(T__5);
			setState(1664);
			typeExpression();
			setState(1665);
			match(T__6);
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
		enterRule(_localctx, 252, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1667);
			match(T__133);
			setState(1668);
			match(T__5);
			setState(1669);
			typeExpression();
			setState(1670);
			match(T__4);
			setState(1671);
			typeExpression();
			setState(1672);
			match(T__6);
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
		enterRule(_localctx, 254, RULE_tupleType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1674);
			match(T__132);
			setState(1675);
			match(T__5);
			setState(1676);
			tupleTypePart();
			setState(1681);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1677);
				match(T__4);
				setState(1678);
				tupleTypePart();
				}
				}
				setState(1683);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1684);
			match(T__6);
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
		case 51:
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
			return precpred(_ctx, 16);
		case 9:
			return precpred(_ctx, 15);
		case 10:
			return precpred(_ctx, 14);
		case 11:
			return precpred(_ctx, 13);
		case 12:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u009c\u0697\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"|\u0002}\u0007}\u0002~\u0007~\u0002\u007f\u0007\u007f\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0001\u0005\u0001\u0105\b\u0001\n\u0001\f\u0001"+
		"\u0108\t\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0003\u0002\u010f\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004\u0118\b\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0003\u0004\u011d\b\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u0124\b\u0005\n\u0005"+
		"\f\u0005\u0127\t\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0003\u0006\u012d\b\u0006\u0001\u0006\u0003\u0006\u0130\b\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u0137"+
		"\b\u0007\n\u0007\f\u0007\u013a\t\u0007\u0001\u0007\u0001\u0007\u0001\b"+
		"\u0005\b\u013f\b\b\n\b\f\b\u0142\t\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003"+
		"\b\u0148\b\b\u0001\b\u0001\b\u0005\b\u014c\b\b\n\b\f\b\u014f\t\b\u0001"+
		"\b\u0001\b\u0005\b\u0153\b\b\n\b\f\b\u0156\t\b\u0001\b\u0001\b\u0003\b"+
		"\u015a\b\b\u0001\b\u0003\b\u015d\b\b\u0001\t\u0001\t\u0001\t\u0003\t\u0162"+
		"\b\t\u0001\t\u0005\t\u0165\b\t\n\t\f\t\u0168\t\t\u0001\t\u0001\t\u0005"+
		"\t\u016c\b\t\n\t\f\t\u016f\t\t\u0001\t\u0001\t\u0003\t\u0173\b\t\u0001"+
		"\n\u0001\n\u0001\n\u0005\n\u0178\b\n\n\n\f\n\u017b\t\n\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0003\u000b\u0185\b\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001"+
		"\r\u0003\r\u018c\b\r\u0001\u000e\u0001\u000e\u0003\u000e\u0190\b\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0005\u0010\u0199\b\u0010\n\u0010\f\u0010\u019c\t\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u01ad\b\u0012\u0001\u0013\u0005"+
		"\u0013\u01b0\b\u0013\n\u0013\f\u0013\u01b3\t\u0013\u0001\u0013\u0001\u0013"+
		"\u0003\u0013\u01b7\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0005\u0013"+
		"\u01bc\b\u0013\n\u0013\f\u0013\u01bf\t\u0013\u0001\u0013\u0003\u0013\u01c2"+
		"\b\u0013\u0001\u0013\u0003\u0013\u01c5\b\u0013\u0001\u0013\u0001\u0013"+
		"\u0003\u0013\u01c9\b\u0013\u0001\u0014\u0001\u0014\u0003\u0014\u01cd\b"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u01d2\b\u0014\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0005\u0015\u01d7\b\u0015\n\u0015\f\u0015"+
		"\u01da\t\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0003\u0016\u01e1\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0005\u0017"+
		"\u01e6\b\u0017\n\u0017\f\u0017\u01e9\t\u0017\u0001\u0018\u0003\u0018\u01ec"+
		"\b\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0005\u001d\u0201\b\u001d\n\u001d\f\u001d\u0204\t\u001d"+
		"\u0001\u001d\u0003\u001d\u0207\b\u001d\u0001\u001d\u0001\u001d\u0001\u001e"+
		"\u0001\u001e\u0003\u001e\u020d\b\u001e\u0001\u001e\u0001\u001e\u0003\u001e"+
		"\u0211\b\u001e\u0001\u001e\u0003\u001e\u0214\b\u001e\u0001\u001e\u0001"+
		"\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0001 \u0001 \u0001 \u0001"+
		"!\u0001!\u0001!\u0001\"\u0005\"\u0222\b\"\n\"\f\"\u0225\t\"\u0001\"\u0001"+
		"\"\u0001\"\u0001\"\u0001\"\u0003\"\u022c\b\"\u0001\"\u0001\"\u0001\"\u0001"+
		"\"\u0001\"\u0001\"\u0003\"\u0234\b\"\u0001#\u0005#\u0237\b#\n#\f#\u023a"+
		"\t#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001"+
		"#\u0001#\u0003#\u0247\b#\u0001$\u0001$\u0001$\u0001$\u0001$\u0003$\u024e"+
		"\b$\u0001%\u0001%\u0001%\u0001%\u0003%\u0254\b%\u0001%\u0001%\u0003%\u0258"+
		"\b%\u0001%\u0001%\u0003%\u025c\b%\u0003%\u025e\b%\u0001&\u0001&\u0003"+
		"&\u0262\b&\u0001&\u0001&\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0003\'\u026d\b\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0003\'\u0278\b\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0003\'\u0282\b\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0003\'\u028c\b\'\u0001(\u0001"+
		"(\u0001(\u0001(\u0001(\u0003(\u0293\b(\u0001(\u0001(\u0005(\u0297\b(\n"+
		"(\f(\u029a\t(\u0001(\u0001(\u0003(\u029e\b(\u0001)\u0001)\u0001)\u0005"+
		")\u02a3\b)\n)\f)\u02a6\t)\u0001*\u0005*\u02a9\b*\n*\f*\u02ac\t*\u0001"+
		"*\u0001*\u0001*\u0001*\u0001*\u0003*\u02b3\b*\u0001*\u0001*\u0001+\u0001"+
		"+\u0001,\u0001,\u0001,\u0001,\u0001,\u0003,\u02be\b,\u0001-\u0001-\u0001"+
		"-\u0005-\u02c3\b-\n-\f-\u02c6\t-\u0001.\u0001.\u0001.\u0001.\u0001.\u0001"+
		".\u0003.\u02ce\b.\u0001/\u0005/\u02d1\b/\n/\f/\u02d4\t/\u00010\u00010"+
		"\u00010\u00010\u00010\u00010\u00030\u02dc\b0\u00011\u00011\u00012\u0001"+
		"2\u00052\u02e2\b2\n2\f2\u02e5\t2\u00012\u00012\u00013\u00013\u00013\u0001"+
		"3\u00013\u00013\u00033\u02ef\b3\u00013\u00013\u00013\u00013\u00013\u0001"+
		"3\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u0001"+
		"3\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u0001"+
		"3\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u0003"+
		"3\u0314\b3\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u0001"+
		"3\u00033\u031f\b3\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u0001"+
		"3\u00033\u0329\b3\u00053\u032b\b3\n3\f3\u032e\t3\u00014\u00014\u00014"+
		"\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00054\u033f\b4\n4\f4\u0342\t4\u00014\u00014\u00034\u0346"+
		"\b4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00054\u0355\b4\n4\f4\u0358\t4\u00014\u00014\u0003"+
		"4\u035c\b4\u00014\u00034\u035f\b4\u00014\u00014\u00014\u00014\u00054\u0365"+
		"\b4\n4\f4\u0368\t4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00034\u0383\b4\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00034\u038c\b4\u00015\u0001"+
		"5\u00015\u00015\u00015\u00015\u00015\u00035\u0395\b5\u00016\u00016\u0001"+
		"6\u00016\u00016\u00017\u00017\u00017\u00017\u00017\u00017\u00017\u0001"+
		"8\u00018\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u0001"+
		"9\u00019\u00019\u00019\u00039\u03b1\b9\u0001:\u0004:\u03b4\b:\u000b:\f"+
		":\u03b5\u0001;\u0001;\u0001;\u0001;\u0001;\u0005;\u03bd\b;\n;\f;\u03c0"+
		"\t;\u0003;\u03c2\b;\u0001;\u0001;\u0001<\u0001<\u0001<\u0001<\u0001=\u0001"+
		"=\u0001=\u0005=\u03cd\b=\n=\f=\u03d0\t=\u0001=\u0001=\u0001=\u0005=\u03d5"+
		"\b=\n=\f=\u03d8\t=\u0001=\u0003=\u03db\b=\u0001>\u0001>\u0001>\u0001>"+
		"\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001"+
		">\u0001>\u0001>\u0001>\u0001>\u0003>\u03ef\b>\u0001?\u0001?\u0001?\u0001"+
		"?\u0001?\u0003?\u03f6\b?\u0001?\u0001?\u0001?\u0001@\u0001@\u0001A\u0001"+
		"A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001A\u0003A\u0405\bA\u0001A\u0001"+
		"A\u0001A\u0001A\u0001A\u0003A\u040c\bA\u0001B\u0001B\u0001B\u0001B\u0001"+
		"B\u0003B\u0413\bB\u0001B\u0001B\u0005B\u0417\bB\nB\fB\u041a\tB\u0001B"+
		"\u0001B\u0001B\u0001B\u0003B\u0420\bB\u0001B\u0001B\u0001C\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0001C\u0001D\u0001D\u0001D\u0001D\u0001D\u0001"+
		"D\u0001E\u0001E\u0001E\u0001E\u0003E\u0435\bE\u0001E\u0003E\u0438\bE\u0001"+
		"E\u0001E\u0001F\u0001F\u0001F\u0001F\u0003F\u0440\bF\u0001F\u0001F\u0001"+
		"G\u0001G\u0001G\u0001G\u0003G\u0448\bG\u0001G\u0001G\u0001H\u0001H\u0001"+
		"I\u0003I\u044f\bI\u0001I\u0001I\u0001I\u0003I\u0454\bI\u0001I\u0001I\u0001"+
		"J\u0001J\u0001K\u0003K\u045b\bK\u0001K\u0001K\u0001K\u0001K\u0001K\u0003"+
		"K\u0462\bK\u0001K\u0001K\u0001L\u0001L\u0001M\u0001M\u0001M\u0003M\u046b"+
		"\bM\u0001N\u0001N\u0001N\u0001N\u0001N\u0003N\u0472\bN\u0001O\u0001O\u0001"+
		"O\u0004O\u0477\bO\u000bO\fO\u0478\u0001P\u0001P\u0001P\u0001P\u0001P\u0003"+
		"P\u0480\bP\u0001P\u0003P\u0483\bP\u0001P\u0003P\u0486\bP\u0001P\u0001"+
		"P\u0001Q\u0001Q\u0001Q\u0005Q\u048d\bQ\nQ\fQ\u0490\tQ\u0001R\u0001R\u0001"+
		"R\u0001R\u0003R\u0496\bR\u0001R\u0003R\u0499\bR\u0001R\u0003R\u049c\b"+
		"R\u0001S\u0001S\u0003S\u04a0\bS\u0001S\u0001S\u0001S\u0001S\u0001S\u0003"+
		"S\u04a7\bS\u0001T\u0001T\u0001T\u0003T\u04ac\bT\u0001T\u0001T\u0001T\u0003"+
		"T\u04b1\bT\u0001U\u0001U\u0001U\u0003U\u04b6\bU\u0001U\u0001U\u0001U\u0003"+
		"U\u04bb\bU\u0001V\u0001V\u0003V\u04bf\bV\u0001W\u0001W\u0001W\u0001W\u0005"+
		"W\u04c5\bW\nW\fW\u04c8\tW\u0001X\u0001X\u0001X\u0003X\u04cd\bX\u0001X"+
		"\u0001X\u0001X\u0003X\u04d2\bX\u0001Y\u0001Y\u0001Z\u0001Z\u0001Z\u0005"+
		"Z\u04d9\bZ\nZ\fZ\u04dc\tZ\u0001[\u0001[\u0001[\u0001[\u0003[\u04e2\b["+
		"\u0001[\u0001[\u0001[\u0003[\u04e7\b[\u0001[\u0001[\u0001[\u0003[\u04ec"+
		"\b[\u0001[\u0001[\u0001[\u0003[\u04f1\b[\u0001[\u0001[\u0001[\u0001[\u0001"+
		"[\u0003[\u04f8\b[\u0001[\u0001[\u0001[\u0001[\u0001[\u0003[\u04ff\b[\u0001"+
		"[\u0001[\u0003[\u0503\b[\u0001[\u0001[\u0003[\u0507\b[\u0003[\u0509\b"+
		"[\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0003\\\u0510\b\\\u0001\\\u0001"+
		"\\\u0001\\\u0001\\\u0003\\\u0516\b\\\u0001\\\u0001\\\u0001\\\u0003\\\u051b"+
		"\b\\\u0001\\\u0001\\\u0001\\\u0003\\\u0520\b\\\u0001\\\u0001\\\u0001\\"+
		"\u0001\\\u0001\\\u0003\\\u0527\b\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001"+
		"\\\u0001\\\u0003\\\u052f\b\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001"+
		"\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0003"+
		"\\\u053f\b\\\u0001\\\u0001\\\u0001\\\u0001\\\u0003\\\u0545\b\\\u0001\\"+
		"\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0003"+
		"\\\u0550\b\\\u0001\\\u0001\\\u0001\\\u0003\\\u0555\b\\\u0001]\u0001]\u0001"+
		"]\u0003]\u055a\b]\u0001]\u0001]\u0001]\u0001]\u0003]\u0560\b]\u0005]\u0562"+
		"\b]\n]\f]\u0565\t]\u0001^\u0001^\u0001^\u0003^\u056a\b^\u0001^\u0001^"+
		"\u0001^\u0001_\u0001_\u0001_\u0001_\u0001`\u0001`\u0001`\u0003`\u0576"+
		"\b`\u0001`\u0001`\u0001`\u0001a\u0001a\u0001a\u0001a\u0001a\u0001a\u0001"+
		"a\u0001a\u0001a\u0001a\u0001a\u0001a\u0001a\u0003a\u0588\ba\u0001b\u0001"+
		"b\u0001b\u0005b\u058d\bb\nb\fb\u0590\tb\u0001c\u0001c\u0001c\u0005c\u0595"+
		"\bc\nc\fc\u0598\tc\u0001d\u0001d\u0001e\u0001e\u0001e\u0001f\u0001f\u0001"+
		"f\u0001g\u0005g\u05a3\bg\ng\fg\u05a6\tg\u0001g\u0001g\u0005g\u05aa\bg"+
		"\ng\fg\u05ad\tg\u0001h\u0001h\u0001h\u0003h\u05b2\bh\u0001h\u0001h\u0001"+
		"h\u0003h\u05b7\bh\u0001i\u0001i\u0001i\u0005i\u05bc\bi\ni\fi\u05bf\ti"+
		"\u0001i\u0001i\u0001j\u0001j\u0001j\u0003j\u05c6\bj\u0001k\u0001k\u0001"+
		"k\u0004k\u05cb\bk\u000bk\fk\u05cc\u0001l\u0001l\u0003l\u05d1\bl\u0001"+
		"m\u0001m\u0001m\u0001m\u0001m\u0001m\u0003m\u05d9\bm\u0003m\u05db\bm\u0001"+
		"m\u0001m\u0001m\u0001n\u0001n\u0003n\u05e2\bn\u0001n\u0001n\u0001n\u0001"+
		"n\u0001n\u0001n\u0001n\u0001n\u0001n\u0001n\u0003n\u05ee\bn\u0001n\u0001"+
		"n\u0001n\u0001n\u0001n\u0001n\u0003n\u05f6\bn\u0001o\u0001o\u0001o\u0001"+
		"o\u0001o\u0001o\u0003o\u05fe\bo\u0001o\u0001o\u0001o\u0001o\u0004o\u0604"+
		"\bo\u000bo\fo\u0605\u0001p\u0001p\u0003p\u060a\bp\u0001p\u0001p\u0001"+
		"p\u0001p\u0003p\u0610\bp\u0001p\u0001p\u0001p\u0001p\u0003p\u0616\bp\u0001"+
		"p\u0001p\u0003p\u061a\bp\u0001q\u0001q\u0001q\u0001q\u0001q\u0001q\u0001"+
		"q\u0004q\u0623\bq\u000bq\fq\u0624\u0001r\u0001r\u0003r\u0629\br\u0001"+
		"r\u0001r\u0001r\u0001r\u0003r\u062f\br\u0001r\u0001r\u0003r\u0633\br\u0001"+
		"s\u0001s\u0001s\u0005s\u0638\bs\ns\fs\u063b\ts\u0001t\u0001t\u0001t\u0001"+
		"t\u0001u\u0001u\u0001u\u0005u\u0644\bu\nu\fu\u0647\tu\u0001v\u0001v\u0001"+
		"v\u0001w\u0001w\u0001w\u0001w\u0001w\u0005w\u0651\bw\nw\fw\u0654\tw\u0003"+
		"w\u0656\bw\u0001w\u0001w\u0001x\u0001x\u0001x\u0003x\u065d\bx\u0001y\u0001"+
		"y\u0001y\u0001y\u0001y\u0005y\u0664\by\ny\fy\u0667\ty\u0001y\u0001y\u0001"+
		"z\u0001z\u0001z\u0001z\u0001z\u0005z\u0670\bz\nz\fz\u0673\tz\u0003z\u0675"+
		"\bz\u0001z\u0001z\u0001{\u0001{\u0001{\u0001{\u0001|\u0001|\u0001}\u0001"+
		"}\u0001}\u0001}\u0001}\u0001~\u0001~\u0001~\u0001~\u0001~\u0001~\u0001"+
		"~\u0001\u007f\u0001\u007f\u0001\u007f\u0001\u007f\u0001\u007f\u0005\u007f"+
		"\u0690\b\u007f\n\u007f\f\u007f\u0693\t\u007f\u0001\u007f\u0001\u007f\u0001"+
		"\u007f\u0000\u0001f\u0080\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012"+
		"\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\"+
		"^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090"+
		"\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8"+
		"\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0"+
		"\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8"+
		"\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0"+
		"\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0000\u001a\u0001\u0000\u000e"+
		"\u0010\u0001\u0000\u000b\f\u0001\u0000\u0012\u0013\u0001\u0000\u0014\u0016"+
		"\u0001\u0000\u0018\u001a\u0003\u0000\u0016\u0016)+\u0094\u0094\u0002\u0000"+
		"\u0095\u0095\u0098\u0098\u0001\u0000\u0091\u0093\u0001\u0000:;\u0002\u0000"+
		"99<<\u0001\u0000=@\u0002\u0000  AA\u0001\u000001\u0001\u000023\u0001\u0000"+
		"KL\u0002\u0000RRTX\u0001\u0000_`\u0001\u0000fg\u0001\u0000il\u0001\u0000"+
		"mp\u0001\u0000rs\u0003\u0000  \u0091\u0091\u0093\u0093\n\u0000\u0003\u0004"+
		"\b\b\u000b\f\u0012\u001f!\',,./PQ]z\u0099\u0099\u0003\u0000\u0001\u0001"+
		"\f\f{{\u0002\u0000//\u0087\u0087\u0001\u0000\u0088\u0090\u0724\u0000\u0100"+
		"\u0001\u0000\u0000\u0000\u0002\u0106\u0001\u0000\u0000\u0000\u0004\u010e"+
		"\u0001\u0000\u0000\u0000\u0006\u0110\u0001\u0000\u0000\u0000\b\u0114\u0001"+
		"\u0000\u0000\u0000\n\u0120\u0001\u0000\u0000\u0000\f\u012f\u0001\u0000"+
		"\u0000\u0000\u000e\u0131\u0001\u0000\u0000\u0000\u0010\u0140\u0001\u0000"+
		"\u0000\u0000\u0012\u015e\u0001\u0000\u0000\u0000\u0014\u0174\u0001\u0000"+
		"\u0000\u0000\u0016\u0184\u0001\u0000\u0000\u0000\u0018\u0186\u0001\u0000"+
		"\u0000\u0000\u001a\u0188\u0001\u0000\u0000\u0000\u001c\u018d\u0001\u0000"+
		"\u0000\u0000\u001e\u0193\u0001\u0000\u0000\u0000 \u0195\u0001\u0000\u0000"+
		"\u0000\"\u019d\u0001\u0000\u0000\u0000$\u01ac\u0001\u0000\u0000\u0000"+
		"&\u01b1\u0001\u0000\u0000\u0000(\u01ca\u0001\u0000\u0000\u0000*\u01d3"+
		"\u0001\u0000\u0000\u0000,\u01e0\u0001\u0000\u0000\u0000.\u01e2\u0001\u0000"+
		"\u0000\u00000\u01eb\u0001\u0000\u0000\u00002\u01f1\u0001\u0000\u0000\u0000"+
		"4\u01f4\u0001\u0000\u0000\u00006\u01f6\u0001\u0000\u0000\u00008\u01f9"+
		"\u0001\u0000\u0000\u0000:\u01fc\u0001\u0000\u0000\u0000<\u020a\u0001\u0000"+
		"\u0000\u0000>\u0217\u0001\u0000\u0000\u0000@\u021a\u0001\u0000\u0000\u0000"+
		"B\u021d\u0001\u0000\u0000\u0000D\u0223\u0001\u0000\u0000\u0000F\u0238"+
		"\u0001\u0000\u0000\u0000H\u0248\u0001\u0000\u0000\u0000J\u025d\u0001\u0000"+
		"\u0000\u0000L\u025f\u0001\u0000\u0000\u0000N\u028b\u0001\u0000\u0000\u0000"+
		"P\u028d\u0001\u0000\u0000\u0000R\u029f\u0001\u0000\u0000\u0000T\u02aa"+
		"\u0001\u0000\u0000\u0000V\u02b6\u0001\u0000\u0000\u0000X\u02b8\u0001\u0000"+
		"\u0000\u0000Z\u02bf\u0001\u0000\u0000\u0000\\\u02c7\u0001\u0000\u0000"+
		"\u0000^\u02d2\u0001\u0000\u0000\u0000`\u02db\u0001\u0000\u0000\u0000b"+
		"\u02dd\u0001\u0000\u0000\u0000d\u02df\u0001\u0000\u0000\u0000f\u02ee\u0001"+
		"\u0000\u0000\u0000h\u038b\u0001\u0000\u0000\u0000j\u0394\u0001\u0000\u0000"+
		"\u0000l\u0396\u0001\u0000\u0000\u0000n\u039b\u0001\u0000\u0000\u0000p"+
		"\u03a2\u0001\u0000\u0000\u0000r\u03b0\u0001\u0000\u0000\u0000t\u03b3\u0001"+
		"\u0000\u0000\u0000v\u03b7\u0001\u0000\u0000\u0000x\u03c5\u0001\u0000\u0000"+
		"\u0000z\u03da\u0001\u0000\u0000\u0000|\u03ee\u0001\u0000\u0000\u0000~"+
		"\u03f0\u0001\u0000\u0000\u0000\u0080\u03fa\u0001\u0000\u0000\u0000\u0082"+
		"\u040b\u0001\u0000\u0000\u0000\u0084\u040d\u0001\u0000\u0000\u0000\u0086"+
		"\u0423\u0001\u0000\u0000\u0000\u0088\u042a\u0001\u0000\u0000\u0000\u008a"+
		"\u0430\u0001\u0000\u0000\u0000\u008c\u043b\u0001\u0000\u0000\u0000\u008e"+
		"\u0443\u0001\u0000\u0000\u0000\u0090\u044b\u0001\u0000\u0000\u0000\u0092"+
		"\u044e\u0001\u0000\u0000\u0000\u0094\u0457\u0001\u0000\u0000\u0000\u0096"+
		"\u045a\u0001\u0000\u0000\u0000\u0098\u0465\u0001\u0000\u0000\u0000\u009a"+
		"\u0467\u0001\u0000\u0000\u0000\u009c\u0471\u0001\u0000\u0000\u0000\u009e"+
		"\u0473\u0001\u0000\u0000\u0000\u00a0\u047a\u0001\u0000\u0000\u0000\u00a2"+
		"\u0489\u0001\u0000\u0000\u0000\u00a4\u0491\u0001\u0000\u0000\u0000\u00a6"+
		"\u049d\u0001\u0000\u0000\u0000\u00a8\u04a8\u0001\u0000\u0000\u0000\u00aa"+
		"\u04b2\u0001\u0000\u0000\u0000\u00ac\u04bc\u0001\u0000\u0000\u0000\u00ae"+
		"\u04c0\u0001\u0000\u0000\u0000\u00b0\u04c9\u0001\u0000\u0000\u0000\u00b2"+
		"\u04d3\u0001\u0000\u0000\u0000\u00b4\u04d5\u0001\u0000\u0000\u0000\u00b6"+
		"\u0508\u0001\u0000\u0000\u0000\u00b8\u0554\u0001\u0000\u0000\u0000\u00ba"+
		"\u0556\u0001\u0000\u0000\u0000\u00bc\u0566\u0001\u0000\u0000\u0000\u00be"+
		"\u056e\u0001\u0000\u0000\u0000\u00c0\u0572\u0001\u0000\u0000\u0000\u00c2"+
		"\u0587\u0001\u0000\u0000\u0000\u00c4\u0589\u0001\u0000\u0000\u0000\u00c6"+
		"\u0591\u0001\u0000\u0000\u0000\u00c8\u0599\u0001\u0000\u0000\u0000\u00ca"+
		"\u059b\u0001\u0000\u0000\u0000\u00cc\u059e\u0001\u0000\u0000\u0000\u00ce"+
		"\u05a4\u0001\u0000\u0000\u0000\u00d0\u05ae\u0001\u0000\u0000\u0000\u00d2"+
		"\u05b8\u0001\u0000\u0000\u0000\u00d4\u05c5\u0001\u0000\u0000\u0000\u00d6"+
		"\u05c7\u0001\u0000\u0000\u0000\u00d8\u05d0\u0001\u0000\u0000\u0000\u00da"+
		"\u05d2\u0001\u0000\u0000\u0000\u00dc\u05df\u0001\u0000\u0000\u0000\u00de"+
		"\u05f7\u0001\u0000\u0000\u0000\u00e0\u0619\u0001\u0000\u0000\u0000\u00e2"+
		"\u061b\u0001\u0000\u0000\u0000\u00e4\u0632\u0001\u0000\u0000\u0000\u00e6"+
		"\u0634\u0001\u0000\u0000\u0000\u00e8\u063c\u0001\u0000\u0000\u0000\u00ea"+
		"\u0640\u0001\u0000\u0000\u0000\u00ec\u0648\u0001\u0000\u0000\u0000\u00ee"+
		"\u064b\u0001\u0000\u0000\u0000\u00f0\u0659\u0001\u0000\u0000\u0000\u00f2"+
		"\u065e\u0001\u0000\u0000\u0000\u00f4\u066a\u0001\u0000\u0000\u0000\u00f6"+
		"\u0678\u0001\u0000\u0000\u0000\u00f8\u067c\u0001\u0000\u0000\u0000\u00fa"+
		"\u067e\u0001\u0000\u0000\u0000\u00fc\u0683\u0001\u0000\u0000\u0000\u00fe"+
		"\u068a\u0001\u0000\u0000\u0000\u0100\u0101\u0003\u0002\u0001\u0000\u0101"+
		"\u0102\u0005\u0000\u0000\u0001\u0102\u0001\u0001\u0000\u0000\u0000\u0103"+
		"\u0105\u0003\u0004\u0002\u0000\u0104\u0103\u0001\u0000\u0000\u0000\u0105"+
		"\u0108\u0001\u0000\u0000\u0000\u0106\u0104\u0001\u0000\u0000\u0000\u0106"+
		"\u0107\u0001\u0000\u0000\u0000\u0107\u0003\u0001\u0000\u0000\u0000\u0108"+
		"\u0106\u0001\u0000\u0000\u0000\u0109\u010f\u0003\u0006\u0003\u0000\u010a"+
		"\u010f\u0003\b\u0004\u0000\u010b\u010f\u0003\u0010\b\u0000\u010c\u010f"+
		"\u0003\u0012\t\u0000\u010d\u010f\u0003$\u0012\u0000\u010e\u0109\u0001"+
		"\u0000\u0000\u0000\u010e\u010a\u0001\u0000\u0000\u0000\u010e\u010b\u0001"+
		"\u0000\u0000\u0000\u010e\u010c\u0001\u0000\u0000\u0000\u010e\u010d\u0001"+
		"\u0000\u0000\u0000\u010f\u0005\u0001\u0000\u0000\u0000\u0110\u0111\u0005"+
		"\u0001\u0000\u0000\u0111\u0112\u0003\u00c6c\u0000\u0112\u0113\u0005\u0002"+
		"\u0000\u0000\u0113\u0007\u0001\u0000\u0000\u0000\u0114\u0115\u0005\u0003"+
		"\u0000\u0000\u0115\u0117\u0003\u00c8d\u0000\u0116\u0118\u0005\u0098\u0000"+
		"\u0000\u0117\u0116\u0001\u0000\u0000\u0000\u0117\u0118\u0001\u0000\u0000"+
		"\u0000\u0118\u0119\u0001\u0000\u0000\u0000\u0119\u011a\u0005\u0004\u0000"+
		"\u0000\u011a\u011c\u0003\n\u0005\u0000\u011b\u011d\u0003\u000e\u0007\u0000"+
		"\u011c\u011b\u0001\u0000\u0000\u0000\u011c\u011d\u0001\u0000\u0000\u0000"+
		"\u011d\u011e\u0001\u0000\u0000\u0000\u011e\u011f\u0005\u0002\u0000\u0000"+
		"\u011f\t\u0001\u0000\u0000\u0000\u0120\u0125\u0003\f\u0006\u0000\u0121"+
		"\u0122\u0005\u0005\u0000\u0000\u0122\u0124\u0003\f\u0006\u0000\u0123\u0121"+
		"\u0001\u0000\u0000\u0000\u0124\u0127\u0001\u0000\u0000\u0000\u0125\u0123"+
		"\u0001\u0000\u0000\u0000\u0125\u0126\u0001\u0000\u0000\u0000\u0126\u000b"+
		"\u0001\u0000\u0000\u0000\u0127\u0125\u0001\u0000\u0000\u0000\u0128\u012c"+
		"\u0003\u00b4Z\u0000\u0129\u012a\u0005\u0006\u0000\u0000\u012a\u012b\u0005"+
		"\u0098\u0000\u0000\u012b\u012d\u0005\u0007\u0000\u0000\u012c\u0129\u0001"+
		"\u0000\u0000\u0000\u012c\u012d\u0001\u0000\u0000\u0000\u012d\u0130\u0001"+
		"\u0000\u0000\u0000\u012e\u0130\u0005\u0098\u0000\u0000\u012f\u0128\u0001"+
		"\u0000\u0000\u0000\u012f\u012e\u0001\u0000\u0000\u0000\u0130\r\u0001\u0000"+
		"\u0000\u0000\u0131\u0132\u0005\b\u0000\u0000\u0132\u0133\u0005\t\u0000"+
		"\u0000\u0133\u0138\u0003f3\u0000\u0134\u0135\u0005\u0005\u0000\u0000\u0135"+
		"\u0137\u0003f3\u0000\u0136\u0134\u0001\u0000\u0000\u0000\u0137\u013a\u0001"+
		"\u0000\u0000\u0000\u0138\u0136\u0001\u0000\u0000\u0000\u0138\u0139\u0001"+
		"\u0000\u0000\u0000\u0139\u013b\u0001\u0000\u0000\u0000\u013a\u0138\u0001"+
		"\u0000\u0000\u0000\u013b\u013c\u0005\n\u0000\u0000\u013c\u000f\u0001\u0000"+
		"\u0000\u0000\u013d\u013f\u0003\"\u0011\u0000\u013e\u013d\u0001\u0000\u0000"+
		"\u0000\u013f\u0142\u0001\u0000\u0000\u0000\u0140\u013e\u0001\u0000\u0000"+
		"\u0000\u0140\u0141\u0001\u0000\u0000\u0000\u0141\u0143\u0001\u0000\u0000"+
		"\u0000\u0142\u0140\u0001\u0000\u0000\u0000\u0143\u0144\u0005\u000b\u0000"+
		"\u0000\u0144\u0145\u0003\u00c6c\u0000\u0145\u0147\u0005\u0006\u0000\u0000"+
		"\u0146\u0148\u0003\u0014\n\u0000\u0147\u0146\u0001\u0000\u0000\u0000\u0147"+
		"\u0148\u0001\u0000\u0000\u0000\u0148\u0149\u0001\u0000\u0000\u0000\u0149"+
		"\u014d\u0005\u0007\u0000\u0000\u014a\u014c\u0003\u001c\u000e\u0000\u014b"+
		"\u014a\u0001\u0000\u0000\u0000\u014c\u014f\u0001\u0000\u0000\u0000\u014d"+
		"\u014b\u0001\u0000\u0000\u0000\u014d\u014e\u0001\u0000\u0000\u0000\u014e"+
		"\u015c\u0001\u0000\u0000\u0000\u014f\u014d\u0001\u0000\u0000\u0000\u0150"+
		"\u0154\u0005\t\u0000\u0000\u0151\u0153\u0003$\u0012\u0000\u0152\u0151"+
		"\u0001\u0000\u0000\u0000\u0153\u0156\u0001\u0000\u0000\u0000\u0154\u0152"+
		"\u0001\u0000\u0000\u0000\u0154\u0155\u0001\u0000\u0000\u0000\u0155\u0157"+
		"\u0001\u0000\u0000\u0000\u0156\u0154\u0001\u0000\u0000\u0000\u0157\u0159"+
		"\u0005\n\u0000\u0000\u0158\u015a\u0005\u0002\u0000\u0000\u0159\u0158\u0001"+
		"\u0000\u0000\u0000\u0159\u015a\u0001\u0000\u0000\u0000\u015a\u015d\u0001"+
		"\u0000\u0000\u0000\u015b\u015d\u0005\u0002\u0000\u0000\u015c\u0150\u0001"+
		"\u0000\u0000\u0000\u015c\u015b\u0001\u0000\u0000\u0000\u015d\u0011\u0001"+
		"\u0000\u0000\u0000\u015e\u015f\u0005\f\u0000\u0000\u015f\u0161\u0003\u00c6"+
		"c\u0000\u0160\u0162\u0003L&\u0000\u0161\u0160\u0001\u0000\u0000\u0000"+
		"\u0161\u0162\u0001\u0000\u0000\u0000\u0162\u0166\u0001\u0000\u0000\u0000"+
		"\u0163\u0165\u0003\u001c\u000e\u0000\u0164\u0163\u0001\u0000\u0000\u0000"+
		"\u0165\u0168\u0001\u0000\u0000\u0000\u0166\u0164\u0001\u0000\u0000\u0000"+
		"\u0166\u0167\u0001\u0000\u0000\u0000\u0167\u0169\u0001\u0000\u0000\u0000"+
		"\u0168\u0166\u0001\u0000\u0000\u0000\u0169\u016d\u0005\t\u0000\u0000\u016a"+
		"\u016c\u0003$\u0012\u0000\u016b\u016a\u0001\u0000\u0000\u0000\u016c\u016f"+
		"\u0001\u0000\u0000\u0000\u016d\u016b\u0001\u0000\u0000\u0000\u016d\u016e"+
		"\u0001\u0000\u0000\u0000\u016e\u0170\u0001\u0000\u0000\u0000\u016f\u016d"+
		"\u0001\u0000\u0000\u0000\u0170\u0172\u0005\n\u0000\u0000\u0171\u0173\u0005"+
		"\u0002\u0000\u0000\u0172\u0171\u0001\u0000\u0000\u0000\u0172\u0173\u0001"+
		"\u0000\u0000\u0000\u0173\u0013\u0001\u0000\u0000\u0000\u0174\u0179\u0003"+
		"\u0016\u000b\u0000\u0175\u0176\u0005\u0005\u0000\u0000\u0176\u0178\u0003"+
		"\u0016\u000b\u0000\u0177\u0175\u0001\u0000\u0000\u0000\u0178\u017b\u0001"+
		"\u0000\u0000\u0000\u0179\u0177\u0001\u0000\u0000\u0000\u0179\u017a\u0001"+
		"\u0000\u0000\u0000\u017a\u0015\u0001\u0000\u0000\u0000\u017b\u0179\u0001"+
		"\u0000\u0000\u0000\u017c\u017d\u0003\u0018\f\u0000\u017d\u017e\u0003\u00c8"+
		"d\u0000\u017e\u017f\u0005\r\u0000\u0000\u017f\u0180\u0003\u001a\r\u0000"+
		"\u0180\u0185\u0001\u0000\u0000\u0000\u0181\u0182\u0003\u0018\f\u0000\u0182"+
		"\u0183\u0003\u001a\r\u0000\u0183\u0185\u0001\u0000\u0000\u0000\u0184\u017c"+
		"\u0001\u0000\u0000\u0000\u0184\u0181\u0001\u0000\u0000\u0000\u0185\u0017"+
		"\u0001\u0000\u0000\u0000\u0186\u0187\u0007\u0000\u0000\u0000\u0187\u0019"+
		"\u0001\u0000\u0000\u0000\u0188\u018b\u0003j5\u0000\u0189\u018a\u0005\u0011"+
		"\u0000\u0000\u018a\u018c\u0003\u00c8d\u0000\u018b\u0189\u0001\u0000\u0000"+
		"\u0000\u018b\u018c\u0001\u0000\u0000\u0000\u018c\u001b\u0001\u0000\u0000"+
		"\u0000\u018d\u018f\u0003\u001e\u000f\u0000\u018e\u0190\u0007\u0001\u0000"+
		"\u0000\u018f\u018e\u0001\u0000\u0000\u0000\u018f\u0190\u0001\u0000\u0000"+
		"\u0000\u0190\u0191\u0001\u0000\u0000\u0000\u0191\u0192\u0003 \u0010\u0000"+
		"\u0192\u001d\u0001\u0000\u0000\u0000\u0193\u0194\u0007\u0002\u0000\u0000"+
		"\u0194\u001f\u0001\u0000\u0000\u0000\u0195\u019a\u0003\u00c6c\u0000\u0196"+
		"\u0197\u0005\u0005\u0000\u0000\u0197\u0199\u0003\u00c6c\u0000\u0198\u0196"+
		"\u0001\u0000\u0000\u0000\u0199\u019c\u0001\u0000\u0000\u0000\u019a\u0198"+
		"\u0001\u0000\u0000\u0000\u019a\u019b\u0001\u0000\u0000\u0000\u019b!\u0001"+
		"\u0000\u0000\u0000\u019c\u019a\u0001\u0000\u0000\u0000\u019d\u019e\u0007"+
		"\u0003\u0000\u0000\u019e#\u0001\u0000\u0000\u0000\u019f\u01ad\u0003&\u0013"+
		"\u0000\u01a0\u01ad\u0003D\"\u0000\u01a1\u01ad\u0003F#\u0000\u01a2\u01ad"+
		"\u0003H$\u0000\u01a3\u01ad\u0003J%\u0000\u01a4\u01ad\u0003N\'\u0000\u01a5"+
		"\u01ad\u0003P(\u0000\u01a6\u01a7\u0003X,\u0000\u01a7\u01a8\u0005\u0002"+
		"\u0000\u0000\u01a8\u01ad\u0001\u0000\u0000\u0000\u01a9\u01aa\u0003\\."+
		"\u0000\u01aa\u01ab\u0005\u0002\u0000\u0000\u01ab\u01ad\u0001\u0000\u0000"+
		"\u0000\u01ac\u019f\u0001\u0000\u0000\u0000\u01ac\u01a0\u0001\u0000\u0000"+
		"\u0000\u01ac\u01a1\u0001\u0000\u0000\u0000\u01ac\u01a2\u0001\u0000\u0000"+
		"\u0000\u01ac\u01a3\u0001\u0000\u0000\u0000\u01ac\u01a4\u0001\u0000\u0000"+
		"\u0000\u01ac\u01a5\u0001\u0000\u0000\u0000\u01ac\u01a6\u0001\u0000\u0000"+
		"\u0000\u01ac\u01a9\u0001\u0000\u0000\u0000\u01ad%\u0001\u0000\u0000\u0000"+
		"\u01ae\u01b0\u0003\"\u0011\u0000\u01af\u01ae\u0001\u0000\u0000\u0000\u01b0"+
		"\u01b3\u0001\u0000\u0000\u0000\u01b1\u01af\u0001\u0000\u0000\u0000\u01b1"+
		"\u01b2\u0001\u0000\u0000\u0000\u01b2\u01b4\u0001\u0000\u0000\u0000\u01b3"+
		"\u01b1\u0001\u0000\u0000\u0000\u01b4\u01b6\u0005\u0017\u0000\u0000\u01b5"+
		"\u01b7\u0003\u0018\f\u0000\u01b6\u01b5\u0001\u0000\u0000\u0000\u01b6\u01b7"+
		"\u0001\u0000\u0000\u0000\u01b7\u01b8\u0001\u0000\u0000\u0000\u01b8\u01b9"+
		"\u0003\u00c2a\u0000\u01b9\u01bd\u0003(\u0014\u0000\u01ba\u01bc\u00032"+
		"\u0019\u0000\u01bb\u01ba\u0001\u0000\u0000\u0000\u01bc\u01bf\u0001\u0000"+
		"\u0000\u0000\u01bd\u01bb\u0001\u0000\u0000\u0000\u01bd\u01be\u0001\u0000"+
		"\u0000\u0000\u01be\u01c1\u0001\u0000\u0000\u0000\u01bf\u01bd\u0001\u0000"+
		"\u0000\u0000\u01c0\u01c2\u00036\u001b\u0000\u01c1\u01c0\u0001\u0000\u0000"+
		"\u0000\u01c1\u01c2\u0001\u0000\u0000\u0000\u01c2\u01c4\u0001\u0000\u0000"+
		"\u0000\u01c3\u01c5\u00038\u001c\u0000\u01c4\u01c3\u0001\u0000\u0000\u0000"+
		"\u01c4\u01c5\u0001\u0000\u0000\u0000\u01c5\u01c6\u0001\u0000\u0000\u0000"+
		"\u01c6\u01c8\u0003<\u001e\u0000\u01c7\u01c9\u0005\u0002\u0000\u0000\u01c8"+
		"\u01c7\u0001\u0000\u0000\u0000\u01c8\u01c9\u0001\u0000\u0000\u0000\u01c9"+
		"\'\u0001\u0000\u0000\u0000\u01ca\u01cc\u0005\u0006\u0000\u0000\u01cb\u01cd"+
		"\u0003.\u0017\u0000\u01cc\u01cb\u0001\u0000\u0000\u0000\u01cc\u01cd\u0001"+
		"\u0000\u0000\u0000\u01cd\u01ce\u0001\u0000\u0000\u0000\u01ce\u01d1\u0005"+
		"\u0007\u0000\u0000\u01cf\u01d0\u0005\r\u0000\u0000\u01d0\u01d2\u0003*"+
		"\u0015\u0000\u01d1\u01cf\u0001\u0000\u0000\u0000\u01d1\u01d2\u0001\u0000"+
		"\u0000\u0000\u01d2)\u0001\u0000\u0000\u0000\u01d3\u01d8\u0003,\u0016\u0000"+
		"\u01d4\u01d5\u0005\u0005\u0000\u0000\u01d5\u01d7\u0003,\u0016\u0000\u01d6"+
		"\u01d4\u0001\u0000\u0000\u0000\u01d7\u01da\u0001\u0000\u0000\u0000\u01d8"+
		"\u01d6\u0001\u0000\u0000\u0000\u01d8\u01d9\u0001\u0000\u0000\u0000\u01d9"+
		"+\u0001\u0000\u0000\u0000\u01da\u01d8\u0001\u0000\u0000\u0000\u01db\u01dc"+
		"\u0003\u00c8d\u0000\u01dc\u01dd\u0005\r\u0000\u0000\u01dd\u01de\u0003"+
		"j5\u0000\u01de\u01e1\u0001\u0000\u0000\u0000\u01df\u01e1\u0003j5\u0000"+
		"\u01e0\u01db\u0001\u0000\u0000\u0000\u01e0\u01df\u0001\u0000\u0000\u0000"+
		"\u01e1-\u0001\u0000\u0000\u0000\u01e2\u01e7\u00030\u0018\u0000\u01e3\u01e4"+
		"\u0005\u0005\u0000\u0000\u01e4\u01e6\u00030\u0018\u0000\u01e5\u01e3\u0001"+
		"\u0000\u0000\u0000\u01e6\u01e9\u0001\u0000\u0000\u0000\u01e7\u01e5\u0001"+
		"\u0000\u0000\u0000\u01e7\u01e8\u0001\u0000\u0000\u0000\u01e8/\u0001\u0000"+
		"\u0000\u0000\u01e9\u01e7\u0001\u0000\u0000\u0000\u01ea\u01ec\u0003\u0018"+
		"\f\u0000\u01eb\u01ea\u0001\u0000\u0000\u0000\u01eb\u01ec\u0001\u0000\u0000"+
		"\u0000\u01ec\u01ed\u0001\u0000\u0000\u0000\u01ed\u01ee\u0003\u00c8d\u0000"+
		"\u01ee\u01ef\u0005\r\u0000\u0000\u01ef\u01f0\u0003j5\u0000\u01f01\u0001"+
		"\u0000\u0000\u0000\u01f1\u01f2\u00034\u001a\u0000\u01f2\u01f3\u0003\u00c4"+
		"b\u0000\u01f33\u0001\u0000\u0000\u0000\u01f4\u01f5\u0007\u0004\u0000\u0000"+
		"\u01f55\u0001\u0000\u0000\u0000\u01f6\u01f7\u0005\u001b\u0000\u0000\u01f7"+
		"\u01f8\u0003:\u001d\u0000\u01f87\u0001\u0000\u0000\u0000\u01f9\u01fa\u0005"+
		"\b\u0000\u0000\u01fa\u01fb\u0003:\u001d\u0000\u01fb9\u0001\u0000\u0000"+
		"\u0000\u01fc\u01fd\u0005\t\u0000\u0000\u01fd\u0202\u0003f3\u0000\u01fe"+
		"\u01ff\u0005\u0002\u0000\u0000\u01ff\u0201\u0003f3\u0000\u0200\u01fe\u0001"+
		"\u0000\u0000\u0000\u0201\u0204\u0001\u0000\u0000\u0000\u0202\u0200\u0001"+
		"\u0000\u0000\u0000\u0202\u0203\u0001\u0000\u0000\u0000\u0203\u0206\u0001"+
		"\u0000\u0000\u0000\u0204\u0202\u0001\u0000\u0000\u0000\u0205\u0207\u0005"+
		"\u0002\u0000\u0000\u0206\u0205\u0001\u0000\u0000\u0000\u0206\u0207\u0001"+
		"\u0000\u0000\u0000\u0207\u0208\u0001\u0000\u0000\u0000\u0208\u0209\u0005"+
		"\n\u0000\u0000\u0209;\u0001\u0000\u0000\u0000\u020a\u020c\u0005\t\u0000"+
		"\u0000\u020b\u020d\u0003>\u001f\u0000\u020c\u020b\u0001\u0000\u0000\u0000"+
		"\u020c\u020d\u0001\u0000\u0000\u0000\u020d\u0210\u0001\u0000\u0000\u0000"+
		"\u020e\u0211\u0003@ \u0000\u020f\u0211\u0003^/\u0000\u0210\u020e\u0001"+
		"\u0000\u0000\u0000\u0210\u020f\u0001\u0000\u0000\u0000\u0211\u0213\u0001"+
		"\u0000\u0000\u0000\u0212\u0214\u0003B!\u0000\u0213\u0212\u0001\u0000\u0000"+
		"\u0000\u0213\u0214\u0001\u0000\u0000\u0000\u0214\u0215\u0001\u0000\u0000"+
		"\u0000\u0215\u0216\u0005\n\u0000\u0000\u0216=\u0001\u0000\u0000\u0000"+
		"\u0217\u0218\u0005\u001c\u0000\u0000\u0218\u0219\u0003d2\u0000\u0219?"+
		"\u0001\u0000\u0000\u0000\u021a\u021b\u0005\u001d\u0000\u0000\u021b\u021c"+
		"\u0003d2\u0000\u021cA\u0001\u0000\u0000\u0000\u021d\u021e\u0005\u001e"+
		"\u0000\u0000\u021e\u021f\u0003d2\u0000\u021fC\u0001\u0000\u0000\u0000"+
		"\u0220\u0222\u0003\"\u0011\u0000\u0221\u0220\u0001\u0000\u0000\u0000\u0222"+
		"\u0225\u0001\u0000\u0000\u0000\u0223\u0221\u0001\u0000\u0000\u0000\u0223"+
		"\u0224\u0001\u0000\u0000\u0000\u0224\u0226\u0001\u0000\u0000\u0000\u0225"+
		"\u0223\u0001\u0000\u0000\u0000\u0226\u0227\u0005\u001f\u0000\u0000\u0227"+
		"\u0228\u0003\u00c2a\u0000\u0228\u022b\u0003L&\u0000\u0229\u022a\u0005"+
		"\r\u0000\u0000\u022a\u022c\u0003j5\u0000\u022b\u0229\u0001\u0000\u0000"+
		"\u0000\u022b\u022c\u0001\u0000\u0000\u0000\u022c\u0233\u0001\u0000\u0000"+
		"\u0000\u022d\u0234\u0003d2\u0000\u022e\u022f\u0005 \u0000\u0000\u022f"+
		"\u0230\u0003f3\u0000\u0230\u0231\u0005\u0002\u0000\u0000\u0231\u0234\u0001"+
		"\u0000\u0000\u0000\u0232\u0234\u0005\u0002\u0000\u0000\u0233\u022d\u0001"+
		"\u0000\u0000\u0000\u0233\u022e\u0001\u0000\u0000\u0000\u0233\u0232\u0001"+
		"\u0000\u0000\u0000\u0234E\u0001\u0000\u0000\u0000\u0235\u0237\u0003\""+
		"\u0011\u0000\u0236\u0235\u0001\u0000\u0000\u0000\u0237\u023a\u0001\u0000"+
		"\u0000\u0000\u0238\u0236\u0001\u0000\u0000\u0000\u0238\u0239\u0001\u0000"+
		"\u0000\u0000\u0239\u023b\u0001\u0000\u0000\u0000\u023a\u0238\u0001\u0000"+
		"\u0000\u0000\u023b\u023c\u0005!\u0000\u0000\u023c\u023d\u0003\u00c2a\u0000"+
		"\u023d\u023e\u0003L&\u0000\u023e\u023f\u0005\r\u0000\u0000\u023f\u0246"+
		"\u0003*\u0015\u0000\u0240\u0247\u0003d2\u0000\u0241\u0242\u0005 \u0000"+
		"\u0000\u0242\u0243\u0003f3\u0000\u0243\u0244\u0005\u0002\u0000\u0000\u0244"+
		"\u0247\u0001\u0000\u0000\u0000\u0245\u0247\u0005\u0002\u0000\u0000\u0246"+
		"\u0240\u0001\u0000\u0000\u0000\u0246\u0241\u0001\u0000\u0000\u0000\u0246"+
		"\u0245\u0001\u0000\u0000\u0000\u0247G\u0001\u0000\u0000\u0000\u0248\u0249"+
		"\u0005\"\u0000\u0000\u0249\u024a\u0003\u00c2a\u0000\u024a\u024b\u0003"+
		"L&\u0000\u024b\u024d\u0003d2\u0000\u024c\u024e\u0005\u0002\u0000\u0000"+
		"\u024d\u024c\u0001\u0000\u0000\u0000\u024d\u024e\u0001\u0000\u0000\u0000"+
		"\u024eI\u0001\u0000\u0000\u0000\u024f\u0250\u0005#\u0000\u0000\u0250\u0251"+
		"\u0003L&\u0000\u0251\u0253\u0003d2\u0000\u0252\u0254\u0005\u0002\u0000"+
		"\u0000\u0253\u0252\u0001\u0000\u0000\u0000\u0253\u0254\u0001\u0000\u0000"+
		"\u0000\u0254\u025e\u0001\u0000\u0000\u0000\u0255\u0257\u0005$\u0000\u0000"+
		"\u0256\u0258\u0003L&\u0000\u0257\u0256\u0001\u0000\u0000\u0000\u0257\u0258"+
		"\u0001\u0000\u0000\u0000\u0258\u0259\u0001\u0000\u0000\u0000\u0259\u025b"+
		"\u0003d2\u0000\u025a\u025c\u0005\u0002\u0000\u0000\u025b\u025a\u0001\u0000"+
		"\u0000\u0000\u025b\u025c\u0001\u0000\u0000\u0000\u025c\u025e\u0001\u0000"+
		"\u0000\u0000\u025d\u024f\u0001\u0000\u0000\u0000\u025d\u0255\u0001\u0000"+
		"\u0000\u0000\u025eK\u0001\u0000\u0000\u0000\u025f\u0261\u0005\u0006\u0000"+
		"\u0000\u0260\u0262\u0003.\u0017\u0000\u0261\u0260\u0001\u0000\u0000\u0000"+
		"\u0261\u0262\u0001\u0000\u0000\u0000\u0262\u0263\u0001\u0000\u0000\u0000"+
		"\u0263\u0264\u0005\u0007\u0000\u0000\u0264M\u0001\u0000\u0000\u0000\u0265"+
		"\u0266\u0005%\u0000\u0000\u0266\u0267\u0005&\u0000\u0000\u0267\u0268\u0003"+
		"\u00c2a\u0000\u0268\u0269\u0005\r\u0000\u0000\u0269\u026c\u0003j5\u0000"+
		"\u026a\u026b\u0005 \u0000\u0000\u026b\u026d\u0003f3\u0000\u026c\u026a"+
		"\u0001\u0000\u0000\u0000\u026c\u026d\u0001\u0000\u0000\u0000\u026d\u026e"+
		"\u0001\u0000\u0000\u0000\u026e\u026f\u0005\u0002\u0000\u0000\u026f\u028c"+
		"\u0001\u0000\u0000\u0000\u0270\u0271\u0005\'\u0000\u0000\u0271\u0272\u0005"+
		"&\u0000\u0000\u0272\u0273\u0003\u00c8d\u0000\u0273\u0274\u0005\r\u0000"+
		"\u0000\u0274\u0277\u0003j5\u0000\u0275\u0276\u0005 \u0000\u0000\u0276"+
		"\u0278\u0003f3\u0000\u0277\u0275\u0001\u0000\u0000\u0000\u0277\u0278\u0001"+
		"\u0000\u0000\u0000\u0278\u0279\u0001\u0000\u0000\u0000\u0279\u027a\u0005"+
		"\u0002\u0000\u0000\u027a\u028c\u0001\u0000\u0000\u0000\u027b\u027c\u0005"+
		"&\u0000\u0000\u027c\u027d\u0003\u00c8d\u0000\u027d\u027e\u0005\r\u0000"+
		"\u0000\u027e\u0281\u0003j5\u0000\u027f\u0280\u0005 \u0000\u0000\u0280"+
		"\u0282\u0003f3\u0000\u0281\u027f\u0001\u0000\u0000\u0000\u0281\u0282\u0001"+
		"\u0000\u0000\u0000\u0282\u0283\u0001\u0000\u0000\u0000\u0283\u0284\u0005"+
		"\u0002\u0000\u0000\u0284\u028c\u0001\u0000\u0000\u0000\u0285\u0286\u0005"+
		"&\u0000\u0000\u0286\u0287\u0003\u00c8d\u0000\u0287\u0288\u0005 \u0000"+
		"\u0000\u0288\u0289\u0003f3\u0000\u0289\u028a\u0005\u0002\u0000\u0000\u028a"+
		"\u028c\u0001\u0000\u0000\u0000\u028b\u0265\u0001\u0000\u0000\u0000\u028b"+
		"\u0270\u0001\u0000\u0000\u0000\u028b\u027b\u0001\u0000\u0000\u0000\u028b"+
		"\u0285\u0001\u0000\u0000\u0000\u028cO\u0001\u0000\u0000\u0000\u028d\u028e"+
		"\u0005%\u0000\u0000\u028e\u028f\u0005(\u0000\u0000\u028f\u0292\u0003\u00c8"+
		"d\u0000\u0290\u0291\u0005\u0013\u0000\u0000\u0291\u0293\u0003R)\u0000"+
		"\u0292\u0290\u0001\u0000\u0000\u0000\u0292\u0293\u0001\u0000\u0000\u0000"+
		"\u0293\u0294\u0001\u0000\u0000\u0000\u0294\u0298\u0005\t\u0000\u0000\u0295"+
		"\u0297\u0003T*\u0000\u0296\u0295\u0001\u0000\u0000\u0000\u0297\u029a\u0001"+
		"\u0000\u0000\u0000\u0298\u0296\u0001\u0000\u0000\u0000\u0298\u0299\u0001"+
		"\u0000\u0000\u0000\u0299\u029b\u0001\u0000\u0000\u0000\u029a\u0298\u0001"+
		"\u0000\u0000\u0000\u029b\u029d\u0005\n\u0000\u0000\u029c\u029e\u0005\u0002"+
		"\u0000\u0000\u029d\u029c\u0001\u0000\u0000\u0000\u029d\u029e\u0001\u0000"+
		"\u0000\u0000\u029eQ\u0001\u0000\u0000\u0000\u029f\u02a4\u0003j5\u0000"+
		"\u02a0\u02a1\u0005\u0005\u0000\u0000\u02a1\u02a3\u0003j5\u0000\u02a2\u02a0"+
		"\u0001\u0000\u0000\u0000\u02a3\u02a6\u0001\u0000\u0000\u0000\u02a4\u02a2"+
		"\u0001\u0000\u0000\u0000\u02a4\u02a5\u0001\u0000\u0000\u0000\u02a5S\u0001"+
		"\u0000\u0000\u0000\u02a6\u02a4\u0001\u0000\u0000\u0000\u02a7\u02a9\u0003"+
		"V+\u0000\u02a8\u02a7\u0001\u0000\u0000\u0000\u02a9\u02ac\u0001\u0000\u0000"+
		"\u0000\u02aa\u02a8\u0001\u0000\u0000\u0000\u02aa\u02ab\u0001\u0000\u0000"+
		"\u0000\u02ab\u02ad\u0001\u0000\u0000\u0000\u02ac\u02aa\u0001\u0000\u0000"+
		"\u0000\u02ad\u02ae\u0003\u00c8d\u0000\u02ae\u02af\u0005\r\u0000\u0000"+
		"\u02af\u02b2\u0003j5\u0000\u02b0\u02b1\u0005 \u0000\u0000\u02b1\u02b3"+
		"\u0003f3\u0000\u02b2\u02b0\u0001\u0000\u0000\u0000\u02b2\u02b3\u0001\u0000"+
		"\u0000\u0000\u02b3\u02b4\u0001\u0000\u0000\u0000\u02b4\u02b5\u0005\u0002"+
		"\u0000\u0000\u02b5U\u0001\u0000\u0000\u0000\u02b6\u02b7\u0007\u0005\u0000"+
		"\u0000\u02b7W\u0001\u0000\u0000\u0000\u02b8\u02b9\u0005,\u0000\u0000\u02b9"+
		"\u02ba\u0007\u0006\u0000\u0000\u02ba\u02bd\u0003Z-\u0000\u02bb\u02bc\u0005"+
		" \u0000\u0000\u02bc\u02be\u0003f3\u0000\u02bd\u02bb\u0001\u0000\u0000"+
		"\u0000\u02bd\u02be\u0001\u0000\u0000\u0000\u02beY\u0001\u0000\u0000\u0000"+
		"\u02bf\u02c4\u0003\u00c8d\u0000\u02c0\u02c1\u0005-\u0000\u0000\u02c1\u02c3"+
		"\u0003\u00c8d\u0000\u02c2\u02c0\u0001\u0000\u0000\u0000\u02c3\u02c6\u0001"+
		"\u0000\u0000\u0000\u02c4\u02c2\u0001\u0000\u0000\u0000\u02c4\u02c5\u0001"+
		"\u0000\u0000\u0000\u02c5[\u0001\u0000\u0000\u0000\u02c6\u02c4\u0001\u0000"+
		"\u0000\u0000\u02c7\u02c8\u0005.\u0000\u0000\u02c8\u02c9\u0003\u00c8d\u0000"+
		"\u02c9\u02ca\u0005 \u0000\u0000\u02ca\u02cd\u0003j5\u0000\u02cb\u02cc"+
		"\u0005/\u0000\u0000\u02cc\u02ce\u0003f3\u0000\u02cd\u02cb\u0001\u0000"+
		"\u0000\u0000\u02cd\u02ce\u0001\u0000\u0000\u0000\u02ce]\u0001\u0000\u0000"+
		"\u0000\u02cf\u02d1\u0003`0\u0000\u02d0\u02cf\u0001\u0000\u0000\u0000\u02d1"+
		"\u02d4\u0001\u0000\u0000\u0000\u02d2\u02d0\u0001\u0000\u0000\u0000\u02d2"+
		"\u02d3\u0001\u0000\u0000\u0000\u02d3_\u0001\u0000\u0000\u0000\u02d4\u02d2"+
		"\u0001\u0000\u0000\u0000\u02d5\u02d6\u0003\u00aeW\u0000\u02d6\u02d7\u0005"+
		"\u0002\u0000\u0000\u02d7\u02dc\u0001\u0000\u0000\u0000\u02d8\u02d9\u0003"+
		"f3\u0000\u02d9\u02da\u0005\u0002\u0000\u0000\u02da\u02dc\u0001\u0000\u0000"+
		"\u0000\u02db\u02d5\u0001\u0000\u0000\u0000\u02db\u02d8\u0001\u0000\u0000"+
		"\u0000\u02dca\u0001\u0000\u0000\u0000\u02dd\u02de\u0007\u0007\u0000\u0000"+
		"\u02dec\u0001\u0000\u0000\u0000\u02df\u02e3\u0005\t\u0000\u0000\u02e0"+
		"\u02e2\u0003`0\u0000\u02e1\u02e0\u0001\u0000\u0000\u0000\u02e2\u02e5\u0001"+
		"\u0000\u0000\u0000\u02e3\u02e1\u0001\u0000\u0000\u0000\u02e3\u02e4\u0001"+
		"\u0000\u0000\u0000\u02e4\u02e6\u0001\u0000\u0000\u0000\u02e5\u02e3\u0001"+
		"\u0000\u0000\u0000\u02e6\u02e7\u0005\n\u0000\u0000\u02e7e\u0001\u0000"+
		"\u0000\u0000\u02e8\u02e9\u00063\uffff\uffff\u0000\u02e9\u02ea\u00058\u0000"+
		"\u0000\u02ea\u02ef\u0003f3\f\u02eb\u02ec\u00059\u0000\u0000\u02ec\u02ef"+
		"\u0003f3\u000b\u02ed\u02ef\u0003h4\u0000\u02ee\u02e8\u0001\u0000\u0000"+
		"\u0000\u02ee\u02eb\u0001\u0000\u0000\u0000\u02ee\u02ed\u0001\u0000\u0000"+
		"\u0000\u02ef\u032c\u0001\u0000\u0000\u0000\u02f0\u02f1\n\n\u0000\u0000"+
		"\u02f1\u02f2\u0007\b\u0000\u0000\u02f2\u032b\u0003f3\u000b\u02f3\u02f4"+
		"\n\t\u0000\u0000\u02f4\u02f5\u0007\t\u0000\u0000\u02f5\u032b\u0003f3\n"+
		"\u02f6\u02f7\n\b\u0000\u0000\u02f7\u02f8\u0007\n\u0000\u0000\u02f8\u032b"+
		"\u0003f3\t\u02f9\u02fa\n\u0007\u0000\u0000\u02fa\u02fb\u0007\u000b\u0000"+
		"\u0000\u02fb\u032b\u0003f3\b\u02fc\u02fd\n\u0006\u0000\u0000\u02fd\u02fe"+
		"\u0005B\u0000\u0000\u02fe\u032b\u0003f3\u0007\u02ff\u0300\n\u0005\u0000"+
		"\u0000\u0300\u0301\u0005C\u0000\u0000\u0301\u032b\u0003f3\u0006\u0302"+
		"\u0303\n\u0004\u0000\u0000\u0303\u0304\u0005D\u0000\u0000\u0304\u032b"+
		"\u0003f3\u0005\u0305\u0306\n\u0003\u0000\u0000\u0306\u0307\u0005E\u0000"+
		"\u0000\u0307\u032b\u0003f3\u0004\u0308\u0309\n\u0010\u0000\u0000\u0309"+
		"\u030a\u0007\f\u0000\u0000\u030a\u032b\u0003\u00b6[\u0000\u030b\u030c"+
		"\n\u000f\u0000\u0000\u030c\u030d\u0007\r\u0000\u0000\u030d\u032b\u0003"+
		"\u00b8\\\u0000\u030e\u030f\n\u000e\u0000\u0000\u030f\u0313\u00054\u0000"+
		"\u0000\u0310\u0311\u0003\u00c8d\u0000\u0311\u0312\u00055\u0000\u0000\u0312"+
		"\u0314\u0001\u0000\u0000\u0000\u0313\u0310\u0001\u0000\u0000\u0000\u0313"+
		"\u0314\u0001\u0000\u0000\u0000\u0314\u0315\u0001\u0000\u0000\u0000\u0315"+
		"\u0316\u0003f3\u0000\u0316\u0317\u00056\u0000\u0000\u0317\u032b\u0001"+
		"\u0000\u0000\u0000\u0318\u0319\n\r\u0000\u0000\u0319\u031a\u00057\u0000"+
		"\u0000\u031a\u031e\u00054\u0000\u0000\u031b\u031c\u0003\u00c8d\u0000\u031c"+
		"\u031d\u00055\u0000\u0000\u031d\u031f\u0001\u0000\u0000\u0000\u031e\u031b"+
		"\u0001\u0000\u0000\u0000\u031e\u031f\u0001\u0000\u0000\u0000\u031f\u0320"+
		"\u0001\u0000\u0000\u0000\u0320\u0321\u0003f3\u0000\u0321\u0322\u00056"+
		"\u0000\u0000\u0322\u032b\u0001\u0000\u0000\u0000\u0323\u0324\n\u0002\u0000"+
		"\u0000\u0324\u0325\u0003b1\u0000\u0325\u0328\u0003f3\u0000\u0326\u0327"+
		"\u0005F\u0000\u0000\u0327\u0329\u0003f3\u0000\u0328\u0326\u0001\u0000"+
		"\u0000\u0000\u0328\u0329\u0001\u0000\u0000\u0000\u0329\u032b\u0001\u0000"+
		"\u0000\u0000\u032a\u02f0\u0001\u0000\u0000\u0000\u032a\u02f3\u0001\u0000"+
		"\u0000\u0000\u032a\u02f6\u0001\u0000\u0000\u0000\u032a\u02f9\u0001\u0000"+
		"\u0000\u0000\u032a\u02fc\u0001\u0000\u0000\u0000\u032a\u02ff\u0001\u0000"+
		"\u0000\u0000\u032a\u0302\u0001\u0000\u0000\u0000\u032a\u0305\u0001\u0000"+
		"\u0000\u0000\u032a\u0308\u0001\u0000\u0000\u0000\u032a\u030b\u0001\u0000"+
		"\u0000\u0000\u032a\u030e\u0001\u0000\u0000\u0000\u032a\u0318\u0001\u0000"+
		"\u0000\u0000\u032a\u0323\u0001\u0000\u0000\u0000\u032b\u032e\u0001\u0000"+
		"\u0000\u0000\u032c\u032a\u0001\u0000\u0000\u0000\u032c\u032d\u0001\u0000"+
		"\u0000\u0000\u032dg\u0001\u0000\u0000\u0000\u032e\u032c\u0001\u0000\u0000"+
		"\u0000\u032f\u0330\u0005\u0006\u0000\u0000\u0330\u0331\u0003f3\u0000\u0331"+
		"\u0332\u0005\u0007\u0000\u0000\u0332\u038c\u0001\u0000\u0000\u0000\u0333"+
		"\u038c\u0005G\u0000\u0000\u0334\u038c\u0005H\u0000\u0000\u0335\u0336\u0005"+
		"I\u0000\u0000\u0336\u0337\u0003f3\u0000\u0337\u0338\u0005J\u0000\u0000"+
		"\u0338\u0340\u0003f3\u0000\u0339\u033a\u0007\u000e\u0000\u0000\u033a\u033b"+
		"\u0003f3\u0000\u033b\u033c\u0005J\u0000\u0000\u033c\u033d\u0003f3\u0000"+
		"\u033d\u033f\u0001\u0000\u0000\u0000\u033e\u0339\u0001\u0000\u0000\u0000"+
		"\u033f\u0342\u0001\u0000\u0000\u0000\u0340\u033e\u0001\u0000\u0000\u0000"+
		"\u0340\u0341\u0001\u0000\u0000\u0000\u0341\u0345\u0001\u0000\u0000\u0000"+
		"\u0342\u0340\u0001\u0000\u0000\u0000\u0343\u0344\u0005M\u0000\u0000\u0344"+
		"\u0346\u0003f3\u0000\u0345\u0343\u0001\u0000\u0000\u0000\u0345\u0346\u0001"+
		"\u0000\u0000\u0000\u0346\u0347\u0001\u0000\u0000\u0000\u0347\u0348\u0005"+
		"N\u0000\u0000\u0348\u038c\u0001\u0000\u0000\u0000\u0349\u034a\u0005I\u0000"+
		"\u0000\u034a\u034b\u0005\u0006\u0000\u0000\u034b\u034c\u0003f3\u0000\u034c"+
		"\u034d\u0005\u0007\u0000\u0000\u034d\u0356\u0003d2\u0000\u034e\u034f\u0005"+
		"L\u0000\u0000\u034f\u0350\u0005\u0006\u0000\u0000\u0350\u0351\u0003f3"+
		"\u0000\u0351\u0352\u0005\u0007\u0000\u0000\u0352\u0353\u0003d2\u0000\u0353"+
		"\u0355\u0001\u0000\u0000\u0000\u0354\u034e\u0001\u0000\u0000\u0000\u0355"+
		"\u0358\u0001\u0000\u0000\u0000\u0356\u0354\u0001\u0000\u0000\u0000\u0356"+
		"\u0357\u0001\u0000\u0000\u0000\u0357\u035b\u0001\u0000\u0000\u0000\u0358"+
		"\u0356\u0001\u0000\u0000\u0000\u0359\u035a\u0005M\u0000\u0000\u035a\u035c"+
		"\u0003d2\u0000\u035b\u0359\u0001\u0000\u0000\u0000\u035b\u035c\u0001\u0000"+
		"\u0000\u0000\u035c\u035e\u0001\u0000\u0000\u0000\u035d\u035f\u0005N\u0000"+
		"\u0000\u035e\u035d\u0001\u0000\u0000\u0000\u035e\u035f\u0001\u0000\u0000"+
		"\u0000\u035f\u038c\u0001\u0000\u0000\u0000\u0360\u0361\u0005O\u0000\u0000"+
		"\u0361\u0366\u0003\u00bc^\u0000\u0362\u0363\u0005\u0005\u0000\u0000\u0363"+
		"\u0365\u0003\u00bc^\u0000\u0364\u0362\u0001\u0000\u0000\u0000\u0365\u0368"+
		"\u0001\u0000\u0000\u0000\u0366\u0364\u0001\u0000\u0000\u0000\u0366\u0367"+
		"\u0001\u0000\u0000\u0000\u0367\u0369\u0001\u0000\u0000\u0000\u0368\u0366"+
		"\u0001\u0000\u0000\u0000\u0369\u036a\u0005\u000e\u0000\u0000\u036a\u036b"+
		"\u0003f3\u0000\u036b\u038c\u0001\u0000\u0000\u0000\u036c\u038c\u0003r"+
		"9\u0000\u036d\u038c\u0003z=\u0000\u036e\u038c\u0003|>\u0000\u036f\u038c"+
		"\u0003~?\u0000\u0370\u038c\u0003\u0084B\u0000\u0371\u038c\u0003\u0088"+
		"D\u0000\u0372\u038c\u0003\u008aE\u0000\u0373\u038c\u0003\u008cF\u0000"+
		"\u0374\u038c\u0003\u008eG\u0000\u0375\u038c\u0003\u0092I\u0000\u0376\u038c"+
		"\u0003\u0096K\u0000\u0377\u038c\u0003\u009eO\u0000\u0378\u038c\u0003\u00a4"+
		"R\u0000\u0379\u038c\u0003\u00a6S\u0000\u037a\u038c\u0003\u00aaU\u0000"+
		"\u037b\u038c\u0003\u00acV\u0000\u037c\u038c\u0005P\u0000\u0000\u037d\u038c"+
		"\u0005Q\u0000\u0000\u037e\u038c\u0003\u00aeW\u0000\u037f\u0380\u0003\u00b4"+
		"Z\u0000\u0380\u0382\u0005\u0006\u0000\u0000\u0381\u0383\u0003\u00eau\u0000"+
		"\u0382\u0381\u0001\u0000\u0000\u0000\u0382\u0383\u0001\u0000\u0000\u0000"+
		"\u0383\u0384\u0001\u0000\u0000\u0000\u0384\u0385\u0005\u0007\u0000\u0000"+
		"\u0385\u038c\u0001\u0000\u0000\u0000\u0386\u038c\u0003\u00f8|\u0000\u0387"+
		"\u038c\u0003\u00fa}\u0000\u0388\u038c\u0003\u00fc~\u0000\u0389\u038c\u0003"+
		"\u00fe\u007f\u0000\u038a\u038c\u0003\u00b4Z\u0000\u038b\u032f\u0001\u0000"+
		"\u0000\u0000\u038b\u0333\u0001\u0000\u0000\u0000\u038b\u0334\u0001\u0000"+
		"\u0000\u0000\u038b\u0335\u0001\u0000\u0000\u0000\u038b\u0349\u0001\u0000"+
		"\u0000\u0000\u038b\u0360\u0001\u0000\u0000\u0000\u038b\u036c\u0001\u0000"+
		"\u0000\u0000\u038b\u036d\u0001\u0000\u0000\u0000\u038b\u036e\u0001\u0000"+
		"\u0000\u0000\u038b\u036f\u0001\u0000\u0000\u0000\u038b\u0370\u0001\u0000"+
		"\u0000\u0000\u038b\u0371\u0001\u0000\u0000\u0000\u038b\u0372\u0001\u0000"+
		"\u0000\u0000\u038b\u0373\u0001\u0000\u0000\u0000\u038b\u0374\u0001\u0000"+
		"\u0000\u0000\u038b\u0375\u0001\u0000\u0000\u0000\u038b\u0376\u0001\u0000"+
		"\u0000\u0000\u038b\u0377\u0001\u0000\u0000\u0000\u038b\u0378\u0001\u0000"+
		"\u0000\u0000\u038b\u0379\u0001\u0000\u0000\u0000\u038b\u037a\u0001\u0000"+
		"\u0000\u0000\u038b\u037b\u0001\u0000\u0000\u0000\u038b\u037c\u0001\u0000"+
		"\u0000\u0000\u038b\u037d\u0001\u0000\u0000\u0000\u038b\u037e\u0001\u0000"+
		"\u0000\u0000\u038b\u037f\u0001\u0000\u0000\u0000\u038b\u0386\u0001\u0000"+
		"\u0000\u0000\u038b\u0387\u0001\u0000\u0000\u0000\u038b\u0388\u0001\u0000"+
		"\u0000\u0000\u038b\u0389\u0001\u0000\u0000\u0000\u038b\u038a\u0001\u0000"+
		"\u0000\u0000\u038ci\u0001\u0000\u0000\u0000\u038d\u0395\u0003\u00f8|\u0000"+
		"\u038e\u0395\u0003\u00fa}\u0000\u038f\u0395\u0003\u00fc~\u0000\u0390\u0395"+
		"\u0003\u00fe\u007f\u0000\u0391\u0395\u0003l6\u0000\u0392\u0395\u0003n"+
		"7\u0000\u0393\u0395\u0003\u00b4Z\u0000\u0394\u038d\u0001\u0000\u0000\u0000"+
		"\u0394\u038e\u0001\u0000\u0000\u0000\u0394\u038f\u0001\u0000\u0000\u0000"+
		"\u0394\u0390\u0001\u0000\u0000\u0000\u0394\u0391\u0001\u0000\u0000\u0000"+
		"\u0394\u0392\u0001\u0000\u0000\u0000\u0394\u0393\u0001\u0000\u0000\u0000"+
		"\u0395k\u0001\u0000\u0000\u0000\u0396\u0397\u0005R\u0000\u0000\u0397\u0398"+
		"\u0005\u0006\u0000\u0000\u0398\u0399\u0003j5\u0000\u0399\u039a\u0005\u0007"+
		"\u0000\u0000\u039am\u0001\u0000\u0000\u0000\u039b\u039c\u0005S\u0000\u0000"+
		"\u039c\u039d\u0005\u0006\u0000\u0000\u039d\u039e\u0003j5\u0000\u039e\u039f"+
		"\u0005\u0005\u0000\u0000\u039f\u03a0\u0003j5\u0000\u03a0\u03a1\u0005\u0007"+
		"\u0000\u0000\u03a1o\u0001\u0000\u0000\u0000\u03a2\u03a3\u0007\u000f\u0000"+
		"\u0000\u03a3q\u0001\u0000\u0000\u0000\u03a4\u03b1\u0005\u0097\u0000\u0000"+
		"\u03a5\u03b1\u0005\u0096\u0000\u0000\u03a6\u03b1\u0003t:\u0000\u03a7\u03b1"+
		"\u0005Y\u0000\u0000\u03a8\u03b1\u0005Z\u0000\u0000\u03a9\u03b1\u0005["+
		"\u0000\u0000\u03aa\u03b1\u0005\\\u0000\u0000\u03ab\u03b1\u0005:\u0000"+
		"\u0000\u03ac\u03b1\u0003\u00eew\u0000\u03ad\u03b1\u0003\u00f2y\u0000\u03ae"+
		"\u03b1\u0003\u00f4z\u0000\u03af\u03b1\u0003v;\u0000\u03b0\u03a4\u0001"+
		"\u0000\u0000\u0000\u03b0\u03a5\u0001\u0000\u0000\u0000\u03b0\u03a6\u0001"+
		"\u0000\u0000\u0000\u03b0\u03a7\u0001\u0000\u0000\u0000\u03b0\u03a8\u0001"+
		"\u0000\u0000\u0000\u03b0\u03a9\u0001\u0000\u0000\u0000\u03b0\u03aa\u0001"+
		"\u0000\u0000\u0000\u03b0\u03ab\u0001\u0000\u0000\u0000\u03b0\u03ac\u0001"+
		"\u0000\u0000\u0000\u03b0\u03ad\u0001\u0000\u0000\u0000\u03b0\u03ae\u0001"+
		"\u0000\u0000\u0000\u03b0\u03af\u0001\u0000\u0000\u0000\u03b1s\u0001\u0000"+
		"\u0000\u0000\u03b2\u03b4\u0007\u0006\u0000\u0000\u03b3\u03b2\u0001\u0000"+
		"\u0000\u0000\u03b4\u03b5\u0001\u0000\u0000\u0000\u03b5\u03b3\u0001\u0000"+
		"\u0000\u0000\u03b5\u03b6\u0001\u0000\u0000\u0000\u03b6u\u0001\u0000\u0000"+
		"\u0000\u03b7\u03b8\u0005S\u0000\u0000\u03b8\u03c1\u0005\t\u0000\u0000"+
		"\u03b9\u03be\u0003x<\u0000\u03ba\u03bb\u0005\u0005\u0000\u0000\u03bb\u03bd"+
		"\u0003x<\u0000\u03bc\u03ba\u0001\u0000\u0000\u0000\u03bd\u03c0\u0001\u0000"+
		"\u0000\u0000\u03be\u03bc\u0001\u0000\u0000\u0000\u03be\u03bf\u0001\u0000"+
		"\u0000\u0000\u03bf\u03c2\u0001\u0000\u0000\u0000\u03c0\u03be\u0001\u0000"+
		"\u0000\u0000\u03c1\u03b9\u0001\u0000\u0000\u0000\u03c1\u03c2\u0001\u0000"+
		"\u0000\u0000\u03c2\u03c3\u0001\u0000\u0000\u0000\u03c3\u03c4\u0005\n\u0000"+
		"\u0000\u03c4w\u0001\u0000\u0000\u0000\u03c5\u03c6\u0003f3\u0000\u03c6"+
		"\u03c7\u0005 \u0000\u0000\u03c7\u03c8\u0003f3\u0000\u03c8y\u0001\u0000"+
		"\u0000\u0000\u03c9\u03ca\u0005]\u0000\u0000\u03ca\u03ce\u0005\t\u0000"+
		"\u0000\u03cb\u03cd\u0003`0\u0000\u03cc\u03cb\u0001\u0000\u0000\u0000\u03cd"+
		"\u03d0\u0001\u0000\u0000\u0000\u03ce\u03cc\u0001\u0000\u0000\u0000\u03ce"+
		"\u03cf\u0001\u0000\u0000\u0000\u03cf\u03d1\u0001\u0000\u0000\u0000\u03d0"+
		"\u03ce\u0001\u0000\u0000\u0000\u03d1\u03db\u0005\n\u0000\u0000\u03d2\u03d6"+
		"\u0005\t\u0000\u0000\u03d3\u03d5\u0003`0\u0000\u03d4\u03d3\u0001\u0000"+
		"\u0000\u0000\u03d5\u03d8\u0001\u0000\u0000\u0000\u03d6\u03d4\u0001\u0000"+
		"\u0000\u0000\u03d6\u03d7\u0001\u0000\u0000\u0000\u03d7\u03d9\u0001\u0000"+
		"\u0000\u0000\u03d8\u03d6\u0001\u0000\u0000\u0000\u03d9\u03db\u0005\n\u0000"+
		"\u0000\u03da\u03c9\u0001\u0000\u0000\u0000\u03da\u03d2\u0001\u0000\u0000"+
		"\u0000\u03db{\u0001\u0000\u0000\u0000\u03dc\u03dd\u0005^\u0000\u0000\u03dd"+
		"\u03de\u0005\u0006\u0000\u0000\u03de\u03df\u0003\u00c8d\u0000\u03df\u03e0"+
		"\u0005\r\u0000\u0000\u03e0\u03e1\u0003j5\u0000\u03e1\u03e2\u0005\u0091"+
		"\u0000\u0000\u03e2\u03e3\u0003f3\u0000\u03e3\u03e4\u0005\u0002\u0000\u0000"+
		"\u03e4\u03e5\u0003f3\u0000\u03e5\u03e6\u0005\u0007\u0000\u0000\u03e6\u03e7"+
		"\u0003d2\u0000\u03e7\u03ef\u0001\u0000\u0000\u0000\u03e8\u03e9\u0005^"+
		"\u0000\u0000\u03e9\u03ea\u0005\u0006\u0000\u0000\u03ea\u03eb\u0003f3\u0000"+
		"\u03eb\u03ec\u0005\u0007\u0000\u0000\u03ec\u03ed\u0003d2\u0000\u03ed\u03ef"+
		"\u0001\u0000\u0000\u0000\u03ee\u03dc\u0001\u0000\u0000\u0000\u03ee\u03e8"+
		"\u0001\u0000\u0000\u0000\u03ef}\u0001\u0000\u0000\u0000\u03f0\u03f1\u0003"+
		"\u0080@\u0000\u03f1\u03f2\u0005\u0006\u0000\u0000\u03f2\u03f5\u0003\u0082"+
		"A\u0000\u03f3\u03f4\u00055\u0000\u0000\u03f4\u03f6\u0003f3\u0000\u03f5"+
		"\u03f3\u0001\u0000\u0000\u0000\u03f5\u03f6\u0001\u0000\u0000\u0000\u03f6"+
		"\u03f7\u0001\u0000\u0000\u0000\u03f7\u03f8\u0005\u0007\u0000\u0000\u03f8"+
		"\u03f9\u0003d2\u0000\u03f9\u007f\u0001\u0000\u0000\u0000\u03fa\u03fb\u0007"+
		"\u0010\u0000\u0000\u03fb\u0081\u0001\u0000\u0000\u0000\u03fc\u03fd\u0003"+
		"\u00ba]\u0000\u03fd\u03fe\u0005\u0002\u0000\u0000\u03fe\u03ff\u0003\u00c8"+
		"d\u0000\u03ff\u0400\u0005\r\u0000\u0000\u0400\u0404\u0003j5\u0000\u0401"+
		"\u0402\u0003\u00b2Y\u0000\u0402\u0403\u0003f3\u0000\u0403\u0405\u0001"+
		"\u0000\u0000\u0000\u0404\u0401\u0001\u0000\u0000\u0000\u0404\u0405\u0001"+
		"\u0000\u0000\u0000\u0405\u040c\u0001\u0000\u0000\u0000\u0406\u0407\u0003"+
		"\u00ba]\u0000\u0407\u0408\u0005\u0002\u0000\u0000\u0408\u0409\u0003j5"+
		"\u0000\u0409\u040c\u0001\u0000\u0000\u0000\u040a\u040c\u0003\u00ba]\u0000"+
		"\u040b\u03fc\u0001\u0000\u0000\u0000\u040b\u0406\u0001\u0000\u0000\u0000"+
		"\u040b\u040a\u0001\u0000\u0000\u0000\u040c\u0083\u0001\u0000\u0000\u0000"+
		"\u040d\u0412\u0005a\u0000\u0000\u040e\u040f\u0005\u0006\u0000\u0000\u040f"+
		"\u0410\u0003f3\u0000\u0410\u0411\u0005\u0007\u0000\u0000\u0411\u0413\u0001"+
		"\u0000\u0000\u0000\u0412\u040e\u0001\u0000\u0000\u0000\u0412\u0413\u0001"+
		"\u0000\u0000\u0000\u0413\u0414\u0001\u0000\u0000\u0000\u0414\u0418\u0005"+
		"\t\u0000\u0000\u0415\u0417\u0003\u0086C\u0000\u0416\u0415\u0001\u0000"+
		"\u0000\u0000\u0417\u041a\u0001\u0000\u0000\u0000\u0418\u0416\u0001\u0000"+
		"\u0000\u0000\u0418\u0419\u0001\u0000\u0000\u0000\u0419\u041f\u0001\u0000"+
		"\u0000\u0000\u041a\u0418\u0001\u0000\u0000\u0000\u041b\u041c\u0005M\u0000"+
		"\u0000\u041c\u041d\u0003f3\u0000\u041d\u041e\u0005\u0002\u0000\u0000\u041e"+
		"\u0420\u0001\u0000\u0000\u0000\u041f\u041b\u0001\u0000\u0000\u0000\u041f"+
		"\u0420\u0001\u0000\u0000\u0000\u0420\u0421\u0001\u0000\u0000\u0000\u0421"+
		"\u0422\u0005\n\u0000\u0000\u0422\u0085\u0001\u0000\u0000\u0000\u0423\u0424"+
		"\u0005b\u0000\u0000\u0424\u0425\u0005\u0006\u0000\u0000\u0425\u0426\u0003"+
		"f3\u0000\u0426\u0427\u0005\u0007\u0000\u0000\u0427\u0428\u0003f3\u0000"+
		"\u0428\u0429\u0005\u0002\u0000\u0000\u0429\u0087\u0001\u0000\u0000\u0000"+
		"\u042a\u042b\u0005c\u0000\u0000\u042b\u042c\u0005\u0006\u0000\u0000\u042c"+
		"\u042d\u0003\u00b0X\u0000\u042d\u042e\u0005\u0007\u0000\u0000\u042e\u042f"+
		"\u0003d2\u0000\u042f\u0089\u0001\u0000\u0000\u0000\u0430\u0434\u0005d"+
		"\u0000\u0000\u0431\u0432\u0003\u00c8d\u0000\u0432\u0433\u0005\r\u0000"+
		"\u0000\u0433\u0435\u0001\u0000\u0000\u0000\u0434\u0431\u0001\u0000\u0000"+
		"\u0000\u0434\u0435\u0001\u0000\u0000\u0000\u0435\u0437\u0001\u0000\u0000"+
		"\u0000\u0436\u0438\u0003j5\u0000\u0437\u0436\u0001\u0000\u0000\u0000\u0437"+
		"\u0438\u0001\u0000\u0000\u0000\u0438\u0439\u0001\u0000\u0000\u0000\u0439"+
		"\u043a\u0003d2\u0000\u043a\u008b\u0001\u0000\u0000\u0000\u043b\u043c\u0005"+
		"e\u0000\u0000\u043c\u043d\u0003\u00b4Z\u0000\u043d\u043f\u0005\u0006\u0000"+
		"\u0000\u043e\u0440\u0003\u00eau\u0000\u043f\u043e\u0001\u0000\u0000\u0000"+
		"\u043f\u0440\u0001\u0000\u0000\u0000\u0440\u0441\u0001\u0000\u0000\u0000"+
		"\u0441\u0442\u0005\u0007\u0000\u0000\u0442\u008d\u0001\u0000\u0000\u0000"+
		"\u0443\u0444\u0003\u0090H\u0000\u0444\u0445\u0003\u00c2a\u0000\u0445\u0447"+
		"\u0005\u0006\u0000\u0000\u0446\u0448\u0003\u00eau\u0000\u0447\u0446\u0001"+
		"\u0000\u0000\u0000\u0447\u0448\u0001\u0000\u0000\u0000\u0448\u0449\u0001"+
		"\u0000\u0000\u0000\u0449\u044a\u0005\u0007\u0000\u0000\u044a\u008f\u0001"+
		"\u0000\u0000\u0000\u044b\u044c\u0007\u0011\u0000\u0000\u044c\u0091\u0001"+
		"\u0000\u0000\u0000\u044d\u044f\u0005h\u0000\u0000\u044e\u044d\u0001\u0000"+
		"\u0000\u0000\u044e\u044f\u0001\u0000\u0000\u0000\u044f\u0450\u0001\u0000"+
		"\u0000\u0000\u0450\u0451\u0003\u0094J\u0000\u0451\u0453\u0005\u0006\u0000"+
		"\u0000\u0452\u0454\u0003\u009aM\u0000\u0453\u0452\u0001\u0000\u0000\u0000"+
		"\u0453\u0454\u0001\u0000\u0000\u0000\u0454\u0455\u0001\u0000\u0000\u0000"+
		"\u0455\u0456\u0005\u0007\u0000\u0000\u0456\u0093\u0001\u0000\u0000\u0000"+
		"\u0457\u0458\u0007\u0012\u0000\u0000\u0458\u0095\u0001\u0000\u0000\u0000"+
		"\u0459\u045b\u0005h\u0000\u0000\u045a\u0459\u0001\u0000\u0000\u0000\u045a"+
		"\u045b\u0001\u0000\u0000\u0000\u045b\u045c\u0001\u0000\u0000\u0000\u045c"+
		"\u045d\u0003\u0098L\u0000\u045d\u045e\u0005\u0006\u0000\u0000\u045e\u0461"+
		"\u0003\u00c2a\u0000\u045f\u0460\u0005\u0005\u0000\u0000\u0460\u0462\u0003"+
		"\u009aM\u0000\u0461\u045f\u0001\u0000\u0000\u0000\u0461\u0462\u0001\u0000"+
		"\u0000\u0000\u0462\u0463\u0001\u0000\u0000\u0000\u0463\u0464\u0005\u0007"+
		"\u0000\u0000\u0464\u0097\u0001\u0000\u0000\u0000\u0465\u0466\u0007\u0013"+
		"\u0000\u0000\u0466\u0099\u0001\u0000\u0000\u0000\u0467\u046a\u0003\u009c"+
		"N\u0000\u0468\u0469\u00055\u0000\u0000\u0469\u046b\u0003f3\u0000\u046a"+
		"\u0468\u0001\u0000\u0000\u0000\u046a\u046b\u0001\u0000\u0000\u0000\u046b"+
		"\u009b\u0001\u0000\u0000\u0000\u046c\u046d\u0003\u00c8d\u0000\u046d\u046e"+
		"\u0005\r\u0000\u0000\u046e\u046f\u0003j5\u0000\u046f\u0472\u0001\u0000"+
		"\u0000\u0000\u0470\u0472\u0003j5\u0000\u0471\u046c\u0001\u0000\u0000\u0000"+
		"\u0471\u0470\u0001\u0000\u0000\u0000\u0472\u009d\u0001\u0000\u0000\u0000"+
		"\u0473\u0474\u0005q\u0000\u0000\u0474\u0476\u0003d2\u0000\u0475\u0477"+
		"\u0003\u00a0P\u0000\u0476\u0475\u0001\u0000\u0000\u0000\u0477\u0478\u0001"+
		"\u0000\u0000\u0000\u0478\u0476\u0001\u0000\u0000\u0000\u0478\u0479\u0001"+
		"\u0000\u0000\u0000\u0479\u009f\u0001\u0000\u0000\u0000\u047a\u0485\u0007"+
		"\u0014\u0000\u0000\u047b\u0482\u0005\u0006\u0000\u0000\u047c\u047d\u0003"+
		"\u00c8d\u0000\u047d\u047e\u0005\r\u0000\u0000\u047e\u0480\u0001\u0000"+
		"\u0000\u0000\u047f\u047c\u0001\u0000\u0000\u0000\u047f\u0480\u0001\u0000"+
		"\u0000\u0000\u0480\u0481\u0001\u0000\u0000\u0000\u0481\u0483\u0003\u00a2"+
		"Q\u0000\u0482\u047f\u0001\u0000\u0000\u0000\u0482\u0483\u0001\u0000\u0000"+
		"\u0000\u0483\u0484\u0001\u0000\u0000\u0000\u0484\u0486\u0005\u0007\u0000"+
		"\u0000\u0485\u047b\u0001\u0000\u0000\u0000\u0485\u0486\u0001\u0000\u0000"+
		"\u0000\u0486\u0487\u0001\u0000\u0000\u0000\u0487\u0488\u0003d2\u0000\u0488"+
		"\u00a1\u0001\u0000\u0000\u0000\u0489\u048e\u0003\u00b4Z\u0000\u048a\u048b"+
		"\u0005\u0005\u0000\u0000\u048b\u048d\u0003\u00b4Z\u0000\u048c\u048a\u0001"+
		"\u0000\u0000\u0000\u048d\u0490\u0001\u0000\u0000\u0000\u048e\u048c\u0001"+
		"\u0000\u0000\u0000\u048e\u048f\u0001\u0000\u0000\u0000\u048f\u00a3\u0001"+
		"\u0000\u0000\u0000\u0490\u048e\u0001\u0000\u0000\u0000\u0491\u049b\u0005"+
		"t\u0000\u0000\u0492\u0498\u0003\u00b4Z\u0000\u0493\u0495\u0005\u0006\u0000"+
		"\u0000\u0494\u0496\u0003f3\u0000\u0495\u0494\u0001\u0000\u0000\u0000\u0495"+
		"\u0496\u0001\u0000\u0000\u0000\u0496\u0497\u0001\u0000\u0000\u0000\u0497"+
		"\u0499\u0005\u0007\u0000\u0000\u0498\u0493\u0001\u0000\u0000\u0000\u0498"+
		"\u0499\u0001\u0000\u0000\u0000\u0499\u049c\u0001\u0000\u0000\u0000\u049a"+
		"\u049c\u0005\u0098\u0000\u0000\u049b\u0492\u0001\u0000\u0000\u0000\u049b"+
		"\u049a\u0001\u0000\u0000\u0000\u049c\u00a5\u0001\u0000\u0000\u0000\u049d"+
		"\u049f\u0005u\u0000\u0000\u049e\u04a0\u0003\u00c8d\u0000\u049f\u049e\u0001"+
		"\u0000\u0000\u0000\u049f\u04a0\u0001\u0000\u0000\u0000\u04a0\u04a1\u0001"+
		"\u0000\u0000\u0000\u04a1\u04a2\u0005\u0006\u0000\u0000\u04a2\u04a3\u0003"+
		"f3\u0000\u04a3\u04a6\u0005\u0007\u0000\u0000\u04a4\u04a5\u0005/\u0000"+
		"\u0000\u04a5\u04a7\u0003\u00a8T\u0000\u04a6\u04a4\u0001\u0000\u0000\u0000"+
		"\u04a6\u04a7\u0001\u0000\u0000\u0000\u04a7\u00a7\u0001\u0000\u0000\u0000"+
		"\u04a8\u04a9\u0005v\u0000\u0000\u04a9\u04ab\u0005\u0006\u0000\u0000\u04aa"+
		"\u04ac\u0003\u00eau\u0000\u04ab\u04aa\u0001\u0000\u0000\u0000\u04ab\u04ac"+
		"\u0001\u0000\u0000\u0000\u04ac\u04ad\u0001\u0000\u0000\u0000\u04ad\u04b0"+
		"\u0005\u0007\u0000\u0000\u04ae\u04af\u0005\u001b\u0000\u0000\u04af\u04b1"+
		"\u0003f3\u0000\u04b0\u04ae\u0001\u0000\u0000\u0000\u04b0\u04b1\u0001\u0000"+
		"\u0000\u0000\u04b1\u00a9\u0001\u0000\u0000\u0000\u04b2\u04b3\u0005v\u0000"+
		"\u0000\u04b3\u04b5\u0005\u0006\u0000\u0000\u04b4\u04b6\u0003\u00eau\u0000"+
		"\u04b5\u04b4\u0001\u0000\u0000\u0000\u04b5\u04b6\u0001\u0000\u0000\u0000"+
		"\u04b6\u04b7\u0001\u0000\u0000\u0000\u04b7\u04ba\u0005\u0007\u0000\u0000"+
		"\u04b8\u04b9\u0005\u001b\u0000\u0000\u04b9\u04bb\u0003f3\u0000\u04ba\u04b8"+
		"\u0001\u0000\u0000\u0000\u04ba\u04bb\u0001\u0000\u0000\u0000\u04bb\u00ab"+
		"\u0001\u0000\u0000\u0000\u04bc\u04be\u0005w\u0000\u0000\u04bd\u04bf\u0003"+
		"f3\u0000\u04be\u04bd\u0001\u0000\u0000\u0000\u04be\u04bf\u0001\u0000\u0000"+
		"\u0000\u04bf\u00ad\u0001\u0000\u0000\u0000\u04c0\u04c1\u0005x\u0000\u0000"+
		"\u04c1\u04c6\u0003\u00b0X\u0000\u04c2\u04c3\u0005\u0005\u0000\u0000\u04c3"+
		"\u04c5\u0003\u00b0X\u0000\u04c4\u04c2\u0001\u0000\u0000\u0000\u04c5\u04c8"+
		"\u0001\u0000\u0000\u0000\u04c6\u04c4\u0001\u0000\u0000\u0000\u04c6\u04c7"+
		"\u0001\u0000\u0000\u0000\u04c7\u00af\u0001\u0000\u0000\u0000\u04c8\u04c6"+
		"\u0001\u0000\u0000\u0000\u04c9\u04cc\u0003\u00c8d\u0000\u04ca\u04cb\u0005"+
		"\r\u0000\u0000\u04cb\u04cd\u0003j5\u0000\u04cc\u04ca\u0001\u0000\u0000"+
		"\u0000\u04cc\u04cd\u0001\u0000\u0000\u0000\u04cd\u04d1\u0001\u0000\u0000"+
		"\u0000\u04ce\u04cf\u0003\u00b2Y\u0000\u04cf\u04d0\u0003f3\u0000\u04d0"+
		"\u04d2\u0001\u0000\u0000\u0000\u04d1\u04ce\u0001\u0000\u0000\u0000\u04d1"+
		"\u04d2\u0001\u0000\u0000\u0000\u04d2\u00b1\u0001\u0000\u0000\u0000\u04d3"+
		"\u04d4\u0007\u0015\u0000\u0000\u04d4\u00b3\u0001\u0000\u0000\u0000\u04d5"+
		"\u04da\u0003\u00c8d\u0000\u04d6\u04d7\u0005-\u0000\u0000\u04d7\u04d9\u0003"+
		"\u00c8d\u0000\u04d8\u04d6\u0001\u0000\u0000\u0000\u04d9\u04dc\u0001\u0000"+
		"\u0000\u0000\u04da\u04d8\u0001\u0000\u0000\u0000\u04da\u04db\u0001\u0000"+
		"\u0000\u0000\u04db\u00b5\u0001\u0000\u0000\u0000\u04dc\u04da\u0001\u0000"+
		"\u0000\u0000\u04dd\u04de\u0003\u0090H\u0000\u04de\u04df\u0003\u00c2a\u0000"+
		"\u04df\u04e1\u0005\u0006\u0000\u0000\u04e0\u04e2\u0003\u00eau\u0000\u04e1"+
		"\u04e0\u0001\u0000\u0000\u0000\u04e1\u04e2\u0001\u0000\u0000\u0000\u04e2"+
		"\u04e3\u0001\u0000\u0000\u0000\u04e3\u04e4\u0005\u0007\u0000\u0000\u04e4"+
		"\u0509\u0001\u0000\u0000\u0000\u04e5\u04e7\u0005h\u0000\u0000\u04e6\u04e5"+
		"\u0001\u0000\u0000\u0000\u04e6\u04e7\u0001\u0000\u0000\u0000\u04e7\u04e8"+
		"\u0001\u0000\u0000\u0000\u04e8\u04e9\u0003\u0094J\u0000\u04e9\u04eb\u0005"+
		"\u0006\u0000\u0000\u04ea\u04ec\u0003\u009aM\u0000\u04eb\u04ea\u0001\u0000"+
		"\u0000\u0000\u04eb\u04ec\u0001\u0000\u0000\u0000\u04ec\u04ed\u0001\u0000"+
		"\u0000\u0000\u04ed\u04ee\u0005\u0007\u0000\u0000\u04ee\u0509\u0001\u0000"+
		"\u0000\u0000\u04ef\u04f1\u0005h\u0000\u0000\u04f0\u04ef\u0001\u0000\u0000"+
		"\u0000\u04f0\u04f1\u0001\u0000\u0000\u0000\u04f1\u04f2\u0001\u0000\u0000"+
		"\u0000\u04f2\u04f3\u0003\u0098L\u0000\u04f3\u04f4\u0005\u0006\u0000\u0000"+
		"\u04f4\u04f7\u0003\u00c2a\u0000\u04f5\u04f6\u0005\u0005\u0000\u0000\u04f6"+
		"\u04f8\u0003\u009aM\u0000\u04f7\u04f5\u0001\u0000\u0000\u0000\u04f7\u04f8"+
		"\u0001\u0000\u0000\u0000\u04f8\u04f9\u0001\u0000\u0000\u0000\u04f9\u04fa"+
		"\u0005\u0007\u0000\u0000\u04fa\u0509\u0001\u0000\u0000\u0000\u04fb\u04fc"+
		"\u0003\u00c8d\u0000\u04fc\u04fe\u0005\u0006\u0000\u0000\u04fd\u04ff\u0003"+
		"\u00eau\u0000\u04fe\u04fd\u0001\u0000\u0000\u0000\u04fe\u04ff\u0001\u0000"+
		"\u0000\u0000\u04ff\u0500\u0001\u0000\u0000\u0000\u0500\u0502\u0005\u0007"+
		"\u0000\u0000\u0501\u0503\u0003\u00ecv\u0000\u0502\u0501\u0001\u0000\u0000"+
		"\u0000\u0502\u0503\u0001\u0000\u0000\u0000\u0503\u0509\u0001\u0000\u0000"+
		"\u0000\u0504\u0506\u0003\u00c8d\u0000\u0505\u0507\u0003\u00ecv\u0000\u0506"+
		"\u0505\u0001\u0000\u0000\u0000\u0506\u0507\u0001\u0000\u0000\u0000\u0507"+
		"\u0509\u0001\u0000\u0000\u0000\u0508\u04dd\u0001\u0000\u0000\u0000\u0508"+
		"\u04e6\u0001\u0000\u0000\u0000\u0508\u04f0\u0001\u0000\u0000\u0000\u0508"+
		"\u04fb\u0001\u0000\u0000\u0000\u0508\u0504\u0001\u0000\u0000\u0000\u0509"+
		"\u00b7\u0001\u0000\u0000\u0000\u050a\u050b\u0003\u0080@\u0000\u050b\u050c"+
		"\u0005\u0006\u0000\u0000\u050c\u050f\u0003\u0082A\u0000\u050d\u050e\u0005"+
		"5\u0000\u0000\u050e\u0510\u0003f3\u0000\u050f\u050d\u0001\u0000\u0000"+
		"\u0000\u050f\u0510\u0001\u0000\u0000\u0000\u0510\u0511\u0001\u0000\u0000"+
		"\u0000\u0511\u0512\u0005\u0007\u0000\u0000\u0512\u0513\u0003d2\u0000\u0513"+
		"\u0555\u0001\u0000\u0000\u0000\u0514\u0516\u0005h\u0000\u0000\u0515\u0514"+
		"\u0001\u0000\u0000\u0000\u0515\u0516\u0001\u0000\u0000\u0000\u0516\u0517"+
		"\u0001\u0000\u0000\u0000\u0517\u0518\u0003\u0094J\u0000\u0518\u051a\u0005"+
		"\u0006\u0000\u0000\u0519\u051b\u0003\u009aM\u0000\u051a\u0519\u0001\u0000"+
		"\u0000\u0000\u051a\u051b\u0001\u0000\u0000\u0000\u051b\u051c\u0001\u0000"+
		"\u0000\u0000\u051c\u051d\u0005\u0007\u0000\u0000\u051d\u0555\u0001\u0000"+
		"\u0000\u0000\u051e\u0520\u0005h\u0000\u0000\u051f\u051e\u0001\u0000\u0000"+
		"\u0000\u051f\u0520\u0001\u0000\u0000\u0000\u0520\u0521\u0001\u0000\u0000"+
		"\u0000\u0521\u0522\u0003\u0098L\u0000\u0522\u0523\u0005\u0006\u0000\u0000"+
		"\u0523\u0526\u0003\u00c2a\u0000\u0524\u0525\u0005\u0005\u0000\u0000\u0525"+
		"\u0527\u0003\u009aM\u0000\u0526\u0524\u0001\u0000\u0000\u0000\u0526\u0527"+
		"\u0001\u0000\u0000\u0000\u0527\u0528\u0001\u0000\u0000\u0000\u0528\u0529"+
		"\u0005\u0007\u0000\u0000\u0529\u0555\u0001\u0000\u0000\u0000\u052a\u052b"+
		"\u0003\u0090H\u0000\u052b\u052c\u0003\u00c2a\u0000\u052c\u052e\u0005\u0006"+
		"\u0000\u0000\u052d\u052f\u0003\u00eau\u0000\u052e\u052d\u0001\u0000\u0000"+
		"\u0000\u052e\u052f\u0001\u0000\u0000\u0000\u052f\u0530\u0001\u0000\u0000"+
		"\u0000\u0530\u0531\u0005\u0007\u0000\u0000\u0531\u0555\u0001\u0000\u0000"+
		"\u0000\u0532\u0533\u0003\u00c8d\u0000\u0533\u0534\u0005\u0006\u0000\u0000"+
		"\u0534\u0535\u0003\u00ba]\u0000\u0535\u0536\u00055\u0000\u0000\u0536\u0537"+
		"\u0003f3\u0000\u0537\u0538\u0005\u0007\u0000\u0000\u0538\u0555\u0001\u0000"+
		"\u0000\u0000\u0539\u053a\u0003\u00c8d\u0000\u053a\u053b\u0005\u0006\u0000"+
		"\u0000\u053b\u053e\u0003\u00c8d\u0000\u053c\u053d\u0005\r\u0000\u0000"+
		"\u053d\u053f\u0003j5\u0000\u053e\u053c\u0001\u0000\u0000\u0000\u053e\u053f"+
		"\u0001\u0000\u0000\u0000\u053f\u0540\u0001\u0000\u0000\u0000\u0540\u0541"+
		"\u0005\u0002\u0000\u0000\u0541\u0544\u0003\u00c8d\u0000\u0542\u0543\u0005"+
		"\r\u0000\u0000\u0543\u0545\u0003j5\u0000\u0544\u0542\u0001\u0000\u0000"+
		"\u0000\u0544\u0545\u0001\u0000\u0000\u0000\u0545\u0546\u0001\u0000\u0000"+
		"\u0000\u0546\u0547\u0005 \u0000\u0000\u0547\u0548\u0003f3\u0000\u0548"+
		"\u0549\u00055\u0000\u0000\u0549\u054a\u0003f3\u0000\u054a\u054b\u0005"+
		"\u0007\u0000\u0000\u054b\u0555\u0001\u0000\u0000\u0000\u054c\u054d\u0003"+
		"\u00c8d\u0000\u054d\u054f\u0005\u0006\u0000\u0000\u054e\u0550\u0003\u00ea"+
		"u\u0000\u054f\u054e\u0001\u0000\u0000\u0000\u054f\u0550\u0001\u0000\u0000"+
		"\u0000\u0550\u0551\u0001\u0000\u0000\u0000\u0551\u0552\u0005\u0007\u0000"+
		"\u0000\u0552\u0555\u0001\u0000\u0000\u0000\u0553\u0555\u0003\u00c8d\u0000"+
		"\u0554\u050a\u0001\u0000\u0000\u0000\u0554\u0515\u0001\u0000\u0000\u0000"+
		"\u0554\u051f\u0001\u0000\u0000\u0000\u0554\u052a\u0001\u0000\u0000\u0000"+
		"\u0554\u0532\u0001\u0000\u0000\u0000\u0554\u0539\u0001\u0000\u0000\u0000"+
		"\u0554\u054c\u0001\u0000\u0000\u0000\u0554\u0553\u0001\u0000\u0000\u0000"+
		"\u0555\u00b9\u0001\u0000\u0000\u0000\u0556\u0559\u0003\u00c8d\u0000\u0557"+
		"\u0558\u0005\r\u0000\u0000\u0558\u055a\u0003j5\u0000\u0559\u0557\u0001"+
		"\u0000\u0000\u0000\u0559\u055a\u0001\u0000\u0000\u0000\u055a\u0563\u0001"+
		"\u0000\u0000\u0000\u055b\u055c\u0005\u0005\u0000\u0000\u055c\u055f\u0003"+
		"\u00c8d\u0000\u055d\u055e\u0005\r\u0000\u0000\u055e\u0560\u0003j5\u0000"+
		"\u055f\u055d\u0001\u0000\u0000\u0000\u055f\u0560\u0001\u0000\u0000\u0000"+
		"\u0560\u0562\u0001\u0000\u0000\u0000\u0561\u055b\u0001\u0000\u0000\u0000"+
		"\u0562\u0565\u0001\u0000\u0000\u0000\u0563\u0561\u0001\u0000\u0000\u0000"+
		"\u0563\u0564\u0001\u0000\u0000\u0000\u0564\u00bb\u0001\u0000\u0000\u0000"+
		"\u0565\u0563\u0001\u0000\u0000\u0000\u0566\u0569\u0003\u00c8d\u0000\u0567"+
		"\u0568\u0005\r\u0000\u0000\u0568\u056a\u0003j5\u0000\u0569\u0567\u0001"+
		"\u0000\u0000\u0000\u0569\u056a\u0001\u0000\u0000\u0000\u056a\u056b\u0001"+
		"\u0000\u0000\u0000\u056b\u056c\u0005 \u0000\u0000\u056c\u056d\u0003f3"+
		"\u0000\u056d\u00bd\u0001\u0000\u0000\u0000\u056e\u056f\u0003\u00c8d\u0000"+
		"\u056f\u0570\u0005\r\u0000\u0000\u0570\u0571\u0003j5\u0000\u0571\u00bf"+
		"\u0001\u0000\u0000\u0000\u0572\u0575\u0003\u00c8d\u0000\u0573\u0574\u0005"+
		"\r\u0000\u0000\u0574\u0576\u0003j5\u0000\u0575\u0573\u0001\u0000\u0000"+
		"\u0000\u0575\u0576\u0001\u0000\u0000\u0000\u0576\u0577\u0001\u0000\u0000"+
		"\u0000\u0577\u0578\u0005 \u0000\u0000\u0578\u0579\u0003f3\u0000\u0579"+
		"\u00c1\u0001\u0000\u0000\u0000\u057a\u057b\u0003\u00c6c\u0000\u057b\u057c"+
		"\u0005-\u0000\u0000\u057c\u057d\u0003\u00c8d\u0000\u057d\u0588\u0001\u0000"+
		"\u0000\u0000\u057e\u057f\u0003\u00f8|\u0000\u057f\u0580\u0005-\u0000\u0000"+
		"\u0580\u0581\u0003\u00c8d\u0000\u0581\u0588\u0001\u0000\u0000\u0000\u0582"+
		"\u0583\u0003\u00fa}\u0000\u0583\u0584\u0005-\u0000\u0000\u0584\u0585\u0003"+
		"\u00c8d\u0000\u0585\u0588\u0001\u0000\u0000\u0000\u0586\u0588\u0003\u00c8"+
		"d\u0000\u0587\u057a\u0001\u0000\u0000\u0000\u0587\u057e\u0001\u0000\u0000"+
		"\u0000\u0587\u0582\u0001\u0000\u0000\u0000\u0587\u0586\u0001\u0000\u0000"+
		"\u0000\u0588\u00c3\u0001\u0000\u0000\u0000\u0589\u058e\u0003\u00c2a\u0000"+
		"\u058a\u058b\u0005\u0005\u0000\u0000\u058b\u058d\u0003\u00c2a\u0000\u058c"+
		"\u058a\u0001\u0000\u0000\u0000\u058d\u0590\u0001\u0000\u0000\u0000\u058e"+
		"\u058c\u0001\u0000\u0000\u0000\u058e\u058f\u0001\u0000\u0000\u0000\u058f"+
		"\u00c5\u0001\u0000\u0000\u0000\u0590\u058e\u0001\u0000\u0000\u0000\u0591"+
		"\u0596\u0003\u00c8d\u0000\u0592\u0593\u00050\u0000\u0000\u0593\u0595\u0003"+
		"\u00c8d\u0000\u0594\u0592\u0001\u0000\u0000\u0000\u0595\u0598\u0001\u0000"+
		"\u0000\u0000\u0596\u0594\u0001\u0000\u0000\u0000\u0596\u0597\u0001\u0000"+
		"\u0000\u0000\u0597\u00c7\u0001\u0000\u0000\u0000\u0598\u0596\u0001\u0000"+
		"\u0000\u0000\u0599\u059a\u0007\u0016\u0000\u0000\u059a\u00c9\u0001\u0000"+
		"\u0000\u0000\u059b\u059c\u0003f3\u0000\u059c\u059d\u0005\u0000\u0000\u0001"+
		"\u059d\u00cb\u0001\u0000\u0000\u0000\u059e\u059f\u0003\u00ceg\u0000\u059f"+
		"\u05a0\u0005\u0000\u0000\u0001\u05a0\u00cd\u0001\u0000\u0000\u0000\u05a1"+
		"\u05a3\u0003\u00d0h\u0000\u05a2\u05a1\u0001\u0000\u0000\u0000\u05a3\u05a6"+
		"\u0001\u0000\u0000\u0000\u05a4\u05a2\u0001\u0000\u0000\u0000\u05a4\u05a5"+
		"\u0001\u0000\u0000\u0000\u05a5\u05ab\u0001\u0000\u0000\u0000\u05a6\u05a4"+
		"\u0001\u0000\u0000\u0000\u05a7\u05aa\u0003\u00d2i\u0000\u05a8\u05aa\u0003"+
		"\u00d4j\u0000\u05a9\u05a7\u0001\u0000\u0000\u0000\u05a9\u05a8\u0001\u0000"+
		"\u0000\u0000\u05aa\u05ad\u0001\u0000\u0000\u0000\u05ab\u05a9\u0001\u0000"+
		"\u0000\u0000\u05ab\u05ac\u0001\u0000\u0000\u0000\u05ac\u00cf\u0001\u0000"+
		"\u0000\u0000\u05ad\u05ab\u0001\u0000\u0000\u0000\u05ae\u05b1\u0007\u0017"+
		"\u0000\u0000\u05af\u05b0\u0005\u0099\u0000\u0000\u05b0\u05b2\u0005\r\u0000"+
		"\u0000\u05b1\u05af\u0001\u0000\u0000\u0000\u05b1\u05b2\u0001\u0000\u0000"+
		"\u0000\u05b2\u05b3\u0001\u0000\u0000\u0000\u05b3\u05b6\u0003\u00b4Z\u0000"+
		"\u05b4\u05b5\u0005-\u0000\u0000\u05b5\u05b7\u0005:\u0000\u0000\u05b6\u05b4"+
		"\u0001\u0000\u0000\u0000\u05b6\u05b7\u0001\u0000\u0000\u0000\u05b7\u00d1"+
		"\u0001\u0000\u0000\u0000\u05b8\u05b9\u0005|\u0000\u0000\u05b9\u05bd\u0003"+
		"\u00b4Z\u0000\u05ba\u05bc\u0003\u00d4j\u0000\u05bb\u05ba\u0001\u0000\u0000"+
		"\u0000\u05bc\u05bf\u0001\u0000\u0000\u0000\u05bd\u05bb\u0001\u0000\u0000"+
		"\u0000\u05bd\u05be\u0001\u0000\u0000\u0000\u05be\u05c0\u0001\u0000\u0000"+
		"\u0000\u05bf\u05bd\u0001\u0000\u0000\u0000\u05c0\u05c1\u0005}\u0000\u0000"+
		"\u05c1\u00d3\u0001\u0000\u0000\u0000\u05c2\u05c6\u0003\u00d6k\u0000\u05c3"+
		"\u05c6\u0003\u00deo\u0000\u05c4\u05c6\u0003\u00e2q\u0000\u05c5\u05c2\u0001"+
		"\u0000\u0000\u0000\u05c5\u05c3\u0001\u0000\u0000\u0000\u05c5\u05c4\u0001"+
		"\u0000\u0000\u0000\u05c6\u00d5\u0001\u0000\u0000\u0000\u05c7\u05c8\u0005"+
		"~\u0000\u0000\u05c8\u05ca\u0003\u00b4Z\u0000\u05c9\u05cb\u0003\u00d8l"+
		"\u0000\u05ca\u05c9\u0001\u0000\u0000\u0000\u05cb\u05cc\u0001\u0000\u0000"+
		"\u0000\u05cc\u05ca\u0001\u0000\u0000\u0000\u05cc\u05cd\u0001\u0000\u0000"+
		"\u0000\u05cd\u00d7\u0001\u0000\u0000\u0000\u05ce\u05d1\u0003\u00dam\u0000"+
		"\u05cf\u05d1\u0003\u00dcn\u0000\u05d0\u05ce\u0001\u0000\u0000\u0000\u05d0"+
		"\u05cf\u0001\u0000\u0000\u0000\u05d1\u00d9\u0001\u0000\u0000\u0000\u05d2"+
		"\u05da\u0005y\u0000\u0000\u05d3\u05d8\u0005\u0099\u0000\u0000\u05d4\u05d5"+
		"\u0005\u0006\u0000\u0000\u05d5\u05d6\u0003f3\u0000\u05d6\u05d7\u0005\u0007"+
		"\u0000\u0000\u05d7\u05d9\u0001\u0000\u0000\u0000\u05d8\u05d4\u0001\u0000"+
		"\u0000\u0000\u05d8\u05d9\u0001\u0000\u0000\u0000\u05d9\u05db\u0001\u0000"+
		"\u0000\u0000\u05da\u05d3\u0001\u0000\u0000\u0000\u05da\u05db\u0001\u0000"+
		"\u0000\u0000\u05db\u05dc\u0001\u0000\u0000\u0000\u05dc\u05dd\u0005\r\u0000"+
		"\u0000\u05dd\u05de\u0003f3\u0000\u05de\u00db\u0001\u0000\u0000\u0000\u05df"+
		"\u05e1\u0005\u007f\u0000\u0000\u05e0\u05e2\u0005\u0099\u0000\u0000\u05e1"+
		"\u05e0\u0001\u0000\u0000\u0000\u05e1\u05e2\u0001\u0000\u0000\u0000\u05e2"+
		"\u05e3\u0001\u0000\u0000\u0000\u05e3\u05f5\u0005\r\u0000\u0000\u05e4\u05e5"+
		"\u0005\u0099\u0000\u0000\u05e5\u05e6\u0005\r\u0000\u0000\u05e6\u05e7\u0003"+
		"j5\u0000\u05e7\u05e8\u0005 \u0000\u0000\u05e8\u05e9\u0003f3\u0000\u05e9"+
		"\u05f6\u0001\u0000\u0000\u0000\u05ea\u05eb\u0005\u0099\u0000\u0000\u05eb"+
		"\u05ed\u0005\u0006\u0000\u0000\u05ec\u05ee\u0003\u00e6s\u0000\u05ed\u05ec"+
		"\u0001\u0000\u0000\u0000\u05ed\u05ee\u0001\u0000\u0000\u0000\u05ee\u05ef"+
		"\u0001\u0000\u0000\u0000\u05ef\u05f0\u0005\u0007\u0000\u0000\u05f0\u05f1"+
		"\u0005\r\u0000\u0000\u05f1\u05f2\u0003j5\u0000\u05f2\u05f3\u0005 \u0000"+
		"\u0000\u05f3\u05f4\u0003f3\u0000\u05f4\u05f6\u0001\u0000\u0000\u0000\u05f5"+
		"\u05e4\u0001\u0000\u0000\u0000\u05f5\u05ea\u0001\u0000\u0000\u0000\u05f6"+
		"\u00dd\u0001\u0000\u0000\u0000\u05f7\u05f8\u0005~\u0000\u0000\u05f8\u05f9"+
		"\u0003\u00b4Z\u0000\u05f9\u05fa\u0005-\u0000\u0000\u05fa\u05fb\u0005\u0099"+
		"\u0000\u0000\u05fb\u05fd\u0005\u0006\u0000\u0000\u05fc\u05fe\u0003\u00e6"+
		"s\u0000\u05fd\u05fc\u0001\u0000\u0000\u0000\u05fd\u05fe\u0001\u0000\u0000"+
		"\u0000\u05fe\u05ff\u0001\u0000\u0000\u0000\u05ff\u0600\u0005\u0007\u0000"+
		"\u0000\u0600\u0601\u0005\r\u0000\u0000\u0601\u0603\u0003j5\u0000\u0602"+
		"\u0604\u0003\u00e0p\u0000\u0603\u0602\u0001\u0000\u0000\u0000\u0604\u0605"+
		"\u0001\u0000\u0000\u0000\u0605\u0603\u0001\u0000\u0000\u0000\u0605\u0606"+
		"\u0001\u0000\u0000\u0000\u0606\u00df\u0001\u0000\u0000\u0000\u0607\u0609"+
		"\u0005\u0080\u0000\u0000\u0608\u060a\u0005\u0099\u0000\u0000\u0609\u0608"+
		"\u0001\u0000\u0000\u0000\u0609\u060a\u0001\u0000\u0000\u0000\u060a\u060b"+
		"\u0001\u0000\u0000\u0000\u060b\u060c\u0005\r\u0000\u0000\u060c\u061a\u0003"+
		"f3\u0000\u060d\u060f\u0005\u0081\u0000\u0000\u060e\u0610\u0005\u0099\u0000"+
		"\u0000\u060f\u060e\u0001\u0000\u0000\u0000\u060f\u0610\u0001\u0000\u0000"+
		"\u0000\u0610\u0611\u0001\u0000\u0000\u0000\u0611\u0612\u0005\r\u0000\u0000"+
		"\u0612\u061a\u0003f3\u0000\u0613\u0615\u0005\u0082\u0000\u0000\u0614\u0616"+
		"\u0005\u0099\u0000\u0000\u0615\u0614\u0001\u0000\u0000\u0000\u0615\u0616"+
		"\u0001\u0000\u0000\u0000\u0616\u0617\u0001\u0000\u0000\u0000\u0617\u0618"+
		"\u0005\r\u0000\u0000\u0618\u061a\u0003f3\u0000\u0619\u0607\u0001\u0000"+
		"\u0000\u0000\u0619\u060d\u0001\u0000\u0000\u0000\u0619\u0613\u0001\u0000"+
		"\u0000\u0000\u061a\u00e1\u0001\u0000\u0000\u0000\u061b\u061c\u0005~\u0000"+
		"\u0000\u061c\u061d\u0003\u00b4Z\u0000\u061d\u061e\u0005-\u0000\u0000\u061e"+
		"\u061f\u0005\u0099\u0000\u0000\u061f\u0620\u0005\r\u0000\u0000\u0620\u0622"+
		"\u0003j5\u0000\u0621\u0623\u0003\u00e4r\u0000\u0622\u0621\u0001\u0000"+
		"\u0000\u0000\u0623\u0624\u0001\u0000\u0000\u0000\u0624\u0622\u0001\u0000"+
		"\u0000\u0000\u0624\u0625\u0001\u0000\u0000\u0000\u0625\u00e3\u0001\u0000"+
		"\u0000\u0000\u0626\u0628\u0005\u001c\u0000\u0000\u0627\u0629\u0005\u0099"+
		"\u0000\u0000\u0628\u0627\u0001\u0000\u0000\u0000\u0628\u0629\u0001\u0000"+
		"\u0000\u0000\u0629\u062a\u0001\u0000\u0000\u0000\u062a\u062b\u0005\r\u0000"+
		"\u0000\u062b\u0633\u0003f3\u0000\u062c\u062e\u0005\u0083\u0000\u0000\u062d"+
		"\u062f\u0005\u0099\u0000\u0000\u062e\u062d\u0001\u0000\u0000\u0000\u062e"+
		"\u062f\u0001\u0000\u0000\u0000\u062f\u0630\u0001\u0000\u0000\u0000\u0630"+
		"\u0631\u0005\r\u0000\u0000\u0631\u0633\u0003f3\u0000\u0632\u0626\u0001"+
		"\u0000\u0000\u0000\u0632\u062c\u0001\u0000\u0000\u0000\u0633\u00e5\u0001"+
		"\u0000\u0000\u0000\u0634\u0639\u0003\u00e8t\u0000\u0635\u0636\u0005\u0005"+
		"\u0000\u0000\u0636\u0638\u0003\u00e8t\u0000\u0637\u0635\u0001\u0000\u0000"+
		"\u0000\u0638\u063b\u0001\u0000\u0000\u0000\u0639\u0637\u0001\u0000\u0000"+
		"\u0000\u0639\u063a\u0001\u0000\u0000\u0000\u063a\u00e7\u0001\u0000\u0000"+
		"\u0000\u063b\u0639\u0001\u0000\u0000\u0000\u063c\u063d\u0005\u0099\u0000"+
		"\u0000\u063d\u063e\u0005\r\u0000\u0000\u063e\u063f\u0003j5\u0000\u063f"+
		"\u00e9\u0001\u0000\u0000\u0000\u0640\u0645\u0003f3\u0000\u0641\u0642\u0005"+
		"\u0005\u0000\u0000\u0642\u0644\u0003f3\u0000\u0643\u0641\u0001\u0000\u0000"+
		"\u0000\u0644\u0647\u0001\u0000\u0000\u0000\u0645\u0643\u0001\u0000\u0000"+
		"\u0000\u0645\u0646\u0001\u0000\u0000\u0000\u0646\u00eb\u0001\u0000\u0000"+
		"\u0000\u0647\u0645\u0001\u0000\u0000\u0000\u0648\u0649\u0005\u0011\u0000"+
		"\u0000\u0649\u064a\u0005\u0080\u0000\u0000\u064a\u00ed\u0001\u0000\u0000"+
		"\u0000\u064b\u064c\u0003p8\u0000\u064c\u0655\u0005\t\u0000\u0000\u064d"+
		"\u0652\u0003\u00f0x\u0000\u064e\u064f\u0005\u0005\u0000\u0000\u064f\u0651"+
		"\u0003\u00f0x\u0000\u0650\u064e\u0001\u0000\u0000\u0000\u0651\u0654\u0001"+
		"\u0000\u0000\u0000\u0652\u0650\u0001\u0000\u0000\u0000\u0652\u0653\u0001"+
		"\u0000\u0000\u0000\u0653\u0656\u0001\u0000\u0000\u0000\u0654\u0652\u0001"+
		"\u0000\u0000\u0000\u0655\u064d\u0001\u0000\u0000\u0000\u0655\u0656\u0001"+
		"\u0000\u0000\u0000\u0656\u0657\u0001\u0000\u0000\u0000\u0657\u0658\u0005"+
		"\n\u0000\u0000\u0658\u00ef\u0001\u0000\u0000\u0000\u0659\u065c\u0003f"+
		"3\u0000\u065a\u065b\u0005\u0084\u0000\u0000\u065b\u065d\u0003f3\u0000"+
		"\u065c\u065a\u0001\u0000\u0000\u0000\u065c\u065d\u0001\u0000\u0000\u0000"+
		"\u065d\u00f1\u0001\u0000\u0000\u0000\u065e\u065f\u0005\u0085\u0000\u0000"+
		"\u065f\u0660\u0005\t\u0000\u0000\u0660\u0665\u0003\u00c0`\u0000\u0661"+
		"\u0662\u0005\u0005\u0000\u0000\u0662\u0664\u0003\u00c0`\u0000\u0663\u0661"+
		"\u0001\u0000\u0000\u0000\u0664\u0667\u0001\u0000\u0000\u0000\u0665\u0663"+
		"\u0001\u0000\u0000\u0000\u0665\u0666\u0001\u0000\u0000\u0000\u0666\u0668"+
		"\u0001\u0000\u0000\u0000\u0667\u0665\u0001\u0000\u0000\u0000\u0668\u0669"+
		"\u0005\n\u0000\u0000\u0669\u00f3\u0001\u0000\u0000\u0000\u066a\u066b\u0005"+
		"\u0086\u0000\u0000\u066b\u0674\u0005\t\u0000\u0000\u066c\u0671\u0003\u00f6"+
		"{\u0000\u066d\u066e\u0005\u0005\u0000\u0000\u066e\u0670\u0003\u00f6{\u0000"+
		"\u066f\u066d\u0001\u0000\u0000\u0000\u0670\u0673\u0001\u0000\u0000\u0000"+
		"\u0671\u066f\u0001\u0000\u0000\u0000\u0671\u0672\u0001\u0000\u0000\u0000"+
		"\u0672\u0675\u0001\u0000\u0000\u0000\u0673\u0671\u0001\u0000\u0000\u0000"+
		"\u0674\u066c\u0001\u0000\u0000\u0000\u0674\u0675\u0001\u0000\u0000\u0000"+
		"\u0675\u0676\u0001\u0000\u0000\u0000\u0676\u0677\u0005\n\u0000\u0000\u0677"+
		"\u00f5\u0001\u0000\u0000\u0000\u0678\u0679\u0003f3\u0000\u0679\u067a\u0007"+
		"\u0018\u0000\u0000\u067a\u067b\u0003f3\u0000\u067b\u00f7\u0001\u0000\u0000"+
		"\u0000\u067c\u067d\u0007\u0019\u0000\u0000\u067d\u00f9\u0001\u0000\u0000"+
		"\u0000\u067e\u067f\u0003p8\u0000\u067f\u0680\u0005\u0006\u0000\u0000\u0680"+
		"\u0681\u0003j5\u0000\u0681\u0682\u0005\u0007\u0000\u0000\u0682\u00fb\u0001"+
		"\u0000\u0000\u0000\u0683\u0684\u0005\u0086\u0000\u0000\u0684\u0685\u0005"+
		"\u0006\u0000\u0000\u0685\u0686\u0003j5\u0000\u0686\u0687\u0005\u0005\u0000"+
		"\u0000\u0687\u0688\u0003j5\u0000\u0688\u0689\u0005\u0007\u0000\u0000\u0689"+
		"\u00fd\u0001\u0000\u0000\u0000\u068a\u068b\u0005\u0085\u0000\u0000\u068b"+
		"\u068c\u0005\u0006\u0000\u0000\u068c\u0691\u0003\u00be_\u0000\u068d\u068e"+
		"\u0005\u0005\u0000\u0000\u068e\u0690\u0003\u00be_\u0000\u068f\u068d\u0001"+
		"\u0000\u0000\u0000\u0690\u0693\u0001\u0000\u0000\u0000\u0691\u068f\u0001"+
		"\u0000\u0000\u0000\u0691\u0692\u0001\u0000\u0000\u0000\u0692\u0694\u0001"+
		"\u0000\u0000\u0000\u0693\u0691\u0001\u0000\u0000\u0000\u0694\u0695\u0005"+
		"\u0007\u0000\u0000\u0695\u00ff\u0001\u0000\u0000\u0000\u00ba\u0106\u010e"+
		"\u0117\u011c\u0125\u012c\u012f\u0138\u0140\u0147\u014d\u0154\u0159\u015c"+
		"\u0161\u0166\u016d\u0172\u0179\u0184\u018b\u018f\u019a\u01ac\u01b1\u01b6"+
		"\u01bd\u01c1\u01c4\u01c8\u01cc\u01d1\u01d8\u01e0\u01e7\u01eb\u0202\u0206"+
		"\u020c\u0210\u0213\u0223\u022b\u0233\u0238\u0246\u024d\u0253\u0257\u025b"+
		"\u025d\u0261\u026c\u0277\u0281\u028b\u0292\u0298\u029d\u02a4\u02aa\u02b2"+
		"\u02bd\u02c4\u02cd\u02d2\u02db\u02e3\u02ee\u0313\u031e\u0328\u032a\u032c"+
		"\u0340\u0345\u0356\u035b\u035e\u0366\u0382\u038b\u0394\u03b0\u03b5\u03be"+
		"\u03c1\u03ce\u03d6\u03da\u03ee\u03f5\u0404\u040b\u0412\u0418\u041f\u0434"+
		"\u0437\u043f\u0447\u044e\u0453\u045a\u0461\u046a\u0471\u0478\u047f\u0482"+
		"\u0485\u048e\u0495\u0498\u049b\u049f\u04a6\u04ab\u04b0\u04b5\u04ba\u04be"+
		"\u04c6\u04cc\u04d1\u04da\u04e1\u04e6\u04eb\u04f0\u04f7\u04fe\u0502\u0506"+
		"\u0508\u050f\u0515\u051a\u051f\u0526\u052e\u053e\u0544\u054f\u0554\u0559"+
		"\u055f\u0563\u0569\u0575\u0587\u058e\u0596\u05a4\u05a9\u05ab\u05b1\u05b6"+
		"\u05bd\u05c5\u05cc\u05d0\u05d8\u05da\u05e1\u05ed\u05f5\u05fd\u0605\u0609"+
		"\u060f\u0615\u0619\u0624\u0628\u062e\u0632\u0639\u0645\u0652\u0655\u065c"+
		"\u0665\u0671\u0674\u0691";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}