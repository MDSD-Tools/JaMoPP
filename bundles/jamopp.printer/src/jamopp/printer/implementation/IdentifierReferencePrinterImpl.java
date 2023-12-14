package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.references.IdentifierReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;

public class IdentifierReferencePrinterImpl implements Printer<IdentifierReference> {

	private final Printer<Annotable> AnnotablePrinter;
	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;
	private final TypeArgumentablePrinterInt TypeArgumentablePrinter;

	@Inject
	public IdentifierReferencePrinterImpl(Printer<Annotable> annotablePrinter,
			TypeArgumentablePrinterInt typeArgumentablePrinter, Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		AnnotablePrinter = annotablePrinter;
		TypeArgumentablePrinter = typeArgumentablePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(IdentifierReference element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		if (element.getTarget() instanceof org.emftext.language.java.containers.Package) {
			var pack = (org.emftext.language.java.containers.Package) element
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
