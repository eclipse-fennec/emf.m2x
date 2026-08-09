# MOFM2T Engine — Security Analysis & Hardening Guide

Security analysis of the Fennec MOFM2T Engine with attack vector assessment, mitigation planning, and references to BSI guidelines.

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

The MOFM2T Engine operates as an **embedded Java library** within a host application. It processes:

- **MOFM2T template source code** (text format) — parsed by the ANTLR4 parser
- **EMF models** (in-memory EObject graphs) — provided via `M2tContext`
- **Module composition** (extends/imports) — multi-module template systems
- **File output** — generated text files via `[file]` blocks

Unlike OCL (pure query) and QVT-O (model transformation), MOFM2T **generates text output** including file paths. This introduces **file-system-adjacent attack surface** even though the default engine uses in-memory output.

### 1.2 Trust Boundaries

```
+-------------------------------------------------+
|  Host Application (trusted)                     |
|  +---------------------------------------------+
|  |  M2T Engine                                  |
|  |  +--------------+  +------------------+      |
|  |  | Parser       |  | Evaluator        |      |
|  |  | (ANTLR4)     |  | (Switch-based)   |      |
|  |  +--------------+  +--------+---------+      |
|  |                             |                 |
|  |  +--------------------------+--------+        |
|  |  | Trust Boundary                    |        |
|  |  |  +----------------+  +---------+  |        |
|  |  |  | Generation     |  | Template|  |        |
|  |  |  | Strategy (SPI) |  | (user)  |  |        |
|  |  |  +----------------+  +---------+  |        |
|  |  +-----------------------------------+        |
|  +---------------------------------------------+
|                                                  |
|  EMF Models  |  Module URIs  |  Output Files     |
+-------------------------------------------------+
```

### 1.3 Actors

| Actor | Trust Level | Description |
|-------|:-----------:|-------------|
| Host application | High | Creates engine, provides models and configuration |
| Template author | Variable | Writes MOFM2T templates; may be internal or external |
| Model provider | Variable | Provides EMF models as input |
| GenerationStrategy implementor | Variable | Controls file output (SPI) |

**Key question:** If the template author or model provider is **untrusted** — what attacks are possible?

---

## 2. Attack Vectors

### T-1: Template Recursion — Stack Overflow

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | Template A invokes Template B which invokes Template A (mutual recursion) |
| **Impact** | StackOverflowError, JVM crash |
| **Prerequisite** | Attacker controls template source |
| **File** | `M2tEvaluator.java:invokeTemplate` |

**Analysis:** Template invocations use Java method calls (`invokeTemplate` → `executeBody` → `templateSwitch.doSwitch` → `caseTemplateInvocation` → `invokeTemplate`). No depth counter exists. Mutual recursion or self-recursion exhausts the Java call stack.

**BSI reference:** CWE-674 (Uncontrolled Recursion)

---

### T-2: For-Block Iteration Exhaustion

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | `[for (e : EObject \| hugeCollection)]...[/for]` — for-block iterates over a large or infinite collection |
| **Impact** | CPU exhaustion, unbounded memory growth from output |
| **Prerequisite** | Attacker controls template + model with large collections |
| **File** | `M2tEvaluator.java:caseForBlock` (line 260) |

**Analysis:** The for-block iterates over an OCL collection result without any iteration counter. If the OCL expression returns a large collection (e.g., `EClass.allInstances()` on a large model), the loop runs unboundedly. Combined with text output in the body, this also causes output size explosion.

**BSI reference:** CWE-400 (Resource Exhaustion)

---

### T-3: Cross-Product Explosion in Set-Argument Invocations

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | `[t(Set{1..100}, Set{1..100}, Set{1..100})/]` — template invocation with Set arguments causes Cartesian product |
| **Impact** | OutOfMemoryError (100^3 = 1,000,000 combinations) |
| **Prerequisite** | Attacker controls template source |
| **File** | `M2tEvaluator.java:crossProduct` (line 812) |

