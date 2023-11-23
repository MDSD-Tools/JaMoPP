package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.variables.AdditionalLocalVariable;

class AdditionalLocalVariablePrinter {

	static void print(AdditionalLocalVariable element, BufferedWriter writer) throws IOException {
		writer.append(element.getName());
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		if (element.getInitialValue() != null) {
			writer.append(" = ");
			ExpressionPrinter.print(element.getInitialValue(), writer);
		}
	}

}
