# Future: OclAspectProvider — MetadataService Integration

## Status: Design Concept (not implemented)

Discussed 2026-02-20. Prepared by refactoring PropertyAccessorCache from singleton to instance
and introducing OclConfiguration + Builder.

## Vision

When `model.metadata` moves into `emf.osgi`:

```
EPackage registered
  → emf.osgi (EPackage.Registry)
  → MetadataService
  → OclAspectProvider.buildAspect(EPackage)
      → scan OCL annotations → populate ExpressionCache
      → build PropertyAccessorCache for all concrete EClasses
      → create pre-warmed OclEngine(s) (engine pool/factory)
```

## Current State (Bridge-Ready)

1. **OclConfiguration** (ocl.api) — bundles parser + expressionCache + operationProviders
2. **PropertyAccessorCache** — instance-based (not singleton), each engine has its own
3. **warmUp(EPackage)** — already does exactly what OclAspectProvider would call
4. **OclLruExpressionCache** — shared, injectable, already befüllbar

## TODOs When Implementing

### Step 1: New Module `org.eclipse.fennec.m2x.ocl.osgi` (or `ocl.engine.cfg`)
- OSGi: can be packaged into same bundle as `ocl.engine` via bndtools
- Contains: OclAspectProvider, OclEngineFactory

### Step 2: OclAccessorCache Interface (if needed)
- Currently PropertyAccessorCache is engine-internal
- If AspectProvider needs to share accessor caches across engines → extract interface to API
- Interface: `OclAccessorCache { void warmUp(EObject eo); }`
- Decision: Only extract if actually needed — YAGNI until then

### Step 3: OclAspectProvider
```java
@Component(service = AspectProvider.class)
public class OclAspectProvider implements AspectProvider<OclAspect> {
    @Reference OclExpressionParser parser;

    @Override
    public OclAspect buildAspect(EPackage pkg, PackageMetadata metadata) {
        OclLruExpressionCache cache = new OclLruExpressionCache(2048);
        OclConfiguration config = OclConfiguration.builder(parser)
            .expressionCache(cache)
            .build();
        OclEngine engine = OclEngines.create(config);
        engine.warmUp(pkg);
        return new OclAspect(config, engine);
    }
}
```

### Step 4: OclAspect Record
```java
public record OclAspect(OclConfiguration config, OclEngine warmEngine) {
    public OclEngine createEngine() {
        // Creates new engine sharing the same pre-warmed config
        return OclEngines.create(config);
    }
}
```

### Step 5: Engine Pool (Optional)
- If multiple concurrent engines needed → pool pattern
- OclEngine is lightweight (~340ns startup) so pool may not be necessary
- Real benefit: shared pre-warmed caches, not the engine instances themselves

## Key Design Decisions

- **Dependency direction**: ocl.osgi → emf.osgi (MetadataService), emf.osgi does NOT know OCL
- **Coupling**: Only via AspectProvider interface (DS whiteboard)
- **Cache isolation**: Each OclAspectProvider-created engine has its own accessor cache
  (separate from engines created without the provider)
- **Plain Java**: OclConfiguration + warmUp work without OSGi; AspectProvider is the OSGi bridge