**Analysis:** MOFM2T §8.1.10 specifies that when a template argument is a Set and the parameter is a singleton, the template is invoked for each element of the cross-product. The `crossProduct()` method has no size limit — with N Set arguments of size M each, it produces M^N combinations.

**BSI reference:** CWE-400 (Resource Exhaustion)

---

### T-4: File Path Traversal via [file] Block

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | `[file ('../../etc/shadow', false, 'UTF-8')]...[/file]` — path traversal in file block URL |
| **Impact** | File write to arbitrary path (if GenerationStrategy writes to filesystem) |
| **Prerequisite** | Attacker controls template + a GenerationStrategy that writes to disk **without** path validation. The shipped `FileSystemGenerationStrategy` validates (M-T6). |
| **File** | `M2tEvaluator.java:caseFileBlock` (line 348), `M2tWriterStack.java:pushFileWriter` |

**Analysis:** The file path from the `[file]` block URL expression is passed directly to `M2tWriterStack.pushFileWriter()` without any validation or canonicalization. The default engine stores paths as keys in an in-memory map (no actual filesystem I/O), but a `M2tGenerationStrategy` implementation that writes to the filesystem could be exploited.

**BSI reference:** CWE-22 (Path Traversal)

---

### T-5: URI Path Traversal in parse(URI)

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | `engine.parse(URI.createURI("file:///etc/passwd"))` — reads arbitrary files as template source |
| **Impact** | Reading arbitrary files on the filesystem |
| **Prerequisite** | Attacker controls the URI passed to `parse()` |
| **File** | `M2tEngineImpl.java:parse(URI)` (line 124) |

**Analysis:** Same pattern as Q-3 in QVT-O. `Files.readString(Path.of(moduleUri))` reads any file accessible to the JVM process. No path whitelisting or canonicalization.

**BSI reference:** CWE-22 (Path Traversal)

---

### T-6: Protected Area Marker Injection

| | |
|---|---|
| **Severity** | MEDIUM |
| **Vector** | Template output contains `// [protected 'inject']` markers that manipulate protected area merging |
| **Impact** | Protected area merge confusion, loss of user code on regeneration |
| **Prerequisite** | Attacker controls template source |
| **File** | `M2tProtectedAreaMerger.java:merge` |

**Analysis:** If a template generates text that happens to contain `// [protected 'id']` and `// [/protected]` markers outside of actual `[protected]` blocks, the merger's regex-based extraction will pick them up. This could cause unintended merging behavior or marker collision. The D31 hash-based smart merge mitigates data loss (unmodified content gets replaced by new default), but marker collision between legitimate and injected areas is still possible.

**BSI reference:** CWE-74 (Injection)

---

### T-7: Output Size Exhaustion

| | |
|---|---|
| **Severity** | HIGH |
| **Vector** | Template generates unbounded text via loops: `[for (i : Integer \| Sequence{1..1000000})]text[/for]` |
| **Impact** | OutOfMemoryError from StringBuilder growth |
| **Prerequisite** | Attacker controls template + model |
| **File** | `M2tWriterStack.java:append` — unbounded StringBuilder |

**Analysis:** All text output accumulates in `StringBuilder` instances on the `M2tWriterStack`. No output size limit exists. A for-loop over a large collection, each iteration emitting text, can exhaust heap memory. This compounds with T-2 (for-block iteration exhaustion).

**BSI reference:** CWE-400 (Resource Exhaustion)

---

### T-8: Module Composition — Extends/Imports Chain

| | |
|---|---|
| **Severity** | LOW |
| **Vector** | Deep module extends/imports chain (A extends B extends C extends ...) |
| **Impact** | Increased link time, override resolution overhead |
| **Prerequisite** | Attacker controls multiple module sources |
| **File** | `M2tModuleLinker.java:link` |

**Analysis:** Module linking processes all extends/imports at parse-time. The `M2tModuleLinker` resolves references linearly. Deep chains increase processing time but do not cause unbounded growth. Override resolution in the evaluator uses a `visited` set for cycle detection (already hardened).

