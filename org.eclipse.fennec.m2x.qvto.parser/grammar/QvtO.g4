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
    | accessDecl
    | moduleElement
    ;

// ==================== Import + Modeltype ====================

qvtoImportDecl
    : 'import' qualifiedName ';'                                          // import all
    | 'from' qualifiedName 'import' importedNameList ';'                  // selective import (§8.4)
    ;

importedNameList
    : '*'                                                                  // import all
    | qvtoIdentifier (',' qvtoIdentifier)*                                // selective
    ;

modeltypeDecl
    : 'modeltype' qvtoIdentifier (STRING_LITERAL | DOUBLE_QUOTED_STRING)? 'uses' packageRefList modeltypeWhere? ';'
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
// §8.4.7: <transformation_h> ::= <qualifier>* 'transformation' <identifier>
//         <transformation_signature> <transformation_usage_refine>?
// <transformation_usage_refine> ::= <module_usage> | <transformation_refine>
// <transformation_refine> ::= 'refines' <moduleref>
transformationDef
    : qualifier* 'transformation' qualifiedName '(' modelParamList? ')' (moduleUsage* | transformationRefine) ('{' moduleElement* '}' ';'? | ';')
    ;

transformationRefine
    : 'refines' moduleRef
    ;

libraryDef
    : 'library' qualifiedName simpleSignature? moduleUsage* '{' moduleElement* '}' ';'?
    | 'library' qualifiedName simpleSignature? moduleUsage* ';'
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
    : moduleRef (',' moduleRef)*
    ;

// §8.4.7: <moduleref> ::= <scoped_identifier> <simple_signature>?
// Spec uses '::' (<scoped_identifier>), Eclipse uses '.' (<qualifiedName>).
// We accept both separators for compatibility.
moduleRef
    : moduleRefName simpleSignature?
    ;

moduleRefName
    : qvtoIdentifier (('::' | '.') qvtoIdentifier)*
    ;

// §8.4.7: standalone access declaration (unit-level or module-level)
accessDecl
    : 'access' ('transformation' | 'library')? moduleRefList ';'
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
    | exceptionDef
    | datatypeDef
    | primitiveDef
    | enumDef
    | metamodelDef
    | tagDecl ';'
    | typedefDecl ';'
    | accessDecl
    ;

// ==================== Mapping ====================

mappingDef
    : qualifier* 'mapping' directionKind? scopedName mappingSignature
      mappingExtension* whenClause? whereClause? mappingBody ';'?
    | qualifier* 'mapping' directionKind? scopedName mappingSignature
      mappingExtension* ';'
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

// §8.4.7: <mapping_extra> ::= <mapping_extension> | <mapping_refinement>
// <mapping_extension> ::= <mapping_extension_key> <scoped_identifier_list>
// <mapping_refinement> ::= 'refines' <scoped_identifier>
mappingExtension
    : mappingExtensionKind scopedNameList
    | 'refines' scopedName
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

// §8.4.7: <constructor> ::= <constructor_decl> | <constructor_def>
// <constructor_header> ::= <qualifier>* 'constructor' <scoped_identifier> <simple_signature>
constructorDef
    : qualifier* 'constructor' scopedName simpleSignature (block ';'? | ';')
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

// §8.4: <classifier_info> ::= 'exception' — exception classifier declaration
exceptionDef
    : 'exception' qvtoIdentifier ('extends' typeList)? '{' classifierFeature* '}' ';'?
    ;

// §8.4: <classifier_info> ::= 'datatype' — user-defined datatype declaration
datatypeDef
    : 'datatype' qvtoIdentifier '{' classifierFeature* '}' ';'?
    ;

// §8.4: <classifier_info> ::= 'primitive' — primitive type declaration
primitiveDef
    : 'primitive' qvtoIdentifier ';'
    ;

// §8.4.6: <enumeration> ::= 'enum' <identifier> '{' <enum_literal_list> '}'
enumDef
    : 'enum' qvtoIdentifier '{' enumLiteralList? '}' ';'?
    ;

