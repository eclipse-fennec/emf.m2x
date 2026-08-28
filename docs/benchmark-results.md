# Fennec M2M — Benchmark Results

Date: 2026-08-09
Platform: Linux 6.17.0-1030-oem, Java 21
Machine: mark-dell

## 1. OCL Benchmark (Fennec vs Eclipse OCL Classic)

**Test:** `org.eclipse.fennec.m2x.ocl.benchmark.OclPerformanceBenchmarkTest`
**Parameters:** 2,000 JIT warmup iterations, 500 warmup iterations, 1,000 measurement iterations
**Expressions:** simple (`self.name`-level), medium (collection ops), complex (nested iterators + let)

### Variants

| Variant | Description |
|---------|-------------|
| **Fennec plain** | No caching — re-parses every invocation |
| **Fennec+cache** | LRU expression cache enabled — cache hits after first parse |
| **Fennec+warmUp** | Cache + PropertyAccessorCache pre-warmed for source/target packages |
| **Eclipse** | Eclipse OCL Classic (Ecore binding), standalone programmatic API |

### 1.1 Parse Performance

Times are total for 1000 iterations (lower is better).

| Expression | Fennec plain | Fennec+cache | Fennec+warmUp | Eclipse | Fennec plain/Eclipse |
|-----------|-------------|-------------|--------------|---------|----------------------|
| simple | 8.07 ms | 0.12 ms | 0.11 ms | 642.60 ms | **80x faster** |
| medium | 39.19 ms | 0.14 ms | 0.14 ms | 9448.56 ms | **241x faster** |
| complex | 50.89 ms | 0.16 ms | 0.16 ms | 16310.06 ms | **320x faster** |

**Summary:** Fennec parser is **~80–320x faster** without cache, **~100000x faster** with cache (cache hit = no re-parse). Eclipse OCL Classic uses heavyweight Ecore/pivot setup per parse.

### 1.2 Parse + Eval Performance

Realistic workload: parse and evaluate in one call.

| Expression | Fennec plain | Fennec+cache | Fennec+warmUp | Eclipse | Fennec plain/Eclipse |
|-----------|-------------|-------------|--------------|---------|----------------------|
| simple | 6.58 ms | 0.37 ms | 0.36 ms | 1153.48 ms | **175x faster** |
| medium | 35.99 ms | 2.71 ms | 3.18 ms | 15701.08 ms | **436x faster** |
| complex | 70.30 ms | 5.19 ms | 5.19 ms | 24427.42 ms | **347x faster** |

### 1.3 Eval Performance (pre-parsed)

Expression already parsed — measures pure evaluation engine.

| Expression | Fennec plain | Fennec+warmUp | Eclipse | Fennec/Eclipse |
|-----------|-------------|--------------|---------|----------------|
| simple | 0.24 ms | 0.22 ms | 0.27 ms | **0.89x** (11% faster) |
| medium | 2.54 ms | 2.49 ms | 6.58 ms | **0.39x** (61% faster) |
| complex | 3.74 ms | 3.66 ms | 6.63 ms | **0.56x** (44% faster) |

The February run had the warmUp variant at 10.68 ms for `complex` and explained it as
PropertyAccessorCache overhead on deeply nested expressions. It no longer reproduces —
warmUp and plain are within noise of each other — so that explanation is withdrawn rather
than carried forward.

### OCL Summary

- **Parsing:** Fennec is 100x–100,000x faster (ANTLR4 vs Eclipse pivot/Xtext overhead)
- **Evaluation:** Fennec is 11–61% faster
- **Cache:** Provides massive speedup for repeated expressions (the common case in EMF delegates)

---

## 2. QVT-O Benchmark (Fennec vs Eclipse QVT-O)

**Test:** `org.eclipse.fennec.m2x.qvto.benchmark.QvtoPerformanceBenchmarkTest`
**Parameters:** 500 JIT warmup iterations, 200 measurement iterations, 100 source model elements
**Transformation:** Source-to-target mapping with attribute copying, collection operations, and resolve

### Variants

| Variant | Description |
|---------|-------------|
| **Plain** | No OCL expression cache |
| **Cache** | OCL LRU expression cache enabled |
| **Cache+WU** | Cache + PropertyAccessorCache pre-warmed |
| **Eclipse** | Eclipse QVT-O (LPG parser), standalone via Gecko QVT-O |

### 2.1 Parse Performance

| Metric | Fennec Plain | Fennec Cache | Fennec Cache+WU | Eclipse | Best Fennec/Eclipse |
|--------|-------------|-------------|-----------------|---------|---------------------|
| Parse (200 iter) | 81.09 ms | 77.20 ms | 76.89 ms | 162.02 ms | **0.47x** (2.1x faster) |

