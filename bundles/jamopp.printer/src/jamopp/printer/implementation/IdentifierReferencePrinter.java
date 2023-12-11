package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.IdentifierReference;

import com.google.inject.Inject;

public class IdentifierReferencePrinter {

	private final AnnotablePrinter AnnotablePrinter;
	private final TypeArgumentablePrinter TypeArgumentablePrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;

	@Inject
	public IdentifierReferencePrinter(jamopp.printer.implementation.AnnotablePrinter annotablePrinter,
			jamopp.printer.implementation.TypeArgumentablePrinter typeArgumentablePrinter,
			jamopp.printer.implementation.ArrayDimensionsPrinter arrayDimensionsPrinter) {
		super();
		AnnotablePrinter = annotablePrinter;
		TypeArgumentablePrinter = typeArgumentablePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	public void printIdentifierReference(IdentifierReference element, BufferedWriter writer) throws IOException {
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
