// Generated from /opt/git/m2m/workspace/org.eclipse.fennec.m2x.qvtd.parser/grammar/QvtR.g4 by ANTLR 4.13.2
package org.eclipse.fennec.m2x.qvtd.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class QvtRParser extends Parser {
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
		T__73=74, T__74=75, CHECKONLY=76, DOMAIN=77, ENFORCE=78, EXTENDS=79, IMPLEMENTEDBY=80, 
		IMPORT=81, KEY=82, OVERRIDES=83, PRIMITIVE=84, QUERY=85, RELATION=86, 
		TOP=87, TRANSFORMATION=88, WHEN=89, WHERE=90, DEFAULT_VALUES=91, ABSTRACT=92, 
		OPPOSITE=93, PLUSPLUS=94, COLONCOLON=95, UNDERSCORE=96, REAL_LITERAL=97, 
		INTEGER_LITERAL=98, STRING_LITERAL=99, ESCAPED_IDENTIFIER=100, IDENTIFIER=101, 
		WS=102, LINE_COMMENT=103, BLOCK_COMMENT=104;
	public static final int
		RULE_compilationUnitEntry = 0, RULE_compilationUnit = 1, RULE_importDecl = 2, 
		RULE_importTarget = 3, RULE_qualifiedName = 4, RULE_transformationDef = 5, 
		RULE_modelDecl = 6, RULE_metaModelId = 7, RULE_keyDecl = 8, RULE_keyProperty = 9, 
		RULE_relation = 10, RULE_varDeclaration = 11, RULE_domain = 12, RULE_primitiveTypeDomain = 13, 
		RULE_checkEnforceQualifier = 14, RULE_assignmentExp = 15, RULE_template = 16, 
		RULE_objectTemplate = 17, RULE_optionalMultiplicity = 18, RULE_propertyTemplateList = 19, 
		RULE_propertyTemplate = 20, RULE_collectionTemplate = 21, RULE_memberSelection = 22, 
		RULE_memberItem = 23, RULE_whenClause = 24, RULE_whereClause = 25, RULE_predicate = 26, 
		RULE_queryDef = 27, RULE_paramDecl = 28, RULE_identifier = 29, RULE_qvtrDocumentKeyword = 30, 
		RULE_softKeyword = 31, RULE_expressionEntry = 32, RULE_completeOclDocumentEntry = 33, 
		RULE_completeOclDocument = 34, RULE_importDeclaration = 35, RULE_packageDeclaration = 36, 
		RULE_contextDeclaration = 37, RULE_classifierContextDeclaration = 38, 
		RULE_classifierContextBody = 39, RULE_invariantConstraint = 40, RULE_definitionConstraint = 41, 
		RULE_operationContextDeclaration = 42, RULE_operationContextBody = 43, 
		RULE_propertyContextDeclaration = 44, RULE_propertyContextBody = 45, RULE_parameterList = 46, 
		RULE_parameter = 47, RULE_expression = 48, RULE_primaryExpression = 49, 
		RULE_propertyOrCallSuffix = 50, RULE_iteratorOrOperationCall = 51, RULE_iteratorVariables = 52, 
		RULE_argumentList = 53, RULE_letBinding = 54, RULE_isMarkedPre = 55, RULE_literalExpression = 56, 
		RULE_stringLiteral_ = 57, RULE_collectionLiteral = 58, RULE_collectionKind = 59, 
		RULE_collectionLiteralPart = 60, RULE_tupleLiteral = 61, RULE_tupleLiteralPart = 62, 
		RULE_mapLiteral = 63, RULE_mapLiteralPart = 64, RULE_typeExpression = 65, 
		RULE_primitiveType = 66, RULE_collectionType = 67, RULE_mapType = 68, 
		RULE_tupleType = 69, RULE_tupleTypePart = 70, RULE_pathName = 71, RULE_restrictedWord = 72, 
		RULE_documentKeyword = 73;
	private static String[] makeRuleNames() {
		return new String[] {
			"compilationUnitEntry", "compilationUnit", "importDecl", "importTarget", 
			"qualifiedName", "transformationDef", "modelDecl", "metaModelId", "keyDecl", 
			"keyProperty", "relation", "varDeclaration", "domain", "primitiveTypeDomain", 
			"checkEnforceQualifier", "assignmentExp", "template", "objectTemplate", 
			"optionalMultiplicity", "propertyTemplateList", "propertyTemplate", "collectionTemplate", 
			"memberSelection", "memberItem", "whenClause", "whereClause", "predicate", 
			"queryDef", "paramDecl", "identifier", "qvtrDocumentKeyword", "softKeyword", 
			"expressionEntry", "completeOclDocumentEntry", "completeOclDocument", 
			"importDeclaration", "packageDeclaration", "contextDeclaration", "classifierContextDeclaration", 
			"classifierContextBody", "invariantConstraint", "definitionConstraint", 
			"operationContextDeclaration", "operationContextBody", "propertyContextDeclaration", 
			"propertyContextBody", "parameterList", "parameter", "expression", "primaryExpression", 
			"propertyOrCallSuffix", "iteratorOrOperationCall", "iteratorVariables", 
			"argumentList", "letBinding", "isMarkedPre", "literalExpression", "stringLiteral_", 
			"collectionLiteral", "collectionKind", "collectionLiteralPart", "tupleLiteral", 
			"tupleLiteralPart", "mapLiteral", "mapLiteralPart", "typeExpression", 
			"primitiveType", "collectionType", "mapType", "tupleType", "tupleTypePart", 
			"pathName", "restrictedWord", "documentKeyword"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "':'", "'*'", "';'", "'.'", "'('", "','", "')'", "'{'", "'}'", 
			"'='", "'['", "'?'", "']'", "'body'", "'context'", "'def'", "'derive'", 
			"'endpackage'", "'init'", "'inv'", "'package'", "'post'", "'pre'", "'static'", 
			"'include'", "'library'", "'?.'", "'->'", "'?->'", "'not'", "'-'", "'/'", 
			"'+'", "'<'", "'>'", "'<='", "'>='", "'<>'", "'and'", "'or'", "'xor'", 
			"'implies'", "'self'", "'if'", "'then'", "'elseif'", "'else'", "'endif'", 
			"'let'", "'in'", "'|'", "'@'", "'true'", "'false'", "'null'", "'invalid'", 
			"'Set'", "'OrderedSet'", "'Bag'", "'Sequence'", "'Collection'", "'..'", 
			"'Tuple'", "'Map'", "'with'", "'<-'", "'Boolean'", "'Integer'", "'Real'", 
			"'String'", "'UnlimitedNatural'", "'OclAny'", "'OclVoid'", "'OclInvalid'", 
			"'OclMessage'", "'checkonly'", "'domain'", "'enforce'", "'extends'", 
			"'implementedby'", "'import'", "'key'", "'overrides'", "'primitive'", 
			"'query'", "'relation'", "'top'", "'transformation'", "'when'", "'where'", 
			"'default_values'", "'abstract'", "'opposite'", "'++'", "'::'", "'_'"
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
			null, null, null, null, "CHECKONLY", "DOMAIN", "ENFORCE", "EXTENDS", 
			"IMPLEMENTEDBY", "IMPORT", "KEY", "OVERRIDES", "PRIMITIVE", "QUERY", 
			"RELATION", "TOP", "TRANSFORMATION", "WHEN", "WHERE", "DEFAULT_VALUES", 
			"ABSTRACT", "OPPOSITE", "PLUSPLUS", "COLONCOLON", "UNDERSCORE", "REAL_LITERAL", 
			"INTEGER_LITERAL", "STRING_LITERAL", "ESCAPED_IDENTIFIER", "IDENTIFIER", 
			"WS", "LINE_COMMENT", "BLOCK_COMMENT"
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
	public String getGrammarFileName() { return "QvtR.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public QvtRParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompilationUnitEntryContext extends ParserRuleContext {
		public CompilationUnitContext compilationUnit() {
			return getRuleContext(CompilationUnitContext.class,0);
		}
		public TerminalNode EOF() { return getToken(QvtRParser.EOF, 0); }
		public CompilationUnitEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compilationUnitEntry; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCompilationUnitEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompilationUnitEntryContext compilationUnitEntry() throws RecognitionException {
		CompilationUnitEntryContext _localctx = new CompilationUnitEntryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_compilationUnitEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			compilationUnit();
			setState(149);
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
		public List<ImportDeclContext> importDecl() {
			return getRuleContexts(ImportDeclContext.class);
		}
		public ImportDeclContext importDecl(int i) {
			return getRuleContext(ImportDeclContext.class,i);
		}
		public List<TransformationDefContext> transformationDef() {
			return getRuleContexts(TransformationDefContext.class);
		}
		public TransformationDefContext transformationDef(int i) {
			return getRuleContext(TransformationDefContext.class,i);
		}
		public CompilationUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compilationUnit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCompilationUnit(this);
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
			setState(154);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(151);
				importDecl();
				}
				}
				setState(156);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(160);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TRANSFORMATION) {
				{
				{
				setState(157);
				transformationDef();
				}
				}
				setState(162);
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
	public static class ImportDeclContext extends ParserRuleContext {
		public IdentifierContext alias;
		public Token wildcard;
		public TerminalNode IMPORT() { return getToken(QvtRParser.IMPORT, 0); }
		public ImportTargetContext importTarget() {
			return getRuleContext(ImportTargetContext.class,0);
		}
		public List<TerminalNode> COLONCOLON() { return getTokens(QvtRParser.COLONCOLON); }
		public TerminalNode COLONCOLON(int i) {
			return getToken(QvtRParser.COLONCOLON, i);
		}
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public ImportDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitImportDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportDeclContext importDecl() throws RecognitionException {
		ImportDeclContext _localctx = new ImportDeclContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_importDecl);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			match(IMPORT);
			setState(167);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(164);
				((ImportDeclContext)_localctx).alias = identifier();
				setState(165);
				match(T__0);
				}
				break;
			}
			setState(169);
			importTarget();
			setState(174);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(170);
					match(COLONCOLON);
					setState(171);
					identifier();
					}
					} 
				}
				setState(176);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(179);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLONCOLON) {
				{
				setState(177);
				((ImportDeclContext)_localctx).wildcard = match(COLONCOLON);
				setState(178);
				match(T__1);
				}
			}

			setState(181);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ImportTargetContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL() { return getToken(QvtRParser.STRING_LITERAL, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public ImportTargetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importTarget; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitImportTarget(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportTargetContext importTarget() throws RecognitionException {
		ImportTargetContext _localctx = new ImportTargetContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_importTarget);
		try {
			setState(185);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(183);
				match(STRING_LITERAL);
				}
				break;
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case T__18:
			case T__19:
			case T__20:
			case T__21:
			case T__22:
			case T__23:
			case T__24:
			case T__25:
			case DEFAULT_VALUES:
			case ABSTRACT:
			case OPPOSITE:
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(184);
				qualifiedName();
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
	public static class QualifiedNameContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitQualifiedName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187);
			identifier();
			setState(192);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(188);
				match(T__3);
				setState(189);
				identifier();
				}
				}
				setState(194);
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
	public static class TransformationDefContext extends ParserRuleContext {
		public PathNameContext name;
		public PathNameContext base;
		public TerminalNode TRANSFORMATION() { return getToken(QvtRParser.TRANSFORMATION, 0); }
		public List<ModelDeclContext> modelDecl() {
			return getRuleContexts(ModelDeclContext.class);
		}
		public ModelDeclContext modelDecl(int i) {
			return getRuleContext(ModelDeclContext.class,i);
		}
		public List<PathNameContext> pathName() {
			return getRuleContexts(PathNameContext.class);
		}
		public PathNameContext pathName(int i) {
			return getRuleContext(PathNameContext.class,i);
		}
		public TerminalNode EXTENDS() { return getToken(QvtRParser.EXTENDS, 0); }
		public List<KeyDeclContext> keyDecl() {
			return getRuleContexts(KeyDeclContext.class);
		}
		public KeyDeclContext keyDecl(int i) {
			return getRuleContext(KeyDeclContext.class,i);
		}
		public List<RelationContext> relation() {
			return getRuleContexts(RelationContext.class);
		}
		public RelationContext relation(int i) {
			return getRuleContext(RelationContext.class,i);
		}
		public List<QueryDefContext> queryDef() {
			return getRuleContexts(QueryDefContext.class);
		}
		public QueryDefContext queryDef(int i) {
			return getRuleContext(QueryDefContext.class,i);
		}
		public TransformationDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transformationDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitTransformationDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TransformationDefContext transformationDef() throws RecognitionException {
		TransformationDefContext _localctx = new TransformationDefContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_transformationDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(TRANSFORMATION);
			setState(196);
			((TransformationDefContext)_localctx).name = pathName();
			setState(197);
			match(T__4);
			setState(198);
			modelDecl();
			setState(203);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(199);
				match(T__5);
				setState(200);
				modelDecl();
				}
				}
				setState(205);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(206);
			match(T__6);
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(207);
				match(EXTENDS);
				setState(208);
				((TransformationDefContext)_localctx).base = pathName();
				}
			}

			setState(211);
			match(T__7);
			setState(215);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==KEY) {
				{
				{
				setState(212);
				keyDecl();
				}
				}
				setState(217);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(222);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 85)) & ~0x3f) == 0 && ((1L << (_la - 85)) & 135L) != 0)) {
				{
				setState(220);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case RELATION:
				case TOP:
				case ABSTRACT:
					{
					setState(218);
					relation();
					}
					break;
				case QUERY:
					{
					setState(219);
					queryDef();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(224);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(225);
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
	public static class ModelDeclContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<MetaModelIdContext> metaModelId() {
			return getRuleContexts(MetaModelIdContext.class);
		}
		public MetaModelIdContext metaModelId(int i) {
			return getRuleContext(MetaModelIdContext.class,i);
		}
		public ModelDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modelDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitModelDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModelDeclContext modelDecl() throws RecognitionException {
		ModelDeclContext _localctx = new ModelDeclContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_modelDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			identifier();
			setState(228);
			match(T__0);
			setState(241);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case T__18:
			case T__19:
			case T__20:
			case T__21:
			case T__22:
			case T__23:
			case T__24:
			case T__25:
			case DEFAULT_VALUES:
			case ABSTRACT:
			case OPPOSITE:
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				{
				setState(229);
				metaModelId();
				}
				break;
			case T__7:
				{
				setState(230);
				match(T__7);
				setState(231);
				metaModelId();
				setState(236);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__5) {
					{
					{
					setState(232);
					match(T__5);
					setState(233);
					metaModelId();
					}
					}
					setState(238);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(239);
				match(T__8);
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
	public static class MetaModelIdContext extends ParserRuleContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public MetaModelIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metaModelId; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitMetaModelId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetaModelIdContext metaModelId() throws RecognitionException {
		MetaModelIdContext _localctx = new MetaModelIdContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_metaModelId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			pathName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KeyDeclContext extends ParserRuleContext {
		public TerminalNode KEY() { return getToken(QvtRParser.KEY, 0); }
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public List<KeyPropertyContext> keyProperty() {
			return getRuleContexts(KeyPropertyContext.class);
		}
		public KeyPropertyContext keyProperty(int i) {
			return getRuleContext(KeyPropertyContext.class,i);
		}
		public KeyDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitKeyDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyDeclContext keyDecl() throws RecognitionException {
		KeyDeclContext _localctx = new KeyDeclContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_keyDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			match(KEY);
			setState(246);
			pathName();
			setState(247);
			match(T__7);
			setState(248);
			keyProperty();
			setState(253);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(249);
				match(T__5);
				setState(250);
				keyProperty();
				}
				}
				setState(255);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(256);
			match(T__8);
			setState(257);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KeyPropertyContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode OPPOSITE() { return getToken(QvtRParser.OPPOSITE, 0); }
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode COLONCOLON() { return getToken(QvtRParser.COLONCOLON, 0); }
		public KeyPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyProperty; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitKeyProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyPropertyContext keyProperty() throws RecognitionException {
		KeyPropertyContext _localctx = new KeyPropertyContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_keyProperty);
		try {
			setState(267);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(259);
				identifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(260);
				match(OPPOSITE);
				setState(261);
				match(T__4);
				setState(262);
				pathName();
				setState(263);
				match(COLONCOLON);
				setState(264);
				identifier();
				setState(265);
				match(T__6);
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
	public static class RelationContext extends ParserRuleContext {
		public TerminalNode RELATION() { return getToken(QvtRParser.RELATION, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode TOP() { return getToken(QvtRParser.TOP, 0); }
		public TerminalNode ABSTRACT() { return getToken(QvtRParser.ABSTRACT, 0); }
		public TerminalNode OVERRIDES() { return getToken(QvtRParser.OVERRIDES, 0); }
		public List<VarDeclarationContext> varDeclaration() {
			return getRuleContexts(VarDeclarationContext.class);
		}
		public VarDeclarationContext varDeclaration(int i) {
			return getRuleContext(VarDeclarationContext.class,i);
		}
		public List<DomainContext> domain() {
			return getRuleContexts(DomainContext.class);
		}
		public DomainContext domain(int i) {
			return getRuleContext(DomainContext.class,i);
		}
		public List<PrimitiveTypeDomainContext> primitiveTypeDomain() {
			return getRuleContexts(PrimitiveTypeDomainContext.class);
		}
		public PrimitiveTypeDomainContext primitiveTypeDomain(int i) {
			return getRuleContext(PrimitiveTypeDomainContext.class,i);
		}
		public WhenClauseContext whenClause() {
			return getRuleContext(WhenClauseContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public RelationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relation; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitRelation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationContext relation() throws RecognitionException {
		RelationContext _localctx = new RelationContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_relation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(270);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TOP) {
				{
				setState(269);
				match(TOP);
				}
			}

			setState(273);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ABSTRACT) {
				{
				setState(272);
				match(ABSTRACT);
				}
			}

			setState(275);
			match(RELATION);
			setState(276);
			identifier();
			setState(279);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OVERRIDES) {
				{
				setState(277);
				match(OVERRIDES);
				setState(278);
				identifier();
				}
			}

			setState(281);
			match(T__7);
			setState(285);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
				{
				{
				setState(282);
				varDeclaration();
				}
				}
				setState(287);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(290); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(290);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case CHECKONLY:
				case DOMAIN:
				case ENFORCE:
					{
					setState(288);
					domain();
					}
					break;
				case PRIMITIVE:
					{
					setState(289);
					primitiveTypeDomain();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(292); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & 263L) != 0) );
			setState(295);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHEN) {
				{
				setState(294);
				whenClause();
				}
			}

			setState(298);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(297);
				whereClause();
				}
			}

			setState(300);
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
	public static class VarDeclarationContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VarDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitVarDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclarationContext varDeclaration() throws RecognitionException {
		VarDeclarationContext _localctx = new VarDeclarationContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_varDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			identifier();
			setState(307);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(303);
				match(T__5);
				setState(304);
				identifier();
				}
				}
				setState(309);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(310);
			match(T__0);
			setState(311);
			typeExpression();
			setState(314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(312);
				match(T__9);
				setState(313);
				expression(0);
				}
			}

			setState(316);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DomainContext extends ParserRuleContext {
		public TerminalNode DOMAIN() { return getToken(QvtRParser.DOMAIN, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<TemplateContext> template() {
			return getRuleContexts(TemplateContext.class);
		}
		public TemplateContext template(int i) {
			return getRuleContext(TemplateContext.class,i);
		}
		public CheckEnforceQualifierContext checkEnforceQualifier() {
			return getRuleContext(CheckEnforceQualifierContext.class,0);
		}
		public TerminalNode IMPLEMENTEDBY() { return getToken(QvtRParser.IMPLEMENTEDBY, 0); }
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode DEFAULT_VALUES() { return getToken(QvtRParser.DEFAULT_VALUES, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public List<AssignmentExpContext> assignmentExp() {
			return getRuleContexts(AssignmentExpContext.class);
		}
		public AssignmentExpContext assignmentExp(int i) {
			return getRuleContext(AssignmentExpContext.class,i);
		}
		public DomainContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_domain; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitDomain(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DomainContext domain() throws RecognitionException {
		DomainContext _localctx = new DomainContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_domain);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(319);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CHECKONLY || _la==ENFORCE) {
				{
				setState(318);
				checkEnforceQualifier();
				}
			}

			setState(321);
			match(DOMAIN);
			setState(322);
			identifier();
			setState(323);
			template();
			setState(328);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(324);
				match(T__5);
				setState(325);
				template();
				}
				}
				setState(330);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(339);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IMPLEMENTEDBY) {
				{
				setState(331);
				match(IMPLEMENTEDBY);
				setState(332);
				pathName();
				setState(333);
				match(T__4);
				setState(335);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214108L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 267227500537L) != 0)) {
					{
					setState(334);
					argumentList();
					}
				}

				setState(337);
				match(T__6);
				}
			}

			setState(350);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT_VALUES) {
				{
				setState(341);
				match(DEFAULT_VALUES);
				setState(342);
				match(T__7);
				setState(344); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(343);
					assignmentExp();
					}
					}
					setState(346); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0) );
				setState(348);
				match(T__8);
				}
			}

			setState(352);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveTypeDomainContext extends ParserRuleContext {
		public TerminalNode PRIMITIVE() { return getToken(QvtRParser.PRIMITIVE, 0); }
		public TerminalNode DOMAIN() { return getToken(QvtRParser.DOMAIN, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public PrimitiveTypeDomainContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveTypeDomain; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPrimitiveTypeDomain(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveTypeDomainContext primitiveTypeDomain() throws RecognitionException {
		PrimitiveTypeDomainContext _localctx = new PrimitiveTypeDomainContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_primitiveTypeDomain);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(354);
			match(PRIMITIVE);
			setState(355);
			match(DOMAIN);
			setState(356);
			identifier();
			setState(357);
			match(T__0);
			setState(358);
			typeExpression();
			setState(359);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CheckEnforceQualifierContext extends ParserRuleContext {
		public TerminalNode CHECKONLY() { return getToken(QvtRParser.CHECKONLY, 0); }
		public TerminalNode ENFORCE() { return getToken(QvtRParser.ENFORCE, 0); }
		public CheckEnforceQualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_checkEnforceQualifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCheckEnforceQualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CheckEnforceQualifierContext checkEnforceQualifier() throws RecognitionException {
		CheckEnforceQualifierContext _localctx = new CheckEnforceQualifierContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_checkEnforceQualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(361);
			_la = _input.LA(1);
			if ( !(_la==CHECKONLY || _la==ENFORCE) ) {
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
	public static class AssignmentExpContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AssignmentExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitAssignmentExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentExpContext assignmentExp() throws RecognitionException {
		AssignmentExpContext _localctx = new AssignmentExpContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_assignmentExp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363);
			identifier();
			setState(364);
			match(T__9);
			setState(365);
			expression(0);
			setState(366);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateContext extends ParserRuleContext {
		public ObjectTemplateContext objectTemplate() {
			return getRuleContext(ObjectTemplateContext.class,0);
		}
		public CollectionTemplateContext collectionTemplate() {
			return getRuleContext(CollectionTemplateContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TemplateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_template; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitTemplate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplateContext template() throws RecognitionException {
		TemplateContext _localctx = new TemplateContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_template);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(370);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(368);
				objectTemplate();
				}
				break;
			case 2:
				{
				setState(369);
				collectionTemplate();
				}
				break;
			}
			setState(376);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(372);
				match(T__7);
				setState(373);
				expression(0);
				setState(374);
				match(T__8);
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
	public static class ObjectTemplateContext extends ParserRuleContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public OptionalMultiplicityContext optionalMultiplicity() {
			return getRuleContext(OptionalMultiplicityContext.class,0);
		}
		public PropertyTemplateListContext propertyTemplateList() {
			return getRuleContext(PropertyTemplateListContext.class,0);
		}
		public ObjectTemplateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectTemplate; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitObjectTemplate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectTemplateContext objectTemplate() throws RecognitionException {
		ObjectTemplateContext _localctx = new ObjectTemplateContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_objectTemplate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(379);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
				{
				setState(378);
				identifier();
				}
			}

			setState(381);
			match(T__0);
			setState(382);
			pathName();
			setState(384);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(383);
				optionalMultiplicity();
				}
			}

			setState(386);
			match(T__7);
			setState(388);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
				{
				setState(387);
				propertyTemplateList();
				}
			}

			setState(390);
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
	public static class OptionalMultiplicityContext extends ParserRuleContext {
		public OptionalMultiplicityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_optionalMultiplicity; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitOptionalMultiplicity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OptionalMultiplicityContext optionalMultiplicity() throws RecognitionException {
		OptionalMultiplicityContext _localctx = new OptionalMultiplicityContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_optionalMultiplicity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(392);
			match(T__10);
			setState(393);
			match(T__11);
			setState(394);
			match(T__12);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyTemplateListContext extends ParserRuleContext {
		public List<PropertyTemplateContext> propertyTemplate() {
			return getRuleContexts(PropertyTemplateContext.class);
		}
		public PropertyTemplateContext propertyTemplate(int i) {
			return getRuleContext(PropertyTemplateContext.class,i);
		}
		public PropertyTemplateListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyTemplateList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPropertyTemplateList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyTemplateListContext propertyTemplateList() throws RecognitionException {
		PropertyTemplateListContext _localctx = new PropertyTemplateListContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_propertyTemplateList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(396);
			propertyTemplate();
			setState(401);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(397);
				match(T__5);
				setState(398);
				propertyTemplate();
				}
				}
				setState(403);
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
	public static class PropertyTemplateContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TemplateContext template() {
			return getRuleContext(TemplateContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode OPPOSITE() { return getToken(QvtRParser.OPPOSITE, 0); }
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode COLONCOLON() { return getToken(QvtRParser.COLONCOLON, 0); }
		public PropertyTemplateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyTemplate; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPropertyTemplate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyTemplateContext propertyTemplate() throws RecognitionException {
		PropertyTemplateContext _localctx = new PropertyTemplateContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_propertyTemplate);
		try {
			setState(430);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(404);
				identifier();
				setState(405);
				match(T__9);
				setState(406);
				template();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(408);
				identifier();
				setState(409);
				match(T__9);
				setState(410);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(412);
				match(OPPOSITE);
				setState(413);
				match(T__4);
				setState(414);
				pathName();
				setState(415);
				match(COLONCOLON);
				setState(416);
				identifier();
				setState(417);
				match(T__6);
				setState(418);
				match(T__9);
				setState(419);
				template();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(421);
				match(OPPOSITE);
				setState(422);
				match(T__4);
				setState(423);
				pathName();
				setState(424);
				match(COLONCOLON);
				setState(425);
				identifier();
				setState(426);
				match(T__6);
				setState(427);
				match(T__9);
				setState(428);
				expression(0);
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
	public static class CollectionTemplateContext extends ParserRuleContext {
		public CollectionKindContext collectionKind() {
			return getRuleContext(CollectionKindContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public MemberSelectionContext memberSelection() {
			return getRuleContext(MemberSelectionContext.class,0);
		}
		public CollectionTemplateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionTemplate; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCollectionTemplate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionTemplateContext collectionTemplate() throws RecognitionException {
		CollectionTemplateContext _localctx = new CollectionTemplateContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_collectionTemplate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(433);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
				{
				setState(432);
				identifier();
				}
			}

			setState(435);
			match(T__0);
			setState(436);
			collectionKind();
			setState(437);
			match(T__4);
			setState(438);
			typeExpression();
			setState(439);
			match(T__6);
			setState(440);
			match(T__7);
			setState(442);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201346L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1575L) != 0)) {
				{
				setState(441);
				memberSelection();
				}
			}

			setState(444);
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
	public static class MemberSelectionContext extends ParserRuleContext {
		public List<MemberItemContext> memberItem() {
			return getRuleContexts(MemberItemContext.class);
		}
		public MemberItemContext memberItem(int i) {
			return getRuleContext(MemberItemContext.class,i);
		}
		public TerminalNode PLUSPLUS() { return getToken(QvtRParser.PLUSPLUS, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode UNDERSCORE() { return getToken(QvtRParser.UNDERSCORE, 0); }
		public MemberSelectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberSelection; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitMemberSelection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberSelectionContext memberSelection() throws RecognitionException {
		MemberSelectionContext _localctx = new MemberSelectionContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_memberSelection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(446);
			memberItem();
			setState(451);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(447);
				match(T__5);
				setState(448);
				memberItem();
				}
				}
				setState(453);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(459);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUSPLUS) {
				{
				setState(454);
				match(PLUSPLUS);
				setState(457);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__13:
				case T__14:
				case T__15:
				case T__16:
				case T__17:
				case T__18:
				case T__19:
				case T__20:
				case T__21:
				case T__22:
				case T__23:
				case T__24:
				case T__25:
				case DEFAULT_VALUES:
				case ABSTRACT:
				case OPPOSITE:
				case ESCAPED_IDENTIFIER:
				case IDENTIFIER:
					{
					setState(455);
					identifier();
					}
					break;
				case UNDERSCORE:
					{
					setState(456);
					match(UNDERSCORE);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
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
	public static class MemberItemContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TemplateContext template() {
			return getRuleContext(TemplateContext.class,0);
		}
		public TerminalNode UNDERSCORE() { return getToken(QvtRParser.UNDERSCORE, 0); }
		public MemberItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberItem; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitMemberItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberItemContext memberItem() throws RecognitionException {
		MemberItemContext _localctx = new MemberItemContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_memberItem);
		try {
			setState(464);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(461);
				identifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(462);
				template();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(463);
				match(UNDERSCORE);
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
	public static class WhenClauseContext extends ParserRuleContext {
		public TerminalNode WHEN() { return getToken(QvtRParser.WHEN, 0); }
		public List<PredicateContext> predicate() {
			return getRuleContexts(PredicateContext.class);
		}
		public PredicateContext predicate(int i) {
			return getRuleContext(PredicateContext.class,i);
		}
		public WhenClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whenClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitWhenClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhenClauseContext whenClause() throws RecognitionException {
		WhenClauseContext _localctx = new WhenClauseContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_whenClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(466);
			match(WHEN);
			setState(467);
			match(T__7);
			setState(473);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214106L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 267227500537L) != 0)) {
				{
				{
				setState(468);
				predicate();
				setState(469);
				match(T__2);
				}
				}
				setState(475);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(476);
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
	public static class WhereClauseContext extends ParserRuleContext {
		public TerminalNode WHERE() { return getToken(QvtRParser.WHERE, 0); }
		public List<PredicateContext> predicate() {
			return getRuleContexts(PredicateContext.class);
		}
		public PredicateContext predicate(int i) {
			return getRuleContext(PredicateContext.class,i);
		}
		public WhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitWhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_whereClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(478);
			match(WHERE);
			setState(479);
			match(T__7);
			setState(485);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214106L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 267227500537L) != 0)) {
				{
				{
				setState(480);
				predicate();
				setState(481);
				match(T__2);
				}
				}
				setState(487);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(488);
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
	public static class PredicateContext extends ParserRuleContext {
		public TemplateContext template() {
			return getRuleContext(TemplateContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_predicate);
		try {
			setState(492);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(490);
				template();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(491);
				expression(0);
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
	public static class QueryDefContext extends ParserRuleContext {
		public TerminalNode QUERY() { return getToken(QvtRParser.QUERY, 0); }
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<ParamDeclContext> paramDecl() {
			return getRuleContexts(ParamDeclContext.class);
		}
		public ParamDeclContext paramDecl(int i) {
			return getRuleContext(ParamDeclContext.class,i);
		}
		public QueryDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitQueryDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryDefContext queryDef() throws RecognitionException {
		QueryDefContext _localctx = new QueryDefContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_queryDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(494);
			match(QUERY);
			setState(495);
			pathName();
			setState(496);
			match(T__4);
			setState(505);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
				{
				setState(497);
				paramDecl();
				setState(502);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__5) {
					{
					{
					setState(498);
					match(T__5);
					setState(499);
					paramDecl();
					}
					}
					setState(504);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(507);
			match(T__6);
			setState(508);
			match(T__0);
			setState(509);
			typeExpression();
			setState(515);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				{
				setState(510);
				match(T__2);
				}
				break;
			case T__7:
				{
				setState(511);
				match(T__7);
				setState(512);
				expression(0);
				setState(513);
				match(T__8);
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
	public static class ParamDeclContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public ParamDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitParamDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamDeclContext paramDecl() throws RecognitionException {
		ParamDeclContext _localctx = new ParamDeclContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_paramDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(517);
			identifier();
			setState(518);
			match(T__0);
			setState(519);
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
	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(QvtRParser.IDENTIFIER, 0); }
		public TerminalNode ESCAPED_IDENTIFIER() { return getToken(QvtRParser.ESCAPED_IDENTIFIER, 0); }
		public QvtrDocumentKeywordContext qvtrDocumentKeyword() {
			return getRuleContext(QvtrDocumentKeywordContext.class,0);
		}
		public SoftKeywordContext softKeyword() {
			return getRuleContext(SoftKeywordContext.class,0);
		}
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_identifier);
		try {
			setState(525);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(521);
				match(IDENTIFIER);
				}
				break;
			case ESCAPED_IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(522);
				match(ESCAPED_IDENTIFIER);
				}
				break;
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case T__18:
			case T__19:
			case T__20:
			case T__21:
			case T__22:
			case T__23:
			case T__24:
			case T__25:
				enterOuterAlt(_localctx, 3);
				{
				setState(523);
				qvtrDocumentKeyword();
				}
				break;
			case DEFAULT_VALUES:
			case ABSTRACT:
			case OPPOSITE:
				enterOuterAlt(_localctx, 4);
				{
				setState(524);
				softKeyword();
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
	public static class QvtrDocumentKeywordContext extends ParserRuleContext {
		public QvtrDocumentKeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qvtrDocumentKeyword; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitQvtrDocumentKeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QvtrDocumentKeywordContext qvtrDocumentKeyword() throws RecognitionException {
		QvtrDocumentKeywordContext _localctx = new QvtrDocumentKeywordContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_qvtrDocumentKeyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(527);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0)) ) {
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
	public static class SoftKeywordContext extends ParserRuleContext {
		public TerminalNode ABSTRACT() { return getToken(QvtRParser.ABSTRACT, 0); }
		public TerminalNode OPPOSITE() { return getToken(QvtRParser.OPPOSITE, 0); }
		public TerminalNode DEFAULT_VALUES() { return getToken(QvtRParser.DEFAULT_VALUES, 0); }
		public SoftKeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_softKeyword; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitSoftKeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SoftKeywordContext softKeyword() throws RecognitionException {
		SoftKeywordContext _localctx = new SoftKeywordContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_softKeyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(529);
			_la = _input.LA(1);
			if ( !(((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 7L) != 0)) ) {
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
		public TerminalNode EOF() { return getToken(QvtRParser.EOF, 0); }
		public ExpressionEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionEntry; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitExpressionEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionEntryContext expressionEntry() throws RecognitionException {
		ExpressionEntryContext _localctx = new ExpressionEntryContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_expressionEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(531);
			expression(0);
			setState(532);
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
		public TerminalNode EOF() { return getToken(QvtRParser.EOF, 0); }
		public CompleteOclDocumentEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_completeOclDocumentEntry; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCompleteOclDocumentEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompleteOclDocumentEntryContext completeOclDocumentEntry() throws RecognitionException {
		CompleteOclDocumentEntryContext _localctx = new CompleteOclDocumentEntryContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_completeOclDocumentEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(534);
			completeOclDocument();
			setState(535);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCompleteOclDocument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompleteOclDocumentContext completeOclDocument() throws RecognitionException {
		CompleteOclDocumentContext _localctx = new CompleteOclDocumentContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_completeOclDocument);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(540);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 25)) & ~0x3f) == 0 && ((1L << (_la - 25)) & 72057594037927939L) != 0)) {
				{
				{
				setState(537);
				importDeclaration();
				}
				}
				setState(542);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(547);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14 || _la==T__20) {
				{
				setState(545);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__20:
					{
					setState(543);
					packageDeclaration();
					}
					break;
				case T__14:
					{
					setState(544);
					contextDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(549);
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
		public TerminalNode IMPORT() { return getToken(QvtRParser.IMPORT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode COLONCOLON() { return getToken(QvtRParser.COLONCOLON, 0); }
		public ImportDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitImportDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportDeclarationContext importDeclaration() throws RecognitionException {
		ImportDeclarationContext _localctx = new ImportDeclarationContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_importDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(550);
			_la = _input.LA(1);
			if ( !(((((_la - 25)) & ~0x3f) == 0 && ((1L << (_la - 25)) & 72057594037927939L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(554);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				{
				setState(551);
				identifier();
				setState(552);
				match(T__0);
				}
				break;
			}
			setState(556);
			pathName();
			setState(559);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLONCOLON) {
				{
				setState(557);
				match(COLONCOLON);
				setState(558);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPackageDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageDeclarationContext packageDeclaration() throws RecognitionException {
		PackageDeclarationContext _localctx = new PackageDeclarationContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_packageDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(561);
			match(T__20);
			setState(562);
			pathName();
			setState(566);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(563);
				contextDeclaration();
				}
				}
				setState(568);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(569);
			match(T__17);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitContextDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextDeclarationContext contextDeclaration() throws RecognitionException {
		ContextDeclarationContext _localctx = new ContextDeclarationContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_contextDeclaration);
		try {
			setState(574);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(571);
				classifierContextDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(572);
				operationContextDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(573);
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
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitClassifierContextDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassifierContextDeclarationContext classifierContextDeclaration() throws RecognitionException {
		ClassifierContextDeclarationContext _localctx = new ClassifierContextDeclarationContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_classifierContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(576);
			match(T__14);
			setState(580);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				{
				setState(577);
				identifier();
				setState(578);
				match(T__0);
				}
				break;
			}
			setState(582);
			pathName();
			setState(584); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(583);
				classifierContextBody();
				}
				}
				setState(586); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 17891328L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitClassifierContextBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassifierContextBodyContext classifierContextBody() throws RecognitionException {
		ClassifierContextBodyContext _localctx = new ClassifierContextBodyContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_classifierContextBody);
		try {
			setState(590);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__19:
				enterOuterAlt(_localctx, 1);
				{
				setState(588);
				invariantConstraint();
				}
				break;
			case T__15:
			case T__23:
				enterOuterAlt(_localctx, 2);
				{
				setState(589);
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
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public InvariantConstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_invariantConstraint; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitInvariantConstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InvariantConstraintContext invariantConstraint() throws RecognitionException {
		InvariantConstraintContext _localctx = new InvariantConstraintContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_invariantConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(592);
			match(T__19);
			setState(600);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
				{
				setState(593);
				identifier();
				setState(598);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(594);
					match(T__4);
					setState(595);
					expression(0);
					setState(596);
					match(T__6);
					}
				}

				}
			}

			setState(602);
			match(T__0);
			setState(603);
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
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitDefinitionConstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionConstraintContext definitionConstraint() throws RecognitionException {
		DefinitionConstraintContext _localctx = new DefinitionConstraintContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_definitionConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(606);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23) {
				{
				setState(605);
				match(T__23);
				}
			}

			setState(608);
			match(T__15);
			setState(610);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
				{
				setState(609);
				identifier();
				}
			}

			setState(612);
			match(T__0);
			setState(630);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
			case 1:
				{
				setState(613);
				identifier();
				setState(614);
				match(T__0);
				setState(615);
				typeExpression();
				setState(616);
				match(T__9);
				setState(617);
				expression(0);
				}
				break;
			case 2:
				{
				setState(619);
				identifier();
				setState(620);
				match(T__4);
				setState(622);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
					{
					setState(621);
					parameterList();
					}
				}

				setState(624);
				match(T__6);
				setState(625);
				match(T__0);
				setState(626);
				typeExpression();
				setState(627);
				match(T__9);
				setState(628);
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
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode COLONCOLON() { return getToken(QvtRParser.COLONCOLON, 0); }
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitOperationContextDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperationContextDeclarationContext operationContextDeclaration() throws RecognitionException {
		OperationContextDeclarationContext _localctx = new OperationContextDeclarationContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_operationContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(632);
			match(T__14);
			setState(636);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				{
				setState(633);
				pathName();
				setState(634);
				match(COLONCOLON);
				}
				break;
			}
			setState(638);
			identifier();
			setState(639);
			match(T__4);
			setState(641);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
				{
				setState(640);
				parameterList();
				}
			}

			setState(643);
			match(T__6);
			setState(646);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(644);
				match(T__0);
				setState(645);
				typeExpression();
				}
			}

			setState(649); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(648);
				operationContextBody();
				}
				}
				setState(651); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 12599296L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public BodyExpressionContext(OperationContextBodyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitBodyExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PreConditionContext extends OperationContextBodyContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public PreConditionContext(OperationContextBodyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPreCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PostConditionContext extends OperationContextBodyContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public PostConditionContext(OperationContextBodyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPostCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperationContextBodyContext operationContextBody() throws RecognitionException {
		OperationContextBodyContext _localctx = new OperationContextBodyContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_operationContextBody);
		int _la;
		try {
			setState(671);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__22:
				_localctx = new PreConditionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(653);
				match(T__22);
				setState(655);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
					{
					setState(654);
					identifier();
					}
				}

				setState(657);
				match(T__0);
				setState(658);
				expression(0);
				}
				break;
			case T__21:
				_localctx = new PostConditionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(659);
				match(T__21);
				setState(661);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
					{
					setState(660);
					identifier();
					}
				}

				setState(663);
				match(T__0);
				setState(664);
				expression(0);
				}
				break;
			case T__13:
				_localctx = new BodyExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(665);
				match(T__13);
				setState(667);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
					{
					setState(666);
					identifier();
					}
				}

				setState(669);
				match(T__0);
				setState(670);
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
		public TerminalNode COLONCOLON() { return getToken(QvtRParser.COLONCOLON, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPropertyContextDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContextDeclarationContext propertyContextDeclaration() throws RecognitionException {
		PropertyContextDeclarationContext _localctx = new PropertyContextDeclarationContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_propertyContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(673);
			match(T__14);
			setState(674);
			pathName();
			setState(675);
			match(COLONCOLON);
			setState(676);
			identifier();
			setState(677);
			match(T__0);
			setState(678);
			typeExpression();
			setState(680); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(679);
				propertyContextBody();
				}
				}
				setState(682); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__16 || _la==T__18 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DeriveExpressionContext(PropertyContextBodyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitDeriveExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InitExpressionContext extends PropertyContextBodyContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public InitExpressionContext(PropertyContextBodyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitInitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContextBodyContext propertyContextBody() throws RecognitionException {
		PropertyContextBodyContext _localctx = new PropertyContextBodyContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_propertyContextBody);
		int _la;
		try {
			setState(696);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__18:
				_localctx = new InitExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(684);
				match(T__18);
				setState(686);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
					{
					setState(685);
					identifier();
					}
				}

				setState(688);
				match(T__0);
				setState(689);
				expression(0);
				}
				break;
			case T__16:
				_localctx = new DeriveExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(690);
				match(T__16);
				setState(692);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 1543L) != 0)) {
					{
					setState(691);
					identifier();
					}
				}

				setState(694);
				match(T__0);
				setState(695);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterListContext parameterList() throws RecognitionException {
		ParameterListContext _localctx = new ParameterListContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(698);
			parameter();
			setState(703);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(699);
				match(T__5);
				setState(700);
				parameter();
				}
				}
				setState(705);
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
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_parameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(706);
			identifier();
			setState(707);
			match(T__0);
			setState(708);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitNotExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitAndExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCompareExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitMultExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitAddExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitNavigationExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitImpliesExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPrimaryExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitUnaryMinusExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitEqualityExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitOrExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitXorExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitArrowExp(this);
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
		int _startState = 96;
		enterRecursionRule(_localctx, 96, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(716);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__29:
				{
				_localctx = new NotExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(711);
				match(T__29);
				setState(712);
				expression(11);
				}
				break;
			case T__30:
				{
				_localctx = new UnaryMinusExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(713);
				match(T__30);
				setState(714);
				expression(10);
				}
				break;
			case T__1:
			case T__4:
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case T__18:
			case T__19:
			case T__20:
			case T__21:
			case T__22:
			case T__23:
			case T__24:
			case T__25:
			case T__42:
			case T__43:
			case T__48:
			case T__52:
			case T__53:
			case T__54:
			case T__55:
			case T__56:
			case T__57:
			case T__58:
			case T__59:
			case T__60:
			case T__62:
			case T__63:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case T__70:
			case T__71:
			case T__72:
			case T__73:
			case T__74:
			case DEFAULT_VALUES:
			case ABSTRACT:
			case OPPOSITE:
			case REAL_LITERAL:
			case INTEGER_LITERAL:
			case STRING_LITERAL:
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				{
				_localctx = new PrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(715);
				primaryExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(750);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,83,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(748);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
					case 1:
						{
						_localctx = new MultExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(718);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(719);
						((MultExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__1 || _la==T__31) ) {
							((MultExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(720);
						expression(10);
						}
						break;
					case 2:
						{
						_localctx = new AddExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(721);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(722);
						((AddExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__30 || _la==T__32) ) {
							((AddExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(723);
						expression(9);
						}
						break;
					case 3:
						{
						_localctx = new CompareExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(724);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(725);
						((CompareExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 257698037760L) != 0)) ) {
							((CompareExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(726);
						expression(8);
						}
						break;
					case 4:
						{
						_localctx = new EqualityExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(727);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(728);
						((EqualityExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__9 || _la==T__37) ) {
							((EqualityExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(729);
						expression(7);
						}
						break;
					case 5:
						{
						_localctx = new AndExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(730);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(731);
						match(T__38);
						setState(732);
						expression(6);
						}
						break;
					case 6:
						{
						_localctx = new OrExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(733);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(734);
						match(T__39);
						setState(735);
						expression(5);
						}
						break;
					case 7:
						{
						_localctx = new XorExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(736);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(737);
						match(T__40);
						setState(738);
						expression(4);
						}
						break;
					case 8:
						{
						_localctx = new ImpliesExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(739);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(740);
						match(T__41);
						setState(741);
						expression(3);
						}
						break;
					case 9:
						{
						_localctx = new NavigationExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(742);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(743);
						_la = _input.LA(1);
						if ( !(_la==T__3 || _la==T__26) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(744);
						propertyOrCallSuffix();
						}
						break;
					case 10:
						{
						_localctx = new ArrowExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(745);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(746);
						_la = _input.LA(1);
						if ( !(_la==T__27 || _la==T__28) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(747);
						iteratorOrOperationCall();
						}
						break;
					}
					} 
				}
				setState(752);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,83,_ctx);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitLiteralExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitMapTypeExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SelfExpContext extends PrimaryExpressionContext {
		public SelfExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitSelfExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCollectionTypeExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitIfExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitParenExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPrimitiveTypeExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitLetExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OperationCallExpContext extends PrimaryExpressionContext {
		public IsMarkedPreContext preBefore;
		public IsMarkedPreContext preAfter;
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public List<IsMarkedPreContext> isMarkedPre() {
			return getRuleContexts(IsMarkedPreContext.class);
		}
		public IsMarkedPreContext isMarkedPre(int i) {
			return getRuleContext(IsMarkedPreContext.class,i);
		}
		public OperationCallExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitOperationCallExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitTupleTypeExp(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPathNameExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
		PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_primaryExpression);
		int _la;
		try {
			setState(806);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				_localctx = new ParenExpContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(753);
				match(T__4);
				setState(754);
				expression(0);
				setState(755);
				match(T__6);
				}
				break;
			case 2:
				_localctx = new SelfExpContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(757);
				match(T__42);
				}
				break;
			case 3:
				_localctx = new IfExpContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(758);
				match(T__43);
				setState(759);
				((IfExpContext)_localctx).condition = expression(0);
				setState(760);
				match(T__44);
				setState(761);
				((IfExpContext)_localctx).thenExp = expression(0);
				setState(769);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__45) {
					{
					{
					setState(762);
					match(T__45);
					setState(763);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfCondition.add(((IfExpContext)_localctx).expression);
					setState(764);
					match(T__44);
					setState(765);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfExp.add(((IfExpContext)_localctx).expression);
					}
					}
					setState(771);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(772);
				match(T__46);
				setState(773);
				((IfExpContext)_localctx).elseExp = expression(0);
				setState(774);
				match(T__47);
				}
				break;
			case 4:
				_localctx = new LetExpContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(776);
				match(T__48);
				setState(777);
				letBinding();
				setState(782);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__5) {
					{
					{
					setState(778);
					match(T__5);
					setState(779);
					letBinding();
					}
					}
					setState(784);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(785);
				match(T__49);
				setState(786);
				expression(0);
				}
				break;
			case 5:
				_localctx = new LiteralExpContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(788);
				literalExpression();
				}
				break;
			case 6:
				_localctx = new OperationCallExpContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(789);
				pathName();
				setState(791);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__51) {
					{
					setState(790);
					((OperationCallExpContext)_localctx).preBefore = isMarkedPre();
					}
				}

				setState(793);
				match(T__4);
				setState(795);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214108L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 267227500537L) != 0)) {
					{
					setState(794);
					argumentList();
					}
				}

				setState(797);
				match(T__6);
				setState(799);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
				case 1:
					{
					setState(798);
					((OperationCallExpContext)_localctx).preAfter = isMarkedPre();
					}
					break;
				}
				}
				break;
			case 7:
				_localctx = new PrimitiveTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(801);
				primitiveType();
				}
				break;
			case 8:
				_localctx = new CollectionTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(802);
				collectionType();
				}
				break;
			case 9:
				_localctx = new MapTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(803);
				mapType();
				}
				break;
			case 10:
				_localctx = new TupleTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(804);
				tupleType();
				}
				break;
			case 11:
				_localctx = new PathNameExpContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(805);
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
	public static class DotCallSuffixContext extends PropertyOrCallSuffixContext {
		public IsMarkedPreContext preBefore;
		public IsMarkedPreContext preAfter;
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode COLONCOLON() { return getToken(QvtRParser.COLONCOLON, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public List<IsMarkedPreContext> isMarkedPre() {
			return getRuleContexts(IsMarkedPreContext.class);
		}
		public IsMarkedPreContext isMarkedPre(int i) {
			return getRuleContext(IsMarkedPreContext.class,i);
		}
		public DotCallSuffixContext(PropertyOrCallSuffixContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitDotCallSuffix(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PropertySuffixContext extends PropertyOrCallSuffixContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode COLONCOLON() { return getToken(QvtRParser.COLONCOLON, 0); }
		public IsMarkedPreContext isMarkedPre() {
			return getRuleContext(IsMarkedPreContext.class,0);
		}
		public PropertySuffixContext(PropertyOrCallSuffixContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPropertySuffix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyOrCallSuffixContext propertyOrCallSuffix() throws RecognitionException {
		PropertyOrCallSuffixContext _localctx = new PropertyOrCallSuffixContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_propertyOrCallSuffix);
		int _la;
		try {
			setState(834);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,96,_ctx) ) {
			case 1:
				_localctx = new DotCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(811);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
				case 1:
					{
					setState(808);
					pathName();
					setState(809);
					match(COLONCOLON);
					}
					break;
				}
				setState(813);
				identifier();
				setState(815);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__51) {
					{
					setState(814);
					((DotCallSuffixContext)_localctx).preBefore = isMarkedPre();
					}
				}

				setState(817);
				match(T__4);
				setState(819);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214108L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 267227500537L) != 0)) {
					{
					setState(818);
					argumentList();
					}
				}

				setState(821);
				match(T__6);
				setState(823);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
				case 1:
					{
					setState(822);
					((DotCallSuffixContext)_localctx).preAfter = isMarkedPre();
					}
					break;
				}
				}
				break;
			case 2:
				_localctx = new PropertySuffixContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(828);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
				case 1:
					{
					setState(825);
					pathName();
					setState(826);
					match(COLONCOLON);
					}
					break;
				}
				setState(830);
				identifier();
				setState(832);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
				case 1:
					{
					setState(831);
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
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public CollectionOperationCallContext(IteratorOrOperationCallContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCollectionOperationCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IterateCallContext extends IteratorOrOperationCallContext {
		public IdentifierContext iterName;
		public TypeExpressionContext iterType;
		public IdentifierContext accName;
		public TypeExpressionContext accType;
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitIterateCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IteratorCallContext extends IteratorOrOperationCallContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitIteratorCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IteratorOrOperationCallContext iteratorOrOperationCall() throws RecognitionException {
		IteratorOrOperationCallContext _localctx = new IteratorOrOperationCallContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_iteratorOrOperationCall);
		int _la;
		try {
			setState(872);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
			case 1:
				_localctx = new IteratorCallContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(836);
				identifier();
				setState(837);
				match(T__4);
				setState(838);
				iteratorVariables();
				setState(839);
				match(T__50);
				setState(840);
				expression(0);
				setState(841);
				match(T__6);
				}
				break;
			case 2:
				_localctx = new IterateCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(843);
				identifier();
				setState(844);
				match(T__4);
				setState(852);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,98,_ctx) ) {
				case 1:
					{
					setState(845);
					((IterateCallContext)_localctx).iterName = identifier();
					setState(848);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==T__0) {
						{
						setState(846);
						match(T__0);
						setState(847);
						((IterateCallContext)_localctx).iterType = typeExpression();
						}
					}

					setState(850);
					match(T__2);
					}
					break;
				}
				setState(854);
				((IterateCallContext)_localctx).accName = identifier();
				setState(857);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(855);
					match(T__0);
					setState(856);
					((IterateCallContext)_localctx).accType = typeExpression();
					}
				}

				setState(859);
				match(T__9);
				setState(860);
				expression(0);
				setState(861);
				match(T__50);
				setState(862);
				expression(0);
				setState(863);
				match(T__6);
				}
				break;
			case 3:
				_localctx = new CollectionOperationCallContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(865);
				identifier();
				setState(866);
				match(T__4);
				setState(868);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214108L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 267227500537L) != 0)) {
					{
					setState(867);
					argumentList();
					}
				}

				setState(870);
				match(T__6);
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
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitIteratorVariables(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IteratorVariablesContext iteratorVariables() throws RecognitionException {
		IteratorVariablesContext _localctx = new IteratorVariablesContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_iteratorVariables);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(874);
			identifier();
			setState(877);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(875);
				match(T__0);
				setState(876);
				typeExpression();
				}
			}

			setState(887);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(879);
				match(T__5);
				setState(880);
				identifier();
				setState(883);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(881);
					match(T__0);
					setState(882);
					typeExpression();
					}
				}

				}
				}
				setState(889);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(890);
			expression(0);
			setState(895);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(891);
				match(T__5);
				setState(892);
				expression(0);
				}
				}
				setState(897);
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
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitLetBinding(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetBindingContext letBinding() throws RecognitionException {
		LetBindingContext _localctx = new LetBindingContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_letBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(898);
			identifier();
			setState(901);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(899);
				match(T__0);
				setState(900);
				typeExpression();
				}
			}

			setState(903);
			match(T__9);
			setState(904);
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
	public static class IsMarkedPreContext extends ParserRuleContext {
		public IsMarkedPreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isMarkedPre; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitIsMarkedPre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IsMarkedPreContext isMarkedPre() throws RecognitionException {
		IsMarkedPreContext _localctx = new IsMarkedPreContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_isMarkedPre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(906);
			match(T__51);
			setState(907);
			match(T__22);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode REAL_LITERAL() { return getToken(QvtRParser.REAL_LITERAL, 0); }
		public RealLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitRealLiteral(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCollectionLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TrueLiteralContext extends LiteralExpressionContext {
		public TrueLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitTrueLiteral(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitTupleLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InvalidLiteralContext extends LiteralExpressionContext {
		public InvalidLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitInvalidLiteral(this);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitMapLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NullLiteralContext extends LiteralExpressionContext {
		public NullLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitNullLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnlimitedNaturalLiteralContext extends LiteralExpressionContext {
		public UnlimitedNaturalLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitUnlimitedNaturalLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntegerLiteralContext extends LiteralExpressionContext {
		public TerminalNode INTEGER_LITERAL() { return getToken(QvtRParser.INTEGER_LITERAL, 0); }
		public IntegerLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitIntegerLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringLitContext extends LiteralExpressionContext {
		public StringLiteral_Context stringLiteral_() {
			return getRuleContext(StringLiteral_Context.class,0);
		}
		public StringLitContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitStringLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FalseLiteralContext extends LiteralExpressionContext {
		public FalseLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitFalseLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralExpressionContext literalExpression() throws RecognitionException {
		LiteralExpressionContext _localctx = new LiteralExpressionContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_literalExpression);
		try {
			setState(920);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER_LITERAL:
				_localctx = new IntegerLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(909);
				match(INTEGER_LITERAL);
				}
				break;
			case REAL_LITERAL:
				_localctx = new RealLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(910);
				match(REAL_LITERAL);
				}
				break;
			case STRING_LITERAL:
				_localctx = new StringLitContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(911);
				stringLiteral_();
				}
				break;
			case T__52:
				_localctx = new TrueLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(912);
				match(T__52);
				}
				break;
			case T__53:
				_localctx = new FalseLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(913);
				match(T__53);
				}
				break;
			case T__54:
				_localctx = new NullLiteralContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(914);
				match(T__54);
				}
				break;
			case T__55:
				_localctx = new InvalidLiteralContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(915);
				match(T__55);
				}
				break;
			case T__1:
				_localctx = new UnlimitedNaturalLiteralContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(916);
				match(T__1);
				}
				break;
			case T__56:
			case T__57:
			case T__58:
			case T__59:
			case T__60:
				_localctx = new CollectionLitContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(917);
				collectionLiteral();
				}
				break;
			case T__62:
				_localctx = new TupleLitContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(918);
				tupleLiteral();
				}
				break;
			case T__63:
				_localctx = new MapLitContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(919);
				mapLiteral();
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
		public List<TerminalNode> STRING_LITERAL() { return getTokens(QvtRParser.STRING_LITERAL); }
		public TerminalNode STRING_LITERAL(int i) {
			return getToken(QvtRParser.STRING_LITERAL, i);
		}
		public StringLiteral_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral_; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitStringLiteral_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteral_Context stringLiteral_() throws RecognitionException {
		StringLiteral_Context _localctx = new StringLiteral_Context(_ctx, getState());
		enterRule(_localctx, 114, RULE_stringLiteral_);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(923); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(922);
					match(STRING_LITERAL);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(925); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,108,_ctx);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCollectionLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionLiteralContext collectionLiteral() throws RecognitionException {
		CollectionLiteralContext _localctx = new CollectionLiteralContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_collectionLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(927);
			collectionKind();
			setState(928);
			match(T__7);
			setState(937);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214108L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 267227500537L) != 0)) {
				{
				setState(929);
				collectionLiteralPart();
				setState(934);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__5) {
					{
					{
					setState(930);
					match(T__5);
					setState(931);
					collectionLiteralPart();
					}
					}
					setState(936);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(939);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCollectionKind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionKindContext collectionKind() throws RecognitionException {
		CollectionKindContext _localctx = new CollectionKindContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_collectionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(941);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 4467570830351532032L) != 0)) ) {
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCollectionLiteralPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionLiteralPartContext collectionLiteralPart() throws RecognitionException {
		CollectionLiteralPartContext _localctx = new CollectionLiteralPartContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_collectionLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(943);
			expression(0);
			setState(946);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__61) {
				{
				setState(944);
				match(T__61);
				setState(945);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitTupleLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleLiteralContext tupleLiteral() throws RecognitionException {
		TupleLiteralContext _localctx = new TupleLiteralContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_tupleLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(948);
			match(T__62);
			setState(949);
			match(T__7);
			setState(950);
			tupleLiteralPart();
			setState(955);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(951);
				match(T__5);
				setState(952);
				tupleLiteralPart();
				}
				}
				setState(957);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(958);
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
	public static class TupleLiteralPartContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitTupleLiteralPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleLiteralPartContext tupleLiteralPart() throws RecognitionException {
		TupleLiteralPartContext _localctx = new TupleLiteralPartContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_tupleLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(960);
			identifier();
			setState(963);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(961);
				match(T__0);
				setState(962);
				typeExpression();
				}
			}

			setState(965);
			match(T__9);
			setState(966);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitMapLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapLiteralContext mapLiteral() throws RecognitionException {
		MapLiteralContext _localctx = new MapLiteralContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_mapLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(968);
			match(T__63);
			setState(969);
			match(T__7);
			setState(978);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214108L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 267227500537L) != 0)) {
				{
				setState(970);
				mapLiteralPart();
				setState(975);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__5) {
					{
					{
					setState(971);
					match(T__5);
					setState(972);
					mapLiteralPart();
					}
					}
					setState(977);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(980);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitMapLiteralPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapLiteralPartContext mapLiteralPart() throws RecognitionException {
		MapLiteralPartContext _localctx = new MapLiteralPartContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_mapLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(982);
			expression(0);
			setState(983);
			_la = _input.LA(1);
			if ( !(_la==T__64 || _la==T__65) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(984);
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
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TypeExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeExpression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitTypeExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeExpressionContext typeExpression() throws RecognitionException {
		TypeExpressionContext _localctx = new TypeExpressionContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_typeExpression);
		try {
			setState(991);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case T__70:
			case T__71:
			case T__72:
			case T__73:
			case T__74:
				enterOuterAlt(_localctx, 1);
				{
				setState(986);
				primitiveType();
				}
				break;
			case T__56:
			case T__57:
			case T__58:
			case T__59:
			case T__60:
				enterOuterAlt(_localctx, 2);
				{
				setState(987);
				collectionType();
				}
				break;
			case T__63:
				enterOuterAlt(_localctx, 3);
				{
				setState(988);
				mapType();
				}
				break;
			case T__62:
				enterOuterAlt(_localctx, 4);
				{
				setState(989);
				tupleType();
				}
				break;
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case T__18:
			case T__19:
			case T__20:
			case T__21:
			case T__22:
			case T__23:
			case T__24:
			case T__25:
			case DEFAULT_VALUES:
			case ABSTRACT:
			case OPPOSITE:
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 5);
				{
				setState(990);
				pathName();
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
	public static class PrimitiveTypeContext extends ParserRuleContext {
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPrimitiveType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(993);
			_la = _input.LA(1);
			if ( !(((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 511L) != 0)) ) {
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitCollectionType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionTypeContext collectionType() throws RecognitionException {
		CollectionTypeContext _localctx = new CollectionTypeContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_collectionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(995);
			collectionKind();
			setState(996);
			match(T__4);
			setState(997);
			typeExpression();
			setState(998);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitMapType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapTypeContext mapType() throws RecognitionException {
		MapTypeContext _localctx = new MapTypeContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1000);
			match(T__63);
			setState(1001);
			match(T__4);
			setState(1002);
			typeExpression();
			setState(1003);
			match(T__5);
			setState(1004);
			typeExpression();
			setState(1005);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitTupleType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleTypeContext tupleType() throws RecognitionException {
		TupleTypeContext _localctx = new TupleTypeContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_tupleType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1007);
			match(T__62);
			setState(1008);
			match(T__4);
			setState(1009);
			tupleTypePart();
			setState(1014);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(1010);
				match(T__5);
				setState(1011);
				tupleTypePart();
				}
				}
				setState(1016);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1017);
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
	public static class TupleTypePartContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
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
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitTupleTypePart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleTypePartContext tupleTypePart() throws RecognitionException {
		TupleTypePartContext _localctx = new TupleTypePartContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_tupleTypePart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1019);
			identifier();
			setState(1020);
			match(T__0);
			setState(1021);
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
	public static class PathNameContext extends ParserRuleContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TerminalNode> COLONCOLON() { return getTokens(QvtRParser.COLONCOLON); }
		public TerminalNode COLONCOLON(int i) {
			return getToken(QvtRParser.COLONCOLON, i);
		}
		public List<RestrictedWordContext> restrictedWord() {
			return getRuleContexts(RestrictedWordContext.class);
		}
		public RestrictedWordContext restrictedWord(int i) {
			return getRuleContext(RestrictedWordContext.class,i);
		}
		public PathNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitPathName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathNameContext pathName() throws RecognitionException {
		PathNameContext _localctx = new PathNameContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_pathName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1023);
			identifier();
			setState(1031);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,119,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1024);
					match(COLONCOLON);
					setState(1027);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__13:
					case T__14:
					case T__15:
					case T__16:
					case T__17:
					case T__18:
					case T__19:
					case T__20:
					case T__21:
					case T__22:
					case T__23:
					case T__24:
					case T__25:
					case DEFAULT_VALUES:
					case ABSTRACT:
					case OPPOSITE:
					case ESCAPED_IDENTIFIER:
					case IDENTIFIER:
						{
						setState(1025);
						identifier();
						}
						break;
					case T__56:
					case T__57:
					case T__58:
					case T__59:
					case T__60:
					case T__62:
					case T__66:
					case T__67:
					case T__68:
					case T__69:
					case T__70:
					case T__71:
					case T__72:
					case T__73:
					case T__74:
						{
						setState(1026);
						restrictedWord();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					} 
				}
				setState(1033);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,119,_ctx);
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
	public static class RestrictedWordContext extends ParserRuleContext {
		public RestrictedWordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_restrictedWord; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitRestrictedWord(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RestrictedWordContext restrictedWord() throws RecognitionException {
		RestrictedWordContext _localctx = new RestrictedWordContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_restrictedWord);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1034);
			_la = _input.LA(1);
			if ( !(((((_la - 57)) & ~0x3f) == 0 && ((1L << (_la - 57)) & 523359L) != 0)) ) {
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
	public static class DocumentKeywordContext extends ParserRuleContext {
		public TerminalNode IMPORT() { return getToken(QvtRParser.IMPORT, 0); }
		public DocumentKeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_documentKeyword; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QvtRVisitor ) return ((QvtRVisitor<? extends T>)visitor).visitDocumentKeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocumentKeywordContext documentKeyword() throws RecognitionException {
		DocumentKeywordContext _localctx = new DocumentKeywordContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_documentKeyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1036);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || _la==IMPORT) ) {
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 48:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 9);
		case 1:
			return precpred(_ctx, 8);
		case 2:
			return precpred(_ctx, 7);
		case 3:
			return precpred(_ctx, 6);
		case 4:
			return precpred(_ctx, 5);
		case 5:
			return precpred(_ctx, 4);
		case 6:
			return precpred(_ctx, 3);
		case 7:
			return precpred(_ctx, 2);
		case 8:
			return precpred(_ctx, 13);
		case 9:
			return precpred(_ctx, 12);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001h\u040f\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"+
		"(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007,\u0002"+
		"-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u00071\u0002"+
		"2\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u00076\u0002"+
		"7\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007;\u0002"+
		"<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007@\u0002"+
		"A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007D\u0002E\u0007E\u0002"+
		"F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007I\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0005\u0001\u0099\b\u0001\n\u0001\f\u0001\u009c"+
		"\t\u0001\u0001\u0001\u0005\u0001\u009f\b\u0001\n\u0001\f\u0001\u00a2\t"+
		"\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002\u00a8"+
		"\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u00ad\b\u0002"+
		"\n\u0002\f\u0002\u00b0\t\u0002\u0001\u0002\u0001\u0002\u0003\u0002\u00b4"+
		"\b\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0003\u0003\u00ba"+
		"\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u00bf\b\u0004"+
		"\n\u0004\f\u0004\u00c2\t\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u00ca\b\u0005\n\u0005\f\u0005"+
		"\u00cd\t\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u00d2\b"+
		"\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u00d6\b\u0005\n\u0005\f\u0005"+
		"\u00d9\t\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u00dd\b\u0005\n\u0005"+
		"\f\u0005\u00e0\t\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006"+
		"\u00eb\b\u0006\n\u0006\f\u0006\u00ee\t\u0006\u0001\u0006\u0001\u0006\u0003"+
		"\u0006\u00f2\b\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0005\b\u00fc\b\b\n\b\f\b\u00ff\t\b\u0001\b\u0001\b"+
		"\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0003\t\u010c\b\t\u0001\n\u0003\n\u010f\b\n\u0001\n\u0003\n\u0112\b"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u0118\b\n\u0001\n\u0001\n\u0005"+
		"\n\u011c\b\n\n\n\f\n\u011f\t\n\u0001\n\u0001\n\u0004\n\u0123\b\n\u000b"+
		"\n\f\n\u0124\u0001\n\u0003\n\u0128\b\n\u0001\n\u0003\n\u012b\b\n\u0001"+
		"\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b\u0132\b\u000b"+
		"\n\u000b\f\u000b\u0135\t\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0003\u000b\u013b\b\u000b\u0001\u000b\u0001\u000b\u0001\f\u0003"+
		"\f\u0140\b\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u0147\b\f"+
		"\n\f\f\f\u014a\t\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u0150\b\f\u0001"+
		"\f\u0001\f\u0003\f\u0154\b\f\u0001\f\u0001\f\u0001\f\u0004\f\u0159\b\f"+
		"\u000b\f\f\f\u015a\u0001\f\u0001\f\u0003\f\u015f\b\f\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0003\u0010\u0173\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0003\u0010\u0179\b\u0010\u0001\u0011\u0003\u0011\u017c\b"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u0181\b\u0011\u0001"+
		"\u0011\u0001\u0011\u0003\u0011\u0185\b\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0005\u0013\u0190\b\u0013\n\u0013\f\u0013\u0193\t\u0013\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0003\u0014\u01af\b\u0014\u0001\u0015\u0003\u0015\u01b2\b"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0003\u0015\u01bb\b\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0005\u0016\u01c2\b\u0016\n\u0016\f\u0016"+
		"\u01c5\t\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u01ca\b"+
		"\u0016\u0003\u0016\u01cc\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0003"+
		"\u0017\u01d1\b\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0005\u0018\u01d8\b\u0018\n\u0018\f\u0018\u01db\t\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0005\u0019\u01e4\b\u0019\n\u0019\f\u0019\u01e7\t\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u001a\u0001\u001a\u0003\u001a\u01ed\b\u001a\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0005\u001b\u01f5"+
		"\b\u001b\n\u001b\f\u001b\u01f8\t\u001b\u0003\u001b\u01fa\b\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0003\u001b\u0204\b\u001b\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003"+
		"\u001d\u020e\b\u001d\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001"+
		" \u0001 \u0001 \u0001!\u0001!\u0001!\u0001\"\u0005\"\u021b\b\"\n\"\f\""+
		"\u021e\t\"\u0001\"\u0001\"\u0005\"\u0222\b\"\n\"\f\"\u0225\t\"\u0001#"+
		"\u0001#\u0001#\u0001#\u0003#\u022b\b#\u0001#\u0001#\u0001#\u0003#\u0230"+
		"\b#\u0001$\u0001$\u0001$\u0005$\u0235\b$\n$\f$\u0238\t$\u0001$\u0001$"+
		"\u0001%\u0001%\u0001%\u0003%\u023f\b%\u0001&\u0001&\u0001&\u0001&\u0003"+
		"&\u0245\b&\u0001&\u0001&\u0004&\u0249\b&\u000b&\f&\u024a\u0001\'\u0001"+
		"\'\u0003\'\u024f\b\'\u0001(\u0001(\u0001(\u0001(\u0001(\u0001(\u0003("+
		"\u0257\b(\u0003(\u0259\b(\u0001(\u0001(\u0001(\u0001)\u0003)\u025f\b)"+
		"\u0001)\u0001)\u0003)\u0263\b)\u0001)\u0001)\u0001)\u0001)\u0001)\u0001"+
		")\u0001)\u0001)\u0001)\u0001)\u0003)\u026f\b)\u0001)\u0001)\u0001)\u0001"+
		")\u0001)\u0001)\u0003)\u0277\b)\u0001*\u0001*\u0001*\u0001*\u0003*\u027d"+
		"\b*\u0001*\u0001*\u0001*\u0003*\u0282\b*\u0001*\u0001*\u0001*\u0003*\u0287"+
		"\b*\u0001*\u0004*\u028a\b*\u000b*\f*\u028b\u0001+\u0001+\u0003+\u0290"+
		"\b+\u0001+\u0001+\u0001+\u0001+\u0003+\u0296\b+\u0001+\u0001+\u0001+\u0001"+
		"+\u0003+\u029c\b+\u0001+\u0001+\u0003+\u02a0\b+\u0001,\u0001,\u0001,\u0001"+
		",\u0001,\u0001,\u0001,\u0004,\u02a9\b,\u000b,\f,\u02aa\u0001-\u0001-\u0003"+
		"-\u02af\b-\u0001-\u0001-\u0001-\u0001-\u0003-\u02b5\b-\u0001-\u0001-\u0003"+
		"-\u02b9\b-\u0001.\u0001.\u0001.\u0005.\u02be\b.\n.\f.\u02c1\t.\u0001/"+
		"\u0001/\u0001/\u0001/\u00010\u00010\u00010\u00010\u00010\u00010\u0003"+
		"0\u02cd\b0\u00010\u00010\u00010\u00010\u00010\u00010\u00010\u00010\u0001"+
		"0\u00010\u00010\u00010\u00010\u00010\u00010\u00010\u00010\u00010\u0001"+
		"0\u00010\u00010\u00010\u00010\u00010\u00010\u00010\u00010\u00010\u0001"+
		"0\u00010\u00050\u02ed\b0\n0\f0\u02f0\t0\u00011\u00011\u00011\u00011\u0001"+
		"1\u00011\u00011\u00011\u00011\u00011\u00011\u00011\u00011\u00011\u0005"+
		"1\u0300\b1\n1\f1\u0303\t1\u00011\u00011\u00011\u00011\u00011\u00011\u0001"+
		"1\u00011\u00051\u030d\b1\n1\f1\u0310\t1\u00011\u00011\u00011\u00011\u0001"+
		"1\u00011\u00031\u0318\b1\u00011\u00011\u00031\u031c\b1\u00011\u00011\u0003"+
		"1\u0320\b1\u00011\u00011\u00011\u00011\u00011\u00031\u0327\b1\u00012\u0001"+
		"2\u00012\u00032\u032c\b2\u00012\u00012\u00032\u0330\b2\u00012\u00012\u0003"+
		"2\u0334\b2\u00012\u00012\u00032\u0338\b2\u00012\u00012\u00012\u00032\u033d"+
		"\b2\u00012\u00012\u00032\u0341\b2\u00032\u0343\b2\u00013\u00013\u0001"+
		"3\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u0003"+
		"3\u0351\b3\u00013\u00013\u00033\u0355\b3\u00013\u00013\u00013\u00033\u035a"+
		"\b3\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u0003"+
		"3\u0365\b3\u00013\u00013\u00033\u0369\b3\u00014\u00014\u00014\u00034\u036e"+
		"\b4\u00014\u00014\u00014\u00014\u00034\u0374\b4\u00054\u0376\b4\n4\f4"+
		"\u0379\t4\u00015\u00015\u00015\u00055\u037e\b5\n5\f5\u0381\t5\u00016\u0001"+
		"6\u00016\u00036\u0386\b6\u00016\u00016\u00016\u00017\u00017\u00017\u0001"+
		"8\u00018\u00018\u00018\u00018\u00018\u00018\u00018\u00018\u00018\u0001"+
		"8\u00038\u0399\b8\u00019\u00049\u039c\b9\u000b9\f9\u039d\u0001:\u0001"+
		":\u0001:\u0001:\u0001:\u0005:\u03a5\b:\n:\f:\u03a8\t:\u0003:\u03aa\b:"+
		"\u0001:\u0001:\u0001;\u0001;\u0001<\u0001<\u0001<\u0003<\u03b3\b<\u0001"+
		"=\u0001=\u0001=\u0001=\u0001=\u0005=\u03ba\b=\n=\f=\u03bd\t=\u0001=\u0001"+
		"=\u0001>\u0001>\u0001>\u0003>\u03c4\b>\u0001>\u0001>\u0001>\u0001?\u0001"+
		"?\u0001?\u0001?\u0001?\u0005?\u03ce\b?\n?\f?\u03d1\t?\u0003?\u03d3\b?"+
		"\u0001?\u0001?\u0001@\u0001@\u0001@\u0001@\u0001A\u0001A\u0001A\u0001"+
		"A\u0001A\u0003A\u03e0\bA\u0001B\u0001B\u0001C\u0001C\u0001C\u0001C\u0001"+
		"C\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0001E\u0001E\u0001"+
		"E\u0001E\u0001E\u0005E\u03f5\bE\nE\fE\u03f8\tE\u0001E\u0001E\u0001F\u0001"+
		"F\u0001F\u0001F\u0001G\u0001G\u0001G\u0001G\u0003G\u0404\bG\u0005G\u0406"+
		"\bG\nG\fG\u0409\tG\u0001H\u0001H\u0001I\u0001I\u0001I\u0000\u0001`J\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c"+
		"\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084"+
		"\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0000\u000f\u0002\u0000LLN"+
		"N\u0001\u0000\u000e\u001a\u0001\u0000[]\u0002\u0000\u0019\u001aQQ\u0002"+
		"\u0000\u0002\u0002  \u0002\u0000\u001f\u001f!!\u0001\u0000\"%\u0002\u0000"+
		"\n\n&&\u0002\u0000\u0004\u0004\u001b\u001b\u0001\u0000\u001c\u001d\u0001"+
		"\u00009=\u0001\u0000AB\u0001\u0000CK\u0003\u00009=??CK\u0002\u0000\u000e"+
		"\u001aQQ\u0462\u0000\u0094\u0001\u0000\u0000\u0000\u0002\u009a\u0001\u0000"+
		"\u0000\u0000\u0004\u00a3\u0001\u0000\u0000\u0000\u0006\u00b9\u0001\u0000"+
		"\u0000\u0000\b\u00bb\u0001\u0000\u0000\u0000\n\u00c3\u0001\u0000\u0000"+
		"\u0000\f\u00e3\u0001\u0000\u0000\u0000\u000e\u00f3\u0001\u0000\u0000\u0000"+
		"\u0010\u00f5\u0001\u0000\u0000\u0000\u0012\u010b\u0001\u0000\u0000\u0000"+
		"\u0014\u010e\u0001\u0000\u0000\u0000\u0016\u012e\u0001\u0000\u0000\u0000"+
		"\u0018\u013f\u0001\u0000\u0000\u0000\u001a\u0162\u0001\u0000\u0000\u0000"+
		"\u001c\u0169\u0001\u0000\u0000\u0000\u001e\u016b\u0001\u0000\u0000\u0000"+
		" \u0172\u0001\u0000\u0000\u0000\"\u017b\u0001\u0000\u0000\u0000$\u0188"+
		"\u0001\u0000\u0000\u0000&\u018c\u0001\u0000\u0000\u0000(\u01ae\u0001\u0000"+
		"\u0000\u0000*\u01b1\u0001\u0000\u0000\u0000,\u01be\u0001\u0000\u0000\u0000"+
		".\u01d0\u0001\u0000\u0000\u00000\u01d2\u0001\u0000\u0000\u00002\u01de"+
		"\u0001\u0000\u0000\u00004\u01ec\u0001\u0000\u0000\u00006\u01ee\u0001\u0000"+
		"\u0000\u00008\u0205\u0001\u0000\u0000\u0000:\u020d\u0001\u0000\u0000\u0000"+
		"<\u020f\u0001\u0000\u0000\u0000>\u0211\u0001\u0000\u0000\u0000@\u0213"+
		"\u0001\u0000\u0000\u0000B\u0216\u0001\u0000\u0000\u0000D\u021c\u0001\u0000"+
		"\u0000\u0000F\u0226\u0001\u0000\u0000\u0000H\u0231\u0001\u0000\u0000\u0000"+
		"J\u023e\u0001\u0000\u0000\u0000L\u0240\u0001\u0000\u0000\u0000N\u024e"+
		"\u0001\u0000\u0000\u0000P\u0250\u0001\u0000\u0000\u0000R\u025e\u0001\u0000"+
		"\u0000\u0000T\u0278\u0001\u0000\u0000\u0000V\u029f\u0001\u0000\u0000\u0000"+
		"X\u02a1\u0001\u0000\u0000\u0000Z\u02b8\u0001\u0000\u0000\u0000\\\u02ba"+
		"\u0001\u0000\u0000\u0000^\u02c2\u0001\u0000\u0000\u0000`\u02cc\u0001\u0000"+
		"\u0000\u0000b\u0326\u0001\u0000\u0000\u0000d\u0342\u0001\u0000\u0000\u0000"+
		"f\u0368\u0001\u0000\u0000\u0000h\u036a\u0001\u0000\u0000\u0000j\u037a"+
		"\u0001\u0000\u0000\u0000l\u0382\u0001\u0000\u0000\u0000n\u038a\u0001\u0000"+
		"\u0000\u0000p\u0398\u0001\u0000\u0000\u0000r\u039b\u0001\u0000\u0000\u0000"+
		"t\u039f\u0001\u0000\u0000\u0000v\u03ad\u0001\u0000\u0000\u0000x\u03af"+
		"\u0001\u0000\u0000\u0000z\u03b4\u0001\u0000\u0000\u0000|\u03c0\u0001\u0000"+
		"\u0000\u0000~\u03c8\u0001\u0000\u0000\u0000\u0080\u03d6\u0001\u0000\u0000"+
		"\u0000\u0082\u03df\u0001\u0000\u0000\u0000\u0084\u03e1\u0001\u0000\u0000"+
		"\u0000\u0086\u03e3\u0001\u0000\u0000\u0000\u0088\u03e8\u0001\u0000\u0000"+
		"\u0000\u008a\u03ef\u0001\u0000\u0000\u0000\u008c\u03fb\u0001\u0000\u0000"+
		"\u0000\u008e\u03ff\u0001\u0000\u0000\u0000\u0090\u040a\u0001\u0000\u0000"+
		"\u0000\u0092\u040c\u0001\u0000\u0000\u0000\u0094\u0095\u0003\u0002\u0001"+
		"\u0000\u0095\u0096\u0005\u0000\u0000\u0001\u0096\u0001\u0001\u0000\u0000"+
		"\u0000\u0097\u0099\u0003\u0004\u0002\u0000\u0098\u0097\u0001\u0000\u0000"+
		"\u0000\u0099\u009c\u0001\u0000\u0000\u0000\u009a\u0098\u0001\u0000\u0000"+
		"\u0000\u009a\u009b\u0001\u0000\u0000\u0000\u009b\u00a0\u0001\u0000\u0000"+
		"\u0000\u009c\u009a\u0001\u0000\u0000\u0000\u009d\u009f\u0003\n\u0005\u0000"+
		"\u009e\u009d\u0001\u0000\u0000\u0000\u009f\u00a2\u0001\u0000\u0000\u0000"+
		"\u00a0\u009e\u0001\u0000\u0000\u0000\u00a0\u00a1\u0001\u0000\u0000\u0000"+
		"\u00a1\u0003\u0001\u0000\u0000\u0000\u00a2\u00a0\u0001\u0000\u0000\u0000"+
		"\u00a3\u00a7\u0005Q\u0000\u0000\u00a4\u00a5\u0003:\u001d\u0000\u00a5\u00a6"+
		"\u0005\u0001\u0000\u0000\u00a6\u00a8\u0001\u0000\u0000\u0000\u00a7\u00a4"+
		"\u0001\u0000\u0000\u0000\u00a7\u00a8\u0001\u0000\u0000\u0000\u00a8\u00a9"+
		"\u0001\u0000\u0000\u0000\u00a9\u00ae\u0003\u0006\u0003\u0000\u00aa\u00ab"+
		"\u0005_\u0000\u0000\u00ab\u00ad\u0003:\u001d\u0000\u00ac\u00aa\u0001\u0000"+
		"\u0000\u0000\u00ad\u00b0\u0001\u0000\u0000\u0000\u00ae\u00ac\u0001\u0000"+
		"\u0000\u0000\u00ae\u00af\u0001\u0000\u0000\u0000\u00af\u00b3\u0001\u0000"+
		"\u0000\u0000\u00b0\u00ae\u0001\u0000\u0000\u0000\u00b1\u00b2\u0005_\u0000"+
		"\u0000\u00b2\u00b4\u0005\u0002\u0000\u0000\u00b3\u00b1\u0001\u0000\u0000"+
		"\u0000\u00b3\u00b4\u0001\u0000\u0000\u0000\u00b4\u00b5\u0001\u0000\u0000"+
		"\u0000\u00b5\u00b6\u0005\u0003\u0000\u0000\u00b6\u0005\u0001\u0000\u0000"+
		"\u0000\u00b7\u00ba\u0005c\u0000\u0000\u00b8\u00ba\u0003\b\u0004\u0000"+
		"\u00b9\u00b7\u0001\u0000\u0000\u0000\u00b9\u00b8\u0001\u0000\u0000\u0000"+
		"\u00ba\u0007\u0001\u0000\u0000\u0000\u00bb\u00c0\u0003:\u001d\u0000\u00bc"+
		"\u00bd\u0005\u0004\u0000\u0000\u00bd\u00bf\u0003:\u001d\u0000\u00be\u00bc"+
		"\u0001\u0000\u0000\u0000\u00bf\u00c2\u0001\u0000\u0000\u0000\u00c0\u00be"+
		"\u0001\u0000\u0000\u0000\u00c0\u00c1\u0001\u0000\u0000\u0000\u00c1\t\u0001"+
		"\u0000\u0000\u0000\u00c2\u00c0\u0001\u0000\u0000\u0000\u00c3\u00c4\u0005"+
		"X\u0000\u0000\u00c4\u00c5\u0003\u008eG\u0000\u00c5\u00c6\u0005\u0005\u0000"+
		"\u0000\u00c6\u00cb\u0003\f\u0006\u0000\u00c7\u00c8\u0005\u0006\u0000\u0000"+
		"\u00c8\u00ca\u0003\f\u0006\u0000\u00c9\u00c7\u0001\u0000\u0000\u0000\u00ca"+
		"\u00cd\u0001\u0000\u0000\u0000\u00cb\u00c9\u0001\u0000\u0000\u0000\u00cb"+
		"\u00cc\u0001\u0000\u0000\u0000\u00cc\u00ce\u0001\u0000\u0000\u0000\u00cd"+
		"\u00cb\u0001\u0000\u0000\u0000\u00ce\u00d1\u0005\u0007\u0000\u0000\u00cf"+
		"\u00d0\u0005O\u0000\u0000\u00d0\u00d2\u0003\u008eG\u0000\u00d1\u00cf\u0001"+
		"\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2\u00d3\u0001"+
		"\u0000\u0000\u0000\u00d3\u00d7\u0005\b\u0000\u0000\u00d4\u00d6\u0003\u0010"+
		"\b\u0000\u00d5\u00d4\u0001\u0000\u0000\u0000\u00d6\u00d9\u0001\u0000\u0000"+
		"\u0000\u00d7\u00d5\u0001\u0000\u0000\u0000\u00d7\u00d8\u0001\u0000\u0000"+
		"\u0000\u00d8\u00de\u0001\u0000\u0000\u0000\u00d9\u00d7\u0001\u0000\u0000"+
		"\u0000\u00da\u00dd\u0003\u0014\n\u0000\u00db\u00dd\u00036\u001b\u0000"+
		"\u00dc\u00da\u0001\u0000\u0000\u0000\u00dc\u00db\u0001\u0000\u0000\u0000"+
		"\u00dd\u00e0\u0001\u0000\u0000\u0000\u00de\u00dc\u0001\u0000\u0000\u0000"+
		"\u00de\u00df\u0001\u0000\u0000\u0000\u00df\u00e1\u0001\u0000\u0000\u0000"+
		"\u00e0\u00de\u0001\u0000\u0000\u0000\u00e1\u00e2\u0005\t\u0000\u0000\u00e2"+
		"\u000b\u0001\u0000\u0000\u0000\u00e3\u00e4\u0003:\u001d\u0000\u00e4\u00f1"+
		"\u0005\u0001\u0000\u0000\u00e5\u00f2\u0003\u000e\u0007\u0000\u00e6\u00e7"+
		"\u0005\b\u0000\u0000\u00e7\u00ec\u0003\u000e\u0007\u0000\u00e8\u00e9\u0005"+
		"\u0006\u0000\u0000\u00e9\u00eb\u0003\u000e\u0007\u0000\u00ea\u00e8\u0001"+
		"\u0000\u0000\u0000\u00eb\u00ee\u0001\u0000\u0000\u0000\u00ec\u00ea\u0001"+
		"\u0000\u0000\u0000\u00ec\u00ed\u0001\u0000\u0000\u0000\u00ed\u00ef\u0001"+
		"\u0000\u0000\u0000\u00ee\u00ec\u0001\u0000\u0000\u0000\u00ef\u00f0\u0005"+
		"\t\u0000\u0000\u00f0\u00f2\u0001\u0000\u0000\u0000\u00f1\u00e5\u0001\u0000"+
		"\u0000\u0000\u00f1\u00e6\u0001\u0000\u0000\u0000\u00f2\r\u0001\u0000\u0000"+
		"\u0000\u00f3\u00f4\u0003\u008eG\u0000\u00f4\u000f\u0001\u0000\u0000\u0000"+
		"\u00f5\u00f6\u0005R\u0000\u0000\u00f6\u00f7\u0003\u008eG\u0000\u00f7\u00f8"+
		"\u0005\b\u0000\u0000\u00f8\u00fd\u0003\u0012\t\u0000\u00f9\u00fa\u0005"+
		"\u0006\u0000\u0000\u00fa\u00fc\u0003\u0012\t\u0000\u00fb\u00f9\u0001\u0000"+
		"\u0000\u0000\u00fc\u00ff\u0001\u0000\u0000\u0000\u00fd\u00fb\u0001\u0000"+
		"\u0000\u0000\u00fd\u00fe\u0001\u0000\u0000\u0000\u00fe\u0100\u0001\u0000"+
		"\u0000\u0000\u00ff\u00fd\u0001\u0000\u0000\u0000\u0100\u0101\u0005\t\u0000"+
		"\u0000\u0101\u0102\u0005\u0003\u0000\u0000\u0102\u0011\u0001\u0000\u0000"+
		"\u0000\u0103\u010c\u0003:\u001d\u0000\u0104\u0105\u0005]\u0000\u0000\u0105"+
		"\u0106\u0005\u0005\u0000\u0000\u0106\u0107\u0003\u008eG\u0000\u0107\u0108"+
		"\u0005_\u0000\u0000\u0108\u0109\u0003:\u001d\u0000\u0109\u010a\u0005\u0007"+
		"\u0000\u0000\u010a\u010c\u0001\u0000\u0000\u0000\u010b\u0103\u0001\u0000"+
		"\u0000\u0000\u010b\u0104\u0001\u0000\u0000\u0000\u010c\u0013\u0001\u0000"+
		"\u0000\u0000\u010d\u010f\u0005W\u0000\u0000\u010e\u010d\u0001\u0000\u0000"+
		"\u0000\u010e\u010f\u0001\u0000\u0000\u0000\u010f\u0111\u0001\u0000\u0000"+
		"\u0000\u0110\u0112\u0005\\\u0000\u0000\u0111\u0110\u0001\u0000\u0000\u0000"+
		"\u0111\u0112\u0001\u0000\u0000\u0000\u0112\u0113\u0001\u0000\u0000\u0000"+
		"\u0113\u0114\u0005V\u0000\u0000\u0114\u0117\u0003:\u001d\u0000\u0115\u0116"+
		"\u0005S\u0000\u0000\u0116\u0118\u0003:\u001d\u0000\u0117\u0115\u0001\u0000"+
		"\u0000\u0000\u0117\u0118\u0001\u0000\u0000\u0000\u0118\u0119\u0001\u0000"+
		"\u0000\u0000\u0119\u011d\u0005\b\u0000\u0000\u011a\u011c\u0003\u0016\u000b"+
		"\u0000\u011b\u011a\u0001\u0000\u0000\u0000\u011c\u011f\u0001\u0000\u0000"+
		"\u0000\u011d\u011b\u0001\u0000\u0000\u0000\u011d\u011e\u0001\u0000\u0000"+
		"\u0000\u011e\u0122\u0001\u0000\u0000\u0000\u011f\u011d\u0001\u0000\u0000"+
		"\u0000\u0120\u0123\u0003\u0018\f\u0000\u0121\u0123\u0003\u001a\r\u0000"+
		"\u0122\u0120\u0001\u0000\u0000\u0000\u0122\u0121\u0001\u0000\u0000\u0000"+
		"\u0123\u0124\u0001\u0000\u0000\u0000\u0124\u0122\u0001\u0000\u0000\u0000"+
		"\u0124\u0125\u0001\u0000\u0000\u0000\u0125\u0127\u0001\u0000\u0000\u0000"+
		"\u0126\u0128\u00030\u0018\u0000\u0127\u0126\u0001\u0000\u0000\u0000\u0127"+
		"\u0128\u0001\u0000\u0000\u0000\u0128\u012a\u0001\u0000\u0000\u0000\u0129"+
		"\u012b\u00032\u0019\u0000\u012a\u0129\u0001\u0000\u0000\u0000\u012a\u012b"+
		"\u0001\u0000\u0000\u0000\u012b\u012c\u0001\u0000\u0000\u0000\u012c\u012d"+
		"\u0005\t\u0000\u0000\u012d\u0015\u0001\u0000\u0000\u0000\u012e\u0133\u0003"+
		":\u001d\u0000\u012f\u0130\u0005\u0006\u0000\u0000\u0130\u0132\u0003:\u001d"+
		"\u0000\u0131\u012f\u0001\u0000\u0000\u0000\u0132\u0135\u0001\u0000\u0000"+
		"\u0000\u0133\u0131\u0001\u0000\u0000\u0000\u0133\u0134\u0001\u0000\u0000"+
		"\u0000\u0134\u0136\u0001\u0000\u0000\u0000\u0135\u0133\u0001\u0000\u0000"+
		"\u0000\u0136\u0137\u0005\u0001\u0000\u0000\u0137\u013a\u0003\u0082A\u0000"+
		"\u0138\u0139\u0005\n\u0000\u0000\u0139\u013b\u0003`0\u0000\u013a\u0138"+
		"\u0001\u0000\u0000\u0000\u013a\u013b\u0001\u0000\u0000\u0000\u013b\u013c"+
		"\u0001\u0000\u0000\u0000\u013c\u013d\u0005\u0003\u0000\u0000\u013d\u0017"+
		"\u0001\u0000\u0000\u0000\u013e\u0140\u0003\u001c\u000e\u0000\u013f\u013e"+
		"\u0001\u0000\u0000\u0000\u013f\u0140\u0001\u0000\u0000\u0000\u0140\u0141"+
		"\u0001\u0000\u0000\u0000\u0141\u0142\u0005M\u0000\u0000\u0142\u0143\u0003"+
		":\u001d\u0000\u0143\u0148\u0003 \u0010\u0000\u0144\u0145\u0005\u0006\u0000"+
		"\u0000\u0145\u0147\u0003 \u0010\u0000\u0146\u0144\u0001\u0000\u0000\u0000"+
		"\u0147\u014a\u0001\u0000\u0000\u0000\u0148\u0146\u0001\u0000\u0000\u0000"+
		"\u0148\u0149\u0001\u0000\u0000\u0000\u0149\u0153\u0001\u0000\u0000\u0000"+
		"\u014a\u0148\u0001\u0000\u0000\u0000\u014b\u014c\u0005P\u0000\u0000\u014c"+
		"\u014d\u0003\u008eG\u0000\u014d\u014f\u0005\u0005\u0000\u0000\u014e\u0150"+
		"\u0003j5\u0000\u014f\u014e\u0001\u0000\u0000\u0000\u014f\u0150\u0001\u0000"+
		"\u0000\u0000\u0150\u0151\u0001\u0000\u0000\u0000\u0151\u0152\u0005\u0007"+
		"\u0000\u0000\u0152\u0154\u0001\u0000\u0000\u0000\u0153\u014b\u0001\u0000"+
		"\u0000\u0000\u0153\u0154\u0001\u0000\u0000\u0000\u0154\u015e\u0001\u0000"+
		"\u0000\u0000\u0155\u0156\u0005[\u0000\u0000\u0156\u0158\u0005\b\u0000"+
		"\u0000\u0157\u0159\u0003\u001e\u000f\u0000\u0158\u0157\u0001\u0000\u0000"+
		"\u0000\u0159\u015a\u0001\u0000\u0000\u0000\u015a\u0158\u0001\u0000\u0000"+
		"\u0000\u015a\u015b\u0001\u0000\u0000\u0000\u015b\u015c\u0001\u0000\u0000"+
		"\u0000\u015c\u015d\u0005\t\u0000\u0000\u015d\u015f\u0001\u0000\u0000\u0000"+
		"\u015e\u0155\u0001\u0000\u0000\u0000\u015e\u015f\u0001\u0000\u0000\u0000"+
		"\u015f\u0160\u0001\u0000\u0000\u0000\u0160\u0161\u0005\u0003\u0000\u0000"+
		"\u0161\u0019\u0001\u0000\u0000\u0000\u0162\u0163\u0005T\u0000\u0000\u0163"+
		"\u0164\u0005M\u0000\u0000\u0164\u0165\u0003:\u001d\u0000\u0165\u0166\u0005"+
		"\u0001\u0000\u0000\u0166\u0167\u0003\u0082A\u0000\u0167\u0168\u0005\u0003"+
		"\u0000\u0000\u0168\u001b\u0001\u0000\u0000\u0000\u0169\u016a\u0007\u0000"+
		"\u0000\u0000\u016a\u001d\u0001\u0000\u0000\u0000\u016b\u016c\u0003:\u001d"+
		"\u0000\u016c\u016d\u0005\n\u0000\u0000\u016d\u016e\u0003`0\u0000\u016e"+
		"\u016f\u0005\u0003\u0000\u0000\u016f\u001f\u0001\u0000\u0000\u0000\u0170"+
		"\u0173\u0003\"\u0011\u0000\u0171\u0173\u0003*\u0015\u0000\u0172\u0170"+
		"\u0001\u0000\u0000\u0000\u0172\u0171\u0001\u0000\u0000\u0000\u0173\u0178"+
		"\u0001\u0000\u0000\u0000\u0174\u0175\u0005\b\u0000\u0000\u0175\u0176\u0003"+
		"`0\u0000\u0176\u0177\u0005\t\u0000\u0000\u0177\u0179\u0001\u0000\u0000"+
		"\u0000\u0178\u0174\u0001\u0000\u0000\u0000\u0178\u0179\u0001\u0000\u0000"+
		"\u0000\u0179!\u0001\u0000\u0000\u0000\u017a\u017c\u0003:\u001d\u0000\u017b"+
		"\u017a\u0001\u0000\u0000\u0000\u017b\u017c\u0001\u0000\u0000\u0000\u017c"+
		"\u017d\u0001\u0000\u0000\u0000\u017d\u017e\u0005\u0001\u0000\u0000\u017e"+
		"\u0180\u0003\u008eG\u0000\u017f\u0181\u0003$\u0012\u0000\u0180\u017f\u0001"+
		"\u0000\u0000\u0000\u0180\u0181\u0001\u0000\u0000\u0000\u0181\u0182\u0001"+
		"\u0000\u0000\u0000\u0182\u0184\u0005\b\u0000\u0000\u0183\u0185\u0003&"+
		"\u0013\u0000\u0184\u0183\u0001\u0000\u0000\u0000\u0184\u0185\u0001\u0000"+
		"\u0000\u0000\u0185\u0186\u0001\u0000\u0000\u0000\u0186\u0187\u0005\t\u0000"+
		"\u0000\u0187#\u0001\u0000\u0000\u0000\u0188\u0189\u0005\u000b\u0000\u0000"+
		"\u0189\u018a\u0005\f\u0000\u0000\u018a\u018b\u0005\r\u0000\u0000\u018b"+
		"%\u0001\u0000\u0000\u0000\u018c\u0191\u0003(\u0014\u0000\u018d\u018e\u0005"+
		"\u0006\u0000\u0000\u018e\u0190\u0003(\u0014\u0000\u018f\u018d\u0001\u0000"+
		"\u0000\u0000\u0190\u0193\u0001\u0000\u0000\u0000\u0191\u018f\u0001\u0000"+
		"\u0000\u0000\u0191\u0192\u0001\u0000\u0000\u0000\u0192\'\u0001\u0000\u0000"+
		"\u0000\u0193\u0191\u0001\u0000\u0000\u0000\u0194\u0195\u0003:\u001d\u0000"+
		"\u0195\u0196\u0005\n\u0000\u0000\u0196\u0197\u0003 \u0010\u0000\u0197"+
		"\u01af\u0001\u0000\u0000\u0000\u0198\u0199\u0003:\u001d\u0000\u0199\u019a"+
		"\u0005\n\u0000\u0000\u019a\u019b\u0003`0\u0000\u019b\u01af\u0001\u0000"+
		"\u0000\u0000\u019c\u019d\u0005]\u0000\u0000\u019d\u019e\u0005\u0005\u0000"+
		"\u0000\u019e\u019f\u0003\u008eG\u0000\u019f\u01a0\u0005_\u0000\u0000\u01a0"+
		"\u01a1\u0003:\u001d\u0000\u01a1\u01a2\u0005\u0007\u0000\u0000\u01a2\u01a3"+
		"\u0005\n\u0000\u0000\u01a3\u01a4\u0003 \u0010\u0000\u01a4\u01af\u0001"+
		"\u0000\u0000\u0000\u01a5\u01a6\u0005]\u0000\u0000\u01a6\u01a7\u0005\u0005"+
		"\u0000\u0000\u01a7\u01a8\u0003\u008eG\u0000\u01a8\u01a9\u0005_\u0000\u0000"+
		"\u01a9\u01aa\u0003:\u001d\u0000\u01aa\u01ab\u0005\u0007\u0000\u0000\u01ab"+
		"\u01ac\u0005\n\u0000\u0000\u01ac\u01ad\u0003`0\u0000\u01ad\u01af\u0001"+
		"\u0000\u0000\u0000\u01ae\u0194\u0001\u0000\u0000\u0000\u01ae\u0198\u0001"+
		"\u0000\u0000\u0000\u01ae\u019c\u0001\u0000\u0000\u0000\u01ae\u01a5\u0001"+
		"\u0000\u0000\u0000\u01af)\u0001\u0000\u0000\u0000\u01b0\u01b2\u0003:\u001d"+
		"\u0000\u01b1\u01b0\u0001\u0000\u0000\u0000\u01b1\u01b2\u0001\u0000\u0000"+
		"\u0000\u01b2\u01b3\u0001\u0000\u0000\u0000\u01b3\u01b4\u0005\u0001\u0000"+
		"\u0000\u01b4\u01b5\u0003v;\u0000\u01b5\u01b6\u0005\u0005\u0000\u0000\u01b6"+
		"\u01b7\u0003\u0082A\u0000\u01b7\u01b8\u0005\u0007\u0000\u0000\u01b8\u01ba"+
		"\u0005\b\u0000\u0000\u01b9\u01bb\u0003,\u0016\u0000\u01ba\u01b9\u0001"+
		"\u0000\u0000\u0000\u01ba\u01bb\u0001\u0000\u0000\u0000\u01bb\u01bc\u0001"+
		"\u0000\u0000\u0000\u01bc\u01bd\u0005\t\u0000\u0000\u01bd+\u0001\u0000"+
		"\u0000\u0000\u01be\u01c3\u0003.\u0017\u0000\u01bf\u01c0\u0005\u0006\u0000"+
		"\u0000\u01c0\u01c2\u0003.\u0017\u0000\u01c1\u01bf\u0001\u0000\u0000\u0000"+
		"\u01c2\u01c5\u0001\u0000\u0000\u0000\u01c3\u01c1\u0001\u0000\u0000\u0000"+
		"\u01c3\u01c4\u0001\u0000\u0000\u0000\u01c4\u01cb\u0001\u0000\u0000\u0000"+
		"\u01c5\u01c3\u0001\u0000\u0000\u0000\u01c6\u01c9\u0005^\u0000\u0000\u01c7"+
		"\u01ca\u0003:\u001d\u0000\u01c8\u01ca\u0005`\u0000\u0000\u01c9\u01c7\u0001"+
		"\u0000\u0000\u0000\u01c9\u01c8\u0001\u0000\u0000\u0000\u01ca\u01cc\u0001"+
		"\u0000\u0000\u0000\u01cb\u01c6\u0001\u0000\u0000\u0000\u01cb\u01cc\u0001"+
		"\u0000\u0000\u0000\u01cc-\u0001\u0000\u0000\u0000\u01cd\u01d1\u0003:\u001d"+
		"\u0000\u01ce\u01d1\u0003 \u0010\u0000\u01cf\u01d1\u0005`\u0000\u0000\u01d0"+
		"\u01cd\u0001\u0000\u0000\u0000\u01d0\u01ce\u0001\u0000\u0000\u0000\u01d0"+
		"\u01cf\u0001\u0000\u0000\u0000\u01d1/\u0001\u0000\u0000\u0000\u01d2\u01d3"+
		"\u0005Y\u0000\u0000\u01d3\u01d9\u0005\b\u0000\u0000\u01d4\u01d5\u0003"+
		"4\u001a\u0000\u01d5\u01d6\u0005\u0003\u0000\u0000\u01d6\u01d8\u0001\u0000"+
		"\u0000\u0000\u01d7\u01d4\u0001\u0000\u0000\u0000\u01d8\u01db\u0001\u0000"+
		"\u0000\u0000\u01d9\u01d7\u0001\u0000\u0000\u0000\u01d9\u01da\u0001\u0000"+
		"\u0000\u0000\u01da\u01dc\u0001\u0000\u0000\u0000\u01db\u01d9\u0001\u0000"+
		"\u0000\u0000\u01dc\u01dd\u0005\t\u0000\u0000\u01dd1\u0001\u0000\u0000"+
		"\u0000\u01de\u01df\u0005Z\u0000\u0000\u01df\u01e5\u0005\b\u0000\u0000"+
		"\u01e0\u01e1\u00034\u001a\u0000\u01e1\u01e2\u0005\u0003\u0000\u0000\u01e2"+
		"\u01e4\u0001\u0000\u0000\u0000\u01e3\u01e0\u0001\u0000\u0000\u0000\u01e4"+
		"\u01e7\u0001\u0000\u0000\u0000\u01e5\u01e3\u0001\u0000\u0000\u0000\u01e5"+
		"\u01e6\u0001\u0000\u0000\u0000\u01e6\u01e8\u0001\u0000\u0000\u0000\u01e7"+
		"\u01e5\u0001\u0000\u0000\u0000\u01e8\u01e9\u0005\t\u0000\u0000\u01e93"+
		"\u0001\u0000\u0000\u0000\u01ea\u01ed\u0003 \u0010\u0000\u01eb\u01ed\u0003"+
		"`0\u0000\u01ec\u01ea\u0001\u0000\u0000\u0000\u01ec\u01eb\u0001\u0000\u0000"+
		"\u0000\u01ed5\u0001\u0000\u0000\u0000\u01ee\u01ef\u0005U\u0000\u0000\u01ef"+
		"\u01f0\u0003\u008eG\u0000\u01f0\u01f9\u0005\u0005\u0000\u0000\u01f1\u01f6"+
		"\u00038\u001c\u0000\u01f2\u01f3\u0005\u0006\u0000\u0000\u01f3\u01f5\u0003"+
		"8\u001c\u0000\u01f4\u01f2\u0001\u0000\u0000\u0000\u01f5\u01f8\u0001\u0000"+
		"\u0000\u0000\u01f6\u01f4\u0001\u0000\u0000\u0000\u01f6\u01f7\u0001\u0000"+
		"\u0000\u0000\u01f7\u01fa\u0001\u0000\u0000\u0000\u01f8\u01f6\u0001\u0000"+
		"\u0000\u0000\u01f9\u01f1\u0001\u0000\u0000\u0000\u01f9\u01fa\u0001\u0000"+
		"\u0000\u0000\u01fa\u01fb\u0001\u0000\u0000\u0000\u01fb\u01fc\u0005\u0007"+
		"\u0000\u0000\u01fc\u01fd\u0005\u0001\u0000\u0000\u01fd\u0203\u0003\u0082"+
		"A\u0000\u01fe\u0204\u0005\u0003\u0000\u0000\u01ff\u0200\u0005\b\u0000"+
		"\u0000\u0200\u0201\u0003`0\u0000\u0201\u0202\u0005\t\u0000\u0000\u0202"+
		"\u0204\u0001\u0000\u0000\u0000\u0203\u01fe\u0001\u0000\u0000\u0000\u0203"+
		"\u01ff\u0001\u0000\u0000\u0000\u02047\u0001\u0000\u0000\u0000\u0205\u0206"+
		"\u0003:\u001d\u0000\u0206\u0207\u0005\u0001\u0000\u0000\u0207\u0208\u0003"+
		"\u0082A\u0000\u02089\u0001\u0000\u0000\u0000\u0209\u020e\u0005e\u0000"+
		"\u0000\u020a\u020e\u0005d\u0000\u0000\u020b\u020e\u0003<\u001e\u0000\u020c"+
		"\u020e\u0003>\u001f\u0000\u020d\u0209\u0001\u0000\u0000\u0000\u020d\u020a"+
		"\u0001\u0000\u0000\u0000\u020d\u020b\u0001\u0000\u0000\u0000\u020d\u020c"+
		"\u0001\u0000\u0000\u0000\u020e;\u0001\u0000\u0000\u0000\u020f\u0210\u0007"+
		"\u0001\u0000\u0000\u0210=\u0001\u0000\u0000\u0000\u0211\u0212\u0007\u0002"+
		"\u0000\u0000\u0212?\u0001\u0000\u0000\u0000\u0213\u0214\u0003`0\u0000"+
		"\u0214\u0215\u0005\u0000\u0000\u0001\u0215A\u0001\u0000\u0000\u0000\u0216"+
		"\u0217\u0003D\"\u0000\u0217\u0218\u0005\u0000\u0000\u0001\u0218C\u0001"+
		"\u0000\u0000\u0000\u0219\u021b\u0003F#\u0000\u021a\u0219\u0001\u0000\u0000"+
		"\u0000\u021b\u021e\u0001\u0000\u0000\u0000\u021c\u021a\u0001\u0000\u0000"+
		"\u0000\u021c\u021d\u0001\u0000\u0000\u0000\u021d\u0223\u0001\u0000\u0000"+
		"\u0000\u021e\u021c\u0001\u0000\u0000\u0000\u021f\u0222\u0003H$\u0000\u0220"+
		"\u0222\u0003J%\u0000\u0221\u021f\u0001\u0000\u0000\u0000\u0221\u0220\u0001"+
		"\u0000\u0000\u0000\u0222\u0225\u0001\u0000\u0000\u0000\u0223\u0221\u0001"+
		"\u0000\u0000\u0000\u0223\u0224\u0001\u0000\u0000\u0000\u0224E\u0001\u0000"+
		"\u0000\u0000\u0225\u0223\u0001\u0000\u0000\u0000\u0226\u022a\u0007\u0003"+
		"\u0000\u0000\u0227\u0228\u0003:\u001d\u0000\u0228\u0229\u0005\u0001\u0000"+
		"\u0000\u0229\u022b\u0001\u0000\u0000\u0000\u022a\u0227\u0001\u0000\u0000"+
		"\u0000\u022a\u022b\u0001\u0000\u0000\u0000\u022b\u022c\u0001\u0000\u0000"+
		"\u0000\u022c\u022f\u0003\u008eG\u0000\u022d\u022e\u0005_\u0000\u0000\u022e"+
		"\u0230\u0005\u0002\u0000\u0000\u022f\u022d\u0001\u0000\u0000\u0000\u022f"+
		"\u0230\u0001\u0000\u0000\u0000\u0230G\u0001\u0000\u0000\u0000\u0231\u0232"+
		"\u0005\u0015\u0000\u0000\u0232\u0236\u0003\u008eG\u0000\u0233\u0235\u0003"+
		"J%\u0000\u0234\u0233\u0001\u0000\u0000\u0000\u0235\u0238\u0001\u0000\u0000"+
		"\u0000\u0236\u0234\u0001\u0000\u0000\u0000\u0236\u0237\u0001\u0000\u0000"+
		"\u0000\u0237\u0239\u0001\u0000\u0000\u0000\u0238\u0236\u0001\u0000\u0000"+
		"\u0000\u0239\u023a\u0005\u0012\u0000\u0000\u023aI\u0001\u0000\u0000\u0000"+
		"\u023b\u023f\u0003L&\u0000\u023c\u023f\u0003T*\u0000\u023d\u023f\u0003"+
		"X,\u0000\u023e\u023b\u0001\u0000\u0000\u0000\u023e\u023c\u0001\u0000\u0000"+
		"\u0000\u023e\u023d\u0001\u0000\u0000\u0000\u023fK\u0001\u0000\u0000\u0000"+
		"\u0240\u0244\u0005\u000f\u0000\u0000\u0241\u0242\u0003:\u001d\u0000\u0242"+
		"\u0243\u0005\u0001\u0000\u0000\u0243\u0245\u0001\u0000\u0000\u0000\u0244"+
		"\u0241\u0001\u0000\u0000\u0000\u0244\u0245\u0001\u0000\u0000\u0000\u0245"+
		"\u0246\u0001\u0000\u0000\u0000\u0246\u0248\u0003\u008eG\u0000\u0247\u0249"+
		"\u0003N\'\u0000\u0248\u0247\u0001\u0000\u0000\u0000\u0249\u024a\u0001"+
		"\u0000\u0000\u0000\u024a\u0248\u0001\u0000\u0000\u0000\u024a\u024b\u0001"+
		"\u0000\u0000\u0000\u024bM\u0001\u0000\u0000\u0000\u024c\u024f\u0003P("+
		"\u0000\u024d\u024f\u0003R)\u0000\u024e\u024c\u0001\u0000\u0000\u0000\u024e"+
		"\u024d\u0001\u0000\u0000\u0000\u024fO\u0001\u0000\u0000\u0000\u0250\u0258"+
		"\u0005\u0014\u0000\u0000\u0251\u0256\u0003:\u001d\u0000\u0252\u0253\u0005"+
		"\u0005\u0000\u0000\u0253\u0254\u0003`0\u0000\u0254\u0255\u0005\u0007\u0000"+
		"\u0000\u0255\u0257\u0001\u0000\u0000\u0000\u0256\u0252\u0001\u0000\u0000"+
		"\u0000\u0256\u0257\u0001\u0000\u0000\u0000\u0257\u0259\u0001\u0000\u0000"+
		"\u0000\u0258\u0251\u0001\u0000\u0000\u0000\u0258\u0259\u0001\u0000\u0000"+
		"\u0000\u0259\u025a\u0001\u0000\u0000\u0000\u025a\u025b\u0005\u0001\u0000"+
		"\u0000\u025b\u025c\u0003`0\u0000\u025cQ\u0001\u0000\u0000\u0000\u025d"+
		"\u025f\u0005\u0018\u0000\u0000\u025e\u025d\u0001\u0000\u0000\u0000\u025e"+
		"\u025f\u0001\u0000\u0000\u0000\u025f\u0260\u0001\u0000\u0000\u0000\u0260"+
		"\u0262\u0005\u0010\u0000\u0000\u0261\u0263\u0003:\u001d\u0000\u0262\u0261"+
		"\u0001\u0000\u0000\u0000\u0262\u0263\u0001\u0000\u0000\u0000\u0263\u0264"+
		"\u0001\u0000\u0000\u0000\u0264\u0276\u0005\u0001\u0000\u0000\u0265\u0266"+
		"\u0003:\u001d\u0000\u0266\u0267\u0005\u0001\u0000\u0000\u0267\u0268\u0003"+
		"\u0082A\u0000\u0268\u0269\u0005\n\u0000\u0000\u0269\u026a\u0003`0\u0000"+
		"\u026a\u0277\u0001\u0000\u0000\u0000\u026b\u026c\u0003:\u001d\u0000\u026c"+
		"\u026e\u0005\u0005\u0000\u0000\u026d\u026f\u0003\\.\u0000\u026e\u026d"+
		"\u0001\u0000\u0000\u0000\u026e\u026f\u0001\u0000\u0000\u0000\u026f\u0270"+
		"\u0001\u0000\u0000\u0000\u0270\u0271\u0005\u0007\u0000\u0000\u0271\u0272"+
		"\u0005\u0001\u0000\u0000\u0272\u0273\u0003\u0082A\u0000\u0273\u0274\u0005"+
		"\n\u0000\u0000\u0274\u0275\u0003`0\u0000\u0275\u0277\u0001\u0000\u0000"+
		"\u0000\u0276\u0265\u0001\u0000\u0000\u0000\u0276\u026b\u0001\u0000\u0000"+
		"\u0000\u0277S\u0001\u0000\u0000\u0000\u0278\u027c\u0005\u000f\u0000\u0000"+
		"\u0279\u027a\u0003\u008eG\u0000\u027a\u027b\u0005_\u0000\u0000\u027b\u027d"+
		"\u0001\u0000\u0000\u0000\u027c\u0279\u0001\u0000\u0000\u0000\u027c\u027d"+
		"\u0001\u0000\u0000\u0000\u027d\u027e\u0001\u0000\u0000\u0000\u027e\u027f"+
		"\u0003:\u001d\u0000\u027f\u0281\u0005\u0005\u0000\u0000\u0280\u0282\u0003"+
		"\\.\u0000\u0281\u0280\u0001\u0000\u0000\u0000\u0281\u0282\u0001\u0000"+
		"\u0000\u0000\u0282\u0283\u0001\u0000\u0000\u0000\u0283\u0286\u0005\u0007"+
		"\u0000\u0000\u0284\u0285\u0005\u0001\u0000\u0000\u0285\u0287\u0003\u0082"+
		"A\u0000\u0286\u0284\u0001\u0000\u0000\u0000\u0286\u0287\u0001\u0000\u0000"+
		"\u0000\u0287\u0289\u0001\u0000\u0000\u0000\u0288\u028a\u0003V+\u0000\u0289"+
		"\u0288\u0001\u0000\u0000\u0000\u028a\u028b\u0001\u0000\u0000\u0000\u028b"+
		"\u0289\u0001\u0000\u0000\u0000\u028b\u028c\u0001\u0000\u0000\u0000\u028c"+
		"U\u0001\u0000\u0000\u0000\u028d\u028f\u0005\u0017\u0000\u0000\u028e\u0290"+
		"\u0003:\u001d\u0000\u028f\u028e\u0001\u0000\u0000\u0000\u028f\u0290\u0001"+
		"\u0000\u0000\u0000\u0290\u0291\u0001\u0000\u0000\u0000\u0291\u0292\u0005"+
		"\u0001\u0000\u0000\u0292\u02a0\u0003`0\u0000\u0293\u0295\u0005\u0016\u0000"+
		"\u0000\u0294\u0296\u0003:\u001d\u0000\u0295\u0294\u0001\u0000\u0000\u0000"+
		"\u0295\u0296\u0001\u0000\u0000\u0000\u0296\u0297\u0001\u0000\u0000\u0000"+
		"\u0297\u0298\u0005\u0001\u0000\u0000\u0298\u02a0\u0003`0\u0000\u0299\u029b"+
		"\u0005\u000e\u0000\u0000\u029a\u029c\u0003:\u001d\u0000\u029b\u029a\u0001"+
		"\u0000\u0000\u0000\u029b\u029c\u0001\u0000\u0000\u0000\u029c\u029d\u0001"+
		"\u0000\u0000\u0000\u029d\u029e\u0005\u0001\u0000\u0000\u029e\u02a0\u0003"+
		"`0\u0000\u029f\u028d\u0001\u0000\u0000\u0000\u029f\u0293\u0001\u0000\u0000"+
		"\u0000\u029f\u0299\u0001\u0000\u0000\u0000\u02a0W\u0001\u0000\u0000\u0000"+
		"\u02a1\u02a2\u0005\u000f\u0000\u0000\u02a2\u02a3\u0003\u008eG\u0000\u02a3"+
		"\u02a4\u0005_\u0000\u0000\u02a4\u02a5\u0003:\u001d\u0000\u02a5\u02a6\u0005"+
		"\u0001\u0000\u0000\u02a6\u02a8\u0003\u0082A\u0000\u02a7\u02a9\u0003Z-"+
		"\u0000\u02a8\u02a7\u0001\u0000\u0000\u0000\u02a9\u02aa\u0001\u0000\u0000"+
		"\u0000\u02aa\u02a8\u0001\u0000\u0000\u0000\u02aa\u02ab\u0001\u0000\u0000"+
		"\u0000\u02abY\u0001\u0000\u0000\u0000\u02ac\u02ae\u0005\u0013\u0000\u0000"+
		"\u02ad\u02af\u0003:\u001d\u0000\u02ae\u02ad\u0001\u0000\u0000\u0000\u02ae"+
		"\u02af\u0001\u0000\u0000\u0000\u02af\u02b0\u0001\u0000\u0000\u0000\u02b0"+
		"\u02b1\u0005\u0001\u0000\u0000\u02b1\u02b9\u0003`0\u0000\u02b2\u02b4\u0005"+
		"\u0011\u0000\u0000\u02b3\u02b5\u0003:\u001d\u0000\u02b4\u02b3\u0001\u0000"+
		"\u0000\u0000\u02b4\u02b5\u0001\u0000\u0000\u0000\u02b5\u02b6\u0001\u0000"+
		"\u0000\u0000\u02b6\u02b7\u0005\u0001\u0000\u0000\u02b7\u02b9\u0003`0\u0000"+
		"\u02b8\u02ac\u0001\u0000\u0000\u0000\u02b8\u02b2\u0001\u0000\u0000\u0000"+
		"\u02b9[\u0001\u0000\u0000\u0000\u02ba\u02bf\u0003^/\u0000\u02bb\u02bc"+
		"\u0005\u0006\u0000\u0000\u02bc\u02be\u0003^/\u0000\u02bd\u02bb\u0001\u0000"+
		"\u0000\u0000\u02be\u02c1\u0001\u0000\u0000\u0000\u02bf\u02bd\u0001\u0000"+
		"\u0000\u0000\u02bf\u02c0\u0001\u0000\u0000\u0000\u02c0]\u0001\u0000\u0000"+
		"\u0000\u02c1\u02bf\u0001\u0000\u0000\u0000\u02c2\u02c3\u0003:\u001d\u0000"+
		"\u02c3\u02c4\u0005\u0001\u0000\u0000\u02c4\u02c5\u0003\u0082A\u0000\u02c5"+
		"_\u0001\u0000\u0000\u0000\u02c6\u02c7\u00060\uffff\uffff\u0000\u02c7\u02c8"+
		"\u0005\u001e\u0000\u0000\u02c8\u02cd\u0003`0\u000b\u02c9\u02ca\u0005\u001f"+
		"\u0000\u0000\u02ca\u02cd\u0003`0\n\u02cb\u02cd\u0003b1\u0000\u02cc\u02c6"+
		"\u0001\u0000\u0000\u0000\u02cc\u02c9\u0001\u0000\u0000\u0000\u02cc\u02cb"+
		"\u0001\u0000\u0000\u0000\u02cd\u02ee\u0001\u0000\u0000\u0000\u02ce\u02cf"+
		"\n\t\u0000\u0000\u02cf\u02d0\u0007\u0004\u0000\u0000\u02d0\u02ed\u0003"+
		"`0\n\u02d1\u02d2\n\b\u0000\u0000\u02d2\u02d3\u0007\u0005\u0000\u0000\u02d3"+
		"\u02ed\u0003`0\t\u02d4\u02d5\n\u0007\u0000\u0000\u02d5\u02d6\u0007\u0006"+
		"\u0000\u0000\u02d6\u02ed\u0003`0\b\u02d7\u02d8\n\u0006\u0000\u0000\u02d8"+
		"\u02d9\u0007\u0007\u0000\u0000\u02d9\u02ed\u0003`0\u0007\u02da\u02db\n"+
		"\u0005\u0000\u0000\u02db\u02dc\u0005\'\u0000\u0000\u02dc\u02ed\u0003`"+
		"0\u0006\u02dd\u02de\n\u0004\u0000\u0000\u02de\u02df\u0005(\u0000\u0000"+
		"\u02df\u02ed\u0003`0\u0005\u02e0\u02e1\n\u0003\u0000\u0000\u02e1\u02e2"+
		"\u0005)\u0000\u0000\u02e2\u02ed\u0003`0\u0004\u02e3\u02e4\n\u0002\u0000"+
		"\u0000\u02e4\u02e5\u0005*\u0000\u0000\u02e5\u02ed\u0003`0\u0003\u02e6"+
		"\u02e7\n\r\u0000\u0000\u02e7\u02e8\u0007\b\u0000\u0000\u02e8\u02ed\u0003"+
		"d2\u0000\u02e9\u02ea\n\f\u0000\u0000\u02ea\u02eb\u0007\t\u0000\u0000\u02eb"+
		"\u02ed\u0003f3\u0000\u02ec\u02ce\u0001\u0000\u0000\u0000\u02ec\u02d1\u0001"+
		"\u0000\u0000\u0000\u02ec\u02d4\u0001\u0000\u0000\u0000\u02ec\u02d7\u0001"+
		"\u0000\u0000\u0000\u02ec\u02da\u0001\u0000\u0000\u0000\u02ec\u02dd\u0001"+
		"\u0000\u0000\u0000\u02ec\u02e0\u0001\u0000\u0000\u0000\u02ec\u02e3\u0001"+
		"\u0000\u0000\u0000\u02ec\u02e6\u0001\u0000\u0000\u0000\u02ec\u02e9\u0001"+
		"\u0000\u0000\u0000\u02ed\u02f0\u0001\u0000\u0000\u0000\u02ee\u02ec\u0001"+
		"\u0000\u0000\u0000\u02ee\u02ef\u0001\u0000\u0000\u0000\u02efa\u0001\u0000"+
		"\u0000\u0000\u02f0\u02ee\u0001\u0000\u0000\u0000\u02f1\u02f2\u0005\u0005"+
		"\u0000\u0000\u02f2\u02f3\u0003`0\u0000\u02f3\u02f4\u0005\u0007\u0000\u0000"+
		"\u02f4\u0327\u0001\u0000\u0000\u0000\u02f5\u0327\u0005+\u0000\u0000\u02f6"+
		"\u02f7\u0005,\u0000\u0000\u02f7\u02f8\u0003`0\u0000\u02f8\u02f9\u0005"+
		"-\u0000\u0000\u02f9\u0301\u0003`0\u0000\u02fa\u02fb\u0005.\u0000\u0000"+
		"\u02fb\u02fc\u0003`0\u0000\u02fc\u02fd\u0005-\u0000\u0000\u02fd\u02fe"+
		"\u0003`0\u0000\u02fe\u0300\u0001\u0000\u0000\u0000\u02ff\u02fa\u0001\u0000"+
		"\u0000\u0000\u0300\u0303\u0001\u0000\u0000\u0000\u0301\u02ff\u0001\u0000"+
		"\u0000\u0000\u0301\u0302\u0001\u0000\u0000\u0000\u0302\u0304\u0001\u0000"+
		"\u0000\u0000\u0303\u0301\u0001\u0000\u0000\u0000\u0304\u0305\u0005/\u0000"+
		"\u0000\u0305\u0306\u0003`0\u0000\u0306\u0307\u00050\u0000\u0000\u0307"+
		"\u0327\u0001\u0000\u0000\u0000\u0308\u0309\u00051\u0000\u0000\u0309\u030e"+
		"\u0003l6\u0000\u030a\u030b\u0005\u0006\u0000\u0000\u030b\u030d\u0003l"+
		"6\u0000\u030c\u030a\u0001\u0000\u0000\u0000\u030d\u0310\u0001\u0000\u0000"+
		"\u0000\u030e\u030c\u0001\u0000\u0000\u0000\u030e\u030f\u0001\u0000\u0000"+
		"\u0000\u030f\u0311\u0001\u0000\u0000\u0000\u0310\u030e\u0001\u0000\u0000"+
		"\u0000\u0311\u0312\u00052\u0000\u0000\u0312\u0313\u0003`0\u0000\u0313"+
		"\u0327\u0001\u0000\u0000\u0000\u0314\u0327\u0003p8\u0000\u0315\u0317\u0003"+
		"\u008eG\u0000\u0316\u0318\u0003n7\u0000\u0317\u0316\u0001\u0000\u0000"+
		"\u0000\u0317\u0318\u0001\u0000\u0000\u0000\u0318\u0319\u0001\u0000\u0000"+
		"\u0000\u0319\u031b\u0005\u0005\u0000\u0000\u031a\u031c\u0003j5\u0000\u031b"+
		"\u031a\u0001\u0000\u0000\u0000\u031b\u031c\u0001\u0000\u0000\u0000\u031c"+
		"\u031d\u0001\u0000\u0000\u0000\u031d\u031f\u0005\u0007\u0000\u0000\u031e"+
		"\u0320\u0003n7\u0000\u031f\u031e\u0001\u0000\u0000\u0000\u031f\u0320\u0001"+
		"\u0000\u0000\u0000\u0320\u0327\u0001\u0000\u0000\u0000\u0321\u0327\u0003"+
		"\u0084B\u0000\u0322\u0327\u0003\u0086C\u0000\u0323\u0327\u0003\u0088D"+
		"\u0000\u0324\u0327\u0003\u008aE\u0000\u0325\u0327\u0003\u008eG\u0000\u0326"+
		"\u02f1\u0001\u0000\u0000\u0000\u0326\u02f5\u0001\u0000\u0000\u0000\u0326"+
		"\u02f6\u0001\u0000\u0000\u0000\u0326\u0308\u0001\u0000\u0000\u0000\u0326"+
		"\u0314\u0001\u0000\u0000\u0000\u0326\u0315\u0001\u0000\u0000\u0000\u0326"+
		"\u0321\u0001\u0000\u0000\u0000\u0326\u0322\u0001\u0000\u0000\u0000\u0326"+
		"\u0323\u0001\u0000\u0000\u0000\u0326\u0324\u0001\u0000\u0000\u0000\u0326"+
		"\u0325\u0001\u0000\u0000\u0000\u0327c\u0001\u0000\u0000\u0000\u0328\u0329"+
		"\u0003\u008eG\u0000\u0329\u032a\u0005_\u0000\u0000\u032a\u032c\u0001\u0000"+
		"\u0000\u0000\u032b\u0328\u0001\u0000\u0000\u0000\u032b\u032c\u0001\u0000"+
		"\u0000\u0000\u032c\u032d\u0001\u0000\u0000\u0000\u032d\u032f\u0003:\u001d"+
		"\u0000\u032e\u0330\u0003n7\u0000\u032f\u032e\u0001\u0000\u0000\u0000\u032f"+
		"\u0330\u0001\u0000\u0000\u0000\u0330\u0331\u0001\u0000\u0000\u0000\u0331"+
		"\u0333\u0005\u0005\u0000\u0000\u0332\u0334\u0003j5\u0000\u0333\u0332\u0001"+
		"\u0000\u0000\u0000\u0333\u0334\u0001\u0000\u0000\u0000\u0334\u0335\u0001"+
		"\u0000\u0000\u0000\u0335\u0337\u0005\u0007\u0000\u0000\u0336\u0338\u0003"+
		"n7\u0000\u0337\u0336\u0001\u0000\u0000\u0000\u0337\u0338\u0001\u0000\u0000"+
		"\u0000\u0338\u0343\u0001\u0000\u0000\u0000\u0339\u033a\u0003\u008eG\u0000"+
		"\u033a\u033b\u0005_\u0000\u0000\u033b\u033d\u0001\u0000\u0000\u0000\u033c"+
		"\u0339\u0001\u0000\u0000\u0000\u033c\u033d\u0001\u0000\u0000\u0000\u033d"+
		"\u033e\u0001\u0000\u0000\u0000\u033e\u0340\u0003:\u001d\u0000\u033f\u0341"+
		"\u0003n7\u0000\u0340\u033f\u0001\u0000\u0000\u0000\u0340\u0341\u0001\u0000"+
		"\u0000\u0000\u0341\u0343\u0001\u0000\u0000\u0000\u0342\u032b\u0001\u0000"+
		"\u0000\u0000\u0342\u033c\u0001\u0000\u0000\u0000\u0343e\u0001\u0000\u0000"+
		"\u0000\u0344\u0345\u0003:\u001d\u0000\u0345\u0346\u0005\u0005\u0000\u0000"+
		"\u0346\u0347\u0003h4\u0000\u0347\u0348\u00053\u0000\u0000\u0348\u0349"+
		"\u0003`0\u0000\u0349\u034a\u0005\u0007\u0000\u0000\u034a\u0369\u0001\u0000"+
		"\u0000\u0000\u034b\u034c\u0003:\u001d\u0000\u034c\u0354\u0005\u0005\u0000"+
		"\u0000\u034d\u0350\u0003:\u001d\u0000\u034e\u034f\u0005\u0001\u0000\u0000"+
		"\u034f\u0351\u0003\u0082A\u0000\u0350\u034e\u0001\u0000\u0000\u0000\u0350"+
		"\u0351\u0001\u0000\u0000\u0000\u0351\u0352\u0001\u0000\u0000\u0000\u0352"+
		"\u0353\u0005\u0003\u0000\u0000\u0353\u0355\u0001\u0000\u0000\u0000\u0354"+
		"\u034d\u0001\u0000\u0000\u0000\u0354\u0355\u0001\u0000\u0000\u0000\u0355"+
		"\u0356\u0001\u0000\u0000\u0000\u0356\u0359\u0003:\u001d\u0000\u0357\u0358"+
		"\u0005\u0001\u0000\u0000\u0358\u035a\u0003\u0082A\u0000\u0359\u0357\u0001"+
		"\u0000\u0000\u0000\u0359\u035a\u0001\u0000\u0000\u0000\u035a\u035b\u0001"+
		"\u0000\u0000\u0000\u035b\u035c\u0005\n\u0000\u0000\u035c\u035d\u0003`"+
		"0\u0000\u035d\u035e\u00053\u0000\u0000\u035e\u035f\u0003`0\u0000\u035f"+
		"\u0360\u0005\u0007\u0000\u0000\u0360\u0369\u0001\u0000\u0000\u0000\u0361"+
		"\u0362\u0003:\u001d\u0000\u0362\u0364\u0005\u0005\u0000\u0000\u0363\u0365"+
		"\u0003j5\u0000\u0364\u0363\u0001\u0000\u0000\u0000\u0364\u0365\u0001\u0000"+
		"\u0000\u0000\u0365\u0366\u0001\u0000\u0000\u0000\u0366\u0367\u0005\u0007"+
		"\u0000\u0000\u0367\u0369\u0001\u0000\u0000\u0000\u0368\u0344\u0001\u0000"+
		"\u0000\u0000\u0368\u034b\u0001\u0000\u0000\u0000\u0368\u0361\u0001\u0000"+
		"\u0000\u0000\u0369g\u0001\u0000\u0000\u0000\u036a\u036d\u0003:\u001d\u0000"+
		"\u036b\u036c\u0005\u0001\u0000\u0000\u036c\u036e\u0003\u0082A\u0000\u036d"+
		"\u036b\u0001\u0000\u0000\u0000\u036d\u036e\u0001\u0000\u0000\u0000\u036e"+
		"\u0377\u0001\u0000\u0000\u0000\u036f\u0370\u0005\u0006\u0000\u0000\u0370"+
		"\u0373\u0003:\u001d\u0000\u0371\u0372\u0005\u0001\u0000\u0000\u0372\u0374"+
		"\u0003\u0082A\u0000\u0373\u0371\u0001\u0000\u0000\u0000\u0373\u0374\u0001"+
		"\u0000\u0000\u0000\u0374\u0376\u0001\u0000\u0000\u0000\u0375\u036f\u0001"+
		"\u0000\u0000\u0000\u0376\u0379\u0001\u0000\u0000\u0000\u0377\u0375\u0001"+
		"\u0000\u0000\u0000\u0377\u0378\u0001\u0000\u0000\u0000\u0378i\u0001\u0000"+
		"\u0000\u0000\u0379\u0377\u0001\u0000\u0000\u0000\u037a\u037f\u0003`0\u0000"+
		"\u037b\u037c\u0005\u0006\u0000\u0000\u037c\u037e\u0003`0\u0000\u037d\u037b"+
		"\u0001\u0000\u0000\u0000\u037e\u0381\u0001\u0000\u0000\u0000\u037f\u037d"+
		"\u0001\u0000\u0000\u0000\u037f\u0380\u0001\u0000\u0000\u0000\u0380k\u0001"+
		"\u0000\u0000\u0000\u0381\u037f\u0001\u0000\u0000\u0000\u0382\u0385\u0003"+
		":\u001d\u0000\u0383\u0384\u0005\u0001\u0000\u0000\u0384\u0386\u0003\u0082"+
		"A\u0000\u0385\u0383\u0001\u0000\u0000\u0000\u0385\u0386\u0001\u0000\u0000"+
		"\u0000\u0386\u0387\u0001\u0000\u0000\u0000\u0387\u0388\u0005\n\u0000\u0000"+
		"\u0388\u0389\u0003`0\u0000\u0389m\u0001\u0000\u0000\u0000\u038a\u038b"+
		"\u00054\u0000\u0000\u038b\u038c\u0005\u0017\u0000\u0000\u038co\u0001\u0000"+
		"\u0000\u0000\u038d\u0399\u0005b\u0000\u0000\u038e\u0399\u0005a\u0000\u0000"+
		"\u038f\u0399\u0003r9\u0000\u0390\u0399\u00055\u0000\u0000\u0391\u0399"+
		"\u00056\u0000\u0000\u0392\u0399\u00057\u0000\u0000\u0393\u0399\u00058"+
		"\u0000\u0000\u0394\u0399\u0005\u0002\u0000\u0000\u0395\u0399\u0003t:\u0000"+
		"\u0396\u0399\u0003z=\u0000\u0397\u0399\u0003~?\u0000\u0398\u038d\u0001"+
		"\u0000\u0000\u0000\u0398\u038e\u0001\u0000\u0000\u0000\u0398\u038f\u0001"+
		"\u0000\u0000\u0000\u0398\u0390\u0001\u0000\u0000\u0000\u0398\u0391\u0001"+
		"\u0000\u0000\u0000\u0398\u0392\u0001\u0000\u0000\u0000\u0398\u0393\u0001"+
		"\u0000\u0000\u0000\u0398\u0394\u0001\u0000\u0000\u0000\u0398\u0395\u0001"+
		"\u0000\u0000\u0000\u0398\u0396\u0001\u0000\u0000\u0000\u0398\u0397\u0001"+
		"\u0000\u0000\u0000\u0399q\u0001\u0000\u0000\u0000\u039a\u039c\u0005c\u0000"+
		"\u0000\u039b\u039a\u0001\u0000\u0000\u0000\u039c\u039d\u0001\u0000\u0000"+
		"\u0000\u039d\u039b\u0001\u0000\u0000\u0000\u039d\u039e\u0001\u0000\u0000"+
		"\u0000\u039es\u0001\u0000\u0000\u0000\u039f\u03a0\u0003v;\u0000\u03a0"+
		"\u03a9\u0005\b\u0000\u0000\u03a1\u03a6\u0003x<\u0000\u03a2\u03a3\u0005"+
		"\u0006\u0000\u0000\u03a3\u03a5\u0003x<\u0000\u03a4\u03a2\u0001\u0000\u0000"+
		"\u0000\u03a5\u03a8\u0001\u0000\u0000\u0000\u03a6\u03a4\u0001\u0000\u0000"+
		"\u0000\u03a6\u03a7\u0001\u0000\u0000\u0000\u03a7\u03aa\u0001\u0000\u0000"+
		"\u0000\u03a8\u03a6\u0001\u0000\u0000\u0000\u03a9\u03a1\u0001\u0000\u0000"+
		"\u0000\u03a9\u03aa\u0001\u0000\u0000\u0000\u03aa\u03ab\u0001\u0000\u0000"+
		"\u0000\u03ab\u03ac\u0005\t\u0000\u0000\u03acu\u0001\u0000\u0000\u0000"+
		"\u03ad\u03ae\u0007\n\u0000\u0000\u03aew\u0001\u0000\u0000\u0000\u03af"+
		"\u03b2\u0003`0\u0000\u03b0\u03b1\u0005>\u0000\u0000\u03b1\u03b3\u0003"+
		"`0\u0000\u03b2\u03b0\u0001\u0000\u0000\u0000\u03b2\u03b3\u0001\u0000\u0000"+
		"\u0000\u03b3y\u0001\u0000\u0000\u0000\u03b4\u03b5\u0005?\u0000\u0000\u03b5"+
		"\u03b6\u0005\b\u0000\u0000\u03b6\u03bb\u0003|>\u0000\u03b7\u03b8\u0005"+
		"\u0006\u0000\u0000\u03b8\u03ba\u0003|>\u0000\u03b9\u03b7\u0001\u0000\u0000"+
		"\u0000\u03ba\u03bd\u0001\u0000\u0000\u0000\u03bb\u03b9\u0001\u0000\u0000"+
		"\u0000\u03bb\u03bc\u0001\u0000\u0000\u0000\u03bc\u03be\u0001\u0000\u0000"+
		"\u0000\u03bd\u03bb\u0001\u0000\u0000\u0000\u03be\u03bf\u0005\t\u0000\u0000"+
		"\u03bf{\u0001\u0000\u0000\u0000\u03c0\u03c3\u0003:\u001d\u0000\u03c1\u03c2"+
		"\u0005\u0001\u0000\u0000\u03c2\u03c4\u0003\u0082A\u0000\u03c3\u03c1\u0001"+
		"\u0000\u0000\u0000\u03c3\u03c4\u0001\u0000\u0000\u0000\u03c4\u03c5\u0001"+
		"\u0000\u0000\u0000\u03c5\u03c6\u0005\n\u0000\u0000\u03c6\u03c7\u0003`"+
		"0\u0000\u03c7}\u0001\u0000\u0000\u0000\u03c8\u03c9\u0005@\u0000\u0000"+
		"\u03c9\u03d2\u0005\b\u0000\u0000\u03ca\u03cf\u0003\u0080@\u0000\u03cb"+
		"\u03cc\u0005\u0006\u0000\u0000\u03cc\u03ce\u0003\u0080@\u0000\u03cd\u03cb"+
		"\u0001\u0000\u0000\u0000\u03ce\u03d1\u0001\u0000\u0000\u0000\u03cf\u03cd"+
		"\u0001\u0000\u0000\u0000\u03cf\u03d0\u0001\u0000\u0000\u0000\u03d0\u03d3"+
		"\u0001\u0000\u0000\u0000\u03d1\u03cf\u0001\u0000\u0000\u0000\u03d2\u03ca"+
		"\u0001\u0000\u0000\u0000\u03d2\u03d3\u0001\u0000\u0000\u0000\u03d3\u03d4"+
		"\u0001\u0000\u0000\u0000\u03d4\u03d5\u0005\t\u0000\u0000\u03d5\u007f\u0001"+
		"\u0000\u0000\u0000\u03d6\u03d7\u0003`0\u0000\u03d7\u03d8\u0007\u000b\u0000"+
		"\u0000\u03d8\u03d9\u0003`0\u0000\u03d9\u0081\u0001\u0000\u0000\u0000\u03da"+
		"\u03e0\u0003\u0084B\u0000\u03db\u03e0\u0003\u0086C\u0000\u03dc\u03e0\u0003"+
		"\u0088D\u0000\u03dd\u03e0\u0003\u008aE\u0000\u03de\u03e0\u0003\u008eG"+
		"\u0000\u03df\u03da\u0001\u0000\u0000\u0000\u03df\u03db\u0001\u0000\u0000"+
		"\u0000\u03df\u03dc\u0001\u0000\u0000\u0000\u03df\u03dd\u0001\u0000\u0000"+
		"\u0000\u03df\u03de\u0001\u0000\u0000\u0000\u03e0\u0083\u0001\u0000\u0000"+
		"\u0000\u03e1\u03e2\u0007\f\u0000\u0000\u03e2\u0085\u0001\u0000\u0000\u0000"+
		"\u03e3\u03e4\u0003v;\u0000\u03e4\u03e5\u0005\u0005\u0000\u0000\u03e5\u03e6"+
		"\u0003\u0082A\u0000\u03e6\u03e7\u0005\u0007\u0000\u0000\u03e7\u0087\u0001"+
		"\u0000\u0000\u0000\u03e8\u03e9\u0005@\u0000\u0000\u03e9\u03ea\u0005\u0005"+
		"\u0000\u0000\u03ea\u03eb\u0003\u0082A\u0000\u03eb\u03ec\u0005\u0006\u0000"+
		"\u0000\u03ec\u03ed\u0003\u0082A\u0000\u03ed\u03ee\u0005\u0007\u0000\u0000"+
		"\u03ee\u0089\u0001\u0000\u0000\u0000\u03ef\u03f0\u0005?\u0000\u0000\u03f0"+
		"\u03f1\u0005\u0005\u0000\u0000\u03f1\u03f6\u0003\u008cF\u0000\u03f2\u03f3"+
		"\u0005\u0006\u0000\u0000\u03f3\u03f5\u0003\u008cF\u0000\u03f4\u03f2\u0001"+
		"\u0000\u0000\u0000\u03f5\u03f8\u0001\u0000\u0000\u0000\u03f6\u03f4\u0001"+
		"\u0000\u0000\u0000\u03f6\u03f7\u0001\u0000\u0000\u0000\u03f7\u03f9\u0001"+
		"\u0000\u0000\u0000\u03f8\u03f6\u0001\u0000\u0000\u0000\u03f9\u03fa\u0005"+
		"\u0007\u0000\u0000\u03fa\u008b\u0001\u0000\u0000\u0000\u03fb\u03fc\u0003"+
		":\u001d\u0000\u03fc\u03fd\u0005\u0001\u0000\u0000\u03fd\u03fe\u0003\u0082"+
		"A\u0000\u03fe\u008d\u0001\u0000\u0000\u0000\u03ff\u0407\u0003:\u001d\u0000"+
		"\u0400\u0403\u0005_\u0000\u0000\u0401\u0404\u0003:\u001d\u0000\u0402\u0404"+
		"\u0003\u0090H\u0000\u0403\u0401\u0001\u0000\u0000\u0000\u0403\u0402\u0001"+
		"\u0000\u0000\u0000\u0404\u0406\u0001\u0000\u0000\u0000\u0405\u0400\u0001"+
		"\u0000\u0000\u0000\u0406\u0409\u0001\u0000\u0000\u0000\u0407\u0405\u0001"+
		"\u0000\u0000\u0000\u0407\u0408\u0001\u0000\u0000\u0000\u0408\u008f\u0001"+
		"\u0000\u0000\u0000\u0409\u0407\u0001\u0000\u0000\u0000\u040a\u040b\u0007"+
		"\r\u0000\u0000\u040b\u0091\u0001\u0000\u0000\u0000\u040c\u040d\u0007\u000e"+
		"\u0000\u0000\u040d\u0093\u0001\u0000\u0000\u0000x\u009a\u00a0\u00a7\u00ae"+
		"\u00b3\u00b9\u00c0\u00cb\u00d1\u00d7\u00dc\u00de\u00ec\u00f1\u00fd\u010b"+
		"\u010e\u0111\u0117\u011d\u0122\u0124\u0127\u012a\u0133\u013a\u013f\u0148"+
		"\u014f\u0153\u015a\u015e\u0172\u0178\u017b\u0180\u0184\u0191\u01ae\u01b1"+
		"\u01ba\u01c3\u01c9\u01cb\u01d0\u01d9\u01e5\u01ec\u01f6\u01f9\u0203\u020d"+
		"\u021c\u0221\u0223\u022a\u022f\u0236\u023e\u0244\u024a\u024e\u0256\u0258"+
		"\u025e\u0262\u026e\u0276\u027c\u0281\u0286\u028b\u028f\u0295\u029b\u029f"+
		"\u02aa\u02ae\u02b4\u02b8\u02bf\u02cc\u02ec\u02ee\u0301\u030e\u0317\u031b"+
		"\u031f\u0326\u032b\u032f\u0333\u0337\u033c\u0340\u0342\u0350\u0354\u0359"+
		"\u0364\u0368\u036d\u0373\u0377\u037f\u0385\u0398\u039d\u03a6\u03a9\u03b2"+
		"\u03bb\u03c3\u03cf\u03d2\u03df\u03f6\u0403\u0407";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}