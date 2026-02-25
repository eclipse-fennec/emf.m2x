# QVT-O Implementation Architecture — Fennec M2M (Phase 2)

> Consolidated reference for the QVT-O implementation. Cross-cutting conventions remain in:
> [Development Guideline](development-guideline.md), [Design Decisions](design-decisions.md),
> [QVT-O Test Plan](qvto-test-plan.md)

---

## 1. Overview & Status

The QVT-O engine implements **QVT-Operational v1.3** as a pure Java library,
fully decoupled from the Eclipse platform. It reuses the OCL engine (Phase 1) for
expression evaluation via composition (D25).

**Status:** Phase 2 complete. 862 QVT-O tests (0 failures, 0 @Disabled, 1 @Tag("perf")). Phase 0–10 spec-conformance complete.

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
└── org.eclipse.fennec.m2x.qvto.tests/   # Tests (plain JUnit 5, 862 tests)
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
| Module structure | `Module`, `OperationalTransformation`, `Library`, `ModuleImport` |
| Operations | `ImperativeOperation`, `MappingOperation`, `Helper`, `Constructor`, `EntryOperation` |
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

Immutable engine configuration bundling OCL config, blackbox libraries, unit resolvers:

```java
QvtoConfiguration config = QvtoConfiguration.builder()
    .oclConfiguration(oclConfig)
    .blackboxLibrary(myBlackbox)
    .unitResolver(myResolver)
    .build();
QvtoEngine engine = new QvtoEngineImpl(config);
```

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
public interface QvtoBlackboxLibrary {          // D24
    String getModuleName();
    String getUnitQualifiedName();
    List<QvtoBlackboxOperation> getOperations();
    List<String> getUsedPackageURIs();
}

@ConsumerType
public interface QvtoUnitResolver {             // D27
    Optional<QvtoUnit> resolveUnit(String qualifiedName);
}

public sealed interface QvtoUnit {              // Sealed union
    record SourceUnit(String source, String name) implements QvtoUnit {}
    record CompiledUnit(OperationalTransformation transformation) implements QvtoUnit {}
}
```

**OSGi registration:** `@Reference(cardinality = MULTIPLE, policy = DYNAMIC)` whiteboard.
**Standalone:** `ServiceLoader<QvtoUnitResolver>` + programmatic `QvtoEngine.registerUnitResolver()`.

---

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
OperationalTransformation ast = parser.parse(source, "MyTransform.qvto");
// Or with custom EPackage.Registry:
OperationalTransformation ast = parser.parse(source, unitName, registry);
```

---

## 6. Engine Architecture (`qvto.engine`)

**Bundle:** `org.eclipse.fennec.m2x.qvto.engine`

### 6.1 QvtoEngineImpl — Facade

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
  └── OCL delegation → OclEngineImpl via snapshot OclContext
        └── handles: PropertyCallExp, IteratorExp, literals, stdlib, ...
```

**Dispatch flow:** `eval(OclExpression)` tries ImperativeDispatch first, then QvtOpDispatch,
then delegates to OclEngineImpl for standard OCL expressions.

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
  ├── owns: OclEngineImpl (created internally)
  │     └── registered provider: QvtoOperationProvider
  └── delegates OCL sub-expressions → OclEngineImpl

QvtoOperationProvider implements OclOperationProvider
  └── recognizes QVT-O helpers/mappings → re-enters QvtoEvaluator
```

Uses `QvtoOperationResolver` to find named operations (helpers, mappings, main) in the
transformation AST.

### 6.5 QvtoExtentManager — Model Extent Binding

Binds `ModelParameter` declarations to `QvtoModelExtent` instances:

- Positional mapping: ModelParameter → QvtoModelExtent
- Enforces read-only constraint on `in` parameters (§8.1.3.2)
- Accessed via `getExtent(ModelParameter)` or `getExtent(int index)`

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
| `QvtoOperationResolver` | Resolves named operations (helpers, mappings, main()) from transformation AST |
| `QvtoIntermediatePropertyStore` | Per-instance storage for `ContextualProperty` values (§8.1.10) |
| `QvtoAliasRegistry` | Tag alias resolution from EAnnotations (§8.3.19) |
| `QvtoModelOperations` | Built-in operations: `objectsOfType`, `rootObjects`, `metaClassName`, `clone`, etc. |

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

### 10.5 Imperative if (with Block)

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

For full decision records, see [Design Decisions](design-decisions.md) §20.

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
