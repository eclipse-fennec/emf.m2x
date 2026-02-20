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
 * ANTLR4 combined lexer+parser grammar for QVT-Operational v1.3.
 * Imports the OCL v2.5 base grammar and adds imperative constructs,
 * transformation structure, and mapping operations.
 *
 * D26: Dict/List are parser-level aliases for Map/Sequence.
 * D28: Eclipse QVT-O feature scope.
 */
grammar QvtO;

import Ocl;

// ==================== Entry Point ====================

compilationUnitEntry
    : compilationUnit EOF
    ;

compilationUnit
    : unitElement*
    ;

unitElement
    : qvtoImportDecl
    | modeltypeDecl
    | transformationDef
    | libraryDef
    | moduleElement
    ;

// ==================== Import + Modeltype ====================

qvtoImportDecl
    : 'import' qualifiedName ';'
    ;

modeltypeDecl
    : 'modeltype' qvtoIdentifier (STRING_LITERAL)? 'uses' packageRefList modeltypeWhere? ';'
    ;

packageRefList
    : packageRef (',' packageRef)*
    ;

packageRef
    : pathName ('(' STRING_LITERAL ')')?
    | STRING_LITERAL
    ;

modeltypeWhere
    : 'where' '{' expression (',' expression)* '}'
    ;

// ==================== Transformation + Library ====================

transformationDef
    : qualifier* 'transformation' qualifiedName '(' modelParamList? ')' moduleUsage* '{' moduleElement* '}' ';'?
    ;

libraryDef
    : 'library' qualifiedName simpleSignature? moduleUsage* '{' moduleElement* '}' ';'?
    ;

modelParamList
    : modelParam (',' modelParam)*
    ;

modelParam
    : directionKind qvtoIdentifier ':' typeSpec
    ;

directionKind
    : 'in'
    | 'out'
    | 'inout'
    ;

typeSpec
    : typeExpression ('@' qvtoIdentifier)?
    ;

moduleUsage
    : moduleUsageKind ('transformation' | 'library')? moduleRefList
    ;

moduleUsageKind
    : 'access'
    | 'extends'
    ;

moduleRefList
    : qualifiedName (',' qualifiedName)*
    ;

qualifier
    : 'abstract'
    | 'blackbox'
    | 'static'
    ;

// ==================== Module Elements ====================

moduleElement
    : mappingDef
    | helperDef
    | queryDef
    | constructorDef
    | entryDef
    | propertyDecl
    | intermediateClassDef
    | tagDecl ';'
    | typedefDecl ';'
    ;

// ==================== Mapping ====================

mappingDef
    : qualifier* 'mapping' directionKind? scopedName mappingSignature
      mappingExtension* whenClause? whereClause? mappingBody ';'?
    ;

mappingSignature
    : '(' inputParams=paramList? ')' (':' resultParams=paramList)?
    ;

paramList
    : param (',' param)*
    ;

param
    : qvtoIdentifier ':' typeExpression
    ;

mappingExtension
    : mappingExtensionKind scopedNameList
    ;

mappingExtensionKind
    : 'inherits'
    | 'merges'
    | 'disjuncts'
    ;

whenClause
    : 'when' block
    ;

whereClause
    : 'where' block
    ;

mappingBody
    : '{' initSection? statementList endSection? '}'
    ;

initSection
    : 'init' block
    ;

endSection
    : 'end' block
    ;

// ==================== Helper / Query / Constructor / Entry ====================

helperDef
    : qualifier* 'helper' scopedName simpleSignature (':' typeExpression)? (block | '=' expression ';' | ';')
    ;

queryDef
    : qualifier* 'query' scopedName simpleSignature ':' typeExpression (block | '=' expression ';' | ';')
    ;

constructorDef
    : 'constructor' scopedName simpleSignature block ';'?
    ;

entryDef
    : 'main' '(' ')' block ';'?
    | 'entry' simpleSignature? block ';'?
    ;

simpleSignature
    : '(' paramList? ')'
    ;

// ==================== Property + Intermediate Class + Tag + Typedef ====================

propertyDecl
    : 'intermediate' 'property' scopedName ':' typeExpression ('=' expression)? ';'
    | 'configuration' 'property' qvtoIdentifier ':' typeExpression ('=' expression)? ';'
    ;

intermediateClassDef
    : 'intermediate' 'class' qvtoIdentifier ('extends' typeList)? '{' classifierFeature* '}' ';'?
    ;

typeList
    : typeExpression (',' typeExpression)*
    ;

classifierFeature
    : qvtoIdentifier ':' typeExpression ('=' expression)? ';'
    ;

tagDecl
    : 'tag' STRING_LITERAL scopedName ('=' expression)?
    ;

