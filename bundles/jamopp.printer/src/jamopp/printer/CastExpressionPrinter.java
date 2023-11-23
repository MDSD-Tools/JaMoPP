package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.CastExpression;
import org.emftext.language.java.types.TypeReference;

class CastExpressionPrinter {

	static void print(CastExpression element, BufferedWriter writer) throws IOException {
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
