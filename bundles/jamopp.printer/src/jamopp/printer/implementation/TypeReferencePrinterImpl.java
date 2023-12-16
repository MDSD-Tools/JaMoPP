package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.InferableType;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.PrimitiveType;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import jamopp.printer.interfaces.EmptyPrinter;
import jamopp.printer.interfaces.Printer;

public class TypeReferencePrinterImpl implements Printer<TypeReference> {

	private final Printer<ClassifierReference> classifierReferencePrinter;
	private final EmptyPrinter inferableTypePrinter;
	private final Printer<NamespaceClassifierReference> namespaceClassifierReferencePrinter;
	private final Printer<PrimitiveType> primitiveTypePrinter;

	@Inject
	public TypeReferencePrinterImpl(Printer<NamespaceClassifierReference> namespaceClassifierReferencePrinter,
			Printer<ClassifierReference> classifierReferencePrinter, Printer<PrimitiveType> primitiveTypePrinter,
			@Named("InferableTypePrinte") EmptyPrinter inferableTypePrinter) {
		this.namespaceClassifierReferencePrinter = namespaceClassifierReferencePrinter;
		this.classifierReferencePrinter = classifierReferencePrinter;
		this.primitiveTypePrinter = primitiveTypePrinter;
		this.inferableTypePrinter = inferableTypePrinter;
	}

	@Override
	public void print(TypeReference element, BufferedWriter writer) throws IOException {
		if (element instanceof NamespaceClassifierReference) {
			this.namespaceClassifierReferencePrinter.print((NamespaceClassifierReference) element, writer);
		} else if (element instanceof ClassifierReference) {
			this.classifierReferencePrinter.print((ClassifierReference) element, writer);
		} else if (element instanceof PrimitiveType) {
			this.primitiveTypePrinter.print((PrimitiveType) element, writer);
		} else if (element instanceof InferableType) {
			this.inferableTypePrinter.print(writer);
		}
	}

}
