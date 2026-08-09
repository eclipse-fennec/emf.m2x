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
| `org.eclipse.fennec.m2x.qvto.engine` | Evaluator implementation |
| `org.eclipse.fennec.m2x.qvto.model` | QVT-O & Trace EMF metamodel |
| `org.eclipse.fennec.m2x.ocl.*` | Required OCL bundles (transitive) |

---

## 2. Quick Start

Minimal example — parse a QVT-O transformation from a String and execute it:

```java
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngine;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;

// 1. Create engine
OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
QvtoConfiguration qvtoConfig = QvtoConfiguration.builder(oclConfig).build();
QvtoEngine engine = QvtoEngines.create(qvtoConfig);

// 2. Parse transformation
OperationalTransformation trafo = engine.parse("""
    transformation Uml2Rdbms(in uml : UML, out rdbms : RDBMS);

    main() {
        uml.rootObjects()[Package]->map packageToSchema();
    }

    mapping Package::packageToSchema() : Schema {
        name := self.name;
    }
    """, "Uml2Rdbms");

// 3. Set up model extents
BasicQvtoModelExtent inExtent = new BasicQvtoModelExtent(myUmlPackage);
inExtent.setReadOnly(true);  // in-parameter
BasicQvtoModelExtent outExtent = new BasicQvtoModelExtent();

// 4. Execute
QvtoExecutionResult result = engine.execute(trafo, QvtoExecutionContext.of(inExtent, outExtent));

// 5. Read output
if (result.isSuccess()) {
    List<EObject> outputObjects = outExtent.getContents();
}
```

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

#### Under OSGi

`DefaultQvtoEngine` publishes `QvtoEngine` as a prototype-scoped service, so every consumer gets its own engine with its own caches. Its configuration policy is optional: there is a working engine without configuring anything, and configuring one does not register a different service.

It takes the OCL engine as a mandatory service reference, which is door 2 — the engine a consumer configured is the engine that evaluates. OCL limits therefore belong on `DefaultOclEngine`, not here:

```json
{
  "DefaultQvtoEngine": { "qvto.blackboxEnabled": true },
  "DefaultOclEngine":  { "ocl.maxDepth": 500 }
}
```

| Property | Default |
|---|---|
| `qvto.blackboxEnabled` | `false` |
| `qvto.allowedBlackboxModules` | *(empty — allows none)* |
| `qvto.unitResolverEnabled` | `false` |
| `qvto.allowedUnitModules` | *(empty — allows none)* |
| `qvto.maxBlackboxLibraries` | 10 |
| `qvto.maxUnitResolvers` | 5 |

### 3.1.1 Configuring both sides

The full form, when the OCL side needs configuration too:

```java
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;

// OCL configuration (shared with QVT-O)
OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport())
    .expressionCache(OclLruExpressionCache.ofSize(4096))
    .build();

// QVT-O configuration — or builder(oclEngine) to use an engine you already have
QvtoConfiguration qvtoConfig = QvtoConfiguration.builder(oclConfig)
    .blackboxRegistry(new BasicQvtoBlackboxRegistry())
    .addUnitResolver(myUnitResolver)
    .build();

QvtoEngine engine = QvtoEngines.create(qvtoConfig);
```

### 3.2 Configuration Options

`QvtoConfiguration.Builder` methods:

| Method | Description |
|--------|-------------|
| `resourceSet(resourceSet)` | Resource set whose package registry resolves `modeltype … uses '<nsURI>'` |
| `packageRegistry(registry)` | The registry itself (default: `EPackage.Registry.INSTANCE`); wins over `resourceSet` when both are set |
| `blackboxRegistry(registry)` | Registry for blackbox Java libraries |
| `addUnitResolver(resolver)` | Add a unit resolver for multi-file composition |
| `unitResolvers(list)` | Set all unit resolvers at once |
| `parallelExecutor(executor)` | Executor for `parallelTransform()` (default: virtual threads) |

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

Everything downstream — parser, linker, alias resolution, nested blackbox invocations — uses exactly that registry; nothing reaches for the global one on its own (D42). Under OSGi, or wherever two versions of one nsURI can coexist, this is what keeps the engine from forming its own opinion about which version an nsURI names. Model version identity stays yours; see the `emf.osgi` fingerprint guide.

