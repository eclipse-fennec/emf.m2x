# Compiled Units — User Guide

A **compiled unit** is a transformation or template that has been parsed, resolved and packaged
into one document: it can be stored, loaded somewhere else and executed there, with nothing left to
resolve. This guide is the one place the mechanism is described; it works the same way for QVT-O,
QVT-R and MOFM2T, and every example below is a shape the test suites execute.

## Table of Contents

1. [Why](#1-why)
2. [The three phases](#2-the-three-phases)
3. [`compile()` beside `parse()`](#3-compile-beside-parse)
4. [Dependency modes: embed, pin, rebind](#4-dependency-modes-embed-pin-rebind)
5. [The unit store](#5-the-unit-store)
6. [Prepare and execute](#6-prepare-and-execute)
7. [Several sources for one name](#7-several-sources-for-one-name)
8. [Validation on load](#8-validation-on-load)
9. [Fingerprints](#9-fingerprints)
10. [What this means for OCL](#10-what-this-means-for-ocl)
11. [Bundles and API index](#11-bundles-and-api-index)

---

## 1. Why

`parse()` gives you an object graph that runs. It does **not** give you something you can store:
a parser creates objects the tree points at but that no metamodel feature contains — the variable
`self`, a type instance for every `Integer`, the default expression of an intermediate class. In
memory that costs nothing. `resource.save()` fails on exactly those objects with a
`DanglingHREFException`, and `EcoreUtil.copy` produces a copy whose references still point at the
original's objects.

A compiled unit gives them a home beside the script, records what the unit was built against, and
carries its dependencies — as much of them as you ask for. It is what makes these work:

- keep a parsed transformation in a repository and run it on another machine, another day;
- run a pipeline where one transformation's output is the next one's input, without the two
  disagreeing about what a `Book` is;
- know whether the thing you are about to run is still the thing you tested.

---

## 2. The three phases

| Phase | What happens | Resolvers asked |
|---|---|---|
| **Compile** | parse → resolve the imports → package | yes |
| **Prepare** | load the unit and its closure, verify the metamodels, bind | yes |
| **Execute** | run it | **never** |

The rule in the third column is the point of the whole mechanism, and it is asserted by a test
rather than by convention: the engine that executes a prepared unit is configured with a resolver
that throws if it is ever asked.

---

## 3. `compile()` beside `parse()`

Every engine has both. `parse()` returns the language's root object, `compile()` returns a
`CompiledUnit` document whose `getUnit()` is that very object.

**QVT-O**

```java
QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(oclConfig).build());

OperationalTransformation ast = engine.parse(source, "Main");   // in-memory, not storable
CompiledUnit compiled = engine.compile(source, "Main");         // storable document

// the script inside is the same object parse() would have returned
OperationalTransformation script = (OperationalTransformation) compiled.getUnit();
engine.execute(script, QvtoExecutionContext.of(extent));
```

**QVT-R**

```java
QvtdEngine engine = QvtdEngines.create(QvtdConfiguration.builder(oclConfig).build());
CompiledUnit compiled = engine.compile(source, "T");
RelationalTransformation script = (RelationalTransformation) compiled.getUnit();
```

**MOFM2T**

```java
M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(oclConfig).build());
CompiledUnit compiled = engine.compile(templateSource, "my.module");
Module module = (Module) compiled.getUnit();
```

What the document holds:

```java
compiled.getId();            // a UUID, unique across all compiled units
compiled.getNamespace();     // grouping for versioning, yours to set
compiled.getVersion();       // what someone claims this is
compiled.getUnit();          // the script — an EPackage in all three languages
compiled.getSatellite();     // the parser's leftovers, so the document is self-contained
compiled.getEmbedded();      // dependencies carried inside, under `embed`
compiled.getPackages();      // copies of dynamic metamodels (see §6)
compiled.getManifest();      // what it was built from and against
```

The manifest is the interesting half:

```java
CompiledUnitManifest m = compiled.getManifest();
m.getLanguage();             // "qvto", "qvtr", "m2t"
m.getQualifiedName();        // the name it is imported by
m.getUnitFingerprint();      // m2x1:… — what this unit is
m.getSourceFingerprint();    // m2x1:… — the text it was compiled from
m.getDependencyMode();       // how imports were bound
m.getDependencyEntry();      // the units it needs
m.getPackageEntry();         // the metamodels it was compiled against, with fp1 fingerprints
m.getBlackboxRequirement();  // the blackbox implementations the runtime has to bring
```

A `compile()` that cannot make the document self-contained fails with the language's parse
exception, naming the unit — not with a dangling reference on someone else's `save()` later.

---

## 4. Dependency modes: embed, pin, rebind

An import is a dependency. What `compile()` does with it is the mode, and the mode is recorded in
the manifest so that prepare can read how the unit was built:

| Mode | The dependency | Reproducible | Fixes arrive | Needs a resolver to run |
|---|---|---|---|---|
| `embed` | compiled in turn and carried inside the unit | absolutely | never | no |
| `pin` (default) | named in the manifest with its unit fingerprint | while that version is findable | on recompile | yes |
| `rebind` | named in the manifest | no | automatically | yes |

```java
CompiledUnit archived  = engine.compile(source, "Main", UnitCompileOptions.of(DependencyMode.EMBED));
CompiledUnit pinned    = engine.compile(source, "Main");                                  // pin
CompiledUnit following = engine.compile(source, "Main", UnitCompileOptions.of(DependencyMode.REBIND));
```

An import nobody can resolve fails the compile in **every** mode — `Cannot resolve import: X`. A
cycle is reported under `embed` and `pin`, where the compiler follows the dependency; under
`rebind` the closure is prepare's business.

### What each language binds

**QVT-O** — `import`/`access` of a module. Under `embed` the dependency becomes an entry of
`getEmbedded()` and the import points into it; under `pin`/`rebind` the parser's stub stays and is
bound later.

```java
QvtoUnitResolver lib = name -> "HelperLib".equals(name)
        ? Optional.of(new QvtoUnit.SourceUnit(name, URI.createURI("mem:/HelperLib.qvto"), LIB_SOURCE))
        : Optional.empty();
QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(oclConfig)
        .addUnitResolver(lib).unitResolverEnabled(true).build());

CompiledUnit unit = engine.compile("""
        modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
        import HelperLib;
        transformation Main(inout m : ECORE) { main() { log(greet()); } }
        """, "Main", UnitCompileOptions.of(DependencyMode.EMBED));

unit.getEmbedded().get(0).getManifest().getQualifiedName();   // "HelperLib"
// runs on an engine that knows no resolver at all — also after a store round trip
bare.execute((OperationalTransformation) unit.getUnit(), QvtoExecutionContext.of(extent));
```

**QVT-R** — `import` of a transformation. QVT-R binds an import by *merging* the imported relations
into the importing transformation (§7.11.1.1), so under `embed` the merge happens at compile time
and the import is struck; there is no embedded child, because nothing would point at it. The
manifest entry names what was merged.

```java
CompiledUnit unit = engine.compile("""
        import shared.Library;
        transformation importer(source : bookshelf, target : bookshelf) { }
        """, "importer", UnitCompileOptions.of(DependencyMode.EMBED));

((RelationalTransformation) unit.getUnit()).getRule();        // the merged relations
unit.getManifest().getDependencyEntry().get(0);               // shared.Library, embed, fingerprint
```

**MOFM2T** — `extends` and `import` of a module. Under `embed` the dependency is carried and the
module is linked against it; under `pin`/`rebind` the module stays unbound and keeps what it still
has to bind on itself, so an engine that never parsed it can link it after a reload.

```java
CompiledUnit unit = engine.compile("""
        [module main(_'http://www.eclipse.org/emf/2002/Ecore') extends base/]
        [template public main(c : EClass)][file ('out.txt', false)][greet(c)/][/file][/template]
        """, "main", UnitCompileOptions.of(DependencyMode.EMBED));

((Module) unit.getUnit()).getExtends();                        // bound, inside the document
```

### Blackboxes are requirements, not dependencies

A blackbox implementation is never carried — its declaration is. The manifest records the name and
a fingerprint over the signatures the unit was compiled against, so a runtime that brings the right
name with the wrong shape is caught when the unit is prepared, not at the call.

```java
compiled.getManifest().getBlackboxRequirement().get(0).getName();                  // "mylib"
compiled.getManifest().getBlackboxRequirement().get(0).getSignatureFingerprint();  // m2x1:…
```

MOFM2T has no blackboxes: its Java services arrive through the OCL operation providers of the
engine, so a compiled module never carries a requirement.

---

## 5. The unit store

`UnitStore` holds sources and compiled units, keyed by language, qualified name, kind and
fingerprint. `DefaultUnitStore` implements it over a `UnitStoreBackend` — bytes by key, keys by
name. The in-memory backend needs nothing:

```java
UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());

// a compiled unit goes in as a document; the key carries its fingerprint
UnitKey key = store.store("qvto", new PackagedUnit(engine.compile(source, "Main")));

// … and comes back as an independent copy
PackagedUnit loaded = (PackagedUnit) store.load(key).orElseThrow();
engine.execute((OperationalTransformation) loaded.document().getUnit(), context);
```

A source is stored the same way, under its source fingerprint:

```java
store.store("qvto", new QvtoUnit.SourceUnit("HelperLib", URI.createURI("file:/lib.qvto"), text));
```

Both kinds live side by side under one name and are told apart by kind:

```java
store.versions("qvto", "HelperLib", UnitKind.SOURCE);     // every stored version, newest first
store.versions("qvto", "HelperLib", UnitKind.COMPILED);
store.load(UnitKey.of("qvto", "HelperLib", UnitKind.COMPILED));                    // the newest
store.load(UnitKey.pinned("qvto", "HelperLib", UnitKind.COMPILED, "m2x1:9f86…"));  // exactly that one
```

Three properties worth knowing:

- **Only documents.** A bare AST has no manifest to be stored by — `compile()` first. Handing one
  in is a `UnitStoreException` saying so.
- **Copies both ways.** What you store is copied on the way in, what you load is a fresh copy: a
  later write cannot change what an earlier caller holds.
- **A missing version is not "not found".** A pinned key the store does not have is an error that
  names the versions it does have. `Optional.empty()` means the name is unknown, nothing else.

### Resolving from a store

Each language has a resolver that reads from a store — compiled unit first, source second:

```java
QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(oclConfig)
        .addUnitResolver(new QvtoStoreUnitResolver(store))
        .unitResolverEnabled(true).build());
```

`QvtdStoreUnitResolver` and `M2tStoreUnitResolver` are the same for their languages.

---

## 6. Prepare and execute

Prepare turns stored units into something an engine can run without asking anybody anything. It
loads the unit **and its dependency closure** into one resource set, verifies the metamodels, and
lets the language bind what is left:

```java
UnitPreparer preparer = UnitPreparer.withDefaults(store, engine.unitBinder());
PreparedContext prepared = preparer.prepare(mainKey);

QvtoExecutionResult result = engine.execute(prepared, "Main", QvtoExecutionContext.of(extent));
```

`M2tEngine.execute(prepared, "my.module", M2tContext.of(input))` and
`QvtdEngine.execute(prepared, "T", context)` are the same for their languages. A context is built
once and reused across many executions.

**What prepare needs in the store.** Prepare loads documents by key — it does not parse. So every
dependency the manifest names under `pin` or `rebind` has to be in the store as a **compiled**
unit; a *source* under that name serves the compiler (through a store resolver, which parses it),
not prepare. Storing the library compiled is the step that is easy to forget:

```java
store.store("qvto", new PackagedUnit(engine.compile(libSource, "HelperLib")));  // prepare finds this
store.store("qvto", new QvtoUnit.SourceUnit("HelperLib", uri, libSource));     // the compiler finds this
```

Without it, prepare says so rather than guessing:

```
the store has no compiled unit 'HelperLib'
```

Under `embed` the question does not arise — the dependency travels inside the unit.

### Metamodels: the runtime instance wins, on equality

Every `PackageEntry` of every unit is checked against the runtime's package registry:

| Runtime has | Fingerprints | What the unit's types resolve to |
|---|---|---|
| an instance | equal | **the runtime instance** — generated code stays in play, blackboxes get generated types |
| nothing | — | the copy the unit carries (`getPackages()`) |
| an instance | **different** | hard failure at prepare, naming the nsURI and both values |

```
'Duplicate' was compiled against http://example.org/shelf/1.0 with fingerprint fp1:2b1c…,
the runtime provides fp1:9a04…
```

Two units of one run that pin different versions of one name are a conflict too — not a silent
choice.

### A pipeline shares one context

That is what the context is for: T1's output is T2's input, and both agree about the metamodel.

```java
PreparedContext prepared = preparer.prepare(renameKey, duplicateKey);

BasicQvtoModelExtent extent = new BasicQvtoModelExtent(books);
engine.execute(prepared, "Rename",    QvtoExecutionContext.of(extent));
engine.execute(prepared, "Duplicate", QvtoExecutionContext.of(extent));
```

A model loaded **in** the context carries the context's types, which is what an extent of
already-loaded objects cannot promise:

```java
List<EObject> roots = prepared.contents(URI.createFileURI("/models/shelf.xmi"));
engine.execute(prepared, "Rename", QvtoExecutionContext.of(new BasicQvtoModelExtent(roots)));
```

### Under rebind: what was actually bound

Under `rebind` the unit fingerprint says nothing about the dependencies, so prepare records what it
took, in the loaded copy's manifest:

```java
CompiledUnit ran = ((PackagedUnit) prepared.unit("Main").orElseThrow()).document();
ran.getManifest().getResolvedClosure().get(0).getQualifiedName();  // "HelperLib"
ran.getManifest().getResolvedClosure().get(0).getFingerprint();    // the version that ran
```

---

## 7. Several sources for one name

With one resolver nothing here matters. With several — a store, a file system, a bundle — three
rules apply, in one place (`ResolutionPolicy`), for compile and for the execute-time link alike:

1. **Every source is asked**, not only until the first answer. The first answer in source order is
   what is used: configured resolvers in configuration order, OSGi whiteboard services by
   `service.ranking` (highest first), class-path providers in declaration order.
2. **A failing source is an error.** A resolver that throws ends the resolution naming the source
   and the cause — `Cannot resolve import 'HelperLib': source …StoreUnitResolver failed …: disk on
   fire`. It is never turned into "not found", so a broken store cannot let a stale copy from the
   next source step in unnoticed.
3. **Answers have to agree.** Two sources with different content for one name are a conflict naming
   both. Comparable pairs: source ↔ source by source fingerprint, document ↔ document by unit
   fingerprint, source ↔ document by the document's `sourceFingerprint`.

**Ownership:** a unit a resolver hands out is *lent*. The receiver must not mutate it — the
compiler copies what it embeds, the linker binds inside the importing unit only. A resolver may
therefore hand out the same instance again and again.

---

## 8. Validation on load

A unit from a store bypasses the parser and every check it enforces. `DefaultUnitStore` validates
what it loads — on by default:

| Check | Catches |
|---|---|
| Integrity | the unit fingerprint recomputed and compared with the manifest |
| Manifest | format version, language and name, `pin` entries with fingerprint, embedded units of the same language, package copies with an entry |
| Closure | every reference that leaves the document points into a metamodel; no proxies |
| Ecore structure | the multiplicity and data-value rules of the languages' own features |
| Size | depth ≤ 1 000, objects ≤ 1 000 000 (configurable) |

```java
UnitStore checking = new DefaultUnitStore(backend);                        // validates
UnitStore trusting = DefaultUnitStore.withoutValidation(backend, packages); // for a trusted, hot backend
```

The language's half runs in prepare, before binding: the root is a unit of the language, every
variable use has its declaration in the document, QVT-R domains refer to declared typed models,
MOFM2T invocations are bound or recorded for binding.

**The fingerprint protects against drift, not against manipulation** — whoever can change a unit
can recompute its fingerprint. Authenticity is the backend's question: `UnitStoreBackend` stores
and returns bytes, so a backend that signs on `put` and verifies on `get` closes that gap without
any change to the store or the format. The shipped in-memory backend does not sign.

---

## 9. Fingerprints

Two kinds, two schemes, both in the manifest:

- **`fp1`** for **packages** — from emf.osgi's `FingerprintService`, the value the ecosystem keys
  models by (`@EPackage(fingerprint)`, the metadata service). This is what prepare compares.
- **`m2x1`** for **units** — SHA-256 over a canonical form of the AST. A reformatted source yields
  the same value; two transformations with the same intermediate classes and different logic do
  not. Source positions, comments and `documentation` annotations never enter; a metamodel
  reference enters as `nsURI#//Classifier/feature`, so where a metamodel was loaded from does not
  change the value, and a metamodel change does not cascade into every expression.

```java
compiled.getManifest().getUnitFingerprint();     // m2x1:9f86d081…
compiled.getManifest().getSourceFingerprint();   // m2x1:… over the LF-normalized text
compiled.getManifest().getPackageEntry().get(0).getFingerprint();   // fp1:2b1c…
```

Under `embed` and `pin` the unit fingerprint folds in the fingerprints of the dependencies, so the
same source compiled against a changed library has a different value. Under `rebind` it describes
the unit alone — which is why prepare records the closure.

The `m2x1` scheme is **frozen**: a change to the canonicalization needs a new tag, and a golden
value in the test suite says so.

---

## 10. What this means for OCL

OCL has no unit concept: an OCL expression is not something you import by name, so there is no
`OclEngine.compile()` and no OCL entry in a store. Two things still touch OCL:

- **OCL expressions are inside every unit.** The `m2x1` canonicalization walks them, so what the
  fingerprint says about a QVT-O mapping includes the OCL it contains. The standard-library types
  (`Integer`, `String`, `OclAny`) live in their own EPackage and enter as URIs, not as copies.
- **Property resolution.** Where an expression navigates to a property whose owner type is not
  known at parse time, the parser leaves a *marked* placeholder — the evaluator resolves it by name
  at runtime, which is what a template over a dynamic model needs, and the mark is what lets the
  fingerprint and the validator tell it from a resolved feature. A caller that hands the parser a
  whole document can ask for the typo instead:

```java
new OclParserSupport(registry).strictPropertyResolution(true).parse("self.nam", personClass);
// OclParseException: Unknown property (Person::nam)
```

---

## 11. Bundles and API index

Everything neutral is in **`org.eclipse.fennec.m2x.unit`**; the language APIs depend on it, never
the other way round. What needs a `BundleContext` lives beside it in
**`org.eclipse.fennec.m2x.unit.osgi`**, so that `unit` stays framework-neutral and every engine
runs as a plain Java library (D39).

| Package | What is in it |
|---|---|
| `…unit.api` | `Unit` (+`Source`, `Compiled`, `Packaged`), `UnitKey`, `UnitKind`, `UnitStore`, `UnitCompileOptions`, `UnitBinder`, `PreparedContext`, `UnitResourceSet`, the exceptions |
| `…unit.compile` | `UnitPackager`, `ReferencedPackages`, `SignatureFingerprint` |
| `…unit.store` | `DefaultUnitStore`, `UnitStoreBackend`, `InMemoryUnitStoreBackend`, `PackagedUnit`, `StoredSource` |
| `…unit.prepare` | `UnitPreparer` |
| `…unit.resolve` | `ResolutionPolicy`, `ServiceLoaderUnitResolver` (the class-path half of discovery) |
| `…unit.validate` | `UnitValidator` |
| `…unit.satellite` | `SatelliteCollector` |
| `…unit.fingerprint` | `DefaultUnitFingerprintService`, `AstCanonicalizer` |
| `…model.compiled` | the `CompiledUnit` metamodel (nsURI `http://www.eclipse.org/fennec/m2x/compiled/1.0`) |
| `…unit.osgi` | `OsgiServiceUnitResolver` — the whiteboard half of discovery, one for all three languages (in the separate `unit.osgi` bundle) |

Per language, in the engine bundle: `QvtoStoreUnitResolver`, `QvtdStoreUnitResolver`,
`M2tStoreUnitResolver`, the component and the class-path resolver that wrap the two shared
discovery implementations, and `engine.unitBinder()` for the preparer.

**Where the decisions are written down:** [QVT-O Architecture](qvto-architecture.md) §4.t–§4.z —
why type references stay non-containment, why satellites get a dedicated container, why packages
keep `fp1` while units get `m2x1`, and what the resolution order in a `UnitResourceSet` is.

---

*The engines run without a store and without OSGi, as plain Java libraries. Everything on this page
is optional: `parse()` and `execute()` are unchanged.*
