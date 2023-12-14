package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.NamespaceClassifierReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class NamespaceClassifierReferencePrinterImpl implements Printer<NamespaceClassifierReference> {

	private final Printer<ClassifierReference> ClassifierReferencePrinter;

	@Inject
	public NamespaceClassifierReferencePrinterImpl(Printer<ClassifierReference> classifierReferencePrinter) {
		ClassifierReferencePrinter = classifierReferencePrinter;
	}

	@Override
	public void print(NamespaceClassifierReference element, BufferedWriter writer) throws IOException {
		writer.append(element.getNamespacesAsString());
		if (!element.getNamespaces().isEmpty()) {
			writer.append(".");
		}
		for (var index = 0; index < element.getClassifierReferences().size() - 1; index++) {
			ClassifierReferencePrinter.print(element.getClassifierReferences().get(index), writer);
			writer.append(".");
		}
		ClassifierReferencePrinter
		.print(element.getClassifierReferences().get(element.getClassifierReferences().size() - 1), writer);
	}

}
