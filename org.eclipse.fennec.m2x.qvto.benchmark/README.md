# Fennec QVT-O Benchmark

Performance benchmarks: **Fennec QVT-O vs Eclipse QVT-O**.

## Prerequisites

The benchmark requires Eclipse QVT-O and platform JARs that are **not checked into the repository**.
They must be provided locally as symlinks in the `lib/` directory.

### Required JARs

| Symlink (in `lib/`) | Source (in `/opt/git/m2m/tools/`) |
|----------------------|-----------------------------------|
| `lpg.runtime.java_2.0.17.jar` | `lpg.runtime.java_2.0.17.v201004271640.jar` |
| `org.eclipse.ocl_3.22.0.jar` | `org.eclipse.ocl_3.22.0.v20240902-1518.jar` |
| `org.eclipse.ocl.common_1.22.0.jar` | `org.eclipse.ocl.common_1.22.0.v20240902-1518.jar` |
| `org.eclipse.ocl.ecore_3.22.0.jar` | `org.eclipse.ocl.ecore_3.22.0.v20240902-1518.jar` |
| `org.eclipse.m2m.qvt.oml_3.11.0.jar` | `org.eclipse.m2m.qvt.oml_3.11.0.v20251124-1059.jar` |
| `org.eclipse.m2m.qvt.oml.common_3.11.0.jar` | `org.eclipse.m2m.qvt.oml.common_3.11.0.v20251124-1059.jar` |
| `org.eclipse.m2m.qvt.oml.cst.parser_3.11.0.jar` | `org.eclipse.m2m.qvt.oml.cst.parser_3.11.0.v20251124-1059.jar` |
| `org.eclipse.m2m.qvt.oml.ecore.imperativeocl_3.11.0.jar` | `org.eclipse.m2m.qvt.oml.ecore.imperativeocl_3.11.0.v20251124-1059.jar` |
| `org.eclipse.m2m.qvt.oml.emf.util_3.11.0.jar` | `org.eclipse.m2m.qvt.oml.emf.util_3.11.0.v20251124-1059.jar` |
| `org.eclipse.m2m.qvt.oml.ocl_3.11.0.jar` | `org.eclipse.m2m.qvt.oml.ocl_3.11.0.v20251124-1059.jar` |
| `org.eclipse.m2m.qvt.oml.runtime_3.11.0.jar` | `org.eclipse.m2m.qvt.oml.runtime_3.11.0.v20251124-1059.jar` |
| `org.eclipse.equinox.common_3.20.300.jar` | `org.eclipse.equinox.common_3.20.300.v20251111-0312.jar` |
| `org.eclipse.equinox.registry_3.12.600.jar` | `org.eclipse.equinox.registry_3.12.600.v20250906-0651.jar` |
| `org.eclipse.equinox.supplement_1.12.100.jar` | `org.eclipse.equinox.supplement-1.12.100.jar` |
| `org.eclipse.core.runtime_3.34.100.jar` | `org.eclipse.core.runtime_3.34.100.v20251111-1421.jar` |
| `org.eclipse.core.jobs_3.15.700.jar` | `org.eclipse.core.jobs_3.15.700.v20250725-1147.jar` |
| `org.eclipse.core.contenttype_3.9.800.jar` | `org.eclipse.core.contenttype_3.9.800.v20251105-1620.jar` |
| `org.eclipse.core.resources_3.23.100.jar` | `org.eclipse.core.resources_3.23.100.v20251106-1705.jar` |
| `org.eclipse.core.filesystem_1.11.400.jar` | `org.eclipse.core.filesystem_1.11.400.v20251107-0507.jar` |
| `org.eclipse.core.expressions_3.9.500.jar` | `org.eclipse.core.expressions_3.9.500.v20250608-0434.jar` |
| `org.osgi.service.prefs_1.1.2.jar` | `org.osgi.service.prefs_1.1.2.202109301733.jar` |

### Setup

Create the symlinks from the project's `lib/` directory:

