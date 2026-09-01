# QVT-O Implementation Architecture — Fennec M2M (Phase 2)

> Consolidated reference for the QVT-O implementation.

---

## 1. Overview & Status

The QVT-O engine implements **QVT-Operational v1.3** as a pure Java library,
fully decoupled from the Eclipse platform. It reuses the OCL engine (Phase 1) for
expression evaluation via composition (D25).

**Status:** Phase 2 complete. 941 QVT-O tests (0 failures, 0 @Disabled, 1 @Tag("perf")). Phase 0–10 spec-conformance complete, incl. blackbox libraries (§8.1.4), `from ... import` (§8.4), String Counter API (§8.3.16.31–35), and §8.4.4 shorthand operators (`%`, `#`, `##`, `*`, `!->`).

**Key properties:**
- Hybrid evaluator — QvtoEvaluator delegates OCL sub-expressions to OclEvaluator (D25)
- OSGi-optional — works as plain Java library
- 3-level switch dispatch: ImperativeOCL → QvtOperational → OCL
- ANTLR4-based parser importing Ocl.g4 grammar
- Lightweight trace records + EMF Trace export

---

## 2. Module Structure

```
workspace/
├── org.eclipse.fennec.m2x.qvto.model/    # QVT-O EMF metamodel (3 EPackages, 59 classifiers)
├── org.eclipse.fennec.m2x.qvto.api/     # Public API interfaces
├── org.eclipse.fennec.m2x.qvto.parser/  # ANTLR4 parser (imports Ocl.g4)
├── org.eclipse.fennec.m2x.qvto.engine/  # Imperative transformation engine
└── org.eclipse.fennec.m2x.qvto.tests/   # Tests (plain JUnit 5, 941 tests)
```

### 2.1 Dependency Graph

```
            ┌──────────┐
            │ocl.model  │
            └────┬─────┘
                 │
            ┌────┴─────┐
            │qvto.model │  ← extends OCL metamodel
            └────┬─────┘
                 │
            ┌────┴────┐
            │qvto.api  │  ← depends on ocl.api + qvto.model
            └────┬────┘
                 │
       ┌─────────┼─────────┐
       ▼                   ▼
┌───────────┐       ┌───────────┐
│qvto.parser│       │qvto.engine│  ← depends on ocl.engine (composition)
└───────────┘       └───────────┘
       │                   │
       └─────────┬─────────┘
                 ▼
          ┌───────────┐
          │qvto.tests │
          └───────────┘
```

**Key cross-component dependencies:**
- `qvto.model` depends on `ocl.model` (ImperativeOCL extends OCL expression types)
- `qvto.engine` depends on `ocl.engine` (delegates OCL expression evaluation)
- `qvto.parser` imports `Ocl.g4` grammar for expression parsing

---

## 3. QVT-O Metamodel (`qvto.model`)

**Bundle:** `org.eclipse.fennec.m2x.qvto.model`

Mixed project — EMF-generated code in `src-gen/`, hand-written code in `src/`.

| Property | Value |
|----------|-------|
| EPackages | 3 (`imperativeocl`, `qvtoperational`, `trace`) |
| Classifiers | 59 total (55 EClasses + 4 EEnums) |
| Design Decision | D22 — single bundle, 3 logical EPackages |

### 3.1 EPackage: `imperativeocl`

**nsURI:** `http://www.eclipse.org/fennec/m2x/qvto/imperativeocl/1.0`

Imperative expression extensions to the OCL model:

| Category | EClasses |
|----------|----------|
| Control flow | `BlockExp`, `ForExp`, `WhileExp`, `SwitchExp`, `AltExp` |
| Variables | `AssignExp`, `VariableInitExp`, `ComputeExp` |
| Control transfer | `ReturnExp`, `BreakExp`, `ContinueExp` |
| Exceptions | `TryExp`, `CatchExp`, `RaiseExp` |
| Debugging | `AssertExp`, `LogExp` |
| Iteration | `ImperativeIterateExp` |
| Instantiation | `InstantiationExp`, `TransformationInstantiationExp` |

### 3.2 EPackage: `qvtoperational`

**nsURI:** `http://www.eclipse.org/fennec/m2x/qvto/qvtoperational/1.0`

Transformation structure and QVT-O-specific constructs:

| Category | EClasses/EEnums |
|----------|-----------------|
| Module structure | `Module`, `OperationalTransformation`, `Library`, `ModuleImport` (+`importedNames`) |
| Operations | `ImperativeOperation`, `MappingOperation`, `Helper`, `Constructor`, `EntryOperation` |
| Blackbox | `BlackboxOperationDescriptor` (name, contextType, parameterTypes, returnType) |
| Parameters | `VarParameter`, `MappingParameter`, `ModelParameter` |
| Bodies | `MappingBody`, `OperationBody`, `ConstructorBody` |
| Expressions | `ObjectExp`, `MappingCallExp`, `ResolveExp`, `ResolveInExp` |
| Types | `ModelType`, `ContextualProperty` |
| Enums | `ImportKind`, `DirectionKind` |

### 3.3 EPackage: `trace`

**nsURI:** `http://www.eclipse.org/fennec/m2x/qvto/trace/1.0`

Trace model for recording transformation execution:

| EClass | Purpose |
|--------|---------|
| `Trace` | Root container for trace records |
| `TraceRecord` | Single mapping invocation record |
| `EMappingOperation` | Mapping reference in trace |
| `EMappingContext`, `EMappingParameters`, `EMappingResults` | Structured trace data |
| `EValue`, `VarParameterValue` | Typed parameter values |
| `EDirectionKind` | in/inout/out direction enum |

### 3.4 Design Choices (D22)

- **3 EPackages in one bundle:** The three metamodel areas are tightly coupled (QVTOperational references ImperativeOCL expressions, Trace references QVTOperational elements). Separate bundles would create circular dependencies.
- **`VarParameter extends EParameter` only:** EMF rejected dual inheritance from `EParameter` + `ocl::Variable` (conflicting `name` features). Solved with `representedParameter: Variable` reference.
- **`Module extends EPackage` only:** EMF codegen rejected `Module extends EClass, EPackage`. EPackage provides sufficient namespace semantics.
- **Skipped EClasses:** `DictLiteralExp`/`ListLiteralExp`/`DictionaryType`/`ListType` (D26: parser aliases), `OrderedTupleLiteralExp`/`OrderedTupleType` (Eclipse extension, not in spec).

---

## 4. API Design (`qvto.api`)

**Bundle:** `org.eclipse.fennec.m2x.qvto.api`

