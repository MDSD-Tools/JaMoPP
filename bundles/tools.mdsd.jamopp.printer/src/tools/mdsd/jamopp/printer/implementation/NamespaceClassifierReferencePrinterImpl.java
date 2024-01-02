package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class NamespaceClassifierReferencePrinterImpl implements Printer<NamespaceClassifierReference> {

	private final Printer<ClassifierReference> classifierReferencePrinter;

	@Inject
	public NamespaceClassifierReferencePrinterImpl(Printer<ClassifierReference> classifierReferencePrinter) {
		this.classifierReferencePrinter = classifierReferencePrinter;
	}

	@Override
	public void print(NamespaceClassifierReference element, BufferedWriter writer) throws IOException {
		writer.append(element.getNamespacesAsString());
		if (!element.getNamespaces().isEmpty()) {
			writer.append(".");
		}
		for (var index = 0; index < element.getClassifierReferences().size() - 1; index++) {
			this.classifierReferencePrinter.print(element.getClassifierReferences().get(index), writer);
			writer.append(".");
		}
		this.classifierReferencePrinter
				.print(element.getClassifierReferences().get(element.getClassifierReferences().size() - 1), writer);
	}

}
