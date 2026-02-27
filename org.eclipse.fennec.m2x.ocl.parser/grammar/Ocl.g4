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
 * ANTLR4 combined lexer+parser grammar for OCL v2.5.
 * Covers Essential OCL expressions (OCL v2.4 Section 9.3) and
 * Complete OCL document structure (OCL v2.4 Section 12).
 * Includes v2.5 additions: safe navigation (?. / ?->), MapType, MapLiteralExp.
 *
 * Base grammar for import by QVT-O, QVT-R, and MOFM2T grammars.
 */
grammar Ocl;

// Package is set via ANTLR4 -package option; no @header needed.

// ==================== Entry Points ====================

/** Entry point for a single OCL expression. */
expressionEntry
    : expression EOF
    ;

/** Entry point for a Complete OCL document. */
completeOclDocumentEntry
    : completeOclDocument EOF
    ;

// ==================== Complete OCL Document ====================

completeOclDocument
    : importDeclaration* (packageDeclaration | contextDeclaration)*
    ;

importDeclaration
    : ('import' | 'include' | 'library') (identifier ':')? pathName ('::' '*')?
    ;

packageDeclaration
    : 'package' pathName contextDeclaration* 'endpackage'
    ;

contextDeclaration
    : classifierContextDeclaration
    | operationContextDeclaration
    | propertyContextDeclaration
    ;

classifierContextDeclaration
    : 'context' (identifier ':')? pathName (classifierContextBody)+
    ;

classifierContextBody
    : invariantConstraint
    | definitionConstraint
    ;

invariantConstraint
    : 'inv' (identifier ('(' expression ')')?)? ':' expression
    ;

definitionConstraint
    : 'static'? 'def' identifier? ':'
      ( identifier ':' typeExpression '=' expression                                     // attribute def
      | identifier '(' parameterList? ')' ':' typeExpression '=' expression              // operation def
      )
    ;

operationContextDeclaration
    : 'context' (pathName '::')? identifier '(' parameterList? ')' (':' typeExpression)?
      operationContextBody+
    ;

operationContextBody
    : 'pre'  (identifier)? ':' expression                                                # PreCondition
    | 'post' (identifier)? ':' expression                                                # PostCondition
    | 'body' (identifier)? ':' expression                                                # BodyExpression
    ;

propertyContextDeclaration
    : 'context' pathName '::' identifier ':' typeExpression
      propertyContextBody+
    ;

propertyContextBody
    : 'init'   (identifier)? ':' expression                                              # InitExpression
    | 'derive' (identifier)? ':' expression                                              # DeriveExpression
    ;

parameterList
    : parameter (',' parameter)*
    ;

parameter
    : identifier ':' typeExpression
    ;

// ==================== Expressions (Operator Precedence via ANTLR4 left-recursion) ====================

expression
    : expression ('.' | '?.') propertyOrCallSuffix                                       # NavigationExp
    | expression ('->' | '?->') iteratorOrOperationCall                                  # ArrowExp
    | 'not' expression                                                                   # NotExp
    | '-' expression                                                                     # UnaryMinusExp
    | expression op=('*' | '/') expression                                               # MultExp
    | expression op=('+' | '-') expression                                               # AddExp
    | expression op=('<' | '>' | '<=' | '>=') expression                                 # CompareExp
    | expression op=('=' | '<>') expression                                              # EqualityExp
    | expression 'and' expression                                                        # AndExp
    | expression 'or' expression                                                         # OrExp
    | expression 'xor' expression                                                        # XorExp
    | expression 'implies' expression                                                    # ImpliesExp
    | primaryExpression                                                                  # PrimaryExp
    ;

primaryExpression
    : '(' expression ')'                                                                 # ParenExp
    | 'self'                                                                             # SelfExp
    | 'if' condition=expression 'then' thenExp=expression
      ('elseif' elseIfCondition+=expression 'then' elseIfExp+=expression)*
      'else' elseExp=expression 'endif'                                                  # IfExp
    | 'let' letBinding (',' letBinding)* 'in' expression                                 # LetExp
    | literalExpression                                                                  # LiteralExp
    | pathName '(' argumentList? ')' isMarkedPre?                                        # OperationCallExp
    | primitiveType                                                                      # PrimitiveTypeExp
    | collectionType                                                                     # CollectionTypeExp
    | mapType                                                                            # MapTypeExp
    | tupleType                                                                          # TupleTypeExp
    | pathName                                                                           # PathNameExp
    ;

