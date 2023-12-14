package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.IdentifierReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.printer.AnnotablePrinterInt;
import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;
import jamopp.printer.interfaces.printer.IdentifierReferencePrinterInt;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;

public class IdentifierReferencePrinterImpl implements IdentifierReferencePrinterInt {

	private final AnnotablePrinterInt AnnotablePrinter;
	private final TypeArgumentablePrinterInt TypeArgumentablePrinter;
	private final ArrayDimensionsPrinterInt ArrayDimensionsPrinter;

	@Inject
	public IdentifierReferencePrinterImpl(AnnotablePrinterInt annotablePrinter,
			TypeArgumentablePrinterInt typeArgumentablePrinter, ArrayDimensionsPrinterInt arrayDimensionsPrinter) {
		AnnotablePrinter = annotablePrinter;
		TypeArgumentablePrinter = typeArgumentablePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(IdentifierReference element, BufferedWriter writer) throws IOException {
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
