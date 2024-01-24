package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ArrayDimensionsPrinterImpl implements Printer<List<ArrayDimension>> {

	private final Printer<Annotable> annotablePrinter;

	@Inject
	public ArrayDimensionsPrinterImpl(final Printer<Annotable> annotablePrinter) {
		this.annotablePrinter = annotablePrinter;
	}

	@Override
	public void print(final List<ArrayDimension> element, final BufferedWriter writer) throws IOException {
		for (final ArrayDimension dim : element) {
			if (!dim.getAnnotations().isEmpty()) {
				writer.append(" ");
				annotablePrinter.print(dim, writer);
			}
			writer.append("[] ");
		}
	}

}
