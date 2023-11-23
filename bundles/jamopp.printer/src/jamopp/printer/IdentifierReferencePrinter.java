package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.containers.Package;
import org.emftext.language.java.references.IdentifierReference;

public class IdentifierReferencePrinter {

	static void printIdentifierReference(IdentifierReference element, BufferedWriter writer)
			throws IOException {
		AnnotablePrinter.printAnnotable(element, writer);
		if (element.getTarget() instanceof org.emftext.language.java.containers.Package) {
			org.emftext.language.java.containers.Package pack = (org.emftext.language.java.containers.Package) element
					.getTarget();
			writer.append(pack.getNamespaces().get(pack.getNamespaces().size() - 1));
		} else {
			writer.append(element.getTarget().getName());
		}
		TypeArgumentablePrinter.printTypeArgumentable(element, writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsAfter(), writer);
	}

}
