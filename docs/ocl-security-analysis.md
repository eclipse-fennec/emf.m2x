# OCL Engine — Security Analysis & Hardening Guide

Security analysis of the Fennec OCL Engine with attack vector assessment, mitigation planning, and references to BSI guidelines.

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

The OCL Engine operates as an **embedded Java library** within a host application. It processes:

- **OCL expressions** (text format) — parsed by the ANTLR4 parser
- **EMF models** (in-memory EObject graphs) — provided via `OclContext`
- **Complete OCL documents** — additional constraints and operations loaded from `.ocl` files
- **Custom operation providers** — Java implementations registered by the host application

OCL is a **pure query language** — it has no side effects, no I/O, no file access, and no network access. The primary threat is **denial-of-service via resource exhaustion**.

### 1.2 Trust Boundaries

```
┌─────────────────────────────────────────────────┐
│  Host Application (trusted)                     │
│  ┌─────────────────────────────────────────┐    │
│  │  OCL Engine                             │    │
│  │  ┌──────────────┐  ┌────────────────┐   │    │
│  │  │ Parser       │  │ Evaluator      │   │    │
│  │  │ (ANTLR4)     │  │ (Switch-based) │   │    │
│  │  └──────────────┘  └───────┬────────┘   │    │
│  │                            │             │    │
│  │  ┌─────────────────────────┼──────────┐  │    │
│  │  │ Trust Boundary          │          │  │    │
│  │  │  ┌──────────────────┐  ┌┴────────┐ │  │    │
│  │  │  │ Custom Operation │  │ OCL Expr │ │  │    │
│  │  │  │ Providers (Java) │  │ (user)   │ │  │    │
│  │  │  └──────────────────┘  └──────────┘ │  │    │
│  │  └────────────────────────────────────┘  │    │
│  └─────────────────────────────────────────┘    │
│                                                  │
│  EMF Models  │  Complete OCL Docs  │  Delegates  │
└─────────────────────────────────────────────────┘
```

### 1.3 Actors

| Actor | Trust Level | Description |
|-------|:-----------:|-------------|
| Host application | High | Creates engine, provides models and evaluation context |
| OCL expression author | Variable | Writes OCL expressions; may be internal or external |
| Complete OCL document author | Variable | Provides additional constraints and operations |
| Custom operation provider | Variable | Implements Java operations; extends engine capabilities |
| Model provider | Variable | Provides EMF models and model extents |

**Key question:** If the expression author or model provider is **untrusted** — what attacks are possible?

---

## 2. Attack Vectors

### S-1: ReDoS — Crafted Regex Patterns

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | `'input'.matches('(a+)+$')` — backtracking regex via `matches()`, `replaceAll()`, `replaceFirst()` |
| **Impact** | CPU exhaustion, thread blocks indefinitely |
| **Prerequisite** | Attacker controls OCL expression |
| **File** | `OclOperationSupport.java` — String operations delegation to `java.util.regex` |

**Analysis:** Java's `Pattern.compile()` uses a backtracking NFA engine. Patterns like `(a+)+$` exhibit exponential backtracking on crafted input.

**BSI reference:** CWE-1333 (Inefficient Regular Expression Complexity)

---

### S-2: Range Explosion

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | `Sequence{1..2147483647}` — collection range literal with extreme bounds |
| **Impact** | OutOfMemoryError, Denial of Service |
| **Prerequisite** | Attacker controls OCL expression |
| **File** | `OclEvaluator.java` — `caseCollectionRange()` |

**BSI reference:** CWE-400 (Resource Exhaustion)

---

### S-3: Cartesian Product Explosion

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | `Seq{1..1000}->product(Seq{1..1000})` — quadratic result size |
| **Impact** | OutOfMemoryError, Denial of Service |
| **Prerequisite** | Attacker controls OCL expression |
| **File** | `OclCollectionOperations.java` — `product()` |

**BSI reference:** CWE-400 (Resource Exhaustion)

---

### S-4: Unbounded Closure Traversal

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | `Seq{1}->closure(i \| i+1)` — generates infinite non-repeating values |
| **Impact** | CPU exhaustion, unbounded memory growth |
| **Prerequisite** | Attacker controls OCL expression |
| **File** | `OclEvaluator.java` — `iteratorClosure()` |

