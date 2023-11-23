package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;

public class ArrayDimensionsPrinter {

	static void printArrayDimensions(List<ArrayDimension> element, BufferedWriter writer) throws IOException {
		for (ArrayDimension dim : element) {
			if (!dim.getAnnotations().isEmpty()) {
				writer.append(" ");
				AnnotablePrinter.printAnnotable(dim, writer);
			}
			writer.append("[] ");
		}
	}

}
