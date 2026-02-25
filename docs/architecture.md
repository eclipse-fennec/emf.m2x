# Fennec M2M Architecture

> Part of the [Development Guideline](development-guideline.md). See also: [Implementation Plan](implementation-plan.md), [Design Decisions](design-decisions.md), [OCL Architecture](ocl-architecture.md)

---

## 10. OSGi-Optional Architecture

### 10.1 Core Principle: Works Without OSGi, Enhanced With OSGi

All engines and services must be **usable as plain Java libraries** without any OSGi container. OSGi is an optional enhancement that provides service discovery, whiteboard patterns, and lifecycle management.

**Pattern: Pure Java core + OSGi adapter layer**

```
┌─────────────────────────────────────────────────────────────┐
│  .engine (pure Java, no OSGi imports)                       │
│                                                             │
│  OclEngineImpl implements OclEngine                         │
│    - constructor injection: new OclEngineImpl(config)        │
│    - works standalone as plain Java library                  │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────┴──────────────────────────────────┐
│  .engine (OSGi DS adapter, same bundle)                     │
│                                                             │
│  @Component(service = OclEngine.class)                      │
│  OclEngineComponent extends OclEngineImpl                   │
│    - @Reference injection for extensions                    │
│    - DS whiteboard for dynamic extension discovery          │
└─────────────────────────────────────────────────────────────┘
```

### 10.2 Standalone Usage (No OSGi)