**BSI reference:** CWE-835 (Loop with Unreachable Exit Condition)

---

### S-5: Unsafe Type Casts in Stdlib

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | Type mismatches in stdlib operations could throw `ClassCastException` |
| **Impact** | Unexpected engine termination |
| **Prerequisite** | Attacker controls OCL expression |
| **File** | `OclOperationSupport.java` — various operations |

**Analysis:** All stdlib operations use pattern matching (`instanceof`) and safe casts. Type mismatches produce `OclInvalid` instead of exceptions.

---

### S-6: EMF Delegate Injection

| | |
|---|---|
| **Severity** | LOW |
| **Vector** | Malicious EAnnotations with delegate URI trigger OCL evaluation on model load |
| **Impact** | Uncontrolled OCL evaluation (invariants, derived features) |
| **Prerequisite** | Attacker controls EMF model with delegate annotations |
| **File** | `OclDelegateFactory.java` |

**Analysis:** EMF delegates are an explicit opt-in (`engine.installDelegates()`). The host application decides whether to enable delegate support. Once enabled, any model loaded in the same `EPackage.Registry` can trigger OCL evaluation.

**BSI reference:** CWE-94 (Code Injection)

---

### S-7: Parse Amplification

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | Repeated parsing of the same expression without caching |
| **Impact** | CPU exhaustion via repeated ANTLR4 parser invocations |
| **Prerequisite** | Attacker triggers evaluation in a hot path without caching |
| **File** | `OclEngineImpl.java` — `parse()` |

**Analysis:** The engine provides an LRU expression cache (`OclExpressionCache`) that eliminates redundant parsing.

---

### S-8: allInstances on Large Extent

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | `MyClass.allInstances()` on an extent with millions of objects |
| **Impact** | Large collection creation, memory pressure |
| **Prerequisite** | Large model extent provided by host |
| **File** | `OclEvaluator.java` — `caseOperationCallExp()` allInstances handling |

**BSI reference:** CWE-400 (Resource Exhaustion)

---

### S-9: Stack Overflow via Deep Recursion

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | Deeply nested `let...in` or chained operations: `let a=1 in let b=2 in ...` |
| **Impact** | StackOverflowError |
| **Prerequisite** | Attacker controls OCL expression |
| **File** | `OclEvaluator.java` — `eval()` |

**BSI reference:** CWE-674 (Uncontrolled Recursion)

---

### S-10: Integer Overflow

| | |
|---|---|
| **Severity** | LOW |
| **Vector** | `2147483647 + 1` — arithmetic overflow in Integer operations |
| **Impact** | Incorrect results (wraps around) |
| **Prerequisite** | Attacker controls OCL expression |

**Analysis:** Integer promotion to `Long` mitigates most cases. Accepted risk for extreme values.

---

### S-11: String Concatenation Amplification

| | |
|---|---|
| **Severity** | LOW |
| **Vector** | `'a'.concat('a').concat('a')...` — exponential string growth via chaining |
| **Impact** | Memory exhaustion |
| **Prerequisite** | Attacker controls OCL expression |

**Analysis:** Bounded by `maxDepth` — each `.concat()` is a nested expression. No dedicated string-length limit.

---

### S-12: allInstances Result Size

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | `MyClass.allInstances()` returns more elements than `maxCollectionSize` |
| **Impact** | Controlled: produces `OclInvalid` |
| **Prerequisite** | Large extent + no collection size limit |
| **File** | `OclEvaluator.java` — allInstances size check |

---

### S-13: Custom Operation Provider Abuse

| | |
|---|---|
| **Severity** | LOW |
| **Vector** | Malicious `OclOperationProvider` executes arbitrary Java code |
| **Impact** | Full JVM access (filesystem, network, processes, reflection) |
| **Prerequisite** | Attacker can register a custom operation provider |
| **File** | `OclOperationProvider.java` |

