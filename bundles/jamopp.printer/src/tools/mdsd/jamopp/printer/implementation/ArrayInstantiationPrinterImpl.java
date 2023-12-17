package tools.mdsd.jamopp.printer.implementation;

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
import org.emftext.language.java.generics.TypeArgumentable;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

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
			this.typeReferencePrinter.print(inst.getTypeReference(), writer);
			this.typeArgumentablePrinter.print(inst, writer);
			writer.append(" ");
			for (Expression expr : inst.getSizes()) {
				writer.append("[");
				this.expressionPrinter.print(expr, writer);
				writer.append("] ");
			}
			this.arrayDimensionsPrinter.print(inst.getArrayDimensionsBefore(), writer);
			this.arrayDimensionsPrinter.print(inst.getArrayDimensionsAfter(), writer);
		} else if (element instanceof ArrayInstantiationByValuesUntyped inst) {
			this.arrayInitializerPrinter.print(inst.getArrayInitializer(), writer);
		} else {
			var inst = (ArrayInstantiationByValuesTyped) element;
			writer.append("new ");
			this.typeReferencePrinter.print(inst.getTypeReference(), writer);
			this.typeArgumentablePrinter.print(inst, writer);
			this.arrayDimensionsPrinter.print(inst.getArrayDimensionsBefore(), writer);
			this.arrayDimensionsPrinter.print(inst.getArrayDimensionsAfter(), writer);
			writer.append(" ");
			this.arrayInitializerPrinter.print(inst.getArrayInitializer(), writer);
		}
	}

}
