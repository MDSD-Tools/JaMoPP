package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.CastExpression;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;
import jamopp.printer.interfaces.printer.CastExpressionPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class CastExpressionPrinter implements CastExpressionPrinterInt {

	private final TypeReferencePrinterInt TypeReferencePrinter;
	private final ArrayDimensionsPrinterInt ArrayDimensionsPrinter;
	private final ExpressionPrinterInt ExpressionPrinter;

	@Inject
	public CastExpressionPrinter(TypeReferencePrinterInt typeReferencePrinter,
			ArrayDimensionsPrinterInt arrayDimensionsPrinter, ExpressionPrinterInt expressionPrinter) {
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
