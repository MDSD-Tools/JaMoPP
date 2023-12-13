package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;

import com.google.inject.Inject;

import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;

public class ArrayDimensionsPrinter implements ArrayDimensionsPrinterInt {

	private final AnnotablePrinter AnnotablePrinter;

	@Inject
	public ArrayDimensionsPrinter(AnnotablePrinter annotablePrinter) {
		AnnotablePrinter = annotablePrinter;
	}

	@Override
	public void print(List<ArrayDimension> element, BufferedWriter writer) throws IOException {
		for (ArrayDimension dim : element) {
			if (!dim.getAnnotations().isEmpty()) {
				writer.append(" ");
				AnnotablePrinter.print(dim, writer);
			}
			writer.append("[] ");
		}
	}

}
