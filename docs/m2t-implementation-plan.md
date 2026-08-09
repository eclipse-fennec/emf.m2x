# Phase 3 — MOFM2T (Model-to-Text) Implementation Plan

## Context

Phase 1 (OCL) and Phase 2 (QVT-O) are complete. Phase 3 implements MOFM2T 1.0 — the OMG specification for template-based model-to-text transformations.

**Module name:** `org.eclipse.fennec.m2x.m2t.*`

**References:**
- MOFM2T 1.0 Spec: `docs/specifications/MOFM2T/`
- Eclipse Acceleo 3.7 (MOFM2T-compliant): `/opt/git/m2m/repositories/eclipse-acceleo-3.7/`

**Spec-Compliance Rule:** All behavior MUST conform to the MOFM2T 1.0 specification (§8.1–§8.4). Features beyond the spec (e.g., Acceleo extensions like `protected` visibility, charset in FileBlock) are explicitly marked as extensions. When in doubt, the spec has priority over Eclipse Acceleo behavior. Gaps between our implementation and the spec must be discussed before proceeding.

**Acceleo 3.7 Test Reference:** The Acceleo 3.7 engine tests at `/opt/git/m2m/repositories/eclipse-acceleo-3.7/tests/org.eclipse.acceleo.engine.tests/` provide 575+ `.mtl` template files with reference results in `src-gen/ReferenceResult/`. These MUST be consulted when implementing and testing each feature to validate spec-conformance. Key test directories:
- `data/IfBlock/`, `data/ForBlock/`, `data/LetBlock/` — block evaluation
- `data/FileBlock/` — file generation
- `data/TemplateInvocation/` — invocation patterns
- `data/Resolution/Override/`, `data/Resolution/Namesake/` — template resolution
- `data/Library/` — standard library operations
- `data/Variables/` — variable scoping
- `data/Service/` — Java service integration

## Modules

| Module | Description | Status |
|--------|-------------|--------|
| `org.eclipse.fennec.m2x.m2t.model` | EMF metamodel (2 enums, 17 EClasses) | ✅ P3-0 DONE |
| `org.eclipse.fennec.m2x.m2t.api` | Public API (M2tEngine, M2tConfiguration, etc.) | ✅ P3-0 DONE |
| `org.eclipse.fennec.m2x.m2t.parser` | ANTLR4 parser + CST→AST builder | ✅ P3-1 DONE |
| `org.eclipse.fennec.m2x.m2t.engine` | Template evaluator + writer + engine facade + stdlib | ✅ P3-2 DONE, P3-3 DONE |
| `org.eclipse.fennec.m2x.m2t.tests` | JUnit 5 tests (176 tests, 0 failures) | 🔄 Ongoing |

## Metamodel Design Decisions

- **ModuleElement** does NOT extend `ENamedElement` — has own `name : EString` attribute to avoid raw-type `EList` issues with dual inheritance (Block + ModuleElement)
- **Query.returnType / Macro.returnType** — named `returnType` (not `type`) to avoid conflict with inherited `OclExpression.type`
- **VisibilityKind** — enum `{public, private, protected}` instead of spec's binary `isPublic : Boolean` (Acceleo extension)
- **OpenModeKind** — enum `{overwrite, append}` (spec calls it `AppendModeKind`)
- **Module** extends `EPackage` (not `EPackage + Template` as some readings suggest)

## Completed Sub-Phases

### P3-0: Metamodel + Project Infrastructure ✅
- EMF metamodel with 2 enums + 17 EClasses
- Bundle structure (bnd.bnd, .project, .classpath, .settings)
- API interfaces (M2tEngine, M2tConfiguration, M2tContext, M2tResult, M2tParseException, M2tGenerationStrategy)
- Build verification: all modules compile

