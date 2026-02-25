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

// §8.4: <transformation> ::= <transformation_decl> | <transformation_def>
// Declaration form: transformation T(...); — module elements follow at unit level
// Definition form:  transformation T(...) { moduleElement* } ;?
transformationDef
    : qualifier* 'transformation' qualifiedName '(' modelParamList? ')' moduleUsage* ('{' moduleElement* '}' ';'? | ';')
    ;

libraryDef
    : 'library' qualifiedName simpleSignature? moduleUsage* '{' moduleElement* '}' ';'?
    ;

modelParamList
    : modelParam (',' modelParam)*
    ;

// §8.4: <model_param> ::= <direction>? <param_name>? ':' <typespec>
// Named: out output : ecore — Unnamed: out ecore (name derived from type)
modelParam
    : directionKind qvtoIdentifier ':' typeSpec
    | directionKind typeSpec
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
    : '(' inputParams=paramList? ')' (':' resultList)?
    ;

resultList
    : resultParam (',' resultParam)*
    ;

// §8.4: <declarator> ::= <typespec> | <scoped_identifier> ':' <typespec>
// Allows both named (r : Type) and unnamed (Type → implicit 'result') result parameters.
resultParam
    : qvtoIdentifier ':' typeExpression                                                     # NamedResult
    | typeExpression                                                                        # UnnamedResult
    ;

paramList
    : param (',' param)*
    ;

param
    : directionKind? qvtoIdentifier ':' typeExpression
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
    : 'when' guardBlock
    ;

whereClause
    : 'where' guardBlock
    ;

// §8.4: when/where use ocl_expression_list where ';' is a separator, not a terminator.
// This allows: when { expr }, when { expr; }, when { expr1; expr2 }
guardBlock
    : '{' expression (';' expression)* ';'? '}'
    ;

mappingBody
    : '{' initSection? (populationSection | statementList) endSection? '}'
    ;

initSection
    : 'init' block
    ;

populationSection
    : 'population' block
    ;

endSection
    : 'end' block
    ;

// ==================== Helper / Query / Constructor / Entry ====================

helperDef
    : qualifier* 'helper' scopedName simpleSignature (':' typeExpression)? (block | '=' expression ';' | ';')
    ;

queryDef
    : qualifier* 'query' scopedName simpleSignature ':' resultList (block | '=' expression ';' | ';')
    ;

constructorDef
    : 'constructor' scopedName simpleSignature block ';'?
    ;

// §8.4: <entry> ::= 'main' <simple_signature> <expression_block>
// Supports main() and main(in param : Type) forms
entryDef
    : 'main' simpleSignature block ';'?
    | 'entry' simpleSignature? block ';'?
    ;

simpleSignature
    : '(' paramList? ')'
    ;

// ==================== Property + Intermediate Class + Tag + Typedef ====================

// §8.4: <property> ::= 'intermediate'? <property_key>+ <declarator> ';'
// <declarator> ::= <typespec> <init_part>? | <scoped_identifier> ':' <typespec> <init_part>?
propertyDecl
    : 'intermediate' 'property' scopedName ':' typeExpression ('=' expression)? ';'
    | 'configuration' 'property' qvtoIdentifier ':' typeExpression ('=' expression)? ';'
    | 'property' qvtoIdentifier ':' typeExpression ('=' expression)? ';'
    | 'property' qvtoIdentifier '=' expression ';'                                       // untyped module property
    ;

intermediateClassDef
    : 'intermediate' 'class' qvtoIdentifier ('extends' typeList)? '{' classifierFeature* '}' ';'?
    ;

typeList
    : typeExpression (',' typeExpression)*
    ;

classifierFeature
    : classifierFeatureModifier* qvtoIdentifier ':' typeExpression ('=' expression)? ';'
    ;

classifierFeatureModifier
    : 'static'
    | 'readonly'
    | 'references'
    | 'composes'
    | STEREOTYPE_ID
    ;

tagDecl
    : 'tag' (STRING_LITERAL | DOUBLE_QUOTED_STRING) tagTarget ('=' expression)?
    ;

// §8.4: tag target can reference deeply scoped names like Package::Class::feature
tagTarget
    : qvtoIdentifier ('::' qvtoIdentifier)*
    ;

