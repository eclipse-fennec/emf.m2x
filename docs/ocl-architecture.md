# OCL Implementation Architecture — Fennec M2M (Phase 1)

> Consolidated reference for the OCL implementation. Cross-cutting conventions remain in:
> [Development Guideline](development-guideline.md), [Design Decisions](design-decisions.md)

---

## 1. Overview & Status

The OCL engine implements **OCL v2.5** (backward compatible with v2.4) as a pure Java library,
fully decoupled from the Eclipse platform. It serves as the expression language foundation
for all higher-level engines (QVT-O, QVT-D, MOFM2T).

**Status:** Phase 1 complete. 4108 OCL tests, 0 failures. Spec-conformance verified against OCL v2.4/v2.5, see [archive](archive/ocl-spec-conformance-gaps.md).

**Key properties:**
- No Pivot layer — direct Ecore access (D16)
- OSGi-optional — works as plain Java library
- Configurable evaluation — strict (spec-compliant) or lenient (pragmatic) (D14)
- EMF delegate integration — validation, setting, invocation delegates
- ANTLR4-based parser with grammar composition support

---

## 2. Module Structure

```
workspace/
├── org.eclipse.fennec.m2x.ocl.model/    # OCL EMF metamodel (Ecore-based AST)
├── org.eclipse.fennec.m2x.ocl.api/     # Public API interfaces
├── org.eclipse.fennec.m2x.ocl.parser/  # ANTLR4 parser
├── org.eclipse.fennec.m2x.ocl.engine/  # Evaluator / interpreter
└── org.eclipse.fennec.m2x.ocl.tests/   # Tests (plain JUnit 5)
```

### 2.1 Dependency Graph

```
            ┌──────────┐
            │ ocl.model │  ← EMF metamodel, generated into src/
            └────┬─────┘
                 │
            ┌────┴────┐
            │ ocl.api  │  ← Public API interfaces
            └────┬────┘
                 │
       ┌─────────┼─────────┐
       ▼                   ▼
┌──────────┐        ┌──────────┐
│ocl.parser│        │ocl.engine│  ← engine depends on api+model, NOT parser
└──────────┘        └──────────┘
       │                   │
       └─────────┬─────────┘
                 ▼
          ┌──────────┐
          │ocl.tests │
          └──────────┘
```

**Cross-component consumers** (future):
- `qvto.model/engine` depends on `ocl.model/engine`
- `qvtd.model/engine` depends on `ocl.model/engine`
- `m2t.model/engine` depends on `ocl.model/engine`

---

## 3. OCL Metamodel (`ocl.model`)

**Bundle:** `org.eclipse.fennec.m2x.ocl.model`

Pure model project — EMF-generated code lives in `src/` (no `src-gen/`).

| Property | Value |
|----------|-------|
| nsURI | `http://www.eclipse.org/fennec/m2x/ocl/1.0` |
| basePackage | `org.eclipse.fennec.m2x.model` → generates to `org.eclipse.fennec.m2x.model.ocl` |
| Classifiers | 48 EClasses + 2 EEnums = 50 total |
| EPackage | Single `ocl` package, no subpackages |

### 3.1 Design Choices

- **Direct Ecore references:** `EStructuralFeature`, `EOperation`, `EClassifier`, `EEnumLiteral`
  as attribute types — no wrapper indirection.
- **`name: String` on `OperationCallExp`** (D19): `referredOperation: EOperation` can be null
  for stdlib operations. The `name` field stores the operation name (e.g. `size`, `+`,
  `oclIsKindOf`) for stdlib dispatch. Parser always sets it.
- **v2.5 additions:** `MapType`, `MapLiteralExp`, `MapLiteralPart`, `isSafe: Boolean` on `CallExp`.
- **Blueprint:** Based on `EssentialOCL.emof` from the OCL spec.

---

## 4. API Design (`ocl.api`)

**Bundle:** `org.eclipse.fennec.m2x.ocl.api`

### 4.1 Core Interfaces

```java
@ProviderType
public interface OclEngine {
    // Parsing
    OclExpression parse(String expression, EClassifier contextType) throws OclParseException;
    List<Constraint> parseDocument(String oclDocument) throws OclParseException;

    // Evaluation
    Object evaluate(OclExpression expression, OclContext context);
    Object evaluate(OclExpression expression, OclContext context, OclEvaluationOptions options);
    Object evaluate(String expression, OclContext context) throws OclParseException;
    OclResult evaluateWithDiagnostics(OclExpression expression, OclContext context,
            OclEvaluationOptions options);

    // Validation
    List<Diagnostic> validate(OclExpression expression, EClassifier contextType);

    // Extension management
    void registerOperations(OclOperationProvider provider);
    void unregisterOperations(OclOperationProvider provider);
    void registerCompleteOclDocument(CompleteOclContribution contribution);
    void unregisterCompleteOclDocument(CompleteOclContribution contribution);
}
```

### 4.2 OclConfiguration (Builder Pattern)

Bundles parser + expression cache + operation providers. Primary constructor for `OclEngineImpl`.

```java
OclConfiguration config = OclConfiguration.builder(new OclParserSupport())
    .expressionCache(OclLruExpressionCache.ofSize(1024))
    .operationProvider(myProvider)
    .build();
OclEngine engine = new OclEngineImpl(config);
```

Multiple engines sharing the same configuration share the expression cache but have
independent `PropertyAccessorCache` instances.

### 4.3 OclContext

Bundles evaluation context (replaces bare `EObject` parameter):

