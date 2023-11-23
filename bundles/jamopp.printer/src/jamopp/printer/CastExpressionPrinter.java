package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.CastExpression;
import org.emftext.language.java.types.TypeReference;

public class CastExpressionPrinter {

	static void printCastExpression(CastExpression element, BufferedWriter writer) throws IOException {
		writer.append("(");
		TypeReferencePrinter.printTypeReference(element.getTypeReference(), writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsAfter(), writer);
		for (TypeReference ref : element.getAdditionalBounds()) {
			writer.append(" & ");
			TypeReferencePrinter.printTypeReference(ref, writer);
		}
		writer.append(") ");
		ExpressionPrinter.printExpression(element.getGeneralChild(), writer);
	}

}
