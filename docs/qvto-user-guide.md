# QVT-O Engine User Guide

Fennec QVT-O is a lightweight, spec-compliant QVT Operational v1.3 transformation engine that works as a standalone Java library — no Eclipse platform required.

## Table of Contents

1. [Overview](#1-overview)
2. [Quick Start](#2-quick-start)
3. [Engine Setup](#3-engine-setup)
4. [Model Extents](#4-model-extents)
5. [Parsing Transformations](#5-parsing-transformations)
6. [Executing Transformations](#6-executing-transformations)
7. [Execution Options](#7-execution-options)
8. [Caching](#8-caching)
9. [Error Handling](#9-error-handling)
10. [Tracing](#10-tracing)
11. [Blackbox Libraries](#11-blackbox-libraries)
12. [Multi-File Composition](#12-multi-file-composition)
13. [Security Hardening](#13-security-hardening)

---

## 1. Overview

The Fennec QVT-O Engine provides:

- **QVT Operational v1.3** transformation parsing and execution
- **Standalone operation** — works as a plain Java library without OSGi
- **OSGi-optional** — full Declarative Services support when running in OSGi
- **ANTLR4-based parser** — fast, reliable parsing (2.2x faster than Eclipse's LPG parser)
- **Built on Fennec OCL** — full OCL v2.5 expression support within transformations
- **Blackbox libraries** — call Java code from QVT-O transformations (§8.1.4)
- **Multi-file composition** — `access` keyword, `new T(args)`, transformation chaining
- **Trace support** — full trace model for resolve operations and debugging

### Performance

Fennec QVT-O is faster than Eclipse QVT-O across all dimensions:

| Operation | Speedup |
|-----------|---------|
| Parse | 2.2x faster |
| Execution (cached) | 11% faster |
| End-to-End | 2.3x faster |

See [benchmark-results.md](benchmark-results.md) for detailed numbers.

### Bundles

| Bundle | Description |
|--------|-------------|
| `org.eclipse.fennec.m2x.qvto.api` | Public API interfaces |
| `org.eclipse.fennec.m2x.qvto.parser` | ANTLR4 parser |
| `org.eclipse.fennec.m2x.qvto.engine` | Evaluator implementation, DS components |
| `org.eclipse.fennec.m2x.qvto.model` | QVT-O & Trace EMF metamodel |
| `org.eclipse.fennec.m2x.qvt.model` | QVTBase metamodel, shared with QVT-R |
| `org.eclipse.fennec.m2x.qvtd.api` | Needed by the engine: it implements `RelationImplementationProvider` for QVT-R hybrid execution |
| `org.eclipse.fennec.m2x.unit` | Language-neutral unit contracts: compile, store, prepare |
| `org.eclipse.fennec.m2x.ocl.{api,model,parser,engine}` | Fennec OCL |

**Standalone class path.** Outside OSGi nothing resolves dependencies for you. These are needed for the Quick Start below — checked by running it with exactly them:

- the bundles above, `ocl.*` being four jars
- `org.antlr:antlr4-runtime` 4.13.x
- `org.eclipse.emf.common` and `org.eclipse.emf.ecore`
- `org.eclipse.fennec.emf.osgi.component.minimal` — the engine computes metamodel fingerprints with its `FingerprintHelper`; it carries `FingerprintService` itself, so `emf.osgi.api` is not needed in plain Java

Compiled units (§12.2.1) additionally need `org.eclipse.emf.ecore.xmi`. Under OSGi the engine also needs `org.eclipse.fennec.m2x.unit.osgi` and the `emf.osgi` runtime that provides the `ResourceSet` and `FingerprintService` services (§3.4).

---

## 2. Quick Start

Minimal example — parse a QVT-O transformation from a String and execute it. It copies `EPackage`s, so it needs no metamodel of its own:

```java
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;

// 1. Create engine
OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
QvtoConfiguration qvtoConfig = QvtoConfiguration.builder(oclConfig).build();
QvtoEngine engine = QvtoEngines.create(qvtoConfig);

// 2. Parse transformation
OperationalTransformation trafo = engine.parse("""
    modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');

    transformation CopyPackages(in source : ECORE, out target : ECORE);

    main() {
        source.rootObjects()[EPackage]->map toCopy();
    }

    mapping EPackage::toCopy() : EPackage {
        name := self.name + 'Copy';
        nsURI := self.nsURI + '/copy';
    }
    """, "CopyPackages");

// 3. Set up model extents
EPackage myPackage = EcoreFactory.eINSTANCE.createEPackage();
myPackage.setName("library");
myPackage.setNsURI("http://example.org/library");
BasicQvtoModelExtent inExtent = new BasicQvtoModelExtent(myPackage);
inExtent.setReadOnly(true);  // in-parameter
BasicQvtoModelExtent outExtent = new BasicQvtoModelExtent();

// 4. Execute
QvtoExecutionResult result = engine.execute(trafo, QvtoExecutionContext.of(inExtent, outExtent));

// 5. Read output
if (result.isSuccess()) {
    List<EObject> outputObjects = outExtent.getContents();  // one EPackage "libraryCopy"
}
```

Every metamodel a transformation uses is declared with `modeltype … uses '<nsURI>'` and resolved through the package registry (§3.3); a type name without one fails to parse with *Unknown type (…)*.

For a worked example over real files — two metamodels, an instance model and two `.qvto` files,
run ad hoc and as compiled units — see the `org.eclipse.fennec.m2x.qvto.example` project. Its
`main()` classes are the walkthrough.

---

## 3. Engine Setup

### 3.1 Three ways to give QVT-O its OCL engine

QVT-O evaluates transformation expressions with an OCL engine. Which one, is the caller's choice — and the first door needs no OCL knowledge at all:

```java
// 1. don't care: QVT-O settings alone, the factory supplies a default OCL engine
QvtoConfiguration.builder().build();

// 2. bring the engine you already have — the injected service under OSGi,
//    OclEngines.create(...) in plain Java
QvtoConfiguration.builder(oclEngine).build();

// 3. configure the OCL side yourself
QvtoConfiguration.builder(oclConfiguration).build();
```

A supplied engine is used as it is: its cache, its operation providers, its evaluation settings. `QvtoEngine.getOclEngine()` returns the engine that actually runs — useful to warm it up, inspect its cache, or install EMF delegates on it.

Under OSGi the engine is a service and door 2 is what the component uses — see [§3.4](#34-osgi-setup).

### 3.1.1 Configuring both sides

The full form, when the OCL side needs configuration too:

```java
import java.util.Set;

import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;

// OCL configuration (shared with QVT-O)
OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport())
    .expressionCache(OclLruExpressionCache.ofSize(4096))
    .build();

// QVT-O configuration — or builder(oclEngine) to use an engine you already have
QvtoConfiguration qvtoConfig = QvtoConfiguration.builder(oclConfig)
    .blackboxRegistry(myBlackboxRegistry)
    .blackboxEnabled(true)                                // off by default
    .allowedBlackboxModules(Set.of("my.lib.StringUtils"))
    .addUnitResolver(myUnitResolver)
    .unitResolverEnabled(true)                            // off by default
    .allowedUnitModules(Set.of("my.company.Utilities"))
    .build();

QvtoEngine engine = QvtoEngines.create(qvtoConfig);
```

### 3.2 Configuration Options

`QvtoConfiguration.Builder` methods:

| Method | Description |
|--------|-------------|
| `resourceSet(resourceSet)` | Resource set whose package registry resolves `modeltype … uses '<nsURI>'` |
| `packageRegistry(registry)` | The registry itself (default: `EPackage.Registry.INSTANCE`); wins over `resourceSet` when both are set |
| `registerPackage(ePackage)` | Contributes a metamodel that answers for its nsURI ahead of the registry — local to this configuration, nothing reaches the global registry |
| `blackboxRegistry(registry)` | Registry for blackbox Java libraries (default: none) |
| `blackboxEnabled(boolean)` | Whether blackbox libraries are consulted at all (default: `false`) |
| `allowedBlackboxModules(set)` | Qualified names that may be imported as a blackbox; empty puts no restriction (default: empty) |
| `maxBlackboxLibraries(int)` | Blackbox libraries one link may pull in (default: 10) |
| `addUnitResolver(resolver)` | Add a unit resolver for multi-file composition |
| `unitResolvers(list)` | Set all unit resolvers at once |
| `unitResolverEnabled(boolean)` | Whether unit resolvers are consulted at all (default: `false`) |
| `discoverUnitResolvers(boolean)` | Also ask resolvers found through `ServiceLoader`; requires `unitResolverEnabled`, counts as one resolver (default: `false`) |
| `allowedUnitModules(set)` | Qualified names that may be imported as a unit; empty puts no restriction (default: empty) |
| `maxUnitResolvers(int)` | Unit resolvers one engine will use (default: 5) |
| `parallelExecutor(executor)` | Executor for `parallelTransform()` (default: virtual threads) |

A registry or resolver alone does nothing: blackboxes and unit resolvers are off until `blackboxEnabled(true)` or `unitResolverEnabled(true)` switches them on, because both let a transformation reach code and files outside itself.

### 3.3 Which Metamodels the Engine Sees

A `modeltype LIB uses 'http://example.org/library/1.0';` declaration is resolved when the transformation is parsed. By default the engine looks in `EPackage.Registry.INSTANCE`, which is the right answer in plain Java. Hand it your own registry when you hold the packages yourself:

```java
// the ResourceSet you already have — under OSGi this is what emf.osgi injects
QvtoConfiguration qvtoConfig = QvtoConfiguration.builder(oclConfig)
    .resourceSet(resourceSet)
    .build();

// or the registry itself, if that is what you hold
QvtoConfiguration qvtoConfig = QvtoConfiguration.builder(oclConfig)
    .packageRegistry(registry)
    .build();
```

If both are set, the explicitly configured registry wins — the more specific setting beats the more general one. Only the resource set's package registry is used; nothing is loaded through it.

Under OSGi you do not do this yourself: the DS component binds the `ResourceSet` that `emf.osgi` publishes, as a mandatory reference — see §3.4.

Everything downstream — parser, linker, alias resolution, nested blackbox invocations — uses exactly that registry; nothing reaches for the global one on its own (D42). Under OSGi, or wherever two versions of one nsURI can coexist, this is what keeps the engine from forming its own opinion about which version an nsURI names. Model version identity stays yours; see the `emf.osgi` fingerprint guide.

An nsURI that resolves in no registry is an error: `parse` fails with `QvtoParseException` carrying *Failed to resolve metamodel (…)*. QVT v1.3 §8.2.1.6 declares `ModelType.metamodel` as `[1..*]`, so a model type without a metamodel would not be a well-formed AST. A type name that resolves in neither the registry, the module's `typedef`s nor the QVT-O standard library fails the same way, with *Unknown type (…)*; every unresolved name of the unit is reported at once.

The standard library types of QVT v1.3 §8.3.1 — `Exception`, `StringException`, `AssertionFailed` and the `Void` synonym — are part of the language and need no metamodel: they resolve without a registry entry, and a metamodel classifier of the same name takes precedence over them.

### 3.4 OSGi Setup

`DefaultQvtoEngine` publishes `QvtoEngine` as a **PROTOTYPE**-scoped service. Every `@Reference` injection gets its own engine with its own caches, so one consumer's warm-up and another's memory footprint stay separate.

Its configuration policy is **optional**: there is a working engine without configuring anything, and configuring one later does not register a second service that consumers would have to disambiguate.

```java
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.annotation.require.RequireQVTO;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@RequireQVTO                     // resolver: a bundle providing the QVT-O engine must be present
@Component
public class MyTransformationRunner {

    @Reference
    private QvtoEngine engine;   // fresh instance, own caches

    public void run(EObject input) throws QvtoParseException {
        OperationalTransformation trafo = engine.parse(source, "MyTrafo");
        engine.execute(trafo, QvtoExecutionContext.of(inExtent, outExtent));
    }
}
```

**Components and scopes:**

| Component | Scope | What's shared? |
|---|---|---|
| `DefaultQvtoEngine` | PROTOTYPE | Nothing — each consumer gets its own engine and trace state |
| `DefaultOclEngine` | PROTOTYPE | Nothing — bound `prototype_required`, so each QVT-O engine evaluates on its own OCL engine |
| `DefaultOclExpressionCache` | SINGLETON | The parsed-expression cache, shared by all OCL engines |
| `QvtoServiceUnitResolver` | SINGLETON | Looks up units in the service registry by the name an import gives (§3.4.2); bound by every engine as `unitDiscovery`, consulted only with `qvto.discoverUnitResolvers` |
| `ResourceSet` (from `emf.osgi`) | PROTOTYPE | Nothing — bound `prototype_required`, so each engine resolves metamodels through its own resource set |

The engine takes its OCL engine as a **mandatory** service reference. That is door 2 from [§3.1](#31-three-ways-to-give-qvt-o-its-ocl-engine): the OCL engine a consumer configured — with its cache and its operation providers — is the one that evaluates transformation expressions, rather than a second one built beside it.

The **resource set** is a mandatory reference as well, and deliberately not an optional one with a fallback to the global registry. Under `emf.osgi` a metamodel registered for the resource set (`emf.model.scope=resourceset`) lives in the resource set's package registry and never in the global one — an engine built on the fallback would resolve a `modeltype … uses '<nsURI>'` declaration to the wrong package instance and match nothing, silently. And an optional reference cannot say *when* the service arrives, only that it was not there at activation. Mandatory means: the engine exists once the resource set does, and what §3.3 describes for plain Java happens by itself. Where several resource sets are published, pick one:

```json
{
    "DefaultQvtoEngine": { "resourceSet.target": "(emf.name=myapp)" }
}
```

The fingerprint service `emf.osgi` provides is mandatory for the same reason; it is a plain singleton reference, not `prototype_required`. Blackbox libraries are bound the other way — dynamically, `0..n`, see §11.2.

### 3.4.1 Configuration via ConfigAdmin

All properties use the `qvto.` prefix. OCL limits are **not** repeated here; they are configured on `DefaultOclEngine` with `ocl.*`, so the two cannot drift apart.

```json
{
    ":configurator:resource-version": 1,
    "DefaultQvtoEngine": {
        "qvto.blackboxEnabled": true,
        "qvto.allowedBlackboxModules": ["my.trusted.Library"]
    },
    "DefaultOclEngine": {
        "ocl.maxDepth": 500
    }
}
```

**Available properties:**

| Property | Type | Default | Description |
|---|---|---|---|
| `qvto.blackboxEnabled` | boolean | `false` | Whether transformations may call Java blackbox libraries — bound as `QvtoBlackboxLibrary` services, see §11.2 |
| `qvto.allowedBlackboxModules` | String[] | *(empty)* | Qualified names that may be imported as a blackbox; **empty puts no restriction on the names** |
| `qvto.unitResolverEnabled` | boolean | `false` | Whether transformations may import units resolved from outside |
| `qvto.discoverUnitResolvers` | boolean | `false` | Whether resolvers registered as services are looked up by the name a transformation imports |
| `qvto.allowedUnitModules` | String[] | *(empty)* | Qualified names that may be imported as a unit; **empty puts no restriction on the names** |
| `qvto.maxBlackboxLibraries` | int | 10 | Blackbox libraries one engine will use |
| `qvto.maxUnitResolvers` | int | 5 | Unit resolvers one engine will use |

Blackboxes and unit resolvers are **off by default**: both let a transformation reach code and files outside itself, so switching them on is a decision.

The allow-lists narrow what may be reached once a feature is on. An **empty** allow-list does not narrow anything — enabling `blackboxEnabled` or `unitResolverEnabled` without also naming what may be reached permits every name. Name them.

The same settings are available to plain Java through `QvtoConfiguration.Builder` — see [§3.2](#32-configuration-options).

### 3.4.2 Units the engine finds itself

A transformation names what it imports, so the engine knows at link time which name it needs and can ask for exactly that. A bundle offers a unit by publishing a resolver under the name it answers for:

```java
@Component(service = QvtoUnitResolver.class,
           property = "qvto.unit.name=my.company.Utilities")
public class UtilitiesUnit implements QvtoUnitResolver {

    private final URI uri = URI.createURI("platform:/plugin/com.acme/Utilities.qvto");
    private final String source = "...";  // the unit's QVT-O text

    @Override
    public Optional<QvtoUnit> resolveUnit(String qualifiedName) {
        return Optional.of(new QvtoUnit.SourceUnit(qualifiedName, uri, source));
    }
}
```

The engine looks that up when it links `import my.company.Utilities;` — not before. It is deliberately not a whiteboard: a declarative reference would have to bind every resolver in the framework up front, and a static one would restart the engine, and drop its caches, each time one came or went.

Outside OSGi the same thing happens through `ServiceLoader`, via `QvtoConfiguration.Builder.discoverUnitResolvers(true)` and a `META-INF/services` entry.

**One mechanism per environment, on purpose.** The class-path route looks through the class loader of `QvtoUnitResolver`, not through the thread context class loader — inside OSGi that loader belongs to the api bundle and cannot see into others, so the route is inert there and the service registry is the only one that answers. This bundle also declares no `osgi.serviceloader` requirement, so a Service Loader Mediator such as Aries SPI Fly does not weave the call and feed it from that same registry; otherwise two mechanisms would answer the same import.

**Both are off by default, and that is deliberate.** `unitResolverEnabled` alone leaves the allow-list empty, which puts no restriction on names — so discovery would let anything registered, or anything on the class path, answer an import. Turning discovery on is a second decision, and the sensible companion to it is naming what may be reached:

```json
{
    "DefaultQvtoEngine": {
        "qvto.unitResolverEnabled": true,
        "qvto.discoverUnitResolvers": true,
        "qvto.allowedUnitModules": ["my.company.Utilities"]
    }
}
```

Discovery widens **who may answer**, never **which names may be asked**: the allow-list is checked before any resolver is consulted, and the discovering resolver counts as one against `qvto.maxUnitResolvers`.

**Running on a specific OCL engine:** the reference is unfiltered by default, so the engine binds whichever `OclEngine` service is there. To pin it to one you configured yourself, set a target filter:

```json
{
    "DefaultQvtoEngine": { "oclEngine.target": "(component.name=MyTunedOclEngine)" }
}
```

---

## 4. Model Extents

Model extents are containers for the input and output models of a transformation.

### 4.1 Creating Extents

```java
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;

// Empty extent (for output)
QvtoModelExtent outExtent = new BasicQvtoModelExtent();

// Pre-populated extent (for input)
QvtoModelExtent inExtent = new BasicQvtoModelExtent(myRootObject);

// Multiple root objects
QvtoModelExtent inExtent = new BasicQvtoModelExtent(List.of(obj1, obj2, obj3));
```

### 4.2 Read-Only for `in` Parameters

Mark input extents as read-only to enforce the QVT-O `in` parameter contract (§8.1.3.2):

```java
BasicQvtoModelExtent inExtent = new BasicQvtoModelExtent(myModel);
inExtent.setReadOnly(true);

// Any modification attempt now throws:
// UnsupportedOperationException: "Cannot modify read-only model extent (in-parameter)"
```

### 4.3 Reading Output

After execution, read the transformation output from the `out` extent:

```java
List<EObject> outputRoots = outExtent.getContents();

// Cast to expected type
Schema schema = (Schema) outputRoots.get(0);
```

### 4.4 Inout Parameters

For `inout` parameters, the extent is both input and output:

```java
BasicQvtoModelExtent inoutExtent = new BasicQvtoModelExtent(existingModel);
// Do NOT mark as read-only — inout allows modification
```

---

## 5. Parsing Transformations

### 5.1 From String

```java
OperationalTransformation trafo = engine.parse("""
    modeltype PKG uses 'http://example.org/library/1.0';
    transformation T(in m : PKG);
    main() { /* ... */ }
    """,
    "T"  // unit name
);
```

### 5.2 From URI

```java
import org.eclipse.emf.common.util.URI;

OperationalTransformation trafo = engine.parse(URI.createURI("file:/path/to/MyTrafo.qvto"));
```

The source is read through the `URIConverter` of the configured resource set — or of a default one, when none is configured — so `file:`, `http:` and `archive:` URIs work out of the box, and `platform:`/`bundleresource:` as soon as you hand over a resource set that knows them (§3.3).

Callers rarely start with an EMF URI. `UriHelper` in the shared `org.eclipse.fennec.m2x` bundle converts the forms you are likely to hold:

```java
import org.eclipse.fennec.m2x.utils.UriHelper;

engine.parse(UriHelper.fromPath(Path.of("/srv/templates/report.mtl")));
engine.parse(UriHelper.fromJavaUri(request.getUri()));
engine.parse(UriHelper.fromFile(chooser.getSelectedFile()));
```

Use it rather than `URI.createURI(path.toString())`: a path is not a URI. On Windows `C:\templates\report.mtl` would become a URI whose scheme is `C`, while `fromPath` uses `createFileURI`, which knows about drive letters and separators. `UriHelper.toJavaUri(uri)` goes back the other way, for APIs that speak `java.net.URI`.

> `UriHelper` ships in the bundle **`org.eclipse.fennec.m2x`** (package `org.eclipse.fennec.m2x.utils`) — the shared commons of this workspace, separate from the engine bundles:
>
> ```xml
> <dependency>
>     <groupId>org.eclipse.fennec.m2x</groupId>
>     <artifactId>org.eclipse.fennec.m2x</artifactId>
>     <version>0.1.1-SNAPSHOT</version>
> </dependency>
> ```

### 5.3 Reuse Parsed ASTs

Parse once and execute many times — the `OperationalTransformation` AST is reusable:

```java
// Parse once at startup
OperationalTransformation trafo = engine.parse(source, "MyTrafo");

// Execute repeatedly with different inputs
for (EObject input : inputs) {
    BasicQvtoModelExtent in = new BasicQvtoModelExtent(input);
    in.setReadOnly(true);
    BasicQvtoModelExtent out = new BasicQvtoModelExtent();

    QvtoExecutionResult result = engine.execute(trafo, QvtoExecutionContext.of(in, out));
    results.add(out.getContents());
}
```

---

## 6. Executing Transformations

### 6.1 Positional Parameters

The simplest form — extents are matched to transformation parameters by position:

```java
// transformation T(in src : SRC, out tgt : TGT)
QvtoExecutionResult result = engine.execute(
    trafo,
    QvtoExecutionContext.of(inExtent, outExtent)
);
```

### 6.2 With Configuration Properties

A transformation declares its configuration properties and reads them by name; the execution context supplies the values:

```java
QvtoExecutionContext ctx = QvtoExecutionContext.of(
    List.of(inExtent, outExtent),
    Map.of(
        "outputDir", "/tmp/generated",
        "verbose", true
    )
);
```

In QVT-O:
```
configuration property outputDir : String;
configuration property verbose : Boolean;

main() {
    if verbose then log('writing to ' + outputDir) endif;
}
```

### 6.3 With Named Parameter Bindings

For explicit parameter-to-extent mapping:

```java
QvtoExecutionContext ctx = QvtoExecutionContext.builder()
    .addModelExtent("src", inExtent)
    .addModelExtent("tgt", outExtent)
    .configProperty("outputDir", "/tmp")
    .build();
```

### 6.4 Multiple Extents per Parameter

For collection-of-models parameters (§8.1.1):

```java
QvtoExecutionContext ctx = QvtoExecutionContext.builder()
    .addModelExtents("sources", extent1, extent2, extent3)
    .addModelExtent("target", outExtent)
    .build();
```

---

## 7. Execution Options

### 7.1 QvtoEvaluationOptions

```java
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;

QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults()
    .withMaxStackDepth(500)
    .withTimeout(Duration.ofSeconds(30))
    .withTracing(true)
    .withOclOptions(OclEvaluationOptions.strict());

QvtoExecutionResult result = engine.execute(trafo, ctx, options);
```

### 7.2 Option Reference

| Option | Default | Description |
|--------|---------|-------------|
| `maxStackDepth` | 1,000 | Maximum recursive mapping/helper call depth |
| `timeout` | 30 s | Maximum execution time (deadline-enforced); `withTimeout(null)` removes it |
| `maxLoopIterations` | 1,000,000 | Maximum iterations per while/for loop |
| `maxDiagnostics` | 10,000 | Maximum diagnostic entries before truncation |
| `maxTraceRecords` | 1,000,000 | Maximum trace records (0 = unlimited) |
| `tracingEnabled` | false | Return the trace in `QvtoExecutionResult.trace()`; records are kept either way, because resolve needs them |
| `oclOptions` | the OCL engine's defaults | OCL evaluation options (limits, providers) for the transformation's expressions; unset, the defaults of the OCL engine apply — what `OclConfiguration` or `ocl.*` configured |

### 7.3 OCL Options Within QVT-O

Every OCL expression of a transformation — helper bodies, guards, initialisers — is evaluated
with `oclOptions()` when the run sets them, else with the defaults of the OCL engine the QVT-O
engine runs on. So a limit configured once on the OCL engine (`OclConfiguration`, or `ocl.*`
under ConfigAdmin) holds inside QVT-O as well, and a run can tighten it further. Null handling
is always LENIENT on top of that: module-level operations have no `self`, and QVT-O reads a
null operand as empty rather than invalid.

```java
OclEvaluationOptions oclOpts = OclEvaluationOptions.strict()
    .withMaxCollectionSize(500_000)
    .withTimeout(Duration.ofSeconds(5));

QvtoEvaluationOptions opts = QvtoEvaluationOptions.defaults()
    .withOclOptions(oclOpts);
```

---

## 8. Caching

### 8.1 OCL Expression Cache (Shared)

QVT-O shares the OCL expression cache. Configure it in `OclConfiguration`:

```java
OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport())
    .expressionCache(OclLruExpressionCache.ofSize(4096))
    .build();

QvtoConfiguration qvtoConfig = QvtoConfiguration.builder(oclConfig).build();
```

OCL expressions within QVT-O mapping bodies, guards, and helpers are cached automatically.

### 8.2 Transformation AST Reuse

The biggest performance gain comes from parsing transformations once and reusing the AST:

```java
// Parse once
OperationalTransformation trafo = engine.parse(source, "MyTrafo");

// Execute many times — no re-parsing needed
for (int i = 0; i < 1000; i++) {
    engine.execute(trafo, ctx);
}
```

### 8.3 Performance Characteristics

| Strategy | Impact |
|----------|--------|
| OCL expression cache | Eliminates re-parsing of OCL expressions in mapping bodies |
| AST reuse | Eliminates transformation re-parsing (2.2x parser speedup) |
| Combined | End-to-end 2.3x faster than Eclipse QVT-O |

---

## 9. Error Handling

### 9.1 Parse Errors

`QvtoParseException` is thrown when the transformation source has syntax errors:

```java
try {
    engine.parse(source, "MyTrafo");
} catch (QvtoParseException e) {
    System.err.println(e.getMessage());
    for (Resource.Diagnostic error : e.getErrors()) {
        System.err.printf("  Line %d, Col %d: %s%n",
            error.getLine(), error.getColumn(), error.getMessage());
    }
}
```

**Positions are real for both kinds now** (#110). A syntax error carries what ANTLR reported; an unresolved name, unknown type or unknown metamodel carries the place it stands. `0:0` still means "position unknown", which is what a diagnostic reported without any parse context gets.

### 9.1.1 Type Errors (#378)

A transformation is type-checked when it is parsed, with the OCL rules (see the OCL user guide,
§14.6) and QVT-O's own. Each of these is a parse error at its position:

```
var x : String := 5;                       -- The variable 'x' is declared as String but initialized with a Integer
x := 5;                                    -- A Integer cannot be assigned to a String
e.name := Sequence{'a', 'b'};              -- A Sequence(String) cannot be assigned to a single-valued String
isElement(5)    -- query isElement(e : Element)   Argument 1 of isElement is a Integer, but the parameter 'e' is a EObject
'abc'.substring('a', 2)                    -- No operation String::substring(String, Integer); …
if (5) then 1 else 2 endif                 -- The condition of an if is a Integer, not a Boolean
transformation T(in s : String);           -- Model parameter 's' has the type 'String', which is no model type …
var x : Shared := null;  -- Shared in two model types   Type 'Shared' is ambiguous: …
```

An element or a collection is a valid condition (§8.4.5(2)). A helper or query call is checked
against its declaration wherever it stands; parameters with a default may be left out.

A disjunct candidate needs the disjunction's number of parameters and a result type that is the
same or derived (§8.1.14.1). An operation that overrides one of an `extends`-imported module —
same name, context and arity — needs parameter types that comply with the overridden one's
(§8.2.1.10); that is checked when the transformation is linked, so it surfaces at `compile()` or
`execute()`, like an unresolved import.

Unknown types are never an error: a value of a dynamic model, a blackbox result, a call into an
imported unit. Intermediate class attributes have their declared type (#404), so
`h.label := 5` for `label : String` is a type error too. What the evaluator
meets at run time is checked there, as before.

### 9.2 Execution Diagnostics

`QvtoExecutionResult` contains diagnostics collected during execution:

```java
QvtoExecutionResult result = engine.execute(trafo, ctx);

if (!result.isSuccess()) {
    for (Diagnostic d : result.diagnostics()) {
        System.err.printf("[%d] %s%n", d.getSeverity(), d.getMessage());
    }
}

if (result.hasFatalError()) {
    // CANCEL-level severity — transformation was aborted
}
```

`isSuccess()` is true when no diagnostic is `ERROR` or worse; `hasFatalError()` when one is `CANCEL`.

### 9.3 Severity Levels

| Level | Value | Meaning |
|-------|:-----:|---------|
| `Diagnostic.OK` | 0 | Success |
| `Diagnostic.INFO` | 1 | Information, e.g. `log()` output |
| `Diagnostic.WARNING` | 2 | Warning (transformation completed) |
| `Diagnostic.ERROR` | 4 | Error (transformation may have partial output) |
| `Diagnostic.CANCEL` | 8 | Fatal error (transformation aborted) |

The values are those of `org.eclipse.emf.common.util.Diagnostic`.

### 9.4 QVT-O log() and assert

`log()` and `assert` statements in QVT-O produce diagnostics:

```
-- In QVT-O:
log("Processing " + self.name);
assert fatal (self.name <> null) with log("Name must not be null");
```

These appear in `result.diagnostics()` with appropriate severity levels.

---

## 10. Tracing

Traces record the mapping execution history, enabling resolve operations and post-execution analysis.

### 10.1 Enabling Tracing

The engine records traces in every run — resolve operations depend on them. The option decides only whether the result hands the trace out:

```java
QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults()
    .withTracing(true);

QvtoExecutionResult result = engine.execute(trafo, ctx, options);
```

### 10.2 Reading Trace Records

```java
import org.eclipse.fennec.m2x.model.trace.Trace;
import org.eclipse.fennec.m2x.model.trace.TraceRecord;

Trace trace = result.trace();
if (trace != null) {
    for (TraceRecord record : trace.getTraceRecords()) {
        String mappingName = record.getMappingOperation().getName();
        String moduleName = record.getMappingOperation().getModule();

        // Context object
        VarParameterValue ctxParam = record.getContext().getContext();

        // Input parameters
        for (VarParameterValue param : record.getParameters().getParameters()) {
            System.out.printf("  %s %s : %s = %s%n",
                param.getKind(), param.getName(), param.getType(),
                param.getValue().getModelElement());
        }

        // Output results
        for (VarParameterValue res : record.getResult().getResult()) {
            EObject outputObj = res.getValue().getModelElement();
        }
    }
}
```

### 10.3 Resolve Operations and Trace

QVT-O resolve operations (`resolve`, `resolveone`, `resolveIn`, etc.) use the trace to find previously created objects:

```
-- In QVT-O:
mapping Class::classToTable() : Table {
    name := self.name;
    columns := self.attributes->map attributeToColumn();
}

mapping Attribute::attributeToColumn() : Column {
    name := self.name;
    table := self.owner.resolveone(Table);  -- finds Table via trace
}
```

Resolve works whether or not `tracingEnabled` is set: the records are kept either way, up to `maxTraceRecords` (§13.3).

### 10.4 TraceRecord Structure

```
TraceRecord
  ├── EMappingOperation (name, module, package)
  ├── EMappingContext
  │     └── VarParameterValue (context object)
  ├── EMappingParameters
  │     └── VarParameterValue[] (input parameters)
  └── EMappingResults
        └── VarParameterValue[] (output results)
              └── EValue (modelElement, primitiveValue, collection, oclObject)
```

---

## 11. Blackbox Libraries

Call Java code from QVT-O transformations (§8.1.4).

### 11.1 Implement a Library

```java
import java.util.List;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxInvocationContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxLibrary;

public class StringUtils implements QvtoBlackboxLibrary {

    @Override
    public String getModuleName() {
        return "StringUtils";  // the name of the synthetic library module
    }

    @Override
    public String getUnitQualifiedName() {
        return "my.lib.StringUtils";  // what 'import my.lib.StringUtils;' resolves to
    }

    @Override
    public List<String> getUsedPackageURIs() {
        return List.of();  // EPackage nsURIs used by this library
    }

    @Override
    public List<BlackboxOperationDescriptor> getOperationDescriptors() {
        // String::capitalize() : String — contextual, called as 'hello'.capitalize()
        BlackboxOperationDescriptor capitalize =
                QvtOperationalFactory.eINSTANCE.createBlackboxOperationDescriptor();
        capitalize.setName("capitalize");
        capitalize.setContextType(EcorePackage.Literals.ESTRING);
        capitalize.setReturnType(EcorePackage.Literals.ESTRING);

        // slugify(s : String) : String — module-level, called as slugify('Hello World')
        BlackboxOperationDescriptor slugify =
                QvtOperationalFactory.eINSTANCE.createBlackboxOperationDescriptor();
        slugify.setName("slugify");
        slugify.getParameterTypes().add(EcorePackage.Literals.ESTRING);
        slugify.setReturnType(EcorePackage.Literals.ESTRING);

        return List.of(capitalize, slugify);
    }

    @Override
    public Object invoke(String operationName, QvtoBlackboxInvocationContext context,
                         Object[] args) {
        return switch (operationName) {
            case "capitalize" -> {
                String s = (String) context.self();
                yield s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
            }
            case "slugify" -> ((String) args[0]).toLowerCase().replaceAll("\\s+", "-");
            default -> throw new IllegalArgumentException("Unknown operation: " + operationName);
        };
    }
}
```

A descriptor declares what the transformation may call: an operation with a `contextType` is contextual (`'hello'.capitalize()`, the receiver in `context.self()`), one without is module-level (`slugify('…')`, the arguments in `args`).

### 11.2 Register the Library

Blackboxes are off by default, because they let a transformation run Java code. Enabling them is a separate decision from registering a library: a registered library is unreachable until `blackboxEnabled` is on, and then only for the names the allow-list permits. An empty allow-list puts no restriction on the names.

**Standalone:**

```java
import java.util.Set;

import org.eclipse.fennec.m2x.qvto.api.BasicQvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;

BasicQvtoBlackboxRegistry registry = new BasicQvtoBlackboxRegistry();
registry.register(new StringUtils());

QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
    .blackboxRegistry(registry)
    .blackboxEnabled(true)                                  // required: off by default
    .allowedBlackboxModules(Set.of("my.lib.StringUtils"))   // recommended
    .build();
```

**OSGi:** publish the library as a `QvtoBlackboxLibrary` service. `DefaultQvtoEngine` binds every such service through a dynamic reference of cardinality `0..n` named `blackboxes`, and drops it when it goes away — without restarting the engine. Each link and each run uses the libraries bound at that moment.

```java
@Component(service = QvtoBlackboxLibrary.class)
public class StringUtils implements QvtoBlackboxLibrary {
    // ... as in 11.1
}
```

```json
"DefaultQvtoEngine": {
    "qvto.blackboxEnabled": true,
    "qvto.allowedBlackboxModules": ["my.lib.StringUtils"]
}
```

The reference is configured like any Declarative Services reference, through the engine's configuration:

| Property | Effect |
|----------|--------|
| `blackboxes.target` | LDAP filter on the library services this engine binds, e.g. `(blackbox.group=reporting)` |
| `blackboxes.cardinality.minimum` | The engine is not available until this many libraries are bound — the way to make a deployment wait for its libraries instead of failing an import that runs first |

Without `blackboxes.cardinality.minimum` a run sees what is registered when it starts: a library that arrives later is not waited for, and the import fails with `Cannot resolve import`. Where two services have the same `getUnitQualifiedName()`, the one with the higher `service.ranking` answers; among equal rankings, the one registered first.

### 11.3 Using in QVT-O

The library is imported by its `getUnitQualifiedName()`, and its operations are called like any other:

```
modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');

import my.lib.StringUtils;

transformation T(inout m : ECORE) {
    main() {
        m.objectsOfType(EPackage)->forEach(p) {
            p.name := p.name.capitalize();   -- contextual operation
            p.nsPrefix := slugify(p.name);   -- module-level operation
        };
    }
}
```

Selective import works as well: `from my.lib.StringUtils import capitalize;`.

### 11.4 BlackboxInvocationContext

The invocation context provides access to the execution environment:

```java
@Override
public Object invoke(String opName, QvtoBlackboxInvocationContext context, Object[] args) {
    // Access context object (self)
    Object self = context.self();

    // Access configuration properties
    String dir = (String) context.getConfigProperties().get("outputDir");

    // Access model extents
    QvtoModelExtent ext = context.getExtent("target");

    // Report diagnostics
    context.addInfo("Processing...");
    context.addWarning("Deprecated usage");
    context.addError("Invalid input");

    // Access EPackage registry
    EPackage.Registry registry = context.getPackageRegistry();

    return result;
}
```

---

## 12. Multi-File Composition

QVT-O supports splitting transformations across multiple files and calling between transformations.

### 12.1 QvtoUnitResolver

Implement `QvtoUnitResolver` to tell the engine where to find transformation units:

```java
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;

public class FileSystemUnitResolver implements QvtoUnitResolver {

    private final Path baseDir;

    public FileSystemUnitResolver(Path baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public Optional<QvtoUnit> resolveUnit(String qualifiedName) {
        // Convert qualified name to file path: "my.pkg.Helper" → "my/pkg/Helper.qvto"
        String path = qualifiedName.replace('.', '/') + ".qvto";
        Path file = baseDir.resolve(path);

        if (Files.exists(file)) {
            String source = Files.readString(file);
            URI uri = URI.createFileURI(file.toString());
            return Optional.of(new QvtoUnit.SourceUnit(qualifiedName, uri, source));
        }
        return Optional.empty();
    }
}
```

### 12.2 SourceUnit vs. CompiledUnit

`QvtoUnit` is a sealed interface with three variants:

```java
// Source code — will be parsed by the engine
QvtoUnit source = new QvtoUnit.SourceUnit("my.pkg.Helper", uri, sourceCode);

// Pre-compiled — already parsed OperationalTransformation
QvtoUnit compiled = new QvtoUnit.CompiledUnit("my.pkg.Helper", parsedTrafo);

// By reference — where the unit lives; loaded when it is consumed, in the consumer's context
QvtoUnit referenced = new QvtoUnit.ResourceUnit("my.pkg.Helper", compiledUnitUri);
```

A `ResourceUnit` URI holds a compiled-unit document (or a bare AST); loading it runs the same validation as a store load.

Use `CompiledUnit` to hand the engine a transformation it has already parsed, within one JVM.

Note the two meanings of the word: `QvtoUnit.CompiledUnit` is a *parsed AST in memory* handed to a
resolver, while `engine.compile(...)` returns a `CompiledUnit` **document** that can be stored,
loaded elsewhere and executed there — with its dependencies embedded, pinned or rebound. See the
[Compiled Units Guide](compiled-units-guide.md).

### 12.2.1 Storable units, dependency modes, store, prepare

```java
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;

// a document instead of a graph — storable, with the library carried inside it
CompiledUnit unit = engine.compile(source, "Main", UnitCompileOptions.of(DependencyMode.EMBED));

UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
UnitKey key = store.put(unit);

// somewhere else: load, prepare, run — no resolver is asked during execution
PreparedContext prepared = UnitPreparer.withDefaults(store, engine.unitBinder()).prepare(key);
QvtoExecutionResult result = engine.execute(prepared, "Main", QvtoExecutionContext.of(extent));
```

`put` throws `UnitStoreException`, `prepare` throws `UnitPrepareException` (both in `org.eclipse.fennec.m2x.unit.api`). In plain Java this path needs `org.eclipse.emf.ecore.xmi` on the class path (§1). `QvtoStoreUnitResolver` resolves imports from a store. The whole mechanism — modes, store,
prepare, validation, fingerprints — is described in the
[Compiled Units Guide](compiled-units-guide.md).

### 12.3 Registration

**Standalone:**

```java
QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
    .addUnitResolver(new FileSystemUnitResolver(basePath))
    .unitResolverEnabled(true)                           // required: off by default
    .allowedUnitModules(Set.of("my.pkg.Helper"))         // recommended
    .build();
```

All resolvers are declared at configuration time; there is no registration at runtime.

**OSGi:** not a whiteboard. Publish the resolver with the name it serves as `qvto.unit.name`, and switch discovery on with `qvto.unitResolverEnabled` and `qvto.discoverUnitResolvers` — see §3.4.2:

```java
@Component(service = QvtoUnitResolver.class, property = "qvto.unit.name=my.pkg.Helper")
public class HelperUnit implements QvtoUnitResolver {
    // ...
}
```

A resolver service without `qvto.unit.name` is never asked.

### 12.4 QVT-O Syntax for Composition

**Access — call another transformation:**

```
modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');

transformation Main(inout m : ECORE)
    access Helper {
    main() {
        var helper := new Helper(m);
        var status := helper.transform();
        assert fatal (status.succeeded());
    }
}
```

**Import — reuse library modules:**

```
-- Import a library
import my.lib.StringUtils;

-- Selective import
from my.lib.StringUtils import capitalize, slugify;
```

**Transformation instantiation:** `transform()` and `parallelTransform()` are called on one transformation instance and return a `Status`; `wait` is a module-level operation that takes the statuses to wait for:

```
main() {
    -- Create and run another transformation
    var t := new OtherTrafo(m);
    var s := t.transform();
    if s.succeeded() then log('OtherTrafo completed successfully') endif;

    -- Parallel execution
    var s1 := new OtherTrafo(m1).parallelTransform();
    var s2 := new OtherTrafo(m2).parallelTransform();
    wait(Set{s1, s2});
}
```

A model passed to `parallelTransform()` is lent to that transformation until it ends (§8.1.3.3: "The Models passed to nested transformation calls … may not be modified after the call"):

- A second `parallelTransform()` on a model that is still in use fails: its status is failed, the transformation does not run, and an ERROR names the transformation in the way. Two transformations that both take the model as `in` may share it.
- The caller may not modify the model until the transformation ends, which `wait` makes sure of. A modification raises `InvalidModelMutation`.
- `transform()` runs synchronously, so the model is free again once the call returns.

An extent the engine binds to an `in` parameter is read-only for that execution only. Afterwards it is writable again, including the caller's model after a nested `transform()` (#387).

---

### Resolving units through a ResourceSet

`ResourceSetUnitResolver` resolves imported units through a resource set's `URIConverter`, so imports reach whatever that resource set reaches — files, bundle resources, archives, or anything a custom `URIHandler` serves:

```java
QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
    .addUnitResolver(new ResourceSetUnitResolver(
            resourceSet, URI.createURI("platform:/plugin/com.acme.transforms/")))
    .unitResolverEnabled(true)
    .build();
```

An import of `my.company.Helpers` is looked for at `<base>/my/company/Helpers.qvto`. The resolver answers with empty when it cannot find a unit, so further resolvers still get their turn, and the opt-in plus the module allow-list (§13) apply unchanged.

## 13. Security Hardening

When executing untrusted QVT-O transformations (e.g. user-supplied code in a web service or multi-tenant environment), the engine provides configurable resource limits to prevent denial-of-service attacks.

### 13.1 Configurable Limits

All limits are set via `QvtoEvaluationOptions`:

```java
import java.time.Duration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;

QvtoEvaluationOptions opts = QvtoEvaluationOptions.defaults()
    .withTimeout(Duration.ofSeconds(30))       // Q-5: execution deadline
    .withMaxLoopIterations(100_000)            // Q-4: while/for/forEach limit
    .withMaxStackDepth(200)                    // Q-5: recursion depth
    .withMaxDiagnostics(1_000)                 // Q-7: log/assert flooding
    .withMaxTraceRecords(500_000);             // Q-6: trace memory growth
```

| Option | Default | Purpose |
|--------|---------|---------|
| `timeout` | 30 s | Wall-clock deadline — terminates execution after elapsed time; `null` removes it |
| `maxLoopIterations` | 1,000,000 | Per-loop iteration cap — applies to `while`, `forEach`, `forExp` |
| `maxStackDepth` | 1,000 | Call stack depth — limits recursive helpers/mappings |
| `maxDiagnostics` | 10,000 | Maximum diagnostic messages before truncation |
| `maxTraceRecords` | 1,000,000 | Maximum trace records — further records are dropped, and the run fails with an error |

### 13.2 Sandboxed Execution Example

A typical configuration for executing untrusted transformations:

```java
import java.time.Duration;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;

// 1. Strict options with tight limits
QvtoEvaluationOptions opts = QvtoEvaluationOptions.defaults()
    .withTimeout(Duration.ofSeconds(10))
    .withMaxLoopIterations(50_000)
    .withMaxStackDepth(100)
    .withMaxDiagnostics(500)
    .withMaxTraceRecords(100_000)
    .withOclOptions(OclEvaluationOptions.strict());

// 2. Read-only input extent
BasicQvtoModelExtent inExtent = new BasicQvtoModelExtent(inputObjects);
inExtent.setReadOnly(true);

// 3. Execute
QvtoExecutionContext ctx = QvtoExecutionContext.of(inExtent, new BasicQvtoModelExtent());
QvtoExecutionResult result = engine.execute(parsed, ctx, opts);

// 4. Check result
if (!result.isSuccess()) {
    result.diagnostics().forEach(d -> log.warn("QVT-O: {}", d.getMessage()));
}
```

### 13.3 How Limits Are Enforced

- **Timeout**: A deadline (`System.nanoTime()`) is set at execution start. The evaluator checks the deadline at every function call entry and loop iteration (~15ns overhead per check). When exceeded, execution stops with an `Execution timeout exceeded` error.

- **Loop iterations**: Each `while`, `forEach`, and `forExp` loop maintains a per-loop counter. When `maxLoopIterations` is exceeded, the loop breaks with a `Maximum loop iterations exceeded: <limit>` error.

- **Stack depth**: Checked at every `callOperation`, `callMapping`, and `callConstructor`. When exceeded, the call returns with a `Maximum stack depth exceeded: <limit>` error.

- **Diagnostics**: The `log()` built-in and internal diagnostics are capped. When the limit is reached, one final warning `Maximum diagnostics limit reached (<limit>), further diagnostics truncated` is added and further diagnostics are silently dropped.

- **Trace records**: When `maxTraceRecords` is reached, further mapping trace records are dropped and one error `Maximum trace records exceeded: <limit>` is reported, so the run does not report success. Execution continues, but `resolve`/`invresolve` and the reuse of a mapping already applied may miss what was mapped after the limit (#284).

### 13.4 Embedder Responsibilities

The engine protects against resource exhaustion from QVT-O code, but the **host application** is responsible for:

| Concern | Recommendation |
|---------|---------------|
| **Blackbox libraries** (Q-1) | Only register trusted libraries; review all `invoke()` implementations |
| **Configuration properties** (Q-2) | Never pass secrets (DB passwords, API keys) as config properties |
| **URI path traversal** (Q-3) | Validate URIs in `QvtoUnitResolver` before resolving to filesystem paths |
| **SSRF via unit resolver** (Q-8) | Restrict `QvtoUnitResolver` to local paths; reject `http://`, `ftp://` URIs |
| **Read-only enforcement** (Q-9) | Mark input extents as `setReadOnly(true)` |

