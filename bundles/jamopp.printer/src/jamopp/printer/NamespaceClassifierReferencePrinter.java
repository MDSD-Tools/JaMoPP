package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.types.NamespaceClassifierReference;

public class NamespaceClassifierReferencePrinter {

	static void printNamespaceClassifierReference(NamespaceClassifierReference element, BufferedWriter writer) throws IOException {
		writer.append(element.getNamespacesAsString());
		if (!element.getNamespaces().isEmpty()) {
			writer.append(".");
		}
		for (int index = 0; index < element.getClassifierReferences().size() - 1; index++) {
			ClassifierReferencePrinter.printClassifierReference(element.getClassifierReferences().get(index), writer);
			writer.append(".");
		}
		ClassifierReferencePrinter.printClassifierReference(element.getClassifierReferences().get(element.getClassifierReferences().size() - 1), writer);
	}

}
