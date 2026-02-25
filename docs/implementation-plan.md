# Fennec M2M Implementation Plan

> Part of the [Development Guideline](development-guideline.md). See also: [Architecture](architecture.md), [Design Decisions](design-decisions.md), [OCL Architecture](ocl-architecture.md)

---

> **SPEC-FIRST WORKFLOW** — bei JEDEM Task befolgen:
> 1. ZUERST die OMG-Spec lesen (OCL v2.4/v2.5, QVT-O v1.3, QVT-D v1.3, MOFM2T v1.0)
> 2. Tests gegen die Spec schreiben — das erwartete Ergebnis kommt aus der Spec
> 3. Tests laufen lassen — Failures sind Implementierungslücken, NICHT Testfehler
> 4. Implementierung fixen — bis die Tests grün sind
> 5. NIEMALS Tests an die Implementierung anpassen, um sie grün zu machen
> 6. Bei Unklarheit: Rückfrage an den User

---

## 14. Implementation Order (Updated)

### Phase 1: OCL (Foundation) — ✅ Complete

**Status:** ✅ Complete — 4108 tests, 0 failures, spec-verified against OCL v2.4/v2.5. All spec-conformance gaps resolved (D-1..D-8, F-1..F-7, T-1..T-15). See [archive/ocl-spec-conformance-gaps.md](archive/ocl-spec-conformance-gaps.md) and [archive/ocl-engine-gaps.md](archive/ocl-engine-gaps.md) for details.

**Deferred to Phase 5 (LSP):** GAP-2 (@pre/postconditions), GAP-3 (validate type-checker).

| Step | Module | Status | Description |
|------|--------|--------|-------------|
| 1.1 | `ocl.model` | **Done** | OCL EMF metamodel (48 EClasses + 2 EEnums), pure model project generating into `src/`. nsURI: `http://www.eclipse.org/fennec/m2x/ocl/1.0`. Based on `EssentialOCL.emof` blueprint + v2.5 extensions (MapType, MapLiteralExp, isSafe). |
| 1.2 | `ocl.api` | **Done** | `OclEngine` interface (parse, evaluate with `OclContext`, validate, register/unregister extensions), `OclContext` record (self + extent + variables), `OclEvaluationOptions`, `OclInvalid` sentinel, `OclParseException`, extension interfaces (`OclOperationProvider`, `CompleteOclContribution`, `OclStandardLibraryContribution`), `OclModelExtent` for `allInstances()`. |
| 1.3 | `ocl.parser` | **Done** | ANTLR4 combined grammar (`Ocl.g4`) for Essential OCL + Complete OCL, bnd `-generate:` with `system=` for ANTLR4 tool invocation, `OclAstBuilder` visitor (parse tree → EMF AST), `OclEnvironment` for nested scopes, `OclParserSupport` entry point, `OclErrorListener` with `Resource.Diagnostic` error collection. Grammar bundled in JAR for downstream import. |
| 1.4 | `ocl.engine` | **Done** | `OclEvaluator` (recursive `OclSwitch<Object>` interpreter), `OclEvalEnvironment` (chain-of-scopes runtime bindings), `OclStdlib` (switch-based dispatch for OclAny/Boolean/Integer/Real/String/Collection/Map operations), `OclEngineImpl` (facade with 3-level operation dispatch: Ecore eInvoke → stdlib → custom providers), EMF delegates (`OclInvocationDelegateFactory`, `OclSettingDelegateFactory` extending `BasicSettingDelegate.Stateless`, `OclValidationDelegateFactory`) under delegate URI `http://www.eclipse.org/fennec/m2x/ocl/1.0`, `OclEngineComponent` DS adapter with `@Activate`/`@Deactivate` delegate lifecycle. Model extended: `name: String` on `OperationCallExp`. |
| 1.5 | `ocl.tests` | **4108 tests** | Unit tests, delegate tests, spec compliance tests, collection operations, iterators, stdlib, null/invalid handling, type system, EMF delegates. Spec-conformance verified 2026-02-21. |

### Phase 2: QVT-Operational

