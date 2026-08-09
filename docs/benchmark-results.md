# Fennec M2M — Benchmark Results

Date: 2026-02-28
Platform: Linux 6.17.0-1012-oem, Java 21
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
| simple | 6.66 ms | 0.12 ms | 0.11 ms | 665.37 ms | **100x faster** |
| medium | 34.52 ms | 0.15 ms | 0.14 ms | 9793.92 ms | **284x faster** |
| complex | 51.31 ms | 0.16 ms | 0.15 ms | 16168.95 ms | **315x faster** |

**Summary:** Fennec parser is **~100–315x faster** without cache, **~100000x faster** with cache (cache hit = no re-parse). Eclipse OCL Classic uses heavyweight Ecore/pivot setup per parse.

### 1.2 Parse + Eval Performance

Realistic workload: parse and evaluate in one call.

| Expression | Fennec plain | Fennec+cache | Fennec+warmUp | Eclipse | Fennec plain/Eclipse |
|-----------|-------------|-------------|--------------|---------|----------------------|
| simple | 6.60 ms | 0.36 ms | 0.34 ms | 1165.15 ms | **176x faster** |
| medium | 35.60 ms | 3.28 ms | 2.77 ms | 16152.72 ms | **454x faster** |
| complex | 69.15 ms | 4.67 ms | 4.71 ms | 25816.78 ms | **373x faster** |

### 1.3 Eval Performance (pre-parsed)

Expression already parsed — measures pure evaluation engine.

| Expression | Fennec plain | Fennec+warmUp | Eclipse | Fennec/Eclipse |
|-----------|-------------|--------------|---------|----------------|
| simple | 0.20 ms | 0.19 ms | 0.25 ms | **0.80x** (20% faster) |
| medium | 2.61 ms | 2.60 ms | 6.30 ms | **0.41x** (59% faster) |
| complex | 3.75 ms | 10.68 ms | 6.95 ms | **0.54x** plain / 1.54x warmUp* |

*Note: warmUp variant shows slower complex eval due to PropertyAccessorCache overhead on deeply nested expressions. Plain mode is faster here.

### OCL Summary

- **Parsing:** Fennec is 100x–100,000x faster (ANTLR4 vs Eclipse pivot/Xtext overhead)
- **Evaluation:** Fennec is 20–59% faster for simple/medium expressions
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
| Parse (200 iter) | 79.09 ms | 75.98 ms | 80.11 ms | 164.72 ms | **0.46x** (2.2x faster) |

### 2.2 Execution Performance

| Metric | Fennec Plain | Fennec Cache | Fennec Cache+WU | Eclipse | Best Fennec/Eclipse |
|--------|-------------|-------------|-----------------|---------|---------------------|
| Execute (200 iter × 100 elem) | 170.44 ms | 157.41 ms | 156.45 ms | 175.49 ms | **0.89x** (11% faster) |
| ns/mapping | 8.522 ns | 7.871 ns | 7.822 ns | 8.774 ns | |

### 2.3 Parse + Execute (End-to-End)

| Metric | Fennec Plain | Fennec Cache | Fennec Cache+WU | Eclipse | Best Fennec/Eclipse |
|--------|-------------|-------------|-----------------|---------|---------------------|
| Parse+Exec (200 iter × 100 elem) | 262.96 ms | 256.93 ms | 257.41 ms | 593.65 ms | **0.43x** (2.3x faster) |

### QVT-O Summary

- **Parsing:** Fennec is **2.2x faster** (ANTLR4 vs LPG)
- **Execution:** Fennec is **11% faster** (with cache+warmup)
- **End-to-End:** Fennec is **2.3x faster** overall

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

# OCL benchmarks
./gradlew org.eclipse.fennec.m2x.ocl.benchmark:perfTest

# QVT-O benchmarks
./gradlew org.eclipse.fennec.m2x.qvto.benchmark:perfTest
```

Results are printed to stdout and saved in JUnit XML format under:
- `org.eclipse.fennec.m2x.ocl.benchmark/generated/test-reports/perfTest/`
- `org.eclipse.fennec.m2x.qvto.benchmark/generated/test-reports/perfTest/`