An nsURI that resolves in no registry is an error: `parse` fails with `QvtoParseException` carrying *Failed to resolve metamodel (…)*. QVT v1.3 §8.2.1.6 declares `ModelType.metamodel` as `[1..*]`, so a model type without a metamodel would not be a well-formed AST. A type name that resolves in neither the registry, the module's `typedef`s nor the QVT-O standard library fails the same way, with *Unknown type (…)*; every unresolved name of the unit is reported at once.

The standard library types of QVT v1.3 §8.3.1 — `Exception`, `StringException`, `AssertionFailed` and the `Void` synonym — are part of the language and need no metamodel: they resolve without a registry entry, and a metamodel classifier of the same name takes precedence over them.

### 3.4 OSGi Setup

In OSGi, inject the engine via Declarative Services:

```java
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class MyTransformationRunner {

    @Reference
    private QvtoEngine engine;

    public void run(EObject input) throws QvtoParseException {
        // Engine is fully configured via OSGi
        OperationalTransformation trafo = engine.parse(source, "MyTrafo");
        // ...
    }
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
OperationalTransformation trafo = engine.parse(
    "transformation T(in m : PKG); main() { /* ... */ }",
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

Configuration properties are accessible in QVT-O via `configProperty`:

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
| `timeout` | none | Maximum execution time (deadline-enforced) |
| `maxLoopIterations` | 1,000,000 | Maximum iterations per while/for loop |
| `maxDiagnostics` | 10,000 | Maximum diagnostic entries before truncation |
| `maxTraceRecords` | 1,000,000 | Maximum trace records (0 = unlimited) |
| `tracingEnabled` | false | Collect trace records for resolve operations |
| `oclOptions` | strict | OCL evaluation options (null handling, limits) |

### 7.3 OCL Options Within QVT-O

The underlying OCL evaluator respects `OclEvaluationOptions`:

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

### 9.2 Execution Diagnostics

`QvtoExecutionResult` contains diagnostics collected during execution:

```java
QvtoExecutionResult result = engine.execute(trafo, ctx);

if (!result.isSuccess()) {
    for (Diagnostic d : result.diagnostics()) {
        System.err.printf("[%s] %s%n", severityName(d.getSeverity()), d.getMessage());
    }
}

if (result.hasFatalError()) {
    // CANCEL-level severity — transformation was aborted
}
```

### 9.3 Severity Levels

| Level | Value | Meaning |
|-------|:-----:|---------|
| `Diagnostic.OK` | 0 | Success |
| `Diagnostic.WARNING` | 1 | Warning (transformation completed) |
| `Diagnostic.ERROR` | 2 | Error (transformation may have partial output) |
| `Diagnostic.CANCEL` | 4 | Fatal error (transformation aborted) |

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

Tracing is required for resolve operations — they are enabled automatically when the transformation uses resolve.

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
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxLibrary;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxInvocationContext;
import org.eclipse.fennec.m2x.qvto.api.BlackboxOperationDescriptor;

public class StringUtils implements QvtoBlackboxLibrary {

    @Override
    public String getModuleName() {
        return "StringUtils";  // used in 'uses StringUtils;'
    }

    @Override
    public String getUnitQualifiedName() {
        return "my.lib.StringUtils";  // used in 'import my.lib.StringUtils;'
    }

    @Override
    public List<String> getUsedPackageURIs() {
        return List.of();  // EPackage nsURIs used by this library
    }

    @Override
    public List<BlackboxOperationDescriptor> getOperationDescriptors() {
        return List.of(/* operation descriptors */);
    }

    @Override
    public Object invoke(String operationName, QvtoBlackboxInvocationContext context,
                         Object[] args) {
        return switch (operationName) {
            case "capitalize" -> {
                String s = (String) args[0];
                yield s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
            }
            case "slugify" -> ((String) args[0]).toLowerCase().replaceAll("\\s+", "-");
            default -> throw new IllegalArgumentException("Unknown operation: " + operationName);
        };
    }
}
```

### 11.2 Register the Library

**Standalone:**

```java
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoBlackboxRegistry;

BasicQvtoBlackboxRegistry registry = new BasicQvtoBlackboxRegistry();
registry.register(new StringUtils());

QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
    .blackboxRegistry(registry)
    .build();
```

**OSGi:**

```java
@Component(service = QvtoBlackboxLibrary.class)
public class StringUtils implements QvtoBlackboxLibrary {
    // ... automatically discovered via whiteboard pattern
}
```

### 11.3 Using in QVT-O

```
-- Simple import
import my.lib.StringUtils;

-- Selective import
from my.lib.StringUtils import capitalize;

-- In a transformation
transformation T(in m : PKG);

uses StringUtils;

main() {
    var name := 'hello'.capitalize();  -- calls Java method
}
```

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

