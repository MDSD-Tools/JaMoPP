package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;

import jamopp.printer.interfaces.Printer;

class ArrayDimensionsPrinter implements Printer<List<ArrayDimension>> {

	private final AnnotablePrinter AnnotablePrinter;
	
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
