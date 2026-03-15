# QVT-R Engine — Security Analysis & Hardening Guide

Security analysis of the Fennec QVT-R Engine with attack vector assessment, mitigation planning, and references to BSI guidelines.

## Table of Contents

1. [Threat Model](#1-threat-model)
2. [Attack Vectors](#2-attack-vectors)
3. [Existing Mitigations](#3-existing-mitigations)
4. [Mitigation Plan](#4-mitigation-plan)
5. [BSI TR-03185 Mapping](#5-bsi-tr-03185-mapping)
6. [Embedder Guide](#6-embedder-guide)
7. [References](#7-references)

---

## 1. Threat Model

### 1.1 System Context

The QVT-R Engine operates as an **embedded Java library** within a host application. It processes:

- **Transformation source code** (QVT-R text format) — parsed by the ANTLR4 parser
- **EMF models** (in-memory EObject graphs) — provided by the host application via typed model extents
- **Blackbox libraries** — Java implementations registered via `QvtdBlackboxRegistry`
- **Relation implementation providers** — QVT-O engines providing `where`-clause implementations for hybrid QVT-R↔QVT-O

### 1.2 Trust Boundaries

```
┌─────────────────────────────────────────────────┐
│  Host Application (trusted)                     │
│  ┌─────────────────────────────────────────┐    │
│  │  QVT-R Engine                           │    │
│  │  ┌──────────────┐  ┌────────────────┐   │    │
│  │  │ Parser       │  │ Evaluator      │   │    │
│  │  │ (ANTLR4)     │  │ (Direct Interp)│   │    │
│  │  └──────────────┘  └───────┬────────┘   │    │
│  │                            │             │    │
│  │  ┌─────────────────────────┼──────────┐  │    │
│  │  │ Trust Boundary          │          │  │    │
│  │  │  ┌──────────────┐  ┌───┴────────┐ │  │    │
│  │  │  │ Blackbox Libs │  │ QVT-R Code │ │  │    │
│  │  │  │ (Java)       │  │ (from user)│ │  │    │
│  │  │  └──────────────┘  └────────────┘ │  │    │
│  │  │  ┌──────────────────────────────┐ │  │    │
│  │  │  │ Impl. Providers (QVT-O etc.) │ │  │    │
│  │  │  └──────────────────────────────┘ │  │    │
│  │  └────────────────────────────────────┘  │    │
│  └─────────────────────────────────────────┘    │
│                                                  │
│  EMF Models  │  EPackage Registry  │  URIs       │
└─────────────────────────────────────────────────┘
```

### 1.3 Actors

| Actor | Trust Level | Description |
|-------|:-----------:|-------------|
| Host application | High | Creates engine, provides models and config |
| Transformation author | Variable | Writes QVT-R code; may be internal or external |
| Blackbox author | Variable | Implements Java libraries; extends engine capabilities |
| Model provider | Variable | Provides EMF models as input |
| Implementation provider | Variable | Registers QVT-O engines for hybrid execution |

**Key question:** If the transformation author or model provider is **untrusted** — what attacks are possible?

---

## 2. Attack Vectors

### R-1: Unbounded Recursion in Relation Calls

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | Mutually recursive where-clauses without depth limit |
| **Impact** | `StackOverflowError`, Denial of Service |
| **Prerequisite** | Attacker provides malicious transformation |
| **File** | `QvtrEvaluator.java:717` (`invokeNonTopRelation`), `:748` (`executeRelationWithBindings`), `:682` (`evaluateWhereClause`) |

**Analysis:** `invokeNonTopRelation()` → `executeRelationWithBindings()` → `evaluateWhereClause()` → `invokeNonTopRelation()`. This recursive chain has no depth limit. A transformation with mutually recursive where-clauses (e.g., relation A's where calls relation B, whose where calls relation A with different bindings) will cause a `StackOverflowError`.

**BSI reference:** BSI TR-03185 §4.6 (Availability), CWE-674 (Uncontrolled Recursion)

---

### R-2: Combinatorial Explosion in Pattern Matching (Cross-Product)

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | Cross-product of binding sets from multiple domain patterns |
| **Impact** | OutOfMemoryError, CPU exhaustion, Denial of Service |
| **Prerequisite** | Malicious transformation or large model with broad pattern matches |
| **File** | `QvtrPatternMatcher.java:172` (`matchObjectTemplateAll`), `:387-444` (cross-product loop) |

**Analysis:** The pattern matcher computes cross-products of binding sets. In `matchObjectTemplateAll()`, each `PropertyTemplateItem` multiplies the number of binding sets by the number of matches. With N properties each matching M elements, the result is M^N binding sets. `matchSourceDomains()` in `QvtrEvaluator.java:326-341` further compounds this by threading binding lists across multiple domains.

**BSI reference:** BSI TR-03185 §4.6 (Availability), CWE-400 (Resource Exhaustion)

---

### R-3: Unbounded Model Traversal in `collectInstances()`

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | Recursive EMF containment tree traversal without depth or size limit |
| **Impact** | `StackOverflowError`, excessive CPU on deeply nested models |
| **Prerequisite** | Large or deeply nested input model |
| **File** | `QvtrExtentManager.java:105` (`collectInstances`) |

**Analysis:** `collectInstances()` recurses through the entire EMF containment tree without depth or size limit. A deeply nested model could cause `StackOverflowError`. The method is called for every domain match, compounding the cost. Results are not cached per `(TypedModel, EClass)` pair.

**BSI reference:** BSI TR-03185 §4.6 (Availability), CWE-400 (Resource Exhaustion)

---

### R-4: Blackbox Libraries — Arbitrary Java Code Execution

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | Registered blackbox library executes arbitrary Java code |
| **Impact** | Full JVM access (filesystem, network, processes, reflection) |
| **Prerequisite** | Attacker can register a blackbox library or `blackboxEnabled = true` without allow-list |
| **File** | `QvtrBlackboxBridge.java:127,165` (`library.invoke`) |

**Analysis:** Blackbox invocations execute arbitrary code from registered libraries without sandboxing. The operation name comes from the transformation model (user-controlled input). Additional issues:
- Exceptions from blackbox invocations are silently swallowed (catch blocks return `null`)
- Return values from blackbox code are used directly without type validation
- Blackbox code runs with the same privileges as the engine (full JVM access)

**BSI reference:** BSI TR-03185 §4.3 (Secure Architecture), CWE-94 (Code Injection)

---

### R-5: Filesystem Access via `parse(URI)`

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | `engine.parse(URI)` reads arbitrary files via `Files.readString(Path.of(uri))` |
| **Impact** | Reading arbitrary files on the filesystem |
| **Prerequisite** | Attacker controls the URI passed to `parse()` |
| **File** | `QvtdEngineImpl.java:82-89` |

**Analysis:** No path whitelisting or canonicalization. Path traversal possible:
```java
engine.parse(URI.createFileURI("/etc/passwd"));  // reads /etc/passwd as "QVT-R code"
```

**BSI reference:** BSI TR-03185 §4.5 (Input Validation), CWE-22 (Path Traversal)

---

### R-6: No Input Source Size Limit

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | Arbitrarily large `.qvtr` file read into memory |
| **Impact** | OutOfMemoryError |
| **Prerequisite** | Attacker controls the URI passed to `parse()` |
| **File** | `QvtdEngineImpl.java:89` (`Files.readString`) |

**Analysis:** Neither `QvtdEngineImpl.parse(URI)` nor the parser enforces a maximum source size. A multi-gigabyte `.qvtr` file would cause `OutOfMemoryError`. The OCL layer has `maxCollectionSize`, `maxRegexLength` limits, but there is no `maxSourceSize` at the QVT-R parser level.

**BSI reference:** BSI TR-03185 §4.6 (Availability), CWE-400 (Resource Exhaustion)

---

### R-7: No Cancellation / Timeout Mechanism

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | Transformation over a large model blocks the calling thread indefinitely |
| **Impact** | Thread starvation, Denial of Service |
| **Prerequisite** | Malicious transformation or large model |
| **File** | `QvtrEvaluator.java:120-155` (`execute()`) |

**Analysis:** The `execute()` method iterates over all top-level relations and their source bindings with no mechanism for cancellation or timeout. Unlike QVT-O (which has `timeout` + deadline enforcement at 7 checkpoints), the QVT-R evaluator has no timeout support.

**BSI reference:** BSI TR-03185 §4.6 (Availability), CWE-400 (Resource Exhaustion)

---

### R-8: Silent Trace Record Dropping at Capacity Limit

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | Trace records silently dropped when `TRACE_MAX_PER_RELATION` (100,000) exceeded |
| **Impact** | Incorrect transformation results (silent data loss) |
| **Prerequisite** | Large-scale transformation with many relation invocations |
| **File** | `QvtrTraceManager.java:84-86` |

**Analysis:** When the max is exceeded, new trace records are silently dropped (`return` at line 85). The transformation continues executing but produces incorrect results because subsequent when-clause trace lookups will fail to find records that should exist. A warning is logged at 10,000 (threshold) but no notification at all at 100,000 (hard limit).

**BSI reference:** BSI TR-03185 §4.3 (Secure Design — Fail-Safe Defaults), CWE-221 (Information Loss)

---

### R-9: Thread Safety — Implementation Provider List Race Condition

| | |
|---|---|
| **Severity** | LOW |
| **Vector** | Concurrent `registerImplementationProvider()` + `execute()` causes data race |
| **Impact** | `ConcurrentModificationException` or corrupted state |
| **Prerequisite** | Multi-threaded usage of the engine |
| **File** | `QvtdEngineImpl.java:64` (field), `:132-134` (`registerImplementationProvider`) |

**Analysis:** The class Javadoc (line 53) claims "the engine instance is safely shared across threads", but `implementationProviders` is a plain `ArrayList`. `registerImplementationProvider()` mutates it without synchronization, while `execute()` reads it via `List.copyOf()`. Concurrent calls create a data race.

**BSI reference:** CWE-362 (Race Condition)

---

### R-10: Information Disclosure in Error Messages

| | |
|---|---|
| **Severity** | LOW |
| **Vector** | Error messages expose internal file paths, available model extents, exception details |
| **Impact** | Leaks configuration internals to callers |
| **Prerequisite** | Engine used in multi-tenant or LSP deployment |
| **File** | `QvtrExtentManager.java:73`, `QvtdEngineImpl.java:122-128` |

**Analysis:** Error messages include:
- Full URIs (`"Only file URIs are supported: " + transformationUri`)
- Available model extent names (`"Available: %s"`)
- Raw exception messages (`"Execution error: " + e.getMessage()`)

In a service-oriented deployment, this leaks internal state.

**BSI reference:** BSI TR-03185 §4.4 (Secret Protection), CWE-200 (Information Exposure)

---

### R-11: No ANTLR Parse Tree Depth Limit

| | |
|---|---|
| **Severity** | LOW |
| **Vector** | Deeply nested expressions cause `StackOverflowError` during parsing/AST construction |
| **Impact** | `StackOverflowError`, Denial of Service |
| **Prerequisite** | Attacker provides malicious transformation source |
| **File** | `QvtrExpressionBuilder.java` (recursive visitor calls) |

**Analysis:** ANTLR4's default parser has no built-in nesting depth limit. Deeply nested expressions (e.g., `(((((...))))`) cause stack overflow during both ANTLR parsing and AST construction. The OCL engine has `maxDepth=1000` for evaluation, but no equivalent at parse time.

**BSI reference:** BSI TR-03185 §4.5 (Input Validation), CWE-674 (Uncontrolled Recursion)

---

### R-12: Global EPackage Registry Shared State

| | |
|---|---|
| **Severity** | LOW |
| **Vector** | `parse(String, String)` uses global `EPackage.Registry.INSTANCE` |
| **Impact** | Cross-tenant metamodel leakage in multi-tenant environments |
| **Prerequisite** | OSGi or multi-tenant deployment |
| **File** | `QvtdEngineImpl.java:100` |

**Analysis:** In a multi-tenant environment, one tenant's registered packages are visible to another tenant's parsing operations. A malicious package registration could cause the parser to resolve types from an attacker-controlled metamodel.

**BSI reference:** CWE-668 (Exposure of Resource to Wrong Sphere)

---

## 3. Existing Mitigations

### 3.1 Inherited from OCL (14 Vectors)

The QVT-R Engine inherits all OCL mitigations (documented in `ocl-architecture.md` §10) via composition with `OclEvaluator`:

| ID | Vector | Status | Implementation |
|----|--------|--------|----------------|
| S-1 | ReDoS | Mitigated | `maxRegexLength` (default: 1000) |
| S-2 | Range explosion | Mitigated | `maxCollectionSize` check before range creation |
| S-3 | Product explosion | Mitigated | Product size checked before computation |
| S-4 | Unbounded closure | Mitigated | `maxClosureIterations` (default: 100,000) |
| S-5 | Unsafe casts | Mitigated | Type-safe dispatch logic |
| S-6 | Delegate injection | Documented | Trust boundary |
| S-7 | Parse amplification | Mitigated | LRU expression cache |
| S-8 | allInstances large extent | Mitigated | `maxCollectionSize` check |
| S-9 | Stack overflow | Mitigated | `maxDepth` (default: 1000) |
| S-10 | Integer overflow | Accepted | Low risk |
| S-11 | String concat amplification | Partial | Bounded by `maxDepth` |
| S-12 | allInstances result size | Mitigated | `maxCollectionSize` check |
| S-13 | Custom op provider abuse | Mitigated (D29) | Disabled by default, dual-flag opt-in |
| S-14 | Delegate URI spoofing | Documented | By design |

### 3.2 QVT-R Specific

| Mitigation | Implementation | Status |
|------------|----------------|--------|
| Relation call depth limit | `maxRelationDepth` (default: 200) in `QvtdConfiguration` | ✅ Implemented (M-R2) |
| Binding set limit | `maxBindings` (default: 10,000) in `QvtdConfiguration` | ✅ Implemented (M-R3) |
| Timeout enforcement | `timeoutMs` deadline check at 4 checkpoints | ✅ Implemented (M-R4) |
| Trace records warn threshold | `TRACE_WARN_THRESHOLD = 10,000` in `QvtrTraceManager` | Implemented |
| Trace records hard limit | `TRACE_MAX_PER_RELATION = 100,000` in `QvtrTraceManager` | Implemented (silent drop — R-8) |
| Blackbox config flags | `blackboxEnabled` + `allowedBlackboxModules` in `QvtdConfiguration` | Implemented (enforcement gap — R-4) |
| No reflection | No `Class.forName()`, no `Method.invoke()` in engine | By design |
| No dynamic loading | Only EClasses from registered metamodels can be instantiated | By design |
| Relation overrides validation | `@overrides` annotation resolved and validated in `resolveOverrides()` | Implemented |
| Binding validation | `QvtrBindingValidator` performs post-parse static analysis (§7.5) | Implemented |

### 3.3 Security Test Coverage

**OCL:** 19 dedicated security tests in `OclSecurityHardeningTest.java`

**QVT-R:** `QvtdSecurityHardeningTest` — 9 tests covering R-1 depth limit, R-2 binding limit, R-4 blackbox (disabled + allow-list), R-7 timeout, R-8 trace limit, config defaults (M-R7 ✅).

---

## 4. Mitigation Plan

### Implemented Mitigations

#### M-R1: Trace Record Limits ✅ IMPLEMENTED (Partial)

**Problem:** R-8 — Unbounded trace model growth.

**Solution:** `TRACE_WARN_THRESHOLD = 10,000` logs a warning. `TRACE_MAX_PER_RELATION = 100,000` drops new records.

Trace limit is now configurable via `QvtdConfiguration.maxTraceRecords()` (default: 100,000). WARNING logged when hard limit reached (M-R8 ✅).

---

### Remaining Mitigations

#### M-R2: Relation Call Depth Limit ✅ IMPLEMENTED

**Problem:** R-1 — Unbounded recursion in relation calls.

**Solution:** Configurable `maxRelationDepth` (default: 200) in `QvtdConfiguration`. Depth counter in `executeRelationWithBindings()`, incremented on entry, decremented in finally-block. Throws `QvtdExecutionException` if the limit is exceeded.

**Files:** `QvtrEvaluator.java` (`executeRelationWithBindings()`), `QvtdConfiguration.java`

---

#### M-R3: Pattern Matching Binding Limit ✅ IMPLEMENTED

**Problem:** R-2 — Combinatorial explosion in binding sets.

**Solution:** Configurable `maxBindings` (default: 10,000) in `QvtdConfiguration`. Checked at 3 checkpoints:
1. `QvtrPatternMatcher.matchObjectTemplateAll()` — cross-product within nested templates
2. `QvtrPatternMatcher.matchCollectionTemplate()` — cross-product in collection matching
3. `QvtrEvaluator.matchSourceDomains()` — cross-product across multiple source domains

Throws `QvtdExecutionException` if exceeded. Tested in `QvtdSecurityHardeningTest.r2_crossProduct_exceedingBindingLimit_terminates()`.

**Files:** `QvtrPatternMatcher.java`, `QvtrEvaluator.java` (`matchSourceDomains()`), `QvtdConfiguration.java`

---

#### M-R4: Timeout / Cancellation ✅ IMPLEMENTED

**Problem:** R-7 — No way to cancel or timeout a running transformation.

**Solution:** Configurable `timeoutMs` (default: 0 = disabled) in `QvtdConfiguration`. Deadline-based checking via `System.nanoTime()` at 4 checkpoints:
- Top-level `executeRelation()` entry
- Source bindings iteration loop
- `executeRelationWithBindings()` entry (non-top relations)
- (future: source domain matching)

Throws `QvtdExecutionException` if deadline exceeded.

**Files:** `QvtrEvaluator.java` (`checkDeadline()`), `QvtdConfiguration.java`

---

#### M-R5: Blackbox Enforcement ✅ IMPLEMENTED

**Problem:** R-4 — `blackboxEnabled` and `allowedBlackboxModules` defined in `QvtdConfiguration` but not enforced in evaluator.

**Priority:** MEDIUM → **Done**

**Implementation:**
1. Added `config.blackboxEnabled()` gate in `QvtrBlackboxBridge.invokeImplementedBy()` and `evaluateBlackboxQuery()`
2. Added `isBlackboxAllowed()` helper checking `allowedBlackboxModules` allow-list
3. Blackbox disabled by default — existing tests updated to opt-in with `.blackboxEnabled(true)`

**Affected files:** `QvtrBlackboxBridge.java`

---

#### M-R6: Thread Safety Fix ✅ IMPLEMENTED

**Problem:** R-9 — `implementationProviders` ArrayList race condition.

**Priority:** MEDIUM → **Done**

**Implementation:** Replaced `ArrayList` with `CopyOnWriteArrayList` for `implementationProviders`.

**Affected files:** `QvtdEngineImpl.java`

---

#### M-R7: QVT-R Security Tests ✅ IMPLEMENTED

**Problem:** No dedicated security hardening tests for QVT-R.

**Priority:** MEDIUM → **Done**

**Implementation:** Created `QvtdSecurityHardeningTest.java` with 9 tests:

| Test | Vector | Mitigation |
|------|--------|------------|
| `r1_recursiveRelation_exceedingDepth_terminates` | R-1 | M-R2 |
| `r2_crossProduct_exceedingBindingLimit_terminates` | R-2 | M-R3 |
| `r7_timeout_exceedingDeadline_terminates` | R-7 | M-R4 |
| `r7_timeout_withinDeadline_succeeds` | R-7 | M-R4 |
| `r4_blackboxDisabled_byDefault` | R-4 | M-R5 |
| `r4_blackboxAllowList_blocksUnlisted` | R-4 | M-R5 |
| `r8_traceLimit_dropsRecordsAtLimit` | R-8 | M-R8 |
| `config_defaults_haveCorrectValues` | — | — |
| `config_builderMethods_setValues` | — | — |

**Affected files:** `QvtdSecurityHardeningTest.java`

---

#### M-R8: Trace Record Dropping — Fail-Loud ✅ IMPLEMENTED

**Problem:** R-8 — Silent data loss when trace records are dropped.

**Priority:** MEDIUM → **Done**

**Implementation:**
1. Made trace limit configurable via `QvtdConfiguration.maxTraceRecords()` (default: 100,000)
2. `QvtrTraceManager` constructor now takes `maxTraceRecords` parameter
3. WARNING logged when hard limit reached, further records dropped with clear message

**Affected files:** `QvtrTraceManager.java`, `QvtdConfiguration.java`

---

#### M-R9: URI Validation / Source Size Limit

**Problem:** R-5, R-6 — Arbitrary file access, unbounded source size.

**Priority:** LOW (Embedder responsibility)

**Status:** Documented in Embedder Guide (§6.2). Same approach as QVT-O: embedder validates URI before passing to engine.

**Optional:** Add `maxSourceSize` to `QvtdConfiguration` (default: 1 MB). Check `Files.size()` in `parse(URI)` before reading.

---

#### M-R10: Error Message Sanitization

**Problem:** R-10 — Information disclosure in error messages.

**Priority:** LOW

**Status:** Accepted risk for library usage. For LSP/service deployments, the host application should sanitize error messages before forwarding to clients.

---

## 5. BSI TR-03185 Mapping

Mapping of identified risks and mitigations to the requirement areas of [BSI TR-03185](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/Technische-Richtlinien/TR-nach-Thema-sortiert/tr03185/tr-03185.html) (Secure Software Lifecycle):

### 5.1 Requirement Area: Secure Design (§4.3)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Threat modeling | Trust boundary diagram (§1.2), actor analysis (§1.3) | Present |
| Attack surface minimization | No reflection, no dynamic loading, EMF-only types | Implemented |
| Defense in depth | OCL limits + trace limits + relation depth + binding limit + timeout | ✅ Implemented (M-R2, M-R3, M-R4) |
| Secure defaults | Blackbox disabled by default, inherited OCL strict defaults | Implemented |
| Fail-safe defaults | Trace limit configurable, WARNING logged at hard limit | ✅ M-R8 |

### 5.2 Requirement Area: Input Validation (§4.5)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Parser input validation | ANTLR4-based, structured errors with line/column | Implemented |
| URI/path validation | `parse(URI)` without canonicalization | **Gap** → M-R9 (Embedder Guide) |
| Source size validation | No `maxSourceSize` limit | **Gap** → M-R9 |
| Parse tree depth limit | No nesting depth limit in ANTLR4 parser | **Gap** (LOW — R-11) |

### 5.3 Requirement Area: Availability (§4.6)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Recursion depth limit | `maxRelationDepth` (default: 200) in `QvtdConfiguration` | ✅ Implemented (M-R2) |
| Cross-product limit | `maxBindings` (default: 10,000) in `QvtdConfiguration` | ✅ Implemented (M-R3) |
| Timeout enforcement | `timeoutMs` deadline check at 4 checkpoints | ✅ Implemented (M-R4) |
| Trace size limit | `TRACE_MAX_PER_RELATION = 100,000` | Implemented (silent drop) |
| Inherited OCL limits | `maxDepth`, `maxCollectionSize`, `maxClosureIterations`, `maxRegexLength` | Implemented |

### 5.4 Requirement Area: Third-Party Components (§4.7)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Blackbox library audit | `blackboxEnabled` + `allowedBlackboxModules` enforced in evaluator | ✅ M-R5 |
| Implementation providers | No audit/allow-list for `RelationImplementationProvider` | **Gap** (accepted — trusted host-provided) |
| Least privilege | Blackbox disabled by default | Implemented |

### 5.5 Requirement Area: Testing (§4.8)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Security tests OCL | 19 tests in `OclSecurityHardeningTest` | ✅ Implemented |
| Security tests QVT-O | 12 tests in `QvtoSecurityHardeningTest` | ✅ Implemented |
| Security tests QVT-R | `QvtdSecurityHardeningTest` (9 tests) + 26 unit tests (helpers) | ✅ M-R7 |
| Penetration testing | Not performed | Pending |

### 5.6 Further Relevant BSI Guidelines

| Guideline | Relevance | Reference |
|-----------|-----------|-----------|
| [BSI TR-03185](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/Technische-Richtlinien/TR-nach-Thema-sortiert/tr03185/tr-03185.html) | High | Secure software lifecycle — primary guideline |
| [BSI TR-03185-2](https://www.bsi.bund.de/DE/Service-Navi/Presse/Alle-Meldungen-News/Meldungen/TR-03185-2_OSS_251103.html) | High | Part 2 for open-source software (EPL-2.0 license) |
| [BSI IT-Grundschutz](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/IT-Grundschutz/it-grundschutz_node.html) | Medium | Foundation for TR-03185; APP.6 General Software |
| BSI TR-02102 | Low | Cryptographic procedures — not directly relevant (no crypto in QVT-R) |

---

## 6. Embedder Guide

Recommendations for developers embedding the QVT-R Engine in their applications.

### 6.1 Minimum Hardening (MUST)

```java
// 1. Configure OCL limits (inherited by QVT-R evaluator)
OclEvaluationOptions oclOpts = OclEvaluationOptions.strict()
    .withMaxDepth(200)                   // Tightened from 1000
    .withMaxCollectionSize(50_000)       // Tightened from 1_000_000
    .withMaxClosureIterations(10_000)    // Tightened from 100_000
    .withMaxRegexLength(200);            // Tightened from 1000

// 2. Configure QVT-R engine
OclConfiguration oclConfig = OclConfiguration.builder()
    .evaluationOptions(oclOpts)
    .build();

QvtdConfiguration config = QvtdConfiguration.builder(oclConfig)
    .maxRelationDepth(100)               // Tightened from 200 (M-R2)
    .maxBindings(5_000)                  // Tightened from 10,000 (M-R3)
    .timeoutMs(30_000)                   // 30 seconds (M-R4)
    .build();                            // blackbox disabled by default

QvtdEngine engine = new QvtdEngineImpl(config);

// 3. Parse and execute
RelationalTransformation trafo = engine.parse(source, "myTransformation");
QvtdExecutionContext ctx = QvtdExecutionContext.of(
    Map.of("uml", umlExtent, "rdbms", rdbmsExtent),
    Direction.ENFORCE
);

QvtdExecutionResult result = engine.execute(trafo, ctx);
if (!result.isSuccess()) {
    // Evaluate diagnostics, do NOT ignore
}
```

### 6.2 URI Validation (when using `parse(URI)`)

```java
public RelationalTransformation safeParse(QvtdEngine engine, URI uri, Path allowedBase)
        throws QvtdParseException {
    // Only allow file scheme
    if (!"file".equals(uri.scheme())) {
        throw new IllegalArgumentException("Only file:// URIs allowed");
    }

    // Canonicalize path and check base directory
    Path resolved = Path.of(uri).toAbsolutePath().normalize();
    if (!resolved.startsWith(allowedBase)) {
        throw new IllegalArgumentException("Path outside allowed directory");
    }

    // Optional: check file size
    if (Files.size(resolved) > 1_000_000) {
        throw new IllegalArgumentException("Source file too large");
    }

    return engine.parse(uri);
}
```

### 6.3 Blackbox Libraries

Blackbox libraries are **disabled by default**. Enable explicitly with optional allow-list:

```java
BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
registry.register(new MyAuditedLibrary());  // internally reviewed

QvtdConfiguration config = QvtdConfiguration.builder(oclConfig)
    .blackboxRegistry(registry)
    .blackboxEnabled(true)                       // required — default is false
    .allowedBlackboxModules(Set.of("mylib"))     // optional — empty = allow all
    .build();

// DO NOT: dynamically loaded or user-provided libraries
// registry.register(untrustedLibrary);  // RISK: full JVM access!
```

### 6.4 Hybrid QVT-R↔QVT-O (Implementation Providers)

Implementation providers are registered by the **host application** (trusted):

```java
QvtdEngineImpl engine = new QvtdEngineImpl(config);

// Register QVT-O engine as implementation provider
engine.registerImplementationProvider(qvtoEngine);

// NOTE: Only register trusted providers.
// Providers execute arbitrary QVT-O code with full engine privileges.
```

### 6.5 Thread-Safety Caveat

R-9 has been fixed — `implementationProviders` now uses `CopyOnWriteArrayList` (M-R6). However, for best practice:
- Register all providers before first `execute()` call, or
- Synchronize externally if registration and execution overlap

---

## 7. References

### BSI Guidelines

- [BSI TR-03185 — Secure Software Lifecycle](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/Technische-Richtlinien/TR-nach-Thema-sortiert/tr03185/tr-03185.html) — Primary guideline for secure software development
- [BSI TR-03185-2 — Open Source Software](https://www.bsi.bund.de/DE/Service-Navi/Presse/Alle-Meldungen-News/Meldungen/TR-03185-2_OSS_251103.html) — Part 2 specifically for OSS (relevant for EPL-2.0)
- [BSI IT-Grundschutz](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/IT-Grundschutz/it-grundschutz_node.html) — Foundation framework

### CWE References

| CWE | Vector |
|-----|--------|
| CWE-674 (Uncontrolled Recursion) | R-1 Relation Calls |
| CWE-400 (Resource Exhaustion) | R-2 Cross-Product, R-3 Traversal, R-6 Source Size, R-7 Timeout |
| CWE-94 (Code Injection) | R-4 Blackbox Libraries |
| CWE-22 (Path Traversal) | R-5 URI |
| CWE-221 (Information Loss) | R-8 Silent Trace Drop |
| CWE-362 (Race Condition) | R-9 Thread Safety |
| CWE-200 (Information Exposure) | R-10 Error Messages |
| CWE-668 (Exposure to Wrong Sphere) | R-12 Global Registry |

### Internal Documents

- [OCL Architecture §10 — Security Hardening](ocl-architecture.md) — OCL-specific threat analysis and tests
- [OCL Security Analysis](ocl-security-analysis.md) — Full OCL security analysis
- [QVT-O Security Analysis](qvto-security-analysis.md) — Full QVT-O security analysis
- [M2T Security Analysis](m2t-security-analysis.md) — Full M2T security analysis
- [QVT-R Architecture](qvtd-architecture.md) — Engine architecture
- [Design Decisions](design-decisions.md) — Architecture decisions (D29: Extension Security Controls, D33: Direct Interpretation)