---

### T-9: Diagnostics Flooding

| | |
|---|---|
| **Severity** | LOW |
| **Vector** | Template with many OCL evaluation errors fills the diagnostics list |
| **Impact** | Memory consumption from large diagnostic list |
| **Prerequisite** | Attacker provides template with many invalid expressions |
| **File** | `M2tEvaluator.java:addDiagnostic` |

**Analysis:** Already mitigated. `maxDiagnostics` (default: 10,000) truncates the diagnostics list with a warning message when exceeded.

---

## 3. Existing Mitigations

### 3.1 Inherited from OCL (14 Vectors)

The MOFM2T Engine delegates OCL expression evaluation to the OCL Engine, inheriting all OCL mitigations:

| ID | Vector | Status | Implementation |
|----|--------|--------|----------------|
| S-1 | ReDoS | Mitigated | `maxRegexLength` (default: 1000) |
| S-2 | Range explosion | Mitigated | `maxCollectionSize` check before range creation |
| S-3 | Product explosion | Mitigated | Product size checked before computation |
| S-4 | Unbounded closure | Mitigated | `maxClosureIterations` (default: 100,000) |
| S-5 | Unsafe casts | Mitigated | Type-safe dispatch logic |
| S-6 | Delegate injection | N/A | M2T does not use EMF delegates |
| S-7 | Parse amplification | Mitigated | LRU expression cache |
| S-8 | allInstances large extent | Mitigated | `maxCollectionSize` check |
| S-9 | Stack overflow (OCL) | Mitigated | `maxDepth` (default: 1000) |
| S-10 | Integer overflow | Accepted | Low risk |
| S-11 | String concat amplification | Partial | Bounded by `maxDepth` |
| S-12 | allInstances result size | Mitigated | `maxCollectionSize` check |
| S-13 | Custom op provider abuse | Mitigated (D29) | M2T stdlib is built-in, no external providers |
| S-14 | Delegate URI spoofing | N/A | M2T does not use delegates |

### 3.2 M2T Specific

| Mitigation | Implementation | Status |
|------------|----------------|--------|
| Diagnostics limit | `maxDiagnostics` (default: 10,000) with truncation warning | ✅ Implemented |
| Override cycle detection | `visited` Set in `resolveOverride()` | ✅ Implemented |
| No reflection | No `Class.forName()`, no `Method.invoke()` | By design |
| No network access | No HTTP/socket operations | By design |
| In-memory output default | Default engine uses `M2tWriterStack` (in-memory), no filesystem I/O | By design |
| Hash-based merge (D31) | Protected area merger uses SHA-256 hash to detect user modifications | ✅ Implemented |
| Null-safety on public API | `Objects.requireNonNull()` on all public methods | ✅ Implemented |
| Stdlib null-safety | `Objects.toString(self, "")` for null self in all stdlib operations | ✅ Implemented |

### 3.3 Security Test Coverage

**OCL:** 22 tests in `OclSecurityHardeningTest.java` (inherited via OCL engine composition)

**M2T:** Hardening tests in `M2tHardeningTest.java` (null-safety, error recovery, diagnostics) — but no dedicated **security** tests yet.

---

## 4. Mitigation Plan

### Implemented Mitigations

#### M-T1: Diagnostics Flooding Limit ✅ IMPLEMENTED

**Problem:** T-9 — Unbounded diagnostics list from many OCL errors.

**Solution:** `maxDiagnostics` (default: 10,000) in `M2tConfiguration`. When exceeded, a truncation warning is inserted and further entries are discarded.

**Test:** `M2tHardeningTest.maxDiagnosticsLimit`

---

#### M-T2: Override Cycle Detection ✅ IMPLEMENTED

**Problem:** Circular override chains could cause infinite loop in `resolveOverride()`.

**Solution:** `visited` Set using `IdentityHashMap` in `resolveOverride()`. Cycles are detected and resolution stops, returning the target template.

---

### To Be Implemented

#### M-T3: Template Recursion Depth Limit

