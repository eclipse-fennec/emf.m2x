# QVT-R Architecture Reference

> Consolidated reference for the QVT-Relations (QVT-R) v1.3 implementation.
> Covers metamodel, API, parser, engine, traces, and design decisions.

## 1. Overview

Direct QVT-R interpretation engine (D33 — no QVT-C lowering). Implements QVT v1.3 Chapter 7 (Relations Language). Spec-first approach (D35) with Eclipse QVT-D as behavioral reference.

**Modules:**

| Module | Purpose |
|--------|---------|
| `org.eclipse.fennec.m2x.qvtd.model` | EMF metamodel (2 EPackages: qvtrelation, qvttemplate; qvtbase lives in `qvt.model`, D40) |
| `org.eclipse.fennec.m2x.qvtd.api` | Public API interfaces |
| `org.eclipse.fennec.m2x.qvtd.parser` | ANTLR4 parser, CST→AST |
| `org.eclipse.fennec.m2x.qvtd.engine` | Direct interpretation engine |
| `org.eclipse.fennec.m2x.qvtd.tests` | Spec-conformance tests (§9) |

## 2. Metamodel (D34)

Three EPackages following the spec structure:

| EPackage | Classifiers | Key Types |
|----------|-------------|-----------|
| `qvtbase` | 9 | `Transformation`, `TypedModel`, `Domain`, `Pattern`, `Predicate`, `Rule`, `Function`, `FunctionParameter`, `Tag` |
| `qvtrelation` | 8 | `RelationalTransformation`, `Relation`, `RelationDomain`, `DomainPattern`, `RelationCallExp`, `RelationImplementation`, `RelationDomainAssignment`, `Key` |
| `qvttemplate` | 3 | `TemplateExp`, `ObjectTemplateExp` (+`PropertyTemplateItem`), `CollectionTemplateExp` |

**Key relationships:**
- `RelationalTransformation` extends `Transformation`, owns `Relation` instances and `Key` declarations
- `Relation` extends `Rule`, has `domain : RelationDomain[*]`, `when : Pattern`, `where : Pattern`
- `RelationDomain` extends `Domain`, has `pattern : DomainPattern`, references `TypedModel`
- `DomainPattern` extends `Pattern`, has `templateExpression : TemplateExp`
- `ObjectTemplateExp` has `bindsTo : Variable`, `referredClass : EClass`, `part : PropertyTemplateItem[*]`

**Deliberate deviations from the spec metamodel** (Ecore has no multiple inheritance from the
MOF classes the spec names):
- `Transformation` extends only `EPackage`, not `Class`. Its queries (`Function`) live in a
  synthetic `EClass` named `_queries` inside the transformation (DR-D22).
