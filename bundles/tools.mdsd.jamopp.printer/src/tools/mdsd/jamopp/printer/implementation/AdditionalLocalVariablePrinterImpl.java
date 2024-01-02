package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AdditionalLocalVariablePrinterImpl implements Printer<AdditionalLocalVariable> {

	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<Expression> expressionPrinter;

	@Inject
	public AdditionalLocalVariablePrinterImpl(Printer<List<ArrayDimension>> arrayDimensionsPrinter,
			Printer<Expression> expressionPrinter) {
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(AdditionalLocalVariable element, BufferedWriter writer) throws IOException {
		writer.append(element.getName());
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		if (element.getInitialValue() != null) {
			writer.append(" = ");
			this.expressionPrinter.print(element.getInitialValue(), writer);
		}
	}

}