**Problem:** T-1 — Unbounded template recursion.

**Solution:** `maxTemplateDepth` (default: 1,000) in `M2tConfiguration`. Depth counter incremented in `invokeTemplate()` and `execute()`, decremented on return. When exceeded, an ERROR diagnostic is emitted and the invocation returns empty string.

---

#### M-T4: For-Block Iteration Limit

**Problem:** T-2 — Unbounded for-block iterations.

**Solution:** `maxForIterations` (default: 1,000,000) in `M2tConfiguration`. Iteration counter in `caseForBlock()`. When exceeded, an ERROR diagnostic is emitted and the loop terminates.

---

#### M-T5: Cross-Product Size Limit

**Problem:** T-3 — Exponential cross-product in set-argument invocations.

**Solution:** `maxCrossProductSize` (default: 1,000,000) in `M2tConfiguration`. Size check in `crossProduct()` before building combinations. When exceeded, an ERROR diagnostic is emitted and the invocation is skipped.

---

### Remaining Mitigations (Documentation / Accepted)

#### M-T6: File Path Traversal

**Problem:** T-4 — Path traversal in `[file]` block paths.

**Status:** **Mitigated** for the shipped strategy. `FileSystemGenerationStrategy` (in `m2t.engine`) resolves every template-supplied path against its output directory and rejects anything that escapes it — `..` segments, absolute paths, and symbolic links inside the output directory that point outside — with a `SecurityException`. The default in-memory engine still stores file paths as map keys and touches no filesystem.

A custom `M2tGenerationStrategy` that writes to the filesystem remains responsible for its own path validation; the engine passes the template-supplied path through unchanged.

---

#### M-T7: URI Path Traversal in parse(URI)

**Problem:** T-5 — Arbitrary file reading.

**Status:** Documented in Embedder Guide (§6.1). Same pattern as Q-3 in QVT-O — the host application must validate URIs before passing to `parse()`.

---

#### M-T8: Protected Area Marker Injection ✅ IMPLEMENTED

**Problem:** T-6 — Injected markers confuse merge.

**Solution:** `protectedAreaEnabled` flag (default: `true`) in `M2tConfiguration`. When disabled:
- `[protected]` blocks emit their body content **without markers**
- No merge with existing content is performed (even if a `M2tGenerationStrategy` is configured)

Additionally, D31 hash-based smart merge limits damage when enabled: if marker content was never user-modified, the new template default replaces it.

**Tests:** `t6_protectedAreaDisabled_noMarkersInOutput`, `t6_protectedAreaEnabled_markersInOutput`, `t6_defaultIsEnabled`

---

#### M-T9: Output Size Limit ✅ IMPLEMENTED

**Problem:** T-7 — Unbounded text output from loops, nested invocations, or large OCL strings.

**Solution:** `maxOutputSize` (default: 10,000,000 characters ~ 10 MB) in `M2tConfiguration`. The `M2tWriterStack` tracks total output size across all writers. When exceeded, further `append()` calls are silently dropped and an ERROR diagnostic is emitted after execution completes. Use `maxOutputSize(0)` for unlimited output.

**Tests:** `t7_outputExceedingLimit_producesError`, `t7_outputWithinLimit_succeeds`, `t7_unlimitedOutput_succeeds`

---

## 5. BSI TR-03185 Mapping

Mapping of identified risks and mitigations to the requirement areas of [BSI TR-03185](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/Technische-Richtlinien/TR-nach-Thema-sortiert/tr03185/tr-03185.html) (Secure Software Lifecycle):

### 5.1 Requirement Area: Secure Design (§4.3)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Threat modeling | Trust boundary diagram (§1.2), actor analysis (§1.3), 9 vectors | Present |
| Attack surface minimization | No reflection, no network access, in-memory output default | By design |
| Defense in depth | OCL limits (inherited) + M2T limits (template depth, loop, cross-product) + diagnostics limit | Implemented |
| Secure defaults | Conservative limits: depth 1000, iterations 1M, diagnostics 10K | Implemented |