```java
public record OclContext(
    EObject self,                      // The context object ('self')
    OclModelExtent extent,             // For allInstances() — nullable
    Map<String, Object> variables      // Global variables
) {
    public static OclContext of(EObject self) { ... }
    public static OclContext of(EObject self, OclModelExtent extent) { ... }
    public static OclContext of(EObject self, Map<String, Object> variables) { ... }
}
```

### 4.4 OclResult (D20)

Three-state result: valid value, null (OclVoid), or OclInvalid. No `Optional` — OCL's
three-value semantics don't map to Java's two-state Optional.

```java
public record OclResult(Object value, List<Diagnostic> diagnostics) { ... }
```

### 4.5 OclEvaluationOptions

Immutable record with `with*()` copy methods. Configurable per evaluation.

```java
public record OclEvaluationOptions(
    NullHandling nullHandling,          // STRICT or LENIENT
    ErrorRecovery errorRecovery,        // FAIL_FAST or COLLECT_ERRORS
    int maxDepth,                       // Default 1,000
    int maxCollectionSize,              // Default 1,000,000
    int maxClosureIterations,           // Default 100,000
    int maxRegexLength,                 // Default 1,000
    Duration timeout                    // Default: none
) {
    public static OclEvaluationOptions strict() { ... }
    public static OclEvaluationOptions lenient() { ... }
}
```

| Mode | Behavior | Use case |
|------|----------|----------|
| `STRICT` + `FAIL_FAST` | Null → OclInvalid, first error stops | Validation, constraints |
| `LENIENT` + `COLLECT_ERRORS` | Null → null/empty, errors collected | M2T, interactive console |

### 4.6 Extension Interfaces

```java
@ConsumerType
public interface OclOperationProvider {
    List<OclOperation> getOperations();
}

@ConsumerType
public interface CompleteOclContribution {
    URI getDocumentUri();
    String getDocumentText();
}

@ConsumerType
public interface OclStandardLibraryContribution {
    List<OclOperation> getOperations();
    List<OclType> getTypes();
}
```

### 4.7 OclExpressionCache

```java
@ProviderType
public interface OclExpressionCache {
    OclExpression get(String expression, EClassifier contextType);
    void put(String expression, EClassifier contextType, OclExpression parsed);
    void invalidate(String expression, EClassifier contextType);
    void invalidateAll();
    long size();
    long hitCount();
    long missCount();
}
```

### 4.8 OclExpressionParser (D21)

Decouples engine from parser. Named to avoid collision with ANTLR-generated `OclParser`.
Engine depends only on `ocl.api`, not `ocl.parser`. OSGi `@Capability`/`@Requirement`
annotations for service filtering.

---

## 5. Parser Architecture (`ocl.parser`)

**Bundle:** `org.eclipse.fennec.m2x.ocl.parser`

### 5.1 Structure

```
org.eclipse.fennec.m2x.ocl.parser/
├── src/
│   └── org/eclipse/fennec/m2x/ocl/parser/
│       ├── OclParserSupport.java       # Entry point (implements OclExpressionParser)
│       ├── OclAstBuilder.java          # Parse tree → EMF AST (visitor)
│       ├── OclDocumentBuilder.java     # Complete OCL document → Constraints
│       ├── OclEnvironment.java         # Name resolution with nested scopes
│       ├── OclErrorListener.java       # Error collection as Resource.Diagnostic
│       └── OclParseDiagnostic.java     # Resource.Diagnostic implementation
├── src-gen/                            # ANTLR4-generated parser code
│   └── org/eclipse/fennec/m2x/ocl/parser/
│       ├── OclParser.java
│       ├── OclLexer.java
│       ├── OclVisitor.java
│       └── OclBaseVisitor.java
└── grammar/
    └── Ocl.g4                          # Combined lexer/parser grammar
```

### 5.2 ANTLR4 Build Integration

Parser projects use bnd's `-generate:` instruction with `system=` to invoke the ANTLR4 tool:

```bnd
antlr4-version = 4.13.2

antlr4.tool.cp = \
	${repo;org.antlr:antlr4;${antlr4-version}}:\
	${repo;org.antlr:antlr4-runtime;${antlr4-version}}:\
	${repo;org.antlr:antlr-runtime;3.5.3}:\
	${repo;org.antlr:ST4;4.3.4}:\
	${repo;org.abego.treelayout:org.abego.treelayout.core;1.0.3}:\
	${repo;com.ibm.icu:icu4j;72.1}

-generate: \
	grammar/Ocl.g4; \
		output=src-gen; \
		system='java -cp ${antlr4.tool.cp} org.antlr.v4.Tool \
			-visitor -no-listener \
			-package org.eclipse.fennec.m2x.ocl.parser \
			-o src-gen/org/eclipse/fennec/m2x/ocl/parser \
			${project}/grammar/Ocl.g4'
```

Key points:
- `${project}/grammar/Ocl.g4` (absolute path) prevents ANTLR4 from mirroring subdirectories
- Package set via ANTLR4 `-package` flag, **not** via `@header` block
- `-visitor -no-listener` — AST builder uses visitor pattern
- Grammar bundled in JAR via `-includeresource.grammar` for downstream `import`

### 5.3 Two-Phase Parsing (D8)

1. **Parse:** ANTLR4 grammar → parse tree (`ParserRuleContext` nodes)
2. **Build:** `OclAstBuilder` (extends `OclBaseVisitor`) → EMF model instances

Parser resolves types during AST construction for hot-path performance.

### 5.4 Grammar Design

- Follows OCL v2.4 concrete syntax (spec §9.3)
- ANTLR4 labeled alternatives for clean visitor dispatch
- Operator precedence encoded via rule ordering (ANTLR4 handles left-recursion)
- Reference: Acceleo `Query.g4` for expression parsing patterns