enumLiteralList
    : enumLiteral (',' enumLiteral)*
    ;

enumLiteral
    : STRING_LITERAL
    | DOUBLE_QUOTED_STRING
    | qvtoIdentifier
    ;

// §8.4.6: <metamodel> ::= ('metamodel' | 'package') <name> '{' <metamodel_element>* '}' ';'?
metamodelDef
    : ('metamodel' | 'package') qvtoIdentifier '{' metamodelElement* '}' ';'?
    ;

metamodelElement
    : intermediateClassDef
    | exceptionDef
    | datatypeDef
    | primitiveDef
    | enumDef
    | tagDecl ';'
    ;

typeList
    : typeExpression (',' typeExpression)*
    ;

classifierFeature
    : classifierFeatureModifier* qvtoIdentifier ':' typeExpression
      simpleSignature ';'                                                     // §8.4: <classifier_operation>
    | classifierFeatureModifier* qvtoIdentifier ':' typeExpression
      multiplicity? ('ordered')? oppositeProperty? ('=' expression)? ';'     // §8.4: <classifier_property>
    ;

classifierFeatureModifier
    : 'static'
    | 'readonly'
    | 'references'
    | 'composes'
    | 'derived'
    | stereotypeQualifier
    ;

stereotypeQualifier
    : '<<' qvtoIdentifier (',' qvtoIdentifier)* '>>'
    ;

multiplicity
    : '[' multiplicityRange ']'
    ;

multiplicityRange
    : INTEGER_LITERAL '..' (INTEGER_LITERAL | '*')
    | INTEGER_LITERAL
    | '*'
    ;

oppositeProperty
    : 'opposites' '~'? qvtoIdentifier multiplicity?
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
    | '-='
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
    | expression ('->' | '?->' | '!->') iteratorOrOperationCall                           # ArrowExp
    // §8.2.2.7: xselect — list[condition], xselectOne — list![condition]
    | expression '[' (xselectIter=qvtoIdentifier '|')? xselectCondition=expression ']'  # XselectExp
    | expression '!' '[' (xselectOneIter=qvtoIdentifier '|')? xselectOneCondition=expression ']'  # XselectOneExp
    // §8.4.4: shorthand operators — #Type → oclIsKindOf, ##Type → oclIsTypeOf, *"stereo" → stereotypedBy
    | HASH expression                                                                    # HashExp
    | DOUBLE_HASH expression                                                             # DoubleHashExp
    | '*' expression                                                                     # UnaryStarExp
    | 'not' expression                                                                   # NotExp
    | '-' expression                                                                     # UnaryMinusExp
    | expression op=('*' | '/' | '%') expression                                          # MultExp
    | expression op=('+' | '-') expression                                               # AddExp
    | expression op=('<' | '>' | '<=' | '>=') expression                                 # CompareExp
    // §8.4.4: '!=' is a synonym for '<>', '==' is a synonym for '=' (like Eclipse QVT-O)
    | expression op=('=' | '<>' | '!=' | '==') expression                                  # EqualityExp
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
    | ('*' | 'unlimited')                                                                # UnlimitedNaturalLiteral
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
    : key=literalSimple '=' value=expression
    ;

// §8.4.7: <literal_simple> ::= <INTEGER> | <FLOAT> | <STRING> | 'true' | 'false' | 'unlimited' | 'null'
literalSimple
    : INTEGER_LITERAL                                                                    # SimpleLitInt
    | REAL_LITERAL                                                                       # SimpleLitReal
    | stringLiteral_                                                                      # SimpleLitString
    | 'true'                                                                             # SimpleLitTrue
    | 'false'                                                                            # SimpleLitFalse
    | 'null'                                                                             # SimpleLitNull
    | ('*' | 'unlimited')                                                                # SimpleLitUnlimited
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
    : 'switch' ('(' switchIterator ')')? '{' switchAlt* ('else' expression ';')? '}'
    ;

