# QVT-R Architecture Reference

> Consolidated reference for the QVT-Relations (QVT-R) v1.3 implementation.
> Covers metamodel, API, parser, engine, traces, and design decisions.

## 1. Overview

Direct QVT-R interpretation engine (D33 — no QVT-C lowering). Implements QVT v1.3 Chapter 7 (Relations Language). Spec-first approach (D35) with Eclipse QVT-D as behavioral reference.

**Modules:**

| Module | Purpose |
|--------|---------|
| `org.eclipse.fennec.m2x.qvtd.model` | EMF metamodel (3 EPackages) |
| `org.eclipse.fennec.m2x.qvtd.api` | Public API interfaces |
| `org.eclipse.fennec.m2x.qvtd.parser` | ANTLR4 parser, CST→AST |
| `org.eclipse.fennec.m2x.qvtd.engine` | Direct interpretation engine |
| `org.eclipse.fennec.m2x.qvtd.tests` | Spec-conformance tests (160 tests) |

## 2. Metamodel (D34)

Three EPackages following the spec structure:

| EPackage | Classifiers | Key Types |
|----------|-------------|-----------|
| `qvtbase` | 7 | `Transformation`, `TypedModel`, `Domain`, `Pattern`, `Predicate`, `Rule`, `Function` |
| `qvtrelation` | 8 | `RelationalTransformation`, `Relation`, `RelationDomain`, `DomainPattern`, `RelationCallExp`, `RelationImplementation`, `Key`, `OppositePropertyCallExp` |
| `qvttemplate` | 3 | `TemplateExp`, `ObjectTemplateExp` (+`PropertyTemplateItem`), `CollectionTemplateExp` |

**Key relationships:**
- `RelationalTransformation` extends `Transformation`, owns `Relation` instances and `Key` declarations
- `Relation` extends `Rule`, has `domain : RelationDomain[*]`, `when : Pattern`, `where : Pattern`
- `RelationDomain` extends `Domain`, has `pattern : DomainPattern`, references `TypedModel`
- `DomainPattern` extends `Pattern`, has `templateExpression : TemplateExp`
- `ObjectTemplateExp` has `bindsTo : Variable`, `referredClass : EClass`, `part : PropertyTemplateItem[*]`

## 3. API

| Interface/Class | Purpose |
|-----------------|---------|
| `QvtdEngine` | Main engine interface: `parse()`, `execute()` |
| `QvtdConfiguration` | Engine config: OCL config, blackbox registry, package registry (D42 — the single place applying the `EPackage.Registry.INSTANCE` fallback) |
| `QvtdExecutionContext` | Execution mode (enforce/checkOnly), model extents, target model |
| `QvtdExecutionResult` | Diagnostics, success flag |
| `QvtdModelExtent` | Wraps `Resource` contents for a typed model |
| `QvtdBlackboxRegistry` | Registry for blackbox libraries (GAP-9, GAP-12) |
| `QvtdBlackboxLibrary` | Individual blackbox library: `invoke(name, self, args)` |
| `QvtdUnit` | Sealed interface: `SourceUnit` (string) / `CompiledUnit` (parsed AST) |
| `QvtdUnitResolver` | Resolves import URIs to units (future: GAP-10) |
| `QvtdParseException` | Parse errors with `Resource.Diagnostic` list |

### 3.x `compile()` — the storable form

`QvtdEngine.compile(source, unitName)` returns a `CompiledUnit` beside `parse()`'s
`RelationalTransformation`. The distinction is ownership: `parse()` yields the in-memory graph, and
that graph references objects the parser created but nothing contains — relation variables, type
instances, synthetic features. Measured on a relation with a query, a when and a where clause:
twelve such objects, and `save()` fails on the first. `compile()` moves them into
`CompiledUnit.satellite` so the whole document saves, loads and copies; the script inside is the
same `RelationalTransformation` and executes as before. The mechanism is language-neutral
(`UnitPackager`, `SatelliteCollector` in `org.eclipse.fennec.m2x.unit`) — the QVT-O architecture
describes it in full.