- `FunctionParameter` extends only `EParameter`, not `Variable`.
- `RelationImplementation.impl` is a synthetic `EOperation` that the transformation does not
  contain. The engine resolves it at run time to the operation that the `implementedby` clause
  names (D39, #282).

`Rule.overrides` is set by the parser, after the rules an extending transformation inherits are
merged. An overrides clause naming no rule of the transformation is a positioned parse error (#356).

`CollectionTemplateExp.referredCollectionType` and the parameter and result types of a query are
the OCL `CollectionType` itself, kind and element type included (#354). The compiled unit carries
it as a satellite.

## 3. API

| Interface/Class | Purpose |
|-----------------|---------|
| `QvtdEngine` | Main engine interface: `parse()`, `execute()` |
| `QvtdConfiguration` | Engine config: OCL config, blackbox registry, package registry (D42 — the single place applying the `EPackage.Registry.INSTANCE` fallback) |
| `QvtdExecutionContext` | Execution mode (enforce/checkOnly), model extents, target model, `withPostCheck()` |
| `QvtdExecutionResult` | Diagnostics, success flag |
| `QvtdModelExtent` | Wraps `Resource` contents for a typed model |
| `QvtdBlackboxRegistry` | Registry for blackbox libraries (GAP-9, GAP-12) |
| `QvtdBlackboxLibrary` | Individual blackbox library: `invoke(name, self, args)` |
| `QvtdUnit` | Sealed interface: `SourceUnit` (string) / `CompiledUnit` (parsed AST) |
| `QvtdUnitResolver` | Resolves import URIs to units; implemented by the store and service resolvers |
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
| `QvtrQueryEvaluator` | Query resolution (§7.11.1.5 Function), where-clause variable pre-computation |
| `QvtrOclCallback` | Functional interface: breaks circular deps between helpers and evaluator |
| `QvtrPatternMatcher` | Pattern matching: object/collection templates, variable binding |
| `QvtrEvalEnvironment` | Variable scope stack (push/pop) |
| `QvtrExtentManager` | Model extent management: typed model → extent mapping, `allInstances()` |
| `QvtrTraceManager` | Implicit trace: `TraceRecord(relation, bindings)`, trace lookup for `RelationCallExp` |

### 5.2 Execution Flow

1. **`execute(transformation, context)`** → `QvtrEvaluator.execute()`
2. Order the top relations (`QvtrRelationOrder`, #349): each runs after the relations its
   when clause calls. A when call to a non-top relation depends on the top relations whose where
   clauses invoke it. Relations that depend on each other form a component, which runs until it
   adds no trace record. The result does not depend on the order of declaration (§7.2.2).
3. For each **top relation** (skip abstract — §7.11.1.4 "never matched directly"). An overridden relation runs, but a binding that an overriding relation applies to is left to it: its when clause and the patterns of its non-target domains match the binding's roots (decided in #356). A top-level overriding relation covers the binding in its own run. Any other one, and every one reached through a where call, is invoked with the roots. An abstract relation reached for a binding no override applies to is an ERROR:
   a. Evaluate the when-clause's **relation calls** → one pre-binding per matching trace (RelationCallExp → trace lookup)
   b. **Classify domains**: source vs. target (based on `isEnforceable()` + target model)
   c. **Match source domains** → list of binding maps
   d. For each source binding:
      - Check **optional null** (`[?]`) → record the trace and skip the rest if the root variable is null
      - Evaluate the **remaining when-predicates** against this binding → skip the binding if one is false
      - **Pre-compute where bindings** (query calls, computed values)
      - **Enforce target domain** (or check-only verify) → skip the binding if that reported an ERROR
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
check-only run (or post-check)  → every model domain in turn is direction k, the others are sources
enforce run:
  enforce domain of the target model        → targetDomain (enforced)
  else a domain of the target model         → checked direction k (§7.2.3, reported, not enforced)
  every other model domain                  → sourceDomain
```

For **in-place** transformations (GAP-7): the same TypedModel can appear in both source and target. If an `enforce` domain is in the target model, a `checkonly` domain of the same model stays a source domain.

§7.7: "Execution proceeds as if the source and target models are distinct". When a relation has two domains on the target model, the evaluator copies the target model before the run (`EcoreUtil.Copier`). Source domains match the copy (`QvtrExtentManager.matchingSources`), and enforcing writes the model itself; before enforcing, objects of the copy in a binding are replaced by their originals. A run therefore never sees its own output as source (#353). The copy is dropped once the run is done, so a post-check sees the model as it is.

**Changed status** (§7.7, #353): `QvtdExecutionResult.modelChanged()`. An `EContentAdapter` on the roots of the target model notes every change in an existing tree, and a comparison of the roots before and after notes roots that were added or removed.

### 5.3.1 Checking (§7.9, §7.10.1, #348)

`checkDirection` checks one direction k. For every binding of the when clause and the other domains
that satisfies the when condition, there must be a match of domain k for which the where
condition holds. In checking, relation calls in `where` are evaluated as checks by `relationHolds`:
the called relation holds if some joint binding of its domains extends the arguments. A binding
without such a match is reported as a WARNING with code `QvtdExecutionResult.INCONSISTENCY`, naming
relation, direction and binding. `isSuccess()` stays true and `isConsistent()` becomes false.
A binding that holds is traced, so relations whose when clause depends on it are checked as well.
Empty models hold vacuously and report nothing.

An enforce run checks only the domains of the target model that are not enforced (§7.2.3).
`QvtdExecutionContext.withPostCheck()` adds a check of every relation in every direction once
enforcing is done.

### 5.4 Enforcement (§7.10.2)

Extracted into `QvtrEnforcer` for testability and separation of concerns.

1. Try **match** target domain against existing elements: the first match that also satisfies the where predicates it binds the variables of (§7.10.2, #351)
2. Apply **default value assignments** (§7.11.3.7) for unbound variables
3. Try **`implementedby`** (§7.11.3.6) via `QvtrBlackboxBridge` — only when the target does not match yet: invoke the named operation through a `RelationImplementationProvider` or the blackbox registry, then re-match; still no match is an error (#282)
4. If no match → **create** via `ObjectTemplateExp`:
   a. Check already-bound variable (e.g. from when-clause or shared variable)
   b. Look up by **Key** (§7.4, #350): every key of the class or a superclass (`isSuperTypeOf`; the
      keys of the most specific class take precedence) that the template gives a value for each
      part of. A part whose value is an object template takes the object it binds or enforces; an
      `opposite(C::p)` part matches the candidate held by the owner's `p`. One complete key match
      selects the object. A part whose value cannot be determined is an ERROR.
   c. **Instantiate** (`EcoreUtil.create()`) + set features from template. Without a key nothing
      else identifies an object — the earlier selection by bound variables took over foreign
      objects and is gone (decided in #350).
5. For `CollectionTemplateExp` (GAP-1): create collection members
6. Every object created for the domain that no container holds becomes a root of the target extent —
   also a container created by a nested template that holds the object through an opposite (#351).

**Clashes** (§7.10.2, #351): a run-local register remembers which relation set a single-valued
feature of an object. A different value for the same feature in the same run is an ERROR, and the
first value stays. Values the object had before the run are modified, not a clash.

**where** (§7.2.1, #351): a where predicate that reads only what the sources bind and is false is an
ERROR, and nothing is created for that binding. A where predicate that is false after enforcing is an
ERROR.

**Deletion** (§7.10.2, #352): after all relations ran, each binding of an enforced target domain that
no trace of its relation holds marks the objects bound to the domain's template variables — root and
nested. Those that no trace of any relation holds are deleted, together with the references the target
model holds to them. Never in a check, never towards a checkonly domain, never outside the target
model, and not at all when the trace limit cut the trace. `QvtdConfiguration.deleteUnmatched(false)`
switches it off.

### 5.4.1 Blackbox Bridge (§7.8)

Extracted into `QvtrBlackboxBridge` for testability and security centralization (M-R5).

- `invokeImplementedBy()` — asks `RelationImplementationProvider`s for the operation the clause names (D39 hybrid), then the blackbox registry; arguments are the clause's, i.e. the domains' root objects; unserved is an error, never a silent fallback to QVT-R enforcement (#282)
- `evaluateBlackboxQuery()` reports an unserved query once per run with the reason (#283)
- A relation with `implementedby` that goes beyond the §7.8 restrictions (domains not primitive or simple templates, when/where defining variables) gets one positioned WARNING per run from `QvtrEvaluator` — "should" in the spec, and the check before and after the operation keeps the general form safe (#287)
- `evaluateBlackboxQuery()` — delegates query calls without body to registered libraries
- `isBlackboxAllowed()` — enforces `allowedBlackboxModules` allow-list
- All blackbox access gated by `config.blackboxEnabled()` (disabled by default)
- Under OSGi the libraries come from `DefaultQvtdEngine`'s dynamic `0..n` reference `blackboxes` (bind and unbind methods, #269): no restart when a library comes or goes, each run reads what is bound, the higher `service.ranking` wins a name; `blackboxes.target` and `blackboxes.cardinality.minimum` narrow the set and make the engine wait for it. A whiteboard on purpose — the engine selects libraries by operation name, so it has to know them all

### 5.4.2 Query Evaluation (§7.11.1.5)

Extracted into `QvtrQueryEvaluator` for testability.

- `resolveQuery()` — finds `Function` by name from the transformation's synthetic `_queries` EClass
- `evaluateQueryCall()` — binds parameters and evaluates query body, or delegates to blackbox
- `evaluateExprWithQueries()` — expression evaluation with automatic query call resolution
- `preComputeWhereBindings()` — pre-computes where-clause `var = expr` bindings before enforcement

### 5.5 Pattern Matching

`QvtrPatternMatcher.matchDomain()`:
- Iterates `DomainPattern.templateExpression`. Several templates of one domain are matched jointly, as the cross product of their roots: each pattern extends the bindings of the ones before it (§7.11.3.3, #355)
- For `ObjectTemplateExp`: bind root variable, match `PropertyTemplateItem` values
- A variable bound to `null` counts as bound: `name = n, kind = n` does not match a null name with a kind (#355)
- A nested template on a single-valued reference matches only an object of its class or a subclass (§7.10.3.2)
- `opposite(C::p)` for a `p` without an eOpposite: the owners are the instances of `C` in the object's model whose `p` holds it (§7.3). Enforcing adds the object to the owner's `p` (#355)
- Variables with an initializer are bound to its value before matching — a fixed value that constrains the match (decided in #357)
- For `CollectionTemplateExp` (§7.11.2.3, #354): the members match distinct elements — a variable, an object template or `_` each consumes one — and the rest is bound to the elements they leave. Without a rest, nothing may be left. For a Sequence or OrderedSet, the members are the head of the sequence, in order. For a Set or OrderedSet, the values must be unique. The template's `where` filters the bindings, and a single-valued property is a singleton collection (§7.11.2.4)
- **Optional `[?]`** (GAP-5): if no instances match, bind root to `null` (vacuous match)
- Returns `List<Map<String, Object>>` — all valid binding combinations

### 5.6 Traces (§7.2.1)

- `QvtrTraceManager` stores `TraceRecord(relation, Map<String, Object>)` per execution
- `RelationCallExp` in **when-clause** → trace lookup (the called relation must have a matching
  trace). Every matching trace gives a binding: arguments that are already bound select the
  records, and unbound ones are bound from each of them (§7.2.3, #349).
- A trace is recorded only for a binding the relation holds for. If the target could not be
  enforced (an ERROR during enforcement), no trace is recorded and no when clause can count on
  it. A `[?]` root bound to null holds and is traced, for top and non-top relations alike.
- Equal records are kept once; `size()` grows only with a new binding, which ends the fixpoint
  of a cycle.
- `RelationCallExp` in **where-clause** → invokes non-top relation with pre-bindings

### 5.7 OCL Integration (D36)

- Engine delegates OCL evaluation to `OclEngine.evaluate(expr, ctx)` via composition
- `QvtrExpressionBuilder` shares `AbstractExpressionBuilder` with OCL parser
- OCL context created per evaluation: `OclContext(null, null, bindings)`

## 6. Implemented Features

| Feature | Spec Reference | Implementation |
|---------|---------------|----------------|
| Top relations | §7.10.1, §7.2.2 | Dependency order, fixpoint for cycles (`QvtrRelationOrder`) |
| Non-top relations | §7.10.2 | Via where-clause `RelationCallExp` |
| When-clause | §7.2.1 | Predicate eval + trace lookup, one binding per matching trace |
| Where-clause | §7.2.1 | Predicate eval + relation invocation |
| Checkonly domains | §7.9, §7.10.1, §7.2.3 | Every direction checked; violations are `INCONSISTENCY` warnings, `isConsistent()` false |
| Enforce domains | §7.10.2 | Find-or-create via templates, clash detection, deletion of what no binding requires |
| Key-based identity | §7.4, §7.13.4 | `findByKey()`: all keys of the class and its superclasses, template and opposite parts |
| Object templates | §7.11.2 | Full property matching + binding |
| Collection templates | §7.11.2.3, §7.11.2.4 | Disjoint members, rest, Sequence head, uniqueness, where, singleton for single-valued properties; kind rules checked by the parser |
| Primitive domains | §7.13.4 | Variable binding only |
| Abstract relations | §7.11.1.4 | `isAbstract` skip; `overrides` replaces the base per binding where the overriding conditions hold, also for where calls |
| Blackbox queries | §7.11.3.6 | `QvtdBlackboxRegistry` |
| Transformation extends | §7.11.1.1 | Rule merging with copy |
| `implementedby` | §7.11.3.6 | `RelationImplementation` + blackbox |
| In-place transforms | §7.7 | Source domains match a copy of the state before the run; `modelChanged()` status |
| Optional `[?]` | Eclipse ext. | `DomainPattern` EAnnotation, null binding |
| Default values | §7.11.3 | `default_values { ... }` assignment |
| Opposite properties | §7.11.2 | `opposite(T::prop)` in templates |
| Binding validation | §7.5 | `QvtrBindingValidator` — post-parse static analysis; a declaration binds only with an initializer; per-direction check before an enforce run (`QvtrParserSupport.unboundInDirection`) |
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
| GAP-13 | Change propagation (§7.6) | Semantically ≡ full re-execution, which deletes what no binding requires; incremental execution is an optimisation | Phase 5 |
| GAP-6 | QVT-O `refined` — stub resolution | Parser + Provider-Brücke ✅ (Phase 4b). Runtime Stub-Auflösung (`OT.refined` → echte RT) + Trace-Semantik ausstehend | Phase 5 |

## 8. Design Decisions

| ID | Decision | Rationale |
|----|----------|-----------|
| D33 | Direct interpretation (no QVT-C) | Simpler, no intermediate representation |
| D34 | 3 EPackages (qvtbase/qvttemplate/qvtrelation) | Follows spec package structure |
| D35 | Spec-first approach | QVT v1.3 Ch. 7 as primary, Eclipse as reference |
| D36 | Evaluator composition (QvtrEvaluator → OclEngine) | Reuse OCL engine, no duplication |
| D37 | UnitResolver for imports | Import resolution kept out of the engine, one contract for store and OSGi |
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
| `QvtdConcreteSyntaxTest` | §7.13.2/§7.13.4: one accepted and one rejected form per production | 41 |
| `QvtdAbstractSyntaxMappingTest` | §7.13.7: the AST each construct maps to | 11 |
| `QvtdCommentTest` | §7.13.3: comments, nested paragraph comments | 4 |

**Total (2026-10-31): 320 tests, 0 failures, 9 @Disabled** — each disabled test names the issue
that will enable it (#360, #361).