`QvtoUnit` is a sealed interface with two variants:

```java
// Source code — will be parsed by the engine
QvtoUnit source = new QvtoUnit.SourceUnit("my.pkg.Helper", uri, sourceCode);

// Pre-compiled — already parsed OperationalTransformation
QvtoUnit compiled = new QvtoUnit.CompiledUnit("my.pkg.Helper", parsedTrafo);
```

Use `CompiledUnit` to cache parsed transformations across executions.

### 12.3 Registration

**Standalone:**

```java
QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
    .addUnitResolver(new FileSystemUnitResolver(basePath))
    .build();

// D29: unitResolverEnabled(true) required to activate resolver lookups
// Runtime registration removed — all resolvers declared at configuration time
```

**OSGi:**

```java
@Component(service = QvtoUnitResolver.class)
public class BundleUnitResolver implements QvtoUnitResolver {
    // ... automatically discovered via whiteboard pattern
}
```

### 12.4 QVT-O Syntax for Composition

**Access — call another transformation:**

```
-- Main transformation accesses a helper transformation
transformation Main(in src : SRC, out tgt : TGT);

access Helper;

main() {
    var helper := new Helper(src, tgt);
    helper.transform();
}
```

**Import — reuse library modules:**

```
-- Import a library
import my.lib.StringUtils;

-- Selective import
from my.lib.StringUtils import capitalize, slugify;
```

**Transformation instantiation:**

```
main() {
    -- Create and run another transformation
    var t := new OtherTrafo(inExtent, outExtent);
    t.transform();

    -- Check status
    if t.succeeded() then
        log("OtherTrafo completed successfully")
    endif;

    -- Parallel execution
    var tasks := Sequence{t1, t2, t3};
    tasks->parallelTransform();
    tasks->wait();
}
```

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

For the full threat model and BSI TR-03185 mapping, see [QVT-O Security Analysis](qvto-security-analysis.md).

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
| `timeout` | `null` (none) | Wall-clock deadline — terminates execution after elapsed time |
| `maxLoopIterations` | 1,000,000 | Per-loop iteration cap — applies to `while`, `forEach`, `forExp` |
| `maxStackDepth` | 1,000 | Call stack depth — limits recursive helpers/mappings |
| `maxDiagnostics` | 10,000 | Maximum diagnostic messages before truncation |
| `maxTraceRecords` | 1,000,000 | Maximum trace records — silently drops further records |

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
    .withOclOptions(OclEvaluationOptions.defaults()
        .withNullHandling(OclEvaluationOptions.NullHandling.STRICT));

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

- **Timeout**: A deadline (`System.nanoTime()`) is set at execution start. The evaluator checks the deadline at every function call entry and loop iteration (~15ns overhead per check). When exceeded, execution stops with a `"timeout"` diagnostic.

- **Loop iterations**: Each `while`, `forEach`, and `forExp` loop maintains a per-loop counter. When `maxLoopIterations` is exceeded, the loop breaks with a `"Maximum loop iterations exceeded"` diagnostic.

- **Stack depth**: Checked at every `callOperation`, `callMapping`, and `callConstructor`. When exceeded, the call returns with a `"Maximum stack depth exceeded"` diagnostic.

- **Diagnostics**: The `log()` built-in and internal diagnostics are capped. When the limit is reached, one final `"truncated"` warning is added and further diagnostics are silently dropped.

- **Trace records**: When `maxTraceRecords` is reached, further mapping trace records are silently dropped. This does not terminate execution but may affect `resolve`/`invResolve` for later mappings.

### 13.4 Embedder Responsibilities

The engine protects against resource exhaustion from QVT-O code, but the **host application** is responsible for:

| Concern | Recommendation |
|---------|---------------|
| **Blackbox libraries** (Q-1) | Only register trusted libraries; review all `invoke()` implementations |
| **Configuration properties** (Q-2) | Never pass secrets (DB passwords, API keys) as config properties |
| **URI path traversal** (Q-3) | Validate URIs in `QvtoUnitResolver` before resolving to filesystem paths |
| **SSRF via unit resolver** (Q-8) | Restrict `QvtoUnitResolver` to local paths; reject `http://`, `ftp://` URIs |
| **Read-only enforcement** (Q-9) | Mark input extents as `setReadOnly(true)` |

See the [Security Analysis](qvto-security-analysis.md) for the complete threat model and mitigation details.
