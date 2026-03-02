# QVT-O Engine — Security Analysis & Hardening Guide

Security analysis of the Fennec QVT-O Engine with attack vector assessment, mitigation planning, and references to BSI guidelines.

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

The QVT-O Engine operates as an **embedded Java library** within a host application. It processes:

- **Transformation source code** (QVT-O text format) — parsed by the ANTLR4 parser
- **EMF models** (in-memory EObject graphs) — provided by the host application
- **Configuration properties** — key-value pairs from the host application
- **Blackbox libraries** — Java implementations registered by the host application
- **Unit resolvers** — resolution of multi-file transformations

### 1.2 Trust Boundaries

```
┌─────────────────────────────────────────────────┐
│  Host Application (trusted)                     │
│  ┌─────────────────────────────────────────┐    │
│  │  QVT-O Engine                           │    │
│  │  ┌──────────────┐  ┌────────────────┐   │    │
│  │  │ Parser       │  │ Evaluator      │   │    │
│  │  │ (ANTLR4)     │  │ (Switch-based) │   │    │
│  │  └──────────────┘  └───────┬────────┘   │    │
│  │                            │             │    │
│  │  ┌─────────────────────────┼──────────┐  │    │
│  │  │ Trust Boundary          │          │  │    │
│  │  │  ┌──────────────┐  ┌───┴────────┐ │  │    │
│  │  │  │ Blackbox Libs │  │ QVT-O Code │ │  │    │
│  │  │  │ (Java)       │  │ (from user)│ │  │    │
│  │  │  └──────────────┘  └────────────┘ │  │    │
│  │  └────────────────────────────────────┘  │    │
│  └─────────────────────────────────────────┘    │
│                                                  │
│  EMF Models  │  Config Properties  │  URIs       │
└─────────────────────────────────────────────────┘
```

### 1.3 Actors

| Actor | Trust Level | Description |
|-------|:-----------:|-------------|
| Host application | High | Creates engine, provides models and config |
| Transformation author | Variable | Writes QVT-O code; may be internal or external |
| Blackbox author | Variable | Implements Java libraries; extends engine capabilities |
| Model provider | Variable | Provides EMF models as input |

**Key question:** If the transformation author or model provider is **untrusted** — what attacks are possible?

---

## 2. Attack Vectors

### Q-1: Blackbox Libraries — Arbitrary Java Code Execution

| | |
|---|---|
| **Severity** | CRITICAL |
| **Vector** | Registered `QvtoBlackboxLibrary` implements `invoke()` with arbitrary Java code |
| **Impact** | Full JVM access (filesystem, network, processes, reflection) |
| **Prerequisite** | Attacker can register a blackbox library |
| **File** | `QvtoOperationProvider.java:614-641` |

**Analysis:** The `invoke()` method is called without sandboxing. The `QvtoBlackboxInvocationContext` provides access to:
- Context object (`self`)
- All config properties (`getConfigProperties()`)
- Model extents (`getExtent(name)`)
- EPackage registry (`getPackageRegistry()`)

**BSI reference:** BSI TR-03185 §4.3 (Secure Architecture), OWASP: Insecure Deserialization/Third-Party Components

---

### Q-2: Config Properties — Sensitive Data Leakage

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | Transformation reads sensitive config properties and leaks them via `log()` or output model |
| **Impact** | Credentials, API keys, internal URLs leaked to transformation author |
| **Prerequisite** | Host application passes sensitive data in `configProperties` |
| **File** | `QvtoEvalEnvironment.java:64-69` |

**Analysis:** All `configProperties` are directly registered as variables in the evaluation scope:

```java
// Host side
QvtoExecutionContext ctx = QvtoExecutionContext.of(
    extents,
    Map.of("dbPassword", "secret123")  // ERROR: becomes QVT-O variable
);

// In QVT-O:
// var pw := dbPassword;  -- directly accessible
// log(pw);               -- leaked via diagnostics
```

**BSI reference:** BSI TR-03185 §4.4 (Secret Protection), CWE-200 (Information Exposure)

---

