package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.arrays.ArraySelector;

public class ArraySelectorPrinter {

	static void printArraySelector(ArraySelector element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.printAnnotable(element, writer);
		writer.append("[");
		ExpressionPrinter.printExpression(element.getPosition(), writer);
		writer.append("]");
	}

}