typedefDecl
    : 'typedef' qvtoIdentifier '=' typeExpression ('with' expression)?
    ;

// ==================== Statements ====================

statementList
    : statement*
    ;

statement
    : expression assignOp expression ';'                                                 # AssignStatement
    | expression ';'                                                                     # ExpressionStatement
    ;

assignOp
    : ':='
    | '+='
    | '::='
    ;

// ==================== Block ====================

block
    : '{' statement* '}'
    ;

// ==================== Expression Override ====================

// Override primaryExpression to add imperative constructs.
// QVT-O alternatives MUST come BEFORE pathName/OperationCallExp because
// soft keywords (break, continue, return, resolve, log, assert, etc.)
// are also valid qvtoIdentifiers and would match pathName first.
// ANTLR4 resolves ambiguity by picking the first matching alternative.
primaryExpression
    : '(' expression ')'                                                                 # ParenExp
    | 'self'                                                                             # SelfExp
    | 'if' condition=expression 'then' thenExp=expression
      ('elseif' elseIfCondition+=expression 'then' elseIfExp+=expression)*
      'else' elseExp=expression 'endif'                                                  # IfExp
    | 'let' letBinding (',' letBinding)* 'in' expression                                 # LetExp
    | literalExpression                                                                  # LiteralExp
    // === QVT-O Imperative Expressions (before pathName to win ambiguity) ===
    | blockExp                                                                           # BlockPrimary
    | whileExp                                                                           # WhilePrimary
    | forExp                                                                             # ForPrimary
    | switchExp                                                                          # SwitchPrimary
    | computeExp                                                                         # ComputePrimary
    | objectExp                                                                          # ObjectPrimary
    | newExp                                                                             # NewPrimary
    | mappingCallExp                                                                     # MappingCallPrimary
    | resolveExp                                                                         # ResolvePrimary
    | resolveInExp                                                                       # ResolveInPrimary
    | tryExp                                                                             # TryPrimary
    | raiseExp                                                                           # RaisePrimary
    | assertExp                                                                          # AssertPrimary
    | logExp                                                                             # LogPrimary
    | returnExp                                                                          # ReturnPrimary
    | 'break'                                                                            # BreakPrimary
    | 'continue'                                                                         # ContinuePrimary
    | varDeclExp                                                                         # VarDeclPrimary
    // === OCL fallback alternatives (after QVT-O to avoid soft keyword ambiguity) ===
    | pathName '(' argumentList? ')'                                                     # OperationCallExp
    | primitiveType                                                                      # PrimitiveTypeExp
    | collectionType                                                                     # CollectionTypeExp
    | mapType                                                                            # MapTypeExp
    | tupleType                                                                          # TupleTypeExp
    | pathName                                                                           # PathNameExp
    ;

// ==================== Type Expression Override ====================

// Override to add List/Dict aliases (D26)
typeExpression
    : primitiveType
    | collectionType
    | mapType
    | tupleType
    | listType
    | dictType
    | pathName
    ;

listType
    : 'List' '(' typeExpression ')'
    ;

dictType
    : 'Dict' '(' typeExpression ',' typeExpression ')'
    ;

// ==================== Imperative Expressions ====================

blockExp
    : 'do' '{' statement* '}'
    ;

whileExp
    : 'while' '(' expression ')' block
    ;

forExp
    : forKind '(' forVarList ('|' expression)? ')' block
    ;

forKind
    : 'forEach'
    | 'forOne'
    ;

forVarList
    : iteratorVariables ';' typeExpression
    | iteratorVariables
    ;

switchExp
    : 'switch' ('(' expression ')')? '{' switchAlt* ('else' expression ';')? '}'
    ;

switchAlt
    : 'case' '(' expression ')' expression ';'
    ;

computeExp
    : 'compute' '(' varDeclarator ')' block
    ;

objectExp
    : 'object' (qvtoIdentifier ':')? typeExpression? block
    ;

newExp
    : 'new' pathName '(' argumentList? ')'
    ;

mappingCallExp
    : mappingCallKind scopedName '(' argumentList? ')'
    ;

mappingCallKind
    : 'map'
    | 'xmap'
    ;

resolveExp
    : 'late'? resolveKind '(' resolveArgs? ')'
    ;

resolveKind
    : 'resolve'
    | 'resolveone'
    | 'invresolve'
    | 'invresolveone'
    ;

resolveInExp
    : 'late'? resolveInKind '(' scopedName (',' resolveArgs)? ')'
    ;

resolveInKind
    : 'resolveIn'
    | 'resolveoneIn'
    | 'invresolveIn'
    | 'invresolveoneIn'
    ;

resolveArgs
    : resolveTarget ('|' expression)?
    ;

