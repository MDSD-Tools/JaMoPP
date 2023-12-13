package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.CastExpression;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.CastExpressionPrinterInt;

class CastExpressionPrinter implements Printer<CastExpression>, CastExpressionPrinterInt {

	private final TypeReferencePrinter TypeReferencePrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;
	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public CastExpressionPrinter(jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter,
			jamopp.printer.implementation.ArrayDimensionsPrinter arrayDimensionsPrinter,
			jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		TypeReferencePrinter = typeReferencePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(CastExpression element, BufferedWriter writer) throws IOException {
		writer.append("(");
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		for (TypeReference ref : element.getAdditionalBounds()) {
			writer.append(" & ");
			TypeReferencePrinter.print(ref, writer);
		}
		writer.append(") ");
		ExpressionPrinter.print(element.getGeneralChild(), writer);
	}

}
