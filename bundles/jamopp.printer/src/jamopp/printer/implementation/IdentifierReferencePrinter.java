package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.containers.Package;
import org.emftext.language.java.references.IdentifierReference;

public class IdentifierReferencePrinter {

	private final AnnotablePrinter AnnotablePrinter;
	private final TypeArgumentablePrinter TypeArgumentablePrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;
	
	public void printIdentifierReference(IdentifierReference element, BufferedWriter writer)
			throws IOException {
		AnnotablePrinter.print(element, writer);
		if (element.getTarget() instanceof org.emftext.language.java.containers.Package) {
			org.emftext.language.java.containers.Package pack = (org.emftext.language.java.containers.Package) element
					.getTarget();
			writer.append(pack.getNamespaces().get(pack.getNamespaces().size() - 1));
		} else {
			writer.append(element.getTarget().getName());
		}
		TypeArgumentablePrinter.print(element, writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
