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
    : ('import' | 'include' | 'library') (IDENTIFIER ':')? pathName ('::' '*')?
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
    : 'context' pathName (classifierContextBody)+
    ;

classifierContextBody
    : invariantConstraint
    | definitionConstraint
    ;

invariantConstraint
    : 'inv' (IDENTIFIER ('(' expression ')')?)? ':' expression
    ;

definitionConstraint
    : 'def' IDENTIFIER? ':'
      ( IDENTIFIER ':' typeExpression '=' expression                                     // attribute def
      | IDENTIFIER '(' parameterList? ')' ':' typeExpression '=' expression              // operation def
      )
    ;

operationContextDeclaration
    : 'context' pathName '::' IDENTIFIER '(' parameterList? ')' ':' typeExpression
      operationContextBody+
    ;

operationContextBody
    : 'pre'  (IDENTIFIER)? ':' expression                                                # PreCondition
    | 'post' (IDENTIFIER)? ':' expression                                                # PostCondition
    | 'body' (IDENTIFIER)? ':' expression                                                # BodyExpression
    ;

propertyContextDeclaration
    : 'context' pathName '::' IDENTIFIER ':' typeExpression
      propertyContextBody+
    ;

propertyContextBody
    : 'init'   (IDENTIFIER)? ':' expression                                              # InitExpression
    | 'derive' (IDENTIFIER)? ':' expression                                              # DeriveExpression
    ;

parameterList
    : parameter (',' parameter)*
    ;

parameter
    : IDENTIFIER ':' typeExpression
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
    | pathName '(' argumentList? ')'                                                     # OperationCallExp
    | pathName                                                                           # PathNameExp
    ;

propertyOrCallSuffix
    : IDENTIFIER '(' argumentList? ')' isMarkedPre?                                      # DotCallSuffix
    | IDENTIFIER isMarkedPre?                                                            # PropertySuffix
    ;

iteratorOrOperationCall
    : IDENTIFIER '(' iteratorVariables '|' expression ')'                                # IteratorCall
    | IDENTIFIER '(' IDENTIFIER (':' iterType=typeExpression)?
      ';' IDENTIFIER (':' accType=typeExpression)? '=' expression '|' expression ')'     # IterateCall
    | IDENTIFIER '(' argumentList? ')'                                                   # CollectionOperationCall
    ;

iteratorVariables
    : IDENTIFIER (':' typeExpression)? (',' IDENTIFIER (':' typeExpression)?)*
    ;

argumentList
    : expression (',' expression)*
    ;

letBinding
    : IDENTIFIER (':' typeExpression)? '=' expression
    ;

isMarkedPre
    : '@' 'pre'
    ;

// ==================== Literals ====================

literalExpression
    : INTEGER_LITERAL                                                                    # IntegerLiteral
    | REAL_LITERAL                                                                       # RealLiteral
    | STRING_LITERAL                                                                     # StringLiteral
    | 'true'                                                                             # TrueLiteral
    | 'false'                                                                            # FalseLiteral
    | 'null'                                                                             # NullLiteral
    | 'invalid'                                                                          # InvalidLiteral
    | '*'                                                                                # UnlimitedNaturalLiteral
    | collectionLiteral                                                                  # CollectionLit
    | tupleLiteral                                                                       # TupleLit
    | mapLiteral                                                                         # MapLit
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
    : IDENTIFIER (':' typeExpression)? '=' expression
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
    : IDENTIFIER ':' typeExpression
    ;

// ==================== Path Names ====================

pathName
    : IDENTIFIER ('::' IDENTIFIER)*
    ;

// ==================== Lexer Rules ====================

// --- Keywords (listed explicitly so they are not matched as IDENTIFIER) ---

// Note: ANTLR4 matches keywords before IDENTIFIER because string literals
// in parser rules take priority. No explicit keyword tokens needed.

// --- Literals ---

REAL_LITERAL
    : [0-9]+ '.' [0-9]+
    ;

INTEGER_LITERAL
    : [0-9]+
    ;

STRING_LITERAL
    : '\'' ( '\\\\' | '\\\'' | ~['\\] )* '\''
    ;

// --- Identifiers ---

IDENTIFIER
    : ('_' | LETTER) (LETTER | [0-9] | '_')*
    ;

fragment LETTER
    : [a-zA-Z]
    ;

// --- Whitespace and Comments ---

WS
    : [ \t\r\n]+ -> skip
    ;

LINE_COMMENT
    : '--' ~[\r\n]* -> skip
    ;

BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;
