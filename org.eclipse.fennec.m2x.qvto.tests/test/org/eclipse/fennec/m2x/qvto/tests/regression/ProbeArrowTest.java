package org.eclipse.fennec.m2x.qvto.tests.regression;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.Test;

class ProbeArrowTest {

	@Test
	void probe() throws Exception {
		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build()).build());
		CompiledUnit compiled = engine.compile("""
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE) {
				    main() {
				        m.objectsOfType(EPackage)->forEach(p) {
				            log('p is ' + p.repr());
				            log('oclAsSet size ' + p.oclAsSet()->size().repr());
				            p.name := p->size().repr();
				        };
				    }
				}
				""", "Main");
		System.out.println("PROBE fresh: " + arrows(compiled));
		System.out.println("PROBE fresh vars: " + vars(compiled));
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		UnitKey key = store.store("qvto", new PackagedUnit(compiled));
		CompiledUnit reloaded = ((PackagedUnit) store.load(key).orElseThrow()).document();
		System.out.println("PROBE loaded: " + arrows(reloaded));
		System.out.println("PROBE loaded vars: " + vars(reloaded));
		System.out.println("PROBE fresh run: " + runOf(engine, compiled));
		System.out.println("PROBE loaded run: " + runOf(engine, reloaded));
	}

	private static String runOf(QvtoEngine engine, CompiledUnit unit) {
		EObject pkg = org.eclipse.emf.ecore.util.EcoreUtil.create(
				org.eclipse.emf.ecore.EcorePackage.Literals.EPACKAGE);
		pkg.eSet(org.eclipse.emf.ecore.EcorePackage.Literals.ENAMED_ELEMENT__NAME, "Original");
		var result = engine.execute(
				(org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation) unit.getUnit(),
				org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext.of(
						new org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent(java.util.List.of(pkg))));
		return result.diagnostics().toString();
	}

	private static String vars(EObject root) {
		StringBuilder out = new StringBuilder();
		for (Iterator<EObject> it = root.eAllContents(); it.hasNext();) {
			EObject node = it.next();
			if (node instanceof org.eclipse.fennec.m2x.model.ocl.VariableExp ve
					&& ve.getReferredVariable() != null
					&& "p".equals(ve.getReferredVariable().getName())) {
				var v = ve.getReferredVariable();
				out.append("use->var#").append(System.identityHashCode(v))
						.append(v.eContainer() == null ? "(uncontained)" : "(in " + v.eContainer().eClass().getName() + ")")
						.append(' ');
			}
			if (node instanceof org.eclipse.fennec.m2x.model.ocl.Variable v && "p".equals(v.getName())) {
				out.append("decl#").append(System.identityHashCode(v))
						.append("(in ").append(v.eContainer() == null ? "null" : v.eContainer().eClass().getName()).append(") ");
			}
		}
		return out.toString();
	}

	private static String arrows(EObject root) {
		StringBuilder out = new StringBuilder();
		for (Iterator<EObject> it = root.eAllContents(); it.hasNext();) {
			EObject node = it.next();
			if (node instanceof OperationCallExp op && "size".equals(op.getName())) {
				Object src = op.getOwnedSource();
				String varInfo = "?";
				if (src instanceof org.eclipse.fennec.m2x.model.ocl.VariableExp ve) {
					var v = ve.getReferredVariable();
					varInfo = v == null ? "no variable"
							: v.getName() + ":" + (v.getType() == null ? "no type"
									: v.getType().getName() + "@" + (v.getType().eIsProxy() ? "proxy" : "resolved"));
				}
				out.append("size(source=").append(src == null ? "null" : src.getClass().getSimpleName())
						.append(", var=").append(varInfo)
						.append(", type=").append(op.getType() == null ? "null" : op.getType().getName())
						.append(") ");
			}
		}
		return out.isEmpty() ? "(no size call)" : out.toString();
	}
}
