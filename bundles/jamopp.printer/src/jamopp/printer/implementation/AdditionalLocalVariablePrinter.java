package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.variables.AdditionalLocalVariable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class AdditionalLocalVariablePrinter implements Printer<AdditionalLocalVariable> {

	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;
	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public AdditionalLocalVariablePrinter(jamopp.printer.implementation.ArrayDimensionsPrinter arrayDimensionsPrinter,
			jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
		ExpressionPrinter = expressionPrinter;
	}

	public void print(AdditionalLocalVariable element, BufferedWriter writer) throws IOException {
		writer.append(element.getName());
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		if (element.getInitialValue() != null) {
			writer.append(" = ");
			ExpressionPrinter.print(element.getInitialValue(), writer);
		}
	}

}
