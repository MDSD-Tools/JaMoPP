package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.arrays.ArraySelector;

class ArraySelectorPrinter {

	static void print(ArraySelector element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		writer.append("[");
		ExpressionPrinter.print(element.getPosition(), writer);
		writer.append("]");
	}

}
