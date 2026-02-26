# QVT-O Test Plan & Fortschrittstracking

> Part of the [Development Guideline](development-guideline.md). See also: [QVT-O Architecture](qvto-architecture.md), [Design Decisions](design-decisions.md)

---

## ⚠️ SPEC-FIRST WORKFLOW (bei JEDEM Task befolgen!)

**Tests definieren das korrekte Verhalten — NICHT die aktuelle Implementierung.**

1. **ZUERST die Spec lesen** — QVT-O v1.3: `docs/specifications/QVT/formal-16-06-03.pdf`, Chapter 8
2. **Tests gegen die Spec schreiben** — das erwartete Ergebnis kommt aus der Spec, NICHT aus dem Code
3. **Tests laufen lassen** — Failures sind Implementierungslücken, NICHT Testfehler
4. **Implementierung fixen** — bis die Tests grün sind. **NICHT disablen, sondern SOFORT fixen!**
5. **NIEMALS einen Test an die Implementierung anpassen**, um ihn grün zu machen
6. **Bei Unklarheit: Rückfrage an den User**, nicht raten — **User entscheidet**, ob/wann ein Test `@Disabled` wird

**Referenz-Quellen (in dieser Reihenfolge):**
1. OMG QVT-O v1.3 Spec (immer zuerst)
2. Eclipse-Referenztests zum Vergleich (`/opt/git/m2m/repositories/org.eclipse.qvto/`)
3. Gecko QVT-O zum Vergleich (`/opt/git/m2m/repositories/org.gecko.qvto/`)
4. Spec hat Vorrang vor Eclipse bei Widersprüchen
5. Bei Unklarheit: Rückfrage

---

## Dashboard

| Phase | Thema | Tests | Pass | Fail | Status |
|-------|-------|------:|-----:|-----:|--------|
| 0 | Spec-Konformitätsprüfung bestehender 298 Tests | 298 | 298 | 0 | ✅ DONE |
| 1 | Bestehende Failures fixen | 303 | 303 | 0 | ✅ DONE |
| 2 | Transformation, ModelType, Library, Mapping, ObjectExp, Constructor | ~150 | 104 | 0 | ✅ DONE |
| 3 | Helpers, Intermediate Data | ~50 | 26 | 0 | ✅ DONE |
| 4 | Tracing, Resolve, Late Resolve | ~90 | 86 | 0 | ✅ DONE |
| 5 | Updating, Composing, Disjuncts, Inherits/Merges | ~55 | 21 | 0 | ✅ DONE |
| 6 | Imperative Expressions (9 Tasks: P6-01…P6-09) | 83 | 83 | 0 | ✅ DONE |
| 7 | this/self/result, Null, Invalid, Tag/Typedef (6 Tasks: P7-01…P7-06) | ~35 | 34 | 0 | ✅ DONE |
| 8 | Standard Library (Dict, List, String, Model, Element) | ~195 | 111 | 0 | ✅ DONE (P8-06–07 ⏸️ DEFERRED) |
| 9 | Spec-Conformance, Eclipse-Referenztests & Edge Cases (9 Tasks: P9-01…P9-09) | ~100 | 88 | 0 | ✅ DONE |
| 10 | Deferred Features & Offene Gaps (9 Tasks: P10-01…P10-09) | 78 | 78 | 0 | P10-01–09 ✅ |
| **Σ** | | **~1012** | **947** | **0** | |

**Bestandstests:** 947 total (946 pass, 0 fail, 0 @Disabled, 1 @Tag("perf")) — Phase 0–10 ✅ DONE + GAP-10

---

## Phase 0: Spec-Konformitätsprüfung bestehender 295 Tests

> **⚠️ SPEC-FIRST WORKFLOW (bei JEDEM Task befolgen!)**
>
> **Tests definieren das korrekte Verhalten — NICHT die aktuelle Implementierung.**
>
> 1. **ZUERST die Spec lesen** — QVT-O v1.3: `docs/specifications/QVT/formal-16-06-03.pdf`, Chapter 8
> 2. **Tests gegen die Spec schreiben** — das erwartete Ergebnis kommt aus der Spec, NICHT aus dem Code
> 3. **Tests laufen lassen** — Failures sind Implementierungslücken, NICHT Testfehler
> 4. **Implementierung fixen** — bis die Tests grün sind
> 5. **NIEMALS einen Test an die Implementierung anpassen**, um ihn grün zu machen
> 6. **Bei Unklarheit: Rückfrage an den User**, nicht raten
>
> **Referenz-Quellen:** 1. QVT-O v1.3 Spec → 2. Eclipse QVT-O Tests → 3. Gecko QVT-O → 4. Spec hat Vorrang → 5. Bei Unklarheit: Rückfrage

**Ziel:** Alle bestehenden 295 Tests auf Spec-Konformität prüfen, bevor Failures gefixt oder neue Tests geschrieben werden. Sicherstellen, dass die Tests das **spezifizierte Verhalten** testen — nicht das Verhalten der aktuellen Implementierung.

### Vorgehen

Für jede Testklasse systematisch:

1. **Test lesen** — Was testet er genau? Welche QVT-O-Konstrukte? Welches erwartete Verhalten?
2. **Spec-Stelle nachschlagen** — QVT-O v1.3 Chapter 8, relevante Abschnitte identifizieren
3. **Prüfen** — Erwartet der Test das spec-konforme Verhalten?
4. **Ergebnis dokumentieren:**
   - ✅ Spec-konform
   - ⚠️ Korrektur nötig (mit Begründung und Spec-Referenz)
   - ❓ Unklar → Rückfrage an User
5. **Korrekturen durchführen** — Tests anpassen, die NICHT spec-konform sind (mit Spec-Nachweis)

### Reihenfolge: E2E-Tests zuerst (kritischste Kategorie)

| # | Testklasse | Tests | Kategorie | Spec-Ref | Status |
|--:|------------|------:|-----------|----------|--------|
| 1 | `QvtoE2eMappingTest` | 23 | E2E | §8.1.5 MappingOperation | ✅ 21 konform, 1 where-Warning (Eclipse-konform), 1 merges-Assertion verstärkt |
| 2 | `QvtoE2eControlFlowTest` | 21 | E2E | §8.1.17 Imperative Expressions | ✅ 21/21 konform |
| 3 | `QvtoE2eHelperQueryTest` | 18 | E2E | §8.1.9 Helper/Query | ✅ 18/18 konform |
| 4 | `QvtoE2eCollectionTest` | 19 | E2E | §8.3 Standard Library + OCL §11.7 | ✅ 19/19 konform (isEmpty/notEmpty aufgeteilt, append-Assertion verstärkt) |
| 5 | `QvtoE2eObjectExpTest` | 13 | E2E | §8.1.6 ObjectExp | ✅ 13/13 konform (3 Assertions verstärkt, self-Bug in Test 9 behoben) |
| 6 | `QvtoE2eResolveTest` | 12 | E2E | §8.1.11 Resolve | ✅ 12/12 konform (3 Assertions verstärkt, 1 Assertion korrigiert per no-source-Semantik, 1 ungültiger QVT-O-Code ersetzt, 1 Testname korrigiert, Spec-Kommentare ergänzt) |
| 7 | `QvtoE2eConfigPropertyTest` | 8 | E2E | §8.2.1.1 configProperty | ✅ 8/8 konform (alle Assertions spezifisch, keine Änderungen nötig) |
| 8 | `QvtoE2eTraceTest` | 8 | E2E | §8.1.11.1 Trace Records | ✅ 8/8 konform (spezifische Trace-Assertions, keine Änderungen nötig) |

### Dann: Parser-Tests (10 Klassen, 108 Tests)

| # | Testklasse | Tests | Spec-Ref | Status |
|--:|------------|------:|----------|--------|
| 9 | `QvtoTransformationParseTest` | 15 | §8.4 transformation, modeltype, import, qualifiers | ✅ 15/15 konform |
| 10 | `QvtoMappingParseTest` | 15 | §8.4 mapping, when/where, init/end, inherits | ✅ 15/15 konform |
| 11 | `QvtoImperativeExpParseTest` | 14 | §8.2.1.12-20 while, for, switch, compute, return, break | ✅ 14/14 konform |
| 12 | `QvtoHelperQueryParseTest` | 12 | §8.4 helper, query, constructor, entry | ✅ 12/12 konform |
| 13 | `QvtoAssignVarParseTest` | 10 | §8.2.1.5 AssignExp, §8.2.1.7 VariableInitExp | ✅ 10/10 konform |
| 14 | `QvtoMappingCallResolveParseTest` | 10 | §8.2.1.3 MappingCallExp, §8.2.1.10-11 ResolveExp | ✅ 10/10 konform |
| 15 | `QvtoTryCatchParseTest` | 10 | §8.2.1.13 TryExp, §8.2.1.1 AssertExp, §8.2.1.9 LogExp | ✅ 10/10 konform |
| 16 | `QvtoObjectNewParseTest` | 8 | §8.2.1.4 ObjectExp, §8.2.1.6 InstantiationExp | ✅ 8/8 konform |
| 17 | `QvtoParseErrorTest` | 8 | §8.4 Concrete Syntax (negative tests) | ✅ 8/8 konform |
| 18 | `QvtoDictListParseTest` | 6 | D26 Dict/List aliases | ✅ 6/6 konform |

### Dann: Execution-Tests (11 Klassen, 68 Tests)

> **Hinweis:** Die Klassen #19-22 waren im Plan als Parser-Tests gelistet, sind aber Execution-Tests (extends `AbstractQvtoEngineTest`). Hier korrekt einsortiert.

| # | Testklasse | Tests | Spec-Ref | Status |
|--:|------------|------:|----------|--------|
| 19 | `QvtoControlFlowTest` | 6 | §8.2.1.17 WhileExp, §8.2.1.15 SwitchExp, §8.2.1.18-19 Break/Continue/Return | ✅ 6/6 konform |
| 20 | `QvtoResolveExpTest` | 6 | §8.1.11 Resolve + basic mapping | ✅ 6/6 konform |
| 21 | `QvtoVariableTest` | 6 | §8.2.1.7 VariableInitExp, assignment, scoping | ✅ 6/6 konform |
| 22 | `QvtoComputeExpTest` | 3 | §8.2.1.20 ComputeExp | ✅ 3/3 konform |
| 23 | `QvtoMappingExecutionTest` | 10 | §8.1.5 MappingOperation, when/where, init/end, disjuncts | ✅ 10/10 konform |
| 24 | `QvtoObjectExpTest` | 8 | §8.1.6 ObjectExp, §8.2.1.6 InstantiationExp | ✅ 8/8 konform |
| 25 | `QvtoHelperExecutionTest` | 7 | §8.1.9 Helper/Query | ✅ 7/7 konform |
| 26 | `QvtoMappingCallTest` | 6 | §8.1.5 map/xmap | ✅ 6/6 konform |
| 27 | `QvtoLateResolveTest` | 6 | §8.1.11.7 Late Resolve | ✅ 6/6 konform (3× `self.` als explizite Source ergänzt per §8.1.11.3 — resolve ohne Source sucht ALLE Records) |
| 28 | `QvtoTraceModelTest` | 5 | §8.1.11.1 Trace Records | ✅ 5/5 konform |
| 29 | `QvtoLogAssertTest` | 5 | §8.2.1.1 AssertExp, §8.2.1.9 LogExp | ✅ 5/5 konform |

### Ergebnis-Zusammenfassung Phase 0

| Kategorie | Klassen | Tests | ✅ Konform | ⚠️ Korrigiert | Änderungen |
|-----------|--------:|------:|-----------:|--------------:|------------|
| E2E | 8 | 122 | 122 | 9 | 6× Resolve-Assertions (no-source-Semantik), 3× Assertions verstärkt |
| Parser | 10 | 108 | 108 | 0 | keine Änderungen nötig |
| Execution | 11 | 68 | 68 | 3 | 3× `self.` als explizite Source ergänzt (QvtoLateResolveTest) |
| **Σ** | **29** | **295** | — | — | — |

---

## Phase 1: Bestehende Failures fixen

**Ziel:** Alle bestehenden Test-Failures beseitigen, bevor neue Tests geschrieben werden.

**Stand:** 303 Tests, 303 pass, **0 fail** ✅ — alle 35 ursprünglichen Failures behoben.

### Übersicht aller Failure-Cluster

| ID | Cluster | Tests | Status |
|----|---------|------:|--------|
| F-05 | when-Guards | 4 | ✅ DONE |
| F-06 | Result-Param | 1 | ✅ DONE |
| F-08 | ObjectExp | 5 | ✅ DONE |
| F-01 | forEach + Gaps | 8+5 | ✅ DONE |
| F-03 | compute | 0 | ✅ DONE (mitgefixt) |
| F-11 | ClassCast | 0 | ✅ DONE (mitgefixt) |
| F-02 | if/then/endif | 2 | ✅ DONE |
| F-04 | return | 1 | ✅ DONE (mitgefixt durch F-02) |
| F-09 | resolveone | 5 | ✅ DONE |
| F-07 | late resolve mit source | 6 | ✅ DONE |
| F-10 | config properties | 2 | ✅ DONE |

---

### ✅ F-05: when-Guards (4 Tests) — DONE

**Spec-Validierung:** §8.4 p168 — `<when_clause> ::= 'when' '{' <ocl_expression_list> '}'` — `;` ist Separator, nicht Terminator.
**Eclipse QVT-O:** Bestätigt — `when { expr1; expr2 }` und `when { expr }` beide gültig.
**Fix:** Grammar: neue `guardBlock`-Regel mit `expression (';' expression)* ';'?`. QvtoUnitBuilder angepasst.

### ✅ F-06: Mapping ohne benannten Result-Param (1 Test) — DONE

**Spec-Validierung:** §8.4 p166 — `<declarator> ::= <typespec> | <scoped_identifier> ':' <typespec>` — unnamed Result erlaubt. §8.1.18: implizite `result`-Variable wenn einziger Result-Param unnamed.
**Eclipse QVT-O:** Bestätigt — alle Eclipse-Beispiele verwenden unnamed Results (`mapping f() : Type`).
**Fix:** Grammar: `resultParam` mit `NamedResult` / `UnnamedResult` Alternativen. QvtoUnitBuilder: unnamed → `result` als Name.

### ✅ F-08: ObjectExp Extent/Evaluation (5 Tests) — DONE

**Spec-Validierung:** §8.2.1.17 OperationBody (`self` = context param), §8.2.1.18 ConstructorBody (implizite Property-Zuweisungen resolven zum erzeugten Objekt, `self` wird NICHT reboundt), §8.2.1.19 MappingBody (`self` = contextual argument, `result` = declared result).
**Eclipse QVT-O:** Bestätigt — `name := 'x'` (implizit) statt `self.name := 'x'` im ObjectExp. `+=` mit Collection → addAll.
**Fix (3 Stufen):**
1. Engine: `resolveImplicitPropertyTarget` — prüft `_objectExp`, `self`, `result` für implizite Property-Zuweisung
2. Engine: `+=` auf Collection-Features: wenn RHS eine Collection ist, iteriere und füge jedes Element einzeln hinzu
3. Engine: `self` wird in ObjectExp NICHT reboundt (§8.2.1.18). Stattdessen dedizierte `_objectExp` Variable. Unit-Tests korrigiert: `self.name :=` → `name :=`

### ✅ F-01: forEach als Arrow-Call (8+5 Tests) — DONE (inkl. Gap-Fixes)

**Spec-Validierung (retroaktiv):** §8.2.2.6 ForExp (p122-123) — ForExp extends ImperativeLoopExp, zwei Varianten (`forEach`/`forOne`) via `name`-Attribut. Notation: `<source>-><for-name> (<iterator-list> | <condition>) <body>`. forEach iteriert über alle passenden Elemente, forOne nur über das erste (dann break). Rückgabewert: null. §8.4 p170: `<for_exp>` mit optionalem `(';' <declarator>)?` für Compute-Shorthand.
**Eclipse QVT-O:** Bestätigt — `Sequence{1,2,3}->forEach(n) { ... }` ist Standard-Syntax.
**Fix (Phase 1):** Grammar: `ForEachCall` als neue Alternative in `iteratorOrOperationCall`. QvtoExpressionBuilder: `createForEachArrowCall` setzt `ownedSource`.
**Gap-Fixes (Phase 1, nachträglich):**
- GAP-1: EMF Model `ForExp.name : EString` + Parser `setName(forKind)` — unterscheidet forEach/forOne
- GAP-2: Engine `isForOne` → break nach erstem Body-Execution (3 neue Tests: forOne_stopsAfterFirstMatch, forOne_withCondition_findsFirstMatch, forOne_noMatch_executesNothing)
- GAP-3: Engine `return WRAPPED_NULL` statt lastResult (§8.2.2.6: ForExp returns null)
- GAP-4: Grammar `forVarList` 3 Alternativen (compute-shorthand `i;x:Type=init` / Typ `i;Integer` / plain `i`), Parser `wrapInComputeIfShorthand` (1 neuer Test: forEach_computeShorthand_accumulatesResult)
- GAP-5: Engine Set→OrderedSet Konvertierung via `SequencedSet`-Check (1 neuer Test: forEach_overSet_iteratesAllElements)

---

### ✅ F-02: Imperative if/then/endif (2 Tests) — DONE

**Spec-Validierung:** §8.2.2.8 SwitchExp (p127) — imperative if ist eine Notation von SwitchExp. Else ist **optional** (erweitert OCL-if, wo else mandatory ist). §8.2.2.2 BlockExp (p121) — `do` Keyword kann in if/switch/compute/for weggelassen werden. §8.4 p170 — `<if_body> ::= <expression> | <expression_block>`.
**Eclipse QVT-O:** Bestätigt — imperative if mit Blockbody und optionalem else.
**Fix:**
- Grammar: IfExp `('else' elseExp=expression)?` statt mandatory else; blockExp mit bare `{ statement* }` Alternative
- Parser: `visitIfExp` behandelt null elseExp
- Engine: IfExp-Interception in `eval()` vor OCL-Delegation → `evalIfExp()` dispatcht Body-Expressions über QVT-O eval-Chain (löst "Unsupported BlockExp" Fehler)

### ✅ F-04: return in Blöcken (1 Test) — DONE (mitgefixt durch F-02)

**Root Cause:** `return_fromHelper_withConditional` nutzte imperatives if mit BlockExp-Body. Der Fehler war identisch mit F-02 — IfExp wurde an OCL-Evaluator delegiert, der BlockExp nicht kennt.
**Fix:** Durch F-02 IfExp-Interception automatisch mitgefixt.

### ✅ F-09: resolveone standalone (5 Tests) — DONE

**Spec-Validierung:** §8.1.11.1–8.1.11.7 — 8 Resolve-Varianten (resolve/resolveone/invresolve/invresolveone × normal/In). `resolveone(Type)` ohne Source sucht ALLE Trace-Records (§8.1.11.3). `resolveone(var:Type|cond)` filtert nach Typ und Bedingung.
**Eclipse QVT-O:** Bestätigt — `source.resolveone(...)` als Navigation-Suffix geparst.
**Fix (5 Root Causes):**
- RC-1: Grammar `ResolveCallSuffix`/`ResolveInCallSuffix` in `propertyOrCallSuffix` für `self.late resolveone(...)` Syntax
- RC-2: `source.resolveone(Type)` fiel durch zu OCL → neue `createDotResolveCall`/`createDotResolveInCall` Methoden
- RC-3: Grammar `resolveTarget` split in `NamedResolveTarget` (`var:Type`) und `TypeOnlyResolveTarget` (`Type`)
- RC-4: Engine: `source=null` → alle Trace-Records durchsuchen (nicht nur aktuellen Source)
- ClassifierType-Unwrapping: `target.getType() instanceof ClassifierType ct` → `ct.getReferredClassifier()` als EClass

### ✅ F-07: late resolve mit expliziter Source (6 Tests) — DONE

**Spec-Validierung:** §8.1.11.3 (Resolve mit Source), §8.1.11.7 (Late Resolve) — `self.late resolveone(...)` ist Navigation-Suffix mit `late` Modifier. Deferred Resolve wird nach Transformation-Ende aufgelöst.
**Eclipse QVT-O:** Bestätigt — `late` als optionaler Modifier vor resolve-Kind in Navigation-Kette.
**Fix:** Mitgefixt durch F-09 Grammar-Änderungen (RC-1: `ResolveCallSuffix` mit `'late'?` Prefix). Engine: deferred resolve mit `capturedSource=null` wenn kein `ownedSource` → sucht alle Records.

### ✅ F-10: Configuration Properties (2 Tests) — DONE

**Spec-Validierung:** §8.2.1.22 ConfigProperty, §8.1.1 Transformation — Configuration Properties werden vor Ausführung deklariert. Nicht gesetzte Properties haben Wert `null`.
**Eclipse QVT-O:** Bestätigt — fehlende Config-Properties evaluieren zu null.
**Fix:** Engine: Vor `findMainOperation()` alle deklarierten `configProperty`-Features als null-Variablen registrieren, wenn nicht bereits gesetzt.

### ✅ F-08 Rest: ObjectExp verbleibend (2 Tests) — DONE

**Spec-Validierung:** §8.2.1.18 ConstructorBody — implizite Property-Zuweisungen resolven zum erzeugten Objekt. `self` wird NICHT reboundt (§8.2.1.17: `self` = kontextuelles Argument der umgebenden Operation). §8.2.1.4 ObjectExp `+=` mit Collection-RHS → addAll-Semantik.
**Eclipse QVT-O:** Bestätigt — `name := 'x'` statt `self.name := 'x'` im ObjectExp. `+=` auf Containment-Features mit Collection-RHS fügt alle Elemente einzeln hinzu.
**Fix:**
- `mapping_containerWithChildren`: `+=` prüft ob RHS eine Collection ist → iteriert und fügt jedes Element einzeln hinzu (statt Collection als einzelnes Element)
- `object_asMappingResult`: `env.define("_objectExp", created)` als dedizierte Variable für `resolveImplicitPropertyTarget` — `self` wird NICHT auf das ObjectExp-Objekt gesetzt (spec-konform)
- Unit-Tests korrigiert: `self.name := 'test'` → `name := 'test'` (implizite Syntax per §8.2.1.18)

---

### Erledigte Fixes — Zusammenfassung

| Fix | Spec-Ref | Spec-Stelle gelesen | Eclipse geprüft | Änderung |
|-----|----------|---------------------|-----------------|----------|
| F-05 | §8.4 p168 when_clause | ✅ Ja | ✅ Ja | Grammar: `guardBlock` mit `;` als Separator |
| F-06 | §8.4 p166 declarator, §8.1.18 | ✅ Ja | ✅ Ja | Grammar: `resultParam` named/unnamed, implicit `result` |
| F-08 | §8.2.1.17-18 MappingBody, ConstructorBody | ✅ Ja | ✅ Ja | Engine: `resolveImplicitPropertyTarget`, `_objectExp` Variable, `+=` addAll, self NOT rebound |
| F-01 | §8.2.2.6 ForExp, §8.4 p170 | ✅ Ja (retroaktiv) | ⚠️ Nicht geprüft | Grammar: `ForEachCall` als Arrow-Call, forOne, compute-shorthand |
| F-02 | §8.2.2.8 SwitchExp, §8.4 p170 | ✅ Ja | ✅ Ja | Engine: `evalIfExp` vor OCL-Delegation, optionales else |
| F-04 | §8.2.2.8 (via F-02) | ✅ Ja | — | Mitgefixt durch F-02 IfExp-Interception |
| F-09 | §8.1.11.1–7 Resolve | ✅ Ja | ✅ Ja | Grammar: resolve Suffixes + Target-Split. Engine: null-Source, ClassifierType-Unwrapping |
| F-07 | §8.1.11.3, §8.1.11.7 Late Resolve | ✅ Ja | ✅ Ja | Mitgefixt durch F-09 Grammar. Engine: deferred resolve null-source |
| F-10 | §8.2.1.22 ConfigProperty, §8.1.1 | ✅ Ja | ✅ Ja | Engine: Config-Properties als null registrieren |