### P3-1: Parser (ANTLR4) ✅
- Full M2t.g4 grammar (text_explicit mode, lexer modes TEXT_MODE ↔ CODE_MODE)
- M2tParserSupport facade (CST parsing)
- M2tExpressionBuilder (OCL expression building via AbstractExpressionBuilder)
- M2tModuleBuilder (CST→AST visitor)
- 34 parser tests (M2tParserSupportTest + M2tModuleBuilderTest)

### P3-2: Core Engine ✅ (with gaps — see below)
- AbstractExpressionBuilder refactoring (shared OCL expression building)
- M2tEvalEnvironment (mutable scope-stack)
- M2tEvaluator (switch-based interpreter)
- M2tEngine (facade, parse + execute)
- M2tWriterStack (nested file/string output)
- M2tProtectedAreaMerger (hash-based smart merge — D31)
- 21 engine tests (M2tEngineTest)

---

## Spec-Compliance Audit (against MOFM2T 1.0 §8.1–§8.4)

### Metamodel (§8.1) — Feature Status

| Spec Feature | §Ref | Metamodell | Parser | Evaluator | Tests | Status |
|-------------|------|-----------|--------|-----------|-------|--------|
| Module (name, input, extends, imports) | §8.1.1 | ✅ | ✅ | ✅ | ✅ | ✅ P3-4 DONE |
| ModuleElement (name, isPublic) | §8.1.2 | ✅ | ✅ | ✅ | ✅ | OK |
| Template (params, guard, body) | §8.1.3 | ✅ | ✅ | ✅ | ✅ | OK |
| Template overrides + super | §8.1.3 | ✅ | ✅ | ✅ | ✅ | ✅ GAP-M2T-1 FIXED |
| Template post-expression | §8.1.3 | ✅ | ✅ | ✅ | ✅ 1 Test | OK |
| Block (body, init) | §8.1.4 | ✅ | ✅ | ✅ | ✅ | OK |
| InitSection | §8.1.5 | ✅ | ✅ | ✅ | ✅ 1 Test | OK |
| TemplateExpression | §8.1.6 | ✅ | ✅ | ✅ | ✅ | OK |
| ProtectedAreaBlock (marker) | §8.1.7 | ✅ | ✅ | ✅ | ✅ | OK |
| ProtectedAreaBlock merge (D31) | §8.1.7 | — | — | ✅ hash + smart merge | ✅ 15 Tests | ✅ D31 DONE |
| ForBlock (loopVar, iterSet, guard, before/each/after) | §8.1.8 | ✅ | ✅ | ✅ | ✅ 5 Tests | OK |
| QueryInvocation | §8.1.9 | ✅ | ⚠️ | ✅ eval | ✅ 2 Tests | OK |
| TemplateInvocation (def, args, before/each/after) | §8.1.10 | ✅ | ✅ | ✅ | ⚠️ basic | OK-ish |
| TemplateInvocation super | §8.1.10 | ✅ | ✅ | ✅ | ✅ | ✅ GAP-M2T-1 FIXED |
| TemplateInvocation set-argument iteration | §8.1.10 | — | — | ✅ cross-product | ✅ 3 Tests | ✅ GAP-M2T-2 FIXED |
| Query (params, returnType, expression) | §8.1.15 | ✅ | ✅ | — | ✅ CST | OK |
| LetBlock (letVar, body) | §8.1.16 | ✅ | ✅ | ✅ | ✅ | OK |
| LetBlock (elseLet, else) | §8.1.16 | ✅ | ✅ parsed | ✅ subtype-test chain | ✅ 4 Tests | ✅ GAP-M2T-3 FIXED |
| FileBlock (fileUrl, openMode) | §8.1.17 | ✅ | ✅ append=true/false | ✅ APPEND+OVERWRITE | ✅ 6 Tests | ✅ GAP-M2T-4 FIXED |
| FileBlock (uniqId) | §8.1.17 | ✅ | ✅ 3rd param = uniqId | ✅ evaluated + stored | ✅ 2 Tests | ✅ GAP-M2T-5 FIXED |
| TraceBlock (modelElement) | §8.1.18 | ✅ | ✅ | ⚠️ Stub | ❌ | Deferred |
| Macro (params, Body param) | §8.1.19 | ✅ | ✅ | ✅ eval | ✅ 2 Tests | OK |
| MacroInvocation | §8.1.20 | ✅ | ✅ | ✅ eval | ✅ 2 Tests | OK |
| IfBlock (ifExpr, elseIf, else) | §8.1.21 | ✅ | ✅ | ✅ | ✅ | OK |

