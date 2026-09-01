# OCL Example: Ecore Annotations and a Complete OCL Document

A worked, plain-Java OCL example over real files (#205) — the same metamodel served two ways,
which is exactly the boundary the example is about:

| | |
|---|---|
| `model/library.ecore` | metamodel **with OCL in delegate annotations**: the `validYear` invariant, the derived `bookCount`, the `booksBy(author)` operation body |
| `model/library.xmi` | instance model — six books (one violates `validYear`), a shelf tree, two members |
| `documents/library.ocl` | a **Complete OCL document** for the same metamodel: `def:`, `derive:`, `body:`, `inv:` — the `.ecore` stays untouched |

Three ways to run it, from this directory:

- **`DelegatesExample`** — the annotation path. After `installDelegates()` no OCL API appears:
  `Diagnostician` reports the `validYear` violation, `eGet` computes `bookCount`, `eInvoke`
  runs `booksBy`.
- **`AdHocExample`** — the document path. `loadDocument(...)` puts the `.ocl` file into effect:
  `def:` features answer in ad-hoc expressions (plus `closure` over the shelf tree and tuples
  over the catalog), `derive:`/`body:` become visible to OCL evaluation, `inv:` feeds the
  `OclDocumentValidator`.
- **`CompiledUnitExample`** — the document as a compiled unit: compile, store, prepare,
  `registerCompleteOclDocument(prepared, name)`. A document is installed, never executed, so
  for OCL the unit lifecycle ends at register — the registering engine never sees the source.

## The boundary, on purpose

A registered document contributes to **OCL evaluation and the `OclDocumentValidator`** — it does
not feed EMF's own `eGet`/`eInvoke`/`Diagnostician`. That path reads delegate annotations only
(#204, decided scope). The example makes both sides visible: `displayName` has a `derive:` in the
document, so `evaluate("self.displayName")` answers `Herman Melville - Moby Dick` while `eGet`
still answers the raw `MD-0001`; Grace's four borrowed books trip the document's `borrowLimit`
in the `OclDocumentValidator` while `Diagnostician` sees nothing wrong with her.

One syntax note: `library` is a keyword in the document grammar (imports), so the package
heading is the escaped `package _'library'`.

`OclExampleSmokeTest` runs all three paths in CI. The unit mechanism is described in
[`docs/compiled-units-guide.md`](../docs/compiled-units-guide.md).
