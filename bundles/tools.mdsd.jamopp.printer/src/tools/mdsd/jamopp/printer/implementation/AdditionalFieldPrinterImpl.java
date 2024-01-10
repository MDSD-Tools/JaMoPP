package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AdditionalFieldPrinterImpl implements Printer<AdditionalField> {

	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<Expression> expressionPrinter;

	@Inject
	public AdditionalFieldPrinterImpl(Printer<Expression> expressionPrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(AdditionalField element, BufferedWriter writer) throws IOException {
		writer.append(element.getName());
		arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		if (element.getInitialValue() != null) {
			writer.append(" = ");
			expressionPrinter.print(element.getInitialValue(), writer);
		}
	}

}