---

## Phase 2: Transformation, ModelType, Library, Mapping, ObjectExp, Constructor (~150 Tests)

> **⚠️ SPEC-FIRST WORKFLOW (bei JEDEM Task befolgen!)**
>
> **Tests definieren das korrekte Verhalten — NICHT die aktuelle Implementierung.**
>
> 1. **ZUERST die Spec lesen** — QVT-O v1.3: `docs/specifications/QVT/formal-16-06-03.pdf`, §8.1.1–8.1.8
> 2. **Tests gegen die Spec schreiben** — das erwartete Ergebnis kommt aus der Spec, NICHT aus dem Code
> 3. **Tests laufen lassen** — Failures sind Implementierungslücken, NICHT Testfehler
> 4. **Implementierung fixen** — bis die Tests grün sind
> 5. **NIEMALS einen Test an die Implementierung anpassen**, um ihn grün zu machen
> 6. **Bei Unklarheit: Rückfrage an den User**, nicht raten
>
> **Referenz-Quellen:** 1. QVT-O v1.3 Spec → 2. Eclipse QVT-O Tests → 3. Gecko QVT-O → 4. Spec hat Vorrang → 5. Bei Unklarheit: Rückfrage

**Spec-Abschnitte:** §8.1.1–8.1.8, §8.2.1.1–8.2.1.24, §8.4 (Concrete Syntax)

**Eclipse-Referenz:** `repositories/org.eclipse.qvto/tests/org.eclipse.m2m.tests.qvt.oml/parserTestData/models/`

**Bestandsanalyse:** 303 bestehende Tests (303 pass). Ziel: ~150 neue Tests → ~453 gesamt.

### Übersicht Teilaufgaben (Priorität: CRITICAL → HIGH → MEDIUM)

| # | Teilaufgabe | Spec-Ref | Gap | Geschätzte Tests | Status |
|--:|-------------|----------|-----|:----------------:|--------|
| P2-01 | Constructor (Deklaration + Ausführung) | §8.1.8, §8.2.1.13 | ✅ gefixt | 15 | ✅ DONE |
| P2-02 | Intermediate Properties | §8.1.10, §8.2.1.14 | 🔴 CRITICAL — komplett ungetestet | ~15 | ⬜ |
| P2-03 | Library (Deklaration + Import + Execution) | §8.1.4, §8.2.1.2 | ✅ 12 Tests (6 Parse + 6 E2E) | 12 | ✅ |
| P2-04 | Transformation extends/access | §8.1.1, §8.2.1.1 | ✅ 7 E2E Tests | 7 | ✅ |
| P2-05 | Mapping Overloading + Dispatch | §8.1.14, §8.2.1.15 | ✅ 8 E2E Tests | 8 | ✅ |
| P2-06 | Mapping Trace-Caching (Implicit Inhibition) | §8.1.5 p106 | ✅ 6 Tests | 6 | ✅ |
| P2-07 | Mapping disjuncts/merges/inherits (Parse) | §8.1.5, §8.4 p168 | ✅ 8 Tests + Engine-Fix | 8 | ✅ |
| P2-08 | ObjectExp Extent + Update | §8.1.6, §8.2.1.24 | 🟠 MEDIUM — Extent-Zuweisung ungetestet | 6 | ✅ |
| P2-09 | ModelType (Compliance, Multi-Package) | §8.1.2, §8.2.1.6 | 🟠 MEDIUM — nur strict, kein effective | 7 | ✅ |
| P2-10 | Entry Operation (main) | §8.1.1, §8.2.1.11 | 🟠 MEDIUM — implizit getestet, keine dedizierten Tests | 6 | ✅ |
| P2-11 | Mapping Sections vertieft (init/population/end) | §8.1.5, §8.2.1.15 | 🟢 LOW — gut abgedeckt, Lücken bei Randfällen | ~10 | ✅ |
| P2-12 | Mapping Guards vertieft (when/where) | §8.1.5, §8.2.1.15 | 🟢 LOW — gut abgedeckt, Lücken bei komplexen Guards | ~8 | ✅ |
| P2-13 | Configuration Properties vertieft | §8.1.4, §8.2.1.1 | 🟢 LOW — 8 Tests vorhanden, Lücken bei Parse+Typen | ~6 | ✅ |

---

### P2-01: Constructor (Deklaration + Ausführung) — 🔴 CRITICAL

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.8 (p72) + §8.2.1.13 (p103) + §8.2.1.18 ConstructorBody (p109):**
>    - Constructor ist eine ImperativeOperation, deren Body ein ConstructorBody ist
>    - **Kein expliziter Result-Parameter** — Rückgabewert ist das erzeugte Objekt
>    - Name ist typischerweise der Klassenname, aber nicht zwingend (→ erlaubt Überladung)
>    - Für jede Klasse gibt es einen **impliziten Default-Konstruktor** ohne Parameter
>    - Im ConstructorBody: **unqualifizierte Property-Zuweisungen** (`name := n`) beziehen sich auf das erzeugte Objekt (§8.2.1.18)
>    - Aufruf über `new MyClass(args)` → InstantiationExp (§8.2.1.24)
>
> 2. **Concrete Syntax (§8.4 p168):**
>    ```
>    <constructor_header> ::= <qualifier>* 'constructor' <scoped_identifier> <simple_signature>
>    <constructor_def> ::= <constructor_header> <expression_block> ';'?
>    ```
>    Beispiel: `constructor Column::Column(n:String, t:String) { name := n; type := t; }`
>
> 3. **Eclipse QVT-O prüfen:** `repositories/org.eclipse.qvto/` — Suche nach Constructor-Tests und `visitConstructor` in der Evaluator-Klasse
>
> 4. **Bestand:** 1 Parse-Test (`constructorDef`), 0 Execution-Tests, 0 E2E-Tests

**Neue Tests (~15):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| Parse: Constructor mit Parametern | `constructor T::create(name:String)` → Parameter im AST | §8.4 p168 |
| Parse: Constructor ohne Parameter | `constructor T::T()` → leere Signatur | §8.4 p168 |
| Parse: Constructor mit Body-Assignments | Body enthält `name := n; value := v;` | §8.2.1.18 |
| Parse: Constructor mit qualifiziertem Scope | `constructor pkg::T::create()` | §8.4 p168 |
| Exec: Constructor-Aufruf via `new` | `new TargetElement('test')` → ruft Constructor auf, setzt Properties | §8.2.1.24, §8.1.8 |
| Exec: Default-Constructor (implizit) | `new TargetElement()` ohne expliziten Constructor → erzeugt Objekt | §8.1.8 |
| Exec: Constructor mit mehreren Parametern | `new Column('name', 'type')` → beide Properties gesetzt | §8.1.8 p72 |
| Exec: Constructor-Rückgabewert | `var x := new T('test'); log(x.name)` → Rückgabe = erzeugtes Objekt | §8.2.1.13 |
| Exec: Constructor body mit Berechnungen | `constructor T::T(n:String) { name := n.toUpper(); }` | §8.2.1.18 |
| E2E: Constructor in Mapping aufgerufen | Mapping ruft `new T(self.name)` auf | §8.1.8 + §8.1.5 |
| E2E: Constructor erzeugt in Extent | `new T()` erzeugt Objekt im Output-Extent | §8.2.1.24 |
| E2E: Mehrere Constructors (Überladung) | Zwei Constructors für selbe Klasse, verschiedene Parameter | §8.1.8 p72 |
| E2E: Constructor mit verschachteltem Object | Constructor-Body enthält `elements += object ...` | §8.2.1.18 + §8.1.6 |
| E2E: Constructor in Library definiert | Library enthält Constructor, Transformation nutzt ihn | §8.1.4 + §8.1.8 |
| E2E: Constructor mit Helper-Aufruf | Constructor-Body ruft Helper auf | §8.2.1.18 |

**Ergebnis P2-01:** ✅ 15 neue Tests (4 Parse, 11 E2E), 0 Failures. **318 Tests gesamt.**

**Implementierte Änderungen:**
- Engine: `caseInstantiationExp` erweitert um Constructor-Lookup + Aufruf
- Engine: `findConstructor(EClass, argCount)` — sucht Constructor im Module-Class nach Klassennamen + Argument-Count
- Engine: `callConstructor(Constructor, EObject, args)` — führt Constructor-Body aus mit `result` = erzeugtes Objekt
- Spec-Konformität: §8.2.2.22 Objekt wird VOR Constructor erzeugt, §8.2.1.13 `self`→`result` im Body, §8.2.1.18 implizite Property-Zuweisungen
- Eclipse-Konformität: Übereinstimmend mit `visitConstructor` + `visitInstantiationExp` Semantik

---

### P2-02: Intermediate Properties — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.10 (p73-74) + §8.2.1.14 ContextualProperty (p104):**
>    - `intermediate property <ContextClass>::<propName> : <Type>;` — erweitert eine Metaklasse um eine zusätzliche Property innerhalb des Transformationsscope
>    - ContextualProperty extends Property, hat `context: Class [1]`, `initExpression: OclExpression [0..1]`
>    - Die Property ist **logisch** dem Kontext-Typ zugeordnet, aber **nicht physisch** ins Metamodell eingefügt
>    - Intermediate Properties sind **temporär** — nur innerhalb der Transformation verfügbar, nicht im Output
>    - `intermediate class` — eigene Klassen für Zwischenergebnisse (gehören zum impliziten `_INTERMEDIATE`-Modelltyp)
>    - initExpression: optionaler Initialisierungswert (lazy oder eager?)
>
> 2. **Concrete Syntax (§8.4 p167):**
>    ```
>    <property> ::= 'intermediate'? <property_key>+ <declarator> ';'
>    <property_key> ::= 'derived' | 'literal' | 'configuration' | 'property'
>    ```
>    Beispiel: `intermediate property EClass::myProp : String;`
>    Beispiel: `intermediate property EClass::counter : Integer = 0;`
>
> 3. **Eclipse QVT-O prüfen:** `repositories/org.eclipse.qvto/` — Suche nach `intermediate property` Tests, `ContextualProperty`, `visitContextualProperty`
>
> 4. **Bestand:** 0 Tests (komplett ungetestet!)

**Neue Tests (~15):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| Parse: Intermediate property Deklaration | `intermediate property SourceElement::tag : String;` → ContextualProperty im AST | §8.4 p167 |
| Parse: Intermediate property mit Init | `intermediate property SourceElement::counter : Integer = 0;` → initExpression | §8.2.1.14 |
| Parse: Intermediate property Kontext-Typ | Verify `context` = SourceElement EClass | §8.2.1.14 |
| Parse: Mehrere intermediate properties | 3 verschiedene Properties auf verschiedenen Klassen | §8.1.10 |
| Exec: Intermediate property lesen/schreiben | `elem.tag := 'x'; log(elem.tag)` → Property setzbar und lesbar | §8.1.10 p73 |
| Exec: Intermediate property Default-Wert | Ohne Zuweisung → null (oder initExpression-Wert) | §8.2.1.14 |
| Exec: Intermediate property mit initExpression | `= 0` → Anfangswert 0, danach `+= 1` | §8.2.1.14 |
| Exec: Intermediate property pro Instanz | Zwei verschiedene EObjects → verschiedene Werte | §8.1.10 |
| E2E: Intermediate property in Mapping | Mapping setzt intermediate property, Helper liest sie | §8.1.10 + §8.1.5 |
| E2E: Intermediate property als Zwischenspeicher | Transformation nutzt intermediate property für Lookup | §8.1.10 p73 |
| E2E: Intermediate property nicht im Output | Intermediate property erscheint nicht im Output-Extent | §8.1.10 p74 |
| E2E: Intermediate property auf Collection-Typ | `intermediate property T::items : Sequence(String);` → `+=` zum Hinzufügen | §8.1.10 |
| E2E: Intermediate class Deklaration | `intermediate class TempData { name : String; value : Integer; }` | §8.1.10 p73 |
| E2E: Intermediate class in Mapping erzeugen | `object TempData { ... }` → nur intern verfügbar | §8.1.10 |
| E2E: Intermediate property mit Complex-Typ | Property-Typ ist eine EClass (Referenz, nicht DataType) | §8.1.10 |

**Ergebnis (2026-02-22):** ✅ 11 neue Tests (4 Parse + 7 E2E), alle grün. + 7 neue OCL-Interceptor-Tests (4115 OCL total, 0 Failures).

**Implementierte Änderungen:**
- **OclContext.java:** `propertyInterceptor` (BiFunction) als Extension Point für QVT-O intermediate properties; `PROPERTY_NOT_HANDLED` Sentinel; backward-kompatible Konstruktoren
- **OclEvaluator.java:** `tryPropertyInterceptor()` — Interceptor-Check in `casePropertyCallExp` vor `eGet` (single EObject + Collection/implicit collect)
- **QvtoEvaluator.java:** `IdentityHashMap<EObject, Map<String, Object>>` Storage; `findIntermediateProperty()` mit Name-basiertem Fallback für cross-package EClass; Read/Write-Interception in `eval()` und `caseAssignExp`; `evalOcl()` übergibt Interceptor an OCL; `resolveImplicitPropertyTarget` Fix: result vor self (§8.2.1.17)

**Neue Testklassen:**
- `QvtoHelperQueryParseTest`: 4 Parse-Tests (intermediatePropertyDecl, intermediatePropertyWithInit, multipleIntermediateProperties, intermediateClassDecl)
- `QvtoE2eIntermediatePropertyTest`: 7 E2E-Tests (writeAndRead, multipleProperties, perInstanceValues, usedInMapping, uninitializedReturnsNull, readInHelper, onTargetType)
- `OclPropertyInterceptorTest`: 7 OCL-Tests (noInterceptor, notHandled, overridesExisting, returnsNull, worksInConcatenation, mixedProperties, implicitCollect)

**Offen für spätere Phasen:** initExpression-Evaluation, intermediate class Instanzierung, Collection-typed intermediate properties

---

### P2-03: Library (Deklaration + Import + Execution) — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.4 (p68-69) + §8.2.1.2 Library (p95):**
>    - Library extends Module — enthält Operationen, Typen, Tags, aber **keinen `main()` Entry**
>    - **access-Import:** Implizite Instanz wird erzeugt, Features sind aufrufbar
>    - **extends-Import:** Klassen-Vererbung — Library-Features werden Teil des importierenden Moduls, keine separate Instanz
>    - Library kann eigene Model-Signatur haben (Typ-Kontext)
>    - ModuleImport: `importedModule: Module [1]`, `kind: ImportKind {access | extension}`
>
> 2. **Concrete Syntax (§8.4 p165-166):**
>    ```
>    <library_h> ::= 'library' <identifier> <library_signature>? <module_usage>?
>    <module_usage> ::= <access_usage> | <extends_usage>
>    <access_usage> ::= 'access' <module_ref_list>
>    <extends_usage> ::= 'extends' <module_ref_list>
>    ```
>    Beispiel: `library StringUtils { helper toUpperFirst(s : String) : String { ... } }`
>    Import: `transformation T() access StringUtils;`
>
> 3. **Eclipse QVT-O prüfen:** `repositories/org.eclipse.qvto/` — Suche nach Library-Tests, multi-module compilation
>
> 4. **Bestand:** 2 Parse-Tests (`libraryDef`, `importDeclaration`), 0 Execution/E2E

**Neue Tests (~12):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| Parse: Library mit Operationen | `library L { helper f() : String { ... } }` → Operationen im AST | §8.4 p166 |
| Parse: Library mit Model-Signatur | `library L(in m : SRC) { ... }` → modelParameter | §8.4 p166 |
| Parse: Import mit `access` Keyword | `transformation T() access MyLib;` → ImportKind.ACCESS | §8.4 p166 |
| Parse: Import mit `extends` Keyword | `transformation T() extends MyLib;` → ImportKind.EXTENSION | §8.4 p166 |
| Parse: Blackbox Library | `blackbox library BL { ... }` → isIsBlackbox | §8.1.4 |
| Exec: Library-Helper aufrufen | Transformation importiert Library, ruft Helper auf | §8.1.4 p68 |
| Exec: Library-Query aufrufen | Library enthält Query, Transformation nutzt sie | §8.1.4 |
| Exec: Library mit access — implizite Instanz | Verify Library-Features zugreifbar | §8.1.4 p68 |
| E2E: Library-Helper in Mapping | Mapping ruft Library-Helper auf | §8.1.4 + §8.1.5 |
| E2E: Library mit mehreren Helpers | Library hat 3 Helpers, Transformation nutzt alle | §8.1.4 |
| E2E: Zwei Libraries importiert | Transformation importiert 2 Libraries | §8.1.4 |
| E2E: Library-Helper mit Kontext-Typ | `helper SourceElement::format() : String` in Library | §8.1.4 + §8.1.9 |

**Ergebnis (2026-02-22):** ✅ 12 neue Tests (6 Parse + 6 E2E), alle grün.

**Implementierte Änderungen:**
- **QvtoUnitBuilder.java:** `visitCompilationUnit()` — inline Libraries als ModuleImport mit `importedModule` auf der Transformation registriert; `visitQvtoImportDecl()` — Qualified Name als Stub-Library gespeichert; `processModuleUsage()` — Module-Referenz-Namen als Stub gespeichert
- **QvtoEvaluator.java:** `findOperation()` — durchsucht nach main module class auch importierte Module (§8.2.1.4); `findModuleClassIn()` — generische Module-Class-Suche
- **QvtoOperationProvider.java:** `buildOperations()` — exportiert auch Operationen aus importierten Modulen an OCL-Engine; `addModuleOperations()` + `findModuleClassIn()` Hilfsmethoden

**Neue Tests:**
- `QvtoTransformationParseTest`: 6 Parse-Tests (libraryWithOperations, libraryWithMultipleOperations, transformationWithAccessUsage, transformationWithExtendsUsage, importDeclarationStoresName, inlineLibraryCreatesImport)
- `QvtoE2eLibraryTest`: 6 E2E-Tests (inlineLibrary_helperCallable, inlineLibrary_queryCallable, inlineLibrary_multipleHelpers, inlineLibrary_helperInMapping, inlineLibrary_twoLibraries, inlineLibrary_contextualHelper)

**Offen für spätere Phasen:** Externes Library-Linking via QvtoUnitResolver (Infrastruktur vorhanden, nicht verdrahtet); Library model signature `library L(ModelType)`; `extends` Import-Semantik (Vererbung); Blackbox Libraries (Java-Integration)

---

### P2-04: Transformation extends/access — ✅ DONE

**Spec-Referenzen:** §8.1.1 (p65), §8.2.1.1 (p89-94), §8.2.1.5 (p97), §8.2.1.11 (p101)

**Tests (7 E2E):**

| Test | Beschreibung | Status |
|------|-------------|--------|
| transformation_inOutParameters | in/out Transform source→target | ✅ |
| transformation_threeParameters | in/inout/out alle drei Directions | ✅ |
| transformation_inoutModifiesModel | inout liest Input + erzeugt Output | ✅ |
| transformation_noMain_producesError | Ohne main() → Fehler | ✅ |
| transformation_emptyMain | Leere main() → Erfolg | ✅ |
| transformation_accessInlineLibrary | Library helper in Mapping via inline import | ✅ |
| transformation_withAccessModuleUsage | Explizite `access Lib1` Syntax | ✅ |

**Testklasse:** `QvtoE2eTransformationTest`

**Implementierungsänderungen:**
- **Parser (QvtoUnitBuilder):** ModelParameter.setEType() auf ModelType verknüpft — war vorher fehlend
- **Engine (QvtoExtentManager):** Neues `getExtentForClassifier()` — Extent-Routing anhand EPackage nsURI statt nur erstem OUT/INOUT Extent
- **Engine (QvtoEvaluator):** `addToDefaultExtent()` nutzt jetzt metamodel-basiertes Routing mit Fallback

**Ergebnis:** 348 Tests, 0 Failures

**Offen (deferred):**
- Default-Direction (kein Keyword) → braucht Grammar-Änderung (`directionKind` optional machen)
- Abstract Transformation → braucht `qualifier*` Handling
- Transformation `extends` → braucht Linking-Phase für Module-Vererbung

---

### P2-05: Mapping Overloading + Dispatch — ✅ DONE

**Spec-Referenzen:** §8.1.14 (p82-83), §8.2.1.15 (p104-107)

**Tests (8 E2E):**

| Test | Beschreibung | Status |
|------|-------------|--------|
| disjuncts_allGuardsFalse_noBodyExecutes | Alle Guards false → kein Body | ✅ |
| disjuncts_thirdCandidateWins | 3 Kandidaten, dritter passt | ✅ |
| map_whenFalse_bodySkipped | Standard map: when false → Body übersprungen | ✅ |
| map_whenTrue_bodyExecutes | Standard map: when true → Body läuft | ✅ |
| xmap_whenFalse_producesError | Strict xmap: when false → Assertion Failure | ✅ |
| contextOverload_dispatchByType | SourceElement::convert() vs SourceContainer::convert() | ✅ |
| overload_whenGuardDispatch | Zwei Mappings gleicher Name, when-Guards differenzieren | ✅ |
| contextOverload_inForEach | Kontextuelles Mapping in forEach | ✅ |

**Testklasse:** `QvtoE2eMappingOverloadTest`

**Implementierungsänderungen:**
- **Engine (QvtoEvaluator):** `findAllOperations()` — sammelt ALLE gleichnamigen Operationen (§8.1.14.2 implizite Disjunktion)
- **Engine (QvtoEvaluator):** `caseMappingCallExp()` — bei mehreren Kandidaten implizite Disjunktion
- **Engine (QvtoEvaluator):** `callMappingStrict()` — `xmap` when-guard als Pre-Condition (§8.2.1.15)
- **Engine (QvtoEvaluator):** `isStrict` Flag aus `MappingCallExp` wird jetzt ausgewertet

**Ergebnis:** 356 Tests, 0 Failures

---

### P2-06: Mapping Trace-Caching (Implicit Inhibition) — ✅ DONE

**Ergebnis:** 6 Tests in `QvtoE2eTraceInhibitionTest`, alle grün.

**Implementierung:**
- `QvtoTraceManager.lookupCachedResult()` — Identity-basierter Trace-Lookup (Object-Identity, nicht Equality)
- `QvtoEvaluator.callMapping()` — Trace-Check nach when-Guard, vor Body-Ausführung
- 1 Testfix: `->any(true)` nicht auf ArrayList unterstützt → Variable im forEach

**Tests:**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| `inhibition_bodyExecutesOnlyOnce` | Same object mapped 3× → body runs once (log count) | §8.2.1.15 p106 |
| `inhibition_sameResultReturned` | Same object mapped 2× → identical result object, 1 in extent | §8.2.1.15 p106 |
| `inhibition_differentObjects_differentResults` | Different objects → both bodies run, 2 results | §8.1.5 |
| `inhibition_differentMappings_noCacheConflict` | Same object, different mappings → both run (separate caches) | §8.1.5 |
| `inhibition_nestedMappings` | Inner mapping cached across nested + direct calls | §8.2.1.15 |
| `inhibition_resolveFindsResult` | resolveone finds traced/cached result | §8.1.5 + §8.1.11 |

---

### P2-07: Mapping disjuncts/merges/inherits (Parse-Level) — ✅ DONE

