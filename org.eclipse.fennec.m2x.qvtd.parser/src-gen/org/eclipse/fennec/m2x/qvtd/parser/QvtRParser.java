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
		RULE_qualifiedName = 3, RULE_transformationDef = 4, RULE_modelDecl = 5, 
		RULE_metaModelId = 6, RULE_keyDecl = 7, RULE_keyProperty = 8, RULE_relation = 9, 
		RULE_varDeclaration = 10, RULE_domain = 11, RULE_primitiveTypeDomain = 12, 
		RULE_checkEnforceQualifier = 13, RULE_assignmentExp = 14, RULE_template = 15, 
		RULE_objectTemplate = 16, RULE_optionalMultiplicity = 17, RULE_propertyTemplateList = 18, 
		RULE_propertyTemplate = 19, RULE_collectionTemplate = 20, RULE_memberSelection = 21, 
		RULE_memberItem = 22, RULE_whenClause = 23, RULE_whereClause = 24, RULE_queryDef = 25, 
		RULE_paramDecl = 26, RULE_expressionEntry = 27, RULE_completeOclDocumentEntry = 28, 
		RULE_completeOclDocument = 29, RULE_importDeclaration = 30, RULE_packageDeclaration = 31, 
		RULE_contextDeclaration = 32, RULE_classifierContextDeclaration = 33, 
		RULE_classifierContextBody = 34, RULE_invariantConstraint = 35, RULE_definitionConstraint = 36, 
		RULE_operationContextDeclaration = 37, RULE_operationContextBody = 38, 
		RULE_propertyContextDeclaration = 39, RULE_propertyContextBody = 40, RULE_parameterList = 41, 
		RULE_parameter = 42, RULE_expression = 43, RULE_primaryExpression = 44, 
		RULE_propertyOrCallSuffix = 45, RULE_iteratorOrOperationCall = 46, RULE_iteratorVariables = 47, 
		RULE_argumentList = 48, RULE_letBinding = 49, RULE_isMarkedPre = 50, RULE_literalExpression = 51, 
		RULE_stringLiteral_ = 52, RULE_collectionLiteral = 53, RULE_collectionKind = 54, 
		RULE_collectionLiteralPart = 55, RULE_tupleLiteral = 56, RULE_tupleLiteralPart = 57, 
		RULE_mapLiteral = 58, RULE_mapLiteralPart = 59, RULE_typeExpression = 60, 
		RULE_primitiveType = 61, RULE_collectionType = 62, RULE_mapType = 63, 
		RULE_tupleType = 64, RULE_tupleTypePart = 65, RULE_pathName = 66, RULE_restrictedWord = 67, 
		RULE_identifier = 68, RULE_documentKeyword = 69;
	private static String[] makeRuleNames() {
		return new String[] {
			"compilationUnitEntry", "compilationUnit", "importDecl", "qualifiedName", 
			"transformationDef", "modelDecl", "metaModelId", "keyDecl", "keyProperty", 
			"relation", "varDeclaration", "domain", "primitiveTypeDomain", "checkEnforceQualifier", 
			"assignmentExp", "template", "objectTemplate", "optionalMultiplicity", 
			"propertyTemplateList", "propertyTemplate", "collectionTemplate", "memberSelection", 
			"memberItem", "whenClause", "whereClause", "queryDef", "paramDecl", "expressionEntry", 
			"completeOclDocumentEntry", "completeOclDocument", "importDeclaration", 
			"packageDeclaration", "contextDeclaration", "classifierContextDeclaration", 
			"classifierContextBody", "invariantConstraint", "definitionConstraint", 
			"operationContextDeclaration", "operationContextBody", "propertyContextDeclaration", 
			"propertyContextBody", "parameterList", "parameter", "expression", "primaryExpression", 
			"propertyOrCallSuffix", "iteratorOrOperationCall", "iteratorVariables", 
			"argumentList", "letBinding", "isMarkedPre", "literalExpression", "stringLiteral_", 
			"collectionLiteral", "collectionKind", "collectionLiteralPart", "tupleLiteral", 
			"tupleLiteralPart", "mapLiteral", "mapLiteralPart", "typeExpression", 
			"primitiveType", "collectionType", "mapType", "tupleType", "tupleTypePart", 
			"pathName", "restrictedWord", "identifier", "documentKeyword"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'*'", "';'", "'.'", "'('", "','", "')'", "'{'", "'}'", "':'", 
			"'='", "'['", "'?'", "']'", "'include'", "'library'", "'package'", "'endpackage'", 
			"'context'", "'inv'", "'static'", "'def'", "'pre'", "'post'", "'body'", 
			"'init'", "'derive'", "'?.'", "'->'", "'?->'", "'not'", "'-'", "'/'", 
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
			setState(140);
			compilationUnit();
			setState(141);
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
			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(143);
				importDecl();
				}
				}
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(152);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TRANSFORMATION) {
				{
				{
				setState(149);
				transformationDef();
				}
				}
				setState(154);
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
		public TerminalNode IMPORT() { return getToken(QvtRParser.IMPORT, 0); }
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public List<TerminalNode> COLONCOLON() { return getTokens(QvtRParser.COLONCOLON); }
		public TerminalNode COLONCOLON(int i) {
			return getToken(QvtRParser.COLONCOLON, i);
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
			setState(155);
			match(IMPORT);
			setState(156);
			qualifiedName();
			setState(161);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(157);
					match(COLONCOLON);
					setState(158);
					qualifiedName();
					}
					} 
				}
				setState(163);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(166);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLONCOLON) {
				{
				setState(164);
				match(COLONCOLON);
				setState(165);
				match(T__0);
				}
			}

			setState(168);
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
		enterRule(_localctx, 6, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			identifier();
			setState(175);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(171);
				match(T__2);
				setState(172);
				identifier();
				}
				}
				setState(177);
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
		public TerminalNode TRANSFORMATION() { return getToken(QvtRParser.TRANSFORMATION, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<ModelDeclContext> modelDecl() {
			return getRuleContexts(ModelDeclContext.class);
		}
		public ModelDeclContext modelDecl(int i) {
			return getRuleContext(ModelDeclContext.class,i);
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
		enterRule(_localctx, 8, RULE_transformationDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			match(TRANSFORMATION);
			setState(179);
			identifier();
			setState(180);
			match(T__3);
			setState(181);
			modelDecl();
			setState(186);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(182);
				match(T__4);
				setState(183);
				modelDecl();
				}
				}
				setState(188);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(189);
			match(T__5);
			setState(192);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(190);
				match(EXTENDS);
				setState(191);
				identifier();
				}
			}

			setState(194);
			match(T__6);
			setState(198);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==KEY) {
				{
				{
				setState(195);
				keyDecl();
				}
				}
				setState(200);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 85)) & ~0x3f) == 0 && ((1L << (_la - 85)) & 135L) != 0)) {
				{
				setState(203);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case RELATION:
				case TOP:
				case ABSTRACT:
					{
					setState(201);
					relation();
					}
					break;
				case QUERY:
					{
					setState(202);
					queryDef();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(207);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(208);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 10, RULE_modelDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			identifier();
			setState(211);
			match(T__8);
			setState(224);
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
			case IMPORT:
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				{
				setState(212);
				metaModelId();
				}
				break;
			case T__6:
				{
				setState(213);
				match(T__6);
				setState(214);
				metaModelId();
				setState(219);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(215);
					match(T__4);
					setState(216);
					metaModelId();
					}
					}
					setState(221);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(222);
				match(T__7);
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
		enterRule(_localctx, 12, RULE_metaModelId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(226);
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
		enterRule(_localctx, 14, RULE_keyDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(228);
			match(KEY);
			setState(229);
			pathName();
			setState(230);
			match(T__6);
			setState(231);
			keyProperty();
			setState(236);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(232);
				match(T__4);
				setState(233);
				keyProperty();
				}
				}
				setState(238);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(239);
			match(T__7);
			setState(240);
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
		enterRule(_localctx, 16, RULE_keyProperty);
		try {
			setState(250);
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
			case IMPORT:
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(242);
				identifier();
				}
				break;
			case OPPOSITE:
				enterOuterAlt(_localctx, 2);
				{
				setState(243);
				match(OPPOSITE);
				setState(244);
				match(T__3);
				setState(245);
				pathName();
				setState(246);
				match(COLONCOLON);
				setState(247);
				identifier();
				setState(248);
				match(T__5);
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
		enterRule(_localctx, 18, RULE_relation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(253);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TOP) {
				{
				setState(252);
				match(TOP);
				}
			}

			setState(256);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ABSTRACT) {
				{
				setState(255);
				match(ABSTRACT);
				}
			}

			setState(258);
			match(RELATION);
			setState(259);
			identifier();
			setState(262);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OVERRIDES) {
				{
				setState(260);
				match(OVERRIDES);
				setState(261);
				identifier();
				}
			}

			setState(264);
			match(T__6);
			setState(268);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0)) {
				{
				{
				setState(265);
				varDeclaration();
				}
				}
				setState(270);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(273); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(273);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case CHECKONLY:
				case DOMAIN:
				case ENFORCE:
					{
					setState(271);
					domain();
					}
					break;
				case PRIMITIVE:
					{
					setState(272);
					primitiveTypeDomain();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(275); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & 263L) != 0) );
			setState(278);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHEN) {
				{
				setState(277);
				whenClause();
				}
			}

			setState(281);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(280);
				whereClause();
				}
			}

			setState(283);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 20, RULE_varDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(285);
			identifier();
			setState(290);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(286);
				match(T__4);
				setState(287);
				identifier();
				}
				}
				setState(292);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(293);
			match(T__8);
			setState(294);
			typeExpression();
			setState(297);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(295);
				match(T__9);
				setState(296);
				expression(0);
				}
			}

			setState(299);
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
		enterRule(_localctx, 22, RULE_domain);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CHECKONLY || _la==ENFORCE) {
				{
				setState(301);
				checkEnforceQualifier();
				}
			}

			setState(304);
			match(DOMAIN);
			setState(305);
			identifier();
			setState(306);
			template();
			setState(311);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(307);
				match(T__4);
				setState(308);
				template();
				}
				}
				setState(313);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(322);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IMPLEMENTEDBY) {
				{
				setState(314);
				match(IMPLEMENTEDBY);
				setState(315);
				pathName();
				setState(316);
				match(T__3);
				setState(318);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214126L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266288107513L) != 0)) {
					{
					setState(317);
					argumentList();
					}
				}

				setState(320);
				match(T__5);
				}
			}

			setState(333);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT_VALUES) {
				{
				setState(324);
				match(DEFAULT_VALUES);
				setState(325);
				match(T__6);
				setState(327); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(326);
					assignmentExp();
					}
					}
					setState(329); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0) );
				setState(331);
				match(T__7);
				}
			}

			setState(335);
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
		enterRule(_localctx, 24, RULE_primitiveTypeDomain);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(337);
			match(PRIMITIVE);
			setState(338);
			match(DOMAIN);
			setState(339);
			identifier();
			setState(340);
			match(T__8);
			setState(341);
			typeExpression();
			setState(342);
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
		enterRule(_localctx, 26, RULE_checkEnforceQualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344);
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
		enterRule(_localctx, 28, RULE_assignmentExp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(346);
			identifier();
			setState(347);
			match(T__9);
			setState(348);
			expression(0);
			setState(349);
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
		enterRule(_localctx, 30, RULE_template);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(353);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(351);
				objectTemplate();
				}
				break;
			case 2:
				{
				setState(352);
				collectionTemplate();
				}
				break;
			}
			setState(359);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(355);
				match(T__6);
				setState(356);
				expression(0);
				setState(357);
				match(T__7);
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
		enterRule(_localctx, 32, RULE_objectTemplate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(362);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0)) {
				{
				setState(361);
				identifier();
				}
			}

			setState(364);
			match(T__8);
			setState(365);
			pathName();
			setState(367);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(366);
				optionalMultiplicity();
				}
			}

			setState(369);
			match(T__6);
			setState(371);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1576961L) != 0)) {
				{
				setState(370);
				propertyTemplateList();
				}
			}

			setState(373);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 34, RULE_optionalMultiplicity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(375);
			match(T__10);
			setState(376);
			match(T__11);
			setState(377);
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
		enterRule(_localctx, 36, RULE_propertyTemplateList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(379);
			propertyTemplate();
			setState(384);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(380);
				match(T__4);
				setState(381);
				propertyTemplate();
				}
				}
				setState(386);
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
		enterRule(_localctx, 38, RULE_propertyTemplate);
		try {
			setState(413);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(387);
				identifier();
				setState(388);
				match(T__9);
				setState(389);
				template();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(391);
				identifier();
				setState(392);
				match(T__9);
				setState(393);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(395);
				match(OPPOSITE);
				setState(396);
				match(T__3);
				setState(397);
				pathName();
				setState(398);
				match(COLONCOLON);
				setState(399);
				identifier();
				setState(400);
				match(T__5);
				setState(401);
				match(T__9);
				setState(402);
				template();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(404);
				match(OPPOSITE);
				setState(405);
				match(T__3);
				setState(406);
				pathName();
				setState(407);
				match(COLONCOLON);
				setState(408);
				identifier();
				setState(409);
				match(T__5);
				setState(410);
				match(T__9);
				setState(411);
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
		enterRule(_localctx, 40, RULE_collectionTemplate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0)) {
				{
				setState(415);
				identifier();
				}
			}

			setState(418);
			match(T__8);
			setState(419);
			collectionKind();
			setState(420);
			match(T__3);
			setState(421);
			typeExpression();
			setState(422);
			match(T__5);
			setState(423);
			match(T__6);
			setState(425);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201856L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1605633L) != 0)) {
				{
				setState(424);
				memberSelection();
				}
			}

			setState(427);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 42, RULE_memberSelection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(429);
			memberItem();
			setState(434);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(430);
				match(T__4);
				setState(431);
				memberItem();
				}
				}
				setState(436);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(442);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUSPLUS) {
				{
				setState(437);
				match(PLUSPLUS);
				setState(440);
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
				case IMPORT:
				case ESCAPED_IDENTIFIER:
				case IDENTIFIER:
					{
					setState(438);
					identifier();
					}
					break;
				case UNDERSCORE:
					{
					setState(439);
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
		enterRule(_localctx, 44, RULE_memberItem);
		try {
			setState(447);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(444);
				identifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(445);
				template();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(446);
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
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
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
		enterRule(_localctx, 46, RULE_whenClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(449);
			match(WHEN);
			setState(450);
			match(T__6);
			setState(456);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214126L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266288107513L) != 0)) {
				{
				{
				setState(451);
				expression(0);
				setState(452);
				match(T__1);
				}
				}
				setState(458);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(459);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
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
		enterRule(_localctx, 48, RULE_whereClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(461);
			match(WHERE);
			setState(462);
			match(T__6);
			setState(468);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214126L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266288107513L) != 0)) {
				{
				{
				setState(463);
				expression(0);
				setState(464);
				match(T__1);
				}
				}
				setState(470);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(471);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 50, RULE_queryDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(473);
			match(QUERY);
			setState(474);
			pathName();
			setState(475);
			match(T__3);
			setState(484);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0)) {
				{
				setState(476);
				paramDecl();
				setState(481);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(477);
					match(T__4);
					setState(478);
					paramDecl();
					}
					}
					setState(483);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(486);
			match(T__5);
			setState(487);
			match(T__8);
			setState(488);
			typeExpression();
			setState(494);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(489);
				match(T__1);
				}
				break;
			case T__6:
				{
				setState(490);
				match(T__6);
				setState(491);
				expression(0);
				setState(492);
				match(T__7);
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
		enterRule(_localctx, 52, RULE_paramDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(496);
			identifier();
			setState(497);
			match(T__8);
			setState(498);
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
		enterRule(_localctx, 54, RULE_expressionEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(500);
			expression(0);
			setState(501);
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
		enterRule(_localctx, 56, RULE_completeOclDocumentEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(503);
			completeOclDocument();
			setState(504);
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
		enterRule(_localctx, 58, RULE_completeOclDocument);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(509);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__13 || _la==T__14 || _la==IMPORT) {
				{
				{
				setState(506);
				importDeclaration();
				}
				}
				setState(511);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(516);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__15 || _la==T__17) {
				{
				setState(514);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__15:
					{
					setState(512);
					packageDeclaration();
					}
					break;
				case T__17:
					{
					setState(513);
					contextDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(518);
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
		enterRule(_localctx, 60, RULE_importDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(519);
			_la = _input.LA(1);
			if ( !(_la==T__13 || _la==T__14 || _la==IMPORT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(523);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				{
				setState(520);
				identifier();
				setState(521);
				match(T__8);
				}
				break;
			}
			setState(525);
			pathName();
			setState(528);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLONCOLON) {
				{
				setState(526);
				match(COLONCOLON);
				setState(527);
				match(T__0);
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
		enterRule(_localctx, 62, RULE_packageDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(530);
			match(T__15);
			setState(531);
			pathName();
			setState(535);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__17) {
				{
				{
				setState(532);
				contextDeclaration();
				}
				}
				setState(537);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(538);
			match(T__16);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 64, RULE_contextDeclaration);
		try {
			setState(543);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(540);
				classifierContextDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(541);
				operationContextDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(542);
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
		enterRule(_localctx, 66, RULE_classifierContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(545);
			match(T__17);
			setState(549);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				{
				setState(546);
				identifier();
				setState(547);
				match(T__8);
				}
				break;
			}
			setState(551);
			pathName();
			setState(553); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(552);
				classifierContextBody();
				}
				}
				setState(555); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 3670016L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 68, RULE_classifierContextBody);
		try {
			setState(559);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__18:
				enterOuterAlt(_localctx, 1);
				{
				setState(557);
				invariantConstraint();
				}
				break;
			case T__19:
			case T__20:
				enterOuterAlt(_localctx, 2);
				{
				setState(558);
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
		enterRule(_localctx, 70, RULE_invariantConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(561);
			match(T__18);
			setState(569);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0)) {
				{
				setState(562);
				identifier();
				setState(567);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(563);
					match(T__3);
					setState(564);
					expression(0);
					setState(565);
					match(T__5);
					}
				}

				}
			}

			setState(571);
			match(T__8);
			setState(572);
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
		enterRule(_localctx, 72, RULE_definitionConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(575);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__19) {
				{
				setState(574);
				match(T__19);
				}
			}

			setState(577);
			match(T__20);
			setState(579);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0)) {
				{
				setState(578);
				identifier();
				}
			}

			setState(581);
			match(T__8);
			setState(599);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				{
				setState(582);
				identifier();
				setState(583);
				match(T__8);
				setState(584);
				typeExpression();
				setState(585);
				match(T__9);
				setState(586);
				expression(0);
				}
				break;
			case 2:
				{
				setState(588);
				identifier();
				setState(589);
				match(T__3);
				setState(591);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0)) {
					{
					setState(590);
					parameterList();
					}
				}

				setState(593);
				match(T__5);
				setState(594);
				match(T__8);
				setState(595);
				typeExpression();
				setState(596);
				match(T__9);
				setState(597);
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
		enterRule(_localctx, 74, RULE_operationContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(601);
			match(T__17);
			setState(605);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				{
				setState(602);
				pathName();
				setState(603);
				match(COLONCOLON);
				}
				break;
			}
			setState(607);
			identifier();
			setState(608);
			match(T__3);
			setState(610);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0)) {
				{
				setState(609);
				parameterList();
				}
			}

			setState(612);
			match(T__5);
			setState(615);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(613);
				match(T__8);
				setState(614);
				typeExpression();
				}
			}

			setState(618); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(617);
				operationContextBody();
				}
				}
				setState(620); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 29360128L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 76, RULE_operationContextBody);
		int _la;
		try {
			setState(640);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__21:
				_localctx = new PreConditionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(622);
				match(T__21);
				setState(624);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0)) {
					{
					setState(623);
					identifier();
					}
				}

				setState(626);
				match(T__8);
				setState(627);
				expression(0);
				}
				break;
			case T__22:
				_localctx = new PostConditionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(628);
				match(T__22);
				setState(630);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0)) {
					{
					setState(629);
					identifier();
					}
				}

				setState(632);
				match(T__8);
				setState(633);
				expression(0);
				}
				break;
			case T__23:
				_localctx = new BodyExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(634);
				match(T__23);
				setState(636);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0)) {
					{
					setState(635);
					identifier();
					}
				}

				setState(638);
				match(T__8);
				setState(639);
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
		enterRule(_localctx, 78, RULE_propertyContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(642);
			match(T__17);
			setState(643);
			pathName();
			setState(644);
			match(COLONCOLON);
			setState(645);
			identifier();
			setState(646);
			match(T__8);
			setState(647);
			typeExpression();
			setState(649); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(648);
				propertyContextBody();
				}
				}
				setState(651); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__24 || _la==T__25 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 80, RULE_propertyContextBody);
		int _la;
		try {
			setState(665);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__24:
				_localctx = new InitExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(653);
				match(T__24);
				setState(655);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0)) {
					{
					setState(654);
					identifier();
					}
				}

				setState(657);
				match(T__8);
				setState(658);
				expression(0);
				}
				break;
			case T__25:
				_localctx = new DeriveExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(659);
				match(T__25);
				setState(661);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 134201344L) != 0) || ((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & 1572865L) != 0)) {
					{
					setState(660);
					identifier();
					}
				}

				setState(663);
				match(T__8);
				setState(664);
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
		enterRule(_localctx, 82, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(667);
			parameter();
			setState(672);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(668);
				match(T__4);
				setState(669);
				parameter();
				}
				}
				setState(674);
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
		enterRule(_localctx, 84, RULE_parameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(675);
			identifier();
			setState(676);
			match(T__8);
			setState(677);
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
		int _startState = 86;
		enterRecursionRule(_localctx, 86, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(685);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__29:
				{
				_localctx = new NotExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(680);
				match(T__29);
				setState(681);
				expression(11);
				}
				break;
			case T__30:
				{
				_localctx = new UnaryMinusExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(682);
				match(T__30);
				setState(683);
				expression(10);
				}
				break;
			case T__0:
			case T__3:
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
			case IMPORT:
			case REAL_LITERAL:
			case INTEGER_LITERAL:
			case STRING_LITERAL:
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				{
				_localctx = new PrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(684);
				primaryExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(719);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(717);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
					case 1:
						{
						_localctx = new MultExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(687);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(688);
						((MultExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__0 || _la==T__31) ) {
							((MultExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(689);
						expression(10);
						}
						break;
					case 2:
						{
						_localctx = new AddExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(690);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(691);
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
						setState(692);
						expression(9);
						}
						break;
					case 3:
						{
						_localctx = new CompareExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(693);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(694);
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
						setState(695);
						expression(8);
						}
						break;
					case 4:
						{
						_localctx = new EqualityExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(696);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(697);
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
						setState(698);
						expression(7);
						}
						break;
					case 5:
						{
						_localctx = new AndExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(699);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(700);
						match(T__38);
						setState(701);
						expression(6);
						}
						break;
					case 6:
						{
						_localctx = new OrExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(702);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(703);
						match(T__39);
						setState(704);
						expression(5);
						}
						break;
					case 7:
						{
						_localctx = new XorExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(705);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(706);
						match(T__40);
						setState(707);
						expression(4);
						}
						break;
					case 8:
						{
						_localctx = new ImpliesExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(708);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(709);
						match(T__41);
						setState(710);
						expression(3);
						}
						break;
					case 9:
						{
						_localctx = new NavigationExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(711);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(712);
						_la = _input.LA(1);
						if ( !(_la==T__2 || _la==T__26) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(713);
						propertyOrCallSuffix();
						}
						break;
					case 10:
						{
						_localctx = new ArrowExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(714);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(715);
						_la = _input.LA(1);
						if ( !(_la==T__27 || _la==T__28) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(716);
						iteratorOrOperationCall();
						}
						break;
					}
					} 
				}
				setState(721);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
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
		enterRule(_localctx, 88, RULE_primaryExpression);
		int _la;
		try {
			setState(775);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				_localctx = new ParenExpContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(722);
				match(T__3);
				setState(723);
				expression(0);
				setState(724);
				match(T__5);
				}
				break;
			case 2:
				_localctx = new SelfExpContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(726);
				match(T__42);
				}
				break;
			case 3:
				_localctx = new IfExpContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(727);
				match(T__43);
				setState(728);
				((IfExpContext)_localctx).condition = expression(0);
				setState(729);
				match(T__44);
				setState(730);
				((IfExpContext)_localctx).thenExp = expression(0);
				setState(738);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__45) {
					{
					{
					setState(731);
					match(T__45);
					setState(732);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfCondition.add(((IfExpContext)_localctx).expression);
					setState(733);
					match(T__44);
					setState(734);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfExp.add(((IfExpContext)_localctx).expression);
					}
					}
					setState(740);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(741);
				match(T__46);
				setState(742);
				((IfExpContext)_localctx).elseExp = expression(0);
				setState(743);
				match(T__47);
				}
				break;
			case 4:
				_localctx = new LetExpContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(745);
				match(T__48);
				setState(746);
				letBinding();
				setState(751);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(747);
					match(T__4);
					setState(748);
					letBinding();
					}
					}
					setState(753);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(754);
				match(T__49);
				setState(755);
				expression(0);
				}
				break;
			case 5:
				_localctx = new LiteralExpContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(757);
				literalExpression();
				}
				break;
			case 6:
				_localctx = new OperationCallExpContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(758);
				pathName();
				setState(760);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__51) {
					{
					setState(759);
					((OperationCallExpContext)_localctx).preBefore = isMarkedPre();
					}
				}

				setState(762);
				match(T__3);
				setState(764);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214126L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266288107513L) != 0)) {
					{
					setState(763);
					argumentList();
					}
				}

				setState(766);
				match(T__5);
				setState(768);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
				case 1:
					{
					setState(767);
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
				setState(770);
				primitiveType();
				}
				break;
			case 8:
				_localctx = new CollectionTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(771);
				collectionType();
				}
				break;
			case 9:
				_localctx = new MapTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(772);
				mapType();
				}
				break;
			case 10:
				_localctx = new TupleTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(773);
				tupleType();
				}
				break;
			case 11:
				_localctx = new PathNameExpContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(774);
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
		enterRule(_localctx, 90, RULE_propertyOrCallSuffix);
		int _la;
		try {
			setState(803);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
			case 1:
				_localctx = new DotCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(780);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
				case 1:
					{
					setState(777);
					pathName();
					setState(778);
					match(COLONCOLON);
					}
					break;
				}
				setState(782);
				identifier();
				setState(784);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__51) {
					{
					setState(783);
					((DotCallSuffixContext)_localctx).preBefore = isMarkedPre();
					}
				}

				setState(786);
				match(T__3);
				setState(788);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214126L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266288107513L) != 0)) {
					{
					setState(787);
					argumentList();
					}
				}

				setState(790);
				match(T__5);
				setState(792);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
				case 1:
					{
					setState(791);
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
				setState(797);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
				case 1:
					{
					setState(794);
					pathName();
					setState(795);
					match(COLONCOLON);
					}
					break;
				}
				setState(799);
				identifier();
				setState(801);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
				case 1:
					{
					setState(800);
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
		enterRule(_localctx, 92, RULE_iteratorOrOperationCall);
		int _la;
		try {
			setState(841);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				_localctx = new IteratorCallContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(805);
				identifier();
				setState(806);
				match(T__3);
				setState(807);
				iteratorVariables();
				setState(808);
				match(T__50);
				setState(809);
				expression(0);
				setState(810);
				match(T__5);
				}
				break;
			case 2:
				_localctx = new IterateCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(812);
				identifier();
				setState(813);
				match(T__3);
				setState(821);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
				case 1:
					{
					setState(814);
					((IterateCallContext)_localctx).iterName = identifier();
					setState(817);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==T__8) {
						{
						setState(815);
						match(T__8);
						setState(816);
						((IterateCallContext)_localctx).iterType = typeExpression();
						}
					}

					setState(819);
					match(T__1);
					}
					break;
				}
				setState(823);
				((IterateCallContext)_localctx).accName = identifier();
				setState(826);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(824);
					match(T__8);
					setState(825);
					((IterateCallContext)_localctx).accType = typeExpression();
					}
				}

				setState(828);
				match(T__9);
				setState(829);
				expression(0);
				setState(830);
				match(T__50);
				setState(831);
				expression(0);
				setState(832);
				match(T__5);
				}
				break;
			case 3:
				_localctx = new CollectionOperationCallContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(834);
				identifier();
				setState(835);
				match(T__3);
				setState(837);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214126L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266288107513L) != 0)) {
					{
					setState(836);
					argumentList();
					}
				}

				setState(839);
				match(T__5);
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
		enterRule(_localctx, 94, RULE_iteratorVariables);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(843);
			identifier();
			setState(846);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(844);
				match(T__8);
				setState(845);
				typeExpression();
				}
			}

			setState(856);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(848);
				match(T__4);
				setState(849);
				identifier();
				setState(852);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(850);
					match(T__8);
					setState(851);
					typeExpression();
					}
				}

				}
				}
				setState(858);
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
		enterRule(_localctx, 96, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(859);
			expression(0);
			setState(864);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(860);
				match(T__4);
				setState(861);
				expression(0);
				}
				}
				setState(866);
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
		enterRule(_localctx, 98, RULE_letBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(867);
			identifier();
			setState(870);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(868);
				match(T__8);
				setState(869);
				typeExpression();
				}
			}

			setState(872);
			match(T__9);
			setState(873);
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
		enterRule(_localctx, 100, RULE_isMarkedPre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(875);
			match(T__51);
			setState(876);
			match(T__21);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 102, RULE_literalExpression);
		try {
			setState(889);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER_LITERAL:
				_localctx = new IntegerLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(878);
				match(INTEGER_LITERAL);
				}
				break;
			case REAL_LITERAL:
				_localctx = new RealLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(879);
				match(REAL_LITERAL);
				}
				break;
			case STRING_LITERAL:
				_localctx = new StringLitContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(880);
				stringLiteral_();
				}
				break;
			case T__52:
				_localctx = new TrueLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(881);
				match(T__52);
				}
				break;
			case T__53:
				_localctx = new FalseLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(882);
				match(T__53);
				}
				break;
			case T__54:
				_localctx = new NullLiteralContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(883);
				match(T__54);
				}
				break;
			case T__55:
				_localctx = new InvalidLiteralContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(884);
				match(T__55);
				}
				break;
			case T__0:
				_localctx = new UnlimitedNaturalLiteralContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(885);
				match(T__0);
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
				setState(886);
				collectionLiteral();
				}
				break;
			case T__62:
				_localctx = new TupleLitContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(887);
				tupleLiteral();
				}
				break;
			case T__63:
				_localctx = new MapLitContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(888);
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
		enterRule(_localctx, 104, RULE_stringLiteral_);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(892); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(891);
					match(STRING_LITERAL);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(894); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
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
		enterRule(_localctx, 106, RULE_collectionLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(896);
			collectionKind();
			setState(897);
			match(T__6);
			setState(906);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214126L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266288107513L) != 0)) {
				{
				setState(898);
				collectionLiteralPart();
				setState(903);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(899);
					match(T__4);
					setState(900);
					collectionLiteralPart();
					}
					}
					setState(905);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(908);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 108, RULE_collectionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(910);
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
		enterRule(_localctx, 110, RULE_collectionLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(912);
			expression(0);
			setState(915);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__61) {
				{
				setState(913);
				match(T__61);
				setState(914);
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
		enterRule(_localctx, 112, RULE_tupleLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(917);
			match(T__62);
			setState(918);
			match(T__6);
			setState(919);
			tupleLiteralPart();
			setState(924);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(920);
				match(T__4);
				setState(921);
				tupleLiteralPart();
				}
				}
				setState(926);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(927);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 114, RULE_tupleLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(929);
			identifier();
			setState(932);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(930);
				match(T__8);
				setState(931);
				typeExpression();
				}
			}

			setState(934);
			match(T__9);
			setState(935);
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
		enterRule(_localctx, 116, RULE_mapLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(937);
			match(T__63);
			setState(938);
			match(T__6);
			setState(947);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876094214126L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266288107513L) != 0)) {
				{
				setState(939);
				mapLiteralPart();
				setState(944);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(940);
					match(T__4);
					setState(941);
					mapLiteralPart();
					}
					}
					setState(946);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(949);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 118, RULE_mapLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(951);
			expression(0);
			setState(952);
			_la = _input.LA(1);
			if ( !(_la==T__64 || _la==T__65) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(953);
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
		enterRule(_localctx, 120, RULE_typeExpression);
		try {
			setState(960);
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
				setState(955);
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
				setState(956);
				collectionType();
				}
				break;
			case T__63:
				enterOuterAlt(_localctx, 3);
				{
				setState(957);
				mapType();
				}
				break;
			case T__62:
				enterOuterAlt(_localctx, 4);
				{
				setState(958);
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
			case IMPORT:
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 5);
				{
				setState(959);
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
		enterRule(_localctx, 122, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(962);
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
		enterRule(_localctx, 124, RULE_collectionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(964);
			collectionKind();
			setState(965);
			match(T__3);
			setState(966);
			typeExpression();
			setState(967);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 126, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(969);
			match(T__63);
			setState(970);
			match(T__3);
			setState(971);
			typeExpression();
			setState(972);
			match(T__4);
			setState(973);
			typeExpression();
			setState(974);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 128, RULE_tupleType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(976);
			match(T__62);
			setState(977);
			match(T__3);
			setState(978);
			tupleTypePart();
			setState(983);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(979);
				match(T__4);
				setState(980);
				tupleTypePart();
				}
				}
				setState(985);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(986);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 130, RULE_tupleTypePart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(988);
			identifier();
			setState(989);
			match(T__8);
			setState(990);
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
		enterRule(_localctx, 132, RULE_pathName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(992);
			identifier();
			setState(1000);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,115,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(993);
					match(COLONCOLON);
					setState(996);
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
					case IMPORT:
					case ESCAPED_IDENTIFIER:
					case IDENTIFIER:
						{
						setState(994);
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
						setState(995);
						restrictedWord();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					} 
				}
				setState(1002);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,115,_ctx);
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
		enterRule(_localctx, 134, RULE_restrictedWord);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1003);
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
	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(QvtRParser.IDENTIFIER, 0); }
		public TerminalNode ESCAPED_IDENTIFIER() { return getToken(QvtRParser.ESCAPED_IDENTIFIER, 0); }
		public DocumentKeywordContext documentKeyword() {
			return getRuleContext(DocumentKeywordContext.class,0);
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
		enterRule(_localctx, 136, RULE_identifier);
		try {
			setState(1008);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1005);
				match(IDENTIFIER);
				}
				break;
			case ESCAPED_IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(1006);
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
			case IMPORT:
				enterOuterAlt(_localctx, 3);
				{
				setState(1007);
				documentKeyword();
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
		enterRule(_localctx, 138, RULE_documentKeyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1010);
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
		case 43:
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
		"\u0004\u0001h\u03f5\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007D\u0002E\u0007E\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0005\u0001\u0091\b\u0001\n"+
		"\u0001\f\u0001\u0094\t\u0001\u0001\u0001\u0005\u0001\u0097\b\u0001\n\u0001"+
		"\f\u0001\u009a\t\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0005\u0002\u00a0\b\u0002\n\u0002\f\u0002\u00a3\t\u0002\u0001\u0002\u0001"+
		"\u0002\u0003\u0002\u00a7\b\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0005\u0003\u00ae\b\u0003\n\u0003\f\u0003\u00b1\t\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0005\u0004\u00b9\b\u0004\n\u0004\f\u0004\u00bc\t\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0003\u0004\u00c1\b\u0004\u0001\u0004\u0001\u0004\u0005"+
		"\u0004\u00c5\b\u0004\n\u0004\f\u0004\u00c8\t\u0004\u0001\u0004\u0001\u0004"+
		"\u0005\u0004\u00cc\b\u0004\n\u0004\f\u0004\u00cf\t\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0005\u0005\u00da\b\u0005\n\u0005\f\u0005\u00dd\t\u0005"+
		"\u0001\u0005\u0001\u0005\u0003\u0005\u00e1\b\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0005\u0007\u00eb\b\u0007\n\u0007\f\u0007\u00ee\t\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0003\b\u00fb\b\b\u0001\t\u0003\t\u00fe\b\t\u0001\t\u0003\t"+
		"\u0101\b\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u0107\b\t\u0001\t\u0001"+
		"\t\u0005\t\u010b\b\t\n\t\f\t\u010e\t\t\u0001\t\u0001\t\u0004\t\u0112\b"+
		"\t\u000b\t\f\t\u0113\u0001\t\u0003\t\u0117\b\t\u0001\t\u0003\t\u011a\b"+
		"\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0005\n\u0121\b\n\n\n\f\n\u0124"+
		"\t\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u012a\b\n\u0001\n\u0001\n"+
		"\u0001\u000b\u0003\u000b\u012f\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0005\u000b\u0136\b\u000b\n\u000b\f\u000b\u0139"+
		"\t\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u013f"+
		"\b\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u0143\b\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0004\u000b\u0148\b\u000b\u000b\u000b\f\u000b"+
		"\u0149\u0001\u000b\u0001\u000b\u0003\u000b\u014e\b\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r"+
		"\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000f\u0001\u000f\u0003\u000f\u0162\b\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0003\u000f\u0168\b\u000f\u0001\u0010\u0003\u0010\u016b"+
		"\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0003\u0010\u0170\b\u0010"+
		"\u0001\u0010\u0001\u0010\u0003\u0010\u0174\b\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0005\u0012\u017f\b\u0012\n\u0012\f\u0012\u0182\t\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0003\u0013\u019e\b\u0013\u0001\u0014\u0003\u0014\u01a1"+
		"\b\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0003\u0014\u01aa\b\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0005\u0015\u01b1\b\u0015\n\u0015\f\u0015"+
		"\u01b4\t\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u01b9\b"+
		"\u0015\u0003\u0015\u01bb\b\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0003"+
		"\u0016\u01c0\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0005\u0017\u01c7\b\u0017\n\u0017\f\u0017\u01ca\t\u0017\u0001\u0017"+
		"\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0005\u0018\u01d3\b\u0018\n\u0018\f\u0018\u01d6\t\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0005\u0019\u01e0\b\u0019\n\u0019\f\u0019\u01e3\t\u0019\u0003\u0019"+
		"\u01e5\b\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u01ef\b\u0019\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0005\u001d\u01fc\b\u001d"+
		"\n\u001d\f\u001d\u01ff\t\u001d\u0001\u001d\u0001\u001d\u0005\u001d\u0203"+
		"\b\u001d\n\u001d\f\u001d\u0206\t\u001d\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0003\u001e\u020c\b\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0003\u001e\u0211\b\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0005\u001f"+
		"\u0216\b\u001f\n\u001f\f\u001f\u0219\t\u001f\u0001\u001f\u0001\u001f\u0001"+
		" \u0001 \u0001 \u0003 \u0220\b \u0001!\u0001!\u0001!\u0001!\u0003!\u0226"+
		"\b!\u0001!\u0001!\u0004!\u022a\b!\u000b!\f!\u022b\u0001\"\u0001\"\u0003"+
		"\"\u0230\b\"\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0003#\u0238\b"+
		"#\u0003#\u023a\b#\u0001#\u0001#\u0001#\u0001$\u0003$\u0240\b$\u0001$\u0001"+
		"$\u0003$\u0244\b$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001"+
		"$\u0001$\u0001$\u0003$\u0250\b$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001"+
		"$\u0003$\u0258\b$\u0001%\u0001%\u0001%\u0001%\u0003%\u025e\b%\u0001%\u0001"+
		"%\u0001%\u0003%\u0263\b%\u0001%\u0001%\u0001%\u0003%\u0268\b%\u0001%\u0004"+
		"%\u026b\b%\u000b%\f%\u026c\u0001&\u0001&\u0003&\u0271\b&\u0001&\u0001"+
		"&\u0001&\u0001&\u0003&\u0277\b&\u0001&\u0001&\u0001&\u0001&\u0003&\u027d"+
		"\b&\u0001&\u0001&\u0003&\u0281\b&\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0004\'\u028a\b\'\u000b\'\f\'\u028b\u0001(\u0001(\u0003"+
		"(\u0290\b(\u0001(\u0001(\u0001(\u0001(\u0003(\u0296\b(\u0001(\u0001(\u0003"+
		"(\u029a\b(\u0001)\u0001)\u0001)\u0005)\u029f\b)\n)\f)\u02a2\t)\u0001*"+
		"\u0001*\u0001*\u0001*\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0003"+
		"+\u02ae\b+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001"+
		"+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001"+
		"+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001"+
		"+\u0001+\u0005+\u02ce\b+\n+\f+\u02d1\t+\u0001,\u0001,\u0001,\u0001,\u0001"+
		",\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0005"+
		",\u02e1\b,\n,\f,\u02e4\t,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001"+
		",\u0001,\u0005,\u02ee\b,\n,\f,\u02f1\t,\u0001,\u0001,\u0001,\u0001,\u0001"+
		",\u0001,\u0003,\u02f9\b,\u0001,\u0001,\u0003,\u02fd\b,\u0001,\u0001,\u0003"+
		",\u0301\b,\u0001,\u0001,\u0001,\u0001,\u0001,\u0003,\u0308\b,\u0001-\u0001"+
		"-\u0001-\u0003-\u030d\b-\u0001-\u0001-\u0003-\u0311\b-\u0001-\u0001-\u0003"+
		"-\u0315\b-\u0001-\u0001-\u0003-\u0319\b-\u0001-\u0001-\u0001-\u0003-\u031e"+
		"\b-\u0001-\u0001-\u0003-\u0322\b-\u0003-\u0324\b-\u0001.\u0001.\u0001"+
		".\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0003"+
		".\u0332\b.\u0001.\u0001.\u0003.\u0336\b.\u0001.\u0001.\u0001.\u0003.\u033b"+
		"\b.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0003"+
		".\u0346\b.\u0001.\u0001.\u0003.\u034a\b.\u0001/\u0001/\u0001/\u0003/\u034f"+
		"\b/\u0001/\u0001/\u0001/\u0001/\u0003/\u0355\b/\u0005/\u0357\b/\n/\f/"+
		"\u035a\t/\u00010\u00010\u00010\u00050\u035f\b0\n0\f0\u0362\t0\u00011\u0001"+
		"1\u00011\u00031\u0367\b1\u00011\u00011\u00011\u00012\u00012\u00012\u0001"+
		"3\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u0001"+
		"3\u00033\u037a\b3\u00014\u00044\u037d\b4\u000b4\f4\u037e\u00015\u0001"+
		"5\u00015\u00015\u00015\u00055\u0386\b5\n5\f5\u0389\t5\u00035\u038b\b5"+
		"\u00015\u00015\u00016\u00016\u00017\u00017\u00017\u00037\u0394\b7\u0001"+
		"8\u00018\u00018\u00018\u00018\u00058\u039b\b8\n8\f8\u039e\t8\u00018\u0001"+
		"8\u00019\u00019\u00019\u00039\u03a5\b9\u00019\u00019\u00019\u0001:\u0001"+
		":\u0001:\u0001:\u0001:\u0005:\u03af\b:\n:\f:\u03b2\t:\u0003:\u03b4\b:"+
		"\u0001:\u0001:\u0001;\u0001;\u0001;\u0001;\u0001<\u0001<\u0001<\u0001"+
		"<\u0001<\u0003<\u03c1\b<\u0001=\u0001=\u0001>\u0001>\u0001>\u0001>\u0001"+
		">\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001@\u0001@\u0001"+
		"@\u0001@\u0001@\u0005@\u03d6\b@\n@\f@\u03d9\t@\u0001@\u0001@\u0001A\u0001"+
		"A\u0001A\u0001A\u0001B\u0001B\u0001B\u0001B\u0003B\u03e5\bB\u0005B\u03e7"+
		"\bB\nB\fB\u03ea\tB\u0001C\u0001C\u0001D\u0001D\u0001D\u0003D\u03f1\bD"+
		"\u0001E\u0001E\u0001E\u0000\u0001VF\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDF"+
		"HJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u0000"+
		"\r\u0002\u0000LLNN\u0002\u0000\u000e\u000fQQ\u0002\u0000\u0001\u0001 "+
		" \u0002\u0000\u001f\u001f!!\u0001\u0000\"%\u0002\u0000\n\n&&\u0002\u0000"+
		"\u0003\u0003\u001b\u001b\u0001\u0000\u001c\u001d\u0001\u00009=\u0001\u0000"+
		"AB\u0001\u0000CK\u0003\u00009=??CK\u0002\u0000\u000e\u001aQQ\u0448\u0000"+
		"\u008c\u0001\u0000\u0000\u0000\u0002\u0092\u0001\u0000\u0000\u0000\u0004"+
		"\u009b\u0001\u0000\u0000\u0000\u0006\u00aa\u0001\u0000\u0000\u0000\b\u00b2"+
		"\u0001\u0000\u0000\u0000\n\u00d2\u0001\u0000\u0000\u0000\f\u00e2\u0001"+
		"\u0000\u0000\u0000\u000e\u00e4\u0001\u0000\u0000\u0000\u0010\u00fa\u0001"+
		"\u0000\u0000\u0000\u0012\u00fd\u0001\u0000\u0000\u0000\u0014\u011d\u0001"+
		"\u0000\u0000\u0000\u0016\u012e\u0001\u0000\u0000\u0000\u0018\u0151\u0001"+
		"\u0000\u0000\u0000\u001a\u0158\u0001\u0000\u0000\u0000\u001c\u015a\u0001"+
		"\u0000\u0000\u0000\u001e\u0161\u0001\u0000\u0000\u0000 \u016a\u0001\u0000"+
		"\u0000\u0000\"\u0177\u0001\u0000\u0000\u0000$\u017b\u0001\u0000\u0000"+
		"\u0000&\u019d\u0001\u0000\u0000\u0000(\u01a0\u0001\u0000\u0000\u0000*"+
		"\u01ad\u0001\u0000\u0000\u0000,\u01bf\u0001\u0000\u0000\u0000.\u01c1\u0001"+
		"\u0000\u0000\u00000\u01cd\u0001\u0000\u0000\u00002\u01d9\u0001\u0000\u0000"+
		"\u00004\u01f0\u0001\u0000\u0000\u00006\u01f4\u0001\u0000\u0000\u00008"+
		"\u01f7\u0001\u0000\u0000\u0000:\u01fd\u0001\u0000\u0000\u0000<\u0207\u0001"+
		"\u0000\u0000\u0000>\u0212\u0001\u0000\u0000\u0000@\u021f\u0001\u0000\u0000"+
		"\u0000B\u0221\u0001\u0000\u0000\u0000D\u022f\u0001\u0000\u0000\u0000F"+
		"\u0231\u0001\u0000\u0000\u0000H\u023f\u0001\u0000\u0000\u0000J\u0259\u0001"+
		"\u0000\u0000\u0000L\u0280\u0001\u0000\u0000\u0000N\u0282\u0001\u0000\u0000"+
		"\u0000P\u0299\u0001\u0000\u0000\u0000R\u029b\u0001\u0000\u0000\u0000T"+
		"\u02a3\u0001\u0000\u0000\u0000V\u02ad\u0001\u0000\u0000\u0000X\u0307\u0001"+
		"\u0000\u0000\u0000Z\u0323\u0001\u0000\u0000\u0000\\\u0349\u0001\u0000"+
		"\u0000\u0000^\u034b\u0001\u0000\u0000\u0000`\u035b\u0001\u0000\u0000\u0000"+
		"b\u0363\u0001\u0000\u0000\u0000d\u036b\u0001\u0000\u0000\u0000f\u0379"+
		"\u0001\u0000\u0000\u0000h\u037c\u0001\u0000\u0000\u0000j\u0380\u0001\u0000"+
		"\u0000\u0000l\u038e\u0001\u0000\u0000\u0000n\u0390\u0001\u0000\u0000\u0000"+
		"p\u0395\u0001\u0000\u0000\u0000r\u03a1\u0001\u0000\u0000\u0000t\u03a9"+
		"\u0001\u0000\u0000\u0000v\u03b7\u0001\u0000\u0000\u0000x\u03c0\u0001\u0000"+
		"\u0000\u0000z\u03c2\u0001\u0000\u0000\u0000|\u03c4\u0001\u0000\u0000\u0000"+
		"~\u03c9\u0001\u0000\u0000\u0000\u0080\u03d0\u0001\u0000\u0000\u0000\u0082"+
		"\u03dc\u0001\u0000\u0000\u0000\u0084\u03e0\u0001\u0000\u0000\u0000\u0086"+
		"\u03eb\u0001\u0000\u0000\u0000\u0088\u03f0\u0001\u0000\u0000\u0000\u008a"+
		"\u03f2\u0001\u0000\u0000\u0000\u008c\u008d\u0003\u0002\u0001\u0000\u008d"+
		"\u008e\u0005\u0000\u0000\u0001\u008e\u0001\u0001\u0000\u0000\u0000\u008f"+
		"\u0091\u0003\u0004\u0002\u0000\u0090\u008f\u0001\u0000\u0000\u0000\u0091"+
		"\u0094\u0001\u0000\u0000\u0000\u0092\u0090\u0001\u0000\u0000\u0000\u0092"+
		"\u0093\u0001\u0000\u0000\u0000\u0093\u0098\u0001\u0000\u0000\u0000\u0094"+
		"\u0092\u0001\u0000\u0000\u0000\u0095\u0097\u0003\b\u0004\u0000\u0096\u0095"+
		"\u0001\u0000\u0000\u0000\u0097\u009a\u0001\u0000\u0000\u0000\u0098\u0096"+
		"\u0001\u0000\u0000\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u0003"+
		"\u0001\u0000\u0000\u0000\u009a\u0098\u0001\u0000\u0000\u0000\u009b\u009c"+
		"\u0005Q\u0000\u0000\u009c\u00a1\u0003\u0006\u0003\u0000\u009d\u009e\u0005"+
		"_\u0000\u0000\u009e\u00a0\u0003\u0006\u0003\u0000\u009f\u009d\u0001\u0000"+
		"\u0000\u0000\u00a0\u00a3\u0001\u0000\u0000\u0000\u00a1\u009f\u0001\u0000"+
		"\u0000\u0000\u00a1\u00a2\u0001\u0000\u0000\u0000\u00a2\u00a6\u0001\u0000"+
		"\u0000\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a4\u00a5\u0005_\u0000"+
		"\u0000\u00a5\u00a7\u0005\u0001\u0000\u0000\u00a6\u00a4\u0001\u0000\u0000"+
		"\u0000\u00a6\u00a7\u0001\u0000\u0000\u0000\u00a7\u00a8\u0001\u0000\u0000"+
		"\u0000\u00a8\u00a9\u0005\u0002\u0000\u0000\u00a9\u0005\u0001\u0000\u0000"+
		"\u0000\u00aa\u00af\u0003\u0088D\u0000\u00ab\u00ac\u0005\u0003\u0000\u0000"+
		"\u00ac\u00ae\u0003\u0088D\u0000\u00ad\u00ab\u0001\u0000\u0000\u0000\u00ae"+
		"\u00b1\u0001\u0000\u0000\u0000\u00af\u00ad\u0001\u0000\u0000\u0000\u00af"+
		"\u00b0\u0001\u0000\u0000\u0000\u00b0\u0007\u0001\u0000\u0000\u0000\u00b1"+
		"\u00af\u0001\u0000\u0000\u0000\u00b2\u00b3\u0005X\u0000\u0000\u00b3\u00b4"+
		"\u0003\u0088D\u0000\u00b4\u00b5\u0005\u0004\u0000\u0000\u00b5\u00ba\u0003"+
		"\n\u0005\u0000\u00b6\u00b7\u0005\u0005\u0000\u0000\u00b7\u00b9\u0003\n"+
		"\u0005\u0000\u00b8\u00b6\u0001\u0000\u0000\u0000\u00b9\u00bc\u0001\u0000"+
		"\u0000\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000\u00ba\u00bb\u0001\u0000"+
		"\u0000\u0000\u00bb\u00bd\u0001\u0000\u0000\u0000\u00bc\u00ba\u0001\u0000"+
		"\u0000\u0000\u00bd\u00c0\u0005\u0006\u0000\u0000\u00be\u00bf\u0005O\u0000"+
		"\u0000\u00bf\u00c1\u0003\u0088D\u0000\u00c0\u00be\u0001\u0000\u0000\u0000"+
		"\u00c0\u00c1\u0001\u0000\u0000\u0000\u00c1\u00c2\u0001\u0000\u0000\u0000"+
		"\u00c2\u00c6\u0005\u0007\u0000\u0000\u00c3\u00c5\u0003\u000e\u0007\u0000"+
		"\u00c4\u00c3\u0001\u0000\u0000\u0000\u00c5\u00c8\u0001\u0000\u0000\u0000"+
		"\u00c6\u00c4\u0001\u0000\u0000\u0000\u00c6\u00c7\u0001\u0000\u0000\u0000"+
		"\u00c7\u00cd\u0001\u0000\u0000\u0000\u00c8\u00c6\u0001\u0000\u0000\u0000"+
		"\u00c9\u00cc\u0003\u0012\t\u0000\u00ca\u00cc\u00032\u0019\u0000\u00cb"+
		"\u00c9\u0001\u0000\u0000\u0000\u00cb\u00ca\u0001\u0000\u0000\u0000\u00cc"+
		"\u00cf\u0001\u0000\u0000\u0000\u00cd\u00cb\u0001\u0000\u0000\u0000\u00cd"+
		"\u00ce\u0001\u0000\u0000\u0000\u00ce\u00d0\u0001\u0000\u0000\u0000\u00cf"+
		"\u00cd\u0001\u0000\u0000\u0000\u00d0\u00d1\u0005\b\u0000\u0000\u00d1\t"+
		"\u0001\u0000\u0000\u0000\u00d2\u00d3\u0003\u0088D\u0000\u00d3\u00e0\u0005"+
		"\t\u0000\u0000\u00d4\u00e1\u0003\f\u0006\u0000\u00d5\u00d6\u0005\u0007"+
		"\u0000\u0000\u00d6\u00db\u0003\f\u0006\u0000\u00d7\u00d8\u0005\u0005\u0000"+
		"\u0000\u00d8\u00da\u0003\f\u0006\u0000\u00d9\u00d7\u0001\u0000\u0000\u0000"+
		"\u00da\u00dd\u0001\u0000\u0000\u0000\u00db\u00d9\u0001\u0000\u0000\u0000"+
		"\u00db\u00dc\u0001\u0000\u0000\u0000\u00dc\u00de\u0001\u0000\u0000\u0000"+
		"\u00dd\u00db\u0001\u0000\u0000\u0000\u00de\u00df\u0005\b\u0000\u0000\u00df"+
		"\u00e1\u0001\u0000\u0000\u0000\u00e0\u00d4\u0001\u0000\u0000\u0000\u00e0"+
		"\u00d5\u0001\u0000\u0000\u0000\u00e1\u000b\u0001\u0000\u0000\u0000\u00e2"+
		"\u00e3\u0003\u0084B\u0000\u00e3\r\u0001\u0000\u0000\u0000\u00e4\u00e5"+
		"\u0005R\u0000\u0000\u00e5\u00e6\u0003\u0084B\u0000\u00e6\u00e7\u0005\u0007"+
		"\u0000\u0000\u00e7\u00ec\u0003\u0010\b\u0000\u00e8\u00e9\u0005\u0005\u0000"+
		"\u0000\u00e9\u00eb\u0003\u0010\b\u0000\u00ea\u00e8\u0001\u0000\u0000\u0000"+
		"\u00eb\u00ee\u0001\u0000\u0000\u0000\u00ec\u00ea\u0001\u0000\u0000\u0000"+
		"\u00ec\u00ed\u0001\u0000\u0000\u0000\u00ed\u00ef\u0001\u0000\u0000\u0000"+
		"\u00ee\u00ec\u0001\u0000\u0000\u0000\u00ef\u00f0\u0005\b\u0000\u0000\u00f0"+
		"\u00f1\u0005\u0002\u0000\u0000\u00f1\u000f\u0001\u0000\u0000\u0000\u00f2"+
		"\u00fb\u0003\u0088D\u0000\u00f3\u00f4\u0005]\u0000\u0000\u00f4\u00f5\u0005"+
		"\u0004\u0000\u0000\u00f5\u00f6\u0003\u0084B\u0000\u00f6\u00f7\u0005_\u0000"+
		"\u0000\u00f7\u00f8\u0003\u0088D\u0000\u00f8\u00f9\u0005\u0006\u0000\u0000"+
		"\u00f9\u00fb\u0001\u0000\u0000\u0000\u00fa\u00f2\u0001\u0000\u0000\u0000"+
		"\u00fa\u00f3\u0001\u0000\u0000\u0000\u00fb\u0011\u0001\u0000\u0000\u0000"+
		"\u00fc\u00fe\u0005W\u0000\u0000\u00fd\u00fc\u0001\u0000\u0000\u0000\u00fd"+
		"\u00fe\u0001\u0000\u0000\u0000\u00fe\u0100\u0001\u0000\u0000\u0000\u00ff"+
		"\u0101\u0005\\\u0000\u0000\u0100\u00ff\u0001\u0000\u0000\u0000\u0100\u0101"+
		"\u0001\u0000\u0000\u0000\u0101\u0102\u0001\u0000\u0000\u0000\u0102\u0103"+
		"\u0005V\u0000\u0000\u0103\u0106\u0003\u0088D\u0000\u0104\u0105\u0005S"+
		"\u0000\u0000\u0105\u0107\u0003\u0088D\u0000\u0106\u0104\u0001\u0000\u0000"+
		"\u0000\u0106\u0107\u0001\u0000\u0000\u0000\u0107\u0108\u0001\u0000\u0000"+
		"\u0000\u0108\u010c\u0005\u0007\u0000\u0000\u0109\u010b\u0003\u0014\n\u0000"+
		"\u010a\u0109\u0001\u0000\u0000\u0000\u010b\u010e\u0001\u0000\u0000\u0000"+
		"\u010c\u010a\u0001\u0000\u0000\u0000\u010c\u010d\u0001\u0000\u0000\u0000"+
		"\u010d\u0111\u0001\u0000\u0000\u0000\u010e\u010c\u0001\u0000\u0000\u0000"+
		"\u010f\u0112\u0003\u0016\u000b\u0000\u0110\u0112\u0003\u0018\f\u0000\u0111"+
		"\u010f\u0001\u0000\u0000\u0000\u0111\u0110\u0001\u0000\u0000\u0000\u0112"+
		"\u0113\u0001\u0000\u0000\u0000\u0113\u0111\u0001\u0000\u0000\u0000\u0113"+
		"\u0114\u0001\u0000\u0000\u0000\u0114\u0116\u0001\u0000\u0000\u0000\u0115"+
		"\u0117\u0003.\u0017\u0000\u0116\u0115\u0001\u0000\u0000\u0000\u0116\u0117"+
		"\u0001\u0000\u0000\u0000\u0117\u0119\u0001\u0000\u0000\u0000\u0118\u011a"+
		"\u00030\u0018\u0000\u0119\u0118\u0001\u0000\u0000\u0000\u0119\u011a\u0001"+
		"\u0000\u0000\u0000\u011a\u011b\u0001\u0000\u0000\u0000\u011b\u011c\u0005"+
		"\b\u0000\u0000\u011c\u0013\u0001\u0000\u0000\u0000\u011d\u0122\u0003\u0088"+
		"D\u0000\u011e\u011f\u0005\u0005\u0000\u0000\u011f\u0121\u0003\u0088D\u0000"+
		"\u0120\u011e\u0001\u0000\u0000\u0000\u0121\u0124\u0001\u0000\u0000\u0000"+
		"\u0122\u0120\u0001\u0000\u0000\u0000\u0122\u0123\u0001\u0000\u0000\u0000"+
		"\u0123\u0125\u0001\u0000\u0000\u0000\u0124\u0122\u0001\u0000\u0000\u0000"+
		"\u0125\u0126\u0005\t\u0000\u0000\u0126\u0129\u0003x<\u0000\u0127\u0128"+
		"\u0005\n\u0000\u0000\u0128\u012a\u0003V+\u0000\u0129\u0127\u0001\u0000"+
		"\u0000\u0000\u0129\u012a\u0001\u0000\u0000\u0000\u012a\u012b\u0001\u0000"+
		"\u0000\u0000\u012b\u012c\u0005\u0002\u0000\u0000\u012c\u0015\u0001\u0000"+
		"\u0000\u0000\u012d\u012f\u0003\u001a\r\u0000\u012e\u012d\u0001\u0000\u0000"+
		"\u0000\u012e\u012f\u0001\u0000\u0000\u0000\u012f\u0130\u0001\u0000\u0000"+
		"\u0000\u0130\u0131\u0005M\u0000\u0000\u0131\u0132\u0003\u0088D\u0000\u0132"+
		"\u0137\u0003\u001e\u000f\u0000\u0133\u0134\u0005\u0005\u0000\u0000\u0134"+
		"\u0136\u0003\u001e\u000f\u0000\u0135\u0133\u0001\u0000\u0000\u0000\u0136"+
		"\u0139\u0001\u0000\u0000\u0000\u0137\u0135\u0001\u0000\u0000\u0000\u0137"+
		"\u0138\u0001\u0000\u0000\u0000\u0138\u0142\u0001\u0000\u0000\u0000\u0139"+
		"\u0137\u0001\u0000\u0000\u0000\u013a\u013b\u0005P\u0000\u0000\u013b\u013c"+
		"\u0003\u0084B\u0000\u013c\u013e\u0005\u0004\u0000\u0000\u013d\u013f\u0003"+
		"`0\u0000\u013e\u013d\u0001\u0000\u0000\u0000\u013e\u013f\u0001\u0000\u0000"+
		"\u0000\u013f\u0140\u0001\u0000\u0000\u0000\u0140\u0141\u0005\u0006\u0000"+
		"\u0000\u0141\u0143\u0001\u0000\u0000\u0000\u0142\u013a\u0001\u0000\u0000"+
		"\u0000\u0142\u0143\u0001\u0000\u0000\u0000\u0143\u014d\u0001\u0000\u0000"+
		"\u0000\u0144\u0145\u0005[\u0000\u0000\u0145\u0147\u0005\u0007\u0000\u0000"+
		"\u0146\u0148\u0003\u001c\u000e\u0000\u0147\u0146\u0001\u0000\u0000\u0000"+
		"\u0148\u0149\u0001\u0000\u0000\u0000\u0149\u0147\u0001\u0000\u0000\u0000"+
		"\u0149\u014a\u0001\u0000\u0000\u0000\u014a\u014b\u0001\u0000\u0000\u0000"+
		"\u014b\u014c\u0005\b\u0000\u0000\u014c\u014e\u0001\u0000\u0000\u0000\u014d"+
		"\u0144\u0001\u0000\u0000\u0000\u014d\u014e\u0001\u0000\u0000\u0000\u014e"+
		"\u014f\u0001\u0000\u0000\u0000\u014f\u0150\u0005\u0002\u0000\u0000\u0150"+
		"\u0017\u0001\u0000\u0000\u0000\u0151\u0152\u0005T\u0000\u0000\u0152\u0153"+
		"\u0005M\u0000\u0000\u0153\u0154\u0003\u0088D\u0000\u0154\u0155\u0005\t"+
		"\u0000\u0000\u0155\u0156\u0003x<\u0000\u0156\u0157\u0005\u0002\u0000\u0000"+
		"\u0157\u0019\u0001\u0000\u0000\u0000\u0158\u0159\u0007\u0000\u0000\u0000"+
		"\u0159\u001b\u0001\u0000\u0000\u0000\u015a\u015b\u0003\u0088D\u0000\u015b"+
		"\u015c\u0005\n\u0000\u0000\u015c\u015d\u0003V+\u0000\u015d\u015e\u0005"+
		"\u0002\u0000\u0000\u015e\u001d\u0001\u0000\u0000\u0000\u015f\u0162\u0003"+
		" \u0010\u0000\u0160\u0162\u0003(\u0014\u0000\u0161\u015f\u0001\u0000\u0000"+
		"\u0000\u0161\u0160\u0001\u0000\u0000\u0000\u0162\u0167\u0001\u0000\u0000"+
		"\u0000\u0163\u0164\u0005\u0007\u0000\u0000\u0164\u0165\u0003V+\u0000\u0165"+
		"\u0166\u0005\b\u0000\u0000\u0166\u0168\u0001\u0000\u0000\u0000\u0167\u0163"+
		"\u0001\u0000\u0000\u0000\u0167\u0168\u0001\u0000\u0000\u0000\u0168\u001f"+
		"\u0001\u0000\u0000\u0000\u0169\u016b\u0003\u0088D\u0000\u016a\u0169\u0001"+
		"\u0000\u0000\u0000\u016a\u016b\u0001\u0000\u0000\u0000\u016b\u016c\u0001"+
		"\u0000\u0000\u0000\u016c\u016d\u0005\t\u0000\u0000\u016d\u016f\u0003\u0084"+
		"B\u0000\u016e\u0170\u0003\"\u0011\u0000\u016f\u016e\u0001\u0000\u0000"+
		"\u0000\u016f\u0170\u0001\u0000\u0000\u0000\u0170\u0171\u0001\u0000\u0000"+
		"\u0000\u0171\u0173\u0005\u0007\u0000\u0000\u0172\u0174\u0003$\u0012\u0000"+
		"\u0173\u0172\u0001\u0000\u0000\u0000\u0173\u0174\u0001\u0000\u0000\u0000"+
		"\u0174\u0175\u0001\u0000\u0000\u0000\u0175\u0176\u0005\b\u0000\u0000\u0176"+
		"!\u0001\u0000\u0000\u0000\u0177\u0178\u0005\u000b\u0000\u0000\u0178\u0179"+
		"\u0005\f\u0000\u0000\u0179\u017a\u0005\r\u0000\u0000\u017a#\u0001\u0000"+
		"\u0000\u0000\u017b\u0180\u0003&\u0013\u0000\u017c\u017d\u0005\u0005\u0000"+
		"\u0000\u017d\u017f\u0003&\u0013\u0000\u017e\u017c\u0001\u0000\u0000\u0000"+
		"\u017f\u0182\u0001\u0000\u0000\u0000\u0180\u017e\u0001\u0000\u0000\u0000"+
		"\u0180\u0181\u0001\u0000\u0000\u0000\u0181%\u0001\u0000\u0000\u0000\u0182"+
		"\u0180\u0001\u0000\u0000\u0000\u0183\u0184\u0003\u0088D\u0000\u0184\u0185"+
		"\u0005\n\u0000\u0000\u0185\u0186\u0003\u001e\u000f\u0000\u0186\u019e\u0001"+
		"\u0000\u0000\u0000\u0187\u0188\u0003\u0088D\u0000\u0188\u0189\u0005\n"+
		"\u0000\u0000\u0189\u018a\u0003V+\u0000\u018a\u019e\u0001\u0000\u0000\u0000"+
		"\u018b\u018c\u0005]\u0000\u0000\u018c\u018d\u0005\u0004\u0000\u0000\u018d"+
		"\u018e\u0003\u0084B\u0000\u018e\u018f\u0005_\u0000\u0000\u018f\u0190\u0003"+
		"\u0088D\u0000\u0190\u0191\u0005\u0006\u0000\u0000\u0191\u0192\u0005\n"+
		"\u0000\u0000\u0192\u0193\u0003\u001e\u000f\u0000\u0193\u019e\u0001\u0000"+
		"\u0000\u0000\u0194\u0195\u0005]\u0000\u0000\u0195\u0196\u0005\u0004\u0000"+
		"\u0000\u0196\u0197\u0003\u0084B\u0000\u0197\u0198\u0005_\u0000\u0000\u0198"+
		"\u0199\u0003\u0088D\u0000\u0199\u019a\u0005\u0006\u0000\u0000\u019a\u019b"+
		"\u0005\n\u0000\u0000\u019b\u019c\u0003V+\u0000\u019c\u019e\u0001\u0000"+
		"\u0000\u0000\u019d\u0183\u0001\u0000\u0000\u0000\u019d\u0187\u0001\u0000"+
		"\u0000\u0000\u019d\u018b\u0001\u0000\u0000\u0000\u019d\u0194\u0001\u0000"+
		"\u0000\u0000\u019e\'\u0001\u0000\u0000\u0000\u019f\u01a1\u0003\u0088D"+
		"\u0000\u01a0\u019f\u0001\u0000\u0000\u0000\u01a0\u01a1\u0001\u0000\u0000"+
		"\u0000\u01a1\u01a2\u0001\u0000\u0000\u0000\u01a2\u01a3\u0005\t\u0000\u0000"+
		"\u01a3\u01a4\u0003l6\u0000\u01a4\u01a5\u0005\u0004\u0000\u0000\u01a5\u01a6"+
		"\u0003x<\u0000\u01a6\u01a7\u0005\u0006\u0000\u0000\u01a7\u01a9\u0005\u0007"+
		"\u0000\u0000\u01a8\u01aa\u0003*\u0015\u0000\u01a9\u01a8\u0001\u0000\u0000"+
		"\u0000\u01a9\u01aa\u0001\u0000\u0000\u0000\u01aa\u01ab\u0001\u0000\u0000"+
		"\u0000\u01ab\u01ac\u0005\b\u0000\u0000\u01ac)\u0001\u0000\u0000\u0000"+
		"\u01ad\u01b2\u0003,\u0016\u0000\u01ae\u01af\u0005\u0005\u0000\u0000\u01af"+
		"\u01b1\u0003,\u0016\u0000\u01b0\u01ae\u0001\u0000\u0000\u0000\u01b1\u01b4"+
		"\u0001\u0000\u0000\u0000\u01b2\u01b0\u0001\u0000\u0000\u0000\u01b2\u01b3"+
		"\u0001\u0000\u0000\u0000\u01b3\u01ba\u0001\u0000\u0000\u0000\u01b4\u01b2"+
		"\u0001\u0000\u0000\u0000\u01b5\u01b8\u0005^\u0000\u0000\u01b6\u01b9\u0003"+
		"\u0088D\u0000\u01b7\u01b9\u0005`\u0000\u0000\u01b8\u01b6\u0001\u0000\u0000"+
		"\u0000\u01b8\u01b7\u0001\u0000\u0000\u0000\u01b9\u01bb\u0001\u0000\u0000"+
		"\u0000\u01ba\u01b5\u0001\u0000\u0000\u0000\u01ba\u01bb\u0001\u0000\u0000"+
		"\u0000\u01bb+\u0001\u0000\u0000\u0000\u01bc\u01c0\u0003\u0088D\u0000\u01bd"+
		"\u01c0\u0003\u001e\u000f\u0000\u01be\u01c0\u0005`\u0000\u0000\u01bf\u01bc"+
		"\u0001\u0000\u0000\u0000\u01bf\u01bd\u0001\u0000\u0000\u0000\u01bf\u01be"+
		"\u0001\u0000\u0000\u0000\u01c0-\u0001\u0000\u0000\u0000\u01c1\u01c2\u0005"+
		"Y\u0000\u0000\u01c2\u01c8\u0005\u0007\u0000\u0000\u01c3\u01c4\u0003V+"+
		"\u0000\u01c4\u01c5\u0005\u0002\u0000\u0000\u01c5\u01c7\u0001\u0000\u0000"+
		"\u0000\u01c6\u01c3\u0001\u0000\u0000\u0000\u01c7\u01ca\u0001\u0000\u0000"+
		"\u0000\u01c8\u01c6\u0001\u0000\u0000\u0000\u01c8\u01c9\u0001\u0000\u0000"+
		"\u0000\u01c9\u01cb\u0001\u0000\u0000\u0000\u01ca\u01c8\u0001\u0000\u0000"+
		"\u0000\u01cb\u01cc\u0005\b\u0000\u0000\u01cc/\u0001\u0000\u0000\u0000"+
		"\u01cd\u01ce\u0005Z\u0000\u0000\u01ce\u01d4\u0005\u0007\u0000\u0000\u01cf"+
		"\u01d0\u0003V+\u0000\u01d0\u01d1\u0005\u0002\u0000\u0000\u01d1\u01d3\u0001"+
		"\u0000\u0000\u0000\u01d2\u01cf\u0001\u0000\u0000\u0000\u01d3\u01d6\u0001"+
		"\u0000\u0000\u0000\u01d4\u01d2\u0001\u0000\u0000\u0000\u01d4\u01d5\u0001"+
		"\u0000\u0000\u0000\u01d5\u01d7\u0001\u0000\u0000\u0000\u01d6\u01d4\u0001"+
		"\u0000\u0000\u0000\u01d7\u01d8\u0005\b\u0000\u0000\u01d81\u0001\u0000"+
		"\u0000\u0000\u01d9\u01da\u0005U\u0000\u0000\u01da\u01db\u0003\u0084B\u0000"+
		"\u01db\u01e4\u0005\u0004\u0000\u0000\u01dc\u01e1\u00034\u001a\u0000\u01dd"+
		"\u01de\u0005\u0005\u0000\u0000\u01de\u01e0\u00034\u001a\u0000\u01df\u01dd"+
		"\u0001\u0000\u0000\u0000\u01e0\u01e3\u0001\u0000\u0000\u0000\u01e1\u01df"+
		"\u0001\u0000\u0000\u0000\u01e1\u01e2\u0001\u0000\u0000\u0000\u01e2\u01e5"+
		"\u0001\u0000\u0000\u0000\u01e3\u01e1\u0001\u0000\u0000\u0000\u01e4\u01dc"+
		"\u0001\u0000\u0000\u0000\u01e4\u01e5\u0001\u0000\u0000\u0000\u01e5\u01e6"+
		"\u0001\u0000\u0000\u0000\u01e6\u01e7\u0005\u0006\u0000\u0000\u01e7\u01e8"+
		"\u0005\t\u0000\u0000\u01e8\u01ee\u0003x<\u0000\u01e9\u01ef\u0005\u0002"+
		"\u0000\u0000\u01ea\u01eb\u0005\u0007\u0000\u0000\u01eb\u01ec\u0003V+\u0000"+
		"\u01ec\u01ed\u0005\b\u0000\u0000\u01ed\u01ef\u0001\u0000\u0000\u0000\u01ee"+
		"\u01e9\u0001\u0000\u0000\u0000\u01ee\u01ea\u0001\u0000\u0000\u0000\u01ef"+
		"3\u0001\u0000\u0000\u0000\u01f0\u01f1\u0003\u0088D\u0000\u01f1\u01f2\u0005"+
		"\t\u0000\u0000\u01f2\u01f3\u0003x<\u0000\u01f35\u0001\u0000\u0000\u0000"+
		"\u01f4\u01f5\u0003V+\u0000\u01f5\u01f6\u0005\u0000\u0000\u0001\u01f67"+
		"\u0001\u0000\u0000\u0000\u01f7\u01f8\u0003:\u001d\u0000\u01f8\u01f9\u0005"+
		"\u0000\u0000\u0001\u01f99\u0001\u0000\u0000\u0000\u01fa\u01fc\u0003<\u001e"+
		"\u0000\u01fb\u01fa\u0001\u0000\u0000\u0000\u01fc\u01ff\u0001\u0000\u0000"+
		"\u0000\u01fd\u01fb\u0001\u0000\u0000\u0000\u01fd\u01fe\u0001\u0000\u0000"+
		"\u0000\u01fe\u0204\u0001\u0000\u0000\u0000\u01ff\u01fd\u0001\u0000\u0000"+
		"\u0000\u0200\u0203\u0003>\u001f\u0000\u0201\u0203\u0003@ \u0000\u0202"+
		"\u0200\u0001\u0000\u0000\u0000\u0202\u0201\u0001\u0000\u0000\u0000\u0203"+
		"\u0206\u0001\u0000\u0000\u0000\u0204\u0202\u0001\u0000\u0000\u0000\u0204"+
		"\u0205\u0001\u0000\u0000\u0000\u0205;\u0001\u0000\u0000\u0000\u0206\u0204"+
		"\u0001\u0000\u0000\u0000\u0207\u020b\u0007\u0001\u0000\u0000\u0208\u0209"+
		"\u0003\u0088D\u0000\u0209\u020a\u0005\t\u0000\u0000\u020a\u020c\u0001"+
		"\u0000\u0000\u0000\u020b\u0208\u0001\u0000\u0000\u0000\u020b\u020c\u0001"+
		"\u0000\u0000\u0000\u020c\u020d\u0001\u0000\u0000\u0000\u020d\u0210\u0003"+
		"\u0084B\u0000\u020e\u020f\u0005_\u0000\u0000\u020f\u0211\u0005\u0001\u0000"+
		"\u0000\u0210\u020e\u0001\u0000\u0000\u0000\u0210\u0211\u0001\u0000\u0000"+
		"\u0000\u0211=\u0001\u0000\u0000\u0000\u0212\u0213\u0005\u0010\u0000\u0000"+
		"\u0213\u0217\u0003\u0084B\u0000\u0214\u0216\u0003@ \u0000\u0215\u0214"+
		"\u0001\u0000\u0000\u0000\u0216\u0219\u0001\u0000\u0000\u0000\u0217\u0215"+
		"\u0001\u0000\u0000\u0000\u0217\u0218\u0001\u0000\u0000\u0000\u0218\u021a"+
		"\u0001\u0000\u0000\u0000\u0219\u0217\u0001\u0000\u0000\u0000\u021a\u021b"+
		"\u0005\u0011\u0000\u0000\u021b?\u0001\u0000\u0000\u0000\u021c\u0220\u0003"+
		"B!\u0000\u021d\u0220\u0003J%\u0000\u021e\u0220\u0003N\'\u0000\u021f\u021c"+
		"\u0001\u0000\u0000\u0000\u021f\u021d\u0001\u0000\u0000\u0000\u021f\u021e"+
		"\u0001\u0000\u0000\u0000\u0220A\u0001\u0000\u0000\u0000\u0221\u0225\u0005"+
		"\u0012\u0000\u0000\u0222\u0223\u0003\u0088D\u0000\u0223\u0224\u0005\t"+
		"\u0000\u0000\u0224\u0226\u0001\u0000\u0000\u0000\u0225\u0222\u0001\u0000"+
		"\u0000\u0000\u0225\u0226\u0001\u0000\u0000\u0000\u0226\u0227\u0001\u0000"+
		"\u0000\u0000\u0227\u0229\u0003\u0084B\u0000\u0228\u022a\u0003D\"\u0000"+
		"\u0229\u0228\u0001\u0000\u0000\u0000\u022a\u022b\u0001\u0000\u0000\u0000"+
		"\u022b\u0229\u0001\u0000\u0000\u0000\u022b\u022c\u0001\u0000\u0000\u0000"+
		"\u022cC\u0001\u0000\u0000\u0000\u022d\u0230\u0003F#\u0000\u022e\u0230"+
		"\u0003H$\u0000\u022f\u022d\u0001\u0000\u0000\u0000\u022f\u022e\u0001\u0000"+
		"\u0000\u0000\u0230E\u0001\u0000\u0000\u0000\u0231\u0239\u0005\u0013\u0000"+
		"\u0000\u0232\u0237\u0003\u0088D\u0000\u0233\u0234\u0005\u0004\u0000\u0000"+
		"\u0234\u0235\u0003V+\u0000\u0235\u0236\u0005\u0006\u0000\u0000\u0236\u0238"+
		"\u0001\u0000\u0000\u0000\u0237\u0233\u0001\u0000\u0000\u0000\u0237\u0238"+
		"\u0001\u0000\u0000\u0000\u0238\u023a\u0001\u0000\u0000\u0000\u0239\u0232"+
		"\u0001\u0000\u0000\u0000\u0239\u023a\u0001\u0000\u0000\u0000\u023a\u023b"+
		"\u0001\u0000\u0000\u0000\u023b\u023c\u0005\t\u0000\u0000\u023c\u023d\u0003"+
		"V+\u0000\u023dG\u0001\u0000\u0000\u0000\u023e\u0240\u0005\u0014\u0000"+
		"\u0000\u023f\u023e\u0001\u0000\u0000\u0000\u023f\u0240\u0001\u0000\u0000"+
		"\u0000\u0240\u0241\u0001\u0000\u0000\u0000\u0241\u0243\u0005\u0015\u0000"+
		"\u0000\u0242\u0244\u0003\u0088D\u0000\u0243\u0242\u0001\u0000\u0000\u0000"+
		"\u0243\u0244\u0001\u0000\u0000\u0000\u0244\u0245\u0001\u0000\u0000\u0000"+
		"\u0245\u0257\u0005\t\u0000\u0000\u0246\u0247\u0003\u0088D\u0000\u0247"+
		"\u0248\u0005\t\u0000\u0000\u0248\u0249\u0003x<\u0000\u0249\u024a\u0005"+
		"\n\u0000\u0000\u024a\u024b\u0003V+\u0000\u024b\u0258\u0001\u0000\u0000"+
		"\u0000\u024c\u024d\u0003\u0088D\u0000\u024d\u024f\u0005\u0004\u0000\u0000"+
		"\u024e\u0250\u0003R)\u0000\u024f\u024e\u0001\u0000\u0000\u0000\u024f\u0250"+
		"\u0001\u0000\u0000\u0000\u0250\u0251\u0001\u0000\u0000\u0000\u0251\u0252"+
		"\u0005\u0006\u0000\u0000\u0252\u0253\u0005\t\u0000\u0000\u0253\u0254\u0003"+
		"x<\u0000\u0254\u0255\u0005\n\u0000\u0000\u0255\u0256\u0003V+\u0000\u0256"+
		"\u0258\u0001\u0000\u0000\u0000\u0257\u0246\u0001\u0000\u0000\u0000\u0257"+
		"\u024c\u0001\u0000\u0000\u0000\u0258I\u0001\u0000\u0000\u0000\u0259\u025d"+
		"\u0005\u0012\u0000\u0000\u025a\u025b\u0003\u0084B\u0000\u025b\u025c\u0005"+
		"_\u0000\u0000\u025c\u025e\u0001\u0000\u0000\u0000\u025d\u025a\u0001\u0000"+
		"\u0000\u0000\u025d\u025e\u0001\u0000\u0000\u0000\u025e\u025f\u0001\u0000"+
		"\u0000\u0000\u025f\u0260\u0003\u0088D\u0000\u0260\u0262\u0005\u0004\u0000"+
		"\u0000\u0261\u0263\u0003R)\u0000\u0262\u0261\u0001\u0000\u0000\u0000\u0262"+
		"\u0263\u0001\u0000\u0000\u0000\u0263\u0264\u0001\u0000\u0000\u0000\u0264"+
		"\u0267\u0005\u0006\u0000\u0000\u0265\u0266\u0005\t\u0000\u0000\u0266\u0268"+
		"\u0003x<\u0000\u0267\u0265\u0001\u0000\u0000\u0000\u0267\u0268\u0001\u0000"+
		"\u0000\u0000\u0268\u026a\u0001\u0000\u0000\u0000\u0269\u026b\u0003L&\u0000"+
		"\u026a\u0269\u0001\u0000\u0000\u0000\u026b\u026c\u0001\u0000\u0000\u0000"+
		"\u026c\u026a\u0001\u0000\u0000\u0000\u026c\u026d\u0001\u0000\u0000\u0000"+
		"\u026dK\u0001\u0000\u0000\u0000\u026e\u0270\u0005\u0016\u0000\u0000\u026f"+
		"\u0271\u0003\u0088D\u0000\u0270\u026f\u0001\u0000\u0000\u0000\u0270\u0271"+
		"\u0001\u0000\u0000\u0000\u0271\u0272\u0001\u0000\u0000\u0000\u0272\u0273"+
		"\u0005\t\u0000\u0000\u0273\u0281\u0003V+\u0000\u0274\u0276\u0005\u0017"+
		"\u0000\u0000\u0275\u0277\u0003\u0088D\u0000\u0276\u0275\u0001\u0000\u0000"+
		"\u0000\u0276\u0277\u0001\u0000\u0000\u0000\u0277\u0278\u0001\u0000\u0000"+
		"\u0000\u0278\u0279\u0005\t\u0000\u0000\u0279\u0281\u0003V+\u0000\u027a"+
		"\u027c\u0005\u0018\u0000\u0000\u027b\u027d\u0003\u0088D\u0000\u027c\u027b"+
		"\u0001\u0000\u0000\u0000\u027c\u027d\u0001\u0000\u0000\u0000\u027d\u027e"+
		"\u0001\u0000\u0000\u0000\u027e\u027f\u0005\t\u0000\u0000\u027f\u0281\u0003"+
		"V+\u0000\u0280\u026e\u0001\u0000\u0000\u0000\u0280\u0274\u0001\u0000\u0000"+
		"\u0000\u0280\u027a\u0001\u0000\u0000\u0000\u0281M\u0001\u0000\u0000\u0000"+
		"\u0282\u0283\u0005\u0012\u0000\u0000\u0283\u0284\u0003\u0084B\u0000\u0284"+
		"\u0285\u0005_\u0000\u0000\u0285\u0286\u0003\u0088D\u0000\u0286\u0287\u0005"+
		"\t\u0000\u0000\u0287\u0289\u0003x<\u0000\u0288\u028a\u0003P(\u0000\u0289"+
		"\u0288\u0001\u0000\u0000\u0000\u028a\u028b\u0001\u0000\u0000\u0000\u028b"+
		"\u0289\u0001\u0000\u0000\u0000\u028b\u028c\u0001\u0000\u0000\u0000\u028c"+
		"O\u0001\u0000\u0000\u0000\u028d\u028f\u0005\u0019\u0000\u0000\u028e\u0290"+
		"\u0003\u0088D\u0000\u028f\u028e\u0001\u0000\u0000\u0000\u028f\u0290\u0001"+
		"\u0000\u0000\u0000\u0290\u0291\u0001\u0000\u0000\u0000\u0291\u0292\u0005"+
		"\t\u0000\u0000\u0292\u029a\u0003V+\u0000\u0293\u0295\u0005\u001a\u0000"+
		"\u0000\u0294\u0296\u0003\u0088D\u0000\u0295\u0294\u0001\u0000\u0000\u0000"+
		"\u0295\u0296\u0001\u0000\u0000\u0000\u0296\u0297\u0001\u0000\u0000\u0000"+
		"\u0297\u0298\u0005\t\u0000\u0000\u0298\u029a\u0003V+\u0000\u0299\u028d"+
		"\u0001\u0000\u0000\u0000\u0299\u0293\u0001\u0000\u0000\u0000\u029aQ\u0001"+
		"\u0000\u0000\u0000\u029b\u02a0\u0003T*\u0000\u029c\u029d\u0005\u0005\u0000"+
		"\u0000\u029d\u029f\u0003T*\u0000\u029e\u029c\u0001\u0000\u0000\u0000\u029f"+
		"\u02a2\u0001\u0000\u0000\u0000\u02a0\u029e\u0001\u0000\u0000\u0000\u02a0"+
		"\u02a1\u0001\u0000\u0000\u0000\u02a1S\u0001\u0000\u0000\u0000\u02a2\u02a0"+
		"\u0001\u0000\u0000\u0000\u02a3\u02a4\u0003\u0088D\u0000\u02a4\u02a5\u0005"+
		"\t\u0000\u0000\u02a5\u02a6\u0003x<\u0000\u02a6U\u0001\u0000\u0000\u0000"+
		"\u02a7\u02a8\u0006+\uffff\uffff\u0000\u02a8\u02a9\u0005\u001e\u0000\u0000"+
		"\u02a9\u02ae\u0003V+\u000b\u02aa\u02ab\u0005\u001f\u0000\u0000\u02ab\u02ae"+
		"\u0003V+\n\u02ac\u02ae\u0003X,\u0000\u02ad\u02a7\u0001\u0000\u0000\u0000"+
		"\u02ad\u02aa\u0001\u0000\u0000\u0000\u02ad\u02ac\u0001\u0000\u0000\u0000"+
		"\u02ae\u02cf\u0001\u0000\u0000\u0000\u02af\u02b0\n\t\u0000\u0000\u02b0"+
		"\u02b1\u0007\u0002\u0000\u0000\u02b1\u02ce\u0003V+\n\u02b2\u02b3\n\b\u0000"+
		"\u0000\u02b3\u02b4\u0007\u0003\u0000\u0000\u02b4\u02ce\u0003V+\t\u02b5"+
		"\u02b6\n\u0007\u0000\u0000\u02b6\u02b7\u0007\u0004\u0000\u0000\u02b7\u02ce"+
		"\u0003V+\b\u02b8\u02b9\n\u0006\u0000\u0000\u02b9\u02ba\u0007\u0005\u0000"+
		"\u0000\u02ba\u02ce\u0003V+\u0007\u02bb\u02bc\n\u0005\u0000\u0000\u02bc"+
		"\u02bd\u0005\'\u0000\u0000\u02bd\u02ce\u0003V+\u0006\u02be\u02bf\n\u0004"+
		"\u0000\u0000\u02bf\u02c0\u0005(\u0000\u0000\u02c0\u02ce\u0003V+\u0005"+
		"\u02c1\u02c2\n\u0003\u0000\u0000\u02c2\u02c3\u0005)\u0000\u0000\u02c3"+
		"\u02ce\u0003V+\u0004\u02c4\u02c5\n\u0002\u0000\u0000\u02c5\u02c6\u0005"+
		"*\u0000\u0000\u02c6\u02ce\u0003V+\u0003\u02c7\u02c8\n\r\u0000\u0000\u02c8"+
		"\u02c9\u0007\u0006\u0000\u0000\u02c9\u02ce\u0003Z-\u0000\u02ca\u02cb\n"+
		"\f\u0000\u0000\u02cb\u02cc\u0007\u0007\u0000\u0000\u02cc\u02ce\u0003\\"+
		".\u0000\u02cd\u02af\u0001\u0000\u0000\u0000\u02cd\u02b2\u0001\u0000\u0000"+
		"\u0000\u02cd\u02b5\u0001\u0000\u0000\u0000\u02cd\u02b8\u0001\u0000\u0000"+
		"\u0000\u02cd\u02bb\u0001\u0000\u0000\u0000\u02cd\u02be\u0001\u0000\u0000"+
		"\u0000\u02cd\u02c1\u0001\u0000\u0000\u0000\u02cd\u02c4\u0001\u0000\u0000"+
		"\u0000\u02cd\u02c7\u0001\u0000\u0000\u0000\u02cd\u02ca\u0001\u0000\u0000"+
		"\u0000\u02ce\u02d1\u0001\u0000\u0000\u0000\u02cf\u02cd\u0001\u0000\u0000"+
		"\u0000\u02cf\u02d0\u0001\u0000\u0000\u0000\u02d0W\u0001\u0000\u0000\u0000"+
		"\u02d1\u02cf\u0001\u0000\u0000\u0000\u02d2\u02d3\u0005\u0004\u0000\u0000"+
		"\u02d3\u02d4\u0003V+\u0000\u02d4\u02d5\u0005\u0006\u0000\u0000\u02d5\u0308"+
		"\u0001\u0000\u0000\u0000\u02d6\u0308\u0005+\u0000\u0000\u02d7\u02d8\u0005"+
		",\u0000\u0000\u02d8\u02d9\u0003V+\u0000\u02d9\u02da\u0005-\u0000\u0000"+
		"\u02da\u02e2\u0003V+\u0000\u02db\u02dc\u0005.\u0000\u0000\u02dc\u02dd"+
		"\u0003V+\u0000\u02dd\u02de\u0005-\u0000\u0000\u02de\u02df\u0003V+\u0000"+
		"\u02df\u02e1\u0001\u0000\u0000\u0000\u02e0\u02db\u0001\u0000\u0000\u0000"+
		"\u02e1\u02e4\u0001\u0000\u0000\u0000\u02e2\u02e0\u0001\u0000\u0000\u0000"+
		"\u02e2\u02e3\u0001\u0000\u0000\u0000\u02e3\u02e5\u0001\u0000\u0000\u0000"+
		"\u02e4\u02e2\u0001\u0000\u0000\u0000\u02e5\u02e6\u0005/\u0000\u0000\u02e6"+
		"\u02e7\u0003V+\u0000\u02e7\u02e8\u00050\u0000\u0000\u02e8\u0308\u0001"+
		"\u0000\u0000\u0000\u02e9\u02ea\u00051\u0000\u0000\u02ea\u02ef\u0003b1"+
		"\u0000\u02eb\u02ec\u0005\u0005\u0000\u0000\u02ec\u02ee\u0003b1\u0000\u02ed"+
		"\u02eb\u0001\u0000\u0000\u0000\u02ee\u02f1\u0001\u0000\u0000\u0000\u02ef"+
		"\u02ed\u0001\u0000\u0000\u0000\u02ef\u02f0\u0001\u0000\u0000\u0000\u02f0"+
		"\u02f2\u0001\u0000\u0000\u0000\u02f1\u02ef\u0001\u0000\u0000\u0000\u02f2"+
		"\u02f3\u00052\u0000\u0000\u02f3\u02f4\u0003V+\u0000\u02f4\u0308\u0001"+
		"\u0000\u0000\u0000\u02f5\u0308\u0003f3\u0000\u02f6\u02f8\u0003\u0084B"+
		"\u0000\u02f7\u02f9\u0003d2\u0000\u02f8\u02f7\u0001\u0000\u0000\u0000\u02f8"+
		"\u02f9\u0001\u0000\u0000\u0000\u02f9\u02fa\u0001\u0000\u0000\u0000\u02fa"+
		"\u02fc\u0005\u0004\u0000\u0000\u02fb\u02fd\u0003`0\u0000\u02fc\u02fb\u0001"+
		"\u0000\u0000\u0000\u02fc\u02fd\u0001\u0000\u0000\u0000\u02fd\u02fe\u0001"+
		"\u0000\u0000\u0000\u02fe\u0300\u0005\u0006\u0000\u0000\u02ff\u0301\u0003"+
		"d2\u0000\u0300\u02ff\u0001\u0000\u0000\u0000\u0300\u0301\u0001\u0000\u0000"+
		"\u0000\u0301\u0308\u0001\u0000\u0000\u0000\u0302\u0308\u0003z=\u0000\u0303"+
		"\u0308\u0003|>\u0000\u0304\u0308\u0003~?\u0000\u0305\u0308\u0003\u0080"+
		"@\u0000\u0306\u0308\u0003\u0084B\u0000\u0307\u02d2\u0001\u0000\u0000\u0000"+
		"\u0307\u02d6\u0001\u0000\u0000\u0000\u0307\u02d7\u0001\u0000\u0000\u0000"+
		"\u0307\u02e9\u0001\u0000\u0000\u0000\u0307\u02f5\u0001\u0000\u0000\u0000"+
		"\u0307\u02f6\u0001\u0000\u0000\u0000\u0307\u0302\u0001\u0000\u0000\u0000"+
		"\u0307\u0303\u0001\u0000\u0000\u0000\u0307\u0304\u0001\u0000\u0000\u0000"+
		"\u0307\u0305\u0001\u0000\u0000\u0000\u0307\u0306\u0001\u0000\u0000\u0000"+
		"\u0308Y\u0001\u0000\u0000\u0000\u0309\u030a\u0003\u0084B\u0000\u030a\u030b"+
		"\u0005_\u0000\u0000\u030b\u030d\u0001\u0000\u0000\u0000\u030c\u0309\u0001"+
		"\u0000\u0000\u0000\u030c\u030d\u0001\u0000\u0000\u0000\u030d\u030e\u0001"+
		"\u0000\u0000\u0000\u030e\u0310\u0003\u0088D\u0000\u030f\u0311\u0003d2"+
		"\u0000\u0310\u030f\u0001\u0000\u0000\u0000\u0310\u0311\u0001\u0000\u0000"+
		"\u0000\u0311\u0312\u0001\u0000\u0000\u0000\u0312\u0314\u0005\u0004\u0000"+
		"\u0000\u0313\u0315\u0003`0\u0000\u0314\u0313\u0001\u0000\u0000\u0000\u0314"+
		"\u0315\u0001\u0000\u0000\u0000\u0315\u0316\u0001\u0000\u0000\u0000\u0316"+
		"\u0318\u0005\u0006\u0000\u0000\u0317\u0319\u0003d2\u0000\u0318\u0317\u0001"+
		"\u0000\u0000\u0000\u0318\u0319\u0001\u0000\u0000\u0000\u0319\u0324\u0001"+
		"\u0000\u0000\u0000\u031a\u031b\u0003\u0084B\u0000\u031b\u031c\u0005_\u0000"+
		"\u0000\u031c\u031e\u0001\u0000\u0000\u0000\u031d\u031a\u0001\u0000\u0000"+
		"\u0000\u031d\u031e\u0001\u0000\u0000\u0000\u031e\u031f\u0001\u0000\u0000"+
		"\u0000\u031f\u0321\u0003\u0088D\u0000\u0320\u0322\u0003d2\u0000\u0321"+
		"\u0320\u0001\u0000\u0000\u0000\u0321\u0322\u0001\u0000\u0000\u0000\u0322"+
		"\u0324\u0001\u0000\u0000\u0000\u0323\u030c\u0001\u0000\u0000\u0000\u0323"+
		"\u031d\u0001\u0000\u0000\u0000\u0324[\u0001\u0000\u0000\u0000\u0325\u0326"+
		"\u0003\u0088D\u0000\u0326\u0327\u0005\u0004\u0000\u0000\u0327\u0328\u0003"+
		"^/\u0000\u0328\u0329\u00053\u0000\u0000\u0329\u032a\u0003V+\u0000\u032a"+
		"\u032b\u0005\u0006\u0000\u0000\u032b\u034a\u0001\u0000\u0000\u0000\u032c"+
		"\u032d\u0003\u0088D\u0000\u032d\u0335\u0005\u0004\u0000\u0000\u032e\u0331"+
		"\u0003\u0088D\u0000\u032f\u0330\u0005\t\u0000\u0000\u0330\u0332\u0003"+
		"x<\u0000\u0331\u032f\u0001\u0000\u0000\u0000\u0331\u0332\u0001\u0000\u0000"+
		"\u0000\u0332\u0333\u0001\u0000\u0000\u0000\u0333\u0334\u0005\u0002\u0000"+
		"\u0000\u0334\u0336\u0001\u0000\u0000\u0000\u0335\u032e\u0001\u0000\u0000"+
		"\u0000\u0335\u0336\u0001\u0000\u0000\u0000\u0336\u0337\u0001\u0000\u0000"+
		"\u0000\u0337\u033a\u0003\u0088D\u0000\u0338\u0339\u0005\t\u0000\u0000"+
		"\u0339\u033b\u0003x<\u0000\u033a\u0338\u0001\u0000\u0000\u0000\u033a\u033b"+
		"\u0001\u0000\u0000\u0000\u033b\u033c\u0001\u0000\u0000\u0000\u033c\u033d"+
		"\u0005\n\u0000\u0000\u033d\u033e\u0003V+\u0000\u033e\u033f\u00053\u0000"+
		"\u0000\u033f\u0340\u0003V+\u0000\u0340\u0341\u0005\u0006\u0000\u0000\u0341"+
		"\u034a\u0001\u0000\u0000\u0000\u0342\u0343\u0003\u0088D\u0000\u0343\u0345"+
		"\u0005\u0004\u0000\u0000\u0344\u0346\u0003`0\u0000\u0345\u0344\u0001\u0000"+
		"\u0000\u0000\u0345\u0346\u0001\u0000\u0000\u0000\u0346\u0347\u0001\u0000"+
		"\u0000\u0000\u0347\u0348\u0005\u0006\u0000\u0000\u0348\u034a\u0001\u0000"+
		"\u0000\u0000\u0349\u0325\u0001\u0000\u0000\u0000\u0349\u032c\u0001\u0000"+
		"\u0000\u0000\u0349\u0342\u0001\u0000\u0000\u0000\u034a]\u0001\u0000\u0000"+
		"\u0000\u034b\u034e\u0003\u0088D\u0000\u034c\u034d\u0005\t\u0000\u0000"+
		"\u034d\u034f\u0003x<\u0000\u034e\u034c\u0001\u0000\u0000\u0000\u034e\u034f"+
		"\u0001\u0000\u0000\u0000\u034f\u0358\u0001\u0000\u0000\u0000\u0350\u0351"+
		"\u0005\u0005\u0000\u0000\u0351\u0354\u0003\u0088D\u0000\u0352\u0353\u0005"+
		"\t\u0000\u0000\u0353\u0355\u0003x<\u0000\u0354\u0352\u0001\u0000\u0000"+
		"\u0000\u0354\u0355\u0001\u0000\u0000\u0000\u0355\u0357\u0001\u0000\u0000"+
		"\u0000\u0356\u0350\u0001\u0000\u0000\u0000\u0357\u035a\u0001\u0000\u0000"+
		"\u0000\u0358\u0356\u0001\u0000\u0000\u0000\u0358\u0359\u0001\u0000\u0000"+
		"\u0000\u0359_\u0001\u0000\u0000\u0000\u035a\u0358\u0001\u0000\u0000\u0000"+
		"\u035b\u0360\u0003V+\u0000\u035c\u035d\u0005\u0005\u0000\u0000\u035d\u035f"+
		"\u0003V+\u0000\u035e\u035c\u0001\u0000\u0000\u0000\u035f\u0362\u0001\u0000"+
		"\u0000\u0000\u0360\u035e\u0001\u0000\u0000\u0000\u0360\u0361\u0001\u0000"+
		"\u0000\u0000\u0361a\u0001\u0000\u0000\u0000\u0362\u0360\u0001\u0000\u0000"+
		"\u0000\u0363\u0366\u0003\u0088D\u0000\u0364\u0365\u0005\t\u0000\u0000"+
		"\u0365\u0367\u0003x<\u0000\u0366\u0364\u0001\u0000\u0000\u0000\u0366\u0367"+
		"\u0001\u0000\u0000\u0000\u0367\u0368\u0001\u0000\u0000\u0000\u0368\u0369"+
		"\u0005\n\u0000\u0000\u0369\u036a\u0003V+\u0000\u036ac\u0001\u0000\u0000"+
		"\u0000\u036b\u036c\u00054\u0000\u0000\u036c\u036d\u0005\u0016\u0000\u0000"+
		"\u036de\u0001\u0000\u0000\u0000\u036e\u037a\u0005b\u0000\u0000\u036f\u037a"+
		"\u0005a\u0000\u0000\u0370\u037a\u0003h4\u0000\u0371\u037a\u00055\u0000"+
		"\u0000\u0372\u037a\u00056\u0000\u0000\u0373\u037a\u00057\u0000\u0000\u0374"+
		"\u037a\u00058\u0000\u0000\u0375\u037a\u0005\u0001\u0000\u0000\u0376\u037a"+
		"\u0003j5\u0000\u0377\u037a\u0003p8\u0000\u0378\u037a\u0003t:\u0000\u0379"+
		"\u036e\u0001\u0000\u0000\u0000\u0379\u036f\u0001\u0000\u0000\u0000\u0379"+
		"\u0370\u0001\u0000\u0000\u0000\u0379\u0371\u0001\u0000\u0000\u0000\u0379"+
		"\u0372\u0001\u0000\u0000\u0000\u0379\u0373\u0001\u0000\u0000\u0000\u0379"+
		"\u0374\u0001\u0000\u0000\u0000\u0379\u0375\u0001\u0000\u0000\u0000\u0379"+
		"\u0376\u0001\u0000\u0000\u0000\u0379\u0377\u0001\u0000\u0000\u0000\u0379"+
		"\u0378\u0001\u0000\u0000\u0000\u037ag\u0001\u0000\u0000\u0000\u037b\u037d"+
		"\u0005c\u0000\u0000\u037c\u037b\u0001\u0000\u0000\u0000\u037d\u037e\u0001"+
		"\u0000\u0000\u0000\u037e\u037c\u0001\u0000\u0000\u0000\u037e\u037f\u0001"+
		"\u0000\u0000\u0000\u037fi\u0001\u0000\u0000\u0000\u0380\u0381\u0003l6"+
		"\u0000\u0381\u038a\u0005\u0007\u0000\u0000\u0382\u0387\u0003n7\u0000\u0383"+
		"\u0384\u0005\u0005\u0000\u0000\u0384\u0386\u0003n7\u0000\u0385\u0383\u0001"+
		"\u0000\u0000\u0000\u0386\u0389\u0001\u0000\u0000\u0000\u0387\u0385\u0001"+
		"\u0000\u0000\u0000\u0387\u0388\u0001\u0000\u0000\u0000\u0388\u038b\u0001"+
		"\u0000\u0000\u0000\u0389\u0387\u0001\u0000\u0000\u0000\u038a\u0382\u0001"+
		"\u0000\u0000\u0000\u038a\u038b\u0001\u0000\u0000\u0000\u038b\u038c\u0001"+
		"\u0000\u0000\u0000\u038c\u038d\u0005\b\u0000\u0000\u038dk\u0001\u0000"+
		"\u0000\u0000\u038e\u038f\u0007\b\u0000\u0000\u038fm\u0001\u0000\u0000"+
		"\u0000\u0390\u0393\u0003V+\u0000\u0391\u0392\u0005>\u0000\u0000\u0392"+
		"\u0394\u0003V+\u0000\u0393\u0391\u0001\u0000\u0000\u0000\u0393\u0394\u0001"+
		"\u0000\u0000\u0000\u0394o\u0001\u0000\u0000\u0000\u0395\u0396\u0005?\u0000"+
		"\u0000\u0396\u0397\u0005\u0007\u0000\u0000\u0397\u039c\u0003r9\u0000\u0398"+
		"\u0399\u0005\u0005\u0000\u0000\u0399\u039b\u0003r9\u0000\u039a\u0398\u0001"+
		"\u0000\u0000\u0000\u039b\u039e\u0001\u0000\u0000\u0000\u039c\u039a\u0001"+
		"\u0000\u0000\u0000\u039c\u039d\u0001\u0000\u0000\u0000\u039d\u039f\u0001"+
		"\u0000\u0000\u0000\u039e\u039c\u0001\u0000\u0000\u0000\u039f\u03a0\u0005"+
		"\b\u0000\u0000\u03a0q\u0001\u0000\u0000\u0000\u03a1\u03a4\u0003\u0088"+
		"D\u0000\u03a2\u03a3\u0005\t\u0000\u0000\u03a3\u03a5\u0003x<\u0000\u03a4"+
		"\u03a2\u0001\u0000\u0000\u0000\u03a4\u03a5\u0001\u0000\u0000\u0000\u03a5"+
		"\u03a6\u0001\u0000\u0000\u0000\u03a6\u03a7\u0005\n\u0000\u0000\u03a7\u03a8"+
		"\u0003V+\u0000\u03a8s\u0001\u0000\u0000\u0000\u03a9\u03aa\u0005@\u0000"+
		"\u0000\u03aa\u03b3\u0005\u0007\u0000\u0000\u03ab\u03b0\u0003v;\u0000\u03ac"+
		"\u03ad\u0005\u0005\u0000\u0000\u03ad\u03af\u0003v;\u0000\u03ae\u03ac\u0001"+
		"\u0000\u0000\u0000\u03af\u03b2\u0001\u0000\u0000\u0000\u03b0\u03ae\u0001"+
		"\u0000\u0000\u0000\u03b0\u03b1\u0001\u0000\u0000\u0000\u03b1\u03b4\u0001"+
		"\u0000\u0000\u0000\u03b2\u03b0\u0001\u0000\u0000\u0000\u03b3\u03ab\u0001"+
		"\u0000\u0000\u0000\u03b3\u03b4\u0001\u0000\u0000\u0000\u03b4\u03b5\u0001"+
		"\u0000\u0000\u0000\u03b5\u03b6\u0005\b\u0000\u0000\u03b6u\u0001\u0000"+
		"\u0000\u0000\u03b7\u03b8\u0003V+\u0000\u03b8\u03b9\u0007\t\u0000\u0000"+
		"\u03b9\u03ba\u0003V+\u0000\u03baw\u0001\u0000\u0000\u0000\u03bb\u03c1"+
		"\u0003z=\u0000\u03bc\u03c1\u0003|>\u0000\u03bd\u03c1\u0003~?\u0000\u03be"+
		"\u03c1\u0003\u0080@\u0000\u03bf\u03c1\u0003\u0084B\u0000\u03c0\u03bb\u0001"+
		"\u0000\u0000\u0000\u03c0\u03bc\u0001\u0000\u0000\u0000\u03c0\u03bd\u0001"+
		"\u0000\u0000\u0000\u03c0\u03be\u0001\u0000\u0000\u0000\u03c0\u03bf\u0001"+
		"\u0000\u0000\u0000\u03c1y\u0001\u0000\u0000\u0000\u03c2\u03c3\u0007\n"+
		"\u0000\u0000\u03c3{\u0001\u0000\u0000\u0000\u03c4\u03c5\u0003l6\u0000"+
		"\u03c5\u03c6\u0005\u0004\u0000\u0000\u03c6\u03c7\u0003x<\u0000\u03c7\u03c8"+
		"\u0005\u0006\u0000\u0000\u03c8}\u0001\u0000\u0000\u0000\u03c9\u03ca\u0005"+
		"@\u0000\u0000\u03ca\u03cb\u0005\u0004\u0000\u0000\u03cb\u03cc\u0003x<"+
		"\u0000\u03cc\u03cd\u0005\u0005\u0000\u0000\u03cd\u03ce\u0003x<\u0000\u03ce"+
		"\u03cf\u0005\u0006\u0000\u0000\u03cf\u007f\u0001\u0000\u0000\u0000\u03d0"+
		"\u03d1\u0005?\u0000\u0000\u03d1\u03d2\u0005\u0004\u0000\u0000\u03d2\u03d7"+
		"\u0003\u0082A\u0000\u03d3\u03d4\u0005\u0005\u0000\u0000\u03d4\u03d6\u0003"+
		"\u0082A\u0000\u03d5\u03d3\u0001\u0000\u0000\u0000\u03d6\u03d9\u0001\u0000"+
		"\u0000\u0000\u03d7\u03d5\u0001\u0000\u0000\u0000\u03d7\u03d8\u0001\u0000"+
		"\u0000\u0000\u03d8\u03da\u0001\u0000\u0000\u0000\u03d9\u03d7\u0001\u0000"+
		"\u0000\u0000\u03da\u03db\u0005\u0006\u0000\u0000\u03db\u0081\u0001\u0000"+
		"\u0000\u0000\u03dc\u03dd\u0003\u0088D\u0000\u03dd\u03de\u0005\t\u0000"+
		"\u0000\u03de\u03df\u0003x<\u0000\u03df\u0083\u0001\u0000\u0000\u0000\u03e0"+
		"\u03e8\u0003\u0088D\u0000\u03e1\u03e4\u0005_\u0000\u0000\u03e2\u03e5\u0003"+
		"\u0088D\u0000\u03e3\u03e5\u0003\u0086C\u0000\u03e4\u03e2\u0001\u0000\u0000"+
		"\u0000\u03e4\u03e3\u0001\u0000\u0000\u0000\u03e5\u03e7\u0001\u0000\u0000"+
		"\u0000\u03e6\u03e1\u0001\u0000\u0000\u0000\u03e7\u03ea\u0001\u0000\u0000"+
		"\u0000\u03e8\u03e6\u0001\u0000\u0000\u0000\u03e8\u03e9\u0001\u0000\u0000"+
		"\u0000\u03e9\u0085\u0001\u0000\u0000\u0000\u03ea\u03e8\u0001\u0000\u0000"+
		"\u0000\u03eb\u03ec\u0007\u000b\u0000\u0000\u03ec\u0087\u0001\u0000\u0000"+
		"\u0000\u03ed\u03f1\u0005e\u0000\u0000\u03ee\u03f1\u0005d\u0000\u0000\u03ef"+
		"\u03f1\u0003\u008aE\u0000\u03f0\u03ed\u0001\u0000\u0000\u0000\u03f0\u03ee"+
		"\u0001\u0000\u0000\u0000\u03f0\u03ef\u0001\u0000\u0000\u0000\u03f1\u0089"+
		"\u0001\u0000\u0000\u0000\u03f2\u03f3\u0007\f\u0000\u0000\u03f3\u008b\u0001"+
		"\u0000\u0000\u0000u\u0092\u0098\u00a1\u00a6\u00af\u00ba\u00c0\u00c6\u00cb"+
		"\u00cd\u00db\u00e0\u00ec\u00fa\u00fd\u0100\u0106\u010c\u0111\u0113\u0116"+
		"\u0119\u0122\u0129\u012e\u0137\u013e\u0142\u0149\u014d\u0161\u0167\u016a"+
		"\u016f\u0173\u0180\u019d\u01a0\u01a9\u01b2\u01b8\u01ba\u01bf\u01c8\u01d4"+
		"\u01e1\u01e4\u01ee\u01fd\u0202\u0204\u020b\u0210\u0217\u021f\u0225\u022b"+
		"\u022f\u0237\u0239\u023f\u0243\u024f\u0257\u025d\u0262\u0267\u026c\u0270"+
		"\u0276\u027c\u0280\u028b\u028f\u0295\u0299\u02a0\u02ad\u02cd\u02cf\u02e2"+
		"\u02ef\u02f8\u02fc\u0300\u0307\u030c\u0310\u0314\u0318\u031d\u0321\u0323"+
		"\u0331\u0335\u033a\u0345\u0349\u034e\u0354\u0358\u0360\u0366\u0379\u037e"+
		"\u0387\u038a\u0393\u039c\u03a4\u03b0\u03b3\u03c0\u03d7\u03e4\u03e8\u03f0";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}