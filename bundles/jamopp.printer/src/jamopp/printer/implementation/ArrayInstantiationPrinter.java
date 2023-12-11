package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.arrays.ArrayInstantiation;
import org.emftext.language.java.arrays.ArrayInstantiationBySize;
import org.emftext.language.java.arrays.ArrayInstantiationByValuesTyped;
import org.emftext.language.java.arrays.ArrayInstantiationByValuesUntyped;
import org.emftext.language.java.expressions.Expression;

import jamopp.printer.interfaces.Printer;

class ArrayInstantiationPrinter implements Printer<ArrayInstantiation> {

	static void print(ArrayInstantiation element, BufferedWriter writer) throws IOException {
		if (element instanceof ArrayInstantiationBySize inst) {
			writer.append("new ");
			TypeReferencePrinter.print(inst.getTypeReference(), writer);
			TypeArgumentablePrinter.print(inst, writer);
			writer.append(" ");
			for (Expression expr : inst.getSizes()) {
				writer.append("[");
				ExpressionPrinter.print(expr, writer);
				writer.append("] ");
			}
			ArrayDimensionsPrinter.print(inst.getArrayDimensionsBefore(), writer);
			ArrayDimensionsPrinter.print(inst.getArrayDimensionsAfter(), writer);
		} else if (element instanceof ArrayInstantiationByValuesUntyped inst) {
			ArrayInitializerPrinter.print(inst.getArrayInitializer(), writer);
		} else {
			ArrayInstantiationByValuesTyped inst = (ArrayInstantiationByValuesTyped) element;
			writer.append("new ");
			TypeReferencePrinter.print(inst.getTypeReference(), writer);
			TypeArgumentablePrinter.print(inst, writer);
			ArrayDimensionsPrinter.print(inst.getArrayDimensionsBefore(), writer);
			ArrayDimensionsPrinter.print(inst.getArrayDimensionsAfter(), writer);
			writer.append(" ");
			ArrayInitializerPrinter.print(inst.getArrayInitializer(), writer);
		}
	}

}
