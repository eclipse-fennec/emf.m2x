# Phase 3 — MOFM2T (Model-to-Text) Implementation Plan

## Context

Phase 1 (OCL) and Phase 2 (QVT-O) are complete. Phase 3 implements MOFM2T 1.0 — the OMG specification for template-based model-to-text transformations.

**Module name:** `org.eclipse.fennec.m2x.m2t.*`

**References:**
- MOFM2T 1.0 Spec: `docs/specifications/MOFM2T/`
- Eclipse Acceleo 3.7 (MOFM2T-compliant): `/opt/git/m2m/repositories/eclipse-acceleo-3.7/`

## Modules

| Module | Description | Status |
|--------|-------------|--------|
| `org.eclipse.fennec.m2x.m2t.model` | EMF metamodel (2 enums, 17 EClasses) | P3-0 DONE |
| `org.eclipse.fennec.m2x.m2t.api` | Public API (M2tEngine, M2tConfiguration, etc.) | P3-0 DONE |
| `org.eclipse.fennec.m2x.m2t.parser` | ANTLR4 parser (text_explicit + code_explicit) | P3-1 TODO |
| `org.eclipse.fennec.m2x.m2t.engine` | Template evaluator + writer | P3-2 TODO |
| `org.eclipse.fennec.m2x.m2t.tests` | JUnit 5 tests (inline + file-based) | P3-1+ TODO |

## Metamodel Design Decisions

- **ModuleElement** does NOT extend `ENamedElement` — has own `name : EString` attribute to avoid raw-type `EList` issues with dual inheritance (Block + ModuleElement)
- **Query.returnType / Macro.returnType** — named `returnType` (not `type`) to avoid conflict with inherited `OclExpression.type`
- **VisibilityKind** — enum `{public, private, protected}` instead of spec's binary `isPublic : Boolean` (Acceleo extension)
- **OpenModeKind** — enum `{overwrite, append}` (spec calls it `AppendModeKind`)
- **Module** extends `EPackage` (not `EPackage + Template` as some readings suggest)

## Sub-Phases

### P3-0: Metamodel + Project Infrastructure (DONE)
- EMF metamodel with 2 enums + 17 EClasses
- Bundle structure (bnd.bnd, .project, .classpath, .settings)
- API interfaces (M2tEngine, M2tConfiguration, M2tContext, M2tResult, M2tParseException, M2tGenerationStrategy)
- Stub grammar (M2t.g4)
- Build verification: all modules compile

### P3-1: Parser (ANTLR4)
- Full M2t.g4 grammar (text_explicit + code_explicit modes)
- Lexer modes: TEXT_MODE <-> CODE_MODE
- M2tParserSupport facade
- Parser tests

### P3-2: API + Core Engine
- M2tEngineImpl (composition with OclEngineImpl)
- M2tEvaluator (switch-based interpreter)
- M2tEvalEnvironment (variable stack)
- Whitespace handling (standalone blocks, BOL indicator)
- Template resolution (guard, overrides, parameter matching)
- Tests

### P3-3: FileBlock + Writer
- Writer stack (M2tStringWriter, M2tFileWriter)
- FileBlock evaluation (URL, openMode, charset)
- M2tGenerationStrategy SPI
- Tests

### P3-4: Module Composition + Protected Areas
- Module import/extends
- Visibility rules
- Protected areas (merge on re-generation)
- TraceBlock
- Tests

### P3-5: Macros + Standard Library
- Macro definition + invocation (Body parameter)
- 15 MOFM2T string operations as OclOperationProvider
- Tests

### P3-6: Post-Expression + Polishing
- Template post-expression
- Encoding handling
- End-to-end tests (spec examples)
- Regression check (OCL + QVT-O tests)

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