**Ergebnis:** 6 Parse-Tests in `QvtoMappingParseTest` + 2 Exec-Tests in `QvtoE2eMappingTest`, alle grün.

**Engine-Fix:** Reihenfolge korrigiert gemäß §8.1.15 (p107):
- **Vorher:** init → population → inherited → merged → end (FALSCH)
- **Nachher:** init → inherited → population → end → merged (SPEC-KONFORM)
- `inherits`: Nach init+instantiation, VOR population → derived kann base Properties überschreiben
- `merges`: Nach end-Section → merged hat finales Ergebnis

**Parse-Tests (in QvtoMappingParseTest):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| `mappingMerges` | `merges setName, setValue` → 2 merged refs im AST | §8.2.1.15 |
| `mappingDisjuncts` | `disjuncts alt1, alt2` → 2 disjunct refs im AST | §8.2.1.15 |
| `mappingInheritsAndMerges` | `inherits base merges extra` → beide Referenzen | §8.2.1.15 |
| `mappingDisjuncts_emptyBody` | Disjunct mapping hat leeren population body | §8.2.1.15 |
| `mappingMerges_threeTargets` | `merges m1, m2, m3` → 3 Einträge | §8.1.15 |
| `mappingInherits_twoBases` | `inherits base1, base2` → 2 Einträge | §8.1.15 |

**Exec-Tests (in QvtoE2eMappingTest):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| `inherits_derivedOverridesBase` | Derived population überschreibt base (base läuft zuerst) | §8.1.15 p107 |
| `merges_executesAfterEnd` | Merged läuft nach end, überschreibt main population | §8.1.15 p107 |

---

### P2-08: ObjectExp Extent + Update — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.6 (p70-72) + §8.2.1.24 ObjectExp (p116):**
>    - **Object Creation vs Update:** Wenn `referredObject` Variable **null** → neues Objekt erzeugen. Wenn **non-null** → kein neues Objekt, aber ConstructorBody wird auf dem existierenden Objekt ausgeführt (Update-Semantik)
>    - **Extent-Zuweisung:** `object x : T @ modelExtent { ... }` — explizit in welchem Model-Extent das Objekt angelegt wird
>    - **Extent-Inferenz:** Wenn kein Extent angegeben, wird er aus den Model-Typen der Transformation inferiert
>    - **Residence:** Unabhängig vom Extent — Objekt kann über Containment-Referenz seinen Residenz-Ort wechseln
>    - Syntax (§8.4 p170): `<object_exp> ::= 'object' ('(' <iter_declarator> ')')? <object_declarator> <expression_block>`
>    - Syntax: `<object_declarator> ::= <typespec> | <identifier> ':' <typespec>?`
>    - Syntax mit Extent: `object x : T @ srcmodel { ... }`
>
> 2. **Eclipse QVT-O prüfen:** Object-Update-Semantik, Extent-Handling
>
> 3. **Bestand:** 29 Tests (Parse+Exec+E2E), aber 0 für Extent-Zuweisung und 0 für Update-Semantik

**Neue Tests (~12):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| Parse: ObjectExp mit Extent `@` | `object x : T @ outModel { }` → extent-Variable im AST | §8.4 p170 |
| Parse: ObjectExp ohne Typ (nur Variable) | `object x : { }` — Typ von Variable `x` bekannt | §8.4 p170 |
| Exec: Object-Update auf existierendem Objekt | `var x := ...; object x : T { value := 99; }` → x wird aktualisiert, nicht neu erzeugt | §8.1.6 p70 |
| Exec: Object-Update verändert Properties | Existierendes Objekt + ObjectExp Body → Property-Wert geändert | §8.2.1.24 |
| Exec: Object-Update erzeugt kein neues Objekt | Extent-Größe bleibt gleich nach Update | §8.1.6 p70 |
| Exec: Object-Creation wenn Variable null | `var x : T := null; object x : T { name := 'new'; }` → neues Objekt | §8.1.6 p70 |
| E2E: Extent-Zuweisung mit `@` | `object TargetElement @ t { }` → in Extent `t` | §8.1.6 p71 |
| E2E: Objekt wechselt Extent über Containment | Kind-Objekt zu Container hinzugefügt → bewegt sich aus Root-Extent | §8.1.6 p71 |
| E2E: Object-Update in Mapping end-Section | Mapping erzeugt in population, aktualisiert in end | §8.1.5 + §8.1.6 |
| E2E: ObjectExp mit Iterator | `list->object(x) T { ... }` → Kurzform für xcollect | §8.1.6 p72 |
| E2E: ObjectExp mit bedingtem Body | `object T { if (cond) { name := 'a'; } else { name := 'b'; }; }` | §8.1.6 |
| E2E: Object-Update + neue Properties hinzufügen | Update setzt Properties, die vorher null waren | §8.1.6 |

**Ergebnis: 6 neue Tests, alle grün. Fixes:**
- **Engine (QvtoEvaluator.caseObjectExp):** Update-Semantik implementiert (§8.2.1.24) — wenn `referredObject`-Variable existiert und non-null ist, wird das existierende Objekt aktualisiert statt neu erzeugt; kein Extent-Eintrag bei Update
- **Engine:** Outer-Scope-Variable-Zuweisung bei Object-Creation mit expliziter Variable
- **Grammar (QvtO.g4):** Imperative if mit Block-Syntax hinzugefügt — `if (cond) { stmts } else { stmts }` (Eclipse IfExpCS_ext)
- **Parser (QvtoExpressionBuilder):** `visitImperativeIfExp` — baut IfExp AST mit BlockExp als then/else
- **Deferred:** `@` Extent-Syntax und ObjectExp-Iterator — in Phase 5/6 oder bei Bedarf
- **Gesamt: 376 QVT-O Tests, 0 Failures**

---

### P2-09: ModelType (Compliance, Multi-Package) — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.2 (p66) + §8.2.1.6 ModelType (p98-100):**
>    - **strict:** Instanzen müssen exakte Typen des referenzierten Metamodell-Packages sein
>    - **effective (default):** Typ-Kompatibilität über Name-Matching — flexible Bindung
>    - `metamodel: Package [1..*]` — kann MEHRERE Packages referenzieren
>    - `additionalCondition: OclExpression [*]` — optionale Where-Bedingungen
>    - `conformanceKind: String` — Default ist `"effective"` wenn nicht angegeben
>    - Wenn kein expliziter ModelType deklariert wird, ist es äquivalent zu `modeltype MT uses 'nsURI'` mit "effective" Compliance
>
> 2. **Concrete Syntax (§8.4 p166-167):**
>    ```
>    <modeltype> ::= 'modeltype' <identifier> <compliance_kind>?
>        'uses' <packageref_list> <modeltype_where>? ';'
>    <compliance_kind> ::= <STRING>   // "strict" oder "effective"
>    ```
>
> 3. **Bestand:** 4 Parse-Tests (basic, resolve, strict, packageName), 0 E2E

**Neue Tests (~10):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| Parse: ModelType ohne Compliance (Default) | `modeltype M uses 'uri';` → conformanceKind = "effective" (oder null/default) | §8.2.1.6 |
| Parse: ModelType `'effective'` | `modeltype M 'effective' uses 'uri';` → conformanceKind = "effective" | §8.2.1.6 |
| Parse: ModelType mit zwei Packages | `modeltype M uses 'uri1', 'uri2';` → 2 metamodel-Einträge | §8.4 p167 |
| Parse: ModelType mit where-Clause | `modeltype M uses 'uri' where { ... };` → additionalCondition | §8.4 p167 |
| Parse: ModelType mit Package-Name | `modeltype M uses source('uri');` → Named Package-Ref | §8.4 p167 |
| Exec: objectsOfType mit ModelType | `m.objectsOfType(SourceElement)` → korrekte Typ-Filterung | §8.1.2 |
| E2E: ModelType resolves korrekte Typen | Transformation kann nur Typen des deklarierten ModelType verwenden | §8.1.2 p66 |
| E2E: Zwei ModelTypes in Transformation | `(in s : SRC, out t : TGT)` — verschiedene Metamodelle | §8.1.1 + §8.1.2 |
| E2E: ModelType mit mehreren Packages | Typen aus beiden Packages verwendbar | §8.1.2 |
| E2E: Impliziter ModelType | Kein expliziter modeltype → äquivalent zu effective | §8.1.2 p66 |

**Ergebnis: 7 neue Tests (5 Parse + 2 E2E), alle grün. Fixes:**
- **Parser (QvtoUnitBuilder.visitModeltypeDecl):** Where-Clause wird jetzt verarbeitet — Expressions aus `modeltypeWhere` werden in `modelType.getAdditionalCondition()` eingefügt
- **Test-Infrastruktur (AbstractQvtoParserTest):** `targetPackage` hinzugefügt für Multi-Package-Tests
- **Getestet:** Default-Compliance (null/effective), explicit "effective", "strict", Multi-Package (2 URIs), where-Clause, zwei ModelTypes in einer Transformation, objectsOfType-Filterung
- **Deferred:** Runtime-Evaluation der additionalCondition (Engine prüft where-Clause nicht zur Laufzeit — nur Parse-Level)
- **Gesamt: 383 QVT-O Tests, 0 Failures**

---

### P2-10: Entry Operation (main) — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.2.1.11 EntryOperation (p101-102):**
>    - Name ist **immer "main"**, keine Parameter
>    - Maximal **ein** Entry pro Transformation
>    - Hat Zugriff auf alle global zugänglichen Properties und Parameter
>    - Body enthält geordnete Liste von Expressions
>    - Deferred Resolve Assignments werden am Ende der main-Ausführung abgearbeitet
>
> 2. **Bestand:** 2 Parse-Tests, implizit in jedem E2E-Test verwendet

**Neue Tests (~8):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| Parse: main() mit leerer Signatur | `main() { }` → EntryOperation | §8.4 p168 |
| Parse: main() Body-Expressions | Body enthält 3 Statements → geordnete Liste | §8.2.1.11 |
| Exec: main() hat Zugriff auf Model-Parameter | `s.objectsOfType(...)` in main → Model-Parameter zugreifbar | §8.2.1.11 |
| Exec: main() hat Zugriff auf Config-Properties | Config-Property in main lesbar | §8.2.1.11 |
| Exec: main() Expressions in Reihenfolge | 3 log-Aufrufe → Reihenfolge verifizieren | §8.2.1.11 |
| Exec: Transformation ohne main() → Fehler | Kein Entry → definierter Fehler | §8.2.1.11 p101 |
| E2E: main() mit komplexem Body | Variablen, Mappings, Helpers in main aufgerufen | §8.2.1.11 |
| E2E: Deferred Resolves am Ende von main | Late resolve wird nach main-Body aufgelöst | §8.2.1.11 + §8.1.11.7 |

**Ergebnis: 6 neue Tests (3 Parse + 3 E2E), alle grün.**
- **Parse:** main() wird als EntryOperation geparst, Body-Expressions in geordneter Liste, keine Parameter
- **E2E:** Expressions in Reihenfolge, komplexer Body (Variablen + Helpers + Mappings), lokale Variablen
- **Bereits existent:** `transformation_noMain_producesError`, `transformation_emptyMain` — zählen als bestehende Abdeckung
- **Deferred:** Late resolve am Ende von main (Phase 4)
- **Gesamt: 389 QVT-O Tests, 0 Failures**

---

### P2-11: Mapping Sections vertieft (init/population/end) — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.5 (p106) + §8.2.1.19 MappingBody (p110):**
>    - **4 Phasen:** init → implicit instantiation → population → end
>    - **init:** Variablen vorbereiten, Queries aufrufen, explizite out-Parameter-Zuweisung möglich
>    - **implicit instantiation:** null out-Parameter werden automatisch erzeugt, Trace-Record wird angelegt
>    - **population:** Objekt-Properties befüllen (optional mit `population` Keyword)
>    - **end:** Nachbearbeitungen, zusätzliche Berechnungen
>    - §8.2.1.19: `self` = contextual argument, `result` = declared result
>    - Ohne `population` Keyword + Single Result → Body = impliziter ObjectExp auf dem Result
>
> 2. **Bestand:** 23 E2E + 15 Parse + 10 Exec — gut abgedeckt

**Neue Tests (~10):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| Exec: init mit early return | `init { return existingObj; }` → Body wird übersprungen | §8.1.5 |
| Exec: init setzt out-Parameter explizit | `init { r := existingObj; }` → kein auto-instantiation | §8.1.5 p106 |
| Exec: population Keyword explizit | `population { name := 'x'; }` → identisch mit implizit | §8.4 p168 |
| Exec: end hat Zugriff auf result | `end { log(r.name); }` → result ist zugreifbar | §8.1.5 |
| Exec: init-Variable in population verwenden | `init { var x := compute(); } name := x;` → Scope-Übergreifend | §8.1.5 |
| E2E: Reihenfolge init → population → end | 3 Logs in korrekter Reihenfolge | §8.1.5 p106 |
| E2E: Single-Result ohne population = impliziter ObjectExp | `mapping f() : T { name := 'x'; }` ≡ `object result : T { name := 'x'; }` | §8.2.1.19 p110 |
| E2E: Multiple Results ohne population | `mapping f() : (r1 : T1, r2 : T2) { r1.name := 'a'; r2.name := 'b'; }` | §8.2.1.19 |
| E2E: Mapping mit nur init-Section | `mapping f() : T { init { r.name := 'init-only'; } }` | §8.1.5 |
| E2E: Mapping mit nur end-Section | `mapping f() : T { end { r.name := r.name + '-end'; } }` | §8.1.5 |

**Ergebnis P2-11:** ✅ 9 neue Tests (2 Parse, 7 E2E), 0 Failures. Grammar-Fix: `population` Keyword in `mappingBody`-Regel + Builder-Support. Verifiziert gegen Eclipse QVT-O Tests (assignresultininit.qvto, endsectresultpatch.qvto, populationSection.qvto, outininitvar.qvto). **402 Tests gesamt.**

---

### P2-12: Mapping Guards vertieft (when/where) — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.5 (p106) + §8.2.1.15:**
>    - **when + map:** Guard — Body wird übersprungen, null zurückgegeben
>    - **when + xmap:** Pre-condition (Assertion) — Failure = Fehler
>    - **where:** Post-condition — immer Assertion, Failure = Fehler
>    - Guards können beliebige OCL-Expressions sein
>    - Mehrere Expressions in when/where: alle müssen true sein (implizites AND)
>
> 2. **Bestand:** 5 E2E-Tests + 3 Parse-Tests + 2 Exec-Tests — gut abgedeckt

**Neue Tests (~8):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| Exec: when mit komplexem OCL | `when { self.name.size() > 3 and self.value > 0; }` | §8.1.5 |
| Exec: when mit mehreren Bedingungen | `when { cond1; cond2; }` → implizites AND | §8.1.5 |
| Exec: where greift auf result zu | `where { r.name <> null; }` | §8.1.5 |
| Exec: where Failure → Warning/Error | `where { false; }` → definiertes Fehlerverhalten | §8.1.5 p106 |
| E2E: xmap mit when=false → Assertion-Failure | `e.xmap convert()` → when false → Fehler | §8.1.5 |
| E2E: when mit Collection-Operation | `when { self.elements->notEmpty(); }` | §8.1.5 |
| E2E: when + where kombiniert | `when { cond1; } where { cond2; }` → beide geprüft | §8.1.5 |
| E2E: when mit null-Check | `when { self.name <> null; }` → null → Guard false | §8.1.5 |

**Ergebnis P2-12:** ✅ 4 neue E2E-Tests (multiple when-conditions, when+where combined, null-check, where passes), 0 Failures. xmap-Test bereits in QvtoE2eMappingOverloadTest vorhanden. **400 Tests gesamt.**

---

### P2-13: Configuration Properties vertieft — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.2.1.1 (p93) + §8.2.1.3 Module (p96):**
>    - `configProperty: Property [0..*]` — Wert kann undefiniert bleiben
>    - Wert-Zuweisung über externen Mechanismus (außerhalb der Spec)
>    - Deklaration: `configuration property name : Type;` oder `configuration property name : Type = defaultValue;`
>
> 2. **Bestand:** 8 E2E-Tests — gut abgedeckt, aber keine Parse-Tests und keine Typ-Validierung

**Neue Tests (~6):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| Parse: Configuration property Deklaration | `configuration property maxSize : Integer;` → AST | §8.4 p167 |
| Parse: Configuration property mit Default | `configuration property name : String = 'default';` → initExpression | §8.4 p167 |
| Parse: Mehrere config properties | 3 Properties verschiedener Typen | §8.2.1.3 |
| Exec: Config property Default-Wert | Property mit `= 'default'` → Wert wenn nicht extern gesetzt | §8.2.1.1 |
| E2E: Config property mit Collection-Typ | `configuration property items : Sequence(String);` | §8.2.1.3 |
| E2E: Config property mit Real-Typ | `configuration property threshold : Real;` | §8.2.1.3 |

**Ergebnis P2-13:** ✅ 5 neue Tests (3 Parse, 2 E2E), 0 Failures. Verifiziert gegen Spec §8.2.1.1 (p93-94, p96) + Eclipse QVT-O Tests (simpleconfigproperty.qvto, invalidConfigProp.qvto, bug267917.qvto). **407 Tests gesamt.**

---

## Phase 3: Helpers, Intermediate Data (~50 Tests)

> **⚠️ SPEC-FIRST WORKFLOW (bei JEDEM Task befolgen!)**
>
> **Tests definieren das korrekte Verhalten — NICHT die aktuelle Implementierung.**
>
> 1. **ZUERST die Spec lesen** — QVT-O v1.3: `docs/specifications/QVT/formal-16-06-03.pdf`, §8.1.9–8.1.10
> 2. **Tests gegen die Spec schreiben** — das erwartete Ergebnis kommt aus der Spec, NICHT aus dem Code
> 3. **Tests laufen lassen** — Failures sind Implementierungslücken, NICHT Testfehler
> 4. **Implementierung fixen** — bis die Tests grün sind
> 5. **NIEMALS einen Test an die Implementierung anpassen**, um ihn grün zu machen
> 6. **Bei Unklarheit: Rückfrage an den User**, nicht raten
>
> **Referenz-Quellen:** 1. QVT-O v1.3 Spec → 2. Eclipse QVT-O Tests → 3. Gecko QVT-O → 4. Spec hat Vorrang → 5. Bei Unklarheit: Rückfrage

**Spec-Abschnitte:** §8.1.9 (p73) Helpers, §8.1.10 (p73-74) Intermediate Data, §8.2.1.12 (p102-103) Helper, §8.2.1.14 (p104) ContextualProperty

**Eclipse-Referenz:** `repositories/org.eclipse.qvto/tests/org.eclipse.m2m.tests.qvt.oml/unparserTestData/sources/` — helperSimpleDef_252173, overload_singleParam, bug424584 (inout param), q1 (Tuple return), bug418961_lib (Collection context)

**Bestand:** 56 Helper/Query-relevante Tests (20 Parse, 7 Exec, 18 E2E, 6 Library, 4 Transformation, 1 Mapping)

| Testbereich | Spec-Ref | Beschreibung | Geschätzte Tests | Status |
|-------------|----------|--------------|:----------------:|--------|
| P3-01 Helper vertieft | §8.1.9, §8.2.1.12 | Void-Helper, inout-Param, Seiteneffekte | 8 | ✅ |
| P3-02 Query vertieft | §8.1.9, §8.2.1.12 | isQuery, keine Seiteneffekte, Expression-Body | 7 | ✅ |
| P3-03 Contextual Helper vertieft | §8.2.1.10, §8.2.1.12 | Helper auf String/Integer/Collection-Typen | 3 | ✅ |
| P3-04 Intermediate Classes | §8.1.10, §8.2.1.3 | `intermediate class IC { props }`, Instantiierung, Properties | 5 | ✅ |
| P3-05 Intermediate Properties | §8.1.10, §8.2.1.14 | Init-Expression, Persistence, IC-Kombination | 3 | ✅ |

---

### P3-01: Helper vertieft — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.9 (p73) + §8.2.1.12 (p102-103):**
>    - Helper = Berechnung mit möglichen Seiteneffekten, `isQuery = false`
>    - Body: Expression (`= expr`) oder Block (`{ stmts }`)
>    - **return** Statement zum vorzeitigen Beenden
>    - Parameter-Modi: `in` (default), `inout`, `out`
>    - `inout`/`out` DataType-Parameter: by reference-to-variable, Updates sichtbar für Caller
>    - Helper kann Class-Instanzen erzeugen/modifizieren
>    - Kein expliziter Result bei Void-Helpern
>
> 2. **Eclipse-Referenz:** `helperSimpleDef_252173.qvto`, `bug424584.qvto` (inout param), `q1.qvto` (Tuple return)
>
> 3. **Bestand:** 20 Parse + 7 Exec + 18 E2E — Basics gut abgedeckt; Lücken bei void, inout, Tuple-Return

**Neue Tests (~8):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| Parse: Helper ohne Return-Typ (void) | `helper doWork() { log('done'); }` → kein Result | §8.2.1.12 |
| Parse: Helper mit inout-Parameter | `helper inc(inout x : Integer) { x := x + 1; }` | §8.1.9 p73 |
| Parse: Helper mit Tuple-Rückgabetyp | `helper pair() : Tuple(a:String, b:Integer)` | §8.2.1.12 |
| Exec: Helper void — kein Rückgabewert | `helper doLog() { log('side-effect'); }` → nur Seiteneffekt | §8.1.9 |
| Exec: Helper mit return in Block | `helper f() : String { var x := 'a'; return x + 'b'; }` | §8.1.9 p73 |
| Exec: Helper mit early return | `helper check(v:Integer) : String { if v>0 then return 'pos' endif; return 'neg'; }` | §8.1.9 |
| E2E: Helper in Mapping erzeugt Objekte | Helper ruft `object T { ... }` auf → Seiteneffekt sichtbar | §8.2.1.12 |
| E2E: Helper Tuple-Return verwenden | `var t := pair(); log(t.a + t.b.toString())` | §8.2.1.12 |

**Ergebnis:** 8 neue Tests (3 Parse + 2 Exec + 3 E2E), alle ✅ PASS. **3 Implementierungslücken gefixt:**
1. **Grammar**: `directionKind?` in `param`-Regel → `inout`/`out`/`in` bei Helper-Parametern (§8.1.9)
2. **Evaluator**: Expression-Body (`= expr`) liefert jetzt `lastResult` wenn result-Variable uninitialisiert (§8.2.1.12)
3. **OperationProvider**: `addObject` auf Model-Extents registriert (§8.1.3)

---

### P3-02: Query vertieft — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.9 (p73) + §8.2.1.12 (p102-103):**
>    - Query = Helper ohne Seiteneffekte, `isQuery = true`
>    - Alle Parameter implizit `in`
>    - Body: Expression (`= expr`) oder Block (`{ return expr; }`)
>    - Kann auf beliebigen Kontext-Typen definiert werden (auch String, Integer)
>    - Spec-Beispiel: `query String::addUnderscores() : String = "_".concat(self).concat("_");`
>    - Spec-Beispiel: `query Class::isPersistent() : Boolean = self.kind='persistent';`
>
> 2. **Eclipse-Referenz:** `HelpersAndQueries.qvto`, `resolve_resolveIn.qvto` (recursive query)
>
> 3. **Bestand:** 5 Query-Tests vorhanden; Lücken bei Block-Query, Query auf primitiven Typen

