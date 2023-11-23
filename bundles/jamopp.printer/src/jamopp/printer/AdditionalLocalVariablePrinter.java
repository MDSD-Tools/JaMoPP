package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.variables.AdditionalLocalVariable;

public class AdditionalLocalVariablePrinter {

	static void printAdditionalLocalVariable(AdditionalLocalVariable element, BufferedWriter writer)
			throws IOException {
		writer.append(element.getName());
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsAfter(), writer);
		if (element.getInitialValue() != null) {
			writer.append(" = ");
			ExpressionPrinter.printExpression(element.getInitialValue(), writer);
		}
	}

}