### Concrete Syntax (§8.2)

| Feature | Status | Notes |
|---------|--------|-------|
| text_explicit mode | ✅ | Default mode, fully implemented |
| code_explicit mode | ❌ | Deferred — low priority, rarely used |
| Comments `// ...` in code blocks | ✅ | |

### Library (§8.3) — ✅ GAP-M2T-6 FIXED (M2tStandardLibrary as OclOperationProvider)

| Operation | §Ref | Status | Notes |
|-----------|------|--------|-------|
| `substitute(String r, String t) : String` | §8.3.1 | ✅ | `String.replace()` — all occurrences |
| `index(String r) : Integer` | §8.3.1 | ✅ | 1-based, -1 if not found |
| `first(Integer n) : String` | §8.3.1 | ✅ | |
| `last(Integer n) : String` | §8.3.1 | ✅ | |
| `strstr(String r) : Boolean` | §8.3.1 | ✅ | |
| `toUpper() : String` | §8.3.1 | ✅ | MOFM2T name (OCL has `toUpperCase()`) |
| `toLower() : String` | §8.3.1 | ✅ | MOFM2T name (OCL has `toLowerCase()`) |
| `strtok(String s1, Integer flag) : String` | §8.3.1 | ✅ | Stateful StringTokenizer cache |
| `strcmp(String s1) : Integer` | §8.3.1 | ✅ | |
| `isAlpha() : Boolean` | §8.3.1 | ✅ | |
| `isAlphanum() : Boolean` | §8.3.1 | ✅ | |
| `toUpperFirst() : String` | §8.3.1 | ✅ | |
| `toLowerFirst() : String` | §8.3.1 | ✅ | |
| `Integer.toString() : String` | §8.3.2 | ⚠️ | Covered by OCL `toString()` |
| `Real.toString() : String` | §8.3.3 | ⚠️ | Covered by OCL `toString()` |

21 tests in `M2tStandardLibraryTest`.

### Whitespace Handling (§8.4) — ✅ GAP-M2T-7 FIXED (M2tWhitespaceNormalizer + WhitespaceMode D32)

| Rule | Status | Notes |
|------|--------|-------|
| Template body starts at next line after head | ✅ | Body-trimming (leading newline after head) |
| Trailing newline before tail stripped | ✅ | Body-trimming (trailing newline + whitespace before tail) |
| Standalone block: strip whitespace before head | ✅ | Standalone block detection + whitespace stripping |
| Multi-line for: default separator = newline | ✅ | `\n` separator injected for standalone for-blocks without explicit separator |
| Standalone template invocation: indent propagation | ✅ | Indent extracted at parse-time, applied at eval-time via `fitIndentationTo()` |
| BOL indicator `^` | ✅ | SPEC mode: `^` marker strips leading whitespace on that line. ACCELEO mode: `^` passed through as literal text (Acceleo 3.7 does not support BOL) |

17 tests in `M2tWhitespaceTest` (body-trimming 3, standalone-block 6, BOL 3 (2 SPEC + 1 ACCELEO), indent-propagation 3, SPEC-mode 1, NONE-mode 1).

---

## Open Gaps (Prioritized)

### Priority 1 — ✅ ALL FIXED

**GAP-M2T-3: LetBlock elseLet/else chain** (§8.1.16) — ✅ FIXED
- `M2tEvaluator.caseLetBlock()` now implements spec-conformant subtype-test chain
- `tryLetBlock()` helper: evaluate init → if non-null bind+execute, else try next
- 3 new tests (null→else, elseLet-chain, non-null-skips-else)

