package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.InferableType;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import tools.mdsd.jamopp.printer.interfaces.EmptyPrinter;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class TypeReferencePrinterImpl implements Printer<TypeReference> {

	private final Printer<ClassifierReference> classifierReferencePrinter;
	private final EmptyPrinter inferableTypePrinter;
	private final Printer<NamespaceClassifierReference> namespaceClassifierReferencePrinter;
	private final Printer<PrimitiveType> primitiveTypePrinter;

	@Inject
	public TypeReferencePrinterImpl(Printer<NamespaceClassifierReference> namespaceClassifierReferencePrinter,
			Printer<ClassifierReference> classifierReferencePrinter, Printer<PrimitiveType> primitiveTypePrinter,
			@Named("InferableTypePrinter") EmptyPrinter inferableTypePrinter) {
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
