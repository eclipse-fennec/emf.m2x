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
 * ANTLR4 standalone lexer for MOFM2T v1.0 (text-explicit mode).
 *
 * Two lexer modes:
 *   DEFAULT_MODE  — text output (everything is literal text until '[')
 *   CODE_MODE     — template code (keywords, OCL expressions, delimiters)
 *
 * The '[' character switches from text to code mode.
 * ']' and '/]' switch back from code to text mode.
 *
 * OCL expressions inside code blocks are collected as token sequences
 * and delegated to the OCL parser in the M2tParserSupport layer.
 */
lexer grammar M2tLexer;

// ==================== DEFAULT_MODE (Text) ====================

// The '[' delimiter opens a code block — switch to CODE_MODE
BLOCK_OPEN
    : '[' -> pushMode(CODE_MODE)
    ;

// Literal text: everything that is not '['.
// Matches one or more characters greedily.
TEXT
    : ~'['+
    ;

// ==================== CODE_MODE ====================

mode CODE_MODE;

// Self-closing block end: '/]'  (e.g. [module.../], [expr/], [import.../])
SLASH_CLOSE
    : '/]' -> popMode
    ;

// Block-closing ']' — ends block header or standalone close tag
CLOSE
    : ']' -> popMode
    ;

// Slash for close-tags like [/template], [/for], etc.
SLASH
    : '/'
    ;

// --- Block Keywords (MOFM2T §8.2) ---

KW_MODULE     : 'module' ;
KW_IMPORT     : 'import' ;
KW_EXTENDS    : 'extends' ;
KW_TEMPLATE   : 'template' ;
KW_QUERY      : 'query' ;
KW_MACRO      : 'macro' ;
KW_FOR        : 'for' ;
KW_IF         : 'if' ;
KW_ELSEIF     : 'elseif' ;
KW_ELSE       : 'else' ;
KW_LET        : 'let' ;
KW_ELSELET    : 'elselet' ;
KW_FILE       : 'file' ;
KW_TRACE      : 'trace' ;
KW_PROTECTED  : 'protected' ;
KW_COMMENT    : 'comment' ;
KW_OVERRIDES  : 'overrides' ;

// --- Visibility ---

KW_PUBLIC     : 'public' ;
KW_PRIVATE    : 'private' ;

// --- Template Clauses ---

KW_BEFORE     : 'before' ;
KW_SEPARATOR  : 'separator' ;
KW_AFTER      : 'after' ;
KW_GUARD      : '?' ;
KW_POST       : 'post' ;
KW_SUPER      : 'super' ;
KW_STDOUT     : 'stdout' ;

// --- Punctuation ---

LPAREN        : '(' ;
RPAREN        : ')' ;
LBRACE        : '{' ;
RBRACE        : '}' ;
COMMA         : ',' ;
SEMICOLON     : ';' ;
COLONCOLON    : '::' ;
COLON         : ':' ;
PIPE          : '|' ;
EQUALS        : '=' ;
DOT           : '.' ;
SAFENAV       : '?.' ;
ARROW         : '->' ;
SAFEARROW     : '?->' ;
DOTDOT        : '..' ;

// --- OCL Operators ---

STAR          : '*' ;
PLUS          : '+' ;
MINUS         : '-' ;
// Note: '/' is covered by SLASH token; '=' is covered by EQUALS token.
// Parser rules use SLASH for division and EQUALS for equality.
LT            : '<' ;
GT            : '>' ;
LE            : '<=' ;
GE            : '>=' ;
NE            : '<>' ;

// --- OCL Keywords (needed for embedded expressions) ---

KW_NOT        : 'not' ;
KW_AND        : 'and' ;
KW_OR         : 'or' ;
KW_XOR        : 'xor' ;
KW_IMPLIES    : 'implies' ;
KW_SELF       : 'self' ;
KW_TRUE       : 'true' ;
KW_FALSE      : 'false' ;
KW_NULL       : 'null' ;
KW_INVALID    : 'invalid' ;
KW_IN         : 'in' ;
KW_THEN       : 'then' ;
KW_ENDIF      : 'endif' ;

// OCL collection keywords
KW_SET        : 'Set' ;
KW_ORDEREDSET : 'OrderedSet' ;
KW_BAG        : 'Bag' ;
KW_SEQUENCE   : 'Sequence' ;
KW_COLLECTION : 'Collection' ;
KW_MAP        : 'Map' ;
KW_TUPLE      : 'Tuple' ;

// OCL primitive type keywords
KW_BOOLEAN    : 'Boolean' ;
KW_INTEGER    : 'Integer' ;
KW_REAL       : 'Real' ;
KW_STRING     : 'String' ;
KW_UNLIMITEDNATURAL : 'UnlimitedNatural' ;
KW_OCLANY     : 'OclAny' ;
KW_OCLVOID    : 'OclVoid' ;
KW_OCLINVALID : 'OclInvalid' ;
KW_OCLMESSAGE : 'OclMessage' ;

// OCL with / <- for map literals
KW_WITH       : 'with' ;
LARROW        : '<-' ;

// --- Literals ---

REAL_LITERAL
    : [0-9]+ '.' [0-9]+ ([eE] [+-]? [0-9]+)?
    | [0-9]+ [eE] [+-]? [0-9]+
    ;

INTEGER_LITERAL
    : [0-9]+
    ;

STRING_LITERAL
    : '\'' ( '\\\\' | '\\\'' | '\\"' | '\\n' | '\\t' | '\\r' | '\\f' | '\\b'
           | '\\x' [0-9a-fA-F] [0-9a-fA-F]
           | '\\u' [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F]
           | '\\' [0-3]? [0-7] [0-7]?
           | ~['\\] )* '\''
    ;

// --- Identifiers ---

ESCAPED_IDENTIFIER
    : '_' '\'' ( '\'\'' | ~['] )* '\''
    ;

IDENTIFIER
    : ('_' | '$' | LETTER) (LETTER | [0-9] | '_' | '$')*
    ;

fragment LETTER
    : [a-zA-Z]
    | [\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02FF]
    | [\u0370-\u037D\u037F-\u1FFF]
    | [\u200C-\u200D\u2070-\u218F\u2C00-\u2FEF]
    | [\u3001-\uD7FF\uF900-\uFDCF\uFDF0-\uFFFD]
    ;

// --- Whitespace (skipped in code mode) ---

CODE_WS
    : [ \t\r\n]+ -> skip
    ;

// --- OCL Comments (inside code blocks) ---

CODE_LINE_COMMENT
    : '--' ~[\r\n]* -> skip
    ;

CODE_BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;