### 5.2 Requirement Area: Input Validation (§4.5)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Parser input validation | ANTLR4-based, structured errors with line/column | Implemented |
| URI/path validation | `parse(URI)` without canonicalization | **Gap** → M-T7 (Documented) |
| File path validation | `[file]` paths not validated by engine | Documented (SPI responsibility) |
| Protected area control | `protectedAreaEnabled` flag — disable marker generation + merging | ✅ Implemented (M-T8) |
| Null-safety | `Objects.requireNonNull()` on all public API methods | ✅ Implemented |

### 5.3 Requirement Area: Availability (§4.6)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Template recursion limit | `maxTemplateDepth` (default: 1,000) | ✅ Implemented (M-T3) |
| For-block iteration limit | `maxForIterations` (default: 1,000,000) | ✅ Implemented (M-T4) |
| Cross-product size limit | `maxCrossProductSize` (default: 1,000,000) | ✅ Implemented (M-T5) |
| Output size limit | `maxOutputSize` (default: 10,000,000 chars) | ✅ Implemented (M-T9) |
| Diagnostics limit | `maxDiagnostics` (default: 10,000) with truncation | ✅ Implemented (M-T1) |
| OCL evaluation limits | Inherited: maxDepth, maxCollectionSize, maxClosureIterations, maxRegexLength, timeout | ✅ Inherited |

### 5.4 Requirement Area: Third-Party Components (§4.7)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| GenerationStrategy audit | SPI implementors are registered by host application | By design |
| No dynamic code loading | Templates are text-only, no Java class loading | By design |
| Module linking | `M2tModuleLinker` validates references at link-time | Implemented |

### 5.5 Requirement Area: Testing (§4.8)

| TR Requirement | Implementation | Status |
|----------------|----------------|--------|
| Security tests | Tests in `M2tSecurityHardeningTest` | ✅ Implemented |
| Null-safety tests | Tests in `M2tHardeningTest` | ✅ Implemented |
| Limit enforcement tests | Template depth, for-loop, cross-product, diagnostics | ✅ Implemented |
| Penetration testing | Not performed | Pending |

### 5.6 Further Relevant BSI Guidelines

