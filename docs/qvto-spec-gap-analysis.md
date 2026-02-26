# QVT-O v1.3 Spec-Gap-Analyse

> Systematische Analyse der Fennec M2M QVT-O Implementierung gegen die OMG QVT v1.3 Spec (Chapter 8, pp. 65–172).
> Erstellt: 2026-02-26 | Spec: `docs/specifications/QVT/QVT-formal-16-06-03.pdf`
> Siehe auch: [QVT-O Test Plan](qvto-test-plan.md), [QVT-O Architecture](qvto-architecture.md), [Design Decisions](design-decisions.md)

---

## Übersicht

**Fennec QVT-O Status:** 941 Tests, 0 Failures, 0 @Disabled, 1 @Tag("perf") — Phase 0–10 ✅

**Gesamtabdeckung:** ~97% der QVT-O v1.3 Spec. Die verbleibenden Gaps betreffen überwiegend:
- UML/MOF-spezifische Features (bewusst übersprungen, wie Eclipse)
- QVT-R Hybrid-Kopplung (Phase 4)

---

## Coverage-Matrix

### §8.1 — Operational Mappings Language (Semantik)

| Sektion | Thema | Status | Fennec-Tests | Gaps |
|---------|-------|--------|:------------:|------|
| §8.1.1 | Operational Transformation | ✅ | ~18 | GAP-1 (Collection-of-Models), ~~GAP-2 (@extent)~~ ✅ |
| §8.1.2 | Model Types | ✅ | ~12 | where-Clause nur geparst, nicht evaluiert |
| §8.1.3 | Extents, Models, Model Parameters | ✅ | ~15 | ~~GAP-2 (@extent Engine-Routing)~~ ✅ |
| §8.1.4 | Libraries | ✅ | ~52 | ~~GAP-3 (Blackbox Libraries)~~ ✅ |
| §8.1.5 | Mapping Operations | ✅ | ~90 | — |
| §8.1.6 | Object Expression | ✅ | ~19 | ~~GAP-2 (@extent in object/new)~~ ✅ |
| §8.1.7 | Scoped Identifiers | ✅ | implizit | — |
| §8.1.8 | Constructor Operations | ✅ | 11 | — |
| §8.1.9 | Helpers/Queries | ✅ | ~24 | — |
| §8.1.10 | Intermediate Data | ✅ | ~22 | — |
| §8.1.11 | Tracing and Resolving | ✅ | ~70 | GAP-4 (Persisted Trace) |
| §8.1.12 | Updating Objects | ✅ | implizit | — |
| §8.1.13 | Composing Transformations | ✅ | ~18 | — |
| §8.1.14 | Disjunct Mappings | ✅ | ~15 | — |
| §8.1.15 | Mapping Inheritance (inherits/merges) | ✅ | ~10 | — |
| §8.1.16 | Type Extensions (List, Dict, typedef, tag) | ✅ | ~20 | — |
| §8.1.17 | Imperative Expressions | ✅ | ~83 | — |
| §8.1.18 | Pre-defined Variables (this, self, result) | ✅ | ~17 | — |
| §8.1.19 | Null Semantics | ✅ | ~5 | — |
| §8.1.20 | Invalid/OclInvalid | ✅ | ~7 | — |
| §8.1.21 | Advanced (asTransformation, parallelTransform) | ✅ | ~18 | GAP-5 (asTransformation ohne dyn. Kompilierung) |

### §8.2.1 — QVTOperational Package (Metamodell)

| Sektion | EClass/Feature | Status | Gaps |
|---------|---------------|--------|------|
| §8.2.1.1 | OperationalTransformation | ✅ | GAP-6 (refined, relation → QVT-R) |
| §8.2.1.2 | Library | ✅ | — |
| §8.2.1.3 | Module | ✅ | D22: extends EPackage only (bewusst) |
| §8.2.1.4 | ModuleImport | ✅ | — |
| §8.2.1.5 | ModelParameter | ✅ | — |
| §8.2.1.6 | ModelType | ✅ | — |
| §8.2.1.7 | VarParameter | ✅ | D22: extends EParameter only (bewusst) |
| §8.2.1.8 | DirectionKind | ✅ | — |
| §8.2.1.9 | ImportKind | ✅ | — |
| §8.2.1.10 | ImperativeOperation | ✅ | — |
| §8.2.1.11 | MappingOperation | ✅ | GAP-6 (refinedRelation → QVT-R) |
| §8.2.1.12 | Helper | ✅ | — |
| §8.2.1.13 | Constructor | ✅ | — |
| §8.2.1.14 | EntryOperation | ✅ | — |
| §8.2.1.15 | ContextualProperty | ✅ | — |
| §8.2.1.16–18 | OperationBody, MappingBody, ConstructorBody | ✅ | — |
| §8.2.1.19 | MappingParameter | ✅ | — |
| §8.2.1.20 | MappingCallExp | ✅ | — |
| §8.2.1.21 | ObjectExp | ✅ | — |
| §8.2.1.22 | ResolveExp/ResolveInExp | ✅ | — |