**Analysis:** Custom operation providers are a trust boundary: the host application decides which providers are registered. No sandboxing is applied. **Mitigated by D29:** Providers are disabled by default and require explicit opt-in via `customOperationsEnabled` on both `OclConfiguration` and `OclEvaluationOptions` (AND-linked). See [DR-D29](design-decisions.md#dr-d29-extension-security-controls).

**BSI reference:** CWE-94 (Code Injection)

---

### S-14: EMF Delegate URI Spoofing

| | |
|---|---|
| **Severity** | LOW |
| **Vector** | Attacker uses the Fennec OCL delegate URI in their model, triggering evaluation in an unintended context |
| **Impact** | Uncontrolled OCL evaluation |
| **Prerequisite** | Attacker controls model annotations + delegates are installed |

**Analysis:** Delegate URI (`http://www.eclipse.org/fennec/m2x/ocl/1.0`) is well-known. By design: delegates are a conscious opt-in.

---

## 3. Existing Mitigations

### 3.1 Configurable Limits (5 Limits)

| Limit | Field | Default | Protects | Status |
|-------|-------|---------|----------|--------|
| Recursion depth | `maxDepth` | 1,000 | S-9 | ✅ Implemented |
| Collection size | `maxCollectionSize` | 1,000,000 | S-2, S-3, S-8, S-12 | ✅ Implemented |
| Closure iterations | `maxClosureIterations` | 100,000 | S-4 | ✅ Implemented |
| Regex length | `maxRegexLength` | 1,000 | S-1 | ✅ Implemented |
| Evaluation timeout | `timeout` | none | S-13 (runaway eval) | ✅ Implemented |

All limits are per-evaluation, configurable via `OclEvaluationOptions`, and produce `OclInvalid` with diagnostic error on violation.

### 3.2 Design-Level Mitigations

| Mitigation | Status | Description |
|------------|--------|-------------|
| Pure query language | By design | No I/O, no side effects, no file/network access |
| No reflection | By design | No `Class.forName()`, no `Method.invoke()` |
| Expression cache | Implemented | LRU cache prevents parse amplification (S-7) |
| Safe type dispatch | Implemented | Pattern matching instead of unchecked casts (S-5) |
| Integer promotion | Implemented | `Integer` → `Long` for arithmetic (S-10) |
| Delegate opt-in | By design | `installDelegates()` is explicit (S-6, S-14) |
| Trust boundary docs | Documented | Custom providers and delegates (S-13, S-14) |
| Extension security (D29) | Implemented | Custom ops disabled by default, dual-flag opt-in (S-13) |

### 3.3 Security Test Coverage

**22 tests** in `OclSecurityHardeningTest.java`:

| Test | Vector | Verification |
|------|--------|--------------|
| `s1_matchesWithShortPattern_succeeds` | S-1 | Short regex succeeds |
| `s1_matchesWithLongPattern_returnsInvalid` | S-1 | Long regex → `OclInvalid` |
| `s1_replaceAllWithLongPattern_returnsInvalid` | S-1 | `replaceAll` regex check |
| `s1_replaceFirstWithLongPattern_returnsInvalid` | S-1 | `replaceFirst` regex check |
| `s2_smallRange_succeeds` | S-2 | Small range succeeds |
| `s2_rangeExceedingLimit_returnsInvalid` | S-2 | Large range → `OclInvalid` |
| `s3_smallProduct_succeeds` | S-3 | Small product succeeds |
| `s3_largeProduct_returnsInvalid` | S-3 | Large product → `OclInvalid` |
| `s4_unboundedClosure_returnsInvalid` | S-4 | Infinite closure → `OclInvalid` |
| `s4_cyclicClosure_terminatesNormally` | S-4 | Cyclic closure terminates via visited set |
| `s5_typeMismatch_noClassCastException` | S-5 | Division by zero → `OclInvalid` |
| `s9_deeplyNestedLet_exceedingDepthLimit_returnsInvalid` | S-9 | Deep nesting → `OclInvalid` |
| `s9_normalDepth_succeeds` | S-9 | Normal depth succeeds |
| `s12_allInstances_exceedingLimit_returnsInvalid` | S-12 | allInstances size limit enforced |
| `s13_timeout_exceedingDeadline_returnsInvalid` | S-13 | Timeout deadline enforcement |
| `s13_timeout_withinDeadline_succeeds` | S-13 | Normal evaluation within deadline |
| `strictOptions_haveCorrectDefaults` | — | Default values verification |
| `lenientOptions_haveCorrectDefaults` | — | Lenient defaults verification |
| `withMethods_createNewInstancesWithUpdatedValues` | — | Immutability verification |
| `d29_customOpsEnabled_defaultsFalseInConfig` | S-13/D29 | Config flag defaults to `false` |
| `d29_customOpsEnabled_defaultsFalseInOptions` | S-13/D29 | Options flag defaults to `false` |
| `d29_configProvider_requiresBothFlags` | S-13/D29 | Dual-flag AND-logic enforcement |

---

## 4. Mitigation Plan

### Implemented Mitigations

#### S-1: ReDoS Protection ✅ IMPLEMENTED

**Problem:** Backtracking regex via `matches()`, `replaceAll()`, `replaceFirst()`.

**Solution:** `maxRegexLength` (default: 1,000) — patterns exceeding the limit are rejected with `OclInvalid` before compilation.

**Tests:** `s1_matchesWithShortPattern_succeeds`, `s1_matchesWithLongPattern_returnsInvalid`, `s1_replaceAllWithLongPattern_returnsInvalid`, `s1_replaceFirstWithLongPattern_returnsInvalid`

---

#### S-2/S-3: Collection Size Guards ✅ IMPLEMENTED

**Problem:** Range explosion (`Sequence{1..MAX_INT}`) and Cartesian product explosion.

**Solution:** `maxCollectionSize` (default: 1,000,000) — checked before range creation and before product computation.

**Tests:** `s2_smallRange_succeeds`, `s2_rangeExceedingLimit_returnsInvalid`, `s3_smallProduct_succeeds`, `s3_largeProduct_returnsInvalid`

---

#### S-4: Closure Iteration Limit ✅ IMPLEMENTED

**Problem:** Unbounded closure traversal generating infinite non-repeating values.

**Solution:** `maxClosureIterations` (default: 100,000) — iteration counter in `iteratorClosure()`.

**Tests:** `s4_unboundedClosure_returnsInvalid`, `s4_cyclicClosure_terminatesNormally`

---

#### S-9: Recursion Depth Limit ✅ IMPLEMENTED

**Problem:** Stack overflow via deeply nested expressions.

**Solution:** `maxDepth` (default: 1,000) — depth counter incremented in `eval()`, checked before each expression evaluation.

**Tests:** `s9_deeplyNestedLet_exceedingDepthLimit_returnsInvalid`, `s9_normalDepth_succeeds`

---

#### S-12: allInstances Size Limit ✅ IMPLEMENTED

**Problem:** `allInstances()` returning millions of objects.

**Solution:** `maxCollectionSize` — result size checked after `OclModelExtent.getAllInstances()`.

**Test:** `s12_allInstances_exceedingLimit_returnsInvalid`

---

#### S-13 (Timeout): Evaluation Timeout ✅ IMPLEMENTED

**Problem:** Runaway evaluation blocks the calling thread indefinitely.

**Solution:** Deadline-based checking via `System.nanoTime()`. Deadline is set in the constructor when `options.timeout() != null` and checked at two key checkpoints:

- `eval()` — after depth check (every expression evaluation)
- `iteratorClosure()` — inside the worklist loop (every closure iteration)

**Overhead:** ~15ns per `System.nanoTime()` call, negligible compared to expression evaluation.

**Tests:** `s13_timeout_exceedingDeadline_returnsInvalid`, `s13_timeout_withinDeadline_succeeds`

---

### Remaining Mitigations (Accepted Risks)

#### S-6/S-14: EMF Delegate Trust Boundary

**Status:** Accepted risk with documentation. Delegates are an explicit opt-in via `engine.installDelegates()`. The host application controls whether delegate evaluation is enabled.

---

#### S-10: Integer Overflow

**Status:** Accepted risk. Integer promotion to `Long` covers most practical cases. Extreme-value overflow is inherent to fixed-precision arithmetic.

---

#### S-11: String Concatenation Amplification

**Status:** Partially mitigated. String concatenation chains are bounded by `maxDepth` since each `.concat()` is a nested expression. No dedicated string-length limit is needed for practical inputs.

---

#### S-13: Custom Operation Provider Sandboxing

**Status:** Addressed by D29 (Extension Security Controls). Custom operations are disabled by default in both `OclConfiguration` and `OclEvaluationOptions` (AND-linked). Must be explicitly enabled. See [DR-D29](design-decisions.md#dr-d29-extension-security-controls).

---

## 5. BSI TR-03185 Mapping

Mapping of identified risks and mitigations to the requirement areas of [BSI TR-03185](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/Technische-Richtlinien/TR-nach-Thema-sortiert/tr03185/tr-03185.html) (Secure Software Lifecycle):

### 5.1 Requirement Area: Secure Design (§4.3)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Threat modeling | Trust boundary diagram (§1.2), actor analysis (§1.3), 14 vectors | Present |
| Attack surface minimization | Pure query language — no I/O, no reflection, no dynamic loading | By design |
| Defense in depth | 5 configurable limits + timeout + expression cache + safe casts | Implemented |
| Secure defaults | `OclEvaluationOptions.strict()` with conservative limits | Implemented |

### 5.2 Requirement Area: Input Validation (§4.5)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Parser input validation | ANTLR4-based, structured errors with line/column | Implemented |
| Regex pattern validation | `maxRegexLength` before `Pattern.compile()` | Implemented |
| Expression caching | LRU cache prevents repeated parse overhead | Implemented |

### 5.3 Requirement Area: Availability (§4.6)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Recursion depth limit | `maxDepth` (default: 1,000) in `eval()` | ✅ Implemented |
| Collection size limit | `maxCollectionSize` (default: 1,000,000) for ranges, products, allInstances | ✅ Implemented |
| Closure iteration limit | `maxClosureIterations` (default: 100,000) in `iteratorClosure()` | ✅ Implemented |
| Regex length limit | `maxRegexLength` (default: 1,000) | ✅ Implemented |
| Timeout enforcement | Deadline-based `System.nanoTime()` check at eval + closure | ✅ Implemented |

### 5.4 Requirement Area: Third-Party Components (§4.7)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Custom operation provider audit | D29: disabled by default, dual-flag opt-in | ✅ Implemented |
| Delegate security | Explicit opt-in via `installDelegates()` | By design |

### 5.5 Requirement Area: Testing (§4.8)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Security tests | 22 tests in `OclSecurityHardeningTest` | ✅ Implemented |
| Limit enforcement tests | All 5 limits + timeout tested | ✅ Implemented |
| Penetration testing | Not performed | Pending |

### 5.6 Further Relevant BSI Guidelines

| Guideline | Relevance | Reference |
|-----------|-----------|-----------|
| [BSI TR-03185](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/Technische-Richtlinien/TR-nach-Thema-sortiert/tr03185/tr-03185.html) | High | Secure software lifecycle — primary guideline |
| [BSI TR-03185-2](https://www.bsi.bund.de/DE/Service-Navi/Presse/Alle-Meldungen-News/Meldungen/TR-03185-2_OSS_251103.html) | High | Part 2 for open-source software (EPL-2.0 license) |
| [BSI IT-Grundschutz](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/IT-Grundschutz/it-grundschutz_node.html) | Medium | Foundation for TR-03185; APP.6 General Software |

---

## 6. Embedder Guide

Recommendations for developers embedding the OCL Engine in their applications.

### 6.1 Sandboxed Evaluation (MUST for untrusted input)

```java
import java.time.Duration;

// 1. Configure conservative limits
OclEvaluationOptions sandboxed = OclEvaluationOptions.strict()
    .withMaxDepth(100)                   // Tightened from 1000
    .withMaxCollectionSize(10_000)       // Tightened from 1_000_000
    .withMaxClosureIterations(1_000)     // Tightened from 100_000
    .withMaxRegexLength(200)             // Tightened from 1000
    .withTimeout(Duration.ofSeconds(5)); // Hard deadline

// 2. Parse and evaluate
OclExpression expr = engine.parse(untrustedInput, contextClass);
OclResult result = engine.evaluateWithDiagnostics(expr, context, sandboxed);

// 3. Check result
if (result.value() == OclInvalid.INSTANCE) {
    // Expression violated a limit — check diagnostics
    result.diagnostics().forEach(d ->
        logger.warn("OCL limit violation: {}", d.getMessage()));
}
```

### 6.2 EMF Delegate Security

When enabling OCL delegates for derived features and invariants:

```java
// Configure delegate options BEFORE installing delegates
OclEvaluationOptions delegateOpts = OclEvaluationOptions.strict()
    .withMaxDepth(200)
    .withMaxCollectionSize(50_000)
    .withTimeout(Duration.ofSeconds(10));

engine.setDelegateOptions(delegateOpts);
engine.installDelegates();

// WARNING: After installDelegates(), any model loaded in the
// EPackage.Registry can trigger OCL evaluation via EAnnotations.
// Only install delegates if you trust the models being loaded.
```

### 6.3 Custom Operation Provider Guidance (D29)

Custom operations are **disabled by default** (D29). Both the engine configuration AND the
per-evaluation options must explicitly enable them:

```java
// 1. Register providers in configuration AND enable
OclConfiguration config = OclConfiguration.builder(parserSupport)
    .addOperationProvider(new MyAuditedProvider())  // internally reviewed
    .customOperationsEnabled(true)                  // engine-wide enable
    .build();

OclEngine engine = new OclEngineImpl(config);

// 2. Enable per-evaluation (AND-linked with config flag)
OclEvaluationOptions opts = OclEvaluationOptions.strict()
    .withCustomOperationsEnabled(true);             // per-evaluation enable

// 3. Both flags must be true for custom operations to be active
// Config false → hard disable, Options cannot override (Defense in Depth)
// Config true, Options false → sandboxed evaluation (e.g. for user input)
// Config true, Options true → custom operations fully active
```

**Per-evaluation providers** (via `additionalProviders`) bypass the enable flags and are always
active. This is used internally by the QVT-O↔OCL bridge and should not be used for untrusted
providers.

```java
// DO NOT: register user-provided or dynamically loaded providers
// config.addOperationProvider(untrustedProvider);  // RISK: full JVM access

// Custom providers have access to:
// - The context object (self)
// - All arguments
// - No sandboxing is applied
```

### 6.4 Complete OCL Document Security

When loading Complete OCL documents from external sources:

```java
// 1. Validate the document source
Path oclDoc = trustedBasePath.resolve(userPath).normalize();
if (!oclDoc.startsWith(trustedBasePath)) {
    throw new SecurityException("Path traversal detected");
}

// 2. Load with tightened options
engine.loadCompleteOclDocument(oclDoc.toUri());

// 3. All expressions in the document will be evaluated with
//    the configured delegate options (see §6.2)
```

---

## 7. References

### BSI Guidelines

- [BSI TR-03185 — Secure Software Lifecycle](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/Technische-Richtlinien/TR-nach-Thema-sortiert/tr03185/tr-03185.html) — Primary guideline for secure software development
- [BSI TR-03185-2 — Open Source Software](https://www.bsi.bund.de/DE/Service-Navi/Presse/Alle-Meldungen-News/Meldungen/TR-03185-2_OSS_251103.html) — Part 2 specifically for OSS (relevant for EPL-2.0)
- [BSI IT-Grundschutz](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/IT-Grundschutz/it-grundschutz_node.html) — Foundation framework

### CWE References

| CWE | Vector |
|-----|--------|
| CWE-400 (Resource Exhaustion) | S-2 Ranges, S-3 Products, S-4 Closure, S-8/S-12 allInstances |
| CWE-674 (Uncontrolled Recursion) | S-9 Stack Overflow |
| CWE-835 (Unreachable Exit Condition) | S-4 Unbounded Closure |
| CWE-1333 (Inefficient Regex) | S-1 ReDoS |
| CWE-94 (Code Injection) | S-6 Delegates, S-13 Custom Providers |

### Internal Documents

- [OCL Architecture §10 — Security Hardening](ocl-architecture.md) — Internal architecture-level security documentation
- [OCL User Guide §14 — Security Hardening](ocl-user-guide.md) — User-facing security guidance
- [QVT-O Security Analysis](qvto-security-analysis.md) — Companion analysis for the QVT-O layer
- [Design Decisions](design-decisions.md) — Architecture decisions D1–D30 (D29: Extension Security Controls)