| Guideline | Relevance | Reference |
|-----------|-----------|-----------|
| [BSI TR-03185](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/Technische-Richtlinien/TR-nach-Thema-sortiert/tr03185/tr-03185.html) | High | Secure software lifecycle — primary guideline |
| [BSI TR-03185-2](https://www.bsi.bund.de/DE/Service-Navi/Presse/Alle-Meldungen-News/Meldungen/TR-03185-2_OSS_251103.html) | High | Part 2 for open-source software (EPL-2.0 license) |
| [BSI IT-Grundschutz](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/IT-Grundschutz/it-grundschutz_node.html) | Medium | Foundation for TR-03185; APP.6 General Software |

---

## 6. Embedder Guide

Recommendations for developers embedding the MOFM2T Engine in their applications.

### 6.1 URI Validation (when using `parse(URI)`)

```java
public Module safeParse(M2tEngine engine, URI uri, Path allowedBase)
        throws M2tParseException {
    // Only allow file scheme
    if (!"file".equals(uri.getScheme())) {
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

### 6.2 GenerationStrategy — Secure File Output

When implementing `M2tGenerationStrategy` for filesystem output, validate all file paths:

```java
public class SecureFileStrategy implements M2tGenerationStrategy {

    private final Path outputBase;

    public SecureFileStrategy(Path outputBase) {
        this.outputBase = outputBase.toAbsolutePath().normalize();
    }

    @Override
    public Writer createWriter(String filePath, OpenModeKind mode, Charset charset) {
        // Canonicalize and validate path
        Path resolved = outputBase.resolve(filePath).toAbsolutePath().normalize();
        if (!resolved.startsWith(outputBase)) {
            throw new SecurityException("Path traversal detected: " + filePath);
        }

        // Reject absolute paths and ".." components
        if (filePath.startsWith("/") || filePath.contains("..")) {
            throw new SecurityException("Invalid file path: " + filePath);
        }

        try {
            Files.createDirectories(resolved.getParent());
            return Files.newBufferedWriter(resolved, charset,
                mode == OpenModeKind.APPEND
                    ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND}
                    : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING});
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
```

### 6.3 Tightened Limits for Untrusted Templates

```java
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngineImpl;

// 1. Configure OCL with conservative limits
OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport())
    .maxDepth(200)
    .maxCollectionSize(50_000)
    .maxClosureIterations(10_000)
    .maxRegexLength(200)
    .timeoutMs(5_000)
    .build();

// 2. Configure M2T with tightened limits
M2tConfiguration config = M2tConfiguration.builder(oclConfig)
    .maxTemplateDepth(100)         // Tightened from 1000
    .maxForIterations(10_000)      // Tightened from 1_000_000
    .maxCrossProductSize(10_000)   // Tightened from 1_000_000
    .maxOutputSize(1_000_000)      // Tightened from 10_000_000 (~1 MB)
    .maxDiagnostics(1_000)         // Tightened from 10_000
    .protectedAreaEnabled(false)   // T-6: disable for untrusted templates
    .build();

// 3. Create engine and execute
M2tEngineImpl engine = new M2tEngineImpl(config);
Module module = engine.parse(untrustedTemplate, "template");
M2tResult result = engine.execute(module, M2tContext.of(inputElement));

// 4. Check result
if (!result.isSuccess()) {
    result.diagnostics().forEach(d ->
        logger.warn("M2T issue: {}", d.getMessage()));
}
```

### 6.4 Protected Area Security

For untrusted templates, **disable protected areas** to prevent marker injection (T-6):

```java
M2tConfiguration config = M2tConfiguration.builder(oclConfig)
    .protectedAreaEnabled(false)   // T-6: no markers, no merging
    .build();
```

When protected areas are enabled (default):
- Protected area markers use a SHA-256 hash (D31) to detect user modifications
- Unmodified protected areas are safely replaced with new template defaults
- User-modified areas are always preserved
- **Caution:** If template output contains text matching the marker pattern (`// [protected 'id']`), this could interfere with merging. Ensure generated output does not accidentally produce marker-like text.

---

## 7. References

### BSI Guidelines

- [BSI TR-03185 — Secure Software Lifecycle](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/Technische-Richtlinien/TR-nach-Thema-sortiert/tr03185/tr-03185.html) — Primary guideline for secure software development
- [BSI TR-03185-2 — Open Source Software](https://www.bsi.bund.de/DE/Service-Navi/Presse/Alle-Meldungen-News/Meldungen/TR-03185-2_OSS_251103.html) — Part 2 specifically for OSS (relevant for EPL-2.0)
- [BSI IT-Grundschutz](https://www.bsi.bund.de/DE/Themen/Unternehmen-und-Organisationen/Standards-und-Zertifizierung/IT-Grundschutz/it-grundschutz_node.html) — Foundation framework

### CWE References

| CWE | Vector |
|-----|--------|
| CWE-674 (Uncontrolled Recursion) | T-1 Template Recursion |
| CWE-400 (Resource Exhaustion) | T-2 For-Block Iteration, T-3 Cross-Product, T-7 Output Size, T-9 Diagnostics |
| CWE-22 (Path Traversal) | T-4 File Path, T-5 URI Path |
| CWE-74 (Injection) | T-6 Protected Area Marker |

### Internal Documents

- [OCL Security Analysis](ocl-security-analysis.md) — OCL-specific threat analysis (14 vectors)
- [QVT-O Security Analysis](qvto-security-analysis.md) — QVT-O-specific threat analysis (9 vectors)
- [Design Decisions](design-decisions.md) — Architecture decisions D1-D31 (D31: Protected Area Hash-Based Merge)
- [M2T Implementation Plan](m2t-implementation-plan.md) — MOFM2T v1.0 implementation reference
