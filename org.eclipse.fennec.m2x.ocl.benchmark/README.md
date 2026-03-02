# Fennec OCL Benchmark

Correctness comparison and performance benchmarks: **Fennec OCL vs Eclipse OCL Classic**.

## Prerequisites

The benchmark requires Eclipse OCL Classic JARs that are **not checked into the repository**.
They must be provided locally as symlinks in the `lib/` directory.

### Required JARs

| Symlink (in `lib/`) | Source (in `/opt/git/m2m/tools/`) |
|----------------------|-----------------------------------|
| `lpg.runtime.java_2.0.17.jar` | `lpg.runtime.java_2.0.17.v201004271640.jar` |
| `org.eclipse.ocl_3.22.0.jar` | `org.eclipse.ocl_3.22.0.v20240902-1518.jar` |
| `org.eclipse.ocl.common_1.22.0.jar` | `org.eclipse.ocl.common_1.22.0.v20240902-1518.jar` |
| `org.eclipse.ocl.ecore_3.22.0.jar` | `org.eclipse.ocl.ecore_3.22.0.v20240902-1518.jar` |

### Setup

Create the symlinks from the project's `lib/` directory:

```bash
cd /opt/git/m2m/workspace/org.eclipse.fennec.m2x.ocl.benchmark/lib

ln -s /opt/git/m2m/tools/lpg.runtime.java_2.0.17.v201004271640.jar lpg.runtime.java_2.0.17.jar
ln -s /opt/git/m2m/tools/org.eclipse.ocl_3.22.0.v20240902-1518.jar org.eclipse.ocl_3.22.0.jar
ln -s /opt/git/m2m/tools/org.eclipse.ocl.common_1.22.0.v20240902-1518.jar org.eclipse.ocl.common_1.22.0.jar
ln -s /opt/git/m2m/tools/org.eclipse.ocl.ecore_3.22.0.v20240902-1518.jar org.eclipse.ocl.ecore_3.22.0.jar
```

The JARs can be downloaded from:
- **Eclipse OCL Classic 3.22.0** — [Eclipse OCL Downloads](https://projects.eclipse.org/projects/modeling.mdt.ocl/downloads)
- **LPG Runtime 2.0.17** — bundled with Eclipse OCL Classic

## Running the Benchmarks

```bash
cd /opt/git/m2m/workspace

# Run OCL performance benchmarks
./gradlew org.eclipse.fennec.m2x.ocl.benchmark:perfTest
```

This runs only tests annotated with `@Tag("perf")`.

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