For the OCL engine standalone example and full OSGi component architecture, see
[OCL Architecture §9](ocl-architecture.md#9-osgi-integration).

### 10.3 OSGi Usage (With DS)

For OCL-specific OSGi usage (prototype-scoped engine, expression cache, delegate
registration, configurator JSON), see
[OCL Architecture §9](ocl-architecture.md#9-osgi-integration).

### 10.4 API vs Implementation Packages

- `.api` and `.model` bundles **export** their packages (public API)
- `.parser` and `.engine` bundles export only their top-level service package
- Internal implementation packages use `*.internal` naming

---

## 11. Extension Points

The Eclipse implementations use `plugin.xml` extension points for registration. We replace these with **programmatic registration** (plain Java) and **OSGi DS whiteboard** (OSGi). This section catalogs all extension points discovered from the Eclipse plugin.xml files.

### 11.1 EMF Infrastructure Registrations

These are mandatory baseline registrations that Eclipse handles via `plugin.xml` → `org.eclipse.emf.ecore.generated_package` etc. We must handle them explicitly.

| Registration | Eclipse mechanism | Our approach (plain Java) | Our approach (OSGi) |
|---|---|---|---|
| **EPackage registration** | `org.eclipse.emf.ecore.generated_package` | `EPackage.Registry.INSTANCE.put(uri, pkg)` | fennec emf.osgi `EPackageConfigurator` (automatic) |
| **Resource.Factory for file extensions** | `org.eclipse.emf.ecore.extension_parser` | `Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(ext, factory)` | DS component |
| **Resource.Factory for protocols** | `org.eclipse.emf.ecore.protocol_parser` | `Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put(proto, factory)` | DS component |
| **URI mapping** (e.g. stdlib location) | `org.eclipse.emf.ecore.uri_mapping` | `URIConverter.URI_MAP.put(logicalUri, physicalUri)` | DS component |

Each of our `.model` bundles must register its EPackages. Each `.parser` bundle must register resource factories for its file extensions.

### 11.2 Language-Specific Extension Points

#### OCL Extensions

| Extension | Eclipse extension point | Without OSGi | With OSGi |
|---|---|---|---|
| **OCL Custom Operations** | `org.eclipse.ocl.pivot.standard_library` | `OclEngine.registerOperations(...)` | DS whiteboard: `@Component(service = OclOperationProvider.class)` |
| **OCL Standard Library** | `org.eclipse.ocl.pivot.standard_library` | Built-in, loaded at startup | DS whiteboard: `@Component(service = OclStandardLibraryContribution.class)` |
| **Complete OCL Documents** | `org.eclipse.ocl.pivot.complete_ocl_registry` | `OclEngine.registerCompleteOclDocument(contribution)` / `unregisterCompleteOclDocument(contribution)` | DS whiteboard: `@Component(service = CompleteOclContribution.class)` |
| **Model Extents / Resource Providers** | N/A (programmatic in Eclipse too) | Pass `Resource` / `ResourceSet` directly | DS `@Reference` |

#### QVT-O Extensions

| Extension | Eclipse extension point | Without OSGi | With OSGi |
|---|---|---|---|
| **Blackbox Libraries** | `org.eclipse.m2m.qvt.oml.javaBlackboxUnits` / `blackboxProvider` | `QvtoEngine.registerBlackbox(...)` | DS whiteboard: `@Component(service = QvtoBlackboxLibrary.class)` |
| **Unit Resolver** (finds .qvto files) | `org.eclipse.m2m.qvt.oml.unitResolverFactory` | `QvtoEngine.registerUnitResolver(...)` (classpath/filesystem-based) | DS whiteboard: `@Component(service = QvtoUnitResolver.class)` |
| **Deployed Transformations** | `org.eclipse.m2m.qvt.oml.runtime.qvtTransformation` | Load by URI/path directly | DS whiteboard: `@Component(service = QvtoTransformationContribution.class)` |

#### QVT-D Extensions

| Extension | Eclipse extension point | Without OSGi | With OSGi |
|---|---|---|---|
| **QVT-D Key Definitions** | N/A (in metamodel) | `QvtdEngine.registerKeys(...)` | DS whiteboard: `@Component(service = QvtdKeyProvider.class)` |
| **QVT Runtime Library** | `org.eclipse.ocl.pivot.standard_library` | Built-in, loaded at startup | DS whiteboard |

#### M2T Extensions

| Extension | Eclipse extension point | Without OSGi | With OSGi |
|---|---|---|---|
| **Custom Java Services** | `org.eclipse.acceleo.query.ide.servicesConfigurator` | `M2tEngine.registerService(...)` | DS whiteboard: `@Component(service = M2tService.class)` |
| **Module/Name Resolver** | `org.eclipse.acceleo.query.ide.resolverfactory` | `M2tEngine.registerResolver(...)` (classpath-based) | DS whiteboard: `@Component(service = M2tModuleResolver.class)` |
| **ResourceSet Configurator** | `org.eclipse.acceleo.query.ide.resourceSetConfigurator` | Pass configured `ResourceSet` directly | DS whiteboard: `@Component(service = M2tResourceSetConfigurator.class)` |

### 11.3 EMF Delegate Registrations

These are handled in detail in section 12, but listed here for completeness. Eclipse registers them via `org.eclipse.emf.ecore.invocation_delegate`, `setting_delegate`, `validation_delegate`, `query_delegate`, and `annotation_validator`.

| Delegate | URI | Eclipse extension point |
|---|---|---|
| InvocationDelegate | `http://www.eclipse.org/emf/2002/Ecore/OCL` | `org.eclipse.emf.ecore.invocation_delegate` |
| SettingDelegate | `http://www.eclipse.org/emf/2002/Ecore/OCL` | `org.eclipse.emf.ecore.setting_delegate` |
| ValidationDelegate | `http://www.eclipse.org/emf/2002/Ecore/OCL` | `org.eclipse.emf.ecore.validation_delegate` |
| QueryDelegate | `http://www.eclipse.org/emf/2002/Ecore/OCL` | `org.eclipse.emf.ecore.query_delegate` |
| EAnnotationValidator | `http://www.eclipse.org/emf/2002/Ecore/OCL` (+ variants) | `org.eclipse.emf.ecore.annotation_validator` |

**Note:** Eclipse OCL uses the `/Pivot` suffix variant (`http://www.eclipse.org/emf/2002/Ecore/OCL/Pivot`). We register under the base URI without suffix, as we are the only OCL implementation.

### 11.4 Whiteboard Pattern (OSGi)

Extensions register themselves as OSGi services. The engine discovers them dynamically:

```java
// Extension provider (user code)
@Component(service = QvtoBlackboxLibrary.class)
public class MyBlackbox implements QvtoBlackboxLibrary {
    @Override
    public String getName() { return "mylib"; }

    @Override
    public Map<String, Method> getOperations() { ... }
}

// Engine (consumer) - discovers all blackbox libraries
@Component(service = QvtoEngine.class)
public class QvtoEngineComponent extends QvtoEngineImpl {

    @Reference(cardinality = ReferenceCardinality.MULTIPLE,
               policy = ReferencePolicy.DYNAMIC)
    volatile List<QvtoBlackboxLibrary> blackboxLibraries;
}
```

### 11.5 Extension Interfaces (in `.api` bundles)

For OCL extension interfaces (`OclOperationProvider`, `CompleteOclContribution`,
`OclStandardLibraryContribution`), see [OCL Architecture §4.6](ocl-architecture.md#46-extension-interfaces).

```java
// In org.eclipse.fennec.m2x.qvto.api (implemented)
public interface QvtoBlackboxLibrary {
    String getName();
    List<QvtoBlackboxOperation> getOperations();
}

public interface QvtoUnitResolver {
    Optional<QvtoUnit> resolveUnit(String qualifiedName);
}

// In org.eclipse.fennec.m2x.m2t.api (planned)
public interface M2tService {
    String getName();
    Object invoke(String operationName, Object... args);
}

public interface M2tModuleResolver {
    Optional<M2tModule> resolveModule(String qualifiedName);
}
```

---

## 12. EMF Delegate Integration

EMF provides delegate registries that allow OCL expressions to be embedded directly in Ecore
models via `EAnnotation` — for validation constraints, derived features, and operation bodies.

The OCL engine implements all three delegate types (Validation, Setting, Invocation) under the
Fennec delegate URI `http://www.eclipse.org/fennec/m2x/ocl/1.0`, with both standalone
registration (`installDelegates()`) and emf.osgi whiteboard support.

For full details (implementation classes, standalone/OSGi registration, caching, delegate
options, emf.osgi properties), see [OCL Architecture §8](ocl-architecture.md#8-emf-delegate-integration).

For the emf.osgi delegate registry infrastructure analysis, see
[emf-delegate-registries.md](emf-delegate-registries.md).

---

## 13. Language Server Protocol (LSP) - Future Phase

### 13.1 Overview

As a **later phase** (after core engines are stable), provide LSP implementations for all three languages. This enables IDE support in VS Code, Eclipse, IntelliJ, and any LSP-capable editor.

### 13.2 Planned Modules

```
workspace/
├── org.eclipse.fennec.m2x.ocl.lsp/     # OCL Language Server
├── org.eclipse.fennec.m2x.qvto.lsp/    # QVT-O Language Server
├── org.eclipse.fennec.m2x.qvtd.lsp/   # QVT-R Language Server
└── org.eclipse.fennec.m2x.m2t.lsp/     # MOFM2T Language Server
```

### 13.3 LSP Features Per Language

| Feature | OCL | QVT-O | QVT-R | MOFM2T |
|---------|-----|-------|-------|--------|
| Syntax highlighting | Yes | Yes | Yes | Yes |
| Error diagnostics | Yes (ANTLR4 errors) | Yes | Yes | Yes |
| Completion | Types, properties, operations, iterators | Mappings, helpers, model types | Relations, domains, patterns | Templates, OCL expressions |
| Hover | Type information, documentation | Mapping signatures | Relation signatures | Template parameters |
| Go to definition | Variable/type definitions | Mapping/helper definitions | Relation/key definitions | Template definitions |
| Find references | Variable/type usage | Mapping calls | Relation invocations | Template calls |
| Formatting | Yes | Yes | Yes | Yes |
| Validation | OCL well-formedness rules | QVT-O type checking | QVT-R domain/pattern rules | Template parameter types |

### 13.4 Implementation Approach

- Use [Eclipse LSP4J](https://github.com/eclipse-lsp4j/lsp4j) as the LSP protocol library (Java, lightweight)
- Reuse ANTLR4 parsers for incremental parsing and error recovery
- Reuse EMF metamodels for type information and completion
- Each LSP server is a standalone Java process (no Eclipse platform required)
- Can also run embedded in OSGi for Eclipse IDE integration

### 13.5 Dependencies on Core

LSP modules depend on the parser and engine modules being stable. They are **not part of the initial implementation phases**.

---

## 14. Security Hardening

The OCL engine implements configurable resource limits to prevent denial-of-service when
evaluating untrusted expressions (recursion depth, collection sizes, closure iterations,
regex pattern lengths).

See [OCL Architecture §10](ocl-architecture.md#10-security-hardening) for the full threat
model, attack vector catalogue, and embedder guidance.

---

## 15. Performance & Thread-Safety Architecture

For OCL-specific performance details (thread-safety guarantees, caching architecture,
benchmark results, optimization stages, warm-up), see
[OCL Architecture §7](ocl-architecture.md#7-caching-architecture) and
[OCL Architecture §11](ocl-architecture.md#11-performance).
