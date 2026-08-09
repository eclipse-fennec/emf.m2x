# Fennec QVT-O Benchmark

Performance benchmarks: **Fennec QVT-O vs Eclipse QVT-O**.

## Prerequisites

None. The Eclipse QVT-O, Eclipse OCL Classic and platform bundles this benchmark measures
against are fetched into `lib/` by the Gradle build (see `gradle/benchmark-dependencies.gradle`),
which is also where the exact versions are pinned. They are not checked in.

They come from the Eclipse simultaneous release p2 repository, because none of them is
published to Maven — Eclipse OCL was last released to a Maven repository in 2014, Eclipse
QVT-O never was, and `lpg.runtime.java` only ever shipped through Orbit. They are fetched as
files rather than declared as a bnd repository on purpose: a bnd repository applies to the
whole workspace, and that p2 site also carries JUnit 6, which would outrank the JUnit the
shared `fennecTest` library resolves with `version=latest`.

Building with `--offline` leaves this project out as long as `lib/` is empty.

## Running the Benchmarks

```bash
cd /opt/git/m2m/workspace

# Run QVT-O performance benchmarks
./gradlew org.eclipse.fennec.m2x.qvto.benchmark:perfTest
```

This runs only tests annotated with `@Tag("perf")`.

## Test Classes

| Class | Description |
|-------|-------------|
| `QvtoPerformanceBenchmarkTest` | Parse, execute, and E2E performance comparison (100 model elements, 200 iterations) |

## Results

Results are printed to stdout and saved as JUnit XML under:
```
generated/test-reports/perfTest/
```

See `workspace/docs/benchmark-results.md` for latest benchmark results.
