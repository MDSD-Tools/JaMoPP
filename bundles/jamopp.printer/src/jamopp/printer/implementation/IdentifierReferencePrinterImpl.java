package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.generics.TypeArgumentable;
import org.emftext.language.java.references.IdentifierReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class IdentifierReferencePrinterImpl implements Printer<IdentifierReference> {

	private final Printer<Annotable> annotablePrinter;
	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<TypeArgumentable> typeArgumentablePrinter;

	@Inject
	public IdentifierReferencePrinterImpl(Printer<Annotable> annotablePrinter,
			Printer<TypeArgumentable> typeArgumentablePrinter, Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		this.annotablePrinter = annotablePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(IdentifierReference element, BufferedWriter writer) throws IOException {
		this.annotablePrinter.print(element, writer);
		if (element.getTarget() instanceof org.emftext.language.java.containers.Package) {
			var pack = (org.emftext.language.java.containers.Package) element.getTarget();
			writer.append(pack.getNamespaces().get(pack.getNamespaces().size() - 1));
		} else {
			writer.append(element.getTarget().getName());
		}
		this.typeArgumentablePrinter.print(element, writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
