package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.arrays.ArrayDimension;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ArrayDimensionsPrinterImpl implements Printer<List<ArrayDimension>> {

	private final Printer<Annotable> annotablePrinter;

	@Inject
	public ArrayDimensionsPrinterImpl(Printer<Annotable> annotablePrinter) {
		this.annotablePrinter = annotablePrinter;
	}

	@Override
	public void print(List<ArrayDimension> element, BufferedWriter writer) throws IOException {
		for (ArrayDimension dim : element) {
			if (!dim.getAnnotations().isEmpty()) {
				writer.append(" ");
				this.annotablePrinter.print(dim, writer);
			}
			writer.append("[] ");
		}
	}

}
