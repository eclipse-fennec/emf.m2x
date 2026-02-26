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
		T__143=144, T__144=145, LINE_COMMENT=146, RESET_ASSIGN=147, ADD_ASSIGN=148, 
		ORDERED_COPY=149, SUBTRACT_ASSIGN=150, NOT_EQUAL_EXEQ=151, STEREOTYPE_ID=152, 
		DOUBLE_QUOTED_STRING=153, REAL_LITERAL=154, INTEGER_LITERAL=155, STRING_LITERAL=156, 
		IDENTIFIER=157, WS=158, BLOCK_COMMENT=159;
	public static final int
		RULE_compilationUnitEntry = 0, RULE_compilationUnit = 1, RULE_unitElement = 2, 
		RULE_qvtoImportDecl = 3, RULE_importedNameList = 4, RULE_modeltypeDecl = 5, 
		RULE_packageRefList = 6, RULE_packageRef = 7, RULE_modeltypeWhere = 8, 
		RULE_transformationDef = 9, RULE_libraryDef = 10, RULE_modelParamList = 11, 
		RULE_modelParam = 12, RULE_directionKind = 13, RULE_typeSpec = 14, RULE_moduleUsage = 15, 
		RULE_moduleUsageKind = 16, RULE_moduleRefList = 17, RULE_qualifier = 18, 
		RULE_moduleElement = 19, RULE_mappingDef = 20, RULE_mappingSignature = 21, 
		RULE_resultList = 22, RULE_resultParam = 23, RULE_paramList = 24, RULE_param = 25, 
		RULE_mappingExtension = 26, RULE_mappingExtensionKind = 27, RULE_whenClause = 28, 
		RULE_whereClause = 29, RULE_guardBlock = 30, RULE_mappingBody = 31, RULE_initSection = 32, 
		RULE_populationSection = 33, RULE_endSection = 34, RULE_helperDef = 35, 
		RULE_queryDef = 36, RULE_constructorDef = 37, RULE_entryDef = 38, RULE_simpleSignature = 39, 
		RULE_propertyDecl = 40, RULE_intermediateClassDef = 41, RULE_typeList = 42, 
		RULE_classifierFeature = 43, RULE_classifierFeatureModifier = 44, RULE_tagDecl = 45, 
		RULE_tagTarget = 46, RULE_typedefDecl = 47, RULE_statementList = 48, RULE_statement = 49, 
		RULE_assignOp = 50, RULE_block = 51, RULE_expression = 52, RULE_primaryExpression = 53, 
		RULE_typeExpression = 54, RULE_listType = 55, RULE_dictType = 56, RULE_collectionKind = 57, 
		RULE_literalExpression = 58, RULE_stringLiteral_ = 59, RULE_dictLiteral = 60, 
		RULE_dictLiteralPart = 61, RULE_blockExp = 62, RULE_whileExp = 63, RULE_forExp = 64, 
		RULE_forKind = 65, RULE_forVarList = 66, RULE_switchExp = 67, RULE_switchAlt = 68, 
		RULE_computeExp = 69, RULE_objectExp = 70, RULE_newExp = 71, RULE_mappingCallExp = 72, 
		RULE_mappingCallKind = 73, RULE_resolveExp = 74, RULE_resolveKind = 75, 
		RULE_resolveInExp = 76, RULE_resolveInKind = 77, RULE_resolveArgs = 78, 
		RULE_resolveTarget = 79, RULE_tryExp = 80, RULE_catchClause = 81, RULE_exceptionTypeList = 82, 
		RULE_raiseExp = 83, RULE_assertExp = 84, RULE_logCallExp = 85, RULE_logExp = 86, 
		RULE_returnExp = 87, RULE_varDeclExp = 88, RULE_varDeclarator = 89, RULE_varInitOp = 90, 
		RULE_pathName = 91, RULE_propertyOrCallSuffix = 92, RULE_iteratorOrOperationCall = 93, 
		RULE_iteratorVariables = 94, RULE_letBinding = 95, RULE_tupleTypePart = 96, 
		RULE_tupleLiteralPart = 97, RULE_scopedName = 98, RULE_scopedNameList = 99, 
		RULE_qualifiedName = 100, RULE_qvtoIdentifier = 101, RULE_expressionEntry = 102, 
		RULE_completeOclDocumentEntry = 103, RULE_completeOclDocument = 104, RULE_importDeclaration = 105, 
		RULE_packageDeclaration = 106, RULE_contextDeclaration = 107, RULE_classifierContextDeclaration = 108, 
		RULE_classifierContextBody = 109, RULE_invariantConstraint = 110, RULE_definitionConstraint = 111, 
		RULE_operationContextDeclaration = 112, RULE_operationContextBody = 113, 
		RULE_propertyContextDeclaration = 114, RULE_propertyContextBody = 115, 
		RULE_parameterList = 116, RULE_parameter = 117, RULE_argumentList = 118, 
		RULE_isMarkedPre = 119, RULE_collectionLiteral = 120, RULE_collectionLiteralPart = 121, 
		RULE_tupleLiteral = 122, RULE_mapLiteral = 123, RULE_mapLiteralPart = 124, 
		RULE_primitiveType = 125, RULE_collectionType = 126, RULE_mapType = 127, 
		RULE_tupleType = 128;
	private static String[] makeRuleNames() {
		return new String[] {
			"compilationUnitEntry", "compilationUnit", "unitElement", "qvtoImportDecl", 
			"importedNameList", "modeltypeDecl", "packageRefList", "packageRef", 
			"modeltypeWhere", "transformationDef", "libraryDef", "modelParamList", 
			"modelParam", "directionKind", "typeSpec", "moduleUsage", "moduleUsageKind", 
			"moduleRefList", "qualifier", "moduleElement", "mappingDef", "mappingSignature", 
			"resultList", "resultParam", "paramList", "param", "mappingExtension", 
			"mappingExtensionKind", "whenClause", "whereClause", "guardBlock", "mappingBody", 
			"initSection", "populationSection", "endSection", "helperDef", "queryDef", 
			"constructorDef", "entryDef", "simpleSignature", "propertyDecl", "intermediateClassDef", 
			"typeList", "classifierFeature", "classifierFeatureModifier", "tagDecl", 
			"tagTarget", "typedefDecl", "statementList", "statement", "assignOp", 
			"block", "expression", "primaryExpression", "typeExpression", "listType", 
			"dictType", "collectionKind", "literalExpression", "stringLiteral_", 
			"dictLiteral", "dictLiteralPart", "blockExp", "whileExp", "forExp", "forKind", 
			"forVarList", "switchExp", "switchAlt", "computeExp", "objectExp", "newExp", 
			"mappingCallExp", "mappingCallKind", "resolveExp", "resolveKind", "resolveInExp", 
			"resolveInKind", "resolveArgs", "resolveTarget", "tryExp", "catchClause", 
			"exceptionTypeList", "raiseExp", "assertExp", "logCallExp", "logExp", 
			"returnExp", "varDeclExp", "varDeclarator", "varInitOp", "pathName", 
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
			"'('", "')'", "'where'", "'{'", "'}'", "'transformation'", "'library'", 
			"':'", "'in'", "'out'", "'inout'", "'@'", "'access'", "'extends'", "'abstract'", 
			"'blackbox'", "'static'", "'mapping'", "'inherits'", "'merges'", "'disjuncts'", 
			"'when'", "'init'", "'population'", "'end'", "'helper'", "'='", "'query'", 
			"'constructor'", "'main'", "'entry'", "'intermediate'", "'property'", 
			"'configuration'", "'class'", "'readonly'", "'references'", "'composes'", 
			"'tag'", "'::'", "'typedef'", "'with'", "'.'", "'?.'", "'->'", "'?->'", 
			"'['", "'|'", "']'", "'!'", "'not'", "'-'", "'/'", "'+'", "'<'", "'>'", 
			"'<='", "'>='", "'<>'", "'and'", "'or'", "'xor'", "'implies'", "'default'", 
			"'self'", "'this'", "'if'", "'then'", "'elseif'", "'elif'", "'else'", 
			"'endif'", "'let'", "'break'", "'continue'", "'List'", "'Dict'", "'Set'", 
			"'OrderedSet'", "'Bag'", "'Sequence'", "'Collection'", "'true'", "'false'", 
			"'null'", "'invalid'", "'do'", "'while'", "'forEach'", "'forOne'", "'switch'", 
			"'case'", "'compute'", "'object'", "'new'", "'map'", "'xmap'", "'late'", 
			"'resolve'", "'resolveone'", "'invresolve'", "'invresolveone'", "'resolveIn'", 
			"'resolveoneIn'", "'invresolveIn'", "'invresolveoneIn'", "'try'", "'catch'", 
			"'except'", "'raise'", "'assert'", "'log'", "'return'", "'var'", "'inv'", 
			"'result'", "'include'", "'package'", "'endpackage'", "'context'", "'def'", 
			"'pre'", "'post'", "'body'", "'derive'", "'..'", "'Tuple'", "'Map'", 
			"'<-'", "'Boolean'", "'Integer'", "'Real'", "'String'", "'UnlimitedNatural'", 
			"'OclAny'", "'OclVoid'", "'OclInvalid'", "'OclMessage'", null, "':='", 
			"'+='", "'::='", "'-='", "'!='"
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
			null, null, "LINE_COMMENT", "RESET_ASSIGN", "ADD_ASSIGN", "ORDERED_COPY", 
			"SUBTRACT_ASSIGN", "NOT_EQUAL_EXEQ", "STEREOTYPE_ID", "DOUBLE_QUOTED_STRING", 
			"REAL_LITERAL", "INTEGER_LITERAL", "STRING_LITERAL", "IDENTIFIER", "WS", 
			"BLOCK_COMMENT"
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
			setState(258);
			compilationUnit();
			setState(259);
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
			setState(264);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 356216060534858L) != 0)) {
				{
				{
				setState(261);
				unitElement();
				}
				}
				setState(266);
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
			setState(272);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(267);
				qvtoImportDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(268);
				modeltypeDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(269);
				transformationDef();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(270);
				libraryDef();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(271);
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
			setState(284);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(274);
				match(T__0);
				setState(275);
				qualifiedName();
				setState(276);
				match(T__1);
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 2);
				{
				setState(278);
				match(T__2);
				setState(279);
				qualifiedName();
				setState(280);
				match(T__0);
				setState(281);
				importedNameList();
				setState(282);
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
			setState(295);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
				enterOuterAlt(_localctx, 1);
				{
				setState(286);
				match(T__3);
				}
				break;
			case T__2:
			case T__5:
			case T__6:
			case T__9:
			case T__12:
			case T__13:
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
			case T__31:
			case T__32:
			case T__34:
			case T__35:
			case T__36:
			case T__37:
			case T__38:
			case T__39:
			case T__40:
			case T__45:
			case T__47:
			case T__48:
			case T__80:
			case T__81:
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
			case T__122:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(287);
				qvtoIdentifier();
				setState(292);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(288);
					match(T__4);
					setState(289);
					qvtoIdentifier();
					}
					}
					setState(294);
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
			setState(297);
			match(T__5);
			setState(298);
			qvtoIdentifier();
			setState(300);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING_LITERAL) {
				{
				setState(299);
				match(STRING_LITERAL);
				}
			}

			setState(302);
			match(T__6);
			setState(303);
			packageRefList();
			setState(305);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(304);
				modeltypeWhere();
				}
			}

			setState(307);
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
			setState(309);
			packageRef();
			setState(314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(310);
				match(T__4);
				setState(311);
				packageRef();
				}
				}
				setState(316);
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
			setState(324);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case T__5:
			case T__6:
			case T__9:
			case T__12:
			case T__13:
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
			case T__31:
			case T__32:
			case T__34:
			case T__35:
			case T__36:
			case T__37:
			case T__38:
			case T__39:
			case T__40:
			case T__45:
			case T__47:
			case T__48:
			case T__80:
			case T__81:
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
			case T__122:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(317);
				pathName();
				setState(321);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(318);
					match(T__7);
					setState(319);
					match(STRING_LITERAL);
					setState(320);
					match(T__8);
					}
				}

				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(323);
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
			setState(326);
			match(T__9);
			setState(327);
			match(T__10);
			setState(328);
			expression(0);
			setState(333);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(329);
				match(T__4);
				setState(330);
				expression(0);
				}
				}
				setState(335);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(336);
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
			setState(341);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 29360128L) != 0)) {
				{
				{
				setState(338);
				qualifier();
				}
				}
				setState(343);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(344);
			match(T__12);
			setState(345);
			qualifiedName();
			setState(346);
			match(T__7);
			setState(348);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 458752L) != 0)) {
				{
				setState(347);
				modelParamList();
				}
			}

			setState(350);
			match(T__8);
			setState(354);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19 || _la==T__20) {
				{
				{
				setState(351);
				moduleUsage();
				}
				}
				setState(356);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(369);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__10:
				{
				setState(357);
				match(T__10);
				setState(361);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 356216060510208L) != 0)) {
					{
					{
					setState(358);
					moduleElement();
					}
					}
					setState(363);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(364);
				match(T__11);
				setState(366);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(365);
					match(T__1);
					}
				}

				}
				break;
			case T__1:
				{
				setState(368);
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
		enterRule(_localctx, 20, RULE_libraryDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(371);
			match(T__13);
			setState(372);
			qualifiedName();
			setState(374);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(373);
				simpleSignature();
				}
			}

			setState(379);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19 || _la==T__20) {
				{
				{
				setState(376);
				moduleUsage();
				}
				}
				setState(381);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(382);
			match(T__10);
			setState(386);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 356216060510208L) != 0)) {
				{
				{
				setState(383);
				moduleElement();
				}
				}
				setState(388);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(389);
			match(T__11);
			setState(391);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(390);
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
		enterRule(_localctx, 22, RULE_modelParamList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
			modelParam();
			setState(398);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(394);
				match(T__4);
				setState(395);
				modelParam();
				}
				}
				setState(400);
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
		enterRule(_localctx, 24, RULE_modelParam);
		try {
			setState(409);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(401);
				directionKind();
				setState(402);
				qvtoIdentifier();
				setState(403);
				match(T__14);
				setState(404);
				typeSpec();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(406);
				directionKind();
				setState(407);
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
		enterRule(_localctx, 26, RULE_directionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 458752L) != 0)) ) {
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
		enterRule(_localctx, 28, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(413);
			typeExpression();
			setState(416);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__18) {
				{
				setState(414);
				match(T__18);
				setState(415);
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
		enterRule(_localctx, 30, RULE_moduleUsage);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418);
			moduleUsageKind();
			setState(420);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(419);
				_la = _input.LA(1);
				if ( !(_la==T__12 || _la==T__13) ) {
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
			setState(422);
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
		enterRule(_localctx, 32, RULE_moduleUsageKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(424);
			_la = _input.LA(1);
			if ( !(_la==T__19 || _la==T__20) ) {
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
		enterRule(_localctx, 34, RULE_moduleRefList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(426);
			qualifiedName();
			setState(431);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(427);
				match(T__4);
				setState(428);
				qualifiedName();
				}
				}
				setState(433);
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
		enterRule(_localctx, 36, RULE_qualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(434);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 29360128L) != 0)) ) {
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
		enterRule(_localctx, 38, RULE_moduleElement);
		try {
			setState(449);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(436);
				mappingDef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(437);
				helperDef();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(438);
				queryDef();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(439);
				constructorDef();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(440);
				entryDef();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(441);
				propertyDecl();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(442);
				intermediateClassDef();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(443);
				tagDecl();
				setState(444);
				match(T__1);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(446);
				typedefDecl();
				setState(447);
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
		enterRule(_localctx, 40, RULE_mappingDef);
		int _la;
		try {
			setState(499);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(454);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 29360128L) != 0)) {
					{
					{
					setState(451);
					qualifier();
					}
					}
					setState(456);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(457);
				match(T__24);
				setState(459);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 458752L) != 0)) {
					{
					setState(458);
					directionKind();
					}
				}

				setState(461);
				scopedName();
				setState(462);
				mappingSignature();
				setState(466);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 469762048L) != 0)) {
					{
					{
					setState(463);
					mappingExtension();
					}
					}
					setState(468);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(470);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__28) {
					{
					setState(469);
					whenClause();
					}
				}

				setState(473);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__9) {
					{
					setState(472);
					whereClause();
					}
				}

				setState(475);
				mappingBody();
				setState(477);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(476);
					match(T__1);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(482);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 29360128L) != 0)) {
					{
					{
					setState(479);
					qualifier();
					}
					}
					setState(484);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(485);
				match(T__24);
				setState(487);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 458752L) != 0)) {
					{
					setState(486);
					directionKind();
					}
				}

				setState(489);
				scopedName();
				setState(490);
				mappingSignature();
				setState(494);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 469762048L) != 0)) {
					{
					{
					setState(491);
					mappingExtension();
					}
					}
					setState(496);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(497);
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
		enterRule(_localctx, 42, RULE_mappingSignature);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(501);
			match(T__7);
			setState(503);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 919174540387528L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 8796093014019L) != 0) || _la==IDENTIFIER) {
				{
				setState(502);
				((MappingSignatureContext)_localctx).inputParams = paramList();
				}
			}

			setState(505);
			match(T__8);
			setState(508);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__14) {
				{
				setState(506);
				match(T__14);
				setState(507);
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
		enterRule(_localctx, 44, RULE_resultList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(510);
			resultParam();
			setState(515);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(511);
				match(T__4);
				setState(512);
				resultParam();
				}
				}
				setState(517);
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
		enterRule(_localctx, 46, RULE_resultParam);
		try {
			setState(523);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				_localctx = new NamedResultContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(518);
				qvtoIdentifier();
				setState(519);
				match(T__14);
				setState(520);
				typeExpression();
				}
				break;
			case 2:
				_localctx = new UnnamedResultContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(522);
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
		enterRule(_localctx, 48, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(525);
			param();
			setState(530);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(526);
				match(T__4);
				setState(527);
				param();
				}
				}
				setState(532);
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
		enterRule(_localctx, 50, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(534);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 458752L) != 0)) {
				{
				setState(533);
				directionKind();
				}
			}

			setState(536);
			qvtoIdentifier();
			setState(537);
			match(T__14);
			setState(538);
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
		enterRule(_localctx, 52, RULE_mappingExtension);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(540);
			mappingExtensionKind();
			setState(541);
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
		enterRule(_localctx, 54, RULE_mappingExtensionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(543);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 469762048L) != 0)) ) {
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
		enterRule(_localctx, 56, RULE_whenClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(545);
			match(T__28);
			setState(546);
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
		enterRule(_localctx, 58, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(548);
			match(T__9);
			setState(549);
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
		enterRule(_localctx, 60, RULE_guardBlock);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(551);
			match(T__10);
			setState(552);
			expression(0);
			setState(557);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,43,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(553);
					match(T__1);
					setState(554);
					expression(0);
					}
					} 
				}
				setState(559);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,43,_ctx);
			}
			setState(561);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(560);
				match(T__1);
				}
			}

			setState(563);
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
		enterRule(_localctx, 62, RULE_mappingBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(565);
			match(T__10);
			setState(567);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				{
				setState(566);
				initSection();
				}
				break;
			}
			setState(571);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(569);
				populationSection();
				}
				break;
			case 2:
				{
				setState(570);
				statementList();
				}
				break;
			}
			setState(574);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(573);
				endSection();
				}
			}

			setState(576);
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
		enterRule(_localctx, 64, RULE_initSection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(578);
			match(T__29);
			setState(579);
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
		enterRule(_localctx, 66, RULE_populationSection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(581);
			match(T__30);
			setState(582);
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
		enterRule(_localctx, 68, RULE_endSection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(584);
			match(T__31);
			setState(585);
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
		enterRule(_localctx, 70, RULE_helperDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(590);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 29360128L) != 0)) {
				{
				{
				setState(587);
				qualifier();
				}
				}
				setState(592);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(593);
			match(T__32);
			setState(594);
			scopedName();
			setState(595);
			simpleSignature();
			setState(598);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__14) {
				{
				setState(596);
				match(T__14);
				setState(597);
				typeExpression();
				}
			}

			setState(606);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__10:
				{
				setState(600);
				block();
				}
				break;
			case T__33:
				{
				setState(601);
				match(T__33);
				setState(602);
				expression(0);
				setState(603);
				match(T__1);
				}
				break;
			case T__1:
				{
				setState(605);
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
		enterRule(_localctx, 72, RULE_queryDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(611);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 29360128L) != 0)) {
				{
				{
				setState(608);
				qualifier();
				}
				}
				setState(613);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(614);
			match(T__34);
			setState(615);
			scopedName();
			setState(616);
			simpleSignature();
			setState(617);
			match(T__14);
			setState(618);
			resultList();
			setState(625);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__10:
				{
				setState(619);
				block();
				}
				break;
			case T__33:
				{
				setState(620);
				match(T__33);
				setState(621);
				expression(0);
				setState(622);
				match(T__1);
				}
				break;
			case T__1:
				{
				setState(624);
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
		enterRule(_localctx, 74, RULE_constructorDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(627);
			match(T__35);
			setState(628);
			scopedName();
			setState(629);
			simpleSignature();
			setState(630);
			block();
			setState(632);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(631);
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
		enterRule(_localctx, 76, RULE_entryDef);
		int _la;
		try {
			setState(648);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__36:
				enterOuterAlt(_localctx, 1);
				{
				setState(634);
				match(T__36);
				setState(635);
				simpleSignature();
				setState(636);
				block();
				setState(638);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(637);
					match(T__1);
					}
				}

				}
				break;
			case T__37:
				enterOuterAlt(_localctx, 2);
				{
				setState(640);
				match(T__37);
				setState(642);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(641);
					simpleSignature();
					}
				}

				setState(644);
				block();
				setState(646);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(645);
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
		enterRule(_localctx, 78, RULE_simpleSignature);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(650);
			match(T__7);
			setState(652);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 919174540387528L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 8796093014019L) != 0) || _la==IDENTIFIER) {
				{
				setState(651);
				paramList();
				}
			}

			setState(654);
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
		enterRule(_localctx, 80, RULE_propertyDecl);
		int _la;
		try {
			setState(694);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(656);
				match(T__38);
				setState(657);
				match(T__39);
				setState(658);
				scopedName();
				setState(659);
				match(T__14);
				setState(660);
				typeExpression();
				setState(663);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__33) {
					{
					setState(661);
					match(T__33);
					setState(662);
					expression(0);
					}
				}

				setState(665);
				match(T__1);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(667);
				match(T__40);
				setState(668);
				match(T__39);
				setState(669);
				qvtoIdentifier();
				setState(670);
				match(T__14);
				setState(671);
				typeExpression();
				setState(674);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__33) {
					{
					setState(672);
					match(T__33);
					setState(673);
					expression(0);
					}
				}

				setState(676);
				match(T__1);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(678);
				match(T__39);
				setState(679);
				qvtoIdentifier();
				setState(680);
				match(T__14);
				setState(681);
				typeExpression();
				setState(684);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__33) {
					{
					setState(682);
					match(T__33);
					setState(683);
					expression(0);
					}
				}

				setState(686);
				match(T__1);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(688);
				match(T__39);
				setState(689);
				qvtoIdentifier();
				setState(690);
				match(T__33);
				setState(691);
				expression(0);
				setState(692);
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
		enterRule(_localctx, 82, RULE_intermediateClassDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(696);
			match(T__38);
			setState(697);
			match(T__41);
			setState(698);
			qvtoIdentifier();
			setState(701);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(699);
				match(T__20);
				setState(700);
				typeList();
				}
			}

			setState(703);
			match(T__10);
			setState(707);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 980747191084232L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 8796093014019L) != 0) || _la==STEREOTYPE_ID || _la==IDENTIFIER) {
				{
				{
				setState(704);
				classifierFeature();
				}
				}
				setState(709);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(710);
			match(T__11);
			setState(712);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(711);
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
		enterRule(_localctx, 84, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(714);
			typeExpression();
			setState(719);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(715);
				match(T__4);
				setState(716);
				typeExpression();
				}
				}
				setState(721);
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
		enterRule(_localctx, 86, RULE_classifierFeature);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(725);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(722);
					classifierFeatureModifier();
					}
					} 
				}
				setState(727);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
			}
			setState(728);
			qvtoIdentifier();
			setState(729);
			match(T__14);
			setState(730);
			typeExpression();
			setState(733);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__33) {
				{
				setState(731);
				match(T__33);
				setState(732);
				expression(0);
				}
			}

			setState(735);
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
		enterRule(_localctx, 88, RULE_classifierFeatureModifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(737);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 61572667932672L) != 0) || _la==STEREOTYPE_ID) ) {
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
		enterRule(_localctx, 90, RULE_tagDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(739);
			match(T__45);
			setState(740);
			_la = _input.LA(1);
			if ( !(_la==DOUBLE_QUOTED_STRING || _la==STRING_LITERAL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(741);
			tagTarget();
			setState(744);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__33) {
				{
				setState(742);
				match(T__33);
				setState(743);
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
		enterRule(_localctx, 92, RULE_tagTarget);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(746);
			qvtoIdentifier();
			setState(751);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__46) {
				{
				{
				setState(747);
				match(T__46);
				setState(748);
				qvtoIdentifier();
				}
				}
				setState(753);
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
		enterRule(_localctx, 94, RULE_typedefDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(754);
			match(T__47);
			setState(755);
			qvtoIdentifier();
			setState(756);
			match(T__33);
			setState(757);
			typeExpression();
			setState(760);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__48) {
				{
				setState(758);
				match(T__48);
				setState(759);
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
		enterRule(_localctx, 96, RULE_statementList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(765);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(762);
					statement();
					}
					} 
				}
				setState(767);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
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
		enterRule(_localctx, 98, RULE_statement);
		try {
			setState(774);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				_localctx = new VarDeclStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(768);
				varDeclExp();
				setState(769);
				match(T__1);
				}
				break;
			case 2:
				_localctx = new ExpressionStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(771);
				expression(0);
				setState(772);
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
		enterRule(_localctx, 100, RULE_assignOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(776);
			_la = _input.LA(1);
			if ( !(((((_la - 147)) & ~0x3f) == 0 && ((1L << (_la - 147)) & 15L) != 0)) ) {
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
		enterRule(_localctx, 102, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(778);
			match(T__10);
			setState(782);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
				{
				{
				setState(779);
				statement();
				}
				}
				setState(784);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(785);
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
		int _startState = 104;
		enterRecursionRule(_localctx, 104, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(793);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__57:
				{
				_localctx = new NotExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(788);
				match(T__57);
				setState(789);
				expression(12);
				}
				break;
			case T__58:
				{
				_localctx = new UnaryMinusExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(790);
				match(T__58);
				setState(791);
				expression(11);
				}
				break;
			case T__2:
			case T__3:
			case T__5:
			case T__6:
			case T__7:
			case T__9:
			case T__10:
			case T__12:
			case T__13:
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
			case T__31:
			case T__32:
			case T__34:
			case T__35:
			case T__36:
			case T__37:
			case T__38:
			case T__39:
			case T__40:
			case T__45:
			case T__47:
			case T__48:
			case T__71:
			case T__72:
			case T__73:
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
			case T__122:
			case T__133:
			case T__134:
			case T__136:
			case T__137:
			case T__138:
			case T__139:
			case T__140:
			case T__141:
			case T__142:
			case T__143:
			case T__144:
			case DOUBLE_QUOTED_STRING:
			case REAL_LITERAL:
			case INTEGER_LITERAL:
			case STRING_LITERAL:
			case IDENTIFIER:
				{
				_localctx = new PrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(792);
				primaryExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(855);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,80,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(853);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
					case 1:
						{
						_localctx = new MultExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(795);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(796);
						((MultExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__3 || _la==T__59) ) {
							((MultExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(797);
						expression(11);
						}
						break;
					case 2:
						{
						_localctx = new AddExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(798);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(799);
						((AddExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__58 || _la==T__60) ) {
							((AddExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(800);
						expression(10);
						}
						break;
					case 3:
						{
						_localctx = new CompareExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(801);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(802);
						((CompareExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 62)) & ~0x3f) == 0 && ((1L << (_la - 62)) & 15L) != 0)) ) {
							((CompareExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(803);
						expression(9);
						}
						break;
					case 4:
						{
						_localctx = new EqualityExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(804);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(805);
						((EqualityExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__33 || _la==T__65 || _la==NOT_EQUAL_EXEQ) ) {
							((EqualityExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(806);
						expression(8);
						}
						break;
					case 5:
						{
						_localctx = new AndExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(807);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(808);
						match(T__66);
						setState(809);
						expression(7);
						}
						break;
					case 6:
						{
						_localctx = new OrExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(810);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(811);
						match(T__67);
						setState(812);
						expression(6);
						}
						break;
					case 7:
						{
						_localctx = new XorExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(813);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(814);
						match(T__68);
						setState(815);
						expression(5);
						}
						break;
					case 8:
						{
						_localctx = new ImpliesExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(816);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(817);
						match(T__69);
						setState(818);
						expression(4);
						}
						break;
					case 9:
						{
						_localctx = new NavigationExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(819);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(820);
						_la = _input.LA(1);
						if ( !(_la==T__49 || _la==T__50) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(821);
						propertyOrCallSuffix();
						}
						break;
					case 10:
						{
						_localctx = new ArrowExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(822);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(823);
						_la = _input.LA(1);
						if ( !(_la==T__51 || _la==T__52) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(824);
						iteratorOrOperationCall();
						}
						break;
					case 11:
						{
						_localctx = new XselectExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(825);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(826);
						match(T__53);
						setState(830);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,76,_ctx) ) {
						case 1:
							{
							setState(827);
							((XselectExpContext)_localctx).xselectIter = qvtoIdentifier();
							setState(828);
							match(T__54);
							}
							break;
						}
						setState(832);
						((XselectExpContext)_localctx).xselectCondition = expression(0);
						setState(833);
						match(T__55);
						}
						break;
					case 12:
						{
						_localctx = new XselectOneExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(835);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(836);
						match(T__56);
						setState(837);
						match(T__53);
						setState(841);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
						case 1:
							{
							setState(838);
							((XselectOneExpContext)_localctx).xselectOneIter = qvtoIdentifier();
							setState(839);
							match(T__54);
							}
							break;
						}
						setState(843);
						((XselectOneExpContext)_localctx).xselectOneCondition = expression(0);
						setState(844);
						match(T__55);
						}
						break;
					case 13:
						{
						_localctx = new AssignExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(846);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(847);
						assignOp();
						setState(848);
						expression(0);
						setState(851);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
						case 1:
							{
							setState(849);
							match(T__70);
							setState(850);
							((AssignExpContext)_localctx).defaultValue = expression(0);
							}
							break;
						}
						}
						break;
					}
					} 
				}
				setState(857);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,80,_ctx);
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
		enterRule(_localctx, 106, RULE_primaryExpression);
		int _la;
		try {
			int _alt;
			setState(950);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				_localctx = new ParenExpContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(858);
				match(T__7);
				setState(859);
				expression(0);
				setState(860);
				match(T__8);
				}
				break;
			case 2:
				_localctx = new SelfExpContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(862);
				match(T__71);
				}
				break;
			case 3:
				_localctx = new ThisExpContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(863);
				match(T__72);
				}
				break;
			case 4:
				_localctx = new IfExpContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(864);
				match(T__73);
				setState(865);
				((IfExpContext)_localctx).condition = expression(0);
				setState(866);
				match(T__74);
				setState(867);
				((IfExpContext)_localctx).thenExp = expression(0);
				setState(875);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__75 || _la==T__76) {
					{
					{
					setState(868);
					_la = _input.LA(1);
					if ( !(_la==T__75 || _la==T__76) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(869);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfCondition.add(((IfExpContext)_localctx).expression);
					setState(870);
					match(T__74);
					setState(871);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfExp.add(((IfExpContext)_localctx).expression);
					}
					}
					setState(877);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(880);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__77) {
					{
					setState(878);
					match(T__77);
					setState(879);
					((IfExpContext)_localctx).elseExp = expression(0);
					}
				}

				setState(882);
				match(T__78);
				}
				break;
			case 5:
				_localctx = new ImperativeIfExpContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(884);
				match(T__73);
				setState(885);
				match(T__7);
				setState(886);
				((ImperativeIfExpContext)_localctx).impCondition = expression(0);
				setState(887);
				match(T__8);
				setState(888);
				((ImperativeIfExpContext)_localctx).thenBlock = block();
				setState(897);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,83,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(889);
						match(T__76);
						setState(890);
						match(T__7);
						setState(891);
						((ImperativeIfExpContext)_localctx).expression = expression(0);
						((ImperativeIfExpContext)_localctx).elifCondition.add(((ImperativeIfExpContext)_localctx).expression);
						setState(892);
						match(T__8);
						setState(893);
						((ImperativeIfExpContext)_localctx).block = block();
						((ImperativeIfExpContext)_localctx).elifBlock.add(((ImperativeIfExpContext)_localctx).block);
						}
						} 
					}
					setState(899);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,83,_ctx);
				}
				setState(902);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
				case 1:
					{
					setState(900);
					match(T__77);
					setState(901);
					((ImperativeIfExpContext)_localctx).elseBlock = block();
					}
					break;
				}
				setState(905);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
				case 1:
					{
					setState(904);
					match(T__78);
					}
					break;
				}
				}
				break;
			case 6:
				_localctx = new LetExpContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(907);
				match(T__79);
				setState(908);
				letBinding();
				setState(913);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(909);
					match(T__4);
					setState(910);
					letBinding();
					}
					}
					setState(915);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(916);
				match(T__15);
				setState(917);
				expression(0);
				}
				break;
			case 7:
				_localctx = new LiteralExpContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(919);
				literalExpression();
				}
				break;
			case 8:
				_localctx = new BlockPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(920);
				blockExp();
				}
				break;
			case 9:
				_localctx = new WhilePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(921);
				whileExp();
				}
				break;
			case 10:
				_localctx = new ForPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(922);
				forExp();
				}
				break;
			case 11:
				_localctx = new SwitchPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(923);
				switchExp();
				}
				break;
			case 12:
				_localctx = new ComputePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(924);
				computeExp();
				}
				break;
			case 13:
				_localctx = new ObjectPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(925);
				objectExp();
				}
				break;
			case 14:
				_localctx = new NewPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(926);
				newExp();
				}
				break;
			case 15:
				_localctx = new MappingCallPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(927);
				mappingCallExp();
				}
				break;
			case 16:
				_localctx = new ResolvePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(928);
				resolveExp();
				}
				break;
			case 17:
				_localctx = new ResolveInPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(929);
				resolveInExp();
				}
				break;
			case 18:
				_localctx = new TryPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(930);
				tryExp();
				}
				break;
			case 19:
				_localctx = new RaisePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(931);
				raiseExp();
				}
				break;
			case 20:
				_localctx = new AssertPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(932);
				assertExp();
				}
				break;
			case 21:
				_localctx = new LogPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 21);
				{
				setState(933);
				logExp();
				}
				break;
			case 22:
				_localctx = new ReturnPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 22);
				{
				setState(934);
				returnExp();
				}
				break;
			case 23:
				_localctx = new BreakPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 23);
				{
				setState(935);
				match(T__80);
				}
				break;
			case 24:
				_localctx = new ContinuePrimaryContext(_localctx);
				enterOuterAlt(_localctx, 24);
				{
				setState(936);
				match(T__81);
				}
				break;
			case 25:
				_localctx = new VarDeclPrimaryContext(_localctx);
				enterOuterAlt(_localctx, 25);
				{
				setState(937);
				varDeclExp();
				}
				break;
			case 26:
				_localctx = new OperationCallExpContext(_localctx);
				enterOuterAlt(_localctx, 26);
				{
				setState(938);
				pathName();
				setState(939);
				match(T__7);
				setState(941);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
					{
					setState(940);
					argumentList();
					}
				}

				setState(943);
				match(T__8);
				}
				break;
			case 27:
				_localctx = new PrimitiveTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 27);
				{
				setState(945);
				primitiveType();
				}
				break;
			case 28:
				_localctx = new CollectionTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 28);
				{
				setState(946);
				collectionType();
				}
				break;
			case 29:
				_localctx = new MapTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 29);
				{
				setState(947);
				mapType();
				}
				break;
			case 30:
				_localctx = new TupleTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 30);
				{
				setState(948);
				tupleType();
				}
				break;
			case 31:
				_localctx = new PathNameExpContext(_localctx);
				enterOuterAlt(_localctx, 31);
				{
				setState(949);
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
		enterRule(_localctx, 108, RULE_typeExpression);
		try {
			setState(959);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(952);
				primitiveType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(953);
				collectionType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(954);
				mapType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(955);
				tupleType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(956);
				listType();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(957);
				dictType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(958);
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
		enterRule(_localctx, 110, RULE_listType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(961);
			match(T__82);
			setState(962);
			match(T__7);
			setState(963);
			typeExpression();
			setState(964);
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
		enterRule(_localctx, 112, RULE_dictType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(966);
			match(T__83);
			setState(967);
			match(T__7);
			setState(968);
			typeExpression();
			setState(969);
			match(T__4);
			setState(970);
			typeExpression();
			setState(971);
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
		enterRule(_localctx, 114, RULE_collectionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(973);
			_la = _input.LA(1);
			if ( !(((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & 125L) != 0)) ) {
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
		enterRule(_localctx, 116, RULE_literalExpression);
		try {
			setState(987);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER_LITERAL:
				_localctx = new IntegerLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(975);
				match(INTEGER_LITERAL);
				}
				break;
			case REAL_LITERAL:
				_localctx = new RealLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(976);
				match(REAL_LITERAL);
				}
				break;
			case DOUBLE_QUOTED_STRING:
			case STRING_LITERAL:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(977);
				stringLiteral_();
				}
				break;
			case T__89:
				_localctx = new TrueLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(978);
				match(T__89);
				}
				break;
			case T__90:
				_localctx = new FalseLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(979);
				match(T__90);
				}
				break;
			case T__91:
				_localctx = new NullLiteralContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(980);
				match(T__91);
				}
				break;
			case T__92:
				_localctx = new InvalidLiteralContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(981);
				match(T__92);
				}
				break;
			case T__3:
				_localctx = new UnlimitedNaturalLiteralContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(982);
				match(T__3);
				}
				break;
			case T__82:
			case T__84:
			case T__85:
			case T__86:
			case T__87:
			case T__88:
				_localctx = new CollectionLitContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(983);
				collectionLiteral();
				}
				break;
			case T__133:
				_localctx = new TupleLitContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(984);
				tupleLiteral();
				}
				break;
			case T__134:
				_localctx = new MapLitContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(985);
				mapLiteral();
				}
				break;
			case T__83:
				_localctx = new DictLitContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(986);
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
		enterRule(_localctx, 118, RULE_stringLiteral_);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(990); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(989);
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
				setState(992); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,91,_ctx);
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
		enterRule(_localctx, 120, RULE_dictLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(994);
			match(T__83);
			setState(995);
			match(T__10);
			setState(1004);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
				{
				setState(996);
				dictLiteralPart();
				setState(1001);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(997);
					match(T__4);
					setState(998);
					dictLiteralPart();
					}
					}
					setState(1003);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1006);
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
		enterRule(_localctx, 122, RULE_dictLiteralPart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1008);
			((DictLiteralPartContext)_localctx).key = expression(0);
			setState(1009);
			match(T__33);
			setState(1010);
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
		enterRule(_localctx, 124, RULE_blockExp);
		int _la;
		try {
			setState(1029);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__93:
				enterOuterAlt(_localctx, 1);
				{
				setState(1012);
				match(T__93);
				setState(1013);
				match(T__10);
				setState(1017);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
					{
					{
					setState(1014);
					statement();
					}
					}
					setState(1019);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1020);
				match(T__11);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 2);
				{
				setState(1021);
				match(T__10);
				setState(1025);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
					{
					{
					setState(1022);
					statement();
					}
					}
					setState(1027);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1028);
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
		enterRule(_localctx, 126, RULE_whileExp);
		try {
			setState(1049);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				_localctx = new WhileWithInitContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1031);
				match(T__94);
				setState(1032);
				match(T__7);
				setState(1033);
				((WhileWithInitContext)_localctx).varName = qvtoIdentifier();
				setState(1034);
				match(T__14);
				setState(1035);
				((WhileWithInitContext)_localctx).type = typeExpression();
				setState(1036);
				match(RESET_ASSIGN);
				setState(1037);
				((WhileWithInitContext)_localctx).initValue = expression(0);
				setState(1038);
				match(T__1);
				setState(1039);
				((WhileWithInitContext)_localctx).condition = expression(0);
				setState(1040);
				match(T__8);
				setState(1041);
				block();
				}
				break;
			case 2:
				_localctx = new WhileBasicContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1043);
				match(T__94);
				setState(1044);
				match(T__7);
				setState(1045);
				expression(0);
				setState(1046);
				match(T__8);
				setState(1047);
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
		enterRule(_localctx, 128, RULE_forExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1051);
			forKind();
			setState(1052);
			match(T__7);
			setState(1053);
			forVarList();
			setState(1056);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__54) {
				{
				setState(1054);
				match(T__54);
				setState(1055);
				expression(0);
				}
			}

			setState(1058);
			match(T__8);
			setState(1059);
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
		enterRule(_localctx, 130, RULE_forKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1061);
			_la = _input.LA(1);
			if ( !(_la==T__95 || _la==T__96) ) {
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
		enterRule(_localctx, 132, RULE_forVarList);
		int _la;
		try {
			setState(1078);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1063);
				iteratorVariables();
				setState(1064);
				match(T__1);
				setState(1065);
				qvtoIdentifier();
				setState(1066);
				match(T__14);
				setState(1067);
				typeExpression();
				setState(1071);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__33 || _la==RESET_ASSIGN || _la==ORDERED_COPY) {
					{
					setState(1068);
					varInitOp();
					setState(1069);
					expression(0);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1073);
				iteratorVariables();
				setState(1074);
				match(T__1);
				setState(1075);
				typeExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1077);
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
		enterRule(_localctx, 134, RULE_switchExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1080);
			match(T__97);
			setState(1085);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(1081);
				match(T__7);
				setState(1082);
				expression(0);
				setState(1083);
				match(T__8);
				}
			}

			setState(1087);
			match(T__10);
			setState(1091);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__98) {
				{
				{
				setState(1088);
				switchAlt();
				}
				}
				setState(1093);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1098);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__77) {
				{
				setState(1094);
				match(T__77);
				setState(1095);
				expression(0);
				setState(1096);
				match(T__1);
				}
			}

			setState(1100);
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
		enterRule(_localctx, 136, RULE_switchAlt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1102);
			match(T__98);
			setState(1103);
			match(T__7);
			setState(1104);
			expression(0);
			setState(1105);
			match(T__8);
			setState(1106);
			expression(0);
			setState(1107);
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
		enterRule(_localctx, 138, RULE_computeExp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1109);
			match(T__99);
			setState(1110);
			match(T__7);
			setState(1111);
			varDeclarator();
			setState(1112);
			match(T__8);
			setState(1113);
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
		enterRule(_localctx, 140, RULE_objectExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1115);
			match(T__100);
			setState(1119);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
			case 1:
				{
				setState(1116);
				((ObjectExpContext)_localctx).varName = qvtoIdentifier();
				setState(1117);
				match(T__14);
				}
				break;
			}
			setState(1122);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 919174539928776L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & -45027200180690433L) != 0) || _la==T__144 || _la==IDENTIFIER) {
				{
				setState(1121);
				typeExpression();
				}
			}

			setState(1126);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__18) {
				{
				setState(1124);
				match(T__18);
				setState(1125);
				((ObjectExpContext)_localctx).extentName = qvtoIdentifier();
				}
			}

			setState(1128);
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
		enterRule(_localctx, 142, RULE_newExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1130);
			match(T__101);
			setState(1131);
			pathName();
			setState(1134);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__18) {
				{
				setState(1132);
				match(T__18);
				setState(1133);
				((NewExpContext)_localctx).extentName = qvtoIdentifier();
				}
			}

			setState(1136);
			match(T__7);
			setState(1138);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
				{
				setState(1137);
				argumentList();
				}
			}

			setState(1140);
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
		enterRule(_localctx, 144, RULE_mappingCallExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1142);
			mappingCallKind();
			setState(1143);
			scopedName();
			setState(1144);
			match(T__7);
			setState(1146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
				{
				setState(1145);
				argumentList();
				}
			}

			setState(1148);
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
		enterRule(_localctx, 146, RULE_mappingCallKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1150);
			_la = _input.LA(1);
			if ( !(_la==T__102 || _la==T__103) ) {
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
		enterRule(_localctx, 148, RULE_resolveExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1153);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__104) {
				{
				setState(1152);
				match(T__104);
				}
			}

			setState(1155);
			resolveKind();
			setState(1156);
			match(T__7);
			setState(1158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 919174539928776L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & -45027200180690433L) != 0) || _la==T__144 || _la==IDENTIFIER) {
				{
				setState(1157);
				resolveArgs();
				}
			}

			setState(1160);
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
		enterRule(_localctx, 150, RULE_resolveKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1162);
			_la = _input.LA(1);
			if ( !(((((_la - 106)) & ~0x3f) == 0 && ((1L << (_la - 106)) & 15L) != 0)) ) {
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
		enterRule(_localctx, 152, RULE_resolveInExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1165);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__104) {
				{
				setState(1164);
				match(T__104);
				}
			}

			setState(1167);
			resolveInKind();
			setState(1168);
			match(T__7);
			setState(1169);
			scopedName();
			setState(1172);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(1170);
				match(T__4);
				setState(1171);
				resolveArgs();
				}
			}

			setState(1174);
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
		enterRule(_localctx, 154, RULE_resolveInKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1176);
			_la = _input.LA(1);
			if ( !(((((_la - 110)) & ~0x3f) == 0 && ((1L << (_la - 110)) & 15L) != 0)) ) {
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
		enterRule(_localctx, 156, RULE_resolveArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1178);
			resolveTarget();
			setState(1181);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__54) {
				{
				setState(1179);
				match(T__54);
				setState(1180);
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
		enterRule(_localctx, 158, RULE_resolveTarget);
		try {
			setState(1188);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,115,_ctx) ) {
			case 1:
				_localctx = new NamedResolveTargetContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1183);
				qvtoIdentifier();
				setState(1184);
				match(T__14);
				setState(1185);
				typeExpression();
				}
				break;
			case 2:
				_localctx = new TypeOnlyResolveTargetContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1187);
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
		enterRule(_localctx, 160, RULE_tryExp);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1190);
			match(T__113);
			setState(1191);
			block();
			setState(1193); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1192);
					catchClause();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1195); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,116,_ctx);
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
		enterRule(_localctx, 162, RULE_catchClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1197);
			_la = _input.LA(1);
			if ( !(_la==T__114 || _la==T__115) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1208);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(1198);
				match(T__7);
				setState(1205);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 919174539928776L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 8796093014019L) != 0) || _la==IDENTIFIER) {
					{
					setState(1202);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
					case 1:
						{
						setState(1199);
						((CatchClauseContext)_localctx).exceptionVar = qvtoIdentifier();
						setState(1200);
						match(T__14);
						}
						break;
					}
					setState(1204);
					exceptionTypeList();
					}
				}

				setState(1207);
				match(T__8);
				}
			}

			setState(1210);
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
		enterRule(_localctx, 164, RULE_exceptionTypeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1212);
			pathName();
			setState(1217);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1213);
				match(T__4);
				setState(1214);
				pathName();
				}
				}
				setState(1219);
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
		enterRule(_localctx, 166, RULE_raiseExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1220);
			match(T__116);
			setState(1230);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case T__5:
			case T__6:
			case T__9:
			case T__12:
			case T__13:
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
			case T__31:
			case T__32:
			case T__34:
			case T__35:
			case T__36:
			case T__37:
			case T__38:
			case T__39:
			case T__40:
			case T__45:
			case T__47:
			case T__48:
			case T__80:
			case T__81:
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
			case T__122:
			case IDENTIFIER:
				{
				setState(1221);
				pathName();
				setState(1227);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
				case 1:
					{
					setState(1222);
					match(T__7);
					setState(1224);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
						{
						setState(1223);
						expression(0);
						}
					}

					setState(1226);
					match(T__8);
					}
					break;
				}
				}
				break;
			case STRING_LITERAL:
				{
				setState(1229);
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
		enterRule(_localctx, 168, RULE_assertExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1232);
			match(T__117);
			setState(1234);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 919174539928776L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 8796093014019L) != 0) || _la==IDENTIFIER) {
				{
				setState(1233);
				qvtoIdentifier();
				}
			}

			setState(1236);
			match(T__7);
			setState(1237);
			expression(0);
			setState(1238);
			match(T__8);
			setState(1241);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,125,_ctx) ) {
			case 1:
				{
				setState(1239);
				match(T__48);
				setState(1240);
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
		enterRule(_localctx, 170, RULE_logCallExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1243);
			match(T__118);
			setState(1244);
			match(T__7);
			setState(1246);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
				{
				setState(1245);
				argumentList();
				}
			}

			setState(1248);
			match(T__8);
			setState(1251);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,127,_ctx) ) {
			case 1:
				{
				setState(1249);
				match(T__28);
				setState(1250);
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
		enterRule(_localctx, 172, RULE_logExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1253);
			match(T__118);
			setState(1254);
			match(T__7);
			setState(1256);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
				{
				setState(1255);
				argumentList();
				}
			}

			setState(1258);
			match(T__8);
			setState(1261);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,129,_ctx) ) {
			case 1:
				{
				setState(1259);
				match(T__28);
				setState(1260);
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
		enterRule(_localctx, 174, RULE_returnExp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1263);
			match(T__119);
			setState(1265);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				{
				setState(1264);
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
		enterRule(_localctx, 176, RULE_varDeclExp);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1267);
			match(T__120);
			setState(1268);
			varDeclarator();
			setState(1273);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,131,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1269);
					match(T__4);
					setState(1270);
					varDeclarator();
					}
					} 
				}
				setState(1275);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,131,_ctx);
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
		enterRule(_localctx, 178, RULE_varDeclarator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1276);
			qvtoIdentifier();
			setState(1279);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,132,_ctx) ) {
			case 1:
				{
				setState(1277);
				match(T__14);
				setState(1278);
				typeExpression();
				}
				break;
			}
			setState(1284);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,133,_ctx) ) {
			case 1:
				{
				setState(1281);
				varInitOp();
				setState(1282);
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
		enterRule(_localctx, 180, RULE_varInitOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1286);
			_la = _input.LA(1);
			if ( !(_la==T__33 || _la==RESET_ASSIGN || _la==ORDERED_COPY) ) {
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
		enterRule(_localctx, 182, RULE_pathName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1288);
			qvtoIdentifier();
			setState(1293);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,134,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1289);
					match(T__46);
					setState(1290);
					qvtoIdentifier();
					}
					} 
				}
				setState(1295);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,134,_ctx);
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
		enterRule(_localctx, 184, RULE_propertyOrCallSuffix);
		int _la;
		try {
			setState(1339);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,143,_ctx) ) {
			case 1:
				_localctx = new MappingCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1296);
				mappingCallKind();
				setState(1297);
				scopedName();
				setState(1298);
				match(T__7);
				setState(1300);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
					{
					setState(1299);
					argumentList();
					}
				}

				setState(1302);
				match(T__8);
				}
				break;
			case 2:
				_localctx = new ResolveCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1305);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__104) {
					{
					setState(1304);
					match(T__104);
					}
				}

				setState(1307);
				resolveKind();
				setState(1308);
				match(T__7);
				setState(1310);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 919174539928776L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & -45027200180690433L) != 0) || _la==T__144 || _la==IDENTIFIER) {
					{
					setState(1309);
					resolveArgs();
					}
				}

				setState(1312);
				match(T__8);
				}
				break;
			case 3:
				_localctx = new ResolveInCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1315);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__104) {
					{
					setState(1314);
					match(T__104);
					}
				}

				setState(1317);
				resolveInKind();
				setState(1318);
				match(T__7);
				setState(1319);
				scopedName();
				setState(1322);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(1320);
					match(T__4);
					setState(1321);
					resolveArgs();
					}
				}

				setState(1324);
				match(T__8);
				}
				break;
			case 4:
				_localctx = new DotCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(1326);
				qvtoIdentifier();
				setState(1327);
				match(T__7);
				setState(1329);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
					{
					setState(1328);
					argumentList();
					}
				}

				setState(1331);
				match(T__8);
				setState(1333);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,141,_ctx) ) {
				case 1:
					{
					setState(1332);
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
				setState(1335);
				qvtoIdentifier();
				setState(1337);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,142,_ctx) ) {
				case 1:
					{
					setState(1336);
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
		enterRule(_localctx, 186, RULE_iteratorOrOperationCall);
		int _la;
		try {
			setState(1415);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,153,_ctx) ) {
			case 1:
				_localctx = new ForEachCallContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1341);
				forKind();
				setState(1342);
				match(T__7);
				setState(1343);
				forVarList();
				setState(1346);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__54) {
					{
					setState(1344);
					match(T__54);
					setState(1345);
					expression(0);
					}
				}

				setState(1348);
				match(T__8);
				setState(1349);
				block();
				}
				break;
			case 2:
				_localctx = new ArrowResolveCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1352);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__104) {
					{
					setState(1351);
					match(T__104);
					}
				}

				setState(1354);
				resolveKind();
				setState(1355);
				match(T__7);
				setState(1357);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 919174539928776L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & -45027200180690433L) != 0) || _la==T__144 || _la==IDENTIFIER) {
					{
					setState(1356);
					resolveArgs();
					}
				}

				setState(1359);
				match(T__8);
				}
				break;
			case 3:
				_localctx = new ArrowResolveInCallContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1362);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__104) {
					{
					setState(1361);
					match(T__104);
					}
				}

				setState(1364);
				resolveInKind();
				setState(1365);
				match(T__7);
				setState(1366);
				scopedName();
				setState(1369);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(1367);
					match(T__4);
					setState(1368);
					resolveArgs();
					}
				}

				setState(1371);
				match(T__8);
				}
				break;
			case 4:
				_localctx = new ArrowMappingCallContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(1373);
				mappingCallKind();
				setState(1374);
				scopedName();
				setState(1375);
				match(T__7);
				setState(1377);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
					{
					setState(1376);
					argumentList();
					}
				}

				setState(1379);
				match(T__8);
				}
				break;
			case 5:
				_localctx = new IteratorCallContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(1381);
				qvtoIdentifier();
				setState(1382);
				match(T__7);
				setState(1383);
				iteratorVariables();
				setState(1384);
				match(T__54);
				setState(1385);
				expression(0);
				setState(1386);
				match(T__8);
				}
				break;
			case 6:
				_localctx = new IterateCallContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(1388);
				qvtoIdentifier();
				setState(1389);
				match(T__7);
				setState(1390);
				qvtoIdentifier();
				setState(1393);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__14) {
					{
					setState(1391);
					match(T__14);
					setState(1392);
					((IterateCallContext)_localctx).iterType = typeExpression();
					}
				}

				setState(1395);
				match(T__1);
				setState(1396);
				qvtoIdentifier();
				setState(1399);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__14) {
					{
					setState(1397);
					match(T__14);
					setState(1398);
					((IterateCallContext)_localctx).accType = typeExpression();
					}
				}

				setState(1401);
				match(T__33);
				setState(1402);
				expression(0);
				setState(1403);
				match(T__54);
				setState(1404);
				expression(0);
				setState(1405);
				match(T__8);
				}
				break;
			case 7:
				_localctx = new CollectionOperationCallContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(1407);
				qvtoIdentifier();
				setState(1408);
				match(T__7);
				setState(1410);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
					{
					setState(1409);
					argumentList();
					}
				}

				setState(1412);
				match(T__8);
				}
				break;
			case 8:
				_localctx = new ArrowPropertyCallContext(_localctx);
				enterOuterAlt(_localctx, 8);
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
		enterRule(_localctx, 188, RULE_iteratorVariables);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1417);
			qvtoIdentifier();
			setState(1420);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__14) {
				{
				setState(1418);
				match(T__14);
				setState(1419);
				typeExpression();
				}
			}

			setState(1430);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1422);
				match(T__4);
				setState(1423);
				qvtoIdentifier();
				setState(1426);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__14) {
					{
					setState(1424);
					match(T__14);
					setState(1425);
					typeExpression();
					}
				}

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
		enterRule(_localctx, 190, RULE_letBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1433);
			qvtoIdentifier();
			setState(1436);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__14) {
				{
				setState(1434);
				match(T__14);
				setState(1435);
				typeExpression();
				}
			}

			setState(1438);
			match(T__33);
			setState(1439);
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
		enterRule(_localctx, 192, RULE_tupleTypePart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1441);
			qvtoIdentifier();
			setState(1442);
			match(T__14);
			setState(1443);
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
		enterRule(_localctx, 194, RULE_tupleLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1445);
			qvtoIdentifier();
			setState(1448);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__14) {
				{
				setState(1446);
				match(T__14);
				setState(1447);
				typeExpression();
				}
			}

			setState(1450);
			match(T__33);
			setState(1451);
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
		enterRule(_localctx, 196, RULE_scopedName);
		try {
			setState(1466);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,159,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1453);
				qualifiedName();
				setState(1454);
				match(T__46);
				setState(1455);
				qvtoIdentifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1457);
				primitiveType();
				setState(1458);
				match(T__46);
				setState(1459);
				qvtoIdentifier();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1461);
				collectionType();
				setState(1462);
				match(T__46);
				setState(1463);
				qvtoIdentifier();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1465);
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
		enterRule(_localctx, 198, RULE_scopedNameList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1468);
			scopedName();
			setState(1473);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1469);
				match(T__4);
				setState(1470);
				scopedName();
				}
				}
				setState(1475);
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
		enterRule(_localctx, 200, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1476);
			qvtoIdentifier();
			setState(1481);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__49) {
				{
				{
				setState(1477);
				match(T__49);
				setState(1478);
				qvtoIdentifier();
				}
				}
				setState(1483);
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
		enterRule(_localctx, 202, RULE_qvtoIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1484);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 919174539928776L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 8796093014019L) != 0) || _la==IDENTIFIER) ) {
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
		enterRule(_localctx, 204, RULE_expressionEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1486);
			expression(0);
			setState(1487);
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
		enterRule(_localctx, 206, RULE_completeOclDocumentEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1489);
			completeOclDocument();
			setState(1490);
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
		enterRule(_localctx, 208, RULE_completeOclDocument);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1495);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0 || _la==T__13 || _la==T__123) {
				{
				{
				setState(1492);
				importDeclaration();
				}
				}
				setState(1497);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1502);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__124 || _la==T__126) {
				{
				setState(1500);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__124:
					{
					setState(1498);
					packageDeclaration();
					}
					break;
				case T__126:
					{
					setState(1499);
					contextDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(1504);
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
		enterRule(_localctx, 210, RULE_importDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1505);
			_la = _input.LA(1);
			if ( !(_la==T__0 || _la==T__13 || _la==T__123) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1508);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,165,_ctx) ) {
			case 1:
				{
				setState(1506);
				match(IDENTIFIER);
				setState(1507);
				match(T__14);
				}
				break;
			}
			setState(1510);
			pathName();
			setState(1513);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__46) {
				{
				setState(1511);
				match(T__46);
				setState(1512);
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
		enterRule(_localctx, 212, RULE_packageDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1515);
			match(T__124);
			setState(1516);
			pathName();
			setState(1520);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__126) {
				{
				{
				setState(1517);
				contextDeclaration();
				}
				}
				setState(1522);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1523);
			match(T__125);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 214, RULE_contextDeclaration);
		try {
			setState(1528);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,168,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1525);
				classifierContextDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1526);
				operationContextDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1527);
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
		enterRule(_localctx, 216, RULE_classifierContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1530);
			match(T__126);
			setState(1531);
			pathName();
			setState(1533); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1532);
				classifierContextBody();
				}
				}
				setState(1535); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__23 || _la==T__121 || _la==T__127 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 218, RULE_classifierContextBody);
		try {
			setState(1539);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__121:
				enterOuterAlt(_localctx, 1);
				{
				setState(1537);
				invariantConstraint();
				}
				break;
			case T__23:
			case T__127:
				enterOuterAlt(_localctx, 2);
				{
				setState(1538);
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
		enterRule(_localctx, 220, RULE_invariantConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1541);
			match(T__121);
			setState(1549);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(1542);
				match(IDENTIFIER);
				setState(1547);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(1543);
					match(T__7);
					setState(1544);
					expression(0);
					setState(1545);
					match(T__8);
					}
				}

				}
			}

			setState(1551);
			match(T__14);
			setState(1552);
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
		enterRule(_localctx, 222, RULE_definitionConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1555);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23) {
				{
				setState(1554);
				match(T__23);
				}
			}

			setState(1557);
			match(T__127);
			setState(1559);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(1558);
				match(IDENTIFIER);
				}
			}

			setState(1561);
			match(T__14);
			setState(1579);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,176,_ctx) ) {
			case 1:
				{
				setState(1562);
				match(IDENTIFIER);
				setState(1563);
				match(T__14);
				setState(1564);
				typeExpression();
				setState(1565);
				match(T__33);
				setState(1566);
				expression(0);
				}
				break;
			case 2:
				{
				setState(1568);
				match(IDENTIFIER);
				setState(1569);
				match(T__7);
				setState(1571);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1570);
					parameterList();
					}
				}

				setState(1573);
				match(T__8);
				setState(1574);
				match(T__14);
				setState(1575);
				typeExpression();
				setState(1576);
				match(T__33);
				setState(1577);
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
		enterRule(_localctx, 224, RULE_operationContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1581);
			match(T__126);
			setState(1582);
			pathName();
			setState(1583);
			match(T__46);
			setState(1584);
			match(IDENTIFIER);
			setState(1585);
			match(T__7);
			setState(1587);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(1586);
				parameterList();
				}
			}

			setState(1589);
			match(T__8);
			setState(1590);
			match(T__14);
			setState(1591);
			typeExpression();
			setState(1593); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1592);
				operationContextBody();
				}
				}
				setState(1595); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & 7L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 226, RULE_operationContextBody);
		int _la;
		try {
			setState(1615);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__128:
				_localctx = new PreConditionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1597);
				match(T__128);
				setState(1599);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1598);
					match(IDENTIFIER);
					}
				}

				setState(1601);
				match(T__14);
				setState(1602);
				expression(0);
				}
				break;
			case T__129:
				_localctx = new PostConditionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1603);
				match(T__129);
				setState(1605);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1604);
					match(IDENTIFIER);
					}
				}

				setState(1607);
				match(T__14);
				setState(1608);
				expression(0);
				}
				break;
			case T__130:
				_localctx = new BodyExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1609);
				match(T__130);
				setState(1611);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1610);
					match(IDENTIFIER);
					}
				}

				setState(1613);
				match(T__14);
				setState(1614);
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
		enterRule(_localctx, 228, RULE_propertyContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1617);
			match(T__126);
			setState(1618);
			pathName();
			setState(1619);
			match(T__46);
			setState(1620);
			match(IDENTIFIER);
			setState(1621);
			match(T__14);
			setState(1622);
			typeExpression();
			setState(1624); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1623);
				propertyContextBody();
				}
				}
				setState(1626); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__29 || _la==T__131 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 230, RULE_propertyContextBody);
		int _la;
		try {
			setState(1640);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__29:
				_localctx = new InitExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1628);
				match(T__29);
				setState(1630);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1629);
					match(IDENTIFIER);
					}
				}

				setState(1632);
				match(T__14);
				setState(1633);
				expression(0);
				}
				break;
			case T__131:
				_localctx = new DeriveExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1634);
				match(T__131);
				setState(1636);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(1635);
					match(IDENTIFIER);
					}
				}

				setState(1638);
				match(T__14);
				setState(1639);
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
		enterRule(_localctx, 232, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1642);
			parameter();
			setState(1647);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1643);
				match(T__4);
				setState(1644);
				parameter();
				}
				}
				setState(1649);
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
		enterRule(_localctx, 234, RULE_parameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1650);
			match(IDENTIFIER);
			setState(1651);
			match(T__14);
			setState(1652);
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
		enterRule(_localctx, 236, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1654);
			expression(0);
			setState(1659);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1655);
				match(T__4);
				setState(1656);
				expression(0);
				}
				}
				setState(1661);
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
		enterRule(_localctx, 238, RULE_isMarkedPre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1662);
			match(T__18);
			setState(1663);
			match(T__128);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 240, RULE_collectionLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1665);
			collectionKind();
			setState(1666);
			match(T__10);
			setState(1675);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
				{
				setState(1667);
				collectionLiteralPart();
				setState(1672);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(1668);
					match(T__4);
					setState(1669);
					collectionLiteralPart();
					}
					}
					setState(1674);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1677);
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
		enterRule(_localctx, 242, RULE_collectionLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1679);
			expression(0);
			setState(1682);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__132) {
				{
				setState(1680);
				match(T__132);
				setState(1681);
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
		enterRule(_localctx, 244, RULE_tupleLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1684);
			match(T__133);
			setState(1685);
			match(T__10);
			setState(1686);
			tupleLiteralPart();
			setState(1691);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1687);
				match(T__4);
				setState(1688);
				tupleLiteralPart();
				}
				}
				setState(1693);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1694);
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
		enterRule(_localctx, 246, RULE_mapLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1696);
			match(T__134);
			setState(1697);
			match(T__10);
			setState(1706);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 865610302995066328L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -4607182418800017657L) != 0) || ((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 2032127L) != 0)) {
				{
				setState(1698);
				mapLiteralPart();
				setState(1703);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(1699);
					match(T__4);
					setState(1700);
					mapLiteralPart();
					}
					}
					setState(1705);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1708);
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
		enterRule(_localctx, 248, RULE_mapLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1710);
			expression(0);
			setState(1711);
			_la = _input.LA(1);
			if ( !(_la==T__48 || _la==T__135) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1712);
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
		enterRule(_localctx, 250, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1714);
			_la = _input.LA(1);
			if ( !(((((_la - 137)) & ~0x3f) == 0 && ((1L << (_la - 137)) & 511L) != 0)) ) {
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
		enterRule(_localctx, 252, RULE_collectionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1716);
			collectionKind();
			setState(1717);
			match(T__7);
			setState(1718);
			typeExpression();
			setState(1719);
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
		enterRule(_localctx, 254, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1721);
			match(T__134);
			setState(1722);
			match(T__7);
			setState(1723);
			typeExpression();
			setState(1724);
			match(T__4);
			setState(1725);
			typeExpression();
			setState(1726);
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
		enterRule(_localctx, 256, RULE_tupleType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1728);
			match(T__133);
			setState(1729);
			match(T__7);
			setState(1730);
			tupleTypePart();
			setState(1735);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(1731);
				match(T__4);
				setState(1732);
				tupleTypePart();
				}
				}
				setState(1737);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1738);
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
		case 52:
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
		"\u0004\u0001\u009f\u06cd\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"\u0080\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0005\u0001\u0107"+
		"\b\u0001\n\u0001\f\u0001\u010a\t\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0003\u0002\u0111\b\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0003\u0003\u011d\b\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0005\u0004\u0123\b\u0004\n\u0004\f\u0004\u0126"+
		"\t\u0004\u0003\u0004\u0128\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0003\u0005\u012d\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005"+
		"\u0132\b\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0005\u0006\u0139\b\u0006\n\u0006\f\u0006\u013c\t\u0006\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u0142\b\u0007\u0001\u0007\u0003"+
		"\u0007\u0145\b\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0005\b\u014c"+
		"\b\b\n\b\f\b\u014f\t\b\u0001\b\u0001\b\u0001\t\u0005\t\u0154\b\t\n\t\f"+
		"\t\u0157\t\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u015d\b\t\u0001\t"+
		"\u0001\t\u0005\t\u0161\b\t\n\t\f\t\u0164\t\t\u0001\t\u0001\t\u0005\t\u0168"+
		"\b\t\n\t\f\t\u016b\t\t\u0001\t\u0001\t\u0003\t\u016f\b\t\u0001\t\u0003"+
		"\t\u0172\b\t\u0001\n\u0001\n\u0001\n\u0003\n\u0177\b\n\u0001\n\u0005\n"+
		"\u017a\b\n\n\n\f\n\u017d\t\n\u0001\n\u0001\n\u0005\n\u0181\b\n\n\n\f\n"+
		"\u0184\t\n\u0001\n\u0001\n\u0003\n\u0188\b\n\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0005\u000b\u018d\b\u000b\n\u000b\f\u000b\u0190\t\u000b\u0001\f"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u019a"+
		"\b\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u01a1"+
		"\b\u000e\u0001\u000f\u0001\u000f\u0003\u000f\u01a5\b\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0005\u0011\u01ae\b\u0011\n\u0011\f\u0011\u01b1\t\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0003\u0013\u01c2\b\u0013\u0001\u0014\u0005\u0014\u01c5"+
		"\b\u0014\n\u0014\f\u0014\u01c8\t\u0014\u0001\u0014\u0001\u0014\u0003\u0014"+
		"\u01cc\b\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u01d1\b"+
		"\u0014\n\u0014\f\u0014\u01d4\t\u0014\u0001\u0014\u0003\u0014\u01d7\b\u0014"+
		"\u0001\u0014\u0003\u0014\u01da\b\u0014\u0001\u0014\u0001\u0014\u0003\u0014"+
		"\u01de\b\u0014\u0001\u0014\u0005\u0014\u01e1\b\u0014\n\u0014\f\u0014\u01e4"+
		"\t\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u01e8\b\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0005\u0014\u01ed\b\u0014\n\u0014\f\u0014\u01f0"+
		"\t\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u01f4\b\u0014\u0001\u0015"+
		"\u0001\u0015\u0003\u0015\u01f8\b\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0003\u0015\u01fd\b\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0005\u0016"+
		"\u0202\b\u0016\n\u0016\f\u0016\u0205\t\u0016\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u020c\b\u0017\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0005\u0018\u0211\b\u0018\n\u0018\f\u0018\u0214\t\u0018"+
		"\u0001\u0019\u0003\u0019\u0217\b\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b"+
		"\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0005\u001e\u022c\b\u001e"+
		"\n\u001e\f\u001e\u022f\t\u001e\u0001\u001e\u0003\u001e\u0232\b\u001e\u0001"+
		"\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0003\u001f\u0238\b\u001f\u0001"+
		"\u001f\u0001\u001f\u0003\u001f\u023c\b\u001f\u0001\u001f\u0003\u001f\u023f"+
		"\b\u001f\u0001\u001f\u0001\u001f\u0001 \u0001 \u0001 \u0001!\u0001!\u0001"+
		"!\u0001\"\u0001\"\u0001\"\u0001#\u0005#\u024d\b#\n#\f#\u0250\t#\u0001"+
		"#\u0001#\u0001#\u0001#\u0001#\u0003#\u0257\b#\u0001#\u0001#\u0001#\u0001"+
		"#\u0001#\u0001#\u0003#\u025f\b#\u0001$\u0005$\u0262\b$\n$\f$\u0265\t$"+
		"\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001"+
		"$\u0001$\u0003$\u0272\b$\u0001%\u0001%\u0001%\u0001%\u0001%\u0003%\u0279"+
		"\b%\u0001&\u0001&\u0001&\u0001&\u0003&\u027f\b&\u0001&\u0001&\u0003&\u0283"+
		"\b&\u0001&\u0001&\u0003&\u0287\b&\u0003&\u0289\b&\u0001\'\u0001\'\u0003"+
		"\'\u028d\b\'\u0001\'\u0001\'\u0001(\u0001(\u0001(\u0001(\u0001(\u0001"+
		"(\u0001(\u0003(\u0298\b(\u0001(\u0001(\u0001(\u0001(\u0001(\u0001(\u0001"+
		"(\u0001(\u0001(\u0003(\u02a3\b(\u0001(\u0001(\u0001(\u0001(\u0001(\u0001"+
		"(\u0001(\u0001(\u0003(\u02ad\b(\u0001(\u0001(\u0001(\u0001(\u0001(\u0001"+
		"(\u0001(\u0001(\u0003(\u02b7\b(\u0001)\u0001)\u0001)\u0001)\u0001)\u0003"+
		")\u02be\b)\u0001)\u0001)\u0005)\u02c2\b)\n)\f)\u02c5\t)\u0001)\u0001)"+
		"\u0003)\u02c9\b)\u0001*\u0001*\u0001*\u0005*\u02ce\b*\n*\f*\u02d1\t*\u0001"+
		"+\u0005+\u02d4\b+\n+\f+\u02d7\t+\u0001+\u0001+\u0001+\u0001+\u0001+\u0003"+
		"+\u02de\b+\u0001+\u0001+\u0001,\u0001,\u0001-\u0001-\u0001-\u0001-\u0001"+
		"-\u0003-\u02e9\b-\u0001.\u0001.\u0001.\u0005.\u02ee\b.\n.\f.\u02f1\t."+
		"\u0001/\u0001/\u0001/\u0001/\u0001/\u0001/\u0003/\u02f9\b/\u00010\u0005"+
		"0\u02fc\b0\n0\f0\u02ff\t0\u00011\u00011\u00011\u00011\u00011\u00011\u0003"+
		"1\u0307\b1\u00012\u00012\u00013\u00013\u00053\u030d\b3\n3\f3\u0310\t3"+
		"\u00013\u00013\u00014\u00014\u00014\u00014\u00014\u00014\u00034\u031a"+
		"\b4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00034\u033f\b4\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00034\u034a\b4\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00014\u00034\u0354\b4\u00054\u0356"+
		"\b4\n4\f4\u0359\t4\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u0001"+
		"5\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u00055\u036a\b5\n5"+
		"\f5\u036d\t5\u00015\u00015\u00035\u0371\b5\u00015\u00015\u00015\u0001"+
		"5\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u0005"+
		"5\u0380\b5\n5\f5\u0383\t5\u00015\u00015\u00035\u0387\b5\u00015\u00035"+
		"\u038a\b5\u00015\u00015\u00015\u00015\u00055\u0390\b5\n5\f5\u0393\t5\u0001"+
		"5\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u0001"+
		"5\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u00015\u0001"+
		"5\u00015\u00015\u00015\u00015\u00035\u03ae\b5\u00015\u00015\u00015\u0001"+
		"5\u00015\u00015\u00015\u00035\u03b7\b5\u00016\u00016\u00016\u00016\u0001"+
		"6\u00016\u00016\u00036\u03c0\b6\u00017\u00017\u00017\u00017\u00017\u0001"+
		"8\u00018\u00018\u00018\u00018\u00018\u00018\u00019\u00019\u0001:\u0001"+
		":\u0001:\u0001:\u0001:\u0001:\u0001:\u0001:\u0001:\u0001:\u0001:\u0001"+
		":\u0003:\u03dc\b:\u0001;\u0004;\u03df\b;\u000b;\f;\u03e0\u0001<\u0001"+
		"<\u0001<\u0001<\u0001<\u0005<\u03e8\b<\n<\f<\u03eb\t<\u0003<\u03ed\b<"+
		"\u0001<\u0001<\u0001=\u0001=\u0001=\u0001=\u0001>\u0001>\u0001>\u0005"+
		">\u03f8\b>\n>\f>\u03fb\t>\u0001>\u0001>\u0001>\u0005>\u0400\b>\n>\f>\u0403"+
		"\t>\u0001>\u0003>\u0406\b>\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001"+
		"?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001"+
		"?\u0001?\u0003?\u041a\b?\u0001@\u0001@\u0001@\u0001@\u0001@\u0003@\u0421"+
		"\b@\u0001@\u0001@\u0001@\u0001A\u0001A\u0001B\u0001B\u0001B\u0001B\u0001"+
		"B\u0001B\u0001B\u0001B\u0003B\u0430\bB\u0001B\u0001B\u0001B\u0001B\u0001"+
		"B\u0003B\u0437\bB\u0001C\u0001C\u0001C\u0001C\u0001C\u0003C\u043e\bC\u0001"+
		"C\u0001C\u0005C\u0442\bC\nC\fC\u0445\tC\u0001C\u0001C\u0001C\u0001C\u0003"+
		"C\u044b\bC\u0001C\u0001C\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001"+
		"D\u0001E\u0001E\u0001E\u0001E\u0001E\u0001E\u0001F\u0001F\u0001F\u0001"+
		"F\u0003F\u0460\bF\u0001F\u0003F\u0463\bF\u0001F\u0001F\u0003F\u0467\b"+
		"F\u0001F\u0001F\u0001G\u0001G\u0001G\u0001G\u0003G\u046f\bG\u0001G\u0001"+
		"G\u0003G\u0473\bG\u0001G\u0001G\u0001H\u0001H\u0001H\u0001H\u0003H\u047b"+
		"\bH\u0001H\u0001H\u0001I\u0001I\u0001J\u0003J\u0482\bJ\u0001J\u0001J\u0001"+
		"J\u0003J\u0487\bJ\u0001J\u0001J\u0001K\u0001K\u0001L\u0003L\u048e\bL\u0001"+
		"L\u0001L\u0001L\u0001L\u0001L\u0003L\u0495\bL\u0001L\u0001L\u0001M\u0001"+
		"M\u0001N\u0001N\u0001N\u0003N\u049e\bN\u0001O\u0001O\u0001O\u0001O\u0001"+
		"O\u0003O\u04a5\bO\u0001P\u0001P\u0001P\u0004P\u04aa\bP\u000bP\fP\u04ab"+
		"\u0001Q\u0001Q\u0001Q\u0001Q\u0001Q\u0003Q\u04b3\bQ\u0001Q\u0003Q\u04b6"+
		"\bQ\u0001Q\u0003Q\u04b9\bQ\u0001Q\u0001Q\u0001R\u0001R\u0001R\u0005R\u04c0"+
		"\bR\nR\fR\u04c3\tR\u0001S\u0001S\u0001S\u0001S\u0003S\u04c9\bS\u0001S"+
		"\u0003S\u04cc\bS\u0001S\u0003S\u04cf\bS\u0001T\u0001T\u0003T\u04d3\bT"+
		"\u0001T\u0001T\u0001T\u0001T\u0001T\u0003T\u04da\bT\u0001U\u0001U\u0001"+
		"U\u0003U\u04df\bU\u0001U\u0001U\u0001U\u0003U\u04e4\bU\u0001V\u0001V\u0001"+
		"V\u0003V\u04e9\bV\u0001V\u0001V\u0001V\u0003V\u04ee\bV\u0001W\u0001W\u0003"+
		"W\u04f2\bW\u0001X\u0001X\u0001X\u0001X\u0005X\u04f8\bX\nX\fX\u04fb\tX"+
		"\u0001Y\u0001Y\u0001Y\u0003Y\u0500\bY\u0001Y\u0001Y\u0001Y\u0003Y\u0505"+
		"\bY\u0001Z\u0001Z\u0001[\u0001[\u0001[\u0005[\u050c\b[\n[\f[\u050f\t["+
		"\u0001\\\u0001\\\u0001\\\u0001\\\u0003\\\u0515\b\\\u0001\\\u0001\\\u0001"+
		"\\\u0003\\\u051a\b\\\u0001\\\u0001\\\u0001\\\u0003\\\u051f\b\\\u0001\\"+
		"\u0001\\\u0001\\\u0003\\\u0524\b\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001"+
		"\\\u0003\\\u052b\b\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0003\\\u0532"+
		"\b\\\u0001\\\u0001\\\u0003\\\u0536\b\\\u0001\\\u0001\\\u0003\\\u053a\b"+
		"\\\u0003\\\u053c\b\\\u0001]\u0001]\u0001]\u0001]\u0001]\u0003]\u0543\b"+
		"]\u0001]\u0001]\u0001]\u0001]\u0003]\u0549\b]\u0001]\u0001]\u0001]\u0003"+
		"]\u054e\b]\u0001]\u0001]\u0001]\u0003]\u0553\b]\u0001]\u0001]\u0001]\u0001"+
		"]\u0001]\u0003]\u055a\b]\u0001]\u0001]\u0001]\u0001]\u0001]\u0001]\u0003"+
		"]\u0562\b]\u0001]\u0001]\u0001]\u0001]\u0001]\u0001]\u0001]\u0001]\u0001"+
		"]\u0001]\u0001]\u0001]\u0001]\u0001]\u0003]\u0572\b]\u0001]\u0001]\u0001"+
		"]\u0001]\u0003]\u0578\b]\u0001]\u0001]\u0001]\u0001]\u0001]\u0001]\u0001"+
		"]\u0001]\u0001]\u0003]\u0583\b]\u0001]\u0001]\u0001]\u0003]\u0588\b]\u0001"+
		"^\u0001^\u0001^\u0003^\u058d\b^\u0001^\u0001^\u0001^\u0001^\u0003^\u0593"+
		"\b^\u0005^\u0595\b^\n^\f^\u0598\t^\u0001_\u0001_\u0001_\u0003_\u059d\b"+
		"_\u0001_\u0001_\u0001_\u0001`\u0001`\u0001`\u0001`\u0001a\u0001a\u0001"+
		"a\u0003a\u05a9\ba\u0001a\u0001a\u0001a\u0001b\u0001b\u0001b\u0001b\u0001"+
		"b\u0001b\u0001b\u0001b\u0001b\u0001b\u0001b\u0001b\u0001b\u0003b\u05bb"+
		"\bb\u0001c\u0001c\u0001c\u0005c\u05c0\bc\nc\fc\u05c3\tc\u0001d\u0001d"+
		"\u0001d\u0005d\u05c8\bd\nd\fd\u05cb\td\u0001e\u0001e\u0001f\u0001f\u0001"+
		"f\u0001g\u0001g\u0001g\u0001h\u0005h\u05d6\bh\nh\fh\u05d9\th\u0001h\u0001"+
		"h\u0005h\u05dd\bh\nh\fh\u05e0\th\u0001i\u0001i\u0001i\u0003i\u05e5\bi"+
		"\u0001i\u0001i\u0001i\u0003i\u05ea\bi\u0001j\u0001j\u0001j\u0005j\u05ef"+
		"\bj\nj\fj\u05f2\tj\u0001j\u0001j\u0001k\u0001k\u0001k\u0003k\u05f9\bk"+
		"\u0001l\u0001l\u0001l\u0004l\u05fe\bl\u000bl\fl\u05ff\u0001m\u0001m\u0003"+
		"m\u0604\bm\u0001n\u0001n\u0001n\u0001n\u0001n\u0001n\u0003n\u060c\bn\u0003"+
		"n\u060e\bn\u0001n\u0001n\u0001n\u0001o\u0003o\u0614\bo\u0001o\u0001o\u0003"+
		"o\u0618\bo\u0001o\u0001o\u0001o\u0001o\u0001o\u0001o\u0001o\u0001o\u0001"+
		"o\u0001o\u0003o\u0624\bo\u0001o\u0001o\u0001o\u0001o\u0001o\u0001o\u0003"+
		"o\u062c\bo\u0001p\u0001p\u0001p\u0001p\u0001p\u0001p\u0003p\u0634\bp\u0001"+
		"p\u0001p\u0001p\u0001p\u0004p\u063a\bp\u000bp\fp\u063b\u0001q\u0001q\u0003"+
		"q\u0640\bq\u0001q\u0001q\u0001q\u0001q\u0003q\u0646\bq\u0001q\u0001q\u0001"+
		"q\u0001q\u0003q\u064c\bq\u0001q\u0001q\u0003q\u0650\bq\u0001r\u0001r\u0001"+
		"r\u0001r\u0001r\u0001r\u0001r\u0004r\u0659\br\u000br\fr\u065a\u0001s\u0001"+
		"s\u0003s\u065f\bs\u0001s\u0001s\u0001s\u0001s\u0003s\u0665\bs\u0001s\u0001"+
		"s\u0003s\u0669\bs\u0001t\u0001t\u0001t\u0005t\u066e\bt\nt\ft\u0671\tt"+
		"\u0001u\u0001u\u0001u\u0001u\u0001v\u0001v\u0001v\u0005v\u067a\bv\nv\f"+
		"v\u067d\tv\u0001w\u0001w\u0001w\u0001x\u0001x\u0001x\u0001x\u0001x\u0005"+
		"x\u0687\bx\nx\fx\u068a\tx\u0003x\u068c\bx\u0001x\u0001x\u0001y\u0001y"+
		"\u0001y\u0003y\u0693\by\u0001z\u0001z\u0001z\u0001z\u0001z\u0005z\u069a"+
		"\bz\nz\fz\u069d\tz\u0001z\u0001z\u0001{\u0001{\u0001{\u0001{\u0001{\u0005"+
		"{\u06a6\b{\n{\f{\u06a9\t{\u0003{\u06ab\b{\u0001{\u0001{\u0001|\u0001|"+
		"\u0001|\u0001|\u0001}\u0001}\u0001~\u0001~\u0001~\u0001~\u0001~\u0001"+
		"\u007f\u0001\u007f\u0001\u007f\u0001\u007f\u0001\u007f\u0001\u007f\u0001"+
		"\u007f\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0080\u0005"+
		"\u0080\u06c6\b\u0080\n\u0080\f\u0080\u06c9\t\u0080\u0001\u0080\u0001\u0080"+
		"\u0001\u0080\u0000\u0001h\u0081\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPR"+
		"TVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e"+
		"\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6"+
		"\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be"+
		"\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6"+
		"\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee"+
		"\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0000\u001a\u0001"+
		"\u0000\u0010\u0012\u0001\u0000\r\u000e\u0001\u0000\u0014\u0015\u0001\u0000"+
		"\u0016\u0018\u0001\u0000\u001a\u001c\u0003\u0000\u0018\u0018+-\u0098\u0098"+
		"\u0002\u0000\u0099\u0099\u009c\u009c\u0001\u0000\u0093\u0096\u0002\u0000"+
		"\u0004\u0004<<\u0002\u0000;;==\u0001\u0000>A\u0003\u0000\"\"BB\u0097\u0097"+
		"\u0001\u000023\u0001\u000045\u0001\u0000LM\u0002\u0000SSUY\u0001\u0000"+
		"`a\u0001\u0000gh\u0001\u0000jm\u0001\u0000nq\u0001\u0000st\u0003\u0000"+
		"\"\"\u0093\u0093\u0095\u0095\u000b\u0000\u0003\u0003\u0006\u0007\n\n\r"+
		"\u000e\u0014!#)..01QR^{\u009d\u009d\u0003\u0000\u0001\u0001\u000e\u000e"+
		"||\u0002\u000011\u0088\u0088\u0001\u0000\u0089\u0091\u0763\u0000\u0102"+
		"\u0001\u0000\u0000\u0000\u0002\u0108\u0001\u0000\u0000\u0000\u0004\u0110"+
		"\u0001\u0000\u0000\u0000\u0006\u011c\u0001\u0000\u0000\u0000\b\u0127\u0001"+
		"\u0000\u0000\u0000\n\u0129\u0001\u0000\u0000\u0000\f\u0135\u0001\u0000"+
		"\u0000\u0000\u000e\u0144\u0001\u0000\u0000\u0000\u0010\u0146\u0001\u0000"+
		"\u0000\u0000\u0012\u0155\u0001\u0000\u0000\u0000\u0014\u0173\u0001\u0000"+
		"\u0000\u0000\u0016\u0189\u0001\u0000\u0000\u0000\u0018\u0199\u0001\u0000"+
		"\u0000\u0000\u001a\u019b\u0001\u0000\u0000\u0000\u001c\u019d\u0001\u0000"+
		"\u0000\u0000\u001e\u01a2\u0001\u0000\u0000\u0000 \u01a8\u0001\u0000\u0000"+
		"\u0000\"\u01aa\u0001\u0000\u0000\u0000$\u01b2\u0001\u0000\u0000\u0000"+
		"&\u01c1\u0001\u0000\u0000\u0000(\u01f3\u0001\u0000\u0000\u0000*\u01f5"+
		"\u0001\u0000\u0000\u0000,\u01fe\u0001\u0000\u0000\u0000.\u020b\u0001\u0000"+
		"\u0000\u00000\u020d\u0001\u0000\u0000\u00002\u0216\u0001\u0000\u0000\u0000"+
		"4\u021c\u0001\u0000\u0000\u00006\u021f\u0001\u0000\u0000\u00008\u0221"+
		"\u0001\u0000\u0000\u0000:\u0224\u0001\u0000\u0000\u0000<\u0227\u0001\u0000"+
		"\u0000\u0000>\u0235\u0001\u0000\u0000\u0000@\u0242\u0001\u0000\u0000\u0000"+
		"B\u0245\u0001\u0000\u0000\u0000D\u0248\u0001\u0000\u0000\u0000F\u024e"+
		"\u0001\u0000\u0000\u0000H\u0263\u0001\u0000\u0000\u0000J\u0273\u0001\u0000"+
		"\u0000\u0000L\u0288\u0001\u0000\u0000\u0000N\u028a\u0001\u0000\u0000\u0000"+
		"P\u02b6\u0001\u0000\u0000\u0000R\u02b8\u0001\u0000\u0000\u0000T\u02ca"+
		"\u0001\u0000\u0000\u0000V\u02d5\u0001\u0000\u0000\u0000X\u02e1\u0001\u0000"+
		"\u0000\u0000Z\u02e3\u0001\u0000\u0000\u0000\\\u02ea\u0001\u0000\u0000"+
		"\u0000^\u02f2\u0001\u0000\u0000\u0000`\u02fd\u0001\u0000\u0000\u0000b"+
		"\u0306\u0001\u0000\u0000\u0000d\u0308\u0001\u0000\u0000\u0000f\u030a\u0001"+
		"\u0000\u0000\u0000h\u0319\u0001\u0000\u0000\u0000j\u03b6\u0001\u0000\u0000"+
		"\u0000l\u03bf\u0001\u0000\u0000\u0000n\u03c1\u0001\u0000\u0000\u0000p"+
		"\u03c6\u0001\u0000\u0000\u0000r\u03cd\u0001\u0000\u0000\u0000t\u03db\u0001"+
		"\u0000\u0000\u0000v\u03de\u0001\u0000\u0000\u0000x\u03e2\u0001\u0000\u0000"+
		"\u0000z\u03f0\u0001\u0000\u0000\u0000|\u0405\u0001\u0000\u0000\u0000~"+
		"\u0419\u0001\u0000\u0000\u0000\u0080\u041b\u0001\u0000\u0000\u0000\u0082"+
		"\u0425\u0001\u0000\u0000\u0000\u0084\u0436\u0001\u0000\u0000\u0000\u0086"+
		"\u0438\u0001\u0000\u0000\u0000\u0088\u044e\u0001\u0000\u0000\u0000\u008a"+
		"\u0455\u0001\u0000\u0000\u0000\u008c\u045b\u0001\u0000\u0000\u0000\u008e"+
		"\u046a\u0001\u0000\u0000\u0000\u0090\u0476\u0001\u0000\u0000\u0000\u0092"+
		"\u047e\u0001\u0000\u0000\u0000\u0094\u0481\u0001\u0000\u0000\u0000\u0096"+
		"\u048a\u0001\u0000\u0000\u0000\u0098\u048d\u0001\u0000\u0000\u0000\u009a"+
		"\u0498\u0001\u0000\u0000\u0000\u009c\u049a\u0001\u0000\u0000\u0000\u009e"+
		"\u04a4\u0001\u0000\u0000\u0000\u00a0\u04a6\u0001\u0000\u0000\u0000\u00a2"+
		"\u04ad\u0001\u0000\u0000\u0000\u00a4\u04bc\u0001\u0000\u0000\u0000\u00a6"+
		"\u04c4\u0001\u0000\u0000\u0000\u00a8\u04d0\u0001\u0000\u0000\u0000\u00aa"+
		"\u04db\u0001\u0000\u0000\u0000\u00ac\u04e5\u0001\u0000\u0000\u0000\u00ae"+
		"\u04ef\u0001\u0000\u0000\u0000\u00b0\u04f3\u0001\u0000\u0000\u0000\u00b2"+
		"\u04fc\u0001\u0000\u0000\u0000\u00b4\u0506\u0001\u0000\u0000\u0000\u00b6"+
		"\u0508\u0001\u0000\u0000\u0000\u00b8\u053b\u0001\u0000\u0000\u0000\u00ba"+
		"\u0587\u0001\u0000\u0000\u0000\u00bc\u0589\u0001\u0000\u0000\u0000\u00be"+
		"\u0599\u0001\u0000\u0000\u0000\u00c0\u05a1\u0001\u0000\u0000\u0000\u00c2"+
		"\u05a5\u0001\u0000\u0000\u0000\u00c4\u05ba\u0001\u0000\u0000\u0000\u00c6"+
		"\u05bc\u0001\u0000\u0000\u0000\u00c8\u05c4\u0001\u0000\u0000\u0000\u00ca"+
		"\u05cc\u0001\u0000\u0000\u0000\u00cc\u05ce\u0001\u0000\u0000\u0000\u00ce"+
		"\u05d1\u0001\u0000\u0000\u0000\u00d0\u05d7\u0001\u0000\u0000\u0000\u00d2"+
		"\u05e1\u0001\u0000\u0000\u0000\u00d4\u05eb\u0001\u0000\u0000\u0000\u00d6"+
		"\u05f8\u0001\u0000\u0000\u0000\u00d8\u05fa\u0001\u0000\u0000\u0000\u00da"+
		"\u0603\u0001\u0000\u0000\u0000\u00dc\u0605\u0001\u0000\u0000\u0000\u00de"+
		"\u0613\u0001\u0000\u0000\u0000\u00e0\u062d\u0001\u0000\u0000\u0000\u00e2"+
		"\u064f\u0001\u0000\u0000\u0000\u00e4\u0651\u0001\u0000\u0000\u0000\u00e6"+
		"\u0668\u0001\u0000\u0000\u0000\u00e8\u066a\u0001\u0000\u0000\u0000\u00ea"+
		"\u0672\u0001\u0000\u0000\u0000\u00ec\u0676\u0001\u0000\u0000\u0000\u00ee"+
		"\u067e\u0001\u0000\u0000\u0000\u00f0\u0681\u0001\u0000\u0000\u0000\u00f2"+
		"\u068f\u0001\u0000\u0000\u0000\u00f4\u0694\u0001\u0000\u0000\u0000\u00f6"+
		"\u06a0\u0001\u0000\u0000\u0000\u00f8\u06ae\u0001\u0000\u0000\u0000\u00fa"+
		"\u06b2\u0001\u0000\u0000\u0000\u00fc\u06b4\u0001\u0000\u0000\u0000\u00fe"+
		"\u06b9\u0001\u0000\u0000\u0000\u0100\u06c0\u0001\u0000\u0000\u0000\u0102"+
		"\u0103\u0003\u0002\u0001\u0000\u0103\u0104\u0005\u0000\u0000\u0001\u0104"+
		"\u0001\u0001\u0000\u0000\u0000\u0105\u0107\u0003\u0004\u0002\u0000\u0106"+
		"\u0105\u0001\u0000\u0000\u0000\u0107\u010a\u0001\u0000\u0000\u0000\u0108"+
		"\u0106\u0001\u0000\u0000\u0000\u0108\u0109\u0001\u0000\u0000\u0000\u0109"+
		"\u0003\u0001\u0000\u0000\u0000\u010a\u0108\u0001\u0000\u0000\u0000\u010b"+
		"\u0111\u0003\u0006\u0003\u0000\u010c\u0111\u0003\n\u0005\u0000\u010d\u0111"+
		"\u0003\u0012\t\u0000\u010e\u0111\u0003\u0014\n\u0000\u010f\u0111\u0003"+
		"&\u0013\u0000\u0110\u010b\u0001\u0000\u0000\u0000\u0110\u010c\u0001\u0000"+
		"\u0000\u0000\u0110\u010d\u0001\u0000\u0000\u0000\u0110\u010e\u0001\u0000"+
		"\u0000\u0000\u0110\u010f\u0001\u0000\u0000\u0000\u0111\u0005\u0001\u0000"+
		"\u0000\u0000\u0112\u0113\u0005\u0001\u0000\u0000\u0113\u0114\u0003\u00c8"+
		"d\u0000\u0114\u0115\u0005\u0002\u0000\u0000\u0115\u011d\u0001\u0000\u0000"+
		"\u0000\u0116\u0117\u0005\u0003\u0000\u0000\u0117\u0118\u0003\u00c8d\u0000"+
		"\u0118\u0119\u0005\u0001\u0000\u0000\u0119\u011a\u0003\b\u0004\u0000\u011a"+
		"\u011b\u0005\u0002\u0000\u0000\u011b\u011d\u0001\u0000\u0000\u0000\u011c"+
		"\u0112\u0001\u0000\u0000\u0000\u011c\u0116\u0001\u0000\u0000\u0000\u011d"+
		"\u0007\u0001\u0000\u0000\u0000\u011e\u0128\u0005\u0004\u0000\u0000\u011f"+
		"\u0124\u0003\u00cae\u0000\u0120\u0121\u0005\u0005\u0000\u0000\u0121\u0123"+
		"\u0003\u00cae\u0000\u0122\u0120\u0001\u0000\u0000\u0000\u0123\u0126\u0001"+
		"\u0000\u0000\u0000\u0124\u0122\u0001\u0000\u0000\u0000\u0124\u0125\u0001"+
		"\u0000\u0000\u0000\u0125\u0128\u0001\u0000\u0000\u0000\u0126\u0124\u0001"+
		"\u0000\u0000\u0000\u0127\u011e\u0001\u0000\u0000\u0000\u0127\u011f\u0001"+
		"\u0000\u0000\u0000\u0128\t\u0001\u0000\u0000\u0000\u0129\u012a\u0005\u0006"+
		"\u0000\u0000\u012a\u012c\u0003\u00cae\u0000\u012b\u012d\u0005\u009c\u0000"+
		"\u0000\u012c\u012b\u0001\u0000\u0000\u0000\u012c\u012d\u0001\u0000\u0000"+
		"\u0000\u012d\u012e\u0001\u0000\u0000\u0000\u012e\u012f\u0005\u0007\u0000"+
		"\u0000\u012f\u0131\u0003\f\u0006\u0000\u0130\u0132\u0003\u0010\b\u0000"+
		"\u0131\u0130\u0001\u0000\u0000\u0000\u0131\u0132\u0001\u0000\u0000\u0000"+
		"\u0132\u0133\u0001\u0000\u0000\u0000\u0133\u0134\u0005\u0002\u0000\u0000"+
		"\u0134\u000b\u0001\u0000\u0000\u0000\u0135\u013a\u0003\u000e\u0007\u0000"+
		"\u0136\u0137\u0005\u0005\u0000\u0000\u0137\u0139\u0003\u000e\u0007\u0000"+
		"\u0138\u0136\u0001\u0000\u0000\u0000\u0139\u013c\u0001\u0000\u0000\u0000"+
		"\u013a\u0138\u0001\u0000\u0000\u0000\u013a\u013b\u0001\u0000\u0000\u0000"+
		"\u013b\r\u0001\u0000\u0000\u0000\u013c\u013a\u0001\u0000\u0000\u0000\u013d"+
		"\u0141\u0003\u00b6[\u0000\u013e\u013f\u0005\b\u0000\u0000\u013f\u0140"+
		"\u0005\u009c\u0000\u0000\u0140\u0142\u0005\t\u0000\u0000\u0141\u013e\u0001"+
		"\u0000\u0000\u0000\u0141\u0142\u0001\u0000\u0000\u0000\u0142\u0145\u0001"+
		"\u0000\u0000\u0000\u0143\u0145\u0005\u009c\u0000\u0000\u0144\u013d\u0001"+
		"\u0000\u0000\u0000\u0144\u0143\u0001\u0000\u0000\u0000\u0145\u000f\u0001"+
		"\u0000\u0000\u0000\u0146\u0147\u0005\n\u0000\u0000\u0147\u0148\u0005\u000b"+
		"\u0000\u0000\u0148\u014d\u0003h4\u0000\u0149\u014a\u0005\u0005\u0000\u0000"+
		"\u014a\u014c\u0003h4\u0000\u014b\u0149\u0001\u0000\u0000\u0000\u014c\u014f"+
		"\u0001\u0000\u0000\u0000\u014d\u014b\u0001\u0000\u0000\u0000\u014d\u014e"+
		"\u0001\u0000\u0000\u0000\u014e\u0150\u0001\u0000\u0000\u0000\u014f\u014d"+
		"\u0001\u0000\u0000\u0000\u0150\u0151\u0005\f\u0000\u0000\u0151\u0011\u0001"+
		"\u0000\u0000\u0000\u0152\u0154\u0003$\u0012\u0000\u0153\u0152\u0001\u0000"+
		"\u0000\u0000\u0154\u0157\u0001\u0000\u0000\u0000\u0155\u0153\u0001\u0000"+
		"\u0000\u0000\u0155\u0156\u0001\u0000\u0000\u0000\u0156\u0158\u0001\u0000"+
		"\u0000\u0000\u0157\u0155\u0001\u0000\u0000\u0000\u0158\u0159\u0005\r\u0000"+
		"\u0000\u0159\u015a\u0003\u00c8d\u0000\u015a\u015c\u0005\b\u0000\u0000"+
		"\u015b\u015d\u0003\u0016\u000b\u0000\u015c\u015b\u0001\u0000\u0000\u0000"+
		"\u015c\u015d\u0001\u0000\u0000\u0000\u015d\u015e\u0001\u0000\u0000\u0000"+
		"\u015e\u0162\u0005\t\u0000\u0000\u015f\u0161\u0003\u001e\u000f\u0000\u0160"+
		"\u015f\u0001\u0000\u0000\u0000\u0161\u0164\u0001\u0000\u0000\u0000\u0162"+
		"\u0160\u0001\u0000\u0000\u0000\u0162\u0163\u0001\u0000\u0000\u0000\u0163"+
		"\u0171\u0001\u0000\u0000\u0000\u0164\u0162\u0001\u0000\u0000\u0000\u0165"+
		"\u0169\u0005\u000b\u0000\u0000\u0166\u0168\u0003&\u0013\u0000\u0167\u0166"+
		"\u0001\u0000\u0000\u0000\u0168\u016b\u0001\u0000\u0000\u0000\u0169\u0167"+
		"\u0001\u0000\u0000\u0000\u0169\u016a\u0001\u0000\u0000\u0000\u016a\u016c"+
		"\u0001\u0000\u0000\u0000\u016b\u0169\u0001\u0000\u0000\u0000\u016c\u016e"+
		"\u0005\f\u0000\u0000\u016d\u016f\u0005\u0002\u0000\u0000\u016e\u016d\u0001"+
		"\u0000\u0000\u0000\u016e\u016f\u0001\u0000\u0000\u0000\u016f\u0172\u0001"+
		"\u0000\u0000\u0000\u0170\u0172\u0005\u0002\u0000\u0000\u0171\u0165\u0001"+
		"\u0000\u0000\u0000\u0171\u0170\u0001\u0000\u0000\u0000\u0172\u0013\u0001"+
		"\u0000\u0000\u0000\u0173\u0174\u0005\u000e\u0000\u0000\u0174\u0176\u0003"+
		"\u00c8d\u0000\u0175\u0177\u0003N\'\u0000\u0176\u0175\u0001\u0000\u0000"+
		"\u0000\u0176\u0177\u0001\u0000\u0000\u0000\u0177\u017b\u0001\u0000\u0000"+
		"\u0000\u0178\u017a\u0003\u001e\u000f\u0000\u0179\u0178\u0001\u0000\u0000"+
		"\u0000\u017a\u017d\u0001\u0000\u0000\u0000\u017b\u0179\u0001\u0000\u0000"+
		"\u0000\u017b\u017c\u0001\u0000\u0000\u0000\u017c\u017e\u0001\u0000\u0000"+
		"\u0000\u017d\u017b\u0001\u0000\u0000\u0000\u017e\u0182\u0005\u000b\u0000"+
		"\u0000\u017f\u0181\u0003&\u0013\u0000\u0180\u017f\u0001\u0000\u0000\u0000"+
		"\u0181\u0184\u0001\u0000\u0000\u0000\u0182\u0180\u0001\u0000\u0000\u0000"+
		"\u0182\u0183\u0001\u0000\u0000\u0000\u0183\u0185\u0001\u0000\u0000\u0000"+
		"\u0184\u0182\u0001\u0000\u0000\u0000\u0185\u0187\u0005\f\u0000\u0000\u0186"+
		"\u0188\u0005\u0002\u0000\u0000\u0187\u0186\u0001\u0000\u0000\u0000\u0187"+
		"\u0188\u0001\u0000\u0000\u0000\u0188\u0015\u0001\u0000\u0000\u0000\u0189"+
		"\u018e\u0003\u0018\f\u0000\u018a\u018b\u0005\u0005\u0000\u0000\u018b\u018d"+
		"\u0003\u0018\f\u0000\u018c\u018a\u0001\u0000\u0000\u0000\u018d\u0190\u0001"+
		"\u0000\u0000\u0000\u018e\u018c\u0001\u0000\u0000\u0000\u018e\u018f\u0001"+
		"\u0000\u0000\u0000\u018f\u0017\u0001\u0000\u0000\u0000\u0190\u018e\u0001"+
		"\u0000\u0000\u0000\u0191\u0192\u0003\u001a\r\u0000\u0192\u0193\u0003\u00ca"+
		"e\u0000\u0193\u0194\u0005\u000f\u0000\u0000\u0194\u0195\u0003\u001c\u000e"+
		"\u0000\u0195\u019a\u0001\u0000\u0000\u0000\u0196\u0197\u0003\u001a\r\u0000"+
		"\u0197\u0198\u0003\u001c\u000e\u0000\u0198\u019a\u0001\u0000\u0000\u0000"+
		"\u0199\u0191\u0001\u0000\u0000\u0000\u0199\u0196\u0001\u0000\u0000\u0000"+
		"\u019a\u0019\u0001\u0000\u0000\u0000\u019b\u019c\u0007\u0000\u0000\u0000"+
		"\u019c\u001b\u0001\u0000\u0000\u0000\u019d\u01a0\u0003l6\u0000\u019e\u019f"+
		"\u0005\u0013\u0000\u0000\u019f\u01a1\u0003\u00cae\u0000\u01a0\u019e\u0001"+
		"\u0000\u0000\u0000\u01a0\u01a1\u0001\u0000\u0000\u0000\u01a1\u001d\u0001"+
		"\u0000\u0000\u0000\u01a2\u01a4\u0003 \u0010\u0000\u01a3\u01a5\u0007\u0001"+
		"\u0000\u0000\u01a4\u01a3\u0001\u0000\u0000\u0000\u01a4\u01a5\u0001\u0000"+
		"\u0000\u0000\u01a5\u01a6\u0001\u0000\u0000\u0000\u01a6\u01a7\u0003\"\u0011"+
		"\u0000\u01a7\u001f\u0001\u0000\u0000\u0000\u01a8\u01a9\u0007\u0002\u0000"+
		"\u0000\u01a9!\u0001\u0000\u0000\u0000\u01aa\u01af\u0003\u00c8d\u0000\u01ab"+
		"\u01ac\u0005\u0005\u0000\u0000\u01ac\u01ae\u0003\u00c8d\u0000\u01ad\u01ab"+
		"\u0001\u0000\u0000\u0000\u01ae\u01b1\u0001\u0000\u0000\u0000\u01af\u01ad"+
		"\u0001\u0000\u0000\u0000\u01af\u01b0\u0001\u0000\u0000\u0000\u01b0#\u0001"+
		"\u0000\u0000\u0000\u01b1\u01af\u0001\u0000\u0000\u0000\u01b2\u01b3\u0007"+
		"\u0003\u0000\u0000\u01b3%\u0001\u0000\u0000\u0000\u01b4\u01c2\u0003(\u0014"+
		"\u0000\u01b5\u01c2\u0003F#\u0000\u01b6\u01c2\u0003H$\u0000\u01b7\u01c2"+
		"\u0003J%\u0000\u01b8\u01c2\u0003L&\u0000\u01b9\u01c2\u0003P(\u0000\u01ba"+
		"\u01c2\u0003R)\u0000\u01bb\u01bc\u0003Z-\u0000\u01bc\u01bd\u0005\u0002"+
		"\u0000\u0000\u01bd\u01c2\u0001\u0000\u0000\u0000\u01be\u01bf\u0003^/\u0000"+
		"\u01bf\u01c0\u0005\u0002\u0000\u0000\u01c0\u01c2\u0001\u0000\u0000\u0000"+
		"\u01c1\u01b4\u0001\u0000\u0000\u0000\u01c1\u01b5\u0001\u0000\u0000\u0000"+
		"\u01c1\u01b6\u0001\u0000\u0000\u0000\u01c1\u01b7\u0001\u0000\u0000\u0000"+
		"\u01c1\u01b8\u0001\u0000\u0000\u0000\u01c1\u01b9\u0001\u0000\u0000\u0000"+
		"\u01c1\u01ba\u0001\u0000\u0000\u0000\u01c1\u01bb\u0001\u0000\u0000\u0000"+
		"\u01c1\u01be\u0001\u0000\u0000\u0000\u01c2\'\u0001\u0000\u0000\u0000\u01c3"+
		"\u01c5\u0003$\u0012\u0000\u01c4\u01c3\u0001\u0000\u0000\u0000\u01c5\u01c8"+
		"\u0001\u0000\u0000\u0000\u01c6\u01c4\u0001\u0000\u0000\u0000\u01c6\u01c7"+
		"\u0001\u0000\u0000\u0000\u01c7\u01c9\u0001\u0000\u0000\u0000\u01c8\u01c6"+
		"\u0001\u0000\u0000\u0000\u01c9\u01cb\u0005\u0019\u0000\u0000\u01ca\u01cc"+
		"\u0003\u001a\r\u0000\u01cb\u01ca\u0001\u0000\u0000\u0000\u01cb\u01cc\u0001"+
		"\u0000\u0000\u0000\u01cc\u01cd\u0001\u0000\u0000\u0000\u01cd\u01ce\u0003"+
		"\u00c4b\u0000\u01ce\u01d2\u0003*\u0015\u0000\u01cf\u01d1\u00034\u001a"+
		"\u0000\u01d0\u01cf\u0001\u0000\u0000\u0000\u01d1\u01d4\u0001\u0000\u0000"+
		"\u0000\u01d2\u01d0\u0001\u0000\u0000\u0000\u01d2\u01d3\u0001\u0000\u0000"+
		"\u0000\u01d3\u01d6\u0001\u0000\u0000\u0000\u01d4\u01d2\u0001\u0000\u0000"+
		"\u0000\u01d5\u01d7\u00038\u001c\u0000\u01d6\u01d5\u0001\u0000\u0000\u0000"+
		"\u01d6\u01d7\u0001\u0000\u0000\u0000\u01d7\u01d9\u0001\u0000\u0000\u0000"+
		"\u01d8\u01da\u0003:\u001d\u0000\u01d9\u01d8\u0001\u0000\u0000\u0000\u01d9"+
		"\u01da\u0001\u0000\u0000\u0000\u01da\u01db\u0001\u0000\u0000\u0000\u01db"+
		"\u01dd\u0003>\u001f\u0000\u01dc\u01de\u0005\u0002\u0000\u0000\u01dd\u01dc"+
		"\u0001\u0000\u0000\u0000\u01dd\u01de\u0001\u0000\u0000\u0000\u01de\u01f4"+
		"\u0001\u0000\u0000\u0000\u01df\u01e1\u0003$\u0012\u0000\u01e0\u01df\u0001"+
		"\u0000\u0000\u0000\u01e1\u01e4\u0001\u0000\u0000\u0000\u01e2\u01e0\u0001"+
		"\u0000\u0000\u0000\u01e2\u01e3\u0001\u0000\u0000\u0000\u01e3\u01e5\u0001"+
		"\u0000\u0000\u0000\u01e4\u01e2\u0001\u0000\u0000\u0000\u01e5\u01e7\u0005"+
		"\u0019\u0000\u0000\u01e6\u01e8\u0003\u001a\r\u0000\u01e7\u01e6\u0001\u0000"+
		"\u0000\u0000\u01e7\u01e8\u0001\u0000\u0000\u0000\u01e8\u01e9\u0001\u0000"+
		"\u0000\u0000\u01e9\u01ea\u0003\u00c4b\u0000\u01ea\u01ee\u0003*\u0015\u0000"+
		"\u01eb\u01ed\u00034\u001a\u0000\u01ec\u01eb\u0001\u0000\u0000\u0000\u01ed"+
		"\u01f0\u0001\u0000\u0000\u0000\u01ee\u01ec\u0001\u0000\u0000\u0000\u01ee"+
		"\u01ef\u0001\u0000\u0000\u0000\u01ef\u01f1\u0001\u0000\u0000\u0000\u01f0"+
		"\u01ee\u0001\u0000\u0000\u0000\u01f1\u01f2\u0005\u0002\u0000\u0000\u01f2"+
		"\u01f4\u0001\u0000\u0000\u0000\u01f3\u01c6\u0001\u0000\u0000\u0000\u01f3"+
		"\u01e2\u0001\u0000\u0000\u0000\u01f4)\u0001\u0000\u0000\u0000\u01f5\u01f7"+
		"\u0005\b\u0000\u0000\u01f6\u01f8\u00030\u0018\u0000\u01f7\u01f6\u0001"+
		"\u0000\u0000\u0000\u01f7\u01f8\u0001\u0000\u0000\u0000\u01f8\u01f9\u0001"+
		"\u0000\u0000\u0000\u01f9\u01fc\u0005\t\u0000\u0000\u01fa\u01fb\u0005\u000f"+
		"\u0000\u0000\u01fb\u01fd\u0003,\u0016\u0000\u01fc\u01fa\u0001\u0000\u0000"+
		"\u0000\u01fc\u01fd\u0001\u0000\u0000\u0000\u01fd+\u0001\u0000\u0000\u0000"+
		"\u01fe\u0203\u0003.\u0017\u0000\u01ff\u0200\u0005\u0005\u0000\u0000\u0200"+
		"\u0202\u0003.\u0017\u0000\u0201\u01ff\u0001\u0000\u0000\u0000\u0202\u0205"+
		"\u0001\u0000\u0000\u0000\u0203\u0201\u0001\u0000\u0000\u0000\u0203\u0204"+
		"\u0001\u0000\u0000\u0000\u0204-\u0001\u0000\u0000\u0000\u0205\u0203\u0001"+
		"\u0000\u0000\u0000\u0206\u0207\u0003\u00cae\u0000\u0207\u0208\u0005\u000f"+
		"\u0000\u0000\u0208\u0209\u0003l6\u0000\u0209\u020c\u0001\u0000\u0000\u0000"+
		"\u020a\u020c\u0003l6\u0000\u020b\u0206\u0001\u0000\u0000\u0000\u020b\u020a"+
		"\u0001\u0000\u0000\u0000\u020c/\u0001\u0000\u0000\u0000\u020d\u0212\u0003"+
		"2\u0019\u0000\u020e\u020f\u0005\u0005\u0000\u0000\u020f\u0211\u00032\u0019"+
		"\u0000\u0210\u020e\u0001\u0000\u0000\u0000\u0211\u0214\u0001\u0000\u0000"+
		"\u0000\u0212\u0210\u0001\u0000\u0000\u0000\u0212\u0213\u0001\u0000\u0000"+
		"\u0000\u02131\u0001\u0000\u0000\u0000\u0214\u0212\u0001\u0000\u0000\u0000"+
		"\u0215\u0217\u0003\u001a\r\u0000\u0216\u0215\u0001\u0000\u0000\u0000\u0216"+
		"\u0217\u0001\u0000\u0000\u0000\u0217\u0218\u0001\u0000\u0000\u0000\u0218"+
		"\u0219\u0003\u00cae\u0000\u0219\u021a\u0005\u000f\u0000\u0000\u021a\u021b"+
		"\u0003l6\u0000\u021b3\u0001\u0000\u0000\u0000\u021c\u021d\u00036\u001b"+
		"\u0000\u021d\u021e\u0003\u00c6c\u0000\u021e5\u0001\u0000\u0000\u0000\u021f"+
		"\u0220\u0007\u0004\u0000\u0000\u02207\u0001\u0000\u0000\u0000\u0221\u0222"+
		"\u0005\u001d\u0000\u0000\u0222\u0223\u0003<\u001e\u0000\u02239\u0001\u0000"+
		"\u0000\u0000\u0224\u0225\u0005\n\u0000\u0000\u0225\u0226\u0003<\u001e"+
		"\u0000\u0226;\u0001\u0000\u0000\u0000\u0227\u0228\u0005\u000b\u0000\u0000"+
		"\u0228\u022d\u0003h4\u0000\u0229\u022a\u0005\u0002\u0000\u0000\u022a\u022c"+
		"\u0003h4\u0000\u022b\u0229\u0001\u0000\u0000\u0000\u022c\u022f\u0001\u0000"+
		"\u0000\u0000\u022d\u022b\u0001\u0000\u0000\u0000\u022d\u022e\u0001\u0000"+
		"\u0000\u0000\u022e\u0231\u0001\u0000\u0000\u0000\u022f\u022d\u0001\u0000"+
		"\u0000\u0000\u0230\u0232\u0005\u0002\u0000\u0000\u0231\u0230\u0001\u0000"+
		"\u0000\u0000\u0231\u0232\u0001\u0000\u0000\u0000\u0232\u0233\u0001\u0000"+
		"\u0000\u0000\u0233\u0234\u0005\f\u0000\u0000\u0234=\u0001\u0000\u0000"+
		"\u0000\u0235\u0237\u0005\u000b\u0000\u0000\u0236\u0238\u0003@ \u0000\u0237"+
		"\u0236\u0001\u0000\u0000\u0000\u0237\u0238\u0001\u0000\u0000\u0000\u0238"+
		"\u023b\u0001\u0000\u0000\u0000\u0239\u023c\u0003B!\u0000\u023a\u023c\u0003"+
		"`0\u0000\u023b\u0239\u0001\u0000\u0000\u0000\u023b\u023a\u0001\u0000\u0000"+
		"\u0000\u023c\u023e\u0001\u0000\u0000\u0000\u023d\u023f\u0003D\"\u0000"+
		"\u023e\u023d\u0001\u0000\u0000\u0000\u023e\u023f\u0001\u0000\u0000\u0000"+
		"\u023f\u0240\u0001\u0000\u0000\u0000\u0240\u0241\u0005\f\u0000\u0000\u0241"+
		"?\u0001\u0000\u0000\u0000\u0242\u0243\u0005\u001e\u0000\u0000\u0243\u0244"+
		"\u0003f3\u0000\u0244A\u0001\u0000\u0000\u0000\u0245\u0246\u0005\u001f"+
		"\u0000\u0000\u0246\u0247\u0003f3\u0000\u0247C\u0001\u0000\u0000\u0000"+
		"\u0248\u0249\u0005 \u0000\u0000\u0249\u024a\u0003f3\u0000\u024aE\u0001"+
		"\u0000\u0000\u0000\u024b\u024d\u0003$\u0012\u0000\u024c\u024b\u0001\u0000"+
		"\u0000\u0000\u024d\u0250\u0001\u0000\u0000\u0000\u024e\u024c\u0001\u0000"+
		"\u0000\u0000\u024e\u024f\u0001\u0000\u0000\u0000\u024f\u0251\u0001\u0000"+
		"\u0000\u0000\u0250\u024e\u0001\u0000\u0000\u0000\u0251\u0252\u0005!\u0000"+
		"\u0000\u0252\u0253\u0003\u00c4b\u0000\u0253\u0256\u0003N\'\u0000\u0254"+
		"\u0255\u0005\u000f\u0000\u0000\u0255\u0257\u0003l6\u0000\u0256\u0254\u0001"+
		"\u0000\u0000\u0000\u0256\u0257\u0001\u0000\u0000\u0000\u0257\u025e\u0001"+
		"\u0000\u0000\u0000\u0258\u025f\u0003f3\u0000\u0259\u025a\u0005\"\u0000"+
		"\u0000\u025a\u025b\u0003h4\u0000\u025b\u025c\u0005\u0002\u0000\u0000\u025c"+
		"\u025f\u0001\u0000\u0000\u0000\u025d\u025f\u0005\u0002\u0000\u0000\u025e"+
		"\u0258\u0001\u0000\u0000\u0000\u025e\u0259\u0001\u0000\u0000\u0000\u025e"+
		"\u025d\u0001\u0000\u0000\u0000\u025fG\u0001\u0000\u0000\u0000\u0260\u0262"+
		"\u0003$\u0012\u0000\u0261\u0260\u0001\u0000\u0000\u0000\u0262\u0265\u0001"+
		"\u0000\u0000\u0000\u0263\u0261\u0001\u0000\u0000\u0000\u0263\u0264\u0001"+
		"\u0000\u0000\u0000\u0264\u0266\u0001\u0000\u0000\u0000\u0265\u0263\u0001"+
		"\u0000\u0000\u0000\u0266\u0267\u0005#\u0000\u0000\u0267\u0268\u0003\u00c4"+
		"b\u0000\u0268\u0269\u0003N\'\u0000\u0269\u026a\u0005\u000f\u0000\u0000"+
		"\u026a\u0271\u0003,\u0016\u0000\u026b\u0272\u0003f3\u0000\u026c\u026d"+
		"\u0005\"\u0000\u0000\u026d\u026e\u0003h4\u0000\u026e\u026f\u0005\u0002"+
		"\u0000\u0000\u026f\u0272\u0001\u0000\u0000\u0000\u0270\u0272\u0005\u0002"+
		"\u0000\u0000\u0271\u026b\u0001\u0000\u0000\u0000\u0271\u026c\u0001\u0000"+
		"\u0000\u0000\u0271\u0270\u0001\u0000\u0000\u0000\u0272I\u0001\u0000\u0000"+
		"\u0000\u0273\u0274\u0005$\u0000\u0000\u0274\u0275\u0003\u00c4b\u0000\u0275"+
		"\u0276\u0003N\'\u0000\u0276\u0278\u0003f3\u0000\u0277\u0279\u0005\u0002"+
		"\u0000\u0000\u0278\u0277\u0001\u0000\u0000\u0000\u0278\u0279\u0001\u0000"+
		"\u0000\u0000\u0279K\u0001\u0000\u0000\u0000\u027a\u027b\u0005%\u0000\u0000"+
		"\u027b\u027c\u0003N\'\u0000\u027c\u027e\u0003f3\u0000\u027d\u027f\u0005"+
		"\u0002\u0000\u0000\u027e\u027d\u0001\u0000\u0000\u0000\u027e\u027f\u0001"+
		"\u0000\u0000\u0000\u027f\u0289\u0001\u0000\u0000\u0000\u0280\u0282\u0005"+
		"&\u0000\u0000\u0281\u0283\u0003N\'\u0000\u0282\u0281\u0001\u0000\u0000"+
		"\u0000\u0282\u0283\u0001\u0000\u0000\u0000\u0283\u0284\u0001\u0000\u0000"+
		"\u0000\u0284\u0286\u0003f3\u0000\u0285\u0287\u0005\u0002\u0000\u0000\u0286"+
		"\u0285\u0001\u0000\u0000\u0000\u0286\u0287\u0001\u0000\u0000\u0000\u0287"+
		"\u0289\u0001\u0000\u0000\u0000\u0288\u027a\u0001\u0000\u0000\u0000\u0288"+
		"\u0280\u0001\u0000\u0000\u0000\u0289M\u0001\u0000\u0000\u0000\u028a\u028c"+
		"\u0005\b\u0000\u0000\u028b\u028d\u00030\u0018\u0000\u028c\u028b\u0001"+
		"\u0000\u0000\u0000\u028c\u028d\u0001\u0000\u0000\u0000\u028d\u028e\u0001"+
		"\u0000\u0000\u0000\u028e\u028f\u0005\t\u0000\u0000\u028fO\u0001\u0000"+
		"\u0000\u0000\u0290\u0291\u0005\'\u0000\u0000\u0291\u0292\u0005(\u0000"+
		"\u0000\u0292\u0293\u0003\u00c4b\u0000\u0293\u0294\u0005\u000f\u0000\u0000"+
		"\u0294\u0297\u0003l6\u0000\u0295\u0296\u0005\"\u0000\u0000\u0296\u0298"+
		"\u0003h4\u0000\u0297\u0295\u0001\u0000\u0000\u0000\u0297\u0298\u0001\u0000"+
		"\u0000\u0000\u0298\u0299\u0001\u0000\u0000\u0000\u0299\u029a\u0005\u0002"+
		"\u0000\u0000\u029a\u02b7\u0001\u0000\u0000\u0000\u029b\u029c\u0005)\u0000"+
		"\u0000\u029c\u029d\u0005(\u0000\u0000\u029d\u029e\u0003\u00cae\u0000\u029e"+
		"\u029f\u0005\u000f\u0000\u0000\u029f\u02a2\u0003l6\u0000\u02a0\u02a1\u0005"+
		"\"\u0000\u0000\u02a1\u02a3\u0003h4\u0000\u02a2\u02a0\u0001\u0000\u0000"+
		"\u0000\u02a2\u02a3\u0001\u0000\u0000\u0000\u02a3\u02a4\u0001\u0000\u0000"+
		"\u0000\u02a4\u02a5\u0005\u0002\u0000\u0000\u02a5\u02b7\u0001\u0000\u0000"+
		"\u0000\u02a6\u02a7\u0005(\u0000\u0000\u02a7\u02a8\u0003\u00cae\u0000\u02a8"+
		"\u02a9\u0005\u000f\u0000\u0000\u02a9\u02ac\u0003l6\u0000\u02aa\u02ab\u0005"+
		"\"\u0000\u0000\u02ab\u02ad\u0003h4\u0000\u02ac\u02aa\u0001\u0000\u0000"+
		"\u0000\u02ac\u02ad\u0001\u0000\u0000\u0000\u02ad\u02ae\u0001\u0000\u0000"+
		"\u0000\u02ae\u02af\u0005\u0002\u0000\u0000\u02af\u02b7\u0001\u0000\u0000"+
		"\u0000\u02b0\u02b1\u0005(\u0000\u0000\u02b1\u02b2\u0003\u00cae\u0000\u02b2"+
		"\u02b3\u0005\"\u0000\u0000\u02b3\u02b4\u0003h4\u0000\u02b4\u02b5\u0005"+
		"\u0002\u0000\u0000\u02b5\u02b7\u0001\u0000\u0000\u0000\u02b6\u0290\u0001"+
		"\u0000\u0000\u0000\u02b6\u029b\u0001\u0000\u0000\u0000\u02b6\u02a6\u0001"+
		"\u0000\u0000\u0000\u02b6\u02b0\u0001\u0000\u0000\u0000\u02b7Q\u0001\u0000"+
		"\u0000\u0000\u02b8\u02b9\u0005\'\u0000\u0000\u02b9\u02ba\u0005*\u0000"+
		"\u0000\u02ba\u02bd\u0003\u00cae\u0000\u02bb\u02bc\u0005\u0015\u0000\u0000"+
		"\u02bc\u02be\u0003T*\u0000\u02bd\u02bb\u0001\u0000\u0000\u0000\u02bd\u02be"+
		"\u0001\u0000\u0000\u0000\u02be\u02bf\u0001\u0000\u0000\u0000\u02bf\u02c3"+
		"\u0005\u000b\u0000\u0000\u02c0\u02c2\u0003V+\u0000\u02c1\u02c0\u0001\u0000"+
		"\u0000\u0000\u02c2\u02c5\u0001\u0000\u0000\u0000\u02c3\u02c1\u0001\u0000"+
		"\u0000\u0000\u02c3\u02c4\u0001\u0000\u0000\u0000\u02c4\u02c6\u0001\u0000"+
		"\u0000\u0000\u02c5\u02c3\u0001\u0000\u0000\u0000\u02c6\u02c8\u0005\f\u0000"+
		"\u0000\u02c7\u02c9\u0005\u0002\u0000\u0000\u02c8\u02c7\u0001\u0000\u0000"+
		"\u0000\u02c8\u02c9\u0001\u0000\u0000\u0000\u02c9S\u0001\u0000\u0000\u0000"+
		"\u02ca\u02cf\u0003l6\u0000\u02cb\u02cc\u0005\u0005\u0000\u0000\u02cc\u02ce"+
		"\u0003l6\u0000\u02cd\u02cb\u0001\u0000\u0000\u0000\u02ce\u02d1\u0001\u0000"+
		"\u0000\u0000\u02cf\u02cd\u0001\u0000\u0000\u0000\u02cf\u02d0\u0001\u0000"+
		"\u0000\u0000\u02d0U\u0001\u0000\u0000\u0000\u02d1\u02cf\u0001\u0000\u0000"+
		"\u0000\u02d2\u02d4\u0003X,\u0000\u02d3\u02d2\u0001\u0000\u0000\u0000\u02d4"+
		"\u02d7\u0001\u0000\u0000\u0000\u02d5\u02d3\u0001\u0000\u0000\u0000\u02d5"+
		"\u02d6\u0001\u0000\u0000\u0000\u02d6\u02d8\u0001\u0000\u0000\u0000\u02d7"+
		"\u02d5\u0001\u0000\u0000\u0000\u02d8\u02d9\u0003\u00cae\u0000\u02d9\u02da"+
		"\u0005\u000f\u0000\u0000\u02da\u02dd\u0003l6\u0000\u02db\u02dc\u0005\""+
		"\u0000\u0000\u02dc\u02de\u0003h4\u0000\u02dd\u02db\u0001\u0000\u0000\u0000"+
		"\u02dd\u02de\u0001\u0000\u0000\u0000\u02de\u02df\u0001\u0000\u0000\u0000"+
		"\u02df\u02e0\u0005\u0002\u0000\u0000\u02e0W\u0001\u0000\u0000\u0000\u02e1"+
		"\u02e2\u0007\u0005\u0000\u0000\u02e2Y\u0001\u0000\u0000\u0000\u02e3\u02e4"+
		"\u0005.\u0000\u0000\u02e4\u02e5\u0007\u0006\u0000\u0000\u02e5\u02e8\u0003"+
		"\\.\u0000\u02e6\u02e7\u0005\"\u0000\u0000\u02e7\u02e9\u0003h4\u0000\u02e8"+
		"\u02e6\u0001\u0000\u0000\u0000\u02e8\u02e9\u0001\u0000\u0000\u0000\u02e9"+
		"[\u0001\u0000\u0000\u0000\u02ea\u02ef\u0003\u00cae\u0000\u02eb\u02ec\u0005"+
		"/\u0000\u0000\u02ec\u02ee\u0003\u00cae\u0000\u02ed\u02eb\u0001\u0000\u0000"+
		"\u0000\u02ee\u02f1\u0001\u0000\u0000\u0000\u02ef\u02ed\u0001\u0000\u0000"+
		"\u0000\u02ef\u02f0\u0001\u0000\u0000\u0000\u02f0]\u0001\u0000\u0000\u0000"+
		"\u02f1\u02ef\u0001\u0000\u0000\u0000\u02f2\u02f3\u00050\u0000\u0000\u02f3"+
		"\u02f4\u0003\u00cae\u0000\u02f4\u02f5\u0005\"\u0000\u0000\u02f5\u02f8"+
		"\u0003l6\u0000\u02f6\u02f7\u00051\u0000\u0000\u02f7\u02f9\u0003h4\u0000"+
		"\u02f8\u02f6\u0001\u0000\u0000\u0000\u02f8\u02f9\u0001\u0000\u0000\u0000"+
		"\u02f9_\u0001\u0000\u0000\u0000\u02fa\u02fc\u0003b1\u0000\u02fb\u02fa"+
		"\u0001\u0000\u0000\u0000\u02fc\u02ff\u0001\u0000\u0000\u0000\u02fd\u02fb"+
		"\u0001\u0000\u0000\u0000\u02fd\u02fe\u0001\u0000\u0000\u0000\u02fea\u0001"+
		"\u0000\u0000\u0000\u02ff\u02fd\u0001\u0000\u0000\u0000\u0300\u0301\u0003"+
		"\u00b0X\u0000\u0301\u0302\u0005\u0002\u0000\u0000\u0302\u0307\u0001\u0000"+
		"\u0000\u0000\u0303\u0304\u0003h4\u0000\u0304\u0305\u0005\u0002\u0000\u0000"+
		"\u0305\u0307\u0001\u0000\u0000\u0000\u0306\u0300\u0001\u0000\u0000\u0000"+
		"\u0306\u0303\u0001\u0000\u0000\u0000\u0307c\u0001\u0000\u0000\u0000\u0308"+
		"\u0309\u0007\u0007\u0000\u0000\u0309e\u0001\u0000\u0000\u0000\u030a\u030e"+
		"\u0005\u000b\u0000\u0000\u030b\u030d\u0003b1\u0000\u030c\u030b\u0001\u0000"+
		"\u0000\u0000\u030d\u0310\u0001\u0000\u0000\u0000\u030e\u030c\u0001\u0000"+
		"\u0000\u0000\u030e\u030f\u0001\u0000\u0000\u0000\u030f\u0311\u0001\u0000"+
		"\u0000\u0000\u0310\u030e\u0001\u0000\u0000\u0000\u0311\u0312\u0005\f\u0000"+
		"\u0000\u0312g\u0001\u0000\u0000\u0000\u0313\u0314\u00064\uffff\uffff\u0000"+
		"\u0314\u0315\u0005:\u0000\u0000\u0315\u031a\u0003h4\f\u0316\u0317\u0005"+
		";\u0000\u0000\u0317\u031a\u0003h4\u000b\u0318\u031a\u0003j5\u0000\u0319"+
		"\u0313\u0001\u0000\u0000\u0000\u0319\u0316\u0001\u0000\u0000\u0000\u0319"+
		"\u0318\u0001\u0000\u0000\u0000\u031a\u0357\u0001\u0000\u0000\u0000\u031b"+
		"\u031c\n\n\u0000\u0000\u031c\u031d\u0007\b\u0000\u0000\u031d\u0356\u0003"+
		"h4\u000b\u031e\u031f\n\t\u0000\u0000\u031f\u0320\u0007\t\u0000\u0000\u0320"+
		"\u0356\u0003h4\n\u0321\u0322\n\b\u0000\u0000\u0322\u0323\u0007\n\u0000"+
		"\u0000\u0323\u0356\u0003h4\t\u0324\u0325\n\u0007\u0000\u0000\u0325\u0326"+
		"\u0007\u000b\u0000\u0000\u0326\u0356\u0003h4\b\u0327\u0328\n\u0006\u0000"+
		"\u0000\u0328\u0329\u0005C\u0000\u0000\u0329\u0356\u0003h4\u0007\u032a"+
		"\u032b\n\u0005\u0000\u0000\u032b\u032c\u0005D\u0000\u0000\u032c\u0356"+
		"\u0003h4\u0006\u032d\u032e\n\u0004\u0000\u0000\u032e\u032f\u0005E\u0000"+
		"\u0000\u032f\u0356\u0003h4\u0005\u0330\u0331\n\u0003\u0000\u0000\u0331"+
		"\u0332\u0005F\u0000\u0000\u0332\u0356\u0003h4\u0004\u0333\u0334\n\u0010"+
		"\u0000\u0000\u0334\u0335\u0007\f\u0000\u0000\u0335\u0356\u0003\u00b8\\"+
		"\u0000\u0336\u0337\n\u000f\u0000\u0000\u0337\u0338\u0007\r\u0000\u0000"+
		"\u0338\u0356\u0003\u00ba]\u0000\u0339\u033a\n\u000e\u0000\u0000\u033a"+
		"\u033e\u00056\u0000\u0000\u033b\u033c\u0003\u00cae\u0000\u033c\u033d\u0005"+
		"7\u0000\u0000\u033d\u033f\u0001\u0000\u0000\u0000\u033e\u033b\u0001\u0000"+
		"\u0000\u0000\u033e\u033f\u0001\u0000\u0000\u0000\u033f\u0340\u0001\u0000"+
		"\u0000\u0000\u0340\u0341\u0003h4\u0000\u0341\u0342\u00058\u0000\u0000"+
		"\u0342\u0356\u0001\u0000\u0000\u0000\u0343\u0344\n\r\u0000\u0000\u0344"+
		"\u0345\u00059\u0000\u0000\u0345\u0349\u00056\u0000\u0000\u0346\u0347\u0003"+
		"\u00cae\u0000\u0347\u0348\u00057\u0000\u0000\u0348\u034a\u0001\u0000\u0000"+
		"\u0000\u0349\u0346\u0001\u0000\u0000\u0000\u0349\u034a\u0001\u0000\u0000"+
		"\u0000\u034a\u034b\u0001\u0000\u0000\u0000\u034b\u034c\u0003h4\u0000\u034c"+
		"\u034d\u00058\u0000\u0000\u034d\u0356\u0001\u0000\u0000\u0000\u034e\u034f"+
		"\n\u0002\u0000\u0000\u034f\u0350\u0003d2\u0000\u0350\u0353\u0003h4\u0000"+
		"\u0351\u0352\u0005G\u0000\u0000\u0352\u0354\u0003h4\u0000\u0353\u0351"+
		"\u0001\u0000\u0000\u0000\u0353\u0354\u0001\u0000\u0000\u0000\u0354\u0356"+
		"\u0001\u0000\u0000\u0000\u0355\u031b\u0001\u0000\u0000\u0000\u0355\u031e"+
		"\u0001\u0000\u0000\u0000\u0355\u0321\u0001\u0000\u0000\u0000\u0355\u0324"+
		"\u0001\u0000\u0000\u0000\u0355\u0327\u0001\u0000\u0000\u0000\u0355\u032a"+
		"\u0001\u0000\u0000\u0000\u0355\u032d\u0001\u0000\u0000\u0000\u0355\u0330"+
		"\u0001\u0000\u0000\u0000\u0355\u0333\u0001\u0000\u0000\u0000\u0355\u0336"+
		"\u0001\u0000\u0000\u0000\u0355\u0339\u0001\u0000\u0000\u0000\u0355\u0343"+
		"\u0001\u0000\u0000\u0000\u0355\u034e\u0001\u0000\u0000\u0000\u0356\u0359"+
		"\u0001\u0000\u0000\u0000\u0357\u0355\u0001\u0000\u0000\u0000\u0357\u0358"+
		"\u0001\u0000\u0000\u0000\u0358i\u0001\u0000\u0000\u0000\u0359\u0357\u0001"+
		"\u0000\u0000\u0000\u035a\u035b\u0005\b\u0000\u0000\u035b\u035c\u0003h"+
		"4\u0000\u035c\u035d\u0005\t\u0000\u0000\u035d\u03b7\u0001\u0000\u0000"+
		"\u0000\u035e\u03b7\u0005H\u0000\u0000\u035f\u03b7\u0005I\u0000\u0000\u0360"+
		"\u0361\u0005J\u0000\u0000\u0361\u0362\u0003h4\u0000\u0362\u0363\u0005"+
		"K\u0000\u0000\u0363\u036b\u0003h4\u0000\u0364\u0365\u0007\u000e\u0000"+
		"\u0000\u0365\u0366\u0003h4\u0000\u0366\u0367\u0005K\u0000\u0000\u0367"+
		"\u0368\u0003h4\u0000\u0368\u036a\u0001\u0000\u0000\u0000\u0369\u0364\u0001"+
		"\u0000\u0000\u0000\u036a\u036d\u0001\u0000\u0000\u0000\u036b\u0369\u0001"+
		"\u0000\u0000\u0000\u036b\u036c\u0001\u0000\u0000\u0000\u036c\u0370\u0001"+
		"\u0000\u0000\u0000\u036d\u036b\u0001\u0000\u0000\u0000\u036e\u036f\u0005"+
		"N\u0000\u0000\u036f\u0371\u0003h4\u0000\u0370\u036e\u0001\u0000\u0000"+
		"\u0000\u0370\u0371\u0001\u0000\u0000\u0000\u0371\u0372\u0001\u0000\u0000"+
		"\u0000\u0372\u0373\u0005O\u0000\u0000\u0373\u03b7\u0001\u0000\u0000\u0000"+
		"\u0374\u0375\u0005J\u0000\u0000\u0375\u0376\u0005\b\u0000\u0000\u0376"+
		"\u0377\u0003h4\u0000\u0377\u0378\u0005\t\u0000\u0000\u0378\u0381\u0003"+
		"f3\u0000\u0379\u037a\u0005M\u0000\u0000\u037a\u037b\u0005\b\u0000\u0000"+
		"\u037b\u037c\u0003h4\u0000\u037c\u037d\u0005\t\u0000\u0000\u037d\u037e"+
		"\u0003f3\u0000\u037e\u0380\u0001\u0000\u0000\u0000\u037f\u0379\u0001\u0000"+
		"\u0000\u0000\u0380\u0383\u0001\u0000\u0000\u0000\u0381\u037f\u0001\u0000"+
		"\u0000\u0000\u0381\u0382\u0001\u0000\u0000\u0000\u0382\u0386\u0001\u0000"+
		"\u0000\u0000\u0383\u0381\u0001\u0000\u0000\u0000\u0384\u0385\u0005N\u0000"+
		"\u0000\u0385\u0387\u0003f3\u0000\u0386\u0384\u0001\u0000\u0000\u0000\u0386"+
		"\u0387\u0001\u0000\u0000\u0000\u0387\u0389\u0001\u0000\u0000\u0000\u0388"+
		"\u038a\u0005O\u0000\u0000\u0389\u0388\u0001\u0000\u0000\u0000\u0389\u038a"+
		"\u0001\u0000\u0000\u0000\u038a\u03b7\u0001\u0000\u0000\u0000\u038b\u038c"+
		"\u0005P\u0000\u0000\u038c\u0391\u0003\u00be_\u0000\u038d\u038e\u0005\u0005"+
		"\u0000\u0000\u038e\u0390\u0003\u00be_\u0000\u038f\u038d\u0001\u0000\u0000"+
		"\u0000\u0390\u0393\u0001\u0000\u0000\u0000\u0391\u038f\u0001\u0000\u0000"+
		"\u0000\u0391\u0392\u0001\u0000\u0000\u0000\u0392\u0394\u0001\u0000\u0000"+
		"\u0000\u0393\u0391\u0001\u0000\u0000\u0000\u0394\u0395\u0005\u0010\u0000"+
		"\u0000\u0395\u0396\u0003h4\u0000\u0396\u03b7\u0001\u0000\u0000\u0000\u0397"+
		"\u03b7\u0003t:\u0000\u0398\u03b7\u0003|>\u0000\u0399\u03b7\u0003~?\u0000"+
		"\u039a\u03b7\u0003\u0080@\u0000\u039b\u03b7\u0003\u0086C\u0000\u039c\u03b7"+
		"\u0003\u008aE\u0000\u039d\u03b7\u0003\u008cF\u0000\u039e\u03b7\u0003\u008e"+
		"G\u0000\u039f\u03b7\u0003\u0090H\u0000\u03a0\u03b7\u0003\u0094J\u0000"+
		"\u03a1\u03b7\u0003\u0098L\u0000\u03a2\u03b7\u0003\u00a0P\u0000\u03a3\u03b7"+
		"\u0003\u00a6S\u0000\u03a4\u03b7\u0003\u00a8T\u0000\u03a5\u03b7\u0003\u00ac"+
		"V\u0000\u03a6\u03b7\u0003\u00aeW\u0000\u03a7\u03b7\u0005Q\u0000\u0000"+
		"\u03a8\u03b7\u0005R\u0000\u0000\u03a9\u03b7\u0003\u00b0X\u0000\u03aa\u03ab"+
		"\u0003\u00b6[\u0000\u03ab\u03ad\u0005\b\u0000\u0000\u03ac\u03ae\u0003"+
		"\u00ecv\u0000\u03ad\u03ac\u0001\u0000\u0000\u0000\u03ad\u03ae\u0001\u0000"+
		"\u0000\u0000\u03ae\u03af\u0001\u0000\u0000\u0000\u03af\u03b0\u0005\t\u0000"+
		"\u0000\u03b0\u03b7\u0001\u0000\u0000\u0000\u03b1\u03b7\u0003\u00fa}\u0000"+
		"\u03b2\u03b7\u0003\u00fc~\u0000\u03b3\u03b7\u0003\u00fe\u007f\u0000\u03b4"+
		"\u03b7\u0003\u0100\u0080\u0000\u03b5\u03b7\u0003\u00b6[\u0000\u03b6\u035a"+
		"\u0001\u0000\u0000\u0000\u03b6\u035e\u0001\u0000\u0000\u0000\u03b6\u035f"+
		"\u0001\u0000\u0000\u0000\u03b6\u0360\u0001\u0000\u0000\u0000\u03b6\u0374"+
		"\u0001\u0000\u0000\u0000\u03b6\u038b\u0001\u0000\u0000\u0000\u03b6\u0397"+
		"\u0001\u0000\u0000\u0000\u03b6\u0398\u0001\u0000\u0000\u0000\u03b6\u0399"+
		"\u0001\u0000\u0000\u0000\u03b6\u039a\u0001\u0000\u0000\u0000\u03b6\u039b"+
		"\u0001\u0000\u0000\u0000\u03b6\u039c\u0001\u0000\u0000\u0000\u03b6\u039d"+
		"\u0001\u0000\u0000\u0000\u03b6\u039e\u0001\u0000\u0000\u0000\u03b6\u039f"+
		"\u0001\u0000\u0000\u0000\u03b6\u03a0\u0001\u0000\u0000\u0000\u03b6\u03a1"+
		"\u0001\u0000\u0000\u0000\u03b6\u03a2\u0001\u0000\u0000\u0000\u03b6\u03a3"+
		"\u0001\u0000\u0000\u0000\u03b6\u03a4\u0001\u0000\u0000\u0000\u03b6\u03a5"+
		"\u0001\u0000\u0000\u0000\u03b6\u03a6\u0001\u0000\u0000\u0000\u03b6\u03a7"+
		"\u0001\u0000\u0000\u0000\u03b6\u03a8\u0001\u0000\u0000\u0000\u03b6\u03a9"+
		"\u0001\u0000\u0000\u0000\u03b6\u03aa\u0001\u0000\u0000\u0000\u03b6\u03b1"+
		"\u0001\u0000\u0000\u0000\u03b6\u03b2\u0001\u0000\u0000\u0000\u03b6\u03b3"+
		"\u0001\u0000\u0000\u0000\u03b6\u03b4\u0001\u0000\u0000\u0000\u03b6\u03b5"+
		"\u0001\u0000\u0000\u0000\u03b7k\u0001\u0000\u0000\u0000\u03b8\u03c0\u0003"+
		"\u00fa}\u0000\u03b9\u03c0\u0003\u00fc~\u0000\u03ba\u03c0\u0003\u00fe\u007f"+
		"\u0000\u03bb\u03c0\u0003\u0100\u0080\u0000\u03bc\u03c0\u0003n7\u0000\u03bd"+
		"\u03c0\u0003p8\u0000\u03be\u03c0\u0003\u00b6[\u0000\u03bf\u03b8\u0001"+
		"\u0000\u0000\u0000\u03bf\u03b9\u0001\u0000\u0000\u0000\u03bf\u03ba\u0001"+
		"\u0000\u0000\u0000\u03bf\u03bb\u0001\u0000\u0000\u0000\u03bf\u03bc\u0001"+
		"\u0000\u0000\u0000\u03bf\u03bd\u0001\u0000\u0000\u0000\u03bf\u03be\u0001"+
		"\u0000\u0000\u0000\u03c0m\u0001\u0000\u0000\u0000\u03c1\u03c2\u0005S\u0000"+
		"\u0000\u03c2\u03c3\u0005\b\u0000\u0000\u03c3\u03c4\u0003l6\u0000\u03c4"+
		"\u03c5\u0005\t\u0000\u0000\u03c5o\u0001\u0000\u0000\u0000\u03c6\u03c7"+
		"\u0005T\u0000\u0000\u03c7\u03c8\u0005\b\u0000\u0000\u03c8\u03c9\u0003"+
		"l6\u0000\u03c9\u03ca\u0005\u0005\u0000\u0000\u03ca\u03cb\u0003l6\u0000"+
		"\u03cb\u03cc\u0005\t\u0000\u0000\u03ccq\u0001\u0000\u0000\u0000\u03cd"+
		"\u03ce\u0007\u000f\u0000\u0000\u03ces\u0001\u0000\u0000\u0000\u03cf\u03dc"+
		"\u0005\u009b\u0000\u0000\u03d0\u03dc\u0005\u009a\u0000\u0000\u03d1\u03dc"+
		"\u0003v;\u0000\u03d2\u03dc\u0005Z\u0000\u0000\u03d3\u03dc\u0005[\u0000"+
		"\u0000\u03d4\u03dc\u0005\\\u0000\u0000\u03d5\u03dc\u0005]\u0000\u0000"+
		"\u03d6\u03dc\u0005\u0004\u0000\u0000\u03d7\u03dc\u0003\u00f0x\u0000\u03d8"+
		"\u03dc\u0003\u00f4z\u0000\u03d9\u03dc\u0003\u00f6{\u0000\u03da\u03dc\u0003"+
		"x<\u0000\u03db\u03cf\u0001\u0000\u0000\u0000\u03db\u03d0\u0001\u0000\u0000"+
		"\u0000\u03db\u03d1\u0001\u0000\u0000\u0000\u03db\u03d2\u0001\u0000\u0000"+
		"\u0000\u03db\u03d3\u0001\u0000\u0000\u0000\u03db\u03d4\u0001\u0000\u0000"+
		"\u0000\u03db\u03d5\u0001\u0000\u0000\u0000\u03db\u03d6\u0001\u0000\u0000"+
		"\u0000\u03db\u03d7\u0001\u0000\u0000\u0000\u03db\u03d8\u0001\u0000\u0000"+
		"\u0000\u03db\u03d9\u0001\u0000\u0000\u0000\u03db\u03da\u0001\u0000\u0000"+
		"\u0000\u03dcu\u0001\u0000\u0000\u0000\u03dd\u03df\u0007\u0006\u0000\u0000"+
		"\u03de\u03dd\u0001\u0000\u0000\u0000\u03df\u03e0\u0001\u0000\u0000\u0000"+
		"\u03e0\u03de\u0001\u0000\u0000\u0000\u03e0\u03e1\u0001\u0000\u0000\u0000"+
		"\u03e1w\u0001\u0000\u0000\u0000\u03e2\u03e3\u0005T\u0000\u0000\u03e3\u03ec"+
		"\u0005\u000b\u0000\u0000\u03e4\u03e9\u0003z=\u0000\u03e5\u03e6\u0005\u0005"+
		"\u0000\u0000\u03e6\u03e8\u0003z=\u0000\u03e7\u03e5\u0001\u0000\u0000\u0000"+
		"\u03e8\u03eb\u0001\u0000\u0000\u0000\u03e9\u03e7\u0001\u0000\u0000\u0000"+
		"\u03e9\u03ea\u0001\u0000\u0000\u0000\u03ea\u03ed\u0001\u0000\u0000\u0000"+
		"\u03eb\u03e9\u0001\u0000\u0000\u0000\u03ec\u03e4\u0001\u0000\u0000\u0000"+
		"\u03ec\u03ed\u0001\u0000\u0000\u0000\u03ed\u03ee\u0001\u0000\u0000\u0000"+
		"\u03ee\u03ef\u0005\f\u0000\u0000\u03efy\u0001\u0000\u0000\u0000\u03f0"+
		"\u03f1\u0003h4\u0000\u03f1\u03f2\u0005\"\u0000\u0000\u03f2\u03f3\u0003"+
		"h4\u0000\u03f3{\u0001\u0000\u0000\u0000\u03f4\u03f5\u0005^\u0000\u0000"+
		"\u03f5\u03f9\u0005\u000b\u0000\u0000\u03f6\u03f8\u0003b1\u0000\u03f7\u03f6"+
		"\u0001\u0000\u0000\u0000\u03f8\u03fb\u0001\u0000\u0000\u0000\u03f9\u03f7"+
		"\u0001\u0000\u0000\u0000\u03f9\u03fa\u0001\u0000\u0000\u0000\u03fa\u03fc"+
		"\u0001\u0000\u0000\u0000\u03fb\u03f9\u0001\u0000\u0000\u0000\u03fc\u0406"+
		"\u0005\f\u0000\u0000\u03fd\u0401\u0005\u000b\u0000\u0000\u03fe\u0400\u0003"+
		"b1\u0000\u03ff\u03fe\u0001\u0000\u0000\u0000\u0400\u0403\u0001\u0000\u0000"+
		"\u0000\u0401\u03ff\u0001\u0000\u0000\u0000\u0401\u0402\u0001\u0000\u0000"+
		"\u0000\u0402\u0404\u0001\u0000\u0000\u0000\u0403\u0401\u0001\u0000\u0000"+
		"\u0000\u0404\u0406\u0005\f\u0000\u0000\u0405\u03f4\u0001\u0000\u0000\u0000"+
		"\u0405\u03fd\u0001\u0000\u0000\u0000\u0406}\u0001\u0000\u0000\u0000\u0407"+
		"\u0408\u0005_\u0000\u0000\u0408\u0409\u0005\b\u0000\u0000\u0409\u040a"+
		"\u0003\u00cae\u0000\u040a\u040b\u0005\u000f\u0000\u0000\u040b\u040c\u0003"+
		"l6\u0000\u040c\u040d\u0005\u0093\u0000\u0000\u040d\u040e\u0003h4\u0000"+
		"\u040e\u040f\u0005\u0002\u0000\u0000\u040f\u0410\u0003h4\u0000\u0410\u0411"+
		"\u0005\t\u0000\u0000\u0411\u0412\u0003f3\u0000\u0412\u041a\u0001\u0000"+
		"\u0000\u0000\u0413\u0414\u0005_\u0000\u0000\u0414\u0415\u0005\b\u0000"+
		"\u0000\u0415\u0416\u0003h4\u0000\u0416\u0417\u0005\t\u0000\u0000\u0417"+
		"\u0418\u0003f3\u0000\u0418\u041a\u0001\u0000\u0000\u0000\u0419\u0407\u0001"+
		"\u0000\u0000\u0000\u0419\u0413\u0001\u0000\u0000\u0000\u041a\u007f\u0001"+
		"\u0000\u0000\u0000\u041b\u041c\u0003\u0082A\u0000\u041c\u041d\u0005\b"+
		"\u0000\u0000\u041d\u0420\u0003\u0084B\u0000\u041e\u041f\u00057\u0000\u0000"+
		"\u041f\u0421\u0003h4\u0000\u0420\u041e\u0001\u0000\u0000\u0000\u0420\u0421"+
		"\u0001\u0000\u0000\u0000\u0421\u0422\u0001\u0000\u0000\u0000\u0422\u0423"+
		"\u0005\t\u0000\u0000\u0423\u0424\u0003f3\u0000\u0424\u0081\u0001\u0000"+
		"\u0000\u0000\u0425\u0426\u0007\u0010\u0000\u0000\u0426\u0083\u0001\u0000"+
		"\u0000\u0000\u0427\u0428\u0003\u00bc^\u0000\u0428\u0429\u0005\u0002\u0000"+
		"\u0000\u0429\u042a\u0003\u00cae\u0000\u042a\u042b\u0005\u000f\u0000\u0000"+
		"\u042b\u042f\u0003l6\u0000\u042c\u042d\u0003\u00b4Z\u0000\u042d\u042e"+
		"\u0003h4\u0000\u042e\u0430\u0001\u0000\u0000\u0000\u042f\u042c\u0001\u0000"+
		"\u0000\u0000\u042f\u0430\u0001\u0000\u0000\u0000\u0430\u0437\u0001\u0000"+
		"\u0000\u0000\u0431\u0432\u0003\u00bc^\u0000\u0432\u0433\u0005\u0002\u0000"+
		"\u0000\u0433\u0434\u0003l6\u0000\u0434\u0437\u0001\u0000\u0000\u0000\u0435"+
		"\u0437\u0003\u00bc^\u0000\u0436\u0427\u0001\u0000\u0000\u0000\u0436\u0431"+
		"\u0001\u0000\u0000\u0000\u0436\u0435\u0001\u0000\u0000\u0000\u0437\u0085"+
		"\u0001\u0000\u0000\u0000\u0438\u043d\u0005b\u0000\u0000\u0439\u043a\u0005"+
		"\b\u0000\u0000\u043a\u043b\u0003h4\u0000\u043b\u043c\u0005\t\u0000\u0000"+
		"\u043c\u043e\u0001\u0000\u0000\u0000\u043d\u0439\u0001\u0000\u0000\u0000"+
		"\u043d\u043e\u0001\u0000\u0000\u0000\u043e\u043f\u0001\u0000\u0000\u0000"+
		"\u043f\u0443\u0005\u000b\u0000\u0000\u0440\u0442\u0003\u0088D\u0000\u0441"+
		"\u0440\u0001\u0000\u0000\u0000\u0442\u0445\u0001\u0000\u0000\u0000\u0443"+
		"\u0441\u0001\u0000\u0000\u0000\u0443\u0444\u0001\u0000\u0000\u0000\u0444"+
		"\u044a\u0001\u0000\u0000\u0000\u0445\u0443\u0001\u0000\u0000\u0000\u0446"+
		"\u0447\u0005N\u0000\u0000\u0447\u0448\u0003h4\u0000\u0448\u0449\u0005"+
		"\u0002\u0000\u0000\u0449\u044b\u0001\u0000\u0000\u0000\u044a\u0446\u0001"+
		"\u0000\u0000\u0000\u044a\u044b\u0001\u0000\u0000\u0000\u044b\u044c\u0001"+
		"\u0000\u0000\u0000\u044c\u044d\u0005\f\u0000\u0000\u044d\u0087\u0001\u0000"+
		"\u0000\u0000\u044e\u044f\u0005c\u0000\u0000\u044f\u0450\u0005\b\u0000"+
		"\u0000\u0450\u0451\u0003h4\u0000\u0451\u0452\u0005\t\u0000\u0000\u0452"+
		"\u0453\u0003h4\u0000\u0453\u0454\u0005\u0002\u0000\u0000\u0454\u0089\u0001"+
		"\u0000\u0000\u0000\u0455\u0456\u0005d\u0000\u0000\u0456\u0457\u0005\b"+
		"\u0000\u0000\u0457\u0458\u0003\u00b2Y\u0000\u0458\u0459\u0005\t\u0000"+
		"\u0000\u0459\u045a\u0003f3\u0000\u045a\u008b\u0001\u0000\u0000\u0000\u045b"+
		"\u045f\u0005e\u0000\u0000\u045c\u045d\u0003\u00cae\u0000\u045d\u045e\u0005"+
		"\u000f\u0000\u0000\u045e\u0460\u0001\u0000\u0000\u0000\u045f\u045c\u0001"+
		"\u0000\u0000\u0000\u045f\u0460\u0001\u0000\u0000\u0000\u0460\u0462\u0001"+
		"\u0000\u0000\u0000\u0461\u0463\u0003l6\u0000\u0462\u0461\u0001\u0000\u0000"+
		"\u0000\u0462\u0463\u0001\u0000\u0000\u0000\u0463\u0466\u0001\u0000\u0000"+
		"\u0000\u0464\u0465\u0005\u0013\u0000\u0000\u0465\u0467\u0003\u00cae\u0000"+
		"\u0466\u0464\u0001\u0000\u0000\u0000\u0466\u0467\u0001\u0000\u0000\u0000"+
		"\u0467\u0468\u0001\u0000\u0000\u0000\u0468\u0469\u0003f3\u0000\u0469\u008d"+
		"\u0001\u0000\u0000\u0000\u046a\u046b\u0005f\u0000\u0000\u046b\u046e\u0003"+
		"\u00b6[\u0000\u046c\u046d\u0005\u0013\u0000\u0000\u046d\u046f\u0003\u00ca"+
		"e\u0000\u046e\u046c\u0001\u0000\u0000\u0000\u046e\u046f\u0001\u0000\u0000"+
		"\u0000\u046f\u0470\u0001\u0000\u0000\u0000\u0470\u0472\u0005\b\u0000\u0000"+
		"\u0471\u0473\u0003\u00ecv\u0000\u0472\u0471\u0001\u0000\u0000\u0000\u0472"+
		"\u0473\u0001\u0000\u0000\u0000\u0473\u0474\u0001\u0000\u0000\u0000\u0474"+
		"\u0475\u0005\t\u0000\u0000\u0475\u008f\u0001\u0000\u0000\u0000\u0476\u0477"+
		"\u0003\u0092I\u0000\u0477\u0478\u0003\u00c4b\u0000\u0478\u047a\u0005\b"+
		"\u0000\u0000\u0479\u047b\u0003\u00ecv\u0000\u047a\u0479\u0001\u0000\u0000"+
		"\u0000\u047a\u047b\u0001\u0000\u0000\u0000\u047b\u047c\u0001\u0000\u0000"+
		"\u0000\u047c\u047d\u0005\t\u0000\u0000\u047d\u0091\u0001\u0000\u0000\u0000"+
		"\u047e\u047f\u0007\u0011\u0000\u0000\u047f\u0093\u0001\u0000\u0000\u0000"+
		"\u0480\u0482\u0005i\u0000\u0000\u0481\u0480\u0001\u0000\u0000\u0000\u0481"+
		"\u0482\u0001\u0000\u0000\u0000\u0482\u0483\u0001\u0000\u0000\u0000\u0483"+
		"\u0484\u0003\u0096K\u0000\u0484\u0486\u0005\b\u0000\u0000\u0485\u0487"+
		"\u0003\u009cN\u0000\u0486\u0485\u0001\u0000\u0000\u0000\u0486\u0487\u0001"+
		"\u0000\u0000\u0000\u0487\u0488\u0001\u0000\u0000\u0000\u0488\u0489\u0005"+
		"\t\u0000\u0000\u0489\u0095\u0001\u0000\u0000\u0000\u048a\u048b\u0007\u0012"+
		"\u0000\u0000\u048b\u0097\u0001\u0000\u0000\u0000\u048c\u048e\u0005i\u0000"+
		"\u0000\u048d\u048c\u0001\u0000\u0000\u0000\u048d\u048e\u0001\u0000\u0000"+
		"\u0000\u048e\u048f\u0001\u0000\u0000\u0000\u048f\u0490\u0003\u009aM\u0000"+
		"\u0490\u0491\u0005\b\u0000\u0000\u0491\u0494\u0003\u00c4b\u0000\u0492"+
		"\u0493\u0005\u0005\u0000\u0000\u0493\u0495\u0003\u009cN\u0000\u0494\u0492"+
		"\u0001\u0000\u0000\u0000\u0494\u0495\u0001\u0000\u0000\u0000\u0495\u0496"+
		"\u0001\u0000\u0000\u0000\u0496\u0497\u0005\t\u0000\u0000\u0497\u0099\u0001"+
		"\u0000\u0000\u0000\u0498\u0499\u0007\u0013\u0000\u0000\u0499\u009b\u0001"+
		"\u0000\u0000\u0000\u049a\u049d\u0003\u009eO\u0000\u049b\u049c\u00057\u0000"+
		"\u0000\u049c\u049e\u0003h4\u0000\u049d\u049b\u0001\u0000\u0000\u0000\u049d"+
		"\u049e\u0001\u0000\u0000\u0000\u049e\u009d\u0001\u0000\u0000\u0000\u049f"+
		"\u04a0\u0003\u00cae\u0000\u04a0\u04a1\u0005\u000f\u0000\u0000\u04a1\u04a2"+
		"\u0003l6\u0000\u04a2\u04a5\u0001\u0000\u0000\u0000\u04a3\u04a5\u0003l"+
		"6\u0000\u04a4\u049f\u0001\u0000\u0000\u0000\u04a4\u04a3\u0001\u0000\u0000"+
		"\u0000\u04a5\u009f\u0001\u0000\u0000\u0000\u04a6\u04a7\u0005r\u0000\u0000"+
		"\u04a7\u04a9\u0003f3\u0000\u04a8\u04aa\u0003\u00a2Q\u0000\u04a9\u04a8"+
		"\u0001\u0000\u0000\u0000\u04aa\u04ab\u0001\u0000\u0000\u0000\u04ab\u04a9"+
		"\u0001\u0000\u0000\u0000\u04ab\u04ac\u0001\u0000\u0000\u0000\u04ac\u00a1"+
		"\u0001\u0000\u0000\u0000\u04ad\u04b8\u0007\u0014\u0000\u0000\u04ae\u04b5"+
		"\u0005\b\u0000\u0000\u04af\u04b0\u0003\u00cae\u0000\u04b0\u04b1\u0005"+
		"\u000f\u0000\u0000\u04b1\u04b3\u0001\u0000\u0000\u0000\u04b2\u04af\u0001"+
		"\u0000\u0000\u0000\u04b2\u04b3\u0001\u0000\u0000\u0000\u04b3\u04b4\u0001"+
		"\u0000\u0000\u0000\u04b4\u04b6\u0003\u00a4R\u0000\u04b5\u04b2\u0001\u0000"+
		"\u0000\u0000\u04b5\u04b6\u0001\u0000\u0000\u0000\u04b6\u04b7\u0001\u0000"+
		"\u0000\u0000\u04b7\u04b9\u0005\t\u0000\u0000\u04b8\u04ae\u0001\u0000\u0000"+
		"\u0000\u04b8\u04b9\u0001\u0000\u0000\u0000\u04b9\u04ba\u0001\u0000\u0000"+
		"\u0000\u04ba\u04bb\u0003f3\u0000\u04bb\u00a3\u0001\u0000\u0000\u0000\u04bc"+
		"\u04c1\u0003\u00b6[\u0000\u04bd\u04be\u0005\u0005\u0000\u0000\u04be\u04c0"+
		"\u0003\u00b6[\u0000\u04bf\u04bd\u0001\u0000\u0000\u0000\u04c0\u04c3\u0001"+
		"\u0000\u0000\u0000\u04c1\u04bf\u0001\u0000\u0000\u0000\u04c1\u04c2\u0001"+
		"\u0000\u0000\u0000\u04c2\u00a5\u0001\u0000\u0000\u0000\u04c3\u04c1\u0001"+
		"\u0000\u0000\u0000\u04c4\u04ce\u0005u\u0000\u0000\u04c5\u04cb\u0003\u00b6"+
		"[\u0000\u04c6\u04c8\u0005\b\u0000\u0000\u04c7\u04c9\u0003h4\u0000\u04c8"+
		"\u04c7\u0001\u0000\u0000\u0000\u04c8\u04c9\u0001\u0000\u0000\u0000\u04c9"+
		"\u04ca\u0001\u0000\u0000\u0000\u04ca\u04cc\u0005\t\u0000\u0000\u04cb\u04c6"+
		"\u0001\u0000\u0000\u0000\u04cb\u04cc\u0001\u0000\u0000\u0000\u04cc\u04cf"+
		"\u0001\u0000\u0000\u0000\u04cd\u04cf\u0005\u009c\u0000\u0000\u04ce\u04c5"+
		"\u0001\u0000\u0000\u0000\u04ce\u04cd\u0001\u0000\u0000\u0000\u04cf\u00a7"+
		"\u0001\u0000\u0000\u0000\u04d0\u04d2\u0005v\u0000\u0000\u04d1\u04d3\u0003"+
		"\u00cae\u0000\u04d2\u04d1\u0001\u0000\u0000\u0000\u04d2\u04d3\u0001\u0000"+
		"\u0000\u0000\u04d3\u04d4\u0001\u0000\u0000\u0000\u04d4\u04d5\u0005\b\u0000"+
		"\u0000\u04d5\u04d6\u0003h4\u0000\u04d6\u04d9\u0005\t\u0000\u0000\u04d7"+
		"\u04d8\u00051\u0000\u0000\u04d8\u04da\u0003\u00aaU\u0000\u04d9\u04d7\u0001"+
		"\u0000\u0000\u0000\u04d9\u04da\u0001\u0000\u0000\u0000\u04da\u00a9\u0001"+
		"\u0000\u0000\u0000\u04db\u04dc\u0005w\u0000\u0000\u04dc\u04de\u0005\b"+
		"\u0000\u0000\u04dd\u04df\u0003\u00ecv\u0000\u04de\u04dd\u0001\u0000\u0000"+
		"\u0000\u04de\u04df\u0001\u0000\u0000\u0000\u04df\u04e0\u0001\u0000\u0000"+
		"\u0000\u04e0\u04e3\u0005\t\u0000\u0000\u04e1\u04e2\u0005\u001d\u0000\u0000"+
		"\u04e2\u04e4\u0003h4\u0000\u04e3\u04e1\u0001\u0000\u0000\u0000\u04e3\u04e4"+
		"\u0001\u0000\u0000\u0000\u04e4\u00ab\u0001\u0000\u0000\u0000\u04e5\u04e6"+
		"\u0005w\u0000\u0000\u04e6\u04e8\u0005\b\u0000\u0000\u04e7\u04e9\u0003"+
		"\u00ecv\u0000\u04e8\u04e7\u0001\u0000\u0000\u0000\u04e8\u04e9\u0001\u0000"+
		"\u0000\u0000\u04e9\u04ea\u0001\u0000\u0000\u0000\u04ea\u04ed\u0005\t\u0000"+
		"\u0000\u04eb\u04ec\u0005\u001d\u0000\u0000\u04ec\u04ee\u0003h4\u0000\u04ed"+
		"\u04eb\u0001\u0000\u0000\u0000\u04ed\u04ee\u0001\u0000\u0000\u0000\u04ee"+
		"\u00ad\u0001\u0000\u0000\u0000\u04ef\u04f1\u0005x\u0000\u0000\u04f0\u04f2"+
		"\u0003h4\u0000\u04f1\u04f0\u0001\u0000\u0000\u0000\u04f1\u04f2\u0001\u0000"+
		"\u0000\u0000\u04f2\u00af\u0001\u0000\u0000\u0000\u04f3\u04f4\u0005y\u0000"+
		"\u0000\u04f4\u04f9\u0003\u00b2Y\u0000\u04f5\u04f6\u0005\u0005\u0000\u0000"+
		"\u04f6\u04f8\u0003\u00b2Y\u0000\u04f7\u04f5\u0001\u0000\u0000\u0000\u04f8"+
		"\u04fb\u0001\u0000\u0000\u0000\u04f9\u04f7\u0001\u0000\u0000\u0000\u04f9"+
		"\u04fa\u0001\u0000\u0000\u0000\u04fa\u00b1\u0001\u0000\u0000\u0000\u04fb"+
		"\u04f9\u0001\u0000\u0000\u0000\u04fc\u04ff\u0003\u00cae\u0000\u04fd\u04fe"+
		"\u0005\u000f\u0000\u0000\u04fe\u0500\u0003l6\u0000\u04ff\u04fd\u0001\u0000"+
		"\u0000\u0000\u04ff\u0500\u0001\u0000\u0000\u0000\u0500\u0504\u0001\u0000"+
		"\u0000\u0000\u0501\u0502\u0003\u00b4Z\u0000\u0502\u0503\u0003h4\u0000"+
		"\u0503\u0505\u0001\u0000\u0000\u0000\u0504\u0501\u0001\u0000\u0000\u0000"+
		"\u0504\u0505\u0001\u0000\u0000\u0000\u0505\u00b3\u0001\u0000\u0000\u0000"+
		"\u0506\u0507\u0007\u0015\u0000\u0000\u0507\u00b5\u0001\u0000\u0000\u0000"+
		"\u0508\u050d\u0003\u00cae\u0000\u0509\u050a\u0005/\u0000\u0000\u050a\u050c"+
		"\u0003\u00cae\u0000\u050b\u0509\u0001\u0000\u0000\u0000\u050c\u050f\u0001"+
		"\u0000\u0000\u0000\u050d\u050b\u0001\u0000\u0000\u0000\u050d\u050e\u0001"+
		"\u0000\u0000\u0000\u050e\u00b7\u0001\u0000\u0000\u0000\u050f\u050d\u0001"+
		"\u0000\u0000\u0000\u0510\u0511\u0003\u0092I\u0000\u0511\u0512\u0003\u00c4"+
		"b\u0000\u0512\u0514\u0005\b\u0000\u0000\u0513\u0515\u0003\u00ecv\u0000"+
		"\u0514\u0513\u0001\u0000\u0000\u0000\u0514\u0515\u0001\u0000\u0000\u0000"+
		"\u0515\u0516\u0001\u0000\u0000\u0000\u0516\u0517\u0005\t\u0000\u0000\u0517"+
		"\u053c\u0001\u0000\u0000\u0000\u0518\u051a\u0005i\u0000\u0000\u0519\u0518"+
		"\u0001\u0000\u0000\u0000\u0519\u051a\u0001\u0000\u0000\u0000\u051a\u051b"+
		"\u0001\u0000\u0000\u0000\u051b\u051c\u0003\u0096K\u0000\u051c\u051e\u0005"+
		"\b\u0000\u0000\u051d\u051f\u0003\u009cN\u0000\u051e\u051d\u0001\u0000"+
		"\u0000\u0000\u051e\u051f\u0001\u0000\u0000\u0000\u051f\u0520\u0001\u0000"+
		"\u0000\u0000\u0520\u0521\u0005\t\u0000\u0000\u0521\u053c\u0001\u0000\u0000"+
		"\u0000\u0522\u0524\u0005i\u0000\u0000\u0523\u0522\u0001\u0000\u0000\u0000"+
		"\u0523\u0524\u0001\u0000\u0000\u0000\u0524\u0525\u0001\u0000\u0000\u0000"+
		"\u0525\u0526\u0003\u009aM\u0000\u0526\u0527\u0005\b\u0000\u0000\u0527"+
		"\u052a\u0003\u00c4b\u0000\u0528\u0529\u0005\u0005\u0000\u0000\u0529\u052b"+
		"\u0003\u009cN\u0000\u052a\u0528\u0001\u0000\u0000\u0000\u052a\u052b\u0001"+
		"\u0000\u0000\u0000\u052b\u052c\u0001\u0000\u0000\u0000\u052c\u052d\u0005"+
		"\t\u0000\u0000\u052d\u053c\u0001\u0000\u0000\u0000\u052e\u052f\u0003\u00ca"+
		"e\u0000\u052f\u0531\u0005\b\u0000\u0000\u0530\u0532\u0003\u00ecv\u0000"+
		"\u0531\u0530\u0001\u0000\u0000\u0000\u0531\u0532\u0001\u0000\u0000\u0000"+
		"\u0532\u0533\u0001\u0000\u0000\u0000\u0533\u0535\u0005\t\u0000\u0000\u0534"+
		"\u0536\u0003\u00eew\u0000\u0535\u0534\u0001\u0000\u0000\u0000\u0535\u0536"+
		"\u0001\u0000\u0000\u0000\u0536\u053c\u0001\u0000\u0000\u0000\u0537\u0539"+
		"\u0003\u00cae\u0000\u0538\u053a\u0003\u00eew\u0000\u0539\u0538\u0001\u0000"+
		"\u0000\u0000\u0539\u053a\u0001\u0000\u0000\u0000\u053a\u053c\u0001\u0000"+
		"\u0000\u0000\u053b\u0510\u0001\u0000\u0000\u0000\u053b\u0519\u0001\u0000"+
		"\u0000\u0000\u053b\u0523\u0001\u0000\u0000\u0000\u053b\u052e\u0001\u0000"+
		"\u0000\u0000\u053b\u0537\u0001\u0000\u0000\u0000\u053c\u00b9\u0001\u0000"+
		"\u0000\u0000\u053d\u053e\u0003\u0082A\u0000\u053e\u053f\u0005\b\u0000"+
		"\u0000\u053f\u0542\u0003\u0084B\u0000\u0540\u0541\u00057\u0000\u0000\u0541"+
		"\u0543\u0003h4\u0000\u0542\u0540\u0001\u0000\u0000\u0000\u0542\u0543\u0001"+
		"\u0000\u0000\u0000\u0543\u0544\u0001\u0000\u0000\u0000\u0544\u0545\u0005"+
		"\t\u0000\u0000\u0545\u0546\u0003f3\u0000\u0546\u0588\u0001\u0000\u0000"+
		"\u0000\u0547\u0549\u0005i\u0000\u0000\u0548\u0547\u0001\u0000\u0000\u0000"+
		"\u0548\u0549\u0001\u0000\u0000\u0000\u0549\u054a\u0001\u0000\u0000\u0000"+
		"\u054a\u054b\u0003\u0096K\u0000\u054b\u054d\u0005\b\u0000\u0000\u054c"+
		"\u054e\u0003\u009cN\u0000\u054d\u054c\u0001\u0000\u0000\u0000\u054d\u054e"+
		"\u0001\u0000\u0000\u0000\u054e\u054f\u0001\u0000\u0000\u0000\u054f\u0550"+
		"\u0005\t\u0000\u0000\u0550\u0588\u0001\u0000\u0000\u0000\u0551\u0553\u0005"+
		"i\u0000\u0000\u0552\u0551\u0001\u0000\u0000\u0000\u0552\u0553\u0001\u0000"+
		"\u0000\u0000\u0553\u0554\u0001\u0000\u0000\u0000\u0554\u0555\u0003\u009a"+
		"M\u0000\u0555\u0556\u0005\b\u0000\u0000\u0556\u0559\u0003\u00c4b\u0000"+
		"\u0557\u0558\u0005\u0005\u0000\u0000\u0558\u055a\u0003\u009cN\u0000\u0559"+
		"\u0557\u0001\u0000\u0000\u0000\u0559\u055a\u0001\u0000\u0000\u0000\u055a"+
		"\u055b\u0001\u0000\u0000\u0000\u055b\u055c\u0005\t\u0000\u0000\u055c\u0588"+
		"\u0001\u0000\u0000\u0000\u055d\u055e\u0003\u0092I\u0000\u055e\u055f\u0003"+
		"\u00c4b\u0000\u055f\u0561\u0005\b\u0000\u0000\u0560\u0562\u0003\u00ec"+
		"v\u0000\u0561\u0560\u0001\u0000\u0000\u0000\u0561\u0562\u0001\u0000\u0000"+
		"\u0000\u0562\u0563\u0001\u0000\u0000\u0000\u0563\u0564\u0005\t\u0000\u0000"+
		"\u0564\u0588\u0001\u0000\u0000\u0000\u0565\u0566\u0003\u00cae\u0000\u0566"+
		"\u0567\u0005\b\u0000\u0000\u0567\u0568\u0003\u00bc^\u0000\u0568\u0569"+
		"\u00057\u0000\u0000\u0569\u056a\u0003h4\u0000\u056a\u056b\u0005\t\u0000"+
		"\u0000\u056b\u0588\u0001\u0000\u0000\u0000\u056c\u056d\u0003\u00cae\u0000"+
		"\u056d\u056e\u0005\b\u0000\u0000\u056e\u0571\u0003\u00cae\u0000\u056f"+
		"\u0570\u0005\u000f\u0000\u0000\u0570\u0572\u0003l6\u0000\u0571\u056f\u0001"+
		"\u0000\u0000\u0000\u0571\u0572\u0001\u0000\u0000\u0000\u0572\u0573\u0001"+
		"\u0000\u0000\u0000\u0573\u0574\u0005\u0002\u0000\u0000\u0574\u0577\u0003"+
		"\u00cae\u0000\u0575\u0576\u0005\u000f\u0000\u0000\u0576\u0578\u0003l6"+
		"\u0000\u0577\u0575\u0001\u0000\u0000\u0000\u0577\u0578\u0001\u0000\u0000"+
		"\u0000\u0578\u0579\u0001\u0000\u0000\u0000\u0579\u057a\u0005\"\u0000\u0000"+
		"\u057a\u057b\u0003h4\u0000\u057b\u057c\u00057\u0000\u0000\u057c\u057d"+
		"\u0003h4\u0000\u057d\u057e\u0005\t\u0000\u0000\u057e\u0588\u0001\u0000"+
		"\u0000\u0000\u057f\u0580\u0003\u00cae\u0000\u0580\u0582\u0005\b\u0000"+
		"\u0000\u0581\u0583\u0003\u00ecv\u0000\u0582\u0581\u0001\u0000\u0000\u0000"+
		"\u0582\u0583\u0001\u0000\u0000\u0000\u0583\u0584\u0001\u0000\u0000\u0000"+
		"\u0584\u0585\u0005\t\u0000\u0000\u0585\u0588\u0001\u0000\u0000\u0000\u0586"+
		"\u0588\u0003\u00cae\u0000\u0587\u053d\u0001\u0000\u0000\u0000\u0587\u0548"+
		"\u0001\u0000\u0000\u0000\u0587\u0552\u0001\u0000\u0000\u0000\u0587\u055d"+
		"\u0001\u0000\u0000\u0000\u0587\u0565\u0001\u0000\u0000\u0000\u0587\u056c"+
		"\u0001\u0000\u0000\u0000\u0587\u057f\u0001\u0000\u0000\u0000\u0587\u0586"+
		"\u0001\u0000\u0000\u0000\u0588\u00bb\u0001\u0000\u0000\u0000\u0589\u058c"+
		"\u0003\u00cae\u0000\u058a\u058b\u0005\u000f\u0000\u0000\u058b\u058d\u0003"+
		"l6\u0000\u058c\u058a\u0001\u0000\u0000\u0000\u058c\u058d\u0001\u0000\u0000"+
		"\u0000\u058d\u0596\u0001\u0000\u0000\u0000\u058e\u058f\u0005\u0005\u0000"+
		"\u0000\u058f\u0592\u0003\u00cae\u0000\u0590\u0591\u0005\u000f\u0000\u0000"+
		"\u0591\u0593\u0003l6\u0000\u0592\u0590\u0001\u0000\u0000\u0000\u0592\u0593"+
		"\u0001\u0000\u0000\u0000\u0593\u0595\u0001\u0000\u0000\u0000\u0594\u058e"+
		"\u0001\u0000\u0000\u0000\u0595\u0598\u0001\u0000\u0000\u0000\u0596\u0594"+
		"\u0001\u0000\u0000\u0000\u0596\u0597\u0001\u0000\u0000\u0000\u0597\u00bd"+
		"\u0001\u0000\u0000\u0000\u0598\u0596\u0001\u0000\u0000\u0000\u0599\u059c"+
		"\u0003\u00cae\u0000\u059a\u059b\u0005\u000f\u0000\u0000\u059b\u059d\u0003"+
		"l6\u0000\u059c\u059a\u0001\u0000\u0000\u0000\u059c\u059d\u0001\u0000\u0000"+
		"\u0000\u059d\u059e\u0001\u0000\u0000\u0000\u059e\u059f\u0005\"\u0000\u0000"+
		"\u059f\u05a0\u0003h4\u0000\u05a0\u00bf\u0001\u0000\u0000\u0000\u05a1\u05a2"+
		"\u0003\u00cae\u0000\u05a2\u05a3\u0005\u000f\u0000\u0000\u05a3\u05a4\u0003"+
		"l6\u0000\u05a4\u00c1\u0001\u0000\u0000\u0000\u05a5\u05a8\u0003\u00cae"+
		"\u0000\u05a6\u05a7\u0005\u000f\u0000\u0000\u05a7\u05a9\u0003l6\u0000\u05a8"+
		"\u05a6\u0001\u0000\u0000\u0000\u05a8\u05a9\u0001\u0000\u0000\u0000\u05a9"+
		"\u05aa\u0001\u0000\u0000\u0000\u05aa\u05ab\u0005\"\u0000\u0000\u05ab\u05ac"+
		"\u0003h4\u0000\u05ac\u00c3\u0001\u0000\u0000\u0000\u05ad\u05ae\u0003\u00c8"+
		"d\u0000\u05ae\u05af\u0005/\u0000\u0000\u05af\u05b0\u0003\u00cae\u0000"+
		"\u05b0\u05bb\u0001\u0000\u0000\u0000\u05b1\u05b2\u0003\u00fa}\u0000\u05b2"+
		"\u05b3\u0005/\u0000\u0000\u05b3\u05b4\u0003\u00cae\u0000\u05b4\u05bb\u0001"+
		"\u0000\u0000\u0000\u05b5\u05b6\u0003\u00fc~\u0000\u05b6\u05b7\u0005/\u0000"+
		"\u0000\u05b7\u05b8\u0003\u00cae\u0000\u05b8\u05bb\u0001\u0000\u0000\u0000"+
		"\u05b9\u05bb\u0003\u00cae\u0000\u05ba\u05ad\u0001\u0000\u0000\u0000\u05ba"+
		"\u05b1\u0001\u0000\u0000\u0000\u05ba\u05b5\u0001\u0000\u0000\u0000\u05ba"+
		"\u05b9\u0001\u0000\u0000\u0000\u05bb\u00c5\u0001\u0000\u0000\u0000\u05bc"+
		"\u05c1\u0003\u00c4b\u0000\u05bd\u05be\u0005\u0005\u0000\u0000\u05be\u05c0"+
		"\u0003\u00c4b\u0000\u05bf\u05bd\u0001\u0000\u0000\u0000\u05c0\u05c3\u0001"+
		"\u0000\u0000\u0000\u05c1\u05bf\u0001\u0000\u0000\u0000\u05c1\u05c2\u0001"+
		"\u0000\u0000\u0000\u05c2\u00c7\u0001\u0000\u0000\u0000\u05c3\u05c1\u0001"+
		"\u0000\u0000\u0000\u05c4\u05c9\u0003\u00cae\u0000\u05c5\u05c6\u00052\u0000"+
		"\u0000\u05c6\u05c8\u0003\u00cae\u0000\u05c7\u05c5\u0001\u0000\u0000\u0000"+
		"\u05c8\u05cb\u0001\u0000\u0000\u0000\u05c9\u05c7\u0001\u0000\u0000\u0000"+
		"\u05c9\u05ca\u0001\u0000\u0000\u0000\u05ca\u00c9\u0001\u0000\u0000\u0000"+
		"\u05cb\u05c9\u0001\u0000\u0000\u0000\u05cc\u05cd\u0007\u0016\u0000\u0000"+
		"\u05cd\u00cb\u0001\u0000\u0000\u0000\u05ce\u05cf\u0003h4\u0000\u05cf\u05d0"+
		"\u0005\u0000\u0000\u0001\u05d0\u00cd\u0001\u0000\u0000\u0000\u05d1\u05d2"+
		"\u0003\u00d0h\u0000\u05d2\u05d3\u0005\u0000\u0000\u0001\u05d3\u00cf\u0001"+
		"\u0000\u0000\u0000\u05d4\u05d6\u0003\u00d2i\u0000\u05d5\u05d4\u0001\u0000"+
		"\u0000\u0000\u05d6\u05d9\u0001\u0000\u0000\u0000\u05d7\u05d5\u0001\u0000"+
		"\u0000\u0000\u05d7\u05d8\u0001\u0000\u0000\u0000\u05d8\u05de\u0001\u0000"+
		"\u0000\u0000\u05d9\u05d7\u0001\u0000\u0000\u0000\u05da\u05dd\u0003\u00d4"+
		"j\u0000\u05db\u05dd\u0003\u00d6k\u0000\u05dc\u05da\u0001\u0000\u0000\u0000"+
		"\u05dc\u05db\u0001\u0000\u0000\u0000\u05dd\u05e0\u0001\u0000\u0000\u0000"+
		"\u05de\u05dc\u0001\u0000\u0000\u0000\u05de\u05df\u0001\u0000\u0000\u0000"+
		"\u05df\u00d1\u0001\u0000\u0000\u0000\u05e0\u05de\u0001\u0000\u0000\u0000"+
		"\u05e1\u05e4\u0007\u0017\u0000\u0000\u05e2\u05e3\u0005\u009d\u0000\u0000"+
		"\u05e3\u05e5\u0005\u000f\u0000\u0000\u05e4\u05e2\u0001\u0000\u0000\u0000"+
		"\u05e4\u05e5\u0001\u0000\u0000\u0000\u05e5\u05e6\u0001\u0000\u0000\u0000"+
		"\u05e6\u05e9\u0003\u00b6[\u0000\u05e7\u05e8\u0005/\u0000\u0000\u05e8\u05ea"+
		"\u0005\u0004\u0000\u0000\u05e9\u05e7\u0001\u0000\u0000\u0000\u05e9\u05ea"+
		"\u0001\u0000\u0000\u0000\u05ea\u00d3\u0001\u0000\u0000\u0000\u05eb\u05ec"+
		"\u0005}\u0000\u0000\u05ec\u05f0\u0003\u00b6[\u0000\u05ed\u05ef\u0003\u00d6"+
		"k\u0000\u05ee\u05ed\u0001\u0000\u0000\u0000\u05ef\u05f2\u0001\u0000\u0000"+
		"\u0000\u05f0\u05ee\u0001\u0000\u0000\u0000\u05f0\u05f1\u0001\u0000\u0000"+
		"\u0000\u05f1\u05f3\u0001\u0000\u0000\u0000\u05f2\u05f0\u0001\u0000\u0000"+
		"\u0000\u05f3\u05f4\u0005~\u0000\u0000\u05f4\u00d5\u0001\u0000\u0000\u0000"+
		"\u05f5\u05f9\u0003\u00d8l\u0000\u05f6\u05f9\u0003\u00e0p\u0000\u05f7\u05f9"+
		"\u0003\u00e4r\u0000\u05f8\u05f5\u0001\u0000\u0000\u0000\u05f8\u05f6\u0001"+
		"\u0000\u0000\u0000\u05f8\u05f7\u0001\u0000\u0000\u0000\u05f9\u00d7\u0001"+
		"\u0000\u0000\u0000\u05fa\u05fb\u0005\u007f\u0000\u0000\u05fb\u05fd\u0003"+
		"\u00b6[\u0000\u05fc\u05fe\u0003\u00dam\u0000\u05fd\u05fc\u0001\u0000\u0000"+
		"\u0000\u05fe\u05ff\u0001\u0000\u0000\u0000\u05ff\u05fd\u0001\u0000\u0000"+
		"\u0000\u05ff\u0600\u0001\u0000\u0000\u0000\u0600\u00d9\u0001\u0000\u0000"+
		"\u0000\u0601\u0604\u0003\u00dcn\u0000\u0602\u0604\u0003\u00deo\u0000\u0603"+
		"\u0601\u0001\u0000\u0000\u0000\u0603\u0602\u0001\u0000\u0000\u0000\u0604"+
		"\u00db\u0001\u0000\u0000\u0000\u0605\u060d\u0005z\u0000\u0000\u0606\u060b"+
		"\u0005\u009d\u0000\u0000\u0607\u0608\u0005\b\u0000\u0000\u0608\u0609\u0003"+
		"h4\u0000\u0609\u060a\u0005\t\u0000\u0000\u060a\u060c\u0001\u0000\u0000"+
		"\u0000\u060b\u0607\u0001\u0000\u0000\u0000\u060b\u060c\u0001\u0000\u0000"+
		"\u0000\u060c\u060e\u0001\u0000\u0000\u0000\u060d\u0606\u0001\u0000\u0000"+
		"\u0000\u060d\u060e\u0001\u0000\u0000\u0000\u060e\u060f\u0001\u0000\u0000"+
		"\u0000\u060f\u0610\u0005\u000f\u0000\u0000\u0610\u0611\u0003h4\u0000\u0611"+
		"\u00dd\u0001\u0000\u0000\u0000\u0612\u0614\u0005\u0018\u0000\u0000\u0613"+
		"\u0612\u0001\u0000\u0000\u0000\u0613\u0614\u0001\u0000\u0000\u0000\u0614"+
		"\u0615\u0001\u0000\u0000\u0000\u0615\u0617\u0005\u0080\u0000\u0000\u0616"+
		"\u0618\u0005\u009d\u0000\u0000\u0617\u0616\u0001\u0000\u0000\u0000\u0617"+
		"\u0618\u0001\u0000\u0000\u0000\u0618\u0619\u0001\u0000\u0000\u0000\u0619"+
		"\u062b\u0005\u000f\u0000\u0000\u061a\u061b\u0005\u009d\u0000\u0000\u061b"+
		"\u061c\u0005\u000f\u0000\u0000\u061c\u061d\u0003l6\u0000\u061d\u061e\u0005"+
		"\"\u0000\u0000\u061e\u061f\u0003h4\u0000\u061f\u062c\u0001\u0000\u0000"+
		"\u0000\u0620\u0621\u0005\u009d\u0000\u0000\u0621\u0623\u0005\b\u0000\u0000"+
		"\u0622\u0624\u0003\u00e8t\u0000\u0623\u0622\u0001\u0000\u0000\u0000\u0623"+
		"\u0624\u0001\u0000\u0000\u0000\u0624\u0625\u0001\u0000\u0000\u0000\u0625"+
		"\u0626\u0005\t\u0000\u0000\u0626\u0627\u0005\u000f\u0000\u0000\u0627\u0628"+
		"\u0003l6\u0000\u0628\u0629\u0005\"\u0000\u0000\u0629\u062a\u0003h4\u0000"+
		"\u062a\u062c\u0001\u0000\u0000\u0000\u062b\u061a\u0001\u0000\u0000\u0000"+
		"\u062b\u0620\u0001\u0000\u0000\u0000\u062c\u00df\u0001\u0000\u0000\u0000"+
		"\u062d\u062e\u0005\u007f\u0000\u0000\u062e\u062f\u0003\u00b6[\u0000\u062f"+
		"\u0630\u0005/\u0000\u0000\u0630\u0631\u0005\u009d\u0000\u0000\u0631\u0633"+
		"\u0005\b\u0000\u0000\u0632\u0634\u0003\u00e8t\u0000\u0633\u0632\u0001"+
		"\u0000\u0000\u0000\u0633\u0634\u0001\u0000\u0000\u0000\u0634\u0635\u0001"+
		"\u0000\u0000\u0000\u0635\u0636\u0005\t\u0000\u0000\u0636\u0637\u0005\u000f"+
		"\u0000\u0000\u0637\u0639\u0003l6\u0000\u0638\u063a\u0003\u00e2q\u0000"+
		"\u0639\u0638\u0001\u0000\u0000\u0000\u063a\u063b\u0001\u0000\u0000\u0000"+
		"\u063b\u0639\u0001\u0000\u0000\u0000\u063b\u063c\u0001\u0000\u0000\u0000"+
		"\u063c\u00e1\u0001\u0000\u0000\u0000\u063d\u063f\u0005\u0081\u0000\u0000"+
		"\u063e\u0640\u0005\u009d\u0000\u0000\u063f\u063e\u0001\u0000\u0000\u0000"+
		"\u063f\u0640\u0001\u0000\u0000\u0000\u0640\u0641\u0001\u0000\u0000\u0000"+
		"\u0641\u0642\u0005\u000f\u0000\u0000\u0642\u0650\u0003h4\u0000\u0643\u0645"+
		"\u0005\u0082\u0000\u0000\u0644\u0646\u0005\u009d\u0000\u0000\u0645\u0644"+
		"\u0001\u0000\u0000\u0000\u0645\u0646\u0001\u0000\u0000\u0000\u0646\u0647"+
		"\u0001\u0000\u0000\u0000\u0647\u0648\u0005\u000f\u0000\u0000\u0648\u0650"+
		"\u0003h4\u0000\u0649\u064b\u0005\u0083\u0000\u0000\u064a\u064c\u0005\u009d"+
		"\u0000\u0000\u064b\u064a\u0001\u0000\u0000\u0000\u064b\u064c\u0001\u0000"+
		"\u0000\u0000\u064c\u064d\u0001\u0000\u0000\u0000\u064d\u064e\u0005\u000f"+
		"\u0000\u0000\u064e\u0650\u0003h4\u0000\u064f\u063d\u0001\u0000\u0000\u0000"+
		"\u064f\u0643\u0001\u0000\u0000\u0000\u064f\u0649\u0001\u0000\u0000\u0000"+
		"\u0650\u00e3\u0001\u0000\u0000\u0000\u0651\u0652\u0005\u007f\u0000\u0000"+
		"\u0652\u0653\u0003\u00b6[\u0000\u0653\u0654\u0005/\u0000\u0000\u0654\u0655"+
		"\u0005\u009d\u0000\u0000\u0655\u0656\u0005\u000f\u0000\u0000\u0656\u0658"+
		"\u0003l6\u0000\u0657\u0659\u0003\u00e6s\u0000\u0658\u0657\u0001\u0000"+
		"\u0000\u0000\u0659\u065a\u0001\u0000\u0000\u0000\u065a\u0658\u0001\u0000"+
		"\u0000\u0000\u065a\u065b\u0001\u0000\u0000\u0000\u065b\u00e5\u0001\u0000"+
		"\u0000\u0000\u065c\u065e\u0005\u001e\u0000\u0000\u065d\u065f\u0005\u009d"+
		"\u0000\u0000\u065e\u065d\u0001\u0000\u0000\u0000\u065e\u065f\u0001\u0000"+
		"\u0000\u0000\u065f\u0660\u0001\u0000\u0000\u0000\u0660\u0661\u0005\u000f"+
		"\u0000\u0000\u0661\u0669\u0003h4\u0000\u0662\u0664\u0005\u0084\u0000\u0000"+
		"\u0663\u0665\u0005\u009d\u0000\u0000\u0664\u0663\u0001\u0000\u0000\u0000"+
		"\u0664\u0665\u0001\u0000\u0000\u0000\u0665\u0666\u0001\u0000\u0000\u0000"+
		"\u0666\u0667\u0005\u000f\u0000\u0000\u0667\u0669\u0003h4\u0000\u0668\u065c"+
		"\u0001\u0000\u0000\u0000\u0668\u0662\u0001\u0000\u0000\u0000\u0669\u00e7"+
		"\u0001\u0000\u0000\u0000\u066a\u066f\u0003\u00eau\u0000\u066b\u066c\u0005"+
		"\u0005\u0000\u0000\u066c\u066e\u0003\u00eau\u0000\u066d\u066b\u0001\u0000"+
		"\u0000\u0000\u066e\u0671\u0001\u0000\u0000\u0000\u066f\u066d\u0001\u0000"+
		"\u0000\u0000\u066f\u0670\u0001\u0000\u0000\u0000\u0670\u00e9\u0001\u0000"+
		"\u0000\u0000\u0671\u066f\u0001\u0000\u0000\u0000\u0672\u0673\u0005\u009d"+
		"\u0000\u0000\u0673\u0674\u0005\u000f\u0000\u0000\u0674\u0675\u0003l6\u0000"+
		"\u0675\u00eb\u0001\u0000\u0000\u0000\u0676\u067b\u0003h4\u0000\u0677\u0678"+
		"\u0005\u0005\u0000\u0000\u0678\u067a\u0003h4\u0000\u0679\u0677\u0001\u0000"+
		"\u0000\u0000\u067a\u067d\u0001\u0000\u0000\u0000\u067b\u0679\u0001\u0000"+
		"\u0000\u0000\u067b\u067c\u0001\u0000\u0000\u0000\u067c\u00ed\u0001\u0000"+
		"\u0000\u0000\u067d\u067b\u0001\u0000\u0000\u0000\u067e\u067f\u0005\u0013"+
		"\u0000\u0000\u067f\u0680\u0005\u0081\u0000\u0000\u0680\u00ef\u0001\u0000"+
		"\u0000\u0000\u0681\u0682\u0003r9\u0000\u0682\u068b\u0005\u000b\u0000\u0000"+
		"\u0683\u0688\u0003\u00f2y\u0000\u0684\u0685\u0005\u0005\u0000\u0000\u0685"+
		"\u0687\u0003\u00f2y\u0000\u0686\u0684\u0001\u0000\u0000\u0000\u0687\u068a"+
		"\u0001\u0000\u0000\u0000\u0688\u0686\u0001\u0000\u0000\u0000\u0688\u0689"+
		"\u0001\u0000\u0000\u0000\u0689\u068c\u0001\u0000\u0000\u0000\u068a\u0688"+
		"\u0001\u0000\u0000\u0000\u068b\u0683\u0001\u0000\u0000\u0000\u068b\u068c"+
		"\u0001\u0000\u0000\u0000\u068c\u068d\u0001\u0000\u0000\u0000\u068d\u068e"+
		"\u0005\f\u0000\u0000\u068e\u00f1\u0001\u0000\u0000\u0000\u068f\u0692\u0003"+
		"h4\u0000\u0690\u0691\u0005\u0085\u0000\u0000\u0691\u0693\u0003h4\u0000"+
		"\u0692\u0690\u0001\u0000\u0000\u0000\u0692\u0693\u0001\u0000\u0000\u0000"+
		"\u0693\u00f3\u0001\u0000\u0000\u0000\u0694\u0695\u0005\u0086\u0000\u0000"+
		"\u0695\u0696\u0005\u000b\u0000\u0000\u0696\u069b\u0003\u00c2a\u0000\u0697"+
		"\u0698\u0005\u0005\u0000\u0000\u0698\u069a\u0003\u00c2a\u0000\u0699\u0697"+
		"\u0001\u0000\u0000\u0000\u069a\u069d\u0001\u0000\u0000\u0000\u069b\u0699"+
		"\u0001\u0000\u0000\u0000\u069b\u069c\u0001\u0000\u0000\u0000\u069c\u069e"+
		"\u0001\u0000\u0000\u0000\u069d\u069b\u0001\u0000\u0000\u0000\u069e\u069f"+
		"\u0005\f\u0000\u0000\u069f\u00f5\u0001\u0000\u0000\u0000\u06a0\u06a1\u0005"+
		"\u0087\u0000\u0000\u06a1\u06aa\u0005\u000b\u0000\u0000\u06a2\u06a7\u0003"+
		"\u00f8|\u0000\u06a3\u06a4\u0005\u0005\u0000\u0000\u06a4\u06a6\u0003\u00f8"+
		"|\u0000\u06a5\u06a3\u0001\u0000\u0000\u0000\u06a6\u06a9\u0001\u0000\u0000"+
		"\u0000\u06a7\u06a5\u0001\u0000\u0000\u0000\u06a7\u06a8\u0001\u0000\u0000"+
		"\u0000\u06a8\u06ab\u0001\u0000\u0000\u0000\u06a9\u06a7\u0001\u0000\u0000"+
		"\u0000\u06aa\u06a2\u0001\u0000\u0000\u0000\u06aa\u06ab\u0001\u0000\u0000"+
		"\u0000\u06ab\u06ac\u0001\u0000\u0000\u0000\u06ac\u06ad\u0005\f\u0000\u0000"+
		"\u06ad\u00f7\u0001\u0000\u0000\u0000\u06ae\u06af\u0003h4\u0000\u06af\u06b0"+
		"\u0007\u0018\u0000\u0000\u06b0\u06b1\u0003h4\u0000\u06b1\u00f9\u0001\u0000"+
		"\u0000\u0000\u06b2\u06b3\u0007\u0019\u0000\u0000\u06b3\u00fb\u0001\u0000"+
		"\u0000\u0000\u06b4\u06b5\u0003r9\u0000\u06b5\u06b6\u0005\b\u0000\u0000"+
		"\u06b6\u06b7\u0003l6\u0000\u06b7\u06b8\u0005\t\u0000\u0000\u06b8\u00fd"+
		"\u0001\u0000\u0000\u0000\u06b9\u06ba\u0005\u0087\u0000\u0000\u06ba\u06bb"+
		"\u0005\b\u0000\u0000\u06bb\u06bc\u0003l6\u0000\u06bc\u06bd\u0005\u0005"+
		"\u0000\u0000\u06bd\u06be\u0003l6\u0000\u06be\u06bf\u0005\t\u0000\u0000"+
		"\u06bf\u00ff\u0001\u0000\u0000\u0000\u06c0\u06c1\u0005\u0086\u0000\u0000"+
		"\u06c1\u06c2\u0005\b\u0000\u0000\u06c2\u06c7\u0003\u00c0`\u0000\u06c3"+
		"\u06c4\u0005\u0005\u0000\u0000\u06c4\u06c6\u0003\u00c0`\u0000\u06c5\u06c3"+
		"\u0001\u0000\u0000\u0000\u06c6\u06c9\u0001\u0000\u0000\u0000\u06c7\u06c5"+
		"\u0001\u0000\u0000\u0000\u06c7\u06c8\u0001\u0000\u0000\u0000\u06c8\u06ca"+
		"\u0001\u0000\u0000\u0000\u06c9\u06c7\u0001\u0000\u0000\u0000\u06ca\u06cb"+
		"\u0005\t\u0000\u0000\u06cb\u0101\u0001\u0000\u0000\u0000\u00c4\u0108\u0110"+
		"\u011c\u0124\u0127\u012c\u0131\u013a\u0141\u0144\u014d\u0155\u015c\u0162"+
		"\u0169\u016e\u0171\u0176\u017b\u0182\u0187\u018e\u0199\u01a0\u01a4\u01af"+
		"\u01c1\u01c6\u01cb\u01d2\u01d6\u01d9\u01dd\u01e2\u01e7\u01ee\u01f3\u01f7"+
		"\u01fc\u0203\u020b\u0212\u0216\u022d\u0231\u0237\u023b\u023e\u024e\u0256"+
		"\u025e\u0263\u0271\u0278\u027e\u0282\u0286\u0288\u028c\u0297\u02a2\u02ac"+
		"\u02b6\u02bd\u02c3\u02c8\u02cf\u02d5\u02dd\u02e8\u02ef\u02f8\u02fd\u0306"+
		"\u030e\u0319\u033e\u0349\u0353\u0355\u0357\u036b\u0370\u0381\u0386\u0389"+
		"\u0391\u03ad\u03b6\u03bf\u03db\u03e0\u03e9\u03ec\u03f9\u0401\u0405\u0419"+
		"\u0420\u042f\u0436\u043d\u0443\u044a\u045f\u0462\u0466\u046e\u0472\u047a"+
		"\u0481\u0486\u048d\u0494\u049d\u04a4\u04ab\u04b2\u04b5\u04b8\u04c1\u04c8"+
		"\u04cb\u04ce\u04d2\u04d9\u04de\u04e3\u04e8\u04ed\u04f1\u04f9\u04ff\u0504"+
		"\u050d\u0514\u0519\u051e\u0523\u052a\u0531\u0535\u0539\u053b\u0542\u0548"+
		"\u054d\u0552\u0559\u0561\u0571\u0577\u0582\u0587\u058c\u0592\u0596\u059c"+
		"\u05a8\u05ba\u05c1\u05c9\u05d7\u05dc\u05de\u05e4\u05e9\u05f0\u05f8\u05ff"+
		"\u0603\u060b\u060d\u0613\u0617\u0623\u062b\u0633\u063b\u063f\u0645\u064b"+
		"\u064f\u065a\u065e\u0664\u0668\u066f\u067b\u0688\u068b\u0692\u069b\u06a7"+
		"\u06aa\u06c7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}