### §8.2.2 — ImperativeOCL Package

| Sektion | EClass | Status | Tests | Gaps |
|---------|--------|--------|:-----:|------|
| §8.2.2.1 | ImperativeExpression | ✅ | implizit | — |
| §8.2.2.2 | BlockExp | ✅ | ja | — |
| §8.2.2.3 | ComputeExp | ✅ | ja | — |
| §8.2.2.4 | WhileExp | ✅ | ja | — |
| §8.2.2.5 | ImperativeLoopExp | ✅ | implizit | — |
| §8.2.2.6 | ForExp (forEach/forOne) | ✅ | ja | — |
| §8.2.2.7 | ImperativeIterateExp | ✅ | ja | — |
| §8.2.2.8 | SwitchExp + AltExp | ✅ | ja | — |
| §8.2.2.10 | VariableInitExp | ✅ | ja | — |
| §8.2.2.11 | AssignExp | ✅ | ja | — |
| §8.2.2.12 | UnlinkExp | ✅ | ja | ~~GAP-7~~ ✅ |
| §8.2.2.13 | TryExp | ✅ | ja | — |
| §8.2.2.14 | CatchExp | ✅ | ja | — |
| §8.2.2.15 | RaiseExp | ✅ | ja | — |
| §8.2.2.16 | ReturnExp | ✅ | ja | — |
| §8.2.2.17 | BreakExp | ✅ | ja | — |
| §8.2.2.18 | ContinueExp | ✅ | ja | — |
| §8.2.2.19 | LogExp | ✅ | ja | — |
| §8.2.2.20 | AssertExp | ✅ | ja | — |
| §8.2.2.21 | InstantiationExp | ✅ | ja | — |
| §8.2.2.23 | Typedef | ✅ | ja | — |
| §8.2.2.24–29 | List/Dict Types + Literals | ✅ | ja | D26: Aliases |

### §8.3 — Standard Library

| Sektion | Bereich | Ops gesamt | Implementiert | Gaps |
|---------|---------|:----------:|:------------:|------|
| §8.3.3 | Object (repr) | 1 | 1 | — |
| §8.3.4 | Element | 15 | 9 | 6 UML/MOF-spezifisch (P10-07) |
| §8.3.5 | Model | 9 | 9 | — |
| §8.3.6 | Transformation | 3 | 3 | — |
| §8.3.7 | Status | 3 | 3 | — |
| §8.3.8 | Dict | 9 | 9 | — |
| §8.3.9 | List | 47 | 47 | ~~GAP-8~~ ✅ |
| §8.3.10 | List Iterations | 11 | 11 | — |
| §8.3.11–15 | Collection/Bag/OrderedSet/Seq/Set | 15 | 15 | — |
| §8.3.16 | String | 35 | 35 | — |
| §8.3.17 | Numeric | 1 | 1 | ~~GAP-10~~ ✅ |
| §8.3.18 | Classifier | 1 | 1 | — |
| §8.3.19 | Predefined Tags | 4 | 4 | ~~GAP-11~~ ✅ |

### §8.4 — Concrete Syntax (EBNF vs. ANTLR4)