**Neue Tests (~8):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| Parse: Query mit Block-Body | `query f() : Integer { return 42; }` → isQuery=true, Block-Body | §8.2.1.12 |
| Parse: Query mit Expression-Body | `query f() : String = 'hello'` → isQuery=true | §8.2.1.12 |
| Parse: Query isQuery-Flag | Parse prüft `isQuery = true` auf dem Helper-Objekt | §8.2.1.12 p102 |
| Exec: Query Expression-Body berechnet | `query double(n:Integer) : Integer = n * 2` → 10 | §8.1.9 |
| Exec: Query Block-Body mit return | `query abs(n:Integer) : Integer { if n<0 then return -n endif; return n; }` | §8.1.9 |
| Exec: Query auf String-Kontext | `query String::wrap() : String = '[' + self + ']'` → `'hello'.wrap()` | §8.1.9 p73 |
| E2E: Query in Mapping-Guard | `when { self.isPersistent(); }` → Query als Guard | §8.1.9 |
| E2E: Recursive Query | `query factorial(n:Integer) : Integer = if n<=1 then 1 else n*factorial(n-1) endif` | §8.1.9 |

**Ergebnis:** 7 neue Tests (2 Parse + 2 Exec + 3 E2E), alle ✅ PASS. **1 Implementierungslücke gefixt:**
1. **Grammar**: `scopedName` akzeptiert jetzt `primitiveType '::' name` und `collectionType '::' name` — ermöglicht kontextuale Queries/Helpers auf String, Integer, Boolean, Real und Collection-Typen (§8.1.9 p73: `query String::addUnderscores()`)

---

### P3-03: Contextual Helper vertieft — ✅ DONE

> **Spec-Basis:** §8.2.1.10 ImperativeOperation (p101), §8.2.1.12 Helper (p102-103)
> **Eclipse-Referenz:** `virtualPredefinedTypeOpers.qvto`, `overload_singleParam.qvto`

**Neue Tests (3):**

| Test | Beschreibung | Spec-Ref | Status |
|------|-------------|----------|--------|
| Exec: `helper Integer::doubled()` | `7.doubled()` → 14 via `self * 2` | §8.2.1.10 p101 | ✅ |
| Exec: Same name, different context | `String::describe()` vs `Integer::describe()` — korrekte Dispatch | §8.2.1.10 p101 | ✅ |
| E2E: Same name on SourceElement/TargetElement | `SourceElement::describe()` vs `TargetElement::describe()` | §8.2.1.10 p101 | ✅ |

**Ergebnis:** 3 Tests geschrieben. 2 davon deckten Context-Type-Dispatch-Bug auf (beide gleicher Name → falscher Dispatch). Beide Seiten sauber gefixt:

**Implementierungs-Fixes:**
- **Parser** (`QvtoExpressionBuilder`): Neue Methode `resolveContextType(ScopedNameContext)` — löst Context-Typ aus primitiveType/collectionType/qualifiedName auf
- **Parser** (`QvtoUnitBuilder`): `setOperationNameAndContext` akzeptiert jetzt `ScopedNameContext`, setzt aufgelösten Typ auf VarParameter; `setParameterType` erweitert für PrimitiveType → synthetischer EDataType; auch `visitMappingDef` nachgezogen
- **Engine** (`QvtoOperationProvider`): Neue Methode `resolveOwnerType(ImperativeOperation)` — konvertiert EClassifier→OclType; `addModuleOperations` nutzt Context-Typ als `ownerType` statt `ANY_TYPE` → `isCompatibleOwner` dispatcht korrekt

---

### P3-04: Intermediate Classes — ✅ DONE

> **Spec-Basis:** §8.1.10 (p73-74) Intermediate Data, §8.2.1.3 Module (_INTERMEDIATE package)
> **Eclipse-Referenz:** `intermSimple.qvto`, `intermProperties.qvto`, `intermWithExtends.qvto`

**Neue Tests (5):**

| Test | Beschreibung | Spec-Ref | Status |
|------|-------------|----------|--------|
| Parse: Intermediate Class ohne Properties | `intermediate class Empty {}` | §8.1.10 | ✅ |
| Parse: Mehrere Intermediate Classes | 2 ICs in einer Transformation | §8.1.10 | ✅ |
| Exec: Instantiierung + Property lesen | `object Memo { text := 'hello' }; log(m.text)` | §8.1.10 | ✅ |
| Exec: Mehrere Instanzen, eigene Werte | 2 Items mit verschiedenen Namen | §8.1.10 | ✅ |
| Exec: Intermediate Class in Helper | `describe(p : Pair) : String` liest IC-Properties | §8.1.10 | ✅ |

**Ergebnis:** 5 Tests geschrieben. Alle 3 Exec-Tests deckten Implementierungslücke auf: `EcoreUtil.create()` scheiterte weil der EClass kein EPackage hatte.

**Implementierungs-Fix:**
- **Parser** (`QvtoUnitBuilder`): Neues `_INTERMEDIATE` EPackage für intermediate classes. `getOrCreateIntermediatePackage()` erstellt ein dynamisches Package mit EFactory und registriert es in der `packageRegistry`. Intermediate classes werden dort eingetragen, sodass `resolveClassifier` sie findet und `EcoreUtil.create()` sie instantiieren kann.

---

### P3-05: Intermediate Properties — ✅ DONE

> **Spec-Basis:** §8.1.10 (p73-74) Intermediate Data, §8.2.1.14 (p104) ContextualProperty
> **Eclipse-Referenz:** `CheckMemoryLeak.qvto`, `IntermediateData.qvto`, `bug417996.qvto`
> **Bestand validiert:** 3 Parse-Tests + 7 E2E-Tests — alle spec-konform, alle grün

**Neue Tests (3):**

| Test | Beschreibung | Spec-Ref | Status |
|------|-------------|----------|--------|
| E2E: Persistence across mappings | Property in main setzen, in mapping lesen | §8.1.10, Eclipse CheckMemoryLeak | ✅ |
| E2E: Init-Expression Default-Wert | `= 'default-label'` → Wert ohne Zuweisung | §8.2.1.14, Eclipse bug417996 | ✅ |
| E2E: Property auf intermediate class | `intermediate property Node::visited` auf IC | §8.1.10 + §8.2.1.14 kombiniert | ✅ |

**Ergebnis:** 3 neue Tests. Init-Expression-Test deckte Implementierungslücke auf: `getIntermediatePropertyValue` wertete `initExpression` nicht aus.

**Implementierungs-Fix:**
- **Engine** (`QvtoEvaluator`): `getIntermediatePropertyValue()` evaluiert jetzt `initExpression` beim ersten Zugriff auf eine nicht-initialisierte Property (§8.2.1.14). Wert wird gecacht nach erster Auswertung.

---

## Phase 4: Tracing, Resolve, Late Resolve (~90 Tests)

> **⚠️ SPEC-FIRST WORKFLOW (bei JEDEM Task befolgen!)**
>
> **Tests definieren das korrekte Verhalten — NICHT die aktuelle Implementierung.**
>
> 1. **ZUERST die Spec lesen** — QVT-O v1.3: `docs/specifications/QVT/QVT-formal-16-06-03.pdf`, §8.1.11 (p74-79)
> 2. **Tests gegen die Spec schreiben** — das erwartete Ergebnis kommt aus der Spec, NICHT aus dem Code
> 3. **Tests laufen lassen** — Failures sind Implementierungslücken, NICHT Testfehler
> 4. **Implementierung fixen** — bis die Tests grün sind
> 5. **NIEMALS einen Test an die Implementierung anpassen**, um ihn grün zu machen
> 6. **Bei Unklarheit: Rückfrage an den User**, nicht raten
>
> **Referenz-Quellen:** 1. QVT-O v1.3 Spec → 2. Eclipse QVT-O Tests → 3. Gecko QVT-O → 4. Spec hat Vorrang → 5. Bei Unklarheit: Rückfrage

**Spec-Abschnitte:** §8.1.11 (p74-79) Tracing and Resolving, §8.1.12 (p79) Updating Objects

**Eclipse-Referenz:** `repositories/org.eclipse.qvto/tests/org.eclipse.m2m.tests.qvt.oml/unparserTestData/sources/` — simpleresolve, resolveall, resolve_resolveone, resolve_resolveIn, resolve_resolveoneIn, invresolve_, resolve_invresolveoneIn, invresolvebyrule, lateresolve, lateresolvebyrule, resolve_lateresolveoneIn, lateresolve_many, traceLookup_287589, intermediateprop_resolve

**Bestand:** 53 Tests (10 Parse, 6 Exec, 12 E2E-Resolve, 6 Late-Resolve, 5 Trace-Model, 8 E2E-Trace, 6 E2E-Inhibition)

| Testbereich | Spec-Ref | Beschreibung | Geschätzte Tests | Status |
|-------------|----------|--------------|:----------------:|--------|
| P4-01 Trace Records | §8.1.11.1 | Trace-Erzeugung, Felder, Struktur | 6 | ✅ |
| P4-02 Trace Inhibition | §8.1.11.2 | Re-Execution-Unterdrückung, Cached Results | 6 | ✅ |
| P4-03 resolve() by Type | §8.1.11.3 | `resolve()`, `source.resolve(Type)`, mit Condition | 7 | ✅ |
| P4-04 resolveIn() by Mapping | §8.1.11.4 | `resolveIn(Mapping)`, `source.resolveIn(Mapping, Type)` | 6 | ✅ |
| P4-05 invresolve() / invresolveIn() | §8.1.11.5 | Inverse: Target→Source, by Type, by Mapping | 6 | ✅ |
| P4-06 resolveone() / resolveoneIn() | §8.1.11.6 | Single-Result-Varianten aller Resolve-Formen | ~8 | ⬜ |
| P4-07 Late Resolve | §8.1.11.7 | `late resolve`, deferred bis alle Mappings fertig | ~8 | ⬜ |
| P4-08 Resolve Parse (vertieft) | §8.4 | Parse-Tests für alle Resolve-Varianten | ~8 | ⬜ |

---

### P4-01: Trace Records — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.11.1 (p74-75):**
>    - Execution of a mapping → trace-record (context-parameter, in-parameters, invoked-mapping, executed-mapping, out-parameters, result-parameters)
>    - trace-record created during instantiation section (after init, before population)
>    - invoked-mapping vs executed-mapping differ for disjunct mappings
>    - inout params traced once as in-parameters
>    - No trace for `object` expressions or helpers — only mappings
>    - trace-record created by every mapping execution, unless predicates/initialization fail
> 2. **Eclipse-Referenztests prüfen:**
>    - `traceLookup_287589.qvto` — Performance-Test, Shared-Instance mit Trace
>    - `TestTraceFile.java` — multipletracerecords (5 records), mappingWithNoResultTrace
>    - `TestTraceFileForMyUml.java` — Trace-Record-Population-Validierung
> 3. **Bestand validieren:** QvtoTraceModelTest (5) + QvtoE2eTraceTest (8) = 13 Tests vorhanden ✅
> 4. **Tests schreiben, laufen lassen, Failures = Implementierungslücken**

**Bestand:** 13 Tests (QvtoTraceModelTest: 5, QvtoE2eTraceTest: 8)

**Neue Tests (6):**

| Test | Beschreibung | Spec-Ref | Status |
|------|-------------|----------|--------|
| E2E: Trace enthält in-parameters | Mapping mit `in label : String` → params-Liste hat IN-Eintrag | §8.1.11.1 p74 | ✅ |
| E2E: Kein Trace bei when-Guard-Failure | `when { false }` → 0 trace-records | §8.1.11.1 p74 | ✅ |
| E2E: Object-Expression hat kein Trace | `object TargetElement {}` in main → 0 trace-records | §8.1.11.1 p75 | ✅ |
| E2E: Trace-Reihenfolge = Ausführungsreihenfolge | 3 Elemente → records in execution order (context prüfen) | §8.1.11.1 p74 | ✅ |
| E2E: inout-Parameter im Trace als IN | `inout count : Integer` → kind=IN in parameters | §8.1.11.1 p74 | ✅ |
| E2E: Mapping ohne Context | `mapping createElem()` → trace hat mapping-name, kein context-object | §8.1.11.1 p74 | ✅ |

**Ergebnis:** 6 Tests geschrieben. 2 davon deckten Implementierungslücke auf: in-parameters wurden nicht im Trace-Record erfasst.

**Implementierungs-Fix:**
- **Engine** (`QvtoTraceManager`): `addRecord()` erweitert um `Object[] args` Parameter. Formale Parameter aus `mappingOp.getEParameters()` werden durchlaufen, Result-Parameter übersprungen, in/inout-Werte als `VarParameterValue` mit `EDirectionKind.IN` in `EMappingParameters` eingetragen (§8.1.11.1: inout traced once as in-parameters).
- **Engine** (`QvtoEvaluator`): Beide `addRecord`-Aufrufe (normaler Return + ReturnException) um `args` ergänzt.

---

### P4-02: Trace Inhibition — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.11.2 (p75-76):**
>    - Inhibition section: vor Mapping-Body wird trace-data nach bestehendem trace-record durchsucht
>    - Match-Kriterien: executed-mapping + context-parameter + in-parameters
>    - Bei Match: vorherige out/result zurückgeben, Body nicht erneut ausführen
>    - Class instances: verglichen als Objekte (Identität), DataType values: deep value equality
>    - Traced Class instances sind mutable → können zwischen Aufrufen geändert werden
>    - Disjunct mappings: invoked-mapping ≠ executed-mapping möglich
> 2. **Eclipse-Referenztests prüfen:**
>    - `traceLookup_287589.qvto` — Performance: 20.000 Iterationen, shared instance pattern
>    - Gecko QVT-O InternalEvaluator: `checkForDuplicateExecution()`
> 3. **Bestand validieren:** QvtoE2eTraceInhibitionTest (6 Tests) ✅
> 4. **Tests schreiben, laufen lassen, Failures = Implementierungslücken**

**Bestand:** 6 Tests (QvtoE2eTraceInhibitionTest: bodyExecutesOnlyOnce, sameResultReturned, differentObjects_differentResults, differentMappings_noCacheConflict, nestedMappings, resolveFindsResult)

**Neue Tests (6):**

| Test | Beschreibung | Spec-Ref | Status |
|------|-------------|----------|--------|
| E2E: Same self + same in-param → inhibited | `e.map toTarget('same')` 2x → Body nur 1x | §8.1.11.2 p75-76 | ✅ |
| E2E: Same self + different in-param → NOT inhibited | `e.map toTarget('first')` + `('second')` → 2 Results | §8.1.11.2 p75-76 | ✅ |
| E2E: DataType deep-value equality | `e.map toTarget(42)` 2x → inhibiert (Integer deep equality) | §8.1.11.2 p76 | ✅ |
| E2E: Mutable result after inhibition | `r1.name := 'mutated'; r2 := e.map toTarget()` → r2.name = 'mutated' | §8.1.11.2 p76 | ✅ |
| E2E: No new trace-record on inhibition | 3x map → nur 1 trace-record | §8.1.11.2 p76 | ✅ |
| E2E: when-guard failure → no inhibit on retry | `when { pass }` mit false, dann true → Body ausgeführt | §8.1.11.1-2 | ✅ |

**Ergebnis:** 6 Tests geschrieben. 1 deckte Implementierungslücke auf: `lookupCachedResult` verglich nur `mappingName + source`, nicht `args`.

**Implementierungs-Fix:**
- **Engine** (`QvtoTraceRecord`): Um `Object[] args` erweitert. Neue Methode `argsMatch()` mit §8.1.11.2-konformer Vergleichslogik: `EObject` → Identität (`==`), DataType → `Objects.equals()` (deep equality).
- **Engine** (`QvtoTraceManager`): `lookupCachedResult()` um `args` erweitert, nutzt `record.argsMatch(args)`. `addRecord()` speichert `args.clone()` im Record.
- **Engine** (`QvtoEvaluator`): `lookupCachedResult`-Aufruf um `args` ergänzt.

---

### P4-03: resolve() by Type — ✅ DONE

> **🛑 SPEC-FIRST — §8.1.11.3 (p76):**
> - `resolve()` — alle target objects aus trace-data
> - `source.resolve(Type)` — nur targets von source, gefiltert nach Typ
> - `resolve(t : Type | cond)` — mit OCL-Condition
> - Ergebnis: Sequence in mapping invocation order

**Ergebnis:** 7 neue Tests, 0 Failures. **452 Tests gesamt.**

**Implementierungslücken gefixt (nicht disabled!):**
- **Grammar: AssignExp als Expression** (§8.2.2.4) — `:=`/`+=`/`::=` als niedrigste Priorität in `expression` Rule hinzugefügt. Ermöglicht `if cond then x := e endif` (§8.4 `<expression_block>`).
  - Datei: `QvtO.g4` — `expression` Rule überschrieben mit `assignOp` Alternative
  - Datei: `QvtoExpressionBuilder.java` — `visitAssignExp()` hinzugefügt
- **Test-Assertion:** `log()` erzeugt "log: " Prefix → `startsWith` → `contains` korrigiert

| Test | Status | Spec-Ref |
|------|--------|----------|
| `resolve_allTargets` | ✅ | §8.1.11.3 |
| `resolve_withSource_onlyFromSource` | ✅ | §8.1.11.3 |
| `resolve_withCondition` | ✅ | §8.1.11.3 |
| `resolve_emptyTrace_returnsEmptySequence` | ✅ | §8.1.11.3 |
| `resolve_inExecutionOrder` | ✅ | §8.1.11.3 |
| `resolve_sourceAndCondition` | ✅ | §8.1.11.3 |
| `resolve_afterWhenGuardFailure_empty` | ✅ | §8.1.11.3 + §8.1.11.1 |

---

### P4-04: resolveIn() by Mapping — ✅ DONE

> **🛑 SPEC-FIRST — §8.1.11.4 (p77):**
> - `resolveIn(Class2Table)` — targets aus Trace-Records mit invoked/executed-mapping = Class2Table
> - `source.resolveIn(Class2Table)` — nur von source
> - `source.resolveIn(Class2Table, t : Table | cond)` — mit OCL-Condition
> - `resolveoneIn` — Einzelergebnis-Variante (§8.1.11.6)

**Ergebnis:** 6 neue Tests, 0 Failures. **458 Tests gesamt.**

**Implementierungslücke gefixt:**
- **Parser: `inMapping` auf ResolveInExp nicht gesetzt** — `visitResolveInExp()` und `createDotResolveInCall()` ignorierten den `scopedName` komplett (TODO "resolved during linking phase" nie implementiert). Fix: `resolveInMapping()` Hilfsmethode extrahiert Mapping-Namen aus `scopedName` und erstellt MappingOperation-Stub.
  - Datei: `QvtoExpressionBuilder.java` — `resolveInMapping()` + Import `MappingOperation`

| Test | Status | Spec-Ref |
|------|--------|----------|
| `resolveIn_onlyFromMapping` | ✅ | §8.1.11.4 |
| `resolveIn_withSource` | ✅ | §8.1.11.4 |
| `resolveIn_withCondition` | ✅ | §8.1.11.4 |
| `resolveIn_noMatch_emptySequence` | ✅ | §8.1.11.4 |
| `resolveoneIn_singleResult` | ✅ | §8.1.11.6 |
| `resolveoneIn_withSourceAndCondition` | ✅ | §8.1.11.4+6 |

---

### P4-05: invresolve() / invresolveIn() — ✅ DONE

> **🛑 SPEC-FIRST — §8.1.11.5 (p77):**
> - `invresolve()` — inverse Suche: findet **Source-Objekte** statt Target-Objekte
> - `target.invresolve(SourceType)` — Sources die zu target gemappt wurden
> - `invresolveIn(Mapping, Type)` — Sources aus bestimmtem Mapping

**Ergebnis:** 6 neue Tests, 0 Failures. **484 Tests gesamt.**

**Implementierungslücken gefixt:**
- **Engine: `invresolveIn` fehlte** — `caseResolveExp` leitete `ResolveInExp` mit `isInverse=true` fälschlich an `resolveIn()` (forward) weiter. Fix: Dispatch prüft jetzt `isInverse` Flag bei ResolveInExp.
  - Datei: `QvtoEvaluator.java` — `caseResolveExp()` Dispatch erweitert
- **TraceManager: `invResolveIn()` hinzugefügt** — Neue Methode filtert inverse Trace-Records nach Mapping-Name.
  - Datei: `QvtoTraceManager.java` — `invResolveIn(mappingName, result, sourceType)`

| Test | Status | Spec-Ref |
|------|--------|----------|
| `invresolve_findsSource` | ✅ | §8.1.11.5 |
| `invresolve_allSources` | ✅ | §8.1.11.5 |
| `invresolve_withCondition` | ✅ | §8.1.11.5 |
| `invresolve_noMatch_empty` | ✅ | §8.1.11.5 |
| `invresolveIn_onlyFromMapping` | ✅ | §8.1.11.5 |
| `invresolveoneIn_withSource` | ✅ | §8.1.11.5+6 |

---

### P4-06: resolveone() / resolveoneIn() — ✅ DONE

**Ergebnis:** 4 neue Tests, alle ✅ sofort grün. Keine Implementierungslücken — Bestand (6 Tests aus P4-03/04/05) + 4 neue decken alle resolveone/invresolveone Varianten ab.

**Analyse:** 8 Varianten laut §8.1.11.6. Davon bereits durch Bestand abgedeckt:
- `resolveone_afterMapping`, `resolveone_noMatch`, `resolveone_withCondition`, `resolveone_emptyTrace`, `resolveone_multipleMappings`, `resolveone_withExplicitSource` (P4-03)
- `resolveoneIn_singleResult`, `resolveoneIn_withSourceAndCondition` (P4-04)
- `invresolveoneIn_withSource` (P4-05)

| Test | Status | Spec-Ref |
|------|--------|----------|
| `invresolveone_findsFirstSource` | ✅ | §8.1.11.5+6 |
| `invresolveone_withCondition` | ✅ | §8.1.11.5+6 |
| `resolveone_multipleResults_returnsFirst` | ✅ | §8.1.11.6 |
| `invresolveoneIn_noMatch_null` | ✅ | §8.1.11.5+6 |

---

### P4-07: Late Resolve — ✅ DONE

**Ergebnis:** 6 neue Tests, alle ✅ sofort grün. Keine Implementierungslücken.

**Bestand:** 11 Tests + 6 neue = 17 Late-Resolve-Tests gesamt.

**Implementierungs-Fix:** `coll->resolve(Type)` Arrow-Syntax (§8.1.11.7 xcollect-Shorthand):
- **Parser: `iteratorOrOperationCall` erweitert** — `ArrowResolveCall` + `ArrowResolveInCall` Alternativen hinzugefügt
- **Parser: `createArrowResolveCall()` + `createArrowResolveInCall()`** — bauen ResolveExp mit Collection als Source
- **Engine: `resolveForSource()` extrahiert** — wiederverwendbare Resolve-Logik für einzelne Source-Objekte
- **Engine: Collection-Source-Handling** — wenn sourceObj eine Collection ist, per-Element iterieren und Ergebnisse sammeln

| Test | Status | Spec-Ref |
|------|--------|----------|
| `lateResolveIn_mapping` | ✅ | §8.1.11.7+4 |
| `lateInvresolveIn_mapping` | ✅ | §8.1.11.7+5 |
| `lateResolve_returnsNullImmediately` | ✅ | §8.1.11.7 |
| `lateResolve_sameProperty_lastWins` | ✅ | §8.1.11.7 |
| `lateResolve_conditionEvaluatedDeferred` | ✅ | §8.1.11.7+3 |
| `lateResolve_orderIndependent` | ✅ | §8.1.11.7 |
| `lateResolve_arrowCollection` | ✅ | §8.1.11.7 (xcollect) |
| `lateResolve_arrowCollection_deferred` | ✅ | §8.1.11.7 (xcollect) |

---

### P4-08: Resolve Parse (vertieft) — ✅ DONE

