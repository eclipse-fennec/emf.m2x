// Generated from grammar/QvtR.g4 by ANTLR 4.13.2
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
		T__66=67, T__67=68, T__68=69, T__69=70, T__70=71, T__71=72, CHECKONLY=73, 
		DOMAIN=74, ENFORCE=75, EXTENDS=76, IMPLEMENTEDBY=77, IMPORT=78, KEY=79, 
		OVERRIDES=80, PRIMITIVE=81, QUERY=82, RELATION=83, TOP=84, TRANSFORMATION=85, 
		WHEN=86, WHERE=87, DEFAULT_VALUES=88, ABSTRACT=89, OPPOSITE=90, PLUSPLUS=91, 
		COLONCOLON=92, UNDERSCORE=93, REAL_LITERAL=94, INTEGER_LITERAL=95, STRING_LITERAL=96, 
		ESCAPED_IDENTIFIER=97, IDENTIFIER=98, WS=99, LINE_COMMENT=100, BLOCK_COMMENT=101;
	public static final int
		RULE_compilationUnitEntry = 0, RULE_compilationUnit = 1, RULE_importDecl = 2, 
		RULE_qualifiedName = 3, RULE_transformationDef = 4, RULE_modelDecl = 5, 
		RULE_metaModelId = 6, RULE_keyDecl = 7, RULE_keyProperty = 8, RULE_relation = 9, 
		RULE_varDeclaration = 10, RULE_domain = 11, RULE_primitiveTypeDomain = 12, 
		RULE_checkEnforceQualifier = 13, RULE_assignmentExp = 14, RULE_template = 15, 
		RULE_objectTemplate = 16, RULE_propertyTemplateList = 17, RULE_propertyTemplate = 18, 
		RULE_collectionTemplate = 19, RULE_memberSelection = 20, RULE_memberItem = 21, 
		RULE_whenClause = 22, RULE_whereClause = 23, RULE_queryDef = 24, RULE_paramDecl = 25, 
		RULE_expressionEntry = 26, RULE_completeOclDocumentEntry = 27, RULE_completeOclDocument = 28, 
		RULE_importDeclaration = 29, RULE_packageDeclaration = 30, RULE_contextDeclaration = 31, 
		RULE_classifierContextDeclaration = 32, RULE_classifierContextBody = 33, 
		RULE_invariantConstraint = 34, RULE_definitionConstraint = 35, RULE_operationContextDeclaration = 36, 
		RULE_operationContextBody = 37, RULE_propertyContextDeclaration = 38, 
		RULE_propertyContextBody = 39, RULE_parameterList = 40, RULE_parameter = 41, 
		RULE_expression = 42, RULE_primaryExpression = 43, RULE_propertyOrCallSuffix = 44, 
		RULE_iteratorOrOperationCall = 45, RULE_iteratorVariables = 46, RULE_argumentList = 47, 
		RULE_letBinding = 48, RULE_isMarkedPre = 49, RULE_literalExpression = 50, 
		RULE_stringLiteral_ = 51, RULE_collectionLiteral = 52, RULE_collectionKind = 53, 
		RULE_collectionLiteralPart = 54, RULE_tupleLiteral = 55, RULE_tupleLiteralPart = 56, 
		RULE_mapLiteral = 57, RULE_mapLiteralPart = 58, RULE_typeExpression = 59, 
		RULE_primitiveType = 60, RULE_collectionType = 61, RULE_mapType = 62, 
		RULE_tupleType = 63, RULE_tupleTypePart = 64, RULE_pathName = 65, RULE_identifier = 66;
	private static String[] makeRuleNames() {
		return new String[] {
			"compilationUnitEntry", "compilationUnit", "importDecl", "qualifiedName", 
			"transformationDef", "modelDecl", "metaModelId", "keyDecl", "keyProperty", 
			"relation", "varDeclaration", "domain", "primitiveTypeDomain", "checkEnforceQualifier", 
			"assignmentExp", "template", "objectTemplate", "propertyTemplateList", 
			"propertyTemplate", "collectionTemplate", "memberSelection", "memberItem", 
			"whenClause", "whereClause", "queryDef", "paramDecl", "expressionEntry", 
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
			"'='", "'include'", "'library'", "'package'", "'endpackage'", "'context'", 
			"'inv'", "'static'", "'def'", "'pre'", "'post'", "'body'", "'init'", 
			"'derive'", "'?.'", "'->'", "'?->'", "'not'", "'-'", "'/'", "'+'", "'<'", 
			"'>'", "'<='", "'>='", "'<>'", "'and'", "'or'", "'xor'", "'implies'", 
			"'self'", "'if'", "'then'", "'elseif'", "'else'", "'endif'", "'let'", 
			"'in'", "'|'", "'@'", "'true'", "'false'", "'null'", "'invalid'", "'Set'", 
			"'OrderedSet'", "'Bag'", "'Sequence'", "'Collection'", "'..'", "'Tuple'", 
			"'Map'", "'with'", "'<-'", "'Boolean'", "'Integer'", "'Real'", "'String'", 
			"'UnlimitedNatural'", "'OclAny'", "'OclVoid'", "'OclInvalid'", "'OclMessage'", 
			"'checkonly'", "'domain'", "'enforce'", "'extends'", "'implementedby'", 
			"'import'", "'key'", "'overrides'", "'primitive'", "'query'", "'relation'", 
			"'top'", "'transformation'", "'when'", "'where'", "'default_values'", 
			"'abstract'", "'opposite'", "'++'", "'::'", "'_'"
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
			null, "CHECKONLY", "DOMAIN", "ENFORCE", "EXTENDS", "IMPLEMENTEDBY", "IMPORT", 
			"KEY", "OVERRIDES", "PRIMITIVE", "QUERY", "RELATION", "TOP", "TRANSFORMATION", 
			"WHEN", "WHERE", "DEFAULT_VALUES", "ABSTRACT", "OPPOSITE", "PLUSPLUS", 
			"COLONCOLON", "UNDERSCORE", "REAL_LITERAL", "INTEGER_LITERAL", "STRING_LITERAL", 
			"ESCAPED_IDENTIFIER", "IDENTIFIER", "WS", "LINE_COMMENT", "BLOCK_COMMENT"
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
			setState(134);
			compilationUnit();
			setState(135);
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
			setState(140);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(137);
				importDecl();
				}
				}
				setState(142);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TRANSFORMATION) {
				{
				{
				setState(143);
				transformationDef();
				}
				}
				setState(148);
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
			setState(149);
			match(IMPORT);
			setState(150);
			qualifiedName();
			setState(155);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(151);
					match(COLONCOLON);
					setState(152);
					qualifiedName();
					}
					} 
				}
				setState(157);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(160);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLONCOLON) {
				{
				setState(158);
				match(COLONCOLON);
				setState(159);
				match(T__0);
				}
			}

			setState(162);
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
			setState(164);
			identifier();
			setState(169);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(165);
				match(T__2);
				setState(166);
				identifier();
				}
				}
				setState(171);
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
			setState(172);
			match(TRANSFORMATION);
			setState(173);
			identifier();
			setState(174);
			match(T__3);
			setState(175);
			modelDecl();
			setState(180);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(176);
				match(T__4);
				setState(177);
				modelDecl();
				}
				}
				setState(182);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(183);
			match(T__5);
			setState(186);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(184);
				match(EXTENDS);
				setState(185);
				identifier();
				}
			}

			setState(188);
			match(T__6);
			setState(192);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==KEY) {
				{
				{
				setState(189);
				keyDecl();
				}
				}
				setState(194);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(199);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 82)) & ~0x3f) == 0 && ((1L << (_la - 82)) & 135L) != 0)) {
				{
				setState(197);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case RELATION:
				case TOP:
				case ABSTRACT:
					{
					setState(195);
					relation();
					}
					break;
				case QUERY:
					{
					setState(196);
					queryDef();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(201);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(202);
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
			setState(204);
			identifier();
			setState(205);
			match(T__8);
			setState(218);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				{
				setState(206);
				metaModelId();
				}
				break;
			case T__6:
				{
				setState(207);
				match(T__6);
				setState(208);
				metaModelId();
				setState(213);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(209);
					match(T__4);
					setState(210);
					metaModelId();
					}
					}
					setState(215);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(216);
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
			setState(220);
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
			setState(222);
			match(KEY);
			setState(223);
			pathName();
			setState(224);
			match(T__6);
			setState(225);
			keyProperty();
			setState(230);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(226);
				match(T__4);
				setState(227);
				keyProperty();
				}
				}
				setState(232);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(233);
			match(T__7);
			setState(234);
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
			setState(244);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(236);
				identifier();
				}
				break;
			case OPPOSITE:
				enterOuterAlt(_localctx, 2);
				{
				setState(237);
				match(OPPOSITE);
				setState(238);
				match(T__3);
				setState(239);
				pathName();
				setState(240);
				match(COLONCOLON);
				setState(241);
				identifier();
				setState(242);
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
			setState(247);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TOP) {
				{
				setState(246);
				match(TOP);
				}
			}

			setState(250);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ABSTRACT) {
				{
				setState(249);
				match(ABSTRACT);
				}
			}

			setState(252);
			match(RELATION);
			setState(253);
			identifier();
			setState(256);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OVERRIDES) {
				{
				setState(254);
				match(OVERRIDES);
				setState(255);
				identifier();
				}
			}

			setState(258);
			match(T__6);
			setState(262);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				{
				setState(259);
				varDeclaration();
				}
				}
				setState(264);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(267); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(267);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case CHECKONLY:
				case DOMAIN:
				case ENFORCE:
					{
					setState(265);
					domain();
					}
					break;
				case PRIMITIVE:
					{
					setState(266);
					primitiveTypeDomain();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(269); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 73)) & ~0x3f) == 0 && ((1L << (_la - 73)) & 263L) != 0) );
			setState(272);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHEN) {
				{
				setState(271);
				whenClause();
				}
			}

			setState(275);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(274);
				whereClause();
				}
			}

			setState(277);
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
			setState(279);
			identifier();
			setState(284);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(280);
				match(T__4);
				setState(281);
				identifier();
				}
				}
				setState(286);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(287);
			match(T__8);
			setState(288);
			typeExpression();
			setState(291);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(289);
				match(T__9);
				setState(290);
				expression(0);
				}
			}

			setState(293);
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
			setState(296);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CHECKONLY || _la==ENFORCE) {
				{
				setState(295);
				checkEnforceQualifier();
				}
			}

			setState(298);
			match(DOMAIN);
			setState(299);
			identifier();
			setState(300);
			template();
			setState(305);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(301);
				match(T__4);
				setState(302);
				template();
				}
				}
				setState(307);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(316);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IMPLEMENTEDBY) {
				{
				setState(308);
				match(IMPLEMENTEDBY);
				setState(309);
				pathName();
				setState(310);
				match(T__3);
				setState(312);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4034173033898835986L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 33285997055L) != 0)) {
					{
					setState(311);
					argumentList();
					}
				}

				setState(314);
				match(T__5);
				}
			}

			setState(327);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT_VALUES) {
				{
				setState(318);
				match(DEFAULT_VALUES);
				setState(319);
				match(T__6);
				setState(321); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(320);
					assignmentExp();
					}
					}
					setState(323); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==ESCAPED_IDENTIFIER || _la==IDENTIFIER );
				setState(325);
				match(T__7);
				}
			}

			setState(329);
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
			setState(331);
			match(PRIMITIVE);
			setState(332);
			match(DOMAIN);
			setState(333);
			identifier();
			setState(334);
			match(T__8);
			setState(335);
			typeExpression();
			setState(336);
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
			setState(338);
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
			setState(340);
			identifier();
			setState(341);
			match(T__9);
			setState(342);
			expression(0);
			setState(343);
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
			setState(347);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(345);
				objectTemplate();
				}
				break;
			case 2:
				{
				setState(346);
				collectionTemplate();
				}
				break;
			}
			setState(353);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(349);
				match(T__6);
				setState(350);
				expression(0);
				setState(351);
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
			setState(356);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(355);
				identifier();
				}
			}

			setState(358);
			match(T__8);
			setState(359);
			pathName();
			setState(360);
			match(T__6);
			setState(362);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 90)) & ~0x3f) == 0 && ((1L << (_la - 90)) & 385L) != 0)) {
				{
				setState(361);
				propertyTemplateList();
				}
			}

			setState(364);
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
		enterRule(_localctx, 34, RULE_propertyTemplateList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(366);
			propertyTemplate();
			setState(371);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(367);
				match(T__4);
				setState(368);
				propertyTemplate();
				}
				}
				setState(373);
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
		enterRule(_localctx, 36, RULE_propertyTemplate);
		try {
			setState(387);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(374);
				identifier();
				setState(375);
				match(T__9);
				setState(376);
				expression(0);
				}
				break;
			case OPPOSITE:
				enterOuterAlt(_localctx, 2);
				{
				setState(378);
				match(OPPOSITE);
				setState(379);
				match(T__3);
				setState(380);
				pathName();
				setState(381);
				match(COLONCOLON);
				setState(382);
				identifier();
				setState(383);
				match(T__5);
				setState(384);
				match(T__9);
				setState(385);
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
		enterRule(_localctx, 38, RULE_collectionTemplate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(390);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(389);
				identifier();
				}
			}

			setState(392);
			match(T__8);
			setState(393);
			collectionKind();
			setState(394);
			match(T__3);
			setState(395);
			typeExpression();
			setState(396);
			match(T__5);
			setState(397);
			match(T__6);
			setState(399);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8 || ((((_la - 93)) & ~0x3f) == 0 && ((1L << (_la - 93)) & 49L) != 0)) {
				{
				setState(398);
				memberSelection();
				}
			}

			setState(401);
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
		enterRule(_localctx, 40, RULE_memberSelection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403);
			memberItem();
			setState(408);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(404);
				match(T__4);
				setState(405);
				memberItem();
				}
				}
				setState(410);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(416);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUSPLUS) {
				{
				setState(411);
				match(PLUSPLUS);
				setState(414);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case ESCAPED_IDENTIFIER:
				case IDENTIFIER:
					{
					setState(412);
					identifier();
					}
					break;
				case UNDERSCORE:
					{
					setState(413);
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
		enterRule(_localctx, 42, RULE_memberItem);
		try {
			setState(421);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(418);
				identifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(419);
				template();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(420);
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
		enterRule(_localctx, 44, RULE_whenClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(423);
			match(WHEN);
			setState(424);
			match(T__6);
			setState(430);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4034173033898835986L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 33285997055L) != 0)) {
				{
				{
				setState(425);
				expression(0);
				setState(426);
				match(T__1);
				}
				}
				setState(432);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(433);
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
		enterRule(_localctx, 46, RULE_whereClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(435);
			match(WHERE);
			setState(436);
			match(T__6);
			setState(442);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4034173033898835986L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 33285997055L) != 0)) {
				{
				{
				setState(437);
				expression(0);
				setState(438);
				match(T__1);
				}
				}
				setState(444);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(445);
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
		enterRule(_localctx, 48, RULE_queryDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(447);
			match(QUERY);
			setState(448);
			pathName();
			setState(449);
			match(T__3);
			setState(458);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(450);
				paramDecl();
				setState(455);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(451);
					match(T__4);
					setState(452);
					paramDecl();
					}
					}
					setState(457);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(460);
			match(T__5);
			setState(461);
			match(T__8);
			setState(462);
			typeExpression();
			setState(468);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(463);
				match(T__1);
				}
				break;
			case T__6:
				{
				setState(464);
				match(T__6);
				setState(465);
				expression(0);
				setState(466);
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
		enterRule(_localctx, 50, RULE_paramDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(470);
			identifier();
			setState(471);
			match(T__8);
			setState(472);
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
		enterRule(_localctx, 52, RULE_expressionEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(474);
			expression(0);
			setState(475);
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
		enterRule(_localctx, 54, RULE_completeOclDocumentEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(477);
			completeOclDocument();
			setState(478);
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
		enterRule(_localctx, 56, RULE_completeOclDocument);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(483);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10 || _la==T__11 || _la==IMPORT) {
				{
				{
				setState(480);
				importDeclaration();
				}
				}
				setState(485);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(490);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12 || _la==T__14) {
				{
				setState(488);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__12:
					{
					setState(486);
					packageDeclaration();
					}
					break;
				case T__14:
					{
					setState(487);
					contextDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(492);
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
		enterRule(_localctx, 58, RULE_importDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(493);
			_la = _input.LA(1);
			if ( !(_la==T__10 || _la==T__11 || _la==IMPORT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(497);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(494);
				identifier();
				setState(495);
				match(T__8);
				}
				break;
			}
			setState(499);
			pathName();
			setState(502);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLONCOLON) {
				{
				setState(500);
				match(COLONCOLON);
				setState(501);
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
		enterRule(_localctx, 60, RULE_packageDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(504);
			match(T__12);
			setState(505);
			pathName();
			setState(509);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(506);
				contextDeclaration();
				}
				}
				setState(511);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(512);
			match(T__13);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 62, RULE_contextDeclaration);
		try {
			setState(517);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(514);
				classifierContextDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(515);
				operationContextDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(516);
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
		enterRule(_localctx, 64, RULE_classifierContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(519);
			match(T__14);
			setState(523);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
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
			setState(527); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(526);
				classifierContextBody();
				}
				}
				setState(529); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 458752L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 66, RULE_classifierContextBody);
		try {
			setState(533);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__15:
				enterOuterAlt(_localctx, 1);
				{
				setState(531);
				invariantConstraint();
				}
				break;
			case T__16:
			case T__17:
				enterOuterAlt(_localctx, 2);
				{
				setState(532);
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
		enterRule(_localctx, 68, RULE_invariantConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(535);
			match(T__15);
			setState(543);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(536);
				identifier();
				setState(541);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(537);
					match(T__3);
					setState(538);
					expression(0);
					setState(539);
					match(T__5);
					}
				}

				}
			}

			setState(545);
			match(T__8);
			setState(546);
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
		enterRule(_localctx, 70, RULE_definitionConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(549);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__16) {
				{
				setState(548);
				match(T__16);
				}
			}

			setState(551);
			match(T__17);
			setState(553);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(552);
				identifier();
				}
			}

			setState(555);
			match(T__8);
			setState(573);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				{
				setState(556);
				identifier();
				setState(557);
				match(T__8);
				setState(558);
				typeExpression();
				setState(559);
				match(T__9);
				setState(560);
				expression(0);
				}
				break;
			case 2:
				{
				setState(562);
				identifier();
				setState(563);
				match(T__3);
				setState(565);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(564);
					parameterList();
					}
				}

				setState(567);
				match(T__5);
				setState(568);
				match(T__8);
				setState(569);
				typeExpression();
				setState(570);
				match(T__9);
				setState(571);
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
		enterRule(_localctx, 72, RULE_operationContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(575);
			match(T__14);
			setState(579);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				{
				setState(576);
				pathName();
				setState(577);
				match(COLONCOLON);
				}
				break;
			}
			setState(581);
			identifier();
			setState(582);
			match(T__3);
			setState(584);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(583);
				parameterList();
				}
			}

			setState(586);
			match(T__5);
			setState(589);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(587);
				match(T__8);
				setState(588);
				typeExpression();
				}
			}

			setState(592); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(591);
				operationContextBody();
				}
				}
				setState(594); 
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
		enterRule(_localctx, 74, RULE_operationContextBody);
		int _la;
		try {
			setState(614);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__18:
				_localctx = new PreConditionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(596);
				match(T__18);
				setState(598);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(597);
					identifier();
					}
				}

				setState(600);
				match(T__8);
				setState(601);
				expression(0);
				}
				break;
			case T__19:
				_localctx = new PostConditionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(602);
				match(T__19);
				setState(604);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(603);
					identifier();
					}
				}

				setState(606);
				match(T__8);
				setState(607);
				expression(0);
				}
				break;
			case T__20:
				_localctx = new BodyExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(608);
				match(T__20);
				setState(610);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(609);
					identifier();
					}
				}

				setState(612);
				match(T__8);
				setState(613);
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
		enterRule(_localctx, 76, RULE_propertyContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(616);
			match(T__14);
			setState(617);
			pathName();
			setState(618);
			match(COLONCOLON);
			setState(619);
			identifier();
			setState(620);
			match(T__8);
			setState(621);
			typeExpression();
			setState(623); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(622);
				propertyContextBody();
				}
				}
				setState(625); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__21 || _la==T__22 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 78, RULE_propertyContextBody);
		int _la;
		try {
			setState(639);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__21:
				_localctx = new InitExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(627);
				match(T__21);
				setState(629);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(628);
					identifier();
					}
				}

				setState(631);
				match(T__8);
				setState(632);
				expression(0);
				}
				break;
			case T__22:
				_localctx = new DeriveExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(633);
				match(T__22);
				setState(635);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(634);
					identifier();
					}
				}

				setState(637);
				match(T__8);
				setState(638);
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
		enterRule(_localctx, 80, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(641);
			parameter();
			setState(646);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(642);
				match(T__4);
				setState(643);
				parameter();
				}
				}
				setState(648);
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
		enterRule(_localctx, 82, RULE_parameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(649);
			identifier();
			setState(650);
			match(T__8);
			setState(651);
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
		int _startState = 84;
		enterRecursionRule(_localctx, 84, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(659);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__26:
				{
				_localctx = new NotExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(654);
				match(T__26);
				setState(655);
				expression(11);
				}
				break;
			case T__27:
				{
				_localctx = new UnaryMinusExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(656);
				match(T__27);
				setState(657);
				expression(10);
				}
				break;
			case T__0:
			case T__3:
			case T__39:
			case T__40:
			case T__45:
			case T__49:
			case T__50:
			case T__51:
			case T__52:
			case T__53:
			case T__54:
			case T__55:
			case T__56:
			case T__57:
			case T__59:
			case T__60:
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case T__70:
			case T__71:
			case REAL_LITERAL:
			case INTEGER_LITERAL:
			case STRING_LITERAL:
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				{
				_localctx = new PrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(658);
				primaryExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(693);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,78,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(691);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
					case 1:
						{
						_localctx = new MultExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(661);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(662);
						((MultExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__0 || _la==T__28) ) {
							((MultExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(663);
						expression(10);
						}
						break;
					case 2:
						{
						_localctx = new AddExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(664);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(665);
						((AddExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__27 || _la==T__29) ) {
							((AddExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(666);
						expression(9);
						}
						break;
					case 3:
						{
						_localctx = new CompareExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(667);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(668);
						((CompareExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 32212254720L) != 0)) ) {
							((CompareExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(669);
						expression(8);
						}
						break;
					case 4:
						{
						_localctx = new EqualityExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(670);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(671);
						((EqualityExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__9 || _la==T__34) ) {
							((EqualityExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(672);
						expression(7);
						}
						break;
					case 5:
						{
						_localctx = new AndExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(673);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(674);
						match(T__35);
						setState(675);
						expression(6);
						}
						break;
					case 6:
						{
						_localctx = new OrExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(676);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(677);
						match(T__36);
						setState(678);
						expression(5);
						}
						break;
					case 7:
						{
						_localctx = new XorExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(679);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(680);
						match(T__37);
						setState(681);
						expression(4);
						}
						break;
					case 8:
						{
						_localctx = new ImpliesExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(682);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(683);
						match(T__38);
						setState(684);
						expression(3);
						}
						break;
					case 9:
						{
						_localctx = new NavigationExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(685);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(686);
						_la = _input.LA(1);
						if ( !(_la==T__2 || _la==T__23) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(687);
						propertyOrCallSuffix();
						}
						break;
					case 10:
						{
						_localctx = new ArrowExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(688);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(689);
						_la = _input.LA(1);
						if ( !(_la==T__24 || _la==T__25) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(690);
						iteratorOrOperationCall();
						}
						break;
					}
					} 
				}
				setState(695);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,78,_ctx);
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
		enterRule(_localctx, 86, RULE_primaryExpression);
		int _la;
		try {
			setState(746);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				_localctx = new ParenExpContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(696);
				match(T__3);
				setState(697);
				expression(0);
				setState(698);
				match(T__5);
				}
				break;
			case 2:
				_localctx = new SelfExpContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(700);
				match(T__39);
				}
				break;
			case 3:
				_localctx = new IfExpContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(701);
				match(T__40);
				setState(702);
				((IfExpContext)_localctx).condition = expression(0);
				setState(703);
				match(T__41);
				setState(704);
				((IfExpContext)_localctx).thenExp = expression(0);
				setState(712);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__42) {
					{
					{
					setState(705);
					match(T__42);
					setState(706);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfCondition.add(((IfExpContext)_localctx).expression);
					setState(707);
					match(T__41);
					setState(708);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfExp.add(((IfExpContext)_localctx).expression);
					}
					}
					setState(714);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(715);
				match(T__43);
				setState(716);
				((IfExpContext)_localctx).elseExp = expression(0);
				setState(717);
				match(T__44);
				}
				break;
			case 4:
				_localctx = new LetExpContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(719);
				match(T__45);
				setState(720);
				letBinding();
				setState(725);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(721);
					match(T__4);
					setState(722);
					letBinding();
					}
					}
					setState(727);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(728);
				match(T__46);
				setState(729);
				expression(0);
				}
				break;
			case 5:
				_localctx = new LiteralExpContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(731);
				literalExpression();
				}
				break;
			case 6:
				_localctx = new OperationCallExpContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(732);
				pathName();
				setState(733);
				match(T__3);
				setState(735);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4034173033898835986L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 33285997055L) != 0)) {
					{
					setState(734);
					argumentList();
					}
				}

				setState(737);
				match(T__5);
				setState(739);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
				case 1:
					{
					setState(738);
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
				setState(741);
				primitiveType();
				}
				break;
			case 8:
				_localctx = new CollectionTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(742);
				collectionType();
				}
				break;
			case 9:
				_localctx = new MapTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(743);
				mapType();
				}
				break;
			case 10:
				_localctx = new TupleTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(744);
				tupleType();
				}
				break;
			case 11:
				_localctx = new PathNameExpContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(745);
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
		enterRule(_localctx, 88, RULE_propertyOrCallSuffix);
		int _la;
		try {
			setState(771);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				_localctx = new DotCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(751);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
				case 1:
					{
					setState(748);
					pathName();
					setState(749);
					match(COLONCOLON);
					}
					break;
				}
				setState(753);
				identifier();
				setState(754);
				match(T__3);
				setState(756);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4034173033898835986L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 33285997055L) != 0)) {
					{
					setState(755);
					argumentList();
					}
				}

				setState(758);
				match(T__5);
				setState(760);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
				case 1:
					{
					setState(759);
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
				setState(765);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
				case 1:
					{
					setState(762);
					pathName();
					setState(763);
					match(COLONCOLON);
					}
					break;
				}
				setState(767);
				identifier();
				setState(769);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
				case 1:
					{
					setState(768);
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
		enterRule(_localctx, 90, RULE_iteratorOrOperationCall);
		int _la;
		try {
			setState(806);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
			case 1:
				_localctx = new IteratorCallContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(773);
				identifier();
				setState(774);
				match(T__3);
				setState(775);
				iteratorVariables();
				setState(776);
				match(T__47);
				setState(777);
				expression(0);
				setState(778);
				match(T__5);
				}
				break;
			case 2:
				_localctx = new IterateCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(780);
				identifier();
				setState(781);
				match(T__3);
				setState(782);
				identifier();
				setState(785);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(783);
					match(T__8);
					setState(784);
					((IterateCallContext)_localctx).iterType = typeExpression();
					}
				}

				setState(787);
				match(T__1);
				setState(788);
				identifier();
				setState(791);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(789);
					match(T__8);
					setState(790);
					((IterateCallContext)_localctx).accType = typeExpression();
					}
				}

				setState(793);
				match(T__9);
				setState(794);
				expression(0);
				setState(795);
				match(T__47);
				setState(796);
				expression(0);
				setState(797);
				match(T__5);
				}
				break;
			case 3:
				_localctx = new CollectionOperationCallContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(799);
				identifier();
				setState(800);
				match(T__3);
				setState(802);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4034173033898835986L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 33285997055L) != 0)) {
					{
					setState(801);
					argumentList();
					}
				}

				setState(804);
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
		enterRule(_localctx, 92, RULE_iteratorVariables);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(808);
			identifier();
			setState(811);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(809);
				match(T__8);
				setState(810);
				typeExpression();
				}
			}

			setState(821);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(813);
				match(T__4);
				setState(814);
				identifier();
				setState(817);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(815);
					match(T__8);
					setState(816);
					typeExpression();
					}
				}

				}
				}
				setState(823);
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
		enterRule(_localctx, 94, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(824);
			expression(0);
			setState(829);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(825);
				match(T__4);
				setState(826);
				expression(0);
				}
				}
				setState(831);
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
		enterRule(_localctx, 96, RULE_letBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(832);
			identifier();
			setState(835);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(833);
				match(T__8);
				setState(834);
				typeExpression();
				}
			}

			setState(837);
			match(T__9);
			setState(838);
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
		enterRule(_localctx, 98, RULE_isMarkedPre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(840);
			match(T__48);
			setState(841);
			match(T__18);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		enterRule(_localctx, 100, RULE_literalExpression);
		try {
			setState(854);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER_LITERAL:
				_localctx = new IntegerLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(843);
				match(INTEGER_LITERAL);
				}
				break;
			case REAL_LITERAL:
				_localctx = new RealLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(844);
				match(REAL_LITERAL);
				}
				break;
			case STRING_LITERAL:
				_localctx = new StringLitContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(845);
				stringLiteral_();
				}
				break;
			case T__49:
				_localctx = new TrueLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(846);
				match(T__49);
				}
				break;
			case T__50:
				_localctx = new FalseLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(847);
				match(T__50);
				}
				break;
			case T__51:
				_localctx = new NullLiteralContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(848);
				match(T__51);
				}
				break;
			case T__52:
				_localctx = new InvalidLiteralContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(849);
				match(T__52);
				}
				break;
			case T__0:
				_localctx = new UnlimitedNaturalLiteralContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(850);
				match(T__0);
				}
				break;
			case T__53:
			case T__54:
			case T__55:
			case T__56:
			case T__57:
				_localctx = new CollectionLitContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(851);
				collectionLiteral();
				}
				break;
			case T__59:
				_localctx = new TupleLitContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(852);
				tupleLiteral();
				}
				break;
			case T__60:
				_localctx = new MapLitContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(853);
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
		enterRule(_localctx, 102, RULE_stringLiteral_);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(857); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(856);
					match(STRING_LITERAL);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(859); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,100,_ctx);
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
		enterRule(_localctx, 104, RULE_collectionLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(861);
			collectionKind();
			setState(862);
			match(T__6);
			setState(871);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4034173033898835986L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 33285997055L) != 0)) {
				{
				setState(863);
				collectionLiteralPart();
				setState(868);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(864);
					match(T__4);
					setState(865);
					collectionLiteralPart();
					}
					}
					setState(870);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(873);
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
		enterRule(_localctx, 106, RULE_collectionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(875);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 558446353793941504L) != 0)) ) {
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
		enterRule(_localctx, 108, RULE_collectionLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(877);
			expression(0);
			setState(880);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__58) {
				{
				setState(878);
				match(T__58);
				setState(879);
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
		enterRule(_localctx, 110, RULE_tupleLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(882);
			match(T__59);
			setState(883);
			match(T__6);
			setState(884);
			tupleLiteralPart();
			setState(889);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(885);
				match(T__4);
				setState(886);
				tupleLiteralPart();
				}
				}
				setState(891);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(892);
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
		enterRule(_localctx, 112, RULE_tupleLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(894);
			identifier();
			setState(897);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(895);
				match(T__8);
				setState(896);
				typeExpression();
				}
			}

			setState(899);
			match(T__9);
			setState(900);
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
		enterRule(_localctx, 114, RULE_mapLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(902);
			match(T__60);
			setState(903);
			match(T__6);
			setState(912);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4034173033898835986L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 33285997055L) != 0)) {
				{
				setState(904);
				mapLiteralPart();
				setState(909);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(905);
					match(T__4);
					setState(906);
					mapLiteralPart();
					}
					}
					setState(911);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
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
		enterRule(_localctx, 116, RULE_mapLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(916);
			expression(0);
			setState(917);
			_la = _input.LA(1);
			if ( !(_la==T__61 || _la==T__62) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(918);
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
		enterRule(_localctx, 118, RULE_typeExpression);
		try {
			setState(925);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case T__70:
			case T__71:
				enterOuterAlt(_localctx, 1);
				{
				setState(920);
				primitiveType();
				}
				break;
			case T__53:
			case T__54:
			case T__55:
			case T__56:
			case T__57:
				enterOuterAlt(_localctx, 2);
				{
				setState(921);
				collectionType();
				}
				break;
			case T__60:
				enterOuterAlt(_localctx, 3);
				{
				setState(922);
				mapType();
				}
				break;
			case T__59:
				enterOuterAlt(_localctx, 4);
				{
				setState(923);
				tupleType();
				}
				break;
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 5);
				{
				setState(924);
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
		enterRule(_localctx, 120, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(927);
			_la = _input.LA(1);
			if ( !(((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 511L) != 0)) ) {
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
		enterRule(_localctx, 122, RULE_collectionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(929);
			collectionKind();
			setState(930);
			match(T__3);
			setState(931);
			typeExpression();
			setState(932);
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
		enterRule(_localctx, 124, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(934);
			match(T__60);
			setState(935);
			match(T__3);
			setState(936);
			typeExpression();
			setState(937);
			match(T__4);
			setState(938);
			typeExpression();
			setState(939);
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
		enterRule(_localctx, 126, RULE_tupleType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(941);
			match(T__59);
			setState(942);
			match(T__3);
			setState(943);
			tupleTypePart();
			setState(948);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(944);
				match(T__4);
				setState(945);
				tupleTypePart();
				}
				}
				setState(950);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(951);
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
		enterRule(_localctx, 128, RULE_tupleTypePart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(953);
			identifier();
			setState(954);
			match(T__8);
			setState(955);
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
		enterRule(_localctx, 130, RULE_pathName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(957);
			identifier();
			setState(962);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,110,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(958);
					match(COLONCOLON);
					setState(959);
					identifier();
					}
					} 
				}
				setState(964);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,110,_ctx);
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
		enterRule(_localctx, 132, RULE_identifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(965);
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
		case 42:
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
		"\u0004\u0001e\u03c8\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"A\u0007A\u0002B\u0007B\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0005\u0001\u008b\b\u0001\n\u0001\f\u0001\u008e\t\u0001\u0001\u0001\u0005"+
		"\u0001\u0091\b\u0001\n\u0001\f\u0001\u0094\t\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0005\u0002\u009a\b\u0002\n\u0002\f\u0002\u009d"+
		"\t\u0002\u0001\u0002\u0001\u0002\u0003\u0002\u00a1\b\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003\u00a8\b\u0003"+
		"\n\u0003\f\u0003\u00ab\t\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u00b3\b\u0004\n\u0004\f\u0004"+
		"\u00b6\t\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004\u00bb\b"+
		"\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u00bf\b\u0004\n\u0004\f\u0004"+
		"\u00c2\t\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u00c6\b\u0004\n\u0004"+
		"\f\u0004\u00c9\t\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005"+
		"\u00d4\b\u0005\n\u0005\f\u0005\u00d7\t\u0005\u0001\u0005\u0001\u0005\u0003"+
		"\u0005\u00db\b\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u00e5\b\u0007\n"+
		"\u0007\f\u0007\u00e8\t\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003\b\u00f5"+
		"\b\b\u0001\t\u0003\t\u00f8\b\t\u0001\t\u0003\t\u00fb\b\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0003\t\u0101\b\t\u0001\t\u0001\t\u0005\t\u0105\b\t"+
		"\n\t\f\t\u0108\t\t\u0001\t\u0001\t\u0004\t\u010c\b\t\u000b\t\f\t\u010d"+
		"\u0001\t\u0003\t\u0111\b\t\u0001\t\u0003\t\u0114\b\t\u0001\t\u0001\t\u0001"+
		"\n\u0001\n\u0001\n\u0005\n\u011b\b\n\n\n\f\n\u011e\t\n\u0001\n\u0001\n"+
		"\u0001\n\u0001\n\u0003\n\u0124\b\n\u0001\n\u0001\n\u0001\u000b\u0003\u000b"+
		"\u0129\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0005\u000b\u0130\b\u000b\n\u000b\f\u000b\u0133\t\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u0139\b\u000b\u0001\u000b\u0001"+
		"\u000b\u0003\u000b\u013d\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0004"+
		"\u000b\u0142\b\u000b\u000b\u000b\f\u000b\u0143\u0001\u000b\u0001\u000b"+
		"\u0003\u000b\u0148\b\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0003\u000f"+
		"\u015c\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f"+
		"\u0162\b\u000f\u0001\u0010\u0003\u0010\u0165\b\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0003\u0010\u016b\b\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u0172\b\u0011\n"+
		"\u0011\f\u0011\u0175\t\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0184\b\u0012\u0001"+
		"\u0013\u0003\u0013\u0187\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u0190\b\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u0197"+
		"\b\u0014\n\u0014\f\u0014\u019a\t\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0003\u0014\u019f\b\u0014\u0003\u0014\u01a1\b\u0014\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0003\u0015\u01a6\b\u0015\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0005\u0016\u01ad\b\u0016\n\u0016\f\u0016"+
		"\u01b0\t\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0001\u0017\u0001\u0017\u0005\u0017\u01b9\b\u0017\n\u0017\f\u0017\u01bc"+
		"\t\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0005\u0018\u01c6\b\u0018\n\u0018\f\u0018"+
		"\u01c9\t\u0018\u0003\u0018\u01cb\b\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003"+
		"\u0018\u01d5\b\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001c\u0005\u001c\u01e2\b\u001c\n\u001c\f\u001c\u01e5\t\u001c\u0001\u001c"+
		"\u0001\u001c\u0005\u001c\u01e9\b\u001c\n\u001c\f\u001c\u01ec\t\u001c\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u01f2\b\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u01f7\b\u001d\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0005\u001e\u01fc\b\u001e\n\u001e\f\u001e\u01ff\t\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f"+
		"\u0206\b\u001f\u0001 \u0001 \u0001 \u0001 \u0003 \u020c\b \u0001 \u0001"+
		" \u0004 \u0210\b \u000b \f \u0211\u0001!\u0001!\u0003!\u0216\b!\u0001"+
		"\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0003\"\u021e\b\"\u0003\"\u0220"+
		"\b\"\u0001\"\u0001\"\u0001\"\u0001#\u0003#\u0226\b#\u0001#\u0001#\u0003"+
		"#\u022a\b#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001"+
		"#\u0001#\u0003#\u0236\b#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0003"+
		"#\u023e\b#\u0001$\u0001$\u0001$\u0001$\u0003$\u0244\b$\u0001$\u0001$\u0001"+
		"$\u0003$\u0249\b$\u0001$\u0001$\u0001$\u0003$\u024e\b$\u0001$\u0004$\u0251"+
		"\b$\u000b$\f$\u0252\u0001%\u0001%\u0003%\u0257\b%\u0001%\u0001%\u0001"+
		"%\u0001%\u0003%\u025d\b%\u0001%\u0001%\u0001%\u0001%\u0003%\u0263\b%\u0001"+
		"%\u0001%\u0003%\u0267\b%\u0001&\u0001&\u0001&\u0001&\u0001&\u0001&\u0001"+
		"&\u0004&\u0270\b&\u000b&\f&\u0271\u0001\'\u0001\'\u0003\'\u0276\b\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0003\'\u027c\b\'\u0001\'\u0001\'\u0003\'\u0280"+
		"\b\'\u0001(\u0001(\u0001(\u0005(\u0285\b(\n(\f(\u0288\t(\u0001)\u0001"+
		")\u0001)\u0001)\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0003*\u0294"+
		"\b*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001"+
		"*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001"+
		"*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001"+
		"*\u0005*\u02b4\b*\n*\f*\u02b7\t*\u0001+\u0001+\u0001+\u0001+\u0001+\u0001"+
		"+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0005+\u02c7"+
		"\b+\n+\f+\u02ca\t+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001"+
		"+\u0005+\u02d4\b+\n+\f+\u02d7\t+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001"+
		"+\u0001+\u0003+\u02e0\b+\u0001+\u0001+\u0003+\u02e4\b+\u0001+\u0001+\u0001"+
		"+\u0001+\u0001+\u0003+\u02eb\b+\u0001,\u0001,\u0001,\u0003,\u02f0\b,\u0001"+
		",\u0001,\u0001,\u0003,\u02f5\b,\u0001,\u0001,\u0003,\u02f9\b,\u0001,\u0001"+
		",\u0001,\u0003,\u02fe\b,\u0001,\u0001,\u0003,\u0302\b,\u0003,\u0304\b"+
		",\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001"+
		"-\u0001-\u0001-\u0003-\u0312\b-\u0001-\u0001-\u0001-\u0001-\u0003-\u0318"+
		"\b-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0003"+
		"-\u0323\b-\u0001-\u0001-\u0003-\u0327\b-\u0001.\u0001.\u0001.\u0003.\u032c"+
		"\b.\u0001.\u0001.\u0001.\u0001.\u0003.\u0332\b.\u0005.\u0334\b.\n.\f."+
		"\u0337\t.\u0001/\u0001/\u0001/\u0005/\u033c\b/\n/\f/\u033f\t/\u00010\u0001"+
		"0\u00010\u00030\u0344\b0\u00010\u00010\u00010\u00011\u00011\u00011\u0001"+
		"2\u00012\u00012\u00012\u00012\u00012\u00012\u00012\u00012\u00012\u0001"+
		"2\u00032\u0357\b2\u00013\u00043\u035a\b3\u000b3\f3\u035b\u00014\u0001"+
		"4\u00014\u00014\u00014\u00054\u0363\b4\n4\f4\u0366\t4\u00034\u0368\b4"+
		"\u00014\u00014\u00015\u00015\u00016\u00016\u00016\u00036\u0371\b6\u0001"+
		"7\u00017\u00017\u00017\u00017\u00057\u0378\b7\n7\f7\u037b\t7\u00017\u0001"+
		"7\u00018\u00018\u00018\u00038\u0382\b8\u00018\u00018\u00018\u00019\u0001"+
		"9\u00019\u00019\u00019\u00059\u038c\b9\n9\f9\u038f\t9\u00039\u0391\b9"+
		"\u00019\u00019\u0001:\u0001:\u0001:\u0001:\u0001;\u0001;\u0001;\u0001"+
		";\u0001;\u0003;\u039e\b;\u0001<\u0001<\u0001=\u0001=\u0001=\u0001=\u0001"+
		"=\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001?\u0001?\u0001"+
		"?\u0001?\u0001?\u0005?\u03b3\b?\n?\f?\u03b6\t?\u0001?\u0001?\u0001@\u0001"+
		"@\u0001@\u0001@\u0001A\u0001A\u0001A\u0005A\u03c1\bA\nA\fA\u03c4\tA\u0001"+
		"B\u0001B\u0001B\u0000\u0001TC\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPR"+
		"TVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0000\f\u0002\u0000IIKK\u0002"+
		"\u0000\u000b\fNN\u0002\u0000\u0001\u0001\u001d\u001d\u0002\u0000\u001c"+
		"\u001c\u001e\u001e\u0001\u0000\u001f\"\u0002\u0000\n\n##\u0002\u0000\u0003"+
		"\u0003\u0018\u0018\u0001\u0000\u0019\u001a\u0001\u00006:\u0001\u0000>"+
		"?\u0001\u0000@H\u0001\u0000ab\u0415\u0000\u0086\u0001\u0000\u0000\u0000"+
		"\u0002\u008c\u0001\u0000\u0000\u0000\u0004\u0095\u0001\u0000\u0000\u0000"+
		"\u0006\u00a4\u0001\u0000\u0000\u0000\b\u00ac\u0001\u0000\u0000\u0000\n"+
		"\u00cc\u0001\u0000\u0000\u0000\f\u00dc\u0001\u0000\u0000\u0000\u000e\u00de"+
		"\u0001\u0000\u0000\u0000\u0010\u00f4\u0001\u0000\u0000\u0000\u0012\u00f7"+
		"\u0001\u0000\u0000\u0000\u0014\u0117\u0001\u0000\u0000\u0000\u0016\u0128"+
		"\u0001\u0000\u0000\u0000\u0018\u014b\u0001\u0000\u0000\u0000\u001a\u0152"+
		"\u0001\u0000\u0000\u0000\u001c\u0154\u0001\u0000\u0000\u0000\u001e\u015b"+
		"\u0001\u0000\u0000\u0000 \u0164\u0001\u0000\u0000\u0000\"\u016e\u0001"+
		"\u0000\u0000\u0000$\u0183\u0001\u0000\u0000\u0000&\u0186\u0001\u0000\u0000"+
		"\u0000(\u0193\u0001\u0000\u0000\u0000*\u01a5\u0001\u0000\u0000\u0000,"+
		"\u01a7\u0001\u0000\u0000\u0000.\u01b3\u0001\u0000\u0000\u00000\u01bf\u0001"+
		"\u0000\u0000\u00002\u01d6\u0001\u0000\u0000\u00004\u01da\u0001\u0000\u0000"+
		"\u00006\u01dd\u0001\u0000\u0000\u00008\u01e3\u0001\u0000\u0000\u0000:"+
		"\u01ed\u0001\u0000\u0000\u0000<\u01f8\u0001\u0000\u0000\u0000>\u0205\u0001"+
		"\u0000\u0000\u0000@\u0207\u0001\u0000\u0000\u0000B\u0215\u0001\u0000\u0000"+
		"\u0000D\u0217\u0001\u0000\u0000\u0000F\u0225\u0001\u0000\u0000\u0000H"+
		"\u023f\u0001\u0000\u0000\u0000J\u0266\u0001\u0000\u0000\u0000L\u0268\u0001"+
		"\u0000\u0000\u0000N\u027f\u0001\u0000\u0000\u0000P\u0281\u0001\u0000\u0000"+
		"\u0000R\u0289\u0001\u0000\u0000\u0000T\u0293\u0001\u0000\u0000\u0000V"+
		"\u02ea\u0001\u0000\u0000\u0000X\u0303\u0001\u0000\u0000\u0000Z\u0326\u0001"+
		"\u0000\u0000\u0000\\\u0328\u0001\u0000\u0000\u0000^\u0338\u0001\u0000"+
		"\u0000\u0000`\u0340\u0001\u0000\u0000\u0000b\u0348\u0001\u0000\u0000\u0000"+
		"d\u0356\u0001\u0000\u0000\u0000f\u0359\u0001\u0000\u0000\u0000h\u035d"+
		"\u0001\u0000\u0000\u0000j\u036b\u0001\u0000\u0000\u0000l\u036d\u0001\u0000"+
		"\u0000\u0000n\u0372\u0001\u0000\u0000\u0000p\u037e\u0001\u0000\u0000\u0000"+
		"r\u0386\u0001\u0000\u0000\u0000t\u0394\u0001\u0000\u0000\u0000v\u039d"+
		"\u0001\u0000\u0000\u0000x\u039f\u0001\u0000\u0000\u0000z\u03a1\u0001\u0000"+
		"\u0000\u0000|\u03a6\u0001\u0000\u0000\u0000~\u03ad\u0001\u0000\u0000\u0000"+
		"\u0080\u03b9\u0001\u0000\u0000\u0000\u0082\u03bd\u0001\u0000\u0000\u0000"+
		"\u0084\u03c5\u0001\u0000\u0000\u0000\u0086\u0087\u0003\u0002\u0001\u0000"+
		"\u0087\u0088\u0005\u0000\u0000\u0001\u0088\u0001\u0001\u0000\u0000\u0000"+
		"\u0089\u008b\u0003\u0004\u0002\u0000\u008a\u0089\u0001\u0000\u0000\u0000"+
		"\u008b\u008e\u0001\u0000\u0000\u0000\u008c\u008a\u0001\u0000\u0000\u0000"+
		"\u008c\u008d\u0001\u0000\u0000\u0000\u008d\u0092\u0001\u0000\u0000\u0000"+
		"\u008e\u008c\u0001\u0000\u0000\u0000\u008f\u0091\u0003\b\u0004\u0000\u0090"+
		"\u008f\u0001\u0000\u0000\u0000\u0091\u0094\u0001\u0000\u0000\u0000\u0092"+
		"\u0090\u0001\u0000\u0000\u0000\u0092\u0093\u0001\u0000\u0000\u0000\u0093"+
		"\u0003\u0001\u0000\u0000\u0000\u0094\u0092\u0001\u0000\u0000\u0000\u0095"+
		"\u0096\u0005N\u0000\u0000\u0096\u009b\u0003\u0006\u0003\u0000\u0097\u0098"+
		"\u0005\\\u0000\u0000\u0098\u009a\u0003\u0006\u0003\u0000\u0099\u0097\u0001"+
		"\u0000\u0000\u0000\u009a\u009d\u0001\u0000\u0000\u0000\u009b\u0099\u0001"+
		"\u0000\u0000\u0000\u009b\u009c\u0001\u0000\u0000\u0000\u009c\u00a0\u0001"+
		"\u0000\u0000\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009e\u009f\u0005"+
		"\\\u0000\u0000\u009f\u00a1\u0005\u0001\u0000\u0000\u00a0\u009e\u0001\u0000"+
		"\u0000\u0000\u00a0\u00a1\u0001\u0000\u0000\u0000\u00a1\u00a2\u0001\u0000"+
		"\u0000\u0000\u00a2\u00a3\u0005\u0002\u0000\u0000\u00a3\u0005\u0001\u0000"+
		"\u0000\u0000\u00a4\u00a9\u0003\u0084B\u0000\u00a5\u00a6\u0005\u0003\u0000"+
		"\u0000\u00a6\u00a8\u0003\u0084B\u0000\u00a7\u00a5\u0001\u0000\u0000\u0000"+
		"\u00a8\u00ab\u0001\u0000\u0000\u0000\u00a9\u00a7\u0001\u0000\u0000\u0000"+
		"\u00a9\u00aa\u0001\u0000\u0000\u0000\u00aa\u0007\u0001\u0000\u0000\u0000"+
		"\u00ab\u00a9\u0001\u0000\u0000\u0000\u00ac\u00ad\u0005U\u0000\u0000\u00ad"+
		"\u00ae\u0003\u0084B\u0000\u00ae\u00af\u0005\u0004\u0000\u0000\u00af\u00b4"+
		"\u0003\n\u0005\u0000\u00b0\u00b1\u0005\u0005\u0000\u0000\u00b1\u00b3\u0003"+
		"\n\u0005\u0000\u00b2\u00b0\u0001\u0000\u0000\u0000\u00b3\u00b6\u0001\u0000"+
		"\u0000\u0000\u00b4\u00b2\u0001\u0000\u0000\u0000\u00b4\u00b5\u0001\u0000"+
		"\u0000\u0000\u00b5\u00b7\u0001\u0000\u0000\u0000\u00b6\u00b4\u0001\u0000"+
		"\u0000\u0000\u00b7\u00ba\u0005\u0006\u0000\u0000\u00b8\u00b9\u0005L\u0000"+
		"\u0000\u00b9\u00bb\u0003\u0084B\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000"+
		"\u00ba\u00bb\u0001\u0000\u0000\u0000\u00bb\u00bc\u0001\u0000\u0000\u0000"+
		"\u00bc\u00c0\u0005\u0007\u0000\u0000\u00bd\u00bf\u0003\u000e\u0007\u0000"+
		"\u00be\u00bd\u0001\u0000\u0000\u0000\u00bf\u00c2\u0001\u0000\u0000\u0000"+
		"\u00c0\u00be\u0001\u0000\u0000\u0000\u00c0\u00c1\u0001\u0000\u0000\u0000"+
		"\u00c1\u00c7\u0001\u0000\u0000\u0000\u00c2\u00c0\u0001\u0000\u0000\u0000"+
		"\u00c3\u00c6\u0003\u0012\t\u0000\u00c4\u00c6\u00030\u0018\u0000\u00c5"+
		"\u00c3\u0001\u0000\u0000\u0000\u00c5\u00c4\u0001\u0000\u0000\u0000\u00c6"+
		"\u00c9\u0001\u0000\u0000\u0000\u00c7\u00c5\u0001\u0000\u0000\u0000\u00c7"+
		"\u00c8\u0001\u0000\u0000\u0000\u00c8\u00ca\u0001\u0000\u0000\u0000\u00c9"+
		"\u00c7\u0001\u0000\u0000\u0000\u00ca\u00cb\u0005\b\u0000\u0000\u00cb\t"+
		"\u0001\u0000\u0000\u0000\u00cc\u00cd\u0003\u0084B\u0000\u00cd\u00da\u0005"+
		"\t\u0000\u0000\u00ce\u00db\u0003\f\u0006\u0000\u00cf\u00d0\u0005\u0007"+
		"\u0000\u0000\u00d0\u00d5\u0003\f\u0006\u0000\u00d1\u00d2\u0005\u0005\u0000"+
		"\u0000\u00d2\u00d4\u0003\f\u0006\u0000\u00d3\u00d1\u0001\u0000\u0000\u0000"+
		"\u00d4\u00d7\u0001\u0000\u0000\u0000\u00d5\u00d3\u0001\u0000\u0000\u0000"+
		"\u00d5\u00d6\u0001\u0000\u0000\u0000\u00d6\u00d8\u0001\u0000\u0000\u0000"+
		"\u00d7\u00d5\u0001\u0000\u0000\u0000\u00d8\u00d9\u0005\b\u0000\u0000\u00d9"+
		"\u00db\u0001\u0000\u0000\u0000\u00da\u00ce\u0001\u0000\u0000\u0000\u00da"+
		"\u00cf\u0001\u0000\u0000\u0000\u00db\u000b\u0001\u0000\u0000\u0000\u00dc"+
		"\u00dd\u0003\u0082A\u0000\u00dd\r\u0001\u0000\u0000\u0000\u00de\u00df"+
		"\u0005O\u0000\u0000\u00df\u00e0\u0003\u0082A\u0000\u00e0\u00e1\u0005\u0007"+
		"\u0000\u0000\u00e1\u00e6\u0003\u0010\b\u0000\u00e2\u00e3\u0005\u0005\u0000"+
		"\u0000\u00e3\u00e5\u0003\u0010\b\u0000\u00e4\u00e2\u0001\u0000\u0000\u0000"+
		"\u00e5\u00e8\u0001\u0000\u0000\u0000\u00e6\u00e4\u0001\u0000\u0000\u0000"+
		"\u00e6\u00e7\u0001\u0000\u0000\u0000\u00e7\u00e9\u0001\u0000\u0000\u0000"+
		"\u00e8\u00e6\u0001\u0000\u0000\u0000\u00e9\u00ea\u0005\b\u0000\u0000\u00ea"+
		"\u00eb\u0005\u0002\u0000\u0000\u00eb\u000f\u0001\u0000\u0000\u0000\u00ec"+
		"\u00f5\u0003\u0084B\u0000\u00ed\u00ee\u0005Z\u0000\u0000\u00ee\u00ef\u0005"+
		"\u0004\u0000\u0000\u00ef\u00f0\u0003\u0082A\u0000\u00f0\u00f1\u0005\\"+
		"\u0000\u0000\u00f1\u00f2\u0003\u0084B\u0000\u00f2\u00f3\u0005\u0006\u0000"+
		"\u0000\u00f3\u00f5\u0001\u0000\u0000\u0000\u00f4\u00ec\u0001\u0000\u0000"+
		"\u0000\u00f4\u00ed\u0001\u0000\u0000\u0000\u00f5\u0011\u0001\u0000\u0000"+
		"\u0000\u00f6\u00f8\u0005T\u0000\u0000\u00f7\u00f6\u0001\u0000\u0000\u0000"+
		"\u00f7\u00f8\u0001\u0000\u0000\u0000\u00f8\u00fa\u0001\u0000\u0000\u0000"+
		"\u00f9\u00fb\u0005Y\u0000\u0000\u00fa\u00f9\u0001\u0000\u0000\u0000\u00fa"+
		"\u00fb\u0001\u0000\u0000\u0000\u00fb\u00fc\u0001\u0000\u0000\u0000\u00fc"+
		"\u00fd\u0005S\u0000\u0000\u00fd\u0100\u0003\u0084B\u0000\u00fe\u00ff\u0005"+
		"P\u0000\u0000\u00ff\u0101\u0003\u0084B\u0000\u0100\u00fe\u0001\u0000\u0000"+
		"\u0000\u0100\u0101\u0001\u0000\u0000\u0000\u0101\u0102\u0001\u0000\u0000"+
		"\u0000\u0102\u0106\u0005\u0007\u0000\u0000\u0103\u0105\u0003\u0014\n\u0000"+
		"\u0104\u0103\u0001\u0000\u0000\u0000\u0105\u0108\u0001\u0000\u0000\u0000"+
		"\u0106\u0104\u0001\u0000\u0000\u0000\u0106\u0107\u0001\u0000\u0000\u0000"+
		"\u0107\u010b\u0001\u0000\u0000\u0000\u0108\u0106\u0001\u0000\u0000\u0000"+
		"\u0109\u010c\u0003\u0016\u000b\u0000\u010a\u010c\u0003\u0018\f\u0000\u010b"+
		"\u0109\u0001\u0000\u0000\u0000\u010b\u010a\u0001\u0000\u0000\u0000\u010c"+
		"\u010d\u0001\u0000\u0000\u0000\u010d\u010b\u0001\u0000\u0000\u0000\u010d"+
		"\u010e\u0001\u0000\u0000\u0000\u010e\u0110\u0001\u0000\u0000\u0000\u010f"+
		"\u0111\u0003,\u0016\u0000\u0110\u010f\u0001\u0000\u0000\u0000\u0110\u0111"+
		"\u0001\u0000\u0000\u0000\u0111\u0113\u0001\u0000\u0000\u0000\u0112\u0114"+
		"\u0003.\u0017\u0000\u0113\u0112\u0001\u0000\u0000\u0000\u0113\u0114\u0001"+
		"\u0000\u0000\u0000\u0114\u0115\u0001\u0000\u0000\u0000\u0115\u0116\u0005"+
		"\b\u0000\u0000\u0116\u0013\u0001\u0000\u0000\u0000\u0117\u011c\u0003\u0084"+
		"B\u0000\u0118\u0119\u0005\u0005\u0000\u0000\u0119\u011b\u0003\u0084B\u0000"+
		"\u011a\u0118\u0001\u0000\u0000\u0000\u011b\u011e\u0001\u0000\u0000\u0000"+
		"\u011c\u011a\u0001\u0000\u0000\u0000\u011c\u011d\u0001\u0000\u0000\u0000"+
		"\u011d\u011f\u0001\u0000\u0000\u0000\u011e\u011c\u0001\u0000\u0000\u0000"+
		"\u011f\u0120\u0005\t\u0000\u0000\u0120\u0123\u0003v;\u0000\u0121\u0122"+
		"\u0005\n\u0000\u0000\u0122\u0124\u0003T*\u0000\u0123\u0121\u0001\u0000"+
		"\u0000\u0000\u0123\u0124\u0001\u0000\u0000\u0000\u0124\u0125\u0001\u0000"+
		"\u0000\u0000\u0125\u0126\u0005\u0002\u0000\u0000\u0126\u0015\u0001\u0000"+
		"\u0000\u0000\u0127\u0129\u0003\u001a\r\u0000\u0128\u0127\u0001\u0000\u0000"+
		"\u0000\u0128\u0129\u0001\u0000\u0000\u0000\u0129\u012a\u0001\u0000\u0000"+
		"\u0000\u012a\u012b\u0005J\u0000\u0000\u012b\u012c\u0003\u0084B\u0000\u012c"+
		"\u0131\u0003\u001e\u000f\u0000\u012d\u012e\u0005\u0005\u0000\u0000\u012e"+
		"\u0130\u0003\u001e\u000f\u0000\u012f\u012d\u0001\u0000\u0000\u0000\u0130"+
		"\u0133\u0001\u0000\u0000\u0000\u0131\u012f\u0001\u0000\u0000\u0000\u0131"+
		"\u0132\u0001\u0000\u0000\u0000\u0132\u013c\u0001\u0000\u0000\u0000\u0133"+
		"\u0131\u0001\u0000\u0000\u0000\u0134\u0135\u0005M\u0000\u0000\u0135\u0136"+
		"\u0003\u0082A\u0000\u0136\u0138\u0005\u0004\u0000\u0000\u0137\u0139\u0003"+
		"^/\u0000\u0138\u0137\u0001\u0000\u0000\u0000\u0138\u0139\u0001\u0000\u0000"+
		"\u0000\u0139\u013a\u0001\u0000\u0000\u0000\u013a\u013b\u0005\u0006\u0000"+
		"\u0000\u013b\u013d\u0001\u0000\u0000\u0000\u013c\u0134\u0001\u0000\u0000"+
		"\u0000\u013c\u013d\u0001\u0000\u0000\u0000\u013d\u0147\u0001\u0000\u0000"+
		"\u0000\u013e\u013f\u0005X\u0000\u0000\u013f\u0141\u0005\u0007\u0000\u0000"+
		"\u0140\u0142\u0003\u001c\u000e\u0000\u0141\u0140\u0001\u0000\u0000\u0000"+
		"\u0142\u0143\u0001\u0000\u0000\u0000\u0143\u0141\u0001\u0000\u0000\u0000"+
		"\u0143\u0144\u0001\u0000\u0000\u0000\u0144\u0145\u0001\u0000\u0000\u0000"+
		"\u0145\u0146\u0005\b\u0000\u0000\u0146\u0148\u0001\u0000\u0000\u0000\u0147"+
		"\u013e\u0001\u0000\u0000\u0000\u0147\u0148\u0001\u0000\u0000\u0000\u0148"+
		"\u0149\u0001\u0000\u0000\u0000\u0149\u014a\u0005\u0002\u0000\u0000\u014a"+
		"\u0017\u0001\u0000\u0000\u0000\u014b\u014c\u0005Q\u0000\u0000\u014c\u014d"+
		"\u0005J\u0000\u0000\u014d\u014e\u0003\u0084B\u0000\u014e\u014f\u0005\t"+
		"\u0000\u0000\u014f\u0150\u0003v;\u0000\u0150\u0151\u0005\u0002\u0000\u0000"+
		"\u0151\u0019\u0001\u0000\u0000\u0000\u0152\u0153\u0007\u0000\u0000\u0000"+
		"\u0153\u001b\u0001\u0000\u0000\u0000\u0154\u0155\u0003\u0084B\u0000\u0155"+
		"\u0156\u0005\n\u0000\u0000\u0156\u0157\u0003T*\u0000\u0157\u0158\u0005"+
		"\u0002\u0000\u0000\u0158\u001d\u0001\u0000\u0000\u0000\u0159\u015c\u0003"+
		" \u0010\u0000\u015a\u015c\u0003&\u0013\u0000\u015b\u0159\u0001\u0000\u0000"+
		"\u0000\u015b\u015a\u0001\u0000\u0000\u0000\u015c\u0161\u0001\u0000\u0000"+
		"\u0000\u015d\u015e\u0005\u0007\u0000\u0000\u015e\u015f\u0003T*\u0000\u015f"+
		"\u0160\u0005\b\u0000\u0000\u0160\u0162\u0001\u0000\u0000\u0000\u0161\u015d"+
		"\u0001\u0000\u0000\u0000\u0161\u0162\u0001\u0000\u0000\u0000\u0162\u001f"+
		"\u0001\u0000\u0000\u0000\u0163\u0165\u0003\u0084B\u0000\u0164\u0163\u0001"+
		"\u0000\u0000\u0000\u0164\u0165\u0001\u0000\u0000\u0000\u0165\u0166\u0001"+
		"\u0000\u0000\u0000\u0166\u0167\u0005\t\u0000\u0000\u0167\u0168\u0003\u0082"+
		"A\u0000\u0168\u016a\u0005\u0007\u0000\u0000\u0169\u016b\u0003\"\u0011"+
		"\u0000\u016a\u0169\u0001\u0000\u0000\u0000\u016a\u016b\u0001\u0000\u0000"+
		"\u0000\u016b\u016c\u0001\u0000\u0000\u0000\u016c\u016d\u0005\b\u0000\u0000"+
		"\u016d!\u0001\u0000\u0000\u0000\u016e\u0173\u0003$\u0012\u0000\u016f\u0170"+
		"\u0005\u0005\u0000\u0000\u0170\u0172\u0003$\u0012\u0000\u0171\u016f\u0001"+
		"\u0000\u0000\u0000\u0172\u0175\u0001\u0000\u0000\u0000\u0173\u0171\u0001"+
		"\u0000\u0000\u0000\u0173\u0174\u0001\u0000\u0000\u0000\u0174#\u0001\u0000"+
		"\u0000\u0000\u0175\u0173\u0001\u0000\u0000\u0000\u0176\u0177\u0003\u0084"+
		"B\u0000\u0177\u0178\u0005\n\u0000\u0000\u0178\u0179\u0003T*\u0000\u0179"+
		"\u0184\u0001\u0000\u0000\u0000\u017a\u017b\u0005Z\u0000\u0000\u017b\u017c"+
		"\u0005\u0004\u0000\u0000\u017c\u017d\u0003\u0082A\u0000\u017d\u017e\u0005"+
		"\\\u0000\u0000\u017e\u017f\u0003\u0084B\u0000\u017f\u0180\u0005\u0006"+
		"\u0000\u0000\u0180\u0181\u0005\n\u0000\u0000\u0181\u0182\u0003T*\u0000"+
		"\u0182\u0184\u0001\u0000\u0000\u0000\u0183\u0176\u0001\u0000\u0000\u0000"+
		"\u0183\u017a\u0001\u0000\u0000\u0000\u0184%\u0001\u0000\u0000\u0000\u0185"+
		"\u0187\u0003\u0084B\u0000\u0186\u0185\u0001\u0000\u0000\u0000\u0186\u0187"+
		"\u0001\u0000\u0000\u0000\u0187\u0188\u0001\u0000\u0000\u0000\u0188\u0189"+
		"\u0005\t\u0000\u0000\u0189\u018a\u0003j5\u0000\u018a\u018b\u0005\u0004"+
		"\u0000\u0000\u018b\u018c\u0003v;\u0000\u018c\u018d\u0005\u0006\u0000\u0000"+
		"\u018d\u018f\u0005\u0007\u0000\u0000\u018e\u0190\u0003(\u0014\u0000\u018f"+
		"\u018e\u0001\u0000\u0000\u0000\u018f\u0190\u0001\u0000\u0000\u0000\u0190"+
		"\u0191\u0001\u0000\u0000\u0000\u0191\u0192\u0005\b\u0000\u0000\u0192\'"+
		"\u0001\u0000\u0000\u0000\u0193\u0198\u0003*\u0015\u0000\u0194\u0195\u0005"+
		"\u0005\u0000\u0000\u0195\u0197\u0003*\u0015\u0000\u0196\u0194\u0001\u0000"+
		"\u0000\u0000\u0197\u019a\u0001\u0000\u0000\u0000\u0198\u0196\u0001\u0000"+
		"\u0000\u0000\u0198\u0199\u0001\u0000\u0000\u0000\u0199\u01a0\u0001\u0000"+
		"\u0000\u0000\u019a\u0198\u0001\u0000\u0000\u0000\u019b\u019e\u0005[\u0000"+
		"\u0000\u019c\u019f\u0003\u0084B\u0000\u019d\u019f\u0005]\u0000\u0000\u019e"+
		"\u019c\u0001\u0000\u0000\u0000\u019e\u019d\u0001\u0000\u0000\u0000\u019f"+
		"\u01a1\u0001\u0000\u0000\u0000\u01a0\u019b\u0001\u0000\u0000\u0000\u01a0"+
		"\u01a1\u0001\u0000\u0000\u0000\u01a1)\u0001\u0000\u0000\u0000\u01a2\u01a6"+
		"\u0003\u0084B\u0000\u01a3\u01a6\u0003\u001e\u000f\u0000\u01a4\u01a6\u0005"+
		"]\u0000\u0000\u01a5\u01a2\u0001\u0000\u0000\u0000\u01a5\u01a3\u0001\u0000"+
		"\u0000\u0000\u01a5\u01a4\u0001\u0000\u0000\u0000\u01a6+\u0001\u0000\u0000"+
		"\u0000\u01a7\u01a8\u0005V\u0000\u0000\u01a8\u01ae\u0005\u0007\u0000\u0000"+
		"\u01a9\u01aa\u0003T*\u0000\u01aa\u01ab\u0005\u0002\u0000\u0000\u01ab\u01ad"+
		"\u0001\u0000\u0000\u0000\u01ac\u01a9\u0001\u0000\u0000\u0000\u01ad\u01b0"+
		"\u0001\u0000\u0000\u0000\u01ae\u01ac\u0001\u0000\u0000\u0000\u01ae\u01af"+
		"\u0001\u0000\u0000\u0000\u01af\u01b1\u0001\u0000\u0000\u0000\u01b0\u01ae"+
		"\u0001\u0000\u0000\u0000\u01b1\u01b2\u0005\b\u0000\u0000\u01b2-\u0001"+
		"\u0000\u0000\u0000\u01b3\u01b4\u0005W\u0000\u0000\u01b4\u01ba\u0005\u0007"+
		"\u0000\u0000\u01b5\u01b6\u0003T*\u0000\u01b6\u01b7\u0005\u0002\u0000\u0000"+
		"\u01b7\u01b9\u0001\u0000\u0000\u0000\u01b8\u01b5\u0001\u0000\u0000\u0000"+
		"\u01b9\u01bc\u0001\u0000\u0000\u0000\u01ba\u01b8\u0001\u0000\u0000\u0000"+
		"\u01ba\u01bb\u0001\u0000\u0000\u0000\u01bb\u01bd\u0001\u0000\u0000\u0000"+
		"\u01bc\u01ba\u0001\u0000\u0000\u0000\u01bd\u01be\u0005\b\u0000\u0000\u01be"+
		"/\u0001\u0000\u0000\u0000\u01bf\u01c0\u0005R\u0000\u0000\u01c0\u01c1\u0003"+
		"\u0082A\u0000\u01c1\u01ca\u0005\u0004\u0000\u0000\u01c2\u01c7\u00032\u0019"+
		"\u0000\u01c3\u01c4\u0005\u0005\u0000\u0000\u01c4\u01c6\u00032\u0019\u0000"+
		"\u01c5\u01c3\u0001\u0000\u0000\u0000\u01c6\u01c9\u0001\u0000\u0000\u0000"+
		"\u01c7\u01c5\u0001\u0000\u0000\u0000\u01c7\u01c8\u0001\u0000\u0000\u0000"+
		"\u01c8\u01cb\u0001\u0000\u0000\u0000\u01c9\u01c7\u0001\u0000\u0000\u0000"+
		"\u01ca\u01c2\u0001\u0000\u0000\u0000\u01ca\u01cb\u0001\u0000\u0000\u0000"+
		"\u01cb\u01cc\u0001\u0000\u0000\u0000\u01cc\u01cd\u0005\u0006\u0000\u0000"+
		"\u01cd\u01ce\u0005\t\u0000\u0000\u01ce\u01d4\u0003v;\u0000\u01cf\u01d5"+
		"\u0005\u0002\u0000\u0000\u01d0\u01d1\u0005\u0007\u0000\u0000\u01d1\u01d2"+
		"\u0003T*\u0000\u01d2\u01d3\u0005\b\u0000\u0000\u01d3\u01d5\u0001\u0000"+
		"\u0000\u0000\u01d4\u01cf\u0001\u0000\u0000\u0000\u01d4\u01d0\u0001\u0000"+
		"\u0000\u0000\u01d51\u0001\u0000\u0000\u0000\u01d6\u01d7\u0003\u0084B\u0000"+
		"\u01d7\u01d8\u0005\t\u0000\u0000\u01d8\u01d9\u0003v;\u0000\u01d93\u0001"+
		"\u0000\u0000\u0000\u01da\u01db\u0003T*\u0000\u01db\u01dc\u0005\u0000\u0000"+
		"\u0001\u01dc5\u0001\u0000\u0000\u0000\u01dd\u01de\u00038\u001c\u0000\u01de"+
		"\u01df\u0005\u0000\u0000\u0001\u01df7\u0001\u0000\u0000\u0000\u01e0\u01e2"+
		"\u0003:\u001d\u0000\u01e1\u01e0\u0001\u0000\u0000\u0000\u01e2\u01e5\u0001"+
		"\u0000\u0000\u0000\u01e3\u01e1\u0001\u0000\u0000\u0000\u01e3\u01e4\u0001"+
		"\u0000\u0000\u0000\u01e4\u01ea\u0001\u0000\u0000\u0000\u01e5\u01e3\u0001"+
		"\u0000\u0000\u0000\u01e6\u01e9\u0003<\u001e\u0000\u01e7\u01e9\u0003>\u001f"+
		"\u0000\u01e8\u01e6\u0001\u0000\u0000\u0000\u01e8\u01e7\u0001\u0000\u0000"+
		"\u0000\u01e9\u01ec\u0001\u0000\u0000\u0000\u01ea\u01e8\u0001\u0000\u0000"+
		"\u0000\u01ea\u01eb\u0001\u0000\u0000\u0000\u01eb9\u0001\u0000\u0000\u0000"+
		"\u01ec\u01ea\u0001\u0000\u0000\u0000\u01ed\u01f1\u0007\u0001\u0000\u0000"+
		"\u01ee\u01ef\u0003\u0084B\u0000\u01ef\u01f0\u0005\t\u0000\u0000\u01f0"+
		"\u01f2\u0001\u0000\u0000\u0000\u01f1\u01ee\u0001\u0000\u0000\u0000\u01f1"+
		"\u01f2\u0001\u0000\u0000\u0000\u01f2\u01f3\u0001\u0000\u0000\u0000\u01f3"+
		"\u01f6\u0003\u0082A\u0000\u01f4\u01f5\u0005\\\u0000\u0000\u01f5\u01f7"+
		"\u0005\u0001\u0000\u0000\u01f6\u01f4\u0001\u0000\u0000\u0000\u01f6\u01f7"+
		"\u0001\u0000\u0000\u0000\u01f7;\u0001\u0000\u0000\u0000\u01f8\u01f9\u0005"+
		"\r\u0000\u0000\u01f9\u01fd\u0003\u0082A\u0000\u01fa\u01fc\u0003>\u001f"+
		"\u0000\u01fb\u01fa\u0001\u0000\u0000\u0000\u01fc\u01ff\u0001\u0000\u0000"+
		"\u0000\u01fd\u01fb\u0001\u0000\u0000\u0000\u01fd\u01fe\u0001\u0000\u0000"+
		"\u0000\u01fe\u0200\u0001\u0000\u0000\u0000\u01ff\u01fd\u0001\u0000\u0000"+
		"\u0000\u0200\u0201\u0005\u000e\u0000\u0000\u0201=\u0001\u0000\u0000\u0000"+
		"\u0202\u0206\u0003@ \u0000\u0203\u0206\u0003H$\u0000\u0204\u0206\u0003"+
		"L&\u0000\u0205\u0202\u0001\u0000\u0000\u0000\u0205\u0203\u0001\u0000\u0000"+
		"\u0000\u0205\u0204\u0001\u0000\u0000\u0000\u0206?\u0001\u0000\u0000\u0000"+
		"\u0207\u020b\u0005\u000f\u0000\u0000\u0208\u0209\u0003\u0084B\u0000\u0209"+
		"\u020a\u0005\t\u0000\u0000\u020a\u020c\u0001\u0000\u0000\u0000\u020b\u0208"+
		"\u0001\u0000\u0000\u0000\u020b\u020c\u0001\u0000\u0000\u0000\u020c\u020d"+
		"\u0001\u0000\u0000\u0000\u020d\u020f\u0003\u0082A\u0000\u020e\u0210\u0003"+
		"B!\u0000\u020f\u020e\u0001\u0000\u0000\u0000\u0210\u0211\u0001\u0000\u0000"+
		"\u0000\u0211\u020f\u0001\u0000\u0000\u0000\u0211\u0212\u0001\u0000\u0000"+
		"\u0000\u0212A\u0001\u0000\u0000\u0000\u0213\u0216\u0003D\"\u0000\u0214"+
		"\u0216\u0003F#\u0000\u0215\u0213\u0001\u0000\u0000\u0000\u0215\u0214\u0001"+
		"\u0000\u0000\u0000\u0216C\u0001\u0000\u0000\u0000\u0217\u021f\u0005\u0010"+
		"\u0000\u0000\u0218\u021d\u0003\u0084B\u0000\u0219\u021a\u0005\u0004\u0000"+
		"\u0000\u021a\u021b\u0003T*\u0000\u021b\u021c\u0005\u0006\u0000\u0000\u021c"+
		"\u021e\u0001\u0000\u0000\u0000\u021d\u0219\u0001\u0000\u0000\u0000\u021d"+
		"\u021e\u0001\u0000\u0000\u0000\u021e\u0220\u0001\u0000\u0000\u0000\u021f"+
		"\u0218\u0001\u0000\u0000\u0000\u021f\u0220\u0001\u0000\u0000\u0000\u0220"+
		"\u0221\u0001\u0000\u0000\u0000\u0221\u0222\u0005\t\u0000\u0000\u0222\u0223"+
		"\u0003T*\u0000\u0223E\u0001\u0000\u0000\u0000\u0224\u0226\u0005\u0011"+
		"\u0000\u0000\u0225\u0224\u0001\u0000\u0000\u0000\u0225\u0226\u0001\u0000"+
		"\u0000\u0000\u0226\u0227\u0001\u0000\u0000\u0000\u0227\u0229\u0005\u0012"+
		"\u0000\u0000\u0228\u022a\u0003\u0084B\u0000\u0229\u0228\u0001\u0000\u0000"+
		"\u0000\u0229\u022a\u0001\u0000\u0000\u0000\u022a\u022b\u0001\u0000\u0000"+
		"\u0000\u022b\u023d\u0005\t\u0000\u0000\u022c\u022d\u0003\u0084B\u0000"+
		"\u022d\u022e\u0005\t\u0000\u0000\u022e\u022f\u0003v;\u0000\u022f\u0230"+
		"\u0005\n\u0000\u0000\u0230\u0231\u0003T*\u0000\u0231\u023e\u0001\u0000"+
		"\u0000\u0000\u0232\u0233\u0003\u0084B\u0000\u0233\u0235\u0005\u0004\u0000"+
		"\u0000\u0234\u0236\u0003P(\u0000\u0235\u0234\u0001\u0000\u0000\u0000\u0235"+
		"\u0236\u0001\u0000\u0000\u0000\u0236\u0237\u0001\u0000\u0000\u0000\u0237"+
		"\u0238\u0005\u0006\u0000\u0000\u0238\u0239\u0005\t\u0000\u0000\u0239\u023a"+
		"\u0003v;\u0000\u023a\u023b\u0005\n\u0000\u0000\u023b\u023c\u0003T*\u0000"+
		"\u023c\u023e\u0001\u0000\u0000\u0000\u023d\u022c\u0001\u0000\u0000\u0000"+
		"\u023d\u0232\u0001\u0000\u0000\u0000\u023eG\u0001\u0000\u0000\u0000\u023f"+
		"\u0243\u0005\u000f\u0000\u0000\u0240\u0241\u0003\u0082A\u0000\u0241\u0242"+
		"\u0005\\\u0000\u0000\u0242\u0244\u0001\u0000\u0000\u0000\u0243\u0240\u0001"+
		"\u0000\u0000\u0000\u0243\u0244\u0001\u0000\u0000\u0000\u0244\u0245\u0001"+
		"\u0000\u0000\u0000\u0245\u0246\u0003\u0084B\u0000\u0246\u0248\u0005\u0004"+
		"\u0000\u0000\u0247\u0249\u0003P(\u0000\u0248\u0247\u0001\u0000\u0000\u0000"+
		"\u0248\u0249\u0001\u0000\u0000\u0000\u0249\u024a\u0001\u0000\u0000\u0000"+
		"\u024a\u024d\u0005\u0006\u0000\u0000\u024b\u024c\u0005\t\u0000\u0000\u024c"+
		"\u024e\u0003v;\u0000\u024d\u024b\u0001\u0000\u0000\u0000\u024d\u024e\u0001"+
		"\u0000\u0000\u0000\u024e\u0250\u0001\u0000\u0000\u0000\u024f\u0251\u0003"+
		"J%\u0000\u0250\u024f\u0001\u0000\u0000\u0000\u0251\u0252\u0001\u0000\u0000"+
		"\u0000\u0252\u0250\u0001\u0000\u0000\u0000\u0252\u0253\u0001\u0000\u0000"+
		"\u0000\u0253I\u0001\u0000\u0000\u0000\u0254\u0256\u0005\u0013\u0000\u0000"+
		"\u0255\u0257\u0003\u0084B\u0000\u0256\u0255\u0001\u0000\u0000\u0000\u0256"+
		"\u0257\u0001\u0000\u0000\u0000\u0257\u0258\u0001\u0000\u0000\u0000\u0258"+
		"\u0259\u0005\t\u0000\u0000\u0259\u0267\u0003T*\u0000\u025a\u025c\u0005"+
		"\u0014\u0000\u0000\u025b\u025d\u0003\u0084B\u0000\u025c\u025b\u0001\u0000"+
		"\u0000\u0000\u025c\u025d\u0001\u0000\u0000\u0000\u025d\u025e\u0001\u0000"+
		"\u0000\u0000\u025e\u025f\u0005\t\u0000\u0000\u025f\u0267\u0003T*\u0000"+
		"\u0260\u0262\u0005\u0015\u0000\u0000\u0261\u0263\u0003\u0084B\u0000\u0262"+
		"\u0261\u0001\u0000\u0000\u0000\u0262\u0263\u0001\u0000\u0000\u0000\u0263"+
		"\u0264\u0001\u0000\u0000\u0000\u0264\u0265\u0005\t\u0000\u0000\u0265\u0267"+
		"\u0003T*\u0000\u0266\u0254\u0001\u0000\u0000\u0000\u0266\u025a\u0001\u0000"+
		"\u0000\u0000\u0266\u0260\u0001\u0000\u0000\u0000\u0267K\u0001\u0000\u0000"+
		"\u0000\u0268\u0269\u0005\u000f\u0000\u0000\u0269\u026a\u0003\u0082A\u0000"+
		"\u026a\u026b\u0005\\\u0000\u0000\u026b\u026c\u0003\u0084B\u0000\u026c"+
		"\u026d\u0005\t\u0000\u0000\u026d\u026f\u0003v;\u0000\u026e\u0270\u0003"+
		"N\'\u0000\u026f\u026e\u0001\u0000\u0000\u0000\u0270\u0271\u0001\u0000"+
		"\u0000\u0000\u0271\u026f\u0001\u0000\u0000\u0000\u0271\u0272\u0001\u0000"+
		"\u0000\u0000\u0272M\u0001\u0000\u0000\u0000\u0273\u0275\u0005\u0016\u0000"+
		"\u0000\u0274\u0276\u0003\u0084B\u0000\u0275\u0274\u0001\u0000\u0000\u0000"+
		"\u0275\u0276\u0001\u0000\u0000\u0000\u0276\u0277\u0001\u0000\u0000\u0000"+
		"\u0277\u0278\u0005\t\u0000\u0000\u0278\u0280\u0003T*\u0000\u0279\u027b"+
		"\u0005\u0017\u0000\u0000\u027a\u027c\u0003\u0084B\u0000\u027b\u027a\u0001"+
		"\u0000\u0000\u0000\u027b\u027c\u0001\u0000\u0000\u0000\u027c\u027d\u0001"+
		"\u0000\u0000\u0000\u027d\u027e\u0005\t\u0000\u0000\u027e\u0280\u0003T"+
		"*\u0000\u027f\u0273\u0001\u0000\u0000\u0000\u027f\u0279\u0001\u0000\u0000"+
		"\u0000\u0280O\u0001\u0000\u0000\u0000\u0281\u0286\u0003R)\u0000\u0282"+
		"\u0283\u0005\u0005\u0000\u0000\u0283\u0285\u0003R)\u0000\u0284\u0282\u0001"+
		"\u0000\u0000\u0000\u0285\u0288\u0001\u0000\u0000\u0000\u0286\u0284\u0001"+
		"\u0000\u0000\u0000\u0286\u0287\u0001\u0000\u0000\u0000\u0287Q\u0001\u0000"+
		"\u0000\u0000\u0288\u0286\u0001\u0000\u0000\u0000\u0289\u028a\u0003\u0084"+
		"B\u0000\u028a\u028b\u0005\t\u0000\u0000\u028b\u028c\u0003v;\u0000\u028c"+
		"S\u0001\u0000\u0000\u0000\u028d\u028e\u0006*\uffff\uffff\u0000\u028e\u028f"+
		"\u0005\u001b\u0000\u0000\u028f\u0294\u0003T*\u000b\u0290\u0291\u0005\u001c"+
		"\u0000\u0000\u0291\u0294\u0003T*\n\u0292\u0294\u0003V+\u0000\u0293\u028d"+
		"\u0001\u0000\u0000\u0000\u0293\u0290\u0001\u0000\u0000\u0000\u0293\u0292"+
		"\u0001\u0000\u0000\u0000\u0294\u02b5\u0001\u0000\u0000\u0000\u0295\u0296"+
		"\n\t\u0000\u0000\u0296\u0297\u0007\u0002\u0000\u0000\u0297\u02b4\u0003"+
		"T*\n\u0298\u0299\n\b\u0000\u0000\u0299\u029a\u0007\u0003\u0000\u0000\u029a"+
		"\u02b4\u0003T*\t\u029b\u029c\n\u0007\u0000\u0000\u029c\u029d\u0007\u0004"+
		"\u0000\u0000\u029d\u02b4\u0003T*\b\u029e\u029f\n\u0006\u0000\u0000\u029f"+
		"\u02a0\u0007\u0005\u0000\u0000\u02a0\u02b4\u0003T*\u0007\u02a1\u02a2\n"+
		"\u0005\u0000\u0000\u02a2\u02a3\u0005$\u0000\u0000\u02a3\u02b4\u0003T*"+
		"\u0006\u02a4\u02a5\n\u0004\u0000\u0000\u02a5\u02a6\u0005%\u0000\u0000"+
		"\u02a6\u02b4\u0003T*\u0005\u02a7\u02a8\n\u0003\u0000\u0000\u02a8\u02a9"+
		"\u0005&\u0000\u0000\u02a9\u02b4\u0003T*\u0004\u02aa\u02ab\n\u0002\u0000"+
		"\u0000\u02ab\u02ac\u0005\'\u0000\u0000\u02ac\u02b4\u0003T*\u0003\u02ad"+
		"\u02ae\n\r\u0000\u0000\u02ae\u02af\u0007\u0006\u0000\u0000\u02af\u02b4"+
		"\u0003X,\u0000\u02b0\u02b1\n\f\u0000\u0000\u02b1\u02b2\u0007\u0007\u0000"+
		"\u0000\u02b2\u02b4\u0003Z-\u0000\u02b3\u0295\u0001\u0000\u0000\u0000\u02b3"+
		"\u0298\u0001\u0000\u0000\u0000\u02b3\u029b\u0001\u0000\u0000\u0000\u02b3"+
		"\u029e\u0001\u0000\u0000\u0000\u02b3\u02a1\u0001\u0000\u0000\u0000\u02b3"+
		"\u02a4\u0001\u0000\u0000\u0000\u02b3\u02a7\u0001\u0000\u0000\u0000\u02b3"+
		"\u02aa\u0001\u0000\u0000\u0000\u02b3\u02ad\u0001\u0000\u0000\u0000\u02b3"+
		"\u02b0\u0001\u0000\u0000\u0000\u02b4\u02b7\u0001\u0000\u0000\u0000\u02b5"+
		"\u02b3\u0001\u0000\u0000\u0000\u02b5\u02b6\u0001\u0000\u0000\u0000\u02b6"+
		"U\u0001\u0000\u0000\u0000\u02b7\u02b5\u0001\u0000\u0000\u0000\u02b8\u02b9"+
		"\u0005\u0004\u0000\u0000\u02b9\u02ba\u0003T*\u0000\u02ba\u02bb\u0005\u0006"+
		"\u0000\u0000\u02bb\u02eb\u0001\u0000\u0000\u0000\u02bc\u02eb\u0005(\u0000"+
		"\u0000\u02bd\u02be\u0005)\u0000\u0000\u02be\u02bf\u0003T*\u0000\u02bf"+
		"\u02c0\u0005*\u0000\u0000\u02c0\u02c8\u0003T*\u0000\u02c1\u02c2\u0005"+
		"+\u0000\u0000\u02c2\u02c3\u0003T*\u0000\u02c3\u02c4\u0005*\u0000\u0000"+
		"\u02c4\u02c5\u0003T*\u0000\u02c5\u02c7\u0001\u0000\u0000\u0000\u02c6\u02c1"+
		"\u0001\u0000\u0000\u0000\u02c7\u02ca\u0001\u0000\u0000\u0000\u02c8\u02c6"+
		"\u0001\u0000\u0000\u0000\u02c8\u02c9\u0001\u0000\u0000\u0000\u02c9\u02cb"+
		"\u0001\u0000\u0000\u0000\u02ca\u02c8\u0001\u0000\u0000\u0000\u02cb\u02cc"+
		"\u0005,\u0000\u0000\u02cc\u02cd\u0003T*\u0000\u02cd\u02ce\u0005-\u0000"+
		"\u0000\u02ce\u02eb\u0001\u0000\u0000\u0000\u02cf\u02d0\u0005.\u0000\u0000"+
		"\u02d0\u02d5\u0003`0\u0000\u02d1\u02d2\u0005\u0005\u0000\u0000\u02d2\u02d4"+
		"\u0003`0\u0000\u02d3\u02d1\u0001\u0000\u0000\u0000\u02d4\u02d7\u0001\u0000"+
		"\u0000\u0000\u02d5\u02d3\u0001\u0000\u0000\u0000\u02d5\u02d6\u0001\u0000"+
		"\u0000\u0000\u02d6\u02d8\u0001\u0000\u0000\u0000\u02d7\u02d5\u0001\u0000"+
		"\u0000\u0000\u02d8\u02d9\u0005/\u0000\u0000\u02d9\u02da\u0003T*\u0000"+
		"\u02da\u02eb\u0001\u0000\u0000\u0000\u02db\u02eb\u0003d2\u0000\u02dc\u02dd"+
		"\u0003\u0082A\u0000\u02dd\u02df\u0005\u0004\u0000\u0000\u02de\u02e0\u0003"+
		"^/\u0000\u02df\u02de\u0001\u0000\u0000\u0000\u02df\u02e0\u0001\u0000\u0000"+
		"\u0000\u02e0\u02e1\u0001\u0000\u0000\u0000\u02e1\u02e3\u0005\u0006\u0000"+
		"\u0000\u02e2\u02e4\u0003b1\u0000\u02e3\u02e2\u0001\u0000\u0000\u0000\u02e3"+
		"\u02e4\u0001\u0000\u0000\u0000\u02e4\u02eb\u0001\u0000\u0000\u0000\u02e5"+
		"\u02eb\u0003x<\u0000\u02e6\u02eb\u0003z=\u0000\u02e7\u02eb\u0003|>\u0000"+
		"\u02e8\u02eb\u0003~?\u0000\u02e9\u02eb\u0003\u0082A\u0000\u02ea\u02b8"+
		"\u0001\u0000\u0000\u0000\u02ea\u02bc\u0001\u0000\u0000\u0000\u02ea\u02bd"+
		"\u0001\u0000\u0000\u0000\u02ea\u02cf\u0001\u0000\u0000\u0000\u02ea\u02db"+
		"\u0001\u0000\u0000\u0000\u02ea\u02dc\u0001\u0000\u0000\u0000\u02ea\u02e5"+
		"\u0001\u0000\u0000\u0000\u02ea\u02e6\u0001\u0000\u0000\u0000\u02ea\u02e7"+
		"\u0001\u0000\u0000\u0000\u02ea\u02e8\u0001\u0000\u0000\u0000\u02ea\u02e9"+
		"\u0001\u0000\u0000\u0000\u02ebW\u0001\u0000\u0000\u0000\u02ec\u02ed\u0003"+
		"\u0082A\u0000\u02ed\u02ee\u0005\\\u0000\u0000\u02ee\u02f0\u0001\u0000"+
		"\u0000\u0000\u02ef\u02ec\u0001\u0000\u0000\u0000\u02ef\u02f0\u0001\u0000"+
		"\u0000\u0000\u02f0\u02f1\u0001\u0000\u0000\u0000\u02f1\u02f2\u0003\u0084"+
		"B\u0000\u02f2\u02f4\u0005\u0004\u0000\u0000\u02f3\u02f5\u0003^/\u0000"+
		"\u02f4\u02f3\u0001\u0000\u0000\u0000\u02f4\u02f5\u0001\u0000\u0000\u0000"+
		"\u02f5\u02f6\u0001\u0000\u0000\u0000\u02f6\u02f8\u0005\u0006\u0000\u0000"+
		"\u02f7\u02f9\u0003b1\u0000\u02f8\u02f7\u0001\u0000\u0000\u0000\u02f8\u02f9"+
		"\u0001\u0000\u0000\u0000\u02f9\u0304\u0001\u0000\u0000\u0000\u02fa\u02fb"+
		"\u0003\u0082A\u0000\u02fb\u02fc\u0005\\\u0000\u0000\u02fc\u02fe\u0001"+
		"\u0000\u0000\u0000\u02fd\u02fa\u0001\u0000\u0000\u0000\u02fd\u02fe\u0001"+
		"\u0000\u0000\u0000\u02fe\u02ff\u0001\u0000\u0000\u0000\u02ff\u0301\u0003"+
		"\u0084B\u0000\u0300\u0302\u0003b1\u0000\u0301\u0300\u0001\u0000\u0000"+
		"\u0000\u0301\u0302\u0001\u0000\u0000\u0000\u0302\u0304\u0001\u0000\u0000"+
		"\u0000\u0303\u02ef\u0001\u0000\u0000\u0000\u0303\u02fd\u0001\u0000\u0000"+
		"\u0000\u0304Y\u0001\u0000\u0000\u0000\u0305\u0306\u0003\u0084B\u0000\u0306"+
		"\u0307\u0005\u0004\u0000\u0000\u0307\u0308\u0003\\.\u0000\u0308\u0309"+
		"\u00050\u0000\u0000\u0309\u030a\u0003T*\u0000\u030a\u030b\u0005\u0006"+
		"\u0000\u0000\u030b\u0327\u0001\u0000\u0000\u0000\u030c\u030d\u0003\u0084"+
		"B\u0000\u030d\u030e\u0005\u0004\u0000\u0000\u030e\u0311\u0003\u0084B\u0000"+
		"\u030f\u0310\u0005\t\u0000\u0000\u0310\u0312\u0003v;\u0000\u0311\u030f"+
		"\u0001\u0000\u0000\u0000\u0311\u0312\u0001\u0000\u0000\u0000\u0312\u0313"+
		"\u0001\u0000\u0000\u0000\u0313\u0314\u0005\u0002\u0000\u0000\u0314\u0317"+
		"\u0003\u0084B\u0000\u0315\u0316\u0005\t\u0000\u0000\u0316\u0318\u0003"+
		"v;\u0000\u0317\u0315\u0001\u0000\u0000\u0000\u0317\u0318\u0001\u0000\u0000"+
		"\u0000\u0318\u0319\u0001\u0000\u0000\u0000\u0319\u031a\u0005\n\u0000\u0000"+
		"\u031a\u031b\u0003T*\u0000\u031b\u031c\u00050\u0000\u0000\u031c\u031d"+
		"\u0003T*\u0000\u031d\u031e\u0005\u0006\u0000\u0000\u031e\u0327\u0001\u0000"+
		"\u0000\u0000\u031f\u0320\u0003\u0084B\u0000\u0320\u0322\u0005\u0004\u0000"+
		"\u0000\u0321\u0323\u0003^/\u0000\u0322\u0321\u0001\u0000\u0000\u0000\u0322"+
		"\u0323\u0001\u0000\u0000\u0000\u0323\u0324\u0001\u0000\u0000\u0000\u0324"+
		"\u0325\u0005\u0006\u0000\u0000\u0325\u0327\u0001\u0000\u0000\u0000\u0326"+
		"\u0305\u0001\u0000\u0000\u0000\u0326\u030c\u0001\u0000\u0000\u0000\u0326"+
		"\u031f\u0001\u0000\u0000\u0000\u0327[\u0001\u0000\u0000\u0000\u0328\u032b"+
		"\u0003\u0084B\u0000\u0329\u032a\u0005\t\u0000\u0000\u032a\u032c\u0003"+
		"v;\u0000\u032b\u0329\u0001\u0000\u0000\u0000\u032b\u032c\u0001\u0000\u0000"+
		"\u0000\u032c\u0335\u0001\u0000\u0000\u0000\u032d\u032e\u0005\u0005\u0000"+
		"\u0000\u032e\u0331\u0003\u0084B\u0000\u032f\u0330\u0005\t\u0000\u0000"+
		"\u0330\u0332\u0003v;\u0000\u0331\u032f\u0001\u0000\u0000\u0000\u0331\u0332"+
		"\u0001\u0000\u0000\u0000\u0332\u0334\u0001\u0000\u0000\u0000\u0333\u032d"+
		"\u0001\u0000\u0000\u0000\u0334\u0337\u0001\u0000\u0000\u0000\u0335\u0333"+
		"\u0001\u0000\u0000\u0000\u0335\u0336\u0001\u0000\u0000\u0000\u0336]\u0001"+
		"\u0000\u0000\u0000\u0337\u0335\u0001\u0000\u0000\u0000\u0338\u033d\u0003"+
		"T*\u0000\u0339\u033a\u0005\u0005\u0000\u0000\u033a\u033c\u0003T*\u0000"+
		"\u033b\u0339\u0001\u0000\u0000\u0000\u033c\u033f\u0001\u0000\u0000\u0000"+
		"\u033d\u033b\u0001\u0000\u0000\u0000\u033d\u033e\u0001\u0000\u0000\u0000"+
		"\u033e_\u0001\u0000\u0000\u0000\u033f\u033d\u0001\u0000\u0000\u0000\u0340"+
		"\u0343\u0003\u0084B\u0000\u0341\u0342\u0005\t\u0000\u0000\u0342\u0344"+
		"\u0003v;\u0000\u0343\u0341\u0001\u0000\u0000\u0000\u0343\u0344\u0001\u0000"+
		"\u0000\u0000\u0344\u0345\u0001\u0000\u0000\u0000\u0345\u0346\u0005\n\u0000"+
		"\u0000\u0346\u0347\u0003T*\u0000\u0347a\u0001\u0000\u0000\u0000\u0348"+
		"\u0349\u00051\u0000\u0000\u0349\u034a\u0005\u0013\u0000\u0000\u034ac\u0001"+
		"\u0000\u0000\u0000\u034b\u0357\u0005_\u0000\u0000\u034c\u0357\u0005^\u0000"+
		"\u0000\u034d\u0357\u0003f3\u0000\u034e\u0357\u00052\u0000\u0000\u034f"+
		"\u0357\u00053\u0000\u0000\u0350\u0357\u00054\u0000\u0000\u0351\u0357\u0005"+
		"5\u0000\u0000\u0352\u0357\u0005\u0001\u0000\u0000\u0353\u0357\u0003h4"+
		"\u0000\u0354\u0357\u0003n7\u0000\u0355\u0357\u0003r9\u0000\u0356\u034b"+
		"\u0001\u0000\u0000\u0000\u0356\u034c\u0001\u0000\u0000\u0000\u0356\u034d"+
		"\u0001\u0000\u0000\u0000\u0356\u034e\u0001\u0000\u0000\u0000\u0356\u034f"+
		"\u0001\u0000\u0000\u0000\u0356\u0350\u0001\u0000\u0000\u0000\u0356\u0351"+
		"\u0001\u0000\u0000\u0000\u0356\u0352\u0001\u0000\u0000\u0000\u0356\u0353"+
		"\u0001\u0000\u0000\u0000\u0356\u0354\u0001\u0000\u0000\u0000\u0356\u0355"+
		"\u0001\u0000\u0000\u0000\u0357e\u0001\u0000\u0000\u0000\u0358\u035a\u0005"+
		"`\u0000\u0000\u0359\u0358\u0001\u0000\u0000\u0000\u035a\u035b\u0001\u0000"+
		"\u0000\u0000\u035b\u0359\u0001\u0000\u0000\u0000\u035b\u035c\u0001\u0000"+
		"\u0000\u0000\u035cg\u0001\u0000\u0000\u0000\u035d\u035e\u0003j5\u0000"+
		"\u035e\u0367\u0005\u0007\u0000\u0000\u035f\u0364\u0003l6\u0000\u0360\u0361"+
		"\u0005\u0005\u0000\u0000\u0361\u0363\u0003l6\u0000\u0362\u0360\u0001\u0000"+
		"\u0000\u0000\u0363\u0366\u0001\u0000\u0000\u0000\u0364\u0362\u0001\u0000"+
		"\u0000\u0000\u0364\u0365\u0001\u0000\u0000\u0000\u0365\u0368\u0001\u0000"+
		"\u0000\u0000\u0366\u0364\u0001\u0000\u0000\u0000\u0367\u035f\u0001\u0000"+
		"\u0000\u0000\u0367\u0368\u0001\u0000\u0000\u0000\u0368\u0369\u0001\u0000"+
		"\u0000\u0000\u0369\u036a\u0005\b\u0000\u0000\u036ai\u0001\u0000\u0000"+
		"\u0000\u036b\u036c\u0007\b\u0000\u0000\u036ck\u0001\u0000\u0000\u0000"+
		"\u036d\u0370\u0003T*\u0000\u036e\u036f\u0005;\u0000\u0000\u036f\u0371"+
		"\u0003T*\u0000\u0370\u036e\u0001\u0000\u0000\u0000\u0370\u0371\u0001\u0000"+
		"\u0000\u0000\u0371m\u0001\u0000\u0000\u0000\u0372\u0373\u0005<\u0000\u0000"+
		"\u0373\u0374\u0005\u0007\u0000\u0000\u0374\u0379\u0003p8\u0000\u0375\u0376"+
		"\u0005\u0005\u0000\u0000\u0376\u0378\u0003p8\u0000\u0377\u0375\u0001\u0000"+
		"\u0000\u0000\u0378\u037b\u0001\u0000\u0000\u0000\u0379\u0377\u0001\u0000"+
		"\u0000\u0000\u0379\u037a\u0001\u0000\u0000\u0000\u037a\u037c\u0001\u0000"+
		"\u0000\u0000\u037b\u0379\u0001\u0000\u0000\u0000\u037c\u037d\u0005\b\u0000"+
		"\u0000\u037do\u0001\u0000\u0000\u0000\u037e\u0381\u0003\u0084B\u0000\u037f"+
		"\u0380\u0005\t\u0000\u0000\u0380\u0382\u0003v;\u0000\u0381\u037f\u0001"+
		"\u0000\u0000\u0000\u0381\u0382\u0001\u0000\u0000\u0000\u0382\u0383\u0001"+
		"\u0000\u0000\u0000\u0383\u0384\u0005\n\u0000\u0000\u0384\u0385\u0003T"+
		"*\u0000\u0385q\u0001\u0000\u0000\u0000\u0386\u0387\u0005=\u0000\u0000"+
		"\u0387\u0390\u0005\u0007\u0000\u0000\u0388\u038d\u0003t:\u0000\u0389\u038a"+
		"\u0005\u0005\u0000\u0000\u038a\u038c\u0003t:\u0000\u038b\u0389\u0001\u0000"+
		"\u0000\u0000\u038c\u038f\u0001\u0000\u0000\u0000\u038d\u038b\u0001\u0000"+
		"\u0000\u0000\u038d\u038e\u0001\u0000\u0000\u0000\u038e\u0391\u0001\u0000"+
		"\u0000\u0000\u038f\u038d\u0001\u0000\u0000\u0000\u0390\u0388\u0001\u0000"+
		"\u0000\u0000\u0390\u0391\u0001\u0000\u0000\u0000\u0391\u0392\u0001\u0000"+
		"\u0000\u0000\u0392\u0393\u0005\b\u0000\u0000\u0393s\u0001\u0000\u0000"+
		"\u0000\u0394\u0395\u0003T*\u0000\u0395\u0396\u0007\t\u0000\u0000\u0396"+
		"\u0397\u0003T*\u0000\u0397u\u0001\u0000\u0000\u0000\u0398\u039e\u0003"+
		"x<\u0000\u0399\u039e\u0003z=\u0000\u039a\u039e\u0003|>\u0000\u039b\u039e"+
		"\u0003~?\u0000\u039c\u039e\u0003\u0082A\u0000\u039d\u0398\u0001\u0000"+
		"\u0000\u0000\u039d\u0399\u0001\u0000\u0000\u0000\u039d\u039a\u0001\u0000"+
		"\u0000\u0000\u039d\u039b\u0001\u0000\u0000\u0000\u039d\u039c\u0001\u0000"+
		"\u0000\u0000\u039ew\u0001\u0000\u0000\u0000\u039f\u03a0\u0007\n\u0000"+
		"\u0000\u03a0y\u0001\u0000\u0000\u0000\u03a1\u03a2\u0003j5\u0000\u03a2"+
		"\u03a3\u0005\u0004\u0000\u0000\u03a3\u03a4\u0003v;\u0000\u03a4\u03a5\u0005"+
		"\u0006\u0000\u0000\u03a5{\u0001\u0000\u0000\u0000\u03a6\u03a7\u0005=\u0000"+
		"\u0000\u03a7\u03a8\u0005\u0004\u0000\u0000\u03a8\u03a9\u0003v;\u0000\u03a9"+
		"\u03aa\u0005\u0005\u0000\u0000\u03aa\u03ab\u0003v;\u0000\u03ab\u03ac\u0005"+
		"\u0006\u0000\u0000\u03ac}\u0001\u0000\u0000\u0000\u03ad\u03ae\u0005<\u0000"+
		"\u0000\u03ae\u03af\u0005\u0004\u0000\u0000\u03af\u03b4\u0003\u0080@\u0000"+
		"\u03b0\u03b1\u0005\u0005\u0000\u0000\u03b1\u03b3\u0003\u0080@\u0000\u03b2"+
		"\u03b0\u0001\u0000\u0000\u0000\u03b3\u03b6\u0001\u0000\u0000\u0000\u03b4"+
		"\u03b2\u0001\u0000\u0000\u0000\u03b4\u03b5\u0001\u0000\u0000\u0000\u03b5"+
		"\u03b7\u0001\u0000\u0000\u0000\u03b6\u03b4\u0001\u0000\u0000\u0000\u03b7"+
		"\u03b8\u0005\u0006\u0000\u0000\u03b8\u007f\u0001\u0000\u0000\u0000\u03b9"+
		"\u03ba\u0003\u0084B\u0000\u03ba\u03bb\u0005\t\u0000\u0000\u03bb\u03bc"+
		"\u0003v;\u0000\u03bc\u0081\u0001\u0000\u0000\u0000\u03bd\u03c2\u0003\u0084"+
		"B\u0000\u03be\u03bf\u0005\\\u0000\u0000\u03bf\u03c1\u0003\u0084B\u0000"+
		"\u03c0\u03be\u0001\u0000\u0000\u0000\u03c1\u03c4\u0001\u0000\u0000\u0000"+
		"\u03c2\u03c0\u0001\u0000\u0000\u0000\u03c2\u03c3\u0001\u0000\u0000\u0000"+
		"\u03c3\u0083\u0001\u0000\u0000\u0000\u03c4\u03c2\u0001\u0000\u0000\u0000"+
		"\u03c5\u03c6\u0007\u000b\u0000\u0000\u03c6\u0085\u0001\u0000\u0000\u0000"+
		"o\u008c\u0092\u009b\u00a0\u00a9\u00b4\u00ba\u00c0\u00c5\u00c7\u00d5\u00da"+
		"\u00e6\u00f4\u00f7\u00fa\u0100\u0106\u010b\u010d\u0110\u0113\u011c\u0123"+
		"\u0128\u0131\u0138\u013c\u0143\u0147\u015b\u0161\u0164\u016a\u0173\u0183"+
		"\u0186\u018f\u0198\u019e\u01a0\u01a5\u01ae\u01ba\u01c7\u01ca\u01d4\u01e3"+
		"\u01e8\u01ea\u01f1\u01f6\u01fd\u0205\u020b\u0211\u0215\u021d\u021f\u0225"+
		"\u0229\u0235\u023d\u0243\u0248\u024d\u0252\u0256\u025c\u0262\u0266\u0271"+
		"\u0275\u027b\u027f\u0286\u0293\u02b3\u02b5\u02c8\u02d5\u02df\u02e3\u02ea"+
		"\u02ef\u02f4\u02f8\u02fd\u0301\u0303\u0311\u0317\u0322\u0326\u032b\u0331"+
		"\u0335\u033d\u0343\u0356\u035b\u0364\u0367\u0370\u0379\u0381\u038d\u0390"+
		"\u039d\u03b4\u03c2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}