package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.InferableType;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.PrimitiveType;
import org.emftext.language.java.types.TypeReference;

public class TypeReferencePrinter {

	static void printTypeReference(TypeReference element, BufferedWriter writer) throws IOException {
		if (element instanceof NamespaceClassifierReference) {
			NamespaceClassifierReferencePrinter.printNamespaceClassifierReference((NamespaceClassifierReference) element, writer);
		} else if (element instanceof ClassifierReference) {
			ClassifierReferencePrinter.printClassifierReference((ClassifierReference) element, writer);
		} else if (element instanceof PrimitiveType) {
			PrimitiveTypePrinter.printPrimitiveType((PrimitiveType) element, writer);
		} else if (element instanceof InferableType) {
			InferableTypePrinter.printInferableType(writer);
		}
	}

}