### Q-3: Filesystem Access via URI

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | `engine.parse(URI)` reads arbitrary files via `Files.readString(Path.of(uri))` |
| **Impact** | Reading arbitrary files on the filesystem |
| **Prerequisite** | Attacker controls the URI passed to `parse()` |
| **File** | `QvtoEngineImpl.java:86-94` |

**Analysis:** No path whitelisting or canonicalization. Path traversal possible:
```java
engine.parse(URI.createFileURI("/etc/passwd"));  // reads /etc/passwd as "QVT-O code"
```

**BSI reference:** BSI TR-03185 §4.5 (Input Validation), CWE-22 (Path Traversal)

---

### Q-4: Resource Exhaustion — Heap (Unbounded Collections)

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | QVT-O code creates unbounded collections via `select()`, `collect()`, loops |
| **Impact** | OutOfMemoryError, Denial of Service |
| **Prerequisite** | Attacker provides malicious transformation |
| **File** | `QvtoEvaluator.java:405-420` (e.g. `select` without size guard) |

**Analysis:** Unlike the OCL evaluator (which checks `maxCollectionSize` for `closure()`, ranges and `product()`), the QVT-O evaluator has **no collection-size guards** for:
- `select()`, `collect()`, `reject()`, `sortedBy()`
- `objectsOfType()`, `objects()` on extents
- While loops with collection building
- Intermediate property storage (`QvtoIntermediatePropertyStore`)

---

### Q-5: Resource Exhaustion — Timeout Not Enforced

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | Infinite loop in QVT-O code (while-true, infinite recursion up to stack limit, then restart) |
| **Impact** | Engine blocks the calling thread indefinitely |
| **Prerequisite** | Attacker provides malicious transformation |
| **File** | `QvtoEvaluationOptions.java:37` (timeout field exists but is not checked) |

**Analysis:** The `timeout` field in `QvtoEvaluationOptions` is defined and configurable, but **never checked in the evaluator**. A transformation can block the thread indefinitely.

**BSI reference:** BSI TR-03185 §4.6 (Availability), CWE-400 (Resource Exhaustion)

---

### Q-6: Trace Model Growth

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | Transformations with many mapping invocations fill the trace model unboundedly |
| **Impact** | High heap consumption, GC pressure |
| **Prerequisite** | Tracing enabled + transformation with many mappings |
| **File** | `QvtoTraceManager.java` |

---

### Q-7: Log Flooding via `log()`

| | |
|---|---|
| **Severity** | LOW |
| **Vector** | Transformation calls `log()` in a loop, filling the diagnostics list |
| **Impact** | Large diagnostics list, memory consumption |
| **Prerequisite** | Attacker provides malicious transformation |
| **File** | `QvtoEvaluator.java:1699-1720` |

**Analysis:** `log()` writes to an in-memory `List<Diagnostic>` without size limit. No log injection possible (no external logger), but memory flooding.

---

### Q-8: Unit Resolver — SSRF / Path Traversal

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | `access` or `import` statement in QVT-O triggers unit resolution with arbitrary `qualifiedName` |
| **Impact** | Depends on unit resolver implementation: filesystem access, network access |
| **Prerequisite** | Unit resolver processes the `qualifiedName` unsafely |
| **File** | `QvtoLinker.java` → `QvtoUnitResolver.resolveUnit(String)` |

**Analysis:** The QVT-O linker calls `resolveUnit(qualifiedName)`. The `qualifiedName` comes from the transformation source code (`import my.pkg.Helper`). An insecurely implemented unit resolver could allow:
- Path traversal: `import ....etc.passwd` → `../../../../etc/passwd.qvto`
- SSRF: Resolver with HTTP backend → `import evil.server.payload`

---

### Q-9: Model Manipulation — Read-Only Bypass

| | |
|---|---|
| **Severity** | LOW |
| **Vector** | Transformation modifies `in` model despite read-only flag |
| **Impact** | Unexpected side effects on input data |
| **Prerequisite** | Programming error — read-only only at extent API level (not at EObject level) |
| **File** | `BasicQvtoModelExtent.java:94` |

**Analysis:** `setReadOnly(true)` only protects `add()`, `setContents()` on the extent. Direct EObject manipulation (`eSet()`, `eUnset()`) on objects **within** the extent is not prevented.

---

## 3. Existing Mitigations

### 3.1 Inherited from OCL (14 Vectors)

