package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.arrays.ArrayInstantiation;
import org.emftext.language.java.arrays.ArrayInstantiationBySize;
import org.emftext.language.java.arrays.ArrayInstantiationByValuesTyped;
import org.emftext.language.java.arrays.ArrayInstantiationByValuesUntyped;
import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;
import jamopp.printer.interfaces.printer.ArrayInitializerPrinterInt;
import jamopp.printer.interfaces.printer.ArrayInstantiationPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class ArrayInstantiationPrinter implements ArrayInstantiationPrinterInt {

	private final TypeReferencePrinterInt TypeReferencePrinter;
	private final TypeArgumentablePrinterInt TypeArgumentablePrinter;
	private final ExpressionPrinterInt ExpressionPrinter;
	private final ArrayDimensionsPrinterInt ArrayDimensionsPrinter;
	private final ArrayInitializerPrinterInt ArrayInitializerPrinter;

	@Inject
	public ArrayInstantiationPrinter(TypeReferencePrinterInt typeReferencePrinter,
			TypeArgumentablePrinterInt typeArgumentablePrinter, ExpressionPrinterInt expressionPrinter,
			ArrayDimensionsPrinterInt arrayDimensionsPrinter, ArrayInitializerPrinterInt arrayInitializerPrinter) {
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