**Operator Precedence** (spec §7.4.8):

1. `@pre` → 2. `.`, `->`, `?.`, `?->` → 3. `not`, `-` (unary) → 4. `*`, `/` →
5. `+`, `-` → 6. `if-then-else-endif` → 7. `<`, `>`, `<=`, `>=` → 8. `=`, `<>` →
9. `and` → 10. `or` → 11. `xor` → 12. `implies`

### 5.5 Grammar Composition

| Language | Grammar File | Extends |
|----------|-------------|---------|
| OCL | `Ocl.g4` | — (base grammar) |
| QVT-O | `QvtOperational.g4` | `import Ocl;` |
| QVT-R | `QvtRelations.g4` | `import Ocl;` |
| MOFM2T | `Mofm2t.g4` | `import Ocl;` |

### 5.6 Error Handling

- Custom `ANTLRErrorListener` collects parse errors with source positions
- All errors collected in a single pass (ANTLR4 auto-recovery)
- ANTLR4 `RecognitionException` subtypes mapped to `OclParseException`

---

## 6. Engine Architecture (`ocl.engine`)

**Bundle:** `org.eclipse.fennec.m2x.ocl.engine`

### 6.1 OclEngineImpl — Facade

- Primary constructor: `OclEngineImpl(OclConfiguration)`
- Backward-compatible constructors delegate to it
- 3-level operation dispatch: Ecore `eInvoke` → stdlib → custom providers
- `installDelegates()` / `uninstallDelegates()` for standalone EMF delegate registration
- `warmUp(EPackage)` pre-populates all cache layers
- Thread-safe: stateless evaluation, each `evaluate()` creates fresh `OclEvaluator`

### 6.2 OclEvaluator — Recursive Interpreter

Recursive `OclSwitch<Object>` visitor over the EMF AST.

**Supported expression types:**

| Category | Expressions |
|----------|-------------|
| Literals | Integer, Real, String, Boolean, Null, Invalid, UnlimitedNatural, Enum, Type |
| Structural | VariableExp, IfExp, LetExp |
| Calls | PropertyCallExp (eGet), OperationCallExp (3-level dispatch) |
| Collections | CollectionLiteralExp, TupleLiteralExp, MapLiteralExp |
| Iterators | select, reject, collect, collectNested, forAll, exists, any, one, isUnique, sortedBy, closure |
| Iterate | Accumulator pattern |

Non-static, receives `PropertyAccessorCache` + `List<OclOperationProvider>` in constructor.

### 6.3 OclStdlib — Standard Library

Switch-based dispatch for all OCL standard library operations. No reflection, no HashMap
lookup overhead.

| Type | Key Operations |
|------|---------------|
| OclAny | `=`, `<>`, `oclIsTypeOf()`, `oclIsKindOf()`, `oclAsType()`, `oclIsUndefined()`, `oclIsInvalid()`, `oclContainer()`, `oclContents()` |
| Boolean | `and`, `or`, `xor`, `not`, `implies` |
| Integer | `+`, `-`, `*`, `/`, `mod`, `div`, `abs`, `max`, `min`, `floor`, `ceiling`, `round` |
| Real | `+`, `-`, `*`, `/`, `abs`, `floor`, `round`, `max`, `min` |
| String | `size()`, `concat()`, `substring()`, `toInteger()`, `toReal()`, `toUpperCase()`, `toLowerCase()`, `matches()`, `replaceAll()`, `replaceFirst()`, `startsWith()`, `endsWith()`, `equalsIgnoreCase()` |
| Collection | `size()`, `includes()`, `excludes()`, `count()`, `isEmpty()`, `notEmpty()`, `sum()`, `flatten()` |
| Set | `union()`, `intersection()`, `-`, `including()`, `excluding()`, `symmetricDifference()` |
| OrderedSet | `append()`, `prepend()`, `insertAt()`, `at()`, `indexOf()`, `first()`, `last()`, `reverse()` |
| Bag | `union()`, `intersection()`, `including()`, `excluding()` |
| Sequence | `union()`, `append()`, `prepend()`, `insertAt()`, `at()`, `indexOf()`, `first()`, `last()`, `reverse()` |
| Map (v2.5) | `at()`, `put()`, `size()`, `includes()`, `excludes()`, `keys()`, `values()` |

**Safe navigation (v2.5):** `?.` and `?->` return null/empty collection instead of OclInvalid
when source is null.

**Spec-compliance fixes (2026-02-20):**
- `excluding(x)` removes **all** occurrences (§11.7.2), not just the first — uses `removeAll(Collections.singleton(x))` for List/Bag
- `substring(lower, upper)` requires `1 <= lower AND lower <= upper` (§11.5.1) — `substring(2,1)` returns OclInvalid, not empty string
- `null.oclType()` returns `OclVoid` type, `invalid.oclType()` returns `OclInvalid` type (§11.2.1) — not OclInvalid sentinel

**Non-standard extensions (intentionally supported):**
- `toUpperCase()`/`toLowerCase()` — aliases for OCL standard `toUpper()`/`toLower()`
- `+` operator for String concatenation — alias for `concat()`
- `trim()` on String — not in OCL standard

### 6.4 OclEvalEnvironment — Runtime Bindings

Chain-of-scopes variable environment. Per-evaluation instance, not shared.

### 6.5 Collection Type Mapping

