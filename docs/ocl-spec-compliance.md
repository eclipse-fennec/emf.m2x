# OCL Spec-Compliance Analysis

> Systematic comparison of our Fennec OCL implementation against the **OMG OCL v2.4 specification** (formal/2014-02-03), with Eclipse OCL as reference implementation.
>
> **Date:** 2026-02-25
> **OCL Tests:** 4202 Tests, 0 Failures
> **Overall:** ~97% of standard library operations implemented, all core language features covered

**Related documents:**
- [OCL Architecture](ocl-architecture.md) — implementation details
- [Design Decisions](design-decisions.md) — D1–D18 (OCL-specific)
- [Development Guideline](development-guideline.md) — conventions, testing, roadmap
- **Specification:** `docs/specifications/OCL/OCL-formal-14-02-03.pdf`

---

## Table of Contents

1. [Summary of Gaps](#1-summary-of-gaps)
2. [Chapter 7 — OCL Language Features](#2-chapter-7--ocl-language-features)
3. [Chapter 8 — Abstract Syntax (Metamodel)](#3-chapter-8--abstract-syntax-metamodel)
4. [Chapter 9 — Concrete Syntax (Grammar)](#4-chapter-9--concrete-syntax-grammar)
5. [Chapter 10 — Semantics / Evaluation](#5-chapter-10--semantics--evaluation)
6. [Chapter 11 — Standard Library](#6-chapter-11--standard-library)
7. [Chapter 12 — Complete OCL](#7-chapter-12--complete-ocl)
8. [Chapter 13 — Relation to UML](#8-chapter-13--relation-to-uml)
9. [Gap Detail Sheets](#9-gap-detail-sheets)

---

## 1. Summary of Gaps

### Gap Overview Table

#### Completed

| ID | Gap | Spec Ref | Status | Eclipse OCL |
|----|-----|----------|--------|-------------|
| **S-01** | `oclContainer()` / `oclContents()` | §11.2.1 | **Done** ✅ | Implemented |
| **S-02** | `def` expressions runtime support | §12.6 | **Done** ✅ | Implemented |
| **S-04** | `oclLocale` attribute | §11.2.1 | **Done** ✅ | Implemented |
| **S-09** | `oclAsType()` on collections | §11.7 | **Done** ✅ | Partial |

#### Open — ordered by implementation priority

| ID | Gap | Spec Ref | Priority | Status | Eclipse OCL |
|----|-----|----------|----------|--------|-------------|
| **S-10** | `validate()` type-checker (no-op stub) | §9 | **MEDIUM** | Deferred to Phase 5 (LSP) | Implemented |
| **S-06** | `oclIsInState()` | §11.2.1, §7.5.10 | **LOW** | Deferred (UML-only) | Implemented (UML Pivot) |
| **S-07** | Association class navigation | §7.5.4 | **LOW** | Deferred (UML-only) | Implemented (UML Pivot) |
| **S-08** | Qualified association navigation | §7.5.4 | **LOW** | Deferred (UML-only) | Implemented (UML Pivot) |
| **S-03** | OclMessage / `^^` operator | §11.10, §7.6.4 | **DEFERRED** | Deferred indefinitely | Not implemented (same) |
| **S-05** | `UnspecifiedValueExp` evaluator case | §10.3.1 | **DEFERRED** | Deferred indefinitely (tied to S-03) | Not implemented (same) |

### Existing GAP Status Updates

| Old ID | Description | Old Status | **Correct Status** | Notes |
|--------|-------------|------------|-------------------|-------|
| GAP-2 | `@pre` evaluation | Open | **DONE** ✅ | `PreStateSnapshot` + `OclEvaluator` lines 108–128 |
| GAP-7 | `oclIsNew` | Deferred | **DONE** ✅ | `OclStdlib.java` — compares against PreStateSnapshot |
| GAP-3 | `validate()` no-op | Open | Open (= S-10) | Still a stub |
| GAP-6 | MessageExp | Deferred | Deferred (= S-03) | Eclipse also doesn't implement |

### Priority Legend

- **MEDIUM** — Missing functionality for advanced use cases (next to implement)
- **LOW** — UML-only features (Ecore has no equivalent), would require UML metamodel support
- **DEFERRED** — Features that Eclipse OCL also skips for Ecore; only relevant with UML behavioral modeling

---

## 2. Chapter 7 — OCL Language Features

### 2.1 Property Access (§7.5)

| Feature | Spec Section | Status | Notes |
|---------|-------------|--------|-------|
| Attribute access | §7.5.1 | ✅ | Via `eGet()` |
| Operation calls | §7.5.2 | ✅ | 3-level dispatch (stdlib → accessor → provider) |
| Navigation (references) | §7.5.3 | ✅ | Single/multi-valued |
| Association class navigation | §7.5.4 | ⚠️ S-07 | Ecore has no association classes |
| Qualified associations | §7.5.4 | ⚠️ S-08 | Ecore has no qualifiers |
| Implicit self | §7.5.5 | ✅ | Parser resolves |
| Self reference | §7.5.6 | ✅ | Implicit `self` in contexts |
| Previous values (`@pre`) | §7.5.7 | ✅ | `PreStateSnapshot` mechanism |
| `oclIsNew()` | §7.5.8 | ✅ | Compares against snapshot |
| Collection navigation | §7.5.9 | ✅ | Implicit collect |
| `oclIsInState()` | §7.5.10 | ⚠️ S-06 | UML state machines only |

### 2.2 Literals and Types (§7.3, §7.4)

| Feature | Status | Notes |
|---------|--------|-------|
| Integer, Real, String, Boolean literals | ✅ | All parseable and evaluable |
| Null literal | ✅ | |
| Invalid literal | ✅ | |
| UnlimitedNatural (`*`) | ✅ | Mapped to `Integer.MAX_VALUE` |
| Enum literals | ✅ | |
| Type literals | ✅ | |
| Collection literals (Set, Bag, Sequence, OrderedSet) | ✅ | Including ranges `a..b` |
| Tuple literals | ✅ | |
| Map literals (v2.5) | ✅ | Non-standard extension |

### 2.3 Control Flow (§7.6)

| Feature | Status | Notes |
|---------|--------|-------|
| `if-then-else-endif` | ✅ | |
| `let-in` expressions | ✅ | |
| Iterator expressions | ✅ | All 12 iterators |
| `iterate` | ✅ | Accumulator pattern |
| Message expressions (`^^`, `^`) | ⚠️ S-03 | Not parseable |

### 2.4 Safe Navigation (v2.5, §7.5.11)

| Feature | Status | Notes |
|---------|--------|-------|
| `?.` (safe property) | ✅ | Returns null instead of OclInvalid |
| `?->` (safe collection) | ✅ | Returns empty collection |
| `isIsSafe()` flag | ✅ | Checked in `OclEvaluator` lines 318, 393, 566, 613 |

---

## 3. Chapter 8 — Abstract Syntax (Metamodel)

Our OCL metamodel has **50 classifiers** in `org.eclipse.fennec.m2x.ocl.model`.

### 3.1 Expression AST Nodes

| Spec Node | Our Implementation | Status |
|-----------|-------------------|--------|
| `OclExpression` (abstract root) | `OclExpression` | ✅ |
| `LiteralExp` hierarchy | All 10 literal types | ✅ |
| `VariableExp` | `VariableExp` | ✅ |
| `IfExp` | `IfExp` | ✅ |
| `LetExp` | `LetExp` | ✅ |
| `PropertyCallExp` | `PropertyCallExp` | ✅ |
| `OperationCallExp` | `OperationCallExp` | ✅ |
| `IteratorExp` | `IteratorExp` | ✅ |
| `IterateExp` | `IterateExp` | ✅ |
| `CollectionLiteralExp` | `CollectionLiteralExp` | ✅ |
| `TupleLiteralExp` | `TupleLiteralExp` | ✅ |
| `TypeExp` | `TypeExp` | ✅ |
| `UnlimitedNaturalLiteralExp` | `UnlimitedNaturalLiteralExp` | ✅ |
| `StateExp` | — | ⚠️ S-06 | UML states |
| `MessageExp` | — | ⚠️ S-03 |
| `OclMessageArg` | — | ⚠️ S-03 |
| `UnspecifiedValueExp` | — | ⚠️ S-05 |
| `AssociationClassCallExp` | — | ⚠️ S-07 | UML only |

### 3.2 Type Nodes

| Spec Type | Our Implementation | Status |
|-----------|-------------------|--------|
| `OclAny` | ✅ (implicit, all EObjects) | ✅ |
| `OclVoid` | `OclVoidType` | ✅ |
| `OclInvalid` | `OclInvalidType` | ✅ |
| `CollectionType` (Set, Bag, Sequence, OrderedSet) | ✅ | ✅ |
| `TupleType` | `TupleType` | ✅ |
| `MapType` (v2.5) | `MapType` | ✅ |
| `PrimitiveType` | Mapped to EDataType | ✅ |
| `MessageType` | — | ⚠️ S-03 |
| `TemplateParameterType` | — | N/A (no generic OCL types used) |

---

## 4. Chapter 9 — Concrete Syntax (Grammar)

Our ANTLR4 grammar `Ocl.g4` covers all production rules from the spec except:

| Grammar Rule | Status | Notes |
|-------------|--------|-------|
| All expression rules | ✅ | Including precedence |
| Collection literal parts | ✅ | Including ranges |
| Iterator/iterate syntax | ✅ | |
| `let-in` | ✅ | |
| `if-then-else-endif` | ✅ | |
| `@pre` suffix | ✅ | |
| Safe navigation `?.` / `?->` | ✅ | v2.5 |
| Message `^^` / `^` | ❌ S-03 | Not in grammar |
| Qualified name navigation | ✅ | Package::Type |
| Complete OCL document | ✅ | `OclDocumentBuilder` |

### Type-Checker / Validation (§9)

The spec defines static type rules that a conforming implementation should check. Our `validate()` method on `OclEngine` is currently a **no-op stub** (S-10 / GAP-3). The parser does basic type resolution but doesn't enforce all static type rules.

---

## 5. Chapter 10 — Semantics / Evaluation

### 5.1 Three-Valued Logic

| Aspect | Status | Notes |
|--------|--------|-------|
| true / false / OclInvalid | ✅ | `OclInvalid.INSTANCE` sentinel |
| Short-circuit `and` (false ∧ invalid = false) | ✅ | Spec §10 Table 10.1 |
| Short-circuit `or` (true ∨ invalid = true) | ✅ | |
| Short-circuit `implies` (false ⇒ invalid = true) | ✅ | |
| `not(invalid)` = invalid | ✅ | |

### 5.2 Null / Invalid Propagation

| Aspect | Status | Notes |
|--------|--------|-------|
| `null = null` → true | ✅ | §10 |
| `null <> null` → false | ✅ | |
| `null.op()` → OclInvalid (strict) | ✅ | |
| `null.op()` → null (lenient/safe nav) | ✅ | |
| OclInvalid propagation | ✅ | Strict mode |
| OclVoid as subtype of all | ✅ | `null` matches any type |
| OclInvalid as subtype of OclVoid | ✅ | Type hierarchy correct |

### 5.3 UnlimitedNatural

| Aspect | Status | Notes |
|--------|--------|-------|
| `*` literal | ✅ | Maps to `Integer.MAX_VALUE` |
| Arithmetic (`* + 1` = invalid) | ✅ | |
| Comparison (`* > n` = true for finite n) | ✅ | |
| `* = *` → true | ✅ | |
| `oclIsUndefined()` on `*` → false | ✅ | |

### 5.4 @pre and Postconditions

| Aspect | Status | Notes |
|--------|--------|-------|
| `@pre` parsing | ✅ | `PropertyCallExp.isPre()` flag |
| `@pre` evaluation via snapshot | ✅ | `PreStateSnapshot` captures pre-state |
| `oclIsNew()` | ✅ | Checks existence in snapshot |
| Postcondition evaluation | ✅ | `InvocationDelegate` supports `pre:`/`post:` annotations |

### 5.5 Missing Evaluator Cases

| Feature | Status | Notes |
|---------|--------|-------|
| `UnspecifiedValueExp` | ⚠️ S-05 | No `caseUnspecifiedValueExp` in evaluator. Eclipse also doesn't implement. Rarely used in practice (represents `?` in constraints). |
| `MessageExp` | ⚠️ S-03 | No parser/evaluator support. Eclipse also doesn't implement for Ecore. |

---

## 6. Chapter 11 — Standard Library

### 6.1 Coverage Summary

**195 out of 201 spec operations implemented (97%)**

### 6.2 OclAny Operations (§11.2)

| Operation | Status | Notes |
|-----------|--------|-------|
| `=` (equality) | ✅ | |
| `<>` (inequality) | ✅ | |
| `oclIsTypeOf(type)` | ✅ | |
| `oclIsKindOf(type)` | ✅ | |
| `oclAsType(type)` | ✅ | |
| `oclIsUndefined()` | ✅ | |
| `oclIsInvalid()` | ✅ | |
| `oclIsNew()` | ✅ | Via PreStateSnapshot |
| `oclIsInState(stateSpec)` | ⚠️ S-06 | UML state machines only |
| `oclType()` | ✅ | Returns EClass/EDataType |
| `oclContainer()` | ✅ | Via `eContainer()` — returns null for root |
| `oclContents()` | ✅ | Via `eContents()` — returns Set of direct children |
| `oclLocale` | ✅ S-04 | Property on OclAny, default `'en_us'`, overridable via `let` |
| `toString()` | ✅ | |

### 6.3 Boolean Operations (§11.3)

All implemented: `and`, `or`, `xor`, `not`, `implies` — including three-valued logic truth tables. ✅

### 6.4 Integer Operations (§11.4)

All implemented: `+`, `-`, `*`, `/`, `mod`, `div`, `abs`, `max`, `min`, `floor`, `ceiling`, `round`, `toReal()`, `toString()`. ✅

Comparisons: `<`, `>`, `<=`, `>=`. ✅

### 6.5 Real Operations (§11.4)

All implemented: `+`, `-`, `*`, `/`, `abs`, `floor`, `round`, `max`, `min`, `toInteger()`, `toString()`. ✅

### 6.6 String Operations (§11.5)

| Operation | Status | Notes |
|-----------|--------|-------|
| `size()` | ✅ | |
| `concat(s)` | ✅ | |
| `substring(lower, upper)` | ✅ | 1-based, validates bounds |
| `toInteger()` | ✅ | |
| `toReal()` | ✅ | |
| `toUpperCase()` / `toUpper()` | ✅ | Both forms |
| `toLowerCase()` / `toLower()` | ✅ | Both forms |
| `indexOf(s)` | ✅ | 1-based result |
| `equalsIgnoreCase(s)` | ✅ | |
| `at(i)` | ✅ | 1-based |
| `characters()` | ✅ | |
| `toBoolean()` | ✅ | |
| `matches(regex)` | ✅ | |
| `replaceAll(regex, replacement)` | ✅ | |
| `replaceFirst(regex, replacement)` | ✅ | |
| `startsWith(s)` | ✅ | Non-standard extension |
| `endsWith(s)` | ✅ | Non-standard extension |
| `trim()` | ✅ | Non-standard extension |
| `+` (concatenation) | ✅ | Non-standard extension |

### 6.7 Collection Operations (§11.7)

All standard collection operations are implemented for Set, Bag, Sequence, OrderedSet:

| Category | Operations | Status |
|----------|-----------|--------|
| Basic | `size`, `isEmpty`, `notEmpty`, `count`, `includes`, `excludes`, `includesAll`, `excludesAll` | ✅ |
| Adding/Removing | `including`, `excluding` | ✅ (`excluding` removes all occurrences per spec) |
| Set algebra | `union`, `intersection`, `symmetricDifference`, `-` | ✅ |
| Order-dependent | `first`, `last`, `at`, `indexOf`, `append`, `prepend`, `insertAt`, `reverse` | ✅ |
| Conversion | `asSet`, `asBag`, `asSequence`, `asOrderedSet` | ✅ |
| Flattening | `flatten` | ✅ |
| Aggregation | `sum`, `product` | ✅ |
| Sorting | `sortedBy` (iterator) | ✅ |
| `subSequence`, `subOrderedSet` | ✅ | 1-based |

### 6.8 Iterator Operations (§11.9)

All 12 spec iterators implemented:

| Iterator | Status |
|----------|--------|
| `select` | ✅ |
| `reject` | ✅ |
| `collect` | ✅ |
| `collectNested` | ✅ |
| `forAll` | ✅ |
| `exists` | ✅ |
| `any` | ✅ |
| `one` | ✅ |
| `isUnique` | ✅ |
| `sortedBy` | ✅ |
| `closure` | ✅ |
| `iterate` | ✅ |

### 6.9 Tuple Operations (§11.8)

All implemented: construction, element access, equality, `=`, `<>`. ✅

### 6.10 Map Operations (v2.5)

All implemented: `at()`, `put()`, `size()`, `includes()`, `excludes()`, `keys()`, `values()`, `includesMap()`, `excludesMap()`, `includingMap()`, `excludingMap()`. ✅

### 6.11 OclMessage Operations (§11.10)

| Operation | Status | Notes |
|-----------|--------|-------|
| `hasReturned()` | ⚠️ S-03 | Not implemented |
| `result()` | ⚠️ S-03 | Not implemented |
| `isSignalSent()` | ⚠️ S-03 | Not implemented |
| `isOperationCall()` | ⚠️ S-03 | Not implemented |

Eclipse OCL also does **not** implement OclMessage for Ecore models. This is primarily a UML concept for asynchronous operation calls.

---

## 7. Chapter 12 — Complete OCL

Complete OCL document support (standalone `.ocl` files that augment existing metamodels):

| Feature | Status | Notes |
|---------|--------|-------|
| `OclDocumentBuilder` | ✅ | Parses `.ocl` documents |
| `package` context declarations | ✅ | |
| `context Type` | ✅ | |
| `inv:` constraints | ✅ | |
| `pre:` / `post:` conditions | ✅ | |
| `body:` operation bodies | ✅ | |
| `derive:` derived features | ✅ | |
| `init:` initial values | ✅ | |
| `def:` additional features | ✅ S-02 | `loadDocument()` registers def-properties and def-operations |
| `static def:` | ✅ | Parser + `isStatic` flag on Constraint (OCL v2.4 §12.12.6) |
| `import` declarations | ✅ | |
| `endpackage` | ✅ | |

### S-02: `def` Expression Support — ✅ Done

`def:` expressions are fully supported at runtime via `OclEngine.loadDocument()`. Both def-properties and def-operations are available during evaluation:

```ocl
context Person
  def: fullName : String = firstName.concat(' ').concat(lastName)
  inv nameLength: fullName.size() > 0  -- WORKS: fullName resolved via DefRegistry
```

---

## 8. Chapter 13 — Relation to UML

The spec defines how OCL integrates with UML metamodel features that have no direct Ecore equivalent:

| UML Feature | Spec Section | Status | Notes |
|-------------|-------------|--------|-------|
| Class attributes/operations | §13.1 | ✅ | Mapped to EClass/EAttribute/EOperation |
| Associations | §13.2 | ✅ | Mapped to EReference |
| Association classes | §13.2 | ⚠️ S-07 | No Ecore equivalent |
| Qualified associations | §13.2 | ⚠️ S-08 | No Ecore equivalent |
| State machines | §13.3 | ⚠️ S-06 | No Ecore equivalent |
| Signals / Messages | §13.4 | ⚠️ S-03 | No Ecore equivalent |
| Stereotypes | §13.5 | N/A | Not relevant for Ecore |
| Profiles | §13.5 | N/A | Not relevant for Ecore |

**Design decision (D16):** No Pivot layer. We work directly with Ecore, which means UML-only features are inherently out of scope. This is a deliberate trade-off.

---

## 9. Gap Detail Sheets

### Completed Gaps

#### S-01: `oclContainer()` / `oclContents()` — **Done** ✅

**Spec reference:** §11.2.1

**Description:** `oclContainer()` returns the container (parent) of an object. `oclContents()` returns the direct contents (children).

**Implementation:**
- `OclStdlib.oclContainer()` — delegates to `EObject.eContainer()`, returns null for root elements
- `OclStdlib.oclContents()` — delegates to `EObject.eContents()`, returns `OclSet` of direct children
- Tests in `OclContainerContentsTest`

---

#### S-02: `def` Expressions Runtime Support — **Done** ✅

**Spec reference:** §12.6

**Description:** Complete OCL `def:` declarations define additional properties and operations on existing types. Available during evaluation via `OclEngine.loadDocument()`.

**Implementation:**
- `OclDocumentBuilder.buildDefinition()` — extracts feature name (last IDENTIFIER), detects operation defs via `(` token, stores parameter names in synthetic `EOperation`, detects `static` keyword and sets `isStatic` flag
- `OclEngineImpl.loadDocument()` — parses document, populates `ConcurrentHashMap<DefKey, DefEntry>` for properties, registers `OclOperationProvider` for operations
- `OclEvaluator` — `lookupDefProperty()` with supertype search, `evaluateDefBody()` with nested environment. Real EAttributes take precedence over def-properties.
- `DefRegistry.java` — `DefKey` and `DefEntry` records (DefEntry carries `isStatic` flag)
- `Constraint.isStatic` — metamodel attribute (OCL v2.4 §12.12.6), `static def:` syntax in grammar
- `OclDefExpressionTest` — 17 tests (properties, operations, chaining, precedence, multiple contexts, static def parsing + evaluation)

---

#### S-04: `oclLocale` Attribute — **Done** ✅

**Spec reference:** §11.2.1

**Description:** `oclLocale` is a property of `OclAny` controlling locale-sensitive String operations (`toUpperCase()`, `toLowerCase()`). Default `"en_us"`, overridable via `let`.

**Implementation:**
- `OclEvalEnvironment.root()` — default binding `oclLocale = "en_us"`
- `OclStdlib.dispatch()` — accepts `Locale` parameter, passes to `dispatchString()`
- `OclStdlib.dispatchString()` — `toUpperCase(locale)`/`toLowerCase(locale)`
- `OclEvaluator.resolveOclLocale()` — reads `oclLocale` from environment, parses to `java.util.Locale`
- `OclEvaluator.casePropertyCallExp()` — handles `self.oclLocale` property access
- `OclLocaleTest` — 11 tests (default value, self.oclLocale, let override, Turkish i/I, German ß, short forms)

---

#### S-09: `oclAsType()` on Collections — **Done** ✅

**Spec reference:** §11.7

**Description:** `oclAsType()` on collections checks type conformance (collection kind + element type). Returns `OclInvalid` if kind or element type doesn't conform.

**Implementation:**
- `OclStdlib.oclAsType()` delegates to `oclAsTypeCollection()` for `CollectionType` targets
- `collectionKindConforms()` checks runtime collection kind matches target (COLLECTION accepts any)
- `allElementsConform()` checks all elements conform to target element type (PrimitiveType, ClassifierType, AnyType)
- 21 tests in `OclAsTypeCollectionTest`: kind matching, Collection(T) supertype, wrong kind → OclInvalid, element type conformance, classifier elements, non-collection source, empty collections
- Files changed: `OclStdlib.java`, `OclAsTypeCollectionTest.java` (new)

---

### Open Gaps — ordered by implementation priority

#### S-10: `validate()` Type-Checker — MEDIUM (= GAP-3) — Deferred to Phase 5 (LSP)

**Spec reference:** §9 (concrete syntax / well-formedness rules)

**Description:** The `OclEngine.validate()` method should perform static type-checking on parsed OCL expressions, verifying:
- Property/operation names exist on the source type
- Argument types match parameter types
- Iterator body types are compatible
- Collection operations applied to correct collection kinds

**Current state:** `validate()` is a no-op stub. The parser does basic name resolution but doesn't enforce all static type rules.

**Eclipse OCL:** Full type-checker in the Pivot layer.

**Decision:** Deferred to Phase 5 (Language Server). Der Type-Checker bringt erst im LSP echten Mehrwert (Echtzeit-Feedback, Diagnostics, Quick-Fixes). Ohne LSP wird die Engine korrekt evaluiert — fehlende statische Prüfung führt nur zu Laufzeitfehlern statt Kompilierfehlern.

**Effort:** Large (1–2 weeks). Touches all expression types.

---

#### S-06: `oclIsInState()` — LOW (Deferred, UML-only)

**Spec reference:** §7.5.10, §11.2.1

**Description:** Checks if an object is in a particular UML state. Requires `StateExp` AST node and UML state machine navigation.

**Current state:** `StateExp` AST node present in metamodel. Evaluation deferred to `ocl.uml` bundle (Phase 5).

**Eclipse OCL:** Implemented via Pivot layer (UML metamodel). Not usable with plain Ecore.

**Decision:** AST node in core metamodel; runtime evaluation in `ocl.uml` bundle. Ecore has no state machine concept.

---

#### S-07: Association Class Navigation — LOW (Deferred, UML-only)

**Spec reference:** §7.5.4

**Description:** In UML, association classes can be navigated to using the association class name. Produces an `AssociationClassCallExp`.

**Current state:** `AssociationClassCallExp` and `NavigationCallExp` AST nodes present in metamodel. Evaluation deferred to `ocl.uml` bundle (Phase 5).

**Eclipse OCL:** Implemented via Pivot layer (UML metamodel).

**Decision:** AST nodes in core metamodel; runtime evaluation in `ocl.uml` bundle. Ecore has no association classes.

---

#### S-08: Qualified Association Navigation — LOW (Deferred, UML-only)

**Spec reference:** §7.5.4

**Description:** In UML, qualified associations allow navigation with qualifier values to restrict the target set.

**Current state:** `NavigationCallExp.qualifiers` reference available in metamodel. Evaluation deferred to `ocl.uml` bundle (Phase 5).

**Eclipse OCL:** Implemented via Pivot layer.

**Decision:** AST support in core metamodel (via `NavigationCallExp.qualifiers`); runtime evaluation in `ocl.uml` bundle. Ecore has no qualifiers.

---

#### S-03: OclMessage / `^^` Operator — DEFERRED

**Spec reference:** §7.6.4, §11.10

**Description:** OclMessage represents the result of sending a message to an object (via `^^` for synchronous, `^` for asynchronous). Includes `hasReturned()`, `result()`, `isSignalSent()`, `isOperationCall()`.

**Current state:** Not parseable (no grammar rule), no AST nodes (`MessageExp`, `OclMessageArg`), no evaluator case, no `MessageType` in metamodel.

**Eclipse OCL:** Also **not implemented** for Ecore models. Only relevant for UML behavioral modeling.

**Decision:** Defer indefinitely. Same as Eclipse. Only implement if UML model support is added.

---

#### S-05: `UnspecifiedValueExp` — DEFERRED (tied to S-03)

**Spec reference:** §10.3.1

**Description:** Represents `?` in constraints, denoting an unspecified value of a given type. Used in message expressions (`obj^^op(?)` meaning "any value").

**Current state:** No AST node, no evaluator case.

**Eclipse OCL:** Also **not implemented**.

**Decision:** Defer. Tied to OclMessage (S-03). Only implement if OclMessage is implemented.

---

## Appendix: Implementation Statistics

| Metric | Value |
|--------|-------|
| Tests | 4202 (0 failures) |
| Metamodel classifiers | 53 |
| Stdlib operations | 195 / 201 (97%) |
| Iterators | 12 / 12 (100%) |
| Literal types | 10 / 10 (100%) |
| Expression types | 17 / 18 (94%) — missing: OclMessageArg |
| Safe navigation (v2.5) | ✅ |
| Three-valued logic | ✅ |
| @pre / postconditions | ✅ |
| Complete OCL documents | ✅ |
| Maps (v2.5) | ✅ |

---

*Last updated 2026-02-25 — based on systematic analysis of OCL v2.4 spec (formal/2014-02-03) chapters 7–13 against Fennec OCL implementation.*
