package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.expressions.InstanceOfExpression;
import tools.mdsd.jamopp.model.java.expressions.InstanceOfExpressionChild;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class InstanceOfExpressionPrinterImpl implements Printer<InstanceOfExpression> {

	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<InstanceOfExpressionChild> instanceOfExpressionChildPrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public InstanceOfExpressionPrinterImpl(Printer<InstanceOfExpressionChild> instanceOfExpressionChildPrinter,
			Printer<TypeReference> typeReferencePrinter, Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		this.instanceOfExpressionChildPrinter = instanceOfExpressionChildPrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(InstanceOfExpression element, BufferedWriter writer) throws IOException {
		this.instanceOfExpressionChildPrinter.print(element.getChild(), writer);
		writer.append(" instanceof ");
		this.typeReferencePrinter.print(element.getTypeReference(), writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