// §8.4.7: <switch_exp> ::= 'switch' ('(' <iter_declarator> ')')? <switch_body>
// Eclipse LPG rules 443/444: switchDeclaratorCS ::= IDENTIFIER | IDENTIFIER = OclExpressionCS
switchIterator
    : qvtoIdentifier (':' typeExpression)? (varInitOp expression)?
    ;

switchAlt
    : 'case' '(' expression ')' expression ';'
    ;

computeExp
    : 'compute' '(' varDeclarator ')' block
    ;

// §8.4.7: <object_exp> ::= 'object' ('(' <iter_declarator> ')')? <object_declarator> <expression_block>
objectExp
    : 'object' objectIterator? (varName=qvtoIdentifier ':')? typeExpression? ('@' extentName=qvtoIdentifier)? block
    ;

objectIterator
    : '(' qvtoIdentifier ':' typeExpression (varInitOp expression)? ')'
    ;

newExp
    : 'new' pathName ('@' extentName=qvtoIdentifier)? '(' argumentList? ')'
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

// §8.4.7: <var_init_exp> ::= 'var' <declarator_list> | 'var' '(' <declarator_list> ')'
varDeclExp
    : 'var' varDeclarator (',' varDeclarator)*
    | 'var' '(' varDeclarator (',' varDeclarator)* ')'
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
    // §8.4.7: coll->object(x:T) Type { ... } = coll->collect(x | object Type { ... })
    | objectExp                                                                             # ArrowObjectCall
    // §8.2.2.8: coll->switch(i) { ... } = coll->xcollect(i | switch { ... })
    | switchExp                                                                             # ArrowSwitchCall
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

// §8.4.7: <scoped_identifier> ::= <identifier> ('::' <identifier>)*
// Also supports primitive/collection context types (e.g., String::wrap(), Collection(Real)::sum())
scopedName
    : primitiveType '::' qvtoIdentifier
    | collectionType '::' qvtoIdentifier
    | qvtoIdentifier ('::' qvtoIdentifier)*
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
    | ESCAPED_IDENTIFIER
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
    | 'from'
    | 'opposites'
    | 'datatype'
    | 'derived'
    | 'enum'
    | 'exception'
    | 'metamodel'
    | 'ordered'
    | 'package'
    | 'primitive'
    | 'readonly'
    | 'composes'
    | 'references'
    | 'unlimited'
    | 'class'
    | 'default'
    | 'refines'
    ;

// ==================== Comment Override ====================

// §8.4.2: QVT-O supports '//' as line comment in addition to '--' (from OCL) and '/* ... */' (block).
// Override OCL's LINE_COMMENT to accept both styles.
LINE_COMMENT
    : ('--' | '//') ~[\r\n]* -> skip
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

SUBTRACT_ASSIGN
    : '-='
    ;

// §8.4.4: '##' before '#' — longest match first (ANTLR4 lexer rule ordering)
DOUBLE_HASH
    : '##'
    ;

HASH
    : '#'
    ;

// §8.4.4: '==' as synonym for '=' — explicit token so lexer matches as single token (not two '=')
DOUBLE_EQUAL
    : '=='
    ;

// §8.4.4: '!=' as synonym for '<>' — explicit token to disambiguate from '!' '[' (XselectOne)
NOT_EQUAL_EXEQ
    : '!='
    ;

// §8.4.4: '!->' as not-arrow — explicit token to disambiguate from '!' '->' sequence
NOT_ARROW
    : '!->'
    ;

// §8.4: Chevron tokens for stereotype qualifiers on intermediate class features (§8.2.1.3)
// ANTLR4 longest-match: '<<' wins over '<' in lexer. No shift operators in QVT-O.
LCHEVRON2 : '<<' ;
RCHEVRON2 : '>>' ;

// §8.4: double-quoted string literals (used in tags, expressions, and adjacent concatenation)
DOUBLE_QUOTED_STRING
    : '"' ( '\\\\' | '\\"' | '\\\'' | '\\n' | '\\t' | '\\r' | '\\f' | '\\b' | '\\' [0-3]? [0-7] [0-7]? | ~["\\] )* '"'
    ;
