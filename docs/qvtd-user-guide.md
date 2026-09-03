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
| `org.eclipse.fennec.m2x.qvtd.engine` | Direct interpretation engine |
| `org.eclipse.fennec.m2x.qvtd.model` | QVT-R EMF metamodel (3 EPackages) |
| `org.eclipse.fennec.m2x.ocl.*` | Required OCL bundles (transitive) |

---

## 2. Quick Start

Minimal example — parse a QVT-R transformation from a String and execute it:

```java
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngine;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;

// 1. Create engine
OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
QvtdConfiguration config = QvtdConfiguration.builder(oclConfig).build();
QvtdEngine engine = QvtdEngines.create(config);

// 2. Parse transformation
RelationalTransformation trafo = engine.parse("""
    transformation Uml2Rdbms(uml : SimpleUML, rdbms : SimpleRDBMS) {
        top relation PackageToSchema {
            pn : String;
            checkonly domain uml p : Package { name = pn };
            enforce domain rdbms s : Schema { name = pn };
        }
    }
    """, "Uml2Rdbms");

// 3. Set up named model extents
QvtdModelExtent umlExtent = QvtdModelExtent.of(myPackage);
QvtdModelExtent rdbmsExtent = QvtdModelExtent.of();  // empty output

// 4. Execute in enforce mode (target = rdbms)
QvtdExecutionContext ctx = QvtdExecutionContext.enforce("rdbms",
    Map.of("uml", umlExtent, "rdbms", rdbmsExtent));
QvtdExecutionResult result = engine.execute(trafo, ctx);

// 5. Read output
if (result.isSuccess()) {
    List<EObject> schemas = rdbmsExtent.getContents();
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
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;

// OCL configuration (shared with QVT-R)
OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport())
    .build();

// QVT-R configuration
QvtdConfiguration config = QvtdConfiguration.builder(oclConfig)
    .blackboxRegistry(new BasicQvtdBlackboxRegistry())
    .build();

QvtdEngine engine = QvtdEngines.create(config);
```

### 3.2 Configuration Options

`QvtdConfiguration.Builder` methods:

| Method | Default | Description |
|--------|---------|-------------|
| `resourceSet(resourceSet)` | — | Resource set whose package registry resolves the transformation's typed models |
| `packageRegistry(registry)` | `EPackage.Registry.INSTANCE` | The registry itself; wins over `resourceSet` when both are set |
| `blackboxRegistry(registry)` | empty | Registry for blackbox Java libraries |
| `blackboxEnabled(boolean)` | `false` | Enable blackbox library imports |
| `allowedBlackboxModules(Set)` | empty | Allow-list for blackbox module names |
| `addUnitResolver(resolver)` | — | Add a unit resolver for multi-file imports |
| `unitResolverEnabled(boolean)` | `false` | Enable unit resolver imports |
| `maxBlackboxLibraries(int)` | 10 | Maximum registered blackbox libraries |
| `maxUnitResolvers(int)` | 5 | Maximum registered unit resolvers |

### 3.3 Which Metamodels the Engine Sees

The typed models of a transformation — `transformation t(uml : simpleuml, rdbms : simplerdbms)` — are resolved by package name when the transformation is parsed. By default the engine looks in `EPackage.Registry.INSTANCE`, which is the right answer in plain Java. Hand it your own registry when you hold the packages yourself:

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
| `DefaultOclExpressionCacheComponent` | SINGLETON | The parsed-expression cache, shared by all OCL engines |
| `ResourceSet` (from `emf.osgi`) | PROTOTYPE | Nothing — bound `prototype_required`, so each engine resolves metamodels through its own resource set |

