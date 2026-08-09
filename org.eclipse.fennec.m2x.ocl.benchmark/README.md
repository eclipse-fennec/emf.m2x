# Fennec OCL Benchmark

Correctness comparison and performance benchmarks: **Fennec OCL vs Eclipse OCL Classic**.

## Prerequisites

None. The Eclipse OCL Classic bundles this benchmark measures against are fetched into
`lib/` by the Gradle build (see `gradle/benchmark-dependencies.gradle`), which is also where
the exact versions are pinned. They are not checked in.

They come from the Eclipse simultaneous release p2 repository, because Eclipse OCL was last
published to a Maven repository in 2014 and `lpg.runtime.java` only ever shipped through
Orbit. They are fetched as files rather than declared as a bnd repository on purpose: a bnd
repository applies to the whole workspace, and that p2 site also carries JUnit 6, which would
outrank the JUnit the shared `fennecTest` library resolves with `version=latest`.

Building with `--offline` leaves this project out as long as `lib/` is empty.

## Running the Benchmarks

```bash
cd /opt/git/m2m/workspace

# Run OCL performance benchmarks
./gradlew org.eclipse.fennec.m2x.ocl.benchmark:perfTest
```

This runs only tests annotated with `@Tag("perf")`.

`OclCorrectnessComparisonTest` carries no `perf` tag, so its 57 differential checks against
Eclipse OCL run as part of the ordinary `./gradlew build`.

## Test Classes

| Class | Description |
|-------|-------------|
| `OclPerformanceBenchmarkTest` | Parse, eval, and parse+eval performance comparison (simple/medium/complex expressions) |
| `OclCorrectnessComparisonTest` | Correctness validation: Fennec vs Eclipse OCL Classic results |
| `AbstractComparisonTest` | Shared test infrastructure |

## Results

Results are printed to stdout and saved as JUnit XML under:
```
generated/test-reports/perfTest/
```

See `workspace/docs/benchmark-results.md` for latest benchmark results.
