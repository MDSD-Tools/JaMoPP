package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InstanceOfExpression;

class InstanceOfExpressionPrinter {

	static void print(InstanceOfExpression element, BufferedWriter writer)
			throws IOException {
		InstanceOfExpressionChildPrinter.print(element.getChild(), writer);
		writer.append(" instanceof ");
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