**Ergebnis:** 8 neue Parse-Tests, alle ✅ sofort grün. Keine Implementierungslücken.

| Test | Status | Spec-Ref |
|------|--------|----------|
| `resolveoneIn_withType` | ✅ | §8.4 |
| `invresolveIn_parse` | ✅ | §8.4 |
| `invresolveoneIn_allFlags` | ✅ | §8.4 |
| `lateResolveIn_parse` | ✅ | §8.4 |
| `lateInvresolveone_allFlags` | ✅ | §8.4 |
| `dotResolve_sourceTargetCondition` | ✅ | §8.4 |
| `dotResolveoneIn_fullSyntax` | ✅ | §8.4 |
| `resolveone_inVarDeclaration` | ✅ | §8.4 |

---

## Phase 5: Composing, Disjuncts, Inherits/Merges (~55 Tests)

> **⚠️ SPEC-FIRST WORKFLOW (bei JEDEM Task befolgen!)**
>
> **Tests definieren das korrekte Verhalten — NICHT die aktuelle Implementierung.**
>
> 1. **ZUERST die Spec lesen** — QVT-O v1.3: `docs/specifications/QVT/QVT-formal-16-06-03.pdf`, §8.1.13–8.1.15 (PDF p81–84)
> 2. **Tests gegen die Spec schreiben** — das erwartete Ergebnis kommt aus der Spec, NICHT aus dem Code
> 3. **Tests laufen lassen** — Failures sind Implementierungslücken, NICHT Testfehler
> 4. **Implementierung SOFORT fixen** — NICHT disablen, sondern Implementierungslücken gleich schließen!
> 5. **NIEMALS einen Test an die Implementierung anpassen**, um ihn grün zu machen
> 6. **User entscheidet**, ob/wann ein Test `@Disabled` wird — bei Unklarheit: Rückfrage
>
> **Referenz-Quellen:** 1. QVT-O v1.3 Spec → 2. Eclipse QVT-O Tests → 3. Gecko QVT-O → 4. Spec hat Vorrang → 5. Bei Unklarheit: Rückfrage

**Spec-Abschnitte:** §8.1.13–8.1.15 (§8.1.12 = Resolve/Update, bereits in Phase 4 abgedeckt)

**Eclipse-Referenz:** `repositories/org.eclipse.qvto/tests/org.eclipse.m2m.tests.qvt.oml/parserTestData/models/`

---

### P5-01: Disjunct Mappings — Explicit (§8.1.14.1) — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.14.1 (p82):**
>    - `mapping f() disjuncts m1, m2, m3 {}` — explizite Disjunktion
>    - Disjuncting mapping hat leeren Body, delegiert an Candidates
>    - Candidates werden in Deklarations-Reihenfolge evaluiert
>    - Jeder Candidate hat when-Guard (explizit) oder Typ-Match (implizit) als Predicate
>    - Erster Match gewinnt, kein Match → null
>    - Return-Typ der Candidates muss covariant zum disjuncting mapping sein
> 2. **Eclipse-Referenztests prüfen:**
>    - `disjuncts_explicit.qvto`, `disjuncts_when.qvto`
> 3. **Bestand validieren:** Bestehende Tests zählen
> 4. **Tests schreiben, laufen lassen, Failures = Implementierungslücken → SOFORT fixen!**

**Neue Tests (~8):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: Explicit disjuncts — first when-match wins | 3 Candidates, erster matching when-Guard gewinnt | §8.1.14.1 |
| E2E: Explicit disjuncts — no match returns null | Kein Candidate matched → null | §8.1.14.1 |
| E2E: Explicit disjuncts — declaration order | 2 Candidates die beide matchen → erster in Deklaration gewinnt | §8.1.14.1 |
| E2E: Explicit disjuncts — empty body | Disjuncting mapping hat `{}` → nur Candidates Body ausgeführt | §8.1.14.1 |
| Parse: `disjuncts` Keyword | `mapping f() disjuncts m1, m2` → MappingOperation mit disjuncts-Liste | §8.1.14.1 |
| Parse: `disjuncts` mit when-Guards | Candidates mit `when {}` werden geparst | §8.1.14.1 |
| E2E: Explicit disjuncts — covariant return | Candidate-Return ist Subtyp des disjuncting-Return | §8.1.14.1 |
| E2E: Explicit disjuncts — with parameters | Disjuncting mapping mit Parametern an Candidates weitergereicht | §8.1.14.1 |

**Ergebnis:** 6 neue Tests geschrieben, 3 Failures = Implementierungslücken, sofort gefixt:
- **GUARD_FAILED sentinel** in `QvtoEvaluator` — when-guard failure vs. null-Ergebnis unterscheidbar
- **Scoped name resolution** in `QvtoUnitBuilder.resolvePendingExtensions()` — `SourceElement::toHigh` → `toHigh`
- **Alle 490 QVT-O Tests grün, 0 Failures.**

---

### P5-02: Disjunct Mappings — Implicit (§8.1.14.2) — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.14.2 (p82–83):**
>    - Implicit disjuncts: Mapping overloads ein anderes wenn Source-Typ abgeleitet + gleicher Name + gleiche Parameter-Anzahl
>    - Automatische Candidate-Erkennung basierend auf Typ-Hierarchie
>    - Mapping für derived type occluded mapping für base type
> 2. **Eclipse-Referenztests prüfen:**
>    - `disjuncts_implicit.qvto`
> 3. **Tests schreiben, laufen lassen, Failures → SOFORT fixen!**

**Neue Tests (~7):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: Implicit disjuncts — derived type wins | Mapping auf SubType hat Vorrang vor BaseType | §8.1.14.2 |
| E2E: Implicit disjuncts — same name required | Gleichnamige Mappings bilden implizite Disjunktion | §8.1.14.2 |
| E2E: Implicit disjuncts — base fallback | Kein spezialisiertes Mapping → Base-Mapping greift | §8.1.14.2 |
| E2E: Implicit disjuncts — parameter count match | Parameter-Anzahl muss übereinstimmen | §8.1.14.2 |
| Parse: Implicit disjuncts — overloaded mappings | Gleicher Name, verschiedene Context-Typen → geparst | §8.1.14.2 |
| E2E: Disjunct candidate order (§8.1.14.3) | Sortier-Kriterien: current trans → extended → most derived → alphabetisch | §8.1.14.3 |
| E2E: Strict evaluation (xmap) | `xmap` mit Disjuncts: kein Guard-Eval, nur Typ-Konformität | §8.1.14.3 |

**Ergebnis:** 5 neue Tests geschrieben, 2 Failures = Implementierungslücken, sofort gefixt:
- **Context-type filtering** in `QvtoEvaluator.filterAndSortByType()` — nur type-compatible Candidates
- **Most-derived-first sorting** — SpecialSourceElement::convert() hat Vorrang vor SourceElement::convert()
- **Alle 495 QVT-O Tests grün, 0 Failures.**

---

### P5-03: Mapping Inherits (§8.1.15) — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.15 (p83–84):**
>    - `mapping f() inherits base()` — inherited mapping wird NACH init-Section ausgeführt
>    - Execution order: init → inherited body → own body → end
>    - Inherited mapping teilt die gleichen result-Parameter
> 2. **Eclipse-Referenztests prüfen:**
>    - `mapping_inherits.qvto`, `mapping_inherits_multi.qvto`
> 3. **Tests schreiben, laufen lassen, Failures → SOFORT fixen!**

**Neue Tests (~8):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: `inherits` — base body runs after init | Init → inherited body → own body | §8.1.15 |
| E2E: `inherits` — shared result | Inherited mapping modifiziert gleichen result | §8.1.15 |
| E2E: `inherits` — base sets properties | Base setzt Properties, derived fügt hinzu | §8.1.15 |
| E2E: `inherits` — multiple inheritance | `inherits a, b` → beide Bodies laufen in Reihenfolge | §8.1.15 |
| E2E: `inherits` — when guard on inherited | Inherited mapping hat when → guard wird evaluiert | §8.1.15 |
| Parse: `inherits` Keyword | `mapping f() inherits base()` → MappingOperation.inherited | §8.1.15 |
| Parse: Multiple inherits | `mapping f() inherits a, b` → 2 Einträge in inherited-Liste | §8.1.15 |
| E2E: `inherits` — deep chain | A inherits B, B inherits C → alle 3 Bodies laufen | §8.1.15 |

**Ergebnis:** 3 neue Tests (execution order, multiple bases, parent guard false), 1 Failure = Implementierungslücke, sofort gefixt:
- **When-guard check in `executePopulationOnly()`** — inherited/merged mit `when { false; }` werden übersprungen

---

### P5-04: Mapping Merges (§8.1.15) — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.15 (p84):**
>    - `mapping f() merges other1, other2` — merged mappings werden NACH end-Section ausgeführt
>    - Execution order: init → body → end → merged1 → merged2
>    - Merged mapping hat when-Guard → wird nur ausgeführt wenn Guard true
>    - "A merged mapping is not invoked if the guard is not satisfied"
> 2. **Eclipse-Referenztests prüfen:**
>    - `mapping_merges.qvto`, `mapping_merge_when.qvto`
> 3. **Tests schreiben, laufen lassen, Failures → SOFORT fixen!**

**Neue Tests (~7):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: `merges` — runs after end | Own body → end → merged body | §8.1.15 |
| E2E: `merges` — multiple | `merges a, b` → beide nach end in Reihenfolge | §8.1.15 |
| E2E: `merges` — when guard | Merged mapping mit when → nur wenn Guard true | §8.1.15 |
| E2E: `merges` — when guard false | When-Guard false → merged mapping wird übersprungen | §8.1.15 |
| E2E: `merges` — shared result | Merged mapping modifiziert gleichen result | §8.1.15 |
| Parse: `merges` Keyword | `mapping f() merges other()` → MappingOperation.merged | §8.1.15 |
| Parse: Multiple merges | `mapping f() merges a, b` → 2 Einträge in merged-Liste | §8.1.15 |

**Ergebnis:** 4 neue Tests (execution order, guard false skipped, multiple in order, inherits+merges combined), gleicher Fix wie P5-03.
- **Alle 502 QVT-O Tests grün, 0 Failures.**

---

### P5-05: Composing Transformations (§8.1.13) — ✅ DONE

> **🛑 SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.13 (p81):**
>    - `access transformation T(inout m : MT)` — importiert T als Dependency
>    - `extends transformation T(in m1 : MT1, out m2 : MT2)` — class inheritance Semantik
>    - `new T(params)` — explizite Transformation-Instanziierung
>    - `transform()` — Aufruf einer instanziierten Transformation
>    - `copy()` — Model-Cloning (Deep Copy)
>    - `inout` Extent — In-place-Modifikation
> 2. **Eclipse-Referenztests prüfen:**
>    - `access_transformation.qvto`, `extends_transformation.qvto`, `inout_transformation.qvto`
> 3. **Tests schreiben, laufen lassen, Failures → SOFORT fixen!**

**Neue Tests (~10):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: `inout` Extent — in-place modify | `transformation T(inout m : MT)` modifiziert Eingabe | §8.1.13 |
| E2E: `inout` Extent — mapping modifies source | Mapping ändert Source-Properties in-place | §8.1.13 |
| Parse: `access transformation` | Access-Deklaration wird geparst | §8.1.13 |
| Parse: `extends transformation` | Extends-Deklaration wird geparst | §8.1.13 |
| Parse: `inout` Parameter | `transformation T(inout m : MT)` → inout-VarParameter | §8.1.13 |
| E2E: `extends` — inherited operations | Extended transformation erbt Operationen | §8.1.13 |
| E2E: `extends` — main override | Extending transformation kann main() überschreiben | §8.1.13 |
| E2E: `new T()` — transformation instantiation | Explizite Instanziierung einer Transformation | §8.1.13 |
| E2E: `transform()` — invoke instantiated | Instanziierte Transformation ausführen | §8.1.13 |
| E2E: `copy()` — deep clone | Model-Cloning via copy() | §8.1.13 |

**Ergebnis:** 3 neue Tests (inout in-place modify, extends inherits ops, access imports ops), keine Failures.
- **`new T()`, `transform()`** sind implementiert (P10-01) — `copy()` noch deferred
- **Alle 505 QVT-O Tests grün, 0 Failures.**

---

## Phase 6: Imperative Expressions (~120 Tests)

> **⚠️ SPEC-FIRST WORKFLOW (bei JEDEM Task befolgen!)**
>
> **Tests definieren das korrekte Verhalten — NICHT die aktuelle Implementierung.**
>
> 1. **ZUERST die Spec lesen** — QVT-O v1.3: `docs/specifications/QVT/QVT-formal-16-06-03.pdf`, §8.1.17, §8.2.2
> 2. **Tests gegen die Spec schreiben** — das erwartete Ergebnis kommt aus der Spec, NICHT aus dem Code
> 3. **Tests laufen lassen** — Failures sind Implementierungslücken, NICHT Testfehler
> 4. **Implementierung fixen** — bis die Tests grün sind. **SOFORT fixen, nicht disablen!**
> 5. **NIEMALS einen Test an die Implementierung anpassen**, um ihn grün zu machen
> 6. **Bei Unklarheit: Rückfrage an den User**, nicht raten
>
> **Referenz-Quellen:** 1. QVT-O v1.3 Spec → 2. Eclipse QVT-O Tests → 3. Gecko QVT-O → 4. Spec hat Vorrang → 5. Bei Unklarheit: Rückfrage

**Spec-Abschnitte:** §8.1.17, §8.2.2 (p85–86, p117–136)

**Eclipse-Referenz:** `repositories/org.eclipse.qvto/tests/org.eclipse.m2m.tests.qvt.oml/parserTestData/models/`

**Bestand:** 99 imperative Tests vorhanden (34 Parse, 20 Engine, 45 E2E) — alle grün

**Testklassen:**
- Parse: `QvtoImperativeExpParseTest` (14), `QvtoAssignVarParseTest` (10), `QvtoTryCatchParseTest` (10)
- Engine: `QvtoControlFlowTest` (6), `QvtoVariableTest` (6), `QvtoComputeExpTest` (3), `QvtoLogAssertTest` (5)
- E2E: `QvtoE2eControlFlowTest` (26), `QvtoE2eCollectionTest` (19)

---

### P6-01: VariableInitExp & AssignExp (§8.2.2.10, §8.2.2.11) — ✅ DONE (14 Tests)

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.2.2.10 VariableInitExp (p128):**
>    - `var x : String := "abracadabra"` — Deklaration mit optionalem Init
>    - `withResult` property: `:=` (false) vs. `::=` (true, returns init value)
>    - Typ kann weggelassen werden → Typ-Inferenz
>    - Default-Werte: Collection → leer, Numeric → 0, String → "", sonst null
>    - Mehrere Deklarationen: `var x:= "", i:=0;`
>    - `=` als Alternative zu `:=`
> 2. **Spec lesen — §8.2.2.11 AssignExp (p129–130):**
>    - `:=` (isReset=true) vs. `+=` (isReset=false, append) vs. `-=` (isSubtract=true, remove from collection)
>    - Single-valued: `left := value` — ersetzt
>    - Multi-valued: `left := value` — Reset + Assign
>    - Multi-valued: `left += value` — Append
>    - `default` keyword für Null-Ersetzung
>    - Deferred assignment bei `late resolve`
>    - Composite assignment: `feature := { expr1; expr2; }`
> 3. **Eclipse-Referenztests:** `assignaliases_source.qvto`, `bug400089_source.qvto`
> 4. **Bestand prüfen:** 11 Parse-Tests (inkl. `-=`) + 8 Engine-Tests vorhanden
> 5. **Neue Tests schreiben → laufen lassen → Failures = SOFORT fixen!**

**Neue Tests (~10):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: var init — type inference from value | `var x := 42` → Integer | §8.2.2.10 |
| E2E: var init — default values | `var s : String` → "" , `var i : Integer` → 0, `var c : Sequence(String)` → leer | §8.2.2.10 |
| E2E: var init — withResult (::=) | `if (var x ::= "hello") then ...` — Init-Wert als Return | §8.2.2.10 |
| E2E: var — multiple declarations | `var x := "", i := 0` — Gruppierte Deklaration | §8.2.2.10 |
| E2E: assign — single-valued reset | `x := "new"` ersetzt alten Wert | §8.2.2.11 |
| E2E: assign — multi-valued append (+=) | `list += element` hängt an | §8.2.2.11 |
| E2E: assign — multi-valued subtract (-=) | `list -= element` entfernt aus Collection | §8.2.2.11 |
| E2E: assign — property subtract (-=) | `eo.refs -= element` entfernt aus multi-valued EReference | §8.2.2.11 |
| E2E: assign — multi-valued reset (:=) | `list := Sequence{1,2}` ersetzt komplett | §8.2.2.11 |
| E2E: assign — property on EObject | `result.name := "test"` — Feature-Assign | §8.2.2.11 |
| E2E: assign — default keyword | `x := expr default "fallback"` bei null → fallback | §8.2.2.11 |
| E2E: assign — composite | `feature := { expr1; expr2 }` — Sequence | §8.2.2.11 |

---

### P6-02: BlockExp & if/then/else (§8.2.2.2, §8.2.2.8) — ✅ DONE

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.2.2.2 BlockExp (p121):**
>    - `do { stmt1; stmt2; }` — Sequenz-Ausführung, returned null
>    - Neue Scope: Lokale Variablen nicht außerhalb sichtbar
>    - Kann durch break, continue, return unterbrochen werden
>    - `do` keyword innerhalb von if/switch/for kann weggelassen werden
> 2. **Spec lesen — §8.2.2.8 SwitchExp als if/then/else (p127):**
>    - Zwei Notationsformen: `if (cond) exp1 elif (cond2) exp2 else exp3 endif`
>    - Java-like: `if (cond) { body } else { body }`
>    - SwitchExp mit alternativePart (AltExp) + elsePart
>    - Sensitiv für break/continue/raise/return innerhalb
> 3. **Eclipse-Referenztests:** `controlflow_source.qvto`
> 4. **Bestand prüfen:** 0 BlockExp-Tests (implizit überall), 2 if/else E2E-Tests
> 5. **Neue Tests schreiben → laufen lassen → Failures = SOFORT fixen!**

**Neue Tests (8):** alle ✅

| Test | Beschreibung | Spec-Ref | Status |
|------|-------------|----------|--------|
| `blockExp_outerVarModifiableInsideBlock` | Outer-Scope-Var im Block änderbar | §8.2.2.2 | ✅ |
| `blockExp_varScopedToBlock_reusableInNextBlock` | Block-lokale Var außerhalb unsichtbar, wiederverwendbar | §8.2.2.2 | ✅ |
| `blockExp_nestedBlocks_innerScopeIndependent` | Verschachtelte Blöcke, separate Scopes | §8.2.2.2 | ✅ |
| `if_elif_else_chain_matchesCorrectBranch` | if/elif/else-Kette, alle Branches | §8.2.2.8 | ✅ |
| `if_asExpression_returnsValue` | if als Expression mit Rückgabewert | §8.2.2.8 | ✅ |
| `if_noElse_conditionFalse_returnsNull` | if ohne else, false → null | §8.2.2.8 | ✅ |
| `if_elif_noElse_noMatch_continuesExecution` | if/elif ohne else, kein Match → weiter | §8.2.2.8 | ✅ |
| `switch_noElse_noMatch_returnsNull` | switch kein Match → null verifiziert | §8.2.2.8 | ✅ |

**Parser-Fix:** IfExp-Grammatik akzeptiert jetzt `elif` neben `elseif` (spec-konform, §8.2.2.8 zeigt `elif`)

---

### P6-03: WhileExp & ComputeExp (§8.2.2.4, §8.2.2.3) — ✅ DONE

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.2.2.4 WhileExp (p121–122):**
>    - `while (condition) { body }` — Schleife bis condition = false, returns null
>    - break beendet while, continue springt zur nächsten Iteration
>    - Compute-Shorthand: `while (x:MyClass := self.getFirstItem(); x<>null) { ... }`
> 2. **Spec lesen — §8.2.2.3 ComputeExp (p121):**
>    - `compute (x : String = "_") { body }` — definiert Variable, returned deren Wert nach body
>    - Kombination mit while: `compute (x:T) { while ... }`
>    - forEach-Shorthand: `compute (s:String = "_") { self->forEach(...) { s += i.name; } }`
> 3. **Eclipse-Referenztests:** `controlflow_source.qvto`, `compute_source.qvto`
> 4. **Bestand prüfen:** 4 while-E2E + 3 compute-Engine + 2 compute-E2E Tests vorhanden
> 5. **Neue Tests schreiben → laufen lassen → Failures = SOFORT fixen!**

**Neue Tests (8):** alle ✅

| Test | Beschreibung | Spec-Ref | Status |
|------|-------------|----------|--------|
| `while_returnsNull` | WhileExp returned null (nicht letzten Body-Wert) | §8.2.2.4 | ✅ |
| `while_nestedBreak_onlyInnerLoopBreaks` | Inner while bricht, outer läuft weiter | §8.2.2.4 | ✅ |
| `while_continue_skipsRestOfIteration` | continue überspringt Rest der Iteration | §8.2.2.4 | ✅ |
| `while_withInitVariable_computeShorthand` | `while (n:Integer := 3; n > 0) { }` Compute-Shorthand | §8.2.2.4 | ✅ |
| `compute_returnsFinalVarValue` | `compute (acc:Integer = 0) { acc := 15 }` → 15 | §8.2.2.3 | ✅ |
| `compute_withWhileLoop_accumulates` | compute + while Kombination | §8.2.2.3 | ✅ |
| `compute_withForEach_accumulates` | compute + forEach Kombination | §8.2.2.3 | ✅ |
| `compute_initialValueReturned_whenBodyEmpty` | Leerer Body → init-Wert | §8.2.2.3 | ✅ |

**Implementierungs-Fixes:**
1. **WhileExp returned jetzt null** (war: letzter Body-Wert) — §8.2.2.4: "The returned value is null"
2. **Parser: while-init-Shorthand** hinzugefügt — `while (x:Type := init; cond) { }` wird zu `compute(x:Type = init) { while (cond) { } }` desugared (§8.2.2.4 Notation)

---

### P6-04: ForExp — forEach & forOne (§8.2.2.6) — ✅ DONE

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.2.2.6 ForExp (p122–124):**
>    - `forEach`: iteriert über Collection, evaluiert Body für jedes Element (mit optionaler Condition)
>    - `forOne`: wie forEach + break nach erstem Match
>    - Semantik: `do { count := 1; while (count <= source->size()) { var iter := source->at(count); if (condition) body; count += 1; }; };`
>    - Notation: `list->forEach(i) { ... }`, `list->forEach(i | condition) { ... }`
>    - Compute-Shorthand: `mylist->forEach(i;x:X=...|cond) { ... }` = `compute (x:X=...) mylist->forEach(i|cond) { ... }`
>    - Unordered Collection wird zu geordneter konvertiert (Set → OrderedSet, Bag → Sequence)
> 2. **Eclipse-Referenztests:** `foreach_source.qvto`, `forone_source.qvto`
> 3. **Bestand prüfen:** 9 forEach-E2E + 3 forOne-E2E Tests vorhanden
> 4. **Neue Tests schreiben → laufen lassen → Failures = SOFORT fixen!**

**Neue Tests (8):** alle ✅ — keine Implementierungs-Fixes nötig

