package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.expressions.CastExpression;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class CastExpressionPrinterImpl implements Printer<CastExpression> {

	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<Expression> expressionPrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public CastExpressionPrinterImpl(final Printer<TypeReference> typeReferencePrinter,
			final Printer<List<ArrayDimension>> arrayDimensionsPrinter, final Printer<Expression> expressionPrinter) {
		this.typeReferencePrinter = typeReferencePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(final CastExpression element, final BufferedWriter writer) throws IOException {
		writer.append("(");
		typeReferencePrinter.print(element.getTypeReference(), writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		for (final TypeReference ref : element.getAdditionalBounds()) {
			writer.append(" & ");
			typeReferencePrinter.print(ref, writer);
		}
		writer.append(") ");
		expressionPrinter.print(element.getGeneralChild(), writer);
	}

}
