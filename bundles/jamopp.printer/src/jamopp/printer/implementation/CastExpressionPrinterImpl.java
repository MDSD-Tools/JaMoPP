package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.expressions.CastExpression;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class CastExpressionPrinterImpl implements Printer<CastExpression> {

	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<Expression> expressionPrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public CastExpressionPrinterImpl(Printer<TypeReference> typeReferencePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter, Printer<Expression> expressionPrinter) {
		this.typeReferencePrinter = typeReferencePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(CastExpression element, BufferedWriter writer) throws IOException {
		writer.append("(");
		this.typeReferencePrinter.print(element.getTypeReference(), writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		for (TypeReference ref : element.getAdditionalBounds()) {
			writer.append(" & ");
			this.typeReferencePrinter.print(ref, writer);
		}
		writer.append(") ");
		this.expressionPrinter.print(element.getGeneralChild(), writer);
	}

}
