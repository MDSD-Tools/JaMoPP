package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.InferableType;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.PrimitiveType;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.ClassifierReferencePrinterInt;
import jamopp.printer.interfaces.printer.InferableTypePrinterInt;
import jamopp.printer.interfaces.printer.NamespaceClassifierReferencePrinterInt;
import jamopp.printer.interfaces.printer.PrimitiveTypePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class TypeReferencePrinterImpl implements TypeReferencePrinterInt {

	private final NamespaceClassifierReferencePrinterInt NamespaceClassifierReferencePrinter;
	private final ClassifierReferencePrinterInt ClassifierReferencePrinter;
	private final PrimitiveTypePrinterInt PrimitiveTypePrinter;
	private final InferableTypePrinterInt InferableTypePrinter;

	@Inject
	public TypeReferencePrinterImpl(NamespaceClassifierReferencePrinterInt namespaceClassifierReferencePrinter,
			ClassifierReferencePrinterInt classifierReferencePrinter, PrimitiveTypePrinterInt primitiveTypePrinter,
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
