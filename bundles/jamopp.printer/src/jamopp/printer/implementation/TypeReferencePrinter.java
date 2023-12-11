package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.InferableType;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.PrimitiveType;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class TypeReferencePrinter implements Printer<TypeReference> {

	private final NamespaceClassifierReferencePrinter NamespaceClassifierReferencePrinter;
	private final ClassifierReferencePrinter ClassifierReferencePrinter;
	private final PrimitiveTypePrinter PrimitiveTypePrinter;
	private final InferableTypePrinter InferableTypePrinter;

	@Inject
	public TypeReferencePrinter(
			jamopp.printer.implementation.NamespaceClassifierReferencePrinter namespaceClassifierReferencePrinter,
			jamopp.printer.implementation.ClassifierReferencePrinter classifierReferencePrinter,
			jamopp.printer.implementation.PrimitiveTypePrinter primitiveTypePrinter,
			jamopp.printer.implementation.InferableTypePrinter inferableTypePrinter) {
		super();
		NamespaceClassifierReferencePrinter = namespaceClassifierReferencePrinter;
		ClassifierReferencePrinter = classifierReferencePrinter;
		PrimitiveTypePrinter = primitiveTypePrinter;
		InferableTypePrinter = inferableTypePrinter;
	}

	public void print(TypeReference element, BufferedWriter writer) throws IOException {
		if (element instanceof NamespaceClassifierReference) {
			NamespaceClassifierReferencePrinter.print((NamespaceClassifierReference) element, writer);
		} else if (element instanceof ClassifierReference) {
			ClassifierReferencePrinter.print((ClassifierReference) element, writer);
		} else if (element instanceof PrimitiveType) {
			PrimitiveTypePrinter.print((PrimitiveType) element, writer);
		} else if (element instanceof InferableType) {
			InferableTypePrinter.print(writer);
		}
	}

}