| OCL Type | Java Implementation |
|----------|-------------------|
| Set | `LinkedHashSet` |
| Sequence | `ArrayList` |
| Bag | `ArrayList` (via `OclBag` marker) |
| OrderedSet | `LinkedHashSet` (via `OclOrderedSet` marker) |
| Tuple | `Map<String, Object>` |
| Map (v2.5) | `LinkedHashMap` |

**Indexing:** OCL uses 1-based indexing for `String.substring/at/indexOf` and
`Collection.at/indexOf/subSequence`.

### 6.6 Configurable Evaluation (D14)

| Context | Null handling | Error recovery | Rationale |
|---------|-------------|---------------|-----------|
| Validation (`inv:`) | STRICT | FAIL_FAST | Spec compliance, clear pass/fail |
| Derived features (`derive:`) | STRICT | FAIL_FAST | Must produce correct value or error |
| QVT-O mappings | STRICT | FAIL_FAST | Transformation must be correct |
| M2T templates | LENIENT | COLLECT_ERRORS | Best-effort text, report issues separately |
| Interactive console | LENIENT | COLLECT_ERRORS | Exploration, partial results useful |
| QVT-R `checkonly` | STRICT | COLLECT_ERRORS | Need all violations, not just first |

### 6.7 No Pivot Layer (D16)

Eclipse OCL introduced the "Pivot" layer (~262 EClassifiers) to abstract over Ecore vs UML.
We don't need it:

| Eclipse problem | Our situation |
|-----------------|---------------|
| Must support Ecore AND UML bindings | Ecore only. No dual binding. |
| 262-class Pivot metamodel = slow startup | Lean EMF model + lazy stdlib = fast startup |
| Ecore→AS conversion on every parse | No conversion — direct Ecore access |

UML models loaded in EMF are already `EObject` instances and work with our engine.
UML-specific OCL operations (`oclIsInState`, stereotypes) can be added via an optional
`ocl.uml` extension bundle using `OclOperationProvider`.

---

## 7. Caching Architecture

The OCL engine uses a layered caching strategy. Each layer targets a different bottleneck
in the parse → resolve → evaluate pipeline.

```
┌──────────────────────────────────────────────────────────────────┐
│  User Code                                                       │
│  engine.evaluate("self.name", OclContext.of(person))             │
└────────────────────────┬─────────────────────────────────────────┘
                         │
         ┌───────────────▼────────────────┐
         │  Layer 1: Expression Cache      │  OclExpressionCache (API)
         │  "self.name" + Person → AST     │  OclLruExpressionCache (Engine)
         │  ─────────────────────────────  │
         │  Scope: shared across engines   │
         │  Key: nsURI#typeName#exprText   │
         │  Hit: ~190 ns  Miss: ~23 µs    │
         │  Speedup: 100–190x             │
         └───────────────┬────────────────┘
                         │ parsed AST
         ┌───────────────▼────────────────┐
         │  Layer 2: Feature Resolution    │  1-entry cache in OclEvaluator
         │  (EClass, sf) → resolved sf     │
         │  ─────────────────────────────  │
         │  Scope: per evaluation          │
         │  Hit: ~1 ns (two == checks)     │
         │  Benefit: iterator hot paths    │
         │  (same feature on N objects)    │
         └───────────────┬────────────────┘
                         │ resolved feature
         ┌───────────────▼────────────────┐
         │  Layer 3: Property Accessor     │  PropertyAccessorCache (per engine)
         │  (Class, sf) → LambdaAccessor   │
         │  ─────────────────────────────  │
         │  Scope: per engine instance     │
         │  Generated models: ~5 ns/call   │
         │  Dynamic models: skipped (0 ns) │
         │  Speedup: 19–22% (generated)    │
         └───────────────┬────────────────┘
                         │ property value
         ┌───────────────▼────────────────┐
         │  EMF eGet() fallback            │  Only for dynamic models
         │  DynamicEObjectImpl.eSettings[] │  ~40 ns/call
         └────────────────────────────────┘
```

### 7.1 Layer 1: Expression Parse Cache

**Problem:** Parsing costs ~23 µs. Delegates parse the same expression on every access.

**Implementation:** `OclLruExpressionCache`
- `LinkedHashMap(16, 0.75f, true)` with `removeEldestEntry()` for LRU eviction
- All map operations `synchronized` — parsing happens outside the lock
- Hit/miss counters via `AtomicLong`
- Cache key: `nsURI + "#" + typeName + "#" + expression`
- Factory: `OclLruExpressionCache.ofSize(1024)`

**Why not `ConcurrentHashMap`?** LRU eviction requires access-order tracking, which
`ConcurrentHashMap` does not support.

Cache is nullable in `OclConfiguration` — engine works without caching.

**Delegate-local caches:** The delegate factories maintain their own volatile/synchronized
lazy parse caches per delegate instance, orthogonal to the shared expression cache.

### 7.2 Layer 2: Feature Resolution Cache (1-Entry)

**Problem:** `resolveFeature()` on every property access costs ~5–10 ns via `getFeatureID()`.

**Optimization:** 1-entry "last resolved" cache in `OclEvaluator` (per-evaluation):

```java
private EClass lastResolvedClass;
private EStructuralFeature lastResolvedInput;
private EStructuralFeature lastResolvedOutput;
```

**Why 1-entry?** In iterator bodies, the same feature is accessed on many objects of the
same EClass. Only the first iteration is a miss.

**Impact:** P7 (closure) **−52%**, P4 (iterators) **−7%**, single access ≈0%.

### 7.3 Layer 3: Property Accessor Cache

**Problem:** EMF `eGet()` goes through 3-level dispatch (~40 ns). Generated models have
typed getters (~5 ns) but `eGet()` never uses them.

