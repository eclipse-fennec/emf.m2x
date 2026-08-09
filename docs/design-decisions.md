# Fennec M2M Design Decisions

> Part of the [Development Guideline](development-guideline.md). See also: [OCL Architecture](ocl-architecture.md), [QVT-O Architecture](qvto-architecture.md)

---

## 16. Key Design Decisions

| # | Decision | Rationale |
|---|----------|-----------|
| D1 | EMF Ecore for metamodels (OCL types, expressions) | XMI serialization, UI building blocks, standard compliance |
| D2 | fennecEMF for code generation | OSGi-compatible, bnd-integrated, no Eclipse PDE needed |
| D3 | ANTLR4 for parsing | Best error recovery, largest community, proven in Acceleo/OSGi, grammar composition via `import` |
| D4 | Visitor pattern for evaluation | Clean separation of traversal and operation |
| D5 | `src-gen/` for all generated code | Clear separation from hand-written code |
| D6 | GenModel annotations in ecore files | Single source of truth, genmodel derived from ecore |
| D7 | `enableEMF` library for EMF buildpath | Consistent EMF dependency management via bnd |
| D8 | Two-phase parsing (ANTLR4 parse tree -> EMF AST) | Clean separation of syntax and semantics. Parser resolves types during AST construction for hot-path performance. |
| D9 | OSGi-optional (works without OSGi) | Pure Java core usable anywhere, OSGi DS adapter for service discovery |
| D10 | Whiteboard / single @Reference for extensions | QVT blackboxes, M2T services: whiteboard (MULTIPLE, DYNAMIC). OCL custom operations: single mandatory `@Reference` + target (see §12.2). Programmatic without OSGi |
| D11 | EMF delegate integration | OCL in EAnnotations for validation/setting/invocation delegates. See [OCL Architecture §8](ocl-architecture.md#8-emf-delegate-integration) |
| D12 | fennec emf.osgi compatibility | Automatic model registration, future delegate registry support |
| D13 | OCL v2.5 (backward compatible with v2.4) | v2.4 is the formal OMG spec; v2.5 features (Map, safe navigation, OclComparable) added without breaking v2.4 |
| D14 | Configurable OCL evaluation (strict/lenient) | Strict mode for validation, lenient mode for M2T. See [OCL Architecture §13.2](ocl-architecture.md#132-dr-d14-configurable-evaluation-strict--lenient) |
| D15 | MOFM2T v1.0 + cherry-picked extensions (not Acceleo) | Spec-compliant M2T with OCL expressions. Extensions: `post()`, `@main`, `elseif`, Java services. No AQL, no Acceleo compatibility. |
| D16 | No Pivot layer | Direct Ecore access, no intermediate AS. See [OCL Architecture §6.7](ocl-architecture.md#67-no-pivot-layer-d16) |
| D17 | QVT-D (Relations + Core) alongside QVT-O | Full QVT spec coverage: imperative (QVT-O) and declarative (QVT-R/C) paradigms |
| D18 | Performance benchmarks vs Eclipse (standalone) | Compare Fennec against Eclipse OCL/QVTo/Acceleo running standalone (no OSGi). Test small/large models, high-frequency calls. Investigate Eclipse hot paths to understand why. |
| D19 | `name: String` on `OperationCallExp` | For stdlib dispatch when `referredOperation` is null. See [OCL Architecture §3.1](ocl-architecture.md#31-design-choices) |
| D20 | `OclResult` record for diagnostics propagation | Three-state result: value/null/OclInvalid. See [OCL Architecture §4.4](ocl-architecture.md#44-oclresult-d20) |
| D21 | `OclExpressionParser` interface in ocl.api | Decouples engine from parser. See [OCL Architecture §4.8](ocl-architecture.md#48-oclexpressionparser-d21) |
| D22 | QVT-O: 3 EPackages in `qvto.model` | `imperativeocl` + `qvtoperational` + `trace` in one model bundle |
| D23-ocl | Security hardening limits | See [OCL Architecture §10](ocl-architecture.md#10-security-hardening) |
| D24 | QVT-O: Blackbox via `QvtoBlackboxLibrary` interface | No reflection/annotation-parsing. Typed interface, consistent with `OclOperationProvider`. OSGi: `@Reference(MULTIPLE, DYNAMIC)` |
| D25 | QVT-O: Hybrid evaluator (composition, not inheritance) | `QvtoEvaluator` delegates to `OclEvaluator`, callback via `QvtoOperationProvider` for mutual recursion |
| D26 | QVT-O: `Dict`/`List` as aliases for OCL `Map`/`Sequence` | Syntactically recognized, semantically mapped to OCL types |
| D27 | QVT-O: Unit Resolver — ServiceLoader + Whiteboard | Standalone: `ServiceLoader<QvtoUnitResolver>` + filesystem/classpath. OSGi: `@Reference(MULTIPLE)` whiteboard |
| D28 | QVT-O v1.3, Eclipse feature scope | Implement what Eclipse implements, document gaps for later |
| D29 | Extension Security Controls | Custom ops / blackbox / unit resolvers disabled by default, opt-in with allow-lists |
| D30 | Resource Integrity Service (Future) | Build-time SHA-256 hashing of arbitrary resources (scripts, ecore, files) with optional runtime verification |
| D31 | M2T Protected Area Merge: opt-in + hash-based smart merge | Merge only when `M2tGenerationStrategy.readExistingContent()` is provided (opt-in). When active, engine emits a hash of the default content in the marker comment. On re-generation, merger compares existing content against hash: if unchanged → overwrite with new template default; if user-modified → preserve user content. Spec-compatible (§8.1.7 defines markers as implementation-specific). |
| D32 | M2T Whitespace: `WhitespaceMode` enum + hybrid normalizer | Three-tier `WhitespaceMode` (NONE/SPEC/ACCELEO, default ACCELEO). `M2tWhitespaceNormalizer` runs post-link as AST transformation. Body-trimming, standalone-block detection, BOL indicator at parse-time; indent-propagation at eval-time via `fitIndentationTo()`. Inline LetBlocks excluded from standalone detection. |
| D33 | QVT-R: Direkte Interpretation (kein QVT-C) | QVT-R Relations werden direkt ausgewertet. Kein QVTr→QVTc Compiler, kein QVT-C Engine. Traces implizit in der Engine. QVT-C kann später als Optimierung ergänzt werden. |
| D34 | QVT-R: 3 EPackages in einem `qvtd.model` Bundle | `qvtbase` (Transformation, TypedModel, Domain, Rule, Pattern), `qvttemplate` (TemplateExp, ObjectTemplateExp, CollectionTemplateExp), `qvtrelation` (Relation, RelationDomain, DomainPattern, Key). Kein QVTCore-Package zunächst. |
| D35 | QVT-R: Spec-first, Eclipse als Verhaltensreferenz | Implementierung gegen QVT v1.3 Spec Ch. 7. Eclipse QVT-D nur als Verhaltensreferenz, nicht als Architektur-Vorlage (deren Xtext/Pivot/QVTr→QVTc Stack ist inkompatibel mit unserem Ansatz). |
| D36 | QVT-R: Evaluator-Komposition wie D25 | `QvtdEvaluator → OclEvaluator` Komposition für OCL in when/where. Blackbox via Interface-Pattern wie D24. QVT-O→QVT-R Brücke (GAP-6) deferred bis QVT-R standalone läuft. |
| D37 | QVT-R: Unit Resolver wiederverwenden | Gemeinsames `TransformationUnitResolver`-Konzept für QVT-R und QVT-O. Gleiche Patterns: ServiceLoader (standalone), Whiteboard (OSGi), Security Controls (D29). |
| D38 | QVT-R: Phasenplan — QVT-R standalone zuerst, dann QVT-O Brücke | Phase 4a: QVT-R standalone (Metamodell, Parser, Engine). Phase 4b: QVT-R↔QVT-O Hybrid (GAP-6 `refined`, §7.8 Blackbox-Delegation). QVT-C bleibt optional für spätere Optimierung. |
| D39 | QVT-R: Modul-Abhängigkeiten — keine Zyklen durch Interface-Entkopplung | `qvtd.model` und `qvto.model` bleiben unabhängig (kein Refactoring). Hybrid-Brücke über Interfaces in `qvtd.api` (`QvtdEngine`, `RelationImplementationProvider`). `qvto.engine → qvtd.api` (nicht qvtd.engine). `qvtd.engine` kennt QVT-O nicht. |
| D40 | QVTBase als Shared-Bundle `org.eclipse.fennec.m2x.qvt.model` | `qvtbase.ecore` extrahiert; typsichere EReferences zwischen `qvto.model` und `qvtd.model`. Siehe DR-D40. |
| D41 | OCL IDE-Integration als isoliertes, platform-gekoppeltes Bundle | `org.eclipse.fennec.m2x.ocl.ide` registriert die EMF-Delegates via `plugin.xml` für den generischen EMF-Editor. Einzige Ausnahme zur "No Eclipse Platform"-Regel, strikt isoliert. Siehe DR-D41. |
| D42 | `EPackage.Registry` ist Eingabewert der Engine, genau ein Fallback-Punkt | Engine-Interna lesen nie `EPackage.Registry.INSTANCE`. Die Konfiguration löst einmalig `registry != null ? registry : INSTANCE` auf, alles darunter bekommt die Registry als Parameter. Modellversions-Identität bleibt Sache des Aufrufers. Siehe DR-D42. |

---

## 17. Forbidden / Out of Scope

- **No Eclipse Platform dependencies** (org.eclipse.core.*, org.eclipse.ui.*)
- **No LPG Runtime** (lpg.runtime.*) - dead technology
- **No Xtext** (org.eclipse.xtext.*) - too heavy, pulls in Eclipse platform
- **No Guava / Apache Commons** - use Java stdlib
- **No Eclipse IDE integration** (editors, content assist)
- **No Acceleo 3.x/4.x compatibility** (implement MOFM2T v1.0 spec with OCL + cherry-picked extensions, not Acceleo-specific behavior)
- **No AQL** (Acceleo Query Language) - OCL is the only expression language across all engines

**Allowed non-EMF runtime dependency:**
- `org.antlr:antlr4-runtime:4.13.2` (~318KB, zero transitive deps, OSGi-ready, BSD license)

---

## 18. Reference Projects

| Project | Path | What to learn |
|---------|------|---------------|
| `org.eclipse.fennec.m2x.model` | `workspace/org.eclipse.fennec.m2x.model/` | Ecore + genmodel + bnd.bnd pattern for model projects |
| `org.eclipse.fennec.m2x.code` | `workspace/org.eclipse.fennec.m2x.code/` | Simple code bundle, license header, .project/.classpath |
| `org.eclipse.fennec.model.metadata` | `/opt/git/emf.codec/org.eclipse.fennec.model.metadata/` | Mixed project with src/ + src-gen/ + src-gen-api/ |
| `fennec.bnd.libraries` | `/opt/git/fennec.bnd.libraries/` | Library definitions (fennec, fennecTest, fennecJacoco) |
| `emf.osgi` | `/opt/git/m2m/repositories/emf.osgi/` | fennecEMF library, EMF OSGi code generation, EPackageConfigurator pattern |
| `org.eclipse.qvtd` | `/opt/git/m2m/repositories/org.eclipse.qvtd/` | QVT-Relations + QVT-Core reference implementation (Xtext/ANTLR3, Pivot metamodel, QVTr→QVTc compiler) |
| `org.eclipse.qvto` | `/opt/git/m2m/repositories/org.eclipse.qvto/` | QVT-Operational reference implementation (LPG parser, imperative transformation engine) |
| `eclipse-acceleo` | `/opt/git/m2m/repositories/eclipse-acceleo/` | ANTLR4 `Query.g4` grammar for OCL-like expressions, `acceleo.ecore` template metamodel design reference, AcceleoParser for template parsing patterns. **Note:** Acceleo 4 uses AQL instead of OCL - only use as structural reference, not for expression handling. |

---

## 19. Decision Review Process

Design decisions are living documents, not permanent rules. They must be periodically reviewed as the project evolves — new libraries emerge, requirements shift, implementation experience reveals unexpected trade-offs.

### 19.1 Review Triggers

A decision review is triggered when:

- **Phase transition** — starting a new implementation phase (OCL → QVT-O → QVT-D → M2T)
- **Unexpected friction** — a decision makes implementation significantly harder than expected
- **New discovery** — a library, tool, or technique is found that would substantially improve the result
- **Performance gap** — benchmarks show we can't meet targets under current constraints
- **User feedback** — requirements or priorities change

### 19.2 Review Checklist

At each phase transition, walk through all decisions and ask:

| Question | Action if "yes" |
|----------|-----------------|
| Does this decision still serve our goals? | Keep |
| Has the ecosystem changed (new libraries, deprecations)? | Re-evaluate |
| Did implementation reveal the decision was based on wrong assumptions? | Revise, document why |
| Is a "forbidden" dependency actually the right tool for a specific problem? | Discuss, weigh trade-off, revise if justified |
| Are we over-engineering or under-engineering based on this decision? | Adjust scope |
| Did we discover something in the Eclipse code that challenges our approach? | Investigate, potentially adopt |

### 19.3 Review Format

When revisiting a decision, update the Decision Record (section 20) with:

```
### DR-Dxx: <Title>
...existing content...

#### Review <date>
**Trigger:** <what caused the review>
**Finding:** <what changed>
**Outcome:** Decision confirmed / revised / revoked
**Rationale:** <why>
```

### 19.4 Example: The "No External Libraries" Balance

The "Forbidden" section (17) bans Guava, Apache Commons, etc. This is a good default — Java 21 stdlib is excellent and zero-dependency bundles are easier to deploy. But it should not prevent using the right tool:

- If a specific, small, well-maintained library solves a hard problem significantly better than hand-rolling (e.g., a graph algorithm library for `closure` evaluation, or a high-performance collection library for large model navigation), the decision should be **challenged, not obeyed blindly**.
- The bar for adding a dependency: **"Does this library solve a problem we can't solve well ourselves, with acceptable size and licensing?"**
- Every added dependency must be: small footprint, zero/few transitive deps, OSGi-compatible, EPL-2.0-compatible license.

---

## 20. Decision Records

Detailed rationale for key design decisions that require more context than a one-liner.

### DR-D15: MOFM2T v1.0 + Cherry-Picked Extensions (not Acceleo)

**Context:** The MOFM2T v1.0 OMG spec defines a model-to-text transformation language. Eclipse Acceleo is the primary implementation, but exists in two incompatible generations.

**Problem:** MOFM2T v1.0 mandates OCL 2.0 as expression language. Acceleo 3.x used Eclipse OCL (compliant). Acceleo 4.x replaced OCL with AQL (Acceleo Query Language) - a fundamentally different expression language. AQL is not OCL: different collection model (no Bag/Set distinction), different null semantics, different syntax for collection operations (`.` instead of `->`).

**Decision:** Implement MOFM2T v1.0 spec with our OCL engine + cherry-picked practical extensions from Acceleo.

**Cherry-picked extensions:**

| Extension | Source | Rationale |
|-----------|--------|-----------|
| `post(expr)` | Acceleo 3/4 | Post-processing on template output (trim, format). Genuinely useful, trivial to implement. |
| `@main` | Acceleo 3/4 | Explicit entry point marking. Better UX than "first public template". |
| `elseif` | Acceleo 3/4 | Syntactic sugar for nested if/else. Improves readability. |
| Java service imports | Acceleo 3/4 | Call Java methods from templates. Maps to our `M2tService` extension point. |

**Explicitly excluded:**

| Feature | Reason |
|---------|--------|
| AQL expression language | We have OCL. One expression language across all engines. |
| TraceBlock | Rarely used in practice. Can be added later if needed. |
| Macros | Barely used. Templates + queries cover all practical needs. |
| Text mode switching (`@text-explicit`/`@code-explicit`) | Nobody uses this. |
| `create` file open mode (Acceleo 4 only) | Non-standard. `overwrite`/`append` from spec sufficient. |

**Why not Acceleo 3 compatibility?** Acceleo 3's `.emtl` compiled binary format, implicit `self` semantics, and tight Eclipse OCL coupling make it a poor compatibility target. The spec is cleaner.

**Why not Acceleo 4 as reference?** AQL is not OCL. Using Acceleo 4's expression handling would require maintaining a second expression language with no benefit.

---

### DR-D16: No Pivot Layer

Direct Ecore access without intermediate AS representation. Avoids Eclipse OCL's 262-class
Pivot metamodel and Ecore→AS conversion overhead. UML models work via `EObject` navigation;
UML-specific operations via future `ocl.uml` extension bundle.

Full rationale: [OCL Architecture §6.7](ocl-architecture.md#67-no-pivot-layer-d16)

---

### DR-D14: Configurable OCL Evaluation

Strict/lenient evaluation modes. STRICT for validation (spec-compliant null→OclInvalid),
LENIENT for M2T (null→null/empty). Safe navigation (v2.5) provides explicit null-safety;
LENIENT provides implicit null-safety.

Full rationale: [OCL Architecture §13.2](ocl-architecture.md#132-dr-d14-configurable-evaluation-strict--lenient)

---

### DR-D22: MessageExp — Deferred

`MessageExp` (`^^` operator) deferred. Currently returns OclInvalid. Future: `OclMessageProvider`
interface. Neither Eclipse OCL nor QVT-O ever implemented it at runtime.

Full details: [OCL Architecture §13.4](ocl-architecture.md#134-dr-d22-messageexp--deferred)

---

### DR-D23: Security Hardening Limits

Configurable per-evaluation resource limits in `OclEvaluationOptions` (maxDepth, maxCollectionSize,
maxClosureIterations, maxRegexLength). 14 attack vectors identified and mitigated/documented.

Full details: [OCL Architecture §10](ocl-architecture.md#10-security-hardening)

---

### DR-D24: Performance Benchmark Design

Both engines under identical conditions (standalone Java 21, same model/expressions, 100 warmup +
10K measurement iterations). Metrics: ns/eval, ops/sec, MB heap.

Full details: [OCL Architecture §13.6](ocl-architecture.md#136-dr-d24-performance-benchmark-design)

---

### DR-D25: Test Coverage Gaps

JaCoCo analysis after 3296 tests. Accepted gaps: OclEngineComponent, dispatchBoolean, validate().
Gaps to close: @pre, LENIENT, Complete OCL operation context, thread-safety.

Full details: [OCL Architecture §12.6](ocl-architecture.md#126-coverage-gaps-dr-d25)

#### Test Review (2026-02-20)
**Trigger:** Test-writing adapted tests to match implementation rather than fixing implementation.
**Finding:** Comprehensive review of ~90 OCL + 21 QVT-O test files found 22 issues:
- 3 critical implementation bugs (excluding, substring, oclType) — **fixed in OclStdlib**
- 8 tests with wrong names or missing verification — **fixed**
- ~20 smoke tests (only `assertNotNull`/`isSuccess`) — **strengthened with value assertions**
- Non-standard OCL extensions (toUpperCase, +, trim) — **documented as intentional**
**Outcome:** 3537 tests (3361 OCL + 176 QVT-O), 0 failures. See `docs/archive/test-review-findings.md`.

---

### DR-D22: QVT-O Metamodel — 3 EPackages in One Bundle

**Context:** QVT-O needs its own metamodel extending the OCL metamodel. The OMG QVT v1.3 spec defines three distinct metamodel areas: ImperativeOCL (imperative expression extensions), QVTOperational (transformation/mapping/module structure), and a Trace model for recording transformation execution.

**Options considered:**
- (A) One bundle `qvto.model` with 3 EPackages — simple build, single genmodel, cross-package references trivial
- (B) Separate bundles per EPackage — more modular but adds build complexity and inter-bundle dependencies for tightly coupled metamodels
- (C) One EPackage with all classifiers — flattens the logical structure

**Decision:** Option A — single `qvto.model` bundle containing 3 EPackages (`imperativeocl`, `qvtoperational`, `trace`). Each EPackage gets its own nsURI. ImperativeOCL EClasses extend OCL model types (e.g., `AssignExp extends OclExpression`). Cross-package references within the bundle are trivial.

**Rationale:** The three metamodel areas are tightly coupled (QVTOperational references ImperativeOCL expressions, Trace references QVTOperational elements). Separate bundles would create circular dependencies. A single bundle with logical EPackage separation is the cleanest approach, consistent with `ocl.model` (single EPackage, single bundle).

#### Implementation Review (2026-02-20)
**Status:** Implemented. 55 EClasses + 4 EEnums = 59 classifiers across 3 EPackages.

**EMF codegen adaptations (lessons learned):**

1. **`VarParameter` — no dual-source `name` feature.** Plan had `VarParameter extends EParameter, ocl::Variable`. EMF rejected this: `EParameter` inherits `name` from `ENamedElement`, and `ocl::Variable` declares its own `name: EString` — two distinct feature declarations with the same name. Fix: `VarParameter extends EParameter` only, with `representedParameter: Variable` (non-containment ref) and `ownedInit: OclExpression` (containment) added locally.

2. **`Module` — no dual Ecore metaclass impl inheritance.** Plan had `Module extends EClass, EPackage`. EMF codegen produces raw-type `EList` fields (no generics) for features from the second supertype because `ModuleImpl` can only extend one concrete impl class. Fix: `Module extends EPackage` only. EPackage provides `name`, `nsURI`, `nsPrefix`, `eClassifiers`, `eSubpackages` — sufficient for a module as namespace container.

3. **Skipped EClasses vs. Eclipse:** `DictLiteralExp`/`ListLiteralExp`/`DictionaryType`/`ListType` (D26: parser aliases), `OrderedTupleLiteralExp`/`OrderedTupleType` (Eclipse extension, not in spec), `UnpackExp` (rare), `VisitableASTNode` (use EMF Switch pattern).

4. **Map$Entry EClasses** (`MappingOperationToTraceRecordMapEntry`, `ObjectToTraceRecordMapEntry`) require `instanceClassName="java.util.Map$Entry"` for EMF's typed map support.

---

### DR-D24: QVT-O Blackbox via `QvtoBlackboxLibrary` Interface

**Context:** QVT-O allows "blackbox" operations — Java methods callable from transformation code. Eclipse QVT-O uses `@Module`/`@Operation` annotations with reflection-based discovery via a static `BlackboxRegistry` singleton. The gecko.qvt.osgi project bridges this with an OSGi ServiceTracker but still depends on Eclipse's annotation+reflection approach internally.

**Options considered:**
- (A) Annotation-based like Eclipse (reflection, class scanning) — familiar but fragile, not OSGi-friendly
- (B) Typed interface like our `OclOperationProvider` — explicit contract, no reflection, OSGi whiteboard
- (C) Mixed approach (annotations for convenience, interface as fallback)

**Decision:** Option B — typed `QvtoBlackboxLibrary` interface with `QvtoBlackboxOperation` records. No annotation parsing, no reflection. Operations are declared programmatically with explicit type signatures and `BiFunction` handlers.

**Rationale:** Consistent with the `OclOperationProvider` pattern already established in Phase 1. Reflection-based discovery is fragile across classloaders, requires additional security permissions, and breaks when Java modules restrict access. A typed interface is explicit, testable, and works identically in standalone and OSGi modes. OSGi registration: `@Reference(cardinality = MULTIPLE, policy = DYNAMIC)`.

**Interface sketch:**
```java
@ConsumerType
public interface QvtoBlackboxLibrary {
    String getModuleName();
    String getUnitQualifiedName();
    List<QvtoBlackboxOperation> getOperations();
    List<String> getUsedPackageURIs();
}

public record QvtoBlackboxOperation(
    String name,
    EClassifier contextType,       // null for module-level
    List<EClassifier> parameterTypes,
    EClassifier returnType,
    BiFunction<Object, Object[], Object> handler
) {}
```

---

### DR-D25: QVT-O Hybrid Evaluator (Composition, Not Inheritance)

**Context:** Eclipse QVT-O uses a single monolithic `QvtOperationalEvaluationVisitorImpl` (~2654 lines) that inherits from an OCL visitor. This creates a god class that is hard to test, maintain, and extend. QVT-O expressions and OCL expressions have mutual recursion: a QVT-O mapping body contains OCL expressions, and OCL expressions within a mapping can call QVT-O helpers/mappings.

**Options considered:**
- (A) Inheritance: `QvtoEvaluator extends OclEvaluator` — simple but creates monolithic class, tight coupling, OCL evaluator internals leak into QVT-O
- (B) Composition with callback: `QvtoEvaluator` delegates OCL sub-expressions to `OclEvaluator`, uses `OclOperationProvider` callback for mutual recursion — clean separation, each evaluator handles its own concern
- (C) Full separation with expression tree rewriting — over-engineered, unnecessary

**Decision:** Option B — hybrid composition. `QvtoEvaluator` is its own class (not inheriting from `OclEvaluator`). It creates an `OclEvaluator` internally and delegates OCL expression evaluation to it. For mutual recursion (OCL expressions calling QVT-O helpers/mappings), a `QvtoOperationProvider implements OclOperationProvider` is registered with the OCL evaluator as a callback bridge.

**Rationale:** Keeps both evaluators focused on their own language constructs. The existing `OclOperationProvider` mechanism is exactly the right hook — no new infrastructure needed. The OCL evaluator's 3-level dispatch (eInvoke → stdlib → custom providers) already supports this pattern. Testing is simpler: OCL evaluator can be tested independently, QVT-O evaluator tests focus on imperative constructs.

**Architecture:**
```
QvtoEvaluator
  ├── handles: AssignExp, WhileExp, ForExp, MappingOperation, ...
  ├── owns: OclEvaluator (created internally)
  │     └── registered provider: QvtoOperationProvider
  └── delegates: PropertyCallExp, IteratorExp, literals, ... → OclEvaluator

QvtoOperationProvider implements OclOperationProvider
  └── recognizes QVT-O helpers/mappings → re-enters QvtoEvaluator
```

---

### DR-D26: QVT-O `Dict`/`List` as Aliases for OCL `Map`/`Sequence`

**Context:** The QVT-O spec defines `Dict(K,V)` and `List(T)` collection types. These are semantically identical to OCL's `Map(K,V)` and `Sequence(T)` but use QVT-O-specific syntax in the parser.

**Options considered:**
- (A) Separate EClasses for `DictType`/`ListType` — duplicates OCL types unnecessarily
- (B) Ignore `Dict`/`List` entirely — breaks QVT-O syntax compatibility
- (C) Parser-level aliases: recognize `Dict`/`List` syntax, map to OCL `MapType`/`SequenceType` in the AST

**Decision:** Option C — syntactic aliases. The QVT-O grammar recognizes `Dict` and `List` keywords and constructs the corresponding OCL `MapType` and `SequenceType` AST nodes. No new metamodel types needed. All collection operations work identically since the runtime representation is the same.

**Rationale:** Follows the QVT spec's intent (these are convenience aliases, not distinct type systems). Avoids metamodel bloat. OCL standard library operations on Map/Sequence work transparently on Dict/List. The mapping happens entirely in the parser's AST builder.

---

### DR-D27: QVT-O Unit Resolver — ServiceLoader + Whiteboard

**Context:** QVT-O transformations can import other transformations and libraries by qualified name. The system needs a mechanism to resolve these qualified names to actual transformation units (`.qvto` files or compiled units).

**Decision:**
- **Standalone:** `ServiceLoader<QvtoUnitResolver>` discovers resolvers on the classpath. Built-in resolvers for filesystem paths and classpath resources. Resolvers are passed via `QvtoConfiguration.Builder.unitResolvers(...)` (D29: runtime registration removed).
- **OSGi:** Whiteboard pattern with `@Reference(cardinality = MULTIPLE, policy = DYNAMIC) volatile List<QvtoUnitResolver>` on `QvtoEngineComponent`. Bundles contribute resolvers as DS components.

**Rationale:** Consistent with the OSGi-optional architecture (D9). ServiceLoader provides zero-config discovery in standalone mode. The whiteboard pattern is already proven with `OclOperationProvider` and `CompleteOclContribution` in Phase 1. Multiple resolvers can be composed (first match wins), supporting different resolution strategies (workspace, repository, classpath).

---

### DR-D28: QVT-O v1.3, Eclipse Feature Scope

**Context:** The OMG QVT v1.3 spec is extensive. Eclipse QVT-O implements a substantial subset but not everything. Some spec features have no known practical usage.

**Decision:** Implement the features that Eclipse QVT-O implements, document all gaps explicitly for future implementation. This provides practical compatibility (existing QVT-O transformations work) while avoiding effort on unused spec features.

**Known Eclipse gaps to document:** intermediate classes (partial), access modifiers on properties, some late resolution features, module-level exception handling. A complete gap analysis will be produced during the `qvto.parser` phase when the grammar is defined.

**Rationale:** Eclipse QVT-O is the de-facto standard for QVT-O usage. Its feature subset covers >99% of real-world transformations. Documenting gaps ensures nothing is silently dropped and allows prioritized implementation later based on user demand.

---

### DR-D29: Extension Security Controls

**Context:** Both OCL and QVT-O engines have extension points that allow executing arbitrary Java code:

- **OCL:** `OclOperationProvider` — custom operations invoked during expression evaluation
- **QVT-O:** `QvtoBlackboxLibrary` — Java libraries callable from transformations
- **QVT-O:** `QvtoUnitResolver` — resolves import names to transformation source code

These are trust boundaries: a malicious provider/library/resolver has full JVM access (filesystem, network, processes). Currently all registered extensions are always active — there is no opt-in mechanism.

**Threat:** In multi-tenant or plugin-based environments (OSGi whiteboard, ServiceLoader), an attacker could register a malicious extension that gets automatically discovered and invoked.

**Decision:** All extension points are **disabled by default** and require explicit opt-in. Two enforcement levels (AND-linked).

**OCL operation provider wiring:** Instead of a whiteboard (MULTIPLE, DYNAMIC), the `OclEngineComponent`
uses a **single mandatory `@Reference(name="operationProvider")`** with a configurable target filter.
The deployer explicitly selects which provider is bound via `operationProvider.target`. A built-in
`NoOpOclOperationProvider` (empty list) is bound by default. If multiple providers are needed,
a compound provider aggregating them must be registered as a single service.

#### OCL: Boolean Enable Flag

| Setting | Location | Default | Semantics |
|---------|----------|---------|-----------|
| `customOperationsEnabled` | `OclConfiguration` | `false` | Engine-wide: are custom ops allowed at all? |
| `customOperationsEnabled` | `OclEvaluationOptions` | `false` | Per-evaluation: override for this evaluation |

Custom operations are only dispatched if **both** flags are `true`. This enables:
- Config `false` → hard disable, Options cannot override (Defense in Depth)
- Config `true`, Options `false` → enabled engine, but sandboxed evaluation (e.g. for user input)
- Config `true`, Options `true` → fully active

**Enforcement point:** `OclEvaluator.dispatchCustomOperation()` — returns `NOT_FOUND` immediately if disabled.

#### QVT-O: Enable Flag + Allow-Lists

| Setting | Location | Default | Semantics |
|---------|----------|---------|-----------|
| `blackboxEnabled` | `QvtoConfiguration` | `false` | Are blackbox libraries allowed? |
| `allowedBlackboxModules` | `QvtoConfiguration` | `Set.of()` | If enabled: empty = allow all, non-empty = allow only listed modules |
| `unitResolverEnabled` | `QvtoConfiguration` | `false` | Are unit resolvers allowed? |
| `allowedUnitModules` | `QvtoConfiguration` | `Set.of()` | If enabled: empty = allow all, non-empty = allow only listed modules |
| `maxBlackboxLibraries` | `QvtoConfiguration` | `10` | Maximum number of registered blackbox libraries |
| `maxUnitResolvers` | `QvtoConfiguration` | `5` | Maximum number of registered unit resolvers |

**Allow-list semantics:**
- `enabled == false` → extension completely disabled
- `enabled == true && allowList.isEmpty()` → all registered extensions allowed
- `enabled == true && !allowList.isEmpty()` → only modules whose `qualifiedName` is in the list

**Enforcement point:** `QvtoLinker` — before the evaluator starts. Unresolvable imports produce `QvtoParseException("Cannot resolve import: ...")` → fail-fast.

**Max-limits rationale:** Too many resolvers/libraries increase attack surface and cause performance issues (linear scan per import).

#### Immutable Configuration

Runtime registration methods (`registerUnitResolver()`, `unregisterUnitResolver()`, `registerOperations()`, `unregisterOperations()`) have been removed. All extensions are declared at configuration time via immutable `QvtoConfiguration` / `OclConfiguration`. This prevents runtime injection attacks.

- **Standalone:** Extensions passed to `QvtoConfiguration.builder()`
- **OSGi:** DS component activation injects extensions into configuration

**Rationale:** Explicit opt-in follows the principle of least privilege. Breaking change is acceptable (no backward compatibility constraint). The two-level enforcement (Configuration + Options) provides defense in depth for OCL.

---

### DR-D30: Resource Integrity Service (Future)

**Context:** QVT-O scripts, Ecore models, and other resources loaded at runtime could be tampered with. D29 controls *who* can provide extensions, but not *whether the content* of a resolved resource has been modified.

**Problem:** An attacker who cannot inject a malicious resolver (blocked by D29) might still modify the resource file on disk that a legitimate resolver reads. The resolver returns the tampered content, which passes the allow-list check (correct qualifiedName) but contains malicious code.

**Decision:** Build-time SHA-256 hashing of arbitrary resources with optional runtime verification. This is a **future requirement** — the design is defined here, implementation is deferred.

#### Architecture

```
Build-Zeit (Gradle/bnd):
  resource files → SHA-256 → generated Java class ResourceHashes
                              (immutable Map<String, String> name→hash)
                              → compiled into descriptor bundle

Laufzeit (Engine):
  Resolver liefert Source → hash(source) == ResourceHashes.get(name)?
  Match    → proceed
  Mismatch → reject with IntegrityException
```

#### Bundle Separation

```
Bundle A: "my.transformations"         Bundle B: "my.transformations.integrity"
├── scripts/                           ├── ResourceHashes.class (generated)
│   ├── Main.qvto                      │   contains:
│   ├── Helper.qvto                    │     HASHES: Map<String, String>
│   └── model.ecore                    │     CHAIN_HASH: String
└── (no services)                      └── MyUnitResolver.class (OSGi service)
```

Two separate bundles ensure that replacing Bundle A (the scripts) is detected by Bundle B (the hashes). An attacker must replace *both* bundles to succeed.

#### Hash Scheme

| Feature | Description |
|---------|-------------|
| **Individual hashes** | `SHA-256(salt + resourceContent)` per resource |
| **Chain hash** | `SHA-256` over sorted `(name + hash)` pairs — changes when *any* resource is added/removed/modified |
| **Salt** | `bundleSymbolicName + ":" + bundleVersion + ":" + buildTimestamp` — prevents pre-computation |
| **External trust anchor** | Chain hash published in CI artifact / deployment DB / signed manifest — enables cross-bundle verification |

#### Applicability

This is a **generic resource integrity mechanism**, not specific to QVT-O:

| Resource Type | Use Case |
|---------------|----------|
| `.qvto` scripts | Transformation integrity |
| `.ecore` models | Metamodel integrity |
| `.ocl` documents | Complete OCL constraint integrity |
| `.mtl` templates | M2T template integrity |
| Any file | Arbitrary resource verification |

#### Configuration

| Setting | Default | Semantics |
|---------|---------|-----------|
| `integrityVerificationEnabled` | `false` | Is hash verification active? |
| `integrityHashProvider` | `null` | Implementation providing the hash map |
| `requireIntegrity` | `false` | `true` = reject unverified resources; `false` = warn only |

**Development mode:** `integrityVerificationEnabled = false` — no hash checks, full developer agility.

**Production mode:** `integrityVerificationEnabled = true, requireIntegrity = true` — all resources must match their hashes.

#### Build Integration

Gradle task generates the hash class into `src-gen/`:

```groovy
task generateResourceHashes {
    inputs.dir("scripts/")
    inputs.dir("model/")
    outputs.file("src-gen/.../ResourceHashes.java")
    doLast {
        // SHA-256 over all configured resource files
        // Generate Java class with HASHES map + CHAIN_HASH
    }
}
```

Fits the existing build model: `src-gen/` is generated code, bnd compiles and packages it.

#### Attack Analysis

| Attack | Result |
|--------|--------|
| Replace script bundle only | Hash mismatch → **detected** |
| Replace both script + integrity bundle | Requires compromising two bundles → **harder** |
| Replace integrity bundle only | Hashes don't match unmodified scripts → **detected** (wrong direction) |
| Classpath manipulation (plain Java) | No bundle isolation → **vulnerable** (mitigation: signed JARs) |
| OSGi bundle replacement | Mitigated by OSGi bundle signing (Stufe 1, deployer responsibility) |
| Pre-compute hashes | Salted hashes prevent rainbow tables → **mitigated** |

#### Hardening Levels (Embedder Responsibility)

| Level | Mechanism | Protects Against | Effort |
|-------|-----------|------------------|--------|
| 1 | OSGi bundle signing (`jarsigner`) | Bundle replacement | Low (platform feature) |
| 2 | Chain hash + external trust anchor | Both-bundles-replaced | Medium (our code) |
| 3 | Salted hashes | Pre-computation | Low (our code) |
| 4 | bnd/Gradle build integration | Developer errors (forgotten hash update) | Medium (our code) |

**Implementation priority:** Levels 2–4 are our responsibility. Level 1 is the deployer's responsibility and provides the strongest single protection.

**Status:** Future requirement. Implementation deferred until Phase 5 (Language Servers / Production Hardening).

---

### DR-D32: M2T Whitespace Handling — WhitespaceMode + Hybrid Normalizer

**Context:** MOFM2T §8.4 defines 5 whitespace rules that transform raw template text into normalized output. Without normalization, template output contains unwanted leading/trailing newlines and indentation artifacts from the template structure itself. The rules must be applied consistently across all block types (Template, ForBlock, IfBlock, LetBlock, FileBlock).

**Decision:** Three-tier `WhitespaceMode` enum controlling §8.4 behavior:

| Mode | Behavior |
|------|----------|
| `NONE` | No normalization — raw template output |
| `SPEC` | Strict MOFM2T §8.4 (all 5 rules) |
| `ACCELEO` | Default — like SPEC but without BOL `^` indicator (passed through as literal text; Acceleo 3.7 does not implement `^`) |

**Architecture:** Hybrid parse-time + eval-time approach:

| Concern | When | Why |
|---------|------|-----|
| Body-trimming (leading/trailing newlines) | Parse-time | Statically derivable from AST structure |
| Standalone-block detection + WS-strip | Parse-time | Statically derivable from TextExpression contents |
| Default `\n` separator injection (standalone for) | Parse-time | Can be set as StringLiteralExp in AST |
| BOL indicator `^` processing (SPEC only) | Parse-time | Pure text transformation on TextExpression.value; skipped in ACCELEO mode |
| Indent-propagation (template invocations) | Eval-time | Depends on runtime output via `fitIndentationTo()` |

**Key implementation details:**

1. **`M2tWhitespaceNormalizer`** (`m2t.parser`): AST transformation that runs **after linking** in `M2tEngineImpl.execute()`. Must run post-link because the linker replaces inline `__inline__` LetBlocks with actual `TemplateInvocation` objects — the normalizer needs the final AST to correctly extract indentation for standalone invocations.

2. **Inline LetBlock exclusion:** The parser wraps inline expressions `[expr/]` as LetBlocks with synthetic `__inline__` variable names. These must be excluded from standalone-block detection and body-trimming, as they are expression wrappers, not structural blocks.

3. **Indent-propagation:** `M2tEvaluator.caseTemplateInvocation()` checks the `indentationMap` (populated by the normalizer). If an indent is found, it pushes a nested StringWriter, executes the template, pops the result, and applies `fitIndentationTo()` which inserts the indent string after every newline (first line is NOT indented).

4. **Default separator:** Both SPEC and ACCELEO modes inject `\n` as default separator for standalone for-blocks without explicit separator. This is fundamental standalone-block behavior per §8.4.

**Alternatives considered:**
- *Pure parse-time:* Cannot handle indent-propagation (depends on runtime output).
- *Pure eval-time:* Would require tracking line positions during evaluation, adding complexity to every `caseXxx()` method.
- *Single boolean flag:* Initially implemented as `whitespaceNormalization: boolean`, refactored to enum after discovering behavioral differences between strict spec and Acceleo compatibility.

**Files:**
- `m2t.api/.../WhitespaceMode.java` — enum
- `m2t.api/.../M2tConfiguration.java` — `whitespaceMode()` accessor
- `m2t.parser/.../M2tWhitespaceNormalizer.java` — AST normalizer
- `m2t.engine/.../M2tEngineImpl.java` — normalizer invocation + indentation map
- `m2t.engine/.../internal/M2tEvaluator.java` — `fitIndentationTo()` + indent-propagation
- `m2t.tests/.../M2tWhitespaceTest.java` — 17 tests

---

### DR-D33: QVT-R Direkte Interpretation (kein QVT-C)

**Context:** Die QVT v1.3 Spec definiert eine Zweischicht-Architektur: QVT-R (Relations, Ch. 7) als High-Level-Sprache und QVT-C (Core, Ch. 9) als Low-Level-"Bytecode". Die Spec beschreibt eine QVTr→QVTc Transformation (Ch. 10, ~30 Seiten Mapping-Regeln), die Relations in flache Core-Mappings mit expliziten Trace-Klassen übersetzt. Eclipse QVT-D implementiert diesen Compiler-Ansatz.

**Options considered:**
- (A) QVTr→QVTc Compiler + QVTc Engine (wie Eclipse) — formale Semantik über QVT-C definiert, inkrementelle Updates einfacher durch explizite Traces, aber Compiler selbst ist komplex (~30 Seiten Transformationsregeln)
- (B) Direkte QVT-R Interpretation — Engine wertet Relations direkt aus, implizite Traces, kein QVT-C nötig
- (C) Beides — QVT-R Interpreter + optionaler QVTr→QVTc Compiler für Optimierung

**Decision:** Option B — direkte QVT-R Interpretation. Die Engine wertet Relations, Domain-Patterns und when/where-Klauseln direkt aus. Trace-Objekte werden implizit von der Engine erzeugt und verwaltet (wie QVT-R es vorsieht). Kein QVT-C Metamodell, kein Compiler.

**Rationale:**
- Der QVTr→QVTc Compiler ist ein eigenständiges Großprojekt (Eclipse hat Jahre dafür gebraucht)
- QVT-C existiert primär als formale Semantik-Referenz und als Kompilationsziel — niemand schreibt QVT-C direkt
- Direkte Interpretation ist konzeptionell einfacher: ein Ausführungsmodell statt zwei + Compiler dazwischen
- QVT-C kann später als Optimierung ergänzt werden (Option C), ohne die QVT-R API zu ändern
- Für den hybriden QVT-R↔QVT-O Ansatz (GAP-6) ist kein QVT-C erforderlich

**Konsequenzen:**
- Inkrementelle Updates müssen direkt in der QVT-R Engine gelöst werden (kein "geschenktes" Trace-Modell aus QVT-C)
- Die formale Enforcement-Semantik (§9.10) muss direkt in der Engine umgesetzt werden, nicht über QVT-C Patterns
- `qvtd.model` enthält kein `qvtcore` EPackage (kann bei Bedarf ergänzt werden)

---

### DR-D34: QVT-R Metamodell — 3 EPackages in einem Bundle

**Context:** Die QVT v1.3 Spec definiert drei Metamodell-Packages für den deklarativen Teil (§6.4, §7.11):

```
EMOF ──── EssentialOCL
  │            │
QVTBase ──── QVTTemplate
  │               │
QVTCore      QVTRelation
```

- **QVTBase** (§7.11.1): gemeinsame Basis — `Transformation`, `TypedModel`, `Domain`, `Rule`, `Function`, `Pattern`, `Predicate`
- **QVTTemplate** (§7.11.2): Template-Expressions für Pattern Matching — `TemplateExp`, `ObjectTemplateExp`, `CollectionTemplateExp`, `PropertyTemplateItem`
- **QVTRelation** (§7.11.3): Relations-spezifisch — `RelationalTransformation`, `Relation`, `RelationDomain`, `DomainPattern`, `Key`, `RelationCallExp`

QVTCore hängt nur von QVTBase ab (nicht von QVTTemplate). QVTRelation hängt von QVTBase + QVTTemplate ab.

**Decision:** Ein Bundle `qvtd.model` mit 3 EPackages: `qvtbase`, `qvttemplate`, `qvtrelation`. Kein `qvtcore` EPackage (D33). Analog zu D22 (qvto.model mit 3 EPackages).

**Rationale:** QVTBase wird von QVTRelation und potentiell QVTCore geteilt — deshalb als eigenes EPackage sauber getrennt. Alle drei sind eng gekoppelt (QVTRelation referenziert QVTBase-Typen und QVTTemplate-Typen). Ein einzelnes Bundle vermeidet zirkuläre Abhängigkeiten. Falls QVT-C später ergänzt wird, kommt `qvtcore` als 4. EPackage in dasselbe Bundle.

**Erwartete Classifier-Verteilung (geschätzt aus Spec §7.11):**

| EPackage | Classifiers | Wichtigste Typen |
|----------|-------------|-----------------|
| `qvtbase` | ~8 | Transformation, TypedModel, Domain, Rule, Function, FunctionParameter, Pattern, Predicate |
| `qvttemplate` | ~4 | TemplateExp, ObjectTemplateExp, CollectionTemplateExp, PropertyTemplateItem |
| `qvtrelation` | ~8 | RelationalTransformation, Relation, RelationDomain, DomainPattern, Key, RelationImplementation, RelationDomainAssignment, RelationCallExp |
| **Gesamt** | **~20** | |

---

### DR-D35: QVT-R Spec-First, Eclipse als Verhaltensreferenz

**Context:** D28 (QVT-O) sagt "implement what Eclipse implements". Eclipse QVT-D (`org.eclipse.qvtd`) verwendet Xtext-basierte Parser, das Pivot-Metamodell und einen QVTr→QVTc Compiler — alles inkompatibel mit unserem Ansatz (ANTLR4, direktes Ecore, keine Pivot-Schicht D16, kein QVT-C D33).

**Decision:** Für QVT-R gilt: **Spec-first**. Implementierung gegen QVT v1.3 Kapitel 7. Eclipse QVT-D dient als:
- **Verhaltensreferenz:** Was soll bei einer gegebenen Transformation rauskommen? (Testdaten, Ausführungsbeispiele)
- **Spec-Klärung:** Wenn die Spec unklar ist, zeigt Eclipse eine mögliche Interpretation

Eclipse QVT-D dient **nicht** als:
- Architektur-Vorlage (Xtext/Pivot/QVTr→QVTc passt nicht zu unserem Stack)
- Feature-Scope-Vorgabe (D28-Ansatz "implement what Eclipse implements" greift nicht, da Eclipse QVT-D einen fundamental anderen Ausführungsweg hat)

**Rationale:** Eclipse QVT-D's Architektur (Ed Willink's QVTr→QVTc→QVTi→QVTs Pipeline) ist ein Forschungsprojekt mit mehreren Zwischenstufen. Direkte QVT-R Interpretation (D33) ist ein bewusst anderer Weg. Die Spec ist die primäre Wahrheit, Eclipse ist ein Datenpunkt.

---

### DR-D36: QVT-R Evaluator-Komposition (D25 erweitert)

**Context:** D25 etabliert das Kompositions-Pattern für QVT-O: `QvtoEvaluator` delegiert OCL-Expressions an `OclEvaluator`, Rückkanal über `QvtoOperationProvider implements OclOperationProvider`. QVT-R braucht dasselbe: OCL-Expressions in `when`/`where`-Klauseln und Domain-Patterns.

**Decision:** Gleiche Architektur für QVT-R:

```
QvtdEvaluator
  ├── handles: Relation, RelationDomain, DomainPattern, TemplateExp, Key, ...
  ├── owns: OclEvaluator (created internally)
  │     └── registered provider: QvtdOperationProvider
  └── delegates: OCL expressions in when/where/patterns → OclEvaluator

QvtdOperationProvider implements OclOperationProvider
  └── recognizes QVT-R Functions (§7.11.1.5) → re-enters QvtdEvaluator
```

**Blackbox:** QVT-R §7.8 definiert Blackbox-Operationen für Relations. Gleiches Interface-Pattern wie D24 (`QvtoBlackboxLibrary`), adaptiert für QVT-R: `QvtdBlackboxLibrary` mit denselben Prinzipien (typed interface, kein Reflection, OSGi Whiteboard).

**QVT-O→QVT-R Brücke (GAP-6):** Deferred bis QVT-R standalone läuft. Dann:

```
QvtoEvaluator
  └── GAP-6: refined relation → delegates to QvtdEvaluator
```

**Rationale:** Das bestehende Kompositions-Pattern (Delegation + OclOperationProvider Callback) hat sich in QVT-O bewährt. Kein neuer Mechanismus nötig. Die dreistufige Kette (QVT-O → QVT-R → OCL) ist eine natürliche Erweiterung.

---

### DR-D37: QVT-R Unit Resolver — Wiederverwenden

**Context:** D27 definiert `QvtoUnitResolver` für QVT-O (ServiceLoader + Whiteboard). QVT-R braucht denselben Mechanismus: Transformations-Dateien (`.qvtr`) nach qualifiziertem Namen auflösen.

**Options considered:**
- (A) Gemeinsames `TransformationUnitResolver` Interface für QVT-O und QVT-R — maximale Wiederverwendung, aber unterschiedliche Dateitypen/Parsing
- (B) Separates `QvtdUnitResolver` mit gleichem Pattern — sauber getrennt, gleiche Architektur, unabhängig erweiterbar
- (C) Ein Resolver der beides kann — über-engineered, vermischt Concerns

**Decision:** Option B — separates `QvtdUnitResolver` Interface, gleiche Architektur wie D27:
- Standalone: Registrierung über `QvtdConfiguration.builder().unitResolvers(...)`
- OSGi: Whiteboard `@Reference(MULTIPLE, DYNAMIC)`
- Security: gleiche Enable-Flag + Allow-List Patterns wie D29

**Rationale:** QVT-R und QVT-O haben unterschiedliche Dateiendungen, Parser und AST-Typen. Ein gemeinsames Interface würde zu generisch. Separate Interfaces mit gleicher Architektur sind klar verständlich und unabhängig testbar. Falls die QVT-O→QVT-R Brücke (GAP-6) kommt, kann `QvtoUnitResolver` an `QvtdUnitResolver` delegieren.

---

### DR-D38: QVT-R Phasenplan

**Context:** Phase 4 umfasst QVT-R (Relations, Spec Ch. 7). Die Spec ist umfangreich (~80 Sections). Die Arbeit muss in handhabbare Sub-Phasen zerlegt werden, analog zu Phase 2 (QVT-O, 10 Phasen) und Phase 3 (M2T, 7 Phasen).

**Decision:** Zweistufiger Ansatz:

**Phase 4a — QVT-R standalone:**

| Sub-Phase | Modul | Inhalt |
|-----------|-------|--------|
| P4-0 | `qvtd.model` | Ecore-Metamodell (3 EPackages: qvtbase, qvttemplate, qvtrelation), ~20 Classifiers |
| P4-1 | `qvtd.api` | `QvtdEngine` Interface, `QvtdConfiguration`, `QvtdResult`, `QvtdUnitResolver` |
| P4-2 | `qvtd.parser` | ANTLR4 Grammatik `QvtRelations.g4` (importiert `Ocl.g4`), CST→AST Builder |
| P4-3 | `qvtd.engine` | Core Engine: top relation execution, domain pattern matching, variable binding, when/where |
| P4-4 | `qvtd.engine` | Enforcement: checkonly/enforce semantics (§7.10), object creation via patterns, Key-based identity |
| P4-5 | `qvtd.engine` | Traces: implizite Trace-Erzeugung, RelationCallExp in when/where, non-top relation invocation |
| P4-6 | `qvtd.tests` | Spec-Conformance Tests, E2E-Tests mit UML→RDBMS Beispiel aus Spec |

**Phase 4b — QVT-R↔QVT-O Hybrid ✅ (teilweise abgeschlossen):**

| Sub-Phase | Inhalt | Status |
|-----------|--------|--------|
| P4b-1 | Parser: `refines` keyword (§8.4.7), Grammar (`moduleRefName` akzeptiert `::` und `.`), UnitBuilder (Stub Transformation + EAnnotation) | ✅ 12 Tests |
| P4b-2 | D39 Brücke: `QvtoEngineImpl implements RelationImplementationProvider`, `QvtdEngine.registerImplementationProvider()`, `QvtrEvaluator` Provider-First + Blackbox-Fallback | ✅ |
| P4b-3 | Runtime Stub-Auflösung: `OT.refined` → echte `RelationalTransformation` via QvtdEngine | ⏳ Phase 5 |
| P4b-4 | Integration-Tests (Hybrid QVT-O↔QVT-R) | ✅ 13 Tests |

**D40:** `qvtbase.ecore` in Shared-Bundle `org.eclipse.fennec.m2x.qvt.model` extrahiert (9 Classifiers).

**Verbleibende TODOs (deferred → Phase 5):**
- **P4b-3:** Stub-Auflösung `OT.refined` → echte RT zur Laufzeit. QVT-O Engine braucht Zugang zum QVT-R Engine (Reverse-Richtung). Erst relevant wenn QVT-O Engine eine QVT-R Transformation tatsächlich laden/ausführen soll.
- **GAP-13 Semantik:** `refines` pattern inheritance — Mapping populates same trace as refined Relation (§8.2.1.15). Erfordert P4b-3.

---

### DR-D39: QVT-R Modul-Abhängigkeiten — keine Zyklen durch Interface-Entkopplung

**Context:** Die QVT v1.3 Spec definiert bidirektionale Beziehungen zwischen QVT-R und QVT-O:
- §8.2.1.1: QVT-O `OperationalTransformation.refined` referenziert eine QVT-R `Transformation` → QVT-O ruft QVT-R
- §7.8: Eine QVT-R Relation kann eine Blackbox/QVT-O Implementierung haben → QVT-R ruft QVT-O
- §6.4: "QVTOperational extends QVTRelation" (Metamodell-Hierarchie)

Naiv umgesetzt ergäbe das zirkuläre Abhängigkeiten auf Model- und Engine-Ebene.

**Problem auf Model-Ebene:** Die Spec sagt `QVTOperational extends QVTRelation` (gemeinsames Trace-Framework). Unser `qvto.model` (59 Classifiers, 1050 Tests) existiert bereits mit eigenständigem Trace-Modell.

**Options considered:**
- (A) `qvto.model → qvtd.model` — spec-konform, aber Breaking Change: alle QVT-O Classifiers müssten auf `qvtbase`-Supertypen umgestellt werden, Tests refactored
- (B) Modelle unabhängig lassen — pragmatisch, kein Refactoring, Brücke über API-Interfaces
- (C) Gemeinsames `qvtbase.model` extrahieren — sauberer als A, aber trotzdem großes Refactoring

**Decision Model-Ebene:** Option B — `qvtd.model` und `qvto.model` bleiben unabhängig. Beide hängen von `ocl.model` ab, aber nicht voneinander.

**Rationale Model-Ebene:**
- `qvto.model` ist stabil und getestet. Refactoring wäre riskant und aufwändig.
- Die "gemeinsamen" Konzepte haben unterschiedliche Features: `OperationalTransformation` hat Extents, ModelParameters, EntryOperation — `RelationalTransformation` hat Relations, Keys, ModelTypes. Sie teilen nur den abstrakten Namen.
- Die Spec-Hierarchie ist ein formales Design, kein zwingendes Implementierungserfordernis.
- Die Brücke (GAP-6, §7.8) funktioniert über Interfaces auf API-Ebene.

**Problem auf Engine-Ebene:** Bidirektionaler Aufruf:
- `qvto.engine` will `qvtd.engine` aufrufen (GAP-6: `refined`)
- `qvtd.engine` will `qvto.engine` aufrufen (§7.8: Blackbox-Implementierung)

**Decision Engine-Ebene:** Interface-Entkopplung über `qvtd.api`:

```
qvtd.api definiert:
  ├── QvtdEngine                       — Engine Interface (execute, check)
  └── RelationImplementationProvider   — §7.8 Blackbox-Callback
        Object executeRelation(Relation, Map<Domain, EObject> bindings)
```

**Abhängigkeitsregeln:**

```
                         ocl.model
                        ↗         ↖
                 qvtd.model      qvto.model         (unabhängig!)
                      ↑               ↑
  ocl.api ←──── qvtd.api         qvto.api
     ↑           ↑     ↖            ↑
 ocl.engine  qvtd.engine  ───→ qvto.engine
     ↑           ↑                   ↑
 ocl.parser  qvtd.parser       qvto.parser
```

| Abhängigkeit | Richtung | Zweck |
|-------------|----------|-------|
| `qvtd.engine → ocl.engine` | ↓ | OCL-Expressions in when/where auswerten |
| `qvto.engine → qvtd.api` | → | GAP-6: `refined` über `QvtdEngine` Interface |
| `qvtd.engine → qvtd.api` | ↑ | Implementiert `QvtdEngine` |
| `qvto.engine → qvtd.api` | → | Registriert sich als `RelationImplementationProvider` |
| `qvtd.engine` → `qvto.*` | **VERBOTEN** | Kein Rückkanal — bricht den Zyklus |

**Schlüsselregel:** `qvtd.engine` kennt QVT-O nicht. Für §7.8 (QVT-R ruft QVT-O auf) nutzt `qvtd.engine` das `RelationImplementationProvider` Interface aus `qvtd.api`. `qvto.engine` registriert sich als Implementierung dieses Interface — genau wie `QvtoOperationProvider implements OclOperationProvider` (D25).

**Runtime-Wiring (Phase 4b):**

```java
// Standalone
QvtdEngine qvtdEngine = new QvtdEngineImpl(qvtdConfig);
QvtoEngine qvtoEngine = new QvtoEngineImpl(qvtoConfig, qvtdEngine);  // GAP-6
qvtdEngine.registerImplementationProvider(qvtoEngine);                // §7.8

// OSGi: automatic via DS @Reference injection
```

**Konsequenzen:**
- Phase 4a (QVT-R standalone) hat keine Abhängigkeit auf QVT-O — sauber isoliert
- Phase 4b fügt die Brücke hinzu, ohne qvtd.engine zu ändern — nur qvto.engine bekommt neue Dependency auf qvtd.api
- `RelationImplementationProvider` ist auch für reine Java-Blackboxes nutzbar (ohne QVT-O)
- Pattern ist bewährt: identisch zu OclOperationProvider (D25), QvtoBlackboxLibrary (D24)

**Update (Phase 4b):** D39 Option B wird durch D40 revidiert → `qvtbase` wird in eigenes Shared-Bundle extrahiert.

---

### DR-D40: QVTBase als Shared Bundle `org.eclipse.fennec.m2x.qvt.model`

**Context:** Phase 4b erfordert bidirektionale Verknüpfung: `OperationalTransformation.refined : Transformation` und `MappingOperation.refinedRelation : Relation`. D39 Option B (Modelle unabhängig, Brücke über EAnnotation + Engine-Auflösung) funktioniert, erfordert aber String-basierte Indirektion im Metamodell und Runtime-Auflösung im Engine.

**Problem:** `Transformation`, `Rule`, `TypedModel` etc. liegen in `qvtbase.ecore` innerhalb `qvtd.model`. Damit kann `qvto.model` keine typsicheren EReferences auf diese Typen haben, ohne D39 zu verletzen.

**Options considered:**
- (A) EAnnotation-basiert (D39 Status quo) — funktioniert, aber String-Hack, keine EMF-Typsicherheit
- (B) `qvtbase.ecore` in eigenes Bundle `org.eclipse.fennec.m2x.qvt.model` extrahieren — sauber, spec-konform, echte EReferences möglich

**Decision:** Option B — `qvtbase.ecore` wird in ein neues Shared-Bundle `org.eclipse.fennec.m2x.qvt.model` extrahiert. Dies revidiert D39 Option B auf Model-Ebene.

**Rationale:**
- Die QVT v1.3 Spec definiert QVTBase als gemeinsames Paket zwischen Relations und Operational (§6.4, §7.11.1, §8.2.1)
- `Transformation`, `TypedModel`, `Rule`, `Domain`, `Pattern`, `Predicate`, `Function`, `FunctionParameter`, `Tag` sind genuine Shared-Konzepte
- Echte EReferences (`refined : Transformation`, `refinedRelation : Relation`) sind typsicher und EMF-idiomatisch
- Kein Zyklus: beide Model-Bundles hängen vom gemeinsamen Base ab

**Neuer Abhängigkeitsgraph (Model-Ebene):**

```
              ocl.model
                 ↑
           qvt.model (qvtbase: Transformation, TypedModel, Rule, Domain, ...)
            ↗          ↖
     qvto.model       qvtd.model (qvtrelation + qvttemplate)
```

**Neuer Abhängigkeitsgraph (komplett):**

```
                         ocl.model
                            ↑
                        qvt.model
                        ↗       ↖
                 qvtd.model      qvto.model
                      ↑               ↑
  ocl.api ←──── qvtd.api         qvto.api
     ↑           ↑     ↖            ↑
 ocl.engine  qvtd.engine  ───→ qvto.engine
     ↑           ↑                   ↑
 ocl.parser  qvtd.parser       qvto.parser
```

**Änderungen:**
1. Neues Bundle `org.eclipse.fennec.m2x.qvt.model` mit `qvtbase.ecore` (9 Classifiers: Transformation, TypedModel, Domain, Rule, Pattern, Predicate, Function, FunctionParameter, Tag)
2. `qvtd.model`: Entfernt `qvtbase.ecore`, behält `qvtrelation.ecore` + `qvttemplate.ecore`, neue Dependency auf `qvt.model`
3. `qvto.model`: Neue Dependency auf `qvt.model`, neue EReferences: `OperationalTransformation.refined : Transformation [0..1]`, `MappingOperation.refinedRelation : Relation [0..1]`
4. Downstream-Bundles: Buildpath um `qvt.model` ergänzen wo nötig

**D39 Engine-Ebene bleibt unverändert:** Interface-Entkopplung über `qvtd.api` (QvtdEngine, RelationImplementationProvider) gilt weiterhin. Nur die Model-Ebene wird durch D40 revidiert.

**Konsequenzen:**
- EMF-Code muss für alle drei Model-Bundles neu generiert werden
- nsURI `http://www.eclipse.org/fennec/m2x/qvtd/qvtbase/1.0` bleibt (Abwärtskompatibilität)
- Bestehende Tests in `qvtd.tests` müssen Imports anpassen (Package wechselt von `qvtd.model` nach `qvt.model`)

---

### DR-D41: OCL IDE-Integration als isoliertes, platform-gekoppeltes Bundle

**Context:** Das Legacy-Eclipse-OCL unterstützt den generischen (reflektiven) EMF-Editor: OCL-Annotationen (Invarianten, abgeleitete Features, Operations-Bodies) werden beim Öffnen/Validieren eines Modells ausgewertet. Diese Fähigkeit soll für die Fennec-OCL-Engine nachgebaut werden. Mechanismus im Legacy-OCL: Registrierung der EMF-Delegates über die Extension-Points `org.eclipse.emf.ecore.{validation,setting,invocation,query}_delegate` per `plugin.xml`.

**Problem:** Das Projekt verbietet Eclipse-Platform-Abhängigkeiten (§17 — kein `org.eclipse.core.*`, `org.eclipse.ui.*`). Eine IDE-Integration setzt aber zwingend die Eclipse-Extension-Registry voraus.

**Options considered:**
- (A) Delegate-Logik in `plugin.xml` der Engine — koppelt das saubere Engine-Bundle an IDE-Belange
- (B) Eigenes, isoliertes Bundle `org.eclipse.fennec.m2x.ocl.ide` mit `plugin.xml`, das die vorhandenen Engine-Delegates über die Extension-Registry registriert

**Decision:** Option B. `org.eclipse.fennec.m2x.ocl.ide` ist die **einzige** platform-gekoppelte Komponente und bewusste Ausnahme zur "No Eclipse Platform"-Regel. Sie ist strikt isoliert: kein Engine-/Core-Bundle referenziert sie rückwärts; die Engines bleiben plain-Java/OSGi-optional.

**Scope (bewusst minimal):** Nur Delegate-Registrierung (validation/setting/invocation). **Kein** OCL-Editor, **keine** Konsole, **kein** Xtext (verboten). Query-Delegate vorerst nicht (Engine hat keine Query-Delegate-Logik; für die Annotation-Auswertung im generischen Editor nicht erforderlich).

**Rationale / Footprint:**
- Compile-Buildpath nur `org.eclipse.emf.ecore` + OCL-Bundles — **kein** `org.eclipse.core.*`/`org.eclipse.ui.*` (belegt im Manifest: `Import-Package` nur EMF + ocl.*). Die Extension-Registry ist reine Laufzeit-Host-Umgebung (im Ziel-Eclipse vorhanden), kein Buildpath-Eintrag.
- Keine Duplikation: die Engine-Delegate-Logik (inkl. `@pre`/Postcondition-Maschinerie) bleibt im `internal`-Package der Engine. Eine einzige additive public Klasse `OclDelegateFactories` (im exportierten Engine-Paket) reicht die internen Factories als EMF-Interface-Typen heraus; `internal` bleibt unexportiert.
- `ocl.ide` komponiert Parser+Engine (`SharedOclEngine`, lazy) — dieser Wiring-Schritt gehört nicht in die parser-agnostische Engine, sondern ins IDE-Bundle.
- Dual-URI: Delegates registriert unter Fennec-URI **und** Legacy-Eclipse-OCL-Pivot-URI → Modelle aus Eclipse-OCL werden direkt ausgewertet.

**Änderungen:**
1. Neue public Klasse `org.eclipse.fennec.m2x.ocl.engine.OclDelegateFactories` (additiv, keine Verhaltensänderung)
2. Neues Bundle `org.eclipse.fennec.m2x.ocl.ide`: `plugin.xml` + 3 no-arg Extension-Factories (`OclValidationDelegate`, `OclSettingDelegateFactory`, `OclInvocationDelegateFactory`) + `SharedOclEngine`; Paket `Private-Package` (nicht exportiert), `Bundle-ActivationPolicy: lazy`
3. Neues Test-Bundle `org.eclipse.fennec.m2x.ocl.ide.tests` (Plain JUnit 5, 4 Tests): registriert die Delegates in den statischen EMF-Registries (wie die Extension-Registry zur Laufzeit) und prüft `eGet`/`eInvoke`/`validate` inkl. Legacy-Pivot-URI

**Auslieferung:** p2-Repository über ein separates Projekt `org.eclipse.fennec.m2x.ocl.ide.p2` (bnd `-export … type=p2`) — nur OCL-Bundles; EMF/Equinox sind im Ziel-Eclipse host-seitig vorhanden. (Modul B, folgt.)

---

### DR-D42: `EPackage.Registry` als Eingabewert der Engine — genau ein Fallback-Punkt

**Context:** Typnamen in OCL-Ausdrücken, QVT-Transformationen und M2T-Templates werden zur Parse-Zeit gegen eine `EPackage.Registry` aufgelöst. Bisher greifen Parser- und Engine-Interna aller vier Engines direkt auf `EPackage.Registry.INSTANCE` zu (neun Fundstellen, siehe Issue #47).

**Problem:** Die statische Registry ist JVM-globaler Zustand mit der nsURI als Schlüssel. Unter OSGi — und überall dort, wo zwei Versionen einer nsURI koexistieren können — ist damit nicht definiert, *welches* `EPackage` die Engine auflöst. `emf.osgi` löst dieses Problem für Modell-Identität über den Fingerprint (inhaltsabgeleitet statt nsURI-basiert); eine Engine, die sich implizit aus der globalen Registry bedient, unterläuft das, weil sie eine Auflösungsentscheidung trifft, die dem Aufrufer gehört.

Der schwerste Fall war `QvtoAliasRegistry`: eine Suche nach einfachem Klassennamen über *alle* in der JVM registrierten Packages — das Ergebnis hängt davon ab, welche fremden Bundles zufällig aktiv sind.

**Options considered:**
- (A) Registry weiter global lesen, dafür Fingerprint-Prüfungen in den Engines — verlagert eine Aufrufer-Entscheidung in die Engine und macht sie von `emf.osgi` abhängig
- (B) Registry als Pflichtparameter ohne Fallback — bricht den Plain-Java-Fall ohne Not; dort *ist* die statische Registry die richtige Antwort
- (C) Registry als Eingabewert mit genau einem Fallback-Punkt

**Decision:** Option C.

1. **Engine-Interna lesen nie `EPackage.Registry.INSTANCE`.** Die Registry wird als Parameter durchgereicht.
2. **Ist keine Registry konfiguriert, gilt die statische.** Das ist der Plain-Java-/Non-OSGi-Fall: dort existiert keine Versions-Mehrdeutigkeit, und die statische Registry ist die korrekte Auflösung.
3. **Genau eine Stelle je Engine wendet diesen Fallback an** — dort, wo die Registry entgegengenommen wird, löst sie einmalig `registry != null ? registry : EPackage.Registry.INSTANCE` auf. Alles darunter bekommt die aufgelöste Registry.

**Nachtrag (2026-08-09): das ResourceSet ist die Standardform.** Eine `EPackage.Registry` ist in EMF kein Objekt, mit dem man üblicherweise hantiert — das ist das `ResourceSet`. Unter `emf.osgi` bekommt man einen konfigurierten, isolierten Stack als `ResourceSetFactory`/`ResourceSet`-Service injiziert; die Registry müsste man daraus erst auspacken. Genau diese Reibung führt dazu, dass Konfiguration weggelassen wird — also zu dem Zustand, den D42 verhindern soll.

Deshalb nimmt jede Engine zusätzlich ein `ResourceSet` entgegen und verwendet dessen `getPackageRegistry()`. Die Auflösungsreihenfolge ist:

1. **explizit gesetzte Registry** — das Speziellere schlägt das Allgemeinere,
2. sonst das **ResourceSet**,
3. sonst `EPackage.Registry.INSTANCE`.

Damit bleibt es bei genau einem Fallback-Punkt je Engine; er hat nur eine Stufe mehr davor. Dokumentiert wird künftig die ResourceSet-Form, `packageRegistry(…)` bleibt die tiefere Ebene.

Vom ResourceSet wird vorerst **ausschließlich** die Package-Registry benutzt — es wird nichts darüber geladen. Das steht so im Javadoc, damit niemand eine URI-Auflösung erwartet, die es noch nicht gibt; `parse(URI)` über den `URIConverter` ist ein eigener, folgender Schritt.

Welche Stelle das ist, folgt daraus, wer den Parser besitzt:

| Engine | Fallback-Punkt | Grund |
|---|---|---|
| OCL | `OclParserSupport`-Konstruktor (`new OclParserSupport()` → `INSTANCE`, `new OclParserSupport(registry)` → explizit) | Der Parser wird vom Aufrufer gebaut und in die `OclConfiguration` hineingereicht; die Engine kann ihm nachträglich nichts mitgeben, ohne das `OclExpressionParser`-Interface zu erweitern. Der Parser ist zugleich die einzige Komponente, die Classifier auflöst. |
| QVT-O, QVT-R, M2T | `<X>Configuration.packageRegistry(…)` | Diese Engines bauen ihren Parser-Support selbst; der Aufrufer erreicht ihn nur über die Konfiguration. |

**Rationale:** Der Fallback ist keine Ausnahme von der Regel, sondern ihre Anwendung an einer einzigen, sichtbaren Stelle. Wer die Auflösung kontrollieren muss — ein OSGi-Consumer, oder wer zwei Versionen einer nsURI hält — übergibt die selbst aufgelösten und geprüften Packages. Die Engine hat danach keine implizite Meinung mehr dazu, welche Modellversion eine URI bezeichnet; Versions-Identität bleibt beim Aufrufer, wo der Fingerprint sie ohnehin verortet.

Für den Bestand ist die Änderung verhaltensneutral: dieselbe Registry ist in Gebrauch, nur eine Ebene höher aufgelöst.

**Scope:** OCL, QVT-O, QVT-R, M2T. (Keine Paketversions-Anpassung — vor dem ersten Release werden Versionen nicht gepflegt.)

**Folge für OCL über das reine Durchreichen hinaus:** `OclParserSupport.parse(expression, contextType)` benutzte bisher überhaupt keine Registry — Typnamen in einem Ausdruck wurden nur über das Package des Kontexttyps aufgelöst, alles andere fiel still auf den Kontexttyp zurück. Mit D42 bekommt auch dieser Pfad die Registry, womit Typnamen aus fremden Packages korrekt auflösen. Das ändert Auflösungsergebnisse (bewusst: `self.oclIsKindOf(FremdesPackageTyp)` lieferte vorher `true`, weil der Name zum Kontexttyp degradierte).

**Nicht betroffen:** EMFs Delegate-Registries (`EOperation.Internal.InvocationDelegate.Factory.Registry`, `EStructuralFeature.Internal.SettingDelegate.Factory.Registry`, `EValidator.ValidationDelegate.Registry`) in `OclEngineImpl`. Die sind von EMF als global vorgesehen und gehören zum Delegate-Mechanismus selbst (D11).

**Nachgelagert:** Die OCL-Caches schlüsseln heute über nsURI bzw. einfachen Klassennamen und müssen auf Fingerprint-abgeleitete Schlüssel umgestellt werden (Issue #50) — dieselbe Identitätsfrage, eine Ebene tiefer.
