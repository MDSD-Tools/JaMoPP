package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InstanceOfExpression;

public class InstanceOfExpressionPrinter {

	static void printInstanceOfExpression(InstanceOfExpression element, BufferedWriter writer)
			throws IOException {
		InstanceOfExpressionChildPrinter.printInstanceOfExpressionChild(element.getChild(), writer);
		writer.append(" instanceof ");
		TypeReferencePrinter.printTypeReference(element.getTypeReference(), writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsAfter(), writer);
	}

}