**Optimization:** `PropertyAccessorCache` uses `LambdaMetafactory` to generate accessors
wrapping typed getters. JIT can inline to ~5 ns.

- Instance-based: each `OclEngineImpl` has its own cache
- `ConcurrentHashMap` for lock-free reads
- Dynamic models: `sf.getContainerClass() == null` → returns null (zero overhead)
- Autoboxing handled via `autobox()` mapping
- Getter name: EMF convention — `isXxx()` for `EBoolean`, `getXxx()` otherwise

### 7.4 Warm-Up

`OclEngineImpl.warmUp(EPackage)` pre-populates all cache layers:

1. **Property Accessor Cache:** Creates temp EObject per non-abstract EClass, pre-populates
   accessor entries. Cost: ~4 µs per package.
2. **Expression Parse Cache:** Scans OCL `EAnnotation`s (derivation, initial, body, pre, post,
   constraints) and pre-parses. Each annotated expression adds ~23 µs.
3. Parse errors during warm-up are silently ignored — reported at evaluation time.

### 7.5 Future: OclAspectProvider

Designed for integration with fennec's `MetadataService` / `AspectProvider` pattern
(planned for `emf.osgi`):

1. `OclAspectProvider` registers as `AspectProvider` in `MetadataService`
2. When an EPackage is registered, the provider receives it
3. Builds `OclConfiguration` with pre-populated caches, calls `warmUp(EPackage)`
4. New `OclEngineImpl` instances created from pre-warmed configuration

The `OclConfiguration` builder pattern and instance-based `PropertyAccessorCache` are
already prepared for this integration.

---

## 8. EMF Delegate Integration

### 8.1 Overview

EMF delegate registries allow OCL expressions in Ecore models via `EAnnotation`:

| Delegate | Purpose | Annotation Key |
|----------|---------|---------------|
| **ValidationDelegate** | OCL invariants as validation rules | constraint name |
| **SettingDelegate** | Derived features (`derive:`) and defaults (`initial:`) | `derivation`, `initial` |
| **InvocationDelegate** | Operation bodies (`body:`) with pre/post | `body`, `pre`, `post` |

### 8.2 Annotation URI

All annotations use the Fennec OCL delegate URI:
```
http://www.eclipse.org/fennec/m2x/ocl/1.0
```

Example in Ecore:
```xml
<eStructuralFeatures xsi:type="ecore:EAttribute" name="fullName" eType="ecore:EDataType ...#//EString"
    changeable="false" volatile="true" transient="true" derived="true">
  <eAnnotations source="http://www.eclipse.org/fennec/m2x/ocl/1.0">
    <details key="derive" value="self.firstName.concat(' ').concat(self.lastName)"/>
  </eAnnotations>
</eStructuralFeatures>
```

### 8.3 Implementation Classes

| Class | Extends/Implements | Package |
|-------|-------------------|---------|
| `OclInvocationDelegateFactory` | `EOperation.Internal.InvocationDelegate.Factory` | `ocl.engine.internal` |
| `OclSettingDelegateFactory` | `BasicSettingDelegate.Stateless` | `ocl.engine.internal` |
| `OclValidationDelegateFactory` | `EValidator.ValidationDelegate` | `ocl.engine.internal` |
| `OclDelegateUtil` | — (annotation reader + URI constant) | `ocl.engine.internal` |

### 8.4 Standalone Registration

```java
OclConfiguration config = OclConfiguration.builder(new OclParserSupport()).build();
OclEngineImpl engine = new OclEngineImpl(config);
engine.installDelegates();  // registers all three delegates

// Equivalent manual registration:
String uri = "http://www.eclipse.org/fennec/m2x/ocl/1.0";
EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE
    .put(uri, new OclInvocationDelegateFactory(engine));
EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE
    .put(uri, new OclSettingDelegateFactory(engine));
EValidator.ValidationDelegate.Registry.INSTANCE
    .put(uri, new OclValidationDelegateFactory(engine));
```

### 8.5 Delegate Options

EMF delegates use `OclEngineImpl.delegateOptions` (configurable via `setDelegateOptions()`).
Configure before `installDelegates()` for untrusted models:

```java
engine.setDelegateOptions(OclEvaluationOptions.strict()
    .withMaxDepth(100)
    .withMaxCollectionSize(10_000));
engine.installDelegates();
```

### 8.6 Validation Caching

`OclValidationDelegateFactory` caches parsed expressions in a `ConcurrentHashMap`
keyed by `contextType#expression` to prevent parse amplification (S-7).

### 8.7 OSGi Registration (emf.osgi Whiteboard)

The delegate factories are annotated with `@Component` and service properties matching the
emf.osgi whiteboard pattern:

| Property | Value |
|----------|-------|
| `emf.configuratorName` | `http://www.eclipse.org/fennec/m2x/ocl/1.0` |
| `emf.name` | `fennec-ocl` |
| `configuratorType` | `OPERATION_INVOCATION_FACTORY` / `SETTING_DELEGATE_FACTORY` / `VALIDATION_DELEGATE` |

The emf.osgi delegate registry whiteboard components pick up these services automatically —
no manual `installDelegates()` call needed.

### 8.8 emf.osgi Delegate Registry Analysis

For the full emf.osgi delegate registry architecture (whiteboard populator components,
missing registries for SettingDelegate/ValidationDelegate), see
[emf-delegate-registries.md (archived)](../../docs/archive/emf-delegate-registries.md).

---

## 9. OSGi Integration

### 9.1 Core Principle

All engines work as **plain Java libraries** without OSGi. OSGi is an optional enhancement
providing service discovery, whiteboard patterns, and lifecycle management.

