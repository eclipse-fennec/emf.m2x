# OCL Constraint Validation Across OSGi Bundles

## Problem

When validating OCL constraints on EMF model instances where the **model lives in a different bundle** than the OCL engine, a `NoClassDefFoundError` occurs:

```
An exception occurred while delegating evaluation of the 'ValidPhoneNumber'
constraint on '...impl.PersonImpl@ac2625{#//}':
java.lang.NoClassDefFoundError: .../impl/PersonImpl
```

This affects both validation paths:
- **`Diagnostician.INSTANCE.validate(eObject)`** — reports an error diagnostic wrapping the `NoClassDefFoundError`
- **`engine.evaluate(expression, OclContext.of(eObject))`** — throws `NoClassDefFoundError` directly

## Use Case

A typical scenario is a REST endpoint that receives an EMF `EObject` and validates it using OCL constraints defined in the model's `.ecore`:

```java
@Component(service = MyResource.class)
public class MyResource {

    @Reference
    private OclEngine engine;

    public Response validate(EObject eObject) {
        Diagnostic diagnostic = Diagnostician.INSTANCE.validate(eObject);
        // ...
    }
}
```

The model (e.g. `Person`) is generated in a separate bundle (`ocl.example.model`) and has OCL constraints declared via EAnnotations:

```xml
<!-- EPackage-level: declare the validation delegate -->
<eAnnotations source="http://www.eclipse.org/emf/2002/Ecore">
  <details key="validationDelegates"
           value="http://www.eclipse.org/fennec/m2x/ocl/1.0"/>
</eAnnotations>

<!-- EClass-level: declare constraint names + OCL expressions -->
<eClassifiers xsi:type="ecore:EClass" name="Person">
  <eAnnotations source="http://www.eclipse.org/emf/2002/Ecore">
    <details key="constraints" value="ValidPhoneNumber AgeAppropriate"/>
  </eAnnotations>
  <eAnnotations source="http://www.eclipse.org/fennec/m2x/ocl/1.0">
    <details key="ValidPhoneNumber" value="self.phone.matches('^\\d{10}$')"/>
    <details key="AgeAppropriate" value="self.age > 18"/>
  </eAnnotations>
</eClassifiers>
```

## Root Cause

The OCL engine uses `PropertyAccessorCache` to create fast property accessors via `LambdaMetafactory`. For performance, it bypasses EMF's reflective `eGet()` and generates direct getter lambdas (e.g. `PersonImpl.getAge()`).

The problem was in `PropertyAccessorCache.java`:

```java
// BEFORE: Lookup rooted in the engine bundle's classloader
private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
```

This `Lookup` is bound to the **engine bundle's** classloader context. When `LambdaMetafactory` generates bytecode for a getter on `PersonImpl`, the generated lambda references a class (`PersonImpl`) that the engine bundle cannot resolve — hence `NoClassDefFoundError`.

The `catch (Throwable)` in `createAccessor()` was intended to catch such failures and fall back to `eGet()`, but the error can occur **after** lambda creation, during invocation of the generated `CallSite`, where it escapes the catch block.

### Call chain

```
OclValidationDelegateFactory.evaluateConstraint()
  → engine.evaluate()
    → OclEvaluator.casePropertyCallExp()
      → PropertyAccessorCache.getAccessor()
        → createLambdaAccessor()
          → LOOKUP.unreflect(getter)          // may fail here
          → LambdaMetafactory.metafactory()   // or here
          → site.getTarget().invokeExact()     // or here (escapes catch)
```

## Fix

Use `MethodHandles.privateLookupIn()` to obtain a `Lookup` rooted in the **model class's own module/classloader**:

```java
// AFTER: Lookup rooted in the model class's classloader
private static PropertyAccessor createLambdaAccessor(Class<?> implClass, Method getter)
        throws Throwable {
    MethodHandles.Lookup lookup =
        MethodHandles.privateLookupIn(implClass, ENGINE_LOOKUP);
    MethodHandle mh = lookup.unreflect(getter);

    CallSite site = LambdaMetafactory.metafactory(
        lookup,          // now uses the model's context
        "apply",
        MethodType.methodType(Function.class),
        MethodType.methodType(Object.class, Object.class),
        mh,
        MethodType.methodType(autobox(getter.getReturnType()),
                              getter.getDeclaringClass())
    );

    Function<Object, Object> fn =
        (Function<Object, Object>) site.getTarget().invokeExact();
    return fn::apply;
}
```

The `ENGINE_LOOKUP` (static lookup from the engine bundle) is still needed as the bootstrap caller for `privateLookupIn()`, but the resulting lookup has the model class's access context, so the generated lambda can resolve the model's implementation classes.

**Changed file:** `org.eclipse.fennec.m2x.ocl.engine/src/org/eclipse/fennec/m2x/ocl/engine/internal/PropertyAccessorCache.java`

## Test Coverage

`EMFAnnotationOSGiTest` in `org.eclipse.fennec.m2x.ocl.tests` validates the cross-bundle scenario using a generated `Person` model from the separate `org.eclipse.fennec.m2x.ocl.example.model` bundle:

| Test | What it verifies |
|------|-----------------|
| `diagnostician_validPerson_noErrors` | Valid Person passes all OCL constraints via `Diagnostician` |
| `diagnostician_invalidPhone_error` | Invalid phone fails `ValidPhoneNumber` constraint |
| `diagnostician_underageAge_error` | Age < 18 fails `AgeAppropriate` constraint |
| `oclEngine_evaluateConstraint_onCrossBundleModel` | Direct `engine.evaluate()` works on cross-bundle model |
| `oclEngine_evaluateConstraint_crossBundleModel_violatedConstraint` | Violated constraint returns `false` |
