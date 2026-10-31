/*
 * ******************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 * ******************************************************************
 *
 * ANTLR4 parser grammar for MOFM2T v1.0 (text-explicit mode).
 * Uses tokens from M2tLexer.g4.
 *
 * Implements the concrete syntax from MOFM2T §8.2.
 * OCL expressions are parsed inline using the same operator-precedence
 * rules as OCL v2.5 (replicated here since we cannot import a combined grammar).
 */
parser grammar M2tParser;

options { tokenVocab = M2tLexer; }

// ==================== Entry Point ====================

module
    : TEXT? moduleDecl (TEXT | LINE_COMMENT_BLOCK | importDecl)* (TEXT | LINE_COMMENT_BLOCK | moduleElement)* EOF
    ;

// ==================== Module Declaration ====================

// [module name(metamodel1, metamodel2) extends base /]
moduleDecl
    : BLOCK_OPEN KW_MODULE pathName LPAREN metamodelList RPAREN extendsDecl? SLASH_CLOSE
    ;

metamodelList
    : pathName (COMMA pathName)*
    ;

extendsDecl
    : KW_EXTENDS pathName (COMMA pathName)*
    ;

// ==================== Import ====================

// [import path::to::module /]
importDecl
    : BLOCK_OPEN KW_IMPORT pathName SLASH_CLOSE
    ;

// ==================== Module Elements ====================

moduleElement
    : templateDef
    | queryDef
    | macroDef
    ;

// ==================== Comment ====================

// [comment] ... [/comment] and [comment()] ... [/comment] are consumed and dropped by the lexer
// (COMMENT_BLOCK), so the body is literal text that never reaches the parser. The single-line
// [comment … /] is a token of its own (LINE_COMMENT_BLOCK): a '[comment @main/]' before a
// template marks it as the entry point (#372). KW_COMMENT remains a keyword for `identifier`.

// ==================== Template ====================

// [template public name(p1 : Type1, p2 : Type2) overrides base ? (guard) { init }]
//   ...body...
// [/template]
templateDef
    : BLOCK_OPEN KW_TEMPLATE signature CLOSE
      body
      BLOCK_OPEN SLASH KW_TEMPLATE CLOSE
    ;

signature
    : visibility? pathName LPAREN argList? RPAREN overridesDecl? guard? postDecl? initSection?
    ;

visibility
    : KW_PUBLIC
    | KW_PRIVATE
    | KW_PROTECTED
    ;

overridesDecl
    : KW_OVERRIDES pathName (COMMA pathName)*
    ;

guard
    : KW_GUARD LPAREN expression RPAREN
    ;

postDecl
    : KW_POST LPAREN expression RPAREN
    ;

initSection
    : LBRACE (variableDeclaration SEMICOLON)+ RBRACE
    ;

variableDeclaration
    : identifier COLON typeExpression (EQUALS expression)?
    ;

// ==================== Query ====================

// [query public name(params) : ReturnType = oclExpr /]
queryDef
    : BLOCK_OPEN KW_QUERY visibility? pathName LPAREN argList? RPAREN
      COLON typeExpression EQUALS expression SLASH_CLOSE
    ;

// ==================== Macro ====================

// [macro name(params)]...body...[/macro]
macroDef
    : BLOCK_OPEN KW_MACRO pathName LPAREN argList? RPAREN CLOSE
      body
      BLOCK_OPEN SLASH KW_MACRO CLOSE
    ;

// ==================== Arguments ====================

argList
    : argDecl (COMMA argDecl)*
    ;

argDecl
    : identifier COLON typeExpression
    ;

// ==================== Template Body ====================

body
    : bodyElement*
    ;

bodyElement
    : textBlock
    | LINE_COMMENT_BLOCK
    | inlineExpression
    | forBlock
    | ifBlock
    | letBlock
    | fileBlock
    | traceBlock
    | protectedBlock
    | macroBlockInvocation
    | templateInvocation
    ;

// Literal text (outside of [...] blocks)
textBlock
    : TEXT+
    ;

// [expression/]  — inline OCL expression producing text output
inlineExpression
    : BLOCK_OPEN expression SLASH_CLOSE
    ;

// ==================== For Block ====================

// [for (var : Type | collection) before(...) separator(...) after(...) ? (guard) { init }]
//   ...body...
// [/for]
forBlock
    : BLOCK_OPEN KW_FOR LPAREN argDecl PIPE expression RPAREN
      beforeClause? separatorClause? afterClause? guard? initSection?
      CLOSE
      body
      BLOCK_OPEN SLASH KW_FOR CLOSE
    ;

beforeClause
    : KW_BEFORE LPAREN expression RPAREN
    ;

separatorClause
    : KW_SEPARATOR LPAREN expression RPAREN
    ;

afterClause
    : KW_AFTER LPAREN expression RPAREN
    ;

// ==================== If Block ====================