**Pattern:** Pure Java core + OSGi adapter layer (same bundle).

### 9.2 Standalone Usage

```java
OclConfiguration config = OclConfiguration.builder(new OclParserSupport())
    .expressionCache(OclLruExpressionCache.ofSize(1024))
    .build();
OclEngine engine = new OclEngineImpl(config);

OclContext context = OclContext.of(myEObject);
Object result = engine.evaluate("self.name", context);

// With global variables
OclContext ctxWithVars = OclContext.of(myEObject, Map.of("threshold", 50000));
Object result2 = engine.evaluate("self.salary > threshold", ctxWithVars);

// Install EMF delegates
engine.installDelegates();
```

### 9.3 OSGi Component Architecture

```
┌─────────────────────────────────────────────────────────────┐
│  DefaultOclExpressionCacheComponent (SINGLETON)             │
│  → OclExpressionCache service (LRU, 1024 entries)          │
└──────────────┬──────────────────────────────────────────────┘
               │ @Reference(name="expressionCache")
┌──────────────▼──────────────────────────────────────────────┐
│  OclEngineComponent (PROTOTYPE)                             │
│  → OclEngine service                                        │
│  Each instance has its own PropertyAccessorCache             │
│  All instances share the injected OclExpressionCache         │
└──────────────┬──────────────────────────────────────────────┘
               │
┌──────────────▼──────────────────────────────────────────────┐
│  EMF Delegates (emf.osgi whiteboard, see §8.7)              │
└─────────────────────────────────────────────────────────────┘
```

**Key components:**
- `OclEngineComponent` — PROTOTYPE scope, extends `OclEngineImpl`, no `installDelegates`
- `DefaultOclExpressionCacheComponent` — SINGLETON, provides default LRU cache (1024 entries)
- Delegate factories — separate `@Component` services with emf.osgi properties

### 9.4 Customizing the Expression Cache (OSGi Configurator)

```json
{
    ":configurator:resource-version": 1,
    "org.eclipse.fennec.m2x.ocl.engine.OclEngineComponent": {
        "expressionCache.target": "(cache.name=large-cache)"
    }
}
```

### 9.5 Consumer Example

```java
@Component
public class MyService {
    @Reference
    OclEngine oclEngine;  // injected by DS (prototype: own instance)

    void doSomething(EObject obj) {
        Object result = oclEngine.evaluate("self.name", OclContext.of(obj));
    }
}
```

---

## 10. Security Hardening

### 10.1 Threat Model

The OCL engine evaluates expressions that may come from untrusted sources (model annotations,
external OCL documents, user input). **No remote code execution vectors exist** — OCL is a
pure query language with no I/O. **Denial-of-service via resource exhaustion** is the primary
concern.

### 10.2 Configurable Limits

All limits are configured via `OclEvaluationOptions`:

| Field | Type | Default | Protects against |
|-------|------|---------|-----------------|
| `maxDepth` | int | 1,000 | S-9: Stack overflow via deep recursion |
| `maxCollectionSize` | int | 1,000,000 | S-2: Range explosion, S-3: Product explosion, S-12: allInstances |
| `maxClosureIterations` | int | 100,000 | S-4: Unbounded closure traversal |
| `maxRegexLength` | int | 1,000 | S-1: ReDoS via crafted regex patterns |

Limits are per-evaluation (not global). Violations produce `OclInvalid` with diagnostic error.

### 10.3 Attack Vectors

| # | Vector | Severity | Status |
|---|--------|----------|--------|
| S-1 | ReDoS — `'input'.matches('(a+)+$')` | High | Mitigated: regex length check |
| S-2 | Range explosion — `Sequence{1..2147483647}` | High | Mitigated: size check |
| S-3 | Cartesian product — `Seq{1..1000}->product(Seq{1..1000})` | High | Mitigated: product size check |
| S-4 | Unbounded closure — `Seq{1}->closure(i \| i+1)` | High | Mitigated: iteration counter |
| S-5 | Unsafe type casts in stdlib | Medium | Mitigated: safe pattern matching |
| S-6 | Delegate injection (trust boundary) | Low | Documented: explicit opt-in |
| S-7 | Parse amplification (repeated validation) | Medium | Mitigated: expression cache |
| S-8 | allInstances on large extent | Medium | Mitigated: size check (see S-12) |
| S-9 | Stack overflow via deep recursion | High | Mitigated: depth counter |
| S-10 | Integer overflow | Low | Documented: accepted risk |
| S-11 | String concatenation amplification | Low | Partially mitigated: bounded by maxDepth |
| S-12 | allInstances result size | Medium | Mitigated: maxCollectionSize |
| S-13 | Custom operation provider abuse | Low | Documented: trust boundary |
| S-14 | EMF delegate URI spoofing | Low | Documented: by design |

### 10.4 Trust Boundaries

| Boundary | Trust Level | Recommendation |
|----------|------------|----------------|
| Own model annotations | Trusted | Default `strict()` options |
| External Complete OCL documents | Semi-trusted | `strict()` with tightened limits |
| User input (console, LSP) | Untrusted | Tightened limits, consider timeout |
| Custom operation providers | Trusted (code) | Only register known implementations |
| Model extent for allInstances | Varies | Bounded extent, set `maxCollectionSize` |

### 10.5 Embedder Guidance

When evaluating OCL expressions from untrusted sources:

1. **Set conservative limits:**
   ```java
   OclEvaluationOptions sandboxed = OclEvaluationOptions.strict()
       .withMaxDepth(100)
       .withMaxCollectionSize(10_000)
       .withMaxClosureIterations(1_000)
       .withMaxRegexLength(200);
   ```