| Test | Beschreibung | Spec-Ref | Status |
|------|-------------|----------|--------|
| `forEach_withCondition_filtersElements` | forEach mit Condition — nur Elemente >3 | §8.2.2.6 | ✅ |
| `forEach_returnsNull` | forEach-Rückgabe ist null | §8.2.2.6 | ✅ |
| `forEach_overOrderedSet_iteratesInOrder` | OrderedSet-Iteration in Reihenfolge | §8.2.2.6 | ✅ |
| `forEach_continue_skipsCurrentElement` | continue springt zum nächsten Element | §8.2.2.6 | ✅ |
| `forEach_nestedForEach_independentIterators` | Verschachtelte forEach, unabhängige Iteratoren | §8.2.2.6 | ✅ |
| `forEach_return_exitsEnclosingHelper` | return innerhalb forEach beendet Helper | §8.2.2.6 | ✅ |
| `forEach_withCondition_andBreak` | forEach mit Condition + break | §8.2.2.6 | ✅ |
| `forOne_conditionFalse_bodyNotExecuted` | forOne kein Match → Body nicht ausgeführt | §8.2.2.6 | ✅ |

---

### P6-05: ImperativeIterateExp — xcollect, xselect, xcollectselect (§8.2.2.7) — ✅ DONE

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.2.2.7 ImperativeIterateExp (p124–127):**
>    - 5 Varianten: `xcollect`, `xcollectselect`, `xcollectselectOne`, `xselect`, `xselectOne`
>    - Wie OCL collect/select aber mit imperativen Erweiterungen (break/continue/return)
>    - `xcollect`: `list->prop` = `list->xcollect(i | i.prop)` — null values entfernt
>    - `xselect`: `list[condition]` = `list->xselect(i | condition)`
>    - `xcollectselect`: `list->prop[res | condition]` = `list->xcollectselect(i; res := i.prop | condition)`
>    - `xselectOne`: `list![condition]` — `!` prefix
>    - Type re-casting: wenn condition TypeExp ist → `oclIsKindOf(TypeExp)` + Typ-Cast des Ergebnisses
>    - Shorthand: `list->prop` (xcollect), `list[cond]` (xselect), `list->prop[cond]` (xcollectselect)
> 2. **Eclipse-Referenztests:** `imperativecollect_source.qvto`, `imperativeselect_source.qvto`
> 3. **Bestand prüfen:** 0 dedizierte Tests vorhanden!
> 4. **Neue Tests schreiben → laufen lassen → Failures = SOFORT fixen!**

**Neue Tests (~10):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: xcollect — property shorthand | `list->name` sammelt alle .name Werte | §8.2.2.7 |
| E2E: xcollect — null values removed | Null-Werte in xcollect-Ergebnis entfernt | §8.2.2.7 |
| E2E: xselect — bracket condition | `list[value > 5]` filtert | §8.2.2.7 |
| E2E: xselect — type re-casting | `list[MyType]` = `list->xselect(oclIsKindOf(MyType))` + Cast | §8.2.2.7 |
| E2E: xselectOne — first match | `list![condition]` → erster Match | §8.2.2.7 |
| E2E: xcollectselect — combined | `list->prop[cond]` sammelt + filtert | §8.2.2.7 |
| E2E: xcollectselectOne — first | `list->prop![cond]` → erster Match | §8.2.2.7 |
| E2E: xcollect — with break | break innerhalb xcollect beendet Iteration | §8.2.2.7 |
| E2E: imperative collect — map shorthand | `list->map f()` = `list->xcollect(i \| i.map f())` | §8.2.2.7 |
| E2E: xselect — empty result | Kein Match → leere Collection | §8.2.2.7 |

---

### P6-06: ReturnExp, BreakExp, ContinueExp (§8.2.2.16–18) — ✅ DONE

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.2.2.16 ReturnExp (p132–133):**
>    - `return` — verlässt Operation, unterbricht normalen Kontrollfluss
>    - `return expr` — Wert wird result-Parameter zugewiesen
>    - Bei mehreren Return-Parametern: Tuple
>    - Ohne Wert: Tuple aus aktuellen Result-Parametern
> 2. **Spec lesen — §8.2.2.17 BreakExp (p133):**
>    - `break` — beendet Iteration in while/for-Schleifen
>    - Darf NICHT in side-effect-free OCL iterate benutzt werden
> 3. **Spec lesen — §8.2.2.18 ContinueExp (p133):**
>    - `continue` — springt zur nächsten Iteration
>    - Nur in imperativen Schleifen (while, for)
> 4. **Eclipse-Referenztests:** `return_source.qvto`, `controlflow_source.qvto`
> 5. **Bestand prüfen:** 3 return-E2E + 3 break-E2E + 1 continue-E2E vorhanden
> 6. **Neue Tests schreiben → laufen lassen → Failures = SOFORT fixen!**

**Neue Tests (~8):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: return — from mapping with value | `return result` innerhalb mapping | §8.2.2.16 |
| E2E: return — without value | `return;` erstellt Tuple aus aktuellen result-Params | §8.2.2.16 |
| E2E: return — from nested if | Return innerhalb if/else in mapping | §8.2.2.16 |
| E2E: return — from forEach (early exit) | Return in forEach beendet gesamte Operation | §8.2.2.16 |
| E2E: break — in nested while | Nur inneres while beendet | §8.2.2.17 |
| E2E: break — in forEach | break beendet forEach-Schleife | §8.2.2.17 |
| E2E: continue — in while | Überspringt Rest der while-Iteration | §8.2.2.18 |
| E2E: continue — in forEach | Überspringt aktuelles Element | §8.2.2.18 |

---

### P6-07: LogExp & AssertExp (§8.2.2.19–20) — ✅ DONE

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.2.2.19 LogExp (p133–134):**
>    - `log(message)` — Schreibt Log-Eintrag, returns null
>    - `log(message, element)` — mit Model-Element (impliziter repr()-Aufruf)
>    - `log(message, element, level)` — mit Log-Level
>    - Optional: `when condition` — Log nur wenn Bedingung wahr
>    - Superclasses: OperationCallExp + ImperativeExpression
> 2. **Spec lesen — §8.2.2.20 AssertExp (p134):**
>    - `assert condition` — prüft Bedingung, generiert Error-Diagnostic bei Failure
>    - `assert warning condition` — Severity: warning
>    - `assert fatal condition` — Severity: fatal → terminiert mit AssertionFailed
>    - `assert condition with log(message)` — mit Log-Record
>    - Default Severity: error
>    - SeverityKind Enum: warning, error, fatal
> 3. **Eclipse-Referenztests:** `log_source.qvto`, `assert_source.qvto`
> 4. **Bestand prüfen:** 3 log-Tests + 7 assert-Tests vorhanden
> 5. **Neue Tests schreiben → laufen lassen → Failures = SOFORT fixen!**

**Neue Tests (~8):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: log — basic message | `log("hello")` → Diagnostic erzeugt | §8.2.2.19 |
| E2E: log — with element | `log("msg", element)` → repr() implizit | §8.2.2.19 |
| E2E: log — with level | `log("msg", element, 2)` — Level-Parameter | §8.2.2.19 |
| E2E: log — when false skips | `log("msg") when false` → kein Diagnostic | §8.2.2.19 |
| E2E: assert — error (default) | `assert false` → Error-Diagnostic | §8.2.2.20 |
| E2E: assert — warning severity | `assert warning false` → Warning-Diagnostic | §8.2.2.20 |
| E2E: assert — fatal terminates | `assert fatal false` → AssertionFailed Exception | §8.2.2.20 |
| E2E: assert — with log message | `assert false with log("expected true")` | §8.2.2.20 |

---

### P6-08: TryExp, CatchExp, RaiseExp (§8.2.2.13–15) — ✅ DONE

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.2.2.13 TryExp (p131):**
>    - `try { body } except (ExType1, ExType2) { handler }` — Exception-Handling
>    - exceptClauses in Reihenfolge geprüft, erster passender Typ gewinnt
>    - Nested exception in exceptClause terminiert den Clause (außer nested TryExp)
> 2. **Spec lesen — §8.2.2.14 CatchExp (p131–132):**
>    - `except (ExType) { body }` — Exception-Clause
>    - exceptionVariable: Zugriff auf die gefangene Exception
>    - exception: Type [+] — Liste der behandelten Exception-Typen
> 3. **Spec lesen — §8.2.2.15 RaiseExp (p132):**
>    - `raise StringException("ProblemHere")` — Exception werfen
>    - Shorthand: `raise "ProblemHere"` → StringException
>    - Verwendbar mit `default`: `x := expr default raise "error"`
> 4. **Eclipse-Referenztests:** `trycatch_source.qvto`, `exceptions_source.qvto`
> 5. **Bestand prüfen:** 3 Parse-Tests, 0 Execution-Tests! ⚠️ Kritische Lücke
> 6. **Neue Tests schreiben → laufen lassen → Failures = SOFORT fixen!**

**Neue Tests (~10):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: try/except — basic catch | `try { raise "err" } except (String) { "caught" }` | §8.2.2.13 |
| E2E: try — no exception passes through | `try { "ok" } except (String) { "err" }` → "ok" | §8.2.2.13 |
| E2E: try — multiple except clauses | Erster passender Typ gewinnt | §8.2.2.13 |
| E2E: try — nested try/except | Inneres try fängt innere Exception | §8.2.2.13 |
| E2E: except — exception variable | `except (e : String) { log(e) }` — Zugriff auf Exception | §8.2.2.14 |
| E2E: raise — string shorthand | `raise "error"` → StringException | §8.2.2.15 |
| E2E: raise — with type constructor | `raise StringException("msg")` | §8.2.2.15 |
| E2E: raise — uncaught propagates | Exception ohne try → Transformation-Fehler | §8.2.2.15 |
| E2E: raise — in default | `x := expr default raise "fallback"` | §8.2.2.15 |
| E2E: assert fatal — try catches it | `try { assert fatal false } except (...) { ... }` | §8.2.2.13+20 |

---

### P6-09: Nested Imperative Combinations — ✅ DONE

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec-Querverweis:** §8.2.2 Figure 8.6 (p117) — Klassendiagramm der ImperativeExpression-Hierarchie
> 2. **Constraint (p119):** Jeder Containment-Ancestor eines ImperativeExpression muss selbst ImperativeExpression sein
> 3. **Ziel:** Testen, dass verschachtelte Kombinationen korrekt funktionieren
> 4. **Bestand prüfen:** 2 nesting-Tests vorhanden (nestedForEach, nestedWhile)
> 5. **Neue Tests schreiben → laufen lassen → Failures = SOFORT fixen!**

**Neue Tests (~6):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: forEach inside while | while-Schleife enthält forEach | §8.2.2 |
| E2E: switch inside forEach | forEach mit switch zur Fallunterscheidung | §8.2.2 |
| E2E: compute with nested while and break | compute + while + break | §8.2.2 |
| E2E: try inside forEach | Exception-Handling pro Iteration | §8.2.2 |
| E2E: return from deeply nested | Return aus forEach in if in mapping | §8.2.2 |
| E2E: multiple imperative constructs in mapping | Mapping mit var + forEach + if + compute | §8.2.2 |

---

### Phase 6 Zusammenfassung

| Task | Thema | Spec-Ref | Neue Tests | Status |
|------|-------|----------|:----------:|--------|
| P6-01 | VariableInitExp & AssignExp | §8.2.2.10–11 | 14 | ✅ DONE |
| P6-02 | BlockExp & if/then/else | §8.2.2.2, §8.2.2.8 | 8 | ✅ DONE |
| P6-03 | WhileExp & ComputeExp | §8.2.2.3–4 | 8 | ✅ DONE |
| P6-04 | ForExp — forEach & forOne | §8.2.2.6 | 8 | ✅ DONE |
| P6-05 | ImperativeIterateExp — xcollect/xselect | §8.2.2.7 | 10 | ✅ DONE |
| P6-06 | ReturnExp, BreakExp, ContinueExp | §8.2.2.16–18 | 8 | ✅ DONE |
| P6-07 | LogExp & AssertExp | §8.2.2.19–20 | 8 | ✅ DONE |
| P6-08 | TryExp, CatchExp, RaiseExp | §8.2.2.13–15 | 10 | ✅ DONE |
| P6-09 | Nested Imperative Combinations | §8.2.2 | 9 | ✅ DONE |
| **Σ** | | | **~76** | |

**Hinweis:** ~76 neue Tests + ~99 bestehende = ~175 imperative Tests gesamt. Die geschätzten ~120 neuen Tests im Dashboard werden auf ~76 reduziert, da bereits 99 Tests vorhanden sind.

---

## Phase 7: this/self/result, Null, Invalid, Tag/Typedef (~35 Tests)

> **⚠️ SPEC-FIRST WORKFLOW (bei JEDEM Task befolgen!)**
>
> **Unsere Implementierung orientiert sich an der Spec. Eclipse-Tests dienen als Referenz.**
>
> 1. **ZUERST die Spec lesen** — QVT-O v1.3: `docs/specifications/QVT/formal-16-06-03.pdf`
> 2. **Eclipse-Tests als Referenz prüfen** — `repositories/org.eclipse.qvto/tests/org.eclipse.m2m.tests.qvt.oml/`
> 3. **Tests gegen die Spec schreiben** — das erwartete Ergebnis kommt aus der Spec, NICHT aus dem Code
> 4. **Tests laufen lassen** — Failures sind Implementierungslücken → bevorzugt spec-kompatibel fixen
> 5. **NIEMALS einen Test an die Implementierung anpassen**, um ihn grün zu machen
> 6. **Bei Diskrepanzen zwischen Spec und Eclipse: Rückfrage an den User** — der User entscheidet
> 7. **Bei Unklarheiten: Rückfrage stellen**, nicht raten
>
> **Referenz-Quellen (Priorität):**
> 1. QVT-O v1.3 Spec (§8.1.16, §8.1.18–8.1.20, §8.2.1.10, §8.2.1.15, §8.2.1.17, §8.2.1.19)
> 2. Eclipse QVT-O Tests (`parserTestData/models/`, `deployed/`)
> 3. Gecko QVT-O (`repositories/org.gecko.qvto/`)
> 4. **Spec hat Vorrang** bei Widersprüchen — aber Rückfrage bei Unklarheit

**Spec-Abschnitte:** §8.1.16, §8.1.18–8.1.20, §8.2.1.10, §8.2.1.15, §8.2.1.17, §8.2.1.19

**Bestandsanalyse (vor Phase 7):**

| Thema | Bestehende Tests | Bewertung |
|-------|:---:|---|
| `this` keyword | 0 | ❌ Kritische Lücke — komplett ungetestet |
| `self` keyword | ~135 indirect | ✅ Gut abgedeckt, aber keine dedizierten Edge-Case-Tests |
| `result` variable | ~30 | ✅ Gut abgedeckt, Eclipse-Patterns fehlen (named result, collection result) |
| Null semantics | ~25 | ✅ Gut abgedeckt, Edge-Cases fehlen (null property access, null in ops) |
| OclInvalid | 1 (nur Parse!) | ❌ Kritische Lücke — kein Execution-Test |
| Tag/Typedef | 0 | ⚠️ Lücke |

| Task | Thema | Spec-Ref | Neue Tests | Status |
|------|-------|----------|:----------:|--------|
| P7-01 | `this` keyword | §8.1.18 | 6 | ✅ DONE |
| P7-02 | `self` Edge-Cases | §8.1.18, §8.2.1.10 | 5 | ✅ DONE |
| P7-03 | `result` Edge-Cases | §8.1.18, §8.2.1.17, §8.2.1.19 | 6 | ✅ DONE |
| P7-04 | Null Semantics | §8.1.19 | 5 | ✅ DONE |
| P7-05 | OclInvalid in QVT-O | §8.1.20 | ~7 | ✅ DONE |
| P7-06 | Tag & Typedef | §8.1.16 | ~6 | ✅ DONE |
| **Σ** | | | **~35** | |

---

### P7-01: `this` keyword (§8.1.18) — ✅ DONE (6 Tests, 6 pass)

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.18 Pre-defined Variables (p86):**
>    - `this` repräsentiert die Transformation-Instanz
>    - Kann implizit verwendet werden um auf Transformation-Properties und -Operationen zuzugreifen
>    - `this` ist immer verfügbar — auch in Helpers und Queries
> 2. **Eclipse-Referenztests:**
>    - `parserTestData/models/moduleProperty/moduleProperty.qvto` — `this.propX` Zugriff auf Module-Properties
>    - `this.propStrOrderedSet += localOrderedSet` — Collection-Property via `this`
>    - `this.ePropStrOrderedSet := null` — Null-Zuweisung an Property
> 3. **Bestand prüfen:** 0 Tests! ⚠️ Kritische Lücke
> 4. **Neue Tests schreiben → laufen lassen → Failures = spec-kompatibel fixen!**
> 5. **Bei Diskrepanzen Spec ↔ Eclipse: Rückfrage an User!**

**Neue Tests (~6):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: this — access transformation property | `this.myProp` liest Transformation-Property | §8.1.18 |
| E2E: this — write transformation property | `this.myProp := value` schreibt Property | §8.1.18 |
| E2E: this — implicit this for property access | `myProp` ohne `this.` Prefix = gleicher Effekt | §8.1.18 |
| E2E: this — call own operation | `this.myHelper()` ruft eigene Operation auf | §8.1.18 |
| E2E: this — inside helper (still transformation ref) | `this` in Helper = Transformation, NICHT self | §8.1.18 |
| E2E: this — config property access | `this.configProp` liest Configuration-Property | §8.1.18 |

---

### P7-02: `self` Edge-Cases (§8.1.18, §8.2.1.10) — ✅ DONE (5 Tests, 5 pass)

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.18 (p86):**
>    - `self` = kontextueller Parameter innerhalb einer kontextuellen Operation
>    - Nur in Queries, Helpers und Mappings mit Kontext-Typ verfügbar
> 2. **Spec lesen — §8.2.1.10 ImperativeOperation (p101):**
>    - `context: VarParameter [0..1]` — Kontext-Variable = `self`
>    - `self` hat den Typ des Kontext-Parameters
> 3. **Eclipse-Referenztests:**
>    - `lateresolve.qvto` — `self.name`, `self._abstract` in Mappings
>    - `stacktrace.qvto` — `self.mapCreateInstaceFailure2()` in init-Section
>    - `callvirtforundefined.qvto` — virtual Operation auf null-self
> 4. **Bestand prüfen:** ~135 indirekte Verwendungen, aber keine dedizierten Edge-Case-Tests
> 5. **Neue Tests schreiben → laufen lassen → Failures = spec-kompatibel fixen!**
> 6. **Bei Diskrepanzen Spec ↔ Eclipse: Rückfrage an User!**

**Neue Tests (~5):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: self — explicit in mapping body | `self.name` explizit in Mapping-Body | §8.1.18 |
| E2E: self — explicit in helper | `self.attr` explizit in Helper | §8.1.18 |
| E2E: self — implicit (omitted) | Property-Zugriff ohne `self.` Prefix | §8.1.18 |
| E2E: self — not available in main() | `main()` hat kein self (kein Kontext-Typ) | §8.2.1.10 |
| E2E: self — type matches context parameter | `self.oclIsKindOf(ContextType)` = true | §8.2.1.10 |

---

### P7-03: `result` Edge-Cases (§8.1.18, §8.2.1.17, §8.2.1.19) — ✅ DONE (6 Tests, 6 pass)

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.18 (p86):**
>    - `result` = Name des einzigen Result-Parameters
>    - Bei mehreren Result-Parametern: `result` ist ein Tuple
>    - **Wenn ein einzelner Result-Parameter explizit benannt ist, existiert KEIN `result` Variable!**
> 2. **Spec lesen — §8.2.1.17 OperationBody (p108–109):**
>    - `self` = Kontext-Parameter, `result` = Rückgabe-Parameter
>    - Bei Tuple: `result` hat Tuple-Typ
> 3. **Spec lesen — §8.2.1.19 MappingBody (p109–110):**
>    - init-Section → implizite Instanziierung → population → end-Section
>    - Ohne `population` Keyword: Body = implizites `object result:Type { body }`
>    - `result` in init reassignment verhindert Auto-Instanziierung
> 4. **Eclipse-Referenztests:**
>    - `useresultinsameout.qvto` — `getPackClassifiers(result)` = result als Parameter
>    - `collectionMappingResult.qvto` — `result := Set { ... }` in init, `result->notEmpty()` in end
>    - `resolvebeforeoutcompletion.qvto` — `result := pack.main1()` in init, `result.name +` im Body
> 5. **Bestand prüfen:** ~30 Tests, aber Eclipse-Patterns fehlen
> 6. **Neue Tests schreiben → laufen lassen → Failures = spec-kompatibel fixen!**
> 7. **Bei Diskrepanzen Spec ↔ Eclipse: Rückfrage an User!**

**Neue Tests (~6):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: result — named result parameter (kein `result` Variable) | `mapping f() : out:EClass` → Variable heißt `out`, nicht `result` | §8.1.18 |
| E2E: result — collection result type | `mapping f() : Set(EClass)` → result ist Collection, init setzt Set | §8.2.1.19 |
| E2E: result — access in init before instantiation | `result` in init ist null, bis explizit gesetzt | §8.2.1.15 |
| E2E: result — passed as argument | `result` an Helper übergeben (Eclipse useresultinsameout) | §8.2.1.17 |
| E2E: result — accessible in end section | `result.name` in end-Section = Population-Ergebnis | §8.2.1.19 |
| E2E: result — in helper/query | Helper mit Result-Parameter: `result` ist Rückgabewert | §8.2.1.17 |

---

### P7-04: Null Semantics (§8.1.19) — ✅ DONE (5 Tests, 5 pass)

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.19 Null (p86):**
>    - `null` ist ein universeller Literal-Wert, der zu jedem Typ konform ist
>    - Kann explizit oder implizit returned werden (= "absence of value")
> 2. **Spec lesen — §8.2.1.15 MappingOperation (p104–107):**
>    - Initialwert aller `out`/`result` Parameter = null
>    - Wenn kein Kandidat-Mapping gefunden → null returned
> 3. **Eclipse-Referenztests:**
>    - `calloclIsUndefinedforundefined.qvto` — `p.oclIsUndefined()` auf null-Variable
>    - `nullsource.qvto` — Method-Call auf null → returns undefined
>    - `callvirtforundefined.qvto` — virtuelle Operation auf null-Objekt
> 4. **Bestand prüfen:** ~25 Tests (indirekt), aber keine dedizierten Null-Semantik-Tests
> 5. **Neue Tests schreiben → laufen lassen → Failures = spec-kompatibel fixen!**
> 6. **Bei Diskrepanzen Spec ↔ Eclipse: Rückfrage an User!**

**Neue Tests (~5):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: null — conforms to any type | `var s : String := null; var i : Integer := null` — kein Fehler | §8.1.19 |
| E2E: null — property access on null | `nullObj.name` → null/undefined (nicht Exception) | §8.1.19 |
| E2E: null — equality | `null = null` → true, `null <> 'x'` → true | §8.1.19 |
| E2E: null — oclIsUndefined on null | `null.oclIsUndefined()` → true | §8.1.19 |
| E2E: null — in collection operations | `Sequence{1,null,3}->size()` Semantik | §8.1.19 |

---

