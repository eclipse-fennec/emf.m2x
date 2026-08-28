# OCL Engine User Guide

Fennec OCL is a lightweight, spec-compliant OCL v2.5 engine (backward compatible with v2.4) that works as a standalone Java library — no Eclipse platform required.

## Table of Contents

1. [Overview](#1-overview)
2. [Quick Start](#2-quick-start)
3. [Engine Setup](#3-engine-setup)
4. [Evaluating Expressions](#4-evaluating-expressions)
5. [OclContext — Evaluation Context](#5-oclcontext--evaluation-context)
6. [Evaluation Options](#6-evaluation-options)
7. [Caching](#7-caching)
8. [Error Handling](#8-error-handling)
9. [EMF Delegate Integration](#9-emf-delegate-integration)
10. [Complete OCL Documents](#10-complete-ocl-documents)
11. [Custom Operations](#11-custom-operations)
12. [Thread Safety](#12-thread-safety)
13. [Value Type Mapping](#13-value-type-mapping)
14. [Security Hardening](#14-security-hardening)

---

## 1. Overview

The Fennec OCL Engine provides:

- **OCL v2.5** expression parsing and evaluation (backward compatible with v2.4)
- **Standalone operation** — works as a plain Java library without OSGi
- **OSGi-optional** — full Declarative Services support when running in OSGi
- **ANTLR4-based parser** — fast, reliable parsing with precise error locations
- **EMF delegate integration** — derived features, operation bodies, and validation constraints
- **LRU expression cache** — thread-safe caching with hit/miss statistics
- **Configurable evaluation** — strict/lenient null handling, timeouts, depth limits

### Performance

Fennec OCL is significantly faster than Eclipse OCL Classic:

| Operation | Speedup |
|-----------|---------|
| Parse (no cache) | 100–315x faster |
| Parse (cached) | ~100,000x faster |
| Parse + Eval | 176–454x faster |
| Pure Evaluation | 20–59% faster |

See [benchmark-results.md](benchmark-results.md) for detailed numbers.

### Maven Coordinates

```
GroupId: org.eclipse.fennec.m2x
```

| Bundle | Description |
|--------|-------------|
| `org.eclipse.fennec.m2x.ocl.api` | Public API interfaces |
| `org.eclipse.fennec.m2x.ocl.parser` | ANTLR4 parser |
| `org.eclipse.fennec.m2x.ocl.engine` | Evaluator implementation |
| `org.eclipse.fennec.m2x.ocl.model` | OCL EMF metamodel |

---

## 2. Quick Start

Minimal example — parse and evaluate an OCL expression against an EMF object:

```java
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;

// Create engine
OclEngine engine = OclEngines.create(new OclParserSupport());

// Evaluate an expression
Object result = engine.evaluate("self.name.size() > 0", OclContext.of(myEObject));
System.out.println(result); // true
```

That's it. Three lines to set up, one line to evaluate.

---

## 3. Engine Setup

### 3.1 Minimal (No Cache)

```java
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;

OclEngine engine = OclEngines.create(new OclParserSupport());
```

### 3.2 With LRU Expression Cache

```java
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;

OclEngine engine = OclEngines.create(
    new OclParserSupport(),
    OclLruExpressionCache.ofSize(2048)
);
```

### 3.3 With OclConfiguration Builder (Recommended)

```java
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;

OclConfiguration config = OclConfiguration.builder(new OclParserSupport())
    .expressionCache(OclLruExpressionCache.ofSize(2048))
    .addOperationProvider(myCustomOps)
    .build();

OclEngine engine = OclEngines.create(config);
```

### 3.4 OSGi (Declarative Services)

In OSGi, the engine and parser are registered as **PROTOTYPE-scoped** DS components. Every `@Reference` injection creates a fresh, isolated instance:

```java
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class MyComponent {

    @Reference
    private OclEngine engine;  // fresh instance, own PropertyAccessorCache

    public boolean validateName(EObject obj) {
        return (Boolean) engine.evaluate("self.name.size() > 0", OclContext.of(obj));
    }
}
```

**Component scopes:**

| Component | Scope | What's shared? |
|-----------|-------|----------------|
| `OclParserSupport` | PROTOTYPE | Nothing — each engine gets its own parser |
| `OclEngineComponent` (`DefaultOclEngine`) | PROTOTYPE | Nothing — each consumer gets its own engine with isolated `PropertyAccessorCache`. Published as `OclEngine` and, for the EMF delegates in the same bundle, as the internal `OclDelegateSupport` |
| `DefaultOclExpressionCacheComponent` | SINGLETON | **Shared by default** — all engines share the same LRU parse cache (1024 entries) |

The expression cache is shared because parsed `OclExpression` ASTs are immutable and thread-safe. This means a parse result from one engine benefits all others.

**That sharing has a limit worth knowing.** The default cache keys by nsURI, which names a model but not a *version* of it. Where two versions of one nsURI are live at once, they share an entry, and whichever was compiled first decides what the other gets. Navigation survives that by accident — feature access falls back to resolving by name on the runtime class — but an expression naming a type does not, and reports a perfectly valid object as invalid with nothing said about why.

`org.eclipse.fennec.m2x.ocl.fingerprint` keys by the model fingerprint instead, so two versions are two entries and two loads of the same content are one. Select it where two versions can coexist:

```json
{
    "DefaultOclEngine": { "expressionCache.target": "(cache.name=fingerprint)" }
}
```

Outside OSGi:

```java
OclConfiguration.builder(parser)
    .expressionCache(new FingerprintExpressionCache(OclLruExpressionCache.ofSize(1024),
            FingerprintHelper.getDefaultFingerprintService()))
    .build();
```

It is not the default because the shipped cache is right whenever one version of an nsURI is live at a time, which is the ordinary case. Note also that **changing a model after expressions have been compiled against it is out of contract** — a fingerprint is computed per package instance, and neither cache tries to notice a mutation.

To use **individual caches** (e.g. for different metamodels), create factory configurations with `expressionCache.target` — see [§3.5](#35-osgi-configuration-via-configadmin) and the [Architecture Doc §9.5](ocl-architecture.md#95-expression-cache-sharing-and-isolation).

### 3.5 OSGi Configuration via ConfigAdmin

The `OclEngineComponent` supports engine-wide defaults through OSGi ConfigurationAdmin. All properties use the `ocl.` prefix.

**OSGi Configurator JSON:**

```json
{
    ":configurator:resource-version": 1,
    "DefaultOclEngine": {
        "ocl.maxDepth": 500,
        "ocl.maxCollectionSize": 100000,
        "ocl.maxClosureIterations": 10000,
        "ocl.maxRegexLength": 200,
        "ocl.timeout": 5000,
        "ocl.nullHandling": "STRICT",
        "ocl.errorRecovery": "FAIL_FAST",
        "ocl.customOperationsEnabled": false
    }
}
```

**Available properties:**

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `ocl.maxDepth` | int | 1,000 | Maximum expression nesting depth |
| `ocl.maxCollectionSize` | int | 1,000,000 | Maximum collection elements |
| `ocl.maxClosureIterations` | int | 100,000 | Maximum closure() iterations |
| `ocl.maxRegexLength` | int | 1,000 | Maximum regex pattern length |
| `ocl.timeout` | long | 0 | Evaluation timeout in ms (0 = no timeout) |
| `ocl.nullHandling` | String | `STRICT` | `STRICT` or `LENIENT` |
| `ocl.errorRecovery` | String | `FAIL_FAST` | `FAIL_FAST` or `COLLECT_ERRORS` |
| `ocl.customOperationsEnabled` | boolean | false | Enable config-registered custom operations |

These defaults are used by `evaluate(expression, context)` (without explicit options). Calls with explicit `OclEvaluationOptions` override the engine-wide defaults.

**Multiple engine configurations:**

Use factory configurations to create multiple engines with different settings:

```json
{
    ":configurator:resource-version": 1,
    "DefaultOclEngine~strict": {
        "ocl.maxDepth": 100,
        "ocl.nullHandling": "STRICT",
        "engine.tag": "strict"
    },
    "DefaultOclEngine~lenient": {
        "ocl.maxDepth": 1000,
        "ocl.nullHandling": "LENIENT",
        "engine.tag": "lenient"
    }
}
```

Inject a specific engine via filter:

```java
@Reference(target = "(engine.tag=strict)")
private OclEngine strictEngine;

@Reference(target = "(engine.tag=lenient)")
private OclEngine lenientEngine;
```

### 3.6 Standalone Configuration with Engine-Wide Defaults

The `OclConfiguration` builder also supports engine-wide defaults for standalone (non-OSGi) use:

```java
OclConfiguration config = OclConfiguration.builder(new OclParserSupport())
    .expressionCache(OclLruExpressionCache.ofSize(2048))
    .nullHandling(NullHandling.LENIENT)
    .errorRecovery(ErrorRecovery.COLLECT_ERRORS)
    .maxDepth(500)
    .timeoutMs(5000)
    .maxCollectionSize(100_000)
    .maxClosureIterations(10_000)
    .maxRegexLength(200)
    .build();

OclEngine engine = OclEngines.create(config);

// evaluate() without options uses the engine-wide defaults
Object result = engine.evaluate("self.name", OclContext.of(obj));

// LENIENT covers navigation, not calls:
engine.evaluate("self.employer.name", OclContext.of(obj));   // employer unset → null
engine.evaluate("self.employer.name.size()", OclContext.of(obj));   // → OclInvalid

// evaluate() with explicit options overrides the defaults
Object sandboxed = engine.evaluate(expr, ctx,
    OclEvaluationOptions.strict().withMaxDepth(50));
```

### 3.7 Type Resolution — Which Packages the Parser Sees

Classifier names are resolved when an expression is parsed — `self.oclIsTypeOf(Novel)` needs to find `Novel` somewhere. The parser looks in the context type's own package first, then in its `EPackage.Registry`.

```java
// plain Java: the global registry, which is the correct answer there
OclEngine engine = OclEngines.create(new OclParserSupport());

// your own packages — hand over the ResourceSet you already have
OclEngine engine = OclEngines.create(new OclParserSupport(resourceSet));

// or the registry itself, if that is what you hold
OclEngine engine = OclEngines.create(new OclParserSupport(registry));
```

Under OSGi the resource set is what `emf.osgi` injects — a configured, isolated stack arrives as a `ResourceSetFactory`, never as a bare registry:

```java
@Reference(target = "(rsf.name=myapp)")
ResourceSetFactory factory;

OclEngine engine = OclEngines.create(new OclParserSupport(factory.createResourceSet()));
```

Only the resource set's package registry is used; nothing is loaded through it.

The registry is the parser's, not the engine's: hand it over when constructing `OclParserSupport`, and every expression and Complete OCL document parsed by that engine resolves against it. Nothing inside the engine reaches for the global registry on its own (D42), so what you pass in is what the parser sees.

Which packages a caller supplies is a model-identity decision, and identity is the model **fingerprint** rather than the nsURI — see the `emf.osgi` fingerprint guide. Supply the packages you resolved and verified yourself; the engine forms no opinion about which version an nsURI names.

A **qualified** name that resolves in neither the context package nor the registry is an error: parsing fails with `OclParseException`, and `getErrors()` carries one diagnostic per unresolved name — all of them, not just the first.

```java
parser.parse("self.oclIsTypeOf(nosuch::Type)", bookClass);
// OclParseException: Unknown type (nosuch::Type)

parser.parse("Status::UNKNOWN", personClass);
// OclParseException: Unknown enumeration literal (Status::UNKNOWN)
```

An **unqualified** name is not rejected in a *value* position. A bare name that matches no property and no classifier becomes an external variable reference, which `OclContext` can bind at evaluation time — rejecting it would break context variables.

In a **type** position it is rejected, because there is no second reading available there. The argument of `oclIsKindOf`, `oclIsTypeOf` and `oclAsType` is a type by definition (OCL v2.4 §13.2: a `TypeExp` refers to an *existing* type), as is every `: Type` annotation:

```java
parser.parse("self.oclIsKindOf(NoSuchType)", bookClass);
// OclParseException: Unknown type (NoSuchType)
```

Before this was reported at parse time, such a name became an external variable: the evaluator then said `Unresolved variable: NoSuchType` — the wrong noun, only visible to a caller who asked for diagnostics — and `evaluate` answered `false`, which for `oclIsKindOf` is an answer nobody questions.

A name that *is* bound stays accepted in a type position: whether a bound value may serve as a type is a question about the value, and belongs to evaluation.

```java
parser.parse("Sequence{1}->exists(x | self.oclIsKindOf(x))", bookClass);   // parses
```

---

## 4. Evaluating Expressions

### 4.1 Convenience: Parse + Eval in One Step

The simplest approach — pass a String expression and context:

```java
Object result = engine.evaluate("self.name", OclContext.of(myEObject));
```

This parses and evaluates in one call. If a cache is configured, the parsed expression is cached automatically.

### 4.2 Pre-Parsed: Parse Once, Evaluate Many Times

For repeated evaluation of the same expression against different objects:

```java
import org.eclipse.fennec.m2x.model.ocl.OclExpression;

// Parse once
OclExpression expr = engine.parse("self.name.size() > 3", myEClass);

// Evaluate many times
for (EObject obj : objects) {
    Object result = engine.evaluate(expr, OclContext.of(obj));
}
```

### 4.3 With Diagnostics: Fault-Tolerant Evaluation

Use `evaluateWithDiagnostics()` to collect warnings and errors without throwing:

```java
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclResult;

OclResult result = engine.evaluateWithDiagnostics(
    expr,
    OclContext.of(myEObject),
    OclEvaluationOptions.lenient()
);

if (result.isSuccess()) {
    String name = result.getValueAs(String.class);
} else {
    result.diagnostics().forEach(d -> System.err.println(d.getMessage()));
}
```

### 4.4 With Custom Evaluation Options

```java
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;

Object result = engine.evaluate(
    expr,
    OclContext.of(myEObject),
    OclEvaluationOptions.strict().withTimeout(Duration.ofSeconds(5))
);
```

---

## 5. OclContext — Evaluation Context

`OclContext` is a Java record that defines the evaluation environment.

### 5.1 Factory Methods

```java
// Just a context object (self)
OclContext ctx = OclContext.of(myEObject);

// With external variables
OclContext ctx = OclContext.of(myEObject, Map.of("threshold", 42));

// With model extent (enables allInstances())
OclContext ctx = OclContext.of(myEObject, myExtent);

// With ResourceSet (for package resolution)
OclContext ctx = OclContext.of(myEObject, myResourceSet);

// With extent and ResourceSet
OclContext ctx = OclContext.of(myEObject, myExtent, myResourceSet);

// Variables only (no self)
OclContext ctx = OclContext.of(Map.of("x", 1, "y", 2));
```

### 5.2 Full Constructor

```java
OclContext ctx = new OclContext(
    myEObject,           // self — context object (nullable)
    myExtent,            // OclModelExtent — scope for allInstances() (nullable)
    Map.of("x", 42),    // variables — external variable bindings
    myResourceSet,       // ResourceSet — for EPackage resolution (nullable)
    myInterceptor        // BiFunction<EObject, String, Object> — property interceptor (nullable)
);
```

### 5.3 OclModelExtent — allInstances() Scope

To use `allInstances()` in OCL expressions, provide an `OclModelExtent`:

```java
import org.eclipse.fennec.m2x.ocl.api.OclModelExtent;

OclModelExtent extent = eClass -> {
    // Return all instances of the given EClass in your model
    return myResource.getAllContents()
        .filter(eClass::isInstance)
        .toList();
};
```

### 5.4 Property Interceptor

Intercept property access for custom resolution (e.g., aspect-oriented properties):

```java
BiFunction<EObject, String, Object> interceptor = (obj, propertyName) -> {
    if ("customProp".equals(propertyName)) {
        return computeCustomValue(obj);
    }
    return OclContext.PROPERTY_NOT_HANDLED; // fall back to default
};

OclContext ctx = new OclContext(myEObject, null, Map.of(), null, interceptor);
```

---

## 6. Evaluation Options

`OclEvaluationOptions` controls how the engine handles nulls, errors, and resource limits.

There are three levels of configuration, from broadest to narrowest:

1. **Engine-wide defaults** — set via `OclConfiguration` builder (standalone) or ConfigAdmin (OSGi). Used by `evaluate(expression, context)`.
2. **Delegate defaults** — set via `setDelegateOptions()`. Used by EMF delegate evaluations.
3. **Per-evaluation options** — passed explicitly to `evaluate(expression, context, options)`. Override engine-wide defaults.

### 6.1 Presets

```java
// Strict: null access → OclInvalid, stop on first error
OclEvaluationOptions strict = OclEvaluationOptions.strict();

// Lenient: null access → null, collect all errors
OclEvaluationOptions lenient = OclEvaluationOptions.lenient();
```

### 6.2 Custom Options

All `with*` methods return new immutable instances:

```java
OclEvaluationOptions options = OclEvaluationOptions.strict()
    .withTimeout(Duration.ofSeconds(10))
    .withMaxDepth(500)
    .withMaxCollectionSize(100_000)
    .withMaxClosureIterations(50_000)
    .withMaxRegexLength(500)
    .withUseEMFTypes(true);
```

### 6.3 Option Reference

| Option | Default | Description |
|--------|---------|-------------|
| `NullHandling.STRICT` | yes | Null property access returns `OclInvalid.INSTANCE` |
| `NullHandling.LENIENT` | | Null property access returns `null` |
| `ErrorRecovery.FAIL_FAST` | yes | Stop on first error |
| `ErrorRecovery.COLLECT_ERRORS` | | Collect all errors, continue evaluation |
| `maxDepth` | 1,000 | Maximum expression nesting depth |
| `timeout` | none | Maximum evaluation time |
| `maxCollectionSize` | 1,000,000 | Maximum elements in a collection |
| `maxClosureIterations` | 100,000 | Maximum closure() iterations |
| `maxRegexLength` | 1,000 | Maximum regex pattern length |
| `useEMFTypes` | `false` | Wrap top-level `Collection` as `EList`, top-level `Map` as `EMap` |

### 6.4 Return Types — Java vs. EMF

By default, `OclEngine.evaluate(...)` returns OCL-spec-native Java types:

| OCL type        | Java class (default)                                  |
|-----------------|-------------------------------------------------------|
| `Sequence(T)`   | `java.util.ArrayList`                                 |
| `OrderedSet(T)` | `OclOrderedSet` (extends `ArrayList`, OCL equality)   |
| `Bag(T)`        | `OclBag` (extends `ArrayList`)                        |
| `Set(T)`        | `OclSet` (extends `AbstractSet`, OCL equality)        |
| `Map(K,V)`      | `java.util.LinkedHashMap`                             |
| `Integer`       | `Integer` (narrowed from `Long` when it fits)         |

When a caller expects EMF-shaped results — e.g. the result is being assigned
to an `EList<T>`-typed variable or fed back into EMF APIs — set
`useEMFTypes = true`. The engine then wraps **top-level only** collection/map
results:

- `Collection` (any OCL kind) → unmodifiable `EList`
- `Map` → unmodifiable `EMap`

Nested collections inside the result are left untouched. Non-collection
values (numbers, strings, `EObject`s, …) pass through unchanged.

#### Example

```java
OclEvaluationOptions options = OclEvaluationOptions.strict()
    .withUseEMFTypes(true);

// "self.employees->select(e | e.name.startsWith(prefix))->asSequence()"
EList<Person> matches = (EList<Person>) engine.evaluate(parsed, ctx, options);
```

The same flag can be set once on the engine configuration so every call uses
it:

```java
OclConfiguration config = OclConfiguration.builder(parser)
    .useEMFTypes(true)
    .build();
OclEngine engine = OclEngines.create(config);
```

> **Delegate note:** The EMF invocation delegate always returns an `EList` for
> multi-valued `EOperation`s regardless of this flag — that is required by the
> EMF contract (see [issue #3](https://github.com/eclipse-fennec/emf.m2x/issues/3)).
> The `useEMFTypes` flag (see [issue #4](https://github.com/eclipse-fennec/emf.m2x/issues/4))
> only affects the shape of `OclEngine.evaluate(...)` return values.

---

## 7. Caching

### 7.1 Expression Cache

The `OclLruExpressionCache` caches parsed `OclExpression` ASTs keyed by `(expression, contextType)`:

```java
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;

OclLruExpressionCache cache = OclLruExpressionCache.ofSize(2048);
OclEngine engine = OclEngines.create(new OclParserSupport(), cache);

// Cache statistics
long hits = cache.hitCount();
long misses = cache.missCount();
long size = cache.size();
double hitRate = (double) hits / (hits + misses);
```

- **Thread-safe** — internally synchronized
- **LRU eviction** — least-recently-used entries evicted when full
- **Cache key** — `nsURI#contextTypeName#expression`

### 7.2 WarmUp

Pre-populate the PropertyAccessorCache and parse common expressions:

```java
OclEngine engine = OclEngines.create(config);
engine.warmUp(MyPackage.eINSTANCE);
```

This caches property accessors for all classes in the package, reducing first-evaluation latency.

### 7.3 Performance Impact

From benchmarks with cache enabled:

| Scenario | Without Cache | With Cache | Speedup |
|----------|--------------|------------|---------|
| Simple expression (repeated) | 6.66 ms | ~0.06 ms | ~100x |
| Medium expression (repeated) | 34.52 ms | ~0.35 ms | ~100x |

---

## 8. Error Handling

### 8.1 Parse Errors

`OclParseException` is a checked exception carrying one diagnostic per problem:

```java
try {
    engine.parse("self.name +++ 42", myEClass);
} catch (OclParseException e) {
    System.err.println(e.getMessage());
    for (Resource.Diagnostic error : e.getErrors()) {
        System.err.printf("  Line %d, Col %d: %s%n",
            error.getLine(), error.getColumn(), error.getMessage());
    }
}
```

**Positions are real for both kinds now** (#110). A syntax error carries what ANTLR reported; an unresolved name or unknown type carries the place the offending name starts — several problems on one line are told apart by their column. `0:0` still means "position unknown" and is what you get for a diagnostic reported without a parse context at all, which is rare.

### 8.2 OclInvalid vs. null

OCL distinguishes three value states:

| State | Meaning | Check |
|-------|---------|-------|
| Regular value | Normal result | `result != null && !(result instanceof OclInvalid)` |
| `null` | OCL void/undefined | `result == null` |
| `OclInvalid.INSTANCE` | OCL invalid (error) | `result instanceof OclInvalid` |

### 8.3 OclResult — Three States

When using `evaluateWithDiagnostics()`:

```java
OclResult result = engine.evaluateWithDiagnostics(expr, ctx, options);

if (result.hasValue()) {
    // Normal value — no errors, value is present and not invalid
    process(result.getValueAs(String.class));
} else if (result.isInvalid()) {
    // OclInvalid — the expression evaluated to invalid
    handleInvalid(result.diagnostics());
} else if (result.isNull()) {
    // null — the expression evaluated to void/undefined
    handleNull();
}

// Or simply check for success (no ERROR-level diagnostics)
if (result.isSuccess()) {
    // safe to use result.value()
}
```

---

## 9. EMF Delegate Integration

Register the OCL engine as an EMF delegate for derived features, operation bodies, and validation constraints.

### 9.1 Standalone Registration

```java
OclEngine engine = OclEngines.create(config);
engine.installDelegates();

// Now EMF will use Fennec OCL for:
// - EOperation invocation delegates (body annotations)
// - EStructuralFeature setting delegates (derivation/initial annotations)
// - EValidator validation delegates (constraint annotations)

// Cleanup when done
engine.uninstallDelegates();
```

### 9.2 Delegate URI

The native Fennec OCL delegate URI is:

```
http://www.eclipse.org/fennec/m2x/ocl/1.0
```

The engine also serves the legacy Eclipse OCL Pivot delegate URI for
interop (see [9.6](#96-legacy-eclipse-ocl-pivot-namespace)):

```
http://www.eclipse.org/emf/2002/Ecore/OCL/Pivot
```

Both are exposed as `OclDelegateUtil.DELEGATE_URI` and
`OclDelegateUtil.LEGACY_PIVOT_URI`; the full set served by the engine is
`OclDelegateUtil.SERVED_URIS`.

### 9.3 Ecore Annotations

In your `.ecore` model, add annotations with this delegate URI:

**Derived Feature:**
```xml
<eStructuralFeatures xsi:type="ecore:EAttribute" name="fullName" eType="ecore:EDataType ...">
  <eAnnotations source="http://www.eclipse.org/fennec/m2x/ocl/1.0">
    <details key="derivation" value="self.firstName + ' ' + self.lastName"/>
  </eAnnotations>
</eStructuralFeatures>
```

**Operation Body:**
```xml
<eOperations name="isAdult" eType="ecore:EDataType ...">
  <eAnnotations source="http://www.eclipse.org/fennec/m2x/ocl/1.0">
    <details key="body" value="self.age >= 18"/>
  </eAnnotations>
</eOperations>
```

**Validation Constraint:**
```xml
<eAnnotations source="http://www.eclipse.org/fennec/m2x/ocl/1.0">
  <details key="nameNotEmpty" value="self.name.size() > 0"/>
</eAnnotations>
```

### 9.4 Operations of Dynamic Models

An `.ecore` loaded at runtime has no generated `XxxImpl` and therefore no `eInvoke`
switch. That does **not** mean its operations cannot run — it means EMF asks a delegate
instead, and the OCL engine is one. Three things have to come together:

```java
// 1. the package declares which delegate URI it uses
EAnnotation packageAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
packageAnnotation.setSource("http://www.eclipse.org/emf/2002/Ecore");
packageAnnotation.getDetails().put("invocationDelegates",
        "http://www.eclipse.org/fennec/m2x/ocl/1.0");
bookPackage.getEAnnotations().add(packageAnnotation);

// 2. the operation carries its body
EAnnotation body = EcoreFactory.eINSTANCE.createEAnnotation();
body.setSource("http://www.eclipse.org/fennec/m2x/ocl/1.0");
body.getDetails().put("body", "self.title.toUpper()");
shout.getEAnnotations().add(body);

// 3. the factory is registered
engine.installDelegates();

book.eInvoke(shout, ECollections.emptyEList());   // → "MOBY DICK"
```

In an `.ecore` file the same three parts look like this — the package annotation is the
one that is easy to forget:

```xml
<ecore:EPackage ...>
  <eAnnotations source="http://www.eclipse.org/emf/2002/Ecore">
    <details key="invocationDelegates" value="http://www.eclipse.org/fennec/m2x/ocl/1.0"/>
  </eAnnotations>
  <eClassifiers xsi:type="ecore:EClass" name="Book">
    <eOperations name="shout" eType="ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//EString">
      <eAnnotations source="http://www.eclipse.org/fennec/m2x/ocl/1.0">
        <details key="body" value="self.title.toUpper()"/>
      </eAnnotations>
    </eOperations>
  </eClassifiers>
</ecore:EPackage>
```

**Without a body annotation there is nothing to invoke.** An operation declared in a
dynamic `.ecore` and meant to be implemented in Java needs generated model code — with a
dynamic package, evaluating such a call reports:

```
Operation 'displayLabel' has no implementation. Either the model is generated and
implements it, or the operation carries an OCL body annotation and the package
declares the delegate (see installDelegates())
```

So the rule of thumb: **generated model code → implement in Java; dynamic model →
express the body in OCL.** Mixing the two — a dynamic model with an operation that
expects a Java implementation — is the one combination that cannot work.

### 9.5 OSGi

In OSGi, the delegate factories (`OclInvocationDelegateFactory`, `OclSettingDelegateFactory`,
`OclValidationDelegateFactory`) are registered automatically as DS components with
emf.osgi whiteboard properties. The emf.osgi delegate registry components discover
them and populate the global EMF registries. No manual `installDelegates()` call needed.

Each delegate factory injects `OclEngine` via `@Reference` to access both the
public API methods (`parse`, `evaluate`) and internal delegate methods
(`getDelegateOptions`, `evaluatePostcondition`).

### 9.6 Legacy Eclipse OCL Pivot Namespace

The Fennec engine also serves the legacy **Eclipse OCL Pivot** delegate URI:

```
http://www.eclipse.org/emf/2002/Ecore/OCL/Pivot
```

This lets models authored against Eclipse OCL — whose derived features,
operation bodies, and constraints are annotated under the Pivot URI rather
than the Fennec URI — evaluate with the Fennec engine **without re-annotating
the model**. The Fennec OCL dialect is a superset of Eclipse OCL's, so no
expression translation is involved; only the annotation source differs.

Both URIs are registered on every path:

- **Standalone** — `installDelegates()` registers each factory under all
  served URIs; `uninstallDelegates()` removes them all.
- **OSGi** — the emf.osgi delegate registry reads `emf.configuratorName` as a
  single value, so each delegate type ships a thin companion component for the
  legacy URI (`OclLegacyPivotSettingDelegateFactory`,
  `OclLegacyPivotInvocationDelegateFactory`,
  `OclLegacyPivotValidationDelegateFactory`). These subclass the Fennec
  factories and only differ in their whiteboard properties
  (`emf.configuratorName` = the Pivot URI, `emf.name=fennec-ocl-pivot`).

The factories resolve the OCL expression by consulting the served URIs in
order (Fennec first, Pivot as fallback), so a feature annotated under either
URI is picked up transparently. An example of a model that relies on this is
`org.eclipse.daanse.cwm.model.cwmx.eorm`, whose derived features (e.g.
`Column.name = cwmColumn.name`) are annotated under the legacy Pivot URI.

To serve additional legacy or third-party delegate URIs, add them to
`OclDelegateUtil.SERVED_URIS` — standalone registration, expression lookup, and
warm-up all iterate that list. For OSGi, also add a companion component for the
new URI (one per delegate type), mirroring the `OclLegacyPivot*` factories.

> **Note:** This changes only the set of annotation sources the engine
> *answers to*. The OCL expressions themselves are handled exactly as before.

---

## 10. Complete OCL Documents

Load standalone `.ocl` documents that define additional operations, properties, and constraints for existing Ecore models.

### 10.1 Parse a Document

```java
import org.eclipse.fennec.m2x.model.ocl.Constraint;

List<Constraint> constraints = engine.parseDocument(
    """
    context Person
    inv nameNotEmpty: self.name.size() > 0
    inv agePositive: self.age >= 0

    context Person
    def: fullName : String = self.firstName + ' ' + self.lastName
    """
);
```

### 10.2 Load and Register

`loadDocument()` parses the document and registers all definitions (def:, inv:) with the engine:

```java
engine.loadDocument(oclDocumentText);

// Now def:-operations are available in subsequent evaluations
Object fullName = engine.evaluate("self.fullName", OclContext.of(personObject));
```

### 10.3 With ResourceSet

If the document references types from specific packages:

```java
List<Constraint> constraints = engine.parseDocument(oclDocument, myResourceSet);
```

This resolves against the resource set's own package registry, for that one call. To make *every* parse of an engine use a specific registry, give it to the parser instead (see §3.7).

### 10.3.1 Import declarations

A Complete OCL document may open with `import`, `include` or `library`. These are **not** part of OCL v2.4 — its Complete OCL concrete syntax is `packageDeclarationCS ::= "package" pathNameCS contextDeclarationCS* "endpackage"` (§12.12.1), and the specification does not use the word "import" at all. Fennec accepts them for compatibility with Eclipse OCL, so what each one does is a decision:

| Form | Effect |
|---|---|
| `import company::Company` | **Nothing, and nothing is missing.** Type names resolve against the whole package registry the engine was given, so an import cannot add anything that is not already visible. |
| `import c : company` | The alias is remembered — `c::Person` resolves for the rest of the document. |
| `library company::Utilities` | **Reported as an error.** Additional operations reach the engine as `OclOperationProvider`s, not by being named in a document, so there is nothing to resolve this to. |

The `library` form is reported rather than skipped on purpose: skipping it means the operations it was supposed to bring are missing, and the document then fails somewhere else with "unknown operation" — at a place that says nothing about the cause. See [§11](#11-custom-operations) for how to supply operations.

### 10.4 Locating Documents

A Complete OCL document is passed as text, so where it comes from is the caller's business. When that means a file or a bundle resource, `UriHelper` in the shared `org.eclipse.fennec.m2x` bundle produces the EMF URI the resource set expects:

```java
import org.eclipse.fennec.m2x.utils.UriHelper;

URI uri = UriHelper.fromPath(Path.of("/srv/constraints/company.ocl"));
String document = new String(resourceSet.getURIConverter()
        .createInputStream(uri).readAllBytes(), StandardCharsets.UTF_8);

engine.parseDocument(document, resourceSet);
```

`UriHelper.fromJavaUri` and `fromFile` cover the other forms; `toJavaUri` converts back. Do not build a URI with `URI.createURI(path.toString())` — a path is not a URI, and on Windows the drive letter becomes the scheme.

> `UriHelper` ships in the bundle **`org.eclipse.fennec.m2x`** (package `org.eclipse.fennec.m2x.utils`) — the shared commons of this workspace, separate from the engine bundles:
>
> ```xml
> <dependency>
>     <groupId>org.eclipse.fennec.m2x</groupId>
>     <artifactId>org.eclipse.fennec.m2x</artifactId>
>     <version>0.1.1-SNAPSHOT</version>
> </dependency>
> ```

### 10.5 OSGi: CompleteOclContribution

In OSGi, deploy Complete OCL documents as whiteboard services:

```java
import org.eclipse.fennec.m2x.ocl.api.CompleteOclContribution;

@Component(service = CompleteOclContribution.class)
public class MyOclConstraints implements CompleteOclContribution {

    @Override
    public URI getDocumentUri() {
        return URI.createURI("platform:/plugin/my.bundle/model/constraints.ocl");
    }

    @Override
    public String getDocumentText() {
        return """
            context Person
            inv nameNotEmpty: self.name.size() > 0
            """;
    }
}
```

---

## 11. Custom Operations

Extend the OCL standard library with custom operations.

### 11.1 Define an Operation

Use `OclOperation.of()` for simple no-arg operations or the full constructor for operations with parameters:

```java
import org.eclipse.fennec.m2x.ocl.api.OclOperation;

// Simple no-arg operation using factory method
OclOperation toUpperCase = OclOperation.of(
    "toUpperCase",    // operation name
    stringOclType,    // owner type (OclType instance for String)
    stringOclType,    // return type
    (self, args) -> ((String) self).toUpperCase()
);
```

### 11.2 Register via Provider

```java
public class MyOperations implements OclOperationProvider {

    @Override
    public List<OclOperation> getOperations() {
        return List.of(
            new OclOperation(
                "trimToNull",
                stringType,
                List.of(),       // no parameters
                stringType,
                (self, args) -> {
                    String s = ((String) self).trim();
                    return s.isEmpty() ? null : s;
                }
            )
        );
    }
}

// Standalone registration via configuration (D29: no runtime registration)
OclConfiguration config = OclConfiguration.builder(new OclParserSupport())
    .operationProviders(List.of(new MyOperations()))
    .customOperationsEnabled(true)  // D29: required opt-in
    .build();
OclEngine engine = OclEngines.create(config);
```

### 11.3 OSGi Registration

In OSGi, `OclEngineComponent` binds **every** registered `OclOperationProvider`. Several can
coexist — the MOFM2T standard library, a set of domain operations, the operations a generator
brings — and they have no reason to exclude each other. The OCL specification has no notion of
one source of additional operations either: §11.8.1 makes the standard library extensible, and
a document may carry any number of `def:` declarations.

Register a provider as a DS component and it is bound:

```java
@Component(service = OclOperationProvider.class,
           property = "provider.name=myProvider")
public class MyOperations implements OclOperationProvider {
    @Override
    public List<OclOperation> getOperations() {
        return List.of(/* ... */);
    }
}
```

Binding a provider does not make its operations usable. The gate does that, and it is shut by
default (D29, see §14 Security):

```json
{
    "DefaultOclEngine": {
        "ocl.customOperationsEnabled": true
    }
}
```

That is the whole security decision: with the gate shut, no config-registered operation resolves,
however many providers a runtime happens to hold. With it open, all of them do.

A deployment that wants exactly one provider says so with a target filter — the reference is
named `operationProvider`:

```json
{
    "DefaultOclEngine": {
        "ocl.customOperationsEnabled": true,
        "operationProvider.target": "(provider.name=myProvider)"
    }
}
```

### 11.3.1 Which operation answers

Among the registered operations, the one that answers a call is chosen by, in order:

1. **How many arguments the call passes.** An operation declaring that many parameters is preferred, so a no-argument and a one-argument operation of the same name do not hide each other. If none matches that way, the count is ignored — several operations declare no parameter types at all and are still called with arguments.
2. **How specific the receiver type is.** An exact primitive match beats a widening one (Integer → Real), which beats a catch-all (`OclAny` / `AnyType`).
3. **Registration order.** Among equally applicable candidates the first registered one answers — whichever provider it came from. Two providers offering the same operation is a configuration mistake, not a crash, and this is what makes the outcome predictable rather than dependent on the order services happened to bind.

The parameter *types* are not checked, only their number.

### 11.4 Using OclConfiguration

```java
OclConfiguration config = OclConfiguration.builder(parser)
    .addOperationProvider(new MyOperations())
    .addOperationProvider(new MoreOperations())
    .build();
```

---

### 11.5 Tutorial: compiled OCL that follows the model version

A model states its own constraints. Until something asks for one, each is a string in an
annotation, so the first validation of a model pays for parsing and the second does not — and
an LRU inside the engine cannot help with the part that actually matters: it knows how many
entries it may keep, not what a model version is. An expression compiled against a model long
retired survives because it was used recently; one belonging to a live model is dropped
because it was not.

This wires up the other arrangement: **a model's OCL is compiled when the model registers, and
filed under that model version.** Four configurations, no code.

Everything below is what `OclMetadataCacheOSGiTest` and `OclMetadataBridgeOSGiTest` run, so the
tutorial and the tests cannot drift apart.

### What the pieces do

| Piece | Role |
|---|---|
| `EObjectRegistry` | holds the compiled expressions, each anchored with `emf.nsURI` and `emf.fingerprint` |
| `OclMetadataExpressionCache` | the `OclExpressionCache` service over that registry, with an LRU for what has no model version to anchor to |
| `OclConstraintPrecompiler` | binds `EPackage` services: when a model arrives, compiles the OCL it declares; when it goes, drops it |
| `EObjectRegistryMetadataBridge` | optional — puts registry content onto the metadata tree of the model version it names |
| `OclExpressionAnchorResolver` | tells that bridge which class a compiled expression belongs to: its context type |

### The configuration

```json
{
  ":configurator:resource-version": 1,

  "EObjectRegistry~ocl": {
    "name": "ocl-compiled",
    "initialProvider.target": "(emf.eobject.provider.name=ocl-empty-provider)"
  },

  "OclMetadataExpressionCache": {
    "writer.target": "(emf.eobject.registry.name=ocl-compiled)",
    "ocl.metadata.lruSize": 256
  },

  "OclConstraintPrecompiler": {
    "cache.target": "(cache.name=metadata)",
    "versioned.target": "(cache.name=metadata)"
  },

  "EObjectRegistryMetadataBridge~ocl": {
    "emf.eobject.registry.name": "ocl-compiled",
    "aspect.type.id": "ocl.compiled",
    "anchorResolver.target": "(anchor.resolver.name=ocl)"
  },

  "DefaultOclEngine": {
    "expressionCache.target": "(cache.name=metadata)"
  }
}
```

The last one is what makes an engine use it. Without it everything above still runs and simply
holds nothing anyone reads — nothing here displaces the default cache by existing.

### The initial provider

A registry publishes only once its initial provider has finished loading, which is what keeps
a consumer from ever seeing a half-filled one. Compiled OCL does not come from a content
source — it is written as expressions are compiled — so the provider here loads nothing, and
says so:

```java
@Component(service = EObjectProvider.class,
           property = "emf.eobject.provider.name=ocl-empty-provider")
public class EmptyEObjectProvider implements EObjectProvider {

    @Override
    public CompletableFuture<Void> load(EObjectRegistryWriter writer) {
        return CompletableFuture.completedFuture(null);
    }
}
```

If the registry should also carry **authored** expression libraries from files, use
`FileEObjectProvider` instead and the compiled ones join them.

The bridge is what makes compiled OCL findable from a class: `getClassAspect(book,
"ocl.compiled")`. It needs the anchor resolver in its target, because by default a bridge anchors
an entry at the `eClass()` of its content — for a compiled expression that is a class of the OCL
metamodel, where nobody looks. The resolver anchors it at the **context type** instead.

### The trigger is the model

The precompiler reacts to the arrival of an `EPackage`, not to a metadata handler callback. That
is not a detail: a version's metadata tree is published only *after* every handler has run, and
handlers run in registration order — so content derived inside a handler and pushed from there
lands on the tree being built only if the bridge happened to be wired first. Reacting to the
model itself has no such dependency.

### What you get

- A model's invariants, derivations and operation bodies are compiled once, when it arrives.
- Each is keyed by the derived fingerprint of the version it was compiled against, so two
  versions of one nsURI never answer for each other — the failure that is otherwise silent, since
  feature access falls back to resolving by name while a type reference does not.
- Each carries the **plain** model fingerprint as `emf.fingerprint`, which is what the bridge
  compares against a tree before placing anything on it. So an expression compiled against v1
  appears on v1's tree and on no other, even while v2 of the same nsURI is live.
- A version that goes takes its compiled OCL with it.
- A model that registers *later* than its compiled OCL still gets it, through the handler replay.

### Limits worth knowing

- **Changing a model after registration is out of contract.** A fingerprint describes the
  package as it was; nothing here re-checks a model that changed underneath.
- **The registry is the read face, not the aspect.** A class carries at most one aspect per
  type id, and an OCL context type has as many expressions as someone writes — so with several
  expressions on one class, the aspect shows the last one placed. Ask the registry for all of
  them.
- **The bridge needs the entry's fingerprint to be honoured**, which requires
  `org.eclipse.fennec.emf.osgi.eobject.registry.metadata` 1.1.0 or newer
  ([emf.osgi#81](https://github.com/eclipse-fennec/emf.osgi/issues/81)). Against an older
  version, a compiled expression is copied onto every live version's tree, where its references
  still point into the package it was built from.
- A constraint that does not parse is logged and skipped. Refusing the model would turn a
  validation error into a model that cannot be loaded at all; the delegate still reports it
  when someone asks.

---

## 11.9 OCL and Compiled Units

There is no `OclEngine.compile()`, and OCL has no entry in a unit store: an OCL expression is not
something you import by name, so there is nothing to package, store and resolve. What does concern
OCL is that expressions live **inside** the units of the other three languages:

- The `m2x1` unit fingerprint walks the OCL expressions of a transformation or template, so what it
  says about a QVT-O mapping includes the OCL it contains. Standard-library types enter as URIs,
  and a metamodel reference as `nsURI#//Classifier/feature` — where a metamodel was loaded from
  does not change the value.
- A navigation whose owner type is unknown at parse time leaves a *marked* placeholder, which the
  evaluator resolves by name at runtime. A caller that hands over a whole document can ask for the
  typo instead:

```java
new OclParserSupport(registry).strictPropertyResolution(true).parse("self.nam", personClass);
// OclParseException: Unknown property (Person::nam)
```

See the [Compiled Units Guide](compiled-units-guide.md) §10.

## 12. Thread Safety

| Component | Thread-Safe? | Notes |
|-----------|:---:|-------|
| `OclEngine` | Yes | Stateless evaluation; shared parser and cache are synchronized |
| `OclLruExpressionCache` | Yes | Internally synchronized with atomic counters |
| `OclContext` | Yes | Immutable record |
| `OclEvaluationOptions` | Yes | Immutable record |
| `OclResult` | Yes | Immutable record |
| `OclExpression` (parsed AST) | Yes | Immutable after parsing; safe to share across threads |
| `OclParserSupport` | Yes | Thread-safe parser implementation |
| `installDelegates()` | No | Modifies global EMF registries — call once at startup |

**Recommended pattern for multi-threaded use:**

```java
// Create once, share across threads
OclEngine engine = OclEngines.create(config);

// Each thread creates its own context
ExecutorService pool = Executors.newFixedThreadPool(8);
for (EObject obj : objects) {
    pool.submit(() -> {
        OclContext ctx = OclContext.of(obj);
        return engine.evaluate("self.name", ctx);
    });
}
```

---

## 13. Value Type Mapping

> See also [Section 6.4](#64-return-types--java-vs-emf) for the exact runtime
> classes used for OCL collections and the `useEMFTypes` option that switches
> top-level `Collection`/`Map` returns to `EList`/`EMap`.

OCL types map to Java types as follows:

| OCL Type | Java Type | Notes |
|----------|-----------|-------|
| `Boolean` | `java.lang.Boolean` | |
| `Integer` | `java.lang.Integer` | Promoted to `Long` for large values |
| `Real` | `java.lang.Double` | |
| `String` | `java.lang.String` | |
| `UnlimitedNatural` | `java.lang.Integer` | `*` maps to `-1` |
| `OclVoid` | `null` | |
| `OclInvalid` | `OclInvalid.INSTANCE` | Singleton |
| `Set(T)` | `java.util.Set<T>` | `LinkedHashSet` (preserves insertion order) |
| `OrderedSet(T)` | `java.util.Set<T>` | `LinkedHashSet` |
| `Bag(T)` | `java.util.List<T>` | `ArrayList` (duplicates allowed) |
| `Sequence(T)` | `java.util.List<T>` | `ArrayList` |
| `Tuple` | `java.util.Map<String, Object>` | Keys are part names |
| Any EClass | `org.eclipse.emf.ecore.EObject` | |
| Any EEnum | EMF-generated enum literal | |
| Any EDataType | Corresponding Java instance class | |

---

## 14. Security Hardening

When evaluating OCL expressions from untrusted sources (user input, external Complete OCL documents, model annotations from unknown models), configure conservative resource limits to prevent denial-of-service attacks.

### Configurable Limits

| Field | Type | Default | Protects against |
|-------|------|---------|-----------------|
| `maxDepth` | int | 1,000 | Stack overflow via deeply nested expressions |
| `maxCollectionSize` | int | 1,000,000 | Range explosion, product explosion, allInstances |
| `maxClosureIterations` | int | 100,000 | Unbounded closure traversal |
| `maxRegexLength` | int | 1,000 | ReDoS via crafted regex patterns |
| `timeout` | Duration | none | Runaway evaluation (deadline-based enforcement) |

All limits produce `OclInvalid` with a diagnostic error when exceeded.

### Sandboxed Evaluation

```java
import java.time.Duration;

// Configure conservative limits for untrusted input
OclEvaluationOptions sandboxed = OclEvaluationOptions.strict()
    .withMaxDepth(100)
    .withMaxCollectionSize(10_000)
    .withMaxClosureIterations(1_000)
    .withMaxRegexLength(200)
    .withTimeout(Duration.ofSeconds(5));

// Evaluate with sandboxed options
OclResult result = engine.evaluateWithDiagnostics(expr, context, sandboxed);

// Check for limit violations
if (result.value() == OclInvalid.INSTANCE) {
    result.diagnostics().forEach(d ->
        logger.warn("OCL limit violation: {}", d.getMessage()));
}
```

### How Limits Are Enforced

- **Recursion depth** — checked in `eval()` before every expression evaluation
- **Collection size** — checked before creating ranges (`Sequence{1..n}`), products, and allInstances results
- **Closure iterations** — checked in the worklist loop of `closure()` iterator
- **Regex length** — checked before `Pattern.compile()` in `matches()`, `replaceAll()`, `replaceFirst()`
- **Timeout** — deadline-based `System.nanoTime()` check at every `eval()` call and every closure iteration (~15ns overhead)

### Trust Boundaries

| Source | Trust Level | Recommendation |
|--------|------------|----------------|
| Own model annotations | Trusted | Default `strict()` options |
| External Complete OCL documents | Semi-trusted | `strict()` with tightened limits |
| User input (console, LSP) | Untrusted | Tightened limits + timeout |
| Custom operation providers | Controlled (D29) | Disabled by default; requires `customOperationsEnabled` on both Config and Options |
| EMF delegates | Explicit opt-in | Configure `delegateOptions` before `installDelegates()` |

### OSGi: Engine-Wide Security Defaults via ConfigAdmin

In an OSGi environment, configure conservative defaults centrally via ConfigAdmin instead of relying on each caller to pass sandboxed options:

```json
{
    ":configurator:resource-version": 1,
    "DefaultOclEngine": {
        "ocl.maxDepth": 100,
        "ocl.maxCollectionSize": 10000,
        "ocl.maxClosureIterations": 1000,
        "ocl.maxRegexLength": 200,
        "ocl.timeout": 5000,
        "ocl.nullHandling": "STRICT",
        "ocl.errorRecovery": "FAIL_FAST"
    }
}
```

These limits apply to all `evaluate(expression, context)` calls (without explicit options), including EMF delegate evaluations. Callers can still pass explicit `OclEvaluationOptions` to override for individual evaluations.

### EMF Delegate Security

```java
// Configure delegate options BEFORE installing delegates
engine.setDelegateOptions(sandboxed);
engine.installDelegates();
```

After `installDelegates()`, any model loaded in the `EPackage.Registry` can trigger OCL evaluation via EAnnotations. Only install delegates if you trust the models being loaded.

### Custom Operation Security (D29)

Custom operations are **disabled by default**. To enable them, set `customOperationsEnabled` on both the engine configuration and the per-evaluation options (AND-linked):

```java
// Engine configuration: enable custom ops engine-wide
OclConfiguration config = OclConfiguration.builder(parserSupport)
    .addOperationProvider(myProvider)
    .customOperationsEnabled(true)
    .build();

// Per-evaluation: enable for this specific evaluation
OclEvaluationOptions opts = OclEvaluationOptions.strict()
    .withCustomOperationsEnabled(true);
```

If either flag is `false`, custom operations are not dispatched. This allows sandboxing individual evaluations (e.g. user input) even when the engine has providers registered.

