# OCL Implementation Architecture — Fennec M2M (Phase 1)

> Consolidated reference for the OCL implementation.

---

## 1. Overview & Status

The OCL engine implements **OCL v2.5** (backward compatible with v2.4) as a pure Java library,
fully decoupled from the Eclipse platform. It serves as the expression language foundation
for all higher-level engines (QVT-O, QVT-D, MOFM2T).

**Status:** Phase 1 complete. Spec-conformance verified against OCL v2.4/v2.5.

**Key properties:**
- No Pivot layer — direct Ecore access (D16)
- OSGi-optional — works as plain Java library
- Configurable evaluation — strict (spec-compliant) or lenient (pragmatic) (D14)
- EMF delegate integration — validation, setting, invocation delegates
- ANTLR4-based parser with grammar composition support

---

## 2. Module Structure

```
workspace/
├── org.eclipse.fennec.m2x.ocl.model/    # OCL EMF metamodel (Ecore-based AST)
├── org.eclipse.fennec.m2x.ocl.api/     # Public API interfaces
├── org.eclipse.fennec.m2x.ocl.parser/  # ANTLR4 parser
├── org.eclipse.fennec.m2x.ocl.engine/  # Evaluator / interpreter
└── org.eclipse.fennec.m2x.ocl.tests/   # Tests (plain JUnit 5)
```

### 2.1 Dependency Graph

```
            ┌──────────┐
            │ ocl.model │  ← EMF metamodel, generated into src/
            └────┬─────┘
                 │
            ┌────┴────┐
            │ ocl.api  │  ← Public API interfaces
            └────┬────┘
                 │
       ┌─────────┼─────────┐
       ▼                   ▼
┌──────────┐        ┌──────────┐
│ocl.parser│        │ocl.engine│  ← engine depends on api+model, NOT parser
└──────────┘        └──────────┘
       │                   │
       └─────────┬─────────┘
                 ▼
          ┌──────────┐
          │ocl.tests │
          └──────────┘
```

**Cross-component consumers:**
- `qvto.model/engine` depends on `ocl.model/engine`
- `qvtd.model/engine` depends on `ocl.model/engine`
- `m2t.model/engine` depends on `ocl.model/engine`

---

## 3. OCL Metamodel (`ocl.model`)

**Bundle:** `org.eclipse.fennec.m2x.ocl.model`

Pure model project — EMF-generated code lives in `src/` (no `src-gen/`).

| Property | Value |
|----------|-------|
| nsURI | `http://www.eclipse.org/fennec/m2x/ocl/1.0` |
| basePackage | `org.eclipse.fennec.m2x.model` → generates to `org.eclipse.fennec.m2x.model.ocl` |
| Classifiers | 48 EClasses + 2 EEnums = 50 total |
| EPackage | Single `ocl` package, no subpackages |

### 3.1 Design Choices

- **Direct Ecore references:** `EStructuralFeature`, `EOperation`, `EClassifier`, `EEnumLiteral`
  as attribute types — no wrapper indirection.
- **`name: String` on `OperationCallExp`** (D19): `referredOperation: EOperation` can be null
  for stdlib operations. The `name` field stores the operation name (e.g. `size`, `+`,
  `oclIsKindOf`) for stdlib dispatch. Parser always sets it.