The engine takes its OCL engine as a **mandatory** service reference. That is door 2 from [§3.1](#31-three-ways-to-give-qvt-r-its-ocl-engine): the OCL engine a consumer configured — with its cache and its operation providers — is the one that evaluates relation expressions, rather than a second one built beside it.

The **resource set** is a mandatory reference as well, and deliberately not an optional one with a fallback to the global registry. Under `emf.osgi` a metamodel registered for the resource set (`emf.model.scope=resourceset`) lives in the resource set's package registry and never in the global one — an engine built on the fallback would resolve a typed model such as `uml : simpleuml` to the wrong package instance and match nothing, silently. And an optional reference cannot say *when* the service arrives, only that it was not there at activation. Mandatory means: the engine exists once the resource set does, and what §3.3 describes for plain Java happens by itself. Where several resource sets are published, pick one:

```json
{
    "DefaultQvtdEngine": { "resourceSet.target": "(emf.name=myapp)" }
}
```

The fingerprint service `emf.osgi` provides is bound the same way, mandatory, for the same reason.

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
| `qvtd.blackboxEnabled` | boolean | `false` | Whether transformations may call Java blackbox libraries |
| `qvtd.allowedBlackboxModules` | String[] | *(empty)* | Qualified names that may be imported as a blackbox; **empty puts no restriction on the names** |
| `qvtd.unitResolverEnabled` | boolean | `false` | Whether transformations may import units resolved from outside |
| `qvtd.discoverUnitResolvers` | boolean | `false` | Whether resolvers registered as services are looked up by the name a transformation imports |
| `qvtd.allowedUnitModules` | String[] | *(empty)* | Qualified names that may be imported as a unit; **empty puts no restriction on the names** |
| `qvtd.maxBlackboxLibraries` | int | 10 | Blackbox libraries one engine will use |
| `qvtd.maxUnitResolvers` | int | 5 | Unit resolvers one engine will use |
| `qvtd.maxRelationDepth` | int | 200 | Recursion depth of relation invocation (§7.10) |
| `qvtd.maxBindings` | int | 10,000 | Variable bindings one relation may produce |
| `qvtd.timeout` | long | 0 | Transformation timeout in ms (0 = no timeout) |
| `qvtd.maxTraceRecords` | int | 100,000 | Trace records one transformation may create |

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

The imported unit's relations become available to the importing transformation, the way `extends` works. What the importing transformation declares itself wins, so an import cannot quietly replace a relation.

Where the unit comes from is the resolvers' business. Configure them with `QvtdConfiguration.Builder.addUnitResolver(...)`, or let the engine find them:

```java
@Component(service = QvtdUnitResolver.class,
           property = "qvtd.unit.name=shared.Library")
public class SharedLibraryUnit implements QvtdUnitResolver {

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

An import that nothing answers for is an error — `Cannot resolve import: shared.Library` — rather than a silently missing relation.

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

Metamodel EPackages referenced in the transformation must be registered in the global `EPackage.Registry` before parsing:

```java
// Register metamodels
EPackage.Registry.INSTANCE.put(SimpleUMLPackage.eNS_URI, SimpleUMLPackage.eINSTANCE);
EPackage.Registry.INSTANCE.put(SimpleRDBMSPackage.eNS_URI, SimpleRDBMSPackage.eINSTANCE);

// Now parse — the parser resolves types from registered packages
RelationalTransformation trafo = engine.parse(source, "MyTrafo");
```

---

### 5.3 Storable Units

`parse()` returns a graph that runs; `compile()` returns a document that can be stored, loaded
elsewhere and executed there:

```java
CompiledUnit unit = engine.compile(source, "T");   // dependencies pinned by default

UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
UnitKey key = store.put(unit);

PreparedContext prepared = UnitPreparer.withDefaults(store, engine.unitBinder()).prepare(key);
QvtdExecutionResult result = engine.execute(prepared, "T", QvtdExecutionContext.enforce("target", extents));
```

QVT-R binds an `import` by merging the imported relations (§7.11.1.1), so under
`DependencyMode.EMBED` that merge happens at compile time and the unit runs on an engine that knows
no resolver at all. `QvtdStoreUnitResolver` resolves imports from a store.

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
1. Matches source domain patterns against the source extent
2. For each match, enforces the target domain (find-or-create)
3. Evaluates when/where clauses
4. Records trace entries

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

When the same typed model appears in both source and target domains, the transformation modifies elements in place:

```qvtr
transformation Refactor(m : MyModel) {
    top relation RenameClass {
        oldName : String;
        checkonly domain m c1 : Class { name = oldName };
        enforce domain m c2 : Class { name = oldName.concat('_v2') };
    }
}
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
    -- relations, keys, queries go here
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

### 7.3 When and Where Clauses

```qvtr
top relation ClassToTable {
    cn : String;
    checkonly domain uml c : Class { name = cn };
    enforce domain rdbms t : Table { name = cn };

    -- when: precondition (checked before matching)
    when {
        PackageToSchema(p, s);  -- requires trace from PackageToSchema
    }

    -- where: postcondition (evaluated after enforcement)
    where {
        AttributeToColumn(a, col);  -- invokes non-top relation
        cn <> 'Abstract';           -- OCL predicate
    }
}
```

- **When-clause** `RelationCallExp`: checks if a trace record exists for the called relation. The arguments are **bound from the trace**.
- **Where-clause** `RelationCallExp`: invokes the called relation with the given arguments as pre-bindings.

### 7.4 Object Templates

Object templates define patterns for matching and creating model elements:

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
checkonly domain uml p : Package {
    ownedClass = Set(Class) { c ++ rest }  -- c = first member, rest = remainder
};
```

### 7.6 Keys

Keys define identity properties for find-or-create semantics during enforcement:

```qvtr
key Schema { name };
key Table { schema, name };
```

When enforcing a target domain, the engine first tries to find an existing object matching the key properties. If no match is found, a new object is created.

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
abstract relation BaseMapping {
    -- abstract: not executed directly
}

top relation ConcreteMapping overrides BaseMapping {
    -- overrides: replaces BaseMapping in execution
}
```

### 7.9 Transformation Extends

```qvtr
transformation Base(src : SRC, tgt : TGT) {
    top relation R1 { ... }
}

transformation Extended(src : SRC, tgt : TGT) extends Base {
    -- inherits R1, can override it
    top relation R2 { ... }
}
```

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

Default value assignments set properties when the target domain is enforced:

```qvtr
enforce domain rdbms t : Table { name = cn }
    default_values {
        isTemporary = false;
    };
```

### 7.12 Opposite Properties

Access the opposite end of a reference in templates:

```qvtr
checkonly domain uml a : Attribute {
    opposite(Class::ownedAttribute) = c
};
```

### 7.13 Optional Root Variable `[?]`

Eclipse extension — marks a domain root as optional. If no match is found, the root variable is bound to `null` instead of failing:

```qvtr
checkonly domain uml p : Package [?] { name = pn };
-- If no Package matches, p = null (vacuous match)
```

---

## 8. Binding Validation

The parser performs a **static analysis** (§7.5 "Restrictions on Expressions") to ensure all variable references in a relation can be organized into a valid binding order. This catches errors at parse time rather than at runtime.

### 8.1 What Is Validated

For each relation, the validator checks:

1. **When-clause**: All variable references (except `RelationCallExp` arguments, which are bound from trace) must be resolvable from explicit variable declarations.

2. **Each domain as potential target**: Since the target domain depends on execution direction, the validator verifies that for each domain D, all non-binding variable references in D's template expressions are satisfiable from external sources (when-clause + other domains + variable declarations).

3. **Where-clause**: All variable references must be bound by the when-clause, domain templates, or variable declarations.

4. **Local scopes**: Variables introduced by OCL `let`, `->select()`, `->collect()`, and other iterator expressions are correctly scoped and not flagged.

### 8.2 Binding Sites

Variables can be bound by:

| Binding site | Example | Mechanism |
|-------------|---------|-----------|
| Template `bindsTo` | `p : Package { ... }` | Pattern matching binds `p` |
| Property template (simple variable) | `name = pn` | §7.5 Rule 1.1: binds `pn` |
| Collection template members | `{ c ++ rest }` | Binds `c` and `rest` |
| Primitive domain root | `primitive domain x : String` | Framework binds `x` |
| Variable declaration | `pn : String;` | Explicit declaration |
| When-clause `RelationCallExp` | `when { R(a, b); }` | `a`, `b` bound from trace |

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

### 8.4 What Is NOT an Error

A **simple `VariableExp`** as a `PropertyTemplateItem` value is a potential binding site (§7.5 Rule 1.1), not a usage:

```qvtr
checkonly domain uml p : Package { name = newVar };
-- NOT an error: newVar is bound by pattern matching (Rule 1.1)
```

### 8.5 Error Message Format

```
QVT-R binding validation error in 'MyTrafo':
  §7.5: Variable 'unknownVar' in domain 'rdbms' of relation 'R' is not bound
```

Each diagnostic includes the §7.5 reference, the variable name, the domain/clause context, and the relation name.

---

## 9. Blackbox Libraries

Call Java code from QVT-R transformations via the `implementedby` clause (§7.8).

### 9.1 Implement a Library

```java
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxLibrary;

public class MyBlackbox implements QvtdBlackboxLibrary {

    @Override
    public String getModuleName() {
        return "MyBlackbox";
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
    public Object invoke(String operationName, Object self, Object[] args) {
        return switch (operationName) {
            case "computeName" -> {
                EObject pkg = (EObject) args[0];
                yield pkg.eGet(pkg.eClass().getEStructuralFeature("name"));
            }
            default -> throw new IllegalArgumentException(
                "Unknown operation: " + operationName);
        };
    }
}
```

### 9.2 Register the Library

```java
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdBlackboxRegistry;

BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
registry.register(new MyBlackbox());

QvtdConfiguration config = QvtdConfiguration.builder(oclConfig)
    .blackboxRegistry(registry)
    .blackboxEnabled(true)
    .build();
```

### 9.3 Using in QVT-R

```qvtr
top relation PackageToSchema {
    pn : String;
    checkonly domain uml p : Package { name = pn };
    enforce domain rdbms s : Schema { name = pn }
        implementedby MyBlackbox::computeName(p);
}
```

The `implementedby` clause delegates enforcement of the target domain to the blackbox library. The engine calls `invoke("computeName", null, new Object[]{p})`.

---

## 10. Hybrid QVT-R / QVT-O

Fennec supports hybrid execution where QVT-R relations delegate their `implementedby` clause to a QVT-O transformation engine (§7.8, D39).

### 10.1 Architecture

```
QvtdEngine (Relations)
    │
    ├── evaluates top relations
    ├── matches source domains
    └── for implementedby clauses:
            │
            ├── 1. Try RelationImplementationProviders (QVT-O)
            └── 2. Fallback to BlackboxRegistry (Java)
```

### 10.2 Registering a QVT-O Provider

`QvtoEngine` implements `RelationImplementationProvider`. Register it with the QVT-R engine:

```java
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngine;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngine;

// Create QVT-O engine
QvtoEngine qvtoEngine = QvtoEngines.create(qvtoConfig);

// Load the operational transformation that implements relations
OperationalTransformation ot = qvtoEngine.parse(qvtoSource, "MyImpl");
qvtoEngine.loadTransformation(ot);

// Register as provider with the QVT-R engine
QvtdEngine qvtdEngine = QvtdEngines.create(qvtdConfig);
qvtdEngine.registerImplementationProvider(qvtoEngine);

// Now execute — implementedby clauses try the QVT-O engine first
QvtdExecutionResult result = qvtdEngine.execute(trafo, ctx);
```

### 10.3 How It Works

When the QVT-R engine encounters an `implementedby` clause:

1. It iterates all registered `RelationImplementationProvider` instances
2. For each provider, it calls `canProvide(relationName)` — checks if the provider's loaded transformation has a `MappingOperation` with that name
3. If a provider matches, it calls `executeRelation(name, context)` — the provider bridges the QVT-R execution context to QVT-O and executes the mapping
4. If no provider matches, it falls back to the `QvtdBlackboxRegistry`

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
- **Binding violations** — §7.5 unresolved variable references (detected post-parse)

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
| `QvtdParseException` | Parse time | Syntax error or §7.5 binding violation |
| `QvtdExecutionException` | Runtime | Fatal error (stack overflow, missing extent) |
| `isSuccess() == false` | Runtime | Relation enforcement failed, OCL evaluation error |
| `isConsistent() == false` | Check-only | Relations do not hold between the models |

---

## 12. Tracing

The engine maintains implicit trace records (§7.2.1) for each executed relation.

### 12.1 How Traces Work

- Each successful relation execution records a `TraceRecord(relation, bindings)`
- `RelationCallExp` in **when-clauses** looks up existing trace records — the arguments are bound from matching traces
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
| Top relations | §7.10.1 | Fully implemented |
| Non-top relations | §7.10.2 | Fully implemented |
| When/where clauses | §7.2.1 | Fully implemented |
| Checkonly domains | §7.10 | Fully implemented |
| Enforce domains | §7.10.2 | Fully implemented |
| Binding validation | §7.5 | Fully implemented (static analysis) |
| Key-based identity | §7.4, §7.13.4 | Fully implemented |
| Object templates | §7.11.2 | Fully implemented |
| Collection templates | §7.11.2 | Fully implemented |
| Primitive domains | §7.11.3.10 | Fully implemented |
| Abstract relations | §7.11.3.2 | Fully implemented |
| Transformation extends | §7.11.1.1 | Fully implemented |
| Blackbox / implementedby | §7.8 | Fully implemented |
| In-place transforms | §7.7 | Fully implemented |
| Optional `[?]` | Eclipse ext. | Fully implemented |
| Default values | §7.11.3 | Fully implemented |
| Opposite properties | §7.11.2 | Fully implemented |
| Standard Library | §7.12 | Fully implemented (= OCL stdlib) |
| Multi-file import | §7.3 | Implemented — `import` resolves through the configured unit resolvers (§3.4.2) |
| Change propagation | §7.6 | Deferred (GAP-13 → Phase 5) |
