package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.generics.TypeArgumentable;
import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class IdentifierReferencePrinterImpl implements Printer<IdentifierReference> {

	private final Printer<Annotable> annotablePrinter;
	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<TypeArgumentable> typeArgumentablePrinter;

	@Inject
	public IdentifierReferencePrinterImpl(final Printer<Annotable> annotablePrinter,
			final Printer<TypeArgumentable> typeArgumentablePrinter,
			final Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		this.annotablePrinter = annotablePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(final IdentifierReference element, final BufferedWriter writer) throws IOException {
		annotablePrinter.print(element, writer);
		if (element.getTarget() instanceof tools.mdsd.jamopp.model.java.containers.Package) {
			final var pack = (tools.mdsd.jamopp.model.java.containers.Package) element.getTarget();
			writer.append(pack.getNamespaces().get(pack.getNamespaces().size() - 1));
		} else {
			writer.append(element.getTarget().getName());
		}
		typeArgumentablePrinter.print(element, writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