> Systematischer EBNF-Quercheck (2026-02-26): ~120 Produktionen analysiert, ~90–92% Abdeckung.
> Details: [EBNF-Quercheck Sektion](#ebnf-quercheck-spec-847-vs-qvtog4-2026-02-26)

| Bereich | Regeln gesamt | Implementiert | Gaps |
|---------|:------------:|:------------:|------|
| Top-Level | ~12 | ~11 | ~~GAP-12~~ ✅, GAP-13, N1 |
| Deklarationen | ~20 | ~17 | GAP-14, GAP-15, N2, N4 |
| Operationen | ~15 | ~15 | ~~GAP-16~~ ✅, N8 |
| Expressions | ~30 | ~28 | ~~GAP-17~~ ✅, ~~GAP-18~~ ✅, ~~GAP-19~~ ✅, ~~GAP-20~~ ✅, ~~GAP-21~~ ✅, GAP-22, N3, N5–N7 |
| Kommentare | 3 | 2 | ~~GAP-23~~ ✅ |

---

## Vollständig implementierte Sektionen

Die folgenden Spec-Sektionen sind **vollständig implementiert** und durch Tests abgedeckt:

- **§8.1.5 Mapping Operations** — when/where, init/population/end, inhibition, instantiation, multiple results, direction-kind, inherits/merges/disjuncts (90+ Tests)
- **§8.1.8 Constructor Operations** — Deklaration, Invokation via `new`, Default-Constructor (11 Tests)
- **§8.1.9 Helpers/Queries** — Context-Type, inout, expression-body, void, primitiver Kontext (24+ Tests)
- **§8.1.10 Intermediate Data** — Intermediate class, intermediate property, static, readonly, <<id>>, init-expressions (22 Tests)
- **§8.1.11 Tracing and Resolving** — Alle 8 resolve-Varianten, late resolve, trace records, inhibition (70+ Tests)
- **§8.1.4 Libraries** — Regular + Blackbox Libraries, programmatische Blackbox-API, `QvtoBlackboxRegistry`, synthetische Module, `from ... import` selektive Sichtbarkeit (52 Tests)
- **§8.1.13 Composing Transformations** — access, extends, new T(), transform(), parallelTransform(), wait() (18 Tests)
- **§8.1.14 Disjunct Mappings** — Explicit + implicit disjuncts, candidate ordering (15 Tests)
- **§8.1.15 Mapping Inheritance** — inherits, merges, execution order (10 Tests)
- **§8.1.16 Type Extensions** — List, Dict, typedef, tag (20 Tests)
- **§8.1.17–20 Imperative Expressions, Variables, Null, Invalid** — Alle imperativen Konstrukte (112+ Tests)
- **§8.2.2 ImperativeOCL Package** — Alle EClasses bis auf UnlinkExp
- **§8.3.5–9 Standard Library** — Model, Transformation, Status, Dict, List Operationen (vollständig)

---

## Gaps (detailliert)

### GAP-1: Collection of Models als Model-Parameter

| | |
|---|---|
| **Spec-Referenz** | §8.1.1, S. 65 |
| **Was die Spec fordert** | `in uml : Sequence(UML)` — Model-Parameter kann Collection-Typ haben |
| **Aktueller Stand** | Grammar parst `typeSpec` mit `typeExpression`, aber Engine behandelt Model-Extents nicht als Collection |
| **Eclipse QVT-O** | Nicht unterstützt |
| **Aufwand** | S (klein) |
| **Priorität** | Niedrig — Eclipse ignoriert das ebenfalls |

### GAP-2: `@extent`-Annotation (Extent-Routing) — ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.1.1 S. 66, §8.1.3 S. 68, §8.1.6 S. 68, §8.4 S. 170 |
| **Was die Spec fordert** | `object x : T @ srcmodel { ... }`, `new Column@mymodel(n,t)` — explizite Extent-Zuweisung |
| **Aktueller Stand** | ✅ Implementiert. Grammar, Parser (ExpressionBuilder) und Engine (QvtoEvaluator) unterstützen `@extentName` für `objectExp` und `newExp`. 5 E2E-Tests in `QvtoE2eExtentRoutingTest`. |
| **Eclipse QVT-O** | Unterstützt |
| **Aufwand** | M (mittel) — Parser-Verarbeitung + Engine-Extent-Routing |
| **Priorität** | Mittel — relevant für Multi-Extent-Transformationen mit Packages im selben Metamodell |

### GAP-3: Blackbox Libraries — ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.1.4, S. 68–69 |
| **Was die Spec fordert** | `blackbox library BL { ... }` — externe Java-Implementierung als Library einbinden |
| **Aktueller Stand** | ✅ Implementiert (P10-08, 28 Tests). Programmatische API: `QvtoBlackboxLibrary` mit `BlackboxOperationDescriptor` + `invoke()`. `QvtoBlackboxRegistry` + `BasicQvtoBlackboxRegistry` für Registrierung. Linker erzeugt synthetische Library-Module. Engine dispatcht via `isBlackbox`-Flag. Kontextuelle + module-level Operationen. `QvtoBlackboxInvocationContext` für Diagnostics, Config, Extents. |
| **Eclipse QVT-O** | Unterstützt via Java-Annotationen |
| **Aufwand** | M (mittel) |
| **Priorität** | Mittel — Power-Feature für Praxis-Anwendung |

### GAP-4: Persisted Trace Data

| | |
|---|---|
| **Spec-Referenz** | §8.1.11.8, S. 79 |
| **Was die Spec fordert** | Trace-Daten persistieren und für Re-Execution wiederverwenden |
| **Aktueller Stand** | EMF Trace Model wird parallel befüllt und in `QvtoExecutionResult` exportiert. Keine Mechanik zum Laden für Re-Execution. |
| **Eclipse QVT-O** | Nicht unterstützt |
| **Aufwand** | M (mittel) |
| **Priorität** | Niedrig — Spec sagt selbst: "left unspecified" |

### GAP-5: `asTransformation()` ohne dynamische Kompilierung

| | |
|---|---|
| **Spec-Referenz** | §8.1.21, S. 87; §8.3.5.7 |
| **Was die Spec fordert** | Dynamisches Casten eines Models zu einer Transformation zur Laufzeit |
| **Aktueller Stand** | Implementiert (P10-03, 6 Tests), aber ohne dynamische Kompilierung. Extrahiert vorregistrierte OT aus Extent. |
| **Eclipse QVT-O** | Nicht vollständig implementiert |
| **Aufwand** | L (groß) — Runtime-Compiler |
| **Priorität** | Niedrig — bewusste Einschränkung |

### GAP-6: QVT-R Hybrid-Kopplung (`refined`, `relation`, `refinedRelation`)

| | |
|---|---|
| **Spec-Referenz** | §8.2.1.1 S. 92, §8.2.1.11 |
| **Was die Spec fordert** | `OperationalTransformation.refined: Transformation[0..1]`, `relation: Relation[*]`, `MappingOperation.refinedRelation` |
| **Aktueller Stand** | Nicht im Metamodell. `refines` Keyword nicht im Parser. |
| **Eclipse QVT-O** | Eingeschränkt unterstützt |
| **Aufwand** | L (groß) — QVT-R Metamodell-Integration |
| **Priorität** | Phase 4 (QVT-D) — bewusst deferred |

### ~~GAP-7: UnlinkExp~~ ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.2.2.12 |
| **Was die Spec fordert** | `feature.unlink(item)` — explizites Entfernen aus Multi-Valued Properties |
| **Aktueller Stand** | ✅ Implementiert als Operation-Dispatch in `QvtoOperationProvider`. `list.remove(item)` auf der live EMF-EList. 5 E2E-Tests. |
| **Eclipse QVT-O** | Nur Stub (Evaluator returns null) — Fennec geht über Eclipse hinaus (Spec hat Vorrang) |
| **Aufwand** | S (klein) — Operation-Dispatch-Logik, kein eigenes Grammar-Keyword nötig |
| **Priorität** | Niedrig |

### ~~GAP-8: List::deepclone() für mutable Collections mit EObject-Elementen~~ ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.3.9.13, §8.3.11.3 |
| **Was die Spec fordert** | `deepclone()` auf mutable List kopiert jedes EObject-Element per `EcoreUtil.copy()` |
| **Aktueller Stand** | ✅ Implementiert in `QvtoOperationProvider`. EObject-Elemente per `EcoreUtil.copy()`, Primitives as-is. 4 E2E-Tests. |
| **Eclipse QVT-O** | Implementiert |
| **Aufwand** | S (klein) |
| **Priorität** | Niedrig |

### GAP-9: String Counter API (5 Operationen) ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.3.16.31–35 |
| **Was die Spec fordert** | `startStrCounter()`, `getStrCounter()`, `incrStrCounter()`, `restartAllStrCounter()`, `addSuffixNumber()` |
| **Aktueller Stand** | ✅ Implementiert in `QvtoOperationProvider` — 5 Operationen, 7 Tests |
| **Eclipse QVT-O** | Implementiert |
| **Aufwand** | S (klein) — 5 Methoden im QvtoOperationProvider |
| **Priorität** | Niedrig — sehr selten genutzt |

### ~~GAP-10: Integer::range(start, end) als benannte Operation~~ ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.3.17 |
| **Was die Spec fordert** | `Integer::range(start, end) : List(Element)` als Methoden-Call |
| **Aktueller Stand** | ✅ Implementiert in `OclStdlib.dispatchInteger()`. Beide Varianten: `source.range(end)` (1 arg) und `source.range(start, end)` (2 args). 6 E2E-Tests. |
| **Eclipse QVT-O** | Deklariert als UNSUPPORTED_OPER — Fennec geht über Eclipse hinaus (Spec hat Vorrang) |
| **Aufwand** | S (klein) |
| **Priorität** | Niedrig |

### ~~GAP-11~~: Predefined Tags (topclasses, rememberChanges, proxy) — ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.3.19 |
| **Was die Spec fordert** | Tags `topclasses`, `rememberChanges`, `proxy` mit definierter Semantik |
| **Aktueller Stand** | ✅ Implementiert. `alias` (QvtoAliasRegistry), `proxy` und `topclasses` (QvtoTagRegistry). `proxy`: Tag wird registriert, `isProxy()` API. `topclasses`: Validierung in `addToExtent()` — nicht-erlaubte Root-Typen werden nicht hinzugefügt. `rememberChanges`/`manuallyChanged`: bewusst ignoriert (wie Eclipse). 6 E2E-Tests in `QvtoE2ePredefTagsTest`. |
| **Eclipse QVT-O** | Nur `alias` implementiert. Alle anderen Tags nicht. |
| **Aufwand** | S (klein) |
| **Priorität** | Niedrig |

### GAP-12: `from <unit> import <names>` Syntax — ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.4, S. 164 |
| **Was die Spec fordert** | `from mylib import helper1, helper2;` — selektiver Import |
| **Aktueller Stand** | ✅ Implementiert (P10-08). Grammar: `from qualifiedName import importedNameList;` mit Wildcard (`*`) und selektiver Namensliste. `ModuleImport.importedNames` für Sichtbarkeitsfilterung. `QvtoOperationResolver` + `QvtoOperationProvider` filtern nach `importedNames`. 11 Tests (4 E2E + 7 Parser-AST). |
| **Eclipse QVT-O** | Implementiert |
| **Aufwand** | M (mittel) |
| **Priorität** | Mittel — nützlich für Multi-File Composition |

### GAP-13: `refines` Keyword bei Transformation

| | |
|---|---|
| **Spec-Referenz** | §8.4, S. 164–165 |
| **Was die Spec fordert** | `transformation T(...) refines R_UML2RDBMS { ... }` |
| **Aktueller Stand** | Nicht im Parser |
| **Eclipse QVT-O** | Eingeschränkt |
| **Aufwand** | S (klein) für Parser, L für Semantik |
| **Priorität** | Niedrig — Phase 4 (QVT-R) |

### GAP-14: Inline `metamodel`/`package` Syntax

| | |
|---|---|
| **Spec-Referenz** | §8.4, S. 166 |
| **Was die Spec fordert** | `metamodel MM { class Foo { ... }; enum Bar { ... }; }` — Inline-Metamodell-Definition |
| **Aktueller Stand** | Komplett nicht implementiert. Nur `intermediate class` als Classifier. |
| **Eclipse QVT-O** | Teilweise implementiert |
| **Aufwand** | L (groß) |
| **Priorität** | Niedrig — in der Praxis fast nie genutzt (man nutzt Ecore-Dateien) |

### GAP-15: `datatype`, `primitive`, `exception` Classifier-Deklarationen

| | |
|---|---|
| **Spec-Referenz** | §8.4, S. 166 |
| **Was die Spec fordert** | `datatype MyData { ... };`, `primitive MyPrim;`, `exception MyEx;` als eigenständige Classifier |
| **Aktueller Stand** | Nur `intermediate class` implementiert |
| **Eclipse QVT-O** | Teilweise |
| **Aufwand** | M (mittel) |
| **Priorität** | Niedrig |

### GAP-16: Mapping-Deklaration ohne Body — ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.4, S. 168 |
| **Was die Spec fordert** | `mapping T::f(...);` — Deklaration ohne Body (für abstrakte/blackbox Mappings) |
| **Aktueller Stand** | ✅ Implementiert — Grammatik erlaubt `mappingDef` ohne Body, Parser setzt `body=null`, Evaluator führt inherited/merged auch bei bodyless Mappings aus |
| **Eclipse QVT-O** | Unterstützt |
| **Aufwand** | S (klein) |
| **Tests** | 3 Parse-Tests (bodyless, blackbox bodyless, bodyless+extensions), 2 E2E-Tests (bodyless+inherits, bodyless standalone) |

### GAP-17: `-=` Assign-Operator — ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.4, S. 169 |
| **Was die Spec fordert** | `<assign_op> ::= ':=' | '::=' | '+=' | '-='` |
| **Aktueller Stand** | ✅ Alle vier Operatoren implementiert. `-=` entfernt Elemente aus Collections. Metamodell: `AssignExp.isSubtract`. |
| **Eclipse QVT-O** | Implementiert |
| **Aufwand** | S (klein) |
| **Priorität** | Niedrig |

### GAP-18: `!=` als Vergleichs-Alternative — ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.4.4 item 6 |
| **Was die Spec fordert** | `==` als Synonym für `=`, `!=` als Synonym für `<>` (mit Directive) |
| **Aktueller Stand** | ✅ `!=` implementiert (wie Eclipse). `==` bewusst nicht (Eclipse auch nicht). Directive nicht implementiert (Eclipse auch nicht). |
| **Eclipse QVT-O** | Nur `!=`, kein `==`, keine Directive |
| **Tests** | `QvtoE2eLexerNotEqualTest` (5 Tests: basic, equal values, mixed with `<>`, null, xselectOne-Koexistenz) |

### GAP-19: `%` Binary Format Operator — ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.4.4 item 4 |
| **Was die Spec fordert** | `"blabla %s" % myvar` als Shorthand für `String::format()` |
| **Aktueller Stand** | ✅ Implementiert. `%` als MultExp in Grammar, `format`/`%` in OclStdlib.dispatchString(). Einzelwert und Collection-Argumente unterstützt. |
| **Eclipse QVT-O** | Implementiert |
| **Tests** | `QvtoShorthandParseTest.formatOperator_parsesCorrectly`, `QvtoE2eShorthandTest.formatOperator_formatsString/multipleArgs/calledAsMethod` (4 Tests) |

### GAP-20: `#`, `##`, unary `*` Shorthand-Operatoren — ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.4.4 items 1–3 |
| **Was die Spec fordert** | `#MyClass` → `oclIsKindOf(MyClass)`, `##MyClass` → `oclIsTypeOf(MyClass)`, `*"stereo"` → `stereotypedBy("stereo")` |
| **Aktueller Stand** | ✅ Implementiert. `HASH`/`DOUBLE_HASH` Lexer-Tokens, `HashExp`/`DoubleHashExp`/`UnaryStarExp` Grammar-Regeln. Im Iterator-Kontext (`->select(#Type)`) automatische Desugaring zu IteratorExp mit Iterator-Variable als Source. `*"stereo"` → `OclInvalid` (UML-spezifisch, P10-07). |
| **Eclipse QVT-O** | Implementiert |
| **Tests** | `QvtoShorthandParseTest` (3 Tests: hash, doubleHash, unaryStar), `QvtoE2eShorthandTest` (5 Tests: hash/doubleHash mit primitiven + Ecore-Typen, unaryStar) |
| **Bugfix** | `objectsOfType`/`subobjectsOfType`/`allSubobjectsOfType` — `==` Identity-Check durch `isExactType()` ersetzt (nsURI+Name-Vergleich, robust über EPackage-Instanzen hinweg, §8.3.5.3) |

### GAP-21: `!->` (not-arrow) Operator — ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.4, S. 169: `<access_op> ::= '.' | '->' | '!->'` |
| **Was die Spec fordert** | `!->` als Access-Operator: `list!->isEmpty()` → `not(list->isEmpty())` |
| **Aktueller Stand** | ✅ Implementiert. `NOT_ARROW` Lexer-Token, `!->` in ArrowExp. Semantik: Ergebnis wird mit `OperationCallExp("not")` gewrappt. |
| **Eclipse QVT-O** | Implementiert |
| **Tests** | `QvtoShorthandParseTest.notArrowOperator_parsesCorrectly`, `QvtoE2eShorthandTest.notArrow_negatesBoolean/emptyCollection` (3 Tests) |

### GAP-22: Intermediate Class Features (Multiplicity, Opposites, Operations, generische Stereotypen)

| | |
|---|---|
| **Spec-Referenz** | §8.4, S. 166–167 |
| **Was die Spec fordert** | `[0..1]`, `[*]`, `ordered`, `opposites ~refName`, Methoden in Intermediate Classes, `<<name1, name2>>` generisch |
| **Aktueller Stand** | Multiplicity, opposites, classifier_operation fehlen. Stereotyp nur hardcoded `<<id>>`. `'derived'` als Feature-Key fehlt (N4). |
| **EBNF-Quercheck** | N4 (`'derived'` Feature-Key), N9 (`<classifier_operation>` in ICs — mittlere Relevanz) bestätigen diesen Gap |
| **Eclipse QVT-O** | Teilweise |
| **Aufwand** | M (mittel) |
| **Priorität** | Niedrig |

### GAP-23: `//` als Zeilenkommentar — ✅ DONE

| | |
|---|---|
| **Spec-Referenz** | §8.4.2 |
| **Was die Spec fordert** | `//` neben `--` als Zeilenkommentar |
| **Aktueller Stand** | ✅ Implementiert — `LINE_COMMENT` in `QvtO.g4` überschrieben mit `('--' \| '//')` |
| **Eclipse QVT-O** | Unterstützt `//` |
| **Tests** | `QvtoE2eLexerCommentTest` (4 Tests: slash-only, mixed, string-safety) |

---

## Bewusst übersprungene Features

### P10-07: UML-spezifische Operationen (§8.3.4)

Wie Eclipse QVT-O werden folgende UML/MOF-spezifische Operationen **bewusst nicht implementiert**:

| Operation | Spec-Ref | Begründung |
|-----------|----------|------------|
| `_localId()` | §8.3.4.1 | UML/MOF-spezifisch |
| `_globalId()` | §8.3.4.2 | UML/MOF-spezifisch |
| `markedAs()` | §8.3.4.12 | UML TaggedValue / MOF::Tag |
| `markValue()` | §8.3.4.13 | UML TaggedValue / MOF::Tag |
| `stereotypedBy()` | §8.3.4.14 | UML Stereotype |
| `stereotypedStrictlyBy()` | §8.3.4.15 | UML Stereotype |

**Begründung:** Fennec M2M zielt auf Ecore/EMF, nicht auf UML. Eclipse überspringt diese ebenfalls.

### Bewusste Metamodell-Abweichungen (Design Decisions)

| Abweichung | Spec | Fennec | Begründung |
|-----------|------|--------|------------|
| Module Vererbung | extends Class + Package | extends EPackage only | D22: EMF codegen rejected dual inheritance |
| VarParameter Vererbung | extends Variable + Parameter | extends EParameter + representedParameter ref | D22: Name-Konflikt vermieden |
| Dict/List als EClasses | DictionaryType, ListType (§8.2.2.24–25) | Runtime-Aliases (D26) | Effizienter, Parser-Level genügt |
| Implicit Relation | Jedes Mapping hat implizite QVT-R Relation | Nicht modelliert | QVT-R Konzept, kein Runtime-Feature |
| `_INTERMEDIATE` Extent | Separater Extent für IC-Instanzen | QvtoIntermediatePropertyStore | Wie Eclipse |

---

## Konsolidierte Gap-Tabelle (nach Priorität)

### Priorität: Mittel

| # | Gap | Spec-Ref | Aufwand | Eclipse |
|---|-----|----------|---------|---------|
| GAP-2 | `@extent`-Annotation (Extent-Routing) | §8.1.3, §8.1.6, §8.4 | M | ✅ DONE |
| GAP-3 | Blackbox Libraries (Java-Integration) | §8.1.4 | M | ✅ DONE |
| GAP-12 | `from <unit> import <names>` Syntax | §8.4 | M | ✅ DONE |
| GAP-18 | `!=` Vergleichs-Alternative | §8.4.4 | S | ✅ DONE |
| GAP-23 | `//` Zeilenkommentar | §8.4.2 | S | ✅ DONE |
| GAP-17 | `-=` Assign-Operator | §8.4 | S | ✅ DONE |

### Priorität: Niedrig

| # | Gap | Spec-Ref | Aufwand | Eclipse |
|---|-----|----------|---------|---------|
| GAP-1 | Collection of Models als Parameter | §8.1.1 | S | ❌ |
| GAP-4 | Persisted Trace Data | §8.1.11.8 | M | ❌ |
| GAP-5 | asTransformation dynamische Kompilierung | §8.1.21 | L | ❌ |
| ~~GAP-7~~ | ~~UnlinkExp~~ | ~~§8.2.2.12~~ | ~~S~~ | ✅ DONE |
| ~~GAP-8~~ | ~~List::deepclone mit EObject-Elementen~~ | ~~§8.3.9.13~~ | ~~S~~ | ✅ DONE |
| ~~GAP-9~~ | ~~String Counter API (5 Ops)~~ | ~~§8.3.16.31–35~~ | ~~S~~ | ✅ DONE |
| ~~GAP-10~~ | ~~Integer::range als Operation~~ | ~~§8.3.17~~ | ~~S~~ | ✅ DONE |
| ~~GAP-11~~ | ~~Predefined Tags (topclasses etc.)~~ | ~~§8.3.19~~ | ~~S~~ | ✅ DONE |
| GAP-13 | `refines` Keyword | §8.4 | S/L | Teilw. |
| GAP-14 | Inline metamodel/package Syntax | §8.4 | L | Teilw. |
| GAP-15 | datatype/primitive/exception Classifier | §8.4 | M | Teilw. |
| ~~GAP-16~~ | ~~Mapping-Deklaration ohne Body~~ | ~~§8.4~~ | ~~S~~ | ✅ DONE |
| ~~GAP-19~~ | ~~`%` Format-Operator~~ | ~~§8.4.4~~ | ~~S~~ | ✅ DONE |
| ~~GAP-20~~ | ~~`#`, `##`, `*` Shorthand-Operatoren~~ | ~~§8.4.4~~ | ~~M~~ | ✅ DONE |
| ~~GAP-21~~ | ~~`!->` Not-Arrow Operator~~ | ~~§8.4~~ | ~~S~~ | ✅ DONE |
| GAP-22 | IC: Multiplicity, Opposites, Operations | §8.4 | M | Teilw. |

### Priorität: Phase 4 (QVT-R/QVT-D)

| # | Gap | Spec-Ref | Aufwand |
|---|-----|----------|---------|
| GAP-6 | refined, relation, refinedRelation | §8.2.1.1, §8.2.1.11 | L |

---

## Cross-Check mit bestehendem Test-Plan

Alle in `qvto-test-plan.md` dokumentierten Phase-10-Gaps sind in dieser Analyse referenziert:

| P10-ID | Status | In dieser Analyse |
|--------|--------|-------------------|
| P10-01 | ✅ DONE | §8.1.13, §8.3.6 — vollständig |
| P10-02 | ✅ DONE | §8.3.7 — vollständig |
| P10-03 | ✅ DONE | §8.3.5.7 — vollständig (GAP-5 für dyn. Kompilierung) |
| P10-04 | ✅ DONE | §8.1.10 — static intermediate property sharing |
| P10-05 | ✅ DONE | §8.1.10 — <<id>> Stereotyp |
| P10-06 | ✅ DONE | §8.3.5 — removeElement + read-only guards |
| P10-07 | ⏭️ Übersprungen | §8.3.4 — UML-spezifische Ops (bewusst) |
| P10-08 | ✅ DONE | §8.1.4 (GAP-3), §8.4 (GAP-12) — Blackbox Libraries + `from...import` |

---

## EBNF-Quercheck: Spec §8.4.7 vs. QvtO.g4 (2026-02-26)

Systematischer Abgleich aller ~120 EBNF-Produktionen aus §8.4.7 (S. 164–171) gegen `QvtO.g4`.

**Ergebnis:** ~90–92% Abdeckung. Alle Abweichungen sind niedrig-prioritär.

### Neue Micro-Findings (N1–N10)

| # | Finding | Spec-Stelle | Schwere | Beschreibung |
|---|---------|-------------|---------|--------------|
| N1 | Standalone `access` Deklaration | §8.4.7 S. 165–166 | Gering | `access lib;` als eigenständiges `<unit_element>` und `<module_element>` fehlt. In Fennec nur im Transformation-Header (`moduleUsage`). |
| N2 | `<moduleref>` mit Signatur | §8.4.7 S. 166 | Gering | Spec erlaubt `<moduleref> <simple_signature>?` (parametrisierte Module). Fennec hat nur den Namen. Eclipse ignoriert das auch. |
| N3 | `'unlimited'` Keyword | §8.4.7 S. 169 | Gering | Spec hat `'unlimited'` als Literal-Alternative. Fennec hat `'*'` (OCL-kanonisch). Trivial zu ergänzen. |
| N4 | `'derived'` Feature-Key | §8.4.7 S. 167 | Gering | `'derived'` fehlt als `<feature_key>` in `classifierFeatureModifier`. |
| N5 | `object` mit Iterator | §8.4.7 S. 170 | Gering | `object ('(' <iter_declarator> ')')` Form (forEach-over-Object) fehlt. Selten genutzt. |
| N6 | `switch` mit Iterator-Deklarator | §8.4.7 S. 170 | Gering | Spec hat `<iter_declarator>`, Fennec hat nur Expression als Switch-Wert. |
| N7 | `var` mit Klammern | §8.4.7 S. 171 | Gering | `'var' '(' <declarator_list> ')'` Form fehlt. Selten genutzt. |
| N8 | Qualifier vor `constructor` | §8.4.7 S. 168 | Gering | `<qualifier>*` vor `constructor` fehlt. Kein `blackbox constructor` möglich. |
| N9 | Operations in Intermediate Classes | §8.4.7 S. 167 | Mittel | `<classifier_operation>` (Methoden-Deklarationen in ICs) fehlt. → GAP-22 erweitern. |
| N10 | Dict-Key Typ | §8.4.7 S. 170 | — | Fennec ist **permissiver** (beliebige Expression als Key, Spec nur `<literal_simple>`). Kein Bug. |

### Bewusste EXTRA-Erweiterungen (nicht in Spec)

| # | Erweiterung | Herkunft | Beschreibung |
|---|------------|---------|--------------|
| E1 | `?.` / `?->` Safe Navigation | OCL v2.5 | Null-sichere Navigation |
| E2 | `this` | Eclipse QVT-O | Synonym für Transformation-Instanz (Spec: nur implicit) |
| E3 | `entry` statt `main` | Eclipse QVT-O | Alternatives Entry-Point-Keyword |
| E4 | `catch` statt `except` | Eclipse QVT-O | Synonym für Exception-Handler |
| E5 | `elseif` statt `elif` | OCL | Alternatives Keyword |
| E6 | Double-Quoted Strings | Eclipse QVT-O | `"string"` neben `'string'` |
| E7 | `!=` statt `<>` | §8.4.4 | Implementiert wie Eclipse (mit Spec-Basis) |
| E8 | Adjacent String Concatenation | Eclipse QVT-O | `'abc' 'def'` → `'abcdef'` |
| E9 | Imperative If (Block-Syntax) | Eclipse QVT-O | `if (cond) { ... }` neben OCL-`if then endif` |

### Bestätigte bekannte Gaps

Die folgenden bereits bekannten Gaps wurden durch den EBNF-Quercheck bestätigt:

| Gap | EBNF-Stelle |
|-----|-------------|
| GAP-13 (`refines`) | `<transformation_refine>`, `<mapping_refinement>` (S. 165, 168) |
| GAP-14 (metamodel/package) | `<metamodel>`, `<metamodel_h>`, `<metamodel_element>` (S. 167) |
| GAP-15 (datatype/primitive/exception) | `<classifier_info>`, `<enumeration>` (S. 167) |
| GAP-19 (`%` Operator) | `<mult_op>` (S. 169) |
| GAP-20 (`#`, `##`, `*`) | `<unary_op>` (S. 169) |
| GAP-21 (`!->`) | `<access_op>` (S. 169) |
| GAP-22 (IC Features) | `<classifier_feature>`, `<multiplicity>`, `<opposite_property>`, `<stereotype_qualifier>` (S. 167) |

---

## Empfehlung: Quick Wins

Folgende Gaps können mit geringem Aufwand (S) geschlossen werden und verbessern die Eclipse-Kompatibilität:

1. ~~**GAP-23** — `//` Zeilenkommentar (eine Lexer-Regel)~~ ✅ DONE
2. ~~**GAP-18** — `!=` Vergleichs-Alternative (Lexer + Parser)~~ ✅ DONE
3. ~~**GAP-17** — `-=` Assign-Operator (Grammar + Evaluator)~~ ✅ DONE
4. ~~**GAP-9** — String Counter API (5 Methoden in QvtoOperationProvider)~~ ✅ DONE
5. ~~**GAP-10** — Integer::range als Operation (1 Methode)~~ ✅ DONE
6. ~~**GAP-7** — UnlinkExp (Operation-Dispatch)~~ ✅ DONE
7. **N3** — `'unlimited'` Keyword (1 Lexer-Alternative)