- **v2.5 additions:** `MapType`, `MapLiteralExp`, `MapLiteralPart`, `isSafe: Boolean` on `CallExp`.
- **Blueprint:** Based on `EssentialOCL.emof` from the OCL spec.
- **Types are classifiers (#154):** `OclType` extends `EClassifier`, `PrimitiveType` extends
  `EDataType` — as in OCL v2.4 §8.2 (types are Classifiers) and in the Eclipse OCL Ecore binding.
  That is what lets the standard library be an ordinary `EPackage` (below), and what let the
  `ClassifierType` wrapper go (#156): `OclExpression.type`, `Variable.type`, `TypeExp.referredType`
  and the element types of collections, maps and tuples are typed `EClassifier` and point at the
  metamodel's `EClass` or `EDataType` directly — as Eclipse OCL's `TypedElement.eType` does. An OCL
  operation's owner is an `EClassifier` for the same reason (`OclOperation`).

### 3.2 The Standard Library (`OclStandardLibrary`, `ocl.api`)

One instance of every predefined type — `Integer`, `Real`, `String`, `Boolean`,
`UnlimitedNatural`, `OclAny`, `OclVoid`, `OclInvalid` — in an `EPackage` with the nsURI
`http://www.eclipse.org/fennec/m2x/ocl/stdlib/1.0`, registered in `EPackage.Registry.INSTANCE`
on first use. The counterpart of Eclipse OCL's `oclstdlib.ecore`.

Types are like classes, not like values: there is one `Integer` in the language however many
numbers a transformation contains. Until #154 the parser created a fresh `PrimitiveType` at every
use site, and so did the evaluator, the MOFM2T standard library and the QVT-O operation provider —
four sets of `Integer`, none contained anywhere, every one a satellite in a compiled unit. Now a
reference to `Integer` is an external reference like one to `EString`: it serializes as
`…/ocl/stdlib/1.0#//Integer`, resolves from any resource set, and enters a fingerprint as
`nsURI#name` (#138). `PrimitiveType.instanceClass` is `Long` for `Integer` and `Double` for
`Real` — what `evaluate()` hands back for a Real. Inside an evaluation the numbers are wider than
that: an Integer is a `BigInteger` once it leaves the 64-bit range, a Real is a `BigDecimal`
(§6.5, #322).

The library initializes the OCL metamodel with itself, so both are in the global registry before
any name is resolved; in an Eclipse IDE that registration comes from `plugin.xml` instead (#157).

---

## 4. API Design (`ocl.api`)

**Bundle:** `org.eclipse.fennec.m2x.ocl.api`

### 4.1 Core Interfaces

```java
@ProviderType
public interface OclEngine {
    // Parsing
    OclExpression parse(String expression, EClassifier contextType) throws OclParseException;
    List<Constraint> parseDocument(String oclDocument) throws OclParseException;

    // Evaluation
    Object evaluate(OclExpression expression, OclContext context);
    Object evaluate(OclExpression expression, OclContext context, OclEvaluationOptions options);
    Object evaluate(String expression, OclContext context) throws OclParseException;
    OclResult evaluateWithDiagnostics(OclExpression expression, OclContext context,
            OclEvaluationOptions options);

    // Validation
    List<Diagnostic> validate(OclExpression expression, EClassifier contextType);

    // Extension management (D29: runtime registration removed — use OclConfiguration)
    void registerCompleteOclDocument(CompleteOclContribution contribution);
    void unregisterCompleteOclDocument(CompleteOclContribution contribution);
}
```

### 4.2 OclConfiguration (Builder Pattern)

Bundles parser + expression cache + operation providers. Primary constructor for `OclEngine`.

```java
OclConfiguration config = OclConfiguration.builder(new OclParserSupport())
    .expressionCache(OclLruExpressionCache.ofSize(1024))
    .operationProvider(myProvider)
    .build();
OclEngine engine = OclEngines.create(config);
```

Multiple engines sharing the same configuration share the expression cache but have
independent `PropertyAccessorCache` instances.

### 4.3 OclContext

Bundles evaluation context (replaces bare `EObject` parameter):

```java
public record OclContext(
    EObject self,                      // The context object ('self')
    OclModelExtent extent,             // For allInstances() — nullable
    Map<String, Object> variables,     // Global variables
    ResourceSet resourceSet,           // Package resolution — nullable
    BiFunction<EObject, String, Object> propertyInterceptor,              // nullable
    BiFunction<OclExpression, Map<String, Object>, Object> foreignExpressionEvaluator // nullable
) {
    public static OclContext of(EObject self) { ... }
    public static OclContext of(EObject self, OclModelExtent extent) { ... }
    public static OclContext of(EObject self, Map<String, Object> variables) { ... }
}
```

The last two are hooks for a language built on OCL. `propertyInterceptor` answers property
reads before `eGet()` (QVT-O intermediate properties). `foreignExpressionEvaluator` evaluates
a node that is not OCL's but sits inside an OCL expression — QVT-O's `list[T]->first()` or
`x.late resolveone(T)->asOrderedSet()`: the OCL evaluator hands the node over together with
the iterator and let variables it has bound since the context, and uses the returned value.
Without it such a node is an `Unsupported expression type` error (#296).

An operation call whose `referredOperation` the parser could not resolve — the source's type
was unknown — is still dispatched to the object's own `EOperation` of that name and arity at
run time, before the standard library and custom providers (§7.5.4, #300).

### 4.4 OclResult (D20)

Three-state result: valid value, null (OclVoid), or OclInvalid. No `Optional` — OCL's
three-value semantics don't map to Java's two-state Optional.

```java
public record OclResult(Object value, List<Diagnostic> diagnostics) { ... }
```

### 4.5 OclEvaluationOptions

Immutable record with `with*()` copy methods. Configurable per evaluation.

```java
public record OclEvaluationOptions(
    NullHandling nullHandling,          // STRICT or LENIENT
    ErrorRecovery errorRecovery,        // FAIL_FAST or COLLECT_ERRORS
    int maxDepth,                       // Default 1,000
    Duration timeout,                   // Default: none
    int maxCollectionSize,              // Default 1,000,000
    int maxClosureIterations,           // Default 100,000
    int maxRegexLength,                 // Default 1,000
    int maxStringLength,                // Default 10,000,000
    boolean customOperationsEnabled,    // D29, default false
    List<OclOperationProvider> additionalProviders,
    boolean useEMFTypes,                // Default false
    boolean specStrict                  // Default false, see §13.8 (#316, #326)
) {
    public static OclEvaluationOptions strict() { ... }
    public static OclEvaluationOptions lenient() { ... }
}
```

| Mode | Behavior | Use case |
|------|----------|----------|
| `STRICT` + `FAIL_FAST` | Null → OclInvalid, first error stops | Validation, constraints |
| `LENIENT` + `COLLECT_ERRORS` | Null *navigation* → null, errors collected | M2T, interactive console |

**What `LENIENT` covers, and what it does not** (#112). Navigating from a `null` source answers
`null`, and a chain keeps propagating it: `self.employer.name` with `employer` unset is `null`
rather than `OclInvalid`. **Calling** on a `null` source is a different matter — the call proceeds
so that the standard library and the registered operation providers still get to dispatch, and
what none of them can handle ends as `OclInvalid` one step later. `null + 1` has no lenient
reading, and `null.size()` none either.

Neither of them is quiet. A `null` source is reported where it is refused (`Null source for
operation …` under STRICT) or where no provider took it (`Unknown operation: size on null`
under LENIENT); a `null` *argument* to a standard-library operation is reported in either mode,
`Null argument for operation '+'`, and the value stays `invalid` (OCL v2.4 §11.2.3). The argument
case was, until #270, the one path on which an expression turned invalid without a diagnostic — a
generator then wrote the word `OclInvalid` into its document while the run counted as successful.

That refusal is the standard library's alone (#306). `null` conforms to every type (§8.2,
§11.2.1), so an operation the model, an OCL document or the host language defines — a model
`EOperation`, a QVT-O helper, query or blackbox — receives it and its body decides. With a `null`
argument the evaluator therefore tries the model operation and the custom providers, and reports
`Null argument for operation …` only when none of them answers. A generated `eInvoke` that cannot
take the `null` (an `int` parameter) is an `Operation invocation failed` error, not an exception.

That second half is not a leftover: QVT-O evaluates every expression leniently and resolves its
module-level helpers — which have no `self` — through exactly that path (`QvtoEvaluator:587`).
Making a call on `null` an error breaks 86 QVT-O tests, which is how the boundary was located.
Until #112 the navigation half did nothing at all: the `LENIENT` branch returned Java `null`,
which its caller could not tell from "proceed with evaluation".

**Lenient navigation is not silent.** Each occurrence is recorded as a `WARNING` — leniency keeps
a result usable, it does not hide that something was missing. A warning rather than an error,
because an unset value rendering as nothing is ordinary where leniency is chosen, so the
evaluation stays successful. M2T selects `LENIENT` (D14, #114) and forwards these diagnostics into
its own result, which is what lets a template author see which expression came up empty instead of
finding a gap in the generated document.

**Nor is an `invalid` silent where it arises** (#304). A standard-library operation answers
`invalid` for values it is not defined for: `first()` of an empty collection, `at()` out of
range, a division by zero (OCL v2.4 §11). The value is right, but it used to travel on unnoticed
and end as the text `OclInvalid`. Where it originates, from valid source and arguments, the
evaluator records a `WARNING` naming the operation and the values, e.g. `'first' on an empty
Sequence is invalid`. It does this in either mode, and only there: propagating `invalid`
further says nothing more, and a call tested on the spot (`coll->first().oclIsUndefined()`,
`(a / b).oclIsInvalid()`) expects it and stays quiet. A warning, not an error, because
`invalid` is a value of the language. Measured before it was introduced: across all suites
the origins came from tests that produce `invalid` on purpose, plus two QVT-O transformations
that really did call `first()` on an empty collection or an operation on `null`.

### 4.6 Extension Interfaces

```java
@ConsumerType
public interface OclOperationProvider {
    List<OclOperation> getOperations();
}
```

**OSGi wiring:** `OclEngineComponent` binds **every** registered `OclOperationProvider` via
`@Reference(name="operationProvider", cardinality = MULTIPLE)`. There is no placeholder provider and
none is needed: with nothing registered the list is simply empty. To contribute operations, register
a provider as a DS component; narrow the set with `operationProvider.target` if a given engine should
see only some of them (see [User Guide §11.3](ocl-user-guide.md#113-osgi-registration)).
The D29 `customOperationsEnabled` gate must also be enabled.

```java
@ConsumerType
public interface CompleteOclContribution {
    URI getDocumentUri();
    String getDocumentText();
}

@ConsumerType
public interface OclStandardLibraryContribution {
    List<OclOperation> getOperations();
    List<OclType> getTypes();
}
```

### 4.7 OclExpressionCache

```java
@ProviderType
public interface OclExpressionCache {
    OclExpression get(String expression, EClassifier contextType);
    void put(String expression, EClassifier contextType, OclExpression parsed);
    void invalidate(String expression, EClassifier contextType);
    void invalidateAll();
    long size();
    long hitCount();
    long missCount();
}
```

### 4.8 OclExpressionParser (D21)

Decouples engine from parser. Named to avoid collision with ANTLR-generated `OclParser`.
Engine depends only on `ocl.api`, not `ocl.parser`. OSGi `@Capability`/`@Requirement`
annotations for service filtering.

---

## 5. Parser Architecture (`ocl.parser`)

**Bundle:** `org.eclipse.fennec.m2x.ocl.parser`

### 5.1 Structure

```
org.eclipse.fennec.m2x.ocl.parser/
├── src/
│   └── org/eclipse/fennec/m2x/ocl/parser/
│       ├── OclParserSupport.java       # Entry point (implements OclExpressionParser)
│       ├── OclAstBuilder.java          # Parse tree → EMF AST (visitor)
│       ├── OclDocumentBuilder.java     # Complete OCL document → Constraints
│       ├── OclEnvironment.java         # Name resolution with nested scopes
│       ├── OclErrorListener.java       # Error collection as Resource.Diagnostic
│       └── OclParseDiagnostic.java     # Resource.Diagnostic implementation
├── src-gen/                            # ANTLR4-generated parser code
│   └── org/eclipse/fennec/m2x/ocl/parser/
│       ├── OclParser.java
│       ├── OclLexer.java
│       ├── OclVisitor.java
│       └── OclBaseVisitor.java
└── grammar/
    └── Ocl.g4                          # Combined lexer/parser grammar
```

### 5.2 ANTLR4 Build Integration

Parser projects use bnd's `-generate:` instruction with `system=` to invoke the ANTLR4 tool:

```bnd
antlr4-version = 4.13.2

antlr4.tool.cp = \
	${repo;org.antlr:antlr4;${antlr4-version}}:\
	${repo;org.antlr:antlr4-runtime;${antlr4-version}}:\
	${repo;org.antlr:antlr-runtime;3.5.3}:\
	${repo;org.antlr:ST4;4.3.4}:\
	${repo;org.abego.treelayout:org.abego.treelayout.core;1.0.3}:\
	${repo;com.ibm.icu:icu4j;72.1}

-generate: \
	grammar/Ocl.g4; \
		output=src-gen; \
		system='java -cp ${antlr4.tool.cp} org.antlr.v4.Tool \
			-visitor -no-listener \
			-package org.eclipse.fennec.m2x.ocl.parser \
			-o src-gen/org/eclipse/fennec/m2x/ocl/parser \
			${project}/grammar/Ocl.g4'
```

Key points:
- `${project}/grammar/Ocl.g4` (absolute path) prevents ANTLR4 from mirroring subdirectories
- Package set via ANTLR4 `-package` flag, **not** via `@header` block
- `-visitor -no-listener` — AST builder uses visitor pattern
- Grammar bundled in JAR via `-includeresource.grammar` for downstream `import`

### 5.3 Two-Phase Parsing (D8)

1. **Parse:** ANTLR4 grammar → parse tree (`ParserRuleContext` nodes)
2. **Build:** `OclAstBuilder` (extends `OclBaseVisitor`) → EMF model instances

Parser resolves types during AST construction for hot-path performance.

### 5.4 Grammar Design

- Follows OCL v2.4 concrete syntax (spec §9.3)
- ANTLR4 labeled alternatives for clean visitor dispatch
- Operator precedence encoded via rule ordering (ANTLR4 handles left-recursion)
- Reference: Acceleo `Query.g4` for expression parsing patterns

**Operator Precedence** (spec §7.4.8, as `Ocl.g4` encodes it):

1. `@pre` → 2. `.`, `->`, `?.`, `?->` → 3. `not`, `-` (unary) → 4. `*`, `/` →
5. `+`, `-` → 6. `<`, `>`, `<=`, `>=` → 7. `=`, `<>` → 8. `and` → 9. `or` → 10. `xor` →
11. `implies`

§7.4.8 lists `if-then-else-endif` between `+`/`-` and the relational operators. In the grammar
it is a primary expression, like `let` and a parenthesised expression: `endif` closes it, so it
has no binding strength to place in the list, and `1 + if c then 2 else 3 endif` parses as the
operand of `+`. A `let` extends as far right as possible: `1 + let x : Integer = 2 in x + 3` is
`6` (#330). `not` and unary minus bind weaker than `.`: `not 1 = 1` is `(not 1) = 1`, `invalid`,
and `-1.abs()` is `-(1.abs())`, `-1`. §7.4.8 puts `and`, `or` and `xor` on one level; the
grammar gives them three, `and` binding strongest — the same result for left-to-right chains
such as `true or true xor true` (`false`), different for `a or b and c`, which reads as
`a or (b and c)`.

### 5.5 Grammar Composition

| Language | Grammar File | Extends |
|----------|-------------|---------|
| OCL | `Ocl.g4` | — (base grammar) |
| QVT-O | `QvtOperational.g4` | `import Ocl;` |
| QVT-R | `QvtRelations.g4` | `import Ocl;` |
| MOFM2T | `Mofm2t.g4` | `import Ocl;` |

### 5.6 Error Handling

- Custom `ANTLRErrorListener` collects parse errors with source positions
- All errors collected in a single pass (ANTLR4 auto-recovery)
- ANTLR4 `RecognitionException` subtypes mapped to `OclParseException`

### 5.7 Classifier Resolution — Scope, not Registry (D42, #158)

Classifier names in an expression (`self.oclIsTypeOf(Novel)`) and in a Complete OCL document
(`context Novel`) are resolved at parse time. Two things take part: a **scope** and a **registry**.

The scope is what a simple name resolves in — the context type's own package, and the packages the
unit **declares**:

| Language | Declaration | Reaches the scope through |
|---|---|---|
| OCL expression | the context type | its package |
| Complete OCL | `import path` (nsURI, package name or classifier path); `package … endpackage` | `OclDocumentBuilder` → `importPackage` |
| QVT-O | `modeltype M uses '…'`; `metamodel` declarations; the unit's intermediate classes | `QvtoUnitBuilder.declaredPackages` |
| QVT-R | the typed models of the transformation signature | `TypedModel.usedPackage` → `importPackage` |
| MOFM2T | `[module m(A, B)]` | `Module.input` → `importPackage` |

The registry — `EPackage.Registry.INSTANCE` for `new OclParserSupport()`, the given one for
`new OclParserSupport(registry)`, the resource set's own for `parseDocument(String, ResourceSet)` —
is consulted for **declarations and qualified names only**: `import 'http://…'`, `modeltype M uses
'name'`, `company::Person`. Looking a declared name up is what a declaration is for.

Until #158 a simple name that resolved in no scope fell back to a scan of **every package in the
registry**. Which type a bare name meant then depended on what else was registered in the JVM —
including the AST metamodels of the four languages, so that `let t : OclType = …` resolved to the
OCL metamodel's own `EClass` `OclType`, and only when that metamodel happened to be registered
before the parse. OCL v2.4 §9.3 resolves a simple name in the context namespace and what is imported
into it; §11.2.1 gives `oclType()` the result type `Classifier`, not `OclType`. The scan is gone;
the expression above is an error, and a document, a transformation or a module names the packages
it uses.

A qualified name that resolves nowhere is recorded as a diagnostic by `AbstractExpressionBuilder`
and turned into an `OclParseException` by `OclParserSupport` once the whole unit has been visited —
every unresolved name at once, the same contract syntax errors have. The message follows what the
name turned out to be, as in Eclipse OCL: *Unknown enumeration literal* when the path names an
enumeration, *Unknown type* otherwise.

An unqualified name that matches no property and no classifier stays an external variable reference
(`resolveImplicitProperty`), which `OclContext` binds at evaluation time — **except in a type
position**, where `resolveOperation` rejects it (`rejectUnknownTypeArgument`). The argument of
`oclIsKindOf`, `oclIsTypeOf` and `oclAsType` is a type by definition (§13.2: a `TypeExp` refers to
an *existing* type), so a name that names no type has no second reading. A name that *is* bound
stays accepted — whether a bound value may serve as a type belongs to evaluation (#70).

---

### 5.8 Property Resolution — resolved, marked, or reported on request (#153)

`AbstractExpressionBuilder.resolveProperty` binds a navigation to the feature of the source's type.
Where the type is not known at parse time it leaves a **marked placeholder**: an `EAttribute` with
the name as written and an annotation `…/ocl/unresolved`. The evaluator looks such a property up by
name on the runtime object, which is what a template over a dynamic model needs; the annotation is
what lets the fingerprint, a validator or a reader of a stored unit tell a placeholder from a
resolved feature (`AbstractExpressionBuilder.isUnresolvedProperty`).

A missing property is **not** reported by default, and the test suite says why: a Complete OCL
`def:` registered by an earlier parse call, a library property such as `oclLocale`, and a feature of
the runtime type where the static one is `EObject` are all properties the parser cannot see and the
evaluator finds. `OclParserSupport.strictPropertyResolution(true)` is what a caller that hands over
the whole document — an editor, a validator, a build check — turns on to get `Unknown property
(Person::nam)` at its position. Within one document a `def:` is seen wherever it stands: the names
are collected before a single expression is built, so an invariant may use a `def:` that follows it.

QVT-R binds its property template items to the referred class of the object template
(§7.11.2.4) once that class is known, and reports a class without such a property at the item's
position; before #153 the placeholder was never replaced and travelled into the compiled unit as a
satellite. M2T resolves through the nested parameter scope since #154/#158. Both are pinned by tests
that count the placeholders left in a parsed unit.

### 5.9 Enum literals are the model's values (#133)

`EnumLiteralExp` evaluates to `EEnumLiteral.getInstance()` — the generated Java constant for a
generated package, the literal itself for a dynamic one. That is what `eGet` returns for an
enum-typed attribute in either world, so `status = Status::DEPRECATED` compares equal; returning the
`EEnumLiteral` made it false against every generated model. `OclEqualityUtil` bridges the two
representations in `oclEquals` and in `lookupKey`, so a literal and its constant also land in the
same bucket of a unique collection. A path-qualified literal (`pkg::Enum::LITERAL`, OCL v2.4 §7.5.3)
resolves; a literal the enumeration does not have is a parse error naming it.

### 5.10 Static typing (GAP-3, #378)

Every expression the builder creates gets its type while it is built — the builder needs them
there, because the implicit collect (§7.6.2) and the type of an iterator variable are decided
during construction. The rules live in the exported package `org.eclipse.fennec.m2x.ocl.parser.typing`,
shared by the expression builders of OCL, QVT-R and MTL (and QVT-O in stage B):

| Class | Role |
|---|---|
| `OclTypeRules` | Conformance (§8.2.1, §11.2–§11.7), common supertype, normalization of Ecore data types (`EString` is `String`, `EInt`/`ELong`/… `Integer`, `EDouble`/`EFloat` `Real`, `EBoolean` `Boolean`) |
| `OclLibrarySignatures` | The library operations (§11) in a generic notation — `T` element type, `Self` receiver, `SelfKind(P)`, `Common(P, Q)`, `Flat(P)`, `ArgN`, `ArgElemN`, `TypeArgN`, `Tuple(a : P)` — so that the result type follows from the signature, as in Eclipse Pivot's `oclstdlib`, not from code per operation as in Eclipse OCL classic. Looked up along the receiver's chain (`Set` → `Collection` → `OclAny`, `UnlimitedNatural` → `Integer` → `Real` → `OclAny`); the first signature whose parameters the arguments conform to wins. `extend(...)` adds a language's operations. Beyond §11 it holds OCL 2.5's generalized `union`/`intersection` and what the evaluator implements beyond that (`Set - Bag`, `Bag - Bag`, …), marked as such |
| `OclTypeChecker` | The same rules over a finished AST, for `OclEngine.validate` (`OclParserSupport.validate`) |

**Gradual.** `null` is an unknown type and conforms to everything: an external variable, a
blackbox result, a value of a dynamic model is never an error. On a source of unknown type an
operation is still typed where every signature of that name and arity answers the same fixed type
(`v.size()` is an `Integer`).

**What is a parse error** (§8.3.7): a library operation none of whose signatures takes the
arguments (`1 + 'a'`, `'abc'.substring()`), an argument of a metamodel operation or of a `def:`
operation that does not conform to its parameter, a declared type its initial value does not
conform to, an `if` condition and the body of `select`/`reject`/`forAll`/`exists`/`any`/`one`
that are known not to be Boolean, a known non-type as the argument of `oclIsKindOf`/`oclAsType`.
`AbstractExpressionBuilder.getTypeFindings()` lists them apart from the other diagnostics. The
evaluator keeps its run-time checks: they are what a value of unknown type meets.

**Declared and inferred.** A variable keeps the type it was declared with; a `let` without one is
used with its initial value's type, but its `Variable.type` stays empty, so `specStrict` still
reports the missing declaration (#326).

**`def:`** A Complete OCL document collects the signatures of its defs before it builds an
expression (`OclDocumentBuilder.collectDefinitions`), so a call of a defined operation — also one
above its def — has the declared result type and its arguments are checked (#324).

Measured on all suites before the switch (#378): untyped nodes went from 69.5 % to 2.8 % of the
OCL expressions; the only type errors were in tests that check run-time behaviour of ill-typed
expressions on purpose, and in four test expressions that were ill-typed by the spec.

## 6. Engine Architecture (`ocl.engine`)

**Bundle:** `org.eclipse.fennec.m2x.ocl.engine`

### 6.1 OclEngine — Facade

- Primary constructor: `OclEngine(OclConfiguration)`
- Backward-compatible constructors delegate to it
- 3-level operation dispatch: Ecore `eInvoke` → stdlib → custom providers
- `installDelegates()` / `uninstallDelegates()` for standalone EMF delegate registration
- `warmUp(EPackage)` pre-populates all cache layers
- Thread-safe: stateless evaluation, each `evaluate()` creates fresh `OclEvaluator`

### 6.2 OclEvaluator — Recursive Interpreter

Recursive `OclSwitch<Object>` visitor over the EMF AST.

**Supported expression types:**

| Category | Expressions |
|----------|-------------|
| Literals | Integer, Real, String, Boolean, Null, Invalid, UnlimitedNatural, Enum, Type |
| Structural | VariableExp, IfExp, LetExp |
| Calls | PropertyCallExp (eGet), OperationCallExp (3-level dispatch) |
| Collections | CollectionLiteralExp, TupleLiteralExp, MapLiteralExp |
| Iterators | select, reject, collect, collectNested, forAll, exists, any, one, isUnique, sortedBy, closure |
| Iterate | Accumulator pattern |

Non-static, receives `PropertyAccessorCache` + `List<OclOperationProvider>` in constructor.

### 6.3 OclStdlib — Standard Library

Switch-based dispatch for all OCL standard library operations. No reflection, no HashMap
lookup overhead.

| Type | Key Operations |
|------|---------------|
| OclAny | `=`, `<>`, `oclIsTypeOf()`, `oclIsKindOf()`, `oclAsType()`, `oclIsUndefined()`, `oclIsInvalid()`, `oclIsNew()`, `oclType()`, `oclContainer()`, `oclContents()`, `toString()` |
| Boolean | `and`, `or`, `xor`, `not`, `implies` |
| Integer | `+`, `-`, `*`, `/`, `mod`, `div`, `abs`, `max`, `min`, `floor`, `ceiling`, `round` |
| Real | `+`, `-`, `*`, `/`, `abs`, `floor`, `round`, `max`, `min` |
| String | `size()`, `concat()`, `substring()`, `toInteger()`, `toReal()`, `toUpperCase()`, `toLowerCase()`, `matches()`, `replaceAll()`, `replaceFirst()`, `startsWith()`, `endsWith()`, `equalsIgnoreCase()` |
| Collection | `size()`, `includes()`, `excludes()`, `count()`, `isEmpty()`, `notEmpty()`, `sum()`, `flatten()` |
| Set | `union()`, `intersection()`, `-`, `including()`, `excluding()`, `symmetricDifference()` |
| OrderedSet | `append()`, `prepend()`, `insertAt()`, `at()`, `indexOf()`, `first()`, `last()`, `reverse()` |
| Bag | `union()`, `intersection()`, `including()`, `excluding()` |
| Sequence | `union()`, `append()`, `prepend()`, `insertAt()`, `at()`, `indexOf()`, `first()`, `last()`, `reverse()` |
| Map (v2.5) | `at()`, `put()`, `size()`, `includes()`, `excludes()`, `keys()`, `values()` |

**Safe navigation (v2.5):** `?.` and `?->` return null/empty collection instead of OclInvalid
when source is null.

**Spec-compliance fixes (2026-02-20):**
- `excluding(x)` removes **all** occurrences (§11.7.2), not just the first — uses `removeAll(Collections.singleton(x))` for List/Bag
- `substring(lower, upper)` requires `1 <= lower AND lower <= upper` (§11.5.1) — `substring(2,1)` returns OclInvalid, not empty string
- `null.oclType()` returns `OclVoid` type, `invalid.oclType()` returns `OclInvalid` type (§11.2.1) — not OclInvalid sentinel

**Non-standard extensions (intentionally supported):**
- `toUpperCase()`/`toLowerCase()` — aliases for OCL standard `toUpper()`/`toLower()`
- `+` operator for String concatenation — alias for `concat()`; both operands must be Strings, `'a' + 1` is `invalid` (#314)
- `trim()` on String — not in OCL standard

### 6.4 OclEvalEnvironment — Runtime Bindings

Chain-of-scopes variable environment. Per-evaluation instance, not shared.

### 6.5 Value Representation

| OCL Type | Java Implementation |
|----------|-------------------|
| Integer | `Long`; `BigInteger` where a result leaves the 64-bit range, back to `Long` when it fits (#322) |
| Real | `BigDecimal`; division keeps at least 32 fraction digits, HALF_EVEN; NaN/±∞ are no Real (#322) |
| UnlimitedNatural | finite values are Integers; `*` is `OclUnlimitedNatural.INSTANCE` |
| Set | `OclSet` (`AbstractSet`, hashed by OCL lookup key) |
| Sequence | `ArrayList` |
| Bag | `OclBag` (`ArrayList`) |
| OrderedSet | `OclOrderedSet` (`ArrayList`, OCL equality) |
| Tuple | `LinkedHashMap<String, Object>` |
| Map (v2.5) | `LinkedHashMap` |

`OclNumbers` (engine-internal) holds the arithmetic: `long` while it does not overflow, then
`BigInteger`; any Real operand makes the operation `BigDecimal`. A `Double`/`Float` from the model
enters by its shortest decimal (`BigDecimal.valueOf`). Equality, hashing and ordering go by the
mathematical value (`OclEqualityUtil` lookup key): `1 = 1.0`, `-0.0 = 0.0`, Integers beyond 2^53
stay distinct (#317). Objects are equal by identity, not structure (#318).

**Boundaries.** `evaluate()` narrows the result recursively: `Long` → `Integer` where it fits,
`BigDecimal` → `Double`; `evaluateWithDiagnostics()` keeps the exact values. Values written into
a model — derived features and operations of the EMF delegates, QVT-O assignments, QVT-R
enforcement — go through `OclValues.toEcore` (`ocl.api`) to the feature's instance class; M2T
writes text through `OclValues.toText`. Literals are limited by the metamodel:
`IntegerLiteralExp.integerSymbol` is an `ELong`, `RealLiteralExp.realSymbol` an `EDouble`, so a
literal beyond 64 bits is a parse error and a Real literal is exact up to 15 significant digits.
Unbounded literals would need `EBigInteger`/`EBigDecimal` in `ocl.ecore`.

**Navigation results** of a many-valued feature take the feature's collection kind (§7.5.3,
§7.5.11, #319) as a copy in the model's order, implemented by the engine-internal
`FeatureValues.Feature{Set,OrderedSet,Bag,Sequence}`. Each implements `OclFeatureValue`
(`ocl.api`: `owner()`, `feature()`), so the #182 guard still refuses mutating operations on model
values and QVT-O's `feature.unlink(item)` reaches the live list. For a unique reference the copy
skips the distinctness check: EMF keeps it distinct by identity, which is OCL equality for
objects.

**Indexing:** OCL uses 1-based indexing for `String.substring/at/indexOf` and
`Collection.at/indexOf/subSequence`.

### 6.6 Configurable Evaluation (D14)

| Context | Null handling | Error recovery | Rationale |
|---------|-------------|---------------|-----------|
| Validation (`inv:`) | STRICT | FAIL_FAST | Spec compliance, clear pass/fail |
| Derived features (`derive:`) | STRICT | FAIL_FAST | Must produce correct value or error |
| QVT-O mappings | STRICT | FAIL_FAST | Transformation must be correct |
| M2T templates | LENIENT | COLLECT_ERRORS | Best-effort text, report issues separately |
| Interactive console | LENIENT | COLLECT_ERRORS | Exploration, partial results useful |
| QVT-R `checkonly` | STRICT | COLLECT_ERRORS | Need all violations, not just first |

### 6.7 No Pivot Layer (D16)

Eclipse OCL introduced the "Pivot" layer (~262 EClassifiers) to abstract over Ecore vs UML.
We don't need it:

| Eclipse problem | Our situation |
|-----------------|---------------|
| Must support Ecore AND UML bindings | Ecore only. No dual binding. |
| 262-class Pivot metamodel = slow startup | Lean EMF model + lazy stdlib = fast startup |
| Ecore→AS conversion on every parse | No conversion — direct Ecore access |

UML models loaded in EMF are already `EObject` instances and work with our engine.
UML-specific OCL operations (stereotypes) can be added via an optional `ocl.uml` extension
bundle using `OclOperationProvider`. `oclIsInState` is answered by the evaluator itself with the
ERROR "not supported: Ecore models have no state machines", before any provider is asked (#321);
a UML bundle would have to lift that.

---

## 7. Caching Architecture

The OCL engine uses a layered caching strategy. Each layer targets a different bottleneck
in the parse → resolve → evaluate pipeline.

```
┌──────────────────────────────────────────────────────────────────┐
│  User Code                                                       │
│  engine.evaluate("self.name", OclContext.of(person))             │
└────────────────────────┬─────────────────────────────────────────┘
                         │
         ┌───────────────▼────────────────┐
         │  Layer 1: Expression Cache      │  OclExpressionCache (API)
         │  "self.name" + Person → AST     │  OclLruExpressionCache (Engine)
         │  ─────────────────────────────  │
         │  Scope: shared across engines   │
         │  Key: nsURI#typeName#exprText   │
         │  Hit: ~190 ns  Miss: ~23 µs    │
         │  Speedup: 100–190x             │
         └───────────────┬────────────────┘
                         │ parsed AST
         ┌───────────────▼────────────────┐
         │  Layer 2: Feature Resolution    │  1-entry cache in OclEvaluator
         │  (EClass, sf) → resolved sf     │
         │  ─────────────────────────────  │
         │  Scope: per evaluation          │
         │  Hit: ~1 ns (two == checks)     │
         │  Benefit: iterator hot paths    │
         │  (same feature on N objects)    │
         └───────────────┬────────────────┘
                         │ resolved feature
         ┌───────────────▼────────────────┐
         │  Layer 3: Property Accessor     │  PropertyAccessorCache (per engine)
         │  (Class, sf) → LambdaAccessor   │
         │  ─────────────────────────────  │
         │  Scope: per engine instance     │
         │  Generated models: ~5 ns/call   │
         │  Dynamic models: skipped (0 ns) │
         │  Speedup: 19–22% (generated)    │
         └───────────────┬────────────────┘
                         │ property value
         ┌───────────────▼────────────────┐
         │  EMF eGet() fallback            │  Only for dynamic models
         │  DynamicEObjectImpl.eSettings[] │  ~40 ns/call
         └────────────────────────────────┘
```

### 7.1 Layer 1: Expression Parse Cache

**Problem:** Parsing costs ~23 µs. Delegates parse the same expression on every access.

**Implementation:** `OclLruExpressionCache`
- `LinkedHashMap(16, 0.75f, true)` with `removeEldestEntry()` for LRU eviction
- All map operations `synchronized` — parsing happens outside the lock
- Hit/miss counters via `AtomicLong`
- Cache key: `nsURI + "#" + typeName + "#" + expression`
- Factory: `OclLruExpressionCache.ofSize(1024)`

**Why not `ConcurrentHashMap`?** LRU eviction requires access-order tracking, which
`ConcurrentHashMap` does not support.

Cache is nullable in `OclConfiguration` — engine works without caching.

**Delegate-local caches:** The delegate factories maintain their own volatile/synchronized
lazy parse caches per delegate instance, orthogonal to the shared expression cache.

#### 7.1.1 Model-anchored variants of layer 1

An LRU knows how many entries it may keep, not what a model version is: an expression compiled
against a retired model survives because it was used recently, one belonging to a live model is
dropped because it was not. Two implementations in their own bundles replace the key with model
identity — both optional, neither displaces the default by existing:

| Bundle | Implementation | Key | Holds entries in |
|---|---|---|---|
| `…ocl.fingerprint` | `FingerprintExpressionCache` | derived fingerprint of the package | its own map |
| `…ocl.metadata` | `RegistryExpressionCache` | derived fingerprint of the package | a named `EObjectRegistry` |

**Two fingerprints, two jobs** (D44). `RegistryExpressionCache` keys entries by the *derived*
fingerprint — `fingerprint(ePackage, "ocl")`, the answer to "may this compiled artifact be
reused?" — and files the *plain* model fingerprint as the entry property `emf.fingerprint`,
because that is the identity the rest of the runtime is keyed by: `MetadataService` computes it
for every version it registers, and `RegistryMetadataBridge` compares it against a tree's version
before placing anything there. Mixing the two leaves every entry naming a version no tree has.

**What has no version to anchor to** — a classifier of Ecore itself, a type built at runtime —
goes to an LRU delegate. That is the right structure for something with no lifetime of its own.

**The registry is the read face, not the aspect.** A class carries at most one aspect per type
id while an OCL context type has as many expressions as someone writes, so the aspect is the
model-anchored entry point and the registry answers the actual queries.

**Anchoring for the bridge.** `OclVersionedExpressions` (in `…ocl.metadata`) exposes the two
questions a plain cache cannot answer: `anchorOf(key)` — the context type an entry was compiled
against, which is not in the expression and cannot be looked up while a version's tree is still
being built — and `release(ePackage)`, which drops what a departing version leaves behind.
`OclExpressionAnchorResolver` adapts the first to the bridge's `AspectAnchorResolver`; it is the
only class mentioning bridge types, and that import is optional so neither a plain-Java user nor
a deployment without the bridge has to carry the bundle.

**Compiling on arrival.** `OclConstraintPrecompiler` compiles what a model declares — invariants,
derivations, operation bodies — when the model arrives, triggered by the `EPackage` service and
deliberately *not* by `MetadataHandler.onPackageRegistered`: a version's tree is published only
after all handlers ran, so pushing from inside one makes placement depend on wiring order (D44).

### 7.2 Layer 2: Feature Resolution Cache (1-Entry)

**Problem:** `resolveFeature()` on every property access costs ~5–10 ns via `getFeatureID()`.

**Optimization:** 1-entry "last resolved" cache in `OclEvaluator` (per-evaluation):

```java
private EClass lastResolvedClass;
private EStructuralFeature lastResolvedInput;
private EStructuralFeature lastResolvedOutput;
```

**Why 1-entry?** In iterator bodies, the same feature is accessed on many objects of the
same EClass. Only the first iteration is a miss.

**Impact:** P7 (closure) **−52%**, P4 (iterators) **−7%**, single access ≈0%.

### 7.3 Layer 3: Property Accessor Cache

**Problem:** EMF `eGet()` goes through 3-level dispatch (~40 ns). Generated models have
typed getters (~5 ns) but `eGet()` never uses them.

**Optimization:** `PropertyAccessorCache` uses `LambdaMetafactory` to generate accessors
wrapping typed getters. JIT can inline to ~5 ns.

- Instance-based: each `OclEngine` has its own cache
- `ConcurrentHashMap` for lock-free reads
- Dynamic models: `sf.getContainerClass() == null` → returns null (zero overhead)
- Autoboxing handled via `autobox()` mapping
- Getter name: EMF convention — `isXxx()` for `EBoolean`, `getXxx()` otherwise

### 7.4 Warm-Up

`OclEngine.warmUp(EPackage)` pre-populates all cache layers:

1. **Property Accessor Cache:** Creates temp EObject per non-abstract EClass, pre-populates
   accessor entries. Cost: ~4 µs per package.
2. **Expression Parse Cache:** Scans OCL `EAnnotation`s (derivation, initial, body, pre, post,
   constraints) and pre-parses. Each annotated expression adds ~23 µs.
3. Parse errors during warm-up are silently ignored — reported at evaluation time.

### 7.5 Future: OclAspectProvider

Designed for integration with fennec's `MetadataService` / `AspectProvider` pattern
(planned for `emf.osgi`):

1. `OclAspectProvider` registers as `AspectProvider` in `MetadataService`
2. When an EPackage is registered, the provider receives it
3. Builds `OclConfiguration` with pre-populated caches, calls `warmUp(EPackage)`
4. New `OclEngine` instances created from pre-warmed configuration

The `OclConfiguration` builder pattern and instance-based `PropertyAccessorCache` are
already prepared for this integration.

---

## 8. EMF Delegate Integration

### 8.1 Overview

EMF delegate registries allow OCL expressions in Ecore models via `EAnnotation`:

| Delegate | Purpose | Annotation Key |
|----------|---------|---------------|
| **ValidationDelegate** | OCL invariants as validation rules | constraint name |
| **SettingDelegate** | Derived features; `initial` is read like a derivation on every access | `derivation`, `initial` |
| **InvocationDelegate** | Operation bodies with pre/post, plus the `pre:`/`post:` of loaded Complete OCL documents (#323) | `body`, `pre`, `post` |

The **validation delegate** counts only `true` as satisfied; a non-Boolean result violates the
constraint like `null` and `invalid` (§12.6.1 [1], #324). The **setting delegate** is stateless:
`initial` is used when there is no `derivation` and evaluated on every `eGet`, not once at
creation (§12.8) — Eclipse's `OCLSettingDelegate` does the same (#324). The **invocation delegate**
parses `body`, `pre` and `post` with the operation's parameters declared, and `result` in `post`
(`OclEngine.parse(expression, contextType, variables)`, #324). It is reached only for operations
that have an OCL delegate (`body` or `post`); a Java-implemented operation of a generated model
never passes it, so a document's `pre:`/`post:` for it is not checked.

### 8.2 Annotation URI

All annotations use the Fennec OCL delegate URI:
```
http://www.eclipse.org/fennec/m2x/ocl/1.0
```

Example in Ecore:
```xml
<eStructuralFeatures xsi:type="ecore:EAttribute" name="fullName" eType="ecore:EDataType ...#//EString"
    changeable="false" volatile="true" transient="true" derived="true">
  <eAnnotations source="http://www.eclipse.org/fennec/m2x/ocl/1.0">
    <details key="derivation" value="self.firstName.concat(' ').concat(self.lastName)"/>
  </eAnnotations>
</eStructuralFeatures>
```

### 8.2.1 EPackages declared to the IDE (#157)

`plugin.xml` also declares two `EPackage`s through `org.eclipse.emf.ecore.generated_package`: the OCL
metamodel (`OclPackage`, `http://www.eclipse.org/fennec/m2x/ocl/1.0`) and the standard library
(`OclStandardLibrary.eINSTANCE`, `http://www.eclipse.org/fennec/m2x/ocl/stdlib/1.0`). In a plain JVM
and under OSGi both reach `EPackage.Registry.INSTANCE` when their classes initialize, before anything
could reference them; an Eclipse IDE fills its registry from `plugin.xml` and reads a resource before
running any of our code, so without the declaration a stored unit referencing
`…/ocl/stdlib/1.0#//Integer` resolves to nothing. `OclIdePackages.DECLARED` lists the same two in
code, and `OclIdeGeneratedPackagesTest` keeps file and list in step by doing what the registry
reader does: load the declared class, take its static `eINSTANCE`, compare the nsURI. The
languages' own metamodels follow when their bundles ship in a feature.

### 8.3 Implementation Classes

| Class | Extends/Implements | Package |
|-------|-------------------|---------|
| `OclInvocationDelegateFactory` | `EOperation.Internal.InvocationDelegate.Factory` | `ocl.engine.internal` |
| `OclSettingDelegateFactory` | `BasicSettingDelegate.Stateless` | `ocl.engine.internal` |
| `OclValidationDelegateFactory` | `EValidator.ValidationDelegate` | `ocl.engine.internal` |
| `OclDelegateUtil` | — (annotation reader + URI constant) | `ocl.engine.internal` |

### 8.4 Standalone Registration

```java
OclConfiguration config = OclConfiguration.builder(new OclParserSupport()).build();
OclEngine engine = OclEngines.create(config);
engine.installDelegates();  // registers all three delegates

// Equivalent manual registration:
String uri = "http://www.eclipse.org/fennec/m2x/ocl/1.0";
EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE
    .put(uri, new OclInvocationDelegateFactory(engine));
EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE
    .put(uri, new OclSettingDelegateFactory(engine));
EValidator.ValidationDelegate.Registry.INSTANCE
    .put(uri, new OclValidationDelegateFactory(engine));
```

### 8.5 Delegate Options

EMF delegates use `OclEngine.delegateOptions` (configurable via `setDelegateOptions()`).
Configure before `installDelegates()` for untrusted models:

```java
engine.setDelegateOptions(OclEvaluationOptions.strict()
    .withMaxDepth(100)
    .withMaxCollectionSize(10_000));
engine.installDelegates();
```

### 8.6 Validation Caching

`OclValidationDelegateFactory` caches parsed expressions in a `ConcurrentHashMap`
keyed by `contextType#expression` to prevent parse amplification (S-7).

### 8.7 OSGi Registration (emf.osgi Whiteboard)

The delegate factories are annotated with `@Component` and service properties matching the
emf.osgi whiteboard pattern:

| Property | Value |
|----------|-------|
| `emf.configuratorName` | `http://www.eclipse.org/fennec/m2x/ocl/1.0` |
| `emf.name` | `fennec-ocl` |
| `emf.configuratorType` | `OPERATION_INVOCATION_FACTORY` / `SETTING_DELEGATE_FACTORY` / `VALIDATION_DELEGATE` |

The emf.osgi delegate registry whiteboard components pick up these services automatically —
no manual `installDelegates()` call needed.

Each delegate factory injects `OclDelegateSupport` via `@Reference` — an internal
interface extending `OclEngine` with the one method the public API has no reason to carry,
`evaluatePostcondition()`, since `@pre` snapshots exist only for postcondition delegates.
`OclEngineComponent` therefore registers as `OclEngine` and `OclDelegateSupport`.

Before, the factories took `OclEngineImpl` and the component published that class as a
service type. Nothing outside the bundle could use it — `…engine.internal` is not exported —
so it was a service type that existed only to let one internal class find another.

### 8.8 emf.osgi Delegate Registry Analysis

emf.osgi populates its delegate registries through whiteboard populator components.
There are no registries for `SettingDelegate` and `ValidationDelegate`; the detailed
analysis is kept internally.

---

## 9. OSGi Integration

### 9.1 Core Principle

All engines work as **plain Java libraries** without OSGi. OSGi is an optional enhancement
providing service discovery, whiteboard patterns, and lifecycle management.

**Pattern:** Pure Java core + OSGi adapter layer (same bundle).

### 9.2 Standalone Usage

```java
OclConfiguration config = OclConfiguration.builder(new OclParserSupport())
    .expressionCache(OclLruExpressionCache.ofSize(1024))
    .build();
OclEngine engine = OclEngines.create(config);

OclContext context = OclContext.of(myEObject);
Object result = engine.evaluate("self.name", context);

// With global variables
OclContext ctxWithVars = OclContext.of(myEObject, Map.of("threshold", 50000));
Object result2 = engine.evaluate("self.salary > threshold", ctxWithVars);

// Install EMF delegates
engine.installDelegates();
```

### 9.3 OSGi Component Architecture

```
┌─────────────────────────────────────────────────────────────┐
│  OclParserSupport (PROTOTYPE)                               │
│  → OclExpressionParser service                              │
│  Each consumer gets its own parser instance                 │
└──────────────┬──────────────────────────────────────────────┘
               │ @Reference(scope = PROTOTYPE_REQUIRED)
               │
┌──────────────┼──────────────────────────────────────────────┐
│  DefaultOclExpressionCacheComponent (SINGLETON)             │
│  → OclExpressionCache service (LRU, 1024 entries)          │
│  Shared across all engine instances by default              │
└──────────────┬──────────────────────────────────────────────┘
               │ @Reference(name="expressionCache")
               │
┌──────────────────────────────────────────────────────────────┐
│  OclOperationProvider services (any number, optional)        │
│  Every registered provider is bound; none is required        │
└──────────────┬──────────────────────────────────────────────┘
               │ @Reference(name="operationProvider", MULTIPLE)
┌──────────────▼──────────────────────────────────────────────┐
│  OclEngineComponent (PROTOTYPE)                             │
│  → OclEngine + OclDelegateSupport services                  │
│  @Designate(ocd = OclEngineConfiguration.class)             │
│  Each consumer gets its own engine instance with:           │
│  - own PropertyAccessorCache (per-engine, not shared)       │
│  - own OclParserSupport instance (PROTOTYPE_REQUIRED)       │
│  - shared OclExpressionCache (via @Reference target)        │
│  - every OclOperationProvider (MULTIPLE, target-filterable)  │
│  Engine-wide defaults from ConfigAdmin (ocl.* properties)   │
└──────────────┬──────────────────────────────────────────────┘
               │ @Reference OclEngine
┌──────────────▼──────────────────────────────────────────────┐
│  EMF Delegates (emf.osgi whiteboard, see §8.7)              │
└─────────────────────────────────────────────────────────────┘
```

**Component scopes and lifecycle:**

| Component | Scope | Instances | Rationale |
|-----------|-------|-----------|-----------|
| `OclParserSupport` | PROTOTYPE | One per engine | ANTLR4 parser is lightweight; each engine gets its own instance via `PROTOTYPE_REQUIRED` |
| `OclEngineComponent` | PROTOTYPE | One per consumer | Each `@Reference OclEngine` injection creates a fresh engine with isolated `PropertyAccessorCache` |
| `DefaultOclExpressionCacheComponent` | SINGLETON | One per config (shared) | Parse cache is thread-safe and expensive to warm; shared across all engines by default |

**Key:** Every `@Reference OclEngine` injection yields a **fresh, isolated engine instance**. The parser and engine are never shared between consumers. Only the expression cache is shared (by default) — this is intentional, since parsed ASTs are immutable and safe to reuse.

**Key components:**
- `OclEngineComponent` — PROTOTYPE scope, extends `OclEngine`, `@Designate`d with `OclEngineConfiguration`
- `OclParserSupport` — PROTOTYPE scope, ANTLR4-based parser
- `OclEngineConfiguration` — `@ObjectClassDefinition` annotation with `ocl.*` prefixed properties
- `OclConfigurationHelper` — maps `OclEngineConfiguration` to `OclConfiguration`
- `DefaultOclExpressionCacheComponent` — SINGLETON, provides default LRU cache (1024 entries)
- Delegate factories — separate `@Component` services with emf.osgi properties, inject `OclEngine`

### 9.4 Engine-Wide Defaults via ConfigAdmin

The `OclEngineComponent` supports engine-wide defaults through the `OclEngineConfiguration` Metatype annotation. All properties use the `ocl.` prefix and map directly to `OclConfiguration` fields.

**Available properties:**

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `ocl.maxDepth` | int | 1,000 | Maximum expression nesting depth |
| `ocl.maxCollectionSize` | int | 1,000,000 | Maximum collection elements |
| `ocl.maxClosureIterations` | int | 100,000 | Maximum closure() iterations |
| `ocl.maxRegexLength` | int | 1,000 | Maximum regex pattern length |
| `ocl.maxStringLength` | int | 10,000,000 | Maximum length of a string an operation produces |
| `ocl.timeout` | long | 0 | Evaluation timeout in ms (0 = no timeout) |
| `ocl.nullHandling` | String | `STRICT` | `STRICT` or `LENIENT` |
| `ocl.errorRecovery` | String | `FAIL_FAST` | `FAIL_FAST` or `COLLECT_ERRORS` |
| `ocl.customOperationsEnabled` | boolean | false | Enable config-registered custom operations |

These defaults are used by:
- `evaluate(expression, context)` — convenience method without explicit options
- `delegateOptions` — initial value for EMF delegate evaluations

Calls with explicit `OclEvaluationOptions` override the engine-wide defaults.

**Default configuration (OSGi Configurator JSON):**

```json
{
    ":configurator:resource-version": 1,
    "DefaultOclEngine": {
        "ocl.maxDepth": 500,
        "ocl.maxCollectionSize": 100000,
        "ocl.timeout": 5000,
        "ocl.nullHandling": "STRICT"
    }
}
```

Without explicit configuration, all defaults match `OclEvaluationOptions.strict()` — existing behavior is preserved.

### 9.5 Expression Cache Sharing and Isolation

By default, all engine instances share the single `DefaultOclExpressionCache` (SINGLETON). This is efficient because parsed `OclExpression` ASTs are immutable and thread-safe.

**Shared cache (default behavior — no configuration needed):**

All engines use the same cache. A parse result cached by one engine is reused by all others:

```
Engine A ──┐
Engine B ──┼──→ DefaultOclExpressionCache (1024 entries)
Engine C ──┘
```

**Custom shared cache (larger size):**

Create a cache with custom size; all engines that target it share the same instance:

```json
{
    ":configurator:resource-version": 1,
    "DefaultOclExpressionCache~large": {
        "oclExpressionSize": 8192,
        "name": "large-cache"
    },
    "DefaultOclEngine": {
        "expressionCache.target": "(name=large-cache)"
    }
}
```

**Individual caches per engine (isolation):**

Create separate cache instances for engines that should not share parse results — e.g. when different engines evaluate expressions against different metamodels:

```json
{
    ":configurator:resource-version": 1,
    "DefaultOclExpressionCache~app-a": {
        "oclExpressionSize": 2048,
        "name": "cache-app-a"
    },
    "DefaultOclExpressionCache~app-b": {
        "oclExpressionSize": 512,
        "name": "cache-app-b"
    },
    "DefaultOclEngine~app-a": {
        "expressionCache.target": "(name=cache-app-a)",
        "engine.tag": "app-a"
    },
    "DefaultOclEngine~app-b": {
        "expressionCache.target": "(name=cache-app-b)",
        "engine.tag": "app-b"
    }
}
```

```
Engine app-a ──→ cache-app-a (2048 entries)
Engine app-b ──→ cache-app-b (512 entries)
```

### 9.6 Multiple Engine Configurations

Use factory configurations to create multiple engines with different security profiles:

```json
{
    ":configurator:resource-version": 1,
    "DefaultOclEngine~trusted": {
        "ocl.maxDepth": 1000,
        "ocl.nullHandling": "STRICT",
        "engine.tag": "trusted"
    },
    "DefaultOclEngine~sandbox": {
        "ocl.maxDepth": 50,
        "ocl.maxCollectionSize": 1000,
        "ocl.maxClosureIterations": 100,
        "ocl.timeout": 2000,
        "ocl.nullHandling": "STRICT",
        "engine.tag": "sandbox"
    }
}
```

Consumers select a specific engine via target filter:

```java
@Component
public class MyService {

    @Reference(target = "(engine.tag=trusted)")
    OclEngine trustedEngine;

    @Reference(target = "(engine.tag=sandbox)")
    OclEngine sandboxEngine;
}
```

### 9.7 Consumer Example

```java
@Component
public class MyService {
    @Reference
    OclEngine oclEngine;  // injected by DS (prototype: own instance)

    void doSomething(EObject obj) {
        Object result = oclEngine.evaluate("self.name", OclContext.of(obj));
    }
}
```

---

## 10. Security Hardening

### 10.1 Threat Model

The OCL engine evaluates expressions that may come from untrusted sources (model annotations,
external OCL documents, user input). **No remote code execution vectors exist** — OCL is a
pure query language with no I/O. **Denial-of-service via resource exhaustion** is the primary
concern.

### 10.2 Configurable Limits

All limits are configured via `OclEvaluationOptions`:

| Field | Type | Default | Protects against |
|-------|------|---------|-----------------|
| `maxDepth` | int | 1,000 | S-9: Stack overflow via deep recursion |
| `maxCollectionSize` | int | 1,000,000 | S-2: Range explosion, S-3: Product explosion, S-12: allInstances, S-16: collection growth |
| `maxClosureIterations` | int | 100,000 | S-4: Unbounded closure traversal |
| `maxRegexLength` | int | 1,000 | S-1: ReDoS via crafted regex patterns |
| `maxStringLength` | int | 10,000,000 | S-11: string growth, S-15: `format` width |
| `timeout` | Duration | none | S-13: Runaway evaluation (deadline-based) |

Limits are per-evaluation (not global). Violations produce `OclInvalid` with diagnostic error.

The limits on ranges, products and `allInstances` bound what one step of an expression can
*create*. `maxStringLength` and `maxCollectionSize` also bound what an expression can *grow*
(S-11, S-16): every standard-library result is checked against them, so `acc.concat(acc)` or
`acc->union(acc)` in an `iterate` — or in a QVT-O `while` — stops at the limit instead of
doubling thirty times at a depth no other limit notices. Operations whose result can be far
larger than the limit before an after-check runs bound themselves while producing it:
`concat` checks the sum of the lengths, `replaceAll`/`replaceFirst` check each match with the
worst case of its expansion (a `$0` in the replacement repeats the match), `joinfields` and
`format` write into a sink that refuses to grow past the limit (S-15: the width field of a
format specifier is an allocation request with several spellings — `%9s`, `%1$9s`, `%<9s`), and
`collect` counts while it collects (a collection-valued body squares the source size).

What the limits do not bound is aggregate memory: a collection of a million strings of a
million characters each is within both limits. Building it takes time, which is what
`timeout` is for.

### 10.3 Attack Vectors

| # | Vector | Severity | Status |
|---|--------|----------|--------|
| S-1 | ReDoS — `'input'.matches('(a+)+$')` | High | Mitigated: regex length check |
| S-2 | Range explosion — `Sequence{1..2147483647}` | High | Mitigated: size check |
| S-3 | Cartesian product — `Seq{1..1000}->product(Seq{1..1000})` | High | Mitigated: product size check |
| S-4 | Unbounded closure — `Seq{1}->closure(i \| i+1)` | High | Mitigated: iteration counter |
| S-5 | Unsafe type casts in stdlib | Medium | Mitigated: safe pattern matching |
| S-6 | Delegate injection (trust boundary) | Low | Documented: explicit opt-in |
| S-7 | Parse amplification (repeated validation) | Medium | Mitigated: expression cache |
| S-8 | allInstances on large extent | Medium | Mitigated: size check (see S-12) |
| S-9 | Stack overflow via deep recursion | High | Mitigated: depth counter |
| S-10 | Integer overflow | Low | Gone: Integer is unbounded since #322. A `BigInteger` has no size limit of its own — repeated squaring in an `iterate` is bounded by `timeout` only |
| S-11 | String growth — `Sequence{1..31}->iterate(i; s = 'x' \| s + s)` | Medium | Mitigated: maxStringLength on every result |
| S-12 | allInstances result size | Medium | Mitigated: maxCollectionSize |
| S-13 | Custom operation provider abuse | Low | Mitigated: D29 disabled by default |
| S-14 | EMF delegate URI spoofing | Low | Documented: by design |
| S-15 | Format width allocation — `'%1$999999999s'.format('x')` | Medium | Mitigated: bounded output sink |
| S-16 | Collection growth — `Sequence{1..31}->iterate(i; c = Sequence{1} \| c->union(c))` | Medium | Mitigated: maxCollectionSize on every result |

### 10.4 Trust Boundaries

| Boundary | Trust Level | Recommendation |
|----------|------------|----------------|
| Own model annotations | Trusted | Default `strict()` options |
| External Complete OCL documents | Semi-trusted | `strict()` with tightened limits |
| User input (console, LSP) | Untrusted | Tightened limits, consider timeout |
| Custom operation providers | Controlled (D29) | Disabled by default; requires `customOperationsEnabled` on both Config and Options |
| Model extent for allInstances | Varies | Bounded extent, set `maxCollectionSize` |

### 10.5 Embedder Guidance

When evaluating OCL expressions from untrusted sources:

1. **Set conservative limits** — per-evaluation (standalone):
   ```java
   OclEvaluationOptions sandboxed = OclEvaluationOptions.strict()
       .withMaxDepth(100)
       .withMaxCollectionSize(10_000)
       .withMaxClosureIterations(1_000)
       .withMaxRegexLength(200)
       .withMaxStringLength(100_000)
       .withTimeout(Duration.ofSeconds(5));
   ```

2. **Set engine-wide defaults** — via `OclConfiguration` (standalone) or ConfigAdmin (OSGi):
   ```java
   // Standalone
   OclConfiguration config = OclConfiguration.builder(parser)
       .maxDepth(100).maxCollectionSize(10_000)
       .maxClosureIterations(1_000).maxRegexLength(200)
       .maxStringLength(100_000).timeoutMs(5000).build();
   ```
   ```json
   // OSGi Configurator
   {
       ":configurator:resource-version": 1,
       "DefaultOclEngine": {
           "ocl.maxDepth": 100,
           "ocl.maxCollectionSize": 10000,
           "ocl.maxClosureIterations": 1000,
           "ocl.maxRegexLength": 200,
           "ocl.maxStringLength": 100000,
           "ocl.timeout": 5000
       }
   }
   ```

3. **Use bounded model extents** for `allInstances()`.

4. **Configure delegate options** before `installDelegates()` (standalone only; in OSGi the engine-wide defaults are applied automatically):
   ```java
   engine.setDelegateOptions(sandboxed);
   engine.installDelegates();
   ```

5. **Check `OclResult.diagnostics()`** — limit violations are reported as error diagnostics.

---

## 11. Performance

### 11.1 Thread-Safety Guarantees

| Component | Strategy | Status |
|-----------|----------|--------|
| `OclEngine` | Stateless evaluation — each `evaluate()` creates fresh `OclEvaluator` | Thread-safe by design |
| `OclEvaluator` | Per-evaluation instance, no shared mutable state | Thread-safe by design |
| `OclStdlib` | Static methods, no state | Thread-safe by design |
| `OclEvalEnvironment` | Per-evaluation chain-of-scopes, not shared | Thread-safe by design |
| `configProviders` | Immutable `List` from configuration (D29) — no mutable registration | Thread-safe by design |
| `defProviders` | `CopyOnWriteArrayList` — for `loadDocument()` def-operations | Thread-safe |
| `additionalProviders` | Per-evaluation via `OclEvaluationOptions` — no shared state | Thread-safe by design |
| `expressionCache` | `ConcurrentHashMap` in delegates | Thread-safe |
| `delegateOptions` | `volatile` field on `OclEngine` | Visibility guaranteed |
| Parser (ANTLR4) | New `OclLexer`/`OclParser` per parse call | Thread-safe |

### 11.2 Performance Design Principles

- **No Pivot layer** (D16): direct Ecore access, no Ecore→AS conversion
- **Lazy parsing in delegates**: expressions parsed on first access, cached
- **Switch-based dispatch** in OclStdlib: no reflection, no HashMap lookups
- **Immutable AST**: parsed `OclExpression` shareable across threads
- **Per-evaluation environment**: no lock contention

### 11.3 Benchmark Results

Measured on: Linux 6.17.0-1011-oem, Java 21. Dynamic EMF models (PropertyAccessorCache
not active — only benefits generated models).

#### µs/op (Median, 3 JVM runs)

| # | Benchmark | Baseline | + LRU Cache | + Accessor | + Resolve Cache | Δ gesamt |
|---|-----------|--------:|----------:|----------:|---------------:|--------:|
| P1 | Parse (complex) | 28.57 | 28.57 | 28.57 | 20.32 | **−29%** |
| P2 | Property (`self.name`) | 0.078 | 0.078 | 0.074 | 0.086 | ≈0% |
| P3 | Collection (100 emps) | — | — | 22.18 | — | — |
| P4 | Iterators (forAll, 50) | 31.96 | 31.96 | 35.91 | 33.43 | **−7%** |
| P5 | allInstances (50K) | — | — | 11,780 | — | — |
| P6 | Navigation (5-step) | 2.13 | 2.13 | 1.74 | 2.22 | ≈0% |
| P7 | Closure (100 emps) | — | — | 5,149 | 2,460 | **−52%** |
| P17 | Engine startup | — | — | 0.34 | — | — |
| P18 | Cache hit | — | 0.141 | 0.190 | — | — |
| P18 | **Speedup** | — | **192x** | **147x** | — | — |
| P19 | Accessor (`self.name`) | — | — | 0.076 | 0.081 | — |
| P20 | WarmUp (2 classes) | — | — | 3.86 | — | — |

#### Key Metrics

| Metric | Value |
|--------|-------|
| Engine startup | 341 ns (0.3 µs, ~2.9M ops/sec) |
| Parse cache hit speedup | 147–192x |
| Memory (1000 ASTs) | ~0 KB (very compact) |
| WarmUp (2 classes, no annotations) | 3.9 µs |

### 11.4 Optimization Stages

| Stage | Optimization | Primary Effect |
|-------|-------------|----------------|
| 0 | Baseline | No caching, O(n) feature scan |
| 1 | LRU Expression Cache | Redundant parsing eliminated (147–192x) |
| 2 | resolveFeature O(1) + PropertyAccessorCache | EMF eGet() dispatch bypassed (generated models) |
| 3 | warmUp(EPackage) | First-access latency eliminated (~4 µs/pkg) |
| 4 | Feature Resolution Cache (1-entry) | Iterator hotpath: P7 −52%, P4 −7% |
| 5 | `OclSet`/`OclOrderedSet` hashed on `OclEqualityUtil.lookupKey` | Unique collections linear instead of quadratic: P5 (`allInstances()->select` over 50 000 instances) 10.9 s → 52 ms per evaluation |

### 11.5 Benchmark Test Plan (P1–P17)

| # | Test | What it measures |
|---|------|-----------------|
| P1 | Parse throughput | 10K parses of same expression |
| P2 | Simple property eval | 100K evals on same parsed AST |
| P3 | Collection pipeline | select→collect→size, 10K× |
| P4 | Nested iterators | forAll with closure, 1K× |
| P5 | Large model allInstances | 50K EObjects, allInstances→select |
| P6 | Deep navigation | 10-level containment tree |
| P7 | Closure on graph | 10K nodes, closure traversal |
| P8–P12 | Concurrent evaluations | 16 threads, various patterns |
| P13–P15 | Security limit stress | Rapid-fire range/closure/regex limits |
| P16 | Memory footprint | Parse 1000 expressions, measure heap |
| P17 | Engine startup time | Cold-start: OclEngines.create() |

---

## 12. Testing

### 12.1 Coverage Targets

| Component | Line Target | Branch Target | Measured |
|-----------|------------|---------------|----------|
| OclEvaluator | > 90% | > 85% | 91% / 83% |
| OclStdlib | > 85% | > 80% | 87% / 77% |
| OclAstBuilder | > 85% | > 70% | 88% / 68% |
| Delegate factories | > 90% | > 85% | 95% / 90% |
| API classes | > 80% | > 70% | Varies |

### 12.2 Test Model

```
Package: company (nsURI: http://company)
  Class: Company
    - name: EString
    - employees: Person [*]
  Class: Person
    - name: EString
    - age: EInt
    - salary: EDouble
    - isMarried: EBoolean
    - employer: Company [0..1]
```

### 12.3 Test Categories

| Category | Runner | Tag |
|----------|--------|-----|
| Unit (individual classes) | JUnit 5 | — |
| Integration (parse → evaluate) | JUnit 5 | — |
| Spec compliance (spec examples) | JUnit 5 | `@Tag("spec")` |
| Edge cases | JUnit 5 | — |
| Performance tests | JUnit 5 | `@Tag("perf")` |
| Eclipse comparison benchmarks | JUnit 5 | `@Tag("benchmark")` |
| OSGi integration | `*.itest` bundle | — |

### 12.4 Thread-Safety Testing

Every thread-safe component must have concurrent tests:
- Parse + evaluate from multiple threads simultaneously
- Mutate extension registries during evaluation
- Invoke EMF delegates from parallel threads
- Verify no `ConcurrentModificationException`, correct results

### 12.5 Performance Regression Policy

Performance tests run against Eclipse OCL under identical conditions (both standalone, no OSGi).
Results documented but not gating — the goal is awareness, not hard thresholds.

### 12.6 Coverage Gaps (DR-D25)

**Accepted gaps (not requiring tests):**
- `OclEngineComponent` (0%) — OSGi DS adapter, requires itest bundle
- `dispatchBoolean` (0%) — dead path; all booleans route through `evaluateThreeValuedBoolean`

**Gaps to close:**
- `@pre` postcondition integration (45% → target 85%)
- LENIENT null handling mode (0% branch → target 80%)
- Complete OCL operation context (pre/post/body, 64% → target 80%)
- Thread-safety verification (0 parallel tests → 5 core scenarios)

---

## 13. Design Decisions (OCL-specific)

### 13.1 Decision Summary

| # | Decision | Status |
|---|----------|--------|
| D11 | EMF delegate integration | Implemented |
| D13 | OCL v2.5 backward-compatible with v2.4 | Implemented |
| D14 | Configurable evaluation (strict/lenient) | Implemented |
| D16 | No Pivot layer — direct Ecore access | Implemented |
| D19 | `name: String` on OperationCallExp | Implemented |
| D20 | OclResult record for diagnostics | Implemented |
| D21 | OclExpressionParser interface in ocl.api | Implemented |
| — | Spec-audit decisions of 2026-10-30 (#313–#330), see §13.8 | Implemented, except the `inv` message (#324) |

### 13.2 DR-D14: Configurable Evaluation (Strict / Lenient)

Eclipse OCL's strict null handling drove Acceleo to replace OCL with AQL. We solve this at
the evaluation level without abandoning OCL.

`OclEvaluationOptions` provides:
- **STRICT** (default): Null property access → OclInvalid, propagates per spec
- **LENIENT**: Null → null/empty, collection ops on null → empty collection

Safe navigation (v2.5 `?.`, `?->`) provides **explicit** null-safety in expressions.
LENIENT mode provides **implicit** null-safety. Both coexist.

### 13.3 DR-D16: No Pivot Layer

See §6.7 above for full rationale. Key insight: UML-specific OCL operations are convenience
shortcuts for standard Ecore navigation — they validate the `OclOperationProvider` design.

### 13.4 DR-D22: MessageExp — Deferred

`MessageExp` (`^^` operator) for asynchronous operation calls in postconditions.
Neither Eclipse OCL nor QVT-O ever implemented it at runtime.

**Current:** Returns OclInvalid with diagnostic error.

**Future:** `OclMessageProvider` interface (API) + default EMF implementation (engine).

### 13.5 DR-D23: Security Hardening Limits

See §10 above. Configurable, per-evaluation resource limits in `OclEvaluationOptions`.
Defaults are generous for normal usage while preventing obvious attacks.

### 13.6 DR-D24: Performance Benchmark Design

All benchmarks run both engines under identical conditions:
- Plain Java 21, no OSGi
- 100 iterations warmup, 10,000 measurement iterations
- Shared Ecore test model, identical OCL expressions
- Metrics: ns/eval, ops/sec, MB heap

### 13.7 DR-D25: Test Coverage Gaps

See §12.6 above. Documents accepted gaps and gaps to close.

### 13.8 Spec-Audit Decisions (2026-10-30, #313–#330)

A section-by-section audit against OCL v2.4 led to these decisions. The rule was: follow the
specification; where its text and the Eclipse OCL default disagree, keep the Eclipse default and
offer the text behind `specStrict`; where Fennec deliberately differs from Eclipse, say so.

| Topic | Decision | Issue |
|-------|----------|-------|
| `specStrict` | New flag on `OclEvaluationOptions` / `OclConfiguration` / `ocl.specStrict` (default `false`). Switches: `any` without a match `null` → `invalid`; `one`/`isUnique` evaluate every body; `any` evaluates every body before returning a match; a decreasing range is `invalid` + ERROR instead of empty + WARNING; an untyped `let`/accumulator is an ERROR. The last one is reported at evaluation, since parsed expressions are cached across option sets | #316, #326 |
| Boolean operators with `null` | §11.5.4: after the short-circuits, `invalid` wins, then `null`. `true and null = null`; a non-Boolean operand is `invalid` + ERROR. Eclipse Pivot answers the same | #313 |
| Argument checking | A signature table for the §11.5 operations checks arity and argument kind; a mismatch is ERROR + `invalid` instead of coercion. A model operation called with no matching arity is a parse error | #314 |
| Non-Boolean iterator body | `select`/`reject`/`forAll`/`exists`/`any`/`one`: ERROR + `invalid`. `any` with a `null` body is `invalid` (it is defined by `select`) | #315 |
| `invalid` in collections | Never an element (§11.7.1 [1]): literal parts, `collect`/`collectNested`/`closure` bodies make the result `invalid`. `collect` flattens recursively. `sortedBy` with a `null` key is `invalid` | #316 |
| Numbers | Integer unbounded (`Long`, then `BigInteger`), Real `BigDecimal`, like Eclipse. `evaluate()` still returns a Real as `Double`; values written to a model are converted with `OclValues.toEcore`. `toString` plain decimal; `sum` concatenates Strings; `max`/`min` on Booleans `invalid` | #322 |
| Equality | Numbers by mathematical value, also in `isUnique`, `intersection`, `-`; objects by identity, not `EcoreUtil.equals` | #317, #318 |
| Navigation kind | A navigation yields the feature's collection kind, as a copy implementing `OclFeatureValue` | #319 |
| Implicit collect of operations | A dot on a collection is an implicit collect for operations too, OclAny operations included (as Eclipse Pivot); the collection itself needs `->` | #320 |
| Named iterators as implicit sources | §9.3.25 [A]: `self.employees->collect(p \| name)` reads `p.name`. **Deliberately differs from Eclipse.** Parameters therefore must be declared at parse time: `OclEngine.parse(expression, contextType, variables)` | #320, #324 |
| Iterator result types | §11.9: `select`/`reject` keep the source's collection type, `sortedBy`/`closure` are ordered or unique according to the source, `collect` is a Sequence or Bag of the flattened body type, `any` is the element type, and the quantifiers are Boolean. Collection literals are typed with the common supertype of their parts. A dot after `select` therefore collects; with every operation typed (§5.10) a dot after any operation answering a collection does too | #378 |
| Implicit operation source | An unqualified `op()` goes to the innermost implicit source whose type has it; OclAny operations to the innermost one; else `self`. All implicit sources are consulted, innermost first | #320 |
| Shadowing | A `let`, iterator or accumulator variable that hides a variable in scope is a parse error (§9.3.41 [1]). **Deliberately stricter than Eclipse** | #326 |
| Static well-formedness | `Collection{…}` literal, duplicate tuple parts and initial values that do not conform to the declared type are parse errors; so is every other expression whose types are known and do not fit (§5.10) | #326, #378 |
| Type operations | `oclIsKindOf`/`oclIsTypeOf` with collection types check kind and element type; `5.oclAsType(Real) = 5.0`; `5.oclIsKindOf(UnlimitedNatural) = false`, but `oclAsType(UnlimitedNatural)` accepts values `>= 0` (Eclipse); `invalid.toString()` is `invalid`; `oclIsInState` reports that it is unsupported | #321 |
| Unknown property | ERROR + `invalid` instead of an exception out of `evaluate()` | #327 |
| `allInstances()` | Enumerations, `Boolean`, `OclVoid` need no extent; `OclInvalid.allInstances()` is `invalid` | #328 |
| `@pre` / `oclIsNew()` | Outside `post:` a parse error in documents; ad hoc, `@pre` reads the current value with a WARNING and `oclIsNew()` is `invalid` + ERROR. Property `@pre` is captured per (object, feature) and read on the post-state object (A.3.2.2 (vii)). `op@pre()` is accepted next to `op()@pre`; an operation `@pre` is still evaluated once per node | #323 |
| Document `pre:`/`post:` | Checked by the invocation delegate, only for operations with an OCL delegate | #323 |
| Complete OCL | Form [B] context variable is `self`; parameters and `result` are variables; unknown operation/property contexts and conflicting `def`s are parse errors; operation `def`s keep their arity | #324 |
| `initial` | Evaluated like a derivation on every access (Eclipse-compatible); documented, not changed | #324 |
| `inv name('message'):` | To be evaluated as the violation message, like Eclipse. **Open:** needs a `messageExpression` containment on `Constraint` in `ocl.ecore`; until then the expression is parsed and dropped | #324 |
| Grammar | Complete OCL keywords are names in expressions (one grammar); restricted words after `::`; escaped names take string escapes and keep the doubled quote; supplementary-plane letters; `iterate` without an iterator. M2T's own grammar copy is not changed | #320, #325 |
| Qualified names | Package paths resolve through subpackages; the qualifier of `self.Type::feature` is checked; `Type::op()` is a call on `self` for a supertype, else an error (Ecore has no static operations) | #325 |
| Strings | `<`/`>`/`<=`/`>=`, `sortedBy`, `max`/`min` use `java.text.Collator` in `oclLocale`, ties by code point; `equalsIgnoreCase` via `toUpperCase(locale)`; positions count code points | #329 |

---

## 14. Future / Planned Work

### 14.1 OclAspectProvider (MetadataService Integration)

Integration with fennec's `MetadataService` / `AspectProvider` pattern (see §7.5).
Infrastructure already prepared: `OclConfiguration`, instance-based `PropertyAccessorCache`,
`warmUp(EPackage)`.

### 14.2 MessageExp Runtime Support (DR-D22)

`OclMessageProvider` interface for `^^` operator in postconditions.

### 14.3 UML Extension Bundle

Optional `org.eclipse.fennec.m2x.ocl.uml` providing UML-specific OCL operations
(`oclIsInState`, stereotype access) via `OclOperationProvider`.

### 14.4 Static typing in the other languages

GAP-3 (#378) types OCL and the OCL parts of QVT-R, MTL and QVT-O (stage B: QVT-O's builder uses
`AbstractExpressionBuilder` as `support`, see the QVT-O architecture §5.4). The constructs of QVT-R
and MTL themselves (templates, text and file blocks) carry no type yet.

### 14.5 Known Implementation Gaps

| # | Gap | Severity | Status |
|---|-----|----------|--------|
| GAP-1 | Complete OCL document parsing | Critical | Done |
| GAP-2 | `@pre` parsed but never evaluated | Critical | **Done** ✅ (PreStateSnapshot) |
| GAP-3 | Static type checker | Critical | Done (#378): OCL §5.10, QVT-O (QVT-O architecture §5.4) |
| GAP-6 | MessageExp has no evaluator case | Medium | Deferred (Eclipse also doesn't implement) |
| GAP-7 | `oclIsNew` absent | Medium | **Done** ✅ (compares against PreStateSnapshot) |

All other gaps (GAP-4, 5, 8–20) have been fixed.