| Step | Module | Status | Description |
|------|--------|--------|-------------|
| 2.1 | `qvto.model` | **Done** | QVT-O EMF metamodel with 3 EPackages: `imperativeocl` (22 EClasses + 1 EEnum), `qvtoperational` (22 EClasses + 2 EEnums), `trace` (11 EClasses + 1 EEnum) = 59 classifiers total. Extends OCL model. Imperative constructs (AssignExp, WhileExp, ForExp, SwitchExp, TryExp, etc.), transformation structure (Module, MappingOperation, Helper, Library), and trace records. EMF codegen adaptations: `VarParameter` extends only `EParameter` (not `Variable`), `Module` extends only `EPackage` (not `EClass`+`EPackage`). |
| 2.2 | `qvto.api` | **Done** | `QvtoEngine` interface (parse/execute), `QvtoConfiguration` (Builder), `QvtoExecutionContext`/`Result` records, `QvtoEvaluationOptions`, `QvtoModelExtent`/`BasicQvtoModelExtent`, `QvtoBlackboxLibrary`/`QvtoBlackboxOperation` (D24), `QvtoUnitResolver`/`QvtoUnit` (D27), `QvtoTransformationContribution` (whiteboard), `QvtoParseException`. |
| 2.3 | `qvto.parser` | **Done** | ANTLR4 grammar (`QvtOperational.g4`, imports `Ocl.g4`), `QvtoParserSupport` entry point, `QvtoAstBuilder` visitor. Supports: transformations, helpers/queries, mappings (with when/where/init/end/inherits), imperative statements, resolve expressions, modeltype/import. |
| 2.4 | `qvto.engine` | **Phase A+B+C Done** | `QvtoEvaluator` (3-level switch: ImperativeOcl → QvtOperational → OCL), `QvtoEvalEnvironment` (scope stack), `QvtoOperationProvider` (D25 mutual recursion), `QvtoExtentManager`, `QvtoTraceManager` (lightweight records + EMF Trace), `QvtoControlFlowException` (sealed). Phase A: imperative statements. Phase B: MappingOperation, ObjectExp, MappingCallExp, resolve expressions. Phase C: late resolve (deferred tasks), EMF Trace model export. |
| 2.5 | `qvto.tests` | **Done** | 862 tests (0 failures, 0 @Disabled, 1 @Tag("perf")). Parse tests, engine tests, E2E integration tests. Spec-conformance Phase 0–10 complete. |
| 2.6 | `qvto.tests` | **Done** | Comprehensive QVT-O test coverage. 862 tests across ~60 test classes. See §15.8 and `docs/qvto-test-plan.md`. |

### Phase 3: Acceleo/MOFM2T (with OCL expressions + cherry-picked extensions)

| Step | Module | Description |
|------|--------|-------------|
| 3.1 | `m2t.model` | Template model: Module, Template, Query, ForBlock, IfBlock (+ elseif), LetBlock, FileBlock, ProtectedAreaBlock. OCL expressions embedded via `ocl.model` types. |
| 3.2 | `m2t.api` | `M2tEngine` interface, `M2tService` extension interface, `M2tModuleResolver`, `M2tGenerationOptions`, public API |
| 3.3 | `m2t.parser` | ANTLR4 grammar (`Mofm2t.g4`, imports `Ocl.g4` for embedded OCL expressions) |
| 3.4 | `m2t.engine` | Template evaluator (uses OCL engine with lenient mode by default), file writer with encoding, protected area preservation, post() support, Java service integration, DS component |
| 3.5 | `m2t.tests` | Template generation tests, MOFM2T spec compliance tests |

### Phase 4: QVT-Declarative (Relations + Core)

| Step | Module | Description |
|------|--------|-------------|
| 4.1 | `qvtd.model` | QVT-R metamodel (Relation, RelationDomain, DomainPattern, Key, Template) + QVT-C metamodel (CoreModel, Area, Mapping, Guard, Assignment) |
| 4.2 | `qvtd.api` | `QvtdEngine` interface, `QvtdKeyProvider` extension interface, public API |
| 4.3 | `qvtd.parser` | ANTLR4 grammar (`QvtRelations.g4`, imports `Ocl.g4`). QVT-Core is primarily a compilation target, no separate parser unless needed. |
| 4.4 | `qvtd.engine` | Declarative transformation engine: relation matching, pattern matching, domain enforcement, `checkonly`/`enforce` semantics, optional QVTr→QVTc compilation |
| 4.5 | `qvtd.tests` | Relation tests, domain tests, pattern matching tests |

### Phase 5: Language Servers (Future)

| Step | Module | Description |
|------|--------|-------------|
| 5.1 | `ocl.lsp` | OCL Language Server (LSP4J) |
| 5.2 | `qvto.lsp` | QVT-O Language Server |
| 5.3 | `qvtd.lsp` | QVT-R Language Server |
| 5.4 | `m2t.lsp` | MOFM2T Language Server |
| 5.5 | OCL GAP-2 | `@pre` postcondition support (pre-state snapshot mechanism, MessageExp, oclIsNew) |
| 5.6 | OCL GAP-3 | `validate()` implementation (static type-checking visitor over AST) |