typedefDecl
    : 'typedef' qvtoIdentifier '=' typeExpression ('with' expression)?
    ;

// ==================== Statements ====================

statementList
    : statement*
    ;

statement
    : varDeclExp ';'                                                                     # VarDeclStatement
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

// Override expression to add assignment as lowest-precedence expression operator.
// §8.2.2.4 AssignExp: assignment IS an expression in QVT-O (not just a statement).
// This is required for: if cond then x := e endif (§8.4 <expression_block>).
// Assignment must be lowest precedence so x := y + 1 means x := (y + 1).
expression
    : expression ('.' | '?.') propertyOrCallSuffix                                       # NavigationExp
    | expression ('->' | '?->') iteratorOrOperationCall                                  # ArrowExp
    // §8.2.2.7: xselect — list[condition], xselectOne — list![condition]
    | expression '[' (xselectIter=qvtoIdentifier '|')? xselectCondition=expression ']'  # XselectExp
    | expression '!' '[' (xselectOneIter=qvtoIdentifier '|')? xselectOneCondition=expression ']'  # XselectOneExp
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
    | <assoc=right> expression assignOp expression ('default' defaultValue=expression)?   # AssignExp
    | primaryExpression                                                                  # PrimaryExp
    ;

// Override primaryExpression to add imperative constructs.
// QVT-O alternatives MUST come BEFORE pathName/OperationCallExp because
// soft keywords (break, continue, return, resolve, log, assert, etc.)
// are also valid qvtoIdentifiers and would match pathName first.
// ANTLR4 resolves ambiguity by picking the first matching alternative.
primaryExpression
    : '(' expression ')'                                                                 # ParenExp
    | 'self'                                                                             # SelfExp
    | 'this'                                                                             # ThisExp
    // §8.2.2.8: imperative if — else is optional (extends OCL if where else is mandatory)
    | 'if' condition=expression 'then' thenExp=expression
      (('elseif'|'elif') elseIfCondition+=expression 'then' elseIfExp+=expression)*
      ('else' elseExp=expression)? 'endif'                                                # IfExp
    // §8.2.2.8: imperative if with block syntax — if (cond) { stmts } else { stmts }
    | 'if' '(' impCondition=expression ')' thenBlock=block
      ('elif' '(' elifCondition+=expression ')' elifBlock+=block)*
      ('else' elseBlock=block)? 'endif'?                                                  # ImperativeIfExp
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

// ==================== Collection Kind Override ====================

// Override to add List as collection literal kind (D26: List alias for Sequence)
collectionKind
    : 'Set'
    | 'OrderedSet'
    | 'Bag'
    | 'Sequence'
    | 'Collection'
    | 'List'
    ;

// ==================== Literal Expression Override ====================

// Override to add Dict literal syntax (§8.2.2.27)
// Dict { 'key1' = value1, 'key2' = value2 }
literalExpression
    : INTEGER_LITERAL                                                                    # IntegerLiteral
    | REAL_LITERAL                                                                       # RealLiteral
    | stringLiteral_                                                                      # StringLiteral
    | 'true'                                                                             # TrueLiteral
    | 'false'                                                                            # FalseLiteral
    | 'null'                                                                             # NullLiteral
    | 'invalid'                                                                          # InvalidLiteral
    | '*'                                                                                # UnlimitedNaturalLiteral
    | collectionLiteral                                                                  # CollectionLit
    | tupleLiteral                                                                       # TupleLit
    | mapLiteral                                                                         # MapLit
    | dictLiteral                                                                        # DictLit
    ;

// §8.4: String literals — single-quoted, double-quoted, and adjacent concatenation
stringLiteral_
    : (STRING_LITERAL | DOUBLE_QUOTED_STRING)+
    ;

dictLiteral
    : 'Dict' '{' (dictLiteralPart (',' dictLiteralPart)*)? '}'
    ;

dictLiteralPart
    : key=expression '=' value=expression
    ;

// ==================== Imperative Expressions ====================

// §8.2.2.2: 'do' keyword can be skipped inside if, switch, compute, and for expressions
blockExp
    : 'do' '{' statement* '}'
    | '{' statement* '}'
    ;

