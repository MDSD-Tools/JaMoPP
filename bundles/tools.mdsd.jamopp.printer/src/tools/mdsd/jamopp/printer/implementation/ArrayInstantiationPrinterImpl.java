package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.arrays.ArrayInitializer;
import tools.mdsd.jamopp.model.java.arrays.ArrayInstantiation;
import tools.mdsd.jamopp.model.java.arrays.ArrayInstantiationBySize;
import tools.mdsd.jamopp.model.java.arrays.ArrayInstantiationByValuesTyped;
import tools.mdsd.jamopp.model.java.arrays.ArrayInstantiationByValuesUntyped;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.generics.TypeArgumentable;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ArrayInstantiationPrinterImpl implements Printer<ArrayInstantiation> {

	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<ArrayInitializer> arrayInitializerPrinter;
	private final Printer<Expression> expressionPrinter;
	private final Printer<TypeArgumentable> typeArgumentablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public ArrayInstantiationPrinterImpl(Printer<TypeReference> typeReferencePrinter,
			Printer<TypeArgumentable> typeArgumentablePrinter, Printer<Expression> expressionPrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter, Printer<ArrayInitializer> arrayInitializerPrinter) {
		this.typeReferencePrinter = typeReferencePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
		this.expressionPrinter = expressionPrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
		this.arrayInitializerPrinter = arrayInitializerPrinter;
	}

	@Override
	public void print(ArrayInstantiation element, BufferedWriter writer) throws IOException {
		if (element instanceof ArrayInstantiationBySize inst) {
			writer.append("new ");
			typeReferencePrinter.print(inst.getTypeReference(), writer);
			typeArgumentablePrinter.print(inst, writer);
			writer.append(" ");
			for (Expression expr : inst.getSizes()) {
				writer.append("[");
				expressionPrinter.print(expr, writer);
				writer.append("] ");
			}
			arrayDimensionsPrinter.print(inst.getArrayDimensionsBefore(), writer);
			arrayDimensionsPrinter.print(inst.getArrayDimensionsAfter(), writer);
		} else if (element instanceof ArrayInstantiationByValuesUntyped inst) {
			arrayInitializerPrinter.print(inst.getArrayInitializer(), writer);
		} else {
			var inst = (ArrayInstantiationByValuesTyped) element;
			writer.append("new ");
			typeReferencePrinter.print(inst.getTypeReference(), writer);
			typeArgumentablePrinter.print(inst, writer);
			arrayDimensionsPrinter.print(inst.getArrayDimensionsBefore(), writer);
			arrayDimensionsPrinter.print(inst.getArrayDimensionsAfter(), writer);
			writer.append(" ");
			arrayInitializerPrinter.print(inst.getArrayInitializer(), writer);
		}
	}

}
