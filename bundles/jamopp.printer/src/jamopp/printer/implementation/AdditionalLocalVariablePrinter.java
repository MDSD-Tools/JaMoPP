package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.variables.AdditionalLocalVariable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AdditionalLocalVariablePrinterInt;
import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;

public class AdditionalLocalVariablePrinter implements AdditionalLocalVariablePrinterInt {

	private final ArrayDimensionsPrinterInt ArrayDimensionsPrinter;
	private final ExpressionPrinterInt ExpressionPrinter;

	@Inject
	public AdditionalLocalVariablePrinter(ArrayDimensionsPrinterInt arrayDimensionsPrinter,
			ExpressionPrinterInt expressionPrinter) {
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
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