// [if (condition)]...body...[elseif (cond)]...body...[else]...body...[/if]
ifBlock
    : BLOCK_OPEN KW_IF LPAREN expression RPAREN CLOSE
      body
      elseIfBlock*
      elseBlock?
      BLOCK_OPEN SLASH KW_IF CLOSE
    ;

// §8.2: the end tags of elseif, else and elselet are optional here — '[/elseif]' and '[/else]'
// are optional in the spec, and Acceleo omits '[/elselet]', which the spec requires (#375)
elseIfBlock
    : BLOCK_OPEN KW_ELSEIF LPAREN expression RPAREN CLOSE
      body
      (BLOCK_OPEN SLASH KW_ELSEIF CLOSE)?
    ;

elseBlock
    : BLOCK_OPEN KW_ELSE CLOSE
      body
      (BLOCK_OPEN SLASH KW_ELSE CLOSE)?
    ;

// ==================== Let Block ====================

// [let var : Type = expr]...body...[elselet var2 : Type2 = expr2]...body...[/let]
letBlock
    : BLOCK_OPEN KW_LET variableDeclaration CLOSE
      body
      elseLetBlock*
      elseBlock?
      BLOCK_OPEN SLASH KW_LET CLOSE
    ;

elseLetBlock
    : BLOCK_OPEN KW_ELSELET variableDeclaration CLOSE
      body
      (BLOCK_OPEN SLASH KW_ELSELET CLOSE)?
    ;

// ==================== File Block ====================

// [file (urlExpr, appendExpr?, idExpr?)]...body...[/file]
fileBlock
    : BLOCK_OPEN KW_FILE LPAREN expression (COMMA expression (COMMA expression)?)? RPAREN CLOSE
      body
      BLOCK_OPEN SLASH KW_FILE CLOSE
    ;

// ==================== Trace Block ====================

// [trace (traceExpr)]...body...[/trace]
traceBlock
    : BLOCK_OPEN KW_TRACE LPAREN expression RPAREN CLOSE
      body
      BLOCK_OPEN SLASH KW_TRACE CLOSE
    ;

// ==================== Protected Block ====================

// [protected (idExpr)]...body...[/protected]
protectedBlock
    : BLOCK_OPEN KW_PROTECTED LPAREN expression RPAREN CLOSE
      body
      BLOCK_OPEN SLASH KW_PROTECTED CLOSE
    ;

// ==================== Template Invocation (inside body) ====================

// [templateName(args) before(...) separator(...) after(...)/]
// or [super/]
templateInvocation
    : BLOCK_OPEN pathName LPAREN argumentList? RPAREN
      beforeClause? separatorClause? afterClause? SLASH_CLOSE
    | BLOCK_OPEN KW_SUPER SLASH_CLOSE
    ;

// §7.4, §8.1.20: [name(args)]…body…[/name] — a macro invocation whose body is the macro's last,
// Body-typed argument (#371)
macroBlockInvocation
    : BLOCK_OPEN pathName LPAREN argumentList? RPAREN CLOSE
      body
      BLOCK_OPEN SLASH endName=pathName CLOSE
    ;

// ==================== OCL Expressions (inline, same precedence as OCL v2.5) ====================

expression
    : expression (DOT | SAFENAV) propertyOrCallSuffix                       # NavigationExp
    | expression (ARROW | SAFEARROW) iteratorOrOperationCall                # ArrowExp
    | KW_NOT expression                                                     # NotExp
    | MINUS expression                                                      # UnaryMinusExp
    | expression op=(STAR | SLASH) expression                               # MultExp
    | expression op=(PLUS | MINUS) expression                               # AddExp
    | expression op=(LT | GT | LE | GE) expression                         # CompareExp
    | expression op=(EQUALS | NE) expression                                # EqualityExp
    | expression KW_AND expression                                          # AndExp
    | expression KW_OR expression                                           # OrExp
    | expression KW_XOR expression                                          # XorExp
    | expression KW_IMPLIES expression                                      # ImpliesExp
    // §8.2 <ifExp>: a ? b : c, the lowest precedence; a guard's '?' always follows a ')' of a
    // signature or a for clause, never an expression (#375)
    | <assoc=right> condition=expression KW_GUARD thenExp=expression COLON elseExp=expression # TernaryExp
    | primaryExpression                                                     # PrimaryExp
    ;

primaryExpression
    : LPAREN expression RPAREN                                              # ParenExp
    | KW_SELF                                                               # SelfExp
    | KW_IF condition=expression KW_THEN thenExp=expression
      (KW_ELSEIF elseIfCondition+=expression KW_THEN elseIfExp+=expression)*
      KW_ELSE elseExp=expression KW_ENDIF                                   # OclIfExp
    | KW_LET letBinding (COMMA letBinding)* KW_IN expression               # OclLetExp
    | literalExpression                                                     # LiteralExp
    | pathName LPAREN argumentList? RPAREN                                  # OperationCallExp
    | primitiveType                                                         # PrimitiveTypeExp
    | collectionType                                                        # CollectionTypeExp
    | mapType                                                               # MapTypeExp
    | tupleType                                                             # TupleTypeExp
    | pathName                                                              # PathNameExp
    ;

