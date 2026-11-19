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
 * ANTLR4 combined lexer+parser grammar for QVT-Relations v1.3.
 * Imports the OCL v2.5 base grammar and adds relational constructs:
 * transformations, typed models, relations, domains, templates, keys, queries.
 *
 * D33: Direct interpretation (no QVT-C lowering).
 * D35: Spec-first approach.
 */
grammar QvtR;

import Ocl;

// ==================== Entry Point ====================

compilationUnitEntry
    : compilationUnit EOF
    ;

compilationUnit
    : importDecl* transformationDef*
    ;

// ==================== Import ====================

// §7.13.4: <import> ::= 'import' [<identifier> ':'] <URI> ('::' <identifier>)* ['::*'] ';'
// A quoted URI names a metamodel, an unquoted qualified name a compilation unit (§7.13.1, #360)
importDecl
    : 'import' (alias=identifier ':')? importTarget ('::' identifier)* (wildcard='::' '*')? ';'
    ;

importTarget
    : STRING_LITERAL
    | qualifiedName
    ;

qualifiedName
    : identifier ('.' identifier)*
    ;

// ==================== Transformation ====================

// §7.13.4: <transformationId> ::= <pathNameCS> (#360)
transformationDef
    : 'transformation' name=pathName '(' modelDecl (',' modelDecl)* ')'
      ('extends' base=pathName)?
      '{' keyDecl* (relation | queryDef)* '}'
    ;

// ==================== Model Declaration ====================

modelDecl
    : identifier ':' ( metaModelId | '{' metaModelId (',' metaModelId)* '}' )
    ;

metaModelId
    : pathName
    ;

// ==================== Key Declaration (§7.4 / §7.13.4) ====================

keyDecl
    : 'key' pathName '{' keyProperty (',' keyProperty)* '}' ';'
    ;

keyProperty
    : identifier
    | 'opposite' '(' pathName '::' identifier ')'
    ;

// ==================== Relation ====================

relation
    : 'top'? 'abstract'? 'relation' identifier ('overrides' identifier)?
      '{'
      varDeclaration*
      (domain | primitiveTypeDomain)+
      whenClause? whereClause?
      '}'
    ;

// ==================== Variable Declaration ====================

varDeclaration
    : identifier (',' identifier)* ':' typeExpression ('=' expression)? ';'
    ;

// ==================== Domain ====================

domain
    : checkEnforceQualifier? 'domain' identifier template (',' template)*
      ('implementedby' pathName '(' argumentList? ')')?
      ('default_values' '{' assignmentExp+ '}')?
      ';'
    ;

primitiveTypeDomain
    : 'primitive' 'domain' identifier ':' typeExpression ';'
    ;

checkEnforceQualifier
    : 'checkonly'
    | 'enforce'
    ;

assignmentExp
    : identifier '=' expression ';'
    ;

// ==================== Template (§7.11.2) ====================

template
    : (objectTemplate | collectionTemplate) ('{' expression '}')?
    ;

objectTemplate
    : identifier? ':' pathName optionalMultiplicity? '{' propertyTemplateList? '}'
    ;

optionalMultiplicity
    : '[' '?' ']'
    ;

propertyTemplateList
    : propertyTemplate (',' propertyTemplate)*
    ;

propertyTemplate
    : identifier '=' template
    | identifier '=' expression
    | 'opposite' '(' pathName '::' identifier ')' '=' template
    | 'opposite' '(' pathName '::' identifier ')' '=' expression
    ;

collectionTemplate
    : identifier? ':' collectionKind '(' typeExpression ')' '{' memberSelection? '}'
    ;

memberSelection
    : memberItem (',' memberItem)* ('++' (identifier | '_'))?
    ;

memberItem
    : identifier
    | template
    | '_'
    ;

// ==================== When / Where ====================

whenClause
    : 'when' '{' (predicate ';')* '}'
    ;

whereClause
    : 'where' '{' (predicate ';')* '}'
    ;

// §7.13.5: <OclExpressionCS> ::= … | <template> — a template is a predicate that holds when
// it has a match (decided in #361)
predicate
    : template
    | expression
    ;

// ==================== Query (§7.11.3.6 / Function) ====================

queryDef
    : 'query' pathName '(' (paramDecl (',' paramDecl)*)? ')' ':' typeExpression
      (';' | '{' expression '}')
    ;

paramDecl
    : identifier ':' typeExpression
    ;

// ==================== Identifiers (§7.13.2) ====================

// Overrides Ocl.g4's identifier: 'import' is reserved in QVT-R (§7.13.2), and abstract, opposite
// and default_values — literals of §7.13.4 but not in the reserved list — are names (#361)
identifier
    : IDENTIFIER
    | ESCAPED_IDENTIFIER
    | qvtrDocumentKeyword
    | softKeyword
    ;

qvtrDocumentKeyword
    : 'body' | 'context' | 'def' | 'derive' | 'endpackage' | 'init' | 'inv' | 'package'
    | 'post' | 'pre' | 'static' | 'include' | 'library'
    ;

softKeyword
    : 'abstract' | 'opposite' | 'default_values'
    ;

// ==================== New Keywords ====================
// These are QVT-R specific keywords not present in OCL.
// ANTLR4 handles them as inline string literals in parser rules;
// the Ocl.g4 identifier rule already allows escaped identifiers (_'...').

CHECKONLY     : 'checkonly' ;
DOMAIN        : 'domain' ;
ENFORCE       : 'enforce' ;
EXTENDS       : 'extends' ;
IMPLEMENTEDBY : 'implementedby' ;
IMPORT        : 'import' ;
KEY           : 'key' ;
OVERRIDES     : 'overrides' ;
PRIMITIVE     : 'primitive' ;
QUERY         : 'query' ;
RELATION      : 'relation' ;
TOP           : 'top' ;
TRANSFORMATION: 'transformation' ;
WHEN          : 'when' ;
WHERE         : 'where' ;
DEFAULT_VALUES: 'default_values' ;
ABSTRACT      : 'abstract' ;
OPPOSITE      : 'opposite' ;
PLUSPLUS       : '++' ;
COLONCOLON     : '::' ;
UNDERSCORE     : '_' ;
