# Fennec M2M — Benchmark Results

Date: 2026-08-09, re-run 2026-08-28 (§5)
Platform: Linux 6.17.0-1030-oem / 6.17.0-1032-oem, Java 21
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

## 5. Re-run after the compiled-unit work (2026-08-28)

Same machine, same tests, same parameters, after #154–#158 (OCL types as `EClassifier`s, no
`ClassifierType` wrapper, scope instead of registry scan) and epic #135 (compiled units). The
question was whether any of it moved the standard path, and what the new path costs and saves.

### 5.1 The standard path — unchanged or slightly faster

| Metric | 2026-08-09 | 2026-08-28 | Change |
|---|---|---|---|
| QVT-O parse (200 iter, plain) | 81.09 ms | 67.33 ms | **−17 %** |
| QVT-O execute (200 × 100 elem, plain) | 167.67 ms | 159.38 ms | −5 % |
| QVT-O parse+exec (plain) | 287.23 ms | 240.42 ms | **−16 %** |
| QVT-O execute, Eclipse | 173.98 ms | 178.59 ms | +3 % (reference, run-to-run) |
| OCL parse complex (1000 iter, plain) | 50.89 ms | 52.55 ms | +3 % |
| OCL parse+eval complex (plain) | 70.30 ms | 59.35 ms | −16 % |
| OCL eval complex (pre-parsed, plain) | 3.74 ms | 3.60 ms | −4 % |
| OCL eval medium, Eclipse | 6.58 ms | 14.30 ms | run-to-run on the Eclipse side |

Parsing got faster — the plausible cause is #158: type names resolve in the declared scope
instead of a scan over the whole package registry. Execution is within noise of the previous run
in both directions. Nothing regressed.

### 5.2 The compiled-unit path — what it costs and what it saves

Tests: `QvtoCompiledUnitBenchmarkTest`, `QvtdCompiledUnitBenchmarkTest`,
`M2tCompiledUnitBenchmarkTest` (all `@Tag("benchmark")`). 200 iterations; QVT-O 100 elements,
QVT-R 100 books, M2T one class with 50 operations. Validation on load is **on**.

| | QVT-O | QVT-R | M2T |
|---|---|---|---|
| `parse()` × 200 | 72.22 ms | 9.87 ms | 17.90 ms |
| `compile()` × 200 | 170.99 ms (2.4×) | 28.19 ms (2.9×) | 35.34 ms (2.0×) |
| store + load, per round trip | 1.84 ms | 0.75 ms | 0.67 ms |
| prepare (once, no dependencies) | 1.43 ms | 0.44 ms | 0.45 ms |
| `execute(ast)` × 200 | 163.68 ms | 140.76 ms | 30.86 ms |
| `execute(prepared)` × 200 | 174.25 ms (1.06×) | 147.36 ms (1.05×) | 20.75 ms (**0.67×**) |

What the numbers say:

- **`compile()` costs two to three times `parse()`.** That is the satellite walk, the package
  entries with their `fp1` fingerprints, and the `m2x1` unit fingerprint. It is a one-time price
  per unit, paid where a unit is produced, not where it runs.
- **A store round trip is under 2 ms** for a transformation, under 1 ms for a template — XMI out
  and in, plus the full `UnitValidator` (integrity, manifest, closure, Ecore structure, size).
- **Repeated execution from a prepared context is at parity for QVT-O and QVT-R** (+5–6 %,
  inside run-to-run variation). The link phase `execute(ast)` runs on every call is cheap for a
  unit without imports, so there is nothing for prepare to save here; the saving prepare exists for
  — no resolver in Execute, one metamodel decision per pipeline — is not a per-call cost. The
  small plus comes from the context's registry chain (`UnitResourceSet`: given → global → copies)
  standing between `objectsOfType` and the package.
- **M2T is 20–33 % faster from the prepared context, in two runs.** This is not the prepared path
  being clever; it is the parsed path carrying weight it should not: at the time of measurement the
  engine kept source positions for every AST node of every module it had ever parsed, and releasing
  a module did not remove them, while a loaded module has no entries there at all. The positions
  have since moved into `M2tLinkRegistry`, whose `release(Module)` drops a module's entries — the
  numbers below predate that fix, so the gap they show is smaller today.

### 5.3 Reading

The compiled-unit work did not slow the standard path — parse got faster, execute did not move.
Compiled units are not a performance feature and were not built as one: they buy reproducibility,
a storable form and a resolver-free Execute. Their run-time cost is a one-time compile at two to
three times a parse and a sub-2-ms load; their run-time saving is zero for a unit without imports
and grows with the import closure that Execute no longer has to resolve.

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

# The compiled-unit path (§5.2) — the M2T and QVT-R ones live in the test bundles
./gradlew org.eclipse.fennec.m2x.m2t.tests:benchmarkTest org.eclipse.fennec.m2x.qvtd.tests:benchmarkTest

# Gradle treats an unchanged benchmark as up to date and will not run it again; force a
# second measurement with --rerun
./gradlew org.eclipse.fennec.m2x.m2t.tests:benchmarkTest --rerun
```

The comparisons carry `@Tag("benchmark")` and no workflow runs them — their numbers are
relative to the machine. Our own performance tests (`@Tag("perf")`, task `perfTest`) do run
in CI.

Results are printed to stdout and saved in JUnit XML format under:
- `org.eclipse.fennec.m2x.ocl.benchmark/generated/test-reports/benchmarkTest/`
- `org.eclipse.fennec.m2x.qvto.benchmark/generated/test-reports/benchmarkTest/`
