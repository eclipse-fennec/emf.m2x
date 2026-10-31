# QVT-R Engine User Guide

Fennec QVT-R is a lightweight, spec-compliant QVT Relations v1.3 transformation engine that works as a standalone Java library — no Eclipse platform required.

## Table of Contents

1. [Overview](#1-overview)
2. [Quick Start](#2-quick-start)
3. [Engine Setup](#3-engine-setup)
4. [Model Extents](#4-model-extents)
5. [Parsing Transformations](#5-parsing-transformations)
6. [Executing Transformations](#6-executing-transformations)
7. [QVT-R Language Features](#7-qvt-r-language-features)
8. [Binding Validation](#8-binding-validation)
9. [Blackbox Libraries](#9-blackbox-libraries)
10. [Hybrid QVT-R / QVT-O](#10-hybrid-qvt-r--qvt-o)
11. [Error Handling](#11-error-handling)
12. [Tracing](#12-tracing)

---

## 1. Overview

The Fennec QVT-R Engine provides:

- **QVT Relations v1.3** transformation parsing and execution
- **Direct interpretation** — no intermediate QVT Core representation (D33)
- **Standalone operation** — works as a plain Java library without OSGi
- **OSGi-optional** — full Declarative Services support when running in OSGi (see §3.4)
- **ANTLR4-based parser** — fast, reliable parsing with precise error locations
- **Built on Fennec OCL** — full OCL v2.5 expression support within relations
- **Binding validation** — static §7.5 analysis catches unbound variables at parse time
- **Blackbox libraries** — call Java code from QVT-R via `implementedby`
- **Hybrid execution** — delegate relation implementations to QVT-O engines (D39)
- **Bidirectional** — enforce in any direction, or check-only

### Bundles

| Bundle | Description |
|--------|-------------|
| `org.eclipse.fennec.m2x.qvtd.api` | Public API interfaces |
| `org.eclipse.fennec.m2x.qvtd.parser` | ANTLR4 parser + §7.5 validator |
| `org.eclipse.fennec.m2x.qvtd.engine` | Direct interpretation engine, DS components |
| `org.eclipse.fennec.m2x.qvtd.model` | QVT-R EMF metamodel (2 EPackages: `qvtrelation`, `qvttemplate`) |
| `org.eclipse.fennec.m2x.qvt.model` | QVTBase metamodel (`qvtbase`), shared with QVT-O |
| `org.eclipse.fennec.m2x.unit` | Language-neutral unit contracts: compile, store, prepare |
| `org.eclipse.fennec.m2x.ocl.{api,model,parser,engine}` | Fennec OCL |

**Standalone class path.** Outside OSGi nothing resolves dependencies for you. These are needed for the Quick Start below — checked by running it with exactly them:

- the bundles above, `ocl.*` being four jars
- `org.antlr:antlr4-runtime` 4.13.x
- `org.eclipse.emf.common` and `org.eclipse.emf.ecore`
- `org.eclipse.fennec.emf.osgi.component.minimal` — the engine computes metamodel fingerprints with its `FingerprintHelper`; it carries `FingerprintService` itself, so `emf.osgi.api` is not needed in plain Java

Compiled units (§5.5) additionally need `org.eclipse.emf.ecore.xmi`; hybrid execution with QVT-O (§10) needs the QVT-O bundles. Under OSGi the engine also needs `org.eclipse.fennec.m2x.unit.osgi` and the `emf.osgi` runtime that provides the `ResourceSet` and `FingerprintService` services (§3.4).

---

## 2. Quick Start

Minimal example — parse a QVT-R transformation from a String and execute it. It copies `EPackage`s, so it needs no metamodel of its own:

```java
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;

// 1. Create engine
OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
QvtdConfiguration config = QvtdConfiguration.builder(oclConfig).build();
QvtdEngine engine = QvtdEngines.create(config);

// 2. Parse transformation — typed models name their metamodel (§3.3)
RelationalTransformation trafo = engine.parse("""
    transformation CopyPackages(source : ecore, target : ecore) {
        top relation PackageToPackage {
            pn : String;
            checkonly domain source p : EPackage { name = pn };
            enforce domain target q : EPackage { name = pn };
        }
    }
    """, "CopyPackages");

// 3. Set up named model extents
EPackage myPackage = EcoreFactory.eINSTANCE.createEPackage();
myPackage.setName("library");
QvtdModelExtent sourceExtent = QvtdModelExtent.of(myPackage);
QvtdModelExtent targetExtent = QvtdModelExtent.of();  // empty output

// 4. Execute in enforce mode, "target" being the direction
QvtdExecutionContext ctx = QvtdExecutionContext.enforce("target",
    Map.of("source", sourceExtent, "target", targetExtent));
QvtdExecutionResult result = engine.execute(trafo, ctx);

// 5. Read output
if (result.isSuccess()) {
    List<EObject> packages = targetExtent.getContents();  // one EPackage "library"
}
```

For a worked example over real files — a `.qvtr` transformation with keys, queries, a
where-chain and a when-guard, run ad hoc and as a compiled unit — see the
`org.eclipse.fennec.m2x.qvtd.example` project. Its `main()` classes are the walkthrough.

---

## 3. Engine Setup

### 3.1 Three ways to give QVT-R its OCL engine

QVT-R evaluates relation expressions with an OCL engine. Which one, is the caller's choice — and the first door needs no OCL knowledge at all:

```java
// 1. don't care: QVT-R settings alone, the factory supplies a default OCL engine
QvtdConfiguration.builder().build();

// 2. bring the engine you already have — the injected service under OSGi,
//    OclEngines.create(...) in plain Java
QvtdConfiguration.builder(oclEngine).build();

// 3. configure the OCL side yourself
QvtdConfiguration.builder(oclConfiguration).build();
```

A supplied engine is used as it is: its cache, its operation providers, its evaluation settings. `QvtdEngine.getOclEngine()` returns the engine that actually runs — useful to warm it up, inspect its cache, or install EMF delegates on it.

Under OSGi the engine is a service and door 2 is what the component uses — see [§3.4](#34-osgi-setup).

### 3.1.1 Configuring both sides

The full form, when the OCL side needs configuration too:

```java
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;

// OCL configuration (shared with QVT-R)
OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport())
    .build();

// QVT-R configuration
QvtdConfiguration config = QvtdConfiguration.builder(oclConfig)
    .blackboxRegistry(myBlackboxRegistry)
    .blackboxEnabled(true)        // off by default
    .maxRelationDepth(100)
    .build();

QvtdEngine engine = QvtdEngines.create(config);
```

### 3.2 Configuration Options

`QvtdConfiguration.Builder` methods:

| Method | Default | Description |
|--------|---------|-------------|
| `resourceSet(resourceSet)` | — | Resource set whose package registry resolves the transformation's typed models |
| `packageRegistry(registry)` | `EPackage.Registry.INSTANCE` | The registry itself; wins over `resourceSet` when both are set |
| `registerPackage(ePackage)` | — | Contributes a metamodel that answers for its nsURI ahead of the registry — local to this configuration, nothing reaches the global registry |
| `blackboxRegistry(registry)` | none | Registry for blackbox Java libraries (§9) |
| `blackboxEnabled(boolean)` | `false` | Whether blackbox libraries are consulted at all |
| `allowedBlackboxModules(Set)` | empty | Module names (`getModuleName()`) that may serve a blackbox operation; empty puts no restriction |
| `maxBlackboxLibraries(int)` | 10 | Distinct blackbox libraries one run may use |
| `addUnitResolver(resolver)` | — | Add a unit resolver for imports (§3.4.2) |
| `unitResolvers(list)` | — | Set all unit resolvers at once |
| `unitResolverEnabled(boolean)` | `false` | Whether unit resolvers are consulted at all |
| `discoverUnitResolvers(boolean)` | `false` | Also ask resolvers found through `ServiceLoader`; requires `unitResolverEnabled`, counts as one resolver |
| `allowedUnitModules(Set)` | empty | Qualified names that may be imported as a unit; empty puts no restriction |
| `maxUnitResolvers(int)` | 5 | Unit resolvers one engine will use |
| `maxRelationDepth(int)` | 200 | Maximum depth of relation calls |
| `maxBindings(int)` | 10,000 | Maximum bindings while matching a pattern |
| `timeoutMs(long)` | 30,000 | Execution timeout in milliseconds; 0 means none (`qvtd.timeout` under OSGi) |
| `maxTraceRecords(int)` | 1,000,000 | Maximum trace records one run may hold, across all relations; reaching it is an error |
| `deleteUnmatched(boolean)` | `true` | Whether an enforce run deletes the target objects that no valid binding requires (§7.10.2) |

A registry or resolver alone does nothing: blackboxes and unit resolvers are off until `blackboxEnabled(true)` or `unitResolverEnabled(true)` switches them on. Exceeding one of the limits ends the run with a `QvtdExecutionException` (§11.3).

### 3.3 Which Metamodels the Engine Sees

The typed models of a transformation — `transformation t(uml : simpleuml, rdbms : simplerdbms)` — are resolved when the transformation is parsed: first as a registry key (an nsURI), then by package name, and last by package name ignoring case. By default the engine looks in `EPackage.Registry.INSTANCE`, which is the right answer in plain Java. Hand it your own registry when you hold the packages yourself:

```java
// the ResourceSet you already have — under OSGi this is what emf.osgi injects
QvtdConfiguration config = QvtdConfiguration.builder(oclConfig)
    .resourceSet(resourceSet)
    .build();

// or the registry itself, if that is what you hold
QvtdConfiguration config = QvtdConfiguration.builder(oclConfig)
    .packageRegistry(registry)
    .build();
```

If both are set, the explicitly configured registry wins — the more specific setting beats the more general one. Only the resource set's package registry is used; nothing is loaded through it.

Under OSGi you do not do this yourself: the DS component binds the `ResourceSet` that `emf.osgi` publishes, as a mandatory reference — see §3.4.

A class in a template resolves only in the metamodels of the transformation's model types, and a domain's templates may only use classes of its own model's metamodels, subpackages included (§7.1). Before #358 the parser also searched the whole package registry, so `checkonly domain uml x : EClass {}` worked without Ecore in the header. That is now a positioned parse error, as are:

- an unknown model, class or property, including `opposite(…)` and keys;
- a `default_values` entry that assigns no variable;
- a call of a name that is neither a relation nor a query;
- a relation call with the wrong number or types of arguments;
- a duplicate relation or model parameter;
- an `extends` that names no transformation.

At run time the engine does not check whether an extent's contents conform to the model type, because the spec requires no such check.

Parser and engine use exactly that registry; nothing reaches for the global one on its own (D42). Under OSGi, or wherever two versions of one nsURI can coexist, this is what keeps the engine from forming its own opinion about which version an nsURI names. Model version identity stays yours; see the `emf.osgi` fingerprint guide.

### 3.4 OSGi Setup

`DefaultQvtdEngine` publishes `QvtdEngine` as a **PROTOTYPE**-scoped service. Every `@Reference` injection gets its own engine with its own caches, so one consumer's warm-up and another's memory footprint stay separate.

Its configuration policy is **optional**: there is a working engine without configuring anything, and configuring one later does not register a second service that consumers would have to disambiguate.

```java
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.annotation.require.RequireQVTD;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@RequireQVTD                     // resolver: a bundle providing the QVT-R engine must be present
@Component
public class MyTransformationRunner {

    @Reference
    private QvtdEngine engine;   // fresh instance, own caches

    public void run(EObject input) throws QvtdParseException {
        RelationalTransformation trafo = engine.parse(source, "MyTrafo");
        engine.execute(trafo, QvtdExecutionContext.enforce("target",
                Map.of("source", inExtent, "target", outExtent)));
    }
}
```

**Components and scopes:**

| Component | Scope | What's shared? |
|---|---|---|
| `DefaultQvtdEngine` | PROTOTYPE | Nothing — each consumer gets its own engine and trace state |
| `DefaultOclEngine` | PROTOTYPE | Nothing — bound `prototype_required`, so each QVT-R engine evaluates on its own OCL engine |
| `DefaultOclExpressionCache` | SINGLETON | The parsed-expression cache, shared by all OCL engines |
| `QvtdServiceUnitResolver` | SINGLETON | Looks up units in the service registry by the name an import gives (§3.4.2); bound by every engine as `unitDiscovery`, consulted only with `qvtd.discoverUnitResolvers` |
| `ResourceSet` (from `emf.osgi`) | PROTOTYPE | Nothing — bound `prototype_required`, so each engine resolves metamodels through its own resource set |

The engine takes its OCL engine as a **mandatory** service reference. That is door 2 from [§3.1](#31-three-ways-to-give-qvt-r-its-ocl-engine): the OCL engine a consumer configured — with its cache and its operation providers — is the one that evaluates relation expressions, rather than a second one built beside it.

The **resource set** is a mandatory reference as well, and deliberately not an optional one with a fallback to the global registry. Under `emf.osgi` a metamodel registered for the resource set (`emf.model.scope=resourceset`) lives in the resource set's package registry and never in the global one — an engine built on the fallback would resolve a typed model such as `uml : simpleuml` to the wrong package instance and match nothing, silently. And an optional reference cannot say *when* the service arrives, only that it was not there at activation. Mandatory means: the engine exists once the resource set does, and what §3.3 describes for plain Java happens by itself. Where several resource sets are published, pick one:

```json
{
    "DefaultQvtdEngine": { "resourceSet.target": "(emf.name=myapp)" }
}
```

The fingerprint service `emf.osgi` provides is mandatory for the same reason; it is a plain singleton reference, not `prototype_required`. Blackbox libraries are bound the other way — dynamically, `0..n`, see §9.2.

### 3.4.1 Configuration via ConfigAdmin

All properties use the `qvtd.` prefix. OCL limits are **not** repeated here; they are configured on `DefaultOclEngine` with `ocl.*`, so the two cannot drift apart.

```json
{
    ":configurator:resource-version": 1,
    "DefaultQvtdEngine": {
        "qvtd.blackboxEnabled": true,
        "qvtd.allowedBlackboxModules": ["my.trusted.Library"]
    },
    "DefaultOclEngine": {
        "ocl.maxDepth": 500
    }
}
```

**Available properties:**

| Property | Type | Default | Description |
|---|---|---|---|
| `qvtd.blackboxEnabled` | boolean | `false` | Whether transformations may call Java blackbox libraries — bound as `QvtdBlackboxLibrary` services, see §9.2 |
| `qvtd.allowedBlackboxModules` | String[] | *(empty)* | Module names (`getModuleName()`) of the libraries that may serve a query or an `implementedby`; **empty puts no restriction on the names** |
| `qvtd.unitResolverEnabled` | boolean | `false` | Whether transformations may import units resolved from outside |
| `qvtd.discoverUnitResolvers` | boolean | `false` | Whether resolvers registered as services are looked up by the name a transformation imports |
| `qvtd.allowedUnitModules` | String[] | *(empty)* | Qualified names that may be imported as a unit; **empty puts no restriction on the names** |
| `qvtd.maxBlackboxLibraries` | int | 10 | Blackbox libraries one engine will use |
| `qvtd.maxUnitResolvers` | int | 5 | Unit resolvers one engine will use |
| `qvtd.maxRelationDepth` | int | 200 | Recursion depth of relation invocation (§7.10) |
| `qvtd.maxBindings` | int | 10,000 | Variable bindings one relation may produce |
| `qvtd.timeout` | long | 30,000 | Transformation timeout in ms (0 = no timeout) |
| `qvtd.maxTraceRecords` | int | 1,000,000 | Trace records one run may hold, across all relations; reaching it is an error (`Maximum trace records exceeded: <limit>`) |
| `qvtd.deleteUnmatched` | boolean | true | Whether an enforce run deletes the target objects that no valid binding requires (§7.10.2) |

Blackboxes and unit resolvers are **off by default**: both let a transformation reach code and files outside itself, so switching them on is a decision.

The allow-lists narrow what may be reached once a feature is on. An **empty** allow-list does not narrow anything — enabling `blackboxEnabled` or `unitResolverEnabled` without also naming what may be reached permits every name. Name them.

The same settings are available to plain Java through `QvtdConfiguration.Builder` — see [§3.2](#32-configuration-options).

### 3.4.2 Imports

A unit may import another (§7.11.1):

```qvtr
import shared.Library;

transformation importer(source : bookshelf, target : bookshelf) {
}
```

An import makes the imported unit's names visible (§7.1.1, #360). Its relations do not run because of the import; they take part only through what the importing transformation asks for:

- **`extends`** names an imported transformation: its relations, keys and queries are inherited, as with a base in the same unit (§7.9).
- **A call** — qualified `shared.CopyTitle(b1, b2)` or unqualified `CopyTitle(b1, b2)` — runs that relation when it is called. It is copied into the importer as a non-top relation, so a `top relation` of the imported unit is not also run as a top relation of the importer.

```qvtr
import shared.Library;

transformation importer(source : bookshelf, target : bookshelf) {
    top relation Copy {
        checkonly domain source b1 : Book {};
        enforce domain target b2 : Book {};
        where { shared.CopyTitle(b1, b2); }
    }
}
```

The parser cannot know the imported names, so with an import present it leaves an unknown call, an unknown `extends` base and an `overrides` of an inherited relation open for the link phase. Whatever is still open after linking is an error: `Unknown relation or query 'shared.Nope': neither this transformation nor an imported one declares it`.

**Unquoted name or quoted URI.** An unquoted qualified name (`import shared.Library;`) names a compilation unit (§7.13.1), and the resolvers are asked for it. A quoted URI (§7.13.4) is a metamodel when the package registry knows it:

```qvtr
import UML : 'http://www.omg.org/spec/UML/20131001/UML.xmi';   -- alias, usable in model declarations
import 'http://example.org/outer'::inner;                    -- a subpackage, under its own name
import 'http://example.org/outer'::*;                        -- all names below the URI

transformation T(uml : UML, other : inner) { ... }
```

An alias and a wildcard cannot be used together (§7.1.1); a subpackage the metamodel lacks is a positioned error. A quoted URI the registry does not know names a unit document, and the resolvers are asked for the URI as written. Loading metamodel *documents* (`.ecore`, `.emof`) from an import is not supported; register the package instead (§5.4).

Where the unit comes from is the resolvers' business. Configure them with `QvtdConfiguration.Builder.addUnitResolver(...)`, or let the engine find them:

```java
@Component(service = QvtdUnitResolver.class,
           property = "qvtd.unit.name=shared.Library")
public class SharedLibraryUnit implements QvtdUnitResolver {

    private final URI uri = URI.createURI("platform:/plugin/com.acme/Library.qvtr");
    private final String source = "...";  // the unit's QVT-R text

    @Override
    public Optional<QvtdUnit> resolveUnit(String qualifiedName) {
        return Optional.of(new QvtdUnit.SourceUnit(qualifiedName, uri, source));
    }
}
```

The engine looks that up when it links the import — not before. Outside OSGi the same happens through `ServiceLoader` and `discoverUnitResolvers(true)`.

**Both are off by default.** `unitResolverEnabled` alone leaves the allow-list empty, which puts no restriction on names, so discovery would let anything registered answer an import. Name what may be reached:

```json
{
    "DefaultQvtdEngine": {
        "qvtd.unitResolverEnabled": true,
        "qvtd.discoverUnitResolvers": true,
        "qvtd.allowedUnitModules": ["shared.Library"]
    }
}
```

An import that nothing answers for is an error — `Cannot resolve import: shared.Library` — rather than a silently missing relation. Imports are linked after parsing, so `parse()` does not report them:

- `compile()` throws `QvtdParseException`; `getErrors()` carries one diagnostic per failed import, at the line and column of its name, all of them in one exception (#264).
- `execute()` on a parsed transformation returns a result with `isSuccess() == false` and an `Execution error: Cannot resolve import: …` diagnostic.

**Running on a specific OCL engine:** the reference is unfiltered by default, so the engine binds whichever `OclEngine` service is there. To pin it to one you configured yourself, set a target filter:

```json
{
    "DefaultQvtdEngine": { "oclEngine.target": "(component.name=MyTunedOclEngine)" }
}
```

---

## 4. Model Extents

QVT-R uses **named** model extents, keyed by the typed model name declared in the transformation header.

### 4.1 Creating Extents

```java
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;

// Empty extent (for output)
QvtdModelExtent outExtent = QvtdModelExtent.of();

// Pre-populated extent (for input)
QvtdModelExtent inExtent = QvtdModelExtent.of(myRootObject);

// Multiple root objects
QvtdModelExtent inExtent = QvtdModelExtent.of(List.of(obj1, obj2, obj3));

// Using the concrete class directly
BasicQvtdModelExtent extent = new BasicQvtdModelExtent();
extent.add(myObject);
```

### 4.2 Named Extent Mapping

Extents are mapped to typed model names from the transformation declaration:

```java
// transformation T(uml : SimpleUML, rdbms : SimpleRDBMS)
//                  ^^^                ^^^^^
//                  typed model names

Map<String, QvtdModelExtent> extents = Map.of(
    "uml", QvtdModelExtent.of(myUmlPackage),     // matches "uml" typed model
    "rdbms", QvtdModelExtent.of()                 // matches "rdbms" typed model
);
```

### 4.3 Reading Output

After execution, read the transformation output from the target extent:

```java
List<EObject> outputRoots = rdbmsExtent.getContents();
EObject schema = outputRoots.get(0);
String name = (String) schema.eGet(schema.eClass().getEStructuralFeature("name"));
```

---

## 5. Parsing Transformations

### 5.1 From String

```java
RelationalTransformation trafo = engine.parse(
    "transformation T(src : PKG, tgt : PKG) { ... }",
    "T"  // unit name
);
```

A unit may declare several transformations (§7.13.7). `parse(source, unitName)` returns the one no other transformation of the unit extends — a base and its extension yield the extension. Where that leaves more than one, it throws rather than pick one; name it, or take them all (#360):

```java
RelationalTransformation u = engine.parse(source, "unit", "U");
List<RelationalTransformation> all = engine.parseAll(source, "unit");
```

A transformation name is a path name (`transformation a::b::T(...)`, `extends a::Base`); the name is its last segment.

### 5.2 From URI

```java
import org.eclipse.emf.common.util.URI;

RelationalTransformation trafo = engine.parse(
    URI.createFileURI("/path/to/MyTrafo.qvtr")
);
```

Callers rarely start with an EMF URI. `UriHelper` in the shared `org.eclipse.fennec.m2x` bundle converts the forms you are likely to hold — and it is the right way to turn a *path* into a URI:

```java
import org.eclipse.fennec.m2x.utils.UriHelper;

engine.parse(UriHelper.fromPath(Path.of("/srv/transforms/MyTrafo.qvtr")));
engine.parse(UriHelper.fromJavaUri(someJavaUri));
```

`URI.createURI(path.toString())` is the trap it avoids: a path is not a URI, and on Windows `C:\transforms\MyTrafo.qvtr` would end up with `C` as its scheme. `UriHelper.toJavaUri(uri)` converts back for APIs that speak `java.net.URI`.

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

Parse once and execute many times — the `RelationalTransformation` AST is reusable:

```java
// Parse once at startup
RelationalTransformation trafo = engine.parse(source, "MyTrafo");

// Execute repeatedly with different inputs
for (EObject input : inputs) {
    QvtdModelExtent src = QvtdModelExtent.of(input);
    QvtdModelExtent tgt = QvtdModelExtent.of();

    QvtdExecutionContext ctx = QvtdExecutionContext.enforce("tgt",
        Map.of("src", src, "tgt", tgt));
    QvtdExecutionResult result = engine.execute(trafo, ctx);
}
```

### 5.4 Metamodel Registration

Metamodel EPackages referenced in the transformation must be resolvable through the registry the configuration uses (§3.3) — the global `EPackage.Registry` by default — before parsing:

```java
// contribute the metamodels to this configuration only
QvtdConfiguration config = QvtdConfiguration.builder(oclConfig)
    .registerPackage(SimpleUMLPackage.eINSTANCE)
    .registerPackage(SimpleRDBMSPackage.eINSTANCE)
    .build();

// or, in plain Java, the global registry
EPackage.Registry.INSTANCE.put(SimpleUMLPackage.eNS_URI, SimpleUMLPackage.eINSTANCE);
```

---

### 5.5 Storable Units

`parse()` returns a graph that runs; `compile()` returns a document that can be stored, loaded
elsewhere and executed there:

```java
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;

CompiledUnit unit = engine.compile(source, "T");   // dependencies pinned by default

UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
UnitKey key = store.put(unit);

PreparedContext prepared = UnitPreparer.withDefaults(store, engine.unitBinder()).prepare(key);
QvtdExecutionResult result = engine.execute(prepared, "T", QvtdExecutionContext.enforce("target", extents));
```

QVT-R binds an `import` by copying what the importer takes from it — an `extends` base, the called
relations (§3.4.2) — so under `DependencyMode.EMBED` that happens at compile time and the unit runs
on an engine that knows no resolver at all. `QvtdStoreUnitResolver` resolves imports from a store.

The mechanism — the three dependency modes, the store, prepare, validation on load, fingerprints —
is described once for all languages in the [Compiled Units Guide](compiled-units-guide.md).

## 6. Executing Transformations

### 6.1 Enforce Mode

Enforce mode creates or updates objects in the target model to satisfy the relations:

```java
// transformation T(src : SRC, tgt : TGT)
QvtdExecutionContext ctx = QvtdExecutionContext.enforce(
    "tgt",                                        // target model name
    Map.of("src", srcExtent, "tgt", tgtExtent)    // named extents
);

QvtdExecutionResult result = engine.execute(trafo, ctx);
```

The engine iterates all **top-level relations**:
1. Evaluates the relation calls of the when-clause, binding their arguments from existing traces — no trace, no execution
2. Matches the source domain patterns against the source extents
3. For each match: checks the remaining when-predicates, enforces the target domain (find-or-create), records a trace entry, then evaluates the where-clause

Enforcing makes the target model hold the relations: it creates, modifies and **deletes** (§7.1.2). After all relations ran, a target object that matches an enforced target pattern but that no source binding requires is deleted — for example, a `Schema Z` without a `Package Z` (§7.2.3), or a stale `Table` in a schema that still holds. Running the transformation again after a change in the source therefore gives what a fresh run gives (§7.6). Objects outside the target model are never deleted, and neither check-only runs nor runs towards a `checkonly` domain delete anything. `QvtdConfiguration.Builder.deleteUnmatched(false)` keeps everything the target model held, e.g. objects a user added by hand.

Two relations that give the same single-valued property of the same object different values in one run clash (§7.10.2). This is an ERROR, and the first value stays. Values the object had before the run are modified, not a clash. A where predicate that reads only source variables and is false is an ERROR, and nothing is created for that binding.

### 6.2 Check-Only Mode

Check-only mode verifies whether all relations hold without modifying any model:

```java
QvtdExecutionContext ctx = QvtdExecutionContext.checkOnly(
    Map.of("src", srcExtent, "tgt", tgtExtent)
);

QvtdExecutionResult result = engine.execute(trafo, ctx);
if (result.isConsistent()) {
    // All relations hold between the models
}
```

Check-only mode checks every relation in every direction, regardless of whether a domain is marked `checkonly` or `enforce` (§7.9, §7.10.1). Each binding a relation does not hold for is reported as a WARNING with the code `QvtdExecutionResult.INCONSISTENCY`, for example:

```
Relation 'PackageToSchema' does not hold in direction 'rdbms': no match for p = Package 'B' (§7.10.1)
```

The run still succeeds (`isSuccess()` is true), but `isConsistent()` is false. Empty models are consistent and report nothing.

An enforce run in the direction of a domain that is only `checkonly` checks that domain and reports what does not hold, without creating anything (§7.2.3). Apart from that, an enforce run reports no inconsistencies. To check every relation in every direction after enforcing, use `withPostCheck()`:

```java
engine.execute(trafo, QvtdExecutionContext.enforce("tgt", extents).withPostCheck());
```

### 6.3 Bidirectional Execution

The same transformation can be executed in either direction. The **target model name** determines the enforcement direction:

```java
// Forward: src → tgt
QvtdExecutionContext forward = QvtdExecutionContext.enforce("tgt",
    Map.of("src", srcExtent, "tgt", tgtExtent));

// Backward: tgt → src
QvtdExecutionContext backward = QvtdExecutionContext.enforce("src",
    Map.of("src", srcExtent, "tgt", tgtExtent));
```

Domains marked `enforce` in the QVT-R source become target domains when their typed model matches the target model name. Domains marked `checkonly` are always source domains.

### 6.4 In-Place Transformations

When the same typed model appears in both source and target domains, the transformation modifies elements in place. A key identifies the object to update. Without a key, nothing identifies it, and the enforced domain describes a new object (§7.4):

```qvtr
transformation MarkPersistent(m : MyModel) {
    key Class { name };
    top relation MarkPersistent {
        cn : String;
        checkonly domain m c1 : Class { name = cn };
        enforce domain m c2 : Class { name = cn, kind = 'Persistent' };
    }
}
```

The relation is idempotent: a second run changes nothing. A relation that derives a new name from the old one (`name = oldName.concat('_v2')`) would grow the model on every run, because each run matches its own output again.

An in-place run works as if source and target were distinct models (§7.7). Source domains match a copy of the model as it was before the run, and enforcing changes the model itself. A relation therefore never matches objects that the same run created or changed.

`QvtdExecutionResult.modelChanged()` reports whether a run created, modified or deleted anything in the target model. This lets you repeat a transformation until it reaches a fixpoint:

```java
QvtdExecutionResult result;
do {
    result = engine.execute(trafo, ctx);
} while (result.isSuccess() && result.modelChanged());
```

```java
QvtdModelExtent extent = QvtdModelExtent.of(myModelRoot);
QvtdExecutionContext ctx = QvtdExecutionContext.enforce("m",
    Map.of("m", extent));
engine.execute(trafo, ctx);
```

---

## 7. QVT-R Language Features

### 7.1 Transformation Declaration

```qvtr
transformation Uml2Rdbms(uml : SimpleUML, rdbms : SimpleRDBMS) {
    -- keys first, then relations and queries in any order
}
```

The typed model names (`uml`, `rdbms`) are used as extent keys in `QvtdExecutionContext`.

### 7.2 Relations

Relations define correspondences between model elements:

```qvtr
-- Top-level relation: executed automatically for all matching instances
top relation PackageToSchema {
    pn : String;                                    -- shared variable
    checkonly domain uml p : Package { name = pn }; -- source pattern
    enforce domain rdbms s : Schema { name = pn };  -- target pattern
}

-- Non-top relation: invoked from a where-clause
relation AttributeToColumn {
    an : String;
    checkonly domain uml a : Attribute { name = an };
    enforce domain rdbms c : Column { name = an };
}
```

- **Initializer:** a variable may have one, as in `n : String = 'X';`. It is a fixed value that constrains the match: `checkonly domain uml p : Package { name = n }` then only matches the package `X`. In `a, b : String = 'x';` every variable gets the value.
- **Several templates in one domain:** `checkonly domain uml p : Package {…}, c : Class {…};` binds both roots jointly, giving one binding per combination.
- **Template variables:** a template variable is the relation's variable of that name. `p : Package;` and a nested `owningPackage = p : Package {}` are the same variable. A declared type that the template's class cannot be is a parse error.

### 7.3 When and Where Clauses

```qvtr
top relation ClassToTable {
    cn : String;
    p : Package;
    s : Schema;
    checkonly domain uml c : Class { owningPackage = p : Package {}, name = cn };
    enforce domain rdbms t : Table { schema = s : Schema {}, name = cn };

    -- when: precondition, checked before enforcing
    when {
        PackageToSchema(p, s);   -- requires a trace from PackageToSchema
        cn <> 'Abstract';         -- OCL predicate
    }

    -- where: evaluated after enforcement
    where {
        AttributeToColumn(c, t);  -- invokes a non-top relation
    }
}

relation AttributeToColumn {
    an : String;
    checkonly domain uml c : Class { ownedAttribute = a : Attribute { name = an } };
    enforce domain rdbms t : Table { ownedColumn = col : Column { name = an } };
}
```

Every variable a relation uses is declared — by a variable declaration, a domain, or a template binding (§8.2).

- **When-clause** `RelationCallExp`: checks if a trace record exists for the called relation. The arguments are **bound from the trace**.
- **Where-clause** `RelationCallExp`: invokes the called relation with the given arguments as pre-bindings.
- **Template as predicate** (§7.13.5, #361): `when { c : Class { owningPackage = p }; }` holds when some object of the run's models matches the template under the current bindings. A bound root variable is the only candidate; what the template binds besides stays inside the predicate. In a where clause a template without a match makes the relation not hold, an ERROR — it is checked, nothing is created for it. Only object templates; a collection template as predicate is a parse error. Eclipse does not support this form.

### 7.4 Object Templates

Object templates define patterns for matching and creating model elements (a fragment — `cn` and `an` are declared in the enclosing relation):

```qvtr
enforce domain rdbms t : Table {
    name = cn,                      -- bind/set property
    ownedColumn = col : Column {    -- nested template
        name = an,
        type = 'VARCHAR'            -- literal value
    }
};
```

### 7.5 Collection Templates

Collection templates match and create collection-valued properties:

```qvtr
-- c : Class; rest : Set(Class); declared in the relation
checkonly domain uml p : Package {
    ownedClass = cs : Set(Class) { c ++ rest }  -- c = one member, rest = the remaining members
};
```

The variable before the `:` binds the whole collection and may be omitted (`ownedClass = : Set(Class) { … }`); members and the rest must be declared variables or `_`.

- **Distinct elements:** the members match distinct elements, so `{ c1, c2 ++ _ }` needs two. Each `_` member also takes an element.
- **No rest:** `{ c1, c2 }` matches only a collection of exactly two elements.
- **Sequence and OrderedSet:** for a `Sequence(…)` or `OrderedSet(…)` template, the members are the head of the sequence, in order.
- **Set and OrderedSet:** for a `Set(…)` or `OrderedSet(…)` template, the property's values must be unique.
- **Template condition:** `{ c ++ _ } { cs->size() > 1 }` matches only when the condition holds.
- **Single-valued properties:** a collection template on a single-valued property matches the value as a one-element collection, and an unset property as an empty one.
- **Allowed kinds (§7.11.2.4):** a set property allows only a `Set` template, and an ordered set or a sequence allows `Set`, `Sequence` or `OrderedSet`. Any other kind is a parse error.
- **Not a root:** a collection template cannot be the root of a domain; that is a parse error.

### 7.6 Keys

Keys define identity properties for find-or-create semantics during enforcement:

```qvtr
key Schema { name };
key Table { schema, name };
```

When enforcing a target domain, the engine first tries to find an existing object matching the key properties. If no match is found, a new object is created. Without a key, nothing identifies an existing object, and a new one is created; the old one is deleted if no binding requires it.

- **Several keys:** a class may have several keys (§7.4). An object is found when one of them matches completely.
- **Superclass keys:** a key of a superclass applies to its subclasses. The keys of the most specific class take precedence.
- **Object values:** a key part whose value is an object template (`schema = s : Schema {}`) takes the object that template binds or enforces.
- **Opposite parts:** `key Attribute { name, opposite(Class::ownedAttribute) }` identifies an attribute by its name and the class that holds it.
- **Errors:** a key of an unknown class or property is a parse error.

### 7.7 Queries

Queries are reusable helper functions:

```qvtr
query sqlType(oclType : String) : String {
    if oclType = 'Integer' then 'NUMBER'
    else if oclType = 'String' then 'VARCHAR'
    else 'BLOB'
    endif endif
}
```

### 7.8 Abstract Relations and Overrides

```qvtr
top abstract relation BaseMapping {          -- abstract: not executed directly
    pn : String;
    checkonly domain uml p : Package { name = pn };
    enforce domain rdbms s : Schema { name = pn };
}

top relation ConcreteMapping overrides BaseMapping {   -- replaces BaseMapping where it applies
    pn : String;
    checkonly domain uml p : Package { name = pn };
    enforce domain rdbms s : Schema { name = pn.toUpper() };
}
```

The modifiers come in this order: `top`, `abstract`, `relation`. Every relation has at least one domain, abstract ones included.

**One domain:** a relation with a single domain is a model predicate, for example `top relation Marked { checkonly domain uml p : Package { name = 'X' }; }`. The normative grammar allows it, §7.2 speaks of two or more domains. Its traces can be used in other relations' `when` clauses, also negated: `when { not Marked(p); }`.

**Relation calls:** a relation call is a Boolean expression and may stand anywhere in `when` and `where`, as in `not R(p, s)` or `R(p, s) or …`. It may also be qualified with the transformation's own name: `T.R(p, s)`.

**Anonymous roots:** a domain root without a name (`checkonly domain uml : Package {…}`) gets an implicit variable.

An overriding relation replaces the overridden one only for the bindings where its `when` clause and the patterns of its non-target domains match (§7.11.1.4).

For every other binding, the overridden relation runs. A call from a `where` clause reaches the overriding relation in the same way. An abstract relation never runs itself: a call to it runs the overriding relation that applies, and if none applies, the call is an ERROR naming the relation and the binding. A relation may override one it inherits through `extends`. Overriding a name that is no relation of the transformation is a parse error.

### 7.9 Transformation Extends

Schematically — `{ ... }` stands for a complete relation body:

```qvtr
transformation Base(src : SRC, tgt : TGT) {
    top relation R1 { ... }
}

transformation Extended(src : SRC, tgt : TGT) extends Base {
    -- inherits R1, can override it
    top relation R2 { ... }
}
```

The base may also come from an imported unit (§3.4.2).

### 7.10 Primitive Domains

Primitive domains bind a single typed variable without a pattern:

```qvtr
top relation WithPrefix {
    primitive domain prefix : String;
    checkonly domain uml p : Package { name = prefix };
    enforce domain rdbms s : Schema { name = prefix };
}
```

### 7.11 Default Values

Default value assignments give a *variable* of the relation a value when nothing else binds it while the target domain is enforced (§7.11.3.7, `RelationDomainAssignment.variable`). They do not assign properties:

```qvtr
tt : String;
...
enforce domain rdbms t : Table { name = cn, type = tt }
    default_values {
        tt = 'TABLE';
    };
```

### 7.12 Opposite Properties

Access the opposite end of a reference in templates:

```qvtr
checkonly domain uml a : Attribute {
    opposite(Class::ownedAttribute) = c
};
```

`opposite()` also works for a property without an eOpposite, the case §7.3 introduces it for. The owner is then the object of class `Class` in the same model whose `ownedAttribute` holds the attribute. Enforcing adds the attribute to the owner's property.

### 7.13 Optional Root Variable `[?]`

Eclipse extension — marks a domain root as optional. If no match is found, the root variable is bound to `null` instead of failing:

```qvtr
checkonly domain uml p : Package [?] { name = pn };
-- If no Package matches, p = null (vacuous match)
```

The relation holds for the null binding: it records a trace, so a relation whose when clause calls it runs. This applies to top and non-top relations alike.

---

## 8. Binding Validation

The parser performs a **static analysis** (§7.5 "Restrictions on Expressions") to ensure all variable references in a relation can be organized into a valid binding order. This catches errors at parse time rather than at runtime.

### 8.1 What Is Validated

For each relation, the validator checks:

1. **When-clause**: All variable references (except `RelationCallExp` arguments, which are bound from trace) must be bound by an initialized declaration, the when clause or a domain.

2. **Each domain as potential target**: Since the target domain depends on execution direction, the validator verifies that for each domain D, all non-binding variable references in D's template expressions are satisfiable from external sources (when-clause + other domains + variable declarations).

3. **Where-clause**: All variable references must be bound by the when clause, domain templates, initialized declarations, or a where assignment `x = expr` to a declared variable.

A plain declaration (`x : String;`) binds nothing: a variable that nothing binds is an error (#359). Only a declaration with an initializer (`x : String = 'a';`) binds its variable.

**Per direction** (§7.5 rule 2.1, #359): which domain is the target depends on the direction, so the parser accepts a variable that only the target domain binds. An enforce run checks its direction before anything runs. A variable that nothing but the enforced domain binds, in the target domain, the where clause or the when clause, is an ERROR naming the variable and the relation, and nothing is executed. Such a relation is executable in the other direction only.

4. **Local scopes**: Variables introduced by OCL `let`, `->select()`, `->collect()`, and other iterator expressions are correctly scoped and not flagged.

### 8.2 Binding Sites

A binding site binds a variable the relation **declares** — by a variable declaration, a domain root, or a template's `bindsTo` (§7.13.5). Mentioning a name declares nothing. Declared variables can be bound by:

| Binding site | Example | Mechanism |
|-------------|---------|-----------|
| Template `bindsTo` | `p : Package { ... }` | Pattern matching binds `p` |
| Property template (simple variable) | `name = pn` | §7.5 Rule 1.1: binds `pn` |
| Collection template members | `{ c ++ rest }` | Binds `c` and `rest` (both must be declared) |
| Primitive domain root | `primitive domain x : String` | Framework binds `x` |
| Variable declaration with initializer | `pn : String = 'X';` | Binds `pn` to the value |
| Where assignment | `where { tn = cn.concat('_t'); }` | Binds the declared `tn` before enforcing |
| When-clause `RelationCallExp` | `when { R(a, b); }` | `a`, `b` bound from trace (both must be declared) |

### 8.3 What Triggers an Error

A `QvtdParseException` is thrown when:

- A variable is referenced in a **complex expression** but never bound anywhere:
  ```qvtr
  enforce domain rdbms s : Schema {
      name = pn.concat(unknownVar)  -- ERROR: unknownVar is never bound
  };
  ```

- A variable is used in a **where-clause** but not bound by any domain or when-clause:
  ```qvtr
  where { ghostVar = 'test'; }  -- ERROR: ghostVar is never bound
  ```

- A name at a **binding site** — property template value, collection member or rest, when-clause relation call argument — is declared nowhere (§7.13.5). Before this check such a name ran as `null`: the enforced property was written as `null` and the run reported success.
  ```qvtr
  enforce domain rdbms s : Schema { name = tlsPort };  -- ERROR: tlsPort is not declared
  when { PackageToSchema(p, s); }                      -- ERROR unless p and s are declared
  ```
  A declaration inside a comment declares nothing — comments are never parsed or executed.

### 8.4 What Is NOT an Error

A **simple `VariableExp`** as a `PropertyTemplateItem` value is a potential binding site (§7.5 Rule 1.1), not a usage — a declared variable that no other part of the relation binds yet is bound here:

```qvtr
newVar : String;
checkonly domain uml p : Package { name = newVar };
-- NOT an error: the declared newVar is bound by pattern matching (Rule 1.1)
```

A variable bound by a nested template is declared by that template, as in the specification's `ClassToTable`:

```qvtr
checkonly domain uml c : Class { namespace = p : Package {} };
when { PackageToSchema(p, s); }  -- p is declared by the nested template, s needs `s : Schema;`
```

### 8.5 Error Message Format

```
QVT-R binding validation error in 'MyTrafo':
  §7.5: Variable 'unknownVar' in domain 'rdbms' of relation 'R' is not bound
```

An undeclared name at a binding site is reported at the position where it is written:

```
QVT-R binding validation error in 'MyTrafo':
  §7.13.5: Variable 'tlsPort' in domain 'rdbms' of relation 'R' is not declared — neither in
  the relation's variable declarations nor bound by a domain or template
```

Each diagnostic includes the spec reference, the variable name, the domain/clause context, and the relation name.

---

## 9. Blackbox Libraries

Call Java code from QVT-R transformations: as a blackbox query — a `query` without a body — or through the `implementedby` clause of a domain (§7.8).

### 9.1 Implement a Library

```java
import java.util.List;

import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxLibrary;

public class MyBlackbox implements QvtdBlackboxLibrary {

    @Override
    public String getModuleName() {
        return "MyBlackbox";  // what qvtd.allowedBlackboxModules names
    }

    @Override
    public String getUnitQualifiedName() {
        return "my.lib.MyBlackbox";
    }

    @Override
    public List<String> getUsedPackageURIs() {
        return List.of();
    }

    @Override
    public List<String> getOperationNames() {
        return List.of("TypeMap");  // the operations this library serves
    }

    @Override
    public Object invoke(String operationName, Object self, Object[] args) {
        return switch (operationName) {
            case "TypeMap" -> "INTEGER".equals(args[0]) ? "NUMBER" : "VARCHAR";
            default -> throw new IllegalArgumentException(
                "Unknown operation: " + operationName);
        };
    }
}
```

QVT-R does not import a library by name. The engine picks the library that lists the operation in `getOperationNames()`, if the allow-list permits its `getModuleName()` and the run is still under `maxBlackboxLibraries` — a ceiling on the distinct libraries one run uses.

### 9.2 Register the Library

Blackboxes are off by default. A registered library is unreachable until `blackboxEnabled` is on, and then only for the module names the allow-list permits; an empty allow-list puts no restriction on the names.

**Standalone:**

```java
import java.util.Set;

import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;

BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
registry.register(new MyBlackbox());

QvtdConfiguration config = QvtdConfiguration.builder(oclConfig)
    .blackboxRegistry(registry)
    .blackboxEnabled(true)                           // required: off by default
    .allowedBlackboxModules(Set.of("MyBlackbox"))    // recommended
    .build();
```

Both calls are needed: without a registry there is nothing to call (the default is none), without `blackboxEnabled(true)` the registry is not consulted.

**OSGi:** publish the library as a `QvtdBlackboxLibrary` service. `DefaultQvtdEngine` binds every such service through a dynamic reference of cardinality `0..n` named `blackboxes`, and drops it when it goes away — without restarting the engine. Each run uses the libraries bound when it starts.

```java
@Component(service = QvtdBlackboxLibrary.class)
public class MyBlackbox implements QvtdBlackboxLibrary {
    // ... as in 9.1
}
```

```json
"DefaultQvtdEngine": {
    "qvtd.blackboxEnabled": true,
    "qvtd.allowedBlackboxModules": ["MyBlackbox"]
}
```

| Property | Effect |
|----------|--------|
| `blackboxes.target` | LDAP filter on the library services this engine binds, e.g. `(blackbox.group=reporting)` |
| `blackboxes.cardinality.minimum` | The engine is not available until this many libraries are bound — the way to make a deployment wait for its libraries instead of running without them |

Where two services have the same `getUnitQualifiedName()`, the one with the higher `service.ranking` is used; among equal rankings, the one registered first.

### 9.3 Using in QVT-R

**Blackbox query** — declared without a body, served by the library that lists its name:

```qvtr
transformation T(uml : simpleuml, rdbms : simplerdbms) {
    query TypeMap(umlType : String) : String;

    top relation AttributeToColumn {
        an : String; atype : String; sqltype : String;
        checkonly domain uml c : Class {
            ownedAttribute = a : Attribute { name = an, type = atype }
        };
        enforce domain rdbms t : Table {
            ownedColumn = col : Column { name = an, type = sqltype }
        };
        where { sqltype = TypeMap(atype); }
    }
}
```

The engine calls `invoke("TypeMap", null, new Object[]{atype})`. A blackbox query that no library serves — blackboxes off, no library lists it, or the allow-list excludes it — is an error naming the query and the reason (`Blackbox query 'TypeMap' cannot be served: …`), reported once per run; the call evaluates to `null`.

**`implementedby`** — delegates the enforcement of a domain to an operation:

```qvtr
enforce domain rdbms s : Schema { name = pn }
    implementedby CreateSchema(p);
```

When `rdbms` is the target, the engine first checks whether the relation already holds; only if it does not does it call `invoke("CreateSchema", null, new Object[]{p})` (§7.11.3.6). The arguments are the root objects of the other domains (§10, rule 4.5). Only the last segment of the name selects the operation (`MyBlackbox::CreateSchema` selects `CreateSchema` as well). A returned `EObject` that has no container is added to the target extent; any other return value is ignored.

The operation is responsible for making the relation hold. Afterwards the engine checks again, and a relation that still does not hold is an error — "a runtime exception" in the words of the specification. An `implementedby` operation that neither a provider (§10) nor a library serves is an error as well; the engine does not enforce the domain in its place.

The specification restricts the relations an operation may implement (§7.8): their domains *should* be primitive or a simple object template without sub-elements, and their when and where clauses *should* not define variables. The conditions belong in a "wrapper" relation that calls the simple one — as in the specification's own example:

```qvtr
relation RExpToMExp {
    domain relations re : OclExpression {};
    enforce domain core me : OclExpression {} implementedby CopyOclExpession(re, me);
}
```

A relation with an `implementedby` clause that goes beyond this runs, but the result carries one warning per run, positioned at the clause:

```
<unit>:<line>:<column> Relation 'ClassToTable': implemented by an operation, but domain 'rdbms' is neither primitive nor a simple object template without sub-elements (§7.8) — keep it simple and constrain it from a wrapper relation that calls it
```

---

## 10. Hybrid QVT-R / QVT-O

Fennec supports hybrid execution where QVT-R relations delegate their `implementedby` clause to a QVT-O transformation engine (§7.8, D39).

### 10.1 Architecture

```
QvtdEngine (Relations)
    │
    ├── evaluates top relations
    ├── matches source domains
    └── for an implementedby clause, when the relation does not hold:
            │
            ├── 1. a RelationImplementationProvider that serves the operation (QVT-O)
            ├── 2. otherwise a blackbox library that serves it (Java)
            ├── 3. otherwise an error
            └── afterwards: the relation must hold, or it is an error
```

### 10.2 Registering a QVT-O Provider

Every `QvtoEngine` is a `RelationImplementationProvider`. Load the transformation whose mappings implement relations and register the engine with the QVT-R engine:

```java
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;

// Create the QVT-O engine and load the transformation that implements relations
QvtoEngine qvtoEngine = QvtoEngines.create(qvtoConfig);
OperationalTransformation ot = qvtoEngine.parse("""
    modeltype SRC uses 'http://test/simpleuml/1.0';
    modeltype TGT uses 'http://test/simplerdbms/1.0';
    transformation QvtoImpl(in uml : SRC, out rdbms : TGT);
    mapping Class::classToTableOp() : Table {
        name := self.name;
        type := 'QVTO';
    }
    """, "QvtoImpl");
qvtoEngine.loadTransformation(ot);

// Register it with the QVT-R engine
QvtdEngine qvtdEngine = QvtdEngines.create(qvtdConfig);
qvtdEngine.registerImplementationProvider(qvtoEngine);

// The relation names the mapping and passes the root object of its other domain
RelationalTransformation trafo = qvtdEngine.parse("""
    transformation T(uml : simpleuml, rdbms : simplerdbms) {
        top relation ClassToTable {
            cn : String;
            checkonly domain uml c : Class { name = cn };
            enforce domain rdbms t : Table { name = cn }
                implementedby classToTableOp(c);
        }
    }
    """, "T");
QvtdExecutionResult result = qvtdEngine.execute(trafo, ctx);
```

Compiling against `qvto.api` therefore needs `qvtd.api` on the class path.

### 10.3 How It Works

When a relation with an `implementedby` clause for the enforced direction does not hold (§7.11.3.6):

1. The engine evaluates the clause's arguments — the root objects of the other domains (§10, rule 4.5).
2. It asks the registered providers, in registration order, `canProvide(transformationName, operationName)` with the name of the running relational transformation and the name the clause gives. A QVT-O engine answers for a mapping of that name in a loaded transformation that refines this relational transformation, or that says nothing about whom it refines (§10.4).
3. The first provider that serves it gets `invoke(transformationName, operationName, arguments, context)`. The QVT-O engine runs that one mapping — not the transformation's `main()` — with the first argument as its context object if the mapping has one, and with its model parameters bound by name to the QVT-R run's extents, so what it creates lands in the QVT-R target. The result's diagnostics join the run's; an error among them fails it.
4. If no provider serves the operation, a blackbox library may (§9); if none does, that is an error.
5. The relation is checked again; if it still does not hold, that is an error.

### 10.4 QVT-O `refines` Clause

In QVT-O, a transformation can declare that it refines a relational transformation (§8.2.1.1):

```qvto
transformation Uml2RdbmsImpl(in uml : SimpleUML, out rdbms : SimpleRDBMS)
    refines Uml2Rdbms;

mapping uml::Package::packageToSchema() : rdbms::Schema
    refines PackageToSchema {
    name := self.name;
}
```

The parser supports both `::` (spec §8.4.7) and `.` (Eclipse-compatible) separators in the `refines` module reference.

The transformation's `refines` decides which relational transformation its mappings serve: `refines Uml2Rdbms` serves the `implementedby` clauses of `Uml2Rdbms` and of no other transformation. A transformation without `refines` serves any (#286). A mapping's `refines` records which relation it implements; which mapping an `implementedby` clause calls is decided by the name the clause gives.

### 10.5 Under OSGi

Under OSGi the QVT-O side is a service of its own, and the QVT-R engine binds it (#286).

**1. Publish the QVT-O unit** as a `QvtoUnitResolver` service with its unit name, and mark it as a relation implementation:

```java
@Component(property = {
    "qvto.unit.name=my.company.Uml2RdbmsImpl",
    "qvto.relation.implementation=true" })
public class Uml2RdbmsImplUnit implements QvtoUnitResolver {
    @Override
    public Optional<QvtoUnit> resolveUnit(String qualifiedName) {
        try (InputStream in = getClass().getResourceAsStream("/transforms/Uml2RdbmsImpl.qvto")) {
            String source = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            return Optional.of(new QvtoUnit.SourceUnit(qualifiedName,
                    URI.createURI("bundle:/transforms/Uml2RdbmsImpl.qvto"), source));
        } catch (IOException e) {
            throw new UncheckedIOException(e); // a unit that cannot be read is no "not found"
        }
    }
}
```

Which relational transformation the unit serves is said by the unit itself, with `refines` (§10.4). The property only says that it is an implementation.

**2. The provider.** The component `QvtoRelationImplementations` (in `org.eclipse.fennec.m2x.qvto.engine`) binds every unit so marked and is registered as `RelationImplementationProvider`. It loads the units into an engine instance of its own when they are first needed after they changed, so units may come and go at run time. A unit that cannot be resolved or parsed is not served. Without configuration there is one provider for all marked units. A factory configuration of the same name makes more, each narrowed with `units.target`:

```json
"QvtoRelationImplementations~uml": {
    "units.target": "(&(qvto.relation.implementation=true)(qvto.unit.name=my.company.uml.*))",
    "provider.group": "uml"
}
```

**3. Enable it on the QVT-R engine.** `DefaultQvtdEngine` binds `RelationImplementationProvider` services through a dynamic `0..n` reference, but it uses them only when this is switched on, as with blackboxes (§9.2):

```json
"DefaultQvtdEngine": {
    "qvtd.implementationProvidersEnabled": true,
    "implementationProviders.target": "(provider.group=uml)",
    "implementationProviders.cardinality.minimum": 1
}
```

A provider runs code on the QVT-R run's models, so the switch is off by default. If it is off and providers are bound, a run with an `implementedby` clause says so in a WARNING. Several providers are asked in the order of their `service.ranking`, highest first. A provider registered with `registerImplementationProvider(...)` comes before them and needs no switch.

---

## 11. Error Handling

### 11.1 Parse Errors

`QvtdParseException` is thrown for syntax errors and §7.5 binding violations:

```java
try {
    engine.parse(source, "MyTrafo");
} catch (QvtdParseException e) {
    System.err.println(e.getMessage());
    for (Resource.Diagnostic error : e.getErrors()) {
        System.err.printf("  Line %d, Col %d: %s%n",
            error.getLine(), error.getColumn(), error.getMessage());
    }
}
```

**Positions are real for both kinds now** (#110). A syntax error carries what ANTLR reported; an unresolved name, unknown type or unknown metamodel carries the place it stands. `0:0` still means "position unknown", which is what a diagnostic reported without any parse context gets.

Parse errors include:
- **Syntax errors** — malformed QVT-R source (line/column from ANTLR4)
- **Resolution errors** — unknown metamodels, types and properties
- **Binding violations** — §7.5 / §7.13.5 variable problems (detected post-parse, §8)

Unresolvable imports are not parse errors: they surface at `compile()` or `execute()` (§3.4.2).

A syntax error that expects a name says `identifier`, not the list of every word that may stand as one.

**Reserved words** (§7.13.2): `checkonly`, `domain`, `enforce`, `extends`, `implementedby`, `import`, `key`, `overrides`, `primitive`, `query`, `relation`, `top`, `transformation`, `when`, `where` cannot be names; `_'key'` escapes one. `abstract`, `opposite` and `default_values` are not on the list and can be names. The deprecated escape `_key` still means `key`; as the parse has no channel for warnings, a run of the transformation reports a WARNING at its place: `T:4:42: '_key' is a deprecated escape of 'key'; write _'key' (§7.13.2)`.

### 11.2 Execution Diagnostics

`QvtdExecutionResult` contains diagnostics collected during execution:

```java
QvtdExecutionResult result = engine.execute(trafo, ctx);

if (!result.isSuccess()) {
    for (Diagnostic d : result.diagnostics()) {
        System.err.printf("[%s] %s%n",
            d.getSeverity() == Diagnostic.ERROR ? "ERROR" : "WARN",
            d.getMessage());
    }
}
```

### 11.3 Common Error Scenarios

| Error | When | Cause |
|-------|------|-------|
| `QvtdParseException` | Parse / compile time | Syntax, resolution or binding error; at `compile()` also an unresolvable import |
| `QvtdExecutionException` | Runtime (unchecked) | A limit was exceeded: relation or query call depth, bindings, timeout (§3.2) |
| `IllegalArgumentException` | Creating the context | The target model name of `enforce(...)` is not among the extents |
| `isSuccess() == false` | Runtime | Relation enforcement failed, OCL evaluation error |
| `isConsistent() == false` | Check-only, enforce towards a `checkonly` domain, `withPostCheck()` | A relation does not hold: `INCONSISTENCY` warnings name relation, direction and binding |

---

## 12. Tracing

The engine maintains implicit trace records (§7.2.1) for each executed relation.

### 12.1 How Traces Work

- Each successful relation execution records a `TraceRecord(relation, bindings)`
- `RelationCallExp` in **when-clauses** looks up existing trace records. Per §7.2.3 every matching trace gives a binding of the arguments.

Top relations run in the order of their when dependencies, not in the order they are declared: a relation runs after the relations its when clause calls. Relations whose when clauses call each other run until they add no trace (§7.2.2).

A relation leaves a trace only for a binding it holds for. If enforcing the target fails with an ERROR, no trace is recorded, and no relation whose when clause depends on that binding runs for it.
- `RelationCallExp` in **where-clauses** invokes non-top relations with the given arguments as pre-bindings

### 12.2 Trace-Driven When-Clauses

```qvtr
top relation PackageToSchema {
    pn : String;
    checkonly domain uml p : Package { name = pn };
    enforce domain rdbms s : Schema { name = pn };
}

top relation ClassToTable {
    cn : String;
    p : Package;
    s : Schema;
    checkonly domain uml c : Class { name = cn };
    enforce domain rdbms t : Table { name = cn };
    when {
        -- Looks up trace: was PackageToSchema executed for (p, s)?
        -- If yes, p and s are bound from the trace record.
        PackageToSchema(p, s);
    }
}
```

### 12.3 Current Limitations

The trace is **implicit and internal** — there is no public API to read trace records after execution. The trace model is an implementation detail of the engine (D38). Future phases may expose trace access for debugging and incremental execution.

---

## Appendix A: Spec Compliance

| Feature | Spec Reference | Status |
|---------|---------------|--------|
| Top relations | §7.10.1, §7.2.2 | Fully implemented — dependency order, cycles to a fixpoint |
| Non-top relations | §7.10.2 | Fully implemented |
| When/where clauses | §7.2.1 | Fully implemented |
| Checkonly domains | §7.9, §7.10.1, §7.2.3 | Fully implemented |
| Enforce domains | §7.10.2 | Fully implemented — create, modify, delete; clash detection |
| Binding validation | §7.5 | Implemented (static analysis) — every declared variable counts as bound (#359) |
| Key-based identity | §7.4, §7.13.4 | Fully implemented |
| Object templates | §7.11.2 | Fully implemented |
| Collection templates | §7.11.2.3, §7.11.2.4 | Fully implemented (as domain root: parse error) |
| Primitive domains | §7.13.4 | Fully implemented |
| Abstract relations | §7.11.1.4 | Fully implemented — conditional overrides, also for where calls |
| Transformation extends | §7.11.1.1 | Fully implemented |
| Blackbox / implementedby | §7.8 | Fully implemented |
| In-place transforms | §7.7 | Fully implemented, with the `modelChanged()` status |
| Optional `[?]` | Eclipse ext. | Fully implemented |
| Default values | §7.11.3.7 | Fully implemented |
| Opposite properties | §7.11.2 | Fully implemented |
| Standard Library | §7.12 | Fully implemented (= OCL stdlib) |
| Multi-file import | §7.13.1 | Implemented — `import` resolves through the configured unit resolvers (§3.4.2); it also merges the imported unit's relations, which the spec does not (#360) |
| Change propagation | §7.6 | Re-execution propagates changes, deletion included; incremental execution deferred (GAP-13 → Phase 5) |