2. **Use bounded model extents** for `allInstances()`.

3. **Configure delegate options** before `installDelegates()`:
   ```java
   engine.setDelegateOptions(sandboxed);
   engine.installDelegates();
   ```

4. **Check `OclResult.diagnostics()`** — limit violations are reported as error diagnostics.

5. **Future:** `timeout` field exists in `OclEvaluationOptions` but is not yet enforced.

---

## 11. Performance

### 11.1 Thread-Safety Guarantees

| Component | Strategy | Status |
|-----------|----------|--------|
| `OclEngineImpl` | Stateless evaluation — each `evaluate()` creates fresh `OclEvaluator` | Thread-safe by design |
| `OclEvaluator` | Per-evaluation instance, no shared mutable state | Thread-safe by design |
| `OclStdlib` | Static methods, no state | Thread-safe by design |
| `OclEvalEnvironment` | Per-evaluation chain-of-scopes, not shared | Thread-safe by design |
| `operationProviders` | `CopyOnWriteArrayList` — safe concurrent read, rare write | Thread-safe |
| `expressionCache` | `ConcurrentHashMap` in delegates | Thread-safe |
| `delegateOptions` | `volatile` field on `OclEngineImpl` | Visibility guaranteed |
| Parser (ANTLR4) | New `OclLexer`/`OclParser` per parse call | Thread-safe |

### 11.2 Performance Design Principles

- **No Pivot layer** (D16): direct Ecore access, no Ecore→AS conversion
- **Lazy parsing in delegates**: expressions parsed on first access, cached
- **Switch-based dispatch** in OclStdlib: no reflection, no HashMap lookups
- **Immutable AST**: parsed `OclExpression` shareable across threads
- **Per-evaluation environment**: no lock contention

### 11.3 Benchmark Results

Measured on: Linux 6.17.0-1011-oem, Java 21. Dynamic EMF models (PropertyAccessorCache
not active — only benefits generated models).

#### µs/op (Median, 3 JVM runs)

| # | Benchmark | Baseline | + LRU Cache | + Accessor | + Resolve Cache | Δ gesamt |
|---|-----------|--------:|----------:|----------:|---------------:|--------:|
| P1 | Parse (complex) | 28.57 | 28.57 | 28.57 | 20.32 | **−29%** |
| P2 | Property (`self.name`) | 0.078 | 0.078 | 0.074 | 0.086 | ≈0% |
| P3 | Collection (100 emps) | — | — | 22.18 | — | — |
| P4 | Iterators (forAll, 50) | 31.96 | 31.96 | 35.91 | 33.43 | **−7%** |
| P5 | allInstances (50K) | — | — | 11,780 | — | — |
| P6 | Navigation (5-step) | 2.13 | 2.13 | 1.74 | 2.22 | ≈0% |
| P7 | Closure (100 emps) | — | — | 5,149 | 2,460 | **−52%** |
| P17 | Engine startup | — | — | 0.34 | — | — |
| P18 | Cache hit | — | 0.141 | 0.190 | — | — |
| P18 | **Speedup** | — | **192x** | **147x** | — | — |
| P19 | Accessor (`self.name`) | — | — | 0.076 | 0.081 | — |
| P20 | WarmUp (2 classes) | — | — | 3.86 | — | — |

#### Key Metrics

| Metric | Value |
|--------|-------|
| Engine startup | 341 ns (0.3 µs, ~2.9M ops/sec) |
| Parse cache hit speedup | 147–192x |
| Memory (1000 ASTs) | ~0 KB (very compact) |
| WarmUp (2 classes, no annotations) | 3.9 µs |

### 11.4 Optimization Stages

| Stage | Optimization | Primary Effect |
|-------|-------------|----------------|
| 0 | Baseline | No caching, O(n) feature scan |
| 1 | LRU Expression Cache | Redundant parsing eliminated (147–192x) |
| 2 | resolveFeature O(1) + PropertyAccessorCache | EMF eGet() dispatch bypassed (generated models) |
| 3 | warmUp(EPackage) | First-access latency eliminated (~4 µs/pkg) |
| 4 | Feature Resolution Cache (1-entry) | Iterator hotpath: P7 −52%, P4 −7% |

### 11.5 Benchmark Test Plan (P1–P17)

| # | Test | What it measures |
|---|------|-----------------|
| P1 | Parse throughput | 10K parses of same expression |
| P2 | Simple property eval | 100K evals on same parsed AST |
| P3 | Collection pipeline | select→collect→size, 10K× |
| P4 | Nested iterators | forAll with closure, 1K× |
| P5 | Large model allInstances | 50K EObjects, allInstances→select |
| P6 | Deep navigation | 10-level containment tree |
| P7 | Closure on graph | 10K nodes, closure traversal |
| P8–P12 | Concurrent evaluations | 16 threads, various patterns |
| P13–P15 | Security limit stress | Rapid-fire range/closure/regex limits |
| P16 | Memory footprint | Parse 1000 expressions, measure heap |
| P17 | Engine startup time | Cold-start: new OclEngineImpl() |

---

## 12. Testing

### 12.1 Coverage Targets

| Component | Line Target | Branch Target | Measured |
|-----------|------------|---------------|----------|
| OclEvaluator | > 90% | > 85% | 91% / 83% |
| OclStdlib | > 85% | > 80% | 87% / 77% |
| OclAstBuilder | > 85% | > 70% | 88% / 68% |
| Delegate factories | > 90% | > 85% | 95% / 90% |
| API classes | > 80% | > 70% | Varies |

### 12.2 Test Model