**GAP-M2T-4: FileBlock openMode** (§8.1.17) — ✅ FIXED (was already correct)
- `caseFileBlock()` reads `block.getOpenMode()` and passes to `pushFileWriter()`
- `M2tWriterStack` handles APPEND by prepending existing content
- 2 new tests (append concatenation, overwrite replacement)

**GAP-M2T-6: Standard Library** (§8.3) — ✅ FIXED
- `M2tStandardLibrary` implements `OclOperationProvider` with 13 String operations
- Auto-registered by `M2tEngine` via `addOperationProvider()`
- 21 tests in `M2tStandardLibraryTest`

### Priority 2 — ✅ ALL FIXED

**GAP-M2T-2: Set-argument iteration in TemplateInvocation** (§8.1.10) — ✅ FIXED
- `caseTemplateInvocation()` rewritten: evaluates arguments, detects set args with singleton params
- Cross-product computation for k set-arguments via `crossProduct()` helper
- `invokeTemplate()` helper for clean parameter binding
- 3 new tests (set iteration with separator, before/after, singleton no-iteration)

**GAP-M2T-5: FileBlock uniqId** (§8.1.17) — ✅ FIXED
- `caseFileBlock()` evaluates optional `block.getUniqId()` and stores via `writers.setFileUniqueId()`
- `M2tWriterStack.getFileUniqueIds()` exposes the mapping
- `M2tResult.fileUniqueIds()` added to the API record
- 1 new test (uniqId stored in writer)

**GAP-M2T-1: Template overrides + super** (§8.1.3, §8.1.10) → ✅ FIXED in P3-4
- `M2tModuleLinker` resolves overrides declarations, links super invocations
- Override resolution: parameter type narrowing, guard priority
- super keyword evaluates overridden template
- Verified against Acceleo 3.7 `data/Resolution/Override/` tests

**GAP-M2T-7: Whitespace Handling** (§8.4) → ✅ FIXED in P3-5
- `M2tWhitespaceNormalizer`: body-trimming, standalone block detection, BOL indicator `^`, indent extraction
- `WhitespaceMode` enum: `NONE` (raw), `SPEC` (strict §8.4 incl. BOL `^`), `ACCELEO` (default, like SPEC but without BOL `^` — passed through as literal)
- Normalizer runs after linking in `M2tEngine.execute()` (needs resolved TemplateInvocations)
- Indent-propagation via `fitIndentationTo()` in `M2tEvaluator.caseTemplateInvocation()`
- 17 tests in `M2tWhitespaceTest` — see D32

### Priority 3 — Deferred (low priority)

- **TraceBlock semantics** — traceability dictionary, deferred to Phase 5
- **code_explicit mode** — rarely used syntax variant, deferred
- **Macro Body parameter** — spec says last parameter must be type Body

### Missing Tests (to add alongside gap fixes)

| Feature | Status |
|---------|--------|
| Template post-expression | ✅ 1 Test |
| InitSection | ✅ 1 Test |
| QueryInvocation | ✅ 2 Tests |
| MacroInvocation | ✅ 2 Tests (body + init section) |
| For before/after | ✅ 1 Test |
| For guard | ✅ 2 Tests (filter + empty result) |
| Nested blocks | ✅ 3 Tests (for-in-if, if-in-for, let-in-for) |
| LetBlock elseLet/else | ✅ 4 Tests (from GAP-M2T-3) |
| FileBlock APPEND | ✅ 3 Tests (from GAP-M2T-4) |
| Protected area merge (D31) | ✅ 15 Tests: marker format (4), smart merge (8), engine E2E (3) |
| Template invocation before/each/after | ✅ 3 Tests (from GAP-M2T-2) |

---

## Remaining Sub-Phases

### P3-3: Evaluator Gap Fixes + Tests — ✅ DONE
**Goal:** Fix all evaluator gaps that don't require cross-module linking, add missing tests.