propertyOrCallSuffix
    : (pathName '::')? identifier '(' argumentList? ')' isMarkedPre?                     # DotCallSuffix
    | (pathName '::')? identifier isMarkedPre?                                           # PropertySuffix
    ;

iteratorOrOperationCall
    : identifier '(' iteratorVariables '|' expression ')'                                # IteratorCall
    | identifier '(' identifier (':' iterType=typeExpression)?
      ';' identifier (':' accType=typeExpression)? '=' expression '|' expression ')'     # IterateCall
    | identifier '(' argumentList? ')'                                                   # CollectionOperationCall
    ;

iteratorVariables
    : identifier (':' typeExpression)? (',' identifier (':' typeExpression)?)*
    ;

argumentList
    : expression (',' expression)*
    ;

letBinding
    : identifier (':' typeExpression)? '=' expression
    ;

isMarkedPre
    : '@' 'pre'
    ;

// ==================== Literals ====================

literalExpression
    : INTEGER_LITERAL                                                                    # IntegerLiteral
    | REAL_LITERAL                                                                       # RealLiteral
    | stringLiteral_                                                                     # StringLit
    | 'true'                                                                             # TrueLiteral
    | 'false'                                                                            # FalseLiteral
    | 'null'                                                                             # NullLiteral
    | 'invalid'                                                                          # InvalidLiteral
    | '*'                                                                                # UnlimitedNaturalLiteral
    | collectionLiteral                                                                  # CollectionLit
    | tupleLiteral                                                                       # TupleLit
    | mapLiteral                                                                         # MapLit
    ;

stringLiteral_
    : STRING_LITERAL+
    ;

collectionLiteral
    : collectionKind '{' (collectionLiteralPart (',' collectionLiteralPart)*)? '}'
    ;

collectionKind
    : 'Set'
    | 'OrderedSet'
    | 'Bag'
    | 'Sequence'
    | 'Collection'
    ;

collectionLiteralPart
    : expression ('..' expression)?
    ;

tupleLiteral
    : 'Tuple' '{' tupleLiteralPart (',' tupleLiteralPart)* '}'
    ;

tupleLiteralPart
    : identifier (':' typeExpression)? '=' expression
    ;

mapLiteral
    : 'Map' '{' (mapLiteralPart (',' mapLiteralPart)*)? '}'
    ;

mapLiteralPart
    : expression ('with' | '<-') expression
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
    : 'Boolean'
    | 'Integer'
    | 'Real'
    | 'String'
    | 'UnlimitedNatural'
    | 'OclAny'
    | 'OclVoid'
    | 'OclInvalid'
    | 'OclMessage'
    ;

collectionType
    : collectionKind '(' typeExpression ')'
    ;

mapType
    : 'Map' '(' typeExpression ',' typeExpression ')'
    ;

tupleType
    : 'Tuple' '(' tupleTypePart (',' tupleTypePart)* ')'
    ;

tupleTypePart
    : identifier ':' typeExpression
    ;

// ==================== Path Names ====================

pathName
    : identifier ('::' identifier)*
    ;

// ==================== Identifier (supports escaped identifiers §9.3.4) ====================

identifier
    : IDENTIFIER
    | ESCAPED_IDENTIFIER
    ;

// ==================== Lexer Rules ====================

// --- Keywords (listed explicitly so they are not matched as IDENTIFIER) ---

// Note: ANTLR4 matches keywords before IDENTIFIER because string literals
// in parser rules take priority. No explicit keyword tokens needed.

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

// G-01: Escaped identifiers _'keyword' (§9.3.4) — must precede IDENTIFIER for priority
ESCAPED_IDENTIFIER
    : '_' '\'' ( '\'\'' | ~['] )* '\''
    ;

IDENTIFIER
    : ('_' | '$' | LETTER) (LETTER | [0-9] | '_' | '$')*
    ;

// G-02: Unicode identifier characters (§9.3.4)
fragment LETTER
    : [a-zA-Z]
    | [\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02FF]
    | [\u0370-\u037D\u037F-\u1FFF]
    | [\u200C-\u200D\u2070-\u218F\u2C00-\u2FEF]
    | [\u3001-\uD7FF\uF900-\uFDCF\uFDF0-\uFFFD]
    ;

// --- Whitespace and Comments ---

WS
    : [ \t\r\n]+ -> skip
    ;

LINE_COMMENT
    : '--' ~[\r\n]* -> skip
    ;

// G-09: Nested block comments (§9.3.49)
BLOCK_COMMENT
    : '/*' ( BLOCK_COMMENT | . )*? '*/' -> skip
    ;
