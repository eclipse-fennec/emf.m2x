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
		RULE_tupleType = 64, RULE_tupleTypePart = 65, RULE_pathName = 66, RULE_identifier = 67;
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
			"pathName", "identifier"
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
			setState(136);
			compilationUnit();
			setState(137);
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
			setState(142);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(139);
				importDecl();
				}
				}
				setState(144);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(148);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TRANSFORMATION) {
				{
				{
				setState(145);
				transformationDef();
				}
				}
				setState(150);
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
			setState(151);
			match(IMPORT);
			setState(152);
			qualifiedName();
			setState(157);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(153);
					match(COLONCOLON);
					setState(154);
					qualifiedName();
					}
					} 
				}
				setState(159);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(162);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLONCOLON) {
				{
				setState(160);
				match(COLONCOLON);
				setState(161);
				match(T__0);
				}
			}

			setState(164);
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
			setState(166);
			identifier();
			setState(171);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(167);
				match(T__2);
				setState(168);
				identifier();
				}
				}
				setState(173);
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
			setState(174);
			match(TRANSFORMATION);
			setState(175);
			identifier();
			setState(176);
			match(T__3);
			setState(177);
			modelDecl();
			setState(182);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(178);
				match(T__4);
				setState(179);
				modelDecl();
				}
				}
				setState(184);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(185);
			match(T__5);
			setState(188);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(186);
				match(EXTENDS);
				setState(187);
				identifier();
				}
			}

			setState(190);
			match(T__6);
			setState(194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==KEY) {
				{
				{
				setState(191);
				keyDecl();
				}
				}
				setState(196);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(201);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 85)) & ~0x3f) == 0 && ((1L << (_la - 85)) & 135L) != 0)) {
				{
				setState(199);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case RELATION:
				case TOP:
				case ABSTRACT:
					{
					setState(197);
					relation();
					}
					break;
				case QUERY:
					{
					setState(198);
					queryDef();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(203);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(204);
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
			setState(206);
			identifier();
			setState(207);
			match(T__8);
			setState(220);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				{
				setState(208);
				metaModelId();
				}
				break;
			case T__6:
				{
				setState(209);
				match(T__6);
				setState(210);
				metaModelId();
				setState(215);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(211);
					match(T__4);
					setState(212);
					metaModelId();
					}
					}
					setState(217);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(218);
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
			setState(222);
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
			setState(224);
			match(KEY);
			setState(225);
			pathName();
			setState(226);
			match(T__6);
			setState(227);
			keyProperty();
			setState(232);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(228);
				match(T__4);
				setState(229);
				keyProperty();
				}
				}
				setState(234);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(235);
			match(T__7);
			setState(236);
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
			setState(246);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(238);
				identifier();
				}
				break;
			case OPPOSITE:
				enterOuterAlt(_localctx, 2);
				{
				setState(239);
				match(OPPOSITE);
				setState(240);
				match(T__3);
				setState(241);
				pathName();
				setState(242);
				match(COLONCOLON);
				setState(243);
				identifier();
				setState(244);
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
			setState(249);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TOP) {
				{
				setState(248);
				match(TOP);
				}
			}

			setState(252);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ABSTRACT) {
				{
				setState(251);
				match(ABSTRACT);
				}
			}

			setState(254);
			match(RELATION);
			setState(255);
			identifier();
			setState(258);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OVERRIDES) {
				{
				setState(256);
				match(OVERRIDES);
				setState(257);
				identifier();
				}
			}

			setState(260);
			match(T__6);
			setState(264);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				{
				setState(261);
				varDeclaration();
				}
				}
				setState(266);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(269); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(269);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case CHECKONLY:
				case DOMAIN:
				case ENFORCE:
					{
					setState(267);
					domain();
					}
					break;
				case PRIMITIVE:
					{
					setState(268);
					primitiveTypeDomain();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(271); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & 263L) != 0) );
			setState(274);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHEN) {
				{
				setState(273);
				whenClause();
				}
			}

			setState(277);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(276);
				whereClause();
				}
			}

			setState(279);
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
			setState(281);
			identifier();
			setState(286);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(282);
				match(T__4);
				setState(283);
				identifier();
				}
				}
				setState(288);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(289);
			match(T__8);
			setState(290);
			typeExpression();
			setState(293);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(291);
				match(T__9);
				setState(292);
				expression(0);
				}
			}

			setState(295);
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
			setState(298);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CHECKONLY || _la==ENFORCE) {
				{
				setState(297);
				checkEnforceQualifier();
				}
			}

			setState(300);
			match(DOMAIN);
			setState(301);
			identifier();
			setState(302);
			template();
			setState(307);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(303);
				match(T__4);
				setState(304);
				template();
				}
				}
				setState(309);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(318);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IMPLEMENTEDBY) {
				{
				setState(310);
				match(IMPLEMENTEDBY);
				setState(311);
				pathName();
				setState(312);
				match(T__3);
				setState(314);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876228415470L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266287976441L) != 0)) {
					{
					setState(313);
					argumentList();
					}
				}

				setState(316);
				match(T__5);
				}
			}

			setState(329);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT_VALUES) {
				{
				setState(320);
				match(DEFAULT_VALUES);
				setState(321);
				match(T__6);
				setState(323); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(322);
					assignmentExp();
					}
					}
					setState(325); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==ESCAPED_IDENTIFIER || _la==IDENTIFIER );
				setState(327);
				match(T__7);
				}
			}

			setState(331);
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
			setState(333);
			match(PRIMITIVE);
			setState(334);
			match(DOMAIN);
			setState(335);
			identifier();
			setState(336);
			match(T__8);
			setState(337);
			typeExpression();
			setState(338);
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
			setState(340);
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
			setState(342);
			identifier();
			setState(343);
			match(T__9);
			setState(344);
			expression(0);
			setState(345);
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
			setState(349);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(347);
				objectTemplate();
				}
				break;
			case 2:
				{
				setState(348);
				collectionTemplate();
				}
				break;
			}
			setState(355);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(351);
				match(T__6);
				setState(352);
				expression(0);
				setState(353);
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
			setState(358);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(357);
				identifier();
				}
			}

			setState(360);
			match(T__8);
			setState(361);
			pathName();
			setState(363);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(362);
				optionalMultiplicity();
				}
			}

			setState(365);
			match(T__6);
			setState(367);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 93)) & ~0x3f) == 0 && ((1L << (_la - 93)) & 385L) != 0)) {
				{
				setState(366);
				propertyTemplateList();
				}
			}

			setState(369);
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
			setState(371);
			match(T__10);
			setState(372);
			match(T__11);
			setState(373);
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
			setState(375);
			propertyTemplate();
			setState(380);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(376);
				match(T__4);
				setState(377);
				propertyTemplate();
				}
				}
				setState(382);
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
			setState(409);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(383);
				identifier();
				setState(384);
				match(T__9);
				setState(385);
				template();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(387);
				identifier();
				setState(388);
				match(T__9);
				setState(389);
				expression(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(391);
				match(OPPOSITE);
				setState(392);
				match(T__3);
				setState(393);
				pathName();
				setState(394);
				match(COLONCOLON);
				setState(395);
				identifier();
				setState(396);
				match(T__5);
				setState(397);
				match(T__9);
				setState(398);
				template();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(400);
				match(OPPOSITE);
				setState(401);
				match(T__3);
				setState(402);
				pathName();
				setState(403);
				match(COLONCOLON);
				setState(404);
				identifier();
				setState(405);
				match(T__5);
				setState(406);
				match(T__9);
				setState(407);
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
			setState(412);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(411);
				identifier();
				}
			}

			setState(414);
			match(T__8);
			setState(415);
			collectionKind();
			setState(416);
			match(T__3);
			setState(417);
			typeExpression();
			setState(418);
			match(T__5);
			setState(419);
			match(T__6);
			setState(421);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8 || ((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & 49L) != 0)) {
				{
				setState(420);
				memberSelection();
				}
			}

			setState(423);
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
			setState(425);
			memberItem();
			setState(430);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(426);
				match(T__4);
				setState(427);
				memberItem();
				}
				}
				setState(432);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(438);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUSPLUS) {
				{
				setState(433);
				match(PLUSPLUS);
				setState(436);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case ESCAPED_IDENTIFIER:
				case IDENTIFIER:
					{
					setState(434);
					identifier();
					}
					break;
				case UNDERSCORE:
					{
					setState(435);
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
			setState(443);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(440);
				identifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(441);
				template();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(442);
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
			setState(445);
			match(WHEN);
			setState(446);
			match(T__6);
			setState(452);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876228415470L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266287976441L) != 0)) {
				{
				{
				setState(447);
				expression(0);
				setState(448);
				match(T__1);
				}
				}
				setState(454);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(455);
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
			setState(457);
			match(WHERE);
			setState(458);
			match(T__6);
			setState(464);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876228415470L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266287976441L) != 0)) {
				{
				{
				setState(459);
				expression(0);
				setState(460);
				match(T__1);
				}
				}
				setState(466);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(467);
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
			setState(469);
			match(QUERY);
			setState(470);
			pathName();
			setState(471);
			match(T__3);
			setState(480);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(472);
				paramDecl();
				setState(477);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(473);
					match(T__4);
					setState(474);
					paramDecl();
					}
					}
					setState(479);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(482);
			match(T__5);
			setState(483);
			match(T__8);
			setState(484);
			typeExpression();
			setState(490);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(485);
				match(T__1);
				}
				break;
			case T__6:
				{
				setState(486);
				match(T__6);
				setState(487);
				expression(0);
				setState(488);
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
			setState(492);
			identifier();
			setState(493);
			match(T__8);
			setState(494);
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
			setState(496);
			expression(0);
			setState(497);
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
			setState(499);
			completeOclDocument();
			setState(500);
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
			setState(505);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__13 || _la==T__14 || _la==IMPORT) {
				{
				{
				setState(502);
				importDeclaration();
				}
				}
				setState(507);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(512);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__15 || _la==T__17) {
				{
				setState(510);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__15:
					{
					setState(508);
					packageDeclaration();
					}
					break;
				case T__17:
					{
					setState(509);
					contextDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(514);
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
			setState(515);
			_la = _input.LA(1);
			if ( !(_la==T__13 || _la==T__14 || _la==IMPORT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(519);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				{
				setState(516);
				identifier();
				setState(517);
				match(T__8);
				}
				break;
			}
			setState(521);
			pathName();
			setState(524);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLONCOLON) {
				{
				setState(522);
				match(COLONCOLON);
				setState(523);
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
			setState(526);
			match(T__15);
			setState(527);
			pathName();
			setState(531);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__17) {
				{
				{
				setState(528);
				contextDeclaration();
				}
				}
				setState(533);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(534);
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
			setState(539);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(536);
				classifierContextDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(537);
				operationContextDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(538);
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
			setState(541);
			match(T__17);
			setState(545);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				{
				setState(542);
				identifier();
				setState(543);
				match(T__8);
				}
				break;
			}
			setState(547);
			pathName();
			setState(549); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(548);
				classifierContextBody();
				}
				}
				setState(551); 
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
			setState(555);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__18:
				enterOuterAlt(_localctx, 1);
				{
				setState(553);
				invariantConstraint();
				}
				break;
			case T__19:
			case T__20:
				enterOuterAlt(_localctx, 2);
				{
				setState(554);
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
			setState(557);
			match(T__18);
			setState(565);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(558);
				identifier();
				setState(563);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(559);
					match(T__3);
					setState(560);
					expression(0);
					setState(561);
					match(T__5);
					}
				}

				}
			}

			setState(567);
			match(T__8);
			setState(568);
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
			setState(571);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__19) {
				{
				setState(570);
				match(T__19);
				}
			}

			setState(573);
			match(T__20);
			setState(575);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(574);
				identifier();
				}
			}

			setState(577);
			match(T__8);
			setState(595);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				{
				setState(578);
				identifier();
				setState(579);
				match(T__8);
				setState(580);
				typeExpression();
				setState(581);
				match(T__9);
				setState(582);
				expression(0);
				}
				break;
			case 2:
				{
				setState(584);
				identifier();
				setState(585);
				match(T__3);
				setState(587);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(586);
					parameterList();
					}
				}

				setState(589);
				match(T__5);
				setState(590);
				match(T__8);
				setState(591);
				typeExpression();
				setState(592);
				match(T__9);
				setState(593);
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
			setState(597);
			match(T__17);
			setState(601);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				{
				setState(598);
				pathName();
				setState(599);
				match(COLONCOLON);
				}
				break;
			}
			setState(603);
			identifier();
			setState(604);
			match(T__3);
			setState(606);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(605);
				parameterList();
				}
			}

			setState(608);
			match(T__5);
			setState(611);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(609);
				match(T__8);
				setState(610);
				typeExpression();
				}
			}

			setState(614); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(613);
				operationContextBody();
				}
				}
				setState(616); 
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
			setState(636);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__21:
				_localctx = new PreConditionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(618);
				match(T__21);
				setState(620);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(619);
					identifier();
					}
				}

				setState(622);
				match(T__8);
				setState(623);
				expression(0);
				}
				break;
			case T__22:
				_localctx = new PostConditionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(624);
				match(T__22);
				setState(626);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(625);
					identifier();
					}
				}

				setState(628);
				match(T__8);
				setState(629);
				expression(0);
				}
				break;
			case T__23:
				_localctx = new BodyExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(630);
				match(T__23);
				setState(632);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(631);
					identifier();
					}
				}

				setState(634);
				match(T__8);
				setState(635);
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
			setState(638);
			match(T__17);
			setState(639);
			pathName();
			setState(640);
			match(COLONCOLON);
			setState(641);
			identifier();
			setState(642);
			match(T__8);
			setState(643);
			typeExpression();
			setState(645); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(644);
				propertyContextBody();
				}
				}
				setState(647); 
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
			setState(661);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__24:
				_localctx = new InitExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(649);
				match(T__24);
				setState(651);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(650);
					identifier();
					}
				}

				setState(653);
				match(T__8);
				setState(654);
				expression(0);
				}
				break;
			case T__25:
				_localctx = new DeriveExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(655);
				match(T__25);
				setState(657);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(656);
					identifier();
					}
				}

				setState(659);
				match(T__8);
				setState(660);
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
			setState(663);
			parameter();
			setState(668);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(664);
				match(T__4);
				setState(665);
				parameter();
				}
				}
				setState(670);
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
			setState(671);
			identifier();
			setState(672);
			match(T__8);
			setState(673);
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
			setState(681);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__29:
				{
				_localctx = new NotExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(676);
				match(T__29);
				setState(677);
				expression(11);
				}
				break;
			case T__30:
				{
				_localctx = new UnaryMinusExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(678);
				match(T__30);
				setState(679);
				expression(10);
				}
				break;
			case T__0:
			case T__3:
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
			case REAL_LITERAL:
			case INTEGER_LITERAL:
			case STRING_LITERAL:
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				{
				_localctx = new PrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(680);
				primaryExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(715);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(713);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
					case 1:
						{
						_localctx = new MultExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(683);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(684);
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
						setState(685);
						expression(10);
						}
						break;
					case 2:
						{
						_localctx = new AddExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(686);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(687);
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
						setState(688);
						expression(9);
						}
						break;
					case 3:
						{
						_localctx = new CompareExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(689);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(690);
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
						setState(691);
						expression(8);
						}
						break;
					case 4:
						{
						_localctx = new EqualityExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(692);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(693);
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
						setState(694);
						expression(7);
						}
						break;
					case 5:
						{
						_localctx = new AndExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(695);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(696);
						match(T__38);
						setState(697);
						expression(6);
						}
						break;
					case 6:
						{
						_localctx = new OrExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(698);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(699);
						match(T__39);
						setState(700);
						expression(5);
						}
						break;
					case 7:
						{
						_localctx = new XorExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(701);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(702);
						match(T__40);
						setState(703);
						expression(4);
						}
						break;
					case 8:
						{
						_localctx = new ImpliesExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(704);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(705);
						match(T__41);
						setState(706);
						expression(3);
						}
						break;
					case 9:
						{
						_localctx = new NavigationExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(707);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(708);
						_la = _input.LA(1);
						if ( !(_la==T__2 || _la==T__26) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(709);
						propertyOrCallSuffix();
						}
						break;
					case 10:
						{
						_localctx = new ArrowExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(710);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(711);
						_la = _input.LA(1);
						if ( !(_la==T__27 || _la==T__28) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(712);
						iteratorOrOperationCall();
						}
						break;
					}
					} 
				}
				setState(717);
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
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public IsMarkedPreContext isMarkedPre() {
			return getRuleContext(IsMarkedPreContext.class,0);
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
			setState(768);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				_localctx = new ParenExpContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(718);
				match(T__3);
				setState(719);
				expression(0);
				setState(720);
				match(T__5);
				}
				break;
			case 2:
				_localctx = new SelfExpContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(722);
				match(T__42);
				}
				break;
			case 3:
				_localctx = new IfExpContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(723);
				match(T__43);
				setState(724);
				((IfExpContext)_localctx).condition = expression(0);
				setState(725);
				match(T__44);
				setState(726);
				((IfExpContext)_localctx).thenExp = expression(0);
				setState(734);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__45) {
					{
					{
					setState(727);
					match(T__45);
					setState(728);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfCondition.add(((IfExpContext)_localctx).expression);
					setState(729);
					match(T__44);
					setState(730);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfExp.add(((IfExpContext)_localctx).expression);
					}
					}
					setState(736);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(737);
				match(T__46);
				setState(738);
				((IfExpContext)_localctx).elseExp = expression(0);
				setState(739);
				match(T__47);
				}
				break;
			case 4:
				_localctx = new LetExpContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(741);
				match(T__48);
				setState(742);
				letBinding();
				setState(747);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(743);
					match(T__4);
					setState(744);
					letBinding();
					}
					}
					setState(749);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(750);
				match(T__49);
				setState(751);
				expression(0);
				}
				break;
			case 5:
				_localctx = new LiteralExpContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(753);
				literalExpression();
				}
				break;
			case 6:
				_localctx = new OperationCallExpContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(754);
				pathName();
				setState(755);
				match(T__3);
				setState(757);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876228415470L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266287976441L) != 0)) {
					{
					setState(756);
					argumentList();
					}
				}

				setState(759);
				match(T__5);
				setState(761);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
				case 1:
					{
					setState(760);
					isMarkedPre();
					}
					break;
				}
				}
				break;
			case 7:
				_localctx = new PrimitiveTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(763);
				primitiveType();
				}
				break;
			case 8:
				_localctx = new CollectionTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(764);
				collectionType();
				}
				break;
			case 9:
				_localctx = new MapTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(765);
				mapType();
				}
				break;
			case 10:
				_localctx = new TupleTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(766);
				tupleType();
				}
				break;
			case 11:
				_localctx = new PathNameExpContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(767);
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
		public IsMarkedPreContext isMarkedPre() {
			return getRuleContext(IsMarkedPreContext.class,0);
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
			setState(793);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
			case 1:
				_localctx = new DotCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(773);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
				case 1:
					{
					setState(770);
					pathName();
					setState(771);
					match(COLONCOLON);
					}
					break;
				}
				setState(775);
				identifier();
				setState(776);
				match(T__3);
				setState(778);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876228415470L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266287976441L) != 0)) {
					{
					setState(777);
					argumentList();
					}
				}

				setState(780);
				match(T__5);
				setState(782);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
				case 1:
					{
					setState(781);
					isMarkedPre();
					}
					break;
				}
				}
				break;
			case 2:
				_localctx = new PropertySuffixContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(787);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
				case 1:
					{
					setState(784);
					pathName();
					setState(785);
					match(COLONCOLON);
					}
					break;
				}
				setState(789);
				identifier();
				setState(791);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
				case 1:
					{
					setState(790);
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
		public TypeExpressionContext iterType;
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
			setState(828);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
			case 1:
				_localctx = new IteratorCallContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(795);
				identifier();
				setState(796);
				match(T__3);
				setState(797);
				iteratorVariables();
				setState(798);
				match(T__50);
				setState(799);
				expression(0);
				setState(800);
				match(T__5);
				}
				break;
			case 2:
				_localctx = new IterateCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(802);
				identifier();
				setState(803);
				match(T__3);
				setState(804);
				identifier();
				setState(807);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(805);
					match(T__8);
					setState(806);
					((IterateCallContext)_localctx).iterType = typeExpression();
					}
				}

				setState(809);
				match(T__1);
				setState(810);
				identifier();
				setState(813);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(811);
					match(T__8);
					setState(812);
					((IterateCallContext)_localctx).accType = typeExpression();
					}
				}

				setState(815);
				match(T__9);
				setState(816);
				expression(0);
				setState(817);
				match(T__50);
				setState(818);
				expression(0);
				setState(819);
				match(T__5);
				}
				break;
			case 3:
				_localctx = new CollectionOperationCallContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(821);
				identifier();
				setState(822);
				match(T__3);
				setState(824);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876228415470L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266287976441L) != 0)) {
					{
					setState(823);
					argumentList();
					}
				}

				setState(826);
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
			setState(830);
			identifier();
			setState(833);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(831);
				match(T__8);
				setState(832);
				typeExpression();
				}
			}

			setState(843);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(835);
				match(T__4);
				setState(836);
				identifier();
				setState(839);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(837);
					match(T__8);
					setState(838);
					typeExpression();
					}
				}

				}
				}
				setState(845);
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
			setState(846);
			expression(0);
			setState(851);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(847);
				match(T__4);
				setState(848);
				expression(0);
				}
				}
				setState(853);
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
			setState(854);
			identifier();
			setState(857);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(855);
				match(T__8);
				setState(856);
				typeExpression();
				}
			}

			setState(859);
			match(T__9);
			setState(860);
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
			setState(862);
			match(T__51);
			setState(863);
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
			setState(876);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER_LITERAL:
				_localctx = new IntegerLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(865);
				match(INTEGER_LITERAL);
				}
				break;
			case REAL_LITERAL:
				_localctx = new RealLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(866);
				match(REAL_LITERAL);
				}
				break;
			case STRING_LITERAL:
				_localctx = new StringLitContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(867);
				stringLiteral_();
				}
				break;
			case T__52:
				_localctx = new TrueLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(868);
				match(T__52);
				}
				break;
			case T__53:
				_localctx = new FalseLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(869);
				match(T__53);
				}
				break;
			case T__54:
				_localctx = new NullLiteralContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(870);
				match(T__54);
				}
				break;
			case T__55:
				_localctx = new InvalidLiteralContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(871);
				match(T__55);
				}
				break;
			case T__0:
				_localctx = new UnlimitedNaturalLiteralContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(872);
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
				setState(873);
				collectionLiteral();
				}
				break;
			case T__62:
				_localctx = new TupleLitContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(874);
				tupleLiteral();
				}
				break;
			case T__63:
				_localctx = new MapLitContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(875);
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
			setState(879); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(878);
					match(STRING_LITERAL);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(881); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,101,_ctx);
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
			setState(883);
			collectionKind();
			setState(884);
			match(T__6);
			setState(893);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876228415470L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266287976441L) != 0)) {
				{
				setState(885);
				collectionLiteralPart();
				setState(890);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(886);
					match(T__4);
					setState(887);
					collectionLiteralPart();
					}
					}
					setState(892);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(895);
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
			setState(897);
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
			setState(899);
			expression(0);
			setState(902);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__61) {
				{
				setState(900);
				match(T__61);
				setState(901);
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
			setState(904);
			match(T__62);
			setState(905);
			match(T__6);
			setState(906);
			tupleLiteralPart();
			setState(911);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(907);
				match(T__4);
				setState(908);
				tupleLiteralPart();
				}
				}
				setState(913);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(914);
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
			setState(916);
			identifier();
			setState(919);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(917);
				match(T__8);
				setState(918);
				typeExpression();
				}
			}

			setState(921);
			match(T__9);
			setState(922);
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
			setState(924);
			match(T__63);
			setState(925);
			match(T__6);
			setState(934);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4620103876228415470L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 266287976441L) != 0)) {
				{
				setState(926);
				mapLiteralPart();
				setState(931);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(927);
					match(T__4);
					setState(928);
					mapLiteralPart();
					}
					}
					setState(933);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(936);
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
			setState(938);
			expression(0);
			setState(939);
			_la = _input.LA(1);
			if ( !(_la==T__64 || _la==T__65) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(940);
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
			setState(947);
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
				setState(942);
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
				setState(943);
				collectionType();
				}
				break;
			case T__63:
				enterOuterAlt(_localctx, 3);
				{
				setState(944);
				mapType();
				}
				break;
			case T__62:
				enterOuterAlt(_localctx, 4);
				{
				setState(945);
				tupleType();
				}
				break;
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 5);
				{
				setState(946);
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
			setState(949);
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
			setState(951);
			collectionKind();
			setState(952);
			match(T__3);
			setState(953);
			typeExpression();
			setState(954);
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
			setState(956);
			match(T__63);
			setState(957);
			match(T__3);
			setState(958);
			typeExpression();
			setState(959);
			match(T__4);
			setState(960);
			typeExpression();
			setState(961);
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
			setState(963);
			match(T__62);
			setState(964);
			match(T__3);
			setState(965);
			tupleTypePart();
			setState(970);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(966);
				match(T__4);
				setState(967);
				tupleTypePart();
				}
				}
				setState(972);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(973);
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
			setState(975);
			identifier();
			setState(976);
			match(T__8);
			setState(977);
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
			setState(979);
			identifier();
			setState(984);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,111,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(980);
					match(COLONCOLON);
					setState(981);
					identifier();
					}
					} 
				}
				setState(986);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,111,_ctx);
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
		enterRule(_localctx, 134, RULE_identifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(987);
			_la = _input.LA(1);
			if ( !(_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) ) {
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
		"\u0004\u0001h\u03de\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"A\u0007A\u0002B\u0007B\u0002C\u0007C\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0005\u0001\u008d\b\u0001\n\u0001\f\u0001\u0090\t\u0001\u0001"+
		"\u0001\u0005\u0001\u0093\b\u0001\n\u0001\f\u0001\u0096\t\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u009c\b\u0002\n\u0002"+
		"\f\u0002\u009f\t\u0002\u0001\u0002\u0001\u0002\u0003\u0002\u00a3\b\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003"+
		"\u00aa\b\u0003\n\u0003\f\u0003\u00ad\t\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u00b5\b\u0004\n"+
		"\u0004\f\u0004\u00b8\t\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003"+
		"\u0004\u00bd\b\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u00c1\b\u0004"+
		"\n\u0004\f\u0004\u00c4\t\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u00c8"+
		"\b\u0004\n\u0004\f\u0004\u00cb\t\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0005\u0005\u00d6\b\u0005\n\u0005\f\u0005\u00d9\t\u0005\u0001\u0005\u0001"+
		"\u0005\u0003\u0005\u00dd\b\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u00e7"+
		"\b\u0007\n\u0007\f\u0007\u00ea\t\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003"+
		"\b\u00f7\b\b\u0001\t\u0003\t\u00fa\b\t\u0001\t\u0003\t\u00fd\b\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0003\t\u0103\b\t\u0001\t\u0001\t\u0005\t\u0107"+
		"\b\t\n\t\f\t\u010a\t\t\u0001\t\u0001\t\u0004\t\u010e\b\t\u000b\t\f\t\u010f"+
		"\u0001\t\u0003\t\u0113\b\t\u0001\t\u0003\t\u0116\b\t\u0001\t\u0001\t\u0001"+
		"\n\u0001\n\u0001\n\u0005\n\u011d\b\n\n\n\f\n\u0120\t\n\u0001\n\u0001\n"+
		"\u0001\n\u0001\n\u0003\n\u0126\b\n\u0001\n\u0001\n\u0001\u000b\u0003\u000b"+
		"\u012b\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0005\u000b\u0132\b\u000b\n\u000b\f\u000b\u0135\t\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u013b\b\u000b\u0001\u000b\u0001"+
		"\u000b\u0003\u000b\u013f\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0004"+
		"\u000b\u0144\b\u000b\u000b\u000b\f\u000b\u0145\u0001\u000b\u0001\u000b"+
		"\u0003\u000b\u014a\b\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0003\u000f"+
		"\u015e\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f"+
		"\u0164\b\u000f\u0001\u0010\u0003\u0010\u0167\b\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0003\u0010\u016c\b\u0010\u0001\u0010\u0001\u0010\u0003"+
		"\u0010\u0170\b\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u017b"+
		"\b\u0012\n\u0012\f\u0012\u017e\t\u0012\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013"+
		"\u019a\b\u0013\u0001\u0014\u0003\u0014\u019d\b\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003"+
		"\u0014\u01a6\b\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0005\u0015\u01ad\b\u0015\n\u0015\f\u0015\u01b0\t\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0003\u0015\u01b5\b\u0015\u0003\u0015\u01b7\b"+
		"\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u01bc\b\u0016\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0005\u0017\u01c3"+
		"\b\u0017\n\u0017\f\u0017\u01c6\t\u0017\u0001\u0017\u0001\u0017\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0005\u0018\u01cf\b\u0018"+
		"\n\u0018\f\u0018\u01d2\t\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0005\u0019\u01dc"+
		"\b\u0019\n\u0019\f\u0019\u01df\t\u0019\u0003\u0019\u01e1\b\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0003\u0019\u01eb\b\u0019\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001d\u0005\u001d\u01f8\b\u001d\n\u001d\f\u001d"+
		"\u01fb\t\u001d\u0001\u001d\u0001\u001d\u0005\u001d\u01ff\b\u001d\n\u001d"+
		"\f\u001d\u0202\t\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0003\u001e\u0208\b\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e"+
		"\u020d\b\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0005\u001f\u0212\b"+
		"\u001f\n\u001f\f\u001f\u0215\t\u001f\u0001\u001f\u0001\u001f\u0001 \u0001"+
		" \u0001 \u0003 \u021c\b \u0001!\u0001!\u0001!\u0001!\u0003!\u0222\b!\u0001"+
		"!\u0001!\u0004!\u0226\b!\u000b!\f!\u0227\u0001\"\u0001\"\u0003\"\u022c"+
		"\b\"\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0003#\u0234\b#\u0003#"+
		"\u0236\b#\u0001#\u0001#\u0001#\u0001$\u0003$\u023c\b$\u0001$\u0001$\u0003"+
		"$\u0240\b$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001"+
		"$\u0001$\u0003$\u024c\b$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0003"+
		"$\u0254\b$\u0001%\u0001%\u0001%\u0001%\u0003%\u025a\b%\u0001%\u0001%\u0001"+
		"%\u0003%\u025f\b%\u0001%\u0001%\u0001%\u0003%\u0264\b%\u0001%\u0004%\u0267"+
		"\b%\u000b%\f%\u0268\u0001&\u0001&\u0003&\u026d\b&\u0001&\u0001&\u0001"+
		"&\u0001&\u0003&\u0273\b&\u0001&\u0001&\u0001&\u0001&\u0003&\u0279\b&\u0001"+
		"&\u0001&\u0003&\u027d\b&\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0004\'\u0286\b\'\u000b\'\f\'\u0287\u0001(\u0001(\u0003(\u028c"+
		"\b(\u0001(\u0001(\u0001(\u0001(\u0003(\u0292\b(\u0001(\u0001(\u0003(\u0296"+
		"\b(\u0001)\u0001)\u0001)\u0005)\u029b\b)\n)\f)\u029e\t)\u0001*\u0001*"+
		"\u0001*\u0001*\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0003+\u02aa"+
		"\b+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001"+
		"+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001"+
		"+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001"+
		"+\u0005+\u02ca\b+\n+\f+\u02cd\t+\u0001,\u0001,\u0001,\u0001,\u0001,\u0001"+
		",\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0005,\u02dd"+
		"\b,\n,\f,\u02e0\t,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001"+
		",\u0005,\u02ea\b,\n,\f,\u02ed\t,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001"+
		",\u0001,\u0003,\u02f6\b,\u0001,\u0001,\u0003,\u02fa\b,\u0001,\u0001,\u0001"+
		",\u0001,\u0001,\u0003,\u0301\b,\u0001-\u0001-\u0001-\u0003-\u0306\b-\u0001"+
		"-\u0001-\u0001-\u0003-\u030b\b-\u0001-\u0001-\u0003-\u030f\b-\u0001-\u0001"+
		"-\u0001-\u0003-\u0314\b-\u0001-\u0001-\u0003-\u0318\b-\u0003-\u031a\b"+
		"-\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001"+
		".\u0001.\u0001.\u0003.\u0328\b.\u0001.\u0001.\u0001.\u0001.\u0003.\u032e"+
		"\b.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0003"+
		".\u0339\b.\u0001.\u0001.\u0003.\u033d\b.\u0001/\u0001/\u0001/\u0003/\u0342"+
		"\b/\u0001/\u0001/\u0001/\u0001/\u0003/\u0348\b/\u0005/\u034a\b/\n/\f/"+
		"\u034d\t/\u00010\u00010\u00010\u00050\u0352\b0\n0\f0\u0355\t0\u00011\u0001"+
		"1\u00011\u00031\u035a\b1\u00011\u00011\u00011\u00012\u00012\u00012\u0001"+
		"3\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u00013\u0001"+
		"3\u00033\u036d\b3\u00014\u00044\u0370\b4\u000b4\f4\u0371\u00015\u0001"+
		"5\u00015\u00015\u00015\u00055\u0379\b5\n5\f5\u037c\t5\u00035\u037e\b5"+
		"\u00015\u00015\u00016\u00016\u00017\u00017\u00017\u00037\u0387\b7\u0001"+
		"8\u00018\u00018\u00018\u00018\u00058\u038e\b8\n8\f8\u0391\t8\u00018\u0001"+
		"8\u00019\u00019\u00019\u00039\u0398\b9\u00019\u00019\u00019\u0001:\u0001"+
		":\u0001:\u0001:\u0001:\u0005:\u03a2\b:\n:\f:\u03a5\t:\u0003:\u03a7\b:"+
		"\u0001:\u0001:\u0001;\u0001;\u0001;\u0001;\u0001<\u0001<\u0001<\u0001"+
		"<\u0001<\u0003<\u03b4\b<\u0001=\u0001=\u0001>\u0001>\u0001>\u0001>\u0001"+
		">\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001@\u0001@\u0001"+
		"@\u0001@\u0001@\u0005@\u03c9\b@\n@\f@\u03cc\t@\u0001@\u0001@\u0001A\u0001"+
		"A\u0001A\u0001A\u0001B\u0001B\u0001B\u0005B\u03d7\bB\nB\fB\u03da\tB\u0001"+
		"C\u0001C\u0001C\u0000\u0001VD\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPR"+
		"TVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0000\f\u0002\u0000LL"+
		"NN\u0002\u0000\u000e\u000fQQ\u0002\u0000\u0001\u0001  \u0002\u0000\u001f"+
		"\u001f!!\u0001\u0000\"%\u0002\u0000\n\n&&\u0002\u0000\u0003\u0003\u001b"+
		"\u001b\u0001\u0000\u001c\u001d\u0001\u00009=\u0001\u0000AB\u0001\u0000"+
		"CK\u0001\u0000de\u042d\u0000\u0088\u0001\u0000\u0000\u0000\u0002\u008e"+
		"\u0001\u0000\u0000\u0000\u0004\u0097\u0001\u0000\u0000\u0000\u0006\u00a6"+
		"\u0001\u0000\u0000\u0000\b\u00ae\u0001\u0000\u0000\u0000\n\u00ce\u0001"+
		"\u0000\u0000\u0000\f\u00de\u0001\u0000\u0000\u0000\u000e\u00e0\u0001\u0000"+
		"\u0000\u0000\u0010\u00f6\u0001\u0000\u0000\u0000\u0012\u00f9\u0001\u0000"+
		"\u0000\u0000\u0014\u0119\u0001\u0000\u0000\u0000\u0016\u012a\u0001\u0000"+
		"\u0000\u0000\u0018\u014d\u0001\u0000\u0000\u0000\u001a\u0154\u0001\u0000"+
		"\u0000\u0000\u001c\u0156\u0001\u0000\u0000\u0000\u001e\u015d\u0001\u0000"+
		"\u0000\u0000 \u0166\u0001\u0000\u0000\u0000\"\u0173\u0001\u0000\u0000"+
		"\u0000$\u0177\u0001\u0000\u0000\u0000&\u0199\u0001\u0000\u0000\u0000("+
		"\u019c\u0001\u0000\u0000\u0000*\u01a9\u0001\u0000\u0000\u0000,\u01bb\u0001"+
		"\u0000\u0000\u0000.\u01bd\u0001\u0000\u0000\u00000\u01c9\u0001\u0000\u0000"+
		"\u00002\u01d5\u0001\u0000\u0000\u00004\u01ec\u0001\u0000\u0000\u00006"+
		"\u01f0\u0001\u0000\u0000\u00008\u01f3\u0001\u0000\u0000\u0000:\u01f9\u0001"+
		"\u0000\u0000\u0000<\u0203\u0001\u0000\u0000\u0000>\u020e\u0001\u0000\u0000"+
		"\u0000@\u021b\u0001\u0000\u0000\u0000B\u021d\u0001\u0000\u0000\u0000D"+
		"\u022b\u0001\u0000\u0000\u0000F\u022d\u0001\u0000\u0000\u0000H\u023b\u0001"+
		"\u0000\u0000\u0000J\u0255\u0001\u0000\u0000\u0000L\u027c\u0001\u0000\u0000"+
		"\u0000N\u027e\u0001\u0000\u0000\u0000P\u0295\u0001\u0000\u0000\u0000R"+
		"\u0297\u0001\u0000\u0000\u0000T\u029f\u0001\u0000\u0000\u0000V\u02a9\u0001"+
		"\u0000\u0000\u0000X\u0300\u0001\u0000\u0000\u0000Z\u0319\u0001\u0000\u0000"+
		"\u0000\\\u033c\u0001\u0000\u0000\u0000^\u033e\u0001\u0000\u0000\u0000"+
		"`\u034e\u0001\u0000\u0000\u0000b\u0356\u0001\u0000\u0000\u0000d\u035e"+
		"\u0001\u0000\u0000\u0000f\u036c\u0001\u0000\u0000\u0000h\u036f\u0001\u0000"+
		"\u0000\u0000j\u0373\u0001\u0000\u0000\u0000l\u0381\u0001\u0000\u0000\u0000"+
		"n\u0383\u0001\u0000\u0000\u0000p\u0388\u0001\u0000\u0000\u0000r\u0394"+
		"\u0001\u0000\u0000\u0000t\u039c\u0001\u0000\u0000\u0000v\u03aa\u0001\u0000"+
		"\u0000\u0000x\u03b3\u0001\u0000\u0000\u0000z\u03b5\u0001\u0000\u0000\u0000"+
		"|\u03b7\u0001\u0000\u0000\u0000~\u03bc\u0001\u0000\u0000\u0000\u0080\u03c3"+
		"\u0001\u0000\u0000\u0000\u0082\u03cf\u0001\u0000\u0000\u0000\u0084\u03d3"+
		"\u0001\u0000\u0000\u0000\u0086\u03db\u0001\u0000\u0000\u0000\u0088\u0089"+
		"\u0003\u0002\u0001\u0000\u0089\u008a\u0005\u0000\u0000\u0001\u008a\u0001"+
		"\u0001\u0000\u0000\u0000\u008b\u008d\u0003\u0004\u0002\u0000\u008c\u008b"+
		"\u0001\u0000\u0000\u0000\u008d\u0090\u0001\u0000\u0000\u0000\u008e\u008c"+
		"\u0001\u0000\u0000\u0000\u008e\u008f\u0001\u0000\u0000\u0000\u008f\u0094"+
		"\u0001\u0000\u0000\u0000\u0090\u008e\u0001\u0000\u0000\u0000\u0091\u0093"+
		"\u0003\b\u0004\u0000\u0092\u0091\u0001\u0000\u0000\u0000\u0093\u0096\u0001"+
		"\u0000\u0000\u0000\u0094\u0092\u0001\u0000\u0000\u0000\u0094\u0095\u0001"+
		"\u0000\u0000\u0000\u0095\u0003\u0001\u0000\u0000\u0000\u0096\u0094\u0001"+
		"\u0000\u0000\u0000\u0097\u0098\u0005Q\u0000\u0000\u0098\u009d\u0003\u0006"+
		"\u0003\u0000\u0099\u009a\u0005_\u0000\u0000\u009a\u009c\u0003\u0006\u0003"+
		"\u0000\u009b\u0099\u0001\u0000\u0000\u0000\u009c\u009f\u0001\u0000\u0000"+
		"\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000"+
		"\u0000\u009e\u00a2\u0001\u0000\u0000\u0000\u009f\u009d\u0001\u0000\u0000"+
		"\u0000\u00a0\u00a1\u0005_\u0000\u0000\u00a1\u00a3\u0005\u0001\u0000\u0000"+
		"\u00a2\u00a0\u0001\u0000\u0000\u0000\u00a2\u00a3\u0001\u0000\u0000\u0000"+
		"\u00a3\u00a4\u0001\u0000\u0000\u0000\u00a4\u00a5\u0005\u0002\u0000\u0000"+
		"\u00a5\u0005\u0001\u0000\u0000\u0000\u00a6\u00ab\u0003\u0086C\u0000\u00a7"+
		"\u00a8\u0005\u0003\u0000\u0000\u00a8\u00aa\u0003\u0086C\u0000\u00a9\u00a7"+
		"\u0001\u0000\u0000\u0000\u00aa\u00ad\u0001\u0000\u0000\u0000\u00ab\u00a9"+
		"\u0001\u0000\u0000\u0000\u00ab\u00ac\u0001\u0000\u0000\u0000\u00ac\u0007"+
		"\u0001\u0000\u0000\u0000\u00ad\u00ab\u0001\u0000\u0000\u0000\u00ae\u00af"+
		"\u0005X\u0000\u0000\u00af\u00b0\u0003\u0086C\u0000\u00b0\u00b1\u0005\u0004"+
		"\u0000\u0000\u00b1\u00b6\u0003\n\u0005\u0000\u00b2\u00b3\u0005\u0005\u0000"+
		"\u0000\u00b3\u00b5\u0003\n\u0005\u0000\u00b4\u00b2\u0001\u0000\u0000\u0000"+
		"\u00b5\u00b8\u0001\u0000\u0000\u0000\u00b6\u00b4\u0001\u0000\u0000\u0000"+
		"\u00b6\u00b7\u0001\u0000\u0000\u0000\u00b7\u00b9\u0001\u0000\u0000\u0000"+
		"\u00b8\u00b6\u0001\u0000\u0000\u0000\u00b9\u00bc\u0005\u0006\u0000\u0000"+
		"\u00ba\u00bb\u0005O\u0000\u0000\u00bb\u00bd\u0003\u0086C\u0000\u00bc\u00ba"+
		"\u0001\u0000\u0000\u0000\u00bc\u00bd\u0001\u0000\u0000\u0000\u00bd\u00be"+
		"\u0001\u0000\u0000\u0000\u00be\u00c2\u0005\u0007\u0000\u0000\u00bf\u00c1"+
		"\u0003\u000e\u0007\u0000\u00c0\u00bf\u0001\u0000\u0000\u0000\u00c1\u00c4"+
		"\u0001\u0000\u0000\u0000\u00c2\u00c0\u0001\u0000\u0000\u0000\u00c2\u00c3"+
		"\u0001\u0000\u0000\u0000\u00c3\u00c9\u0001\u0000\u0000\u0000\u00c4\u00c2"+
		"\u0001\u0000\u0000\u0000\u00c5\u00c8\u0003\u0012\t\u0000\u00c6\u00c8\u0003"+
		"2\u0019\u0000\u00c7\u00c5\u0001\u0000\u0000\u0000\u00c7\u00c6\u0001\u0000"+
		"\u0000\u0000\u00c8\u00cb\u0001\u0000\u0000\u0000\u00c9\u00c7\u0001\u0000"+
		"\u0000\u0000\u00c9\u00ca\u0001\u0000\u0000\u0000\u00ca\u00cc\u0001\u0000"+
		"\u0000\u0000\u00cb\u00c9\u0001\u0000\u0000\u0000\u00cc\u00cd\u0005\b\u0000"+
		"\u0000\u00cd\t\u0001\u0000\u0000\u0000\u00ce\u00cf\u0003\u0086C\u0000"+
		"\u00cf\u00dc\u0005\t\u0000\u0000\u00d0\u00dd\u0003\f\u0006\u0000\u00d1"+
		"\u00d2\u0005\u0007\u0000\u0000\u00d2\u00d7\u0003\f\u0006\u0000\u00d3\u00d4"+
		"\u0005\u0005\u0000\u0000\u00d4\u00d6\u0003\f\u0006\u0000\u00d5\u00d3\u0001"+
		"\u0000\u0000\u0000\u00d6\u00d9\u0001\u0000\u0000\u0000\u00d7\u00d5\u0001"+
		"\u0000\u0000\u0000\u00d7\u00d8\u0001\u0000\u0000\u0000\u00d8\u00da\u0001"+
		"\u0000\u0000\u0000\u00d9\u00d7\u0001\u0000\u0000\u0000\u00da\u00db\u0005"+
		"\b\u0000\u0000\u00db\u00dd\u0001\u0000\u0000\u0000\u00dc\u00d0\u0001\u0000"+
		"\u0000\u0000\u00dc\u00d1\u0001\u0000\u0000\u0000\u00dd\u000b\u0001\u0000"+
		"\u0000\u0000\u00de\u00df\u0003\u0084B\u0000\u00df\r\u0001\u0000\u0000"+
		"\u0000\u00e0\u00e1\u0005R\u0000\u0000\u00e1\u00e2\u0003\u0084B\u0000\u00e2"+
		"\u00e3\u0005\u0007\u0000\u0000\u00e3\u00e8\u0003\u0010\b\u0000\u00e4\u00e5"+
		"\u0005\u0005\u0000\u0000\u00e5\u00e7\u0003\u0010\b\u0000\u00e6\u00e4\u0001"+
		"\u0000\u0000\u0000\u00e7\u00ea\u0001\u0000\u0000\u0000\u00e8\u00e6\u0001"+
		"\u0000\u0000\u0000\u00e8\u00e9\u0001\u0000\u0000\u0000\u00e9\u00eb\u0001"+
		"\u0000\u0000\u0000\u00ea\u00e8\u0001\u0000\u0000\u0000\u00eb\u00ec\u0005"+
		"\b\u0000\u0000\u00ec\u00ed\u0005\u0002\u0000\u0000\u00ed\u000f\u0001\u0000"+
		"\u0000\u0000\u00ee\u00f7\u0003\u0086C\u0000\u00ef\u00f0\u0005]\u0000\u0000"+
		"\u00f0\u00f1\u0005\u0004\u0000\u0000\u00f1\u00f2\u0003\u0084B\u0000\u00f2"+
		"\u00f3\u0005_\u0000\u0000\u00f3\u00f4\u0003\u0086C\u0000\u00f4\u00f5\u0005"+
		"\u0006\u0000\u0000\u00f5\u00f7\u0001\u0000\u0000\u0000\u00f6\u00ee\u0001"+
		"\u0000\u0000\u0000\u00f6\u00ef\u0001\u0000\u0000\u0000\u00f7\u0011\u0001"+
		"\u0000\u0000\u0000\u00f8\u00fa\u0005W\u0000\u0000\u00f9\u00f8\u0001\u0000"+
		"\u0000\u0000\u00f9\u00fa\u0001\u0000\u0000\u0000\u00fa\u00fc\u0001\u0000"+
		"\u0000\u0000\u00fb\u00fd\u0005\\\u0000\u0000\u00fc\u00fb\u0001\u0000\u0000"+
		"\u0000\u00fc\u00fd\u0001\u0000\u0000\u0000\u00fd\u00fe\u0001\u0000\u0000"+
		"\u0000\u00fe\u00ff\u0005V\u0000\u0000\u00ff\u0102\u0003\u0086C\u0000\u0100"+
		"\u0101\u0005S\u0000\u0000\u0101\u0103\u0003\u0086C\u0000\u0102\u0100\u0001"+
		"\u0000\u0000\u0000\u0102\u0103\u0001\u0000\u0000\u0000\u0103\u0104\u0001"+
		"\u0000\u0000\u0000\u0104\u0108\u0005\u0007\u0000\u0000\u0105\u0107\u0003"+
		"\u0014\n\u0000\u0106\u0105\u0001\u0000\u0000\u0000\u0107\u010a\u0001\u0000"+
		"\u0000\u0000\u0108\u0106\u0001\u0000\u0000\u0000\u0108\u0109\u0001\u0000"+
		"\u0000\u0000\u0109\u010d\u0001\u0000\u0000\u0000\u010a\u0108\u0001\u0000"+
		"\u0000\u0000\u010b\u010e\u0003\u0016\u000b\u0000\u010c\u010e\u0003\u0018"+
		"\f\u0000\u010d\u010b\u0001\u0000\u0000\u0000\u010d\u010c\u0001\u0000\u0000"+
		"\u0000\u010e\u010f\u0001\u0000\u0000\u0000\u010f\u010d\u0001\u0000\u0000"+
		"\u0000\u010f\u0110\u0001\u0000\u0000\u0000\u0110\u0112\u0001\u0000\u0000"+
		"\u0000\u0111\u0113\u0003.\u0017\u0000\u0112\u0111\u0001\u0000\u0000\u0000"+
		"\u0112\u0113\u0001\u0000\u0000\u0000\u0113\u0115\u0001\u0000\u0000\u0000"+
		"\u0114\u0116\u00030\u0018\u0000\u0115\u0114\u0001\u0000\u0000\u0000\u0115"+
		"\u0116\u0001\u0000\u0000\u0000\u0116\u0117\u0001\u0000\u0000\u0000\u0117"+
		"\u0118\u0005\b\u0000\u0000\u0118\u0013\u0001\u0000\u0000\u0000\u0119\u011e"+
		"\u0003\u0086C\u0000\u011a\u011b\u0005\u0005\u0000\u0000\u011b\u011d\u0003"+
		"\u0086C\u0000\u011c\u011a\u0001\u0000\u0000\u0000\u011d\u0120\u0001\u0000"+
		"\u0000\u0000\u011e\u011c\u0001\u0000\u0000\u0000\u011e\u011f\u0001\u0000"+
		"\u0000\u0000\u011f\u0121\u0001\u0000\u0000\u0000\u0120\u011e\u0001\u0000"+
		"\u0000\u0000\u0121\u0122\u0005\t\u0000\u0000\u0122\u0125\u0003x<\u0000"+
		"\u0123\u0124\u0005\n\u0000\u0000\u0124\u0126\u0003V+\u0000\u0125\u0123"+
		"\u0001\u0000\u0000\u0000\u0125\u0126\u0001\u0000\u0000\u0000\u0126\u0127"+
		"\u0001\u0000\u0000\u0000\u0127\u0128\u0005\u0002\u0000\u0000\u0128\u0015"+
		"\u0001\u0000\u0000\u0000\u0129\u012b\u0003\u001a\r\u0000\u012a\u0129\u0001"+
		"\u0000\u0000\u0000\u012a\u012b\u0001\u0000\u0000\u0000\u012b\u012c\u0001"+
		"\u0000\u0000\u0000\u012c\u012d\u0005M\u0000\u0000\u012d\u012e\u0003\u0086"+
		"C\u0000\u012e\u0133\u0003\u001e\u000f\u0000\u012f\u0130\u0005\u0005\u0000"+
		"\u0000\u0130\u0132\u0003\u001e\u000f\u0000\u0131\u012f\u0001\u0000\u0000"+
		"\u0000\u0132\u0135\u0001\u0000\u0000\u0000\u0133\u0131\u0001\u0000\u0000"+
		"\u0000\u0133\u0134\u0001\u0000\u0000\u0000\u0134\u013e\u0001\u0000\u0000"+
		"\u0000\u0135\u0133\u0001\u0000\u0000\u0000\u0136\u0137\u0005P\u0000\u0000"+
		"\u0137\u0138\u0003\u0084B\u0000\u0138\u013a\u0005\u0004\u0000\u0000\u0139"+
		"\u013b\u0003`0\u0000\u013a\u0139\u0001\u0000\u0000\u0000\u013a\u013b\u0001"+
		"\u0000\u0000\u0000\u013b\u013c\u0001\u0000\u0000\u0000\u013c\u013d\u0005"+
		"\u0006\u0000\u0000\u013d\u013f\u0001\u0000\u0000\u0000\u013e\u0136\u0001"+
		"\u0000\u0000\u0000\u013e\u013f\u0001\u0000\u0000\u0000\u013f\u0149\u0001"+
		"\u0000\u0000\u0000\u0140\u0141\u0005[\u0000\u0000\u0141\u0143\u0005\u0007"+
		"\u0000\u0000\u0142\u0144\u0003\u001c\u000e\u0000\u0143\u0142\u0001\u0000"+
		"\u0000\u0000\u0144\u0145\u0001\u0000\u0000\u0000\u0145\u0143\u0001\u0000"+
		"\u0000\u0000\u0145\u0146\u0001\u0000\u0000\u0000\u0146\u0147\u0001\u0000"+
		"\u0000\u0000\u0147\u0148\u0005\b\u0000\u0000\u0148\u014a\u0001\u0000\u0000"+
		"\u0000\u0149\u0140\u0001\u0000\u0000\u0000\u0149\u014a\u0001\u0000\u0000"+
		"\u0000\u014a\u014b\u0001\u0000\u0000\u0000\u014b\u014c\u0005\u0002\u0000"+
		"\u0000\u014c\u0017\u0001\u0000\u0000\u0000\u014d\u014e\u0005T\u0000\u0000"+
		"\u014e\u014f\u0005M\u0000\u0000\u014f\u0150\u0003\u0086C\u0000\u0150\u0151"+
		"\u0005\t\u0000\u0000\u0151\u0152\u0003x<\u0000\u0152\u0153\u0005\u0002"+
		"\u0000\u0000\u0153\u0019\u0001\u0000\u0000\u0000\u0154\u0155\u0007\u0000"+
		"\u0000\u0000\u0155\u001b\u0001\u0000\u0000\u0000\u0156\u0157\u0003\u0086"+
		"C\u0000\u0157\u0158\u0005\n\u0000\u0000\u0158\u0159\u0003V+\u0000\u0159"+
		"\u015a\u0005\u0002\u0000\u0000\u015a\u001d\u0001\u0000\u0000\u0000\u015b"+
		"\u015e\u0003 \u0010\u0000\u015c\u015e\u0003(\u0014\u0000\u015d\u015b\u0001"+
		"\u0000\u0000\u0000\u015d\u015c\u0001\u0000\u0000\u0000\u015e\u0163\u0001"+
		"\u0000\u0000\u0000\u015f\u0160\u0005\u0007\u0000\u0000\u0160\u0161\u0003"+
		"V+\u0000\u0161\u0162\u0005\b\u0000\u0000\u0162\u0164\u0001\u0000\u0000"+
		"\u0000\u0163\u015f\u0001\u0000\u0000\u0000\u0163\u0164\u0001\u0000\u0000"+
		"\u0000\u0164\u001f\u0001\u0000\u0000\u0000\u0165\u0167\u0003\u0086C\u0000"+
		"\u0166\u0165\u0001\u0000\u0000\u0000\u0166\u0167\u0001\u0000\u0000\u0000"+
		"\u0167\u0168\u0001\u0000\u0000\u0000\u0168\u0169\u0005\t\u0000\u0000\u0169"+
		"\u016b\u0003\u0084B\u0000\u016a\u016c\u0003\"\u0011\u0000\u016b\u016a"+
		"\u0001\u0000\u0000\u0000\u016b\u016c\u0001\u0000\u0000\u0000\u016c\u016d"+
		"\u0001\u0000\u0000\u0000\u016d\u016f\u0005\u0007\u0000\u0000\u016e\u0170"+
		"\u0003$\u0012\u0000\u016f\u016e\u0001\u0000\u0000\u0000\u016f\u0170\u0001"+
		"\u0000\u0000\u0000\u0170\u0171\u0001\u0000\u0000\u0000\u0171\u0172\u0005"+
		"\b\u0000\u0000\u0172!\u0001\u0000\u0000\u0000\u0173\u0174\u0005\u000b"+
		"\u0000\u0000\u0174\u0175\u0005\f\u0000\u0000\u0175\u0176\u0005\r\u0000"+
		"\u0000\u0176#\u0001\u0000\u0000\u0000\u0177\u017c\u0003&\u0013\u0000\u0178"+
		"\u0179\u0005\u0005\u0000\u0000\u0179\u017b\u0003&\u0013\u0000\u017a\u0178"+
		"\u0001\u0000\u0000\u0000\u017b\u017e\u0001\u0000\u0000\u0000\u017c\u017a"+
		"\u0001\u0000\u0000\u0000\u017c\u017d\u0001\u0000\u0000\u0000\u017d%\u0001"+
		"\u0000\u0000\u0000\u017e\u017c\u0001\u0000\u0000\u0000\u017f\u0180\u0003"+
		"\u0086C\u0000\u0180\u0181\u0005\n\u0000\u0000\u0181\u0182\u0003\u001e"+
		"\u000f\u0000\u0182\u019a\u0001\u0000\u0000\u0000\u0183\u0184\u0003\u0086"+
		"C\u0000\u0184\u0185\u0005\n\u0000\u0000\u0185\u0186\u0003V+\u0000\u0186"+
		"\u019a\u0001\u0000\u0000\u0000\u0187\u0188\u0005]\u0000\u0000\u0188\u0189"+
		"\u0005\u0004\u0000\u0000\u0189\u018a\u0003\u0084B\u0000\u018a\u018b\u0005"+
		"_\u0000\u0000\u018b\u018c\u0003\u0086C\u0000\u018c\u018d\u0005\u0006\u0000"+
		"\u0000\u018d\u018e\u0005\n\u0000\u0000\u018e\u018f\u0003\u001e\u000f\u0000"+
		"\u018f\u019a\u0001\u0000\u0000\u0000\u0190\u0191\u0005]\u0000\u0000\u0191"+
		"\u0192\u0005\u0004\u0000\u0000\u0192\u0193\u0003\u0084B\u0000\u0193\u0194"+
		"\u0005_\u0000\u0000\u0194\u0195\u0003\u0086C\u0000\u0195\u0196\u0005\u0006"+
		"\u0000\u0000\u0196\u0197\u0005\n\u0000\u0000\u0197\u0198\u0003V+\u0000"+
		"\u0198\u019a\u0001\u0000\u0000\u0000\u0199\u017f\u0001\u0000\u0000\u0000"+
		"\u0199\u0183\u0001\u0000\u0000\u0000\u0199\u0187\u0001\u0000\u0000\u0000"+
		"\u0199\u0190\u0001\u0000\u0000\u0000\u019a\'\u0001\u0000\u0000\u0000\u019b"+
		"\u019d\u0003\u0086C\u0000\u019c\u019b\u0001\u0000\u0000\u0000\u019c\u019d"+
		"\u0001\u0000\u0000\u0000\u019d\u019e\u0001\u0000\u0000\u0000\u019e\u019f"+
		"\u0005\t\u0000\u0000\u019f\u01a0\u0003l6\u0000\u01a0\u01a1\u0005\u0004"+
		"\u0000\u0000\u01a1\u01a2\u0003x<\u0000\u01a2\u01a3\u0005\u0006\u0000\u0000"+
		"\u01a3\u01a5\u0005\u0007\u0000\u0000\u01a4\u01a6\u0003*\u0015\u0000\u01a5"+
		"\u01a4\u0001\u0000\u0000\u0000\u01a5\u01a6\u0001\u0000\u0000\u0000\u01a6"+
		"\u01a7\u0001\u0000\u0000\u0000\u01a7\u01a8\u0005\b\u0000\u0000\u01a8)"+
		"\u0001\u0000\u0000\u0000\u01a9\u01ae\u0003,\u0016\u0000\u01aa\u01ab\u0005"+
		"\u0005\u0000\u0000\u01ab\u01ad\u0003,\u0016\u0000\u01ac\u01aa\u0001\u0000"+
		"\u0000\u0000\u01ad\u01b0\u0001\u0000\u0000\u0000\u01ae\u01ac\u0001\u0000"+
		"\u0000\u0000\u01ae\u01af\u0001\u0000\u0000\u0000\u01af\u01b6\u0001\u0000"+
		"\u0000\u0000\u01b0\u01ae\u0001\u0000\u0000\u0000\u01b1\u01b4\u0005^\u0000"+
		"\u0000\u01b2\u01b5\u0003\u0086C\u0000\u01b3\u01b5\u0005`\u0000\u0000\u01b4"+
		"\u01b2\u0001\u0000\u0000\u0000\u01b4\u01b3\u0001\u0000\u0000\u0000\u01b5"+
		"\u01b7\u0001\u0000\u0000\u0000\u01b6\u01b1\u0001\u0000\u0000\u0000\u01b6"+
		"\u01b7\u0001\u0000\u0000\u0000\u01b7+\u0001\u0000\u0000\u0000\u01b8\u01bc"+
		"\u0003\u0086C\u0000\u01b9\u01bc\u0003\u001e\u000f\u0000\u01ba\u01bc\u0005"+
		"`\u0000\u0000\u01bb\u01b8\u0001\u0000\u0000\u0000\u01bb\u01b9\u0001\u0000"+
		"\u0000\u0000\u01bb\u01ba\u0001\u0000\u0000\u0000\u01bc-\u0001\u0000\u0000"+
		"\u0000\u01bd\u01be\u0005Y\u0000\u0000\u01be\u01c4\u0005\u0007\u0000\u0000"+
		"\u01bf\u01c0\u0003V+\u0000\u01c0\u01c1\u0005\u0002\u0000\u0000\u01c1\u01c3"+
		"\u0001\u0000\u0000\u0000\u01c2\u01bf\u0001\u0000\u0000\u0000\u01c3\u01c6"+
		"\u0001\u0000\u0000\u0000\u01c4\u01c2\u0001\u0000\u0000\u0000\u01c4\u01c5"+
		"\u0001\u0000\u0000\u0000\u01c5\u01c7\u0001\u0000\u0000\u0000\u01c6\u01c4"+
		"\u0001\u0000\u0000\u0000\u01c7\u01c8\u0005\b\u0000\u0000\u01c8/\u0001"+
		"\u0000\u0000\u0000\u01c9\u01ca\u0005Z\u0000\u0000\u01ca\u01d0\u0005\u0007"+
		"\u0000\u0000\u01cb\u01cc\u0003V+\u0000\u01cc\u01cd\u0005\u0002\u0000\u0000"+
		"\u01cd\u01cf\u0001\u0000\u0000\u0000\u01ce\u01cb\u0001\u0000\u0000\u0000"+
		"\u01cf\u01d2\u0001\u0000\u0000\u0000\u01d0\u01ce\u0001\u0000\u0000\u0000"+
		"\u01d0\u01d1\u0001\u0000\u0000\u0000\u01d1\u01d3\u0001\u0000\u0000\u0000"+
		"\u01d2\u01d0\u0001\u0000\u0000\u0000\u01d3\u01d4\u0005\b\u0000\u0000\u01d4"+
		"1\u0001\u0000\u0000\u0000\u01d5\u01d6\u0005U\u0000\u0000\u01d6\u01d7\u0003"+
		"\u0084B\u0000\u01d7\u01e0\u0005\u0004\u0000\u0000\u01d8\u01dd\u00034\u001a"+
		"\u0000\u01d9\u01da\u0005\u0005\u0000\u0000\u01da\u01dc\u00034\u001a\u0000"+
		"\u01db\u01d9\u0001\u0000\u0000\u0000\u01dc\u01df\u0001\u0000\u0000\u0000"+
		"\u01dd\u01db\u0001\u0000\u0000\u0000\u01dd\u01de\u0001\u0000\u0000\u0000"+
		"\u01de\u01e1\u0001\u0000\u0000\u0000\u01df\u01dd\u0001\u0000\u0000\u0000"+
		"\u01e0\u01d8\u0001\u0000\u0000\u0000\u01e0\u01e1\u0001\u0000\u0000\u0000"+
		"\u01e1\u01e2\u0001\u0000\u0000\u0000\u01e2\u01e3\u0005\u0006\u0000\u0000"+
		"\u01e3\u01e4\u0005\t\u0000\u0000\u01e4\u01ea\u0003x<\u0000\u01e5\u01eb"+
		"\u0005\u0002\u0000\u0000\u01e6\u01e7\u0005\u0007\u0000\u0000\u01e7\u01e8"+
		"\u0003V+\u0000\u01e8\u01e9\u0005\b\u0000\u0000\u01e9\u01eb\u0001\u0000"+
		"\u0000\u0000\u01ea\u01e5\u0001\u0000\u0000\u0000\u01ea\u01e6\u0001\u0000"+
		"\u0000\u0000\u01eb3\u0001\u0000\u0000\u0000\u01ec\u01ed\u0003\u0086C\u0000"+
		"\u01ed\u01ee\u0005\t\u0000\u0000\u01ee\u01ef\u0003x<\u0000\u01ef5\u0001"+
		"\u0000\u0000\u0000\u01f0\u01f1\u0003V+\u0000\u01f1\u01f2\u0005\u0000\u0000"+
		"\u0001\u01f27\u0001\u0000\u0000\u0000\u01f3\u01f4\u0003:\u001d\u0000\u01f4"+
		"\u01f5\u0005\u0000\u0000\u0001\u01f59\u0001\u0000\u0000\u0000\u01f6\u01f8"+
		"\u0003<\u001e\u0000\u01f7\u01f6\u0001\u0000\u0000\u0000\u01f8\u01fb\u0001"+
		"\u0000\u0000\u0000\u01f9\u01f7\u0001\u0000\u0000\u0000\u01f9\u01fa\u0001"+
		"\u0000\u0000\u0000\u01fa\u0200\u0001\u0000\u0000\u0000\u01fb\u01f9\u0001"+
		"\u0000\u0000\u0000\u01fc\u01ff\u0003>\u001f\u0000\u01fd\u01ff\u0003@ "+
		"\u0000\u01fe\u01fc\u0001\u0000\u0000\u0000\u01fe\u01fd\u0001\u0000\u0000"+
		"\u0000\u01ff\u0202\u0001\u0000\u0000\u0000\u0200\u01fe\u0001\u0000\u0000"+
		"\u0000\u0200\u0201\u0001\u0000\u0000\u0000\u0201;\u0001\u0000\u0000\u0000"+
		"\u0202\u0200\u0001\u0000\u0000\u0000\u0203\u0207\u0007\u0001\u0000\u0000"+
		"\u0204\u0205\u0003\u0086C\u0000\u0205\u0206\u0005\t\u0000\u0000\u0206"+
		"\u0208\u0001\u0000\u0000\u0000\u0207\u0204\u0001\u0000\u0000\u0000\u0207"+
		"\u0208\u0001\u0000\u0000\u0000\u0208\u0209\u0001\u0000\u0000\u0000\u0209"+
		"\u020c\u0003\u0084B\u0000\u020a\u020b\u0005_\u0000\u0000\u020b\u020d\u0005"+
		"\u0001\u0000\u0000\u020c\u020a\u0001\u0000\u0000\u0000\u020c\u020d\u0001"+
		"\u0000\u0000\u0000\u020d=\u0001\u0000\u0000\u0000\u020e\u020f\u0005\u0010"+
		"\u0000\u0000\u020f\u0213\u0003\u0084B\u0000\u0210\u0212\u0003@ \u0000"+
		"\u0211\u0210\u0001\u0000\u0000\u0000\u0212\u0215\u0001\u0000\u0000\u0000"+
		"\u0213\u0211\u0001\u0000\u0000\u0000\u0213\u0214\u0001\u0000\u0000\u0000"+
		"\u0214\u0216\u0001\u0000\u0000\u0000\u0215\u0213\u0001\u0000\u0000\u0000"+
		"\u0216\u0217\u0005\u0011\u0000\u0000\u0217?\u0001\u0000\u0000\u0000\u0218"+
		"\u021c\u0003B!\u0000\u0219\u021c\u0003J%\u0000\u021a\u021c\u0003N\'\u0000"+
		"\u021b\u0218\u0001\u0000\u0000\u0000\u021b\u0219\u0001\u0000\u0000\u0000"+
		"\u021b\u021a\u0001\u0000\u0000\u0000\u021cA\u0001\u0000\u0000\u0000\u021d"+
		"\u0221\u0005\u0012\u0000\u0000\u021e\u021f\u0003\u0086C\u0000\u021f\u0220"+
		"\u0005\t\u0000\u0000\u0220\u0222\u0001\u0000\u0000\u0000\u0221\u021e\u0001"+
		"\u0000\u0000\u0000\u0221\u0222\u0001\u0000\u0000\u0000\u0222\u0223\u0001"+
		"\u0000\u0000\u0000\u0223\u0225\u0003\u0084B\u0000\u0224\u0226\u0003D\""+
		"\u0000\u0225\u0224\u0001\u0000\u0000\u0000\u0226\u0227\u0001\u0000\u0000"+
		"\u0000\u0227\u0225\u0001\u0000\u0000\u0000\u0227\u0228\u0001\u0000\u0000"+
		"\u0000\u0228C\u0001\u0000\u0000\u0000\u0229\u022c\u0003F#\u0000\u022a"+
		"\u022c\u0003H$\u0000\u022b\u0229\u0001\u0000\u0000\u0000\u022b\u022a\u0001"+
		"\u0000\u0000\u0000\u022cE\u0001\u0000\u0000\u0000\u022d\u0235\u0005\u0013"+
		"\u0000\u0000\u022e\u0233\u0003\u0086C\u0000\u022f\u0230\u0005\u0004\u0000"+
		"\u0000\u0230\u0231\u0003V+\u0000\u0231\u0232\u0005\u0006\u0000\u0000\u0232"+
		"\u0234\u0001\u0000\u0000\u0000\u0233\u022f\u0001\u0000\u0000\u0000\u0233"+
		"\u0234\u0001\u0000\u0000\u0000\u0234\u0236\u0001\u0000\u0000\u0000\u0235"+
		"\u022e\u0001\u0000\u0000\u0000\u0235\u0236\u0001\u0000\u0000\u0000\u0236"+
		"\u0237\u0001\u0000\u0000\u0000\u0237\u0238\u0005\t\u0000\u0000\u0238\u0239"+
		"\u0003V+\u0000\u0239G\u0001\u0000\u0000\u0000\u023a\u023c\u0005\u0014"+
		"\u0000\u0000\u023b\u023a\u0001\u0000\u0000\u0000\u023b\u023c\u0001\u0000"+
		"\u0000\u0000\u023c\u023d\u0001\u0000\u0000\u0000\u023d\u023f\u0005\u0015"+
		"\u0000\u0000\u023e\u0240\u0003\u0086C\u0000\u023f\u023e\u0001\u0000\u0000"+
		"\u0000\u023f\u0240\u0001\u0000\u0000\u0000\u0240\u0241\u0001\u0000\u0000"+
		"\u0000\u0241\u0253\u0005\t\u0000\u0000\u0242\u0243\u0003\u0086C\u0000"+
		"\u0243\u0244\u0005\t\u0000\u0000\u0244\u0245\u0003x<\u0000\u0245\u0246"+
		"\u0005\n\u0000\u0000\u0246\u0247\u0003V+\u0000\u0247\u0254\u0001\u0000"+
		"\u0000\u0000\u0248\u0249\u0003\u0086C\u0000\u0249\u024b\u0005\u0004\u0000"+
		"\u0000\u024a\u024c\u0003R)\u0000\u024b\u024a\u0001\u0000\u0000\u0000\u024b"+
		"\u024c\u0001\u0000\u0000\u0000\u024c\u024d\u0001\u0000\u0000\u0000\u024d"+
		"\u024e\u0005\u0006\u0000\u0000\u024e\u024f\u0005\t\u0000\u0000\u024f\u0250"+
		"\u0003x<\u0000\u0250\u0251\u0005\n\u0000\u0000\u0251\u0252\u0003V+\u0000"+
		"\u0252\u0254\u0001\u0000\u0000\u0000\u0253\u0242\u0001\u0000\u0000\u0000"+
		"\u0253\u0248\u0001\u0000\u0000\u0000\u0254I\u0001\u0000\u0000\u0000\u0255"+
		"\u0259\u0005\u0012\u0000\u0000\u0256\u0257\u0003\u0084B\u0000\u0257\u0258"+
		"\u0005_\u0000\u0000\u0258\u025a\u0001\u0000\u0000\u0000\u0259\u0256\u0001"+
		"\u0000\u0000\u0000\u0259\u025a\u0001\u0000\u0000\u0000\u025a\u025b\u0001"+
		"\u0000\u0000\u0000\u025b\u025c\u0003\u0086C\u0000\u025c\u025e\u0005\u0004"+
		"\u0000\u0000\u025d\u025f\u0003R)\u0000\u025e\u025d\u0001\u0000\u0000\u0000"+
		"\u025e\u025f\u0001\u0000\u0000\u0000\u025f\u0260\u0001\u0000\u0000\u0000"+
		"\u0260\u0263\u0005\u0006\u0000\u0000\u0261\u0262\u0005\t\u0000\u0000\u0262"+
		"\u0264\u0003x<\u0000\u0263\u0261\u0001\u0000\u0000\u0000\u0263\u0264\u0001"+
		"\u0000\u0000\u0000\u0264\u0266\u0001\u0000\u0000\u0000\u0265\u0267\u0003"+
		"L&\u0000\u0266\u0265\u0001\u0000\u0000\u0000\u0267\u0268\u0001\u0000\u0000"+
		"\u0000\u0268\u0266\u0001\u0000\u0000\u0000\u0268\u0269\u0001\u0000\u0000"+
		"\u0000\u0269K\u0001\u0000\u0000\u0000\u026a\u026c\u0005\u0016\u0000\u0000"+
		"\u026b\u026d\u0003\u0086C\u0000\u026c\u026b\u0001\u0000\u0000\u0000\u026c"+
		"\u026d\u0001\u0000\u0000\u0000\u026d\u026e\u0001\u0000\u0000\u0000\u026e"+
		"\u026f\u0005\t\u0000\u0000\u026f\u027d\u0003V+\u0000\u0270\u0272\u0005"+
		"\u0017\u0000\u0000\u0271\u0273\u0003\u0086C\u0000\u0272\u0271\u0001\u0000"+
		"\u0000\u0000\u0272\u0273\u0001\u0000\u0000\u0000\u0273\u0274\u0001\u0000"+
		"\u0000\u0000\u0274\u0275\u0005\t\u0000\u0000\u0275\u027d\u0003V+\u0000"+
		"\u0276\u0278\u0005\u0018\u0000\u0000\u0277\u0279\u0003\u0086C\u0000\u0278"+
		"\u0277\u0001\u0000\u0000\u0000\u0278\u0279\u0001\u0000\u0000\u0000\u0279"+
		"\u027a\u0001\u0000\u0000\u0000\u027a\u027b\u0005\t\u0000\u0000\u027b\u027d"+
		"\u0003V+\u0000\u027c\u026a\u0001\u0000\u0000\u0000\u027c\u0270\u0001\u0000"+
		"\u0000\u0000\u027c\u0276\u0001\u0000\u0000\u0000\u027dM\u0001\u0000\u0000"+
		"\u0000\u027e\u027f\u0005\u0012\u0000\u0000\u027f\u0280\u0003\u0084B\u0000"+
		"\u0280\u0281\u0005_\u0000\u0000\u0281\u0282\u0003\u0086C\u0000\u0282\u0283"+
		"\u0005\t\u0000\u0000\u0283\u0285\u0003x<\u0000\u0284\u0286\u0003P(\u0000"+
		"\u0285\u0284\u0001\u0000\u0000\u0000\u0286\u0287\u0001\u0000\u0000\u0000"+
		"\u0287\u0285\u0001\u0000\u0000\u0000\u0287\u0288\u0001\u0000\u0000\u0000"+
		"\u0288O\u0001\u0000\u0000\u0000\u0289\u028b\u0005\u0019\u0000\u0000\u028a"+
		"\u028c\u0003\u0086C\u0000\u028b\u028a\u0001\u0000\u0000\u0000\u028b\u028c"+
		"\u0001\u0000\u0000\u0000\u028c\u028d\u0001\u0000\u0000\u0000\u028d\u028e"+
		"\u0005\t\u0000\u0000\u028e\u0296\u0003V+\u0000\u028f\u0291\u0005\u001a"+
		"\u0000\u0000\u0290\u0292\u0003\u0086C\u0000\u0291\u0290\u0001\u0000\u0000"+
		"\u0000\u0291\u0292\u0001\u0000\u0000\u0000\u0292\u0293\u0001\u0000\u0000"+
		"\u0000\u0293\u0294\u0005\t\u0000\u0000\u0294\u0296\u0003V+\u0000\u0295"+
		"\u0289\u0001\u0000\u0000\u0000\u0295\u028f\u0001\u0000\u0000\u0000\u0296"+
		"Q\u0001\u0000\u0000\u0000\u0297\u029c\u0003T*\u0000\u0298\u0299\u0005"+
		"\u0005\u0000\u0000\u0299\u029b\u0003T*\u0000\u029a\u0298\u0001\u0000\u0000"+
		"\u0000\u029b\u029e\u0001\u0000\u0000\u0000\u029c\u029a\u0001\u0000\u0000"+
		"\u0000\u029c\u029d\u0001\u0000\u0000\u0000\u029dS\u0001\u0000\u0000\u0000"+
		"\u029e\u029c\u0001\u0000\u0000\u0000\u029f\u02a0\u0003\u0086C\u0000\u02a0"+
		"\u02a1\u0005\t\u0000\u0000\u02a1\u02a2\u0003x<\u0000\u02a2U\u0001\u0000"+
		"\u0000\u0000\u02a3\u02a4\u0006+\uffff\uffff\u0000\u02a4\u02a5\u0005\u001e"+
		"\u0000\u0000\u02a5\u02aa\u0003V+\u000b\u02a6\u02a7\u0005\u001f\u0000\u0000"+
		"\u02a7\u02aa\u0003V+\n\u02a8\u02aa\u0003X,\u0000\u02a9\u02a3\u0001\u0000"+
		"\u0000\u0000\u02a9\u02a6\u0001\u0000\u0000\u0000\u02a9\u02a8\u0001\u0000"+
		"\u0000\u0000\u02aa\u02cb\u0001\u0000\u0000\u0000\u02ab\u02ac\n\t\u0000"+
		"\u0000\u02ac\u02ad\u0007\u0002\u0000\u0000\u02ad\u02ca\u0003V+\n\u02ae"+
		"\u02af\n\b\u0000\u0000\u02af\u02b0\u0007\u0003\u0000\u0000\u02b0\u02ca"+
		"\u0003V+\t\u02b1\u02b2\n\u0007\u0000\u0000\u02b2\u02b3\u0007\u0004\u0000"+
		"\u0000\u02b3\u02ca\u0003V+\b\u02b4\u02b5\n\u0006\u0000\u0000\u02b5\u02b6"+
		"\u0007\u0005\u0000\u0000\u02b6\u02ca\u0003V+\u0007\u02b7\u02b8\n\u0005"+
		"\u0000\u0000\u02b8\u02b9\u0005\'\u0000\u0000\u02b9\u02ca\u0003V+\u0006"+
		"\u02ba\u02bb\n\u0004\u0000\u0000\u02bb\u02bc\u0005(\u0000\u0000\u02bc"+
		"\u02ca\u0003V+\u0005\u02bd\u02be\n\u0003\u0000\u0000\u02be\u02bf\u0005"+
		")\u0000\u0000\u02bf\u02ca\u0003V+\u0004\u02c0\u02c1\n\u0002\u0000\u0000"+
		"\u02c1\u02c2\u0005*\u0000\u0000\u02c2\u02ca\u0003V+\u0003\u02c3\u02c4"+
		"\n\r\u0000\u0000\u02c4\u02c5\u0007\u0006\u0000\u0000\u02c5\u02ca\u0003"+
		"Z-\u0000\u02c6\u02c7\n\f\u0000\u0000\u02c7\u02c8\u0007\u0007\u0000\u0000"+
		"\u02c8\u02ca\u0003\\.\u0000\u02c9\u02ab\u0001\u0000\u0000\u0000\u02c9"+
		"\u02ae\u0001\u0000\u0000\u0000\u02c9\u02b1\u0001\u0000\u0000\u0000\u02c9"+
		"\u02b4\u0001\u0000\u0000\u0000\u02c9\u02b7\u0001\u0000\u0000\u0000\u02c9"+
		"\u02ba\u0001\u0000\u0000\u0000\u02c9\u02bd\u0001\u0000\u0000\u0000\u02c9"+
		"\u02c0\u0001\u0000\u0000\u0000\u02c9\u02c3\u0001\u0000\u0000\u0000\u02c9"+
		"\u02c6\u0001\u0000\u0000\u0000\u02ca\u02cd\u0001\u0000\u0000\u0000\u02cb"+
		"\u02c9\u0001\u0000\u0000\u0000\u02cb\u02cc\u0001\u0000\u0000\u0000\u02cc"+
		"W\u0001\u0000\u0000\u0000\u02cd\u02cb\u0001\u0000\u0000\u0000\u02ce\u02cf"+
		"\u0005\u0004\u0000\u0000\u02cf\u02d0\u0003V+\u0000\u02d0\u02d1\u0005\u0006"+
		"\u0000\u0000\u02d1\u0301\u0001\u0000\u0000\u0000\u02d2\u0301\u0005+\u0000"+
		"\u0000\u02d3\u02d4\u0005,\u0000\u0000\u02d4\u02d5\u0003V+\u0000\u02d5"+
		"\u02d6\u0005-\u0000\u0000\u02d6\u02de\u0003V+\u0000\u02d7\u02d8\u0005"+
		".\u0000\u0000\u02d8\u02d9\u0003V+\u0000\u02d9\u02da\u0005-\u0000\u0000"+
		"\u02da\u02db\u0003V+\u0000\u02db\u02dd\u0001\u0000\u0000\u0000\u02dc\u02d7"+
		"\u0001\u0000\u0000\u0000\u02dd\u02e0\u0001\u0000\u0000\u0000\u02de\u02dc"+
		"\u0001\u0000\u0000\u0000\u02de\u02df\u0001\u0000\u0000\u0000\u02df\u02e1"+
		"\u0001\u0000\u0000\u0000\u02e0\u02de\u0001\u0000\u0000\u0000\u02e1\u02e2"+
		"\u0005/\u0000\u0000\u02e2\u02e3\u0003V+\u0000\u02e3\u02e4\u00050\u0000"+
		"\u0000\u02e4\u0301\u0001\u0000\u0000\u0000\u02e5\u02e6\u00051\u0000\u0000"+
		"\u02e6\u02eb\u0003b1\u0000\u02e7\u02e8\u0005\u0005\u0000\u0000\u02e8\u02ea"+
		"\u0003b1\u0000\u02e9\u02e7\u0001\u0000\u0000\u0000\u02ea\u02ed\u0001\u0000"+
		"\u0000\u0000\u02eb\u02e9\u0001\u0000\u0000\u0000\u02eb\u02ec\u0001\u0000"+
		"\u0000\u0000\u02ec\u02ee\u0001\u0000\u0000\u0000\u02ed\u02eb\u0001\u0000"+
		"\u0000\u0000\u02ee\u02ef\u00052\u0000\u0000\u02ef\u02f0\u0003V+\u0000"+
		"\u02f0\u0301\u0001\u0000\u0000\u0000\u02f1\u0301\u0003f3\u0000\u02f2\u02f3"+
		"\u0003\u0084B\u0000\u02f3\u02f5\u0005\u0004\u0000\u0000\u02f4\u02f6\u0003"+
		"`0\u0000\u02f5\u02f4\u0001\u0000\u0000\u0000\u02f5\u02f6\u0001\u0000\u0000"+
		"\u0000\u02f6\u02f7\u0001\u0000\u0000\u0000\u02f7\u02f9\u0005\u0006\u0000"+
		"\u0000\u02f8\u02fa\u0003d2\u0000\u02f9\u02f8\u0001\u0000\u0000\u0000\u02f9"+
		"\u02fa\u0001\u0000\u0000\u0000\u02fa\u0301\u0001\u0000\u0000\u0000\u02fb"+
		"\u0301\u0003z=\u0000\u02fc\u0301\u0003|>\u0000\u02fd\u0301\u0003~?\u0000"+
		"\u02fe\u0301\u0003\u0080@\u0000\u02ff\u0301\u0003\u0084B\u0000\u0300\u02ce"+
		"\u0001\u0000\u0000\u0000\u0300\u02d2\u0001\u0000\u0000\u0000\u0300\u02d3"+
		"\u0001\u0000\u0000\u0000\u0300\u02e5\u0001\u0000\u0000\u0000\u0300\u02f1"+
		"\u0001\u0000\u0000\u0000\u0300\u02f2\u0001\u0000\u0000\u0000\u0300\u02fb"+
		"\u0001\u0000\u0000\u0000\u0300\u02fc\u0001\u0000\u0000\u0000\u0300\u02fd"+
		"\u0001\u0000\u0000\u0000\u0300\u02fe\u0001\u0000\u0000\u0000\u0300\u02ff"+
		"\u0001\u0000\u0000\u0000\u0301Y\u0001\u0000\u0000\u0000\u0302\u0303\u0003"+
		"\u0084B\u0000\u0303\u0304\u0005_\u0000\u0000\u0304\u0306\u0001\u0000\u0000"+
		"\u0000\u0305\u0302\u0001\u0000\u0000\u0000\u0305\u0306\u0001\u0000\u0000"+
		"\u0000\u0306\u0307\u0001\u0000\u0000\u0000\u0307\u0308\u0003\u0086C\u0000"+
		"\u0308\u030a\u0005\u0004\u0000\u0000\u0309\u030b\u0003`0\u0000\u030a\u0309"+
		"\u0001\u0000\u0000\u0000\u030a\u030b\u0001\u0000\u0000\u0000\u030b\u030c"+
		"\u0001\u0000\u0000\u0000\u030c\u030e\u0005\u0006\u0000\u0000\u030d\u030f"+
		"\u0003d2\u0000\u030e\u030d\u0001\u0000\u0000\u0000\u030e\u030f\u0001\u0000"+
		"\u0000\u0000\u030f\u031a\u0001\u0000\u0000\u0000\u0310\u0311\u0003\u0084"+
		"B\u0000\u0311\u0312\u0005_\u0000\u0000\u0312\u0314\u0001\u0000\u0000\u0000"+
		"\u0313\u0310\u0001\u0000\u0000\u0000\u0313\u0314\u0001\u0000\u0000\u0000"+
		"\u0314\u0315\u0001\u0000\u0000\u0000\u0315\u0317\u0003\u0086C\u0000\u0316"+
		"\u0318\u0003d2\u0000\u0317\u0316\u0001\u0000\u0000\u0000\u0317\u0318\u0001"+
		"\u0000\u0000\u0000\u0318\u031a\u0001\u0000\u0000\u0000\u0319\u0305\u0001"+
		"\u0000\u0000\u0000\u0319\u0313\u0001\u0000\u0000\u0000\u031a[\u0001\u0000"+
		"\u0000\u0000\u031b\u031c\u0003\u0086C\u0000\u031c\u031d\u0005\u0004\u0000"+
		"\u0000\u031d\u031e\u0003^/\u0000\u031e\u031f\u00053\u0000\u0000\u031f"+
		"\u0320\u0003V+\u0000\u0320\u0321\u0005\u0006\u0000\u0000\u0321\u033d\u0001"+
		"\u0000\u0000\u0000\u0322\u0323\u0003\u0086C\u0000\u0323\u0324\u0005\u0004"+
		"\u0000\u0000\u0324\u0327\u0003\u0086C\u0000\u0325\u0326\u0005\t\u0000"+
		"\u0000\u0326\u0328\u0003x<\u0000\u0327\u0325\u0001\u0000\u0000\u0000\u0327"+
		"\u0328\u0001\u0000\u0000\u0000\u0328\u0329\u0001\u0000\u0000\u0000\u0329"+
		"\u032a\u0005\u0002\u0000\u0000\u032a\u032d\u0003\u0086C\u0000\u032b\u032c"+
		"\u0005\t\u0000\u0000\u032c\u032e\u0003x<\u0000\u032d\u032b\u0001\u0000"+
		"\u0000\u0000\u032d\u032e\u0001\u0000\u0000\u0000\u032e\u032f\u0001\u0000"+
		"\u0000\u0000\u032f\u0330\u0005\n\u0000\u0000\u0330\u0331\u0003V+\u0000"+
		"\u0331\u0332\u00053\u0000\u0000\u0332\u0333\u0003V+\u0000\u0333\u0334"+
		"\u0005\u0006\u0000\u0000\u0334\u033d\u0001\u0000\u0000\u0000\u0335\u0336"+
		"\u0003\u0086C\u0000\u0336\u0338\u0005\u0004\u0000\u0000\u0337\u0339\u0003"+
		"`0\u0000\u0338\u0337\u0001\u0000\u0000\u0000\u0338\u0339\u0001\u0000\u0000"+
		"\u0000\u0339\u033a\u0001\u0000\u0000\u0000\u033a\u033b\u0005\u0006\u0000"+
		"\u0000\u033b\u033d\u0001\u0000\u0000\u0000\u033c\u031b\u0001\u0000\u0000"+
		"\u0000\u033c\u0322\u0001\u0000\u0000\u0000\u033c\u0335\u0001\u0000\u0000"+
		"\u0000\u033d]\u0001\u0000\u0000\u0000\u033e\u0341\u0003\u0086C\u0000\u033f"+
		"\u0340\u0005\t\u0000\u0000\u0340\u0342\u0003x<\u0000\u0341\u033f\u0001"+
		"\u0000\u0000\u0000\u0341\u0342\u0001\u0000\u0000\u0000\u0342\u034b\u0001"+
		"\u0000\u0000\u0000\u0343\u0344\u0005\u0005\u0000\u0000\u0344\u0347\u0003"+
		"\u0086C\u0000\u0345\u0346\u0005\t\u0000\u0000\u0346\u0348\u0003x<\u0000"+
		"\u0347\u0345\u0001\u0000\u0000\u0000\u0347\u0348\u0001\u0000\u0000\u0000"+
		"\u0348\u034a\u0001\u0000\u0000\u0000\u0349\u0343\u0001\u0000\u0000\u0000"+
		"\u034a\u034d\u0001\u0000\u0000\u0000\u034b\u0349\u0001\u0000\u0000\u0000"+
		"\u034b\u034c\u0001\u0000\u0000\u0000\u034c_\u0001\u0000\u0000\u0000\u034d"+
		"\u034b\u0001\u0000\u0000\u0000\u034e\u0353\u0003V+\u0000\u034f\u0350\u0005"+
		"\u0005\u0000\u0000\u0350\u0352\u0003V+\u0000\u0351\u034f\u0001\u0000\u0000"+
		"\u0000\u0352\u0355\u0001\u0000\u0000\u0000\u0353\u0351\u0001\u0000\u0000"+
		"\u0000\u0353\u0354\u0001\u0000\u0000\u0000\u0354a\u0001\u0000\u0000\u0000"+
		"\u0355\u0353\u0001\u0000\u0000\u0000\u0356\u0359\u0003\u0086C\u0000\u0357"+
		"\u0358\u0005\t\u0000\u0000\u0358\u035a\u0003x<\u0000\u0359\u0357\u0001"+
		"\u0000\u0000\u0000\u0359\u035a\u0001\u0000\u0000\u0000\u035a\u035b\u0001"+
		"\u0000\u0000\u0000\u035b\u035c\u0005\n\u0000\u0000\u035c\u035d\u0003V"+
		"+\u0000\u035dc\u0001\u0000\u0000\u0000\u035e\u035f\u00054\u0000\u0000"+
		"\u035f\u0360\u0005\u0016\u0000\u0000\u0360e\u0001\u0000\u0000\u0000\u0361"+
		"\u036d\u0005b\u0000\u0000\u0362\u036d\u0005a\u0000\u0000\u0363\u036d\u0003"+
		"h4\u0000\u0364\u036d\u00055\u0000\u0000\u0365\u036d\u00056\u0000\u0000"+
		"\u0366\u036d\u00057\u0000\u0000\u0367\u036d\u00058\u0000\u0000\u0368\u036d"+
		"\u0005\u0001\u0000\u0000\u0369\u036d\u0003j5\u0000\u036a\u036d\u0003p"+
		"8\u0000\u036b\u036d\u0003t:\u0000\u036c\u0361\u0001\u0000\u0000\u0000"+
		"\u036c\u0362\u0001\u0000\u0000\u0000\u036c\u0363\u0001\u0000\u0000\u0000"+
		"\u036c\u0364\u0001\u0000\u0000\u0000\u036c\u0365\u0001\u0000\u0000\u0000"+
		"\u036c\u0366\u0001\u0000\u0000\u0000\u036c\u0367\u0001\u0000\u0000\u0000"+
		"\u036c\u0368\u0001\u0000\u0000\u0000\u036c\u0369\u0001\u0000\u0000\u0000"+
		"\u036c\u036a\u0001\u0000\u0000\u0000\u036c\u036b\u0001\u0000\u0000\u0000"+
		"\u036dg\u0001\u0000\u0000\u0000\u036e\u0370\u0005c\u0000\u0000\u036f\u036e"+
		"\u0001\u0000\u0000\u0000\u0370\u0371\u0001\u0000\u0000\u0000\u0371\u036f"+
		"\u0001\u0000\u0000\u0000\u0371\u0372\u0001\u0000\u0000\u0000\u0372i\u0001"+
		"\u0000\u0000\u0000\u0373\u0374\u0003l6\u0000\u0374\u037d\u0005\u0007\u0000"+
		"\u0000\u0375\u037a\u0003n7\u0000\u0376\u0377\u0005\u0005\u0000\u0000\u0377"+
		"\u0379\u0003n7\u0000\u0378\u0376\u0001\u0000\u0000\u0000\u0379\u037c\u0001"+
		"\u0000\u0000\u0000\u037a\u0378\u0001\u0000\u0000\u0000\u037a\u037b\u0001"+
		"\u0000\u0000\u0000\u037b\u037e\u0001\u0000\u0000\u0000\u037c\u037a\u0001"+
		"\u0000\u0000\u0000\u037d\u0375\u0001\u0000\u0000\u0000\u037d\u037e\u0001"+
		"\u0000\u0000\u0000\u037e\u037f\u0001\u0000\u0000\u0000\u037f\u0380\u0005"+
		"\b\u0000\u0000\u0380k\u0001\u0000\u0000\u0000\u0381\u0382\u0007\b\u0000"+
		"\u0000\u0382m\u0001\u0000\u0000\u0000\u0383\u0386\u0003V+\u0000\u0384"+
		"\u0385\u0005>\u0000\u0000\u0385\u0387\u0003V+\u0000\u0386\u0384\u0001"+
		"\u0000\u0000\u0000\u0386\u0387\u0001\u0000\u0000\u0000\u0387o\u0001\u0000"+
		"\u0000\u0000\u0388\u0389\u0005?\u0000\u0000\u0389\u038a\u0005\u0007\u0000"+
		"\u0000\u038a\u038f\u0003r9\u0000\u038b\u038c\u0005\u0005\u0000\u0000\u038c"+
		"\u038e\u0003r9\u0000\u038d\u038b\u0001\u0000\u0000\u0000\u038e\u0391\u0001"+
		"\u0000\u0000\u0000\u038f\u038d\u0001\u0000\u0000\u0000\u038f\u0390\u0001"+
		"\u0000\u0000\u0000\u0390\u0392\u0001\u0000\u0000\u0000\u0391\u038f\u0001"+
		"\u0000\u0000\u0000\u0392\u0393\u0005\b\u0000\u0000\u0393q\u0001\u0000"+
		"\u0000\u0000\u0394\u0397\u0003\u0086C\u0000\u0395\u0396\u0005\t\u0000"+
		"\u0000\u0396\u0398\u0003x<\u0000\u0397\u0395\u0001\u0000\u0000\u0000\u0397"+
		"\u0398\u0001\u0000\u0000\u0000\u0398\u0399\u0001\u0000\u0000\u0000\u0399"+
		"\u039a\u0005\n\u0000\u0000\u039a\u039b\u0003V+\u0000\u039bs\u0001\u0000"+
		"\u0000\u0000\u039c\u039d\u0005@\u0000\u0000\u039d\u03a6\u0005\u0007\u0000"+
		"\u0000\u039e\u03a3\u0003v;\u0000\u039f\u03a0\u0005\u0005\u0000\u0000\u03a0"+
		"\u03a2\u0003v;\u0000\u03a1\u039f\u0001\u0000\u0000\u0000\u03a2\u03a5\u0001"+
		"\u0000\u0000\u0000\u03a3\u03a1\u0001\u0000\u0000\u0000\u03a3\u03a4\u0001"+
		"\u0000\u0000\u0000\u03a4\u03a7\u0001\u0000\u0000\u0000\u03a5\u03a3\u0001"+
		"\u0000\u0000\u0000\u03a6\u039e\u0001\u0000\u0000\u0000\u03a6\u03a7\u0001"+
		"\u0000\u0000\u0000\u03a7\u03a8\u0001\u0000\u0000\u0000\u03a8\u03a9\u0005"+
		"\b\u0000\u0000\u03a9u\u0001\u0000\u0000\u0000\u03aa\u03ab\u0003V+\u0000"+
		"\u03ab\u03ac\u0007\t\u0000\u0000\u03ac\u03ad\u0003V+\u0000\u03adw\u0001"+
		"\u0000\u0000\u0000\u03ae\u03b4\u0003z=\u0000\u03af\u03b4\u0003|>\u0000"+
		"\u03b0\u03b4\u0003~?\u0000\u03b1\u03b4\u0003\u0080@\u0000\u03b2\u03b4"+
		"\u0003\u0084B\u0000\u03b3\u03ae\u0001\u0000\u0000\u0000\u03b3\u03af\u0001"+
		"\u0000\u0000\u0000\u03b3\u03b0\u0001\u0000\u0000\u0000\u03b3\u03b1\u0001"+
		"\u0000\u0000\u0000\u03b3\u03b2\u0001\u0000\u0000\u0000\u03b4y\u0001\u0000"+
		"\u0000\u0000\u03b5\u03b6\u0007\n\u0000\u0000\u03b6{\u0001\u0000\u0000"+
		"\u0000\u03b7\u03b8\u0003l6\u0000\u03b8\u03b9\u0005\u0004\u0000\u0000\u03b9"+
		"\u03ba\u0003x<\u0000\u03ba\u03bb\u0005\u0006\u0000\u0000\u03bb}\u0001"+
		"\u0000\u0000\u0000\u03bc\u03bd\u0005@\u0000\u0000\u03bd\u03be\u0005\u0004"+
		"\u0000\u0000\u03be\u03bf\u0003x<\u0000\u03bf\u03c0\u0005\u0005\u0000\u0000"+
		"\u03c0\u03c1\u0003x<\u0000\u03c1\u03c2\u0005\u0006\u0000\u0000\u03c2\u007f"+
		"\u0001\u0000\u0000\u0000\u03c3\u03c4\u0005?\u0000\u0000\u03c4\u03c5\u0005"+
		"\u0004\u0000\u0000\u03c5\u03ca\u0003\u0082A\u0000\u03c6\u03c7\u0005\u0005"+
		"\u0000\u0000\u03c7\u03c9\u0003\u0082A\u0000\u03c8\u03c6\u0001\u0000\u0000"+
		"\u0000\u03c9\u03cc\u0001\u0000\u0000\u0000\u03ca\u03c8\u0001\u0000\u0000"+
		"\u0000\u03ca\u03cb\u0001\u0000\u0000\u0000\u03cb\u03cd\u0001\u0000\u0000"+
		"\u0000\u03cc\u03ca\u0001\u0000\u0000\u0000\u03cd\u03ce\u0005\u0006\u0000"+
		"\u0000\u03ce\u0081\u0001\u0000\u0000\u0000\u03cf\u03d0\u0003\u0086C\u0000"+
		"\u03d0\u03d1\u0005\t\u0000\u0000\u03d1\u03d2\u0003x<\u0000\u03d2\u0083"+
		"\u0001\u0000\u0000\u0000\u03d3\u03d8\u0003\u0086C\u0000\u03d4\u03d5\u0005"+
		"_\u0000\u0000\u03d5\u03d7\u0003\u0086C\u0000\u03d6\u03d4\u0001\u0000\u0000"+
		"\u0000\u03d7\u03da\u0001\u0000\u0000\u0000\u03d8\u03d6\u0001\u0000\u0000"+
		"\u0000\u03d8\u03d9\u0001\u0000\u0000\u0000\u03d9\u0085\u0001\u0000\u0000"+
		"\u0000\u03da\u03d8\u0001\u0000\u0000\u0000\u03db\u03dc\u0007\u000b\u0000"+
		"\u0000\u03dc\u0087\u0001\u0000\u0000\u0000p\u008e\u0094\u009d\u00a2\u00ab"+
		"\u00b6\u00bc\u00c2\u00c7\u00c9\u00d7\u00dc\u00e8\u00f6\u00f9\u00fc\u0102"+
		"\u0108\u010d\u010f\u0112\u0115\u011e\u0125\u012a\u0133\u013a\u013e\u0145"+
		"\u0149\u015d\u0163\u0166\u016b\u016f\u017c\u0199\u019c\u01a5\u01ae\u01b4"+
		"\u01b6\u01bb\u01c4\u01d0\u01dd\u01e0\u01ea\u01f9\u01fe\u0200\u0207\u020c"+
		"\u0213\u021b\u0221\u0227\u022b\u0233\u0235\u023b\u023f\u024b\u0253\u0259"+
		"\u025e\u0263\u0268\u026c\u0272\u0278\u027c\u0287\u028b\u0291\u0295\u029c"+
		"\u02a9\u02c9\u02cb\u02de\u02eb\u02f5\u02f9\u0300\u0305\u030a\u030e\u0313"+
		"\u0317\u0319\u0327\u032d\u0338\u033c\u0341\u0347\u034b\u0353\u0359\u036c"+
		"\u0371\u037a\u037d\u0386\u038f\u0397\u03a3\u03a6\u03b3\u03ca\u03d8";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}