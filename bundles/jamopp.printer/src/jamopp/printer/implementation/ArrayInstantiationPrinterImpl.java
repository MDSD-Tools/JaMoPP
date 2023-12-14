package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.arrays.ArrayInitializer;
import org.emftext.language.java.arrays.ArrayInstantiation;
import org.emftext.language.java.arrays.ArrayInstantiationBySize;
import org.emftext.language.java.arrays.ArrayInstantiationByValuesTyped;
import org.emftext.language.java.arrays.ArrayInstantiationByValuesUntyped;
import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class ArrayInstantiationPrinterImpl implements Printer<ArrayInstantiation> {

	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;
	private final Printer<ArrayInitializer> ArrayInitializerPrinter;
	private final Printer<Expression> ExpressionPrinter;
	private final TypeArgumentablePrinterInt TypeArgumentablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;

	@Inject
	public ArrayInstantiationPrinterImpl(TypeReferencePrinterInt typeReferencePrinter,
			TypeArgumentablePrinterInt typeArgumentablePrinter, Printer<Expression> expressionPrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter, Printer<ArrayInitializer> arrayInitializerPrinter) {
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
			var inst = (ArrayInstantiationByValuesTyped) element;
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