### 2.2 Execution Performance

| Metric | Fennec Plain | Fennec Cache | Fennec Cache+WU | Eclipse | Best Fennec/Eclipse |
|--------|-------------|-------------|-----------------|---------|---------------------|
| Execute (200 iter × 100 elem) | 167.67 ms | 166.55 ms | 171.96 ms | 173.98 ms | **0.96x** (4% faster) |
| ns/mapping | 8.384 ns | 8.327 ns | 8.598 ns | 8.699 ns | |

### 2.3 Parse + Execute (End-to-End)

| Metric | Fennec Plain | Fennec Cache | Fennec Cache+WU | Eclipse | Best Fennec/Eclipse |
|--------|-------------|-------------|-----------------|---------|---------------------|
| Parse+Exec (200 iter × 100 elem) | 287.23 ms | 265.57 ms | 279.92 ms | 480.33 ms | **0.55x** (1.8x faster) |

### QVT-O Summary

- **Parsing:** Fennec is **2.1x faster** (ANTLR4 vs LPG)
- **Execution:** at parity — 4% faster with cache, which is inside run-to-run variation
- **End-to-End:** Fennec is **1.8x faster** overall

Execution is the one place where the two are level. The end-to-end advantage comes from
parsing, and it moves between runs mostly with the Eclipse side (593.65 ms in February,
480.33 ms now).

---

## 3. Implemented Optimizations

The following optimizations were applied to achieve the above results:

### OCL Engine

| # | Optimization | Impact |
|---|-------------|--------|
| 1 | LRU expression cache (String → parsed OclExpression) | Eliminates re-parsing for repeated expressions |
| 2 | PropertyAccessorCache warmUp for EPackages | Pre-caches EStructuralFeature reflective access |
| 3 | **Hashed unique collections** — `OclSet` backed by `LinkedHashMap`, `OclOrderedSet` by a key set, both keyed on `OclEqualityUtil.lookupKey` | O(1) instead of O(n) per `contains`/`add`. Building a set was quadratic: `Person.allInstances()->select(...)` over 50 000 instances took 10.9 s per evaluation, now 52 ms |

### QVT-O Engine

| # | Optimization | Affected File | Impact |
|---|-------------|---------------|--------|
| 1 | **Trace-Lookup Index** — 3 HashMap indices (mapping→key→record, source→records, result→records) | `QvtoTraceManager.java` | O(1) vs O(n) per resolve/inhibition check |
| 2 | **Operation-Index** — lazy name→operations HashMap + cached moduleClass | `QvtoOperationResolver.java` | O(1) vs O(m×n) per operation dispatch |
| 3 | **Scope-Snapshot-Cache** — dirty-flag for `allVisibleVariables()` | `QvtoEvalEnvironment.java` | Avoids LinkedHashMap rebuild per OCL delegation |
| 4 | **Variable-Fast-Path** — `lookup()`/`assign()`/`contains()` check current scope first | `QvtoEvalEnvironment.java` | O(1) for >90% of variable accesses |
| 5 | **Implicit Property Resolution** — single scope walk for `_objectExp`/`result`/`self` | `QvtoEvalEnvironment.java` | 1 walk vs 3 per implicit assignment |
| 6 | **Iterator Scope Pool** — reuse HashMap instances in `pushScope()`/`popScope()` | `QvtoEvalEnvironment.java` | Avoids allocation in tight iterator loops |

---

## 4. How to Reproduce

No setup is needed. The Eclipse bundles that Fennec is measured against are fetched into the
benchmark projects' `lib/` folders by the Gradle build; `gradle/benchmark-dependencies.gradle`
pins the versions and explains where they come from.

```bash
cd /opt/git/m2m/workspace

# Both comparisons against Eclipse
./gradlew benchmarkTest

# Or one at a time
./gradlew org.eclipse.fennec.m2x.ocl.benchmark:benchmarkTest
./gradlew org.eclipse.fennec.m2x.qvto.benchmark:benchmarkTest
```

The comparisons carry `@Tag("benchmark")` and no workflow runs them — their numbers are
relative to the machine. Our own performance tests (`@Tag("perf")`, task `perfTest`) do run
in CI.

Results are printed to stdout and saved in JUnit XML format under:
- `org.eclipse.fennec.m2x.ocl.benchmark/generated/test-reports/benchmarkTest/`
- `org.eclipse.fennec.m2x.qvto.benchmark/generated/test-reports/benchmarkTest/`