The QVT-O Engine inherits all OCL mitigations (documented in `ocl-architecture.md` §10):

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

### 3.2 QVT-O Specific

| Mitigation | Implementation | Status |
|------------|----------------|--------|
| Stack depth limit | `maxStackDepth` (default: 1000) in `QvtoEvaluationOptions` | Implemented |
| Timeout enforcement | `timeout` → deadline check at all checkpoints + loops | Implemented (M-3) |
| Loop iteration limit | `maxLoopIterations` (default: 1,000,000) in while/for/imperativeIterate | Implemented (M-8) |
| Diagnostics limit | `maxDiagnostics` (default: 10,000) in `addDiagnostic()` | Implemented (M-4b) |
| Trace records limit | `maxTraceRecords` (default: 1,000,000) in `QvtoTraceManager` | Implemented (M-4c) |
| Abstract classes | Instantiation rejected (`eClass.isAbstract()`) | Implemented |
| No reflection | No `Class.forName()`, no `Method.invoke()` | By design |
| No dynamic loading | Only EClasses from the metamodel can be instantiated | By design |
| Import cycle detection | `QvtoLinker` detects circular imports | Implemented |
| Controlled `assert fatal` | `FatalAssertionException` is caught, no JVM crash | Implemented |
| Read-only extents | `BasicQvtoModelExtent.setReadOnly(true)` for `in` parameters | Implemented |
| Blackbox disabled by default (D29) | `blackboxEnabled = false`, opt-in with allow-list | Implemented |
| Unit resolver disabled by default (D29) | `unitResolverEnabled = false`, opt-in with allow-list | Implemented |
| Immutable extension config (D29) | No runtime registration — all extensions declared at config time | Implemented |
| Linker-stub validation (D29) | Linker always validates imports, rejects unresolvable stubs | Implemented |

### 3.3 Security Test Coverage

**OCL:** 19 dedicated security tests in `OclSecurityHardeningTest.java`

**QVT-O:** 12 dedicated security tests in `QvtoSecurityHardeningTest.java`:

| Test | Vector | Verification |
|------|--------|--------------|
| `q4_whileLoop_exceedingLimit_terminates` | Q-4/Q-5 | While loop + maxLoopIterations |
| `q4_whileLoop_withinLimit_succeeds` | Q-4 | Normal loop within limit |
| `q4_forLoop_exceedingLimit_terminates` | Q-4 | For loop + maxLoopIterations |
| `q5_timeout_exceedingDeadline_terminates` | Q-5 | Timeout deadline enforcement |
| `q5_timeout_withinDeadline_succeeds` | Q-5 | Normal execution within deadline |
| `q5_recursiveMapping_exceedingStackDepth_terminates` | Q-5 | Stack depth limit |
| `q7_diagnosticsFlooding_truncatesAtLimit` | Q-7 | Diagnostics list limit |
| `options_defaults_haveCorrectValues` | — | Default values verification |
| `options_withMethods_createNewInstances` | — | Immutability verification |
| `d29_defaults_allDisabled` | Q-1/Q-8/D29 | All extension flags default to disabled |
| `d29_blackboxDisabled_importFails` | Q-1/D29 | Blackbox import rejected when disabled |
| `d29_unitResolverDisabled_importFails` | Q-8/D29 | Unit resolver import rejected when disabled |

---

## 4. Mitigation Plan

### Implemented Mitigations

#### M-3: Timeout Enforcement ✅ IMPLEMENTED

**Problem:** Q-5 — Timeout defined but not enforced.

**Solution:** Deadline-based checking via `System.nanoTime()`. Deadline is set at the start of `execute()` and checked at all `stackDepth` checkpoints + loop iterations (~15ns per check).

**Checkpoints:** `callOperation`, `callMappingStrict`, `callMapping`, `callConstructor`, `caseWhileExp`, `caseForExp`, `caseImperativeIterateExp`

**Test:** `q5_timeout_exceedingDeadline_terminates`

---

#### M-4b: Diagnostics Flooding Limit ✅ IMPLEMENTED

**Problem:** Q-7 — Unbounded `List<Diagnostic>` from `log()` in loops.

