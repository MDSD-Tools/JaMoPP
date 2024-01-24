package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.InferableType;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.EmptyPrinter;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class TypeReferencePrinterImpl implements Printer<TypeReference> {

	private final Printer<ClassifierReference> classifierReferencePrinter;
	private final EmptyPrinter inferableTypePrinter;
	private final Printer<NamespaceClassifierReference> namespaceClassifierReferencePrinter;
	private final Printer<PrimitiveType> primitiveTypePrinter;

	@Inject
	public TypeReferencePrinterImpl(final Printer<NamespaceClassifierReference> namespaceClassifierReferencePrinter,
			final Printer<ClassifierReference> classifierReferencePrinter,
			final Printer<PrimitiveType> primitiveTypePrinter,
			@Named("InferableTypePrinter") final EmptyPrinter inferableTypePrinter) {
		this.namespaceClassifierReferencePrinter = namespaceClassifierReferencePrinter;
		this.classifierReferencePrinter = classifierReferencePrinter;
		this.primitiveTypePrinter = primitiveTypePrinter;
		this.inferableTypePrinter = inferableTypePrinter;
	}

	@Override
	public void print(final TypeReference element, final BufferedWriter writer) throws IOException {
		if (element instanceof NamespaceClassifierReference) {
			namespaceClassifierReferencePrinter.print((NamespaceClassifierReference) element, writer);
		} else if (element instanceof ClassifierReference) {
			classifierReferencePrinter.print((ClassifierReference) element, writer);
		} else if (element instanceof PrimitiveType) {
			primitiveTypePrinter.print((PrimitiveType) element, writer);
		} else if (element instanceof InferableType) {
			inferableTypePrinter.print(writer);
		}
	}

}
