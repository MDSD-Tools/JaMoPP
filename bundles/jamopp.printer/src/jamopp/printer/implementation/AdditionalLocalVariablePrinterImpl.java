package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.variables.AdditionalLocalVariable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

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
