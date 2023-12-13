package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.arrays.ArrayInstantiation;
import org.emftext.language.java.arrays.ArrayInstantiationBySize;
import org.emftext.language.java.arrays.ArrayInstantiationByValuesTyped;
import org.emftext.language.java.arrays.ArrayInstantiationByValuesUntyped;
import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ArrayInstantiationPrinterInt;

class ArrayInstantiationPrinter implements ArrayInstantiationPrinterInt {

	private final TypeReferencePrinter TypeReferencePrinter;
	private final TypeArgumentablePrinter TypeArgumentablePrinter;
	private final ExpressionPrinter ExpressionPrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;
	private final ArrayInitializerPrinter ArrayInitializerPrinter;

	@Inject
	public ArrayInstantiationPrinter(jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter,
			jamopp.printer.implementation.TypeArgumentablePrinter typeArgumentablePrinter,
			jamopp.printer.implementation.ExpressionPrinter expressionPrinter,
			jamopp.printer.implementation.ArrayDimensionsPrinter arrayDimensionsPrinter,
			jamopp.printer.implementation.ArrayInitializerPrinter arrayInitializerPrinter) {
		super();
		TypeReferencePrinter = typeReferencePrinter;
		TypeArgumentablePrinter = typeArgumentablePrinter;
		ExpressionPrinter = expressionPrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
		ArrayInitializerPrinter = arrayInitializerPrinter;
	}

	@Override
	public void print(ArrayInstantiation element, BufferedWriter writer) throws IOException {
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