```
Package: company (nsURI: http://company)
  Class: Company
    - name: EString
    - employees: Person [*]
  Class: Person
    - name: EString
    - age: EInt
    - salary: EDouble
    - isMarried: EBoolean
    - employer: Company [0..1]
```

### 12.3 Test Categories

| Category | Runner | Tag |
|----------|--------|-----|
| Unit (individual classes) | JUnit 5 | — |
| Integration (parse → evaluate) | JUnit 5 | — |
| Spec compliance (spec examples) | JUnit 5 | `@Tag("spec")` |
| Edge cases | JUnit 5 | — |
| Performance benchmarks | JUnit 5 + JMH | `@Tag("perf")` |
| OSGi integration | `*.itest` bundle | — |

### 12.4 Thread-Safety Testing

Every thread-safe component must have concurrent tests:
- Parse + evaluate from multiple threads simultaneously
- Mutate extension registries during evaluation
- Invoke EMF delegates from parallel threads
- Verify no `ConcurrentModificationException`, correct results

### 12.5 Performance Regression Policy

Performance tests run against Eclipse OCL under identical conditions (both standalone, no OSGi).
Results documented but not gating — the goal is awareness, not hard thresholds.

### 12.6 Coverage Gaps (DR-D25)

**Accepted gaps (not requiring tests):**
- `OclEngineComponent` (0%) — OSGi DS adapter, requires itest bundle
- `dispatchBoolean` (0%) — dead path; all booleans route through `evaluateThreeValuedBoolean`
- `validate()` (0%) — TODO stub, no implementation yet

**Gaps to close:**
- `@pre` postcondition integration (45% → target 85%)
- LENIENT null handling mode (0% branch → target 80%)
- Complete OCL operation context (pre/post/body, 64% → target 80%)
- Thread-safety verification (0 parallel tests → 5 core scenarios)

---

## 13. Design Decisions (OCL-specific)

### 13.1 Decision Summary

| # | Decision | Status |
|---|----------|--------|
| D11 | EMF delegate integration | Implemented |
| D13 | OCL v2.5 backward-compatible with v2.4 | Implemented |
| D14 | Configurable evaluation (strict/lenient) | Implemented |
| D16 | No Pivot layer — direct Ecore access | Implemented |
| D19 | `name: String` on OperationCallExp | Implemented |
| D20 | OclResult record for diagnostics | Implemented |
| D21 | OclExpressionParser interface in ocl.api | Implemented |

### 13.2 DR-D14: Configurable Evaluation (Strict / Lenient)

Eclipse OCL's strict null handling drove Acceleo to replace OCL with AQL. We solve this at
the evaluation level without abandoning OCL.

`OclEvaluationOptions` provides:
- **STRICT** (default): Null property access → OclInvalid, propagates per spec
- **LENIENT**: Null → null/empty, collection ops on null → empty collection

Safe navigation (v2.5 `?.`, `?->`) provides **explicit** null-safety in expressions.
LENIENT mode provides **implicit** null-safety. Both coexist.

### 13.3 DR-D16: No Pivot Layer

See §6.7 above for full rationale. Key insight: UML-specific OCL operations are convenience
shortcuts for standard Ecore navigation — they validate the `OclOperationProvider` design.

### 13.4 DR-D22: MessageExp — Deferred

`MessageExp` (`^^` operator) for asynchronous operation calls in postconditions.
Neither Eclipse OCL nor QVT-O ever implemented it at runtime.

**Current:** Returns OclInvalid with diagnostic error.

**Future:** `OclMessageProvider` interface (API) + default EMF implementation (engine).

### 13.5 DR-D23: Security Hardening Limits

See §10 above. Configurable, per-evaluation resource limits in `OclEvaluationOptions`.
Defaults are generous for normal usage while preventing obvious attacks.

### 13.6 DR-D24: Performance Benchmark Design

All benchmarks run both engines under identical conditions:
- Plain Java 21, no OSGi
- 100 iterations warmup, 10,000 measurement iterations
- Shared Ecore test model, identical OCL expressions
- Metrics: ns/eval, ops/sec, MB heap

### 13.7 DR-D25: Test Coverage Gaps

See §12.6 above. Documents accepted gaps and gaps to close.

---

## 14. Future / Planned Work

### 14.1 OclAspectProvider (MetadataService Integration)

Integration with fennec's `MetadataService` / `AspectProvider` pattern (see §7.5).
Infrastructure already prepared: `OclConfiguration`, instance-based `PropertyAccessorCache`,
`warmUp(EPackage)`.

### 14.2 MessageExp Runtime Support (DR-D22)

`OclMessageProvider` interface for `^^` operator in postconditions.

### 14.3 UML Extension Bundle

Optional `org.eclipse.fennec.m2x.ocl.uml` providing UML-specific OCL operations
(`oclIsInState`, stereotype access) via `OclOperationProvider`.

### 14.4 `validate()` Implementation

Static well-formedness checking visitor over entire AST.

### 14.5 Known Implementation Gaps

| # | Gap | Severity | Status |
|---|-----|----------|--------|
| GAP-1 | Complete OCL document parsing | Critical | Done |
| GAP-2 | `@pre` parsed but never evaluated | Critical | Open |
| GAP-3 | `validate()` no-op stub | Critical | Open |
| GAP-6 | MessageExp has no evaluator case | Medium | Deferred (depends on GAP-2) |
| GAP-7 | `oclIsNew` absent | Medium | Deferred (depends on GAP-2) |

All other gaps (GAP-4, 5, 8–20) have been fixed. See [archive/ocl-engine-gaps.md](archive/ocl-engine-gaps.md) for details.
