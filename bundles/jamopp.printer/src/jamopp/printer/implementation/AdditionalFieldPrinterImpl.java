package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.members.AdditionalField;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class AdditionalFieldPrinterImpl implements Printer<AdditionalField> {

	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;
	private final Printer<Expression> ExpressionPrinter;

	@Inject
	public AdditionalFieldPrinterImpl(Printer<Expression> expressionPrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
		ExpressionPrinter = expressionPrinter;
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