**Dependencies (#139).** `compile(source, unitName, UnitCompileOptions)` resolves every `import`
through the configured unit resolvers (D29 gate and allow-list as in `execute()`) and binds by the
dependency mode. QVT-R binds an import by *merging* the imported relations into the importer
(§7.11.1.1, `QvtdLinker.merge`) — afterwards nothing refers to the imported unit. Hence:

- **embed** — the dependency is compiled and merged at compile time, and the name is struck from the
  `qvtr.imports` annotation. The merged rules *are* the embedding; no unit is attached under
  `CompiledUnit.embedded`, it would be dead weight nothing points at. The `dependencyEntry` records
  the name and the fingerprint of what was merged. The unit runs on an engine without resolvers.
- **pin** — the dependency is compiled to learn its fingerprint; nothing is merged, the import stays
  declared and the execute-time linker (or prepare, #140) merges it.
- **rebind** — the name alone; import stays declared.

A blackbox query — a `Function` declared without a body, evaluated through the blackbox registry
(§7.8) — becomes a `BlackboxRequirement` naming the operation and the fingerprint of its signature
(`name(params):return`, types as `nsURI#Name`). QVT-R addresses blackbox operations by name across
all registered libraries, so the requirement names no provider. Package entries and copies of dynamic
metamodels are recorded as for every language (QVT-O architecture §4.w).

## 4. Parser

**Grammar:** `QvtR.g4` imports `Ocl.g4` (shared OCL grammar).

**Architecture:** Two-visitor composition pattern (like QVT-O):
- `QvtrUnitBuilder` — Main CST→AST visitor. 2-pass for transformation-level constructs:
  - Pass 1: Build all transformations, typed models, relations, domains, templates
  - Pass 2: Resolve `extends` inheritance (rule merging with `EcoreUtil.copy()`)
- `QvtrExpressionBuilder` — OCL expression visitor, delegates to `AbstractExpressionBuilder` (shared with OCL/QVT-O parsers)

**Key grammar extensions beyond OCL:**
- `transformationDef`, `modelDecl`, `keyDecl`, `relation`, `domain`
- `objectTemplate`, `collectionTemplate`, `propertyTemplate`
- `whenClause`, `whereClause`, `queryDef`
- `optionalMultiplicity` (`[?]`) — Eclipse extension (GAP-5)
- `implementedby` clause on domains (GAP-12)

**Post-parse validation:**
- `QvtrBindingValidator` — §7.5 binding restriction analysis (run after AST build)

**EAnnotation conventions** (metadata without metamodel changes):
| Annotation Source | On | Purpose |
|---|---|---|
| `qvtr.optional` | `DomainPattern` | Marks `[?]` optional root variable |
| `qvtr.extends` | `RelationalTransformation` | Stores base transformation name |
| `qvtr.implementedby.args` | `RelationImplementation` | Stores argument expression references |
| `qvtr.imports` | `RelationalTransformation` | Names of imported units still to merge; struck by `compile()` under `embed` |

## 5. Engine

### 5.1 Core Components

| Class | Purpose |
|-------|---------|
| `QvtdEngine` | Facade: parse + execute, OCL engine composition (D36) |
| `QvtrEvaluator` | Core evaluator: relation execution, domain classification, when/where-clauses |
| `QvtrEnforcer` | Target domain enforcement (§7.10.2), key-based identity (§7.4), property enforcement |
| `QvtrBlackboxBridge` | Blackbox library invocation (§7.8), `implementedby` delegation (§7.11.3.6), M-R5 security |
| `QvtrQueryEvaluator` | Query resolution (§7.11.4), where-clause variable pre-computation |
| `QvtrOclCallback` | Functional interface: breaks circular deps between helpers and evaluator |
| `QvtrPatternMatcher` | Pattern matching: object/collection templates, variable binding |
| `QvtrEvalEnvironment` | Variable scope stack (push/pop) |
| `QvtrExtentManager` | Model extent management: typed model → extent mapping, `allInstances()` |
| `QvtrTraceManager` | Implicit trace: `TraceRecord(relation, bindings)`, trace lookup for `RelationCallExp` |

### 5.2 Execution Flow

1. **`execute(transformation, context)`** → `QvtrEvaluator.execute()`
2. For each **top relation** (skip abstract, skip overridden):
   a. Evaluate the when-clause's **relation calls** → pre-bindings (RelationCallExp → trace lookup)
   b. **Classify domains**: source vs. target (based on `isEnforceable()` + target model)
   c. **Match source domains** → list of binding maps
   d. For each source binding:
      - Check **optional null** (`[?]`) → skip if root variable is null
      - Evaluate the **remaining when-predicates** against this binding → skip the binding if one is false
      - **Pre-compute where bindings** (query calls, computed values)
      - **Enforce target domain** (or check-only verify)
      - Record **trace**
      - Evaluate **where-clause** (may invoke non-top relations)

The when-clause is split across steps (a) and (d) on purpose. §7.10.2 evaluates the when-condition
over a *joint* binding — "for each valid binding of variables of the when clause **and variables of
domains other than the target domain k** that satisfy the when condition and source domain patterns"
— and §7.10 states outright that "the intersection of a domain variable set and when variable set
need not be null". So a predicate may read what the domains bind and can only be evaluated once a
candidate binding exists. Relation calls are the exception: they *produce* bindings that then
restrict what the patterns match (§7.2.3), so they run before the match and reject the relation as a
whole when the trace holds nothing.

Evaluating the whole when-clause up front, which is what happened until #145, left every non-call
predicate with an empty binding and reported `Unresolved variable` — for transformations written in
the style of the spec's own `UmlToRdb` example.

### 5.3 Domain Classification (§7.10)

```
if (!checkOnly && rd.isIsEnforceable() && isTargetModel(rd.getTypedModel()))
    → targetDomain
else
    → sourceDomain
```

For **in-place** transformations (GAP-7): same TypedModel can appear in both source and target. `isIsEnforceable()` distinguishes `enforce` from `checkonly` domains.

### 5.4 Enforcement (§7.10.2)

Extracted into `QvtrEnforcer` for testability and separation of concerns.

1. Try **match** target domain against existing elements
2. Apply **default value assignments** (§7.11.3.7) for unbound variables
3. Try **`implementedby`** (§7.11.3.6) via `QvtrBlackboxBridge` — delegate to `RelationImplementationProvider` or blackbox registry
4. If no match → **create** via `ObjectTemplateExp`:
   a. Check already-bound variable (e.g. from when-clause or shared variable)
   b. Look up by **Key** (`key T { prop1, prop2 }`) — identity-based find-or-create (§7.4)
   c. Look up by **bound variables** (`findByBoundVariables()`) — for in-place (§7.7)
   d. **Instantiate** (`EcoreUtil.create()`) + set features from template
5. For `CollectionTemplateExp` (GAP-1): create collection members

### 5.4.1 Blackbox Bridge (§7.8)

Extracted into `QvtrBlackboxBridge` for testability and security centralization (M-R5).

- `invokeImplementedBy()` — searches `RelationImplementationProvider`s first (D39 hybrid), then falls back to blackbox registry
- `evaluateBlackboxQuery()` — delegates query calls without body to registered libraries
- `isBlackboxAllowed()` — enforces `allowedBlackboxModules` allow-list
- All blackbox access gated by `config.blackboxEnabled()` (disabled by default)

### 5.4.2 Query Evaluation (§7.11.4)

Extracted into `QvtrQueryEvaluator` for testability.

- `resolveQuery()` — finds `Function` by name from the transformation's synthetic `_queries` EClass
- `evaluateQueryCall()` — binds parameters and evaluates query body, or delegates to blackbox
- `evaluateExprWithQueries()` — expression evaluation with automatic query call resolution
- `preComputeWhereBindings()` — pre-computes where-clause `var = expr` bindings before enforcement

### 5.5 Pattern Matching

`QvtrPatternMatcher.matchDomain()`:
- Iterates `DomainPattern.templateExpression`
- For `ObjectTemplateExp`: bind root variable, match `PropertyTemplateItem` values
- For `CollectionTemplateExp`: match collection members with `++` rest-variable
- **Optional `[?]`** (GAP-5): if no instances match, bind root to `null` (vacuous match)
- Returns `List<Map<String, Object>>` — all valid binding combinations

### 5.6 Traces (§7.2.1)

- `QvtrTraceManager` stores `TraceRecord(relation, Map<String, Object>)` per execution
- `RelationCallExp` in **when-clause** → trace lookup (the called relation must have matching trace)
- `RelationCallExp` in **where-clause** → invokes non-top relation with pre-bindings

### 5.7 OCL Integration (D36)

- Engine delegates OCL evaluation to `OclEngine.evaluate(expr, ctx)` via composition
- `QvtrExpressionBuilder` shares `AbstractExpressionBuilder` with OCL parser
- OCL context created per evaluation: `OclContext(null, null, bindings)`

## 6. Implemented Features

| Feature | Spec Reference | Implementation |
|---------|---------------|----------------|
| Top relations | §7.10.1 | Iterate + execute |
| Non-top relations | §7.10.2 | Via where-clause `RelationCallExp` |
| When-clause | §7.2.1 | Predicate eval + trace lookup |
| Where-clause | §7.2.1 | Predicate eval + relation invocation |
| Checkonly domains | §7.10 | Match-only, no enforcement |
| Enforce domains | §7.10.2 | Find-or-create via templates |
| Key-based identity | §7.4, §7.13.4 | `enforceObjectTemplate()` key lookup |
| Object templates | §7.11.2 | Full property matching + binding |
| Collection templates | §7.11.2 | Member matching with `++` rest |
| Primitive domains | §7.11.3.10 | Variable binding only |
| Abstract relations | §7.11.3.2 | `isAbstract` skip + `overrides` |
| Blackbox queries | §7.11.3.6 | `QvtdBlackboxRegistry` |
| Transformation extends | §7.11.1.1 | Rule merging with copy |
| `implementedby` | §7.11.3.6 | `RelationImplementation` + blackbox |
| In-place transforms | §7.7 | `isEnforceable()` + variable lookup |
| Optional `[?]` | Eclipse ext. | `DomainPattern` EAnnotation, null binding |
| Default values | §7.11.3 | `default_values { ... }` assignment |
| Opposite properties | §7.11.2 | `opposite(T::prop)` in templates |
| Binding validation | §7.5 | `QvtrBindingValidator` — post-parse static analysis |
| Standard Library | §7.12 | OCL standard library (spec: "no additional operation") |

### 6.1 Binding Validation (§7.5)

The parser performs a post-parse static analysis via `QvtrBindingValidator` to enforce
§7.5 "Restrictions on Expressions". For each relation, the validator:

1. **Collects binding sites** — variables bound by:
   - Explicit variable declarations
   - When-clause `RelationCallExp` arguments (bound from trace)
   - Domain template patterns (`ObjectTemplateExp.bindsTo`, `PropertyTemplateItem` simple VariableExp, `CollectionTemplateExp.bindsTo`/`rest`/members)
   - Primitive domain root variables

2. **Checks each domain as potential target** — since the target domain depends on execution direction, the validator verifies that for each domain D, all non-binding variable references in D's template expressions are satisfiable from external sources (when-clause + other domains + variable declarations).

3. **Checks when/where clauses** — all variable references must be bound by appropriate sources.

4. **Respects local scopes** — variables introduced by `LetExp`, `IteratorExp`, and `IterateExp` are correctly scoped and not flagged as unresolved.

Violations are reported as `QvtdParseException` with `Resource.Diagnostic` entries referencing §7.5.

## 7. Known Gaps (deferred)

| Gap | Feature | Reason | Target |
|-----|---------|--------|--------|
| GAP-10 | Multi-file import | Needs file resolution infrastructure | Phase 5 |
| GAP-13 | Change propagation (§7.6) | Semantically ≡ full re-execution | Phase 5 |
| GAP-6 | QVT-O `refined` — stub resolution | Parser + Provider-Brücke ✅ (Phase 4b). Runtime Stub-Auflösung (`OT.refined` → echte RT) + Trace-Semantik ausstehend | Phase 5 |

## 8. Design Decisions

| ID | Decision | Rationale |
|----|----------|-----------|
| D33 | Direct interpretation (no QVT-C) | Simpler, no intermediate representation |
| D34 | 3 EPackages (qvtbase/qvttemplate/qvtrelation) | Follows spec package structure |
| D35 | Spec-first approach | QVT v1.3 Ch. 7 as primary, Eclipse as reference |
| D36 | Evaluator composition (QvtrEvaluator → OclEngine) | Reuse OCL engine, no duplication |
| D37 | UnitResolver for imports | Future extensibility (GAP-10) |
| D38 | TraceManager for implicit traces | No explicit trace model needed |
| D39 | Module independence (qvtd.model ↔ qvto.model) | No circular dependencies |

## 9. Test Structure

| Test Class | Focus | Tests |
|-----------|-------|-------|
| `QvtdEngineBasicTest` | Basic engine: parse, execute, empty | ~10 |
| `QvtdEnginePatternTest` | Pattern matching: objects, collections, nesting | ~15 |
| `QvtdEngineKeyTest` | Key-based identity and enforcement | ~10 |
| `QvtdEngineTraceTest` | Traces, RelationCallExp, non-top relations | ~10 |
| `QvtdE2EUmlToRdbmsTest` | End-to-end UML→RDBMS transformation | ~5 |
| `QvtdSpecConformanceTest` | Systematic spec conformance (per section) | ~46 |
| `QvtdBindingValidatorTest` | §7.5 binding validation (valid/invalid/edge) | 16 |
| `QvtdHybridIntegrationTest` | §7.8 QVT-R↔QVT-O hybrid (Phase 4b) | 13 |
| `QvtdSecurityHardeningTest` | BSI TR-03185 security mitigations (M-R2–M-R8) | 9 |
| `QvtrEnforcerTest` | Unit tests: enforcement, key lookup, feature resolution | 12 |
| `QvtrQueryEvaluatorTest` | Unit tests: query resolution, body/blackbox evaluation | 8 |
| `QvtrBlackboxBridgeTest` | Unit tests: allow-list, blackbox delegation | 6 |

**Total: 160 tests, 0 failures, 2 @Disabled**