---

### 14.1.6 Security Hardening

Done. See [OCL Architecture §10](ocl-architecture.md#10-security-hardening).

---

### 14.1.7 Test Coverage Gaps

See [OCL Architecture §12.6](ocl-architecture.md#126-coverage-gaps-dr-d25).

### 14.1.8 Performance & Thread-Safety Test Plan

See [OCL Architecture §11.5](ocl-architecture.md#115-benchmark-test-plan-p1p17).

---

## 15. Testing Strategy

### 15.1 Plain JUnit 5 First

**All tests that can run without OSGi must be plain JUnit 5 tests.** This includes:

- Parser tests (grammar correctness, error reporting)
- Evaluator tests (expression evaluation, standard library)
- EMF delegate tests (validation/setting/invocation delegates)
- Spec compliance tests
- Integration tests (parse -> evaluate pipeline)

Only create a **separate OSGi test project** (`*.itest`) when the test **requires an OSGi container**:
- DS component wiring tests
- Whiteboard extension discovery tests
- emf.osgi EPackageConfigurator integration tests

### 15.2 Test Project Structure

```
workspace/
├── org.eclipse.fennec.m2x.ocl.tests/       # Plain JUnit 5 tests (parser, engine, delegates)
├── org.eclipse.fennec.m2x.ocl.itest/       # OSGi integration tests (DS, whiteboard) - only if needed
├── org.eclipse.fennec.m2x.qvto.tests/      # Plain JUnit 5 tests (QVT-O)
├── org.eclipse.fennec.m2x.qvto.itest/      # OSGi integration tests - only if needed
├── org.eclipse.fennec.m2x.qvtd.tests/      # Plain JUnit 5 tests (QVT-R/QVT-C)
├── org.eclipse.fennec.m2x.qvtd.itest/      # OSGi integration tests - only if needed
├── org.eclipse.fennec.m2x.m2t.tests/       # Plain JUnit 5 tests
└── org.eclipse.fennec.m2x.m2t.itest/       # OSGi integration tests - only if needed
```

### 15.3 Test Categories

| Category | Type | Runner | Tag |
|----------|------|--------|-----|
| **Unit** | Individual classes/methods | JUnit 5 | - |
| **Integration** | Parse -> evaluate pipeline | JUnit 5 | - |
| **Spec compliance** | Examples from OCL/QVT/MOFM2T specs | JUnit 5 | `@Tag("spec")` |
| **Edge cases** | Error handling, boundary conditions | JUnit 5 | - |
| **Eclipse spec tests** | Migrated from Eclipse OCL/QVTo/Acceleo test suites | JUnit 5 (adapted) | `@Tag("spec")` |
| **Performance** | Benchmarks comparing Fennec vs Eclipse implementations | JUnit 5 + JMH | `@Tag("perf")` |
| **OSGi integration** | DS wiring, whiteboard, emf.osgi | OSGi test (separate `*.itest` project) | - |

### 15.4 Migrating Eclipse Spec Tests

Existing spec-related tests from Eclipse OCL, QVTo, and Acceleo repositories should be:

1. Identified and extracted from the Eclipse test projects
2. Migrated to **JUnit 5** (from JUnit 3/4)
3. Adapted to use our API instead of Eclipse's
4. Placed in the corresponding `*.tests` project
5. Tagged with `@Tag("spec")` for filtering

### 15.5 Test Model

See [OCL Architecture §12.2](ocl-architecture.md#122-test-model).

### 15.6 Test Naming

```java
@Test
void integerLiteral_parsesCorrectly() { ... }

@Test
@Tag("spec")
void specExample_7_3_3_invariant() { ... }  // Reference to spec section
```

### 15.7 Performance Testing (Fennec vs Eclipse)

For OCL-specific benchmark results and test plan, see
[OCL Architecture §11](ocl-architecture.md#11-performance).

For future phases (QVT-O, QVT-R, M2T), benchmark design will follow the same pattern:
both engines under identical conditions (standalone, no OSGi), shared test models,
System.nanoTime() loops with warmup (JMH for rigorous benchmarks later).

#### Test Project Dependencies

Performance test projects need the Eclipse implementations on their buildpath:

```bnd
# In org.eclipse.fennec.m2x.ocl.tests/bnd.bnd
-buildpath: \
    org.eclipse.fennec.m2x.ocl.engine;version=latest,\
    org.eclipse.fennec.m2x.ocl.parser;version=latest,\
    org.eclipse.ocl.pivot;version=latest,\
    org.eclipse.ocl.xtext.essentialocl;version=latest,\
    ...
```

The Eclipse OCL/QVTo/Acceleo jars must be available in the bnd repository (indexed from the `repositories/` folder or Maven Central).

### 15.8 QVT-O Test Coverage (Step 2.6)

> **Detailliertes Tracking:** Siehe [QVT-O Test Plan](qvto-test-plan.md) — Phase 0 (35 Failures fixen) + Phasen 1–8 (Spec-basierte Erweiterung, Ziel: ~950 Tests).

Current state: **862 tests (0 failures, 0 @Disabled, 1 @Tag("perf"))**. Includes programmatic AST tests + ~60 E2E integration test classes covering spec-conformance Phase 0–10. See [QVT-O Test Plan](qvto-test-plan.md) for detailed tracking.

#### 15.8.1 End-to-End Integration Tests

Parse `.qvto` source → build AST → execute transformation → verify output model. This tests the full pipeline (parser + engine together), unlike the current tests which construct AST nodes programmatically.

**Approach:**
- `.qvto` transformation files as test resources in `src/test/resources/`
- Ecore test models (source/target metamodels) as test resources
- JUnit 5 parameterized tests loading `.qvto` files by category
- Abstract base class `AbstractQvtoIntegrationTest` with parse+execute+verify pattern

**Categories:**
| Category | Description | Reference |
|----------|-------------|-----------|
| `e2e/mapping/` | Basic and nested mappings, when/where guards, init/end sections, inherits/merges/disjuncts | Eclipse `parserTestData/models/` |
| `e2e/helper/` | Helpers, queries, expression-body (`= expr`), contextual helpers | Eclipse `parserTestData/models/` |
| `e2e/resolve/` | resolve, resolveone, resolveIn, invresolve, late resolve | Eclipse `parserTestData/models/resolve_*` |
| `e2e/collection/` | Dict/List aliases, collection operations in imperative context | Eclipse `parserTestData/models/` |
| `e2e/controlflow/` | while, for, switch, compute, break/continue, try/catch | Eclipse `parserTestData/models/` |
| `e2e/object/` | ObjectExp, implicit/explicit population, intermediate classes | Eclipse `parserTestData/models/` |
| `e2e/trace/` | Trace model generation, trace-based resolve | Eclipse `parserTestData/models/` |
| `e2e/blackbox/` | Blackbox library integration | Eclipse `deployed/` |
| `e2e/complex/` | Multi-mapping transformations (e.g., Simpleuml_To_Rdb) | Eclipse classic examples |

#### 15.8.2 Spec-Oriented Tests

Systematic coverage of QVT-O v1.3 specification chapters, tagged with `@Tag("spec")`.

| Spec Section | Topic | Key Tests |
|-------------|-------|-----------|
| §8.1 | Module, Transformation, Library | Module structure, imports, access |
| §8.2 | ModelType, ModelParameter | in/inout/out binding, type matching |
| §8.3 | MappingOperation | Signature, guards, population, result |
| §8.4 | Helper, Query | Contextual, static, overloading |
| §8.5 | Constructor | Object initialization |
| §8.6 | Entry operation (main) | Transformation entry point |
| §8.7 | Resolve expressions | All resolve variants |
| §8.8 | Late resolve | Deferred execution semantics |
| §8.9 | Intermediate data | Intermediate classes/properties |
| §8.10 | Imperative expressions | All imperative constructs |
| §8.11 | Exception handling | Try/catch/raise |
| §8.12 | Blackbox | Library integration |

#### 15.8.3 Regression Tests

Derived from Eclipse QVT-O bug-fix tests (`bug*`, `scr*` directories). Selected based on relevance to Fennec's feature scope:

- Extract semantically interesting `.qvto` transformations from Eclipse's ~47 named bug/SCR test directories
- Adapt to Fennec API (no Eclipse platform dependencies)
- Tag with `@Tag("regression")`

#### 15.8.4 Target

| Metric | Current | Target | Status |
|--------|---------|--------|--------|
| Test classes | ~60 | ~50+ | ✅ |
| Test methods | 862 | ~500+ | ✅ |
| .qvto test files | 100+ | ~100+ | ✅ |
| End-to-end tests | 150+ | ~150+ | ✅ |
| Spec compliance tests | 80+ | ~80+ | ✅ |
| Regression tests | 0 | ~30+ |