### 4.1 Core Interfaces

```java
@ProviderType
public interface QvtoEngine {
    OperationalTransformation parse(URI sourceUri) throws QvtoParseException;
    OperationalTransformation parse(String source, String unitName) throws QvtoParseException;
    QvtoExecutionResult execute(OperationalTransformation transformation,
                                QvtoExecutionContext context);
}
```

### 4.2 QvtoConfiguration

Immutable engine configuration bundling OCL config, blackbox registry, unit resolvers:

```java
QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
    .blackboxRegistry(registry)
    .addUnitResolver(myResolver)
    .packageRegistry(myPackageRegistry)   // optional, see below
    .build();
QvtoEngine engine = QvtoEngines.create(config);
```

`packageRegistry` resolves `modeltype … uses '<nsURI>'` declarations and everything below them. It defaults to `EPackage.Registry.INSTANCE`, and **this default is the single place in QVT-O where the static registry is applied** (D42) — parser, linker, evaluator, alias resolution and nested blackbox contexts all receive the resolved registry as a parameter.

### 4.3 QvtoExecutionContext

Bundles model extents + configuration properties for execution:

```java
public record QvtoExecutionContext(
    List<QvtoModelExtent> extents,
    Map<String, Object> configProperties
) { ... }
```

### 4.4 QvtoExecutionResult

```java
public record QvtoExecutionResult(
    List<Diagnostic> diagnostics,
    Trace trace    // Optional EMF trace model
) { ... }
```

### 4.5 QvtoModelExtent

