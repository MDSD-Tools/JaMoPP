package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.AdditionalField;
import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AdditionalFieldPrinterInt;

class AdditionalFieldPrinter implements  AdditionalFieldPrinterInt {

	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;
	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public AdditionalFieldPrinter(ExpressionPrinter expressionPrinter, ArrayDimensionsPrinter arrayDimensionsPrinter) {
		this.ArrayDimensionsPrinter = arrayDimensionsPrinter;
		this.ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(AdditionalField element, BufferedWriter writer) throws IOException {
		writer.append(element.getName());
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		if (element.getInitialValue() != null) {
			writer.append(" = ");
			ExpressionPrinter.print(element.getInitialValue(), writer);
		}
	}

}