```bash
cd /opt/git/m2m/workspace/org.eclipse.fennec.m2x.qvto.benchmark/lib

# Eclipse OCL Classic
ln -s /opt/git/m2m/tools/lpg.runtime.java_2.0.17.v201004271640.jar lpg.runtime.java_2.0.17.jar
ln -s /opt/git/m2m/tools/org.eclipse.ocl_3.22.0.v20240902-1518.jar org.eclipse.ocl_3.22.0.jar
ln -s /opt/git/m2m/tools/org.eclipse.ocl.common_1.22.0.v20240902-1518.jar org.eclipse.ocl.common_1.22.0.jar
ln -s /opt/git/m2m/tools/org.eclipse.ocl.ecore_3.22.0.v20240902-1518.jar org.eclipse.ocl.ecore_3.22.0.jar

# Eclipse QVT-O
ln -s /opt/git/m2m/tools/org.eclipse.m2m.qvt.oml_3.11.0.v20251124-1059.jar org.eclipse.m2m.qvt.oml_3.11.0.jar
ln -s /opt/git/m2m/tools/org.eclipse.m2m.qvt.oml.common_3.11.0.v20251124-1059.jar org.eclipse.m2m.qvt.oml.common_3.11.0.jar
ln -s /opt/git/m2m/tools/org.eclipse.m2m.qvt.oml.cst.parser_3.11.0.v20251124-1059.jar org.eclipse.m2m.qvt.oml.cst.parser_3.11.0.jar
ln -s /opt/git/m2m/tools/org.eclipse.m2m.qvt.oml.ecore.imperativeocl_3.11.0.v20251124-1059.jar org.eclipse.m2m.qvt.oml.ecore.imperativeocl_3.11.0.jar
ln -s /opt/git/m2m/tools/org.eclipse.m2m.qvt.oml.emf.util_3.11.0.v20251124-1059.jar org.eclipse.m2m.qvt.oml.emf.util_3.11.0.jar
ln -s /opt/git/m2m/tools/org.eclipse.m2m.qvt.oml.ocl_3.11.0.v20251124-1059.jar org.eclipse.m2m.qvt.oml.ocl_3.11.0.jar
ln -s /opt/git/m2m/tools/org.eclipse.m2m.qvt.oml.runtime_3.11.0.v20251124-1059.jar org.eclipse.m2m.qvt.oml.runtime_3.11.0.jar

# Eclipse Platform / Equinox
ln -s /opt/git/m2m/tools/org.eclipse.equinox.common_3.20.300.v20251111-0312.jar org.eclipse.equinox.common_3.20.300.jar
ln -s /opt/git/m2m/tools/org.eclipse.equinox.registry_3.12.600.v20250906-0651.jar org.eclipse.equinox.registry_3.12.600.jar
ln -s /opt/git/m2m/tools/org.eclipse.equinox.supplement-1.12.100.jar org.eclipse.equinox.supplement_1.12.100.jar
ln -s /opt/git/m2m/tools/org.eclipse.core.runtime_3.34.100.v20251111-1421.jar org.eclipse.core.runtime_3.34.100.jar
ln -s /opt/git/m2m/tools/org.eclipse.core.jobs_3.15.700.v20250725-1147.jar org.eclipse.core.jobs_3.15.700.jar
ln -s /opt/git/m2m/tools/org.eclipse.core.contenttype_3.9.800.v20251105-1620.jar org.eclipse.core.contenttype_3.9.800.jar
ln -s /opt/git/m2m/tools/org.eclipse.core.resources_3.23.100.v20251106-1705.jar org.eclipse.core.resources_3.23.100.jar
ln -s /opt/git/m2m/tools/org.eclipse.core.filesystem_1.11.400.v20251107-0507.jar org.eclipse.core.filesystem_1.11.400.jar
ln -s /opt/git/m2m/tools/org.eclipse.core.expressions_3.9.500.v20250608-0434.jar org.eclipse.core.expressions_3.9.500.jar
ln -s /opt/git/m2m/tools/org.osgi.service.prefs_1.1.2.202109301733.jar org.osgi.service.prefs_1.1.2.jar
```

The JARs can be downloaded from:
- **Eclipse QVT-O 3.11.0** — [Eclipse M2M QVT-O Downloads](https://projects.eclipse.org/projects/modeling.mmt.qvt-oml/downloads) or [Gecko QVT-O](https://github.com/geckoprojects-org/org.gecko.qvto)
- **Eclipse OCL Classic 3.22.0** — [Eclipse OCL Downloads](https://projects.eclipse.org/projects/modeling.mdt.ocl/downloads)
- **Eclipse Platform 4.35** — [Eclipse Downloads](https://download.eclipse.org/eclipse/downloads/)
- **LPG Runtime 2.0.17** — bundled with Eclipse OCL Classic

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