whileExp
    // §8.2.2.4: while with init variable (compute shorthand) — must be before WhileBasic
    // while (x:Type := init; condition) { body }
    : 'while' '(' varName=qvtoIdentifier ':' type=typeExpression ':=' initValue=expression ';' condition=expression ')' block  # WhileWithInit
    | 'while' '(' expression ')' block                                                    # WhileBasic
    ;

forExp
    : forKind '(' forVarList ('|' expression)? ')' block
    ;

forKind
    : 'forEach'
    | 'forOne'
    ;

// §8.2.2.6 + §8.4 p170: <for_exp> ::= ... '(' <iter_declarator_list> (';' <declarator>)? ...
// <declarator> is either a named compute var (x:Type=init) or just a type (Integer).
// The ':' after qvtoIdentifier disambiguates compute shorthand from plain type.
forVarList
    : iteratorVariables ';' qvtoIdentifier ':' typeExpression (varInitOp expression)?      // compute shorthand: i;x:Type=init
    | iteratorVariables ';' typeExpression                                                 // iterator type: i;Integer
    | iteratorVariables                                                                    // plain: i
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
    : qvtoIdentifier ':' typeExpression                                                    # NamedResolveTarget
    | typeExpression                                                                        # TypeOnlyResolveTarget
    ;

tryExp
    : 'try' block catchClause+
    ;

catchClause
    : ('catch'|'except') ('(' ((exceptionVar=qvtoIdentifier ':')? exceptionTypeList)? ')')? block
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
    : 'var' varDeclarator (',' varDeclarator)*
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
// §8.1.11: source.resolve(...), source.late resolve(...), source.resolveIn(...)
propertyOrCallSuffix
    : mappingCallKind scopedName '(' argumentList? ')'                                     # MappingCallSuffix
    | 'late'? resolveKind '(' resolveArgs? ')'                                             # ResolveCallSuffix
    | 'late'? resolveInKind '(' scopedName (',' resolveArgs)? ')'                          # ResolveInCallSuffix
    | qvtoIdentifier '(' argumentList? ')' isMarkedPre?                                    # DotCallSuffix
    | qvtoIdentifier isMarkedPre?                                                          # PropertySuffix
    ;

// Override iterator/operation calls: coll->collect(result | ...), coll->iterate(...)
// §8.2.1.5: ForExp as arrow call: coll->forEach(x) { ... }, coll->forOne(x | cond) { ... }
// §8.1.11.7: coll->late resolve(Type) = coll->xcollect(late resolve(Type))
iteratorOrOperationCall
    : forKind '(' forVarList ('|' expression)? ')' block                                    # ForEachCall
    | 'late'? resolveKind '(' resolveArgs? ')'                                              # ArrowResolveCall
    | 'late'? resolveInKind '(' scopedName (',' resolveArgs)? ')'                           # ArrowResolveInCall
    // §8.2.2.7: list->map f() = list->xcollect(i | i.map f()) — mapping call on collection
    | mappingCallKind scopedName '(' argumentList? ')'                                      # ArrowMappingCall
    | qvtoIdentifier '(' iteratorVariables '|' expression ')'                              # IteratorCall
    | qvtoIdentifier '(' qvtoIdentifier (':' iterType=typeExpression)?
      ';' qvtoIdentifier (':' accType=typeExpression)? '=' expression '|' expression ')'   # IterateCall
    | qvtoIdentifier '(' argumentList? ')'                                                 # CollectionOperationCall
    // §8.2.2.7: list->prop = list->xcollect(i | i.prop) — property collect shorthand
    | qvtoIdentifier                                                                       # ArrowPropertyCall
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
    | primitiveType '::' qvtoIdentifier
    | collectionType '::' qvtoIdentifier
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
    | 'inv'
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
    | 'except'
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

// §8.4: <<id>> stereotype for intermediate class properties (§8.2.1.3)
// Matched as a single 6-char token — no conflict with < / > comparison operators.
STEREOTYPE_ID
    : '<<' 'id' '>>'
    ;

// §8.4: double-quoted string literals (used in tags, expressions, and adjacent concatenation)
DOUBLE_QUOTED_STRING
    : '"' ( '\\\\' | '\\"' | '\\\'' | '\\n' | '\\t' | '\\r' | '\\f' | '\\b' | '\\' [0-3]? [0-7] [0-7]? | ~["\\] )* '"'
    ;
