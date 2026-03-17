// Generated from /opt/git/emf.m2x/org.eclipse.fennec.m2x.m2t.parser/grammar/M2tParser.g4 by ANTLR 4.13.2
package org.eclipse.fennec.m2x.m2t.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class M2tParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		BLOCK_OPEN=1, TEXT=2, SLASH_CLOSE=3, CLOSE=4, SLASH=5, KW_MODULE=6, KW_IMPORT=7, 
		KW_EXTENDS=8, KW_TEMPLATE=9, KW_QUERY=10, KW_MACRO=11, KW_FOR=12, KW_IF=13, 
		KW_ELSEIF=14, KW_ELSE=15, KW_LET=16, KW_ELSELET=17, KW_FILE=18, KW_TRACE=19, 
		KW_PROTECTED=20, KW_COMMENT=21, KW_OVERRIDES=22, KW_PUBLIC=23, KW_PRIVATE=24, 
		KW_BEFORE=25, KW_SEPARATOR=26, KW_AFTER=27, KW_GUARD=28, KW_POST=29, KW_SUPER=30, 
		KW_STDOUT=31, LPAREN=32, RPAREN=33, LBRACE=34, RBRACE=35, COMMA=36, SEMICOLON=37, 
		COLONCOLON=38, COLON=39, PIPE=40, EQUALS=41, DOT=42, SAFENAV=43, ARROW=44, 
		SAFEARROW=45, DOTDOT=46, STAR=47, PLUS=48, MINUS=49, LT=50, GT=51, LE=52, 
		GE=53, NE=54, KW_NOT=55, KW_AND=56, KW_OR=57, KW_XOR=58, KW_IMPLIES=59, 
		KW_SELF=60, KW_TRUE=61, KW_FALSE=62, KW_NULL=63, KW_INVALID=64, KW_IN=65, 
		KW_THEN=66, KW_ENDIF=67, KW_SET=68, KW_ORDEREDSET=69, KW_BAG=70, KW_SEQUENCE=71, 
		KW_COLLECTION=72, KW_MAP=73, KW_TUPLE=74, KW_BOOLEAN=75, KW_INTEGER=76, 
		KW_REAL=77, KW_STRING=78, KW_UNLIMITEDNATURAL=79, KW_OCLANY=80, KW_OCLVOID=81, 
		KW_OCLINVALID=82, KW_OCLMESSAGE=83, KW_WITH=84, LARROW=85, REAL_LITERAL=86, 
		INTEGER_LITERAL=87, STRING_LITERAL=88, ESCAPED_IDENTIFIER=89, IDENTIFIER=90, 
		CODE_WS=91, CODE_LINE_COMMENT=92, CODE_BLOCK_COMMENT=93;
	public static final int
		RULE_module = 0, RULE_moduleDecl = 1, RULE_metamodelList = 2, RULE_extendsDecl = 3, 
		RULE_importDecl = 4, RULE_moduleElement = 5, RULE_commentBlock = 6, RULE_templateDef = 7, 
		RULE_signature = 8, RULE_visibility = 9, RULE_overridesDecl = 10, RULE_guard = 11, 
		RULE_postDecl = 12, RULE_initSection = 13, RULE_variableDeclaration = 14, 
		RULE_queryDef = 15, RULE_macroDef = 16, RULE_argList = 17, RULE_argDecl = 18, 
		RULE_body = 19, RULE_bodyElement = 20, RULE_textBlock = 21, RULE_inlineExpression = 22, 
		RULE_forBlock = 23, RULE_beforeClause = 24, RULE_separatorClause = 25, 
		RULE_afterClause = 26, RULE_ifBlock = 27, RULE_elseIfBlock = 28, RULE_elseBlock = 29, 
		RULE_letBlock = 30, RULE_elseLetBlock = 31, RULE_fileBlock = 32, RULE_traceBlock = 33, 
		RULE_protectedBlock = 34, RULE_templateInvocation = 35, RULE_expression = 36, 
		RULE_primaryExpression = 37, RULE_propertyOrCallSuffix = 38, RULE_iteratorOrOperationCall = 39, 
		RULE_iteratorVariables = 40, RULE_argumentList = 41, RULE_letBinding = 42, 
		RULE_literalExpression = 43, RULE_stringLiteral = 44, RULE_collectionLiteral = 45, 
		RULE_collectionKind = 46, RULE_collectionLiteralPart = 47, RULE_tupleLiteral = 48, 
		RULE_tupleLiteralPart = 49, RULE_mapLiteral = 50, RULE_mapLiteralPart = 51, 
		RULE_typeExpression = 52, RULE_primitiveType = 53, RULE_collectionType = 54, 
		RULE_mapType = 55, RULE_tupleType = 56, RULE_tupleTypePart = 57, RULE_pathName = 58, 
		RULE_identifier = 59;
	private static String[] makeRuleNames() {
		return new String[] {
			"module", "moduleDecl", "metamodelList", "extendsDecl", "importDecl", 
			"moduleElement", "commentBlock", "templateDef", "signature", "visibility", 
			"overridesDecl", "guard", "postDecl", "initSection", "variableDeclaration", 
			"queryDef", "macroDef", "argList", "argDecl", "body", "bodyElement", 
			"textBlock", "inlineExpression", "forBlock", "beforeClause", "separatorClause", 
			"afterClause", "ifBlock", "elseIfBlock", "elseBlock", "letBlock", "elseLetBlock", 
			"fileBlock", "traceBlock", "protectedBlock", "templateInvocation", "expression", 
			"primaryExpression", "propertyOrCallSuffix", "iteratorOrOperationCall", 
			"iteratorVariables", "argumentList", "letBinding", "literalExpression", 
			"stringLiteral", "collectionLiteral", "collectionKind", "collectionLiteralPart", 
			"tupleLiteral", "tupleLiteralPart", "mapLiteral", "mapLiteralPart", "typeExpression", 
			"primitiveType", "collectionType", "mapType", "tupleType", "tupleTypePart", 
			"pathName", "identifier"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'['", null, "'/]'", "']'", "'/'", "'module'", "'import'", "'extends'", 
			"'template'", "'query'", "'macro'", "'for'", "'if'", "'elseif'", "'else'", 
			"'let'", "'elselet'", "'file'", "'trace'", "'protected'", "'comment'", 
			"'overrides'", "'public'", "'private'", "'before'", "'separator'", "'after'", 
			"'?'", "'post'", "'super'", "'stdout'", "'('", "')'", "'{'", "'}'", "','", 
			"';'", "'::'", "':'", "'|'", "'='", "'.'", "'?.'", "'->'", "'?->'", "'..'", 
			"'*'", "'+'", "'-'", "'<'", "'>'", "'<='", "'>='", "'<>'", "'not'", "'and'", 
			"'or'", "'xor'", "'implies'", "'self'", "'true'", "'false'", "'null'", 
			"'invalid'", "'in'", "'then'", "'endif'", "'Set'", "'OrderedSet'", "'Bag'", 
			"'Sequence'", "'Collection'", "'Map'", "'Tuple'", "'Boolean'", "'Integer'", 
			"'Real'", "'String'", "'UnlimitedNatural'", "'OclAny'", "'OclVoid'", 
			"'OclInvalid'", "'OclMessage'", "'with'", "'<-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "BLOCK_OPEN", "TEXT", "SLASH_CLOSE", "CLOSE", "SLASH", "KW_MODULE", 
			"KW_IMPORT", "KW_EXTENDS", "KW_TEMPLATE", "KW_QUERY", "KW_MACRO", "KW_FOR", 
			"KW_IF", "KW_ELSEIF", "KW_ELSE", "KW_LET", "KW_ELSELET", "KW_FILE", "KW_TRACE", 
			"KW_PROTECTED", "KW_COMMENT", "KW_OVERRIDES", "KW_PUBLIC", "KW_PRIVATE", 
			"KW_BEFORE", "KW_SEPARATOR", "KW_AFTER", "KW_GUARD", "KW_POST", "KW_SUPER", 
			"KW_STDOUT", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "COMMA", "SEMICOLON", 
			"COLONCOLON", "COLON", "PIPE", "EQUALS", "DOT", "SAFENAV", "ARROW", "SAFEARROW", 
			"DOTDOT", "STAR", "PLUS", "MINUS", "LT", "GT", "LE", "GE", "NE", "KW_NOT", 
			"KW_AND", "KW_OR", "KW_XOR", "KW_IMPLIES", "KW_SELF", "KW_TRUE", "KW_FALSE", 
			"KW_NULL", "KW_INVALID", "KW_IN", "KW_THEN", "KW_ENDIF", "KW_SET", "KW_ORDEREDSET", 
			"KW_BAG", "KW_SEQUENCE", "KW_COLLECTION", "KW_MAP", "KW_TUPLE", "KW_BOOLEAN", 
			"KW_INTEGER", "KW_REAL", "KW_STRING", "KW_UNLIMITEDNATURAL", "KW_OCLANY", 
			"KW_OCLVOID", "KW_OCLINVALID", "KW_OCLMESSAGE", "KW_WITH", "LARROW", 
			"REAL_LITERAL", "INTEGER_LITERAL", "STRING_LITERAL", "ESCAPED_IDENTIFIER", 
			"IDENTIFIER", "CODE_WS", "CODE_LINE_COMMENT", "CODE_BLOCK_COMMENT"
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
	public String getGrammarFileName() { return "M2tParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public M2tParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleContext extends ParserRuleContext {
		public ModuleDeclContext moduleDecl() {
			return getRuleContext(ModuleDeclContext.class,0);
		}
		public TerminalNode EOF() { return getToken(M2tParser.EOF, 0); }
		public List<TerminalNode> TEXT() { return getTokens(M2tParser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(M2tParser.TEXT, i);
		}
		public List<ImportDeclContext> importDecl() {
			return getRuleContexts(ImportDeclContext.class);
		}
		public ImportDeclContext importDecl(int i) {
			return getRuleContext(ImportDeclContext.class,i);
		}
		public List<ModuleElementContext> moduleElement() {
			return getRuleContexts(ModuleElementContext.class);
		}
		public ModuleElementContext moduleElement(int i) {
			return getRuleContext(ModuleElementContext.class,i);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_module);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TEXT) {
				{
				setState(120);
				match(TEXT);
				}
			}

			setState(123);
			moduleDecl();
			setState(128);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(126);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case TEXT:
						{
						setState(124);
						match(TEXT);
						}
						break;
					case BLOCK_OPEN:
						{
						setState(125);
						importDecl();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(130);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(135);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BLOCK_OPEN || _la==TEXT) {
				{
				setState(133);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case TEXT:
					{
					setState(131);
					match(TEXT);
					}
					break;
				case BLOCK_OPEN:
					{
					setState(132);
					moduleElement();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(137);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(138);
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
	public static class ModuleDeclContext extends ParserRuleContext {
		public TerminalNode BLOCK_OPEN() { return getToken(M2tParser.BLOCK_OPEN, 0); }
		public TerminalNode KW_MODULE() { return getToken(M2tParser.KW_MODULE, 0); }
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public MetamodelListContext metamodelList() {
			return getRuleContext(MetamodelListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public TerminalNode SLASH_CLOSE() { return getToken(M2tParser.SLASH_CLOSE, 0); }
		public ExtendsDeclContext extendsDecl() {
			return getRuleContext(ExtendsDeclContext.class,0);
		}
		public ModuleDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitModuleDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleDeclContext moduleDecl() throws RecognitionException {
		ModuleDeclContext _localctx = new ModuleDeclContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_moduleDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			match(BLOCK_OPEN);
			setState(141);
			match(KW_MODULE);
			setState(142);
			pathName();
			setState(143);
			match(LPAREN);
			setState(144);
			metamodelList();
			setState(145);
			match(RPAREN);
			setState(147);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_EXTENDS) {
				{
				setState(146);
				extendsDecl();
				}
			}

			setState(149);
			match(SLASH_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetamodelListContext extends ParserRuleContext {
		public List<PathNameContext> pathName() {
			return getRuleContexts(PathNameContext.class);
		}
		public PathNameContext pathName(int i) {
			return getRuleContext(PathNameContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(M2tParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(M2tParser.COMMA, i);
		}
		public MetamodelListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metamodelList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitMetamodelList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetamodelListContext metamodelList() throws RecognitionException {
		MetamodelListContext _localctx = new MetamodelListContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_metamodelList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			pathName();
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(152);
				match(COMMA);
				setState(153);
				pathName();
				}
				}
				setState(158);
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
	public static class ExtendsDeclContext extends ParserRuleContext {
		public TerminalNode KW_EXTENDS() { return getToken(M2tParser.KW_EXTENDS, 0); }
		public List<PathNameContext> pathName() {
			return getRuleContexts(PathNameContext.class);
		}
		public PathNameContext pathName(int i) {
			return getRuleContext(PathNameContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(M2tParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(M2tParser.COMMA, i);
		}
		public ExtendsDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extendsDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitExtendsDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExtendsDeclContext extendsDecl() throws RecognitionException {
		ExtendsDeclContext _localctx = new ExtendsDeclContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_extendsDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(159);
			match(KW_EXTENDS);
			setState(160);
			pathName();
			setState(165);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(161);
				match(COMMA);
				setState(162);
				pathName();
				}
				}
				setState(167);
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
		public TerminalNode BLOCK_OPEN() { return getToken(M2tParser.BLOCK_OPEN, 0); }
		public TerminalNode KW_IMPORT() { return getToken(M2tParser.KW_IMPORT, 0); }
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode SLASH_CLOSE() { return getToken(M2tParser.SLASH_CLOSE, 0); }
		public ImportDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitImportDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportDeclContext importDecl() throws RecognitionException {
		ImportDeclContext _localctx = new ImportDeclContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_importDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			match(BLOCK_OPEN);
			setState(169);
			match(KW_IMPORT);
			setState(170);
			pathName();
			setState(171);
			match(SLASH_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TemplateDefContext templateDef() {
			return getRuleContext(TemplateDefContext.class,0);
		}
		public QueryDefContext queryDef() {
			return getRuleContext(QueryDefContext.class,0);
		}
		public MacroDefContext macroDef() {
			return getRuleContext(MacroDefContext.class,0);
		}
		public CommentBlockContext commentBlock() {
			return getRuleContext(CommentBlockContext.class,0);
		}
		public ModuleElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleElement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitModuleElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleElementContext moduleElement() throws RecognitionException {
		ModuleElementContext _localctx = new ModuleElementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_moduleElement);
		try {
			setState(177);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(173);
				templateDef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(174);
				queryDef();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(175);
				macroDef();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(176);
				commentBlock();
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
	public static class CommentBlockContext extends ParserRuleContext {
		public List<TerminalNode> BLOCK_OPEN() { return getTokens(M2tParser.BLOCK_OPEN); }
		public TerminalNode BLOCK_OPEN(int i) {
			return getToken(M2tParser.BLOCK_OPEN, i);
		}
		public List<TerminalNode> KW_COMMENT() { return getTokens(M2tParser.KW_COMMENT); }
		public TerminalNode KW_COMMENT(int i) {
			return getToken(M2tParser.KW_COMMENT, i);
		}
		public List<TerminalNode> CLOSE() { return getTokens(M2tParser.CLOSE); }
		public TerminalNode CLOSE(int i) {
			return getToken(M2tParser.CLOSE, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode SLASH() { return getToken(M2tParser.SLASH, 0); }
		public CommentBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commentBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitCommentBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentBlockContext commentBlock() throws RecognitionException {
		CommentBlockContext _localctx = new CommentBlockContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_commentBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			match(BLOCK_OPEN);
			setState(180);
			match(KW_COMMENT);
			setState(181);
			match(CLOSE);
			setState(182);
			body();
			setState(183);
			match(BLOCK_OPEN);
			setState(184);
			match(SLASH);
			setState(185);
			match(KW_COMMENT);
			setState(186);
			match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateDefContext extends ParserRuleContext {
		public List<TerminalNode> BLOCK_OPEN() { return getTokens(M2tParser.BLOCK_OPEN); }
		public TerminalNode BLOCK_OPEN(int i) {
			return getToken(M2tParser.BLOCK_OPEN, i);
		}
		public List<TerminalNode> KW_TEMPLATE() { return getTokens(M2tParser.KW_TEMPLATE); }
		public TerminalNode KW_TEMPLATE(int i) {
			return getToken(M2tParser.KW_TEMPLATE, i);
		}
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public List<TerminalNode> CLOSE() { return getTokens(M2tParser.CLOSE); }
		public TerminalNode CLOSE(int i) {
			return getToken(M2tParser.CLOSE, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode SLASH() { return getToken(M2tParser.SLASH, 0); }
		public TemplateDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitTemplateDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplateDefContext templateDef() throws RecognitionException {
		TemplateDefContext _localctx = new TemplateDefContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_templateDef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(188);
			match(BLOCK_OPEN);
			setState(189);
			match(KW_TEMPLATE);
			setState(190);
			signature();
			setState(191);
			match(CLOSE);
			setState(192);
			body();
			setState(193);
			match(BLOCK_OPEN);
			setState(194);
			match(SLASH);
			setState(195);
			match(KW_TEMPLATE);
			setState(196);
			match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SignatureContext extends ParserRuleContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public VisibilityContext visibility() {
			return getRuleContext(VisibilityContext.class,0);
		}
		public ArgListContext argList() {
			return getRuleContext(ArgListContext.class,0);
		}
		public OverridesDeclContext overridesDecl() {
			return getRuleContext(OverridesDeclContext.class,0);
		}
		public GuardContext guard() {
			return getRuleContext(GuardContext.class,0);
		}
		public PostDeclContext postDecl() {
			return getRuleContext(PostDeclContext.class,0);
		}
		public InitSectionContext initSection() {
			return getRuleContext(InitSectionContext.class,0);
		}
		public SignatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signature; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitSignature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SignatureContext signature() throws RecognitionException {
		SignatureContext _localctx = new SignatureContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_signature);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				setState(198);
				visibility();
				}
				break;
			}
			setState(201);
			pathName();
			setState(202);
			match(LPAREN);
			setState(204);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4026531776L) != 0) || _la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(203);
				argList();
				}
			}

			setState(206);
			match(RPAREN);
			setState(208);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_OVERRIDES) {
				{
				setState(207);
				overridesDecl();
				}
			}

			setState(211);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_GUARD) {
				{
				setState(210);
				guard();
				}
			}

			setState(214);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_POST) {
				{
				setState(213);
				postDecl();
				}
			}

			setState(217);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACE) {
				{
				setState(216);
				initSection();
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
	public static class VisibilityContext extends ParserRuleContext {
		public TerminalNode KW_PUBLIC() { return getToken(M2tParser.KW_PUBLIC, 0); }
		public TerminalNode KW_PRIVATE() { return getToken(M2tParser.KW_PRIVATE, 0); }
		public TerminalNode KW_PROTECTED() { return getToken(M2tParser.KW_PROTECTED, 0); }
		public VisibilityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_visibility; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitVisibility(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VisibilityContext visibility() throws RecognitionException {
		VisibilityContext _localctx = new VisibilityContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_visibility);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 26214400L) != 0)) ) {
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
	public static class OverridesDeclContext extends ParserRuleContext {
		public TerminalNode KW_OVERRIDES() { return getToken(M2tParser.KW_OVERRIDES, 0); }
		public List<PathNameContext> pathName() {
			return getRuleContexts(PathNameContext.class);
		}
		public PathNameContext pathName(int i) {
			return getRuleContext(PathNameContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(M2tParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(M2tParser.COMMA, i);
		}
		public OverridesDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_overridesDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitOverridesDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OverridesDeclContext overridesDecl() throws RecognitionException {
		OverridesDeclContext _localctx = new OverridesDeclContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_overridesDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			match(KW_OVERRIDES);
			setState(222);
			pathName();
			setState(227);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(223);
				match(COMMA);
				setState(224);
				pathName();
				}
				}
				setState(229);
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
	public static class GuardContext extends ParserRuleContext {
		public TerminalNode KW_GUARD() { return getToken(M2tParser.KW_GUARD, 0); }
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public GuardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_guard; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitGuard(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GuardContext guard() throws RecognitionException {
		GuardContext _localctx = new GuardContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_guard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			match(KW_GUARD);
			setState(231);
			match(LPAREN);
			setState(232);
			expression(0);
			setState(233);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PostDeclContext extends ParserRuleContext {
		public TerminalNode KW_POST() { return getToken(M2tParser.KW_POST, 0); }
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public PostDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitPostDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PostDeclContext postDecl() throws RecognitionException {
		PostDeclContext _localctx = new PostDeclContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_postDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(235);
			match(KW_POST);
			setState(236);
			match(LPAREN);
			setState(237);
			expression(0);
			setState(238);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode LBRACE() { return getToken(M2tParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(M2tParser.RBRACE, 0); }
		public List<VariableDeclarationContext> variableDeclaration() {
			return getRuleContexts(VariableDeclarationContext.class);
		}
		public VariableDeclarationContext variableDeclaration(int i) {
			return getRuleContext(VariableDeclarationContext.class,i);
		}
		public List<TerminalNode> SEMICOLON() { return getTokens(M2tParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(M2tParser.SEMICOLON, i);
		}
		public InitSectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initSection; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitInitSection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitSectionContext initSection() throws RecognitionException {
		InitSectionContext _localctx = new InitSectionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_initSection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(240);
			match(LBRACE);
			setState(244); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(241);
				variableDeclaration();
				setState(242);
				match(SEMICOLON);
				}
				}
				setState(246); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 4026531776L) != 0) || _la==ESCAPED_IDENTIFIER || _la==IDENTIFIER );
			setState(248);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclarationContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode COLON() { return getToken(M2tParser.COLON, 0); }
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public TerminalNode EQUALS() { return getToken(M2tParser.EQUALS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclarationContext variableDeclaration() throws RecognitionException {
		VariableDeclarationContext _localctx = new VariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_variableDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(250);
			identifier();
			setState(251);
			match(COLON);
			setState(252);
			typeExpression();
			setState(255);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EQUALS) {
				{
				setState(253);
				match(EQUALS);
				setState(254);
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
	public static class QueryDefContext extends ParserRuleContext {
		public TerminalNode BLOCK_OPEN() { return getToken(M2tParser.BLOCK_OPEN, 0); }
		public TerminalNode KW_QUERY() { return getToken(M2tParser.KW_QUERY, 0); }
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public TerminalNode COLON() { return getToken(M2tParser.COLON, 0); }
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public TerminalNode EQUALS() { return getToken(M2tParser.EQUALS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SLASH_CLOSE() { return getToken(M2tParser.SLASH_CLOSE, 0); }
		public VisibilityContext visibility() {
			return getRuleContext(VisibilityContext.class,0);
		}
		public ArgListContext argList() {
			return getRuleContext(ArgListContext.class,0);
		}
		public QueryDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitQueryDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryDefContext queryDef() throws RecognitionException {
		QueryDefContext _localctx = new QueryDefContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_queryDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257);
			match(BLOCK_OPEN);
			setState(258);
			match(KW_QUERY);
			setState(260);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(259);
				visibility();
				}
				break;
			}
			setState(262);
			pathName();
			setState(263);
			match(LPAREN);
			setState(265);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4026531776L) != 0) || _la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(264);
				argList();
				}
			}

			setState(267);
			match(RPAREN);
			setState(268);
			match(COLON);
			setState(269);
			typeExpression();
			setState(270);
			match(EQUALS);
			setState(271);
			expression(0);
			setState(272);
			match(SLASH_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MacroDefContext extends ParserRuleContext {
		public List<TerminalNode> BLOCK_OPEN() { return getTokens(M2tParser.BLOCK_OPEN); }
		public TerminalNode BLOCK_OPEN(int i) {
			return getToken(M2tParser.BLOCK_OPEN, i);
		}
		public List<TerminalNode> KW_MACRO() { return getTokens(M2tParser.KW_MACRO); }
		public TerminalNode KW_MACRO(int i) {
			return getToken(M2tParser.KW_MACRO, i);
		}
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public List<TerminalNode> CLOSE() { return getTokens(M2tParser.CLOSE); }
		public TerminalNode CLOSE(int i) {
			return getToken(M2tParser.CLOSE, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode SLASH() { return getToken(M2tParser.SLASH, 0); }
		public ArgListContext argList() {
			return getRuleContext(ArgListContext.class,0);
		}
		public MacroDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_macroDef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitMacroDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MacroDefContext macroDef() throws RecognitionException {
		MacroDefContext _localctx = new MacroDefContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_macroDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(274);
			match(BLOCK_OPEN);
			setState(275);
			match(KW_MACRO);
			setState(276);
			pathName();
			setState(277);
			match(LPAREN);
			setState(279);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4026531776L) != 0) || _la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(278);
				argList();
				}
			}

			setState(281);
			match(RPAREN);
			setState(282);
			match(CLOSE);
			setState(283);
			body();
			setState(284);
			match(BLOCK_OPEN);
			setState(285);
			match(SLASH);
			setState(286);
			match(KW_MACRO);
			setState(287);
			match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgListContext extends ParserRuleContext {
		public List<ArgDeclContext> argDecl() {
			return getRuleContexts(ArgDeclContext.class);
		}
		public ArgDeclContext argDecl(int i) {
			return getRuleContext(ArgDeclContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(M2tParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(M2tParser.COMMA, i);
		}
		public ArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitArgList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgListContext argList() throws RecognitionException {
		ArgListContext _localctx = new ArgListContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_argList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(289);
			argDecl();
			setState(294);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(290);
				match(COMMA);
				setState(291);
				argDecl();
				}
				}
				setState(296);
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
	public static class ArgDeclContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode COLON() { return getToken(M2tParser.COLON, 0); }
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public ArgDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitArgDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgDeclContext argDecl() throws RecognitionException {
		ArgDeclContext _localctx = new ArgDeclContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_argDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(297);
			identifier();
			setState(298);
			match(COLON);
			setState(299);
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
	public static class BodyContext extends ParserRuleContext {
		public List<BodyElementContext> bodyElement() {
			return getRuleContexts(BodyElementContext.class);
		}
		public BodyElementContext bodyElement(int i) {
			return getRuleContext(BodyElementContext.class,i);
		}
		public BodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_body; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BodyContext body() throws RecognitionException {
		BodyContext _localctx = new BodyContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_body);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(304);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(301);
					bodyElement();
					}
					} 
				}
				setState(306);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
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
	public static class BodyElementContext extends ParserRuleContext {
		public TextBlockContext textBlock() {
			return getRuleContext(TextBlockContext.class,0);
		}
		public InlineExpressionContext inlineExpression() {
			return getRuleContext(InlineExpressionContext.class,0);
		}
		public ForBlockContext forBlock() {
			return getRuleContext(ForBlockContext.class,0);
		}
		public IfBlockContext ifBlock() {
			return getRuleContext(IfBlockContext.class,0);
		}
		public LetBlockContext letBlock() {
			return getRuleContext(LetBlockContext.class,0);
		}
		public FileBlockContext fileBlock() {
			return getRuleContext(FileBlockContext.class,0);
		}
		public TraceBlockContext traceBlock() {
			return getRuleContext(TraceBlockContext.class,0);
		}
		public ProtectedBlockContext protectedBlock() {
			return getRuleContext(ProtectedBlockContext.class,0);
		}
		public TemplateInvocationContext templateInvocation() {
			return getRuleContext(TemplateInvocationContext.class,0);
		}
		public CommentBlockContext commentBlock() {
			return getRuleContext(CommentBlockContext.class,0);
		}
		public BodyElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bodyElement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitBodyElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BodyElementContext bodyElement() throws RecognitionException {
		BodyElementContext _localctx = new BodyElementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_bodyElement);
		try {
			setState(317);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(307);
				textBlock();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(308);
				inlineExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(309);
				forBlock();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(310);
				ifBlock();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(311);
				letBlock();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(312);
				fileBlock();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(313);
				traceBlock();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(314);
				protectedBlock();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(315);
				templateInvocation();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(316);
				commentBlock();
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
	public static class TextBlockContext extends ParserRuleContext {
		public List<TerminalNode> TEXT() { return getTokens(M2tParser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(M2tParser.TEXT, i);
		}
		public TextBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_textBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitTextBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TextBlockContext textBlock() throws RecognitionException {
		TextBlockContext _localctx = new TextBlockContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_textBlock);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(320); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(319);
					match(TEXT);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(322); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
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
	public static class InlineExpressionContext extends ParserRuleContext {
		public TerminalNode BLOCK_OPEN() { return getToken(M2tParser.BLOCK_OPEN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SLASH_CLOSE() { return getToken(M2tParser.SLASH_CLOSE, 0); }
		public InlineExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inlineExpression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitInlineExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InlineExpressionContext inlineExpression() throws RecognitionException {
		InlineExpressionContext _localctx = new InlineExpressionContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_inlineExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			match(BLOCK_OPEN);
			setState(325);
			expression(0);
			setState(326);
			match(SLASH_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForBlockContext extends ParserRuleContext {
		public List<TerminalNode> BLOCK_OPEN() { return getTokens(M2tParser.BLOCK_OPEN); }
		public TerminalNode BLOCK_OPEN(int i) {
			return getToken(M2tParser.BLOCK_OPEN, i);
		}
		public List<TerminalNode> KW_FOR() { return getTokens(M2tParser.KW_FOR); }
		public TerminalNode KW_FOR(int i) {
			return getToken(M2tParser.KW_FOR, i);
		}
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public ArgDeclContext argDecl() {
			return getRuleContext(ArgDeclContext.class,0);
		}
		public TerminalNode PIPE() { return getToken(M2tParser.PIPE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public List<TerminalNode> CLOSE() { return getTokens(M2tParser.CLOSE); }
		public TerminalNode CLOSE(int i) {
			return getToken(M2tParser.CLOSE, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode SLASH() { return getToken(M2tParser.SLASH, 0); }
		public BeforeClauseContext beforeClause() {
			return getRuleContext(BeforeClauseContext.class,0);
		}
		public SeparatorClauseContext separatorClause() {
			return getRuleContext(SeparatorClauseContext.class,0);
		}
		public AfterClauseContext afterClause() {
			return getRuleContext(AfterClauseContext.class,0);
		}
		public GuardContext guard() {
			return getRuleContext(GuardContext.class,0);
		}
		public InitSectionContext initSection() {
			return getRuleContext(InitSectionContext.class,0);
		}
		public ForBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitForBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForBlockContext forBlock() throws RecognitionException {
		ForBlockContext _localctx = new ForBlockContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_forBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(328);
			match(BLOCK_OPEN);
			setState(329);
			match(KW_FOR);
			setState(330);
			match(LPAREN);
			setState(331);
			argDecl();
			setState(332);
			match(PIPE);
			setState(333);
			expression(0);
			setState(334);
			match(RPAREN);
			setState(336);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_BEFORE) {
				{
				setState(335);
				beforeClause();
				}
			}

			setState(339);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_SEPARATOR) {
				{
				setState(338);
				separatorClause();
				}
			}

			setState(342);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AFTER) {
				{
				setState(341);
				afterClause();
				}
			}

			setState(345);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_GUARD) {
				{
				setState(344);
				guard();
				}
			}

			setState(348);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACE) {
				{
				setState(347);
				initSection();
				}
			}

			setState(350);
			match(CLOSE);
			setState(351);
			body();
			setState(352);
			match(BLOCK_OPEN);
			setState(353);
			match(SLASH);
			setState(354);
			match(KW_FOR);
			setState(355);
			match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BeforeClauseContext extends ParserRuleContext {
		public TerminalNode KW_BEFORE() { return getToken(M2tParser.KW_BEFORE, 0); }
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public BeforeClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_beforeClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitBeforeClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BeforeClauseContext beforeClause() throws RecognitionException {
		BeforeClauseContext _localctx = new BeforeClauseContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_beforeClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(357);
			match(KW_BEFORE);
			setState(358);
			match(LPAREN);
			setState(359);
			expression(0);
			setState(360);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SeparatorClauseContext extends ParserRuleContext {
		public TerminalNode KW_SEPARATOR() { return getToken(M2tParser.KW_SEPARATOR, 0); }
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public SeparatorClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_separatorClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitSeparatorClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SeparatorClauseContext separatorClause() throws RecognitionException {
		SeparatorClauseContext _localctx = new SeparatorClauseContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_separatorClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(362);
			match(KW_SEPARATOR);
			setState(363);
			match(LPAREN);
			setState(364);
			expression(0);
			setState(365);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AfterClauseContext extends ParserRuleContext {
		public TerminalNode KW_AFTER() { return getToken(M2tParser.KW_AFTER, 0); }
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public AfterClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_afterClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitAfterClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AfterClauseContext afterClause() throws RecognitionException {
		AfterClauseContext _localctx = new AfterClauseContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_afterClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(367);
			match(KW_AFTER);
			setState(368);
			match(LPAREN);
			setState(369);
			expression(0);
			setState(370);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfBlockContext extends ParserRuleContext {
		public List<TerminalNode> BLOCK_OPEN() { return getTokens(M2tParser.BLOCK_OPEN); }
		public TerminalNode BLOCK_OPEN(int i) {
			return getToken(M2tParser.BLOCK_OPEN, i);
		}
		public List<TerminalNode> KW_IF() { return getTokens(M2tParser.KW_IF); }
		public TerminalNode KW_IF(int i) {
			return getToken(M2tParser.KW_IF, i);
		}
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public List<TerminalNode> CLOSE() { return getTokens(M2tParser.CLOSE); }
		public TerminalNode CLOSE(int i) {
			return getToken(M2tParser.CLOSE, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode SLASH() { return getToken(M2tParser.SLASH, 0); }
		public List<ElseIfBlockContext> elseIfBlock() {
			return getRuleContexts(ElseIfBlockContext.class);
		}
		public ElseIfBlockContext elseIfBlock(int i) {
			return getRuleContext(ElseIfBlockContext.class,i);
		}
		public ElseBlockContext elseBlock() {
			return getRuleContext(ElseBlockContext.class,0);
		}
		public IfBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitIfBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfBlockContext ifBlock() throws RecognitionException {
		IfBlockContext _localctx = new IfBlockContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_ifBlock);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(372);
			match(BLOCK_OPEN);
			setState(373);
			match(KW_IF);
			setState(374);
			match(LPAREN);
			setState(375);
			expression(0);
			setState(376);
			match(RPAREN);
			setState(377);
			match(CLOSE);
			setState(378);
			body();
			setState(382);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(379);
					elseIfBlock();
					}
					} 
				}
				setState(384);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			}
			setState(386);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(385);
				elseBlock();
				}
				break;
			}
			setState(388);
			match(BLOCK_OPEN);
			setState(389);
			match(SLASH);
			setState(390);
			match(KW_IF);
			setState(391);
			match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElseIfBlockContext extends ParserRuleContext {
		public TerminalNode BLOCK_OPEN() { return getToken(M2tParser.BLOCK_OPEN, 0); }
		public TerminalNode KW_ELSEIF() { return getToken(M2tParser.KW_ELSEIF, 0); }
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public TerminalNode CLOSE() { return getToken(M2tParser.CLOSE, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public ElseIfBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseIfBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitElseIfBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseIfBlockContext elseIfBlock() throws RecognitionException {
		ElseIfBlockContext _localctx = new ElseIfBlockContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_elseIfBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
			match(BLOCK_OPEN);
			setState(394);
			match(KW_ELSEIF);
			setState(395);
			match(LPAREN);
			setState(396);
			expression(0);
			setState(397);
			match(RPAREN);
			setState(398);
			match(CLOSE);
			setState(399);
			body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElseBlockContext extends ParserRuleContext {
		public TerminalNode BLOCK_OPEN() { return getToken(M2tParser.BLOCK_OPEN, 0); }
		public TerminalNode KW_ELSE() { return getToken(M2tParser.KW_ELSE, 0); }
		public TerminalNode CLOSE() { return getToken(M2tParser.CLOSE, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public ElseBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitElseBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseBlockContext elseBlock() throws RecognitionException {
		ElseBlockContext _localctx = new ElseBlockContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_elseBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			match(BLOCK_OPEN);
			setState(402);
			match(KW_ELSE);
			setState(403);
			match(CLOSE);
			setState(404);
			body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LetBlockContext extends ParserRuleContext {
		public List<TerminalNode> BLOCK_OPEN() { return getTokens(M2tParser.BLOCK_OPEN); }
		public TerminalNode BLOCK_OPEN(int i) {
			return getToken(M2tParser.BLOCK_OPEN, i);
		}
		public List<TerminalNode> KW_LET() { return getTokens(M2tParser.KW_LET); }
		public TerminalNode KW_LET(int i) {
			return getToken(M2tParser.KW_LET, i);
		}
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public List<TerminalNode> CLOSE() { return getTokens(M2tParser.CLOSE); }
		public TerminalNode CLOSE(int i) {
			return getToken(M2tParser.CLOSE, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode SLASH() { return getToken(M2tParser.SLASH, 0); }
		public List<ElseLetBlockContext> elseLetBlock() {
			return getRuleContexts(ElseLetBlockContext.class);
		}
		public ElseLetBlockContext elseLetBlock(int i) {
			return getRuleContext(ElseLetBlockContext.class,i);
		}
		public ElseBlockContext elseBlock() {
			return getRuleContext(ElseBlockContext.class,0);
		}
		public LetBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitLetBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetBlockContext letBlock() throws RecognitionException {
		LetBlockContext _localctx = new LetBlockContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_letBlock);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(406);
			match(BLOCK_OPEN);
			setState(407);
			match(KW_LET);
			setState(408);
			variableDeclaration();
			setState(409);
			match(CLOSE);
			setState(410);
			body();
			setState(414);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(411);
					elseLetBlock();
					}
					} 
				}
				setState(416);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			}
			setState(418);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(417);
				elseBlock();
				}
				break;
			}
			setState(420);
			match(BLOCK_OPEN);
			setState(421);
			match(SLASH);
			setState(422);
			match(KW_LET);
			setState(423);
			match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElseLetBlockContext extends ParserRuleContext {
		public TerminalNode BLOCK_OPEN() { return getToken(M2tParser.BLOCK_OPEN, 0); }
		public TerminalNode KW_ELSELET() { return getToken(M2tParser.KW_ELSELET, 0); }
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public TerminalNode CLOSE() { return getToken(M2tParser.CLOSE, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public ElseLetBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseLetBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitElseLetBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseLetBlockContext elseLetBlock() throws RecognitionException {
		ElseLetBlockContext _localctx = new ElseLetBlockContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_elseLetBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(425);
			match(BLOCK_OPEN);
			setState(426);
			match(KW_ELSELET);
			setState(427);
			variableDeclaration();
			setState(428);
			match(CLOSE);
			setState(429);
			body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FileBlockContext extends ParserRuleContext {
		public List<TerminalNode> BLOCK_OPEN() { return getTokens(M2tParser.BLOCK_OPEN); }
		public TerminalNode BLOCK_OPEN(int i) {
			return getToken(M2tParser.BLOCK_OPEN, i);
		}
		public List<TerminalNode> KW_FILE() { return getTokens(M2tParser.KW_FILE); }
		public TerminalNode KW_FILE(int i) {
			return getToken(M2tParser.KW_FILE, i);
		}
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public List<TerminalNode> CLOSE() { return getTokens(M2tParser.CLOSE); }
		public TerminalNode CLOSE(int i) {
			return getToken(M2tParser.CLOSE, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode SLASH() { return getToken(M2tParser.SLASH, 0); }
		public List<TerminalNode> COMMA() { return getTokens(M2tParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(M2tParser.COMMA, i);
		}
		public FileBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fileBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitFileBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FileBlockContext fileBlock() throws RecognitionException {
		FileBlockContext _localctx = new FileBlockContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_fileBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(431);
			match(BLOCK_OPEN);
			setState(432);
			match(KW_FILE);
			setState(433);
			match(LPAREN);
			setState(434);
			expression(0);
			setState(441);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(435);
				match(COMMA);
				setState(436);
				expression(0);
				setState(439);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(437);
					match(COMMA);
					setState(438);
					expression(0);
					}
				}

				}
			}

			setState(443);
			match(RPAREN);
			setState(444);
			match(CLOSE);
			setState(445);
			body();
			setState(446);
			match(BLOCK_OPEN);
			setState(447);
			match(SLASH);
			setState(448);
			match(KW_FILE);
			setState(449);
			match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TraceBlockContext extends ParserRuleContext {
		public List<TerminalNode> BLOCK_OPEN() { return getTokens(M2tParser.BLOCK_OPEN); }
		public TerminalNode BLOCK_OPEN(int i) {
			return getToken(M2tParser.BLOCK_OPEN, i);
		}
		public List<TerminalNode> KW_TRACE() { return getTokens(M2tParser.KW_TRACE); }
		public TerminalNode KW_TRACE(int i) {
			return getToken(M2tParser.KW_TRACE, i);
		}
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public List<TerminalNode> CLOSE() { return getTokens(M2tParser.CLOSE); }
		public TerminalNode CLOSE(int i) {
			return getToken(M2tParser.CLOSE, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode SLASH() { return getToken(M2tParser.SLASH, 0); }
		public TraceBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_traceBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitTraceBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TraceBlockContext traceBlock() throws RecognitionException {
		TraceBlockContext _localctx = new TraceBlockContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_traceBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(451);
			match(BLOCK_OPEN);
			setState(452);
			match(KW_TRACE);
			setState(453);
			match(LPAREN);
			setState(454);
			expression(0);
			setState(455);
			match(RPAREN);
			setState(456);
			match(CLOSE);
			setState(457);
			body();
			setState(458);
			match(BLOCK_OPEN);
			setState(459);
			match(SLASH);
			setState(460);
			match(KW_TRACE);
			setState(461);
			match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProtectedBlockContext extends ParserRuleContext {
		public List<TerminalNode> BLOCK_OPEN() { return getTokens(M2tParser.BLOCK_OPEN); }
		public TerminalNode BLOCK_OPEN(int i) {
			return getToken(M2tParser.BLOCK_OPEN, i);
		}
		public List<TerminalNode> KW_PROTECTED() { return getTokens(M2tParser.KW_PROTECTED); }
		public TerminalNode KW_PROTECTED(int i) {
			return getToken(M2tParser.KW_PROTECTED, i);
		}
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public List<TerminalNode> CLOSE() { return getTokens(M2tParser.CLOSE); }
		public TerminalNode CLOSE(int i) {
			return getToken(M2tParser.CLOSE, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode SLASH() { return getToken(M2tParser.SLASH, 0); }
		public ProtectedBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_protectedBlock; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitProtectedBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProtectedBlockContext protectedBlock() throws RecognitionException {
		ProtectedBlockContext _localctx = new ProtectedBlockContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_protectedBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(463);
			match(BLOCK_OPEN);
			setState(464);
			match(KW_PROTECTED);
			setState(465);
			match(LPAREN);
			setState(466);
			expression(0);
			setState(467);
			match(RPAREN);
			setState(468);
			match(CLOSE);
			setState(469);
			body();
			setState(470);
			match(BLOCK_OPEN);
			setState(471);
			match(SLASH);
			setState(472);
			match(KW_PROTECTED);
			setState(473);
			match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateInvocationContext extends ParserRuleContext {
		public TerminalNode BLOCK_OPEN() { return getToken(M2tParser.BLOCK_OPEN, 0); }
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public TerminalNode SLASH_CLOSE() { return getToken(M2tParser.SLASH_CLOSE, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public BeforeClauseContext beforeClause() {
			return getRuleContext(BeforeClauseContext.class,0);
		}
		public SeparatorClauseContext separatorClause() {
			return getRuleContext(SeparatorClauseContext.class,0);
		}
		public AfterClauseContext afterClause() {
			return getRuleContext(AfterClauseContext.class,0);
		}
		public TerminalNode KW_SUPER() { return getToken(M2tParser.KW_SUPER, 0); }
		public TemplateInvocationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateInvocation; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitTemplateInvocation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplateInvocationContext templateInvocation() throws RecognitionException {
		TemplateInvocationContext _localctx = new TemplateInvocationContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_templateInvocation);
		int _la;
		try {
			setState(496);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(475);
				match(BLOCK_OPEN);
				setState(476);
				pathName();
				setState(477);
				match(LPAREN);
				setState(479);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1116189011824607296L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 131071985L) != 0)) {
					{
					setState(478);
					argumentList();
					}
				}

				setState(481);
				match(RPAREN);
				setState(483);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==KW_BEFORE) {
					{
					setState(482);
					beforeClause();
					}
				}

				setState(486);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==KW_SEPARATOR) {
					{
					setState(485);
					separatorClause();
					}
				}

				setState(489);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==KW_AFTER) {
					{
					setState(488);
					afterClause();
					}
				}

				setState(491);
				match(SLASH_CLOSE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(493);
				match(BLOCK_OPEN);
				setState(494);
				match(KW_SUPER);
				setState(495);
				match(SLASH_CLOSE);
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
		public TerminalNode KW_NOT() { return getToken(M2tParser.KW_NOT, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public NotExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitNotExp(this);
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
		public TerminalNode KW_AND() { return getToken(M2tParser.KW_AND, 0); }
		public AndExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitAndExp(this);
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
		public TerminalNode LT() { return getToken(M2tParser.LT, 0); }
		public TerminalNode GT() { return getToken(M2tParser.GT, 0); }
		public TerminalNode LE() { return getToken(M2tParser.LE, 0); }
		public TerminalNode GE() { return getToken(M2tParser.GE, 0); }
		public CompareExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitCompareExp(this);
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
		public TerminalNode STAR() { return getToken(M2tParser.STAR, 0); }
		public TerminalNode SLASH() { return getToken(M2tParser.SLASH, 0); }
		public MultExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitMultExp(this);
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
		public TerminalNode PLUS() { return getToken(M2tParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(M2tParser.MINUS, 0); }
		public AddExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitAddExp(this);
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
		public TerminalNode DOT() { return getToken(M2tParser.DOT, 0); }
		public TerminalNode SAFENAV() { return getToken(M2tParser.SAFENAV, 0); }
		public NavigationExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitNavigationExp(this);
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
		public TerminalNode KW_IMPLIES() { return getToken(M2tParser.KW_IMPLIES, 0); }
		public ImpliesExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitImpliesExp(this);
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
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitPrimaryExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryMinusExpContext extends ExpressionContext {
		public TerminalNode MINUS() { return getToken(M2tParser.MINUS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryMinusExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitUnaryMinusExp(this);
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
		public TerminalNode EQUALS() { return getToken(M2tParser.EQUALS, 0); }
		public TerminalNode NE() { return getToken(M2tParser.NE, 0); }
		public EqualityExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitEqualityExp(this);
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
		public TerminalNode KW_OR() { return getToken(M2tParser.KW_OR, 0); }
		public OrExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitOrExp(this);
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
		public TerminalNode KW_XOR() { return getToken(M2tParser.KW_XOR, 0); }
		public XorExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitXorExp(this);
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
		public TerminalNode ARROW() { return getToken(M2tParser.ARROW, 0); }
		public TerminalNode SAFEARROW() { return getToken(M2tParser.SAFEARROW, 0); }
		public ArrowExpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitArrowExp(this);
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
		int _startState = 72;
		enterRecursionRule(_localctx, 72, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(504);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_NOT:
				{
				_localctx = new NotExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(499);
				match(KW_NOT);
				setState(500);
				expression(11);
				}
				break;
			case MINUS:
				{
				_localctx = new UnaryMinusExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(501);
				match(MINUS);
				setState(502);
				expression(10);
				}
				break;
			case KW_MODULE:
			case KW_IMPORT:
			case KW_EXTENDS:
			case KW_TEMPLATE:
			case KW_QUERY:
			case KW_MACRO:
			case KW_FOR:
			case KW_IF:
			case KW_ELSEIF:
			case KW_ELSE:
			case KW_LET:
			case KW_ELSELET:
			case KW_FILE:
			case KW_TRACE:
			case KW_PROTECTED:
			case KW_COMMENT:
			case KW_OVERRIDES:
			case KW_PUBLIC:
			case KW_PRIVATE:
			case KW_BEFORE:
			case KW_SEPARATOR:
			case KW_AFTER:
			case KW_POST:
			case KW_SUPER:
			case KW_STDOUT:
			case LPAREN:
			case STAR:
			case KW_SELF:
			case KW_TRUE:
			case KW_FALSE:
			case KW_NULL:
			case KW_INVALID:
			case KW_SET:
			case KW_ORDEREDSET:
			case KW_BAG:
			case KW_SEQUENCE:
			case KW_COLLECTION:
			case KW_MAP:
			case KW_TUPLE:
			case KW_BOOLEAN:
			case KW_INTEGER:
			case KW_REAL:
			case KW_STRING:
			case KW_UNLIMITEDNATURAL:
			case KW_OCLANY:
			case KW_OCLVOID:
			case KW_OCLINVALID:
			case KW_OCLMESSAGE:
			case REAL_LITERAL:
			case INTEGER_LITERAL:
			case STRING_LITERAL:
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				{
				_localctx = new PrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(503);
				primaryExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(538);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,43,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(536);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
					case 1:
						{
						_localctx = new MultExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(506);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(507);
						((MultExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==SLASH || _la==STAR) ) {
							((MultExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(508);
						expression(10);
						}
						break;
					case 2:
						{
						_localctx = new AddExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(509);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(510);
						((AddExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((AddExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(511);
						expression(9);
						}
						break;
					case 3:
						{
						_localctx = new CompareExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(512);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(513);
						((CompareExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 16888498602639360L) != 0)) ) {
							((CompareExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(514);
						expression(8);
						}
						break;
					case 4:
						{
						_localctx = new EqualityExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(515);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(516);
						((EqualityExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQUALS || _la==NE) ) {
							((EqualityExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(517);
						expression(7);
						}
						break;
					case 5:
						{
						_localctx = new AndExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(518);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(519);
						match(KW_AND);
						setState(520);
						expression(6);
						}
						break;
					case 6:
						{
						_localctx = new OrExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(521);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(522);
						match(KW_OR);
						setState(523);
						expression(5);
						}
						break;
					case 7:
						{
						_localctx = new XorExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(524);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(525);
						match(KW_XOR);
						setState(526);
						expression(4);
						}
						break;
					case 8:
						{
						_localctx = new ImpliesExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(527);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(528);
						match(KW_IMPLIES);
						setState(529);
						expression(3);
						}
						break;
					case 9:
						{
						_localctx = new NavigationExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(530);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(531);
						_la = _input.LA(1);
						if ( !(_la==DOT || _la==SAFENAV) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(532);
						propertyOrCallSuffix();
						}
						break;
					case 10:
						{
						_localctx = new ArrowExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(533);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(534);
						_la = _input.LA(1);
						if ( !(_la==ARROW || _la==SAFEARROW) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(535);
						iteratorOrOperationCall();
						}
						break;
					}
					} 
				}
				setState(540);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,43,_ctx);
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
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitLiteralExp(this);
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
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitMapTypeExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SelfExpContext extends PrimaryExpressionContext {
		public TerminalNode KW_SELF() { return getToken(M2tParser.KW_SELF, 0); }
		public SelfExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitSelfExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OclLetExpContext extends PrimaryExpressionContext {
		public TerminalNode KW_LET() { return getToken(M2tParser.KW_LET, 0); }
		public List<LetBindingContext> letBinding() {
			return getRuleContexts(LetBindingContext.class);
		}
		public LetBindingContext letBinding(int i) {
			return getRuleContext(LetBindingContext.class,i);
		}
		public TerminalNode KW_IN() { return getToken(M2tParser.KW_IN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(M2tParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(M2tParser.COMMA, i);
		}
		public OclLetExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitOclLetExp(this);
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
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitCollectionTypeExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenExpContext extends PrimaryExpressionContext {
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public ParenExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitParenExp(this);
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
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitPrimitiveTypeExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OperationCallExpContext extends PrimaryExpressionContext {
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public OperationCallExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitOperationCallExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OclIfExpContext extends PrimaryExpressionContext {
		public TerminalNode KW_IF() { return getToken(M2tParser.KW_IF, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> KW_THEN() { return getTokens(M2tParser.KW_THEN); }
		public TerminalNode KW_THEN(int i) {
			return getToken(M2tParser.KW_THEN, i);
		}
		public TerminalNode KW_ELSE() { return getToken(M2tParser.KW_ELSE, 0); }
		public TerminalNode KW_ENDIF() { return getToken(M2tParser.KW_ENDIF, 0); }
		public List<TerminalNode> KW_ELSEIF() { return getTokens(M2tParser.KW_ELSEIF); }
		public TerminalNode KW_ELSEIF(int i) {
			return getToken(M2tParser.KW_ELSEIF, i);
		}
		public OclIfExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitOclIfExp(this);
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
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitTupleTypeExp(this);
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
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitPathNameExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
		PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_primaryExpression);
		int _la;
		try {
			setState(589);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				_localctx = new ParenExpContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(541);
				match(LPAREN);
				setState(542);
				expression(0);
				setState(543);
				match(RPAREN);
				}
				break;
			case 2:
				_localctx = new SelfExpContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(545);
				match(KW_SELF);
				}
				break;
			case 3:
				_localctx = new OclIfExpContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(546);
				match(KW_IF);
				setState(547);
				expression(0);
				setState(548);
				match(KW_THEN);
				setState(549);
				expression(0);
				setState(557);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==KW_ELSEIF) {
					{
					{
					setState(550);
					match(KW_ELSEIF);
					setState(551);
					expression(0);
					setState(552);
					match(KW_THEN);
					setState(553);
					expression(0);
					}
					}
					setState(559);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(560);
				match(KW_ELSE);
				setState(561);
				expression(0);
				setState(562);
				match(KW_ENDIF);
				}
				break;
			case 4:
				_localctx = new OclLetExpContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(564);
				match(KW_LET);
				setState(565);
				letBinding();
				setState(570);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(566);
					match(COMMA);
					setState(567);
					letBinding();
					}
					}
					setState(572);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(573);
				match(KW_IN);
				setState(574);
				expression(0);
				}
				break;
			case 5:
				_localctx = new LiteralExpContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(576);
				literalExpression();
				}
				break;
			case 6:
				_localctx = new OperationCallExpContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(577);
				pathName();
				setState(578);
				match(LPAREN);
				setState(580);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1116189011824607296L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 131071985L) != 0)) {
					{
					setState(579);
					argumentList();
					}
				}

				setState(582);
				match(RPAREN);
				}
				break;
			case 7:
				_localctx = new PrimitiveTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(584);
				primitiveType();
				}
				break;
			case 8:
				_localctx = new CollectionTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(585);
				collectionType();
				}
				break;
			case 9:
				_localctx = new MapTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(586);
				mapType();
				}
				break;
			case 10:
				_localctx = new TupleTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(587);
				tupleType();
				}
				break;
			case 11:
				_localctx = new PathNameExpContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(588);
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
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public PathNameContext pathName() {
			return getRuleContext(PathNameContext.class,0);
		}
		public TerminalNode COLONCOLON() { return getToken(M2tParser.COLONCOLON, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public DotCallSuffixContext(PropertyOrCallSuffixContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitDotCallSuffix(this);
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
		public TerminalNode COLONCOLON() { return getToken(M2tParser.COLONCOLON, 0); }
		public PropertySuffixContext(PropertyOrCallSuffixContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitPropertySuffix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyOrCallSuffixContext propertyOrCallSuffix() throws RecognitionException {
		PropertyOrCallSuffixContext _localctx = new PropertyOrCallSuffixContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_propertyOrCallSuffix);
		int _la;
		try {
			setState(609);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				_localctx = new DotCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(594);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
				case 1:
					{
					setState(591);
					pathName();
					setState(592);
					match(COLONCOLON);
					}
					break;
				}
				setState(596);
				identifier();
				setState(597);
				match(LPAREN);
				setState(599);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1116189011824607296L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 131071985L) != 0)) {
					{
					setState(598);
					argumentList();
					}
				}

				setState(601);
				match(RPAREN);
				}
				break;
			case 2:
				_localctx = new PropertySuffixContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(606);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
				case 1:
					{
					setState(603);
					pathName();
					setState(604);
					match(COLONCOLON);
					}
					break;
				}
				setState(608);
				identifier();
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
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public CollectionOperationCallContext(IteratorOrOperationCallContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitCollectionOperationCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IterateCallContext extends IteratorOrOperationCallContext {
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public TerminalNode SEMICOLON() { return getToken(M2tParser.SEMICOLON, 0); }
		public TerminalNode EQUALS() { return getToken(M2tParser.EQUALS, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode PIPE() { return getToken(M2tParser.PIPE, 0); }
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public List<TerminalNode> COLON() { return getTokens(M2tParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(M2tParser.COLON, i);
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
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitIterateCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IteratorCallContext extends IteratorOrOperationCallContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public IteratorVariablesContext iteratorVariables() {
			return getRuleContext(IteratorVariablesContext.class,0);
		}
		public TerminalNode PIPE() { return getToken(M2tParser.PIPE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public IteratorCallContext(IteratorOrOperationCallContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitIteratorCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IteratorOrOperationCallContext iteratorOrOperationCall() throws RecognitionException {
		IteratorOrOperationCallContext _localctx = new IteratorOrOperationCallContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_iteratorOrOperationCall);
		int _la;
		try {
			setState(644);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				_localctx = new IteratorCallContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(611);
				identifier();
				setState(612);
				match(LPAREN);
				setState(613);
				iteratorVariables();
				setState(614);
				match(PIPE);
				setState(615);
				expression(0);
				setState(616);
				match(RPAREN);
				}
				break;
			case 2:
				_localctx = new IterateCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(618);
				identifier();
				setState(619);
				match(LPAREN);
				setState(620);
				identifier();
				setState(623);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(621);
					match(COLON);
					setState(622);
					typeExpression();
					}
				}

				setState(625);
				match(SEMICOLON);
				setState(626);
				identifier();
				setState(629);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(627);
					match(COLON);
					setState(628);
					typeExpression();
					}
				}

				setState(631);
				match(EQUALS);
				setState(632);
				expression(0);
				setState(633);
				match(PIPE);
				setState(634);
				expression(0);
				setState(635);
				match(RPAREN);
				}
				break;
			case 3:
				_localctx = new CollectionOperationCallContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(637);
				identifier();
				setState(638);
				match(LPAREN);
				setState(640);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1116189011824607296L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 131071985L) != 0)) {
					{
					setState(639);
					argumentList();
					}
				}

				setState(642);
				match(RPAREN);
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
		public List<TerminalNode> COLON() { return getTokens(M2tParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(M2tParser.COLON, i);
		}
		public List<TypeExpressionContext> typeExpression() {
			return getRuleContexts(TypeExpressionContext.class);
		}
		public TypeExpressionContext typeExpression(int i) {
			return getRuleContext(TypeExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(M2tParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(M2tParser.COMMA, i);
		}
		public IteratorVariablesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iteratorVariables; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitIteratorVariables(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IteratorVariablesContext iteratorVariables() throws RecognitionException {
		IteratorVariablesContext _localctx = new IteratorVariablesContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_iteratorVariables);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(646);
			identifier();
			setState(649);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(647);
				match(COLON);
				setState(648);
				typeExpression();
				}
			}

			setState(659);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(651);
				match(COMMA);
				setState(652);
				identifier();
				setState(655);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(653);
					match(COLON);
					setState(654);
					typeExpression();
					}
				}

				}
				}
				setState(661);
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
		public List<TerminalNode> COMMA() { return getTokens(M2tParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(M2tParser.COMMA, i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(662);
			expression(0);
			setState(667);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(663);
				match(COMMA);
				setState(664);
				expression(0);
				}
				}
				setState(669);
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
		public TerminalNode EQUALS() { return getToken(M2tParser.EQUALS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode COLON() { return getToken(M2tParser.COLON, 0); }
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public LetBindingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letBinding; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitLetBinding(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetBindingContext letBinding() throws RecognitionException {
		LetBindingContext _localctx = new LetBindingContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_letBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(670);
			identifier();
			setState(673);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(671);
				match(COLON);
				setState(672);
				typeExpression();
				}
			}

			setState(675);
			match(EQUALS);
			setState(676);
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
		public TerminalNode REAL_LITERAL() { return getToken(M2tParser.REAL_LITERAL, 0); }
		public RealLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitRealLiteral(this);
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
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitCollectionLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TrueLiteralContext extends LiteralExpressionContext {
		public TerminalNode KW_TRUE() { return getToken(M2tParser.KW_TRUE, 0); }
		public TrueLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitTrueLiteral(this);
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
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitTupleLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InvalidLiteralContext extends LiteralExpressionContext {
		public TerminalNode KW_INVALID() { return getToken(M2tParser.KW_INVALID, 0); }
		public InvalidLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitInvalidLiteral(this);
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
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitMapLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NullLiteralContext extends LiteralExpressionContext {
		public TerminalNode KW_NULL() { return getToken(M2tParser.KW_NULL, 0); }
		public NullLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitNullLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnlimitedNaturalLiteralContext extends LiteralExpressionContext {
		public TerminalNode STAR() { return getToken(M2tParser.STAR, 0); }
		public UnlimitedNaturalLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitUnlimitedNaturalLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntegerLiteralContext extends LiteralExpressionContext {
		public TerminalNode INTEGER_LITERAL() { return getToken(M2tParser.INTEGER_LITERAL, 0); }
		public IntegerLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitIntegerLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringLitContext extends LiteralExpressionContext {
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public StringLitContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitStringLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FalseLiteralContext extends LiteralExpressionContext {
		public TerminalNode KW_FALSE() { return getToken(M2tParser.KW_FALSE, 0); }
		public FalseLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitFalseLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralExpressionContext literalExpression() throws RecognitionException {
		LiteralExpressionContext _localctx = new LiteralExpressionContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_literalExpression);
		try {
			setState(689);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER_LITERAL:
				_localctx = new IntegerLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(678);
				match(INTEGER_LITERAL);
				}
				break;
			case REAL_LITERAL:
				_localctx = new RealLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(679);
				match(REAL_LITERAL);
				}
				break;
			case STRING_LITERAL:
				_localctx = new StringLitContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(680);
				stringLiteral();
				}
				break;
			case KW_TRUE:
				_localctx = new TrueLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(681);
				match(KW_TRUE);
				}
				break;
			case KW_FALSE:
				_localctx = new FalseLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(682);
				match(KW_FALSE);
				}
				break;
			case KW_NULL:
				_localctx = new NullLiteralContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(683);
				match(KW_NULL);
				}
				break;
			case KW_INVALID:
				_localctx = new InvalidLiteralContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(684);
				match(KW_INVALID);
				}
				break;
			case STAR:
				_localctx = new UnlimitedNaturalLiteralContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(685);
				match(STAR);
				}
				break;
			case KW_SET:
			case KW_ORDEREDSET:
			case KW_BAG:
			case KW_SEQUENCE:
			case KW_COLLECTION:
				_localctx = new CollectionLitContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(686);
				collectionLiteral();
				}
				break;
			case KW_TUPLE:
				_localctx = new TupleLitContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(687);
				tupleLiteral();
				}
				break;
			case KW_MAP:
				_localctx = new MapLitContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(688);
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
	public static class StringLiteralContext extends ParserRuleContext {
		public List<TerminalNode> STRING_LITERAL() { return getTokens(M2tParser.STRING_LITERAL); }
		public TerminalNode STRING_LITERAL(int i) {
			return getToken(M2tParser.STRING_LITERAL, i);
		}
		public StringLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_stringLiteral);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(692); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(691);
					match(STRING_LITERAL);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(694); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,62,_ctx);
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
		public TerminalNode LBRACE() { return getToken(M2tParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(M2tParser.RBRACE, 0); }
		public List<CollectionLiteralPartContext> collectionLiteralPart() {
			return getRuleContexts(CollectionLiteralPartContext.class);
		}
		public CollectionLiteralPartContext collectionLiteralPart(int i) {
			return getRuleContext(CollectionLiteralPartContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(M2tParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(M2tParser.COMMA, i);
		}
		public CollectionLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionLiteral; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitCollectionLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionLiteralContext collectionLiteral() throws RecognitionException {
		CollectionLiteralContext _localctx = new CollectionLiteralContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_collectionLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(696);
			collectionKind();
			setState(697);
			match(LBRACE);
			setState(706);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1116189011824607296L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 131071985L) != 0)) {
				{
				setState(698);
				collectionLiteralPart();
				setState(703);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(699);
					match(COMMA);
					setState(700);
					collectionLiteralPart();
					}
					}
					setState(705);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(708);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode KW_SET() { return getToken(M2tParser.KW_SET, 0); }
		public TerminalNode KW_ORDEREDSET() { return getToken(M2tParser.KW_ORDEREDSET, 0); }
		public TerminalNode KW_BAG() { return getToken(M2tParser.KW_BAG, 0); }
		public TerminalNode KW_SEQUENCE() { return getToken(M2tParser.KW_SEQUENCE, 0); }
		public TerminalNode KW_COLLECTION() { return getToken(M2tParser.KW_COLLECTION, 0); }
		public CollectionKindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionKind; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitCollectionKind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionKindContext collectionKind() throws RecognitionException {
		CollectionKindContext _localctx = new CollectionKindContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_collectionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(710);
			_la = _input.LA(1);
			if ( !(((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 31L) != 0)) ) {
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
		public TerminalNode DOTDOT() { return getToken(M2tParser.DOTDOT, 0); }
		public CollectionLiteralPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionLiteralPart; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitCollectionLiteralPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionLiteralPartContext collectionLiteralPart() throws RecognitionException {
		CollectionLiteralPartContext _localctx = new CollectionLiteralPartContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_collectionLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(712);
			expression(0);
			setState(715);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOTDOT) {
				{
				setState(713);
				match(DOTDOT);
				setState(714);
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
		public TerminalNode KW_TUPLE() { return getToken(M2tParser.KW_TUPLE, 0); }
		public TerminalNode LBRACE() { return getToken(M2tParser.LBRACE, 0); }
		public List<TupleLiteralPartContext> tupleLiteralPart() {
			return getRuleContexts(TupleLiteralPartContext.class);
		}
		public TupleLiteralPartContext tupleLiteralPart(int i) {
			return getRuleContext(TupleLiteralPartContext.class,i);
		}
		public TerminalNode RBRACE() { return getToken(M2tParser.RBRACE, 0); }
		public List<TerminalNode> COMMA() { return getTokens(M2tParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(M2tParser.COMMA, i);
		}
		public TupleLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleLiteral; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitTupleLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleLiteralContext tupleLiteral() throws RecognitionException {
		TupleLiteralContext _localctx = new TupleLiteralContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_tupleLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(717);
			match(KW_TUPLE);
			setState(718);
			match(LBRACE);
			setState(719);
			tupleLiteralPart();
			setState(724);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(720);
				match(COMMA);
				setState(721);
				tupleLiteralPart();
				}
				}
				setState(726);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(727);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode EQUALS() { return getToken(M2tParser.EQUALS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode COLON() { return getToken(M2tParser.COLON, 0); }
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public TupleLiteralPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleLiteralPart; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitTupleLiteralPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleLiteralPartContext tupleLiteralPart() throws RecognitionException {
		TupleLiteralPartContext _localctx = new TupleLiteralPartContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_tupleLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(729);
			identifier();
			setState(732);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(730);
				match(COLON);
				setState(731);
				typeExpression();
				}
			}

			setState(734);
			match(EQUALS);
			setState(735);
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
		public TerminalNode KW_MAP() { return getToken(M2tParser.KW_MAP, 0); }
		public TerminalNode LBRACE() { return getToken(M2tParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(M2tParser.RBRACE, 0); }
		public List<MapLiteralPartContext> mapLiteralPart() {
			return getRuleContexts(MapLiteralPartContext.class);
		}
		public MapLiteralPartContext mapLiteralPart(int i) {
			return getRuleContext(MapLiteralPartContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(M2tParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(M2tParser.COMMA, i);
		}
		public MapLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapLiteral; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitMapLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapLiteralContext mapLiteral() throws RecognitionException {
		MapLiteralContext _localctx = new MapLiteralContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_mapLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(737);
			match(KW_MAP);
			setState(738);
			match(LBRACE);
			setState(747);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1116189011824607296L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 131071985L) != 0)) {
				{
				setState(739);
				mapLiteralPart();
				setState(744);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(740);
					match(COMMA);
					setState(741);
					mapLiteralPart();
					}
					}
					setState(746);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(749);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode KW_WITH() { return getToken(M2tParser.KW_WITH, 0); }
		public TerminalNode LARROW() { return getToken(M2tParser.LARROW, 0); }
		public MapLiteralPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapLiteralPart; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitMapLiteralPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapLiteralPartContext mapLiteralPart() throws RecognitionException {
		MapLiteralPartContext _localctx = new MapLiteralPartContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_mapLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(751);
			expression(0);
			setState(752);
			_la = _input.LA(1);
			if ( !(_la==KW_WITH || _la==LARROW) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(753);
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
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitTypeExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeExpressionContext typeExpression() throws RecognitionException {
		TypeExpressionContext _localctx = new TypeExpressionContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_typeExpression);
		try {
			setState(760);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_BOOLEAN:
			case KW_INTEGER:
			case KW_REAL:
			case KW_STRING:
			case KW_UNLIMITEDNATURAL:
			case KW_OCLANY:
			case KW_OCLVOID:
			case KW_OCLINVALID:
			case KW_OCLMESSAGE:
				enterOuterAlt(_localctx, 1);
				{
				setState(755);
				primitiveType();
				}
				break;
			case KW_SET:
			case KW_ORDEREDSET:
			case KW_BAG:
			case KW_SEQUENCE:
			case KW_COLLECTION:
				enterOuterAlt(_localctx, 2);
				{
				setState(756);
				collectionType();
				}
				break;
			case KW_MAP:
				enterOuterAlt(_localctx, 3);
				{
				setState(757);
				mapType();
				}
				break;
			case KW_TUPLE:
				enterOuterAlt(_localctx, 4);
				{
				setState(758);
				tupleType();
				}
				break;
			case KW_MODULE:
			case KW_IMPORT:
			case KW_EXTENDS:
			case KW_TEMPLATE:
			case KW_QUERY:
			case KW_MACRO:
			case KW_FOR:
			case KW_IF:
			case KW_ELSEIF:
			case KW_ELSE:
			case KW_LET:
			case KW_ELSELET:
			case KW_FILE:
			case KW_TRACE:
			case KW_PROTECTED:
			case KW_COMMENT:
			case KW_OVERRIDES:
			case KW_PUBLIC:
			case KW_PRIVATE:
			case KW_BEFORE:
			case KW_SEPARATOR:
			case KW_AFTER:
			case KW_POST:
			case KW_SUPER:
			case KW_STDOUT:
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 5);
				{
				setState(759);
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
		public TerminalNode KW_BOOLEAN() { return getToken(M2tParser.KW_BOOLEAN, 0); }
		public TerminalNode KW_INTEGER() { return getToken(M2tParser.KW_INTEGER, 0); }
		public TerminalNode KW_REAL() { return getToken(M2tParser.KW_REAL, 0); }
		public TerminalNode KW_STRING() { return getToken(M2tParser.KW_STRING, 0); }
		public TerminalNode KW_UNLIMITEDNATURAL() { return getToken(M2tParser.KW_UNLIMITEDNATURAL, 0); }
		public TerminalNode KW_OCLANY() { return getToken(M2tParser.KW_OCLANY, 0); }
		public TerminalNode KW_OCLVOID() { return getToken(M2tParser.KW_OCLVOID, 0); }
		public TerminalNode KW_OCLINVALID() { return getToken(M2tParser.KW_OCLINVALID, 0); }
		public TerminalNode KW_OCLMESSAGE() { return getToken(M2tParser.KW_OCLMESSAGE, 0); }
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitPrimitiveType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(762);
			_la = _input.LA(1);
			if ( !(((((_la - 75)) & ~0x3f) == 0 && ((1L << (_la - 75)) & 511L) != 0)) ) {
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
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public CollectionTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitCollectionType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionTypeContext collectionType() throws RecognitionException {
		CollectionTypeContext _localctx = new CollectionTypeContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_collectionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(764);
			collectionKind();
			setState(765);
			match(LPAREN);
			setState(766);
			typeExpression();
			setState(767);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode KW_MAP() { return getToken(M2tParser.KW_MAP, 0); }
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public List<TypeExpressionContext> typeExpression() {
			return getRuleContexts(TypeExpressionContext.class);
		}
		public TypeExpressionContext typeExpression(int i) {
			return getRuleContext(TypeExpressionContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(M2tParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public MapTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitMapType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapTypeContext mapType() throws RecognitionException {
		MapTypeContext _localctx = new MapTypeContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(769);
			match(KW_MAP);
			setState(770);
			match(LPAREN);
			setState(771);
			typeExpression();
			setState(772);
			match(COMMA);
			setState(773);
			typeExpression();
			setState(774);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode KW_TUPLE() { return getToken(M2tParser.KW_TUPLE, 0); }
		public TerminalNode LPAREN() { return getToken(M2tParser.LPAREN, 0); }
		public List<TupleTypePartContext> tupleTypePart() {
			return getRuleContexts(TupleTypePartContext.class);
		}
		public TupleTypePartContext tupleTypePart(int i) {
			return getRuleContext(TupleTypePartContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(M2tParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(M2tParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(M2tParser.COMMA, i);
		}
		public TupleTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitTupleType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleTypeContext tupleType() throws RecognitionException {
		TupleTypeContext _localctx = new TupleTypeContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_tupleType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(776);
			match(KW_TUPLE);
			setState(777);
			match(LPAREN);
			setState(778);
			tupleTypePart();
			setState(783);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(779);
				match(COMMA);
				setState(780);
				tupleTypePart();
				}
				}
				setState(785);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(786);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode COLON() { return getToken(M2tParser.COLON, 0); }
		public TypeExpressionContext typeExpression() {
			return getRuleContext(TypeExpressionContext.class,0);
		}
		public TupleTypePartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleTypePart; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitTupleTypePart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleTypePartContext tupleTypePart() throws RecognitionException {
		TupleTypePartContext _localctx = new TupleTypePartContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_tupleTypePart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(788);
			identifier();
			setState(789);
			match(COLON);
			setState(790);
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
		public List<TerminalNode> COLONCOLON() { return getTokens(M2tParser.COLONCOLON); }
		public TerminalNode COLONCOLON(int i) {
			return getToken(M2tParser.COLONCOLON, i);
		}
		public PathNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitPathName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathNameContext pathName() throws RecognitionException {
		PathNameContext _localctx = new PathNameContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_pathName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(792);
			identifier();
			setState(797);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,72,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(793);
					match(COLONCOLON);
					setState(794);
					identifier();
					}
					} 
				}
				setState(799);
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
	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(M2tParser.IDENTIFIER, 0); }
		public TerminalNode ESCAPED_IDENTIFIER() { return getToken(M2tParser.ESCAPED_IDENTIFIER, 0); }
		public TerminalNode KW_MODULE() { return getToken(M2tParser.KW_MODULE, 0); }
		public TerminalNode KW_IMPORT() { return getToken(M2tParser.KW_IMPORT, 0); }
		public TerminalNode KW_EXTENDS() { return getToken(M2tParser.KW_EXTENDS, 0); }
		public TerminalNode KW_TEMPLATE() { return getToken(M2tParser.KW_TEMPLATE, 0); }
		public TerminalNode KW_QUERY() { return getToken(M2tParser.KW_QUERY, 0); }
		public TerminalNode KW_MACRO() { return getToken(M2tParser.KW_MACRO, 0); }
		public TerminalNode KW_FOR() { return getToken(M2tParser.KW_FOR, 0); }
		public TerminalNode KW_IF() { return getToken(M2tParser.KW_IF, 0); }
		public TerminalNode KW_ELSEIF() { return getToken(M2tParser.KW_ELSEIF, 0); }
		public TerminalNode KW_ELSE() { return getToken(M2tParser.KW_ELSE, 0); }
		public TerminalNode KW_LET() { return getToken(M2tParser.KW_LET, 0); }
		public TerminalNode KW_ELSELET() { return getToken(M2tParser.KW_ELSELET, 0); }
		public TerminalNode KW_FILE() { return getToken(M2tParser.KW_FILE, 0); }
		public TerminalNode KW_TRACE() { return getToken(M2tParser.KW_TRACE, 0); }
		public TerminalNode KW_PROTECTED() { return getToken(M2tParser.KW_PROTECTED, 0); }
		public TerminalNode KW_COMMENT() { return getToken(M2tParser.KW_COMMENT, 0); }
		public TerminalNode KW_PUBLIC() { return getToken(M2tParser.KW_PUBLIC, 0); }
		public TerminalNode KW_PRIVATE() { return getToken(M2tParser.KW_PRIVATE, 0); }
		public TerminalNode KW_OVERRIDES() { return getToken(M2tParser.KW_OVERRIDES, 0); }
		public TerminalNode KW_BEFORE() { return getToken(M2tParser.KW_BEFORE, 0); }
		public TerminalNode KW_SEPARATOR() { return getToken(M2tParser.KW_SEPARATOR, 0); }
		public TerminalNode KW_AFTER() { return getToken(M2tParser.KW_AFTER, 0); }
		public TerminalNode KW_POST() { return getToken(M2tParser.KW_POST, 0); }
		public TerminalNode KW_SUPER() { return getToken(M2tParser.KW_SUPER, 0); }
		public TerminalNode KW_STDOUT() { return getToken(M2tParser.KW_STDOUT, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof M2tParserVisitor ) return ((M2tParserVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_identifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(800);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 4026531776L) != 0) || _la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) ) {
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
		case 36:
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
		"\u0004\u0001]\u0323\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"7\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007;\u0001"+
		"\u0000\u0003\u0000z\b\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0005"+
		"\u0000\u007f\b\u0000\n\u0000\f\u0000\u0082\t\u0000\u0001\u0000\u0001\u0000"+
		"\u0005\u0000\u0086\b\u0000\n\u0000\f\u0000\u0089\t\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0003\u0001\u0094\b\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u009b\b\u0002\n\u0002\f\u0002"+
		"\u009e\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003"+
		"\u00a4\b\u0003\n\u0003\f\u0003\u00a7\t\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0003\u0005\u00b2\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0003\b\u00c8\b\b\u0001"+
		"\b\u0001\b\u0001\b\u0003\b\u00cd\b\b\u0001\b\u0001\b\u0003\b\u00d1\b\b"+
		"\u0001\b\u0003\b\u00d4\b\b\u0001\b\u0003\b\u00d7\b\b\u0001\b\u0003\b\u00da"+
		"\b\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0005\n\u00e2\b\n"+
		"\n\n\f\n\u00e5\t\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r"+
		"\u0001\r\u0004\r\u00f5\b\r\u000b\r\f\r\u00f6\u0001\r\u0001\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u0100\b\u000e"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u0105\b\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0003\u000f\u010a\b\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0003\u0010\u0118\b\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011"+
		"\u0125\b\u0011\n\u0011\f\u0011\u0128\t\u0011\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0013\u0005\u0013\u012f\b\u0013\n\u0013\f\u0013"+
		"\u0132\t\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014"+
		"\u013e\b\u0014\u0001\u0015\u0004\u0015\u0141\b\u0015\u000b\u0015\f\u0015"+
		"\u0142\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0003\u0017\u0151\b\u0017\u0001\u0017\u0003\u0017\u0154\b\u0017"+
		"\u0001\u0017\u0003\u0017\u0157\b\u0017\u0001\u0017\u0003\u0017\u015a\b"+
		"\u0017\u0001\u0017\u0003\u0017\u015d\b\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0005\u001b\u017d\b\u001b\n"+
		"\u001b\f\u001b\u0180\t\u001b\u0001\u001b\u0003\u001b\u0183\b\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0005"+
		"\u001e\u019d\b\u001e\n\u001e\f\u001e\u01a0\t\u001e\u0001\u001e\u0003\u001e"+
		"\u01a3\b\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0003 \u01b8"+
		"\b \u0003 \u01ba\b \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001"+
		" \u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001"+
		"!\u0001!\u0001!\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001"+
		"\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001#\u0001#\u0001#\u0001"+
		"#\u0003#\u01e0\b#\u0001#\u0001#\u0003#\u01e4\b#\u0001#\u0003#\u01e7\b"+
		"#\u0001#\u0003#\u01ea\b#\u0001#\u0001#\u0001#\u0001#\u0001#\u0003#\u01f1"+
		"\b#\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0003$\u01f9\b$\u0001$\u0001"+
		"$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001"+
		"$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001"+
		"$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0005$\u0219"+
		"\b$\n$\f$\u021c\t$\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001"+
		"%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0005%\u022c\b%\n%\f%\u022f"+
		"\t%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0005%\u0239"+
		"\b%\n%\f%\u023c\t%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0003"+
		"%\u0245\b%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0003%\u024e"+
		"\b%\u0001&\u0001&\u0001&\u0003&\u0253\b&\u0001&\u0001&\u0001&\u0003&\u0258"+
		"\b&\u0001&\u0001&\u0001&\u0001&\u0001&\u0003&\u025f\b&\u0001&\u0003&\u0262"+
		"\b&\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0003\'\u0270\b\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0003\'\u0276\b\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0003\'\u0281\b\'\u0001\'\u0001\'\u0003\'\u0285\b\'"+
		"\u0001(\u0001(\u0001(\u0003(\u028a\b(\u0001(\u0001(\u0001(\u0001(\u0003"+
		"(\u0290\b(\u0005(\u0292\b(\n(\f(\u0295\t(\u0001)\u0001)\u0001)\u0005)"+
		"\u029a\b)\n)\f)\u029d\t)\u0001*\u0001*\u0001*\u0003*\u02a2\b*\u0001*\u0001"+
		"*\u0001*\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001"+
		"+\u0001+\u0001+\u0003+\u02b2\b+\u0001,\u0004,\u02b5\b,\u000b,\f,\u02b6"+
		"\u0001-\u0001-\u0001-\u0001-\u0001-\u0005-\u02be\b-\n-\f-\u02c1\t-\u0003"+
		"-\u02c3\b-\u0001-\u0001-\u0001.\u0001.\u0001/\u0001/\u0001/\u0003/\u02cc"+
		"\b/\u00010\u00010\u00010\u00010\u00010\u00050\u02d3\b0\n0\f0\u02d6\t0"+
		"\u00010\u00010\u00011\u00011\u00011\u00031\u02dd\b1\u00011\u00011\u0001"+
		"1\u00012\u00012\u00012\u00012\u00012\u00052\u02e7\b2\n2\f2\u02ea\t2\u0003"+
		"2\u02ec\b2\u00012\u00012\u00013\u00013\u00013\u00013\u00014\u00014\u0001"+
		"4\u00014\u00014\u00034\u02f9\b4\u00015\u00015\u00016\u00016\u00016\u0001"+
		"6\u00016\u00017\u00017\u00017\u00017\u00017\u00017\u00017\u00018\u0001"+
		"8\u00018\u00018\u00018\u00058\u030e\b8\n8\f8\u0311\t8\u00018\u00018\u0001"+
		"9\u00019\u00019\u00019\u0001:\u0001:\u0001:\u0005:\u031c\b:\n:\f:\u031f"+
		"\t:\u0001;\u0001;\u0001;\u0000\u0001H<\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDF"+
		"HJLNPRTVXZ\\^`bdfhjlnprtv\u0000\u000b\u0002\u0000\u0014\u0014\u0017\u0018"+
		"\u0002\u0000\u0005\u0005//\u0001\u000001\u0001\u000025\u0002\u0000))6"+
		"6\u0001\u0000*+\u0001\u0000,-\u0001\u0000DH\u0001\u0000TU\u0001\u0000"+
		"KS\u0003\u0000\u0006\u001b\u001d\u001fYZ\u0358\u0000y\u0001\u0000\u0000"+
		"\u0000\u0002\u008c\u0001\u0000\u0000\u0000\u0004\u0097\u0001\u0000\u0000"+
		"\u0000\u0006\u009f\u0001\u0000\u0000\u0000\b\u00a8\u0001\u0000\u0000\u0000"+
		"\n\u00b1\u0001\u0000\u0000\u0000\f\u00b3\u0001\u0000\u0000\u0000\u000e"+
		"\u00bc\u0001\u0000\u0000\u0000\u0010\u00c7\u0001\u0000\u0000\u0000\u0012"+
		"\u00db\u0001\u0000\u0000\u0000\u0014\u00dd\u0001\u0000\u0000\u0000\u0016"+
		"\u00e6\u0001\u0000\u0000\u0000\u0018\u00eb\u0001\u0000\u0000\u0000\u001a"+
		"\u00f0\u0001\u0000\u0000\u0000\u001c\u00fa\u0001\u0000\u0000\u0000\u001e"+
		"\u0101\u0001\u0000\u0000\u0000 \u0112\u0001\u0000\u0000\u0000\"\u0121"+
		"\u0001\u0000\u0000\u0000$\u0129\u0001\u0000\u0000\u0000&\u0130\u0001\u0000"+
		"\u0000\u0000(\u013d\u0001\u0000\u0000\u0000*\u0140\u0001\u0000\u0000\u0000"+
		",\u0144\u0001\u0000\u0000\u0000.\u0148\u0001\u0000\u0000\u00000\u0165"+
		"\u0001\u0000\u0000\u00002\u016a\u0001\u0000\u0000\u00004\u016f\u0001\u0000"+
		"\u0000\u00006\u0174\u0001\u0000\u0000\u00008\u0189\u0001\u0000\u0000\u0000"+
		":\u0191\u0001\u0000\u0000\u0000<\u0196\u0001\u0000\u0000\u0000>\u01a9"+
		"\u0001\u0000\u0000\u0000@\u01af\u0001\u0000\u0000\u0000B\u01c3\u0001\u0000"+
		"\u0000\u0000D\u01cf\u0001\u0000\u0000\u0000F\u01f0\u0001\u0000\u0000\u0000"+
		"H\u01f8\u0001\u0000\u0000\u0000J\u024d\u0001\u0000\u0000\u0000L\u0261"+
		"\u0001\u0000\u0000\u0000N\u0284\u0001\u0000\u0000\u0000P\u0286\u0001\u0000"+
		"\u0000\u0000R\u0296\u0001\u0000\u0000\u0000T\u029e\u0001\u0000\u0000\u0000"+
		"V\u02b1\u0001\u0000\u0000\u0000X\u02b4\u0001\u0000\u0000\u0000Z\u02b8"+
		"\u0001\u0000\u0000\u0000\\\u02c6\u0001\u0000\u0000\u0000^\u02c8\u0001"+
		"\u0000\u0000\u0000`\u02cd\u0001\u0000\u0000\u0000b\u02d9\u0001\u0000\u0000"+
		"\u0000d\u02e1\u0001\u0000\u0000\u0000f\u02ef\u0001\u0000\u0000\u0000h"+
		"\u02f8\u0001\u0000\u0000\u0000j\u02fa\u0001\u0000\u0000\u0000l\u02fc\u0001"+
		"\u0000\u0000\u0000n\u0301\u0001\u0000\u0000\u0000p\u0308\u0001\u0000\u0000"+
		"\u0000r\u0314\u0001\u0000\u0000\u0000t\u0318\u0001\u0000\u0000\u0000v"+
		"\u0320\u0001\u0000\u0000\u0000xz\u0005\u0002\u0000\u0000yx\u0001\u0000"+
		"\u0000\u0000yz\u0001\u0000\u0000\u0000z{\u0001\u0000\u0000\u0000{\u0080"+
		"\u0003\u0002\u0001\u0000|\u007f\u0005\u0002\u0000\u0000}\u007f\u0003\b"+
		"\u0004\u0000~|\u0001\u0000\u0000\u0000~}\u0001\u0000\u0000\u0000\u007f"+
		"\u0082\u0001\u0000\u0000\u0000\u0080~\u0001\u0000\u0000\u0000\u0080\u0081"+
		"\u0001\u0000\u0000\u0000\u0081\u0087\u0001\u0000\u0000\u0000\u0082\u0080"+
		"\u0001\u0000\u0000\u0000\u0083\u0086\u0005\u0002\u0000\u0000\u0084\u0086"+
		"\u0003\n\u0005\u0000\u0085\u0083\u0001\u0000\u0000\u0000\u0085\u0084\u0001"+
		"\u0000\u0000\u0000\u0086\u0089\u0001\u0000\u0000\u0000\u0087\u0085\u0001"+
		"\u0000\u0000\u0000\u0087\u0088\u0001\u0000\u0000\u0000\u0088\u008a\u0001"+
		"\u0000\u0000\u0000\u0089\u0087\u0001\u0000\u0000\u0000\u008a\u008b\u0005"+
		"\u0000\u0000\u0001\u008b\u0001\u0001\u0000\u0000\u0000\u008c\u008d\u0005"+
		"\u0001\u0000\u0000\u008d\u008e\u0005\u0006\u0000\u0000\u008e\u008f\u0003"+
		"t:\u0000\u008f\u0090\u0005 \u0000\u0000\u0090\u0091\u0003\u0004\u0002"+
		"\u0000\u0091\u0093\u0005!\u0000\u0000\u0092\u0094\u0003\u0006\u0003\u0000"+
		"\u0093\u0092\u0001\u0000\u0000\u0000\u0093\u0094\u0001\u0000\u0000\u0000"+
		"\u0094\u0095\u0001\u0000\u0000\u0000\u0095\u0096\u0005\u0003\u0000\u0000"+
		"\u0096\u0003\u0001\u0000\u0000\u0000\u0097\u009c\u0003t:\u0000\u0098\u0099"+
		"\u0005$\u0000\u0000\u0099\u009b\u0003t:\u0000\u009a\u0098\u0001\u0000"+
		"\u0000\u0000\u009b\u009e\u0001\u0000\u0000\u0000\u009c\u009a\u0001\u0000"+
		"\u0000\u0000\u009c\u009d\u0001\u0000\u0000\u0000\u009d\u0005\u0001\u0000"+
		"\u0000\u0000\u009e\u009c\u0001\u0000\u0000\u0000\u009f\u00a0\u0005\b\u0000"+
		"\u0000\u00a0\u00a5\u0003t:\u0000\u00a1\u00a2\u0005$\u0000\u0000\u00a2"+
		"\u00a4\u0003t:\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a4\u00a7\u0001"+
		"\u0000\u0000\u0000\u00a5\u00a3\u0001\u0000\u0000\u0000\u00a5\u00a6\u0001"+
		"\u0000\u0000\u0000\u00a6\u0007\u0001\u0000\u0000\u0000\u00a7\u00a5\u0001"+
		"\u0000\u0000\u0000\u00a8\u00a9\u0005\u0001\u0000\u0000\u00a9\u00aa\u0005"+
		"\u0007\u0000\u0000\u00aa\u00ab\u0003t:\u0000\u00ab\u00ac\u0005\u0003\u0000"+
		"\u0000\u00ac\t\u0001\u0000\u0000\u0000\u00ad\u00b2\u0003\u000e\u0007\u0000"+
		"\u00ae\u00b2\u0003\u001e\u000f\u0000\u00af\u00b2\u0003 \u0010\u0000\u00b0"+
		"\u00b2\u0003\f\u0006\u0000\u00b1\u00ad\u0001\u0000\u0000\u0000\u00b1\u00ae"+
		"\u0001\u0000\u0000\u0000\u00b1\u00af\u0001\u0000\u0000\u0000\u00b1\u00b0"+
		"\u0001\u0000\u0000\u0000\u00b2\u000b\u0001\u0000\u0000\u0000\u00b3\u00b4"+
		"\u0005\u0001\u0000\u0000\u00b4\u00b5\u0005\u0015\u0000\u0000\u00b5\u00b6"+
		"\u0005\u0004\u0000\u0000\u00b6\u00b7\u0003&\u0013\u0000\u00b7\u00b8\u0005"+
		"\u0001\u0000\u0000\u00b8\u00b9\u0005\u0005\u0000\u0000\u00b9\u00ba\u0005"+
		"\u0015\u0000\u0000\u00ba\u00bb\u0005\u0004\u0000\u0000\u00bb\r\u0001\u0000"+
		"\u0000\u0000\u00bc\u00bd\u0005\u0001\u0000\u0000\u00bd\u00be\u0005\t\u0000"+
		"\u0000\u00be\u00bf\u0003\u0010\b\u0000\u00bf\u00c0\u0005\u0004\u0000\u0000"+
		"\u00c0\u00c1\u0003&\u0013\u0000\u00c1\u00c2\u0005\u0001\u0000\u0000\u00c2"+
		"\u00c3\u0005\u0005\u0000\u0000\u00c3\u00c4\u0005\t\u0000\u0000\u00c4\u00c5"+
		"\u0005\u0004\u0000\u0000\u00c5\u000f\u0001\u0000\u0000\u0000\u00c6\u00c8"+
		"\u0003\u0012\t\u0000\u00c7\u00c6\u0001\u0000\u0000\u0000\u00c7\u00c8\u0001"+
		"\u0000\u0000\u0000\u00c8\u00c9\u0001\u0000\u0000\u0000\u00c9\u00ca\u0003"+
		"t:\u0000\u00ca\u00cc\u0005 \u0000\u0000\u00cb\u00cd\u0003\"\u0011\u0000"+
		"\u00cc\u00cb\u0001\u0000\u0000\u0000\u00cc\u00cd\u0001\u0000\u0000\u0000"+
		"\u00cd\u00ce\u0001\u0000\u0000\u0000\u00ce\u00d0\u0005!\u0000\u0000\u00cf"+
		"\u00d1\u0003\u0014\n\u0000\u00d0\u00cf\u0001\u0000\u0000\u0000\u00d0\u00d1"+
		"\u0001\u0000\u0000\u0000\u00d1\u00d3\u0001\u0000\u0000\u0000\u00d2\u00d4"+
		"\u0003\u0016\u000b\u0000\u00d3\u00d2\u0001\u0000\u0000\u0000\u00d3\u00d4"+
		"\u0001\u0000\u0000\u0000\u00d4\u00d6\u0001\u0000\u0000\u0000\u00d5\u00d7"+
		"\u0003\u0018\f\u0000\u00d6\u00d5\u0001\u0000\u0000\u0000\u00d6\u00d7\u0001"+
		"\u0000\u0000\u0000\u00d7\u00d9\u0001\u0000\u0000\u0000\u00d8\u00da\u0003"+
		"\u001a\r\u0000\u00d9\u00d8\u0001\u0000\u0000\u0000\u00d9\u00da\u0001\u0000"+
		"\u0000\u0000\u00da\u0011\u0001\u0000\u0000\u0000\u00db\u00dc\u0007\u0000"+
		"\u0000\u0000\u00dc\u0013\u0001\u0000\u0000\u0000\u00dd\u00de\u0005\u0016"+
		"\u0000\u0000\u00de\u00e3\u0003t:\u0000\u00df\u00e0\u0005$\u0000\u0000"+
		"\u00e0\u00e2\u0003t:\u0000\u00e1\u00df\u0001\u0000\u0000\u0000\u00e2\u00e5"+
		"\u0001\u0000\u0000\u0000\u00e3\u00e1\u0001\u0000\u0000\u0000\u00e3\u00e4"+
		"\u0001\u0000\u0000\u0000\u00e4\u0015\u0001\u0000\u0000\u0000\u00e5\u00e3"+
		"\u0001\u0000\u0000\u0000\u00e6\u00e7\u0005\u001c\u0000\u0000\u00e7\u00e8"+
		"\u0005 \u0000\u0000\u00e8\u00e9\u0003H$\u0000\u00e9\u00ea\u0005!\u0000"+
		"\u0000\u00ea\u0017\u0001\u0000\u0000\u0000\u00eb\u00ec\u0005\u001d\u0000"+
		"\u0000\u00ec\u00ed\u0005 \u0000\u0000\u00ed\u00ee\u0003H$\u0000\u00ee"+
		"\u00ef\u0005!\u0000\u0000\u00ef\u0019\u0001\u0000\u0000\u0000\u00f0\u00f4"+
		"\u0005\"\u0000\u0000\u00f1\u00f2\u0003\u001c\u000e\u0000\u00f2\u00f3\u0005"+
		"%\u0000\u0000\u00f3\u00f5\u0001\u0000\u0000\u0000\u00f4\u00f1\u0001\u0000"+
		"\u0000\u0000\u00f5\u00f6\u0001\u0000\u0000\u0000\u00f6\u00f4\u0001\u0000"+
		"\u0000\u0000\u00f6\u00f7\u0001\u0000\u0000\u0000\u00f7\u00f8\u0001\u0000"+
		"\u0000\u0000\u00f8\u00f9\u0005#\u0000\u0000\u00f9\u001b\u0001\u0000\u0000"+
		"\u0000\u00fa\u00fb\u0003v;\u0000\u00fb\u00fc\u0005\'\u0000\u0000\u00fc"+
		"\u00ff\u0003h4\u0000\u00fd\u00fe\u0005)\u0000\u0000\u00fe\u0100\u0003"+
		"H$\u0000\u00ff\u00fd\u0001\u0000\u0000\u0000\u00ff\u0100\u0001\u0000\u0000"+
		"\u0000\u0100\u001d\u0001\u0000\u0000\u0000\u0101\u0102\u0005\u0001\u0000"+
		"\u0000\u0102\u0104\u0005\n\u0000\u0000\u0103\u0105\u0003\u0012\t\u0000"+
		"\u0104\u0103\u0001\u0000\u0000\u0000\u0104\u0105\u0001\u0000\u0000\u0000"+
		"\u0105\u0106\u0001\u0000\u0000\u0000\u0106\u0107\u0003t:\u0000\u0107\u0109"+
		"\u0005 \u0000\u0000\u0108\u010a\u0003\"\u0011\u0000\u0109\u0108\u0001"+
		"\u0000\u0000\u0000\u0109\u010a\u0001\u0000\u0000\u0000\u010a\u010b\u0001"+
		"\u0000\u0000\u0000\u010b\u010c\u0005!\u0000\u0000\u010c\u010d\u0005\'"+
		"\u0000\u0000\u010d\u010e\u0003h4\u0000\u010e\u010f\u0005)\u0000\u0000"+
		"\u010f\u0110\u0003H$\u0000\u0110\u0111\u0005\u0003\u0000\u0000\u0111\u001f"+
		"\u0001\u0000\u0000\u0000\u0112\u0113\u0005\u0001\u0000\u0000\u0113\u0114"+
		"\u0005\u000b\u0000\u0000\u0114\u0115\u0003t:\u0000\u0115\u0117\u0005 "+
		"\u0000\u0000\u0116\u0118\u0003\"\u0011\u0000\u0117\u0116\u0001\u0000\u0000"+
		"\u0000\u0117\u0118\u0001\u0000\u0000\u0000\u0118\u0119\u0001\u0000\u0000"+
		"\u0000\u0119\u011a\u0005!\u0000\u0000\u011a\u011b\u0005\u0004\u0000\u0000"+
		"\u011b\u011c\u0003&\u0013\u0000\u011c\u011d\u0005\u0001\u0000\u0000\u011d"+
		"\u011e\u0005\u0005\u0000\u0000\u011e\u011f\u0005\u000b\u0000\u0000\u011f"+
		"\u0120\u0005\u0004\u0000\u0000\u0120!\u0001\u0000\u0000\u0000\u0121\u0126"+
		"\u0003$\u0012\u0000\u0122\u0123\u0005$\u0000\u0000\u0123\u0125\u0003$"+
		"\u0012\u0000\u0124\u0122\u0001\u0000\u0000\u0000\u0125\u0128\u0001\u0000"+
		"\u0000\u0000\u0126\u0124\u0001\u0000\u0000\u0000\u0126\u0127\u0001\u0000"+
		"\u0000\u0000\u0127#\u0001\u0000\u0000\u0000\u0128\u0126\u0001\u0000\u0000"+
		"\u0000\u0129\u012a\u0003v;\u0000\u012a\u012b\u0005\'\u0000\u0000\u012b"+
		"\u012c\u0003h4\u0000\u012c%\u0001\u0000\u0000\u0000\u012d\u012f\u0003"+
		"(\u0014\u0000\u012e\u012d\u0001\u0000\u0000\u0000\u012f\u0132\u0001\u0000"+
		"\u0000\u0000\u0130\u012e\u0001\u0000\u0000\u0000\u0130\u0131\u0001\u0000"+
		"\u0000\u0000\u0131\'\u0001\u0000\u0000\u0000\u0132\u0130\u0001\u0000\u0000"+
		"\u0000\u0133\u013e\u0003*\u0015\u0000\u0134\u013e\u0003,\u0016\u0000\u0135"+
		"\u013e\u0003.\u0017\u0000\u0136\u013e\u00036\u001b\u0000\u0137\u013e\u0003"+
		"<\u001e\u0000\u0138\u013e\u0003@ \u0000\u0139\u013e\u0003B!\u0000\u013a"+
		"\u013e\u0003D\"\u0000\u013b\u013e\u0003F#\u0000\u013c\u013e\u0003\f\u0006"+
		"\u0000\u013d\u0133\u0001\u0000\u0000\u0000\u013d\u0134\u0001\u0000\u0000"+
		"\u0000\u013d\u0135\u0001\u0000\u0000\u0000\u013d\u0136\u0001\u0000\u0000"+
		"\u0000\u013d\u0137\u0001\u0000\u0000\u0000\u013d\u0138\u0001\u0000\u0000"+
		"\u0000\u013d\u0139\u0001\u0000\u0000\u0000\u013d\u013a\u0001\u0000\u0000"+
		"\u0000\u013d\u013b\u0001\u0000\u0000\u0000\u013d\u013c\u0001\u0000\u0000"+
		"\u0000\u013e)\u0001\u0000\u0000\u0000\u013f\u0141\u0005\u0002\u0000\u0000"+
		"\u0140\u013f\u0001\u0000\u0000\u0000\u0141\u0142\u0001\u0000\u0000\u0000"+
		"\u0142\u0140\u0001\u0000\u0000\u0000\u0142\u0143\u0001\u0000\u0000\u0000"+
		"\u0143+\u0001\u0000\u0000\u0000\u0144\u0145\u0005\u0001\u0000\u0000\u0145"+
		"\u0146\u0003H$\u0000\u0146\u0147\u0005\u0003\u0000\u0000\u0147-\u0001"+
		"\u0000\u0000\u0000\u0148\u0149\u0005\u0001\u0000\u0000\u0149\u014a\u0005"+
		"\f\u0000\u0000\u014a\u014b\u0005 \u0000\u0000\u014b\u014c\u0003$\u0012"+
		"\u0000\u014c\u014d\u0005(\u0000\u0000\u014d\u014e\u0003H$\u0000\u014e"+
		"\u0150\u0005!\u0000\u0000\u014f\u0151\u00030\u0018\u0000\u0150\u014f\u0001"+
		"\u0000\u0000\u0000\u0150\u0151\u0001\u0000\u0000\u0000\u0151\u0153\u0001"+
		"\u0000\u0000\u0000\u0152\u0154\u00032\u0019\u0000\u0153\u0152\u0001\u0000"+
		"\u0000\u0000\u0153\u0154\u0001\u0000\u0000\u0000\u0154\u0156\u0001\u0000"+
		"\u0000\u0000\u0155\u0157\u00034\u001a\u0000\u0156\u0155\u0001\u0000\u0000"+
		"\u0000\u0156\u0157\u0001\u0000\u0000\u0000\u0157\u0159\u0001\u0000\u0000"+
		"\u0000\u0158\u015a\u0003\u0016\u000b\u0000\u0159\u0158\u0001\u0000\u0000"+
		"\u0000\u0159\u015a\u0001\u0000\u0000\u0000\u015a\u015c\u0001\u0000\u0000"+
		"\u0000\u015b\u015d\u0003\u001a\r\u0000\u015c\u015b\u0001\u0000\u0000\u0000"+
		"\u015c\u015d\u0001\u0000\u0000\u0000\u015d\u015e\u0001\u0000\u0000\u0000"+
		"\u015e\u015f\u0005\u0004\u0000\u0000\u015f\u0160\u0003&\u0013\u0000\u0160"+
		"\u0161\u0005\u0001\u0000\u0000\u0161\u0162\u0005\u0005\u0000\u0000\u0162"+
		"\u0163\u0005\f\u0000\u0000\u0163\u0164\u0005\u0004\u0000\u0000\u0164/"+
		"\u0001\u0000\u0000\u0000\u0165\u0166\u0005\u0019\u0000\u0000\u0166\u0167"+
		"\u0005 \u0000\u0000\u0167\u0168\u0003H$\u0000\u0168\u0169\u0005!\u0000"+
		"\u0000\u01691\u0001\u0000\u0000\u0000\u016a\u016b\u0005\u001a\u0000\u0000"+
		"\u016b\u016c\u0005 \u0000\u0000\u016c\u016d\u0003H$\u0000\u016d\u016e"+
		"\u0005!\u0000\u0000\u016e3\u0001\u0000\u0000\u0000\u016f\u0170\u0005\u001b"+
		"\u0000\u0000\u0170\u0171\u0005 \u0000\u0000\u0171\u0172\u0003H$\u0000"+
		"\u0172\u0173\u0005!\u0000\u0000\u01735\u0001\u0000\u0000\u0000\u0174\u0175"+
		"\u0005\u0001\u0000\u0000\u0175\u0176\u0005\r\u0000\u0000\u0176\u0177\u0005"+
		" \u0000\u0000\u0177\u0178\u0003H$\u0000\u0178\u0179\u0005!\u0000\u0000"+
		"\u0179\u017a\u0005\u0004\u0000\u0000\u017a\u017e\u0003&\u0013\u0000\u017b"+
		"\u017d\u00038\u001c\u0000\u017c\u017b\u0001\u0000\u0000\u0000\u017d\u0180"+
		"\u0001\u0000\u0000\u0000\u017e\u017c\u0001\u0000\u0000\u0000\u017e\u017f"+
		"\u0001\u0000\u0000\u0000\u017f\u0182\u0001\u0000\u0000\u0000\u0180\u017e"+
		"\u0001\u0000\u0000\u0000\u0181\u0183\u0003:\u001d\u0000\u0182\u0181\u0001"+
		"\u0000\u0000\u0000\u0182\u0183\u0001\u0000\u0000\u0000\u0183\u0184\u0001"+
		"\u0000\u0000\u0000\u0184\u0185\u0005\u0001\u0000\u0000\u0185\u0186\u0005"+
		"\u0005\u0000\u0000\u0186\u0187\u0005\r\u0000\u0000\u0187\u0188\u0005\u0004"+
		"\u0000\u0000\u01887\u0001\u0000\u0000\u0000\u0189\u018a\u0005\u0001\u0000"+
		"\u0000\u018a\u018b\u0005\u000e\u0000\u0000\u018b\u018c\u0005 \u0000\u0000"+
		"\u018c\u018d\u0003H$\u0000\u018d\u018e\u0005!\u0000\u0000\u018e\u018f"+
		"\u0005\u0004\u0000\u0000\u018f\u0190\u0003&\u0013\u0000\u01909\u0001\u0000"+
		"\u0000\u0000\u0191\u0192\u0005\u0001\u0000\u0000\u0192\u0193\u0005\u000f"+
		"\u0000\u0000\u0193\u0194\u0005\u0004\u0000\u0000\u0194\u0195\u0003&\u0013"+
		"\u0000\u0195;\u0001\u0000\u0000\u0000\u0196\u0197\u0005\u0001\u0000\u0000"+
		"\u0197\u0198\u0005\u0010\u0000\u0000\u0198\u0199\u0003\u001c\u000e\u0000"+
		"\u0199\u019a\u0005\u0004\u0000\u0000\u019a\u019e\u0003&\u0013\u0000\u019b"+
		"\u019d\u0003>\u001f\u0000\u019c\u019b\u0001\u0000\u0000\u0000\u019d\u01a0"+
		"\u0001\u0000\u0000\u0000\u019e\u019c\u0001\u0000\u0000\u0000\u019e\u019f"+
		"\u0001\u0000\u0000\u0000\u019f\u01a2\u0001\u0000\u0000\u0000\u01a0\u019e"+
		"\u0001\u0000\u0000\u0000\u01a1\u01a3\u0003:\u001d\u0000\u01a2\u01a1\u0001"+
		"\u0000\u0000\u0000\u01a2\u01a3\u0001\u0000\u0000\u0000\u01a3\u01a4\u0001"+
		"\u0000\u0000\u0000\u01a4\u01a5\u0005\u0001\u0000\u0000\u01a5\u01a6\u0005"+
		"\u0005\u0000\u0000\u01a6\u01a7\u0005\u0010\u0000\u0000\u01a7\u01a8\u0005"+
		"\u0004\u0000\u0000\u01a8=\u0001\u0000\u0000\u0000\u01a9\u01aa\u0005\u0001"+
		"\u0000\u0000\u01aa\u01ab\u0005\u0011\u0000\u0000\u01ab\u01ac\u0003\u001c"+
		"\u000e\u0000\u01ac\u01ad\u0005\u0004\u0000\u0000\u01ad\u01ae\u0003&\u0013"+
		"\u0000\u01ae?\u0001\u0000\u0000\u0000\u01af\u01b0\u0005\u0001\u0000\u0000"+
		"\u01b0\u01b1\u0005\u0012\u0000\u0000\u01b1\u01b2\u0005 \u0000\u0000\u01b2"+
		"\u01b9\u0003H$\u0000\u01b3\u01b4\u0005$\u0000\u0000\u01b4\u01b7\u0003"+
		"H$\u0000\u01b5\u01b6\u0005$\u0000\u0000\u01b6\u01b8\u0003H$\u0000\u01b7"+
		"\u01b5\u0001\u0000\u0000\u0000\u01b7\u01b8\u0001\u0000\u0000\u0000\u01b8"+
		"\u01ba\u0001\u0000\u0000\u0000\u01b9\u01b3\u0001\u0000\u0000\u0000\u01b9"+
		"\u01ba\u0001\u0000\u0000\u0000\u01ba\u01bb\u0001\u0000\u0000\u0000\u01bb"+
		"\u01bc\u0005!\u0000\u0000\u01bc\u01bd\u0005\u0004\u0000\u0000\u01bd\u01be"+
		"\u0003&\u0013\u0000\u01be\u01bf\u0005\u0001\u0000\u0000\u01bf\u01c0\u0005"+
		"\u0005\u0000\u0000\u01c0\u01c1\u0005\u0012\u0000\u0000\u01c1\u01c2\u0005"+
		"\u0004\u0000\u0000\u01c2A\u0001\u0000\u0000\u0000\u01c3\u01c4\u0005\u0001"+
		"\u0000\u0000\u01c4\u01c5\u0005\u0013\u0000\u0000\u01c5\u01c6\u0005 \u0000"+
		"\u0000\u01c6\u01c7\u0003H$\u0000\u01c7\u01c8\u0005!\u0000\u0000\u01c8"+
		"\u01c9\u0005\u0004\u0000\u0000\u01c9\u01ca\u0003&\u0013\u0000\u01ca\u01cb"+
		"\u0005\u0001\u0000\u0000\u01cb\u01cc\u0005\u0005\u0000\u0000\u01cc\u01cd"+
		"\u0005\u0013\u0000\u0000\u01cd\u01ce\u0005\u0004\u0000\u0000\u01ceC\u0001"+
		"\u0000\u0000\u0000\u01cf\u01d0\u0005\u0001\u0000\u0000\u01d0\u01d1\u0005"+
		"\u0014\u0000\u0000\u01d1\u01d2\u0005 \u0000\u0000\u01d2\u01d3\u0003H$"+
		"\u0000\u01d3\u01d4\u0005!\u0000\u0000\u01d4\u01d5\u0005\u0004\u0000\u0000"+
		"\u01d5\u01d6\u0003&\u0013\u0000\u01d6\u01d7\u0005\u0001\u0000\u0000\u01d7"+
		"\u01d8\u0005\u0005\u0000\u0000\u01d8\u01d9\u0005\u0014\u0000\u0000\u01d9"+
		"\u01da\u0005\u0004\u0000\u0000\u01daE\u0001\u0000\u0000\u0000\u01db\u01dc"+
		"\u0005\u0001\u0000\u0000\u01dc\u01dd\u0003t:\u0000\u01dd\u01df\u0005 "+
		"\u0000\u0000\u01de\u01e0\u0003R)\u0000\u01df\u01de\u0001\u0000\u0000\u0000"+
		"\u01df\u01e0\u0001\u0000\u0000\u0000\u01e0\u01e1\u0001\u0000\u0000\u0000"+
		"\u01e1\u01e3\u0005!\u0000\u0000\u01e2\u01e4\u00030\u0018\u0000\u01e3\u01e2"+
		"\u0001\u0000\u0000\u0000\u01e3\u01e4\u0001\u0000\u0000\u0000\u01e4\u01e6"+
		"\u0001\u0000\u0000\u0000\u01e5\u01e7\u00032\u0019\u0000\u01e6\u01e5\u0001"+
		"\u0000\u0000\u0000\u01e6\u01e7\u0001\u0000\u0000\u0000\u01e7\u01e9\u0001"+
		"\u0000\u0000\u0000\u01e8\u01ea\u00034\u001a\u0000\u01e9\u01e8\u0001\u0000"+
		"\u0000\u0000\u01e9\u01ea\u0001\u0000\u0000\u0000\u01ea\u01eb\u0001\u0000"+
		"\u0000\u0000\u01eb\u01ec\u0005\u0003\u0000\u0000\u01ec\u01f1\u0001\u0000"+
		"\u0000\u0000\u01ed\u01ee\u0005\u0001\u0000\u0000\u01ee\u01ef\u0005\u001e"+
		"\u0000\u0000\u01ef\u01f1\u0005\u0003\u0000\u0000\u01f0\u01db\u0001\u0000"+
		"\u0000\u0000\u01f0\u01ed\u0001\u0000\u0000\u0000\u01f1G\u0001\u0000\u0000"+
		"\u0000\u01f2\u01f3\u0006$\uffff\uffff\u0000\u01f3\u01f4\u00057\u0000\u0000"+
		"\u01f4\u01f9\u0003H$\u000b\u01f5\u01f6\u00051\u0000\u0000\u01f6\u01f9"+
		"\u0003H$\n\u01f7\u01f9\u0003J%\u0000\u01f8\u01f2\u0001\u0000\u0000\u0000"+
		"\u01f8\u01f5\u0001\u0000\u0000\u0000\u01f8\u01f7\u0001\u0000\u0000\u0000"+
		"\u01f9\u021a\u0001\u0000\u0000\u0000\u01fa\u01fb\n\t\u0000\u0000\u01fb"+
		"\u01fc\u0007\u0001\u0000\u0000\u01fc\u0219\u0003H$\n\u01fd\u01fe\n\b\u0000"+
		"\u0000\u01fe\u01ff\u0007\u0002\u0000\u0000\u01ff\u0219\u0003H$\t\u0200"+
		"\u0201\n\u0007\u0000\u0000\u0201\u0202\u0007\u0003\u0000\u0000\u0202\u0219"+
		"\u0003H$\b\u0203\u0204\n\u0006\u0000\u0000\u0204\u0205\u0007\u0004\u0000"+
		"\u0000\u0205\u0219\u0003H$\u0007\u0206\u0207\n\u0005\u0000\u0000\u0207"+
		"\u0208\u00058\u0000\u0000\u0208\u0219\u0003H$\u0006\u0209\u020a\n\u0004"+
		"\u0000\u0000\u020a\u020b\u00059\u0000\u0000\u020b\u0219\u0003H$\u0005"+
		"\u020c\u020d\n\u0003\u0000\u0000\u020d\u020e\u0005:\u0000\u0000\u020e"+
		"\u0219\u0003H$\u0004\u020f\u0210\n\u0002\u0000\u0000\u0210\u0211\u0005"+
		";\u0000\u0000\u0211\u0219\u0003H$\u0003\u0212\u0213\n\r\u0000\u0000\u0213"+
		"\u0214\u0007\u0005\u0000\u0000\u0214\u0219\u0003L&\u0000\u0215\u0216\n"+
		"\f\u0000\u0000\u0216\u0217\u0007\u0006\u0000\u0000\u0217\u0219\u0003N"+
		"\'\u0000\u0218\u01fa\u0001\u0000\u0000\u0000\u0218\u01fd\u0001\u0000\u0000"+
		"\u0000\u0218\u0200\u0001\u0000\u0000\u0000\u0218\u0203\u0001\u0000\u0000"+
		"\u0000\u0218\u0206\u0001\u0000\u0000\u0000\u0218\u0209\u0001\u0000\u0000"+
		"\u0000\u0218\u020c\u0001\u0000\u0000\u0000\u0218\u020f\u0001\u0000\u0000"+
		"\u0000\u0218\u0212\u0001\u0000\u0000\u0000\u0218\u0215\u0001\u0000\u0000"+
		"\u0000\u0219\u021c\u0001\u0000\u0000\u0000\u021a\u0218\u0001\u0000\u0000"+
		"\u0000\u021a\u021b\u0001\u0000\u0000\u0000\u021bI\u0001\u0000\u0000\u0000"+
		"\u021c\u021a\u0001\u0000\u0000\u0000\u021d\u021e\u0005 \u0000\u0000\u021e"+
		"\u021f\u0003H$\u0000\u021f\u0220\u0005!\u0000\u0000\u0220\u024e\u0001"+
		"\u0000\u0000\u0000\u0221\u024e\u0005<\u0000\u0000\u0222\u0223\u0005\r"+
		"\u0000\u0000\u0223\u0224\u0003H$\u0000\u0224\u0225\u0005B\u0000\u0000"+
		"\u0225\u022d\u0003H$\u0000\u0226\u0227\u0005\u000e\u0000\u0000\u0227\u0228"+
		"\u0003H$\u0000\u0228\u0229\u0005B\u0000\u0000\u0229\u022a\u0003H$\u0000"+
		"\u022a\u022c\u0001\u0000\u0000\u0000\u022b\u0226\u0001\u0000\u0000\u0000"+
		"\u022c\u022f\u0001\u0000\u0000\u0000\u022d\u022b\u0001\u0000\u0000\u0000"+
		"\u022d\u022e\u0001\u0000\u0000\u0000\u022e\u0230\u0001\u0000\u0000\u0000"+
		"\u022f\u022d\u0001\u0000\u0000\u0000\u0230\u0231\u0005\u000f\u0000\u0000"+
		"\u0231\u0232\u0003H$\u0000\u0232\u0233\u0005C\u0000\u0000\u0233\u024e"+
		"\u0001\u0000\u0000\u0000\u0234\u0235\u0005\u0010\u0000\u0000\u0235\u023a"+
		"\u0003T*\u0000\u0236\u0237\u0005$\u0000\u0000\u0237\u0239\u0003T*\u0000"+
		"\u0238\u0236\u0001\u0000\u0000\u0000\u0239\u023c\u0001\u0000\u0000\u0000"+
		"\u023a\u0238\u0001\u0000\u0000\u0000\u023a\u023b\u0001\u0000\u0000\u0000"+
		"\u023b\u023d\u0001\u0000\u0000\u0000\u023c\u023a\u0001\u0000\u0000\u0000"+
		"\u023d\u023e\u0005A\u0000\u0000\u023e\u023f\u0003H$\u0000\u023f\u024e"+
		"\u0001\u0000\u0000\u0000\u0240\u024e\u0003V+\u0000\u0241\u0242\u0003t"+
		":\u0000\u0242\u0244\u0005 \u0000\u0000\u0243\u0245\u0003R)\u0000\u0244"+
		"\u0243\u0001\u0000\u0000\u0000\u0244\u0245\u0001\u0000\u0000\u0000\u0245"+
		"\u0246\u0001\u0000\u0000\u0000\u0246\u0247\u0005!\u0000\u0000\u0247\u024e"+
		"\u0001\u0000\u0000\u0000\u0248\u024e\u0003j5\u0000\u0249\u024e\u0003l"+
		"6\u0000\u024a\u024e\u0003n7\u0000\u024b\u024e\u0003p8\u0000\u024c\u024e"+
		"\u0003t:\u0000\u024d\u021d\u0001\u0000\u0000\u0000\u024d\u0221\u0001\u0000"+
		"\u0000\u0000\u024d\u0222\u0001\u0000\u0000\u0000\u024d\u0234\u0001\u0000"+
		"\u0000\u0000\u024d\u0240\u0001\u0000\u0000\u0000\u024d\u0241\u0001\u0000"+
		"\u0000\u0000\u024d\u0248\u0001\u0000\u0000\u0000\u024d\u0249\u0001\u0000"+
		"\u0000\u0000\u024d\u024a\u0001\u0000\u0000\u0000\u024d\u024b\u0001\u0000"+
		"\u0000\u0000\u024d\u024c\u0001\u0000\u0000\u0000\u024eK\u0001\u0000\u0000"+
		"\u0000\u024f\u0250\u0003t:\u0000\u0250\u0251\u0005&\u0000\u0000\u0251"+
		"\u0253\u0001\u0000\u0000\u0000\u0252\u024f\u0001\u0000\u0000\u0000\u0252"+
		"\u0253\u0001\u0000\u0000\u0000\u0253\u0254\u0001\u0000\u0000\u0000\u0254"+
		"\u0255\u0003v;\u0000\u0255\u0257\u0005 \u0000\u0000\u0256\u0258\u0003"+
		"R)\u0000\u0257\u0256\u0001\u0000\u0000\u0000\u0257\u0258\u0001\u0000\u0000"+
		"\u0000\u0258\u0259\u0001\u0000\u0000\u0000\u0259\u025a\u0005!\u0000\u0000"+
		"\u025a\u0262\u0001\u0000\u0000\u0000\u025b\u025c\u0003t:\u0000\u025c\u025d"+
		"\u0005&\u0000\u0000\u025d\u025f\u0001\u0000\u0000\u0000\u025e\u025b\u0001"+
		"\u0000\u0000\u0000\u025e\u025f\u0001\u0000\u0000\u0000\u025f\u0260\u0001"+
		"\u0000\u0000\u0000\u0260\u0262\u0003v;\u0000\u0261\u0252\u0001\u0000\u0000"+
		"\u0000\u0261\u025e\u0001\u0000\u0000\u0000\u0262M\u0001\u0000\u0000\u0000"+
		"\u0263\u0264\u0003v;\u0000\u0264\u0265\u0005 \u0000\u0000\u0265\u0266"+
		"\u0003P(\u0000\u0266\u0267\u0005(\u0000\u0000\u0267\u0268\u0003H$\u0000"+
		"\u0268\u0269\u0005!\u0000\u0000\u0269\u0285\u0001\u0000\u0000\u0000\u026a"+
		"\u026b\u0003v;\u0000\u026b\u026c\u0005 \u0000\u0000\u026c\u026f\u0003"+
		"v;\u0000\u026d\u026e\u0005\'\u0000\u0000\u026e\u0270\u0003h4\u0000\u026f"+
		"\u026d\u0001\u0000\u0000\u0000\u026f\u0270\u0001\u0000\u0000\u0000\u0270"+
		"\u0271\u0001\u0000\u0000\u0000\u0271\u0272\u0005%\u0000\u0000\u0272\u0275"+
		"\u0003v;\u0000\u0273\u0274\u0005\'\u0000\u0000\u0274\u0276\u0003h4\u0000"+
		"\u0275\u0273\u0001\u0000\u0000\u0000\u0275\u0276\u0001\u0000\u0000\u0000"+
		"\u0276\u0277\u0001\u0000\u0000\u0000\u0277\u0278\u0005)\u0000\u0000\u0278"+
		"\u0279\u0003H$\u0000\u0279\u027a\u0005(\u0000\u0000\u027a\u027b\u0003"+
		"H$\u0000\u027b\u027c\u0005!\u0000\u0000\u027c\u0285\u0001\u0000\u0000"+
		"\u0000\u027d\u027e\u0003v;\u0000\u027e\u0280\u0005 \u0000\u0000\u027f"+
		"\u0281\u0003R)\u0000\u0280\u027f\u0001\u0000\u0000\u0000\u0280\u0281\u0001"+
		"\u0000\u0000\u0000\u0281\u0282\u0001\u0000\u0000\u0000\u0282\u0283\u0005"+
		"!\u0000\u0000\u0283\u0285\u0001\u0000\u0000\u0000\u0284\u0263\u0001\u0000"+
		"\u0000\u0000\u0284\u026a\u0001\u0000\u0000\u0000\u0284\u027d\u0001\u0000"+
		"\u0000\u0000\u0285O\u0001\u0000\u0000\u0000\u0286\u0289\u0003v;\u0000"+
		"\u0287\u0288\u0005\'\u0000\u0000\u0288\u028a\u0003h4\u0000\u0289\u0287"+
		"\u0001\u0000\u0000\u0000\u0289\u028a\u0001\u0000\u0000\u0000\u028a\u0293"+
		"\u0001\u0000\u0000\u0000\u028b\u028c\u0005$\u0000\u0000\u028c\u028f\u0003"+
		"v;\u0000\u028d\u028e\u0005\'\u0000\u0000\u028e\u0290\u0003h4\u0000\u028f"+
		"\u028d\u0001\u0000\u0000\u0000\u028f\u0290\u0001\u0000\u0000\u0000\u0290"+
		"\u0292\u0001\u0000\u0000\u0000\u0291\u028b\u0001\u0000\u0000\u0000\u0292"+
		"\u0295\u0001\u0000\u0000\u0000\u0293\u0291\u0001\u0000\u0000\u0000\u0293"+
		"\u0294\u0001\u0000\u0000\u0000\u0294Q\u0001\u0000\u0000\u0000\u0295\u0293"+
		"\u0001\u0000\u0000\u0000\u0296\u029b\u0003H$\u0000\u0297\u0298\u0005$"+
		"\u0000\u0000\u0298\u029a\u0003H$\u0000\u0299\u0297\u0001\u0000\u0000\u0000"+
		"\u029a\u029d\u0001\u0000\u0000\u0000\u029b\u0299\u0001\u0000\u0000\u0000"+
		"\u029b\u029c\u0001\u0000\u0000\u0000\u029cS\u0001\u0000\u0000\u0000\u029d"+
		"\u029b\u0001\u0000\u0000\u0000\u029e\u02a1\u0003v;\u0000\u029f\u02a0\u0005"+
		"\'\u0000\u0000\u02a0\u02a2\u0003h4\u0000\u02a1\u029f\u0001\u0000\u0000"+
		"\u0000\u02a1\u02a2\u0001\u0000\u0000\u0000\u02a2\u02a3\u0001\u0000\u0000"+
		"\u0000\u02a3\u02a4\u0005)\u0000\u0000\u02a4\u02a5\u0003H$\u0000\u02a5"+
		"U\u0001\u0000\u0000\u0000\u02a6\u02b2\u0005W\u0000\u0000\u02a7\u02b2\u0005"+
		"V\u0000\u0000\u02a8\u02b2\u0003X,\u0000\u02a9\u02b2\u0005=\u0000\u0000"+
		"\u02aa\u02b2\u0005>\u0000\u0000\u02ab\u02b2\u0005?\u0000\u0000\u02ac\u02b2"+
		"\u0005@\u0000\u0000\u02ad\u02b2\u0005/\u0000\u0000\u02ae\u02b2\u0003Z"+
		"-\u0000\u02af\u02b2\u0003`0\u0000\u02b0\u02b2\u0003d2\u0000\u02b1\u02a6"+
		"\u0001\u0000\u0000\u0000\u02b1\u02a7\u0001\u0000\u0000\u0000\u02b1\u02a8"+
		"\u0001\u0000\u0000\u0000\u02b1\u02a9\u0001\u0000\u0000\u0000\u02b1\u02aa"+
		"\u0001\u0000\u0000\u0000\u02b1\u02ab\u0001\u0000\u0000\u0000\u02b1\u02ac"+
		"\u0001\u0000\u0000\u0000\u02b1\u02ad\u0001\u0000\u0000\u0000\u02b1\u02ae"+
		"\u0001\u0000\u0000\u0000\u02b1\u02af\u0001\u0000\u0000\u0000\u02b1\u02b0"+
		"\u0001\u0000\u0000\u0000\u02b2W\u0001\u0000\u0000\u0000\u02b3\u02b5\u0005"+
		"X\u0000\u0000\u02b4\u02b3\u0001\u0000\u0000\u0000\u02b5\u02b6\u0001\u0000"+
		"\u0000\u0000\u02b6\u02b4\u0001\u0000\u0000\u0000\u02b6\u02b7\u0001\u0000"+
		"\u0000\u0000\u02b7Y\u0001\u0000\u0000\u0000\u02b8\u02b9\u0003\\.\u0000"+
		"\u02b9\u02c2\u0005\"\u0000\u0000\u02ba\u02bf\u0003^/\u0000\u02bb\u02bc"+
		"\u0005$\u0000\u0000\u02bc\u02be\u0003^/\u0000\u02bd\u02bb\u0001\u0000"+
		"\u0000\u0000\u02be\u02c1\u0001\u0000\u0000\u0000\u02bf\u02bd\u0001\u0000"+
		"\u0000\u0000\u02bf\u02c0\u0001\u0000\u0000\u0000\u02c0\u02c3\u0001\u0000"+
		"\u0000\u0000\u02c1\u02bf\u0001\u0000\u0000\u0000\u02c2\u02ba\u0001\u0000"+
		"\u0000\u0000\u02c2\u02c3\u0001\u0000\u0000\u0000\u02c3\u02c4\u0001\u0000"+
		"\u0000\u0000\u02c4\u02c5\u0005#\u0000\u0000\u02c5[\u0001\u0000\u0000\u0000"+
		"\u02c6\u02c7\u0007\u0007\u0000\u0000\u02c7]\u0001\u0000\u0000\u0000\u02c8"+
		"\u02cb\u0003H$\u0000\u02c9\u02ca\u0005.\u0000\u0000\u02ca\u02cc\u0003"+
		"H$\u0000\u02cb\u02c9\u0001\u0000\u0000\u0000\u02cb\u02cc\u0001\u0000\u0000"+
		"\u0000\u02cc_\u0001\u0000\u0000\u0000\u02cd\u02ce\u0005J\u0000\u0000\u02ce"+
		"\u02cf\u0005\"\u0000\u0000\u02cf\u02d4\u0003b1\u0000\u02d0\u02d1\u0005"+
		"$\u0000\u0000\u02d1\u02d3\u0003b1\u0000\u02d2\u02d0\u0001\u0000\u0000"+
		"\u0000\u02d3\u02d6\u0001\u0000\u0000\u0000\u02d4\u02d2\u0001\u0000\u0000"+
		"\u0000\u02d4\u02d5\u0001\u0000\u0000\u0000\u02d5\u02d7\u0001\u0000\u0000"+
		"\u0000\u02d6\u02d4\u0001\u0000\u0000\u0000\u02d7\u02d8\u0005#\u0000\u0000"+
		"\u02d8a\u0001\u0000\u0000\u0000\u02d9\u02dc\u0003v;\u0000\u02da\u02db"+
		"\u0005\'\u0000\u0000\u02db\u02dd\u0003h4\u0000\u02dc\u02da\u0001\u0000"+
		"\u0000\u0000\u02dc\u02dd\u0001\u0000\u0000\u0000\u02dd\u02de\u0001\u0000"+
		"\u0000\u0000\u02de\u02df\u0005)\u0000\u0000\u02df\u02e0\u0003H$\u0000"+
		"\u02e0c\u0001\u0000\u0000\u0000\u02e1\u02e2\u0005I\u0000\u0000\u02e2\u02eb"+
		"\u0005\"\u0000\u0000\u02e3\u02e8\u0003f3\u0000\u02e4\u02e5\u0005$\u0000"+
		"\u0000\u02e5\u02e7\u0003f3\u0000\u02e6\u02e4\u0001\u0000\u0000\u0000\u02e7"+
		"\u02ea\u0001\u0000\u0000\u0000\u02e8\u02e6\u0001\u0000\u0000\u0000\u02e8"+
		"\u02e9\u0001\u0000\u0000\u0000\u02e9\u02ec\u0001\u0000\u0000\u0000\u02ea"+
		"\u02e8\u0001\u0000\u0000\u0000\u02eb\u02e3\u0001\u0000\u0000\u0000\u02eb"+
		"\u02ec\u0001\u0000\u0000\u0000\u02ec\u02ed\u0001\u0000\u0000\u0000\u02ed"+
		"\u02ee\u0005#\u0000\u0000\u02eee\u0001\u0000\u0000\u0000\u02ef\u02f0\u0003"+
		"H$\u0000\u02f0\u02f1\u0007\b\u0000\u0000\u02f1\u02f2\u0003H$\u0000\u02f2"+
		"g\u0001\u0000\u0000\u0000\u02f3\u02f9\u0003j5\u0000\u02f4\u02f9\u0003"+
		"l6\u0000\u02f5\u02f9\u0003n7\u0000\u02f6\u02f9\u0003p8\u0000\u02f7\u02f9"+
		"\u0003t:\u0000\u02f8\u02f3\u0001\u0000\u0000\u0000\u02f8\u02f4\u0001\u0000"+
		"\u0000\u0000\u02f8\u02f5\u0001\u0000\u0000\u0000\u02f8\u02f6\u0001\u0000"+
		"\u0000\u0000\u02f8\u02f7\u0001\u0000\u0000\u0000\u02f9i\u0001\u0000\u0000"+
		"\u0000\u02fa\u02fb\u0007\t\u0000\u0000\u02fbk\u0001\u0000\u0000\u0000"+
		"\u02fc\u02fd\u0003\\.\u0000\u02fd\u02fe\u0005 \u0000\u0000\u02fe\u02ff"+
		"\u0003h4\u0000\u02ff\u0300\u0005!\u0000\u0000\u0300m\u0001\u0000\u0000"+
		"\u0000\u0301\u0302\u0005I\u0000\u0000\u0302\u0303\u0005 \u0000\u0000\u0303"+
		"\u0304\u0003h4\u0000\u0304\u0305\u0005$\u0000\u0000\u0305\u0306\u0003"+
		"h4\u0000\u0306\u0307\u0005!\u0000\u0000\u0307o\u0001\u0000\u0000\u0000"+
		"\u0308\u0309\u0005J\u0000\u0000\u0309\u030a\u0005 \u0000\u0000\u030a\u030f"+
		"\u0003r9\u0000\u030b\u030c\u0005$\u0000\u0000\u030c\u030e\u0003r9\u0000"+
		"\u030d\u030b\u0001\u0000\u0000\u0000\u030e\u0311\u0001\u0000\u0000\u0000"+
		"\u030f\u030d\u0001\u0000\u0000\u0000\u030f\u0310\u0001\u0000\u0000\u0000"+
		"\u0310\u0312\u0001\u0000\u0000\u0000\u0311\u030f\u0001\u0000\u0000\u0000"+
		"\u0312\u0313\u0005!\u0000\u0000\u0313q\u0001\u0000\u0000\u0000\u0314\u0315"+
		"\u0003v;\u0000\u0315\u0316\u0005\'\u0000\u0000\u0316\u0317\u0003h4\u0000"+
		"\u0317s\u0001\u0000\u0000\u0000\u0318\u031d\u0003v;\u0000\u0319\u031a"+
		"\u0005&\u0000\u0000\u031a\u031c\u0003v;\u0000\u031b\u0319\u0001\u0000"+
		"\u0000\u0000\u031c\u031f\u0001\u0000\u0000\u0000\u031d\u031b\u0001\u0000"+
		"\u0000\u0000\u031d\u031e\u0001\u0000\u0000\u0000\u031eu\u0001\u0000\u0000"+
		"\u0000\u031f\u031d\u0001\u0000\u0000\u0000\u0320\u0321\u0007\n\u0000\u0000"+
		"\u0321w\u0001\u0000\u0000\u0000Iy~\u0080\u0085\u0087\u0093\u009c\u00a5"+
		"\u00b1\u00c7\u00cc\u00d0\u00d3\u00d6\u00d9\u00e3\u00f6\u00ff\u0104\u0109"+
		"\u0117\u0126\u0130\u013d\u0142\u0150\u0153\u0156\u0159\u015c\u017e\u0182"+
		"\u019e\u01a2\u01b7\u01b9\u01df\u01e3\u01e6\u01e9\u01f0\u01f8\u0218\u021a"+
		"\u022d\u023a\u0244\u024d\u0252\u0257\u025e\u0261\u026f\u0275\u0280\u0284"+
		"\u0289\u028f\u0293\u029b\u02a1\u02b1\u02b6\u02bf\u02c2\u02cb\u02d4\u02dc"+
		"\u02e8\u02eb\u02f8\u030f\u031d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}