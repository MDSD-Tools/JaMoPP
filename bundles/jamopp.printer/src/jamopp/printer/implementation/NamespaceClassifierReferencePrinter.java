package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.types.NamespaceClassifierReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.NamespaceClassifierReferencePrinterInt;

public class NamespaceClassifierReferencePrinter implements NamespaceClassifierReferencePrinterInt {

	private final ClassifierReferencePrinter ClassifierReferencePrinter;

	@Inject
	public NamespaceClassifierReferencePrinter(
			ClassifierReferencePrinter classifierReferencePrinter) {
		super();
		ClassifierReferencePrinter = classifierReferencePrinter;
	}

	@Override
	public void print(NamespaceClassifierReference element, BufferedWriter writer) throws IOException {
		writer.append(element.getNamespacesAsString());
		if (!element.getNamespaces().isEmpty()) {
			writer.append(".");
		}
		for (int index = 0; index < element.getClassifierReferences().size() - 1; index++) {
			ClassifierReferencePrinter.print(element.getClassifierReferences().get(index), writer);
			writer.append(".");
		}
		ClassifierReferencePrinter
				.print(element.getClassifierReferences().get(element.getClassifierReferences().size() - 1), writer);
	}

}