Mutable model extent (unlike OCL's read-only extent):

```java
public interface QvtoModelExtent {
    List<EObject> getInitialObjects();
    List<EObject> getObjects();
    void addObject(EObject object);
    void removeObject(EObject object);
}
```

### 4.6 Extension Interfaces

```java
@ConsumerType
public interface QvtoBlackboxLibrary {          // D24 — see §4.7 for full details
    String getModuleName();
    String getUnitQualifiedName();
    List<String> getUsedPackageURIs();
    List<BlackboxOperationDescriptor> getOperationDescriptors();
    Object invoke(String operationName, QvtoBlackboxInvocationContext context, Object[] args);
}

@ConsumerType
public interface QvtoUnitResolver {             // D27
    Optional<QvtoUnit> resolveUnit(String qualifiedName);
}

public sealed interface QvtoUnit {              // Sealed union
    record SourceUnit(String qualifiedName, URI uri, String source) implements QvtoUnit {}
    record CompiledUnit(String qualifiedName, OperationalTransformation ot) implements QvtoUnit {}
}
```

**OSGi registration:** `@Reference(cardinality = MULTIPLE, policy = DYNAMIC)` whiteboard.
**Standalone:** Pass resolvers via `QvtoConfiguration.Builder.unitResolvers(List<QvtoUnitResolver>)`.

> **D29 Security:** Unit resolvers are **disabled by default**. Set `unitResolverEnabled(true)` in
> `QvtoConfiguration` to activate resolver lookups. Optionally restrict which modules may be
> resolved via `allowedUnitModules(Set<String>)`.

### 4.7 Blackbox Libraries (§8.1.4)

Blackbox libraries are the extension mechanism for calling native Java operations from QVT-O
transformations. They are registered programmatically via a `QvtoBlackboxRegistry` and resolved
at link-time into synthetic EMF modules.

#### 4.7.1 End-to-End Flow

```
QVT-O source:  import mylib;  ... trimAll('  hello  ')
                   │
                   ▼
Parser:        Creates stub Library (name="mylib", no classifiers)
                   │
                   ▼
Linker:        QvtoBlackboxRegistry.getLibrary("mylib")
               → Creates synthetic Library module with Helper EOperations
                   │
                   ▼
OpResolver:    findAllOperations("trimAll") finds synthetic Helper
                   │
                   ▼
OpProvider:    Detects isBlackbox flag → delegates to QvtoBlackboxLibrary.invoke()
                   │
                   ▼
Java impl:    invoke("trimAll", context, args) → returns result
```

#### 4.7.2 QvtoBlackboxLibrary Interface

```java
@ConsumerType
public interface QvtoBlackboxLibrary {
    String getModuleName();                          // Simple name for 'uses'
    String getUnitQualifiedName();                   // Qualified name for 'import'
    List<String> getUsedPackageURIs();               // EPackage nsURIs
    List<BlackboxOperationDescriptor> getOperationDescriptors();
    Object invoke(String operationName,
                  QvtoBlackboxInvocationContext context, Object[] args);
}
```

Operations can be **module-level** (no context type) or **contextual** (bound to an EClassifier,
called via `self.operation()`). The `invoke()` method receives the operation name, an invocation
context with the current evaluation state, and the arguments.

#### 4.7.3 BlackboxOperationDescriptor (EMF EClass)

Defined in `qvtoperational.ecore`, describes a single blackbox operation:

| Attribute | Type | Description |
|-----------|------|-------------|
| `name` | `EString` | Operation name |
| `contextType` | `EClassifier [0..1]` | Context type — `null` for module-level operations |
| `parameterTypes` | `EClassifier [0..*]` | Parameter types (positional) |
| `returnType` | `EClassifier [1..1]` | Return type (required) |

#### 4.7.4 QvtoBlackboxInvocationContext

Bridges blackbox operations to the current evaluation state:

```java
@ProviderType
public interface QvtoBlackboxInvocationContext {
    Object self();                         // Context object (null for module-level)
    void addInfo(String message);          // Informational diagnostic
    void addWarning(String message);       // Warning diagnostic
    void addError(String message);         // Error diagnostic
    Map<String, Object> getConfigProperties();
    QvtoModelExtent getExtent(String name);
    EPackage.Registry getPackageRegistry();
}
```

Implemented internally by `QvtoBlackboxInvocationContextImpl`, which delegates to
`QvtoEvaluator` for diagnostics and `QvtoExtentManager` for extent access.

#### 4.7.5 QvtoBlackboxRegistry

Lookup interface for blackbox libraries. A `QvtoConfiguration` holds at most one registry.

```java
@ProviderType
public interface QvtoBlackboxRegistry {
    Optional<QvtoBlackboxLibrary> getLibrary(String qualifiedName);
    List<QvtoBlackboxLibrary> getLibraries();
}
```

`BasicQvtoBlackboxRegistry` is the thread-safe standalone implementation using
`ConcurrentHashMap`. It provides `register(library)` and `unregister(library)` methods.

**Configuration:**

```java
BasicQvtoBlackboxRegistry registry = new BasicQvtoBlackboxRegistry();
registry.register(myLibrary);

QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
    .blackboxRegistry(registry)
    .blackboxEnabled(true)                    // D29: required opt-in
    .allowedBlackboxModules(Set.of("mylib"))  // D29: optional allow-list
    .build();
```

> **D29 Security:** Blackbox libraries are **disabled by default**. Set `blackboxEnabled(true)` to
> activate registry lookups. Optionally restrict which modules may be resolved via
> `allowedBlackboxModules(Set<String>)` — an empty set (default) allows all registered libraries
> when enabled. See also §4.7.6 for linker enforcement.

**OSGi:** The registry is a DS Component with `@Reference(MULTIPLE, DYNAMIC)` whiteboard
for `QvtoBlackboxLibrary` services.

#### 4.7.6 Synthetic Module Creation (QvtoLinker)

When the parser encounters `import mylib;`, `access Tools;`, or `extends Base;`, it creates a
**stub module** — a `Library`/`OperationalTransformation` with only the qualified name set.
The parser marks each stub with a `qvto.linker.stub` EAnnotation so the linker can distinguish
real stubs (requiring external resolution) from inline-defined modules (forward declarations
in the same compilation unit).

**Linker resolution order:**

1. **Inline modules** — modules defined in the same source (e.g., `library Tools { ... }` +
   `access Tools;` in the same file). Matched by name against non-stub `ModuleImport` targets.
2. **Unit resolvers** — `QvtoUnitResolver` instances (regular QVT-O modules from other files).
   Only consulted if `unitResolverEnabled = true` (D29).
3. **Blackbox registry** — `QvtoBlackboxRegistry.getLibrary(qualifiedName)`.
   Only consulted if `blackboxEnabled = true` (D29).

If a stub cannot be resolved through any of these, the linker throws
`QvtoParseException("Cannot resolve import: qualifiedName")` — **fail-fast at link time**.

> **D29 Security:** The linker **always runs**, regardless of enable flags. When
> `blackboxEnabled = false` and `unitResolverEnabled = false`, only inline modules can be
> resolved. Any external import will fail at link time with a clear error message.
> Allow-lists (`allowedBlackboxModules`, `allowedUnitModules`) provide additional filtering
> when the corresponding feature is enabled.

**Synthetic module creation** (when a blackbox library is resolved):
- `Library.isBlackbox = true`
- One `EClass` (module class) with the library name
- For each `BlackboxOperationDescriptor`: a synthetic `Helper` EOperation
  - `Helper.isBlackbox = true`, `Helper.isQuery = true`
  - Context parameter (`self`) if `contextType != null`
  - Named parameters (`arg0`, `arg1`, ...)

This synthetic module integrates seamlessly with the existing `QvtoOperationResolver` —
blackbox operations are found the same way as regular helpers.

#### 4.7.7 Runtime Dispatch (QvtoOperationProvider)

In `QvtoOperationProvider.addModuleOperations()`, blackbox operations are detected by the
`isBlackbox` flag on `Helper`. Instead of delegating to `QvtoEvaluator.callOperation()`,
the provider:

1. Finds the owning `QvtoBlackboxLibrary` via module name → registry lookup
2. Creates a `QvtoBlackboxInvocationContextImpl` with the current evaluation state
3. Calls `library.invoke(operationName, context, args)`

#### 4.7.8 Selective Import — `from ... import` (§8.4)

QVT-O supports selective imports via `from ... import` syntax:

```qvto
from mylib import trimAll;              -- only trimAll visible
from mylib import greet, farewell;      -- multiple names
from mylib import *;                    -- wildcard: all operations visible
import mylib;                           -- classic: all operations visible
```

**Metamodel:** `ModuleImport.importedNames : EString [0..*]`
- Empty list → all operations visible (wildcard or classic import)
- Non-empty list → only named operations are visible

**Visibility enforcement:** Both `QvtoOperationResolver.findAllOperations()` and
`QvtoOperationProvider.addModuleOperations()` filter operations by `importedNames`.
Operations not in the list are excluded from resolution, producing an "Unknown operation"
error at runtime if called.

---

### 4.x `parse()` and `compile()` — the graph and the document

`QvtoEngine` offers two ways from source to something executable, and they differ in ownership:

| | returns | what it is | storable |
|---|---|---|---|
| `parse(source, unitName)` | `OperationalTransformation` | the in-memory graph the evaluator needs | **no** |
| `compile(source, unitName)` | `CompiledUnit` | one document: manifest, the script, the parser's satellites, embedded dependencies | yes |

The parser creates objects the tree references but that no metamodel feature contains — the
variable `self`, a `PrimitiveType` for every `Integer` it meets, a wrapper type for every classifier
it resolves, the default expression of an intermediate class. In memory that costs nothing, because
a Java reference needs no owner. Saving the result of `parse()` fails on exactly those objects with a
`DanglingHREFException`, and `EcoreUtil.copy` points its references back at the originals. Measured:
a minimal transformation with no import keeps one such object (`PrimitiveType(Integer)`); one with
modeltypes, a mapping, a guard and an intermediate class keeps thirteen.

`compile()` gives them a home. The language-neutral `UnitPackager` (bundle
`org.eclipse.fennec.m2x.unit`) wraps the script into a `CompiledUnit`, then `SatelliteCollector`
walks every non-containment reference of the document and moves whatever it finds uncontained into
`CompiledUnit.satellite`, iterating until nothing is left — a satellite may itself reference a
further uncontained object. Afterwards every reference resolves within the document; that is
asserted, not assumed, and a document that does not become self-contained fails `compile()` with a
`QvtoParseException` rather than the first `save()` somewhere else.

Three things are deliberately so:

- **The script is moved, not copied.** `compiled.getUnit()` is the very `OperationalTransformation`
  `parse()` would have returned, and `execute()` takes it as before. `compile()` adds a document
  around the graph; it does not replace the graph.
- **References into metamodels stay external.** A referenced `EPackage` lives in its own resource
  and must, otherwise a shared unit could not lend its types — the reason the containment approach
  of PR #128 was rejected. The collector treats anything that already lives in a resource as
  not-a-satellite.
- **The container has no semantics.** `satellite` is typed `EObject`, the evaluator never reads it,
  and the container knows nothing about what it holds. Metadata — `id`, `namespace`, `version`,
  `description` — live on the `CompiledUnit`, because a satellite means nothing outside its unit.

### 4.z The unit fingerprint (`m2x1`)

`compile()` stamps `manifest.unitFingerprint` with the value of `DefaultUnitFingerprintService`
(bundle `org.eclipse.fennec.m2x.unit`): `m2x1:` followed by the SHA-256 of a canonical form of the
script. The contract is that of `emf.osgi`'s `FingerprintService` — `<scheme>:<digest>`, the tag
versions the canonicalization, values of different tags are not comparable. Packages keep their `fp1`
values; units get `m2x1`; a manifest carries both. `fp1` could not serve for units: it canonicalizes
the Ecore metastructure only, and all three unit roots inherit from `EPackage`, so two
transformations with the same intermediate classes and different mapping logic would share a value.

The canonical form is a reflective walk with an **exclusion** list (`AstCanonicalizer`), not a
hand-written switch with an inclusion list: the metamodels grow, and a forgotten class would make
two different units silently share a value. Per node: the class as `nsURI#Name`; every set,
non-derived, non-transient feature sorted by name; attribute values as `EcoreUtil.convertToString`;
many-valued features in order — operand and statement order is semantic. A reference to a metamodel
element enters as `nsURI#//Classifier/feature` — the package's nsURI and the name path below it,
**wherever the metamodel lives**: in a resource named by its nsURI (generated code), in an `.ecore`
file whose path must not enter the value, in no resource at all (built in memory), or as a copy the
document carries. That is what makes the value reproducible across a store round trip (#142 found
`EcoreUtil.getURI` there, which is the file path for a loaded `.ecore` and `#//Book` for a package
without resource — corrected under `m2x1`, the frozen sample being unaffected). A metamodel change
does not cascade into every expression's value. A standard-library type enters as its URI. A reference into the script is the target's
fragment path. A reference to a satellite — outside the script, in the compiled unit's container or
uncontained in a freshly parsed graph — is the satellite **inline**: the value depends on what the
satellite says, not on which object says it, so a parse, its compiled unit and that unit reloaded
from XMI agree. Excluded: `documentation`/GenModel annotations, container references, the
one-to-one back-link of a bidirectional pair. Source positions and comments never enter; they live
beside the AST (#110/#116).

The scheme is **frozen**: `UnitFingerprintServiceTest.m2x1_isFrozen` pins a recorded value, and a
change to the canonicalization has to come with a new tag. Two decisions the concept left open were
taken with it: bound variable names count (no alpha-invariance — `collect(a | a.x)` and
`collect(b | b.x)` differ), and there is one tag for all four languages, because they nest.

Two parser accidents had to go first, because a reproducible fingerprint cannot contain them: the
intermediate package's nsURI carried a per-parse counter (it was registered in the global registry,
which #158 made unnecessary; the nsURI is `…/qvto/intermediate/<module>` now), and MOFM2T named its
inline variables by `identityHashCode`.

Two follow-ups shrank the satellite container after the numbers above were measured: the
predefined types come from the standard library and type references point at the metamodel
classifier directly, without a `ClassifierType` wrapper (#154, #156). The same sample has 7
satellites now instead of 13 — the four variables, the two model types and the intermediate class's
default expression; QVT-R's sample went from 12 to 4, MOFM2T's from 15 to 5.

### 4.w Dependencies: embed, pin, rebind (#139)

`compile()` is parse → resolve → package. The resolve step asks the same sources the link phase of
`execute()` asks — inline modules, the configured unit resolvers, the blackbox registry, under the
same D29 enable flags, allow-lists and limits — but what it does with an answer is the
`UnitCompileOptions.dependencyMode()` (bundle `org.eclipse.fennec.m2x.unit`; `compile(source, name)`
is `compile(source, name, UnitCompileOptions.defaults())`, and the default is `pin`):

| mode | the dependency | the import in the AST | manifest `dependencyEntry` |
|---|---|---|---|
| `embed` | compiled in turn, recursively in the same mode, attached under `CompiledUnit.embedded` | bound to the module inside the embedded unit — in-tree, so the document saves | name, `embed`, the embedded unit's fingerprint |
| `pin` | compiled to learn its unit fingerprint, then dropped | stays the parser's stub, contained as a satellite | name, `pin`, that fingerprint |
| `rebind` | resolved — the name has to be servable — and dropped | stays the stub | name, `rebind`, no fingerprint |

Under `embed` the unit runs on an engine that knows no resolver, also after a reload from XMI;
under `pin` and `rebind` the execute-time linker binds the stub as it always did, and prepare
(#140) will do it from the manifest. A same-file resolution — a stub for a library defined in the
same source — is not a dependency and is bound in every mode. An import nobody serves fails the
compile in every mode with `Cannot resolve import`; a cycle is reported where the dependency is
followed (`embed`, `pin`) — under `rebind` the closure is prepare's business.

**Blackboxes are requirements, not dependencies.** An import the blackbox registry answers produces
a `BlackboxRequirement` — the library name, the class that provided it, and a `SignatureFingerprint`
over one line per operation (`name(context;params):return`, types as `nsURI#Name`, sorted). The
implementation stays with the runtime, the stub stays in the AST; a provider with the right name and
the wrong shape is caught when the unit is loaded, not at the call.

**Package entries.** `UnitPackager.seal()` follows every reference that leaves the document
(`ReferencedPackages`, an `ExternalCrossReferencer` walk) and records one `PackageEntry` per
metamodel: nsURI, fingerprint, scheme, role. The fingerprint is `fp1` from emf.osgi's
`FingerprintService` — the value the ecosystem keys models by (`@EPackage(fingerprint)`,
`MetadataService`), so an entry can be looked up, not only compared. Ecore, XMLType and the OCL
standard library are in every runtime by definition and get no entry. A package without generated
code — a plain `EPackageImpl`, not a generated subclass — is *dynamic*: its entry is `embedded` and
`seal()` copies its root package into `CompiledUnit.packages`, so a runtime that has nothing to
offer for the nsURI can still serve the unit's types (#140 decides per package: equal fingerprint →
the runtime instance wins, missing → the copy, differing → hard failure). A generated package is
only `referenced`. The service arrives under OSGi as a reference of the engine component
(`FingerprintService`, optional); a plain JVM uses `UnitPackager.withDefaults()`, which reaches the
default implementation through `FingerprintHelper`.

**Nothing a resolver lends is mutated.** A `SourceUnit` is parsed afresh; a `CompiledUnit` whose
AST sits in a compiled document is taken as a copy of that document (and its manifest fingerprint
serves a `pin` without compiling again); a bare AST is copied together with its satellites first
(`UnitPackager.detach` — a plain `EcoreUtil.copy` would point the copy's references at the
original's uncontained satellites, §5.5 of the concept). The manifest also records the source
fingerprint (`sourceFingerprint`, over the LF-normalized text) and the format version, so a store
holding the source as well can tell whether the compiled unit is behind it.

**Metamodel elements are never satellites.** `SatelliteCollector.isMetamodelElement` treats anything
whose outermost container is a plain `EPackage` with an nsURI as external — whether that package
lives in a resource or not. A generated package that was never given a resource and a package built
in memory and put into a registry are metamodels all the same; before #139 the collector would have
pulled such a package into the satellite container. The fingerprint canonicalizer applies the same
rule and writes them by URI. XMI still needs a resource to write an href, so a store gives such a
package the resource EMF gives a generated one: `createResource(nsURI)`.

**Store.** `DefaultUnitStore` (package `unit.store`) implements the `UnitStore` contract over a
`UnitStoreBackend` — bytes by key, keys by name — with `InMemoryUnitStoreBackend` as the one that
needs nothing; a file system, a bundle or emf.osgi's `ArtifactStore` with an index beside it are
other backends. The object medium is a store of its own: `RegistryUnitStore` (bridge bundle
`…unit.registry`, #213) over an emf.osgi `EObjectRegistry` of live compiled-unit documents —
normalized into the transport state on the way in, an independent copy on the way out. The store is dumb — key ↔ document (#211): it owns the serialization and nothing
else. A compiled unit goes in as the XMI of a *copy* of its document (a document without a
manifest is refused, `compile()` first) under
`UnitKey.pinned(language, name, COMPILED, manifest.unitFingerprint)`, a source as a
`SourceUnit` document under its source fingerprint. Both come back as independent copies loaded into
a fresh resource set: `PackagedUnit` or `StoredSource`. On the way in, a referenced metamodel without
a resource gets `createResource(nsURI)`, as EMF gives a generated one. On the way out the store's
registry answers for the user's metamodels, the global registry for the languages' own, and a
package copy the document carries serves every nsURI neither knows — the fragment is walked from
the copy itself, because EMF would otherwise look for `//Book` in the document's resource. A
reference that still resolves to nothing is a `UnitStoreException`, never a proxy handed on. An
unpinned key loads the newest version; a pinned key whose fingerprint the store does not hold is an
error naming the versions it has, never "not found". `QvtoStoreUnitResolver` (and its QVT-R and M2T
twins in the engine bundles) resolve from a store — compiled unit first, source second — and turn a
`UnitStoreException` into an `IllegalStateException` rather than an empty answer, so a broken store
cannot let a stale copy elsewhere step in (§7 of the concept, #141 refines the rules).

### 4.v Prepare: one context, nothing left to resolve (#140)

Execute never asks a resolver. What makes that true is Prepare (`UnitPreparer`, package
`unit.prepare`): from a store, it loads the requested units and — following their manifests — the
whole dependency closure into **one** `UnitResourceSet`, verifies what they were built against, has
the language bind them, and hands the engine a `PreparedContext`. Per unit:

- **Load.** `pin` loads exactly the pinned fingerprint — a version the store no longer has is a hard
  failure naming what it has instead (decision E4). `rebind` takes the newest version and writes what
  was bound into the unit's `resolvedClosure` (name, fingerprint, `store`), because under rebind the
  unit fingerprint says nothing about the dependencies. `embed` has nothing to load. Two units
  pinning different versions of one name are a conflict, not a silent choice.
- **Verify.** Every `PackageEntry` against the context's registry: an instance with the same
  fingerprint is what the unit's references resolve to — generated code wins, blackboxes keep working
  with generated types; a missing instance is served from the copy the unit carries; a *differing*
  fingerprint is a hard failure naming the nsURI and both values. The same nsURI recorded with two
  fingerprints by two units of one run is a failure too. The resolution happens as the resource set
  resolves the document — the context's registry, then global, then copy — and the check comes after; the
  order is sound because no decision changes: equal → the runtime instance the document already
  resolved to, different → failure and the context is discarded, absent → the copy.
- **Bind.** The language's `UnitBinder` — `QvtoEngine.unitBinder()` and its QVT-R/M2T twins, so the
  binder carries the engine's blackbox registry and D29 limits — points every stub at the loaded
  dependency (QVT-O: `ModuleImport.importedModule`, blackbox stubs at the registry's synthetic
  library, placed in the unit's satellites; QVT-R: merge and strike the import; MOFM2T: link from the
  `m2t.link.*` information and strip it) and checks the blackbox requirements (QVT-O: library present
  and signature fingerprint equal; QVT-R: a registry to serve it at all — its blackbox API declares no
  operations, which is stated rather than hidden).

`QvtoEngine.execute(PreparedContext, qualifiedName, context)` is the Execute phase proper: no linker
is built, the evaluator runs with the context's registry, and a stub left over is an
`IllegalArgumentException` — the context was not prepared with this engine's binder — not something
to resolve on the quiet. `PreparedContext.contents(URI)` loads a model in the same resource set, so it
carries the context's types (§6.1 of the concept: an extent handed in as already loaded objects
carries the types of wherever it was loaded); the output of one unit is the input of the next without
a foreign-type surprise, and a `Book` created by a unit is an instance of the runtime's `Book`.

`UnitResourceSet` (`unit.api`) is where type identity is decided, once, at load time. Its registry
answers in three tiers — the registry it was given, the global one (the languages' own metamodels
are generated), the copies loaded documents carry — and it resolves package-rooted references
(`nsURI#/`, `nsURI#//Book`) by walking the fragment from the package that answers, because EMF's own
route goes through the package's resource and a package built in memory has none.

### 4.t Structural validation on load (#142)

A unit loaded from a store bypasses the parser and every check it enforces by construction. The
evaluator's runtime limits still hold; the shape of the document does not check itself. `UnitValidator`
(`unit.validate`) does, in the `UnitMaterializer` every consumer's load goes through — on by
default, `UnitMaterializer.withoutValidation()` for a trusted, hot medium — and rejects a unit with
every finding named by its object path:

| Check | What it catches |
|---|---|
| Integrity | `unitFingerprint` recomputed and compared — corruption and drift in the backend; **not** manipulation, whoever changes the unit recomputes the fingerprint |
| Manifest | known format version, language and name, `pin` entries with fingerprint, embedded units of the same language, package copies with nsURI and entry |
| Closure | every reference leaving the document points into a metamodel (`SatelliteCollector.metamodelOf`), no proxies; EMF's lazily created `EFactory` of a package in the document is a back-link, not a reference out |
| Ecore structure | `Diagnostician` with the multiplicity and data-value rules of the languages' own features — Ecore's own rules are left out, because a QVT-O `Module` is an `EPackage` that has no nsURI to be well formed |
| Size | depth ≤ 1 000, objects ≤ 1 000 000 by default — closing for the load path the parse-depth and input-size gaps the security analyses name |

The language's half is `UnitBinder.validate`, run by `UnitPreparer` before binding: the root is a unit
of the language, every `VariableExp` has its declaration in the document, QVT-R domains refer to declared
typed models, MOFM2T invocations are bound or recorded for binding. **Authenticity is the backend's
question**, not the format's: `UnitStoreBackend` stores and returns bytes, so a backend that signs on
`put` and verifies on `get` closes it without any change to store or format — recorded in the four
security analyses as an accepted risk with that recommendation (S-15, Q-10, R-13, T-11).

### 4.u Several sources for one import (#141)

The resolver signature stays `Optional<QvtoUnit> resolveUnit(String)` — minimal, and a store, a
file system or a bundle each map a qualified name onto their own addressing. What several sources
newly require is one policy, `ResolutionPolicy` (`unit.resolve`), used by the compiler, the
execute-time linker and both discovery resolvers:

1. **Every source is asked**, not only until the first answer. Order is the caller's: configured
   resolvers in configuration order, whiteboard services by `service.ranking` (highest first; among
   equals the one registered first), class-path providers in declaration order. The first answer in
   that order is returned.
2. **A failing source is an error.** A resolver that throws ends the resolution with a
   `UnitResolutionException` naming the source and the cause, which the compiler and linker turn into
   their own `Cannot resolve import 'x': source … failed …`. Before, the whiteboard and class-path
   resolvers logged and skipped — a broken store read as "not found" while a stale copy from the
   next source stepped in. The store resolvers throw the same exception for a `UnitStoreException`.
3. **Answers have to agree.** Two answers are compared by what can be compared — source with source
   by source fingerprint, compiled document with compiled document by unit fingerprint, source with
   compiled document by the document's `sourceFingerprint` (the reason the manifest records it), bare
   AST with bare AST by AST fingerprint. Different content is a conflict naming both sources. A bare
   AST against a source cannot be compared and the first wins — the one gap, stated.

**Ownership** is now in the javadoc of the three resolver interfaces: a unit a resolver hands out is
lent, the receiver must not mutate it; the compiler copies what it embeds, the linker binds inside
the importing unit only. Prepare (#140) needs none of this: it takes units from the store by key, and
two units pinning different versions of one name are its own conflict check.

The three compilers are one shape in three languages: `QvtoUnitCompiler` (this section),
`QvtdUnitCompiler` (QVT-R: binding is a merge of rules, see the QVT-R architecture) and
`M2tUnitCompiler` (MOFM2T: `extends`/`imports`; the names to bind are kept on the module under
`pin`/`rebind`, see the M2T user guide §3.7).

### 4.y Type names resolve in what the unit declares

A simple type name in a QVT-O unit resolves in the packages the unit **declares** — `modeltype M
uses '…'`, `metamodel` declarations, and the unit's own intermediate classes (QVT v1.3 §8.1.3,
§8.1.5, §8.1.10) — and nowhere else. `QvtoUnitBuilder.declaredPackages` collects them as the
declarations are visited and shares the set with every `QvtoExpressionBuilder` it creates.

Until #158 a name that resolved in no declared package fell back to a scan of every package in the
registry, so `transformation t() { var c := object EClass {}; }` parsed without any `modeltype` —
and what a bare name meant depended on what else the JVM had registered. It is an error now, as it
is in Eclipse QVT-O (*Unresolved type*); the transformation declares `modeltype ECORE uses
'http://www.eclipse.org/emf/2002/Ecore';`. Qualified names (`ECORE::EClass`) and the `uses`
clause itself still go through the registry — a declaration is what the registry is for.

## 5. Parser Architecture (`qvto.parser`)

**Bundle:** `org.eclipse.fennec.m2x.qvto.parser`

### 5.1 Structure

```
org.eclipse.fennec.m2x.qvto.parser/
├── src/
│   └── org/eclipse/fennec/m2x/qvto/parser/
│       ├── QvtoParserSupport.java         # Entry point
│       ├── QvtoUnitBuilder.java           # Module-level visitor (parse tree → EMF AST)
│       ├── QvtoExpressionBuilder.java     # Expression-level visitor
│       ├── QvtoErrorListener.java         # Error collection
│       └── QvtoParseDiagnostic.java       # Diagnostic implementation
├── src-gen/                               # ANTLR4-generated parser code
│   └── org/eclipse/fennec/m2x/qvto/parser/
│       ├── QvtOParser.java
│       ├── QvtOLexer.java
│       ├── QvtOVisitor.java
│       └── QvtOBaseVisitor.java
└── grammar/
    └── QvtO.g4                            # Combined lexer/parser grammar
```

### 5.2 Grammar Composition

The QVT-O grammar imports the OCL base grammar:

```
QvtO.g4 ──imports──> Ocl.g4 (from ocl.parser JAR)
```

Entry rule: `compilationUnitEntry()`. Extends OCL with transformation-level constructs
(mappings, helpers, imports, modeltypes, imperative statements).

### 5.3 Two-Level Visitor

| Visitor | Scope | Handles |
|---------|-------|---------|
| `QvtoUnitBuilder` | Module level | Transformations, libraries, mappings, helpers, constructors, imports, tags, typedefs, properties |
| `QvtoExpressionBuilder` | Expression level | Literals, operators, navigation, iterators, imperative constructs (blocks, loops, assignments) |

`QvtoUnitBuilder` delegates expression sub-trees to `QvtoExpressionBuilder`.

### 5.4 Parsing API

```java
QvtoParserSupport parser = new QvtoParserSupport();
OperationalTransformation ast = parser.parse(source, unitName, registry);
```

The registry is always explicit. There is deliberately no convenience overload that reaches for `EPackage.Registry.INSTANCE`: the fallback belongs to `QvtoConfiguration` and is applied there, once (D42).

Alias tag targets (`tag "alias" RDBMS::Table::key_ = "key";`, §8.3.19) resolve through their qualifier: it names a model type of the transformation, or a package by name or nsURI, and only that metamodel is searched. Without a usable qualifier the search falls back to the model extents and then to the registry, and a name that fits several metamodels produces a warning instead of a silent pick.

Type names resolve in this order: the module's own `typedef`s → the package registry → the QVT-O standard library (`QvtoStandardLibraryTypes`, QVT v1.3 §8.3.1). A name that resolves in none of them becomes an error diagnostic, collected and reported together with every other unresolved name when the unit has been visited. Typedefs are registered in a pre-pass, like intermediate class shells, so a reference ahead of the declaration resolves too.

---

## 6. Engine Architecture (`qvto.engine`)

**Bundle:** `org.eclipse.fennec.m2x.qvto.engine`

### 6.1 QvtoEngine — Facade

- Implements `QvtoEngine`
- Plain Java (OSGi-optional), no OSGi dependencies
- Key methods: `parse(URI)`, `parse(String, String)`, `execute(OperationalTransformation, QvtoExecutionContext)`

### 6.2 QvtoEvaluator — 3-Level Switch Interpreter

The evaluator uses composition (D25), not inheritance:

```
QvtoEvaluator
  ├── ImperativeDispatch (inner class, extends ImperativeOclSwitch<Object>)
  │     └── handles: BlockExp, AssignExp, ForExp, WhileExp, SwitchExp,
  │                  ReturnExp, BreakExp, ContinueExp, TryExp, RaiseExp,
  │                  AssertExp, LogExp, ComputeExp, VariableInitExp, ...
  ├── QvtOpDispatch (inner class, extends QvtOperationalSwitch<Object>)
  │     └── handles: ObjectExp, MappingCallExp, ResolveExp, ResolveInExp, ...
  └── OCL delegation → OclEngine via snapshot OclContext
        └── handles: PropertyCallExp, IteratorExp, literals, stdlib, ...
```

**Dispatch flow:** `eval(OclExpression)` tries ImperativeDispatch first, then QvtOpDispatch,
then delegates to OclEngine for standard OCL expressions.

**Source evaluation invariant:** For `OperationCallExp`, intermediate dispatch steps
(model extent/element operation checks, imperative source handling) must not eagerly
evaluate the source expression unless the operation name actually matches a known
operation in that category. This prevents duplicate side effects when chaining operations
(e.g. `String.incrStrCounter('X').repr()` must evaluate `incrStrCounter` exactly once).
Name guards (`QvtoModelOperations.isExtentOperation`/`isElementOperation`) enforce this.

### 6.3 QvtoEvalEnvironment — Mutable Scope Stack

Unlike OCL's immutable chain-of-scopes, the QVT-O environment supports imperative assignment:

```java
env.pushScope();          // Enter mapping/helper/block
env.define("x", value);   // New variable in current scope
env.assign("x", newVal);  // Update first matching scope (imperative :=)
env.lookup("x");          // Walk scope stack upward
env.popScope();           // Leave scope
```

Stack-based (`ArrayDeque`) for efficient push/pop during imperative evaluation.

### 6.4 QvtoOperationProvider — Mutual Recursion Bridge (D25)

Implements `OclOperationProvider` to enable OCL expressions calling back into QVT-O:

```
QvtoEvaluator
  ├── owns: OclEngine (created internally)
  │     └── registered provider: QvtoOperationProvider
  └── delegates OCL sub-expressions → OclEngine

QvtoOperationProvider implements OclOperationProvider
  ├── recognizes QVT-O helpers/mappings → re-enters QvtoEvaluator
  └── recognizes blackbox operations → delegates to QvtoBlackboxLibrary.invoke()
```

Uses `QvtoOperationResolver` to find named operations (helpers, mappings, main) in the
transformation AST. For blackbox operations (detected via `Helper.isBlackbox`), dispatches
to the owning `QvtoBlackboxLibrary` instead of the evaluator — see §4.7.7.

Respects selective import visibility: operations from modules imported via
`from ... import name1, name2` are filtered by `ModuleImport.importedNames` — see §4.7.8.

### 6.5 QvtoExtentManager — Model Extent Binding

Binds `ModelParameter` declarations to `QvtoModelExtent` instances:

- Positional mapping: ModelParameter → QvtoModelExtent
- Enforces read-only constraint on `in` parameters (§8.1.3.2)
- Accessed via `getExtent(ModelParameter)`, `getExtent(int index)`, or `getExtent(String name)`
- **Explicit extent routing (§8.1.3/§8.1.6):** `object T@extentName { ... }` and `new T@extentName()` route created objects to the named extent via `addToExtent(EObject, Variable)`

### 6.6 QvtoControlFlowException — Imperative Control Flow

Sealed exception hierarchy for imperative control flow:

```java
public sealed class QvtoControlFlowException extends RuntimeException
    permits ReturnException, BreakException, ContinueException,
            RaiseException, FatalAssertionException { ... }
```

### 6.7 Supporting Classes

| Class | Purpose |
|-------|---------|
| `QvtoLinker` | Resolves stub modules (imports) into parsed modules or synthetic blackbox modules — see §4.7.6 |
| `QvtoOperationResolver` | Resolves named operations (helpers, mappings, main()) from transformation AST, respects `importedNames` |
| `QvtoIntermediatePropertyStore` | Per-instance storage for `ContextualProperty` values (§8.1.10) |
| `QvtoAliasRegistry` | Tag alias resolution from EAnnotations (§8.3.19) |
| `QvtoModelOperations` | Built-in operations: `objectsOfType`, `rootObjects`, `metaClassName`, `clone`, etc. |
| `QvtoBlackboxInvocationContextImpl` | Bridges `QvtoBlackboxInvocationContext` to evaluator state — see §4.7.4 |

---

## 7. Trace Infrastructure

### 7.1 QvtoTraceManager

Maintains parallel trace data structures for two purposes:

1. **Fast resolve lookups:** Flat list `records: List<QvtoTraceRecord>` scanned linearly for resolve operations
2. **EMF Trace export:** EMF `Trace` model for `QvtoExecutionResult`

```java
traceManager.addRecord(mappingOp, source, args, result);
// → adds to both lightweight records and EMF Trace model
```

### 7.2 Trace Model

The `trace` EPackage provides EMF-serializable trace records:

| Element | Purpose |
|---------|---------|
| `Trace` | Root container, holds all `TraceRecord` instances |
| `TraceRecord` | One mapping invocation: source → result |
| `EMappingOperation` | Reference to the executed mapping |
| `EMappingContext` | Context object (self) |
| `EMappingParameters` / `EMappingResults` | Input/output parameter values |
| `EValue` / `VarParameterValue` | Typed trace values |

---

## 8. Resolve Mechanism

### 8.1 Resolve Variants

All resolve expressions query the trace manager:

```qvto
resolve(Type)                                    -- all, implicit source
resolveone(Type)                                 -- one, implicit source
obj.resolve(Type)                                -- explicit source
resolve(t : Type | condition)                    -- with variable + condition
resolveIn(ClassName::mappingName, Type)           -- in specific mapping
resolveIn(ClassName::mappingName, t : Type | cond)
invresolve(Type)                                 -- inverse (result → source)
invresolveone(Type)
```

### 8.2 Late Resolve

Deferred resolve expressions are collected during evaluation and resolved after the
transformation completes:

```qvto
late resolve(Type)                               -- deferred
late resolveone(Type)
late resolveIn(ClassName::mappingName, Type)
```

Late resolve creates deferred tasks that are executed after the main transformation run,
allowing forward references to mappings that haven't executed yet.

---

## 9. Multi-File Composition

### 9.1 Overview

QVT-O supports multi-transformation composition (§8.1.5):

```qvto
access transformation T(in src : SRC, out tgt : TGT);

// Create and execute another transformation
var t : T = new T(srcExtent, tgtExtent);
t.transform();
```

### 9.2 Supported Operations

| Operation | Purpose |
|-----------|---------|
| `access` declaration | Declares a required transformation without importing its body |
| `new T(args)` | Instantiates a transformation with bound extents |
| `transform()` | Executes a transformation synchronously |
| `parallelTransform()` | Executes asynchronously (future) |
| `wait()` | Waits for async transformation completion |

### 9.3 Status Query

```qvto
t.succeeded()        -- true if transform completed without errors
t.failed()           -- true if transform failed
t.raisedException()  -- returns the raised exception (if any)
```

### 9.4 Static Intermediate Property Sharing

Intermediate properties declared as `static` are shared across transformation instances,
enabling cross-transformation data flow.

### 9.5 Unresolved Imports Are Not Dangling References

Two failure modes look alike and are not related. Keeping them apart saves the next reader a
measurement:

| | What it is | What happens |
|---|---|---|
| **Unresolved import** | `import lib.Strings;` names a unit no resolver has | `QvtoParseException: Cannot resolve import: lib.Strings` (`QvtoLinker`). The same in QVT-R; M2T reports it as an error diagnostic since #144 |
| **Dangling reference on save** | the parsed AST holds objects the parser created and no feature contains — type instances, `self`, `result`, synthetic classifiers | `resource.save()` fails with `DanglingHREFException`. Nothing to do with resolution |

The second happens with a *fully resolved* transformation. Measured on a minimal one — no `import`,
no `modeltype`, parses, links and executes — the AST still holds one uncontained target,
`Variable.type → PrimitiveType(Integer)`, and the save fails on it. With modeltypes, a mapping and
`self` there are fifteen.

That is the reduced, still valid core of #127: only the parser-created satellites need a home, and
`compile()` is where they get one (§4.x). The type references themselves stay non-containment,
because a shared unit lends its types rather than owning them — see the compiled-unit concept behind
epic #135.

---

## 10. Syntax Reference

Verified against Eclipse QVT-O (LPG grammar `ImperativeOCL.gi` + test files).

### 10.1 forEach / forOne (Arrow-Operator)

```qvto
collection->forEach(var) { body }
collection->forEach(var1, var2) { body }
collection->forEach(var | condition) { body }
collection->forOne(var) { body }       -- stops after first match
```

- `->forEach` is an iterator via arrow operator, NOT a standalone statement
- `forEach(var; collection)` is INVALID

### 10.2 Object Expression

Direct feature assignment without prefix (`self.` or `result.` not needed):

```qvto
object EClass {
    name := 'mapped' + self.name;
}
object x : EPackage {
    name := 'p1';
    eClassifiers += self.eClassifiers->map c2c();
}
```

### 10.3 When / Where Clauses

Single OCL expression, no semicolon:

```qvto
mapping EClass::toClass() : Class when { not self.interface } { ... }
mapping X::m() : Y
    when { self.name <> null and self.name.startsWith('A') }
    where { result.name = self.name } { ... }
```

### 10.4 Resolve Syntax

```qvto
resolve(Type)
resolveone(Type)
obj.resolve(Type)
resolve(t : Type | condition)
resolveIn(ClassName::mappingName, Type)
resolveIn(ClassName::mappingName, t : Type | cond)
invresolve(Type)
invresolveone(Type)
late resolve(Type)
late resolveone(Type)
late resolveIn(ClassName::mappingName, Type)
```

### 10.5 Import Declarations

```qvto
import mylib;                           -- classic: all operations visible
from mylib import trimAll;              -- selective: only trimAll
from mylib import greet, farewell;      -- selective: multiple names
from mylib import *;                    -- wildcard: all operations visible
access transformation T(in s : SRC, out t : TGT);  -- access declaration
```

### 10.6 Imperative if (with Block)

QVT-O has BOTH:
- OCL-Expression: `if cond then expr else expr endif`
- Imperative with blocks: `if (cond) then { stmts } else { stmts } endif`

```qvto
if (ro.oclIsKindOf(Family::Family)) then {
    object Family::Family {};
    log("It is a Family");
} else {
    log("Not a family");
} endif;
```

---

## 11. Design Decisions (QVT-O-specific)

| # | Decision | Status |
|---|----------|--------|
| D22 | 3 EPackages in `qvto.model` | Implemented — see §3.4 |
| D24 | Blackbox via `QvtoBlackboxLibrary` interface | Implemented — see §4.6 |
| D25 | Hybrid evaluator (composition, not inheritance) | Implemented — see §6.2 |
| D26 | `Dict`/`List` as aliases for OCL `Map`/`Sequence` | Implemented — parser-level mapping |
| D27 | Unit Resolver — ServiceLoader + Whiteboard | Implemented — see §4.6 |
| D28 | QVT-O v1.3, Eclipse feature scope | Implemented — gaps documented in §12 |

---

## 12. Future / Planned Work

### 12.1 Deferred Gaps (Phase 10)

| # | Gap | Severity | Status |
|---|-----|----------|--------|
| P10-05 | `<<id>>` Stereotyp-Syntax | Low | @Disabled — Lexer `<<`/`>>` collision |
| P10-06 | Model I/O (removeElement, read-only guards) | High | @Disabled — requires EMF resource model integration |
| P10-07 | UML-specific Ops | Low | Skipped — intentionally not implemented (like Eclipse) |

### 12.2 Future Enhancements

- **OSGi DS Component:** `QvtoEngineComponent` with `@Reference` injection for blackbox libraries and unit resolvers
- **Performance benchmarks:** QVT-O vs Eclipse QVT-O under identical conditions (following OCL benchmark pattern)
- **QVT-O Language Server:** LSP support (Phase 5)