- [x] **GAP-M2T-3** Fix LetBlock elseLet/else evaluation ✅
- [x] **GAP-M2T-4** Fix FileBlock openMode (was already correct) ✅
- [x] **GAP-M2T-6** Standard Library (§8.3) as OclOperationProvider ✅
- [x] **GAP-M2T-2** Set-argument iteration in TemplateInvocation ✅
- [x] **GAP-M2T-5** FileBlock uniqId ✅
- [x] **D31** Protected Area Smart Merge ✅ — 15 tests
- [x] Add tests for: post-expression (1), InitSection (1), QueryInvocation (2), MacroInvocation (2), for before/after/guard (3), nested blocks (3) ✅ — 12 tests
- [x] Verify against Acceleo 3.7 engine tests ✅ — 11 reference comparison tests (ForBlock 7, IfBlock 3, GenericEngine 1)
- [x] Fix: ForBlock separator/before/after now evaluated lazily (separator inside loop with `i` in scope)
- [x] Fix: Loop counter `i` is 1-based (Acceleo convention)

### P3-4: Module Composition + Template Resolution ✅
**Goal:** Cross-module linking, visibility rules, overrides, super.

- [x] Module import resolution ✅
- [x] Module extends resolution ✅
- [x] Template name→definition linking (for parsed templates) ✅
- [x] Visibility enforcement (public/private/protected) ✅
- [x] **GAP-M2T-1** Template overrides linking + evaluation ✅
- [x] **GAP-M2T-1** super keyword in TemplateInvocation ✅
- [x] Override resolution rules (parameter type matching, guard priority) ✅
- [x] Tests with multi-module templates ✅
- [x] Verify against `data/Resolution/Override/` Acceleo tests ✅

### P3-5: Whitespace Handling (§8.4) ✅
**Goal:** Spec-conformant whitespace (§8.4) with configurable `WhitespaceMode` (D32).

- [x] **GAP-M2T-7** Standalone block detection + whitespace stripping ✅
- [x] Default newline separator for standalone for-blocks ✅
- [x] Indent propagation for standalone template invocations ✅
- [x] BOL indicator `^` (SPEC mode only — ACCELEO passes `^` through as literal) ✅
- [x] `WhitespaceMode` enum (NONE/SPEC/ACCELEO) with ACCELEO as default ✅
- [x] 17 whitespace tests in `M2tWhitespaceTest` ✅

### P3-6: Polishing + End-to-End ✅
**Goal:** Spec examples, encoding, regression.

- [x] Acceleo reference tests switched to exact whitespace comparison (11 tests) ✅
- [x] Spec §7 end-to-end tests (9 tests in `M2tSpecExampleTest`) ✅
- [x] Additional Acceleo reference tests: Variables (2), LetBlock (3), TemplateInvocation (3) ✅
- [x] Encoding verification tests (2 tests in `M2tEngineTest.EncodingTests`) ✅
- [x] Full regression: OCL (4202) + QVT-O (1050) + M2T (195) = all pass ✅

---

## Workflow Rules

1. **Spec-first:** Always read the relevant MOFM2T 1.0 spec section BEFORE implementing
2. **Acceleo-3.7-verify:** Check Acceleo 3.7 tests for expected behavior (`data/` templates + `src-gen/ReferenceResult/` expected output)
3. **Gaps → ask user:** When a gap is discovered between our implementation and the spec/Acceleo behavior, present it to the user for decision (fix now, defer, or skip)
4. **Tests before code:** Write failing tests first, then implement the fix
5. **Never adapt tests to implementation** — tests define spec behavior, not the other way around

## Verification

After each sub-phase:
```bash
cd /opt/git/m2m/workspace
./gradlew org.eclipse.fennec.m2x.m2t.tests:test
```

End-of-phase regression:
```bash
./gradlew org.eclipse.fennec.m2x.ocl.tests:test
./gradlew org.eclipse.fennec.m2x.qvto.tests:test
```