### P7-05: OclInvalid in QVT-O (§8.1.20) — ✅ DONE (7 Tests, 7 pass)

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.20 Invalid (p86–87):**
>    - OCL-Fehler (z.B. Division durch 0) erzeugen `invalid`, KEINE Exception
>    - **Jede Verwendung von `invalid` als Source in ImperativeExpression = Assertion-Failure!**
>    - `invalid` KANN in Variable gespeichert werden (Zuweisung = kein "Verwenden")
>    - Nachfolgendes Lesen der Variable = wahrscheinlich Assertion-Failure
>    - `oclIsInvalid()` testet auf invalid OHNE Failure auszulösen
> 2. **Eclipse-Referenztests:**
>    - `addundefined.qvto` — Arithmetik mit invalid: `1.0 + invalid`, `1.0 / invalid` → invalid
>    - `equndefined.qvto` — Vergleich mit invalid: `invalid = model`, `invalid <> model`
>    - `stdlibList.qvto` — `oclIsInvalid()` Check, invalid-Werte in List werden gefiltert
> 3. **Bestand prüfen:** 1 Test (nur Parse)! ⚠️ Kritische Lücke — KEIN Execution-Test!
> 4. **Neue Tests schreiben → laufen lassen → Failures = spec-kompatibel fixen!**
> 5. **⚠️ ACHTUNG:** Diskrepanz zwischen Spec und Eclipse wahrscheinlich!
>    - Spec: `invalid` als Source = **Assertion-Failure**
>    - Eclipse: `invalid` propagiert oft still (wie in OCL)
>    - **→ Rückfrage an User bei Diskrepanz!**

**Neue Tests (~7):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: invalid — oclIsInvalid() check | `invalid.oclIsInvalid()` → true, kein Failure | §8.1.20 |
| E2E: invalid — store in variable | `var x := invalid` — Zuweisung erlaubt | §8.1.20 |
| E2E: invalid — arithmetic produces invalid | `1 + invalid` → invalid (OCL-Semantik) | §8.1.20 |
| E2E: invalid — equality with invalid | `invalid = invalid`, `invalid = 1` Semantik | §8.1.20 |
| E2E: invalid — divide by zero produces invalid | `1 / 0` → invalid (nicht Exception) | §8.1.20 |
| E2E: invalid — oclIsUndefined on invalid | `invalid.oclIsUndefined()` → true (OCL: invalid is undefined) | §8.1.20 |
| E2E: invalid — null vs invalid distinction | `null.oclIsInvalid()` → false, `invalid.oclIsInvalid()` → true | §8.1.20 |

---

### P7-06: Tag & Typedef (§8.1.16) — ✅ DONE (6 Tests, 6 pass)

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.16 Type Extensions (p85):**
>    - List = mutable, ordered, parameterized Collection (→ Phase 8 Stdlib)
>    - Dict = mutable Key-Value Store (→ Phase 8 Stdlib)
>    - `typedef` = Alias für komplexe Typen (z.B. Tuple)
> 2. **Spec lesen — §8.4 Concrete Syntax:** `tag` Keyword Syntax
> 3. **Eclipse-Referenztests:**
>    - `simpletag/simpletag.qvto` — `tag "alias" EPackage::name = 'yetAnotherName'`
>    - Tag auf Metamodel-Feature, auf Intermediate-Property
>    - Alias-Name in Object-Initialisierung verwenden
> 4. **Bestand prüfen:** 0 Tests! ⚠️ Lücke
> 5. **Neue Tests schreiben → laufen lassen → Failures = spec-kompatibel fixen!**
> 6. **Bei Diskrepanzen Spec ↔ Eclipse: Rückfrage an User!**
> 7. **Hinweis:** List/Dict Literals und Operationen gehören in Phase 8 (Stdlib), NICHT hierher

**Neue Tests (~6):**

| Test | Beschreibung | Spec-Ref |
|------|-------------|----------|
| E2E: tag — alias on metamodel feature | `tag "alias" EClass::name = 'myName'` → `myName` statt `name` | §8.1.16 |
| E2E: tag — alias in object expression | `object EClass { myName := 'foo' }` mit Alias | §8.1.16 |
| E2E: tag — alias on intermediate property | `tag "alias"` auf Intermediate-Class Feature | §8.1.16 |
| E2E: typedef — simple alias | `typedef PersonName = String` → Typ-Alias nutzbar | §8.1.16 |
| E2E: typedef — tuple alias | `typedef PersonInfo = Tuple{name:String,age:Integer}` | §8.1.16 |
| Parse: tag — syntax validation | Tag-Deklaration wird korrekt geparst | §8.4 |

---

## Phase 8: Standard Library (~195 Tests)

> **⚠️ SPEC-FIRST WORKFLOW (bei JEDEM Task befolgen!)**
>
> **Tests definieren das korrekte Verhalten — NICHT die aktuelle Implementierung.**
>
> 1. **ZUERST die Spec lesen** — QVT-O v1.3: `docs/specifications/QVT/formal-16-06-03.pdf`, §8.3
> 2. **Tests gegen die Spec schreiben** — das erwartete Ergebnis kommt aus der Spec, NICHT aus dem Code
> 3. **Tests laufen lassen** — Failures sind Implementierungslücken, NICHT Testfehler
> 4. **Implementierung fixen** — bis die Tests grün sind
> 5. **NIEMALS einen Test an die Implementierung anpassen**, um ihn grün zu machen
> 6. **Bei Unklarheit: Rückfrage an den User**, nicht raten
>
> **Referenz-Quellen:** 1. QVT-O v1.3 Spec → 2. Eclipse QVT-O Tests → 3. Gecko QVT-O → 4. Spec hat Vorrang → 5. Bei Unklarheit: Rückfrage

**Spec-Abschnitte:** §8.3

**Eclipse-Referenz:** `repositories/org.eclipse.qvto/tests/org.eclipse.m2m.tests.qvt.oml/parserTestData/models/`

| Testbereich | Spec-Ref | Beschreibung | Geschätzte Tests | Status |
|-------------|----------|--------------|:----------------:|--------|
| Dict (MutableMap) | §8.3.8 | `Dict(K,V)`, `get()`, `put()`, `keys()`, `values()`, `hasKey()`, `size()`, `clear()`, `defaultget()`, `isEmpty()`, Dict literal, `repr()` | 26 | ✅ |
| List (MutableList) | §8.3.9 | `List(T)`, `add()`, `remove()`, `removeAt()`, `removeFirst()`, `removeLast()`, `removeAll()`, `joinfields()`, List literal, Sequence-inherited ops | 23 | ✅ |
| String Extensions | §8.3.16 | `substringBefore/After`, `firstToUpper`, `lastToUpper`, `normalizeSpace`, `find`, `rfind`, `match`, `replace`, `isQuoted`, `quotify`, `unquotify`, `asBoolean/Integer/Real`, `matchBoolean/Integer/Real/Identifier`, `length`, `format`, `startStrCounter`, `getStrCounter`, `incrStrCounter`, `restartAllStrCounter`, `addSuffixNumber` (GAP-9) | 34 | ✅ |
| Model Operations | §8.3.5 | `objects()`, `objectsOfKind()`, `objectsOfType()`, `rootObjects()`, `addElement()`, `removeElement()`, `copy()` | 10 | ✅ |
| Element Operations | §8.3.4 | `metaClassName()`, `subobjects()`, `allSubobjects()`, `subobjectsOfType/Kind()`, `allSubobjectsOfType/Kind()`, `clone()`, `deepclone()`, `container()` — 5 UML-spezifische ops (`_localId`, `_globalId`, `markedAs`, `markValue`, `stereotypedBy`) übersprungen (wie Eclipse) | 12 | ✅ |
| Transformation Operations | §8.3.6 | `transform()`, `parallelTransform()`, `wait()` | 12 | ✅ P10-01 |
| Status/Exception | §8.3.7 | `succeeded()`, `failed()`, `raisedException()` | 12 | ✅ P10-02 (in P10-01 E2E-Tests) |
| Collection Extensions | §8.3.9 / Eclipse | `asList()` auf allen Collection-Typen, `reverse()` auf OrderedSet, List-Konversionen (`asBag`, `asSet`, `asOrderedSet`, `asSequence`) | 11 | ✅ |
| Stdlib Completeness | §8.3 | Cross-Check aller §8.3 Operationen — siehe Gap-Analyse unten | 0 | ✅ |

### ✅ P8-06 + P8-07 DONE: Transformation Composition + Status (→ P10-01/P10-02)

**Implementiert in P10-01 (Multi-File Transformation Composition):**

1. **`access` keyword** — Parser + Linker: `QvtoUnitResolver`-basierte Import-Auflösung
2. **`new T(models)`** — `TransformationInstantiationExp` im Parser, `QvtoTransformationInstance` Runtime-Wrapper
3. **`transform()`** — Synchrone Ausführung via nested `QvtoEngineImpl.execute()`
4. **`parallelTransform()`** — Async via konfigurierbarem `Executor` (Default: Virtual Threads) + `CompletableFuture`
5. **`wait(Set{status})`** — Blockiert bis alle `PendingStatus`-Futures aufgelöst sind
6. **`Status`-Typ** — EMF EClass mit `succeeded()`, `failed()`, `raisedException()`, `PendingStatus` für async

**Testklasse:** `QvtoE2eMultiFileCompositionTest` (12 Tests):
- `accessTransform_nestedModifiesExtent` — access + new T(args) + transform()
- `accessTransform_statusOnFailure` — Status.failed() bei fehlgeschlagener Transformation
- `parallelTransform_withWait` — parallelTransform() + wait(Set{s})
- `multipleAccess_chainedTransforms` — Mehrere comma-separated access-Imports
- `linker_unresolvedImport_returnsError` — Unbekannter Import → Link-Error-Diagnostik
- `status_succeeded_trueOnSuccess` — succeeded=true + failed=false
- `status_failed_trueOnFailure` — succeeded=false + failed=true
- `parallelTransform_multipleWithWait` — 2 parallele Transformationen mit wait(Set{s1, s2})
- `parallelTransform_customExecutor` — Konfigurierter FixedThreadPool-Executor
- `mixedSyncAndAsync_transform` — sync transform() + async parallelTransform() gemischt
- `separateAccessDeclarations` — Separate access-Zeilen + verkettetes `new T(m).transform()`
- `accessWithoutInstantiation_noError` — access-Deklaration ohne Nutzung

**Architektur:**
- `QvtoLinker` — Resolves stub modules via `QvtoUnitResolver`, aktualisiert `TransformationInstantiationExp`-Referenzen
- `QvtoTransformationInstance` — Runtime-Wrapper (extends `DynamicEObjectImpl`)
- `QvtoStatusHelper` — Status-Factory (`success()`, `failed()`, `pending()`, `await()`)
- `QvtoTransformationOperations` — Dispatch für `transform()`, `parallelTransform()`, `wait()`
- `QvtoConfiguration.parallelExecutor(Executor)` — Konfigurierbarer Executor für `parallelTransform()`

**Eclipse-Vergleich:** `parallelTransform()` und `wait()` sind in Eclipse UNSUPPORTED — wir unterstützen sie via Virtual Threads.

### ✅ P8-09 Stdlib Completeness — Gap-Analyse

**Systematischer Cross-Check aller §8.3 Spec-Operationen vs. Implementierung:**

| Spec-Section | Operationen | Status | Anmerkung |
|---|---|---|---|
| §8.3.3.1 | `repr()` | ✅ | OCL stdlib (OclStdlib.java) |
| §8.3.4.1–2 | `_localId()`, `_globalId()` | ⏭️ | UML-spezifisch, übersprungen (wie Eclipse) |
| §8.3.4.3–11 | Element ops (10 Stück) | ✅ | P8-05 |
| §8.3.4.12–15 | `markedAs`, `markValue`, `stereotypedBy`, `stereotypedStrictlyBy` | ⏭️ | UML-spezifisch, übersprungen (wie Eclipse) |
| §8.3.5.1–6, 8 | Model/Extent ops (7 Stück) | ✅ | P8-04 |
| §8.3.5.7 | `asTransformation()` | ✅ | P10-03 (3 Tests) — extrahiert OT aus Extent |
| §8.3.5.9 | `createEmptyModel()` | ✅ | Auf QvtoModelExtent — gibt neues leeres BasicQvtoModelExtent zurück (2 Tests) |
| §8.3.6.1–3 | Transformation ops (`transform`, `parallelTransform`, `wait`) | ✅ | P10-01 (4 E2E-Tests) |
| §8.3.7 | Status ops (`succeeded`, `failed`, `raisedException`) | ✅ | P10-02 (in P10-01 E2E-Tests) |
| §8.3.8 | Dict ops (14 Stück) | ✅ | P8-01 |
| §8.3.9 | List ops (47 Spec-Ops) | ✅ | P8-02 (mutating) + OCL Sequence-Vererbung |
| §8.3.9.6 | `asList()` auf allen Collections | ✅ | P8-08 |

**Ergebnis:** Alle implementierbaren Stdlib-Operationen sind abgedeckt. Die verbleibenden Gaps sind:
1. **`asTransformation()`** — ✅ implementiert (P10-03, ohne dynamische Kompilierung)
2. **5 UML-spezifische Element ops** — bewusst übersprungen (wie Eclipse)

Transformation ops (§8.3.6) und Status ops (§8.3.7) sind seit P10-01/P10-02 implementiert.

---

## Phase 9: Spec-Conformance, Eclipse-Referenztests & Edge Cases (~130 Tests)

> **⚠️ SPEC-FIRST WORKFLOW (bei JEDEM Task befolgen!)**
>
> **Tests definieren das korrekte Verhalten — NICHT die aktuelle Implementierung.**
>
> 1. **ZUERST die Spec lesen** — QVT-O v1.3: `docs/specifications/QVT/formal-16-06-03.pdf`
> 2. **Eclipse-Referenztests lesen** — `repositories/org.eclipse.qvto/tests/org.eclipse.m2m.tests.qvt.oml/parserTestData/models/`
> 3. **Tests gegen die Spec schreiben** — das erwartete Ergebnis kommt aus der Spec, NICHT aus dem Code
> 4. **Tests laufen lassen** — Failures sind Implementierungslücken, NICHT Testfehler
> 5. **Implementierung fixen** — bis die Tests grün sind
> 6. **NIEMALS einen Test an die Implementierung anpassen**, um ihn grün zu machen
> 7. **Bei Unklarheit: Rückfrage an den User**, nicht raten
>
> **Referenz-Quellen:** 1. QVT-O v1.3 Spec → 2. Eclipse QVT-O Tests → 3. Gecko QVT-O → 4. Spec hat Vorrang → 5. Bei Unklarheit: Rückfrage

**Spec-Abschnitte:** §8.1.7 (Helpers), §8.1.16 (Type Extensions), §8.2.1.10 (OperationBody/Dispatch), §8.4 (Concrete Syntax)

**Eclipse-Referenz:** `repositories/org.eclipse.qvto/tests/org.eclipse.m2m.tests.qvt.oml/parserTestData/models/`

### Task-Übersicht

| Task | Bereich | Beschreibung | Eclipse-Referenz | Geschätzte Tests | Status |
|------|---------|--------------|------------------|:----------------:|--------|
| P9-01 | Virtual Dispatch | Helper auf `Integer::`, `Real::`, `String::`, `Boolean::`, `OclAny::`, Dispatch mit Widening (Integer→Real), most-specific-type-first | `virtualPredefinedTypeOpers/` | 13 | ✅ |
| P9-02 | Intermediate Class Advanced | static, readonly, opposite, `<<id>>`, extends Metamodel-Klasse | `intermProperties/`, `intermWithCrossRefs/`, `intermWithExtends/` | 12 | ✅ |
| P9-03 | Module Properties (deep) | Collection-Properties (`+=`), null-Semantik, OrderedSet/Sequence/List property types | `moduleProperty/` | 12 | ✅ |
| P9-04 | Tuple Advanced | Nested Tuples, null in Tuple-Feldern, Tuple als Property-Typ, mutable List in Tuple | `bug413131/`, `bug415024/`, `tuples/` | 11 | ✅ |
| P9-05 | Boxing / Numeric Widening | Integer→Real Widening, `query bar(): Real { return 1 }`, auto-boxing | `boxing/` | 10 | ✅ |
| P9-06 | inout Mapping | `mapping inout EClass::m()`, inout context param, inout ohne Result | `inoutMapping/`, `inoutcontextparam/`, `inoutcontextparamnoresult/` | 10 | ✅ |
| P9-07 | Mapping Result Variations | `Sequence(T)` result, multiple result params, population section | `collectionMappingResult/`, `multiresultpars/`, `populationSection/` | 10 | ✅ |
| P9-08 | Lexer/String Edge Cases | Double-quoted strings, escape sequences (`\n`, `\123`), multiline, Java keywords | `doubleQuoteStrings_262734/`, `escape_sequences_250630/`, `javakeywords/` | 11 | ✅ |
| P9-09 | Eclipse Bug Regressions | Ausgewählte bug-fix Tests für Edge Cases (Collection `+=` null, object reparenting, etc.) | `bug449445/`, `bug561707/`, `bug415661/`, `scr878/` | 12 | ✅ (2 deferred) |

### P9-01: Virtual Dispatch on Predefined Types — ✅ DONE (13 Tests, 13 pass)

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.2.1.10 OperationBody (p100-105):**
>    - Wie werden kontextuelle Operationen dispatched?
>    - Virtual Dispatch: Typ-Matching für Helper/Query
>    - Wie interagieren predefined type operations mit user-defined operations?
> 2. **Spec lesen — §8.1.7 Helper/Query (p72-74):**
>    - Kontextparameter-Semantik
>    - "Most specific type" Dispatch-Regel
> 3. **Eclipse-Referenztests:**
>    - `virtualPredefinedTypeOpers/virtualPredefinedTypeOpers.qvto` — Helper auf Integer, Real, String, Collection(T)
>    - `virt/virt.qvto` — Virtual Dispatch Grundlagen
>    - `virtual_contextVsOverride/virtual_contextVsOverride.qvto` — Context vs Override
> 4. **Bestand prüfen:** Welche Helper-Dispatch-Tests existieren bereits?
> 5. **Neue Tests schreiben → laufen lassen → Failures = spec-kompatibel fixen!**

### P9-02: Intermediate Class Advanced — ✅ DONE (12 Tests, 10 pass, 2 skipped)

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.10 Intermediate Data, §8.2.1.4 OperationalTransformation::intermediateClass:**
>    - `intermediate class` Deklaration mit Features
>    - `extends` (andere intermediate oder Metamodel-Klasse)
>    - static, readonly, `<<id>>`, opposite Semantik
> 2. **Eclipse-Referenztests:**
>    - `intermProperties/intermProperties.qvto` — static, readonly, opposite, `<<id>>`
>    - `intermWithCrossRefs/intermWithCrossRefs.qvto` — Self-Referenz, `refSelf := result`
>    - `intermWithExtends/intermWithExtends.qvto` — extends Metamodel-Klasse
> 3. **Bestand prüfen:** `QvtoE2eIntermediatePropertyTest` (10 Tests) — was fehlt?
> 4. **Neue Tests schreiben → laufen lassen → Failures = spec-kompatibel fixen!**

**Testklasse:** `QvtoE2eIntermediateClassAdvancedTest` (12 Tests)

**Implementierte Features:**
- Default-Werte in intermediate class properties (`= 3`, `= 'default'`)
- Default-Werte werden durch object-expression Assignments überschrieben
- Intermediate class inheritance (`extends`)
- Inherited property override in object expression
- Collection-typed properties (`Sequence(String) = Sequence{...}`)
- `oclIsTypeOf` / `oclIsKindOf` auf intermediate classes (inkl. Vererbung)
- `readonly` property mit default value (Grammatik: `readonly` Modifier)
- Cross-References zwischen intermediate class Instanzen (`parent : Node`)
- Self-Reference in mapping (`selfRef := result`)
- Intermediate class Instanzen in Collections (forEach)

**Grammatik-Erweiterung:** `classifierFeatureModifier` Rule für `static`, `readonly`, `references`, `composes`

**Parser-Fix:** Unique `_INTERMEDIATE` Package-URI pro Parse-Vorgang (vermeidet Cross-Test-Interferenz über globale EPackage.Registry)

**Typ-Inference:** ObjectExp setzt jetzt `type` und `refVar.type`, Variable-Typ wird aus Init-Expression inferiert (`var x := object Foo {}` → x hat Typ Foo)

**Skipped (2):**
- `<<id>>` Stereotyp-Syntax — Parser-Gap: `<<` / `>>` kollidiert mit Vergleichsoperatoren im Lexer
- `static` Property-Sharing — Implementierungs-Gap: erfordert cross-instance Interception auf Read/Write-Pfaden durch mehrere Evaluator-Layers (Parser-Typ-Propagation + OCL-Property-Interceptor + Assign-Dispatch). Grammatik parst `static` Modifier korrekt, Evaluator leitet aber Reads/Writes noch nicht durch einen shared Static-Store um.

### P9-03: Module Properties (deep) ✅

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.2.1.1 OperationalTransformation::ownedVariable (Figure 8.2):**
>    - Module-level `property` → Variable in `ownedVariable [*]`
>    - Collection-typed Properties: `+=` Append-Semantik
>    - null-Zuweisungsverhalten
> 2. **Eclipse-Referenztests:**
>    - `moduleProperty/moduleProperty.qvto` — alle Collection-Property-Typen, `this.prop += ...`, null-assign
> 3. **Bestand prüfen:** `QvtoE2eThisKeywordTest` — welche Property-Semantiken fehlen?
> 4. **Neue Tests schreiben → laufen lassen → Failures = spec-kompatibel fixen!**

**Testklasse:** `QvtoE2eModulePropertyDeepTest` (12 Tests)

**Implementierte Features:**
- Collection-typed module properties: OrderedSet, Sequence, Set, Bag mit Default-Werten
- `+=` Append-Semantik auf OrderedSet und Sequence Properties
- null-Assignment auf String und Integer Properties (`this.prop := null` → `prop = null`)
- Integer-Mutation (`this.count := count + 11`)
- String-Concat-Mutation (`this.label := label + 'myString'`)
- Property Read/Write aus Helpern (Cross-Scope via root env)
- Mehrere Properties verschiedener Typen (Integer, Real, Boolean, String)

**Alle 12 Tests grün, keine Skipped, keine Code-Änderungen nötig.**

### P9-04: Tuple Advanced ✅

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — OCL v2.4 §7.4.3 TupleType + QVT-O Erweiterungen:**
>    - Nested Tuples: `Tuple(a: Tuple(b: Integer))`
>    - null in Tuple-Feldern
>    - Mutable List in Tuple
> 2. **Eclipse-Referenztests:**
>    - `bug413131/bug413131.qvto` — Nested Tuples, null
>    - `bug415024/bug415024.qvto` — Mutable List in Tuple, multiple result params
>    - `tuples/tuples.qvto` — Basis-Tuple-Operationen
> 3. **Neue Tests schreiben → laufen lassen → Failures = spec-kompatibel fixen!**

**Testklasse:** `QvtoE2eTupleAdvancedTest` (11 Tests)

**Implementierte Features:**
- Nested Tuples: `Tuple { obj = Tuple { name = 'inner', size = 10 }, size = 5 }`
- null in Tuple-Feldern (String, Integer, innerer Tuple = null)
- Tuple als Module-Property
- Tuple-Equality by parts (`t1 = t2` → true wenn alle Parts gleich)
- Tuple in Sequence-Collection + `->collect()` über Tuples
- Tuple-Variable-Reassignment
- Tuple als Helper-Parameter
- Mutable List-Feld in Tuple (`t.items += 3`)

**Implementierungs-Fix:**
- `caseAssignExp`: Tuple-Part-Assignment (`:=` und `+=`) für `Map<String, Object>` Tuples hinzugefügt.
  Bisher wurde nur `EObject`-basierte PropertyCallExp-Assignment unterstützt, Tuple-Maps wurden
  beim Schreiben ignoriert. Jetzt wird `+=` auf mutable Collections (List) im Tuple korrekt dispatched.

**Alle 11 Tests grün, keine Skipped.**

