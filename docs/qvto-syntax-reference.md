# QVT-O Syntax-Referenz (verifiziert gegen Eclipse QVT-O)

Quelle: Eclipse QVT-O LPG-Grammatik (`ImperativeOCL.gi`) + Testdateien

## forEach (via Arrow-Operator)

```
collection->forEach(var) { body }
collection->forEach(var1, var2) { body }
collection->forEach(var | condition) { body }
collection->forEach(var1, var2 | condition) { body }
```

- `->forEach` ist ein Iterator via Arrow-Operator, KEIN standalone Statement
- `forEach(var; collection)` ist UNGÜLTIG
- Grammatik: `IteratorExpCS ::= primaryExpCS '->' forExpCS`

## forOne

Gleiche Syntax wie forEach, aber stoppt nach erstem Match:
```
collection->forOne(var) { body }
collection->forOne(var | condition) { body }
```

## Object Expression

Direkte Feature-Zuweisung OHNE Prefix (`self.` oder `result.` nicht nötig):
```qvto
object EClass {
    name := 'mapped' + self.name;
}
object x : EPackage {
    name := 'p1';
    eClassifiers += self.eClassifiers->map c2c();
}
```

## When Clause

Einzelner OCL-Ausdruck, KEIN Semikolon:
```qvto
mapping EClass::toClass() : Class when { not self.interface } { ... }
mapping X::m() : Y when { self.name <> null and self.name.startsWith('A') } { ... }
```

## Resolve Syntax

```qvto
resolve(Type)                                    -- alle, implizite Source
resolveone(Type)                                 -- eins, implizite Source
obj.resolve(Type)                                -- explizite Source
resolve(t : Type | condition)                    -- mit Variable + Bedingung
resolveIn(ClassName::mappingName, Type)           -- in bestimmtem Mapping
resolveIn(ClassName::mappingName, t : Type | cond)
invresolve(Type)                                 -- inverse
invresolveone(Type)
late resolve(Type)                               -- deferred
late resolveone(Type)
late resolveIn(ClassName::mappingName, Type)
```

## Imperative if (mit Block)

QVT-O hat BEIDES:
- OCL-Expression: `if cond then expr else expr endif`
- Imperativ mit Blöcken: `if (cond) then { stmts } else { stmts } endif`

```qvto
if (ro.oclIsKindOf(Family::Family)) then {
    object Family::Family {};
    log("It is a Family");
} else {
    log("Not a family");
} endif;
```