resolveTarget
    : qvtoIdentifier ':' typeExpression
    ;

tryExp
    : 'try' block catchClause+
    ;

catchClause
    : 'catch' ('(' exceptionTypeList ')')? block
    ;

exceptionTypeList
    : pathName (',' pathName)*
    ;

raiseExp
    : 'raise' (pathName ('(' expression? ')')? | STRING_LITERAL)
    ;

assertExp
    : 'assert' (qvtoIdentifier)? '(' expression ')' ('with' logCallExp)?
    ;

logCallExp
    : 'log' '(' argumentList? ')' ('when' expression)?
    ;

logExp
    : 'log' '(' argumentList? ')' ('when' expression)?
    ;

returnExp
    : 'return' expression?
    ;

varDeclExp
    : 'var' varDeclarator
    ;

varDeclarator
    : qvtoIdentifier (':' typeExpression)? (varInitOp expression)?
    ;

varInitOp
    : ':='
    | '='
    | '::='
    ;

// ==================== OCL Rule Overrides for Soft Keywords ====================

// Override pathName to accept QVT-O soft keywords as identifiers in expression contexts.
pathName
    : qvtoIdentifier ('::' qvtoIdentifier)*
    ;

// Override navigation suffixes: obj.result, obj.object(), obj.map target(), etc.
propertyOrCallSuffix
    : mappingCallKind scopedName '(' argumentList? ')'                                     # MappingCallSuffix
    | qvtoIdentifier '(' argumentList? ')' isMarkedPre?                                    # DotCallSuffix
    | qvtoIdentifier isMarkedPre?                                                          # PropertySuffix
    ;

// Override iterator/operation calls: coll->collect(result | ...), coll->iterate(...)
iteratorOrOperationCall
    : qvtoIdentifier '(' iteratorVariables '|' expression ')'                              # IteratorCall
    | qvtoIdentifier '(' qvtoIdentifier (':' iterType=typeExpression)?
      ';' qvtoIdentifier (':' accType=typeExpression)? '=' expression '|' expression ')'   # IterateCall
    | qvtoIdentifier '(' argumentList? ')'                                                 # CollectionOperationCall
    ;

// Override iterator variables to accept soft keywords as names
iteratorVariables
    : qvtoIdentifier (':' typeExpression)? (',' qvtoIdentifier (':' typeExpression)?)*
    ;

// Override let bindings to accept soft keywords as names
letBinding
    : qvtoIdentifier (':' typeExpression)? '=' expression
    ;

// Override tuple type parts to accept soft keywords as names
tupleTypePart
    : qvtoIdentifier ':' typeExpression
    ;

// Override tuple literal parts to accept soft keywords as names
tupleLiteralPart
    : qvtoIdentifier (':' typeExpression)? '=' expression
    ;

// ==================== Scoped + Qualified Names ====================

scopedName
    : qualifiedName '::' qvtoIdentifier
    | qvtoIdentifier
    ;

scopedNameList
    : scopedName (',' scopedName)*
    ;

qualifiedName
    : qvtoIdentifier ('.' qvtoIdentifier)*
    ;

// ==================== Soft Keywords ====================

// Accepts IDENTIFIER plus contextual keywords that are not reserved in all positions.
qvtoIdentifier
    : IDENTIFIER
    | 'access'
    | 'blackbox'
    | 'configuration'
    | 'constructor'
    | 'disjuncts'
    | 'end'
    | 'entry'
    | 'extends'
    | 'forEach'
    | 'forOne'
    | 'helper'
    | 'inherits'
    | 'init'
    | 'intermediate'
    | 'invresolve'
    | 'invresolveone'
    | 'invresolveIn'
    | 'invresolveoneIn'
    | 'late'
    | 'library'
    | 'main'
    | 'map'
    | 'mapping'
    | 'merges'
    | 'modeltype'
    | 'object'
    | 'population'
    | 'property'
    | 'query'
    | 'resolve'
    | 'resolveIn'
    | 'resolveone'
    | 'resolveoneIn'
    | 'result'
    | 'static'
    | 'tag'
    | 'transformation'
    | 'typedef'
    | 'uses'
    | 'when'
    | 'where'
    | 'with'
    | 'xmap'
    | 'abstract'
    | 'do'
    | 'switch'
    | 'case'
    | 'raise'
    | 'try'
    | 'catch'
    | 'assert'
    | 'log'
    | 'compute'
    | 'new'
    | 'var'
    | 'break'
    | 'continue'
    | 'return'
    | 'while'
    ;

// ==================== Additional Lexer Tokens ====================

RESET_ASSIGN
    : ':='
    ;

ADD_ASSIGN
    : '+='
    ;

ORDERED_COPY
    : '::='
    ;
