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

importDecl
    : 'import' qualifiedName ('::' qualifiedName)* ('::' '*')? ';'
    ;

qualifiedName
    : identifier ('.' identifier)*
    ;

// ==================== Transformation ====================

transformationDef
    : 'transformation' identifier '(' modelDecl (',' modelDecl)* ')'
      ('extends' identifier)?
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
    : 'when' '{' (expression ';')* '}'
    ;

whereClause
    : 'where' '{' (expression ';')* '}'
    ;

// ==================== Query (§7.11.3.6 / Function) ====================

queryDef
    : 'query' pathName '(' (paramDecl (',' paramDecl)*)? ')' ':' typeExpression
      (';' | '{' expression '}')
    ;

paramDecl
    : identifier ':' typeExpression
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