**Solution:** `maxDiagnostics` (default: 10,000) in `QvtoEvaluationOptions`. When exceeded, a truncation warning is inserted and further entries are discarded.

**Test:** `q7_diagnosticsFlooding_truncatesAtLimit`

---

#### M-4c: Trace Records Limit ✅ IMPLEMENTED

**Problem:** Q-6 — Unbounded trace model growth.

**Solution:** `maxTraceRecords` (default: 1,000,000) in `QvtoEvaluationOptions`, enforced in `QvtoTraceManager.addRecord()`. When exceeded, further records are silently dropped.

---

#### M-8 (Loop Limits): While/For Loop Iteration Limit ✅ IMPLEMENTED

**Problem:** Q-4/Q-5 — `while(true)` without counter, unbounded for loops.

**Solution:** `maxLoopIterations` (default: 1,000,000) in `QvtoEvaluationOptions`. Iteration counters in `caseWhileExp`, `caseForExp`, and `caseImperativeIterateExp`.

**Tests:** `q4_whileLoop_exceedingLimit_terminates`, `q4_forLoop_exceedingLimit_terminates`

---

#### M-7: QVT-O Security Tests ✅ IMPLEMENTED

**9 tests** in `QvtoSecurityHardeningTest.java` — verifies timeout, loop limits, stack depth, diagnostics limit, and options defaults.

---

### Remaining Mitigations (Documentation / Future)

#### M-1: Blackbox Library Security Controls

**Problem:** Q-1 — Arbitrary Java code execution without restrictions.

