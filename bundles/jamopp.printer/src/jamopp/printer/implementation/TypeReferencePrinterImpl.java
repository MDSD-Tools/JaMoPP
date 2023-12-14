package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.InferableType;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.PrimitiveType;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.InferableTypePrinterInt;
import jamopp.printer.interfaces.Printer;

public class TypeReferencePrinterImpl implements Printer<TypeReference> {

	private final Printer<ClassifierReference> ClassifierReferencePrinter;
	private final InferableTypePrinterInt InferableTypePrinter;
	private final Printer<NamespaceClassifierReference> NamespaceClassifierReferencePrinter;
	private final Printer<PrimitiveType> PrimitiveTypePrinter;

	@Inject
	public TypeReferencePrinterImpl(Printer<NamespaceClassifierReference> namespaceClassifierReferencePrinter,
			Printer<ClassifierReference> classifierReferencePrinter, Printer<PrimitiveType> primitiveTypePrinter,
			InferableTypePrinterInt inferableTypePrinter) {
		NamespaceClassifierReferencePrinter = namespaceClassifierReferencePrinter;
		ClassifierReferencePrinter = classifierReferencePrinter;
		PrimitiveTypePrinter = primitiveTypePrinter;
		InferableTypePrinter = inferableTypePrinter;
	}

	@Override
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
