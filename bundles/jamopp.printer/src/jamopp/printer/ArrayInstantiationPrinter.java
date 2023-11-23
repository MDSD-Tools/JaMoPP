package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.arrays.ArrayInstantiation;
import org.emftext.language.java.arrays.ArrayInstantiationBySize;
import org.emftext.language.java.arrays.ArrayInstantiationByValuesTyped;
import org.emftext.language.java.arrays.ArrayInstantiationByValuesUntyped;
import org.emftext.language.java.expressions.Expression;

public class ArrayInstantiationPrinter {

	static void printArrayInstantiation(ArrayInstantiation element, BufferedWriter writer) throws IOException {
		if (element instanceof ArrayInstantiationBySize inst) {
			writer.append("new ");
			TypeReferencePrinter.printTypeReference(inst.getTypeReference(), writer);
			TypeArgumentablePrinter.printTypeArgumentable(inst, writer);
			writer.append(" ");
			for (Expression expr : inst.getSizes()) {
				writer.append("[");
				ExpressionPrinter.printExpression(expr, writer);
				writer.append("] ");
			}
			ArrayDimensionsPrinter.printArrayDimensions(inst.getArrayDimensionsBefore(), writer);
			ArrayDimensionsPrinter.printArrayDimensions(inst.getArrayDimensionsAfter(), writer);
		} else if (element instanceof ArrayInstantiationByValuesUntyped inst) {
			ArrayInitializerPrinter.printArrayInitializer(inst.getArrayInitializer(), writer);
		} else {
			ArrayInstantiationByValuesTyped inst = (ArrayInstantiationByValuesTyped) element;
			writer.append("new ");
			TypeReferencePrinter.printTypeReference(inst.getTypeReference(), writer);
			TypeArgumentablePrinter.printTypeArgumentable(inst, writer);
			ArrayDimensionsPrinter.printArrayDimensions(inst.getArrayDimensionsBefore(), writer);
			ArrayDimensionsPrinter.printArrayDimensions(inst.getArrayDimensionsAfter(), writer);
			writer.append(" ");
			ArrayInitializerPrinter.printArrayInitializer(inst.getArrayInitializer(), writer);
		}
	}

}
