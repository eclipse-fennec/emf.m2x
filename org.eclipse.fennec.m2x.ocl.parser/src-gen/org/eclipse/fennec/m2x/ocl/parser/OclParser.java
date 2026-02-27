// Generated from /opt/git/m2m/workspace/org.eclipse.fennec.m2x.ocl.parser/grammar/Ocl.g4 by ANTLR 4.13.2
package org.eclipse.fennec.m2x.ocl.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class OclParser extends Parser {
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
		T__73=74, REAL_LITERAL=75, INTEGER_LITERAL=76, STRING_LITERAL=77, ESCAPED_IDENTIFIER=78, 
		IDENTIFIER=79, WS=80, LINE_COMMENT=81, BLOCK_COMMENT=82;
	public static final int
		RULE_expressionEntry = 0, RULE_completeOclDocumentEntry = 1, RULE_completeOclDocument = 2, 
		RULE_importDeclaration = 3, RULE_packageDeclaration = 4, RULE_contextDeclaration = 5, 
		RULE_classifierContextDeclaration = 6, RULE_classifierContextBody = 7, 
		RULE_invariantConstraint = 8, RULE_definitionConstraint = 9, RULE_operationContextDeclaration = 10, 
		RULE_operationContextBody = 11, RULE_propertyContextDeclaration = 12, 
		RULE_propertyContextBody = 13, RULE_parameterList = 14, RULE_parameter = 15, 
		RULE_expression = 16, RULE_primaryExpression = 17, RULE_propertyOrCallSuffix = 18, 
		RULE_iteratorOrOperationCall = 19, RULE_iteratorVariables = 20, RULE_argumentList = 21, 
		RULE_letBinding = 22, RULE_isMarkedPre = 23, RULE_literalExpression = 24, 
		RULE_stringLiteral_ = 25, RULE_collectionLiteral = 26, RULE_collectionKind = 27, 
		RULE_collectionLiteralPart = 28, RULE_tupleLiteral = 29, RULE_tupleLiteralPart = 30, 
		RULE_mapLiteral = 31, RULE_mapLiteralPart = 32, RULE_typeExpression = 33, 
		RULE_primitiveType = 34, RULE_collectionType = 35, RULE_mapType = 36, 
		RULE_tupleType = 37, RULE_tupleTypePart = 38, RULE_pathName = 39, RULE_identifier = 40;
	private static String[] makeRuleNames() {
		return new String[] {
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
			"pathName", "identifier"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'import'", "'include'", "'library'", "':'", "'::'", "'*'", "'package'", 
			"'endpackage'", "'context'", "'inv'", "'('", "')'", "'static'", "'def'", 
			"'='", "'pre'", "'post'", "'body'", "'init'", "'derive'", "','", "'.'", 
			"'?.'", "'->'", "'?->'", "'not'", "'-'", "'/'", "'+'", "'<'", "'>'", 
			"'<='", "'>='", "'<>'", "'and'", "'or'", "'xor'", "'implies'", "'self'", 
			"'if'", "'then'", "'elseif'", "'else'", "'endif'", "'let'", "'in'", "'|'", 
			"';'", "'@'", "'true'", "'false'", "'null'", "'invalid'", "'{'", "'}'", 
			"'Set'", "'OrderedSet'", "'Bag'", "'Sequence'", "'Collection'", "'..'", 
			"'Tuple'", "'Map'", "'with'", "'<-'", "'Boolean'", "'Integer'", "'Real'", 
			"'String'", "'UnlimitedNatural'", "'OclAny'", "'OclVoid'", "'OclInvalid'", 
			"'OclMessage'"
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
			null, null, null, "REAL_LITERAL", "INTEGER_LITERAL", "STRING_LITERAL", 
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
	public String getGrammarFileName() { return "Ocl.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public OclParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionEntryContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(OclParser.EOF, 0); }
		public ExpressionEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionEntry; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitExpressionEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionEntryContext expressionEntry() throws RecognitionException {
		ExpressionEntryContext _localctx = new ExpressionEntryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_expressionEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			expression(0);
			setState(83);
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
		public TerminalNode EOF() { return getToken(OclParser.EOF, 0); }
		public CompleteOclDocumentEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_completeOclDocumentEntry; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCompleteOclDocumentEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompleteOclDocumentEntryContext completeOclDocumentEntry() throws RecognitionException {
		CompleteOclDocumentEntryContext _localctx = new CompleteOclDocumentEntryContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_completeOclDocumentEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			completeOclDocument();
			setState(86);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCompleteOclDocument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompleteOclDocumentContext completeOclDocument() throws RecognitionException {
		CompleteOclDocumentContext _localctx = new CompleteOclDocumentContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_completeOclDocument);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 14L) != 0)) {
				{
				{
				setState(88);
				importDeclaration();
				}
				}
				setState(93);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(98);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6 || _la==T__8) {
				{
				setState(96);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__6:
					{
					setState(94);
					packageDeclaration();
					}
					break;
				case T__8:
					{
					setState(95);
					contextDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(100);
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
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ImportDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitImportDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportDeclarationContext importDeclaration() throws RecognitionException {
		ImportDeclarationContext _localctx = new ImportDeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_importDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 14L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(105);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(102);
				identifier();
				setState(103);
				match(T__3);
				}
				break;
			}
			setState(107);
			pathName();
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(108);
				match(T__4);
				setState(109);
				match(T__5);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPackageDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageDeclarationContext packageDeclaration() throws RecognitionException {
		PackageDeclarationContext _localctx = new PackageDeclarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_packageDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(T__6);
			setState(113);
			pathName();
			setState(117);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__8) {
				{
				{
				setState(114);
				contextDeclaration();
				}
				}
				setState(119);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(120);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitContextDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextDeclarationContext contextDeclaration() throws RecognitionException {
		ContextDeclarationContext _localctx = new ContextDeclarationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_contextDeclaration);
		try {
			setState(125);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(122);
				classifierContextDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(123);
				operationContextDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(124);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitClassifierContextDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassifierContextDeclarationContext classifierContextDeclaration() throws RecognitionException {
		ClassifierContextDeclarationContext _localctx = new ClassifierContextDeclarationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_classifierContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			match(T__8);
			setState(131);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(128);
				identifier();
				setState(129);
				match(T__3);
				}
				break;
			}
			setState(133);
			pathName();
			setState(135); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(134);
				classifierContextBody();
				}
				}
				setState(137); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 25600L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitClassifierContextBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassifierContextBodyContext classifierContextBody() throws RecognitionException {
		ClassifierContextBodyContext _localctx = new ClassifierContextBodyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_classifierContextBody);
		try {
			setState(141);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
				enterOuterAlt(_localctx, 1);
				{
				setState(139);
				invariantConstraint();
				}
				break;
			case T__12:
			case T__13:
				enterOuterAlt(_localctx, 2);
				{
				setState(140);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitInvariantConstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InvariantConstraintContext invariantConstraint() throws RecognitionException {
		InvariantConstraintContext _localctx = new InvariantConstraintContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_invariantConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			match(T__9);
			setState(151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(144);
				identifier();
				setState(149);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__10) {
					{
					setState(145);
					match(T__10);
					setState(146);
					expression(0);
					setState(147);
					match(T__11);
					}
				}

				}
			}

			setState(153);
			match(T__3);
			setState(154);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitDefinitionConstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionConstraintContext definitionConstraint() throws RecognitionException {
		DefinitionConstraintContext _localctx = new DefinitionConstraintContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_definitionConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(156);
				match(T__12);
				}
			}

			setState(159);
			match(T__13);
			setState(161);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(160);
				identifier();
				}
			}

			setState(163);
			match(T__3);
			setState(181);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(164);
				identifier();
				setState(165);
				match(T__3);
				setState(166);
				typeExpression();
				setState(167);
				match(T__14);
				setState(168);
				expression(0);
				}
				break;
			case 2:
				{
				setState(170);
				identifier();
				setState(171);
				match(T__10);
				setState(173);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(172);
					parameterList();
					}
				}

				setState(175);
				match(T__11);
				setState(176);
				match(T__3);
				setState(177);
				typeExpression();
				setState(178);
				match(T__14);
				setState(179);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitOperationContextDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperationContextDeclarationContext operationContextDeclaration() throws RecognitionException {
		OperationContextDeclarationContext _localctx = new OperationContextDeclarationContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_operationContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(T__8);
			setState(187);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(184);
				pathName();
				setState(185);
				match(T__4);
				}
				break;
			}
			setState(189);
			identifier();
			setState(190);
			match(T__10);
			setState(192);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
				{
				setState(191);
				parameterList();
				}
			}

			setState(194);
			match(T__11);
			setState(197);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(195);
				match(T__3);
				setState(196);
				typeExpression();
				}
			}

			setState(200); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(199);
				operationContextBody();
				}
				}
				setState(202); 
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitBodyExpression(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPreCondition(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPostCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperationContextBodyContext operationContextBody() throws RecognitionException {
		OperationContextBodyContext _localctx = new OperationContextBodyContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_operationContextBody);
		int _la;
		try {
			setState(222);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__15:
				_localctx = new PreConditionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(204);
				match(T__15);
				setState(206);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(205);
					identifier();
					}
				}

				setState(208);
				match(T__3);
				setState(209);
				expression(0);
				}
				break;
			case T__16:
				_localctx = new PostConditionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(210);
				match(T__16);
				setState(212);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(211);
					identifier();
					}
				}

				setState(214);
				match(T__3);
				setState(215);
				expression(0);
				}
				break;
			case T__17:
				_localctx = new BodyExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(216);
				match(T__17);
				setState(218);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(217);
					identifier();
					}
				}

				setState(220);
				match(T__3);
				setState(221);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPropertyContextDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContextDeclarationContext propertyContextDeclaration() throws RecognitionException {
		PropertyContextDeclarationContext _localctx = new PropertyContextDeclarationContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_propertyContextDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(224);
			match(T__8);
			setState(225);
			pathName();
			setState(226);
			match(T__4);
			setState(227);
			identifier();
			setState(228);
			match(T__3);
			setState(229);
			typeExpression();
			setState(231); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(230);
				propertyContextBody();
				}
				}
				setState(233); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__18 || _la==T__19 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitDeriveExpression(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitInitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContextBodyContext propertyContextBody() throws RecognitionException {
		PropertyContextBodyContext _localctx = new PropertyContextBodyContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_propertyContextBody);
		int _la;
		try {
			setState(247);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__18:
				_localctx = new InitExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(235);
				match(T__18);
				setState(237);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(236);
					identifier();
					}
				}

				setState(239);
				match(T__3);
				setState(240);
				expression(0);
				}
				break;
			case T__19:
				_localctx = new DeriveExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(241);
				match(T__19);
				setState(243);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ESCAPED_IDENTIFIER || _la==IDENTIFIER) {
					{
					setState(242);
					identifier();
					}
				}

				setState(245);
				match(T__3);
				setState(246);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterListContext parameterList() throws RecognitionException {
		ParameterListContext _localctx = new ParameterListContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			parameter();
			setState(254);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__20) {
				{
				{
				setState(250);
				match(T__20);
				setState(251);
				parameter();
				}
				}
				setState(256);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_parameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257);
			identifier();
			setState(258);
			match(T__3);
			setState(259);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitNotExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitAndExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCompareExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitMultExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitAddExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitNavigationExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitImpliesExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPrimaryExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitUnaryMinusExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitEqualityExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitOrExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitXorExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitArrowExp(this);
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
		int _startState = 32;
		enterRecursionRule(_localctx, 32, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__25:
				{
				_localctx = new NotExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(262);
				match(T__25);
				setState(263);
				expression(11);
				}
				break;
			case T__26:
				{
				_localctx = new UnaryMinusExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(264);
				match(T__26);
				setState(265);
				expression(10);
				}
				break;
			case T__5:
			case T__10:
			case T__38:
			case T__39:
			case T__44:
			case T__49:
			case T__50:
			case T__51:
			case T__52:
			case T__55:
			case T__56:
			case T__57:
			case T__58:
			case T__59:
			case T__61:
			case T__62:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case T__70:
			case T__71:
			case T__72:
			case T__73:
			case REAL_LITERAL:
			case INTEGER_LITERAL:
			case STRING_LITERAL:
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				{
				_localctx = new PrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(266);
				primaryExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(301);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(299);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
					case 1:
						{
						_localctx = new MultExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(269);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(270);
						((MultExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__5 || _la==T__27) ) {
							((MultExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(271);
						expression(10);
						}
						break;
					case 2:
						{
						_localctx = new AddExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(272);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(273);
						((AddExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__26 || _la==T__28) ) {
							((AddExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(274);
						expression(9);
						}
						break;
					case 3:
						{
						_localctx = new CompareExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(275);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(276);
						((CompareExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 16106127360L) != 0)) ) {
							((CompareExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(277);
						expression(8);
						}
						break;
					case 4:
						{
						_localctx = new EqualityExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(278);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(279);
						((EqualityExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__14 || _la==T__33) ) {
							((EqualityExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(280);
						expression(7);
						}
						break;
					case 5:
						{
						_localctx = new AndExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(281);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(282);
						match(T__34);
						setState(283);
						expression(6);
						}
						break;
					case 6:
						{
						_localctx = new OrExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(284);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(285);
						match(T__35);
						setState(286);
						expression(5);
						}
						break;
					case 7:
						{
						_localctx = new XorExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(287);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(288);
						match(T__36);
						setState(289);
						expression(4);
						}
						break;
					case 8:
						{
						_localctx = new ImpliesExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(290);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(291);
						match(T__37);
						setState(292);
						expression(3);
						}
						break;
					case 9:
						{
						_localctx = new NavigationExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(293);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(294);
						_la = _input.LA(1);
						if ( !(_la==T__21 || _la==T__22) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(295);
						propertyOrCallSuffix();
						}
						break;
					case 10:
						{
						_localctx = new ArrowExpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(296);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(297);
						_la = _input.LA(1);
						if ( !(_la==T__23 || _la==T__24) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(298);
						iteratorOrOperationCall();
						}
						break;
					}
					} 
				}
				setState(303);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitLiteralExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitMapTypeExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SelfExpContext extends PrimaryExpressionContext {
		public SelfExpContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitSelfExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCollectionTypeExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitIfExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitParenExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPrimitiveTypeExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitLetExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitOperationCallExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitTupleTypeExp(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPathNameExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
		PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_primaryExpression);
		int _la;
		try {
			setState(354);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				_localctx = new ParenExpContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(304);
				match(T__10);
				setState(305);
				expression(0);
				setState(306);
				match(T__11);
				}
				break;
			case 2:
				_localctx = new SelfExpContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(308);
				match(T__38);
				}
				break;
			case 3:
				_localctx = new IfExpContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(309);
				match(T__39);
				setState(310);
				((IfExpContext)_localctx).condition = expression(0);
				setState(311);
				match(T__40);
				setState(312);
				((IfExpContext)_localctx).thenExp = expression(0);
				setState(320);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__41) {
					{
					{
					setState(313);
					match(T__41);
					setState(314);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfCondition.add(((IfExpContext)_localctx).expression);
					setState(315);
					match(T__40);
					setState(316);
					((IfExpContext)_localctx).expression = expression(0);
					((IfExpContext)_localctx).elseIfExp.add(((IfExpContext)_localctx).expression);
					}
					}
					setState(322);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(323);
				match(T__42);
				setState(324);
				((IfExpContext)_localctx).elseExp = expression(0);
				setState(325);
				match(T__43);
				}
				break;
			case 4:
				_localctx = new LetExpContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(327);
				match(T__44);
				setState(328);
				letBinding();
				setState(333);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__20) {
					{
					{
					setState(329);
					match(T__20);
					setState(330);
					letBinding();
					}
					}
					setState(335);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(336);
				match(T__45);
				setState(337);
				expression(0);
				}
				break;
			case 5:
				_localctx = new LiteralExpContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(339);
				literalExpression();
				}
				break;
			case 6:
				_localctx = new OperationCallExpContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(340);
				pathName();
				setState(341);
				match(T__10);
				setState(343);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2360975270808123328L) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 16383L) != 0)) {
					{
					setState(342);
					argumentList();
					}
				}

				setState(345);
				match(T__11);
				setState(347);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
				case 1:
					{
					setState(346);
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
				setState(349);
				primitiveType();
				}
				break;
			case 8:
				_localctx = new CollectionTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(350);
				collectionType();
				}
				break;
			case 9:
				_localctx = new MapTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(351);
				mapType();
				}
				break;
			case 10:
				_localctx = new TupleTypeExpContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(352);
				tupleType();
				}
				break;
			case 11:
				_localctx = new PathNameExpContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(353);
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
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public IsMarkedPreContext isMarkedPre() {
			return getRuleContext(IsMarkedPreContext.class,0);
		}
		public DotCallSuffixContext(PropertyOrCallSuffixContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitDotCallSuffix(this);
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
		public IsMarkedPreContext isMarkedPre() {
			return getRuleContext(IsMarkedPreContext.class,0);
		}
		public PropertySuffixContext(PropertyOrCallSuffixContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPropertySuffix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyOrCallSuffixContext propertyOrCallSuffix() throws RecognitionException {
		PropertyOrCallSuffixContext _localctx = new PropertyOrCallSuffixContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_propertyOrCallSuffix);
		int _la;
		try {
			setState(379);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				_localctx = new DotCallSuffixContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(359);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
				case 1:
					{
					setState(356);
					pathName();
					setState(357);
					match(T__4);
					}
					break;
				}
				setState(361);
				identifier();
				setState(362);
				match(T__10);
				setState(364);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2360975270808123328L) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 16383L) != 0)) {
					{
					setState(363);
					argumentList();
					}
				}

				setState(366);
				match(T__11);
				setState(368);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
				case 1:
					{
					setState(367);
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
				setState(373);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
				case 1:
					{
					setState(370);
					pathName();
					setState(371);
					match(T__4);
					}
					break;
				}
				setState(375);
				identifier();
				setState(377);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
				case 1:
					{
					setState(376);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCollectionOperationCall(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitIterateCall(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitIteratorCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IteratorOrOperationCallContext iteratorOrOperationCall() throws RecognitionException {
		IteratorOrOperationCallContext _localctx = new IteratorOrOperationCallContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_iteratorOrOperationCall);
		int _la;
		try {
			setState(414);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				_localctx = new IteratorCallContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(381);
				identifier();
				setState(382);
				match(T__10);
				setState(383);
				iteratorVariables();
				setState(384);
				match(T__46);
				setState(385);
				expression(0);
				setState(386);
				match(T__11);
				}
				break;
			case 2:
				_localctx = new IterateCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(388);
				identifier();
				setState(389);
				match(T__10);
				setState(390);
				identifier();
				setState(393);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(391);
					match(T__3);
					setState(392);
					((IterateCallContext)_localctx).iterType = typeExpression();
					}
				}

				setState(395);
				match(T__47);
				setState(396);
				identifier();
				setState(399);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(397);
					match(T__3);
					setState(398);
					((IterateCallContext)_localctx).accType = typeExpression();
					}
				}

				setState(401);
				match(T__14);
				setState(402);
				expression(0);
				setState(403);
				match(T__46);
				setState(404);
				expression(0);
				setState(405);
				match(T__11);
				}
				break;
			case 3:
				_localctx = new CollectionOperationCallContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(407);
				identifier();
				setState(408);
				match(T__10);
				setState(410);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2360975270808123328L) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 16383L) != 0)) {
					{
					setState(409);
					argumentList();
					}
				}

				setState(412);
				match(T__11);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitIteratorVariables(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IteratorVariablesContext iteratorVariables() throws RecognitionException {
		IteratorVariablesContext _localctx = new IteratorVariablesContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_iteratorVariables);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416);
			identifier();
			setState(419);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(417);
				match(T__3);
				setState(418);
				typeExpression();
				}
			}

			setState(429);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__20) {
				{
				{
				setState(421);
				match(T__20);
				setState(422);
				identifier();
				setState(425);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(423);
					match(T__3);
					setState(424);
					typeExpression();
					}
				}

				}
				}
				setState(431);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(432);
			expression(0);
			setState(437);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__20) {
				{
				{
				setState(433);
				match(T__20);
				setState(434);
				expression(0);
				}
				}
				setState(439);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitLetBinding(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetBindingContext letBinding() throws RecognitionException {
		LetBindingContext _localctx = new LetBindingContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_letBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(440);
			identifier();
			setState(443);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(441);
				match(T__3);
				setState(442);
				typeExpression();
				}
			}

			setState(445);
			match(T__14);
			setState(446);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitIsMarkedPre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IsMarkedPreContext isMarkedPre() throws RecognitionException {
		IsMarkedPreContext _localctx = new IsMarkedPreContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_isMarkedPre);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(448);
			match(T__48);
			setState(449);
			match(T__15);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode REAL_LITERAL() { return getToken(OclParser.REAL_LITERAL, 0); }
		public RealLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitRealLiteral(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCollectionLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TrueLiteralContext extends LiteralExpressionContext {
		public TrueLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitTrueLiteral(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitTupleLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InvalidLiteralContext extends LiteralExpressionContext {
		public InvalidLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitInvalidLiteral(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitMapLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NullLiteralContext extends LiteralExpressionContext {
		public NullLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitNullLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnlimitedNaturalLiteralContext extends LiteralExpressionContext {
		public UnlimitedNaturalLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitUnlimitedNaturalLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntegerLiteralContext extends LiteralExpressionContext {
		public TerminalNode INTEGER_LITERAL() { return getToken(OclParser.INTEGER_LITERAL, 0); }
		public IntegerLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitIntegerLiteral(this);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitStringLit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FalseLiteralContext extends LiteralExpressionContext {
		public FalseLiteralContext(LiteralExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitFalseLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralExpressionContext literalExpression() throws RecognitionException {
		LiteralExpressionContext _localctx = new LiteralExpressionContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_literalExpression);
		try {
			setState(462);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER_LITERAL:
				_localctx = new IntegerLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(451);
				match(INTEGER_LITERAL);
				}
				break;
			case REAL_LITERAL:
				_localctx = new RealLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(452);
				match(REAL_LITERAL);
				}
				break;
			case STRING_LITERAL:
				_localctx = new StringLitContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(453);
				stringLiteral_();
				}
				break;
			case T__49:
				_localctx = new TrueLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(454);
				match(T__49);
				}
				break;
			case T__50:
				_localctx = new FalseLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(455);
				match(T__50);
				}
				break;
			case T__51:
				_localctx = new NullLiteralContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(456);
				match(T__51);
				}
				break;
			case T__52:
				_localctx = new InvalidLiteralContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(457);
				match(T__52);
				}
				break;
			case T__5:
				_localctx = new UnlimitedNaturalLiteralContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(458);
				match(T__5);
				}
				break;
			case T__55:
			case T__56:
			case T__57:
			case T__58:
			case T__59:
				_localctx = new CollectionLitContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(459);
				collectionLiteral();
				}
				break;
			case T__61:
				_localctx = new TupleLitContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(460);
				tupleLiteral();
				}
				break;
			case T__62:
				_localctx = new MapLitContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(461);
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
		public List<TerminalNode> STRING_LITERAL() { return getTokens(OclParser.STRING_LITERAL); }
		public TerminalNode STRING_LITERAL(int i) {
			return getToken(OclParser.STRING_LITERAL, i);
		}
		public StringLiteral_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral_; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitStringLiteral_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteral_Context stringLiteral_() throws RecognitionException {
		StringLiteral_Context _localctx = new StringLiteral_Context(_ctx, getState());
		enterRule(_localctx, 50, RULE_stringLiteral_);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(465); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(464);
					match(STRING_LITERAL);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(467); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCollectionLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionLiteralContext collectionLiteral() throws RecognitionException {
		CollectionLiteralContext _localctx = new CollectionLiteralContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_collectionLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(469);
			collectionKind();
			setState(470);
			match(T__53);
			setState(479);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2360975270808123328L) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 16383L) != 0)) {
				{
				setState(471);
				collectionLiteralPart();
				setState(476);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__20) {
					{
					{
					setState(472);
					match(T__20);
					setState(473);
					collectionLiteralPart();
					}
					}
					setState(478);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(481);
			match(T__54);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCollectionKind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionKindContext collectionKind() throws RecognitionException {
		CollectionKindContext _localctx = new CollectionKindContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_collectionKind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(483);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2233785415175766016L) != 0)) ) {
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCollectionLiteralPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionLiteralPartContext collectionLiteralPart() throws RecognitionException {
		CollectionLiteralPartContext _localctx = new CollectionLiteralPartContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_collectionLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(485);
			expression(0);
			setState(488);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__60) {
				{
				setState(486);
				match(T__60);
				setState(487);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitTupleLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleLiteralContext tupleLiteral() throws RecognitionException {
		TupleLiteralContext _localctx = new TupleLiteralContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_tupleLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(490);
			match(T__61);
			setState(491);
			match(T__53);
			setState(492);
			tupleLiteralPart();
			setState(497);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__20) {
				{
				{
				setState(493);
				match(T__20);
				setState(494);
				tupleLiteralPart();
				}
				}
				setState(499);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(500);
			match(T__54);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitTupleLiteralPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleLiteralPartContext tupleLiteralPart() throws RecognitionException {
		TupleLiteralPartContext _localctx = new TupleLiteralPartContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_tupleLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(502);
			identifier();
			setState(505);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(503);
				match(T__3);
				setState(504);
				typeExpression();
				}
			}

			setState(507);
			match(T__14);
			setState(508);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitMapLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapLiteralContext mapLiteral() throws RecognitionException {
		MapLiteralContext _localctx = new MapLiteralContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_mapLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(510);
			match(T__62);
			setState(511);
			match(T__53);
			setState(520);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2360975270808123328L) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 16383L) != 0)) {
				{
				setState(512);
				mapLiteralPart();
				setState(517);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__20) {
					{
					{
					setState(513);
					match(T__20);
					setState(514);
					mapLiteralPart();
					}
					}
					setState(519);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(522);
			match(T__54);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitMapLiteralPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapLiteralPartContext mapLiteralPart() throws RecognitionException {
		MapLiteralPartContext _localctx = new MapLiteralPartContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_mapLiteralPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(524);
			expression(0);
			setState(525);
			_la = _input.LA(1);
			if ( !(_la==T__63 || _la==T__64) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(526);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitTypeExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeExpressionContext typeExpression() throws RecognitionException {
		TypeExpressionContext _localctx = new TypeExpressionContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_typeExpression);
		try {
			setState(533);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case T__70:
			case T__71:
			case T__72:
			case T__73:
				enterOuterAlt(_localctx, 1);
				{
				setState(528);
				primitiveType();
				}
				break;
			case T__55:
			case T__56:
			case T__57:
			case T__58:
			case T__59:
				enterOuterAlt(_localctx, 2);
				{
				setState(529);
				collectionType();
				}
				break;
			case T__62:
				enterOuterAlt(_localctx, 3);
				{
				setState(530);
				mapType();
				}
				break;
			case T__61:
				enterOuterAlt(_localctx, 4);
				{
				setState(531);
				tupleType();
				}
				break;
			case ESCAPED_IDENTIFIER:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 5);
				{
				setState(532);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPrimitiveType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(535);
			_la = _input.LA(1);
			if ( !(((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 511L) != 0)) ) {
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitCollectionType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionTypeContext collectionType() throws RecognitionException {
		CollectionTypeContext _localctx = new CollectionTypeContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_collectionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(537);
			collectionKind();
			setState(538);
			match(T__10);
			setState(539);
			typeExpression();
			setState(540);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitMapType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapTypeContext mapType() throws RecognitionException {
		MapTypeContext _localctx = new MapTypeContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_mapType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(542);
			match(T__62);
			setState(543);
			match(T__10);
			setState(544);
			typeExpression();
			setState(545);
			match(T__20);
			setState(546);
			typeExpression();
			setState(547);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitTupleType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleTypeContext tupleType() throws RecognitionException {
		TupleTypeContext _localctx = new TupleTypeContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_tupleType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(549);
			match(T__61);
			setState(550);
			match(T__10);
			setState(551);
			tupleTypePart();
			setState(556);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__20) {
				{
				{
				setState(552);
				match(T__20);
				setState(553);
				tupleTypePart();
				}
				}
				setState(558);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(559);
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
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitTupleTypePart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleTypePartContext tupleTypePart() throws RecognitionException {
		TupleTypePartContext _localctx = new TupleTypePartContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_tupleTypePart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(561);
			identifier();
			setState(562);
			match(T__3);
			setState(563);
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
		public PathNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitPathName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathNameContext pathName() throws RecognitionException {
		PathNameContext _localctx = new PathNameContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_pathName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(565);
			identifier();
			setState(570);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(566);
					match(T__4);
					setState(567);
					identifier();
					}
					} 
				}
				setState(572);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode IDENTIFIER() { return getToken(OclParser.IDENTIFIER, 0); }
		public TerminalNode ESCAPED_IDENTIFIER() { return getToken(OclParser.ESCAPED_IDENTIFIER, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OclVisitor ) return ((OclVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_identifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(573);
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
		case 16:
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
		"\u0004\u0001R\u0240\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"(\u0007(\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0005\u0002Z\b\u0002\n\u0002\f\u0002]\t\u0002\u0001"+
		"\u0002\u0001\u0002\u0005\u0002a\b\u0002\n\u0002\f\u0002d\t\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003j\b\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0003\u0003o\b\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0005\u0004t\b\u0004\n\u0004\f\u0004w\t\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005~\b"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006\u0084"+
		"\b\u0006\u0001\u0006\u0001\u0006\u0004\u0006\u0088\b\u0006\u000b\u0006"+
		"\f\u0006\u0089\u0001\u0007\u0001\u0007\u0003\u0007\u008e\b\u0007\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003\b\u0096\b\b\u0003\b\u0098"+
		"\b\b\u0001\b\u0001\b\u0001\b\u0001\t\u0003\t\u009e\b\t\u0001\t\u0001\t"+
		"\u0003\t\u00a2\b\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0003\t\u00ae\b\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0003\t\u00b6\b\t\u0001\n\u0001\n\u0001\n\u0001\n\u0003"+
		"\n\u00bc\b\n\u0001\n\u0001\n\u0001\n\u0003\n\u00c1\b\n\u0001\n\u0001\n"+
		"\u0001\n\u0003\n\u00c6\b\n\u0001\n\u0004\n\u00c9\b\n\u000b\n\f\n\u00ca"+
		"\u0001\u000b\u0001\u000b\u0003\u000b\u00cf\b\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0003\u000b\u00d5\b\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0003\u000b\u00db\b\u000b\u0001\u000b\u0001\u000b"+
		"\u0003\u000b\u00df\b\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0004\f\u00e8\b\f\u000b\f\f\f\u00e9\u0001\r\u0001\r\u0003\r"+
		"\u00ee\b\r\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u00f4\b\r\u0001\r\u0001"+
		"\r\u0003\r\u00f8\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000e\u00fd"+
		"\b\u000e\n\u000e\f\u000e\u0100\t\u000e\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0003\u0010\u010c\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0005\u0010\u012c\b\u0010\n\u0010"+
		"\f\u0010\u012f\t\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u013f\b\u0011"+
		"\n\u0011\f\u0011\u0142\t\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u014c"+
		"\b\u0011\n\u0011\f\u0011\u014f\t\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u0158\b\u0011"+
		"\u0001\u0011\u0001\u0011\u0003\u0011\u015c\b\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u0163\b\u0011\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0003\u0012\u0168\b\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0003\u0012\u016d\b\u0012\u0001\u0012\u0001\u0012\u0003\u0012"+
		"\u0171\b\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0176\b"+
		"\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u017a\b\u0012\u0003\u0012\u017c"+
		"\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0003\u0013\u018a\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0003\u0013\u0190\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003"+
		"\u0013\u019b\b\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u019f\b\u0013"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u01a4\b\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u01aa\b\u0014\u0005\u0014"+
		"\u01ac\b\u0014\n\u0014\f\u0014\u01af\t\u0014\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0005\u0015\u01b4\b\u0015\n\u0015\f\u0015\u01b7\t\u0015\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0003\u0016\u01bc\b\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u01cf\b\u0018\u0001\u0019"+
		"\u0004\u0019\u01d2\b\u0019\u000b\u0019\f\u0019\u01d3\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0005\u001a\u01db\b\u001a\n"+
		"\u001a\f\u001a\u01de\t\u001a\u0003\u001a\u01e0\b\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0003"+
		"\u001c\u01e9\b\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0005\u001d\u01f0\b\u001d\n\u001d\f\u001d\u01f3\t\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e\u01fa\b\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0005\u001f\u0204\b\u001f\n\u001f\f\u001f\u0207"+
		"\t\u001f\u0003\u001f\u0209\b\u001f\u0001\u001f\u0001\u001f\u0001 \u0001"+
		" \u0001 \u0001 \u0001!\u0001!\u0001!\u0001!\u0001!\u0003!\u0216\b!\u0001"+
		"\"\u0001\"\u0001#\u0001#\u0001#\u0001#\u0001#\u0001$\u0001$\u0001$\u0001"+
		"$\u0001$\u0001$\u0001$\u0001%\u0001%\u0001%\u0001%\u0001%\u0005%\u022b"+
		"\b%\n%\f%\u022e\t%\u0001%\u0001%\u0001&\u0001&\u0001&\u0001&\u0001\'\u0001"+
		"\'\u0001\'\u0005\'\u0239\b\'\n\'\f\'\u023c\t\'\u0001(\u0001(\u0001(\u0000"+
		"\u0001 )\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018"+
		"\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNP\u0000\u000b\u0001\u0000\u0001"+
		"\u0003\u0002\u0000\u0006\u0006\u001c\u001c\u0002\u0000\u001b\u001b\u001d"+
		"\u001d\u0001\u0000\u001e!\u0002\u0000\u000f\u000f\"\"\u0001\u0000\u0016"+
		"\u0017\u0001\u0000\u0018\u0019\u0001\u00008<\u0001\u0000@A\u0001\u0000"+
		"BJ\u0001\u0000NO\u0277\u0000R\u0001\u0000\u0000\u0000\u0002U\u0001\u0000"+
		"\u0000\u0000\u0004[\u0001\u0000\u0000\u0000\u0006e\u0001\u0000\u0000\u0000"+
		"\bp\u0001\u0000\u0000\u0000\n}\u0001\u0000\u0000\u0000\f\u007f\u0001\u0000"+
		"\u0000\u0000\u000e\u008d\u0001\u0000\u0000\u0000\u0010\u008f\u0001\u0000"+
		"\u0000\u0000\u0012\u009d\u0001\u0000\u0000\u0000\u0014\u00b7\u0001\u0000"+
		"\u0000\u0000\u0016\u00de\u0001\u0000\u0000\u0000\u0018\u00e0\u0001\u0000"+
		"\u0000\u0000\u001a\u00f7\u0001\u0000\u0000\u0000\u001c\u00f9\u0001\u0000"+
		"\u0000\u0000\u001e\u0101\u0001\u0000\u0000\u0000 \u010b\u0001\u0000\u0000"+
		"\u0000\"\u0162\u0001\u0000\u0000\u0000$\u017b\u0001\u0000\u0000\u0000"+
		"&\u019e\u0001\u0000\u0000\u0000(\u01a0\u0001\u0000\u0000\u0000*\u01b0"+
		"\u0001\u0000\u0000\u0000,\u01b8\u0001\u0000\u0000\u0000.\u01c0\u0001\u0000"+
		"\u0000\u00000\u01ce\u0001\u0000\u0000\u00002\u01d1\u0001\u0000\u0000\u0000"+
		"4\u01d5\u0001\u0000\u0000\u00006\u01e3\u0001\u0000\u0000\u00008\u01e5"+
		"\u0001\u0000\u0000\u0000:\u01ea\u0001\u0000\u0000\u0000<\u01f6\u0001\u0000"+
		"\u0000\u0000>\u01fe\u0001\u0000\u0000\u0000@\u020c\u0001\u0000\u0000\u0000"+
		"B\u0215\u0001\u0000\u0000\u0000D\u0217\u0001\u0000\u0000\u0000F\u0219"+
		"\u0001\u0000\u0000\u0000H\u021e\u0001\u0000\u0000\u0000J\u0225\u0001\u0000"+
		"\u0000\u0000L\u0231\u0001\u0000\u0000\u0000N\u0235\u0001\u0000\u0000\u0000"+
		"P\u023d\u0001\u0000\u0000\u0000RS\u0003 \u0010\u0000ST\u0005\u0000\u0000"+
		"\u0001T\u0001\u0001\u0000\u0000\u0000UV\u0003\u0004\u0002\u0000VW\u0005"+
		"\u0000\u0000\u0001W\u0003\u0001\u0000\u0000\u0000XZ\u0003\u0006\u0003"+
		"\u0000YX\u0001\u0000\u0000\u0000Z]\u0001\u0000\u0000\u0000[Y\u0001\u0000"+
		"\u0000\u0000[\\\u0001\u0000\u0000\u0000\\b\u0001\u0000\u0000\u0000][\u0001"+
		"\u0000\u0000\u0000^a\u0003\b\u0004\u0000_a\u0003\n\u0005\u0000`^\u0001"+
		"\u0000\u0000\u0000`_\u0001\u0000\u0000\u0000ad\u0001\u0000\u0000\u0000"+
		"b`\u0001\u0000\u0000\u0000bc\u0001\u0000\u0000\u0000c\u0005\u0001\u0000"+
		"\u0000\u0000db\u0001\u0000\u0000\u0000ei\u0007\u0000\u0000\u0000fg\u0003"+
		"P(\u0000gh\u0005\u0004\u0000\u0000hj\u0001\u0000\u0000\u0000if\u0001\u0000"+
		"\u0000\u0000ij\u0001\u0000\u0000\u0000jk\u0001\u0000\u0000\u0000kn\u0003"+
		"N\'\u0000lm\u0005\u0005\u0000\u0000mo\u0005\u0006\u0000\u0000nl\u0001"+
		"\u0000\u0000\u0000no\u0001\u0000\u0000\u0000o\u0007\u0001\u0000\u0000"+
		"\u0000pq\u0005\u0007\u0000\u0000qu\u0003N\'\u0000rt\u0003\n\u0005\u0000"+
		"sr\u0001\u0000\u0000\u0000tw\u0001\u0000\u0000\u0000us\u0001\u0000\u0000"+
		"\u0000uv\u0001\u0000\u0000\u0000vx\u0001\u0000\u0000\u0000wu\u0001\u0000"+
		"\u0000\u0000xy\u0005\b\u0000\u0000y\t\u0001\u0000\u0000\u0000z~\u0003"+
		"\f\u0006\u0000{~\u0003\u0014\n\u0000|~\u0003\u0018\f\u0000}z\u0001\u0000"+
		"\u0000\u0000}{\u0001\u0000\u0000\u0000}|\u0001\u0000\u0000\u0000~\u000b"+
		"\u0001\u0000\u0000\u0000\u007f\u0083\u0005\t\u0000\u0000\u0080\u0081\u0003"+
		"P(\u0000\u0081\u0082\u0005\u0004\u0000\u0000\u0082\u0084\u0001\u0000\u0000"+
		"\u0000\u0083\u0080\u0001\u0000\u0000\u0000\u0083\u0084\u0001\u0000\u0000"+
		"\u0000\u0084\u0085\u0001\u0000\u0000\u0000\u0085\u0087\u0003N\'\u0000"+
		"\u0086\u0088\u0003\u000e\u0007\u0000\u0087\u0086\u0001\u0000\u0000\u0000"+
		"\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u0087\u0001\u0000\u0000\u0000"+
		"\u0089\u008a\u0001\u0000\u0000\u0000\u008a\r\u0001\u0000\u0000\u0000\u008b"+
		"\u008e\u0003\u0010\b\u0000\u008c\u008e\u0003\u0012\t\u0000\u008d\u008b"+
		"\u0001\u0000\u0000\u0000\u008d\u008c\u0001\u0000\u0000\u0000\u008e\u000f"+
		"\u0001\u0000\u0000\u0000\u008f\u0097\u0005\n\u0000\u0000\u0090\u0095\u0003"+
		"P(\u0000\u0091\u0092\u0005\u000b\u0000\u0000\u0092\u0093\u0003 \u0010"+
		"\u0000\u0093\u0094\u0005\f\u0000\u0000\u0094\u0096\u0001\u0000\u0000\u0000"+
		"\u0095\u0091\u0001\u0000\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000"+
		"\u0096\u0098\u0001\u0000\u0000\u0000\u0097\u0090\u0001\u0000\u0000\u0000"+
		"\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u0099\u0001\u0000\u0000\u0000"+
		"\u0099\u009a\u0005\u0004\u0000\u0000\u009a\u009b\u0003 \u0010\u0000\u009b"+
		"\u0011\u0001\u0000\u0000\u0000\u009c\u009e\u0005\r\u0000\u0000\u009d\u009c"+
		"\u0001\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000\u0000\u009e\u009f"+
		"\u0001\u0000\u0000\u0000\u009f\u00a1\u0005\u000e\u0000\u0000\u00a0\u00a2"+
		"\u0003P(\u0000\u00a1\u00a0\u0001\u0000\u0000\u0000\u00a1\u00a2\u0001\u0000"+
		"\u0000\u0000\u00a2\u00a3\u0001\u0000\u0000\u0000\u00a3\u00b5\u0005\u0004"+
		"\u0000\u0000\u00a4\u00a5\u0003P(\u0000\u00a5\u00a6\u0005\u0004\u0000\u0000"+
		"\u00a6\u00a7\u0003B!\u0000\u00a7\u00a8\u0005\u000f\u0000\u0000\u00a8\u00a9"+
		"\u0003 \u0010\u0000\u00a9\u00b6\u0001\u0000\u0000\u0000\u00aa\u00ab\u0003"+
		"P(\u0000\u00ab\u00ad\u0005\u000b\u0000\u0000\u00ac\u00ae\u0003\u001c\u000e"+
		"\u0000\u00ad\u00ac\u0001\u0000\u0000\u0000\u00ad\u00ae\u0001\u0000\u0000"+
		"\u0000\u00ae\u00af\u0001\u0000\u0000\u0000\u00af\u00b0\u0005\f\u0000\u0000"+
		"\u00b0\u00b1\u0005\u0004\u0000\u0000\u00b1\u00b2\u0003B!\u0000\u00b2\u00b3"+
		"\u0005\u000f\u0000\u0000\u00b3\u00b4\u0003 \u0010\u0000\u00b4\u00b6\u0001"+
		"\u0000\u0000\u0000\u00b5\u00a4\u0001\u0000\u0000\u0000\u00b5\u00aa\u0001"+
		"\u0000\u0000\u0000\u00b6\u0013\u0001\u0000\u0000\u0000\u00b7\u00bb\u0005"+
		"\t\u0000\u0000\u00b8\u00b9\u0003N\'\u0000\u00b9\u00ba\u0005\u0005\u0000"+
		"\u0000\u00ba\u00bc\u0001\u0000\u0000\u0000\u00bb\u00b8\u0001\u0000\u0000"+
		"\u0000\u00bb\u00bc\u0001\u0000\u0000\u0000\u00bc\u00bd\u0001\u0000\u0000"+
		"\u0000\u00bd\u00be\u0003P(\u0000\u00be\u00c0\u0005\u000b\u0000\u0000\u00bf"+
		"\u00c1\u0003\u001c\u000e\u0000\u00c0\u00bf\u0001\u0000\u0000\u0000\u00c0"+
		"\u00c1\u0001\u0000\u0000\u0000\u00c1\u00c2\u0001\u0000\u0000\u0000\u00c2"+
		"\u00c5\u0005\f\u0000\u0000\u00c3\u00c4\u0005\u0004\u0000\u0000\u00c4\u00c6"+
		"\u0003B!\u0000\u00c5\u00c3\u0001\u0000\u0000\u0000\u00c5\u00c6\u0001\u0000"+
		"\u0000\u0000\u00c6\u00c8\u0001\u0000\u0000\u0000\u00c7\u00c9\u0003\u0016"+
		"\u000b\u0000\u00c8\u00c7\u0001\u0000\u0000\u0000\u00c9\u00ca\u0001\u0000"+
		"\u0000\u0000\u00ca\u00c8\u0001\u0000\u0000\u0000\u00ca\u00cb\u0001\u0000"+
		"\u0000\u0000\u00cb\u0015\u0001\u0000\u0000\u0000\u00cc\u00ce\u0005\u0010"+
		"\u0000\u0000\u00cd\u00cf\u0003P(\u0000\u00ce\u00cd\u0001\u0000\u0000\u0000"+
		"\u00ce\u00cf\u0001\u0000\u0000\u0000\u00cf\u00d0\u0001\u0000\u0000\u0000"+
		"\u00d0\u00d1\u0005\u0004\u0000\u0000\u00d1\u00df\u0003 \u0010\u0000\u00d2"+
		"\u00d4\u0005\u0011\u0000\u0000\u00d3\u00d5\u0003P(\u0000\u00d4\u00d3\u0001"+
		"\u0000\u0000\u0000\u00d4\u00d5\u0001\u0000\u0000\u0000\u00d5\u00d6\u0001"+
		"\u0000\u0000\u0000\u00d6\u00d7\u0005\u0004\u0000\u0000\u00d7\u00df\u0003"+
		" \u0010\u0000\u00d8\u00da\u0005\u0012\u0000\u0000\u00d9\u00db\u0003P("+
		"\u0000\u00da\u00d9\u0001\u0000\u0000\u0000\u00da\u00db\u0001\u0000\u0000"+
		"\u0000\u00db\u00dc\u0001\u0000\u0000\u0000\u00dc\u00dd\u0005\u0004\u0000"+
		"\u0000\u00dd\u00df\u0003 \u0010\u0000\u00de\u00cc\u0001\u0000\u0000\u0000"+
		"\u00de\u00d2\u0001\u0000\u0000\u0000\u00de\u00d8\u0001\u0000\u0000\u0000"+
		"\u00df\u0017\u0001\u0000\u0000\u0000\u00e0\u00e1\u0005\t\u0000\u0000\u00e1"+
		"\u00e2\u0003N\'\u0000\u00e2\u00e3\u0005\u0005\u0000\u0000\u00e3\u00e4"+
		"\u0003P(\u0000\u00e4\u00e5\u0005\u0004\u0000\u0000\u00e5\u00e7\u0003B"+
		"!\u0000\u00e6\u00e8\u0003\u001a\r\u0000\u00e7\u00e6\u0001\u0000\u0000"+
		"\u0000\u00e8\u00e9\u0001\u0000\u0000\u0000\u00e9\u00e7\u0001\u0000\u0000"+
		"\u0000\u00e9\u00ea\u0001\u0000\u0000\u0000\u00ea\u0019\u0001\u0000\u0000"+
		"\u0000\u00eb\u00ed\u0005\u0013\u0000\u0000\u00ec\u00ee\u0003P(\u0000\u00ed"+
		"\u00ec\u0001\u0000\u0000\u0000\u00ed\u00ee\u0001\u0000\u0000\u0000\u00ee"+
		"\u00ef\u0001\u0000\u0000\u0000\u00ef\u00f0\u0005\u0004\u0000\u0000\u00f0"+
		"\u00f8\u0003 \u0010\u0000\u00f1\u00f3\u0005\u0014\u0000\u0000\u00f2\u00f4"+
		"\u0003P(\u0000\u00f3\u00f2\u0001\u0000\u0000\u0000\u00f3\u00f4\u0001\u0000"+
		"\u0000\u0000\u00f4\u00f5\u0001\u0000\u0000\u0000\u00f5\u00f6\u0005\u0004"+
		"\u0000\u0000\u00f6\u00f8\u0003 \u0010\u0000\u00f7\u00eb\u0001\u0000\u0000"+
		"\u0000\u00f7\u00f1\u0001\u0000\u0000\u0000\u00f8\u001b\u0001\u0000\u0000"+
		"\u0000\u00f9\u00fe\u0003\u001e\u000f\u0000\u00fa\u00fb\u0005\u0015\u0000"+
		"\u0000\u00fb\u00fd\u0003\u001e\u000f\u0000\u00fc\u00fa\u0001\u0000\u0000"+
		"\u0000\u00fd\u0100\u0001\u0000\u0000\u0000\u00fe\u00fc\u0001\u0000\u0000"+
		"\u0000\u00fe\u00ff\u0001\u0000\u0000\u0000\u00ff\u001d\u0001\u0000\u0000"+
		"\u0000\u0100\u00fe\u0001\u0000\u0000\u0000\u0101\u0102\u0003P(\u0000\u0102"+
		"\u0103\u0005\u0004\u0000\u0000\u0103\u0104\u0003B!\u0000\u0104\u001f\u0001"+
		"\u0000\u0000\u0000\u0105\u0106\u0006\u0010\uffff\uffff\u0000\u0106\u0107"+
		"\u0005\u001a\u0000\u0000\u0107\u010c\u0003 \u0010\u000b\u0108\u0109\u0005"+
		"\u001b\u0000\u0000\u0109\u010c\u0003 \u0010\n\u010a\u010c\u0003\"\u0011"+
		"\u0000\u010b\u0105\u0001\u0000\u0000\u0000\u010b\u0108\u0001\u0000\u0000"+
		"\u0000\u010b\u010a\u0001\u0000\u0000\u0000\u010c\u012d\u0001\u0000\u0000"+
		"\u0000\u010d\u010e\n\t\u0000\u0000\u010e\u010f\u0007\u0001\u0000\u0000"+
		"\u010f\u012c\u0003 \u0010\n\u0110\u0111\n\b\u0000\u0000\u0111\u0112\u0007"+
		"\u0002\u0000\u0000\u0112\u012c\u0003 \u0010\t\u0113\u0114\n\u0007\u0000"+
		"\u0000\u0114\u0115\u0007\u0003\u0000\u0000\u0115\u012c\u0003 \u0010\b"+
		"\u0116\u0117\n\u0006\u0000\u0000\u0117\u0118\u0007\u0004\u0000\u0000\u0118"+
		"\u012c\u0003 \u0010\u0007\u0119\u011a\n\u0005\u0000\u0000\u011a\u011b"+
		"\u0005#\u0000\u0000\u011b\u012c\u0003 \u0010\u0006\u011c\u011d\n\u0004"+
		"\u0000\u0000\u011d\u011e\u0005$\u0000\u0000\u011e\u012c\u0003 \u0010\u0005"+
		"\u011f\u0120\n\u0003\u0000\u0000\u0120\u0121\u0005%\u0000\u0000\u0121"+
		"\u012c\u0003 \u0010\u0004\u0122\u0123\n\u0002\u0000\u0000\u0123\u0124"+
		"\u0005&\u0000\u0000\u0124\u012c\u0003 \u0010\u0003\u0125\u0126\n\r\u0000"+
		"\u0000\u0126\u0127\u0007\u0005\u0000\u0000\u0127\u012c\u0003$\u0012\u0000"+
		"\u0128\u0129\n\f\u0000\u0000\u0129\u012a\u0007\u0006\u0000\u0000\u012a"+
		"\u012c\u0003&\u0013\u0000\u012b\u010d\u0001\u0000\u0000\u0000\u012b\u0110"+
		"\u0001\u0000\u0000\u0000\u012b\u0113\u0001\u0000\u0000\u0000\u012b\u0116"+
		"\u0001\u0000\u0000\u0000\u012b\u0119\u0001\u0000\u0000\u0000\u012b\u011c"+
		"\u0001\u0000\u0000\u0000\u012b\u011f\u0001\u0000\u0000\u0000\u012b\u0122"+
		"\u0001\u0000\u0000\u0000\u012b\u0125\u0001\u0000\u0000\u0000\u012b\u0128"+
		"\u0001\u0000\u0000\u0000\u012c\u012f\u0001\u0000\u0000\u0000\u012d\u012b"+
		"\u0001\u0000\u0000\u0000\u012d\u012e\u0001\u0000\u0000\u0000\u012e!\u0001"+
		"\u0000\u0000\u0000\u012f\u012d\u0001\u0000\u0000\u0000\u0130\u0131\u0005"+
		"\u000b\u0000\u0000\u0131\u0132\u0003 \u0010\u0000\u0132\u0133\u0005\f"+
		"\u0000\u0000\u0133\u0163\u0001\u0000\u0000\u0000\u0134\u0163\u0005\'\u0000"+
		"\u0000\u0135\u0136\u0005(\u0000\u0000\u0136\u0137\u0003 \u0010\u0000\u0137"+
		"\u0138\u0005)\u0000\u0000\u0138\u0140\u0003 \u0010\u0000\u0139\u013a\u0005"+
		"*\u0000\u0000\u013a\u013b\u0003 \u0010\u0000\u013b\u013c\u0005)\u0000"+
		"\u0000\u013c\u013d\u0003 \u0010\u0000\u013d\u013f\u0001\u0000\u0000\u0000"+
		"\u013e\u0139\u0001\u0000\u0000\u0000\u013f\u0142\u0001\u0000\u0000\u0000"+
		"\u0140\u013e\u0001\u0000\u0000\u0000\u0140\u0141\u0001\u0000\u0000\u0000"+
		"\u0141\u0143\u0001\u0000\u0000\u0000\u0142\u0140\u0001\u0000\u0000\u0000"+
		"\u0143\u0144\u0005+\u0000\u0000\u0144\u0145\u0003 \u0010\u0000\u0145\u0146"+
		"\u0005,\u0000\u0000\u0146\u0163\u0001\u0000\u0000\u0000\u0147\u0148\u0005"+
		"-\u0000\u0000\u0148\u014d\u0003,\u0016\u0000\u0149\u014a\u0005\u0015\u0000"+
		"\u0000\u014a\u014c\u0003,\u0016\u0000\u014b\u0149\u0001\u0000\u0000\u0000"+
		"\u014c\u014f\u0001\u0000\u0000\u0000\u014d\u014b\u0001\u0000\u0000\u0000"+
		"\u014d\u014e\u0001\u0000\u0000\u0000\u014e\u0150\u0001\u0000\u0000\u0000"+
		"\u014f\u014d\u0001\u0000\u0000\u0000\u0150\u0151\u0005.\u0000\u0000\u0151"+
		"\u0152\u0003 \u0010\u0000\u0152\u0163\u0001\u0000\u0000\u0000\u0153\u0163"+
		"\u00030\u0018\u0000\u0154\u0155\u0003N\'\u0000\u0155\u0157\u0005\u000b"+
		"\u0000\u0000\u0156\u0158\u0003*\u0015\u0000\u0157\u0156\u0001\u0000\u0000"+
		"\u0000\u0157\u0158\u0001\u0000\u0000\u0000\u0158\u0159\u0001\u0000\u0000"+
		"\u0000\u0159\u015b\u0005\f\u0000\u0000\u015a\u015c\u0003.\u0017\u0000"+
		"\u015b\u015a\u0001\u0000\u0000\u0000\u015b\u015c\u0001\u0000\u0000\u0000"+
		"\u015c\u0163\u0001\u0000\u0000\u0000\u015d\u0163\u0003D\"\u0000\u015e"+
		"\u0163\u0003F#\u0000\u015f\u0163\u0003H$\u0000\u0160\u0163\u0003J%\u0000"+
		"\u0161\u0163\u0003N\'\u0000\u0162\u0130\u0001\u0000\u0000\u0000\u0162"+
		"\u0134\u0001\u0000\u0000\u0000\u0162\u0135\u0001\u0000\u0000\u0000\u0162"+
		"\u0147\u0001\u0000\u0000\u0000\u0162\u0153\u0001\u0000\u0000\u0000\u0162"+
		"\u0154\u0001\u0000\u0000\u0000\u0162\u015d\u0001\u0000\u0000\u0000\u0162"+
		"\u015e\u0001\u0000\u0000\u0000\u0162\u015f\u0001\u0000\u0000\u0000\u0162"+
		"\u0160\u0001\u0000\u0000\u0000\u0162\u0161\u0001\u0000\u0000\u0000\u0163"+
		"#\u0001\u0000\u0000\u0000\u0164\u0165\u0003N\'\u0000\u0165\u0166\u0005"+
		"\u0005\u0000\u0000\u0166\u0168\u0001\u0000\u0000\u0000\u0167\u0164\u0001"+
		"\u0000\u0000\u0000\u0167\u0168\u0001\u0000\u0000\u0000\u0168\u0169\u0001"+
		"\u0000\u0000\u0000\u0169\u016a\u0003P(\u0000\u016a\u016c\u0005\u000b\u0000"+
		"\u0000\u016b\u016d\u0003*\u0015\u0000\u016c\u016b\u0001\u0000\u0000\u0000"+
		"\u016c\u016d\u0001\u0000\u0000\u0000\u016d\u016e\u0001\u0000\u0000\u0000"+
		"\u016e\u0170\u0005\f\u0000\u0000\u016f\u0171\u0003.\u0017\u0000\u0170"+
		"\u016f\u0001\u0000\u0000\u0000\u0170\u0171\u0001\u0000\u0000\u0000\u0171"+
		"\u017c\u0001\u0000\u0000\u0000\u0172\u0173\u0003N\'\u0000\u0173\u0174"+
		"\u0005\u0005\u0000\u0000\u0174\u0176\u0001\u0000\u0000\u0000\u0175\u0172"+
		"\u0001\u0000\u0000\u0000\u0175\u0176\u0001\u0000\u0000\u0000\u0176\u0177"+
		"\u0001\u0000\u0000\u0000\u0177\u0179\u0003P(\u0000\u0178\u017a\u0003."+
		"\u0017\u0000\u0179\u0178\u0001\u0000\u0000\u0000\u0179\u017a\u0001\u0000"+
		"\u0000\u0000\u017a\u017c\u0001\u0000\u0000\u0000\u017b\u0167\u0001\u0000"+
		"\u0000\u0000\u017b\u0175\u0001\u0000\u0000\u0000\u017c%\u0001\u0000\u0000"+
		"\u0000\u017d\u017e\u0003P(\u0000\u017e\u017f\u0005\u000b\u0000\u0000\u017f"+
		"\u0180\u0003(\u0014\u0000\u0180\u0181\u0005/\u0000\u0000\u0181\u0182\u0003"+
		" \u0010\u0000\u0182\u0183\u0005\f\u0000\u0000\u0183\u019f\u0001\u0000"+
		"\u0000\u0000\u0184\u0185\u0003P(\u0000\u0185\u0186\u0005\u000b\u0000\u0000"+
		"\u0186\u0189\u0003P(\u0000\u0187\u0188\u0005\u0004\u0000\u0000\u0188\u018a"+
		"\u0003B!\u0000\u0189\u0187\u0001\u0000\u0000\u0000\u0189\u018a\u0001\u0000"+
		"\u0000\u0000\u018a\u018b\u0001\u0000\u0000\u0000\u018b\u018c\u00050\u0000"+
		"\u0000\u018c\u018f\u0003P(\u0000\u018d\u018e\u0005\u0004\u0000\u0000\u018e"+
		"\u0190\u0003B!\u0000\u018f\u018d\u0001\u0000\u0000\u0000\u018f\u0190\u0001"+
		"\u0000\u0000\u0000\u0190\u0191\u0001\u0000\u0000\u0000\u0191\u0192\u0005"+
		"\u000f\u0000\u0000\u0192\u0193\u0003 \u0010\u0000\u0193\u0194\u0005/\u0000"+
		"\u0000\u0194\u0195\u0003 \u0010\u0000\u0195\u0196\u0005\f\u0000\u0000"+
		"\u0196\u019f\u0001\u0000\u0000\u0000\u0197\u0198\u0003P(\u0000\u0198\u019a"+
		"\u0005\u000b\u0000\u0000\u0199\u019b\u0003*\u0015\u0000\u019a\u0199\u0001"+
		"\u0000\u0000\u0000\u019a\u019b\u0001\u0000\u0000\u0000\u019b\u019c\u0001"+
		"\u0000\u0000\u0000\u019c\u019d\u0005\f\u0000\u0000\u019d\u019f\u0001\u0000"+
		"\u0000\u0000\u019e\u017d\u0001\u0000\u0000\u0000\u019e\u0184\u0001\u0000"+
		"\u0000\u0000\u019e\u0197\u0001\u0000\u0000\u0000\u019f\'\u0001\u0000\u0000"+
		"\u0000\u01a0\u01a3\u0003P(\u0000\u01a1\u01a2\u0005\u0004\u0000\u0000\u01a2"+
		"\u01a4\u0003B!\u0000\u01a3\u01a1\u0001\u0000\u0000\u0000\u01a3\u01a4\u0001"+
		"\u0000\u0000\u0000\u01a4\u01ad\u0001\u0000\u0000\u0000\u01a5\u01a6\u0005"+
		"\u0015\u0000\u0000\u01a6\u01a9\u0003P(\u0000\u01a7\u01a8\u0005\u0004\u0000"+
		"\u0000\u01a8\u01aa\u0003B!\u0000\u01a9\u01a7\u0001\u0000\u0000\u0000\u01a9"+
		"\u01aa\u0001\u0000\u0000\u0000\u01aa\u01ac\u0001\u0000\u0000\u0000\u01ab"+
		"\u01a5\u0001\u0000\u0000\u0000\u01ac\u01af\u0001\u0000\u0000\u0000\u01ad"+
		"\u01ab\u0001\u0000\u0000\u0000\u01ad\u01ae\u0001\u0000\u0000\u0000\u01ae"+
		")\u0001\u0000\u0000\u0000\u01af\u01ad\u0001\u0000\u0000\u0000\u01b0\u01b5"+
		"\u0003 \u0010\u0000\u01b1\u01b2\u0005\u0015\u0000\u0000\u01b2\u01b4\u0003"+
		" \u0010\u0000\u01b3\u01b1\u0001\u0000\u0000\u0000\u01b4\u01b7\u0001\u0000"+
		"\u0000\u0000\u01b5\u01b3\u0001\u0000\u0000\u0000\u01b5\u01b6\u0001\u0000"+
		"\u0000\u0000\u01b6+\u0001\u0000\u0000\u0000\u01b7\u01b5\u0001\u0000\u0000"+
		"\u0000\u01b8\u01bb\u0003P(\u0000\u01b9\u01ba\u0005\u0004\u0000\u0000\u01ba"+
		"\u01bc\u0003B!\u0000\u01bb\u01b9\u0001\u0000\u0000\u0000\u01bb\u01bc\u0001"+
		"\u0000\u0000\u0000\u01bc\u01bd\u0001\u0000\u0000\u0000\u01bd\u01be\u0005"+
		"\u000f\u0000\u0000\u01be\u01bf\u0003 \u0010\u0000\u01bf-\u0001\u0000\u0000"+
		"\u0000\u01c0\u01c1\u00051\u0000\u0000\u01c1\u01c2\u0005\u0010\u0000\u0000"+
		"\u01c2/\u0001\u0000\u0000\u0000\u01c3\u01cf\u0005L\u0000\u0000\u01c4\u01cf"+
		"\u0005K\u0000\u0000\u01c5\u01cf\u00032\u0019\u0000\u01c6\u01cf\u00052"+
		"\u0000\u0000\u01c7\u01cf\u00053\u0000\u0000\u01c8\u01cf\u00054\u0000\u0000"+
		"\u01c9\u01cf\u00055\u0000\u0000\u01ca\u01cf\u0005\u0006\u0000\u0000\u01cb"+
		"\u01cf\u00034\u001a\u0000\u01cc\u01cf\u0003:\u001d\u0000\u01cd\u01cf\u0003"+
		">\u001f\u0000\u01ce\u01c3\u0001\u0000\u0000\u0000\u01ce\u01c4\u0001\u0000"+
		"\u0000\u0000\u01ce\u01c5\u0001\u0000\u0000\u0000\u01ce\u01c6\u0001\u0000"+
		"\u0000\u0000\u01ce\u01c7\u0001\u0000\u0000\u0000\u01ce\u01c8\u0001\u0000"+
		"\u0000\u0000\u01ce\u01c9\u0001\u0000\u0000\u0000\u01ce\u01ca\u0001\u0000"+
		"\u0000\u0000\u01ce\u01cb\u0001\u0000\u0000\u0000\u01ce\u01cc\u0001\u0000"+
		"\u0000\u0000\u01ce\u01cd\u0001\u0000\u0000\u0000\u01cf1\u0001\u0000\u0000"+
		"\u0000\u01d0\u01d2\u0005M\u0000\u0000\u01d1\u01d0\u0001\u0000\u0000\u0000"+
		"\u01d2\u01d3\u0001\u0000\u0000\u0000\u01d3\u01d1\u0001\u0000\u0000\u0000"+
		"\u01d3\u01d4\u0001\u0000\u0000\u0000\u01d43\u0001\u0000\u0000\u0000\u01d5"+
		"\u01d6\u00036\u001b\u0000\u01d6\u01df\u00056\u0000\u0000\u01d7\u01dc\u0003"+
		"8\u001c\u0000\u01d8\u01d9\u0005\u0015\u0000\u0000\u01d9\u01db\u00038\u001c"+
		"\u0000\u01da\u01d8\u0001\u0000\u0000\u0000\u01db\u01de\u0001\u0000\u0000"+
		"\u0000\u01dc\u01da\u0001\u0000\u0000\u0000\u01dc\u01dd\u0001\u0000\u0000"+
		"\u0000\u01dd\u01e0\u0001\u0000\u0000\u0000\u01de\u01dc\u0001\u0000\u0000"+
		"\u0000\u01df\u01d7\u0001\u0000\u0000\u0000\u01df\u01e0\u0001\u0000\u0000"+
		"\u0000\u01e0\u01e1\u0001\u0000\u0000\u0000\u01e1\u01e2\u00057\u0000\u0000"+
		"\u01e25\u0001\u0000\u0000\u0000\u01e3\u01e4\u0007\u0007\u0000\u0000\u01e4"+
		"7\u0001\u0000\u0000\u0000\u01e5\u01e8\u0003 \u0010\u0000\u01e6\u01e7\u0005"+
		"=\u0000\u0000\u01e7\u01e9\u0003 \u0010\u0000\u01e8\u01e6\u0001\u0000\u0000"+
		"\u0000\u01e8\u01e9\u0001\u0000\u0000\u0000\u01e99\u0001\u0000\u0000\u0000"+
		"\u01ea\u01eb\u0005>\u0000\u0000\u01eb\u01ec\u00056\u0000\u0000\u01ec\u01f1"+
		"\u0003<\u001e\u0000\u01ed\u01ee\u0005\u0015\u0000\u0000\u01ee\u01f0\u0003"+
		"<\u001e\u0000\u01ef\u01ed\u0001\u0000\u0000\u0000\u01f0\u01f3\u0001\u0000"+
		"\u0000\u0000\u01f1\u01ef\u0001\u0000\u0000\u0000\u01f1\u01f2\u0001\u0000"+
		"\u0000\u0000\u01f2\u01f4\u0001\u0000\u0000\u0000\u01f3\u01f1\u0001\u0000"+
		"\u0000\u0000\u01f4\u01f5\u00057\u0000\u0000\u01f5;\u0001\u0000\u0000\u0000"+
		"\u01f6\u01f9\u0003P(\u0000\u01f7\u01f8\u0005\u0004\u0000\u0000\u01f8\u01fa"+
		"\u0003B!\u0000\u01f9\u01f7\u0001\u0000\u0000\u0000\u01f9\u01fa\u0001\u0000"+
		"\u0000\u0000\u01fa\u01fb\u0001\u0000\u0000\u0000\u01fb\u01fc\u0005\u000f"+
		"\u0000\u0000\u01fc\u01fd\u0003 \u0010\u0000\u01fd=\u0001\u0000\u0000\u0000"+
		"\u01fe\u01ff\u0005?\u0000\u0000\u01ff\u0208\u00056\u0000\u0000\u0200\u0205"+
		"\u0003@ \u0000\u0201\u0202\u0005\u0015\u0000\u0000\u0202\u0204\u0003@"+
		" \u0000\u0203\u0201\u0001\u0000\u0000\u0000\u0204\u0207\u0001\u0000\u0000"+
		"\u0000\u0205\u0203\u0001\u0000\u0000\u0000\u0205\u0206\u0001\u0000\u0000"+
		"\u0000\u0206\u0209\u0001\u0000\u0000\u0000\u0207\u0205\u0001\u0000\u0000"+
		"\u0000\u0208\u0200\u0001\u0000\u0000\u0000\u0208\u0209\u0001\u0000\u0000"+
		"\u0000\u0209\u020a\u0001\u0000\u0000\u0000\u020a\u020b\u00057\u0000\u0000"+
		"\u020b?\u0001\u0000\u0000\u0000\u020c\u020d\u0003 \u0010\u0000\u020d\u020e"+
		"\u0007\b\u0000\u0000\u020e\u020f\u0003 \u0010\u0000\u020fA\u0001\u0000"+
		"\u0000\u0000\u0210\u0216\u0003D\"\u0000\u0211\u0216\u0003F#\u0000\u0212"+
		"\u0216\u0003H$\u0000\u0213\u0216\u0003J%\u0000\u0214\u0216\u0003N\'\u0000"+
		"\u0215\u0210\u0001\u0000\u0000\u0000\u0215\u0211\u0001\u0000\u0000\u0000"+
		"\u0215\u0212\u0001\u0000\u0000\u0000\u0215\u0213\u0001\u0000\u0000\u0000"+
		"\u0215\u0214\u0001\u0000\u0000\u0000\u0216C\u0001\u0000\u0000\u0000\u0217"+
		"\u0218\u0007\t\u0000\u0000\u0218E\u0001\u0000\u0000\u0000\u0219\u021a"+
		"\u00036\u001b\u0000\u021a\u021b\u0005\u000b\u0000\u0000\u021b\u021c\u0003"+
		"B!\u0000\u021c\u021d\u0005\f\u0000\u0000\u021dG\u0001\u0000\u0000\u0000"+
		"\u021e\u021f\u0005?\u0000\u0000\u021f\u0220\u0005\u000b\u0000\u0000\u0220"+
		"\u0221\u0003B!\u0000\u0221\u0222\u0005\u0015\u0000\u0000\u0222\u0223\u0003"+
		"B!\u0000\u0223\u0224\u0005\f\u0000\u0000\u0224I\u0001\u0000\u0000\u0000"+
		"\u0225\u0226\u0005>\u0000\u0000\u0226\u0227\u0005\u000b\u0000\u0000\u0227"+
		"\u022c\u0003L&\u0000\u0228\u0229\u0005\u0015\u0000\u0000\u0229\u022b\u0003"+
		"L&\u0000\u022a\u0228\u0001\u0000\u0000\u0000\u022b\u022e\u0001\u0000\u0000"+
		"\u0000\u022c\u022a\u0001\u0000\u0000\u0000\u022c\u022d\u0001\u0000\u0000"+
		"\u0000\u022d\u022f\u0001\u0000\u0000\u0000\u022e\u022c\u0001\u0000\u0000"+
		"\u0000\u022f\u0230\u0005\f\u0000\u0000\u0230K\u0001\u0000\u0000\u0000"+
		"\u0231\u0232\u0003P(\u0000\u0232\u0233\u0005\u0004\u0000\u0000\u0233\u0234"+
		"\u0003B!\u0000\u0234M\u0001\u0000\u0000\u0000\u0235\u023a\u0003P(\u0000"+
		"\u0236\u0237\u0005\u0005\u0000\u0000\u0237\u0239\u0003P(\u0000\u0238\u0236"+
		"\u0001\u0000\u0000\u0000\u0239\u023c\u0001\u0000\u0000\u0000\u023a\u0238"+
		"\u0001\u0000\u0000\u0000\u023a\u023b\u0001\u0000\u0000\u0000\u023bO\u0001"+
		"\u0000\u0000\u0000\u023c\u023a\u0001\u0000\u0000\u0000\u023d\u023e\u0007"+
		"\n\u0000\u0000\u023eQ\u0001\u0000\u0000\u0000@[`binu}\u0083\u0089\u008d"+
		"\u0095\u0097\u009d\u00a1\u00ad\u00b5\u00bb\u00c0\u00c5\u00ca\u00ce\u00d4"+
		"\u00da\u00de\u00e9\u00ed\u00f3\u00f7\u00fe\u010b\u012b\u012d\u0140\u014d"+
		"\u0157\u015b\u0162\u0167\u016c\u0170\u0175\u0179\u017b\u0189\u018f\u019a"+
		"\u019e\u01a3\u01a9\u01ad\u01b5\u01bb\u01ce\u01d3\u01dc\u01df\u01e8\u01f1"+
		"\u01f9\u0205\u0208\u0215\u022c\u023a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}