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
	public ArrayInstantiationPrinterImpl(final Printer<TypeReference> typeReferencePrinter,
			final Printer<TypeArgumentable> typeArgumentablePrinter, final Printer<Expression> expressionPrinter,
			final Printer<List<ArrayDimension>> arrayDimensionsPrinter,
			final Printer<ArrayInitializer> arrayInitializerPrinter) {
		this.typeReferencePrinter = typeReferencePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
		this.expressionPrinter = expressionPrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
		this.arrayInitializerPrinter = arrayInitializerPrinter;
	}

	@Override
	public void print(final ArrayInstantiation element, final BufferedWriter writer) throws IOException {
		if (element instanceof final ArrayInstantiationBySize inst) {
			printArrayInstantiationBySize(writer, inst);
		} else if (element instanceof final ArrayInstantiationByValuesUntyped inst) {
			printArrayInstantiationByValuesUntyped(writer, inst);
		} else {
			printArrayInstantiationByValuesTyped(element, writer);
		}
	}

	private void printArrayInstantiationByValuesTyped(final ArrayInstantiation element, final BufferedWriter writer)
			throws IOException {
		final ArrayInstantiationByValuesTyped inst = (ArrayInstantiationByValuesTyped) element;
		writer.append("new ");
		typeReferencePrinter.print(inst.getTypeReference(), writer);
		typeArgumentablePrinter.print(inst, writer);
		arrayDimensionsPrinter.print(inst.getArrayDimensionsBefore(), writer);
		arrayDimensionsPrinter.print(inst.getArrayDimensionsAfter(), writer);
		writer.append(" ");
		arrayInitializerPrinter.print(inst.getArrayInitializer(), writer);
	}

	private void printArrayInstantiationByValuesUntyped(final BufferedWriter writer,
			final ArrayInstantiationByValuesUntyped inst) throws IOException {
		arrayInitializerPrinter.print(inst.getArrayInitializer(), writer);
	}

	private void printArrayInstantiationBySize(final BufferedWriter writer, final ArrayInstantiationBySize inst)
			throws IOException {
		writer.append("new ");
		typeReferencePrinter.print(inst.getTypeReference(), writer);
		typeArgumentablePrinter.print(inst, writer);
		writer.append(" ");
		for (final Expression expr : inst.getSizes()) {
			writer.append("[");
			expressionPrinter.print(expr, writer);
			writer.append("] ");
		}
		arrayDimensionsPrinter.print(inst.getArrayDimensionsBefore(), writer);
		arrayDimensionsPrinter.print(inst.getArrayDimensionsAfter(), writer);
	}

}