**Status:** Addressed by D29 (Extension Security Controls). Blackbox libraries disabled by default (`blackboxEnabled = false`). When enabled, allow-list restricts which modules can be invoked. Max-limit caps number of registered libraries. Runtime registration removed (immutable configuration). See [DR-D29](design-decisions.md#dr-d29-extension-security-controls).

Future: D30 (Resource Integrity Service) adds hash-based verification. See [DR-D30](design-decisions.md#dr-d30-resource-integrity-service-future).

---

#### M-2: Config Properties — Scoped Access

**Problem:** Q-2 — All properties are exposed as QVT-O variables.

**Status:** Documented in Embedder Guide (§6.1): "No secrets in configProperties".

**Potential mitigations:**

| Mitigation | Effort | Description |
|------------|--------|-------------|
| M-2b: Prefixed scope | Medium | Only pass properties with prefix `qvto.` |

---

#### M-5: URI Validation for `parse(URI)`

**Problem:** Q-3 — Arbitrary files can be read.

**Status:** Documented in Embedder Guide (§6.2) with code example for secure URI validation.

---

#### M-6: Unit Resolver Security Controls

**Problem:** Q-8 — Insecurely implemented resolvers.

**Status:** Addressed by D29 (Extension Security Controls). Unit resolvers disabled by default (`unitResolverEnabled = false`). When enabled, allow-list restricts which modules can be resolved. Max-limit caps number of registered resolvers. Runtime registration removed (immutable configuration). See [DR-D29](design-decisions.md#dr-d29-extension-security-controls).

Future: D30 (Resource Integrity Service) adds hash-based verification of resolved source. See [DR-D30](design-decisions.md#dr-d30-resource-integrity-service-future).

---

#### M-4a: Collection Size Guards in OCL Iterators

**Problem:** OCL `select()`, `collect()`, `reject()` without collection size guard.

**Status:** Deliberately not implemented. Rationale: these operations can never produce more elements than the input collection contains. The input collection size is bounded by existing guards (range, product, allInstances). The risk is therefore transitively covered.

---

#### M-9: EObject-Level Read-Only Protection (Low Priority)

**Problem:** Q-9 — `setReadOnly()` only protects extent API, not EObject mutation.

**Status:** Accepted risk. An EMF proxy-based solution would be high effort for low risk.

---

## 5. BSI TR-03185 Mapping

Mapping of identified risks and mitigations to the requirement areas of [BSI TR-03185](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/Technische-Richtlinien/TR-nach-Thema-sortiert/tr03185/tr-03185.html) (Secure Software Lifecycle):

### 5.1 Requirement Area: Secure Design (§4.3)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Threat modeling | Trust boundary diagram (§1.2), actor analysis (§1.3) | Present |
| Attack surface minimization | No reflection, no dynamic loading, EMF-only types | Implemented |
| Defense in depth | OCL limits + QVT-O stack limit + read-only extents | Partial (gaps in timeout, collection size) |
| Secure defaults | `OclEvaluationOptions.strict()`, `maxStackDepth=1000` | Implemented |

### 5.2 Requirement Area: Input Validation (§4.5)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Parser input validation | ANTLR4-based, structured errors with line/column | Implemented |
| URI/path validation | `parse(URI)` without canonicalization | **Gap** → M-5 |
| Config property validation | No filtering of sensitive keys | **Gap** → M-2 |
| QualifiedName validation | No check for path traversal patterns | **Gap** → M-6 |

### 5.3 Requirement Area: Availability (§4.6)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Resource limits | OCL: 5 limits + QVT-O: 4 limits | Implemented |
| Timeout enforcement | Deadline check at 7 checkpoints + loops | ✅ Implemented (M-3) |
| Loop iteration limit | `maxLoopIterations` in while/for/imperativeIterate | ✅ Implemented (M-8) |
| Diagnostics limit | `maxDiagnostics` with truncation warning | ✅ Implemented (M-4b) |
| Trace size limit | `maxTraceRecords` in `QvtoTraceManager` | ✅ Implemented (M-4c) |

### 5.4 Requirement Area: Third-Party Components (§4.7)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Blackbox library audit | D29: disabled by default, allow-list, immutable config | ✅ Implemented |
| Least privilege | D29: `blackboxEnabled` + `allowedBlackboxModules` allow-list | ✅ Implemented |
| Supply chain | D29: no runtime registration, immutable configuration | ✅ Implemented |

### 5.5 Requirement Area: Testing (§4.8)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Security tests OCL | 19 tests in `OclSecurityHardeningTest` | ✅ Implemented |
| Security tests QVT-O | 12 tests in `QvtoSecurityHardeningTest` | ✅ Implemented (M-7) |
| Penetration testing | Not performed | Pending |

### 5.6 Further Relevant BSI Guidelines

| Guideline | Relevance | Reference |
|-----------|-----------|-----------|
| [BSI TR-03185](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/Technische-Richtlinien/TR-nach-Thema-sortiert/tr03185/tr-03185.html) | High | Secure software lifecycle — primary guideline |
| [BSI TR-03185-2](https://www.bsi.bund.de/DE/Service-Navi/Presse/Alle-Meldungen-News/Meldungen/TR-03185-2_OSS_251103.html) | High | Part 2 for open-source software (EPL-2.0 license) |
| [BSI IT-Grundschutz](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/IT-Grundschutz/it-grundschutz_node.html) | Medium | Foundation for TR-03185; APP.6 General Software |
| BSI TR-02102 | Low | Cryptographic procedures — not directly relevant (no crypto in QVT-O) |

---

## 6. Embedder Guide

Recommendations for developers embedding the QVT-O Engine in their applications.

### 6.1 Minimum Hardening (MUST)

```java
// 1. Configure OCL limits
OclEvaluationOptions oclOpts = OclEvaluationOptions.strict()
    .withMaxDepth(200)                   // Tightened from 1000
    .withMaxCollectionSize(50_000)       // Tightened from 1_000_000
    .withMaxClosureIterations(10_000)    // Tightened from 100_000
    .withMaxRegexLength(200);            // Tightened from 1000

// 2. Configure QVT-O limits
QvtoEvaluationOptions qvtoOpts = QvtoEvaluationOptions.defaults()
    .withMaxStackDepth(200)              // Tightened from 1000
    .withOclOptions(oclOpts);

// 3. NO secrets in config properties
QvtoExecutionContext ctx = QvtoExecutionContext.of(
    List.of(inExtent, outExtent),
    Map.of(
        "outputDir", "/safe/path",       // OK: non-sensitive
        // "dbPassword", secret          // NEVER: becomes QVT-O variable!
    )
);

// 4. Read-only for in parameters
BasicQvtoModelExtent inExtent = new BasicQvtoModelExtent(model);
inExtent.setReadOnly(true);

// 5. Check result
QvtoExecutionResult result = engine.execute(trafo, ctx, qvtoOpts);
if (!result.isSuccess()) {
    // Evaluate diagnostics, do NOT ignore
}
```

### 6.2 URI Validation (when using `parse(URI)`)

```java
public OperationalTransformation safeParse(QvtoEngine engine, URI uri, Path allowedBase)
        throws QvtoParseException {
    // Only allow file scheme
    if (!"file".equals(uri.scheme())) {
        throw new IllegalArgumentException("Only file:// URIs allowed");
    }

    // Canonicalize path and check base directory
    Path resolved = Path.of(uri).toAbsolutePath().normalize();
    if (!resolved.startsWith(allowedBase)) {
        throw new IllegalArgumentException("Path outside allowed directory: " + resolved);
    }

    return engine.parse(uri);
}
```

### 6.3 Blackbox Libraries (D29)

Blackbox libraries are **disabled by default** (D29). Enable explicitly with optional allow-list:

```java
BasicQvtoBlackboxRegistry registry = new BasicQvtoBlackboxRegistry();
registry.register(new MyAuditedLibrary());  // internally reviewed

QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
    .blackboxRegistry(registry)
    .blackboxEnabled(true)                       // required — default is false
    .allowedBlackboxModules(Set.of("mylib"))     // optional — empty = allow all
    .build();

// Without blackboxEnabled(true), import statements for blackbox libraries
// will fail at link-time with "Cannot resolve import: ..."

// DO NOT: dynamically loaded or user-provided libraries
// registry.register(untrustedLibrary);  // RISK: full JVM access!
```

Similarly for **unit resolvers**:

```java
QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
    .addUnitResolver(new SafeUnitResolver(baseDir))
    .unitResolverEnabled(true)                   // required — default is false
    .allowedUnitModules(Set.of("Helper", "Utils")) // optional allow-list
    .build();
```

**Runtime registration is not supported** — all extensions must be declared at configuration
time via the immutable `QvtoConfiguration`. This prevents runtime injection attacks.

### 6.4 Unit Resolver — Secure Implementation

```java
public class SafeUnitResolver implements QvtoUnitResolver {

    private final Path baseDir;

    public SafeUnitResolver(Path baseDir) {
        this.baseDir = baseDir.toAbsolutePath().normalize();
    }

    @Override
    public Optional<QvtoUnit> resolveUnit(String qualifiedName) {
        // Validation: only valid Java identifiers
        if (!qualifiedName.matches("[a-zA-Z_][a-zA-Z0-9_.]*")) {
            return Optional.empty();
        }

        // No ".." or absolute paths
        if (qualifiedName.contains("..")) {
            return Optional.empty();
        }

        Path file = baseDir.resolve(
            qualifiedName.replace('.', '/') + ".qvto"
        ).toAbsolutePath().normalize();

        // Base directory check
        if (!file.startsWith(baseDir)) {
            return Optional.empty();
        }

        // ... read file ...
    }
}
```

### 6.5 Timeout Protection

The engine enforces timeouts natively via `QvtoEvaluationOptions.withTimeout()` (M-3 implemented).
No external executor-based timeout is needed:

```java
QvtoEvaluationOptions opts = QvtoEvaluationOptions.defaults()
    .withTimeout(Duration.ofSeconds(30));

QvtoExecutionResult result = engine.execute(trafo, ctx, opts);
if (!result.isSuccess()) {
    // Check for timeout diagnostic
}
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
| CWE-94 (Code Injection) | Q-1 Blackbox Libraries |
| CWE-200 (Information Exposure) | Q-2 Config Properties |
| CWE-22 (Path Traversal) | Q-3 URI, Q-8 Unit Resolver |
| CWE-400 (Resource Exhaustion) | Q-4 Collections, Q-5 Timeout, Q-6 Trace |
| CWE-770 (Allocation without Limits) | Q-4 Intermediate Properties |

### Internal Documents

- [OCL Architecture §10 — Security Hardening](ocl-architecture.md) — OCL-specific threat analysis and tests
- [QVT-O Architecture](qvto-architecture.md) — Engine architecture
- [Design Decisions](design-decisions.md) — Architecture decisions D1–D30 (D29: Extension Security Controls, D30: Resource Integrity)