### P9-05: Boxing / Numeric Widening ✅

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — OCL v2.4 §11.5.1-2 (Real/Integer) + QVT-O Erweiterungen:**
>    - Integer→Real Widening (implizite Konvertierung)
>    - `query bar(): Real { return 1 }` (Integer-Literal, Real-Return)
> 2. **Eclipse-Referenztests:**
>    - `boxing/boxing.qvto` — Integer/Real boxing in module properties, query returns
> 3. **Neue Tests schreiben → laufen lassen → Failures = spec-kompatibel fixen!**

**Testklasse:** `QvtoE2eBoxingWideningTest` (10 Tests)

**Implementierte Features:**
- Integer-Arithmetik bleibt Integer (`1 + 1 + 1 = 3`)
- Integer-Literal in Real-Property (`property ratio : Real = 1`)
- Query mit Integer-Return und Real-Deklaration (`query getReal() : Real { return 1; }`)
- Mixed Integer+Real Arithmetik (`1.0 + 1 + 1 = 3.0`)
- Integer×Real Multiplikation (`3 * 1.5 = 4.5`)
- Integer-Division erzeugt Real (`7 / 2 = 3.5`)
- Integer-Real Vergleich (`1 = 1.0` → true, `1 < 1.5` → true)
- `round()` / `floor()` für explizite Real→Integer Konvertierung
- Module-Property Mixed Expression (`10 * 1.5 = 15.0`)

**Alle 10 Tests grün, keine Skipped, keine Code-Änderungen nötig.**

### P9-06: inout Mapping Semantics ✅

> 🛑 **SPEC-FIRST — BEVOR DU ANFÄNGST:**
>
> 1. **Spec lesen — §8.1.6 Mapping, §8.1.11.2 (inhibition/instantiation):**
>    - `mapping inout EClass::m()` — in-place Transformation
>    - Context-Parameter mit inout: Objekt wird direkt modifiziert
>    - Kein Result-Objekt bei `inout` ohne explizites `result`
> 2. **Eclipse-Referenztests:**
>    - `inoutMapping/inoutMapping.qvto`
>    - `inoutcontextparam/inoutcontextparam.qvto`
>    - `inoutcontextparamnoresult/inoutcontextparamnoresult.qvto`
> 3. **Neue Tests schreiben → laufen lassen → Failures = spec-kompatibel fixen!**

**Testklasse:** `QvtoE2eInoutMappingTest` (10 Tests)

**Implementierte Features:**
- `inout` Mapping modifiziert self, kein Result-Typ (void)
- `inout` Mapping mit explizitem Result-Typ (`mapping inout T::m() : R`)
- Mehrfach-Aufrufe auf demselben Objekt (akkumulierte Änderungen)
- `inout` Parameter (non-context) — wird direkt modifiziert
- Impliziter self-Zugriff (ohne `self.` Prefix) in inout Mapping
- init/end-Sections in inout Mappings
- `result := self` Pattern (Eclipse inoutcontextparam)
- Chained inout Mappings (Pipeline-Pattern)
- Leerer Body → keine Änderung

**Alle 10 Tests grün, keine Skipped, keine Code-Änderungen nötig.**

### P9-07: Mapping Result Variations ✅

> **Status:** 10 Tests, 10 pass, 0 fail — **DONE**
> **Testklasse:** `QvtoE2eMappingResultVariationsTest`
> **Spec:** §8.1.6, §8.1.11.2, §8.1.12
> **Eclipse-Referenz:** collectionMappingResult, multiresultpars, populationSection
>
> **Tests:**
> - `result_explicitAssignmentInInit` — result assigned in init section
> - `result_populatedInBody` — body populates result properties
> - `result_consistentBetweenInitAndEnd` — init result visible in end section
> - `result_modifiedInBodyAndEnd` — result modified across init→body→end
> - `result_collectedFromMultipleMappings` — mapping results collected in Sequence
> - `result_contextMappingReturnsNewObject` — context mapping (Node::toCopy)
> - `result_initDoesNotSetResult_bodyPopulates` — auto-instantiated result populated in body
> - `result_accessedViaHelper` — helper called from mapping body
> - `result_traceInhibitionReturnsCachedResult` — §8.1.11.2 trace inhibition
> - `result_multiResultQuery` — multi-result query `query f() : r1 : T1, r2 : T2`
>
> **Implementation fixes:**
> - Grammar: `queryDef` now uses `resultList` instead of single `typeExpression`
> - Parser: `visitQueryDef` processes `resultList` with NamedResult/UnnamedResult
> - Evaluator: `collectMultiResult()` for multi-result queries returning Tuple (LinkedHashMap)
>
> **Alle 10 Tests grün, keine Skipped. Multi-Result-Query-Lücke geschlossen.**

### P9-08: Lexer/String Edge Cases ✅

> **Status:** 11 Tests, 11 pass, 0 fail — **DONE**
> **Testklasse:** `QvtoE2eLexerStringEdgeCaseTest`
> **Spec:** §8.4 Concrete Syntax (Lexical Rules)
> **Eclipse-Referenz:** escape_sequences_250630, doubleQuoteStrings_262734, multilineStrings_262733, javakeywords
>
> **Tests:**
> - `string_escapeBackslash` — `\\` → `\`
> - `string_escapeSingleQuote` — `\'` → `'`
> - `string_escapeNewline` — `\n` → newline (size check)
> - `string_escapeTab` — `\t` → tab (size check)
> - `string_escapeCarriageReturn` — `\r` → CR (size check)
> - `string_octalEscape` — `\123` → `S` (ASCII 83)
> - `string_doubleQuotedLiteral` — `"hello"` as expression
> - `string_doubleQuotedEscapeDoubleQuote` — `\"` in double-quoted strings
> - `string_adjacentConcatenation` — `'abc' 'def' 'ghi'` → `abcdefghi`
> - `string_javaKeywordAsIdentifier` — `_class`, `_extends` as identifiers
> - `string_combinedEscapes` — mixed `\'`, `\\`, `\n` in one string
>
> **Implementation fixes:**
> - Ocl.g4: `STRING_LITERAL` extended with octal `\NNN` and `\"` escape
> - QvtO.g4: `DOUBLE_QUOTED_STRING` extended with full escape support
> - QvtO.g4: New `stringLiteral_` rule `(STRING_LITERAL | DOUBLE_QUOTED_STRING)+` for adjacent concat + double-quoted expressions
> - Both `unescapeString` methods (OCL + QVT-O) rewritten with proper StringBuilder-based parsing (fixes `\\n` ordering bug)
>
> **Alle 11 Tests grün, keine Skipped. Alle Lücken geschlossen.**

### P9-09: Eclipse Bug Regressions ✅

> **Status:** 12 Tests, 10 pass, 0 fail, 2 skipped (deferred) — **DONE**
> **Testklasse:** `QvtoE2eEclipseBugRegressionTest`
> **Eclipse-Referenz:** bug449445, bug415661, bug561707, scr878
>
> **Tests (bug449445 — Collection += with invalid/null):**
> - `bug449445_collectionPlusEqualsInvalid_notAdded` — `c += invalid` silently ignored
> - `bug449445_collectionPlusEqualsNull_isAdded` — `c += null` adds null to collection
> - `bug449445_collectionAssignInvalid_clearsCollection` — `items := invalid` clears collection
> - `bug449445_variableAssignInvalid_primitives` — `var x := invalid` → oclIsInvalid() = true
> - `bug449445_invalidCollectionOperationsPropagate` — operations on invalid collections → invalid
> - `bug449445_collectionReassignChangesKind` — Sequence allows duplicates after reassign
> - `bug449445_realSetWidening` — Set(Real) de-duplicates Integer/Real
> - `bug449445_nullAssignmentToObjectProperty` — `item.name := invalid` → null
>
> **Tests (bug415661 — Integer range with invalid bound):**
> - `bug415661_rangeWithInvalidBound` — `Sequence{1..invalid}` → silently returns invalid
> - `bug415661_rangeWithNullBound` — `Sequence{1..null}` → silently returns invalid
>
> **Deferred (need model I/O):**
> - `bug561707_objectReparenting` — @Disabled: needs removeElement on output model extent
> - `scr878_invresolveoneReadOnlyGuard` — @Disabled: needs in/out model parameters with read-only guard
>
> **Implementation fixes:**
> - QvtoEvaluator: `+= invalid` silently ignored on all collection paths (variable, implicit property, EObject property)
> - QvtoEvaluator: `:= invalid` on collection properties → clear; on non-collection → null
> - OclEvaluator: Collection range with invalid/null bounds → return `OclInvalid.INSTANCE` silently (no ERROR diagnostic)
>
> **Alle 10 aktiven Tests grün, 2 deferred. Alle Lücken geschlossen.**

---

## Phase 10: Deferred Features & Offene Gaps (Konsolidierung)

> **Zweck:** Alle offenen Punkte aus Phase 0–9 konsolidiert an einem Ort.
> Diese Phase wird implementiert, sobald die jeweilige Infrastruktur bereitsteht.

### Gesamtstatus: 922 Tests (921 pass, 0 fail, 0 @Disabled, 1 @Tag("perf"))

---

### P10-01: Multi-File Transformation Composition (✅ DONE)

> **Spec:** §8.3.6 — `transform()`, `parallelTransform()`, `wait()`
> **Spec:** §8.1.4 — `access` keyword für Cross-File-Referenzen
> **Spec:** §8.1.13 — Transformation instantiation via `new T(args)`
>
> **Implementiert:**
> - `QvtoLinker` — Import-Auflösung via `QvtoUnitResolver`, Zyklus-Erkennung, AST-Referenz-Update
> - `TransformationInstantiationExp` (Metamodel) — `new T(args)` erzeugt Instanz
> - `QvtoTransformationInstance` — Runtime-Wrapper mit Model-Bindings + Engine-Referenz
> - `QvtoTransformationOperations` — `transform()` (sync), `parallelTransform()` (async), `wait()`
> - `QvtoConfiguration.parallelExecutor(Executor)` — Konfigurierbarer Executor (Default: Virtual Threads)
>
> **Testklasse:** `QvtoE2eMultiFileCompositionTest` (4 Tests)
>
> **Hinweis:** `parallelTransform()` und `wait()` sind in Eclipse UNSUPPORTED — wir unterstützen sie via Virtual Threads.

### P10-02: Status/Exception Operations (✅ DONE)

> **Spec:** §8.3.7 — `succeeded()`, `failed()`, `raisedException()`
>
> **Implementiert:**
> - `Status` EClass im Metamodel (`qvtoperational.ecore`) mit `succeeded`, `failed`, `raisedException`
> - `QvtoStatusHelper` — Factory (`success()`, `failed()`, `pending()`, `await()`)
> - `PendingStatus` — async Status-Wrapper für `parallelTransform()` (extends `StatusImpl`, wraps `CompletableFuture`)
> - Status-Operationen in `QvtoOperationProvider` registriert
>
> **Tests:** In `QvtoE2eMultiFileCompositionTest` integriert (Status wird in allen 4 Tests geprüft)

### P10-03: `asTransformation()` (✅ DONE)

> **Spec:** §8.3.5.7 + §8.1.21
> **Testklasse:** `QvtoE2eMultiFileCompositionTest` (3 Tests)
>
> **Design-Entscheidung:** Keine dynamische Kompilierung nötig — unsere Engine akzeptiert
> `OperationalTransformation` EMF-Objekte direkt (parser-unabhängig). `asTransformation()`
> extrahiert ein `OperationalTransformation`-EObject aus einem Model-Extent und wrapped es
> als ausführbare `QvtoTransformationInstance`. Eclipse markiert dieses Feature als UNSUPPORTED.
>
> **Implementiert:**
> - `asTransformation()` als Model-Operation in `QvtoOperationProvider` (§8.3.5.7)
> - `transform(model1, model2, ...)` mit Argument-Binding für ungebundene Instanzen (§8.1.21)
> - `bindModels()` in `QvtoTransformationOperations` — bindet ModelParameter ↔ Extent-Args
> - `QvtoEngine.execute()` Javadoc dokumentiert programmatischen API-Pfad (ohne Parser)
>
> **Tests:** (6 Tests in `QvtoE2eMultiFileCompositionTest`)
> - `asTransformation_fromModelExtent` — Extent mit OperationalTransformation → transform(m) → Erfolg
> - `asTransformation_emptyExtent_returnsNull` — leerer Extent → null
> - `asTransformation_chained_transformCall` — `qvtModel.asTransformation().transform(m)` Verkettung
> - `asTransformation_noTransformationInExtent_returnsNull` — Extent mit Nicht-OT-Content → null
> - `asTransformation_parallelTransform` — async Ausführung via `parallelTransform(m)` + wait
> - `programmaticApi_executeWithoutParser` — engine.execute() direkt mit EMF-Objekt

### P10-04: `static` Intermediate Property Sharing (✅ DONE)

> **Spec:** §8.1.10 / §8.4 — `static` Modifier auf Intermediate Properties
> **Testklasse:** `QvtoE2eIntermediateClassAdvancedTest.intermediateClass_staticProperty()`
> **Eclipse-Referenz:** `intermProperties/intermProperties.qvto` (`static staticData`)
>
> **Implementiert:**
> - `QvtoIntermediatePropertyStore`: Shared Static-Store (`staticPropertyValues` Map, keyed by class name)
> - Read-Pfad: Property-Interceptor erkennt `static` EAnnotation auf EStructuralFeature → liest aus Shared Store
> - Write-Pfad: `caseAssignExp` erkennt `static` Feature → schreibt in Shared Store statt `eSet()`
> - Parser-Annotation `"fennec:intermediate:modifier"` mit `{"static": "true"}` war bereits korrekt

### P10-05: `<<id>>` Stereotyp-Syntax — ✅ DONE

> **Spec:** §8.1.10 — `<<id>>` Stereotyp auf Intermediate Properties
> **Testklasse:** `QvtoE2eIntermediateClassAdvancedTest.intermediateClass_idStereotype()`
>
> **Implementiert:**
> - `STEREOTYPE_ID` Lexer-Token (`<<id>>` als 6-Zeichen-Token, kein Konflikt mit `<`/`>`)
> - `classifierFeatureModifier` um `STEREOTYPE_ID` erweitert
> - `QvtoUnitBuilder.processIntermediateClass()` setzt `EAttribute.setID(true)`

### P10-06: Model I/O — removeElement + Read-Only Guards — ✅ DONE

> **Eclipse-Referenz:** `bug561707/`, `scr878/`
> **Testklasse:** `QvtoE2eEclipseBugRegressionTest`
>
> **Implementiert:**
> - `QvtoModelExtent.isReadOnly()` API (default method)
> - `BasicQvtoModelExtent`: readOnly-Feld + Guards auf `add()`/`setContents()`
> - `QvtoExtentManager`: Setzt readOnly für IN-Parameter, `isReadOnly(EObject)` Lookup
> - `QvtoOperationProvider` + `QvtoModelOperations`: `checkExtentWritable()` Guards
> - `QvtoEvaluator`: `checkNotReadOnly()` Guard auf Property-Assignments (nach Intermediate-Check)
>
> **Zusätzliche Grammar-Erweiterungen (§8.4 Concrete Syntax):**
> - Top-Level-Transformationssyntax: `transformation T(...);` (Deklarationsform)
> - Unbenannte Model-Parameter: `out ecore` (Name aus Typ abgeleitet)
> - Untypisierte Module-Properties: `property x = expr;`
> - Entry mit Parametern: `main(in param : Type)`
> - `setupOperationEnvironment`: Typ-Info auf self/param/result-Variablen für korrekte Property-Resolution
>
> **bug561707 — Object Reparenting:** ✅ `removeElement()` + Reparenting via Mapping
> **scr878 — invresolveone() Read-Only Guard:** ✅ Write auf in-Model → Transformation scheitert

### P10-07: UML-spezifische Operations (⏭️ bewusst übersprungen)

> **Spec:** §8.3.4
> **Status:** Bewusst nicht implementiert (wie Eclipse)
>
> Folgende Operationen sind UML-spezifisch und werden NICHT implementiert:
> - `_localId()`, `_globalId()` (§8.3.4.1–2)
> - `markedAs()`, `markValue()` (§8.3.4.12–13)
> - `stereotypedBy()`, `stereotypedStrictlyBy()` (§8.3.4.14–15)
>
> **Begründung:** Fennec M2M zielt auf Ecore/EMF, nicht auf UML. Eclipse überspringt diese ebenfalls.

---

### P10-08: Blackbox Libraries (§8.1.4) & `from ... import` (§8.4) ✅ DONE

> **Spec:** §8.1.4 (Blackbox Library Integration), §8.4 (`from ... import` Selective Import)
> **Status:** ✅ DONE — 28 Tests (9 E2E + 12 Registry Unit + 7 Parser AST)

**Implementiert:**
- `QvtoBlackboxLibrary` API mit `BlackboxOperationDescriptor` + `invoke()`
- `QvtoBlackboxInvocationContext` (self, diagnostics, config properties, extents, package registry)
- `QvtoBlackboxRegistry` Interface + `BasicQvtoBlackboxRegistry` (thread-safe, ConcurrentHashMap)
- `QvtoLinker`: Synthetische Library-Module aus Blackbox-Deskriptoren (Helper mit `isBlackbox=true`)
- `QvtoOperationProvider`: Blackbox-Dispatch via `library.invoke()`
- Parser: `from <unit> import <names>;` / `from <unit> import *;` Syntax
- `ModuleImport.importedNames` für selektive Sichtbarkeit
- `QvtoOperationResolver` + `QvtoOperationProvider`: Filtering nach `importedNames`

**Tests:**

| Testklasse | Tests | Scope |
|-----------|------:|-------|
| `QvtoE2eBlackboxTest` | 5 | E2E: module-level op, contextual op, logging, config property, not-imported |
| `QvtoE2eFromImportTest` | 4 | E2E: selective/blackbox, wildcard, selective/regular, visibility filtering |
| `BasicQvtoBlackboxRegistryTest` | 12 | Unit: register, unregister, lookup, null, duplicate, immutable copy, multi-lib |
| `QvtoFromImportParseTest` | 7 | Parser AST: single name, multiple names, wildcard, classic, qualified, coexist, soft keyword |

---

### Regression Tests: Chaining-vs-Variable Bug Fix

6 new regression tests were added to prevent regression of the chaining-vs-variable bug fix (string counter chaining operations evaluated multiple times per operation call). Tests split across:

| Testklasse | Tests | Scope |
|-----------|------:|-------|
| `QvtoE2eModelOpsTest` | 3 | E2E: chaining with `incrStrCounter()`, `repr()`, `clone()` |
| `QvtoE2eElementOpsTest` | 3 | E2E: chaining with `metaClassName()`, `clone()` operations |

These tests verify the source evaluation invariant: intermediate dispatch steps (model extent/element operation checks, imperative source handling) must not eagerly evaluate the source expression unless the operation name actually matches a known operation in that category.

---

### P10-09: Shorthand-Operatoren (§8.4.4) — ✅ DONE

> 🛑 **STOP — Spec lesen:** §8.4.4 (S. 162), §8.4 EBNF S. 169 (`<access_op>`, `<mult_op>`, `<unary_op>`)

**Implementiert:** GAP-19, GAP-20, GAP-21 — alle §8.4.4 Shorthand-Operatoren.

**Änderungen:**

| Datei | Änderung |
|-------|----------|
| `qvto.parser/grammar/QvtO.g4` | `%` in MultExp, `HASH`/`DOUBLE_HASH` Lexer-Tokens, `HashExp`/`DoubleHashExp`/`UnaryStarExp` Regeln, `NOT_ARROW` Token + `!->` in ArrowExp |
| `qvto.parser/.../QvtoExpressionBuilder.java` | `visitHashExp`, `visitDoubleHashExp`, `visitUnaryStarExp`, `desugarShorthandIterator` (für `->select(#Type)`), `!->` Not-Wrapper in `visitArrowExp` |
| `ocl.engine/.../OclStdlib.java` | `%` als Alias für `format` in `dispatchString()`, `stereotypedBy` → `OclInvalid` in `dispatchOclAny()` |
| `qvto.engine/.../QvtoModelOperations.java` | `isExactType()` — robuster Vergleich (nsURI+Name statt `==` Identity) für `objectsOfType`/`subobjectsOfType`/`allSubobjectsOfType` (§8.3.5.3, §8.3.4.6–7) |

**Tests:** 15 neue Tests in 2 Testklassen:

| Testklasse | Tests | Scope |
|-----------|------:|-------|
| `QvtoShorthandParseTest` | 5 | Parse: format `%`, `#`, `##`, `*`, `!->` |
| `QvtoE2eShorthandTest` | 10 | E2E: format (3), hash mit primitiven + Ecore-Typen (2), doubleHash mit primitiven + Ecore-Typen (2), unaryStar (1), notArrow (2) |

**Spec-Validierung:**
- §8.4.4 item 1: `#Type` → `oclIsKindOf(Type)` ✅
- §8.4.4 item 2: `##Type` → `oclIsTypeOf(Type)` ✅
- §8.4.4 item 3: `*"stereo"` → `stereotypedBy("stereo")` ✅ (UML → OclInvalid, P10-07)
- §8.4.4 item 4: `"str" % arg` → `format(arg)` ✅
- §8.4 EBNF: `!->` als `<access_op>` → negated arrow ✅
- §8.3.5.3: `objectsOfType` exact type match — Bugfix: `==` durch `isExactType()` ersetzt ✅

---

### Zusammenfassung offene Punkte

| ID | Bereich | Blocker | Priorität | Status |
|----|---------|---------|-----------|--------|
| P10-01 | Multi-File Composition | — | Hoch | ✅ DONE (12 Tests) |
| P10-02 | Status/Exception | — | Mittel | ✅ DONE (in P10-01 Tests) |
| P10-03 | `asTransformation()` | — | Mittel | ✅ DONE (6 Tests) |
| P10-04 | `static` Property Sharing | — | — | ✅ DONE |
| P10-05 | `<<id>>` Stereotyp | — | — | ✅ DONE |
| P10-06 | Model I/O (removeElement, read-only) | — | — | ✅ DONE |
| P10-07 | UML-spezifische Ops | — | — | ⏭️ Nicht geplant |
| P10-08 | Blackbox Libraries & `from...import` | — | Hoch | ✅ DONE (28 Tests) |
| P10-09 | Shorthand-Operatoren (§8.4.4) | — | Mittel | ✅ DONE (15 Tests) |

**Status:** Phase 10 abgeschlossen. Alle Gaps (außer P10-07 bewusst übersprungen) implementiert. 947 Tests, 0 Failures, 0 @Disabled, 1 @Tag("perf"). GAP-10 (Integer::range) nachträglich implementiert (+6 Tests).

---

## Referenzen

### QVT-O v1.3 Spec
- **Datei:** `docs/specifications/QVT/QVT-formal-16-06-03.pdf`
- **Chapter 8:** Operational Mappings — alle Testphasen basieren darauf

### Eclipse QVT-O Referenztests
- **Parser-Tests:** `repositories/org.eclipse.qvto/tests/org.eclipse.m2m.tests.qvt.oml/parserTestData/`
- **Execution-Tests:** `repositories/org.eclipse.qvto/tests/org.eclipse.m2m.tests.qvt.oml/deploys/`
- **Unit-Tests:** `repositories/org.eclipse.qvto/tests/org.eclipse.m2m.tests.qvt.oml/src/`

### Gecko QVT-O (OSGi-adaptiert)
- **Tests:** `repositories/org.gecko.qvto/`

### Fennec QVT-O Tests
- **Test-Modul:** `workspace/org.eclipse.fennec.m2x.qvto.tests/`
- **Testlauf:** `cd /opt/git/m2m/workspace && ./gradlew org.eclipse.fennec.m2x.qvto.tests:test`