propertyOrCallSuffix
    : (pathName COLONCOLON)? identifier LPAREN argumentList? RPAREN         # DotCallSuffix
    | (pathName COLONCOLON)? identifier                                     # PropertySuffix
    ;

iteratorOrOperationCall
    : identifier LPAREN iteratorVariables PIPE expression RPAREN            # IteratorCall
    // §9.3.26: the iterator variable may be omitted — the body then has an implicit one (#320, #375)
    | identifier LPAREN (iterName=identifier (COLON iterType=typeExpression)? SEMICOLON)?
      accName=identifier (COLON accType=typeExpression)? EQUALS accInit=expression
      PIPE iterBody=expression RPAREN                                       # IterateCall
    | identifier LPAREN argumentList? RPAREN                                # CollectionOperationCall
    ;

iteratorVariables
    : identifier (COLON typeExpression)? (COMMA identifier (COLON typeExpression)?)*
    ;

argumentList
    : expression (COMMA expression)*
    ;

letBinding
    : identifier (COLON typeExpression)? EQUALS expression
    ;

// ==================== Literals ====================

literalExpression
    : INTEGER_LITERAL                                                       # IntegerLiteral
    | REAL_LITERAL                                                          # RealLiteral
    | stringLiteral                                                         # StringLit
    | KW_TRUE                                                               # TrueLiteral
    | KW_FALSE                                                              # FalseLiteral
    | KW_NULL                                                               # NullLiteral
    | KW_INVALID                                                            # InvalidLiteral
    | STAR                                                                  # UnlimitedNaturalLiteral
    | collectionLiteral                                                     # CollectionLit
    | tupleLiteral                                                          # TupleLit
    | mapLiteral                                                            # MapLit
    ;

stringLiteral
    : STRING_LITERAL+
    ;

collectionLiteral
    : collectionKind LBRACE (collectionLiteralPart (COMMA collectionLiteralPart)*)? RBRACE
    ;

collectionKind
    : KW_SET
    | KW_ORDEREDSET
    | KW_BAG
    | KW_SEQUENCE
    | KW_COLLECTION
    ;

collectionLiteralPart
    : expression (DOTDOT expression)?
    ;

tupleLiteral
    : KW_TUPLE LBRACE tupleLiteralPart (COMMA tupleLiteralPart)* RBRACE
    ;

tupleLiteralPart
    : identifier (COLON typeExpression)? EQUALS expression
    ;

mapLiteral
    : KW_MAP LBRACE (mapLiteralPart (COMMA mapLiteralPart)*)? RBRACE
    ;

mapLiteralPart
    : expression (KW_WITH | LARROW) expression
    ;

// ==================== Type Expressions ====================

typeExpression
    : primitiveType
    | collectionType
    | mapType
    | tupleType
    | pathName
    ;

primitiveType
    : KW_BOOLEAN
    | KW_INTEGER
    | KW_REAL
    | KW_STRING
    | KW_UNLIMITEDNATURAL
    | KW_OCLANY
    | KW_OCLVOID
    | KW_OCLINVALID
    | KW_OCLMESSAGE
    ;

collectionType
    : collectionKind LPAREN typeExpression RPAREN
    ;

mapType
    : KW_MAP LPAREN typeExpression COMMA typeExpression RPAREN
    ;

tupleType
    : KW_TUPLE LPAREN tupleTypePart (COMMA tupleTypePart)* RPAREN
    ;

tupleTypePart
    : identifier COLON typeExpression
    ;

// ==================== Path Names ====================

pathName
    : identifier (COLONCOLON (identifier | restrictedWord))*
    ;

/** §7.4.11: "A restricted word can only be used as a name when preceded by a '::'" (#325, #375). */
restrictedWord
    : KW_BAG | KW_BOOLEAN | KW_COLLECTION | KW_INTEGER | KW_OCLANY | KW_OCLINVALID | KW_OCLMESSAGE
    | KW_OCLVOID | KW_ORDEREDSET | KW_REAL | KW_SEQUENCE | KW_SET | KW_STRING | KW_TUPLE
    | KW_UNLIMITEDNATURAL
    ;

// ==================== Identifier ====================

// Keywords can appear as identifiers in pathNames (e.g. [import other::module/])
identifier
    : IDENTIFIER
    | ESCAPED_IDENTIFIER
    | KW_MODULE | KW_IMPORT | KW_EXTENDS | KW_TEMPLATE | KW_QUERY | KW_MACRO
    | KW_FOR | KW_IF | KW_ELSEIF | KW_ELSE | KW_LET | KW_ELSELET
    | KW_FILE | KW_TRACE | KW_PROTECTED | KW_COMMENT
    | KW_PUBLIC | KW_PRIVATE | KW_OVERRIDES
    | KW_BEFORE | KW_SEPARATOR | KW_AFTER | KW_POST | KW_SUPER | KW_STDOUT
    ;
